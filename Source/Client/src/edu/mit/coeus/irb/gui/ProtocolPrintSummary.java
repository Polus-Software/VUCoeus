/*
 * @(#)ProtocolPrintSummary.java 1.0 March 17, 2009, 7:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;

import java.net.URL;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.util.Vector;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author satheeshkumarkn
 */

public class ProtocolPrintSummary extends javax.swing.JPanel implements ActionListener{
    
    private static final String DLG_TITLE = "Print Summary";
    private static final String SERVLET = "/ReportConfigServlet";
    private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    
    private boolean isAmendmentRenewal = false;
    private Vector vcQuestionnaireChk = new Vector();
    // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL;
    // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
    
    private static final char SIGNATURE_REQUIRED_REQUEST = 'r' ;
    private static final char GET_QUESTIONS_MODE = 'I';
    private ProtocolInfoBean protocolInfoBean;
    private CoeusDlgWindow dlgPrintSummary;
    
    private static final char PROTOCOL_NOTICE = 'a';
    private CoeusVector questionnaire;
    private static final int PROTOCOL_MODULE = 7;
    private boolean isQuestionnaireAvailable = true;
    
    // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    private Vector protocolAttachment;
    private boolean isProtocolAttachmentPresent = true;
    private Vector otherAttachment;
    private boolean isOtherAttachmentPresent = true;
    private static final String IACUC_PROTOCOL_SERVLET = "/protocolMntServlet";
    private static final char GET_UPLOAD_DOC_DATA = 'g';
    private static final char GET_OTHER_ATTACHMENTS = '3';
    private Vector vecProtoAttachmentChk = new Vector();
    private Vector vecOtherAttachmentChk = new Vector();
    // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
    /**
     * Creates new form ProtocolPrintSummary
     */
    public ProtocolPrintSummary(ProtocolInfoBean protocolInfoBean) {
        this.protocolInfoBean = protocolInfoBean;
        initComponents();
        registerComponents();
        initCheckBox();
        // 4272: Maintain History of Questionnaires - Start
        setModuleDetailsForQuestionnaire();
        setFocusTraversal();
        // If protocol is Amendment or Renewal check box label is set to Amendment/Renewal Summary
        if(protocolInfoBean.getProtocolNumber().indexOf("A") > -1 || protocolInfoBean.getProtocolNumber().indexOf("R") > -1){
            isAmendmentRenewal = true;
            chkAmendRenew.setText("Amendment/Renewal Summary");
        }
    }
    
    /*
     * Method to register close, deselectall,selectall buttons
     */
    
    public void registerComponents() {
        btnClose.addActionListener(this);
        btnDeselectAll.addActionListener(this);
        btnPrint.addActionListener(this);
        btnSelectAll.addActionListener(this);
    }
    
