/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.encryption;

/**
 *
 * @author mproenza
 */
public class SimpleEncryption {
    
    private static String alphabet   = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM*/.,:;'[]{}()!@#$%^&+-=<>?¿@";
    private static String codeNones  = "qwertyuiop0123456789@#~$€%¬&(=-+=<>asdfghjklAPMWYQZXCREJH!ª*/╝[]{}|öÙ▲ý■║◘○♠♣♦☺☻♥♠µ├╣³Ýéóú";
    private static String codePares  = "QWERTYUIOP◘○♦♣♠☺☻♥▼&♂0123456789•§▬.,*/@#$%^<>!~|_[]{}()+-=qwertyuiopasdfghjklzxcvbnmVHNKML";
    
    public static String encrypt(String strCodigo) {

        String strCodificado= "";
        int indice = 0;
        
        for (int i = 0; i< strCodigo.length(); i++){
            indice = alphabet.indexOf(strCodigo.charAt(i));
            
            if (indice > -1){
                if ( (i % 2)== 0) { strCodificado += codePares.charAt(indice); }
                else { strCodificado += codeNones.charAt(indice); } 
            }
            else { strCodificado += strCodigo.charAt(i); }
        }
    
        return strCodificado;
    }
    
    public static String decrypt(String strCodigo){
        
        String strDecodificado= "";
        int indice = 0;
        
        for (int i = 0; i< strCodigo.length(); i++){
            
            if ( (i % 2)== 0) { indice = codePares.indexOf(strCodigo.charAt(i)); }
            else { indice = codeNones.indexOf(strCodigo.charAt(i)); }

            if (indice > -1) { strDecodificado += alphabet.charAt(indice); }
            else { strDecodificado += strCodigo.charAt(i); }
        }
    
        return strDecodificado;
    }
}
