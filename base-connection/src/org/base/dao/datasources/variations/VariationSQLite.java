/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.base.dao.DAOPackage;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.exceptions.ExceptionDBBackupError;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;
import org.base.dao.exceptions.ExceptionDBRestoreError;
import org.base.dao.exceptions.ExceptionDBUnknownError;
import org.base.utils.io.FileIO;

/**
 *
 * @author mproenza
 */
public class VariationSQLite implements IDataSourceVariation {

    @Override
    public String getDBObjectExpression(String objectName) {
        return objectName;// Simply return the same object name
    }

    @Override
    public String getBetweenExpression(String fieldName, Object d1, Object d2) {
        String filter = "";
        try {
            Date dd1 = null;
            Date dd2 = null;
            
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            if(d1 instanceof String) dd1 = df.parse((String)d1); else dd1 = (Date)d1;
            if(d2 instanceof String)dd2 = df.parse((String)d2); else dd2 = (Date)d2;
            
            // TODO: Adicionar un dia a la fecha final???
            
            filter = fieldName + " >= " + dd1.getTime() + " AND " + fieldName + " <= " + dd2.getTime();
        } catch (ParseException ex) {
            DAOPackage.log(ex);
            //Logger.getLogger(VariationSQLite.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExceptionDBUnknownError(ex);
        }
        
        return filter;
    }

    @Override
    public void manageException(SQLException ex) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        // TODO: This is a hack (is it???)
        String message = ex.getMessage().toLowerCase();
        if(message.contains("sqlite_constraint")) {
            if(message.contains("is not unique")) throw new ExceptionDBDuplicateEntry();
            else if(message.contains("foreign key constraint failed")) throw new ExceptionDBEntryReferencedElsewhere();
            else {
                DAOPackage.log(ex);
                throw new ExceptionDBUnknownError(ex);
            }
        } else {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    @Override
    public void backup(AbstractConnectionPool connPool, String path) throws ExceptionDBBackupError {
        try {
            FileIO.copyFile(connPool.getDBName(), path);
        } catch (FileNotFoundException ex) {
            throw new ExceptionDBBackupError();
        } catch (IOException ex) {
            throw new ExceptionDBBackupError();
        }
    }

    @Override
    public void restore(AbstractConnectionPool connPool, String path) throws ExceptionDBRestoreError {
        try {
            FileIO.copyFile(path, connPool.getDBName());
        } catch (FileNotFoundException ex) {
            throw new ExceptionDBRestoreError();
        } catch (IOException ex) {
            throw new ExceptionDBRestoreError();
        }
    }

    @Override
    public Object getReplaceValue(Object value) {
        if(value instanceof Boolean) {
            if((Boolean)value == true) return new Integer(1);
            else return new Integer(0);
        }
        
        return value;
    }
    
}
