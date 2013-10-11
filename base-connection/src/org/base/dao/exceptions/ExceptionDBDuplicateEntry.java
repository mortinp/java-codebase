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
public class ExceptionDBDuplicateEntry extends SQLException {

    public ExceptionDBDuplicateEntry() {
        super(MessageFactory.getMessage("msg_duplicate_entry"));
    }
}
