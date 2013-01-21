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
import org.base.exceptions.system.SystemException;

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
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE VARIABLES">   
    // Pool de conexiones
    private Connection conn;
    
    private enum TRANSACTION_STATE {
        TSTATE_AUTO_COMMIT, TSTATE_TRANSACTION
    };
    private TRANSACTION_STATE transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
    
    private Properties propertiesFile = null;
    public String DATASOURCE_CONFIGURATION_FILE_PATH = "/org/base/basedatos/jdbc/config/DataSource.properties";
    //public static InputStream objArchivo = null;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ABSTRACT METHODS">   
    protected abstract void loadFromProperties(Properties properties);
    protected abstract boolean isLoaded();
    protected abstract Connection retrieveConnection() throws SQLException;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS">
    protected void init() {
        if(propertiesFile == null) {
            try {
                InputStream objArchivo = AbstractConnectionPool.class.getResourceAsStream(DATASOURCE_CONFIGURATION_FILE_PATH);
                propertiesFile = new Properties();
                propertiesFile.load(objArchivo);
            } catch (IOException ex) {
                throw new SystemException(ex);
            }
        }
        
        loadFromProperties(propertiesFile);
    }
    
    public void setProperties(Properties properties) {
        propertiesFile = properties;
    }

    public void close(Connection conn) {
        if (null != conn) {
            try {
                if(transactionState == TRANSACTION_STATE.TSTATE_AUTO_COMMIT) {
                    conn.close();
                    currentOpenConnCount--;
                }   
            } catch (SQLException ex) {
                throw new SystemException(ex);
            }
        }
    }

    public void close(Statement stm) {
        if (null != stm) {
            try {
                stm.close();
            } catch (SQLException ex) {
                throw new SystemException(ex);
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
                throw new SystemException("Trying to open more connections than allowed.");
            if(transactionState == TRANSACTION_STATE.TSTATE_AUTO_COMMIT) {
                conn =  retrieveConnection();
                currentOpenConnCount++;
            }   
            return conn;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }
    
    public void startTransaction() {
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 
            transactionState = TRANSACTION_STATE.TSTATE_TRANSACTION;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }

    public void commitTransaction() {
        try {
            conn.commit();
            conn.close();
            transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }

    public void rollbackTransaction() {
        try {
            conn.rollback();
            conn.close();
            transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }    
    // </editor-fold>
}
