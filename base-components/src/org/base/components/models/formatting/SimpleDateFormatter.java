/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.formatting;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Martin
 */
public class SimpleDateFormatter implements IFormatter {
    
    SimpleDateFormat dateFormat;
    
    public SimpleDateFormatter() {
        this("dd-MM-yyyy");
    }
    
    public SimpleDateFormatter(String pattern) {
        dateFormat = new SimpleDateFormat(pattern);
    }

    @Override
    public Object format(Object obj) {
        return dateFormat.format((Date)obj);
    }
    
}
