/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

/**
 *
 * @author martin
 */
public class DefaultVariation implements IDataSourceVariation {

    @Override
    public String getDBObjectExpression(String objectName) {
        return objectName;// Simply return the same object name
    }
    
}
