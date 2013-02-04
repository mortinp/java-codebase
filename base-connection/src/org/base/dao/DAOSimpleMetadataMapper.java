/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.exceptions.DuplicateEntryException;
import org.base.dao.searches.DefaultDataMappingStrategy;
import org.base.exceptions.system.SystemException;
import org.base.mapping.dsl.base.MappingEntry;
import org.base.mapping.dsl.base.parsing.IMappingBuilder;
import org.base.mapping.dsl.base.parsing.SimpleMappingBuilder;
import org.base.mapping.dsl.base.service.MappingBuilderLocator;

/**
 *
 * @author Maylen
 */
public class DAOSimpleMetadataMapper extends DAOBase {
    IMappingBuilder mappingBuilder;
    
    //public static String MAPPING_METADATA_FILES_DIRECTORY = "";
    
    public DAOSimpleMetadataMapper(String metadataFilePath, String dataSourceContextName) {
        super(dataSourceContextName, null);
        mappingBuilder = MappingBuilderLocator.getMappingBuilder(metadataFilePath)
                /*new SimpleMappingBuilder(MAPPING_METADATA_FILES_DIRECTORY + "/" + metadataFileName)*/;
        setDataMappingStrategy(new DefaultDataMappingStrategy() {
            @Override
            public Object createResultObject(ResultSet rs) throws SQLException {
                return mappingBuilder.buildObject(rowToMap(rs));
            }
        });
    }

    @Override
    public String getTableName() {
        return mappingBuilder.getTableName();
    }

    @Override
    public String getKeyFields() {
        return mappingBuilder.getKeyFieldsNamesExpression();
    }

    @Override
    protected Object getKeyValueExpression(Object obj) {
        return mappingBuilder.getKeyFieldsValuesExpression(obj);
    }

    @Override
    protected Map<String, Object> getInsertionMap(Object obj) {
        return mappingBuilder.getInsertionMap(obj);
    }

    @Override
    protected Map<String, Object> getUpdateMap(Object obj) {
        return mappingBuilder.getUpdateMap(obj);
    }

    @Override
    protected String getFindAllStatement() {
        List<MappingEntry> orderByEntries = 
                mappingBuilder.getEntries("orderby");
        String findAllStatement = 
                "SELECT * FROM " + getContextualTableName();
        if(!orderByEntries.isEmpty()) {
            String separator = " ORDER BY ";
            for (MappingEntry entry : orderByEntries) {
                findAllStatement += separator + entry.getTargetFieldName();
                separator = ",";
            }
        }
        return findAllStatement;
    }
    
    @Override
    public void insert(Object objModelo, Connection conn) throws DuplicateEntryException {
        PreparedStatement pstm = null;
        try {
            //check if there are any fields modified as @autonumeric and alter the query in case there are.
            List<MappingEntry> autonumericEntries = 
                        mappingBuilder.getEntries("autonumeric");
            if(autonumericEntries.isEmpty()) {
                super.insert(objModelo, conn);
            } else {
                StatementData stmtData = createInsertionData(getInsertionMap(objModelo));

                String separator = " RETURNING ";
                for (MappingEntry entry : autonumericEntries) {
                    stmtData.query += separator + entry.getTargetFieldName();
                    separator = ",";
                }

                pstm = prepareStatement(stmtData.getQuery(), stmtData.getParams(), conn);
                ResultSet rs = pstm.executeQuery();
                if (rs != null && rs.next()) {
                    SimpleMappingBuilder.fillObjectFromEntries(objModelo, autonumericEntries, rowToMap(rs));
                }
            }   
        } catch (SQLException ex) {
            if (ex.getSQLState() == null) {
                ex = (SQLException) ex.getCause();
            }
            if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
                throw new DuplicateEntryException();
            } else {
                throw new SystemException(ex);
            }
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
    
    private Map<String, Object> rowToMap(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<String, Object> row = new HashMap<String, Object>(columns);
        for(int i=1; i<=columns; ++i){           
            row.put(md.getColumnName(i), rs.getObject(i));
        }
        return row;
    }
}
