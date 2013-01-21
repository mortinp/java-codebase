/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.base.core.commands.ICommand;

/**
 *
 * @author martin
 */
public class ListenerExecuteCommandOnAction implements ActionListener {
    private ICommand command;
    
    public ListenerExecuteCommandOnAction(ICommand command) {
        this.command = command;        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        command.execute();
    }
}
