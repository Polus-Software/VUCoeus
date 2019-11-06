/*
 * UnitTableCellRenderer.java
 *
 * Created on March 26, 2004, 5:15 PM
 */

package edu.mit.coeus.utils.investigator;

import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.Component;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class UnitTableCellRenderer extends DefaultTableCellRenderer 
                                                  implements TableCellRenderer {
    
    JTextField txtNameComponent;
    CurrencyField txtCurrencyComponent;
    JCheckBox chkComponent;
    JLabel lblText;
    //Specifying the column numbers for the investigator's unit details.
    private static final int UNIT_HAND_ICON_COLUMN = 0;
    private static final int UNIT_LEAD_FLAG_COLUMN = 1;
    private static final int UNIT_NUMBER_COLUMN = 2;
    private static final int UNIT_NAME_COLUMN = 3;
    private static final int UNIT_OSP_ADMINISTRATOR_COLUMN = 4;
    private Color displayColor;
    private char functionType;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    /** Constructor */ 
    public UnitTableCellRenderer(){
        lblText = new JLabel();
        lblText.setOpaque(true);
        txtNameComponent = new JTextField();
        chkComponent = new JCheckBox();
        displayColor = (Color)UIManager.getDefaults().get("Panel.background");
        chkComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblText.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
//        txtNameComponent.setBorder(new EmptyBorder(0,0,0,0));
        
    }
    
    /**
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param boolean hasFocus
     * @param boolean hasFocus
     * @param int row 
     * @param int col
     * @return Component
     */  
    public Component getTableCellRendererComponent(JTable table,Object value,
                        boolean isSelected,boolean hasFocus, int row, int col) {
                            
        switch(col) {
            case UNIT_NAME_COLUMN:
            case UNIT_OSP_ADMINISTRATOR_COLUMN:
                 if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                }else{
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                }
                 txtNameComponent.setText((String)value);
                 lblText.setText(txtNameComponent.getText());
                 //return txtNameComponent;
                 return lblText;
            case UNIT_NUMBER_COLUMN:
                 if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                }else{
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                }
                 txtNameComponent.setText((String)value);
                 lblText.setText(txtNameComponent.getText());
                 //return txtNameComponent;
                 return lblText;
            case UNIT_LEAD_FLAG_COLUMN:
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                
                return chkComponent;
        }
        return super.getTableCellRendererComponent(table,value,
                                                  isSelected, hasFocus,row,col);
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     *
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
        if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
            txtNameComponent.setBackground(displayColor);
            //chkComponent.setEnabled(false);
            chkComponent.setSelected(false);
        }else{
            txtNameComponent.setBackground(Color.white);
            chkComponent.setEnabled(true);
        
        }
    }
    
}
