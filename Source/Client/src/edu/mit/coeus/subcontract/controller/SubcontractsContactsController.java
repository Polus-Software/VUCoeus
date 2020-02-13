/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * SubcontractsContactsController.java
 *
 * Created on September 3, 2004, 2:21 PM
 */

/* PMD check performed, and commented unused imports and variables on 22-DEC-2011
 * by Bharati
 */

package edu.mit.coeus.subcontract.controller;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.BevelBorder;

import edu.mit.coeus.subcontract.gui.*;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.exception.*;
// JM 5-2-2014 person information
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.vanderbilt.coeus.subcontract.controller.SubcontractPersonController;
// JM END

/**
 *
 * @author  surekhan
 */
public class SubcontractsContactsController extends SubcontractController implements ActionListener , ListSelectionListener , MouseListener{
    
    private SubcontractContactsForm subcontractContactsForm;
    
    private QueryEngine queryEngine;
    
    private CoeusMessageResources coeusMessageResources;
    
    private SubcontractContactsBean subcontractContactsBean;
    
    private SubcontractContactDetailsBean subcontractContactDetailsBean;
    
    private CoeusAppletMDIForm mdiForm;
    
    private CoeusVector cvContactsData;
    
    private CoeusVector cvContactType = null;
    
    private CoeusVector cvDeletedData = new CoeusVector();
    
    private CoeusVector cvData = new CoeusVector();
    
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    
    private static final String ADD = "Add";
    
    private static final String MODIFY = "Modify";
    
    private static final int HAND_ICON_COLUMN = 0;
    
    private static final int CONTACTTYPE = 1;
    
    private static final int NAME_COLUMN = 2;
    
    private static final String EMPTY_STRING = "";
    
    private int initialSize = 0;
    
    private boolean modified;
    
    private JComboBox cmbContactType;
    
    private ContactListTableModel contactListTableModel;
    
    private ContactListEditor contactListEditor;
    
    private ContactListRenderer contactListRenderer;
    
    private ListSelectionModel contactsSelectionModel;
    
    /*Please select a row to modify*/
    private static final String MODIFY_MESSAGE = "subcontractContacts_exceptionCode.1251";
    
    /*Please select a row to delete*/
    private static final String DEL_MESSAGE = "subcontractContacts_exceptionCode.1252";
    
    /*Please select a contact type*/
    private static final String CONTACT_MESSAGE = "subcontractContacts_exceptionCode.1253";
    
    /*Address is mandatory for a contact, Please associate an address with the contact type.*/
    private static final String ADDRESS_MSG = "subcontractContacts_exceptionCode.1254";
    
    /*Duplicate contact information*/
    private static final String DUPLICATE_INFO = "subcontractContacts_exceptionCode.1255";
    
    /*You must have atleast one contact before saving the current subcontract*/
    private static final String SAVE_MSG  ="subcontractContacts_exceptionCode.1256";
    
    private int rowId;
    
    /* JM 2-27-2015 modifications to allow tab access if user has only this right */
    private boolean userHasModify = false;
    private boolean userHasCreate = false;
    /* JM END */
    
      /** Creates a new instance of SubcontractsContactsController */
    public SubcontractsContactsController(SubContractBean subContractBean , char functionType) {
        super(subContractBean);
        this.mdiForm = mdiForm;
        queryEngine = QueryEngine.getInstance();
        subcontractContactsForm = new SubcontractContactsForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        contactListTableModel = new ContactListTableModel();
        contactListEditor = new ContactListEditor();
        contactListRenderer = new ContactListRenderer();
        postInitComponents();
        registerComponents();
        
		/* JM 2-27-2015 no access if not modifier */
        userHasModify = subContractBean.getHasModify();
        userHasCreate = subContractBean.getHasCreate();
		if (!userHasModify && !userHasCreate) {
			functionType = DISPLAY_SUBCONTRACT;
		}
		/* JM END */  
        
        setFunctionType(functionType);
        setTableEditors();
        display();
        setFormData(null);
        setTableKeyTraversal();
    }
    
    /*Instantiates instance objects*/
    private void postInitComponents(){
        try {
            cvContactType = queryEngine.getDetails(queryKey, KeyConstants.AWARD_CONTACT_TYPE );
            NotEquals neNull = new NotEquals("description", null);
            NotEquals neEmpty = new NotEquals("description", " ");
            And emptyAndNull  = new And(neNull, neEmpty);
            cvContactType = cvContactType.filter(emptyAndNull);
            cvContactType.sort("description");
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            subcontractContactsForm.tblContactList.setBackground(PANEL_BACKGROUND_COLOR);
            subcontractContactsForm.btnAdd.setEnabled(false);
            subcontractContactsForm.btnDelete.setEnabled(false);
            subcontractContactsForm.btnModify.setEnabled(false);
        }
    }
    
