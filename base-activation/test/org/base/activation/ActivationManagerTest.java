/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.activation;

import junit.framework.TestCase;

/**
 *
 * @author mproenza
 */
public class ActivationManagerTest extends TestCase {
    
    public void testKeyValidation() {
        String key = null;
        
        key = "1213283442-253126";
        assertEquals(true, new ActivationManager().isActivationKeyValid(key));
        
        System.out.println();
        
        key = "1213283442-456259";
        assertEquals(false, new ActivationManager().isActivationKeyValid(key));
        
    }
}
