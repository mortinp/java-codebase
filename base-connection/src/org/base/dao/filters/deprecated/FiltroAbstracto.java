/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.deprecated;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.base.dao.filters.IFilter;

/**
 *
 * @author leo
 */
@Deprecated
public abstract class FiltroAbstracto implements IFilter {

    protected String nombreCampo;
    protected Class tipo;

    public FiltroAbstracto(String nombreCampo, Class tipo) {
        this.nombreCampo = nombreCampo;
        this.tipo = tipo;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }

    public Class getTipo() {
        return tipo;
    }

    public void setTipo(Class tipo) {
        this.tipo = tipo;
    }

    protected String formarValor(Object valor) {
        if(valor != null) {
            String strTipo = tipo.getName();
            if (strTipo.equals("java.util.Date")) {
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
            return "null";
        }
    }
    
    public static Format getDateFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    @Override
    public abstract String getFilterExpression();
}
