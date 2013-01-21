/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.tasks;

import org.base.reports.operations.OperationViewReport;
import java.util.Map;
import org.jdesktop.application.Application;

/**
 *
 * @author leo
 */
public class TaskViewReport extends TaskExecuteOperation {
    
    public TaskViewReport(Application app, String strNombreReporte, String strTitulo) {
        super(app, new OperationViewReport(strNombreReporte, strTitulo));
    }
    
    public TaskViewReport(Application app, String strNombreReporte, String strTitulo, Map<String, Object> mapParametros) {
        super(app, new OperationViewReport(strNombreReporte, strTitulo, mapParametros));
    }
    
    public TaskViewReport(Application app, String strNombreReporte) {
        this(app, strNombreReporte, "No Title");
    }
    
    public TaskViewReport(Application app, String strNombreReporte, Map<String, Object> mapParametros) {
        this(app, strNombreReporte, strNombreReporte, mapParametros);
    }
}
