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
public class ValidatorEmail extends AbstractComponentValidator {
    private String regex = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    
    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        if(((JTextComponent)objComponent).getText().isEmpty()) return true;// If it is empty, then it is valid. Emptyness should be checked with another validator
        else if(!((JTextComponent)objComponent).getText().matches(regex)) {
            message = MessageFactory.getMessage("msg_validator_value_incorrect_format", objComponent.getName());
            return false;           
        }
        return true;
    }
    
}
