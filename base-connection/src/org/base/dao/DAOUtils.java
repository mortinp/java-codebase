/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mproenza
 */
public class DAOUtils {
    
    
    public static  Map<String, Object> rowToMap(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<String, Object> row = new HashMap<String, Object>(columns);
        for(int i=1; i<=columns; ++i){           
            row.put(md.getColumnName(i), rs.getObject(i));
        }
        return row;
    }
    
    public static String formatValueForQuery(Object valor) {
        if(valor != null) {
            String strTipo = valor.getClass().getName();
            if (strTipo.equals("java.util.Date") || strTipo.equals("java.sql.Date")) {
                Date fecha = (Date) valor;
                Format objFormatoFecha = getDateFormatter();
                String strValor = objFormatoFecha.format(fecha);
                if(!strValor.startsWith("'")) strValor = "'" + strValor;
                if(!strValor.endsWith("'")) strValor = strValor + "'";
                return strValor;
            } else if (strTipo.equals("java.lang.String") || strTipo.equals("java.lang.Character")) {
                String strValor = valor.toString();
                if(!strValor.startsWith("'")) strValor = "'" + strValor;
                if(!strValor.endsWith("'")) strValor = strValor + "'";
                return strValor;
            } else {
                return valor.toString();
            } 
        } else return "NULL";
    }
    public static Format getDateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
}
