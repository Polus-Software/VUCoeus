/**
 * @(#)ProposalSpecialReviewForm.java  1.0  April 29, 2003, 12:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 

/* PMD check performed, and commented unused imports and variables on 13-JULY-2011
 * by Bharati 
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.propdev.gui.SpecialReviewForm;

import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;

/** The class is used to display the <code>Proposal Special Review</code> screen
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on May 29, 2003, 12:17 PM
 */
public class ProposalSpecialReviewForm implements TypeConstants,ActionListener {
    
    private char functionType;
    private String proposalNumber;
    private Vector vecSpecialReviewData ;
    private static boolean isSaved = false;
    private Vector vecDelSpecialReviewData;
    //private Vector vecTempSpecialReviewData = null;
    private Vector vecReplicateData;
    //For the Coeus Enhancement case:#1823 ,for making the special review as a tab page
//    private javax.swing.JButton btnOk;
//    private javax.swing.JButton btnCancel;
    private CoeusDlgWindow dlgParentComponent;
    private SpecialReviewForm specialReviewForm;
    //Vector to hold approval type Codes - Lookup values
    private Vector vecApprovalTypeCode;
    //Vector to hold special review Codes - Lookup values
    private Vector vecSpecialReviewCode;;
    //Vector to hold Special Review Validate Beans used for validations - Lookup values
    private Vector vecValidateRules; 

    //holds whether save is required
    private boolean saveRequired;
    private CoeusMessageResources coeusMessageResources;
    
    //Added COEUSQA-2984 : Statuses in special review - start
    private static final char GET_REVIEW = 'Y';
    private static final int PROPOSAL_SUBMITTED_STATUS = 5;
    private static final int SPECIAL_REVIEW_FOR_HUMAN_SUBJECTS = 1;
    private static final String ENABLE_PROTOCOL_TO_DEV_PROPOSAL ="1";
    private static final String EMPTY_STRING = "";
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";
    private int creationStatusCode;
    private static final int PROP_APPROVAL_TYPE_CODE_LINKED_TO_IRB = 5;
    //Added COEUSQA-2984 : Statuses in special review - end
    
    //For the Coeus Enhancement case:#1799 start step:1
    private int enableProtocolToDevProposalLink;
    private int linkedToIRBCode;
    private int specialReviewTypeCode;
    private CoeusVector cvParameters;
    private Vector vecData;
    private Vector vecDeletedSpecialreviewCodes;
    //End Coeus Enhancement case:#1799 step:1
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private int enableIacucToDevProposalLink;
    private int linkedToIACUCCode;
    private int specialRevTypeCodeParamForIacuc;
    private static final int SPECIAL_REVIEW_FOR_ANIMAL_USAGE = 2;
    private static final String ENABLE_IACUC_TO_DEV_PROPOSAL ="1";
    private static final String HUMAN_SUBJECTS = "SPL_REV_TYPE_CODE_HUMAN";
    private static final String ANIMAL_USAGE = "IACUC_SPL_REV_TYPE_CODE";
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    //public javax.swing.JButton btnStartProtocol;

    //private CoeusAppletMDIForm  mdiForm;
    
    /** 
     * Creates a new instance of ProposalSpecialReviewForm 
     */
    public ProposalSpecialReviewForm() {
    }
    
    /** Creates a new instance of ProposalSpecialReviewForm
     *
     * @param proposalNumber Proposal Number
     * @param vecReviewData Vector of Special Review data
     * @param functionType indicates whether INSERT/MODIFY/DISPLAY 
     */
    public ProposalSpecialReviewForm(String proposalNumber, Vector vecReviewData, char functionType) {
        this.vecSpecialReviewData = vecReviewData;
        this.vecDelSpecialReviewData = new Vector();
        this.functionType = functionType;
        this.proposalNumber = proposalNumber;
        
        //since data persist after the close option
        if(functionType==TypeConstants.ADD_MODE){SpecialReviewForm.removeInstance();}

      //  specialReviewForm = new SpecialReviewForm(functionType, vecSpecialReviewData, "PROPOSAL",btnStartProtocol);
     specialReviewForm= SpecialReviewForm.getInstance(functionType, vecSpecialReviewData,  "PROPOSAL");
        /*try
        {
            vecReplicateData = (Vector)ObjectCloner.deepCopy(vecSpecialReviewData);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }*/    
    }

    /** This method is used to set Approval Type Codes
     *
     * @param approvalTypes  vector od Approval Types
     */            
    public void setApprovalTypes(Vector approvalTypes){    
        this.vecApprovalTypeCode = approvalTypes;
        specialReviewForm.setApprovalTypes(this.vecApprovalTypeCode);
    }
    
