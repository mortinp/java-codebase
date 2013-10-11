/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.exceptions;

import java.sql.SQLException;
import org.base.utils.messages.MessageFactory;

/**
 *
 * @author leo
 */
public class ExceptionDBNoAccess extends SQLException {

    public ExceptionDBNoAccess() {
        super(MessageFactory.getMessage("msg_no_db_access"));
    }
}
