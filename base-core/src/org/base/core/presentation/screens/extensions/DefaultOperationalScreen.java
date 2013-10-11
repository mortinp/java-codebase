/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.screens.extensions;

import org.base.core.presentation.screens.ScreenBase;
import org.base.core.presentation.validation.IValidationStatusReceiver;

/**
 *
 * @author martin
 */
public abstract class DefaultOperationalScreen extends ScreenBase implements IValidationStatusReceiver {
    
    public boolean validationPassed = true;
    
    public DefaultOperationalScreen() {
        super();
    }
    
    public DefaultOperationalScreen(String title) {
        super(title);
    }
    
    @Override
    public void validateFailed(String failureMessage) {
        showWarningMessage(failureMessage);
        validationPassed = false;
    }

    @Override
    public void validatePassed() {
        validationPassed = true;
    }
    
    public void accept() {
        if(validationPassed) {
            doAccept();
        }
    }
    
    protected abstract void doAccept();
    
}
