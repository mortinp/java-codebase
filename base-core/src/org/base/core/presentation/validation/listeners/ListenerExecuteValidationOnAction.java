/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.base.core.presentation.validation.IValidationStatusReceiver;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author Martin
 */
public class ListenerExecuteValidationOnAction implements ActionListener {
    
    IValidator validator;
    IValidationStatusReceiver statusReceiver;

    public ListenerExecuteValidationOnAction(IValidator validator, IValidationStatusReceiver statusReceiver) {
        this.validator = validator;
        this.statusReceiver = statusReceiver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!validator.executeValidation()) statusReceiver.validateFailed(validator.getValidationMessage());
        else statusReceiver.validatePassed();
    }
}
