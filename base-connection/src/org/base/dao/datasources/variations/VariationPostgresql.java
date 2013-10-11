/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

import java.sql.SQLException;
import java.util.Map;
import org.base.dao.DAOPackage;
import org.base.dao.DAOUtils;
import org.base.dao.datasources.connections.AbstractConnectionPool;
import org.base.dao.datasources.connections.ConnectionPoolJDBC;
import org.base.dao.exceptions.ExceptionDBBackupError;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;
import org.base.dao.exceptions.ExceptionDBRestoreError;
import org.base.dao.exceptions.ExceptionDBUnknownError;
import org.base.utils.io.StreamGobbler;

/**
 *
 * @author mproenza
 */
public class VariationPostgresql implements IDataSourceVariation {
    String schema;
    
    public VariationPostgresql(String schema) {
        this.schema = schema;
    }

    @Override
    public String getDBObjectExpression(String objectName) {
        return schema + "." + objectName;
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
        boolean OK = false;
        try {
            String usuario = ((ConnectionPoolJDBC)connPool).getUser();
            String password = ((ConnectionPoolJDBC)connPool).getPassword();
            ProcessBuilder pb;
            pb = new ProcessBuilder("pg_dump", "-h", getIp(connPool), "-F", "t", "-f", path, "-U", usuario, connPool.getDBName());
            pb.redirectErrorStream(true);
            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", password);

            Process p = pb.start();
            
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR", DAOPackage.logger);            
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT", DAOPackage.logger);
            errorGobbler.start();
            outputGobbler.start();
            
            OK = p.waitFor() == 0;
        } catch (Exception ex) {
            DAOPackage.log(ex.getMessage());
            OK = false;
        }
        if(!OK) throw new ExceptionDBBackupError();
    }

    @Override
    public void restore(AbstractConnectionPool connPool, String path) throws ExceptionDBRestoreError {
        boolean OK = false;
        try {
            String usuario = ((ConnectionPoolJDBC)connPool).getUser();
            String password = ((ConnectionPoolJDBC)connPool).getPassword();
            ProcessBuilder pb;
            pb = new ProcessBuilder("pg_restore", "-h", getIp(connPool), "-F", "t", "-U", usuario, "-d", connPool.getDBName(), "-c", path);
            pb.redirectErrorStream(true);
            Map<String, String> env = pb.environment();
            env.put("PGPASSWORD", password);
            
            Process p = pb.start();
            
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR", DAOPackage.logger);            
            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT", DAOPackage.logger);
            errorGobbler.start();
            outputGobbler.start();
            
            OK = p.waitFor() == 0;
        } catch (Exception ex) {
            DAOPackage.log(ex.getMessage());
            OK = false;
        }
        
        if(!OK) throw new ExceptionDBRestoreError();
    }
    
    
    
    // AUX
    private String getIp(AbstractConnectionPool connPool) {
        String db = ((ConnectionPoolJDBC)connPool).getUrl();
        String[] ds_split = db.split("/");
        String ip_port = ds_split[2];
        String[] ip_port_split = ip_port.split(":");
        String ip = ip_port_split[0];
        return ip;
    }

    @Override
    public Object getReplaceValue(Object value) {
        return value;
    }
}
