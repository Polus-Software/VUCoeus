/*
 * TemplateMainController.java
 *
 * Created on Dec 15, 2004, 10:47 AM
 */
/* PMD check performed, and commented unused imports and variables on 2-Sept-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateCommentsBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.admin.gui.TemplateMainForm;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.brokers.*;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * * @author ajaygm
 */
public class TemplateMainController extends AwardTemplateController implements ItemListener, ActionListener, FocusListener, MouseListener, BeanUpdatedListener,Observer{
    
    private CoeusAppletMDIForm mdiForm;
    private TemplateBaseBean templateBaseBean;
    private TemplateMainForm templateMainForm = new TemplateMainForm();
    //    private AwardHeaderBean  awardHeaderBean;
//    private AwardDetailsBean awardDetailsBean;
    private AwardTemplateCommentsBean awardTemplateCommentsBean;
    private AwardTemplateBean awardTemplateBean;
    private QueryEngine queryEngine = QueryEngine.getInstance();
    private CoeusMessageResources  coeusMessageResources;
    
    private  CoeusVector cvAwardTemplateBean;
//    private CoeusVector cvAwardDetails;
//    private CoeusVector cvTemplate;
    private CoeusVector cvBasis;
    private CoeusVector cvMethod;
    private CoeusVector cvInvFrequency;
    private CoeusVector cvProposalDue;
    private CoeusVector cvStatus;
    
    /**
     * This is used to hold the mode .
     * D for Display, I for Add, U for Modify.
     * This is required for
     */
    private char functionType;
    
    private Integer INVOICE_INSTRUCTION_COMMENT_CODE = 0;
    
    private static final String DISPLAY_SPONSOR = "Display Sponsor";
    
    private static final String SELECT_BASIS = "awardOtherHeader_exceptionCode.1101";
    
    private static final String SELECT_METHOD = "awardOtherHeader_exceptionCode.1102";
    
    private static final String SELECT_PAYMENT_FREQUENCY = "awardOtherHeader_exceptionCode.1103";
    
    private static final String SELECT_INVOICE_INSTR = "awardOtherHeader_exceptionCode.1104";
    
    private static final String INVALID_SPONSOR_CODE = "awardTemplateExceptionCode.1605";
    
    private static final String ENTER_VALID_SPONSOR_CODE = "awardDetail_exceptionCode.1059";
    
    //private static final String SELECT_OTHER_TEMPLATE = "awardOtherHeader_exceptionCode.1105";
    
    private static final String ENTER_DESCRIPTION = "adminAward_exceptionCode.1354";
    
    private static final String ENTER_STATUS_CODE = "adminAward_exceptionCode.1355";
    
    private static final String FINAL_INVOICE_DUE = "awardTemplateExceptionCode.1607";
    
    private static final String FREQUENCY_INDICATOR = "B";
    
    private static final String INVEST_INSTRUCTIONS_INDICATOR = "B";
    
    private static final String FREQUENCY_MANDATORY = "M";
    
    private static final String INVEST_INSTRUCTIONS_MANDATORY = "M";
    
    private static final String CODE = "code";
    
    private Component components[]; //Holds all the screen components
    
    /*For Bug Fix:1666 start step:1*/
    private static final char GET_VALID_SPONSOR_CODE = 'P';
    private static final String ROLODEX_SERVLET = "/rolMntServlet";
    private static final String EMPTY_STRING = "";
    private static final String SERVER_ERROR = "Server Error";
    private static final String COULD_NOT_CONTACT_SERVER = "Could Not Contact Server";
    /* end step:1*/
    //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
    private BaseWindowObservable observable;
    //COEUSDEV-562: End
    //private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    //"/AwardMaintenanceServlet";
    
