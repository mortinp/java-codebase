/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.composite;

import org.base.dao.filters.IFilter;

/**
 *
 * @author Maylen
 */
public class LeafFilter implements IFilter {
    String expression;
    
    public LeafFilter(String expression) {
        this.expression = expression;
    }

    @Override
    public String getFilterExpression() {
        return expression;
    }
}
