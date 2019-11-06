package edu.mit.coeus.utils;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * This class is used to Construct the coeus formatted document by extending the
 * Plaindocument.
 * @author Geo 
 * @version 1.0
 */
public class FormattedDocument extends PlainDocument{
    private NumberFormat format;
    private JTextComponent tc;
    boolean isCalledFromInsert = false;
    //added by sharath to allow negative  - START
    private boolean negativeAllowed = false;
    //added by sharath to allow negative  - END
    
    /**
     * This will Construct the coeus formatted document.
     * @param f NumberFormat format to be used
     * @param tc JTextComponent editing component
     */
    public FormattedDocument(NumberFormat f,JTextComponent tc) {
        format = f;
        this.tc = tc;
        tc.addKeyListener(new CustomKeyListener());
    }
    
    /**
     * This will Construct the coeus formatted document.
     * @param tc JTextComponent editing component
     */
    public FormattedDocument(JTextComponent tc) {
        initProperties();
        this.tc = tc;
        tc.addKeyListener(new CustomKeyListener());
    }
    
    private void initProperties(){
        
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        
        decimalFormat.setMinimumIntegerDigits(0);
        decimalFormat.setMaximumIntegerDigits(3);
        
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        this.format = decimalFormat;
        
    }
    
    /**
     * This will get the coeus format used in this document .
     * @return NumberFormat format used in this document.
     */
    public NumberFormat getFormat() {
        return format;
    }
    
    /**
     * This method is used to insert the string into the document
     * @param offs offset length value.
     * @param str value to be inserted
     * @param a AttributeSet of the value.
     * @throws BadLocationException  while inserting string
     */
    public void insertString(int offs, String str, AttributeSet a)
    throws BadLocationException {
        
        DecimalFormat decFormat = (DecimalFormat) format;
        int maxDig = decFormat.getMaximumIntegerDigits();
        int minDig = decFormat.getMinimumIntegerDigits();
        int maxFract = decFormat.getMaximumFractionDigits();
        int minFract = decFormat.getMinimumFractionDigits();

        if( str  == null ){
            str = ".00";
            super.insertString(offs, str, a);
            
        }else{
            //added by ranjeev to take care of when setText values to the Component
            //where its was seen appending values rather than formatting the content
            if( str != null && str.length() > 1){
                super.remove(0,getLength());
                try {
                    super.insertString(offs,decFormat.format(Double.parseDouble(str)),a);
                }catch(Exception e) {
                    super.insertString(offs,format.format(Double.parseDouble(".00")),a);
                }
                tc.setCaretPosition(0);
                return;
            }
            //added by ranjeev
            
            try{
                //added by sharath to allow negative  - START
                if(negativeAllowed && str.equals("-") && offs == 0) {
                    //- sign @ the start & negative allowed - valid entry.
                    //added by sharath to allow negative  - END    
                }else {
                    Double.parseDouble(str);
                }
            }catch(Exception excep){
                //if user enters '.' delete the all the contents in the left side
                if(str.equals(".")){
                    super.remove(offs,(getText(0,getLength()).indexOf('.') - offs));                    
                    formatCurrentData(a);
                }
                tc.setCaretPosition(getText(0,getLength()).indexOf('.')+1);
                //Toolkit.getDefaultToolkit().beep();
                return;
            }
            if( tc.getCaretPosition() == 0 && "0".equals(str)){
                //don't allow to enter '0' at the first position.
                return;
            }
            // find currentText
            String currentText = getText(0, getLength());
            String beforeOffset = currentText.substring(0, offs);
            String afterOffset = currentText.substring(offs, currentText.length());
            int dotPos = currentText.indexOf('.');
            if(getLength()==0 && dotPos == -1){
                super.insertString(0,str,null);
                dotPos = str.indexOf('.');
                if(dotPos!= -1){
                    tc.setCaretPosition(dotPos);
                }
                return;
            }
            if(getLength() < maxDig+maxFract+1){
                if(offs > dotPos){
                    //if(currentText.substring(dotPos).length()<maxDig){
                    //Checking for digits after decomal point 
                    if(currentText.substring(dotPos).length() < maxFract+1){
                        super.insertString(offs,str,a);
                        tc.setCaretPosition(offs);
                        return;
                    }
                    else{
                        if(offs != getLength()){
                            isCalledFromInsert = true;
                            remove(offs,1);
                            super.insertString(offs,str,a);
                            isCalledFromInsert = false;
                        }
                        return;
                    }
                }
                else if(offs < dotPos){
                    if(currentText.substring(0,dotPos-1).length()<maxDig){
                        super.insertString(offs,str,a);
                        return;
                    }
                }
                else{
                    if(dotPos!=0){
                        if(currentText.substring(0,dotPos-1).length()<maxDig){
                            super.insertString(offs,str,a);
                        }
                    }
                    else{
                        super.insertString(offs,str,a);
                    }
                    return;
                }
            }
            else{
                if(offs == dotPos){
                    tc.setCaretPosition(dotPos+1);
                    isCalledFromInsert = true;
                    remove(dotPos+1,1);
                    super.insertString(dotPos+1,str,a);
                    isCalledFromInsert = false;
                }
                else if(offs < dotPos){
                    isCalledFromInsert = true;
                    remove(offs,1);
                    super.insertString(offs,str,a);
                    isCalledFromInsert = false;
                    
                }
                else{
                    if(offs != getLength()){
                        isCalledFromInsert = true;
                        remove(offs,1);
                        super.insertString(offs,str,a);
                        isCalledFromInsert = false;
                    }
                    return;
                }
            }
            
        }
        
    }
    
