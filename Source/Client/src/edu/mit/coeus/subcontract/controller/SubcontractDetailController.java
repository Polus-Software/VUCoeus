/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/*
 * SubcontractDetailController.java
 *
 * Created on September 2, 2004, 11:47 AM
 */

/* PMD check performed, and commented unused imports and variables on 26-JAN-2011
 * by Bharati
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.subcontract.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.organization.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.unit.gui.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
//coeusqa-1528 start
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//coeusqa-1528 end

/**
 *
 * @author  surekhan
 */
public class SubcontractDetailController extends SubcontractController implements ActionListener , MouseListener {
    
    
    /*the subcontract detail form instance*/
    private SubcontractDetailForm subcontractDetailForm;
    
    /*function type*/
    private char functionType;
    
    /*mdiForm instance*/
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    
    private CoeusMessageResources coeusMessageResources;
    
    private QueryEngine queryEngine;
    
    private CoeusVector cvSubcontract;
    
    private SubContractBean subContractBean;
    
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    
    private static final String DATE_SEPARATERS = "-:/.,|";
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private DateUtils dateUtils = new DateUtils();
    
    private static final String EMPTY_STRING = "";
    
    /**Please enter a valid start date.**/
    private static final String INVALID_START_DATE = "subcontractDetail_exceptionCode.1051";
    
    /*Please enter a valid end date.*/
    private static final String INVALID_END_DATE = "subcontractDetail_exceptionCode.1052";
    
    /*Please enter a valid closeout date*/
    private static final String INVALID_CLOSEOUT_DATE = "subcontractDetail_exceptionCode.1053";
    
    /*Please choose a status.*/
    private static final String STATUS_MSG = "subcontractDetail_exceptionCode.1054";
    
    /*Please select a subcontractor*/
    private static final String SUBCONTRACTOR_MSG = "subcontractDetail_exceptionCode.1055";
    
    /*Please choose a sub award type*/
    private static final String SUBAWARD_MSG = "subcontractDetail_exceptionCode.1056";
    
    /*Please select a requisitioner*/
    private static final String REQ_MSG = "subcontractDetail_exceptionCode.1057";
    
    /*please enter the purchase order number*/
    private static final String PURCHASE_MSG = "subcontractDetail_exceptionCode.1058";
    
    /*You have eneterd an invalid name*/
    private static final String INVALID_NAME = "subcontractDetail_exceptionCode.1059";
    
    /*The investigator name cannot be blank*/
    private static final String ENTER_NAME = "subcontractDetail_exceptionCode.1060";
    
    /*This is an invalid unit number*/
    private static final String INVALID_NUMBER = "subcontractDetail_exceptionCode.1061";
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    private String organizationId;
    
    private String reqName;
    
    private String reqNumber;
    
    private String personId;
    
    private String subcontractorName;
    
    private String OrgId;
    
    private SimpleDateFormat simpleDateFormat;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/SubcontractMaintenenceServlet";
    private static final String PERSON_CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/coeusFunctionsServlet";
    private static final char GET_UNIT_NAME = 'B';
    
    private boolean modified;
    
    private String unitNumber;
    
    private String requisitionerName;
    
    private String dataName;
    
    private String dataNumber;
    
    private String perName;
    
    private boolean personFlag;
    
    private boolean dataFlag;
    
    //Bug Fix: Pass the person id to get the person details Start 1
    private String oldPersonID = "";
    //Bug Fix: Pass the person id to get the person details End 1
    
    //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
    private String oldUnitNumber = EMPTY_STRING;
    //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
    private int accountNumberMaxLength = 0;
    //coeusqa-1528 START
    private String rolodexID;
    //coeusqa-1528 end
    //coeusqa-1563 start 
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    //coeusqa-1563 end
    // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
    private int costTypeCode;
    private static final String INVALID_DATE_FULLY_EXECUTED = "subcontractDetail_exceptionCode.1062";
    private static final String NO_COST_TYPE = "subcontractDetail_exceptionCode.1063";
    // Added for COEUSQA-1412 Subcontract Module changes - Change - End
    
    /* JM 2-27-2015 modifications to allow tab access if user has only this right */
    private boolean userHasModify = false;
    private boolean userHasCreate = false;
    /* JM END */
    
    /** Creates a new instance of SubcontractDetailController */
    public SubcontractDetailController(SubContractBean subContractBean , char functionType ) {
        super(subContractBean);
        
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */ 
        
        this.functionType = functionType;
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        subcontractDetailForm = new SubcontractDetailForm();
        queryEngine = QueryEngine.getInstance();
        cvSubcontract = new CoeusVector();
        setFunctionType(functionType);
        registerComponents();
        setFormData(null);
        
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        
        if(functionType == DISPLAY_SUBCONTRACT){
            subcontractDetailForm.txtSubcontractCode.setEditable(false);
            subcontractDetailForm.txtSeqNumber.setEditable(false);
            subcontractDetailForm.cmbStatus.setEnabled(false);
            subcontractDetailForm.txtAccountNumber.setEditable(false);
            subcontractDetailForm.txtSubcontractor.setEditable(false);
            subcontractDetailForm.txtStartDate.setEditable(false);
            subcontractDetailForm.txtEndDate.setEditable(false);
            subcontractDetailForm.cmbSubawardType.setEnabled(false);
            subcontractDetailForm.txtPurchaseOrderNum.setEditable(false);
            subcontractDetailForm.txtArTitle.setEditable(false);
            subcontractDetailForm.txtRequisitioner.setEditable(false);
            subcontractDetailForm.txtRequisitionerUnit.setEditable(false);
            subcontractDetailForm.txtVendorNumber.setEditable(false);
            subcontractDetailForm.txtCloseOutDate.setEditable(false);
            subcontractDetailForm.txtArchive.setEditable(false);
            subcontractDetailForm.txtArComments.setEditable(false);
            subcontractDetailForm.txtSubcontractor.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            subcontractDetailForm.txtSubcontractor.setOpaque(false);
            subcontractDetailForm.txtArComments.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            subcontractDetailForm.txtArTitle.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            subcontractDetailForm.btnRequisitionerSearch.setEnabled(false);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.btnRequisitionerUnitSearch.setEnabled(false);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            subcontractDetailForm.btnSubcontractorSearch.setEnabled(false);
            // modified for coeusqa-1563 start
            //Added for COEUSQA-3370 : Label changes in Subcontract Details screen in Premium - start
            subcontractDetailForm.txtSiteInvestigatorName.setEditable(false);
            //Added for COEUSQA-3370 : Label changes in Subcontract Details screen in Premium - end
            subcontractDetailForm.txtSiteInvestigatorName.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            subcontractDetailForm.txtSiteInvestigatorName.setOpaque(false);
            subcontractDetailForm.btnSiteInvestigatorSearch.setEnabled(false);
            // modified for coeusqa-1563 end
            // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
            subcontractDetailForm.txtCostType.setEditable(false);
            subcontractDetailForm.btnCostType.setEnabled(false);
            subcontractDetailForm.txtNegotiationNumber.setEditable(false);
            subcontractDetailForm.txtDateFullyExecuted.setEditable(false);
            subcontractDetailForm.txtRequistionNumber.setEditable(false);
            // Added for COEUSQA-1412 Subcontract Module changes - Change - End
        }
            //Commented for COEUSQA-3370 : Label changes in Subcontract Details screen in Premium - start
        //subcontractDetailForm.txtSiteInvestigatorName.setEnabled(false);
        //Commented for COEUSQA-3370 : Label changes in Subcontract Details screen in Premium - end
    }
    