    /** This method is used to set Special Review Type Codes
     *
     * @param specialReviewTypeCodes vector of Special Review Type Codes
     */        
    public void setSpecialReviewTypeCodes(Vector specialReviewTypeCodes){    
        this.vecSpecialReviewCode = specialReviewTypeCodes;
        specialReviewForm.setSpecialReviewTypeCodes(this.vecSpecialReviewCode);
    }
    
    /** This method is used to set vecValidateRules
     *
     * @param validateVector is the Vector containing Validation rules.
     */    
    public void setValidateVector(Vector validateVector){    
        this.vecValidateRules = validateVector;
        specialReviewForm.setValidateVector(this.vecValidateRules);
    }
    
    /** This method is used to display the Proposal Special Form
     *  modifed from void return tye to component for the coeus enhancement case:#1823
     */
    // modifed from void return tye to component for the coeus enhancement case:#1823 
    public JComponent showProposalSpecialForm(){    
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.GridBagConstraints gridBagConstraints; 
        
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
        //Getting the data for parameters 
        //Added for Coeus Enhancement Case #1799 - start:  step 2
        //CoeusVector cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        for (int index=0;index<cvParameters.size();index++) {
            CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
            if(CoeusConstants.ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK.equals(coeusParameterBean.getParameterName())){
                enableProtocolToDevProposalLink = Integer.parseInt(coeusParameterBean.getParameterValue());
            }else if(CoeusConstants.LINKED_TO_IRB_CODE.equals(coeusParameterBean.getParameterName())){
                linkedToIRBCode = Integer.parseInt(coeusParameterBean.getParameterValue());
            }else if(CoeusConstants.ENABLE_IACUC_TO_DEV_PROPOSAL_LINK.equals(coeusParameterBean.getParameterName())){
                enableIacucToDevProposalLink = Integer.parseInt(coeusParameterBean.getParameterValue());
            }else if(CoeusConstants.LINKED_TO_IACUC_CODE.equals(coeusParameterBean.getParameterName())){
                linkedToIACUCCode = Integer.parseInt(coeusParameterBean.getParameterValue());
            }
        }
        specialReviewForm.setEnableProtocolLink(enableProtocolToDevProposalLink);
        specialReviewForm.setEnableIacucProtocolLink(enableIacucToDevProposalLink);
        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
        
        //specialReviewForm.btnStartProtocol.addActionListener(this);
        setSpecialReviewCode();




        //End Coeus Enhancement Case #1799 step 2
        
        //Commented for the Coeus Enhancement case:#1823 ,for making the special review as a tab page
//        btnOk = new javax.swing.JButton();
//        btnCancel = new javax.swing.JButton();
//        
//        btnOk.setMnemonic('O');
//        btnOk.setText("OK");
//        btnOk.setFont(CoeusFontFactory.getLabelFont());
//        btnOk.setMaximumSize(new java.awt.Dimension(106, 26));
//        btnOk.setMinimumSize(new java.awt.Dimension(106, 26));
//        btnOk.setPreferredSize(new java.awt.Dimension(85, 26));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
//        
//        //Disable OK if DISPLAY_MODE
//        boolean enabled = functionType != DISPLAY_MODE ? true : false;
//        btnOk.setEnabled(enabled);                        
//        
//        btnCancel.setMnemonic('C');
//        btnCancel.setText("Cancel");
//        btnCancel.setFont(CoeusFontFactory.getLabelFont());
//        btnCancel.setMaximumSize(new java.awt.Dimension(106, 26));
//        btnCancel.setMinimumSize(new java.awt.Dimension(106, 26));
//        btnCancel.setPreferredSize(new java.awt.Dimension(85, 26));        
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 1;
//        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
//        btnOk.addActionListener( new ActionListener(){
//            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
//                try{
//                    if(isSaveRequired()){
//                        if(specialReviewForm.validateData()){
//                            //Get Deleted/Non deleted records from SpecialReviewForm
//                            vecSpecialReviewData = specialReviewForm.getSpecialReviewData();
//                            //Merge the Deleted and Non deleted records
//                            setFormData();
//                            vecSpecialReviewData = getFormData();
//                            dlgParentComponent.dispose();
//                        }
//                    }
//                    else
//                    {
//                        dlgParentComponent.dispose();
//                    }
//                }catch(Exception e){
//                    //e.printStackTrace();
//                    CoeusOptionPane.showErrorDialog(e.getMessage());
//                }                
//            }
//        });
//        btnCancel.addActionListener( new ActionListener(){
//            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
//                try{
//                    performWindowClosing();
//                }catch(Exception e){
//                    CoeusOptionPane.showErrorDialog(e.getMessage());
//                }
//            }
//        });
        
//        String strSponsor = "";
//        if (ProposalDetailAdminForm.SPONSOR_CODE != null)
//        {
//            strSponsor = ProposalDetailAdminForm.SPONSOR_CODE +" : " +ProposalDetailAdminForm.SPONSOR_DESCRIPTION;
//        }
        
//        specialReviewForm.setProposalDescription(proposalNumber,strSponsor);     
        
