/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.testing.base;

import java.util.Date;

/**
 *
 * @author Martin
 */
public class SomeModel {
    
    int intField;
    float floatField;
    String stringField;
    Date dateField;

    public SomeModel(int intField, float floatField, String stringField, Date dateField) {
        this.intField = intField;
        this.floatField = floatField;
        this.stringField = stringField;
        this.dateField = dateField;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    public float getFloatField() {
        return floatField;
    }

    public void setFloatField(float floatField) {
        this.floatField = floatField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }
    
    public String getExtra(Integer i) {
        return "Method called with param = " + i;
    }
    
}
