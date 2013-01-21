package org.base.core.presentation.validation.components.constraints;

import javax.swing.text.*;

public class DocumentTextFieldLimit extends PlainDocument {
    private int limit;
    
    //optional uppercase conversion
    private boolean toUppercase = false;

    public DocumentTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    public DocumentTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
        toUppercase = upper;
    }
    
    @Override
    public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {
            if (toUppercase) str = str.toUpperCase();
            super.insertString(offset, str, attr);
        }
    }
}
