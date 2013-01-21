/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.tasks;

import org.base.reports.Report;
import org.base.reports.operations.IReportingOperation;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

/**
 *
 * @author mproenza
 */
public class TaskExecuteOperation extends Task<Report, Void> {
    
    IReportingOperation operation;

    public TaskExecuteOperation(Application app, IReportingOperation operation) {
        super(app);
        this.operation = operation;
    }

    @Override
    protected Report doInBackground() throws Exception {
        return operation.execute();
    }
    
}
