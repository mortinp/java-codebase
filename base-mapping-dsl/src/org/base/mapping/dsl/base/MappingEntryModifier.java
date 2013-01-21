/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base;

/**
 *
 * @author mproenza
 */
public class MappingEntryModifier {
    String name;

    public MappingEntryModifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        return getName().equals(((MappingEntryModifier)o).getName());
    }
}
