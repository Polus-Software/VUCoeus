/*
 * UnitCellEditor.java
 *
 * Created on March 26, 2004, 4:53 PM
 */

package edu.mit.coeus.utils.investigator;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellEditor;

import edu.mit.coeus.utils.JTextFieldFilter;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class UnitCellEditor extends AbstractCellEditor implements TableCellEditor{
    //Represents the text field
    JTextField txtName;
    //Represents the checkbox
    JCheckBox chkComponent;
    //Represents the column
    private int column;
    //Specifying the column numbers for the investigator's unit details.
    public static final int UNIT_HAND_ICON_COLUMN = 0;
    public static final int UNIT_LEAD_FLAG_COLUMN = 1;
    public static final int UNIT_NUMBER_COLUMN = 2;
    public static final int UNIT_NAME_COLUMN = 3;
    public static final int UNIT_OSP_ADMINISTRATOR_COLUMN = 4;
    
    private InvestigatorController controller;
    private JTable table;
    private int row;
    /**
     * Constructor
     */
    public UnitCellEditor() {
        txtName = new JTextField();
        //Added for case id 2229 - start
        txtName.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        //Added for case id 2229 - end
        chkComponent = new JCheckBox();
        postRegisterComponents();
    }
    
    /**
     * This method is to post the register components
     * @return void
     */
    private void postRegisterComponents(){
        //Bug fix 1611
        chkComponent.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                stopCellEditing();
                //Added by Nadh for Bug fix 1611
                table.editCellAt(row,UNIT_LEAD_FLAG_COLUMN);
                //Commented for the case id 2229 -start
//                java.awt.Component comp = table.getEditorComponent();
//                if( comp != null ) {
//                    comp.requestFocusInWindow();
//                }//ends 16-June-2005
                //Commented for the case id 2229 - end
            }
        });
        txtName.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,8));
        txtName.addFocusListener(new FocusAdapter(){
            public void focusLost(FocusEvent fe){
                if (!fe.isTemporary() ){
                    stopCellEditing();
                    //Added for case id 2229 - start
                    table.editCellAt(row, column);
                    //Added for case id 2229 - end
                }
            }
        });
        txtName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                stopCellEditing();
            }
        });
    }
    public void setUnitDetailsListener(MouseListener listener ) {
        txtName.addMouseListener(listener);
        chkComponent.addMouseListener(listener);
        
    }
    /**
     * This method is to get the table cell editor component
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param int row
     * @param int column
     * @return java.awt.Component
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.column = column;
        this.table = table;
        this.row = row;
        switch(column){
            case UNIT_LEAD_FLAG_COLUMN:
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                chkComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return chkComponent;
            case UNIT_NUMBER_COLUMN:
            case UNIT_NAME_COLUMN:
            case UNIT_OSP_ADMINISTRATOR_COLUMN:
                txtName.setText((String)value);
                return txtName;
        }
        return txtName;
    }
    
    /**
     * This method is to get the cell editor value
     * @return Object
     */
    public Object getCellEditorValue() {
        switch(column) {
            
            case UNIT_NUMBER_COLUMN:
            case UNIT_NAME_COLUMN:
            case UNIT_OSP_ADMINISTRATOR_COLUMN:
                return txtName.getText();
            case UNIT_LEAD_FLAG_COLUMN:
                return new Boolean(chkComponent.isSelected());
        }
        return ((JTextField)txtName).getText();
    }
    
    /**
     * This method is to get the click count
     * @return int
     */
    public int getClickCountToStart() {
        return 1;
    }
}
