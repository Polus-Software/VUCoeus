/*
 * SubcontractGenerateAgreementModification.java
 *
 * Created on February 15, 2012, 2:26 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.subcontract.gui;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.subcontract.bean.SubContractAttachmentBean;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.bean.SubContractFundingSourceBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;

import java.net.URL;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author satheeshkumarkn
 */


public class SubcontractGenerateAgreementModification extends javax.swing.JPanel implements ActionListener{
    
    private static final String DLG_TITLE = "Generate Agreement / Modification";
    private static final String SERVLET = "/ReportConfigServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private Vector vcFDPTemplateChk = new Vector();
    private static final String connectTo = CoeusGuiConstants.CONNECTION_URL;
    private SubContractBean subContractBean;
    private CoeusDlgWindow dlgGenerate;
    private CoeusVector cvFDPTemplates, cvSponsorTemplate, cvFDPMainTemplates;
    private Vector vecSubcontractAttachment;
    private static final String SUBCONTRACT_SERVLET = "/SubcontractMaintenenceServlet";
    private final char GET_ATTACHMENTS_FOR_GENERATE_AGREEMENT = '2';
    private static final char GET_OTHER_ATTACHMENTS = '3';
    private Vector vecSubcontractAttachmentChk = new Vector();
    private Vector vecSponsorAttachmentChk = new Vector();
    private Vector vecFDPMainTemplateChk = new Vector();
    private SubContractFundingSourceBean subContractFundingSourceBean;
    private HashMap hmAttachments;
    private final String NO_ATTACHMENT_SELECTED_FOR_GENERATE = "subcontractGenerate_exceptionCode.1002";
    private final String FDP_TEMPLATE_NO_AVAILABLE = "subcontractGenerate_exceptionCode.1003";
    private final String SELECT_ATLEAST_ONE_FDT_TEMPLATE = "subcontractGenerate_exceptionCode.1004";
    private final String SELECT_ONLY_ONE_FDP_TEMPLATE = "subcontractGenerate_exceptionCode.1005";
    
