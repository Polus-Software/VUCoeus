/*
 * @(#)DollarCurrencyTextField.java 1.0 9/5/03 9:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import javax.swing.text.*;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.ParseException;
import java.awt.Color;



/**
 *  DollarCurrencyTextField is a TextField Component that allows Entry only
 *  in US Currency Format.
 *  When instantiated it will have default entry as : "$.00" and based on the input
 *  it reformats the text entry to US currency format.It takes Negative or
 *  positive Entry based on the boolean parameter as a flag if true it allows negative
 *  entry
 *
 * @author  ranjeeva
 */


public class DollarCurrencyTextField  extends JTextField {

    /** Toolkit instance for System beeping in case of invalid action	*/
    private Toolkit toolkit;

    /** Default String set to TextField when Component is initialised	*/
    private final String PATTERN = "$.00" ;

    /** No of numeric characters only allowed in the Textfield including decimals */
    private int characterLimit = 12;

    /** maximum mantissa allowed in the Textfield */
    private int maxMantissaDigits = 10;

    /** maximum decimal/fraction number allowed in the Textfield */
    private int minFractionDigits = 2;

    /** To verify whether the TextField allow negative Currency Format */
    private boolean isNegativeAllowed;

    /**  aligment propoerty for the TextField */
    private int alignment = JTextField.RIGHT;
    
    private boolean editable;
    
    /**
     * Creates an Instance of Text Field with default currency pattern
     */

    public DollarCurrencyTextField() {
        super();
        initialise();
    }


    /**
     * Creates an Instance of Text Field with columns and alignment as parameter
     *
     * @param columns int number of character mimit allowed to TextField
     *   Thats equals to variable : <code>maxMantissaDigit<code> plus <code>minFractionDigits<code>
     *
     * @param alignment int text alignment value for the TextField
     */

    public DollarCurrencyTextField(int columns,int alignment) {
        super(columns);
        this.characterLimit = columns;
        maxMantissaDigits = columns - 2;
        this.alignment= alignment;
        initialise();
    }

    /**
     * Creates an Instance of Text Field with columns and alignment and
     * boolean parameter for allowing negative Currency value entry
     *
     * @param columns int number of character mimit allowed to TextField
     *   Thats equals to variable : <code>maxMantissaDigit<code> plus <code>minFractionDigits<code>
     *
     * @param alignment int text alignment value for the TextField
     *
     * @param isNegativeAllowed boolean if <code>true</code> allows negative
     *				Currency value entry if <code>false</code>
     *				allows default positive value
     */

    public DollarCurrencyTextField(int columns,int alignment,boolean isNegativeAllowed) {
        this(columns,alignment);
        this.isNegativeAllowed = isNegativeAllowed;
    }

    /**
     * this method returns the value of variable <code> isNegativeAllowed <code>
     * @return boolean <code>true</code> or <code>false</code>
     */

    public boolean isNegativeAllowed() {
        return isNegativeAllowed;
    }

    /**
     * method to initialise the field with the default pattern
     *
     */

    public void initialise() {
        super.setHorizontalAlignment(alignment);
        this.setText(PATTERN);
        int blinkRate = getCaret().getBlinkRate();
        setCaret(new MyCaret());
        getCaret().setBlinkRate(blinkRate);
        addKeyListener(new CustomKeyAdapter());
        
    }
    //Bug fix Bugid:1032 start for the grayed out background in the display mode
    public void setEditable(boolean value) {        
        
        super.setEditable(value);
        editable = value;
        if (value == false){
            setOpaque(false);
        }else{
            setOpaque(true);
        }
        setDisabledTextColor(Color.BLACK);
    }
    
     public void setEnabled(boolean value){
        super.setEnabled(value);
        if(value == false) {
            setOpaque(false);
       }else{
           setOpaque(true);
       }
        setDisabledTextColor(Color.black);
    }
     //Bug fix 1032 end.

    /**
     * To set the Parameter to make the DollarField Allow Negative value
     */

    public void setisNegativeAllowed(boolean isNegativeAllowed) {
        this.isNegativeAllowed = isNegativeAllowed ;
    }

