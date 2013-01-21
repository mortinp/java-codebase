/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing;

import org.base.mapping.dsl.base.MappingDefinition;

/**
 *
 * @author mproenza
 */
public interface IMappingParser {
    public MappingDefinition parseMappingFile(String mappingFile);
}
