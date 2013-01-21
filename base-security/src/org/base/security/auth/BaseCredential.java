/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth;

/**
 *
 * @author Luis Valdes Guerrero <lvaldes@grm.desoft.cu>
 */
public class BaseCredential implements Comparable {

    private String value;

    public BaseCredential(String value) {
        if (value == null) {
            throw new NullPointerException("El valor no debe ser nulo.");
        }
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(Object obj) {
        BaseCredential other = (BaseCredential) obj;
        int classComp = getClass().getName().compareTo(
                other.getClass().getName());
        if (classComp == 0) {
            return getValue().compareTo(other.getValue());
        } else {
            return classComp;
        }

    }

    @Override
    public int hashCode() {
        return value.hashCode() * 29 + getClass().hashCode() * 29;
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
        if (!getValue().equals(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        buf.append(getClass().getName());
        buf.append(": value=");
        buf.append(getValue());
        buf.append(")");
        return buf.toString();
    }
}
