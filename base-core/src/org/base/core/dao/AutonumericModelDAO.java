/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.dao;

import org.base.core.domain.Model;
import org.base.dao.DAOAutonumeric;
import org.base.dao.searches.IDataMappingStrategy;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author mproenza
 */
public abstract class AutonumericModelDAO extends DAOAutonumeric {
    
    public AutonumericModelDAO(String dataSourceContextName, IDataMappingStrategy mappingStrategy) {
        super(dataSourceContextName, mappingStrategy);
    }
    
    @Override
    protected Object getKeyValueExpression(Object obj) {
        if(obj instanceof Model) return ((Model)obj).getKeyValueExpression();
        throw new SystemException("Not able to find a key value expression. Try making your class implement '" + Model.class.getName() + "'");
    }    
}
