/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation.exceptions;

/**
 *
 * @author mproenza
 */
public class ApplicationNotActivatedYet extends Exception {

    public ApplicationNotActivatedYet() {
        super("The application has not been activated yet.");
    }
    
}
