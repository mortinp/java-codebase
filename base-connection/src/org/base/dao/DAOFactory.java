/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;
import org.base.dao.exceptions.ExceptionDBUnknownError;

/**
 *
 * @author leo
 */
public abstract class DAOFactory {
    //Interface DAO
    private static IDAO objDAO;
    
    private static Properties propertiesFile = null;
    private static final String DEFAULT_CONFIG_FILE_PATH = "/META-INF/objects_handlers.properties";

    /**
     * Crea y devuelve el DAO.
     *
     * @param strNombre
     * @param propiedades 
     * @return
     */
    public static IDAO getDAO(String alias) {
        try {
            if (propertiesFile == null) {
                InputStream file = DAOFactory.class.getResourceAsStream(DEFAULT_CONFIG_FILE_PATH);
                propertiesFile = new Properties();
                propertiesFile.load(file);
            }
            return getInstanceForName(alias);
        } catch (IOException ex) {
            throw new ExceptionDBProgrammerMistake(ex);
        } catch (Exception ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    private static IDAO getInstanceForName(String alias) throws Exception {
        boolean noCorrectConfigFound = true;
        String strClassName = propertiesFile.getProperty(alias + "_dao");
        String strContextName = propertiesFile.getProperty(alias + "_context");
        if (strClassName != null && !strClassName.isEmpty()) {//try to create custom DAO class
            Class clazz = Class.forName(strClassName);
            if(clazz == null)
                throw new ExceptionDBProgrammerMistake("Could not create DAO for class " + strClassName + ": class does not exist");
            objDAO = (IDAO) clazz.newInstance();
            if(objDAO == null)
                throw new ExceptionDBProgrammerMistake("Could not create DAO for class " + strClassName);
            noCorrectConfigFound = false;
        } else {//try to create DAO from metadata
            String metadataFilePath = propertiesFile.getProperty(alias + "_mapping");
            if (metadataFilePath != null && !metadataFilePath.isEmpty()) {
                objDAO = new DAOSimpleMetadataMapper(metadataFilePath, strContextName);
                noCorrectConfigFound = false;
            }
        }
        
        if(noCorrectConfigFound) throw new ExceptionDBProgrammerMistake("Configuration for DAO ('" + alias + "') was not found ... check configuration file(s)");
        return objDAO;
    }

    public static void setProperties(Properties properties) {
        propertiesFile = properties;
    }
}
