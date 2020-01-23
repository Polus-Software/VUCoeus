/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * KeyPersonTableCellRenderer.java
 *
 * Created on January 13, 2009, 6:35 PM
 *
 */

package edu.mit.coeus.utils.keyperson;

import edu.mit.coeus.utils.CurrencyField;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.mit.coeus.utils.CoeusGuiConstants;

/**
 *
 * @author sreenathv
 */
public class KeyPersonTableCellRenderer extends DefaultTableCellRenderer
        implements TableCellRenderer{
    
    
    JTextField txtNameComponent;
    CurrencyField txtCurrencyComponent;
    CurrencyField txtCalCurrencyComponent, txtAcadCurrencyComponent,txtSummCurrencyComponent;
    JCheckBox chkComponent;
    JLabel lblText,lblCurrency;
    
    //Represents the empty string
//    private String EMPTY_STRING = "";
    private static final int KEY_PERSON_NAME_COLUMN = 1;
    private static final int KEY_PERSON_ROLE_COLUMN = 2;
    // JM 6-14-2012 reordered columns
    private static final int KEY_PERSON_FACULTY_COLUMN = 3;
    private static final int KEY_PERSON_EFFORT_COLUMN = 4;
    // JM END
    private static final int KEY_PERSON_ACAD_YEAR_COLUMN=5;
    private static final int KEY_PERSON_SUM_EFFORT_COLUMN=6;
    private static final int KEY_PERSON_CAL_YEAR_COLUMN =7;
    private static final int KEY_PERSON_ID_COLUMN = 8;
    private static final int KEY_PERSON_EMPLOYEE_FLAG_COLUMN = 9;
    
    private Color displayColor;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    private char functionType;
    
    public KeyPersonTableCellRenderer() {
        lblText = new JLabel();
        lblCurrency = new JLabel();
        lblText.setOpaque(true);
        lblCurrency.setOpaque(true);
        lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
        txtNameComponent = new JTextField();
        txtCurrencyComponent = new CurrencyField();
        txtAcadCurrencyComponent = new CurrencyField();
        txtCalCurrencyComponent = new CurrencyField();
        txtSummCurrencyComponent = new CurrencyField();
        chkComponent = new JCheckBox();
        chkComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        displayColor = (Color)UIManager.getDefaults().get("Panel.background");
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
            boolean isSelected,boolean hasFocus ,int row, int col) {
        switch (col) {
            
            case KEY_PERSON_NAME_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                }else{
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                }
                txtNameComponent.setText((String)value);
                lblText.setText(txtNameComponent.getText());
                return lblText;
            case KEY_PERSON_ROLE_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                }else{
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                }
                txtNameComponent.setText((String)value);
                lblText.setText(txtNameComponent.getText());
                return lblText;
            case KEY_PERSON_EFFORT_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblCurrency.setBackground(disabledBackground);
                    lblCurrency.setForeground(java.awt.Color.BLACK);
                }else{
                    lblCurrency.setBackground(java.awt.Color.white);
                    lblCurrency.setForeground(java.awt.Color.black);
                }
                txtCurrencyComponent.setText(value.toString());
                lblCurrency.setText(txtCurrencyComponent.getText());
                return lblCurrency;
                
            case KEY_PERSON_FACULTY_COLUMN:
                if(functionType != CoeusGuiConstants.DISPLAY_MODE){
                    chkComponent.setBackground(java.awt.Color.white);
                }
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                return chkComponent;
            case KEY_PERSON_ACAD_YEAR_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblCurrency.setBackground(disabledBackground);
                    lblCurrency.setForeground(java.awt.Color.BLACK);
                }else{
                    lblCurrency.setBackground(java.awt.Color.white);
                    lblCurrency.setForeground(java.awt.Color.black);
                }
                txtAcadCurrencyComponent.setText(value.toString());
                lblCurrency.setText(txtAcadCurrencyComponent.getText());
                return lblCurrency;
            case KEY_PERSON_CAL_YEAR_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblCurrency.setBackground(disabledBackground);
                    lblCurrency.setForeground(java.awt.Color.BLACK);
                }else{
                    lblCurrency.setBackground(java.awt.Color.white);
                    lblCurrency.setForeground(java.awt.Color.black);
                }
                txtCalCurrencyComponent.setText(value.toString());
                lblCurrency.setText(txtCalCurrencyComponent.getText());
                return lblCurrency;
            case KEY_PERSON_SUM_EFFORT_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblCurrency.setBackground(disabledBackground);
                    lblCurrency.setForeground(java.awt.Color.BLACK);
                }else{
                    lblCurrency.setBackground(java.awt.Color.white);
                    lblCurrency.setForeground(java.awt.Color.black);
                }
                txtSummCurrencyComponent.setText(value.toString());
                lblCurrency.setText(txtSummCurrencyComponent.getText());
                return lblCurrency;                
            case KEY_PERSON_ID_COLUMN:
                if( functionType == CoeusGuiConstants.DISPLAY_MODE ) {
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.BLACK);
                }else{
                    lblText.setBackground(java.awt.Color.white);
                    lblText.setForeground(java.awt.Color.black);
                }
                txtNameComponent.setText((String)value);
                lblText.setText(txtNameComponent.getText());
                return lblText;
            case KEY_PERSON_EMPLOYEE_FLAG_COLUMN:
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
            txtCurrencyComponent.setBackground(displayColor);
            txtCalCurrencyComponent.setBackground(displayColor);
            txtAcadCurrencyComponent.setBackground(displayColor);
            txtSummCurrencyComponent.setBackground(displayColor);
            chkComponent.setSelected(false);
        }else{
            txtNameComponent.setBackground(Color.white);
            txtCurrencyComponent.setBackground(Color.white);
            txtCalCurrencyComponent.setBackground(Color.white);
            txtAcadCurrencyComponent.setBackground(Color.white);
            txtSummCurrencyComponent.setBackground(Color.white);
            chkComponent.setEnabled(true);
        }
    }
    
}
