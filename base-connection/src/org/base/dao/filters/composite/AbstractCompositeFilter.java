/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.base.dao.filters.IFilter;

/**
 *
 * @author Maylen
 */
public abstract class AbstractCompositeFilter implements IFilter {    
    List<IFilter> children = new ArrayList<IFilter>();
    
    public AbstractCompositeFilter(IFilter ... filters) {
        children.addAll(Arrays.asList(filters));
    }
    
    public void addFilter(IFilter child) {
        children.add(child);
    }
    
    @Override
    public String getFilterExpression() {
        return asString();
    }
    
    public String asString() {
        String fullExp = "(";
        String concat = "";
        for (IFilter child : children) {
            fullExp += concat + child.getFilterExpression();
            concat = " " + getConcatElement() + " ";
        }
        fullExp += ")";
        return fullExp;
    }
    
    protected abstract String getConcatElement();
}
