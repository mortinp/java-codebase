/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples.better;

import javax.swing.JComponent;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author martin
 */
public abstract class AbstractComponentValidator implements IValidator {
    
    JComponent component;
    
    //the message to show
    protected String message = null;

    public void injectComponent(JComponent component) {
        this.component = component;
    }

    @Override
    public boolean executeValidation() {
        return validateComponent(component);
    }

    @Override
    public String getValidationMessage() {
        return message;
    }
    
    protected abstract boolean validateComponent(JComponent c);
    
}
