/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth.login.strategies;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class SampleStrategy implements StrategyLogin{

    @Override
    public boolean authenticate(String user, String password, Map opt) {
        return true;
    }

    @Override
    public List roles(String nombre, Map opt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
