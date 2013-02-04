/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.searches.modules;

import org.base.dao.searches.IDataMappingStrategy;

/**
 *
 * @author martin
 */
@Deprecated
public abstract class SearchModuleFilterDecorator implements IDataSearchModule {
    
    IDataSearchModule moduloBusquedaGeneral; 
    Object objetoBaseBusqueda;
    
    public SearchModuleFilterDecorator(IDataSearchModule moduloBusquedaGeneral, Object objetoBaseBusqueda) {
        this.moduloBusquedaGeneral = moduloBusquedaGeneral;
        this.objetoBaseBusqueda = objetoBaseBusqueda;
    }
    
    protected abstract String getCondicionBusqueda();
    
    protected abstract Object[] getParametrosObjeto(Object objeto);

    @Override
    public String getQuery() {
        return formarSQL(moduloBusquedaGeneral.getQuery(), getCondicionBusqueda());
    }

    @Override
    public Object[] getParameters() {
        Object[] params = moduloBusquedaGeneral.getParameters();
	Object[] otrosParams = getParametrosObjeto(objetoBaseBusqueda);
        return unirListas(params, otrosParams);
    }

    @Override
    public IDataMappingStrategy getDataMappingStrategy() {
        return moduloBusquedaGeneral.getDataMappingStrategy();
    }
    
    private Object[] unirListas(Object[] destino, Object[] origen) {
        //destino.addAll(origen);
        return destino;
    }
    
    private String formarSQL(String sql, String condicion) {
        //TODO: esto es temporal: arreglar!!!
        return sql + " WHERE " + condicion;
    }
    
}
