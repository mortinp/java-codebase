/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.samples.better;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.base.core.presentation.validation.IValidator;
import org.base.core.presentation.validation.components.AbstractInputVerifier;
import org.base.exceptions.system.SystemException;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.containers.TransientPicoContainer;
import org.picocontainer.injectors.Reinjector;

/**
 *
 * @author martin
 */
public class ValidatorMultipleInputVerifications extends AbstractInputVerifier {

    //the list of validators to be used when validationCriteria() is called
    private List<IValidator> validators = new ArrayList<IValidator>();
    
    MutablePicoContainer pico = new TransientPicoContainer();
    
	
    private ValidatorMultipleInputVerifications(JTextComponent c) {
        super(c);
        pico.addComponent(c);
    }
    
    public ValidatorMultipleInputVerifications(JTextComponent c, boolean transferFocus) {
        super(c, transferFocus);
        pico.addComponent(c);
    }
    
    public ValidatorMultipleInputVerifications(JTextComponent c, IValidator [] validators) {
        this(c);
        for (IValidator v : validators) {
            addvalidator(v);
        }
    }
    
    public ValidatorMultipleInputVerifications(JTextComponent c, IValidator [] validators, boolean transferFocus) {
        this(c, transferFocus);
        for (IValidator v : validators) {
            addvalidator(v);
        }
    }
	
    public final void addvalidator(IValidator validator) {
        try {
            validators.add(validator);
            
            pico.addComponent(validator.getClass(), validator);
            Method method = validator.getClass().getMethod("injectComponent", JComponent.class);
            new Reinjector(pico).reinject(validator.getClass(), method);
        } catch (NoSuchMethodException ex) {
            throw new SystemException(ex);
        } catch (SecurityException ex) {
            throw new SystemException(ex);
        }
    }
    
    @Override
    protected boolean passesValidationCriteria(JComponent c) {
        boolean OK = true;
        for (IValidator validator : validators) {
            IValidator injectedValidator = (IValidator)pico.getComponent(validator.getClass());
            OK = injectedValidator.executeValidation();
            if(!OK) {                
                setMessage(injectedValidator.getValidationMessage());
                break;
            }
        }
        return OK;
    }
        
}
