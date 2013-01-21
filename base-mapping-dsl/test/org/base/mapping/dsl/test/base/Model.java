/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.test.base;

import java.util.Date;

/**
 *
 * @author mproenza
 */
public class Model {
    
    String attrString;
    int attrInt;
    float attrFloat;
    boolean attrBoolean;
    Date attrDate;

    public Model() {
    }

    public Model(String attrString, int attrInt, float attrFloat, boolean attrBoolean, Date attrDate) {
        this.attrString = attrString;
        this.attrInt = attrInt;
        this.attrFloat = attrFloat;
        this.attrBoolean = attrBoolean;
        this.attrDate = attrDate;
    }

    public Boolean isAttrBoolean() {
        return attrBoolean;
    }

    public void setAttrBoolean(boolean attrBoolean) {
        this.attrBoolean = attrBoolean;
    }

    public Date getAttrDate() {
        return attrDate;
    }

    public void setAttrDate(Date attrDate) {
        this.attrDate = attrDate;
    }

    public Float getAttrFloat() {
        return attrFloat;
    }

    public void setAttrFloat(float attrFloat) {
        this.attrFloat = attrFloat;
    }

    public Integer getAttrInt() {
        return attrInt;
    }

    public void setAttrInt(int attrInt) {
        this.attrInt = attrInt;
    }

    public String getAttrString() {
        return attrString;
    }

    public void setAttrString(String attrString) {
        this.attrString = attrString;
    }
}
