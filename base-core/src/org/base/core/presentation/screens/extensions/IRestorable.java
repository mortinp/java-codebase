/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

/**
 *
 * @author mproenza
 */
public interface IRestorable {
    public Object getCurrentState();
    public void restoreFromState(Object state);
}
