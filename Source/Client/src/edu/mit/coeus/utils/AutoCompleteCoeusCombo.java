/*
 * AutoCompleteCoeusCombo.java
 *
 * Created on September 14, 2004, 11:51 AM
 */

package edu.mit.coeus.utils;

import java.awt.Color;
import java.awt.Component;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;


import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.StateComboBoxUI;


/**
 *
 * @author  ajaygm
 */
public class AutoCompleteCoeusCombo extends JComboBox implements ActionListener{
    
    private boolean showCode;
    private Vector items;
    private boolean editable;
    
    private boolean autoCompleteOnFocusLost = true;
    
     /**
     * Initializes the properties of CoeusComboBox as it needs to set custom Renderers
     * and Editors for AutoCompleteCoeusCombo.        
     */
    private void initProperties(){
        //set editor
        JTextField tf;
        if(getEditor() != null) {
            tf = (JTextField)getEditor().getEditorComponent();
            if(tf != null) {
                tf.setDocument(new ComboBoxDocument());
            }
        }
        
        AutoCompleteCoeusComboRenderer renderer= new AutoCompleteCoeusComboRenderer();
        setBorder(new LineBorder(Color.black,1));
        setRenderer(renderer);
        setFont(CoeusFontFactory.getNormalFont());
        
        setAutoCompleteOnFocusLost(autoCompleteOnFocusLost);
        
    }
    
    /** Creates a new instance of AutoCompleteCoeusCombo */
    public AutoCompleteCoeusCombo() {
        this(true);
    }
    
    public AutoCompleteCoeusCombo(boolean autoCompleteOnFocusLost) {
        this.autoCompleteOnFocusLost = autoCompleteOnFocusLost;
        setUI(new StateComboBoxUI());
        initProperties();
    }
    
    /**
     * Constructor with default AutoCompleteCoeusCombo
     * @param aModel represent the AutoCompleteCoeusCombo.
     */
    public AutoCompleteCoeusCombo(ComboBoxModel aModel) {
        this();
        setModel(aModel);
        initProperties();
    }
    
     /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items represent the ComboBoxModel values as array of objects.
     */
    public AutoCompleteCoeusCombo(final Object[] items) {
        super(items);
        setUI(new StateComboBoxUI());
        initProperties();
    }
    
     /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items An array of elements that are shown in popup
     * @param editable  Status of ComboBox, If true the combobox value can be modified.
     */
    public AutoCompleteCoeusCombo(final Object[] items, boolean editable) {
        super(items);        
        this.editable =editable;
        setEditable(editable);
        setUI(new StateComboBoxUI());        
        initProperties();
    }
    
    /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items a Vector of combobox items.
     */
    public AutoCompleteCoeusCombo(Vector items) {
        super(items);
        this.items = items;
        setUI(new StateComboBoxUI());
        initProperties();
    }
    
    /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items A vector of items that are shown in popup
     * @param editable  Status of ComboBox, If true the combobox value can be modified.
     */
    public AutoCompleteCoeusCombo(Vector items, boolean editable){
        super(items);        
        this.items = items;
        this.editable = editable;
        setEditable(editable);
        setUI(new StateComboBoxUI());        
        initProperties();
    }
    
    public void setAutoCompleteOnFocusLost(boolean autoCompleteOnFocusLost) {
        this.autoCompleteOnFocusLost = autoCompleteOnFocusLost;
    }
    
    /**
     * This method is used to Add new item to the Coeus combo box.
     * @param item Object instance represent the new value to be appended.
     */
    public void addItem(Object item){
        if(items==null){
            items = new Vector();
        }
        this.items.addElement(item);
        super.addItem(item);
    }
    
    /* This method is used to remove all items to the Coeus combo box*/
    public void removeAllItems(){
        if(items != null && items.size() > 0 ) {
            this.items.removeAllElements();
        }
        super.removeAllItems();
    }
    
   /**
     * Sets the Status(Editable/NonEditable) of combobox, Based on which the combo 
     * value can be changed. Default is not editable
     *
     * @param item  Object of the selected Item in the combo box.
     */
    public void setSelectedItem( Object item){
        ComboBoxBean sltdComboBeaxBean = new ComboBoxBean("","");
        if(item instanceof ComboBoxBean){
            sltdComboBeaxBean = (ComboBoxBean)item;
        }else{
            String sltdCode = (String)item;
            if(items!=null && sltdCode!=null){
                int cmbSize = items.size();
                for(int cmbIndex=0;cmbIndex<cmbSize;cmbIndex++){
                    ComboBoxBean cmbBean = (ComboBoxBean)items.elementAt(cmbIndex);
                    if(cmbBean.getCode().trim().equalsIgnoreCase(sltdCode.trim())){
                        sltdComboBeaxBean = cmbBean;
                    }
                }
            }
        }
        super.setSelectedItem(sltdComboBeaxBean);
    }
    
