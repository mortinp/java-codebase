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
public class ExceptionDBEntryNotFound extends SQLException {

    public ExceptionDBEntryNotFound() {
        super("Este objeto no existe en la BD.");        
    }
}
