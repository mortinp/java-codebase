/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models;

import org.base.components.models.parsing.ITableModelDataExtractor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import org.base.components.models.util.TableRemoveRowUndoableEdit;

/**
 *
 * @author martin
 */
public class TableMO extends AbstractTableModel {

    // <editor-fold defaultstate="collapsed" desc="DATA MODEL">
    /**
     * The list of objects represented in the rows of the table.
     * These objects are used to extract data from them and show that data in the
     * table view.
     */
    ArrayList lstRowObjects = new ArrayList();
    
    /**
     * Matrix to hold the value of each cell in the table.
     * This matrix is used for performance reasons, so that we don't have to recalculate
     * the values of the cells every time the table suffers a change.
     */
    ArrayList<ArrayList> dataMatrix = new ArrayList<ArrayList>();

    ITableModelDataExtractor dataConfigParser;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="STRUCTURAL MODEL">
    ArrayList colNames = null;
    ArrayList colWidths = null; //longitudes de las columnas
    List colEditables = null;  //columnas editables
    
    int pageOffset;
    int pageSize;
    
    class CompTable implements Comparator<ArrayList> {
        int column;

        public CompTable(int column) {
            this.column = column;
        }

        @Override
        public int compare(ArrayList arg0, ArrayList arg1) {
            return ((String) arg0.get(column)).compareTo((String) arg1.get(column));

        }
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public TableMO() {
        super();
        this.colEditables = new ArrayList();
        this.pageSize = 1000000;
    }
    
    public TableMO(List colNames, List colWidths, ITableModelDataExtractor dataConfigParser) {
        this();
        this.colNames = new ArrayList(colNames);
        this.colWidths = new ArrayList(colWidths);
        this.dataConfigParser = dataConfigParser;
    }

    public TableMO(Collection dataList, List colNames) {
        this();
        this.colNames = new ArrayList(colNames);
    }

    public TableMO(ArrayList<ArrayList> data, List colNames) {
        this();
        this.colNames = new ArrayList(colNames);
        this.dataMatrix = data;       
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MÉTODOS INTERFACE">
    @Override
    public int getRowCount() {
        //return this.dataArray.size();
        return Math.min(pageSize, dataMatrix.size());
    }

    @Override
    public int getColumnCount() {
        return this.colNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return (String) this.colNames.get(column);
    }

    @Override
    public Class getColumnClass(int c) {
        if (getValueAt(0, c) == null) {
            return Object.class;
        }
        return getValueAt(0, c).getClass();
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return colEditables.indexOf(col) != -1;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        (dataMatrix.get(row)).set(col, value);
        fireTableCellUpdated(row, col);
    }  

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(dataMatrix.isEmpty()) return null;
        
        int realRow = rowIndex + (pageOffset * pageSize);
        return dataMatrix.get(realRow).get(columnIndex);        
    }

    @Override
    public void fireTableDataChanged() {
        int first = pageOffset * pageSize;
        int last = first + pageSize;
        dataMatrix = dataConfigParser.getDataMatrix(lstRowObjects, first, last);
        super.fireTableDataChanged();
    }

    @Override
    public void fireTableRowsInserted(int firstRow, int lastRow) {
        //TODO: aqui un while es mas general
        dataMatrix.add(firstRow, dataConfigParser.getDataArray(lstRowObjects.get(firstRow)));
        super.fireTableRowsInserted(firstRow, lastRow);
    }

    @Override
    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        //TODO: aqui un while es mas general
        dataMatrix.remove(firstRow);
        super.fireTableRowsDeleted(firstRow, lastRow);
    }

    @Override
    public void fireTableRowsUpdated(int firstRow, int lastRow) {
        //TODO: aqui un while es mas general
        this.dataMatrix.set(firstRow, dataConfigParser.getDataArray(lstRowObjects.get(firstRow)));
        super.fireTableRowsUpdated(firstRow, lastRow);
    }
    //</editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="MÉTODOS PROPIOS">
    public void sortByRow(int row) {
        CompTable comp = new CompTable(row);
        Collections.sort(dataMatrix, comp);
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        return getCellRenderer(row, column);
    }

