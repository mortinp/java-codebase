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
public class SeleccionModeloFiltroTabla extends FiltroModeloTabla {
    
    IModelReceiver objRecibidor;
    
    public SeleccionModeloFiltroTabla(IModelReceiver objRecibidor) {       
        this.objRecibidor = objRecibidor;  
    }
    
    public void enviarModelo(int filaIndex) {
        Object objModelo = entityTableModel.getRowObjectAt(filaIndex);
        hide();
        inicializar();
        objRecibidor.receiveModel(objModelo);     
    }   
}
