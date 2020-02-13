/*
 * ProposalPersonForm.java
 *
 * Created on May 22, 2003, 9:21 AM
 */

/* PMD check performed, and commented unused imports and variables on 19-Aug-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.departmental.bean.DepartmentBioPDFPersonFormBean;
import edu.mit.coeus.departmental.bean.DepartmentBioPersonFormBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.net.URL;
import java.applet.AppletContext;
import java.util.*;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.query.Equals;
import edu.vanderbilt.coeus.gui.CustomTableCellRenderer;
import edu.vanderbilt.coeus.gui.PersonTableCellRenderer;

/**
 *
 * @author  Raghunath
 */
public class ProposalPersonnelForm extends CoeusInternalFrame implements TypeConstants, ActionListener, ListSelectionListener {
    
    private String referenceId;
    private char functionType;
    
    private Vector proposalPersonData;
    
    private Hashtable htPersonData;
    private Hashtable htPersonBiographyData;
    //private Hashtable htPerBioData;
    
    private Vector vecDeletedBiographyData;
    
    private Vector vecProposalPerson;
    private Vector vecPerson;
    //Commented unused private variable as per pmd check
    //private Vector vecBiographyForPerson;
    private CoeusFileChooser fileChooser;
    private static final String WORD_FORMAT = "doc";
    //Commented unused private variable as per pmd check
    //private static final String PDF_FORMAT = "pdf";
    private static final char SYNC = 'E';
    private String proposalId;
    private int previousSelRow = -1;
   // private int selRow=-1;
    
    //Case:#4210 - Proposal Personnel issues: sync biosketch - Please select a document type loop - Start 
    private int personClicked = 0;
    //Case:#4210 - End
    //hodls the person list slection instance
    private ListSelectionModel personSelectionModel;
    private ListSelectionModel bioSelectionModel;
    
    //Modified for case 3685 - Remove Word icons - start
    private CoeusMenuItem biographyAdd, biographyDelete,personDetail,personDegreeDetail,
    viewPdfFiles,  uploadPdfFiles,
    //Case #1777 Start 1
    mnItmMoveUp,mnItmMoveDown;
    //Modified for case 3685 - Remove Word icons - end
    //Commented for case #2032 , by tarique
    //  private boolean savePerson = false;
    //Case #1777 End 1
    
    //            biographyEditWordSource, biographyViewPDF,
    //            wordFileSaveToDatabase, wordFileSaveToDisk,
    //            pdfFileSaveToDatabase, pdfFileSaveToDisk;
    
    //Modified for case 3685 - Remove Word icons - start
    private CoeusToolBarButton btnAddModule, btnDeleteModule,
    btnViewPdfFile, btnDisplayPersonDetails,btnDisplayDegreeDetails,
    btnCloseBiography, btnSaveBiography,
    //Case #1777 Start 2
    btnMoveUp,btnMoveDown;
    //Case #1777 End 2
    //Modified for case 3685 - Remove Word icons - end    
    private CoeusMessageResources coeusMessageResources;
    private CoeusAppletMDIForm mdiForm;
    private final String SEPERATOR="seperator";
    private boolean saveRequired;
    
    private static final char GET_PROPOSAL_PERSON_BIOGRAPHY_DATA = 'B';
    private static final char SAVE_PROPOSAL_PERSON_BIOGRAPHY_DETAILS = 'w';
    private static final char VIEW_PDF = 'A';
    private static final char VIEW_WORD = 'B';
    private static final String CONNECTION_STRING =
    CoeusGuiConstants.CONNECTION_URL +"/ProposalMiscellaniesServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final char GET_PERSON_DOC_CODE = 'z';
    private boolean typeComboPopulated = false;
    
    //private boolean userCanMaintainUnit;
    //Commented for pmd check, unused private field 
    //private boolean userCanMaintainSelectedPerson ;
    
    
    private int canModifyProposal;
    private static final char PROPOSAL_PERSON_MODULE = 'P';
    //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
    private static final char GET_PROP_PERSONNEL_RIGHTS = 'f';
    private static final char SEND_NOTIFICATION = 'd';
    private boolean canModifyAnyProp = false;
    private boolean canModifyProp = false;
    private boolean canAlterProp = false;
    private boolean hasPropPersonnel = true;
    //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
    private Integer sortId;
    //Added for the Coeus Enhancemnt case:#1767
    private int proposalStatusCode;
    //Added for bug id 1856 step 1 : start
    private boolean alterProposalRight;
    
    private Vector vecDocumentType;
    private boolean instanceCreated;
    //bug id step 1 : end
    //Code added for case#3183 - Proposal Hierarchy enhancement - start
    private boolean isEnabled = true;
    private boolean parentProposal;
    private HashMap hmPropHierLink;
    private boolean hierarchy;
    //Added for Case#2793 - Coeus 4.NOW Person Maintainer - Uploading documents  - Start
    //Commented for pmd check, unused private field 
    //private int BIOSKETCH_TYPE = 1;
    
    private static final String PROP_MAINTENANCE_SERVLET = "/ProposalMaintenanceServlet";
    //End - Case#2793
    //Code added for case#3183 - Proposal Hierarchy enhancement - end

    //JIRA COEUSDEV-548 - START - 1
    private String unitNumber;
    //JIRA COEUSDEV-548 - END - 1
    //JIRA COEUSQA 1540 - START
    //TextValidator validator = new TextValidator(TextValidator.ALPHA + TextValidator.NUMERIC + TextValidator.PERIOD + TextValidator.UNDERSCORE + TextValidator.HYPHEN);
    //JIRA COEUSQA 1540 - END
    /**
     *  Constructor which instantiates the BiographyBaseWindow
     *  @param title a String sets the title to the JInternal Frame
     *  @mdiForm a Reference of CoeusAppletMDIForm
     */
    public ProposalPersonnelForm(String title, CoeusAppletMDIForm mdiForm) {
        
        super(title, mdiForm);
        this.mdiForm = mdiForm;
        this.vecDeletedBiographyData = new Vector();
    }
    
    /**
     * Method which displays the Biographical Window
     */
    public void showWindow(){
        
        try{
            this.referenceId = proposalId;
            mdiForm.putFrame(CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE, referenceId, functionType, this);
            showPersonBiographyWindow();
            formatComponents();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void showPersonBiographyWindow(){
        
        try{
            initComponents();
            tblProposalPerson.setModel(new ProposalPersonTableModel());
            tblPersonBiography.setModel(new PersonnelBiographyTableModel());
            
            setLabelValues();
            postInitComponents();
            setEditors();
            this.addVetoableChangeListener(new VetoableChangeListener(){
                public void vetoableChange(PropertyChangeEvent pce)
                throws PropertyVetoException {
                    if (pce.getPropertyName().equals(
                    JInternalFrame.IS_CLOSED_PROPERTY) ) {
                        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                        if( changed ) {
                            try {
                                //modified for the bug fix:1712 start step:1
                                if(tblPersonBiography.isEditing() && tblPersonBiography.getRowCount() > 0){
                                    //end Bugfix:1712 step:1
                                    tblPersonBiography.getCellEditor().stopCellEditing();
                                }
                                performClose();
                            } catch ( Exception e) {
                                //e.printStackTrace();
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
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            if( functionType != DISPLAY_MODE ) {
                synchorizeData();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void performSave() throws Exception{
        int rowCount = tblPersonBiography.getRowCount();
        if(rowCount > 0 && tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        /** Call this if the empty document type code are there and there is no data
         * change then validations won't fire. In that case this validation will be
         *used fire the messages
         */
        if(performPreValidate()){
            //Case #1777 Start 14
            if ( isSaveRequired()) {
                //Modified for case #2032 , by tarique start
                //if ( isSaveRequired() || savePerson ) {
                //commented for case #2032, by tarique start
                //savePerson =false;
                //commented for case #2032, by tarique end
                //Case #1777 End 14
                setBiographyInfo();
                
            }
        }
    }
    
    // Helpe method which closes the BiographyBaseWindow
    private void performClose() throws Exception{
        
        String msg = coeusMessageResources.parseMessageKey(
        "saveConfirmCode.1002");
        JInternalFrame frame = null;
        //Case #1777 Start 15
        //if ( isSaveRequired()) {
        // Bug Fix 1872
        //commented for case #2032 , by tarique start
        //if (( functionType!= TypeConstants.DISPLAY_MODE)&& (isSaveRequired() || savePerson )) {
        //Modified for case #2032 , by tarique start
        //Modified for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
//        if (( functionType!= TypeConstants.DISPLAY_MODE)&& (isSaveRequired())){ 
         if (isSaveRequired()){
        //Modified for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End            
            //Modified for case #2032 , by tarique end
            //commented for case #2032 , by tarique start
            // savePerson =false;
            //commented for case #2032 , by tarique end
            //Case #1777 End 15
            
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    setBiographyInfo();
                    mdiForm.removeFrame(
                    CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,
                    referenceId );
                    if(mdiForm.getFrame(
                    CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,
                    referenceId) == null){
                        frame = mdiForm.getFrame(
                        CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, referenceId);
                        if(frame != null){
                            frame.setSelected(true);
                            frame.setVisible(true);
                        }
                    }
                    this.dispose();
                    break;
                case(JOptionPane.NO_OPTION):
                    
                    saveRequired= false;
                    //vecBiographyData = null;                //Check This
                    mdiForm.removeFrame(
                    CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,
                    referenceId );
                    if(mdiForm.getFrame(
                    CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,
                    referenceId) == null){
                        frame = mdiForm.getFrame(
                        CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, referenceId);
                        if(frame != null){
                            frame.setSelected(true);
                            frame.setVisible(true);
                        }
                    }
                    this.dispose();
                    break;
                case(JOptionPane.CANCEL_OPTION):
                case ( JOptionPane.CLOSED_OPTION ):
                    throw new PropertyVetoException(
                    coeusMessageResources.parseMessageKey(
                    "protoDetFrm_exceptionCode.1130"),null);
                    
            }
        }else{
            mdiForm.removeFrame(
            CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,
            referenceId );
            if(mdiForm.getFrame(
            CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE,
            referenceId) == null){
                frame = mdiForm.getFrame(
                CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE, referenceId);
                if(frame != null){
                    frame.setSelected(true);
                    frame.setVisible(true);
                }
            }
            this.dispose();
        }
    }
    /*
    private boolean validateData() throws Exception{
     
        boolean valid = true;
     
        if(tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        int rowCount = tblPersonBiography.getRowCount();
        if(rowCount >= 0){
     
            for(int inInd=0; inInd < rowCount ;inInd++){
     
                String desc = (String)((DefaultTableModel)
                                            tblPersonBiography.getModel()).
                                                    getValueAt(inInd,3);
                if((desc == null) || (desc.trim().length() <= 0)){
                    valid=false;
                    errorMessage("Enter the Description");
                    tblPersonBiography.requestFocus();
                    break;
                }
            }
            if(!valid){
                return false;
            }
        }
        return true;
    }*/
    
    /** Perform pre-validation even if the save required is false.
     *Check only for the document type code which would be null
     */
    private boolean performPreValidate() throws Exception{
        
        boolean valid = true;
        if( tblPersonBiography.isEditing() ){
            //modified for the bug fix:1712 start step:2
            if(tblPersonBiography.getCellEditor() != null && tblPersonBiography.getRowCount() > 0){
                //end step:2
                tblPersonBiography.getCellEditor().stopCellEditing();
            }
        }
        if(tblProposalPerson.getRowCount()>0){
            int perRowCount = tblProposalPerson.getRowCount();
            String selPersonId="";
            String selPersonName = "";
            Vector personBio = null;
            ProposalBiographyFormBean propBiographyFormBean = null;
            
            for(int perRow = 0 ; perRow < perRowCount ; perRow++ ){
                
                selPersonId = (String)tblProposalPerson.getValueAt(perRow,2);
                selPersonName = (String)tblProposalPerson.getValueAt(perRow,0);
                
                if(selPersonId != null && selPersonId.trim().length()>0){
                    
                    personBio = (Vector)htPersonBiographyData.get(selPersonId);
                    
                    if( (personBio != null) && (personBio.size() > 0) ){
                        
                        int size = personBio.size();
                        for( int index = 0; index < size; index++ ){
                            
                            propBiographyFormBean = (ProposalBiographyFormBean)personBio.elementAt(index);
                            int documentTypeCode = propBiographyFormBean.getDocumentTypeCode();
                            if(documentTypeCode == 0){// Enhancement - validate the document type
                                valid=false;
                                CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2000"));
                                tblProposalPerson.setRowSelectionInterval(perRow,perRow);
                                setRequestFocusInThread(index, 5);
                                return false;
                            }
                        }
                    }
                }
            }
            if(!valid){
                return false;
            }
        }else{
            return true;
        }
        return true;
        
    }
    
    private boolean validateData() throws Exception{
        
        boolean valid = true;
        if( tblPersonBiography.isEditing() ){
            //modified for the bug fix:1712 start step:2
            if(tblPersonBiography.getCellEditor() != null && tblPersonBiography.getRowCount() > 0){
                //end step:2
                tblPersonBiography.getCellEditor().stopCellEditing();
            }
        }
        if(tblProposalPerson.getRowCount()>0){
            
            int perRowCount = tblProposalPerson.getRowCount();
            String selPersonId="";
            String selPersonName = "";
            Vector personBio = null;
            ProposalBiographyFormBean propBiographyFormBean = null;
            
            for(int perRow = 0 ; perRow < perRowCount ; perRow++ ){
                
                selPersonId = (String)tblProposalPerson.getValueAt(perRow,2);
                selPersonName = (String)tblProposalPerson.getValueAt(perRow,0);
                
                if(selPersonId != null && selPersonId.trim().length()>0){
                    
                    personBio = (Vector)htPersonBiographyData.get(selPersonId);
                    
                    if( (personBio != null) && (personBio.size() > 0) ){
                        
                        int size = personBio.size();
                        for( int index = 0; index < size; index++ ){
                            
                            propBiographyFormBean = (ProposalBiographyFormBean)personBio.elementAt(index);
                            String description = propBiographyFormBean.getDescription();
                            int documentTypeCode = propBiographyFormBean.getDocumentTypeCode();
                            //                            int documentTypeCode = propBiographyFormBean.getDocumentTypeCode();
                            if( description == null || description.length() <= 0){
                                
                                valid=false;
                                //modified for the bug fix:1712 to show consistent info dialog message start
                                CoeusOptionPane.showInfoDialog("Enter the Description");
                                tblProposalPerson.setRowSelectionInterval(perRow,perRow);
                                setRequestFocusInThread(index, 3);
                                //end bug:1712
                                break;
                                 //JIRA COEUSQA 1540 - START
                            /*} else if (!validator.validate(description)) {
                                valid = false;
                                CoeusOptionPane.showErrorDialog("File Description " + coeusMessageResources.parseMessageKey("text_validation_exceptionCode.1000"));
                                setRequestFocusInThread(index, 3);*/
                                //JIRA COEUSQA 1540 - END
                            }else if(documentTypeCode == 0){// Enhacement to check the document type
                                valid=false;
                                CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2000"));
                                tblProposalPerson.setRowSelectionInterval(perRow,perRow);
                                setRequestFocusInThread(index, 5);
                                return valid;
                            }else if(isDuplicateDocumentType(personBio)){// Enhancement - to check duplicate entry
                                valid=false;
                                CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2001"));
                                tblProposalPerson.setRowSelectionInterval(perRow,perRow);
                                return valid;
                            }
                        }
                    }
                }
            }
            if(!valid){
                return false;
            }
        }else{
                /* if atleast one investigator is not specified then show error msg
                   and return validity false */
            //log(coeusMessageResources.parseMessageKey(
            //"protoInvFrm_exceptionCode.1065"));
            return true;
        }
        return true;
    }
    
    /** It is an enhancement. Check for the duplicate document type code
     *@param containing the bio info for a person
     *@returns is boolean if it is duplicate
     */
    private boolean isDuplicateDocumentType(Vector personBio){
        boolean duplicate = false;
        CoeusVector cvData = new CoeusVector();
        if(personBio!= null && personBio.size() > 0){
            for(int index = 0; index < personBio.size(); index++){
                ProposalBiographyFormBean bean = (ProposalBiographyFormBean)personBio.get(index);
                ComboBoxBean codeBaseBean = new ComboBoxBean();
                
                codeBaseBean.setCode(String.valueOf(bean.getDocumentTypeCode()));
                codeBaseBean.setDescription(bean.getDocumentTypeDescription());
                cvData.addElement(codeBaseBean);
            }
            
            if(cvData!= null && cvData.size() > 0){
                for(int ind = 0; ind < cvData.size(); ind++){
                    ComboBoxBean comBean = (ComboBoxBean)cvData.get(ind);
                    Equals eqData = new Equals("code",comBean.getCode());
                    CoeusVector cvValueData = cvData.filter(eqData);
                    if(cvValueData!= null && cvValueData.size() > 1){
                        duplicate = true;
                        break;
                    }
                }
            }
        }
        return duplicate;
    }
    
    
    private void setBiographyInfo() throws Exception{
        //Commented for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
       //boolean alterProposalPerson = false;
        //Commented for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
        if(functionType == 'D'){            
            //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
            if(proposalStatusCode == 2 || proposalStatusCode == 4){
                 saveProposalPersonnel();
            }
            //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
        }else {
            if(!validateData()){
                //added for the bugFix:1712 start step:3
                throw new Exception(coeusMessageResources.parseMessageKey(
                "protoDetFrm_exceptionCode.1130"),null);
                //end step:3
            }else{
                //Modified for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                saveProposalPersonnel();
//                Vector vecBioDeletedData = getProposalPersonBioData();
//                Vector vecDataToServer = new Vector();
//                // if(vecBioDeletedData!=null && vecBioDeletedData.size() > 0){
//                vecDataToServer.addElement(vecBioDeletedData);
//                vecDataToServer.addElement(htPersonBiographyData);
//                vecDataToServer.addElement(proposalId);
//                //case #1777 start 12
//                vecDataToServer.addElement(htPersonData);
//                //case #1777 End 12
//                //Added for the Coeus Enhancemnt case:#1767
//                vecDataToServer.addElement(new Integer(proposalStatusCode));
//                vecDataToServer.addElement(mdiForm.getUnitNumber());
//                if(functionType == MODIFY_MODE && proposalStatusCode == 2 || proposalStatusCode == 4)
//                    alterProposalPerson = true;
//                vecDataToServer.addElement(new Boolean(alterProposalPerson));
//                //End for the Coeus Enhancemnt case:#1767
//                String connectTo = CoeusGuiConstants.CONNECTION_URL +PROP_MAINTENANCE_SERVLET;
//                RequesterBean request = new RequesterBean();
//                request.setFunctionType(SAVE_PROPOSAL_PERSON_BIOGRAPHY_DETAILS);
//                request.setDataObject(vecDataToServer);
//                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
//                comm.send();
//                ResponderBean response = comm.getResponse();
//                if (response.isSuccessfulResponse()) {
//                    vecDataToServer = (Vector)response.getDataObjects();
//                    proposalPersonData = vecDataToServer;
//                    // constructPersonHashtable(proposalPersonData);
//                    //Case #1777 Start 16
//                    htPersonData = constructPersonHashtable(proposalPersonData);
//                    //Case#1777 End 16
//                    setFormData();
//                    saveRequired = false;
//                    //Bug Fix for the nullpointer exception
//                    //Geo on 20-Jul-2005
//                    //clear the temp deleted vector once it got deleted from the database
//                    this.vecDeletedBiographyData.removeAllElements();
//                    //End fix
//                    //Vector dataFromServer = (Vector)response.getDataObject();
//                } else {
//                    CoeusOptionPane.showErrorDialog(response.getMessage());
//                    if (response.isCloseRequired()) {
//                        //mdiForm.removeFrame(
//                        //CoeusGuiConstants.PROTOCOL_FRAME_TITLE, referenceId);
//                        this.doDefaultCloseAction();
//                        return;
//                    }else {
//                        throw new Exception(response.getMessage());
//                        
//                    }
//                }
//                //  }
//                // saveRequired = false;
            //Modified for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
            }
        }
    }
    
    private void postInitComponents(){
        
        setFrameMenu( getPropPersonBiographyEditMenu() );
        setFrameToolBar( getPersonBiographyToolBarMenu() );
        setFrame( CoeusGuiConstants.PROP_PERSON_BIOGRAPHY_FRAME_TITLE );
        setFrameIcon( mdiForm.getCoeusIcon() );
        coeusMessageResources = CoeusMessageResources.getInstance();
        setDataVectors();
        htPersonData = constructPersonHashtable( proposalPersonData );
        //synchorizeData();
        setListeners();
        setFormData();
        
        //Case #1777 Start 3
        if(tblProposalPerson.getRowCount()<=0||tblProposalPerson.getRowCount()==1){
            btnMoveUp.setIcon(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_UP_ICON)));
            btnMoveUp.setEnabled(false);
            //Disable the menu item as well
            mnItmMoveUp.setEnabled(false);
            btnMoveDown.setIcon(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_DOWN_ICON)));
            btnMoveDown.setEnabled(false);
            //Disable the menu item as well
            mnItmMoveDown.setEnabled(false);
        }
        //Case #1777 End 3
        
        if(tblProposalPerson.getRowCount() > 0 ){
            tblProposalPerson.setRowSelectionInterval(0,0);
            tblProposalPerson.setColumnSelectionInterval(0,0);
            if(functionType != DISPLAY_MODE){
                btnDeleteModule.setEnabled(true);
            }
        }
        setEditors();
    }
    
    private void formatComponents(){
        //bug fix id 1744 done by shiji - step 1 : start
        int selBioRow = tblPersonBiography.getSelectedRow();
        boolean isWord=false,isPdf=false;
        if(selBioRow !=-1) {
            isWord = ((Boolean)tblPersonBiography.getValueAt(selBioRow,1)).booleanValue();
            isPdf = ((Boolean)tblPersonBiography.getValueAt(selBioRow,2)).booleanValue();
        }
        //bug fix id 1744 - step 1 : End
        boolean enabled =  ((functionType == DISPLAY_MODE) || (canModifyProposal == 0)) ? false : true;
        btnAddModule.setEnabled(enabled);
        btnDeleteModule.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - start
//        btnEditWordSource.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - end
        btnViewPdfFile.setEnabled(enabled);
        biographyAdd.setEnabled(enabled);
        biographyDelete.setEnabled(enabled);
        //Code added for case#2938 - Proposal Hierarchy enhancement - starts
        //Enable or disable buttons according to the selected persons present in child records
        //Commented for case 3685 - Remove Word icons - start
//        upLoadWordFiles.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - end
        uploadPdfFiles.setEnabled(enabled);
        //Code added for case#2938 - Proposal Hierarchy enhancement - ends
        //Case #1777 - Start 10
        if(!enabled){
            btnMoveUp.setIcon(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_UP_ICON)));
            btnMoveUp.setEnabled(enabled);
            // set for the menu item as well
            mnItmMoveUp.setEnabled(enabled);
            btnMoveDown.setIcon(new ImageIcon(
            getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_DOWN_ICON)));
            btnMoveDown.setEnabled(enabled);
            mnItmMoveDown.setEnabled(enabled);
        }
        //Caes #1777 -End 10
        
        //bug fix id 1744 done by shiji - step 2 : start
        if(selBioRow != -1) {
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(isWord);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(isPdf);
        }else {
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(enabled);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(enabled);
        }
        //bug fix id 1744 - step 2 : End
        //Commented for case 3685 - Remove Word icons - start
//        upLoadWordFiles.setEnabled(enabled);
        //Commented for case 3685 - Remove Word icons - end
        uploadPdfFiles.setEnabled(enabled);
        
        //bug fix id 1744 done by shiji - step 3 : start
        tblPersonBiography.setEnabled(true);
        //bug fix id 1744 - step 3 : End
        
        //Bug Fix: In display mode, not able to select
        //rows other than the 1st one. Start
        //tblProposalPerson.setEnabled(enabled);
        //Bug Fix: In display mode, not able to select
        //rowas other than the 1st one. End
        
        int rowCount = tblPersonBiography.getRowCount();
        // Added by chandra 12/03/2004. Bug id #547
        if( rowCount <= 0){
            btnDeleteModule.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(false);
            //Case 2469 start
            //            upLoadWordFiles.setEnabled(false);
            //            uploadPdfFiles.setEnabled(false);
            //Case 2469 End
        }
        //Code added for case#2938 - Proposal Hierarchy enhancement - starts
        //Enable or disable buttons according to the selected persons present in child records
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            btnAddModule.setEnabled(isEnabled);
            btnDeleteModule.setEnabled(isEnabled);
            //Commented for case 3685 - Remove Word icons - start
//            btnEditWordSource.setEnabled(isEnabled);
            //Commented for case 3685 - Remove Word icons - end
            btnViewPdfFile.setEnabled(isEnabled);
            biographyAdd.setEnabled(isEnabled);
            biographyDelete.setEnabled(isEnabled);
            //Commented for case 3685 - Remove Word icons - start
//            upLoadWordFiles.setEnabled(isEnabled);
            //Commented for case 3685 - Remove Word icons - end
            uploadPdfFiles.setEnabled(isEnabled);            
        }
        //Code added for case#2938 - Proposal Hierarchy enhancement - ends
        // End chandra - 12/03/2004. Bug id #547
        
        //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
             if(getPersonnelRight()){ 
                    if(tblPersonBiography.getRowCount()<=0){                        
                        viewPdfFiles.setEnabled(false);
                        btnViewPdfFile.setDisabledIcon(new ImageIcon(
                                getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
                        btnViewPdfFile.setEnabled(false);
                        uploadPdfFiles.setEnabled(false);
                    }else if(isEnabled){
                         uploadPdfFiles.setEnabled(true);
                         btnViewPdfFile.setEnabled(true);
                         //Commented/Added for case#2156 - start
//                         btnViewPdfFile.setDisabledIcon(new ImageIcon(
//                                getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON)));
                         btnViewPdfFile.setDisabledIcon(new ImageIcon(
                                getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON)));                         
                         //Commented/Added for case#2156 - end
                         viewPdfFiles.setEnabled(true);
                    }                    
                    
                    if(tblProposalPerson.getRowCount()<=0||tblProposalPerson.getRowCount()==1){
                        btnMoveUp.setIcon(new ImageIcon(
                                getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_UP_ICON)));
                        btnMoveUp.setEnabled(false);

                        mnItmMoveUp.setEnabled(false);
                        btnMoveDown.setIcon(new ImageIcon(
                                getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_MOVE_DOWN_ICON)));
                        btnMoveDown.setEnabled(false);

                        mnItmMoveDown.setEnabled(false);                        
                    }else{
                        mnItmMoveUp.setEnabled(true);
                        btnMoveUp.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_MOVE_UP_ICON)));
                        btnMoveUp.setEnabled(true);
                        
                        mnItmMoveDown.setEnabled(true);   
                        btnMoveDown.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_MOVE_DOWN_ICON)));
                        btnMoveDown.setEnabled(true);
                    }
                    btnSaveBiography.setEnabled(true);
                    btnSaveBiography.setIcon(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)));
            } else {
                 uploadPdfFiles.setEnabled(false); 
                 btnViewPdfFile.setDisabledIcon(new ImageIcon(
                                getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
                 hasPropPersonnel = false;
            }
        }
        //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End    
    }
    
    //Helper method which sets the ToolBar buttons
    
    
    private JToolBar getPersonBiographyToolBarMenu(){
        
        JToolBar personBiographyToolBar = new JToolBar();
        
        btnAddModule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null, "Add Module");
        
        btnAddModule.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DADD_ICON)));
        
        btnDeleteModule = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
        null, "Delete Module");
        
        btnDeleteModule.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DDELETE_ICON)));
        //Commented for case 3685 - Remove Word icons - start
