/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import javax.swing.JOptionPane;
import org.base.components.models.TableMO;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.service.IEntityService;
import org.base.core.service.RegistryEntityManager;

/**
 *
 * @author martin
 */
public abstract class TableModelManager extends TableModelFilter implements ITableModelChangeReceiver {

    protected IEntityService servicioNomenclador;
    private TableMO tableMO;

    public TableModelManager(String nombreNomenclador, TableMO tableMO) {
        servicioNomenclador = RegistryEntityManager.getEntityManager(nombreNomenclador);
        this.tableMO = tableMO;
    }

    @Override
    public void addModel(Object objModelo) {
        //try {
            servicioNomenclador.insert(objModelo);         
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
            servicioNomenclador.update(newObjModelo);
            updateModelInTable(index, newObjModelo);
            //mostrarAviso("Registro modificado satisfactoriamente");
        /*} catch (ExceptionWrapAsRuntime ex) {
            showWarningMessage(ex.getMessage());
            throw ex;
        }*/
    }

    @Override
    public void deleteModel(int index) {
        //UndoManager undoManager = new UndoManager();
        //tableMO.addUndoableEditListener(undoManager);

        if (showConfirmationMessage("Se eliminará un registro de la base de datos. ¿Está seguro?") == JOptionPane.YES_OPTION) {
            Object obj = tableMO.getRowObjectAt(index);
            //try {
                servicioNomenclador.remove(obj);
                removeModelFromTable(index);
            /*} catch (ExceptionWrapAsRuntime ex) {
                showWarningMessage(ex.getMessage());
                throw ex;
            }*/
        }
    }

    protected abstract void addModelToTable(Object objNomenc);
    protected abstract void updateModelInTable(int index, Object objNomenc);
    protected abstract void removeModelFromTable(int index);
}
