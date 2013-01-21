/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

import org.base.exceptions.system.SystemException;

/**
 *
 * @author mproenza
 */
public class FilterBetween extends FilterBase {

    private Object bottomValue;
    private Object topValue;

    public FilterBetween(String fieldName, Object bottomValue, Object topValue) {
        super(fieldName);
        if(bottomValue == null || topValue == null) throw new SystemException("Null value unaccepted.");
        this.bottomValue = bottomValue;
        this.topValue = topValue;
    }

    @Override
    public String getFilterExpression() {
        String strValorDesde = null;
        String strValorHasta = null;
        String strFiltro = null;

        strValorDesde = formatValue(this.bottomValue);
        strValorHasta = formatValue(this.topValue);

        strFiltro = strValorDesde + " AND " + strValorHasta;
        return this.fieldName + " BETWEEN " + strFiltro;
    }
    
}
