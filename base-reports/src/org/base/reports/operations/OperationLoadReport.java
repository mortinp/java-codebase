/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import org.base.reports.Report;
import org.base.reports.ReportViewer;

/**
 *
 * @author mproenza
 */
public class OperationLoadReport implements IReportingOperation {
    private String strNombreReporte;
    private Container container;
    
    private Map<String, Object> mapParametros = new HashMap<String, Object>();

    public OperationLoadReport(Container container, String nombreReporte) {
        this.container = container;
        this.strNombreReporte = nombreReporte;
    }
    
    public OperationLoadReport(Container container, String strNombreReporte, Map<String, Object> mapParametros) {
        this(container, strNombreReporte);
        this.mapParametros = mapParametros;
    }

    @Override
    public Report execute() {
        Report rep = new Report(); 
        rep.setReportFileName(strNombreReporte + ".jasper");
        
        if(!mapParametros.isEmpty())
            rep.setParameters(mapParametros);
        ReportViewer.loadInContainer(container, rep);
        return rep;
    }
}
