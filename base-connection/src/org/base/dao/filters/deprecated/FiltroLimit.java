/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.deprecated;

import org.base.dao.filters.IFilter;

/**
 *
 * @author martin
 */
@Deprecated
public class FiltroLimit implements IFilter {

    private int rowLimit = 1;
    
    public FiltroLimit() {
    }
    
    public FiltroLimit(int rowLimit) {
        this.rowLimit = rowLimit;
    }
    
    @Override
    public String getFilterExpression() {
        return "LIMIT " + rowLimit;
    }
    
}
