package org.base.dao;

import java.io.IOException;
import java.io.InputStream;
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
import org.base.dao.searches.modules.IDataSearchModule;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.RegistryDataSourceContext;
import org.base.dao.datasources.context.IDataSourceContextInjectable;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryNotFound;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;
import org.base.dao.exceptions.ExceptionDBUnknownError;
import org.base.dao.filters.AbstractFilterBase;
import org.base.dao.filters.IFilter;
import org.base.dao.wrappers.ResultSetWrapper;
import org.base.utils.io.FileIO;

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
        
    // <editor-fold defaultstate="collapsed" desc="METODOS CONTEXTO">
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
    
    protected void manageContextualException(SQLException ex) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere{
        dataSourceContext.manageDBException(ex);
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
    public abstract Object getKeyValueExpression(Object obj);
    
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
        } catch (ExceptionDBEntryReferencedElsewhere ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
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
            executeBatchStatement(query, matrizParametros);
        } catch (ExceptionDBEntryReferencedElsewhere ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }
    
    /**
     * Updates a given object on the database relying on specifications of this object's data.
     * The object's data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism.
     */
    @Override
    public void update(Object objModelo) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere {
        update(objModelo, getContextualConnection());                
    }
    
    protected void update(Object objModelo, Connection conn) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere {
        PreparedStatement pstm = null;       
        try {   
            StatementData stmtData = createUpdateData(getKeyValueExpression(objModelo), getUpdateMap(objModelo));
            pstm = prepareStatement(stmtData.getQuery(), stmtData.getParams(), conn);
            int intRegistros = executeStatement(pstm, conn);
            if (intRegistros == 0) {
                throw new ExceptionDBEntryNotFound();
            }
        } catch (ExceptionDBDuplicateEntry ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }
    
    /**
     * Updates a list of objects on the database relying on specifications of the objects' data.
     * The objects' data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism. The objects are updated in
     * the same batch, so this method is suitable for the repetitive update of multiple objects.
     */
    @Override
    public void update(List lstModelos) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere {
        try {
            List<Object[]> matrizParametros = new ArrayList<Object[]>(lstModelos.size());
            String query = null;
            for (Object object : lstModelos) {
                StatementData stmtData = createUpdateData(getKeyValueExpression(object), getUpdateMap(object));
                matrizParametros.add(stmtData.getParams());
                query = stmtData.getQuery();
            }
            executeBatchStatement(query, matrizParametros);
        } catch (ExceptionDBDuplicateEntry ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    /**
     * Deletes a given object from the database relying on specifications of this object's data.
     * The object's data could be specified by a DAO extending GeneralDAO (via the implementation
     * of abstract methods), or some other kind of mapping mechanism.
     */
    @Override
    public void remove(Object objModelo) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere {
        remove(objModelo, getContextualConnection());
    }
    
    
    protected void remove(Object objModelo, Connection conn) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere {
        String condicion = prepareConditionWithKey(getKeyValueExpression(objModelo));
        PreparedStatement pstm = null;
        try { 
            pstm = prepareStatement("DELETE FROM " + getContextualTableName() + " WHERE " + condicion, null, conn);
            int nRegistries = executeStatement(pstm, conn);
            if (nRegistries == 0) {
                throw new ExceptionDBEntryNotFound();
            }
        } catch (ExceptionDBDuplicateEntry ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } 
    }
    
    @Override
    public void remove(List lstModelos) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere {
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
        sql = buildSQLWithFilters(sql, filters, endingFilters);
        PreparedStatement pstm = prepareStatement(sql, null, conn);
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
        return findOne(searchModule.getQuery(), 
                       searchModule.getParameters(), 
                       searchModule.getDataMappingStrategy());
    }
    
    public List findMany(IDataSearchModule searchModule) {
        return findMany(searchModule.getQuery(), 
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
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
    }
    
    public void executeQuery(String consulta, Object ... listaParametros) throws Exception {
        PreparedStatement pstm = null;
        Connection conn = getContextualConnection();
        try {
            pstm = prepareStatement(consulta, listaParametros, conn);
            pstm.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
    }    
    
    // </editor-fold> 
  
    // <editor-fold defaultstate="collapsed" desc="OTROS METODOS">
    
    public void deleteAllTableRows(IFilter ... filters) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        executeStatement(buildSQLWithFilters("DELETE FROM " + getContextualTableName(), Arrays.asList(filters), null), null);
    }
    
    protected int executeStatement(String sql, Object[] parametros) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        sql = parseStatement(sql);
        Connection conn = getContextualConnection();
        PreparedStatement pstm = prepareStatement(sql, parametros, conn);
        return executeStatement(pstm, conn);            
    }
    
    protected int[] executeBatchStatement(String sql, List<Object[]> matrizParametros) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        sql = parseStatement(sql);
        Connection conn = getContextualConnection();
        PreparedStatement pstm = prepararSentenciaBatch(sql, matrizParametros, conn);
        return ejecutarSentenciaBatch(pstm, conn);
    }
    
    protected Object findOne(String sql, Object[] parametros) {
        sql = parseStatement(sql);
        if(isSetTemporalMapper()) {
            Object result = findOne(sql, parametros, toObjectMapperTemporal);
            resetTemporalMapper();
            return result;
        }
        return findOne(sql, parametros, toObjectMapper);            
    }
    
    protected List findMany(String sql, Object[] parametros) {
        sql = parseStatement(sql);
        if(isSetTemporalMapper()) {
            List result = findMany(sql, parametros, toObjectMapperTemporal);
            resetTemporalMapper();
            return result;
        }
        return findMany(sql, parametros, toObjectMapper);            
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
    protected Object findOne(String sql, Object[] parametros, IDataMappingStrategy mapper) {
        Connection conn = getContextualConnection();
        return findOne(sql, parametros, mapper, conn);           
    }
    
    protected Object findOne(String sql, Object[] parametros, IDataMappingStrategy mapper, Connection conn) {
        sql = parseStatement(sql);
        PreparedStatement pstm = prepareStatement(sql, parametros, conn);
        return obtenerObjetoResultadoSentencia(pstm, conn, mapper);            
    }
    
    protected List findMany(String sql, Object[] parametros, IDataMappingStrategy mapper) {
        Connection conn = getContextualConnection();
        return findMany(sql, parametros, mapper, conn);            
    }
    
    protected List findMany(String sql, Object[] parametros, IDataMappingStrategy mapper, Connection conn) {
        sql = parseStatement(sql);
        PreparedStatement pstm = prepareStatement(sql, parametros, conn);
        return obtenerListaResultadoSentencia(pstm, conn, mapper);            
    }
    
    protected List findMany(IDataMappingStrategy mapper, IFilter ... filters) {
        Connection conn = getContextualConnection();
        String sql = parseStatement(getFindAllStatement());
        PreparedStatement pstm = prepareStatement(buildSQLWithFilters(sql, Arrays.asList(filters), null), null, conn);
        return obtenerListaResultadoSentencia(pstm, conn, mapper);
    }
       
    // </editor-fold>  
    
    // <editor-fold defaultstate="collapsed" desc="METODOS ACCESO">
    private PreparedStatement prepararSentenciaBatch(String sql, List<Object[]> matrizParametros, Connection conn) {
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            if (matrizParametros != null) {
                for (int i = 0; i < matrizParametros.size(); i++) {
                    Object[] params = matrizParametros.get(i);
                    adicionarParametrosSentencia(pstm, params);                    
                    pstm.addBatch();                   
                }
            }
            return pstm;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
    }
    
    private static void adicionarParametrosSentencia(PreparedStatement pstm, Object[] parametros) throws SQLException {
        if (parametros != null) {
            for (int i = 0; i < parametros.length; i++) {
                Object parametro = parametros[i];
                if(parametro != null && parametro instanceof InputStream) {
                    try {
                        pstm.setBinaryStream(i + 1,(InputStream) parametro, (int) ((InputStream)parametro).available());
                    } catch (IOException ex) {
                        DAOPackage.log(ex);
                        throw new ExceptionDBUnknownError(ex);
                    } catch (Exception ex) {
                        try {
                            DAOPackage.log(ex);
                            //TODO: setBytes
                            pstm.setBytes(i + 1, FileIO.inputStreamToBytes((InputStream) parametro));
                            //pstm.set(i + 1, parametro, getSQLTypeForObject(parametro));
                        } catch (IOException ex1) {
                            DAOPackage.log(ex);
                            throw new ExceptionDBUnknownError(ex);
                        }
                    }
                } else
                    pstm.setObject(i + 1, parametro, getSQLTypeForObject(parametro));
            }
        }
    }
    
    private int executeStatement(PreparedStatement pstm, Connection conn) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        int filasAfectadas = 0;
        //boolean errorSistema = false;
        try {          
            filasAfectadas = pstm.executeUpdate();
            //return filasAfectadas;
        } catch (SQLException ex) {
            /*if (ex.getSQLState() == null) {
                ex = (SQLException) ex.getCause();
            }
            if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
                throw new ExceptionDBDuplicateEntry();
            } else if(ex.getSQLState().equals(AbstractConnectionPool.FOREIGN_KEY_VIOLATION)) {
                throw new ExceptionDBForeignKey();
            } else {
                DAOPackage.log(ex);
                throw new ExceptionDBUnknownError(ex);
            }*/
            manageContextualException(ex);
        } catch (Exception ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
        return filasAfectadas;
    }
    
    private int[] ejecutarSentenciaBatch(PreparedStatement pstm, Connection conn) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        int filasAfectadas [] = {};
        //boolean errorSistema = false;
        try {          
            filasAfectadas = pstm.executeBatch();
            //return filasAfectadas;
        } catch (SQLException ex) {
            /*if (ex.getSQLState() == null) {
                ex = (SQLException) ex.getCause();
            }
            if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
                throw new ExceptionDBDuplicateEntry();
            } else if(ex.getSQLState().equals(AbstractConnectionPool.FOREIGN_KEY_VIOLATION)) {
                throw new ExceptionDBForeignKey();
            } else {
                DAOPackage.log(ex);
                throw new ExceptionDBUnknownError(ex);
            }*/
            manageContextualException(ex);
        } catch (Exception ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
        return filasAfectadas;
    }
    
    private Object obtenerObjetoResultadoSentencia(PreparedStatement pstm, Connection conn, IDataMappingStrategy mapper) {
        ResultSet rs = null;
        Object obj = null;
        try {          
            rs = pstm.executeQuery();
            if (rs.next()) {
                obj = mapper.createResultObject(new ResultSetWrapper(rs));
            }            
            return obj;
        } catch (Exception ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
        }
    }
    
    private List obtenerListaResultadoSentencia(PreparedStatement pstm, Connection conn, IDataMappingStrategy mapper) {
        ResultSet rs = null;
        List lstLista = new ArrayList();
        try {          
            rs = pstm.executeQuery();
            lstLista = mapper.createResultList(new ResultSetWrapper(rs));
            return lstLista;
        } catch (Exception ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
    }
    
    private Object findByKeys(Object objLlave, Connection conn, IDataMappingStrategy mapper) {
        String conditionKeys = prepareConditionWithKey(objLlave);

        PreparedStatement pstm = null;
        try {  
            String sql = parseStatement(getFindAllStatement());
            sql = buildSQLWithFilters(buildSQLWithCondition(sql, conditionKeys), filters, endingFilters);
            pstm = conn.prepareStatement(sql);
            return obtenerObjetoResultadoSentencia(pstm, conn, mapper);
        } catch (Exception ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        } finally {
            closeContextualStatement(pstm);
            closeContextualConnection(conn);
        }
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="METODOS AUXILIARES">
    protected static PreparedStatement prepareStatement(String sql, Object[] parametros, Connection conn) {
        try {
            PreparedStatement pstm = conn.prepareStatement(sql);
            adicionarParametrosSentencia(pstm, parametros);            
            return pstm;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }
    
    protected static PreparedStatement prepareStatementAutogeneratedKey(String sql, Object[] parametros, Connection conn) {
        try {
            PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            adicionarParametrosSentencia(pstm, parametros);            
            return pstm;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }
    
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
        if(objLlave == null) return "";
        
        StringTokenizer kst = new StringTokenizer(getKeyFields(), "|");
        StringTokenizer vst = new StringTokenizer(String.valueOf(objLlave), "|");

        if(kst.countTokens() <= 0)
            throw new ExceptionDBProgrammerMistake("No key fields defined for object '" + getTableName() + "'");
        else if(vst.countTokens() <= 0)
            throw new ExceptionDBProgrammerMistake("No key fields defined for object '" + getTableName() + "'");
        else if(kst.countTokens() != vst.countTokens())
            throw new ExceptionDBProgrammerMistake("Mismatch between key fields and values in object '" + getTableName() + "'");

        String condition = "";
        String separator = "";
        while (kst.hasMoreElements()) {
            Object llave = kst.nextToken();
            Object valor = vst.nextToken();

            if((valor instanceof String) && !(((String)valor).startsWith("'") && ((String)valor).endsWith("'")))
                valor = "'" + valor + "'";
            
            /* NOTE ON THE FOLLOWING LINE: 
             * Hay veces que en las consultas a las que se les va a adicionar una condicion, se han especificado alias para las tablas 
             * que intervienen en las mismas.
             * Esto provoca que la condicion debe usar el alias especificado en la consulta, en vez del nombre original de la tabla.
             * Por otro lado, hay veces que el nombre de un campo es ambiguo (tiene el mismo nombre en varias tablas usadas en la consulta)
             * , y la forma de evitar la ambiguedad es especificando la tabla a la que pertenece.
             * Por tanto, para evitar las ambiguedades, se deberia especificar en nombre o alias de la tabla en la condicion.
             * Como no se puede saber el alias de la tabla, la unica solucion es invocar a getTableName() y esperar que las consultas no
             * usen alias para las tablas.
             */
            // UPDATE: maybe this solves the problem
            // En caso de usar un alias, la llave se deberia dar con el alias.
            if(!((String)llave).contains(".")) llave = getTableName() + "." + llave;
            condition += separator + /*getTableName() + "." +*/ llave + " = " + valor; 
            
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
                
                String filterExp = filter.getFilterExpression();
                if(!filterExp.contains(".")) filterExp = getTableName() + "." + filterExp;
                condition += concat + filterExp;
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
                
                String filterExp = filter.getFilterExpression();
                if(!filterExp.contains(".")) filterExp = getTableName() + "." + filterExp;
                terminalExpression += " " + filterExp;
            }
        }
        sql += terminalExpression;

        return sql;
    }
    
    protected static String buildSQLWithCondition(String sql, String condition) {
        if(condition == null || condition.isEmpty()) return sql;
        String sqlFilter = "";
        
        // Don't mess with things in nested queries
        String[] sqlSplt1 = sql.split("\\)");// ending part without nested queries
        String[] sqlSplt2 = sqlSplt1[0].split("\\(");// starting part without nested queries
        if(condition != null && !"".equals(condition)) {
            if (!sqlSplt1[sqlSplt1.length - 1].toLowerCase().contains("where") &&
                !sqlSplt2[0].toLowerCase().contains("where")) {
                sqlFilter = " WHERE ";
            } else {
                sqlFilter = " AND ";
            }
            sqlFilter += condition;
        }
        
        // Insert condition before GROUP/ORDER BY (See code below)
        
        /* VERSION 1 (OLD)*/
        /*String regexGroupBy = "(?i)^SELECT\\s+.+\\s+GROUP\\s+BY\\s+.+";
        String regexOrderBy = "(?i)^SELECT\\s+.+\\s+ORDER\\s+BY\\s+.+";
        //sql = sql.toUpperCase();
        if (sql.matches(regexGroupBy)) {                
            sql = sql.replaceAll("(?i)\\s+GROUP\\s+BY\\s+", sqlFilter + " GROUP BY ");            
        } else if(sql.matches(regexOrderBy)) {
            sql = sql.replaceAll("(?i)\\s+ORDER\\s+BY\\s+", sqlFilter + " ORDER BY ");
        } else {
            sql += sqlFilter;
        }*/
        
        /* VERSION 2 */
        if(sqlSplt1[sqlSplt1.length - 1].toLowerCase().contains("group by") ||
           sqlSplt2[0].toLowerCase().contains("group by")) {
            sql = sql.replaceAll("(?i)\\s+GROUP\\s+BY\\s+", sqlFilter + " GROUP BY ");  
        } else if(sqlSplt1[sqlSplt1.length - 1].toLowerCase().contains("order by") ||
           sqlSplt2[0].toLowerCase().contains("order by")) {
            sql = sql.replaceAll("(?i)\\s+ORDER\\s+BY\\s+", sqlFilter + " ORDER BY ");
        } else {
            sql += sqlFilter;
        }

        return sql;
    }
    
    public String parseStatement(String stmt) {
        // Make some hacks
        stmt = stmt.replaceAll("=", " = ");
        stmt = stmt.replaceAll(",", " , ");
        
        // Replace table placeholder
        stmt = stmt.replaceAll("@", getTableName());
        
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
        
        // NEW
        for (IFilter iFilter : filtros) {
            if(iFilter instanceof AbstractFilterBase)
                ((AbstractFilterBase)iFilter).injectDataSourceVariation(dataSourceContext.getDataSourceVariation());
        }
        
        this.filters.addAll(Arrays.asList(filtros));
    }
    
    @Override
    public void setFilters(List<IFilter> filtros) {
        this.filters.clear();
        // NEW
        for (IFilter iFilter : filtros) {
            if(iFilter instanceof AbstractFilterBase)
                ((AbstractFilterBase)iFilter).injectDataSourceVariation(dataSourceContext.getDataSourceVariation());
        }
        this.filters.addAll(filtros);
    }
    
    @Override
    public void addFilter(IFilter filtro) {
        if(filtro instanceof AbstractFilterBase)
                ((AbstractFilterBase)filtro).injectDataSourceVariation(dataSourceContext.getDataSourceVariation());
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