    /*
     * Method to set focus for all the components in the print summary dialog window
     */
    private void setFocusTraversal(){
        //Component array to hold static components in pnlInclude
        Component[]  pnlInludeComponents = { chkProtocolDetail,chkFundingSource,chkAmendRenew,chkOrganization,chkActions,chkOthers,
        chkInvestigator,chkSubjects,chkRoles,chkStudyPersonnel,chkSpecialReview,chkReferences,chkCorrespondents,chkRiskLevel,chkAreaOfResearch,chkNotes};
        //Componet array to hold the buttons
        Component[] buttonComponents = {btnSelectAll,btnDeselectAll,btnPrint,btnClose};
        int noOfQuestionnaire = vcQuestionnaireChk.size();
        // Modified and Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        int protoAttachments = vecProtoAttachmentChk.size();
        int otherAttachments = vecOtherAttachmentChk.size();        
        int noOfCompForFocus = noOfQuestionnaire+protoAttachments+otherAttachments+pnlInludeComponents.length+buttonComponents.length;
        // Modified and Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        int count = 0;
        //Components are to hold all the components
        Component[] components = new Component[noOfCompForFocus];
        //Adding static components to components array
        for(int index=0;index<pnlInludeComponents.length;index++){
            components[index] = pnlInludeComponents[index];
            count = index;
        }
        //Adding questionnaire check box components to components array
        if(vcQuestionnaireChk != null){
            count = count + 1;
            for(int index=0 ;index<vcQuestionnaireChk.size();index++){
                components[count] = (javax.swing.JCheckBox)vcQuestionnaireChk.get(index);
                count++;
            }
        }
        // Modified and Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changesstart
        if(vecProtoAttachmentChk != null){
            for(int index=0 ;index<vecProtoAttachmentChk.size();index++){
                components[count] = (javax.swing.JCheckBox)vecProtoAttachmentChk.get(index);
                count++;
            }
        }
        if(vecOtherAttachmentChk != null){
            for(int index=0 ;index<vecOtherAttachmentChk.size();index++){
                components[count] = (javax.swing.JCheckBox)vecOtherAttachmentChk.get(index);
                count++;
            }
        }
        // Modified and Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        for(int index=0;index<buttonComponents.length;index++){
            components[count] = buttonComponents[index];
            count++;
        }
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
    }
    
   /*
    * Method used to enable all the check box
    */
    private void enableComponents() {
        // boolean signatureRequired = isSignatureRequired();
        //chkSignatureReq.setSelected(signatureRequired);
        chkActions.setSelected(true);
        chkAmendRenew.setSelected(true);
        chkAreaOfResearch.setSelected(true);
        chkCorrespondents.setSelected(true);
        chkFundingSource.setSelected(true);
        chkInvestigator.setSelected(true);
        chkNotes.setSelected(true);
        chkOrganization.setSelected(true);
        chkOthers.setSelected(true);
        chkProtocolDetail.setSelected(true);
        chkRiskLevel.setSelected(true);
        chkSpecialReview.setSelected(true);
        chkStudyPersonnel.setSelected(true);
        chkSubjects.setSelected(true);
        chkRoles.setSelected(true);
        chkReferences.setSelected(true);
        if(vcQuestionnaireChk != null && vcQuestionnaireChk.size() > 0){
            for(int index = 0;index < vcQuestionnaireChk.size();index++){
                javax.swing.JCheckBox chkQuestionnarie = (javax.swing.JCheckBox)vcQuestionnaireChk.get(index);
                chkQuestionnarie.setSelected(true);
            }
        }
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        if(vecProtoAttachmentChk != null && vecProtoAttachmentChk.size() > 0){
            for(int index = 0;index < vecProtoAttachmentChk.size();index++){
                javax.swing.JCheckBox chkProtoAttachment = (javax.swing.JCheckBox)vecProtoAttachmentChk.get(index);
                chkProtoAttachment.setSelected(true);
            }
        }
        if(vecOtherAttachmentChk != null && vecOtherAttachmentChk.size() > 0){
            for(int index = 0;index < vecOtherAttachmentChk.size();index++){
                javax.swing.JCheckBox chkOtherAttachment = (javax.swing.JCheckBox)vecOtherAttachmentChk.get(index);
                chkOtherAttachment.setSelected(true);
            }
        }
        // Added for divya-end
        
    }
    
   /*
    * Method used to get Protocol Questionnaire details
    * @return - getDataFromServer(questionnaireModuleObject)
    */
    private CoeusVector getProtocolQuestionnaire(){
        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
        int subModuleItemCode = 0; //Normal Protocol
        if(protocolInfoBean.getProtocolNumber().indexOf("A") > -1 || protocolInfoBean.getProtocolNumber().indexOf("R") > -1){
            subModuleItemCode = 1; // Amendment/Renewal
        }
        //COEUSDEV-86 : END
        QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
        questionnaireModuleObject.setModuleItemCode(PROTOCOL_MODULE);
        questionnaireModuleObject.setModuleItemKey(protocolInfoBean.getProtocolNumber());
        questionnaireModuleObject.setModuleItemDescription(CoeusGuiConstants.PROTOCOL_MODULE);
        //Modified for OEUSDEV-86 : Questionnaire for a Submission - Start
//        questionnaireModuleObject.setModuleSubItemCode(0);
        questionnaireModuleObject.setModuleSubItemCode(subModuleItemCode);
        //COEUSDEV-86 : END
        questionnaireModuleObject.setModuleSubItemKey(CoeusGuiConstants.EMPTY_STRING+protocolInfoBean.getSequenceNumber());
        questionnaireModuleObject.setModuleSubItemDescription(CoeusGuiConstants.PROTOCOL_MODULE);
        return getDataFromServer(questionnaireModuleObject);
    }
     /*
      * Method used to get questionnaire details from servler
      */
    private CoeusVector getDataFromServer(QuestionnaireAnswerHeaderBean questionnaireModuleObject){
        CoeusVector cvMainData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_QUESTIONS_MODE);
        CoeusVector cvQuestionnaireData = new CoeusVector();
        cvQuestionnaireData.add(questionnaireModuleObject);
        request.setDataObject(cvQuestionnaireData);
        // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+QUESTIONNAIRE_SERVLET, request);
        // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                cvMainData = (CoeusVector)response.getDataObject();
            }else{
                //throw new CoeusException(response.getMessage());
            }
        }else{
            // throw new CoeusException(response.getMessage());
        }
        //Added for COEUSDEV-292 : Protocol print summary throwing an error - Start
        //When there are no questionnaires defined for the protocol 
        if(cvMainData == null){
            cvMainData = new CoeusVector();
        }
        //COEUSDEV-292 : End

        return cvMainData;
    }
    
    /*
     * Method used to display the PrintSummary form
     */
    public void display() {
        enableComponents();
        dlgPrintSummary = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgPrintSummary.setTitle(DLG_TITLE);
        dlgPrintSummary.getContentPane().add(this);
        
        // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        //        if(isQuestionnaireAvailable){
//            dlgPrintSummary.setSize(650, 385);
//        }else{
//            dlgPrintSummary.setSize(650, 275);
//        }
        if(isQuestionnaireAvailable || isOtherAttachmentPresent ||isProtocolAttachmentPresent){
            dlgPrintSummary.setSize(650, 670);
        }else{
             jScrollPane1.setVisible(false);
             jScrollPane2.setVisible(false);
             jScrollPane3.setVisible(false);
             dlgPrintSummary.setSize(650, 275);
        }
        // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        
        dlgPrintSummary.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                requestDetailFocus();
            }
        });
        dlgPrintSummary.setResizable(false);
        dlgPrintSummary.setLocation(CoeusDlgWindow.CENTER);
        dlgPrintSummary.setVisible(true);
        
    }
    
    private void requestDetailFocus(){
        pnlInclude.setFocusCycleRoot(true);
        chkProtocolDetail.requestFocusInWindow();
    }
     /*
      * Method to get the PDF url and open the URL
      * @return true - if url exist and opened
      */
    private boolean getPDFUrl() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(PROTOCOL_NOTICE);
        Hashtable htDataToSend = getSelectedItems();
        htDataToSend.put(CoeusConstants.PROTOCOL_INFO_BEAN, protocolInfoBean);
        requesterBean.setDataObject(htDataToSend);
        
        htDataToSend.put(CoeusConstants.LOGGED_IN_USER, CoeusGuiConstants.getMDIForm().getUserId());
        htDataToSend.put(CoeusConstants.REPORT_TYPE,"ProtocolSummary");
        requesterBean.setId("Protocol/ProtocolSummary");
        requesterBean.setFunctionType('R');
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        String url = (String)responderBean.getDataObject();
        url = url.replace('\\', '/');
        
        
        try{
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+ex.getMessage());
            return false;
        }
    }
    
    
    /*
     * Method used to get details of all selected items
     * @return htSelectedItems
     */
    private Hashtable getSelectedItems() {
        Hashtable htSelectedItems = new Hashtable();
        htSelectedItems.put(CoeusConstants.ACTIONS, new Boolean(chkActions.isSelected()));
        boolean isAmendRenewHistory = false;
        boolean isAmendRenewSummary = false;
        //If the protocol is Amendment/Renewal , isAmendRenewHistory is set to false  and if the AmendRenew
        //check box is selected isAmendRenewSummary is set to true
        if(isAmendmentRenewal){
            isAmendRenewHistory = false;
            if(chkAmendRenew.isSelected()){
                isAmendRenewSummary = true;
            }
        }else{
            isAmendRenewSummary = false;
            if(chkAmendRenew.isSelected()){
                isAmendRenewHistory = true;
            }
        }
        htSelectedItems.put(CoeusConstants.AMENDMENT_RENEWAL_SUMMARY, new Boolean(isAmendRenewSummary));
        htSelectedItems.put(CoeusConstants.AMENDMENT_RENEWAL_HISTORY, new Boolean(isAmendRenewHistory));
        htSelectedItems.put(CoeusConstants.AREA_OF_RESEARCH, new Boolean(chkAreaOfResearch.isSelected()));
        htSelectedItems.put(CoeusConstants.CORRESPONDENTS, new Boolean(chkCorrespondents.isSelected()));
        htSelectedItems.put(CoeusConstants.FUNDING_SOURCE, new Boolean(chkFundingSource.isSelected()));
        htSelectedItems.put(CoeusConstants.INVESTIGATOR, new Boolean(chkInvestigator.isSelected()));
        htSelectedItems.put(CoeusConstants.NOTES, new Boolean(chkNotes.isSelected()));
        htSelectedItems.put(CoeusConstants.ORGANIZATION, new Boolean(chkOrganization.isSelected()));
        htSelectedItems.put(CoeusConstants.OTHER_DATA, new Boolean(chkOthers.isSelected()));
        htSelectedItems.put(CoeusConstants.PROTOCOL_DETAIL, new Boolean(chkProtocolDetail.isSelected()));
        htSelectedItems.put(CoeusConstants.RISK_LEVELS, new Boolean(chkRiskLevel.isSelected()));
        htSelectedItems.put(CoeusConstants.ATTACHMENTS, new Boolean(false));
        htSelectedItems.put(CoeusConstants.SPECIAL_REVIEW, new Boolean(chkSpecialReview.isSelected()));
        htSelectedItems.put(CoeusConstants.STUDY_PERSONNEL, new Boolean(chkStudyPersonnel.isSelected()));
        htSelectedItems.put(CoeusConstants.SUBJECTS, new Boolean(chkSubjects.isSelected()));
        htSelectedItems.put(CoeusConstants.ROLES, new Boolean(chkRoles.isSelected()));
        htSelectedItems.put(CoeusConstants.REFERENCES, new Boolean(chkReferences.isSelected()));
        Vector vcQuestionnarie = new Vector();
        if(vcQuestionnaireChk != null && vcQuestionnaireChk.size() > 0){
            for(int index = 0;index < vcQuestionnaireChk.size();index++){
                javax.swing.JCheckBox chkQuestionnarie = (javax.swing.JCheckBox)vcQuestionnaireChk.get(index);
                if(chkQuestionnarie != null && chkQuestionnarie.isSelected()){
                    vcQuestionnarie.add(((QuestionnaireAnswerHeaderBean)questionnaire.get(index)));
                }
            }
        }
        htSelectedItems.put(CoeusConstants.QUESTIONNAIRE, vcQuestionnarie);
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        Vector vecProtoAttachment = new Vector();
        if(vecProtoAttachmentChk != null && vecProtoAttachmentChk.size() > 0){
            for(int index = 0;index < vecProtoAttachmentChk.size();index++){
                javax.swing.JCheckBox chkProtoAttachment = (javax.swing.JCheckBox)vecProtoAttachmentChk.get(index);
                UploadDocumentBean documentBean = null;
                
                if(chkProtoAttachment != null && chkProtoAttachment.isSelected()){
                    documentBean = ((UploadDocumentBean)protocolAttachment.get(index));
                    documentBean.setDocument(null);
                    vecProtoAttachment.add(documentBean);
                }
            }
        }
        htSelectedItems.put(CoeusConstants.PROTOCOL_ATTACHMENTS, vecProtoAttachment);
        
        Vector vecOtherAttachment = new Vector();
        if(vecOtherAttachmentChk != null && vecOtherAttachmentChk.size() > 0){
            for(int index = 0;index < vecOtherAttachmentChk.size();index++){
                javax.swing.JCheckBox chkOtherAttachment = (javax.swing.JCheckBox)vecOtherAttachmentChk.get(index);
                UploadDocumentBean documentBean = null;
                
                if(chkOtherAttachment != null && chkOtherAttachment.isSelected()){
                    documentBean = ((UploadDocumentBean)otherAttachment.get(index));
                    documentBean.setDocument(null);
                    vecOtherAttachment.add(documentBean);
                }
            }
        }
        
        htSelectedItems.put(CoeusConstants.PROTOCOL_OTHER_ATTACHMENTS, vecOtherAttachment);        
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        return htSelectedItems;
    }
    
    /*
     * The method which makes a server call and checks whether signature is required
     */
    private boolean isSignatureRequired() {
        try {
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(SIGNATURE_REQUIRED_REQUEST);
            
            requesterBean.setDataObject(protocolInfoBean);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean == null) {
                //Could not contact server.
                return false;
            }
            Integer signatureRequired = (Integer)responderBean.getDataObject();
            
            if (signatureRequired.intValue()==0) {
                return false;
            } else {
                return true;
            }
        }catch(Exception e) {
            return false;
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object source =ae.getSource();
        try {
            dlgPrintSummary.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (source.equals(btnClose)) {
                dlgPrintSummary.setVisible(false);
            } else if (source.equals(btnPrint)) {
                boolean success=getPDFUrl();
                if (success) {
                    dlgPrintSummary.setVisible(false);
                }
            } else if (source.equals(btnSelectAll)) {
                chkActions.setSelected(true);
                chkAmendRenew.setSelected(true);
                chkAreaOfResearch.setSelected(true);
                chkCorrespondents.setSelected(true);
                chkFundingSource.setSelected(true);
                chkInvestigator.setSelected(true);
                chkNotes.setSelected(true);
                chkOrganization.setSelected(true);
                chkOthers.setSelected(true);
                chkProtocolDetail.setSelected(true);
                chkRiskLevel.setSelected(true);
                chkSpecialReview.setSelected(true);
                chkStudyPersonnel.setSelected(true);
                chkSubjects.setSelected(true);
                chkRoles.setSelected(true);
                chkReferences.setSelected(true);
                if(vcQuestionnaireChk != null && vcQuestionnaireChk.size() > 0){
                    for(int index = 0;index < vcQuestionnaireChk.size();index++){
                        javax.swing.JCheckBox chkQuestionnarie = (javax.swing.JCheckBox)vcQuestionnaireChk.get(index);
                        chkQuestionnarie.setSelected(true);
                    }
                }
                //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
                if(vecProtoAttachmentChk != null && vecProtoAttachmentChk.size() > 0){
                    for(int index = 0;index < vecProtoAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkProtoAttachment = (javax.swing.JCheckBox)vecProtoAttachmentChk.get(index);
                        chkProtoAttachment.setSelected(true);
                    }
                }
                 if(vecOtherAttachmentChk != null && vecOtherAttachmentChk.size() > 0){
                    for(int index = 0;index < vecOtherAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkOtherAttachment = (javax.swing.JCheckBox)vecOtherAttachmentChk.get(index);
                        chkOtherAttachment.setSelected(true);
                    }
                }
                // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes_end
            } else if (source.equals(btnDeselectAll)) {
                chkActions.setSelected(false);
                chkAmendRenew.setSelected(false);
                chkAreaOfResearch.setSelected(false);
                chkCorrespondents.setSelected(false);
                chkFundingSource.setSelected(false);
                chkInvestigator.setSelected(false);
                chkNotes.setSelected(false);
                chkOrganization.setSelected(false);
                chkOthers.setSelected(false);
                chkProtocolDetail.setSelected(false);
                chkRiskLevel.setSelected(false);
                chkSpecialReview.setSelected(false);
                chkStudyPersonnel.setSelected(false);
                chkSubjects.setSelected(false);
                chkRoles.setSelected(false);
                chkReferences.setSelected(false);
                if(vcQuestionnaireChk != null && vcQuestionnaireChk.size() > 0){
                    for(int index = 0;index < vcQuestionnaireChk.size();index++){
                        javax.swing.JCheckBox chkQuestionnarie = (javax.swing.JCheckBox)vcQuestionnaireChk.get(index);
                        chkQuestionnarie.setSelected(false);
                    }
                }
                //Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
                if(vecProtoAttachmentChk != null && vecProtoAttachmentChk.size() > 0){
                    for(int index = 0;index < vecProtoAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkProtoAttachment = (javax.swing.JCheckBox)vecProtoAttachmentChk.get(index);
                        chkProtoAttachment.setSelected(false);
                    }
                }
                if(vecOtherAttachmentChk != null && vecOtherAttachmentChk.size() > 0){
                    for(int index = 0;index < vecOtherAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkOtherAttachment = (javax.swing.JCheckBox)vecOtherAttachmentChk.get(index);
                        chkOtherAttachment.setSelected(false);
                    }
                }
                // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
            }
        } finally {
            dlgPrintSummary.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    /*
     * This method is called from within the constructor to
     * initialize the Check - box in the form.
     */
    private void initCheckBox(){
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        pnlQuestionnaire.setLayout(flowLayout);
        questionnaire = getProtocolQuestionnaire();
        // Modified and added COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        //Removes the questionnaire panel when there is no questionnaire
        if(questionnaire == null || questionnaire.size()<= 0 ){
//            pnlInclude.remove(jScrollPane1);
//            Dimension size = new Dimension(520,225);
            isQuestionnaireAvailable = false;
//            pnlInclude.setMaximumSize(size);
//            pnlInclude.setMinimumSize(size);
//            pnlInclude.setPreferredSize(size);
        }
        // Modified for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
        //Creates questionnaire check box
        int count = 0;
        
        for(int index=0;index<questionnaire.size();index++){
            QuestionnaireAnswerHeaderBean questionnaireDetails = (QuestionnaireAnswerHeaderBean)questionnaire.get(index);
            javax.swing.JCheckBox chkQuestionnaire =  new javax.swing.JCheckBox();
            Dimension max = new Dimension(230,18);
            chkQuestionnaire.setMaximumSize(max);
            chkQuestionnaire.setMinimumSize(max);
            chkQuestionnaire.setPreferredSize(max);
            count++;
            if(questionnaireDetails != null){
                chkQuestionnaire.setText(questionnaireDetails.getLabel());
                chkQuestionnaire.setToolTipText(questionnaireDetails.getLabel());
                vcQuestionnaireChk.add(chkQuestionnaire);
                pnlQuestionnaire.add(chkQuestionnaire);
            }
        }
        //To increase the size of ther questionnaire panel when more than 4 rows are present
        //int rowCount = count/2;
        if(count > 8){
            Dimension questionnairepanelSize = new Dimension(490,90+(count*9));
            jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            pnlQuestionnaire.setMaximumSize(questionnairepanelSize);
            pnlQuestionnaire.setMinimumSize(questionnairepanelSize);
            pnlQuestionnaire.setPreferredSize(questionnairepanelSize);
        }
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
        //get the protocolAttachments 
        FlowLayout flowLayoutAttach = new FlowLayout(FlowLayout.LEFT);
        pnlProtocolAttachments.setLayout(flowLayoutAttach);
        protocolAttachment = getProtocolAttachments();
        if(protocolAttachment == null || protocolAttachment.size()<= 0 ){
            //pnlInclude.remove(jScrollPane2);
            isProtocolAttachmentPresent = false;
        }
        int protoAttachCount = 0;
        for(int protoAttachIndex=0;protoAttachIndex<protocolAttachment.size();protoAttachIndex++){
            UploadDocumentBean protoAttachmentBean= (UploadDocumentBean)protocolAttachment.get(protoAttachIndex);
            javax.swing.JCheckBox chkProtoAttachment =  new javax.swing.JCheckBox();
            Dimension max = new Dimension(230,18);
            chkProtoAttachment.setMaximumSize(max);
            chkProtoAttachment.setMinimumSize(max);
            chkProtoAttachment.setPreferredSize(max);
            protoAttachCount++;
            if(protoAttachmentBean != null){
                chkProtoAttachment.setText(protoAttachmentBean.getFileName());
                chkProtoAttachment.setToolTipText(protoAttachmentBean.getFileName());
                vecProtoAttachmentChk.add(chkProtoAttachment);
                pnlProtocolAttachments.add(chkProtoAttachment);
            }
            
        }
        //To increase the size of the Protocol Attachment panel when more rows are present
        //int protoDocRow = protoAttachCount/2;
        if(protoAttachCount > 8){
            Dimension protoAttachPanelSize = new Dimension(490,90+(protoAttachCount*9));//CHANGE THIS
            jScrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            pnlProtocolAttachments.setMaximumSize(protoAttachPanelSize);
            pnlProtocolAttachments.setMinimumSize(protoAttachPanelSize);
            pnlProtocolAttachments.setPreferredSize(protoAttachPanelSize);
        }
        
        //get the otherAttachments 
        FlowLayout flowLayoutOther = new FlowLayout(FlowLayout.LEFT);
        pnlOtherAttachments.setLayout(flowLayoutOther);
        otherAttachment = getOtherAttachments();
        //get other attachments here         
        if(otherAttachment == null || otherAttachment.size()<= 0 ){
            //pnlInclude.remove(jScrollPane3);
            isOtherAttachmentPresent = false;
        }
        
        int otherAttachCount = 0;
        for(int otherAttachIndex=0;otherAttachIndex<otherAttachment.size();otherAttachIndex++){
            UploadDocumentBean otherAttachmentBean= (UploadDocumentBean)otherAttachment.get(otherAttachIndex);
            javax.swing.JCheckBox chkOtherAttachment =  new javax.swing.JCheckBox();
            Dimension max = new Dimension(230,18);
            chkOtherAttachment.setMaximumSize(max);
            chkOtherAttachment.setMinimumSize(max);
            chkOtherAttachment.setPreferredSize(max);
            otherAttachCount++;
            if(otherAttachmentBean != null){
                chkOtherAttachment.setText(otherAttachmentBean.getFileName());
                chkOtherAttachment.setToolTipText(otherAttachmentBean.getFileName());
                vecOtherAttachmentChk.add(chkOtherAttachment);
                pnlOtherAttachments.add(chkOtherAttachment);
            }
            
        }
        //To increase the size of the Other Attachment panel when more rows are present
        //int otherDocRow = otherAttachCount/2;
        if(otherAttachCount > 8){
            Dimension otherAttachPanelSize = new Dimension(490,90+(otherAttachCount*9));//CHANGE THIS
            jScrollPane3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            pnlOtherAttachments.setMaximumSize(otherAttachPanelSize);
            pnlOtherAttachments.setMinimumSize(otherAttachPanelSize);
            pnlOtherAttachments.setPreferredSize(otherAttachPanelSize);
        }
        // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes end
    }
    
    // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changes start
    /**
     * To get all the protocol attachements 
     * @return  vector containing protocol attachments
     */
     private Vector getProtocolAttachments(){
        Vector vecMainData = new Vector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_UPLOAD_DOC_DATA);
        CoeusVector cvProtocolAttachments = new CoeusVector();                
        request.setDataObject(protocolInfoBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+IACUC_PROTOCOL_SERVLET, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                vecMainData = (Vector)response.getDataObject();
            }
        }        
        vecMainData = vecMainData == null ? new Vector():vecMainData;
        vecMainData = getPDFFiles(vecMainData);        
        return vecMainData;  
     }
     
     /**
      * To get all Other Attachments
      * @return CoeusVector containing other attachments
      */
     private CoeusVector getOtherAttachments(){         
        CoeusVector cvMainData = new CoeusVector();
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_OTHER_ATTACHMENTS);
        CoeusVector cvOtherAttachments = new CoeusVector();        
        
        request.setDataObject(protocolInfoBean.getProtocolNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+IACUC_PROTOCOL_SERVLET, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                Vector vecServerData = response.getDataObjects();
                if(vecServerData != null && vecServerData.size()== 2){
                    cvMainData = (CoeusVector)vecServerData.get(0);                    
                }
            }
        }        
        cvMainData = cvMainData == null ? new CoeusVector():cvMainData;
        cvMainData = (CoeusVector)getPDFFiles(cvMainData);  
        return cvMainData;         
     }
     
     /**
      * To filter only pdf attachments from the given vector
      * @param vector containing all attachments
      * @return vector conaining only pdf attachments
      */
     private Vector getPDFFiles(Vector attachmentsData){
         CoeusVector cvFilteredData =  new CoeusVector();
         if(attachmentsData !=null ){
             UploadDocumentBean attachBean= null;
             for(Object docData: attachmentsData){
                 attachBean = (UploadDocumentBean)docData;
                 if(attachBean != null){                    
                        String fileExtension = attachBean.getFileName().toLowerCase();
                        if(fileExtension.endsWith(".pdf")){                     
                        cvFilteredData.add(attachBean);
                     }
                 }
             }
             
         }
         return cvFilteredData;
     }
    // Added for COEUSQA-2630_Allow the ability to print attachments with protocol summary/Schema changesend
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlInclude = new javax.swing.JPanel();
        chkProtocolDetail = new javax.swing.JCheckBox();
        chkStudyPersonnel = new javax.swing.JCheckBox();
        chkFundingSource = new javax.swing.JCheckBox();
        chkNotes = new javax.swing.JCheckBox();
        chkActions = new javax.swing.JCheckBox();
        chkCorrespondents = new javax.swing.JCheckBox();
        chkSpecialReview = new javax.swing.JCheckBox();
        chkInvestigator = new javax.swing.JCheckBox();
        chkAreaOfResearch = new javax.swing.JCheckBox();
        chkSubjects = new javax.swing.JCheckBox();
        chkRiskLevel = new javax.swing.JCheckBox();
        chkOthers = new javax.swing.JCheckBox();
        btnSelectAll = new javax.swing.JButton();
        btnDeselectAll = new javax.swing.JButton();
        chkOrganization = new javax.swing.JCheckBox();
        chkAmendRenew = new javax.swing.JCheckBox();
        chkReferences = new javax.swing.JCheckBox();
        chkRoles = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlQuestionnaire = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlProtocolAttachments = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnlOtherAttachments = new javax.swing.JPanel();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(610, 560));
        setMinimumSize(new java.awt.Dimension(610, 560));
        setPreferredSize(new java.awt.Dimension(610, 560));
        pnlInclude.setLayout(new java.awt.GridBagLayout());

        pnlInclude.setBorder(javax.swing.BorderFactory.createTitledBorder("Include"));
        pnlInclude.setFocusCycleRoot(true);
        pnlInclude.setMaximumSize(new java.awt.Dimension(670, 900));
        pnlInclude.setMinimumSize(new java.awt.Dimension(670, 900));
        pnlInclude.setPreferredSize(new java.awt.Dimension(670, 900));
        pnlInclude.setRequestFocusEnabled(false);
        chkProtocolDetail.setText("Protocol Details");
        chkProtocolDetail.setToolTipText("Protocol Details");
        chkProtocolDetail.setActionCommand("Protocol Detail");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        pnlInclude.add(chkProtocolDetail, gridBagConstraints);

        chkStudyPersonnel.setText("Study personnel");
        chkStudyPersonnel.setToolTipText("Study personnel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        pnlInclude.add(chkStudyPersonnel, gridBagConstraints);

        chkFundingSource.setText("Funding Source");
        chkFundingSource.setToolTipText("Funding Source");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlInclude.add(chkFundingSource, gridBagConstraints);

        chkNotes.setText("Notes");
        chkNotes.setToolTipText("Notes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlInclude.add(chkNotes, gridBagConstraints);

        chkActions.setText("Action History");
        chkActions.setToolTipText("Action History");
        chkActions.setActionCommand("Actions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlInclude.add(chkActions, gridBagConstraints);

        chkCorrespondents.setText("Correspondents");
        chkCorrespondents.setToolTipText("Correspondents");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        pnlInclude.add(chkCorrespondents, gridBagConstraints);

        chkSpecialReview.setText("Special Review");
        chkSpecialReview.setToolTipText("Special Review");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlInclude.add(chkSpecialReview, gridBagConstraints);

        chkInvestigator.setText("Investigator");
        chkInvestigator.setToolTipText("Investigator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        pnlInclude.add(chkInvestigator, gridBagConstraints);

        chkAreaOfResearch.setText("Area of Research");
        chkAreaOfResearch.setToolTipText("Area of Research");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        pnlInclude.add(chkAreaOfResearch, gridBagConstraints);

        chkSubjects.setText("Subjects");
        chkSubjects.setToolTipText("Subjects");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlInclude.add(chkSubjects, gridBagConstraints);

        chkRiskLevel.setText("Risk Levels");
        chkRiskLevel.setToolTipText("Risk Levels");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 0);
        pnlInclude.add(chkRiskLevel, gridBagConstraints);

        chkOthers.setText("Other Data");
        chkOthers.setToolTipText("Other Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        pnlInclude.add(chkOthers, gridBagConstraints);

        btnSelectAll.setMnemonic('S');
        btnSelectAll.setText("Select All");
        btnSelectAll.setMinimumSize(new java.awt.Dimension(175, 23));
        btnSelectAll.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 60, 3, 0);
        pnlInclude.add(btnSelectAll, gridBagConstraints);

        btnDeselectAll.setMnemonic('D');
        btnDeselectAll.setText("Deselect All");
        btnDeselectAll.setMaximumSize(new java.awt.Dimension(87, 25));
        btnDeselectAll.setMinimumSize(new java.awt.Dimension(175, 23));
        btnDeselectAll.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 125, 3, 0);
        pnlInclude.add(btnDeselectAll, gridBagConstraints);

        chkOrganization.setText("Organization");
        chkOrganization.setToolTipText("Organization");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        pnlInclude.add(chkOrganization, gridBagConstraints);

        chkAmendRenew.setText("Amendment & Renewal");
        chkAmendRenew.setToolTipText("Amendment & Renewal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        pnlInclude.add(chkAmendRenew, gridBagConstraints);

        chkReferences.setText("References");
        chkReferences.setToolTipText("References");
        chkReferences.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkReferences.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 55, 0, 0);
        pnlInclude.add(chkReferences, gridBagConstraints);

        chkRoles.setText("Protocol Roles");
        chkRoles.setToolTipText("Protocol Roles");
        chkRoles.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkRoles.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 55, 0, 0);
        pnlInclude.add(chkRoles, gridBagConstraints);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Questionnaire Forms"));
        jScrollPane1.setMaximumSize(new java.awt.Dimension(490, 125));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(490, 125));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(490, 125));
        pnlQuestionnaire.setLayout(new java.awt.GridBagLayout());

        pnlQuestionnaire.setPreferredSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setViewportView(pnlQuestionnaire);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(jScrollPane1, gridBagConstraints);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Protocol Attachments"));
        jScrollPane2.setMaximumSize(new java.awt.Dimension(490, 125));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(490, 125));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(490, 125));
        pnlProtocolAttachments.setLayout(new java.awt.GridBagLayout());

        pnlProtocolAttachments.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        pnlProtocolAttachments.setMinimumSize(new java.awt.Dimension(0, 0));
        pnlProtocolAttachments.setPreferredSize(new java.awt.Dimension(1, 1));
        jScrollPane2.setViewportView(pnlProtocolAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(jScrollPane2, gridBagConstraints);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Attachments"));
        jScrollPane3.setMaximumSize(new java.awt.Dimension(490, 125));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(490, 125));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(490, 125));
        pnlOtherAttachments.setLayout(new java.awt.GridBagLayout());

        pnlOtherAttachments.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        pnlOtherAttachments.setMinimumSize(new java.awt.Dimension(0, 0));
        pnlOtherAttachments.setPreferredSize(new java.awt.Dimension(1, 1));
        jScrollPane3.setViewportView(pnlOtherAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(pnlInclude, gridBagConstraints);

        btnPrint.setMnemonic('P');
        btnPrint.setText("Print");
        btnPrint.setMaximumSize(new java.awt.Dimension(85, 25));
        btnPrint.setMinimumSize(new java.awt.Dimension(85, 25));
        btnPrint.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 6, 1, 5);
        add(btnPrint, gridBagConstraints);

        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(85, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(85, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 5);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    // 4272: Maintain History of Questionnaires - Start
    private void setModuleDetailsForQuestionnaire() {
        if(questionnaire != null && questionnaire.size() > 0){
            for(int index=0;index<questionnaire.size();index++){
                QuestionnaireAnswerHeaderBean questionAnsHeaderBean = (QuestionnaireAnswerHeaderBean)questionnaire.get(index);           
                questionAnsHeaderBean.setModuleItemCode(PROTOCOL_MODULE);
                questionAnsHeaderBean.setModuleItemKey(protocolInfoBean.getProtocolNumber());
                //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                int subModuleItemCode = 0; //Normal Protocol
                if(protocolInfoBean.getProtocolNumber().indexOf("A") > -1 || protocolInfoBean.getProtocolNumber().indexOf("R") > -1){
                    subModuleItemCode = 1; // Amendment/Renewal
                }
                //COEUSDEV-86 : END
                questionAnsHeaderBean.setModuleItemDescription(CoeusGuiConstants.PROTOCOL_MODULE);
                //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                questionAnsHeaderBean.setModuleSubItemCode(subModuleItemCode);
                //COEUSDEV-86 : END
                questionAnsHeaderBean.setModuleSubItemKey(CoeusGuiConstants.EMPTY_STRING+protocolInfoBean.getSequenceNumber());
                questionAnsHeaderBean.setModuleSubItemDescription(CoeusGuiConstants.PROTOCOL_MODULE); 
                questionAnsHeaderBean.setPrintAnswers(true);
                questionAnsHeaderBean.setPrintAll(true);
            }
        }
    }
    // 4272: Maintain History of Questionnaires - End
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnDeselectAll;
    public javax.swing.JButton btnPrint;
    public javax.swing.JButton btnSelectAll;
    public javax.swing.JCheckBox chkActions;
    public javax.swing.JCheckBox chkAmendRenew;
    public javax.swing.JCheckBox chkAreaOfResearch;
    public javax.swing.JCheckBox chkCorrespondents;
    public javax.swing.JCheckBox chkFundingSource;
    public javax.swing.JCheckBox chkInvestigator;
    public javax.swing.JCheckBox chkNotes;
    public javax.swing.JCheckBox chkOrganization;
    public javax.swing.JCheckBox chkOthers;
    public javax.swing.JCheckBox chkProtocolDetail;
    public javax.swing.JCheckBox chkReferences;
    public javax.swing.JCheckBox chkRiskLevel;
    public javax.swing.JCheckBox chkRoles;
    public javax.swing.JCheckBox chkSpecialReview;
    public javax.swing.JCheckBox chkStudyPersonnel;
    public javax.swing.JCheckBox chkSubjects;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JPanel pnlInclude;
    public javax.swing.JPanel pnlOtherAttachments;
    public javax.swing.JPanel pnlProtocolAttachments;
    public javax.swing.JPanel pnlQuestionnaire;
    // End of variables declaration//GEN-END:variables
    
}
