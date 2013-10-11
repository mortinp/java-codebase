/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.datasources.connections.ConnectionPoolJDBC;
import org.base.dao.datasources.connections.ConnectionPoolSqliteJDBC;
import org.base.dao.datasources.variations.VariationPostgresql;
import org.base.dao.datasources.variations.VariationSQLite;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;

/**
 *
 * @author mproenza
 */
public class DataSourceTemplateFactory {
    
    public static String mainDbContextName = "sqlite";
    public static String mainDbContextConfigFilePath = "/META-INF/sqlite.properties";
    
    static {
        try {
            InputStream file = DataSourceTemplateFactory.class.getResourceAsStream("/META-INF/datasource_context.properties");
            Properties propertiesFile = new Properties();
            propertiesFile.load(file);
            
            mainDbContextName = propertiesFile.getProperty("ds.context.name");
            mainDbContextConfigFilePath = propertiesFile.getProperty("ds.context.config.file.path");
        } catch (IOException ex) {
            throw new ExceptionDBProgrammerMistake(ex);
        } catch (Exception ex) {
            throw new ExceptionDBProgrammerMistake(ex);
        }
    }
    
    public static DataSourceContext buildMainDataSourceContext(String ... params) {
        DataSourceContext dsContext = null;
        if(mainDbContextName.equals("sqlite")) {
            AbstractConnectionPool connPool = new ConnectionPoolSqliteJDBC();
                    /*new ConnectionPoolJDBCNamedDriver(mainDbContextName, "org.sqlite.JDBC");*/
            connPool.setDataSourceConfigurationFilePath(mainDbContextConfigFilePath);

            dsContext = new DataSourceContext(connPool, new VariationSQLite());
        } else if(mainDbContextName.equals(DataSourceTemplateFactory.mainDbContextName)) {
            AbstractConnectionPool connPool = new ConnectionPoolJDBC();
            connPool.setDataSourceConfigurationFilePath(mainDbContextConfigFilePath);
            dsContext = new DataSourceContext(connPool, new VariationPostgresql(params[0]));
        }
        
        RegistryDataSourceContext.registerDataSourceContext(mainDbContextName, dsContext);
        
        return dsContext;
    }
    
}
