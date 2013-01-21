/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.active_record.exceptions;

/**
 *
 * @author Martin
 */
public class ActiveRecordException extends Exception {
    public ActiveRecordException(Exception ex) {
        super(ex);
    }
}
