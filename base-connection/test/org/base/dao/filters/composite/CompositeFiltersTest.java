/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.dao.filters.composite;

import java.text.Format;
import java.util.Date;
import junit.framework.TestCase;
import org.base.dao.filters.FilterBase;
import org.base.dao.filters.FilterBetween;
import org.base.dao.filters.IFilter;
import org.base.dao.filters.FilterSimple;

/**
 *
 * @author Maylen
 */
public class CompositeFiltersTest extends TestCase {
    
    public void testCompositeFiltersExpanded() {
        IFilter a = new LeafFilter("a");
        IFilter b = new LeafFilter("b");
        IFilter c = new LeafFilter("c");
        IFilter d = new LeafFilter("d");
        IFilter e = new LeafFilter("e");
        
        AbstractCompositeFilter aORb = new FilterCompositeOR();
        aORb.addFilter(a);
        aORb.addFilter(b);

        AbstractCompositeFilter cANDd = new FilterCompositeAND();
        cANDd.addFilter(c);
        cANDd.addFilter(d);

        AbstractCompositeFilter eORXXX = new FilterCompositeOR();
        eORXXX.addFilter(cANDd);
        eORXXX.addFilter(e);

        AbstractCompositeFilter full = new FilterCompositeOR();
        full.addFilter(aORb);
        full.addFilter(eORXXX);
        
        String exp = full.getFilterExpression();
        assertEquals("((a or b) or ((c and d) or e))", exp);
    }
    
    public void testCompositeFiltersCompact() {
        IFilter full = 
                new FilterCompositeOR(new FilterCompositeOR(
                    new FilterSimple("fieldA", "valueA"),
                    new FilterSimple("fieldB", "valueB")), 
                new FilterCompositeOR(
                    new FilterCompositeAND(
                        new FilterSimple("fieldC", "valueC"),
                        new FilterSimple("fieldD", "valueD")), 
                    new FilterSimple("fieldE", "valueE")));
        
        String exp = full.getFilterExpression();
        assertEquals(
                "((fieldA = 'valueA' or fieldB = 'valueB') "
                + "or ((fieldC = 'valueC' and fieldD = 'valueD') "
                + "or fieldE = 'valueE'))", exp);
    }
    
    public void testCompositeFiltersVariedNoTypeSpec() {
        //setup
        Date d1 = new Date(1000);
        Date d2 = new Date();
        Format sdf = FilterBase.getDateFormatter();
        String sd1 = sdf.format(d1);
        String sd2 = sdf.format(d2);

        IFilter full = 
                new FilterCompositeOR(new FilterCompositeOR(
                    new FilterSimple("fieldA", "valueA"),
                    new FilterSimple("fieldB", 1)),
                new FilterCompositeOR(
                    new FilterCompositeAND(
                        new FilterBetween("fieldC", d1, d2),
                        new FilterSimple("fieldD", "valueD", "!=")), 
                    new FilterSimple("fieldE", null, "IS NOT")));

        String exp = full.getFilterExpression();
        assertEquals(
                "((fieldA = 'valueA' or fieldB = 1) "
                + "or ((fieldC BETWEEN '" + sd1 + "' AND '" + sd2 + "' and fieldD != 'valueD') "
                + "or fieldE IS NOT NULL))", exp);
    }
}