    /**
     * To get the DollarField Allow Negative value flag
     * @return boolean if <code> true <code> negative value entried allowed to Textfiled
     */

    public boolean getisNegativeAllowed() {
        return isNegativeAllowed;
    }

    /**
     * To get the maximun number of mantissa digit for the Textfield
     * @return int maximun mantissa digits for the Textfield
     */

    public int getmaxMantissaDigits() {
        return maxMantissaDigits;
    }

     /**
     * To set the maximun number of mantissa digit for the Textfield
     * @param int maximun mantissa digit allowed for the Textfield
     */

    public void setmaxMantissaDigits(int maxMantissaDigits) {
        this.maxMantissaDigits = maxMantissaDigits;
    }

    /**
     * To get the maximun number of decimal digit possible for the Textfield
     * @return int maximun decimal digit allowed that is 2 decimal places
     */

    public int getmaxFractionDigits() {
        return minFractionDigits;
    }


    /**
     *  This returns the double Value of the TextField.
     *  @return double Value of the TextField as String or null
     */

    public String getValue() {

        String parseString = getText();

        try {

            boolean isNegative = false;
            int parseIndex = parseString.indexOf('-');

            // checking for negative and converting the currency value to positive
            // for parsing to the correct US Currency format.boolean flag is used
            // to check whether it is a negative entry
            // NumberFormat can only parse positive Currency format

            if( parseIndex != -1) {
                parseString = parseString.substring((parseIndex+1),parseString.length());
                isNegative = true;
                parseString="$"+parseString;

            }

            // Getting the currency Number format for parsing the Text Entry
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            // parsing the string to a double value
            double parsedResult = numberFormatter.parse(parseString).doubleValue();

            //if the entry is negative then the new value is converted to negative
            if(isNegative)
                parseString = (new StringBuffer(parsedResult+"")).insert(0, '-').toString();
            else
                parseString = parsedResult+"";

            return parseString;

        }catch(Exception ex) {
            return null;
        }

    }

    /**
     *  Sets the double Value as US Currency Format into TextField
     *  @param currencyValue double value including negative.
     */

    public void setValue(double currencyValue) {

        boolean isNegative = false;
        try {

            // checking for negative and converting the currency value to positive
            // for parsing to the correct US Currency format.boolean flag is used
            // to check whether it is a negative entry
            // NumberFormat can only parse positive Currency format

            String doubleValueString = currencyValue+"";
            int parseIndex = doubleValueString.indexOf('-');

            if(doubleValueString.indexOf('-') != -1) {
                isNegative = true;
                doubleValueString = doubleValueString.substring((parseIndex+1),doubleValueString.length());
            }

            // Getting the currency Number format for parsing the Text Entry
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            // formating the double value to a currency string
            String parsedResultString = numberFormatter.format(Double.parseDouble(doubleValueString));

            //if the entry is negative value is set to textfield as negative
            if(isNegative)
                super.setText((new StringBuffer(parsedResultString)).insert(1, '-').toString());
            else
                super.setText(parsedResultString);

        }catch(Exception ex) {
            super.setText(currencyValue+"");
        }

    }

    //Bug Fix:1411 Start 3
    public void setText(String currencyValue) {
        boolean isNegative = false;
        try {

            // checking for negative and converting the currency value to positive
            // for parsing to the correct US Currency format.boolean flag is used
            // to check whether it is a negative entry
            // NumberFormat can only parse positive Currency format

            String doubleValueString = currencyValue;
            int parseIndex = doubleValueString.indexOf('-');

            if(doubleValueString.indexOf('-') != -1) {
                isNegative = true;
                doubleValueString = doubleValueString.substring((parseIndex+1),doubleValueString.length());
            }

            // Getting the currency Number format for parsing the Text Entry
            NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            // formating the double value to a currency string
            String parsedResultString = numberFormatter.format(Double.parseDouble(doubleValueString));

            //if the entry is negative value is set to textfield as negative
            if(isNegative)
                super.setText((new StringBuffer(parsedResultString)).insert(1, '-').toString());
            else
                super.setText(parsedResultString);

        }catch(Exception ex) {
            super.setText(currencyValue+"");
        }
    }
    //Bug Fix:1411 End 3
    
