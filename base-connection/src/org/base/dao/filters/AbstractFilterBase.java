/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

import org.base.dao.datasources.variations.IDataSourceVariation;

/**
 *
 * @author Martin
 */
public abstract class AbstractFilterBase implements IFilter {
    
    protected IDataSourceVariation dataSourceVariation;
    
    public void injectDataSourceVariation(IDataSourceVariation dataSourceVariation) {
        this.dataSourceVariation = dataSourceVariation;
    }

    @Override
    public abstract String getFilterExpression();
    
}
