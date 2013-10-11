/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.utils.types;

import java.util.List;

/**
 *
 * @author Martin
 */
public class UtilLists {
    public static void insert(List l, int pos, Object value) {
        int size = l.size();
        
        l.add(l.get(l.size() - 1)); // Repeat last at the end
        for (int i = size - 1; i > pos; i--) {
            l.set(i, l.get(i - 1));
        }
        l.set(pos, value);
    }
}
