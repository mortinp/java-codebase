/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation.exceptions;

/**
 *
 * @author mproenza
 */
public class KeyExpiredException extends Exception {

    public KeyExpiredException() {
        super("Your trial period has expired.");
    }
}
