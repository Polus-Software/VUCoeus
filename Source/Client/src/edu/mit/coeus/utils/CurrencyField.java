package edu.mit.coeus.utils;

import javax.swing.JTextField;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.awt.Color;

/**
 * This class is used to construct the Currency Field.
 * 
 * @author Ravi
 * @version 1.0
 */
public class CurrencyField extends JTextField{
    
    private DecimalFormat format;
    
    private static final  int defaultColumns = 6;
    
    private boolean editable;
    
    /**
     * This will construct new Currency Field.
     */
    public CurrencyField(){
        super(defaultColumns);        
        super.setHorizontalAlignment(JTextField.RIGHT );
		initProperties();
        setText(null);
    }

    /**
     * This Method will give the Currency Format used.
     * @return DecimalFormat format used
     */
	public DecimalFormat getFormat(){
		return format;
	}
        
    private void initProperties(){
        
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
                
        decimalFormat.setMinimumIntegerDigits(0);           
        decimalFormat.setMaximumIntegerDigits(3);   
          
        decimalFormat.setMaximumFractionDigits(2); 
        decimalFormat.setMinimumFractionDigits(2);
        
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        
        setDocument(new FormattedDocument(decimalFormat,this));

        this.format = decimalFormat;
        super.setHorizontalAlignment(JTextField.RIGHT );
     
    }
    
    /**
     * This will construct new Currency Field.
     * @param value string represent the defult value.
     * @param columns total length of this field.
     */
    public CurrencyField(String value, int columns) {        
        
        super(columns);
		initProperties();
        super.setHorizontalAlignment(JTextField.RIGHT );
		if(value.length()>0)
	        setText(format.format(Double.parseDouble(value)));
		else
			setText(format.format(Double.parseDouble(".00")));
    }

    /**
     * This will construct new Currency Field.
     * @param value string represent the defult value.     
     */
    public CurrencyField(String value) {        
        super(defaultColumns);
		initProperties();
        super.setHorizontalAlignment(JTextField.RIGHT );
		if(value!=null && value.length()>0)
	        setText(format.format(Double.parseDouble(value)));
		else
			setText(format.format(Double.parseDouble(".00")));
    }

    /**
     * This will construct new Currency Field.
     * @param value string represent the defult value.
     * @param columns total length of this field.
     * @param f NumberFormat used in this field
     */
	public CurrencyField(String value,int columns,NumberFormat f){
		super(columns);
        setDocument(new FormattedDocument(f,this));
        format = (DecimalFormat)f;
		setHorizontalAlignment(JTextField.RIGHT);
		if(value.length()>0)
	        setText(format.format(Double.parseDouble(value)));
		else
			setText(format.format(Double.parseDouble(".00")));

	}
    
    /**
     * This will construct new Currency Field.
     * @param value double represent the defult value.     * 
     * @param f NumberFormat used in this field
     */    
    public CurrencyField(double value,NumberFormat f) {        
        
        super(defaultColumns);
        super.setHorizontalAlignment(JTextField.RIGHT );
		format = (DecimalFormat)f;
		setDocument(new FormattedDocument(format,this));
        setText(format.format(value));
    }

    /**
     * This will construct new Currency Field.
     * @param value string represent the defult value.    
     * @param f NumberFormat used in this field
     */
    public CurrencyField(String value,NumberFormat f) {        
        
        super(defaultColumns);
        super.setHorizontalAlignment(JTextField.RIGHT );
		format = (DecimalFormat)f;
		setDocument(new FormattedDocument(format,this));
		if(value.length()>0)
	        setText(format.format(Double.parseDouble(value)));
		else
			setText(format.format(Double.parseDouble(".00")));
    }
    
    public void setText(String text){
        if(text!=null && text.length()>0){
            try{
                super.setText(format.format(Double.parseDouble(text)));
            }catch( NumberFormatException nfe){
                super.setText(format.format(Double.parseDouble(".00")));
            }
        }else{
            super.setText(format.format(Double.parseDouble(".00")));        
        }
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
  
} // class CurrencyField