    /** An overridden method of the controller
     * @return subcontractContactsForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return subcontractDetailForm;
    }
    
    /** Returns the form data
     * @return returns the form data
     */
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        
        CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        
        java.awt.Component[] components = {
            subcontractDetailForm.cmbStatus,subcontractDetailForm.txtAccountNumber,
            subcontractDetailForm.btnSubcontractorSearch,subcontractDetailForm.txtNegotiationNumber, subcontractDetailForm.txtSiteInvestigatorName,subcontractDetailForm.btnSiteInvestigatorSearch,subcontractDetailForm.txtStartDate,
            subcontractDetailForm.txtEndDate,subcontractDetailForm.cmbSubawardType,
            subcontractDetailForm.txtPurchaseOrderNum,subcontractDetailForm.txtCostType, subcontractDetailForm.btnCostType, subcontractDetailForm.txtDateFullyExecuted,
            subcontractDetailForm.txtArTitle,subcontractDetailForm.txtRequistionNumber,
            subcontractDetailForm.txtRequisitioner,subcontractDetailForm.btnRequisitionerSearch,
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.txtRequisitionerUnit,subcontractDetailForm.btnRequisitionerUnitSearch,
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            subcontractDetailForm.txtVendorNumber,
            subcontractDetailForm.txtCloseOutDate,subcontractDetailForm.txtArchive,
            subcontractDetailForm.txtArComments};
            
            subcontractDetailForm.txtSubcontractor.setBackground(Color.WHITE);
            subcontractDetailForm.txtCostType.setBackground(Color.WHITE);
            ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
            subcontractDetailForm.setFocusTraversalPolicy(traversePolicy);
            subcontractDetailForm.setFocusCycleRoot(true);
            
            subcontractDetailForm.btnRequisitionerSearch.addActionListener(this);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.btnRequisitionerUnitSearch.addActionListener(this);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            subcontractDetailForm.btnSubcontractorSearch.addActionListener(this);
                        
            //Bug fix Case# 2274--start
            //subcontractDetailForm.txtAccountNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 7));
           // subcontractDetailForm.txtAccountNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 7));
            //Bug fix Case# 2274--end
            //modified for coeusqa-1563 start
            subcontractDetailForm.txtSiteInvestigatorName.addMouseListener(this);
            subcontractDetailForm.btnSiteInvestigatorSearch.addActionListener(this);
            /* As the field is not editable set the background and the fore ground colors in
             *the edit and the new mode */
            if(!subcontractDetailForm.txtSiteInvestigatorName.isEditable() && getFunctionType() != DISPLAY_SUBCONTRACT){
                subcontractDetailForm.txtSiteInvestigatorName.setBackground(Color.WHITE);
                subcontractDetailForm.txtSiteInvestigatorName.setForeground(Color.BLACK);
                subcontractDetailForm.txtSiteInvestigatorName.setOpaque(true);
            }
            // modified for coeusqa-1563 end
	    subcontractDetailForm.txtAccountNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 8));
            subcontractDetailForm.txtPurchaseOrderNum.setDocument(new LimitedPlainDocument(10));
            subcontractDetailForm.txtVendorNumber.setDocument(new LimitedPlainDocument(10));
            subcontractDetailForm.txtCloseOutDate.setDocument(new LimitedPlainDocument(21));
			subcontractDetailForm.txtArComments.setDocument(new LimitedPlainDocument(3878));
            subcontractDetailForm.pnlAmounts.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            subcontractDetailForm.txtArTitle.setDocument(new LimitedPlainDocument(150));
            subcontractDetailForm.txtArchive.setDocument(new LimitedPlainDocument(50));
            subcontractDetailForm.txtSubcontractor.addMouseListener(this);
            subcontractDetailForm.txtRequisitioner.addMouseListener(this);
            subcontractDetailForm.txtRequisitionerUnit.addMouseListener(this);
            // Commented since added mouse listener already
