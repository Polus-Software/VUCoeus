/*
 * @(#)ProtocolMaintenanceForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 21-SEP-2010
 * by Johncy M John
 */
package edu.mit.coeus.irb.gui;

import java.text.ParseException;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;


/** This class represent the <CODE>ProtocolMaintenanceForm</CODE> which contain Protocol
 * information. This consists of details regarding Vulnerable Subjects for this
 * Protocol and also the location details where the research will be held.
 *
 * @author ravikanth Created on October 24, 2002, 11:10 AM
 * @updated Subramanya delete button enable/disable functionality.
 */
public class ProtocolMaintenanceForm extends JComponent {

        // Variables declaration - do not modify
    private JPanel pnlMain;
    private JPanel pnlDetails;
    private CoeusTextField txtProtocolId;
    private CoeusTextField txtApplnDate;
    private CoeusTextField txtExpDate;
    // Last Approval Date Start1
    private CoeusTextField txtLastApprovalDate;
    private JLabel lblLastApprovalDate;
    //Last Approval Date End1
    private JLabel lblApplnDate;
    private JLabel lblProtID;
    // private JPanel pnlSubjects;
    private JLabel lblApprDate;
    private JLabel lblExpDate;
    /*
     * Fix for: GNIR-I Enchancement Phase - II
     * Despcription : To introduce check box for billable Flag for Protocol.
     * Updated by Subramanya 11th April 2003.
     */
    private JLabel lblBillable;
    private JCheckBox chkBillable;
    
    private CoeusComboBox cmbType;
    private JPanel pnlLocations;
    private JLabel lblType;
    //private JButton btnAddAddress;
    private CoeusTextField txtApprDate;
    private CoeusComboBox cmbStatus;
    private JTextArea  txtTitle; //CoeusTextField txtTitle;
    private JLabel lblStatus;
    private CoeusTextField txtFDAApplNo;
    private JLabel lblTitle;
    private JLabel lblFDAApplNo;
    
    private JLabel lblRef1,lblRef2;
    private CoeusTextField txtFldRef1,txtFldRef2;
    private JLabel lblDescription;
    private JTextArea txtArDescription;
    private Vector orgTypes = new Vector();
    /* holds the reference of parent object*/
    private CoeusAppletMDIForm mdiForm=null;
    /* holds the mode in which the form is opened*/
    private char functionType = 'D';
    // Modified for Enable multicampus for default organization in protocols - Start
//    private ProtocolLocationForm locationForm = null;
    public ProtocolLocationForm locationForm = null;
    // Modified for Enable multicampus for default organization in protocols - End
    private ProtocolInfoBean protocolInfoBean = null;
    private final String ROLODEX_SEARCH = "rolodexSearch";
    private Vector protocolLocations = null;
    private Vector availableTypes = null;
    private Vector availableStatus = null;
    private Vector locationsToSend = new Vector();
    //private Vector parameters;// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005

    private DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
//    
    ProtocolLocationListBean newLocListBean = null;
    ProtocolLocationListBean oldLocListBean = null;
    Font labelFont = CoeusFontFactory.getLabelFont();

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    boolean saveRequired = false;
    
    boolean locModified = false;
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement
    /** Holds the mode in which the protocol is opened */
    private String mode;
    //code added for coeus4.3 enhancements for location function type
    private char locationFunctionType = 'D';
    private char originalFunctionType = 'A';
    //COEUSQA-2801-Date validation message display unnecessary - IRB & IACUC
    private final String DATE_SEPERATOR = "/-:,";
    private final String DATE_FORMAT = "MM/dd/yyyy";
    private String oldApplicationDate = null;
    /** Creates new form <CODE>ProtocolMaintenanceForm</CODE>.
     */
    public ProtocolMaintenanceForm() {
    }

    /** Creates new form <CODE>ProtocolMaintenanceForm</CODE> with the given Protocol
     * details and specified <CODE>functionType</CODE>.
     * @param functionType which specifies the mode in which the form will be
     * opened.
     * @param infoBean <CODE>ProtocolInfoBean</CODE> with all the details regarding to
     * protocol along with selected Vulnerable Subjects and Location Details.
     */
    public ProtocolMaintenanceForm( char functionType,
                                                ProtocolInfoBean infoBean){
        this.functionType = functionType;
        this.protocolInfoBean = infoBean;
    }

    /** This method is used to initialize the form components, set the form
     * data and set the enabled status for all components depending on the
     * <CODE>functionType</CODE> specified while opening the form.
     *
     * @param mdiForm reference to the <CODE>CoeusAppletMDIForm</CODE>.
     * @return JComponent reference to the <CODE>ProtocolMaintenanceForm</CODE> component
     * after initializing and setting the data.
     */
    public JComponent showProtocolMaintenanceForm(CoeusAppletMDIForm mdiForm){
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        setFormData();
        formatFields();
        return pnlMain;
    }

    /** This method is used to check whether any Protocol Locations have been
     * modified for existing Protocol.
     *
     * @return true if any modifications have been done to Protocol
     * Locations.
     */
    public boolean isProtocolLocationsModified(){
        return locModified;
    }

    /** This method is used to check whether any unsaved modifications are
     * available.
     * @return true if there are any modifications that are not saved,
     * else false.
     */
    public boolean isSaveRequired(){
        return  ( saveRequired || locationForm.isSaveRequired() );
    }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {        
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE ) &&
                !( functionType == CoeusGuiConstants.AMEND_MODE )) {
                cmbType.requestFocusInWindow();            
        }
    }    
    //End Amit
    
    /** This method is used to set the saveRequired flag.
     * @param save boolean value which specifies any unsaved changes are present
     * or not.
     */
    public void setSaveRequired(boolean save){
        this.saveRequired = save;
        locationForm.setSaveRequired( save );
    }

    /** This method is used to set the function type which specifies the mode in
     * which the form is opened.
     * @param functionType char value  which specifies the mode in which the
     * form is opened.
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }


    /** This method is used to set the <CODE>ProtocolInfoBean</CODE> with all the details
     * of the Protocol.
     * @param infoBean <CODE>ProtocolInfoBean</CODE> which consists of all the details of
     * Protocol like Vulnerable Subjects, Locations, Investigator details etc.
     */
    public void setValues(ProtocolInfoBean infoBean){
        
        
        
        this.protocolInfoBean = infoBean;
        setFormData();
        formatFields();
        //code added for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
        locationForm.setFunctionType(locationFunctionType);
        locationForm.formatFields();
        
    }

    /**
     * This method is used to the set the Available Subjects of this component.
     * @param availableSubjects Collection of Vulnerable Subject details.
     */
