/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.dao;

import org.base.core.domain.Model;
import org.base.utils.exceptions.ExceptionProgrammerMistake;
import org.base.dao.DAOBase;
import org.base.dao.searches.IDataMappingStrategy;

/**
 *
 * @author mproenza
 */
public abstract class DAOModel extends DAOBase {
    
    public DAOModel(String dataSourceContextName, IDataMappingStrategy mappingStrategy) {
        super(dataSourceContextName, mappingStrategy);
    }
    
    @Override
    public Object getKeyValueExpression(Object obj) {
        if(obj instanceof Model) return ((Model)obj).getKeyValueExpression();
        throw new ExceptionProgrammerMistake("Not able to find a key value expression. Try making your class implement '" + Model.class.getName() + "'");
        
    }
    
}
