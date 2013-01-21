/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

/**
 *
 * @author mproenza
 */
public class FilterDateExtract extends FilterBase {

    public static String OP_EQUALS = " = ";
    public static String OP_NOT_EQUALS = " <> ";
    public static String OP_GREATER_THAN = " > ";
    public static String OP_GREATER_THAN_EQUALS = " >= ";
    public static String OP_LESS_THAN = " <";
    public static String OP_LESS_THAN_EQUALS = " <= ";
    private String operator = OP_EQUALS;
    
    public static String EXTRACT_YEAR = "'YEAR'";
    public static String EXTRACT_MONTH = "'MONTH'";
    public static String EXTRACT_DAY = "'DAY'";
    private String extract = EXTRACT_YEAR;

    
    private Object value;
    

    public FilterDateExtract(String fieldName, Object value) {
        super(fieldName);
        //if(value == null) this.operator = " IS ";// Fix operator for null value
        this.value = value;
    }
    
    public FilterDateExtract(String fieldName, String extract, Object value) {
        this(fieldName, value);
        this.extract = extract;
        //if(value == null) this.operator = " IS ";// Fix operator for null value
        this.value = value;
    }

    public FilterDateExtract(String fieldName, String extract, Object value, String operator) {
        this(fieldName, extract, value);
        /*if(value == null) {// Fix operator for null value
            if(operator.equals(OP_EQUALS)) operator = " IS ";
            else if(operator.equals(OP_NOT_EQUALS)) operator = " IS NOT ";
        }*/
        this.operator = " " + operator.trim() + " ";//make sure operator has trailing spaces...this fix is also good for testing
    }

    @Override
    public String getFilterExpression() {
        String strValue = formatValue(this.value);
        return "EXTRACT(" + extract + ", " + fieldName + ")" + this.operator + strValue;
    }
}
