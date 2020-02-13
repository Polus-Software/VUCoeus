/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * SpecialReviewController.java
 *
 * Created on May 11, 2004, 3:39 PM
 */

/*
 * PMD check performed, and commented unused imports and variables on 13-JULY-2011
 * by Bharati Umarani
 */

package edu.mit.coeus.instprop.controller;

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.specialreview.SpecialReviewForm;
import edu.mit.coeus.bean.SRApprovalInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;

import java.util.Vector;
import javax.swing.JComponent;



/** /**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
* @author chandru
*/
public class SpecialReviewController extends InstituteProposalController{
    
    private char functionType; 
    private CoeusVector cvTableData;
    private QueryEngine queryEngine;
    private SpecialReviewForm specialReviewForm;
    private CoeusVector cvSpecialReview;// Get the data for the special Review comboBox
    private CoeusVector cvApproval;// Get the data for the approval combobox
    private CoeusVector cvValidate;// Validate Vector
    private Vector vecSpecialReviewData;
    private static  final String INSTITUTE_PROPOSAL_MODULE_CODE="INSTITUTE_PROPOSAL";
    private JComponent cmpMain;
    private boolean formSaveRequired = false;
    private CoeusVector cvDeletedData;
    private int rowId;//=1;
    
    //For the Coeus enhancement case:#1799  start step:1
    private int enableProposalToProtocolLink;
    private int linkedToIRBCode;
    private int specialReviewTypeCode;
    private CoeusVector cvParameters;
    //End Coeus Enhancement case:#1799 step:1
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    private int linkedToIACUCCode;
    private int enableProposalToIacucProtocolLink;
    private int specialRevTypeCodeParamForIacuc;   
    private CoeusVector cvSpRvData;
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
     /** Creates a new instance of SpecialReviewController */
    public SpecialReviewController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType) {
        super(instituteProposalBaseBean);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        setFormData(instituteProposalBaseBean);
    }
    
    public SpecialReviewController(InstituteProposalBaseBean instituteProposalBaseBean, char functionType,CoeusVector cvParameters) {
        super(instituteProposalBaseBean);
        this.functionType = functionType;
        this.cvParameters = cvParameters;
        queryEngine = QueryEngine.getInstance();
        registerComponents();
        setFormData(instituteProposalBaseBean);
    }
    
    public void display() {
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
    
      public void setRefreshRequired(boolean refreshRequired) {
        super.setRefreshRequired(refreshRequired);
    }
    
    public boolean isRefreshRequired() {
        boolean retValue;
        
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
     /** refreshes the GUI controlled by this. */    
    public void refresh() {
        if(isRefreshRequired()) {
            int selectedRow = specialReviewForm.tblSpecialReview.getSelectedRow();
            int rowCount = specialReviewForm.tblSpecialReview.getRowCount();
            setFormData(instituteProposalBaseBean);
            setRefreshRequired(false);
            specialReviewForm.setSaveRequired(false);
            specialReviewForm.formatFields();
            if(selectedRow!= -1) {
                specialReviewForm.tblSpecialReview.setRowSelectionInterval(selectedRow, selectedRow);
            }
            if(rowCount < 1){
                specialReviewForm.btnDelete.setEnabled(false);
            }
        }
    }
    
    
    public void setFormData(Object instituteProposalBaseBean) {
        try {
        this.instituteProposalBaseBean = (InstituteProposalBaseBean)instituteProposalBaseBean;
        cvTableData = new CoeusVector();
        cvSpecialReview = new CoeusVector();
        cvApproval = new CoeusVector();
        cvValidate = new CoeusVector();
        
        cvSpecialReview = queryEngine.getDetails(queryKey, KeyConstants.SPECIAL_REVIEW_TYPE);
        cvApproval = queryEngine.getDetails(queryKey, KeyConstants.SPECIAL_REVIEW_APPROVAL_TYPE);
        cvTableData = queryEngine.executeQuery(queryKey, InstituteProposalSpecialReviewBean.class,CoeusVector.FILTER_ACTIVE_BEANS);//, new NotEquals("acType", TypeConstants.DELETE_RECORD));
        //cvTableData.sort("specialReviewDescription",true);
        cvValidate = queryEngine.executeQuery(queryKey,SRApprovalInfoBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        if(cvTableData!= null && cvTableData.size() > 0){
            rowId = cvTableData.size();
        }
        
        //Added for Coeus Enhancement Case #1799 - start:  step 2
        if(cvParameters == null){
            cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
        }
        for (int index=0;index<cvParameters.size();index++) {
            CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
            if(CoeusConstants.ENABLE_PROTOCOL_TO_PROPOSAL_LINK.equals(coeusParameterBean.getParameterName())){
                enableProposalToProtocolLink = Integer.parseInt(coeusParameterBean.getParameterValue());
            }else if(CoeusConstants.LINKED_TO_IRB_CODE.equals(coeusParameterBean.getParameterName())){
                linkedToIRBCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //getting value for the parameters
            }else if(CoeusConstants.ENABLE_IACUC_PROTOCOL_TO_PROPOSAL_LINK.equals(coeusParameterBean.getParameterName())){
                enableProposalToIacucProtocolLink = Integer.parseInt(coeusParameterBean.getParameterValue());
            }else if(CoeusConstants.LINKED_TO_IACUC_CODE.equals(coeusParameterBean.getParameterName())){
                linkedToIACUCCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                //Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
            }else if(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN.equals(coeusParameterBean.getParameterName())){
                specialReviewTypeCode = Integer.parseInt(coeusParameterBean.getParameterValue());
            }else if(CoeusConstants.IACUC_SPL_REV_TYPE_CODE.equals(coeusParameterBean.getParameterName())){
                specialRevTypeCodeParamForIacuc = Integer.parseInt(coeusParameterBean.getParameterValue());
            }//Added for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
           
        }
        for(int index=0;index<cvTableData.size();index++){
            InstituteProposalSpecialReviewBean specialReviewBean = (InstituteProposalSpecialReviewBean)cvTableData.get(index);
            //Modified for COEUSQA-3418 : Special Reviews approval type & other details do not "stick" in Dev Proposal, IP, or Award - start
            //If Special review is of type Human Subjects(specialReviewTypeCode) then get the actual status of the protocol
            //If Special review is of type Animal Usage(specialRevTypeCodeParamForIacuc) then get the actual status of the protocol
            //Added for COEUSQA-3569 : Existing Animal Usage Special Review Dates not Visible and New Dates Cannot be Entered - start
            if(specialReviewBean.getApprovalCode() == linkedToIRBCode && specialReviewBean.getSpecialReviewCode() == specialReviewTypeCode
                    && enableProposalToProtocolLink == 1){
                ProtocolInfoBean protocolInfoBean = getProtocolDetails(specialReviewBean.getProtocolSPRevNumber().trim());
                specialReviewBean.setApprovalDescription(protocolInfoBean.getProtocolStatusDesc());
                specialReviewBean.setApplicationDate(protocolInfoBean.getApplicationDate());
                specialReviewBean.setApprovalDate(protocolInfoBean.getApprovalDate());
                //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                //if approval type code of special review is link to IACUC then need to sync the status of that special review
            }else if(specialReviewBean.getApprovalCode() == linkedToIACUCCode && specialReviewBean.getSpecialReviewCode() == specialRevTypeCodeParamForIacuc
                    && enableProposalToIacucProtocolLink == 1){
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
        if( specialReviewForm == null ) {
            specialReviewForm=new SpecialReviewForm(functionType,(Vector)cvTableData,INSTITUTE_PROPOSAL_MODULE_CODE);
             specialReviewForm.setEnableProtocolLink(enableProposalToProtocolLink);
             specialReviewForm.setEnableIacucProtocolLink(enableProposalToIacucProtocolLink);
            setSpecialReviewCode();
            specialReviewForm.setApprovalTypes((Vector)cvApproval);
            specialReviewForm.setSpecialReviewTypeCodes((Vector)cvSpecialReview);
            //Coeus Enhancement Case #1799 - end step:2
            cmpMain = (JComponent)specialReviewForm.showSpecialReviewForm(CoeusGuiConstants.getMDIForm());
            
            //                specialReviewForm.setApprovalTypes((Vector)cvApproval);
            //                specialReviewForm.setSpecialReviewTypeCodes((Vector)cvSpecialReview);
            
            specialReviewForm.setValidateVector((Vector)cvValidate);
        }else{
            specialReviewForm.setSpecialReviewData(cvTableData);
            
            specialReviewForm.setApprovalTypes((Vector)cvApproval);
            specialReviewForm.setSpecialReviewTypeCodes((Vector)cvSpecialReview);
            specialReviewForm.setValidateVector((Vector)cvValidate);
            //Added for Coeus Enhancement Case #1799 - start:  step3
            setSpecialReviewCode();
            //Coeus Enhancement Case #1799 - end step:3
            specialReviewForm.setFormData();
            specialReviewForm.formatFields();
            specialReviewForm.setTableEditors();
        }
            specialReviewForm.setSaveRequired(false);
            setFormSaveRequired(false);
       
    }catch (Exception e) {
        e.printStackTrace();
    }
        
    }
    
    //For the Coeus Enhancement case:#1799 start step:4,to get the protocol details for the protocol number
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
    }//End Coeus Enhancement case:#1799 step:4
    
    //Commented by shiji for for fixing bug id: 2002 i.e. A new row with same protocol number is
    //not getting saved as this method checks whether protocol number already exists in which case it is not 
    //inserted into the query engine.
   /* public void saveFormData() {
        try{
                cvTableData = (CoeusVector)specialReviewForm.getSpecialReviewData();
                CoeusVector cvData = queryEngine.getDetails(queryKey,InstituteProposalSpecialReviewBean.class);
                if(cvTableData!= null && cvTableData.size() > 0){
                    InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = null;
                    for(int index = 0; index < cvTableData.size(); index++){
                        SpecialReviewFormBean bean = (SpecialReviewFormBean)cvTableData.get(index);
                        //For the Coeus Enhancement 1799 start step:5
                        if(enableProposalToProtocolLink == 1 && bean.getSpecialReviewCode() == specialReviewTypeCode){
                            bean.setSpecialReviewCode(specialReviewTypeCode);
                            bean.setApprovalCode(linkedToIRBCode);
                            bean.setApplicationDate(null);
                            bean.setApprovalDate(null);
                        }
                        //End Coeus Enhancement 1799 step:5
                        if(bean.getAcType()!= null){
                            instituteProposalSpecialReviewBean = new InstituteProposalSpecialReviewBean(bean);
                            instituteProposalSpecialReviewBean.setProposalNumber(instituteProposalBaseBean.getProposalNumber());
                            if(instituteProposalSpecialReviewBean.getAcType().equalsIgnoreCase("null")){
                                instituteProposalSpecialReviewBean.setProtocolSPRevNumber(null);
                            }
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                 instituteProposalSpecialReviewBean.setAcType(TypeConstants.DELETE_RECORD);
                                 queryEngine.delete(queryKey, instituteProposalSpecialReviewBean);
                                 instituteProposalSpecialReviewBean.setAcType(TypeConstants.INSERT_RECORD);
                                 rowId = rowId + 1;
                                 instituteProposalSpecialReviewBean.setRowId(rowId);
                                 queryEngine.insert(queryKey, instituteProposalSpecialReviewBean);
                            }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                instituteProposalSpecialReviewBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, instituteProposalSpecialReviewBean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                //For the Coeus Enhancement 1799 start step:6
                                boolean isContinue = false;
                                for(int idx=0;idx<cvData.size();idx++){
                                    InstituteProposalSpecialReviewBean instBean = (InstituteProposalSpecialReviewBean)cvData.get(idx);
                                    if(bean.getProtocolSPRevNumber() != null  && bean.getProtocolSPRevNumber().equals(instBean.getProtocolSPRevNumber()) ) {
                                        isContinue=true;
                                        break;
                                    }
                                    else if(instBean.getProtocolSPRevNumber().equals(bean.getPrevSpRevProtocolNumber())) {
                                        queryEngine.removeData(queryKey,InstituteProposalSpecialReviewBean.class,idx);
                                    }
                                    
                                }
                                if(isContinue){
                                    continue;
                                }
                                //End Coeus Enhancement 1799 start step:6
                                rowId = rowId+1;
                                instituteProposalSpecialReviewBean.setRowId(rowId);
                                instituteProposalSpecialReviewBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, instituteProposalSpecialReviewBean);
                            }
                        }
                    }
                }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally{
            specialReviewForm.setSaveRequired(false);
            setFormSaveRequired(false);
            //refresh();
        }
    }*/
    
    // Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /**
     * To get the special review data
     *@return CoeusVector containing all special review beans
     */
    public CoeusVector getSpecialReviewData(){
        cvSpRvData = new CoeusVector();
        cvSpRvData =  (CoeusVector)specialReviewForm.getSpecialReviewDataForMail();
        return cvSpRvData;
    }
    // Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
    
    //Modified by shiji for fixing bug id:2002 such that the method checks whether the 
    //protocol number already exists only in the case of Human Subjects.
    public void saveFormData() {
        try{
            cvTableData = (CoeusVector)specialReviewForm.getSpecialReviewData();       
            
            CoeusVector cvData = queryEngine.getDetails(queryKey,InstituteProposalSpecialReviewBean.class);
            if(cvTableData!= null && cvTableData.size() > 0){
                InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean = null;
                for(int index = 0; index < cvTableData.size(); index++){
                    SpecialReviewFormBean bean = (SpecialReviewFormBean)cvTableData.get(index);
                    //For the Coeus Enhancement 1799 start step:5
                    if(enableProposalToProtocolLink == 1 && bean.getSpecialReviewCode() == specialReviewTypeCode){
                        bean.setSpecialReviewCode(specialReviewTypeCode);
                        bean.setApprovalCode(linkedToIRBCode);
                        bean.setApplicationDate(null);
                        bean.setApprovalDate(null);
                    }
                    //End Coeus Enhancement 1799 step:5
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    //If parameter IACUC_SPL_REV_TYPE_CODE is enabled then set it as linked to IACUC
                    else if(enableProposalToIacucProtocolLink == 1 && bean.getSpecialReviewCode() == specialRevTypeCodeParamForIacuc){
                        bean.setSpecialReviewCode(specialRevTypeCodeParamForIacuc);
                        bean.setApprovalCode(linkedToIACUCCode);
                        bean.setApplicationDate(null);
                        bean.setApprovalDate(null);
                    }
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
                    
                    if(bean.getAcType()!= null){
                        // Check for "null" also
                        if(!bean.getAcType().equals("null")){
                            instituteProposalSpecialReviewBean = new InstituteProposalSpecialReviewBean(bean);
                            instituteProposalSpecialReviewBean.setProposalNumber(instituteProposalBaseBean.getProposalNumber());
                            if(instituteProposalSpecialReviewBean.getAcType().equalsIgnoreCase("null")){
                                instituteProposalSpecialReviewBean.setProtocolSPRevNumber(null);
                            }
                            if(bean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                                instituteProposalSpecialReviewBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, instituteProposalSpecialReviewBean);
                                instituteProposalSpecialReviewBean.setAcType(TypeConstants.INSERT_RECORD);
                                rowId = rowId + 1;
                                instituteProposalSpecialReviewBean.setRowId(rowId);
                                queryEngine.insert(queryKey, instituteProposalSpecialReviewBean);
                            }else if(bean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                                instituteProposalSpecialReviewBean.setAcType(TypeConstants.DELETE_RECORD);
                                queryEngine.delete(queryKey, instituteProposalSpecialReviewBean);
                            }else if(bean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                                //For the Coeus Enhancement 1799 start step:6
                                //bug id: 2002 - start
                                //Commented COEUSDEV-312 :First Protocol on Proposal not being saved on Award record. - Start
                                //Commented for not to check the duplication of the special review protocol
//                                if(bean.getSpecialReviewCode() == 1) {
//                                    //bug id: 2002 - end
//                                    boolean isContinue = false;
//                                    for(int idx=0;idx<cvData.size();idx++){
//                                        InstituteProposalSpecialReviewBean instBean = (InstituteProposalSpecialReviewBean)cvData.get(idx);
//                                        if(bean.getProtocolSPRevNumber() != null  && bean.getProtocolSPRevNumber().equals(instBean.getProtocolSPRevNumber()) ) {
//                                            isContinue=true;
//                                            break;
//                                        }
//                                        else if(instBean.getProtocolSPRevNumber().equals(bean.getPrevSpRevProtocolNumber())) {
//                                            queryEngine.removeData(queryKey,InstituteProposalSpecialReviewBean.class,idx);
//                                        }
//                                        
//                                    }
//                                    if(isContinue){
//                                        continue;
//                                    }
//                                }
                                //End Coeus Enhancement 1799 start step:6
                                //COEUSDEV-312 : End
                                rowId = rowId+1;
                                instituteProposalSpecialReviewBean.setRowId(rowId);
                                instituteProposalSpecialReviewBean.setAcType(TypeConstants.INSERT_RECORD);
                                queryEngine.insert(queryKey, instituteProposalSpecialReviewBean);
                            }
                        }
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally{
            specialReviewForm.setSaveRequired(false);
            setFormSaveRequired(false);
            //refresh();
        }
    }
    
    //Added for Coeus Enhancement Case #1799 - start:  step 7
    public void setSpecialReviewCode() {
        try {
            if(cvParameters == null){
                cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            }
            //cvParameters = queryEngine.executeQuery(queryKey,CoeusParameterBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            for (int index=0;index<cvParameters.size();index++) {
                CoeusParameterBean coeusParameterBean=(CoeusParameterBean)cvParameters.elementAt(index);
                if(CoeusConstants.SPL_REV_TYPE_CODE_HUMAN.equals(coeusParameterBean.getParameterName())){
                    specialReviewTypeCode = Integer.parseInt(coeusParameterBean.getParameterValue());
                    specialReviewForm.setSpecialRevTypeCode(coeusParameterBean.getParameterValue());
                    //break;
                    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
                    //getting the value for the parameter IACUC_SPL_REV_TYPE_CODE from parameter table
                }else if(CoeusConstants.IACUC_SPL_REV_TYPE_CODE.equals(coeusParameterBean.getParameterName())){
                    specialRevTypeCodeParamForIacuc = Integer.parseInt(coeusParameterBean.getParameterValue());
                    specialReviewForm.setSpecialRevTypeCodeForIacuc(coeusParameterBean.getParameterValue());
                    break;
                }//Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
            }
          
        }catch(Exception exception) {
            
        }
    }
    //Coeus Enhancement Case #1799 - end step:7
   
    public void setDefaultFocusInWindow(){
//        if(functionType!= DISPLAY_PROPOSAL){
//            specialReviewForm.btnAdd.requestFocusInWindow();
//        }else{
//            if(specialReviewForm.tblSpecialReview.getRowCount() > 0){
//                specialReviewForm.tblSpecialReview.requestFocusInWindow();
//            }
//        }
        
        specialReviewForm.tblSpecialReview.requestFocusInWindow();
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        try {
            boolean isValid=specialReviewForm.validateData();
            return isValid;
        } catch (Exception e) {
             edu.mit.coeus.exception.CoeusUIException coeusUIException = new edu.mit.coeus.exception.CoeusUIException(e.getMessage());
             coeusUIException.setTabIndex(6);
             throw coeusUIException;
        }
    }
    
     
     /**
     * Getter for property formSaveRequired.
     * @return Value of property formSaveRequired.
     */
    public boolean isFormSaveRequired() {
       try{            		
            if( functionType != DISPLAY_PROPOSAL ) {
                formSaveRequired =  specialReviewForm.isSaveRequired();
                
                //System.out.println("Special Review Form Save Required :"+specialReviewForm.isSaveRequired());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return formSaveRequired;
    }
    
    /**
     * Setter for property formSaveRequired.
     * @param formSaveRequired New value of property formSaveRequired.
     */
    public void setFormSaveRequired(boolean formSaveRequired) {
        this.formSaveRequired = formSaveRequired;
        specialReviewForm.setSaveRequired(formSaveRequired);
    }
    
    //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - start
    /** 
     * if approval type code of special review is link to IACUC then need to sync the status of that special review
     *  get the IACUC protocol details for that special review
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

    public CoeusVector getCvSpRvData() {
        return cvSpRvData;
    }

    public void setCvSpRvData(CoeusVector cvSpRvData) {
        this.cvSpRvData = cvSpRvData;
    }
    
     //Added for COEUSQA-3119- Need to implement IACUC link to Award, IP, Prop Dev, and IRB - end
}
