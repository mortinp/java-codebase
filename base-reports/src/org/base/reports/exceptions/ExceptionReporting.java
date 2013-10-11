/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.exceptions;

/**
 *
 * @author martin
 */
public class ExceptionReporting extends RuntimeException {

    public ExceptionReporting(Throwable cause) {
        super(cause);
    }

    public ExceptionReporting(String message) {
        super(message);
    }
    
}
