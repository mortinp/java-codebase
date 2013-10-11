/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author martin
 */
public class ExceptionWrapAsRuntime extends RuntimeException {
    
    private final String stackTrace;
    private Exception originalException;
    
    public ExceptionWrapAsRuntime(Exception e) {
        super(e.toString());
        originalException = e;
        
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        stackTrace = sw.toString();
    }
    
    @Override
    public void printStackTrace() { 
        printStackTrace(System.err);
    }
    
    @Override
    public void printStackTrace(java.io.PrintStream s) { 
        synchronized(s) {
            s.print(getClass().getName() + ": ");
            s.print(stackTrace);
        }
    }
    
    @Override
    public void printStackTrace(java.io.PrintWriter s) { 
        synchronized(s) {
            s.print(getClass().getName() + ": ");
            s.print(stackTrace);
        }
    }
    
    public void rethrow() throws Exception { throw originalException; }
    
    @Override
    public String getMessage() {
        return originalException.getMessage();
    }
}