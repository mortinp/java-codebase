/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.tasks;

import org.base.reports.operations.OperationExportReport;
import java.util.Map;
import org.jdesktop.application.Application;

/**
 *
 * @author mproenza
 */
public class TaskExportReport extends TaskExecuteOperation {
    
    public TaskExportReport(Application app, String targetFormat, String baseReportName, String targetFilePath) {
        super(app, new OperationExportReport(targetFormat, baseReportName, targetFilePath));
    }
    
    public TaskExportReport(Application app, String targetFormat, String baseReportName, String targetFilePath, Map<String, Object> parametersMap) {
        super(app, new OperationExportReport(targetFormat, baseReportName, targetFilePath, parametersMap));
    }
}
