/**
 * @(#)ProcessAction.java  1.0 September 1, 2003
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * ProcessAction.java
 *
 * Created on September 1, 2003, 3:46 PM
 */

/* PMD check performed, and commented unused imports and variables on 19-JUN-2007
 * by Leena
 */

package edu.mit.coeus.irb.gui;

//import java.util.Vector;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.irb.controller.ProtocolMailController;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
//import edu.mit.coeus.irb.bean.ProtocolActionsBean ;
//import edu.mit.coeus.irb.bean.ProtocolActionChangesBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.gui.ScheduleActionInputForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.CustomTagScanner;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ReportGui;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.ByteArrayInputStream;
//import java.awt.Cursor;

/**
 *
 * @author  Vyjayanthi
 */
public class ProcessAction implements ProtocolActionsInterface {
    
    static private ProcessAction instance;       // The single instance
    
    private final String PROTOCOL_ACTION_SERVLET = "/protocolActionServlet";
    
//	private final static char ACTION_MENU_ITEM = 'X' ;
//        private final static char ACTION_MENU_ITEM_TAGS = 'x' ;
    private final static char GET_CUSTOM_TAGS = 't' ;
    private final static char VALID_STATUS_CHANGE = 'T' ;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    int actionCode, sequenceNumber;
    
    String protocolId;
    String actionDescription;
//        private boolean lockSchedule = true;
//        private boolean releaseLock;
    private String msgPrompt = "";
    private String strTitle = "";
    private String strDefault = "";
    private ScheduleActionInputForm inputForm ;
    private String userInput = null;
    private Vector reviewComments = null;
    
    //Added for performing request and non request actions - start - 1
    private String committeeId = "";
    //Added for performing request and non request actions - end - 1
    
    /**
     * Returns the single instance, creating one if it's the
     * first time this method is called.
     *
     * @return ProcessAction The single instance.
     */
    static synchronized public ProcessAction getInstance() {
        if (instance == null) {
            instance = new ProcessAction();
        }
        return instance;
    }
    
    public ProtocolActionChangesBean processRequest( int actionCode, int sequenceNumber,
            String protocolId, String comments, String actionDescription ) {
        ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
        actionBean.setActionTypeCode(actionCode) ;
        actionBean.setSequenceNumber(sequenceNumber) ;
        actionBean.setProtocolNumber(protocolId) ;
        actionBean.setComments( comments );
        //Set Description as well - Prasanna
        actionBean.setActionTypeDescription(actionDescription);
        return processRequest( actionBean );
    }
    /**
     * A method to perform request action
     */
    public ProtocolActionChangesBean processRequest(ProtocolActionsBean actionBean) {
        int opt = CoeusOptionPane.DEFAULT_NO;
        opt = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("processAction_exceptionCode.1020"),
                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        if( opt == CoeusOptionPane.SELECTION_YES ) {
            return performAction(actionBean);
        }
        return null;
    }
    
    /**
     * Method used to process the action requested by the user. User can pass directly
     * the action bean with all the parameters set to it.
     */
    
    private ProtocolActionChangesBean performAction( ProtocolActionsBean actionBean ) {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_CUSTOM_TAGS);
        requester.setId(actionBean.getProtocolNumber());
        Vector dataObjects = new Vector();
        dataObjects.addElement( actionBean );
        dataObjects.addElement( new Boolean( false ) );
        requester.setDataObjects( dataObjects ) ;
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_ACTION_SERVLET ;
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        
        //Bijosh
        ResponderBean response = showCustomizeWindow(requester,dataObjects);//Bijosh
        if (response == null) {
            requester.setFunctionType('X');
            comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            response = comm.getResponse();
        }else if (response.getMessage() !=null && response.getMessage().equals("NO_TAGS")) {
            //String connectTo =CoeusGuiConstants.CONNECTION_URL
            // + PROTOCOL_ACTION_SERVLET ;
            requester.setFunctionType('X');
            comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            response = comm.getResponse();
        }
        //Bijosh
        
