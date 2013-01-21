/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.util.HashMap;
import java.util.Map;
import org.base.reports.Report;
import org.base.reports.ReportViewer;

/**
 *
 * @author mproenza
 */
public class OperationViewStaticReport implements IReportingOperation {
    private String strNombreReporte;
    private String strTitulo;
    
    private Map<String, Object> mapParametros = new HashMap<String, Object>();

    public OperationViewStaticReport(String strNombreReporte, String strTitulo) {
        this.strNombreReporte = strNombreReporte;
        this.strTitulo = strTitulo;
    }
    
    public OperationViewStaticReport(String strNombreReporte, String strTitulo, Map<String, Object> mapParametros) {
        this(strNombreReporte, strTitulo);
        this.mapParametros = mapParametros;
    }
    
    @Override
    public Report execute() {
        Report rep = new Report();
        rep.setEmptyDataSource(true); // use empty data source
        rep.setReportFileName(strNombreReporte + ".jasper");
        
        if(!mapParametros.isEmpty())
            rep.setParameters(mapParametros);
        
        ReportViewer.viewReport(rep, this.strTitulo);
        return rep;
    }
}
