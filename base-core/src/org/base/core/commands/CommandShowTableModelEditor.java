/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.commands;

import javax.swing.JTable;
import org.base.components.models.TableMO;
import org.base.core.presentation.screens.ScreenBase;
import org.base.core.domain.extensions.ICrudVerifiable;
import org.base.core.presentation.screens.extensions.TableModelEditor;
import org.base.utils.messages.MessageFactory;

/**
 *
 * @author martin
 */
public class CommandShowTableModelEditor implements ICommand {

    private JTable table;
    private TableModelEditor editorScreen;
    private boolean modificationMode = false;

    public CommandShowTableModelEditor(TableModelEditor editorScreen, JTable table) {
        this.table = table;
        this.editorScreen = editorScreen;
    }

    public CommandShowTableModelEditor(TableModelEditor editorScreen, JTable table, boolean modificationMode) {
        this(editorScreen, table);
        this.modificationMode = modificationMode;
    }

    @Override
    public void execute() {
        if (modificationMode && table.getRowCount() != 0) {
            TableMO tableModel = (TableMO) table.getModel();
            
            int selectedRow;
            if(table.getSelectedRow() < 0) selectedRow = 0;
            else selectedRow = table.getSelectedRow();
            
            int realRow;
            int index = table.convertRowIndexToModel(selectedRow);
            if (tableModel.getPageCount() == 1) {
                realRow = index;
            } else {
                int pageOffset = tableModel.getPageOffset();
                int pageSize = tableModel.getPageSize();
                realRow = index + (pageOffset * pageSize);
            }

            Object model = tableModel.getRowObjectAt(realRow);
            
            if(model instanceof ICrudVerifiable && !((ICrudVerifiable)model).isEditable()) {
                ScreenBase.showWarningMessage(MessageFactory.getMessage("msg_verification_object_not_editable", model.toString()));
                return;
            }   

            editorScreen.putEditionMode(realRow, model);
            editorScreen.show();
        } else {
            editorScreen.quitEditionMode();
            editorScreen.show();
        }
    }
}
