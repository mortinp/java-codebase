/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.test;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.base.mapping.dsl.base.MappingDefinition;
import org.base.mapping.dsl.base.MappingEntry;
import org.base.mapping.dsl.base.MappingEntryModifier;
import org.base.mapping.dsl.base.parsing.IMappingBuilder;
import org.base.mapping.dsl.base.parsing.IMappingParser;
import org.base.mapping.dsl.base.parsing.SimpleMappingBuilder;
import org.base.mapping.dsl.base.parsing.SimpleMappingParser;
import org.base.mapping.dsl.test.base.Model;

/**
 *
 * @author mproenza
 */
public class SimpleMappingBuilderTest extends TestCase {
    
    private static final String TEST_FILE_PATH = "org/base/mapping/dsl/test/base/Model.map";
    
    public void testSimpleMappingParser() {
        IMappingParser parser = new SimpleMappingParser();
        MappingDefinition mappingDef = parser.parseMappingFile(TEST_FILE_PATH);
        
        assertEquals("org.base.mapping.dsl.test.base.Model", mappingDef.getClassName());
        assertEquals("tmodel", mappingDef.getTargetTableName());
        
        List<MappingEntry> entries = mappingDef.getEntries();
        assertEquals(5, entries.size());
        
        //
        MappingEntry entry0 = entries.get(0);
        assertEquals("getAttrString", entry0.getAccessorName());
        assertEquals("fieldString", entry0.getTargetFieldName());
        
        List<MappingEntryModifier> modifiers = entry0.getFieldsModifiers();
        assertEquals("key", modifiers.get(0).getName());
        assertEquals("noupdate", modifiers.get(1).getName());
        
        //
        MappingEntry entry1 = entries.get(1);
        assertEquals("getAttrInt", entry1.getAccessorName());
        assertEquals("fieldInt", entry1.getTargetFieldName());
        
        modifiers = entry1.getFieldsModifiers();
        assertEquals("key", modifiers.get(0).getName());
        assertEquals("noupdate", modifiers.get(1).getName());
        
        //
        MappingEntry entry2 = entries.get(2);
        assertEquals("getAttrFloat", entry2.getAccessorName());
        assertEquals("fieldFloat", entry2.getTargetFieldName());
        
        modifiers = entry2.getFieldsModifiers();
        assertEquals("noupdate", modifiers.get(0).getName());
        
        //
        MappingEntry entry3 = entries.get(3);
        assertEquals("getAttrBoolean", entry3.getAccessorName());//the entry should have the prefix 'get' because there is no @boolean tag specified (see Model.map)
        assertEquals("fieldBoolean", entry3.getTargetFieldName());
        
        modifiers = entry3.getFieldsModifiers();
        assertEquals(0, modifiers.size());
        
        //
        MappingEntry entry4 = entries.get(4);
        assertEquals("getAttrDate", entry4.getAccessorName());
        assertEquals("fieldDate", entry4.getTargetFieldName());
        
        modifiers = entry4.getFieldsModifiers();
        assertEquals(0, modifiers.size());
    }
    
    public void testSimpleMappingMap() throws SQLException {
        IMappingBuilder mappingBuilder = new SimpleMappingBuilder(TEST_FILE_PATH);
        Date date = new Date();
        Model model = new Model("aaa", 1, 3.4f, true, date);
        
        assertEquals("tmodel", mappingBuilder.getTableName());
        assertEquals("fieldString|fieldInt", mappingBuilder.getKeyFieldsNamesExpression());
        assertEquals("aaa|1", mappingBuilder.getKeyFieldsValuesExpression(model));
        
        Map<String, Object> insertionMap = mappingBuilder.getInsertionMap(model);
        assertEquals(insertionMap.get("fieldString"), "aaa");
        assertEquals(insertionMap.get("fieldInt"), 1);
        assertEquals(insertionMap.get("fieldFloat"), 3.4f);
        assertEquals(insertionMap.get("fieldBoolean"), true);
        assertEquals(insertionMap.get("fieldDate"), date);
        assertEquals(insertionMap.get("noExistingField"), null);
        
        Map<String, Object> updateMap = mappingBuilder.getUpdateMap(model);
        assertEquals(updateMap.get("fieldString"), null);
        assertEquals(updateMap.get("fieldInt"), null);
        assertEquals(updateMap.get("fieldFloat"), null);
        assertEquals(updateMap.get("fieldBoolean"), true);
        assertEquals(updateMap.get("fieldDate"), date);
        assertEquals(updateMap.get("noExistingField"), null);
    }
    
    public void testSimpleMappingBuild() {
        IMappingBuilder mappingBuilder = new SimpleMappingBuilder(TEST_FILE_PATH);
        Date date = new Date();
        
        Map<String, Object> mapFieldsValues = new HashMap<String, Object>();
        mapFieldsValues.put("fieldString", "StringValue");
        mapFieldsValues.put("fieldInt", 5);
        mapFieldsValues.put("fieldFloat", 1.3f);
        mapFieldsValues.put("fieldBoolean", false);
        mapFieldsValues.put("fieldDate", date);
        
        Model model = (Model) mappingBuilder.buildObject(mapFieldsValues);
        assertEquals("StringValue", model.getAttrString());
        assertEquals(new Integer(5), model.getAttrInt());
        assertEquals(1.3f, model.getAttrFloat());
        assertEquals(Boolean.FALSE, model.isAttrBoolean());
        assertEquals(date, model.getAttrDate());
    }
    
    public void testEntriesQueries() {
        IMappingBuilder mappingBuilder = new SimpleMappingBuilder(TEST_FILE_PATH);
        
        List<MappingEntry> entries = mappingBuilder.getEntries();
        assertEquals(5, entries.size());
        
        entries = mappingBuilder.getEntries("noExistingModifier");
        assertEquals(0, entries.size());
        
        entries = mappingBuilder.getEntries("noupdate");
        assertEquals(3, entries.size());
        assertEquals(entries.get(0).getTargetFieldName(), "fieldString");
        assertEquals(entries.get(1).getTargetFieldName(), "fieldInt");
        assertEquals(entries.get(2).getTargetFieldName(), "fieldFloat");
        
        entries = mappingBuilder.getEntries("noupdate", "!key");
        assertEquals(1, entries.size());
        assertEquals(entries.get(0).getTargetFieldName(), "fieldFloat");
        
        entries = mappingBuilder.getEntries("!noupdate", "key");
        assertEquals(0, entries.size());
    }
}
