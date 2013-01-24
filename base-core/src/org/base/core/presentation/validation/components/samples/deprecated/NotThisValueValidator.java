/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples.deprecated;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.base.core.presentation.validation.IComponentValidator;
import org.base.core.util.messages.MessageFactory;

/**
 *
 * @author mproenza
 */
public class NotThisValueValidator implements IComponentValidator {
    
    //the message to show
    private String message = null;
    String value;
    
    public NotThisValueValidator(String value) {
        this.value = value;
    }

    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        
        if(((JTextComponent)objComponent).getText().trim().equals(value)) {            
            message = MessageFactory.getMessage("msg_campo_valor_incorrecto", value, objComponent.getName());
            return false;           
        }
        return true;
    }

    @Override
    public String getValidationMessage() {
        return message;
    }
}
