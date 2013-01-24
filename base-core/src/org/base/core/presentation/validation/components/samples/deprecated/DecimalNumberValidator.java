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
public class DecimalNumberValidator implements IComponentValidator {
    
    //the message to show
    private String message = null;
    
    private String regex = null;
    
    public DecimalNumberValidator() {
        //regex = "\\d{1,10}\\.{0,1}\\d{0,2}";//default: 10:2
        regex = "^(\\d{0,10}(\\.\\d{1,2}))+|(\\d{1,10}(\\.\\d{0,2})?)$";
    }
    
    public DecimalNumberValidator(int beforeColon, int afterColon) {
        //regex = "\\d{1," + beforeColon + "}\\.{0,1}\\d{0," + afterColon + "}";
        regex = "^(\\d{0," + beforeColon + "}(\\.\\d{1," + afterColon + "}))+|(\\d{1," + beforeColon + "}(\\.\\d{0," + afterColon + "})?)$";
    } 
    
    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        if(((JTextComponent)objComponent).getText().isEmpty() || //if it is empty, then it is valid. Emptyness should be checked with another validator
            !((JTextComponent)objComponent).getText().matches(regex)) {
            message = MessageFactory.getMessage("msg_campo_formato_incorrecto", objComponent.getName());
            return false;           
        }
        return true;
    }

    @Override
    public String getValidationMessage() {
        return message;
    }
    
}
