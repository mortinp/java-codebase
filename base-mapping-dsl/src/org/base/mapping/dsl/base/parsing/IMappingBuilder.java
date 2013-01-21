/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing;

import java.util.List;
import java.util.Map;
import org.base.mapping.dsl.base.MappingEntry;

/**
 *
 * @author mproenza
 */
public interface IMappingBuilder {
    
    public String getTableName();
    public String getKeyFieldsNamesExpression();
    public String getKeyFieldsValuesExpression(Object obj);
    
    public Map<String, Object> getInsertionMap(Object obj);
    public Map<String, Object> getUpdateMap(Object obj);
    
    public Object buildObject(Map<String, Object> mapFieldsValues);
    public List<MappingEntry> getEntries(String ... modifiersNames);
}
