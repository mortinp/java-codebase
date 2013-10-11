/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing.inference;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class TypesInferreer {
    
    // TODO: I'm assuming this pattern for dates (SQLite style). I should not.
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    
    public static Object inferDate(Object value) {
        java.util.Date date = null;

        if(value == null) return null;
        if(value instanceof String) {
            try {
                date = df.parse((String)value);
            } catch (ParseException ex) {
                Logger.getLogger(TypesInferreer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if(value instanceof Long) {
            date = new java.util.Date((Long)value);
        } else {
            date = (Date)value;
        }
        return new Date(date.getTime());
    }
    
    public static Object inferFloat(Object value) {
        if(value == null) return null;
        if(value instanceof BigDecimal) {
            return ((BigDecimal)value).floatValue();
        }
        return value;
    }
    
    public static Object inferBigDecimal(Object value) {
        if(value == null) return null;
        if(value instanceof BigDecimal == false) {
            return new BigDecimal(String.valueOf(value));
        }
        return value;
    }
    
}
