/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.tests.pico;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.containers.TransientPicoContainer;
import org.picocontainer.injectors.MethodInjection;
import org.picocontainer.injectors.Reinjection;
import org.picocontainer.injectors.Reinjector;

/**
 *
 * @author martin
 */
public class PicoContainerTest extends TestCase {
    
    FooClassA a;
    FooClassB b;
    
    @Override
    public void setUp() {
        a = new FooClassA(3, "some string");
        b = new FooClassB();
    }
    
    public void testLazySetterInjection() {
        try {
            // Variant 1
            MutablePicoContainer pico = new TransientPicoContainer(); 
            pico.addComponent(b);
            pico.addComponent(a);  
            
     
            Method method = FooClassA.class.getMethod("setLazyAttr", FooClassB.class);
            new Reinjector(pico).reinject(FooClassA.class, method);
            
            FooClassA a1 = pico.getComponent(FooClassA.class);
            assertEquals(true, a1.loadedLazyObject());
            
            // Variant 2
           /* MutablePicoContainer pico = new TransientPicoContainer(new Reinjection(new MethodInjection("setLazyAttr"), null), null);  
            pico.addComponent(a);  
            pico.addComponent(b);
            
            FooClassA a1 = pico.getComponent(FooClassA.class);
            assertEquals(true, a1.loadedLazyObject());*/
            
            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(PicoContainerTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PicoContainerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