//        btnEditWordSource = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON)),
//        null, "Upload Word File");
        //Commented for case 3685 - Remove Word icons - end
        //Commented/Added for case#2156 - start
//        btnViewPdfFile = new CoeusToolBarButton(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON)),
//        null, "Upload PDF File");
//        
//        btnViewPdfFile.setDisabledIcon(new ImageIcon(
//        getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
        btnViewPdfFile = new CoeusToolBarButton(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON)),null, "Upload Attachment");
        
        btnViewPdfFile.setDisabledIcon(
                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_ATTACHMENT_ICON)));        
        //Commented/Added for case#2156 - end
        
        btnDisplayPersonDetails = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        null, "Displays person details");
        
        btnDisplayDegreeDetails = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.QUALIFICATIONS_ICON)),
        null, "Displays educational qualification");
        
        btnCloseBiography = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        btnSaveBiography = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null, "Save");
        
        //Case #1777 Start 6
        btnMoveUp = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_MOVE_UP_ICON)),
        null, "Move Up");
        btnMoveDown = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_MOVE_DOWN_ICON)),
        null, "Move Down");
        btnMoveUp.addActionListener(this);
        btnMoveDown.addActionListener(this);
        //Case #1777 End 6
        
        
        
        
        btnAddModule.addActionListener(this);
        btnDeleteModule.addActionListener(this);
        //Commented for case 3685 - Remove Word icons - start
//        btnEditWordSource.addActionListener(this);
        //Commented for case 3685 - Remove Word icons - end
        btnViewPdfFile.addActionListener(this);
        btnCloseBiography.addActionListener(this);
        btnSaveBiography.addActionListener(this);
        btnDisplayDegreeDetails.addActionListener(this);
        btnDisplayPersonDetails.addActionListener(this);
        
        personBiographyToolBar.add(btnAddModule);
        personBiographyToolBar.add(btnDeleteModule);
        personBiographyToolBar.add(btnDisplayPersonDetails);
        personBiographyToolBar.add(btnDisplayDegreeDetails);
        personBiographyToolBar.addSeparator();
        //Case #1777 Start 7
        personBiographyToolBar.add(btnMoveUp);
        personBiographyToolBar.add(btnMoveDown);
        personBiographyToolBar.addSeparator();
        //Case #1777 End 7
        //Commented for case 3685 - Remove Word icons - start
