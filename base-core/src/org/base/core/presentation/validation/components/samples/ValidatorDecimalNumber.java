/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples;

import org.base.core.presentation.validation.components.samples.AbstractComponentValidator;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.base.core.util.messages.MessageFactory;

/**
 *
 * @author martin
 */
public class ValidatorDecimalNumber extends AbstractComponentValidator {
    private String regex = null;
    
    public ValidatorDecimalNumber() {
        regex = "^(\\d{0,10}(\\.\\d{1,2}))+|(\\d{1,10}(\\.\\d{0,2})?)$";
    }
    
    public ValidatorDecimalNumber(int beforeColon, int afterColon) {
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
    
}
