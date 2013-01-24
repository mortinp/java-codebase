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
 * @author martin
 */
public class EmptyTextValidator implements IComponentValidator {
    
    //the message to show
    private String message = null;

    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        
        if(((JTextComponent)objComponent).getText().trim().isEmpty()) {            
            message = MessageFactory.getMessage("msg_campo_obligatorio_vacio", objComponent.getName());
            return false;           
        }
        return true;
    }

    @Override
    public String getValidationMessage() {
        return message;
    }
}
