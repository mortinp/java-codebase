/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.IDataSourceContextInjectable;

/**
 *
 * @author martin
 */
public class FilterMatchExistence extends FilterBase implements IDataSourceContextInjectable {
    
    String fieldToMatch;
    String tableToMatch;
    boolean in = true;
    
    DataSourceContext dataSourceContext;

    public FilterMatchExistence(String fieldName, String fieldToMatch, String tableToMatch) {
        super(fieldName);
        this.fieldToMatch = fieldToMatch;
        this.tableToMatch = tableToMatch;
    }

    public FilterMatchExistence(String fieldName, String fieldToMatch, String tableToMatch, boolean in) {
        this(fieldName, fieldToMatch, tableToMatch);
        this.in = in;
    }
    

    @Override
    public String getFilterExpression() {
        // TODO: are expressions 'in' and 'not in' valid in all database servers and drivers???
        String operator = " IN ";
        if(!in) operator = " NOT IN ";
        return fieldName + operator + "(SELECT " + fieldToMatch + " FROM " + dataSourceContext.getDBObjectExpression(tableToMatch) + ")";
    }

    @Override
    public void setDataSourceContext(DataSourceContext dsc) {
        dataSourceContext = dsc;
    }
    
}
