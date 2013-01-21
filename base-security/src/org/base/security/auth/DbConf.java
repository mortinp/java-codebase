/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth;

import java.util.Locale;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;
import javax.security.auth.login.Configuration;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class DbConf extends Configuration {

    static private DbConf conf;

    static public void init() {
        conf = new DbConf();
        Configuration.setConfiguration(conf);
    }

    static public DbConf getDbConf() {
        return conf;
    }

    //@todo adcionar funcionalidad
    public void addAppConfigurationEntry(String appName, AppConfigurationEntry entry) {
    }

    //@todo adcionar funcionalidad
    public boolean deleteAllAppEntries(String appName) {
        return true;
    }

    //@todo adcionar funcionalidad
    public boolean deleteAppConfigurationEntry(String appName, String loginModuleName) {
        return true;
    }

    @Override
    public void refresh() {
    }

    static String controlFlagString(LoginModuleControlFlag flag) {
        if (LoginModuleControlFlag.REQUIRED.equals(flag)) {
            return "REQUIRED";
        } else if (LoginModuleControlFlag.REQUISITE.equals(flag)) {
            return "REQUISITE";
        } else if (LoginModuleControlFlag.SUFFICIENT.equals(flag)) {
            return "SUFFICIENT";
        } else if (LoginModuleControlFlag.OPTIONAL.equals(flag)) {
            return "OPTIONAL";
        } else {
            // default if unknown
            return "OPTIONAL";
        }
    }

    static LoginModuleControlFlag resolveControlFlag(String name) {
        if (name == null) {
            throw new NullPointerException(
                    "control flag name passed in is null.");
        }
        String uppedName = name.toUpperCase(Locale.US);
        if ("REQUIRED".equals(uppedName)) {
            return LoginModuleControlFlag.REQUIRED;
        } else if ("REQUISITE".equals(uppedName)) {
            return LoginModuleControlFlag.REQUISITE;
        } else if ("SUFFICIENT".equals(uppedName)) {
            return LoginModuleControlFlag.SUFFICIENT;
        } else if ("OPTIONAL".equals(uppedName)) {
            return LoginModuleControlFlag.OPTIONAL;
        } else {
            // default if unknown
            return AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL;
        }
    }

    //@todo adcionar funcionalidad
    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
