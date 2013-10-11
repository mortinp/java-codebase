/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.connections;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;

/**
 *
 * @author mproenza
 */
public class ConnectionPoolJDBC extends AbstractConnectionPool {
    
    private DataSource dataSource = null;
    private String schema = null;
    private String url = null;
    private String user = null;
    private String password = null;
    private String driver = null;

    @Override
    protected void loadFromProperties(Properties properties) {
        schema = properties.getProperty("schema");
        url = properties.getProperty("url");
        user = properties.getProperty("user");
        password = properties.getProperty("pass");
        driver = properties.getProperty("driver");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setDriverClassName(driver);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaxActive(MAX_NUMBER_OF_CONNECTIONS);
        ds.setMaxWait(10);
        ds.setValidationQueryTimeout(10);
        //ds.setValidationQuery(properties.getProperty("validationQuery"));                
        dataSource = ds;
    }

    @Override
    protected boolean isLoaded() {
        return dataSource != null;
    }

    @Override
    protected Connection retrieveConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public String getSchema() {
        if (schema == null) {
            init();
        }
        return schema;
    }
    
    public String getUser() {
        if (user == null) {
            init();
        }
        return user;
    }
    
    public String getPassword() {
        if (password == null) {
            init();
        }
        return password;
    }
    
    public String getUrl() {
        if (url == null) {
            init();
        }
        return url;
    }
    
    @Override
    public String getDBName() {
        String db = getUrl();
        String[] ds_split = db.split("/");
        String dbname = ds_split[3];
        return dbname;
    }
}
