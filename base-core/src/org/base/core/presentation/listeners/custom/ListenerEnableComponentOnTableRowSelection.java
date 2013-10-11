/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners.custom;

import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author leo
 */
public class ListenerEnableComponentOnTableRowSelection implements ListSelectionListener {

    private JTable table;
    private List components;
    private Boolean active;

    public ListenerEnableComponentOnTableRowSelection(JTable table) {
        this.table = table;
        active = false;
    }

    public ListenerEnableComponentOnTableRowSelection(JTable table, List components) {
        this(table);
        this.components = components;        
    }
    
    public ListenerEnableComponentOnTableRowSelection(JTable table, Object ... components) {
        this(table);
        this.components = Arrays.asList(components);        
    }

    @Override
    public void valueChanged(ListSelectionEvent e) { 
        int intFila = this.table.getSelectedRow();              
        if (intFila >= 0 && this.table.getRowCount() > 0) {
            //this.table.changeSelection(intFila, intFila, false, false);
            if (this.components != null && !active) {
                for (int i = 0; i < components.size(); i++) {
                    JComponent object = (JComponent) components.get(i);
                    if(object != null)object.setEnabled(true);                    
                }
                active = true;
            }
        } else {
            for (int i = 0; i < components.size(); i++) {
                JComponent object = (JComponent) components.get(i);
                if(object != null)object.setEnabled(false);                
            }
            active = false;
        }
    }
    
    public void addComponent(Object component) {
        components.add(component);
    }
}