/*    public void setAvailableSubjects(Vector availableSubjects){
        this.availableSubjects = availableSubjects;
    }
*/
    /**
     * This method is used to the focus to the Protocol Review Type comboBox.
     */
    public void setFocusToType() {
        cmbType.requestFocusInWindow();
    }
    /**
     * This method is used to set the enabled status for the components
     * depending on the functionType specified.
     */
    private void formatFields(){

        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != 'D' ? true : false ;
        boolean enabled = (functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE) ? false : true ;
        txtProtocolId.setEditable(false);
        cmbStatus.setEnabled(false);

        txtApplnDate.setOpaque(enabled);
        txtProtocolId.setOpaque(false);
        txtApprDate.setOpaque(enabled);
        txtFDAApplNo.setOpaque(enabled);
        txtTitle.setOpaque(enabled);
        txtArDescription.setOpaque( enabled );
        txtExpDate.setOpaque(enabled);
        txtFldRef1.setOpaque( enabled );
        txtFldRef2.setOpaque( enabled );
        //Last Approval Start2
        txtLastApprovalDate.setOpaque( enabled );
        // Case# 2562: Last Approval Date field  editable in Administrative Correction Mode - Start
//          txtLastApprovalDate.setEditable( false );
        if( CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS.equalsIgnoreCase(mode) ){
            txtLastApprovalDate.setEditable( true );
        } else{
            txtLastApprovalDate.setEditable( false );
        }
        // Case# 2562: Last Approval Date field  editable in Administrative Correction Mode - End
        //LastApproval End2
        
        txtApplnDate.setEditable(enabled);
        txtApprDate.setEditable(enabled);
        txtExpDate.setEditable(enabled);
        txtFDAApplNo.setEditable(enabled);
        txtTitle.setEditable(enabled);
        txtArDescription.setEditable( enabled );
        cmbType.setEnabled(enabled);
        chkBillable.setEnabled( enabled );
        
        txtFldRef1.setEnabled( enabled );
        txtFldRef2.setEnabled( enabled );
        
        //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - Start
        //To disable Approval and Expiration Dates in Administrative Correction mode
        if( mode != null &&
        mode.equalsIgnoreCase(CoeusGuiConstants.ADMINISTRATIVE_CORRECTIONS) ){
            enableApprovalExpirationDates(true);
        }else {
            enableApprovalExpirationDates(false);
        }
        //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - End
    }

    /**
     * This method is used to set the available Protocol Types.
     * @param availableTypes collection of available Protocol Types
     */
    public void setAvailableTypes(Vector availableTypes){
        this.availableTypes = availableTypes;
    }

     /** This method is used to the set the collection of Available Protocol Status
      * information.
      * @param availableStatus Collection of available Protocol Status details.
      */
    public void setAvailableStatus(Vector availableStatus){
        this.availableStatus = availableStatus;
    }

    
    /** This methos is used throw the exception with the given message.
     * @param mesg String which will describe the exception
     * @throws Exception with the given custom message.
     */
    public void log(String mesg) throws CoeusUIException {
        //Commented by sharath(20 - Aug - 2003)
        //throw new Exception(mesg);
        
        //Bug Fix ( Defect Id : 379)
        CoeusUIException coeusUIException = new CoeusUIException(mesg,CoeusUIException.WARNING_MESSAGE);
        coeusUIException.setTabIndex(0);
        throw coeusUIException;
        //Bug Fix ( Defect Id : 379)
    }

    /** Validate the form data before sending to the database.
     *
     * @return true if all form controls have valid data, else false.
     * @throws Exception with the custom message if any form control doesn't
     * have valid data.
     */
    public boolean validateData() throws Exception, CoeusUIException{
        //Added for Case 3075 Editing Questionnaire for Amendments/Renewals - start
        if(functionType == CoeusGuiConstants.DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE){
            return true;
        }
        //Added for Case 3075  Editing Questionnaire for Amendments/Renewals - end
        
        if((txtTitle.getText()== null)
                || (txtTitle.getText().trim().length() == 0)){
            log(coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1070"));
            txtTitle.requestFocus();
            return false;
        }else if((txtApplnDate.getText()== null)
                || (txtApplnDate.getText().trim().length() == 0)){
            /* Application Date doesn't have any value */
            log(coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1071"));
            return false;
            /** 
             * Removing the mandatory condition to FDA Application Number 
             * as per Change Request Id: 107, Feb' 14 2003
             * Updated by Raghunath P.V. Feb' 17 2003
             */
            
          /*  
            }
            else if( (txtFDAApplNo == null)
                || (txtFDAApplNo.getText().trim().length() == 0) ){
            log(coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1072"));
            return false; */
        }else{
            Date applnDate = null;
            Date apprDate = null;
            Date expDate = null;
            String oldDate;
            String convertedDate = dtUtils.formatDate(txtApplnDate.getText(),
                    "/-:," , "dd-MMM-yyyy");
            if (convertedDate==null){
                oldDate = dtUtils.restoreDate(txtApplnDate.getText(),"/-:,");
                if(oldDate == null || oldDate.equals(txtApplnDate.getText())){
                    //COEUSQA-2801-Date validation message display unnecessary - IRB & IACUC
                    if(oldApplicationDate != null && oldApplicationDate.trim().length() > 0){
                        txtApplnDate.setText(oldApplicationDate);
                    }
                    log(coeusMessageResources.parseMessageKey(
                                        "memMntFrm_exceptionCode.1048"));
                    return false;
                }
            }
            applnDate = dtFormat.parse(
                dtUtils.restoreDate(txtApplnDate.getText(),"/:-,"));
            if(applnDate == null){
                log(coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1073"));
                return false;
            }
            if((txtApprDate.getText() != null)
                && (txtApprDate.getText().trim().length() > 0)){

                convertedDate = dtUtils.formatDate(txtApprDate.getText(),
                        "/-:," , "dd-MMM-yyyy");
                if (convertedDate==null){
                    oldDate = dtUtils.restoreDate(txtApprDate.getText(),"/-:,");
                    if(oldDate == null || oldDate.equals(txtApprDate.getText())){
                        log(coeusMessageResources.parseMessageKey(
                                            "memMntFrm_exceptionCode.1048"));
                        return false;
                    }
                }
                apprDate = dtFormat.parse(
                    dtUtils.restoreDate(txtApprDate.getText(),"/:-,"));
                if(apprDate == null){
                    log(coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1074"));
                    return false;
                }
            }
            if((txtExpDate.getText() != null)
                && (txtExpDate.getText().trim().length() > 0)){

                convertedDate = dtUtils.formatDate(txtExpDate.getText(),
                        "/-:," , "dd-MMM-yyyy");
                if (convertedDate==null){
                    oldDate = dtUtils.restoreDate(txtExpDate.getText(),"/-:,");
                    if(oldDate == null || oldDate.equals(txtExpDate.getText())){
                        log(coeusMessageResources.parseMessageKey(
                                            "memMntFrm_exceptionCode.1048"));
                        return false;
                    }
                }

                expDate = dtFormat.parse(
                    dtUtils.restoreDate(txtExpDate.getText(),"/:-,"));
                if(expDate == null){
                    log(coeusMessageResources.parseMessageKey(
                                    "protoMntFrm_exceptionCode.1075"));
                    return false;
                }
            }       
            
            //Commented for  COEUSQA-2801 : IACUC error approval date must be 
            //greater than application date on save attempt of withdrawn amendment - start
//            if( apprDate != null){
//                if(apprDate.compareTo(applnDate)<0){
//                    /* Approval Date is earlier than Application Date */
//                    log(coeusMessageResources.parseMessageKey(
//                                    "protoMntFrm_exceptionCode.1076"));
//                    txtApprDate.requestFocus();
//                    return false;
//                }
//            }
            
            
//            if(expDate != null && apprDate != null){
//                if( (expDate.compareTo(applnDate)<0 )
//                        || (expDate.compareTo(apprDate) <0 )){
//                    /* Expiration Date is earlier than Application Date or
//                       Approval Date */
//                    log(coeusMessageResources.parseMessageKey(
//                                    "protoMntFrm_exceptionCode.1077"));
//                    txtExpDate.requestFocus();
//                    return false;
//                }
//            }
            //Commented for  COEUSQA-2801 : IACUC error approval date must be 
            //greater than application date on save attempt of withdrawn amendment - end
            // Modified for Enable multicampus for default organization in protocols - Start
            if(functionType != 'A' && !locationForm.isMulticampusOrg()){
                locationForm.validateLocations();
            }
            // Modified for Enable multicampus for default organization in protocols - End
        }
        return true;
    }
 

    /** This method is used to get the <CODE>ProtocolMaintenanceForm</CODE> data.
     * @return <CODE>ProtocolInfoBean</CODE> which consists of all the details given by the
     * user in the form.
     * @throws Exception with custom if fetching of the details from the form
     * fails.
     */
    public ProtocolInfoBean getFormData() throws Exception{

        if( functionType == CoeusGuiConstants.ADD_MODE 
                && originalFunctionType == CoeusGuiConstants.ADD_MODE){
            protocolInfoBean = new ProtocolInfoBean();

            // setting all default values to bean so that it wont fire property
            // change events when default values are set.
            protocolInfoBean.setTitle("");
            
            /* commented as FDA application number is not mandatory */
            //protocolInfoBean.setFDAApplicationNumber("");
            
            if((txtApplnDate.getText() != null)
                    && (txtApplnDate.getText().trim().length()>0)){
                protocolInfoBean.setApplicationDate(
                        new java.sql.Date(dtFormat.parse(
                                dtUtils.restoreDate(txtApplnDate.getText(),
                                        "/-:,")).getTime()));


            }
            protocolInfoBean.setProtocolTypeCode(1);
            protocolInfoBean.setProtocolStatusCode(1);
            protocolInfoBean.setAcType("I");
        } else {
            protocolInfoBean.setAcType("U");
        }
        
        protocolInfoBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    /**
                     * Ref ID : 107 Feb' 14/`8 2003. FDA Application Number
                     * Not Mandatory.
                     * UPdated for Save Confirmation Bug. Feb' 19 2003.
                     * Updated by Subramanya 
                     */
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            saveRequired = true;
                        }
                    }
                    
                    //saveRequired = true;
                    if(functionType == 'M'){
                        protocolInfoBean.setAcType("U");

                    }
                }
        });

        protocolInfoBean.setTitle(txtTitle.getText());
        protocolInfoBean.setDescription( txtArDescription.getText().length() == 0 ? null:
                    txtArDescription.getText() );
        /**
         * Ref ID : 107 Feb' 14/`8 2003. FDA Application Number
         * Not Mandatory.
         * UPdated for Save Confirmation Bug. Feb' 19 2003.
         * Updated by Subramanya 
         */
        String fdsApplNumber = txtFDAApplNo.getText();
        if( fdsApplNumber !=  null && fdsApplNumber.trim().length() < 1 ){
            fdsApplNumber =  null;
        }
        protocolInfoBean.setFDAApplicationNumber( fdsApplNumber );
        
        protocolInfoBean.setRefNum_1( txtFldRef1.getText().length() == 0 ? null:
                    txtFldRef1.getText());
                    
        protocolInfoBean.setRefNum_2( txtFldRef2.getText().length() == 0 ? null:
                    txtFldRef2.getText());
        //protocolInfoBean.setFDAApplicationNumber(txtFDAApplNo.getText());
        if((txtApplnDate.getText() != null)
                && (txtApplnDate.getText().trim().length()>0)){
            //COEUSQA-2801-Date validation message display unnecessary - IRB & IACUC
//            protocolInfoBean.setApplicationDate(
//                                new java.sql.Date(dtFormat.parse(
//                            dtUtils.restoreDate(txtApplnDate.getText(),"/-:,")).getTime()));
            String convertedDate = dtUtils.formatDate(txtApplnDate.getText(), DATE_SEPERATOR, DATE_FORMAT);
            if(convertedDate == null) {
                convertedDate = dtUtils.restoreDate(txtApplnDate.getText(), DATE_SEPERATOR);
            }
            try { 
              protocolInfoBean.setApplicationDate(
                            new java.sql.Date(dtFormat.parse(convertedDate).getTime()));
            } catch (ParseException pe) {
                saveRequired = true;
            }
        }else{
            protocolInfoBean.setApplicationDate(null);
        }

        if((txtApprDate.getText()!= null)
                && (txtApprDate.getText().length()>0)){

            protocolInfoBean.setApprovalDate(
                    new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtApprDate.getText(),
                                    "/-:,")).getTime()));

        }else {
            if(protocolInfoBean.getApprovalDate() != null){
                protocolInfoBean.setApprovalDate(null);
            }
        }

        if((txtExpDate.getText()!= null)
                && (txtExpDate.getText().length()>0)){
            protocolInfoBean.setExpirationDate(
                    new java.sql.Date(dtFormat.parse(
                            dtUtils.restoreDate(txtExpDate.getText(),
                                    "/-:,")).getTime()));

        }else {
            if(protocolInfoBean.getExpirationDate() != null){
                protocolInfoBean.setExpirationDate(null);
            }
        }
        //Last Approval Start4
        // commented for case # 3090 - start
        // when administrative correction is done on an approved protocol,
        // this code is resetting the expiration date data from last approval date
