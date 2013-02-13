/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.service;

import java.util.HashMap;
import java.util.Map;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author mproenza
 */
public class RegistryEntityManager {
    private static Map<String, IEntityManager> entityMgrMap = new HashMap<String, IEntityManager>();
    
    static {
        // TODO: aqui se pueden cargar entity managers desde algun fichero de configuracion inicialmente
    }
    
    public static void registerEntityManager(String entityMgrName, IEntityManager entityMgr) {
        /*for (String alias : entitiesAliases) {
            entityMgrMap.put(alias, entityMgr);
        }*/
        entityMgrMap.put(entityMgrName, entityMgr);
    }
    
    public static IEntityManager getEntityManager(String entityMgrName) {
        IEntityManager mgr = entityMgrMap.get(entityMgrName);
        if(mgr == null) throw new SystemException("No Entity Manager configured as '" + entityMgrName +  "'");
        return mgr;
    }
}
