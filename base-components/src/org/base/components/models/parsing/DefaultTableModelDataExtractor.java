/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.components.models.parsing;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.base.components.models.formatting.IFormatter;
import org.base.utils.types.UtilLists;

/**
 *
 * @author Martin
 */
public class DefaultTableModelDataExtractor implements ITableModelDataExtractor {
    List colTypes = null;
    List colDataSources = null;
    
    List colNames = null;
    List colWidths = null;
    
    //I think it's better to have formatters be static, in order to force all formattings in every part of the application
    //be consistent, by configuring formatters only once.
    static Map<String, IFormatter> formatters = new HashMap<String, IFormatter>();
    
    
    //
    Map<String, Object> paramKeyMap = null;

    public DefaultTableModelDataExtractor(List colNames, List colTypes, List colWidths, List colDataSources) {
        this.colNames = colNames;
        this.colTypes = colTypes;
        this.colWidths = colWidths;
        this.colDataSources = colDataSources;
    }
    
    public DefaultTableModelDataExtractor(List colNames, List colTypes, List colWidths, List colDataSources, Map<String, Object> paramKeyMap) {
        this.colNames = colNames;
        this.colTypes = colTypes;
        this.colWidths = colWidths;
        this.colDataSources = colDataSources;
        this.paramKeyMap = paramKeyMap;
        
        preprocessDynamicColumns();
    }
    
    // TODO: This method is public only for tests
    public final void preprocessDynamicColumns() {
        for (int i = 0; i < colNames.size(); i++) {
            String columnName = (String) colNames.get(i);
            String columnType = (String) colTypes.get(i);
            Integer columnWidth = (Integer)colWidths.get(i);
            if(columnName.contains("{")) {
                int index = columnName.indexOf("{");
                //String [] colDef = columnName.split("{");
                String leftPart = columnName.substring(0, index)/*colDef[0]*/;
                String rightPart = columnName.substring(index + 1)/*colDef[1]*/;

                String colNamePrefix = leftPart;
                String paramKey = rightPart.replace("}", "").trim();

                Object param = paramKeyMap.get(paramKey);
                if(param instanceof List) { // Iterate and generate dynamic columns
                    List params = (List) param;
                    
                    colNames.remove(i);
                    colTypes.remove(i);
                    colWidths.remove(i);
                    int off = 0;
                    for (Object object : params) {
                        insertColumn(i + off, colNamePrefix + object.toString(), columnType, columnWidth);
                        off++;
                    }
                } else {
                    colNames.remove(i);
                    colTypes.remove(i);
                    colWidths.remove(i);
                    
                    insertColumn(i, colNamePrefix + param.toString(), columnType, columnWidth);
                }
            }
        }
    }

    public static void setFormatters(Map<String, IFormatter> formatters) {
        DefaultTableModelDataExtractor.formatters = formatters;
    }
    
    @Override
    public List getColumnNames() {
        return colNames;
    }
    
    @Override
    public List getColumnWidths() {
        return colWidths;
    }
    
    @Override
    public List getColumnTypes() {
        return colTypes;
    }
    
    @Override
    public ArrayList<ArrayList> getDataMatrix(List lstDataObjects, int first, int last) {
        ArrayList<ArrayList> matrix = new ArrayList<ArrayList>();
        for (int i = first; (i < last) && (i < lstDataObjects.size()/*sanity check*/); i++) {
            Object dataObject = lstDataObjects.get(i);
            matrix.add(getDataArray(dataObject));
        }
        return matrix;
    }

