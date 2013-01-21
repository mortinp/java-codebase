/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.base.core.presentation.validation.IComponentValidationStatusReceiver;
import org.base.core.presentation.validation.IComponentValidator;

/**
 *
 * @author Martin
 */
public class ListenerExecuteComponentValidationOnAction implements ActionListener {
    
    IComponentValidator validator;
    IComponentValidationStatusReceiver statusReceiver;

    public ListenerExecuteComponentValidationOnAction(IComponentValidator validator, IComponentValidationStatusReceiver statusReceiver) {
        this.validator = validator;
        this.statusReceiver = statusReceiver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent comp = (JComponent) e.getSource();
        if(!validator.validateComponent(comp)) {
            statusReceiver.validationFailed(comp, validator.getValidationMessage());
        }
    }
}
