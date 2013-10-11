/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.base.dao.DAOBase;
import org.base.dao.DAOFactory;
import org.base.dao.filters.FilterSimple;
import org.base.dao.filters.IFilter;
import org.base.dao.searches.DefaultDataMappingStrategy;
import org.base.dao.searches.IDataMappingStrategy;
import org.base.dao.searches.modules.IDataSearchModule;
import org.base.utils.exceptions.ExceptionProgrammerMistake;

/**
 *
 * @author mproenza
 */
public class LazyUtils {
    
    public static class KeyValuePair {
        public Object key;
        public Object value;

        public KeyValuePair(Object key, Object value) {
            this.key = key;
            this.value = value;
        }        
    };
    
    public static List loadChildrenList(String aliasMaster, String aliasDetails, String masterKeyValues) {
        DAOBase masterDAO = (DAOBase)DAOFactory.getDAO(aliasMaster);
        DAOBase detailsDAO = (DAOBase)DAOFactory.getDAO(aliasDetails);
        
        String masterKeyFields = masterDAO.getKeyFields();
        List<KeyValuePair> pairs = splitKeysAndValues(masterKeyFields, masterKeyValues);
        
        // Create filters
        List<IFilter> filters = new ArrayList<IFilter>();
        for (KeyValuePair kv : pairs) {
            String masterKeyFieldName = kv.key + "_" + aliasMaster;
            filters.add(new FilterSimple(detailsDAO.getTableName() + "." + masterKeyFieldName, kv.value));
        }
        
        detailsDAO.setFilters(filters);
        List results = detailsDAO.findAll();
        
        return results;
    } 
    
    public static List loadChildrenList(String aliasMaster, String aliasDetails, String masterKeyValues, List filters) {
        DAOBase masterDAO = (DAOBase)DAOFactory.getDAO(aliasMaster);
        DAOBase detailsDAO = (DAOBase)DAOFactory.getDAO(aliasDetails);
        
        String masterKeyFields = masterDAO.getKeyFields();
        List<KeyValuePair> pairs = splitKeysAndValues(masterKeyFields, masterKeyValues);
        
        // Create filters
        //List<IFilter> filters = new ArrayList<IFilter>();
        for (KeyValuePair kv : pairs) {
            String masterKeyFieldName = kv.key + "_" + aliasMaster;
            filters.add(new FilterSimple(detailsDAO.getTableName() + "." + masterKeyFieldName, kv.value));
        }
        
        detailsDAO.setFilters(filters);
        List results = detailsDAO.findAll();
        
        return results;
    } 
    
    public static Object loadReference(final String aliasContainer, final String aliasReference, final String containerKeyValues) {
        final DAOBase containerDAO = (DAOBase)DAOFactory.getDAO(aliasContainer);
        final DAOBase referenceDAO = (DAOBase)DAOFactory.getDAO(aliasReference);
        
        final String containerKeyFields = containerDAO.getKeyFields();
        final String containerTableName = containerDAO.getTableName();
        
        final String referenceKeyFields = referenceDAO.getKeyFields();
        
        Object obj = containerDAO.findOne(new IDataSearchModule() {
            @Override
            public String getQuery() {
                StringTokenizer refKeyFields = new StringTokenizer(referenceKeyFields, "|");
                String parametersList = "";
                String separator = "";
                while (refKeyFields.hasMoreElements()) {
                    parametersList += separator + refKeyFields.nextToken() + "_" + aliasReference;
                    separator = ",";
                }

                List<KeyValuePair> pairs = splitKeysAndValues(containerKeyFields, containerKeyValues);
                String whereClause = "";
                separator = "";
                for (KeyValuePair kv : pairs) {
                    whereClause += separator + kv.key + " = " + kv.value;
                    separator = " AND ";
                }
                
                return "SELECT " + parametersList
                        + " FROM :" + containerTableName
                        + " WHERE " + whereClause;
            }

            @Override
            public Object[] getParameters() {
                return null;
            }

            @Override
            public IDataMappingStrategy getDataMappingStrategy() {
                return new DefaultDataMappingStrategy() {
                    @Override
                    public Object createResultObject(ResultSet rs) throws SQLException {
                        final StringTokenizer refKeyFields = new StringTokenizer(referenceKeyFields, "|");
                        String keyValues = "";
                        String separator = "";
                        while (refKeyFields.hasMoreElements()) {
                            keyValues += separator + rs.getString(refKeyFields.nextToken() + "_" + aliasReference);
                            separator = "|";
                        }
                        return referenceDAO.findOne(keyValues);
                    }
                };
            }
        } );
        
        return obj;
    }
    
    public static List<KeyValuePair> splitKeysAndValues(String keyFields, String keyValues) {
        StringTokenizer kst = new StringTokenizer(keyFields, "|");
        StringTokenizer vst = new StringTokenizer(keyValues, "|");

        if(kst.countTokens() <= 0)
            throw new ExceptionProgrammerMistake("No key fields must define at least one value (0 found)");
        else if(vst.countTokens() <= 0)
            throw new ExceptionProgrammerMistake("No key fields must define at least one value (0 found)");
        else if(kst.countTokens() != vst.countTokens())
            throw new ExceptionProgrammerMistake("Mismatch between key fields and values (found " + kst.countTokens() + " keys and " + vst.countTokens() + " values)");

        List<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
        while (kst.hasMoreElements()) {
            Object key = kst.nextToken();
            Object value = vst.nextToken();
            
            pairs.add(new KeyValuePair(key, value));
        }
        
        return pairs;
    }
    
    
    public static Map<String, Object> getForeignKeysMap(String alias, Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        DAOBase objTransaccionDAO = (DAOBase)DAOFactory.getDAO(alias);
        String keys = objTransaccionDAO.getKeyFields();
        String values = (String)objTransaccionDAO.getKeyValueExpression(object);
        List<KeyValuePair> pairs = splitKeysAndValues(keys, values);
        for (KeyValuePair kv : pairs) {
            map.put((String)kv.key + "_" + alias, kv.value);
        }
        
        return map;
    }
    
}
