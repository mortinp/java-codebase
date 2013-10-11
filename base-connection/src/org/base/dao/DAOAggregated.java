/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import org.base.dao.searches.IDataMappingStrategy;
import java.util.List;

/**
 *
 * @author martin
 */
public abstract class DAOAggregated extends DAOBase {
    
    public DAOAggregated(String dataSourceContextName, IDataMappingStrategy toObjectMapper) {
        super(dataSourceContextName, toObjectMapper);
    }
    
    private String getFindByParentStatement() {
        String sql = buildSQLWithCondition(parseStatement(getFindAllStatement()), getTableName() + "." + getParentRefFieldName() + " = ?");
        return sql;
    }
    
    protected abstract String getParentRefFieldName();
    
    public List findByParent(Object valorID) {
        return findMany(getFindByParentStatement(), new Object[] {valorID});
    }
}