    /**
     * This method is used to fetch the selected Item from the combo box.
     * @return Object represent the selected value in the combo box.
     */
    public Object getSelectedItem(){
        ComboBoxBean sltdComboBeaxBean = new ComboBoxBean("","");
        Object sltdItem = super.getSelectedItem();
        if(sltdItem!=null){
            if(sltdItem instanceof ComboBoxBean){
                sltdComboBeaxBean = (ComboBoxBean)sltdItem;
            }else {
                String sltdDesc = (String)sltdItem;
                if(items!=null){
                    int cmbSize = items.size();
                    for(int cmbIndex=0;cmbIndex<cmbSize;cmbIndex++){
                        ComboBoxBean cmbBean = (ComboBoxBean)items.elementAt(cmbIndex);
                        if(cmbBean.getDescription().equalsIgnoreCase(sltdDesc)){
                            sltdComboBeaxBean = cmbBean;
                        }
                    }
                }
            }
            if(items!=null && isEditable() && !items.contains(sltdComboBeaxBean)){
                sltdComboBeaxBean = new ComboBoxBean(sltdItem.toString(),"");
            }
        }
        return sltdComboBeaxBean;
    }
    
    /**
     * This method is used specify whether code should be displayed for each
     * item in the combo box.
     * @param flag boolean value which specifies item code should be displayed
     * or not.
     */
    public void setShowCode(boolean flag){
        this.showCode = flag;
    }
    
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        if(autoCompleteOnFocusLost) {
            autoComplete();
        }
    }
    
    /**
     *Auto completes the editor
     */
    private void autoComplete() {
        JTextField tf = (JTextField)getEditor().getEditorComponent();
        String text = tf.getText();
        ComboBoxModel aModel = getModel();
        String current;
        for (int i = 0; i < aModel.getSize(); i++) {
            current = aModel.getElementAt(i).toString();
            if (current.toLowerCase().startsWith(text.toLowerCase())){
                tf.setText(current);
                tf.setSelectionStart(text.length());
                tf.setSelectionEnd(current.length());
                break;
            }
        }
    }
    
    /**
     * An inner class to set the Default renderer for AutoCompleteCoeusCombo. As this combobox 
     * can show multiple columns in combo popup, so add number columns to this panel, like
     * that as many as number panel will be added to panel as many items available in combobox.
     * that is rendered in every cell.
     */    
    class AutoCompleteCoeusComboRenderer extends JLabel implements ListCellRenderer {
        /**
         * Constructor to set the layout and border for this panel.
         */
        public AutoCompleteCoeusComboRenderer() {
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
        }
        
        /**
         * An overridden method to render this component in desired cell.
         * @param list JList represent the component list.
         * @param value Object represent the list value.
         * @param index selected index int value.
         * @param isSelected boolean true if selected else false.
         * @param cellHasFocus has focus parameter.
         * @return Component selected component in the list.
         */
        public Component getListCellRendererComponent(JList list, Object value, 
            int index, boolean isSelected, boolean cellHasFocus) {
            setBackground(isSelected ? Color.blue : Color.white);
            setForeground(isSelected ? Color.white : Color.black);
            if( value instanceof ComboBoxBean ){                
                ComboBoxBean comboBoxBean = (ComboBoxBean) value;
                if(showCode){                    
                    if(!(comboBoxBean.getDescription().trim().equals(""))){
                        setText(comboBoxBean.getDescription() + "("+ comboBoxBean.getCode()+")" );
                    }
                }else{
                    if( comboBoxBean.getDescription() != null ){
                        setText(comboBoxBean.getDescription().trim());
                    }
                }
            }
            return this;
        }
        
    }// end of CoeusComboBoxRenderer class
    
    /*
     *Inner class which sets the Document for the AutoCompleteCoeusCombo*
     */
    public class ComboBoxDocument extends PlainDocument {
        public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException {
            if(str == null){
                return;
            }
            super.insertString(offset, str, a);
            autoComplete();
        }
    }//End of ComboBoxDocument class

}//End of AutoCompleteCoeusCombo class
