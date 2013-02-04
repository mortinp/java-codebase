/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.searches.modules;

import org.base.dao.searches.IDataMappingStrategy;

/**
 *
 * @author martin
 */
@Deprecated
public interface IDataSearchModule {    
    public String getQuery();    
    public Object[] getParameters();    
    public IDataMappingStrategy getDataMappingStrategy();    
}
