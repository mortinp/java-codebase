/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.deprecated;

/**
 *
 * @author leo
 */
@Deprecated
public class FiltroSimple extends FiltroAbstracto {

    public static String OPERADOR_IGUALDAD = " = ";
    public static String OPERADOR_DESIGUALDAD = " <> ";
    public static String OPERADOR_MAYOR = " > ";
    public static String OPERADOR_MAYOR_IGUAL = " >= ";
    public static String OPERADOR_MENOR = " <";
    public static String OPERADOR_MENOR_IGUAL = " <= ";
    
    private Object valor;
    private String operador;

    public FiltroSimple(String nombreCampo, Class tipo, Object valor) {
        super(nombreCampo, tipo);
        this.valor = valor;
        this.operador = null;
    }

    public FiltroSimple(String nombreCampo, Class tipo, Object valor, String operador) {
        this(nombreCampo, tipo, valor);
        this.operador = operador;
    }

    @Override
    public String getFilterExpression() {
        this.operador = this.operador == null ? FiltroSimple.OPERADOR_IGUALDAD : this.operador;
        String strFiltro = formarValor(this.valor);
        return nombreCampo + this.operador + strFiltro;
    }
}
