/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components;

import javax.swing.text.JTextComponent;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author martin
 */
public class ComponentsValidatorsManager {
    
    /**
     * This function was created only to provide a syntactic sugar to bind multiple validators to one component.
     */
    public static IValidator bindValidators(JTextComponent component,  IValidator ... validators) {
        IValidator v = new ValidatorMultipleInputVerifications(
                component,
                validators,
                true);
        return v;
    }
    
    public static IValidator bindValidators(JTextComponent component, boolean allow, IValidator ... validators) {
        IValidator v = new ValidatorMultipleInputVerifications(
                component,
                validators,
                allow);
        return v;
    }
}
