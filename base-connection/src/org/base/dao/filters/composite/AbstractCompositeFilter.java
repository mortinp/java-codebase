/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.base.dao.datasources.variations.IDataSourceVariation;
import org.base.dao.filters.AbstractFilterBase;
import org.base.dao.filters.IFilter;

/**
 *
 * @author Maylen
 */
public abstract class AbstractCompositeFilter extends AbstractFilterBase {    
    List<IFilter> children = new ArrayList<IFilter>();
    
    public AbstractCompositeFilter(IFilter ... filters) {
        children.addAll(Arrays.asList(filters));
    }
    
    @Override
    public void injectDataSourceVariation(IDataSourceVariation dataSourceVariation) {
        super.injectDataSourceVariation(dataSourceVariation);
        // NEW
        for (IFilter iFilter : children) {
            if(iFilter instanceof AbstractFilterBase)
                ((AbstractFilterBase)iFilter).injectDataSourceVariation(dataSourceVariation);
        }
    }
    
    public void addFilter(IFilter child) {
        if(child instanceof AbstractFilterBase)
                ((AbstractFilterBase)child).injectDataSourceVariation(dataSourceVariation);
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
