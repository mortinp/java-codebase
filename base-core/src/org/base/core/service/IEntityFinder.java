/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.service;

import java.util.List;
import org.base.dao.filters.IFilter;

/**
 *
 * @author martin
 */
public interface IEntityFinder {
    public List<Object> findAll();
    
    public List<Object> findAll(List<IFilter> listaFiltros);
    
    public List<Object> findAll(IFilter ... listaFiltros);
    
    public List<Object> findAll(List<IFilter> listaFiltros, 
                                       List<IFilter> listaFiltrosTerminales);

    public Object findOne(String nomenclador, Object keyValue);    

    public Object findOne(Object keyValue);
    
    public Object findOne(Object keyValue, List<IFilter> listaFiltro);
}
