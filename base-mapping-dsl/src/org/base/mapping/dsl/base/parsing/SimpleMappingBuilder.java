/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.base.mapping.dsl.base.MappingDefinition;
import org.base.mapping.dsl.base.MappingEntry;

/**
 *
 * @author mproenza
 */
public class SimpleMappingBuilder implements IMappingBuilder {
    MappingDefinition mappingDef;
    
    public SimpleMappingBuilder(String mappingFile) {
        IMappingParser parser = new SimpleMappingParser();
        mappingDef = parser.parseMappingFile(mappingFile);
    }

    @Override
    public String getTableName() {
        return mappingDef.getTargetTableName();
    }

    @Override
    public String getKeyFieldsNamesExpression() {
        String keyFieldsExpression = "";
        String separator = "";
        List<MappingEntry> entries = mappingDef.getEntries();
        for (MappingEntry entry : entries) {
            if(entry.hasFieldModifier("key")) {
                keyFieldsExpression += separator + entry.getTargetFieldName();
                separator = "|";
            }
        }
        return keyFieldsExpression;
    }

    @Override
    public String getKeyFieldsValuesExpression(Object obj) {
        String keyValuesExpression = "";
        String separator = "";
        List<MappingEntry> entries = mappingDef.getEntries();
        for (MappingEntry entry : entries) {
            if(entry.hasFieldModifier("key")) {
                keyValuesExpression += separator + extractDataThroughObjectAccessor(obj, entry.getAccessorName());
                separator = "|"; 
            }
        }
        return keyValuesExpression;
    }

    @Override
    public Map<String, Object> getInsertionMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<MappingEntry> entries = mappingDef.getEntries();
        for (MappingEntry entry : entries) {
            if(!entry.hasFieldModifier("autonumeric")) {
                map.put(entry.getTargetFieldName(), extractDataThroughObjectAccessor(obj, entry.getAccessorName()));
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> getUpdateMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<MappingEntry> entries = mappingDef.getEntries();
        for (MappingEntry entry : entries) {
            if(!entry.hasFieldModifier("autonumeric") && 
               !entry.hasFieldModifier("noupdate")&& 
               !entry.hasFieldModifier("key")) {
                map.put(entry.getTargetFieldName(), extractDataThroughObjectAccessor(obj, entry.getAccessorName()));
            }
        }
        return map;
    }
    
    @Override
    public Object buildObject(Map<String, Object> mapFieldsValues) {
        Object obj = null;
        try {
            obj = Class.forName(mappingDef.getClassName()).newInstance();
            
            List<MappingEntry> entries = mappingDef.getEntries();
            fillObjectFromEntries(obj, entries, mapFieldsValues);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj;
    }
    
    @Override
    public List<MappingEntry> getEntries(String ... modifiersNames) {
        List<MappingEntry> allEntries = mappingDef.getEntries();
        if(modifiersNames == null || modifiersNames.length == 0) return allEntries;
        
        List<MappingEntry> selectedEntries = new ArrayList<MappingEntry>();
        for (MappingEntry entry : allEntries) {
            boolean add = false;
            for (String modifierName : modifiersNames) {
                boolean different = false;
                if(modifierName.startsWith("!")) {
                    different = true;
                    modifierName = modifierName.substring(1);
                }
                if((!different && entry.hasFieldModifier(modifierName)) ||
                    (different && !entry.hasFieldModifier(modifierName))) {
                    add = true;
                } else {
                    add = false; break;
                }
            } 
            if(add) selectedEntries.add(entry);
        }
        return selectedEntries;
    }
    
    public static void fillObjectFromEntries(Object obj, List<MappingEntry> entries, Map<String, Object> mapFieldsValues) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MappingEntry entry : entries) {
            String accessorName = entry.getAccessorName();
            String fieldName = entry.getTargetFieldName();
            if(accessorName.startsWith("get")) accessorName = accessorName.substring(3);
            else if(accessorName.startsWith("is")) accessorName = accessorName.substring(2);
            String setAccessorName = "set" + accessorName;

            Object value = mapFieldsValues.get(fieldName);
            Class parameterClass = value.getClass();
            Method method = null;
            try {
                method = obj.getClass().getMethod(setAccessorName, parameterClass);
            }catch (NoSuchMethodException ex) {
                try {
                    if(parameterClass == Integer.class) {
                        method = obj.getClass().getMethod(setAccessorName, Integer.TYPE);
                    } else if(parameterClass == Float.class) {
                        method = obj.getClass().getMethod(setAccessorName, Float.TYPE);
                    } else if(parameterClass == Boolean.class) {
                        method = obj.getClass().getMethod(setAccessorName, Boolean.TYPE);
                    } else if(parameterClass == Long.class) {
                        method = obj.getClass().getMethod(setAccessorName, Long.TYPE);
                    } else if(parameterClass == Double.class) {
                        method = obj.getClass().getMethod(setAccessorName, Double.TYPE);
                    } else if(parameterClass == java.sql.Date.class) {
                        method = obj.getClass().getMethod(setAccessorName, java.util.Date.class);
                    }
                } catch (NoSuchMethodException ex1) {
                    Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            method.invoke(obj, value);
        }
    }
    
    private Object extractDataThroughObjectAccessor(Object obj, String accessorName, Object ... params) {
        Object value = null;
        Class<?> [] parameterTypes = new Class<?>[params.length];
        try {
            if(params.length != 0) {
                for (int i = 0; i < params.length; i++) {
                    parameterTypes[i] = params[i].getClass();
                }
                Method method = obj.getClass().getMethod(accessorName, parameterTypes);
                value = method.invoke(obj, params);
            } else {
                Method method = obj.getClass().getMethod(accessorName);  
                value = method.invoke(obj);
            }
            
        } catch (NoSuchMethodException ex) {
            //try to access with a different prefix for the accesor (exchange get/is)
            if(accessorName.startsWith("get")) accessorName = "is" + accessorName.substring(3);
            else if(accessorName.startsWith("is")) accessorName = "get" + accessorName.substring(2);
            try {
                if(params.length != 0) {
                    Method method = obj.getClass().getMethod(accessorName, parameterTypes);
                    value = method.invoke(obj, params);
                } else {
                    Method method = obj.getClass().getMethod(accessorName);  
                    value = method.invoke(obj);
                }
            } catch (IllegalAccessException ex1) {
                throw new RuntimeException(ex1.getMessage());
            } catch (IllegalArgumentException ex1) {
                throw new RuntimeException(ex1.getMessage());
            } catch (InvocationTargetException ex1) {
                throw new RuntimeException(ex1.getMessage());
            } catch (NoSuchMethodException ex1) {
                throw new RuntimeException(ex1.getMessage());
            }
        } catch (SecurityException ex) {
            throw new RuntimeException(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex.getMessage());
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        
        return value;
    }
}
