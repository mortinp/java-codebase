/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.searches.deprecated;

import org.base.dao.searches.IDataMappingStrategy;

/**
 *
 * @author martin
 * 
 * Esta clase solo bloquea la posibilidad de retornar parametros modificando este metodo con 'final'
 */
@Deprecated
public abstract class SearchAllModule implements IDataSearchModule {

    @Override
    public abstract String getQuery();
    
    @Override
    public final Object[] getParameters() {
        return new Object[]{};
    }

    @Override
    public abstract IDataMappingStrategy getDataMappingStrategy();
    
}
