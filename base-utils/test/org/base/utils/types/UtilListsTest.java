/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.types;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Martin
 */
public class UtilListsTest extends TestCase {
    
    public void testListInsertion() {
        List l = new ArrayList();
        l.add("a");
        l.add("b");
        l.add("c");
        l.add("d");
        
        UtilLists.insert(l, 1, "i1");
        UtilLists.insert(l, 2, "i2");
        UtilLists.insert(l, 3, "i3");
        
        assertEquals("i1", l.get(1));
        assertEquals("i2", l.get(2));
        assertEquals("i3", l.get(3));
        
        assertEquals("b", l.get(4));
        assertEquals("c", l.get(5));
        assertEquals("d", l.get(6));
    }
    
}
