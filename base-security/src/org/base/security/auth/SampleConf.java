/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth;

import java.util.HashMap;
import java.util.Map;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 *
 * @author Luis Valdes Guerrero
 */
public class SampleConf extends Configuration {

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        if(!name.equalsIgnoreCase("Sample")) return null;
        Map<String,String> opt = new HashMap<String,String>();
        opt.put("strategy", "org.base.security.auth.login.strategies.MainStrategy");
        //opt.put("uri","jdbc:postgresql://192.168.12.24:5432/prueba");
        AppConfigurationEntry[] entry = {new AppConfigurationEntry("org.base.security.auth.login.GenericLoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, opt)};
        return entry;
    }
    
}