/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation;

import javax.swing.JComponent;

/**
 *
 * @author mproenza
 */
public interface IComponentValidationStatusReceiver {
    
    void validationFailed(JComponent component, String failureMessage);
    void validationPassed();
    
}
