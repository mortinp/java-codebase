/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import org.base.dao.datasources.context.DataSourceContext;
import org.base.dao.datasources.context.DataSourceContextRegistry;
import org.base.exceptions.system.SystemException;

/**
 *
 * @author martin
 */
public class Report {
    public static final String EXPORT_FORMAT_XLS = "FORMAT_XLS";
    public static final String EXPORT_FORMAT_PDF = "FORMAT_PDF";
    public static final String EXPORT_FORMAT_HTML = "FORMAT_HTML";

    private String reportFileName;
    private Map<String, Object> parametersMap;
    
    private static Properties propertiesFile = null;
    
    static DataSourceContext dataSourceContext;
    
    boolean emptyDataSource = false; // use an empty data source, normally to generate a template report.    
    
    public Report(String name)  {
        this.reportFileName = name;
        this.parametersMap = new HashMap<String, Object>();
    }
    
    public Report() {
        this("");
    }
    
    public boolean isEmptyDataSource() {
        return emptyDataSource;
    }

    public void setEmptyDataSource(boolean emptyDataSource) {
        this.emptyDataSource = emptyDataSource;
    }

    public void addParameter(String strNombre, Object objValor) {
        this.parametersMap.put(strNombre, objValor);
    }
    
    public void setParameters(Map<String, Object> mapParams) {
        this.parametersMap = mapParams;
    }

    public JRViewer getViewer() {
        JRViewer jviewer = null;
        Connection conn = getConnection();
        try {
            JasperPrint jasperPrint = null;
            if(isEmptyDataSource()) jasperPrint = fillReport(new JREmptyDataSource());
            else jasperPrint = fillReport(conn);
            jviewer = new JRViewer(jasperPrint);
        } catch (JRException ex) {
            throw new SystemException(ex);
        } finally {
            dataSourceContext.close(conn);
        }
        return jviewer;
    }
    
    public void exportAs(String path, String format) {
        Connection conn = getConnection();
        try {
            JasperPrint jasperPrint = fillReport(conn);
            if(format.equals(EXPORT_FORMAT_XLS)) {
                JExcelApiExporter xlsExporter = new JExcelApiExporter();
                xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                if (!path.endsWith(".xls"))
                    path += ".xls";
                xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path);
                xlsExporter.exportReport();
            } else if(format.equals(EXPORT_FORMAT_PDF)) {
                if (!path.endsWith(".pdf"))
                    path += ".pdf";
                JasperExportManager.exportReportToPdfFile(jasperPrint, path);
            } else if(format.equals(EXPORT_FORMAT_HTML)) {
                if (!path.endsWith(".html"))
                    path += ".html";
                JasperExportManager.exportReportToHtmlFile(jasperPrint, path);
            } else throw new SystemException("The format specified was not recognized.");
            
        } catch (JRException ex) {
            throw new SystemException(ex);
        } finally {
            dataSourceContext.close(conn);
        }
    }
    
    private JasperPrint fillReport(Connection conn) throws JRException {
        parametersMap.put("SUBREPORT_DIR", getReportBaseFolder() + "/");//assumes that subreport is in the same path as the report
        parametersMap.put(JRParameter.REPORT_LOCALE, getDefaultLocale());
        InputStream io = getClass().getResourceAsStream("/" + getReportBaseFolder() + "/" + this.reportFileName);
        JasperReport report = (JasperReport) JRLoader.loadObject(io);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, this.parametersMap, conn);
        return jasperPrint;
    }
    
    private JasperPrint fillReport(JRDataSource ds) throws JRException {
        parametersMap.put("SUBREPORT_DIR", getReportBaseFolder() + "/");//assumes that subreport is in the same path as the report
        parametersMap.put(JRParameter.REPORT_LOCALE, getDefaultLocale());
        InputStream io = getClass().getResourceAsStream("/" + getReportBaseFolder() + "/" + this.reportFileName);
        JasperReport report = (JasperReport) JRLoader.loadObject(io);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, this.parametersMap, ds);
        return jasperPrint;
    }
    
    private String getReportBaseFolder() {
        if(propertiesFile == null) throw new SystemException("Reports base directory not configured yet.");
        return propertiesFile.getProperty("reports.base.directory");
    }
    
    private Locale getDefaultLocale() {
        if(propertiesFile == null) throw new SystemException("Reports default language not configured yet.");
        String defaultLanguage = propertiesFile.getProperty("reports.default.language");
        String defaultVariation = propertiesFile.getProperty("reports.default.language.variation");
        return new Locale(defaultLanguage, defaultVariation);
    }
    
    public static void setProperties(Properties properties) {
        propertiesFile = properties;
        
        // Load data source context
        String defaultDatasourceContext = propertiesFile.getProperty("reports.default.datasource.context");
        dataSourceContext = DataSourceContextRegistry.getDataSourceContext(defaultDatasourceContext);
    }
    
    private Connection getConnection() {
        return dataSourceContext.getConnection();
    }

    public String getName() {
        return reportFileName;
    }

    public void setReportFileName(String name) {
        this.reportFileName = name;
    }
}
