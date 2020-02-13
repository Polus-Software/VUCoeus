/**
 * Copyright (c) Massachusetts Institute of Technology
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

import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.ucsd.coeus.personalization.controller.AbstractController;

/**
 *
 * @author  surekhan
 */

/**
 * Creates an instance of AwardCostSharingController
 */

public class AwardCostSharingController extends AwardController implements ActionListener , MouseListener {
    
    private AwardCostSharingForm awardCostSharingForm;
    private CoeusDlgWindow dlgAwardCostSharing;
    private CoeusAppletMDIForm mdiForm;
    private QueryEngine queryEngine;
    //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//    private static final int WIDTH = 660;
//    private static final int HEIGHT = 350;
    private static final int WIDTH = 760;
    private static final int HEIGHT = 400;
    private int accountNumberMaxLength = 0;
    //Case#2402 - End
    private CostSharingTableModel costSharingTableModel;
    private CostSharingEditor costSharingEditor;
    private CostSharingRenderer costSharingRenderer;
    private AmountTableModel amountTableModel;
    private AwardDetailsBean awardDetailsBean;
    private AwardCommentsBean commentsBean;
    private CoeusParameterBean coeusParameterBean;
    private AmountTableCellRenderer amountTableCellRenderer;
    private CoeusMessageResources coeusMessageResources;
    private static final String WINDOW_TITLE  =  "Cost Sharing ";
    private static final int PERCENTAGE = 0;
    private static final int TYPE = 1;
    private static final int FISCAL_YEAR = 2;
    private static final int SOURCE_ACCOUNT = 3;
    private static final int DESTINATION_ACCOUNT = 4;
    private static final int AMOUNT = 5;
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    private static final String EMPTY_STRING = "";
    private boolean modified = false;
    private static final int TOTAL_COLUMN = 0;
    private static final int TOTAL_AMOUNT_COLUMN = 1;
    private JComboBox cmbType;
    /** Are you sure you  want to delete this row?
     */
    private static final String DELETE_CONFIRMATION = "award_exceptionCode.1009";
    
    /**Enter the cost sharing type
     */
    private static final String COST_TYPE = "awardCostsharing_exceptionCode.1601";
    
    /** Enter a valid fisacl year
     */
    private static final String FISCAL_MSG = "awardCostsharing_exceptionCode.1602";
    
    /** Enter Source Account Number
     */
    private static final String SOURCE_NUMBER = "awardCostsharing_exceptionCode.1603";
    
    /** Enter DestinationAccount Number
     **/
    private static final String DESTINATION_NUMBER = "awardCostsharing_exceptionCode.1604";
    
    /** Souce and destination account should differ
     **/
    private static final String ACCOUNT_DIFFER = "awardCostsharing_exceptionCode.1605";
    
    /** Either remove all the spaces or enter text
     **/
    private static final String REMOVE_SPACES = "awardCostsharing_exceptionCode.1606";
    
    /** Duplicate row*/
    private static final String DUPLICATE_ROW = "awardCostsharing_exceptionCode.1607";
    
