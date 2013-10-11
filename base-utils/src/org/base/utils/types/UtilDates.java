/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author martin
 */
public class UtilDates {

    public static Date getDate(String fecha) {
        Date fechaResult = null;
        DateFormat formatDate = getDefaultFormatter("dd-MM-yyyy");
        try {
            formatDate.setLenient(false);
            fechaResult = formatDate.parse(fecha);
        } catch (Exception e) {
            //throw new ExceptionUnknownError(e);
        }
        return fechaResult;
    }
    
    public static Date getDate(String fecha, String pattern) {
        Date fechaResult = null;
        DateFormat formatDate = getDefaultFormatter(pattern);
        try {
            formatDate.setLenient(false);
            fechaResult = formatDate.parse(fecha);
        } catch (Exception e) {
            //throw new ExceptionUnknownError(e);
        }
        return fechaResult;
    }

    public static String getInvertedDate(String valor) {
        if (valor != null) {
            if (valor.length() != 10) {
                return "";
            } else {
                int posPB = valor.indexOf("-"), posSB = valor.indexOf("-", posPB + 1);
                if (posPB >= 0 && posSB >= 0) {
                    String cadena = valor.substring(posSB + 1, valor.length())
                            + valor.substring(posPB, posSB + 1)
                            + valor.substring(0, posPB);
                    return cadena.replaceAll("-", "/");
                } else {
                    return valor;
                }
            }
        } else {
            return "";
        }
    }
    
    public static String getDay(Date fecha) {        
        return getDefaultFormatter("dd").format(fecha);
    }
    
    public static String getMonth(Date fecha) {
        return getDefaultFormatter("MM").format(fecha);
    }
    
    public static String getYear(Date fecha) {
        return getDefaultFormatter("yyyy").format(fecha);
    }    
    
    public static long getDaysDifference(Date date1, Date date2) {
        GregorianCalendar c1 = new GregorianCalendar();
        GregorianCalendar c2 = new GregorianCalendar();
        c1.setTime(date1);
        c2.setTime(date2);
        long span = c2.getTimeInMillis() - c1.getTimeInMillis();
        GregorianCalendar c3 = new GregorianCalendar();
        c3.setTimeInMillis(span);
        long numberOfMSInADay = 1000*60*60*24;
        //System.out.println(c3.getTimeInMillis() / numberOfMSInADay);
        
        return (long)Math.ceil((double)c3.getTimeInMillis() / (double)numberOfMSInADay);
    }
    
    
    
    
    private static DateFormat getDefaultFormatter(String strFormato) {
        return new SimpleDateFormat(strFormato);
    }
    
}
