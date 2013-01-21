/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.listeners.custom;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.text.JTextComponent;

/**
 *
 * @author martin
 */
public class ListenerSelectTextOnFocus implements FocusListener {

    @Override
    public void focusGained(FocusEvent e) {
        JTextComponent comp = (JTextComponent)e.getComponent();
        if(comp == null) return;
        comp.selectAll();
    }

    @Override
    public void focusLost(FocusEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