    /** An overridden method of the controller
     * @return subcontractContactsForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return subcontractContactsForm;
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
        
        subcontractContactsForm.tblContactList.setModel(contactListTableModel);
        subcontractContactsForm.tblContactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsSelectionModel = subcontractContactsForm.tblContactList.getSelectionModel();
        contactsSelectionModel.addListSelectionListener(this);
        subcontractContactsForm.tblContactList.setSelectionModel(contactsSelectionModel);
        subcontractContactsForm.tblContactList.addMouseListener(this);
        
        java.awt.Component[] components = {
        subcontractContactsForm.btnAdd,
        subcontractContactsForm.btnModify,
        subcontractContactsForm.btnDelete,
        };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        subcontractContactsForm.setFocusTraversalPolicy(traversePolicy);
        subcontractContactsForm.setFocusCycleRoot(true);
        
        subcontractContactsForm.btnAdd.addActionListener(this);
        subcontractContactsForm.btnModify.addActionListener(this);
        subcontractContactsForm.btnDelete.addActionListener(this);
        
    }
    
    /*saves the form data*/
    public void saveFormData() {
        contactListEditor.stopCellEditing();
        
        CoeusVector dataVector =  new CoeusVector();
        try{
            if(cvDeletedData!= null && cvDeletedData.size() > 0){
                dataVector.addAll(cvDeletedData);
                cvDeletedData.clear();
            }
            
            if(cvContactsData!= null && cvContactsData.size() > 0){
                dataVector.addAll(cvContactsData);
            }
            
            for(int index= 0; index < dataVector.size(); index++){
                SubcontractContactDetailsBean dataBean = (SubcontractContactDetailsBean) dataVector.get(index);
                
                if(dataBean.getAcType()!= null){
                    if(dataBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                        //Modified for case#2201 start 1
                        queryEngine.delete(queryKey, dataBean);
                        int rolId = dataBean.getRolodexId();
                        int conCode = dataBean.getContactTypeCode();
                        dataBean.setSubContractCode(subContractBean.getSubContractCode());
                        dataBean.setSequenceNumber(subContractBean.getSequenceNumber());
                        dataBean.setRolodexId(dataBean.getAw_RolodexId());
                        dataBean.setContactTypeCode(dataBean.getAw_ContactTypeCode());
                        //Modified for case#2201 end 1
                        rowId = getExistingMaxId() + 1;
                        dataBean.setRowId(rowId);
                        dataBean.setAcType(TypeConstants.INSERT_RECORD);
                        dataBean.setRolodexId(rolId);
                        dataBean.setContactTypeCode(conCode);
                        queryEngine.insert(queryKey, dataBean);
                    }else if(dataBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                        //Modified for case#2201 start 2
//                        dataBean.setSubContractCode(subContractBean.getSubContractCode());
//                        dataBean.setSequenceNumber(subContractBean.getSequenceNumber());
                        //Modified for case#2201 end 2
                        queryEngine.delete(queryKey, dataBean);
                    }else if(dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        dataBean.setSubContractCode(subContractBean.getSubContractCode());
                        dataBean.setSequenceNumber(subContractBean.getSequenceNumber());
                        if( dataBean.getRowId() == 0 ){
                            rowId = getExistingMaxId() + 1;
                            dataBean.setRowId(rowId);
                        }
                        queryEngine.insert(queryKey, dataBean);
                    }
                }
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    
    
    /*To get the Max of the vector*/
    private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, SubcontractContactDetailsBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                SubcontractContactsBean bean = (SubcontractContactsBean)cvExistingRecords.get(0);
                rowId = bean.getRowId();
                
            }else{
                rowId = 0;
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        return rowId;
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        try{
            
            
            cvContactsData = queryEngine.executeQuery(queryKey,
            SubcontractContactDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            initialSize = cvContactsData.size();
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        contactListTableModel.setData(cvContactsData);
        contactListTableModel.fireTableDataChanged();
        if( cvContactsData != null && cvContactsData.size() > 0){
            subcontractContactsForm.tblContactList.setRowSelectionInterval(0,0);
            subcontractContactDetailsBean = (SubcontractContactDetailsBean)cvContactsData.get(0);
            setData(subcontractContactDetailsBean);
        }else if(cvContactsData == null) {
            setData(null);
        }
    }
    
    /*to set the contact details data*/
    public void setData(SubcontractContactDetailsBean subcontractContactDetailsBean) {
        CoeusVector cvContactDetails = new CoeusVector();
        String name;
        if( subcontractContactDetailsBean == null ) {
            resetData();
            return ;
        }
        String firstName = subcontractContactDetailsBean.getFirstName() == null ?EMPTY_STRING:subcontractContactDetailsBean.getFirstName() ;
        if ( firstName.length() > 0) {
            String suffix = (subcontractContactDetailsBean.getSuffix() == null ?EMPTY_STRING:subcontractContactDetailsBean.getSuffix());
            String prefix = (subcontractContactDetailsBean.getPrefix() == null ?EMPTY_STRING:subcontractContactDetailsBean.getPrefix());
            String middleName = (subcontractContactDetailsBean.getMiddleName() == null ?EMPTY_STRING:subcontractContactDetailsBean.getMiddleName());
            // JM 5-6-2014 update name display for person records
            if (subcontractContactDetailsBean.getPersonId() == null) {
            name = (subcontractContactDetailsBean.getLastName()+" "+
                    suffix+", "+ prefix+" "+ firstName+" "+
                    middleName).trim();
            }
            else {
	            name = (subcontractContactDetailsBean.getLastName()+","+
	                    prefix+" "+ firstName+" "+
	                    middleName).trim();            	
            }
            // JM END
            subcontractContactsForm.awardContactDetailsForm1.txtName.setText(name);
        } else {
            subcontractContactsForm.awardContactDetailsForm1.txtName.setText(subcontractContactDetailsBean.getOrganization());
        }
        
        
        subcontractContactsForm.awardContactDetailsForm1.txtAddress1.setText(subcontractContactDetailsBean.getAddress1());
        subcontractContactsForm.awardContactDetailsForm1.txtAddress2.setText(subcontractContactDetailsBean.getAddress2());
        subcontractContactsForm.awardContactDetailsForm1.txtAddress3.setText(subcontractContactDetailsBean.getAddress3());
        subcontractContactsForm.awardContactDetailsForm1.txtCity.setText(subcontractContactDetailsBean.getCity());
        subcontractContactsForm.awardContactDetailsForm1.lblSponsorName.setText(subcontractContactDetailsBean.getSponsorName());
        subcontractContactsForm.awardContactDetailsForm1.txtCountry.setText(subcontractContactDetailsBean.getCountryName());
        subcontractContactsForm.awardContactDetailsForm1.txtCounty.setText(subcontractContactDetailsBean.getCounty());
        subcontractContactsForm.awardContactDetailsForm1.txtEMail.setText(subcontractContactDetailsBean.getEmailAddress());
        subcontractContactsForm.awardContactDetailsForm1.txtFax.setText(subcontractContactDetailsBean.getFaxNumber());
        subcontractContactsForm.awardContactDetailsForm1.txtOrganisation.setText(subcontractContactDetailsBean.getOrganization());
        subcontractContactsForm.awardContactDetailsForm1.txtPhone.setText(subcontractContactDetailsBean.getPhoneNumber());
        subcontractContactsForm.awardContactDetailsForm1.txtPostalCode.setText(subcontractContactDetailsBean.getPostalCode());
        // JM 3-25-2014 updated for person ID
        if(subcontractContactDetailsBean.getRolodexId() == -1 || subcontractContactDetailsBean.getRolodexId() == 0){
            subcontractContactsForm.awardContactDetailsForm1.txtRolodexID.setText(subcontractContactDetailsBean.getPersonId());
        }else {
            subcontractContactsForm.awardContactDetailsForm1.txtRolodexID.setText(EMPTY_STRING + subcontractContactDetailsBean.getRolodexId());
        }
        subcontractContactsForm.awardContactDetailsForm1.txtState.setText(subcontractContactDetailsBean.getStateName());
        subcontractContactsForm.awardContactDetailsForm1.txtSponsor.setText(subcontractContactDetailsBean.getSponsorCode());
        subcontractContactsForm.awardContactDetailsForm1.txtArComments.setText(subcontractContactDetailsBean.getComments());
        subcontractContactsForm.awardContactDetailsForm1.txtArComments.setCaretPosition(0);
        //Added for COEUSQA-3450 : Subcontract Module missing timestamp for Contacts and save error upon save of added contacts - start
        //Added for COEUSQA-3514 : Subcontract Lock Deleted by Administrator and other save issue - start
        if(subcontractContactDetailsBean.getUpdateTimestamp() != null){
            String lastUpdate = CoeusDateFormat.format(subcontractContactDetailsBean.getUpdateTimestamp().toString());
            subcontractContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(subcontractContactDetailsBean.getUpdateUser());
            subcontractContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(lastUpdate);
        }
        //Added for COEUSQA-3514 : Subcontract Lock Deleted by Administrator and other save issue - end
        //Added for COEUSQA-3450 : Subcontract Module missing timestamp for Contacts and save error upon save of added contacts - end
    }
    
    /*to reset the form data to empty*/
    private void resetData() {
        subcontractContactsForm.awardContactDetailsForm1.txtName.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtRolodexID.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtSponsor.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtOrganisation.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.lblSponsorName.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtAddress1.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtAddress2.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtAddress3.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtCity.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtCountry.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtCounty.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtEMail.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtFax.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtPhone.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtState.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtArComments.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtPostalCode.setText(EMPTY_STRING);
        //Added for COEUSQA-3450 : Subcontract Module missing timestamp for Contacts and save error upon save of added contacts - start
        subcontractContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(EMPTY_STRING);
        subcontractContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(EMPTY_STRING);
        //Added for COEUSQA-3450 : Subcontract Module missing timestamp for Contacts and save error upon save of added contacts - end
        
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        Equals contactType;
        Equals roloID;
        contactListEditor.stopCellEditing();
        for(int index = 0;index < cvContactsData.size();index++){
            SubcontractContactDetailsBean contactsBean = (SubcontractContactDetailsBean)cvContactsData.get(index);
            if(contactsBean.getContactTypeCode() == 0 ) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CONTACT_MESSAGE));
                subcontractContactsForm.tblContactList.clearSelection();
                setRequestFocusInCloseoutThread(index, CONTACTTYPE);
//                subcontractContactsForm.tblContactList.setRowSelectionInterval(index, index);
//                subcontractContactsForm.tblContactList.setColumnSelectionInterval(1,1);
//                subcontractContactsForm.tblContactList .scrollRectToVisible(
//                subcontractContactsForm.tblContactList.getCellRect(index,1, true));
//                subcontractContactsForm.tblContactList.editCellAt(index,CONTACTTYPE);
//                subcontractContactsForm.tblContactList.getEditorComponent().requestFocusInWindow();
//                focusFlag = true;
                return false;
            }
            
            /* JM check for person id too */
            if(contactsBean.getRolodexId() == -1 || 
            		(contactsBean.getRolodexId() == 0 ) && (contactsBean.getPersonId() == null)) {
                subcontractContactsForm.tblContactList.setRowSelectionInterval(index, index);
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ADDRESS_MSG));
                return false;
            }
        }
        // JM 5-7-2014 each role can appear only once; person / rolodex can be in multiple roles
        /*
        if(cvContactsData!= null && cvContactsData.size() > 0){
            for(int index = 0;index <cvContactsData.size();index++){
                SubcontractContactDetailsBean contactsCodeBean = (SubcontractContactDetailsBean)cvContactsData.get(index);
                contactType = new Equals("contactTypeCode",new Integer(contactsCodeBean.getContactTypeCode()));
                roloID = new Equals("rolodexId", new Integer(contactsCodeBean.getRolodexId()));
                And AwContact = new And(contactType , roloID);
                CoeusVector cvFilterd = cvContactsData.filter(AwContact);
                
                if(cvFilterd!= null && cvFilterd.size() > 1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                    return false;
                }
            }
        } */
        
