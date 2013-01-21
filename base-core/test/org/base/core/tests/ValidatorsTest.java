/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.tests;

import javax.swing.JTextField;
import junit.framework.TestCase;
import org.base.core.presentation.validation.IComponentValidator;
import org.base.core.presentation.validation.components.samples.DecimalNumberValidator;

/**
 *
 * @author mproenza
 */
public class ValidatorsTest extends TestCase {
    
    public void testDecimalNumberVlidator() {
        IComponentValidator decimal = new DecimalNumberValidator(10, 2);
        JTextField textComp = new JTextField();
        
        //----- TRUE ----------------------------
        textComp.setText("123456789");//9
        assertEquals(true, decimal.validateComponent(textComp));
        
        textComp.setText("123456789.1");//9,1
        assertEquals(true, decimal.validateComponent(textComp));
        
        textComp.setText("123456789.12");//9,2
        assertEquals(true, decimal.validateComponent(textComp));
        
        textComp.setText("1234567890");//10
        assertEquals(true, decimal.validateComponent(textComp));
        
        textComp.setText("1234567890.1");//10,1
        assertEquals(true, decimal.validateComponent(textComp));
        
        textComp.setText("1234567890.12");//10,2
        assertEquals(true, decimal.validateComponent(textComp));
        
        
        //---------- FALSE ------------------------
        textComp.setText("123456789.123");//9,3
        assertEquals(false, decimal.validateComponent(textComp));
        
        textComp.setText("1234567890.123");//10,3
        assertEquals(false, decimal.validateComponent(textComp));
        
        textComp.setText("12345678901");//11
        assertEquals(false, decimal.validateComponent(textComp));
        
        textComp.setText("12345678901.12");//11,2
        assertEquals(false, decimal.validateComponent(textComp));
    }
}
