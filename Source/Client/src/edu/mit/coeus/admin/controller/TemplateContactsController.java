/*
 * TemplateContactsController.java
 *
 * Created on December 17, 2004, 7:40 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
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
//import edu.mit.coeus.gui.event.Controller;
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

/**
 *
 * @author  ajaygm
 */
public class TemplateContactsController extends AwardTemplateController implements
ActionListener, ListSelectionListener, MouseListener/*, BeanUpdatedListener */{
    
    /* AwardContactForm Instance*/
    private AwardContactsForm awardContactsForm;
    
    /* MDI Form Instance */
    private CoeusAppletMDIForm mdiForm;
    
    private QueryEngine queryEngine;
    /* AwardContactBean */
    //    private AwardContactBean awardContactBean;
    //    private TemplateContactBean templateContactBean;
    private AwardTemplateContactsBean awardTemplateContactsBean;
    //    private AwardContactDetailsBean awardContactDetailsBean;
    
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
    //private static final String CONFIRM_SYNC = "awardContacts_exceptionCode.1203";
    
    /**Address is mandatory for a contact, Please associate an address with the contact type.
     **/
    private static final String ADDRESS_MSG = "awardContacts_exceptionCode.1208";
    
    /** You must have atleast one contact before saving the current award
     **/
    //private static final String SAVE_MSG  ="awardContacts_exceptionCode.1209";
    private int initialSize = 0;
    private static final java.awt.Color  PANEL_BACKGROUND_COLOR =
    (Color) UIManager.getDefaults().get("Panel.background");
    
    private int rowId;
    
    /** Creates a new instance of TemplateContactsController */
    public TemplateContactsController(TemplateBaseBean templateBaseBean ,
    CoeusAppletMDIForm mdiForm , char functionType) {
        super(templateBaseBean);
        contactListTableModel = new ContactListTableModel();
        contactListRenderer =  new ContactListRenderer();
        contactListEditor = new ContactListEditor();
        awardContactsForm = new AwardContactsForm();
        this.mdiForm = mdiForm;
        /*@todo*/
        //        if(functionType == NEW_AWARD) {
        //            AwardHeaderBean awardHeaderBean = new AwardHeaderBean();
        //            awardHeaderBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        //            awardHeaderBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        //        }
        postInitComponents();
        registerComponents();
        setFunctionType(functionType);
        setTableEditors();
        display();
        setFormData(templateBaseBean);
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
        awardContactsForm.btnSync.setEnabled(false);
        awardContactsForm.btnSync.setVisible(false);
        //Added with case 2796: Sync To Parent
        awardContactsForm.btnAddSync.setEnabled(false);
        awardContactsForm.btnAddSync.setVisible(false);
        awardContactsForm.btnModifySync.setEnabled(false);
        awardContactsForm.btnModifySync.setVisible(false);
        awardContactsForm.btnDelSync.setEnabled(false);
        awardContactsForm.btnDelSync.setVisible(false);
        //2796 End
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardContactsForm.btnAdd.setEnabled(false);
            awardContactsForm.btnDelete.setEnabled(false);
            awardContactsForm.btnModify.setEnabled(false);
            awardContactsForm.btnSync.setEnabled(false);
            //Bug Fix : 1026
            //awardContactsForm.tblContactList.setEnabled(false);
            //Bug Fix : 1026
            awardContactsForm.tblContactList.setBackground(PANEL_BACKGROUND_COLOR);
            
        }else{
            awardContactsForm.btnAdd.setEnabled(true);
            awardContactsForm.btnDelete.setEnabled(true);
            awardContactsForm.btnModify.setEnabled(true);
            awardContactsForm.btnSync.setEnabled(true);
            //            awardContactsForm.tblContactList.setBackground(PANEL_BACKGROUND_COLOR);
        }
    }
    
    /** returns controlled UI.
     * @return Controlled UI
     */
    public java.awt.Component getControlledUI() {
        //For the Bug Fix:1871
         if( cvContactsData != null && cvContactsData.size() > 0){
             awardContactsForm.tblContactList.setRowSelectionInterval(0,0);
         }
         //End Bug Fix:1871
         return awardContactsForm;
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
        java.awt.Component[] components = {/*awardContactsForm.tblContactList,*/
        awardContactsForm.btnAdd,
        awardContactsForm.btnModify,
        awardContactsForm.btnDelete,
        awardContactsForm.btnSync,
        awardContactsForm.scrPnContactDetails
        };
        
        awardContactsForm.btnAdd.addActionListener(this);
        awardContactsForm.btnModify.addActionListener(this);
        awardContactsForm.btnDelete.addActionListener(this);
        awardContactsForm.btnSync.addActionListener(this);
        //        addBeanUpdatedListener(this, AwardDetailsBean.class);
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
                AwardTemplateContactsBean dataBean = (AwardTemplateContactsBean) dataVector.get(index);
                dataBean.setTemplateCode(templateBaseBean.getTemplateCode());
                //                if(getFunctionType() == TypeConstants.COPY_MODE){
                //                    if(dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                //                        queryEngine.insert(queryKey, dataBean);
                //                    }else if(dataBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                //                            queryEngine.delete(queryKey, dataBean);
                //                    }
                //                }else{
                if(dataBean.getAcType()!= null){
                    if(dataBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                        dataBean.setTemplateCode(templateBaseBean.getTemplateCode());
                        queryEngine.delete(queryKey, dataBean);
                        /*@todo*/
                    
                        
                        //                        dataBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                        //                        dataBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                        rowId = getExistingMaxId() + 1;
                        dataBean.setRowId(rowId);
                        dataBean.setAcType(TypeConstants.INSERT_RECORD);
                        queryEngine.insert(queryKey, dataBean);
                    }else if(dataBean.getAcType().equals(TypeConstants.DELETE_RECORD)){
                        queryEngine.delete(queryKey, dataBean);
                    }else if(dataBean.getAcType().equals(TypeConstants.INSERT_RECORD)){
                        dataBean.setTemplateCode(templateBaseBean.getTemplateCode());
                        //                        dataBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
                        //                        dataBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
                        if(getFunctionType() != TypeConstants.COPY_MODE){
                            if( dataBean.getRowId() == 0 ){
                                rowId = getExistingMaxId() + 1;
                                dataBean.setRowId(rowId);
                            }
                        }
                        queryEngine.insert(queryKey, dataBean);
                    }
                }
                //                }//End outer Else
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    /** sets data to form.
     * @param data data to be set.
     */
    public void setFormData(Object data) {
//        cvContactsData = new CoeusVector();
        if(data != null){
            this.templateBaseBean = (TemplateBaseBean)data;
        }
        try{
            cvContactsData = queryEngine.executeQuery(queryKey,
            AwardTemplateContactsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            initialSize = cvContactsData.size();
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
//        contactListTableModel.fireTableDataChanged();
        if( cvContactsData != null && cvContactsData.size() > 0){
            //Comment for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            //First row selection is made after loading all the form data
//            awardContactsForm.tblContactList.setRowSelectionInterval(0,0);
            //COEUSA_1456 : End
            awardTemplateContactsBean = (AwardTemplateContactsBean)cvContactsData.get(0);
            contactListTableModel.setData(cvContactsData);
            awardContactsForm.awardContactDetailsForm1.setTemplateData(awardTemplateContactsBean);
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
            if(getFunctionType() != TypeConstants.COPY_MODE && 
                    awardTemplateContactsBean.getUpdateTimestamp() != null){
                String lastUpdate = CoeusDateFormat.format(awardTemplateContactsBean.getUpdateTimestamp().toString());
                awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(lastUpdate);
                awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(awardTemplateContactsBean.getUpdateUserName());
            }else{
                awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
                awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
            }
            try {
                CoeusVector cvUpdateDetails = queryEngine.executeQuery(queryKey, "CONTACT_TEMPLATE_UPDATE_DETAIL", CoeusVector.FILTER_ACTIVE_BEANS);
                if(cvUpdateDetails != null && cvUpdateDetails.size() > 0) {
                    AwardTemplateBean updateDetail = (AwardTemplateBean)cvUpdateDetails.get(0);
                    if(getFunctionType() != TypeConstants.COPY_MODE &&
                            updateDetail.getUpdateTimestamp() != null){
                        String lastUpdate = CoeusDateFormat.format(updateDetail.getUpdateTimestamp().toString());
                        String updateUserName = updateDetail.getUpdateUserName();
                        awardContactsForm.awardContactDetailsForm1.lblUpdateUser.setVisible(true);
                        awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setVisible(true);
                        awardContactsForm.awardContactDetailsForm1.lblLastUpdate.setVisible(true);
                        awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setVisible(true);
                        awardContactsForm.pnlUpdateDetails.setVisible(true);
                        awardContactsForm.txtLastUpdate.setText(lastUpdate);
                        awardContactsForm.txtUpdateUser.setText(updateUserName);
                    }else{
                        awardContactsForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
                        awardContactsForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            awardContactsForm.tblContactList.setRowSelectionInterval(0,0);
            //COEUSQA-1456 : End
        }
        //Bug Fix:1656 Start
        //else if(cvContactsData == null)
        else if(cvContactsData == null || cvContactsData.size() == 0) {//Bug Fix:1656 End
            awardContactsForm.awardContactDetailsForm1.setTemplateData(null);
            //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
//            awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
//            awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
//            awardContactsForm.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
//            awardContactsForm.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
            awardContactsForm.pnlUpdateDetails.setVisible(false);
            awardContactsForm.awardContactDetailsForm1.lblUpdateUser.setVisible(false);
            awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setVisible(false);
            awardContactsForm.awardContactDetailsForm1.lblLastUpdate.setVisible(false);
            awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setVisible(false);
            //COEUSQA-1456 : End
            
        }
        
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
            AwardTemplateContactsBean contactsBean = (AwardTemplateContactsBean)cvContactsData.get(index);
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
                AwardTemplateContactsBean contactsCodeBean = (AwardTemplateContactsBean)cvContactsData.get(index);
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
        
        //        try{
        //            cvData = queryEngine.executeQuery(queryKey,
        //            AwardTemplateContactsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        //            if(cvData == null || cvData.size() == 0){
        //                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SAVE_MSG));
        //                return false;
        //            }
        //        }catch (CoeusException coeusException){
        //            coeusException.printStackTrace();
        //        }
        
        return true;
    }
    
    /** listens to action events.
     * @param actionEvent actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(awardContactsForm.btnAdd)) {
            showRolodex(ADD);
        } else if (source.equals(awardContactsForm.btnDelete)){
            performDeleteAction();
        } else if (source.equals(awardContactsForm.btnModify)){
            showRolodex(MODIFY);
        }else if (source.equals(awardContactsForm.btnSync)) {
            //            syncAwardContacts();
        }
    }
    
    /** Sync the award contacts with the template contacts
     */
    //    private void syncAwardContacts(){
    //        int option = CoeusOptionPane.showQuestionDialog(
    //        coeusMessageResources.parseMessageKey(CONFIRM_SYNC),
    //        CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
    //
    //        switch( option ){
    //            case (CoeusOptionPane.SELECTION_YES):
    //                //Call the Sync Contacts of the AwardController
    //                if ( syncContacts(EMPTY, getTemplateCode()) ){
    //                    setFormData(null);
    //                    setSaveRequired(true);
    //                }
    //        }
    //    }
    
    /*to display the search window*/
    private void showRolodex(String mode ) {
        CoeusSearch proposalSearch = null;
        Vector vecRolodex = null;
        HashMap rolodexData = null;
        
        //Bug Fix :1023 Declared outside
        int selRow = -1;
        ComboBoxBean typeComboBean = new ComboBoxBean();
        String strPerson = null;
        AwardTemplateContactsBean contactDetailsBean = null;
        
        try{
            if(mode.equals(ADD)) {
                proposalSearch = new CoeusSearch( CoeusGuiConstants.getMDIForm(),
                CoeusGuiConstants.ROLODEX_SEARCH,
                CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION ) ;
                proposalSearch.showSearchWindow();
                vecRolodex = proposalSearch.getMultipleSelectedRows();
            }else {
                selRow = awardContactsForm.tblContactList.getSelectedRow();
                if(selRow == -1){
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(MODIFY_MESSAGE));
                    return;
                } else {
                    
                    //Bug Fix :1023 Start1
                    typeComboBean =(ComboBoxBean) awardContactsForm.tblContactList.getValueAt(selRow, CONTACTTYPE);
                    strPerson = (String)awardContactsForm.tblContactList.getValueAt(selRow,NAME_COLUMN);
                    AwardTemplateContactsBean awardTemplateContactsBean  = (AwardTemplateContactsBean)cvContactsData.get(selRow);
                    
                    contactDetailsBean = (AwardTemplateContactsBean)ObjectCloner.deepCopy(awardTemplateContactsBean);
                    //Bug Fix :1023 END1
                    
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
                
                //Bug Fix :1023 Start2
                AwardTemplateContactsBean detailsBean = (AwardTemplateContactsBean) cvContactsData.get(selRow);
                
                HashMap hmData = new HashMap();
                hmData.put(AwardConstants.PERSON_NAME,strPerson);
                hmData.put(AwardConstants.CONTACT_TYPE,typeComboBean);
                hmData.put(AwardConstants.BEAN_BEFORE_MODIFICATION,contactDetailsBean);
                hmData.put(AwardConstants.BEAN_AFTER_MODIFICATION,detailsBean);
                
                BeanEvent beanEvent = new BeanEvent();
                beanEvent.setSource(this);
                beanEvent.setObject(hmData);
                beanEvent.setBean(new AwardTemplateContactsBean());
                fireBeanUpdated(beanEvent);
                //Bug Fix :1023 END2
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
                
                awardContactsForm.tblContactList.setRowSelectionInterval(initialSize, initialSize);
                initialSize += indx;
            }
        }
        selRow = awardContactsForm.tblContactList.getSelectedRow();
        if( selRow != -1 ){
            if(cvContactsData!= null && cvContactsData.size() > 0){
                awardContactsForm.tblContactList.clearSelection();
                awardContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
            }
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
        
        AwardTemplateContactsBean contactDetails = new AwardTemplateContactsBean();
        
//        ComboBoxBean comboBoxBean = (ComboBoxBean)cvContactType.filter(new Equals("description", value.toString())).get(0);
//        contactDetails.setContactTypeCode(Integer.parseInt(comboBoxBean.getCode()));
//        contactDetails.setContactTypeDescription(comboBoxBean.getDescription());
        
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
//            if(getFunctionType() == TypeConstants.COPY_MODE || 
//                getFunctionType() == TypeConstants.NEW_MODE){
                rowId = getExistingMaxId() + 1;
                contactDetails.setRowId(rowId);
//            }
            cvContactsData.add(contactDetails);
//            contactListTableModel.setData(cvContactsData);
//            contactListTableModel.fireTableRowsInserted(cvContactsData.size(), cvContactsData.size());
//            awardContactsForm.tblContactList.clearSelection();
//            int r = awardContactsForm.tblContactList.getSelectedRow();
            contactListTableModel.fireTableRowsInserted(cvContactsData.size(),cvContactsData.size());
//            awardContactsForm.tblContactList.setRowSelectionInterval(cvContactsData.size(), cvContactsData.size());
            awardContactsForm.tblContactList.editCellAt(cvContactsData.size() - 1, CONTACTTYPE);
            contactListEditor.cmbContactType.requestFocusInWindow();
            contactDetails.setTemplateCode(templateBaseBean.getTemplateCode());
            queryEngine.insert(queryKey,contactDetails);
        }else{
            int selRow = awardContactsForm.tblContactList.getSelectedRow();
            
            AwardTemplateContactsBean bean = (AwardTemplateContactsBean)cvContactsData.get(selRow);
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
            contactListTableModel.fireTableCellUpdated(selRow, NAME_COLUMN);
            awardContactsForm.tblContactList.setRowSelectionInterval(selRow, selRow);
        }
    }
    
    
    /*to check for the null value*/
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
        
    }
    
    
    /*to delete the row*/
    private void performDeleteAction() {
        int selectedRow = awardContactsForm.tblContactList.getSelectedRow();
        if( selectedRow == -1) {
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(DEL_MESSAGE));
            return;
        }else{
            int selectedOption
            = showDeleteConfirmMessage(
            "Are you sure you want to remove this row?");
            if( selectedOption == JOptionPane.YES_OPTION ){
                AwardTemplateContactsBean deletedBean = (AwardTemplateContactsBean)cvContactsData.elementAt(selectedRow);
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
                awardContactsForm.awardContactDetailsForm1.setTemplateData(null);
                //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
                awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
                //COEUSQA-1456 : End
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
            if(cvContactsData == null || cvContactsData.size() <= 0) {
                awardContactsForm.tblContactList.clearSelection();
                return;
            }
            int selectedRow = awardContactsForm.tblContactList.getSelectedRow();
            int rowCount = awardContactsForm.tblContactList.getRowCount();
            if (selectedRow != -1 && rowCount > 0) {
                if(cvContactsData!= null && cvContactsData.size() > 0){
                    AwardTemplateContactsBean awardTemplateContactsBean =
                    (AwardTemplateContactsBean)cvContactsData.get(selectedRow);
                    awardContactsForm.awardContactDetailsForm1.setTemplateData(awardTemplateContactsBean);
                    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
                    if(getFunctionType() != TypeConstants.COPY_MODE &&
                            awardTemplateContactsBean.getUpdateTimestamp() != null){
                        String lastUpdate = CoeusDateFormat.format(awardTemplateContactsBean.getUpdateTimestamp().toString());
                        awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(lastUpdate);
                        awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(awardTemplateContactsBean.getUpdateUserName());
                    }else{
                        awardContactsForm.awardContactDetailsForm1.txtLastUpdate.setText(CoeusGuiConstants.EMPTY_STRING);
                        awardContactsForm.awardContactDetailsForm1.txtUpdateUser.setText(CoeusGuiConstants.EMPTY_STRING);
                    }
                    //COEUSQA-1456 : End
                }
            }
        }
    }
    
    /*To get the Max of the vector*/
    private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, AwardTemplateContactsBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                AwardTemplateContactsBean bean = (AwardTemplateContactsBean)cvExistingRecords.get(0);
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
    
    /*@todo*/
    
    //    public void beanUpdated(BeanEvent beanEvent) {
    //        if( beanEvent.getSource().getClass().equals(OtherHeaderController.class) ){
    //            if( beanEvent.getBean().getClass().equals(AwardDetailsBean.class)){
    //                setRefreshRequired(true);
    //            }
    //        }
    //    }
    
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        //Modified for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        if(getFunctionType() == TypeConstants.COPY_MODE){
            setFunctionType(TypeConstants.MODIFY_MODE);
        }
        //COEUSQA-1456 : End
        setFormData(templateBaseBean);
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
        //private CoeusVector cvContactsData;
        
        String colNames[] = {"","Contact Type", "Name/Organization"};
        Class[] colTypes = new Class [] {Object.class , String.class, String.class};
        
        public void setData(CoeusVector cvContactsData) {
//            this.cvContactsData = cvContactsData;
	cvContactsData = cvContactsData;
        this.fireTableDataChanged();
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            AwardTemplateContactsBean contactDetailsBean = (AwardTemplateContactsBean)cvContactsData.get(rowIndex);
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
                        rolodexName = (contactDetailsBean.getLastName() +
                        suffix+", "+ prefix+" "+ firstName+" "+
                        middleName).trim();
                        
                    } else {
                        rolodexName = checkForNull(contactDetailsBean.getOrganization());
                    }
                    return rolodexName;
            }
            return EMPTY_STRING;
            
        }
        
        
        
        public void setValueAt(Object value, int row, int column){
            if(cvContactsData == null || cvContactsData.size() == 0) {
                return;
            }
            
            AwardTemplateContactsBean awardTemplateContactsBean  = (AwardTemplateContactsBean)cvContactsData.get(row);
            
            switch(column){
                case CONTACTTYPE:
                    if(value==null || value.toString().equals(EMPTY_STRING)) return ;
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cvContactType.filter(new Equals("description", value.toString())).get(0);
                    int contactCode = Integer.parseInt(comboBoxBean.getCode());
                    
                    if( contactCode != awardTemplateContactsBean.getContactTypeCode() ){
                        awardTemplateContactsBean.setContactTypeCode(contactCode);
                        awardTemplateContactsBean.setContactTypeDescription(comboBoxBean.getDescription());
                        if( awardTemplateContactsBean.getAcType() == null ){
                            awardTemplateContactsBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        modified = true;
                    }
                    //contactListEditor.stopCellEditing();
                    break;
                case NAME_COLUMN:
                    if(awardTemplateContactsBean.getFirstName()!= null &&
                    awardTemplateContactsBean.getFirstName().equals(value.toString())) {
                        awardTemplateContactsBean.setFirstName(value.toString().trim());
                    } else if( awardTemplateContactsBean.getFirstName().trim().equals(EMPTY_STRING)) {
                        awardTemplateContactsBean.setOrganization(value.toString().trim());
                    }
                    if( awardTemplateContactsBean.getAcType() == null ){
                        awardTemplateContactsBean.setAcType(TypeConstants.UPDATE_RECORD);
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
        awardContactsForm = null;
        mdiForm = null;
        queryEngine = null;
        //        awardContactBean = null;
        awardTemplateContactsBean = null;
        //        awardContactDetailsBean = null;
        cvContactsData = null;
        cvDeletedData = null;
        cvData = null;
        coeusMessageResources = null;
        contactListTableModel = null;
        contactListEditor = null;
        contactListRenderer = null;
        contactsSelectionModel = null;
        cvContactType = null;
        //        removeBeanUpdatedListener(this, AwardDetailsBean.class);
    }
    
}//End Class
