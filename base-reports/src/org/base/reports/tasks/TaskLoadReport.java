/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.tasks;

import org.base.reports.operations.OperationLoadReport;
import java.awt.Container;
import java.util.Map;
import org.jdesktop.application.Application;

/**
 *
 * @author martin
 */
public class TaskLoadReport extends TaskExecuteOperation {

    public TaskLoadReport(Application app, Container container, String nombreReporte) {
        super(app, new OperationLoadReport(container, nombreReporte));
    }
    
    public TaskLoadReport( Application app, Container container, String strNombreReporte, Map<String, Object> mapParametros) {
        super(app, new OperationLoadReport(container, strNombreReporte, mapParametros));
    }
}