        //response = comm.getResponse(); // removed the declaration by bijosh and to  be removed
        if (response.isSuccessfulResponse()) {
            //report that action was successful
            //CoeusOptionPane.showInfoDialog("Requested action on Protocol" + protocolId + " completed successfully") ;
            Object responseObject = response.getDataObject();
            Vector responseVec = (Vector)responseObject;
            if ( responseVec != null  && responseVec.size() >=4  && responseVec.get(3) != null ){
                Vector docList = (Vector) responseVec.elementAt(3);
                DocumentList documentList = new DocumentList(CoeusGuiConstants.getMDIForm(),true);
                documentList.setProtocolNumber(actionBean.getProtocolNumber());
                documentList.setMenuAction(actionBean.getActionTypeDescription());
                //Added for Coeus4.3 enhancement - Email Notification - start
                documentList.setActionBean(actionBean);
                //Added for Coeus4.3 enhancement - Email Notification - end
                documentList.loadForm(docList);
                documentList.display();
            }else{
                //CoeusOptionPane.showInfoDialog("This protocol has no Documents");
                CoeusOptionPane.showInfoDialog("Requested action on Protocol " + actionBean.getProtocolNumber() + " completed successfully") ;
            }
            if( responseVec.size() > 4 ){
                //prps start dec 12 2003
                // to add follow up action vector
                ProtocolActionChangesBean protocolActionChangesBean
                        = (ProtocolActionChangesBean) responseVec.elementAt(4) ;
                if (responseVec.elementAt(0) != null) {
                    Vector vecActionDetails = new Vector() ;
                    vecActionDetails.add(0, (Vector)responseVec.elementAt(0) ) ;
                    vecActionDetails.add(1, (HashMap)responseVec.elementAt(1) ) ;
                    vecActionDetails.add(2, (HashMap)responseVec.elementAt(2) ) ;
                    protocolActionChangesBean.setFollowupAction(vecActionDetails) ;
                } else {
                    protocolActionChangesBean.setFollowupAction(new Vector() ) ;
                }
                return protocolActionChangesBean ;
                //prps end dec 12 2003
                
                // return (ProtocolActionChangesBean) ; // prps commented this line dec 12 2003
            }else{
                return null;
            }
        }else { // report that action was not successful
            //CoeusOptionPane.showWarningDialog("Requested action on Protocol" + protocolId + " failed") ;
            // added by manoj to display action messages as information messages
            if(response.getDataObject() != null){
                CoeusClientException clientException = new
                        CoeusClientException((CoeusException)response.getDataObject());
                if(clientException.getMessageType() == clientException.INFORMATION_MESSAGE){
                    CoeusOptionPane.showInfoDialog(clientException.getMessage());
                }else if(clientException.getMessageType() == clientException.WARNING_MESSAGE){
                    CoeusOptionPane.showWarningDialog(clientException.getMessage());
                }else{
                    CoeusOptionPane.showErrorDialog(clientException.getMessage());
                }
            }else{
                CoeusOptionPane.showWarningDialog(response.getMessage());
            }
            return null;
        }
    }
    //Bijosh
    private ResponderBean showCustomizeWindow(RequesterBean request,Vector data) {
        try {
            Vector dataObjects = data;
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + PROTOCOL_ACTION_SERVLET ;
//                Vector sendToServer = new Vector();
            byte[] stream = null;
            String startingCutomTag = null;
            String endingCutomTag = null;
            ByteArrayInputStream byteArrayInputStream=null;
//                String scheduleID = null;
//                String committeeId = null;
            String protoCorrespTypeDesc= null;
            // int selRow =  tblAdhocList.getSelectedRow() ;
            //this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            AppletServletCommunicator comm = new AppletServletCommunicator(
                    connectTo, request );
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response!= null){
                if(response.isSuccessfulResponse()){
                    Vector dataFromServerVec =(Vector)response.getDataObject();
                    if (dataFromServerVec !=null) {
                        stream = (byte[])dataFromServerVec.get(0);// xsl stream
                        startingCutomTag = (String)dataFromServerVec.get(1);// Starting tag
                        endingCutomTag = (String)dataFromServerVec.get(2);// Ending tag
                        protoCorrespTypeDesc=(String)dataFromServerVec.get(3);//Get the protoCorresTypeDesc
                    }
                    if (stream != null) {
                        byteArrayInputStream = new ByteArrayInputStream(stream);
                    }
                }else {
                    if (response == null) {
                        response.setMessage("NO_TAGS");
                        return response;
                    }
                    if (response.getMessage().equals("NO_TAGS")) {
                        return response;
                    }
                    CoeusOptionPane.showErrorDialog(response.getMessage());
                }
            }
            ResponderBean responderBean = null;
            if (byteArrayInputStream != null) {
//                    java.io.BufferedWriter modifiedXsl = null;
//                    String readtext="";
//                    java.io.DataInputStream dataInputStream = new java.io.DataInputStream(byteArrayInputStream);
                CustomTagScanner customTagScanner = new CustomTagScanner();
                CoeusVector cvCustomTags = customTagScanner.stringScan(stream,startingCutomTag,endingCutomTag);
                // check for the custom tags. If present then popup the window for the corresponsding
                //tags. If not then generate pdf without popping up the window
                if(cvCustomTags!= null && cvCustomTags.size()>0){
                    ReportGui  reportGui = new ReportGui(CoeusGuiConstants.getMDIForm(),protoCorrespTypeDesc);
                    reportGui.setTemplateData(cvCustomTags);
                    reportGui.postInitComponents();
                    int action = reportGui.displayReportGui();
                    if(action ==reportGui.CLICKED_OK){
                        Hashtable htData = reportGui.getXslData();
                        byte[] customData = customTagScanner.replaceContents(htData);
                        //adhocDetailsBean.setFormId(tblAdhocList.getValueAt(selRow, 0).toString()) ;
                        // adhocDetailsBean.setDescription(tblAdhocList.getValueAt(selRow, 1).toString()) ;
                        dataObjects.add(customData);
                        request.setFunctionType('x');
                        request.setDataObjects(dataObjects);
                        responderBean= generatePdfForTags(request);
                    }else{
                        // Do the usual action
                        //scheduleXMLGenerator('M');
                    }
                }else{
                    // Do the usual action
                    // showDocument();
                }
            }
            return responderBean;
        } catch (Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
            return null;
        }
    }
    //Bijosh
    
    //Bijosh
    private ResponderBean generatePdfForTags(RequesterBean request) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_ACTION_SERVLET ;
