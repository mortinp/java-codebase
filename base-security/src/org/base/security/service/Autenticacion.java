package org.base.security.service;

import org.base.security.auth.Rol;
import org.base.security.auth.SampleConf;
import org.base.security.auth.Usuario;
import org.base.security.auth.exception.DSecurityException;
import org.base.security.auth.handlers.AbstractAuthDialog;
import org.base.security.auth.handlers.UICallbackHandler;
import org.base.security.gui.frmCambiarPassword;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.logging.Level;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.base.core.presentation.screens.ScreenBase;
import org.base.utils.messages.MessageFactory;
import org.base.security.auth.config.AuthEntryPoint;

public class Autenticacion {

    static private Subject subj;
    static private String usuario;
    static private LoginContext ctx;

    static public boolean login(AbstractAuthDialog authDialog) {
        final SampleConf conf = new SampleConf();
        try {
            ctx = new LoginContext("Sample", new Subject(), new UICallbackHandler(authDialog), conf);
            ctx.login();
        }
        catch (LoginException e) {
            throw new DSecurityException((e.getMessage()));
        }
        subj = ctx.getSubject();
        Set usuarioset = subj.getPrincipals(Usuario.class);
        usuario = ((Usuario)usuarioset.toArray()[0]).getName();
        return true;
    }
    
    public static boolean  passwordCaducada()
    {
        return new DbServise().passwordCaducada(Autenticacion.getUsuario());
    }
    
    static public String getNombreUsuario()
    {
        return new DbServise().nombreUsuario(Autenticacion.getUsuario());
    }
    
    static public String getUsuario()
    {
        return usuario;
    }
    
    static public String getRoles()
    {
        String roles = "";
        for(Rol rol: subj.getPrincipals(Rol.class))
        {
            roles += rol.getName()+",";
        }
        if(roles.length() > 0)
        roles = roles.substring(0, roles.length()-1);
        return roles;
    }

    static public boolean pruebaPermiso(final Permission perm) {


        return (Boolean) Subject.doAsPrivileged(subj,new PrivilegedAction() {

            @Override
            public Object run() {
                try {
                    AccessController.checkPermission(perm);
                } catch (SecurityException e) {
                    return false;
                }
                return true;
            }
        },null);




    }
    
    static public boolean pruebaRol(String rol)
    {
        Set roles = subj.getPrincipals(Rol.class);
        for(Object principal:roles)
        {
            if(((Rol)principal).getName().equals(rol))
            {
                return true;
            }
        }
        return false;
    }

    static public Subject getSubject() {
        return subj;
    }
    
    static public void logout()
    {
        try{
        ctx.logout();
        }
        catch(LoginException e)
        {
        }
    }
    
    static public void start(AbstractAuthDialog authDialog) {
        while (true) {
            try {
                if (Autenticacion.login(authDialog)) {
                    break;
                }
            } catch (DSecurityException e) {
                AuthEntryPoint.logger.log(Level.SEVERE, e.getMessage());
                ScreenBase.showWarningMessage(MessageFactory.getMessage("msg_connection_not_established"));
            }
        }
        while (Autenticacion.passwordCaducada()) {
            JOptionPane.showMessageDialog(null, "Contraseña caducada, por favor cámbiela para poder ingresar al sistema", "Aviso", JOptionPane.WARNING_MESSAGE);
            JDialog d = new JDialog();
            d.setContentPane(new frmCambiarPassword(d, Autenticacion.getUsuario()));
            d.pack();
            d.setModal(true);
            d.setVisible(true);
        }
    }
}