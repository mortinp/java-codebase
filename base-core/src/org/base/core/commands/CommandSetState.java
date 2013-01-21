/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import org.base.core.delegates.ISimpleStateMachine;


/**
 *
 * @author martin
 */
public class CommandSetState implements ICommand {
    
    private ISimpleStateMachine receiver;
    private int state;
    
    public CommandSetState(ISimpleStateMachine receiver, 
                              int state) {
        this.receiver = receiver;
        this.state = state;
    }

    @Override
    public void execute() {
        receiver.setState(state);
    }    
}
