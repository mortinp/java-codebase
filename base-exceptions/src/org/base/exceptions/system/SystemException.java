/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.exceptions.system;

/**
 *
 * @author Administrador
 */
public class SystemException extends RuntimeException {

    public SystemException(String msg) {
        super(msg);
    }
    
    public SystemException(Exception e) {
        super(e);

    }
}
