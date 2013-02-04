/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth.login;

import org.base.security.auth.Rol;
import org.base.security.auth.Usuario;
import org.base.security.auth.login.strategies.StrategyLogin;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 *
 * @author Luis Valdes Guerrero
 */
public class GenericLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private String name;
    private String password;
    private String strategyLogin;
    private boolean authenticated;
    private Set principalsAdded;
    private Map opt;
    private boolean autenticado;
    private StrategyLogin sl;
    static private final Class[] ZERO_ARGS = {};

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.strategyLogin = options.get("strategy").toString();
        this.opt = options;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCB = new NameCallback("Username");
        PasswordCallback passwordCB = new PasswordCallback("Password",
                false);
        Callback[] callbacks = new Callback[]{nameCB, passwordCB};
        try {
            callbackHandler.handle(callbacks);
        } catch (IOException e) {
            throw new LoginException("Error de entrada");
        } catch (UnsupportedCallbackException e) {
            String className = e.getCallback().getClass().getName();
            throw new LoginException(className + " no esta soportada");
        }
        name = nameCB.getName();
        password = String.valueOf(passwordCB.getPassword());
        Class strategy;
        try {
            strategy = Class.forName(strategyLogin);
        } catch (ClassNotFoundException e) {
            throw new LoginException("No existe la clase " + strategyLogin);
        }
        try {
            try {
                sl = (StrategyLogin)strategy.getConstructor(ZERO_ARGS).newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(GenericLoginModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GenericLoginModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(GenericLoginModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(GenericLoginModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(GenericLoginModule.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(GenericLoginModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        autenticado = sl.authenticate(name, password, opt);     
        return autenticado;
        
       
        
    }

    //@todo implementar
    @Override
    public boolean commit() throws LoginException {
        if(!autenticado) return false;
        this.subject.getPrincipals().add(new Usuario(name));
        for(Object rol: sl.roles(name, opt))
        {
            String nombre = (String)rol;
            this.subject.getPrincipals().add(new Rol(nombre));
        }
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        cleanup();
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        cleanup();
        return true;
    }

    //clean
    private void cleanup() {
        name = null;
        password = null;
    }
}
