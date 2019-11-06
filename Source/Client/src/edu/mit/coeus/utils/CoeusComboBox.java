/*
 * @(#)CoeusComboBox.java 08/29/2002, 5:06 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;
import javax.swing.event.*;
import java.util.Vector;
import javax.swing.border.*;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.gui.CoeusFontFactory;


import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


//import edu.mit.coeus.organizationmaint.bean.*;

/** 
 * <code> CoeusComboBox </code> is a Custom combobox that is required to show 
 * multi columns in a combobox popup.
 *
 * @author  RaYaKu
 * @date Aug,29,2002
 * @since 1.0
 */
public class CoeusComboBox extends JComboBox implements JComboBox.KeySelectionManager{
   
   private boolean editable = false;
   protected int popupWidth;
   
   private Color popupSelBackground  ;
   private Color popupBackGround;
   private Color editorComboBackground;
   private Vector items;
   private JList internalList;
   private boolean showCode;
    /**
     * Initializes the properties of CoeusComboBox as it needs to set custom Renderers
     * and Editors for CoeusComboBox
     */
    private void initProperties(){        
        CoeusComboBoxRenderer renderer= new CoeusComboBoxRenderer();
        //CoeusComboCellEditor editor = new CoeusComboCellEditor();
        setBorder(new LineBorder(Color.black,1));
        setRenderer(renderer);
        //setEditor(editor);  // implemented for editable combo
        //setEditable(true);
        setFont(CoeusFontFactory.getNormalFont());
        popupWidth = 0;
         //Added for Case#3893 - Java 1.5 issues - Start
        setKeySelectionManager(this);
        //Case#3893 - End
    }
  
    /** Creates a new instance of CoeusComboBox */
    public CoeusComboBox() {
        super();
        setUI(new StateComboBoxUI());
        initProperties();        
    }
    
    /**
     * Constructor with default ComboBoxModel
     * @param aModel represent the ComboBoxModel.
     */
    public CoeusComboBox(ComboBoxModel aModel) {
        super(aModel);
        setUI(new StateComboBoxUI());
        initProperties();
    }
    
    /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items represent the ComboBoxModel values as array of objects.
     */
    public CoeusComboBox(final Object[] items) {
        super(items);
        setUI(new StateComboBoxUI());        
        initProperties();
    }
    
    /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items An array of elements that are shown in popup
     * @param editable  Status of ComboBox, If true the combobox value can be modified.
     */
    public CoeusComboBox(final Object[] items, boolean editable) {
        super( items);        
        this.editable =editable;
     
        setEditable(editable);
     
        setUI(new StateComboBoxUI());        
        initProperties();
    }
    
