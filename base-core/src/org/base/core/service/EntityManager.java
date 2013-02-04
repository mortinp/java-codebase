/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.service;

import java.util.List;
import org.base.core.domain.AutonumericModel;
import org.base.dao.DAOAutonumeric;
import org.base.dao.DAOFactory;
import org.base.dao.IDAO;
import org.base.dao.DAOSimpleMetadataMapper;
import org.base.dao.filters.IFilter;
import org.base.core.exceptions.ExceptionWrapAsRuntime;
import org.base.dao.exceptions.DuplicateEntryException;
import org.base.dao.exceptions.EntryNotFoundException;
import org.base.dao.exceptions.ForeignKeyException;

/**
 *
 * @author martin
 */
public class EntityManager implements IEntityService, IEntityFinder {

    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE VARIABLES">
    //Interface DAO
    private IDAO entityDAO;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public EntityManager(String entityAlias) {
        entityDAO = DAOFactory.getDAO(entityAlias);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS DE SERVICIO">
    @Override
    public void insert(Object objModelo) {
        try {
            if (objModelo instanceof AutonumericModel && !(entityDAO instanceof DAOSimpleMetadataMapper)) {
                int llave = ((DAOAutonumeric)entityDAO).insertarObtenerId(objModelo);
                ((AutonumericModel) objModelo).setKeyValue(llave);
            } else {
                entityDAO.insert(objModelo);
            }
        } catch (DuplicateEntryException ex) {
            throw new ExceptionWrapAsRuntime(ex);
        }
    }

    @Override
    public void update(Object objModelo) {
        try {
            entityDAO.update(objModelo);
        } catch (ForeignKeyException ex) {
            throw new ExceptionWrapAsRuntime(ex);
        } catch (EntryNotFoundException ex) {
            throw new ExceptionWrapAsRuntime(ex);
        }
    }
    
    @Override
    public void remove(Object objModelo) {
        try {
            entityDAO.remove(objModelo);
        } catch (ForeignKeyException ex) {
            throw new ExceptionWrapAsRuntime(ex);
        } catch (EntryNotFoundException ex) {
            throw new ExceptionWrapAsRuntime(ex);
        }
    }
    
    @Override
    public List<Object> findAll() {
        return entityDAO.findAll();
    }
    
    @Override
    public List<Object> findAll(IFilter ... listaFiltros) {
        if (listaFiltros != null && listaFiltros.length > 0) {
            entityDAO.setFilters(listaFiltros);                     
        }  
        
        //obtener listado y quitar los filtros del nomencladorDAO
        List<Object> lstModelos = entityDAO.findAll();        
        entityDAO.clearFilters();
        
        return lstModelos;
    }
    
    @Override
    public List<Object> findAll(List<IFilter> listaFiltros) {
        if (listaFiltros != null && listaFiltros.size() > 0) {
            entityDAO.setFilters(listaFiltros);                     
        }  
        
        //obtener listado y quitar los filtros del nomencladorDAO
        List<Object> lstModelos = entityDAO.findAll();        
        entityDAO.clearFilters();
        
        return lstModelos;      
    }
    
    @Override
    public List<Object> findAll(List<IFilter> listaFiltros, List<IFilter> listaFiltrosTerminales) {
        if (listaFiltros != null && listaFiltros.size() > 0) {
            entityDAO.setFilters(listaFiltros);                    
        }
        
        if (listaFiltrosTerminales != null && listaFiltrosTerminales.size() > 0) {
            entityDAO.setFiltersAtEnd(listaFiltrosTerminales);                      
        }
        
        //obtener listado y quitar los filtros del nomencladorDAO
        List<Object> lstModelos = entityDAO.findAll();        
        entityDAO.clearFilters();
        entityDAO.clearFiltersAtEnd();
        
        return lstModelos;      
    }

    @Override
    public Object findOne(String nomenclador, Object keyValue) {
        return DAOFactory.getDAO(nomenclador).findOne(keyValue);
    }

    @Override
    public Object findOne(Object keyValue) {
        return entityDAO.findOne(keyValue);
    }
    
    @Override
    public Object findOne(Object keyValue, List<IFilter> listaFiltro) {
        if (listaFiltro != null && listaFiltro.size() > 0) {
            for (IFilter filtro : listaFiltro) {
                entityDAO.addFilter(filtro);
            }          
        }
        
        //obtener listado y quitar los filtros del nomencladorDAO
        Object objModelo = entityDAO.findOne(keyValue);        
        entityDAO.clearFilters();
        
        return objModelo; 
    }
    // </editor-fold>
}
