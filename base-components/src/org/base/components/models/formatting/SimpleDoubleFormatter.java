/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.formatting;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author mproenza
 */
public class SimpleDoubleFormatter implements IFormatter {
    
    NumberFormat formatter;
    
    public SimpleDoubleFormatter() {
        this("#0.00");
    }
    
    public SimpleDoubleFormatter(String pattern) {
        formatter = new DecimalFormat(pattern);
    }

    @Override
    public Object format(Object obj) {
        return formatter.format((Double)obj);
    }
}
