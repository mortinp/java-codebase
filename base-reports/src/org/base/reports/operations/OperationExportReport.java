/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.util.Map;
import org.base.reports.Report;

/**
 *
 * @author mproenza
 */
public class OperationExportReport extends AbstractReportingOperation {
    private String targetFilePath;
    private String targetFormat;

    public OperationExportReport(String targetFormat, String strNombreReporte, String targetFilePath) {
        super(strNombreReporte);
        this.targetFormat = targetFormat;
        this.targetFilePath = targetFilePath;
    }
    
    public OperationExportReport(String targetFormat, String strNombreReporte, String targetFilePath, Map<String, Object> mapParametros) {
        super(strNombreReporte, mapParametros);
        this.targetFormat = targetFormat;
        this.targetFilePath = targetFilePath;
    }

    @Override
    public void doStuff(Report rep) {
        rep.exportAs(targetFilePath, targetFormat);
    }
}
