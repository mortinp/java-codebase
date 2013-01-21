/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.security.auth.handlers;

import javax.swing.JDialog;

/**
 *
 * @author mproenza
 */
public abstract class AbstractAuthDialog extends JDialog {
    
    public AbstractAuthDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }
    
    public abstract String getUserName();
    public abstract char[] getPassword();
}
