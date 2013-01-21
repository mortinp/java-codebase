/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.exceptions;

import java.sql.SQLException;

/**
 *
 * @author leo
 */
public class ForeignKeyException extends SQLException {

    public ForeignKeyException() {
        super("La operación no puede realizarse.\n Este objeto está relacionado con otros datos");
    }
}
