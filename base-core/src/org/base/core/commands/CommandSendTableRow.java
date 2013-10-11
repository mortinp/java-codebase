/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import javax.swing.JTable;
import org.base.core.delegates.ITableModelChangeReceiver;
import org.base.core.exceptions.DomainException;
import org.base.utils.exceptions.ExceptionUnknownError;

/**
 *
 * @author martin
 */
public class CommandSendTableRow extends CommandDoStuffWithTableRow {

    private ITableModelChangeReceiver receiver;
    
    public static final int MODE_ADITION = 1;
    public static final int MODE_UPDATE= 2;
    int mode = MODE_ADITION;

    public CommandSendTableRow(JTable table,
                               ITableModelChangeReceiver receiver,
                               int mode) {
        super(table);
        this.receiver = receiver;
        this.mode = mode;
    }

    @Override
    protected void doStuff(int rowIndex, Object obj) {
        try {
            if(mode == MODE_ADITION) receiver.addModel(obj);
            else if(mode == MODE_UPDATE) receiver.updateModel(rowIndex, obj);
        } catch (DomainException ex) {
            throw new ExceptionUnknownError(ex);
        }
    }
}
