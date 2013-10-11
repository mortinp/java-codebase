/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Administrador
 */
public final class ScreenUtils {
    
    //public static Image icon = null;

    public static JDialog showViewModal(JPanel view) {
        JDialog dialog = new JDialog();
        //if(icon != null) dialog.setIconImage(icon);
        dialog.setModal(true);
        dialog.setContentPane(view);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        return dialog;
    }

    public static void showViewModal(JPanel view, JDialog dialog) {        
        dialog.setModal(true);
        dialog.setContentPane(view);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);        
    }
}
