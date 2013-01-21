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
import org.base.componentes.modelos.formatting.IFormatter;

/**
 *
 * @author Martin
 */
public class DefaultTableModelDataExtractor implements ITableModelDataExtractor {
    ArrayList colTypes = null;
    ArrayList colDataSources = null;
    
    //I think it's better to have formatters be static, in order to force all formattings in every part of the application
    //be consistent, by configuring formatters only once.
    static Map<String, IFormatter> formatters = new HashMap<String, IFormatter>();

    public DefaultTableModelDataExtractor(ArrayList colTypes, ArrayList colDataSources) {
        this.colTypes = colTypes;
        this.colDataSources = colDataSources;
    }

    public static void setFormatters(Map<String, IFormatter> formatters) {
        DefaultTableModelDataExtractor.formatters = formatters;
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
        for (int j = 0; j < colDataSources.size(); j++) {
            String fieldName = (String) colDataSources.get(j);
            String fieldTypeName = (String) colTypes.get(j);
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
            }
            else {
                value = extractDataThroughObjectAccessor(dataObject, fieldName, fieldTypeName);
            }
            
            if (value == null) {
                value = getDefaultForType(fieldTypeName);
            }
            
            rowData.add(value);
        }
        return rowData;
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
        try {
            String methodName = obtainGetMethodName(accessorName, accesorReturnType);
            
            //Users could specify no name for the accessor (in which case they would specify a space character). In such case, we return null.
            if (methodName.equals("get ") || methodName.equals("is ")) {
                return null;
            }
            if(params.length != 0) {
                Class<?> [] parameterTypes = new Class<?>[params.length];
                for (int i = 0; i < params.length; i++) {
                    parameterTypes[i] = params[i].getClass();
                }
                Method method = obj.getClass().getMethod(methodName, parameterTypes);
                Object value = method.invoke(obj, params);
                return getValueFormatted(value, accesorReturnType);
            }
            else {
                Method method = obj.getClass().getMethod(methodName);  
                Object value = method.invoke(obj);
                return getValueFormatted(value, accesorReturnType);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
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
