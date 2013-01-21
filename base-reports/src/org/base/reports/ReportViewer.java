/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.reports;

import java.awt.Container;
import javax.swing.JDialog;
import javax.swing.JFrame;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author mproenza
 */
public class ReportViewer {
    
    public static void viewReport(Report report, String title) {
        JDialog dialogoReporte = (JDialog) loadInContainer(new JDialog(), report);

        dialogoReporte.setPreferredSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
        dialogoReporte.setModal(true);
        dialogoReporte.setTitle(title);
        dialogoReporte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialogoReporte.pack();
        dialogoReporte.setLocationRelativeTo(null);
        dialogoReporte.setVisible(true);
    }
    
    public static Container loadInContainer(Container container, Report report) {
        if(container == null) container = new JDialog();
        
        JRViewer viewer = report.getViewer();
        container.add(viewer);
        
        return container;
    }
}
