/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.exceptions;

/**
 *
 * @author mproenza
 */
public class NonExistingReferenceException extends RuntimeException {
    public NonExistingReferenceException() {
        super("This object contains an invalid reference to another object: reference not found.");
    }
}