//            boolean success=false;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
    }
    //Bijosh
    /**
     * A method to check if the selected action can be performed
     */
    public boolean canPerformAction(int actionCode, int sequenceNumber, String protocolId,
            String scheduleId, String actionDescription) throws Exception {
        ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
        actionBean.setActionTypeCode(actionCode) ;
        actionBean.setSequenceNumber(sequenceNumber) ;
        actionBean.setProtocolNumber(protocolId) ;
        actionBean.setScheduleId(scheduleId);
        actionBean.setActionTypeDescription(actionDescription);
        return canPerformAction( actionBean );
    }
    public boolean canPerformAction( ProtocolActionsBean actionBean ) throws Exception {
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(VALID_STATUS_CHANGE);
        requester.setId(actionBean.getProtocolNumber());
        requester.setDataObject(actionBean) ;
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_ACTION_SERVLET ;
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            //report that action was successful
            //CoeusOptionPane.showInfoDialog("Requested action on Protocol" + protocolId + " completed successfully") ;
            return true;
        } else {
            //CoeusOptionPane.showWarningDialog("Requested action on Protocol" + protocolId + " failed") ;
            //CoeusOptionPane.showWarningDialog(response.getMessage());
            //modified by manoj to handle protocol actions - 12/09/2003
            if(response.getDataObject() != null){
                throw (CoeusException)response.getDataObject();
            }else{
                CoeusOptionPane.showWarningDialog("Response"+response.getMessage());
            }
        }
        return false;
        
        
    }
    public ProtocolActionChangesBean performOtherAction( ProtocolActionsBean actionBean, boolean lockSchedule ) throws Exception {
        System.out.println("*** In performOtherAction with action type code " + actionBean.getActionTypeCode() + " ***") ;
        boolean canPerform = false ;
        //Added by Jobin For the Action Date
        java.sql.Date actionDate = null; // - end
        // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        //protocol actiosn will have call to canperformaction check in actionTransaction object
        // so no need to check that here
        if (actionBean.getActionTypeCode() == 104 ||
                actionBean.getActionTypeCode() == 105 ||
                actionBean.getActionTypeCode() == 106 ||
                actionBean.getActionTypeCode() == 108 ||
                actionBean.getActionTypeCode() == 300 ||
                actionBean.getActionTypeCode() == 301 ||
                actionBean.getActionTypeCode() == 302 ||
                actionBean.getActionTypeCode() == ProtocolActionsInterface.ACTION_PROTOCOL_SUSPEND_BY_DSMB ||
                actionBean.getActionTypeCode() == ProtocolActionsInterface.ACTION_PROTOCOL_EXPIRE ||
                //Added for performing request and non request actions - start - 2
                actionBean.getActionTypeCode() == 114 ||
                actionBean.getActionTypeCode() == 115 ||
                actionBean.getActionTypeCode() == 116 ||
                actionBean.getActionTypeCode() == 211 ||
                actionBean.getActionTypeCode() == 212 ||
                actionBean.getActionTypeCode() == ProtocolActionsInterface.ACTION_PROTOCOL_ABANDON ) {
            // Modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
            //Added for performing request and non request actions - end - 2
//            System.out.println("** perform protocol action **") ;
            if(!canPerformAction( actionBean )){
                return null;
            }
            // code added by nadh for enhancement to bring up  protocol action window
            // start jul 30 2004
            strTitle = coeusMessageResources.parseMessageKey(
                    "actionInputFrm_TitleCode." + actionBean.getActionTypeCode() + "1")
                    + "-" + actionBean.getProtocolNumber() ;
            msgPrompt = coeusMessageResources.parseMessageKey(
                    "actionInputFrm_PromptCode." + actionBean.getActionTypeCode() + "2") ;
            strDefault = coeusMessageResources.parseMessageKey(
                    "actionInputFrm_DefaultCode." + actionBean.getActionTypeCode() + "3") ;
            
            inputForm = new ScheduleActionInputForm( CoeusGuiConstants.getMDIForm(), strTitle, msgPrompt,strDefault) ;
            inputForm.setLockSchedule( lockSchedule );
            inputForm.setActionBean( actionBean );
            // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved - Start
            if(actionBean.getActionTypeCode() == ProtocolActionsInterface.ACTION_PROTOCOL_ABANDON){
                inputForm.setReviewCommentsButtonVisibility(false);
            }
            // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved - End
            //Added for performing request and non request actions - start - 3
            int actionId = actionBean.getActionTypeCode();
            if(actionId != 104 &&
                    actionId != 105 &&
                    actionId != 106 &&
                    actionId != 107 &&
                    actionId != 108 &&
                    actionId != 114 &&
                    actionId != 115 &&
                    //Code added for Case#3554 - Notify IRB enhancement
                    actionId != 116){
                inputForm.setVisibility();
            }
            //Added for performing request and non request actions - end - 3
           /*
            *   Disable the review comments button for all the request actions
            *   Disable the review comments button, if there is no submission
            *   associated with the protocol, since submission should always go
            *   against the submission number.
            */
            if (actionBean.getActionTypeCode() == 104 ||
                    actionBean.getActionTypeCode() == 105 ||
                    actionBean.getActionTypeCode() == 106 ||
                    actionBean.getActionTypeCode() == 108 ||
                    //Added for performing request and non request actions - start - 4
                    actionBean.getActionTypeCode() == 114 ||
                    actionBean.getActionTypeCode() == 115){
                //Added for performing request and non request actions - end - 4
                inputForm.disableReviewCommBtn(true);
            }else if(actionBean.getSubmissionNumber()==0){
                inputForm.disableReviewCommBtn(true);
            }else{
                inputForm.disableReviewCommBtn(false);
            }
            inputForm.showForm() ;
            userInput = inputForm.getUserInput() ;
            reviewComments = inputForm.getReviewComments();
            //Added for performing request and non request actions - start - 5
            committeeId = inputForm.getCommitteeId();
            //Added for performing request and non request actions - end - 5
            actionDate = inputForm.getActionDate();//Added by Jobin For the Action Date
            //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
            //Modified to set multiple documents in the bean
            //Added for case#3046 - Notify IRB attachments - start
//            actionBean.setFileName(inputForm.getFileName());
//            actionBean.setFileBytes(inputForm.getFileBytes());            
            actionBean.setActionDocuments(inputForm.getUploadDocuments());
            //COEUSDEV-328 : End
            //Added for case#3046 - Notify IRB attachments - end
            //Modified with case 4007: Icon based on attachment type
            //Commented for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
            //No attachments are handled in ProtocolActionsBean
//            CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
//            actionBean.setMimeType(docUtils.getDocumentMimeType(actionBean));
            //COEUSDEV-328 : End
            //4007 End
            //Code added for Case#3554 - Notify IRB enhancement - starts
            if(actionId == 116){
                actionBean.setSubmissionTypeCode(inputForm.getNotificationType());
                actionBean.setReviwerTypeCode(inputForm.getReviewerType());
            }
            //Code added for Case#3554 - Notify IRB enhancement - ends
            if (inputForm.performAction()) {
                actionBean.setComments( userInput );
                actionBean.setReviewComments( reviewComments );
                actionBean.setActionDate(actionDate);//Added by Jobin For the Action Date
                //Added for performing request and non request actions - start - 6
                actionBean.setCommitteeId(committeeId);
                //Added for performing request and non request actions - end - 6
               /*
                *   No locking is required for review comments so commenting
                */
//               releaseLock = inputForm.releaseScheduleLock();
                return performAction( actionBean );
            }//nadh end jul 30 2004
        }else {
            canPerform = canPerformAction( actionBean );
            if (canPerform){
                String msgPrompt = new String() ;
                String strTitle = new String() ;
                String strDefault = new String() ;
                ScheduleActionInputForm inputForm ;
                String userInput = null;
                Vector reviewComments = null;
                //Added by Jobin For the Action Date
                //java.sql.Date actionDate = null; // - end
                strTitle = coeusMessageResources.parseMessageKey(
                        "actionInputFrm_TitleCode." + actionBean.getActionTypeCode() + "1")
                        + "-" + actionBean.getProtocolNumber() ;
                msgPrompt = coeusMessageResources.parseMessageKey(
                        "actionInputFrm_PromptCode." + actionBean.getActionTypeCode() + "2") ;
                strDefault = coeusMessageResources.parseMessageKey(
                        "actionInputFrm_DefaultCode." + actionBean.getActionTypeCode() + "3") ;
                if( actionBean.getActionTypeCode() == ACTION_EXPEDITED_APPROVAL ||
                        actionBean.getActionTypeCode() == ACTION_GRANT_EXEMPTION ||
                        actionBean.getActionTypeCode() == ACTION_IRB_REVIEW_NOT_REQUIRED ||//Added by shiji for case id: 1880
                        actionBean.getActionTypeCode() == ACTION_RESPONSE_APPROVAL ||
                        actionBean.getActionTypeCode() == ACTION_IRB_ACKNOWLEDGEMENT ) {
                    java.sql.Date appDate = null ;
                    java.sql.Date expDate = null ;
                    ScheduleActionApprovalForm appForm =
                            new ScheduleActionApprovalForm(CoeusGuiConstants.getMDIForm(),
                            strTitle, msgPrompt, strDefault) ;
                    appForm.setLockSchedule( lockSchedule );
                    appForm.setActionBean( actionBean );
                    appForm.showForm() ;
                    userInput = appForm.getUserInput() ;
                    appDate = appForm.getApprovalDate() ;
                    expDate = appForm.getExpirationDate() ;
                    //Added by Jobin For the Action Date
                    actionDate = appForm.getActionDate();
                    reviewComments = appForm.getReviewComments();
                    if (appForm.performAction()) {
                        actionBean.setComments( userInput );
                        actionBean.setApprovalDate(appDate) ;
                        actionBean.setExpirationDate(expDate) ;
                        actionBean.setActionDate(actionDate);//Added by Jobin For the Action Date
                        actionBean.setReviewComments( reviewComments );
                        //Added for case 2176 - Risk Level Category - start
                        //Set the risk levels only if the action is Expedieted Approval or Response Approval
                        if(actionBean.getActionTypeCode() == 208 || actionBean.getActionTypeCode() == 205){
                            actionBean.setRiskLevels(appForm.getNewOrModifiedRiskLevels());
                        }
                        //Added for case 2176 - Risk Level Category - end
//                            releaseLock = appForm.releaseScheduleLock();
                        return performAction( actionBean );
                    }
                }//prps start dec 15 2003
                else if (actionBean.getActionTypeCode() == 101) // submit to IRB
                {
                    System.out.println("** perform submit to IRB **") ;
                    //For IRB Enhancement Assigning to agenda as the follow up action to submitted to IRB step:1
                    //performSubmitToIRB(actionBean.getProtocolNumber()) ;
                    //End step:1
                    System.out.println("** returning from submit to IRB **") ;
                    // get the follow up action for submit to irb
                    ProtocolActionChangesBean protocolActionChangesBean
                            = new ProtocolActionChangesBean() ;
                    protocolActionChangesBean.setProtocolNumber(actionBean.getProtocolNumber()) ;
                    //For IRB Enhancement Assigning to agenda as the follow up action to submitted to IRB step:2
                    RequesterBean requester = new RequesterBean();
                    requester.setFunctionType('b');
                    requester.setId(actionBean.getProtocolNumber());
                    String connectTo =CoeusGuiConstants.CONNECTION_URL
                            + PROTOCOL_ACTION_SERVLET;
                    AppletServletCommunicator comm
                            = new AppletServletCommunicator(connectTo,requester);
                    comm.send();
                    ResponderBean response = comm.getResponse();
                    ProtocolSubmissionInfoBean bean = new ProtocolSubmissionInfoBean();
                    if(response != null && response.isSuccessfulResponse()){
                        bean = (ProtocolSubmissionInfoBean)response.getDataObject();
                    }else{
                        CoeusOptionPane.showInfoDialog( response.getMessage() );
                    }
                    protocolActionChangesBean.setSubmissionNumber(bean.getSubmissionNumber());
                    protocolActionChangesBean.setCommitteeId(bean.getCommitteeId());
                    protocolActionChangesBean.setScheduleId(bean.getScheduleId()) ;
                    protocolActionChangesBean.setFollowupAction(getFollowupActions(101)) ;
                    protocolActionChangesBean.setSequenceNumber(actionBean.getSequenceNumber());
                    //End IRB Enhancement step:2
                    
                    return protocolActionChangesBean ;
                } else if (actionBean.getActionTypeCode() == 109) // Notify_committee
                {
                    return performAssignToSchedule(actionBean) ;
                    //return performAction(actionBean) ;
                } // prps end dec 15 2003
                else{
                    inputForm = new ScheduleActionInputForm( CoeusGuiConstants.getMDIForm(), strTitle, msgPrompt,strDefault) ;
                    //Added for performing request and non request actions - start - 7
                    int actionId = actionBean.getActionTypeCode();
                    if(actionId != 104 &&
                            actionId != 105 &&
                            actionId != 106 &&
                            actionId != 107 &&
                            actionId != 108 &&
                            actionId != 114 &&
                            actionId != 115 &&
                            //Code added for Case#3554 - Notify IRB enhancement
                            actionId != 116){
                        inputForm.setVisibility();
                    }
                    //Added for performing request and non request actions - end - 7
                    inputForm.setLockSchedule( lockSchedule );
                    inputForm.setActionBean( actionBean );
                    inputForm.showForm() ;
                    userInput = inputForm.getUserInput() ;
                    reviewComments = inputForm.getReviewComments();
                    actionDate = inputForm.getActionDate();//Added by Jobin For the Action Date
                    if (inputForm.performAction()) {
                        actionBean.setComments( userInput );
                        actionBean.setReviewComments( reviewComments );
                        actionBean.setActionDate(actionDate);//Added by Jobin For the Action Date
//                            releaseLock = inputForm.releaseScheduleLock();
                        return performAction( actionBean );
                    }
                }
                
            }
        }// end else
        return null;
    }
    public ProtocolActionChangesBean performOtherAction( ProtocolActionsBean actionBean ) throws Exception {
        ProtocolActionChangesBean protocolActionChangesBean = this.performOtherAction(actionBean,true);
        if(protocolActionChangesBean != null){
            //Added for COEUSDEV-317 : Notification not working correctly in IRB Module - Start
            //To allow notification for all the actions
            ProtocolMailController mailController = new ProtocolMailController();
            synchronized(mailController) {
                mailController.sendMail(actionBean.getActionTypeCode(), protocolActionChangesBean.getProtocolNumber(), protocolActionChangesBean.getSequenceNumber());
            }
            //COEUSDEV-317 : End
        }
        return  protocolActionChangesBean;
    }
    public ProtocolActionChangesBean performOtherAction(int actionCode, int sequenceNumber, String protocolId,
            String scheduleId, String actionDescription) throws Exception{
        ProtocolActionsBean actionBean = new ProtocolActionsBean();
        actionBean.setActionTypeCode(actionCode);
        actionBean.setSequenceNumber( sequenceNumber );
        actionBean.setProtocolNumber( protocolId );
        actionBean.setScheduleId( scheduleId );
        actionBean.setActionTypeDescription( actionDescription );
        return performOtherAction( actionBean );
    }
    
    
    
    private Vector getFollowupActions(int actionCode) {
        Vector vecActionDetails = new Vector() ;
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('F');
        requester.setId( String.valueOf(actionCode));
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_ACTION_SERVLET ;
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            vecActionDetails = (Vector)response.getDataObject();
        }
        
        return  vecActionDetails ;
    }
    
    
    
    
    // notify committee
    private ProtocolActionChangesBean performAssignToSchedule( ProtocolActionsBean actionBean ) throws Exception {
        AssignScheduleForm assignSchedule = new AssignScheduleForm();
        if( assignSchedule.isScheduleSelected() ) {
//			HashMap selectedSubmissions = new HashMap();
            
            
            String committeeId = assignSchedule.getSelectedCommitteeId();
            String scheduleId = assignSchedule.getSelectedScheduleId();
            
            //                String protocolNumber = (String)tblProtocol.getValueAt(selRow,PROTOCOL_NUMBER_COLUMN);
            //                int seqNo = Integer.parseInt(
            //                    (String)tblProtocol.getValueAt(selRow,SEQUENCE_NUMBER_COLUMN));
            //                int subNo = Integer.parseInt(
            //                    (String)tblProtocol.getValueAt(selRow,SUBMISSION_NUMBER_COLUMN));
            //                actionBean = new ProtocolActionsBean();
            //actionBean.setProtocolNumber(protocolNumber);
            //actionBean.setSequenceNumber(seqNo);
            //actionBean.setSubmissionNumber(subNo);
            actionBean.setActionTypeCode(109); // NOTIFY_COMMITTEE
            actionBean.setCommitteeId( committeeId );
            actionBean.setScheduleId( scheduleId );
            // actionBean.setActionTypeDescription(((CoeusMenuItem)actSource).getText()); //prps commented this dec 12 2002
            return saveAssignedSubmissions( actionBean );
        }
        
        return null ;
    }
    
    
    
    private ProtocolActionChangesBean saveAssignedSubmissions( ProtocolActionsBean actionBean ) {
        Vector dataObjects = new Vector();
        dataObjects.addElement( actionBean );
        // boolean which specifies whether to release lock or not ( used in Schedules )
        dataObjects.addElement( new Boolean(false) );
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolActionServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('X');
        request.setDataObjects( dataObjects );
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (!response.isSuccessfulResponse()) {
            CoeusOptionPane.showInfoDialog( response.getMessage() );
        }else{
            Object responseObject = response.getDataObject();
            Vector responseVec = (Vector)responseObject;
            if ( responseVec != null  && responseVec.size() >=4  && responseVec.get(3) != null ){
                Vector docList = (Vector) responseVec.elementAt(3);
                DocumentList documentList = new DocumentList(CoeusGuiConstants.getMDIForm(),true);
                documentList.setProtocolNumber(actionBean.getProtocolNumber());
                documentList.setMenuAction(actionBean.getActionTypeDescription());
                documentList.setActionBean(actionBean);
                documentList.loadForm(docList);
                documentList.display();
            }else{
                //CoeusOptionPane.showInfoDialog("This protocol has no Documents");
                CoeusOptionPane.showInfoDialog("Requested action on Protocol " + actionBean.getProtocolNumber() + " completed successfully") ;
            }
            if( responseVec.size() > 4 ){
                ProtocolActionChangesBean protocolActionChangesBean
                        = (ProtocolActionChangesBean) responseVec.elementAt(4) ;
                if (responseVec.elementAt(0) != null) {
                    Vector vecActionDetails = new Vector() ;
                    vecActionDetails.add(0, (Vector)responseVec.elementAt(0) ) ;
                    vecActionDetails.add(1, (HashMap)responseVec.elementAt(1) ) ;
                    vecActionDetails.add(2, (HashMap)responseVec.elementAt(2) ) ;
                    protocolActionChangesBean.setFollowupAction(vecActionDetails) ;
                } else {
                    protocolActionChangesBean.setFollowupAction(new Vector() ) ;
                }
                return protocolActionChangesBean ;
                // return((ProtocolActionChangesBean)responseVec.elementAt(4));
            }
        }
        
        return null ;
    }
    
    
    
    
    
    // submit to irb
    ProtocolSubmissionForm submissionForm = null  ;
