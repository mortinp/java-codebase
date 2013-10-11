package org.base.utils.types;


import junit.framework.TestCase;
import org.base.utils.types.UtilFormatting;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mproenza
 */
public class UtilTest extends TestCase {
    
    public void testDoubleToStr() {
        double fValue = 123456789.12;
        String strValue = UtilFormatting.doubleToString(fValue, 2);
        
        assertEquals("123456789.12", strValue);
    }
    
}
