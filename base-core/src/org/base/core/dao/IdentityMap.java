/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.base.core.domain.extensions.ICacheable;
import org.base.dao.DAOFactory;
import org.base.dao.IDAO;

/**
 *
 * @author mproenza
 */
public class IdentityMap {
    private static Map<String, List<ICacheable>> cachedObjects = new HashMap<String, List<ICacheable>>();
    
    public static Object findObject(String alias, Object toMatch) {
        Object match = null;
        
        // Try to find it in the cached objects
        List<ICacheable> objectsList = cachedObjects.get(alias);
        if(objectsList != null) {
            if(!objectsList.isEmpty()) { // maybe it's here
                for (ICacheable obj : objectsList) {
                    if(obj.matches(toMatch)) {
                        match = obj;
                        break;
                    }
                }
            }
        } else { // create a new entry for the alias
            cachedObjects.put(alias, new ArrayList<ICacheable>());
        }
        
        // Try to find it in the database
        if(match == null) {
            IDAO dao = DAOFactory.getDAO(alias);
            match = dao.findOne(toMatch);
            
            if(match != null) cachedObjects.get(alias).add((ICacheable)match);
        }
        
        return match;
    }
    
    public static void clearMap() {
        cachedObjects.clear();
    }
}
