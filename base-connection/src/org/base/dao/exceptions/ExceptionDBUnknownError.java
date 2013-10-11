/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.exceptions;

/**
 *
 * @author martin
 */
public class ExceptionDBUnknownError extends RuntimeException {

    public ExceptionDBUnknownError(Throwable cause) {
        super(cause);
    }

    public ExceptionDBUnknownError(String message) {
        super(message);
    }
    
}
