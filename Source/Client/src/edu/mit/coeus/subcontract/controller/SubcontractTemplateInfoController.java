/*
 * SubcontractTemplateInfoController.java
 *
 * Created on February 3, 2012, 1:26 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.bean.SubcontractTemplateInfoBean;
import edu.mit.coeus.subcontract.gui.SubcontractTemplateInfoForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.TypeSelectionLookUp;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/**
 *
 * @author satheeshkumarkn
 */
public class SubcontractTemplateInfoController extends SubcontractController implements ActionListener {
    
    private SubcontractTemplateInfoForm subcontractTemplateInfoForm;
    private CoeusMessageResources coeusMessageResources;
    private char functionType;
    private SubcontractTemplateInfoBean subcontractTemplateInfoBean;
    private DateUtils dateUtils;
    private final String DATE_SEPARATERS = "-:/.,|";
    private final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private final char YES = 'Y';
    private final char NO = 'N';
    private SimpleDateFormat simpleDateFormat;
    private boolean isModified;
     private final String INVALID_DATE = "subcontractAmountInfo_exceptionCode.1154";
    
     /* JM 2-27-2015 modifications to allow tab access if user has only this right */
     private boolean userHasModify = false;
     private boolean userHasCreate = false;
     /* JM END */
    
    /** Creates a new instance of SubcontractTemplateInfoController */
    public SubcontractTemplateInfoController(SubContractBean subContractBean, char functionType) {
        super(subContractBean);
        this.subContractBean = subContractBean;
        
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */ 
        
        this.functionType = functionType;
        dateUtils = new DateUtils();
        subcontractTemplateInfoForm = new SubcontractTemplateInfoForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        formatFields();
        registerComponents();
        setFormData(null);
    }
    
    public void cleanUp() {
    }
    
    /**
     * Method to get instance of form
     * @return 
     */
    public Component getControlledUI() {
        return subcontractTemplateInfoForm;
    }
    
