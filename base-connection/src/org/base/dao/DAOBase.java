package org.base.dao;

import org.base.dao.searches.IDataMappingStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.exceptions.system.SystemException;
import org.base.dao.searches.modules.IDataSearchModule;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.RegistryDataSourceContext;
import org.base.dao.datasources.context.IDataSourceContextInjectable;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryNotFound;
import org.base.dao.exceptions.ExceptionDBForeignKey;
import org.base.dao.exceptions.ExceptionDBNonExistingReference;
import org.base.dao.filters.IFilter;

/**
 *
 * @author martin
 */
public abstract class DAOBase implements IDAO {
    
    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE VARIABLES">
    DataSourceContext dataSourceContext;
    
    /**
     * The data mapping strategy.
     * This object is used every time an object is retrieved from the database (unless another data 
     * mapping strategy object is explicitly specified) and is intended to construct an object from
     * a query result.
     */
    private IDataMappingStrategy toObjectMapper;
    
    /**
     * This mapper overrides the behavior of the toObjectMapper object during only one retrieval. Once 
     * set and used, it is set back to null. This means that when you set a temporal mapper, it will be used
     * instead of the toObjectMapper object in the construction of objects after the next retrieval from
     * the database (unless you manually set it to null again).
     */
    private IDataMappingStrategy toObjectMapperTemporal = null;
    
    /**
     * A list of filters to be applied every time an object is retrieved from the database (except in the
     * retrievals where a query is explicitly specified, e.g. in findMany(IDataSearchModule searchModule)
     * function).
     */
    private List<IFilter> filters;
    private List<IFilter> endingFilters;
    
    /**
     * This class is used to hold data that will be passed through some methods to carry out executions of
     * statements in the database; in many cases, an object of this class is handy for this purpose.
     */
    protected class StatementData {
        String query;
        Object[] params;

        public StatementData(String query, Object[] params) {
            this.query = query;
            this.params = params;
        }

        public Object[] getParams() {
            return params;
        }

