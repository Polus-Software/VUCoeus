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

/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

//import java.util.Vector;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.iacuc.controller.ProtocolMailController;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;
//import edu.mit.coeus.irb.bean.ProtocolActionsBean ;
//import edu.mit.coeus.irb.bean.ProtocolActionChangesBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.utils.CustomTagScanner;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.IacucProtocolActionsConstants;
import edu.mit.coeus.utils.ReportGui;
import java.util.*;
import java.io.ByteArrayInputStream;
//import java.awt.Cursor;

/**
 *
 * @author  Vyjayanthi
 */
public class ProcessAction {
    
    static private ProcessAction instance;       // The single instance
    
    private final String PROTOCOL_ACTION_SERVLET = "/IacucProtocolActionServlet";
    
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
    //Commented For PMD Check
    // private String committeeId = "";
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
            ProtocolActionChangesBean protocolActionChangesBean = null;
            if( responseVec.size() > 4 ){
               protocolActionChangesBean = (ProtocolActionChangesBean) responseVec.elementAt(4) ;
            }
            if ( responseVec != null  && responseVec.size() >=4  && responseVec.get(3) != null ){
                Vector docList = (Vector) responseVec.elementAt(3);
                 //uncommented for case id COEUSQA-1724 iacuc correspondence generation start
                DocumentList documentList = new DocumentList(CoeusGuiConstants.getMDIForm(),true);
                actionBean.setActionTypeCode(protocolActionChangesBean.getActionTypeCode());
                actionBean.setActionTypeDescription(protocolActionChangesBean.getActionTypeDescription());
                documentList.setProtocolNumber(actionBean.getProtocolNumber());
                documentList.setMenuAction(actionBean.getActionTypeDescription());
                //Added for Coeus4.3 enhancement - Email Notification - start
                documentList.setActionBean(actionBean);
            //Added for Coeus4.3 enhancement - Email Notification - end
                documentList.loadForm(docList);
                documentList.display();
                //uncommented for case id COEUSQA-1724 iacuc correspondence generation end
            }else{
                //CoeusOptionPane.showInfoDialog("This protocol has no Documents");
                CoeusOptionPane.showInfoDialog("Requested action on Protocol " + actionBean.getProtocolNumber() + " completed successfully") ;
            }
            if( responseVec.size() > 4 ){
                //prps start dec 12 2003
                // to add follow up action vector
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
        //Commented For PMD Check
        //boolean canPerform = false ;
        java.sql.Date actionDate = null; 
        java.sql.Date expirationDate = null;
        java.sql.Date approvalDate = null;
        int actionCode = actionBean.getActionTypeCode(); 
        Vector vecReviewComments = null;
        if(IacucProtocolActionsConstants.SUBMITTED_TO_IACUC == actionBean.getActionTypeCode()){
            ProtocolActionChangesBean protocolActionChangesBean
                    = new ProtocolActionChangesBean() ;
            protocolActionChangesBean.setProtocolNumber(actionBean.getProtocolNumber()) ;
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
            //COEUSQA-1724 : Email Notifications For All Actions In IACUC - start
            protocolActionChangesBean.setActionTypeCode(actionBean.getActionTypeCode());
            protocolActionChangesBean.setActionTypeDescription(actionBean.getActionTypeDescription());
            //COEUSQA-1724 : Email Notifications For All Actions In IACUC - end
            protocolActionChangesBean.setSubmissionNumber(bean.getSubmissionNumber());
            protocolActionChangesBean.setCommitteeId(bean.getCommitteeId());
            protocolActionChangesBean.setScheduleId(bean.getScheduleId()) ;
            protocolActionChangesBean.setFollowupAction(getFollowupActions(IacucProtocolActionsConstants.SUBMITTED_TO_IACUC)) ;
            protocolActionChangesBean.setSequenceNumber(actionBean.getSequenceNumber());
            //End IRB Enhancement step:2
            
            return protocolActionChangesBean ;
        }
        if(!canPerformAction( actionBean )){
            return null;
        }
        strTitle = coeusMessageResources.parseMessageKey(
                "iacucActionInputFrm_TitleCode." + actionBean.getActionTypeCode()+"1")
                + "-" + actionBean.getProtocolNumber() ;
        msgPrompt = coeusMessageResources.parseMessageKey(
                "iacucActionInputFrm_PromptCode." + actionBean.getActionTypeCode()+"2") ;
        strDefault = coeusMessageResources.parseMessageKey(
                "iacucActionInputFrm_DefaultCode." + actionBean.getActionTypeCode()+"3") ;
        // modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
        if(actionCode == IacucProtocolActionsConstants.HOLD ||
                actionCode == IacucProtocolActionsConstants.WITHDRAWN  ||
                actionCode == IacucProtocolActionsConstants.ASSIGN_TO_AGENDA ||
                actionCode == IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED ||
                actionCode == IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED ||
                actionCode == IacucProtocolActionsConstants.RETURNED_TO_PI ||
                actionCode == IacucProtocolActionsConstants.DEACTIVATED ||
                actionCode == IacucProtocolActionsConstants.EXPIRED ||
                actionCode == IacucProtocolActionsConstants.REMOVED_FROM_AGENDA ||
                actionCode == IacucProtocolActionsConstants.TABLED ||
                actionCode == IacucProtocolActionsConstants.TERMINATED  ||
                actionCode == IacucProtocolActionsConstants.SUSPENDED ||
                actionCode == IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT ||
                actionCode == IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED ||
                // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
                actionCode == IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE
                // COEUSQA-2666: End
                || actionCode == IacucProtocolActionsConstants.PROTOCOL_ABANDON
                ){
            // modified for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
            inputForm = new ScheduleActionInputForm( CoeusGuiConstants.getMDIForm(), strTitle, msgPrompt,strDefault) ;
            inputForm.setLockSchedule(lockSchedule);
            inputForm.setActionBean(actionBean);
            //Modified for Case id COEUSQA-1724_Reviewer View of Protocol start
            // inputForm.disableReviewCommBtn(true);
            if(actionCode == IacucProtocolActionsConstants.WITHDRAWN && 
               CoeusGuiConstants.IACUC_PROTO_DETAIL_WINDOW.equals(actionBean.getActionTriggeredFrom())){
                inputForm.displayReviewCommBtn(false);
            }
            if(!isReviewCommentButtonRequired(actionCode)){
                inputForm.displayReviewCommBtn(false);
            }
            //Modified for Case id COEUSQA-1724_Reviewer View of Protocol end
            inputForm.showForm();
            if(inputForm.performAction()){
                userInput = inputForm.getUserInput() ;
                actionDate = inputForm.getActionDate();
                actionBean.setComments(userInput);
                actionBean.setActionDate(actionDate);
                return performAction(actionBean);
            }else{
                return null;
            }
        }else if(actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC ||
                actionCode == IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD ||
                actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE){
              inputForm = new ScheduleActionInputForm( CoeusGuiConstants.getMDIForm(), strTitle, msgPrompt,strDefault) ;
            inputForm.setLockSchedule(lockSchedule);
            inputForm.setActionBean(actionBean);
            //Modified for Case id COEUSQA-1724_Reviewer View of Protocol start
            //inputForm.disableReviewCommBtn(true);
            if(!isReviewCommentButtonRequired(actionCode)){
                inputForm.displayReviewCommBtn(false);
            }
            //Modified for Case id COEUSQA-1724_Reviewer View of Protocol end
            inputForm.showForm();
            if(inputForm.performAction()){
                userInput = inputForm.getUserInput() ;
                actionDate = inputForm.getActionDate();
                actionBean.setComments(userInput);
                actionBean.setCommitteeId(inputForm.getCommitteeId());
                actionBean.setActionDocuments(inputForm.getUploadDocuments());
                if(actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC){
                    actionBean.setSubmissionTypeCode(inputForm.getNotificationType());
                    actionBean.setReviwerTypeCode(inputForm.getReviewerType());
                }
                actionBean.setActionDate(actionDate);
                return performAction(actionBean);
            }else{
                return null;
            }
        }   else if(actionCode == IacucProtocolActionsConstants.RESPONSE_APPROVAL ||
                actionCode == IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL ||
                // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
                actionCode == IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL
                // COEUSQA-2666: End
                ){
            ScheduleActionApprovalForm approvalForm =
                    new ScheduleActionApprovalForm(CoeusGuiConstants.getMDIForm(),
                    strTitle, msgPrompt, strDefault) ;
            approvalForm.setActionBean(actionBean);
            approvalForm.showExpiryDate();
            approvalForm.showForm();
            if(approvalForm.performAction()){
                userInput = approvalForm.getUserInput() ;
                actionDate = approvalForm.getActionDate();
                expirationDate = approvalForm.getExpirationDate();
                approvalDate = approvalForm.getApprovalDate();
                vecReviewComments = approvalForm.getReviewComments();
                actionBean.setComments(userInput);
                actionBean.setActionDate(actionDate);
                actionBean.setExpirationDate(expirationDate);
                actionBean.setApprovalDate(approvalDate);
                actionBean.setReviewComments(vecReviewComments);
                return performAction(actionBean);
            }else{
                return null;
            }
        } else if(actionCode == IacucProtocolActionsConstants.DISAPPROVED ||
                actionCode == IacucProtocolActionsConstants.LIFT_HOLD){
            inputForm = new ScheduleActionInputForm( CoeusGuiConstants.getMDIForm(), strTitle, msgPrompt,strDefault) ;
            //Commented For PMD Check
            //int actionId = actionBean.getActionTypeCode();
            inputForm.setLockSchedule(lockSchedule);
            inputForm.setActionBean(actionBean);
            //Modified for Case id COEUSQA-1724_Reviewer View of Protocol start
            //inputForm.disableReviewCommBtn(true);
           if(!isReviewCommentButtonRequired(actionCode)){
                inputForm.displayReviewCommBtn(false);
           }
            //Modified for Case id COEUSQA-1724_Reviewer View of Protocol end
            inputForm.showForm() ;
            userInput = inputForm.getUserInput() ;
            reviewComments = inputForm.getReviewComments();
            actionDate = inputForm.getActionDate();
            if (inputForm.performAction()) {
                actionBean.setComments( userInput );
                actionBean.setReviewComments( reviewComments );
                actionBean.setActionDate(actionDate);
                return performAction( actionBean );
            }
        }else if(actionCode == IacucProtocolActionsConstants.RESCHEDULED){
            AssignScheduleForm  assignScheduleForm = new AssignScheduleForm(strTitle,msgPrompt,IacucProtocolActionsConstants.RESCHEDULED);
            if(assignScheduleForm.isScheduleSelected() && assignScheduleForm.isActionPerformed()){
                actionBean.setCommitteeId(assignScheduleForm.getSelectedCommitteeId());
                actionBean.setScheduleId(assignScheduleForm.getSelectedScheduleId());
                actionBean.setComments(assignScheduleForm.getActionComments());
                 return performAction(actionBean);
            }
        }
        return null;
    }
    public ProtocolActionChangesBean performOtherAction( ProtocolActionsBean actionBean ) throws Exception {
        ProtocolActionChangesBean protocolActionChangesBean = this.performOtherAction(actionBean,true);
        //COEUSQA-1724 Email-Notifications For All Actions In IACUC - start
        if(protocolActionChangesBean != null){
            ProtocolMailController mailController = new ProtocolMailController();
            synchronized(mailController) {
                mailController.sendMail(protocolActionChangesBean.getActionTypeCode(), protocolActionChangesBean.getProtocolNumber(), protocolActionChangesBean.getSequenceNumber());
            }
        }
        //COEUSQA-1724 Email-Notifications For All Actions In IACUC - end
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
        AssignScheduleForm assignSchedule = new AssignScheduleForm("Notify Committee",CoeusGuiConstants.EMPTY_STRING,actionBean.getActionId());
        if( assignSchedule.isScheduleSelected() ) {
            String committeeId = assignSchedule.getSelectedCommitteeId();
            String scheduleId = assignSchedule.getSelectedScheduleId();

            actionBean.setActionTypeCode(IacucProtocolActionsConstants.NOTIFY_COMMITTEE);
            actionBean.setCommitteeId( committeeId );
            actionBean.setScheduleId( scheduleId );
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
    
//    private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    
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
    
    
   //Added for Case id COEUSQA-1724_Reviewer View of Protocol start
    /**
     *  The function isReviewCommentButtonRequired return true when we need to enable reviewCommentButton
     *  else it will return false;
     *  @param actionCode int 
     *  @return isReviewCommentButtonRequired boolean
     */
    
    private boolean isReviewCommentButtonRequired(final int actionCode){
        
        boolean isReviewCommentButtonRequired = false;
        
        if(actionCode ==  IacucProtocolActionsConstants.WITHDRAWN  ||
              actionCode == IacucProtocolActionsConstants.ASSIGN_TO_AGENDA ||
              actionCode == IacucProtocolActionsConstants.TABLED ||
              actionCode == IacucProtocolActionsConstants.APPROVED ||
              actionCode == IacucProtocolActionsConstants.RESPONSE_APPROVAL ||
              actionCode == IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL ||
              actionCode == IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT ||
              actionCode == IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED  ||
              actionCode == IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED  ||
              actionCode == IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED  ||
              actionCode == IacucProtocolActionsConstants.RETURNED_TO_PI ||
              actionCode == IacucProtocolActionsConstants.DISAPPROVED ||
              // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC
              actionCode == IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL ||
              actionCode == IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE
              // COEUSQA-2666: End
              ){
            
             isReviewCommentButtonRequired = true;
        }
        
        return isReviewCommentButtonRequired;
    }
    
     //Added for Case id COEUSQA-1724_Reviewer View of Protocol end
    
}