    //private static final char GET_TEMPLATE_DETAILS = 'J';
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - Start
    boolean primeSponsorChanged = false;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    
    /** Creates a new instance of TemplateMainController */
    public TemplateMainController(TemplateBaseBean templateBaseBean, char functionType) {
        super(templateBaseBean);
        this.mdiForm = mdiForm;
        
        queryEngine = QueryEngine.getInstance();
        //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
        observable = new BaseWindowObservable();
        //COEUSDEV-562: End
        coeusMessageResources = CoeusMessageResources.getInstance();
        //        cvHeaderBean = new CoeusVector();
        cvAwardTemplateBean = new CoeusVector();
//        cvTemplate = new CoeusVector();
        cvBasis = new CoeusVector();
        cvMethod = new CoeusVector();
        cvInvFrequency = new CoeusVector();
        cvProposalDue = new CoeusVector();
        cvStatus = new CoeusVector();
        
        initComponents();
        this.functionType = functionType;
        setFunctionType(functionType);
        setFormData(templateBaseBean);
        registerComponents();
        
    }
    public void setFormData(Object object) {
        try{
//            int  code=0;
            Equals eqPropDueFlag,eqInvoiceFlag;
            String primeSponsorCode = "";
            this.templateBaseBean = (TemplateBaseBean)object;
            
            CoeusVector cvInstr = queryEngine.getDetails(queryKey,KeyConstants.AWARD_STATUS);
            if(cvInstr != null && cvInstr.size() > 0){
                String invInstr = (String)cvInstr.get(2);
                INVOICE_INSTRUCTION_COMMENT_CODE = new Integer(invInstr);
            }
            /*@todo*/          // cvHeaderBean = queryEngine.executeQuery(queryKey,AwardHeaderBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            cvAwardTemplateBean = queryEngine.executeQuery(queryKey,AwardTemplateBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //            cvTemplate = queryEngine.getDetails(queryKey, TemplateBean.class);
            
            if(cvAwardTemplateBean!= null && cvAwardTemplateBean.size() > 0){
                awardTemplateBean = (AwardTemplateBean)cvAwardTemplateBean.get(0);
                /*Just check for the 'code' not required @todo*/                //code = awardHeaderBean.getAwardTypeCode();
            } else {
                awardTemplateBean = new AwardTemplateBean();
                //                awardHeaderBean = new AwardHeaderBean();
                //                awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                //                awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                
            }
            
            //                cvAwardDetails = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            //                if (cvAwardDetails != null && cvAwardDetails.size() > 0) {
            //                    awardDetailsBean = (AwardDetailsBean) cvAwardDetails.get(0);
            //                }else {
            //                    awardDetailsBean = new AwardDetailsBean();
            //                    //                awardDetailsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            //                    //                awardDetailsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            //                }
            
            //            eqAwardType = new Equals("awardTypeCode", new Integer(code));
            cvBasis = queryEngine.getDetails(queryKey, KeyConstants.BASIS_OF_PAYMENT);
            cvStatus = queryEngine.getDetails(queryKey, ComboBoxBean.class);
            
            eqInvoiceFlag = new Equals("invoiceFlag", "Y");
            cvInvFrequency = queryEngine.executeQuery(queryKey, FrequencyBean.class, eqInvoiceFlag);
            cvInvFrequency.sort("description");
            
            eqPropDueFlag = new Equals("proposalDueFlag", "Y");
            cvProposalDue  = queryEngine.executeQuery(queryKey, FrequencyBean.class, eqPropDueFlag);
            
            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
            //            cvTemplate.add(0, emptyBean);
            cvBasis.add(0, emptyBean);
            //cvMethod.add(0, emptyBean);
            cvInvFrequency.add(0, emptyBean);
            cvProposalDue.add(0, emptyBean);
            cvStatus.add(0, emptyBean);
            
            //             otherHeaderForm.cmbTemplate.setModel(new DefaultComboBoxModel(cvTemplate));
            
            //             otherHeaderForm.cmbTemplate.setShowCode(true);
            
            templateMainForm.cmbStatus.setModel(new DefaultComboBoxModel(cvStatus));
            templateMainForm.cmbBasis.setModel(new DefaultComboBoxModel(cvBasis));
            //otherHeaderForm.cmbMethod.setModel(new DefaultComboBoxModel(cvMethod));
            templateMainForm.cmbPaymentFrequency.setModel(new DefaultComboBoxModel(cvInvFrequency));
            templateMainForm.cmbCompetingRenewal.setModel(new DefaultComboBoxModel(cvProposalDue));
            templateMainForm.cmbNonCompeting.setModel(new DefaultComboBoxModel(cvProposalDue));
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            //setting updateTimeStamp details of the main tab
            if( awardTemplateBean.getUpdateTimestamp() != null){
            String lastUpdate = CoeusDateFormat.format(awardTemplateBean.getUpdateTimestamp().toString());
            templateMainForm.txtLastUpdate.setText(lastUpdate);
            String updateUserName = awardTemplateBean.getUpdateUserName();
            templateMainForm.txtUpdateUser.setText(updateUserName);
            }         
            if(functionType != TypeConstants.COPY_MODE &&
                    awardTemplateBean != null && awardTemplateBean.getUpdateTimestamp() != null){
                templateMainForm.lblLastUpdate.setVisible(true);
                templateMainForm.txtLastUpdate.setVisible(true);
                templateMainForm.lblUpdateUser.setVisible(true);
                templateMainForm.txtUpdateUser.setVisible(true);
                templateMainForm.txtLastTemplateUpdate.setVisible(true);
//                templateMainForm.txtLastUpdate.setText(CoeusDateFormat.format(awardTemplateBean.getUpdateTimestamp().toString()));
//                templateMainForm.txtLastUpdate.setText(awardTemplateBean.getUpdateUserName());
                try {
                    CoeusVector cvUpdateDetails = queryEngine.executeQuery(queryKey, "TEMPLATE_MAIN_UPDATE_DETAIL", CoeusVector.FILTER_ACTIVE_BEANS);
                    if(cvUpdateDetails != null && cvUpdateDetails.size() > 0) {
                        AwardTemplateBean updateDetail = (AwardTemplateBean)cvUpdateDetails.get(0);
                        if(functionType != TypeConstants.COPY_MODE &&
                                updateDetail.getUpdateTimestamp() != null){
                            String lastUpdate = CoeusDateFormat.format(updateDetail.getUpdateTimestamp().toString());
                            String updateUserName = updateDetail.getUpdateUserName();
                            //Commented and added to set the last template details to the main teb
                            //templateMainForm.txtLastUpdate.setText(lastUpdate);
                            //templateMainForm.txtUpdateUser.setText(updateUserName);
                            templateMainForm.txtLastTemplateUpdate.setText("Last Template Update: "+lastUpdate+" : "+updateUserName);
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }else{
//                templateMainForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
//                templateMainForm.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
                templateMainForm.lblLastUpdate.setVisible(false);
                templateMainForm.txtLastUpdate.setVisible(false);
                templateMainForm.lblUpdateUser.setVisible(false);
                templateMainForm.txtUpdateUser.setVisible(false);
                templateMainForm.txtLastTemplateUpdate.setVisible(false);
            }
            //COEUSQA-1456 : End
            CoeusVector cvSelectedCode = new CoeusVector();
            
            //if ( !(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD) ) {
            //Set the Award Template
            
            //                Equals selectedTemplate = new Equals(CODE, EMPTY + awardDetailsBean.getTemplateCode());
            //                cvSelectedCode = cvTemplate.filter(selectedTemplate);
            //                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
            //                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
            ////                    otherHeaderForm.cmbTemplate.setSelectedItem(comboBoxBean);
            //                }
            
            if(getFunctionType() == TypeConstants.NEW_MODE){
                templateMainForm.txtTemplateCode.setText(""+templateBaseBean.getTemplateCode());
            }else{
                templateMainForm.txtTemplateCode.setText(""+awardTemplateBean.getTemplateCode());
                templateMainForm.txtDescription.setText(awardTemplateBean.getDescription());
            }
            
            //set Status
            String statusCode = new Integer(awardTemplateBean.getStatusCode()).toString();
            Equals selectedStatusCode = new Equals(CODE,statusCode);
            cvSelectedCode = cvStatus.filter(selectedStatusCode);
            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                templateMainForm.cmbStatus.setSelectedItem(comboBoxBean);
            }
            
            //Set Non-Competing
            String nonCompeting = new Integer(awardTemplateBean.getNonCompetingContPrpslDue()).toString();
            Equals selectedNonCompeting = new Equals(CODE, nonCompeting);
            cvSelectedCode = cvProposalDue.filter(selectedNonCompeting);
            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                templateMainForm.cmbNonCompeting.setSelectedItem(comboBoxBean);
            }
            
            //Set Competing Renewal
            String competing = new Integer(awardTemplateBean.getCompetingRenewalPrpslDue()).toString();
            Equals selectedCompeting = new Equals(CODE, competing);
            cvSelectedCode = cvProposalDue.filter(selectedCompeting);
            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                templateMainForm.cmbCompetingRenewal.setSelectedItem(comboBoxBean);
            }
            
            
            //Set the Payment Basis
            String basisCode = new Integer(awardTemplateBean.getBasisOfPaymentCode()).toString();
            Equals selectedBasis = new Equals(CODE, basisCode);
            cvSelectedCode = cvBasis.filter(selectedBasis);
            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                templateMainForm.cmbBasis.setSelectedItem(comboBoxBean);
            }
            
            /**
             * Set the Payment Method. For this first get the valid payment methods
             * for the selected Payment Basis. Then set the payment method selected.
             *@todo
             */
            /*@todo*/              Equals eqBasis = new Equals("basisOfPaymentCode", new Integer(awardTemplateBean.getBasisOfPaymentCode()));
            cvMethod = queryEngine.executeQuery(queryKey, ValidBasisMethodPaymentBean.class, eqBasis);
            cvMethod.add(0, emptyBean);
            templateMainForm.cmbMethod.setModel(new DefaultComboBoxModel(cvMethod));
            
            //Set the selected Payment Method
            String methodCode = new Integer(awardTemplateBean.getMethodOfPaymentCode()).toString();
            Equals selectedMethod = new Equals(CODE, methodCode);
            cvSelectedCode = cvMethod.filter(selectedMethod);
            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                templateMainForm.cmbMethod.setSelectedItem(comboBoxBean);
                validatePaymentMethod();
            }
            
