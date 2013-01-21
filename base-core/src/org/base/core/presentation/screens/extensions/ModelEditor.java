/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.delegates.IModelReceiver;
import org.base.core.exceptions.DomainException;

/**
 *
 * @author martin
 */
public abstract class ModelEditor extends AbstractModelEditor {

    protected IModelReceiver receiver;

    public ModelEditor(IModelReceiver receiver) {
        super();
        this.receiver = receiver;
    }
    
    public ModelEditor(String title, IModelReceiver receiver) {
        super(title);
        this.receiver = receiver;
    }
    
    @Override
    protected void sendToReceiver(Object model) throws DomainException {
        receiver.receiveModel(model);
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