    /**
     *  Overridding the Default Document Model for TextField
     */

    protected  Document createDefaultModel() {
        return new DollarCurrencyTextFieldDocument();
    }

    /**
     *  The new Document Model for TextField a PlainDocument Model
     */
    protected class DollarCurrencyTextFieldDocument extends PlainDocument {

        /**
         * Filters content before inserting it into the document.
         * Filtering occurs according to logic in this method which only allows numeric,dot
         *
         * @param offs the starting offset >= 0
         * @param str the string to insert; does nothing with null / empty strings
         * @param a the attributes for the inserted content
         *
         * @throws BadLocationException the given insertion point is not a valid position
         *                                within the document
         */

        public void insertString(int offs, String str, AttributeSet a)
        throws BadLocationException {

            String currentText = getText(0, getLength());
            //modified to set initial value as $.00 rather than $0.00
            if(str.length() > 2) {
                
                    //if values are set to field old contents are removed
                    super.remove(0,getLength());
                    // inserted values are validated and converted to US currency format
                    // before insertion
                    str = isValidTextEntry(str);
                    
                    if(str.equals("$0.00"))
                    str = "$.00";
                    
                    //Bug fix:1411 Start 1
                    //Commented since after doing super.remove(0,getLength());
                    //there the offset would be wrong.
//                    super.insertString(offs+1,str,a);
                    super.insertString(0,str,a);
                    //Bug fix:1411 End 1
                    
                    setCaretPosition(1);
                    return;
                
            }

            // getting the initial position
            int dotPos = currentText.indexOf('.');
            int caretPosition = getCaretPosition();
            int numOfcharactersOffset = (getLength() - offs);

            // flag for processing logic for DOT "." entry in mantissa part of the text
            boolean continueProcess = false;

            // checks for the Decimal Position before parsing the content and converting to
            // US dollar Currency Format

            if(dotPos != -1) {

                try {

                    // if CaretPosition has last reached Rightmost position
                    if(offs == dotPos+3 ) {
                        //Toolkit.getDefaultToolkit().beep();
                        return;
                    }

                    // checking zero entry only allowed after a Valid Digit entry before
                    // the text entry is parsed for Double value to avoid invalid character

                    if(offs < 2 && Double.parseDouble(str) == 0)
                        return;
                    else
                        Double.parseDouble(str);

                } catch(Exception excep) {

                    // check for dot entry for Currency  at the offset
                    // if it is then the text after the offset to dot position is removed
                    if(str.compareTo(".") == 0) {
                        super.remove(offs,(getText(0,getLength()).indexOf('.') - offs));

                        // reformatting the Text value to the Currency type

                        reconvertTextFieldValue(a);

                        //setting the caretposition after DOT"." position

                        setCaretPosition(getText(0,getLength()).indexOf('.')+1);
                        continueProcess = true;
                    }

                    // check if negative character entry is done
                    // and allow only when the Components is allowed for such entry
                    // [ An enhancement feature added to component]

                    if(isNegativeAllowed() && (str.compareTo("-") == 0) && offs == 1 ) {

                        // to ensure that only a single "-" negative character is allowed for entry
                        if(getText(0, getLength()).indexOf('-')  == -1) {
                            super.insertString(offs,str,a);
                            setCaretPosition(offs+1);
                            continueProcess = true;
                        } else
                            continueProcess = false;
                    }

                    // preventing more that one "-" negative character entry
                    if(continueProcess == false) {
                        //Toolkit.getDefaultToolkit().beep();
                        setCaretPosition(offs);
                        return;
                    }
                    return;

                }

                // if offset is before $ position and the entry is made
                // the text entry is inserted after $ position and the caretposition

                if(offs == 0) {
                    int hyphenIndex = currentText.indexOf('-');
                    if(hyphenIndex  != -1) {
                        setCaretPosition(hyphenIndex+1);
                        return;
                    }
                    if( isMantissaLimitExceeded(getText(0,getLength())) ){
                        super.remove(offs+1,1);
                    }
                    super.insertString(offs+1,str,a);
                    setCaretPosition(offs+2);
                    return;
                }

                // if offset/cursorposition is in between the text
                if(offs != 0) {

                    // and if offset after DOT"." position

                    if(offs > currentText.indexOf('.')) {
                        super.remove(offs,1);
                        super.insertString(offs,str,a);

                        try {

                            // setting the caret position by determining the position
                            // curson position is determined from the right side by
                            // deducting the number of characters [from offset to curson positon
                            // of new text entry ] from the total text length
                            setCaretPosition(((getLength() - numOfcharactersOffset)+1));

                        }catch(Exception excep) {
                            //Toolkit.getDefaultToolkit().beep();
                            setCaretPosition(((getLength() - numOfcharactersOffset)));
                        }
                        return;
                    }

                    // checking for the character entry have exceeded the allowed mantissa limit
                    boolean isMantissaLimitExceeded = isMantissaLimitExceeded(getText(0,getLength()));

                    if(isMantissaLimitExceeded) {

                        int removedwidth = 0;
                        // checking for comma character while removing the text using
                        // backspace or delete key

                        if(isaCommaCharacter(getText(0,getLength()),offs)) {
                            removedwidth = 2;
                        } else {
                            removedwidth = 1;
                            if( offs == dotPos ){
                                super.remove(dotPos+1,1);
                                super.insertString(dotPos+1,str,a);
                                setCaretPosition(dotPos+2);                        
                                return;
                            }
                        }
                        super.remove(offs,removedwidth);
                        super.insertString(offs,str,a);

                        // reformatting the Text value to the Currency type
                        reconvertTextFieldValue(a);

                        // setting the caret position considering the removed character count as well
                        setCaretPosition((getLength() - numOfcharactersOffset )+removedwidth);
                        if((getLength() - numOfcharactersOffset) == (getText(0,getLength()).indexOf('.') - 1))
                            setCaretPosition((getLength() - numOfcharactersOffset )+removedwidth+1);

                        return;


                    }else
                        super.insertString(offs,str,a);

                    // reformatting the Text value to the Currency type
                    reconvertTextFieldValue(a);

                    // checking for the character entry have reached the mantissa limit
                    boolean isMantissaLimitReached = isMantissaLimitReached(getText(0,getLength()));

                    if(isMantissaLimitReached && offs <= currentText.indexOf('.')) {

                        setCaretPosition((getLength() - numOfcharactersOffset));

                        // checking cursor position have reached the decimal position
                        if((getLength() - numOfcharactersOffset) == getText(0,getLength()).indexOf('.'))
                            setCaretPosition(getText(0,getLength()).indexOf('.')+1);
                        return;
                    }

                }

                // reformatting the Text value to the Currency type
                reconvertTextFieldValue(a);

                // setting the caret position
                // total length minus number of character from offset

                setCaretPosition((getLength() - numOfcharactersOffset));
                return;


            } //dotPos == -1
            else
                super.insertString(offs, "$.00", a);



        }//insert method end


