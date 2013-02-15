/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import org.base.dao.searches.IDataMappingStrategy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author martin
 */
public abstract class DAOAutonumeric extends DAOBase {
    
    protected abstract String getAutonumericFieldName();
    
    public DAOAutonumeric(String dataSourceContextName, IDataMappingStrategy toObjectMapper) {
        super(dataSourceContextName, toObjectMapper);
    }
    
    @Override
    public String getKeyFields() {
        return getAutonumericFieldName();
    }
    
    public int insertarObtenerId(Object objModelo) throws ExceptionDBDuplicateEntry {
        return insertarObtenerId(objModelo, getContextualConnection());            
    }
    
    public int insertarObtenerId(Object objModelo, Connection conn) throws ExceptionDBDuplicateEntry {
        int intId = 0;
        PreparedStatement pstm = null;

        try {
            StatementData stmtData = createInsertionData(getInsertionMap(objModelo));
            pstm = prepareStatement(tryFixQuery(stmtData.getQuery()), stmtData.getParams(), conn);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                intId = Integer.parseInt(rs.getString(getAutonumericFieldName()));
            }
            return intId;
        } catch (SQLException ex) {
            if (ex.getSQLState() == null) {
                ex = (SQLException) ex.getCause();
            }
            if (ex.getSQLState().equals(AbstractConnectionPool.UNIQUE_VIOLATION)) {
                throw new ExceptionDBDuplicateEntry();
            } else {
                throw new SystemException(ex);
            }
        } catch (Exception unknown) {
            throw new SystemException(unknown);
        } finally {
            //try {
                closeContextualStatement(pstm);
                closeContextualConnection(conn);
            //} catch (SQLException ex) {
                //throw new SistemaExcepcion(ex);
            //}
        }
    }
    
    private String tryFixQuery(String sql) {
        if(!sql.toLowerCase().contains("returning")) {
            return sql + " RETURNING " + getAutonumericFieldName();
        }
        return sql;
    }
}