            //Set the selected Payment/Invoice
            String invoiceFreq = new Integer(awardTemplateBean.getPaymentInvoiceFreqCode()).toString();
            Equals selectedFreq = new Equals(CODE, invoiceFreq);
            cvSelectedCode = cvInvFrequency.filter(selectedFreq);
            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                templateMainForm.cmbPaymentFrequency.setSelectedItem(comboBoxBean);
            }
            
            //Set the Prime Sponsor code and Name
            primeSponsorCode = awardTemplateBean.getPrimeSponsorCode();
            templateMainForm.txtSponsor.setText(primeSponsorCode);
            if (primeSponsorCode != null && !primeSponsorCode.equals(EMPTY)) {
                templateMainForm.lblSponsorValue.setText(getSponsorNameForCode(primeSponsorCode));
            } else {
                templateMainForm.lblSponsorValue.setText(EMPTY);
            }
            
            //Set Invoice No. of Copies
            int invCopies = awardTemplateBean.getInvoiceNoOfCopies();
            if (invCopies == 0) {
                templateMainForm.txtInvoiceCopies.setText(EMPTY);
            } else {
                templateMainForm.txtInvoiceCopies.setText(invCopies + EMPTY);
            }
            
            
            //Set Final Invoice Due within
            //                 int invFinalDue = awardHeaderBean.getFinalInvoiceDue();
            Integer invFinalDue = awardTemplateBean.getFinalInvoiceDue();
            if (invFinalDue == null) {
                templateMainForm.txtFinalDue.setText(EMPTY);
            } else {
                templateMainForm.txtFinalDue.setText(invFinalDue + EMPTY);
            }
            
            /**
             * Set the Invoice Instructions by querying the Award Comments and
             * getting the comment for Comment Code = 1
             */
            Equals eqCommentCode = new Equals("commentCode", INVOICE_INSTRUCTION_COMMENT_CODE);
            CoeusVector cvComments = queryEngine.executeQuery(queryKey,
                    AwardTemplateCommentsBean.class, eqCommentCode);
            
            if (cvComments != null && cvComments.size() > 0) {
                awardTemplateCommentsBean = (AwardTemplateCommentsBean) cvComments.get(0);
                templateMainForm.txtArInstructions.setText(awardTemplateCommentsBean.getComments());
            }
            //} //If for New & New Child ends here
            templateMainForm.txtArInstructions.setCaretPosition(0);
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    
    public void saveFormData(){
        
        int code;
        ComboBoxBean comboBoxBean;
        /*@toDo*/
        //            if(isRefreshRequired()) {
        //                //Data modified.get latest and hold it in instance variable.
        //                try{
        //                    CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
        //                    awardDetailsBean = (AwardDetailsBean)cvTemp.get(0);
        //
        //                    cvTemp = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
        //                    awardTemplateBean = (AwardTemplateBean)cvTemp.get(0);
        //
        //                }catch (CoeusException coeusException) {
        //                    coeusException.printStackTrace();
        //                }
        //            }
        
        try {
            
            //Set the Award Template data
            //            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbTemplate.getSelectedItem();
            //            if(! comboBoxBean.getCode().equals(EMPTY)) {
            //                code = Integer.parseInt(comboBoxBean.getCode().trim());
            //                awardDetailsBean.setTemplateCode(code);
            //            }
            
            awardTemplateBean.setTemplateCode(templateBaseBean.getTemplateCode());
            
            //Set the Description
            String strDescription = templateMainForm.txtDescription.getText().trim();
            if(strDescription!=null){
                awardTemplateBean.setDescription(strDescription);
            }
            
            //Set Status
            ComboBoxBean cmbStatus = (ComboBoxBean)templateMainForm.cmbStatus.getSelectedItem();
            if(cmbStatus != null && !EMPTY.equals(cmbStatus.getCode())){
                awardTemplateBean.setStatusCode(Integer.parseInt(cmbStatus.getCode()));
                awardTemplateBean.setStatusDescription(cmbStatus.getDescription());
            }else{
                awardTemplateBean.setStatusDescription(EMPTY);
            }
            
            //Set the Prime Sponsor
            String primeSponsorCode = templateMainForm.txtSponsor.getText();
            if (primeSponsorCode != null) {
                /*modified for the bug fix:1666 step:2 start*/
                awardTemplateBean.setPrimeSponsorCode(getValidSponsorCode(primeSponsorCode));
                /*end step:2*/
            } /*else {
                    awardTemplateBean.setPrimeSponsorCode("");
                }*/
            
            
            //Set the Non Competing
            comboBoxBean = (ComboBoxBean)templateMainForm.cmbNonCompeting.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardTemplateBean.setNonCompetingContPrpslDue(code);
            } else {
                awardTemplateBean.setNonCompetingContPrpslDue(0);
            }
            
            //Set the Competing
            comboBoxBean = (ComboBoxBean)templateMainForm.cmbCompetingRenewal.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardTemplateBean.setCompetingRenewalPrpslDue(code);
            } else {
                awardTemplateBean.setCompetingRenewalPrpslDue(0);
            }
            
            //Set the Payment Basis
            comboBoxBean = (ComboBoxBean)templateMainForm.cmbBasis.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardTemplateBean.setBasisOfPaymentCode(code);
            }
            
            //Set the Payment Method
            comboBoxBean = (ComboBoxBean)templateMainForm.cmbMethod.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardTemplateBean.setMethodOfPaymentCode(code);
            }
            
            //Set the Payment Invoice/Frequency
            comboBoxBean = (ComboBoxBean)templateMainForm.cmbPaymentFrequency.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardTemplateBean.setPaymentInvoiceFreqCode(code);
            } else {
                awardTemplateBean.setPaymentInvoiceFreqCode(0);
            }
            
            //Set the Invoice No. of Copies
            String invCopies = templateMainForm.txtInvoiceCopies.getText();
            if (invCopies == null || invCopies.equals(EMPTY)) {
                awardTemplateBean.setInvoiceNoOfCopies(0);
            } else {
                awardTemplateBean.setInvoiceNoOfCopies(new Integer(invCopies).intValue());
            }
            
            //Set the Final Invoice Due
            String invDue = templateMainForm.txtFinalDue.getText();
            if (invDue == null || invDue.equals(EMPTY)) {
                //                awardHeaderBean.setFinalInvoiceDue(0);
                awardTemplateBean.setFinalInvoiceDue(null);
            } else {
                awardTemplateBean.setFinalInvoiceDue(new Integer(invDue));
            }
            
            //Set the Invoice Instructions in the Award Comments for comment code 1
            String invoiceInstr = templateMainForm.txtArInstructions.getText().trim();
            if (invoiceInstr != null && invoiceInstr.length() > 0) {
                if (awardTemplateCommentsBean == null) {
                    awardTemplateCommentsBean = new AwardTemplateCommentsBean();
                    
                    //                    awardCommentsBean.setMitAwardNumber(awardDetailsBean.getMitAwardNumber());
                    //                    awardCommentsBean.setSequenceNumber(awardDetailsBean.getSequenceNumber());
                    
                    awardTemplateCommentsBean.setTemplateCode(templateBaseBean.getTemplateCode());
                    awardTemplateCommentsBean.setCommentCode(INVOICE_INSTRUCTION_COMMENT_CODE.intValue());
                    awardTemplateCommentsBean.setCheckListPrintFlag(true);
                    awardTemplateCommentsBean.setAcType(TypeConstants.INSERT_RECORD);
                    awardTemplateCommentsBean.setComments(invoiceInstr);
                } else {
                    awardTemplateCommentsBean.setComments(invoiceInstr);
                }
            } else if (awardTemplateCommentsBean != null) {
                awardTemplateCommentsBean.setComments(EMPTY);
            }
            
            //Update only if Data is Changed
            //Check if Data Changed - START
            StrictEquals strictEquals = new StrictEquals();
            
            //                AwardDetailsBean qryAwardDetailsBean = new AwardDetailsBean();
            //                AwardHeaderBean qryAwardHeaderBean = new AwardHeaderBean();
            AwardTemplateBean qryAwardTemplateBean = new AwardTemplateBean();
            AwardTemplateCommentsBean qryAwardsCommentsBean = new AwardTemplateCommentsBean();
            
            //                CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            //                if(cvTemp != null && cvTemp.size() > 0) {
            //                    qryAwardDetailsBean = (AwardDetailsBean)cvTemp.get(0);
            //                }
            //
            CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardTemplateBean.class);
            if(cvTemp != null && cvTemp.size() > 0) {
                qryAwardTemplateBean = (AwardTemplateBean)cvTemp.get(0);
            }
            
            
            Equals eqCommentCode = new Equals("commentCode", INVOICE_INSTRUCTION_COMMENT_CODE);
            //Bug Fix : 1068 - START
            //cvTemp = queryEngine.executeQuery(queryKey,
            //    AwardCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //Above 2 lines of code is commented for the purpose bug fix. it was the prev code.
            cvTemp = queryEngine.executeQuery(queryKey,
                    AwardTemplateCommentsBean.class, eqCommentCode);
            //Bug Fix : 1068 - END
            
            if(cvTemp != null && cvTemp.size() > 0) {
                qryAwardsCommentsBean = (AwardTemplateCommentsBean)cvTemp.get(0);
            }
            
            //Update Award Details
            /*@todo*/
            //          if(!strictEquals.compare(awardDetailsBean, qryAwardDetailsBean)){
            //                if(getFunctionType() == CORRECT_AWARD) {
            //                    awardDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
            //                    queryEngine.update(queryKey, awardDetailsBean);
            //                }else {
            //                    awardDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
            //                    queryEngine.insert(queryKey, awardDetailsBean);
            //                }
            
            //                //Fire Award Details Modified event
            //                BeanEvent beanEvent = new BeanEvent();
            //                beanEvent.setSource(this);
            //                beanEvent.setBean(awardDetailsBean);
            //                fireBeanUpdated(beanEvent);
            //            }
            
            //Update Award Template
            if(!strictEquals.compare(awardTemplateBean, qryAwardTemplateBean)) {
                if(getFunctionType() == TypeConstants.MODIFY_MODE) {
                    awardTemplateBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, awardTemplateBean);
                }else {
                    awardTemplateBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, awardTemplateBean);
                }
                
                //Fire Award Header Modified event
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(awardTemplateBean);
                fireBeanUpdated(beanEvent);
            }
            
            //Update Award Comments
            if(awardTemplateCommentsBean != null && !strictEquals.compare(awardTemplateCommentsBean,
                    qryAwardsCommentsBean)) {
                if(awardTemplateCommentsBean.getAcType() != null && !awardTemplateCommentsBean.
                        getAcType().equals(TypeConstants.INSERT_RECORD)) {
                    awardTemplateCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, awardTemplateCommentsBean);
                }else {
                    queryEngine.insert(queryKey, awardTemplateCommentsBean);
                }
                //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
                HashMap hmDetails = new HashMap();
                hmDetails.put(Integer.class,INVOICE_INSTRUCTION_COMMENT_CODE);
                hmDetails.put(AwardTemplateCommentsBean.class,awardTemplateCommentsBean);
                observable.notifyObservers(hmDetails);
                //COEUSDEV-562: End
            }
        } catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
    }
    
    /** added for the Bug Fix:1666 for alpha numeric sponsor code step:3
     * contacts the server and fetches the valid Sponsor code for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor code
     * @param sponsorCode sponsor code for which valid sponsor code has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    private  String getValidSponsorCode(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_VALID_SPONSOR_CODE);
        requesterBean.setDataObject(sponsorCode);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusException(COULD_NOT_CONTACT_SERVER);
        }else if(!responderBean.isSuccessfulResponse()) {
            throw new CoeusException(SERVER_ERROR);
        }
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
        if(responderBean.getDataObject() == null) return EMPTY_STRING;
        String validSponsorCode = responderBean.getDataObject().toString();
        return validSponsorCode;
    }/*end step:3*/
    
    public void display() {
    }
    
    public void formatFields() {
        if(getFunctionType() == TypeConstants.NEW_MODE){
            templateMainForm.txtDescription.setText(EMPTY);
            templateMainForm.txtArInstructions.setText(EMPTY);
        }
        
        templateMainForm.lblSponsorValue.setText(EMPTY);
        templateMainForm.txtSponsor.setDocument(new LimitedPlainDocument(6));
        templateMainForm.txtInvoiceCopies.setHorizontalAlignment(JTextField.RIGHT);
        templateMainForm.txtFinalDue.setHorizontalAlignment(JTextField.RIGHT);
        
        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
                getDefaults().get("Panel.background");
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            templateMainForm.txtArInstructions.setDisabledTextColor(Color.BLACK);
        }
        
        
        //If in display mode disable editable fields else enable.
        boolean enabled = !(getFunctionType() == TypeConstants.DISPLAY_MODE);
        //Disable button if in display mode.
        templateMainForm.btnSearchSponsor.setEnabled(enabled);
        for(int count = 0; count < components.length; count++) {
            components[count].setEnabled(enabled);
            if(enabled && !(components[count] instanceof JCheckBox)) {
                components[count].setBackground(Color.white);
            }else {
                components[count].setBackground(disabledBackground);
            }
            
            
            if(!enabled && (components[count] instanceof JTextField)){
                ((JTextField)components[count]).setEditable(false);
                ((JTextField)components[count]).setBackground(disabledBackground);
                ((JTextField)components[count]).setDisabledTextColor(Color.black);
            }else if(enabled && (components[count] instanceof JTextField)){
                ((JTextField)components[count]).setEditable(true);
            }
        }
        
        templateMainForm.txtTemplateCode.setEnabled(false);
        templateMainForm.txtTemplateCode.setEditable(false);
    }
    
    public java.awt.Component getControlledUI() {
        return templateMainForm;
    }
    
    public Object getFormData() {
        return templateMainForm;
    }
    
    public void registerComponents() {
        
        if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
            ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
            templateMainForm.setFocusTraversalPolicy(traversePolicy);
            templateMainForm.setFocusCycleRoot(true);
            
            //otherHeaderForm.txtFinalDue.setHorizontalAlignment(alignment
            //            otherHeaderForm.cmbTemplate.addItemListener(this);
            templateMainForm.cmbBasis.addItemListener(this);
            templateMainForm.cmbCompetingRenewal.addItemListener(this);
            templateMainForm.cmbMethod.addItemListener(this);
            templateMainForm.cmbNonCompeting.addItemListener(this);
            templateMainForm.cmbPaymentFrequency.addItemListener(this);
            templateMainForm.btnSearchSponsor.addActionListener(this);
            templateMainForm.txtSponsor.addFocusListener(this);
            addBeanUpdatedListener(this, AwardHeaderBean.class);
            addBeanUpdatedListener(this, AwardDetailsBean.class);
        }
        templateMainForm.txtSponsor.addMouseListener(this);
        
        
        templateMainForm.cmbNonCompeting.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                        kEvent.getSource() instanceof CoeusComboBox ){
                    templateMainForm.cmbNonCompeting.setModel(new DefaultComboBoxModel(cvProposalDue));
                    templateMainForm.cmbNonCompeting.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        
        templateMainForm.cmbCompetingRenewal.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                        kEvent.getSource() instanceof CoeusComboBox ){
                    templateMainForm.cmbCompetingRenewal.setModel(new DefaultComboBoxModel(cvProposalDue));
                    templateMainForm.cmbCompetingRenewal.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        
        templateMainForm.cmbPaymentFrequency.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                        kEvent.getSource() instanceof CoeusComboBox ){
                    templateMainForm.cmbPaymentFrequency.setModel(new DefaultComboBoxModel(cvInvFrequency));
                    templateMainForm.cmbPaymentFrequency.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
    }
    
    public void unRegisterComponents() {
        
        //        otherHeaderForm.cmbTemplate.removeItemListener(this);
        templateMainForm.cmbBasis.removeItemListener(this);
        templateMainForm.cmbCompetingRenewal.removeItemListener(this);
        templateMainForm.cmbMethod.removeItemListener(this);
        templateMainForm.cmbNonCompeting.removeItemListener(this);
        templateMainForm.cmbPaymentFrequency.removeItemListener(this);
        templateMainForm.btnSearchSponsor.removeActionListener(this);
        templateMainForm.txtSponsor.removeFocusListener(this);
        templateMainForm.txtSponsor.removeMouseListener(this);
        
        
    }
    
    private void resetDropdowns() {
        
        templateMainForm.cmbBasis.setModel(new DefaultComboBoxModel(cvBasis));
        templateMainForm.cmbMethod.removeAllItems();
        templateMainForm.cmbPaymentFrequency.setModel(new DefaultComboBoxModel(cvInvFrequency));
        templateMainForm.cmbCompetingRenewal.setModel(new DefaultComboBoxModel(cvProposalDue));
        templateMainForm.cmbNonCompeting.setModel(new DefaultComboBoxModel(cvProposalDue));
        
    }
    
    private void initComponents() {
        
        components = new Component[] {templateMainForm.txtTemplateCode,
        templateMainForm.txtDescription,templateMainForm.cmbStatus,
        templateMainForm.txtSponsor,templateMainForm.btnSearchSponsor,
        templateMainForm.cmbNonCompeting,templateMainForm.cmbCompetingRenewal,
        templateMainForm.cmbBasis,templateMainForm.cmbMethod,
        templateMainForm.cmbPaymentFrequency,
        templateMainForm.txtInvoiceCopies, templateMainForm.txtFinalDue,
        templateMainForm.txtArInstructions};
        
        //setting documents
        templateMainForm.txtFinalDue.setDocument(new JTextFieldFilter(
                JTextFieldFilter.NUMERIC, 3));
        
        templateMainForm.txtInvoiceCopies.setDocument(new JTextFieldFilter(
                JTextFieldFilter.NUMERIC, 1));
        
    }
    
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        String strDescription = templateMainForm.txtDescription.getText().trim();
        String primeSponsorCode = templateMainForm.txtSponsor.getText();
