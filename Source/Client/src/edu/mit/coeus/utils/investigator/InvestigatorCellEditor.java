/*
 * InvestigatorCellEditor.java
 *
 * Created on March 26, 2004, 2:51 PM
 */

package edu.mit.coeus.utils.investigator;

import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.bean.InvestigatorBean;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.border.BevelBorder;


/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class InvestigatorCellEditor extends AbstractCellEditor implements
        TableCellEditor{
    //Represents the currency field
    CurrencyField txtCurrencyComponent;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    CurrencyField txtCalCurrencyComponent, txtAcadCurrencyComponent, txtSummCurrencyComponent;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
    //Represents the textfield
    JTextField txtName;
    //Represents the checkbox
    JCheckBox chkComponent;
    //Represnts the column
    private int column;
    //Represents the empty string
    private String EMPTY_STRING = "";
    private int row;
    private JTable table;
    
    // Specifying the columns numbers for the investigators details.
    public static final int INVESTIGATOR_NAME_COLUMN = 1;
    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - start
//    public static final int INVESTIGATOR_EFFORT_COLUMN = 2;
//    public static final int INVESTIGATOR_PI_COLUMN = 3;
//    public static final int INVESTIGATOR_FACULTY_COLUMN = 4;
    //Commented for Coeus 4.3 -PT ID:2229 Multi PI - end
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
    public static final int INVESTIGATOR_PI_COLUMN = 2;
    private static final int INVESTIGATOR_MULTI_PI_COLUMN = 3;
    public static final int INVESTIGATOR_FACULTY_COLUMN = 4;
    public static final int INVESTIGATOR_EFFORT_COLUMN = 5;
    //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    private static final int INVESTIGATOR_ACAD_YEAR_COLUMN=6;
    private static final int INVESTIGATOR_SUM_EFFORT_COLUMN=7;
    private static final int INVESTIGATOR_CAL_YEAR_COLUMN =8;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
    /**
     * Constructor
     */
    public InvestigatorCellEditor() {
        txtName = new JTextField();
        txtCurrencyComponent = new CurrencyField();
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        txtAcadCurrencyComponent = new CurrencyField();
        txtCalCurrencyComponent = new CurrencyField();
        txtSummCurrencyComponent = new CurrencyField();
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
        chkComponent = new JCheckBox();
        txtName.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        txtCurrencyComponent.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        txtAcadCurrencyComponent.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        txtCalCurrencyComponent.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        txtSummCurrencyComponent.setBorder(new javax.swing.border.BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray));
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
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
                //Commented for case id 2229 - start
                //Added by Nadh for Bug fix 1611
//                table.editCellAt(row,column);
//                java.awt.Component comp = table.getEditorComponent();
//                if( comp != null ) {
//                    comp.requestFocusInWindow();
//                }//ends 16-June-2005
                //Commented for case id 2229 - end
            }
        });
        txtName.addFocusListener(new CustomFocusAdapter());
        txtCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        txtCalCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        txtAcadCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        txtSummCurrencyComponent.addFocusListener(new CustomFocusAdapter());
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
        txtName.addActionListener(new CustomActionListener());
        txtCurrencyComponent.addActionListener(new CustomActionListener());
    }
    
    public void setViewInvestigatorDetailsListener(MouseListener listener) {
        txtName.addMouseListener(listener);
        txtCurrencyComponent.addMouseListener(listener);
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
        txtAcadCurrencyComponent.addMouseListener(listener);
        txtSummCurrencyComponent.addMouseListener(listener);
        txtCalCurrencyComponent.addMouseListener(listener);
        //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
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
            case INVESTIGATOR_NAME_COLUMN:
                txtName.setText((String)value);
                return txtName;
            case INVESTIGATOR_EFFORT_COLUMN:
                txtCurrencyComponent.setText(EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtCurrencyComponent;
            case INVESTIGATOR_PI_COLUMN:
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
            case INVESTIGATOR_MULTI_PI_COLUMN:
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
            case INVESTIGATOR_FACULTY_COLUMN:
                chkComponent.setSelected(((Boolean)(value)).booleanValue());
                chkComponent.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                return chkComponent;
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
            case INVESTIGATOR_ACAD_YEAR_COLUMN:
                txtAcadCurrencyComponent.setText(EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtAcadCurrencyComponent;
            case INVESTIGATOR_CAL_YEAR_COLUMN:
                txtCalCurrencyComponent.setText(EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtCalCurrencyComponent;
            case INVESTIGATOR_SUM_EFFORT_COLUMN:
                txtSummCurrencyComponent.setText(EMPTY_STRING +
                        new Double(value.toString()).doubleValue());
                
                return txtSummCurrencyComponent;
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
        }
        
        return txtName;
    }
    /**
     * This method is to get the cell editor value
     * @return Object
     */
    public Object getCellEditorValue() {
        switch(column){
            case INVESTIGATOR_NAME_COLUMN:
                return txtName.getText();
            case INVESTIGATOR_EFFORT_COLUMN:
                return txtCurrencyComponent.getText();
            case INVESTIGATOR_PI_COLUMN:
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
            case INVESTIGATOR_MULTI_PI_COLUMN:
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
            case INVESTIGATOR_FACULTY_COLUMN:
                return new Boolean(chkComponent.isSelected());
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
            case INVESTIGATOR_ACAD_YEAR_COLUMN:
                return txtAcadCurrencyComponent.getText();
            case INVESTIGATOR_CAL_YEAR_COLUMN:
                return txtCalCurrencyComponent.getText();
            case INVESTIGATOR_SUM_EFFORT_COLUMN:
                return txtSummCurrencyComponent.getText();
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
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
