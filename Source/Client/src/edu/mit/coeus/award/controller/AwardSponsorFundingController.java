/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardSponsorFundingController.java
 *
 * Created on June 2, 2004, 11:30 AM
 */

package edu.mit.coeus.award.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.*;

import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.sponsormaint.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.irb.bean.*;
import java.awt.Component;
import javax.swing.*;
import edu.ucsd.coeus.personalization.controller.AbstractController;

/**
 *
 * @author  surekhan
 */



public class AwardSponsorFundingController extends AwardController
implements ActionListener, MouseListener{
    
    private AwardSponsorFundingForm awardSponsorFundingForm;
    private AwardTransferingSponsorBean awardTransferingSponsorBean;
    private SponsorFundingTableModel sponsorFundingTableModel;
    private SponsorFundingEditor sponsorFundingEditor;
    private SponsorFundingRenderer sponsorFundingRenderer;
    private CoeusMessageResources coeusMessageResources;
    private AwardDetailsBean awardDetailsBean;
    private CoeusDlgWindow dlgAwardSponsorFunding;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private QueryEngine queryEngine;
    private static final int WIDTH = 560;
    private static final int HEIGHT = 340;
    private static final String WINDOW_TITLE = "Sponsor Funding Transferred";
    
    /** Are u sure u want to delete this row
     **/
    private static final String DELETE_CONFIRMATION = "award_exceptionCode.1009";
    
    /** The sponsorCode is invalid
     **/
    private static final String INVALID_SPONSOR_CODE = "awardSponsorFunding_exceptionCode.1951";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    private CoeusVector cvData;
    private CoeusVector cvSponsor;
    private CoeusVector cvDeletedItem;
    private static final String EMPTY_STRING = "";
    private static final int SPONSOR_CODE = 0;
    private static final int SPONSOR_NAME = 1;
    private boolean modified;
    private int rowId ;
    private boolean valueSet = true;
    
    
    /** Creates a new instance of AwardSponsorFundingController */
    public AwardSponsorFundingController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        initComponents();
        postInitComponents();
        registerComponents();
        formatFields();
        setFunctionType(functionType);
        setFormData(null);
        setTableEditors();
    }
    
    private void initComponents(){
        cvData = new CoeusVector();
        cvDeletedItem = new CoeusVector();
        cvSponsor = new CoeusVector();
        awardDetailsBean = new AwardDetailsBean();
        queryEngine = QueryEngine.getInstance();
        sponsorFundingTableModel = new SponsorFundingTableModel();
        sponsorFundingEditor = new SponsorFundingEditor();
        sponsorFundingRenderer = new SponsorFundingRenderer();
        awardSponsorFundingForm = new AwardSponsorFundingForm();
    }
    
    private void postInitComponents(){
        dlgAwardSponsorFunding = new CoeusDlgWindow(mdiForm);
        dlgAwardSponsorFunding.setResizable(false);
        dlgAwardSponsorFunding.setModal(true);
        dlgAwardSponsorFunding.getContentPane().add(awardSponsorFundingForm);
        dlgAwardSponsorFunding.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardSponsorFunding.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardSponsorFunding.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardSponsorFunding.getSize();
        dlgAwardSponsorFunding.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        try{
            cvData = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            awardDetailsBean = (AwardDetailsBean)cvData.get(0);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        
        dlgAwardSponsorFunding.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
        
        
        dlgAwardSponsorFunding.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                performCancelAction();
                
            }
        });
        //code for disposing the window ends
        
        dlgAwardSponsorFunding.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    /** to set the default focus on opening the window*/
    private void requestDefaultFocus(){
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardSponsorFundingForm.btnCancel.requestFocus();
        }else{
            awardSponsorFundingForm.btnCancel.requestFocus();
        }
    }
    
    /** Diaplays the form*/
    public void display() {
    	//    	rdias - UCSD's coeus personalization - Begin
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(getControlledUI(),"GENERIC");
        persnref.customize_Form(awardSponsorFundingForm.awardHeaderForm1,"GENERIC");
        //		rdias - UCSD's coeus personalization - End
    	
        int rowCount = awardSponsorFundingForm.tblSponsorFunding.getRowCount();
        if(rowCount != 0){
            awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(0,0);
        }
        dlgAwardSponsorFunding.show();
    }
    
    
    /**
     * perform field formatting.
     * enabling, disabling components depending on the
     * function type.
     */
    public void formatFields() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            awardSponsorFundingForm.btnAdd.setEnabled(false);
            awardSponsorFundingForm.btnCancel.setEnabled(true);
            awardSponsorFundingForm.btnDelete.setEnabled(false);
            awardSponsorFundingForm.btnOk.setEnabled(false);
            awardSponsorFundingForm.btnFind.setEnabled(false);
            awardSponsorFundingForm.tblSponsorFunding.setEnabled(false);
        }
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            awardSponsorFundingForm.tblSponsorFunding.setBackground(bgColor);
            awardSponsorFundingForm.tblSponsorFunding.setSelectionBackground(bgColor);
            
        }
    }
    
    
    /** An overridden method of the controller
     * @return SpecialRateForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return awardSponsorFundingForm;
    }
    
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return awardSponsorFundingForm;
    }
    
    
    /**
     * Registers listener and other components
     */
    public void registerComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[]  components = {awardSponsorFundingForm.btnCancel,
        awardSponsorFundingForm.btnFind,
        awardSponsorFundingForm.btnDelete,
        awardSponsorFundingForm.btnAdd,
        awardSponsorFundingForm.btnOk};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardSponsorFundingForm.setFocusTraversalPolicy(traversePolicy);
        awardSponsorFundingForm.setFocusCycleRoot(true);
        
        awardSponsorFundingForm.btnAdd.addActionListener(this);
        awardSponsorFundingForm.btnCancel.addActionListener(this);
        awardSponsorFundingForm.btnDelete.addActionListener(this);
        awardSponsorFundingForm.btnFind.addActionListener(this);
        awardSponsorFundingForm.btnOk.addActionListener(this);
        
        sponsorFundingTableModel = new SponsorFundingTableModel();
        awardSponsorFundingForm.tblSponsorFunding.setModel(sponsorFundingTableModel);
        sponsorFundingEditor = new SponsorFundingEditor();
        sponsorFundingRenderer = new SponsorFundingRenderer();
        
        awardSponsorFundingForm.tblSponsorFunding.addMouseListener(this);
        
    }
    
    
    
    
    /** To get the max rowId of the existing beans
     * @return cvExistingRecords.size() the count of existing beans
     */
    private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
        int maxRowId = 0;
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, AwardTransferingSponsorBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                AwardTransferingSponsorBean bean = (AwardTransferingSponsorBean)cvExistingRecords.get(0);
                maxRowId = bean.getRowId();
            }else{
                maxRowId = 0;
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        return maxRowId;
    }
    
    
    /** to dispose the window*/
    private void close(){
        dlgAwardSponsorFunding.dispose();
    }
    
    
    /**
     * Sets the data
     */
    public void setFormData(Object data) {
        cvSponsor = new CoeusVector();
        AwardTransferingSponsorBean awardTransferingSponsorBean;
        try{
            cvSponsor = queryEngine.executeQuery(queryKey,AwardTransferingSponsorBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvSponsor != null && cvSponsor.size() > 0){
                cvSponsor.sort("rowId",false);
                AwardTransferingSponsorBean bean = (AwardTransferingSponsorBean) cvSponsor.get(0);
                rowId = bean.getRowId();
            }
            
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
        dlgAwardSponsorFunding.setTitle(WINDOW_TITLE);
        awardSponsorFundingForm.awardHeaderForm1.setFormData(awardDetailsBean);
        //Case #2336 start
        awardSponsorFundingForm.awardHeaderForm1.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
        //Case #2336 end
        
        if(cvSponsor != null && cvSponsor.size() > 0){
            awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(0,0);
            awardTransferingSponsorBean = (AwardTransferingSponsorBean)cvSponsor.get(0);
            
        }
    }
    
    
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     */
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        sponsorFundingEditor.stopCellEditing();
        String sponsorCode, sponsorName = null;
        
        
        for(int index = 0; index < cvSponsor.size(); index++){
            // Commented for COEUSQA-1434 Add the functionality to set a status on a Sponsor record - Start
//            sponsorCode = (String)sponsorFundingTableModel.getValueAt(index, SPONSOR_CODE);
//            if(!sponsorCode.equals(EMPTY)) {
//                try{
//                    sponsorName = getSponsorName(sponsorCode).trim();
//                }catch (CoeusException coeusException) {
//                    coeusException.printStackTrace();
//                }
//            }
//            if(sponsorCode.equals(EMPTY)) {
//                return true;
//            }else if(sponsorName == null || sponsorName.equals(EMPTY)) {
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
//                awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(index,index);
//                awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(0,0);
//                awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
//                awardSponsorFundingForm.tblSponsorFunding.getCellRect(
//                index ,0, true));
//                awardSponsorFundingForm.tblSponsorFunding.editCellAt(index,SPONSOR_CODE);
//                setRequestFocusInThread(sponsorFundingEditor.txtComponent);
//                
//                return false;
//            }
            // Commented for COEUSQA-1434 Add the functionality to set a status on a Sponsor record - End
            if(!checkDuplicateRow()) return false;
        }
        return true;
    }
    
    
    /* to set the focus in the pertaining fields*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    
    /*to set the focus to that particular column*/
    //Added for Bug Fix:1207 by surekha start
    private void setRequestFocusInClassThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            //awardSponsorFundingForm.tblSponsorFunding.requestFocusInWindow();
            awardSponsorFundingForm.tblSponsorFunding.changeSelection( selrow, selcol, true, false);
            awardSponsorFundingForm.tblSponsorFunding.requestFocusInWindow();
            awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(selrow, selrow);
        }
        });
    } 
    
    
    /*
     * To make the class level instances as null
     */
    public void cleanUp() {
        awardSponsorFundingForm = null;
        awardTransferingSponsorBean = null;
        sponsorFundingTableModel = null;
        sponsorFundingEditor = null;
        sponsorFundingRenderer = null;
        awardDetailsBean = null;
        dlgAwardSponsorFunding = null;
        cvData = null;
        cvSponsor = null;
        cvDeletedItem = null;
    }
    
    /* to check the duplicate row in the window */
    private boolean checkDuplicateRow(){
        sponsorFundingEditor.stopCellEditing();
        CoeusVector coeusVector = null;
        Equals codeEquals;
        if(cvSponsor!=null && cvSponsor.size() > 0){
            for(int index = 0; index < cvSponsor.size(); index++){
                AwardTransferingSponsorBean dataBean = (AwardTransferingSponsorBean)cvSponsor.get(index);
                codeEquals = new Equals("sponsorCode" , dataBean.getSponsorCode());
                coeusVector = cvSponsor.filter(codeEquals);
                if(coeusVector!=null && coeusVector.size() > 1){
                    CoeusOptionPane.showErrorDialog("A row duplicates another");
                    awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(index,index);
                    awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(0,0);
                    awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
                    awardSponsorFundingForm.tblSponsorFunding.getCellRect(
                    index ,0, true));
                    awardSponsorFundingForm.tblSponsorFunding.editCellAt(index,SPONSOR_CODE);
                    setRequestFocusInThread(sponsorFundingEditor.txtComponent);
                    return false;
                }
            }
        }
        return true;
    }
    
    /* The actions performed on the click of
       the buttons*/
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        sponsorFundingEditor.stopCellEditing();
        if(source.equals(awardSponsorFundingForm.btnAdd)){
            performAddAction();
        }else if(source.equals(awardSponsorFundingForm.btnDelete)){
            performDeleteAction();
        }else if(source.equals(awardSponsorFundingForm.btnFind)){
            performFindSponsor();
            
        }else if(source.equals(awardSponsorFundingForm.btnOk)){
            sponsorFundingEditor.stopCellEditing();
            try{
                if(valueSet){
                    if( validate() ){
                        saveFormData();
                    }
                }else{
                    valueSet = true;
                }
                
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }else if(source.equals(awardSponsorFundingForm.btnCancel)){
            performCancelAction();
        }
    }
    
    /** adds a new row to the table on the click of the add button*/
    private void performAddAction(){
        if(cvSponsor != null && cvSponsor.size() > 0){
            sponsorFundingEditor.stopCellEditing();
        }
        
        int rowCount =  awardSponsorFundingForm.tblSponsorFunding.getRowCount();
        if(rowCount  > 0){
            if(cvSponsor!= null && cvSponsor.size() > 0){
                for(int index =0 ; index < cvSponsor.size(); index++){
                    AwardTransferingSponsorBean bean = (AwardTransferingSponsorBean)cvSponsor.get(index);
                    // If the sponsorCode or Name columns are empty then don't add the row.If any one of the
                    // field is entered then allow the user to add a new Row.
                    if(bean.getSponsorCode().trim().equals(EMPTY_STRING) && bean.getSponsorName().trim().equals(EMPTY_STRING)) return ;
                }
            }
        }
        
        AwardTransferingSponsorBean newBean = new AwardTransferingSponsorBean();
        newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        newBean.setSponsorCode(EMPTY_STRING);
        newBean.setSponsorName(EMPTY_STRING);
        rowId = rowId + 1;
        newBean.setRowId(rowId);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvSponsor.add(newBean);
        sponsorFundingTableModel.fireTableRowsInserted(sponsorFundingTableModel.getRowCount(),
        sponsorFundingTableModel.getRowCount());
        
        int lastRow = awardSponsorFundingForm.tblSponsorFunding.getRowCount()-1;
        if(lastRow >= 0){
            awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(lastRow,lastRow);
            awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(0,0);
            awardSponsorFundingForm.tblSponsorFunding.editCellAt(lastRow,SPONSOR_CODE);
            awardSponsorFundingForm.tblSponsorFunding.getEditorComponent().requestFocusInWindow();
            awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
            awardSponsorFundingForm.tblSponsorFunding.getCellRect(lastRow, SPONSOR_CODE, true));
            
        }
        awardSponsorFundingForm.tblSponsorFunding.editCellAt(lastRow,SPONSOR_CODE);
        
        if(awardSponsorFundingForm.tblSponsorFunding.getRowCount() == 0){
            awardSponsorFundingForm.tblSponsorFunding.setVisible(false);
        }else{
            awardSponsorFundingForm.tblSponsorFunding.setVisible(true);
        }
        
    }
    
    /** saves the form data*/
    public void saveFormData() {
        int rowMax = getExistingMaxId();
        sponsorFundingEditor.stopCellEditing();
        try{
            CoeusVector cvTemp = new CoeusVector();
            if(modified) {
                if(cvDeletedItem!= null && cvDeletedItem.size() >0){
                    cvTemp.addAll(cvDeletedItem);
                }
                
                if(cvSponsor!= null && cvSponsor.size() >0){
                    cvTemp.addAll(cvSponsor);
                }
                
                if(cvTemp!=null){
                    for(int index = 0; index < cvTemp.size(); index++){
                        AwardTransferingSponsorBean bean = (AwardTransferingSponsorBean)cvTemp.get(index);
                        if(bean.getAcType()!= null){
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                //rowId = rowId+1;
                                //bean.setRowId(rowId);
                                rowMax = rowMax + 1;
                                bean.setRowId(rowMax);
                                //bean.setRowId(getExistingMaxId() + 1);
                                queryEngine.insert(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                bean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, bean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                bean.setAcType(TypeConstants.INSERT_RECORD);
                                if(bean.getRowId() == 0){
                                    rowMax = rowMax + 1;
                                    bean.setRowId(rowMax);
                                    
                                }
                                queryEngine.insert(queryKey, bean);
                            }
                            
                        }
                        if(bean.getSponsorCode().trim() == null || bean.getSponsorCode().trim().equals(EMPTY_STRING) && bean.getSponsorName().trim() == null || bean.getSponsorName().trim().equals(EMPTY_STRING)){
                            queryEngine.delete(queryKey, bean);
                        }
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        close();
    }
    
    
    /* to delete the selected row*/
    private void performDeleteAction(){
        sponsorFundingEditor.stopCellEditing();
        int selectedRow = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
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
                AwardTransferingSponsorBean deletedPaymentBean = (AwardTransferingSponsorBean)cvSponsor.get(selectedRow);
                deletedPaymentBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDeletedItem.add(deletedPaymentBean);
                if (deletedPaymentBean.getAcType() == null ||
                deletedPaymentBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedItem.add(deletedPaymentBean);
                }
                if(cvSponsor!=null && cvSponsor.size() > 0){
                    cvSponsor.remove(selectedRow);
                    sponsorFundingTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                    
                    
                }
                if(selectedRow >0){
                    awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(
                    selectedRow-1,selectedRow-1);
                    awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
                    awardSponsorFundingForm.tblSponsorFunding.getCellRect(
                    selectedRow -1 ,0, true));
                }else{
                    if(awardSponsorFundingForm.tblSponsorFunding.getRowCount()>0){
                        awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    
    /* to open the search screen on the click of Find button*/
    private void performFindSponsor(){
        NotEquals code,name;
        And codeAndName;
        int selRow = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
        boolean emptyTable = false;
        if( selRow == -1 &&
        awardSponsorFundingForm.tblSponsorFunding.getRowCount() == 0){
            selRow = 0;
            emptyTable = true;
        }
        
        try{
            
            for(int index = 0; index < cvSponsor.size() ; index++){
                AwardTransferingSponsorBean bean = (AwardTransferingSponsorBean)cvSponsor.get(index);
                if( bean.getSponsorCode().equals(EMPTY_STRING) &&
                bean.getSponsorName().equals(EMPTY_STRING)){
                    cvSponsor.remove(index);
                }
            }
            sponsorFundingTableModel.fireTableDataChanged();
            dlgAwardSponsorFunding.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            int click = sponsorSearch();
            dlgAwardSponsorFunding.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
            if(click == OK_CLICKED){
                
                
                AwardTransferingSponsorBean newBean = new AwardTransferingSponsorBean();
                newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                newBean.setSponsorCode(getSponsorCode());
                newBean.setSponsorName(getSponsorName());
                newBean.setAcType(TypeConstants.INSERT_RECORD);
                modified = true;
                cvSponsor.add(newBean);
                awardSponsorFundingForm.tblSponsorFunding.editCellAt(cvSponsor.size() - 1,SPONSOR_CODE);
                setTableEditing(cvSponsor.size() - 1, SPONSOR_CODE);
                awardSponsorFundingForm.tblSponsorFunding.getEditorComponent().requestFocusInWindow();
                sponsorFundingTableModel.fireTableRowsInserted(sponsorFundingTableModel.getRowCount(),
                sponsorFundingTableModel.getRowCount());
                int lastRow = awardSponsorFundingForm.tblSponsorFunding.getRowCount()-1;
                if(lastRow >= 0){
                    awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(lastRow,lastRow);
                    awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(0,0);
                    awardSponsorFundingForm.tblSponsorFunding.editCellAt(lastRow,SPONSOR_CODE);
                    awardSponsorFundingForm.tblSponsorFunding.getEditorComponent().requestFocusInWindow();
                    
                    
                    awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
                    awardSponsorFundingForm.tblSponsorFunding.getCellRect(lastRow, SPONSOR_CODE, true));
                    
                }
                
            }
            
            
            if(click == CANCEL_CLICKED) {
                
                code = new NotEquals("sponsorCode", EMPTY);
                name = new NotEquals("sponsorName", EMPTY);
                codeAndName = new And(code,name);
                cvSponsor = cvSponsor.filter(codeAndName);
                sponsorFundingTableModel.fireTableDataChanged();
                return ;
            }
            
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        if( emptyTable){
            awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(selRow, selRow);
        }
    }
    
    /* the actions performed on the click of the cancel button*/
    private void performCancelAction(){
        sponsorFundingEditor.stopCellEditing();
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
                    close();
                    break;
                default:
                    break;
            }
        }else{
            close();
        }
    }

    // Modified for COEUSQA-1434 Add the functionality to set a status on a Sponsor record - Start
    /* to set the sponsor name for the particular sponsor code*/
//    private void setName(){
//        int selRow = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
//        if( selRow == -1 )return ;
//        String sponsorName;
//        try{
//            sponsorName= getSponsorName((String)sponsorFundingTableModel.getValueAt(selRow,SPONSOR_CODE));
//            if(sponsorName.equals(EMPTY)) {
//                sponsorName = EMPTY;
//            }
//        }catch (CoeusException coeusException) {
//            coeusException.printStackTrace();
//            sponsorName = EMPTY;
//        }
//        sponsorFundingTableModel.setValueAt(sponsorName,selRow,SPONSOR_NAME);
//    }
    
    /**
     * Method to set the sponsor name for the particular sponsor code
     * @param sponsorName 
     */
    private void setName(String sponsorName){
        int selRow = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
        if( selRow == -1 ){
            return ;
        }
        sponsorFundingTableModel.setValueAt(sponsorName,selRow,SPONSOR_NAME);
    }
    // Modified for COEUSQA-1434 Add the functionality to set a status on a Sponsor record - End
    
 /* This method sets the maximum Row ID from the vector
  *that is present in queryEngine
  */
    private void setMaxRowID() {
        CoeusVector cvSponsor = new CoeusVector();
        AwardTransferingSponsorBean awardTransferingSponsorBean;
        try {
            cvSponsor = queryEngine.getDetails(queryKey,
            AwardTransferingSponsorBean.class);
            if (cvSponsor!= null && cvSponsor.size() > 0) {
                cvSponsor.sort("rowId", false);
                awardTransferingSponsorBean = (AwardTransferingSponsorBean) cvSponsor.get(0);
                rowId =  awardTransferingSponsorBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /* to display the sponsor details*/
    private void displaySponsor() {
        
        int row = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
        String displaySponsor = "Display Sponsor";
        
        String sponsorCode, sponsorName = null;
        
        sponsorCode = (String)sponsorFundingTableModel.getValueAt(row, SPONSOR_CODE);
        if(!sponsorCode.equals(EMPTY)) {
            try{
                 // Modified for COEUSQA-1434 Add the functionality to set a status on a Sponsor record - Start
//                sponsorName = getSponsorName(sponsorCode).trim();
                sponsorName = getSponsorNameForCode(sponsorCode).trim();
                 // Modified for COEUSQA-1434 Add the functionality to set a status on a Sponsor record - End
            }catch (CoeusException coeusException) {
                dlgAwardSponsorFunding.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
                coeusException.printStackTrace();
            }
        }
        if(sponsorCode.equals(EMPTY)) {
            dlgAwardSponsorFunding.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
            return ;
        }else if(sponsorName == null || sponsorName.equals(EMPTY)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
            setTableEditing(row, SPONSOR_CODE);
            return ;
        }
        dlgAwardSponsorFunding.setCursor( new Cursor(Cursor.WAIT_CURSOR));
        SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm(DISPLAY_MODE, sponsorCode);
        frmSponsor.showForm(mdiForm, displaySponsor, true);
        dlgAwardSponsorFunding.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /* to set the focus*/
    private void setTableEditing(int row,int column) {
        awardSponsorFundingForm.tblSponsorFunding.requestFocusInWindow();
        awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(row, row);
        awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(1, 1);
        
        // saves the row and column when you enter a cell
        // in this case the values in prevRow and prevCol is set and
        // SwingUtilities.invokeLater() method is called to do the rest
        final int indexRow = row;
        final int indexColumn = column;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                awardSponsorFundingForm.tblSponsorFunding.requestFocusInWindow();
                awardSponsorFundingForm.tblSponsorFunding.changeSelection(indexRow , indexColumn, false, false);
                awardSponsorFundingForm.tblSponsorFunding.setEditingColumn(indexColumn);
                awardSponsorFundingForm.tblSponsorFunding.editCellAt(indexRow ,indexColumn);
                awardSponsorFundingForm.tblSponsorFunding.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                
                Component editingComponent = awardSponsorFundingForm.tblSponsorFunding.getEditorComponent();
                editingComponent.requestFocus();
            }  });
            
    }
    
    /* to set the renderer ,editor and the sizes to the columns in the table*/
    private void setTableEditors(){
        awardSponsorFundingForm.tblSponsorFunding.setRowHeight(22);
        JTableHeader tableHeader = awardSponsorFundingForm.tblSponsorFunding.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        awardSponsorFundingForm.tblSponsorFunding.setSelectionBackground(java.awt.Color.yellow);
        awardSponsorFundingForm.tblSponsorFunding.setSelectionForeground(java.awt.Color.black);
        awardSponsorFundingForm.tblSponsorFunding.setOpaque(false);
        awardSponsorFundingForm.tblSponsorFunding.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column =  awardSponsorFundingForm.tblSponsorFunding.getColumnModel().getColumn(SPONSOR_CODE);
        column.setMinWidth(150);
        column.setPreferredWidth(150);
        column.setResizable(true);
        column.setCellRenderer(sponsorFundingRenderer);
        column.setCellEditor(sponsorFundingEditor);
        tableHeader.setReorderingAllowed(false);
        
        column =  awardSponsorFundingForm.tblSponsorFunding.getColumnModel().getColumn(SPONSOR_NAME);
        column.setMinWidth(315);
        column.setPreferredWidth(315);
        column.setResizable(true);
        column.setCellRenderer(sponsorFundingRenderer);
        column.setCellEditor(sponsorFundingEditor);
        tableHeader.setReorderingAllowed(false);
    }
    
    
    /* action performed on thedoubleclick on the pertaining fields*/
    public void mouseClicked(MouseEvent mouseEvent) {
        int selRow = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
        
        if(mouseEvent.getClickCount() == 2){
            sponsorFundingEditor.stopCellEditing();
            displaySponsor();
            
        }
        
        
        if(mouseEvent.getClickCount() == 2 && awardSponsorFundingForm.tblSponsorFunding.getValueAt(selRow , SPONSOR_NAME).equals(EMPTY_STRING )){
            try{
                int click = sponsorSearch();
                if(click == OK_CLICKED){
                    sponsorFundingEditor.cancelCellEditing();
                    sponsorFundingTableModel.setValueAt(getSponsorCode(), selRow,SPONSOR_CODE);
                    sponsorFundingTableModel.setValueAt(getSponsorName(), selRow, SPONSOR_NAME);
                    awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(cvSponsor.size() - 1,cvSponsor.size() - 1);
                    awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(0,0);
                    awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
                    awardSponsorFundingForm.tblSponsorFunding.getCellRect(
                    cvSponsor.size() - 1 ,0, true));
                    awardSponsorFundingForm.tblSponsorFunding.editCellAt(cvSponsor.size() - 1,SPONSOR_CODE);
                    setRequestFocusInThread(sponsorFundingEditor.txtComponent);
                    sponsorFundingTableModel.fireTableRowsUpdated(selRow, selRow);
                }
                if(click == CANCEL_CLICKED){
                    return;
                }
                
                
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
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
     * Table model for the AwardSponsorFunding table
     */
    class SponsorFundingTableModel extends AbstractTableModel {
        
        String colNames[] = {"Sponsor Code" , "Sponsor Name" };
        Class[] colTypes = new Class [] {String.class , String.class };
        
        /* if the cell is editable return true else return false*/
        public boolean isCellEditable(int row, int col){
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            }else{
                if(col == SPONSOR_CODE){
                    return true;
                }else{
                    return false;
                }
            }
        }
        
        
        /* returns the number of columns*/
        public int getColumnCount() {
            return colNames.length;
        }
        
        /* returns the number of rows*/
        public int getRowCount() {
            if(cvSponsor == null ){
                return 0;
            }else{
                return cvSponsor.size();
            }
        }
        
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardTransferingSponsorBean bean = (AwardTransferingSponsorBean)cvSponsor.get(rowIndex);
            switch(columnIndex){
                case SPONSOR_CODE:
                    return bean.getSponsorCode();
                case SPONSOR_NAME:
                    return bean.getSponsorName();
            }
            return EMPTY_STRING;
        }
        
        /* sets the value at a particular cell*/
        public void setValueAt(Object value, int row, int col){
            AwardTransferingSponsorBean awardTransferingSponsorBean = (AwardTransferingSponsorBean)cvSponsor.get(row);
            //  System.out.println("Spon code: "+awardTransferingSponsorBean.getSponsorCode());
            
            //will be set to false if any error occurs before setting value.
            valueSet = true;
            boolean valueChanged = false;
            switch(col){
                case SPONSOR_CODE:
                    if(value == null) return;
                    if (!value.toString().trim().equals(awardTransferingSponsorBean.getSponsorCode().trim())) {
                        awardTransferingSponsorBean.setSponsorCode(value.toString());
                        modified = true;
                        valueChanged = true;
                    }
                   
                    String sponsorCode, sponsorName = CoeusGuiConstants.EMPTY_STRING;
                    sponsorCode = ((String)sponsorFundingTableModel.getValueAt(row, SPONSOR_CODE)).trim();
                    if(!sponsorCode.equals(EMPTY) && 
                            (valueChanged || TypeConstants.INSERT_RECORD.equals(awardTransferingSponsorBean.getAcType()) ||
                            TypeConstants.UPDATE_RECORD.equals(awardTransferingSponsorBean.getAcType()))) {
                        try{
                            sponsorName = getSponsorName(sponsorCode).trim();
                        }catch (CoeusException coeusException) {
                            coeusException.printStackTrace();
                        }
                    }else if(!CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
                        sponsorName = awardTransferingSponsorBean.getSponsorName();
                    }
                    if(CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)) {
                        setName(sponsorName);
                        return ;
                    }else if((sponsorName == null || sponsorName.equals(EMPTY))) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                        awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(row,row);
                        setRequestFocusInClassThread(row,col);
                        //awardSponsorFundingForm.tblSponsorFunding.setRowSelectionInterval(row,row);
                        awardSponsorFundingForm.tblSponsorFunding.setColumnSelectionInterval(0,0);
                        awardSponsorFundingForm.tblSponsorFunding.scrollRectToVisible(
                        awardSponsorFundingForm.tblSponsorFunding.getCellRect(
                        row ,0, true));
                        valueSet = false;
                        this.setValueAt(CoeusGuiConstants.EMPTY_STRING,row,SPONSOR_CODE);
                        return ;
                    }
                    setName(sponsorName);
                    valueSet = true;
                    break;
                case SPONSOR_NAME:
                    if(value == null) return;
                    if (!value.toString().trim().equals(awardTransferingSponsorBean.getSponsorName().trim())) {
                        awardTransferingSponsorBean.setSponsorName(value.toString());
                        modified = true;
                    }
                    break;
            }
            if(awardTransferingSponsorBean.getAcType()== null && valueChanged){
                awardTransferingSponsorBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        
    }
    
    /* Table Cell Editor for the AwardSponsorFundingtable*/
    class SponsorFundingEditor extends AbstractCellEditor implements TableCellEditor, MouseListener {
        private JTextField txtComponent;
        private int column;
        
        /* Creates a sponsorFunding Editor*/
        public SponsorFundingEditor(){
            txtComponent = new JTextField();
            txtComponent.addMouseListener(this);
        }
        
        /* Returns the CellEditor value*/
        public Object getCellEditorValue() {
            switch(column){
                case SPONSOR_CODE:
                    txtComponent.getText();
            }
            return txtComponent.getText();
        }
        
        /* returns the cellEditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case SPONSOR_CODE:
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                    
            }
            
            return txtComponent;
            
        }
        
        public void mouseClicked(MouseEvent mouseEvent) {
            int selRow = awardSponsorFundingForm.tblSponsorFunding.getSelectedRow();
            if(mouseEvent.getClickCount() == 2 && txtComponent.getText().trim().length() > 0) {
                txtComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                sponsorFundingEditor.stopCellEditing();
                displaySponsor();
                txtComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
            
            
            if(mouseEvent.getClickCount() == 2 && txtComponent.getText().trim().length() == 0) {
                
                try{
                    int click = sponsorSearch();
                    if(click == OK_CLICKED){
                        sponsorFundingEditor.cancelCellEditing();
                        sponsorFundingTableModel.setValueAt(getSponsorCode(), selRow,SPONSOR_CODE);
                        sponsorFundingTableModel.setValueAt(getSponsorName(), selRow, SPONSOR_NAME);
                        awardSponsorFundingForm.tblSponsorFunding.editCellAt(cvSponsor.size() - 1,SPONSOR_CODE);
                        setTableEditing(cvSponsor.size() - 1, SPONSOR_CODE);
                        awardSponsorFundingForm.tblSponsorFunding.getEditorComponent().requestFocusInWindow();
                        //setRequestFocusInThread(sponsorFundingEditor.txtComponent);
                        //sponsorFundingEditor.txtComponent.requestFocus();
                        //  awardSponsorFundingForm.tblSponsorFunding.getEditorComponent().requestFocusInWindow();
                        
                        //awardSponsorFundingForm.tblSponsorFunding.editCellAt(cvSponsor.size() - 1,SPONSOR_CODE);
                        // setTableEditing(cvSponsor.size() - 1, SPONSOR_CODE);
                        sponsorFundingTableModel.fireTableRowsUpdated(selRow, selRow);
                    }
                    if(click == CANCEL_CLICKED){
                        return;
                    }
                }catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
        private void setFocus(int column){
            switch(column){
                case SPONSOR_CODE:
                    txtComponent.requestFocus();
            }
        }
        
    }
    
    /**
     * Table cell renederer class for Cost sharing table
     */
    class SponsorFundingRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
        
        private JTextField txtComponent;
        private JLabel lblText;
        
        /* Creates a sponsorFunding Renderer*/
        public SponsorFundingRenderer(){
            txtComponent = new JTextField();
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
                case SPONSOR_CODE:
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
                case SPONSOR_NAME:
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
            }
            return txtComponent;
        }
        
    }
    
}
