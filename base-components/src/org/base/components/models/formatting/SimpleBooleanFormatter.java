/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.formatting;

/**
 *
 * @author mproenza
 */
public class SimpleBooleanFormatter implements IFormatter {
    
    String trueExpression;
    String falseExpression;

    public SimpleBooleanFormatter(String trueExpression, String falseExpression) {
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public Object format(Object obj) {
        if((Boolean)obj == true) return trueExpression;
        else return falseExpression;
    }
}
