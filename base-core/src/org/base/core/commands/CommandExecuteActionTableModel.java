/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import javax.swing.JTable;
import org.base.componentes.modelos.extensions.TableMOActionDecorator;

/**
 *
 * @author martin
 */
public class CommandExecuteActionTableModel implements ICommand {

    private JTable table;
    
    @Override
    public void execute() {
        ((TableMOActionDecorator)table.getModel()).ejecutarAccion();
    }    
}
