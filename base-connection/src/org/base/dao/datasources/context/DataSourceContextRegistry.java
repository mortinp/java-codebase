/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.context;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mproenza
 */
public class DataSourceContextRegistry {
    private static Map<String, DataSourceContext> dsContextMap = new HashMap<String, DataSourceContext>();
    
    public static void addDataSourceContext(String dsCtxtName, DataSourceContext dsCtxt) {
        dsContextMap.put(dsCtxtName, dsCtxt);
    }
    
    public static DataSourceContext getDataSourceContext(String dsCtxtName) {
        return dsContextMap.get(dsCtxtName);
    }
}
