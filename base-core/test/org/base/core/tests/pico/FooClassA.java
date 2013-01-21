/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.tests.pico;

/**
 *
 * @author martin
 */
public class FooClassA {
    
    int someAttr;
    String anotherAttr;
    
    // Lazy attr
    FooClassB lazyAttr = null;

    public FooClassA(int someAttr, String anotherAttr) {
        this.someAttr = someAttr;
        this.anotherAttr = anotherAttr;
    }

    public void setLazyAttr(FooClassB lazyAttr) {
        this.lazyAttr = lazyAttr;
    }
    
    public boolean loadedLazyObject() {
        return lazyAttr != null;
    }
    
}
