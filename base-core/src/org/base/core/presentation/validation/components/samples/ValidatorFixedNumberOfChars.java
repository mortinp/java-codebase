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
public class ValidatorFixedNumberOfChars extends AbstractComponentValidator {
    private int numberOfChars;

    public ValidatorFixedNumberOfChars(int numberOfDigits) {
        this.numberOfChars = numberOfDigits;
    }

    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        if (((JTextComponent)objComponent).getText().trim().length() != numberOfChars) {
            message = MessageFactory.getMessage("msg_validator_value_incorrect_length", objComponent.getName(), numberOfChars);
            return false;
        }
        return true;
    }
}
