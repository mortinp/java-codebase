/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import javax.swing.JTable;
import org.base.componentes.models.TableMO;

/**
 *
 * @author mproenza
 */
public abstract class CommandDoStuffWithTableRow implements ICommand {
    
    private JTable table;
    
    public CommandDoStuffWithTableRow(JTable table) {
        this.table = table;
    }

    @Override
    public void execute() {
        int selectedRow = table.getSelectedRow();
        int realIndex = table.convertRowIndexToModel(selectedRow);
        if (selectedRow >= 0) {
            Object obj = ((TableMO)table.getModel()).getRowObjectAt(realIndex);
            doStuff(realIndex, obj);
        }
    }
    
    protected abstract void doStuff(int rowIndex, Object obj);    
}
