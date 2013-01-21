/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.components.models.base.DomainFactory;
import org.base.core.exceptions.DomainException;
import org.base.core.presentation.validation.IValidationStatusReceiver;

/**
 *
 * @author mproenza
 */
public abstract class DefaultTableModelEditor extends TableModelEditor implements IValidationStatusReceiver {
    
    protected Object objModel;
    private Object modelInitialState;
    private String modelAlias;
    
    boolean validationPassed = true;
    
    boolean closeOnAccept = true;
    public void setCloseOnAccept(boolean closeOnAccept) {
        this.closeOnAccept = closeOnAccept;
    }
    
    public DefaultTableModelEditor(ITableModelChangeReceiver objRecibidor, String modelAlias) {
        super(objRecibidor);
        this.modelAlias = modelAlias;
    }

    @Override
    protected void setModel(Object model) {
        if(model instanceof IRestorable) modelInitialState = ((IRestorable)model).getCurrentState();
        objModel = model;
    }
    
    @Override
    protected void createNewModel() {
        objModel = DomainFactory.getModelo(modelAlias);
    }

    @Override
    protected abstract void showModelData();

    @Override
    protected abstract void clearFields();

    @Override
    public void validateFailed(String failureMessage) {
        showWarningMessage(failureMessage);
        validationPassed = false;
    }

    @Override
    public void validatePassed() {
        validationPassed = true;
    }
    
    public void accept() {
        if(validationPassed) {
            try {
                buildModel();
                sendToReceiver(objModel);
                if(closeOnAccept) close();
            } catch (DomainException ex) {
                rollback();
                showWarningMessage(ex.getMessage());
            }
        }
    }
    
    public void rollback() {
        if(objModel instanceof IRestorable) 
            ((IRestorable)objModel).restoreFromState(modelInitialState);
    }
    
    protected abstract void buildModel() throws DomainException ;
}
