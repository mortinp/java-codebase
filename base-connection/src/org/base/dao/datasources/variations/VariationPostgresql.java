/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.datasources.variations;

/**
 *
 * @author mproenza
 */
public class VariationPostgresql implements IDataSourceVariation {
    String schema;
    
    public VariationPostgresql(String schema) {
        this.schema = schema;
    }

    @Override
    public String getDBObjectExpression(String objectName) {
        return schema + "." + objectName;
    }
}
