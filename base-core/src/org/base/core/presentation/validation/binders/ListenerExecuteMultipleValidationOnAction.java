/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.binders;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.base.core.presentation.validation.IValidationStatusReceiver;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author Martin
 */
public class ListenerExecuteMultipleValidationOnAction implements ActionListener {
    
    IValidator[] validators = new IValidator[]{};
    IValidationStatusReceiver statusReceiver;

    public ListenerExecuteMultipleValidationOnAction(IValidator [] validators, IValidationStatusReceiver statusReceiver) {
        this.validators = validators;
        this.statusReceiver = statusReceiver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean OK = true;
        for (IValidator validator : validators) {
            OK = validator.executeValidation();
            if(!OK) {                
                statusReceiver.validateFailed(validator.getValidationMessage());
                break;
            }
        }
        if(OK) statusReceiver.validatePassed();
    }
}