   // private int rowId;
    private boolean isCommentPresent=false;
    private CoeusVector cvCostSharing;
    private CoeusVector cvDeletedItem;
    private CoeusVector cvCostType;
    private CoeusVector cvData;
    private CoeusVector cvComments;
    private char functionType;
    private CoeusVector cvCommentDescription;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    
     //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    /** Creates a new instance of AwardCostSharingController */
    public AwardCostSharingController(AwardBaseBean awardBaseBean,
    CoeusAppletMDIForm mdiForm, char functionType) {
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        this.functionType = functionType;
        costSharingTableModel = new CostSharingTableModel();
        costSharingEditor = new CostSharingEditor();
        costSharingRenderer = new CostSharingRenderer();
        cvCostSharing = new CoeusVector();
        cvDeletedItem = new CoeusVector();
        cvCostType = new CoeusVector();
        cvData = new CoeusVector();
        awardDetailsBean = new AwardDetailsBean();
        queryEngine = QueryEngine.getInstance();
        postInitComponents();
        setFormData(null);
        registerComponents();
        formatFields();
        setFunctionType(functionType);
        setTableEditors();
        setDefaultFocusInWindow();
        
    }
    
    
    private void postInitComponents() {
        
        awardCostSharingForm = new AwardCostSharingForm();
        dlgAwardCostSharing = new CoeusDlgWindow(mdiForm);
        dlgAwardCostSharing.setResizable(false);
        dlgAwardCostSharing.setModal(true);
       
        dlgAwardCostSharing.getContentPane().add(awardCostSharingForm);
        dlgAwardCostSharing.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardCostSharing.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardCostSharing.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardCostSharing.getSize();
        dlgAwardCostSharing.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        try{
            cvData = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            awardDetailsBean = (AwardDetailsBean)cvData.get(0);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        
        try{
            cvCostType = queryEngine.getDetails(queryKey, KeyConstants.COST_SHARING_TYPES);
            cvCostType.sort("description");
            
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
        dlgAwardCostSharing.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        
        dlgAwardCostSharing.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        //code for disposing the window ends
        
        
    }
    
    /** Diaplays the form*/
    public void display() {
    	//    	rdias - UCSD's coeus personalization - Begin
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(getControlledUI(),"GENERIC");
        persnref.customize_Form(awardCostSharingForm.awardHeaderForm1,"GENERIC");
        //		rdias - UCSD's coeus personalization - End
        int rowCount = awardCostSharingForm.tblCostSharing.getRowCount();
        if(rowCount != 0){
            awardCostSharingForm.tblCostSharing.setRowSelectionInterval(0,0);
          
        }
        if(rowCount == 0){
            awardCostSharingForm.tblAmount.setVisible(false);
        }
        if(cvCostSharing.size() > 0){
            awardCostSharingForm.tblCostSharing.editCellAt(0,0);
            costSharingEditor.setFocus(PERCENTAGE);
        }
        dlgAwardCostSharing.show();
        
    }
    /**
     * To remove the instances and the listenrs
     */
    public void cleanUp () {
        awardCostSharingForm = null;
        dlgAwardCostSharing = null;
        costSharingTableModel = null;
        costSharingEditor = null;
        costSharingRenderer = null;
        amountTableModel = null;
        awardDetailsBean = null;
        commentsBean = null;
        coeusParameterBean = null;
        amountTableCellRenderer = null;
        cmbType = null;
        cvCostSharing = null;
        cvDeletedItem = null;
        cvCostType = null;
        cvData = null;
        cvComments = null;
        cvCommentDescription = null;
    }
    
    /**
     * Registers listener and other components
     */
    
    public void registerComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {awardCostSharingForm.txtArComments,
        awardCostSharingForm.btnOK ,
        awardCostSharingForm.btnCancel,
        awardCostSharingForm.btnAdd,
        awardCostSharingForm.btnDelete};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardCostSharingForm.setFocusTraversalPolicy(traversePolicy);
        awardCostSharingForm.setFocusCycleRoot(true);
        
        awardCostSharingForm.btnAdd.addActionListener(this);
        awardCostSharingForm.btnCancel.addActionListener(this);
        awardCostSharingForm.btnDelete.addActionListener(this);
        awardCostSharingForm.btnOK.addActionListener(this);
        
        costSharingRenderer = new CostSharingRenderer();
        costSharingTableModel = new CostSharingTableModel();
        awardCostSharingForm.tblCostSharing.setModel(costSharingTableModel);
                awardCostSharingForm.tblCostSharing.setAutoResizeMode(
                awardCostSharingForm.tblCostSharing.AUTO_RESIZE_OFF);
        amountTableCellRenderer = new AmountTableCellRenderer();
        amountTableModel = new AmountTableModel();
        awardCostSharingForm.tblAmount.setModel(amountTableModel);
        
        awardCostSharingForm.tblCostSharing.getTableHeader().addMouseListener(this);
        dlgAwardCostSharing.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setDefaultFocusInWindow();
                
            }
        });
        
    }
    
    /** To set the default focus for the component
     */
    public void setDefaultFocusInWindow(){
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            awardCostSharingForm.btnCancel.requestFocusInWindow();
            
        }else{
            awardCostSharingForm.txtArComments.requestFocusInWindow();
            awardCostSharingForm.tblCostSharing.requestFocusInWindow();
            awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
        }
    }
        
        
        /**
         * perform field formatting.
         * enabling, disabling components depending on the
         * function type.
         */
        public void formatFields() {
            boolean enabled = getFunctionType() != DISPLAY_MODE ? true : false ;
            awardCostSharingForm.btnAdd.setEnabled(enabled);
            awardCostSharingForm.btnDelete.setEnabled(enabled);
            awardCostSharingForm.btnOK.setEnabled(enabled);
            awardCostSharingForm.tblAmount.setEnabled(enabled);
            awardCostSharingForm.tblCostSharing.setEnabled(enabled);
            awardCostSharingForm.txtArComments.setEnabled(enabled);
            
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                
                java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
                awardCostSharingForm.tblAmount.setBackground(bgColor);
                awardCostSharingForm.tblAmount.setSelectionBackground(bgColor);
                
                awardCostSharingForm.tblCostSharing.setBackground(bgColor);
                awardCostSharingForm.tblCostSharing.setSelectionBackground(bgColor);
                
                awardCostSharingForm.txtArComments.setBackground(bgColor);
                awardCostSharingForm.txtArComments.setDisabledTextColor(Color.BLACK);
                
                
            }
        }
        
        /** An overridden method of the controller
         * @return SpecialRateForm returns the controlled form component
         */
        public java.awt.Component getControlledUI()  {
            return awardCostSharingForm;
        }
        
        /** Returns the form data
         * @return returns the form data
         */
        public Object getFormData() {
            return awardCostSharingForm;
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            costSharingEditor.stopCellEditing();
            if(source.equals(awardCostSharingForm.btnAdd)){
                performAddAction();
            }else if(source.equals(awardCostSharingForm.btnDelete)){
                performDeleteAction();
            }else if(source.equals(awardCostSharingForm.btnCancel)){
                performCancelAction();
            }else if(source.equals(awardCostSharingForm.btnOK)){
                try{
                    if( validate() ){
                        saveFormData();
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                
            }
            
        }
        
        /* to dispose the window*/
        private void close(){
            dlgAwardCostSharing.dispose();
        }
        
        
        public void mouseClicked(MouseEvent mouseEvent) {
        costSharingEditor.stopCellEditing();    
        Point clickedPoint = mouseEvent.getPoint();
        int xPosition = (int)clickedPoint.getX();
        int columnIndex = awardCostSharingForm.tblCostSharing.getColumnModel().getColumnIndexAtX(xPosition);
        switch (columnIndex) {
            case PERCENTAGE:
                if(sortCodeAsc) {
                    //Code already sorted in Ascending order. Sort now in Descending order.
                    cvCostSharing.sort("costSharingPercentage", false);
                    sortCodeAsc = false;
                }else {
                    //Code already sorted in Descending order. Sort now in Ascending order.
                    cvCostSharing.sort("costSharingPercentage", true);
                    sortCodeAsc = true;
                }
                break;
            case TYPE:
                if(sortDescAsc){
                    cvCostSharing.sort("costSharingType",false);
                    sortDescAsc = false;
                }else {
                    cvCostSharing.sort("costSharingType",true);
                    sortDescAsc = true;
                }
                break;
            case FISCAL_YEAR:
                if(sortDescAsc){
                    cvCostSharing.sort("fiscalYear",false);
                    sortDescAsc = false;
                }else {
                    cvCostSharing.sort("fiscalYear",true);
                    sortDescAsc = true;
                }
                break;
           case SOURCE_ACCOUNT:
                if(sortDescAsc){
                    cvCostSharing.sort("sourceAccount",false);
                    sortDescAsc = false;
                }else {
                    cvCostSharing.sort("sourceAccount",true);
                    sortDescAsc = true;
                }
                break;
           case DESTINATION_ACCOUNT:
                if(sortDescAsc){
                    cvCostSharing.sort("destinationAccount",false);
                    sortDescAsc = false;
                }else {
                    cvCostSharing.sort("destinationAccount",true);
                    sortDescAsc = true;
                }
                break;
           case AMOUNT:
                if(sortDescAsc){
                    cvCostSharing.sort("amount",false);
                    sortDescAsc = false;
                }else {
                    cvCostSharing.sort("amount",true);
                    sortDescAsc = true;
                }
                break;     
        }//End Switch
        costSharingTableModel.fireTableDataChanged();
        awardCostSharingForm.tblCostSharing.setRowSelectionInterval(0 ,  0); 
        modified = false;

    }//End Mouse Click
        
        /* adds a new row to the table on the click of the Add button*/
        private void performAddAction() {
           if(cvCostSharing != null && cvCostSharing.size() > 0){
                costSharingEditor.stopCellEditing();
            }
            
            
            AwardCostSharingBean newBean = new AwardCostSharingBean();
            newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            newBean.setAmount(0.00);
            newBean.setCostSharingPercentage(.00);
            newBean.setCostSharingType(0);
            newBean.setDestinationAccount(EMPTY_STRING);
            newBean.setFiscalYear(EMPTY_STRING);
            newBean.setSourceAccount(EMPTY_STRING);
            newBean.setAcType(TypeConstants.INSERT_RECORD);
            modified = true;
            cvCostSharing.add(newBean);
            costSharingTableModel.fireTableRowsInserted(costSharingTableModel.getRowCount(),
            costSharingTableModel.getRowCount());
            
            int lastRow = awardCostSharingForm.tblCostSharing.getRowCount()-1;
            if(lastRow >= 0){
                awardCostSharingForm.tblCostSharing.setRowSelectionInterval(lastRow,lastRow);
                awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                awardCostSharingForm.tblCostSharing.editCellAt(lastRow,PERCENTAGE);
                awardCostSharingForm.tblCostSharing.getEditorComponent().requestFocusInWindow();
                
                
                awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                awardCostSharingForm.tblCostSharing.getCellRect(lastRow, PERCENTAGE, true));
                
            }
            if(awardCostSharingForm.tblCostSharing.getRowCount() == 0){
                awardCostSharingForm.tblAmount.setVisible(false);
            }else{
                awardCostSharingForm.tblAmount.setVisible(true);
            }
            
        }
        
        /* Deletes the Selected row on the click of the Delete Button*/
        private void performDeleteAction() {
            costSharingEditor.stopCellEditing();
            int selectedRow = awardCostSharingForm.tblCostSharing.getSelectedRow();
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
                    AwardCostSharingBean deletedCostSharingBean = (AwardCostSharingBean)cvCostSharing.get(selectedRow);
                    cvDeletedItem.add(deletedCostSharingBean);
                    if (deletedCostSharingBean.getAcType() == null ||
                    deletedCostSharingBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                        cvDeletedItem.add(deletedCostSharingBean);
                    }
                    if(cvCostSharing!=null && cvCostSharing.size() > 0){
                        cvCostSharing.remove(selectedRow);
                        costSharingTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                        modified = true;
                        deletedCostSharingBean.setAcType(TypeConstants.DELETE_RECORD);
                        
                    }
                    if(selectedRow >0){
                        awardCostSharingForm.tblCostSharing.setRowSelectionInterval(
                        selectedRow-1,selectedRow-1);
                        awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                        awardCostSharingForm.tblCostSharing.getCellRect(
                        selectedRow -1 ,0, true));
                    }else{
                        if(awardCostSharingForm.tblCostSharing.getRowCount()>0){
                            awardCostSharingForm.tblCostSharing.setRowSelectionInterval(0,0);
                            awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(1,1);
                        }
                    }
                    
                    /*if the rows are zero the amount table is not visible*/
                    if(awardCostSharingForm.tblCostSharing.getRowCount() == 0){
                        awardCostSharingForm.tblAmount.setVisible(false);
                    }
                }
            }
        }
        
        
        /* to display the delete message*/
        private int showDeleteConfirmMessage(String msg){
            int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            return selectedOption;
        }
        
        /*The window disposes if nothing is modified else if modified saves and then disposes*/
        private void performCancelAction(){
            // COEUSQA-2358 'Do you want to save?' message in display mode of award cost sharing - Start
            // Do not display confirmation message in display mode
            if (getFunctionType() == TypeConstants.DISPLAY_MODE) {
                close();
            } else if ((modified) || isCommentsModified()) {
//            if ((modified) || isCommentsModified())
            // COEUSQA-2358 'Do you want to save?' message in display mode of award cost sharing - End
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
                        close();
                        break;
                    default:
                        break;
                }
            }else{
                close();
            }
            
        }
        
        /* to handle the modified comments*/
        private boolean isCommentsModified(){
            if( awardCostSharingForm.txtArComments.getText().trim().length() > 0 ){
                if ( commentsBean == null || commentsBean.getComments() == null ){
                    return true;
                }
            }else {
                if ( commentsBean == null || commentsBean.getComments() == null ){
                    return false;
                }

            }
            if( commentsBean.getComments() == null ){
                return false;
            }
            if( !awardCostSharingForm.txtArComments.getText().trim().equals(
            commentsBean.getComments()) ){
                return true;
            }else{
                return false;
            }
            
        }
        
        
        /* saves the form data*/
        public void saveFormData(){
            
            costSharingEditor.stopCellEditing();
            
            try{
                //Delete all the deleted beans from the query engine
                for( int index = 0; index < cvDeletedItem.size(); index++ ){
                    AwardCostSharingBean bean = (AwardCostSharingBean)cvDeletedItem.get(index);
                    queryEngine.delete(queryKey, bean);
                }
                
                for( int index = 0; index < cvCostSharing.size(); index++ ){
                    AwardCostSharingBean bean = (AwardCostSharingBean)cvCostSharing.get(index);
                    if( bean.getAcType() != null ){
                        if( bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                            //Delete the existing bean in the query engine
                            //got from the database and insert it with new rowId
                            AwardCostSharingBean existingBean = bean;
                            queryEngine.delete(queryKey, bean);
                            existingBean.setAcType(TypeConstants.INSERT_RECORD);
                            existingBean.setRowId(getExistingMaxId() + index + 1);
                            queryEngine.insert(queryKey, existingBean);
                        }else if( bean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                            //Set the rowId of the bean if not already set
                            if( bean.getRowId() == 0 ){
                                bean.setRowId(getExistingMaxId() + index + 1);
                            }
                            queryEngine.insert(queryKey, bean);
                        }
                    }
                }
                
                StrictEquals stCommentsEquals = new StrictEquals();
                if(commentsBean!= null){
                    commentsBean.setComments(awardCostSharingForm.txtArComments.getText());
                }else{
                    
                    commentsBean = new AwardCommentsBean();
                    commentsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                    commentsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                    commentsBean.setComments(awardCostSharingForm.txtArComments.getText().trim());
                }
                
                AwardCommentsBean queryCommentsBean = new AwardCommentsBean();
                CoeusVector cvTempComment = queryEngine.getDetails(queryKey, AwardCommentsBean.class);
                
                cvComments = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                CoeusVector cvCostSharingComment = null;
                CoeusParameterBean coeusParameterBean = null;
                CoeusVector cvCostSharingCommentCode = cvComments.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
                if(cvCostSharingCommentCode!=null && cvCostSharingCommentCode.size() > 0){
                    coeusParameterBean = (CoeusParameterBean)cvCostSharingCommentCode.elementAt(0);
                }
                
                if (cvTempComment!= null && cvTempComment.size()>0) {
                    if(coeusParameterBean!=null){
                        Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                        
                        cvCostSharingComment = cvTempComment.filter(equals);
                        if(cvCostSharingComment!=null && cvCostSharingComment.size() > 0){
                            queryCommentsBean = (AwardCommentsBean)cvCostSharingComment.elementAt(0);
                        }
                    }
                }
                
                if(commentsBean!= null){
                    if(! stCommentsEquals.compare(commentsBean, queryCommentsBean)){
                        //Data Changed. save to query Engine.
                        if(cvCostSharingComment==null || cvCostSharingComment.size() == 0){
                            if(coeusParameterBean != null){
                                commentsBean.setAcType(TypeConstants.INSERT_RECORD);
                                commentsBean.setCommentCode(Integer.parseInt(coeusParameterBean.getParameterValue()));
                                if (!EMPTY_STRING.equals(commentsBean.getComments())) {//For bug fix : 1209 - (if condition) - Jobin
									queryEngine.insert(queryKey, commentsBean);
								}
                            }
                        }else{
                            commentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                            queryEngine.update(queryKey, commentsBean);
                            
                            
                        }
                        BeanEvent beanEvent = new BeanEvent();
                        beanEvent.setBean(commentsBean);
                        beanEvent.setSource(this);
                        fireBeanUpdated(beanEvent);
                    }
                    
                }
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
            
            close();
        }
        
        /*To get the Max of the vector*/
        private int getExistingMaxId() {
            CoeusVector cvExistingRecords = new CoeusVector();
            int maxRowId = 0;
            try{
                cvExistingRecords = queryEngine.getDetails(queryKey, AwardCostSharingBean.class);
                cvExistingRecords.sort("rowId",false);
                if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                    AwardCostSharingBean bean = (AwardCostSharingBean)cvExistingRecords.get(0);
                    maxRowId = bean.getRowId();
                }else{
                    maxRowId = 0;
                }
            }catch (CoeusException coeusException){
                coeusException.getMessage();
            }
            return maxRowId;
        }
        
        
        /**
         * validate the form data/Form and returns true if
         * validation is through else returns false.
         */
        
        public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
            
            costSharingEditor.stopCellEditing();
            for(int index=0 ; index < cvCostSharing.size() ; index++){
                AwardCostSharingBean costBean = (AwardCostSharingBean)cvCostSharing.elementAt(index);
                if(costBean.getCostSharingType() == 0){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(COST_TYPE));
                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                    awardCostSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    awardCostSharingForm.tblCostSharing.editCellAt(index,TYPE);
                    awardCostSharingForm.tblCostSharing.getEditorComponent().requestFocusInWindow();
                    return false;
                }
               //  Modified for CoeusQA-1426 start
               //if(EMPTY_STRING.equals(costBean.getFiscalYear())) {
                String fiscalYear = costBean.getFiscalYear().trim();
                costBean.setFiscalYear(fiscalYear);
                if(EMPTY_STRING.equals(fiscalYear) || Integer.parseInt(fiscalYear)==0) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FISCAL_MSG));
                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                    awardCostSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    awardCostSharingForm.tblCostSharing.editCellAt(index,FISCAL_YEAR);
                    setRequestFocusInThread(costSharingEditor.txtComponent);
                    return false;
                }
