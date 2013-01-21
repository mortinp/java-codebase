/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.base.core.commands.ICommand;

/**
 *
 * @author martin
 */
public class ListenerExecuteCommandOnCustomKey implements KeyListener {
       
    //
    private int keyCode;
    private ICommand command;
    
    public ListenerExecuteCommandOnCustomKey(int keyCode, ICommand command) {
        this.keyCode = keyCode;
        this.command = command;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(keyCode == e.getKeyCode()) {  
            command.execute(); 
            e.consume();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