        if(cvContactsData!= null && cvContactsData.size() > 0){
            for(int index = 0;index <cvContactsData.size();index++){
                SubcontractContactDetailsBean contactsCodeBean = (SubcontractContactDetailsBean)cvContactsData.get(index);
                contactType = new Equals("contactTypeCode",new Integer(contactsCodeBean.getContactTypeCode()));
                CoeusVector cvFilterd = cvContactsData.filter(contactType);
                    
                if(cvFilterd!= null && cvFilterd.size() > 1) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                    
                    return false;
                }
                
            }
            
        }
        // JM END
        
        
       if(subcontractContactsForm.tblContactList.getRowCount() == 0){
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SAVE_MSG));
                return false;
            }
        return true;
        
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(subcontractContactsForm.btnAdd)){
            showRolodex(ADD);
        }else if(source.equals(subcontractContactsForm.btnModify)){
        	// JM 3-21-2014 person search
            showPerson(ADD);
        }else if(source.equals(subcontractContactsForm.btnDelete)){
            performDeleteAction();
        }
    }
    
    // JM 3-21-2014 show Person search screen
    private void showPerson(String mode) {
        CoeusSearch proposalSearch = null;
        Vector vecPerson = null;
        HashMap personData = null;
        
        int selRow = -1;
        ComboBoxBean typeComboBean = new ComboBoxBean();
        String strPerson = null;
        SubcontractContactDetailsBean contactDetailsBean = null;
        
        try{
            if(mode.equals(ADD)) {
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                CoeusGuiConstants.PERSON_SEARCH,
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                proposalSearch.showSearchWindow();
                vecPerson = proposalSearch.getMultipleSelectedRows();
            }else {
                selRow = subcontractContactsForm.tblContactList.getSelectedRow();
                if(selRow == -1){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(MODIFY_MESSAGE));
                    return;
                } 
                else {
                    
                    typeComboBean =(ComboBoxBean) subcontractContactsForm.tblContactList.getValueAt(selRow, CONTACTTYPE);
                    strPerson = (String)subcontractContactsForm.tblContactList.getValueAt(selRow,NAME_COLUMN);
                    SubcontractContactDetailsBean subcontractContactDetailsBean  = (SubcontractContactDetailsBean)cvContactsData.get(selRow);
                    
                    contactDetailsBean = (SubcontractContactDetailsBean)ObjectCloner.deepCopy(subcontractContactDetailsBean);
                    
                    proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                    CoeusGuiConstants.PERSON_SEARCH,1);
                    proposalSearch.showSearchWindow();
                    personData = proposalSearch.getSelectedRow();
                }
            }
        }catch( Exception err ){
            err.printStackTrace();
        }

        if ( vecPerson != null ){

        	int indx = 0;
            for( indx = 0; indx < vecPerson.size(); indx++ ){
                personData = (HashMap) vecPerson.get(indx) ;
                
                if( personData == null || personData.isEmpty() ){
                    continue;
                }
                setPersonData(mode, personData);
            }
            
            subcontractContactsForm.tblContactList.setRowSelectionInterval(initialSize, initialSize);
            initialSize += indx;
        }

        selRow = subcontractContactsForm.tblContactList.getSelectedRow();
        if( selRow != -1 ){
            subcontractContactsForm.tblContactList.clearSelection();
            subcontractContactsForm.tblContactList.scrollRectToVisible(
            subcontractContactsForm.tblContactList.getCellRect(selRow,1, true));
            subcontractContactsForm.tblContactList.requestFocusInWindow();
            subcontractContactsForm.tblContactList.editCellAt(selRow,CONTACTTYPE);
            subcontractContactsForm.tblContactList.getEditorComponent().requestFocusInWindow();
            subcontractContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
            
        }
    }
    // JM END
    
    // JM 3-21-2014 set Person data to form
    private void setPersonData(String mode, HashMap personData ){
        String personId = checkForNull(personData.get("PERSON_ID"));
        SubcontractPersonController personController = new SubcontractPersonController();
    	CoeusVector personInfo = personController.getPersonInfo(personId);
    	DepartmentPersonFormBean personBean = (DepartmentPersonFormBean) personInfo.get(0);

        SubcontractContactDetailsBean contactDetails = new SubcontractContactDetailsBean();
        contactDetails.setRolodexId(0);
        contactDetails.setPersonId(personId);
        contactDetails.setFirstName(personBean.getFirstName());
        contactDetails.setLastName(personBean.getLastName());
        contactDetails.setPrefix(EMPTY_STRING);
        contactDetails.setSuffix(EMPTY_STRING);
        contactDetails.setMiddleName(personBean.getMiddleName());
        contactDetails.setTitle(personBean.getPrimaryTitle());
        contactDetails.setSponsorCode(EMPTY_STRING);
        contactDetails.setSponsorName(EMPTY_STRING);
        contactDetails.setOrganization(EMPTY_STRING);
        contactDetails.setAddress1(personBean.getAddress1());
        contactDetails.setAddress2(personBean.getAddress2());
        contactDetails.setAddress3(personBean.getAddress3());
        contactDetails.setCounty(personBean.getCounty());
        contactDetails.setCity(personBean.getCity());
        contactDetails.setStateName(personBean.getState());
        contactDetails.setPostalCode(personBean.getPostalCode());
        contactDetails.setCountryName(personBean.getCountryCode());
        contactDetails.setEmailAddress(personBean.getEmailAddress());
        contactDetails.setPhoneNumber(personBean.getOfficePhone());
        contactDetails.setFaxNumber(personBean.getFaxNumber());
        contactDetails.setComments(EMPTY_STRING);
    	
        if( mode.equals(ADD) ){
            contactDetails.setAcType( TypeConstants.INSERT_RECORD );
            contactDetails.setContactTypeCode(0);
            cvContactsData.add(contactDetails);
            contactListEditor.cmbContactType.requestFocusInWindow();
            contactListTableModel.fireTableRowsInserted(cvContactsData.size(), cvContactsData.size());
        }
    }
    // JM END
    
    /*this method is to show the search screens on the basis of the add or modify mode*/
    private void showRolodex(String mode ) {
        CoeusSearch proposalSearch = null;
        Vector vecRolodex = null;
        HashMap rolodexData = null;
        
        
        int selRow = -1;
        ComboBoxBean typeComboBean = new ComboBoxBean();
        String strPerson = null;
        SubcontractContactDetailsBean contactDetailsBean = null;
        
        try{
            if(mode.equals(ADD)) {
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                CoeusGuiConstants.ROLODEX_SEARCH,
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                proposalSearch.showSearchWindow();
                vecRolodex = proposalSearch.getMultipleSelectedRows();
            }else {
                selRow = subcontractContactsForm.tblContactList.getSelectedRow();
                if(selRow == -1){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(MODIFY_MESSAGE));
                    return;
                } else {
                    
                    
                    typeComboBean =(ComboBoxBean) subcontractContactsForm.tblContactList.getValueAt(selRow, CONTACTTYPE);
                    strPerson = (String)subcontractContactsForm.tblContactList.getValueAt(selRow,NAME_COLUMN);
                    SubcontractContactDetailsBean subcontractContactDetailsBean  = (SubcontractContactDetailsBean)cvContactsData.get(selRow);
                    
                    contactDetailsBean = (SubcontractContactDetailsBean)ObjectCloner.deepCopy(subcontractContactDetailsBean);
                    
                    
                    proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                    CoeusGuiConstants.ROLODEX_SEARCH,1);
                    proposalSearch.showSearchWindow();
                    rolodexData = proposalSearch.getSelectedRow();
                }
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
        
        if( mode.equals(MODIFY) ){
            if( rolodexData != null ){
                setRolodexData(mode, rolodexData);
                
                
                SubcontractContactDetailsBean detailsBean = (SubcontractContactDetailsBean) cvContactsData.get(selRow);
                
                HashMap hmData = new HashMap();
                hmData.put(AwardConstants.PERSON_NAME,strPerson);
                hmData.put(AwardConstants.CONTACT_TYPE,typeComboBean);
                hmData.put(AwardConstants.BEAN_BEFORE_MODIFICATION,contactDetailsBean);
                hmData.put(AwardConstants.BEAN_AFTER_MODIFICATION,detailsBean);
                
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setObject(hmData);
                beanEvent.setBean(new SubcontractContactDetailsBean());
                fireBeanUpdated(beanEvent);
                
            }
        }else{
            if ( vecRolodex != null ){
                int indx = 0;
                for( indx = 0; indx < vecRolodex.size(); indx++ ){
                    
                    rolodexData = (HashMap)vecRolodex.get( indx ) ;
                    
                    if( rolodexData == null || rolodexData.isEmpty() ){
                        continue;
                    }
                    setRolodexData(mode, rolodexData);
                }
                
                subcontractContactsForm.tblContactList.setRowSelectionInterval(initialSize, initialSize);
                initialSize += indx;
            }
        }
        selRow = subcontractContactsForm.tblContactList.getSelectedRow();
        if( selRow != -1 ){
            subcontractContactsForm.tblContactList.clearSelection();
            subcontractContactsForm.tblContactList .scrollRectToVisible(
            subcontractContactsForm.tblContactList.getCellRect(selRow,1, true));
            subcontractContactsForm.tblContactList.requestFocusInWindow();
            subcontractContactsForm.tblContactList.editCellAt(selRow,CONTACTTYPE);
            subcontractContactsForm.tblContactList.getEditorComponent().requestFocusInWindow();
            subcontractContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
            
        }
    }
    
    /*to set the data to the form*/
    private void setRolodexData(String mode, HashMap rolodexData ){
        String rolodexID = checkForNull(rolodexData.get( "ROLODEX_ID" ));
        String firstName = checkForNull(rolodexData.get( "FIRST_NAME" ));
        String lastName = checkForNull(rolodexData.get( "LAST_NAME" ));
        String middleName = checkForNull(rolodexData.get( "MIDDLE_NAME" ));
        String preffix = checkForNull(rolodexData.get( "PREFIX" ));
        String suffix = checkForNull(rolodexData.get( "SUFFIX" ));
        String title = checkForNull(rolodexData.get( "TITLE" ));
        String sponsorCode = checkForNull(rolodexData.get( "SPONSOR_CODE" ));
        String sponsorName = checkForNull(rolodexData.get( "SPONSOR_NAME" ));
        String organization = checkForNull(rolodexData.get( "ORGANIZATION" ));
        String address1 = checkForNull(rolodexData.get( "ADDRESS_LINE_1" ));
        String address2 = checkForNull(rolodexData.get( "ADDRESS_LINE_2" ));
        String address3 = checkForNull(rolodexData.get( "ADDRESS_LINE_3" ));
        String county = checkForNull(rolodexData.get( "COUNTY" ));
        String city = checkForNull(rolodexData.get( "CITY" ));
        String state = checkForNull(rolodexData.get( "STATE_DESCRIPTION" ));
        String postalCode = checkForNull(rolodexData.get( "POSTAL_CODE" ));
        String countryName = checkForNull(rolodexData.get( "COUNTRY_NAME" ));
        String emailAddress = checkForNull(rolodexData.get( "EMAIL_ADDRESS" ));
        String faxNumber = checkForNull(rolodexData.get( "FAX_NUMBER" ));
        String phoneNumber = checkForNull(rolodexData.get( "PHONE_NUMBER" ));
        String comments = checkForNull(rolodexData.get( "COMMENTS" ));
        String rolodexName = null;
        if ( firstName.length() > 0) {
            rolodexName = ( lastName +" "+","+ preffix
            +""+firstName +" "+ middleName ).trim();
        } else {
            rolodexName = checkForNull(rolodexData.get("ORGANIZATION"));
        }
        // JM 3-25-2014 update for person ID
        String personId = checkForNull(rolodexData.get("PERSON_ID"));
        // JM END
        
        SubcontractContactDetailsBean contactDetails = new SubcontractContactDetailsBean();
        contactDetails.setRolodexId(Integer.parseInt(rolodexID));
        contactDetails.setFirstName(firstName);
        contactDetails.setLastName(lastName);
        contactDetails.setPrefix(preffix);
        contactDetails.setSuffix(suffix);
        contactDetails.setMiddleName(middleName);
        contactDetails.setTitle(title);
        contactDetails.setSponsorCode(sponsorCode);
        contactDetails.setSponsorName(sponsorName);
        contactDetails.setOrganization(organization);
        contactDetails.setAddress1(address1);
        contactDetails.setAddress2(address2);
        contactDetails.setAddress3(address3);
        contactDetails.setCounty(county);
        contactDetails.setCity(city);
        contactDetails.setStateName(state);
        contactDetails.setPostalCode(postalCode);
        contactDetails.setCountryName(countryName);
        contactDetails.setEmailAddress(emailAddress);
        contactDetails.setPhoneNumber(phoneNumber);
        contactDetails.setFaxNumber(faxNumber);
        contactDetails.setComments(comments);
        // JM 3-25-2014 update for person ID
        contactDetails.setPersonId(personId);
        // JM END
        if( mode.equals(ADD) ){
            contactDetails.setAcType( TypeConstants.INSERT_RECORD );
            contactDetails.setContactTypeCode(0);
            cvContactsData.add(contactDetails);
            contactListEditor.cmbContactType.requestFocusInWindow();
            contactListTableModel.fireTableRowsInserted(cvContactsData.size(), cvContactsData.size());
        }else{

            int selRow = subcontractContactsForm.tblContactList.getSelectedRow();
            
            SubcontractContactDetailsBean bean = (SubcontractContactDetailsBean)cvContactsData.get(selRow);
//            bean.setRolodexId(Integer.parseInt(rolodexID));
//            bean.setFirstName(firstName);
//            bean.setLastName(lastName);
//            bean.setPrefix(preffix);
//            bean.setSuffix(suffix);
//            bean.setMiddleName(middleName);
//            bean.setTitle(title);
//            bean.setSponsorCode(sponsorCode);
//            bean.setSponsorName(sponsorName);
//            bean.setOrganization(organization);
//            bean.setAddress1(address1);
//            bean.setAddress2(address2);
//            bean.setAddress3(address3);
//            bean.setCounty(county);
//            bean.setCity(city);
//            bean.setStateName(state);
//            bean.setPostalCode(postalCode);
//            bean.setCountryName(countryName);
//            bean.setEmailAddress(emailAddress);
//            bean.setPhoneNumber(phoneNumber);
//            bean.setFaxNumber(faxNumber);
//            bean.setComments(comments);
//            bean.setAcType( TypeConstants.UPDATE_RECORD );
            bean.setAcType( TypeConstants.DELETE_RECORD);
            
            cvDeletedData.addElement(bean);
            contactDetails.setAcType(TypeConstants.INSERT_RECORD);
            cvContactsData.set(selRow, contactDetails);
            
            contactListTableModel.fireTableCellUpdated(selRow, NAME_COLUMN);
            subcontractContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
        }
    }
    
    
    /*to check for the null value*/
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
        
    }
    
    
    /*to delete the row on the click of the delete button*/
    private void performDeleteAction(){
        int selectedRow = subcontractContactsForm.tblContactList.getSelectedRow();
        if( selectedRow == -1) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DEL_MESSAGE));
            return;
        }else{
            int selectedOption
            = showDeleteConfirmMessage(
            "Are you sure you want to remove this row?");
            if( selectedOption == JOptionPane.YES_OPTION ){
                SubcontractContactDetailsBean deletedBean = (SubcontractContactDetailsBean)cvContactsData.elementAt(selectedRow);
                deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDeletedData.addElement(deletedBean);
                cvContactsData.removeElementAt(selectedRow);
                contactListTableModel.fireTableRowsDeleted(selectedRow,selectedRow);
                initialSize--;
            }
            
            int newRowCount = subcontractContactsForm.tblContactList.getRowCount();
            int newSelRow = 0;
            if(newRowCount >0){
                if(newRowCount > selectedRow){
                    subcontractContactsForm.tblContactList.setRowSelectionInterval(selectedRow,
                    selectedRow);
                    newSelRow = selectedRow;
                }else{
                    subcontractContactsForm.tblContactList.setRowSelectionInterval(
                    newRowCount - 1,newRowCount - 1);
                    newSelRow = newRowCount - 1;
                }
            }else{
                subcontractContactsForm.awardContactDetailsForm1.setFormData(null);
            }
        }
    }
    
    
    /*to show the delete message*/
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        return selectedOption;
    }
    
    /*to set the sizes to the columns and set the editors and the renderers*/
    private void setTableEditors(){
        
        subcontractContactsForm.tblContactList.setRowHeight(22);
        subcontractContactsForm.tblContactList.setShowHorizontalLines(false);
        subcontractContactsForm.tblContactList.setShowVerticalLines(false);
        subcontractContactsForm.tblContactList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = subcontractContactsForm.tblContactList.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        subcontractContactsForm.tblContactList.setOpaque(false);
        subcontractContactsForm.tblContactList.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn column = subcontractContactsForm.tblContactList.getColumnModel().getColumn(HAND_ICON_COLUMN);
        
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setResizable(true);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = subcontractContactsForm.tblContactList.getColumnModel().getColumn(1);
        column.setPreferredWidth(365);
        column.setResizable(true);
       // column.setMinWidth(50);
        column.setCellRenderer(contactListRenderer);
        column.setCellEditor(contactListEditor);
        header.setReorderingAllowed(false);
        
        column = subcontractContactsForm.tblContactList.getColumnModel().getColumn(2);
        column.setPreferredWidth(415);
        column.setResizable(true);
        column.setCellRenderer(contactListRenderer);
        header.setReorderingAllowed(false);
    }
    
    /*Table model*/
    class ContactListTableModel extends AbstractTableModel{
        private CoeusVector cvContactsData;
        
        String colNames[] = {"","Contact Type", "Name/Organization"};
        Class[] colTypes = new Class [] {Object.class , String.class, String.class};
        
        /*to set the data to the table*/
        public void setData(CoeusVector cvContactsData) {
            this.cvContactsData = cvContactsData;
        }
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            SubcontractContactDetailsBean contactDetailsBean = (SubcontractContactDetailsBean)cvContactsData.get(rowIndex);
            switch(columnIndex) {
                case HAND_ICON_COLUMN:
                    return EMPTY_STRING;
                case CONTACTTYPE:
                    int contactCode = contactDetailsBean.getContactTypeCode();
                    CoeusVector filteredVector = cvContactType.filter(
                    new Equals("code", ""+contactCode));
                    if(filteredVector!=null && filteredVector.size() > 0){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                        return comboBoxBean;
                    }else{
                        return new ComboBoxBean("","");
                    }
                case NAME_COLUMN:
                    String rolodexName;
                    String firstName = (contactDetailsBean.getFirstName() == null ?EMPTY_STRING:contactDetailsBean.getFirstName());
                    if ( firstName.length() > 0) {
                        String suffix = (contactDetailsBean.getSuffix() == null ?EMPTY_STRING:contactDetailsBean.getSuffix());
                        String prefix = (contactDetailsBean.getPrefix() == null ?EMPTY_STRING:contactDetailsBean.getPrefix());
                        String middleName = (contactDetailsBean.getMiddleName() == null ?EMPTY_STRING:contactDetailsBean.getMiddleName());
                        //rolodexName = (contactDetailsBean.getLastName() +" "+
                        //suffix+", "+ prefix+" "+ firstName+" "+
                        //middleName).trim();
                        // JM 5-6-2014 update name display for person records
                        if (contactDetailsBean.getPersonId() == null) {
                        rolodexName = (contactDetailsBean.getLastName() +" "+
                        suffix+", "+ prefix+" "+ firstName+" "+
                        middleName).trim();
                        }
                        else {
                        	rolodexName = (contactDetailsBean.getLastName()+","+
            	                    prefix+" "+ firstName+" "+
            	                    middleName).trim();            	
                        }
                        // JM END
                    } else {
                        rolodexName = checkForNull(contactDetailsBean.getOrganization());
                    }
                    return rolodexName;
            }
            return EMPTY_STRING;
            
        }
        
        
        /* to set the value to the particular cell*/
        public void setValueAt(Object value, int row, int column){
            //Modified for COEUSDEV-335 : Subcontract invoices need to be in reverse chronological order on Amount Released tab - Start
//            if(cvContactsData == null) return;
            if(cvContactsData == null || cvContactsData.size() <= row) return;
            //COEUSDEV-335 : End
            SubcontractContactDetailsBean subcontractContactDetailsBean = (SubcontractContactDetailsBean)cvContactsData.get(row);
            
            switch(column){
                case CONTACTTYPE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) return ;
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvContactType.filter(new Equals("description", value.toString())).get(0);
                    int contactCode = Integer.parseInt(comboBoxBean.getCode());
                    
                    if( contactCode != subcontractContactDetailsBean.getContactTypeCode() ){
                        subcontractContactDetailsBean.setContactTypeCode(contactCode);
                        if( subcontractContactDetailsBean.getAcType() == null ){
                            subcontractContactDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        modified = true;
                    }
                    break;
                case NAME_COLUMN:
                    if(value != subcontractContactDetailsBean.getFirstName().trim()){
                        if(subcontractContactDetailsBean.getFirstName()!= null &&
                        subcontractContactDetailsBean.getFirstName().equals(value.toString())) {
                            subcontractContactDetailsBean.setFirstName(value.toString().trim());
                        } else if( subcontractContactDetailsBean.getFirstName().trim().equals(EMPTY_STRING)) {
                            subcontractContactDetailsBean.setOrganization(value.toString().trim());
                        }
                        if( subcontractContactDetailsBean.getAcType() == null ){
                            subcontractContactDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        modified = true;
                    }
                    
            }
            
            
        }
        
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int column) {
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            }else {
                if(column == 1) {
                    return true;
                }else{
                    return false;
                }
            }
        }
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /*returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /*to get the row count*/
        public int getRowCount() {
            if( cvContactsData == null ) return 0;
            return cvContactsData.size();
            
        }
    }
    
    /* Table Cell Editor for the contacts table*/
    class ContactListEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbContactType;
        private boolean populated = false;
        private int column;
        ContactListEditor() {
            cmbContactType = new JComboBox();
        }
        /*to populete the combo box*/
        private void populateCombo() {
             int size = cvContactType.size();
            ComboBoxBean comboBoxBean;
            cmbContactType.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            for(int index = 0; index < size; index++) {
                comboBoxBean = (ComboBoxBean)cvContactType.get(index);
                cmbContactType.addItem(comboBoxBean);
            }
            //cmbContactType.setModel(new DefaultComboBoxModel(cvContactType));
        }
        
         /* returns the cellEditor component*/
       public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case CONTACTTYPE:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    
