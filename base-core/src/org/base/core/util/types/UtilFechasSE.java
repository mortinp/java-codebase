/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.util.types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author martin
 */
public class UtilFechasSE {  
    
    public static String getDia(Date fecha) {        
        return getFormato("dd").format(fecha);
    }
    
    public static String getMes(Date fecha) {
        return getFormato("MM").format(fecha);
    }
    
    public static String getAnno(Date fecha) {
        return getFormato("yyyy").format(fecha);
    }
    
    public static String getFechaDDMMYYY(Date fecha) {        
        return getFormato("dd/MM/yyyy").format(fecha);
    }
    
    public static String getFechaFormato(Date fecha, String strFormato) {        
        return getFormato(strFormato).format(fecha);
    }
    
    private static DateFormat getFormato(String strFormato) {
        return new SimpleDateFormat(strFormato);
    }
    
}