//            subcontractDetailForm.txtSiteInvestigatorName.addMouseListener(this);
            subcontractDetailForm.txtStartDate.addFocusListener(customFocusAdapter);
            subcontractDetailForm.txtEndDate.addFocusListener(customFocusAdapter);
            subcontractDetailForm.txtCloseOutDate.addFocusListener(customFocusAdapter);
            subcontractDetailForm.txtRequisitioner.addFocusListener(customFocusAdapter);
            // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
            subcontractDetailForm.txtDateFullyExecuted.addFocusListener(customFocusAdapter);
            subcontractDetailForm.txtNegotiationNumber.setDocument(new LimitedPlainDocument(8));
            subcontractDetailForm.btnCostType.addActionListener(this);
            subcontractDetailForm.txtRequistionNumber.setDocument(new LimitedPlainDocument(50));
            // Added for COEUSQA-1412 Subcontract Module changes - Change - End
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.txtRequisitionerUnit.addFocusListener(customFocusAdapter);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
//            subcontractDetailForm.txtSiteInvestigatorName.addFocusListener(customFocusAdapter); 
            subcontractDetailForm.txtSubcontractCode.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            subcontractDetailForm.txtObligatedAmount.setEnabled(false);
            subcontractDetailForm.txtAvailableAmount.setEnabled(false);
            subcontractDetailForm.txtAmountReleased.setEnabled(false);
            subcontractDetailForm.txtAnticipatedAmount.setEnabled(false);
            subcontractDetailForm.txtObligatedAmount.setOpaque(false);
            subcontractDetailForm.txtAnticipatedAmount.setOpaque(false);
            subcontractDetailForm.txtAvailableAmount.setOpaque(false);
            subcontractDetailForm.txtAmountReleased.setOpaque(false);
            /* As the field is not editable set the background and the fore ground colors in
             *the edit and the new mode */
            if(!subcontractDetailForm.txtSubcontractor.isEditable() && getFunctionType() != DISPLAY_SUBCONTRACT){
                subcontractDetailForm.txtSubcontractor.setBackground(Color.WHITE);
                subcontractDetailForm.txtSubcontractor.setForeground(Color.BLACK);
                subcontractDetailForm.txtSubcontractor.setOpaque(true);
            }
            subcontractDetailForm.txtObligatedAmount.setDisabledTextColor(Color.BLACK);
            subcontractDetailForm.txtAnticipatedAmount.setDisabledTextColor(Color.BLACK);
            subcontractDetailForm.txtAvailableAmount.setDisabledTextColor(Color.BLACK);
            subcontractDetailForm.txtAmountReleased.setDisabledTextColor(Color.BLACK);
            
            // 3187: Add Last Update and Last Update User fields to the Subcontracts Module - Start
            subcontractDetailForm.txtUpdateUser.setEnabled(false);
            subcontractDetailForm.txtUpdateTimestamp.setEnabled(false);
            subcontractDetailForm.txtUpdateUser.setOpaque(false);
            subcontractDetailForm.txtUpdateTimestamp.setOpaque(false);
            // 3187: Add Last Update and Last Update User fields to the Subcontracts Module - End

    }
    
    /* saves the form data*/
    public void saveFormData() {
        
        ComboBoxBean comboBoxBean;
        int code;
        String strDate;    
        
        Date date;
        try{
            
            /*setting the comboValue to the combo status*/
            comboBoxBean = (ComboBoxBean)subcontractDetailForm.cmbStatus.getSelectedItem();
            if(! comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                subContractBean.setStatusCode(code);
                subContractBean.setStatusDescription(comboBoxBean.getDescription());
            }
            
            /*setting the comboValue to the subaward type comno*/
            comboBoxBean = (ComboBoxBean)subcontractDetailForm.cmbSubawardType.getSelectedItem();
            if(!comboBoxBean.getCode().equals(EMPTY_STRING)) {
                code = Integer.parseInt(comboBoxBean.getCode().trim());
                subContractBean.setSubAwardTypeCode(code);
                subContractBean.setSubAwardTypeDescription(comboBoxBean.getDescription());
            }
            
            subContractBean.setAccountNumber(subcontractDetailForm.txtAccountNumber.getText().trim());
            
            if(! subcontractDetailForm.txtSubcontractor.getText().trim().equals(subContractBean.getSubContractorName())) {
                subContractBean.setSubContractorName(subcontractDetailForm.txtSubcontractor.getText());
            }
            subContractBean.setSubContractId(OrgId);
            
            subContractBean.setPurchaseOrderNumber(subcontractDetailForm.txtPurchaseOrderNum.getText());
            subContractBean.setTitle(subcontractDetailForm.txtArTitle.getText());
            subContractBean.setVendorNumber(subcontractDetailForm.txtVendorNumber.getText());
            subContractBean.setArchiveLocation(subcontractDetailForm.txtArchive.getText());
            subContractBean.setComments(subcontractDetailForm.txtArComments.getText());
            subContractBean.setRequisitionerName(subcontractDetailForm.txtRequisitioner.getText());
            //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            if(subcontractDetailForm.txtRequisitionerUnit.getText().trim().equalsIgnoreCase(EMPTY_STRING)){
                subContractBean.setRequisitionerUnit(EMPTY_STRING);
                subContractBean.setRequisitionerUnitName(EMPTY_STRING);           
            }else{
            //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end    
                subContractBean.setRequisitionerUnit(reqNumber);
                subContractBean.setRequisitionerUnitName(reqName);
            }
            subContractBean.setRequisitionerId(personId);
            subContractBean.setSubContractorName(subcontractorName);
            
            if(CoeusGuiConstants.EMPTY_STRING.equals(subcontractDetailForm.txtSiteInvestigatorName.getText().trim())){
                subContractBean.setSiteInvestigator(0);
            }else if(rolodexID != null){
                subContractBean.setSiteInvestigator(Integer.parseInt(rolodexID));
            }
            strDate = subcontractDetailForm.txtStartDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null) {
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if(strDate1 != null && !strDate1.equals(strDate)){
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        subContractBean.setStartDate(new java.sql.Date(date.getTime()));
                    }else{
                        if(getFunctionType() == CORRECT_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }else if(getFunctionType() == NEW_ENTRY_SUBCONTRACT || getFunctionType() == NEW_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                    }
                } else {
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate1,DATE_SEPARATERS));
                    subContractBean.setStartDate(new java.sql.Date(date.getTime()));
                }
           }else{
                subContractBean.setStartDate(null);
            }
            
            
            
            strDate = subcontractDetailForm.txtEndDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null) {
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if(strDate1 != null && !strDate1.equals(strDate)){
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        subContractBean.setEndDate(new java.sql.Date(date.getTime()));
                    }else{
                        if(getFunctionType() == CORRECT_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }else if(getFunctionType() == NEW_ENTRY_SUBCONTRACT || getFunctionType() == NEW_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                    }
                }else{
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate1,DATE_SEPARATERS));
                    subContractBean.setEndDate(new java.sql.Date(date.getTime()));
                }
            }else{
                
                subContractBean.setEndDate(null);
            }
            
            strDate = subcontractDetailForm.txtCloseOutDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null) {
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    if(strDate1 != null && !strDate1.equals(strDate)){
                        date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                        subContractBean.setCloseOutDate(new java.sql.Date(date.getTime()));
                    }else{
                        if(getFunctionType() == CORRECT_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }else if(getFunctionType() == NEW_ENTRY_SUBCONTRACT || getFunctionType() == NEW_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                    }
                }else{
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate1,DATE_SEPARATERS));
                    subContractBean.setCloseOutDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subContractBean.setCloseOutDate(null);
            }
            // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
            if(CoeusGuiConstants.EMPTY_STRING.equals(subcontractDetailForm.txtCostType.getText().trim())){
                subContractBean.setCostType(0);
            }else if(costTypeCode > 0 ){
                subContractBean.setCostType(costTypeCode);
            }
            subContractBean.setNegotiationNumber(subcontractDetailForm.txtNegotiationNumber.getText().trim());
            subContractBean.setRequistionNumber(subcontractDetailForm.txtRequistionNumber.getText().trim());
            String dateOfFullyExecuted = subcontractDetailForm.txtDateFullyExecuted.getText().trim();
            if(! dateOfFullyExecuted.equals(EMPTY_STRING)) {
                String dateOfFullyExecuted1 =  dateUtils.formatDate(dateOfFullyExecuted, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(dateOfFullyExecuted1 == null) {
                    dateOfFullyExecuted1 =dateUtils.restoreDate(dateOfFullyExecuted, DATE_SEPARATERS);
                    if(dateOfFullyExecuted1 != null && !dateOfFullyExecuted1.equals(dateOfFullyExecuted)){
                        date = simpleDateFormat.parse(dateUtils.restoreDate(dateOfFullyExecuted,DATE_SEPARATERS));
                        subContractBean.setDateOfFullyExecuted(new java.sql.Date(date.getTime()));
                    }else{
                        if(getFunctionType() == CORRECT_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }else if(getFunctionType() == NEW_ENTRY_SUBCONTRACT || getFunctionType() == NEW_SUBCONTRACT) {
                            subContractBean.setAcType(TypeConstants.INSERT_RECORD);
                        }
                    }
                }else{
                    date = simpleDateFormat.parse(dateUtils.restoreDate(dateOfFullyExecuted1,DATE_SEPARATERS));
                    subContractBean.setDateOfFullyExecuted(new java.sql.Date(date.getTime()));
                }
            }else{
                
                subContractBean.setDateOfFullyExecuted(null);
            }
            subContractBean.setReportsIndicator(subContractBean.getReportsIndicator());
            // Added for COEUSQA-1412 Subcontract Module changes - Change - End
            
            subContractBean.setFundingSourceIndicator(subContractBean.getFundingSourceIndicator());
            subContractBean.setCloseOutIndicator(subContractBean.getCloseOutIndicator());
            
            CoeusVector cvTemp = queryEngine.getDetails(queryKey , SubContractBean.class);
            SubContractBean bean = (SubContractBean)cvTemp.get(0);
            subContractBean.setSubContractAmountInfo(bean.getSubContractAmountInfo());
            subContractBean.setAuditorData(bean.getAuditorData());
            StrictEquals strEquals = new StrictEquals();
            if(!strEquals.compare(bean , subContractBean)){
                if(getFunctionType() == CORRECT_SUBCONTRACT){
                    subContractBean.setAcType(TypeConstants.UPDATE_RECORD);
                    queryEngine.update(queryKey, subContractBean);
                }else{
                    subContractBean.setAcType(TypeConstants.INSERT_RECORD);
                    queryEngine.insert(queryKey ,subContractBean);
                }
                setSaveRequired(true);
                modified = true;
            }
        }catch(ParseException parseException){
            parseException.printStackTrace();
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }

    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data)  {
        
        CoeusVector cvStatus;
        CoeusVector cvAwardType;
        try{
            coeusMessageResources = CoeusMessageResources.getInstance();
            this.subContractBean = (SubContractBean)subContractBean;
            cvSubcontract = queryEngine.getDetails(queryKey , SubContractBean.class);
            if(cvSubcontract != null && cvSubcontract.size() > 0){
                subContractBean = (SubContractBean)cvSubcontract.get(0);
            }else{
                subContractBean = new SubContractBean();
            }
            cvStatus = queryEngine.getDetails(queryKey , KeyConstants.SUBCONTRACT_STATUS);
            cvStatus.sort("description");
            ComboBoxBean emptyBean = new ComboBoxBean("" , "");
            if(getFunctionType() == NEW_SUBCONTRACT){
                cvStatus.add(0,emptyBean);
                
            }
            subcontractDetailForm.cmbStatus.setModel(new DefaultComboBoxModel(cvStatus));
            
            
            cvAwardType = queryEngine.getDetails(queryKey,KeyConstants.AWARD_TYPE);
            cvAwardType.sort("description");
            ComboBoxBean bean = new ComboBoxBean("" , "");
            cvAwardType.add(0,bean);
            subcontractDetailForm.cmbSubawardType.setModel(new DefaultComboBoxModel(cvAwardType));
            
            subcontractDetailForm.txtSubcontractCode.setText(subContractBean.getSubContractCode());
            subcontractDetailForm.txtSeqNumber.setText(EMPTY_STRING + subContractBean.getSequenceNumber());
            //Added for Case#2402- use a parameter to set the length of the account number throughout app - Start

            QueryEngine queryEngine = QueryEngine.getInstance();
            CoeusVector cvParameters = queryEngine.executeQuery(queryKey, CoeusParameterBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            //To get the INDIRECT_COST_COMMENT_CODE parameter
            if(cvParameters != null && cvParameters.size()>0){
                CoeusVector cvFiltered = null;
               //To get the MAX_ACCOUNT_NUMBER_LENGTH parameter
               cvFiltered = cvParameters.filter(new Equals("parameterName", CoeusConstants.MAX_ACCOUNT_NUMBER_LENGTH));
                if(cvFiltered != null
                        && cvFiltered.size() > 0){
                    CoeusParameterBean parameterBean = (CoeusParameterBean)cvFiltered.get(0);
                    accountNumberMaxLength = Integer.parseInt(parameterBean.getParameterValue());
                    
                }
            }
            //Sets the accountnumber text field to accept alphanumeric with comma,hyphen and period
            subcontractDetailForm.txtAccountNumber.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
            //Commented to set appropriate accountNumber text to txtAccoutNumber field
            //accountNumber is truncated to accountNumberMaxLength, if the accountNumber length is greater than accountNumberMaxLength value
            //subcontractDetailForm.txtAccountNumber.setText(subContractBean.getAccountNumber());
            String accountNumber = subContractBean.getAccountNumber();
            if(accountNumber == null){
                accountNumber = CoeusGuiConstants.EMPTY_STRING ;
            }else if(accountNumber.length() > accountNumberMaxLength){
                accountNumber = accountNumber.substring(0,accountNumberMaxLength);
            }
            subcontractDetailForm.txtAccountNumber.setText(accountNumber);
            //Case#2402 - End
                
            subcontractDetailForm.txtSubcontractor.setText(subContractBean.getSubContractorName());
            subcontractDetailForm.txtPurchaseOrderNum.setText(subContractBean.getPurchaseOrderNumber());
            subcontractDetailForm.txtArTitle.setText(subContractBean.getTitle());
            subcontractDetailForm.txtArTitle.setCaretPosition(0);
            subcontractDetailForm.txtRequisitioner.setText(subContractBean.getRequisitionerName());
            requisitionerName = subContractBean.getRequisitionerName();
            if(subContractBean.getRequisitionerUnit() == null && subContractBean.getRequisitionerUnitName() == null){                
                subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);               
                //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            }else if(subContractBean.getRequisitionerUnitName() == null || subContractBean.getRequisitionerUnitName().equals(EMPTY_STRING)){
                //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                //subcontractDetailForm.txtRequisitionerUnit.setText(subContractBean.getRequisitionerUnit() + ",  " + EMPTY_STRING);
                subcontractDetailForm.txtRequisitionerUnit.setText(subContractBean.getRequisitionerUnit());
                subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            }else{
                //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                //subcontractDetailForm.txtRequisitionerUnit.setText(subContractBean.getRequisitionerUnit() + ",  " + subContractBean.getRequisitionerUnitName());
                subcontractDetailForm.txtRequisitionerUnit.setText(subContractBean.getRequisitionerUnit());                
                subcontractDetailForm.lblRequistionerUnitDesc.setText(subContractBean.getRequisitionerUnitName());
                //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-END
            }
            subcontractDetailForm.txtVendorNumber.setText(subContractBean.getVendorNumber());
            subcontractDetailForm.txtArchive.setText(subContractBean.getArchiveLocation());
            subcontractDetailForm.txtArComments.setText(subContractBean.getComments());
            subcontractDetailForm.txtArComments.setCaretPosition(0);
            if(subContractBean.getStartDate()!= null){
                subcontractDetailForm.txtStartDate.setText(
                dateUtils.formatDate(subContractBean.getStartDate().toString(),DATE_FORMAT_DISPLAY));
            }else{
                subcontractDetailForm.txtStartDate.setText(null);
            }
            
            
            if(subContractBean.getEndDate()!= null){
                subcontractDetailForm.txtEndDate.setText(
                dateUtils.formatDate(subContractBean.getEndDate().toString(),DATE_FORMAT_DISPLAY));
            }else{
                subcontractDetailForm.txtEndDate.setText(null);
            }
            
            
            if(subContractBean.getCloseOutDate()!= null){
                subcontractDetailForm.txtCloseOutDate.setText(
                dateUtils.formatDate(subContractBean.getCloseOutDate().toString(),DATE_FORMAT_DISPLAY));
            }else{
                subcontractDetailForm.txtCloseOutDate.setText(null);
            }
            
            ComboBoxBean comboBoxBean = new ComboBoxBean();
            comboBoxBean.setCode(EMPTY_STRING + subContractBean.getStatusCode());
            comboBoxBean.setDescription(subContractBean.getStatusDescription());
            subcontractDetailForm.cmbStatus.setSelectedItem(comboBoxBean);
            
            
            ComboBoxBean cmbBean = new ComboBoxBean();
            cmbBean.setCode(EMPTY_STRING + subContractBean.getSubAwardTypeCode());
            cmbBean.setDescription(subContractBean.getSubAwardTypeDescription());
            subcontractDetailForm.cmbSubawardType.setSelectedItem(cmbBean);
            
            organizationId = subContractBean.getSubContractId();
            
            if(getFunctionType() == NEW_SUBCONTRACT){
                subcontractDetailForm.txtSubcontractCode.setText(subContractBean.getSubContractCode());
                setSaveRequired(true);
                modified = true;
            }
            reqNumber = subContractBean.getRequisitionerUnit();
            reqName = subContractBean.getRequisitionerUnitName();
            personId = subContractBean.getRequisitionerId();
            subcontractorName = subContractBean.getSubContractorName();
            OrgId = subContractBean.getSubContractId();
            
            //Bug Fix: Pass the person id to get the person details Start 2
            String requisitioner = subcontractDetailForm.txtRequisitioner.getText();
            if(requisitioner != null && !requisitioner.trim().equals(EMPTY_STRING)){
                oldPersonID = requisitioner.trim();
            }
            //Bug Fix: Pass the person id to get the person details End 2
            //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            if(subcontractDetailForm.txtRequisitionerUnit.getText().trim() != null && !subcontractDetailForm.txtRequisitionerUnit.getText().trim().equalsIgnoreCase(EMPTY_STRING)){
                oldUnitNumber = subcontractDetailForm.txtRequisitionerUnit.getText().trim();
            }
            //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            // 3187: Add Last Update and Last Update User fields to the Subcontracts Module - Start
            subcontractDetailForm.txtUpdateUser.setText(subContractBean.getUpdateUserName());
            java.sql.Timestamp updateTimestamp = subContractBean.getUpdateTimestamp();
            if(updateTimestamp != null){
                subcontractDetailForm.txtUpdateTimestamp.setText(CoeusDateFormat.format(
                        updateTimestamp.toString()));
            }
			//modified for coeusqa-1563 start
            if (subContractBean.getSiteInvestigatorName()!= null &&
                    !subContractBean.getSiteInvestigatorName().equals("")){
                subcontractDetailForm.txtSiteInvestigatorName.setText(subContractBean.getSiteInvestigatorName());
            }else{
                subcontractDetailForm.txtSiteInvestigatorName.setText("");
                
            }
			// coeusqa-1563 end

            // 3187: Add Last Update and Last Update User fields to the Subcontracts Module - End
            // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
            subcontractDetailForm.txtNegotiationNumber.setText(subContractBean.getNegotiationNumber());
            subcontractDetailForm.txtRequistionNumber.setText(subContractBean.getRequistionNumber());
            subcontractDetailForm.txtCostType.setText(subContractBean.getCostTypeDescription());
            if(subContractBean.getDateOfFullyExecuted()!= null){
                subcontractDetailForm.txtDateFullyExecuted.setText(
                dateUtils.formatDate(subContractBean.getDateOfFullyExecuted().toString(),DATE_FORMAT_DISPLAY));
            }else{
                subcontractDetailForm.txtDateFullyExecuted.setText(null);
            }
            // Added for COEUSQA-1412 Subcontract Module changes - Change - End
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    /* to set the amount fields data to the particular fields*/
    public void setAmounts(CoeusVector cvData ){
        if(cvData.get(0).toString().equals(EMPTY_STRING)){
            subcontractDetailForm.txtObligatedAmount.setText(".00");
        }else{
            subcontractDetailForm.txtObligatedAmount.setText(cvData.get(0).toString());
            subcontractDetailForm.txtObligatedAmount.setValue(Double.parseDouble(cvData.get(0).toString()));
        }
        if(cvData.get(1).toString().equals(EMPTY_STRING)){
            subcontractDetailForm.txtAnticipatedAmount.setText(".00");
        }else{
            subcontractDetailForm.txtAnticipatedAmount.setText(cvData.get(1).toString());
            subcontractDetailForm.txtAnticipatedAmount.setValue(Double.parseDouble(cvData.get(1).toString()));
        }
        if(cvData.get(3).toString().equals(EMPTY_STRING)){
            subcontractDetailForm.txtAvailableAmount.setText(".00");
            
        }else{
            subcontractDetailForm.txtAvailableAmount.setText(cvData.get(3).toString());
            subcontractDetailForm.txtAvailableAmount.setValue(Double.parseDouble(cvData.get(3).toString()));
        }
        if(cvData.get(2).toString().equals(EMPTY_STRING)){
            subcontractDetailForm.txtAmountReleased.setText(".00");
        }else{
            subcontractDetailForm.txtAmountReleased.setText(cvData.get(2).toString());
            subcontractDetailForm.txtAmountReleased.setValue(Double.parseDouble(cvData.get(2).toString()));
        }
        
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        if(subcontractDetailForm.cmbStatus.getSelectedItem().toString().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(STATUS_MSG));
            setRequestFocusInThread(subcontractDetailForm.cmbStatus);
            return false;
        }else if(subcontractDetailForm.txtSubcontractor.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUBCONTRACTOR_MSG));
            setRequestFocusInThread(subcontractDetailForm.btnSubcontractorSearch);
            return false;
        }else if(subcontractDetailForm.cmbSubawardType.getSelectedItem().toString().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUBAWARD_MSG));
            setRequestFocusInThread(subcontractDetailForm.cmbSubawardType);
            return false;
            
        }else if(subcontractDetailForm.txtRequisitioner.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REQ_MSG));
            setRequestFocusInThread(subcontractDetailForm.btnRequisitionerSearch);
            return false;
        }else if(subcontractDetailForm.txtPurchaseOrderNum.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PURCHASE_MSG));
            setRequestFocusInThread(subcontractDetailForm.txtPurchaseOrderNum);
            return false;
        }
        //start date validation
        String strDate = subcontractDetailForm.txtStartDate.getText().trim();
        if(!strDate.equals(EMPTY_STRING)) {
            if(subcontractDetailForm.txtStartDate.hasFocus()) {
                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                strDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                strDate = dateUtils.formatDate(strDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(strDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
                setRequestFocusInThread(subcontractDetailForm.txtStartDate);
                return false;
            }else {
                subcontractDetailForm.txtStartDate.setText(strDate);
            }
        }
        //End start Date Validation.
        
        
        //end date validation
        String endDate = subcontractDetailForm.txtEndDate.getText().trim();
        if(!endDate.equals(EMPTY_STRING)) {
            if(subcontractDetailForm.txtEndDate.hasFocus()) {
                endDate = dateUtils.formatDate(endDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                endDate = dateUtils.restoreDate(endDate, DATE_SEPARATERS);
                endDate = dateUtils.formatDate(endDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(endDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_END_DATE));
                setRequestFocusInThread(subcontractDetailForm.txtEndDate);
                return false;
            }else {
                subcontractDetailForm.txtEndDate.setText(endDate);
            }
        }
        //End end Date Validation.
        
        //closeout date validation
        String closeOutdate = subcontractDetailForm.txtCloseOutDate.getText().trim();
        if(!closeOutdate.equals(EMPTY_STRING)) {
            if(subcontractDetailForm.txtEndDate.hasFocus()) {
                closeOutdate = dateUtils.formatDate(closeOutdate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                closeOutdate = dateUtils.restoreDate(closeOutdate, DATE_SEPARATERS);
                closeOutdate = dateUtils.formatDate(closeOutdate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(closeOutdate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_CLOSEOUT_DATE));
                setRequestFocusInThread(subcontractDetailForm.txtCloseOutDate);
                return false;
            }else {
                subcontractDetailForm.txtCloseOutDate.setText(closeOutdate);
            }
        }
        
        //End closeout Validation.
        
        
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
        String dateFullyExecuted = subcontractDetailForm.txtDateFullyExecuted.getText().trim();
        if(!dateFullyExecuted.equals(EMPTY_STRING)) {
            if(subcontractDetailForm.txtDateFullyExecuted.hasFocus()) {
                dateFullyExecuted = dateUtils.formatDate(dateFullyExecuted,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                dateFullyExecuted = dateUtils.restoreDate(dateFullyExecuted, DATE_SEPARATERS);
                dateFullyExecuted = dateUtils.formatDate(dateFullyExecuted,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(dateFullyExecuted == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE_FULLY_EXECUTED));
                setRequestFocusInThread(subcontractDetailForm.txtDateFullyExecuted);
                return false;
            }else {
                subcontractDetailForm.txtDateFullyExecuted.setText(dateFullyExecuted);
            }
        }
        
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start  
        
        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(subcontractDetailForm.btnRequisitionerSearch)){
            displayPersonSearch();
        }else if(source.equals(subcontractDetailForm.btnSubcontractorSearch)){
            displayOrganizationSearch();
        }
	 //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
        else if(source.equals(subcontractDetailForm.btnRequisitionerUnitSearch)){
            displayUnitSearch();
        }
        //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-End
        else if(source.equals(subcontractDetailForm.btnSiteInvestigatorSearch)){
            displayRolodexSearch();
        }
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
        else if(source.equals(subcontractDetailForm.btnCostType)){
            try {
                displayCostTypes();
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
        }

        // Added for COEUSQA-1412 Subcontract Module changes - Change - End
    }
     private void displayRolodexSearch() {
        CoeusSearch siteInvestigatorSearch = null;
        Vector vecRolodex = null;
        HashMap rolodexData = null;

        try {
            siteInvestigatorSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm()
                    ,CoeusGuiConstants.ROLODEX_SEARCH
                    ,CoeusSearch.TWO_TABS);
            siteInvestigatorSearch.showSearchWindow();
            rolodexData = siteInvestigatorSearch.getSelectedRow();
            if( rolodexData != null ){
                rolodexID = checkForNull(rolodexData.get( "ROLODEX_ID" ));
                String firstName = checkForNull(rolodexData.get( "FIRST_NAME" ));
                String lastName = checkForNull(rolodexData.get( "LAST_NAME" ));
                String middleName = checkForNull(rolodexData.get( "MIDDLE_NAME" ));
                String rolodexName = null;
                if ( firstName.length() > 0) {
                    rolodexName = ( lastName +" "+", " + firstName +" "+ middleName ).trim();
                } else {
                    rolodexName = checkForNull(rolodexData.get("ORGANIZATION"));
                }
                subcontractDetailForm.txtSiteInvestigatorName.setText(rolodexName);
                //subcontractDetailForm.txtSiteInvestigatorId.setText(rolodexID);
                subContractBean.setSiteInvestigator(Integer.parseInt(rolodexID));
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
    }
	private String checkForNull( Object value ){
            return (value==null)? "":value.toString();
        }
//modified for coeusqa-1563 end

    /** displays sponsor search. */
    public  void displayPersonSearch() {
        
        try{
            CoeusSearch personSearch = null;
            personSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "PERSONSEARCH",1);
            personSearch.showSearchWindow();
            HashMap personData = personSearch.getSelectedRow();
            perName = requisitionerName;
            if( personData != null){
                String personName = personData.get( "FULL_NAME" ).toString();
                if(personData.get("HOME_UNIT") == null){
                    unitNumber = null;
                    subcontractDetailForm.txtRequisitioner.setText(personName);
                }else{
                    unitNumber = personData.get("HOME_UNIT").toString();
                }
                String personNumber = personData.get("PERSON_ID").toString();
                subcontractDetailForm.txtRequisitioner.setText(personName);
                requisitionerName = personName;
                getDataFromServer();
                personId = personNumber;
                
                ///Bug Fix: Pass the person id to get the person details Start 3
                subContractBean.setRequisitionerId(personId);
                oldPersonID = personName.trim();
                //Bug Fix: Pass the person id to get the person details End 3
            }
        }catch( Exception err ){
            
            err.printStackTrace();
        }
    }
    //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
    /** displays unit search. */
    public  void displayUnitSearch() {        
        try{
            CoeusSearch unitSearch = null;
            unitSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "LEADUNITSEARCH",CoeusSearch.TWO_TABS);
            unitSearch.showSearchWindow();
            HashMap selectedData = unitSearch.getSelectedRow();
            if( selectedData != null){
                String unitID = checkForNull(selectedData.get( "UNIT_NUMBER" ));
                String unitName = checkForNull(selectedData.get( "UNIT_NAME" ));
                if(unitID == null){
                     subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
                     subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                     reqNumber = EMPTY_STRING;
                     reqName = EMPTY_STRING;
                }else if(unitName == null){
                    subcontractDetailForm.txtRequisitionerUnit.setText(unitID);
                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                     reqNumber = unitID;
                     reqName = EMPTY_STRING;
                }else{
                subcontractDetailForm.txtRequisitionerUnit.setText(unitID);
                subcontractDetailForm.lblRequistionerUnitDesc.setText(unitName);
                reqName = unitName;
                reqNumber = unitID;
                subContractBean.setRequisitionerUnit(reqNumber);
                subContractBean.setRequisitionerUnitName(reqName);
                oldUnitNumber = reqNumber;
                }
            }
        }catch( Exception err ){
            
            err.printStackTrace();
        }
    }
    //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-End
    /*to get the requisitioner data from the server and set the particular data*/
    public boolean getDataFromServer(){
        String unitName;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_UNIT_NAME);
        requesterBean.setDataObject(unitNumber);
        if(unitNumber == null){
            subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            reqNumber = EMPTY_STRING;
            reqName = EMPTY_STRING;
            return true;
        }
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CONNECTION_STRING);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null){
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        if(!responderBean.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        if(responderBean.getDataObject() == null){
            /*If the unit name is null or length is zero then set the
             *previous selected name's unit number and unit name are set to the dataName
             *snd dataNumber which are set to the fieldd after the validation on focus lost*/
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.txtRequisitionerUnit.setText(unitNumber);            
            subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            personFlag = true;
            dataFlag = true;
            dataName = reqName;
            dataNumber = reqNumber;
            if(personFlag){
                dataName = EMPTY_STRING;
                dataNumber = unitNumber;
                dataFlag = true;
            }
            return false;
        }else{
            unitName = responderBean.getDataObject().toString();
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            subcontractDetailForm.txtRequisitionerUnit.setText(unitNumber);
            subcontractDetailForm.lblRequistionerUnitDesc.setText(unitName);
            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            reqName = unitName;
            reqNumber = unitNumber;
            return true;
        }
        
    }
    
    /*to display the organisation search on throws click of the search button*/
    public void displayOrganizationSearch(){
        try{
            CoeusSearch organizationSearch = null;
            organizationSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(),"ORGANIZATIONSEARCH" ,1);
            organizationSearch.showSearchWindow();
            HashMap organizationData = organizationSearch.getSelectedRow();
            if(organizationData != null){
                String name = organizationData.get("ORGANIZATION_NAME").toString();
                String organId = organizationData.get("ORGANIZATION_ID").toString();
                subcontractDetailForm.txtSubcontractor.setText(name);
                subcontractorName = name;
                OrgId = organId;
                organizationId = organId;
            }
            
        }catch(Exception err){
            err.printStackTrace();
        }
    }
    
    /* class for recieving the focus events*/
    public class CustomFocusAdapter extends FocusAdapter{
        
        /*the action performed on the focus gain*/
        public void focusGained(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
            Object source = focusEvent.getSource();
            if(source.equals(subcontractDetailForm.txtStartDate)) {
                String startDate;
                startDate = subcontractDetailForm.txtStartDate.getText();
                startDate  = dateUtils.restoreDate(startDate , DATE_SEPARATERS);
                if(startDate != null){
                subcontractDetailForm.txtStartDate.setText(startDate );
                }
            }else if(source.equals(subcontractDetailForm.txtEndDate)) {
                String endDate;
                endDate = subcontractDetailForm.txtEndDate.getText();
                endDate = dateUtils.restoreDate(endDate , DATE_SEPARATERS);
                subcontractDetailForm.txtEndDate.setText(endDate);
            }else if(source.equals(subcontractDetailForm.txtCloseOutDate)){
                String closeOutDate;
                closeOutDate = subcontractDetailForm.txtCloseOutDate.getText();
                closeOutDate = dateUtils.restoreDate(closeOutDate , DATE_SEPARATERS);
                subcontractDetailForm.txtCloseOutDate.setText(closeOutDate);
            }else if(source.equals(subcontractDetailForm.txtDateFullyExecuted)){
                String dateFullyExecuted = subcontractDetailForm.txtDateFullyExecuted.getText();
                dateFullyExecuted = dateUtils.restoreDate(dateFullyExecuted , DATE_SEPARATERS);
                subcontractDetailForm.txtDateFullyExecuted.setText(dateFullyExecuted);
            }
        }
        
        /*action performed on the focus lost*/
        public void focusLost(FocusEvent focusEvent){
            if(focusEvent.isTemporary()) return;
            Object source = focusEvent.getSource();
            PersonInfoFormBean personInfoFormBean;
            //Commented for the case #  COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
//            if(subcontractDetailForm.txtRequisitioner.getText() == null && subcontractDetailForm.txtRequisitioner.getText().equals(EMPTY_STRING)) return;
//            String value = subcontractDetailForm.txtRequisitioner.getText().trim();
//            
//            //Bug Fix: Pass the person id to get the person details Start 4
//            if(oldPersonID.equals(value)){
//                return ;
//            }else{
//                oldPersonID = value;
//            }
//            //Bug Fix: Pass the person id to get the person details End 4
//            
//            //personInfoFormBean = coeusUtils.getPersonInfoID(value.toString());
//            
//            //Bug fix for person Validation on focus lost Start 1
//            personInfoFormBean = checkPerson(value);
//            //Bug fix for person Validation on focus lost End 1
            //Commented for the case #  COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
            if(source.equals(subcontractDetailForm.txtRequisitioner)){
                //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                if(subcontractDetailForm.txtRequisitioner.getText() == null && subcontractDetailForm.txtRequisitioner.getText().equals(EMPTY_STRING)) return;
                String value = subcontractDetailForm.txtRequisitioner.getText().trim();
                if(oldPersonID.equals(value)){
                    return ;
                }else{
                    oldPersonID = value;
                }
                personInfoFormBean = checkPerson(value);
                //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                if(!subcontractDetailForm.txtRequisitioner.getText().trim().equals(EMPTY_STRING) && subcontractDetailForm.txtRequisitioner.getText() != null){
                    
                    if( personInfoFormBean == null ){
                        
                        //Bug fix for person Validation on focus lost Start 2
                        //CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_NAME));
                        //Bug fix for person Validation on focus lost End 2
                        
                        subcontractDetailForm.txtRequisitioner.setText(requisitionerName);
                        /*if the unit name is null then set the previus selected name to the requisitioner field*/
                        if(personFlag = true){
                            if(dataFlag){
                                /*set the previous selected name to the requisitioner field*/
                                if(perName == null){
                                    subcontractDetailForm.txtRequisitioner.setText(EMPTY_STRING);
                                }
                                subcontractDetailForm.txtRequisitioner.setText(perName);
                                personInfoFormBean = coeusUtils.getPersonInfoID(perName);
                                personId = personInfoFormBean.getPersonID();
                                dataFlag = false;
                            }
                            /*if the unit number is existind and unit name is null then set the unit name to the empty string
                             else set it to the previous values*/
                            if(reqName == null){
                                if(reqNumber == null){
                                    subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
                                    //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                                }else{
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    // subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber + ",  " +EMPTY_STRING);
                                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                                }
                            }else{
                                if(reqNumber == null){
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    //subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING + ",  " +reqName);
                                    subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);                                    
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                                }else if(reqName == null){
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    //subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber + ",  " +EMPTY_STRING);
                                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                                }else{
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    // subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber + ",  " +reqName);
                                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);                                    
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                                    personFlag = false;
                                    return;
                                }
                            }
                        }
                        /*If the requisitioner selected doesnt have the unitname then set the previous one based on the selection
                         *or written manually*/
                        if(dataName != null && dataNumber != null){
                            //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                            //subcontractDetailForm.txtRequisitionerUnit.setText(dataNumber + ",  " +dataName);
                            subcontractDetailForm.txtRequisitionerUnit.setText(dataNumber);
                            subcontractDetailForm.lblRequistionerUnitDesc.setText(dataName);
                            //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                        } else {
                            if(reqNumber.equals(EMPTY_STRING) || reqNumber == ""){
                                subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
                                //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                            }else{
                                if(reqName == null){
                                    //Modiifed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    //subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber + ",  " +EMPTY_STRING);
                                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);                                    
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                                }else{
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                    //subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber + ",  " +reqName);
                                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                                    //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                                }
                            }
                            setRequestFocusInThread(subcontractDetailForm.txtVendorNumber);
                            return;
                        }
                    }else{
                     String requisitioner = subcontractDetailForm.txtRequisitioner.getText().trim();
                    requisitionerName = requisitioner;
                    RequesterBean requesterBean = new RequesterBean();
                    requesterBean.setDataObject("GET_PERSONINFO");
                    requesterBean.setId(requisitioner);
                    
                    AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
                    appletServletCommunicator.setConnectTo(PERSON_CONNECTION_STRING);
                    appletServletCommunicator.setRequest(requesterBean);
                    appletServletCommunicator.send();
                    ResponderBean responderBean = appletServletCommunicator.getResponse();
                    if (responderBean!=null){
                        
                        personInfoFormBean =
                        (PersonInfoFormBean) responderBean.getDataObject();
                        unitNumber = personInfoFormBean.getHomeUnit();
                        personId = personInfoFormBean.getPersonID();
                        if(!getDataFromServer()){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_NUMBER));
                            if(personFlag = true){
                                subcontractDetailForm.txtRequisitioner.setText(perName);
                                if(reqNumber == null && reqName == null){
                                    subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                                    return;
                                }
                                if(reqNumber == null ){
                                    subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                                    return;
                                }
                                if(reqName == null){
                                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                                    subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                                    return;
                                }
                                subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                                subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                                personInfoFormBean = coeusUtils.getPersonInfoID(perName);
                                personId = personInfoFormBean.getPersonID();
                                return;
                            }
                            if(dataName != null && dataNumber != null){
                                subcontractDetailForm.txtRequisitioner.setText(perName);
                                subcontractDetailForm.txtRequisitionerUnit.setText(dataNumber);
                                subcontractDetailForm.lblRequistionerUnitDesc.setText(dataName);
                                personInfoFormBean = coeusUtils.getPersonInfoID(perName);
                                personId = personInfoFormBean.getPersonID();
                                return;
                            }else{
                                subcontractDetailForm.txtRequisitioner.setText(requisitionerName);
                                subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                                subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                                personInfoFormBean = coeusUtils.getPersonInfoID(requisitionerName);
                                personId = personInfoFormBean.getPersonID();
                                return;
                            }
                        }else{
                            String name = personInfoFormBean.getFullName();
                            subcontractDetailForm.txtRequisitioner.setText(name);
                        }
                    }
                    }
                }else if(subcontractDetailForm.txtRequisitioner.getText().length() == 0 || subcontractDetailForm.txtRequisitioner.getText().trim().equals(EMPTY_STRING)){
                    if(getFunctionType() != NEW_SUBCONTRACT ){
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_NAME));
                        return;
                    }
                }
            }
            if(source.equals(subcontractDetailForm.txtStartDate)) {
                String startDate;
                startDate = subcontractDetailForm.txtStartDate.getText().trim();
                
                if(!startDate.equals(EMPTY_STRING)) {
                    String strDate1 = dateUtils.formatDate(startDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(strDate1 == null) {
                        strDate1 = dateUtils.restoreDate(startDate, DATE_SEPARATERS);
                        if( strDate1 == null || strDate1.equals(startDate)) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
                            setRequestFocusInThread(subcontractDetailForm.txtStartDate);
                            return ;
                        }
                    }else {
                        startDate = strDate1;
                        subcontractDetailForm.txtStartDate.setText(startDate);
                        
                    }
                }
                
//            }//coeusqa-1563 start
//         else if(source.equals(subcontractDetailForm.txtSiteInvestigatorName)){
//            try{
//                String siteInvestigator = subcontractDetailForm.txtSiteInvestigatorName.getText().trim();
//                    if(siteInvestigator!=null && !EMPTY_STRING.equals(siteInvestigator)) {
//                        getRolodexFullName(siteInvestigator);           
//                    }
//            }catch(Exception exception){
//                exception.printStackTrace();
//            }//coeusqa-1563 end
        } else if(source.equals(subcontractDetailForm.txtEndDate)){
                String endDate;
                endDate = subcontractDetailForm.txtEndDate.getText().trim();
                
                if(!endDate.equals(EMPTY_STRING)) {
                    String strDate1 = dateUtils.formatDate(endDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(strDate1 == null) {
                        strDate1 = dateUtils.restoreDate(endDate, DATE_SEPARATERS);
                        if( strDate1 == null || strDate1.equals(endDate)) {
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_END_DATE));
                            setRequestFocusInThread(subcontractDetailForm.txtEndDate);
                            return ;
                        }
                    }else {
                        endDate = strDate1;
                        subcontractDetailForm.txtEndDate.setText(endDate);
                        
                    }
                }
            }else if(source.equals(subcontractDetailForm.txtCloseOutDate)){
                String closeOutDate;
                closeOutDate = subcontractDetailForm.txtCloseOutDate.getText().trim();
                if(!closeOutDate.equals(EMPTY_STRING)) {
                    String date = dateUtils.formatDate(closeOutDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(date == null) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_CLOSEOUT_DATE));
                        setRequestFocusInThread(subcontractDetailForm.txtCloseOutDate);
                        return ;
                        
                    }else {
                        closeOutDate = date;
                        subcontractDetailForm.txtCloseOutDate.setText(closeOutDate);
                        
                    }
                }
            }else if(source.equals(subcontractDetailForm.txtDateFullyExecuted)){
                String dateFullyExecuted = subcontractDetailForm.txtDateFullyExecuted.getText().trim();
                if(!dateFullyExecuted.equals(EMPTY_STRING)) {
                    String date = dateUtils.formatDate(dateFullyExecuted, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                    if(date == null) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE_FULLY_EXECUTED));
                        setRequestFocusInThread(subcontractDetailForm.txtDateFullyExecuted);
                        return ;
                        
                    }else {
                        dateFullyExecuted = date;
                        subcontractDetailForm.txtDateFullyExecuted.setText(dateFullyExecuted);
                        
                    }
                }
            }
            //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
            else if(source.equals(subcontractDetailForm.txtRequisitionerUnit)){
                String value = EMPTY_STRING;
                String unitNoTemp = reqNumber;
                String unitNameTemp = reqName;
                //int indexOfUnitNo = subcontractDetailForm.txtRequisitionerUnit.getText().trim().indexOf(",");
                if(subcontractDetailForm.txtRequisitionerUnit.getText().trim() != null && !subcontractDetailForm.txtRequisitionerUnit.getText().trim().equalsIgnoreCase(EMPTY_STRING)){
                        value = subcontractDetailForm.txtRequisitionerUnit.getText().trim();
                }              
                //Cheking old unit numbe rwith current value, if it is same then it returns.
                if(oldUnitNumber.equals(value)){
                    subcontractDetailForm.txtRequisitionerUnit.setText( unitNoTemp);
                    subcontractDetailForm.lblRequistionerUnitDesc.setText(unitNameTemp);
                    return ;
                }else{
                    oldUnitNumber = value;
                }
                if(!subcontractDetailForm.txtRequisitionerUnit.getText().trim().equals(EMPTY_STRING) && subcontractDetailForm.txtRequisitionerUnit.getText() != null){
                    //unit number is set, to get the unit name value, from the method getDataFromServer();
                        unitNumber = subcontractDetailForm.txtRequisitionerUnit.getText().trim();
                    if(!getDataFromServer()){   
                        // If unitNumber is not valid the it shows the error message, and set the prevoius value
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_NUMBER));
                        
                        if(unitNoTemp.equalsIgnoreCase(EMPTY_STRING) || unitNoTemp == null){
                            subcontractDetailForm.txtRequisitionerUnit.setText(EMPTY_STRING);
                            subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                        }
                        if(unitNameTemp.equalsIgnoreCase(EMPTY_STRING) || unitNameTemp == null){
                            subcontractDetailForm.txtRequisitionerUnit.setText(unitNoTemp);
                            subcontractDetailForm.lblRequistionerUnitDesc.setText(EMPTY_STRING);
                            
                        }if(!unitNoTemp.equalsIgnoreCase(EMPTY_STRING) && !unitNameTemp.equalsIgnoreCase(EMPTY_STRING)){
                            subcontractDetailForm.txtRequisitionerUnit.setText( unitNoTemp); 
                            subcontractDetailForm.lblRequistionerUnitDesc.setText(unitNameTemp);
                        }
                        setRequestFocusInThread(subcontractDetailForm.txtVendorNumber);                        
                        return;
                    }
                    subcontractDetailForm.txtRequisitionerUnit.setText(reqNumber);
                    subcontractDetailForm.lblRequistionerUnitDesc.setText(reqName);
                }else{
                    reqNumber = EMPTY_STRING;
                    reqName= EMPTY_STRING;
                    setRequestFocusInThread(subcontractDetailForm.txtVendorNumber);
                }
            }
            //Added for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
       }
        //coeusqa-1563 start
        private void getRolodexFullName(String siteInvEnteredName) {

                RequesterBean requesterBean = new RequesterBean();
                ResponderBean responderBean = new ResponderBean();
                requesterBean.setFunctionType('x');
                requesterBean.setDataObject(siteInvEnteredName);
                
                String connectTo = connectionURL + "/unitServlet";
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
                comm.setRequest(requesterBean);
                comm.send();
                
                responderBean = comm.getResponse();
                if(responderBean!=null) {
                     if(responderBean.isSuccessfulResponse()) {
                         String finalData = (String)responderBean.getDataObject();
                          if(finalData.equalsIgnoreCase("TOO MANY")) {
                             CoeusOptionPane.showErrorDialog("You have entered invalid Site Investigator");
                          }
                     }
                     else{
                             Exception ex = responderBean.getException();
                             ex.printStackTrace();
                         }
                }
        
        }

    }
    //coeusqa-1563 end
    
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /* to clean all the objects*/
    public void cleanUp(){
        cvSubcontract = null;
        mdiForm = null;
        subContractBean = null;
        subcontractDetailForm = null;
        coeusMessageResources = null;
        dateUtils = null;
        simpleDateFormat = null;
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        if(source.equals(subcontractDetailForm.txtRequisitioner)){
            int clickCount = mouseEvent.getClickCount();
            if(clickCount != 2)return;
            String requisitionerName = subcontractDetailForm.txtRequisitioner.getText();
            if(requisitionerName == null || requisitionerName.trim().length() == 0 ){
                return;
            }else {
                //Bug Fix: Pass the person id to get the person details Start 5
                /*PersonInfoFormBean personInfoFormBean = coeusUtils.getPersonInfoID(requisitionerName);
                if( personInfoFormBean == null ){
                    return ;
                }*/
                //Bug Fix: Pass the person id to get the person details End 5
                
                String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                
                //Bug Fix: Pass the person id to get the person details Start 6
                /*Bug Fix*/
                /*to get the person details by the person id instead of the person name*/
                //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,TypeConstants.DISPLAY_MODE);
                new PersonDetailForm(subContractBean.getRequisitionerId(),loginUserName,TypeConstants.DISPLAY_MODE);
                //Bug Fix: Pass the person id to get the person details End 6
                
            }
        }else if(source.equals(subcontractDetailForm.txtSubcontractor)){
            
            try{
                int clickCount = mouseEvent.getClickCount();
                if(clickCount == 2) {
                    DetailForm frmOrgDetailForm;
                    frmOrgDetailForm = new DetailForm(TypeConstants.DISPLAY_MODE , organizationId);
                    if (frmOrgDetailForm != null) {
                        frmOrgDetailForm.showDialogForm(mdiForm);
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }else if(source.equals(subcontractDetailForm.txtRequisitionerUnit)){
            try{
            int clickCount = mouseEvent.getClickCount();
            if(clickCount == 2) {
                String unitNumber = reqNumber;
                //UnitDetailForm unitDetailForm = new UnitDetailForm(unitNumber , TypeConstants.DISPLAY_MODE); 
                UnitDetailForm unitDetailForm = new UnitDetailForm(unitNumber , functionType);
                unitDetailForm.setControlsEnabled(false);
                unitDetailForm.showUnitForm(mdiForm);
                
            }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }//coeusqa-1528 start
        else if(source.equals(subcontractDetailForm.txtSiteInvestigatorName)){
            try{
                String siteInvestigator = subcontractDetailForm.txtSiteInvestigatorName.getText();
                if(siteInvestigator != null && !(CoeusGuiConstants.EMPTY_STRING.equals(siteInvestigator)) ){
                    int clickCount = mouseEvent.getClickCount();
                    if(clickCount == 2) {
                        rolodexID = subContractBean.getSiteInvestigator() + CoeusGuiConstants.EMPTY_STRING;
                        RolodexMaintenanceDetailForm frmRolodex = new RolodexMaintenanceDetailForm('V',rolodexID);
                        frmRolodex.showForm(mdiForm,CoeusGuiConstants.TITLE_ROLODEX,true);
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
       //coeusqa-1528 end
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    public boolean isSaveRequired(){
        return modified;
    }
    
    /**
     * Getter for property refreshRequired
     */
    public boolean isRefreshRequired() {
        boolean retValue;
        retValue = super.isRefreshRequired();
        return retValue;
    }
    
    /**
     * To refresh the GUI with new Data
     */
    public void refresh() {
        if (isRefreshRequired()) {
            setFormData(null);
            setRefreshRequired(false);
        }
    }
    
    public void setDefaultFocus(){
        setRequestFocusInThread(subcontractDetailForm.cmbStatus);
    }
    
    //Bug fix for person Validation on focus lost Start 3
    private PersonInfoFormBean checkPerson(String personName){
        
        PersonInfoFormBean personInfo = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType('J');
        requesterBean.setDataObject(personName);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/unitServlet";
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                personInfo =(PersonInfoFormBean)responderBean.getDataObject();
                if(personInfo.getPersonID() == null){
                    CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("investigator_exceptionCode.1007"));
                    personInfo = null;
                }else if(personInfo.getPersonID().equalsIgnoreCase("TOO_MANY")){
                    CoeusOptionPane.showErrorDialog
                        ("\""+personName+"\""+" " +coeusMessageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                    personInfo = null;
                }
            }else{
                Exception ex = responderBean.getException();
                ex.printStackTrace();
            }
        }
        return personInfo;
    }

    // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
    /**
     * Method to get Cost Types
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void displayCostTypes() throws CoeusException {
        CoeusVector cvCostTypes = queryEngine.getDetails(queryKey,KeyConstants.SUBCONTRACT_COST_TYPES);
        if(cvCostTypes != null && !cvCostTypes.isEmpty()){
            TypeSelectionLookUp typeSelectionLookUp = new TypeSelectionLookUp("Select a Cost Type",ListSelectionModel.SINGLE_SELECTION);
            typeSelectionLookUp.setFormData(cvCostTypes);
            typeSelectionLookUp.display();
            CoeusTypeBean coeusTypeBean = typeSelectionLookUp.getSelectedType();
            if(coeusTypeBean != null){
                costTypeCode = coeusTypeBean.getTypeCode();
                subcontractDetailForm.txtCostType.setText(coeusTypeBean.getTypeDescription());
            }
        }else{
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(NO_COST_TYPE));
        }
        
    }
    // Added for COEUSQA-1412 Subcontract Module changes - Change - End
 
}
