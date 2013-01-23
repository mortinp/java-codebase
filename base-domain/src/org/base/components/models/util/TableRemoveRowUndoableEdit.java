/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.util;

import javax.swing.undo.AbstractUndoableEdit;
import org.base.components.models.TableMO;

/**
 *
 * @author leo
 */
public class TableRemoveRowUndoableEdit extends AbstractUndoableEdit {

    private TableMO tableMO;
    private Object dataRowOld;
    private Object dataRowNew;
    private int row;

    public TableRemoveRowUndoableEdit(TableMO tableMO, Object dataRowOld, Object dataRowNew, int row) {
        this.tableMO = tableMO;
        this.dataRowOld = dataRowOld;
        this.dataRowNew = dataRowNew;
        this.row = row;
    }

    @Override
    public String getPresentationName() {
        return "Remove Row";
    }

    @Override
    public void undo() {
        super.undo();
        /*ArrayList arr = tableMO.getDataArray(dataRowOld);
        tableMO.getData().add(row, arr);*/     
        tableMO.getRowObjects().add(row, dataRowOld);
        tableMO.fireTableDataChanged();
    }

    @Override
    public void redo() {
        super.redo();
        /*ArrayList arr = tableMO.getDataArray(dataRowNew);
        tableMO.getData().add(row, arr);*/
        tableMO.getRowObjects().remove(row);
        tableMO.fireTableDataChanged();
    }
}
