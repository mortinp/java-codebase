/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.active_record.model;

import org.base.dao.active_record.exceptions.ActiveRecordException;

/**
 *
 * @author Martin
 */
public interface IActiveRecord {
    public void save() throws ActiveRecordException;
    public void delete() throws ActiveRecordException;
}
