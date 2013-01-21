/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing;

/**
 *
 * @author Martin
 */
public class ReferenceMappingBuilder extends SimpleMappingBuilder {
    
    String referencedFieldName;
    
    public ReferenceMappingBuilder(String referencedFieldName, String mappingFile) {
        super(mappingFile);
        this.referencedFieldName = referencedFieldName;
    }

    public String getReferencedFieldName() {
        return referencedFieldName;
    }
}
