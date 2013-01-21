/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth;

import java.security.Permission;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class AccionPermiso extends Permission {
    
    // <editor-fold defaultstate="collapsed" desc="DECLARACIÓN DE VARIABLES">
    private Set accionSet;
    private String acciones;
    // </editor-fold>
    
    public AccionPermiso(String name, String acciones) {
        super(name);
        accionSet = splitActions(acciones);
        this.acciones = canonizeActions(accionSet);
    }

    @Override
    public boolean implies(Permission permission) {
        if (equals(permission)) {
            return true;
        }
        if (getClass() != permission.getClass()) {
            return false;
        }
        AccionPermiso other = (AccionPermiso) permission;
        if (!getName().equals(other.getName())) {
            return false;
        }
        if (!accionSet.containsAll(other.accionSet)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;


        }
        if (obj.getClass() != AccionPermiso.class) {


            return false;
        }
        AccionPermiso p = (AccionPermiso) obj;
        return p.getName().equals(this.getName()) && p.getActions().equals(this.getActions());
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode() * 19
                + getName().hashCode() * 19 + getActions().hashCode() * 19;

    }

    @Override
    public String getActions() {
        return acciones;
    }

    public boolean hasAction(String action) {
        return accionSet.contains(action);
    }

    private String canonizeActions(Set actions) {
        if (actions == null || actions.isEmpty()) {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for (Iterator itr = actions.iterator(); itr.hasNext();) {
            String action = (String) itr.next();
            buf.append(action);
            if (itr.hasNext()) {
                buf.append(",");
            }
        }
        return buf.toString();
    }

    private Set splitActions(String actions) {
        Set actionSet = Collections.EMPTY_SET;
        if (actions != null && actions.trim().length() > 0) {
            actionSet = new TreeSet();
            String[] split = actions.split(",");
            for (int i = 0; i < split.length; i++) {
                String action = split[i];
                actionSet.add(action.trim());
            }
        }
        return actionSet;
    }
}
