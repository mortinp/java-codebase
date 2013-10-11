/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

import java.sql.SQLException;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.exceptions.ExceptionDBBackupError;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;
import org.base.dao.exceptions.ExceptionDBRestoreError;

/**
 *
 * @author mproenza
 */
public interface IDataSourceVariation {
    public String getDBObjectExpression(String objectName);
    
    public String getBetweenExpression(String fieldName, Object o1, Object o2);
    public Object getReplaceValue(Object value);
    
    public void manageException(SQLException ex)throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere ;
    
    public void backup(AbstractConnectionPool connPool, String path) throws ExceptionDBBackupError;
    public void restore(AbstractConnectionPool connPool, String path) throws ExceptionDBRestoreError;
}

