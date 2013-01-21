/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation;

/**
 *
 * @author Martin
 */
public interface IValidator {
    public boolean executeValidation();
    public String getValidationMessage();
}
