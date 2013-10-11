/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;

/**
 *
 * @author mproenza
 */
public class ConnectionPoolJDBCNamedDriver extends AbstractConnectionPool {    
    private String driverType = "odbc";
    private String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
    private String dataSourceName = null;
    
    private boolean open = false;
    
    static final Properties props;
    static {
        props = new Properties();
        props.put ("charSet", "iso-8859-1");
    }
    
    public ConnectionPoolJDBCNamedDriver() {}
    
    public ConnectionPoolJDBCNamedDriver(String driverType, String driverName) {
        this.driverType = driverType;
        this.driverName = driverName;
    }

    @Override
    protected void loadFromProperties(Properties properties) {
        try {
            dataSourceName = properties.getProperty("dataSourceName"); 
            Class.forName(driverName);
            setProperties(properties);
            open = true;
        } catch (ClassNotFoundException ex) {
            throw new ExceptionDBProgrammerMistake("Could not find class " + driverName + ". Include the driver in your classpath.");
        }
    }

    @Override
    protected boolean isLoaded() {
        return open;
    }

    @Override
    protected Connection retrieveConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:" + driverType + ":" + dataSourceName, props);
    }

    @Override
    public String getDBName() {
        return dataSourceName;
    }
    
}