//        String sponsorName = templateMainForm.lblSponsorValue.getText().trim();
        String strStatus = ((ComboBoxBean)templateMainForm.cmbStatus.getSelectedItem()).getDescription();
        
        if(strDescription != null && strDescription.equals(EMPTY)){
            //Empty Description.
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_DESCRIPTION));
            templateMainForm.txtDescription.requestFocus();
            return false;
        }
        
        if(strStatus != null && strStatus.equals(EMPTY)){
            //Empty Status.
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_STATUS_CODE));
            templateMainForm.cmbStatus.requestFocus();
            return false;
        }
        
        if(!primeSponsorCode.equals(EMPTY) && primeSponsorCode.trim().equals(EMPTY)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_VALID_SPONSOR_CODE));
            templateMainForm.txtSponsor.requestFocus();
            return false;
        }
        
        if(!primeSponsorCode.equals(EMPTY) && !checkSponsor(primeSponsorCode.trim())){
            templateMainForm.txtSponsor.requestFocus();
            return false;
        }
//        if((primeSponsorCode != null && !primeSponsorCode.equals(EMPTY)) &&
//        ((sponsorName == null || sponsorName.equals(EMPTY)))) {
//            //Wrong Sponsor Code. show Error Message.
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
//            templateMainForm.txtSponsor.requestFocus();
//            return false;
//        }
        
        
        
        if(templateMainForm.cmbBasis.getSelectedIndex() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_BASIS));
            templateMainForm.cmbBasis.requestFocus();
            return false;
        }
        if(templateMainForm.cmbMethod.getSelectedIndex() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_METHOD));
            templateMainForm.cmbMethod.requestFocus();
            return false;
        }
        
        ValidBasisMethodPaymentBean methodPaymentBean =
                (ValidBasisMethodPaymentBean)templateMainForm.cmbMethod.getSelectedItem();
        String frequencyIndicator = methodPaymentBean.getFrequencyIndicator();
        String invInstructionsIndicator = methodPaymentBean.getInvInstructionsIndicator();
        
        if (frequencyIndicator != null && frequencyIndicator.equals(FREQUENCY_MANDATORY) &&
                templateMainForm.cmbPaymentFrequency.getSelectedIndex() <= 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    SELECT_PAYMENT_FREQUENCY));
            templateMainForm.cmbPaymentFrequency.requestFocus();
            return false;
        }
        
        String invoiceInstr = templateMainForm.txtArInstructions.getText();
        if (invInstructionsIndicator != null && invInstructionsIndicator.equals(
                INVEST_INSTRUCTIONS_MANDATORY) && (invoiceInstr == null ||
                invoiceInstr.trim().length() == 0)) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_INVOICE_INSTR));
            templateMainForm.txtArInstructions.requestFocus();
            return false;
        }
        
        
        if(templateMainForm.txtFinalDue.getText() != null &&
                !templateMainForm.txtFinalDue.getText().trim().equals(EMPTY)){
            int finalDue =
                    Integer.parseInt(templateMainForm.txtFinalDue.getText().trim());
            if(finalDue > 365){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(FINAL_INVOICE_DUE));
                templateMainForm.txtFinalDue.requestFocus();
                return false;
            }
        }
        return true;
    }
    
    public void itemStateChanged(ItemEvent itemEvent) {
        try{
            
            Equals eqBasis;
            String code  = "";
            Object source = itemEvent.getSource();
            /** Added by chandra. To select the previous item when user selects no button
             *from the message screen
             *bug Id #1107 - Start 03-Sept-2004 - Step1
             */
            //            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
            //                if (source==otherHeaderForm.cmbTemplate) {
            //                    if(!templateChanged){
            //                        oldTemplateName = itemEvent.getItem();
            //                        return;
            //                    }
            //                }
            //            }// End Chandra 03 -Sept 2004-Step1
            
            if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
                if(source.equals(templateMainForm.cmbBasis)){
                    templateMainForm.cmbMethod.removeItemListener(this);
                    templateMainForm.cmbMethod.removeAllItems();
                    templateMainForm.cmbMethod.addItemListener(this);
                    
                    templateMainForm.cmbPaymentFrequency.setEnabled(true);
                    templateMainForm.txtArInstructions.setEditable(true);
                    
                    code = ((ComboBoxBean)templateMainForm.cmbBasis.getSelectedItem()).getCode();
                    eqBasis = new Equals("basisOfPaymentCode",new Integer(code));
                    cvMethod = queryEngine.executeQuery(queryKey, ValidBasisMethodPaymentBean.class, eqBasis);
                    if(cvMethod!= null && cvMethod.size() > 0){
                        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
                        templateMainForm.cmbMethod.addItem(emptyBean);
                        for ( int index = 0; index < cvMethod.size(); index++){
                            templateMainForm.cmbMethod.addItem(
                                    (ComboBoxBean)cvMethod.elementAt(index));
                        }
                    }
                    
                } /*else if (source.equals(otherHeaderForm.cmbTemplate)){
                    // Bug fix 1107 Added by chandra - Step2 - 3-Sept-2004
//                    if (templateChanged) {
//                        templateChanged = false;
//                        return;
//                    }
//                    templateMainForm.cmbPaymentFrequency.setEnabled(true);
//                    templateMainForm.txtArInstructions.setEditable(true);
//                    String mesg = SELECT_OTHER_TEMPLATE;
//                        int selectedOption = CoeusOptionPane.showQuestionDialog(
//                        coeusMessageResources.parseMessageKey(mesg),
//                        CoeusOptionPane.OPTION_YES_NO,
//                        CoeusOptionPane.DEFAULT_YES);
//                    if(selectedOption == CoeusOptionPane.SELECTION_YES){//// Bug fix 1107 Added by chandra - Step2 - 3-Sept-2004
//                        templateChanged = true;
//                        refresh();
//                        templateChanged = false;
//                        ComboBoxBean comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbTemplate.getSelectedItem();
//                        String templateCode = comboBoxBean.getCode();
//                        templateBean = getAwardTemplateData(templateCode);
//                        CoeusVector cvSelectedCode;
//
//                        //If in New Mode then Sync all data for Comments, Terms, Contacts & Reports
////                        if (this.functionType == NEW_AWARD) {
////                            syncData(new Integer(templateCode).intValue());
////                        }
//
//                        //Reset the dropdowns
//                        resetDropdowns();
//                        if (templateBean != null) {
//                            //Set all the data from template
//                            //Set Non-Competing
//                            String nonCompeting = new Integer(templateBean.getNonCompetingContPrpslDue()).toString();
//                            Equals selectedNonCompeting = new Equals(CODE, nonCompeting);
//                            cvSelectedCode = cvProposalDue.filter(selectedNonCompeting);
//                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
//                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
//                                templateMainForm.cmbNonCompeting.setSelectedItem(comboBoxBean);
//                            }
//
//                            //Set Competing Renewal
//                            String competing = new Integer(templateBean.getCompetingRenewalPrpslDue()).toString();
//                            Equals selectedCompeting = new Equals(CODE, competing);
//                            cvSelectedCode = cvProposalDue.filter(selectedCompeting);
//                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
//                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
//                                templateMainForm.cmbCompetingRenewal.setSelectedItem(comboBoxBean);
//                            }
//
//
//                            //Set the Payment Basis
//                            String basisCode = new Integer(templateBean.getBasisOfPaymentCode()).toString();
//                            Equals selectedBasis = new Equals(CODE, basisCode);
//                            cvSelectedCode = cvBasis.filter(selectedBasis);
//                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
//                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
//                                templateMainForm.cmbBasis.setSelectedItem(comboBoxBean);
//                            }
//
//                            /**
//                             * Set the Payment Method. For this first get the valid payment methods
//                             * for the selected Payment Basis. Then set the payment method selected.
//                             */
                //                            eqBasis = new Equals("basisOfPaymentCode", new Integer(templateBean.getBasisOfPaymentCode()));
                //                            cvMethod = queryEngine.executeQuery(queryKey, ValidBasisMethodPaymentBean.class, eqBasis);
                //                            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
                //                            cvMethod.add(0, emptyBean);
                //                            templateMainForm.cmbMethod.setModel(new DefaultComboBoxModel(cvMethod));
                //
                //                            //Set the selected Payment Method
                //                            templateMainForm.cmbMethod.removeItemListener(this);
                //                            String methodCode = new Integer(templateBean.getMethodOfPaymentCode()).toString();
                //                            Equals selectedMethod = new Equals(CODE, methodCode);
                //                            cvSelectedCode = cvMethod.filter(selectedMethod);
                //                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                //                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                //                                templateMainForm.cmbMethod.setSelectedItem(comboBoxBean);
                //                                validatePaymentMethod();
                //                            }
                //                            templateMainForm.cmbMethod.addItemListener(this);
                //
                //                            //Set the selected Payment/Invoice
                //                            String invoiceFreq = new Integer(templateBean.getPaymentInvoiceFreqCode()).toString();
                //                            Equals selectedFreq = new Equals(CODE, invoiceFreq);
                //                            cvSelectedCode = cvInvFrequency.filter(selectedFreq);
                //                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                //                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                //                                templateMainForm.cmbPaymentFrequency.setSelectedItem(comboBoxBean);
                //                            }
                //
                //                             //Set the Prime Sponsor code and Name
                //                             String primeSponsorCode = templateBean.getPrimeSponsorCode();
                //                             templateMainForm.txtSponsor.setText(primeSponsorCode);
                //                             if (primeSponsorCode != null && !primeSponsorCode.equals(EMPTY)) {
                //                                 templateMainForm.lblSponsorValue.setText(getSponsorName(primeSponsorCode));
                //                             } else {
                //                                 templateMainForm.lblSponsorValue.setText(EMPTY);
                //                             }
                //
                //                             //Set Invoice No. of Copies
                //                             int invCopies = templateBean.getInvoiceNumberOfCopies();
                //                             if (invCopies == 0) {
                //                                templateMainForm.txtInvoiceCopies.setText(EMPTY);
                //                             } else {
                //                                templateMainForm.txtInvoiceCopies.setText(invCopies + EMPTY);
                //                             }
                //
                //                             //Set Final Invoice Due within
                //                             int invFinalDue = templateBean.getFinalInvoiceDue();
                //                             if (invFinalDue == 0) {
                //                                 templateMainForm.txtFinalDue.setText(EMPTY);
                //                             } else {
                //                                templateMainForm.txtFinalDue.setText(invFinalDue + EMPTY);
                //                             }
                //
                //                             //Set the Invoice Instructions
                //                             String invInstructions = templateBean.getInvoiceInstructions();
                //                             if (invInstructions == null || invInstructions.length() == 0) {
                //                                templateMainForm.txtArInstructions.setText(EMPTY);
                //                             } else {
                //                                 templateMainForm.txtArInstructions.setText(invInstructions);
                //                             }
                //
                //                        }
                //                // Bug fix 1107 Added by chandra - Step3 - 3-Sept-2004
                //                }else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                //                    templateChanged = true;
                //                    templateMainForm.cmbTemplate.setSelectedItem(oldTemplateName);
                //                    }// Bug fix 1107 Added by chandra - Step3 - 3-Sept-2004
                /*} */else if (source.equals(templateMainForm.cmbMethod) &&
                        templateMainForm.cmbMethod.getSelectedIndex() > 0){
                    validatePaymentMethod();
                }
            }
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            coeusException.printStackTrace();
        }
    }
    
    private void validatePaymentMethod() {
        ValidBasisMethodPaymentBean methodPaymentBean =
                (ValidBasisMethodPaymentBean)templateMainForm.cmbMethod.getSelectedItem();
        String frequencyIndicator = methodPaymentBean.getFrequencyIndicator();
        String invInstructionsIndicator = methodPaymentBean.getInvInstructionsIndicator();
        
        if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
            if (frequencyIndicator.equals(FREQUENCY_INDICATOR)) {
                templateMainForm.cmbPaymentFrequency.setModel(new
                        DefaultComboBoxModel(cvInvFrequency));
                templateMainForm.cmbPaymentFrequency.setEnabled(false);
            } else {
                templateMainForm.cmbPaymentFrequency.setEnabled(true);
            }
            if (invInstructionsIndicator.equals(INVEST_INSTRUCTIONS_INDICATOR)) {
                templateMainForm.txtArInstructions.setText(EMPTY);
                templateMainForm.txtArInstructions.setEditable(false);
            } else {
                templateMainForm.txtArInstructions.setEditable(true);
            }
        }
    }
    
    public void focusLost(FocusEvent focusEvent) {
        
        if(focusEvent.isTemporary()) {
            return ;
        }
        
        Object source = focusEvent.getSource();
        
        if(source.equals(templateMainForm.txtSponsor)) {
            String sponsorName = CoeusGuiConstants.EMPTY_STRING;
            String primeSponsorCode = templateMainForm.txtSponsor.getText().trim();
            
            if(CoeusGuiConstants.EMPTY_STRING.equals(primeSponsorCode)){
                if(!primeSponsorCode.equals(awardTemplateBean.getPrimeSponsorCode())){
                    primeSponsorChanged = true;
                }
                templateMainForm.lblSponsorValue.setText(CoeusGuiConstants.EMPTY_STRING);
                return;
            }
            if(!primeSponsorChanged && !primeSponsorCode.equals(awardTemplateBean.getPrimeSponsorCode())){
                primeSponsorChanged = true;
            }
            try{
                if(primeSponsorChanged){
                    sponsorName= getSponsorName(templateMainForm.txtSponsor.getText());
                    if(CoeusGuiConstants.EMPTY_STRING.equals(sponsorName)){
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
                        templateMainForm.txtSponsor.setText(CoeusGuiConstants.EMPTY_STRING);
                        templateMainForm.lblSponsorValue.setText(CoeusGuiConstants.EMPTY_STRING);
                        templateMainForm.txtSponsor.requestFocusInWindow();
                    }
                }else{
                    sponsorName = getSponsorNameForCode(primeSponsorCode);
                }
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
                sponsorName = CoeusGuiConstants.EMPTY_STRING;
            }
            templateMainForm.lblSponsorValue.setText(sponsorName);
        }//End Sponsor Focus Lost
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if(clickCount != 2) {
            return ;
        }
        //Double Clicked on Sponsor code validate and display sponsor details.
        String sponsorCode, sponsorName = null;
        sponsorCode = templateMainForm.txtSponsor.getText().trim();
        boolean status = checkSponsor(sponsorCode);
        
        if(status){
            //valid sponsor code. display sponsor details.
            SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm(TypeConstants.DISPLAY_MODE, sponsorCode);
            frmSponsor.showForm(mdiForm, DISPLAY_SPONSOR, true);
        }
    }
    
    private boolean checkSponsor(String sponsorCode){
        String sponsorName = null;
        boolean status = false;
        if(!sponsorCode.equals(EMPTY)) {
            try{
                sponsorName = getSponsorName(sponsorCode).trim();
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
        }
        
        if(sponsorCode.equals(EMPTY)) {
            //Sponsor Code not Entered. Do nothing
            return status;
        }else if(sponsorName == null || sponsorName.equals(EMPTY)) {
            //Wrong Sponsor Code. show Error Message.
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
            templateMainForm.txtSponsor.setText(CoeusGuiConstants.EMPTY_STRING);
            templateMainForm.lblSponsorValue.setText(CoeusGuiConstants.EMPTY_STRING);
            templateMainForm.txtSponsor.requestFocusInWindow();
            return status;
        }else{
            status = true;
        }
        
        return status;
    }
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(templateMainForm.btnSearchSponsor)) {
            displaySponsorSearch();
        }
    }
    
    /** Returns the Award header details for the selected template
     * @return TemplateBean
     */
    //    private TemplateBean getAwardTemplateData(String templateCode) {
    //
    //        RequesterBean requester = new RequesterBean();
    //        requester.setFunctionType(GET_TEMPLATE_DETAILS);
    //        requester.setDataObject(new Integer(templateCode));
    //        try{
    //            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
    //            comm.send();
    //            ResponderBean response = comm.getResponse();
    //            if(response.isSuccessfulResponse()){
    //                templateBean = (TemplateBean) response.getDataObject();
    //            }else{
    //                throw new CoeusClientException(response.getMessage());
    //            }
    //        }catch (CoeusClientException coeusClientException){
    //            coeusClientException.printStackTrace();
    //        }
    //        return templateBean;
    //    }
    
    public  void displaySponsorSearch() {
        try{
            int click = sponsorSearch();
            if(click == CANCEL_CLICKED) {
                return ;
            }
            templateMainForm.txtSponsor.setText(getSponsorCode());
            templateMainForm.lblSponsorValue.setText(getSponsorName());
            
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    public boolean isRefreshRequired() {
        boolean retValue;
        
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    public void refresh() {
        if (isRefreshRequired()) {
            //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            if(functionType == TypeConstants.COPY_MODE){
                functionType = TypeConstants.MODIFY_MODE;
            }
            //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - End
            unRegisterComponents();
            setFormData(templateBaseBean);
            registerComponents();
            setRefreshRequired(false);
        }
    }
    
    //    private void syncData(int templateCode) {
    //        syncComments(OTHER_HEADER, templateCode);
    //        syncTerms(OTHER_HEADER, templateCode);
    //        syncContacts(OTHER_HEADER, templateCode);
    //        syncReports(OTHER_HEADER, templateCode);
    //
    //        //Fire Award Header Modified event to notify that Award
    //        BeanEvent beanEvent = new BeanEvent();
    //        beanEvent.setSource(this);
    //        beanEvent.setBean(awardDetailsBean);
    //        fireBeanUpdated(beanEvent);
    //    }
    public void focusGained(FocusEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    /*@toDo*/
    public void beanUpdated(BeanEvent beanEvent) {
//        Object source = beanEvent.getSource();
        
        if (beanEvent.getSource().equals(this)) {
            return;
        }
        
        if (beanEvent.getBean().getClass().equals(AwardHeaderBean.class) ||
                beanEvent.getBean().getClass().equals(AwardDetailsBean.class)) {
            setRefreshRequired(true);
            //            if(beanEvent.getSource().getClass().equals(FundingProposalsController.class)) {
            //               refresh();
            //            }
        }
    }
    
    public void setFocusToDescp(){
        templateMainForm.txtDescription.requestFocus();
    }
    
    
    
    /** Method to clean all objects */
    public void cleanUp() {
        mdiForm = null;
        templateBaseBean = null;
        templateMainForm = null;
        awardTemplateBean= null;
//        awardDetailsBean = null;
        awardTemplateCommentsBean = null;
        //        templateBean = null;
        queryEngine = null;
        coeusMessageResources = null;
        //        cvHeaderBean = null;
        cvAwardTemplateBean = null;
//        cvAwardDetails = null;
//        cvTemplate = null;
        cvBasis = null;
        cvMethod = null;
        cvInvFrequency = null;
        cvProposalDue = null;
        
        removeBeanUpdatedListener(this, AwardDetailsBean.class);
        removeBeanUpdatedListener(this, AwardHeaderBean.class);
    }
    
    //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
    /*
     * Method to register an observer for this observable.
     */
    public void registerObserver(Observer observer) {
        observable.addObserver(observer);
    }
    
    //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
    /**
     * Implementation of abstract method update for Observer
     */
    public void update(Observable o, Object arg) {
        Equals eqCommentCode = new Equals("commentCode", INVOICE_INSTRUCTION_COMMENT_CODE);
        CoeusVector cvComments = null;
        try {
            cvComments = queryEngine.executeQuery(queryKey, AwardTemplateCommentsBean.class, eqCommentCode);
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        
        if (cvComments != null && !cvComments.isEmpty()) {
            awardTemplateCommentsBean = (AwardTemplateCommentsBean) cvComments.get(0);
            templateMainForm.txtArInstructions.setText(awardTemplateCommentsBean.getComments());
        }else{
            awardTemplateCommentsBean = null;
            templateMainForm.txtArInstructions.setText(EMPTY_STRING);
        }
    }
    //COEUSDEV-562: End
}
