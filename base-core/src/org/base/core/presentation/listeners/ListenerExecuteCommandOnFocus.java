/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.base.core.commands.ICommand;

/**
 *
 * @author martin
 */
public class ListenerExecuteCommandOnFocus implements FocusListener {
    
    private ICommand command; 
    
    public ListenerExecuteCommandOnFocus(ICommand command) {
        this.command = command;
    }

    @Override
    public void focusGained(FocusEvent e) {
        command.execute();
    }

    @Override
    public void focusLost(FocusEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
