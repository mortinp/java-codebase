/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.composite;

import java.text.Format;
import java.util.Date;
import junit.framework.TestCase;
import org.base.dao.DAOUtils;
import org.base.dao.datasources.connections.ConnectionPoolJDBC;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.IDataSourceContextInjectable;
import org.base.dao.datasources.variations.VariationPostgresql;
import org.base.dao.filters.FilterBetween;
import org.base.dao.filters.FilterDateExtract;
import org.base.dao.filters.FilterMatchExistence;
import org.base.dao.filters.IFilter;
import org.base.dao.filters.FilterSimple;

/**
 *
 * @author Maylen
 */
public class FiltersTest extends TestCase {
    
    public void testBasicFilters() {
        //setup dates
        Date d1 = new Date(1000);
        Date d2 = new Date();
        Format sdf = DAOUtils.getDateFormatter();
        String sd1 = sdf.format(d1);
        String sd2 = sdf.format(d2);
        
        IFilter f1 = new FilterSimple("field", "value");
        assertEquals("field = 'value'", f1.getFilterExpression());
        
        f1 = new FilterSimple("field", "value", FilterSimple.OP_NOT_EQUALS);
        assertEquals("field <> 'value'", f1.getFilterExpression());

        IFilter f2 = new FilterSimple("field", 123);
        assertEquals("field = 123", f2.getFilterExpression());
        
        f2 = new FilterSimple("field", 123, FilterSimple.OP_NOT_EQUALS);
        assertEquals("field <> 123", f2.getFilterExpression());;
        
        IFilter f3 = new FilterSimple("field", d1);
        assertEquals("field = '" + sd1 + "'", f3.getFilterExpression());
        
        f3 = new FilterSimple("field", d1, FilterSimple.OP_NOT_EQUALS);
        assertEquals("field <> '" + sd1 + "'", f3.getFilterExpression());
        
        IFilter f4 = new FilterSimple("field", true);
        assertEquals("field = true", f4.getFilterExpression());
        
        IFilter f5 = new FilterSimple("field", null);
        assertEquals("field IS NULL", f5.getFilterExpression());
        
        IFilter f6 = new FilterSimple("field", null, FilterSimple.OP_NOT_EQUALS);
        assertEquals("field IS NOT NULL", f6.getFilterExpression());
        
        IFilter f7 = new FilterBetween("field", d1, d2);
        assertEquals("field BETWEEN '" + sd1 + "' AND '" + sd2 + "'", f7.getFilterExpression());
        
        IFilter f8 = new FilterDateExtract("field", FilterDateExtract.EXTRACT_MONTH, 4);
        assertEquals("EXTRACT('MONTH', field) = 4", f8.getFilterExpression());
    }
    
    public void testAdvancedFilters() {
        DataSourceContext dsc = new DataSourceContext(new ConnectionPoolJDBC(), new VariationPostgresql("some_schema"));
        
        IFilter f1 = new FilterMatchExistence("sourceField", "targetField", "targetTable");
        ((IDataSourceContextInjectable)f1).setDataSourceContext(dsc);
        assertEquals("sourceField IN (SELECT targetField FROM some_schema.targetTable)", f1.getFilterExpression());
        
    }
}
