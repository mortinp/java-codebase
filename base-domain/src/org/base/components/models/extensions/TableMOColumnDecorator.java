/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.extensions;

import org.base.componentes.modelos.TableMO;

/**
 *
 * @author martin
 */
public abstract class TableMOColumnDecorator extends TableMO {
    
    private TableMO componente;
    private int columnIndex;
    private String columnName;
    
    public TableMOColumnDecorator(TableMO componente) {
        this.componente = componente;
    }

    @Override
    public int getColumnCount() {
        return componente.getColumnCount() + 1;
    }

    @Override
    public String getColumnName(int column) {
        if(column == columnIndex) return columnName;
        else return componente.getColumnName(column);
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
        if (col == columnIndex) return true;
        else return componente.isCellEditable(row, col);
    }

    /*@Override
    public void setValueAt(Object value, int row, int col) {
        (dataArray.get(row)).set(col, value);
        fireTableCellUpdated(row, col);
    }    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(dataArray.isEmpty()) return null;
        
        int realRow = rowIndex + (pageOffset * pageSize);
        return dataArray.get(realRow).get(columnIndex);        
    }*/
}
