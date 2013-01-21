/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.base.core.presentation.validation.IValidationStatusReceiver;
import org.base.core.presentation.validation.IValidator;

/**
 *
 * @author martin
 */
public abstract class AbstractInputVerifier extends InputVerifier implements IValidator {
    
    private IValidationStatusReceiver receiver = null;
    private JTextComponent component;
    private Color color;
    
    public static Color warningColor = Color.YELLOW;
    
    //makes the verifyer transfer focus on failure (or not)
    private boolean transferFocus = true;
    
    private String message = "ERROR";//default
	
    protected AbstractInputVerifier(JTextComponent c) {
        component = c;
        color = component.getBackground();
        component.setInputVerifier(this);
        
        addDetectorsToClearWarning();
    }
    
    protected AbstractInputVerifier(JTextComponent c, boolean transferFocus) {
        this(c);
        this.transferFocus = transferFocus;
    }
	
    public AbstractInputVerifier (JTextComponent c, IValidationStatusReceiver receiver, boolean transferFocus) {		
        this(c, transferFocus);
        this.receiver = receiver;
    }
    
    private void addDetectorsToClearWarning() {
        //Clear warning when the content of this component changes (whether physically or programatically)
        if(component.getDocument() != null) {
            component.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    clearWarning();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {}
                @Override
                public void changedUpdate(DocumentEvent e) {}
            });
        }
        
        //Clear warning when the component gains focus
        component.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                clearWarning();
            }
            @Override
            public void focusLost(FocusEvent e) {}
        });
        
        //Clear warning whenever user presses a key
        //NOTE: this listener isn't important when we allow the component to transfer focus at a
        //validation failure, since when focus is gained again the warning is cleared. But it is important
        //when we don't transfer focus on a validation failure, because we want that when the user starts
        //typing again the warnig gets cleared.
        component.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                clearWarning();
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
	
    @Override
    public boolean verify(JComponent c) {
        component = (JTextComponent)c;
        if (!executeValidation()) {
			
            if(receiver != null) receiver.validateFailed(getValidationMessage());
			
            showWarning(component);
            return transferFocus;//pass depends on this variable
        }
        
        clearWarning();
        if(receiver != null) receiver.validatePassed();
        
        return true;//pass
    }
    
    @Override
    public boolean executeValidation() {
        return passesValidationCriteria(component);
    }
    
    @Override
    public String getValidationMessage() {
        return message;
    }
    
    protected abstract boolean passesValidationCriteria(JComponent c);
    
    protected void showWarning(JComponent c) {
        c.setBackground(warningColor);
        c.setToolTipText(message);
    }
	
    protected void setMessage(String message) {
        this.message = message;
    }
    
    private void clearWarning() {
        component.setBackground(color);
        component.setToolTipText("");
    }
}