        /**
         *  Converts/reformats the Text Field content to correct US Currency Format.
         *  when values are set from application to the textfield
         *
         *  @param AttributeSet object of AttributeSet set .
         */

        public void reconvertTextFieldValue(AttributeSet a) {

            try {
                String currentText = getText(0, getLength());
                super.remove(0,getLength());

                if(a != null)
                    super.insertString(0,getNumberFormatinLocaleSpecific(currentText),a);
                else
                    super.insertString(0,getNumberFormatinLocaleSpecific(currentText),null);

            } catch(Exception badexp ) {
            }
        }


        /**
         *  Checks whether a valid entry is made to TextField using setText() method.
         *  and formats the entry to correct Currency format and returns the fromatted String
         * if the entry is not valid then the methos retirns the default entry "$.00"
         *
         *  @param numericValue String which should either US Currency format,Primitive values : integer,double,float
         *  @return String a string formatted in US currency format
         */

        public String isValidTextEntry(String numericValue) {

            try {

                boolean isNegative = false;

                // checks for negative double value or US currency values
                //  by looking for neagtive sign "-" or "$" position and the
                // values are then formatted to US currency format

                if(numericValue.indexOf('$') != -1) {
                    if(numericValue.indexOf('-') != -1) {
                        numericValue = numericValue.substring((numericValue.indexOf('-')+1),numericValue.length());
                        isNegative = true;
                        numericValue ="$"+numericValue;
                    }
                    numericValue = NumberFormat.getCurrencyInstance(Locale.US).parse(numericValue).doubleValue()+"";
                }
                //Bug fix:1411 Start 2 
                else{
                    numericValue ="$"+numericValue;
                    numericValue = NumberFormat.getCurrencyInstance(Locale.US).parse(numericValue).doubleValue()+"";
                }
                //Bug fix:1411 End 2 
                
                numericValue = NumberFormat.getCurrencyInstance(Locale.US).format(new Double(numericValue));
                if(isNegative)
                    numericValue = (new StringBuffer(numericValue)).insert(1, '-').toString();
                return numericValue;

            }catch(Exception ex)
            { ex.printStackTrace();	}

            return "$.00";
        }

