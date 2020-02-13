/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * KeyPersonCellEditor.java
 *
 * Created on January 15, 2009, 8:39 PM
 *
 */

package edu.mit.coeus.utils.keyperson;

import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.LimitedPlainDocument;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;


/**
 *
 * @author sreenathv
 */
public class KeyPersonCellEditor  extends AbstractCellEditor implements
        TableCellEditor{
    CurrencyField txtCurrencyComponent;
    CurrencyField txtCalCurrencyComponent, txtAcadCurrencyComponent, txtSummCurrencyComponent;
    JTextField txtName;
    JCheckBox chkComponent;
    
    private int column;
    private int row;
    private JTable table;
    
    // Specifying the columns numbers for the investigators details.
    private static final int KEY_PERSON_HAND_ICON_COLUMN = 0;
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
    
    public KeyPersonCellEditor() {
        txtName = new JTextField();
        txtCurrencyComponent = new CurrencyField();
        txtAcadCurrencyComponent = new CurrencyField();
        txtCalCurrencyComponent = new CurrencyField();
        txtSummCurrencyComponent = new CurrencyField();
        chkComponent = new JCheckBox();
        postRegisterComponents();
    }
    /**
     * This method is to register the components
     * @return void
     */
    private void postRegisterComponents() {
        txtName.setDocument(new LimitedPlainDocument(90));
        chkComponent.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie){
                stopCellEditing();
                
            }
        });
        txtName.addFocusListener(new CustomFocusAdapter());
        txtCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        txtCalCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        txtAcadCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        txtSummCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        txtName.addActionListener(new CustomActionListener());
        txtCurrencyComponent.addActionListener(new CustomActionListener());
    }
    
    public void setViewKeyPersonDetailsListener(MouseListener listener) {
        txtName.addMouseListener(listener);
        txtCurrencyComponent.addMouseListener(listener);
        txtAcadCurrencyComponent.addMouseListener(listener);
        txtSummCurrencyComponent.addMouseListener(listener);
        txtCalCurrencyComponent.addMouseListener(listener);
        chkComponent.addMouseListener(listener);
    }
    
    /**
     *This method is to get the table cell editor component
     * @param JTable table
     * @param Object value
     * @param boolean isSelected
     * @param int row
     * @param int column
     * @return Component
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.column = column;
        this.row = row;
        this.table = table;
        switch (column) {
            case KEY_PERSON_NAME_COLUMN:
                txtName.setText((String)value);
                return txtName;
            case KEY_PERSON_ROLE_COLUMN:
                txtName.setText((String)value);
                return txtName;
            case KEY_PERSON_EFFORT_COLUMN:
                txtCurrencyComponent.setText(CoeusGuiConstants.EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtCurrencyComponent;
                
            case KEY_PERSON_FACULTY_COLUMN:
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                chkComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                chkComponent.setBackground(java.awt.Color.white);
                return chkComponent;
            case KEY_PERSON_ACAD_YEAR_COLUMN:
                txtAcadCurrencyComponent.setText(CoeusGuiConstants.EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtAcadCurrencyComponent;
            // JM 6-26-2012 the following two fields had reversed logic
            case KEY_PERSON_CAL_YEAR_COLUMN:
                txtCalCurrencyComponent.setText(CoeusGuiConstants.EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtCalCurrencyComponent;
            case  KEY_PERSON_SUM_EFFORT_COLUMN:
                txtSummCurrencyComponent.setText(CoeusGuiConstants.EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtSummCurrencyComponent;
            // JM END
            case KEY_PERSON_ID_COLUMN:
                txtName.setText((String)value);
                return txtName;
            case KEY_PERSON_EMPLOYEE_FLAG_COLUMN:
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                chkComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return chkComponent;
        }
        
        return txtName;
    }
    /**
     * This method is to get the cell editor value
     * @return Object
     */
    public Object getCellEditorValue() {
        switch(column){
            case  KEY_PERSON_NAME_COLUMN:
                return txtName.getText();
            case KEY_PERSON_ROLE_COLUMN:
                return txtName.getText();
            case KEY_PERSON_EFFORT_COLUMN:
                return txtCurrencyComponent.getText();
                
            case KEY_PERSON_FACULTY_COLUMN:
                return new Boolean(chkComponent.isSelected());
            case KEY_PERSON_ACAD_YEAR_COLUMN:
                return txtAcadCurrencyComponent.getText();
            case KEY_PERSON_SUM_EFFORT_COLUMN:
                return txtSummCurrencyComponent.getText();
            // JM 6-26-2012 fixed to call CAL year function rather than SUMM
            case  KEY_PERSON_CAL_YEAR_COLUMN:
                //return txtSummCurrencyComponent.getText();
                return txtCalCurrencyComponent.getText();
            // JM END
            case KEY_PERSON_ID_COLUMN:
                return txtName.getText();
            case KEY_PERSON_EMPLOYEE_FLAG_COLUMN:
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
    
    
    class CustomFocusAdapter extends FocusAdapter{
        public void focusLost(FocusEvent fe){
            if (!fe.isTemporary()){
                stopCellEditing();
                table.editCellAt(row, column);
            }
        }
    }
    class CustomActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            stopCellEditing();
        }
    }
}
