/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.util.messages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author martin
 */
public class MessageFactory {
    //true = Carga de Propiedades desde archivo
    private static boolean isPropertiesFileLoaded = false;
    //Propiedades
    private static Properties properties = new Properties();

    public static String getMessage(String strNombre, Object ... datos) {
        try {
            if (!isPropertiesFileLoaded) {
                InputStream file = MessageFactory.class.getResourceAsStream("/org/base/core/util/messages/messages_texts.properties");
                properties.load(file);
                isPropertiesFileLoaded = true;
            }
            return getMensajeStr(strNombre, datos);
        } catch (IOException ex) {
            throw new SystemException(ex);
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }
    
    private static String getMensajeStr(String nombreMensaje, Object ... datos)  {
        String strMensaje = properties.getProperty(nombreMensaje, "");
        
        //TODO: validate message and data
        
        //verificar si se le deben adicionar datos al mensaje            
        if (strMensaje != null && strMensaje.contains("&")) { // '&' es el caracter para adicionar datos
            //a la vez, si se le deben adicionar, la cadena de datos no puede ser vacia
            if (datos != null && datos.length != 0) {
                String nuevoMensaje = "";
                StringTokenizer stMsg = new StringTokenizer(strMensaje, "&");
                int i = 0;
                while (stMsg.hasMoreElements()) {
                    nuevoMensaje += stMsg.nextToken();
                    if (i < datos.length) {
                        nuevoMensaje += datos[i];
                        i++;
                    }
                }
                strMensaje = nuevoMensaje;
            } else {
                throw new SystemException("Expecting parameters for message but not found");
            }
        }
        return strMensaje;
    }
}
