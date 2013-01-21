/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.util.Map;
import java.util.StringTokenizer;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author Martin
 */
public class DAOUtils {
    /**
     * This class is used to hold data that will be passed through some methods to carry out executions of
     * statements in the database; in many cases, an object of this class is handy for this purpose.
     */
    /*public static class StatementData {
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
    
    private String prepareConditionWithKey(Object keyValues) {
        StringTokenizer kst = new StringTokenizer(getKeyFields(), "|");
        StringTokenizer vst = new StringTokenizer((String) keyValues, "|");

        if(kst.countTokens() <= 0)
            throw new SistemaExcepcion("No key fields defined for object '" + getTableName() + "'");
        else if(vst.countTokens() <= 0)
            throw new SistemaExcepcion("No key fields defined for object '" + getTableName() + "'");
        else if(kst.countTokens() != vst.countTokens())
            throw new SistemaExcepcion("Mismatch between key fields and values in object '" + getTableName() + "'");

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
    
    private static String buildSQLWithCondition(String sql, String condition) {
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
    }*/
}
