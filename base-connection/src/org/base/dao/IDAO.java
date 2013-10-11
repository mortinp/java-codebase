package org.base.dao;

import java.util.List;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryNotFound;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;
import org.base.dao.filters.IFilter;

/**
 *
 * @author martin
 */
public interface IDAO {

    public void insert(Object objModelo) throws ExceptionDBDuplicateEntry;
    
    public void insert(List lstModelos) throws ExceptionDBDuplicateEntry;

    public void update(Object objModelo) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere, ExceptionDBDuplicateEntry;
    
    public void update(List lstModelos) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere;

    public void remove(Object objModelo) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere;
    
    public void remove(List lstModelos) throws ExceptionDBEntryNotFound, ExceptionDBEntryReferencedElsewhere;
    
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
