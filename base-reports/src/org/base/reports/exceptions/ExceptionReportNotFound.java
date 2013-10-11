/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.exceptions;

import org.base.utils.messages.MessageFactory;

/**
 *
 * @author mproenza
 */
public class ExceptionReportNotFound extends Exception {
    
    public ExceptionReportNotFound() {
        super(MessageFactory.getMessage("msg_report_not_found"));
    }
}
