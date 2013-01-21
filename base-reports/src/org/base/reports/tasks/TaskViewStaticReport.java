/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.tasks;

import java.util.Map;
import org.base.reports.operations.OperationViewStaticReport;
import org.jdesktop.application.Application;

/**
 *
 * @author leo
 */
public class TaskViewStaticReport extends TaskExecuteOperation {
    
    public TaskViewStaticReport(Application app, String strNombreReporte, String strTitulo) {
        super(app, new OperationViewStaticReport(strNombreReporte, strTitulo));
    }
    
    public TaskViewStaticReport(Application app, String strNombreReporte, String strTitulo, Map<String, Object> mapParametros) {
        super(app, new OperationViewStaticReport(strNombreReporte, strTitulo, mapParametros));
    }
    
    public TaskViewStaticReport(Application app, String strNombreReporte) {
        this(app, strNombreReporte, "No Title");
    }
    
    public TaskViewStaticReport(Application app, String strNombreReporte, Map<String, Object> mapParametros) {
        this(app, strNombreReporte, strNombreReporte, mapParametros);
    }
}
