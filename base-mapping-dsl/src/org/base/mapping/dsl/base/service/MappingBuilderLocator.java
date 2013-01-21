/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.service;

import java.util.HashMap;
import java.util.Map;
import org.base.mapping.dsl.base.parsing.IMappingBuilder;
import org.base.mapping.dsl.base.parsing.SimpleMappingBuilder;

/**
 *
 * @author mproenza
 */
public class MappingBuilderLocator {
    
    public static String MAPPING_METADATA_FILES_DIRECTORY = "";
    
    private static Map<String, IMappingBuilder> buildersMap = new HashMap<String, IMappingBuilder>();
    
    public static IMappingBuilder getMappingBuilder(String metadataFileName) {
        IMappingBuilder mappingBuilder = buildersMap.get(metadataFileName);
        if(mappingBuilder == null) {
            mappingBuilder = new SimpleMappingBuilder(MAPPING_METADATA_FILES_DIRECTORY + "/" + metadataFileName);
            buildersMap.put(metadataFileName, mappingBuilder);
        }
        
        return mappingBuilder;
    }
    
}
