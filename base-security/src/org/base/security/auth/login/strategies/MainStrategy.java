/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth.login.strategies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.security.auth.config.AuthEntryPoint;
import org.base.security.config.Values;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class MainStrategy implements StrategyLogin {
    
    DataSourceContext dataSourceContext = AuthEntryPoint.dataSourceContext;
    
    protected String getContextualDBObjectName(String objName) {
        return dataSourceContext.getDBObjectExpression(objName);
    }
    
    protected String getContextualFunctionName(String fnName) {
        return dataSourceContext.getDBObjectExpression(fnName);
    }
    
    protected Connection getContextualConnection() {
        return dataSourceContext.getConnection();
    }

    @Override
    public boolean authenticate(String user, String password, Map opt) throws LoginException{
        Connection conn = null;
        String pass;
        try {
            conn = dataSourceContext.getConnection();
            String stm = "select password  from " + getContextualDBObjectName(Values.TABLE_NAME_USER) + " where login=" + "'" + user + "'";
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery(stm);
            if (!result.next()) {
                throw new LoginException("Usuario o contraseña incorrectos");
            }
            pass = result.getString(1);

            stm = "select login_fallidos, activo  from " + getContextualDBObjectName(Values.TABLE_NAME_USER) + " where login=" + "'" + user + "'";
            select = conn.createStatement();
            result = select.executeQuery(stm);
            result.next();
            int login_fallidos = result.getInt(1);
            boolean activo = result.getBoolean(2);
            if(!activo) throw new LoginException("Usuario bloqueado");
            if (password.equals(pass)) {
                stm = "update " + getContextualDBObjectName(Values.TABLE_NAME_USER) + " set login_fallidos=? where login=" + "'" + user + "'";
                PreparedStatement pstm = conn.prepareStatement(stm);
                pstm.setInt(1, 0);
                pstm.executeUpdate();
                return true;
            } else {
                login_fallidos += 1;
                PreparedStatement pstm = null;
                if (login_fallidos < 3) {
                    stm = "update " + getContextualDBObjectName(Values.TABLE_NAME_USER) + " set login_fallidos=? where login=" + "'" + user + "'";
                    pstm = conn.prepareStatement(stm);
                    pstm.setInt(1, login_fallidos);
                } else {
                    stm = "update " + getContextualDBObjectName(Values.TABLE_NAME_USER) + " set activo=? where login=" + "'" + user + "'";
                    pstm = conn.prepareStatement(stm);
                    pstm.setBoolean(1, false);
                }
                pstm.executeUpdate();
                throw new LoginException("Usuario o contraseña incorrectos");
            }
            /*   } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
            }
            catch (ClassNotFoundException e1) {
            System.out.println(e1.getMessage());
            return false;
             */
        } catch (SQLException e2) {
            System.out.println(e2.getMessage());
            Logger.getLogger("carnico").log(Level.SEVERE, null, e2);
            return false;

        }
    }

    @Override
    public List roles(String nombre, Map opt) {
        String uri = (String) opt.get("uri");
        Connection conn = null;
        List<String> list = new ArrayList<String>();
        try {
            String stm = "select " + Values.TABLE_NAME_ROL + ".nombre from " + getContextualDBObjectName(Values.TABLE_NAME_ROL) + "," + getContextualDBObjectName(Values.TABLE_NAME_USER) + "," + getContextualDBObjectName(Values.TABLE_NAME_USER_ROL) + " where " + Values.TABLE_NAME_ROL + ".nombre = " + Values.TABLE_NAME_USER_ROL + ".id_rol AND " + Values.TABLE_NAME_USER + ".login = " + Values.TABLE_NAME_USER_ROL + ".id_usuario and " + Values.TABLE_NAME_USER + ".login ='" + nombre + "'";
            conn = dataSourceContext.getConnection();
            Statement select = conn.createStatement();
            ResultSet result = select.executeQuery(stm);
            while (result.next()) {
                list.add(result.getString(1));
            }
            /* } catch (IOException e) {
            System.out.println(e.getMessage());
            }
            catch (ClassNotFoundException e1) {
            System.out.println(e1.getMessage());
             */
        } catch (SQLException e2) {
            System.out.println(e2.getMessage());
            Logger.getLogger("carnico").log(Level.SEVERE, null, e2);
        } catch (Exception e3) {
            System.out.println(e3.getMessage());
            Logger.getLogger("carnico").log(Level.SEVERE, null, e3);
        }


        return list;
    }
}
