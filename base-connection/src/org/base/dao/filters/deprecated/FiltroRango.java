/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.deprecated;

import java.util.Date;

/**
 *
 * @author leo
 */
@Deprecated
public class FiltroRango extends FiltroAbstracto {

    private Object valorDesde;
    private Object valorHasta;

    public FiltroRango(String nombreCampo, Class tipo) {
        this(nombreCampo, tipo, null, null);
    }

    public FiltroRango(String nombreCampo, Class tipo, Object valorDesde, Object valorHasta) {
        super(nombreCampo, tipo);
        inicializarValores(valorDesde, valorHasta);
    }

    private void inicializarValores(Object valorDesde, Object valorHasta) {
        if (valorDesde == null || valorHasta == null) {
            if (this.tipo.getName().equals("java.lang.Integer") || this.tipo.getName().equals("java.lang.Byte") || this.tipo.getName().equals("java.lang.Double") || this.tipo.getName().equals("java.lang.Float")) {
                this.valorDesde = 0;
                this.valorHasta = 0;
            } else if (this.tipo.getName().equals("java.lang.String") || this.tipo.getName().equals("java.util.Character")) {
                this.valorDesde = "";
                this.valorHasta = "";
            } else if (this.tipo.getName().equals("java.lang.Boolean")) {
                this.valorDesde = true;
                this.valorHasta = true;
            } else if (this.tipo.getName().equals("java.util.Date")) {
                this.valorDesde = new Date();
                this.valorHasta = new Date();
            }
        } else {
            this.valorDesde = valorDesde;
            this.valorHasta = valorHasta;
        }
    }

    public Object getValorDesde() {
        return valorDesde;
    }

    public Object getValorHasta() {
        return valorHasta;
    }

    public void setValorDesde(Object valorDesde) {
        this.valorDesde = valorDesde;
    }

    public void setValorHasta(Object valorHasta) {
        this.valorHasta = valorHasta;
    }

    @Override
    public String getFilterExpression() {
        String strValorDesde = null;
        String strValorHasta = null;
        String strFiltro = null;

        strValorDesde = formarValor(this.valorDesde);
        strValorHasta = formarValor(this.valorHasta);

        strFiltro = strValorDesde + " AND " + strValorHasta;
        return this.nombreCampo + " BETWEEN " + strFiltro;
    }
}
