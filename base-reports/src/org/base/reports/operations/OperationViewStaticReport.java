/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.util.Map;
import org.base.reports.Report;
import org.base.reports.ReportViewer;

/**
 *
 * @author mproenza
 */
public class OperationViewStaticReport extends AbstractReportingOperation {
    private String strTitulo;

    public OperationViewStaticReport(String strNombreReporte, String strTitulo) {
        super(strNombreReporte);
        this.strTitulo = strTitulo;
    }
    
    public OperationViewStaticReport(String strNombreReporte, String strTitulo, Map<String, Object> mapParametros) {
        super(strNombreReporte, mapParametros);
        this.strTitulo = strTitulo;
    }

    @Override
    public void doStuff(Report rep) {
        rep.setEmptyDataSource(true); // use empty data source
        ReportViewer.viewReport(rep, this.strTitulo);
    }
}
