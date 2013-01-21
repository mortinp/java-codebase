/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components;

import org.base.core.presentation.validation.IComponentValidator;
import javax.swing.JComponent;
import org.base.core.presentation.validation.IComponentValidationStatusReceiver;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author Martin
 */
public class ComponentValidationGateway implements IValidator {
    
    JComponent component;
    IComponentValidator validator;
    IComponentValidationStatusReceiver validationStatusReceiver = null;

    public ComponentValidationGateway(JComponent component, IComponentValidator validator) {
        this.component = component;
        this.validator = validator;
    }
    
    public ComponentValidationGateway(JComponent component, IComponentValidator validator, IComponentValidationStatusReceiver validationStatusReceiver) {
        this(component, validator);
        this.validationStatusReceiver = validationStatusReceiver;
    }
    
    @Override
    public boolean executeValidation() {
        boolean OK = validator.validateComponent(component);
        if(validationStatusReceiver != null) {
            if(OK) validationStatusReceiver.validationPassed();
            else validationStatusReceiver.validationFailed(component, getValidationMessage());
        }
        
        return OK;
    }
    
    @Override
    public final String getValidationMessage() {
        return validator.getValidationMessage();
    }
}