//        if((txtLastApprovalDate.getText()!= null)
//        && (txtLastApprovalDate.getText().length()>0)){
//            protocolInfoBean.setExpirationDate(
//            new java.sql.Date(dtFormat.parse(
//            dtUtils.restoreDate(txtLastApprovalDate.getText(),
//            "/-:,")).getTime()));
//
//        }else { 
        //commented for case # 3090 - end
//            if(protocolInfoBean.getLastApprovalDate() != null){
//                protocolInfoBean.setLastApprovalDate(null);
//            }
//        }                    
    
        //Last Approval End 4
        // Case# 2562: Last Approval Date field  editable in Administrative Correction Mode - Start
        if((txtLastApprovalDate.getText()!= null)
        && (txtLastApprovalDate.getText().length()>0)){
            protocolInfoBean.setLastApprovalDate(
                    new java.sql.Date(dtFormat.parse(
                    dtUtils.restoreDate(txtLastApprovalDate.getText(),
                    "/-:,")).getTime()));
            
        }else {
            protocolInfoBean.setLastApprovalDate(null);
        }
        // Case# 2562: Last Approval Date field  editable in Administrative Correction Mode - End
        protocolInfoBean.setIsBillableFlag(chkBillable.isSelected());
       
        ComboBoxBean typeBean =
                (ComboBoxBean)cmbType.getSelectedItem();
        protocolInfoBean.setProtocolTypeCode(Integer.parseInt(
                typeBean.getCode()));
        protocolInfoBean.setProtocolTypeDesc(typeBean.toString());

        ComboBoxBean statusBean =
                (ComboBoxBean)cmbStatus.getSelectedItem();
        protocolInfoBean.setProtocolStatusCode(Integer.parseInt(
                statusBean.getCode()));
        protocolInfoBean.setProtocolStatusDesc(statusBean.toString());
        
        Vector newLocations = locationForm.getLocations();

        if((newLocations != null) && (newLocations.size() > 0)){
            if( functionType == CoeusGuiConstants.ADD_MODE ||
                functionType == CoeusGuiConstants.AMEND_MODE ||
                functionType == CoeusGuiConstants.NEW_AMENDMENT ||
                functionType == CoeusGuiConstants.RENEWAL ) {
                //in add mode and copy mode should not consider existing records
                locationsToSend = new Vector();
            }
            ProtocolLocationListBean locBean = null;
            int locCount = newLocations.size();
            int dbLocCount = 0;
            if(locationsToSend!= null && locationsToSend.size() > 0){
                dbLocCount = locationsToSend.size();
                //System.out.println("loc size in maintenanceForm:"+dbLocCount);
            }
            //System.out.println("new locations count:"+locCount);
             boolean found = false;
            for(int newLocIndex = 0; newLocIndex < locCount; newLocIndex++){
                int foundIndex = -1;
                found = false;
                locBean = (ProtocolLocationListBean)newLocations.elementAt(newLocIndex);
                newLocListBean = new ProtocolLocationListBean();
                newLocListBean = locBean;
                    // check for modifications in existing beans in modify mode
                    for(int oldLocIndex = 0;oldLocIndex < dbLocCount; oldLocIndex++){
                        oldLocListBean = (ProtocolLocationListBean)
                            locationsToSend.elementAt(oldLocIndex);
//                        if(functionType == 'M'){
                        if(locationFunctionType == 'M'){ 
                            oldLocListBean.addPropertyChangeListener(
                                 new PropertyChangeListener(){
                                    public void propertyChange(
                                            PropertyChangeEvent pce){
                                        /* if address has been changed to a
                                           location setting the corresponding
                                           rolodex id will fire property change
                                           event. If any changes have been done
                                           to exisiting location new record will
                                           be inserted for protocol details with
                                           new sequence no. So set acType for
                                           both beans to MODIFY_RECORD */
                                        locModified = true;
                                        oldLocListBean.setAcType("U");
                                        saveRequired = true;
                                    }
                            });
                        }
                        if(newLocListBean.getOrganizationId().equals(
                                oldLocListBean.getOrganizationId())){
                            found = true;
                            foundIndex = oldLocIndex;
                            break;
                        }
                    }
                if(!found){
                    /* if location is new set AcType to INSERT_RECORD */
                    locModified = true;
                    newLocListBean.setAcType("I");
                    locationsToSend.addElement(newLocListBean);
                    saveRequired = true;
                }else{
                    /* if present set the values to the bean. if modified,
                       bean will fire property change event */
                    if(oldLocListBean != null){
                        oldLocListBean.setOrganizationTypeId( newLocListBean.getOrganizationTypeId());
                        oldLocListBean.setRolodexId(
                            newLocListBean.getRolodexId());
                        if(foundIndex != -1){
                            //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
                            if(TypeConstants.INSERT_RECORD.equals(newLocListBean.getAcType())){
                            oldLocListBean.setCanLoadCorrepondents(true);
                            newLocListBean.setAcType(null);
                            }
                            //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
                            locationsToSend.setElementAt(oldLocListBean,
                                foundIndex);
                        }
                    }
                }
            }
//            if(functionType == 'M' ){
             if(locationFunctionType == 'M' ){
                //check for deleted beans in modify mode
                for(int oldLocIndex = 0; oldLocIndex <dbLocCount; oldLocIndex++){
                    found = false;
                    oldLocListBean = (ProtocolLocationListBean)
                                    locationsToSend.elementAt(oldLocIndex);
                    for(int newLocIndex = 0; newLocIndex < newLocations.size(); 
                            newLocIndex++){
                        locBean = (ProtocolLocationListBean)newLocations.elementAt(
                                                               newLocIndex);
                        if(oldLocListBean.getOrganizationId().equals(
                                locBean.getOrganizationId())){
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        /* if existing location has been deleted set acType
                           to DELETE_RECORD */
                        saveRequired = true;
                        locModified = true;
                        oldLocListBean.setAcType("D");
                        locationsToSend.setElementAt(oldLocListBean,
                            oldLocIndex);
                    }
                }
            }
        }

        /* commented to send all the data from client to generalize seq no. implementation in server 
        protocolInfoBean.setLocationLists(filterUnModifiedLocations(locationsToSend));
        */
        protocolInfoBean.setLocationLists(locationsToSend);
        //}
        return protocolInfoBean;
    }

    /*
    // supporting method used to filter all un-modified locations , so that only modified
    // location details will go to the server. 
    private Vector filterUnModifiedLocations( Vector locations ) {
        Vector modifiedLocations = new Vector();
        if( locations != null ) {
            ProtocolLocationListBean locBean;
            int count = locations.size();
            //System.out.println("before filtering loc:");
            for( int indx = 0 ; indx < count; indx++ ) {
                locBean = ( ProtocolLocationListBean ) locations.elementAt( indx );
                //System.out.println("locBean:"+locBean);
                if( functionType == 'E' ) {
                    if(locBean.getAcType() == null ||  !locBean.getAcType().equalsIgnoreCase("D")) {
                        locBean.setAcType( "I" );
                        modifiedLocations.addElement( locBean );
                    }
                }else if( locBean.getAcType() != null ) {
                    modifiedLocations.addElement( locBean );
                }
            }
        }else{
            modifiedLocations = locations;
        }
        return modifiedLocations;
    }*/
    
    //supporting method to set the form data of protocol maintenance.
    private void setFormData(){
        
        /****** begin: changed on 12-Feb-03 to fix the bug for 
         reference id: 200 */
        if( ( availableTypes != null ) && ( availableTypes.size() > 0 ) 
                && ( cmbType.getItemCount() == 0 ) ) {
            for(int index = 0 ; index < availableTypes.size(); index++){
                cmbType.addItem((ComboBoxBean)availableTypes.elementAt(index));
            }
        }

        if( ( availableStatus != null ) && ( availableStatus.size() > 0 ) 
                && ( cmbStatus.getItemCount() == 0 ) ) {
            for(int index = 0 ; index < availableStatus.size(); index++){
                cmbStatus.addItem((ComboBoxBean)availableStatus.elementAt(index));
            }
        }
        /****** end of bug fix for ref id:  200 */
        // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
       /* if( parameters != null && parameters.size() > 0 ) {
            lblRef1.setText( parameters.elementAt( 0 ).toString()+" :" );
            lblRef2.setText( parameters.elementAt( 1 ).toString()+" :" );
        }*/
        //End 02-Sep-2005
        if( protocolInfoBean != null ){
            
            chkBillable.setSelected( protocolInfoBean.isBillableFlag() );
            txtProtocolId.setText(protocolInfoBean.getProtocolNumber());
            txtTitle.setText(protocolInfoBean.getTitle());
            txtArDescription.setText( protocolInfoBean.getDescription());
            //txtArDescription.setCaretPosition(0);
            txtFDAApplNo.setText(protocolInfoBean.getFDAApplicationNumber());
            txtFldRef1.setText( protocolInfoBean.getRefNum_1() );
            txtFldRef2.setText( protocolInfoBean.getRefNum_2() );
            if ( protocolInfoBean.getApplicationDate() != null ){
                txtApplnDate.setText(dtUtils.formatDate(
               protocolInfoBean.getApplicationDate().toString(),"dd-MMM-yyyy"));
            }

            if ( protocolInfoBean.getApprovalDate() != null ){
                txtApprDate.setText(dtUtils.formatDate(
                    protocolInfoBean.getApprovalDate().toString(),
                    "dd-MMM-yyyy"));
            }

            if ( protocolInfoBean.getExpirationDate() != null ){
                txtExpDate.setText(dtUtils.formatDate(
                    protocolInfoBean.getExpirationDate().toString(),
                    "dd-MMM-yyyy"));
            }
            //Last Approval Start5
             if ( protocolInfoBean.getLastApprovalDate() != null ){
                txtLastApprovalDate.setText(dtUtils.formatDate(
                    protocolInfoBean.getLastApprovalDate().toString(),
                    "dd-MMM-yyyy"));
            }
            //Last Approval End5
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    protocolInfoBean.getProtocolTypeCode()).toString());
            comboBean.setDescription(protocolInfoBean.getProtocolTypeDesc());
            //code modified for coeus4.3 concurrent Amendments/Renewals enhancement - starts
            //
