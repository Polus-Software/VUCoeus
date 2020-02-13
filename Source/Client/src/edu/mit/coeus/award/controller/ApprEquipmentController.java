/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;
 
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.ucsd.coeus.personalization.controller.AbstractController;

/*
 * ApprEquipmentsController.java
 * Created on May 24, 2004, 9:38 AM
 * @author  ajaygm
 */

public class ApprEquipmentController extends AwardController implements ActionListener{
    
    /** Holds an instance of <CODE>CommentsHistoryForm</CODE> */
    private ApprEquipmentForm apprEquipmentForm;

    /**
     * To create an instance of MDIform
     */  
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    /**
     * Instance of the Dialog
     */ 
    private CoeusDlgWindow dlgApprEquipmentForm;
    
    /**
     * Instance of Coeus Message Resources
     */   
    private CoeusMessageResources coeusMessageResources;
    
     /**
     * Instance of Query Engine
     */
    private QueryEngine queryEngine;
    
    /*CoeusVector For setting seq no and award no*/
    private CoeusVector cvAwardDetailsBean;
    
    /*CoeusVectors of award base bean */
    private CoeusVector cvAwardBaseBean;
    
    /*CoeusVectors of Approved Equipment trip bean */
    private CoeusVector cvApprvdEqpBean;
    
    /*CoeusVectors of deleted beans */
    private CoeusVector cvDeletedItem;
    
    /*Table Model, editor and Renderer for Foreign trip table and total table*/
    private ApprEquipmentTableModel apprEquipmentTableModel;
    private ApprEquipmentEditor apprEquipmentEditor;
    private ApprEquipmentRenderer apprEquipmentRenderer;
    private AmountTabelModel amountTabelModel;
    private AmountTableCellRenderer amountTableCellRenderer;

    /*Creating the instance of awardDetailsBean*/
    private AwardDetailsBean awardDetailsBean = new AwardDetailsBean();
    
    /*Instance of Approved Equipment Bean*/
    private AwardApprovedEquipmentBean awardApprovedEquipmentBean;
    
    /*For assigning row ID*/
    private int rowId;
        
    private char functionType;    
    private static final String EMPTY_STRING = "";
    
     /*Validation messages*/
    private static final String DELETE_CONFIRMATION = "instPropIPReview_exceptionCode.1353";
    private static final String DUPLICATE_ROW = "approvedEquipmentControllerCode.1801";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    private static final String ITEM_VALIDATE = "approvedEquipmentControllerCode.1803";
    private static final String VENDOR_VALIDATE = "approvedEquipmentControllerCode.1802";
    private static final String MODEL_VALIDATE = "approvedEquipmentControllerCode.1804";
    
    /*TO check weather the data is modified*/
    private boolean modified = false;
    
    /*Column indexes for the table*/
    private static final int ITEM_INDEX = 0;
    private static final int VENDOR_INDEX = 1;
    private static final int MODEL_INDEX  = 2;
    private static final int AMOUNT_INDEX = 3;
    
    private static final int ITEM = 0;
    private static final int VENDOR = 1;
    private static final int MODEL = 2;
    private static final int AMOUNT = 3;
    
    private static final int TOTAL_INDEX=0;
    private static final int TOTAL_AMOUNT_INDEX=1;
    
