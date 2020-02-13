/*
 * DateField.java
 *
 * Created on September 17, 2002, 12:14 PM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.utils.DateUtils;
import javax.swing.JTextField;
import edu.mit.coeus.utils.LimitedPlainDocument;

/**
 * This class is used to create the Data Field component.
 *
 * @author  phani
 * @version 1.0
 */
public class DateField extends JTextField {
    
    private static final  int defaultColumns = 12;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    
    /** Creates a new instance of DateField */
    public DateField() {
        super(defaultColumns);
        super.setDocument(new LimitedPlainDocument(12));
        super.setHorizontalAlignment(JTextField.LEFT );
		//initProperties();
        setText(null);
    }
    
    /**
     * This method is used to the set the date filed value
     * @param value represent the date data.
     */
    public void setValue(String value) {
        if ( ( value != null )&& (value.length()>0) ) {
                 setText(new DateUtils().restoreDate(value,DATE_SEPARATERS));
                }
    }
    
    /**
     * This method is used to the get the date filed value
     * @return String value represent the date data.
     */
    public String getValue(){
        String formattedDate = new DateUtils().formatDate(this.getText(),DATE_SEPARATERS,REQUIRED_DATEFORMAT);
        return formattedDate;
    }
    
    /**
     * This method is used to the restore the date filed value
     * @return String value represent the date data.
     */
    public String restoreValue(){
        String formattedDate = new DateUtils().restoreDate(this.getText(),DATE_SEPARATERS);
        return formattedDate;
    }

    /** Creates a new instance of DateField 
     * @param value defult value.
     * @param columns length of this date field
     */
    public DateField(String value, int columns) {        
        super(columns);
        //initProperties();
        super.setHorizontalAlignment(JTextField.RIGHT );
                if(value.length()>0) {
                 setText(new DateUtils().restoreDate(value,DATE_SEPARATERS));
                }
               
   }
    
    /** Creates a new instance of DateField 
     * @param value defult value.     
     */
     public DateField(String value) {        
        super(defaultColumns);
        super.setHorizontalAlignment(JTextField.RIGHT );
        if(value.length()>0) {
            setText(new DateUtils().restoreDate(value,DATE_SEPARATERS));
        }
     }

}