    @Override
    public ArrayList getDataArray(Object dataObject) {                
        ArrayList rowData = new ArrayList();
        int typesOff = -1;
        for (int i = 0; i < colDataSources.size(); i++) {
            boolean skipAddData = false;
            String fieldName = (String) colDataSources.get(i);
            String fieldTypeName = (String)(typesOff == -1?colTypes.get(i) :colTypes.get(i + typesOff));
            Object value = null;
            if(fieldName.contains("%")) {
                String [] accessorDef = fieldName.split(":");
                String leftPart = accessorDef[0];
                String rightPart = accessorDef[1];
                
                String [] fieldDefinition = leftPart.split("%");
                String [] parameters = rightPart.split(",");
                fieldName = fieldDefinition[0];
                
                Object [] params = new Object[parameters.length];
                for (int k = 0; k < parameters.length; k++) {
                    Object paramValue = null;
                    if("i".equals(fieldDefinition[k + 1])) {                        
                        paramValue = Integer.parseInt(parameters[k]);
                    }
                    else if("s".equals(fieldDefinition[k + 1])) {
                        paramValue = parameters[k];
                    }
                    params[k] = paramValue;
                }
                value = extractDataThroughObjectAccessor(dataObject, fieldName, fieldTypeName, params);
            } else if(fieldName.contains("(")) {
                int index = fieldName.indexOf("(");
                //String [] accessorDef = fieldName.split("{");
                String leftPart = fieldName.substring(0, index)/*accessorDef[0]*/;
                String rightPart = fieldName.substring(index + 1)/*accessorDef[1]*/;

                fieldName = leftPart.trim();
                String paramKey = rightPart.replace(")", "").trim();
                
                Object param = paramKeyMap.get(paramKey);
                if(param instanceof List) { // Iterate and generate dynamic columns
                    List params = (List) param;
                    for (Object object : params) {
                        value = extractDataThroughObjectAccessor(dataObject, fieldName, fieldTypeName, object);
                        rowData.add(value);
                        typesOff++;
                        skipAddData = true;
                    }
                } else {
                    value = extractDataThroughObjectAccessor(dataObject, fieldName, fieldTypeName, param);
                }
                 
                
            } else {
                value = extractDataThroughObjectAccessor(dataObject, fieldName, fieldTypeName);
            }
            
            /*if (value == null) {
                value = getDefaultForType(fieldTypeName);
            }*/
            
            if(!skipAddData) rowData.add(value);
        }
        return rowData;
    }
    
    private void insertColumn(int pos, String name, String type, Integer width) {
        UtilLists.insert(colNames, pos, name);
        UtilLists.insert(colTypes, pos, type);
        UtilLists.insert(colWidths, pos, width);
    }

    private String obtainGetMethodName(String accessorName, String typeName) {
        String prefix = "";
        if("boolean".equals(typeName.toLowerCase()))
            prefix = "is";
        else if(!"none".equals(typeName.toLowerCase()))
            prefix = "get";
            
        return prefix + accessorName.substring(0, 1).toUpperCase() + accessorName.substring(1, accessorName.length());
    }
    
    private Object extractDataThroughObjectAccessor(Object obj, String accessorName, String accesorReturnType, Object ... params) {
        Method method = null;
        Object value = null;
        Class<?> [] parameterTypes = new Class<?>[params.length];
        
        String methodName = obtainGetMethodName(accessorName, accesorReturnType);
        
        //Users could specify no name for the accessor (in which case they would specify a space character). In such case, we return null.
        if (methodName.equals("get ") || methodName.equals("is ")) {
            return null;
        }
        try {
            if(params.length != 0) {
                for (int i = 0; i < params.length; i++) {
                    parameterTypes[i] = params[i].getClass();
                }
                method = obj.getClass().getMethod(methodName, parameterTypes);
                value = method.invoke(obj, params);
                //return getValueFormatted(value, accesorReturnType);
            }
            else {
                method = obj.getClass().getMethod(methodName);  
                value = method.invoke(obj);
                //return getValueFormatted(value, accesorReturnType);
            }
            
        } catch (NoSuchMethodException ex) {
            try {
                // Try with something else
                for (int i = 0; i < params.length; i++) {
                    parameterTypes[i] = Object.class;
                }
                method = obj.getClass().getMethod(methodName, parameterTypes);
                value = method.invoke(obj, params);
            } catch (NoSuchMethodException ex1) {
                Logger.getLogger(DefaultTableModelDataExtractor.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (SecurityException ex1) {
                Logger.getLogger(DefaultTableModelDataExtractor.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (Exception ex2) {
                throw new RuntimeException(ex2);
            } 
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } 
        
        return getValueFormatted(value, accesorReturnType);
    }
    
    private Object getValueFormatted(Object value, String toType) {
        //I had to use toType variable, because a did a test extracting value's class (value.getClass())
        //and in the case of dates, it returned java.sql.Date instead of java.util.Date. It seems like these 
        //types are homologous, so the jvm gets confused or the type is uncertain (jvm bug???).
        IFormatter formatter = formatters.get(toType/*.toLowerCase()*/);
        if(formatter != null && value != null) value = formatter.format(value);
        return value;
    }

    private Object getDefaultForType(String typeName) {
        try {
            String strNombreMetodo = "getDefault" + typeName.substring(0, 1).toUpperCase() + typeName.substring(1, typeName.length());
            return this.getClass().getDeclaredMethod(strNombreMetodo).invoke(this);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
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
    // </editor-fold>
}
