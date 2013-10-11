/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.context;

import java.util.HashMap;
import java.util.Map;
import org.base.dao.exceptions.ExceptionDBProgrammerMistake;

/**
 *
 * @author mproenza
 */
public class RegistryDataSourceContext {
    private static Map<String, DataSourceContext> dsContextMap = new HashMap<String, DataSourceContext>();
    
    public static void registerDataSourceContext(String dsCtxtName, DataSourceContext dsCtxt) {
        dsContextMap.put(dsCtxtName, dsCtxt);
    }
    
    public static DataSourceContext getDataSourceContext(String dsCtxtName) {
        DataSourceContext dsContext = dsContextMap.get(dsCtxtName);
        if(dsContext == null) throw new ExceptionDBProgrammerMistake("No datasource context configured as '" + dsCtxtName +  "'");
        return dsContext;
    }
}
