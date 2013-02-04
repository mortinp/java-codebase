/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author martin
 */
public class ObjectsFactory {
    
    private static Properties propertiesFile = null;
    private static final String DEFAULT_CONFIG_FILE_PATH = "org/base/core/config/objects_handlers.properties";
    
    public static Object getObject(String strNombre) {
        try {
            if (propertiesFile == null) {
                InputStream file = ClassLoader.getSystemResourceAsStream(DEFAULT_CONFIG_FILE_PATH);
                propertiesFile = new Properties();
                propertiesFile.load(file);
            }
            return getInstanceForName(strNombre);
        } catch (IOException ex) {
            throw new SystemException(ex);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }
    
    private static Object getInstanceForName(String modelName) throws Exception {
        String strClassName = propertiesFile.getProperty(modelName + "_model");
        if (strClassName == null || strClassName.length() == 0)
            throw new SystemException(
                    "Incorrect configuration for object '" + modelName + "': property '" + modelName + "_model' not found");
        Class clazz = Class.forName(strClassName);
        if(clazz == null)
            throw new SystemException(
                    "Could not create object for class '" + strClassName + "': class not found");
        Object obj = clazz.newInstance();
        if(obj == null)
            throw new SystemException(
                    "Could not create object for class '" + strClassName + "': empty constructor is unavailable");

        return obj;
    }

    public static void setProperties(Properties properties) {
        propertiesFile = properties;
    }
}
