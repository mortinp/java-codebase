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
public class MappingDefinition {
    String className;
    String targetTableName;
    
    List<MappingEntry> entries = new ArrayList<MappingEntry>();

    public List<MappingEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<MappingEntry> entries) {
        this.entries = entries;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public void addEntry(MappingEntry entry) {
        entries.add(entry);
    }
}