//        personBiographyToolBar.add(btnEditWordSource);
        //Commented for case 3685 - Remove Word icons - end
        personBiographyToolBar.add(btnViewPdfFile);
        personBiographyToolBar.addSeparator();
        
        personBiographyToolBar.add(btnSaveBiography);
        personBiographyToolBar.add(btnCloseBiography);
        
        personBiographyToolBar.setFloatable(false);
        return personBiographyToolBar;
    }
    
    private CoeusMenu getPropPersonBiographyEditMenu(){
        
        CoeusMenu menuEditBiography = null;
        Vector fileChildren = new Vector();
        
        biographyAdd = new CoeusMenuItem("Add Module", null, true, true);
        biographyAdd.setMnemonic('A');
        biographyAdd.addActionListener(this);
        
        biographyDelete = new CoeusMenuItem("Delete Module", null, true, true);
        biographyDelete.setMnemonic('D');
        biographyDelete.addActionListener(this);
        
        personDetail = new CoeusMenuItem("Person Detail", null, true, true);
        personDetail.addActionListener(this);
        
        personDegreeDetail = new CoeusMenuItem("Degree Info",null,true,true);
        personDegreeDetail.setMnemonic('f');
        personDegreeDetail.addActionListener(this);
        Vector viewFileChildrean = new Vector();
        //Commented for case 3685 - Remove Word icons - start
//        viewWordFiles = new CoeusMenuItem("View Word Files",null,true,true);
//        viewWordFiles.setMnemonic('W');
//        viewWordFiles.addActionListener(this);
        //Commented for case 3685 - Remove Word icons - end
        //Commented/Added for case#2156 - start
//        viewPdfFiles = new CoeusMenuItem("View PDF Files",null,true,true);
        viewPdfFiles = new CoeusMenuItem("View Attachment",null,true,true);
        //Commented/Added for case#2156 - end
        viewPdfFiles.setMnemonic('V');
        viewPdfFiles.addActionListener(this);
        
        //Commented for case 3685 - Remove Word icons - start
//        viewFileChildrean.add(viewWordFiles);
        //Commented for case 3685 - Remove Word icons - end
        viewFileChildrean.add(viewPdfFiles);
        
        //Commented for case 3685 - Remove Word icons - start
//        CoeusMenu viewFile = new CoeusMenu("View Files",null,viewFileChildrean,true,true);
//        viewFile.setMnemonic('V');
        //Commented for case 3685 - Remove Word icons - end
        
        Vector uploadFileChildrean = new Vector();
        //Commented for case 3685 - Remove Word icons - start
//        upLoadWordFiles = new CoeusMenuItem("Upload Word Files",null,true,true);
//        upLoadWordFiles.setMnemonic('W');
//        upLoadWordFiles.addActionListener(this);
        //Commented for case 3685 - Remove Word icons - end
        //Commented/Added for case#2156 - start
//        uploadPdfFiles = new CoeusMenuItem("Upload PDF Files",null,true,true);
        uploadPdfFiles = new CoeusMenuItem("Upload Attachment",null,true,true);
        //Commented/Added for case#2156 - end
        uploadPdfFiles.setMnemonic('U');
        uploadPdfFiles.addActionListener(this);
        
        //Commented for case 3685 - Remove Word icons - start
//        uploadFileChildrean.add(upLoadWordFiles);
        //Commented for case 3685 - Remove Word icons - end
        uploadFileChildrean.add(uploadPdfFiles);
        
        //Commented for case 3685 - Remove Word icons - start
//        CoeusMenu upLoad = new CoeusMenu("Upload Files",null,uploadFileChildrean,true,true);
//        upLoad.setMnemonic('U');
        //Commented for case 3685 - Remove Word icons - end
        //Case #1777 Start 4
        mnItmMoveUp = new CoeusMenuItem("Move Up", null, true, true);
        mnItmMoveUp.setMnemonic('o');
        mnItmMoveUp.addActionListener(this);
        mnItmMoveDown = new CoeusMenuItem("Move Down", null, true, true);
        mnItmMoveDown.setMnemonic('w');
        mnItmMoveDown.addActionListener(this);
        
        //Case #1777 End 4
        fileChildren.add(biographyAdd);
        fileChildren.add(biographyDelete);
        fileChildren.add(SEPERATOR);
        fileChildren.add(personDetail);
        fileChildren.add(personDegreeDetail);
        fileChildren.add(SEPERATOR);
        fileChildren.add(viewPdfFiles);
        fileChildren.add(uploadPdfFiles);
        //Commented for case 3685 - Remove Word icons - start
//        fileChildren.add(viewFile);
//        fileChildren.add(upLoad);
        //Commented for case 3685 - Remove Word icons - end
        //Case #1777 Start 5
        fileChildren.add(SEPERATOR);
        fileChildren.add(mnItmMoveUp);
        fileChildren.add(mnItmMoveDown);
        //Case #1777 End 5
        
        menuEditBiography = new CoeusMenu("Edit", null, fileChildren, true, true);
        menuEditBiography.setMnemonic('E');
        return menuEditBiography;
    }
    
    private void setLabelValues(){
        
        String code = ProposalDetailAdminForm.SPONSOR_CODE;
        String desc = ProposalDetailAdminForm.SPONSOR_DESCRIPTION;
        String sponsorValue = "";
        if(desc != null){
            sponsorValue = code + " : " + desc;
        }
        lblSponsorValue.setText(sponsorValue);
        lblProposalValue.setText(proposalId);
    }

    private void setListeners(){
        
        personSelectionModel = tblProposalPerson.getSelectionModel();
        personSelectionModel.addListSelectionListener( this );
        personSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        
        bioSelectionModel = tblPersonBiography.getSelectionModel();
        bioSelectionModel.addListSelectionListener( this );
        bioSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        tblProposalPerson.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    try {
                        int selectedRow = tblProposalPerson.getSelectedRow();
                        String personId = (String)tblProposalPerson.getValueAt(selectedRow, 2);
                        String personName = (String)tblProposalPerson.getValueAt(selectedRow, 0);
                        //Case:#4210 - Proposal Personnel issues: sync biosketch - Please select a document type loop - Start 
                        //Assigning value 2 when row is double clicked
                        personClicked = 2;
                        //Case:#4210 - End
                        displayPersonDetails(personId, personName, MODIFY_MODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                //Case:#4210 - Proposal Personnel issues: sync biosketch - Please select a document type loop - Start 
                }else{
                     //Assigning value 0 when row is not double clicked
                     personClicked = 0;
                }
                //Case:#4210 - End
            }
        });
        tblPersonBiography.addKeyListener(new KeyAdapter(){
            int keyPressedBioRow;
            int keyReleasedBioRow;
            public void keyPressed(KeyEvent keyEvent){
                if(keyEvent.getKeyCode() ==  keyEvent.VK_DOWN || keyEvent.getKeyCode() ==  keyEvent.VK_ENTER){
                    int bioRow = tblPersonBiography.getSelectedRow();
                    keyPressedBioRow = bioRow;
                    int docTypeCode = 0;
                    //System.out.println("keyPressedBioRow is "+keyPressedBioRow);
                    //System.out.println("In Key Pressed selected row is "+bioRow);
                    if( bioRow != -1){
                        String desc = (String)tblPersonBiography.getValueAt(bioRow,3);
                        ComboBoxBean cmbBean = (ComboBoxBean)tblPersonBiography.getValueAt(bioRow,5);
                        if(cmbBean!= null){
                            docTypeCode = Integer.parseInt(cmbBean.getCode());
                        }
                        //System.out.println("desc is "+desc);
                        if(desc == null || desc.trim().length() <=0 ){
                            try{
                                CoeusOptionPane.showInfoDialog("Enter the Description");
                                tblPersonBiography.requestFocus();
                                tblPersonBiography.setRowSelectionInterval(bioRow,bioRow);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }else if(docTypeCode == 0){
                            CoeusOptionPane.showInfoDialog(
                                    coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2000"));
                            tblPersonBiography.requestFocus();
                            tblPersonBiography.setRowSelectionInterval(bioRow,bioRow);
                        }
                    }
                }
            }
            
            public void keyReleased(KeyEvent keyEvent){
                 // Modified for internal issue fix 135 Class Cast exception while clicking on down arrow start
               // if(keyEvent.getKeyCode() ==  keyEvent.VK_DOWN || keyEvent.getKeyCode() ==  keyEvent.VK_ENTER){
                boolean flag=false;
                if(keyEvent.getKeyCode() ==  keyEvent.VK_ENTER){
                // Modified for internal issue fix 135 Class Cast exception while clicking on down arrow end
                  
                    int bioRow = tblPersonBiography.getSelectedRow();
                    keyReleasedBioRow = bioRow;
                    //added for the bug fix:1712 start step:4 to validate on the click of the enter key
                    try{
                        boolean validRow = validateRowEntry();
                        if(!validRow){
                            return;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    //end bugfix:1712 step:4
                    //Modified for COEUSDEV-390 : PDF Issue in Proposal Persons - Start
                    //Not adding new row when proposal bio is in display mode.
//                    if(keyPressedBioRow == keyReleasedBioRow){
                    if(keyReleasedBioRow >= tblPersonBiography.getRowCount()-1){
                        if(keyReleasedBioRow == tblPersonBiography.getRowCount()-1){
                            tblPersonBiography.setRowSelectionInterval(keyReleasedBioRow,keyReleasedBioRow);
                        }else{
                            tblPersonBiography.setRowSelectionInterval(0,0);
                        }
                    } else if(keyPressedBioRow == keyReleasedBioRow && getFunctionType() != 'D' ){
                    //COEUSDEV-390 : End
                        String personNum = null;
                        if( tblProposalPerson.getRowCount() > 0 ){
                            int selRow = tblProposalPerson.getSelectedRow();
                            if(selRow != -1){
                                personNum =  (String)((DefaultTableModel)tblProposalPerson.getModel()).getValueAt(selRow, 2);
                            }
                        }
                        Vector newRowData = new Vector();
                        
                        TableColumn column = tblPersonBiography.getColumnModel().getColumn(0);
                        column.setHeaderRenderer(new EmptyHeaderRenderer());
                        column.setCellRenderer(new IconRenderer());
                        
                        column = tblPersonBiography.getColumnModel().getColumn(1);
                        //Commented for case 3685 - Remove Word icons - start
                        //  column.setCellRenderer(new ButtonRenderer());
                        //column.setCellEditor(new ButtonEditor(new JCheckBox()));
                        //Commented for case 3685 - Remove Word icons - end
                        
                        column = tblPersonBiography.getColumnModel().getColumn(2);
                        //Commented for case 3685 - Remove Word icons - start
                        // column.setCellRenderer(new ButtonRenderer());
                       // column.setCellEditor(new ButtonEditor(new JCheckBox()));
                        //Commented for case 3685 - Remove Word icons - end
                        
                        column = tblPersonBiography.getColumnModel().getColumn(3);
                        column.setCellRenderer(new ColumnValueRenderer());
                        column.setCellEditor(new ColumnValueEditor(200));
                        
                        column = tblPersonBiography.getColumnModel().getColumn(5);
                        column.setCellRenderer(new ColumnValueRenderer());
                        column.setCellEditor(new ColumnValueEditor(200));
                        
                        newRowData.addElement("");
                        newRowData.addElement(new Boolean(false));
                        newRowData.addElement(new Boolean(false));
                        newRowData.addElement("");
                        newRowData.addElement(personNum);
                        newRowData.addElement(new ComboBoxBean("",""));
                        //#case 3855 ---- start add new element to store file extension value
                        newRowData.addElement(null);
                        //#case 3855 ---- end
                        ProposalBiographyFormBean bioBean = new ProposalBiographyFormBean();
                        bioBean.setAcType(INSERT_RECORD);
                        bioBean.setProposalNumber(proposalId);
                        bioBean.setPersonId(personNum);
                        
                        //System.out.println("personNum is "+personNum);
                        //System.out.println("Bean is "+bioBean);
                        
                        if(htPersonBiographyData != null && htPersonBiographyData.elements() != null && htPersonBiographyData.size() > 0){
                            //System.out.println("personId is "+personNum);
                            if( ((Vector) htPersonBiographyData.get(personNum)) != null){
                                ((Vector) htPersonBiographyData.get(personNum)).addElement(bioBean);
                            }else{
                                htPersonBiographyData.put(personNum, new Vector());
                                ((Vector) htPersonBiographyData.get(personNum)).addElement(bioBean);
                            }
                        }else{
                            htPersonBiographyData = new Hashtable();
                            Vector vecBio = new Vector();
                            vecBio.addElement(bioBean);
                            htPersonBiographyData.put(personNum, vecBio );
                        }
                        
                        saveRequired = true;
                        
                        ((DefaultTableModel)tblPersonBiography.getModel()).addRow( newRowData );
                        ((DefaultTableModel)tblPersonBiography.getModel()).fireTableDataChanged();
                        
                        int lastRow = tblPersonBiography.getRowCount() - 1;
                   
                        if(lastRow >= 0){
                            btnDeleteModule.setEnabled(true);
                            tblPersonBiography.editCellAt(lastRow ,3);
                            tblPersonBiography.getEditorComponent().requestFocusInWindow();
                            tblPersonBiography.setRowSelectionInterval( lastRow, lastRow );
                            tblPersonBiography.scrollRectToVisible(tblPersonBiography.getCellRect(
                            lastRow ,0, true));
                        }
                    }
                }
                   if(keyEvent.getKeyCode() ==  keyEvent.VK_TAB){
                     int bioRow = tblPersonBiography.getSelectedRow();
                   setRequestFocusInThread(bioRow ,5);
                   flag=false;

               }
                      int bioRow = tblPersonBiography.getSelectedRow();
                      int biocol = tblPersonBiography.getSelectedColumn();
                      if(!flag){
                        setRequestFocusInThread(bioRow ,5);
                        
                      }
               
            }
        });
        bioSelectionModel = tblPersonBiography.getSelectionModel();
        bioSelectionModel.addListSelectionListener( this );
        bioSelectionModel.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        tblPersonBiography.setSelectionModel( bioSelectionModel );
    }
    
    private void displayPersonDetails(String personId, String personName, char MODIFY_MODE){
        
        if (personId == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1052"));
        }else {
            try{
                //Code modified for case#3246 - starts
                char funType = functionType;
                //Code modified for case#3246 - ends
                //Commented for pmd check, unused local variable begin 
                //String loginName = mdiForm.getUserName();
               
                /*
                PersonDetailForm personDetailForm = new PersonDetailForm(personName,loginName,
                                            functionType,PROPOSAL_PERSON_MODULE, canModifyProposal, proposalId ); */
                //Added for bug id 1856 - step 2 : start
                //Commented for COEUSDEV-149 : :Proposal Persons - Degree Details - Approval In Progress - Start
//                if(isAlterProposalRight()) {
//                    if(proposalStatusCode == 2 || proposalStatusCode == 4) {
//                        functionType = MODIFY_MODE;
//                    }
//                }
                //COEUSDEV-149 : END
                //Code modified for case#2938 - Proposal Hierarchy enhancement - starts
                //Opening person details in edit or display mode
                //according to the selected persons in child records
                if(!isEnabled && functionType == MODIFY_MODE){
                    functionType = DISPLAY_MODE;
                }
                //Code modified for case#2938 - Proposal Hierarchy enhancement - ends
                //bug id 1856 - step 2 : end
                PersonDetailForm personDetailForm = new PersonDetailForm(functionType,PROPOSAL_PERSON_MODULE);
                personDetailForm.setDisabled(!isEnabled);
                personDetailForm.setProposalId(proposalId);
                personDetailForm.setPersonId(personId);
                personDetailForm.setPersonName(personName);
                personDetailForm.setCanModifyProposal(canModifyProposal);
                personDetailForm.setTblPropPerson(tblProposalPerson);
                //Added for case #2311 start
                personDetailForm.setWindowInstance(this);
                //Added for case #2311 end
                personDetailForm.showPersonDetailForm();
                //Added by shiji for bug fix id : 1925 : start
                //Code modified for case#3246 - starts
                functionType = funType;
                //Code modified for case#3246 - ends
                setDataVectors();
                //COEUSDEV-150: 4.3.2 Premium - Proposal Personnel - document type 
                personClicked = 2;
                htPersonData = constructPersonHashtable( proposalPersonData );
                personClicked = 0;
                //COEUSDEV-150:End
                //bug id : 1925 : end
                
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    private void synchorizeData(){
        
        if(proposalPersonData != null && proposalPersonData.size() > 0){
            ProposalPersonFormBean personBean = null;
            //Commented for pmd check, unused local variable begin 
            //Vector perBioData = null;
            //Vector propPerBioData = null;
            Vector deptPerBioData = null;
            for(int index = 0; index < proposalPersonData.size(); index++){
                personBean = (ProposalPersonFormBean)proposalPersonData.elementAt(index);
                //Code commented for case#3183 - Proposal Hierarchy enhacement.
                //if(personBean != null){
                if(personBean != null && (!isHierarchy() || personBean.isEditable())){
                    String personId = personBean.getPersonId();
                    deptPerBioData = personBean.getPersonBiography();
                    if( !htPersonBiographyData.containsKey(personId)
                    && ( deptPerBioData != null && deptPerBioData.size() > 0) ){
                        
                        int selectedOption = CoeusOptionPane.showQuestionDialog(" Bio information exists for "+
                        personBean.getFullName()+". Do you want to Syncronize the info",
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                        if( selectedOption == JOptionPane.YES_OPTION ) {
                            Vector vecData = getSyncData(proposalId,personId);
                            Vector syncBioData = new Vector();
                            //Modified with coeusdev-139 : Allow multiple person attachments of same document type in Person Bio Module
                            ProposalBiographyFormBean syncBean;
                            DepartmentBioPersonFormBean deptBean;
                            DepartmentBioPDFPersonFormBean personPdfBean;
                            ProposalPersonBioPDFBean proposalPersonPDFBean;
//                            ProposalBiographyFormBean deptBean;
                            //int deptBioSize = deptPerBioData.size();
                            int deptBioSize = vecData.size();
                            for( int indx = 0; indx < deptBioSize ; indx++) {
                                deptBean = (DepartmentBioPersonFormBean)vecData.get(indx);
//                                deptBean = (ProposalBiographyFormBean)vecData.get(indx);
                                syncBean = new ProposalBiographyFormBean();
                                syncBean.setProposalNumber( proposalId );
                                syncBean.setAcType( INSERT_RECORD );
//                                syncBean.setBioNumber( deptBean.getBioNumber());
                                syncBean.setDescription( deptBean.getDescription() );
                                //Modified for Case#2793 - Coeus 4.NOW Person Maintainer - Uploading documents  - Start
                                //Modified to set 'Biosketch' Document Type During Biographical Sync
                                syncBean.setDocumentTypeCode(deptBean.getDocumentTypeCode());
//                                syncBean.setDocumentTypeCode(BIOSKETCH_TYPE);
//                                syncBean.setAcType(TypeConstants.UPDATE_RECORD);
                                //End - Case#2793
                                syncBean.setDocumentTypeDescription(deptBean.getDocumentTypeDescription());
                                syncBean.setPersonId( deptBean.getPersonId());
                                syncBean.setHasBioPDF(deptBean.isHasBioPDF());
                                syncBean.setHasBioSource(deptBean.isHasBioSource());
                                syncBean.setUpdateTimestamp( deptBean.getUpdateTimestamp());
                                syncBean.setUpdateUser( deptBean.getUpdateUser());
                                //#Case 3855 -- start
//                                syncBean.setProposalPersonBioPDFBean(deptBean.getProposalPersonBioPDFBean());
                              //  #Case3855 -- end
                                personPdfBean = deptBean.getDepartmentBioPDFPersonFormBean();
                                proposalPersonPDFBean = new ProposalPersonBioPDFBean();
                                proposalPersonPDFBean.setProposalNumber( proposalId );
                                proposalPersonPDFBean.setPersonId(personPdfBean.getPersonId());
                                if(deptBean.isHasBioPDF()){
                                    proposalPersonPDFBean.setFileName(personPdfBean.getFileName());
                                    proposalPersonPDFBean.setFileBytes(personPdfBean.getFileBytes());
                                    proposalPersonPDFBean.setMimeType(personPdfBean.getMimeType());
                                    proposalPersonPDFBean.setAcType( INSERT_RECORD );
                                }
                                syncBean.setProposalPersonBioPDFBean(proposalPersonPDFBean);
                                //CoeusDev-139 End
                                syncBioData.addElement( syncBean );
                                
                            }
                            
                            personBean.setPropBiography( syncBioData );
                            htPersonData.put(personId,personBean);
                            htPersonBiographyData.put(personId,syncBioData);
                            // Commneted to fire save validation if the synced person
                            // doesn't have the module type code
                            //saveRequired = false;//Added by chandra
                            saveRequired = true;
                           
                        }
                    }
                }
            }
            tblProposalPerson.clearSelection();
            tblProposalPerson.setRowSelectionInterval(0,0);
        }
    }
    //Case #1777 Start 13
    private Integer getMaxSortId(){
        Integer sortId = (( ProposalPersonFormBean )
        proposalPersonData.get(0)).getSortId();
        for(int index=1;index<proposalPersonData.size();index++){
            try{
                ProposalPersonFormBean proposalPersonFormBean = ( ProposalPersonFormBean )
                proposalPersonData.get( index );
                if(sortId==null&&proposalPersonFormBean.getSortId()==null)
                    continue;
                else if(sortId==null&&proposalPersonFormBean.getSortId()!=null){
                    sortId=proposalPersonFormBean.getSortId();
                }else{
                    if(proposalPersonFormBean.getSortId()!=null){
                        if(proposalPersonFormBean.getSortId().intValue()>sortId.intValue()){
                            sortId=proposalPersonFormBean.getSortId();
                        }
                    }
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return sortId;
    }
    //Case #1777 End 13
    private Hashtable constructPersonHashtable(Vector vecProposalPersonData){
        
        Hashtable newData = new Hashtable();
        
        //htPersonBiographyData = new Hashtable();
        //htPerBioData = new Hashtable();
        
        vecProposalPerson = new Vector();
        vecPerson = new Vector();
        
        if( vecProposalPersonData == null ){
            return newData;
        }
        
        ProposalPersonFormBean proposalPersonFormBean = null;
        
        Vector personTableRow = null;
        
        String personId = null;
        Vector vecPropPersonBioDetail = null;
         //Commented for pmd check, unused local variable 
        //Vector vecPersonBioDetail = null;
        //Case#1777 Start
        sortId=getMaxSortId();
        //Case#1777 End
        
        for( int indx = 0; indx < vecProposalPersonData.size() ; indx ++ ){
            try{
                // construct table data from vector of investigator beans
                proposalPersonFormBean = new ProposalPersonFormBean();
                proposalPersonFormBean = ( ProposalPersonFormBean )
                vecProposalPersonData.get( indx );
                if( proposalPersonFormBean != null){
                    
                    personId  = proposalPersonFormBean.getPersonId();
                    String fullName = proposalPersonFormBean.getFullName();
                    String primaryTitle = proposalPersonFormBean.getPrimaryTitle();
                    //Case #1777 Start 11
                    if(sortId==null){
                        sortId = new Integer(1);
                        proposalPersonFormBean.setSortId(sortId);
                        proposalPersonFormBean.setAcType(TypeConstants.UPDATE_RECORD);
                        //commented for case #2032 , by tarique start
                        //savePerson = true;
                        //commented for case #2032 , by tarique end
                    }else if(proposalPersonFormBean.getSortId() == null){
                        sortId = new Integer(sortId.intValue()+ 1);
                        proposalPersonFormBean.setSortId(sortId);
                        proposalPersonFormBean.setAcType(TypeConstants.UPDATE_RECORD);
                        //commented for case #2032 , by tarique start
                        //savePerson = true;
                        //commented for case #2032 , by tarique end
                    }
                    //Case #1777 End 11
                    /* insert each proposal person bean into hastable with personId
                      as key and proposal person bean as value */
                    newData.put( personId, proposalPersonFormBean );
                    
                    personTableRow = new Vector();
                    personTableRow.add( fullName == null ? "" : fullName );
                    personTableRow.add( primaryTitle == null ? "" : primaryTitle );
                    personTableRow.add(personId);
                    //Code added for case#2938 - Proposal Hierarchy enhancement
                    personTableRow.add(new Boolean(proposalPersonFormBean.isEditable()));
                    ////System.out.println("In SetFormData personId is : "+personId);
                    
                    /* JM 9-2-2015 add the status to a hidden column */
                    personTableRow.add(new String(proposalPersonFormBean.getStatus()));
                    /* JM END */
                    
                    /* JM 9-2-2015 add the isExternalPerson flag to a hidden column */
                    personTableRow.add(new String(proposalPersonFormBean.getIsExternalPerson()));
                    /* JM END */
                    
                    //Case:#4210 - Proposal Personnel issues: sync biosketch - Please select a document type loop - Start 
                    //Checks when personDetail window is opened or not
                    //If opened and closed then htPersonBiographyData HashTable object is used 
                    // OtherWise values are fetched from database
                    if(personClicked != 2){//Case:#4210 - End
                        if(htPersonBiographyData == null){
                            htPersonBiographyData = new Hashtable();
                        }
                        vecPropPersonBioDetail = new Vector();
                        vecPropPersonBioDetail = proposalPersonFormBean.getPropBiography();
                        //vecPersonBioDetail = proposalPersonFormBean.getPersonBiography();
                        if( personId != null && vecPropPersonBioDetail != null ){
                            htPersonBiographyData.put( personId , (Vector)vecPropPersonBioDetail.clone() );
                        }
                    }

                    //if(vecPersonBioDetail != null){
                    //  htPerBioData.put( personId , (Vector)vecPersonBioDetail.clone() );
                    //}
                    vecProposalPerson.add( personTableRow );
                }
            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        return newData;
    }
    
    public void setDataVectors(){
        this.proposalPersonData = getProposalPersonBiographyData(proposalId);
        this.vecDocumentType = getDocumentTypeCodes();
    }
    
    
    //supporting method to construct unit table data for a specified
    // investigator id
    private Vector getBiographyTableData( String personId ){
        
        Vector bioGraphyTableData = new Vector();
        Vector bioGraphyDataBean = new Vector();
        
        // fetch the unit details from hashtable for specified investigator
        bioGraphyDataBean = (Vector)htPersonBiographyData.get( personId );
        
        if( bioGraphyDataBean == null || bioGraphyDataBean.size() <= 0 ){
            // if there are no unit details present return empty vector
            return bioGraphyTableData;
        }
        
        Vector rowData = new Vector();
        ProposalBiographyFormBean proposalBiographyFormBean = null;
        
        for( int indx = 0; indx <  bioGraphyDataBean.size(); indx++ ){
            try{
                // construct vector of vectors from unitBeans
                proposalBiographyFormBean =  ( ProposalBiographyFormBean ) bioGraphyDataBean.get( indx );
                rowData = new Vector();
                
                rowData.add("");
                rowData.add( new Boolean(proposalBiographyFormBean.isHasBioSource()));
                rowData.add( new Boolean(proposalBiographyFormBean.isHasBioPDF()));
                rowData.add( proposalBiographyFormBean.getDescription());
                rowData.add( proposalBiographyFormBean.getPersonId());
                // To get the document type Code
                int typeCode=proposalBiographyFormBean.getDocumentTypeCode();
                ComboBoxBean comboBean = getDocumentComboData(typeCode);
                rowData.add( comboBean );
                 //#case 3855 ---- start
                if(proposalBiographyFormBean.getProposalPersonBioPDFBean()!=null) {
                    //Modified with case 4007:Icon based on mime type
//                     rowData.add(UserUtils.getFileExtension(proposalBiographyFormBean.getProposalPersonBioPDFBean().getFileName()));
                    rowData.add(proposalBiographyFormBean.getProposalPersonBioPDFBean());
                } else {
                    rowData.addElement(null);
                }       
               
                //#case 3855 ---- end
                bioGraphyTableData.add( rowData );
            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        
        return bioGraphyTableData;
        
    }
    /** Get the document type details in comboboxBean
     *@param document type code
     *@returns Comboboxbean for the given document type code
     */
    private ComboBoxBean getDocumentComboData(int typeCode){
        ComboBoxBean comboBean = null;
        Vector dataVector = new Vector();
        ComboBoxBean dataBean = null;
        if(vecDocumentType!= null && vecDocumentType.size() > 0){
            for(int index = 0; index < vecDocumentType.size(); index++){
                comboBean = (ComboBoxBean)vecDocumentType.get(index);
                int code = Integer.parseInt(comboBean.getCode());
                if(code == typeCode ){
                    dataVector.addElement(comboBean);
                    break;
                }
            }
        }
        
        if(dataVector != null && dataVector.size() > 0){
            dataBean = (ComboBoxBean)dataVector.get(0);
        }else{
            dataBean = new ComboBoxBean("","");
        }
        return dataBean;
    }
    
    private void setFormData(){
       //Commented for pmd check, unused local variable begin 
        //Vector vecData = null;
        //Vector vecDataPop = null;
        //Vector vecDataPopulate = new Vector();
         //Commented for pmd check, unused local variable end
        //Bug Fix :1681 Start 1
        int selectedTblRow = tblProposalPerson.getSelectedRow();
        //Bug Fix :1681 End 1
        //Commented for pmd check, unused local variable
        //ProposalBiographyFormBean bean = null;
        if(proposalPersonData != null){
            /* if investigators available then set the person table data */
            ((DefaultTableModel)tblProposalPerson.getModel()).setDataVector(vecProposalPerson,getPersonTableColumnNames());
            
            if( tblProposalPerson.getRowCount() > 0 ) {
            	
                /* JM 1-8-2016 added to set font color based on status; passing in MODIFY MODE to retain colors; 
                 * 1-29-2016 new method to allow multiple options */
            	//CustomTableCellRenderer renderer = 
            	//		new CustomTableCellRenderer(4,"I",Color.RED,true,false,true,MODIFY_MODE);
            	PersonTableCellRenderer renderer = 
            			new PersonTableCellRenderer(4,5,true,false,true,MODIFY_MODE);
            	tblProposalPerson.getColumnModel().getColumn(0).setCellRenderer(renderer);
            	/* JM END */
            	
                /* show the first investigators unit details */
                //Bug Fix  :1681 Start 2
                /*String perId =(tblProposalPerson.getValueAt( 0,2 ) == null ? ""
                : tblProposalPerson.getValueAt( 0,2 ).toString());*/
                String perId = null;
                if(selectedTblRow == -1){
                    selectedTblRow = 0;
                }
                
                perId =(tblProposalPerson.getValueAt( selectedTblRow,2 ) == null ? ""
                : tblProposalPerson.getValueAt( selectedTblRow,2 ).toString());
                tblProposalPerson.setRowSelectionInterval(selectedTblRow,selectedTblRow);
                
                //Bug Fix :1681 End 2
                if( ( perId != null )&& (!perId.equalsIgnoreCase("") )){
                    
                    ((DefaultTableModel)tblPersonBiography.getModel()).setDataVector(
                    getBiographyTableData( perId ),
                    getBiographyTableColumnNames());
                    //Bug Fix :1681 Start3
                    setEditors();
                    
                    if(tblPersonBiography.getRowCount() > 0 ){
                        tblPersonBiography.setRowSelectionInterval(0,0);
                    }
                    
                    
                    //Bug Fix :1681 End 3
                    ((DefaultTableModel)tblPersonBiography.getModel()).fireTableDataChanged();
                    //Bug Fix :1681 Start 4
                    //tblProposalPerson.addRowSelectionInterval( 0, 0 );
                    //Bug Fix :1681 End 4
                    
                    //                    // Added by chandra
                    //                    vecDataPop = (Vector)htPersonBiographyData.get(perId);
                    //                    if(vecDataPop!= null){
                    //                        int size = vecDataPop.size();
                    //                            for(int index = 0; index < size; index ++){
                    //                                bean = (ProposalBiographyFormBean)vecDataPop.get(index);
                    //                                String desc = bean.getDescription();
                    //                                vecData= new java.util.Vector();
                    //                                vecData.addElement("");
                    //                                vecData.addElement(new Boolean(bean.isHasBioSource() ) );
                    //                                vecData.addElement(new Boolean(bean.isHasBioPDF() ));
                    //                                vecData.addElement(desc == null ? "" : desc );
                    //                                vecDataPopulate.addElement(vecData);
                    //                            }
                    //                    }
                    //
                    //                    ((DefaultTableModel)tblPersonBiography.getModel()).
                    //                        setDataVector(vecDataPopulate,getBiographyTableColumnNames());
                    //                    ((DefaultTableModel)tblPersonBiography.getModel()).
                    //                        fireTableDataChanged();
                    
                    // End Chandra
                    
                }else{
                    /* if there are no investigators present show empty table
                     * for unit details table */
                    ((DefaultTableModel)tblPersonBiography.getModel()).setDataVector(
                    new Object[][]{}, getBiographyTableColumnNames().toArray());
                }
            }else{
                /* if there are no investigators present show empty table
                 * for investigators table */
                ((DefaultTableModel)tblPersonBiography.getModel()).setDataVector(
                new Object[][]{}, getBiographyTableColumnNames().toArray());
            }
        }
        //Bug Fix:1681 Start 5
        tblProposalPerson.addRowSelectionInterval( selectedTblRow, selectedTblRow );
        //Bug Fix:1681 End 5
        saveRequired = false;
    }
    
    private Vector getPersonTableColumnNames(){
        
        Enumeration enumColNames = tblProposalPerson.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while( enumColNames.hasMoreElements() ){
            
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    private Vector getBiographyTableColumnNames(){
        
        Enumeration enumColNames = tblPersonBiography.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while( enumColNames.hasMoreElements() ){
            
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }
    
    private void setEditors(){
        
        JTableHeader header = tblProposalPerson.getTableHeader();
        header.setFont( CoeusFontFactory.getLabelFont() );
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        
        TableColumn tColumn = tblProposalPerson.getColumnModel().getColumn(0);
        tColumn.setMinWidth(200);
        tColumn.setMaxWidth(200);
        tColumn.setPreferredWidth(200);
        
        tColumn = tblProposalPerson.getColumnModel().getColumn(1);
        tColumn.setMinWidth(600);
        tColumn.setMaxWidth(600);
        tColumn.setPreferredWidth(600);
        
        tColumn = tblProposalPerson.getColumnModel().getColumn(2);
        tColumn.setMinWidth(0);
        tColumn.setMaxWidth(0);
        tColumn.setPreferredWidth(0);
        
        //Set the row height of the table
        tblProposalPerson.setRowHeight(24);
        tblPersonBiography.setRowHeight(24);
        
        
        TableColumn column = tblPersonBiography.getColumnModel().getColumn(0);
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        
        header = tblPersonBiography.getTableHeader();
        header.setFont( CoeusFontFactory.getLabelFont() );
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        
        column = tblPersonBiography.getColumnModel().getColumn(1);
        //Modified for case 3685 - Remove Word icons - start
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
//        column.setCellRenderer(new ButtonRenderer());
//        column.setCellEditor(new ButtonEditor(new JCheckBox()));
//        //Modified for case 3685 - Remove Word icons - end
        
        column = tblPersonBiography.getColumnModel().getColumn(2);
        column.setMinWidth(22);
        column.setMaxWidth(22);
        column.setPreferredWidth(22);
        column.setResizable(false);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new ButtonRenderer());
        //#case 3855 ---- start
      //  column.setCellEditor(new ButtonEditor(new JCheckBox()));
        column.setCellEditor(new ButtonEditor());
       //#case 3855 ---- End
        column = tblPersonBiography.getColumnModel().getColumn(3);
        column.setResizable(false);
        column.setCellRenderer(new ColumnValueRenderer());
        column.setCellEditor(new ColumnValueEditor(200));
        
        column = tblPersonBiography.getColumnModel().getColumn(4);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
        
        column = tblPersonBiography.getColumnModel().getColumn(5);
        column.setMinWidth(120);
        column.setMaxWidth(120);
        column.setPreferredWidth(120);
        column.setResizable(false);
        column.setCellRenderer(new ColumnValueRenderer());
        column.setCellEditor(new ColumnValueEditor(200));
        
        // # Case  ----- Start
        column = tblPersonBiography.getColumnModel().getColumn(6);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
       // # Case 3855 ----- End
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlProposalPersons = new javax.swing.JPanel();
        pnlProposalPersonContainer = new javax.swing.JPanel();
        scrPnProposalPerson = new javax.swing.JScrollPane();
        tblProposalPerson = new javax.swing.JTable();
        pnlProposalPersonBiographyContainer = new javax.swing.JPanel();
        scrPnBiography = new javax.swing.JScrollPane();
        tblPersonBiography = new javax.swing.JTable();
        pnlProposalDescriptionContainer = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        pnlMessage = new javax.swing.JPanel();
        lblMessage = new javax.swing.JLabel();
        lblUpdatedBy = new javax.swing.JLabel();
        txtUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblLastDocument = new javax.swing.JLabel();
        txtLastDocUpdateUser = new edu.mit.coeus.utils.CoeusTextField();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();

        setMinimumSize(new java.awt.Dimension(795, 443));
        pnlProposalPersons.setLayout(new java.awt.GridBagLayout());

        pnlProposalPersons.setMinimumSize(new java.awt.Dimension(900, 410));
        pnlProposalPersons.setPreferredSize(new java.awt.Dimension(900, 410));
        pnlProposalPersonContainer.setLayout(new java.awt.GridBagLayout());

        pnlProposalPersonContainer.setMinimumSize(new java.awt.Dimension(774, 260));
        pnlProposalPersonContainer.setPreferredSize(new java.awt.Dimension(774, 260));
        scrPnProposalPerson.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnProposalPerson.setMinimumSize(new java.awt.Dimension(774, 260));
        scrPnProposalPerson.setPreferredSize(new java.awt.Dimension(774, 260));
        tblProposalPerson.setFont(CoeusFontFactory.getNormalFont());
        scrPnProposalPerson.setViewportView(tblProposalPerson);

        pnlProposalPersonContainer.add(scrPnProposalPerson, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 2);
        pnlProposalPersons.add(pnlProposalPersonContainer, gridBagConstraints);

        pnlProposalPersonBiographyContainer.setLayout(new java.awt.GridBagLayout());

        scrPnBiography.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnBiography.setMinimumSize(new java.awt.Dimension(774, 153));
        scrPnBiography.setPreferredSize(new java.awt.Dimension(774, 153));
        tblPersonBiography.setFont(CoeusFontFactory.getNormalFont());
        tblPersonBiography.setOpaque(false);
        tblPersonBiography.setSelectionBackground(new java.awt.Color(255, 255, 255));
        tblPersonBiography.setShowHorizontalLines(false);
        tblPersonBiography.setShowVerticalLines(false);
        scrPnBiography.setViewportView(tblPersonBiography);

        pnlProposalPersonBiographyContainer.add(scrPnBiography, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 2);
        pnlProposalPersons.add(pnlProposalPersonBiographyContainer, gridBagConstraints);

        pnlProposalDescriptionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlProposalDescriptionContainer.setMaximumSize(new java.awt.Dimension(774, 40));
        pnlProposalDescriptionContainer.setMinimumSize(new java.awt.Dimension(774, 40));
        pnlProposalDescriptionContainer.setPreferredSize(new java.awt.Dimension(774, 40));
        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        pnlProposalDescription.setMaximumSize(new java.awt.Dimension(774, 39));
        pnlProposalDescription.setMinimumSize(new java.awt.Dimension(774, 39));
        pnlProposalDescription.setPreferredSize(new java.awt.Dimension(774, 39));
        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalValue.setText("xxxxxxxx");
        lblProposalValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblProposalValue, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setText("Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 0, 0);
        pnlProposalDescription.add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSponsorValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lblSponsorValue.setMaximumSize(new java.awt.Dimension(300, 17));
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(300, 17));
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(300, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblSponsorValue, gridBagConstraints);

        pnlProposalDescriptionContainer.add(pnlProposalDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        pnlProposalPersons.add(pnlProposalDescriptionContainer, gridBagConstraints);

        pnlMessage.setLayout(new java.awt.GridBagLayout());

        pnlMessage.setMaximumSize(new java.awt.Dimension(895, 100));
        pnlMessage.setMinimumSize(new java.awt.Dimension(895, 100));
        pnlMessage.setPreferredSize(new java.awt.Dimension(895, 100));
        lblMessage.setFont(CoeusFontFactory.getLabelFont());
        lblMessage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMessage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlMessage.add(lblMessage, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlMessage.add(lblUpdatedBy, gridBagConstraints);

        txtUpdateUser.setEditable(false);
        txtUpdateUser.setMinimumSize(new java.awt.Dimension(380, 20));
        txtUpdateUser.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 2, 6);
        pnlMessage.add(txtUpdateUser, gridBagConstraints);

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
        pnlMessage.add(lblLastDocument, gridBagConstraints);

        txtLastDocUpdateUser.setEditable(false);
        txtLastDocUpdateUser.setMinimumSize(new java.awt.Dimension(380, 20));
        txtLastDocUpdateUser.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 6);
        pnlMessage.add(txtLastDocUpdateUser, gridBagConstraints);

        lblFileName.setFont(CoeusFontFactory.getLabelFont());
        lblFileName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFileName.setText("File Name: ");
        lblFileName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblFileName.setMaximumSize(new java.awt.Dimension(150, 20));
        lblFileName.setMinimumSize(new java.awt.Dimension(150, 20));
        lblFileName.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        pnlMessage.add(lblFileName, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setMinimumSize(new java.awt.Dimension(380, 20));
        txtFileName.setPreferredSize(new java.awt.Dimension(380, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 6);
        pnlMessage.add(txtFileName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 2);
        pnlProposalPersons.add(pnlMessage, gridBagConstraints);

        getContentPane().add(pnlProposalPersons, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    //JIRA COEUSDEV-548 - START - 2
    /**
     * @return the unitNumber
     */
    public String getUnitNumber() {
        return unitNumber;
    }

    /**
     * @param unitNumber the unitNumber to set
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    //JIRA COEUSDEV-548 - END 2

    /* JM 9-2-2015 updated for status; 1-8-2016 update for isExternalPerson  */
    public class ProposalPersonTableModel extends DefaultTableModel{
        private String colNames[] = {"Full Name", "Title", "PersonId", "editable", "status","isExternalPerson"};
        private Class colClass[] =  {String.class, String.class, String.class, Boolean.class, String.class, String.class};
        
        public boolean isCellEditable(int row, int col){
            // If the user doen't have right then stop cell editable for all col &row
            return false;
        }
        public int getColumnCount(){
            return colNames.length;
        }
        public String getColumnName(int column) {
            return colNames[column];
        }
        public Class getColumnClass(int col) {
            return colClass [col];
        }
    }
    
    
    
    public class PersonnelBiographyTableModel extends DefaultTableModel implements TableModel{
       //# Case 3855 ---- Start
        private String colNames[] = {"HandIcon", "", "", "Description", "PersonId", "Document Type",""};
        // Modified for internal issue fix 135 Class Cast exception while clicking on down arrow start
        private Class colClass [] = {java.lang.Object.class, java.lang.Object.class,
        java.lang.Object.class, java.lang.String.class, java.lang.String.class,java.lang.Object.class,java.lang.Object.class };
         // Modified for internal issue fix 135 Class Cast exception while clicking on down arrow end
         //# Case 3855 ---- end
        
        public boolean isCellEditable(int row, int col){
            //Code commented and added for case#2938 - Proposal Hierarchy enhancement
            //Making the row editable according to the selected persons in child records
            //            if( functionType == DISPLAY_MODE ) {
            if( functionType == DISPLAY_MODE || !isEnabled) {
                //bug fix id 1744 done by shiji - step 4 : start
                if(col == 1 || col == 2) {
                    return ((Boolean)getValueAt(row,col)).booleanValue();
                }else {
                    return false;
                }
                //bug fix id 1744 - step 4 : End
            }
            if( col == 1 || col == 2 ){
                return ((Boolean)getValueAt(row,col)).booleanValue();
            }
            //Set the hand icon column non editable
            if(col == 0){
                return false;
            }
            return true;
        }
        
        public int getColumnCount(){
            return colNames.length;
        }
        
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        public Class getColumnClass(int col) {
            return colClass [col];
        }
    }
    
    /** This abstract method must be implemented by all classes which inherits this class.
     * Used for saving the current activesheet when clicked Save from File menu.
     */
    public void saveActiveSheet() {
        try{
            performSave();
        }catch(Exception ex){
            if(!( ex.getMessage().equals(
            coeusMessageResources.parseMessageKey(
            "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
        }
    }
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** Getter for property proposalId.
     * @return Value of property proposalId.
     */
    public java.lang.String getProposalId() {
        return proposalId;
    }
    
    /** Setter for property proposalId.
     * @param proposalId New value of property proposalId.
     */
    public void setProposalId(java.lang.String proposalId) {
        this.proposalId = proposalId;
    }
    
    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /** Getter for property proposalDetailForm.
     * @return Value of property proposalDetailForm.
     */
    // public edu.mit.coeus.propdev.gui.ProposalDetailForm getProposalDetailForm() {
    //     return proposalDetailForm;
    // }
    
    /** Setter for property proposalDetailForm.
     * @param proposalDetailForm New value of property proposalDetailForm.
     */
    // public void setProposalDetailForm(edu.mit.coeus.propdev.gui.ProposalDetailForm proposalDetailForm) {
    //    this.proposalDetailForm = proposalDetailForm;
    // }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        
        Object actSource = actionEvent.getSource();
        int rowCount = tblPersonBiography.getRowCount();
        try{
            if (actSource.equals(biographyAdd) || actSource.equals(btnAddModule)) {
                
                String personNum = null;
                if( tblProposalPerson.getRowCount() > 0 ){
                    //System.out.println("tblProposalPerson.getRowCount() is "+tblProposalPerson.getRowCount());
                    //System.out.println("1");
                    //System.out.println("tblProposalPerson.getSelectedRow() is "+tblProposalPerson.getSelectedRow());
                    int selRow = tblProposalPerson.getSelectedRow();
                    //System.out.println("SelRow is "+selRow);
                    if(selRow != -1){
                        //System.out.println("2");
                        personNum =  (String)((DefaultTableModel)tblProposalPerson.getModel()).getValueAt(selRow, 2);
                    }
                }
                if( tblPersonBiography.isEditing() ){
                    if(tblPersonBiography.getCellEditor() != null && rowCount > 0){
                        tblPersonBiography.getCellEditor().stopCellEditing();
                    }
                }
                //boolean valid = validateRowEntry();
                boolean valid = validateData();
                if(!valid){
                    return;
                }
                
                Vector newRowData = new Vector();
                
                TableColumn column = tblPersonBiography.getColumnModel().getColumn(0);
                column.setHeaderRenderer(new EmptyHeaderRenderer());
                column.setCellRenderer(new IconRenderer());
                
                column = tblPersonBiography.getColumnModel().getColumn(1);
                //Commented for case 3685 - Remove Word icons - start
//                column.setCellRenderer(new ButtonRenderer());
//                column.setCellEditor(new ButtonEditor(new JCheckBox()));
                //Commented for case 3685 - Remove Word icons - end
                column = tblPersonBiography.getColumnModel().getColumn(2);
                column.setCellRenderer(new ButtonRenderer());
               // #Case 3855 ---- Start
                column.setCellEditor(new ButtonEditor());
             //   column.setCellEditor(new ButtonEditor(new JCheckBox()));
              // #Case 3855 ---- End
                column = tblPersonBiography.getColumnModel().getColumn(3);
                column.setCellRenderer(new ColumnValueRenderer());
                column.setCellEditor(new ColumnValueEditor(200));
                
                column = tblPersonBiography.getColumnModel().getColumn(5);
                column.setCellRenderer(new ColumnValueRenderer());
                column.setCellEditor(new ColumnValueEditor(200));
                
                newRowData.addElement("");
                newRowData.addElement(new Boolean(false));
                newRowData.addElement(new Boolean(false));
                newRowData.addElement("");
                newRowData.addElement(personNum);
                newRowData.addElement(new ComboBoxBean("",""));
                
                ProposalBiographyFormBean bioBean = new ProposalBiographyFormBean();
                bioBean.setAcType(INSERT_RECORD);
                bioBean.setProposalNumber(proposalId);
                bioBean.setPersonId(personNum);
                //Code added for Proposal Hierarchy case#3183
                bioBean.setParentProposal(isParentProposal());
                //System.out.println("personNum is "+personNum);
                //System.out.println("Bean is "+bioBean);
                
                if(htPersonBiographyData != null && htPersonBiographyData.elements() != null && htPersonBiographyData.size() > 0){
                    //System.out.println("personId is "+personNum);
                    if( ((Vector) htPersonBiographyData.get(personNum)) != null){
                        ((Vector) htPersonBiographyData.get(personNum)).addElement(bioBean);
                    }else{
                        htPersonBiographyData.put(personNum, new Vector());
                        ((Vector) htPersonBiographyData.get(personNum)).addElement(bioBean);
                    }
                }else{
                    htPersonBiographyData = new Hashtable();
                    Vector vecBio = new Vector();
                    vecBio.addElement(bioBean);
                    htPersonBiographyData.put(personNum, vecBio );
                }
                
                saveRequired=true;
                ((DefaultTableModel)tblPersonBiography.getModel()).addRow( newRowData );
                ((DefaultTableModel)tblPersonBiography.getModel()).fireTableDataChanged();
                
                int lastRow = tblPersonBiography.getRowCount() - 1;
                if(lastRow >= 0){
                    btnDeleteModule.setEnabled(true);
                    //added for the bug fix:1712 to set the focus to the particular cell start
                    setRequestFocusInThread(lastRow ,3);
                    //end
                }
                
                int bioRowCount = tblPersonBiography.getRowCount();
                //Added by Amit 11/20/2003
                if(actSource.equals(btnAddModule)) {
                    //added for the bug fix:1712 to set the focus to the particular cell start
                    setRequestFocusInThread(bioRowCount-1 ,3);
                    //end
                }
                //End Amit
                
                // Added by chandra. If the no row present the disable the corresponding
                // buttons and menu items. Else enable  the menu items and tool bar buttons
                if(tblPersonBiography.getRowCount() <= 0){
                    btnDeleteModule.setEnabled(false);
                    biographyDelete.setEnabled(false);
                     //Commented for case 3685 - Remove Word icons - start
//                    viewWordFiles.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - end
                    viewPdfFiles.setEnabled(false);
                    //Case 2469 start
                    //                    upLoadWordFiles.setEnabled(false);
                    //                    uploadPdfFiles.setEnabled(false);
                    //Case 2469 End
                    //Commented for case 3685 - Remove Word icons - start
//                    btnEditWordSource.setEnabled(false);
                    //Commented for case 3685 - Remove Word icons - end
                    btnViewPdfFile.setEnabled(false);
                    
                }else{
                    btnDeleteModule.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - start
//                    viewWordFiles.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - end
                    viewPdfFiles.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - start
//                    upLoadWordFiles.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - end
                    uploadPdfFiles.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - start
//                    btnEditWordSource.setEnabled(true);
                    //Commented for case 3685 - Remove Word icons - end
                    btnViewPdfFile.setEnabled(true);
                    biographyDelete.setEnabled(true);
                }// End chandra
            }else if (actSource.equals(btnCloseBiography)) {
                performClose();
            }else if (actSource.equals(btnSaveBiography)) {
                performSave();
            }else if (actSource.equals(biographyDelete) || actSource.equals(btnDeleteModule)) {
                performDelete();
                // }else if (actSource.equals(biographyEditWordSource) || actSource.equals(btnEditWordSource)) {
                ////System.out.println("Prints All Biography Information");
                //getProposalPersonBioData();
            }else if (actSource.equals(btnDisplayDegreeDetails) || actSource.equals(personDegreeDetail)){
                
                int selectedRow = 0;
                String personId = null;
                String personNm = null;
                if (tblProposalPerson != null && tblProposalPerson.getRowCount() > 0) {
                    selectedRow = tblProposalPerson.getSelectedRow();
                    if (selectedRow >= 0) {
                        personId = (String)tblProposalPerson.getValueAt(selectedRow, 2);
                        personNm = (String)tblProposalPerson.getValueAt(selectedRow, 0);
                    }
                }
                displayDegreeDetails(personId,personNm);
            }else if (actSource.equals(btnDisplayPersonDetails) || actSource.equals(personDetail)){
                
                int selectedRow = 0;
                String personId = null;
                String personName = null;
                if (tblProposalPerson != null && tblProposalPerson.getRowCount() > 0) {
                    selectedRow = tblProposalPerson.getSelectedRow();
                    if (selectedRow >= 0) {
                        personId = (String)tblProposalPerson.getValueAt(selectedRow, 2);
                        personName = (String)tblProposalPerson.getValueAt(selectedRow, 0);
                        //System.out.println("IN btnDisplayPersonDetails personId is : "+personId);
                    }
                }
                displayPersonDetails(personId, personName, MODIFY_MODE);
                //Commented for case 3685 - Remove Word icons - start
//            }else if(actSource.equals(viewWordFiles)){
//                viewWordDocument();
                //Commented for case 3685 - Remove Word icons - end
            }else if(actSource.equals(viewPdfFiles)){
                viewPdfDocument();
                //Commented for case 3685 - Remove Word icons - start
//            }else if(actSource.equals(upLoadWordFiles) ||(actSource.equals(btnEditWordSource))){
//                uploadWordDocument();
                //Commented for case 3685 - Remove Word icons - end
            }else if(actSource.equals(uploadPdfFiles) || (actSource.equals(btnViewPdfFile))){
                uploadPdfDocument();
            }
            //Case #1777 Start 8
            else if(actSource.equals(btnMoveUp)||(actSource.equals(mnItmMoveUp))){
                moveUp();
            }else if(actSource.equals(btnMoveDown)||(actSource.equals(mnItmMoveDown))){
                moveDown();
            }
            //Case #1777 End 8
            else{
                throw new Exception(coeusMessageResources.parseMessageKey(
                "funcNotImpl_exceptionCode.1100"));
            }
        }catch(Exception exception){
            exception.printStackTrace();
            if(!( exception.getMessage().equals(
            coeusMessageResources.parseMessageKey(
            "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showErrorDialog(exception.getMessage());
            }
        }
    }
    //Case #1777 Start 9
    private void moveDown(){
        int rowCount=tblProposalPerson.getRowCount();
        int selRow = tblProposalPerson.getSelectedRow();
        if(selRow==rowCount-1||selRow==-1){
            return ;
        }
        if(selRow < (rowCount-1)){
            swapSortNumbers( selRow, selRow+1 );
            ((DefaultTableModel)tblProposalPerson.getModel()).moveRow(selRow,selRow,selRow+1);
            tblProposalPerson.setRowSelectionInterval(selRow+1,selRow+1);
            //saveRequired=true;
        }
    }
    
    private void moveUp(){
        int selRow = tblProposalPerson.getSelectedRow();
        if(selRow==0||selRow==-1){
            return ;
        }
        if(selRow>0){
            swapSortNumbers(selRow, selRow-1);
            ((DefaultTableModel)tblProposalPerson.getModel()).moveRow(selRow,selRow,selRow-1);
            tblProposalPerson.setRowSelectionInterval(selRow-1,selRow-1);
            //saveRequired=true;
        }
    }
    private void swapSortNumbers( int row1, int row2 ) {
        ProposalPersonFormBean firstBean= null, secondBean = null;
        //Commented for pmd check, unused local variable
        //int firstSortNum=0, secondSortNum = 0;
        firstBean = (ProposalPersonFormBean)proposalPersonData.get(row1);
        secondBean = (ProposalPersonFormBean)proposalPersonData.get(row2);
        
        Integer sortId1=firstBean.getSortId();
        Integer sortId2=secondBean.getSortId();
        firstBean.setSortId(sortId2);
        secondBean.setSortId(sortId1);
        if( firstBean.getAcType() == null) {
            firstBean.setAcType( TypeConstants.UPDATE_RECORD );
        }
        
        if( secondBean.getAcType() == null) {
            secondBean.setAcType( TypeConstants.UPDATE_RECORD );
        }
        //       firstBean.setAcType( TypeConstants.UPDATE_RECORD );
        //       secondBean.setAcType( TypeConstants.UPDATE_RECORD );
        Object obj = proposalPersonData.get(row1);
        proposalPersonData.remove(row1);
        proposalPersonData.insertElementAt(obj, row2);
        
        saveRequired = true;
        
    }
    //Case #1777 End 9
    
    /**
     * This method is invoked when the user clicks display in the degree menu
     * of Person module
     */
    private void displayDegreeDetails(String personId, String personNm){
        if (personId == null) {
            log(coeusMessageResources.parseMessageKey(
            "protoBaseWin_exceptionCode.1052"));
        }else {
            try{
                PersonDegreeForm personDegreeForm = new PersonDegreeForm(functionType,CoeusGuiConstants.PROPOSAL_MODULE);
                //System.out.println("IN PERSONNEL FORM");
                //System.out.println("proposalId is "+proposalId);
                //System.out.println("personId is "+personId);
                personDegreeForm.setProposalId(proposalId);
                personDegreeForm.setPersonName(personNm);
                personDegreeForm.setPersonId(personId);
                //Code modified for case#2938 - Proposal Hierarchy enhancement - starts
                //Opening degree info details in edit or display mode
                //according to the selected persons in child records
                if(isEnabled){
                    personDegreeForm.setCanModifyProposal(canModifyProposal);
                } else {
                    personDegreeForm.setCanModifyProposal(0);
                }
                /*Bug Fix*/
                /*to set the person name to the title of the dialog instaed of the person id*/
                personDegreeForm.setName(personNm);
                personDegreeForm.showDegreeForm();
                
            }catch( Exception err ){
                err.printStackTrace();
                CoeusOptionPane.showInfoDialog(err.getMessage());
            }
        }
    }
    
    //supporting method to show the delete confirm message.
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        return  selectedOption;
    }
    
    private void performDelete(){
        //added for the bug fix:1712 step:5 start
        if(tblPersonBiography.getCellEditor() != null){
            tblPersonBiography.getCellEditor().stopCellEditing();
        }
        //end step:5
        int selectedRow = tblProposalPerson.getSelectedRow();
        
        if(selectedRow != -1){
            String personId = (String)tblProposalPerson.getValueAt(selectedRow,2);
            int selectedBiographyRow = tblPersonBiography.getSelectedRow();
            if( selectedBiographyRow != -1 ){
                int selectedOption
                = showDeleteConfirmMessage("Are you sure you want to delete this biography?");
                if( selectedOption == JOptionPane.YES_OPTION ){
                    ProposalBiographyFormBean  bioBean = null;
                    if(htPersonBiographyData.containsKey(personId)){
                        Vector vecBio = null;
                        vecBio = (Vector)htPersonBiographyData.get(personId);
                        if(vecBio!= null){
                            //for(int index = 0; index < vecBio.size(); index++){
                            bioBean = (ProposalBiographyFormBean)vecBio.elementAt(selectedBiographyRow);
                            if(bioBean != null){
                                String acType = bioBean.getAcType();
                                if( (acType != null )){
                                    if( !acType.equalsIgnoreCase(INSERT_RECORD) ) {
                                        bioBean.setAcType( DELETE_RECORD );
                                        //Code added for Proposal Hierarchy case#3183
                                        bioBean.setParentProposal(isParentProposal());
                                        //Check if PDF data is present
                                        //If yes delete it
                                        if(bioBean.isHasBioPDF()){
                                            ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
                                            proposalPersonBioPDFBean.setAcType(DELETE_RECORD);
                                            //Code added for Proposal Hierarchy case#3183
                                            bioBean.setParentProposal(isParentProposal());
                                            proposalPersonBioPDFBean.setProposalNumber(bioBean.getProposalNumber());
                                            proposalPersonBioPDFBean.setBioNumber(bioBean.getBioNumber());
                                            bioBean.setProposalPersonBioPDFBean(proposalPersonBioPDFBean);
                                        }
                                        //Check if Source data is present
                                        //If yes delete it
                                        if(bioBean.isHasBioSource()){
                                            ProposalPersonBioSourceBean proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
                                            proposalPersonBioSourceBean.setAcType(DELETE_RECORD);
                                            //Code added for Proposal Hierarchy case#3183
                                            bioBean.setParentProposal(isParentProposal());
                                            proposalPersonBioSourceBean.setProposalNumber(bioBean.getProposalNumber());
                                            proposalPersonBioSourceBean.setBioNumber(bioBean.getBioNumber());
                                            bioBean.setProposalPersonBioSourceBean(proposalPersonBioSourceBean);
                                        }
                                        
                                        vecDeletedBiographyData.addElement( bioBean );
                                    }
                                }else{
                                    bioBean.setAcType( DELETE_RECORD );
                                    //Code added for Proposal Hierarchy case#3183
                                    bioBean.setParentProposal(isParentProposal());
                                    //Check if PDF data is present
                                    //If yes delete it
                                    if(bioBean.isHasBioPDF()){
                                        ProposalPersonBioPDFBean proposalPersonBioPDFBean = new ProposalPersonBioPDFBean();
                                        proposalPersonBioPDFBean.setAcType(DELETE_RECORD);
                                        //Code added for Proposal Hierarchy case#3183
                                        bioBean.setParentProposal(isParentProposal());
                                        proposalPersonBioPDFBean.setProposalNumber(bioBean.getProposalNumber());
                                        proposalPersonBioPDFBean.setBioNumber(bioBean.getBioNumber());
                                        bioBean.setProposalPersonBioPDFBean(proposalPersonBioPDFBean);
                                    }
                                    //Check if Source data is present
                                    //If yes delete it
                                    if(bioBean.isHasBioSource()){
                                        ProposalPersonBioSourceBean proposalPersonBioSourceBean = new ProposalPersonBioSourceBean();
                                        proposalPersonBioSourceBean.setAcType(DELETE_RECORD);
                                        //Code added for Proposal Hierarchy case#3183
                                        bioBean.setParentProposal(isParentProposal());
                                        proposalPersonBioSourceBean.setProposalNumber(bioBean.getProposalNumber());
                                        proposalPersonBioSourceBean.setBioNumber(bioBean.getBioNumber());
                                        bioBean.setProposalPersonBioSourceBean(proposalPersonBioSourceBean);
                                    }
                                    
                                    vecDeletedBiographyData.addElement( bioBean );
                                }
                                vecBio.removeElementAt( selectedBiographyRow );
                                //break;
                            }
                            //}
                        }
                        htPersonBiographyData.put(personId,vecBio);
                    }
                    saveRequired = true;
                    ((DefaultTableModel)tblPersonBiography.getModel()).removeRow( selectedBiographyRow );
                    
                }
                int newRowCount = tblPersonBiography.getRowCount();
                if(newRowCount > 0){
                    if(newRowCount > selectedBiographyRow){
                        tblPersonBiography.setRowSelectionInterval(selectedBiographyRow, selectedBiographyRow);
                    }else{
                        tblPersonBiography.setRowSelectionInterval(newRowCount - 1, newRowCount - 1);
                    }
                }else{
                    btnDeleteModule.setEnabled( false );
                    //Added for case 2349 - Show timestamps in Proposal Personnel - start
                    txtUpdateUser.setText("");
                    txtLastDocUpdateUser.setText("");
                    txtFileName.setText("");
                    //Added for case 2349 - Show timestamps in Proposal Personnel - end
                }
            }else{
                showWarningMessage(
                coeusMessageResources.parseMessageKey(
                "protoInvFrm_exceptionCode.1133") );
            }
        }
        
        if(tblPersonBiography.getRowCount() <= 0){
            btnDeleteModule.setEnabled(false);
            biographyDelete.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(false);
            //            upLoadWordFiles.setEnabled(false);
            //            uploadPdfFiles.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - start
//            btnEditWordSource.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - end
            btnViewPdfFile.setEnabled(false);
            
        }else{
            btnDeleteModule.setEnabled(true);
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(true);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(true);
            //Commented for case 3685 - Remove Word icons - start
//            upLoadWordFiles.setEnabled(true);
            //Commented for case 3685 - Remove Word icons - end
            uploadPdfFiles.setEnabled(true);
            //Commented for case 3685 - Remove Word icons - start
//            btnEditWordSource.setEnabled(true);
            //Commented for case 3685 - Remove Word icons - end
            btnViewPdfFile.setEnabled(true);
            biographyDelete.setEnabled(true);
        }
    }
    
    //supporting method to show the warning message
    private void showWarningMessage( String str ){
        CoeusOptionPane.showWarningDialog(str);
    }
    
    // Helpe method which validates the row entry whether the description is null or not
    private boolean validateRowEntry() throws Exception{
        
        boolean valid = true;
        int rowCnt = tblPersonBiography.getRowCount();
        if(rowCnt >= 0){
            
            for(int inInd=0; inInd < rowCnt ;inInd++){
                
                String desc = (String)((DefaultTableModel)
                tblPersonBiography.getModel()).
                getValueAt(inInd,3);
                
                 ComboBoxBean comboBean = (ComboBoxBean)((DefaultTableModel)tblPersonBiography.getModel()).getValueAt(inInd,5);
                
                int code  = 0;
                if(comboBean!= null && !comboBean.getCode().equals("")){
                    code = Integer.parseInt(comboBean.getCode());
                }
                
                if((desc == null) || (desc.trim().length() <= 0)){
                    valid=false;
                    CoeusOptionPane.showInfoDialog("Enter the Description");
                    /* for the buf fix:1712 added to set the focus to the particular cell*/
                    setRequestFocusInThread(inInd,3);
                    break;
                //JIRA COEUSQA 1540 - START
                /*} else if(!validator.validate(desc)){
                    valid=false;
                    CoeusOptionPane.showErrorDialog("File Description "+coeusMessageResources.parseMessageKey("text_validation_exceptionCode.1000"));
                    setRequestFocusInThread(inInd,3);*/
                //JIRA COEUSQA 1540 - END
                }else if(code == 0){
                    valid=false;
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("prop_person_document_typeCode.2000"));
                    setRequestFocusInThread(inInd,5);
                }
            }
            if(!valid){
                return false;
            }
        }
        return valid;
    }
    
    /*added for the bug fix:1712 start step:6
     *to set the focus to the particular cell*/
    private void setRequestFocusInThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tblPersonBiography.setRowSelectionInterval(selrow, selrow);
                tblPersonBiography.scrollRectToVisible(
                tblPersonBiography.getCellRect(selrow ,0, true));
                tblPersonBiography.editCellAt(selrow,selcol);
                tblPersonBiography.getEditorComponent().requestFocusInWindow();
                
            }
        });
    }//end step:6
    
    /** This method is used to show the alert messages to the user.
     * @param mesg is a message to alert the user.
     * @throws Exception is the <CODE>Exception</CODE> to throw in the client side.
     */
    
    private void errorMessage(String mesg) throws Exception{
        throw new Exception(mesg);
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        //bug fix id 1744 done by shiji - step 5 : start
        
        boolean  isWord=false;
        boolean  isPdf=false;
        //bug fix id 1744 - step 5 : End
        Object source = listSelectionEvent.getSource();
        
        int selPersonBiographyRow = tblPersonBiography.getSelectedRow();
        int selectedRow = tblProposalPerson.getSelectedRow();
        int rowCount = tblProposalPerson.getRowCount();
        
        //Commented for Case#4367 - prop dev move focus to next persons attachment after opening an attachment - Start
        //Check is not required for isWord and isPdf in PersonBiography when Person is selected in ProposalPerson table
        //bug fix id 1744 done by shiji - step 6 : start
//        if(selPersonBiographyRow != -1) {
//            isWord = ((Boolean)tblPersonBiography.getValueAt(selPersonBiographyRow,1)).booleanValue();
//            isPdf = ((Boolean)tblPersonBiography.getValueAt(selPersonBiographyRow,2)).booleanValue();
//        }
        //bug fix id 1744 done by - step 6: End
        //Case#4367 - End

        if( ( source.equals( personSelectionModel) )&& ( selectedRow >= 0 )
        && (htPersonData != null) ) {//&& previousSelRow != -1
            //Added for case 2349 - Show timestamps in Proposal Personnel - start
            txtUpdateUser.setText("");
            txtLastDocUpdateUser.setText("");
            txtFileName.setText("");
            //Added for case 2349 - Show timestamps in Proposal Personnel - end
            ProposalPersonFormBean proposalPersonRow = null;
            String sId = (String)tblProposalPerson.getValueAt( selectedRow,2);
            ////System.out.println("Person Id for row number "+sId+" is "+sId);
            proposalPersonRow = (ProposalPersonFormBean) htPersonData.get( sId );
            if(proposalPersonRow != null){
                ////System.out.println("proposalPersonRow not null");
                ////System.out.println("proposalPersonRow.getPersonId() is "+proposalPersonRow.getPersonId());
                Vector vecBioForPerson = getBiographyTableData( proposalPersonRow.getPersonId() );
                if( vecBioForPerson != null && vecBioForPerson.size() > 0){
                    
                    ((DefaultTableModel)tblPersonBiography.getModel()).setDataVector( vecBioForPerson, getBiographyTableColumnNames() );
                    
                    if( tblPersonBiography.getRowCount() > 0 ){
                        tblPersonBiography.setRowSelectionInterval(0,0);
                    }
                    if(functionType != DISPLAY_MODE){
                        btnDeleteModule.setEnabled( true );
                    }
                    //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                    if(hasPropPersonnel && tblPersonBiography.getRowCount() > 0 && proposalPersonRow.isEditable()){     
                         uploadPdfFiles.setEnabled(true);
                         btnViewPdfFile.setEnabled(true);
                         //Commented/Added for case#2156 - start
//                         btnViewPdfFile.setDisabledIcon(new ImageIcon(
//                                getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON)));
                         btnViewPdfFile.setDisabledIcon(
                                 new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON)));                         
                         //Commented/Added for case#2156 - end
                         //viewPdfFiles.setEnabled(true);
                    }else{
                        uploadPdfFiles.setEnabled(false);
                         btnViewPdfFile.setEnabled(false);
                         //Commented/Added for case#2156 - start
//                         btnViewPdfFile.setDisabledIcon(new ImageIcon(
//                                getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
                         btnViewPdfFile.setDisabledIcon(
                                 new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_ATTACHMENT_ICON)));                         
                         //Commented/Added for case#2156 - end
                         //viewPdfFiles.setEnabled(false);
                    }
                    //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
                }else{
                    btnDeleteModule.setEnabled( false );
                    ((DefaultTableModel)tblPersonBiography.getModel()).setDataVector(
                    new Vector(), getBiographyTableColumnNames() );
                    //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                 if( (tblPersonBiography.getRowCount() <= 0 || functionType == DISPLAY_MODE)){                        
                        viewPdfFiles.setEnabled(false);
                        btnViewPdfFile.setDisabledIcon(
                                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
                        btnViewPdfFile.setEnabled(false);
                        uploadPdfFiles.setEnabled(false);
                    }                 
                //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
                }
                //Code added for case#3183 - Proposal Hierarchy enhancement - starts
                //Enable or disable buttons according to the selected persons present in child records
                isEnabled = proposalPersonRow.isEditable();
                if(isParentProposal() && isEnabled){
                    lblMessage.setForeground(Color.BLUE);
                    lblMessage.setText(coeusMessageResources.parseMessageKey("prop_person_exceptionCode.3000"));
                }else if(isParentProposal() && !isEnabled){
                    lblMessage.setForeground(Color.RED);
                    lblMessage.setText(coeusMessageResources.parseMessageKey("prop_person_exceptionCode.3001"));
                } else if(!isEnabled){
                    lblMessage.setForeground(Color.RED);
                    lblMessage.setText(coeusMessageResources.parseMessageKey("prop_person_exceptionCode.3002"));
                }else{
                    lblMessage.setText(""); 
                }
                if(getFunctionType() != TypeConstants.DISPLAY_MODE){
                    btnAddModule.setEnabled(isEnabled);
                    btnDeleteModule.setEnabled(isEnabled);
                    //Commented for case 3685 - Remove Word icons - start
//                    btnEditWordSource.setEnabled(isEnabled);
                    //Commented for case 3685 - Remove Word icons - end
                    btnViewPdfFile.setEnabled(isEnabled);
                    biographyAdd.setEnabled(isEnabled);
                    biographyDelete.setEnabled(isEnabled);
                    //Commented for case 3685 - Remove Word icons - start
//                    upLoadWordFiles.setEnabled(isEnabled);
                    //Commented for case 3685 - Remove Word icons - end
                    uploadPdfFiles.setEnabled(isEnabled);
                }
                //Code added for case#3183 - Proposal Hierarchy enhancement - ends
            }else {
                ((DefaultTableModel)tblPersonBiography.getModel()).setDataVector(
                new Vector(), getBiographyTableColumnNames() );
                //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                 if(tblPersonBiography.getRowCount()<=0 || functionType == DISPLAY_MODE){                        
                        viewPdfFiles.setEnabled(false);
                        btnViewPdfFile.setDisabledIcon(
                                new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DISABLED_PDF_ICON)));
                        btnViewPdfFile.setEnabled(false);
                        uploadPdfFiles.setEnabled(false);
                    }                    
                //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
            }
            previousSelRow = selectedRow;
            setEditors();
            
        }else if((source.equals( bioSelectionModel)) && ( selPersonBiographyRow >= 0 )
        && (htPersonData != null)) {
            
            //bug fix id 1744 done by shiji - step 7 : start
            isWord = ((Boolean)tblPersonBiography.getValueAt(selPersonBiographyRow,1)).booleanValue();
            isPdf = ((Boolean)tblPersonBiography.getValueAt(selPersonBiographyRow,2)).booleanValue();
            //bug fix id 1744 - step 7 : end
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(isWord);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(isPdf);
            //bug fix id 1744 done by shiji - step 8 : start
             //Added for case 2349 - Show timestamps in Proposal Personnel - start
            if(tblProposalPerson.getSelectedRow()!=-1){
                String personId =(tblProposalPerson.getValueAt( selectedRow ,2 ) == null ? ""
                    : tblProposalPerson.getValueAt( selectedRow ,2 ).toString());
                Vector bioGraphyDataBean = (Vector)htPersonBiographyData.get( personId );
                if( bioGraphyDataBean!=null && bioGraphyDataBean.size()>selPersonBiographyRow ){
                    ProposalBiographyFormBean proposalBiographyFormBean =  
                            ( ProposalBiographyFormBean ) bioGraphyDataBean.get( selPersonBiographyRow );
                    if(proposalBiographyFormBean.getUpdateTimestamp()!=null){
                        txtUpdateUser.setText(UserUtils.getDisplayName(proposalBiographyFormBean.getUpdateUser())+ " at "+
                            CoeusDateFormat.format(proposalBiographyFormBean.getUpdateTimestamp().toString()));
                    }else{
                        txtUpdateUser.setText("");
                    } 
                    
                    if(proposalBiographyFormBean.getProposalPersonBioPDFBean()!=null && 
                        proposalBiographyFormBean.getProposalPersonBioPDFBean().getUpdateTimestamp()!=null){
                        txtLastDocUpdateUser.setText(proposalBiographyFormBean.getProposalPersonBioPDFBean().getUpdateUser() + " at "+
                            CoeusDateFormat.format(proposalBiographyFormBean.getProposalPersonBioPDFBean().getUpdateTimestamp().toString()));
                        txtFileName.setText(proposalBiographyFormBean.getProposalPersonBioPDFBean().getFileName());
                    } else {
                        txtLastDocUpdateUser.setText("");
                        txtFileName.setText("");
                    }
                }
            }
            //Added for case 2349 - Show timestamps in Proposal Personnel - end
            
        }else if((source.equals( bioSelectionModel)) && ( selPersonBiographyRow == -1 )
        && (htPersonData != null)) {
            //Commented for case 3685 - Remove Word icons - start
//            viewWordFiles.setEnabled(false);
            //Commented for case 3685 - Remove Word icons - end
            viewPdfFiles.setEnabled(false);
        }
        if(functionType==DISPLAY_MODE){
            if(tblPersonBiography.getSelectedRow() != -1) {
                viewPdfFiles.setEnabled(isPdf);
                //Commented for case 3685 - Remove Word icons - start
//                viewWordFiles.setEnabled(isWord);
                //Commented for case 3685 - Remove Word icons - end
            }else {
                viewPdfFiles.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - start
//                viewWordFiles.setEnabled(false);
                //Commented for case 3685 - Remove Word icons - end
            }
        }
        //bug fix id 1744 - step 8 : end
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblFileName;
    public javax.swing.JLabel lblLastDocument;
    public javax.swing.JLabel lblMessage;
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalValue;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JLabel lblUpdatedBy;
    public javax.swing.JPanel pnlMessage;
    public javax.swing.JPanel pnlProposalDescription;
    public javax.swing.JPanel pnlProposalDescriptionContainer;
    public javax.swing.JPanel pnlProposalPersonBiographyContainer;
    public javax.swing.JPanel pnlProposalPersonContainer;
    public javax.swing.JPanel pnlProposalPersons;
    public javax.swing.JScrollPane scrPnBiography;
    public javax.swing.JScrollPane scrPnProposalPerson;
    public javax.swing.JTable tblPersonBiography;
    public javax.swing.JTable tblProposalPerson;
    public edu.mit.coeus.utils.CoeusTextField txtFileName;
    public edu.mit.coeus.utils.CoeusTextField txtLastDocUpdateUser;
    public edu.mit.coeus.utils.CoeusTextField txtUpdateUser;
    // End of variables declaration//GEN-END:variables
    
    /** Allows to view the word document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewWordDocument() throws Exception{
        
        int selectedRow = tblPersonBiography.getSelectedRow();
        String personId = (String)((DefaultTableModel)
        tblPersonBiography.getModel()).
        getValueAt(selectedRow,4);
        ProposalPersonBioSourceBean sourceBean=null;
        Vector data= (Vector)htPersonBiographyData.get( personId );
        if(data!= null && data.size() >0){
           //Commented for pmd check unused local variable
           // AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            if( selectedRow != -1 ){
                sourceBean = new ProposalPersonBioSourceBean();
                ProposalBiographyFormBean personBioBean =
                (ProposalBiographyFormBean)data.get(selectedRow);
                
                sourceBean.setPersonId(personBioBean.getPersonId());
                sourceBean.setBioNumber(personBioBean.getBioNumber());
                sourceBean.setProposalNumber(proposalId);
                
            }
            
            RequesterBean request = new RequesterBean();
            //request.setFunctionType(VIEW_WORD);
            //request.setDataObject(sourceBean);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", sourceBean);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPersonPrintReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("REPORT_TYPE", ""+VIEW_WORD);
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator comm =
            new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                //                String url = (String)response.getDataObject();
                //                if(url== null )
                //                    return;
                //                url = url.replace('\\', '/');
                //                URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
                //                if(coeusContext != null){
                //                    coeusContext.showDocument( templateURL, "_blank" );
                //                }else{
                //                    javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                //                    bs.showDocument(templateURL);
                //                }
                map = (Map)response.getDataObject();
                String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                url = url.replace('\\', '/');
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }else{
                System.out.println("Error");
                CoeusOptionPane.showInfoDialog(response.getMessage());
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
    
    
    /** Allows to view the PDF document for the selected row
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
        int selectedRow = tblPersonBiography.getSelectedRow();
        if(selectedRow == -1){
            return ;
        }
        String personId = (String)((DefaultTableModel)
        tblPersonBiography.getModel()).
        getValueAt(selectedRow,4);
        ProposalPersonBioPDFBean bioPdfBean = null;
        Vector data= (Vector)htPersonBiographyData.get( personId );
        if(data!= null && data.size() > 0){
            //AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
            if( selectedRow != -1 ){
                bioPdfBean = new ProposalPersonBioPDFBean();
                ProposalBiographyFormBean pdfBean =
                (ProposalBiographyFormBean)data.get(selectedRow);
                
                bioPdfBean.setPersonId(pdfBean.getPersonId());
                bioPdfBean.setBioNumber(pdfBean.getBioNumber());
                bioPdfBean.setProposalNumber(proposalId);
                //COEUSDEV-139
                ProposalPersonBioPDFBean propPersonBioPDFBean = pdfBean.getProposalPersonBioPDFBean();
                if(propPersonBioPDFBean!=null && propPersonBioPDFBean.getFileBytes()!=null){
                    bioPdfBean.setFileBytes(propPersonBioPDFBean.getFileBytes());
                }
            }
            RequesterBean request = new RequesterBean();
            //request.setFunctionType(VIEW_PDF);
            //request.setDataObject(bioPdfBean);
            
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", bioPdfBean);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.propdev.ProposalPersonPrintReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("REPORT_TYPE", ""+VIEW_PDF);
            documentBean.setParameterMap(map);
            request.setDataObject(documentBean);
            request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator comm
            = new AppletServletCommunicator(STREAMING_SERVLET, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                //                String url = (String)response.getDataObject();
                //                if(url== null) return ;
                //                url = url.replace('\\', '/');
                //                URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
                //                if(coeusContext != null){
                //                    coeusContext.showDocument( templateURL, "_blank" );
                //                }else{
                //                    javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                //                    bs.showDocument(templateURL);
                //                }
                map = (Map)response.getDataObject();
                String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
                url = url.replace('\\', '/');
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }else{
                CoeusOptionPane.showInfoDialog(response.getMessage());
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
    
    
    
    /** Allows to upload the word document for the selected row
     * @throws Exception in case of error occured
     */
    private void uploadWordDocument() throws Exception{
        int selectedRow = tblPersonBiography.getSelectedRow();
        ProposalPersonBioSourceBean sourceBean=null;
        ProposalBiographyFormBean personBioBean = null;
        String acType = null;
        if( selectedRow != -1 ){
            //bug Fix : 2532 - START - 1
            if(!validateData()){
                throw new Exception(coeusMessageResources.parseMessageKey("protoDetFrm_exceptionCode.1130"),null);
            }
            //bug Fix : 2532 - END - 1
            String personId = (String)((DefaultTableModel)
            tblPersonBiography.getModel()).
            getValueAt(selectedRow,4);
            
            Vector data= (Vector)htPersonBiographyData.get( personId );
            if(data!= null && data.size() > 0){
                personBioBean = (ProposalBiographyFormBean)data.get(selectedRow);
            }
            acType = personBioBean.getAcType();
            if( null == personBioBean.getProposalPersonBioSourceBean()) {
                sourceBean = new ProposalPersonBioSourceBean();
            }else{
                sourceBean = personBioBean.getProposalPersonBioSourceBean();
            }
            
            sourceBean.setPersonId(personId);
            sourceBean.setBioNumber(personBioBean.getBioNumber());
            sourceBean.setProposalNumber(proposalId);
            
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
                            if( personBioBean.isHasBioSource() ){
                                sourceBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }else{
                                sourceBean.setAcType(TypeConstants.INSERT_RECORD);
                            }
                            if( null == acType ) {
                                personBioBean.setAcType( TypeConstants.UPDATE_RECORD );
                            }
                            sourceBean.setFileBytes(fileChooser.getFile());
                            //Added for case# 3450 - To save the file name to db - start
                            sourceBean.setFileName(fileChooser.getFileName().getName());
                            //Added for case# 3450 - To save the file name to db - end
                            personBioBean.setProposalPersonBioSourceBean(sourceBean);
                            data.setElementAt(personBioBean, selectedRow );
                            //htPersonBiographyData.put(personBioBean, data);
                            saveRequired = true;
                            setBiographyInfo();
                            //setEditors();
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
    
    
    private void uploadPdfDocument() throws Exception{
        int selectedRow = tblPersonBiography.getSelectedRow();
        ProposalPersonBioPDFBean sourceBean=null;
        ProposalBiographyFormBean personBioBean = null;
        String acType = null;
        if( selectedRow != -1 ){
            //bug Fix : 2532 - START - 2
            if(!validateData()){
                throw new Exception(coeusMessageResources.parseMessageKey("protoDetFrm_exceptionCode.1130"),null);
            }
            //bug Fix : 2532 - END - 2
            String personId = (String)((DefaultTableModel)
            tblPersonBiography.getModel()).
            getValueAt(selectedRow,4);
            Vector data= (Vector)htPersonBiographyData.get( personId );
            if(data!= null && data.size() > 0){
                personBioBean = (ProposalBiographyFormBean)data.get(selectedRow);
            }
            acType = personBioBean.getAcType();
            if( null == personBioBean.getProposalPersonBioPDFBean() ) {
                sourceBean = new ProposalPersonBioPDFBean();
            }else{
                sourceBean = personBioBean.getProposalPersonBioPDFBean();
            }
            
            sourceBean.setPersonId(personId);
            sourceBean.setBioNumber(personBioBean.getBioNumber());
            sourceBean.setProposalNumber(proposalId);
            
            fileChooser = new CoeusFileChooser(mdiForm);
            //Modified for case 3685 - Remove Word icons - end
            //fileChooser.setSelectedFileExtension(PDF_FORMAT);
            fileChooser.setAcceptAllFileFilterUsed(true);            
            //Modified for case 3685 - Remove Word icons - end
            fileChooser.showFileChooser();
            if(fileChooser.isFileSelected()){
                String fileName = fileChooser.getSelectedFile();
                if(fileName != null && !fileName.trim().equals("")){
                    int index = fileName.lastIndexOf('.');
                    if(index != -1 && index != fileName.length()){
                        String extension = fileName.substring(index+1,fileName.length());
                        //Modified for case 3685 - Remove Word icons - start
                        if(extension != null){// && (extension.equalsIgnoreCase(PDF_FORMAT) )){
                            //Modified for case 3685 - Remove Word icons - end
                            //Modified with coeusdev-139 : Allow multiple person attachments of same document type in Person Bio Module
                            if(!TypeConstants.INSERT_RECORD.equals(sourceBean.getAcType()) && personBioBean.isHasBioPDF() ){
                                sourceBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }else{
                                sourceBean.setAcType(TypeConstants.INSERT_RECORD);
                            }
                            if( null == acType ) {
                                personBioBean.setAcType( TypeConstants.UPDATE_RECORD );
                            }
                            sourceBean.setFileBytes(fileChooser.getFile());
                            //Added for case# 3450 - To save the file name to db - start
                            sourceBean.setFileName(fileChooser.getFileName().getName());
                            //Added for case# 3450 - To save the file name to db - end
                            //Added for case 4007: Icon based on mime Type : Start
                            CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                            sourceBean.setMimeType(docTypeUtils.getDocumentMimeType(sourceBean));
                            //4007 End
                            personBioBean.setProposalPersonBioPDFBean(sourceBean);
                            data.setElementAt(personBioBean, selectedRow );                            
                            saveRequired = true;   
                           //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
                            String replacedFileName = txtFileName.getText();
                          //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
                            setBiographyInfo();
                            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress  - Start                          
                            if((functionType == DISPLAY_MODE)  
                                    && getPersonnelRight()) {
                                try {
                                    if(htPersonData != null){
                                        Vector vecData = ((ProposalPersonFormBean) htPersonData.get(sourceBean.getPersonId())).getPropBiography();
                                        vecData = (vecData == null) ? new Vector() :vecData;
                                        for(int count = 0; count < vecData.size(); count++){
                                            personBioBean = (ProposalBiographyFormBean) vecData.get(count);
                                            if( personBioBean.getProposalPersonBioPDFBean() != null &&
                                                    personBioBean.getBioNumber() == sourceBean.getBioNumber()) {
                                                sourceBean = personBioBean.getProposalPersonBioPDFBean();
                                            }
                                        }
                                    }
                                    updateInboxDetails(sourceBean,replacedFileName);
                                }catch(CoeusClientException coeusClientException) {
                                    CoeusOptionPane.showDialog(coeusClientException);
                                }
                            }
                            //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
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
    
    
    //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
    
    public boolean getPersonnelRight(){
        boolean propPersonnel = false;
        if(proposalStatusCode == 2 || proposalStatusCode == 4){
                getPropPersonnelRights(proposalId);
                if(canModifyAnyProp || canModifyProp || canAlterProp){  
                    propPersonnel = true;
                }
        }
        return propPersonnel;
    }
    /*To send Notifications to all users who have already approved the proposal when a change is made to a module.
     *@param sourceBean contains the time at which the file was replaced.
     *@param replacedFileName is the file which was replaced.
     *@throws CoeusClientException
     */
    public void updateInboxDetails(ProposalPersonBioPDFBean sourceBean,String replacedFileName ) throws CoeusClientException {
        int selectedRow=tblPersonBiography.getSelectedRow();
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        CoeusGuiConstants.PROPOSAL_SERVLET ;
        
        RequesterBean request = new RequesterBean();
        String fromUser= sourceBean.getUpdateUser();
        
        String message = "File  '"+replacedFileName+"'  has been replaced by the user "+
                            sourceBean.getUpdateUser();
        
        request.setFunctionType( SEND_NOTIFICATION );
        request.setId(proposalId);
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
    //Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End
    
   // Case 3855 --- start  Replaced old Button editor class with the new button editor class with proper implementation.
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        
        private JButton btnAttachment;
        
        /** Creates a new instance of ButtonEditor */
        public ButtonEditor() {
            btnAttachment = new JButton();
            btnAttachment.addActionListener(this);
        }
        
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            try{
                cancelCellEditing();//COEUSDEV-138
                if( source.equals(btnAttachment) ){
                    if(btnAttachment.getIcon() !=null){
                        viewPdfDocument();
                    }
                }
//                Commented with COEUSDEV-138 - Java.Lang.String error deleting maintained bios
//                this.fireEditingStopped();
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
            //Case 4007: Icon based on mime Type
//            String fileExtension = (String) ((DefaultTableModel)tblPersonBiography.getModel()).getValueAt(row,6);
//            btnAttachment.setIcon(UserUtils.getAttachmentIcon(fileExtension));
//            return btnAttachment;
            CoeusAttachmentBean attachment = (CoeusAttachmentBean) ((DefaultTableModel)tblPersonBiography.getModel()).getValueAt(row,6);
            CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
            btnAttachment.setIcon(docTypeUtils.getAttachmentIcon(attachment));
            return btnAttachment;
            //4007 End
        }
    }
    
//    /**
//     * This inner class is used for setting the button editor to the
//     * first and second column for viewing the Word and PDf functionality.
//     * At present these features are not implemented.
//     */
//    
//    public class ButtonEditor extends DefaultCellEditor {
//        
//        protected JButton button;
//        private int selRow ;
//        private int selCol;
//        ImageIcon wordIcon=null, pdfIcon=null;
//        
//        public ButtonEditor(JCheckBox cf ) {
//            
//            super(cf);
//            wordIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON));
//            //Commented/Added for case#2156 - start
////            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
//            //Commented/Added for case#2156 - end
//            button = new JButton();
//            button.setOpaque(true);
//            //button.setEnabled(false);
//            button.addActionListener(new ActionListener(){
//                public void actionPerformed(ActionEvent actionEvent){
//                    try{
//                        cancelCellEditing();
//                        if( selCol == 1 ) {
//                            //Commented for case 3685 - Remove Word icons - start
////                            if(tblPersonBiography.getRowCount() > 0){
////                                viewWordDocument();
////                            }
//                            //Commented for case 3685 - Remove Word icons - end
//                        }else if( selCol == 2){
//                            if(tblPersonBiography.getRowCount() > 0){
//                                viewPdfDocument();
//                            }
//                        }
//                    }catch(Exception exception){
//                        exception.printStackTrace();
//                        CoeusOptionPane.showInfoDialog(exception.getMessage());
//                    }
//                  /*CoeusOptionPane.showErrorDialog(
//                                    coeusMessageResources.parseMessageKey(
//                                        "funcNotImpl_exceptionCode.1100"));*/
//                    //CoeusOptionPane.showErrorDialog("Functionalty not implemented");
//                }
//            });
//        }
//        /** This overridden to get the custom cell component in the
//         * JTable.
//         * @param table JTable instance for which component is derived
//         * @param value object value.
//         * @param isSelected particular table cell is selected or not
//         * @param row row index
//         * @param column column index
//         * @return a Component which is a editor component to the JTable cell.
//         */
//        public Component getTableCellEditorComponent(JTable table, Object value,
//        boolean isSelected, int row, int column) {
//            
//            this.selRow = row;
//            this.selCol = column;
//            boolean showIcon=false;
//            if( value != null ) {
//                showIcon = ( (Boolean) value ).booleanValue();
//            }
//            if( showIcon ) {
//                if( column == 1) {
//                    //Commented for case 3685 - Remove Word icons - start
////                    button.setIcon( wordIcon );
//                    //Commented for case 3685 - Remove Word icons - end
//                }else{
//                    button.setIcon( pdfIcon );
//                }
//            }else{
//                button.setIcon( null );
//            }
//            return button;
//        }
//        
//        // This overridden method is to enable editor in a single mouse click
//        public int getClickCountToStart(){
//            return 1;
//        }
//        /**
//         * Forwards the message from the CellEditor to the delegate. Tell the
//         * editor to stop editing and accept any partially edited value as the
//         * value of the editor.
//         * @return true if editing was stopped; false otherwise
//         */
//        public boolean stopCellEditing() {
//            cancelCellEditing();
//            return super.stopCellEditing();
//        }
//        
//        protected void fireEditingStopped() {
//            cancelCellEditing();
//            super.fireEditingStopped();
//        }
//    }
//    
    
    
    
    
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {
        
        public ColumnValueRenderer() {
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public Component getTableCellRendererComponent(JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           
            setText( (value ==null) ? "" : value.toString() );

            return this;
        }
    }
    
    public class ColumnValueEditor extends  AbstractCellEditor//DefaultCellEditor
    implements TableCellEditor, ItemListener{
        private JTextField txtDesc;
        
        private CoeusComboBox cmbDocType;
        private int selectedRow ;
        private int selectedCol ;
        private String stDescription;
        private String stPersonId;
        boolean temporary;
        
        ColumnValueEditor(int len ){
            instanceCreated = true;
            //super(new JTextField());
            cmbDocType = new CoeusComboBox();
            cmbDocType.addItemListener(this);
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
            txtDesc.setDocument(new LimitedPlainDocument(len));
            txtDesc.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    
                    if (!fe.isTemporary() ){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){
            
            selectedRow = row;
            selectedCol = column;
            stDescription = (String)tblPersonBiography.getValueAt(row,3);
            stPersonId = (String)tblPersonBiography.getValueAt(row,4);
            
            if(stDescription == null){
                stDescription = "";
            }
            
            if(selectedCol == 3){
                String newValue = ( String ) value ;
                if( newValue != null && newValue.length() > 0 ){
                    txtDesc.setText( (String)newValue );
                }else{
                    txtDesc.setText("");
                }
                this.selectedRow = row;
                return txtDesc;
            }else if(selectedCol == 5){
                if(instanceCreated) {
                    populateDocumentTypeCode();
                    typeComboPopulated = true;
                    instanceCreated = false;
                }
                cmbDocType.setSelectedItem(value);
                return cmbDocType;
            }
            return cmbDocType;
        }
        /** Populate the document type codes available for the persons
         */
        public  void populateDocumentTypeCode(){
            int size = vecDocumentType.size();
            ComboBoxBean comboBoxBean;
            cmbDocType.addItem(new ComboBoxBean("",""));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)vecDocumentType.get(index);
                cmbDocType.addItem(comboBoxBean);
            }
        }
        
        public int getClickCountToStart(){
            return 1;
        }
        
        public boolean stopCellEditing() {
            validateEditorComponent();
            return super.stopCellEditing();
        }
        
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
        
        /** Returns the value contained in the editor.
         * @return a value contained in the editor
         */
        public Object getCellEditorValue() {
            if(selectedCol == 3){
                return ((JTextField)txtDesc).getText();
            }else if(selectedCol == 5){
                Object data = ((CoeusComboBox)cmbDocType).getSelectedItem();
                // Commented for internal issue fix 135 Class Cast exception while clicking on down arrow start
//                String selectedValue = "";
//                if(data!= null){
//                    selectedValue = data.toString();
//                }
                 // Commented for internal issue fix 135 Class Cast exception while clicking on down arrow end
                return data;
            }
            return cmbDocType.getSelectedItem();
        }
        
        private void validateEditorComponent(){
            temporary = true;
             // Modified for internal issue fix 135 Class Cast exception while clicking on down arrow start
            Object objEditingValue = (Object) getCellEditorValue();
            String editingValue = "";
            if(selectedCol == 5){
                ComboBoxBean cmbBean =(ComboBoxBean)objEditingValue;
                editingValue = cmbBean.getDescription();
            }else{
                editingValue = (String)objEditingValue;
            }
//            String editingValue = (String) getCellEditorValue();
             // Modified for internal issue fix 135 Class Cast exception while clicking on down arrow end
            if (editingValue != null && editingValue.trim().length() > 0) {
                if(!(selectedCol == 3 && editingValue.equalsIgnoreCase(stDescription))){
                    setModel(editingValue);
                }
            }
        }
        
        private void setModel(String editingValue){
            //System.out.println("In Side setModel");
            saveRequired=true;
            if(stPersonId != null){
                ProposalBiographyFormBean proposalPerBioFormBean =
                (ProposalBiographyFormBean)((Vector) htPersonBiographyData.get(stPersonId)).get(selectedRow);
                String aType = proposalPerBioFormBean.getAcType();
                if(selectedCol == 3 ){
                    proposalPerBioFormBean.setDescription(editingValue);
                }else if(selectedCol == 5){
                    int code = Integer.parseInt(((ComboBoxBean)cmbDocType.getSelectedItem()).getCode());
                    String desc = ((ComboBoxBean)cmbDocType.getSelectedItem()).getDescription();
                    proposalPerBioFormBean.setDocumentTypeCode(code);
                    proposalPerBioFormBean.setDocumentTypeDescription(desc);
                }
                if(aType != null){
                    if(!aType.equalsIgnoreCase(INSERT_RECORD)){
                        proposalPerBioFormBean.setAcType(UPDATE_RECORD);
                    }
                }else{
                    proposalPerBioFormBean.setAcType(UPDATE_RECORD);
                }
                
            }
        }
        /** Listener for the document type combobox values
         */
        public void itemStateChanged(ItemEvent itemEvent) {
            Object source = itemEvent.getSource();
            if(source.equals(cmbDocType)){
                if(itemEvent.getStateChange() == ItemEvent.DESELECTED){
                    stopCellEditing();
                }
            }
        }
        
    }
    
    public Vector getProposalPersonBioData(){
        
        Vector vecBiographyToServer = new Vector();
        if(vecDeletedBiographyData != null){
            
            int delSize = vecDeletedBiographyData.size();
            ProposalBiographyFormBean bioBean = null;
            for(int index = 0; index < delSize; index++){
                bioBean = (ProposalBiographyFormBean)vecDeletedBiographyData.get(index);
                if(bioBean != null && vecBiographyToServer !=null){
                    vecBiographyToServer.insertElementAt(bioBean,index);
                }
            }
        }
        /*
        if(htPersonBiographyData != null){
         
            Enumeration enum = htPersonBiographyData.keys();
            while (enum.hasMoreElements()){
                String personId = (String)enum.nextElement();
                Vector vecBioForPerson = (Vector)htPersonBiographyData.get(personId);
                if(vecBioForPerson != null){
                    int bioSize = vecBioForPerson.size();
                    for(int index = 0 ; index < bioSize ; index++){
         
                        ProposalBiographyFormBean bioBean = (ProposalBiographyFormBean)vecBioForPerson.get(index);
                        vecBiographyToServer.addElement(bioBean);
                    }
                }
            }
        }*/
        // Handle Deleted Biography Information and send it to server...
        //printVecToServer(vecBiographyToServer);
        return vecBiographyToServer;
    }
    
    private void printVecToServer(Vector vecBiographyToServer){
        //System.out.println("From printVecToServer");
        //System.out.println("**********************");
        if(vecBiographyToServer != null){
            int bioSize = vecBiographyToServer.size();
            for(int index = 0 ; index < bioSize ; index++){
                
                ProposalBiographyFormBean bioBean = (ProposalBiographyFormBean)vecBiographyToServer.get(index);
                //Commented empty if block as per pmd check
               // if(bioBean != null){
                    //System.out.println("~~~~~~~~~~~~~");
                    //System.out.println("personId is "+bioBean.getPersonId());
                    //System.out.println("Bio Description is "+bioBean.getDescription());
                    //System.out.println("Actype is "+bioBean.getAcType());
                    //System.out.println("~~~~~~~~~~~~~");
               // }
            }
        }
    }
    
    /** Getter for property proposalPersonData.
     * @return Value of property proposalPersonData.
     */
    public java.util.Vector getProposalPersonData() {
        return proposalPersonData;
    }
    
    /** Setter for property proposalPersonData.
     * @param proposalPersonData New value of property proposalPersonData.
     */
    public void setProposalPersonData(java.util.Vector proposalPersonData) {
        this.proposalPersonData = proposalPersonData;
    }
    
    /** Make server call to get all the document type codes
     *for the person modules
     */
    private Vector getDocumentTypeCodes() {
        Vector dataObjects = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_PERSON_DOC_CODE);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            if (response.isSuccessfulResponse()){
                dataObjects = response.getDataObjects();
            }
        }
        return dataObjects;
    }
    
    // This method is used to display the biographical information
    
    private Vector getProposalPersonBiographyData(String proposalNumber){
        
        Vector vecProposalPersonBiographyData= null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROP_MAINTENANCE_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(proposalNumber);
        request.setDataObject(unitNumber);//Unit Number
        request.setFunctionType(GET_PROPOSAL_PERSON_BIOGRAPHY_DATA);
        //request.setDataObject(GET_PROPOSAL_PERSON_BIOGRAPHY_DATA);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        CoeusVector cvPropHierLink = null;
        if (response!=null){
            if (response.isSuccessfulResponse()){
                Vector serverDataObject = (Vector)response.getDataObject();
                //personId = (String)response.getId();
                if(serverDataObject != null){
                    vecProposalPersonBiographyData = (Vector)serverDataObject.elementAt(0);
                    //userCanMaintainUnit = ((Boolean)(serverDataObject.elementAt(1))).booleanValue();
                    //userCanMaintainSelectedPerson = ((Boolean)(serverDataObject.elementAt(2))).booleanValue();
                    canModifyProposal = ((Integer)(serverDataObject.elementAt(1))).intValue();
                    cvPropHierLink = ((CoeusVector)serverDataObject.elementAt(2));
                    //System.out.println("canModifyProposal Value is "+canModifyProposal);
                }
            }
        }
        //Code added for Case# 3183 - Proposal Hierarchy - starts
        hmPropHierLink = new HashMap();
        if(cvPropHierLink != null){
            Equals eqLinkType = new Equals("linkType", "A");
            cvPropHierLink = cvPropHierLink.filter(eqLinkType);
            if(cvPropHierLink != null && cvPropHierLink.size() >0){
                for(int index = 0; index < cvPropHierLink.size(); index++){
                    ProposalHierarchyLinkBean linkBean =
                    (ProposalHierarchyLinkBean) cvPropHierLink.get(index);
                    hmPropHierLink.put(""+linkBean.getPersonId(),
                    ""+linkBean.getChildProposalNumber());
                }
            }
        }
        //Code added for Case# 3183 - Proposal Hierarchy - ends
        return vecProposalPersonBiographyData;
    }
    
    public void saveAsActiveSheet() {
    }
    
      // Case# 3855: Replaced old ModuleTableRenderrer and ModuleTableEditor with the New cladsses.- Start
     // Reason : 
    /** Represents the Table renderrer adeed for 0th and 1st column */
    class ButtonRenderer extends JButton implements TableCellRenderer {
        
        /** Creates a new instance of ModuleTableRenderer */
        public ButtonRenderer() {
            this.setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);  // so JLabel background is painted
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {   
            //Modified with case 4007: Icon based on mime Type: Start
            CoeusAttachmentBean attachment = (CoeusAttachmentBean) ((DefaultTableModel)tblPersonBiography.getModel()).getValueAt(row,6);
            CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
            this.setIcon(docTypeUtils.getAttachmentIcon(attachment));           
            return this;
            //4007:End
        }
    }    
    
//    // This inner class is used for rendering the Button to the Column 1 and 2 in the Table
//    
//    public class ButtonRenderer extends JButton implements TableCellRenderer {
//        
//        ImageIcon wordIcon=null, pdfIcon=null;
//        boolean showIcon=false;
//        
//        //Vector vecDataPop = null;
//        public ButtonRenderer() {
//            setOpaque(true);
//            wordIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.WORD_ICON));
//            //Commented/Added for case#2156 - start
////            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
//            pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
//            //Commented/Added for case#2156 - end
//        }
//        /** An overridden method to render the component(icon) in cell.
//         * foreground/background for this cell and Font too.
//         *
//         * @param table the JTable that is asking the renderer to draw;
//         * can be null
//         * @param value the value of the cell to be rendered. It is up to the
//         * specific renderer to interpret and draw the value. For example,
//         * if value is the string "true", it could be rendered as a string or
//         * it could be rendered as a check box that is checked. null is a
//         * valid value
//         * @param isSelected true if the cell is to be rendered with the
//         * selection highlighted; otherwise false
//         * @param hasFocus if true, render cell appropriately. For example,
//         * put a special border on the cell, if the cell can be edited, render
//         * in the color used to indicate editing
//         * @param row the row index of the cell being drawn. When drawing the
//         * header, the value of row is -1
//         * @param column the column index of the cell being drawn
//         * @return Component which is to be rendered.
//         * @see TableCellRenderer#getTableCellRendererComponent(JTable, Object,
//         * boolean, boolean, int, int)
//         */
//        public Component getTableCellRendererComponent(JTable table,
//        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            if( value != null ) {
//                showIcon = ( (Boolean) value ).booleanValue();
//            }
//            if( showIcon ) {
//                if( column == 1) {
//                    //Commented for case 3685 - Remove Word icons - start
////                    setIcon( wordIcon );
//                    //Commented for case 3685 - Remove Word icons - end
//                }else if(column==2){
//                    setIcon( pdfIcon );
//                }
//            }else{
//                setIcon( null );
//            }
//            return this;
//        }
//        
//    }
    
    private Vector getSyncData(String proposalNumber, String personId){
        Vector data = new Vector();
        Vector vecGetData = new Vector();
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        data.addElement(proposalNumber);
        data.addElement(personId);
        requester.setFunctionType(SYNC);
        requester.setDataObjects(data);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(CONNECTION_STRING, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            vecGetData = (Vector)responder.getDataObjects();
            System.out.println("Success");
        }else{
            
            System.out.println(" ");
        }
        return vecGetData;
    }
    //Added for the Coeus Enhancemnt case:#1767
    
    //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - Start
    
    /*
     * This method is used to check if the user has either
     * MODIFY_ANY_PROPOSAL or MODIFY_PROPOSAL or ALTER_PROPOSAL_DATA rights
     * @param proposalNumber is the proposal number.
     */
    private void getPropPersonnelRights(String proposalNumber){
        Map hmPersonnelRight = new HashMap();        
        Vector data = new Vector();
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        data.addElement(proposalNumber);
        data.addElement(CoeusGuiConstants.getMDIForm().getUserId());
        data.addElement(mdiForm.getUnitNumber());        
        data.addElement("MODIFY_ANY_PROPOSAL");
        data.addElement("MODIFY_PROPOSAL");
        data.addElement("ALTER_PROPOSAL_DATA");
        requester.setFunctionType(GET_PROP_PERSONNEL_RIGHTS);
        requester.setDataObject(data);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROP_MAINTENANCE_SERVLET;
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            hmPersonnelRight = (HashMap)responder.getDataObject();
            canModifyAnyProp = ((Boolean)hmPersonnelRight.get("MODIFY_ANY_PROPOSAL")).booleanValue();
            canModifyProp = ((Boolean)hmPersonnelRight.get("MODIFY_PROPOSAL")).booleanValue();
            canAlterProp = ((Boolean)hmPersonnelRight.get("ALTER_PROPOSAL_DATA")).booleanValue();
        }
    }
    
    /*
     * Save the proposal personnel data to the DB
     * @throws Exception
     */
    private void saveProposalPersonnel() throws Exception{
        boolean alterProposalPerson = false;
        Vector vecBioDeletedData = getProposalPersonBioData();
        Vector vecDataToServer = new Vector();
        
        vecDataToServer.addElement(vecBioDeletedData);
        vecDataToServer.addElement(htPersonBiographyData);
        vecDataToServer.addElement(proposalId);
        vecDataToServer.addElement(htPersonData);
        vecDataToServer.addElement(new Integer(proposalStatusCode));
        vecDataToServer.addElement(mdiForm.getUnitNumber());
        if(functionType == DISPLAY_MODE && proposalStatusCode == 2 || proposalStatusCode == 4){
            alterProposalPerson = true;
        }
        vecDataToServer.addElement(new Boolean(alterProposalPerson));
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL +PROP_MAINTENANCE_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(SAVE_PROPOSAL_PERSON_BIOGRAPHY_DETAILS);
        request.setDataObject(vecDataToServer);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            
            vecDataToServer = (Vector)response.getDataObjects();
            proposalPersonData = vecDataToServer;
            htPersonData = constructPersonHashtable(proposalPersonData);
            setFormData();
            saveRequired = false;
            this.vecDeletedBiographyData.removeAllElements();
        } else {
            
            CoeusOptionPane.showErrorDialog(response.getMessage());
            if (response.isCloseRequired()) {
                this.doDefaultCloseAction();
                return;
            }else {
                throw new Exception(response.getMessage());
            }
        }
    }
    //Added for Case#3577 - Replace Personnel Attachments while Proposal Development is Approval in Progress - End    
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
    
    //Added for bug id 1856 step 3 : start
    /**
     * Getter for property alterProposalRight.
     * @return Value of property alterProposalRight.
     */
    public boolean isAlterProposalRight() {
        return alterProposalRight;
    }
    
    /**
     * Setter for property alterProposalRight.
     * @param alterProposalRight New value of property alterProposalRight.
     */
    public void setAlterProposalRight(boolean alterProposalRight) {
        this.alterProposalRight = alterProposalRight;
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
    
    //bug id 1856 step 3 : end
    
}
