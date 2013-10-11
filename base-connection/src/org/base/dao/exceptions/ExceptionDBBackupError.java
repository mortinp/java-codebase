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
public class ExceptionDBBackupError extends SQLException {

    public ExceptionDBBackupError() {
        super(MessageFactory.getMessage("msg_backup_error"));
    }
}
