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
public class FilterCompositeOR extends AbstractCompositeFilter {
    
    public FilterCompositeOR(IFilter ... filters) {
        super(filters);
    }

    @Override
    protected String getConcatElement() {
        return "or";
    }    
}
