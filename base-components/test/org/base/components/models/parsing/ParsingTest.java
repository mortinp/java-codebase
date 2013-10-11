/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.base.components.testing.base.SomeModel;

/**
 *
 * @author Martin
 */
public class ParsingTest extends TestCase {
    
    public void testDynamicColumnsGeneration() {
        
        List colNames = new ArrayList();
        colNames.add("I");
        colNames.add("F");
        colNames.add("S");
        colNames.add("EXTRA {param}");
        colNames.add("D");
        
        List colDataSources = new ArrayList();
        colDataSources.add("intField");
        colDataSources.add("floatField");
        colDataSources.add("stringField");
        colDataSources.add("extra(param)");
        colDataSources.add("dateField");
        
        
        List colTypes = new ArrayList();
        colTypes.add("Integer");
        colTypes.add("Float");
        colTypes.add("String");
        colTypes.add("String");
        colTypes.add("Date");
        
        List colWidths = new ArrayList();
        colWidths.add(10);
        colWidths.add(10);
        colWidths.add(10);
        colWidths.add(10);
        colWidths.add(10);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("param", Arrays.asList(1, 2, 3));
        
        ITableModelDataExtractor parser = new DefaultTableModelDataExtractor(
                colNames, 
                colTypes, 
                colWidths, 
                colDataSources, 
                params);
        
        assertEquals(parser.getColumnNames().get(0), "I");
        assertEquals(parser.getColumnNames().get(1), "F");
        assertEquals(parser.getColumnNames().get(2), "S");
        assertEquals(parser.getColumnNames().get(3), "EXTRA 1");
        assertEquals(parser.getColumnNames().get(4), "EXTRA 2");
        assertEquals(parser.getColumnNames().get(5), "EXTRA 3");
        assertEquals(parser.getColumnNames().get(6), "D");
        
        assertEquals(parser.getColumnTypes().get(0), "Integer");
        assertEquals(parser.getColumnTypes().get(1), "Float");
        assertEquals(parser.getColumnTypes().get(2), "String");
        assertEquals(parser.getColumnTypes().get(3), "String");
        assertEquals(parser.getColumnTypes().get(4), "String");
        assertEquals(parser.getColumnTypes().get(5), "String");
        assertEquals(parser.getColumnTypes().get(6), "Date");        
        
        Date d = new Date();
        SomeModel obj = new SomeModel(3, 5.6f, "Some text", d);
        
        List data = parser.getDataArray(obj);
        
        assertEquals(data.get(0), 3);
        assertEquals(data.get(1), 5.6f);
        assertEquals(data.get(2), "Some text");
        assertEquals(data.get(3), "Method called with param = 1");
        assertEquals(data.get(4), "Method called with param = 2");
        assertEquals(data.get(5), "Method called with param = 3");
        assertEquals(data.get(6), d);
    }
}
