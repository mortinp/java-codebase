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
public class ExceptionDBEntryNotFound extends SQLException {

    public ExceptionDBEntryNotFound() {
        super(MessageFactory.getMessage("msg_entry_not_found"));        
    }
}
