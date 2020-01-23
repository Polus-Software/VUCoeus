/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, commented unused imports and
 * variables on 13-JULY-2011 by Bharati 
 */

/*
 * Award Special Review Controller.java
 *
 * Created on June 7, 2004, 3:20 PM
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.gui.event.BeanUpdatedListener;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.specialreview.*;
import edu.mit.coeus.bean.*;
//import edu.mit.coeus.bean.SRApprovalInfoBean;
import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;

//import javax.swing.JComponent.*;
//import java.util.Vector.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.util.*;
//import javax.swing.*;
//import javax.swing.event.*;
//import javax.swing.table.*;
//import java.text.*;
import java.util.Vector;
import javax.swing.JComponent;


public class AwardSpecialReviewController extends AwardController implements BeanUpdatedListener{
    private AwardSpecialReviewBean awardSpecialReviewBean;
    private AwardDetailsBean awardDetailsBean;
    private char functionType;
    private CoeusVector cvTemp;
    private CoeusVector cvTableData;
    private QueryEngine queryEngine;
    private SpecialReviewForm specialReviewForm;
    private CoeusVector cvSpecialReview;
    private CoeusVector cvApproval;
    private CoeusVector cvValidate;
    private CoeusVector cvData;
//    private Vector vecSpecialReviewData;
    private JComponent cmpMain;
    private boolean formSaveRequired = false;
//    private CoeusVector cvDeletedData;
//    private int rowId;
//    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
//    private static final int WIDTH = 770;
//   private static final int HEIGHT = 480;
//    private static final String WINDOW_TITLE = "Special Review";
    private CoeusDlgWindow dlgSpecialReview;
    private static final String AWARD_MODULE = "Award_Module";
//    private java.awt.GridBagConstraints gridBagConstraints;
//    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
     //Vector to hold approval type Codes - Lookup values
//    private Vector vecApprovalTypeCode;
    //Vector to hold special review Codes - Lookup values
//    private Vector vecSpecialReviewCode;;
    //Vector to hold Special Review Validate Beans used for validations - Lookup values
//    private Vector vecValidateRules;
//    private Vector vecDelSpecialReviewData;
    public boolean saveRequired;
//    private JButton btnOk; 
//    private JButton btnCancel;
    
    //For the Coeus Enhancement case:#1799 start step:1
    private int enableAwardToProtocolLink;
    private int linkedToIRBCode;
    private int specialReviewTypeCode;
    //End Coeus Enhancement case:#1799 step:1
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private int linkedToIACUCCode;
    private int enableAwardToIacucProtocolLink;
    private int specialReviewTypeCodeForIacuc;
    private static final String HUMAN_SUBJECTS = "SPL_REV_TYPE_CODE_HUMAN";
    private static final String ANIMAL_USAGE = "IACUC_SPL_REV_TYPE_CODE";
    private Vector vecAwardSpRvData;
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    

    /** Creates a new instance of AwardSpecialReviewController */
    public AwardSpecialReviewController(AwardBaseBean awardBaseBean,char functionType) {
        super(awardBaseBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
//        vecDelSpecialReviewData = new Vector();
        cvData = new CoeusVector();
        registerComponents();
        setFormData(awardBaseBean);
        //For the Coeus Enhancement case:#1799 start step:2   
//        postInitComponents();
        //End Coeus Enhancement case:#1799 step:2   
        //Case# 2878:Special Reviews do not appear until Award is saved - Start
        registerEvents();
    }
    /*For the Coeus Enhancement case:#1799 start step:3  
    private void postInitComponents(){
        dlgSpecialReview = new CoeusDlgWindow(mdiForm);
        dlgSpecialReview.setResizable(false);
        dlgSpecialReview.setModal(true);
        dlgSpecialReview.getContentPane().add(specialReviewForm);
        dlgSpecialReview.setFont(CoeusFontFactory.getLabelFont());
        dlgSpecialReview.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgSpecialReview.setSize(WIDTH, HEIGHT);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgSpecialReview.getSize();
        dlgSpecialReview.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgSpecialReview.setTitle(WINDOW_TITLE);
         
         dlgSpecialReview.addEscapeKeyListener(
         new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
                return;
            }
        });

        dlgSpecialReview.addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent we){
                    performWindowClosing();
            }
        });

        
        
        
        dlgSpecialReview.addComponentListener(
         new ComponentAdapter(){
             public void componentShown(ComponentEvent e){
                 requestDefaultFocus();
             }
         });

    }//End Coeus Enhancement case:#1799 step:3 */
    
//    private void requestDefaultFocus(){
//        if(functionType!= DISPLAY_MODE){
//            if(specialReviewForm.tblSpecialReview.getRowCount() >0){
//                specialReviewForm.tblSpecialReview.setRowSelectionInterval(0,0);
//                specialReviewForm.tblSpecialReview.setColumnSelectionInterval(0,0);
//                btnCancel.requestFocusInWindow();
//            }else{
//                btnCancel.requestFocusInWindow();
//            }
//        }else{
//            //commented for bug fix : 1011
//            //if(specialReviewForm.tblSpecialReview.getRowCount() > 0 ){
//            btnCancel.requestFocusInWindow();
//            btnOk.setEnabled(false);
//            //}
//        }
//    }
    
    public void display() {
        dlgSpecialReview.setVisible(true);
      
    }

    public void formatFields() {
        specialReviewForm.formatFields();
        
    }

    public java.awt.Component getControlledUI() {
        return cmpMain;
    }

    public Object getFormData() {
        return null;
    }

    public void registerComponents() {
    }

    public void saveFormData() {
        //For the Coeus enhancement case:#1799 start 
        if(!isSaveRequired()){ 
            return;
        }
        //End Coeus enhancement case:#1799
        int rowMax = getExistingMaxId();
        try{
            cvTableData = (CoeusVector)specialReviewForm.getSpecialReviewData();
            CoeusVector cvData = queryEngine.getDetails(queryKey,AwardSpecialReviewBean.class);
            if(cvTableData!= null && cvTableData.size() > 0){
                AwardSpecialReviewBean awardSpecialReviewBean = null;
                for(int index = 0; index < cvTableData.size(); index++){
                    SpecialReviewFormBean bean = (SpecialReviewFormBean)cvTableData.get(index);
                    //For the Coeus Enhancement 1799 start step:4,do not set appldate ,approval date while saving in protocol linking                 
                    if(enableAwardToProtocolLink == 1 && bean.getSpecialReviewCode() == specialReviewTypeCode){
                        bean.setSpecialReviewCode(specialReviewTypeCode);
                        bean.setApprovalCode(linkedToIRBCode);
                        bean.setApplicationDate(null);
                        bean.setApprovalDate(null);
                    }
                    //End Coeus Enhancement step:4
                    
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    //if it is linked to IACUC then set the special review type code 
                    else if(enableAwardToIacucProtocolLink == 1 && bean.getSpecialReviewCode() == specialReviewTypeCodeForIacuc){
                        bean.setSpecialReviewCode(specialReviewTypeCodeForIacuc);
                        bean.setApprovalCode(linkedToIACUCCode);
                        bean.setApplicationDate(null);
                        bean.setApprovalDate(null);
                    }
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                    
                    if(bean.getAcType()!= null){
                        awardSpecialReviewBean = new AwardSpecialReviewBean(bean);
                        awardSpecialReviewBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());

                        if(awardSpecialReviewBean.getAcType().equalsIgnoreCase("null")){
                            awardSpecialReviewBean.setProtocolSPRevNumber(null);
                        }

                        if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                            awardSpecialReviewBean.setAcType(TypeConstants.DELETE_RECORD);
                            queryEngine.delete(queryKey, awardSpecialReviewBean);
                            awardSpecialReviewBean.setAcType(TypeConstants.INSERT_RECORD);
                            rowMax = rowMax + 1;
                            awardSpecialReviewBean.setRowId(rowMax);
                            queryEngine.insert(queryKey, awardSpecialReviewBean);
                        }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                            awardSpecialReviewBean.setAcType(TypeConstants.DELETE_RECORD);
                            queryEngine.delete(queryKey, awardSpecialReviewBean);
                        }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                            //Commented for COEUSDEV-312 : First Protocol on Proposal not being saved on Award record - Start
                            //Check is not need to remove the protocol special review
                            //Duplicate validation for Human Subjects special review are done while saving
                            //Added for the Coeus Enhancement case:#1799  start step:5
                            //Remove the bean which already exists in the queryengine and send the latest bean to the queryengine
//                            boolean isContinue = false;
//                                for(int idx=0;idx<cvData.size();idx++){
//                                    AwardSpecialReviewBean awardBean = (AwardSpecialReviewBean)cvData.get(idx);
//                                    if(bean.getProtocolSPRevNumber() != null  && bean.getProtocolSPRevNumber().equals(awardBean.getProtocolSPRevNumber()) ) {
//                                        // Case# 2878:Special Reviews do not appear until Award is saved
////                                        isContinue=true;
////                                        queryEngine.delete(queryKey, awardBean);
//                                        break;
//                                    }
//                                    else if(awardBean.getProtocolSPRevNumber().equals(bean.getPrevSpRevProtocolNumber())) {
//                                        queryEngine.removeData(queryKey,AwardSpecialReviewBean.class,idx);
//                                    }
//                                    
//                                }
                                // Block Commented for Case# 2878:Special Reviews do not appear until Award is saved
//                                if(isContinue){
//                                    continue;
//                                }
                            //End Coeus Enhancement case:#1799 step:5
                            //COEUSDEV-312 : End
                            if(awardSpecialReviewBean.getRowId() == 0) {
                                rowMax = rowMax + 1;
                                awardSpecialReviewBean.setRowId(rowMax);
                                awardSpecialReviewBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, awardSpecialReviewBean);
                            }//End if rowId == 0
                            else{
                                queryEngine.insert(queryKey, awardSpecialReviewBean);
                            }
                        }
                    }
                }
            }//End if cvTableData

            //Remove beans found in temp and not found in cvTableData
            //in case of INSERT - DELETE
            cvTempLoop : for(int index = 0; index < cvTemp.size() ;index++ ){
                SpecialReviewFormBean specialReviewFormBean = (SpecialReviewFormBean)cvTemp.get(index);
                awardSpecialReviewBean = new AwardSpecialReviewBean(specialReviewFormBean);
                awardSpecialReviewBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                
                AwardSpecialReviewBean awrdSpecialReviewBean;
                SpecialReviewFormBean splReviewFormBean;
                for(int count = 0; count < cvTableData.size(); count++) {
                    if(cvTableData.get(count) instanceof AwardSpecialReviewBean) {
                        awrdSpecialReviewBean = (AwardSpecialReviewBean)cvTableData.get(count);
                    }else {
                        splReviewFormBean = (SpecialReviewFormBean)cvTableData.get(count);
                        awrdSpecialReviewBean = new AwardSpecialReviewBean(splReviewFormBean);
                        awrdSpecialReviewBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                    }
                    
                    if(awrdSpecialReviewBean.equals(awardSpecialReviewBean)) {
                        continue cvTempLoop;
                    }
                    
                    
                    
                }//End for cvTableData
                try{
                       queryEngine.delete(queryKey, awardSpecialReviewBean);
                    }catch (CoeusException coeusException){
                        coeusException.printStackTrace();
                    }
            }//End cvTemp
            
            
            

        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally{
            saveRequired = false;
            specialReviewForm.setSaveRequired(false);
            setFormSaveRequired(false);
            
        }
        //For the Coeus Enhancement case:#1799 start step:6
        //close();
        //Ens Coeus Enhancement case:#1799 step:6
    }

     private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
        int maxRowId = 0;
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, AwardSpecialReviewBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                AwardSpecialReviewBean bean = (AwardSpecialReviewBean)cvExistingRecords.get(0);
                maxRowId = bean.getRowId();
            }else{
                maxRowId = 0;
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        return maxRowId;
    }

