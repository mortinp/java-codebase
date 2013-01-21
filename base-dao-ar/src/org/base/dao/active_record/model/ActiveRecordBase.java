/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.active_record.model;

import org.base.dao.DAOFactory;
import org.base.dao.IDAO;
import org.base.dao.active_record.exceptions.ActiveRecordException;
import org.base.dao.exceptions.DuplicateEntryException;
import org.base.dao.exceptions.EntryNotFoundException;
import org.base.dao.exceptions.ForeignKeyException;

/**
 *
 * @author Martin
 */
public class ActiveRecordBase implements IActiveRecord {
    private String alias;
    private boolean recorded;
    
    IDAO dao;
    
    public ActiveRecordBase(String alias) {
        this.alias = alias;
        dao = DAOFactory.getDAO(alias);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isRecorded() {
        return recorded;
    }

    public void setRecorded(boolean recorded) {
        this.recorded = recorded;
    }

    @Override
    public void save() throws ActiveRecordException {
        try {
            if(isRecorded()) dao.update(this);
            else {
                dao.insert(this);
                setRecorded(true);
            }
        } catch (EntryNotFoundException ex) {
            throw new ActiveRecordException(ex);
        } catch (ForeignKeyException ex) {
            throw new ActiveRecordException(ex);
        } catch (DuplicateEntryException ex) {
            throw new ActiveRecordException(ex);
        }
    }

    @Override
    public void delete() throws ActiveRecordException {
        try {
            dao.remove(this);
        } catch (EntryNotFoundException ex) {
            throw new ActiveRecordException(ex);
        } catch (ForeignKeyException ex) {
            throw new ActiveRecordException(ex);
        }
    }
    
}
