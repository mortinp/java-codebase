/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.exceptions.DomainException;

/**
 *
 * @author martin
 */
public class CommandDeleteTableRow extends CommandDoStuffWithTableRow {

    private ITableModelChangeReceiver receiver;

    public CommandDeleteTableRow(JTable table,
                                 ITableModelChangeReceiver receiver) {
        super(table);
        this.receiver = receiver;
    }

    @Override
    protected void doStuff(int rowIndex, Object obj) {
        try {
            receiver.deleteModel(rowIndex);
        } catch (DomainException ex) {
            Logger.getLogger(CommandDeleteTableRow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
