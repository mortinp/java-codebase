/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.base.core.presentation.validation.components.constraints;

import javax.swing.text.*;

/**
 *
 * @author martin
 */
public class DocumentDecimalFieldLimit extends DocumentTextFieldLimit {
    
    public DocumentDecimalFieldLimit(int limit) {
        super(limit);
    }
    
    @Override
    public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {        
        if (str == null) return;
        
        int strl = str.length();
        for(int i=0;i<strl;i++) {
            if(!Character.isDigit(str.charAt(i)) || str.charAt(i) != '.') {
                return;
            }
        }
        super.insertString(offset, str, attr);
    }
    
}
