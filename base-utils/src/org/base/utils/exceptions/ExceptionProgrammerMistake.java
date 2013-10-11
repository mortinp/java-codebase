/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.exceptions;

/**
 *
 * @author martin
 */
public class ExceptionProgrammerMistake extends RuntimeException {

    public ExceptionProgrammerMistake(Throwable cause) {
        super(cause);
    }

    public ExceptionProgrammerMistake(String message) {
        super(message);
    }
    
}

