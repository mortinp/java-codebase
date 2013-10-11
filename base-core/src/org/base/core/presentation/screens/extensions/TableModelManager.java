/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.components.models.TableMO;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.service.IEntityService;
import org.base.core.service.RegistryEntityManager;

/**
 *
 * @author martin
 */
public abstract class TableModelManager extends TableModelFilter implements ITableModelChangeReceiver {

    protected IEntityService entityManager;
    //private TableMO tableMO;

    public TableModelManager(String entityAlias, TableMO tableMO) {
        this.entityManager = RegistryEntityManager.getEntityManager(entityAlias);
        //this.tableMO = tableMO;
        this.entityTableModel = tableMO;
    }
    
    public TableModelManager(IEntityService entityManager, TableMO tableMO) {
        this.entityManager = entityManager;
        //this.tableMO = tableMO;
        this.entityTableModel = tableMO;
    }
    
    public TableModelManager(String title, String entityAlias, TableMO tableMO) {
        super(title);
        this.entityManager = RegistryEntityManager.getEntityManager(entityAlias);
        //this.tableMO = tableMO;
        this.entityTableModel = tableMO;
    }
    
    public TableModelManager(String title, IEntityService entityManager, TableMO tableMO) {
        super(title);
        this.entityManager = entityManager;
        //this.tableMO = tableMO;
        this.entityTableModel = tableMO;
    }

    @Override
    public void addModel(Object objModelo) {
        //try {
            entityManager.insert(objModelo);         
            addModelToTable(objModelo);            
            //mostrarAviso("Registro insertado satisfactoriamente");
        /*} catch (ExceptionWrapAsRuntime ex) {
            showWarningMessage(ex.getMessage());
            throw ex;
        }*/
    }

    @Override
    public void updateModel(int index, Object newObjModelo) {
        //try {
            entityManager.update(newObjModelo);
            updateModelInTable(index, newObjModelo);
            //mostrarAviso("Registro modificado satisfactoriamente");
        /*} catch (ExceptionWrapAsRuntime ex) {
            showWarningMessage(ex.getMessage());
            throw ex;
        }*/
    }

    @Override
    public void deleteModel(int index) {
        //if (showConfirmationMessage(MessageFactory.getMessage("msg_confirm_entity_deletion")) == JOptionPane.YES_OPTION) {
            Object obj = entityTableModel/*tableMO*/.getRowObjectAt(index);
            //try {
                entityManager.remove(obj);
                removeModelFromTable(index);
            /*} catch (ExceptionWrapAsRuntime ex) {
                showWarningMessage(ex.getMessage());
                //throw ex;
            }*/
        //}
    }

    protected abstract void addModelToTable(Object objNomenc);
    protected abstract void updateModelInTable(int index, Object objNomenc);
    protected abstract void removeModelFromTable(int index);
}
