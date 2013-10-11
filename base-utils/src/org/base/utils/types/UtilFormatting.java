/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.types;

import java.text.DecimalFormat;

/**
 *
 * @author martin
 */
public class UtilFormatting {
    
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
        return Float.parseFloat(floatToString(valor, afterColon));
    }
    
}
