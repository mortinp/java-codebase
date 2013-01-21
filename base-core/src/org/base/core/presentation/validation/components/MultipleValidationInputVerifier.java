/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components;

import org.base.core.presentation.validation.IComponentValidator;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author martin
 */
public class MultipleValidationInputVerifier extends AbstractInputVerifier {
    
    //the list of validators to be used when validationCriteria() is called
    private List<IComponentValidator> validators = new ArrayList<IComponentValidator>();
	
    private MultipleValidationInputVerifier(JTextComponent c) {
        super(c);
    }
    
    public MultipleValidationInputVerifier(JTextComponent c, boolean transferFocus) {
        super(c, transferFocus);
    }
	
    public void addValidator(IComponentValidator objValidator) {
        validators.add(objValidator);
    }
    
    @Override
    protected boolean passesValidationCriteria(JComponent c) {
        boolean OK = true;
        for (IComponentValidator validator : validators) {
            OK = validator.validateComponent(c);
            if(!OK) {                
                setMessage(validator.getValidationMessage());
                break;
            }
        }
        return OK;
    }
}
