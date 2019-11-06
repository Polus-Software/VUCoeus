/*
 * ProposalNarrativeModuleDetails.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May 30, 2003, 11:46 AM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.applet.AppletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.MessageFormat;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.filechooser.FileFilter;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.BaseWindowObservable;

/**
 *
 * @author  ravikanth
 */
public class ProposalNarrativeModuleDetails extends JComponent 
    implements ActionListener,TypeConstants{
    
    private char functionType;
    private ProposalNarrativeFormBean formBean;
    private CoeusMessageResources messageResource;
    private boolean saveRequired;
    private Vector statusLookup;
    private CoeusDlgWindow parent;
    
    private static final char UPDATE_NARRATIVE_DETAILS = 'L';
    //Added by shiji for Case id : 2016 step 1 - start
    private static final char SEND_NOTIFICATION = 'd';
    //Case id : 2016 step 1 - end
    private BaseWindowObservable observable = new BaseWindowObservable();
    
    private HashMap hmProposalUserRights = new HashMap();
    
    /*added for Coeus Enhancement case:1624 step 1
     *to hold  the narrative types*/
    private Vector narrativeTypes;
    
    /*added for Coeus Enhancement case:1624
     *to hold the narrative beans*/
    private Vector narrativeBeans;
    
    /*Added for the CoeusEnhancement case:1776 start step:1*/
    private String narrativeTypeCode;
    private static final String MODULE_TITLE = "proposal_narr_exceptionCode.6614";
    /*End CoeusEnhancement case:1776 step:1*/
    
    //Added for the Coeus Enhancemnt case:#1767
    private int proposalStatusCode;

    //Added for bug id 1860 step 1 : start
    private boolean isAlterProposal;
    // bug id 1860 step 1 : end
    //Code added for Proposal Hierarchy case#3183
    private boolean parentProposal;
    private static final char CAN_ADD_NARRATIVE ='u';
    private int originalValue;
    //Added for case#3183 - Proposal Hierarchy Enhancement
    private boolean hierarchy;
    
    //Added for case#2420 - Upload of files on Edit Module Details Window - start
    private static final String EMPTY_STRING = "";
    private CoeusFileChooser fileChooser;
    private static final char UPDATE_NARRATIVE_PDF = 'm';
    private static final char GET_MODIFY_NARRATIVE_DATA = 'e';  
    private static final char GET_NARRATIVE = 'c';    
    //Modified for 3183 - Proposal Hierarchy changes - start
    //'u' is already used for CAN_ADD_NARRATIVE function type
    //private static final char GET_BLOB_DATA_FOR_NARRATIVE = 'u';
    private static final char GET_BLOB_DATA_FOR_NARRATIVE = 'y';
    //Modified for 3183 - Proposal Hierarchy changes - end
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +CoeusGuiConstants.PROPOSAL_SERVLET;    
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean = null;
    private static final String INSUFFICIENT_UPLOAD_RIGHTS = "proposal_narr_exceptionCode.6612";
    private CoeusDlgWindow dlgModuleDetails;
    String extension = EMPTY_STRING;    
    String oldFileName = EMPTY_STRING;
    String newFileName = EMPTY_STRING;
    String serverMsg = EMPTY_STRING;
    //Added for case#2420 - Upload of files on Edit Module Details Window - end
    //Added for case#2999 - Multiple emails generated when a document is uploaded
    public boolean canModifyNarrative;
    //Added for COEUSDEV-340 : mail that is generated when a narrative is changes does not have proposal and narrative details - Start
    private boolean isDocumentUploaded = false;
    //COEUSDEV-34 : End
    /** Creates new form ProposalNarrativeModuleDetails */
    public ProposalNarrativeModuleDetails() {
    }

    public ProposalNarrativeModuleDetails(ProposalNarrativeFormBean bean, char functionType ) {
        this.formBean = bean;
        this.functionType = functionType;
        messageResource = CoeusMessageResources.getInstance();
    }

    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
     
    public JComponent showForm(CoeusDlgWindow parentComponent  ){
        this.parent = parentComponent;
        initComponents();
        postInitComponents();
        setFormData();
        setTableEditors();
        //formatFields();
        Component[] comp = {cmbStatus,cmbNarrativeType,txtArTitle,txtFldContactName,txtFldPhone,
        txtFldEmail,txtArComments,tblUsers,btnOk,btnCancel,btnUpload, btnView};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        pnlMain.setFocusTraversalPolicy(traversal);
        pnlMain.setFocusCycleRoot(true);
        txtArComments.requestFocus();
        return pnlMain;
    }
    
    //Modified  for bug id 1860 step 2 : start
    private void postInitComponents() {
        tblUsers.setModel( new DefaultTableModel( 
            new Object[][]{}, getUserColumnNames().toArray()){
                public boolean isCellEditable( int row, int col ) {
                    if( col == 2){
                        return true;
                    }
                    return false;
                }
            });
        btnOk.addActionListener( this );
        btnCancel.addActionListener( this );
        btnUpload.addActionListener(this);
        btnView.addActionListener(this);
        if((functionType == MODIFY_MODE) && isAlterProposal ) {
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            cmbNarrativeType.setEnabled(false);
            cmbStatus.setEnabled(false);
            txtFldContactName.setEditable(false);
            txtFldEmail.setEditable(false);
            txtFldPhone.setEditable(false);
            txtArComments.setEnabled(false);
            txtArComments.setBackground(bgColor);
            tblUsers.setEnabled(false);
        }
        //Added for case# 3316 - start
        //Disable the Narrative type combobox when the proposal status code is 2, 4
        if(proposalStatusCode == 2 || proposalStatusCode == 4){
            cmbNarrativeType.setEnabled(false);
            //Added for case#3705 - PR- Narrative Status- Able to Change after Approval in Progress
            //MOdified for the case # COEUSQA-1679-Modification for final flag indicator -start
            //cmbStatus.setEnabled(false);
            //MOdified for the case # COEUSQA-1679-Modification for final flag indicator -end
        }
        //Added for case# 3316 - end
    }
    //bug id 1860 step 2 : end
    
    //Modified  for bug id 1860 step 3 : start
    public void setInitialFocus(){
        txtArTitle.setCaretPosition(0);
        /*modified for the Coeus enhancement:1624*/
        if((functionType == MODIFY_MODE) && !isAlterProposal ) {
    //Modified for Proposal Narrative Locking Issue case id 3316 - start
            if(cmbNarrativeType.isEnabled()){
                cmbNarrativeType.requestFocus();
            }else{
                txtArTitle.requestFocus(); 
            }
            //Modified for Proposal Narrative Locking Issue case id 3316 - end
        }else if((functionType == MODIFY_MODE) && isAlterProposal) {
           txtArTitle.requestFocus(); 
        }
        //Added for case#2420 - Upload of files on Edit Module Details Window
        txtArTitle.requestFocus();
    }
    //bug id 1860 step 3 ; end
    
    private Vector getUserColumnNames(){
        Vector colNames = new Vector();
        colNames.addElement("Icon");
        colNames.addElement("User");
        colNames.addElement("Rights") ;
        
        return colNames;
    }
    
    private void setTableEditors() {
        tblUsers.setRowHeight(22);
        tblUsers.setOpaque( false );
        TableColumn column = tblUsers.getColumnModel().getColumn(0);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer( new IconRenderer() );
        
        column = tblUsers.getColumnModel().getColumn(1);
        column.setMaxWidth(180);
        column.setMinWidth(180);
        column.setPreferredWidth(180);
        column.setCellRenderer( new NarrativeUserTableRenderer() );
        
        column = tblUsers.getColumnModel().getColumn(2);
        column.setMinWidth(250);
        column.setCellRenderer( new NarrativeUserTableRenderer() );
        column.setCellEditor( new ModuleUserEditor());
        tblUsers.getTableHeader().setReorderingAllowed( false );
        tblUsers.getTableHeader().setResizingAllowed( false );
        tblUsers.setShowHorizontalLines( false );
        tblUsers.setShowVerticalLines( false );
        //tblUsers.setRowSelectionAllowed( false );
        tblUsers.getTableHeader().setFont( CoeusFontFactory.getLabelFont() );
        tblUsers.setFont( CoeusFontFactory.getNormalFont() );
        tblUsers.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    }
    
    private void setFormData() {
        if( formBean != null ) {
            lblProposalValue.setText( formBean.getProposalNumber() );
            if( cmbStatus.getItemCount() == 0 && statusLookup != null) {
                int statusCount = statusLookup.size();
                for( int indx  = 0; indx < statusCount; indx++) {
                    cmbStatus.addItem( (ComboBoxBean)statusLookup.elementAt( indx ) );
                }
            }
            /*added for the CoeusEnhancements case:1624
             *get the narrative type beans and add it to the vector of combo beans
             *with the code and the description*/
            Vector vecNarrative = new Vector();
            if(narrativeTypes.size() > 0 && narrativeTypes != null){
                int vecCount = narrativeTypes.size();
                for(int index = 0;index < narrativeTypes.size();index++){
                    ProposalNarrativeTypeBean proposalNarrativeTypeBean = (ProposalNarrativeTypeBean)narrativeTypes.get(index);
                    vecNarrative.addElement( new ComboBoxBean(new Integer(proposalNarrativeTypeBean.getNarrativeTypeCode()).toString(),proposalNarrativeTypeBean.getDescription()));
                }
            }
            
            /* Added for the Coeus Enhancement case #1624:Step4
             *To populate the narrative types in the combobox ,the narrative types vector holds
             *the narr types after the related filterations for the SYSTEM_GENERATED & ALLOW_MULTIPLE*/
            if(cmbNarrativeType.getItemCount() == 0 && vecNarrative != null){
                int narrCount = vecNarrative.size();
                for( int indx  = 0; indx < narrCount; indx++) {
                    cmbNarrativeType.addItem( (ComboBoxBean)vecNarrative.elementAt( indx ) );
                    
                }
            }
            /*end step4*/
            
            txtFldModuleNo.setText( ""+formBean.getModuleNumber() );
            txtFldSequenceNo.setText( ""+formBean.getModuleSequenceNumber() );
            txtArTitle.setText( formBean.getModuleTitle() );
            txtFldContactName.setText( formBean.getContactName() );
            txtFldEmail.setText( formBean.getEmailAddress() );
            txtFldPhone.setText( formBean.getPhoneNumber() );
            txtArComments.setText( formBean.getComments() );
            for( int indx = 0; indx < cmbStatus.getItemCount(); indx++ ) {
                if( ((ComboBoxBean)cmbStatus.getItemAt( indx ) ).getCode().equals(
                ""+formBean.getModuleStatusCode())){
                    cmbStatus.setSelectedIndex( indx );
                    break;
                }
            }
            
            /*added for the Coeus Enhancements case:1624
             *to poulate the combo value from the base window*/
            for( int indx = 0; indx < cmbNarrativeType.getItemCount(); indx++ ) {
                String code = ((ComboBoxBean)cmbNarrativeType.getItemAt( indx )).getCode();
                String narrCode = new Integer(formBean.getNarrativeTypeCode()).toString();
                if( ((ComboBoxBean)cmbNarrativeType.getItemAt( indx ) ).getCode().equals(
                new Integer(formBean.getNarrativeTypeCode()).toString())){
                    cmbNarrativeType.setSelectedIndex( indx );
                    originalValue = indx;
                    break;
                }
                
            }
            
            if( formBean.getPropModuleUsers() != null ) {
                ( ( DefaultTableModel )tblUsers.getModel() ).setDataVector(
                getModuleUsersTableData( formBean.getPropModuleUsers() )
                , getUserColumnNames() );
                
                if( tblUsers.getRowCount() > 0 ) {
                    tblUsers.setRowSelectionInterval(0,0);
                }
                
            }
            //Code modified for case#2420 - Upload of files on Edit Module Details Window - start
            if(formBean.getFileName() != null && !formBean.getFileName().equals("")){
                txtFileName.setText(formBean.getFileName());
                btnView.setEnabled(true);
                formBean.setHasPDFModuleNumber(true);
            }else{
                btnView.setEnabled(false);
                formBean.setHasPDFModuleNumber(false);
            }
            //Code modified for case#2420 - Upload of files on Edit Module Details Window - end
            
            saveRequired = false;
        }
    }
    
    private Vector getModuleUsersTableData(Vector moduleUsers ) {
        Vector userTableData = new Vector();
        Vector userTableRow;
        if( moduleUsers != null && moduleUsers.size() > 0 ) {
            int modUserSize = moduleUsers.size();
            
            ProposalNarrativeModuleUsersFormBean moduleUserBean;
            for( int indx = 0; indx < modUserSize; indx++ ) {
                moduleUserBean = ( ProposalNarrativeModuleUsersFormBean ) moduleUsers.elementAt( indx );
                userTableRow = new Vector();
                userTableRow.addElement( "" );
                userTableRow.addElement( moduleUserBean );
                userTableRow.addElement( moduleUserBean );
                userTableData.addElement( userTableRow );
            }
        }
        return userTableData;
    }
    /** Setter for property statusLookup.
     * @param statusLookup New value of property statusLookup.
     */
    public void setStatusLookup(Vector statusLookup) {
        this.statusLookup = statusLookup;
    }
    
    /*added for the Coeus Enhancements case#1624 step:2
     *to hold the narrative Types */
    public void setNarrativeTypes(Vector narrativeTypes){
        this.narrativeTypes = narrativeTypes;
    }
    /*end step 2*/
    
     /*added for the Coeus Enhancements case#1624
      *to hold the narrative Types */
    public void setNarrativeBeans(Vector narrativeBeans){
        this.narrativeBeans = narrativeBeans;
    }
    /*end */
    
    
    /*Added for the CoeusEnhancement case:1776 start step:2*/
    public void setParameterNarrativeTypeCode(String code){
        this.narrativeTypeCode = code;
    }
    /*End CoeusEnhancement case:1776 step:2*/
    
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        try {
            getFormData();
            if( source.equals( btnCancel ) ){
                if( saveRequired ) {
                    String msg = messageResource.parseMessageKey(
                    "saveConfirmCode.1002");
                    
                    int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
                    switch(confirm){
                        case ( JOptionPane.NO_OPTION ) :
                            saveRequired = false;
                            parent.dispose();
                            break;
                        case ( JOptionPane.YES_OPTION ) :
                            if( validateData() ){
                                //Code modified and added for Proposal Hierarchy case#3183 - starts
                                boolean canDispose = saveNarrativeDetails(); //Added by Vyjayanthi
                                if(canDispose){
                                    parent.dispose();
                                }
                                //Code modified and added for Proposal Hierarchy case#3183 - ends
                            }
                            break;
                        case ( JOptionPane.CANCEL_OPTION ) :
                            parent.setVisible( true );
                            //return;
                            break;
                    }
                    
                }else {
                    parent.dispose();
                }
            }else if( source.equals( btnOk ) ) {
                if( validateData() ) {
                    //Added by shiji for Case id:2016 step 2 - start 
                    //Modified for case#2999 - Multiple emails generated when a document is uploaded
//                    if(saveRequired && isAlterProposal) {
                    if(saveRequired && (proposalStatusCode != 1 || isCanModifyNarrative())) {
                        try {
                             
                            updateInboxDetails();
                        }catch(CoeusClientException coeusClientException) {
                            CoeusOptionPane.showDialog(coeusClientException);
                        }
                    }
                    //Case id : 2016 step 2 - end 
                    //Code modified and added for Proposal Hierarchy case#3183 - starts
                    boolean canDispose = saveNarrativeDetails(); //Added by Vyjayanthi
                    if(canDispose){
                        parent.dispose();
                    }
                    //Code modified and added for Proposal Hierarchy case#3183 - ends
                }
            }
            //Added for case#2420 - Upload of files on Edit Module Details Window - start
            else if(source.equals(btnUpload)){
                setPdfProperties();
            }else if(source.equals(btnView)){
                viewPdfDocument();
            }
            //Added for case#2420 - Upload of files on Edit Module Details Window - end
        }catch( Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage() );
            parent.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    //Added by shiji for Case id:2016 step 3 - start
    /*To send Notifications to all users who have already approved the proposal when a change in made to a Narrative module.*/
    public void updateInboxDetails() throws CoeusClientException {
        String proposalNumber = formBean.getProposalNumber();
        //Commented/Added for case#3469 - Message made generic when a narrative is modified when proposal status is approved/approval in progress
        //String message = "Module title for proposal \'"+proposalNumber+ "\' has been changed to \'"+txtArTitle.getText()+"\' for Module No." +formBean.getModuleNumber();
      
        //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
        //When document is uploaded message text is changed
//        String message =    "Narrative module details for proposal \'"
//                +proposalNumber
//                +"\', module \'"
//                +formBean.getModuleNumber()
//                +"\' have been changed.";
        String message = "";
        MessageFormat formatter = new MessageFormat("");
        if(isDocumentUploaded){
            message = formatter.format(messageResource.parseMessageKey("proposal_narr_document_exceptionCode.6621"), proposalNumber,formBean.getModuleNumber());
        }else{
            message = formatter.format(messageResource.parseMessageKey("proposal_narr_content_exceptionCode.6620"), proposalNumber,formBean.getModuleNumber());        
        }
        message = message+"\r\n\r\n\r\n\r\n\r\nNarrative Type:          "+((ComboBoxBean)cmbNarrativeType.getSelectedItem()).getDescription();
        message = message+"\r\nModule Description:      "+formBean.getModuleTitle();
        
        //COEUSDEV-340 : End
         String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        String fromUser= CoeusGuiConstants.getMDIForm().getUserName();
        
        request.setFunctionType(SEND_NOTIFICATION);
        request.setId(formBean.getProposalNumber());
        request.setDataObject(message);
        request.setUserName(fromUser);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(responderBean!= null){
            if(!responderBean.isSuccessfulResponse()){
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
    }
    //Case id : 2016 step 3 - end 
 
    
    //Added by Vyjayanthi on 27/01/2004
    /**
     * Save details to the database
     * @return boolean it is updated or not.
     */
    public boolean saveNarrativeDetails() throws Exception{
        Vector dataObjects = new Vector();
        boolean alterProposal = false;
        if( saveRequired ) {
            //Code modified and added for Proposal Hierarchy case#3183 - starts
            //These codes are common for both the server call, it is declared as common.
            String connectTo = CoeusGuiConstants.CONNECTION_URL +
            CoeusGuiConstants.PROPOSAL_SERVLET ;
            RequesterBean request = new RequesterBean();
            formBean.setParentProposal(isParentProposal());
            //Code modified and added for Proposal Hierarchy case#3183 - ends
            Vector dataToDB = new Vector();
            dataToDB.addElement(formBean);
            Vector vecAlterNarrData = null;
            //Added for the Coeus Enhancemnt case:#1767
            if(proposalStatusCode == 2 || proposalStatusCode == 4) {
                vecAlterNarrData = new Vector();
                if(functionType == MODIFY_MODE)
                    alterProposal = true;
                vecAlterNarrData.addElement(CoeusGuiConstants.getMDIForm().getUnitNumber());
                vecAlterNarrData.addElement(new Boolean(alterProposal));
            }
            dataToDB.addElement(vecAlterNarrData);
            //End for the Coeus Enhancemnt case:#1767
            //Code added for Proposal Hierarchy case#3183 - starts
            //check done for adding the narrative type
            AppletServletCommunicator comm = null;
            ResponderBean response = null;
            //Modified for case#3183 - Proposal Hierarchy Enhancement
            //If the proposal is in hierarchy and the narrative is insert or the narrative type changed
            //then the allow multiple validation to be done
            if(isHierarchy() && ((formBean.getAcType() != null 
                && formBean.getAcType().equals(TypeConstants.INSERT_RECORD))
                || cmbNarrativeType.getSelectedIndex()!=originalValue)){
                request.setFunctionType(CAN_ADD_NARRATIVE);
                request.setDataObject( formBean );
                comm = new AppletServletCommunicator( connectTo, request );
                comm.send();
                response = comm.getResponse();
                if (response == null) {
                    response = new ResponderBean();
                    response.setResponseStatus(false);
                    response.setMessage(messageResource.parseMessageKey(
                    "server_exceptionCode.1000"));
                }
                if (response.isSuccessfulResponse()) {
                    boolean canAdd = ((Boolean)response.getDataObject()).booleanValue();
                    if(!canAdd){
                        int confirm = CoeusOptionPane.showQuestionDialog(
                        "Narrative Type is already present in the Hierarchy. " +
                        "Adding the narrative type will not be synced. Do you want to continue? ",
                        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                        if(confirm == 1){
                            return false;
                        }
                    }
                }
            }
            //Code added for Proposal Hierarchy case#3183 - ends
            request = new RequesterBean();
            request.setFunctionType( UPDATE_NARRATIVE_DETAILS );
            request.setDataObjects( dataToDB );
            request.setId( formBean.getProposalNumber() );
            
            comm = new AppletServletCommunicator( connectTo, request );
            parent.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            comm.send();
            response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(messageResource.parseMessageKey(
                "server_exceptionCode.1000"));
            }    
            //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - start
            //Modified for case# 3316 - starts
            //Before saving check whether the user has the narrative lock
            //if lock not present response id is set as -1 from the server side
            if(response.getId()!=null && response.getId().equals("-1")){
                saveRequired = false;
                serverMsg = messageResource.parseMessageKey("proposal_narr_exceptionCode.6617") +" " 
                        +formBean.getProposalNumber() +" "
                        +messageResource.parseMessageKey("proposal_narr_exceptionCode.6618");                                  
                CoeusOptionPane.showErrorDialog(serverMsg);   
                parent.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                return false;
            }else if (response.isSuccessfulResponse()) {
                saveRequired = false;
                dataObjects = response.getDataObjects();                
                observable.setFunctionType( functionType );
                observable.notifyObservers( dataObjects );          
                parent.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));   
                if(proposalNarrativePDFSourceBean != null){
                    if(!txtFileName.getText().equals("")){
                        uploadPdfDocument();
                    }
                }
                //Modified for case# 3316 - end
                //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - end
            }
        }
        
        //Added for case#2420 - Upload of files on Edit Module Details Window - start
        newFileName = txtFileName.getText().trim();
        if(!oldFileName.equals("") && !newFileName.equals("") && !oldFileName.equals(newFileName) && !formBean.getAttachmentAcType().equals("I")){
            uploadPdfDocument();
            parent.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
        //Added for case#2420 - Upload of files on Edit Module Details Window - end
     return true;
    }
    
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    public void setSaveRequired( boolean saveRequire ) {
        this.saveRequired = saveRequire;
    }
    
    private void getFormData() {
        formBean.addPropertyChangeListener( new PropertyChangeListener() {
            
            public void propertyChange( PropertyChangeEvent pce ) {
                if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                    saveRequired = true;
                    if( formBean.getAcType() == null ) {
                        formBean.setAcType( UPDATE_RECORD );
                    }
                }
                if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                    saveRequired = true;
                    if( formBean.getAcType() == null ) {
                        formBean.setAcType( UPDATE_RECORD );
                    }
                }
                if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                    if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                        saveRequired = true;
                        if( formBean.getAcType() == null ) {
                            formBean.setAcType( UPDATE_RECORD );
                        }
                    }
                }
                
            }
        });
        formBean.setModuleStatusCode( ((ComboBoxBean)cmbStatus.getSelectedItem()).getCode().charAt(0) );
        
        /*added for the Coeus Enhancement case:1624 step 4
         *to set the narrative type code to  the form bean for saving the data to the data base*/
        formBean.setNarrativeTypeCode( Integer.parseInt(((ComboBoxBean)cmbNarrativeType.getSelectedItem()).getCode()));
        /*end step4*/
        
        formBean.setModuleTitle( txtArTitle.getText().length() == 0 ? null : txtArTitle.getText() );
        formBean.setContactName( txtFldContactName.getText().length() == 0 ? null : txtFldContactName.getText() );
        formBean.setEmailAddress( txtFldEmail.getText().length() == 0 ? null : txtFldEmail.getText() );
        formBean.setPhoneNumber( txtFldPhone.getText().length() == 0 ? null : txtFldPhone.getText() );
        formBean.setComments( txtArComments.getText().length() == 0 ? null : txtArComments.getText() );
        //Added for case#2420 - Upload of files on Edit Module Details Window - start
        formBean.setFileName( txtFileName.getText().length() == 0 ? null : txtFileName.getText() );
        //Added for case#2420 - Upload of files on Edit Module Details Window - end
    }
    
    public boolean isDataChanged(){
        getFormData();
        return saveRequired;
    }
    
    public boolean validateData() throws Exception {
        Vector users = formBean.getPropModuleUsers();
        /*added for the coeus enhancements case:1624
         *validate when the narrative type with allow_multiple = "N"
         * which cannot be added if that type is already existing in
         * the narrative table*/
        if(narrativeBeans != null && narrativeBeans.size()>0){
            for(int index=0;index<narrativeBeans.size();index++){
                ProposalNarrativeFormBean dataBean = (ProposalNarrativeFormBean)narrativeBeans.get(index);
                if(formBean.getModuleNumber()!= dataBean.getModuleNumber()){
                    for(int j=0;j<narrativeTypes.size();j++){
                        ProposalNarrativeTypeBean bean = (ProposalNarrativeTypeBean)narrativeTypes.get(j);
                        if(formBean.getNarrativeTypeCode() == dataBean.getNarrativeTypeCode()){
                            if(formBean.getNarrativeTypeCode() == bean.getNarrativeTypeCode()){
                                if(bean.getAllowMultiple().equals("N")){
                                    CoeusOptionPane.showInfoDialog("A module with Narrative Type '"+ cmbNarrativeType.getSelectedItem().toString() +"' already exists ");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        /*Added for the Coeus Enhancement case:1776 for a module whose code equals to the NARRATIVE_TYPE_CODE in the parameter table,
         *then the module title is mandatory start step:3*/
        ComboBoxBean bean = (ComboBoxBean)cmbNarrativeType.getSelectedItem();
        if(bean.getCode().equals(narrativeTypeCode) && txtArTitle.getText().trim().equals("") && txtArTitle.getText() != null){
            CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(MODULE_TITLE));
            setRequestFocusInThread(txtArTitle);
            return false;
        }
        /*End CoeusEnhancement case:1776 step:3*/
        
        if( users != null && users.size() > 0 ) {
            int userSize = users.size();
            ProposalNarrativeModuleUsersFormBean userBean;
            boolean modifierExists = false;
            for( int indx = 0; indx < userSize; indx++ ) {
                userBean = ( ProposalNarrativeModuleUsersFormBean ) users.elementAt( indx );
                char accessType = userBean.getAccessType();
                if( 'M' ==  accessType ){
                    modifierExists = true;
                    String userAccessType = (String) hmProposalUserRights.get(userBean.getUserId().toUpperCase());
                    if( (userAccessType == null || !userAccessType.equals(""+accessType)) && !isAlterProposal ) {
                        throw new Exception( userBean.getUserId().toUpperCase()+"  does not have Modify Narrative at the Proposal Level" );
                    }
                }
            }
            if( !modifierExists && !isAlterProposal ) {
                throw new Exception(messageResource.parseMessageKey(
                "proposal_narr_exceptionCode.6605" ));
            }
        }                
        //Added for Case #2366 start        
        char code = ((ComboBoxBean)cmbStatus.getSelectedItem()).getCode().charAt(0);
        if(code == 'C'){
//Modified for Case 3673 - Should not allow narratives status to be "Complete" if no attachment -Start            
//            if(txtFileName == null || txtFileName.equals(EMPTY_STRING)){
               if( formBean.getFileName() == null || formBean.getFileName().equals(EMPTY_STRING) ){
                CoeusOptionPane.showErrorDialog(messageResource.parseMessageKey("proposal_narr_exceptionCode.6615"));
                return false;
            }
        }
//Modified for Case 3673 - Should not allow narratives status to be "Complete" if no attachment -End        
        //Added for case #2366 end
        //JIRA COEUSQA 1540 - START
        /*TextValidator validator = new TextValidator(TextValidator.ALPHA+TextValidator.NUMERIC+TextValidator.PERIOD+TextValidator.UNDERSCORE+TextValidator.HYPHEN);
        boolean validate = validator.validate(txtArTitle.getText());
        if(!validate){
            //CoeusOptionPane.showErrorDialog(messageResource.parseMessageKey("proposal_narr_exceptionCode.6615"));
            CoeusOptionPane.showErrorDialog("Module Title "+messageResource.parseMessageKey("text_validation_exceptionCode.1000"));
            return false;
        }*/
        //JIRA COEUSQA 1540 - END
        return true;
        
    }
    
    /* Added for the Coeus Enhancement :1776 ,for focusing the component after the validation for the module title is fired start step:4*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    /*End CoeusEnhancement case:1776 step:4*/
    
    
    /** Method used to set the rights for user at proposal level.
     * @param proposalUserRights HashMap of user rights with user id as key and AccessType as value.
     */
    public void setProposalUserRights(java.util.HashMap proposalUserRights) {
        this.hmProposalUserRights = proposalUserRights;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlContents = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblModuleNo = new javax.swing.JLabel();
        txtFldModuleNo = new edu.mit.coeus.utils.CoeusTextField();
        lblSequenceNo = new javax.swing.JLabel();
        txtFldSequenceNo = new edu.mit.coeus.utils.CoeusTextField();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new edu.mit.coeus.utils.CoeusComboBox();
        lblContactName = new javax.swing.JLabel();
        txtFldContactName = new edu.mit.coeus.utils.CoeusTextField();
        lblPhone = new javax.swing.JLabel();
        txtFldPhone = new edu.mit.coeus.utils.CoeusTextField();
        lblEmail = new javax.swing.JLabel();
        txtFldEmail = new edu.mit.coeus.utils.CoeusTextField();
        lblComments = new javax.swing.JLabel();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        scrPnUsers = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        lblNarrativeType = new javax.swing.JLabel();
        cmbNarrativeType = new edu.mit.coeus.utils.CoeusComboBox();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new javax.swing.JTextField();
        pnlButtons = new javax.swing.JPanel();
        pnlInternalButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTemp = new javax.swing.JLabel();
        btnUpload = new edu.mit.coeus.utils.CoeusButton();
        btnView = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.BorderLayout());

        pnlMain.setLayout(new java.awt.BorderLayout());

        pnlContents.setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setText("Proposal No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblProposalValue, gridBagConstraints);

        lblModuleNo.setFont(CoeusFontFactory.getLabelFont());
        lblModuleNo.setText("Module No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblModuleNo, gridBagConstraints);

        txtFldModuleNo.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,2));
        txtFldModuleNo.setEditable(false);
        txtFldModuleNo.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtFldModuleNo, gridBagConstraints);

        lblSequenceNo.setFont(CoeusFontFactory.getLabelFont());
        lblSequenceNo.setText("Sequence No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlContents.add(lblSequenceNo, gridBagConstraints);

        txtFldSequenceNo.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,2));
        txtFldSequenceNo.setEditable(false);
        txtFldSequenceNo.setMinimumSize(new java.awt.Dimension(100, 20));
        txtFldSequenceNo.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(txtFldSequenceNo, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Module Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblTitle, gridBagConstraints);

        scrPnTitle.setMinimumSize(new java.awt.Dimension(200, 80));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(200, 80));
        txtArTitle.setDocument(new LimitedPlainDocument(150));
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(scrPnTitle, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setText("Status:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblStatus, gridBagConstraints);

        cmbStatus.setPreferredSize(new java.awt.Dimension(100, 19));
        cmbStatus.setShowCode(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(cmbStatus, gridBagConstraints);

        lblContactName.setFont(CoeusFontFactory.getLabelFont());
        lblContactName.setText("Contact Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblContactName, gridBagConstraints);

        txtFldContactName.setDocument(new LimitedPlainDocument(30));
        txtFldContactName.setMinimumSize(new java.awt.Dimension(200, 20));
        txtFldContactName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtFldContactName, gridBagConstraints);

        lblPhone.setFont(CoeusFontFactory.getLabelFont());
        lblPhone.setText("Phone Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblPhone, gridBagConstraints);

        txtFldPhone.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,20));
        txtFldPhone.setMinimumSize(new java.awt.Dimension(200, 20));
        txtFldPhone.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtFldPhone, gridBagConstraints);

        lblEmail.setFont(CoeusFontFactory.getLabelFont());
        lblEmail.setText("Email Address:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblEmail, gridBagConstraints);

        txtFldEmail.setDocument(new LimitedPlainDocument(60));
        txtFldEmail.setMinimumSize(new java.awt.Dimension(200, 20));
        txtFldEmail.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtFldEmail, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblComments, gridBagConstraints);

        scrPnComments.setMinimumSize(new java.awt.Dimension(200, 80));
        scrPnComments.setPreferredSize(new java.awt.Dimension(200, 80));
        txtArComments.setDocument(new LimitedPlainDocument(300));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(scrPnComments, gridBagConstraints);

        scrPnUsers.setMinimumSize(new java.awt.Dimension(300, 150));
        scrPnUsers.setPreferredSize(new java.awt.Dimension(300, 150));
        tblUsers.setFont(CoeusFontFactory.getNormalFont());
        scrPnUsers.setViewportView(tblUsers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 5, 5);
        pnlContents.add(scrPnUsers, gridBagConstraints);

        lblNarrativeType.setFont(CoeusFontFactory.getLabelFont());
        lblNarrativeType.setText("Narrative Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblNarrativeType, gridBagConstraints);

        cmbNarrativeType.setMinimumSize(new java.awt.Dimension(385, 19));
        cmbNarrativeType.setPreferredSize(new java.awt.Dimension(388, 19));
        cmbNarrativeType.setShowCode(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(cmbNarrativeType, gridBagConstraints);

        lblFileName.setFont(CoeusFontFactory.getLabelFont());
        lblFileName.setText("File Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblFileName, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setMaximumSize(new java.awt.Dimension(382, 20));
        txtFileName.setMinimumSize(new java.awt.Dimension(382, 20));
        txtFileName.setPreferredSize(new java.awt.Dimension(388, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(txtFileName, gridBagConstraints);

        pnlMain.add(pnlContents, java.awt.BorderLayout.CENTER);

        pnlButtons.setMaximumSize(new java.awt.Dimension(80, 143));
        pnlButtons.setMinimumSize(new java.awt.Dimension(80, 143));
        pnlButtons.setPreferredSize(new java.awt.Dimension(80, 143));
        pnlInternalButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(67, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(67, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(67, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlInternalButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInternalButtons.add(btnCancel, gridBagConstraints);

        lblTemp.setMaximumSize(new java.awt.Dimension(20, 99));
        lblTemp.setMinimumSize(new java.awt.Dimension(20, 99));
        lblTemp.setPreferredSize(new java.awt.Dimension(20, 99));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pnlInternalButtons.add(lblTemp, gridBagConstraints);

        btnUpload.setMnemonic('U');
        btnUpload.setLabel("Upload");
        btnUpload.setMaximumSize(new java.awt.Dimension(67, 23));
        btnUpload.setMinimumSize(new java.awt.Dimension(67, 23));
        btnUpload.setPreferredSize(new java.awt.Dimension(67, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInternalButtons.add(btnUpload, gridBagConstraints);

        btnView.setMnemonic('V');
        btnView.setText("View");
        btnView.setEnabled(false);
        btnView.setMaximumSize(new java.awt.Dimension(67, 23));
        btnView.setMinimumSize(new java.awt.Dimension(67, 23));
        btnView.setPreferredSize(new java.awt.Dimension(67, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlInternalButtons.add(btnView, gridBagConstraints);

        pnlButtons.add(pnlInternalButtons);

        pnlMain.add(pnlButtons, java.awt.BorderLayout.EAST);

        add(pnlMain, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    //Added for the Coeus Enhancemnt case:#1767 
    /**
     * Getter for property proposalStatusCode.
     * @return Value of property proposalStatusCode.
     */
    public int getProposalStatusCode() {
        return proposalStatusCode;
    }    

    /**
     * Setter for property proposalStatusCode.
     * @param proposalStatusCode New value of property proposalStatusCode.
     */
    public void setProposalStatusCode(int proposalStatusCode) {
        this.proposalStatusCode = proposalStatusCode;
    }    
    
    //Added for bug id 1860 step 4 : start
    /**
     * Getter for property isAlterProposal.
     * @return Value of property isAlterProposal.
     */
    public boolean isIsAlterProposal() {
        return isAlterProposal;
    }
    
    /**
     * Setter for property isAlterProposal.
     * @param isAlterProposal New value of property isAlterProposal.
     */
    public void setIsAlterProposal(boolean isAlterProposal) {
        this.isAlterProposal = isAlterProposal;
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    /**
     * Getter for property hierarchy.
     * @return Value of property hierarchy.
     */
    public boolean isHierarchy() {
        return hierarchy;
    }
    
    /**
     * Setter for property hierarchy.
     * @param hierarchy New value of property hierarchy.
     */
    public void setHierarchy(boolean hierarchy) {
        this.hierarchy = hierarchy;
    }          
    //bug id 1860 step 4 : end
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private edu.mit.coeus.utils.CoeusButton btnUpload;
    private edu.mit.coeus.utils.CoeusButton btnView;
    private edu.mit.coeus.utils.CoeusComboBox cmbNarrativeType;
    private edu.mit.coeus.utils.CoeusComboBox cmbStatus;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblContactName;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblModuleNo;
    private javax.swing.JLabel lblNarrativeType;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblProposalNo;
    private javax.swing.JLabel lblProposalValue;
    private javax.swing.JLabel lblSequenceNo;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTemp;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlContents;
    private javax.swing.JPanel pnlInternalButtons;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JScrollPane scrPnUsers;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextArea txtArComments;
    private javax.swing.JTextArea txtArTitle;
    private javax.swing.JTextField txtFileName;
    private edu.mit.coeus.utils.CoeusTextField txtFldContactName;
    private edu.mit.coeus.utils.CoeusTextField txtFldEmail;
    private edu.mit.coeus.utils.CoeusTextField txtFldModuleNo;
    private edu.mit.coeus.utils.CoeusTextField txtFldPhone;
    private edu.mit.coeus.utils.CoeusTextField txtFldSequenceNo;
    // End of variables declaration//GEN-END:variables
    
  class ModuleUserEditor extends DefaultCellEditor implements ItemListener {
        
    private JPanel pnlRadioButtons;
    private JRadioButton rbtnRead, rbtnModify, rbtnNone;
    private ButtonGroup btnGroup;
    ProposalNarrativeModuleUsersFormBean userFormBean = null;
    /**
     * Default constructor which sets the default font for this editor
     */

    
    public ModuleUserEditor() {
        super( new JCheckBox() );
        pnlRadioButtons = new JPanel( new FlowLayout(FlowLayout.LEFT,0,0));
        rbtnRead = new JRadioButton("Read");
        rbtnModify = new JRadioButton("Modify");
        rbtnNone = new JRadioButton("None");
        btnGroup = new ButtonGroup();
        btnGroup.add( rbtnRead );
        btnGroup.add( rbtnModify );
        btnGroup.add( rbtnNone );
        rbtnRead.addItemListener( this );
        rbtnModify.addItemListener( this );
        rbtnNone.addItemListener( this );
        
        rbtnRead.setFont( CoeusFontFactory.getNormalFont() );
        rbtnModify.setFont( CoeusFontFactory.getNormalFont() );
        rbtnNone.setFont( CoeusFontFactory.getNormalFont() );
        pnlRadioButtons.add( rbtnRead );
        pnlRadioButtons.add( rbtnModify );
        pnlRadioButtons.add( rbtnNone );
    }
    
    public Component getTableCellEditorComponent(JTable table,
        Object value, boolean isSelected, int row, int column) {
        boolean showIcon=false;    
        if( value != null ) {
            
            userFormBean = ( ProposalNarrativeModuleUsersFormBean ) value;
            char accessType = userFormBean.getAccessType();
            switch ( accessType ) {
                case 'R' :
                            rbtnRead.setSelected( true );
                            break;
                case 'M' :
                            rbtnModify.setSelected( true );
                            break;
                case 'N':
                            rbtnNone.setSelected( true );
                            break;
            }
        }
        return pnlRadioButtons;
    }
    public int getClickCountToStart() {
        return 1;
    }
    public Object getCellEditorValue() {
        return userFormBean;
    }

    public void itemStateChanged(ItemEvent e) {
        //super.fireEditingStopped();
        saveRequired = true;
        Object source = e.getSource();
        if( source.equals( rbtnRead ) && e.getStateChange() == e.SELECTED ) {
            userFormBean.setAccessType( 'R' );
        }else if ( source.equals( rbtnModify ) && e.getStateChange() == e.SELECTED ) {
            userFormBean.setAccessType( 'M' );
        }else if( source.equals( rbtnNone ) && e.getStateChange() == e.SELECTED ) {
            userFormBean.setAccessType( 'N' );
        }
        if( userFormBean.getAcType() == null ) {
            userFormBean.setAcType( UPDATE_RECORD );
        }
    }
        
  }
  
  //Added for case#2420 - Upload of files on Edit Module Details Window - start
  /**
   * This method checks whether the file is uploaded or not
   * @return isFileExists boolean A flag indicating whether the file is uploaded or not
   */
  private boolean isPDFFilePresent(){
      boolean isFileExists = false;
      String fileName = txtFileName.getText();
      if(!fileName.equals("")){
          isFileExists = true;          
      }else{
          isFileExists = false;          
      }
      return isFileExists;
  }
  
  /**
   * This method checks the narrative for modify rigths and logged in user for module rights,
   * sets the proposalNarrativePDFSourceBean's properties,
   * displays the file chooser dialog for uploading the file and
   * sets the status to complete or incomplete depending on whether
   * the file is uploaded or not
   * @exception Exception
   */
  private void setPdfProperties() throws Exception{      
      boolean hasAlterRight = false;
      String moduleNumber = (new Integer(formBean.getModuleNumber())).toString();
      // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
//      if(!hasModifyNarrativeRights() &&!hasAlterRight && !isUserHasModuleRight(moduleNumber)){
//          CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(INSUFFICIENT_UPLOAD_RIGHTS));
//          return;
//      }
//      
//      if(hasModifyNarrativeRights() || hasAlterRight){
//          if(!isUserHasModuleRight(moduleNumber)){
//              CoeusOptionPane.showErrorDialog(messageResource.parseMessageKey(INSUFFICIENT_UPLOAD_RIGHTS));
//              return;
//          }
//      } 
      // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
      if(txtFileName.getText() != null){
          oldFileName = txtFileName.getText().trim();
      }      
                  
      fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
      //Code commented for Case#3648 - Uploading non-pdf files
      //fileChooser.setSelectedFileExtension("pdf");
      fileChooser.setAcceptAllFileFilterUsed(true);
      fileChooser.showFileChooser();
      //Added for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
      isDocumentUploaded = false;
      //COEUSDEV-340 : END
      if(fileChooser.isFileSelected()){
          proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();      
          proposalNarrativePDFSourceBean.setProposalNumber(formBean.getProposalNumber());
          proposalNarrativePDFSourceBean.setModuleNumber(formBean.getModuleNumber());      
          if(formBean.getHasPDFModuleNumber()){
              proposalNarrativePDFSourceBean.setAcType(TypeConstants.UPDATE_RECORD);
          }else{
              proposalNarrativePDFSourceBean.setAcType(TypeConstants.INSERT_RECORD);
          }         
          //Added for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
          //setting flag to true when a document is uploaded
          isDocumentUploaded = true;
          //COEUSDEV-340 : end
          String fileName = fileChooser.getSelectedFile();
          File file = fileChooser.getFileName();
          String path = file.getName();
          txtFileName.setText(path);
          proposalNarrativePDFSourceBean.setFileName(path);
          proposalNarrativePDFSourceBean.setFileBytes(fileChooser.getFile());
          //Added with Case 4007:Icon based on attachmentType
          CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
          String mimeType = docUtils.getDocumentMimeType(proposalNarrativePDFSourceBean);
          proposalNarrativePDFSourceBean.setMimeType(mimeType);
          formBean.setMimeType(mimeType);
          //4007 End
          formBean.setAttachmentAcType(proposalNarrativePDFSourceBean.getAcType());
          if(fileName != null && !fileName.trim().equals("")){
              int index = fileName.lastIndexOf('.');
              if(index != -1 && index != fileName.length()){
                  extension = fileName.substring(index+1, fileName.length());
                  if(extension != null){
                      proposalNarrativePDFSourceBean.setFileBytes(fileChooser.getFile());  
                      formBean.setFileBytes(fileChooser.getFile());
                      //Added for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start             
                      setSaveRequired(true);
                      //COEUSDEV-340 : End
                  }else{
                      CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey("proposal_narr_exceptionCode.6609"));                      
                  }
              }else{
                  CoeusOptionPane.showErrorDialog(messageResource.parseMessageKey("correspType_exceptionCode.1012"));
              }
          }
      }else{                 
          if(txtFileName.getText() == null || txtFileName.getText().equals(EMPTY_STRING)){
              proposalNarrativePDFSourceBean = null;  
          }
      }        
      boolean isFilePresent = isPDFFilePresent();
      if(isFilePresent){
//  Modified for Case 3671 - Narrative status default should be 'Incomplete' not 'Complete' -Start
//        cmbStatus.setSelectedIndex(0);
          //Case 4230 - START
          if(proposalStatusCode != 2) {//Change the narrative module status only if proposal status is NOT Approval In Progress
              cmbStatus.setSelectedIndex(1);
          }
          //Case 4230 - END
//  Modified for Case 3671 - Narrative status default should be 'Incomplete' not 'Complete' -End         
          btnView.setEnabled(true);
      }else{
          cmbStatus.setSelectedIndex(1);
          btnView.setEnabled(false);
      }
  }
    
  /**
   * This method saves the pdf document to database table OSP$ in BLOB format
   * @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean   
   * @return successFlag boolean Indicating whether the the save was success or not 
   * @exception Exception
   */
  private boolean saveToDataBase(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean) throws Exception {
      boolean successFlag = false;
      RequesterBean request = new RequesterBean();         
      request.setFunctionType(UPDATE_NARRATIVE_PDF);      
      request.setDataObject(proposalNarrativePDFSourceBean);
      request.setId(CoeusGuiConstants.getMDIForm().getUnitNumber());
      AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, request);
      comm.send();
      ResponderBean response = comm.getResponse();
      
      if(response.getId().equals("1")){
          //Added for case 3183 - Proposal Hierarchy enhancements - start  
          Vector serverDataObjects = response.getDataObjects();
            if(serverDataObjects!=null && serverDataObjects.size()>0){
            	proposalNarrativePDFSourceBean = 
                (ProposalNarrativePDFSourceBean)serverDataObjects.get(0);
            }
            //Added for case 3183 - Proposal Hierarchy enhancements - end
            observable.setFunctionType(functionType);
            HashMap hmFileData = new HashMap();
            hmFileData.put("isFilePresent", new Boolean(true));
            hmFileData.put("fileName", proposalNarrativePDFSourceBean.getFileName());
            //Added for case 3183 - Proposal Hierarchy enhancements - start  
            hmFileData.put("proposalNarrativePDFSourceBean", proposalNarrativePDFSourceBean);
            //Added for case 3183 - Proposal Hierarchy enhancements - end
            observable.notifyObservers(hmFileData);            
            successFlag = true;          
      }else if(response.getId().equals("-1")){   
            serverMsg = messageResource.parseMessageKey("proposal_narr_exceptionCode.6617") +" " 
                        +proposalNarrativePDFSourceBean.getProposalNumber() +" "
                        +messageResource.parseMessageKey("proposal_narr_exceptionCode.6618");                       
            successFlag = false;
      }else{            
            serverMsg = messageResource.parseMessageKey("proposal_narr_exceptionCode.6619");
            successFlag = false;          
      }
      return successFlag;
  }
  
  /**
   * This method updates the Inbox details
   * @param type String
   * @exception CoeusClientException
   */
  private void updateInboxDetails(String type) throws CoeusClientException {  
      
      String proposalNumber = formBean.getProposalNumber();
      String moduleNumber = (new Integer(formBean.getModuleNumber())).toString();
      String message = "A new "+type+" file is uploaded for proposal \'"+proposalNumber+ "\' for Module No." +moduleNumber;
      String connectTo = CoeusGuiConstants.CONNECTION_URL +CoeusGuiConstants.PROPOSAL_SERVLET ;      
      RequesterBean request = new RequesterBean();
      String fromUser= CoeusGuiConstants.getMDIForm().getUserName();      
      request.setFunctionType(SEND_NOTIFICATION);
      request.setId(proposalNumber);
      request.setDataObject(message);
      request.setUserName(fromUser);      
      AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
      comm.send();
      ResponderBean responderBean = comm.getResponse();
      if(responderBean!= null){
          if(!responderBean.isSuccessfulResponse()){
              throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
          }
      }
  } 
  
  /**
   * This method saves the pdf document to database by calling the method saveToDataBase()
   * @exception Exception
   */
  private void uploadPdfDocument() throws Exception{
      if(saveToDataBase(proposalNarrativePDFSourceBean)){
          formBean.setHasPDFModuleNumber(true);
        
          //Commented for case#2999 - Multiple emails generated when a document is uploaded - start
//          if(functionType == MODIFY_MODE){          
//              try {                  
//                  updateInboxDetails(extension);
//              }catch(CoeusClientException coeusClientException){
//                  CoeusOptionPane.showDialog(coeusClientException);
//              }
//          } 
          //Commented for case#2999 - Multiple emails generated when a document is uploaded - end
      }else{
          CoeusOptionPane.showErrorDialog(serverMsg);
      }
  }
  
  /**
   * This method facilitates for viewing of PDF documents
   * @exception Exception
   */
  private void viewPdfDocument() throws Exception{
      Map map = new HashMap();
      if(txtFileName != null && !txtFileName.equals(EMPTY_STRING)){
          Vector dataObjects = new Vector();
          if(proposalNarrativePDFSourceBean == null){
              //Indicates that the PDF is existing in the database
              proposalNarrativePDFSourceBean = new ProposalNarrativePDFSourceBean();
              proposalNarrativePDFSourceBean.setProposalNumber(formBean.getProposalNumber());
              proposalNarrativePDFSourceBean.setModuleNumber(formBean.getModuleNumber());
              HashMap hmBlobData = new HashMap();
              hmBlobData = getBlobDataForNarrative(proposalNarrativePDFSourceBean);
              proposalNarrativePDFSourceBean.setFileName(hmBlobData.get("fileName").toString());
              proposalNarrativePDFSourceBean.setFileBytes((byte[])hmBlobData.get("fileBytes"));
              //Added with Case 4007:Icon based on attachmentType
              CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
              proposalNarrativePDFSourceBean.setMimeType(docUtils.getDocumentMimeType(proposalNarrativePDFSourceBean));
              //4007 End
              proposalNarrativePDFSourceBean.setAcType(TypeConstants.UPDATE_RECORD);
              map.put("VIEW_DOCUMENT", new Boolean(false));
          }else{
              //Indicates that the PDF is just selected to be uploaded, its not present in the database
              map.put("VIEW_DOCUMENT", new Boolean(true));
          }
          dataObjects.add(0, proposalNarrativePDFSourceBean);
          dataObjects.add(1, CoeusGuiConstants.getMDIForm().getUnitNumber());          
          RequesterBean requesterBean = new RequesterBean();
          DocumentBean documentBean = new DocumentBean();          
          map.put("DATA", dataObjects);
          map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.NarrativeDocumentReader");
          map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
          map.put("DOC_TYPE", "NARRATIVE_PDF");
          map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));          
          documentBean.setParameterMap(map);
          requesterBean.setDataObject(documentBean);
          requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
          
          AppletServletCommunicator comm
                  = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
          comm.send();
          ResponderBean response = comm.getResponse();
          if(response.isSuccessfulResponse()){
              map = (Map)response.getDataObject();
              String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
              reportUrl = reportUrl.replace('\\', '/');
              URL urlObj = new URL(reportUrl);
              URLOpener.openUrl(urlObj);
          }else {
              CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey(response.getMessage()));
          }
      }else{
          CoeusOptionPane.showInfoDialog(messageResource.parseMessageKey("proposal_narr_exceptionCode.6606"));
          return ;
      }
  }
    
  /**
   * This method checks whether the narrative has modify rights
   * @exception Exception
   * @return hasRight boolean Indicating whether the narrative has modify rights
   */
  private boolean hasModifyNarrativeRights() throws Exception {
      boolean hasRight = false;
      String connectTo = CoeusGuiConstants.CONNECTION_URL +CoeusGuiConstants.PROPOSAL_SERVLET ;      
      RequesterBean request = new RequesterBean();      
      request.setFunctionType(GET_MODIFY_NARRATIVE_DATA);
      request.setId(formBean.getProposalNumber());
      request.setDataObject(CoeusGuiConstants.getMDIForm().getUnitNumber());      
      AppletServletCommunicator comm = new AppletServletCommunicator( connectTo, request );
      comm.send();
      ResponderBean response = comm.getResponse();
      if (response == null) {
          response = new ResponderBean();
          response.setResponseStatus(false);
          response.setMessage(messageResource.parseMessageKey("server_exceptionCode.1000"));
      }
      if (response.isSuccessfulResponse()) {
          Boolean data = (Boolean) response.getDataObject();
          hasRight = data.booleanValue();
      } else {
          throw new Exception(response.getMessage());
      }
      return hasRight;
  } 
  
  /**
   * This method checks whether the logged in user has module rights
   * @param modNumber String
   * @return hasRight boolean Indicating whether the logged in user has module rights
   */  
  private boolean isUserHasModuleRight(String modNumber){
      
      Vector modUsers = new Vector();
      HashMap hmUsers = new HashMap();      
      modUsers = formBean.getPropModuleUsers();
      if(modUsers != null){
          hmUsers.put(""+formBean.getModuleNumber(), modUsers);          
      }           
      boolean hasRight = false;
      if(hmUsers != null && hmUsers.containsKey(modNumber)) {
          Vector userRights = (Vector) hmUsers.get(modNumber);
          String loggedInUser = CoeusGuiConstants.getMDIForm().getUserName();
          for( int indx=0; indx < userRights.size(); indx++) {
              ProposalNarrativeModuleUsersFormBean userFormBean = (ProposalNarrativeModuleUsersFormBean)userRights.elementAt(indx);
              if( userFormBean.getUserId().equalsIgnoreCase(loggedInUser)){
                  if( userFormBean.getAccessType() == 'M' ){
                      return true;
                  }
              }
          }
      }
      return hasRight;
  } 
  
  /**
   * This method gets the narrative details for a given proposalNumber
   * @param proposalNumber String
   * @return dataObjects Vector Containing narrative details
   * @exception Exception
   */
  private Vector getNarrativeDetails(String proposalNumber) throws Exception{
      Vector dataObjects = null;
      Vector inDataObjects = new Vector();
      String connectTo = CoeusGuiConstants.CONNECTION_URL +CoeusGuiConstants.PROPOSAL_SERVLET ;
      
      RequesterBean request = new RequesterBean();      
      request.setFunctionType(GET_NARRATIVE);
      request.setId( proposalNumber );
      request.setDataObject(new Character(functionType));
      inDataObjects.add(0, new Boolean(true));
      inDataObjects.add(1, CoeusGuiConstants.getMDIForm().getUnitNumber());
      request.setDataObjects(inDataObjects);
      AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
      comm.send();
      ResponderBean response = comm.getResponse();
      if (response == null){
          response = new ResponderBean();
          response.setResponseStatus(false);
          response.setMessage(messageResource.parseMessageKey("server_exceptionCode.1000"));
      }
      if (response.isSuccessfulResponse()){
          dataObjects = response.getDataObjects();
      }else if( response.isLocked()){
          String msg = messageResource.parseMessageKey("narrative_row_locked_exceptionCode.123456");
          int resultConfirm = CoeusOptionPane.showQuestionDialog(msg, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
          if (resultConfirm == 0){
              dataObjects = response.getDataObjects();
              functionType = DISPLAY_MODE;
              return dataObjects;
          }else{
              throw new Exception("");
          }          
      }else{
          throw new Exception(response.getMessage());
      }
      return dataObjects;
  }   
  
  /**
   * This method gets the hasAlterRight
   * @return hasAlterRight boolean Indicating whether the user has alter rights
   * @exception Exception
   */
  private boolean getHasAlterRight() throws Exception{
      boolean hasAlterRight = false;
      Vector vecData = getNarrativeDetails(formBean.getProposalNumber());
      if(vecData != null && vecData.size() > 1){
          boolean rightExists = ((Boolean)vecData.elementAt(0)).booleanValue();
          boolean hasOnlyViewRight=((Boolean)vecData.elementAt(4)).booleanValue();
          if(rightExists && functionType != DISPLAY_MODE && !hasOnlyViewRight){
              functionType = MODIFY_MODE;
          }else{
              hasAlterRight =((Boolean)vecData.elementAt(5)).booleanValue();
          }
      }
      return hasAlterRight;
  }  
  
  /**
   * This method gets the blob data for a given proposal and module number
   * @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean bean properties like proposalNumber
   * moduleNumber, etc are set
   * @return hmBlobData HashMap containing fileName and fileBytes
   * @exception Exception
   */
  private HashMap getBlobDataForNarrative(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean) throws Exception{
      HashMap hmBlobData = new HashMap();
      String connectTo = CoeusGuiConstants.CONNECTION_URL +CoeusGuiConstants.PROPOSAL_SERVLET ;
      RequesterBean request = new RequesterBean();      
      request.setFunctionType(GET_BLOB_DATA_FOR_NARRATIVE);      
      request.setDataObject(proposalNarrativePDFSourceBean);    
      AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
      comm.send();
      
      ResponderBean response = comm.getResponse();
      ProposalNarrativePDFSourceBean propNarrativePDFSourceBean = 
              (ProposalNarrativePDFSourceBean)response.getDataObject();
      
      hmBlobData.put("fileName", propNarrativePDFSourceBean.getFileName());
      hmBlobData.put("fileBytes", propNarrativePDFSourceBean.getFileBytes());
      return hmBlobData;      
  }
  //Added for case#2420 - Upload of files on Edit Module Details Window - end

  //Added for case#2999 - Multiple emails generated when a document is uploaded - start
  public boolean isCanModifyNarrative() {
      return canModifyNarrative;
  }
  
  public void setCanModifyNarrative(boolean canModifyNarrative) {
      this.canModifyNarrative = canModifyNarrative;
  }
  //Added for case#2999 - Multiple emails generated when a document is uploaded - end
}