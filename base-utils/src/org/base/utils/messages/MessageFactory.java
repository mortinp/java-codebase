/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.messages;

import org.base.utils.exceptions.ExceptionUnknownError;
import org.base.utils.exceptions.ExceptionProgrammerMistake;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 *
 * @author martin
 */
public class MessageFactory {
    //true = Carga de Propiedades desde archivo
    private static boolean isPropertiesFileLoaded = false;
    //Propiedades
    private static Properties properties = new Properties();
    private static final String DEFAULT_CONFIG_FILE_PATH = "/META-INF/messages.properties";

    public static String getMessage(String strNombre, Object ... params) {
        try {
            if (!isPropertiesFileLoaded) {
                InputStream file = MessageFactory.class.getResourceAsStream(DEFAULT_CONFIG_FILE_PATH);
                properties.load(file);
                isPropertiesFileLoaded = true;
            }
            return doGetMessage(strNombre, params);
        } catch (IOException ex) {
            throw new ExceptionUnknownError(ex);
        } catch (Exception ex) {
            throw new ExceptionUnknownError(ex);
        }
    }
    
    private static String doGetMessage(String nombreMensaje, Object ... params)  {
        String strMensaje = properties.getProperty(nombreMensaje, "");
        
        //Validate message and data
        if(strMensaje == null) 
            throw new ExceptionProgrammerMistake("Configuration not found for message '" + nombreMensaje + "'");
        
        int index = 0;
        int neededParamsCount = 0;
        while(strMensaje.indexOf("&", index) != -1) {
            neededParamsCount ++;
            index = strMensaje.indexOf("&", index) + 1;
        }
        if((neededParamsCount > 0 && params == null) || neededParamsCount < params.length)
            throw new ExceptionProgrammerMistake("Expecting " + neededParamsCount + " parameters for message but found " + params.length);
        
        // Substitute params if needed 
        if(neededParamsCount > 0) {
            String nuevoMensaje = "";
            StringTokenizer stMsg = new StringTokenizer(strMensaje, "&");
            int i = 0;
            while (stMsg.hasMoreElements()) {
                nuevoMensaje += stMsg.nextToken();
                if (i < params.length) {
                    nuevoMensaje += params[i];
                    i++;
                }
            }
            strMensaje = nuevoMensaje;
        }
        /*if (strMensaje != null && strMensaje.contains("&")) { // '&' es el caracter para adicionar datos
            //a la vez, si se le deben adicionar, la cadena de datos no puede ser vacia
            if (params != null && params.length != 0) {
                String nuevoMensaje = "";
                StringTokenizer stMsg = new StringTokenizer(strMensaje, "&");
                int i = 0;
                while (stMsg.hasMoreElements()) {
                    nuevoMensaje += stMsg.nextToken();
                    if (i < params.length) {
                        nuevoMensaje += params[i];
                        i++;
                    }
                }
                strMensaje = nuevoMensaje;
            } else {
                throw new ExceptionProgrammerMistake("Expecting parameters for message but not found.");
            }
        }*/
        return strMensaje;
    }
}