    /**
     * This method is used to remove the string from the document
     * @param offs offset length value.
     * @param len length of the from the offset
     * @throws BadLocationException  while removing string from the document.
     */
    public void remove(int offs, int len) throws BadLocationException {
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = "";
        int oldDotPos = currentText.indexOf(".");
        if(len+offs <getLength())
            afterOffset = currentText.substring(len + offs,currentText.length());
        String proposedResult = beforeOffset + afterOffset;
        int dotPos = proposedResult.indexOf('.');
        
        if( "".equals(proposedResult ) ) {
            super.remove(offs,len);
            super.insertString(0, ".00", null);
            tc.setCaretPosition(0);
            return;
        }
        if( dotPos == -1 && offs <= oldDotPos && len == 1){
            // trying to remove dot
            offs = oldDotPos+1;
            tc.setCaretPosition(offs);
            if( len > 1 ){
                len--;
            }
        }
        super.remove(offs, len);
        if(!isCalledFromInsert){
            formatCurrentData(null);
            if( offs < getLength()){
                tc.setCaretPosition(offs);
            }
        }
    }
    
    private void formatCurrentData(AttributeSet a){
        try {
            DecimalFormat decFormat = (DecimalFormat) format;
            String currentText = getText(0, getLength());
            super.remove(0,getLength());
            //Trim the comma before parsing
            currentText = currentText.replaceAll(",", "");
            
            super.insertString(0,decFormat.format(Double.parseDouble(currentText)),a);

        } catch(Exception badexp ) {
            badexp.printStackTrace();
        }
    }
    
    /**To set the Parameter to make the document Allow Negative value
     */
    public void setNegativeAllowed(boolean negativeAllowed) {
        this.negativeAllowed = negativeAllowed;
    }
    
    public boolean isNegativeAllowed() {
        return negativeAllowed;
    }
    
    /**
     * This class is used to register the key event changes.
     */
    class CustomKeyListener  implements KeyListener {
        
        /**
         * This method is invoked when a key typed
         * @param e KeyEvent
         */
        public void keyTyped(KeyEvent e) {
        }
        
        /**
         * Handle the key pressed event from the text field.
         * @param e KeyEvent
         */
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                int dPos = tc.getText().indexOf('.');
                int caretPos = tc.getCaretPosition();
                if(caretPos == dPos+1){
                    tc.setCaretPosition(dPos);
                }
            }
        }
        
        /**
         * Handle the key released event from the text field.
         * @param e KeyEvent
         */
        public void keyReleased(KeyEvent e) {
        }
    }
    
    /**
     * For Testing purpose only
     */
    public static void main(String s[]) {
        JFrame jFrame = new JFrame();
        //jFrame.getContentPane().setLayout(new java.awt.FlowLayout());
        
        CoeusTextField coeusTextField;        
        java.text.DecimalFormat decimalFormat;
        
        decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decimalFormat.setMinimumIntegerDigits(0);
        decimalFormat.setMaximumIntegerDigits(10);
        
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        coeusTextField = new CoeusTextField();
        FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,coeusTextField);
        formattedDocument.setNegativeAllowed(true);
        coeusTextField.setDocument(formattedDocument);
        
        coeusTextField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        coeusTextField.setText("2134");
        
        jFrame.getContentPane().add(coeusTextField, java.awt.BorderLayout.NORTH);
        jFrame.setSize(200,200);
        jFrame.setVisible(true);
    }
    
}