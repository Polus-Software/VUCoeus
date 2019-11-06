/*
 * @(#)ProposalNarrativeForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May 17, 2003, 3:29 PM
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.*;

import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.propdev.bean.ProposalHierarchyLinkBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeModuleUsersFormBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTypeBean;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.query.Equals;
import java.util.*;
import java.net.URL;
import java.applet.AppletContext;
import java.io.File;

/**
 *
 * @author  ravikanth
 */
public class ProposalNarrativeForm extends CoeusInternalFrame  
    implements ActionListener,ListSelectionListener,TypeConstants , Observer {
    
    private char functionType;
    private String proposalNumber;
    private CoeusMenuItem addModule, modifyModule, deleteModule, moveUp, moveDown, 
            editSource, viewPdf, viewPdfFiles, viewWordFiles, uploadWordFiles, uploadPdfFiles;
    
    private CoeusToolBarButton btnAdd, btnModify, btnDelete, btnUp, btnDown, 
            btnUploadWord, btnUploadPdf, btnSave, btnClose;
    
    private CoeusMenu viewFiles,uploadFiles;
    
    private CoeusAppletMDIForm mdiForm; 
    
    //Code Added by Vyjayanthi - Start
    private CoeusFileChooser fileChooser;
    private static final String WORD_FORMAT = "doc";
    private static final String PDF_FORMAT = "pdf";
    //To upload any file Case #1779- start
     private static final String ANY_FORMAT = "All Files";
     ////To upload any file Case #1779- end
     
    private static final char GET_NARRATIVE_PDF = 'k';
    private static final char GET_NARRATIVE_SOURCE = 'l';
    
    private static final char UPDATE_NARRATIVE_PDF = 'm';
    private static final char UPDATE_NARRATIVE_SOURCE = 'n';
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL + 
                           CoeusGuiConstants.PROPOSAL_SERVLET ;
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String INSUFFICIENT_RIGHTS = "proposal_narr_exceptionCode.6610";
    private static final String INSUFFICIENT_UPLOAD_RIGHTS = "proposal_narr_exceptionCode.6612";
    //Code Added by Vyjayanthi - End
    
    //Added by shiji for Case id : 2016 step 1 - start
    private static final char SEND_NOTIFICATION = 'd';
    //Case id : 2016 step 1 - end
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusDlgWindow dlgDetails;
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";

    private static final char GET_NARRATIVE_DETAILS = 'N';
    private static final char GET_NARRATIVE_USERS_FOR_PROPOSAL = 'F';
    private static final char UPDATE_NARRATIVE_DETAILS = 'L';
    private static final char USER_HAS_RIGHT = 'U'; 
    //Added for case #1856 start 1
    private static final char GET_MODIFY_NARRATIVE_DATA = 'e';
    //Added for case #1856 end 1
    // Added by Shiji for Right Checking : step 1 : start
    private static final char GET_NARRATIVE = 'c';
    //Right Checking : step 1 : end
    /*Added for the Coeus Enhancement case:1776 ,to get the narrative data on the click of the Add button start step:1*/
    private static final char GET_ADD_NARRATIVE_DATA = 'x';
    private String paramNarrativeTypeCode;
    /*End Coeus Enhancement case:1776 step:1*/
    
    private HashMap hmModules = new HashMap();
    private HashMap hmUsers = new HashMap();
    private HashMap hmModulesToDB = new HashMap();
    private HashMap hmDeletedModules = new HashMap();
    private HashMap hmProposalUserRights = new HashMap();
    private Vector moduleTableData = new Vector();
    private HashMap hmStatusCodes = new HashMap();
    private int maxModuleNo,maxSeqNo;
     // Added by Shiji for Right Checking : step 2 : start
    private boolean isOpenedFromPropListWindow;
    private String unitNumber;
    //Right Checking : step 2 : end
    ProposalNarrativeModuleDetails moduleDetails;
    ProposalNarrativeFormBean proposalNarrativeFormBean;
    private Vector usersForProposal;
    private Vector statusLookUp = new Vector();
    private boolean saveRequired;
    private boolean modifiable = true;
    ProposalNarrativeFormBean existingBean;
    //Added for bug id 1860 step 1 : start
    boolean hasAlterRight=false;
    boolean rightExists = false;
   //bug id 1860 step 1 : end
    
    /*added for the Coeus Enhancements case:1624 
    * to hold all the narrative types from the database*/
    private Vector narrativeTypes = new Vector();
    
    /*added for the Coeus Enhancements case:1624 
    * to hold all the narrative types after all the filterations*/
    private Vector moduleNarrativeTypes = new Vector();
    
    private Vector narrativeBean;
    
    //Added for the Coeus Enhancement case:#1767
    private int proposalStatusCode;
    
    private boolean hierarchy;
    private boolean parent;
    //Added for case#2420 - Upload of files on Edit Module Details Window - start
    private static final String EMPTY_STRING = "";
    String serverMsg = EMPTY_STRING;    
    String moduleTitle = EMPTY_STRING;
    //Added for case#2420 - Upload of files on Edit Module Details Window - end
    
    //Code added for Proposal Hierarchy case#3183 - starts
    private CoeusVector cvPropHierLink;
    private HashMap hmPropHierLink;
    //Code added for Proposal Hierarchy case#3183 - ends
    // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start 
    private boolean canModifyNarrative = false;
     // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -End 
    // COEUSDEV-308: Application not checking module level rights for a user in Narrative- Start
    private boolean hasPropRightToModify = false;
    private boolean hasOSPDeptRightToModify = false;
    // COEUSDEV-308: Application not checking module level rights for a user in Narrative - End
    //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
    private static final int APPROVAL_IN_PROGRESS_STATUS = 2;
    private static final int APPROVED_STATUS = 4;
    private static final String CAN_MODIFY_NARRATIVE = "proposal_narr_can_modify_exceptionCode.6621";
    private static final String CANNOT_MODIFY_NARRATIVE = "proposal_narr_cannot_modify_exceptionCode.6623";
    //COEUSQA-1579 : End
    /** Creates new form ProposalNarrative */
    public ProposalNarrativeForm(String proposalNumber, char functionType, 
        CoeusAppletMDIForm mdiForm ) {
        super("", mdiForm);
        coeusMessageResources = CoeusMessageResources.getInstance();            
        this.proposalNumber = proposalNumber;
        this.functionType = functionType;
        this.mdiForm = mdiForm;

        this.setToolsMenu(null); 
        this.setFrameMenu(getEditMenu());  
        this.setFrameToolBar(getToolBar());
        setFrameIcon(mdiForm.getCoeusIcon());        
        hackToGainFocus();
        hmStatusCodes.put("C","Complete");
        hmStatusCodes.put("I","Incomplete");
        statusLookUp.addElement( new ComboBoxBean("C","Complete"));
        statusLookUp.addElement( new ComboBoxBean("I","Incomplete"));
    }
    
    private void hackToGainFocus() {
        JFrame frame = new JFrame();
        frame.setLocation(-200,100);
        frame.setSize( 100, 100 );
        frame.show();
        frame.dispose();
    }
        
    /**
     * Constructs edit menu with sub menu items like New, Modify Moduleetc..
     * 
     * @return CoeusMenu ProposalNarrative edit menu
     */
    private CoeusMenu getEditMenu() {
        CoeusMenu menuNarrative = null;
        Vector fileChildren = new Vector();

        addModule = new CoeusMenuItem("Add", null, true, true);
        addModule.setMnemonic('A');
        addModule.addActionListener(this);

        modifyModule = new CoeusMenuItem("Modify", null, true, true);
        modifyModule.setMnemonic('M');
        modifyModule.addActionListener(this);

        deleteModule = new CoeusMenuItem("Delete module", null, true, true);
        deleteModule.addActionListener(this);

        moveUp = new CoeusMenuItem("Move Up", null, true, true);
        moveUp.addActionListener(this);

        moveDown = new CoeusMenuItem("Move Down", null, true, true);
        moveDown.addActionListener(this);

        editSource = new CoeusMenuItem("Edit Word Source", null, true, true);
        editSource.addActionListener(this);

        viewPdf = new CoeusMenuItem("View PDF File...", null, true, true);
        viewPdf.addActionListener(this);

        
        viewWordFiles = new CoeusMenuItem("View Word Files", null, true, true);
        viewWordFiles.addActionListener(this);

        //Modified for case#2420 - Upload of files on Edit Module Details Window - start
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//        viewPdfFiles = new CoeusMenuItem("View PDF File", null, true, true);
        viewPdfFiles = new CoeusMenuItem("View Attachment", null, true, true);
        viewPdfFiles.addActionListener(this);
        
        
        uploadWordFiles = new CoeusMenuItem("Upload Word Files", null, true, true);
        uploadWordFiles.addActionListener(this);

        //Modified for case#2420 - Upload of files on Edit Module Details Window - start
//        uploadPdfFiles = new CoeusMenuItem("Upload PDF File", null, true, true);
        uploadPdfFiles = new CoeusMenuItem("Upload Attachment", null, true, true);
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
        uploadPdfFiles.addActionListener(this);
        
        //Code commented for case#2420 - Upload of files on Edit Module Details Window - start
        /*
        Vector viewChildren = new Vector();
        viewChildren.add( viewWordFiles );
        viewChildren.add( viewPdfFiles );
        viewFiles = new CoeusMenu("View Files",null,viewChildren,true,true);
        
        Vector uploadChildren = new Vector();
        uploadChildren.add( uploadWordFiles );
        uploadChildren.add( uploadPdfFiles );
        uploadFiles = new CoeusMenu("Upload Files",null,uploadChildren,true,true);
        */
        //Code commented for case#2420 - Upload of files on Edit Module Details Window - end
        
        fileChildren.add(addModule);
        fileChildren.add(modifyModule);
        fileChildren.add(deleteModule);
        fileChildren.add(moveUp);
        fileChildren.add(moveDown);
        fileChildren.add(SEPERATOR);
        //Code modified for case#2420 - Upload of files on Edit Module Details Window
        fileChildren.add(viewPdfFiles);
        fileChildren.add(uploadPdfFiles);

        menuNarrative = new CoeusMenu("Edit", null, fileChildren, true, true);
        menuNarrative.setMnemonic('E');
        return menuNarrative;

    }
    
    /**
     * This method is used to create the tool bar for add, modfiy, delete module etc.
     *
     * @returns JToolBar ProposalNarrative Toolbar
     */
    private JToolBar getToolBar() {
        JToolBar toolbar = new JToolBar();

        btnAdd = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),null, "Add module");
        
        btnAdd.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DADD_ICON)));

        btnModify = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        null, "Modify module");

        btnModify.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DEDIT_ICON)));
        
        btnDelete = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
        null, "Delete module");

        btnDelete.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DDELETE_ICON)));

        btnUp = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_MOVE_UP_ICON)),
        null, "Move module up one row");

        btnUp.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_UP_ICON)));
        
        btnDown = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_MOVE_DOWN_ICON)),
        null, "Move module down one row");

        btnDown.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_DOWN_ICON)));

        btnUploadWord = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON)),
        null, "Upload Word File");
        
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//        btnUploadPdf = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON)),
//        null, "Upload PDF File");
        btnUploadPdf = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON)), null, "Upload Attachment");              
        
