/*
 * OtherHeaderController.java
 *
 * Created on March 26, 2004, 12:03 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.controller.AwardController;
import edu.mit.coeus.award.gui.OtherHeaderForm;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.bean.FrequencyBean;
import edu.mit.coeus.sponsormaint.gui.SponsorMaintenanceForm;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.brokers.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * * @author chandru
*/
public class OtherHeaderController extends AwardController implements ItemListener,
    ActionListener, FocusListener, MouseListener, BeanUpdatedListener{
    private CoeusAppletMDIForm mdiForm;
    private AwardBaseBean  awardBaseBean;
    private OtherHeaderForm otherHeaderForm = new OtherHeaderForm();
    private AwardHeaderBean  awardHeaderBean;
    private AwardDetailsBean awardDetailsBean;
    private AwardCommentsBean awardCommentsBean;
    private TemplateBean templateBean;
    private QueryEngine queryEngine = QueryEngine.getInstance();
    private CoeusMessageResources  coeusMessageResources;
    
    private CoeusVector cvHeaderBean;
    private CoeusVector cvAwardDetails;
    private CoeusVector cvTemplate;
    private CoeusVector cvBasis;
    private CoeusVector cvMethod;
    private CoeusVector cvInvFrequency;
    private CoeusVector cvProposalDue;
    // Added by chandra to fix 1107 Bug - 3-Sept - 2004 start
    private Object oldTemplateName;
    private boolean templateChanged = false;
    // Added by chandra to fix 1107 Bug - 3-Sept - 2004 End
    /** 
     * This is used to hold the mode .
     * D for Display, I for Add, U for Modify.
     * This is required for 
     */
    private char functionType;
    
    private final Integer INVOICE_INSTR_COMMENT_CODE = new Integer(1);
    
    private static final String DISPLAY_SPONSOR = "Display Sponsor";
    
    private static final String SELECT_BASIS = "awardOtherHeader_exceptionCode.1101";
    
    private static final String SELECT_METHOD = "awardOtherHeader_exceptionCode.1102";
    
    private static final String SELECT_PAYMENT_FREQUENCY = "awardOtherHeader_exceptionCode.1103";
    
    private static final String SELECT_INVOICE_INSTR = "awardOtherHeader_exceptionCode.1104";
    
    private static final String INVALID_SPONSOR_CODE = "awardDetail_exceptionCode.1051";
    
    private static final String ENTER_VALID_SPONSOR_CODE = "awardDetail_exceptionCode.1059";
    
    private static final String SELECT_OTHER_TEMPLATE = "awardOtherHeader_exceptionCode.1105";
    
    private static final String FREQUENCY_INDICATOR = "B";
    
    private static final String INVEST_INSTRUCTIONS_INDICATOR = "B";
    
    private static final String FREQUENCY_MANDATORY = "M";
    
    private static final String INVEST_INSTRUCTIONS_MANDATORY = "M";
    
    private static final String CODE = "code";
    
    private Component components[]; //Holds all the screen components
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
        "/AwardMaintenanceServlet";
    
    private static final char GET_TEMPLATE_DETAILS = 'J';
  
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    
    /*for bug fix:1666 step:1 start*/
    private static final char GET_VALID_SPONSOR_CODE = 'P';
    private static final String EMPTY_STRING = "";
    private static final String ROLODEX_SERVLET = "/rolMntServlet";
    /*step:2 end*/
    boolean sponsorChanged = false;
    
    /** Creates a new instance of OtherHeaderController */
    public OtherHeaderController(AwardBaseBean awardBaseBean, char functionType) {
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(otherHeaderForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvHeaderBean = new CoeusVector();
        cvTemplate = new CoeusVector();
        cvBasis = new CoeusVector();
        cvMethod = new CoeusVector();
        cvInvFrequency = new CoeusVector();
        cvProposalDue = new CoeusVector();
        
        initComponents();
        this.functionType = functionType;
        setFunctionType(functionType);
        setFormData(awardBaseBean);
        registerComponents();
        
    }
     public void setFormData(Object object) {
        try{
            int  code=0;
            Equals eqAwardType, eqPropDueFlag,eqInvoiceFlag;
            String primeSponsorCode = "";
            this.awardBaseBean = (AwardBaseBean)object;
            cvHeaderBean = queryEngine.executeQuery(queryKey,AwardHeaderBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            cvTemplate = queryEngine.getDetails(queryKey, TemplateBean.class);
            
            if(cvHeaderBean!= null && cvHeaderBean.size() > 0){
                awardHeaderBean = (AwardHeaderBean)cvHeaderBean.get(0);    
                code = awardHeaderBean.getAwardTypeCode();
            } else {
                awardHeaderBean = new AwardHeaderBean();
                awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            }
            
            cvAwardDetails = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            if (cvAwardDetails != null && cvAwardDetails.size() > 0) {
                awardDetailsBean = (AwardDetailsBean) cvAwardDetails.get(0);
            }else {
                awardDetailsBean = new AwardDetailsBean();
                awardDetailsBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                awardDetailsBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
            }
            
            eqAwardType = new Equals("awardTypeCode", new Integer(code));
            cvBasis = queryEngine.executeQuery(queryKey, ValidBasisPaymentBean.class, eqAwardType);

            eqInvoiceFlag = new Equals("invoiceFlag", "Y");
            cvInvFrequency = queryEngine.executeQuery(queryKey, FrequencyBean.class, eqInvoiceFlag);
            cvInvFrequency.sort("description");

            eqPropDueFlag = new Equals("proposalDueFlag", "Y");
            cvProposalDue  = queryEngine.executeQuery(queryKey, FrequencyBean.class, eqPropDueFlag);

            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
            cvTemplate.add(0, emptyBean);
            cvBasis.add(0, emptyBean);
            //cvMethod.add(0, emptyBean);
            cvInvFrequency.add(0, emptyBean);
            cvProposalDue.add(0, emptyBean);

             otherHeaderForm.cmbTemplate.setModel(new DefaultComboBoxModel(cvTemplate));
             //Bug Fix : 1036 - START
//             otherHeaderForm.cmbTemplate.setShowCode(true);
             //Bug Fix : 1036 - END
             otherHeaderForm.cmbBasis.setModel(new DefaultComboBoxModel(cvBasis));
             //otherHeaderForm.cmbMethod.setModel(new DefaultComboBoxModel(cvMethod));
             otherHeaderForm.cmbPaymentFrequency.setModel(new DefaultComboBoxModel(cvInvFrequency));
             otherHeaderForm.cmbCompetingRenewal.setModel(new DefaultComboBoxModel(cvProposalDue));
             otherHeaderForm.cmbNonCompeting.setModel(new DefaultComboBoxModel(cvProposalDue));

             
             //Bug Fix:1399&1455 Start 1
//             otherHeaderForm.cmbBasis.setShowCode(true);
//             otherHeaderForm.cmbPaymentFrequency.setShowCode(true);
//             otherHeaderForm.cmbCompetingRenewal.setShowCode(true);
//             otherHeaderForm.cmbNonCompeting.setShowCode(true);
             //Bug Fix:1399&1455 End 1
             
             
            CoeusVector cvSelectedCode = new CoeusVector();
            
            //if ( !(getFunctionType() == NEW_AWARD || getFunctionType() == NEW_CHILD) ) {
                //Set the Award Template
                
                Equals selectedTemplate = new Equals(CODE, EMPTY + awardDetailsBean.getTemplateCode());
                cvSelectedCode = cvTemplate.filter(selectedTemplate);
                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                    otherHeaderForm.cmbTemplate.setSelectedItem(comboBoxBean);
                }

                //Set Non-Competing 
                String nonCompeting = new Integer(awardHeaderBean.getNonCompetingContPrpslDue()).toString();
                Equals selectedNonCompeting = new Equals(CODE, nonCompeting);
                cvSelectedCode = cvProposalDue.filter(selectedNonCompeting);
                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                    otherHeaderForm.cmbNonCompeting.setSelectedItem(comboBoxBean);
                }

                //Set Competing Renewal
                String competing = new Integer(awardHeaderBean.getCompetingRenewalPrpslDue()).toString();
                Equals selectedCompeting = new Equals(CODE, competing);
                cvSelectedCode = cvProposalDue.filter(selectedCompeting);
                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                    otherHeaderForm.cmbCompetingRenewal.setSelectedItem(comboBoxBean);
                }


                //Set the Payment Basis
                String basisCode = new Integer(awardHeaderBean.getBasisOfPaymentCode()).toString();
                Equals selectedBasis = new Equals(CODE, basisCode);
                cvSelectedCode = cvBasis.filter(selectedBasis);
                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                    otherHeaderForm.cmbBasis.setSelectedItem(comboBoxBean);
                }

                /**
                 * Set the Payment Method. For this first get the valid payment methods 
                 * for the selected Payment Basis. Then set the payment method selected.
                 */
                Equals eqBasis = new Equals("basisOfPaymentCode", new Integer(awardHeaderBean.getBasisOfPaymentCode()));
                cvMethod = queryEngine.executeQuery(queryKey, ValidBasisMethodPaymentBean.class, eqBasis);
                cvMethod.add(0, emptyBean);
                otherHeaderForm.cmbMethod.setModel(new DefaultComboBoxModel(cvMethod));
                    
                //Bug Fix:1399&1455 Start 2
//                otherHeaderForm.cmbMethod.setShowCode(true);
                //Bug Fix:1399&1455 End 2
                
                //Set the selected Payment Method
                String methodCode = new Integer(awardHeaderBean.getMethodOfPaymentCode()).toString();
                Equals selectedMethod = new Equals(CODE, methodCode);
                cvSelectedCode = cvMethod.filter(selectedMethod);
                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                    otherHeaderForm.cmbMethod.setSelectedItem(comboBoxBean);
                    validatePaymentMethod();
                }

                //Set the selected Payment/Invoice
                String invoiceFreq = new Integer(awardHeaderBean.getPaymentInvoiceFreqCode()).toString();
                Equals selectedFreq = new Equals(CODE, invoiceFreq);
                cvSelectedCode = cvInvFrequency.filter(selectedFreq);
                if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                    ComboBoxBean comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                    otherHeaderForm.cmbPaymentFrequency.setSelectedItem(comboBoxBean);
                }

                 //Set the Prime Sponsor code and Name
                 primeSponsorCode = awardHeaderBean.getPrimeSponsorCode();
                 otherHeaderForm.txtSponsor.setText(primeSponsorCode);
                 if (primeSponsorCode != null && !primeSponsorCode.equals(EMPTY)) {
                     otherHeaderForm.lblSponsorValue.setText(getSponsorNameForCode(primeSponsorCode));
                 } else {
                     otherHeaderForm.lblSponsorValue.setText(EMPTY);
                 }

                 //Set Invoice No. of Copies
                 int invCopies = awardHeaderBean.getInvoiceNoOfCopies();
                 if (invCopies == 0) {
                    otherHeaderForm.txtInvoiceCopies.setText(EMPTY);
                 } else {
                    otherHeaderForm.txtInvoiceCopies.setText(invCopies + EMPTY);
                 }
                    

                 //Set Final Invoice Due within