    /**
     * Constructor with default items that are shown in popup when the combo is selected
     * @param items a Vector of combobox items.
     */
    public CoeusComboBox(Vector items) {
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
    public CoeusComboBox(Vector items, boolean editable){
        super( items);        
        this.items = items;
        this.editable = editable;
     
        setEditable(editable);
     
        setUI(new StateComboBoxUI());        
        initProperties();
    }
    
// JM 7-21-2011 set background color to yellow to indicate required element
    /**
     * This will set the background color to yellow indicating a required element
     * @param boolean isRequired
     */
    public void setRequired(boolean isRequired) {
    	if (isRequired) {
    		setBackground(Color.YELLOW);
    	}
    }
// END  
    
    /**
     * Sets combobox Popup Background Color
     * @param color  background color of Combobox popup.
     */
    public void setPopupBackground( Color color){
        this.popupBackGround = color;
    }
    
    /**
     * Sets combobox Popup Selection Background Color
     * @param color Selected Item background color of Combobox popup. 
     */
    public void setPopupSelectionBackground( Color color){
        this.popupSelBackground = color;
    }
    
    /**
     * Sets combobox editor Background Color
     * @param color  background color of Combobox editor.
     */
    /*public void setComboEditorBackground( Color color){
        this.editorComboBackground = color;
    }
     */
    
    /**
     * Sets the Status(Editable/NonEditable) of combobox, Based on which the combo 
     * value can be changed. Default is not editable     *
     * @param flag  If true the combobox is editable.
     */
    public void setEditable( boolean flag){
        super.setEditable(flag);
        if( flag && !editable) {
            this.editable = flag;
            setEditor(new CoeusComboCellEditor());
        }
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
     * This method is used to Add new item to the Coeus combo box.
     * @param item Object instance represent the new value to be appended.
     */
    public void addItem(Object item){
        if(items==null)
            items = new Vector();
        /*if(item!=null)
            items.addElement((item instanceof ComboBoxBean)?
                item:new ComboBoxBean(item,""));
         */
        this.items.addElement(item);
        super.addItem(item);
    }
    
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
      public Dimension getPopupSize() {
        Dimension size = getSize();
        if (popupWidth < 1) popupWidth = size.width;
        return new Dimension(popupWidth, size.height);
      }
      public void setPopupWidth(int width) {
        popupWidth = width;
      }
      
    public void setPreferredSize(Dimension preferredSize ){
        super.setPreferredSize(preferredSize);
        //Modified with case 2970:CoeusComboBox.setPreferredSize(null) causes NPE
//        setPopupWidth(preferredSize.width);
        setPopupWidth(preferredSize == null ? 0 : preferredSize.width);
        //2970 End
    }
    public int getMaximumRowCount(){
        return 15;
    }
    
    //Added for Case#3893 - Java 1.5 issues - Start
    public int selectionForKey(char aKey,ComboBoxModel aModel) {
        int i,c;
        int currentSelection = -1;
        Object selectedItem = aModel.getSelectedItem();
        String v;
        String pattern;
        
        if ( selectedItem != null ) {
            for ( i=0,c=aModel.getSize();i<c;i++ ) {
                if ( selectedItem == aModel.getElementAt(i) ) {
                    currentSelection  =  i;
                    break;
                }
            }
        }
        
        pattern = ("" + aKey).toLowerCase();
        aKey = pattern.charAt(0);
        
        for ( i = ++currentSelection, c = aModel.getSize() ; i < c ; i++ ) {
            Object elem = aModel.getElementAt(i);
            if (elem != null && elem.toString() != null) {
                v = elem.toString().toLowerCase();
                if ( v.length() > 0 && v.charAt(0) == aKey )
                    return i;
            }
        }
        
        for ( i = 0 ; i < currentSelection ; i ++ ) {
            Object elem = aModel.getElementAt(i);
            if (elem != null && elem.toString() != null) {
                v = elem.toString().toLowerCase();
                if ( v.length() > 0 && v.charAt(0) == aKey )
                    return i;
            }
        }
        return -1;
    }
    //Case#3893 - End
    
    /**
     * An inner class to set the Default renderer for CoeusComboBox. As this combobox 
     * can show multiple columns in combo popup, so add number columns to this panel, like
     * that as many as number panel will be added to panel as many items available in combobox.
     * that is rendered in every cell.
     */    
    class CoeusComboBoxRenderer extends JLabel implements ListCellRenderer {
        /**
         * Constructor to set the layout and border for this panel.
         */
        public CoeusComboBoxRenderer() {
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
                    // Added by chandra. bug # 962 30th June 2004
                    //Ajay:Commented the 'if' since it was not allowing the Empty String
                    //Added new Code 
                    if(comboBoxBean.getDescription().trim().equals("")){
                        setText("");
                    }else/*if(!(comboBoxBean.getDescription().trim().equals("")))*/{
                        // Bug Fix # 1709 - start
//                        setText(comboBoxBean.getDescription() + "("+ comboBoxBean.getCode()+")" );
                        setText(comboBoxBean.getCode() + " : "+ comboBoxBean.getDescription());
                        // Bug Fix # 1709 - End
                        
                    }// Added by chandra. bug # 962 30th June 2004
                }else{
                    if( comboBoxBean.getDescription() != null ){
                        setText(comboBoxBean.getDescription().trim());
                    }
                }
            }
            internalList = list;
            return this;
        }
        
    }// end of CoeusComboBoxRenderer class
    
    /**
     * An inner class to set the editor of CoeusComboBox when it gets focus and about to be
     * selected by the user and this will be called only for editable combobox.
     */
    class CoeusComboCellEditor extends javax.swing.plaf.basic.BasicComboBoxEditor {
        /**
         * This will construct CoeusComboCellEditor with editor component 
         * Background color.
         */
        public CoeusComboCellEditor() {
            super();
            if(editorComboBackground != null){
                editor.setBackground(editorComboBackground);
            }
            /*
             * Method used to get description for the code entered 
             */
            ((JTextField) getEditorComponent()).addFocusListener(new FocusAdapter(){
                public void focusLost(FocusEvent fe){
                    if(!fe.isTemporary()){
                        String text = ((JTextField) getEditorComponent()).getText();
                        if(items!=null && text != null && text.length() > 0 ){
                            int cmbSize = items.size();
                            for(int cmbIndex=0;cmbIndex<cmbSize;cmbIndex++){
                                ComboBoxBean cmbBean = (ComboBoxBean)items.elementAt(cmbIndex);
                                if(cmbBean.getCode().trim().equalsIgnoreCase(text)){
                                    ((JTextField) getEditorComponent()).setText(cmbBean.getDescription());
                                    internalList.setSelectedIndex(cmbIndex);
                                    setSelectedItem(cmbBean);
                                    break;
                                }
                            }
                        }
                    }
                }
            });
            /* adding document listener to the editor component */
            JTextField tf;
            tf = (JTextField)getEditorComponent();
            if( tf != null ) {
                tf.setDocument(new CBDocument());
            }
        }
    }// end of CoeusComboCellEditor
    
    
//********* inserted    
  /**
   * Custom document which searches for the available list of elements for 
   * each and every character typed in the editor component of combobox
   */  
  class CBDocument extends PlainDocument {

    public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException {
      if(str == null) return;
      super.insertString(offset, str, a);
        JTextField tf = (JTextField)getEditor().getEditorComponent();
        String text = tf.getText();
        ComboBoxModel aModel = getModel();
        String current;
        for(int i = 0; i < aModel.getSize(); i++) {
          current = aModel.getElementAt(i).toString().toLowerCase();
          if(current.toLowerCase().startsWith(text.toLowerCase())){
              
              if( internalList != null && isPopupVisible()){
                  internalList.setSelectedIndex(i);
                  break;
              }
          }
        }
    }
  }
    
//********* end    
    
    
} // end of CoeusComboBox