    public List getEditableColumns() {
        return colEditables;
    }

    public void setEditableColumns(ArrayList colEditables) {
        this.colEditables = colEditables;
    }
    
    public void setEditableColumns(int ... colEditables) {
        for (int i : colEditables) {
            this.colEditables.add(i);
        }
    }

    public void addEditableColumn(int col) {
        colEditables.add(col);
    }

    public ArrayList getColumnsWidths() {
        return colWidths;
    }

    public void setColumnsWidths(ArrayList colWidths) {
        this.colWidths = colWidths;
    }       

    public ArrayList getColumnsNames() {
        return colNames;
    }

    public void setColumnsNames(ArrayList colNames) {
        this.colNames = colNames;
    }
    
    public ArrayList getRowObjects(int ... at) {
        ArrayList rowObjs = new ArrayList();
        if(at == null || at.length == 0) rowObjs = lstRowObjects;
        else {
            for (int i : at) {
                rowObjs.add(lstRowObjects.get(i));
            }
        }
        return rowObjs;
    }

    public void setRowObjects(List lstRowObjects) {
        //this.lstRowObjects = (ArrayList) lstRowObjects;
        this.lstRowObjects = new ArrayList(lstRowObjects);
        fireTableDataChanged();
    }
    
    public Object getRowObjectAt(int rowIndex) {
        return this.lstRowObjects.get(rowIndex);
    }    

    public void addRowObject(Object obj) {
        this.lstRowObjects.add(obj);
        fireTableRowsInserted(lstRowObjects.size() - 1, lstRowObjects.size() - 1);
    }

    public void updateRowObject(int rowIndex, Object obj) {
        this.lstRowObjects.set(rowIndex, obj);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeRowObject(int rowIndex) {
        Object objOld = this.lstRowObjects.remove(rowIndex);
        UndoableEditListener listeners[] = getListeners(UndoableEditListener.class);
        TableRemoveRowUndoableEdit undo = new TableRemoveRowUndoableEdit(this, objOld, objOld, rowIndex);
        UndoableEditEvent editEvent = new UndoableEditEvent(this, undo);

        for (UndoableEditListener listener : listeners) {
            listener.undoableEditHappened(editEvent);
        }

        fireTableRowsDeleted(rowIndex, rowIndex);
    }
    
    public void removeRowObject(Object o) {
        int rowIndex = this.lstRowObjects.indexOf(o);
        Object objOld = this.lstRowObjects.remove(o);
        UndoableEditListener listeners[] = getListeners(UndoableEditListener.class);
        TableRemoveRowUndoableEdit undo = new TableRemoveRowUndoableEdit(this, objOld, objOld, rowIndex);
        UndoableEditEvent editEvent = new UndoableEditEvent(this, undo);

        for (UndoableEditListener listener : listeners) {
            listener.undoableEditHappened(editEvent);
        }

        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public int getPageCount() {
        //return (int) Math.ceil((double) dataArray.size() / pageSize);
        return (int) Math.ceil((double) lstRowObjects.size() / pageSize);
    }

    public int getRealRowCount() {
        //return dataArray.size();
        return lstRowObjects.size();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int size) {
        if (size == pageSize) {
            return;
        }
        //int oldPageSize = pageSize;
        pageSize = size;
        //pageOffset = (oldPageSize * pageOffset) / pageSize;
        pageOffset = 0;
        fireTableDataChanged();
    }

    public boolean pageDown() {
        if (pageOffset < getPageCount() - 1) {
            pageOffset++;
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    public boolean pageUp() {
        if (pageOffset > 0) {
            pageOffset--;
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    public void clearModel() {
        this.dataMatrix.clear();
        this.lstRowObjects.clear();
    }

    public String getPagesString() {
        //TODO: configure string in file
        return "Página: " + (pageOffset + 1) + " de " + getPageCount();
    }

    public ArrayList<ArrayList> getData() {
        return dataMatrix;
    }    

    public void addUndoableEditListener(UndoableEditListener listener) {
        listenerList.add(UndoableEditListener.class, listener);
    }
    // </editor-fold>
}
