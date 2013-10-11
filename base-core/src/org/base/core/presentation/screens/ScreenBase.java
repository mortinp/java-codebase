/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens;

import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author martin
 */
//Esta pantalla tiene el comportamiento general de todas las pantallas de la aplicacion.
//El comportamiento incluye: funciones para abrir y cerrar la pantalla, cierre automatico cuando se aprieta ESC
public class ScreenBase extends JPanel implements IScreen {

    public static final int ACTION_CONFIRMED_OPTION = JOptionPane.YES_OPTION;
    public static final int ACTION_REJECTED_OPTION = JOptionPane.NO_OPTION;
    //contenedor
    protected DialogBase dialog;
    protected String title = "Captar Datos";
    
    
    public static Image icon = null;

    public ScreenBase() {
        initializeDialog();
    }

    public ScreenBase(String title) {
        this.title = title;
        initializeDialog();
    }
    
    private void initializeDialog() {
        JFrame parent = new JFrame();
        if(icon != null) parent.setIconImage(icon);
        this.dialog = new DialogBase(parent, this.title);
    }

    @Override
    public void show() {
        ScreenUtils.showViewModal(this, this.dialog);
    }

    @Override
    public void hide() {
        this.dialog.setVisible(false);
    }

    @Override
    public void close() {
        this.dialog.dispose();
    }

    public static void showInfoMessage(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(String aviso) {
        JOptionPane.showMessageDialog(null, aviso, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public static int showConfirmationMessage(String mensaje) {
        return JOptionPane.showConfirmDialog(null, mensaje, "Confirmación", JOptionPane.YES_NO_OPTION);
    }
}