/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.exceptions;

/**
 *
 * @author mproenza
 */
public class ExceptionDBNonExistingReference extends RuntimeException {
    public ExceptionDBNonExistingReference() {
        super("This object contains an invalid reference to another object: reference not found.");
    }
}
