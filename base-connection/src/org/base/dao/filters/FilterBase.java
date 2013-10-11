/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

import org.base.dao.datasources.variations.IDataSourceVariation;

/**
 *
 * @author mproenza
 */
public abstract class FilterBase extends AbstractFilterBase {

    protected String fieldName;

    public FilterBase(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public abstract String getFilterExpression();
}