//            if(functionType ==CoeusGuiConstants.DISPLAY_MODE 
//                    || functionType ==CoeusGuiConstants.AMEND_MODE){
//                cmbType.addItem(comboBean);
//            }else{
//                for(int typeRow = 0; typeRow < cmbType.getItemCount();
//                        typeRow++){
//                    if(((ComboBoxBean)cmbType.getItemAt(
//                            typeRow)).getCode().equals(comboBean.getCode())){
//                        cmbType.setSelectedIndex(typeRow);
//                    }
//                }
//            }
            boolean isPresent = false;
            for(int typeRow = 0; typeRow < cmbType.getItemCount();
                    typeRow++){
                if(((ComboBoxBean)cmbType.getItemAt(
                        typeRow)).getCode().equals(comboBean.getCode())){
                    cmbType.setSelectedIndex(typeRow);
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent){
                cmbType.addItem(comboBean);
                cmbType.setSelectedItem(comboBean);                
            }
            //code modified for coeus4.3 concurrent Amendments/Renewals enhancement - ends
            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    protocolInfoBean.getProtocolStatusCode()).toString());
            comboBean.setDescription(protocolInfoBean.getProtocolStatusDesc());
            //code modified for coeus4.3 enhancements that UI to be in display mode
            //when new amendment or renewal is created
            cmbStatus.addItem(comboBean);
            cmbStatus.setSelectedItem(comboBean);
