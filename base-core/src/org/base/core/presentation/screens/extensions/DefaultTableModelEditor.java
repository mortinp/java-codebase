/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.commands.ICommand;
import org.base.core.domain.extensions.IRestorable;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.domain.ObjectsFactory;
import org.base.core.exceptions.DomainException;
import org.base.core.exceptions.ExceptionWrapAsRuntime;

/**
 *
 * @author mproenza
 */
public abstract class DefaultTableModelEditor extends TableModelEditor {
    
    protected Object objModel;
    private Object modelInitialState;
    private String modelAlias;
    
    boolean closeOnAccept = true;
    public void setCloseOnAccept(boolean closeOnAccept) {
        this.closeOnAccept = closeOnAccept;
    }
    
    ICommand onSuccess = null;
    public void setOnSuccess(ICommand onSuccess) {
        this.onSuccess = onSuccess;
    }
    
    
    public DefaultTableModelEditor() {
        super();
    }
    
    public DefaultTableModelEditor(String title) {
        super(title);
    }
    
    public DefaultTableModelEditor(String title, String modelAlias) {
        super(title);
        this.modelAlias = modelAlias;
    }
    
    public DefaultTableModelEditor(ITableModelChangeReceiver objRecibidor, String modelAlias) {
        super(objRecibidor);
        this.modelAlias = modelAlias;
    }
    
    public DefaultTableModelEditor(String title, ITableModelChangeReceiver objRecibidor) {
        super(title, objRecibidor);
    }
    
    public DefaultTableModelEditor(String title, ITableModelChangeReceiver objRecibidor, String modelAlias) {
        super(title, objRecibidor);
        this.modelAlias = modelAlias;
    }
    
    public void setAlias(String alias) {
        modelAlias = alias;
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
            if(onSuccess != null) onSuccess.execute();
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