//    private void close(){
//        dlgSpecialReview.dispose();
//    }




    public void setFormData(Object awardBaseBean) {
        cvTableData = new CoeusVector();
        //For the Coeus Enhancement case:#1799 start step:7 
//        btnOk = new JButton();
//        btnCancel = new JButton();
//        btnOk.setFont(CoeusFontFactory.getLabelFont());
//        btnOk.setMnemonic('O');
//        btnOk.setText("OK");
//        btnOk.setMinimumSize(new java.awt.Dimension(85,26));
//        btnOk.setPreferredSize(new java.awt.Dimension(85,26));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
//
//
//        btnCancel.setFont(CoeusFontFactory.getLabelFont());
//        btnCancel.setMnemonic('C');
//        btnCancel.setText("Cancel");
//        btnCancel.setMinimumSize(new java.awt.Dimension(85,26));
//        btnCancel.setPreferredSize(new java.awt.Dimension(85,26));
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 0;
//        gridBagConstraints.gridy = 0;
//        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
//        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        //End Coeus Enhancement case:#1799 step:7
        
        try{
            this.awardBaseBean = (AwardBaseBean)awardBaseBean;
            
            cvSpecialReview = new CoeusVector();
            cvApproval = new CoeusVector();
            cvValidate = new CoeusVector();

            cvSpecialReview = queryEngine.getDetails(queryKey, KeyConstants.SPECIAL_REVIEW_TYPE);
            cvApproval = queryEngine.getDetails(queryKey, KeyConstants.SPECIAL_REVIEW_APPROVAL_TYPE);
            cvTableData = queryEngine.executeQuery(queryKey, AwardSpecialReviewBean.class,CoeusVector.FILTER_ACTIVE_BEANS);//, new NotEquals("acType", TypeConstants.DELETE_RECORD));
            //cvTableData.sort("specialReviewDescription",true);

            //set to temp so that we can know which beans are deleted.
            cvTemp = new CoeusVector();
            cvTemp.addAll(cvTableData);

            cvValidate = queryEngine.executeQuery(queryKey,SRApprovalInfoBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            cvData = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            awardDetailsBean = (AwardDetailsBean)cvData.get(0);

//            if(cvTableData!= null && cvTableData.size() > 0){
//                rowId = cvTableData.size();
//            }
            
            //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            //getting the data for parameters
            //Added for Coeus Enhancement Case #1799 - start:  step 8
            CoeusVector cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            for (int index=0;index<cvParameters.size();index++) {
                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
                if(CoeusConstants.ENABLE_PROTOCOL_TO_AWARD_LINK.equals(coeusParameterBean.getParameterName())){
                    enableAwardToProtocolLink = Integer.parseInt(coeusParameterBean.getParameterValue());
                    //specialReviewForm.setEnableProtocolLink(enableProposalToProtocolLink);
                    //System.out.println("coeusParameterBean.getParameterValue()"+specialReviewForm.getEnableProtocolLink());
                    
                }else if(CoeusConstants.LINKED_TO_IRB_CODE.equals(coeusParameterBean.getParameterName())){
                    linkedToIRBCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                    //System.out.println("linkedtoirbcode value"+linkedToIRBCode);
                }else if(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_AWARD_LINK.equals(coeusParameterBean.getParameterName())){
                    enableAwardToIacucProtocolLink = Integer.parseInt(coeusParameterBean.getParameterValue());
                }else if(CoeusConstants.LINKED_TO_IACUC_CODE.equals(coeusParameterBean.getParameterName())){
                    linkedToIACUCCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                    //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
                }else if(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN.equals(coeusParameterBean.getParameterName())){
                    specialReviewTypeCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                }else if(CoeusConstants.IACUC_SPL_REV_TYPE_CODE.equals(coeusParameterBean.getParameterName())){
                    specialReviewTypeCodeForIacuc = Integer.parseInt(coeusParameterBean.getParameterValue());
                }
                //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - end
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
            
            for(int index=0;index<cvTableData.size();index++){
                AwardSpecialReviewBean specialReviewBean = (AwardSpecialReviewBean)cvTableData.get(index);
                //Modified for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
                //If Special review is of type Human Subjects(specialReviewTypeCode) then get the actual status of the protocol
                //If Special review is of type Animal Usage(specialRevTypeCodeParamForIacuc) then get the actual status of the protocol
                //Added for COEUSQA-3569 : Existing Animal Usage Special Review Dates not Visible and New Dates Cannot be Entered - start
                if(specialReviewBean.getApprovalCode() == linkedToIRBCode && specialReviewBean.getSpecialReviewCode() == specialReviewTypeCode
                        && enableAwardToProtocolLink == 1 ){
                    ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewBean.getProtocolSPRevNumber());
                    specialReviewBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                    specialReviewBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                    specialReviewBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                    // 4154: Problems in IRB Linking - Start
                    specialReviewBean.setProtoSequenceNumber(protocolInfoBean.getSequenceNumber());
                    // 4154: Problems in IRB Linking - End
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    //If special review type is linked to Iacuc then get protocol details
                }else if(specialReviewBean.getApprovalCode() == linkedToIACUCCode && specialReviewBean.getSpecialReviewCode() == specialReviewTypeCodeForIacuc
                        && enableAwardToIacucProtocolLink == 1){
                    edu.mit.coeus.iacuc.bean.ProtocolInfoBean iacucProtocolInfoBean = new edu.mit.coeus.iacuc.bean.ProtocolInfoBean();
                    iacucProtocolInfoBean = getIacucProtocolDetails(specialReviewBean.getProtocolSPRevNumber());
                    //If approval code is 2 and its invalid protocol then we have to set the selected approval status for that special review
                    if(iacucProtocolInfoBean.getProtocolStatusDesc()!=null){
                        specialReviewBean.setApprovalDescription(iacucProtocolInfoBean.getProtocolStatusDesc());
                    }else{
                        specialReviewBean.setApprovalDescription(specialReviewBean.getApprovalDescription());
                    }
                    specialReviewBean.setApplicationDate(iacucProtocolInfoBean.getApplicationDate());
                    specialReviewBean.setApprovalDate(iacucProtocolInfoBean.getApprovalDate());
                    specialReviewBean.setProtoSequenceNumber(iacucProtocolInfoBean.getSequenceNumber());
                }
                //Added for COEUSQA-3569 : Existing Animal Usage Special Review Dates not Visible and New Dates Cannot be Entered - end
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                //Modified for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - end
            }
            //End Coeus Enhancement Case #1799 step 8
            if( specialReviewForm == null ) {
                specialReviewForm=new SpecialReviewForm(functionType,cvTableData,AWARD_MODULE);
                 specialReviewForm.setEnableProtocolLink(enableAwardToProtocolLink);
                 //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                 specialReviewForm.setEnableIacucProtocolLink(enableAwardToIacucProtocolLink);
                 //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                 setSpecialReviewCode();
                specialReviewForm.setApprovalTypes((Vector)cvApproval);
                specialReviewForm.setSpecialReviewTypeCodes((Vector)cvSpecialReview);
                
                cmpMain = (JComponent)specialReviewForm.showSpecialReviewForm(CoeusGuiConstants.getMDIForm());
                //For  Coeus Enhancement case:#1799 Start step:9
                //specialReviewForm.setAwardDetailDescription(getAwardDetails());
                //specialReviewForm.setButtonsReference(btnOk, btnCancel);
                //End Coeus enhancement case:#1799 step:9
                specialReviewForm.setValidateVector((Vector)cvValidate);
   
            }else{
                specialReviewForm.setSpecialReviewData(cvTableData);
                 //Added for Coeus Enhancement Case #1799 - start:  step 10
                setSpecialReviewCode();
                //Coeus Enhancement Case #1799 - end step:10
                specialReviewForm.setApprovalTypes((Vector)cvApproval);
                specialReviewForm.setSpecialReviewTypeCodes((Vector)cvSpecialReview);
                specialReviewForm.setValidateVector((Vector)cvValidate);
                //For  Coeus Enhancement case:#1799 Start step:11
                //specialReviewForm.setAwardDetailDescription(getAwardDetails());
                //specialReviewForm.setButtonsReference(btnOk, btnCancel);
                //End Coeus enhancement case:#1799 step:11
                specialReviewForm.setFormData();
                specialReviewForm.formatFields();
                specialReviewForm.setTableEditors();
                //Added for COEUSDEV 271:special review not selected when copying from proposal to award
                specialReviewForm.enableDisableButtons();
                //COEUSDEV 271:End
            }
            specialReviewForm.setSaveRequired(false);
            setFormSaveRequired(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
        

//        btnOk.addActionListener(new ActionListener(){
//            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
//                try{
//                     if(isFormSaveRequired()){
//                    if(validate()){
//                        saveFormData();
//                    }
//                     }
//                    else {
//                        dlgSpecialReview.dispose();
//                    }
//                }catch(Exception e){
//                    CoeusOptionPane.showErrorDialog(e.getMessage());
//
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
  
      
    }
    
    //Added for Coeus Enhancement Case #1799 - start:  step 12
    //to set the special review code from the parameter table while linking 
    public void setSpecialReviewCode() {
        try {
            CoeusVector cvParameter = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            //Modified and Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
            boolean isHumanSubFetched = false;
            boolean isAnimalUsagefetched = false;
            for (int index=0;index<cvParameter.size();index++) {
                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameter.elementAt(index);
                if(HUMAN_SUBJECTS.equals(coeusParameterBean.getParameterName())){
                    if(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN.equals(coeusParameterBean.getParameterName())){
                        specialReviewTypeCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                        specialReviewForm.setSpecialRevTypeCode(coeusParameterBean.getParameterValue());
                        isHumanSubFetched = true;
                    }
                }
                //Getting the data for parameter IACUC_SPL_REV_TYPE_CODE
                //cvParameters contains all the parameters but here requirement is of only two parameters SPL_REV_TYPE_CODE_HUMAN and IACUC_SPL_REV_TYPE_CODE
                //to avoid the looping break has been provided after getting the value for the mentioned  parameters.
                else if(ANIMAL_USAGE.equals(coeusParameterBean.getParameterName())){
                    if(CoeusConstants.IACUC_SPL_REV_TYPE_CODE.equals(coeusParameterBean.getParameterName())){
                        specialReviewTypeCodeForIacuc = Integer.parseInt(coeusParameterBean.getParameterValue());
                        specialReviewForm.setSpecialRevTypeCodeForIacuc(coeusParameterBean.getParameterValue());
                        isAnimalUsagefetched = true;
                    }
                }
                //if value for both SPL_REV_TYPE_CODE_HUMAN and IACUC_SPL_REV_TYPE_CODE are fetched then break the loop
                if(isHumanSubFetched && isAnimalUsagefetched ){
                    break;
                }              
                //Modified and Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }
   
    //to get the protocol details while opening the page if the protocol link is enabled and the special review is human subjects
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
    }
    //Coeus Enhancement Case #1799 - end step:12

//    private void performWindowClosing(){
//        if(isFormSaveRequired()){
//            int option = CoeusOptionPane.showQuestionDialog(
//            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
//            CoeusOptionPane.OPTION_YES_NO_CANCEL,
//             CoeusOptionPane.DEFAULT_YES);
//            switch( option ) {
//                case (JOptionPane.YES_OPTION ):
//                    
//                    try{
//                        
//                        if(validate()){
//                            saveFormData();
//                        }
//                        
//                    }catch (Exception exception){
//                        exception.printStackTrace();
//                    }
//                    break;
//                case(JOptionPane.NO_OPTION ):
//                    close();
//                    break;
//                default:
//                    break;
//            }
//        }else{
//            close();
//        }
//
//    }


    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /** 
     * If special review type is linked to Iacuc then get the iacuc protocol details
     *  @param protocolNumber - for which detials has to be get
     *  @returns the infobean of iacuc protocol
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
    

    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        try {
            boolean isValid=specialReviewForm.validateData();
            return isValid;
        } catch (Exception e) {
            edu.mit.coeus.exception.CoeusUIException coeusUIException = new edu.mit.coeus.exception.CoeusUIException(e.getMessage());
            CoeusOptionPane.showDialog(coeusUIException);

        }
        return false;
    }
    /**
     * To make the class level instances as null
     */
    public void cleanUp () {
        awardSpecialReviewBean = null;
        awardDetailsBean = null;
        cvTemp = null;
        cvTableData = null;
        specialReviewForm = null;
        cvSpecialReview = null;
        cvApproval = null;
        cvValidate = null;
        cvData = null;
//        vecSpecialReviewData = null;
        cmpMain = null;
//        cvDeletedData = null;
        dlgSpecialReview = null;
//        vecApprovalTypeCode = null;
//        vecSpecialReviewCode = null;
//        vecValidateRules = null;
//        vecDelSpecialReviewData = null;
//        btnOk = null;
//        btnCancel = null;
        // Case# 2878:Special Reviews do not appear until Award is saved
        removeBeanUpdatedListener(this, AwardSpecialReviewBean.class);
    }
    
    /*To set the default focus*/
    public void setDefaultFocusInWindow(){
        if(functionType!= DISPLAY_MODE){
            specialReviewForm.btnAdd.requestFocusInWindow();
        }else{
            if(specialReviewForm.tblSpecialReview.getRowCount() > 0){
                specialReviewForm.tblSpecialReview.requestFocusInWindow();
            }
        }
    }

    public void setFormSaveRequired(boolean formSaveRequired) {
        this.formSaveRequired = formSaveRequired;
    }

    public JComponent getAwardDetails(){

        AwardHeaderForm awardHeaderForm = new AwardHeaderForm();
        java.awt.GridBagConstraints gridBagConstraints;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        awardHeaderForm.lblAwardNumberValue.setText(awardDetailsBean.getMitAwardNumber());
        awardHeaderForm.lblSequenceNumberValue.setText(""+awardDetailsBean.getSequenceNumber());
        awardHeaderForm.lblSponsorAwardNumberValue.setText(awardDetailsBean.getSponsorAwardNumber());
        return awardHeaderForm;
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
        if(isRefreshRequired()) {
            int selectedRow = specialReviewForm.tblSpecialReview.getSelectedRow();
            int rowCount = 0;
            setFormData(awardBaseBean);
            setRefreshRequired(false);
            specialReviewForm.formatFields();
            if(selectedRow!= -1) {
                specialReviewForm.tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
            }
            // Case# 2878:Special Reviews do not appear until Award is saved - Start
            rowCount = specialReviewForm.tblSpecialReview.getRowCount();
            if(rowCount < 1){
                specialReviewForm.btnDelete.setEnabled(false);
            }
            else{
                specialReviewForm.btnDelete.setEnabled(true);
            }
            // Case# 2878:Special Reviews do not appear until Award is saved - End
        }
    }

    public boolean isFormSaveRequired() {
        try{
            if( functionType != DISPLAY_MODE ) {
                formSaveRequired =  specialReviewForm.isSaveRequired();
                
                //System.out.println("Special Review Form Save Required :"+specialReviewForm.isSaveRequired());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return formSaveRequired;
    }

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

    // Case# 2878:Special Reviews do not appear until Award is saved - Start
    public void registerEvents() {
        addBeanUpdatedListener(this, AwardSpecialReviewBean.class);
    }
    
    /*  This is the Implementation of beanUpdated() of BeanUpdatedListener 
     *  This method calls refresh() of AwardSpecialReviewController
     *  @param beanEvent
     */
    public void beanUpdated(BeanEvent beanEvent) {
        if(beanEvent.getSource().getClass().equals(FundingProposalsController.class)
            ||beanEvent.getSource().getClass().equals(AwardBaseWindowController.class)) {
            
            if(beanEvent.getBean().getClass().equals(AwardSpecialReviewBean.class)) {
                setRefreshRequired(true);
                refresh();
            }
            
        }
    }
    
    void setQueryKey(String queryKey) {
        this .queryKey = queryKey;
    }
    // Case# 2878:Special Reviews do not appear until Award is saved - End

    //Added for COEUSQA-3119  - Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /**
     * Getter for property vecAwardSpRvData.
     * @return Value of property vecAwardSpRvData.
     */
    public Vector getVecAwardSpRvData() {
        return vecAwardSpRvData;
    }
    
    /**
     * Setter for property vecAwardSpRvData.
     * @return Value of property vecAwardSpRvData.
     */
    public void setVecAwardSpRvData(Vector vecAwardSpRvData) {
        this.vecAwardSpRvData = vecAwardSpRvData;
    }
    //Added for COEUSQA-3119  - Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
 
}
