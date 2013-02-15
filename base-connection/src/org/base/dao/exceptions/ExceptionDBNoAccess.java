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
public class ExceptionDBNoAccess extends SQLException {

    public ExceptionDBNoAccess() {
        super("No se pudo establecer la conexión con la base de datos.\nVerifique que el servicio se está ejecutando e inténtelo de nuevo.");
    }
}
