/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports.operations;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.base.reports.Report;
import org.base.reports.exceptions.ExceptionReportNotFound;

/**
 *
 * @author mproenza
 */
public abstract class AbstractReportingOperation implements IReportingOperation {
    
    protected String strNombreReporte;
    protected Map<String, Object> mapParametros = new HashMap<String, Object>();
    
    public AbstractReportingOperation(String strNombreReporte) {
        this.strNombreReporte = strNombreReporte;
    }
    
    public AbstractReportingOperation(String strNombreReporte, Map<String, Object> mapParametros) {
        this(strNombreReporte);
        this.mapParametros = mapParametros;
    }

    @Override
    public Report execute() {
        Report rep = new Report(); 
        rep.setReportFileName(strNombreReporte + ".jasper");
        
        if(mapParametros!= null && !mapParametros.isEmpty())
            rep.setParameters(mapParametros);
        
        doStuff(rep);
        
        return rep;
    }
    
    public abstract void doStuff(Report rep);
    
}
