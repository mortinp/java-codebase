/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.awt.Container;
import java.util.Map;
import org.base.reports.Report;
import org.base.reports.ReportViewer;

/**
 *
 * @author mproenza
 */
public class OperationLoadReport extends AbstractReportingOperation {
    private Container container;

    public OperationLoadReport(Container container, String strNombreReporte) {
        super(strNombreReporte);
        this.container = container;
    }
    
    public OperationLoadReport(Container container, String strNombreReporte, Map<String, Object> mapParametros) {
        super(strNombreReporte, mapParametros);
        this.container = container;
    }

    @Override
    public void doStuff(Report rep) {
        ReportViewer.loadInContainer(container, rep);
    }
}
