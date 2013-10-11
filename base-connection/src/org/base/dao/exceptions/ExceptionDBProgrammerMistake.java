/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.exceptions;

/**
 *
 * @author martin
 */
public class ExceptionDBProgrammerMistake extends RuntimeException {

    public ExceptionDBProgrammerMistake(Throwable cause) {
        super(cause);
    }

    public ExceptionDBProgrammerMistake(String message) {
        super(message);
    }
    
}

