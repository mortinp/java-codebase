/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.connections;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.base.dao.DAOPackage;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;
import org.base.dao.exceptions.ExceptionDBUnknownError;

/**
 *
 * @author mproenza
 */
public abstract class AbstractConnectionPool {
    
    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE CONSTANTES">    
    //llave que todavía es referenciada en otra tabla
    public static final String FOREIGN_KEY_VIOLATION = "23503";
    //llave duplicada
    public static final String UNIQUE_VIOLATION = "23505";
    //no se puede establecer la conexión
    public static final String UNABLE_TO_ESTABLISH_SQLCONNECTION = "08001";
    //cualquier otra excepcion
    public static final String OTHER_EXCEPTION = "00000";
    
    public static final int MAX_NUMBER_OF_CONNECTIONS = 20;
    private static int currentOpenConnCount = 0;
    
    /*static Logger logger = Logger.getLogger("sales_connection"); // TODO: Just testing. FIX
    
    static {
        logger.log(Level.INFO, "Number of opened connections: {0}", currentOpenConnCount);
    }*/
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE VARIABLES">   
    // Pool de conexiones
    private Connection conn;
    
    private enum TRANSACTION_STATE {
        TSTATE_AUTO_COMMIT, TSTATE_TRANSACTION
    };
    private TRANSACTION_STATE transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
    
    private Properties propertiesFile = null;
    public String dataSourceConfigurationFilePath = /*DataSourceTemplateFactory.mainDbContextConfigFilePath;*/"/META-INF/datasource_postgres.properties";
    //public static InputStream objArchivo = null;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ABSTRACT METHODS">   
    protected abstract void loadFromProperties(Properties properties);
    protected abstract boolean isLoaded();
    protected abstract Connection retrieveConnection() throws SQLException;
    public abstract String getDBName();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS">
    protected void init() {
        if(propertiesFile == null) {
            try {
                InputStream objArchivo = AbstractConnectionPool.class.getResourceAsStream(dataSourceConfigurationFilePath);
                propertiesFile = new Properties();
                propertiesFile.load(objArchivo);
            } catch (IOException ex) {
                throw new ExceptionDBProgrammerMistake(ex);
            }
        }
        
        loadFromProperties(propertiesFile);
    }

    public void setDataSourceConfigurationFilePath(String dataSourceConfigurationFilePath) {
        this.dataSourceConfigurationFilePath = dataSourceConfigurationFilePath;
    }
    
    public void setProperties(Properties properties) {
        propertiesFile = properties;
    }

    public void close(Connection conn) {
        if (null != conn) {
            if(transactionState == TRANSACTION_STATE.TSTATE_AUTO_COMMIT) {
                doClose(conn);
            }   
        }
    }
    
    private void doClose(Connection conn) {
        try {
            if(conn.isClosed()) return;
            conn.close();
            currentOpenConnCount--;
            //logger.log(Level.INFO, "Number of opened connections: {0}", currentOpenConnCount);
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    public void close(Statement stm) {
        if (null != stm) {
            try {
                stm.close();
            } catch (SQLException ex) {
                DAOPackage.log(ex);
                throw new ExceptionDBUnknownError(ex);
            }
        }
    }
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS TRANSACCION">
    public Connection getConnection() {
        try {
            if (!isLoaded()) {
                init();
            }
            if(currentOpenConnCount >= MAX_NUMBER_OF_CONNECTIONS)
                throw new ExceptionDBProgrammerMistake("Trying to open more connections than allowed.");
            if(transactionState == TRANSACTION_STATE.TSTATE_AUTO_COMMIT) {
                conn =  retrieveConnection();
                currentOpenConnCount++;
                //logger.log(Level.INFO, "Number of opened connections: {0}", currentOpenConnCount);
            }   
            return conn;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }
    
    public void startTransaction() {
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 
            transactionState = TRANSACTION_STATE.TSTATE_TRANSACTION;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    public void commitTransaction() {
        try {
            conn.commit();
            //conn.close();
            doClose(conn);
            transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }

    public void rollbackTransaction() {
        try {
            conn.rollback();
            //conn.close();
            doClose(conn);
            transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
        } catch (SQLException ex) {
            DAOPackage.log(ex);
            throw new ExceptionDBUnknownError(ex);
        }
    }    
    // </editor-fold>
}
