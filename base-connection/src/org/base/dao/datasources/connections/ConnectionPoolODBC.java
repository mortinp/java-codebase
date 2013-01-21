/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author mproenza
 */
public class ConnectionPoolODBC extends AbstractConnectionPool {
    
    private static String dataSourceName = null;
    private static boolean open = false;
    static final Properties props;
    static {
        props = new Properties();
        props.put ("charSet", "iso-8859-1");
    }

    @Override
    protected void loadFromProperties(Properties properties) {
        try {
            dataSourceName = properties.getProperty("dataSourceName"); 
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            open = true;
        } catch (ClassNotFoundException ex) {
            throw new SystemException(ex);
        }
    }

    @Override
    protected boolean isLoaded() {
        return open;
    }

    @Override
    protected Connection retrieveConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:odbc:" + dataSourceName, props);
    }
    
}
