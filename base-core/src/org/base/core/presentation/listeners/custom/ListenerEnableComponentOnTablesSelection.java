/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author mproenza
 * 
 * This listener receives an array of objects to be bound together. The objects can be JTable object or any other
 * that extends JComponent (the rest will be skipped). All the JComponent objects are bound to the selection
 * of items in the JTable objects specified, in a way that the components are enabled only when ALL the tables
 * have at least one item selected. You MUST register this listener in EVERY JTable object you want to be checked
 * in order to get right results.
 */
public class ListenerEnableComponentOnTablesSelection implements ListSelectionListener {

    private List tables = new ArrayList();
    private List components = new ArrayList();
   
    public ListenerEnableComponentOnTablesSelection(Object ... componentsToBind) {
        List temp = Arrays.asList(componentsToBind);
        for (Object obj : temp) {
            if(obj instanceof JTable) tables.add(obj);
            else if(obj instanceof JComponent)components.add(obj);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        boolean enable = true;
        for (Object table : tables) {
            JTable t = (JTable) table;
            int intFila = t.getSelectedRow();
            if(intFila >= 0 && t.getRowCount() > 0) continue;
            else {enable = false; break;}
        }
        for (int i = 0; i < components.size(); i++) {
            JComponent object = (JComponent) components.get(i);
            object.setEnabled(enable);                    
        }
    }
}
