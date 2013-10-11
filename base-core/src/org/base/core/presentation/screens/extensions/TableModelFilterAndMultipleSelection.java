/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import java.util.ArrayList;
import java.util.List;
import org.base.core.delegates.IMultipleModelsReceiver;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.exceptions.DomainException;

/**
 *
 * @author martin
 */
public class TableModelFilterAndMultipleSelection extends TableModelFilter {
    
    IMultipleModelsReceiver receiver = null;
    ITableModelChangeReceiver objTableModelChangeReceiver = null;
    
    public TableModelFilterAndMultipleSelection(IMultipleModelsReceiver receiver) {       
        this.receiver = receiver;
    }
    
    public TableModelFilterAndMultipleSelection(ITableModelChangeReceiver objTableModelChangeReceiver) {       
        this.objTableModelChangeReceiver = objTableModelChangeReceiver;  
    }
    
    public void sendObjects(int ... selectedIndexes) throws DomainException {
        List models = new ArrayList(selectedIndexes.length);
        for (int i : selectedIndexes) {
            models.add(entityTableModel.getRowObjectAt(i));
        }
        
        if(receiver != null) receiver.receiveModels(models);
        else if(objTableModelChangeReceiver != null) {
            for (Object object : models) {// TODO: this could be done better
                objTableModelChangeReceiver.addModel(object);
            }
        }
        
        hide();
        initialize();
             
    }   
}