//	private void performSubmitToIRB(String protocolId) {
//		try {
//
//			ProtocolInfoBean protocolInfo = getProtocolDetails(protocolId) ;
//
//			if((protocolId != null) && (protocolId.trim().length() > 0)) {
//				submissionForm = new ProtocolSubmissionForm(
//				CoeusGuiConstants.getMDIForm(),
//				"Protocol Submission", true,protocolInfo);
//				Dimension screenSize
//				= Toolkit.getDefaultToolkit().getScreenSize();
//				Dimension dlgSize = submissionForm.getSize();
//				submissionForm.setLocation(
//				screenSize.width/2 - (dlgSize.width/2),
//				screenSize.height/2 - (dlgSize.height/2));
//
//				submissionForm.setDefaultCloseOperation(
//				JDialog.DO_NOTHING_ON_CLOSE);
//
//				submissionForm.addWindowListener(
//				new WindowAdapter(){
//					public void windowClosing(WindowEvent we){
//						saveSubmissionDetails();
//					}
//				});
//				//To be confirmed
//				submissionForm.addKeyListener(new KeyAdapter(){
//					public void keyReleased(KeyEvent e){
//						if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
//							if(!CoeusOptionPane.isPropagating()){
//								saveSubmissionDetails();
//							}else{
//								CoeusOptionPane.setPropagating(
//								false);
//							}
//						}
//					}
//				});
//				submissionForm.show();
//				if( submissionForm.isDataSaved()) {
//					protocolInfo
//					= submissionForm.getProtocolDetails();
//					//updateData();
//					//prps start new code - May 1st 2003
//					// once the successful submit is done close the
//					// protocol screen
//					CoeusOptionPane.showInfoDialog("Protocol " + protocolId + " Submitted for Review") ;
//					//                            CoeusGuiConstants.getMDIForm().removeFrame(
//					//                            CoeusGuiConstants.PROTOCOL_FRAME_TITLE,protocolId);
//
//					functionType = 'M' ;
//					//releaseLock = submissionForm. ; //prps check - dec 15 2003
//					submissionForm.dispose() ;
//					//prps end new code
//				}
//			}
//
//		}catch(Exception e){
//			e.printStackTrace();
//			if(!( e.getMessage().equals(coeusMessageResources.parseMessageKey(
//			"protoDetFrm_exceptionCode.1130")) )){
//				CoeusOptionPane.showErrorDialog(e.getMessage());
//			}
//		}
//
//
//
//	}
    
