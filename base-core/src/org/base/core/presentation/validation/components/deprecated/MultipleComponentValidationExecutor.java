/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.deprecated;

import java.util.ArrayList;
import java.util.List;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author Martin
 */
public class MultipleComponentValidationExecutor implements IValidator {
    private List<ComponentValidationGateway> validators = new ArrayList<ComponentValidationGateway>();
    String message = null;
    
    public void addValidator(ComponentValidationGateway objValidator) {
        validators.add(objValidator);
    }
    
    @Override
    public boolean executeValidation() {
        boolean OK = true;
        for (ComponentValidationGateway validator : validators) {
            OK = validator.executeValidation();
            if(!OK) {                
                message = validator.getValidationMessage();
                break;
            }
        }
        return OK;
    }

    @Override
    public String getValidationMessage() {
        return this.message;
    }
}
