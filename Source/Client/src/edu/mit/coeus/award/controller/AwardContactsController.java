/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AwardContactsController.java
 *
 * Created on April 28, 2004, 10:02 AM
 */

package edu.mit.coeus.award.controller;


/**
 *
 * @author  surekhan
 */
import edu.mit.coeus.award.gui.AwardContactsSyncForm;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.BevelBorder;

import edu.mit.coeus.award.AwardConstants;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.award.gui.AwardContactsForm;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.gui.CoeusFontFactory;


public class AwardContactsController extends AwardController implements
ActionListener, ListSelectionListener, MouseListener, BeanUpdatedListener {
    
    /* AwardContactForm Instance*/
    private AwardContactsForm awardContactsForm;
    
    /* MDI Form Instance */
    private CoeusAppletMDIForm mdiForm;
    
    private QueryEngine queryEngine;
    /* AwardContactBean */
    private AwardContactBean awardContactBean;
    
    private AwardContactDetailsBean awardContactDetailsBean;
    
    private CoeusVector cvContactsData;
    
    private CoeusVector cvDeletedData = new CoeusVector();
    
    private CoeusVector cvData = new CoeusVector();
    
    /*MessageResource*/
    private CoeusMessageResources coeusMessageResources;
    private ContactListTableModel contactListTableModel;
    private ContactListEditor contactListEditor;
    private ContactListRenderer contactListRenderer;
    private static final int HAND_ICON_COLUMN = 0;
    private static final int CONTACTTYPE = 1;
    private static final int NAME_COLUMN = 2;
    private static final String EMPTY_STRING = "";
    private ListSelectionModel contactsSelectionModel;
    private static final String ADD = "Add";
    private static final String MODIFY = "Modify";
    private boolean modified = false;
    private CoeusVector cvContactType = null;
    private JComboBox cmbContactType;
    
    /**Please select a row to delete
     */
    private static final String DEL_MESSAGE = "awardContacts_exceptionCode.1204";
    
    /** Please select a row to modify
     */
    private static final String MODIFY_MESSAGE = "awardContacts_exceptionCode.1205" ;
    
    /** Please select a contacttype
     */
    private static final String CONTACT_MESSAGE = "awardContacts_exceptionCode.1206";
    
    /** Duplicate contct information
     */
    private static final String DUPLICATE_INFO = "awardContacts_exceptionCode.1207";
    
    /** Are you sure you want to syncronize Award Contacts information with the selected Template Contacts information?
     */
    private static final String CONFIRM_SYNC = "awardContacts_exceptionCode.1203";
    
    /**Address is mandatory for a contact, Please associate an address with the contact type.
     **/
    private static final String ADDRESS_MSG = "awardContacts_exceptionCode.1208";
    
    /** You must have atleast one contact before saving the current award
     **/
    private static final String SAVE_MSG  ="awardContacts_exceptionCode.1209";
    private int initialSize = 0;
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    
    private int rowId;
    
    //Bug Fix:Performance Issue (Out of memory) Start 1
    private JScrollPane jscrPn;
    //Bug Fix:Performance Issue (Out of memory) End 1
    /*Creates the new instance of the awardcontactscontroller*/
    public AwardContactsController(AwardBaseBean awardBaseBean,
    CoeusAppletMDIForm mdiForm, char functionType) {
        super(awardBaseBean);
        contactListTableModel = new ContactListTableModel();
        contactListRenderer =  new ContactListRenderer();
        contactListEditor = new ContactListEditor();
        awardContactsForm = new AwardContactsForm();
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        jscrPn = new JScrollPane(awardContactsForm);
        // JM 4-10-2012 add listener to pass control to outer pane for scrolling
        jscrPn.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
            	jscrPn.getParent().dispatchEvent(e);
            }
        });
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        this.mdiForm = mdiForm;
        if(functionType == NEW_AWARD) {
            AwardHeaderBean awardHeaderBean = new AwardHeaderBean();
            awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
            awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        }
        postInitComponents();
        registerComponents();
        setFunctionType(functionType);
        setTableEditors();
        display();
        setFormData(awardBaseBean);
        getExistingMaxId();
        
    }
    
    /*Instantiates instance objects*/
    private void postInitComponents() {
        
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        try {
            cvContactType = queryEngine.getDetails(queryKey, KeyConstants.CONTACT_TYPES );
            NotEquals neNull = new NotEquals("description", null);
            NotEquals neEmpty = new NotEquals("description", " ");
            And emptyAndNull  = new And(neNull, neEmpty);
            cvContactType = cvContactType.filter(emptyAndNull);
            cvContactType.sort("description");
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    public void display() {
        
    }
    
    /*formats the components*/
    public void formatFields() {
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardContactsForm.btnAdd.setEnabled(false);
            awardContactsForm.btnDelete.setEnabled(false);
            awardContactsForm.btnModify.setEnabled(false);
            awardContactsForm.btnSync.setEnabled(false);
            //Added with case 2796: Sync to parent.
            awardContactsForm.btnAddSync.setEnabled(false);
            awardContactsForm.btnModifySync.setEnabled(false);
            awardContactsForm.btnDelSync.setEnabled(false);
            //2796 End
            //Bug Fix : 1026
            //awardContactsForm.tblContactList.setEnabled(false);
            //Bug Fix : 1026
            awardContactsForm.tblContactList.setBackground(PANEL_BACKGROUND_COLOR);
        //Added with case 2796: Sync to parent.
        }else if(!awardBaseBean.isParent()){
            awardContactsForm.btnAddSync.setEnabled(false);
            awardContactsForm.btnModifySync.setEnabled(false);
            awardContactsForm.btnDelSync.setEnabled(false);
        }
        //2796 End
    }
    
    /** returns controlled UI.
     * @return Controlled UI
     */
    public java.awt.Component getControlledUI() {
        
        //Bug Fix:Performance Issue (Out of memory) Start 2
        //return awardContactsForm;
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - START
        //jscrPn = new JScrollPane(awardContactsForm);
        //JIRA Case COEUSDEV-160, COEUSDEV-177 - END
        return jscrPn;
        //Bug Fix:Performance Issue (Out of memory) End 2
    }
    
    /** returns form data.
     * @return returns form data.
     */
    public Object getFormData() {
        return null;
    }
    
    /*registers the components and listeners*/
    public void registerComponents() {
        
        awardContactsForm.tblContactList.setModel(contactListTableModel);
        awardContactsForm.tblContactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsSelectionModel = awardContactsForm.tblContactList.getSelectionModel();
        contactsSelectionModel.addListSelectionListener(this);
        awardContactsForm.tblContactList.setSelectionModel(contactsSelectionModel);
        awardContactsForm.tblContactList.addMouseListener(this);
        java.awt.Component[] components = {awardContactsForm.tblContactList,
        awardContactsForm.btnAdd,
        awardContactsForm.btnModify,
        awardContactsForm.btnDelete,
        //Added with case 2796: Sync to parent.
        awardContactsForm.btnAddSync,
        awardContactsForm.btnModifySync,
        awardContactsForm.btnDelSync,
        //2796 End
        awardContactsForm.btnSync,
        awardContactsForm.scrPnContactDetails
        };
        
        awardContactsForm.btnAdd.addActionListener(this);
        awardContactsForm.btnModify.addActionListener(this);
        awardContactsForm.btnDelete.addActionListener(this);
        awardContactsForm.btnSync.addActionListener(this);
        //Added with case 2796: Sync to parent.
        awardContactsForm.btnAddSync.addActionListener(this);
        awardContactsForm.btnModifySync.addActionListener(this);
        awardContactsForm.btnDelSync.addActionListener(this);
        //2796 End
        addBeanUpdatedListener(this, AwardDetailsBean.class);
        // awardContactsForm.tblContactList.getTableHeader().getColumnModel().addColumnModelListener(this);
    }
    
    /*saves the form data*/
    public void saveFormData() {
      //added for the bug fix i.e for adding the row and deleting the same 
      //and saving (array index out of range)start
      contactListEditor.stopCellEditing();
      //bug fix end.
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
                AwardContactBean dataBean = (AwardContactBean) dataVector.get(index);
                
                if(dataBean.getAcType()!= null){
                    if(dataBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                        queryEngine.delete(queryKey, dataBean);
                        dataBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                        dataBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                        rowId = getExistingMaxId() + 1;
                        dataBean.setRowId(rowId);
                        dataBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, dataBean);
                    }else if(dataBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                        queryEngine.delete(queryKey, dataBean);
                    }else if(dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        dataBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                        dataBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
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
    
    /** sets data to form.
     * @param data data to be set.
     */ 
    public void setFormData(Object data) {
        
        try{
            cvContactsData = queryEngine.executeQuery(queryKey,
            AwardContactDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            initialSize = cvContactsData.size();
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        contactListTableModel.setData(cvContactsData);
        contactListTableModel.fireTableDataChanged();
        if( cvContactsData != null && cvContactsData.size() > 0){
            awardContactsForm.tblContactList.setRowSelectionInterval(0,0);
            awardContactDetailsBean = (AwardContactDetailsBean)cvContactsData.get(0);
            awardContactsForm.awardContactDetailsForm1.setFormData( awardContactDetailsBean);
        // Case# 3128:Award template sync and contacts
//        }else if(cvContactsData == null) {
        }else {  
            awardContactsForm.awardContactDetailsForm1.setFormData(null);
           
        }
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //Disables the Last Update and Update User components
        awardContactsForm.pnlUpdateDetails.setVisible(false);
        awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setVisible(false);
        awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setVisible(false);
        awardContactsForm.awardContactDetailsForm1.lblLastUpdate.setVisible(false);
        awardContactsForm.awardContactDetailsForm1.lblUpdateUser.setVisible(false);
        awardContactsForm.scrPnContactList.setPreferredSize(new java.awt.Dimension(600, 300));
        //COEUSQA-1456 : End
    }
    
    /** validates the form.
     * returns false if validation fails.
     * else returns true.
     * @throws CoeusUIException if any exception occurs / validation fails.
     * @return returns false if validation fails.
     * else returns true.
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        Equals contactType;
        Equals roloID;
       contactListEditor.stopCellEditing();
        for(int index = 0;index < cvContactsData.size();index++){
            AwardContactDetailsBean contactsBean = (AwardContactDetailsBean)cvContactsData.get(index);
            if(contactsBean.getContactTypeCode() == 0 ) {
                awardContactsForm.tblContactList.setRowSelectionInterval(index, index);
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CONTACT_MESSAGE));
                
                return false;
            }
            
            if(contactsBean.getRolodexId() == -1 || contactsBean.getRolodexId() == 0 ){
                awardContactsForm.tblContactList.setRowSelectionInterval(index, index);
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ADDRESS_MSG));
                return false;
            }
        }
        if(cvContactsData!= null && cvContactsData.size() > 0){
            for(int index = 0;index <cvContactsData.size();index++){
                AwardContactDetailsBean contactsCodeBean = (AwardContactDetailsBean)cvContactsData.get(index);
                contactType = new Equals("contactTypeCode",new Integer(contactsCodeBean.getContactTypeCode()));
                roloID = new Equals("rolodexId", new Integer(contactsCodeBean.getRolodexId()));
                And AwContact = new And(contactType , roloID);
                CoeusVector cvFilterd = cvContactsData.filter(AwContact);
                
                if(cvFilterd!= null && cvFilterd.size() > 1) {
                    
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_INFO));
                    
                    return false;
                }
            }
            
        }
        
        try{
            cvData = queryEngine.executeQuery(queryKey,
            AwardContactDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvData == null || cvData.size() == 0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SAVE_MSG));
                return false;
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        return true;
    }
    
    /** listens to action events.
     * @param actionEvent actionEvent
     */ 
    public void actionPerformed(ActionEvent actionEvent) {
        contactListEditor.stopCellEditing();
        Object source = actionEvent.getSource();
        if( source.equals(awardContactsForm.btnAdd)) {
            showRolodex(ADD,false);
        } else if (source.equals(awardContactsForm.btnDelete)){
            performDeleteAction(false);
        } else if (source.equals(awardContactsForm.btnModify)){
            showRolodex(MODIFY,false);
        }else if (source.equals(awardContactsForm.btnSync)) {
            syncAwardContacts();
        //Added with case 2796: Sync To Parent
        }else if (source.equals(awardContactsForm.btnAddSync)) {
            performSync(AwardConstants.ADD_SYNC);
        }else if (source.equals(awardContactsForm.btnModifySync)) {
            performSync(AwardConstants.MODIFY_SYNC);
        }else if (source.equals(awardContactsForm.btnDelSync)) {
            if(awardContactsForm.tblContactList.getRowCount()<=1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SAVE_MSG));
            }else if( awardContactsForm.tblContactList.getSelectedRow() == -1) {
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DEL_MESSAGE));
            }else{
                //COEUSDEV 253: Add Fabe and cs to sync screen
                //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//                HashMap target = showSyncTargetWindow(true,AwardConstants.DELETE_SYNC);
                HashMap target = showSyncTargetWindow(true,AwardConstants.CONTACTS_SYNC,AwardConstants.DELETE_SYNC);//COEUSDEV-416 : End 
                if(target != null){
                    performDeleteAction(true);
                    saveFormData();
                    if(setSyncFlags(AwardContactDetailsBean.class,true,target)){
                        saveAndSyncAward(AwardContactDetailsBean.class);
                    }
                }
            }
        }
        //2796 End
    }
    //Added with case 2796: Sync To Parent
    private void performSync(char mode){
        
        int selRow = awardContactsForm.tblContactList.getSelectedRow();
        if(mode == AwardConstants.MODIFY_SYNC && selRow == -1){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(MODIFY_MESSAGE));
            return;
        }else{
            //Modified for COEUSDEV-416 : Award Sync to Children - Display proper error message when not syncing because the award is not saved
//            if(validateBeforeSync()){
            if(validateBeforeSync(AwardConstants.CONTACTS_SYNC,mode)){//COEUSDEV-416 : End
                AwardContactsSyncForm syncForm = new AwardContactsSyncForm(mode,selRow,this);
                //COEUSDEV 253: Add Fabe and cs to sync screen
                HashMap target = syncForm.display();
                if(target!=null){
                    saveFormData();
                    if(setSyncFlags(AwardContactDetailsBean.class,true,target)){
                        setFormData(null);
                        saveAndSyncAward(AwardContactDetailsBean.class);
                    }
                }
            }
        }
    }
    //2796 End
    /** Sync the award contacts with the template contacts
     */
    private void syncAwardContacts(){
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey(CONFIRM_SYNC),
        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        
        switch( option ){
            case (CoeusOptionPane.SELECTION_YES):
                //Call the Sync Contacts of the AwardController
                if ( syncContacts(EMPTY, getTemplateCode()) ){
                    setFormData(null);
                    setSaveRequired(true);
                }
        }
    }
    
    /*to display the search window*/
    //Method modified with case 2796:Sync to parent : Start
    
    private void showRolodex(String mode, boolean syncRequired ) {
        
        CoeusSearch proposalSearch = null;
        int selRow  = -1;
        
        try{
            //Add Mode
            if(mode.equals(ADD)) {
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                CoeusGuiConstants.ROLODEX_SEARCH,
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                proposalSearch.showSearchWindow();
                Vector vecRolodex = proposalSearch.getMultipleSelectedRows();
                 if ( vecRolodex != null ){
                    addRolodex(vecRolodex,syncRequired);
                 }
            //Modify Mode
            }else {
                selRow = awardContactsForm.tblContactList.getSelectedRow();
                if(selRow == -1){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(MODIFY_MESSAGE));
                    return;
                } else {
                    proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                    CoeusGuiConstants.ROLODEX_SEARCH,1);
                    proposalSearch.showSearchWindow();
                    HashMap rolodexData = proposalSearch.getSelectedRow();
                    modifyRolodex(rolodexData,syncRequired);
                }
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
        selRow = awardContactsForm.tblContactList.getSelectedRow();
        if( selRow != -1 ){
            awardContactsForm.tblContactList.clearSelection();
            awardContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
        }
    }
    
    public void modifyRolodex(HashMap rolodexData,boolean syncRequired){
      
        ComboBoxBean typeComboBean = new ComboBoxBean();
        String strPerson = null;
        AwardContactDetailsBean contactDetailsBean = null;
        int selRow = awardContactsForm.tblContactList.getSelectedRow();
        try{
            typeComboBean =(ComboBoxBean) awardContactsForm.tblContactList.getValueAt(selRow, CONTACTTYPE);
            strPerson = (String)awardContactsForm.tblContactList.getValueAt(selRow,NAME_COLUMN);
            AwardContactDetailsBean awardContactDetailsBean  = (AwardContactDetailsBean)cvContactsData.get(selRow);
            contactDetailsBean = (AwardContactDetailsBean)ObjectCloner.deepCopy(awardContactDetailsBean);
        }catch( Exception err ){
            err.printStackTrace();
        }
        
        if( rolodexData != null ){
            setRolodexData(MODIFY, rolodexData,syncRequired);
            
            AwardContactDetailsBean detailsBean = (AwardContactDetailsBean) cvContactsData.get(selRow);
            HashMap hmData = new HashMap();
            hmData.put(AwardConstants.PERSON_NAME,strPerson);
            hmData.put(AwardConstants.CONTACT_TYPE,typeComboBean);
            hmData.put(AwardConstants.BEAN_BEFORE_MODIFICATION,contactDetailsBean);
            hmData.put(AwardConstants.BEAN_AFTER_MODIFICATION,detailsBean);
            
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setSource(this);
            beanEvent.setObject(hmData);
            beanEvent.setBean(new AwardContactDetailsBean());
            fireBeanUpdated(beanEvent);
        }
    }
    
    public void addRolodex(Vector vecRolodex,boolean syncRequired){
        HashMap rolodexData = null;
         if ( vecRolodex != null ){
                int indx = 0;
                for( indx = 0; indx < vecRolodex.size(); indx++ ){
                    
                    rolodexData = (HashMap)vecRolodex.get( indx ) ;
                    
                    if( rolodexData == null || rolodexData.isEmpty() ){
                        continue;
                    }
                    setRolodexData(ADD, rolodexData,syncRequired);
                }
             
                awardContactsForm.tblContactList.setRowSelectionInterval(initialSize, initialSize);
                initialSize += indx;
            }
    }
    
    //2796: Sync to Parent End
    
    /*to set the data to the form*/
    private void setRolodexData(String mode, HashMap rolodexData,boolean syncRequired ){
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
//        String rolodexName = null;
//        if ( firstName.length() > 0) {
//            rolodexName = ( lastName +" "+","+ preffix
//            +""+firstName +" "+ middleName ).trim();
//        } else {
//            rolodexName = checkForNull(rolodexData.get("ORGANIZATION"));
//        }
        
        AwardContactDetailsBean contactDetails = new AwardContactDetailsBean();
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
        if( mode.equals(ADD) ){
            contactDetails.setAcType( TypeConstants.INSERT_RECORD );
            contactDetails.setSyncRequired(syncRequired);//2796
            cvContactsData.add(contactDetails);
            awardContactsForm.tblContactList.editCellAt(cvContactsData.size() - 1, CONTACTTYPE);
            contactListEditor.cmbContactType.requestFocusInWindow();
            contactListTableModel.fireTableRowsInserted(cvContactsData.size(), cvContactsData.size());
        }else{
            int selRow = awardContactsForm.tblContactList.getSelectedRow();
           
            AwardContactDetailsBean bean = (AwardContactDetailsBean)cvContactsData.get(selRow);
            bean.setRolodexId(Integer.parseInt(rolodexID));
            bean.setFirstName(firstName);
            bean.setLastName(lastName);
            bean.setPrefix(preffix);
            bean.setSuffix(suffix);
            bean.setMiddleName(middleName);
            bean.setTitle(title);
            bean.setSponsorCode(sponsorCode);
            bean.setSponsorName(sponsorName);
            bean.setOrganization(organization);
            bean.setAddress1(address1);
            bean.setAddress2(address2);
            bean.setAddress3(address3);
            bean.setCounty(county);
            bean.setCity(city);
            bean.setStateName(state);
            bean.setPostalCode(postalCode);
            bean.setCountryName(countryName);
            bean.setEmailAddress(emailAddress);
            bean.setPhoneNumber(phoneNumber);
            bean.setFaxNumber(faxNumber);
            bean.setComments(comments);
            bean.setAcType( TypeConstants.UPDATE_RECORD );
            bean.setSyncRequired(syncRequired);//2796
            contactListTableModel.fireTableCellUpdated(selRow, NAME_COLUMN);
            awardContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
        }
    }
    
    
    /*to check for the null value*/
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
       
    }
    
   
    /*to delete the row*/
    private void performDeleteAction(boolean syncRequired) {
        int selectedRow = awardContactsForm.tblContactList.getSelectedRow();
        if(selectedRow == -1) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DEL_MESSAGE));
            return;
        }else{
            int selectedOption
            = showDeleteConfirmMessage(
            "Are you sure you want to remove this row?");
            if( selectedOption == JOptionPane.YES_OPTION ){
                AwardContactDetailsBean deletedBean = (AwardContactDetailsBean)cvContactsData.elementAt(selectedRow);
                deletedBean.setSyncRequired(syncRequired);
                deletedBean.setAcType(TypeConstants.DELETE_RECORD);
                cvDeletedData.addElement(deletedBean);
                cvContactsData.removeElementAt(selectedRow);
                contactListTableModel.fireTableRowsDeleted(selectedRow,selectedRow);
                initialSize--;
            }
            
            int newRowCount = awardContactsForm.tblContactList.getRowCount();
            int newSelRow = 0;
            if(newRowCount >0){
                if(newRowCount > selectedRow){
                    awardContactsForm.tblContactList.setRowSelectionInterval(selectedRow,
                    selectedRow);
                    newSelRow = selectedRow;
                }else{
                    awardContactsForm.tblContactList.setRowSelectionInterval(
                    newRowCount - 1,newRowCount - 1);
                    newSelRow = newRowCount - 1;
                }
            }else{
                awardContactsForm.awardContactDetailsForm1.setFormData(null);
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
    
    public boolean isDataChanged() {
        contactListEditor.stopCellEditing();
        return false;
    }
    
    /*to set the sizes to the columns and set the editors and the renderers*/
    private void setTableEditors(){
        
        awardContactsForm.tblContactList.setRowHeight(22);
        awardContactsForm.tblContactList.setShowHorizontalLines(false);
        awardContactsForm.tblContactList.setShowVerticalLines(false);
        
        JTableHeader header = awardContactsForm.tblContactList.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        header.setReorderingAllowed(false);
        
        awardContactsForm.tblContactList.setOpaque(false);
        awardContactsForm.tblContactList.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        TableColumn column = awardContactsForm.tblContactList.getColumnModel().getColumn(HAND_ICON_COLUMN);
        
        column.setMaxWidth(30);
        column.setMinWidth(30);
        column.setPreferredWidth(30);
        column.setCellRenderer(new IconRenderer());
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        
        column = awardContactsForm.tblContactList.getColumnModel().getColumn(1);
        column.setPreferredWidth(50);
        column.setMinWidth(50);
        column.setCellRenderer(contactListRenderer);
        column.setCellEditor(contactListEditor);
        header.setReorderingAllowed(false);
        
        column = awardContactsForm.tblContactList.getColumnModel().getColumn(2);
        column.setPreferredWidth(80);
        column.setPreferredWidth(80);
        column.setCellRenderer(contactListRenderer);
        header.setReorderingAllowed(false);
    }
    
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( !listSelectionEvent.getValueIsAdjusting() ){
            int selectedRow = awardContactsForm.tblContactList.getSelectedRow();
            if (selectedRow != -1) {
                AwardContactDetailsBean awardContactDetailsBean =
                (AwardContactDetailsBean)cvContactsData.get(selectedRow);
                awardContactsForm.awardContactDetailsForm1.setFormData(awardContactDetailsBean);
            }
        }
    }
    
    /*To get the Max of the vector*/
    private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
         try{
            cvExistingRecords = queryEngine.getDetails(queryKey, AwardContactDetailsBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                AwardContactBean bean = (AwardContactBean)cvExistingRecords.get(0);
                //                maxRowId = bean.getRowId();
                rowId = bean.getRowId();
                
            }else{
                rowId = 0;
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        return rowId;
    }
    
    public void mouseClicked(MouseEvent e) {
        if(getFunctionType()!= TypeConstants.DISPLAY_MODE ){
            
            if(e.getClickCount() == 2){
                showRolodex(MODIFY,false);
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
    
    public void beanUpdated(BeanEvent beanEvent) {
        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
                setRefreshRequired(true);
            }
        }
    }
    
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        setFormData(awardBaseBean);
        setRefreshRequired(false);
    }
    
    /*Renderer for the table columns*/
    class ContactListRenderer extends DefaultTableCellRenderer{
        public ContactListRenderer() {
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED, Color.white,Color.lightGray, Color.black, Color.lightGray);
            setBorder(bevelBorder);
        }
        
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
    
    /*Table model*/
    class ContactListTableModel extends AbstractTableModel{
        private CoeusVector cvContactsData;
        
        String colNames[] = {"","Contact Type", "Name/Organization"};
        Class[] colTypes = new Class [] {Object.class , String.class, String.class};
        
        public void setData(CoeusVector cvContactsData) {
            this.cvContactsData = cvContactsData;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            AwardContactDetailsBean contactDetailsBean = (AwardContactDetailsBean)cvContactsData.get(rowIndex);
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
                        //Bug Fix 1505 Start
                        rolodexName = (contactDetailsBean.getLastName() +" "+
                        suffix+", "+ prefix+" "+ firstName+" "+
                        middleName).trim();
                        //Bug Fix 1505 End
                        
                    } else {
                        rolodexName = checkForNull(contactDetailsBean.getOrganization());
                    }
                    return rolodexName;
            }
            return EMPTY_STRING;
            
        }
        
        
        
        public void setValueAt(Object value, int row, int column){
            // Case# 3128:Award template sync and contacts 
//            if(cvContactsData == null) return;
            if(cvContactsData == null || cvContactsData.size() <= row ) return;
            AwardContactDetailsBean awardContactDetailsBean = (AwardContactDetailsBean)cvContactsData.get(row);
            
            switch(column){
                case CONTACTTYPE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) return ;
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvContactType.filter(new Equals("description", value.toString())).get(0);
                    int contactCode = Integer.parseInt(comboBoxBean.getCode());
                    
                    if( contactCode != awardContactDetailsBean.getContactTypeCode() ){
                        awardContactDetailsBean.setContactTypeCode(contactCode);
                        awardContactDetailsBean.setContactTypeDescription(comboBoxBean.getDescription());
                        if( awardContactDetailsBean.getAcType() == null ){
                            awardContactDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        modified = true;
                    }
                    //contactListEditor.stopCellEditing();
                    break;
                case NAME_COLUMN:
                    if(awardContactDetailsBean.getFirstName()!= null &&
                    awardContactDetailsBean.getFirstName().equals(value.toString())) {
                        awardContactDetailsBean.setFirstName(value.toString().trim());
                    } else if( awardContactDetailsBean.getFirstName().trim().equals(EMPTY_STRING)) {
                        awardContactDetailsBean.setOrganization(value.toString().trim());
                    }
                    if( awardContactDetailsBean.getAcType() == null ){
                        awardContactDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    modified = true;
            }
            
            
        }
        
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
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        
        
        public int getRowCount() {
            if( cvContactsData == null ) return 0;
            return cvContactsData.size();
            
        }
    }
    
    class ContactListEditor extends AbstractCellEditor implements TableCellEditor{
        private JComboBox cmbContactType;
        private boolean populated = false;
        private int column;
        ContactListEditor() {
            cmbContactType = new JComboBox();
        }
        private void populateCombo() {
            cmbContactType.setModel(new DefaultComboBoxModel(cvContactType));
        }
        
        
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case CONTACTTYPE:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    
                    if(comboBoxBean.getDescription() == null || comboBoxBean.getDescription().equals(EMPTY_STRING)) {
                        ComboBoxBean selBean = (ComboBoxBean)cvContactType.get(0);
                        cmbContactType.setSelectedItem(selBean);
                        return cmbContactType;
                    }
                    cmbContactType.setSelectedItem(value);
                    return cmbContactType;
            }
            return null;
        }
        
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
    
    /** Method to clean all objects */
    public void cleanUp() {
        //Bug Fix:Performance Issue (Out of memory) Start 3
        jscrPn.remove(awardContactsForm);
        jscrPn = null;
        //Bug Fix:Performance Issue (Out of memory) End 3
        
        awardContactsForm = null;
        mdiForm = null;
        queryEngine = null;
        awardContactBean = null;
        awardContactDetailsBean = null;
        cvContactsData = null;
        cvDeletedData = null;
        cvData = null;
        coeusMessageResources = null;
        contactListTableModel = null;
        contactListEditor = null;
        contactListRenderer = null;
        contactsSelectionModel = null;
        cvContactType = null;
        removeBeanUpdatedListener(this, AwardDetailsBean.class);
    }
    
    //Added with case 2796: Sync to Parent - Start
    
    public void setContactType(String contactType,boolean syncRequired){
        int selRow = awardContactsForm.tblContactList.getSelectedRow();
        awardContactsForm.tblContactList.setValueAt(contactType,selRow,CONTACTTYPE);
        contactListTableModel.fireTableCellUpdated(selRow, CONTACTTYPE);
        AwardContactDetailsBean bean = (AwardContactDetailsBean)cvContactsData.get(selRow);
        bean.setSyncRequired(syncRequired);
    }
    
    public Object getContactType(){
        int selRow = awardContactsForm.tblContactList.getSelectedRow();
        return awardContactsForm.tblContactList.getValueAt(selRow,CONTACTTYPE);
    }
    
    public Object getRolodexName(){
        int selRow = awardContactsForm.tblContactList.getSelectedRow();
        return awardContactsForm.tblContactList.getValueAt(selRow,NAME_COLUMN);
    }
        
    public CoeusVector getContactTypes(){
        return cvContactType;
    }
    public CoeusVector getContactInfo(){
        try {
            return (CoeusVector)ObjectCloner.deepCopy(cvContactsData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    //2796 End
}





