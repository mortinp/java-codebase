/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.base.core.commands.ICommand;

/**
 *
 * @author mproenza
 */
public class ListenerExecuteCommandOnClick implements MouseListener {
    
    ICommand command;
    private boolean doubleClick = false;
    
    public ListenerExecuteCommandOnClick(ICommand command) {
        this.command = command;
    }

    public ListenerExecuteCommandOnClick(ICommand command, boolean doubleClick) {
        this(command);
        this.doubleClick = doubleClick;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(doubleClick) {
            if(e.getClickCount() >= 2) command.execute();
        } else {
            command.execute();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
