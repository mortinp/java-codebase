/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.base.core.presentation.validation.IComponentValidator;
import org.base.core.util.messages.MessageFactory;
import org.base.core.util.types.UtilFunciones;
 
/**
 *
 * @author yrodriguez B
 */
public class RangDateValidator implements IComponentValidator {
    
    //the message to show
    private String message = null;
    
    private JComponent objComponentRange = null;
    private int posicionRango = 1;
    private String formatoFecha = null;
    private boolean validaMes = false;
    private boolean validaAnno = false;

    
    public  RangDateValidator(JComponent objComponent)
    {
      this.objComponentRange =  objComponent;
      this.formatoFecha = "dd-MM-yyyy";
    }

    public  RangDateValidator(JComponent objComponent,boolean valMes,boolean valAnno)
    {
      this.objComponentRange =  objComponent;
      this.formatoFecha = "dd-MM-yyyy";      
      this.validaMes = valMes;
      this.validaAnno = valAnno;
    }

    
    public  RangDateValidator(JComponent objComponent,int placeRange)
    {
      this.objComponentRange =  objComponent;
      this.posicionRango = placeRange;
      this.formatoFecha = "dd-MM-yyyy";      
    }

    public  RangDateValidator(JComponent objComponent,int placeRange,boolean valMes,boolean valAnno)
    {
      this.objComponentRange =  objComponent;
      this.posicionRango = placeRange;
      this.formatoFecha = "dd-MM-yyyy";      
      this.validaMes = valMes;
      this.validaAnno = valAnno;
    }

    
    public  RangDateValidator(JComponent objComponent,String formato)
    {
      this.objComponentRange =  objComponent;
      this.formatoFecha = formato;      
    }
    
    public  RangDateValidator(JComponent objComponent,int placeRange,String formato)
    {
      this.objComponentRange =  objComponent;
      this.posicionRango = placeRange;
      this.formatoFecha = formato;      
    }
    
    //should be passed a JTextComponent
    @Override
    public boolean validateComponent(JComponent objComponent) {
        
        if(!validateRang(((JTextComponent)objComponent).getText())) {

            message = MessageFactory.getMessage("msg_rango_fecha_incorrecto", objComponent.getName());
            return false;           
        }
        if (this.validaMes)
        if(!validateRangMes(((JTextComponent)objComponent).getText())) {
            message = MessageFactory.getMessage("msg_rango_fecha_mes_mincorrecto", objComponent.getName());
            return false;           
        }
        
        if (this.validaAnno)
        if(!validateRangAnno(((JTextComponent)objComponent).getText())) {
            message = MessageFactory.getMessage("msg_rango_fecha_anno_mincorrecto", objComponent.getName());
            return false;           
        }

        
        return true;
        
    }
    

    @Override
    public String getValidationMessage() {
        return message;
    }
    
    //valida un rango de fecha
    private  boolean validateRang(String fecha){
        boolean ok = true;
        
        String fechaInterna = ((JTextComponent)this.objComponentRange).getText();
	Date fechaIni = null;
        Date fechaFin = null;
	// formato desde el formulario 
	SimpleDateFormat sdf = new SimpleDateFormat(this.formatoFecha);
	try {
            fechaIni = sdf.parse(fechaInterna);
            fechaFin = sdf.parse(fecha);
            
    	} catch (Exception e) {
          ok = false;
	}        

        if (this.posicionRango == 1)
        {
            if (fechaIni.after(fechaFin)) {
                //La fecha de inicio es > que la fecha fin
                ok = false;
            }
        }
        else
        {
            if (fechaFin.after(fechaIni)) {
                //La fecha de fin es > que la fecha inicio
                ok = false;
            }
            
        }
	return ok;
    }   
    
//valida un rango de fecha este en un mismo mes
    private  boolean validateRangMes(String fecha){
        boolean ok = true;
        
        String fechaInterna = ((JTextComponent)this.objComponentRange).getText();
	Date fechaIni = UtilFunciones.getFecha(fechaInterna);
        Date fechaFin = UtilFunciones.getFecha(fecha);
        
        Calendar cal = GregorianCalendar.getInstance();
        Calendar ahoraCal = Calendar.getInstance();

        ahoraCal.setTime(fechaIni);
        int mesIni = ahoraCal.get(Calendar.MONTH);
        int annoIni = ahoraCal.get(Calendar.YEAR);
        ahoraCal.setTime(fechaFin);
        int mesFin = ahoraCal.get(Calendar.MONTH);
        int annoFin = ahoraCal.get(Calendar.YEAR);
        

        if ( (mesIni != mesFin) || (annoIni != annoFin)) {
                ok = false;
        }
	return ok;
    }      
   
//valida un rango de fecha este en un mismo ano
    private  boolean validateRangAnno(String fecha){
        boolean ok = true;
        
        String fechaInterna = ((JTextComponent)this.objComponentRange).getText();
	Date fechaIni = UtilFunciones.getFecha(fechaInterna);
        Date fechaFin = UtilFunciones.getFecha(fecha);
        
        Calendar cal = GregorianCalendar.getInstance();
        Calendar ahoraCal = Calendar.getInstance();

        ahoraCal.setTime(fechaIni);
        int annoIni = ahoraCal.get(Calendar.YEAR);
        ahoraCal.setTime(fechaFin);
        int annoFin = ahoraCal.get(Calendar.YEAR);
        

        if ( annoIni != annoFin) {
                ok = false;
        }
	return ok;
    }        
}
