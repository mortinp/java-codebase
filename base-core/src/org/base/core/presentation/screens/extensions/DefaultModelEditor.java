/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.domain.extensions.IRestorable;
import org.base.core.domain.ObjectsFactory;
import org.base.core.delegates.IModelReceiver;
import org.base.core.exceptions.DomainException;
import org.base.core.exceptions.ExceptionWrapAsRuntime;

/**
 *
 * @author mproenza
 */
public abstract class DefaultModelEditor extends ModelEditor {
    
    protected Object objModel;
    private Object modelInitialState;
    private String modelAlias;
    
    boolean closeOnAccept = true;
    public void setCloseOnAccept(boolean closeOnAccept) {
        this.closeOnAccept = closeOnAccept;
    }
    
    public DefaultModelEditor(IModelReceiver objRecibidor, String modelAlias) {
        super(objRecibidor);
        this.modelAlias = modelAlias;
    }
    
    public DefaultModelEditor(String title, IModelReceiver objRecibidor, String modelAlias) {
        super(title, objRecibidor);
        this.modelAlias = modelAlias;
    }

    @Override
    protected void setModel(Object model) {
        if(model instanceof IRestorable) modelInitialState = ((IRestorable)model).getCurrentState();
        objModel = model;
    }
    
    @Override
    protected void createNewModel() {
        objModel = ObjectsFactory.getObject(modelAlias);
    }

    @Override
    protected abstract void showModelData();

    @Override
    protected abstract void clearFields();
    
    @Override
    protected void doAccept() {
        try {
            buildModel();
            sendToReceiver(objModel);
            
            if(closeOnAccept) close();
            else if(editionMode == false) {
                createNewModel();
                clearFields();
            }
        } catch (DomainException ex) {
            rollback();
            showWarningMessage(ex.getMessage());
        } catch (ExceptionWrapAsRuntime ex) {
            rollback();
            showWarningMessage(ex.getMessage());
        }
    }
    
    public void rollback() {
        if(objModel instanceof IRestorable) 
            ((IRestorable)objModel).restoreFromState(modelInitialState);
    }
    
    protected abstract void buildModel() throws DomainException ;
}
