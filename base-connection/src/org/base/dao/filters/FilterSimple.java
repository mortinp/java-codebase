/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

/**
 *
 * @author mproenza
 */
public class FilterSimple extends FilterBase {

    public static String OP_EQUALS = " = ";
    public static String OP_NOT_EQUALS = " <> ";
    public static String OP_GREATER_THAN = " > ";
    public static String OP_GREATER_THAN_EQUALS = " >= ";
    public static String OP_LESS_THAN = " <";
    public static String OP_LESS_THAN_EQUALS = " <= ";
    private String operator = OP_EQUALS;
    
    private Object value;

    public FilterSimple(String fieldName, Object value) {
        super(fieldName);
        if(value == null) this.operator = " IS ";
        this.value = value;
    }

    public FilterSimple(String fieldName, Object value, String operator) {
        this(fieldName, value);
        // Fix operator for null value
        if(value == null) {
            if(operator.equals(OP_EQUALS)) operator = " IS ";
            else if(operator.equals(OP_NOT_EQUALS)) operator = " IS NOT ";
        }
        this.operator = " " + operator.trim() + " ";//make sure operator has trailing spaces...this fix is also good for testing
    }

    @Override
    public String getFilterExpression() {
        String strFiltro = formatValue(this.value);
        return fieldName + this.operator + strFiltro;
    }
}
