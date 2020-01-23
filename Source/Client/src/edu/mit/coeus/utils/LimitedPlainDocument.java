/*
 * LimitedPlainDocument.java
 *
 * Created on August 26, 2002, 9:56 AM
 */

package edu.mit.coeus.utils;

/**
 *
 * @author  mukund
 * @version
 */
import javax.swing.text.*;

/**
 * This class is used to construct the coeus Limited Palin document by 
 * Extending DefaultStyledDocument
 * @author Geo
 * @version 1.0
 */
public class LimitedPlainDocument extends DefaultStyledDocument {
    
    /** User input should not be coerced */
    public static final int NO_FORCED_CASE = 0;
    
    /** User input should be coerced to upper case */
    public static final int FORCE_UPPERCASE = 1;
    
    /** User input should be coerced to lower case */
    public static final int FORCE_LOWERCASE = 2;
    
    protected int filter = 0;
    
    /**
     * This will construct the coeus Limited Palin document.
     * @param maxLength document max length
     */
    public LimitedPlainDocument(int maxLength) {
        this.maxLength = maxLength;
    }
    
    private int maxLength;
    /**
     * This method fetches the Maximum length of the document
     * @return int max length
     */
    public final int getMaxLength() {
        return maxLength;
    }
    
    /**
     * This method set the Maximum length of the document
     * @param maxLength int max length
     */
    public final void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
    
    /**
     * inserts content into the document. 
     *
     * @param offs the starting offset >= 0
     * @param str the string to insert; does nothing with null / empty strings
     * @param a the attributes for the inserted content
     *
     * @throws BadLocationException the given insertion point is not a valid position within the document
     */
    public void insertString(int offs, String str, AttributeSet a) throws
    BadLocationException {
        if(filter == FORCE_UPPERCASE && str!=null) {
                str = str.toUpperCase();
            }else if(filter == FORCE_LOWERCASE && str!=null) {
                str = str.toLowerCase();
            }
        if (str == null || getLength() + str.length() <= maxLength)
            super.insertString(offs, str, a);
        else {
            int remainder = maxLength - getLength();
            if (remainder > 0)
                super.insertString(offs, str.substring(0, remainder), a);
        }
    }
    public void setFilter(int filter) {
        this.filter = filter;
    }
}