        /**
         *  Checks if mantissa Limit has Exceeded returns if <code>true</code> when exceeded
         *  used to check whether the text entry is exceeded and then modification of subsequent
         *  characters in the textfield is carried out
         *  @param String parseString String .
         *  @return boolean true if Mantissa part exceeds the TextField Limit
         */

        public boolean isMantissaLimitExceeded(String parseString) {

            int mantissa = parseString.substring((parseString.indexOf('$')+1),parseString.indexOf('.')).replaceAll(",","" ).length() ;
            if(parseString.indexOf('-') != -1)
                --mantissa;
            if((mantissa+1) > maxMantissaDigits)
                return true;

            return false;

        }

        /**
         *  Checks if mantissa Limit has Reached returns if <code>true</code> when exceeded
         *  when limit is reached subsequent text entry is taken as modification of suffixed
         *  characters in the textfield
         *  @param String parseString String to check the mantissa limit
         *  @return boolean true if Mantissa part reaches the TextField Limit
         */

        public boolean isMantissaLimitReached(String parseString) {

            int mantissa = parseString.substring((parseString.indexOf('$')+1),parseString.indexOf('.')).replaceAll(",","" ).length() ;
            if(parseString.indexOf('-') != -1)
                --mantissa;
            if(mantissa == maxMantissaDigits)
                return true;

            return false;
        }

        /**
         *  Used to check whether a comma character is present while deleting a character at the offset.
         *
         *  @param String parseString String .
         *  @param  position int value indicating the position where to check the character
         */

        public boolean isaCommaCharacter(String parseString,int position) {

            try {
                parseString = parseString.substring(position,position+1);
                if(parseString.charAt(0) == ',')
                    return true;
            } catch(Exception e){
            }
            return false;

        }

        /**
         *  Converts the given String to US currency format else sets to default pattern "$.00"
         *
         *  @param String parseString String to reconvert the US currncy after each deletion
         *  of a character
         */

        public String getNumberFormatinLocaleSpecific(String parseString) {

            try {
                boolean isNegative = false;

                // checking if its a negative value if it is then it is converted to positive value
                // and the string is formated to US currency format using Numberformat class

                if(parseString.indexOf('-') != -1) {
                    parseString = parseString.substring((parseString.indexOf('-')+1),parseString.length());
                    isNegative = true;
                    parseString="$"+parseString;
                }

                NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(Locale.US);
                double parsedResult = numberFormatter.parse(parseString).doubleValue();
                parseString = numberFormatter.format(parsedResult);

                // if negative then the negative character is inserted into the Currency format
                if(isNegative)
                    parseString = (new StringBuffer(parseString)).insert(1, '-').toString();

                return parseString;

            }catch(Exception ex) {
                parseString = "$.00";
                return parseString;
            }

        }

        /**
         *  overridded remove method
         */

