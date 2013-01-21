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
 * @author mproenza
 */
public abstract class DefaultDataMappingStrategy implements IDataMappingStrategy {

    @Override
    public ArrayList createResultList(ResultSet rs) throws SQLException {
        ArrayList lstLista = new ArrayList();
        while (rs.next()) {
            Object obj = createResultObject(rs);
            lstLista.add(obj);
        }
        return lstLista;
    }

    @Override
    public abstract Object createResultObject(ResultSet rs) throws SQLException ;
}
