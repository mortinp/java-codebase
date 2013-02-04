/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author leo
 */
public class ListMO extends AbstractListModel implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="DECLARACIÓN DE VARIABLES">
    protected ArrayList dataArray;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public ListMO() {
        this(0);
    }

    public ListMO(int size) {
        this.dataArray = new ArrayList(size);
    }

    public ListMO(ArrayList dataArray) {
        this.dataArray = dataArray;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MÉTODOS INTERFACE">   
    @Override
    public int getSize() {
        return this.dataArray.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (this.dataArray.size() > index) {
            return this.dataArray.get(index);
        }
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MÉTODOS PROPIOS">   
    public void addElement(Object obj) {
        int index = dataArray.size();
        this.dataArray.add(obj);
        fireIntervalAdded(this, index, index);
    }

    public void removeElementAt(int index) {
        if (this.dataArray.size() > index) {
            this.dataArray.remove(index);
            fireIntervalRemoved(this, index, index);
        }
    }

    public ArrayList getData() {
        return this.dataArray;
    }

    public void setDataArray(ArrayList dataArray) {
        this.dataArray = dataArray;
    }
    // </editor-fold>
}