    /*
     * Method to set form data
     */
    public void setFormData(Object data){
        try {
            isModified = false;
            CoeusVector cvSubcontractTemplateInfo = queryEngine.getDetails(queryKey, SubcontractTemplateInfoBean.class);
            if(cvSubcontractTemplateInfo != null && !cvSubcontractTemplateInfo.isEmpty()){
                subcontractTemplateInfoBean = (SubcontractTemplateInfoBean)cvSubcontractTemplateInfo.get(0);
                if(functionType == NEW_ENTRY_SUBCONTRACT && subContractBean.getSequenceNumber() != subcontractTemplateInfoBean.getSequenceNumber()){
                    subcontractTemplateInfoBean.setSubContractCode(subContractBean.getSubContractCode());
                    subcontractTemplateInfoBean.setSequenceNumber(subContractBean.getSequenceNumber());
                    subcontractTemplateInfoBean.setAcType(TypeConstants.INSERT_RECORD);
                }
            }else if(functionType == NEW_SUBCONTRACT || subcontractTemplateInfoBean == null){
                subcontractTemplateInfoBean = new SubcontractTemplateInfoBean();
                subcontractTemplateInfoBean.setSubContractCode(subContractBean.getSubContractCode());
                subcontractTemplateInfoBean.setSequenceNumber(subContractBean.getSequenceNumber());
                subcontractTemplateInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            }
            
            if(functionType != TypeConstants.DISPLAY_MODE ){
                subcontractTemplateInfoBean.addPropertyChangeListener(
                        new PropertyChangeListener(){
                    public void propertyChange(PropertyChangeEvent pce){
                        
                        if ( pce.getNewValue() == null && pce.getOldValue() != null && !CoeusGuiConstants.EMPTY_STRING.equals(pce.getOldValue().toString().trim())) {
                            isModified = true;
                        }
                        if( pce.getNewValue() != null && pce.getOldValue() == null && !CoeusGuiConstants.EMPTY_STRING.equals(pce.getNewValue().toString().trim())) {
                            isModified = true;
                        }
                        if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                            if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                                isModified = true;
                            }
                        }
                        
                    }
                });
                
            }
            if(subcontractTemplateInfoBean.getSowOrSubProposalBudget() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdSowOrSubProposalBudgetYes,subcontractTemplateInfoForm.rdSowOrSubProposalBudgetNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdSowOrSubProposalBudgetYes,subcontractTemplateInfoForm.rdSowOrSubProposalBudgetNo,false);
            }
            
            if(subcontractTemplateInfoBean.getSubProposalDate() != null){
                String subProposalDate = dateUtils.formatDate(subcontractTemplateInfoBean.getSubProposalDate().toString(),  DATE_FORMAT_DISPLAY);
                subcontractTemplateInfoForm.txtSubProposalDate.setText(subProposalDate);
            }
            
            if(subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeCode() > 0){
                subcontractTemplateInfoForm.txtInvoiceOrPaymentContact.setText(subcontractTemplateInfoBean.getInvoiceOrPaymentContactTypeDesc());
            }
            
            if(subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeCode() > 0){
                subcontractTemplateInfoForm.txtFinalStmtOfCostsContact.setText(subcontractTemplateInfoBean.getFinalStmtOfCostsContactTypeDesc());
            }
            
            if(subcontractTemplateInfoBean.getChangeRequestsContactTypeCode() > 0){
                subcontractTemplateInfoForm.txtChangeRequestsContact.setText(subcontractTemplateInfoBean.getChangeRequestsContactTypeDesc());
            }
            
            if(subcontractTemplateInfoBean.getTerminationContactTypeCode() > 0){
                subcontractTemplateInfoForm.txTerminationContact.setText(subcontractTemplateInfoBean.getTerminationContactTypeDesc());
            }
            
            if(subcontractTemplateInfoBean.getNoCostExtensionContactTypeCode() > 0){
                subcontractTemplateInfoForm.txtNoCostExtensionContact.setText(subcontractTemplateInfoBean.getNoCostExtensionContactTypeDesc());
            }
            
            if(subcontractTemplateInfoBean.getPerfSiteDiffFromOrgAddr() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrYes,subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrYes,subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrNo,false);
            }
            
            if(subcontractTemplateInfoBean.getPerfSiteSameAsSubPiAddr() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrYes,subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrYes,subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrNo,false);
            }
            
            if(subcontractTemplateInfoBean.getSubRegisteredInCcr() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdSubRegisteredInCcrYes,subcontractTemplateInfoForm.rdSubRegisteredInCcrNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdSubRegisteredInCcrYes,subcontractTemplateInfoForm.rdSubRegisteredInCcrNo,false);
            }
            // Commented for COEUSQA-3684 : Subcontract module FDP Agreement Corrections - Start            
