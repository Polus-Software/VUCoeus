/*
 * @(#)ProtocolSubmissionForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on November 25, 2002, 5:56 PM
 * @version 1.0
 */

package edu.mit.coeus.iacuc.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.beans.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;

/** <CODE>ProtocolSubmissionForm</CODE> dialog is used to submit a Protocol to a Committee for
 * review. In this, user has to choose a Committee which will review the Protocol.
 * And also, user has to specify during which Schedule of the Committee  this
 * Protocol should be reviewed. Along with these details user may choose the
 * reviewers from the active members of selected Committee.
 *
 * @author prahalad
 */
public class ScheduleSubmissionForm extends CoeusDlgWindow {

    /** reference object of CommitteeSelectionForm */
    private CommitteeSelectionForm committeeSelectionForm;

    /** reference object of ScheduleSelectionForm */
    private ScheduleSelectionForm scheduleSelectionForm;

    /** reference object of ReviewerSelectionForm */
    private ReviewerSelectionForm reviewerSelectionForm;

    /** boolean value which specifies whether ScheduleSelectionForm to be
        or not. */
    private boolean showSchedule;

    /** boolean value which specifies whether ReviewerSelectionForm to be
        or not. */
    private boolean showReviewer;

    /** char which specifies the form is opened in Add mode */
    private static final char ADD_MODE = 'A';

    /** char which specifies the form is opened in Modify mode */
    private static final char MODIFY_MODE = 'M';

    /** holds string value used to specify that the record has to be inserted.*/
    private static final String INSERT_RECORD = "I";

    /** holds string value used to specify that the record has to be updated. */
    private static final String UPDATE_RECORD = "U";

    /** holds string value used to specify that the record has to be marked as
        deleted. */
    private static final String DELETE_RECORD = "D";

    /** holds character value used to specify that the record has to be saved. */
    private static final char SAVE_RECORD = 'S';

    /** holds character value used to specify that the request is to fetch the
        schedules for a specified committee */
    private static final char SCHEDULE_SELECTION = 'V';

    /** holds character value used to specify that the request is to fetch the
        reviewers for a specified schedule */
    private static final char REVIEWER_SELECTION = 'R';

    /** holds character value used to specify that the request is to fetch the
        count of protocols submitted to a schedule */
    private static final char PROTO_SUB_COUNT = 'C';

    /** char which specifies the mode in which the form is opened */
    private char functionType = ADD_MODE;

    /** collection object which holds the available protocol submission types */
    private ArrayList submissionTypes;

    /** collection object which holds the available protocol submission type
        qualifiers */
    private ArrayList typeQualifiers;

    /** collection object which holds the available protocol review types */
    private ArrayList reviewTypes;

    /** collection object which holds the available protocol reviewer types */
    private ArrayList reviewerTypes;

    /** collection object which holds the list of recommended committees where
        this particular protocol can be submitted. */
    private ArrayList committeeList;

    /** reference object of ProtocolSubmissionInfoBean which will be used to
        get and set the values of the form objects */
    private ProtocolSubmissionInfoBean submissionBean;

    /** reference object of ProtocolReviewerInfoBean which will be used to
        get and set the values of the form object */
    private ProtocolReviewerInfoBean reviewerBean;

    /** reference object of ProtocolInfoBean which is passed as argument from
        ProtocolDetailsForm to get the protocol number and sequence number */
    private ProtocolInfoBean protocolInfo;

    /** holds the protocol number extracted from protocolInfo */
    private String protocolId;

    /** holds the sequence number of protocol extracted from protocolInfo */
    private int seqNo;

    /** boolean value which specifies any changes have been made */
    private boolean saveRequired;

    /** reference object of CoeusMessageResources which will be used to get the
       messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources
            = CoeusMessageResources.getInstance();

    /** integer used as loop variable*/
    private int revIndex;

    /** boolean value which specifies whether the protocol has been successfully
       submitted or not */
    private boolean dataSaved;

    /** boolean value which specifies any changes has been made to
       reviewer table */
    private boolean reviewersModified;


    /** boolean value used in identifying existing reviewers */
    private boolean found;
    private boolean identifyReviewers = true;
    /** Creates new form <CODE>ProtocolSubmissionForm</CODE> and initializes all the
     * components used to select Committee, Schedule and Reviewers required for
     * reviewing a Protocol.
     *
     * @param parent reference to parent Frame object.
     * @param title String value used to display as title to this dialog.
     * @param modal boolean value which specifies the dialog type i.e either
     * modal or non-modal dialog.
     * @param protocolInfo <CODE>ProtocolInfoBean</CODE> object which will be passed from
     * <CODE>ProtocolDetailsForm</CODE>.
     * @throws Exception if unable to fetch any of the required details.
     */
    public ScheduleSubmissionForm(Frame parent, String title, boolean modal,
        ProtocolInfoBean protocolInfo ) throws Exception{
        super(parent, title, modal);
        this.protocolInfo = protocolInfo;
        this.protocolId = protocolInfo.getProtocolNumber();
        this.seqNo = protocolInfo.getSequenceNumber();
        initComponents();
        formatFields(true);
        displayScheduleComponents(false);
        showSchedule = true;
        getLookupDetails();
        setFormData();
    }

     //prps start
    public ScheduleSubmissionForm(Frame parent, String title, boolean modal,
        ProtocolInfoBean protocolInfo, boolean selectSchedule ) throws Exception
    {
        super(parent, title, modal);
        this.protocolInfo = protocolInfo;
        this.protocolId = protocolInfo.getProtocolNumber();
        this.seqNo = protocolInfo.getSequenceNumber();
        initComponents();
        formatFields(true);
        displayScheduleComponents(false);
        showSchedule = true;
        getLookupDetails();
        setFormData();
        btnSelectActionPerformedByDefault() ; 
    }
    
