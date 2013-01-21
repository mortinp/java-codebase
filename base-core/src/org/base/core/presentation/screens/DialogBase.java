/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author martin
 */
public class DialogBase extends javax.swing.JDialog  {    
    public DialogBase() {
        super();
        addCloseOnEscape(this);
    }

    public DialogBase(Dialog owner) {
        super(owner);
        addCloseOnEscape(this);
    }

    public DialogBase(Dialog owner, boolean modal) {
        super(owner, modal);
        addCloseOnEscape(this);
    }

    public DialogBase(Dialog owner, String title) {
        super(owner, title);
        addCloseOnEscape(this);
    }

    public DialogBase(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        addCloseOnEscape(this);
    }

    public DialogBase(Frame owner) {
        super(owner);
        addCloseOnEscape(this);
    }

    public DialogBase(Frame owner, boolean modal) {
        super(owner, modal);
        addCloseOnEscape(this);
    }

    public DialogBase(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        addCloseOnEscape(this);
    }

    public DialogBase(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        addCloseOnEscape(this);
    }

    public DialogBase(java.awt.Frame owner, String title) {
        super(owner, title);
        addCloseOnEscape(this);
    }

    private void addCloseOnEscape(final JDialog dialog) {
        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        };

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        dialog.getRootPane().registerKeyboardAction(cancelListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
    
    public void registerContextActionListener(int keyCode, ActionListener action) {
        KeyStroke stroke = KeyStroke.getKeyStroke(keyCode, 0);
        rootPane.registerKeyboardAction(action, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public void windowOpened(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosed(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeactivated(WindowEvent we) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
