/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.base.components.models.TableMO;
import org.base.core.presentation.screens.ScreenBase;

/**
 *
 * @author martin
 */
public class TableModelFilter extends ScreenBase {

    protected JTable table;
    protected TableMO entityTableModel;
    protected TableRowSorter<TableModel> tableModelRowSorter;

    public TableModelFilter(String title) {
        super(title);
    }

    public TableModelFilter() {
        super();
    }

    public void setTable(JTable table) {
        this.table = table;
    }
    
    public void setTableModel(TableMO modeloTabla) {
        entityTableModel = modeloTabla; 
        entityTableModel.setPageSize(1000000);
        initialize();
    }
    
    protected void initialize() {      
        table.setModel(entityTableModel);
        tableModelRowSorter = new TableRowSorter<TableModel>(entityTableModel);
        table.setRowSorter(tableModelRowSorter);
        table.updateUI();
    }
    
    protected void applyFilter(String texto) {
        this.tableModelRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }
    
    protected void applyFilter(String texto, int ... columnas) {
        this.tableModelRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, columnas));
    }  
}
