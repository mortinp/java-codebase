/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

/**
 *
 * @author mproenza
 */
public class VariationMAccess implements IDataSourceVariation {

    @Override
    public String getDBObjectExpression(String objectName) {
        return "[" + objectName + "]";
    }
    
}