//                if(Integer.parseInt(costBean.getFiscalYear())<1900 || Integer.parseInt(costBean.getFiscalYear())>=2500){
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(FISCAL_MSG));
//                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
//                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
//                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
//                    awardCostSharingForm.tblCostSharing.getCellRect(
//                    index ,0, true));
//                    awardCostSharingForm.tblCostSharing.editCellAt(index,FISCAL_YEAR);
//                    setRequestFocusInThread(costSharingEditor.txtComponent);
//                   return false;
//                } // Modified for CoeusQA-1426 end
                if(EMPTY_STRING.equals(costBean.getSourceAccount())){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SOURCE_NUMBER));
                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                    awardCostSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    awardCostSharingForm.tblCostSharing.editCellAt(index,SOURCE_ACCOUNT);
                    setRequestFocusInThread(costSharingEditor.txtComponent);
                    //setTableEditing(index, SOURCE_ACCOUNT);
                    return false;
                }
                if(costBean.getDestinationAccount() == null ||
                EMPTY_STRING.equals(costBean.getDestinationAccount().trim())){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DESTINATION_NUMBER));
                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                    awardCostSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    awardCostSharingForm.tblCostSharing.editCellAt(index,DESTINATION_ACCOUNT);
                    setRequestFocusInThread(costSharingEditor.txtComponent);
                    return false;
                }
                if(costBean.getSourceAccount().equals(costBean.getDestinationAccount())){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ACCOUNT_DIFFER));
                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                    awardCostSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    awardCostSharingForm.tblCostSharing.editCellAt(index,SOURCE_ACCOUNT);
                    setRequestFocusInThread(costSharingEditor.txtComponent);
                    return false;
                }
                if(costBean.getAmount() < 0){
                    CoeusOptionPane.showInfoDialog("Enter the amount");
                    awardCostSharingForm.tblCostSharing.setRowSelectionInterval(index,index);
                    awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(0,0);
                    awardCostSharingForm.tblCostSharing.scrollRectToVisible(
                    awardCostSharingForm.tblCostSharing.getCellRect(
                    index ,0, true));
                    awardCostSharingForm.tblCostSharing.editCellAt(index,AMOUNT);
                    setRequestFocusInThread(costSharingEditor.txtCurrency);
                    return false;
                }
            }
            
            if(checkDuplicateRow()) return false;
            
            String text = awardCostSharingForm.txtArComments.getText();
            int position = text.length();
            if(text.length() > 0 && text.trim().equals(EMPTY_STRING)) {
                //Comment Text contains only spaces
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REMOVE_SPACES));
                awardCostSharingForm.txtArComments.requestFocusInWindow();
                awardCostSharingForm.txtArComments.setCaretPosition(position);
                return false;
            }
            
            return true;
        }
        
        /** Check for the Duplication . If  Type,Fiscal Year,SourceAccount number and
         *DestinationAccount are same then throw the Duplication message. If any one of these are
         *different then accept the row
         */
        private boolean checkDuplicateRow(){
            costSharingEditor.stopCellEditing();
            Equals typeEquals,fiscalEquals,sourceEquals,destinationEquals;
            CoeusVector coeusVector = null;
            And typeEqualsAndfiscalEquals,sourceEqualsAndDestinationEquals,costEquals;
            if(cvCostSharing!=null && cvCostSharing.size() > 0){
                for(int index = 0; index < cvCostSharing.size(); index++){
                    AwardCostSharingBean awardCostSharingBean = ( AwardCostSharingBean )cvCostSharing.get(index);
                    typeEquals = new Equals("costSharingType", new Integer(awardCostSharingBean.getCostSharingType()));
                    fiscalEquals = new Equals("fiscalYear", awardCostSharingBean.getFiscalYear().trim());
                    sourceEquals = new Equals("sourceAccount", awardCostSharingBean.getSourceAccount().trim());
                    destinationEquals = new Equals("destinationAccount",awardCostSharingBean.getDestinationAccount().trim());
                    typeEqualsAndfiscalEquals = new And(typeEquals, fiscalEquals);
                    sourceEqualsAndDestinationEquals=new And(sourceEquals,destinationEquals);
                    costEquals = new And(typeEqualsAndfiscalEquals,sourceEqualsAndDestinationEquals);
                    coeusVector = cvCostSharing.filter(costEquals);
                    if(coeusVector.size()==-1)return false;
                    if(coeusVector!=null && coeusVector.size() > 1){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_ROW));
                        setTableEditing(index + 1,0);
                        modified = true;
                        return true;
                        
                    }
                }
            }
            return false;
        }
        
        private void setRequestFocusInThread(final Component component) {
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    component.requestFocusInWindow();
                }
            });
        }
        
        
        private void setTableEditing(int row,int column) {
            awardCostSharingForm.tblCostSharing.requestFocusInWindow();
            awardCostSharingForm.tblCostSharing.setRowSelectionInterval(row, row);
            awardCostSharingForm.tblCostSharing.setColumnSelectionInterval(1, 1);
            
            // saves the row and column when you enter a cell
            // in this case the values in prevRow and prevCol is set and
            // SwingUtilities.invokeLater() method is called to do the rest
            final int indexRow = row;
            final int indexColumn = column;
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run(){
                    awardCostSharingForm.tblCostSharing.requestFocusInWindow();
                    awardCostSharingForm.tblCostSharing.changeSelection(indexRow , indexColumn, false, false);
                    awardCostSharingForm.tblCostSharing.setEditingColumn(indexColumn);
                    awardCostSharingForm.tblCostSharing.editCellAt(indexRow ,indexColumn);
                    awardCostSharingForm.tblCostSharing.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    
                    Component editingComponent = awardCostSharingForm.tblCostSharing.getEditorComponent();
                    editingComponent.requestFocus();
                }  });
                
        }
        
        
        
        
        /**
         * Sets the data
         */
        
        public void setFormData(Object data) {
            CoeusVector cvParameters = new CoeusVector();
            AwardCostSharingBean awardCostSharingBean;
            
            try{
                cvCostSharing = queryEngine.executeQuery(queryKey,
                AwardCostSharingBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
                
                awardCostSharingForm.awardHeaderForm1.setFormData(awardDetailsBean);
                //Case #2336 start
                awardCostSharingForm.awardHeaderForm1.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
                //Case #2336 end
                cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
                
                //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                for (int index=0;index<cvParameters.size();index++) {
//                    CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvComments.elementAt(index);
//                    if(CoeusConstants.COST_SHARING_COMMENT_CODE.equals(coeusParameterBean.getParameterName())){
//                        isCommentPresent=true;
//                    }
//                }
                //To get COST_SHARING_COMMENT_CODE parameter
                if(cvParameters != null && cvParameters.size()>0){
                    CoeusVector cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
                    if(cvFiltered != null && cvFiltered.size()>0){
                        isCommentPresent=true;
                    }
                    //To get MAX_ACCOUNT_NUMBER_LENGTH parameter
                    cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                    if(cvFiltered != null
                            && cvFiltered.size() > 0){
                        CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
                        accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                    }
                    
                }
                //Case#2402 - End
                
                
                if (!isCommentPresent) {
                    awardCostSharingForm.txtArComments.setEnabled(false);
                }else {
                    awardCostSharingForm.txtArComments.setEnabled(true);
                    cvCommentDescription=new CoeusVector();
                    cvCommentDescription=queryEngine.getDetails(queryKey,AwardCommentsBean.class);
                    if (cvCommentDescription!= null && cvCommentDescription.size()>0) {
                        //CoeusVector return
                        CoeusVector cvCostSharingCommentCode = cvParameters.filter(new Equals("parameterName", CoeusConstants.COST_SHARING_COMMENT_CODE));
                        CoeusParameterBean coeusParameterBean = null;
                        coeusParameterBean = (CoeusParameterBean)cvCostSharingCommentCode.elementAt(0);
                        
                        Equals equals = new Equals("commentCode", new Integer(coeusParameterBean.getParameterValue()));
                        
                        cvCommentDescription = cvCommentDescription.filter(equals);
                        if(cvCommentDescription!=null && cvCommentDescription.size() > 0){
                            this.commentsBean=(AwardCommentsBean)cvCommentDescription.elementAt(0);
                            awardCostSharingForm.txtArComments.setText(this.commentsBean.getComments());
                        }
                    }
                }
                
                
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
            /*To set the title of the dialog*/
            dlgAwardCostSharing.setTitle(WINDOW_TITLE);
            
            
        }
        
        /* setting the renderers and sizes for the tables*/
        private void setTableEditors() {
            awardCostSharingForm.tblCostSharing.setRowHeight(22);
            
            JTableHeader tableHeader = awardCostSharingForm.tblCostSharing.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            awardCostSharingForm.tblCostSharing.setSelectionBackground(java.awt.Color.yellow);
            awardCostSharingForm.tblCostSharing.setSelectionForeground(java.awt.Color.black);
            awardCostSharingForm.tblCostSharing.setOpaque(false);
            awardCostSharingForm.tblCostSharing.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            awardCostSharingForm.tblAmount.setShowVerticalLines(false);
            
            TableColumn column = awardCostSharingForm.tblCostSharing.getColumnModel().getColumn(PERCENTAGE);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(80);
            column.setPreferredWidth(90);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(costSharingRenderer);
            column.setCellEditor(costSharingEditor);
            tableHeader.setReorderingAllowed(false);
            
            
            column = awardCostSharingForm.tblCostSharing.getColumnModel().getColumn(TYPE);
            column.setPreferredWidth(90);
            column.setResizable(true);
            column.setCellRenderer(costSharingRenderer);
            column.setCellEditor(costSharingEditor);
            tableHeader.setReorderingAllowed(false);
            
            column = awardCostSharingForm.tblCostSharing.getColumnModel().getColumn(FISCAL_YEAR);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(70);
            //Modified for CoeusQA-1426 start
//            column.setPreferredWidth(80);
            column.setPreferredWidth(100);
            //Modified for CoeusQA-1426 end
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(costSharingRenderer);
            column.setCellEditor(costSharingEditor);
            tableHeader.setReorderingAllowed(false);
            
            column = awardCostSharingForm.tblCostSharing.getColumnModel().getColumn(SOURCE_ACCOUNT);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(105);
            column.setPreferredWidth(155);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(costSharingRenderer);
            column.setCellEditor(costSharingEditor);
            tableHeader.setReorderingAllowed(false);
            
            
            column = awardCostSharingForm.tblCostSharing.getColumnModel().getColumn(DESTINATION_ACCOUNT);
            //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
            //column.setPreferredWidth(130);
            column.setPreferredWidth(155);
            //Case#2402 - End
            column.setResizable(true);
            column.setCellRenderer(costSharingRenderer);
            column.setCellEditor(costSharingEditor);
            tableHeader.setReorderingAllowed(false);
            
            column = awardCostSharingForm.tblCostSharing.getColumnModel().getColumn(AMOUNT);
            column.setPreferredWidth(96);
            column.setResizable(true);
            column.setCellRenderer(costSharingRenderer);
            column.setCellEditor(costSharingEditor);
            tableHeader.setReorderingAllowed(false);
            
            // Setting renderer and sizes for the amount related columns
            TableColumn amountColumn = awardCostSharingForm.tblAmount.getColumnModel().getColumn(TOTAL_COLUMN);
            
            amountColumn.setPreferredWidth(575);
            amountColumn.setResizable(true);
            amountColumn.setCellRenderer(amountTableCellRenderer);
            
            amountColumn = awardCostSharingForm.tblAmount.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
            
            amountColumn.setPreferredWidth(125);
            amountColumn.setResizable(true);
            amountColumn.setCellRenderer(amountTableCellRenderer);
            
        }
        
        public void mouseEntered(MouseEvent e) {
        }        
        
        public void mouseExited(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
        /**
         * Table model for the AwardCostsharing table
         */
        class CostSharingTableModel extends AbstractTableModel {
            
            // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
            //String colNames[] = {"Percentage", "Type", "Fiscal Year", "Source Account", "Destination Account", "Amount"};
            String colNames[] = {"Percentage", "Type", "Project Year", "Source Account", "Destination Account", "Amount"};           Class[] colTypes = new Class [] {String.class , String.class, String.class, String.class, String.class, String.class};
            // modified for COEUSQA-1426: Ability to enter data besides YYYY  end
            /* If the cell is editable,return true else return false*/
            public boolean isCellEditable(int row, int col){
                if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                    return false;
                }else{
                    
                    return true;
                }
            }
            /**
             * Returns the number of columns
             */
            public int getColumnCount() {
                return colNames.length;
            }
            
            /**
             * Returns the column class
             */
            public Class getColumnClass(int columnIndex) {
                return colTypes [columnIndex];
            }
            
            /* returns the column names*/
            public String getColumnName(int column) {
                return colNames[column];
            }
            
            /* returns the number of rows*/
            public int getRowCount() {
                if(cvCostSharing == null ){
                    return 0;
                }else{
                    return cvCostSharing.size();
                }
            }
            
            /* gets the value at a particular cell*/
            public Object getValueAt(int row, int column) {
                AwardCostSharingBean awardCostSharingBean = (AwardCostSharingBean)cvCostSharing.get(row);
                switch(column){
                    case PERCENTAGE:
                        return  new Double(awardCostSharingBean.getCostSharingPercentage());
                    case TYPE:
                        int costType =awardCostSharingBean.getCostSharingType();
                        CoeusVector filteredVector = cvCostType.filter(new Equals("code",""+costType));
                        if(filteredVector!=null && filteredVector.size() > 0){
                            ComboBoxBean comboBoxBean = null;
                            comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            return comboBoxBean;
                        }else{
                            return new ComboBoxBean("","");
                        }
                        
                    case FISCAL_YEAR:
                        return awardCostSharingBean.getFiscalYear();
                    case SOURCE_ACCOUNT:
                        return awardCostSharingBean.getSourceAccount();
                    case DESTINATION_ACCOUNT:
                        return awardCostSharingBean.getDestinationAccount();
                    case AMOUNT:
                        return new Double(awardCostSharingBean.getAmount());
                }
                return EMPTY_STRING;
            }
            
            
            /* Sets the value in the cell*/
            public void setValueAt(Object value, int row, int col){
                if(cvCostSharing == null) return;
                AwardCostSharingBean awardCostSharingBean = (AwardCostSharingBean)cvCostSharing.get(row);
                
                switch(col) {
                    case PERCENTAGE:
                        double percentage = Double.parseDouble(value.toString());
                        if( percentage != awardCostSharingBean.getCostSharingPercentage()) {
                            awardCostSharingBean.setCostSharingPercentage(percentage);
                            modified=true;
                        }
                        
                        break;
                    case TYPE:
                        if(value!=null && (!value.toString().equals(EMPTY_STRING))){
                            ComboBoxBean comboBoxBean = (ComboBoxBean)cvCostType.filter(new Equals("description", value.toString())).get(0);
                            int costType = Integer.parseInt(comboBoxBean.getCode());
                            if(costType != awardCostSharingBean.getCostSharingType()){
                                awardCostSharingBean.setCostSharingType(costType);
                                modified = true;
                            }
                        }
                        break;
                        
                    case FISCAL_YEAR:
                        if (!value.toString().trim().equals(awardCostSharingBean.getFiscalYear().trim())) {
                            awardCostSharingBean.setFiscalYear(value.toString().trim());
                            modified = true;
                        }
                        
                        break;
                    case SOURCE_ACCOUNT:
                        if (!value.toString().trim().equals(awardCostSharingBean.getSourceAccount().trim())) {
                            awardCostSharingBean.setSourceAccount(value.toString());
                            modified = true;
                        }
                        break;
                    case DESTINATION_ACCOUNT:
                        if(awardCostSharingBean.getDestinationAccount() != null){
                            if (!value.toString().trim().equals(awardCostSharingBean.getDestinationAccount().trim())) {
                                awardCostSharingBean.setDestinationAccount(value.toString());
                                modified = true;
                            }
                        }else {
                            awardCostSharingBean.setDestinationAccount(value.toString());
                            modified = true;
                        }
                        break;
                        
                    case AMOUNT:
                        double cost = 0.00;
                        cost = new Double(value.toString()).doubleValue();
                        if(cost != awardCostSharingBean.getAmount()) {
                            awardCostSharingBean.setAmount(cost);
                            amountTableModel.fireTableCellUpdated(TOTAL_COLUMN, TOTAL_AMOUNT_COLUMN);
                            modified = true;
                        }
                        break;
                }
                if(awardCostSharingBean.getAcType()== null){
                    awardCostSharingBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        
        
        /* Table Cell Editor for the AwardCostSharingTable*/
        class CostSharingEditor extends AbstractCellEditor implements TableCellEditor{
            private CoeusComboBox cmbType;
            private CoeusTextField txtComponent;
            //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
            private CoeusTextField txtAccountNumber;
            //COEUSDEV-415 : End
            private DollarCurrencyTextField txtCurrency;
            private CurrencyField txtPercentage;
            private int column;
            private boolean populated = false;
            
            /* Creates a CostSharing Editor*/
            public CostSharingEditor() {
                cmbType = new CoeusComboBox();
                txtComponent = new CoeusTextField();
                //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
                txtAccountNumber = new CoeusTextField();
                //COEUSDEV-415 : End
                txtCurrency = new DollarCurrencyTextField();
                txtCurrency = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,true);
                txtPercentage = new CurrencyField();
              
            }
            
            /* To populate the comboBox*/
            private void populateCombo() {
                int size = cvCostType.size();
                ComboBoxBean comboBoxBean;
                
                cmbType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
                for(int index = 0; index < size; index++) {
                    comboBoxBean = (ComboBoxBean)cvCostType.get(index);
                    cmbType.addItem(comboBoxBean);
                    
                }
               
            }
            
            /* Returns the CellEditor value*/
            public Object getCellEditorValue() {
                switch(column){
                    case PERCENTAGE:
                        return txtPercentage.getText();
                    case TYPE:
                        return cmbType.getSelectedItem();
                    case FISCAL_YEAR:
                        return txtComponent.getText();
                    case SOURCE_ACCOUNT:
                        //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
//                        return txtComponent.getText();
                        return txtAccountNumber.getText();
                        //COEUSDEV-415 : End
                    case DESTINATION_ACCOUNT:
                        //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
//                        return txtComponent.getText();
                        return txtAccountNumber.getText();
                        //COEUSDEV-415 : End
                    case AMOUNT:
                        //return txtCurrency.getValue();
                        return txtCurrency.getValue();
                }
                return cmbType;
            }
            
            
            /* returns the cellEditor component*/
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                this.column = column;
                switch(column){
                    case PERCENTAGE:
                        if(value== null){
                            txtPercentage.setText(EMPTY_STRING);
                        }else{
                            txtPercentage.setText(value.toString());
                        }
                        return txtPercentage;
                        
                    case TYPE:
                        if(! populated) {
                            populateCombo();
                            populated = true;
                        }
                        ComboBoxBean bean = (ComboBoxBean)value;
                        cmbType.setSelectedItem(bean);
                        return cmbType;
                        
                    case FISCAL_YEAR:
                        txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
                        if(value == null){
                            txtComponent.setText(EMPTY_STRING);
                        }else{
                            txtComponent.setText(value.toString());
                        }
                        return txtComponent;
                        
                    case SOURCE_ACCOUNT:
                        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
                        //Modified by shiji for bug fix id 1730 - start: step 1
//                        txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,7));
                        //bug fix id 1730 - end
//                        if(value == null){
//                            txtComponent.setText(EMPTY_STRING);
//                        }else{
//                            txtComponent.setText(value.toString());
//                        }
                        
                        //Sets the SourceAccountNumber length based on accountNumberMaxLength value and allow the field to
                        //accept alphanumeric with comma,hyphen and periods
                        txtAccountNumber.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                        String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                        //Checks if sourceAccountNumber is greater than account number length from parameter,
                        //then sourceAccountNumber is substring to accountNumberMaxLength
                        if(sourceAccountNumber.length() > accountNumberMaxLength){
                            sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                        }
                        txtAccountNumber.setText(sourceAccountNumber);
                        //Case#2402 - End
                        
                        return txtAccountNumber;
                        
                    case DESTINATION_ACCOUNT:
                        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                        if(value == null){
//                            txtComponent.setText(EMPTY_STRING);
//                        }else{
//                            txtComponent.setText(value.toString());
//                        }

                        //Sets the SourceAccountNumber length based on accountNumberMaxLength value and allow the field to
                        //accept alphanumeric with comma,hyphen and periods                        
                        txtAccountNumber.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                        String destinationAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                        //Checks if destinationAccountNumber is greater than account number length from parameter,
                        //then destinationAccountNumber is substring to accountNumberMaxLength
                        if(destinationAccountNumber.length() > accountNumberMaxLength){
                            destinationAccountNumber = destinationAccountNumber.substring(0,accountNumberMaxLength);
                        }
                        txtAccountNumber.setText(destinationAccountNumber);
                        //Case#2402 - End
                        return txtAccountNumber;
                        
                    case AMOUNT:

                        if(value== null){
                            txtCurrency.setValue(0.00);
                        }else{
                            txtCurrency.setValue(new Double(value.toString()).doubleValue());
                        }
                        return txtCurrency;
                        
                }
                return txtComponent;
            }//End getTableCellEditorComponent
            
            public void setFocus(int column) {
                switch (column) {
                    case PERCENTAGE:
                        txtPercentage.requestFocus();
                    case TYPE:
                        cmbType.requestFocus();
                    case FISCAL_YEAR:
                        txtComponent.requestFocus();
                    case SOURCE_ACCOUNT:
                        txtComponent.requestFocus();
                    case DESTINATION_ACCOUNT:
                        txtComponent.requestFocus();
                    case AMOUNT:
                        txtCurrency.requestFocus();
                }//End switch
            }//End setFocus
            
        }
        
        /**
         * Table cell renederer class for Cost sharing table
         */
        class CostSharingRenderer extends DefaultTableCellRenderer   implements TableCellRenderer {
            private CoeusTextField txtComponent;
            //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
            private CoeusTextField txtAccountNumber;
            //COEUSDEV-415 : End
            private JLabel lblCurrency;
            private DollarCurrencyTextField txtCurrency;
            private CurrencyField txtPercentage;
            private JLabel lblText,lblPercentage, lblAccountNumber;
            
            /* Creates a costSharing Renderer*/
            public CostSharingRenderer(){
                txtComponent = new CoeusTextField();
                //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
                txtAccountNumber = new CoeusTextField();
                lblAccountNumber = new JLabel();
                lblAccountNumber.setOpaque (true);
                //COEUSDEV-415 : End
                lblCurrency = new JLabel();
                lblText = new JLabel();
                lblPercentage = new JLabel();
                lblCurrency.setBackground(Color.WHITE);
                lblCurrency.setOpaque(true);
                lblCurrency.setHorizontalAlignment(JLabel.RIGHT);
                txtCurrency =  new DollarCurrencyTextField();
                txtPercentage = new CurrencyField();
                lblText.setOpaque (true);
                lblPercentage.setOpaque (true);
                //Added COEUSDEV-415 : Award Cost sharing window - CS type does not stick for first row - Start
                txtAccountNumber.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                //COEUSDEV-415 : End
                txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                txtCurrency.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                txtPercentage.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                lblPercentage.setHorizontalAlignment(RIGHT);
                
            }
            
            /* returns renderer component*/
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col){
                switch(col) {
                    case PERCENTAGE:
                        if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblPercentage.setBackground(disabledBackground);
                            lblPercentage.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblPercentage.setBackground(java.awt.Color.YELLOW);
                            lblPercentage.setForeground(java.awt.Color.black);
                        }else{
                            lblPercentage.setBackground(java.awt.Color.white);
                            lblPercentage.setForeground(java.awt.Color.black);
                        }
                        
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                            txtPercentage.setText(EMPTY_STRING);
                            lblPercentage.setText(txtPercentage.getText());
                        }else{
                            txtPercentage.setText(value.toString());
                            lblPercentage.setText(txtPercentage.getText());
                        }
                        return lblPercentage;
                    case TYPE:
                        if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblText.setBackground(disabledBackground);
                            lblText.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblText.setBackground(java.awt.Color.YELLOW);
                            lblText.setForeground(java.awt.Color.black);
                        }else{
                            lblText.setBackground(java.awt.Color.white);
                            lblText.setForeground(java.awt.Color.black);
                        }
                        
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                        return lblText;
                    case FISCAL_YEAR:
                        if(functionType == TypeConstants.DISPLAY_MODE ){
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
                    case SOURCE_ACCOUNT:
                        if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblAccountNumber.setBackground(disabledBackground);
                            lblAccountNumber.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblAccountNumber.setBackground(java.awt.Color.YELLOW);
                            lblAccountNumber.setForeground(java.awt.Color.black);
                        }else{
                            lblAccountNumber.setBackground(java.awt.Color.white);
                            lblAccountNumber.setForeground(java.awt.Color.black);
                        }
                        
                        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                	if(value != null) {
//                            txtComponent.setText(value.toString());
//                            lblText.setText(value.toString());
//                        } else {
//                            txtComponent.setText(EMPTY_STRING);
//                            lblText.setText(EMPTY_STRING);
//                        }
                        
                        //Sets the SourceAccountNumber length based on accountNumberMaxLength value and allow the field to
                        //accept alphanumeric with comma,hyphen and periods
                        txtAccountNumber.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                        String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                        //Checks if sourceAccountNumber is greater than account number length from parameter,
                        //then sourceAccountNumber is substring to accountNumberMaxLength
                        if(sourceAccountNumber.length() > accountNumberMaxLength){
                            sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                        }
                        txtAccountNumber.setText(sourceAccountNumber);
                        lblAccountNumber.setText(sourceAccountNumber);
                        //Case#2402 - End
                        return lblAccountNumber;
                        
                    case DESTINATION_ACCOUNT:
                        if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblAccountNumber.setBackground(disabledBackground);
                            lblAccountNumber.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblAccountNumber.setBackground(java.awt.Color.YELLOW);
                            lblAccountNumber.setForeground(java.awt.Color.black);
                        }else{
                            lblAccountNumber.setBackground(java.awt.Color.white);
                            lblAccountNumber.setForeground(java.awt.Color.black);
                        }
                        
                        //Modified for Case#2402- use a parameter to set the length of the account number throughout app - Start
//                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
//                            txtComponent.setText(EMPTY_STRING);
//                            lblText.setText(txtComponent.getText());
//                        }else{
//                            txtComponent.setText(value.toString());
//                            lblText.setText(txtComponent.getText());
//                        }
                        
                        //Sets the SourceAccountNumber length based on accountNumberMaxLength value and allow the field to
                        //accept alphaumeric with comma,hyphen and periods
                        txtAccountNumber.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                        String destinationAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING  : value.toString().trim();
                        //Checks if destinationAccountNumber is greater than account number length from parameter,
                        //then destinationAccountNumber is substring to accountNumberMaxLength
                        if(destinationAccountNumber.length() > accountNumberMaxLength){
                            destinationAccountNumber = destinationAccountNumber.substring(0,accountNumberMaxLength);
                        }
                        txtAccountNumber.setText(destinationAccountNumber);
                        lblAccountNumber.setText(destinationAccountNumber);
                        //Case#2402 - End
                        
                        return lblAccountNumber;
                    case AMOUNT:
                        //System.out.println("functiontype"+functionType);
                        if(functionType == TypeConstants.DISPLAY_MODE ){
                            lblPercentage.setBackground(disabledBackground);
                            lblPercentage.setForeground(java.awt.Color.BLACK);
                        }else if(isSelected){
                            lblPercentage.setBackground(java.awt.Color.YELLOW);
                            lblPercentage.setForeground(java.awt.Color.black);
                        }else{
                            lblPercentage.setBackground(java.awt.Color.WHITE);
                            lblPercentage.setForeground(java.awt.Color.black);
                        }
                        
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                          // lblCurrency.setText(EMPTY_STRING);
                            txtCurrency.setText(EMPTY_STRING);
                            lblPercentage.setText(txtCurrency.getText());
                        }else{
                            txtCurrency.setValue(new Double(value.toString()).doubleValue());
                             lblPercentage.setText(txtCurrency.getText());
//                            txtCurrency.setText(value.toString());
//                            lblCurrency.setText(txtCurrency.getText());
                      }
                         //return lblCurrency;
                        return lblPercentage;
                }
                return txtComponent;
            }
            
        }
        
        /* TableModel for amount table*/
        public class AmountTableModel extends AbstractTableModel{
            private String colName[] = { "Total Amount", ""};
            private Class colClass[] = {String.class, Double.class};
            
            
            /* returns true if cell is editable else returns false*/
            public boolean isCellEditable(int row, int col){
                return false;
            }
            
            /* returns number of columns*/
            public int getColumnCount(){
                return colName.length;
            }
            
            /* returnd column class*/
            public Class getColumnClass(int colIndex){
                return colClass[colIndex];
            }
            
            /* returns number of rows*/
            public int getRowCount(){
                return 1;
            }
            
            public void setData( ){
            }
            
            /* returns column names*/
            public String getColumnName(int column){
                return colName[column];
            }
            
            /* gets the value at the cell*/
            public Object getValueAt(int row, int col) {
                double totalAmount = 0.00;
                String name = "Total Amount:";
                if(col==TOTAL_COLUMN){
                    return name;
                }
                if(col==TOTAL_AMOUNT_COLUMN){
                    totalAmount = cvCostSharing.sum("amount");
                    return new Double(totalAmount);
                }
                return EMPTY_STRING;
            }
            
        }
        
        
        /**
         * Cell renderer for Amount Table
         */
        public class AmountTableCellRenderer extends DefaultTableCellRenderer
        implements TableCellRenderer {
            private CoeusTextField txtAmount;
            private DollarCurrencyTextField txtAmtValue;
            private JLabel lblText,lblValue;
            
            
            /**
             * Creates cell renedere for Amount table
             */
            public AmountTableCellRenderer(){
                txtAmount = new CoeusTextField();
                txtAmtValue = new DollarCurrencyTextField();
                lblText = new JLabel();
                lblText.setOpaque(true);
                lblValue = new JLabel();
                lblValue.setOpaque(true);
                lblText.setHorizontalAlignment(RIGHT);
                lblValue.setHorizontalAlignment(RIGHT);
                lblText.setFont(CoeusFontFactory.getLabelFont());
                txtAmount.setBackground(
                (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtAmount.setHorizontalAlignment(JTextField.RIGHT);
                txtAmount.setForeground(java.awt.Color.BLACK);
                txtAmount.setFont(CoeusFontFactory.getLabelFont());
                txtAmtValue.setBackground(
                (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                txtAmtValue.setForeground(java.awt.Color.BLACK);
                txtAmtValue.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
                
                
            }
            
            /**
             * returns table cell renderer component
             */
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col){
                switch(col) {
                    case TOTAL_COLUMN:
                        if(value == null && value.toString().trim().equals(EMPTY_STRING)){
                            txtAmount.setText(EMPTY_STRING);
                            lblText.setText(txtAmount.getText());
                        }else{
                            txtAmount.setText(value.toString());
                            lblText.setText(txtAmount.getText());
                        }
                        return lblText;
                    case TOTAL_AMOUNT_COLUMN:
                        if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                            txtAmtValue.setText(EMPTY_STRING);
                            lblValue.setText(txtAmtValue.getText());
                        }else{
                            txtAmtValue.setValue(new Double(value.toString()).doubleValue());
                            lblValue.setText(txtAmtValue.getText());
                        }
                        return lblValue;
                }
                return txtAmount;
            }
        }
    }
    
    
