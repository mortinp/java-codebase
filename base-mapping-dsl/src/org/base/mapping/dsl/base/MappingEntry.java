/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mproenza
 */
public class MappingEntry {
    String accessorName;
    String targetFieldName;
    
    List<MappingEntryModifier> accessorsModifiers = new ArrayList<MappingEntryModifier>();
    List<MappingEntryModifier> fieldsModifiers = new ArrayList<MappingEntryModifier>();

    public String getAccessorName() {
        return accessorName;
    }

    public void setAccessorName(String accessorName) {
        this.accessorName = accessorName;
    }

    public List<MappingEntryModifier> getAccessorsModifiers() {
        return accessorsModifiers;
    }

    public void setAccessorsModifiers(List<MappingEntryModifier> accessorsModifiers) {
        this.accessorsModifiers = accessorsModifiers;
    }
    
    public boolean hasAccessordModifier(String modifierName) {
        for (MappingEntryModifier mod : accessorsModifiers) {
            if(mod.getName().equals(modifierName)) return true;
        }
        return false;
    }

    public String getTargetFieldName() {
        return targetFieldName;
    }

    public void setTargetFieldName(String targetFieldName) {
        this.targetFieldName = targetFieldName;
    }
    
    public List<MappingEntryModifier> getFieldsModifiers() {
        return fieldsModifiers;
    }

    public void setFieldsModifiers(List<MappingEntryModifier> modifiers) {
        this.fieldsModifiers = modifiers;
    }
    
    public boolean hasFieldModifier(String modifierName) {
        for (MappingEntryModifier mod : fieldsModifiers) {
            if(mod.getName().equals(modifierName)) return true;
        }
        return false;
    }
}
