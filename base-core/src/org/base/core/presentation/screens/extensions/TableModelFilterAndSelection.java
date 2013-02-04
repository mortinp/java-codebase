/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.delegates.IModelReceiver;

/**
 *
 * @author martin
 */
public class TableModelFilterAndSelection extends TableModelFilter {
    
    IModelReceiver objRecibidor;
    
    public TableModelFilterAndSelection(IModelReceiver objRecibidor) {       
        this.objRecibidor = objRecibidor;  
    }
    
    public void enviarModelo(int filaIndex) {
        Object objModelo = entityTableModel.getRowObjectAt(filaIndex);
        hide();
        initialize();
        objRecibidor.receiveModel(objModelo);     
    }   
}
