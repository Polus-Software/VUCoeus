/*
 * @(#)JTextFieldFilter.java 1.0 8/21/02 5:37 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import javax.swing.text.*;

/**
 * This class allows the caller to restrict what the user can type into
 * a text field. You can instantiate this class with an arbitrary string
 * of characters and the class will restrict the text field to only those
 * characters. If the valid set of characters contains upper case alphabetics
 * but no lower case alphabetics then the user's input will be coerced to
 * upper case, and vice versa.
 *
 * The class provides some convenient, predefined sets of allowed characters
 * such as LOWERCASE, UPPERCASE, ALPHA, NUMERIC, and ALPHA_NUMERIC, but there
 * is nothing special about these strings and as the caller you can restrict input
 * to any set of characters you like.
 *
 * @author  mukund
 *
 */

public class JTextFieldFilter extends PlainDocument {
    /** Restricts the user to entering only letters of the alphabet, all of which will be coerced to lower case */
    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    /** Restricts the user to entering only letters of the alphabet, all of which will be coerced to upper case */
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** Restricts the user to entering only letters of the alphabet */
    public static final String ALPHA = LOWERCASE + UPPERCASE;

    /** Restricts the user to entering only digits */
    public static final String NUMERIC = "0123456789";

    /** Restricts the user to entering only letters of the alphabet or digits */
    public static final String ALPHA_NUMERIC = ALPHA + NUMERIC;


    /** User input should not be coerced */
    protected static final int NO_FORCED_CASE = 0;

    /** User input should be coerced to upper case */
    protected static final int FORCE_UPPERCASE = 1;

    /** User input should be coerced to lower case */
    protected static final int FORCE_LOWERCASE = 2;


    /** The list of characters accepted by the text field */
    protected String acceptedChars = null;

    /** One of NO_FORCED_CASE, FORCE_UPPERCASE, or FORCE_LOWERCASE, indicates how user input should be coerced */
    protected int forceCase = JTextFieldFilter.NO_FORCED_CASE;
    
    //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start
    public static final String COMMA = ",";
    public static final String HYPHEN  = "-";
    public static final String PERIOD  = ".";
    public static final String COMMA_HYPHEN_PERIOD = COMMA + HYPHEN + PERIOD;
    //Case#2402 - End
    
    /**
     * When the user adds a string of more than one character to the field, and
     * the input string contains illegal characters, should the entire string be
     * suppressed (<code>true</code>), or should the illegal characters be removed
     * and the legal ones added to the field (<code>false</code>)?
     */
    protected boolean allOrNothing = false;


    private int maxLength;

    /**
     * Creates a text field filter that allows alpha/numeric entry and filters illegal characters
     * from input.
     */
    public JTextFieldFilter() {
        this(JTextFieldFilter.ALPHA_NUMERIC, false);
    }

    /**
     * Creates a text field filter that filters illegal characters from input.
     *
     * @param acceptedChars the characters that this filter will not block. If all of the alphabetic
     *           characters in this string are upper case then all user input will be coerced to upper
     *           case; similarly, if all alphabetic characters in this string are lower case then all
     *           user input will be coerced to lower case; if the string contains mixed case alphabetic
     *           then the user's input will not be coerced.
     */
    public JTextFieldFilter(String acceptedChars) {
        this(acceptedChars, false);
    }
    /**
     * Creates a text field filter that filters illegal characters from input.
     *
     * @param acceptedChars the characters that this filter will not block. If all of the alphabetic
     *           characters in this string are upper case then all user input will be coerced to upper
     *           case; similarly, if all alphabetic characters in this string are lower case then all
     *           user input will be coerced to lower case; if the string contains mixed case alphabetic
     *           then the user's input will not be coerced.
     * @param maxLength maximum length of this editing component.
     */
    public JTextFieldFilter(String acceptedChars, int maxLength) {
        this(acceptedChars, false);
        this.maxLength = maxLength;
    }

    /**
     * Creates a text field filter.
     *
     * @param acceptedChars the characters that this filter will not block. If all of the alphabetic
     *           characters in this string are upper case then all user input will be coerced to upper
     *           case; similarly, if all alphabetic characters in this string are lower case then all
     *           user input will be coerced to lower case; if the string contains mixed case alphabetic
     *           then the user's input will not be coerced.
     * @param allOrNothing if <code>true</code> then any strings entered by the user that contain illegal
     *           characters will be completely suppressed; if <code>false</code> then when the user enters
     *           a string containing illegal characters the legal characters from the string will be
     *           added and the illegal ones suppressed.
     */
    public JTextFieldFilter(String acceptedChars, boolean allOrNothing) {
        this.acceptedChars = acceptedChars;
        this.allOrNothing = allOrNothing;
        if (JTextFieldFilter.stringContains(this.acceptedChars,
                JTextFieldFilter.UPPERCASE, JTextFieldFilter.LOWERCASE)) {
            this.forceCase = JTextFieldFilter.FORCE_UPPERCASE;
        } else if (JTextFieldFilter.stringContains(this.acceptedChars,
                JTextFieldFilter.LOWERCASE, JTextFieldFilter.UPPERCASE)) {
            this.forceCase = JTextFieldFilter.FORCE_LOWERCASE;
        }
    }