//    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    
//	private boolean modifiable = true;
    
//	private ProtocolInfoBean getProtocolDetails(String protocolId) throws Exception {
//		Vector dataObjects = null;
//		String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
//		// connect to the database and get the formData for the given organization id
//		RequesterBean request = new RequesterBean();
//
//		request.setFunctionType('D');
//
//		request.setId(protocolId);
//		AppletServletCommunicator comm
//		= new AppletServletCommunicator(connectTo, request);
//		comm.send();
//		ResponderBean response = comm.getResponse();
//		if (response == null) {
//			response = new ResponderBean();
//			response.setResponseStatus(false);
//			response.setMessage(coeusMessageResources.parseMessageKey(
//			"server_exceptionCode.1000"));
//		}
//		if (response.isSuccessfulResponse()) {
//			dataObjects = response.getDataObjects();
//		} else {
//			if (response.isLocked()) {
//			/* the row is being locked by some other user
//			 */
//				// setModifiable(false);
//				throw new Exception("protocol_row_clocked_exceptionCode.666666");
//			}else {
//				throw new Exception(response.getMessage());
//			}
//		}
//		return  (ProtocolInfoBean) dataObjects.elementAt(0);
//
//	}
    
//	private char functionType = 'D' ;
    
    //prps check = dec 15 2003
    ////    private void releaseUpdateLock(){
    ////        try{
    ////            if(scheduleDetails==null){
    ////                return;
    ////            }
    ////            String refId = scheduleDetails.getScheduleId();
    ////
    ////            if ( functionType == 'M' ) {
    ////                String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
    ////                RequesterBean requester = new RequesterBean();
    ////                requester.setDataObject(refId);
    ////                requester.setFunctionType('Z');
    ////                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
    ////                //comm.releaseUpdateLock(refId,"/scheduleMaintSrvlt");
    ////                comm.send();
    ////                ResponderBean res = comm.getResponse();
    ////                if (res != null && !res.isSuccessfulResponse()){
    ////                    CoeusOptionPane.showErrorDialog(res.getMessage());
    ////                    return;
    ////                }
    ////            }
    ////        }catch(Exception e){
    ////            //e.printStackTrace();
    ////            CoeusOptionPane.showErrorDialog(e.getMessage());
    ////        }
    ////    }
    
    /**
     * This method is used to ask the confirmation and save the submission details
     * before closing the dialog.
     */
//	private void saveSubmissionDetails(){
//		int option = CoeusOptionPane.SELECTION_NO;
//		if(submissionForm.isSaveRequired()){
//			option = CoeusOptionPane.showQuestionDialog(
//			coeusMessageResources.parseMessageKey(
//			"saveConfirmCode.1002"),
//			CoeusOptionPane.OPTION_YES_NO_CANCEL,
//			CoeusOptionPane.DEFAULT_YES);
//			if(option == CoeusOptionPane.SELECTION_YES){
//				try{
//					submissionForm.submitProtocol();
//				}catch(Exception e){
//					CoeusOptionPane.showErrorDialog(
//					e.getMessage());
//					submissionForm.setVisible(true);
//
//				}
//			}else if(option
//			== CoeusOptionPane.SELECTION_NO){
//				submissionForm.dispose();
//			}
//		}else{
//			submissionForm.dispose();
//		}
//
//	}
}
