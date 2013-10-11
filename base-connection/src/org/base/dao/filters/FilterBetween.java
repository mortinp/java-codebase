/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

import java.util.Date;
import org.base.dao.DAOUtils;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;

/**
 *
 * @author mproenza
 */
public class FilterBetween extends FilterBase {

    private Object bottomValue;
    private Object topValue;

    public FilterBetween(String fieldName, Object bottomValue, Object topValue) {
        super(fieldName);
        if(bottomValue == null || topValue == null) throw new ExceptionDBProgrammerMistake("Null value not accepted.");
        this.bottomValue = bottomValue;
        this.topValue = topValue;
    }

    @Override
    public String getFilterExpression() {
        /*String strValorDesde = null;
        String strValorHasta = null;
        String strFiltro = null;

        strValorDesde = DAOUtils.formatValueForQuery(this.bottomValue);
        strValorHasta = DAOUtils.formatValueForQuery(this.topValue);

        strFiltro = strValorDesde + " AND " + strValorHasta;
        return this.fieldName + " BETWEEN " + strFiltro;*/
        
        return dataSourceVariation.getBetweenExpression(
                fieldName, 
                /*DAOUtils.formatValueForQuery(*/this.bottomValue/*)*/,
                /*DAOUtils.formatValueForQuery(*/this.topValue)/*)*/;
    }
    
}