//        btnUploadPdf.setDisabledIcon(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
        btnUploadPdf.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_ATTACHMENT_ICON)));        
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end

        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");

        btnSave.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DSAVE_ICON)));
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");

        btnAdd.addActionListener(this);
        btnClose.addActionListener(this);
        btnDelete.addActionListener(this);
        btnDown.addActionListener(this);
        btnUploadWord.addActionListener(this);
        btnModify.addActionListener(this);
        btnSave.addActionListener(this);
        btnUp.addActionListener(this);
        btnUploadPdf.addActionListener(this);

        toolbar.add(btnAdd);
        toolbar.add(btnModify);
        toolbar.add(btnDelete);
        toolbar.add(btnUp);
        toolbar.add(btnDown);
        toolbar.addSeparator();
        //Code commented for case#2420 - Upload of files on Edit Module Details Window - start
        //toolbar.add(btnUploadWord );
        toolbar.add(btnUploadPdf);
        toolbar.addSeparator();
        toolbar.add(btnSave);
        toolbar.addSeparator();
        toolbar.add(btnClose);

        toolbar.setFloatable(false);
        return toolbar;
    }
    
    //Modified for bug id 1860 step 2 : start
    private void formatFields(){
        
    	// 9-28-2011 JM we don't want to allow narrative uploads in APPROVED status
        if (proposalStatusCode == APPROVED_STATUS) {
        	functionType = DISPLAY_MODE;
        }
		// JM END
    	
        boolean enable = functionType == DISPLAY_MODE ? false : true;
        //Code commented for Proposal Hierarchy case#3183
//        if(isHierarchy() && !isParent()){
//            enable = false;
//        }
        btnSave.setEnabled( enable );
        if(enable && hasAlterRight) {
            btnAdd.setEnabled( false );
            addModule.setEnabled( false );
            btnDelete.setEnabled( false );
            deleteModule.setEnabled( false );
        }else {
            btnAdd.setEnabled( enable );
            addModule.setEnabled( enable );
            btnDelete.setEnabled( enable );
            deleteModule.setEnabled( enable );
        }
        btnModify.setEnabled( enable );
        btnUp.setEnabled( enable );
        btnDown.setEnabled( enable );
        btnSave.setEnabled(enable);
        
        modifyModule.setEnabled( enable );
        moveDown.setEnabled( enable );
        moveUp.setEnabled( enable );
        
        editSource.setEnabled( enable );
        viewPdf.setEnabled( enable );
        //viewWordFiles.setEnabled( enable );
        //viewPdfFiles.setEnabled( enable );
        uploadWordFiles.setEnabled( enable );
        uploadPdfFiles.setEnabled( enable );
        btnUploadWord.setEnabled( enable );
        btnUploadPdf.setEnabled( enable );
        
        if( tblModule.getRowCount() == 0 ) {
            
            btnModify.setEnabled( false );
            btnDelete.setEnabled( false );
            btnUploadWord.setEnabled( false );
            btnUploadPdf.setEnabled( false );
            //btnSave.setEnabled( false );
            btnUp.setEnabled( false);
            btnDown.setEnabled( false );
            
            modifyModule.setEnabled( false );
            deleteModule.setEnabled( false );
            moveDown.setEnabled( false );
            moveUp.setEnabled( false );
            editSource.setEnabled( false );
            //Code modified for Proposal Hierarchy case#3183
            //disabling the buttons if no narrative data found
            viewPdf.setEnabled( false );
            //viewWordFiles.setEnabled( false );
            viewPdfFiles.setEnabled( false );
            uploadWordFiles.setEnabled( false );
            uploadPdfFiles.setEnabled( false );
            if(enable && hasAlterRight) {
                btnSave.setEnabled(false);
            }
        }else if( tblModule.getRowCount() == 1 ){
            btnUp.setEnabled( false);
            btnDown.setEnabled( false );
            moveDown.setEnabled( false );
            moveUp.setEnabled( false );
            
        }
        //Code added for case#3316 - Bug fix - starts
        //Disable Add button for status code 2 or 4
        
        if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS || proposalStatusCode == APPROVED_STATUS){
            btnAdd.setEnabled(false);
            addModule.setEnabled(false);
            btnDelete.setEnabled(false);
            deleteModule.setEnabled(false);
        }
        
        //Code added for case#3316 - Bug fix - ends
    }
    // bug id 1860 step 2: end

  //Modified for bug id 1860 step 3: start
  public void showNarrativeForm() throws Exception{
        Vector vecData = getNarrativeDetails( proposalNumber );
        Vector narrativeData = new Vector();
        if( vecData != null && vecData.size() > 1 ) {
            //Modified by shiji for Right Checking - step 3 : start
            rightExists = ((Boolean)vecData.elementAt(0)).booleanValue();
            //Code commented and modified for case#3316 - Bug fix - starts
            //for checking Narrative rights
//            boolean hasOnlyViewRight=((Boolean)vecData.elementAt(4)).booleanValue();
            //Right Checking - step 3 : end
//            if( rightExists  && functionType != DISPLAY_MODE && !hasOnlyViewRight) {
//                functionType = MODIFY_MODE;
//            }else{
//                //Added for the Coeus Enhancemnt case:#1767
//                hasAlterRight=((Boolean)vecData.elementAt(5)).booleanValue();
//                if(hasAlterRight && (proposalStatusCode == 2 || proposalStatusCode == 4)){
//                    functionType = MODIFY_MODE;
//                }else{
//                    functionType = DISPLAY_MODE;
//                }
//            }
            boolean canViewNarrative =((Boolean)vecData.elementAt(4)).booleanValue();
            boolean canModifyNarrative = ((Boolean)vecData.elementAt(5)).booleanValue();
            HashMap hmProposalData = (HashMap) vecData.elementAt(6);
            if(hmProposalData!= null){
                boolean inHierarchy = ((Boolean)hmProposalData.get("IN_HIERARCHY")).booleanValue();
                boolean isParent = false;
                if(inHierarchy) {
                    setHierarchy(inHierarchy);
                    isParent = ((Boolean)hmProposalData.get("IS_PARENT")).booleanValue();
                    if(isParent){
                        setParent(isParent);
                    }
                }
            }
            //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
            //When the proposal is in ApprovelInProgress status and is the child in the hierarchy then narrative can't be modified
            //Only in the parent proposal narrative can be modified
            if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS && isHierarchy() && !isParent()){
                functionType = DISPLAY_MODE;
            }
            //COEUSQA-1579 : End

            cvPropHierLink = ((CoeusVector)vecData.elementAt(7));
            hmPropHierLink = new HashMap();
            if(cvPropHierLink != null){
                Equals eqLinkType = new Equals("linkType", "N");
                cvPropHierLink = cvPropHierLink.filter(eqLinkType);
                if(cvPropHierLink != null && cvPropHierLink.size() >0){
                    for(int index = 0; index < cvPropHierLink.size(); index++){
                        ProposalHierarchyLinkBean linkBean = 
                            (ProposalHierarchyLinkBean) cvPropHierLink.get(index);
                        hmPropHierLink.put(""+linkBean.getParentModuleNumber(),
                            ""+linkBean.getChildProposalNumber());
                    }
                }
            }      
            //Commented for Proposal Narrative Locking Issue case id 3316 - start
//            if(canModifyNarrative){
//                functionType = MODIFY_MODE;
//            } else {
//                functionType = DISPLAY_MODE;
//            }
            //Commented for Proposal Narrative Locking Issue case id 3316 - end
            //Code commented and modified for case#3316 - Bug fix - ends
            narrativeData.addElement(vecData.get(1));
            
            
            /*added for the CoeusEnhancements
             *to add the narrative types to the narrtiveDataVector*/
            if(vecData.get(2) != null){
                narrativeData.addElement(vecData.get(2));
            }else{
                narrativeData.addElement(null);
            }
            
            /*Added for the Coeus Enhancement case:1776 start step:2*/
            if(vecData.get(3) != null){
                String code = (String)vecData.get(3);
                narrativeData.addElement(vecData.get(3));
            }
            /*End Coeus Enhancement case:1776 End step:2*/
        }
        setValues(narrativeData);
        initComponents();
        postInitComponents();
        setFormData();
        setTableEditors();
        formatFields();
        getNarrativeUsersForProposal( proposalNumber );
        tblModule.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        if( tblModule.getRowCount() > 0 ){
            tblModule.setRowSelectionInterval(0,0);
            btnSave.setEnabled( functionType != DISPLAY_MODE ? true : false );
            //Code commented for case#2420 - Upload of files on Edit Module Details Window
            //btnSave.setEnabled(isHierarchy() && isParent());
        }
         /* This will catch the window closing event and
          * checks any data is modified.If any changes are done by
          * the user the system will ask for confirmation of Saving the info.
          */
        this.addVetoableChangeListener(new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                if (pce.getPropertyName().equals(
                JInternalFrame.IS_CLOSED_PROPERTY) ) {
                    boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                    if( changed ) {
                        
                        try {
                            closeNarrativeDetails();
                        } catch ( Exception e) {
                            e.printStackTrace();
                            if(!( e.getMessage().equals(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130")) )){
                                CoeusOptionPane.showErrorDialog(e.getMessage());
                            }
                            throw new PropertyVetoException(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130"),null);
                        }
                    }
                }
            }
        });
        //Code added for case#3316 - Bug fix - starts
        //Disable Add button for status code 2 or 4
        if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS || proposalStatusCode == APPROVED_STATUS){
            btnAdd.setEnabled(false);
            addModule.setEnabled(false);
            btnDelete.setEnabled(false);
            deleteModule.setEnabled(false);
        }
        //Code added for case#3316 - Bug fix - ends
        setTitle( CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE +" "+ proposalNumber );
        
        mdiForm.putFrame( CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE, proposalNumber,
        this.functionType, this );
        mdiForm.getDeskTopPane().add(this);
        setVisible(true);
        setSelected(true);
        
    }
  
  //bug id 1860 step 3 : end

    //Added by shiji for Right Checking - step 4 : start
    public void setIsNarrativeOpenedFromPropList(boolean isFromPropList) {
        this.isOpenedFromPropListWindow = isFromPropList;
    }
    
    public void setUnitNumber(String leadUnit) {
        this.unitNumber = leadUnit;
    }
    //Right Checking - step 4 : end
    //Commented by shiji for Right Checking - step 5 : start
    /*private Vector getNarrativeDetails( String proposalNumber ) throws Exception{
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
                           CoeusGuiConstants.PROPOSAL_SERVLET ;
     
        RequesterBean request = new RequesterBean();
     
        request.setFunctionType( GET_NARRATIVE_DETAILS );
        request.setId( proposalNumber );
        request.setDataObject(new Character(functionType));
        AppletServletCommunicator comm
                    = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        } else if( response.isLocked() ) {
            String msg = coeusMessageResources.parseMessageKey(
                "narrative_row_locked_exceptionCode.123456");
            int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            if (resultConfirm == 0) {
                dataObjects = response.getDataObjects();
                functionType = DISPLAY_MODE;
                return dataObjects;
            }else{
                // throwing empty exception to stop further proceeding of opening
                //narrative
                throw new Exception("");
            }
     
        }else{
            throw new Exception(response.getMessage());
        }
        return dataObjects;
    }*/
    //Right Checking - step 5 : end
    //Modified by shiji for Right Checking - step 6 : start
    private Vector getNarrativeDetails( String proposalNumber ) throws Exception{
        Vector dataObjects = null;
        Vector inDataObjects = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType( GET_NARRATIVE );
        request.setId( proposalNumber );
        request.setDataObject(new Character(functionType));
        inDataObjects.add(0,new Boolean(isOpenedFromPropListWindow));
        inDataObjects.add(1,unitNumber);
        request.setDataObjects(inDataObjects);
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
            //Added for Proposal Narrative Locking Issue case id 3316 - start
            // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
            canModifyNarrative = ((Boolean)dataObjects.elementAt(5)).booleanValue();
            // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
            // COEUSDEV-308: Application not checking module level rights for a user in Narrative - Start
            hasOSPDeptRightToModify = ((Boolean)dataObjects.elementAt(8)).booleanValue();
            hasPropRightToModify = ((Boolean)dataObjects.elementAt(9)).booleanValue();
            // COEUSDEV-308: Application not checking module level rights for a user in Narrative - End
            if(canModifyNarrative){
                functionType = MODIFY_MODE;
            } else {
                functionType = DISPLAY_MODE;
            }
            //Added for Proposal Narrative Locking Issue case id 3316 - end
        } else if( response.isLocked() ) {
            //Modified for case# 3439 to include the locking message - start
			//if proposal is locked by another user and the 
            //status is 'Approval in Progress'(2) or 'Approved'(4)
            //open in display mode without prompting the user 
            if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS|| proposalStatusCode == APPROVED_STATUS){
                dataObjects = response.getDataObjects();
                functionType = DISPLAY_MODE;
                return dataObjects;
            }

			//For all other statuses prompt the user before opening the narrative
            String msg = "";
            if(response.getMessage()!=null){
                msg = msg + response.getMessage().trim() + ". ";
            }
            msg = msg + coeusMessageResources.parseMessageKey(
					"narrative_row_locked_exceptionCode.123456");
            //Modified for case# 3439 to include the locking message - end
            int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            if (resultConfirm == 0) {
                dataObjects = response.getDataObjects();
                functionType = DISPLAY_MODE;
                return dataObjects;
            }else{
                // throwing empty exception to stop further proceeding of opening
                //narrative
                throw new Exception("");
            }
            
        }else{
            throw new Exception(response.getMessage());
        }
        return dataObjects;
    }
    //Right Checking - step 6 : end
    
    private void getNarrativeUsersForProposal( String proposalNumber ) throws Exception{
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType( GET_NARRATIVE_USERS_FOR_PROPOSAL );
        request.setId( proposalNumber );
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        } else {
            throw new Exception(response.getMessage());
        }
        if( dataObjects != null && dataObjects.size()> 0 ){
            usersForProposal = (Vector) dataObjects.elementAt(0);
        }
        if( usersForProposal != null && usersForProposal.size() > 0 ) {
            ProposalNarrativeModuleUsersFormBean userFormBean;
            int usersSize = usersForProposal.size();
            for( int indx = 0; indx < usersSize; indx++) {
                userFormBean = (ProposalNarrativeModuleUsersFormBean)usersForProposal.elementAt( indx );
                hmProposalUserRights.put(userFormBean.getUserId().toUpperCase(),""+userFormBean.getAccessType() );
            }
        }
    }
    
    //Added by shiji for Case id : 2016  - step2 :start 
    /*To send Notifications to all users who have already approved the proposal when a change in made to a Narrative module.*/
    public void updateInboxDetails(String type) throws CoeusClientException {
        int selectedRow=tblModule.getSelectedRow();
        //Modified for COEUSDEV-340 : Email that is generated when a narrative is changes does not have proposal and narrative details - Start
        //When document is uploaded message text is changed
//        String message = "A new "+type+" file is uploaded for proposal \'"+proposalNumber+ "\' for Module No." + tblModule.getModel().getValueAt(selectedRow,2);
        MessageFormat formatter = new MessageFormat("");
        String message = formatter.format(coeusMessageResources.parseMessageKey("proposal_narr_document_exceptionCode.6621"), proposalNumber,tblModule.getModel().getValueAt(selectedRow,2));
        message = message+"\r\n\r\n\r\n\r\n\r\nNarrative Type:          "+tblModule.getModel().getValueAt(selectedRow,4);
        message = message+"\r\nModule Description:      "+tblModule.getModel().getValueAt(selectedRow,5);
        //COEUSDEV-340 : End
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        String fromUser= CoeusGuiConstants.getMDIForm().getUserName();
        
        request.setFunctionType( SEND_NOTIFICATION );
        request.setId(proposalNumber);
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
    //Case id : 2016  - end 
    
    
    private void setValues( Vector dataObjects ) {
        if( dataObjects != null && dataObjects.size() > 0 ) {
            Vector values = (Vector) dataObjects.elementAt(0);
            narrativeBean = new Vector();
            narrativeBean = (Vector) dataObjects.elementAt(0);
            /*added for the CoeusEnhancements case:1624 , get the narrative types from the database and
             *filter for the types where SYSTEM_GENERATED="N"*/
            if(dataObjects.elementAt(1)!=null){
                narrativeTypes = (Vector)dataObjects.elementAt(1);
            }
            
            /*Added for the Coeus Enhancement case:1776 start step:3*/
            if(dataObjects.elementAt(2)!=null){
                paramNarrativeTypeCode = (String)dataObjects.elementAt(2);
            }
            /*End Coeus Enhancement case:1776 step:3*/
            
            moduleNarrativeTypes = new Vector();
            if(narrativeTypes != null && narrativeTypes.size()>0){
                for(int i=0;i<narrativeTypes.size();i++){
                    ProposalNarrativeTypeBean bean = (ProposalNarrativeTypeBean)narrativeTypes.get(i);
                    if(bean.getSystemGenerated().equals("N")){
                        //add the beans to the moduleNarrativeTypes vector
                        moduleNarrativeTypes.add(bean);
                    }
                }
            }
            
            if( values != null ) {
                int modSize = values.size();
                ProposalNarrativeFormBean narrativeBean;
                moduleTableData = new Vector();
                for( int indx = 0; indx < modSize; indx++ ) {
                    narrativeBean = new ProposalNarrativeFormBean();
                    narrativeBean = ( ProposalNarrativeFormBean ) values.elementAt( indx );
                    int modNum = narrativeBean.getModuleNumber();
                    int seqNo = narrativeBean.getModuleSequenceNumber();
                    maxModuleNo = Math.max(maxModuleNo, modNum);
                    maxSeqNo = Math.max(maxSeqNo,seqNo );
                    hmModules.put( ""+modNum, narrativeBean );
                    ////System.out.println("modNumber:"+narrativeBean.getModuleNumber());
                    Vector modUsers = new Vector();
                    modUsers = narrativeBean.getPropModuleUsers();
                    if( modUsers != null ) {
                        hmUsers.put( ""+narrativeBean.getModuleNumber(), modUsers );
                        ////System.out.println("users size:"+ modUsers.size());
                    }
                    moduleTableData.addElement( getModuleTableRow( narrativeBean ) );
                }
            }
        }
    }
    
    private Vector getModuleTableRow( ProposalNarrativeFormBean bean ) {
        Vector rowData = new Vector();
        if( bean != null ) {
            rowData.addElement( new Boolean( bean.getHasSourceModuleNumber() ) );
            rowData.addElement( new Boolean( bean.getHasPDFModuleNumber() ) );
            rowData.addElement( ""+bean.getModuleNumber() );
            rowData.addElement( hmStatusCodes.get(""+bean.getModuleStatusCode() ) );
            /* added for the Coeus Enhancement 1624 start
             * to add the description value to the column narrative type in the narrative module table*/
            String description = "";
            int code = bean.getNarrativeTypeCode();
            for(int i=0;i<narrativeTypes.size();i++){
                ProposalNarrativeTypeBean narrBean = (ProposalNarrativeTypeBean)narrativeTypes.get(i);
                if(code == narrBean.getNarrativeTypeCode()){
                    description = narrBean.getDescription();
                }
            }
            rowData.addElement(description);
            rowData.addElement( bean.getModuleTitle() == null ? "" : bean.getModuleTitle() );
            //Case :4007: Icon based on Mime type
         //#case 3855 - start Add file extension to the column to display attachment specific icon to the user.
//           rowData.addElement(UserUtils.getFileExtension(bean.getFileName()));    
            rowData.addElement(bean);
         //#Case 3855 - end
            //4007 :end
        }
        return rowData;
    }
    
    private Vector getModuleUsersTableData( String moduleNumber ) {
        Vector userTableData = new Vector();
        Vector userTableRow;
        Vector moduleUsers = ( Vector )hmUsers.get( moduleNumber );
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
    
    private void setFormData() {
        String moduleNumber = null;
        if( moduleTableData != null && moduleTableData.size() > 0 ) {
            ( ( DefaultTableModel )tblModule.getModel() ).setDataVector(
            moduleTableData, getModuleColumnNames() );
            if( tblModule.getRowCount() > 0 ){
                moduleNumber = ( String )tblModule.getValueAt( 0, 2 );
                tblModule.setRowSelectionInterval(0,0);
                showModuleDetails( moduleNumber );
            }
            formatFields();
        }
        tblUsers.setEnabled(false);
    }
    
    private void showModuleDetails( String moduleNumber ) {
        ( ( DefaultTableModel )tblUsers.getModel() ).setDataVector(
        getModuleUsersTableData( moduleNumber ), getUserColumnNames() );
        
        
        if( tblUsers.getRowCount() > 0 ) {
            tblUsers.setRowSelectionInterval(0,0);
        }
        
        ProposalNarrativeFormBean narrBean = ( ProposalNarrativeFormBean )hmModules.get( moduleNumber );
        if( narrBean != null ) {
            txtFldContactName.setText( narrBean.getContactName() );
            txtFldEmail.setText( narrBean.getEmailAddress() );
            txtFldPhoneNumber.setText( narrBean.getPhoneNumber() );
            txtArComments.setText( narrBean.getComments() );
            //Added for the Coeus Enhancemnt case:#1766
            //Commented for Proposal Hierarchy Enhancement Case# 3183 - Start
            //txtUpdateTimestamp.setText(narrBean.getUpdateTimestamp().toString());
            //Commented for Proposal Hierarchy Enhancement Case# 3183 - end
//            txtUpdateUser.setText(narrBean.getUpdateUser());
            /*
             * UserID to UserName Enhancement - Start
             * Added UserUtils class to change userid to username
             */
            //Modifed for Proposal Hierarchy Enhancement Case# 3183 - Start
            //txtUpdateUser.setText(UserUtils.getDisplayName(narrBean.getUpdateUser()));
            if(narrBean.getUpdateTimestamp()!=null){
                txtUpdateUser.setText(UserUtils.getDisplayName(narrBean.getUpdateUser())+ " at "+
                    CoeusDateFormat.format(narrBean.getUpdateTimestamp().toString()));
            }
            //Modifed for Proposal Hierarchy Enhancement Case# 3183 - End
            // UserId to UserName Enhancement - End
            // Added for Proposal Hierarchy Enhancement Case# 3183 - Start
            if(narrBean.getPropNarrativePDFSourceBean()!=null && 
            narrBean.getPropNarrativePDFSourceBean().getUpdateTimestamp()!=null){
                txtLastDocUpdateUser.setText(narrBean.getPropNarrativePDFSourceBean().getUpdateUserName() + " at "+
                    CoeusDateFormat.format(narrBean.getPropNarrativePDFSourceBean().getUpdateTimestamp().toString()));
            } else {
                txtLastDocUpdateUser.setText("");
            }
            // Added for Proposal Hierarchy Enhancement Case# 3183 - End
            // Case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads- Start
            if( narrBean.getFileName() != null){
                txtFileName.setText(narrBean.getFileName());
            } else{
                txtFileName.setText("");
            }
            // Case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads- End
        }
        
    }
    
    private void postInitComponents() {
        tblUsers.setModel( new DefaultTableModel(
        new Object[][]{}, getUserColumnNames().toArray()){
            public boolean isCellEditable( int row, int col ) {
                return false;
            }
        });
        
        tblModule.setModel( new DefaultTableModel(
        new Object[][]{}, getModuleColumnNames().toArray()){
            public boolean isCellEditable( int row, int col ) {
                if( col == 0 || col == 1 ) return true;
                return false;
            }
        });
        tblModule.getSelectionModel().addListSelectionListener( this );
    }
    
    private Vector getUserColumnNames(){
        Vector colNames = new Vector();
        colNames.addElement("Icon");
        colNames.addElement("User");
        colNames.addElement("Rights") ;
        
        return colNames;
    }
    
    private Vector getModuleColumnNames(){
        Vector colNames = new Vector();
        colNames.addElement("Word");
        colNames.addElement("PDF");
        colNames.addElement("No.");
        colNames.addElement("Status");
        /*added for the Coeus Enhancement:1624*/
        colNames.addElement("Narrative Type");
        colNames.addElement("Title");
 // #case 3855 - start  Add a file extension column which is not visible to user
        colNames.addElement("File Extension");
 //#case 3855 - end
        return colNames;
    }
    
    private void setTableEditors(){
        TableColumn column = tblModule.getColumnModel().getColumn(0);
        column = tblModule.getColumnModel().getColumn(0);
        
        //Case 2278 Start 1
        //column.setMaxWidth(20);
        //Case 2278 End 1
        //Code modified for case#2420 - Upload of files on Edit Module Details Window
        //First column width set to zero, and last column with incresed by 20
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setCellRenderer( new ModuleTableRenderer() );
        column.setCellEditor( new ModuleTableEditor() );
        column.setHeaderValue(" ");
        //column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = tblModule.getColumnModel().getColumn(1);
        
        //Case 2278 Start 2
        //column.setMaxWidth(20);
        //Case 2278 End 2
        
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        column.setCellRenderer( new ModuleTableRenderer() );
        column.setCellEditor( new ModuleTableEditor() );
        //column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setHeaderValue(" ");
        
        column = tblModule.getColumnModel().getColumn(2);
        
        //Case 2278 Start 3
        //column.setMaxWidth(20);
        //Case 2278 End 3
        
        column.setMinWidth(40);
        column.setPreferredWidth(40);
        
        column = tblModule.getColumnModel().getColumn(3);
        
        //Case 2278 Start 4
        //column.setMaxWidth(20);
        //Case 2278 End 4
        
        column.setMinWidth(80);
        column.setPreferredWidth(80);
        
        /*added the narrative type column to the narrative table
         *Coeus Enhancement:1624*/
        column = tblModule.getColumnModel().getColumn(4);
        
        //Case 2278 Start 5
        //column.setMaxWidth(20);
        column.setMinWidth(221);
        column.setPreferredWidth(221);
        //Case 2278 End 5
        
        column = tblModule.getColumnModel().getColumn(5);
        
        //Case 2278 Start 6
        //column.setMaxWidth(20);
        //Case 2278 End 6
        
        //Modified for case#2420 - Upload of files on Edit Module Details Window - start
        column.setMinWidth(205);
        column.setPreferredWidth(205);
        
        tblModule.setRowHeight(22);
        ((DefaultTableCellRenderer)tblModule.getTableHeader().getDefaultRenderer()
        ).setHorizontalAlignment(JLabel.LEFT);
        tblModule.getTableHeader().setReorderingAllowed( false );
        
        //Case 2278 Start 7
        //tblModule.getTableHeader().setResizingAllowed( false );
        tblModule.getTableHeader().setResizingAllowed(true);
        //Case 2278 End 7
        
        tblModule.getTableHeader().setFont( CoeusFontFactory.getLabelFont() );
        tblModule.setFont( CoeusFontFactory.getNormalFont() );
        
        // #case 3855 - start File extension column is not visible to the user
        column = tblModule.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        column.setHeaderValue(" ");
        // #case 3855 -end
        tblUsers.setRowHeight(22);
        
        column = tblUsers.getColumnModel().getColumn(0);
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
        //Code modified for Case# 3183 - Proposal Hierarchy - starts
        column = tblUsers.getColumnModel().getColumn(1);
        column.setMaxWidth(220);
        column.setMinWidth(220);
        //column.setPreferredWidth(150);
        column.setPreferredWidth(220);
        column.setCellRenderer( new NarrativeUserTableRenderer() );
        //Code modified for Case# 3183 - Proposal Hierarchy - ends
        
        column = tblUsers.getColumnModel().getColumn(2);
        column.setMinWidth(230);
        column.setCellRenderer( new NarrativeUserTableRenderer() );
        
        tblUsers.getTableHeader().setReorderingAllowed( false );
        tblUsers.getTableHeader().setResizingAllowed( false );
        tblUsers.setRowSelectionAllowed( false );
        tblUsers.getTableHeader().setFont( CoeusFontFactory.getLabelFont() );
        tblUsers.setFont( CoeusFontFactory.getNormalFont() );
    }
    
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        try{
            
            if( source.equals( btnClose ) ){
                this.doDefaultCloseAction();
                //               closeNarrativeDetails(false);
            }else if ( source.equals( btnSave ) ){
                saveNarrativeDetails();
            }else if( source.equals( btnAdd ) || (source.equals( addModule )) ){
                if( !saveRequired ){
                    addNarrative();
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_narr_exceptionCode.6613"));
                }
            }else if( source.equals( btnModify ) || (source.equals( modifyModule )) ){
                if( !saveRequired ){
                    modifyModule();
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_narr_exceptionCode.6613"));
                }
            }else if( source.equals( btnDelete ) || source.equals( deleteModule ) ){
                if( !saveRequired ){
                    deleteModule();
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_narr_exceptionCode.6613"));
                }
            }else if( (source.equals( btnUp )) || (source.equals( moveUp ))){
                moveRowUp();
            }else if( (source.equals( btnDown )) || (source.equals( moveDown ))){
                moveRowDown();
            }else if( (source.equals(btnUploadWord)) || (source.equals(uploadWordFiles))){
                if( !saveRequired ){
                    uploadWordDocument();
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_narr_exceptionCode.6613"));
                }
            }else if( (source.equals(btnUploadPdf)) || (source.equals(uploadPdfFiles))){
                if( !saveRequired ){
                    uploadPdfDocument();
                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    "proposal_narr_exceptionCode.6613"));
                }
            }else if( source.equals(viewWordFiles) ){
                viewWordDocument();
            }else if( source.equals(viewPdfFiles) ){
                viewPdfDocument();
            }else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "funcNotImpl_exceptionCode.1100"));
            }
        } catch ( Exception e) {
            e.printStackTrace();
            if(!( e.getMessage().equals(
            coeusMessageResources.parseMessageKey(
            "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
        }
    }
    
    //Added by Vyjayanthi
    /** Allows to view the word document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewWordDocument() throws Exception{
        int selectedRow = tblModule.getSelectedRow();
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        if( selectedRow != -1 ){
            String module = (String) tblModule.getValueAt(selectedRow,2);
            int moduleNumber = Integer.parseInt(module);
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean =
            new ProposalNarrativePDFSourceBean();
            ProposalNarrativeFormBean proposalNarrativeFormBean =
            (ProposalNarrativeFormBean)hmModules.get(module);
            if( !proposalNarrativeFormBean.getHasSourceModuleNumber() ){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6611"));
                return ;
            }
            proposalNarrativePDFSourceBean.setProposalNumber(
            proposalNarrativeFormBean.getProposalNumber());
            proposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
            
            RequesterBean request = new RequesterBean();
            //modified for case #1860 start 1
            Vector dataObjects = new Vector();
            dataObjects.add(0, proposalNarrativePDFSourceBean);
            dataObjects.add(1,unitNumber);
//            request.setId(proposalNumber);
//            request.setDataObjects(dataObjects);
            //modified for case #1860 end 1
//            request.setFunctionType(GET_NARRATIVE_SOURCE);
            //Commented for case #1860 start 2
           // request.setDataObject(proposalNarrativePDFSourceBean);
            //commented for case #1860 end 2
            
            //For Streaming
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", dataObjects);
            map.put("DOC_TYPE", "NARRATIVE_DOC");
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.NarrativeDocumentReader");
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            //For Streaming
            
            AppletServletCommunicator comm =
            new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
//                Vector vecData = response.getDataObjects();
//                boolean hasRight = ((Boolean)vecData.get(0)).booleanValue();
//                
//                if( hasRight ){
//                    // Bug fix #952
//                    if(vecData!= null && vecData.size() > 1){
//                        String url = (String)vecData.get(1);
//                        url = url.replace('\\', '/');
//                        URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                        if(coeusContext != null){
//                            coeusContext.showDocument( templateURL, "_blank" );
//                        }else{
//                            javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                            bs.showDocument(templateURL);
//                        }
//                    }
//                }else{
//                    CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(INSUFFICIENT_RIGHTS));
//                }
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/') ; // this is fix for Mac
                URL urlObj = new URL(reportUrl);
                URLOpener.openUrl(urlObj);
            }else {
                // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
            }
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    
    //Added by Vyjayanthi
    /** Allows to view the PDF document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
        int selectedRow = tblModule.getSelectedRow();
        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        if( selectedRow != -1 ){
            String module = (String) tblModule.getValueAt(selectedRow,2);
            int moduleNumber = Integer.parseInt(module);
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean =
            new ProposalNarrativePDFSourceBean();
            ProposalNarrativeFormBean proposalNarrativeFormBean =
            (ProposalNarrativeFormBean)hmModules.get(module);
            if( !proposalNarrativeFormBean.getHasPDFModuleNumber() ){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6606"));
                return ;
            }
            proposalNarrativePDFSourceBean.setProposalNumber(
            proposalNarrativeFormBean.getProposalNumber());
            proposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
            RequesterBean request = new RequesterBean();
            //modified for case #1860 start 3
            Vector dataObjects = new Vector();
            dataObjects.add(0, proposalNarrativePDFSourceBean);
            dataObjects.add(1,unitNumber);
//            request.setId(proposalNumber);
//            request.setDataObjects(dataObjects);
            //modified for case #1860 end 3
//            request.setFunctionType(GET_NARRATIVE_PDF);
            //Commented for case #1860 start 4
           //request.setDataObject(proposalNarrativePDFSourceBean);
            //commented for case #1860 end 4
            
            //For Streaming
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", dataObjects);
            map.put("DOC_TYPE", "NARRATIVE_PDF");
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.NarrativeDocumentReader");
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            //For Streaming
            
            AppletServletCommunicator comm
            = new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
//                Vector vecData = response.getDataObjects();
//                boolean hasRight = ((Boolean)vecData.get(0)).booleanValue();
//                if( hasRight ){
//                    // Bug fix #952
//                    if(vecData!= null && vecData.size()>1){
//                        String url = (String)vecData.get(1);
//                        url = url.replace('\\', '/');
//                        URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//                        if(coeusContext != null){
//                            coeusContext.showDocument( templateURL, "_blank" );
//                        }else{
//                            javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                            bs.showDocument(templateURL);
//                        }
//                        URLOpener.openUrl(url);
//                    }
//                    
//                }else{
//                    CoeusOptionPane.showInfoDialog(
//                    coeusMessageResources.parseMessageKey(INSUFFICIENT_RIGHTS));
//                }
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/') ; // this is fix for Mac
                URL urlObj = new URL(reportUrl);
                URLOpener.openUrl(urlObj);
            }else {
                //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - start
                // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//                if(proposalNarrativeFormBean.getHasPDFModuleNumber()){                    
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6616"));
//                }else{
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
//                }
                 // COEUSDEV-308: Application not checking module level rights for a user in Narrative - End
                //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - start
            }
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    
    //Added by Vyjayanthi
    /** Allows to upload the word document for the selected row
     * @throws Exception in case of error occured
     */
    private void uploadWordDocument() throws Exception{
        int selectedRow = tblModule.getSelectedRow();
        boolean hasAccessRight = false;
        if( selectedRow != -1 ){
            String module = (String) tblModule.getValueAt(selectedRow,2);
            int moduleNumber = Integer.parseInt(module);
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean =
            new ProposalNarrativePDFSourceBean();
            ProposalNarrativeFormBean proposalNarrativeFormBean =
            (ProposalNarrativeFormBean)hmModules.get(module);
            
            // Modified for bug id 1860 step 4 : start
            //Check rights
            //modified for case #1856 start 2
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
//             if( !hasModifyNarrativeRights() && !hasAlterRight && !isUserHasModifyRight(module) ){
//                 //Modified for case #1856 end 2
//                 //bug id 1860 step 4 : end
//                CoeusOptionPane.showInfoDialog(
//                coeusMessageResources.parseMessageKey(INSUFFICIENT_UPLOAD_RIGHTS));
//                return ;
//            }
//            //Added for Case #2368 start 1
//            if(hasModifyNarrativeRights() || hasAlterRight   ){
//                if(!isUserHasModifyRight( module )){
//                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
//                        INSUFFICIENT_UPLOAD_RIGHTS));
//                    return;
//                }
//            }
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
            // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
            //Check the User has OSP Level or Departmental Level rights.or Prop and Module level. If user has any one of the right allow to Upload.
            // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//            if( canModifyNarrative ||  isUserHasModifyRight(module)){
            if(canUserEditModule(module)){
                //Added for Case #2368 end 1
                proposalNarrativePDFSourceBean.setProposalNumber(
                proposalNarrativeFormBean.getProposalNumber());
                proposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
                if( proposalNarrativeFormBean.getHasSourceModuleNumber() ){
                    proposalNarrativePDFSourceBean.setAcType(TypeConstants.UPDATE_RECORD);
                }else{
                    proposalNarrativePDFSourceBean.setAcType(TypeConstants.INSERT_RECORD);
                }
                
                fileChooser = new CoeusFileChooser(mdiForm);
                fileChooser.setSelectedFileExtension(WORD_FORMAT);
                fileChooser.showFileChooser();
                if(fileChooser.isFileSelected()){
                    String fileName = fileChooser.getSelectedFile();
                    if(fileName != null && !fileName.trim().equals("")){
                        int index = fileName.lastIndexOf('.');
                        if(index != -1 && index != fileName.length()){
                            String extension = fileName.substring(index+1,fileName.length());
                            if(extension != null && (extension.equalsIgnoreCase(WORD_FORMAT) )){
                                proposalNarrativePDFSourceBean.setFileBytes(fileChooser.getFile());
                                //Case 4007:Icon based on mime type
                                proposalNarrativePDFSourceBean.setFileName(fileChooser.getFileName().getName());
                                CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                                proposalNarrativePDFSourceBean.setMimeType(docTypeUtils.getDocumentMimeType(proposalNarrativePDFSourceBean));
                                //4007 end
                                if( !saveToDataBase(proposalNarrativePDFSourceBean, WORD_FORMAT) ){
                                    CoeusOptionPane.showErrorDialog(
                                    coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6607"));
                                }else{
                                    proposalNarrativeFormBean.setHasSourceModuleNumber(true);
                                    tblModule.getModel().setValueAt(new Boolean(true), selectedRow, 0);
                                    tblModule.setRowSelectionInterval(selectedRow, selectedRow);
                                }
                                //Added by shiji for Case id : 2016  - step3 :start
                                if((functionType == MODIFY_MODE) && hasAlterRight) {
                                    try {
                                        updateInboxDetails(extension);
                                    }catch(CoeusClientException coeusClientException) {
                                        CoeusOptionPane.showDialog(coeusClientException);
                                    }
                                }
                                //Case id: 2016 - step3 :end
                                return;
                            }else{
                                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                "proposal_narr_exceptionCode.6608"));
                                return;
                            }
                        }else{
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "correspType_exceptionCode.1012"));
                        }
                    }
                }
                
            }else {   // end if -right checking //If User does not have any rights shows message
                  CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(INSUFFICIENT_UPLOAD_RIGHTS));
                return ;
            }
            // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    
    //Added by Vyjayanthi
    /** Supporting method to upload the word or PDF document for the selected row
     * @throws Exception in case of error occured
     */
    private boolean saveToDataBase(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean,
    String format) throws Exception {
        RequesterBean request = new RequesterBean();
        //Added for case#2420 - Upload of files on Edit Module Details Window - start
        String serverMsg;
        boolean successFlag;
        //Added for case#2420 - Upload of files on Edit Module Details Window - end
        if( format.equals(WORD_FORMAT) ){
            request.setFunctionType(UPDATE_NARRATIVE_SOURCE);
            //To upload any format Case #1779
        }else if( format.equals(ANY_FORMAT) ){
            //To upload any format Case #1779-end
            request.setFunctionType(UPDATE_NARRATIVE_PDF);
        }
        request.setDataObject(proposalNarrativePDFSourceBean);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(CONNECTION_STRING, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        //Modified for case#2420 - Upload of files on Edit Module Details Window - start
        if(response.getId().equals("1")){
            //Added for case 3183 - Proposal Hierarchy enhancements - start
            Vector serverDataObjects = response.getDataObjects();
            if(serverDataObjects!=null && serverDataObjects.size()>0){
            	ProposalNarrativePDFSourceBean propNarrPDFSourceBean = 
                (ProposalNarrativePDFSourceBean)serverDataObjects.get(0);
                if(propNarrPDFSourceBean!=null){
                	ProposalNarrativeFormBean propNarrFormBean = 
                        (ProposalNarrativeFormBean)hmModules.get(""+propNarrPDFSourceBean.getModuleNumber());
                        propNarrFormBean.setPropNarrativePDFSourceBean(propNarrPDFSourceBean);
                 }
            }
            //Added for case 3183 - Proposal Hierarchy enhancements - end
            successFlag = true;
        }else if(response.getId().equals("-1")){
            serverMsg = coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6617") +" " 
                        +proposalNarrativePDFSourceBean.getProposalNumber() +" "
                        +coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6618");            
            CoeusOptionPane.showErrorDialog(serverMsg);
            successFlag = false;
        }else{            
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6619"));
            successFlag = false;
        }       
        return successFlag;
        //Modified for case#2420 - Upload of files on Edit Module Details Window - end
    }
    
    //Added by Vyjayanthi
    /** Allows to upload the PDF document for the selected row
     * @throws Exception in case of error occured
     */
    //Modified to upload any file Case #1779 -start
private void uploadPdfDocument() throws Exception{
        int selectedRow = tblModule.getSelectedRow();
        boolean hasAccessRight = false;
        if( selectedRow != -1 ){
            String module = (String) tblModule.getValueAt(selectedRow,2);
            int moduleNumber = Integer.parseInt(module);
            ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean =
            new ProposalNarrativePDFSourceBean();
            ProposalNarrativeFormBean proposalNarrativeFormBean =
            (ProposalNarrativeFormBean)hmModules.get(module);
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
            //Modified for bug id 1860 step 5 start
            //Check rights
            //Modified for case #1856 start 3
//            if( !hasModifyNarrativeRights()&&!hasAlterRight && !isUserHasModifyRight(module) ){
//                //Modified for case #1856 end 3
//                //bug id step 5 : end
//                CoeusOptionPane.showInfoDialog(
//                coeusMessageResources.parseMessageKey(INSUFFICIENT_UPLOAD_RIGHTS));
//                return ;
//            }
//            //Added for Case #2368 start 2
//            if(hasModifyNarrativeRights() || hasAlterRight   ){
//                if(!isUserHasModifyRight( module )){
//                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
//                    INSUFFICIENT_UPLOAD_RIGHTS));
//                    return;
//                }
//            }
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
            // Modified for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
            //Check the User has OSP Level or Departmental Level rights.or Prop and Module level. If user has any one of the right allow to Upload.
            // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//            if( canModifyNarrative || isUserHasModifyRight(module)){
            if(canUserEditModule(module)){
            //Added for Case #2368 end 2
            proposalNarrativePDFSourceBean.setProposalNumber(
            proposalNarrativeFormBean.getProposalNumber());
            proposalNarrativePDFSourceBean.setModuleNumber(moduleNumber);
            if( proposalNarrativeFormBean.getHasPDFModuleNumber() ){
                proposalNarrativePDFSourceBean.setAcType(TypeConstants.UPDATE_RECORD);
            }else{
                proposalNarrativePDFSourceBean.setAcType(TypeConstants.INSERT_RECORD);
            }
            
            fileChooser = new CoeusFileChooser(mdiForm);
            //Added for case#2420 - Upload of files on Edit Module Details Window            
            //Code commented for Case#3648 - Uploading non-pdf files
            //fileChooser.setSelectedFileExtension(PDF_FORMAT);
            fileChooser.setAcceptAllFileFilterUsed(true);            
            fileChooser.showFileChooser();
            if(fileChooser.isFileSelected()){
                String fileName = fileChooser.getSelectedFile();
                //To set only the file name to the database file name field
                File file = fileChooser.getFileName();
                String path = file.getName();
                proposalNarrativePDFSourceBean.setFileName(path);
                //End setting only the file name to the database
                if(fileName != null && !fileName.trim().equals("")){
                    int index = fileName.lastIndexOf('.');
                    if(index != -1 && index != fileName.length()){
                        String extension = fileName.substring(index+1,fileName.length());
                        if(extension != null) { // && (extension.equalsIgnoreCase(PDF_FORMAT) )){
                            proposalNarrativePDFSourceBean.setFileBytes(fileChooser.getFile());
                            //Case 4007:Icon based on mime type
                            CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                            String mimeType = docTypeUtils.getDocumentMimeType(proposalNarrativePDFSourceBean);
                            proposalNarrativePDFSourceBean.setMimeType(mimeType);
                            proposalNarrativeFormBean.setMimeType(mimeType);
                            //4007 end
                            if( !saveToDataBase(proposalNarrativePDFSourceBean, ANY_FORMAT) ){
                                //CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6607"));
                            }else{
                                proposalNarrativeFormBean.setHasPDFModuleNumber(true);
                                //Added for case#2420 - Upload of files on Edit Module Details Window
                                proposalNarrativeFormBean.setFileName(path);
                                tblModule.getModel().setValueAt(new Boolean(true), selectedRow, 1);
                                //Case 4007:Icon based on mime type
                               // #Case 3855 -- start added attachment icon when the different icon is added
//                                tblModule.getModel().setValueAt(UserUtils.getFileExtension(path), selectedRow, 6);
                                tblModule.getModel().setValueAt(proposalNarrativePDFSourceBean, selectedRow, 6);
                               // #Case 3855 -- end
                                //4007:End
                                tblModule.setRowSelectionInterval(selectedRow, selectedRow);
                                //Code added for Case# 3183 - Proposal Hierarchy - starts
                                ProposalNarrativeFormBean narrBean = ( ProposalNarrativeFormBean )hmModules.get(""+moduleNumber);
                                //Code modified for case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads - start
                                ProposalNarrativePDFSourceBean propNarrativePDFSourceBean = narrBean.getPropNarrativePDFSourceBean();
                                if(propNarrativePDFSourceBean!=null){
                                    if(propNarrativePDFSourceBean.getUpdateTimestamp()!=null){
                                        txtLastDocUpdateUser.setText(propNarrativePDFSourceBean.getUpdateUserName() + " at "+
                                            CoeusDateFormat.format(propNarrativePDFSourceBean.getUpdateTimestamp().toString()));
                                    }else{
                                        txtLastDocUpdateUser.setText("");
                                    }
                                    txtFileName.setText(propNarrativePDFSourceBean.getFileName());
                                }else{
                                    txtFileName.setText("");
                                    txtLastDocUpdateUser.setText("");
                                }
                                //Code modified for case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads - end
                                //Code added for Case# 3183 - Proposal Hierarchy - ends
             
                            }
                            //Added by shiji for Case id : 2016  - step4 :start
                            //Modified for case#2999 - Multiple emails generated when a document is uploaded
//                            if((functionType == MODIFY_MODE) && hasAlterRight) {
                            if((functionType == MODIFY_MODE) && (proposalStatusCode != 1 || canModifyNarrative)) {
                                try {
                                    updateInboxDetails(extension);
                                }catch(CoeusClientException coeusClientException) {
                                    CoeusOptionPane.showDialog(coeusClientException);
                                }
                            }
                            //Case id : 2016  - step4 :
                            return;
                        }else{
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                            "proposal_narr_exceptionCode.6609"));
                            return;
                        }
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                        "correspType_exceptionCode.1012"));
                    }
                }
            }
             }else {   // end if -right checking //If User does not have any rights shows message
                  CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(INSUFFICIENT_UPLOAD_RIGHTS));
                return ;
            } // Modified for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
        }else{
            //If no row is selected
            // Case# 3294:Erroneous message when adding contact to Report Type - Start
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1120"));
            // Case# 3294:Erroneous message when adding contact to Report Type - End
        }
    }
    //Modified to upload any file Case #1779-end
    
    private void moveRowUp(){
        int selRow = tblModule.getSelectedRow();
        if( selRow > 0 ) {
            swapSequenceNumbers( selRow, selRow-1);
            ((DefaultTableModel)tblModule.getModel()).moveRow(selRow,selRow,selRow - 1 );
            tblModule.setRowSelectionInterval(selRow-1,selRow-1);
        }
    }
    
    private void moveRowDown(){
        int selRow = tblModule.getSelectedRow();
        if( selRow < tblModule.getRowCount()-1 ) {
            swapSequenceNumbers( selRow, selRow+1);
            ((DefaultTableModel)tblModule.getModel()).moveRow(selRow,selRow,selRow + 1 );
            tblModule.setRowSelectionInterval(selRow+1,selRow+1);
        }
    }
    
    private void swapSequenceNumbers( int row1, int row2 ) {
        ProposalNarrativeFormBean firstBean= null, secondBean = null;
        int firstModNum=0, secondModNum = 0;
        firstModNum = Integer.parseInt((String)tblModule.getValueAt(row1,2));
        secondModNum = Integer.parseInt((String)tblModule.getValueAt(row2,2));
        firstBean = (ProposalNarrativeFormBean)hmModules.get(""+firstModNum);
        secondBean = (ProposalNarrativeFormBean)hmModules.get(""+secondModNum);
        int tempSeqNo = firstBean.getModuleSequenceNumber();
        firstBean.setModuleSequenceNumber(secondBean.getModuleSequenceNumber());
        secondBean.setModuleSequenceNumber( tempSeqNo );
        if( firstBean.getAcType() == null) {
            firstBean.setAcType( UPDATE_RECORD );
        }
        if( secondBean.getAcType() == null) {
            secondBean.setAcType( UPDATE_RECORD );
        }
        hmModules.put(""+firstModNum,firstBean);
        hmModules.put(""+secondModNum,secondBean);
        hmModulesToDB.put(""+firstModNum,firstBean);
        hmModulesToDB.put(""+secondModNum,secondBean);
        saveRequired = true;
        //System.out.println("setting save req in swap");
    }
    
    /*Added for the Coeus Enhancement case:1776 start step:4 to set the narrative types and the narr type code to the narrative form
     when modifying and also adding a new narrative*/
    private void setNarrativeTypeAndCode() throws Exception{
        Vector cvData = new Vector();
        cvData = getAddNarrativeData();
        /*added for the CoeusEnhancements case:1624 , get the narrative types from the database and
         *filter for the types where SYSTEM_GENERATED="N"*/
        //System.out.println(cvData.get(0));
        if(cvData.get(0)!=null){
            narrativeTypes = (Vector)cvData.get(0);
        }
        if(cvData.get(1)!=null){
            paramNarrativeTypeCode = (String)cvData.get(1);
            //paramNarrativeTypeCode = Integer.parseInt(narrativeTypeCode);
            
        }
        
        moduleNarrativeTypes = new Vector();
        if(narrativeTypes != null && narrativeTypes.size()>0){
            for(int i=0;i<narrativeTypes.size();i++){
                ProposalNarrativeTypeBean bean = (ProposalNarrativeTypeBean)narrativeTypes.get(i);
                if(bean.getSystemGenerated().equals("N")){
                    //add the beans to the moduleNarrativeTypes vector
                    moduleNarrativeTypes.add(bean);
                }
            }
        }
    }//End Coeus Enhancement case:1776 step:4
    
    //Modified for bug id 1860 step 6 : start
    private void modifyModule() throws Exception{
        int selRow = tblModule.getSelectedRow();
        //Added for case#2420 - Upload of files on Edit Module Details Window - start               
        moduleTitle = "Edit Module";
        /*Added for the Coeus Enhancement case:1776 start step:5*/
        setNarrativeTypeAndCode();
        /*End Coeus Enhancement case:1776 step:5*/
        
        if( selRow >= 0 ) {
            String modNum = (String) tblModule.getValueAt(selRow,2);
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
            ///Modified for case #1856 start 4
            //            if(hasModifyNarrativeRights() || hasAlterRight   ){
            //                //Modified for case #1856 end 4
            //                //Modified for Case #2368 start 3
            //                if(isUserHasModifyRight( modNum )){
            //                    showModuleInfo((ProposalNarrativeFormBean)hmModules.get(modNum),MODIFY_MODE );
            //                }else{
            //                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
            //                    "proposal_narr_exceptionCode.6600" ));
            //                    return;
            //                }
            //                //Modified for case #2368 end 3
            //            }else {
            //                throw new Exception(coeusMessageResources.parseMessageKey(
            //                "proposal_narr_exceptionCode.6600" ));
            //            }
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
            //// Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
              //Check the User has OSP Level or Departmental Level rights. If user has any one of the right allow to Edit Module
              // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//            if(isUserHasModifyRight( modNum ) || canModifyNarrative){ 
            if(canUserEditModule(modNum)){
                showModuleInfo((ProposalNarrativeFormBean)hmModules.get(modNum),MODIFY_MODE );
            }else{
                // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                "proposal_narr_exceptionCode.6600" ));
                return;
            }
            //Modified for case #2368 end 3
        }else{
            throw new Exception(coeusMessageResources.parseMessageKey(
            "proposal_narr_exceptionCode.6601" ));
        }
        
    }
    //bug id 1860 step 6 : end
    
    private void deleteModule() throws Exception{
        int selRow = tblModule.getSelectedRow();
        if( selRow >= 0 ) {
            String modNum = (String) tblModule.getValueAt(selRow,2);
            //Modified for case #1856 start 5
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
            //   if( hasModifyNarrativeRights()  ){
            // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
            // Added for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
             //Check the User has OSP Level or Departmental Level rights. If user has any one of the right allow to Edit Module
             // COEUSDEV-308: Application not checking module level rights for a user in Narrative
//            if( canModifyNarrative || isUserHasModifyRight(modNum)){
            if(canUserEditModule(modNum)){
                // Commented for Case 3316 - OSP/Departemental User able to modify the narrative modules -Start
                //Modified for case #1856 end 5
                //Modified for case #2366 start
                //                if(!isUserHasModifyRight( modNum ) && !canAlterProposalAtPropLevel){
                //                    CoeusOptionPane.showErrorDialog(
                //                    coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6603"));
                //                    return;
                //                }
                //Modified for case #2366 end
                // Commnted for Case 3316 - OSP/Departemental User able to modify the narrative modules -End
                String msg = coeusMessageResources.parseMessageKey(
                "proposal_narr_exceptionCode.6602" );
                //coeusMessageResources.parseMessageKey("saveConfirmCode.1002");
                
                int confirm = CoeusOptionPane.showQuestionDialog(msg,
                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                if( confirm == JOptionPane.YES_OPTION ) {
                    if( Integer.parseInt( modNum ) == maxModuleNo ) {
                        maxModuleNo--;
                    }
                    saveRequired = true;
                    ProposalNarrativeFormBean narrBean = (ProposalNarrativeFormBean)hmModules.get(modNum);
                    if( narrBean.getModuleSequenceNumber() == maxSeqNo ) {
                        maxSeqNo--;
                    }
                    //Code added for Proposal Hierarchy case#3183
                    narrBean.setParentProposal(isParent());
                    if( narrBean.getAcType() == null || narrBean.getAcType().equals( UPDATE_RECORD ) ){
                        narrBean.setAcType( DELETE_RECORD );
                        hmDeletedModules.put(""+narrBean.getModuleNumber(),narrBean);
                        //hmModulesToDB.put(""+narrBean.getModuleNumber(),narrBean);
                    }else{
                        hmModulesToDB.remove(""+narrBean.getModuleNumber());
                    }
                    hmModules.remove(""+narrBean.getModuleNumber());
                    hmUsers.remove(""+narrBean.getModuleNumber());
                    
                    ((DefaultTableModel)tblModule.getModel()).removeRow( selRow );
                    
                    int newRowCount = tblModule.getRowCount();
                    if(newRowCount >0){
                        if(newRowCount > selRow){
                            tblModule.setRowSelectionInterval(selRow, selRow);
                        }else{
                            tblModule.setRowSelectionInterval(newRowCount - 1,
                                    newRowCount - 1);
                        }
                        
                        /*if( tblModule.getRowCount() == 1 ) {
                            btnUp.setEnabled( false );
                            btnDown.setEnabled( false );
                            moveDown.setEnabled( false );
                            moveUp.setEnabled( false );
                        }*/
                    }else{
                        // as there are no modules available for this proposal, reset
                        //module no. and seq no. ( for bug fix : 1097 )
                        maxModuleNo = 0;
                        maxSeqNo = 0;
                        //end of bug fix : 1097
                        tblUsers.setModel( new DefaultTableModel(
                                new Object[][]{}, getUserColumnNames().toArray()){
                            public boolean isCellEditable( int row, int col ) {
                                return false;
                            }
                        });
                        txtFldContactName.setText(null);
                        txtFldEmail.setText(null);
                        txtFldPhoneNumber.setText(null);
                        txtArComments.setText(null);
                        // Added for the Coeus enhancement case:#1766
                        //Commented for Proposal Hierarchy Enhancement Case# 3183 - Start
                        //txtUpdateTimestamp.setText(null);
                        //Commented for Proposal Hierarchy Enhancement Case# 3183 - Start
                        txtUpdateUser.setText(null);
                        // Case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads
                        txtFileName.setText(null);
                        setTableEditors();
                        
                    }
                    //formatFields();
                    saveNarrativeDetails();
                }
            }else {
                throw new Exception(coeusMessageResources.parseMessageKey(
                "proposal_narr_exceptionCode.6603" ));
            }
            
        }else{
            throw new Exception(coeusMessageResources.parseMessageKey(
            "proposal_narr_exceptionCode.6601" ));
        }
        
    }
    
    /*Added for the Coeus Enhancement case:1776 start step:6*/
    private Vector getAddNarrativeData() throws Exception{
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType( GET_ADD_NARRATIVE_DATA );
        //case 2333 adding next line
        request.setId( proposalNumber );
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        } else if( response.isLocked() ) {
            //Modified for case# 3439 to include the locking message - start
			//if proposal is locked by another user and the 
            //status is 'Approval in Progress'(2) or 'Approved'(4)
            //open in display mode without prompting the user 
            if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS || proposalStatusCode == APPROVED_STATUS){
                dataObjects = response.getDataObjects();
                functionType = DISPLAY_MODE;
                return dataObjects;
            }

	    //For all other statuses prompt the user before opening the narrative
            String msg = "";
            if(response.getMessage()!=null){
                msg = msg + response.getMessage().trim() + ". ";
            }
            msg = msg + coeusMessageResources.parseMessageKey(
					"narrative_row_locked_exceptionCode.123456");
//             String msg = coeusMessageResources.parseMessageKey(
//            "narrative_row_locked_exceptionCode.123456");
            //Modified for case# 3439 to include the locking message - end
            int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            if (resultConfirm == 0) {
                dataObjects = response.getDataObjects();
                functionType = DISPLAY_MODE;
                return dataObjects;
            }else{
                // throwing empty exception to stop further proceeding of opening
                //narrative
                throw new Exception("");
            }
            
        }else{
            throw new Exception(response.getMessage());
        }
        return dataObjects;
        
        
    }
    /* End Coeus Enhancement case:1776 step:6*/
    
    private void addNarrative() throws Exception{
        //        if( isUserHasProposalRight( mdiForm.getUserName(),proposalNumber,
        //                MODIFY_NARRATIVE_RIGHT) ) {
        
        //Added for case#2420 - Upload of files on Edit Module Details Window - start               
        moduleTitle = "Add Module";        
        proposalNarrativeFormBean = new ProposalNarrativeFormBean();
        proposalNarrativeFormBean.setProposalNumber( proposalNumber );
        proposalNarrativeFormBean.setModuleNumber( maxModuleNo+1 );
        proposalNarrativeFormBean.setModuleSequenceNumber( maxSeqNo+1 );
            /*if ( usersForProposal == null ) {
                getNarrativeUsersForProposal( proposalNumber );
            }*/
        if( usersForProposal != null && usersForProposal.size() > 0 ) {
            ProposalNarrativeModuleUsersFormBean modUserBean = null;
            for( int indx=0;indx< usersForProposal.size(); indx++ ) {
                modUserBean = ( ProposalNarrativeModuleUsersFormBean )
                usersForProposal.elementAt(indx );
                modUserBean.setAcType( INSERT_RECORD );
                ////System.out.println("userBean:"+modUserBean.getUserId());
            }
            
        }
        /*Added for the Coeus Enhancement case:1776 start step:7*/
        setNarrativeTypeAndCode();
        /*End Coeus Enhancement case:1776 End step:7*/
        
        proposalNarrativeFormBean.setPropModuleUsers( usersForProposal );
        proposalNarrativeFormBean.setAcType( INSERT_RECORD );
        //Code added for Proposal Hierarchy case#3183
        proposalNarrativeFormBean.setParentProposal(isParent());
        //Added for case #2366 start 1
        proposalNarrativeFormBean.setModuleStatusCode('I');
        //Added for case #2366 end 2
        showModuleInfo( proposalNarrativeFormBean, ADD_MODE);
        //        }else{
        //            throw new Exception(coeusMessageResources.parseMessageKey(
        //                        "proposal_narr_exceptionCode.6604" ));
        //        }
    }
    
    private void showModuleInfo( ProposalNarrativeFormBean formBean,char fType) {
        existingBean = new ProposalNarrativeFormBean();
        existingBean.setProposalNumber( formBean.getProposalNumber() );
        existingBean.setComments( formBean.getComments() );
        existingBean.setContactName( formBean.getContactName() );
        existingBean.setEmailAddress( formBean.getEmailAddress() );
        existingBean.setModuleNumber( formBean.getModuleNumber() );
        existingBean.setModuleSequenceNumber( formBean.getModuleSequenceNumber() );
        existingBean.setModuleStatusCode( formBean.getModuleStatusCode() );
        existingBean.setNarrativeTypeCode(formBean.getNarrativeTypeCode());
        existingBean.setModuleTitle( formBean.getModuleTitle() );
        existingBean.setPhoneNumber( formBean.getPhoneNumber() );
        //Code added for Proposal Hierarchy case#3183
        existingBean.setParentProposal(formBean.isParentProposal());
        existingBean.setAcType( formBean.getAcType() );
        //Added for case #2366 start 2
        existingBean.setHasPDFModuleNumber(formBean.getHasPDFModuleNumber());
        existingBean.setHasSourceModuleNumber(formBean.getHasSourceModuleNumber());        
        //Added for case #2366 end 2        
        //Added for case#2420 - Upload of files on Edit Module Details Window - start
        if(formBean.getFileName() != null){
            existingBean.setFileName(formBean.getFileName());
        }
        if(formBean.getUpdateUser() != null){
            existingBean.setUpdateUser(formBean.getUpdateUser());
        }
        if(formBean.getUpdateTimestamp() != null){
            existingBean.setUpdateTimestamp(formBean.getUpdateTimestamp());
        }
        //Added for case#2420 - Upload of files on Edit Module Details Window - end
        Vector newUsers = new Vector();
        if( formBean.getPropModuleUsers() != null ) {
            Vector oldUsers = formBean.getPropModuleUsers();
            ProposalNarrativeModuleUsersFormBean oldBean,newBean;
            for(int indx = 0; indx < oldUsers.size(); indx++) {
                oldBean = ( ProposalNarrativeModuleUsersFormBean) oldUsers.elementAt(indx);
                newBean = new ProposalNarrativeModuleUsersFormBean();
                newBean.setAcType( oldBean.getAcType() );
                newBean.setAccessType( oldBean.getAccessType() );
                newBean.setModuleNumber( oldBean.getModuleNumber() );
                newBean.setProposalNumber( oldBean.getProposalNumber() );
                newBean.setUpdateTimestamp( oldBean.getUpdateTimestamp() );
                newBean.setUpdateUser( oldBean.getUpdateUser() );
                newBean.setUserId( oldBean.getUserId() );
                newBean.setUserName( oldBean.getUserName() );
                newUsers.addElement( newBean );
            }
        }
        existingBean.setPropModuleUsers( newUsers );
        existingBean.setUpdateTimestamp( formBean.getUpdateTimestamp() );
        existingBean.setUpdateUser( formBean.getUpdateUser() );
        existingBean.setAcType( formBean.getAcType() );
        //Modified for case#2420 - Upload of files on Edit Module Details Window - start               
        dlgDetails = new CoeusDlgWindow(mdiForm, moduleTitle, true);        
        moduleDetails = new ProposalNarrativeModuleDetails(existingBean, fType );
        //Code added for Proposal Hierarchy case#3183
        moduleDetails.setParentProposal(isParent());
        //Added for case#3183 - Proposal Hierarchy Enhancement
        moduleDetails.setHierarchy(isHierarchy());        
        //Modified for bug id 1860 step 7 : start
        if((fType == MODIFY_MODE) && hasAlterRight) {
            moduleDetails.setIsAlterProposal(true);
        }else {
            moduleDetails.setIsAlterProposal(false);
        }
        //bug id 1860 step 7 : end
        moduleDetails.setStatusLookup( statusLookUp );
        moduleDetails.setNarrativeTypes(moduleNarrativeTypes);
        /*Added for the Coeus Enhancement case:1776 start step:8*/
        moduleDetails.setParameterNarrativeTypeCode(paramNarrativeTypeCode);
        /*End Coeus Enhancement case:1776 start step:8*/
        moduleDetails.setProposalStatusCode(proposalStatusCode);
        moduleDetails.setNarrativeBeans(narrativeBean);
        moduleDetails.setProposalUserRights( hmProposalUserRights );
        moduleDetails.registerObserver( this );
        //Added for case#2999 - Multiple emails generated when a document is uploaded
        moduleDetails.setCanModifyNarrative(canModifyNarrative);
        dlgDetails.getContentPane().add( moduleDetails.showForm( dlgDetails ) );
        dlgDetails.pack();
        
        // displaying the window in center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgDetails.getSize();
        dlgDetails.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgDetails.setResizable(false);
        dlgDetails.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgDetails.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                validateData();
            }
        });
        dlgDetails.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                validateData();
            }
        });
        
        dlgDetails.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                moduleDetails.setInitialFocus();
            }
        });
        
        dlgDetails.show();
        //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - start
        if( moduleDetails.isSaveRequired() ) {            
            //commented by ravi on 10-Mar-04 as the data will be saved on close
            //of the dialog and also we are synchronizing the data with that of
            // proposal all the times before closing.
            //            saveRequired = true;
            formBean = existingBean;
            formBean.setPropModuleUsers( existingBean.getPropModuleUsers() );
            if( formBean.getAcType() == null ) {
                formBean.setAcType( UPDATE_RECORD );
            }
            
            hmModulesToDB.put( ""+formBean.getModuleNumber(), formBean );
            hmModules.put(""+formBean.getModuleNumber(),formBean );
            hmUsers.put(""+formBean.getModuleNumber(), formBean.getPropModuleUsers() );
            //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - start
            boolean saveFlag = saveNarrativeDetails();
            if(saveFlag){
                if( fType == ADD_MODE ) {
                    maxModuleNo++;
                    maxSeqNo++;
                    ((DefaultTableModel)tblModule.getModel()).addRow( getModuleTableRow( formBean ) );
                    formatFields();
                    int lastRow = tblModule.getRowCount() -1;
                    if( lastRow >= 0 ) {
                        tblModule.setRowSelectionInterval(lastRow, lastRow );
                    }
                    /*if( tblModule.getRowCount() > 1 ) {
                        btnUp.setEnabled( true );
                        btnDown.setEnabled( true );

                    }*/

                }else{
                    int selRow = tblModule.getSelectedRow();
                    if( selRow >= 0 ) {                                                                   
                        ((DefaultTableModel)tblModule.getModel()).setValueAt(
                        hmStatusCodes.get(""+formBean.getModuleStatusCode()),selRow,3);                           
                        ((DefaultTableModel)tblModule.getModel()).setValueAt(
                        formBean.getModuleTitle(),selRow,5);
                        ((DefaultTableModel)tblModule.getModel()).setValueAt(
                        getNarrativeDescription(formBean.getNarrativeTypeCode()),selRow,4);                                            
                        showModuleDetails(""+formBean.getModuleNumber());                    
                    }
                }
                setTableEditors();                            
            }
        }
        //Added/Modified for case#2420 - Upload of files on Edit Module Details Window - end
    }    
    
    private boolean isUserHasModifyRight(String modNumber){
        boolean hasRight = false;
        if( hmUsers != null && hmUsers.containsKey( modNumber) ) {
            Vector userRights = (Vector) hmUsers.get(modNumber);
            String loggedInUser = mdiForm.getUserName();
            for( int indx=0; indx < userRights.size(); indx++) {
                ProposalNarrativeModuleUsersFormBean userFormBean =
                (ProposalNarrativeModuleUsersFormBean)userRights.elementAt( indx );
                if( userFormBean.getUserId().equalsIgnoreCase( loggedInUser ) ){
                    if( userFormBean.getAccessType() == 'M' ){
                        return true;
                    }
                }
            }
        }
        return hasRight;
    }
    private void validateData(){
        if ( moduleDetails.isDataChanged()  ) {
            String msg = coeusMessageResources.parseMessageKey(
            "saveConfirmCode.1002");
            
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :
                    moduleDetails.setSaveRequired( false );
                    dlgDetails.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    try{
                        if( moduleDetails.validateData() ){
                            //Modified for case id 3183- Proposal Hierarchy - start
                            //saveRequired = true;
                            //System.out.println("setting save req in yes option of save conf in validate");
                            moduleDetails.setSaveRequired( true );
                            boolean canDispose = moduleDetails.saveNarrativeDetails(); 
                            if(canDispose){
                                dlgDetails.dispose();
                            }
                            //Modified for case id 3183 -Proposal Hierarchy - end
                        }
                    }catch(Exception e) {
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    dlgDetails.setVisible( true );
                    //return;
                    break;
            }
            
        }else{
            dlgDetails.dispose();
        }
    }
    
    private boolean saveNarrativeDetails() {
        Vector dataObjects = new Vector();
        boolean saveSuccess = true;
        boolean alterProposal = false;
        if( saveRequired ) {
            Vector dataToDB = new Vector( hmModulesToDB.values() );
            if( hmDeletedModules.size() > 0 ){
                dataToDB.addAll(0,hmDeletedModules.values());
            }
            Vector vecAlterNarrData = null;
            //Added for the Coeus Enhancemnt case:#1767
            if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS || proposalStatusCode == APPROVED_STATUS) {
                vecAlterNarrData = new Vector();
                if(functionType == MODIFY_MODE)
                    alterProposal = true;
                vecAlterNarrData.addElement(CoeusGuiConstants.getMDIForm().getUnitNumber());
                vecAlterNarrData.addElement(new Boolean(alterProposal));
            }
            dataToDB.addElement(vecAlterNarrData);
            //End for the Coeus Enhancemnt case:#1767
            String connectTo = CoeusGuiConstants.CONNECTION_URL +
            CoeusGuiConstants.PROPOSAL_SERVLET ;
            
            RequesterBean request = new RequesterBean();
            
            request.setFunctionType( UPDATE_NARRATIVE_DETAILS );
            request.setDataObjects( dataToDB );
            request.setId( proposalNumber );
            
            AppletServletCommunicator comm
            = new AppletServletCommunicator( connectTo, request );
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(coeusMessageResources.parseMessageKey(
                "server_exceptionCode.1000"));
                return saveSuccess;
            }
            if(response!=null && response.getId()!=null && response.getId().equals("-1")){
                serverMsg = coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6617") +" "
                        +proposalNumber +" "
                        +coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6618");
                CoeusOptionPane.showErrorDialog(serverMsg);
                saveSuccess = false;                
                return saveSuccess;
            }
            if (response!=null && response.isSuccessfulResponse()) {
                saveRequired = false;
                dataObjects = response.getDataObjects();
                hmModulesToDB.clear();
                hmDeletedModules.clear();
                setValues( dataObjects );
            }
            
            //Added for case#2420 - Upload of files on Edit Module Details Window - start
            ProposalNarrativeFormBean formBean = (ProposalNarrativeFormBean)dataToDB.get(0);
            if(formBean.getFileName() != null && formBean.getFileBytes()!= null && !formBean.getFileName().equals("")){                
                ProposalNarrativePDFSourceBean pdfSourceBean = new ProposalNarrativePDFSourceBean();
                pdfSourceBean.setProposalNumber(formBean.getProposalNumber());
                pdfSourceBean.setModuleNumber(formBean.getModuleNumber());
                pdfSourceBean.setFileName(formBean.getFileName());
                pdfSourceBean.setFileBytes(formBean.getFileBytes());
                pdfSourceBean.setMimeType(formBean.getMimeType());//4007
                pdfSourceBean.setUpdateUser(formBean.getUpdateUser());
                pdfSourceBean.setUpdateTimestamp(formBean.getUpdateTimestamp());
                pdfSourceBean.setAcType(formBean.getAttachmentAcType());
                try{
                    boolean uploadStatus = uploadPDF(pdfSourceBean);
                    if(!uploadStatus){
                        CoeusOptionPane.showErrorDialog(serverMsg);
                    }else{
                        int selectedRow = tblModule.getSelectedRow();            
                        String module = (String) tblModule.getValueAt(selectedRow, 2);
                        int moduleNumber = Integer.parseInt(module);            
                        tblModule.getModel().setValueAt(new Boolean(true), selectedRow, 1);
                        tblModule.setRowSelectionInterval(selectedRow, selectedRow);                        
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            //Added for case#2420 - Upload of files on Edit Module Details Window - end                        
        }
        return saveSuccess;
    }
    
    private void closeNarrativeDetails() throws Exception{
        CoeusInternalFrame frame;
        if ( ! saveRequired ) {
            synchronizeNarrativeStatus();
        }else{
            String msg = coeusMessageResources.parseMessageKey("saveConfirmCode.1002");
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            
            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    //Code commented for case#3316 - Bug fix
                    //Save functionality in Narrative detil window was removed
                    //Uncommented code for Case#3780 - Sort ID on Narrative - Start
                    saveNarrativeDetails();
                    //Uncommented code for Case#3780 - Sort ID on Narrative - End
                    synchronizeNarrativeStatus();
                    break;
                case(JOptionPane.NO_OPTION):
                    saveRequired = false;
                    synchronizeNarrativeStatus();
                    break;
                case ( JOptionPane.CANCEL_OPTION ):
                case ( JOptionPane.CLOSED_OPTION ):
                    throw new PropertyVetoException(
                    coeusMessageResources.parseMessageKey(
                    "protoDetFrm_exceptionCode.1130"),null);
            }
        }
    }
    
    private void synchronizeNarrativeStatus() throws Exception{
        if( functionType != CoeusGuiConstants.DISPLAY_MODE ) {
            Character narrativeStatus = null;
            Collection values = hmModules.values();
            if( values != null && values.size() > 0 ) {
                boolean complete = true;
                Iterator valueIterator = values.iterator();
                while( valueIterator.hasNext() ) {
                    ProposalNarrativeFormBean narrBean =
                    (ProposalNarrativeFormBean)valueIterator.next();
                    if( 'I' == narrBean.getModuleStatusCode() ){
                        complete = false;
                        break;
                    }
                }
                if( complete ) {
                    narrativeStatus = new Character('C');
                }else{
                    narrativeStatus = new Character('I');
                }
            }else{
                narrativeStatus = new Character('N');
            }
            String connectTo = CoeusGuiConstants.CONNECTION_URL +
            "/ProposalMiscellaniesServlet" ;
            
            Vector vecData = new Vector();
            RequesterBean request = new RequesterBean();
            request.setFunctionType('F');
            vecData.addElement(proposalNumber);
            vecData.addElement(narrativeStatus);
            //Modified for Proposal Narrative Locking Issue case id 3316 - start
            boolean releaseLock = (functionType == MODIFY_MODE) ;
             vecData.addElement(new Boolean(releaseLock));
             //Modified for Proposal Narrative Locking Issue case id 3316 - end
            //Code commented for case#3316 - Bug fix - starts
            //            if(proposalStatusCode == 2  || proposalStatusCode == 4){
            //                vecData.addElement(new Boolean(false));
            //            }else{
            //
            //            }
            //Code commented for case#3316 - Bug fix - ends
            request.setDataObjects( vecData );
            
            AppletServletCommunicator comm = new AppletServletCommunicator(
            connectTo, request );
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response == null) {
                response = new ResponderBean();
                response.setResponseStatus(false);
                response.setMessage(coeusMessageResources.parseMessageKey(
                "server_exceptionCode.1000"));
            }
            if (!response.isSuccessfulResponse()) {
                throw new Exception(response.getMessage());
            }
            /** Added by chandra.  Check if the proposal detail form is opened
             *If not save the data to the DB. else if the parent window is opened,
             *update the status immediately to the form and bean
             *Bug Fix #972, #976 - start 30th June 2004
             */
            CoeusInternalFrame frame = mdiForm.getFrame(CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE,proposalNumber);
            if(frame!= null){
                if(frame.isEnabled()){
                    if(narrativeStatus!= null){
                        if(narrativeStatus.charValue()=='C'){
                            ((ProposalDetailForm)frame).setNarrativeStatusCode('C');
                        }else if(narrativeStatus.charValue()=='I'){
                            ((ProposalDetailForm)frame).setNarrativeStatusCode('I');
                        }else if(narrativeStatus.charValue()=='N'){
                            ((ProposalDetailForm)frame).setNarrativeStatusCode('N');
                        }
                    }
                }
            }//Added by  Chandra - End 30th June 2004
        }
        mdiForm.removeFrame(CoeusGuiConstants.PROPOSAL_NARRATIVE_FRAME_TITLE,proposalNumber);
        
    }
    
    
    /**
     * Used for saving the current activesheet  when clicked Save from File menu.
     */
    public void saveActiveSheet(){
        saveNarrativeDetails();
    }
    
    public void valueChanged( ListSelectionEvent lse ) {
        Object source = lse.getSource();
        
        int selectedRow = tblModule.getSelectedRow();
        int rowCount = tblModule.getRowCount();
        
        if( (selectedRow >= 0 ) && (hmModules != null)) {
            String moduleNo = null;
            moduleNo = ( String )(tblModule.getValueAt( selectedRow,2) == null ? "" :
                tblModule.getValueAt( selectedRow,2));
                if( moduleNo.length() > 0  ) {
                    showModuleDetails( moduleNo );
                    setTableEditors();
                    //Code added for Proposal Hierarchy case#3183 - starts
                    //To enable and disable the buttons for narratives in parent proposal.
                    if(isParent()){
                        //Added for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
                        MessageFormat formatter = new MessageFormat("");
                        String message = EMPTY_STRING;
                        if(proposalStatusCode == APPROVAL_IN_PROGRESS_STATUS){
                            changeButtonStatus(true);
                            lblMessage.setForeground(Color.BLUE);
                            message = formatter.format(
                                    coeusMessageResources.parseMessageKey(CAN_MODIFY_NARRATIVE), hmPropHierLink.get(moduleNo));
                            lblMessage.setText(message);
                        }else//COEUSQA-1579 : End
                        if(hmPropHierLink.get(moduleNo) != null
                        && !hmPropHierLink.get(moduleNo).equals(proposalNumber)){
                            changeButtonStatus(false);
                            lblMessage.setForeground(Color.RED);
                            //Modified for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
                            //Message moved to messages.property file
//                            lblMessage.setText("This attachment was uploaded from proposal number "+
//                            hmPropHierLink.get(moduleNo) + " and cannot be edited in this proposal");
                            message = formatter.format(
                                    coeusMessageResources.parseMessageKey(CANNOT_MODIFY_NARRATIVE), hmPropHierLink.get(moduleNo));
                            lblMessage.setText(message);
                            //COEUSQA-1579 : End
                        } else if(functionType != DISPLAY_MODE) {
                            String modeNo = (String) hmPropHierLink.get(moduleNo);
                            modeNo = (modeNo == null)? proposalNumber : modeNo;
                            changeButtonStatus(true);
                            lblMessage.setForeground(Color.BLUE);
                            //Modified for COEUSQA-1579 : For Hierarchy Proposal, Approval in Progress, cannot sync after narratives updated on child proposal - Start
                            //Message moved to messages.property file
//                            lblMessage.setText("This attachment is added at the proposal "+
//                            modeNo + " and should be edited in this proposal");
                            message = formatter.format(
                                    coeusMessageResources.parseMessageKey(CAN_MODIFY_NARRATIVE), modeNo);
                            lblMessage.setText(message);
                            //COEUSQA-1579 : End

                        }
                    }else{
                        lblMessage.setText("");
                    }
                }
        } else {
            lblMessage.setText("");
            txtLastDocUpdateUser.setText("");
            txtUpdateUser.setText("");
        }
        //Code added for Proposal Hierarchy case#3183 - ends
    }
    
    /**
     * To enable and disable the buttons in the form
     * @param status boolean
     */
    private void changeButtonStatus(boolean status){
        btnModify.setEnabled( status );
        btnDelete.setEnabled( status );
        btnUploadWord.setEnabled( status );
        btnUploadPdf.setEnabled( status );
        modifyModule.setEnabled( status );
        deleteModule.setEnabled( status );
        editSource.setEnabled( status );
        uploadWordFiles.setEnabled( status );
        uploadPdfFiles.setEnabled( status );
    }
    
    private boolean isUserHasProposalRight( String userId, String proposalNumber,
    String rightId ) throws Exception {
        Vector dataObjects = null;
        boolean hasRight = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType( USER_HAS_RIGHT );
        dataObjects = new Vector();
        dataObjects.addElement( userId );
        dataObjects.addElement( proposalNumber );
        dataObjects.addElement( rightId );
        request.setDataObjects( dataObjects );
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            Integer dataObject = (Integer) response.getDataObject();
            hasRight = dataObject.intValue() == 1 ? true : false ;
        } else {
            throw new Exception(response.getMessage());
        }
        return hasRight;
    }
    //Added for case #1856 start 6
    private boolean hasModifyNarrativeRights() throws Exception {
        boolean hasRight = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType( GET_MODIFY_NARRATIVE_DATA );
        request.setId(proposalNumber);
        request.setDataObject(unitNumber);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
            "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            Boolean data = (Boolean) response.getDataObject();
            hasRight = data.booleanValue();
        } else {
            throw new Exception(response.getMessage());
        }
        return hasRight;
    }
    //Added for case #1856 end 6
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlModuleUsers = new javax.swing.JPanel();
        scrPnModule = new javax.swing.JScrollPane();
        tblModule = new javax.swing.JTable();
        scrPnUsers = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        pnlContact = new javax.swing.JPanel();
        pnlContactDetails = new javax.swing.JPanel();
        lblContactName = new javax.swing.JLabel();
        lblPhoneNumber = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblComments = new javax.swing.JLabel();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        txtFldContactName = new edu.mit.coeus.utils.CoeusTextField();
        txtFldPhoneNumber = new edu.mit.coeus.utils.CoeusTextField();
        txtFldEmail = new edu.mit.coeus.utils.CoeusTextField();
        pnlUserTimeStamp = new javax.swing.JPanel();
        lblUpdatedBy = new javax.swing.JLabel();
        txtLastDocUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblLastDocument = new javax.swing.JLabel();
        txtUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblMessage = new javax.swing.JLabel();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();

        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(975, 700));
        pnlMain.setPreferredSize(new java.awt.Dimension(975, 700));
        pnlMain.setRequestFocusEnabled(false);
        pnlModuleUsers.setLayout(new java.awt.BorderLayout(10, 0));

        pnlModuleUsers.setMinimumSize(new java.awt.Dimension(965, 380));
        pnlModuleUsers.setPreferredSize(new java.awt.Dimension(965, 380));
        scrPnModule.setMinimumSize(new java.awt.Dimension(560, 200));
        scrPnModule.setPreferredSize(new java.awt.Dimension(580, 200));
        tblModule.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblModule.setFont(CoeusFontFactory.getNormalFont());
        tblModule.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnModule.setViewportView(tblModule);

        pnlModuleUsers.add(scrPnModule, java.awt.BorderLayout.WEST);

        scrPnUsers.setMinimumSize(new java.awt.Dimension(370, 200));
        scrPnUsers.setPreferredSize(new java.awt.Dimension(370, 200));
        tblUsers.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        tblUsers.setFont(CoeusFontFactory.getNormalFont());
        scrPnUsers.setViewportView(tblUsers);

        pnlModuleUsers.add(scrPnUsers, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlMain.add(pnlModuleUsers, gridBagConstraints);

        pnlContact.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pnlContact.setMinimumSize(new java.awt.Dimension(890, 180));
        pnlContact.setPreferredSize(new java.awt.Dimension(890, 180));
        pnlContactDetails.setLayout(new java.awt.GridBagLayout());

        pnlContactDetails.setMinimumSize(new java.awt.Dimension(965, 250));
        pnlContactDetails.setPreferredSize(new java.awt.Dimension(965, 180));
        lblContactName.setFont(CoeusFontFactory.getLabelFont());
        lblContactName.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblContactName.setText("Contact Name :");
        lblContactName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblContactName.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lblContactName.setMaximumSize(new java.awt.Dimension(95, 15));
        lblContactName.setMinimumSize(new java.awt.Dimension(105, 15));
        lblContactName.setPreferredSize(new java.awt.Dimension(105, 15));
        lblContactName.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContactDetails.add(lblContactName, gridBagConstraints);

        lblPhoneNumber.setFont(CoeusFontFactory.getLabelFont());
        lblPhoneNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPhoneNumber.setText("Phone Number :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContactDetails.add(lblPhoneNumber, gridBagConstraints);

        lblEmail.setFont(CoeusFontFactory.getLabelFont());
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblEmail.setText("Email Address :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        pnlContactDetails.add(lblEmail, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblComments.setText("Comments :");
        lblComments.setMaximumSize(new java.awt.Dimension(95, 15));
        lblComments.setMinimumSize(new java.awt.Dimension(105, 15));
        lblComments.setPreferredSize(new java.awt.Dimension(105, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContactDetails.add(lblComments, gridBagConstraints);

        scrPnComments.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnComments.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrPnComments.setMinimumSize(new java.awt.Dimension(200, 50));
        scrPnComments.setPreferredSize(new java.awt.Dimension(200, 50));
        txtArComments.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
        txtArComments.setColumns(25);
        txtArComments.setEditable(false);
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        txtArComments.setPreferredSize(new java.awt.Dimension(200, 18));
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContactDetails.add(scrPnComments, gridBagConstraints);

        txtFldContactName.setEditable(false);
        txtFldContactName.setMinimumSize(new java.awt.Dimension(190, 20));
        txtFldContactName.setPreferredSize(new java.awt.Dimension(190, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 45);
        pnlContactDetails.add(txtFldContactName, gridBagConstraints);

        txtFldPhoneNumber.setEditable(false);
        txtFldPhoneNumber.setMinimumSize(new java.awt.Dimension(190, 20));
        txtFldPhoneNumber.setPreferredSize(new java.awt.Dimension(190, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 50);
        pnlContactDetails.add(txtFldPhoneNumber, gridBagConstraints);

        txtFldEmail.setEditable(false);
        txtFldEmail.setMinimumSize(new java.awt.Dimension(190, 20));
        txtFldEmail.setPreferredSize(new java.awt.Dimension(190, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContactDetails.add(txtFldEmail, gridBagConstraints);

        pnlUserTimeStamp.setLayout(new java.awt.GridBagLayout());

        pnlUserTimeStamp.setEnabled(false);
        pnlUserTimeStamp.setFocusable(false);
        pnlUserTimeStamp.setMinimumSize(new java.awt.Dimension(890, 150));
        pnlUserTimeStamp.setPreferredSize(new java.awt.Dimension(890, 150));
        lblUpdatedBy.setFont(CoeusFontFactory.getLabelFont());
        lblUpdatedBy.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUpdatedBy.setText("Last Updated by :");
        lblUpdatedBy.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblUpdatedBy.setMaximumSize(new java.awt.Dimension(150, 20));
        lblUpdatedBy.setMinimumSize(new java.awt.Dimension(150, 20));
        lblUpdatedBy.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlUserTimeStamp.add(lblUpdatedBy, gridBagConstraints);

        txtLastDocUpdateUser.setEditable(false);
        txtLastDocUpdateUser.setMinimumSize(new java.awt.Dimension(380, 20));
        txtLastDocUpdateUser.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 6);
        pnlUserTimeStamp.add(txtLastDocUpdateUser, gridBagConstraints);

        lblLastDocument.setFont(CoeusFontFactory.getLabelFont());
        lblLastDocument.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastDocument.setText("Last Document Uploaded by :");
        lblLastDocument.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblLastDocument.setMaximumSize(new java.awt.Dimension(170, 20));
        lblLastDocument.setMinimumSize(new java.awt.Dimension(160, 20));
        lblLastDocument.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlUserTimeStamp.add(lblLastDocument, gridBagConstraints);

        txtUpdateUser.setEditable(false);
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(380, 20));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 6);
        pnlUserTimeStamp.add(txtUpdateUser, gridBagConstraints);

        lblMessage.setFont(CoeusFontFactory.getLabelFont());
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMessage.setMaximumSize(new java.awt.Dimension(95, 20));
        lblMessage.setMinimumSize(new java.awt.Dimension(95, 20));
        lblMessage.setPreferredSize(new java.awt.Dimension(95, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        pnlUserTimeStamp.add(lblMessage, gridBagConstraints);

        lblFileName.setFont(CoeusFontFactory.getLabelFont());
        lblFileName.setText("File Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlUserTimeStamp.add(lblFileName, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setEnabled(false);
        txtFileName.setMinimumSize(new java.awt.Dimension(380, 20));
        txtFileName.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 2);
        pnlUserTimeStamp.add(txtFileName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlContactDetails.add(pnlUserTimeStamp, gridBagConstraints);

        pnlContact.add(pnlContactDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        pnlMain.add(pnlContact, gridBagConstraints);

        getContentPane().add(pnlMain);

    }// </editor-fold>//GEN-END:initComponents

    public void saveAsActiveSheet() {
    }    
    
    public void update(Observable o, Object arg) {
        int selRow = tblModule.getSelectedRow();
        if( arg instanceof Vector ) {
            setValues( ( Vector )arg );
            setFormData();
            setTableEditors();
            if(  (( BaseWindowObservable )o).getFunctionType() == CoeusGuiConstants.ADD_MODE ) {
                int lastRow = tblModule.getRowCount() - 1;
                if( lastRow != -1 ) {
                    tblModule.setRowSelectionInterval(lastRow, lastRow );
                }
            }else if( selRow != -1 && tblModule.getRowCount() > selRow ){
                tblModule.setRowSelectionInterval(selRow, selRow );
            }
        //Added for case#2420 - Upload of files on Edit Module Details Window - start
        }else if(arg instanceof HashMap){
            int requiredRow;            
            if((( BaseWindowObservable )o).getFunctionType() == CoeusGuiConstants.ADD_MODE) {
                requiredRow = tblModule.getRowCount() - 1;
            }else{
                requiredRow = selRow;
            }
            String module = (String) tblModule.getValueAt(requiredRow, 2);
            HashMap hmFileData = (HashMap)arg;
            ProposalNarrativeFormBean proposalNarrativeFormBean =
                    (ProposalNarrativeFormBean)hmModules.get(module);
            proposalNarrativeFormBean.setHasPDFModuleNumber(true);
            proposalNarrativeFormBean.setFileName(hmFileData.get("fileName").toString());
             
            tblModule.getModel().setValueAt(new Boolean(true), requiredRow, 1);
             // Case# 3855:Added to set File extension for the 6th column which is not displayable to the user.- Start
//             tblModule.getModel().setValueAt(UserUtils.getFileExtension(proposalNarrativeFormBean.getFileName()), requiredRow, 6);
            // Case# 3855:                                                                               - End
            
            tblModule.setRowSelectionInterval(requiredRow, requiredRow);            
            // Added for Proposal Hierarchy Enhancement Case# 3183 - Start
            //To set the last updated time stamp and user in txtLastDocUpdateUser 
            ProposalNarrativePDFSourceBean propNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean)hmFileData.get("proposalNarrativePDFSourceBean");
            if(propNarrativePDFSourceBean!=null){
                //4007:Icon based on mime type
                tblModule.getModel().setValueAt(propNarrativePDFSourceBean, requiredRow, 6);
                //4007 End
                proposalNarrativeFormBean.setPropNarrativePDFSourceBean(propNarrativePDFSourceBean);
                if(propNarrativePDFSourceBean.getUpdateTimestamp()!=null){
                    txtLastDocUpdateUser.setText(propNarrativePDFSourceBean.getUpdateUserName() + " at "+
                        CoeusDateFormat.format(propNarrativePDFSourceBean.getUpdateTimestamp().toString()));
                } else {
                    txtLastDocUpdateUser.setText("");
                }
                // Case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads- Start
            if( propNarrativePDFSourceBean.getFileName() != null){
                txtFileName.setText(propNarrativePDFSourceBean.getFileName());
            } else{
                txtFileName.setText("");
            }
            // Case# 3670:Display File name on Narrative Screen like it is displayed on Person Detail Uploads- End
            }
            // Added for Proposal Hierarchy Enhancement Case# 3183 - End
        }                
        int noOfRows = tblModule.getRowCount();
        if(noOfRows > 0){
            viewPdfFiles.setEnabled(true);
        }else{
            viewPdfFiles.setEnabled(false);
        }                
        //Added for case#2420 - Upload of files on Edit Module Details Window - end
    }    
    
    /** Getter for property modifiable.
     * @return Value of property modifiable.
     *
     */
    public boolean isModifiable() {
        return modifiable;
    }
    
    /** Setter for property modifiable.
     * @param modifiable New value of property modifiable.
     *
     */
    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }
    
    ////Added for the Coeus Enhancemnt case:#1767
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
    
    /**
     * Getter for property parent.
     * @return Value of property parent.
     */
    public boolean isParent() {
        return parent;
    }
    
    /**
     * Setter for property parent.
     * @param parent New value of property parent.
     */
    public void setParent(boolean parent) {
        this.parent = parent;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblContactName;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblLastDocument;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblPhoneNumber;
    private javax.swing.JLabel lblUpdatedBy;
    private javax.swing.JPanel pnlContact;
    private javax.swing.JPanel pnlContactDetails;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlModuleUsers;
    private javax.swing.JPanel pnlUserTimeStamp;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JScrollPane scrPnModule;
    private javax.swing.JScrollPane scrPnUsers;
    private javax.swing.JTable tblModule;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextArea txtArComments;
    private edu.mit.coeus.utils.CoeusTextField txtFileName;
    private edu.mit.coeus.utils.CoeusTextField txtFldContactName;
    private edu.mit.coeus.utils.CoeusTextField txtFldEmail;
    private edu.mit.coeus.utils.CoeusTextField txtFldPhoneNumber;
    private edu.mit.coeus.utils.CoeusTextField txtLastDocUpdateUser;
    private edu.mit.coeus.utils.CoeusTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables

     // Case# 3855: Replaced old ModuleTableRenderrer and ModuleTableEditor with the New classes.- Start
     // Reason : Since column 0 is always representing JBUtton, instead of creating a new JButton component,
    // extend the renderrer with JButton so that 0th column will acts as a JButton component.
    /** Represents the Table renderrer adeed for 0th and 1st column */
    class ModuleTableRenderer extends JButton implements TableCellRenderer {
        
        /** Creates a new instance of ModuleTableRenderer */
        public ModuleTableRenderer() {
            this.setHorizontalAlignment(JLabel.CENTER);
            setOpaque(false);  // so JLabel background is painted
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //Modified with case 4007:Icon based on mime type
//            String mimeType = (String) ((Vector)moduleTableData.get(row)).get(6);
            Object data = ((Vector)moduleTableData.get(row)).get(6);
            if(data instanceof CoeusAttachmentBean ){
                CoeusAttachmentBean attachmentBean = (CoeusAttachmentBean)data;
                if(attachmentBean != null) {
                    CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
                    this.setIcon(docUtils.getAttachmentIcon(attachmentBean));
                } else {
                    return new JLabel();
                }
            }
            return this;
        }
    }

    class ModuleTableEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        
        private JButton btnAttachment;
        
        /** Creates a new instance of ButtonEditor */
        public ModuleTableEditor() {
            btnAttachment = new JButton();
            btnAttachment.setOpaque(false);
            btnAttachment.setContentAreaFilled(false);
            btnAttachment.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            try{
                if( source.equals(btnAttachment) ){
                    if(btnAttachment.getIcon() !=null)
                    viewPdfDocument();
                }
                this.fireEditingStopped();
            }catch (Exception exception){
                exception.printStackTrace();
                //Added for case #1860 start 5
                if(!( exception.getMessage().equals(
                        coeusMessageResources.parseMessageKey(
                        "protoDetFrm_exceptionCode.1130")) )){
                    CoeusOptionPane.showInfoDialog(exception.getMessage());
                }
                //Added for case #1860 end 5
            }
        }
        
        public Object getCellEditorValue() {
            return "";
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            //4007:Modified with case 4007:Icon based on attachment Type
//            String fileExtension = (String) ((Vector)moduleTableData.get(row)).get(6);
//            if(fileExtension !=null)
//               btnAttachment.setIcon(UserUtils.getAttachmentIcon(fileExtension));   
            CoeusAttachmentBean attachmentBean = (CoeusAttachmentBean)((Vector)moduleTableData.get(row)).get(6);
            if(attachmentBean != null) {
                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
                btnAttachment.setIcon(docUtils.getAttachmentIcon(attachmentBean));
            }else{
                return new JLabel();
            }
            return btnAttachment;
        }
    }

//    class ModuleTableRenderer extends DefaultTableCellRenderer {
//        ImageIcon wordIcon=null, pdfIcon=null;
//        JButton btnWord, btnPdf;
//        public ModuleTableRenderer(){
//            btnWord = new JButton();
//            btnPdf = new JButton();
//            wordIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON));
//            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
////            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
//            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
//        }
//        public Component getTableCellRendererComponent(JTable table,
//            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            boolean showIcon=false;    
//            if( value != null ) {
//                showIcon = ( (Boolean) value ).booleanValue();
//            }
//            if( showIcon ) {
//                if( column == 0) {
//                    // if column in question is word icon column
//                    btnWord.setIcon( wordIcon );
//                    return btnWord;
//                }else if( column == 1 ) {
//                    //column is pdf icon column
//                    btnPdf.setIcon( pdfIcon );
//                    return btnPdf;
//                }
//            }else{
//                btnWord.setIcon( null );
//                btnPdf.setIcon( null );
//            }
//            return this;
//        }
//            
//    }
   
//    class ModuleTableEditor extends DefaultCellEditor implements ActionListener {
//        ImageIcon wordIcon = null, pdfIcon = null;
//        private JButton btnWord, btnPdf;
//
//        ModuleTableEditor(){
//            super(new JComboBox());
//           wordIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON));
//            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
////            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
//            //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
//
//            btnWord = new JButton(wordIcon);
//            btnWord.addActionListener(this);
//            btnPdf = new JButton(pdfIcon);
//            btnPdf.addActionListener(this);
//        }
//        
//        public java.awt.Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
//            boolean showIcon=false;
//            if( value != null ) {
//                showIcon = ( (Boolean) value ).booleanValue();
//            }
//             
//            if( showIcon ) {
//                if( column == 0) {
//                    // if column in question is word icon column
//                    btnWord.setIcon( wordIcon );
//                    return btnWord;
//                }else if( column == 1 ) {
//                    //column is pdf icon column
//                    btnPdf.setIcon( pdfIcon );
//
//                    return btnPdf;
//                }
//            }else{
//                btnWord.setIcon( null );
//                btnPdf.setIcon( null );
//            }
//            return null;
//        }
//        
//        public boolean stopCellEditing(){
//            cancelCellEditing();
//            return super.stopCellEditing();
//        }
//        
//        public void actionPerformed(ActionEvent actionEvent) {
//            Object source = actionEvent.getSource();
//            try{
//                if( source.equals(btnWord) ){
//                    viewWordDocument();
//                }else if( source.equals(btnPdf) ){
//                    viewPdfDocument();
//                }
//                stopCellEditing();
//            }catch (Exception exception){
//                exception.printStackTrace();
//                //Added for case #1860 start 5
//                if(!( exception.getMessage().equals(
//                coeusMessageResources.parseMessageKey(
//                "protoDetFrm_exceptionCode.1130")) )){
//                    CoeusOptionPane.showInfoDialog(exception.getMessage());
//                }
//                //Added for case #1860 end 5
//            }
//        }        
//    }    
   // Case# 3855   ---  end:                                                                    - End
  //Added for case#2420 - Upload of files on Edit Module Details Window - start  
  /**
   * This method saves the pdf document to database table OSP$NARRATIVE_PDF in BLOB format
   * @param proposalNarrativePDFSourceBean ProposalNarrativePDFSourceBean   
   * @return successFlag boolean Indicating whether the the save was success or not 
   * @exception Exception
   */
  private boolean uploadPDF(ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean) throws Exception {    
      boolean successFlag = true;
      RequesterBean request = new RequesterBean();         
      request.setFunctionType(UPDATE_NARRATIVE_PDF);      
      request.setDataObject(proposalNarrativePDFSourceBean);
      request.setId(CoeusGuiConstants.getMDIForm().getUnitNumber());
      AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, request);
      comm.send();
      ResponderBean response = comm.getResponse();  
      if(response.getId().equals("1")){
            successFlag = true;    
            successFlag = true;            
      }else if(response.getId().equals("-1")){      
            serverMsg = coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6617") +" " 
                        +proposalNarrativePDFSourceBean.getProposalNumber() +" "
                        +coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6618");                      
            successFlag = false;
      }else{            
            serverMsg = coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6619");
            successFlag = false;          
      }
      return successFlag;            
  }   
  
  /**
   * This method gets the narrative type description for a given narrative type code
   * @param narrativeTypeCode int narrative type code
   * @return description String narrative type description
   */
  private String getNarrativeDescription(int narrativeTypeCode){
      String description = "";      
      for(int index=0; index < narrativeTypes.size(); index++){
          ProposalNarrativeTypeBean narrativeTypeBean = (ProposalNarrativeTypeBean)narrativeTypes.get(index);
          if(narrativeTypeCode == narrativeTypeBean.getNarrativeTypeCode()){
              description = narrativeTypeBean.getDescription();
              break;
          }
      }
      return description;
  }
  //Added for case#2420 - Upload of files on Edit Module Details Window - end
  // COEUSDEV-308: Application not checking module level rights for a user in Narrative - Start
  /**
   * This method checks if the logged in user can edit a narrative module.
   */
  private boolean canUserEditModule(String moduleNumber){
      boolean canEdit = false;
      
      canEdit = hasOSPDeptRightToModify;
      if(!canEdit){
          canEdit = isUserHasModifyRight(moduleNumber) && hasPropRightToModify;
      }
      return canEdit;
  }
  // COEUSDEV-308: Application not checking module level rights for a user in Narrative - End
}
