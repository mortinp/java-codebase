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
public class OperationViewReport extends AbstractReportingOperation {
    private String strTitulo;

    public OperationViewReport(String strNombreReporte, String strTitulo) {
        super(strNombreReporte);
        this.strTitulo = strTitulo;
    }
    
    public OperationViewReport(String strNombreReporte, String strTitulo, Map<String, Object> mapParametros) {
        super(strNombreReporte, mapParametros);
        this.strTitulo = strTitulo;
    }

    @Override
    public void doStuff(Report rep) {
        ReportViewer.viewReport(rep, this.strTitulo);
    }
}
