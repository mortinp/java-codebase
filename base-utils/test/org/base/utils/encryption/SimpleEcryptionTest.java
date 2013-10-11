/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.encryption;

import junit.framework.TestCase;
import org.base.utils.encryption.SimpleEncryption;

/**
 *
 * @author martin
 */
public class SimpleEcryptionTest extends TestCase {
    
    public void testSimpleEncryption() {
        String original = "pro12";
        String encrypted = SimpleEncryption.encrypt(original);
        assertEquals("&3▼wE", encrypted);
        String decrypted = SimpleEncryption.decrypt(encrypted);
        assertEquals(original, decrypted);
        
        original = "zamora*%5";
        encrypted = SimpleEncryption.encrypt(original);
        assertEquals("8@,8♣@t☻Y", encrypted);
        decrypted = SimpleEncryption.decrypt(encrypted);
        assertEquals(original, decrypted);
        
    }
    
}
