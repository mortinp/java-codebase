/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao;

import java.util.Map;
import junit.framework.TestCase;
import org.base.dao.datasources.connections.ConnectionPoolJDBC;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.DataSourceTemplateFactory;
import org.base.dao.datasources.context.RegistryDataSourceContext;
import org.base.dao.datasources.variations.VariationPostgresql;

/**
 *
 * @author mproenza
 */
public class DAOBaseFunctionsTest extends TestCase {
    
    DAOBase dao = new DAOBase(DataSourceTemplateFactory.mainDbContextName, null) {

        @Override
        public String getTableName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getKeyFields() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object getKeyValueExpression(Object obj) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected Map<String, Object> getInsertionMap(Object obj) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected Map<String, Object> getUpdateMap(Object obj) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        protected String getFindAllStatement() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };
    
    static {
        RegistryDataSourceContext.registerDataSourceContext(DataSourceTemplateFactory.mainDbContextName, new DataSourceContext(new ConnectionPoolJDBC(), new VariationPostgresql("schema")));
    }
    
    public void testFunctionParseStatement() {
        String stmt = "some stuff :aaa some other stuff :bbb and more stuff :aaa";
        assertEquals("some stuff schema.aaa some other stuff schema.bbb and more stuff schema.aaa", dao.parseStatement(stmt));
        
        stmt = "some stuff :aaa.a some other stuff :bbb.b and more stuff :aaa.c=:ccc.d";
        assertEquals("some stuff schema.aaa.a some other stuff schema.bbb.b and more stuff schema.aaa.c = schema.ccc.d", dao.parseStatement(stmt));
    }
    
}
