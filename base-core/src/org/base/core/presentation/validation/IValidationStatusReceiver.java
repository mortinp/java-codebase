/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation;

/**
 *
 * @author martin
 */
public interface IValidationStatusReceiver {
    
    void validateFailed(String failureMessage);
    void validatePassed();
}
