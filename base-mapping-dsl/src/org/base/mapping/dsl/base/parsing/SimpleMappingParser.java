/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.base.mapping.dsl.base.MappingDefinition;
import org.base.mapping.dsl.base.MappingEntry;
import org.base.mapping.dsl.base.MappingEntryModifier;

/**
 *
 * @author mproenza
 */
public class SimpleMappingParser implements IMappingParser {
    
    MappingDefinition mappingDef;

    @Override
    public MappingDefinition parseMappingFile(String mappingFile) {
        mappingDef = new MappingDefinition();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    ClassLoader.getSystemClassLoader().getResourceAsStream(mappingFile)));
            
            String headerLine = reader.readLine();
            processHeaderLine(headerLine);
            while(processEntry(reader.readLine())){}

        } catch (IOException ex) {
            Logger.getLogger(SimpleMappingParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(SimpleMappingParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return mappingDef;
    }
    
    private void processHeaderLine(String line) {
        String[] parts = line.split("->");
        String className = parts[0].trim();
        String tableName = parts[1].replace("{", "").trim();
        //String tableName = line.subSequence(line.indexOf("(") + 1, line.indexOf(")")).toString().trim();
        mappingDef.setClassName(className);
        mappingDef.setTargetTableName(tableName);
    }
    
    private boolean processEntry(String line) {
        if(line == null || "}".equals(line)) return false;
        
        MappingEntry entry = new MappingEntry();
        List<MappingEntryModifier> accessorsModifiers = new ArrayList<MappingEntryModifier>();
        List<MappingEntryModifier> fieldsModifiers = new ArrayList<MappingEntryModifier>();
        
        String [] parts = line.split(":");
        
        String [] attrParts = parts[0].split("@");
        String attrName = attrParts[0].trim();
        String accessorName = "get";//default
        for (int i = 1; i < attrParts.length; i++) {
            accessorsModifiers.add(new MappingEntryModifier(attrParts[i].trim()));
            if(attrParts[i].trim().equals("boolean"))
                accessorName = "is";
        }
        accessorName += 
                attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());
        
        String [] fieldParts = parts[1].split("@");
        String fieldName = fieldParts[0].trim();
        for (int i = 1; i < fieldParts.length; i++) {
            fieldsModifiers.add(new MappingEntryModifier(fieldParts[i].trim()));
        }

        entry.setAccessorName(accessorName);
        entry.setTargetFieldName(fieldName);
        entry.setAccessorsModifiers(accessorsModifiers);
        entry.setFieldsModifiers(fieldsModifiers);
        
        mappingDef.addEntry(entry);
        
        return true;
    }
}