        /*Commented the Coeus Enhancement case:#1823 ,for making the special review asa tab page*/
//        specialReviewForm.setButtonsReference(btnOk,btnCancel);
        
        JComponent cmpMain = (JComponent)specialReviewForm.showSpecialReviewForm(CoeusGuiConstants.getMDIForm());
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Special Review", true);
        dlgParentComponent.getContentPane().add(cmpMain);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);    
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();        
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));        
        
        specialReviewForm.requestDefaultFocusForComponent();
        
        //Commented the Coeus Enhancement case:#1823 ,for making the special review asa tab page
        
//        //Added By sharath - Bug Fix hit X Btn. Save Cnfrm  Clicked yes. Show Err Msg. close - START
//        //Fix : After Displaying Err Msg Don't Close the Dialog. Keep it in Focus.
//        dlgParentComponent.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
//        //Added By sharath - Bug Fix hit X Btn. Save Cnfrm  Clicked yes. Show Err Msg. close - END
//        
//        dlgParentComponent.addEscapeKeyListener(new AbstractAction("escPressed"){
//            public void actionPerformed(ActionEvent ae){
//                try{
//                    performWindowClosing();
//
//                }catch(Exception e){
//                    CoeusOptionPane.showErrorDialog(e.getMessage());
//                }
//            }
//        });
//        dlgParentComponent.addWindowListener(new WindowAdapter(){
//            
//            public void windowOpened(WindowEvent we){
//                btnCancel.requestFocusInWindow();
//                btnCancel.setFocusable(true);
//            }
//            
//            public void windowClosing(WindowEvent we){
//                try{
//                    performWindowClosing();
//                    
//                }catch(Exception e){
//                    CoeusOptionPane.showErrorDialog(e.getMessage());
//                }
//                 //return;
//            }
//        });
        //dlgParentComponent.show();
        return cmpMain;
    }
    
    
    //Added for Coeus Enhancement Case #1799 - start:  step 4
    public void setSpecialReviewCode() {
        try {
            //CoeusVector cvParameter = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            boolean isHumanSubFetched = false;
            boolean isAnimalUsagefetched = false;
            for (int index=0;index<cvParameters.size();index++) {
                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
                //get the value for the SPL_REV_TYPE_CODE_HUMAN parameter
                if(HUMAN_SUBJECTS.equals(coeusParameterBean.getParameterName())){
                    if(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN.equals(coeusParameterBean.getParameterName())){
                        specialReviewTypeCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                        specialReviewForm.setSpecialRevTypeCode(coeusParameterBean.getParameterValue());
                        isHumanSubFetched = true;
                    }
                }
                //Added for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //getting the data for parameter IACUC_SPL_REV_TYPE_CODE
                else if(ANIMAL_USAGE.equals(coeusParameterBean.getParameterName())) {
                    if(CoeusConstants.IACUC_SPL_REV_TYPE_CODE.equals(coeusParameterBean.getParameterName())){
                        specialRevTypeCodeParamForIacuc = Integer.parseInt(coeusParameterBean.getParameterValue());
                        specialReviewForm.setSpecialRevTypeCodeForIacuc(coeusParameterBean.getParameterValue());
                        isAnimalUsagefetched = true;
                    }
                }
                //if value for both SPL_REV_TYPE_CODE_HUMAN and IACUC_SPL_REV_TYPE_CODE are fetched then break the loop
                if(isHumanSubFetched && isAnimalUsagefetched ){
                    break;
                }
                //Added for the COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
        }catch(Exception exception) {
            
        }
    }
    //Coeus Enhancement Case #1799 - end step:4

    
   /**
    * This method is used to merge the Delete vedtors and non Deleted Vectors 
    *
    * @return Vector of Special Review Form beans.
    */
    
    public Vector getFormData(){
        if(vecDelSpecialReviewData != null)
        {            
            int delSize = vecDelSpecialReviewData.size();
            for(int index = 0; index < delSize; index++){
                SpecialReviewFormBean specialReviewBean = (SpecialReviewFormBean)vecDelSpecialReviewData.get(index);
                if(specialReviewBean != null && vecSpecialReviewData !=null){
                    vecSpecialReviewData.insertElementAt(specialReviewBean,index);
                }
            }
        }
        vecDelSpecialReviewData = null;
        return vecSpecialReviewData;
    }
    
    /**
    * This method is used to separate the Delete & non Deleted data into respective Vectors.
    */
    
    public void setFormData(){
        try{
            if(vecSpecialReviewData != null){
                SpecialReviewFormBean specialReviewFormBean = null;
                for(int index = 0; index < vecSpecialReviewData.size(); index++){
                    specialReviewFormBean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(index);
                    
                    // Case# 3110: Special review in prop dev linked to protocols - Start
                    if(enableProtocolToDevProposalLink == 1 && specialReviewFormBean .getSpecialReviewCode() == specialReviewTypeCode){
                        specialReviewFormBean .setSpecialReviewCode(specialReviewTypeCode);
                        // 4154: Problems in IRB Linking - Start
//                        specialReviewFormBean .setApprovalCode(linkedToIRBCode);
//                        specialReviewFormBean .setApplicationDate(null);
//                        specialReviewFormBean .setApprovalDate(null);
                        // 4154: Problems in IRB Linking - End
                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
                            ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                            if(protocolInfoBean!= null){
                                if(protocolInfoBean.getProtocolStatusDesc() != null && !protocolInfoBean.getProtocolStatusDesc().equals("")){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getApplicationDate() != null && !protocolInfoBean.getApplicationDate().equals("")){
                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                                }
                                if(protocolInfoBean.getApprovalDate() != null && !protocolInfoBean.getApprovalDate().equals("")){
                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                                }
                                // 4154: Problems in IRB Linking - Start
                                // Enabled for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - Start
                                if(protocolInfoBean.getProtocolNumber() != null){
                                    specialReviewFormBean .setApprovalCode(linkedToIRBCode);
                                }
                                // Enabled for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - End
                                // 4154: Problems in IRB Linking - End
                            }
                        }
                        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                        //If ENABLE_IACUC_TO_DEV_PROPOSAL_LINK is 1 and special review code is of type Animal Usage then it should display the
                        //iacuc details of that special review.
                    } else if(enableIacucToDevProposalLink == 1 && specialReviewFormBean .getSpecialReviewCode() == specialRevTypeCodeParamForIacuc){
                        specialReviewFormBean .setSpecialReviewCode(specialRevTypeCodeParamForIacuc);
                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
                            edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = 
                                    getIacucProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                            if(protocolInfoBean!= null){
                                if(protocolInfoBean.getProtocolStatusDesc() != null && !CoeusGuiConstants.EMPTY_STRING.equals(protocolInfoBean.getProtocolStatusDesc())){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getApplicationDate() != null && !CoeusGuiConstants.EMPTY_STRING.equals(protocolInfoBean.getApplicationDate())){
                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                                }
                                if(protocolInfoBean.getApprovalDate() != null && !CoeusGuiConstants.EMPTY_STRING.equals(protocolInfoBean.getApprovalDate())){
                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                                }
                                // Enabled for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - Start
                                if(protocolInfoBean.getProtocolNumber() != null){
                                    specialReviewFormBean .setApprovalCode(linkedToIACUCCode);
                                }
                                // Enabled for COEUSQA-3726 : Issue with IRB linking - while adding a special review in proposal dev application is incorrectly updating the approval type code - End
                            }
                        }
                    }
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                    
                    //Added for COEUSQA-2984 : Statuses in special review - start
                    /*
                     * When proposal will be in progress and it is human usage linkage it should display the current status of the protocol(special review)
                     * But once proposal is submitted it should not display the current status of the protocol
                     */
                    else{
                        int statusCode = getCreationStatusCode();
                        
                        ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = null;
                        if(PROPOSAL_SUBMITTED_STATUS != statusCode && SPECIAL_REVIEW_FOR_HUMAN_SUBJECTS == specialReviewFormBean .getSpecialReviewCode()){
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - start
                            //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
                            //if parameter ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK  is enabled then only  special review should display the original status
                            if(specialReviewFormBean.getProtocolSPRevNumber()!=null && enableProtocolToDevProposalLink == 1){
                                ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                                if(protocolInfoBean.getProtocolStatusDesc()!= null){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getProtocolStatusDesc()== null && specialReviewFormBean.getApprovalDescription()!=null){
                                    specialReviewFormBean.setApprovalDescription(specialReviewFormBean.getApprovalDescription());
                                }
                            }
                            //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - end
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - end
                        }
                        //If there is no protocol status code in DB for particular special review and proposal is in submitted status
                        //then it should display the actual status of the protocol(special review)
                        if(PROPOSAL_SUBMITTED_STATUS == statusCode && specialReviewFormBean.getProtocolStatusCode()== 0 &&
                                SPECIAL_REVIEW_FOR_HUMAN_SUBJECTS == specialReviewFormBean .getSpecialReviewCode()){
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - start
                            if(specialReviewFormBean.getProtocolSPRevNumber()!=null){
                                ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                                if(protocolInfoBean.getProtocolStatusDesc()!= null){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getProtocolStatusDesc()== null && specialReviewFormBean.getApprovalDescription()!=null){
                                    specialReviewFormBean.setApprovalDescription(specialReviewFormBean.getApprovalDescription());
                                }
                            }
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - end
                        }
                        
                        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                        //When proposal will be in progress and it is human usage linkage it should display the current status of the protocol(special review)
                        //But once proposal is submitted it should not display the current status of the protocol
                        if(PROPOSAL_SUBMITTED_STATUS != statusCode && SPECIAL_REVIEW_FOR_ANIMAL_USAGE == specialReviewFormBean .getSpecialReviewCode()){
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - start
                            //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
                            //if parameter ENABLE_IACUC_TO_DEV_PROPOSAL_LINK  is enabled then only  special review should display the original status
                            if(specialReviewFormBean.getProtocolSPRevNumber()!=null && enableIacucToDevProposalLink == 1){
                                edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = getIacucProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                                if(protocolInfoBean.getProtocolStatusDesc()!= null){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getProtocolStatusDesc()== null && specialReviewFormBean.getApprovalDescription()!=null){
                                    specialReviewFormBean.setApprovalDescription(specialReviewFormBean.getApprovalDescription());
                                }
                            }
                            //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - end
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - end
                            
                        }

                        //If there is no protocol status code in DB for particular special review and proposal is in submitted status
                        //then it should display the actual status of the protocol(special review)
                        if(PROPOSAL_SUBMITTED_STATUS == statusCode && specialReviewFormBean.getProtocolStatusCode()== 0 &&
                                SPECIAL_REVIEW_FOR_ANIMAL_USAGE == specialReviewFormBean .getSpecialReviewCode()){
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - start
                            if(specialReviewFormBean.getProtocolSPRevNumber()!=null){
                                edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = getIacucProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                                if(protocolInfoBean.getProtocolStatusDesc()!= null){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getProtocolStatusDesc()== null && specialReviewFormBean.getApprovalDescription()!=null){
                                    specialReviewFormBean.setApprovalDescription(specialReviewFormBean.getApprovalDescription());
                                }
                            }
                            //Added for COEUSQA-3367 : Unknown error opening dev proposal - Error with IACUC link - end
                        }
                        //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                    }
                    //Added for COEUSQA-2984 : Statuses in special review - end
                    
//                    // Case# 3110: Special review in prop dev linked to protocols - End

                    if(specialReviewFormBean.getAcType() != null && specialReviewFormBean.getAcType().equals("D")){
                        /*
                         *Fix for Bugid #2435
                         *Create delete vector instance if its null
                         *Geo on 09-29-2006
                         *BEGIN
                         */
                        if(vecDelSpecialReviewData==null) vecDelSpecialReviewData = new Vector();
                        /*
                         *END #2435
                         */
                        vecDelSpecialReviewData.addElement(specialReviewFormBean);
                        vecSpecialReviewData.removeElementAt(index);
                        index--;
                    }
                }
                //Added for case#3183 - Proposal Hierarchy - starts
                //To update the records in the table after syncing.
//                specialReviewForm.setFormData();
                specialReviewForm.setTableEditors();
                if(((DefaultTableModel) specialReviewForm.tblSpecialReview.getModel()).getRowCount() >0){
                    (specialReviewForm.tblSpecialReview.getSelectionModel()).setSelectionInterval(0, 0);
                }
                //Added for case#3183 - Proposal Hierarchy - ends
                // Commented for Case# 3110: Special review in prop dev linked to protocols - Start
//                // Case # 2021
//                for(int index=0;index<vecSpecialReviewData.size();index++){
//                    specialReviewFormBean  = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
//                    //Added/Modified for case#2990 - start
//                    if(specialReviewFormBean.getSpecialReviewCode() == 1){
//                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
//                            ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
//                            if(protocolInfoBean!= null){
//                                if(protocolInfoBean.getProtocolStatusDesc() != null && !protocolInfoBean.getProtocolStatusDesc().equals("")){
//                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
//                                }
//                                if(protocolInfoBean.getApplicationDate() != null && !protocolInfoBean.getApplicationDate().equals("")){
//                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
//                                }
//                                if(protocolInfoBean.getApprovalDate() != null && !protocolInfoBean.getApprovalDate().equals("")){
//                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
//                                }
//                            }
//                        }
//                    }
//                    //Added/Modified for case#2990 - end
//                }// Case # 2021
                // Commented for Case# 3110: Special review in prop dev linked to protocols -  End
            }
        }catch (CoeusException ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }
    
    
/**
 * this function for setFormData in special review vector in Streamline protocol
 */
     public void setFormDataProtocol(){
        try{
            if(vecSpecialReviewData != null){
                SpecialReviewFormBean specialReviewFormBean = null;
                for(int index = 0; index < vecSpecialReviewData.size(); index++){
                    specialReviewFormBean = (SpecialReviewFormBean)vecSpecialReviewData.elementAt(index);

                    // Case# 3110: Special review in prop dev linked to protocols - Start
                    if(enableProtocolToDevProposalLink == 1 && specialReviewFormBean .getSpecialReviewCode() == specialReviewTypeCode){
                        specialReviewFormBean .setSpecialReviewCode(specialReviewTypeCode);
                        // 4154: Problems in IRB Linking - Start
//                        specialReviewFormBean .setApprovalCode(linkedToIRBCode);
//                        specialReviewFormBean .setApplicationDate(null);
//                        specialReviewFormBean .setApprovalDate(null);
                        // 4154: Problems in IRB Linking - End
                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
                            ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
                            if(protocolInfoBean!= null){
                                if(protocolInfoBean.getProtocolStatusDesc() != null && !protocolInfoBean.getProtocolStatusDesc().equals("")){
                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                                }
                                if(protocolInfoBean.getApplicationDate() != null && !protocolInfoBean.getApplicationDate().equals("")){
                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                                }
                                if(protocolInfoBean.getApprovalDate() != null && !protocolInfoBean.getApprovalDate().equals("")){
                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                                }
                                
                                // : Problems in IRB Linking - Start
//                                if(protocolInfoBean.getProtocolStatusDesc()!= null ){
//                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
//                                }else{
//                                     specialReviewFormBean.setApprovalDescription("newTest");
//                                }
                                //Status Changing....
//                                if(protocolInfoBean.getProtocolNumber() != null){
//                                    specialReviewFormBean .setApprovalCode(linkedToIRBCode);
//                                }
                                // : Problems in IRB Linking - End
                            }
                        }
                    }
                    // Case# 3110: Special review in prop dev linked to protocols - End

                    if(specialReviewFormBean.getAcType() != null && specialReviewFormBean.getAcType().equals("D")){
                        /*
                         *Fix for Bugid #2435
                         *Create delete vector instance if its null
                         *Geo on 09-29-2006
                         *BEGIN
                         */
                        if(vecDelSpecialReviewData==null) vecDelSpecialReviewData = new Vector();
                        /*
                         *END #2435
                         */
                        vecDelSpecialReviewData.addElement(specialReviewFormBean);
                        vecSpecialReviewData.removeElementAt(index);
                        index--;
                    }
                }
//                //Added for case#3183 - Proposal Hierarchy - starts
//                //To update the records in the table after syncing.
////                specialReviewForm.setFormData();
//                specialReviewForm.setTableEditors();
//                if(((DefaultTableModel) specialReviewForm.tblSpecialReview.getModel()).getRowCount() >0){
//                    (specialReviewForm.tblSpecialReview.getSelectionModel()).setSelectionInterval(0, 0);
//                }
                //Added for case#3183 - Proposal Hierarchy - ends
                // Commented for Case# 3110: Special review in prop dev linked to protocols - Start
//                // Case # 2021
//                for(int index=0;index<vecSpecialReviewData.size();index++){
//                    specialReviewFormBean  = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
//                    //Added/Modified for case#2990 - start
//                    if(specialReviewFormBean.getSpecialReviewCode() == 1){
//                        if(specialReviewFormBean.getProtocolSPRevNumber()!= null){
//                            ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewFormBean.getProtocolSPRevNumber().trim());
//                            if(protocolInfoBean!= null){
//                                if(protocolInfoBean.getProtocolStatusDesc() != null && !protocolInfoBean.getProtocolStatusDesc().equals("")){
//                                    specialReviewFormBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
//                                }
//                                if(protocolInfoBean.getApplicationDate() != null && !protocolInfoBean.getApplicationDate().equals("")){
//                                    specialReviewFormBean.setApplicationDate(protocolInfoBean.getApplicationDate());
//                                }
//                                if(protocolInfoBean.getApprovalDate() != null && !protocolInfoBean.getApprovalDate().equals("")){
//                                    specialReviewFormBean.setApprovalDate(protocolInfoBean.getApprovalDate());
//                                }
//                            }
//                        }
//                    }
//                    //Added/Modified for case#2990 - end
//                }// Case # 2021
                // Commented for Case# 3110: Special review in prop dev linked to protocols -  End
            }
        }catch (CoeusException ex){
            ex.printStackTrace();
            CoeusOptionPane.showErrorDialog(ex.getMessage());
        }
    }

    
    //For the Coeus Enhancement case:#2021 start to get the protocol details for the protocol number
    private ProtocolInfoBean getProtocolDetails(String protocolNumber)throws CoeusException{
        ProtocolInfoBean infoBean = new ProtocolInfoBean();
        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('f');
            requester.setDataObject(protocolNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                infoBean = (ProtocolInfoBean)response.getDataObject();
            }
        }
        return infoBean;
    }//End Coeus Enhancement case:#2021
   
    /** This method is used to get the saveRequired Flag
     *
     * @return boolean true if changes are made in the form, else false
     */
    public boolean isSaveRequired(){
        try{
            if( functionType != DISPLAY_MODE ) {
               if(specialReviewForm.isSaveRequired()) {
                   saveRequired = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return saveRequired;
    }
    
    /**
     * This method is used to perform the Window closing operation
     */
    /*private void performWindowClosing() throws Exception{
        int option = JOptionPane.NO_OPTION;
        if(functionType != DISPLAY_MODE){
            if(isSaveRequired()){
                option
                    = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                                "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
            }

            if(option == JOptionPane.YES_OPTION){
                
                if(specialReviewForm.validateData()) {
                    //Get Deleted/Non deleted records from SpecialReviewForm
                    vecSpecialReviewData = specialReviewForm.getSpecialReviewData();
                    //Merge the Deleted and Non deleted records
                    vecSpecialReviewData = getFormData();
                    dlgParentComponent.dispose();
                }
            }else if(option == JOptionPane.NO_OPTION){
                saveRequired = false;
                specialReviewForm.setSaveRequired(false);

                vecSpecialReviewData = vecReplicateData;

                dlgParentComponent.dispose();
            }
        }else{
            dlgParentComponent.dispose();
        }
    }*/
    
    //Added for the Coeus Enhancement case:#1823 start 
    public java.util.Vector getProposalSpecialReviewData(){
        if(!isSaveRequired())
            return new Vector();
        Vector vecSpecialReview = new Vector();
        //Get Deleted/Non deleted records from SpecialReviewForm
        vecSpecialReviewData = specialReviewForm.getSpecialReviewData();
        setFormData();
        //Merge the Deleted and Non deleted records
        vecSpecialReviewData = getFormData();
        if(vecSpecialReviewData != null && vecSpecialReviewData.size()>0){
            ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = null;
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
                if(bean.getAcType()!= null){
                    proposalSpecialReviewFormBean = new ProposalSpecialReviewFormBean(bean);
                    proposalSpecialReviewFormBean.setProposalNumber(proposalNumber);
                    vecSpecialReview.add(proposalSpecialReviewFormBean);
                }
            }
        }
       return vecSpecialReview;
    }
    /**
     *  * this function for getProposalSpecialReview in proposaldetailform for using Streamline protocol
     * @return
     */
     public java.util.Vector getProposalSpecialReviewDataProtocol(){
        if(!isSaveRequired())
            return new Vector();
        Vector vecSpecialReview = new Vector();
        //Get Deleted/Non deleted records from SpecialReviewForm
        vecSpecialReviewData = specialReviewForm.getSpecialReviewData();
        setFormDataProtocol();
        //Merge the Deleted and Non deleted records
        vecSpecialReviewData = getFormData();
        if(vecSpecialReviewData != null && vecSpecialReviewData.size()>0){
            ProposalSpecialReviewFormBean proposalSpecialReviewFormBean = null;
            for(int index = 0; index < vecSpecialReviewData.size(); index++){
                SpecialReviewFormBean bean = (SpecialReviewFormBean)vecSpecialReviewData.get(index);
                if(bean.getAcType()!= null){
                    proposalSpecialReviewFormBean = new ProposalSpecialReviewFormBean(bean);
                    proposalSpecialReviewFormBean.setProposalNumber(proposalNumber);
                    vecSpecialReview.add(proposalSpecialReviewFormBean);
                }
            }
        }
       return vecSpecialReview;
    }
    
    public void setDefaultFocusInComponent(){
        specialReviewForm.tblSpecialReview.requestFocusInWindow();
    }
    
     public void setSaveRequired(boolean save){
        this.saveRequired = save;
        specialReviewForm.setSaveRequired(save);
    }
     
     public boolean validateData() throws edu.mit.coeus.exception.CoeusUIException {
         boolean isValid=false;
         try {
             isValid=specialReviewForm.validateData();
         } catch (Exception e) {
             CoeusUIException coeusUIException = new CoeusUIException(e.getMessage(),CoeusUIException.WARNING_MESSAGE);
             coeusUIException.setTabIndex(5);
             throw coeusUIException;
         }
         return isValid;
     }
   //End Coeus Enhancement case:#1823
    
    
    /** Getter for property vecReplicateData.
     * @return Value of property vecReplicateData.
     */
    public java.util.Vector getVecReplicateData() {
        return vecReplicateData;
    }
    
    /** Setter for property vecReplicateData.
     * @param vecReplicateData New value of property vecReplicateData.
     */
    public void setVecReplicateData(java.util.Vector vecReplicateData) {
        this.vecReplicateData = vecReplicateData;
    }
    
    //Added for Coeus Enhancement Case #1799 - start:  step 5
    /**
     * Getter for property cvParameters.
     * @return Value of property cvParameters.
     */
    public edu.mit.coeus.utils.CoeusVector getCvParameters() {
        return cvParameters;
    }
    
    /**
     * Setter for property cvParameters.
     * @param cvParameters New value of property cvParameters.
     */
    public void setCvParameters(edu.mit.coeus.utils.CoeusVector cvParameters) {
        this.cvParameters = cvParameters;
    }
    
    /**
     * Getter for property vecData.
     * @return Value of property vecData.
     */
    public java.util.Vector getVecData() {
        return vecData;
    }
    
    /**
     * Setter for property vecData.
     * @param vecData New value of property vecData.
     */
    public void setVecData(java.util.Vector vecData) {
        this.vecSpecialReviewData = vecData;
        specialReviewForm.setSpecialReviewData(vecSpecialReviewData);
    }
    
    /**
     * Getter for property vecDeletedSpecialreviewCodes.
     * @return Value of property vecDeletedSpecialreviewCodes.
     */
    public java.util.Vector getVecDeletedSpecialreviewCodes() {
        return vecDeletedSpecialreviewCodes;
    }
    
    /**
     * Setter for property vecDeletedSpecialreviewCodes.
     * @param vecDeletedSpecialreviewCodes New value of property vecDeletedSpecialreviewCodes.
     */
    public void setVecDeletedSpecialreviewCodes(java.util.Vector vecDeletedSpecialreviewCodes) {
        this.vecDeletedSpecialreviewCodes = vecDeletedSpecialreviewCodes;
        specialReviewForm.setVecDeletedSpecialreviewCodes(vecDeletedSpecialreviewCodes);
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    //End Coeus Enhancement Case #1799 - start:  step 5
    
    //Added for COEUSQA-2984 : Statuses in special review - start
    /**
     * Getter for property CreationStatusCode.
     * @return Value of property CreationStatusCode.
     */
    public int getCreationStatusCode() {
        return creationStatusCode;
    }
    
    /**
     * Setter for property CreationStatusCode.
     * @returns value of property CreationStatusCode.
     */
    public void setCreationStatusCode(int creationStatusCode) {
        this.creationStatusCode = creationStatusCode;
    }
    
    /**
     * To get the value of ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK  from the parameter table for given parameter.
     * @return String
     */
     public String getParameterCode(){
        final String connectTo = CoeusGuiConstants.CONNECTION_URL+ "/coeusFunctionsServlet";
        final String PARAMETER = "ENABLE_PROTOCOL_TO_DEV_PROPOSAL_LINK"; 
        String value = EMPTY_STRING;
        //        CoeusVector vctAppointments = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean responder = null;
        requester.setDataObject(GET_PARAMETER_VALUE);
        Vector vecParameter = new Vector();
        vecParameter.add(PARAMETER);
        requester.setDataObjects(vecParameter);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            value =(String) responder.getDataObject();
        }
        return value;
    }
    //Added for COEUSQA-2984 : Statuses in special review - end
    
     //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
     /**
      * To get the IACUC protocol details
      * IF special review code is of type Animal Usage then it should display the iacuc details of that special review.
      * @param protocolNumber - for which detials has to be get
      * @returns the infobean of iacuc protocol
      */
     private edu.mit.coeus.iacuc.bean.ProtocolInfoBean getIacucProtocolDetails(String protocolNumber)throws CoeusException{
         edu.mit.coeus.iacuc.bean.ProtocolInfoBean infoBean = new edu.mit.coeus.iacuc.bean.ProtocolInfoBean();
         if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
             String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
             RequesterBean requester = new RequesterBean();
             requester.setFunctionType('f');
             requester.setDataObject(protocolNumber);
             AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
             comm.send();
             ResponderBean response = comm.getResponse();
             if(!response.isSuccessfulResponse()){
                 throw new CoeusException(response.getMessage(), 1);
             }else{
                 infoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)response.getDataObject();
             }
         }
         return infoBean;
     }
     //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
}
