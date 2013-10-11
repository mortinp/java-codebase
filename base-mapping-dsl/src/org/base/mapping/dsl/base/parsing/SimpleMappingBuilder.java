/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.parsing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.base.mapping.dsl.base.MappingDefinition;
import org.base.mapping.dsl.base.MappingEntry;
import org.base.mapping.dsl.base.exceptions.ExceptionMapping;
import org.base.mapping.dsl.base.parsing.inference.TypesInferreer;

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
            throw new ExceptionMapping(ex);
        } catch (InvocationTargetException ex) {
            throw new ExceptionMapping(ex);
        } catch (SecurityException ex) {
            throw new ExceptionMapping(ex);
        } catch (ClassNotFoundException ex) {
            throw new ExceptionMapping(ex);
        } catch (InstantiationException ex) {
            throw new ExceptionMapping(ex);
        } catch (IllegalAccessException ex) {
            throw new ExceptionMapping(ex);
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
        int i=0;
        for (MappingEntry entry : entries) {
            String accessorName = entry.getAccessorName();
            String fieldName = entry.getTargetFieldName();
            if(accessorName.startsWith("get")) accessorName = accessorName.substring(3);
            else if(accessorName.startsWith("is")) accessorName = accessorName.substring(2);
            String setAccessorName = "set" + accessorName;

            Object value = null;
            if(mapFieldsValues.containsKey(fieldName)) value = mapFieldsValues.get(fieldName);
            else {
                Object [] a = mapFieldsValues.keySet().toArray();
                value = mapFieldsValues.get((String)a[i]);
            }
            i++;
            
            // Sanity check
            if(value == null) {
                
                /* Version 1: Try to set default value */
                /*try {
                    String getMethodName = "get" + accessorName;
                    Method m = obj.getClass().getMethod(getMethodName);
                    value = getDefaultFor(m.getReturnType());
                } catch (NoSuchMethodException ex) {
                    throw new ExceptionMapping(ex);
                }*/
                
                /* Version 2: don't do anything */
                continue;
                
                /* Version 3: throw error */
                //throw new ExceptionMapping("Found null value for database field '" + fieldName +  "'. Could not map.");
            }
            
            Class parameterClass = value.getClass();
            Class inferAsType = value.getClass();
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
                    } else {
                        // Try with the return type of the get method
                        String getter = "get" + accessorName;
                        Method g = obj.getClass().getMethod(getter);
                        Class c = g.getReturnType();
                        
                        method = obj.getClass().getMethod(setAccessorName, c);
                        
                        inferAsType = c;
                    }
                } catch (NoSuchMethodException ex1) {
                    try {
                        // Try with the return type of the get method
                        String getter = "get" + accessorName;
                        Method g = obj.getClass().getMethod(getter);
                        Class c = g.getReturnType();

                        method = obj.getClass().getMethod(setAccessorName, c);

                        inferAsType = c;
                    } catch (NoSuchMethodException ex2) {
                        // TODO: Throw error???
                        Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex2);
                    } catch (SecurityException ex2) {
                        // TODO: Throw error???
                        Logger.getLogger(SimpleMappingBuilder.class.getName()).log(Level.SEVERE, null, ex2);
                    }
                }
            }
            
            if(method != null) {
                if(!inferAsType.getName().equalsIgnoreCase(parameterClass.getName())) {
                    if("java.util.Date".equals(inferAsType.getName())) method.invoke(obj, TypesInferreer.inferDate(value));
                    else if(inferAsType.isInstance(BigDecimal.class) || "java.math.BigDecimal".equals(inferAsType.getName())) method.invoke(obj, TypesInferreer.inferBigDecimal(value));
                    else if(inferAsType.isInstance(Float.class) || "float".equals(inferAsType.getName())) method.invoke(obj, TypesInferreer.inferFloat(value));
                } else  method.invoke(obj, value);
            }
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
    
    
    // <editor-fold defaultstate="collapsed" desc="GETS FOR DEFAULTS">
    private static Object getDefaultObject() {
        return null;
    }

    private static boolean getDefaultBoolean() {
        return false;
    }

    private static Float getDefaultFloat() {
        return 0.0f;
    }
    
    private static Double getDefaultDouble() {
        return 0.0;
    }
    
    private static Integer getDefaultInteger() {
        return 0;
    }
    
    private static Long getDefaultLong() {
        return new Long(0);
    }

    private static String getDefaultString() {
        return "";
    }

    private static Date getDefaultDate() {
        return new Date();
    }
    
    private static Object getDefaultFor(Class clazz) {
        if(clazz == Double.class) return getDefaultDouble();
        else if(clazz == Float.class) return getDefaultFloat();
        else if(clazz == Boolean.class) return getDefaultBoolean();
        else if(clazz == String.class) return getDefaultString();
        else if(clazz == Date.class) return getDefaultDate();
        else if(clazz == Integer.class) return getDefaultInteger();
        else getDefaultObject();

        return 0;
    }
    // </editor-fold>
}
