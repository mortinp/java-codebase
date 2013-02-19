/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.util.List;
import org.base.dao.filters.IFilter;

/**
 *
 * @author Martin
 */
public interface IFilteredDAO extends IDAO {
    
    public void setFilters(IFilter ... filtros);
    
    public void setFilters(List<IFilter> filtros);

    public void addFilter(IFilter filtro);

    public void clearFilters();
    
    public void setFiltersAtEnd(IFilter ... filtros);
    
    public void setFiltersAtEnd(List<IFilter> filtros);
    
    public void addFilterAtEnd(IFilter filtro);

    public void clearFiltersAtEnd();
}
