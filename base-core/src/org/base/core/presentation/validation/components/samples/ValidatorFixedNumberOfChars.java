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
public class ValidatorFixedNumberOfChars extends AbstractComponentValidator {
    private int numberOfDigits;

    public ValidatorFixedNumberOfChars(int numberOfDigits) {
        this.numberOfDigits = numberOfDigits;
    }

    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        if (((JTextComponent)objComponent).getText().trim().length() != numberOfDigits) {
            message = MessageFactory.getMessage("msg_campo_numerico_longitud_incorrecta", objComponent.getName(), numberOfDigits);
            return false;
        }
        return true;
    }
}
