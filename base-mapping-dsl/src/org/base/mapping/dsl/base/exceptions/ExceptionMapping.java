/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.mapping.dsl.base.exceptions;

/**
 *
 * @author martin
 */
public class ExceptionMapping extends RuntimeException {

    public ExceptionMapping(Throwable cause) {
        super(cause);
    }

    public ExceptionMapping(String message) {
        super(message);
    }
    
}
