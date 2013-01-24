/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.tests.validators;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import junit.framework.TestCase;
import org.base.core.presentation.validation.IValidator;
import org.base.core.presentation.validation.components.samples.ValidatorEmptyText;
import org.base.core.presentation.validation.components.samples.ValidatorFixedNumberOfChars;
import org.base.core.presentation.validation.components.ValidatorMultipleInputVerifications;
import org.base.core.presentation.validation.components.samples.AbstractComponentValidator;
import org.base.core.presentation.validation.components.samples.ValidatorDecimalNumber;
import org.base.core.presentation.validation.components.samples.ValidatorNotThisValue;

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
    
    public void testDecimalNumberVlidator() {
        IValidator decimal = new ValidatorDecimalNumber(10, 2);
        JTextField textComp = new JTextField();
        ((AbstractComponentValidator)decimal).injectComponent(textComp); 
        
        //----- TRUE ----------------------------
        textComp.setText("123456789");//9
        assertEquals(true, decimal.executeValidation());
        
        textComp.setText("123456789.1");//9,1
        assertEquals(true, decimal.executeValidation());
        
        textComp.setText("123456789.12");//9,2
        assertEquals(true, decimal.executeValidation());
        
        textComp.setText("1234567890");//10
        assertEquals(true, decimal.executeValidation());
        
        textComp.setText("1234567890.1");//10,1
        assertEquals(true, decimal.executeValidation());
        
        textComp.setText("1234567890.12");//10,2
        assertEquals(true, decimal.executeValidation());
        
        
        //---------- FALSE ------------------------
        textComp.setText("123456789.123");//9,3
        assertEquals(false, decimal.executeValidation());
        
        textComp.setText("1234567890.123");//10,3
        assertEquals(false, decimal.executeValidation());
        
        textComp.setText("12345678901");//11
        assertEquals(false, decimal.executeValidation());
        
        textComp.setText("12345678901.12");//11,2
        assertEquals(false, decimal.executeValidation());
    }
    
}