        public String getQuery() {
            return query;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public DAOBase(String dataSourceContextName, IDataMappingStrategy toObjectMapper) {
        super();
        this.dataSourceContext = RegistryDataSourceContext.getDataSourceContext(dataSourceContextName);
        // TODO: validate dataSourceContext against null value
        this.toObjectMapper = toObjectMapper;
        filters = new ArrayList<IFilter>();
        endingFilters = new ArrayList<IFilter>();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="METODOS PROPIOS">
    protected void setDataMappingStrategy(IDataMappingStrategy toObjectMapper) {
        this.toObjectMapper = toObjectMapper;
    }
    
    /*public static String getSchema() {
        return ConexionJDBC.getSchema();
    }*/
    
    protected String getContextualTableName() {
        return dataSourceContext.getDBObjectExpression(getTableName());
    }
    
    protected String getContextualTableName(String tableName) {
        return dataSourceContext.getDBObjectExpression(tableName);
    }
    
    protected String getContextualObjectName(String objectName) {
        return dataSourceContext.getDBObjectExpression(objectName);
    }
    
    protected String getContextualFunctionName(String fnName) {
        return dataSourceContext.getDBObjectExpression(fnName);
    }
    
    protected Connection getContextualConnection() {
        return dataSourceContext.getConnection();
        //return ConexionJDBC.getConnection();
    }
    
    protected void closeContextualConnection(Connection conn) {
        dataSourceContext.close(conn);
    }
    
    protected void closeContextualStatement(Statement stm) {
        dataSourceContext.close(stm);
    }
    
    protected void useTemporalMapper(IDataMappingStrategy tempMapper) {
        toObjectMapperTemporal = tempMapper;
    }
    
    protected boolean isSetTemporalMapper() {
        return toObjectMapperTemporal != null;
    }
    
    protected void resetTemporalMapper() {
        toObjectMapperTemporal = null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS ABSTRACTOS">
    /**
     * Returns the table name where the objects this DAO represents is to be stored.
     */
    public abstract String getTableName();
    
    /**
     * Returns the names of the key fields of the table where the objects this DAO represents are to be stored.
     * The fields names string should be a representation of the names of the fields separated by the '|' character.
     * An example of a valid string is: "keyFieldA|keyFieldB".
     */
    public abstract String getKeyFields();
    
    /**
     * Returns the values for the key fields specified in getKeyFields() method.
     * The fields values string should be a representation of the values of the fields separated by the '|' character.
     * An example of a valid string is (a string|integer value pair): "stringValue|25".
     * Key fields names expression and key fields values expression should match fields and values in a one to one basis.
     */
    protected abstract Object getKeyValueExpression(Object obj);
    
    /**
     * Returns a map containing (field name, value) pairs. This map is used during the object insertion,
     * so all mandatory fields must be contained in the map with its value specified.
     * @param obj The object from which to extract the values for the fields in the map.
     */
    protected abstract Map<String, Object> getInsertionMap(Object obj);
    
    /**
     * Returns a map containing (field name, value) pairs. This map is used during the object update.
     * Specify only the fields you want to update with their corresponding values.
     * @param obj The object from which to extract the values for the fields in the map.
     */
    protected abstract Map<String, Object> getUpdateMap(Object obj);
    
    /**
     * Returns a select query specifying the data to be retrieved in order to construct an object
     * of the type this DAO represents. The result of the query is passed in the form of a ResultSet 
     * object to the object that encapsulates the data mapping strategy, i.e. the toObjectMapper object.
     */
    protected abstract String getFindAllStatement();
    // </editor-fold>   
    
    // <editor-fold defaultstate="collapsed" desc="METODOS INTERFACE">
    /**
     * Inserts a given object on the database relying on specifications of this object's data.
     * The object's data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism.
     */
    @Override
    public void insert(Object objModelo) throws ExceptionDBDuplicateEntry {
        insert(objModelo, getContextualConnection());
    }
    
    protected void insert(Object objModelo, Connection conn) throws ExceptionDBDuplicateEntry {
        PreparedStatement pstm = null;
        try {
            StatementData stmtData = createInsertionData(getInsertionMap(objModelo));
            pstm = prepareStatement(stmtData.getQuery(), stmtData.getParams(), conn);
            executeStatement(pstm, conn);
        } catch (ExceptionDBForeignKey ex) {
            throw new ExceptionDBNonExistingReference();
        }
    }
    
    /**
     * Inserts a list of objects on the database relying on specifications of the objects' data.
     * The objects' data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism. The objects are inserted in
     * the same batch, so this method is suitable for the repetitive insertion of multiple objects.
     */
    @Override
    public void insert(List lstModelos) throws ExceptionDBDuplicateEntry {
        try {
            List<Object[]> matrizParametros = new ArrayList<Object[]>(lstModelos.size());
            String query = null;
            for (Object object : lstModelos) {
                StatementData stmtData = createInsertionData(getInsertionMap(object));
                matrizParametros.add(stmtData.getParams());
                query = stmtData.getQuery();
            }
            ejecutarSentenciaBatch(query, matrizParametros);
        } catch (ExceptionDBForeignKey ex) {
            throw new SystemException(ex);
        }
    }
    
    /**
     * Updates a given object on the database relying on specifications of this object's data.
     * The object's data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism.
     */
    @Override
    public void update(Object objModelo) throws ExceptionDBEntryNotFound, ExceptionDBForeignKey {
        update(objModelo, getContextualConnection());                
    }
    
    protected void update(Object objModelo, Connection conn) throws ExceptionDBEntryNotFound, ExceptionDBForeignKey {
        PreparedStatement pstm = null;       
        try {   
            StatementData stmtData = createUpdateData(getKeyValueExpression(objModelo), getUpdateMap(objModelo));
            pstm = prepareStatement(stmtData.getQuery(), stmtData.getParams(), conn);
            int intRegistros = executeStatement(pstm, conn);
            if (intRegistros == 0) {
                throw new ExceptionDBEntryNotFound();
            }
        } catch (ExceptionDBDuplicateEntry ex) {
            throw new SystemException(ex);
        }
    }
    
    /**
     * Updates a list of objects on the database relying on specifications of the objects' data.
     * The objects' data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism. The objects are updated in
     * the same batch, so this method is suitable for the repetitive update of multiple objects.
     */
    @Override
    public void update(List lstModelos) throws ExceptionDBEntryNotFound, ExceptionDBForeignKey {
        try {
            List<Object[]> matrizParametros = new ArrayList<Object[]>(lstModelos.size());
            String query = null;
            for (Object object : lstModelos) {
                StatementData stmtData = createUpdateData(getKeyValueExpression(object), getUpdateMap(object));
                matrizParametros.add(stmtData.getParams());
                query = stmtData.getQuery();
            }
            ejecutarSentenciaBatch(query, matrizParametros);
        } catch (ExceptionDBDuplicateEntry ex) {
            throw new SystemException(ex);
        }
    }

    /**
     * Deletes a given object from the database relying on specifications of this object's data.
     * The object's data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism.
     */
    @Override
    public void remove(Object objModelo) throws ExceptionDBEntryNotFound, ExceptionDBForeignKey {
        remove(objModelo, getContextualConnection());
    }
    
    
    protected void remove(Object objModelo, Connection conn) throws ExceptionDBEntryNotFound, ExceptionDBForeignKey {
        String condicion = prepareConditionWithKey(getKeyValueExpression(objModelo));
        PreparedStatement pstm = null;
        try { 
            pstm = prepareStatement("DELETE FROM " + getContextualTableName() + " WHERE " + condicion, null, conn);
            int nRegistries = executeStatement(pstm, conn);
            if (nRegistries == 0) {
                throw new ExceptionDBEntryNotFound();
            }
        } catch (ExceptionDBDuplicateEntry ex) {
            throw new SystemException(ex);
        } 
    }
    
    @Override
    public void remove(List lstModelos) throws ExceptionDBEntryNotFound, ExceptionDBForeignKey {
    }

    @Override
    public Object findOne(Object valorLlave) {
        Connection conn = getContextualConnection();
        if(isSetTemporalMapper()) {
            Object result = findByKeys(valorLlave, conn, toObjectMapperTemporal);
            resetTemporalMapper();
            return result;
        }
        return findByKeys(valorLlave, conn, toObjectMapper);
    }
    
    /**
     * Retrieves a list of objects from the database relying on specifications of this object's data.
     * The objects data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism.
     * All the filters the DAO object contains are applied during the retrieval, so be sure to clear
     * the filters before trying to retrieve ALL the existing objects.
     */
    @Override
    public List findAll() {
        Connection conn = getContextualConnection();        
        String sql = parseStatement(getFindAllStatement());
        PreparedStatement pstm = prepareStatement(buildSQLWithFilters(sql, filters, endingFilters), null, conn);
        //pstm = conn.prepareStatement(formarSentenciaSQLConFiltros(sql), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if(isSetTemporalMapper()) {
            List result = obtenerListaResultadoSentencia(pstm, conn, toObjectMapperTemporal);
            resetTemporalMapper();
            return result;
        }
        return obtenerListaResultadoSentencia(pstm, conn, toObjectMapper);            
    }   
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="SERVICIOS">
    public Object findOne(IDataSearchModule searchModule) {
        return obtenerObjetoResultadoSentencia(searchModule.getQuery(), 
                                               searchModule.getParameters(), 
                                               searchModule.getDataMappingStrategy());
    }
    
    public List findMany(IDataSearchModule searchModule) {
        return obtenerListaResultadoSentencia(searchModule.getQuery(), 
                                              searchModule.getParameters(), 
                                              searchModule.getDataMappingStrategy());
    }
    
    public void ejecutaFuncion(String strNombreFuncion, Object[] listaParametros) {
        PreparedStatement pstm = null;
        Connection conn = getContextualConnection();
        try {
            String sql = "SELECT * FROM " + getContextualFunctionName(strNombreFuncion) + "(";
            String separator = "";
            if (listaParametros != null && listaParametros.length > 0) {
                for (int i = 0; i < listaParametros.length; i++) {
                    sql += separator + "?";
                    separator = ",";
                }
            }
            sql += ")";
            
            pstm = prepareStatement(sql, listaParametros, conn);
            pstm.execute();

        } catch (Exception ex) {
            throw new SystemException(ex);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    public void ejecutarConsulta(String consulta, Object[] listaParametros) {
        PreparedStatement pstm = null;
        Connection conn = getContextualConnection();
        try {
            pstm = prepareStatement(consulta, listaParametros, conn);
            pstm.executeUpdate();
        } catch (Exception ex) {
            throw new SystemException(ex);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    public void deleteAllTableRows(IFilter ... filters) throws ExceptionDBDuplicateEntry, ExceptionDBForeignKey {
        ejecutarSentencia(buildSQLWithFilters("DELETE FROM " + getContextualTableName(), Arrays.asList(filters), null), null);
    }
    // </editor-fold> 
  
    // <editor-fold defaultstate="collapsed" desc="OTROS METODOS">   
    protected int ejecutarSentencia(String sql, Object[] parametros) throws ExceptionDBDuplicateEntry, ExceptionDBForeignKey {
        sql = parseStatement(sql);
        Connection conn = getContextualConnection();
        PreparedStatement pstm = prepareStatement(sql, parametros, conn);
        return executeStatement(pstm, conn);            
    }
    
    protected int[] ejecutarSentenciaBatch(String sql, List<Object[]> matrizParametros) throws ExceptionDBDuplicateEntry, ExceptionDBForeignKey {
        sql = parseStatement(sql);
        Connection conn = getContextualConnection();
        PreparedStatement pstm = prepararSentenciaBatch(sql, matrizParametros, conn);
        return ejecutarSentenciaBatch(pstm, conn);
    }
    
    protected Object obtenerObjetoResultadoSentencia(String sql, Object[] parametros) {
        sql = parseStatement(sql);
        if(isSetTemporalMapper()) {
            Object result = obtenerObjetoResultadoSentencia(sql, parametros, toObjectMapperTemporal);
            resetTemporalMapper();
            return result;
        }
        return obtenerObjetoResultadoSentencia(sql, parametros, toObjectMapper);            
    }
    
    /**
     * This method is intended to be called inline-like, for example:
     * 
     * Object obj = obtenerObjetoResultadoSentencia(sql, params, mapper);
     * 
     * HINT: Using this method inline-like leads to scattered inline calls all over your code, even when the query,
     * parameters and mapping strategy are the same along the calls. To cope with this, you can get the same
     * result by invoking the method findOne(IDataSearchModule searchModule), which receives a IDataSearchModule,
     * and create all your modules separately (modules are similar to views, only that they are in your code).
     * 
     * BEWARE: A null object could be returned if the result set is empty (I've seen it in practice!!!), so use it wisely.
     */
    protected Object obtenerObjetoResultadoSentencia(String sql, Object[] parametros, IDataMappingStrategy mapper) {
        sql = parseStatement(sql);
        Connection conn = getContextualConnection();
        PreparedStatement pstm = prepareStatement(sql, parametros, conn);
        return obtenerObjetoResultadoSentencia(pstm, conn, mapper);            
    }
    
    protected List obtenerListaResultadoSentencia(String sql, Object[] parametros) {
        sql = parseStatement(sql);
        if(isSetTemporalMapper()) {
            List result = obtenerListaResultadoSentencia(sql, parametros, toObjectMapperTemporal);
            resetTemporalMapper();
            return result;
        }
        return obtenerListaResultadoSentencia(sql, parametros, toObjectMapper);            
    }
    
    protected List obtenerListaResultadoSentencia(String sql, Object[] parametros, IDataMappingStrategy mapper) {
        sql = parseStatement(sql);
        Connection conn = getContextualConnection();
        PreparedStatement pstm = prepareStatement(sql, parametros, conn);
        return obtenerListaResultadoSentencia(pstm, conn, mapper);            
    }   
    // </editor-fold>  
    
    // <editor-fold defaultstate="collapsed" desc="METODOS ACCESO">
    protected static PreparedStatement prepareStatement(String sql, Object[] parametros, Connection conn) {
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            adicionarParametrosSentencia(pstm, parametros);            
            return pstm;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }
    
    private PreparedStatement prepararSentenciaBatch(String sql, List<Object[]> matrizParametros, Connection conn) {
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            if (matrizParametros != null) {
                for (int i = 0; i < matrizParametros.size(); i++) {
                    Object[] params = matrizParametros.get(i);
                    adicionarParametrosSentencia(pstm, params);                    
                    pstm.addBatch();                   
                }
            }
            return pstm;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }
    
    private static void adicionarParametrosSentencia(PreparedStatement pstm, Object[] parametros) throws SQLException {
        if (parametros != null) {
            for (int i = 0; i < parametros.length; i++) {
                Object parametro = parametros[i];
                pstm.setObject(i + 1, parametro, getSQLTypeForObject(parametro));
            }
        }
    }
    
    private int executeStatement(PreparedStatement pstm, Connection conn) throws ExceptionDBDuplicateEntry, ExceptionDBForeignKey {
        int filasAfectadas = 0;
        //boolean errorSistema = false;
        try {          
            filasAfectadas = pstm.executeUpdate();
            return filasAfectadas;
        } catch (SQLException ex) {
            if (ex.getSQLState() == null) {
                ex = (SQLException) ex.getCause();
            }
            if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
                throw new ExceptionDBDuplicateEntry();
            } else if(ex.getSQLState().equals(AbstractConnectionPool.FOREIGN_KEY_VIOLATION)) {
                throw new ExceptionDBForeignKey();
            } else {
                //errorSistema = true;
                throw new SystemException(ex);
            }
        } catch (Exception unknown) {
            //errorSistema = true;
            throw new SystemException(unknown);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    private int[] ejecutarSentenciaBatch(PreparedStatement pstm, Connection conn) throws ExceptionDBDuplicateEntry, ExceptionDBForeignKey {
        int filasAfectadas [] = {};
        //boolean errorSistema = false;
        try {          
            filasAfectadas = pstm.executeBatch();
            return filasAfectadas;
        } catch (SQLException ex) {
            if (ex.getSQLState() == null) {
                ex = (SQLException) ex.getCause();
            }
            if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
                throw new ExceptionDBDuplicateEntry();
            } else if(ex.getSQLState().equals(AbstractConnectionPool.FOREIGN_KEY_VIOLATION)) {
                throw new ExceptionDBForeignKey();
            } else {
                //errorSistema = true;
                throw new SystemException(ex);
            }
        } catch (Exception unknown) {
            //errorSistema = true;
            throw new SystemException(unknown);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    private Object obtenerObjetoResultadoSentencia(PreparedStatement pstm, Connection conn, IDataMappingStrategy mapper) {
        ResultSet rs = null;
        Object obj = null;
        try {          
            rs = pstm.executeQuery();
            if (rs.next()) {
                obj = mapper.createResultObject(rs);
            }            
            return obj;
        } catch (Exception unknown) {
            throw new SystemException(unknown);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    private List obtenerListaResultadoSentencia(PreparedStatement pstm, Connection conn, IDataMappingStrategy mapper) {
        ResultSet rs = null;
        List lstLista = new ArrayList();
        try {          
            rs = pstm.executeQuery();
            lstLista = mapper.createResultList(rs);
            return lstLista;
        } catch (Exception unknown) {
            throw new SystemException(unknown);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    private Object findByKeys(Object objLlave, Connection conn, IDataMappingStrategy mapper) {
        String conditionKeys = prepareConditionWithKey(objLlave);

        PreparedStatement pstm = null;
        try {  
            String sql = parseStatement(getFindAllStatement());
            pstm = conn.prepareStatement(buildSQLWithFilters(buildSQLWithCondition(sql, conditionKeys), filters, endingFilters));
            return obtenerObjetoResultadoSentencia(pstm, conn, mapper);
        } catch (Exception unknown) {
            throw new SystemException(unknown);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="METODOS AUXILIARES">
    protected StatementData createInsertionData(Map<String, Object> mapFieldsValues) {
        String query = "INSERT INTO " + getContextualTableName() + " (";
        String valuesReplacements = "";
        String separator = "";
                
        Object [] fields = mapFieldsValues.keySet().toArray();
        Object [] data = new Object[fields.length];
        int i = 0;
        for (Object fieldName : fields) {
            query += separator + fieldName;
            valuesReplacements += separator + "?";
            separator = ",";
            
            data[i] = mapFieldsValues.get((String)fieldName);
            i++;
        }
        query += ") VALUES (" + valuesReplacements + ")";
        
        StatementData objQueryData = new StatementData(query, data);
        return objQueryData;
    }
    
    protected StatementData createUpdateData(Object objLlave, Map<String, Object> mapFieldsValues) {
        String query = "UPDATE " + getContextualTableName() + " SET ";
        String separator = "";
                
        Object [] fields = mapFieldsValues.keySet().toArray();
        Object [] params = new Object[fields.length];
        int i = 0;
        for (Object fieldName : fields) {
            query += separator + fieldName + " = ?";
            separator = ",";
            
            params[i] = mapFieldsValues.get((String)fieldName);
            i++;
        }
        
        String conditionKeys = prepareConditionWithKey(objLlave);
        query = buildSQLWithCondition(query, conditionKeys);
        
        StatementData objQueryData = new StatementData(query, params);
        return objQueryData;
    }
    
    protected String prepareConditionWithKey(Object objLlave) {
        StringTokenizer kst = new StringTokenizer(getKeyFields(), "|");
        StringTokenizer vst = new StringTokenizer((String) objLlave, "|");

        if(kst.countTokens() <= 0)
            throw new SystemException("No key fields defined for object '" + getTableName() + "'");
        else if(vst.countTokens() <= 0)
            throw new SystemException("No key fields defined for object '" + getTableName() + "'");
        else if(kst.countTokens() != vst.countTokens())
            throw new SystemException("Mismatch between key fields and values in object '" + getTableName() + "'");

        String condition = "";
        String separator = "";
        while (kst.hasMoreElements()) {
            Object llave = kst.nextToken();
            Object valor = vst.nextToken();

            if((valor instanceof String) && !(((String)valor).startsWith("'") && ((String)valor).endsWith("'")))
                valor = "'" + valor + "'";
            condition += separator + getTableName() + "." + llave + " = " + valor;
            separator = " AND ";
        }
        return condition;
    }   
    
    protected String buildSQLWithFilters(String sql, List<IFilter> filters, List<IFilter> endingFilters) {      
        String condition = "";
        String concat = "";
        if (filters != null && !filters.isEmpty()) {
            for (IFilter filter : filters) {
                // INJECT: Set data source context as needed
                if(filter instanceof IDataSourceContextInjectable) 
                    ((IDataSourceContextInjectable)filter).setDataSourceContext(dataSourceContext);
                
                condition += concat + filter.getFilterExpression();
                concat = " AND ";
            }
            //condition = condition.substring(0, condition.length() - 4);
        }        
        sql = buildSQLWithCondition(sql, condition);      
        
        String terminalExpression = "";
        if (endingFilters != null && !endingFilters.isEmpty()) {
            for (IFilter filter : endingFilters) {
                // INJECT: Set data source context as needed
                if(filter instanceof IDataSourceContextInjectable) 
                    ((IDataSourceContextInjectable)filter).setDataSourceContext(dataSourceContext);
                
                terminalExpression += " " + filter.getFilterExpression();
            }
        }
        sql += terminalExpression;

        return sql;
    }
    
    protected static String buildSQLWithCondition(String sql, String condition) {
        String sqlFilter = "";
        if(condition != null && !"".equals(condition)) {
            //skip WHEREs in nested queries
            String[] sqlSplt1 = sql.split("\\)");// ending part without nested queries
            String[] sqlSplt2 = sqlSplt1[0].split("\\(");// startign part without nested queries
            if (!sqlSplt1[sqlSplt1.length - 1].toLowerCase().contains("where") &&
                !sqlSplt2[0].toLowerCase().contains("where")) {
                sqlFilter = " WHERE ";
            } else {
                sqlFilter = " AND ";
            }
            sqlFilter += condition;
        }

        //insert condition before GROUP/ORDER BY
        String regexGroupBy = "^SELECT\\s+.+\\s+GROUP\\s+BY\\s+.+";
        String regexOrderBy = "^SELECT\\s+.+\\s+ORDER\\s+BY\\s+.+";
        if (sql.matches(regexGroupBy)) {                
            sql = sql.replaceAll("\\s+GROUP\\s+BY\\s+", sqlFilter + " GROUP BY ");            
        } 
        else if(sql.matches(regexOrderBy)) {
            sql = sql.replaceAll("\\s+ORDER\\s+BY\\s+", sqlFilter + " ORDER BY "); 
        }                
        else {
            sql += sqlFilter;
        }        

        return sql;
    }
    
    public String parseStatement(String stmt) {
        // Make some hacks
        stmt = stmt.replaceAll("=", " = ");
        stmt = stmt.replaceAll(",", " , ");
        
        int fromIndex = 0;
        while(stmt.indexOf(":", fromIndex) != -1) {
            int index1 = stmt.indexOf(":", fromIndex);
            int index2 = stmt.indexOf(" ", index1 + 1);
            String objectName = stmt.substring(index1 + 1, index2==-1?stmt.length() -1:index2);
            
            stmt = stmt.replaceAll(":" + objectName, getContextualObjectName(objectName));
            
            fromIndex = index2 + 1;
        }
        
        return stmt;
    }
    
    /*private static void addParameterToStatement(PreparedStatement pstm, int parameterIndex, Object param) throws SQLException {
        if(param instanceof InputStream) {
            try {
                pstm.setBinaryStream(parameterIndex, (InputStream) param, ((InputStream)param).available());
            } catch (IOException ex) {
                throw new SistemaExcepcion(ex);
            }
        } else {
            pstm.setObject(parameterIndex, param, getSQLTypeForObject(param));
        }
    }*/
    
    private static int getSQLTypeForObject(Object obj) {
        if(obj instanceof Date)
            return Types.DATE;
        else if(obj instanceof Boolean)
            return Types.BOOLEAN;
        //TODO: check more types
        return Types.OTHER;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="FILTROS"> 
    @Override
    public void setFilters(IFilter ... filtros) {
        this.filters.clear();
        this.filters.addAll(Arrays.asList(filtros));
    }
    
    @Override
    public void setFilters(List<IFilter> filtros) {
        this.filters.clear();
        this.filters.addAll(filtros);
    }
    
    @Override
    public void addFilter(IFilter filtro) {
        filters.add(filtro);
    }

    @Override
    public void clearFilters() {
        filters.clear();
    }
    
    @Override
    public void setFiltersAtEnd(IFilter ... filtros) {
        this.endingFilters.clear();
        this.endingFilters.addAll(Arrays.asList(filtros));
    }
    
    @Override
    public void setFiltersAtEnd(List<IFilter> filtros) {
        this.endingFilters = filtros;
    }
    
    @Override
    public void addFilterAtEnd(IFilter filtro) {
        endingFilters.add(filtro);
    }

    @Override
    public void clearFiltersAtEnd() {
        endingFilters.clear();
    }
    // </editor-fold>
}