    /**
     * Adds more characters to the list of characters that the text field will accept.
     *
     * @param moreAcceptedChars the characters to add to those that this filter will not block.
     *           If all of the alphabetic
     *           characters in the resulting list are upper case then all user input will be coerced to upper
     *           case; similarly, if all alphabetic characters in the resulting list are lower case then all
     *           user input will be coerced to lower case; if the resulting list contains mixed case alphabetic
     *           then the user's input will not be coerced.
     */
    public void AddAcceptedCharacters(String moreAcceptedChars) {
        this.acceptedChars += JTextFieldFilter.stringSubtraction(moreAcceptedChars, this.acceptedChars);
        if (JTextFieldFilter.stringContains(this.acceptedChars,
                JTextFieldFilter.UPPERCASE, JTextFieldFilter.LOWERCASE)) {
            this.forceCase = JTextFieldFilter.FORCE_UPPERCASE;
        } else if (JTextFieldFilter.stringContains(this.acceptedChars,
                JTextFieldFilter.LOWERCASE, JTextFieldFilter.UPPERCASE)) {
            this.forceCase = JTextFieldFilter.FORCE_LOWERCASE;
        }
    }

    /**
     * Filters content before inserting it into the document. Filtering occurs according to the instructions
     * given to this class's constructor.
     *
     * @param offset the starting offset >= 0
     * @param str the string to insert; does nothing with null / empty strings
     * @param attr the attributes for the inserted content
     *
     * @throws BadLocationException the given insertion point is not a valid position within the document
     */
    public void insertString(int offset, String str, AttributeSet attr)
            throws BadLocationException {
        if (str == null) return;

        str = correctForCase(str);
        if (getLength() + str.length() <= maxLength) {
            if (this.allOrNothing) {
                if (validContents(str)) {
                        super.insertString(offset, str, attr);
                }
            } else {
                String filteredString = filterContents(str);
                super.insertString(offset, filteredString, attr);
            }
        }
    }

    /**
     * Coerces the string to upper or lower case, or leaves it alone, depending upon the
     * valid character set with which this text filter was instantiated. If the text filter
     * was instantiated with a character set containing upper case characters but no lower
     * case characters then this method will coerce the string to upper case. If the text filter
     * was instantiated with a character set containing lower case characters but no upper
     * case characters then this method will coerce the string to lower case. Otherwise this
     * method will return the string as it was passed.
     *
     * @param str the string to coerce
     * @return String modified focus value
     */
    protected String correctForCase(String str) {
        if (this.forceCase == JTextFieldFilter.FORCE_UPPERCASE) {
            return str.toUpperCase();
        } else if (this.forceCase == JTextFieldFilter.FORCE_LOWERCASE) {
            return str.toLowerCase();
        } else {
            return str;
        }
    }

    /**
     * Returns an indication of whether a string contains only valid characters. It is assumed that
     * the string has already been properly coerced by a call to {@link #correctForCase}.
     *
     * @param str the string to check against the set of valid input characters
     *
     * @return <code>true</code> if the string contains only valid input characters;<BR>
     *         <code>false</code> if the string contains one or more invalid characters
     */
    protected boolean validContents(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (this.acceptedChars.indexOf(str.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Filter a string so that it contains only valid characters. It is assumed that
     * the string has already been properly coerced by a call to {@link #correctForCase}.
     *
     * @param str the string from which to remove invalid characters
     *
     * @return the contents of <code>str</code> with the invalid characters removed
     */
    protected String filterContents(String str) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char charToCheck = str.charAt(i);
            if (this.acceptedChars.indexOf(charToCheck) != -1) {
                result.append(charToCheck);
            }
        }
        return result.toString();
    }

    /**
     * Returns the intersection of two sets of characters, each represented as a string.
     *
     * @param string1 the first set of characters
     * @param string2 the second set of characters
     *
     * @return a string containing all characters that are in both strings
     */
    public static String stringIntersection(String string1, String string2) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < string1.length(); i++) {
            char charToCheck = string1.charAt(i);

            if (string2.indexOf(charToCheck) != -1) {
                result.append(charToCheck);
            }
        }
        return result.toString();
    }

    /**
     * Returns the difference between two sets of characters, each represented as a string.
     *
     * @param mainString the set of characters from which to subtract <code>lessString</code>
     * @param lessString the set of characters to subtract from <code>mainString</code>
     *
     * @return a string containing all characters that are in mainString but not in lessString
     */
    public static String stringSubtraction(String mainString, String lessString) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < mainString.length(); i++) {
            char charToCheck = mainString.charAt(i);

            if (lessString.indexOf(charToCheck) == -1) {
                result.append(charToCheck);
            }
        }
        return result.toString();
    }

    /**
     * Returns an indication of whether the string being checked contains some
     * characters but not other characters.
     *
     * @param stringToCheck the string to examine
     * @param containsCharacters at least one of these characters must be in <code>stringToCheck</code>
     *        for a return value of <code>true</code>
     * @param doesNotContainCharacters if <code>stringToCheck</code> contains any one of these
     *        characters then the function will return <code>false</code>
     *
     * @return <code>true</code> if <code>stringToCheck</code> contains some characters that
     *         are in <code>containsCharacters</code> but no characters from
     *         <code>doesNotContainCharacters</code>;<BR>
     *         <code>false</code> if <code>stringToCheck</code> does not contain any characters
     *         from <code>containsCharacters</code>, or contains one or more characters from
     *         <code>doesNotContainCharacters</code>
     */
    public static boolean stringContains(String stringToCheck, String containsCharacters,
                                         String doesNotContainCharacters) {
        return !JTextFieldFilter.stringIntersection(stringToCheck, containsCharacters).equals("") &&
                JTextFieldFilter.stringIntersection(stringToCheck, doesNotContainCharacters).equals("");
    }

    /**
     * This method fetches the Maximum length of the field
     * @return int max length
     */
    public final int getMaxLength() {
        return maxLength;
    }

    /**
     * This method set the Maximum length of the field
     * @param maxLength int max length
     */
    public final void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}