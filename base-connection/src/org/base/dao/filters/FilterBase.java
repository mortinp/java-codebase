/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mproenza
 */
public abstract class FilterBase implements IFilter {

    protected String fieldName;

    public FilterBase(String fieldName) {
        this.fieldName = fieldName;
    }

    protected String formatValue(Object valor) {
        if(valor != null) {
            String strTipo = valor.getClass().getName();
            if (strTipo.equals("java.util.Date") || strTipo.equals("java.sql.Date")) {
                Date fecha = (Date) valor;
                Format objFormatoFecha = getDateFormatter();
                String strValor = objFormatoFecha.format(fecha);
                return "'" + strValor + "'";
            } else if (strTipo.equals("java.lang.String") || strTipo.equals("java.lang.Character")) {
                return "'" + valor.toString() + "'";
            } else {
                return valor.toString();
            } 
        }
        else {
            return "NULL";
        }
    }
    
    public static Format getDateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public abstract String getFilterExpression();
}
