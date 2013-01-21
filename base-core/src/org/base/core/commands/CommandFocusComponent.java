/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import javax.swing.JComponent;
import javax.swing.JTable;

/**
 *
 * @author leo
 */
public class CommandFocusComponent implements ICommand {

    JComponent component;
    
    public CommandFocusComponent(JComponent component) {
        this.component = component;
    }
    
    @Override
    public void execute() {
        component.requestFocusInWindow();
        
        if(component instanceof JTable) {
            if(((JTable)component).getRowCount() != 0) {
                int intFilaSel = ((JTable)component).getSelectedRow();
                if(intFilaSel >= 0)
                    ((JTable)component).changeSelection(intFilaSel, 0, false, false);
                else
                   ((JTable)component).changeSelection(0, 0, false, false); 
            }
        }
    }
    
}