//            if(functionType ==CoeusGuiConstants.DISPLAY_MODE
//                    || functionType ==CoeusGuiConstants.AMEND_MODE){
//                cmbStatus.addItem(comboBean);
//                //Coeus Enhancement case #1797 start
//                cmbStatus.setSelectedItem(comboBean);
//                //Coeus Enhancement case #1797 end
//            }else{
//                for(int typeRow=0;typeRow<cmbStatus.getItemCount();typeRow++){
//                    if(((ComboBoxBean)cmbStatus.getItemAt(
//                            typeRow)).getCode().equals(comboBean.getCode())){
//                        cmbStatus.setSelectedIndex(typeRow);
//                    }
//                }
//            }
            protocolLocations = protocolInfoBean.getLocationLists();
            if(protocolLocations != null && protocolLocations.size() > 0 ) {
                locationsToSend = new Vector();
                int locCount = protocolLocations.size();
                for(int locIndex = 0 ; locIndex < locCount; locIndex++ ) {
                    ProtocolLocationListBean listBean = new ProtocolLocationListBean();
                    ProtocolLocationListBean tempBean 
                        = (ProtocolLocationListBean)protocolLocations.elementAt(locIndex);
                    listBean.setProtocolNumber(tempBean.getProtocolNumber());
                    listBean.setSequenceNumber(tempBean.getSequenceNumber());
                    listBean.setRolodexId(tempBean.getRolodexId());
                    listBean.setOrganizationId(tempBean.getOrganizationId());
                    listBean.setOrganizationTypeId( tempBean.getOrganizationTypeId());
                    //System.out.println("in setFormData:"+listBean.getOrganizationTypeId());
                    listBean.setOrganizationTypeName(tempBean.getOrganizationTypeName());
                    listBean.setOrganizationName(tempBean.getOrganizationName());
                    listBean.setAddress(tempBean.getAddress());
                    listBean.setAcType(tempBean.getAcType());
                    listBean.setUpdateUser(tempBean.getUpdateUser());
                    listBean.setUpdateTimestamp(tempBean.getUpdateTimestamp());
                    locationsToSend.addElement(listBean);
                }

            }
            locationForm.setLocations(getLocationsFromProtocol());

        }
        if((txtApplnDate.getText() == null)
                || (txtApplnDate.getText().trim().length() == 0)){
            String todayDate = dtUtils.formatDate(
                    (new java.sql.Timestamp(
                        (new java.util.Date()).getTime())).toString(),
                     "dd-MMM-yyyy");
            txtApplnDate.setText(todayDate);
        }
        txtTitle.setCaretPosition(0);
        txtFDAApplNo.setCaretPosition(0);
        txtTitle.requestFocus();
        saveRequired = false;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        pnlMain = new JPanel();
        pnlDetails = new JPanel();
        lblProtID = new JLabel();
        lblStatus = new JLabel();
        lblApplnDate = new JLabel();
        lblExpDate = new JLabel();
        lblType = new JLabel();
        lblBillable = new JLabel();
        chkBillable = new JCheckBox();        
        chkBillable.addChangeListener( new ChangeListener(){
           public void  stateChanged( ChangeEvent chEvent ){
               //System.out.println("in bill state");
                saveRequired = true;
           }
        });
        
        lblTitle = new JLabel();
        lblApprDate = new JLabel();
        lblFDAApplNo = new JLabel();
        txtProtocolId = new CoeusTextField();
        txtApplnDate = new CoeusTextField();
        txtExpDate = new CoeusTextField();
        txtTitle = new   JTextArea() ;  //CoeusTextField();
        txtApprDate = new CoeusTextField();
        txtFDAApplNo = new CoeusTextField();
        cmbType = new CoeusComboBox();
        cmbStatus = new CoeusComboBox();
        lblRef1 = new JLabel();
        lblRef2 = new JLabel();
        txtFldRef1 = new CoeusTextField();
        txtFldRef2 = new CoeusTextField();
        lblDescription = new JLabel();
        //Last Approval Start6
        txtLastApprovalDate = new CoeusTextField();
        lblLastApprovalDate = new JLabel();
        //Last Approval End6
        
        txtArDescription = new JTextArea();
        txtTitle.setWrapStyleWord(true);
        txtTitle.setLineWrap(true);
        txtTitle.setDocument(new LimitedPlainDocument( 2000 ));
        txtTitle.setFont(CoeusFontFactory.getNormalFont());
        txtTitle.setRows(2000) ;
        
        pnlLocations = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        //pnlDetails.setMinimumSize(new Dimension(450,150));
        pnlDetails.setMinimumSize(new Dimension(600,150));
        //pnlDetails.setPreferredSize(new Dimension(800,150));
        //pnlDetails.setPreferredSize(new Dimension(800,400));
        pnlDetails.setLayout(new GridBagLayout());
        pnlDetails.setBorder(new TitledBorder(new EtchedBorder(),
                "Protocol Details", TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));       
                
        lblProtID.setText("Protocol Number :");
        lblProtID.setFont(CoeusFontFactory.getLabelFont());
        lblProtID.setHorizontalAlignment(SwingConstants.RIGHT);
        lblProtID.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblProtID, gridBagConstraints);

        lblStatus.setText("Status :");
        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        lblStatus.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblStatus, gridBagConstraints);

        lblApplnDate.setText("Application Date :");
        lblApplnDate.setFont(CoeusFontFactory.getLabelFont());
        lblApplnDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblApplnDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4; //2
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblApplnDate, gridBagConstraints);

        lblExpDate.setText("Expiration Date :");
        lblExpDate.setFont(CoeusFontFactory.getLabelFont());
        lblExpDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblExpDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6; //3
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblExpDate, gridBagConstraints);
        //Last Approval Date Start7
        lblLastApprovalDate.setText(coeusMessageResources.parseLabelKey("protocol_last_approval_date.1108"));
        lblLastApprovalDate.setFont(CoeusFontFactory.getLabelFont());
        lblLastApprovalDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblLastApprovalDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4; //3
        gridBagConstraints.insets = new Insets(5, 340, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlDetails.add(lblLastApprovalDate, gridBagConstraints);
        //Last Approval Date End7
        
        //Added to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        lblRef1.setText(coeusMessageResources.parseLabelKey("protocol_Ref_1Code.1101")+":");
        //End 02-Sep-2005
        lblRef1.setFont(CoeusFontFactory.getLabelFont());
        lblRef1.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRef1.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8; 
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblRef1, gridBagConstraints);

        
        lblType.setText("Type :");
        lblType.setFont(CoeusFontFactory.getLabelFont());
        lblType.setHorizontalAlignment(SwingConstants.RIGHT);
        lblType.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 5, 5, 130);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblType, gridBagConstraints);

        lblBillable.setText("Billable :");
        lblBillable.setFont(CoeusFontFactory.getLabelFont());
        lblBillable.setHorizontalAlignment(SwingConstants.RIGHT);
        lblBillable.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 5, 5, 30);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblBillable, gridBagConstraints);
        
        lblTitle.setText("Title :");
        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTitle.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0; //2
        gridBagConstraints.gridy = 2; //1
        gridBagConstraints.insets = new Insets(0, 0, 5, 5); //(0,5,5,5)
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        pnlDetails.add(lblTitle, gridBagConstraints);

        lblDescription.setText("Description/Keyword :");
        lblDescription.setFont(CoeusFontFactory.getLabelFont());
        lblDescription.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTitle.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0; //2
        gridBagConstraints.gridy = 3; //1
        gridBagConstraints.insets = new Insets(0, 0, 5, 5); //(0,5,5,5)
        gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
        pnlDetails.add(lblDescription, gridBagConstraints);
                
        lblApprDate.setText("Approval Date :");
        lblApprDate.setFont(CoeusFontFactory.getLabelFont());
        lblApprDate.setHorizontalAlignment(SwingConstants.RIGHT);
        lblApprDate.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4; //2
        gridBagConstraints.insets = new Insets(5, 125, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlDetails.add(lblApprDate, gridBagConstraints);

        lblFDAApplNo.setText("FDA Application No. :");
        lblFDAApplNo.setFont(CoeusFontFactory.getLabelFont());
        lblFDAApplNo.setHorizontalAlignment(SwingConstants.RIGHT);
        lblFDAApplNo.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6; //3
        gridBagConstraints.insets = new Insets(5, 335, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlDetails.add(lblFDAApplNo, gridBagConstraints);
        
        //Added to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        lblRef2.setText(coeusMessageResources.parseLabelKey("protocol_Ref_2Code.1102")+":");
        //End : 02-Sep-2005
        lblRef2.setFont(CoeusFontFactory.getLabelFont());
        lblRef2.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRef2.setHorizontalTextPosition(SwingConstants.RIGHT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9; 
        gridBagConstraints.insets = new Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(lblRef2, gridBagConstraints);
        
        txtProtocolId.setPreferredSize(new Dimension(120, 20));
        txtProtocolId.setRequestFocusEnabled(false);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlDetails.add(txtProtocolId, gridBagConstraints);

        txtApplnDate.setDocument(new LimitedPlainDocument(11));
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType != 'D'){        
        if(functionType != CoeusGuiConstants.DISPLAY_MODE || 
                functionType !=CoeusGuiConstants.AMEND_MODE){
            txtApplnDate.addFocusListener(new CustomFocusAdapter());
        }
        txtApplnDate.setPreferredSize(new Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4; //2
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlDetails.add(txtApplnDate, gridBagConstraints);

        txtExpDate.setDocument(new LimitedPlainDocument(11));
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType != 'D'){        
        if(functionType != CoeusGuiConstants.DISPLAY_MODE 
                || functionType != CoeusGuiConstants.AMEND_MODE){
            txtExpDate.addFocusListener(new CustomFocusAdapter());
        }
        txtExpDate.setPreferredSize(new Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6; //3
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlDetails.add(txtExpDate, gridBagConstraints);
        
        //Last Approval Date Start8
        txtLastApprovalDate.setDocument(new LimitedPlainDocument(11));
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType != 'D'){        
        if(functionType != CoeusGuiConstants.DISPLAY_MODE 
                || functionType != CoeusGuiConstants.AMEND_MODE){
            txtLastApprovalDate.addFocusListener(new CustomFocusAdapter());
        }
        txtLastApprovalDate.setPreferredSize(new Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4; //3
        gridBagConstraints.insets = new Insets(0, 457, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlDetails.add(txtLastApprovalDate, gridBagConstraints);
        //Last Approval Date End 8

        txtFldRef1.setDocument(new LimitedPlainDocument(50));
        txtFldRef1.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8; 
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        pnlDetails.add(txtFldRef1, gridBagConstraints);

        //txtTitle.setPreferredSize(new Dimension(120, 20));
        txtTitle.setPreferredSize(new Dimension(120, 90));
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1; //3
        gridBagConstraints.gridy = 2; //1
        gridBagConstraints.insets = new Insets(0, 0, 5, 5); 
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        
        javax.swing.JScrollPane scrPnCommentsContainer = new javax.swing.JScrollPane();
        scrPnCommentsContainer.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnCommentsContainer.setPreferredSize(new java.awt.Dimension(120, 50));
        scrPnCommentsContainer.setMinimumSize(new java.awt.Dimension(120, 50));
        scrPnCommentsContainer.setMaximumSize(new java.awt.Dimension(120, 50));
        scrPnCommentsContainer.setViewportView(txtTitle);
        pnlDetails.add(scrPnCommentsContainer, gridBagConstraints);
        
        txtArDescription.setDocument( new LimitedPlainDocument(2000) );
        //Added for GN444 issue#15 fix-start
        txtArDescription.setRows(2000);
        //Added for GN444 issue#15 fix-end
        txtArDescription.setPreferredSize(new Dimension(120, 90));
        txtArDescription.setWrapStyleWord( true );
        txtArDescription.setLineWrap(true);        
        txtArDescription.setFont( CoeusFontFactory.getNormalFont() );
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1; //3
        gridBagConstraints.gridy = 3; //1
        gridBagConstraints.insets = new Insets(0, 0, 5, 5); 
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        
        javax.swing.JScrollPane scrPnDescContainer = new javax.swing.JScrollPane();
        scrPnDescContainer.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnDescContainer.setPreferredSize(new java.awt.Dimension(120, 50));
        scrPnDescContainer.setMinimumSize(new java.awt.Dimension(120, 50));
        scrPnDescContainer.setMaximumSize(new java.awt.Dimension(120, 50));
        scrPnDescContainer.setViewportView(txtArDescription);
        pnlDetails.add(scrPnDescContainer, gridBagConstraints);

        //pnlDetails.add(txtTitle, gridBagConstraints);

        txtApprDate.setDocument(new LimitedPlainDocument(11));
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if(functionType != 'D'){        
        if(functionType != CoeusGuiConstants.DISPLAY_MODE 
                || functionType != CoeusGuiConstants.AMEND_MODE){
            txtApprDate.addFocusListener(new CustomFocusAdapter());
        }
        txtApprDate.setPreferredSize(new Dimension(120, 20));
        txtApprDate.setHorizontalAlignment(SwingConstants.LEFT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;//2
        gridBagConstraints.insets = new Insets(0, 215, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlDetails.add(txtApprDate, gridBagConstraints);

        txtFDAApplNo.setDocument(new LimitedPlainDocument(15));
        txtFDAApplNo.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6; //3
        gridBagConstraints.insets = new Insets(0, 457, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlDetails.add(txtFDAApplNo, gridBagConstraints);

        txtFldRef2.setDocument(new LimitedPlainDocument(50));
        txtFldRef2.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9; 
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        pnlDetails.add(txtFldRef2, gridBagConstraints);

        
        cmbType.setPreferredSize(new Dimension(120, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        pnlDetails.add(cmbType, gridBagConstraints);
        
        chkBillable.setHorizontalAlignment(SwingConstants.LEFT);
        chkBillable.setHorizontalTextPosition(SwingConstants.LEFT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(0, 0, 5, 5);
        pnlDetails.add(chkBillable, gridBagConstraints);
        
        //Altered by Vyjayanthi on 27/08/03 to increase the cmbStatus length
        cmbStatus.setPreferredSize(new java.awt.Dimension(260, 20));
        cmbStatus.setRequestFocusEnabled(false);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlDetails.add(cmbStatus, gridBagConstraints);
        
        pnlMain.setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10, 0, 5, 10);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlMain.add(pnlDetails, gridBagConstraints);

        pnlLocations.setBorder(new TitledBorder(new EtchedBorder(),
                "Protocol Organizations", TitledBorder.LEFT, TitledBorder.TOP,
                CoeusFontFactory.getLabelFont()));
        //code modified for coeus4.3 enhancement to set the location mode detail
        //to the location form.
//        locationForm = new ProtocolLocationForm(functionType,null,orgTypes,mdiForm);
        locationForm = new ProtocolLocationForm(locationFunctionType,null,orgTypes,mdiForm);
        locationForm.setSearchFrom(ROLODEX_SEARCH);
        pnlLocations.add(locationForm);
        //pnlLocations.setPreferredSize(new Dimension(700,200));
        pnlLocations.setPreferredSize(new Dimension(686,200));

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        //gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 0, 5, 10);
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        pnlMain.add(pnlLocations, gridBagConstraints);
        add(pnlMain);
        java.awt.Component locationFormComponent = locationForm.getFormComponents();//Added by Nadh to fix Bug#598 
        java.awt.Component[] components = {cmbType,chkBillable,txtTitle,txtArDescription, 
        txtApplnDate,//txtApprDate,txtExpDate,  //Commented by Vyjayanthi - 02/08/2004 for IRB Enhancement
        txtFDAApplNo,txtFldRef1,txtFldRef2,locationFormComponent};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
    }
    
    /** This method is used to get the location details from the <CODE>ProtocolInfoBean</CODE>
     *
     * @return Collection of <CODE>ProtocolLocationListBean</CODE> with all the location details
     * for this Protocol.
     */
    public Vector getLocationsFromProtocol(){
        Vector protLoc = new Vector();
        if(protocolInfoBean != null){
            protLoc = protocolInfoBean.getLocationLists();
            if(protLoc != null){
                ProtocolLocationListBean protLocBean = null;
                String fullAddress = "";
                String address = null;
                StringTokenizer stAddress = null;
                for(int loopIndex = 0 ; loopIndex < protLoc.size() ;
                    loopIndex++ ){
                    protLocBean = ( ProtocolLocationListBean )
                            protLoc.elementAt( loopIndex );

                    address = protLocBean.getAddress();
                    if((address != null) && (address.trim().length() > 0)){
                        stAddress = new StringTokenizer(address,"$#");
                        while(stAddress.hasMoreTokens()){
                            String addr = stAddress.nextToken();
                            fullAddress += addr;
                            fullAddress += (addr.length() > 0)? "\n" : "";
                        }
                        protLocBean.setAddress(fullAddress);
                        fullAddress = "";
                    }
                    protLoc.set(loopIndex,protLocBean);
                }
            }
        }
        return protLoc;
    }


   /**
    * Custom focus adapter which is used for text fields which consists of
    * date values. This is mainly used to format and restore the date value
    * during focus gained / focus lost of the text field.
    */
        private class CustomFocusAdapter extends FocusAdapter{
        //hols the data display Text Field
        CoeusTextField dateField;
        String strDate = "";
        String oldData = "";
        boolean temporary = false;

        public void focusGained (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))) {
                    //System.out.println("in focus gained");        
                    saveRequired = true;
                    oldData = dateField.getText();
                    String focusDate = dtUtils.restoreDate(
                            dateField.getText(),"/-:,");
                    dateField.setText(focusDate);
                    //COEUSQA-2801-Date validation message display unnecessary - IRB & IACUC
                    oldApplicationDate = oldData;
                }
            }
        }

        public void focusLost (FocusEvent fe){
            if (fe.getSource() instanceof CoeusTextField){
                dateField = (CoeusTextField)fe.getSource();
                temporary = fe.isTemporary();
                if ( (dateField.getText() != null)
                        &&  (!dateField.getText().trim().equals(""))
                        && (!temporary) ) {
                    strDate = dateField.getText();
                    String convertedDate =
                            dtUtils.formatDate(dateField.getText(), "/-:," ,
                                    "dd-MMM-yyyy");
                    //COEUSQA-2801-Date validation message display unnecessary - IRB & IACUC
                    if(convertedDate == null) {
                        convertedDate = dtUtils.restoreDate(dateField.getText(),"-");
                        if(!dtUtils.validateDate(convertedDate, "/")) {
                            convertedDate =  null;
                        }
                        
                    }
                    if (convertedDate==null){
                        //System.out.println("in focus lost");
                         String oldDate = dtUtils.restoreDate(oldData,DATE_SEPERATOR);
                         if(oldDate == null || !oldDate.equals(strDate)){
                             saveRequired = true;
                             dateField.setText(oldData);                             
                             CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                    "memMntFrm_exceptionCode.1048")); 
                             temporary = true;
                             dateField.requestFocus(); 
                        }                                               
                    }else {
                        convertedDate = dtUtils.restoreDate(dateField.getText(),"-");
                        convertedDate = dtUtils.formatDate(convertedDate, DATE_SEPERATOR , "dd-MMM-yyyy");
                        dateField.setText(convertedDate);
                        temporary = false;
                        if(!oldData.equals(convertedDate)){
                            //System.out.println("in focus lost 2");
                            saveRequired = true;
                        }else{
                            saveRequired = false;
                        }
                    }
                }
            }
        }
    }
      
    /**
     * This method is used to set the protocol number which will be used through
     * out the program for reference.
     *
     * @param protocolID String representing protocol number.
     */
    public void setProtocolNumber( String protocolID ){
        txtProtocolId.setText( protocolID );
    }
    
   // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
//    /** Setter for property parameters.
//     * @param parameters New value of property parameters.
//     */
//    public void setParameters(java.util.Vector parameters) {
//        this.parameters = parameters;
//    }
    //End 02-Sep-2005
    public void setOrgTypes(Vector orgTypes){
        this.orgTypes = orgTypes;
    }
    
    //Added by Vyjayanthi - 30/07/2004 for IRB Enhancement
    /** Approval and Expiration dates will be editable or non-editable
     * based on the parameter
     * @param enable if true, the Approval and Expiration dates will be enabled
     */
    public void enableApprovalExpirationDates(boolean enable) {
        txtApprDate.setEditable(enable);
        txtExpDate.setEditable(enable);
    }
   
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - Start
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - End
    
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - Start
    /** Set the focus traversal in Administrative Correction mode
     */
    public void setTraversalInAdminCorrection() {
        java.awt.Component[] components = {cmbType,chkBillable,txtTitle,txtArDescription,
        txtApplnDate,txtApprDate,txtExpDate,txtFDAApplNo,txtFldRef1,txtFldRef2};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
    }
    //Added by Vyjayanthi - 02/08/2004 for IRB Enhancement - End

    public char getLocationFunctionType() {
        return locationFunctionType;
    }

    public void setLocationFunctionType(char locationFunctionType) {
        this.locationFunctionType = locationFunctionType;
    }

    public char getOriginalFunctionType() {
        return originalFunctionType;
    }

    public void setOriginalFunctionType(char originalFunctionType) {
        this.originalFunctionType = originalFunctionType;
    }
   // 2562 -Last Approval Date field  editable in Administrative Correction Mode
    public void enableLastApprovalDate(boolean editable) {
       txtLastApprovalDate.setEditable(editable);
    }
}