/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.util.HashMap;
import java.util.Map;
import org.base.reports.Report;

/**
 *
 * @author mproenza
 */
public class OperationExportReport implements IReportingOperation {
    private String baseReportName;
    private String targetFilePath;
    private String targetFormat;
    
    private Map<String, Object> parametersMap = new HashMap<String, Object>();

    public OperationExportReport(String targetFormat, String baseReportName, String targetFilePath) {
        this.targetFormat = targetFormat;
        this.baseReportName = baseReportName;
        this.targetFilePath = targetFilePath;
    }
    
    public OperationExportReport(String targetFormat, String baseReportName, String targetFilePath, Map<String, Object> parametersMap) {
        this(targetFormat, baseReportName, targetFilePath);
        this.parametersMap = parametersMap;
    }

    @Override
    public Report execute() {
        String nombre = baseReportName.replaceAll(" ", "_");
        Report rep = new Report(); 
        rep.setReportFileName(nombre);
        
        if(!parametersMap.isEmpty())
            rep.setParameters(parametersMap);
        
        rep.exportAs(targetFilePath, targetFormat);
        return rep;
    }
}
