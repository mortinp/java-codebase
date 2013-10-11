/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.base.core.dao.DAOModel;
import org.base.dao.DAOFactory;
import org.base.dao.IDAO;
import org.base.dao.datasources.context.DataSourceTemplateFactory;
import org.base.dao.filters.FilterSimple;
import org.base.dao.searches.DefaultDataMappingStrategy;

/**
 *
 * @author mproenza
 */
public class UserSettingsDAO extends DAOModel {

    public UserSettingsDAO() {
        super(DataSourceTemplateFactory.mainDbContextName, new DefaultDataMappingStrategy() {
            @Override
            public Object createResultObject(ResultSet rs) throws SQLException {
                UserSettings obj = new UserSettings();
                obj.setUserId(rs.getString("user_id"));
                obj.setUserType(rs.getString("user_type"));
                obj.setAccountType(rs.getString("account_type"));
                obj.setAccountActivationDate(rs.getDate("account_activation_date"));
                
                SerialKey sk = new SerialKey();
                sk.setId(rs.getInt("current_serial_key_id"));
                sk.setKey(rs.getString("key"));
                
                obj.setCurrentSerialKey(sk);
                
                IDAO skDAO = DAOFactory.getDAO("serial_key");
                skDAO.addFilter(new FilterSimple("user_id", rs.getString("user_id")));
                List<SerialKey> skHistory = skDAO.findAll();
                
                obj.setSerialKeyHistory(skHistory);
                
                return obj;
            }
        });
    }

    @Override
    public String getKeyFields() {
        return "user_id";
    }

    @Override
    public String getTableName() {
        return "auth_user_settings";
    }

    @Override
    protected Map<String, Object> getInsertionMap(Object obj) {
        Map<String, Object> mapa = new HashMap<String, Object>();
        UserSettings us = (UserSettings) obj;
        mapa.put("user_id", us.getUserId());
        mapa.put("user_type", us.getUserType());
        mapa.put("account_type", us.getAccountType());
        mapa.put("account_activation_date", us.getAccountActivationDate());
        mapa.put("current_serial_key_id", us.getCurrentSerialKey().getId());
        return mapa;
    }

    @Override
    protected Map<String, Object> getUpdateMap(Object obj) {
        Map<String, Object> mapa = new HashMap<String, Object>();
        UserSettings us = (UserSettings) obj;
        mapa.put("user_type", us.getUserType());
        mapa.put("account_type", us.getAccountType());
        mapa.put("account_activation_date", us.getAccountActivationDate());
        mapa.put("current_serial_key_id", us.getCurrentSerialKey().getId());
        return mapa;
    }

    @Override
    protected String getFindAllStatement() {
        return "SELECT auth_user_settings.*, auth_serial_key.key"
                + " FROM :auth_user_settings"
                + " INNER JOIN :auth_serial_key"
                + " ON auth_user_settings.current_serial_key_id = auth_serial_key.id";
    }    
}
