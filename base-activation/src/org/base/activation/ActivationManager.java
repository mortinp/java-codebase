/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation;

import org.base.activation.exceptions.ActivationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.base.core.exceptions.ExceptionWrapAsRuntime;
import org.base.utils.messages.MessageFactory;
import org.base.utils.types.UtilDates;
import org.base.dao.DAOFactory;
import org.base.dao.IDAO;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.DataSourceTemplateFactory;
import org.base.dao.datasources.context.RegistryDataSourceContext;
import org.base.dao.exceptions.ExceptionDBDuplicateEntry;
import org.base.dao.exceptions.ExceptionDBEntryNotFound;
import org.base.dao.exceptions.ExceptionDBEntryReferencedElsewhere;

/**
 *
 * @author mproenza
 */
public class ActivationManager {
    private static final int DAYS_ALLOWED = 30;
    
    private static final Map<Integer, String> operators;
    
    static {
        operators = new HashMap<Integer, String>();
        operators.put(1, "_plus");
        operators.put(2, "_minus");
        operators.put(3, "_multiply");
        operators.put(4, "_divide");
    }
    
    public boolean isTrialAccount(String userId) {
        UserSettings userSettings = (UserSettings)DAOFactory.getDAO("user_settings").findOne(userId);
        return userSettings.getAccountType().equals("trial");
    }
    
    public void activateWithKey(String userId, String key) throws ActivationException {
        if(key == null || key.isEmpty()) 
            throw new ActivationException(MessageFactory.getMessage("msg_activation_invalid_key", "<empty key>"));
        
        // Find user settings and user's serial key history
        UserSettings userSettings = (UserSettings)DAOFactory.getDAO("user_settings").findOne(userId);
        List<SerialKey> skHistory = userSettings.getSerialKeyHistory();
        
        SerialKey newSK = new SerialKey(-1, key, userId);
        
        // Verify that the new activation key has not been used before
        if(skHistory.contains(newSK)) throw new ActivationException(MessageFactory.getMessage("msg_activation_key_already_used", key));
        
        // Test activation key
        boolean OK = isActivationKeyValid(key);
        if(!OK) throw new ActivationException(MessageFactory.getMessage("msg_activation_invalid_key", key));
        
        else { // Activate and save data
            DataSourceContext dsc = null;
            try {
                dsc = RegistryDataSourceContext.getDataSourceContext(DataSourceTemplateFactory.mainDbContextName);
                dsc.startTransaction();

                IDAO userSettingsDAO = DAOFactory.getDAO("user_settings");
                IDAO skDAO = DAOFactory.getDAO("serial_key");
                
                skDAO.insert(newSK);
                
                userSettings.setCurrentSerialKey(newSK);
                userSettings.setAccountActivationDate(new Date());
                
                userSettingsDAO.update(userSettings);

                dsc.commitTransaction();
            } catch (ExceptionDBDuplicateEntry ex) {
                dsc.rollbackTransaction();
                throw new ExceptionWrapAsRuntime(ex);
            } catch (ExceptionDBEntryNotFound ex) {
                dsc.rollbackTransaction();
                throw new ExceptionWrapAsRuntime(ex);
            } catch (ExceptionDBEntryReferencedElsewhere ex) {
                dsc.rollbackTransaction();
                throw new ExceptionWrapAsRuntime(ex);
            }
        }
    }
    
    public void testKeyExpired(String userId) throws ActivationException {
        UserSettings userSettings = (UserSettings)DAOFactory.getDAO("user_settings").findOne(userId);
        List<SerialKey> skHistory = userSettings.getSerialKeyHistory();
        
        if(skHistory.isEmpty()) throw new ActivationException(MessageFactory.getMessage("msg_activation_application_not_activated_yet"));
        
        if(UtilDates.getDaysDifference(userSettings.getAccountActivationDate(), new Date()) > DAYS_ALLOWED)
            throw new ActivationException(MessageFactory.getMessage("msg_activation_key_expired"));
    }
    
    public boolean isActivationKeyValid(String key) {
        System.out.println("Testing key '" + key + "'");
        
        // Split function definition and result
        String [] parts = key.split("-");
        
        if(parts.length != 2) return false; // Should have 2 parts
        if(parts[0].length()%2 !=0) return false; // Function definition length should be even
        
        String function = parts[0];
        String result = parts[1];
        
        // Force an arbitrarily long function (hopely more difficult to fake)
        if(function.length() < 10) return false;
        
        String r = getFunctionResult(function);
        
        System.out.println("-- Correct key is " + function + "-" + r + " --");
    
        return r.equals(result);
    }
    
    private String getFunctionResult(String function) {
        String result = "";
        
        try {
            int r = 0;
            for (int i = 0; i < function.length(); i+=2) {
                String op = operators.get(Integer.parseInt(function.substring(i, i+1)));
                Integer value = Integer.parseInt(function.substring(i + 1, i + 2));
                
                Class[] types = new Class[] {Integer.class, Integer.class};
                Method m = getClass().getMethod(op, types);
                
                System.out.println("Applying " + op + " now between " + r + " and " + value);
                
                r = Math.abs((Integer)m.invoke(this, r, value));
                
                System.out.println("Result is " + r + " (absolute value)");
                
                result += r;
            }
        
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ActivationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ActivationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ActivationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ActivationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ActivationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public int _plus(Integer a, Integer b) {
        return a + b;
    }
    
    public int _minus(Integer a, Integer b) {
        return a - b;
    }
    
    public int _multiply(Integer a, Integer b) {
        return a * b;
    }
    
    public int _divide(Integer a, Integer b) {
        return a / b;
    }
}
