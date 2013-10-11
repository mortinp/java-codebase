/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

import java.sql.SQLException;
import org.base.dao.DAOPackage;
import org.base.dao.DAOUtils;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.exceptions.ExceptionDBBackupError;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;
import org.base.dao.exceptions.ExceptionDBRestoreError;
import org.base.dao.exceptions.ExceptionDBUnknownError;

/**
 *
 * @author mproenza
 */
public class VariationMAccess implements IDataSourceVariation {

    @Override
    public String getDBObjectExpression(String objectName) {
        return "[" + objectName + "]";
    }

    @Override
    public String getBetweenExpression(String fieldName, Object d1, Object d2) {
        String strValorDesde = null;
        String strValorHasta = null;
        String strFiltro = null;

        strValorDesde = DAOUtils.formatValueForQuery(d1);
        strValorHasta = DAOUtils.formatValueForQuery(d2);

        strFiltro = strValorDesde + " AND " + strValorHasta;
        return fieldName + " BETWEEN " + strFiltro;
    }

    @Override
    public void manageException(SQLException ex) throws ExceptionDBDuplicateEntry, ExceptionDBEntryReferencedElsewhere {
        if (ex.getSQLState() == null) {
            ex = (SQLException) ex.getCause();
        }
        if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
            throw new ExceptionDBDuplicateEntry();
        } else if(ex.getSQLState().equals(AbstractConnectionPool.FOREIGN_KEY_VIOLATION)) {
            throw new ExceptionDBEntryReferencedElsewhere();
        } else {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    @Override
    public void backup(AbstractConnectionPool connPool, String path) throws ExceptionDBBackupError {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void restore(AbstractConnectionPool connPool, String path) throws ExceptionDBRestoreError {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getReplaceValue(Object value) {
        return value;
    }
    
}
