/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.exceptions.DomainException;

/**
 *
 * @author martin
 */
public abstract class TableModelEditor extends AbstractModelEditor {

    protected ITableModelChangeReceiver receiver;
    
    public TableModelEditor() {
        super();
    }
    
    public TableModelEditor(String title) {
        super(title);
    }
    
    public TableModelEditor(ITableModelChangeReceiver receiver) {
        super();
        this.receiver = receiver;
    }
    
    public TableModelEditor(String title, ITableModelChangeReceiver receiver) {
        super(title);
        this.receiver = receiver;
    }
    
    public void setReceiver(ITableModelChangeReceiver receiver) {
        this.receiver = receiver;
    }       
    
    @Override
    protected void sendToReceiver(Object model) throws DomainException {
        if (!editionMode) {
            receiver.addModel(model);                
        } else {
            receiver.updateModel(editingRow, model);                
        }
    }

    @Override
    protected abstract void setModel(Object model);
    @Override
    protected abstract void showModelData();
    
    @Override
    protected abstract void createNewModel();
    @Override
    protected abstract void clearFields();
}
