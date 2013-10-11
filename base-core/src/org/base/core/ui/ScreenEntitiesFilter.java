/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import org.base.components.models.TableMO;
import org.base.core.commands.ICommand;
import org.base.core.delegates.IModelReceiver;
import org.base.core.exceptions.DomainException;
import org.base.core.presentation.listeners.ListenerExecuteCommandOnAction;
import org.base.core.presentation.listeners.ListenerExecuteCommandOnClick;
import org.base.core.presentation.listeners.ListenerExecuteCommandOnCustomKey;
import org.base.core.presentation.screens.extensions.TableModelFilterAndSelection;
import org.base.core.presentation.util.ViewUtils;

/**
 *
 * @author mproenza
 */
public class ScreenEntitiesFilter extends TableModelFilterAndSelection {
    private JTable tblNomenclador;
    private JTextComponent txtBuscar;
    private JButton btnAceptar;
    private JButton btnCerrar;
    
    
    public ScreenEntitiesFilter(String title, IModelReceiver objRecibidor) {
        super(title, objRecibidor);    
    } 
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INICIO">
    public void setupComponents(JTable table, JTextComponent txtFilter, JButton btnAccept, JButton btnClose) {
        tblNomenclador = table;
        txtBuscar = txtFilter;
        btnAceptar = btnAccept;
        btnCerrar = btnClose;
        inicializarDinamica();
        txtBuscar.requestFocusInWindow();
        
        setTable(tblNomenclador);
    }
    
    private void inicializarDinamica() {
        tblNomenclador.addMouseListener(new ListenerExecuteCommandOnClick(new ICommand() {
            @Override
            public void execute() {
                aceptar();
            }
        }, true));
        
        tblNomenclador.addKeyListener(new ListenerExecuteCommandOnCustomKey(KeyEvent.VK_ENTER, new ICommand() {
            @Override
            public void execute() {
                aceptar();
            }
        }));
        
        txtBuscar.addKeyListener(new ListenerExecuteCommandOnCustomKey(KeyEvent.VK_ENTER, new ICommand() {
            @Override
            public void execute() {
                if(tblNomenclador.getRowCount() != 0) {
                    tblNomenclador.requestFocusInWindow();
                    tblNomenclador.setRowSelectionInterval(0, 0);
                }
            }
        }));
        
        txtBuscar.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if(((TableMO)tblNomenclador.getModel()).getRowCount() != 0) {
                    applyFilter(txtBuscar.getText());
                } 
            }
        });
        
        btnAceptar.addActionListener(new ListenerExecuteCommandOnAction(new ICommand() {
            @Override
            public void execute() {
                aceptar();
            }
        }));
        
        btnCerrar.addActionListener(new ListenerExecuteCommandOnAction(new ICommand() {
            @Override
            public void execute() {
                cerrar();
            }
        }));
    }    
    
    private void aceptar() {
        if(seleccionOK()) {
            try {
                sendObject(tblNomenclador.convertRowIndexToModel(tblNomenclador.getSelectedRow()));
            } catch (DomainException ex) {
                showWarningMessage(ex.getMessage());
            }
        } else {
            showWarningMessage("Escoja una fila.");
        }
    }
    
    private void cerrar() {
        close();
    }
    
    private boolean seleccionOK() {
        return tblNomenclador.getRowCount() != 0 && tblNomenclador.getSelectedRowCount() == 1;
    } 
    
    @Override
    public void show() {
        txtBuscar.setText(null);
        txtBuscar.requestFocusInWindow();
        ViewUtils.arrangeTableColumns(tblNomenclador);
        super.show();
    }
}
