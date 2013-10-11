/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.exceptions;

/**
 *
 * @author martin
 */
public class ExceptionUnknownError extends RuntimeException {

    public ExceptionUnknownError(Throwable cause) {
        super(cause);
    }

    public ExceptionUnknownError(String message) {
        super(message);
    }
    
}

