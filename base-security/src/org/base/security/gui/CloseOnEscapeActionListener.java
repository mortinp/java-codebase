/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.gui;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;

/**
 *
 * @author martin
 */

//Si adicionamos un objeto de esta clase a un componente, cuando se aprieta 'Enter' sobre
//el mismo se transfiere el foco al proximo componente
public class CloseOnEscapeActionListener implements  KeyListener {
    
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyPressed(KeyEvent e) {
        if(KeyEvent.VK_ESCAPE == e.getKeyCode()) {            
            Component dialogo = ((Component) e.getSource()).getParent();
            while(dialogo.getParent() != null) dialogo = dialogo.getParent();
            ((JDialog)dialogo).dispose();
        }            
    }

    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
