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
import org.sqlite.SQLiteConfig;

/**
 *
 * @author mproenza
 */
public class ConnectionPoolSqliteJDBC extends AbstractConnectionPool {    
    private String driverName = "org.sqlite.JDBC";
    private String dataSourceName = null;
    
    private boolean open = false;
    
    static SQLiteConfig config = new SQLiteConfig();  
        
    static {
        config = new SQLiteConfig();
        config.enforceForeignKeys(true);
    }
    
    public ConnectionPoolSqliteJDBC() {}
    
    public ConnectionPoolSqliteJDBC(String driverName) {
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
        return DriverManager.getConnection("jdbc:sqlite:" + dataSourceName, config.toProperties());
    }

    @Override
    public String getDBName() {
        return dataSourceName;
    }
    
}