//            if(subcontractTemplateInfoBean.getSubExemptFromReportingComp() == YES){
//                enableYesNo(subcontractTemplateInfoForm.rdSubExemptFromReportingCompYes,subcontractTemplateInfoForm.rdSubExemptFromReportingCompNo,true);
//            }else{
//                enableYesNo(subcontractTemplateInfoForm.rdSubExemptFromReportingCompYes,subcontractTemplateInfoForm.rdSubExemptFromReportingCompNo,false);
//            }
            // Commented for COEUSQA-3684 : Subcontract module FDP Agreement Corrections - End
            subcontractTemplateInfoForm.txtParentDunsNumber.setText(subcontractTemplateInfoBean.getParentDunsNumber());
            subcontractTemplateInfoForm.txtParentCongressionalDistrict.setText(subcontractTemplateInfoBean.getParentCongressionalDistrict());
            
            if(subcontractTemplateInfoBean.getExemptFromRprtgExecComp() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdExemptFromRprtgExecCompYes,subcontractTemplateInfoForm.rdExemptFromRprtgExecCompNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdExemptFromRprtgExecCompYes,subcontractTemplateInfoForm.rdExemptFromRprtgExecCompNo,false);
            }
            
            if(subcontractTemplateInfoBean.getCopyrightTypeCode() > 0){
                subcontractTemplateInfoForm.txtCopyrightType.setText(subcontractTemplateInfoBean.getCopyrightTypeDesc());
            }
            
            if(subcontractTemplateInfoBean.getAutomaticCarryForward() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdAutomaticCarryForwardYes,subcontractTemplateInfoForm.rdAutomaticCarryForwardNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdAutomaticCarryForwardYes,subcontractTemplateInfoForm.rdAutomaticCarryForwardNo,false);
            }
            
            if(subcontractTemplateInfoBean.getCarryForwardRequestsSentTo() > 0){
                subcontractTemplateInfoForm.txtCarryForwardRequestsSentTo.setText(subcontractTemplateInfoBean.getCarryForwardRequestsSentToDesc());
            }
            
            if(subcontractTemplateInfoBean.getTreatmentPrgmIncomeAdditive() == YES){
                enableYesNo(subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveYes,subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveNo,true);
            }else{
                enableYesNo(subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveYes,subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveNo,false);
            }
            
            subcontractTemplateInfoForm.txtApplicableProgramRegulations.setText(subcontractTemplateInfoBean.getApplicableProgramRegulations());
            
            if(subcontractTemplateInfoBean.getApplicableProgramRegsDate() != null){
                String applicableDate = dateUtils.formatDate(subcontractTemplateInfoBean.getApplicableProgramRegsDate().toString(),  DATE_FORMAT_DISPLAY);
                subcontractTemplateInfoForm.txtApplicableProgramRegsDate.setText(applicableDate);
            }
            
            
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        
    }
    
    /**
     * Method to get form data
     * @return data
     */
    public Object getFormData() {
        return null;
    }
    
    /*
     * Method to set form fields property
     */
    public void formatFields() {
        if(functionType == TypeConstants.DISPLAY_MODE){
            subcontractTemplateInfoForm.rdSowOrSubProposalBudgetYes.setEnabled(false);
            subcontractTemplateInfoForm.rdSowOrSubProposalBudgetNo.setEnabled(false);
            subcontractTemplateInfoForm.txtSubProposalDate.setEnabled(false);
            subcontractTemplateInfoForm.btnInvoiceOrPaymentContactSearch.setEnabled(false);
            subcontractTemplateInfoForm.btnFinalStmtOfCostsContact.setEnabled(false);
            subcontractTemplateInfoForm.btnChangeRequestsContact.setEnabled(false);
            subcontractTemplateInfoForm.btnTerminationContact.setEnabled(false);
            subcontractTemplateInfoForm.btnNoCostExtensionContact.setEnabled(false);
            subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrYes.setEnabled(false);
            subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrNo.setEnabled(false);
            subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrYes.setEnabled(false);
            subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrNo.setEnabled(false);
            subcontractTemplateInfoForm.rdSubRegisteredInCcrYes.setEnabled(false);
            subcontractTemplateInfoForm.rdSubRegisteredInCcrNo.setEnabled(false);
            // Commented for COEUSQA-3684 : Subcontract module FDP Agreement Corrections - Start
//            subcontractTemplateInfoForm.rdSubExemptFromReportingCompYes.setEnabled(false);
//            subcontractTemplateInfoForm.rdSubExemptFromReportingCompNo.setEnabled(false);
            // Commented for COEUSQA-3684 : Subcontract module FDP Agreement Corrections - End
            subcontractTemplateInfoForm.txtParentDunsNumber.setEnabled(false);
            subcontractTemplateInfoForm.txtParentCongressionalDistrict.setEnabled(false);
            subcontractTemplateInfoForm.rdExemptFromRprtgExecCompYes.setEnabled(false);
            subcontractTemplateInfoForm.rdExemptFromRprtgExecCompNo.setEnabled(false);
            subcontractTemplateInfoForm.btnCopyrightType.setEnabled(false);
            subcontractTemplateInfoForm.rdAutomaticCarryForwardYes.setEnabled(false);
            subcontractTemplateInfoForm.rdAutomaticCarryForwardNo.setEnabled(false);
            subcontractTemplateInfoForm.txtCarryForwardRequestsSentTo.setEnabled(false);
            subcontractTemplateInfoForm.btnCarryForwardRequestsSentTo.setEnabled(false);
            subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveYes.setEnabled(false);
            subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveNo.setEnabled(false);
            subcontractTemplateInfoForm.txtApplicableProgramRegulations.setEnabled(false);
            subcontractTemplateInfoForm.txtApplicableProgramRegsDate.setEnabled(false);
        }
    }
    
    /**
     * Method to validate the form
     * @throws edu.mit.coeus.exception.CoeusUIException 
     * @return true
     */
    public boolean validate() throws CoeusUIException {
        boolean isValidate = true;
        String subProposalDate = subcontractTemplateInfoForm.txtSubProposalDate.getText().trim();
        String applicableProgramRegsDate = subcontractTemplateInfoForm.txtApplicableProgramRegsDate.getText().trim();
        if(!subProposalDate.equals(EMPTY_STRING)) {
            isValidate = dateValidation(subProposalDate,subcontractTemplateInfoForm.txtSubProposalDate);
        }else if(!applicableProgramRegsDate.equals(EMPTY_STRING )) {
            isValidate = dateValidation(applicableProgramRegsDate,subcontractTemplateInfoForm.txtApplicableProgramRegsDate);
        }

        return isValidate;
    }
    
    /**
     * Method to register all form components
     */
    public void registerComponents() {
        java.awt.Component[] components = {subcontractTemplateInfoForm.rdSowOrSubProposalBudgetYes,subcontractTemplateInfoForm.rdSowOrSubProposalBudgetNo,
        subcontractTemplateInfoForm.txtSubProposalDate,subcontractTemplateInfoForm.btnInvoiceOrPaymentContactSearch,subcontractTemplateInfoForm.btnFinalStmtOfCostsContact,
        subcontractTemplateInfoForm.btnChangeRequestsContact,subcontractTemplateInfoForm.btnTerminationContact,subcontractTemplateInfoForm.btnNoCostExtensionContact,
        subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrYes,subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrNo,subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrYes,
        subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrNo,subcontractTemplateInfoForm.rdSubRegisteredInCcrYes,subcontractTemplateInfoForm.rdSubRegisteredInCcrNo,
//        subcontractTemplateInfoForm.rdSubExemptFromReportingCompYes,subcontractTemplateInfoForm.rdSubExemptFromReportingCompNo,
        subcontractTemplateInfoForm.txtParentDunsNumber,subcontractTemplateInfoForm.txtParentCongressionalDistrict,subcontractTemplateInfoForm.rdExemptFromRprtgExecCompYes,
        subcontractTemplateInfoForm.rdExemptFromRprtgExecCompNo,subcontractTemplateInfoForm.btnCopyrightType,subcontractTemplateInfoForm.rdAutomaticCarryForwardYes,
        subcontractTemplateInfoForm.rdAutomaticCarryForwardNo,
        subcontractTemplateInfoForm.txtCarryForwardRequestsSentTo,subcontractTemplateInfoForm.btnCarryForwardRequestsSentTo,subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveYes,
        subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveNo,subcontractTemplateInfoForm.txtApplicableProgramRegulations,subcontractTemplateInfoForm.txtApplicableProgramRegsDate};
        
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        subcontractTemplateInfoForm.setFocusTraversalPolicy(traversePolicy);
        subcontractTemplateInfoForm.setFocusCycleRoot(true);

        if(functionType != TypeConstants.DISPLAY_MODE ){
            subcontractTemplateInfoForm.txtInvoiceOrPaymentContact.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.txtChangeRequestsContact.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.txtNoCostExtensionContact.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.txtCopyrightType.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.txtFinalStmtOfCostsContact.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.txTerminationContact.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.txtCarryForwardRequestsSentTo.setBackground(Color.WHITE);
            subcontractTemplateInfoForm.btnChangeRequestsContact.addActionListener(this);
            subcontractTemplateInfoForm.btnCopyrightType.addActionListener(this);
            subcontractTemplateInfoForm.btnFinalStmtOfCostsContact.addActionListener(this);
            subcontractTemplateInfoForm.btnInvoiceOrPaymentContactSearch.addActionListener(this);
            subcontractTemplateInfoForm.btnNoCostExtensionContact.addActionListener(this);
            subcontractTemplateInfoForm.btnTerminationContact.addActionListener(this);
            subcontractTemplateInfoForm.btnCarryForwardRequestsSentTo.addActionListener(this);
            subcontractTemplateInfoForm.txtApplicableProgramRegulations.setDocument(new LimitedPlainDocument(50));
            subcontractTemplateInfoForm.txtParentDunsNumber.setDocument(new LimitedPlainDocument(20));
            subcontractTemplateInfoForm.txtParentCongressionalDistrict.setDocument(new LimitedPlainDocument(20));
            CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
            subcontractTemplateInfoForm.txtSubProposalDate.addFocusListener(customFocusAdapter);
            subcontractTemplateInfoForm.txtApplicableProgramRegsDate.addFocusListener(customFocusAdapter);
        }
        subcontractTemplateInfoForm.requestFocusInWindow();
    }
    
    /**
     * Method to save the form data to query engine
     */
    public void saveFormData() {
        try{
            String subProposalDate = subcontractTemplateInfoForm.txtSubProposalDate.getText();
            if(! subProposalDate.equals(EMPTY_STRING)) {
                String subProposalDate1 =  dateUtils.formatDate(subProposalDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(subProposalDate1 == null) {
                    subProposalDate1 =dateUtils.restoreDate(subProposalDate, DATE_SEPARATERS);
                    if(subProposalDate1 != null && !subProposalDate1.equals(subProposalDate)){
                        java.util.Date date = simpleDateFormat.parse(dateUtils.restoreDate(subProposalDate,DATE_SEPARATERS));
                        subcontractTemplateInfoBean.setSubProposalDate(new java.sql.Date(date.getTime()));
                    }
                } else {
                    java.util.Date date = simpleDateFormat.parse(dateUtils.restoreDate(subProposalDate1,DATE_SEPARATERS));
                    subcontractTemplateInfoBean.setSubProposalDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subcontractTemplateInfoBean.setSubProposalDate(null);
            }
            if(subcontractTemplateInfoForm.rdSowOrSubProposalBudgetYes.isSelected()){
                subcontractTemplateInfoBean.setSowOrSubProposalBudget(YES);
            }else{
                subcontractTemplateInfoBean.setSowOrSubProposalBudget(NO);
            }
            
            if(subcontractTemplateInfoForm.rdPerfSiteSameAsSubPiAddrYes.isSelected()){
                subcontractTemplateInfoBean.setPerfSiteSameAsSubPiAddr(YES);
            }else{
                subcontractTemplateInfoBean.setPerfSiteSameAsSubPiAddr(NO);
            }
            
            if(subcontractTemplateInfoForm.rdSubRegisteredInCcrYes.isSelected()){
                subcontractTemplateInfoBean.setSubRegisteredInCcr(YES);
            }else{
                subcontractTemplateInfoBean.setSubRegisteredInCcr(NO);
            }

            if(subcontractTemplateInfoForm.rdPerfSiteDiffFromOrgAddrYes.isSelected()){
                subcontractTemplateInfoBean.setPerfSiteDiffFromOrgAddr(YES);
            }else{
                subcontractTemplateInfoBean.setPerfSiteDiffFromOrgAddr(NO);
            }

            if(subcontractTemplateInfoForm.rdSubRegisteredInCcrYes.isSelected()){
                subcontractTemplateInfoBean.setSubRegisteredInCcr(YES);
            }else{
                subcontractTemplateInfoBean.setSubRegisteredInCcr(NO);
            }
            // Commented for COEUSQA-3684 : Subcontract module FDP Agreement Corrections - Start        
//            if(subcontractTemplateInfoForm.rdSubExemptFromReportingCompYes.isSelected()){
//                subcontractTemplateInfoBean.setSubExemptFromReportingComp(YES);
//            }else{
//                subcontractTemplateInfoBean.setSubExemptFromReportingComp(NO);
//            }
            // Commented for COEUSQA-3684 : Subcontract module FDP Agreement Corrections - End
            subcontractTemplateInfoBean.setParentDunsNumber(subcontractTemplateInfoForm.txtParentDunsNumber.getText());
            subcontractTemplateInfoBean.setParentCongressionalDistrict(subcontractTemplateInfoForm.txtParentCongressionalDistrict.getText());
            
            if(subcontractTemplateInfoForm.rdExemptFromRprtgExecCompYes.isSelected()){
                subcontractTemplateInfoBean.setExemptFromRprtgExecComp(YES);
            }else{
                subcontractTemplateInfoBean.setExemptFromRprtgExecComp(NO);
            }

            if(subcontractTemplateInfoForm.rdAutomaticCarryForwardYes.isSelected()){
                subcontractTemplateInfoBean.setAutomaticCarryForward(YES);
            }else{
                subcontractTemplateInfoBean.setAutomaticCarryForward(NO);
            }

            if(subcontractTemplateInfoForm.rdTreatmentPrgmIncomeAdditiveYes.isSelected()){
                subcontractTemplateInfoBean.setTreatmentPrgmIncomeAdditive(YES);
            }else{
                subcontractTemplateInfoBean.setTreatmentPrgmIncomeAdditive(NO);
            }
            subcontractTemplateInfoBean.setApplicableProgramRegulations(subcontractTemplateInfoForm.txtApplicableProgramRegulations.getText());
            String applicableProgramRegsDate = subcontractTemplateInfoForm.txtApplicableProgramRegsDate.getText();
            if(! applicableProgramRegsDate.equals(EMPTY_STRING)) {
                String applicableProgramRegsDate1 =  dateUtils.formatDate(applicableProgramRegsDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(applicableProgramRegsDate1 == null) {
                    applicableProgramRegsDate1 =dateUtils.restoreDate(applicableProgramRegsDate, DATE_SEPARATERS);
                    if(applicableProgramRegsDate1 != null && !applicableProgramRegsDate1.equals(applicableProgramRegsDate)){
                        java.util.Date date = simpleDateFormat.parse(dateUtils.restoreDate(applicableProgramRegsDate,DATE_SEPARATERS));
                        subcontractTemplateInfoBean.setApplicableProgramRegsDate(new java.sql.Date(date.getTime()));
                    }
                } else {
                    java.util.Date date = simpleDateFormat.parse(dateUtils.restoreDate(applicableProgramRegsDate,DATE_SEPARATERS));
                    subcontractTemplateInfoBean.setApplicableProgramRegsDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subcontractTemplateInfoBean.setApplicableProgramRegsDate(null);
            }
            
            if(TypeConstants.INSERT_RECORD.equals(subcontractTemplateInfoBean.getAcType())){
                CoeusVector cvTemplateInfo = new CoeusVector();
                cvTemplateInfo.add(subcontractTemplateInfoBean);
                Hashtable htSubcontract = queryEngine.getDataCollection(queryKey);
                htSubcontract.put(SubcontractTemplateInfoBean.class,cvTemplateInfo);
            }else if(isModified){
                subcontractTemplateInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey,subcontractTemplateInfoBean);
            }
            
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void display() {
    }
    
    /**
     * Method to refresh the form data
     *
     */
    public void refresh(){
        if (isRefreshRequired()) {
            setFormData(subContractBean);
            setRefreshRequired(false);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        try{
            Object source = e.getSource();
            CoeusVector cvTypes = null;
            String lookupTitle = CoeusGuiConstants.EMPTY_STRING;
            CoeusTextField txtTextFieldToSetValue = null;
            if(source.equals(subcontractTemplateInfoForm.btnCopyrightType)){
                cvTypes = queryEngine.getDetails(queryKey,KeyConstants.SUBCONTRACT_COPYRIGHT_TYPES);
                lookupTitle = "Select CopyRight";
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txtCopyrightType;
                int copyRightType = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setCopyrightTypeCode(copyRightType);
            }else{
                cvTypes = queryEngine.getDetails(queryKey,KeyConstants.SUBCONTRACT_CONTACT_TYPES);
                lookupTitle = "Select Contact";
                // To remove the -1 code bean from the collection
                if(cvTypes != null && !cvTypes.isEmpty()){
                    CoeusVector cvFiltered = cvTypes.filter(new Equals("typeCode",-1));
                    if(cvFiltered != null && !cvFiltered.isEmpty()){
                        cvTypes.removeAll(cvFiltered);
                    }
                }
            }
            
            if(source.equals(subcontractTemplateInfoForm.btnChangeRequestsContact)){
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txtChangeRequestsContact;
                int changeRequestsContact = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setChangeRequestsContactTypeCode(changeRequestsContact);
            }else if(source.equals(subcontractTemplateInfoForm.btnFinalStmtOfCostsContact)){
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txtFinalStmtOfCostsContact;
                int finalStmtOfCostsContact = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setFinalStmtOfCostsContactTypeCode(finalStmtOfCostsContact);
            }else if(source.equals(subcontractTemplateInfoForm.btnInvoiceOrPaymentContactSearch)){
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txtInvoiceOrPaymentContact;
                int invoiceOrPaymentContactSearch = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setInvoiceOrPaymentContactTypeCode(invoiceOrPaymentContactSearch);
            }else if(source.equals(subcontractTemplateInfoForm.btnNoCostExtensionContact)){
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txtNoCostExtensionContact;
                int noCostExtensionContact = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setNoCostExtensionContactTypeCode(noCostExtensionContact);
            }else if(source.equals(subcontractTemplateInfoForm.btnTerminationContact)){
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txTerminationContact;
                int terminationContact = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setTerminationContactTypeCode(terminationContact);
            }else if(source.equals(subcontractTemplateInfoForm.btnCarryForwardRequestsSentTo)){
                txtTextFieldToSetValue = subcontractTemplateInfoForm.txtCarryForwardRequestsSentTo;
                int carryForwardRequestsSentTo = typeSelectionAndSetValue(txtTextFieldToSetValue,cvTypes, lookupTitle);
                subcontractTemplateInfoBean.setCarryForwardRequestsSentTo(carryForwardRequestsSentTo);
            }
            
        }catch (CoeusException coeusExcep){
            coeusExcep.printStackTrace();
        }
    }
    
    public class CustomFocusAdapter extends FocusAdapter{
        
        /*the action performed on the focus gain*/
        public void focusGained(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()){
                return ;
            }
            Object source = focusEvent.getSource();
            CoeusTextField dateTextField = (CoeusTextField)source;
            String date = dateTextField.getText();
            date  = dateUtils.restoreDate(date , DATE_SEPARATERS);
            if(date != null){
                dateTextField.setText(date);
            }
        }
        
        /*action performed on the focus lost*/
        public void focusLost(FocusEvent focusEvent){
            if(focusEvent.isTemporary()) {
                return;
            }
            Object source = focusEvent.getSource();
            CoeusTextField dateTextField = (CoeusTextField)source;
            String date = dateTextField.getText().trim();
            dateValidation(date,dateTextField);
            
        }
    }
    
    private boolean dateValidation(String date,CoeusTextField dateTextField){
        if(!date.equals(EMPTY_STRING)) {
            String date1 = dateUtils.formatDate(date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(date1 == null) {
                date1 = dateUtils.restoreDate(date, DATE_SEPARATERS);
                if( date1 == null || date1.equals(date)) {
                    MessageFormat formatter = new MessageFormat("");
                    String fieldName = CoeusGuiConstants.EMPTY_STRING;
                    if(dateTextField.equals(subcontractTemplateInfoForm.txtSubProposalDate)){
                        fieldName = "sub proposal date";
                    }else if(dateTextField.equals(subcontractTemplateInfoForm.txtApplicableProgramRegsDate)){
                        fieldName = "applicable program regs date";
                    }
                    String message = formatter.format(coeusMessageResources.parseMessageKey(INVALID_DATE), fieldName);
                    CoeusOptionPane.showWarningDialog(message);
                    dateTextField.setText(CoeusGuiConstants.EMPTY_STRING);
                    setRequestFocusInThread(dateTextField);
                    return false;
                }
            }else {
                date = date1;
                dateTextField.setText(date);
            }
        }
        return true;
    }
    
    /*
     * Method to set focus in any component
     */
    private void setRequestFocusInThread(final Component component){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                component.requestFocusInWindow();
                
            }
        });
    }
    
    /**
     * Method enable or disable yes and no button
     * @param rdYesButton 
     * @param rdNoButton 
     * @param isYes 
     */
    private void enableYesNo(JRadioButton rdYesButton, JRadioButton rdNoButton, boolean isYes){
        if(isYes){
            rdYesButton.setSelected(true);
        }else{
            rdNoButton.setSelected(true);
        }
    }
    
    /**
     * Method to open TypeSelectionLoopUp to select a type
     * selectionTypeCode - int
     */
    private int typeSelectionAndSetValue(CoeusTextField txtTextFiled, CoeusVector cvTypes, String lookupTitle) throws CoeusException {
        int selectionTypeCode = 0;
        if(cvTypes != null && !cvTypes.isEmpty()){
            TypeSelectionLookUp typeSelectionLookUp = new TypeSelectionLookUp(lookupTitle,ListSelectionModel.SINGLE_SELECTION);
            typeSelectionLookUp.setFormData(cvTypes);
            typeSelectionLookUp.display();
            if(typeSelectionLookUp.getSelectedType() != null){
                txtTextFiled.setText(typeSelectionLookUp.getSelectedType().getTypeDescription());
                selectionTypeCode = typeSelectionLookUp.getSelectedType().getTypeCode();
            }
        }else{
            CoeusOptionPane.showWarningDialog("Types are not defined.");
        }
        return selectionTypeCode;
    }
    
}
