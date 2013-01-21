/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.searches;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author martin
 */
public interface IDataMappingStrategy {
    
    public ArrayList createResultList(ResultSet rs) throws SQLException;

    public Object createResultObject(ResultSet rs) throws SQLException;
    
}