//                    if(comboBoxBean.getDescription() == null || comboBoxBean.getDescription().equals(EMPTY_STRING)) {
//                        ComboBoxBean selBean = (ComboBoxBean)cvContactType.get(0);
//                        cmbContactType.setSelectedItem(selBean);
//                        return cmbContactType;
//                    }
                    cmbContactType.setSelectedItem(value);
                    return cmbContactType;
            }
            return null;
        }
        
       /* Returns the CellEditor value*/
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case CONTACTTYPE:
                    return cmbContactType.getSelectedItem();
            }
            return cmbContactType;
        }
        public int getClickCountToStart(){
            return 1;
        }
    }
    
    /*Renderer for the table columns*/
    class ContactListRenderer extends DefaultTableCellRenderer{
        public ContactListRenderer() {
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
        }
        
        /* returns renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column) {
                case HAND_ICON_COLUMN:
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                case CONTACTTYPE:
                    setText(value.toString());
                    if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        setBackground(PANEL_BACKGROUND_COLOR);
                    }else{
                        setBackground(Color.WHITE);
                    }
                    return this;
                case NAME_COLUMN:
                    setText(value.toString());
                    setBackground(PANEL_BACKGROUND_COLOR);
                    return this;
                    
            }
            return null;
        }
        
        public void setFunctionType(char functionType) {
            if( functionType == TypeConstants.DISPLAY_MODE ) {
                cmbContactType.setBackground(PANEL_BACKGROUND_COLOR);
            }
        }
        
    }
    
    /* to clean all the objects*/
    public void cleanUp(){
        cvContactType = null;
        cvContactsData = null;
        cvData = null;
        cvDeletedData = null;
        subContractBean = null;
        subcontractContactDetailsBean = null;
        subcontractContactsBean = null;
        subcontractContactsForm = null;
        mdiForm = null;
        contactListEditor = null;
        coeusMessageResources = null;
        contactListRenderer = null;
        contactListTableModel = null;
        contactsSelectionModel = null;
        
    }
    
    /*to set the data on the value changed in the form*/
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            int selectedRow = subcontractContactsForm.tblContactList.getSelectedRow();
            if (selectedRow != -1) {
                SubcontractContactDetailsBean subcontractContactDetailsBean =
                (SubcontractContactDetailsBean)cvContactsData.get(selectedRow);
                setData(subcontractContactDetailsBean);
            }
        }
    }
    
    /*the action performed on the double click*/
    public void mouseClicked(MouseEvent e) {
        if(getFunctionType()!= TypeConstants.DISPLAY_MODE ){
            
            if(e.getClickCount() == 2){
                showRolodex(MODIFY);
            }
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
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
    
    // This method will provide the key travrsal for the table cells
    // It specifies the tab and shift tab order.
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = subcontractContactsForm.tblContactList.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = subcontractContactsForm.tblContactList.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    column += 1;
                    
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    
                    if (row == rowCount) {
                        row = 0;
                    }
                    
                    // Back to where we started, get out.
                    
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        subcontractContactsForm.tblContactList.getActionMap().put(im.get(tab), tabAction);
        
        
        
        
        // for the shift+tab action
        
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = subcontractContactsForm.tblContactList.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                
                while (! table.isCellEditable(row, column) ) {
                    
                    column -= 1;
                    
                    if (column <= 0) {
                        column = 5;
                        row -=1;
                    }
                    
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    
                    // Back to where we started, get out.
                    if (row == table.getSelectedRow()
                    && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                
                table.changeSelection(row, column, false, false);
            }
        };
        subcontractContactsForm.tblContactList.getActionMap().put(im.get(shiftTab), tabAction1);
        
        
    }
 
    private void setRequestFocusInCloseoutThread(final int selrow , final int selcol){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            subcontractContactsForm.tblContactList.requestFocusInWindow();
            subcontractContactsForm.tblContactList.changeSelection( selrow, selcol, true, false);
            subcontractContactsForm.tblContactList.setRowSelectionInterval(selrow, selrow);
            subcontractContactsForm.tblContactList .scrollRectToVisible(
            subcontractContactsForm.tblContactList.getCellRect(selrow,1, true));
            subcontractContactsForm.tblContactList.editCellAt(selrow,CONTACTTYPE);
            subcontractContactsForm.tblContactList.getEditorComponent().requestFocusInWindow();
        }
        });
    }
    

}
