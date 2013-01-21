/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.tests.validators;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import junit.framework.TestCase;
import org.base.core.presentation.validation.IValidator;
import org.base.core.presentation.validation.components.samples.better.ValidatorEmptyText;
import org.base.core.presentation.validation.components.samples.better.ValidatorFixedNumberOfChars;
import org.base.core.presentation.validation.components.samples.better.ValidatorMultipleInputVerifications;
import org.base.core.presentation.validation.components.samples.better.ValidatorNotThisValue;

/**
 *
 * @author martin
 */
public class ValidatorsTest extends TestCase {
    
    public void testMultipleValidationInputVerifier() {
        JTextComponent textField = new JTextField();
        
        IValidator multiValidator = new ValidatorMultipleInputVerifications((JTextComponent)textField,
                new IValidator[]{
                    new ValidatorEmptyText(),
                    new ValidatorFixedNumberOfChars(10),
                    new ValidatorNotThisValue("1231231231")},
                true);
        
        
        textField.setText(""); // ValidatorEmptyText-fail
        assertEquals(false, multiValidator.executeValidation());
        
        textField.setText("12345"); // ValidatorEmptyText-OK, ValidatorFixedNumberOfChars-fail
        assertEquals(false, multiValidator.executeValidation());
        
        textField.setText("1231231231"); // ValidatorEmptyText-OK, ValidatorFixedNumberOfChars-OK, ValidatorNotThisValue-fail
        assertEquals(false, multiValidator.executeValidation());
        
        textField.setText("1234567890");
        assertEquals(true, multiValidator.executeValidation());
    }
    
}
