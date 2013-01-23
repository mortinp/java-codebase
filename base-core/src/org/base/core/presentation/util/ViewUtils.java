/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.util;

import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.base.components.models.TableMO;

/**
 *
 * @author Nolvis
 */
public class ViewUtils {
    
    public static void arrangeTableColumns(JTable jTable) {
        TableMO modelo = (TableMO)jTable.getModel();
        ArrayList lstLongitudes = modelo.getColumnsWidths();
        
        arrangeTableColumns(jTable, lstLongitudes);

        /*if (lstLongitudes != null) {

            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            for (int i = 0; i < jTable.getColumnCount(); i++) {
                TableColumn columna = jTable.getColumn(modelo.getColumnName(i));
                int intLongitud = (Integer) lstLongitudes.get(i);
                columna.setPreferredWidth(intLongitud * 10);
            }
        }*/
    }
    //yoel
    public static void arrangeTableColumns(JTable jTable,ArrayList lstLongitudes) {
        DefaultTableModel modelo = (DefaultTableModel)jTable.getModel();
        if (lstLongitudes != null) {

            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

            for (int i = 0; i < jTable.getColumnCount(); i++) {
                TableColumn columna = jTable.getColumn(modelo.getColumnName(i));
                int intLongitud = (Integer) lstLongitudes.get(i);
                columna.setPreferredWidth(intLongitud * 10);
            }
        }
    }
}
