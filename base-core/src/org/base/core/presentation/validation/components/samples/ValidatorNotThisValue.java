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
 * @author mproenza
 */
public class ValidatorNotThisValue extends AbstractComponentValidator {
    
    // Comparison Modes
    public static final int AS_STRING = 1;
    public static final int AS_NUMBER = 2;
    int comparisonMode = AS_STRING;
    
    String value;

    public ValidatorNotThisValue(String value) {
        this.value = value;
    }

    public ValidatorNotThisValue(String value, int comparisonMode) {
        this.value = value;
        this.comparisonMode = comparisonMode;
    }

    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {        
        /*if(((JTextComponent)objComponent).getText().trim().equals(value)) {            
            message = MessageFactory.getMessage("msg_validator_value_not_allowed", value, objComponent.getName());
            return false;           
        }*/
        Object comp1 = null;
        Object comp2 = null;
        if(comparisonMode == AS_STRING) {
            comp1 = ((JTextComponent)objComponent).getText().trim();
            comp2 = value;
        } else if(comparisonMode == AS_NUMBER) {
            comp1 = Double.parseDouble(((JTextComponent)objComponent).getText().trim());
            comp2 = Double.parseDouble(value);
        }
        
        if(comp1.equals(comp2)) {
            message = MessageFactory.getMessage(
                    "msg_validator_value_not_allowed", 
                    ((JTextComponent)objComponent).getText().trim()/*value*/, 
                    objComponent.getName());
            return false;
        }
        return true;
    }
}
