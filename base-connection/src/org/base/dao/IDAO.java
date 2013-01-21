package org.base.dao;

import java.util.List;
import org.base.dao.exceptions.DuplicateEntryException;
import org.base.dao.exceptions.EntryNotFoundException;
import org.base.dao.exceptions.ForeignKeyException;
import org.base.dao.filters.IFilter;

/**
 *
 * @author martin
 */
public interface IDAO {

    public void insert(Object objModelo) throws DuplicateEntryException;
    
    public void insert(List lstModelos) throws DuplicateEntryException;

    public void update(Object objModelo) throws EntryNotFoundException, ForeignKeyException;
    
    public void update(List lstModelos) throws EntryNotFoundException, ForeignKeyException;

    public void remove(Object objModelo) throws EntryNotFoundException, ForeignKeyException;
    
    public void remove(List lstModelos) throws EntryNotFoundException, ForeignKeyException;
    
    public Object findOne(Object valorLlave);
    
    public List findAll();
    
    public void setFilters(IFilter ... filtros);
    
    public void setFilters(List<IFilter> filtros);

    public void addFilter(IFilter filtro);

    public void clearFilters();
    
    public void setFiltersAtEnd(IFilter ... filtros);
    
    public void setFiltersAtEnd(List<IFilter> filtros);
    
    public void addFilterAtEnd(IFilter filtro);

    public void clearFiltersAtEnd();
}