//                 int invFinalDue = awardHeaderBean.getFinalInvoiceDue();
                 Integer invFinalDue = awardHeaderBean.getFinalInvoiceDue();
                 if (invFinalDue == null) {
                     otherHeaderForm.txtFinalDue.setText(EMPTY);
                 } else {
                    otherHeaderForm.txtFinalDue.setText(invFinalDue + EMPTY);
                 }

                 /**
                  * Set the Invoice Instructions by querying the Award Comments and 
                  * getting the comment for Comment Code = 1
                  */
                 Equals eqCommentCode = new Equals("commentCode", INVOICE_INSTR_COMMENT_CODE);
                 CoeusVector cvComments = queryEngine.executeQuery(queryKey, 
                    AwardCommentsBean.class, eqCommentCode);

                 if (cvComments != null && cvComments.size() > 0) {
                     awardCommentsBean = (AwardCommentsBean) cvComments.get(0);
                     otherHeaderForm.txtArInstructions.setText(awardCommentsBean.getComments());
                 }
                 //Added for the cas#301- Award List in Display mode-start
                 else{
                      otherHeaderForm.txtArInstructions.setText("");
                 }
                 //Added for the cas#301- Award List in Display mode-End
            //} //If for New & New Child ends here
             otherHeaderForm.txtArInstructions.setCaretPosition(0);
             sponsorChanged = false;
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    public void saveFormData(){
        
        int code;
        ComboBoxBean comboBoxBean;
        
        if(isRefreshRequired()) {
            //Data modified.get latest and hold it in instance variable.
            try{
                CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
                awardDetailsBean = (AwardDetailsBean)cvTemp.get(0);
                
                cvTemp = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
                awardHeaderBean = (AwardHeaderBean)cvTemp.get(0);
                
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
        }
        
        try {
            
            //Set the Award Template data
            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbTemplate.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardDetailsBean.setTemplateCode(code);
            }//bug fix done by shiji : bug id - 1912 :start
            else {
                awardDetailsBean.setTemplateCode(0);
            }// bug fix : end
           
            /* JM 4-7-2015 we already have a fix for this
            String sponsrCode = getValidSponsorCode(otherHeaderForm.txtSponsor.getText());
            awardHeaderBean.setPrimeSponsorCode(getValidSponsorCode(otherHeaderForm.txtSponsor.getText()));
            awardHeaderBean.setPrimeSponsorName(getSponsorName(sponsrCode));
            */
            //Set the Prime Sponsor
            // Commented for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - Start  
//            String primeSponsorCode = otherHeaderForm.txtSponsor.getText();
//            if (primeSponsorCode != null && !primeSponsorCode.equals(EMPTY_STRING)) {
//                /*modified for the bug fix:1666 start step:2*/
//                awardHeaderBean.setPrimeSponsorCode(getValidSponsorCode(primeSponsorCode));
//                /*end step:2*/
//            } else {
//                awardHeaderBean.setPrimeSponsorCode("");
//            }
            // Commented for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - End  
            //Set the Non Competing
            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbNonCompeting.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardHeaderBean.setNonCompetingContPrpslDue(code);
            } else {
                awardHeaderBean.setNonCompetingContPrpslDue(0);
            }

            //Set the Competing
            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbCompetingRenewal.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardHeaderBean.setCompetingRenewalPrpslDue(code);
            } else {
                awardHeaderBean.setCompetingRenewalPrpslDue(0);
            }

            //Set the Payment Basis
            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbBasis.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardHeaderBean.setBasisOfPaymentCode(code);
            }

            //Set the Payment Method
            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbMethod.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardHeaderBean.setMethodOfPaymentCode(code);
            }

            //Set the Payment Invoice/Frequency
            comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbPaymentFrequency.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                awardHeaderBean.setPaymentInvoiceFreqCode(code);
            } else {
                awardHeaderBean.setPaymentInvoiceFreqCode(0);
            }

            //Set the Invoice No. of Copies
            String invCopies = otherHeaderForm.txtInvoiceCopies.getText();
            if (invCopies == null || invCopies.equals(EMPTY)) {
                awardHeaderBean.setInvoiceNoOfCopies(0);
            } else {
                awardHeaderBean.setInvoiceNoOfCopies(new Integer(invCopies).intValue());
            }

            //Set the Final Invoice Due
            String invDue = otherHeaderForm.txtFinalDue.getText();
            if (invDue == null || invDue.equals(EMPTY)) {
//                awardHeaderBean.setFinalInvoiceDue(0);
                awardHeaderBean.setFinalInvoiceDue(null);
            } else {
                awardHeaderBean.setFinalInvoiceDue(new Integer(invDue));
            }

            //Set the Invoice Instructions in the Award Comments for comment code 1
            String invoiceInstr = otherHeaderForm.txtArInstructions.getText().trim();
            if (invoiceInstr != null && invoiceInstr.length() > 0) {
                if (awardCommentsBean == null) {
                    awardCommentsBean = new AwardCommentsBean();
                    awardCommentsBean.setMitAwardNumber(awardDetailsBean.getMitAwardNumber());
                    awardCommentsBean.setSequenceNumber(awardDetailsBean.getSequenceNumber());
                    awardCommentsBean.setCommentCode(INVOICE_INSTR_COMMENT_CODE.intValue());
                    awardCommentsBean.setCheckListPrintFlag(true);
                    awardCommentsBean.setAcType(TypeConstants.INSERT_RECORD);
                    awardCommentsBean.setComments(invoiceInstr);
                } else {
                    awardCommentsBean.setComments(invoiceInstr);
                }
            } else if (awardCommentsBean != null) {
                awardCommentsBean.setComments(EMPTY);
            }


            //Update only if Data is Changed
            //Check if Data Changed - START 
            StrictEquals strictEquals = new StrictEquals();

            AwardDetailsBean qryAwardDetailsBean = new AwardDetailsBean();
            AwardHeaderBean qryAwardHeaderBean = new AwardHeaderBean();
            AwardCommentsBean qryAwardsCommentsBean = new AwardCommentsBean();

            CoeusVector cvTemp = queryEngine.getDetails(queryKey, AwardDetailsBean.class);
            if(cvTemp != null && cvTemp.size() > 0) {
                qryAwardDetailsBean = (AwardDetailsBean)cvTemp.get(0);
            }

            cvTemp = queryEngine.getDetails(queryKey, AwardHeaderBean.class);
            if(cvTemp != null && cvTemp.size() > 0) {
                qryAwardHeaderBean = (AwardHeaderBean)cvTemp.get(0);
            }


            Equals eqCommentCode = new Equals("commentCode", INVOICE_INSTR_COMMENT_CODE);
            //Bug Fix : 1068 - START
            //cvTemp = queryEngine.executeQuery(queryKey, 
            //    AwardCommentsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //Above 2 lines of code is commented for the purpose bug fix. it was the prev code.
            cvTemp = queryEngine.executeQuery(queryKey, 
                AwardCommentsBean.class, eqCommentCode);
            //Bug Fix : 1068 - END
            
            if(cvTemp != null && cvTemp.size() > 0) {
                qryAwardsCommentsBean = (AwardCommentsBean)cvTemp.get(0);
            }

            //Update Award Details
            if(!strictEquals.compare(awardDetailsBean, qryAwardDetailsBean)){
                if(getFunctionType() == CORRECT_AWARD) {
                    awardDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, awardDetailsBean);
                }else {
                    awardDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, awardDetailsBean);
                }
                
                //Fire Award Details Modified event
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(awardDetailsBean);
                fireBeanUpdated(beanEvent);
            }

            //Update Award Header
            if(!strictEquals.compare(awardHeaderBean, qryAwardHeaderBean)) {
                if(getFunctionType() == CORRECT_AWARD) {
                    awardHeaderBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, awardHeaderBean);
                }else {
                    awardHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey, awardHeaderBean);
                }
                
                //Fire Award Header Modified event
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(awardHeaderBean);
                fireBeanUpdated(beanEvent);
            }

            //Update Award Comments
            if(awardCommentsBean != null && !strictEquals.compare(awardCommentsBean, 
                qryAwardsCommentsBean)) {
                    if(awardCommentsBean.getAcType() != null && !awardCommentsBean.
                        getAcType().equals(TypeConstants.INSERT_RECORD)) {
                        awardCommentsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        queryEngine.update(queryKey, awardCommentsBean);
                    }else {
                        queryEngine.insert(queryKey, awardCommentsBean);
                    }
                //Added for bug fixed for case #2344 start
                //Fire Award Comments Modified event
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setBean(awardCommentsBean);
                fireBeanUpdated(beanEvent);
                //Added for bug fixed for case #2344 end
            }
        } catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
    }
    
    /** added for the Bug Fix:1666 for alpha numeric sponsor code step:3 start
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
        
        otherHeaderForm.lblSponsorValue.setText(EMPTY);
        
        Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
        //BUG_FIX START
        if(getFunctionType() == DISPLAY_MODE){
            otherHeaderForm.txtArInstructions.setDisabledTextColor(Color.BLACK);
       }
        //BUG_FIX END
        
        //If in display mode disable editable fields else enable.
        boolean enabled = !(getFunctionType() == DISPLAY_MODE);
        //Disable button if in display mode.
        otherHeaderForm.btnSearchSponsor.setEnabled(enabled);
        for(int count = 0; count < components.length; count++) {
            components[count].setEnabled(enabled);
            if(enabled && !(components[count] instanceof JCheckBox)) {
// JM 7-22-2011 removed the following statement to allow field highlighting
//                components[count].setBackground(Color.white);
// END
            }else {
                components[count].setBackground(disabledBackground);
            }
            
            //BUG - FIX:1032 START
            if(!enabled && (components[count] instanceof JTextField)){
                ((JTextField)components[count]).setEditable(false);
                ((JTextField)components[count]).setBackground(disabledBackground);
                ((JTextField)components[count]).setDisabledTextColor(Color.black);
            }
            //BUG - FIX:1032 END
            
        }
    }
    
    public java.awt.Component getControlledUI() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        //return otherHeaderForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(otherHeaderForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
        //Bug Fix:Performance Issue (Out of memory) End 2
    }
    
    public Object getFormData() {
        return otherHeaderForm;
    }
    
    public void registerComponents() {
        
        if (getFunctionType() != DISPLAY_MODE) {
            ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
            otherHeaderForm.setFocusTraversalPolicy(traversePolicy);
            otherHeaderForm.setFocusCycleRoot(true);

            //otherHeaderForm.txtFinalDue.setHorizontalAlignment(alignment
            otherHeaderForm.cmbTemplate.addItemListener(this);
            otherHeaderForm.cmbBasis.addItemListener(this);
            otherHeaderForm.cmbCompetingRenewal.addItemListener(this);
            otherHeaderForm.cmbMethod.addItemListener(this);
            otherHeaderForm.cmbNonCompeting.addItemListener(this);
            otherHeaderForm.cmbPaymentFrequency.addItemListener(this);
            otherHeaderForm.btnSearchSponsor.addActionListener(this);
            otherHeaderForm.txtSponsor.addFocusListener(this);
            
            addBeanUpdatedListener(this, AwardHeaderBean.class);
            addBeanUpdatedListener(this, AwardDetailsBean.class);
        }
        otherHeaderForm.txtSponsor.addMouseListener(this);
        
        
         //Bug Fix:1342 Start 
        otherHeaderForm.cmbTemplate.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    ComboBoxBean emptyBean = new ComboBoxBean(EMPTY ,EMPTY);
                    cvTemplate.add(emptyBean);
                    otherHeaderForm.cmbTemplate.setModel(new DefaultComboBoxModel(cvTemplate));
                    otherHeaderForm.cmbTemplate.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        
        otherHeaderForm.cmbNonCompeting.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    otherHeaderForm.cmbNonCompeting.setModel(new DefaultComboBoxModel(cvProposalDue));
                    //Bug Fix:1399&1455 Start 7
                    otherHeaderForm.cmbNonCompeting.setShowCode(true);
                    //Bug Fix:1399&1455 End 7 
                    otherHeaderForm.cmbNonCompeting.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        
        otherHeaderForm.cmbCompetingRenewal.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    otherHeaderForm.cmbCompetingRenewal.setModel(new DefaultComboBoxModel(cvProposalDue));
                    //Bug Fix:1399&1455 Start 8
                    otherHeaderForm.cmbCompetingRenewal.setShowCode(true);
                    //Bug Fix:1399&1455 End 8
                    otherHeaderForm.cmbCompetingRenewal.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        
        otherHeaderForm.cmbPaymentFrequency.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent kEvent){
                if( kEvent.getKeyCode() == KeyEvent.VK_DELETE &&
                kEvent.getSource() instanceof CoeusComboBox ){
                    otherHeaderForm.cmbPaymentFrequency.setModel(new DefaultComboBoxModel(cvInvFrequency));
                    //Bug Fix:1399&1455 Start 3
                    otherHeaderForm.cmbPaymentFrequency.setShowCode(true);
                    //Bug Fix:1399&1455 End 3
                    otherHeaderForm.cmbPaymentFrequency.requestFocusInWindow();
                    kEvent.consume();
                }
            }
        });
        //Bug Fix:1342 End
    }
    
    public void unRegisterComponents() {
        
        otherHeaderForm.cmbTemplate.removeItemListener(this);
        otherHeaderForm.cmbBasis.removeItemListener(this);
        otherHeaderForm.cmbCompetingRenewal.removeItemListener(this);
        otherHeaderForm.cmbMethod.removeItemListener(this);
        otherHeaderForm.cmbNonCompeting.removeItemListener(this);
        otherHeaderForm.cmbPaymentFrequency.removeItemListener(this);
        otherHeaderForm.btnSearchSponsor.removeActionListener(this);
        otherHeaderForm.txtSponsor.removeFocusListener(this);
        otherHeaderForm.txtSponsor.removeMouseListener(this);
        
        
    }
    
    private void resetDropdowns() {
        
             otherHeaderForm.cmbBasis.setModel(new DefaultComboBoxModel(cvBasis));
             otherHeaderForm.cmbMethod.removeAllItems();
             otherHeaderForm.cmbPaymentFrequency.setModel(new DefaultComboBoxModel(cvInvFrequency));
             otherHeaderForm.cmbCompetingRenewal.setModel(new DefaultComboBoxModel(cvProposalDue));
             otherHeaderForm.cmbNonCompeting.setModel(new DefaultComboBoxModel(cvProposalDue));

             //Bug Fix:1399&1455 Start 4
             otherHeaderForm.cmbBasis.setShowCode(true);
             otherHeaderForm.cmbPaymentFrequency.setShowCode(true);
             otherHeaderForm.cmbCompetingRenewal.setShowCode(true);
             otherHeaderForm.cmbNonCompeting.setShowCode(true);
             //Bug Fix:1399&1455 End 4
    }
    
    private void initComponents() {
        
        components = new Component[] { otherHeaderForm.cmbTemplate, otherHeaderForm.txtSponsor, 
                        otherHeaderForm.cmbNonCompeting, 
                        otherHeaderForm.cmbCompetingRenewal, otherHeaderForm.cmbBasis, 
                        otherHeaderForm.cmbMethod, otherHeaderForm.cmbPaymentFrequency, 
                        otherHeaderForm.txtInvoiceCopies, otherHeaderForm.txtFinalDue,
                        otherHeaderForm.txtArInstructions};
        
        //setting documents
        otherHeaderForm.txtFinalDue.setDocument(new JTextFieldFilter(
            JTextFieldFilter.NUMERIC, 3));

        otherHeaderForm.txtInvoiceCopies.setDocument(new JTextFieldFilter(
            JTextFieldFilter.NUMERIC, 1));
        otherHeaderForm.txtSponsor.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 6));
        
    }
   
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException{
       String primeSponsorCode = otherHeaderForm.txtSponsor.getText().trim();
       /*added for the sponsor code validation when the focus is within the sponsor text field internal bug fix start*/
        if(primeSponsorCode != null){
            try {
//                otherHeaderForm.lblSponsorValue.setText(getSponsorName(primeSponsorCode));
                return validatePrimeSponsorCode();
            }catch (CoeusException ex) {
                throw new edu.mit.coeus.exception.CoeusUIException(ex.getMessage());
            }
        }
       /*internal bug fix  end*/
