/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import org.base.core.presentation.screens.IScreen;


/**
 *
 * @author martin
 */
public class CommandShowScreen implements ICommand {

    private IScreen screen;
    
    public CommandShowScreen(IScreen screen) {
        this.screen = screen;
    }
    
    @Override
    public void execute() {        
        screen.show();
    }
    
}
