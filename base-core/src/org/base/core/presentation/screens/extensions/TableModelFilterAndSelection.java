/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.delegates.IModelReceiver;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.exceptions.DomainException;

/**
 *
 * @author martin
 */
public class TableModelFilterAndSelection extends TableModelFilter {
    
    IModelReceiver objModelReceiver = null;
    ITableModelChangeReceiver objTableModelChangeReceiver = null;
    
    public TableModelFilterAndSelection(String title, IModelReceiver objModelReceiver) {
        super(title);
        this.objModelReceiver = objModelReceiver;
    }
    
    public TableModelFilterAndSelection(String title, ITableModelChangeReceiver objTableModelChangeReceiver) { 
        super(title);
        this.objTableModelChangeReceiver = objTableModelChangeReceiver;  
    }
    
    public void sendObject(int filaIndex) throws DomainException {
        Object objModelo = entityTableModel.getRowObjectAt(filaIndex);
        
        if(objModelReceiver != null) objModelReceiver.receiveModel(objModelo);
        else if(objTableModelChangeReceiver != null) objTableModelChangeReceiver.addModel(objModelo);
        
        hide();
        initialize();
             
    }   
}
