/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation;

import javax.swing.JComponent;

/**
 *
 * @author martin
 */
public interface IComponentValidator {
    
    public boolean validateComponent(JComponent objComponent);
    public String getValidationMessage();
}
