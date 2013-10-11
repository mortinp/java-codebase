/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.base.utils.messages.MessageFactory;

/**
 *
 * @author martin
 */
public class ValidatorEmptyText extends AbstractComponentValidator {

    public ValidatorEmptyText() {
    }


    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        if(((JTextComponent)objComponent).getText().trim().isEmpty()) {            
            message = MessageFactory.getMessage("msg_validator_mandatory_but_empty", objComponent.getName());
            return false;           
        }
        return true;
    }
}
