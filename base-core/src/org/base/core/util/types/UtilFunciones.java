/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.util.types;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author martin
 */
public class UtilFunciones {

    public static Date getFecha(String fecha) {
        Date fechaResult = null;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        try {
            formatDate.setLenient(false);
            fechaResult = formatDate.parse(fecha);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return fechaResult;
    }
    
    public static Date getFecha(String fecha, String pattern) {
        Date fechaResult = null;
        SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
        try {
            formatDate.setLenient(false);
            fechaResult = formatDate.parse(fecha);
        } catch (Exception e) {
            throw new SystemException(e);
        }
        return fechaResult;
    }

    public static String invertirFecha(String valor) {
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

    public static String FormatearNumero(Float valor) {
        DecimalFormat pp = new DecimalFormat("######0.00");
        return pp.format(valor);
    }
    
    public static String floatToString(float valor, int afterColon) {
        String pattern = "#0.";
        for (int i = 0; i < afterColon; i++) {
            pattern += "0";
        }
        DecimalFormat pp = new DecimalFormat(pattern);
        return pp.format(valor);
    }
    
    public static String doubleToString(double valor, int afterColon) {
        String pattern = "#0.";
        for (int i = 0; i < afterColon; i++) {
            pattern += "0";
        }
        DecimalFormat pp = new DecimalFormat(pattern);
        return pp.format(valor);
    }
    
    public static float formatFloat(float valor, int afterColon) {
        return Float.parseFloat(UtilFunciones.floatToString(valor, afterColon));
    }
}
