/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports;

import java.io.IOException;
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
import org.base.dao.datasources.context.RegistryDataSourceContext;
import org.base.reports.exceptions.ExceptionReporting;

/**
 *
 * @author martin
 */
public class Report {
    public static final String EXPORT_FORMAT_XLS = "FORMAT_XLS";
    public static final String EXPORT_FORMAT_PDF = "FORMAT_PDF";
    public static final String EXPORT_FORMAT_HTML = "FORMAT_HTML";

    private String reportFileName;
    private Map<String, Object> parametersMap = new HashMap<String, Object>();
    
    private static Properties propertiesFile = null;
    private static final String DEFAULT_CONFIG_FILE_PATH = "/META-INF/reports_config.properties";
    
    static DataSourceContext dataSourceContext;
    
    boolean emptyDataSource = false; // Use an empty data source, normally to generate a template report (a report with no data).    
    
    public Report(String name)  {
        this.reportFileName = name;
    }
    
    public Report() {
        this("");
    }
    
    public boolean exists() {
        String reportPath = getReportPath();
        InputStream f = Report.class.getResourceAsStream(reportPath);
        return f != null;
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
            throw new ExceptionReporting(ex);
        } finally {
            getDatasourceContext().close(conn);
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
            } else throw new ExceptionReporting("The format specified was not recognized.");
            
        } catch (JRException ex) {
            throw new ExceptionReporting(ex);
        } finally {
            getDatasourceContext().close(conn);
        }
    }
    
    private JasperPrint fillReport(Connection conn) throws JRException {
        parametersMap.put("SUBREPORT_DIR", getReportBaseFolder() + "/");//assumes that subreport is in the same path as the report
        parametersMap.put("schema_name", getDefaultSchemaName());
        parametersMap.put(JRParameter.REPORT_LOCALE, getDefaultLocale());
        InputStream io = getClass().getResourceAsStream(getReportPath());
        JasperReport report = (JasperReport) JRLoader.loadObject(io);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, this.parametersMap, conn);
        return jasperPrint;
    }
    
    private JasperPrint fillReport(JRDataSource ds) throws JRException {
        parametersMap.put("SUBREPORT_DIR", getReportBaseFolder() + "/");//assumes that subreport is in the same path as the report
        parametersMap.put("schema_name", getDefaultSchemaName());
        parametersMap.put(JRParameter.REPORT_LOCALE, getDefaultLocale());
        InputStream io = getClass().getResourceAsStream(getReportPath());
        JasperReport report = (JasperReport) JRLoader.loadObject(io);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report, this.parametersMap, ds);
        return jasperPrint;
    }
    
    private String getReportBaseFolder() {
        return getProperties().getProperty("reports.base.directory");
    }
    
    private String getDefaultSchemaName() {
        return getProperties().getProperty("reports.default.schema.name");
    }
    
    private Locale getDefaultLocale() {
        String defaultLanguage = getProperties().getProperty("reports.default.language");
        String defaultVariation = getProperties().getProperty("reports.default.language.variation");
        return new Locale(defaultLanguage, defaultVariation);
    }
    
    public static void setProperties(Properties properties) {
        propertiesFile = properties;
        
        // Load data source context
        //String defaultDatasourceContext = propertiesFile.getProperty("reports.default.datasource.context");
        //dataSourceContext = RegistryDataSourceContext.getDataSourceContext(defaultDatasourceContext);
    }
    
    private Connection getConnection() {
        return getDatasourceContext().getConnection();
    }

    public String getName() {
        return reportFileName;
    }
    
    public String getReportPath() {
        String temp = this.reportFileName;
        if(!this.reportFileName.endsWith(".jasper"))
            temp += ".jasper";
        return "/" + getReportBaseFolder() + "/" + temp;
    }

    public void setReportFileName(String name) {
        this.reportFileName = name;
    }
    
    private Properties getProperties() {
        if (propertiesFile == null) {
            loadDefaultProperties();
        }
        return propertiesFile;
    }
    
    private void loadDefaultProperties() {
        try {
            InputStream objArchivo = Report.class.getResourceAsStream(DEFAULT_CONFIG_FILE_PATH);
            Properties properties = new Properties();
            properties.load(objArchivo);
            setProperties(properties);
        } catch (IOException ex) {
            throw new ExceptionReporting(ex);
        }
    }
    
    private DataSourceContext getDatasourceContext() {
        if(dataSourceContext == null) {
            String defaultDatasourceContext = getProperties().getProperty("reports.default.datasource.context");
            dataSourceContext = RegistryDataSourceContext.getDataSourceContext(defaultDatasourceContext);
        }
        return dataSourceContext;
    }
}
