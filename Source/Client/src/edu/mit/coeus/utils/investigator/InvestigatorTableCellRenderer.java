/*
 * InvestigatorTableCellRenderer.java
 *
 * Created on March 26, 2004, 4:28 PM
 */

package edu.mit.coeus.utils.investigator;

import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.*;
import java.awt.Component;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import edu.mit.coeus.utils.CoeusGuiConstants;
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class InvestigatorTableCellRenderer extends DefaultTableCellRenderer
        implements TableCellRenderer {
    
    JTextField txtNameComponent;
    CurrencyField txtCurrencyComponent;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    CurrencyField txtCalCurrencyComponent, txtAcadCurrencyComponent,txtSummCurrencyComponent;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    JCheckBox chkComponent;
    JLabel lblText,lblCurrency;
    
    //Represents the empty string
    private String EMPTY_STRING = "";
    // Specifying the columns numbers for the investigators details.
    private static final int INVESTIGATOR_NAME_COLUMN = 1;
    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - start
//    private static final int INVESTIGATOR_EFFORT_COLUMN = 2;
//    private static final int INVESTIGATOR_PI_COLUMN = 3;
//    private static final int INVESTIGATOR_FACULTY_COLUMN = 4;
    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - end
    
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
    private static final int INVESTIGATOR_PI_COLUMN = 2;
    private static final int INVETIGATOR_MULTI_PI_COLUMN = 3;
    private static final int INVESTIGATOR_FACULTY_COLUMN = 4;
    private static final int INVESTIGATOR_EFFORT_COLUMN = 5;
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    private static final int INVESTIGATOR_ACAD_YEAR_COLUMN=6;
    private static final int INVESTIGATOR_SUM_EFFORT_COLUMN=7;
    private static final int INVESTIGATOR_CAL_YEAR_COLUMN =8;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
    
    private Color displayColor;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
    private char functionType;
    
    /** Constructor */
    public InvestigatorTableCellRenderer() {
        BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
        setBorder(bevelBorder);
        lblText = new JLabel();
        lblCurrency = new JLabel();
        lblText.setOpaque(true);
        lblCurrency.setOpaque(true);
        lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
        txtNameComponent = new JTextField();
        txtCurrencyComponent = new CurrencyField();
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        txtAcadCurrencyComponent = new CurrencyField();
        txtCalCurrencyComponent = new CurrencyField();
        txtSummCurrencyComponent = new CurrencyField();
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        lblText.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        lblCurrency.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
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
            
            case INVESTIGATOR_NAME_COLUMN:
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
            case INVESTIGATOR_EFFORT_COLUMN:
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
            case INVESTIGATOR_PI_COLUMN:
                if(isSelected ){
                    lblText.setBackground(
                            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                    lblText.setForeground(java.awt.Color.white);
                }else{
                    lblText.setBackground(disabledBackground);
                    lblText.setForeground(java.awt.Color.black);
                }
            case INVESTIGATOR_FACULTY_COLUMN:
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
            case INVETIGATOR_MULTI_PI_COLUMN:
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                return chkComponent;
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
            case INVESTIGATOR_ACAD_YEAR_COLUMN:
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
            case INVESTIGATOR_CAL_YEAR_COLUMN:
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
            case INVESTIGATOR_SUM_EFFORT_COLUMN:
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
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
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
            txtNameComponent.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
            txtCurrencyComponent.setBackground(displayColor);
            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            txtCalCurrencyComponent.setBackground(displayColor);
            txtAcadCurrencyComponent.setBackground(displayColor);
            txtSummCurrencyComponent.setBackground(displayColor);
            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            //chkComponent.setEnabled(false);
            chkComponent.setSelected(false);
        }else{
            txtNameComponent.setBackground(Color.white);
            txtCurrencyComponent.setBackground(Color.white);
            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            txtCalCurrencyComponent.setBackground(Color.white);
            txtAcadCurrencyComponent.setBackground(Color.white);
            txtSummCurrencyComponent.setBackground(Color.white);
            //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            chkComponent.setEnabled(true);
        }
    }
    
    
}
