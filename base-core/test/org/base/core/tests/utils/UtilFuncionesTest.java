package org.base.core.tests.utils;


import junit.framework.TestCase;
import org.base.core.util.types.UtilFunciones;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mproenza
 */
public class UtilFuncionesTest extends TestCase {
    
    public void testDoubleToStr() {
        double fValue = 123456789.12;
        String strValue = UtilFunciones.doubleToString(fValue, 2);
        
        assertEquals("123456789.12", strValue);
    }
    
}