    /**
     * Creates new form SubcontractGenerateAgreementModification
     */
    public SubcontractGenerateAgreementModification(SubContractBean subContractBean, SubContractFundingSourceBean subContractFundingSourceBean) {
        this.subContractBean = subContractBean;
        this.subContractFundingSourceBean = subContractFundingSourceBean;
        initComponents();
        registerComponents();
        initCheckBox();
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
    * Method used to enable all the check box
    */
    private void enableComponents() {
        
        // FDP Main template
        if(vecFDPMainTemplateChk != null && vecFDPMainTemplateChk.size() > 0){
            for(int index = 0;index < vecFDPMainTemplateChk.size();index++){
                javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecFDPMainTemplateChk.get(index);
                chkAttachment.setSelected(true);
            }
        }
        
        // FDP template Attachment
        if(vcFDPTemplateChk != null && vcFDPTemplateChk.size() > 0){
            for(int index = 0;index < vcFDPTemplateChk.size();index++){
                javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vcFDPTemplateChk.get(index);
                chkAttachment.setSelected(true);
            }
        }
        // FDP Sponsor Attachment
        if(vecSubcontractAttachmentChk != null && vecSubcontractAttachmentChk.size() > 0){
            for(int index = 0;index < vecSubcontractAttachmentChk.size();index++){
                javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecSubcontractAttachmentChk.get(index);
                chkAttachment.setSelected(true);
            }
        }
        // Sibcontract Attachment
        if(vecSponsorAttachmentChk != null && vecSponsorAttachmentChk.size() > 0){
            for(int index = 0;index < vecSponsorAttachmentChk.size();index++){
                javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecSponsorAttachmentChk.get(index);
                chkAttachment.setSelected(true);
            }
        }
    }
    
    /*
     * Method used to display the PrintSummary form
     */
    public void display() {
        enableComponents();
        dlgGenerate = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgGenerate.setTitle(DLG_TITLE);
        dlgGenerate.getContentPane().add(this);
        dlgGenerate.setSize(650, 670);
        dlgGenerate.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                requestDetailFocus();
            }
        });
        dlgGenerate.setResizable(false);
        dlgGenerate.setLocation(CoeusDlgWindow.CENTER);
        dlgGenerate.setVisible(true);
    }
    
    private void requestDetailFocus(){
        pnlInclude.setFocusCycleRoot(true);
    }

    /*
      * Method to get the PDF url and open the URL
      * @return true - if url exist and opened
      */
    private boolean getPDFUrl() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('R');
        Hashtable htDataToSend = getSelectedItems();
        if(htDataToSend.isEmpty()){
            CoeusOptionPane.showWarningDialog(CoeusMessageResources.getInstance().parseMessageKey(NO_ATTACHMENT_SELECTED_FOR_GENERATE));
            return false;
        }
        htDataToSend.put(CoeusConstants.SUBCONTRACT_BEAN, subContractBean);
        if(subContractFundingSourceBean != null){
            htDataToSend.put(CoeusConstants.SUBCONTRACT_SELECTED_FUNDING_SOURCE, subContractFundingSourceBean);
        }
        String queryKey = subContractBean.getSubContractCode() + subContractBean.getSequenceNumber();
        htDataToSend.put(CoeusConstants.SUBCONTRACT_DATA_COLLECTION, QueryEngine.getInstance().getDataCollection(queryKey));

        requesterBean.setDataObject(htDataToSend);
        
        htDataToSend.put(CoeusConstants.LOGGED_IN_USER, CoeusGuiConstants.getMDIForm().getUserId());
        htDataToSend.put(CoeusConstants.REPORT_TYPE,"SubcontractFdpReports");
        requesterBean.setId("SubcontractFdpReports/SubcontractFdpReports");
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
        
        if(vecFDPMainTemplateChk != null && vecFDPMainTemplateChk.size() > 0){
            Vector vecFDPMainTemplate = new Vector();
            for(int index = 0;index < vecFDPMainTemplateChk.size();index++){
                javax.swing.JCheckBox chkFDPTemplate = (javax.swing.JCheckBox)vecFDPMainTemplateChk.get(index);
                if(chkFDPTemplate != null && chkFDPTemplate.isSelected()){
                    vecFDPMainTemplate.add(cvFDPMainTemplates.get(index));
                }
            }
            if(!vecFDPMainTemplate.isEmpty()){
                htSelectedItems.put(CoeusConstants.SUBCONTRACT_FDP_MAIN_TEMPLATE, vecFDPMainTemplate);
            }
        }
        
        if(vcFDPTemplateChk != null && vcFDPTemplateChk.size() > 0){
            Vector vecFDPTemplate = new Vector();
            for(int index = 0;index < vcFDPTemplateChk.size();index++){
                javax.swing.JCheckBox chkFDPTemplate = (javax.swing.JCheckBox)vcFDPTemplateChk.get(index);
                if(chkFDPTemplate != null && chkFDPTemplate.isSelected()){
                    vecFDPTemplate.add(cvFDPTemplates.get(index));
                }
            }
            if(!vecFDPTemplate.isEmpty()){
                htSelectedItems.put(CoeusConstants.SUBCONTRACT_FDP_TEMPLATE_ATTACHMENT, vecFDPTemplate);
            }
        }
        
        if(vecSponsorAttachmentChk != null && vecSponsorAttachmentChk.size() > 0){
            Vector vecSponsorAttacment = new Vector();
            for(int index = 0;index < vecSponsorAttachmentChk.size();index++){
                javax.swing.JCheckBox chkSposnorAttachment = (javax.swing.JCheckBox)vecSponsorAttachmentChk.get(index);
                if(chkSposnorAttachment != null && chkSposnorAttachment.isSelected()){
                    vecSponsorAttacment.add(cvSponsorTemplate.get(index));
                }
            }
            if(!vecSponsorAttacment.isEmpty()){
                htSelectedItems.put(CoeusConstants.SUBCONTRACT_FDP_SPONSOR_ATTACHMENT, vecSponsorAttacment);
            }
        }
        
        if(vecSubcontractAttachmentChk != null && vecSubcontractAttachmentChk.size() > 0){
            Vector vecSubAttachment = new Vector();
            for(int index = 0;index < vecSubcontractAttachmentChk.size();index++){
                javax.swing.JCheckBox chkProtoAttachment = (javax.swing.JCheckBox)vecSubcontractAttachmentChk.get(index);
                if(chkProtoAttachment != null && chkProtoAttachment.isSelected()){
                    vecSubAttachment.add(vecSubcontractAttachment.get(index));
                }
            }
            if(!vecSubAttachment.isEmpty()){
                htSelectedItems.put(CoeusConstants.SUBCONTRACT_ATTACHMENT, vecSubAttachment);
            }
        }
        
        return htSelectedItems;
    }
    
    
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object source =ae.getSource();
        try {
            dlgGenerate.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (source.equals(btnClose)) {
                dlgGenerate.setVisible(false);
            } else if (source.equals(btnPrint)) {
                if(vecFDPMainTemplateChk.isEmpty()){
                    CoeusOptionPane.showWarningDialog(CoeusMessageResources.getInstance().parseMessageKey(FDP_TEMPLATE_NO_AVAILABLE));
                }else{
                    int selectedTemplateCount = 0;
                    for(int index = 0;index < vecFDPMainTemplateChk.size();index++){
                        javax.swing.JCheckBox chkFDPTemplate = (javax.swing.JCheckBox)vecFDPMainTemplateChk.get(index);
                        if(chkFDPTemplate != null && chkFDPTemplate.isSelected()){
                            selectedTemplateCount++;
                        }
                    }
                    if(selectedTemplateCount == 0){
                        CoeusOptionPane.showWarningDialog(CoeusMessageResources.getInstance().parseMessageKey(SELECT_ATLEAST_ONE_FDT_TEMPLATE));
                    }
                    if(selectedTemplateCount == 1){
                        boolean success = getPDFUrl();
                        if (success) {
                            dlgGenerate.setVisible(false);
                        }
                    }else if(selectedTemplateCount > 1){
                        CoeusOptionPane.showWarningDialog(CoeusMessageResources.getInstance().parseMessageKey(SELECT_ONLY_ONE_FDP_TEMPLATE));
                    }
                }
              
               
            } else if (source.equals(btnSelectAll)) {
                
                if(vecFDPMainTemplateChk != null && vecFDPMainTemplateChk.size() > 0){
                    for(int index = 0;index < vecFDPMainTemplateChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecFDPMainTemplateChk.get(index);
                        chkAttachment.setSelected(true);
                    }
                }
                
                if(vcFDPTemplateChk != null && vcFDPTemplateChk.size() > 0){
                    for(int index = 0;index < vcFDPTemplateChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vcFDPTemplateChk.get(index);
                        chkAttachment.setSelected(true);
                    }
                }
                
                if(vecSubcontractAttachmentChk != null && vecSubcontractAttachmentChk.size() > 0){
                    for(int index = 0;index < vecSubcontractAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecSubcontractAttachmentChk.get(index);
                        chkAttachment.setSelected(true);
                    }
                }
                if(vecSponsorAttachmentChk != null && vecSponsorAttachmentChk.size() > 0){
                    for(int index = 0;index < vecSponsorAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecSponsorAttachmentChk.get(index);
                        chkAttachment.setSelected(true);
                    }
                }
                
            } else if (source.equals(btnDeselectAll)) {
                
                if(vecFDPMainTemplateChk != null && vecFDPMainTemplateChk.size() > 0){
                    for(int index = 0;index < vecFDPMainTemplateChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecFDPMainTemplateChk.get(index);
                        chkAttachment.setSelected(false);
                    }
                }
                
                if(vcFDPTemplateChk != null && vcFDPTemplateChk.size() > 0){
                    for(int index = 0;index < vcFDPTemplateChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vcFDPTemplateChk.get(index);
                        chkAttachment.setSelected(false);
                    }
                }
                if(vecSubcontractAttachmentChk != null && vecSubcontractAttachmentChk.size() > 0){
                    for(int index = 0;index < vecSubcontractAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecSubcontractAttachmentChk.get(index);
                        chkAttachment.setSelected(false);
                    }
                }
                if(vecSponsorAttachmentChk != null && vecSponsorAttachmentChk.size() > 0){
                    for(int index = 0;index < vecSponsorAttachmentChk.size();index++){
                        javax.swing.JCheckBox chkAttachment = (javax.swing.JCheckBox)vecSponsorAttachmentChk.get(index);
                        chkAttachment.setSelected(false);
                    }
                }
            }
        } finally {
            dlgGenerate.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    
    /*
     * This method is called from within the constructor to
     * initialize the Check - box in the form.
     */
    private void initCheckBox(){
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        hmAttachments = getAttachmentsForGenerateAgreement();
        if(hmAttachments != null && !hmAttachments.isEmpty()){
            CoeusVector cvFormAttachments = (CoeusVector)hmAttachments.get("SUBCONTRACT_FORM_ATTACHMENTS");
            if(cvFormAttachments != null && !cvFormAttachments.isEmpty()){
                //FDP Templates
                cvFDPMainTemplates = cvFormAttachments.filter(new Equals("templateTypeCode",4));
                if(cvFDPMainTemplates != null && !cvFDPMainTemplates.isEmpty()){
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - Start
                    cvFDPMainTemplates.sort("description",true);
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - End

                    pnlFDPTemplates.setLayout(flowLayout);
                    int fdpTemplateCount = 0;
                    for(Object fdpTemplate :cvFDPMainTemplates ){
                        RTFFormBean fdpTemplateDetails = (RTFFormBean)fdpTemplate;
                        javax.swing.JCheckBox chkFDPTemplate =  new javax.swing.JCheckBox();
                        Dimension max = new Dimension(230,18);
                        chkFDPTemplate.setMaximumSize(max);
                        chkFDPTemplate.setMinimumSize(max);
                        chkFDPTemplate.setPreferredSize(max);
                        fdpTemplateCount++;
                        if(fdpTemplateDetails != null){
                            chkFDPTemplate.setText(fdpTemplateDetails.getDescription());
                            chkFDPTemplate.setToolTipText(fdpTemplateDetails.getDescription());
                            vecFDPMainTemplateChk.add(chkFDPTemplate);
                            pnlFDPTemplates.add(chkFDPTemplate);
                        }
                    }
                    
                    if(fdpTemplateCount > 8){
                        Dimension fdpTemplatePanelSize = new Dimension(480,90+(fdpTemplateCount)*9);
                        scrnFDPTemplates.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        pnlFDPTemplates.setMaximumSize(fdpTemplatePanelSize);
                        pnlFDPTemplates.setMinimumSize(fdpTemplatePanelSize);
                        pnlFDPTemplates.setPreferredSize(fdpTemplatePanelSize);
                    }
                }
                
                //FDP Template Attachments
                cvFDPTemplates = cvFormAttachments.filter(new Equals("templateTypeCode",3));
                
                if(cvFDPTemplates != null && !cvFDPTemplates.isEmpty()){
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - Start
                    cvFDPTemplates.sort("description",true);
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - End
                    pnlFDPTemplateAttachments.setLayout(flowLayout);
                    int fdpTemplateAttachmentCount = 0;
                    for(Object fdpTemplate :cvFDPTemplates ){
                        RTFFormBean fdpTemplateDetails = (RTFFormBean)fdpTemplate;
                        javax.swing.JCheckBox chkFDPTemplateAttachment =  new javax.swing.JCheckBox();
                        Dimension max = new Dimension(230,18);
                        chkFDPTemplateAttachment.setMaximumSize(max);
                        chkFDPTemplateAttachment.setMinimumSize(max);
                        chkFDPTemplateAttachment.setPreferredSize(max);
                        fdpTemplateAttachmentCount++;
                        if(fdpTemplateDetails != null){
                            chkFDPTemplateAttachment.setText(fdpTemplateDetails.getDescription());
                            chkFDPTemplateAttachment.setToolTipText(fdpTemplateDetails.getDescription());
                            vcFDPTemplateChk.add(chkFDPTemplateAttachment);
                            pnlFDPTemplateAttachments.add(chkFDPTemplateAttachment);
                        }
                    }
                    
                    if(fdpTemplateAttachmentCount > 8){
                        Dimension fdpTemplatePanelSize = new Dimension(480,90+(fdpTemplateAttachmentCount)*9);
                        scrnFDPTemplateAttachments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        pnlFDPTemplateAttachments.setMaximumSize(fdpTemplatePanelSize);
                        pnlFDPTemplateAttachments.setMinimumSize(fdpTemplatePanelSize);
                        pnlFDPTemplateAttachments.setPreferredSize(fdpTemplatePanelSize);
                    }
                }
                
                //FDP Sponsor Attachments
                cvSponsorTemplate = cvFormAttachments.filter(new Equals("templateTypeCode",2));
                if(cvSponsorTemplate != null && !cvSponsorTemplate.isEmpty()){
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - Start
                    cvSponsorTemplate.sort("description",true);
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - End
                    pnlSponsorAttachments.setLayout(flowLayout);
                    int sponsorAttachmentCount = 0;
                    for(Object sponsorTemplate :cvSponsorTemplate ){
                        RTFFormBean sponsorAttachmentDetails = (RTFFormBean)sponsorTemplate;
                        javax.swing.JCheckBox chkSponsorAttachment =  new javax.swing.JCheckBox();
                        Dimension max = new Dimension(230,18);
                        chkSponsorAttachment.setMaximumSize(max);
                        chkSponsorAttachment.setMinimumSize(max);
                        chkSponsorAttachment.setPreferredSize(max);
                        sponsorAttachmentCount++;
                        if(sponsorAttachmentDetails != null){
                            chkSponsorAttachment.setText(sponsorAttachmentDetails.getDescription());
                            chkSponsorAttachment.setToolTipText(sponsorAttachmentDetails.getDescription());
                            vecSponsorAttachmentChk.add(chkSponsorAttachment);
                            pnlSponsorAttachments.add(chkSponsorAttachment);
                        }
                    }
                    if(sponsorAttachmentCount > 8){
                        Dimension sponsorAttachPanelSize = new Dimension(480,90+(sponsorAttachmentCount*9));
                        scrnSponsorAttachments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                        pnlSponsorAttachments.setMaximumSize(sponsorAttachPanelSize);
                        pnlSponsorAttachments.setMinimumSize(sponsorAttachPanelSize);
                        pnlSponsorAttachments.setPreferredSize(sponsorAttachPanelSize);
                    }
                }
                
            }
            // Subcontract attachment - start
            pnlSubcontractAttachments.setLayout(flowLayout);
            vecSubcontractAttachment = (Vector)hmAttachments.get("SUBCONTRACT_ATTACHMENTS");
            vecSubcontractAttachment = getPDFFiles(vecSubcontractAttachment);
            CoeusVector cvAttachmentType = null;
            if(vecSubcontractAttachment != null && !vecSubcontractAttachment.isEmpty() ){
                cvAttachmentType = getAttachmentTypesForPrinting();
            }
            
            int subcontractAttachCount = 0;
            for(int subcontractAttachIndex=0;subcontractAttachIndex<vecSubcontractAttachment.size();subcontractAttachIndex++){
                SubContractAttachmentBean subContractAttachmentBean= (SubContractAttachmentBean)vecSubcontractAttachment.get(subcontractAttachIndex);
                if(cvAttachmentType != null && !cvAttachmentType.isEmpty()){
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - Start
                    cvAttachmentType.sort("description",true);
                    // Added for COEUSQA-3666 : Set order of Attachments - Subcontract Module FDP Agreement / Modification - End
                    CoeusVector cvFilteredType = cvAttachmentType.filter(new Equals("typeCode",subContractAttachmentBean.getAttachmentTypeCode()));
                    if(cvFilteredType != null && !cvFilteredType.isEmpty()){
                        javax.swing.JCheckBox chkSubcontractAttachment =  new javax.swing.JCheckBox();
                        Dimension max = new Dimension(230,18);
                        chkSubcontractAttachment.setMaximumSize(max);
                        chkSubcontractAttachment.setMinimumSize(max);
                        chkSubcontractAttachment.setPreferredSize(max);
                        subcontractAttachCount++;
                        if(subContractAttachmentBean != null){
                            chkSubcontractAttachment.setText(subContractAttachmentBean.getDescription());
                            chkSubcontractAttachment.setToolTipText(subContractAttachmentBean.getDescription());
                            vecSubcontractAttachmentChk.add(chkSubcontractAttachment);
                            pnlSubcontractAttachments.add(chkSubcontractAttachment);
                        }
                    }
                }
            }
            
            if(subcontractAttachCount > 8){
                Dimension protoAttachPanelSize = new Dimension(480,90+(subcontractAttachCount)*9);
                scrnSubcontractAttachments.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                pnlSubcontractAttachments.setMaximumSize(protoAttachPanelSize);
                pnlSubcontractAttachments.setMinimumSize(protoAttachPanelSize);
                pnlSubcontractAttachments.setPreferredSize(protoAttachPanelSize);
            }
            // Subcontract attachment - End
        }
    }
    
    
    
    /**
     * Method to get attachments to Generate Agreement / Modification
     * @return hmAttachments - HashMap
     */
    private HashMap getAttachmentsForGenerateAgreement(){
        HashMap hmAttachments = null;
        RequesterBean request = new RequesterBean();
        ResponderBean response = null;
        request.setFunctionType(GET_ATTACHMENTS_FOR_GENERATE_AGREEMENT);
        request.setDataObject(subContractBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo+SUBCONTRACT_SERVLET, request);
        comm.send();
        response = comm.getResponse();
        if(response != null){
            if(response.isSuccessfulResponse()){
                hmAttachments = (HashMap)response.getDataObject();
            }
        }
        return hmAttachments;
    }
    
    /**
     * To filter only pdf attachments from the given vector
     * @param vector containing all attachments
     * @return vector conaining only pdf attachments
     */
    private Vector getPDFFiles(Vector attachmentsData){
        CoeusVector cvFilteredData =  new CoeusVector();
        if(attachmentsData !=null ){
            for(Object docData: attachmentsData){
                SubContractAttachmentBean attachBean = (SubContractAttachmentBean)docData;
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlInclude = new javax.swing.JPanel();
        scrnFDPTemplateAttachments = new javax.swing.JScrollPane();
        pnlFDPTemplateAttachments = new javax.swing.JPanel();
        scrnSponsorAttachments = new javax.swing.JScrollPane();
        pnlSponsorAttachments = new javax.swing.JPanel();
        scrnSubcontractAttachments = new javax.swing.JScrollPane();
        pnlSubcontractAttachments = new javax.swing.JPanel();
        btnSelectAll = new javax.swing.JButton();
        btnDeselectAll = new javax.swing.JButton();
        scrnFDPTemplates = new javax.swing.JScrollPane();
        pnlFDPTemplates = new javax.swing.JPanel();
        btnPrint = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(610, 700));
        setMinimumSize(new java.awt.Dimension(610, 700));
        setPreferredSize(new java.awt.Dimension(610, 700));
        pnlInclude.setLayout(new java.awt.GridBagLayout());

        pnlInclude.setBorder(javax.swing.BorderFactory.createTitledBorder("Include"));
        pnlInclude.setMaximumSize(new java.awt.Dimension(670, 700));
        pnlInclude.setMinimumSize(new java.awt.Dimension(670, 700));
        pnlInclude.setPreferredSize(new java.awt.Dimension(670, 700));
        pnlInclude.setRequestFocusEnabled(false);
        scrnFDPTemplateAttachments.setBorder(javax.swing.BorderFactory.createTitledBorder("FDP Template Attachments"));
        scrnFDPTemplateAttachments.setMaximumSize(new java.awt.Dimension(500, 130));
        scrnFDPTemplateAttachments.setMinimumSize(new java.awt.Dimension(500, 130));
        scrnFDPTemplateAttachments.setPreferredSize(new java.awt.Dimension(500, 130));
        pnlFDPTemplateAttachments.setLayout(new java.awt.GridBagLayout());

        pnlFDPTemplateAttachments.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        pnlFDPTemplateAttachments.setMinimumSize(new java.awt.Dimension(0, 0));
        pnlFDPTemplateAttachments.setPreferredSize(new java.awt.Dimension(0, 0));
        scrnFDPTemplateAttachments.setViewportView(pnlFDPTemplateAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(scrnFDPTemplateAttachments, gridBagConstraints);

        scrnSponsorAttachments.setBorder(javax.swing.BorderFactory.createTitledBorder("Sponsor Attachments"));
        scrnSponsorAttachments.setMaximumSize(new java.awt.Dimension(500, 130));
        scrnSponsorAttachments.setMinimumSize(new java.awt.Dimension(500, 130));
        scrnSponsorAttachments.setPreferredSize(new java.awt.Dimension(500, 130));
        pnlSponsorAttachments.setLayout(new java.awt.GridBagLayout());

        pnlSponsorAttachments.setPreferredSize(new java.awt.Dimension(0, 0));
        scrnSponsorAttachments.setViewportView(pnlSponsorAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(scrnSponsorAttachments, gridBagConstraints);

        scrnSubcontractAttachments.setBorder(javax.swing.BorderFactory.createTitledBorder("Subcontract Attachments"));
        scrnSubcontractAttachments.setMaximumSize(new java.awt.Dimension(500, 130));
        scrnSubcontractAttachments.setMinimumSize(new java.awt.Dimension(500, 130));
        scrnSubcontractAttachments.setPreferredSize(new java.awt.Dimension(500, 130));
        pnlSubcontractAttachments.setLayout(new java.awt.GridBagLayout());

        pnlSubcontractAttachments.setPreferredSize(new java.awt.Dimension(1, 1));
        scrnSubcontractAttachments.setViewportView(pnlSubcontractAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(scrnSubcontractAttachments, gridBagConstraints);

        btnSelectAll.setMnemonic('S');
        btnSelectAll.setText("Select All");
        btnSelectAll.setMinimumSize(new java.awt.Dimension(175, 23));
        btnSelectAll.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
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
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 3, 0);
        pnlInclude.add(btnDeselectAll, gridBagConstraints);

        scrnFDPTemplates.setBorder(javax.swing.BorderFactory.createTitledBorder("FDP Templates"));
        scrnFDPTemplates.setMaximumSize(new java.awt.Dimension(500, 130));
        scrnFDPTemplates.setMinimumSize(new java.awt.Dimension(500, 130));
        scrnFDPTemplates.setPreferredSize(new java.awt.Dimension(500, 130));
        pnlFDPTemplates.setLayout(new java.awt.GridBagLayout());

        scrnFDPTemplates.setViewportView(pnlFDPTemplates);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 3, 6);
        pnlInclude.add(scrnFDPTemplates, gridBagConstraints);

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
    
    /**
     * Method to subcontract atatchments types
     * @return cvAttachmentTypes - CoeusVector
     */
    private CoeusVector getAttachmentTypesForPrinting() {
        CoeusVector cvAttachmentTypes = null;
        String parameterValue = (String)hmAttachments.get("SUBCONTRACT_GEN_AGRMT_ATT_TYPE_INCLUSION");
        if(parameterValue != null && !CoeusGuiConstants.EMPTY_STRING.equals(parameterValue)){
            if(parameterValue != null) {
                String[] attachmentTypes = parameterValue.split(",");
                if(attachmentTypes != null && attachmentTypes.length > 0) {
                    Pattern pattern = Pattern.compile("^[0-9]+");
                    cvAttachmentTypes = new CoeusVector();
                    for(int index = 0; index < attachmentTypes.length; index++) {
                        String attachmentType = attachmentTypes[index].trim();
                        if(pattern.matcher(attachmentType).matches()){
                            CoeusTypeBean coeusTypeBean = new CoeusTypeBean();
                            coeusTypeBean.setTypeCode(Integer.parseInt(attachmentType));
                            cvAttachmentTypes.add(coeusTypeBean);
                        }
                    }
                }
            }
        }
        
        return cvAttachmentTypes;
    }
    
    /**
     * Method to check is atatchment is selected for generating agreement / modification
     * @return isAttachmentExists
     */
    public boolean isAttachmentExists() {
        boolean isAttachmentExists = true;
        if((vcFDPTemplateChk == null || vcFDPTemplateChk.isEmpty()) &&
                    (vecSponsorAttachmentChk == null || vecSponsorAttachmentChk.isEmpty()) &&
                    (vecSubcontractAttachmentChk == null || vecSubcontractAttachmentChk.isEmpty())) {
            isAttachmentExists = false;
        }
        return isAttachmentExists;
    }
    
    // </editor-fold>
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnDeselectAll;
    public javax.swing.JButton btnPrint;
    public javax.swing.JButton btnSelectAll;
    public javax.swing.JPanel pnlFDPTemplateAttachments;
    public javax.swing.JPanel pnlFDPTemplates;
    public javax.swing.JPanel pnlInclude;
    public javax.swing.JPanel pnlSponsorAttachments;
    public javax.swing.JPanel pnlSubcontractAttachments;
    public javax.swing.JScrollPane scrnFDPTemplateAttachments;
    public javax.swing.JScrollPane scrnFDPTemplates;
    public javax.swing.JScrollPane scrnSponsorAttachments;
    public javax.swing.JScrollPane scrnSubcontractAttachments;
    // End of variables declaration//GEN-END:variables
    
}
