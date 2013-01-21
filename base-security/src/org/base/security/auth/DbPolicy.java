/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth;

import org.base.security.service.DbServise;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class DbPolicy extends Policy {

    @Override
    public PermissionCollection getPermissions(CodeSource codesource) {
// others may add to this, so use heterogeneous Permissions
        Permissions perms = new Permissions();
        perms.add(new AllPermission());
        return perms;
    }

    @Override
    public PermissionCollection getPermissions(final ProtectionDomain domain) {
        final Permissions permissions = new Permissions();
        final Set<String> rolPrincipal = new HashSet();
        final Set<String> usuarioPrincipal = new HashSet();
        Principal[] principals = domain.getPrincipals();
        for (Principal p : principals) {
            if (p instanceof Rol) {
                rolPrincipal.add(p.getName());
            }
            if (p instanceof Usuario) {
                usuarioPrincipal.add(p.getName());
            }
        }
        if (!usuarioPrincipal.isEmpty() || !rolPrincipal.isEmpty()) {
            final DbServise dbservise = new DbServise();
            for (final String usuario : usuarioPrincipal) {
                try {
                    List<Permission> list = (List<Permission>) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                        public Object run() {
                            return dbservise.permisosUsuario((String) usuario);
                        }
                    });
                    for (Permission p : list) {
                        permissions.add(p);
                    }
                } catch (PrivilegedActionException e) {
                }
            }
            for (final String rol : rolPrincipal) {
                try {
                    List<Permission> list = (List<Permission>) AccessController.doPrivileged(new PrivilegedExceptionAction() {

                        public Object run() {
                            return dbservise.permisosRol((String) rol);
                        }
                    });
                    for (Permission p : list) {
                        permissions.add(p);
                    }
                } catch (PrivilegedActionException e) {
                }
            }
            permissions.add(new AccionPermiso("ah", "ah"));
        } else if (domain.getCodeSource() != null) {
            PermissionCollection codeSrcPerms = getPermissions(domain.getCodeSource());
            for (Enumeration en = codeSrcPerms.elements(); en.hasMoreElements();) {
                Permission p = (Permission) en.nextElement();
                permissions.add(p);
            }

        }
        return permissions;
    }

    @Override
    public boolean implies(final ProtectionDomain domain,
            final Permission permission) {
        PermissionCollection perms = getPermissions(domain);
        boolean implies = perms.implies(permission);
        return implies;
    }

    @Override
    public void refresh() {
        // does nothing for DB.
    }
}
