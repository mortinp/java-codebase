/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.domain.extensions;

/**
 *
 * @author mproenza
 */
public interface ICrudVerifiable {
    public boolean allowAdition();
    public boolean isEditable();
    public boolean isDeletable();
}
