/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.active_record.dao;

import java.util.List;
import org.base.dao.DAOBase;
import org.base.dao.active_record.model.ActiveRecordBase;
import org.base.dao.searches.IDataMappingStrategy;

/**
 *
 * @author Martin
 */
public abstract class DefaultActiveRecordDAO extends DAOBase {
    
    public DefaultActiveRecordDAO(String dataSourceContextName, IDataMappingStrategy toObjectMapper) {
        super(dataSourceContextName, toObjectMapper);
    }
    
    @Override
    public Object findOne(Object valorLlave){
        ActiveRecordBase obj = (ActiveRecordBase)super.findOne(valorLlave);
        obj.setRecorded(true);
        return obj;
    }
    
    @Override
    public List findAll() {
        List all = super.findAll();
        
        // Mark objects as recorded
        for (Object obj : all) {
            ((ActiveRecordBase)obj).setRecorded(true);
        }
        
        return all;
    }
    
}
