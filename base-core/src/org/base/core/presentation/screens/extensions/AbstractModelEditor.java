/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.exceptions.DomainException;

/**
 *
 * @author mproenza
 */
public abstract class AbstractModelEditor extends DefaultOperationalScreen {
    
    protected boolean editionMode;
    protected int editingRow = -1;
    
    public AbstractModelEditor() {
        super();
    }
    
    public AbstractModelEditor(String title) {
        super(title);
    }
    
    public void putEditionMode(int rowToEdit, Object model) {        
        this.editionMode = true;
        this.editingRow = rowToEdit;
        setModel(model);
        clearFields();
        showModelData();
    }
    
    public void quitEditionMode() {
        this.editionMode = false;
        createNewModel();
        clearFields();
    }
    
    protected abstract void sendToReceiver(Object model) throws DomainException;

    protected abstract void setModel(Object model);
    protected abstract void showModelData();
    
    protected abstract void createNewModel();
    protected abstract void clearFields();
    
}
