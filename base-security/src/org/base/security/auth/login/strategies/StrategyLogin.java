/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth.login.strategies;

import java.util.List;
import java.util.Map;
import javax.security.auth.login.LoginException;

/**
 *
 * @author goth
 */
public interface StrategyLogin {
    
    public boolean authenticate(String user, String password, Map opt) throws LoginException;
    public List roles(String nombre, Map opt);
}