    /*For setting the dimentions*/
    private static final String WINDOW_TITLE = "Approved Equipment";
    private static final int WIDTH = 610;
    private static final int HEIGHT =  330;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    public ApprEquipmentController (AwardBaseBean awardBaseBean, char funcType){
        super(awardBaseBean);
        queryEngine = QueryEngine.getInstance ();
        coeusMessageResources = CoeusMessageResources.getInstance();
        awardApprovedEquipmentBean = new AwardApprovedEquipmentBean ();
        registerComponents();
        postInitComponents();
        setFormData(awardBaseBean);
        setFunctionType(funcType);
        this.functionType = funcType;
    }
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgApprEquipmentForm = new CoeusDlgWindow(mdiForm);
        dlgApprEquipmentForm.setResizable(false);
        dlgApprEquipmentForm.setModal(true);
        dlgApprEquipmentForm.getContentPane().add(apprEquipmentForm);
        dlgApprEquipmentForm.setTitle(WINDOW_TITLE);
        dlgApprEquipmentForm.setFont(CoeusFontFactory.getLabelFont());
        dlgApprEquipmentForm.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgApprEquipmentForm.getSize();
        dlgApprEquipmentForm.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
         dlgApprEquipmentForm.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        dlgApprEquipmentForm.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    performCancelAction();
            }
        });
       
        dlgApprEquipmentForm.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgApprEquipmentForm.addWindowListener(new WindowAdapter(){
             public void windowOpening(WindowEvent we){
                 apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(0,0);
                 apprEquipmentForm.tblApprovedEquipment.editCellAt(0,0);
             }
             public void windowClosing(WindowEvent we){
                    performCancelAction();
             }
        });
     //code for disposing the window ends
    }
    
    /** Displays the Form which is being controlled.
    */
    public void display () {
    	//    	rdias - UCSD's coeus personalization - Begin
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(getControlledUI(),"GENERIC");
        persnref.customize_Form(apprEquipmentForm.awardHeaderForm,"GENERIC");
        //		rdias - UCSD's coeus personalization - End
    	
        if(apprEquipmentForm.tblApprovedEquipment.getRowCount ()>0){
            apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval (0,0);
        }
        dlgApprEquipmentForm.setVisible (true);
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields () {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            apprEquipmentForm.btnAdd.setEnabled(false);
            apprEquipmentForm.btnDelete.setEnabled(false);
            apprEquipmentForm.btnOk.setEnabled(false);
            apprEquipmentForm.tblApprovedEquipment.setEnabled(false);
        }
    }
    
    /** An overridden method of the controller
    * @return apprEquipmentForm returns the controlled form component
    */
    public Component getControlledUI () {
        return apprEquipmentForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData () {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents () {
        //Add listeners to all the buttons
        apprEquipmentForm = new ApprEquipmentForm();
        apprEquipmentForm.btnOk.addActionListener(this);
        apprEquipmentForm.btnCancel.addActionListener(this);
        apprEquipmentForm.btnAdd.addActionListener(this);
        apprEquipmentForm.btnDelete.addActionListener (this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { apprEquipmentForm.tblApprovedEquipment,
        apprEquipmentForm.btnOk,apprEquipmentForm.btnCancel,apprEquipmentForm.btnAdd,
        apprEquipmentForm.btnDelete
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        apprEquipmentForm.setFocusTraversalPolicy(traversePolicy);
        apprEquipmentForm.setFocusCycleRoot(true);
         
        /** Code for focus traversal - end */
        apprEquipmentEditor = new ApprEquipmentEditor();
        apprEquipmentRenderer = new ApprEquipmentRenderer();
        apprEquipmentTableModel = new ApprEquipmentTableModel();
        apprEquipmentForm.tblApprovedEquipment.setModel (apprEquipmentTableModel);
        
        amountTabelModel = new AmountTabelModel();
        amountTableCellRenderer = new AmountTableCellRenderer();
        apprEquipmentForm.tblApprovedEquipmentTotal.setModel(amountTabelModel);

        setTableEditors();
        
    }
    
    /** To set the default focus for the component
    */
    public void requestDefaultFocus(){
        if(getFunctionType()!= DISPLAY_MODE){
            if(apprEquipmentForm.tblApprovedEquipment.getRowCount ()>0){
               apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval (0,0);
               apprEquipmentForm.tblApprovedEquipment.setColumnSelectionInterval (0,0);
               apprEquipmentForm.tblApprovedEquipment.editCellAt (0,0);
               apprEquipmentForm.tblApprovedEquipment.getEditorComponent ().requestFocusInWindow ();
            }else{
                apprEquipmentForm.btnAdd.requestFocus();
            }
        }else{
            apprEquipmentForm.btnCancel.requestFocus();
        }
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData () {
        apprEquipmentEditor.stopCellEditing();
        try{
            CoeusVector cvTemp = new CoeusVector();
            if(modified) {
                if(cvDeletedItem!= null && cvDeletedItem.size() >0){
                    cvTemp.addAll(cvDeletedItem);
                }
                
                if(cvApprvdEqpBean!= null && cvApprvdEqpBean.size() >0){
                    cvTemp.addAll(cvApprvdEqpBean);
                }
                
                if(cvTemp!=null){
                    for(int index = 0; index < cvTemp.size(); index++){
                        AwardApprovedEquipmentBean bean = (AwardApprovedEquipmentBean)cvTemp.get(index);
                        if(bean.getAcType()!= null){
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                rowId = rowId+1;
                                bean.setRowId(rowId);
                                queryEngine.insert(queryKey, bean);
                             }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                if(bean.getRowId () == 0){
                                    rowId = rowId+1;
                                    bean.setRowId(rowId);
                                }
                                queryEngine.insert(queryKey, bean);
                            }
                        }
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        dlgApprEquipmentForm.dispose();
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData (Object data) {
        AwardBaseBean awardBaseBean= (AwardBaseBean)data;
        /*Create an instance of Coeus Vector to get
         *the Sponsor Award Number ,Award Number*/
        cvAwardDetailsBean = new CoeusVector();   
        cvAwardBaseBean = new CoeusVector();
        cvApprvdEqpBean = new CoeusVector();
        cvDeletedItem = new CoeusVector();
       // cvTempEquipBeans = new CoeusVector();
        try{
            
            /*For getting Award Details Bean to set Award No, Seq No, Sponsor No*/
            cvAwardDetailsBean = queryEngine.executeQuery (
            queryKey,AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            /*For getting Award Details Bean to setting data to table*/
            cvApprvdEqpBean = queryEngine.executeQuery (
            queryKey,AwardApprovedEquipmentBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            //Changed the code for getting the max row id.Since the earlier code 
            //while getting the rowid was not considering beans whose ACTYPE was "D".
            CoeusVector cvRowId = queryEngine.getDetails(queryKey,AwardApprovedEquipmentBean.class);
            if(cvRowId != null && cvRowId.size() > 0){
                cvRowId.sort ("rowId",false);
                AwardApprovedEquipmentBean equipmentBean = (AwardApprovedEquipmentBean) cvRowId.get(0);
                rowId = equipmentBean.getRowId ();
            }
            
        }catch (CoeusException coeusException){
            coeusException.printStackTrace ();
        }
        
            
        awardDetailsBean = (AwardDetailsBean)cvAwardDetailsBean.get(0);
        apprEquipmentForm.awardHeaderForm.setFormData (awardDetailsBean);
        //Case #2336 start
        apprEquipmentForm.awardHeaderForm.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
        //Case #2336 end
        apprEquipmentTableModel.setData (cvApprvdEqpBean);
        amountTabelModel.setData(cvApprvdEqpBean);
        amountTabelModel.fireTableDataChanged();
        if(apprEquipmentForm.tblApprovedEquipment.getRowCount() == 0){
            apprEquipmentForm.tblApprovedEquipmentTotal.setVisible (false);
        }else{
            apprEquipmentForm.tblApprovedEquipmentTotal.setVisible (true);
        }
    }
    
    
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate () throws CoeusUIException {
         apprEquipmentEditor.stopCellEditing();
         double cost = 0.0;
            for(int index=0 ; index < cvApprvdEqpBean.size() ; index++){
                AwardApprovedEquipmentBean equipBean = (AwardApprovedEquipmentBean)cvApprvdEqpBean.elementAt(index);
                
                if (cost==equipBean.getAmount() && (EMPTY_STRING.equals(equipBean.getItem()))
                && (EMPTY_STRING.equals(equipBean.getVendor())) && EMPTY_STRING.equals(equipBean.getModel()))
                {   
                    cvApprvdEqpBean.remove(index);
                    continue; 
                }
            
                if(EMPTY_STRING.equals(equipBean.getItem())){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey (
                    ITEM_VALIDATE));
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(index,index);
                    apprEquipmentForm.tblApprovedEquipment.setColumnSelectionInterval(0,0);
                    apprEquipmentForm.tblApprovedEquipment.editCellAt(index,ITEM);
                    setRequestFocusInThread(apprEquipmentEditor.txtComponent);
                    return false;
                }
                
                if(EMPTY_STRING.equals(equipBean.getVendor())) {
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey (VENDOR_VALIDATE));
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(index,index);
                    apprEquipmentForm.tblApprovedEquipment.setColumnSelectionInterval (1, 1);
                    apprEquipmentForm.tblApprovedEquipment.editCellAt(index,VENDOR);
                    setRequestFocusInThread(apprEquipmentEditor.txtComponent);
                    return false;
                }
                
               if(EMPTY_STRING.equals(equipBean.getModel())) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey (
                    MODEL_VALIDATE));
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(index,index);
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(index,index);
                    apprEquipmentForm.tblApprovedEquipment.editCellAt(index,MODEL);
                    setRequestFocusInThread(apprEquipmentEditor.txtComponent);
                    return false;
                }
            }
        boolean duplicate = checkDuplicateRow();
        return !duplicate;
    }
    /**
     * To make the class variable insatances to null
     */
    public void cleanUp() {
        apprEquipmentForm = null;
        dlgApprEquipmentForm = null;
        cvAwardDetailsBean = null;
        cvAwardBaseBean = null;
        cvApprvdEqpBean = null;
        cvDeletedItem = null;
        apprEquipmentTableModel = null;
        apprEquipmentEditor = null;
        apprEquipmentRenderer = null;
        amountTabelModel = null;
        amountTableCellRenderer = null;
        awardDetailsBean = null;
        awardApprovedEquipmentBean = null;
    }
    
    
    private boolean checkDuplicateRow(){
        apprEquipmentEditor.stopCellEditing();
        Equals itemEq,vendorEq,modelEq;
        CoeusVector coeusVector = new CoeusVector();
        And itemEqAndVendorEq,costEquals;
        if(cvApprvdEqpBean!=null && cvApprvdEqpBean.size() > 0){
            for(int index = 0; index < cvApprvdEqpBean.size(); index++){
                AwardApprovedEquipmentBean awardApprovedEquipmentBean = 
                        (AwardApprovedEquipmentBean)cvApprvdEqpBean.get(index);
                
                itemEq = new Equals("item", awardApprovedEquipmentBean.getItem());
                vendorEq = new Equals("vendor", awardApprovedEquipmentBean.getVendor());
                modelEq = new Equals("model", awardApprovedEquipmentBean.getModel());
                
                itemEqAndVendorEq = new And(itemEq, vendorEq);
                
                costEquals = new And(itemEqAndVendorEq,modelEq);
                coeusVector = cvApprvdEqpBean.filter(costEquals);
                if(coeusVector.size()==-1){
                    return false;
                }
                
                if(coeusVector!=null && coeusVector.size() > 1){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey (
                    DUPLICATE_ROW));
                    apprEquipmentForm.tblApprovedEquipment.editCellAt(index,ITEM);
                    apprEquipmentForm.tblApprovedEquipment.requestFocus();
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(index,index);
                    apprEquipmentForm.tblApprovedEquipment.scrollRectToVisible(
                    apprEquipmentForm.tblApprovedEquipment.getCellRect(
                    index ,0, true));
                    modified = true;
                    return true;
                }
            }
        }
        return false;
    }
 
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent 
     */
    public void actionPerformed (ActionEvent e) {
        Object source = e.getSource();
        if(source.equals(apprEquipmentForm.btnOk)){
            try{
                if( validate() ){
                    saveFormData();
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
        
        if(source.equals(apprEquipmentForm.btnCancel)){
            performCancelAction();
        }
        if(source.equals(apprEquipmentForm.btnAdd)){
            performAddAction();
        }
        if(source.equals(apprEquipmentForm.btnDelete)){
            performDeleteAction();
        }
    }
    
    /*Sets the tabe editors renderers and width for each column of the table*/
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = apprEquipmentForm.tblApprovedEquipment.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            apprEquipmentForm.tblApprovedEquipment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            apprEquipmentForm.tblApprovedEquipment.setRowHeight(22);
            apprEquipmentForm.tblApprovedEquipment.setSelectionBackground(java.awt.Color.yellow);
            apprEquipmentForm.tblApprovedEquipment.setSelectionForeground(java.awt.Color.white);
            apprEquipmentForm.tblApprovedEquipment.setShowHorizontalLines(true);
            apprEquipmentForm.tblApprovedEquipment.setShowVerticalLines(true);
            apprEquipmentForm.tblApprovedEquipment.setOpaque(false);

            apprEquipmentForm.tblApprovedEquipment.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            
            apprEquipmentForm.tblApprovedEquipmentTotal.setShowHorizontalLines(false);
            apprEquipmentForm.tblApprovedEquipmentTotal.setShowVerticalLines(false);
            
            TableColumn column;
            //int minWidth[] = {30, 45, 90, 90, 90, 90, 90, 90};
            int prefWidth[] = {162, 162, 100, 100};
            for(int index = 0; index < prefWidth.length; index++) {
                column = apprEquipmentForm.tblApprovedEquipment.getColumnModel().getColumn(index);
                column.setPreferredWidth(prefWidth[index]);
                column.setCellRenderer(apprEquipmentRenderer);
                column.setCellEditor(apprEquipmentEditor);
            }
            
            TableColumn amountColumn = apprEquipmentForm.tblApprovedEquipmentTotal.getColumnModel().getColumn(TOTAL_INDEX);
            amountColumn.setPreferredWidth(575);
            amountColumn.setResizable(true);
            amountColumn.setCellRenderer(amountTableCellRenderer);


            amountColumn = apprEquipmentForm.tblApprovedEquipmentTotal.getColumnModel().getColumn(TOTAL_AMOUNT_INDEX);
            amountColumn.setPreferredWidth(125);
            amountColumn.setResizable(true);
            amountColumn.setCellRenderer(amountTableCellRenderer);
                        
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*Table model for Appr Equipment*/
    public class ApprEquipmentTableModel extends AbstractTableModel{
        //column names 
         CoeusVector cvTempEquipBeans = new CoeusVector();
         private String ColumnName[] = {"Item","Vendor","Model","Amount"};
         
         private Class colClass[] = {String.class,String.class,String.class,String.class};
         
         public int getColumnCount() {
             return ColumnName.length;
         }
         public Class getColumnClass(int columnIndex){
             return colClass[columnIndex];
         }
      
         public int getRowCount() {
             if (cvTempEquipBeans == null){
                 return 0;
             }else 
                 return cvTempEquipBeans.size();
         }
         
         public void setData(CoeusVector cvApprvdEqpBean){
            cvTempEquipBeans = cvApprvdEqpBean;
         }
         
         public String getColumnName(int col) {
             return ColumnName[col];           
         }
         
         public Object getValueAt(int rowIndex, int columnIndex) {
             awardApprovedEquipmentBean = (AwardApprovedEquipmentBean)cvTempEquipBeans.get(rowIndex);
             switch(columnIndex){
                 case ITEM_INDEX:
                 return awardApprovedEquipmentBean.getItem ();
                 case VENDOR_INDEX:
                     return awardApprovedEquipmentBean.getVendor ();
                 case MODEL_INDEX:
                     return awardApprovedEquipmentBean.getModel ();
                 case AMOUNT_INDEX:
                     double amount = awardApprovedEquipmentBean.getAmount ();
                     amountTabelModel.fireTableDataChanged();
                     return (""+amount );
                     
             }
            return EMPTY_STRING;
         }
         
         /**
          * Returns true if the cell at <code>rowIndex</code> and
          * <code>columnIndex</code>
          * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
          * change the value of that cell.
          *
          * @param	rowIndex	the row whose value to be queried
          * @param	columnIndex	the column whose value to be queried
          * @return	true if the cell is editable
          * @see #setValueAt
          */
         public boolean isCellEditable(int rowIndex, int columnIndex) {
             if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                 return false;
             }else{
                 return true;
             }
         }
         
         /**
          * Sets the value in the cell at <code>columnIndex</code> and
          * <code>rowIndex</code> to <code>aValue</code>.
          *
          * @param	aValue		 the new value
          * @param	rowIndex	 the row whose value is to be changed
          * @param	columnIndex 	 the column whose value is to be changed
          * @see #getValueAt
          * @see #isCellEditable
          */
         public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
             awardApprovedEquipmentBean = (AwardApprovedEquipmentBean)cvTempEquipBeans.get(rowIndex);
             boolean changed = false;
             switch(columnIndex){
                 case ITEM_INDEX:
                      if (!aValue.toString().trim().equals(awardApprovedEquipmentBean.getItem().trim())) {
                          if(awardApprovedEquipmentBean.getItem().trim().equals(aValue.toString())){
                              break;
                          }
                          awardApprovedEquipmentBean.setItem(aValue.toString().trim());
                          changed = modified = true;
                      }
                      break;
                      
                 case VENDOR_INDEX:
                     if (!aValue.toString().trim().equals(awardApprovedEquipmentBean.getVendor().trim())) {
                         if(awardApprovedEquipmentBean.getVendor().trim().equals(aValue.toString())){
                             break;
                         }
                          awardApprovedEquipmentBean.setVendor(aValue.toString().trim());
                          changed = modified = true;
                      }
                     break;
                     
                 case MODEL_INDEX:
                      if (!aValue.toString().trim().equals(awardApprovedEquipmentBean.getModel ().trim())) {
                          if(awardApprovedEquipmentBean.getModel().trim().equals(aValue.toString())){
                              break;
                          }
                          awardApprovedEquipmentBean.setModel(aValue.toString().trim());
                          changed = modified = true;
                      }
                     break;

                 case AMOUNT_INDEX:
                    double amount = Double.parseDouble(aValue.toString());
                    if(amount != awardApprovedEquipmentBean.getAmount()) {
                        awardApprovedEquipmentBean.setAmount (amount);
                        changed = modified = true;
                    }
                    break;
             }
             
             if(changed){
                if(awardApprovedEquipmentBean.getAcType() == null) {
                    awardApprovedEquipmentBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
         }//End setValueAt
         
    }//End Class Table Model
    
    /*Table cell renderer for Appr Equipment*/    
    class ApprEquipmentRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private JLabel lblText,lblAmount;
        
        public ApprEquipmentRenderer(){
            txtComponent = new CoeusTextField();
            txtAmount =  new DollarCurrencyTextField();
            lblText = new JLabel();
            lblAmount = new JLabel();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblText.setOpaque(true);
            lblAmount.setOpaque(true);
            lblAmount.setHorizontalAlignment(RIGHT);
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                
                case ITEM:
                case VENDOR:
                case MODEL:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case AMOUNT:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblAmount.setBackground(disabledBackground);
                        lblAmount.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblAmount.setBackground(java.awt.Color.YELLOW);
                        lblAmount.setForeground(java.awt.Color.black);
                    }else{
                        lblAmount.setBackground(java.awt.Color.white);
                        lblAmount.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtAmount.setText(EMPTY_STRING);
                        lblAmount.setText(txtAmount.getText());
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                        lblAmount.setText(txtAmount.getText());
                    }
                    return lblAmount;
            }
            return txtComponent;
        }
    }
    /*Table editor renderer for Appr Equipment*/    
    class ApprEquipmentEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private int column;
        
        public ApprEquipmentEditor() {
            txtComponent = new CoeusTextField();
            txtAmount = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
        }
        
        public Object getCellEditorValue() {
            switch(column){
                case ITEM:
                case VENDOR:
                case MODEL:
                    return txtComponent.getText();
                case AMOUNT:
                    return txtAmount.getValue();
            }
            return txtComponent;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case ITEM:
                    txtComponent.setDocument(new LimitedPlainDocument(100));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                    
                case VENDOR:
                case MODEL:
                    txtComponent.setDocument(new LimitedPlainDocument(50));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case AMOUNT:
                    if(value == null){
                        txtAmount.setValue(0.00);
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtAmount;
                    
            }
            return txtComponent;
        }
    }
    
    /*To add rows*/
    private void performAddAction() {
        if(cvApprvdEqpBean != null && cvApprvdEqpBean.size() > 0){
            apprEquipmentEditor.stopCellEditing();
        }
        
        double cost = 0.0;
        /*IF empty row are present then do not add a new row*/
        if (cvApprvdEqpBean.size()>0) {
            AwardApprovedEquipmentBean lastRowBean=(AwardApprovedEquipmentBean)cvApprvdEqpBean.elementAt(cvApprvdEqpBean.size()-1);
            if (cost==lastRowBean.getAmount() && (EMPTY_STRING.equals(lastRowBean.getItem()))
            && (EMPTY_STRING.equals(lastRowBean.getVendor())) && EMPTY_STRING.equals(lastRowBean.getModel()))
            { if(cvApprvdEqpBean.size()>0) {  
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval (cvApprvdEqpBean.size()-1,cvApprvdEqpBean.size()-1);
                    apprEquipmentForm.tblApprovedEquipment.setColumnSelectionInterval (0,0);
                    apprEquipmentForm.tblApprovedEquipment.editCellAt(cvApprvdEqpBean.size()-1,ITEM_INDEX);
                    
                    apprEquipmentForm.tblApprovedEquipment.getEditorComponent ().requestFocusInWindow ();
                }
                return;
            }
        }
        
        if(!apprEquipmentForm.tblApprovedEquipmentTotal.isVisible ()){
            apprEquipmentForm.tblApprovedEquipmentTotal.setVisible (true);
        }
        AwardApprovedEquipmentBean newRowBean = new AwardApprovedEquipmentBean();
        
        newRowBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        newRowBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        newRowBean.setItem(EMPTY_STRING);
        newRowBean.setVendor(EMPTY_STRING);
        newRowBean.setModel(EMPTY_STRING);
        newRowBean.setAmount(0.0);
        rowId = rowId+1;
        newRowBean.setRowId(rowId);
        
        
        newRowBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvApprvdEqpBean.add(newRowBean);
        apprEquipmentTableModel.fireTableRowsInserted(apprEquipmentTableModel.getRowCount(),
        apprEquipmentTableModel.getRowCount());
        
        int lastRow = apprEquipmentForm.tblApprovedEquipment.getRowCount()-1;
        if(lastRow >= 0){
            apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(lastRow,lastRow);
            apprEquipmentForm.tblApprovedEquipment.setColumnSelectionInterval (0,0);
            apprEquipmentForm.tblApprovedEquipment.scrollRectToVisible(
            apprEquipmentForm.tblApprovedEquipment.getCellRect(lastRow, ITEM , true));
            apprEquipmentForm.tblApprovedEquipment.editCellAt(lastRow,ITEM);
        apprEquipmentForm.tblApprovedEquipment.getEditorComponent ().requestFocusInWindow ();
        }
        
        
        if(apprEquipmentForm.tblApprovedEquipment.getRowCount() == 0){
            apprEquipmentForm.tblApprovedEquipment.setVisible(false);
        }else{
            apprEquipmentForm.tblApprovedEquipment.setVisible(true);
        }
    }
      
    /*TO delete rows*/
    private void performDeleteAction() {
        apprEquipmentEditor.stopCellEditing();
        int selectedRow = apprEquipmentForm.tblApprovedEquipment.getSelectedRow();
        if(selectedRow == -1){
            return;
        }

        if(selectedRow != -1 && selectedRow >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                AwardApprovedEquipmentBean deletedCostSharingBean = (AwardApprovedEquipmentBean)cvApprvdEqpBean.get(selectedRow);
                deletedCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);    
                cvDeletedItem.add(deletedCostSharingBean);
                if(cvApprvdEqpBean!=null && cvApprvdEqpBean.size() > 0){
                    cvApprvdEqpBean.remove(selectedRow);
                    apprEquipmentTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                }
                
                if(selectedRow >0){
                    apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(
                    selectedRow-1,selectedRow-1);
                    apprEquipmentForm.tblApprovedEquipment.setColumnSelectionInterval (0,0);
                    apprEquipmentForm.tblApprovedEquipment.scrollRectToVisible(
                    apprEquipmentForm.tblApprovedEquipment.getCellRect(
                    selectedRow -1 ,0, true));
                    apprEquipmentForm.tblApprovedEquipment.editCellAt(selectedRow,ITEM_INDEX);
                    setRequestFocusInThread(apprEquipmentEditor.txtComponent);
                }else{
                    if(apprEquipmentForm.tblApprovedEquipment.getRowCount()>0){
                        apprEquipmentForm.tblApprovedEquipment.setRowSelectionInterval(0,0);
                    }else{
                        apprEquipmentForm.tblApprovedEquipmentTotal.setVisible (false);
                    }
                }
            }
        }
    }
    
    /*Performs cancel action*/
    private void performCancelAction(){
        apprEquipmentEditor.stopCellEditing ();
        if(modified){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    setSaveRequired(true);
                    try{
                        if( validate() ){
                            saveFormData();
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                   dlgApprEquipmentForm.dispose();
                    break;
                default:
                    break;
            }
        }else{
            dlgApprEquipmentForm.dispose();
        }
    }
        
   /*Table model for Total*/
    public class AmountTabelModel extends AbstractTableModel {
        private String colName[] = {"Total: ", ""};
        private Class colClass[] = {String.class, Double.class};
        CoeusVector cvTempEquipBeans = new CoeusVector();
        public boolean isCellEditable(int row, int col){
            return false;
        }
        public int getColumnCount(){
            return colName.length;
        }
        
        public Class getColumnClass(int colIndex){
            return colClass[colIndex];
        }
        public int getRowCount(){
            return 1;
        }
        
        public void setData(CoeusVector cvApprvdEqpBean){
            cvTempEquipBeans = cvApprvdEqpBean;
        }
        public String getColumnName(int column){
            return colName[column];
        }
        
        public Object getValueAt(int row, int col) {
           double totalAmount = 0.00;
           String name = "Total: ";
           if(col == TOTAL_INDEX){
               return name;
           }
           if(col == TOTAL_AMOUNT_INDEX){
               totalAmount = cvTempEquipBeans.sum("amount");
               return new Double(totalAmount);
           }
            return EMPTY_STRING;
        }
    }
        
    /*Table cell renderer for amount*/
    public class AmountTableCellRenderer extends DefaultTableCellRenderer 
    implements TableCellRenderer {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtCurrencyComponent;
        
        public AmountTableCellRenderer(){
            txtComponent = new JTextField();
            txtCurrencyComponent = new DollarCurrencyTextField();
            txtComponent.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtComponent.setHorizontalAlignment(JTextField.RIGHT);
            txtComponent.setForeground(java.awt.Color.BLACK);
            txtComponent.setFont(CoeusFontFactory.getLabelFont());
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtCurrencyComponent.setBackground(
                        (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtCurrencyComponent.setForeground(java.awt.Color.BLACK);
            txtCurrencyComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
      public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            JLabel lblText = new JLabel();
            lblText.setHorizontalAlignment (RIGHT);
            switch(col){
                case TOTAL_INDEX:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText (txtComponent.getText ());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText (txtComponent.getText ());
                    }
                    return lblText;

                case TOTAL_AMOUNT_INDEX:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtCurrencyComponent.setText(EMPTY_STRING);
                        lblText.setText (txtCurrencyComponent.getText ());
                    }else{
                        txtCurrencyComponent.setValue(new Double(value.toString()).doubleValue());
                        lblText.setText (txtCurrencyComponent.getText ());
                    }
                    return lblText;
            }
            return lblText;
        }
    }
    
    
    /*For setting the focus inside the table cell*/
     private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
}       