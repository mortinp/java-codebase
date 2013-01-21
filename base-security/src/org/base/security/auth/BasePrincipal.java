/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth;

import java.security.Principal;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public abstract class BasePrincipal implements Principal, Comparable {

    private String name;

    public BasePrincipal(String name) {
        if (name == null) {
            throw new NullPointerException("El nombre no debe ser nulo.");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return getName().hashCode() * 19 + getClass().hashCode() * 19;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BasePrincipal other = (BasePrincipal) obj;
        if (!getName().equals(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        buf.append(getClass().getName());
        buf.append(": name=");
        buf.append(getName());
        buf.append(")");
        return buf.toString();
    }

    @Override
    public int compareTo(Object obj) {
        BasePrincipal other = (BasePrincipal) obj;
        int classComp = getClass().getName().compareTo(
                other.getClass().getName());
        if (classComp == 0) {
            return getName().compareTo(other.getName());
        } else {
            return classComp;
        }
    }
}
