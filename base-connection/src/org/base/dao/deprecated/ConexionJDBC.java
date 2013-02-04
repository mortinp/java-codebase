/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.deprecated;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author martin
 */
@Deprecated
public abstract class ConexionJDBC {

    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE CONSTANTES">    
    //llave que todavía es referenciada en otra tabla
    public static final String FOREIGN_KEY_VIOLATION = "23503";
    //llave duplicada
    public static final String UNIQUE_VIOLATION = "23505";
    //no se puede establecer la conexión
    public static final String UNABLE_TO_ESTABLISH_SQLCONNECTION = "08001";
    //cualquier otra excepcion
    public static final String OTHER_EXCEPTION = "00000";
    
    private static final int MAX_NUMBER_OF_CONNECTIONS = 20;
    private static int currentOpenConnCount = 0;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="DECLARACION DE VARIABLES">   
    // Pool de conexiones
    private static DataSource dataSource = null;
    private static Connection conn;
    
    private static String schema = null;
    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static String driver = null;
    
    private enum TRANSACTION_STATE {
        TSTATE_AUTO_COMMIT, TSTATE_TRANSACTION
    };
    private static TRANSACTION_STATE transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
    
    private static Properties propertiesFile = null;
    public static String DATASOURCE_CONFIGURATION_FILE_PATH = "/org/base/basedatos/jdbc/config/DataSource.properties";
    //public static InputStream objArchivo = null;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    public ConexionJDBC() {
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS">
    /**
     * Inicializa los recursos comunes para todos los daos.
     *
     * @param properties
     */
    private static void init() {
        if(propertiesFile == null) {
            try {
                InputStream objArchivo = ConexionJDBC.class.getResourceAsStream(DATASOURCE_CONFIGURATION_FILE_PATH);
                propertiesFile = new Properties();
                propertiesFile.load(objArchivo);
            } catch (IOException ex) {
                throw new SystemException(ex);
            }
        }

        schema = propertiesFile.getProperty("schema");
        url = propertiesFile.getProperty("url");
        user = propertiesFile.getProperty("user");
        password = propertiesFile.getProperty("pass");
        driver = propertiesFile.getProperty("driver");

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setDriverClassName(driver);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaxActive(MAX_NUMBER_OF_CONNECTIONS);
        ds.setMaxWait(10);
        ds.setValidationQueryTimeout(10);
        //ds.setValidationQuery(properties.getProperty("validationQuery"));                
        ConexionJDBC.dataSource = ds;
    }  

    public static String getSchema() {
        if (schema == null) {
            init();
        }
        return schema;
    }
    
    public static String getUser() {
        if (user == null) {
            init();
        }
        return user;
    }
    
    public static String getPassword() {
        if (password == null) {
            init();
        }
        return password;
    }
    
    public static String getUrl() {
        if (url == null) {
            init();
        }
        return url;
    }

    public static void close(Connection conn) {
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

    protected static void close(Statement stm) throws SQLException {
        if (null != stm) {
            stm.close();
        }
    }

    protected static void close(ResultSet rs) throws SQLException {
        if (null != rs) {
            rs.close();
        }
    }

    protected void close(Connection conn, Statement stm, ResultSet rs) throws SQLException {
        close(rs);
        close(stm);
        close(conn);
    }

    /*protected static List execute(Connection conn, String sql) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        RowSetDynaClass rsdc = null;

        pstm = conn.prepareStatement(sql);
        rs = pstm.executeQuery();
        rsdc = new RowSetDynaClass(rs);

        return rsdc.getRows();
    }*/
    
    protected static ResultSet getResultSet(Connection conn, String sql) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;        

        pstm = conn.prepareStatement(sql);
        rs = pstm.executeQuery();

        return rs;
    }

    public static void setProperties(Properties properties) {
        propertiesFile = properties;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="METODOS TRANSACCION">
    public static Connection getConnection() {
        try {
            if (dataSource == null) {
                init();
            }
            if(currentOpenConnCount >= MAX_NUMBER_OF_CONNECTIONS)
                throw new SystemException("Trying to open more connections than allowed.");
            if(transactionState == TRANSACTION_STATE.TSTATE_AUTO_COMMIT) {
                conn =  dataSource.getConnection();
                currentOpenConnCount++;
            }   
            return conn;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }
    
    public static void startTransaction(/*Connection conn*/) {
        try {
            conn = getConnection();
            conn.setAutoCommit(false); 
            transactionState = TRANSACTION_STATE.TSTATE_TRANSACTION;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }

    public static void commitTransaction(/*Connection conn*/) {
        try {
            conn.commit();
            conn.close();
            transactionState = TRANSACTION_STATE.TSTATE_AUTO_COMMIT;
        } catch (SQLException ex) {
            throw new SystemException(ex);
        }
    }

    public static void rollbackTransaction(/*Connection conn*/) {
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
