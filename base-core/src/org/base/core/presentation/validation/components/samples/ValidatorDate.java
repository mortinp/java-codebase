/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples;

import org.base.core.presentation.validation.components.samples.AbstractComponentValidator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.base.core.util.messages.MessageFactory;

/**
 *
 * @author yrodriguez
 */
public class ValidatorDate extends AbstractComponentValidator {
    //formato por defecto del campo fecha    
    private String formatoFecha = "dd-MM-yyyy";
    
    //Variable para validar la fecha no sea mayor que la fecha actual
    private boolean valFechaActualMayor = true;
    
    
    public ValidatorDate() {
        
    }

    public ValidatorDate(String formato) {
        this.formatoFecha = formato;
    }    

    public ValidatorDate(boolean boolFechaActualMayor) {
        this.valFechaActualMayor = boolFechaActualMayor;
    } 
    
    public ValidatorDate(String formato,boolean boolFechaActualMayor) {
        this.formatoFecha = formato;
        this.valFechaActualMayor = boolFechaActualMayor;
    }    

    @Override
    public boolean validateComponent(JComponent objComponent) {
        
        if(!isDate(((JTextComponent)objComponent).getText())) {
            message = MessageFactory.getMessage("msg_campo_formato_fecha_incorrecto", objComponent.getName());
            return false;           
        }
        if (this.valFechaActualMayor)
        {
            if(isDateMTToday(((JTextComponent)objComponent).getText())) {
                message = MessageFactory.getMessage("msg_campo_fecha_mayor_actual", objComponent.getName());
                return false;           
            }
        }   
        return true;
        
    }
    
    //comprueba que un valor sea una fecha formato especificado
    private  boolean isDate(String cadena){
        boolean ok = true;
        Date fecha = null;
        // formato desde el formulario por defecto dd-MM-yyyy
        SimpleDateFormat sdf = new SimpleDateFormat(this.formatoFecha);
        try 
        {
            sdf.setLenient(false);
            fecha = sdf.parse(cadena);
        } catch (Exception e) {
            ok = false;
        }
        return ok;
    }    
    
    //comprueba que un valor de la fecha no sea mayor que la fecha actual
    private  boolean isDateMTToday(String cadena){
        boolean ok = false;
        Date fecha = null;
        // formato desde el formulario por defecto dd-MM-yyyy
        SimpleDateFormat sdf = new SimpleDateFormat(this.formatoFecha);
        try 
        {
            sdf.setLenient(false);
            fecha = sdf.parse(cadena);
        } catch (Exception e) {
            ok = true;
        }
        Date fechActual = null;
        Calendar cal = GregorianCalendar.getInstance();
        fechActual = cal.getTime();
        if (fecha.after(fechActual))
             ok = true;

        return ok;
    }     
}
