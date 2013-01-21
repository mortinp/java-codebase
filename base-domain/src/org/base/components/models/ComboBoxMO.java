/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author leo
 */
public class ComboBoxMO extends AbstractListModel implements MutableComboBoxModel, Serializable {

    // <editor-fold defaultstate="collapsed" desc="DECLARACIÓN DE VARIABLES">
    protected List dataArray;
    Object selectedObject;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Constructs an empty DefaultComboBoxModel object.
     */
    public ComboBoxMO() {
        dataArray = new ArrayList();
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an collection of objects.
     *
     * @param list  an array of Object objects
     */
    public ComboBoxMO(Collection list) {
        dataArray = new ArrayList(list);

        if (dataArray.size() > 0) {
            selectedObject = dataArray.get(0);
        }
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with
     * an ArrayList of objects.
     *
     * @param list  an array of Object objects
     */
    public ComboBoxMO(ArrayList list) {
        dataArray = new ArrayList(list);

        if (dataArray.size() > 0) {
            selectedObject = dataArray.get(0);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MÉTODOS INTERFACE">   
    @Override
    public int getSize() {
        return dataArray.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (index >= 0 && index < dataArray.size()) {
            return dataArray.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void addElement(Object obj) {
        dataArray.add(obj);
        fireIntervalAdded(this, dataArray.size() - 1, dataArray.size() - 1);
        if (dataArray.size() == 1 && selectedObject == null && obj != null) {
            setSelectedItem(obj);
        }
    }

    @Override
    public void removeElement(Object obj) {
        int index = dataArray.indexOf(obj);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    @Override
    public void insertElementAt(Object obj, int index) {
        dataArray.add(index, obj);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedObject) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        dataArray.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals(anObject))
                || selectedObject == null && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectedObject;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MÉTODOS PROPIOS">   
    public void setSelectedIndex(int index) {
        setSelectedItem(getElementAt(index));
    }

    public int getIndexOf(Object anObject) {
        return dataArray.indexOf(anObject);
    }
    
    public void clearModel() {
        this.dataArray.clear();
    }
    // </editor-fold>
}