    // This routine is same as the btnSelectionActionPerformed, only thing is
    // that it doesnt accept "event" as parameter, the reason why this is written
    // is, when user selects action "Tabled", the selection of commitee is done 
    // automatically and the user shud be shown the "Select schedule screen",
    // so in order to achieve this directly jumping to "Select Schedule screen"
    // this routine is written.
      private void btnSelectActionPerformedByDefault()
      {

        // check whether the ScheduleSelectionForm should be displayed or not
        if( showSchedule ){
            if(committeeSelectionForm.isCommitteeSelected()){
                CommitteeMaintenanceFormBean committeeBean =
                    committeeSelectionForm.getSelectedCommittee();
                if(committeeBean != null){
                    txtCommitteeID.setText(committeeBean.getCommitteeId());
                    txtCommitteeName.setText(committeeBean.getCommitteeName());

                    Vector schedules
                            = getSchedules(committeeBean.getCommitteeId());
                    if(schedules != null && schedules.size() >0 ){
                        showSchedule = false;
                        showReviewer = true;
                        btnSelect.setText("Select Reviewer");
                        formatFields(false);

                        /* fetch the schedules whose status is "SCHEDULED" for
                           the selected committee and send them to
                           scheduleSelectionForm */
                        scheduleSelectionForm
                            = new ScheduleSelectionForm(new ArrayList(
                                schedules));

                        scheduleSelectionForm.setPreferredSize(
                            new java.awt.Dimension(pnlContent.getWidth()-40,
                                pnlContent.getHeight() - 40) );
                        committeeSelectionForm.setVisible(false);
                        pnlContent.add(scheduleSelectionForm,
                            BorderLayout.CENTER);
                        /* selecting the user selected schedule. this code should 
                           be after adding the component otherwise scrolling to
                           selected schedule won't happen*/
                        if( submissionBean != null){
                            String scheduleId = submissionBean.getScheduleId();
                            scheduleSelectionForm.setSelectedSchedule(
                                scheduleId);
                        }
                    }else{
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2004"));
                    }
                }
            }else{
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                                    "commBaseWin_exceptionCode.1007"));
            }
        }else if( showReviewer ){
            showReviewer();
        }
    }
      
   //prps end 
     
    
    /**
     * This method is used to fetch all the lookup details required for protocol
     * submission.
     */
    private void getLookupDetails() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/iacucProtocolSubSrvlt";

        /* connect to the database and get the Schedule Details for the given
           schedule id */
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                            connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector dataObjects = response.getDataObjects();
        if (response.isSuccessfulResponse()) {
            /* get all the lookup details required for submissionTypes,
               submissionTypeQualifiers, protocol review types and protocol
               reviewer types */
            submissionTypes = new ArrayList((Vector)dataObjects.get(0));
            typeQualifiers = new ArrayList((Vector)dataObjects.get(1));
            reviewTypes = new ArrayList((Vector)dataObjects.get(2));
            reviewerTypes = new ArrayList((Vector)dataObjects.get(3));
            if(dataObjects.size() > 4){
                /* if there are any committees whose home unit and research areas
                   matches with that of protocol then get the list of those 
                   committees */
                committeeList = new ArrayList((Vector)dataObjects.get(4));
                /* if the protocol has been submitted then get the submission
                   details from the database */
                submissionBean = (ProtocolSubmissionInfoBean)
                    dataObjects.get(5);
            }
        }else{
            String message = response.getMessage();
            throw new Exception(message);
        }

        /* determine the mode in which the form is opened by fetching the
           submission details from the database for given protocol number. if
           submission details are present then set the mode to MODIFY_MODE else
           set the form opened mode to ADD_MODE */
        if(submissionBean == null){
            functionType = ADD_MODE;
        }else{
            functionType = MODIFY_MODE;
        }
        /* create the committeeSelectionForm with the available committees */
        committeeSelectionForm = new CommitteeSelectionForm(committeeList);
        /* set the size of the committeeSelectionForm depending on the size of
           the panel where the forms will be dynamically loaded */
        committeeSelectionForm.setPreferredSize(
            new java.awt.Dimension( pnlContent.getWidth()-40
                , pnlContent.getHeight() - 40) );

        /* add the committeeSelectionForm dynamically in the Protocol Submission
           form */
        pnlContent.add(committeeSelectionForm,BorderLayout.CENTER);

    }

    /**
     * This method is used to set the form data as well as all lookup data by
     * extracting details from submissionBean.
     */
    private void setFormData(){
        ComboBoxBean comboBean;
        if( submissionTypes != null ){
            /* add a blank row in Submission Type combo box so that the default
               selection will be blank */
            cmbSubmissionType.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < submissionTypes.size();
                loopIndex++ ) {
                /* extract ComboBoxBean from the collection of submissionTypes */    
                comboBean = (ComboBoxBean)submissionTypes.get(loopIndex);
                cmbSubmissionType.addItem(comboBean);
            }
        }

        if( typeQualifiers != null ){
            /* add a blank row in Submission Type Qualifier combo box so that 
               the default selection will be blank */
            cmbTypeQualifier.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < typeQualifiers.size();
                loopIndex++ ) {
                    
                comboBean = (ComboBoxBean)typeQualifiers.get(loopIndex);
                cmbTypeQualifier.addItem(comboBean);
            }

        }

        if( reviewTypes != null ){
            /* add a blank row in Protocol Review Type combo box so that the 
               default selection will be blank */
            cmbReviewType.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < reviewTypes.size();
                loopIndex++ ) {
                comboBean = (ComboBoxBean)reviewTypes.get(loopIndex);
                cmbReviewType.addItem(comboBean);
            }
        }

        if(submissionBean != null){
            /* if the protocol has been already submitted set the previous 
               selections to all the form components */
            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    submissionBean.getSubmissionTypeCode()).toString());
            comboBean.setDescription(submissionBean.getSubmissionTypeDesc());
            
            /* get the submission type description from the given code */
            for(int typeRow = 0; typeRow < cmbSubmissionType.getItemCount();
                    typeRow++){
                if(((ComboBoxBean)cmbSubmissionType.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbSubmissionType.setSelectedIndex(typeRow);
                }
            }

            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    submissionBean.getProtocolReviewTypeCode()).toString());
            comboBean.setDescription(submissionBean.getProtocolReviewTypeDesc());

            /* get the review type description from the given code */
            for(int typeRow = 0; typeRow < cmbReviewType.getItemCount();
                    typeRow++){
                if(((ComboBoxBean)cmbReviewType.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbReviewType.setSelectedIndex(typeRow);
                }
            }

            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    submissionBean.getSubmissionQualTypeCode()).toString());
            comboBean.setDescription(submissionBean.getSubmissionQualTypeDesc());

            /* get the submission type qualifier description from the given code */
            for(int typeRow = 0; typeRow < cmbTypeQualifier.getItemCount();
                    typeRow++){
                if(((ComboBoxBean)cmbTypeQualifier.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbTypeQualifier.setSelectedIndex(typeRow);
                }
            }
            txtCommitteeID.setText(submissionBean.getCommitteeId());
            txtCommitteeName.setText(submissionBean.getCommitteeName());
            formatFields(false);
            btnOk.setEnabled(false);
            btnShowAll.setEnabled(true);
            committeeSelectionForm.setSelectedCommittee(
                submissionBean.getCommitteeId(),
                submissionBean.getCommitteeName());
        }
        saveRequired = false;

    }


    /**
     * This method is used to set the enabled status of all the components in
     * the form depending on the screen displayed.
     *
     * @param inCommitteeSelection boolean value which specifies whether the
     * current displayed form is CommitteeSelectionForm or not.
     */
    private void formatFields(boolean inCommitteeSelection){
        if(inCommitteeSelection){
            btnOk.setEnabled(false);
            btnShowAll.setEnabled(true);
            lblId.setVisible(false);
            txtCommitteeID.setVisible(false);
            lblCommName.setVisible(false);
            txtCommitteeName.setVisible(false);
        }else{
            btnOk.setEnabled(true);
            btnShowAll.setEnabled(false);
            lblId.setVisible(true);
            lblCommName.setVisible(true);
            txtCommitteeID.setVisible(true);
            txtCommitteeName.setVisible(true);
        }
    }


    /**
     * This method is used to set the visible status of the schedule components
     * the form depending on the screen displayed.
     *
     * @param show boolean value which specifies whether the
     * schedule id and schedule date components should be displayed or not.
     */
    private void displayScheduleComponents(boolean show){
        lblSchId.setVisible(show);
        lblSchDate.setVisible(show);
        txtScheduleId.setVisible(show);
        txtScheduleDate.setVisible(show);
    }

    /**
     * This method is used to get all the committees  to display in the
     * committee table of CommitteeSelectionForm
     *
     * @return committees collection of CommitteeMaintenanceFormBean
     */
    private static Vector getCommitteList() {
        /**
         * This sends the functionType as 'G' to the servlet indicating to
         * get the details of all existing committees with the required
         * information
         */

        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('G');
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
                (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;
    }

    /**
     * This method is used to get all the schedules for a given committee,
     * to display in the schedule table of ScheduleSelectionForm
     *
     * @param committeeId represents the committee whose schedules will be
     * fetched.
     * @return schedules collection of ScheduleDetailsBean
     */
    private static Vector getSchedules(String committeeId){

        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/iacucProtocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(SCHEDULE_SELECTION);
        request.setId(committeeId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
                (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;
    }

    /**
     * This method is used to get all the reviewers for a given schedule,
     * to display in the available reviewers table of ReviewerSelectionForm
     *
     * @param scheduleId represents the schedule id which is used to fetch all
     * active members of the committee for that schedule
     * @return schedules collection of ProtocolReviewerInfoBean
     */

    private static Vector getAvailableReviewers( String scheduleId ) {

        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/iacucProtocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(REVIEWER_SELECTION);
        request.setId(scheduleId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            vecBeans = (Vector)(
                (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;

    }


    /**
     * This method is used to get the number of protocols submitted to a given
     * schedule.
     *
     * @param scheduleId String representing the schedule number to which the
     * protocol submission count is to be fetched.
     *
     * @return count which represents the number of protocols submitted to the
     * specified schedule.
     */
    private static int getProtocolSubCount( String scheduleId ) {

        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/iacucProtocolSubSrvlt";
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROTO_SUB_COUNT);
        request.setId(scheduleId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        Integer count = new Integer(0);
        if (response.isSuccessfulResponse()) {
            count = (Integer)((Vector) response.getDataObjects()).elementAt(0);
        }
        if(count != null){
            return count.intValue();
        }
        return 0;
    }

    /** This method is used to check the validity of data entered by user.
     *
     * @return boolean true if all the entered data is valid else false.
     * @throws Exception Exception with the custom message will be thrown if the
     * data for any paritcular field is not valid.
     */
    private boolean validateData() throws Exception{
        /** string which holds the selected submission type description */
        String submissionType;

        /** string which holds the selected protocol review type description */
        String reviewType;

        submissionType = ((ComboBoxBean)
            cmbSubmissionType.getSelectedItem()).toString();

        reviewType =  ((ComboBoxBean)
            cmbReviewType.getSelectedItem()).toString();

        if(submissionType.trim().length() == 0){
            log(coeusMessageResources.parseMessageKey(
                "protoSubmissionFrm_exceptionCode.2000"));
            return false;
        }else if( reviewType.trim().length() == 0 ){
            log(coeusMessageResources.parseMessageKey(
                "protoSubmissionFrm_exceptionCode.2001"));
            return false;
        }else if ( !committeeSelectionForm.isCommitteeSelected() ) {
            log(coeusMessageResources.parseMessageKey(
                "commBaseWin_exceptionCode.1007"));
            return false;
        }else if ( functionType == ADD_MODE ){
            if( ( scheduleSelectionForm == null )
                    || ( !scheduleSelectionForm.isScheduleSelected() ) ){
                log(coeusMessageResources.parseMessageKey(
                        "commSchdDetFrm_exceptionCode.1026"));
                return false;
            }
        }

        return true;
    }

    /**
     * This method is used to check whether user has submitted the protocol 
     * to any other schedule than the one he selected earlier.
     *
     * @return boolean true if user changed his selection else false.
     */
    private boolean isScheduleChanged(){
        
        if( scheduleSelectionForm != null){
            ScheduleDetailsBean scheduleBean
                = scheduleSelectionForm.getSelectedSchedule();
            if(scheduleBean != null && submissionBean != null){
            /* compare schedule id where the protocol has been submitted with
               the selected schedule id */
                if(!scheduleBean.getScheduleId().equals(
                        submissionBean.getScheduleId())){
                            saveRequired = true;
                            return true;
                }
            }
        }
        return false;
    }


    /** This method returns the ProtocolSubmissionInfoBean after constructing it
     * from the selected data in the forms.
     *
     * @return submissionBean ProtocolSubmissionInfoBean with all the data
     * populated from the form objects.
     * @throws Exception Exception with custom message while constructing bean
     * value from any form object goes wrong.
     */
    private ProtocolSubmissionInfoBean getSubmissionData() throws Exception {
        /****** begin: changed on 12-Feb-03 to fix the bug for 
         reference id: GNIRB_DEF_202 */

        /* integer value used to set the protocol submission status to 
         * Submitted to Committee */
        /* Submission status code for "Sumitted to Committee" is 1*/
        final int SUBMIT_TO_COMMITTEE = 100 ;
        /** end of bug fix ref id: GNIRB_DEF_202 */
        
        /** collection object which holds all the available reviewers*/
        Vector existingReviewers;
        String selectedScheduleId="";

        if(functionType == ADD_MODE){
            submissionBean = new ProtocolSubmissionInfoBean();
            submissionBean.setAcType(INSERT_RECORD);
        }else{
            /* in modify mode add the propery change listener to the bean so that
               modifications to any of the bean values can be catched */
            submissionBean.addPropertyChangeListener(
                new PropertyChangeListener(){
                    public void propertyChange(PropertyChangeEvent pce){
                        /* if any of the bean values changes set the acType of
                           the bean to UPDATE_RECORD so that the corresponding 
                           table row will be updated */
                        submissionBean.setAcType(UPDATE_RECORD);
                    }
                }
            );
        }

        /* get the details of the selected schedule */
        if( scheduleSelectionForm != null ){
            selectedScheduleId 
                = scheduleSelectionForm.getSelectedSchedule().getScheduleId();
        }else{
            selectedScheduleId = submissionBean.getScheduleId();
        }

        ComboBoxBean comboBean
                = (ComboBoxBean)cmbSubmissionType.getSelectedItem();

        submissionBean.setProtocolNumber(protocolId);
        submissionBean.setSequenceNumber(seqNo);
        submissionBean.setSubmissionTypeCode(
            Integer.parseInt(comboBean.getCode()));

        submissionBean.setSubmissionTypeDesc(comboBean.getDescription());

        comboBean = ( ComboBoxBean ) cmbReviewType.getSelectedItem();

        submissionBean.setProtocolReviewTypeCode(
            Integer.parseInt(comboBean.getCode()));

        submissionBean.setProtocolReviewTypeDesc(comboBean.getDescription());

        comboBean = ( ComboBoxBean ) cmbTypeQualifier.getSelectedItem();
        /* as the protocol submission type qualifier selection is not mandatory
           check whether user has selected any value. if so set the selected code
           and description to the bean */
        if(comboBean.getCode().length() > 0 ){
            submissionBean.setSubmissionQualTypeCode(
                Integer.parseInt(comboBean.getCode()));
            submissionBean.setSubmissionQualTypeDesc(
                comboBean.getDescription());
        }
        /********************
         * hard coded the submission status code as 1
         ************/
         /****** begin: changed on 12-Feb-03 to fix the bug for 
           reference id: GNIRB_DEF_202 */

        submissionBean.setSubmissionStatusCode(SUBMIT_TO_COMMITTEE);
        /*** end of bug fix refID: GNIRB_DEF_202 */
        
        /* if user has changed the schedule then set all reviewers details for
           that schedule as DELETE_RECORD */
        existingReviewers = submissionBean.getProtocolReviewer();
        if( functionType == MODIFY_MODE){
            /* check whether user has changed the schedule  or deleted all the
               the existing reviewers */
            if(isScheduleChanged()
                    || ( (reviewerSelectionForm != null)
                    && (reviewerSelectionForm.getSelectedReviewers() == null ))){

                if(existingReviewers != null){
                    for( int index = 0 ; index < existingReviewers.size();
                            index++ ){
                        reviewerBean = (ProtocolReviewerInfoBean)
                            existingReviewers.get(index);
                        reviewerBean.setAcType(DELETE_RECORD);
                        existingReviewers.set(index,reviewerBean);
                    }
                }
            }
        }
        submissionBean.setScheduleId(selectedScheduleId);
        if( reviewerSelectionForm != null){

            Vector currentReviewers
                    = reviewerSelectionForm.getSelectedReviewers();
           /* loop through all reviewer table rows and construct beans and check
            * whether this reviewer is already exists or not. If not exists
            * setAcType to INSERT_RECORD and send. If exists, add property
            * change listener and set all the new data to the bean. If any
            * data is changed then the listener will fire and changes the
            * acType of the bean to UPDATE_RECORD
            */
            if(currentReviewers != null){
                int reviewerTypesSize = reviewerTypes.size();
                int currentRevSize = currentReviewers.size();
                for(int currIndex = 0; currIndex < currentRevSize; currIndex++){
                    found = false;
                    int foundIndex = -1;
                    Vector reviewerRow = (Vector)currentReviewers.get(currIndex);
                    ProtocolReviewerInfoBean newReviewerBean
                        = new ProtocolReviewerInfoBean();

                    // populate bean data
                    newReviewerBean.setProtocolNumber(protocolId);
                    newReviewerBean.setSequenceNumber(seqNo);
                    //commented as scheduleId is removed from reviewers table
                    //newReviewerBean.setScheduleId(submissionBean.getScheduleId());
                    newReviewerBean.setPersonId(
                        reviewerRow.elementAt(0).toString());
                    // get reviewType code for the selected review type
                    
                    for(int codeIndex=0; codeIndex < reviewerTypesSize;
                            codeIndex++){
                        ComboBoxBean sCode = (ComboBoxBean)
                                reviewerTypes.get(codeIndex);
                        if(sCode.getDescription().equals(
                                reviewerRow.elementAt(2).toString())){
                            newReviewerBean.setReviewerTypeCode(
                                Integer.parseInt(sCode.getCode()));
                            newReviewerBean.setReviewerTypeDesc(
                                sCode.getDescription());
                            break;
                        }
                    }

                    // loop through available reviewers
                    if(existingReviewers != null){
                        for(revIndex = 0 ; revIndex < existingReviewers.size();
                                revIndex++ ){
                            reviewerBean = (ProtocolReviewerInfoBean)
                                existingReviewers.get(revIndex);
                            // add property change listener
                            reviewerBean.addPropertyChangeListener(
                                new PropertyChangeListener(){
                                    public void propertyChange(
                                            PropertyChangeEvent pce){
                                        reviewerBean.setAcType(UPDATE_RECORD);
                                        reviewersModified = true;
                                    }
                                }
                            );
                            if( reviewerBean.getPersonId().equals(
                                    newReviewerBean.getPersonId())){
                                found  = true;
                                foundIndex = revIndex;
                                break;
                            }
                        }
                    }
                    if( !found ){
                        // new reviewer so add it to existing reviewers
                        newReviewerBean.setAcType(INSERT_RECORD);
                        newReviewerBean.setAWPersonId(null);
                        newReviewerBean.setAWReviewerTypeCode(0);
                        reviewersModified = true;
                        if(existingReviewers == null){
                            existingReviewers = new Vector();
                        }
                        existingReviewers.addElement(newReviewerBean);
                    }else{
                        /* set the selected reviewer type for the existing 
                           reviewer and update in existing reviewers list */
                        reviewerBean.setReviewerTypeCode(
                            newReviewerBean.getReviewerTypeCode());
                        if(foundIndex != -1){
                            existingReviewers.set(foundIndex, reviewerBean);
                        }

                    }


                }
                /* loop through all available reviewers and check whether any
                 * reviewer has been deleted. If yes set acType to DELETE_RECORD
                 * and send.
                 */
                if(existingReviewers != null){
                    ProtocolReviewerInfoBean existingReviewer;
                    for( int index = 0 ; index < existingReviewers.size();
                            index++ ){
                        existingReviewer = (ProtocolReviewerInfoBean)
                            existingReviewers.get(index);
                        found = false;
                        for(int currIndex = 0;
                            currIndex < currentReviewers.size(); currIndex++){
                                Vector reviewerRow
                                    = (Vector)currentReviewers.get(currIndex);
                                if( existingReviewer.getPersonId().equals(
                                        reviewerRow.elementAt(0))){
                                            found = true;
                                            break;
                                }

                        }

                        if( !found ) {
                            existingReviewer.setAcType(DELETE_RECORD);
                            reviewersModified = true;
                            existingReviewers.set(index,existingReviewer);
                        }

                    }
                }

            }
        }
        submissionBean.setProtocolReviewer(existingReviewers);
        if(reviewersModified && functionType == MODIFY_MODE){
            submissionBean.setSequenceNumber(seqNo);
            submissionBean.setAcType(UPDATE_RECORD);
        }
        return submissionBean;
    }

    /** This method is used to throw the exception with the specifed message.
     *
     * @param msg String representing custom message to display.
     * @throws Exception with specified custom message.
     */
    private static void log(String msg) throws Exception {
        throw new Exception(msg);
    }

    /** This method is used to check whether user has modified any existing
     * details.
     *
     * @return true if user modified any details, else false.
     */
    public boolean isSaveRequired(){

        if( ( reviewerSelectionForm != null ) &&
                ( reviewerSelectionForm.isSaveRequired() ) && !saveRequired ){
            saveRequired  = true;
        }
        if( (scheduleSelectionForm != null)
                 && (scheduleSelectionForm.isScheduleSelected())){
            if(functionType == ADD_MODE){
                saveRequired = true;
            }
        }
        return saveRequired;
    }

    /** This method is used to check whether the protocol has been successfully
     * submitted or not.
     *
     * @return true if the data has been successfully
     * submitted, else false.
     */
    public boolean isDataSaved(){
        return dataSaved;
    }

    /** This method is used to get the protocol details with latest sequence
     * number after updating the status.
     *
     * @return <CODE>ProtocolInfoBean</CODE> with latest sequence number and
     * with updated status information.
     */
     public ProtocolInfoBean getProtocolDetails(){

         return protocolInfo;
     }

     /** This method is used to connect to the database and send the submission
      * details for saving in to database. This method calls the private methods
      * <CODE>validateData()</CODE> and <CODE>getSubmissionData()</CODE> to validate and get the
      * submission details.
      *
      * @throws Exception with custom message thrown by either
      * of the private methods called in this method or from the server if it
      * is unable to save the submission details sent.
      */

    public void submitProtocol() throws Exception{
        if( validateData() ){
            
            /* in add mode check whether the reviewerSelectionForm is showed or
               not. If not showed, ask user whether he wants to identify the
               reviewers or not and show the reviewer selection form if he wishes.
               In Modify mode check whether there are any reviewers already selected.
               If not selected, check whether user is in ScheduleSelectionForm. 
               If yes, ask user to identify reviewers. If user is in 
               CommitteeSelectionForm and wishes to save the modifications by
               clicking 'X' button , dont ask the user to identify the reviewers
               though there are no reviewers selected.
             */
            if( functionType == ADD_MODE ) {
                if ( reviewerSelectionForm == null ){
                    identifyReviewers = true;
                }
            }else if ( functionType == MODIFY_MODE ) {
                if( ( submissionBean.getProtocolReviewer() == null ) 
                        &&   ( scheduleSelectionForm != null )
                        && ( reviewerSelectionForm == null ) ) {
                            identifyReviewers = true;
                }
            }
            
            if( identifyReviewers ){
                int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                        "protoSubmissionFrm_exceptionCode.2003"),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                if( option == CoeusOptionPane.SELECTION_NO){
                    saveSubmissionDetails();
                }else if( option == CoeusOptionPane.SELECTION_YES){
                    showReviewer();
                }
            }else{
                saveSubmissionDetails();
            }

        }
    }

    /**
     * This method is used to save the submission details to database after
     * getting confirmation from the user.
     */

    private void saveSubmissionDetails() throws Exception{
        /* setting protocol status to "Submitted to IRB" after submitting the
           protocol to a committee schedule */
        final int SUBMIT_TO_IRB = 101 ;
        if( ( submissionBean != null ) && ( scheduleSelectionForm != null ) ){
            String oldScheduleId = submissionBean.getScheduleId();
            ScheduleDetailsBean scheduleDetailsBean
                = scheduleSelectionForm.getSelectedSchedule();
            if( scheduleDetailsBean != null){
                String scheduleId = scheduleDetailsBean.getScheduleId();
                if(!oldScheduleId.equals(scheduleId)){
                    saveRequired = true;
                }
            }
        }
        if(isSaveRequired()){
            int option
                = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                        "protoSubmissionFrm_exceptionCode.2005"),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
            if(option == CoeusOptionPane.SELECTION_YES){
                ProtocolSubmissionInfoBean submissionInfoBean
                        = getSubmissionData();

                String connectTo = CoeusGuiConstants.CONNECTION_URL
                        + "/protocolActionServlet";

                RequesterBean request = new RequesterBean();
                request.setFunctionType(SAVE_RECORD);
                Vector dataObjects = new Vector();
                dataObjects.addElement(submissionInfoBean);
                  
                 if(functionType == ADD_MODE){
                    // updating protocol status to Submitted to IRB 
                    protocolInfo.setProtocolStatusCode(SUBMIT_TO_IRB);
                 }
                protocolInfo.setAcType(UPDATE_RECORD);
                protocolInfo.setVulnerableSubjectLists(null);
                protocolInfo.setLocationLists(null);
                protocolInfo.setInvestigators(null);
                protocolInfo.setAreaOfResearch(null);
                protocolInfo.setCorrespondants(null);
                protocolInfo.setFundingSources(null);
                protocolInfo.setKeyStudyPersonnel(null);
                dataObjects.addElement(protocolInfo);
                /***** end of updation for refID: GNIRB_DEF_203  */
                
                 request.setDataObjects(dataObjects);
                AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
               if (!response.isSuccessfulResponse()) {
                    throw new Exception( response.getMessage());
               }else{
                   dataSaved = true;
                   protocolInfo = (ProtocolInfoBean)
                        response.getDataObjects().elementAt(0);
                   this.dispose();

               }

            }else{
                this.dispose();
            }
        }else{
            this.dispose();
        }

    }


    /**
     * This method is used to show the ReviewerSelctionForm
     */
    private void showReviewer(){
        /* check whether user has selected any schedule before displaying
           reivewerSelectionForm */
        if(scheduleSelectionForm.isScheduleSelected()){
            boolean submitToSchedule = true;
            ScheduleDetailsBean scheduleDetailsBean
                = scheduleSelectionForm.getSelectedSchedule();
            if( scheduleDetailsBean != null){
                String scheduleId = scheduleDetailsBean.getScheduleId();
                int maxCount = scheduleDetailsBean.getMaxProtocols();
                int count = getProtocolSubCount( scheduleId );

                /* check whether schedule exceeds its maximum protocol
                   review count. if exceeds inform the user with this detail.
                   If he still wants to submit to the same schedule, let him
                   submit */
                if ( count >= maxCount  && reviewerSelectionForm == null) {
                    int option = CoeusOptionPane.showQuestionDialog(

                    coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2002"),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                    if( option == CoeusOptionPane.SELECTION_NO){
                        submitToSchedule = false;
                    }
                }

                if( submitToSchedule ){
                    Vector reviewers = getAvailableReviewers(scheduleId);
                    if(reviewers != null && reviewers.size() > 0){
                        btnSelect.setEnabled( false );
                        txtScheduleId.setText(scheduleId);
                        Date schDate = scheduleDetailsBean.getScheduleDate();
                        DateUtils dtUtils = new DateUtils();
                        String convertedDate = dtUtils.formatDate(
                                schDate.toString(),"dd-MMM-yyyy");
                        if(convertedDate != null){
                            txtScheduleDate.setText(convertedDate);
                        }
                        displayScheduleComponents(true);
                        if(submissionBean != null &&
                            submissionBean.getProtocolReviewer() != null){
                             String oldScheduleId
                                = submissionBean.getScheduleId();
                             if(oldScheduleId.equals(scheduleId)){
                                reviewerSelectionForm
                                    = new ReviewerSelectionForm(
                                    new ArrayList(
                                        submissionBean.getProtocolReviewer()));
                             }else{
                                reviewerSelectionForm
                                    = new ReviewerSelectionForm(null);
                             }
                        }else{
                            reviewerSelectionForm
                                = new ReviewerSelectionForm(null);
                        }
                        identifyReviewers = false;
                        reviewerSelectionForm.setAvailableReviewers(
                            new ArrayList(reviewers));
                        /* pass the available reivewer types and reviewers to
                           ReviewerSelectionForm */
                        reviewerSelectionForm.setReviewerTypes(reviewerTypes);
                        reviewerSelectionForm.setCommitteeId(
                            txtCommitteeID.getText());
                        reviewerSelectionForm.setPreferredSize(
                            new Dimension( pnlContent.getWidth()-40 ,
                                pnlContent.getHeight()-40) );
                        reviewerSelectionForm.setComponentSizes();
                        scheduleSelectionForm.setVisible(false);
                        pnlContent.add(reviewerSelectionForm,BorderLayout.CENTER);
                    }else{
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2006"));
                    }
                }


            }
        }else{
           CoeusOptionPane.showErrorDialog(
                coeusMessageResources.parseMessageKey(
                    "commSchdDetFrm_exceptionCode.1026"));
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        lblSubmissionType = new javax.swing.JLabel();
        cmbSubmissionType = new edu.mit.coeus.utils.CoeusComboBox();
        lblTypeQualifier = new javax.swing.JLabel();
        cmbTypeQualifier = new edu.mit.coeus.utils.CoeusComboBox();
        lblReviewType = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblCommName = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        cmbReviewType = new edu.mit.coeus.utils.CoeusComboBox();
        txtCommitteeID = new edu.mit.coeus.utils.CoeusTextField();
        txtCommitteeName = new edu.mit.coeus.utils.CoeusTextField();
        lblSchId = new javax.swing.JLabel();
        txtScheduleId = new edu.mit.coeus.utils.CoeusTextField();
        lblSchDate = new javax.swing.JLabel();
        txtScheduleDate = new edu.mit.coeus.utils.CoeusTextField();

        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        setModal(true);
        pnlMain.setLayout(new java.awt.GridBagLayout());

        lblSubmissionType.setText("Submission Type :");
        lblSubmissionType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSubmissionType.setFont(CoeusFontFactory.getLabelFont());
        lblSubmissionType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblSubmissionType, gridBagConstraints);

        cmbSubmissionType.setPreferredSize(new java.awt.Dimension(120, 20));
        cmbSubmissionType.setMaximumSize(new java.awt.Dimension(120, 20));
        cmbSubmissionType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSubmissionTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(cmbSubmissionType, gridBagConstraints);

        lblTypeQualifier.setText("Submission Type Qualifier :");
        lblTypeQualifier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTypeQualifier.setFont(CoeusFontFactory.getLabelFont());
        lblTypeQualifier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblTypeQualifier, gridBagConstraints);

        cmbTypeQualifier.setPreferredSize(new java.awt.Dimension(120, 20));
        cmbTypeQualifier.setMaximumSize(new java.awt.Dimension(120, 20));
        cmbTypeQualifier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTypeQualifierItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(cmbTypeQualifier, gridBagConstraints);

        lblReviewType.setText("Review Type :");
        lblReviewType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblReviewType.setFont(CoeusFontFactory.getLabelFont());
        lblReviewType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblReviewType, gridBagConstraints);

        lblId.setText("Committee ID :");
        lblId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblId.setFont(CoeusFontFactory.getLabelFont());
        lblId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblId, gridBagConstraints);

        lblCommName.setText("Committee Name :");
        lblCommName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCommName.setFont(CoeusFontFactory.getLabelFont());
        lblCommName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblCommName, gridBagConstraints);

        pnlContent.setLayout(new java.awt.BorderLayout());

        pnlContent.setPreferredSize(new java.awt.Dimension(10, 300));
        pnlContent.setMinimumSize(new java.awt.Dimension(0, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 0);
        pnlMain.add(pnlContent, gridBagConstraints);

        btnOk.setMnemonic('O');
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnCancel, gridBagConstraints);

        btnSelect.setMnemonic('S');
        btnSelect.setFont(CoeusFontFactory.getLabelFont());
        btnSelect.setText("Select Schedule");
        btnSelect.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnSelect, gridBagConstraints);

        btnShowAll.setMnemonic('A');
        btnShowAll.setFont(CoeusFontFactory.getLabelFont());
        btnShowAll.setText("Show All");
        btnShowAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnShowAll, gridBagConstraints);

        cmbReviewType.setPreferredSize(new java.awt.Dimension(100, 20));
        cmbReviewType.setMaximumSize(new java.awt.Dimension(100, 20));
        cmbReviewType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbReviewTypeItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        pnlMain.add(cmbReviewType, gridBagConstraints);

        txtCommitteeID.setEditable(false);
        txtCommitteeID.setMinimumSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtCommitteeID, gridBagConstraints);

        txtCommitteeName.setEditable(false);
        txtCommitteeName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCommitteeName.setMinimumSize(new java.awt.Dimension(4, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(txtCommitteeName, gridBagConstraints);

        lblSchId.setText("Schedule ID :");
        lblSchId.setFont(CoeusFontFactory.getLabelFont());
        lblSchId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblSchId, gridBagConstraints);

        txtScheduleId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtScheduleId, gridBagConstraints);

        lblSchDate.setText("Scheduled Date :");
        lblSchDate.setFont(CoeusFontFactory.getLabelFont());
        lblSchDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(lblSchDate, gridBagConstraints);

        txtScheduleDate.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        pnlMain.add(txtScheduleDate, gridBagConstraints);

        getContentPane().add(pnlMain);

        pack();
    }

    private void cmbReviewTypeItemStateChanged(java.awt.event.ItemEvent evt) {
        // if user changes the selection then set saveRequired to true
        saveRequired = true;
    }

    private void cmbTypeQualifierItemStateChanged(java.awt.event.ItemEvent evt) {
        // if user changes the selection then set saveRequired to true
        saveRequired = true;
    }

    private void cmbSubmissionTypeItemStateChanged(java.awt.event.ItemEvent evt) {
        // if user changes the selection then set saveRequired to true
        saveRequired = true;
    }

    private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt) {
        /* fetch all the available committees and send them as collection to
           committeeSelectionForm */
        committeeSelectionForm.setCommitteeList(
            new ArrayList(getCommitteList()));

    }

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {

        // check whether the ScheduleSelectionForm should be displayed or not
        if( showSchedule ){
            if(committeeSelectionForm.isCommitteeSelected()){
                CommitteeMaintenanceFormBean committeeBean =
                    committeeSelectionForm.getSelectedCommittee();
                if(committeeBean != null){
                    txtCommitteeID.setText(committeeBean.getCommitteeId());
                    txtCommitteeName.setText(committeeBean.getCommitteeName());

                    Vector schedules
                            = getSchedules(committeeBean.getCommitteeId());
                    if(schedules != null && schedules.size() >0 ){
                        showSchedule = false;
                        showReviewer = true;
                        btnSelect.setText("Select Reviewer");
                        formatFields(false);

                        /* fetch the schedules whose status is "SCHEDULED" for
                           the selected committee and send them to
                           scheduleSelectionForm */
                        scheduleSelectionForm
                            = new ScheduleSelectionForm(new ArrayList(
                                schedules));

                        scheduleSelectionForm.setPreferredSize(
                            new java.awt.Dimension(pnlContent.getWidth()-40,
                                pnlContent.getHeight() - 40) );
                        committeeSelectionForm.setVisible(false);
                        pnlContent.add(scheduleSelectionForm,
                            BorderLayout.CENTER);
                        /* selecting the user selected schedule. this code should 
                           be after adding the component otherwise scrolling to
                           selected schedule won't happen*/
                        if( submissionBean != null){
                            String scheduleId = submissionBean.getScheduleId();
                            scheduleSelectionForm.setSelectedSchedule(
                                scheduleId);
                        }
                    }else{
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2004"));
                    }
                }
            }else{
                CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(
                                    "commBaseWin_exceptionCode.1007"));
            }
        }else if( showReviewer ){
            showReviewer();
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        int option = CoeusOptionPane.SELECTION_NO;

        /* take the user confirmation for saving the details if anything has
           been modified before closing the dialog. */
        isScheduleChanged();
        if(isSaveRequired()){
            option
                = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                                            "saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
        }
        if(option == CoeusOptionPane.SELECTION_YES){
            try{
                submitProtocol();
            }catch(Exception e){
                e.printStackTrace();
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }else if(option == CoeusOptionPane.SELECTION_NO){
            saveRequired = false;
            this.dispose();
        }

    }

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {
        try{
            submitProtocol();
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }

    }


    // Variables declaration - do not modify
    private javax.swing.JButton btnShowAll;
    private javax.swing.JLabel lblSchId;
    private javax.swing.JButton btnSelect;
    private javax.swing.JLabel lblReviewType;
    private edu.mit.coeus.utils.CoeusComboBox cmbSubmissionType;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblCommName;
    private javax.swing.JLabel lblSchDate;
    private edu.mit.coeus.utils.CoeusTextField txtScheduleDate;
    private javax.swing.JPanel pnlMain;
    private edu.mit.coeus.utils.CoeusComboBox cmbTypeQualifier;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeName;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblSubmissionType;
    private edu.mit.coeus.utils.CoeusComboBox cmbReviewType;
    private edu.mit.coeus.utils.CoeusTextField txtCommitteeID;
    private javax.swing.JLabel lblTypeQualifier;
    private edu.mit.coeus.utils.CoeusTextField txtScheduleId;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JButton btnCancel;
    // End of variables declaration

}