//        String sponsorName = otherHeaderForm.lblSponsorValue.getText().trim();
//        if((primeSponsorCode != null && !primeSponsorCode.equals(EMPTY)) && 
//            ((sponsorName == null || sponsorName.equals(EMPTY)))) {
//            //Wrong Sponsor Code. show Error Message.
//            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
//            otherHeaderForm.txtSponsor.requestFocus();
//            return false;
//        }
        
        if(otherHeaderForm.cmbBasis.getSelectedIndex() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_BASIS));
            otherHeaderForm.cmbBasis.requestFocus();
            return false;
        }
        if(otherHeaderForm.cmbMethod.getSelectedIndex() == 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_METHOD));
            otherHeaderForm.cmbMethod.requestFocus();
            return false;
        }
        
        ValidBasisMethodPaymentBean methodPaymentBean = 
            (ValidBasisMethodPaymentBean)otherHeaderForm.cmbMethod.getSelectedItem();
        String frequencyIndicator = methodPaymentBean.getFrequencyIndicator();
        String invInstructionsIndicator = methodPaymentBean.getInvInstructionsIndicator();
        
        if (frequencyIndicator != null && frequencyIndicator.equals(FREQUENCY_MANDATORY) &&
            otherHeaderForm.cmbPaymentFrequency.getSelectedIndex() <= 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    SELECT_PAYMENT_FREQUENCY));
                otherHeaderForm.cmbPaymentFrequency.requestFocus();
                return false;
        }
        
        String invoiceInstr = otherHeaderForm.txtArInstructions.getText();
        if (invInstructionsIndicator != null && invInstructionsIndicator.equals(
            INVEST_INSTRUCTIONS_MANDATORY) && (invoiceInstr == null || 
            invoiceInstr.trim().length() == 0)) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_INVOICE_INSTR));
                otherHeaderForm.txtArInstructions.requestFocus();
                return false;
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
            if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
                if (source==otherHeaderForm.cmbTemplate) {
                    if(!templateChanged){
                        oldTemplateName = itemEvent.getItem();
                        return;
                    }
                }
            }// End Chandra 03 -Sept 2004-Step1
            
            if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
                if(source.equals(otherHeaderForm.cmbBasis)){
                    otherHeaderForm.cmbMethod.removeItemListener(this);
                    otherHeaderForm.cmbMethod.removeAllItems();
                    otherHeaderForm.cmbMethod.addItemListener(this);
                    
                    otherHeaderForm.cmbPaymentFrequency.setEnabled(true);
                    otherHeaderForm.txtArInstructions.setEditable(true);
                    
                    code = ((ComboBoxBean)otherHeaderForm.cmbBasis.getSelectedItem()).getCode();
                    eqBasis = new Equals("basisOfPaymentCode",new Integer(code));
                    cvMethod = queryEngine.executeQuery(queryKey, ValidBasisMethodPaymentBean.class, eqBasis);
                    if(cvMethod!= null && cvMethod.size() > 0){
                        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
                        otherHeaderForm.cmbMethod.addItem(emptyBean);
                        for ( int index = 0; index < cvMethod.size(); index++){
                            otherHeaderForm.cmbMethod.addItem(
                            (ComboBoxBean)cvMethod.elementAt(index));
                        }
                    }
                    
                } else if (source.equals(otherHeaderForm.cmbTemplate)){
                    // Bug fix 1107 Added by chandra - Step2 - 3-Sept-2004
                    if (templateChanged) {
                        templateChanged = false;
                        return;
                    }
                    otherHeaderForm.cmbPaymentFrequency.setEnabled(true);
                    otherHeaderForm.txtArInstructions.setEditable(true);
                    String mesg = SELECT_OTHER_TEMPLATE;
                        int selectedOption = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(mesg),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){//// Bug fix 1107 Added by chandra - Step2 - 3-Sept-2004
                        templateChanged = true;
                        refresh();
                        templateChanged = false;
                        ComboBoxBean comboBoxBean = (ComboBoxBean)otherHeaderForm.cmbTemplate.getSelectedItem();
                        String templateCode = comboBoxBean.getCode();
                        templateBean = getAwardTemplateData(templateCode);
                        CoeusVector cvSelectedCode;

                        //If in New Mode then Sync all data for Comments, Terms, Contacts & Reports
                        if (this.functionType == NEW_AWARD) {
                            syncData(new Integer(templateCode).intValue());
                        }

                        //Reset the dropdowns
                        resetDropdowns();
                        if (templateBean != null) {
                            //Set all the data from template
                            //Set Non-Competing 
                            String nonCompeting = new Integer(templateBean.getNonCompetingContPrpslDue()).toString();
                            Equals selectedNonCompeting = new Equals(CODE, nonCompeting);
                            cvSelectedCode = cvProposalDue.filter(selectedNonCompeting);
                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                                otherHeaderForm.cmbNonCompeting.setSelectedItem(comboBoxBean);
                            }

                            //Set Competing Renewal
                            String competing = new Integer(templateBean.getCompetingRenewalPrpslDue()).toString();
                            Equals selectedCompeting = new Equals(CODE, competing);
                            cvSelectedCode = cvProposalDue.filter(selectedCompeting);
                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                                otherHeaderForm.cmbCompetingRenewal.setSelectedItem(comboBoxBean);
                            }


                            //Set the Payment Basis
                            String basisCode = new Integer(templateBean.getBasisOfPaymentCode()).toString();
                            Equals selectedBasis = new Equals(CODE, basisCode);
                            cvSelectedCode = cvBasis.filter(selectedBasis);
                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                                otherHeaderForm.cmbBasis.setSelectedItem(comboBoxBean);
                            }

                            /**
                             * Set the Payment Method. For this first get the valid payment methods 
                             * for the selected Payment Basis. Then set the payment method selected.
                             */
                            eqBasis = new Equals("basisOfPaymentCode", new Integer(templateBean.getBasisOfPaymentCode()));
                            cvMethod = queryEngine.executeQuery(queryKey, ValidBasisMethodPaymentBean.class, eqBasis);
                            ComboBoxBean emptyBean = new ComboBoxBean(EMPTY, EMPTY);
                            cvMethod.add(0, emptyBean);
                            otherHeaderForm.cmbMethod.setModel(new DefaultComboBoxModel(cvMethod));
                            //Bug Fix:1399&1455 Start 5
                            otherHeaderForm.cmbMethod.setShowCode(true);
                            //Bug Fix:1399&1455 End 6

                            //Set the selected Payment Method
                            otherHeaderForm.cmbMethod.removeItemListener(this);
                            String methodCode = new Integer(templateBean.getMethodOfPaymentCode()).toString();
                            Equals selectedMethod = new Equals(CODE, methodCode);
                            cvSelectedCode = cvMethod.filter(selectedMethod);
                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                                otherHeaderForm.cmbMethod.setSelectedItem(comboBoxBean);
                                validatePaymentMethod();
                            }
                            otherHeaderForm.cmbMethod.addItemListener(this);

                            //Set the selected Payment/Invoice
                            String invoiceFreq = new Integer(templateBean.getPaymentInvoiceFreqCode()).toString();
                            Equals selectedFreq = new Equals(CODE, invoiceFreq);
                            cvSelectedCode = cvInvFrequency.filter(selectedFreq);
                            if (cvSelectedCode != null && cvSelectedCode.size() > 0) {
                                comboBoxBean = (ComboBoxBean) cvSelectedCode.get(0);
                                otherHeaderForm.cmbPaymentFrequency.setSelectedItem(comboBoxBean);
                            }

                             //Set the Prime Sponsor code and Name
                            /* JM 12-3-2013 don't want to clear prime sponsor when template changes
                             String primeSponsorCode = templateBean.getPrimeSponsorCode();
                             otherHeaderForm.txtSponsor.setText(primeSponsorCode);
                             if (primeSponsorCode != null && !primeSponsorCode.equals(EMPTY)) {
                                 otherHeaderForm.lblSponsorValue.setText(getSponsorName(primeSponsorCode));
                             } else {
                                 otherHeaderForm.lblSponsorValue.setText(EMPTY);
                             }
							*/

                             //Set Invoice No. of Copies
                             int invCopies = templateBean.getInvoiceNumberOfCopies();
                             if (invCopies == 0) {
                                otherHeaderForm.txtInvoiceCopies.setText(EMPTY);
                             } else {
                                otherHeaderForm.txtInvoiceCopies.setText(invCopies + EMPTY);
                             }

                             //Set Final Invoice Due within
                             int invFinalDue = templateBean.getFinalInvoiceDue();
                             if (invFinalDue == 0) {
                                 otherHeaderForm.txtFinalDue.setText(EMPTY);
                             } else {
                                otherHeaderForm.txtFinalDue.setText(invFinalDue + EMPTY);
                             }

                             //Set the Invoice Instructions
                             String invInstructions = templateBean.getInvoiceInstructions();
                             if (invInstructions == null || invInstructions.length() == 0) {
                                otherHeaderForm.txtArInstructions.setText(EMPTY);
                             } else {
                                 otherHeaderForm.txtArInstructions.setText(invInstructions);
                             }

                        }
                // Bug fix 1107 Added by chandra - Step3 - 3-Sept-2004
                }else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    templateChanged = true;
                    otherHeaderForm.cmbTemplate.setSelectedItem(oldTemplateName);
                    }// Bug fix 1107 Added by chandra - Step3 - 3-Sept-2004
                } else if (source.equals(otherHeaderForm.cmbMethod) &&
                    otherHeaderForm.cmbMethod.getSelectedIndex() > 0){
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
            (ValidBasisMethodPaymentBean)otherHeaderForm.cmbMethod.getSelectedItem();
        String frequencyIndicator = methodPaymentBean.getFrequencyIndicator();
        String invInstructionsIndicator = methodPaymentBean.getInvInstructionsIndicator();

        if (getFunctionType() != TypeConstants.DISPLAY_MODE) {
            if (frequencyIndicator.equals(FREQUENCY_INDICATOR)) {
                otherHeaderForm.cmbPaymentFrequency.setModel(new 
                    DefaultComboBoxModel(cvInvFrequency));
                //Bug Fix:1399&1455 Start 6
                otherHeaderForm.cmbPaymentFrequency.setShowCode(true);
                //Bug Fix:1399&1455 End 6
                otherHeaderForm.cmbPaymentFrequency.setEnabled(false);
            } else {
                otherHeaderForm.cmbPaymentFrequency.setEnabled(true);
            }
            if (invInstructionsIndicator.equals(INVEST_INSTRUCTIONS_INDICATOR)) {
                otherHeaderForm.txtArInstructions.setText(EMPTY);
                otherHeaderForm.txtArInstructions.setEditable(false);
            } else {
                otherHeaderForm.txtArInstructions.setEditable(true);
            }
        }
    }
    
    public void focusLost(FocusEvent focusEvent) {
        
        if(focusEvent.isTemporary()) return ;
        
        Object source = focusEvent.getSource();
        
        if(source.equals(otherHeaderForm.txtSponsor)) {
            String sponsorName;
            try{
//                sponsorName= getSponsorName(otherHeaderForm.txtSponsor.getText());
//                if(sponsorName == EMPTY) {
//                    sponsorName = EMPTY;
//                }
                validatePrimeSponsorCode();
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
                sponsorName = EMPTY;
            }
//            otherHeaderForm.lblSponsorValue.setText(sponsorName);
        }//End Sponsor Focus Lost
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        int clickCount = mouseEvent.getClickCount();
        if(clickCount != 2) return ;
        //Double Clicked on Sponsor code validate and display sponsor details.
        String sponsorCode, sponsorName = null;
        sponsorCode = otherHeaderForm.txtSponsor.getText().trim();
        if(!sponsorCode.equals(EMPTY)) {
            try{
//                sponsorName = getSponsorName(sponsorCode).trim();
                validatePrimeSponsorCode();
                sponsorName = otherHeaderForm.lblSponsorValue.getText();
            }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
            }
        }
        if(sponsorCode.equals(EMPTY)) {
            //Sponsor Code not Entered. Do nothing
            return ;
        }else if(sponsorName == null || sponsorName.equals(EMPTY)) {
            //Wrong Sponsor Code. show Error Message.
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
            return ;
        }
        //valid sponsor code. display sponsor details.
        SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm(DISPLAY_MODE, sponsorCode);
        frmSponsor.showForm(mdiForm, DISPLAY_SPONSOR, true);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(otherHeaderForm.btnSearchSponsor)) {
            displaySponsorSearch();
        }
    }
    
    /** Returns the Award header details for the selected template
     * @return TemplateBean
     */
    private TemplateBean getAwardTemplateData(String templateCode) {
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_TEMPLATE_DETAILS);
        requester.setDataObject(new Integer(templateCode));
        try{
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                templateBean = (TemplateBean) response.getDataObject();
            }else{
                throw new CoeusClientException(response.getMessage());
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }
        return templateBean;
    }
    
    public  void displaySponsorSearch() {
        try{
            int click = sponsorSearch();
            if(click == CANCEL_CLICKED) {
                return ;
            }
            otherHeaderForm.txtSponsor.setText(getSponsorCode());
            otherHeaderForm.lblSponsorValue.setText(getSponsorName());
            
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
            unRegisterComponents();
            setFormData(awardBaseBean);
            registerComponents();
            setRefreshRequired(false);
        }
    }
    
    private void syncData(int templateCode) {
        syncComments(OTHER_HEADER, templateCode);
        syncTerms(OTHER_HEADER, templateCode);
        syncContacts(OTHER_HEADER, templateCode);
        syncReports(OTHER_HEADER, templateCode);
        
        //Fire Award Header Modified event to notify that Award
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setSource(this);
       // beanEvent.setBean(awardDetailsBean);
        beanEvent.setBean(awardHeaderBean);
        fireBeanUpdated(beanEvent);
    }
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
    
    public void beanUpdated(BeanEvent beanEvent) {
        Object source = beanEvent.getSource();
        
        if (beanEvent.getSource().equals(this)) {
            return;
        }
        
        if (beanEvent.getBean().getClass().equals(AwardHeaderBean.class) ||
            beanEvent.getBean().getClass().equals(AwardDetailsBean.class)) {
            setRefreshRequired(true);
            if(beanEvent.getSource().getClass().equals(FundingProposalsController.class)) {
               refresh(); 
            }
        }
    }
    
    /** Method to clean all objects */
    public void cleanUp() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 3
        removeBeanUpdatedListener(this, AwardDetailsBean.class);
        removeBeanUpdatedListener(this, AwardHeaderBean.class);
        //System.out.println("Other Header ");
        if(jscrPn!=null){
            jscrPn.remove(otherHeaderForm);
            jscrPn = null;
            unRegisterComponents();
        }
        //Bug Fix:Performance Issue (Out of memory) End 3
        
        mdiForm = null;
        awardBaseBean = null;
        otherHeaderForm = null;
        awardHeaderBean = null;
        awardDetailsBean = null;
        awardCommentsBean = null;
        templateBean = null;
        queryEngine = null;
        coeusMessageResources = null;
        cvHeaderBean = null;
        cvAwardDetails = null;
        cvTemplate = null;
        cvBasis = null;
        cvMethod = null;
        cvInvFrequency = null;
        cvProposalDue = null;
    }
    
    // Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - Start  
    /**
     * Method used to validate the sponsor code
     * @throws edu.mit.coeus.exception.CoeusException 
     */    
    private boolean validatePrimeSponsorCode()throws CoeusException{
        String sponsorCode = otherHeaderForm.txtSponsor.getText().trim();
        String sponsorName = CoeusGuiConstants.EMPTY_STRING;
        if(!sponsorChanged && !sponsorCode.equals(awardHeaderBean.getPrimeSponsorCode())){
            sponsorChanged = true;
        }
        if(sponsorChanged && !CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
            sponsorName= getSponsorName(sponsorCode);
        }else if(!CoeusGuiConstants.EMPTY_STRING.equals(sponsorCode)){
            sponsorName= awardHeaderBean.getPrimeSponsorName();
        }
        
        if(!CoeusGuiConstants.EMPTY_STRING.equals(otherHeaderForm.txtSponsor.getText().trim()) && CoeusGuiConstants.EMPTY_STRING.equals(sponsorName)){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(INVALID_SPONSOR_CODE));
            sponsorName = CoeusGuiConstants.EMPTY_STRING;
            otherHeaderForm.txtSponsor.setText(CoeusGuiConstants.EMPTY_STRING);
            otherHeaderForm.lblSponsorValue.setText(sponsorName);
            otherHeaderForm.txtSponsor.requestFocusInWindow();
            return false;
        }else{
            otherHeaderForm.lblSponsorValue.setText(sponsorName);
            awardHeaderBean.setPrimeSponsorCode(sponsorCode);
        }
        return true;
    }
    // Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - End  
    
}