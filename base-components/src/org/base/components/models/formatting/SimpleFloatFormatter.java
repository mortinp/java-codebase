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
public class SimpleFloatFormatter implements IFormatter {
    
    NumberFormat formatter;
    
    public SimpleFloatFormatter() {
        this("#0.00");
    }
    
    public SimpleFloatFormatter(String pattern) {
        formatter = new DecimalFormat(pattern);
    }

    @Override
    public Object format(Object obj) {
        return /*Float.parseFloat(*/formatter.format(/*(Float)*/obj)/*)*/;
    }
}
