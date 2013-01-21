/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.exceptions;

/**
 *
 * @author Maylen
 */
public class DomainException extends Exception {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(Exception ex) {
        super(ex);
    }
}