        public void remove(int offs, int len) throws BadLocationException {

            // getting the initial position
            String currentText = getText(0, getLength());
            int dollarPos = currentText.indexOf('$');
            int dotPos = currentText.indexOf('.');
            int caretPosition = getCaretPosition();
            int numOfcharactersOffset = (getLength() - offs);

            try {

                // checking the decimal point in the Currency field

                if(dotPos != -1) {

                    boolean isaCommaCharacter = false;
                    // if offset and decimal position are same set caret position
                    if(offs == dotPos) {
//                        setCaretPosition(dotPos+1);
                        offs = dotPos+1;
                        setCaretPosition(offs);
                        if( len > 1 ){
                            len--;
                        }
                        super.remove(offs, len);
                        reconvertTextFieldValue(null);
                        if( offs < getLength()){
                            setCaretPosition(offs);
                        }
                        return;
                    }

                    // if offset is less than 1 and when remove is called
                    // then all numeric characters in mantissa part are assumed to have deleted

                    if(len > 0 && offs < 1 && dollarPos != -1)	{
                        //Toolkit.getDefaultToolkit().beep();
                        setCaretPosition(caretPosition);
                        return;
                    }

                    // if offset position is beyond decimal then characters are removed
                    // and field value is reformat with reconvertTextFieldValue() method
                    // with no Attribute set

                    if(offs >= dotPos ) {
                        super.remove(offs,len);
                        reconvertTextFieldValue(null);
                        if(dotPos > 1)
                            setCaretPosition(offs);
                        else
                            setCaretPosition(getText(0, getLength()).indexOf('.')+1);
                        return;
                    }
                    // removed characters if offset position is beyond "$" position in Textfield
                    if(offs >= 1 )
                        super.remove(offs,len);

                    // while removing ensure any comma character at the offset
                    // if it is present then comma is also removed and the resulted String
                    // are again reconverted to Currency format

                    isaCommaCharacter = isaCommaCharacter(getText(0,getLength()),offs);
                    if(offs > 1)
                        reconvertTextFieldValue(null);

                    // while removing ensuring that the text has more that one character
                    // for reformatting or converting
                    if((dotPos - offs ) > 1 )
                        reconvertTextFieldValue(null);


                    try {

                        // while removing if more that one character is to be deleted
                        if(len > 1)
                            setCaretPosition(getText(0, getLength()).indexOf('.')+1);

                        // while removing only one character
                        if(len == 1)
                            setCaretPosition((getLength() - numOfcharactersOffset)+1);

                        // checking comma character and accordingly positioning the cursor
                        if(isaCommaCharacter)
                            setCaretPosition((getLength() - numOfcharactersOffset)+2);

                    }catch(Exception excep) {
                        //Toolkit.getDefaultToolkit().beep();
                        setCaretPosition(((getLength() - numOfcharactersOffset)));
                    }
                    return;
                }

            } catch(IllegalArgumentException illExcep) {
                setCaretPosition(getText(0, getLength()).indexOf('.'));
            }

        }//endof remove

    }//innerclass

    /** It is a supporter class which provides, setting the cursor position 
     *after the dollar currency text filed. - Modified by Ravi/ Chandru
     *10th June 2004 - Bug id# 904
     */
    class MyCaret extends javax.swing.text.DefaultCaret {
        public void focusGained(java.awt.event.FocusEvent e) {
            Object comp = e.getSource();
            String value = ((javax.swing.JTextField)comp).getText();
            if( value != null & value.length() > 1 ){
                setDot(1);
            }
            super.focusGained(e);
        }
        public void mousePressed(java.awt.event.MouseEvent e) {
            Object comp = e.getSource();
            String value = ((javax.swing.JTextField)comp).getText();
            if( value != null & value.length() > 1 ){
                setDot(1);
            }
            super.mousePressed(e);
        }
    }
    
    class CustomKeyAdapter extends KeyAdapter {
        /**
         * Handle the key pressed event from the text field.
         * @param e KeyEvent
         */
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                int dPos = getText().indexOf('.');
                int caretPos = getCaretPosition();
                if(caretPos == dPos+1){
                    setCaretPosition(dPos);
                }
            }
        }
        
    }
    
}//end of class



