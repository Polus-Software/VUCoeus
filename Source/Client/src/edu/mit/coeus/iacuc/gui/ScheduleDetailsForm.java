/*
 * @(#)ScheduleDetailsForm.java  1.0
 *
 * Created on October 10, 2002, 4:20 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 19-JUN-2007
 * by Leena
 */

/* PMD check performed, and commented unused imports and variables on 27-JUNE-2007
 * by Nandkumar S N
 */

/* PMD check performed, and commented unused imports and variables on 15-JULY-2010
 * by Johncy M John
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.iacuc.controller.ProtocolMailController;
import java.text.MessageFormat;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.applet.AppletContext;
import java.net.URL;
import java.util.*;
import java.io.ByteArrayInputStream;
//import java.util.Hashtable;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.exception.*;
//import edu.mit.coeus.utils.ReportGui;
//import edu.mit.coeus.utils.CustomTagScanner;

//prps start
//import edu.mit.coeus.irb.bean.ProtocolActionsBean ;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.iacuc.bean.ScheduleDetailsBean;
import edu.mit.coeus.irb.bean.AdhocDetailsBean;
//prps end

/** This class constructs the tabbed pages required for Schedule Maintenance.
 * It includes Schedule Details, Protocol Assignment, Other Actions,
 * Attendance and Meeting information.
 *
 * @version :1.0 October 10, 2002, 4:20 PM
 * @author ravikanth
 * @version :1.1 October 17, 2002, 8.30 PM
 * @updated Subramanya
 */
public class ScheduleDetailsForm extends CoeusInternalFrame
        implements ActionListener {
    
    //main Schedule container panel.
    private JPanel pnlForm;
    // holds the saveMenu Item.
//    private CoeusMenuItem saveScheduleDetails;
    //holds the Close Menu Item.
//    private CoeusMenuItem closeScheduleDetails;
    
    //holds the CoeusMenu Item for Generate/View Report
    private CoeusMenuItem generateAgenda;
    private CoeusMenuItem viewAgenda;
    private boolean isLockReleased;
    //holds the CoeusMenuItem for Generate/View Minute Report
    private CoeusMenuItem generateMinute;
    private CoeusMenuItem viewMinute;
    
    private CoeusMenuItem submissionDetails ;
    
//    private CoeusMenuItem votingDetails;
    
    private CoeusMenuItem reviewComments;
    
//    private static final String EMPTY_STRING = "";
    
    //prps start dec 4 2003
    // to view all corresondences realted to this schedule
    private CoeusMenuItem viewCorrespondence ;
    //prps end dec 4 2003
    
    
    //prps start dec 22 2003
    private CoeusMenuItem printAdhoc ;
    //prps start dec 22 2003
    
    // added by manoj to perform protocol actions 05/09/2003
    private ProcessAction processAction = ProcessAction.getInstance();
    
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004
    /** Holds all the user rights
     */
    private HashMap hmRights;
    
    //Code for new Enhancement by Vyjayanthi on 27/08/03
    //private CoeusMenuItem revisionRequested;
    //private CoeusMenuItem expeditedApproval;
    //private CoeusMenuItem grantExemption;
    //End
    
    // Toolbar for committee
    private CoeusToolBarButton btnSaveScheduleDetails;
    private CoeusToolBarButton btnCloseScheduleDetails;
    
//    private final char ADD_MODE = 'A';
    private final char MODIFY_MODE = 'M';
    private final char DISPLAY_MODE = 'D';
    
    private final static char VALID_STATUS_CHANGE = 'T' ;
    private final char GET_XSL_STREAM_AGENDA = 'a';
    private final char GET_XSL_STREAM_MINUTES = 'L';
    //Constants for Schedule title and Frame parameters
    private final String SCHEDULE_TITLE = CoeusGuiConstants.SCHEDULE_DETAILS_TITLE;
//    private String windowName;
    
    private Vector protocolSubmissionStatus;
    private Vector protocolSubmissionTypeQualifiers;
    private Vector protocolSubmissionTypes;
    private Vector protocolSubmissionReviewTypes;
    private Vector scheduleActionTypes;
    
    //holds the agenda dailog window
//    private CoeusDlgWindow dlgAgendaView = null;
    //    private String actionDescription;
    
    //holds the schedule maintain form compoenent
    private ScheduleMaintenanceForm scheduleMaintenanceForm;
    private ScheduleActionsForm scheduleActionsForm;
    private ProtocolAssignmentForm protocolAssignmentForm;
    private CommitteeScheduleAttendanceForm commSchedAttedentsForm;
    private ScheduleMinuteMaintenance scheduleMinuteMaintenanceForm;
    private edu.mit.coeus.irb.gui.CommitteeScheduleDetailsForm commSchDtlsForm;
    private CommitteeScheduleDetailsForm iacucCommSchDtlsForm;
    private String scheduleId;
    private char functionType = DISPLAY_MODE;
    private CoeusAppletMDIForm mdiForm;
    //COEUSQA:3333 - IRB and IACUC Ability to add Attachments to Minutes and Agenda - Start
    private ScheduleAttachmentsForm scheduleAttachmentsForm;
    private static final int SCHEDULE_AGENDA_ATTACHMENTS = 1;
    private static final int SCHEDULE_MINITES_ATTACHMENTS = 2;
    private static final char IACUC_GET_SCHEDULE_ATTAHMENTS = 'n';
    private static final String ADD_BOOKMARK = "yes";
    private static final String AGENDA_ATTACH = "Agenda";
    private static final String MINUTE_ATTACH = "Minutes";
    //COEUSQA:3333 - End
    
    
    private ScheduleDetailsBean  scheduleDetails;
    JTabbedPane tbdPnScheduleDetails = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
    private boolean saveRequired;
    private Vector scheduleStatus;
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    /*
     * to indicate horizondal seperator in menu items
     */
    private final String SEPERATOR="seperator";
    
    

    private final String PROTOCOL_ACTION_SERVLET = "/IacucProtocolActionServlet";
    ProtocolSubmissionForm submissionForm;
    ProtocolSubmissionDisplayForm submissionDisplayForm;
    private ProtocolVotingForm protocolVotingForm;
    private boolean promptUser ;
    private String promptMessage ;
    private boolean followupAction ;
    private HashMap hashScheduleActions ;
    private Vector vecScheduleDetailAction ;
    private Vector vecVotingFormAction ;
    // Bug Fix 2011-start step1
    private boolean isRightExists = true;
    private boolean isValidationSuccess;
    // Bug Fix 2011-End step1
    //prps end
    
    
    //prps start - jan 16 2003
    // HashMap which has user right for generating agenda/minute/correspondence
    HashMap hashDocumentRights = null ;
    private static final String GENERATE_AGENDA = "GENERATE_AGENDA" ;
    private static final String GENERATE_MINUTE = "GENERATE_MINUTE" ;
    private final static String ACTION_RIGHT = "PERFORM_IACUC_ACTIONS_ON_PROTO";
    //prps end - jan 16 2003
    
    
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - Start
    private static final String VIEW_AGENDA = "VIEW_AGENDA";
    private static final String VIEW_MINUTES = "VIEW_MINUTES";
    private static final String MAINTAIN_MINUTES = "MAINTAIN_MINUTES";
    //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - End
    
    private BaseWindowObservable observable;
    
    private ReviewCommentsForm reviewCommentsForm = new ReviewCommentsForm(true);
    private static final String SCHEDULE_MAINTENANCE_SERVLET = "/scheduleMaintSrvlt";
    private CoeusMenuItem iacucAcknow,iacucReviewnot;
    /** Constructor used to create <CODE>ScheduleDetailsForm</CODE> with
     * specific functionType, Schedule ID and the parent component.
     * @param functionType represent the Fuction Type. Ex: 'U' / 'G'
     * @param scheduleId repsesnt the schedule ID. Eg: 123987.
     * @param mdiForm CoeusAppletMDIForm
     */
    public ScheduleDetailsForm(char functionType,String scheduleId,
            CoeusAppletMDIForm mdiForm){
        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start
        super("IACUC Schedule Details" + ( (scheduleId != null
                && scheduleId.length() > 0 ) ? " - " + scheduleId :"" ), mdiForm);
        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
        this.mdiForm = mdiForm;
        this.scheduleId = scheduleId;
        this.functionType = functionType;
        hackToGainFocus();
        observable = new BaseWindowObservable();
    }
    
    /** Constructor used to create <CODE>ScheduleDetailsForm</CODE> with
     * specified function type, Schedule ID, frameName and
     * the parent component.
     * @param functionType represent the function type. Eg: 'U' / 'G'
     * @param frameName String used to identify this <CODE>CoeusInternalFrame</CODE>
     * @param scheduleId represent the schedule ID. Eg: 123987.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>
     */
    public ScheduleDetailsForm(char functionType, String scheduleId,
            String frameName, CoeusAppletMDIForm mdiForm) {
        super(frameName + ( (scheduleId != null
                && scheduleId.length() > 0 ) ? " - " + scheduleId :"" ), mdiForm);
        this.mdiForm = mdiForm;
        this.scheduleId = scheduleId;
        this.functionType = functionType;
        hackToGainFocus();
        observable = new BaseWindowObservable();
    }
    
    /* added by Geo to gain the focus */
    /* begin Block */
    
    private void hackToGainFocus() {
        JFrame frame = new JFrame();
        frame.setLocation(-200,100);
        frame.setSize( 100, 100 );
        frame.show();
        frame.dispose();
        this.dispatchEvent(new InternalFrameEvent(this,
                InternalFrameEvent.INTERNAL_FRAME_ACTIVATED));
    }
    /* End Block */
    /** This method is used to construct and show the <CODE>ScheduleDetailForm</CODE>.
     * @throws Exception fired during the process for constrcution or display
     * for the <CODE>ScheduleMaintenanceForm</CODE>.
     */
    public void showScheduleDetailForm() throws Exception{
        
        getContentPane().add(createSchedulePanel(), BorderLayout.CENTER);
        mdiForm.getDeskTopPane().add(this);
        
        /* This will catch the window closing event and checks any data is
         * modified.If any changes are done by
         * the user the system will ask for confirmation of Saving the info.
         */
        this.addVetoableChangeListener(new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                
                if (pce.getPropertyName().equals(
                        JInternalFrame.IS_CLOSED_PROPERTY)){
                    boolean changed = ((Boolean) pce.getNewValue()).
                            booleanValue();
                    try{
                        boolean save = isSaveRequired();
                        scheduleDetails = scheduleMaintenanceForm.getFormData();
                        if(changed && save){
                            closeScheduleDetails();
                        }else if(!isLockReleased){
                            releaseUpdateLock();
                        }
                    }catch(CoeusUIException cue){
                        setVisible(true);
                        CoeusOptionPane.showDialog(cue);
                        tbdPnScheduleDetails.setSelectedIndex(cue.getTabIndex());
                        throw new PropertyVetoException(cue.getMessage(),pce);
                    }catch(CoeusException ex){
                        setVisible(true);
                        CoeusOptionPane.showDialog(new CoeusClientException(ex));
                        throw new PropertyVetoException(ex.getMessage(),pce);
                    }catch(Exception ex){
                        setVisible(true);
                        if(!(ex instanceof PropertyVetoException)){
                            CoeusOptionPane.showInfoDialog( ex.getMessage() );
                        }
                        throw new PropertyVetoException(ex.getMessage(),pce);
                    }
//                    catch(Exception ex){
//                        //releaseUpdateLock();
//                        if(!(ex instanceof PropertyVetoException)){
//                            CoeusOptionPane.showErrorDialog( ex.getMessage() );
//                        }
//                        throw new PropertyVetoException(ex.getMessage(),pce);
//                    }
                }
            }
        });
        
        setFrameMenu(scheduleDetailsEditMenu());
        setToolsMenu(null); //prps commented this line to add a tools menu so that actions can be shown there
        //prps start
        setActionsMenu(scheduleDetailsActionMenu(functionType=='M'?true:false)) ;
        //prps end
        
        
        setFrameToolBar(scheduleDetailsToolBar());
        setFrame(SCHEDULE_TITLE);
        setFrameIcon(mdiForm.getCoeusIcon());
        mdiForm.putFrame(CoeusGuiConstants.SCHEDULE_DETAILS_TITLE, scheduleId,
                functionType, this);
        
        this.setFrameIcon(mdiForm.getCoeusIcon());
        this.setVisible(true);
        coeusMessageResources = CoeusMessageResources.getInstance();
        if (scheduleMaintenanceForm != null && functionType != CoeusGuiConstants.DISPLAY_MODE) {
            scheduleMaintenanceForm.setFocusToType();
        }
        
        if(functionType==DISPLAY_MODE){
            mdiForm.getFileMenu().setSaveEnabled(false);
        }else{
            mdiForm.getFileMenu().setSaveEnabled(true);
        }
    }
    
    
    /** This method creates a panel with tab pages and other required components.
     * @return Panel with tab controls and other components.
     * @throws Exception if unable to create the form.
     */
    public JPanel createSchedulePanel()throws Exception {
        pnlForm = new JPanel();
        
        pnlForm.setLayout(new BorderLayout(10,10));
        pnlForm.add(createForm(),BorderLayout.CENTER);
        pnlForm.setPreferredSize( new Dimension( 700,600 ) );
        return pnlForm;
    }
    
    
    /** This method is used to set the Schedule status.
     * @param scheduleStatus collection instance which contains the status details.
     */
    public void setScheduleStatus(Vector scheduleStatus){
        this.scheduleStatus = scheduleStatus;
    }
    
    /**
     * This method is used to set the reference to the IRB <CODE>CommitteeScheduleDetailsForm</CODE>.
     *
     * @param commSchForm reference to <CODE>CommitteeScheduleDetailsForm</CODE>.
     */
    public void setCommitteeScheduleDetailsForm(
            edu.mit.coeus.irb.gui.CommitteeScheduleDetailsForm commSchForm) {
        commSchDtlsForm = commSchForm;
    }
    
    /* This method is used to set the reference to the IRB <CODE>CommitteeScheduleDetailsForm</CODE>.
     *
     * @param commSchForm reference to <CODE>CommitteeScheduleDetailsForm</CODE>.
     */
    public void setCommitteeScheduleDetailsForm(CommitteeScheduleDetailsForm iacucCommSchDtlsForm) {
        iacucCommSchDtlsForm = iacucCommSchDtlsForm;
    }
    
    /** This method creates the tabbed pane used in <CODE>ScheduleDetailsForm</CODE>
     * @return Tabbedpane with all the tab controls.
     * @throws Exception if unable to create the form.
     */
    public JTabbedPane createForm() throws Exception{
        
        scheduleDetails = getScheduleDetails(true);
        // create Schedule Maintenance tab
        scheduleMaintenanceForm = new ScheduleMaintenanceForm(functionType,
                scheduleDetails,mdiForm);
        scheduleMaintenanceForm.setScheduleStatus(scheduleStatus);
        
        java.awt.FlowLayout flt = new java.awt.FlowLayout();
        flt.setAlignment(FlowLayout.LEFT);
        
        JPanel pnl = new JPanel(flt);
        pnl.add(scheduleMaintenanceForm.createScheduleMaintenanceForm());
        
        JPanel pnlPrtocolAssignment = new JPanel( flt );
        protocolAssignmentForm = new ProtocolAssignmentForm(functionType);
        
        JPanel pnlOtherActions = new JPanel( flt );
        scheduleActionsForm = new ScheduleActionsForm();
        
        JPanel pnlAttendance = new JPanel( flt );
/*        commSchedAttedentsForm = new CommitteeScheduleAttendanceForm( mdiForm,
        functionType,
        scheduleDetails.getCommitteeId(),
        scheduleDetails.getAttendeesLists(),
        scheduleDetails.getAbsenteesLists() );
 */
               
        commSchedAttedentsForm = new CommitteeScheduleAttendanceForm( mdiForm,
                functionType,
                scheduleDetails.getCommitteeId(),
                scheduleId,
                scheduleDetails.getAttendeesLists(),
                scheduleDetails.getAbsenteesLists() );
        
        pnlAttendance.add( commSchedAttedentsForm );
        //panel for minutes
        JPanel pnlMinutes = new JPanel(flt);

        //COEUSQA:3333 - IRB and IACUC Ability to add Attachments to Minutes and Agenda - Start
        JPanel pnlAttachment = new JPanel( flt );
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleDetails.getScheduleId());
        scheduleAttachmentsForm = new ScheduleAttachmentsForm(scheduleAttachmentBean, functionType);
        pnlAttachment.add( scheduleAttachmentsForm );
        //COEUSQA:3333 - End     
      
        scheduleMinuteMaintenanceForm = new ScheduleMinuteMaintenance(functionType,scheduleDetails);
        scheduleMinuteMaintenanceForm.setScheduleID(scheduleId);
        
        
        //Added by Vyjayanthi for IRB Enhancement - 04/08/2004 - Start
        //Set the flag to indicate whether user has MAINTAIN_MINUTES.
        //This is done in order to enable New and Modify buttons in Minutes tab
        //and enable New, Modify and Delete buttons in Review Comments Screen
        //in Display mode, if user has MAINTAIN_MINUTES right
        if( functionType == DISPLAY_MODE ){
            scheduleMinuteMaintenanceForm.setHasMaintainMinutesRight(
                    ((Boolean)hmRights.get(MAINTAIN_MINUTES)).booleanValue());
            reviewCommentsForm.setHasMaintainMinutesRight(
                    ((Boolean)hmRights.get(MAINTAIN_MINUTES)).booleanValue());
        }
        //Added by Vyjayanthi for IRB Enhancement - 04/08/2004 - End
        
        pnlMinutes.add(scheduleMinuteMaintenanceForm.showMinutesMaintenanceForm(mdiForm));
        ((FlowLayout)pnlMinutes.getLayout()).setAlignment(FlowLayout.LEFT);
        if (scheduleDetails != null) {
            scheduleActionsForm.setScheduleActionsFormData(
                    scheduleDetails.getOtherActionsList());
            scheduleActionsForm.setFunctionType(functionType);
            protocolAssignmentForm.setProtocolAssignmentFormData(
                    scheduleDetails.getSubmissionsList());
        }
        JScrollPane jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnl );
        
        tbdPnScheduleDetails.setFont(CoeusFontFactory.getNormalFont());
        tbdPnScheduleDetails.addTab("Schedule", jscrPnTab );
        
        pnlPrtocolAssignment.add(
                protocolAssignmentForm.showProtocolAssignmentForm() );
        
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlPrtocolAssignment );
        //new protocol/OtherAction/Attendance Scree - GUI with default values
        /* modified by ravi on 27-03-2003 for fix id: 196 */
        tbdPnScheduleDetails.addTab("Protocols Submitted",  jscrPnTab );
        
        if (scheduleActionTypes != null ) {
            scheduleActionsForm.setScheduleActionsType(scheduleActionTypes);
            scheduleActionsForm.setFunctionType(functionType);
        }
        scheduleActionsForm.setScheduleID(scheduleId);
        pnlOtherActions.add(scheduleActionsForm.showScheduleActionsForm(mdiForm));
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlOtherActions );
        tbdPnScheduleDetails.addTab("Other Actions",  jscrPnTab );
        
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlAttendance );
        tbdPnScheduleDetails.addTab("Attendance", jscrPnTab );
        
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlMinutes );
        tbdPnScheduleDetails.addTab("Minutes",  jscrPnTab );
        tbdPnScheduleDetails.setSelectedIndex(0);
        
        //COEUSQA:3333 - IRB and IACUC Ability to add Attachments to Minutes and Agenda - Start
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlAttachment );
        tbdPnScheduleDetails.addTab("Attachments",  jscrPnTab );
        tbdPnScheduleDetails.setSelectedIndex(0);
        //COEUSQA:3333 - End
        
         /* This  will catch the tab change event in the tabbed pane.
          * If the user selects any other tab from the CommitteeTab without
          * saving the committee information then it will prompt the user
          * to save the committee information before shifting to the other tab.
          */
        tbdPnScheduleDetails.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ce){
                JTabbedPane pn = (JTabbedPane)ce.getSource();
                int selectedTab = pn.getSelectedIndex();
                try {
                    switch ( selectedTab ) {
                        case 0 :
                            scheduleMaintenanceForm.setDefaultFocusForComponent();
                            break;
                        case 1 :
                            protocolAssignmentForm.setDefaultFocusForComponent();
                            break;
                        case 2 :
                            scheduleActionsForm.setDefaultFocusForComponent();
                            break;
                        case 3 :
                            commSchedAttedentsForm.setDefaultFocusForComponent();
                            break;
                        case 4:
                            scheduleDetails = scheduleMaintenanceForm.getFormData();
                            if( isSaveRequired() ) {
                                saveScheduleDetails();
                            }
                            scheduleMinuteMaintenanceForm.setDefaultFocusForComponent();
                            break;
                    }
                } catch(CoeusUIException cue){
                    CoeusOptionPane.showDialog(cue);
                    tbdPnScheduleDetails.setSelectedIndex(cue.getTabIndex());
                } catch(Exception e) {
                    //e.printStackTrace();
                    tbdPnScheduleDetails.setSelectedIndex(0);
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }
        });
        
        
        return tbdPnScheduleDetails;
    }
    
    
    /**
     * This method is used to get the status of the changes made to the
     * respective Schedule Maintenance From.
     * @return boolean True if it is modified else False
     */
    private boolean isSaveRequired() {
        //Cntl+S not invoking save confirmation
        try {
             scheduleMaintenanceForm.getFormData();
        } catch(Exception ex) {
            
        }
        if(scheduleMaintenanceForm.isSaveRequired()
        || scheduleActionsForm.isSaveRequired()
        || commSchedAttedentsForm.isFormDataUpdatedFlag()
        || scheduleMinuteMaintenanceForm.isSaveRequired()){
            scheduleMaintenanceForm.setSaveRequired(false); //Added by Nadh for the Save Confirmation Bug Fix (6thDec2004)-start
            scheduleActionsForm.setSaveRequired(false);
            scheduleMinuteMaintenanceForm.setSaveRequired(false);
            //end
            saveRequired = true;
        }
        return saveRequired;
    }
    
    
    /**
     * This method is used to set the schedule ID.
     * @param scheduleId represent the schedule ID as string instance.
     */
    public void setScheduleId(String scheduleId){
        this.scheduleId = scheduleId;
    }
    
    /** This method is used to refresh the data for all the components after saving
     * information to the database.
     * @throws Exception if unable to fetch the data from the database.
     */
    public void resetFormData() throws Exception{
        scheduleDetails = getScheduleDetails();
        if(scheduleMaintenanceForm==null){
            return;
        }
        scheduleMaintenanceForm.setScheduleDetails(scheduleDetails);
        scheduleMaintenanceForm.setFormData();
        if (scheduleDetails != null ){
            scheduleActionsForm.setFunctionType('M');
            scheduleActionsForm.setScheduleActionsFormData(
                    scheduleDetails.getOtherActionsList());
            scheduleActionsForm.setSaveRequired(false);
        }
    }
    
    /** This method is used to get the details from all the form components.
     * @throws Exception if any of the form components have invalid data.
     * @return <CODE>ScheduleDetailsBean</CODE> with the data populated from the form components.
     */
    public ScheduleDetailsBean getScheduleDetails() throws Exception {
        return getScheduleDetails(false);
    }
    /** This method is used to get the Schedule detail parameter/data bean from
     * data base.
     * @return ScheduleDetailsBean contins the set of all parameter of schedule.
     * @param isModifyMode boolean true if the selected record is requested for modify, else false.
     * @throws Exception if any of the form components have invalid data.
     */
    public ScheduleDetailsBean getScheduleDetails(boolean isModifyMode) throws Exception {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        
        // connect to the database and get the Schedule Details for the given
        //schedule id
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(scheduleId);
        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start        
        request.setDataObject(CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
        // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            Vector dataObjects = response.getDataObjects();
            if ( functionType == DISPLAY_MODE ) {
                scheduleDetails = (ScheduleDetailsBean)dataObjects.get(0);
                
                //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - Start
                //To get the user rights
                hmRights = (HashMap)dataObjects.get(1);
                //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - End
                
            }else if(functionType == MODIFY_MODE ) {
                protocolSubmissionStatus = (Vector)dataObjects.get(0);
                protocolSubmissionTypeQualifiers = (Vector)dataObjects.get(1);
                protocolSubmissionTypes  = (Vector)dataObjects.get(2);
                protocolSubmissionReviewTypes = (Vector)dataObjects.get(3);
                scheduleActionTypes = (Vector)dataObjects.get(4);
                scheduleStatus = (Vector)dataObjects.get(5);
                scheduleDetails = (ScheduleDetailsBean)dataObjects.get(6);
                //prps start - jan 16 2003
                // get user right for generate agenda/minute/correspondence
//                HashMap hashDocumentRights = (HashMap)dataObjects.get(7) ;
                //prps end - jan 16 2003
                
            }
        }else if(isModifyMode && functionType == MODIFY_MODE && response.isLocked()){
            this.locked = true;
            throw new Exception(response.getMessage());
        }else if( response.getDataObject() != null){
            Object obj = response.getDataObject();
            if(obj instanceof CoeusException){
                throw (CoeusException)obj;
            }
        }else{
            throw new Exception(response.getMessage());
        }
        return scheduleDetails;
    }
    
    private boolean locked;
    
    /** This method is used to check whether the selected record is being modified by
     * any other user.
     * @return true if the selected record is locked in the database for updation by other user, else false.
     */
    public boolean isLocked(){
        return locked;
    }
    
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    public char getFunctionType(){
        return functionType;
    }
    /**
     * constructs Schedule Details edit menu
     *
     * @return JMenu Schedule details edit menu
     */
    private CoeusMenu scheduleDetailsEditMenu() {
        CoeusMenu mnuScheduleDetails = null;
        Vector fileChildren = new Vector();
        
        generateAgenda = new CoeusMenuItem("Generate Agenda",null,true,true);
        generateAgenda.setMnemonic('G');
        generateAgenda.addActionListener(this);
        
        viewAgenda = new CoeusMenuItem("View Agenda",null,true,true);
        viewAgenda.setMnemonic('V');
        viewAgenda.addActionListener(this);
        
        generateMinute = new CoeusMenuItem("Generate Minutes",null,true,true);
        generateMinute.setMnemonic('M');
        generateMinute.addActionListener(this);
        
        viewMinute = new CoeusMenuItem("View Minutes",null,true,true);
        viewMinute.setMnemonic('e');
        viewMinute.addActionListener(this);
        
        submissionDetails = new CoeusMenuItem("Submission Details",null,true,true);
        submissionDetails.setMnemonic('D');
        submissionDetails.addActionListener(this);
        
        
        reviewComments = new CoeusMenuItem("Review Comments",null,true,true);
        reviewComments.setMnemonic('R');
        reviewComments.addActionListener(this);
        
        //prps start dec 4 2003
        viewCorrespondence = new CoeusMenuItem("View All Correspondence",null,true,true);
        viewCorrespondence.setMnemonic('C');
        viewCorrespondence.addActionListener(this);
        //prps end dec 4 2003
        
        //prps start dec 22 2003
        printAdhoc = new CoeusMenuItem("Generate Correspondence",null,true,true);
        printAdhoc.setMnemonic('C');
        printAdhoc.addActionListener(this);
        //prps end dec 22 2003
        
        //prps start jan 16 2004
        if (hashDocumentRights != null) {
            generateAgenda.setEnabled(((Boolean)hashDocumentRights.get(GENERATE_AGENDA)).booleanValue()) ;
            generateMinute.setEnabled(((Boolean)hashDocumentRights.get(GENERATE_MINUTE)).booleanValue()) ;
            printAdhoc.setEnabled(((Boolean)hashDocumentRights.get(ACTION_RIGHT)).booleanValue()) ;
        }
        //prps end jan 16 2004
        
        
        //prps start feb 9 2004
        
        if (functionType == 'M') {
            generateAgenda.setEnabled(true) ;
            generateMinute.setEnabled(true) ;
        } else {
            generateAgenda.setEnabled(false) ;
            generateMinute.setEnabled(false) ;
        }
        //prps end feb 9 2004
        
        
        //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - Start
        if( functionType == DISPLAY_MODE ){
            //View Agenda will be enabled in display mode only if user has VIEW_AGENDA or GENERATE_AGENDA right
            boolean hasRight = ((Boolean)hmRights.get(VIEW_AGENDA)).booleanValue() ||
                    ((Boolean)hmRights.get(GENERATE_AGENDA)).booleanValue();
            viewAgenda.setEnabled(hasRight);
            
            //View Minutes will be enabled in display mode only if user has VIEW_MINUTES or GENERATE_MINUTES right.
            hasRight = ((Boolean)hmRights.get(VIEW_MINUTES)).booleanValue() ||
                    ((Boolean)hmRights.get(GENERATE_MINUTE)).booleanValue();
            viewMinute.setEnabled(hasRight);
            
            //The menu items View All Correspondence, Generate Correspondence,
            //Submission Details, Review Comments will always be enabled in
            //display mode irrespective of user’s rights.
            viewCorrespondence.setEnabled(true);
            printAdhoc.setEnabled(true);
            submissionDetails.setEnabled(true);
            reviewComments.setEnabled(true);
        }
        //Added by Vyjayanthi for IRB Enhancement - 03/08/2004 - End
        
        fileChildren.add( generateAgenda );
        fileChildren.add( viewAgenda );
        fileChildren.add(SEPERATOR);
        fileChildren.add( generateMinute );
        fileChildren.add( viewMinute );
        //prps start dec 4 2003
        fileChildren.add(SEPERATOR);
        fileChildren.add(viewCorrespondence) ;
        //prps end dec 4 2003
        //prps start dec 22 2003
        fileChildren.add(printAdhoc) ;
        //prps end dec 22 2003
        
        fileChildren.add(SEPERATOR);
        fileChildren.add(submissionDetails);
        fileChildren.add(reviewComments);
        mnuScheduleDetails = new CoeusMenu("Edit",null,fileChildren,true,true);
        mnuScheduleDetails.setMnemonic('E');
        return mnuScheduleDetails;
        
    }
    
    
    /**
     * Schedule Details ToolBar is a which provides the Icons for Performing
     * Save, Close operations.
     *
     * @returns JToolBar Schedule Details Toolbar
     */
    private JToolBar scheduleDetailsToolBar() {
        JToolBar tlBrScheduleDetails = new JToolBar();
        
        btnSaveScheduleDetails = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),null,
                "Save Schedule Details");
        
        btnCloseScheduleDetails = new CoeusToolBarButton(new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
                "Close Schedule Details");
        
        btnSaveScheduleDetails.addActionListener(this);
        btnCloseScheduleDetails.addActionListener(this);
        
        tlBrScheduleDetails.add(btnSaveScheduleDetails);
        tlBrScheduleDetails.addSeparator();
        tlBrScheduleDetails.add(btnCloseScheduleDetails);
        
        tlBrScheduleDetails.setFloatable(false);
        return tlBrScheduleDetails;
    }
    
    
    /** Action while firing the events for menu and toolbar items.
     * @param actionType <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent actionType) {
        
        Object actSource = actionType.getSource();
        try{
            scheduleDetails = scheduleMaintenanceForm.getFormData();
            //Commented for COEUSQA-1724 Reviewer Related changes in Premium and Lite start
//            if(actSource.equals(reviewComments)){
//                
//                CoeusOptionPane.showInfoDialog("This functionality is not yet implemented.");
//            }else
              //Commented for COEUSQA-1724 Reviewer Related changes in Premium and Lite end
            if (actSource.equals(btnSaveScheduleDetails)){
                if(isSaveRequired()){
                    saveScheduleDetails();
                }
                
            } else if (actSource.equals(btnCloseScheduleDetails)){
                closeScheduleDetails();
            } else if (actSource.equals(generateAgenda)){
                //generateReport(generateAgendaEntries()); //prps commented this on Sept 30th 2003
                //prps start - sep 30 2003
                //scheduleXMLGenerator('A') ; // Agenda
                //prps end - sep 30 2003
                customScheduleXmlGenerator('A');
                
            }else if (actSource.equals(viewAgenda)){
                AgendaViewForm viewForm = new AgendaViewForm(getViewParameters(),true);
                viewForm.showForm();
            }else if (actSource.equals(submissionDetails)){
                showSubmissionDetails() ;
            }else if( actSource.equals(generateMinute)){
                //generateReport(generateMinuteEntries());//prps commented this on oct 22nd 2003
                //prps start - oct 22 2003
                
                customScheduleXmlGenerator('M');
                //scheduleXMLGenerator('M') ; // Minutes
                //prps end - oct 22 2003
            }else if( actSource.equals(viewMinute)){
                MinuteViewForm viewForm = new MinuteViewForm(getViewParameters(),true);
                viewForm.showForm();
            }else if ( actSource.equals( reviewComments ) ) {
                showReviewComments();
                //prps start dec 4 2003
            }else if ( actSource.equals( viewCorrespondence ) ) {
                showAllCorrespondences();
            } //prps end dec 4 2003
            //prps start dec 22 2003
            else if (actSource.equals( printAdhoc )) {
                showAdhocReports() ;
            }else if(actSource.equals(iacucReviewnot) ||
                    actSource.equals(iacucAcknow)){
                int actionTypeCode = Integer.parseInt(actionType.getActionCommand());
                if(validateBeforePerformingAction(actionTypeCode)){
                    ProtocolSubmissionInfoBean submissionBean
                            = protocolAssignmentForm.getSelectedSubmissionDetails();
                    ProtocolActionsBean actionBean = new ProtocolActionsBean();
                    actionBean.setActionTypeCode(actionTypeCode);
                    actionBean.setProtocolNumber(submissionBean.getProtocolNumber());
                    actionBean.setSequenceNumber(submissionBean.getSequenceNumber());
                    actionBean.setSubmissionNumber(submissionBean.getSubmissionNumber());
                    actionBean.setScheduleId(scheduleId);
                    actionBean.setCommitteeId(submissionBean.getCommitteeId());
                    actionBean.setActionTypeDescription(actionType.getActionCommand());
                    int selRow = getTableComponent().getSelectedRow();
                    processAction(actionBean, selRow) ;
                }
            //prps end dec 22 2003
            }else{       //prps start // from here on, u decide dynamically
                Iterator it = hashScheduleActions.keySet().iterator();
                while (it.hasNext()) {
                    String actionCode = it.next().toString() ;
                    if (hashScheduleActions.get(actionCode).equals(actionType.getActionCommand())) { // first match the actioncommand to actioncode and if itz zero then display voting details
                        if (Integer.parseInt(actionCode) == 0) {
                            //coeusqa-4076 Committee Actions not allowed for SubmissionType Request to Deactivate
                            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = protocolAssignmentForm.getSelectedSubmissionDetails();
                            if(protocolSubmissionInfoBean.getSubmissionTypeCode()!=102){
                            executeAction(0, true) ;  // actioncode will change depending on the user selection, on voting form
                            }else{
                                CoeusOptionPane.showInfoDialog("Committee Actions cannot be performed if Submission type is 'Request to Deactivate'"); 
                            }
                            //coeusqa-4076 Committee Actions not allowed for SubmissionType Request to Deactivate
                        }else {
                            // added by manoj to perform protocol Actions 05/09/2003 starts
                            int actCode = Integer.parseInt(actionCode);
                            if(actCode == IacucProtocolActionsConstants.ASSIGN_TO_AGENDA
                                    || actCode == IacucProtocolActionsConstants.WITHDRAWN
                                    || actCode == IacucProtocolActionsConstants.RESPONSE_APPROVAL
                                    || actCode == IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL
                                    || actCode == IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED
                                    || actCode == IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED
                                    || actCode == IacucProtocolActionsConstants.REMOVED_FROM_AGENDA
                                    || actCode == IacucProtocolActionsConstants.RESCHEDULED
                                    || actCode == IacucProtocolActionsConstants.TABLED
                                    ||  actCode == IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT
                                    ||  actCode == IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED
                                    // COEUSQA-2666: Complete Administrative Review functionality in IACUC - Start
                                    || actCode == IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL
                                    || actCode == IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE
                                    // COEUSQA-2666: End
                                    ){
                                if(validateBeforePerformingAction(actCode)){
                                    ProtocolSubmissionInfoBean submissionBean
                                            = protocolAssignmentForm.getSelectedSubmissionDetails();
                                    String actionDescription = actionType.getActionCommand();
                                    ProtocolActionsBean actionBean = new ProtocolActionsBean();
                                    actionBean.setActionTypeCode(actCode);
                                    actionBean.setProtocolNumber(submissionBean.getProtocolNumber());
                                    actionBean.setSequenceNumber(submissionBean.getSequenceNumber());
                                    actionBean.setSubmissionNumber(submissionBean.getSubmissionNumber());
                                    actionBean.setScheduleId(scheduleId);
                                    actionBean.setCommitteeId(submissionBean.getCommitteeId());
                                    actionBean.setActionTypeDescription(actionDescription);
                                    int selRow = getTableComponent().getSelectedRow();
                                    
                                    // prps start dec 12 2003
                                    processAction(actionBean, selRow) ;
                                    //prps end dec 12 2003
                                    
                                    // prps comment dec 12 2003
                                    //protocolAssignmentForm.updateActionStatus(
                                    //    processAction.performOtherAction(actionBean,false),selRow);
                                    
                                }   //ends here
                            }else{
                                executeAction(Integer.parseInt(actionCode), false) ;
                            }
                            break ;
                        }
                    }
                }
            }
        }catch(CoeusUIException cue){
            setVisible(true);
            CoeusOptionPane.showDialog(cue);
            tbdPnScheduleDetails.setSelectedIndex(cue.getTabIndex());
        }catch(CoeusException ex){
            setVisible(true);
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }catch(Exception ex){
            setVisible(true);
            if(!(ex instanceof PropertyVetoException)){
                CoeusOptionPane.showInfoDialog( ex.getMessage() );
            }
        }
    }
    
    
    //prps start dec 12 2003
    // This method will take care of processing actions having many followup actions
    // or actions having recursive followup actions
    // since updateActionStatus method needs to be called for every action. This
    // method cannot be a part of ProcessAction Object
    private void processAction(ProtocolActionsBean actionBean, int selRow) throws Exception {
        ProtocolActionChangesBean protocolActionChangesBean ;
        actionBean.setActionTriggeredFrom(CoeusGuiConstants.IACUC_SCHEDULE_DETAIL_WINDOW);
        protocolActionChangesBean =  processAction.performOtherAction(actionBean,false) ;
        if (protocolActionChangesBean != null) {
            //send mail
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            String protocolNumber = getProtocolId(selRow);
            int sequenceNumber = getSequenceNumber(selRow);
            //Added for COEUSQA-1724: Email Notifications For All Actions In IACUC.
            ProtocolMailController mailController = new ProtocolMailController();
            synchronized(mailController) {
                mailController.sendMail(actionBean.getActionTypeCode(), protocolNumber, sequenceNumber);
            }
            
            protocolAssignmentForm.updateActionStatus(protocolActionChangesBean, selRow);
            // update the actionBean
            actionBean.setProtocolNumber(protocolActionChangesBean.getProtocolNumber()) ;
            actionBean.setScheduleId(protocolActionChangesBean.getScheduleId()) ;
            actionBean.setSequenceNumber(protocolActionChangesBean.getSequenceNumber()) ;
            actionBean.setSubmissionNumber(protocolActionChangesBean.getSubmissionNumber()) ;
            
            Vector vecActionDetails = protocolActionChangesBean.getFollowupAction() ;
            if (vecActionDetails.size()>0) {
                Vector vecSubActions = (Vector)vecActionDetails.get(0) ;
                HashMap hashUserPrompt = (HashMap)vecActionDetails.get(1) ;
                HashMap hashUserPromptFlag = (HashMap)vecActionDetails.get(2) ;
                
                if (vecSubActions.size() > 0) // perform sub actions or followup actions.
                {  // for an action there cud be multiple followup actions
                    // or one followup action action might have another sub action (recursively)
                    for (int actionCount = 0; actionCount < vecSubActions.size(); actionCount++) {
                        //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - start
                        //int actionTypeCode = ((Integer)vecSubActions.get(actionCount)).intValue() ;
                        ProtocolActionsBean followActionBean = (ProtocolActionsBean)vecSubActions.get(actionCount);
                        int actionTypeCode = followActionBean.getActionTypeCode();
                        String actionTypeDescription = followActionBean.getActionTypeDescription();
                        //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - end
                        if (hashUserPromptFlag.get(new Integer(actionTypeCode)).toString().equalsIgnoreCase("Y") ) {
                            String promptMessage = hashUserPrompt.get(new Integer(actionTypeCode)).toString() ;
                            if (CoeusOptionPane.SELECTION_YES == CoeusOptionPane.showQuestionDialog(promptMessage,
                                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES)) { // prompt the user with msg obtained from database and if the choice is Yes continue follow up action
                                actionBean.setActionTypeCode(actionTypeCode) ;
                                //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - start
                                actionBean.setActionTypeDescription(actionTypeDescription);
                               //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - end
                                processAction(actionBean, selRow) ;
                            }
                        } else { // if the prompt is flag is set to N then user will not be prompted for the followup action
                            actionBean.setActionTypeCode(actionTypeCode) ;
                            //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - start
                            actionBean.setActionTypeDescription(actionTypeDescription);
                           //Added For ISSUEID#1790 - Message display is incorrect-Follow-up Action  - end
                            processAction(actionBean, selRow) ;
                        }
                    }//end for
                    
                }// end if vec sub actions
            }// end if vecActionDetails
        }
//        else
//        {
//            CoeusOptionPane.showErrorDialog("Action "  + actionBean.getActionTypeDescription() + " failed") ;
//        }
    }
    
    //prps start dec 12 2003
    
    
    
    //prps start dec 4 2003
    
    private void showAllCorrespondences() {
        try {
            Vector vecParam = new Vector() ;
            vecParam.add(scheduleId) ;
            vecParam.add(String.valueOf(functionType)) ;
            //prps start jan 16 2004
            if (hashDocumentRights != null) {
                vecParam.add(hashDocumentRights) ;
            }
            
            //prps end jan 16 2004
            ScheduleViewAllCorrespDialog scheduleViewAllCorrespDialog
                    = new ScheduleViewAllCorrespDialog(vecParam) ;
            scheduleViewAllCorrespDialog.showForm() ;
        } catch(Exception ex) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("correspType_exceptionCode.1016")) ;
        }
    }
    
    //prps end dec 4 2003
    
    //prps start june 30th 2003
    
    private void executeAction(int actionCode, boolean showVotingForm) {
        //multiple protocol may be selected
        JTable tblProtocol = getTableComponent() ;
        
        if (validateBeforePerformingAction(actionCode)) {
            
            if (showVotingForm) {   // here show the voting details screen (this is the extra step)
                if(authorizedToPerform(actionCode)){
                    // and then depending on the action selected there perform action
                    if( isSaveRequired() ) {
                        String msg = coeusMessageResources.parseMessageKey("saveConfirmCode.1002");
                        
                        int confirm = CoeusOptionPane.DEFAULT_NO;
                        confirm = CoeusOptionPane.showQuestionDialog(msg,
                                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES );
                        
                        if( confirm == JOptionPane.YES_OPTION ) {
                            saveActiveSheet();
                        }
                        
                    }
                    int selRow = tblProtocol.getSelectedRow();
                    if( serverSideValidation(IacucProtocolActionsConstants.COMMITTEE_ACTIONS, selRow) ){
                        showVotingDetails();
                        // actionCode will get changed if user selects an action on voting screen
                        actionCode = protocolVotingForm.getActionCode() ;
                        if (actionCode == 0) {
                            return ;
                        }
                    } else {
                        return ;
                    }
                }else{
                    return;
                }
            }
            int[] selectedRows =  tblProtocol.getSelectedRows() ;
            if( selectedRows.length > 1 ) {
                if( tblProtocol.getModel() instanceof TableSorter ) {
                    ((TableSorter)tblProtocol.getModel()).setSortingRequired(false);
                }
            }
            //System.out.println("selectedRows:"+selectedRows);
            for (int rowIndex=0 ; rowIndex < selectedRows.length ; rowIndex++) {
                followupAction = false ;
                //System.out.println("calling handleActionMenuItem for index:"+rowIndex);
                handleActionMenuItem(actionCode, selectedRows[rowIndex]) ;
                //// Bug Fix 2011-start step2
                isValidationSuccess = false;
                // Bug Fix 2011-End step2
            }
            if( tblProtocol.getModel() instanceof TableSorter ) {
                ((TableSorter)tblProtocol.getModel()).setSortingRequired(true);
            }
            
        }
        // set the functiontype back to "M", so that when
        // window is closed releaseUpdateLock is called, which will realease the lock
        // Bug Fix 2011-start step3
        if(isValidationSuccess){
            functionType = MODIFY_MODE ;
        }//Bug Fix 2011-start step3
    }
    private boolean authorizedToPerform(int actionCode){
        
        String desc = (String)hashScheduleActions.get(""+actionCode);
        ProtocolActionsBean bean = new ProtocolActionsBean();
        bean.setActionTypeDescription(desc);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolActionServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('H');
        request.setDataObject(bean);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return true;
        }else{
            if(response.getDataObject() != null){
                CoeusOptionPane.showDialog(new CoeusClientException
                        ((CoeusException)response.getDataObject()));
            }else{
                CoeusOptionPane.showWarningDialog(response.getMessage());
            }
        }
        System.out.println(" Server Side Error ");
        return false;
    }
    // prps end
    
    //prps start - sep 30 2003
    
    
    private void customScheduleXmlGenerator(char mode) throws Exception{
        String connURL = CoeusGuiConstants.CONNECTION_URL +"/XMLGeneratorServlet";
        Vector dataVector = new Vector();
        Vector sendToServer = new Vector();
        byte[] stream = null;
        String startingCutomTag = null;
        String endingCutomTag = null;
        ByteArrayInputStream byteArrayInputStream=null;
        String scheduleID = null;
        String committeeId = null;
        String protoCorrespTypeDesc="";
        try{
            scheduleID =  scheduleDetails.getScheduleId();
            committeeId = scheduleDetails.getCommitteeId();
        }catch( Exception err ){
            CoeusOptionPane.showErrorDialog( err.getMessage() );
        }
        dataVector.add(0, scheduleID);
        dataVector.add(1, committeeId);
        RequesterBean request = new RequesterBean();
        request.setDataObjects(dataVector);
        if (mode == 'M') {
            request.setFunctionType(GET_XSL_STREAM_MINUTES);
        } else {
            request.setFunctionType(GET_XSL_STREAM_AGENDA);
        }
        
        AppletServletCommunicator comm = new AppletServletCommunicator(connURL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                Vector dataFromServerVec =(Vector)response.getDataObject();
                if (dataFromServerVec !=null) {
                    stream = (byte[])dataFromServerVec.get(0);// xsl stream
                    startingCutomTag = (String)dataFromServerVec.get(1);// Starting tag
                    endingCutomTag = (String)dataFromServerVec.get(2);// Ending tag
                    protoCorrespTypeDesc=(String)dataFromServerVec.get(3);//Get the protoCorresTypeDesc
                }
                
                if (stream != null) {
                    byteArrayInputStream = new ByteArrayInputStream(stream);
                }
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage());
            }
        }
        if (byteArrayInputStream != null) {
//            java.io.BufferedWriter modifiedXsl = null;
//            String readtext="";
//            java.io.DataInputStream dataInputStream = new java.io.DataInputStream(byteArrayInputStream);
            CustomTagScanner customTagScanner = new CustomTagScanner();
            CoeusVector cvCustomTags = customTagScanner.stringScan(stream,startingCutomTag,endingCutomTag);
            // check for the custom tags. If present then popup the window for the corresponsding
            //tags. If not then generate pdf without popping up the window
            if(cvCustomTags!= null && cvCustomTags.size()>0){
                ReportGui  reportGui = new ReportGui(mdiForm,protoCorrespTypeDesc);
                reportGui.setTemplateData(cvCustomTags);
                reportGui.postInitComponents();
                int action;
                try {
                    this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                    action = reportGui.displayReportGui();
                } finally {
                    this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                }
                if(action ==reportGui.CLICKED_OK){
                    try {
                        this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                        Hashtable data = reportGui.getXslData();
                        byte[] customData = customTagScanner.replaceContents(data);
                        sendToServer.add(0, scheduleID);
                        sendToServer.add(1, committeeId);
                        sendToServer.add(2, customData);
                        sendToServer.add(3, new Character(mode));
                        generatePdf(sendToServer);
                    } finally {
                        this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                }else{
                    // Do the usual action
                    //scheduleXMLGenerator('M');
                }
            }else{
                // Do the usual action
                scheduleXMLGenerator(mode);
            }
        }
    }
    
    
    private void generatePdf(Vector data) throws Exception {
//        boolean success=false;
        String connURL = CoeusGuiConstants.CONNECTION_URL +"/XMLGeneratorServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObjects(data);
        request.setFunctionType('T');
        AppletServletCommunicator comm = new AppletServletCommunicator(connURL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ) {
            String pdfURL = response.getDataObject().toString() ;
            AppletContext coeusContxt = mdiForm.getCoeusAppletContext();
            
            if (coeusContxt != null) {
                coeusContxt.showDocument( new URL(
                        CoeusGuiConstants.CONNECTION_URL + pdfURL ), "_blank" );
            } else {
                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + pdfURL ));
                try {
                    if ((response.getId().equalsIgnoreCase("Yes")) || (response.getId().equalsIgnoreCase("Y")) ) {
                        String debugXmlURL = pdfURL.substring(0,pdfURL.indexOf(".pdf"))  + ".xml" ;
                        bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + debugXmlURL ));
                    }
                } catch(Exception xmlExp) {
                    xmlExp.printStackTrace() ;
                }
            }
            
        } else {
            CoeusOptionPane.showErrorDialog(response.getMessage()) ;
        }
    }
    
    
    // makes a server side call to access newly created Agenda URL
    private void scheduleXMLGenerator(char funcType) throws Exception{
        
//        String connURL = CoeusGuiConstants.CONNECTION_URL + "/XMLGeneratorServlet";
        String connURL = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
        
        String scheduleID = null;
        String committeeId = null ;
        try{
            scheduleID =  scheduleDetails.getScheduleId();
            committeeId = scheduleDetails.getCommitteeId() ;
        }catch( Exception err ){
            CoeusOptionPane.showErrorDialog( err.getMessage() );
        }
        
        // connect to the database and get the formData
        RequesterBean request = new RequesterBean();
//        request.setId( scheduleID );
//        request.setDataObject(committeeId);
//        request.setFunctionType(funcType) ; //Agenda & Minute Report
        
        //For Streaming
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", ""+funcType);
        map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
        map.put("SCHEDULE_ID", scheduleID);
        map.put("COMMITTEE_ID", committeeId);
        map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ScheduleDocumentReader");
        //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
        int attachCount = getSchedulePDFAttachmets(funcType);
        if(attachCount > 0) {
            if(attachBookMark(funcType)) {
                map.put("addBookmark",ADD_BOOKMARK);
            }
        }
        //COEUSQA:3333 - End
        documentBean.setParameterMap(map);
        request.setDataObject(documentBean);
        request.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        //For Streaming
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ) {
            //String pdfURL = "http://coeus-dev1.mit.edu:8080/CoeusApplet/Reports/testAgenda.pdf" ;
//            String pdfURL = response.getDataObject().toString() ;
//            AppletContext coeusContxt = mdiForm.getCoeusAppletContext();
//
//                if (coeusContxt != null)
//                {
//                    coeusContxt.showDocument( new URL(
//                    CoeusGuiConstants.CONNECTION_URL + pdfURL ), "_blank" );
//                }
//                else
//                {
//                    javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                    bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + pdfURL ));
//                    try
//                    {
//                        if ((response.getId().equalsIgnoreCase("Yes")) || (response.getId().equalsIgnoreCase("Y")) )
//                        {
//                            String debugXmlURL = pdfURL.substring(0,pdfURL.indexOf(".pdf"))  + ".xml" ;
//                            bs.showDocument(new URL(CoeusGuiConstants.CONNECTION_URL + debugXmlURL ));
//                        }
//                    }
//                    catch(Exception xmlExp)
//                    {
//                        xmlExp.printStackTrace() ;
//                    }
//                }
            map = (Map)response.getDataObject();
            String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
            reportUrl = reportUrl.replace('\\', '/') ; // this is fix for Mac
            URL urlObj = new URL(reportUrl);
            URLOpener.openUrl(urlObj);
        } else {
            CoeusOptionPane.showErrorDialog(response.getMessage()) ;
        }
        
    }
    
    //prps end - sep 30 2003
    
    // makes a server side call to access newly created Agenda URL
//    private String generateAgendaEntries() throws Exception{
//
//        String connURL = CoeusGuiConstants.CONNECTION_URL +
//                "/AgendaServlet";
//
//        String scheduleID = null;
//        try{
//            scheduleID =  scheduleDetails.getScheduleId(); //getScheduleDetails().getScheduleId();
//        }catch( Exception err ){
//            CoeusOptionPane.showErrorDialog( err.getMessage() );
//        }
//        Vector agendaData  = new Vector(3,2);
//        // connect to the database and get the formData
//        RequesterBean request = new RequesterBean();
//        request.setFunctionType('N');
//        request.setId( scheduleID );
//        request.setDataObject(request);
//
//        AppletServletCommunicator comm = new AppletServletCommunicator(
//                connURL, request );
//        comm.send();
//        ResponderBean response = comm.getResponse();
//        if ( response.isSuccessfulResponse() ){
//            agendaData = (Vector)response.getDataObject();
//        }else{
//
//            if(response.getDataObject() != null){
//                Object obj = response.getDataObject();
//                if(obj instanceof CoeusException){
//                    throw (CoeusException)response.getDataObject();
//                }
//            } else{
//                throw new Exception(response.getMessage());
//            }
//        }
//        return agendaData.get( 0 ).toString() ;
//    }
    
    /**
     * This Method is used to get the parameters required to View Agenda Report List
     *
     */
    private Vector getViewParameters(){
        String scheduleID = null;
        String committeeID = null;
        String committeeName = null;
        Vector params = new Vector();
        scheduleID =  scheduleDetails.getScheduleId();
        committeeID = scheduleDetails.getCommitteeId();
        committeeName = scheduleDetails.getCommitteeName();
        params.addElement( scheduleID );
        params.addElement( committeeID );
        params.addElement( committeeName );
        return params;
    }
    
    private void showVotingDetails(){
        
        ProtocolSubmissionInfoBean submissionBean
                = protocolAssignmentForm.getSelectedSubmissionDetails();
        if(submissionBean != null){
            ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
            actionBean.setProtocolNumber(submissionBean.getProtocolNumber()) ;
            actionBean.setScheduleId(scheduleId) ;
            actionBean.setSequenceNumber(submissionBean.getSequenceNumber()) ;
            actionBean.setUpdateTimestamp(submissionBean.getUpdateTimestamp()) ;
            actionBean.setSubmissionNumber(submissionBean.getSubmissionNumber()) ;
            // Bug Fix 2011-start step7
            char mode = 'D';
            if(!isRightExists){
                mode= 'M';
            }
            
            protocolVotingForm
                    = new ProtocolVotingForm(actionBean,
                    mode,
                    vecVotingFormAction, hashScheduleActions);
            // Bug Fix 2011-end step7
            //Set modified data after updating to reflect in Minutes Tab
            if(protocolVotingForm.isSaveRequired()){
                scheduleMinuteMaintenanceForm.setMinutesData(
                        protocolVotingForm.getReviewComments());
            }
        }else{
            
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(
                    "scheduleDetFrm_exceptionCode.2200"));
        }
    }
    /**
     * Supporting method which Save committee details to database
     * @throws Exception if it is unable to save
     */
    private void saveScheduleDetails() throws Exception, CoeusUIException{
        //Modified by Vyjayanthi for IRB Enhancement
        //Added check for Display mode since minutes can be saved in Display mode if user has rights
        if(functionType == 'M' || functionType == 'D'){
            if(!scheduleMaintenanceForm.validateFormData()){
                tbdPnScheduleDetails.setSelectedIndex(0);
            }else if(!scheduleActionsForm.validateData()){
                tbdPnScheduleDetails.setSelectedIndex(2);
            }else if(!scheduleMinuteMaintenanceForm.validateData()){
                tbdPnScheduleDetails.setSelectedIndex(4);
            }else{
                scheduleDetails = scheduleMaintenanceForm.getFormData();
                if(scheduleDetails!=null){
                    scheduleDetails.setOtherActionsList(
                            scheduleActionsForm.getScheduleActionsData());
                    //update for Attedent Info Save
                    scheduleDetails.setAttendeesLists(
                            commSchedAttedentsForm.getFormData());
                    //Added For Minutes
                    scheduleDetails.setMinuteList(
                            scheduleMinuteMaintenanceForm.getScheduleMinutesData());
                    //scheduleDetails.setAcType("U");
                    
                    String connectTo = CoeusGuiConstants.CONNECTION_URL
                            + "/scheduleMaintSrvlt";
                    // connect to the database and save the formData
                    RequesterBean request = new RequesterBean();
                    request.setFunctionType('S');
                    Vector vecData = new Vector();
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_start
                    vecData.add(0, CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
                    vecData.add(1, new Character(functionType));
                    vecData.add(2, scheduleDetails);
                    // Modified for COEUSQA-2686_IACUC-CHANGES TO SCHEDULE MAINTENANCE FOR 4.4.3 RELEASE_end
                    request.setDataObjects(vecData);
                    //request.setDataObject(scheduleDetails);
                    //request.setId(""+functionType);
                    request.setId(scheduleId);
                    AppletServletCommunicator comm =
                            new AppletServletCommunicator(connectTo, request);
                    comm.send();
                    ResponderBean response = comm.getResponse();
                    Vector dataObjects;
                    if (response.isSuccessfulResponse()) {
                        dataObjects = response.getDataObjects();
                        if(dataObjects != null && dataObjects.size() > 0){
                            scheduleDetails = (ScheduleDetailsBean)
                            dataObjects.elementAt(0);
                            if (commSchDtlsForm != null ) {
                                commSchDtlsForm.performRefresh();
                            }
                            /** begin: fixed bug with id #147  */
                            /*   set the message to be displayed in status bar. RefID:#147 */
                            /*setStatusMessage(
                                coeusMessageResources.parseMessageKey(
                                    "general_saveCode.2275"));*/
                            /** end: fixed bug with id #147  */
                            
                        }
                    } else {
                        throw new Exception(response.getMessage());
                    }
                    if (scheduleDetails != null ){
                        this.saveRequired=false;
                        observable.setFunctionType( functionType );
                        observable.notifyObservers(scheduleDetails);
                        /*if(tblSchedules != null){
                            updateRow();
                        }*/
                        
                        //Modified by Vyjayanthi for IRB Enhancement to check
                        //for Display mode since minutes can be saved in Display mode if user has rights
                        if( functionType == 'D' ) {
                            scheduleMaintenanceForm.setFunctionType('D');
                        }else {
                            scheduleMaintenanceForm.setFunctionType('M');
                        }
                        scheduleMaintenanceForm.setScheduleDetails(
                                scheduleDetails);
                        
                        scheduleMaintenanceForm.setFormData();
                        scheduleMaintenanceForm.setSaveRequired(false);
                        
                        //Modified by Vyjayanthi for IRB Enhancement to check
                        //for Display mode since minutes can be saved in Display mode if user has rights
                        if( functionType == 'D' ) {
                            scheduleActionsForm.setFunctionType('D');
                        }else {
                            scheduleActionsForm.setFunctionType('M');
                        }
                        scheduleActionsForm.setSaveRequired(false);
                        scheduleActionsForm.setScheduleActionsFormData(
                                scheduleDetails.getOtherActionsList());
                        //Added For Minutes
                        
                        //Modified by Vyjayanthi for IRB Enhancement to check
                        //for Display mode since minutes can be saved in Display mode if user has rights
                        if( functionType == 'D' ){
                            scheduleMinuteMaintenanceForm.setFunctionType('D');
                        }else {
                            scheduleMinuteMaintenanceForm.setFunctionType('M');
                        }
                        
                        scheduleMinuteMaintenanceForm.setSaveRequired(false);
                        scheduleMinuteMaintenanceForm.setScheduleDetailsBean(scheduleDetails);
                        scheduleMinuteMaintenanceForm.setFormData();
                        
                        commSchedAttedentsForm.setFormData(
                                scheduleDetails.getCommitteeId(),
                                scheduleDetails.getAttendeesLists(),
                                scheduleDetails.getAbsenteesLists());
                        setVisible(true);
                        
                    }else {
                        throw new Exception(
                                coeusMessageResources.parseMessageKey(
                                "saveFail_exceptionCode.1102"));
                    }
                    
                    
                }
                
            }
        }
    }
    
    /**
     * Supporting method closes the schedule details with confirmation
     * for saving based on change parameter.
     */
    private void closeScheduleDetails() throws Exception,CoeusUIException{
        String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");
        if (isSaveRequired()) {
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_OK );
            isLockReleased=true;
            switch(confirm){
                case(JOptionPane.YES_OPTION):
                    saveScheduleDetails();
                    //Added by sharath (Bug Fix : Infinite Confirm Dialog)
                    releaseUpdateLock();
                    //  mdiForm.removeFrame(CoeusGuiConstants.SCHEDULE_DETAILS_TITLE,
                    //  scheduleId);
                    //this.dispose();
                    this.doDefaultCloseAction();
                    
                    scheduleDetails = null;
                    
                    break;
                    //Added by sharath (Bug Fix : Infinite Confirm Dialog)
                    //releaseUpdateLock();
                case(JOptionPane.NO_OPTION):
                    //Added by sharath (Bug Fix : Infinite Confirm Dialog)
                    saveRequired = false;
                    scheduleMaintenanceForm.setSaveRequired(false);
                    commSchedAttedentsForm.setFormDataUpdatedFlag(false);
                    scheduleActionsForm.setSaveRequired(false);
                    scheduleMinuteMaintenanceForm.setSaveRequired(false);
                    
                    releaseUpdateLock();
                    mdiForm.removeFrame(CoeusGuiConstants.SCHEDULE_DETAILS_TITLE,
                            scheduleId);
                    //this.dispose();
                    this.doDefaultCloseAction();
                    scheduleDetails = null;
                    //Added by sharath (Bug Fix : Infinite Confirm Dialog)
                    break;
                case(JOptionPane.CANCEL_OPTION):
                case(JOptionPane.CLOSED_OPTION):
                    throw new PropertyVetoException(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130"),null);
            }
        }else{
//            releaseUpdateLock();
            mdiForm.removeFrame(CoeusGuiConstants.SCHEDULE_DETAILS_TITLE,scheduleId);
            this.doDefaultCloseAction();
            CoeusInternalFrame frame = (CoeusInternalFrame)mdiForm.getFrame(
                    CoeusGuiConstants.COMMITTEE_FRAME_TITLE,scheduleDetails.getCommitteeId());
            scheduleDetails = null;
            if (frame != null) {
                frame.setVisible(true);
                frame.setSelected(true);
            }
        }
    }
    
    //update for RowLocking. Subramanya
    private void releaseUpdateLock(){
        try{
            if(scheduleDetails==null){
                return;
            }
            String refId = scheduleDetails.getScheduleId();
            
            if ( functionType == MODIFY_MODE ) {
                String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(refId);
                requester.setFunctionType('Z');
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                //comm.releaseUpdateLock(refId,"/scheduleMaintSrvlt");
                comm.send();
                ResponderBean res = comm.getResponse();
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            }
        }catch(Exception e){
            //e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    /** This method is called from Save Menu Item under File Menu.
     * Saves the changes made for all the tab pages in this screen.
     */
    public void saveActiveSheet() {
        try {
            if (isSaveRequired()) {//Added the condition to save the latest comments by Jobin on 2/12/2004
                saveScheduleDetails();
                isLockReleased = false; //Added by Nadh for the Save Confirmation Bug Fix (6thDec2004)
            }
        } catch(CoeusUIException cue){
            CoeusOptionPane.showDialog(cue);
            tbdPnScheduleDetails.setSelectedIndex(cue.getTabIndex());
            isLockReleased = true;//Added by Nadh for the Save Confirmation Bug Fix (6thDec2004)
        }catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    
    
    // prps start
    private CoeusMenu scheduleDetailsActionMenu(boolean enable) {
        CoeusMenu mnuProtocol = null;
        Vector fileChildren = new Vector();
   
        vecScheduleDetailAction = new Vector() ;
        vecVotingFormAction = new Vector() ;
        hashScheduleActions = new HashMap() ;
        
        vecScheduleDetailAction.add(0, ""+IacucProtocolActionsConstants.ASSIGN_TO_AGENDA) ;
        vecScheduleDetailAction.add(1, ""+IacucProtocolActionsConstants.REMOVED_FROM_AGENDA) ;
        vecScheduleDetailAction.add(2, ""+IacucProtocolActionsConstants.RESCHEDULED) ;
        vecScheduleDetailAction.add(3, ""+IacucProtocolActionsConstants.TABLED) ;
        vecScheduleDetailAction.add(4, ""+IacucProtocolActionsConstants.WITHDRAWN) ;
        vecScheduleDetailAction.add(5, "0") ;
        vecScheduleDetailAction.add(6, ""+IacucProtocolActionsConstants.RESPONSE_APPROVAL) ;
        vecScheduleDetailAction.add(7, ""+IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL) ;
        // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC - Start
        vecScheduleDetailAction.add(8, ""+IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL) ;
        vecScheduleDetailAction.add(9, ""+IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE) ;
        // COEUSQA-2666: End
        
        vecVotingFormAction.add(0, ""+IacucProtocolActionsConstants.APPROVED) ;
        vecVotingFormAction.add(1, ""+IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED) ;
        vecVotingFormAction.add(2, ""+IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED) ;
        vecVotingFormAction.add(3, ""+IacucProtocolActionsConstants.DISAPPROVED) ;
        
        hashScheduleActions.put(""+IacucProtocolActionsConstants.ASSIGN_TO_AGENDA, "Assign To Agenda") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.REMOVED_FROM_AGENDA, "Remove From Agenda") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.RESCHEDULED, "ReSchedule") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.TABLED, "Tabled") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.WITHDRAWN, "Withdraw Submission") ;
        hashScheduleActions.put("0", "Committee Actions") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.RESPONSE_APPROVAL, "Response Approval") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.DESIGNATED_REVIEW_APPROVAL, "Designated Review Approval") ;
        // Added for COEUSQA-2666: Complete Administrative Review functionality in IACUC - Start
        hashScheduleActions.put(""+IacucProtocolActionsConstants.ADMINISTRATIVE_APPROVAL, "Administrative Approval") ;
        hashScheduleActions.put(""+IacucProtocolActionsConstants.ADMINISTRATIVELY_INCOMPLETE, "Administratively Incomplete") ;
        // COEUSQA-2666: End
        //For Committee Actions
        hashScheduleActions.put(""+IacucProtocolActionsConstants.APPROVED,"Approve");
        hashScheduleActions.put(""+IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED,"Minor Revisions Required");
        hashScheduleActions.put(""+IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED,"Major Revisions Required");
        hashScheduleActions.put(""+IacucProtocolActionsConstants.DISAPPROVED,"Disapprove");
        
        iacucAcknow = new CoeusMenuItem("IACUC  Acknowledgement", null, true, true);
        iacucAcknow.setActionCommand( "" + IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT);
        iacucReviewnot = new CoeusMenuItem("IACUC Review Not Required", null, true, true);
        iacucReviewnot.setActionCommand( "" + IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED);
        
        for (int i=0 ; i< vecScheduleDetailAction.size() ; i++) {
            CoeusMenuItem scheduleAction = new CoeusMenuItem(hashScheduleActions.get(vecScheduleDetailAction.get(i)).toString(), null, true, true);
            scheduleAction.addActionListener(this);
            scheduleAction.setEnabled(enable) ;
            //To enable/disable the actions menu items in Display mode
            //depending on the right PERFORM_IACUC_ACTIONS_ON_PROTO
            //If user has right the menu items will be enabled, else disabled
            if( functionType == DISPLAY_MODE ){
                scheduleAction.setEnabled(((Boolean)hmRights.get(ACTION_RIGHT)).booleanValue());
            }
            
            fileChildren.add(scheduleAction) ;
        }
        fileChildren.insertElementAt(SEPERATOR,5);
        iacucAcknow.addActionListener(this);
        iacucReviewnot.addActionListener(this);
        if(functionType == DISPLAY_MODE && !((Boolean)hmRights.get(ACTION_RIGHT)).booleanValue()){
            iacucAcknow.setEnabled(false);
            iacucReviewnot.setEnabled(false);
        }
        fileChildren.add(iacucAcknow);
        fileChildren.add(iacucReviewnot);
        mnuProtocol = new CoeusMenu("Actions", null, fileChildren, true, true);
        mnuProtocol.setMnemonic('t');
        return mnuProtocol;
    }
    
    
    // prps added this new function
    // This function will perform the validation at server side.
    private boolean serverSideValidation(int actionCode, int rowIndex) {
        System.out.println(" ** System side validation in progress ** ") ;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_ACTION_SERVLET ;
        //functionType =  VALID_STATUS_CHANGE ;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(VALID_STATUS_CHANGE);
        
        ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
        actionBean.setActionTypeCode(actionCode) ;
        actionBean.setScheduleId(scheduleId) ;
        actionBean.setSequenceNumber(getSequenceNumber(rowIndex)) ;
        actionBean.setProtocolNumber(getProtocolId(rowIndex)) ;
        actionBean.setActionTypeDescription((String)hashScheduleActions.get(""+actionCode));
        //prps start jul 16 2003
        actionBean.setSubmissionNumber(getSubmissionNumber(rowIndex)) ;
        //prps end
        request.setDataObject(actionBean) ;
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (!response.isSuccessfulResponse()) {
            System.out.println(" ** System side validation returned false ** ") ;
            // Bug Fix 2011-start step4
            isValidationSuccess = false;
            // Bug Fix 2011-end step4
            //System.out.println("message:"+response.getMessage());
            if(response.getDataObject() != null ){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    CoeusOptionPane.showDialog(new CoeusClientException((CoeusException)obj));
                }
            }else{
                CoeusOptionPane.showErrorDialog(response.getMessage()) ; // msg will be sent from the server
            }
            return false ;
        }
        
        System.out.println(" ** System side validation returned true ** ") ;
        // Bug Fix 2011-start step5
        isRightExists = false;
        isValidationSuccess = true;
        // Bug Fix 2011-end step5
        return true ;
    }
    
    
    
    
    
    // client side validation
    private boolean validateBeforePerformingAction(int actionCode) {
        //  validation number one
        // perform actions only when user is on the Protocol Assignment tab
        if (tbdPnScheduleDetails.getComponent(1).isShowing()) // tab 1 is Protocol Assignment
        {
            System.out.println(" *********** User is on Protocol Assignment tab *************") ;
            JTable tblProtocol = getTableComponent() ;
            
            if (tblProtocol.getSelectedRow() < 0) // no rows
            {
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                        "scheduleDetFrm_exceptionCode.2201")) ;
                return false ;
            }
            
            if (tblProtocol.getSelectedRowCount() > 1 && actionCode == 0) // if more than one rows selected, voting form shud not be displayed
            {
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                        "scheduleDetFrm_exceptionCode.2206")) ;
                return false ;
            }
        } else { // display message
            
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    "scheduleDetFrm_exceptionCode.2200")) ;
            return false ;
        }
        
        return true ;
    }
    
    
    private  String getProtocolId(int rowIndex) {
        
        //Commented For PMD Check
       // JTable tblProtocol = getTableComponent() ;
//
//        String strProto = tblProtocol.getValueAt(rowIndex, 0).toString() ;
//
//        return strProto ;
        ProtocolSubmissionInfoBean subInfoBean =
                protocolAssignmentForm.getSubmissionDetails( rowIndex );
        if( subInfoBean != null ){
            return subInfoBean.getProtocolNumber();
        }
        return null;
    }
    
    private int getSequenceNumber(int rowIndex) {
        //Commented For PMD Check
        // tblProtocol = getTableComponent() ;
        
//        Vector vecSubmissionBean = scheduleDetails.getSubmissionsList() ;
//        ProtocolSubmissionInfoBean protocolSubInfoBean=(ProtocolSubmissionInfoBean)
//        vecSubmissionBean.get(rowIndex);
//
//        int seqNum = protocolSubInfoBean.getSequenceNumber() ;
//
//        return seqNum ;
        ProtocolSubmissionInfoBean subInfoBean =
                protocolAssignmentForm.getSubmissionDetails( rowIndex );
        if( subInfoBean != null ){
            return subInfoBean.getSequenceNumber();
        }
        return 0;
        
    }
    
    //prps start jul 16 2003
    
    private int getSubmissionNumber(int rowIndex) {
        //Commented For PMD Check
       // JTable tblProtocol = getTableComponent() ;
        
//        Vector vecSubmissionBean = scheduleDetails.getSubmissionsList() ;
//        ProtocolSubmissionInfoBean protocolSubInfoBean=(ProtocolSubmissionInfoBean)
//        vecSubmissionBean.get(rowIndex);
//
//        int subNum = protocolSubInfoBean.getSubmissionNumber() ;
//
//        return subNum ;
        ProtocolSubmissionInfoBean subInfoBean =
                protocolAssignmentForm.getSubmissionDetails( rowIndex );
        if( subInfoBean != null ){
            return subInfoBean.getSubmissionNumber();
        }
        return 0;
        
    }
    
    //prps end
    
    private void handleActionMenuItem(int actionCode, int rowIndex) {
        JTable tblProtocol = getTableComponent() ;
        Vector vecActionDetails ;
        if(actionCode == IacucProtocolActionsConstants.SUBMITTED_TO_IACUC)  
        {
            if (!serverSideValidation(actionCode, rowIndex)) {
                return ;
            }
            stepsMandatoryForSubmitToIACUC(actionCode, rowIndex, false);
            // get the follow up actions for submit to irb
            vecActionDetails = getFollowupActions(actionCode) ;
        } else // all other actions handle event the same way
        {
            if (!serverSideValidation(actionCode, rowIndex)) {
                return ;
            }
            //System.out.println("starting stepsMandatoryForActions");
            vecActionDetails = stepsMandatoryForActions(actionCode, rowIndex) ;
        } // end else
        //Added to take the selected row as it changes the sort order - start
        rowIndex = tblProtocol.getSelectedRow();
        //Added to take the selected row as it changes the sort order - end
        //System.out.println("subactions if any:"+vecActionDetails);
        if (vecActionDetails.size()>0) {
            Vector vecSubActions = (Vector)vecActionDetails.get(0) ;
            HashMap hashUserPrompt = (HashMap)vecActionDetails.get(1) ;
            HashMap hashUserPromptFlag = (HashMap)vecActionDetails.get(2) ;
            
            if (vecSubActions.size() > 0) {
                for (int actionCount = 0; actionCount < vecSubActions.size(); actionCount++) {
                    followupAction = true ;
                    int followUpAction = ((Integer)vecSubActions.get(actionCount)).intValue() ;
                    
                    if (hashUserPromptFlag.get(new Integer(followUpAction)).toString().equalsIgnoreCase("Y") ) {
                        promptUser = true ;
                        promptMessage = hashUserPrompt.get(new Integer(followUpAction)).toString() ;
                        //prps start dec 4 2003
                        if (CoeusOptionPane.SELECTION_YES == CoeusOptionPane.showQuestionDialog(promptMessage,
                                CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES)) { // prompt the user with msg obtained from database and if the choice is Yes continue follow up action
                            handleActionMenuItem(followUpAction, rowIndex) ;
                        }
                        //prps end dec 4 2003
                    } else { // if the prompt is flag is set to N then user will not be prompted for the followup action
                        promptUser = false ;
                        promptMessage = "" ;
                        handleActionMenuItem(followUpAction, rowIndex) ;
                    }
                }//end for
                
            }// end if vec sub actions
        }//end if
        //}
    }
    
    //prps start - dec 16 2003
    private Vector getFollowupActions(int actionCode) {
        Vector vecActionDetails = new Vector() ;
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('F');
        requester.setId( String.valueOf(actionCode));
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_ACTION_SERVLET ;
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            vecActionDetails = (Vector)response.getDataObject();
        }
        return  vecActionDetails ;
    }
    //prps end - dec 16 2003
    
//    /**
//     * This method is used to show the submission display form for the given
//     * protocol number
//     *
//     * @param protocolId String representing protocol whose submission details
//     * should be displayed.
//     */
//    private void showSubmissionDisplayForm(String protocolId) throws Exception {
//        submissionDisplayForm
//                = new ProtocolSubmissionDisplayForm(CoeusGuiConstants.getMDIForm(),
//                "Protocol Submission", true, protocolId);
//        Dimension screenSize
//                = Toolkit.getDefaultToolkit().getScreenSize();
//        Dimension dlgSize = submissionDisplayForm.getSize();
//        submissionDisplayForm.setLocation(
//                screenSize.width/2 - (dlgSize.width/2),
//                screenSize.height/2 - (dlgSize.height/2));
//        submissionDisplayForm.addKeyListener(new KeyAdapter(){
//            public void keyReleased(KeyEvent e){
//                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
//                    submissionDisplayForm.dispose();
//                }
//            }
//        });
//        submissionDisplayForm.show();
//    }
    
    
    /**
     * This method is used to ask the confirmation and save the submission details
     * before closing the dialog.
     */
    private void saveSubmissionDetails(){
        int option = CoeusOptionPane.SELECTION_NO;
        if(submissionForm.isSaveRequired()){
            option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            if(option == CoeusOptionPane.SELECTION_YES){
                try{
                    submissionForm.submitProtocol();
                }catch(Exception e){
                    CoeusOptionPane.showErrorDialog(
                            e.getMessage());
                    submissionForm.setVisible(true);
                    
                }
            }else if(option
                    == CoeusOptionPane.SELECTION_NO){
                submissionForm.dispose();
            }
        }else{
            submissionForm.dispose();
        }
        
    }
    
    private JTable getTableComponent() {
        JScrollPane jsp = (JScrollPane) protocolAssignmentForm.getComponent(0) ;
        JViewport jvp = (JViewport) jsp.getViewport() ;
        JTable tblProtocol = (JTable)jvp.getComponent(0) ;
        
        return tblProtocol ;
    }
    
    //********************************** start *****************************************//
    private Vector stepsMandatoryForActions(int actionCode, int rowIndex) {
        JTable tblProtocol = getTableComponent() ;
        //Updated For PMD Check
        String msgPrompt = "" ;
        String strTitle = "" ;
        String strDefault = "" ;
//        boolean continueAction ;
        ScheduleActionInputForm inputForm ;
        ScheduleActionApprovalForm appForm ;
        Vector vecSubAction = new Vector() ;
        Vector reviewComments = null;
        //Updated for PMD Check
        String userInput = "";
        java.sql.Date appDate = null ;
        java.sql.Date expDate = null ;
        java.sql.Date actionDate = null ;//Added by Jobin For new field(Action Date)
        strTitle = coeusMessageResources.parseMessageKey("iacucActionInputFrm_TitleCode." + actionCode + "1")
        + "-" + tblProtocol.getValueAt(rowIndex,0) ;
        msgPrompt = coeusMessageResources.parseMessageKey("iacucActionInputFrm_PromptCode." + actionCode + "2") ;
        strDefault = coeusMessageResources.parseMessageKey("iacucActionInputFrm_DefaultCode." + actionCode + "3") ;
        
        ProtocolActionsBean actionBean = new ProtocolActionsBean() ;
        actionBean.setActionTypeCode(actionCode) ;
        actionBean.setScheduleId(scheduleId) ;
        actionBean.setSequenceNumber(getSequenceNumber(rowIndex)) ;
        actionBean.setProtocolNumber(getProtocolId(rowIndex)) ;
        actionBean.setSubmissionNumber(getSubmissionNumber(rowIndex));
        
        
        if (actionCode == IacucProtocolActionsConstants.APPROVED) {
            if (followupAction) { 
                if (promptUser) {
                    msgPrompt = promptMessage ;
                    appForm = new ScheduleActionApprovalForm(mdiForm, strTitle, msgPrompt,strDefault) ;
                    appForm.setLockSchedule(false);
                    appForm.setActionBean(actionBean);
                    appForm.showForm() ;
                    userInput = appForm.getUserInput() ;
                    appDate = appForm.getApprovalDate() ;
                    expDate = appForm.getExpirationDate() ;
                    actionDate = appForm.getActionDate();
                    reviewComments = appForm.getReviewComments();
                    if (!appForm.performAction()) {
                        return vecSubAction ;
                    }
                }
            } else { // if itz not a follow up action then go with normal way of prompt the user for comments
                appForm = new ScheduleActionApprovalForm(mdiForm, strTitle, msgPrompt,strDefault) ;
                appForm.setLockSchedule(false);
                appForm.setActionBean(actionBean);
                appForm.showForm() ;
                userInput = appForm.getUserInput() ;
                appDate = appForm.getApprovalDate() ;
                expDate = appForm.getExpirationDate() ;
                actionDate = appForm.getActionDate();
                reviewComments = appForm.getReviewComments();
                actionBean.setRiskLevels(appForm.getNewOrModifiedRiskLevels());
                if (!appForm.performAction()) {
                    return vecSubAction ;
                }
            }
        } 
        
        if (actionCode == IacucProtocolActionsConstants.ASSIGN_TO_AGENDA ||
              actionCode  == IacucProtocolActionsConstants.MINOR_REVISIONS_REQUIRED ||
                actionCode  == IacucProtocolActionsConstants.MAJOR_REVISIONS_REQUIRED ||
                actionCode == IacucProtocolActionsConstants.REMOVED_FROM_AGENDA ||
                actionCode == IacucProtocolActionsConstants.RESCHEDULED ||
                actionCode == IacucProtocolActionsConstants.TABLED ||
                actionCode == IacucProtocolActionsConstants.IACUC_ACKNOWLEDGEMENT ||
                actionCode == IacucProtocolActionsConstants.IACUC_REVIEW_NOT_REQUIRED
                ) {
            if (followupAction) { // if itz a follow up action then make sure that the prompt flag is set, else u dont flash the input form
                if (promptUser) {
                    msgPrompt = promptMessage ;
                    inputForm = new ScheduleActionInputForm(mdiForm, strTitle,msgPrompt,strDefault) ;
                    inputForm.setLockSchedule(false);
                    inputForm.setActionBean(actionBean);
                    int actionId = actionBean.getActionTypeCode();
                    if(actionId != IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE
                            && actionId != IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD){
                        inputForm.setVisibility();
                    }
                    inputForm.showForm() ;
                    userInput = inputForm.getUserInput() ;
                    reviewComments = inputForm.getReviewComments();
                    actionDate = inputForm.getActionDate();
                    if (!inputForm.performAction()) {
                        return vecSubAction ;
                    }
                }
            } else { // if itz not a follow up action then go with normal way of prompt the user for comments
                
                inputForm = new ScheduleActionInputForm(mdiForm, strTitle, msgPrompt, strDefault) ;
                inputForm.setLockSchedule(false);
                inputForm.setActionBean(actionBean);
                int actionId = actionBean.getActionTypeCode();
                if(actionId != IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE
                        && actionId != IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD){
                    inputForm.setVisibility();
                }
               inputForm.showForm() ;
                userInput = inputForm.getUserInput() ;
                reviewComments = inputForm.getReviewComments();
                actionDate = inputForm.getActionDate(); 
                if (!inputForm.performAction()) {
                    return vecSubAction ;
                }
            }
        }  
        
        
        // "Submit to next schedule"
        if (actionCode == 6 ) {   // Submit to next schedule will be same as submit to IRB but the only difference
            // being u already know the commitee, and all that u need to pick is the new schedule
            stepsMandatoryForSubmitToIACUC(actionCode, rowIndex, true) ;
            System.out.println("\n*** Steps for submitting this protocol to next schedule ***\n") ;
            return new Vector() ;
        }
        
        //SubmitNextCommittee
        if (actionCode == 5) {
            stepsMandatoryForSubmitToIACUC(actionCode, rowIndex, false) ;
            System.out.println("\n*** Steps for submitting this protocol to next committee ***\n") ;
            return new Vector() ;
        }
        
        
        // if promptuser is false then user will not see the input form will go ahead and do the rest
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('t');
        requester.setId(getProtocolId(rowIndex));
        actionBean.setComments(userInput) ;
        actionBean.setApprovalDate(appDate) ;
        actionBean.setExpirationDate(expDate) ;
        actionBean.setActionDate(actionDate); //Added by Jobin for Action Date
        actionBean.setReviewComments(reviewComments);
        Vector dataObjects = new Vector();
        dataObjects.add(actionBean);
        dataObjects.add( new Boolean( false ) );
        requester.setDataObjects(dataObjects) ;
        ResponderBean response = showCustomizeWindow(requester,dataObjects);//Bijosh
        if (response == null) {
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + PROTOCOL_ACTION_SERVLET ;
            requester.setFunctionType('X');
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            response = comm.getResponse();
        } else if (response.getMessage() !=null && response.getMessage().equals("NO_TAGS")) {
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + PROTOCOL_ACTION_SERVLET ;
            requester.setFunctionType('X');
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            response = comm.getResponse();
        }
        
        String protocolNumber = getProtocolId(rowIndex);
        int sequenceNumber = getSequenceNumber(rowIndex);
        
        if (response.isSuccessfulResponse()) {
            vecSubAction = (Vector)response.getDataObject() ;
            
            //Added to display Document List - Start
            if(vecSubAction!=null && vecSubAction.size() > 3) {
                Vector docList = (Vector) vecSubAction.get(3);
                if(docList == null || docList.size() == 0) {
                    //report that action was successfull
                    CoeusOptionPane.showInfoDialog("Requested action on Protocol " + getProtocolId(rowIndex) + " completed successfully") ;
                    // get the sub actions vector
                } else{
                    //Vector docList = (Vector) vecActionDetails.get(3);
                    DocumentList documentList = new DocumentList(CoeusGuiConstants.getMDIForm(),true);
                    documentList.setProtocolNumber(getProtocolId(rowIndex));
                    documentList.setMenuAction((String)hashScheduleActions.get(""+actionCode));
                    //Added for Coeus4.3 enhancement - Email Notification - start
                    documentList.setActionBean(actionBean);
                    //Added for Coeus4.3 enhancement - Email Notification - end
                    documentList.loadForm(docList);
                    documentList.display();
                }
                protocolAssignmentForm.updateActionStatus(
                        (ProtocolActionChangesBean) vecSubAction.elementAt(4) ,rowIndex);
            } else {
                //report that action was successfull
                CoeusOptionPane.showInfoDialog("Requested action on Protocol " + getProtocolId(rowIndex) + " completed successfully") ;
                CoeusOptionPane.showErrorDialog(response.getMessage());
            }
            //Added to display Document List - End
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            
            ProtocolMailController mailController = new ProtocolMailController();
            synchronized(mailController) {
                mailController.sendMail(actionBean.getActionTypeCode(), protocolNumber, sequenceNumber);
            }
            //COEUSDEV-75:End
            // set the status in GUI
            // hashScheduleActions will have the string names to be set
            // get that using the actionCode (which is the key)
            //tblProtocol.setValueAt(hashScheduleActions.get(String.valueOf(actionCode)).toString(), rowIndex, 5 ) ;
            
            /** Commented by Vyjayanthi
             * as updation is done using updateActionStatus method at line 1617 */
            
//            int indexValue = Integer.parseInt( ( String ) tblProtocol.getValueAt(
//            rowIndex,tblProtocol.getColumnCount() -1 ));
//            ((DefaultTableModel)tblProtocol.getModel()).setValueAt(
//            hashScheduleActions.get(String.valueOf(actionCode)).toString(), indexValue, 5 ) ;
            //System.out.println("updated the row successfully for row:"+ rowIndex);
        } else { // report that action was not successfull
            CoeusOptionPane.showWarningDialog("Requested action on Protocol " + getProtocolId(rowIndex) + " failed") ;
        }
        
        
        
        return vecSubAction ;
        
    }// end steps mandatory for actions
    //*********************************** end ****************************************//
    //Bijosh
    private ResponderBean showCustomizeWindow(RequesterBean request,Vector data) {
        try {
            Vector dataObjects = data;
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + PROTOCOL_ACTION_SERVLET ;
//            Vector sendToServer = new Vector();
            byte[] stream = null;
            String startingCutomTag = null;
            String endingCutomTag = null;
            ByteArrayInputStream byteArrayInputStream=null;
//            String scheduleID = null;
//            String committeeId = null;
            String protoCorrespTypeDesc= null;
            // int selRow =  tblAdhocList.getSelectedRow() ;
            //this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            AppletServletCommunicator comm = new AppletServletCommunicator(
                    connectTo, request );
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response!= null){
                if(response.isSuccessfulResponse()){
                    Vector dataFromServerVec =(Vector)response.getDataObject();
                    if (dataFromServerVec !=null) {
                        stream = (byte[])dataFromServerVec.get(0);// xsl stream
                        startingCutomTag = (String)dataFromServerVec.get(1);// Starting tag
                        endingCutomTag = (String)dataFromServerVec.get(2);// Ending tag
                        protoCorrespTypeDesc=(String)dataFromServerVec.get(3);//Get the protoCorresTypeDesc
                    }
                    if (stream != null) {
                        byteArrayInputStream = new ByteArrayInputStream(stream);
                    }
                }else {
                    if (response == null) {
                        response.setMessage("NO_TAGS");
                        return response;
                    }
                    if (response.getMessage().equals("NO_TAGS")) {
                        return response;
                    }
                    CoeusOptionPane.showErrorDialog(response.getMessage());
                }
            }
            ResponderBean responderBean = null;
            if (byteArrayInputStream != null) {
//                java.io.BufferedWriter modifiedXsl = null;
//                String readtext="";
//                java.io.DataInputStream dataInputStream = new java.io.DataInputStream(byteArrayInputStream);
                CustomTagScanner customTagScanner = new CustomTagScanner();
                CoeusVector cvCustomTags = customTagScanner.stringScan(stream,startingCutomTag,endingCutomTag);
                // check for the custom tags. If present then popup the window for the corresponsding
                //tags. If not then generate pdf without popping up the window
                if(cvCustomTags!= null && cvCustomTags.size()>0){
                    ReportGui  reportGui = new ReportGui(CoeusGuiConstants.getMDIForm(),protoCorrespTypeDesc);
                    reportGui.setTemplateData(cvCustomTags);
                    reportGui.postInitComponents();
                    int action;
                    try {
                        this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                        action = reportGui.displayReportGui();
                    } finally {
                        this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    }
                    if(action ==reportGui.CLICKED_OK){
                        try {
                            this.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                            Hashtable htData = reportGui.getXslData();
                            byte[] customData = customTagScanner.replaceContents(htData);
                            //adhocDetailsBean.setFormId(tblAdhocList.getValueAt(selRow, 0).toString()) ;
                            // adhocDetailsBean.setDescription(tblAdhocList.getValueAt(selRow, 1).toString()) ;
                            dataObjects.add(customData);
                            request.setFunctionType('x');
                            request.setDataObjects(dataObjects);
                            responderBean= generatePdfForTags(request);
                        } finally {
                            this.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                        }
                    }else{
                        // Do the usual action
                        //scheduleXMLGenerator('M');
                    }
                }else{
                    // Do the usual action
                    // showDocument();
                }
            }
            return responderBean;
        } catch (Exception e) {
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
            return null;
        }
    }
    
    
    private ResponderBean generatePdfForTags(RequesterBean request) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + PROTOCOL_ACTION_SERVLET ;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
    }
    
    
    
    
    private void stepsMandatoryForSubmitToIACUC(int actionCode, int rowIndex, boolean skipCommiteeScreen) {
        try {
            String protocolId = getProtocolId(rowIndex) ;
            ProtocolInfoBean protocolInfo = getProtocolDetails(protocolId) ;
            
            if((protocolId != null) && (protocolId.trim().length() > 0)) {

                submissionForm = new ProtocolSubmissionForm(
                        CoeusGuiConstants.getMDIForm(),
                        "Protocol Submission", true,protocolInfo);
                Dimension screenSize
                        = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension dlgSize = submissionForm.getSize();
                submissionForm.setLocation(
                        screenSize.width/2 - (dlgSize.width/2),
                        screenSize.height/2 - (dlgSize.height/2));
                
                submissionForm.setDefaultCloseOperation(
                        JDialog.DO_NOTHING_ON_CLOSE);
                
                submissionForm.addWindowListener(
                        new WindowAdapter(){
                    public void windowClosing(WindowEvent we){
                        saveSubmissionDetails();
                    }
                });
                //To be confirmed
                submissionForm.addKeyListener(new KeyAdapter(){
                    public void keyReleased(KeyEvent e){
                        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                            if(!CoeusOptionPane.isPropagating()){
                                saveSubmissionDetails();
                            }else{
                                CoeusOptionPane.setPropagating(
                                        false);
                            }
                        }
                    }
                });
                submissionForm.show();
                if( submissionForm.isDataSaved()) {
                    protocolInfo
                            = submissionForm.getProtocolDetails();
                    CoeusOptionPane.showInfoDialog("Protocol " + protocolId + " Submitted for Review") ;
                    
                    functionType = MODIFY_MODE ;
                    releaseUpdateLock() ;
                    submissionForm.dispose() ;

                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
            if(!( e.getMessage().equals(coeusMessageResources.parseMessageKey(
                    "protoDetFrm_exceptionCode.1130")) )){
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }
        }
        
        
        
    }
    
    private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    // private final char DISPLAY_MODE = 'D';
    private boolean modifiable = true;
    
    private ProtocolInfoBean getProtocolDetails(String protocolId) throws Exception {
        Vector dataObjects = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        
        request.setFunctionType(DISPLAY_MODE);
        
        request.setId(protocolId);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            response.setMessage(coeusMessageResources.parseMessageKey(
                    "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        } else {
            if (response.isLocked()) {
            /* the row is being locked by some other user
             */
                setModifiable(false);
                throw new Exception("protocol_row_clocked_exceptionCode.666666");
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return  (ProtocolInfoBean) dataObjects.elementAt(0);
        
    }
    
    
    public void setModifiable(boolean modifiable){
        this.modifiable = modifiable;
    }
    
    
    private final String SUBMISSION_DETAILS_SERVLET = "/IacucProtoSubmissionDetailsServlet" ;
    
    private void showSubmissionDetails() {
        if (tbdPnScheduleDetails.getComponent(1).isShowing()) // tab 1 is Protocol Assignment
        {
            System.out.println(" *********** User is on Protocol Assignment tab *************") ;
            JTable tblProtocol = getTableComponent() ;
            
            if (tblProtocol.getSelectedRow() < 0) // no rows
            {
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                        "scheduleDetFrm_exceptionCode.2201")) ;
                return ;
            }
            
            if (tblProtocol.getSelectedRowCount() > 1 ) // if more than one rows selected, then dont show submissiondetails screen
            {
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                        "scheduleDetFrm_exceptionCode.2206")) ;
                return ;
            }
            
            ProtocolSubmissionInfoBean submissionBean
                    = protocolAssignmentForm.getSelectedSubmissionDetails();
            if(submissionBean != null) {
                SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
                detailsBean.setProtocolNumber(submissionBean.getProtocolNumber()) ;
                detailsBean.setScheduleId(submissionBean.getScheduleId()) ;
                detailsBean.setSequenceNumber(new Integer(submissionBean.getSequenceNumber())) ;
                // commented by manoj to fix the bug# DEF_11 16/09/2003
                /*detailsBean = getSubmissionDetails(detailsBean) ;
                Vector vecDetails = detailsBean.getSubmissionDetails() ;
                Vector vecReviewers = detailsBean.getSelectedReviewers() ; */
                Vector vecDetails = getSubmissionDetails(detailsBean);// added by manoj to fix bug #DEF_11 16/09/2003
                Vector vecReviewers = null;
                if (vecDetails.size() <= 0 || ((Vector)vecDetails.get(0)).size() == 0 ) {
                    //display appropriate msg
                    CoeusOptionPane.showInfoDialog("Submission Details not available for this protocol") ;
                }else {
                    /* Case 646  - prahalad Mar 12 2004
                    added functiontype as a paramter instead of 'E'
                     */
                    SubmissionDetailsForm frmSubmissionDetailsForm =
                            new SubmissionDetailsForm(mdiForm, "Submission details for Protocol " +
                            submissionBean.getProtocolNumber(), submissionBean.getProtocolNumber(),
                            true, vecDetails, vecReviewers, functionType, submissionBean.getSubmissionNumber()) ; // replaced -1 by submissionBean.getSubmissionNumber()  - jan 09 2004
                    frmSubmissionDetailsForm.showForm() ;
                    // COEUSQA-2105: No notification for some IRB actions - Start
//                    // 3283: Reviewer Notification Changes:Start
//                    //Notify all reviewers if the reviewer on max submission number is changed.
//                    Vector vecSubmissionDetails = (Vector)vecDetails.get(0);
//                    if(submissionBean.getSequenceNumber() !=0 && frmSubmissionDetailsForm.isReviewerPersonsChanged()){
//                        //Check if the reviewer change is on maxSubmissionNo
//                        int maxsubmission = 0;
//                        for(int i = 0 ;i<vecSubmissionDetails.size();i++){
//                            ProtocolSubmissionInfoBean protocolSubmissionBean = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
//                            if(protocolSubmissionBean.getSequenceNumber() == submissionBean.getSequenceNumber()
//                                                && protocolSubmissionBean.getSubmissionNumber()>maxsubmission){
//                                maxsubmission    = protocolSubmissionBean.getSubmissionNumber();
//                            }
//                        }
//                        if(submissionBean.getSubmissionNumber() == maxsubmission){
//                            ProtocolMailController mailController = new ProtocolMailController(true);
//                            synchronized(mailController) {
//                                mailController.sendMail(ACTION_REVIEWER_CHANGE, detailsBean.getProtocolNumber(), detailsBean.getSequenceNumber());
//                                //351 - Change in Reviewer
//                            }
//                        }
//                    }
//                    // 3283 - End
                    // COEUSQA-2105: No notification for some IRB actions - End
                    // commented by manoj to fix the bug #DEF_11 16/09/2003
/*                    for (int recCount=0 ; recCount< vecDetails.size(); recCount++ ) {
                        HashMap hashRow = (HashMap)vecDetails.elementAt(recCount) ;
                        int tmpSubNum = hashRow.get("SUBMISSION_NUMBER")==null? -1 : Integer.parseInt(hashRow.get("SUBMISSION_NUMBER").toString()) ;
                        if (tmpSubNum == submissionBean.getSubmissionNumber()) {
                            SubmissionDetailsForm frmSubmissionDetailsForm = new SubmissionDetailsForm(mdiForm, "Submission details for Protocol " + submissionBean.getProtocolNumber(), true, vecDetails, vecReviewers, 'E', recCount ) ;
                            frmSubmissionDetailsForm.showForm() ;
                        }
                    }
 */
                }
            }else{
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(
                        "scheduleDetFrm_exceptionCode.2200"));
            }
            
        } else { // display message
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    "scheduleDetFrm_exceptionCode.2200")) ;
            
        }
        
        
        
    }
    
    // this method will get the submission details data from the servlet
    // modfied by manoj to fix the bug #DEF_11 16/09/2003
    private Vector getSubmissionDetails(SubmissionDetailsBean detailsBean) {
        Vector vecDetails= new Vector() ;
        try {    // send request
            System.out.println("*** Sending ***") ;
            
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('G') ;
            requester.setDataObject(detailsBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                vecDetails = (Vector)responder.getDataObject();//Added by manoj to fix the bug #DEF_11
                //detailsBean = (SubmissionDetailsBean)responder.getDataObject() ;
            }
        }catch(Exception ex) {
            System.out.println("  *** Error in getting submission details ***  ") ;
            ex.printStackTrace() ;
        }
        return vecDetails ; // changed to fix the bug #DEF_11 by manoj 16/09/2003
    }
    //prps end
    
    private void showReviewComments() throws Exception{
        if(tbdPnScheduleDetails.getComponent(1).isShowing()) {
            ProtocolSubmissionInfoBean submissionBean =
                    protocolAssignmentForm.getSelectedSubmissionDetails();
            if( submissionBean != null ) {
                reviewCommentsForm.setLockSchedule(false);
                String protocolNo = submissionBean.getProtocolNumber();
                int subNo = submissionBean.getSubmissionNumber();
                if( functionType == TypeConstants.MODIFY_MODE ) {
                    RequesterBean requesterBean = new RequesterBean();
                    requesterBean.setId(scheduleId) ; //prps added this jan 16 2004
                    requesterBean.setDataObject(submissionBean);
                    requesterBean.setFunctionType('t');
                    AppletServletCommunicator comm = new AppletServletCommunicator(
                            CoeusGuiConstants.CONNECTION_URL + SCHEDULE_MAINTENANCE_SERVLET,
                            requesterBean);
                    comm.send();
                    ResponderBean responderBean = comm.getResponse();
                    if(! responderBean.isSuccessfulResponse()){
                        reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE);
                    }else {
                        reviewCommentsForm.setFunctionType(TypeConstants.MODIFY_MODE);
                    }
                }else{
                    reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE);
                }
                reviewCommentsForm.setFormData(protocolNo, subNo, submissionBean.getSequenceNumber());
                reviewCommentsForm.display();
                if( reviewCommentsForm.isDataSaved() ) {
                    scheduleMinuteMaintenanceForm.setMinutesData(
                            reviewCommentsForm.getMinutesData());
                }
                
            }
        }else { // display message
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                    "scheduleDetFrm_exceptionCode.2200")) ;
        }
        
    }
    
    //prps start dec 22 2003
    public void showAdhocReports() {
        try {
            AdhocDetailsBean adhocDetailsBean = new AdhocDetailsBean() ;
            // Display different list of reports based on the current tab
            if (tbdPnScheduleDetails.getComponent(1).isShowing()) // tab 1 is Protocol Assignment => Module U
            {
                System.out.println(" *********** User is on Protocol Assignment tab *************") ;
                JTable tblProtocol = getTableComponent() ;
                
                if (tblProtocol.getSelectedRow() < 0) // no rows
                {
                    CoeusOptionPane.showWarningDialog("Select a Protocol from the list") ;
                    return ;
                } else if (tblProtocol.getSelectedRowCount() > 1 ) // if more than one rows selected, voting form shud not be displayed
                {
                    CoeusOptionPane.showWarningDialog("Select one protocol from the list") ;
                    return ;
                } else {   //Module "U"
                    adhocDetailsBean.setScheduleId(scheduleId) ;
                    ProtocolSubmissionInfoBean submissionBean
                            = protocolAssignmentForm.getSelectedSubmissionDetails();
                    adhocDetailsBean.setProtocolNumber(submissionBean.getProtocolNumber()) ;
                    adhocDetailsBean.setSequenceNumber(submissionBean.getSequenceNumber()) ;
                    adhocDetailsBean.setSubmissionNumber(submissionBean.getSubmissionNumber()) ;
                    //Added for the case COEUSDEV-220-Generate Correspondence
                    adhocDetailsBean.setCommitteeId(scheduleDetails.getCommitteeId());
                    //Added for the case COEUSDEV-220-Generate Correspondence-end
                    adhocDetailsBean.setModule('U') ;
                    AdhocReportsForm adhocReportsForm = new AdhocReportsForm(adhocDetailsBean) ;
                    adhocReportsForm.showForm() ;
                }
            } else {  // Module "S"
                adhocDetailsBean.setScheduleId(scheduleId) ;
                //Added for the case COEUSDEV-220-Generate Correspondence
                adhocDetailsBean.setCommitteeId(scheduleDetails.getCommitteeId());
                //Added for the case COEUSDEV-220-Generate Correspondence-end
                adhocDetailsBean.setModule('S') ;
                AdhocReportsForm adhocReportsForm = new AdhocReportsForm(adhocDetailsBean) ;
                adhocReportsForm.showForm() ;
            }
            
        } catch(Exception ex) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("correspType_exceptionCode.1015")) ;
        }
    }
    //prps end dec 22 2003
    
    public void saveAsActiveSheet() {
    }
    
    //Overriding methods to enable and disable Save option in file menu...
    public void internalFrameActivated(InternalFrameEvent e) {
        if(functionType==DISPLAY_MODE ) {
            // mdiForm.getFileMenu().setSaveEnabled(false);
            btnSaveScheduleDetails.setEnabled(false);
        } else {
            //mdiForm.getFileMenu().setSaveEnabled(true);
            btnSaveScheduleDetails.setEnabled(true);
        }
        super.internalFrameActivated(e);
    }
    
    
    public void internalFrameDeActivated(InternalFrameEvent e) {
        mdiForm.getFileMenu().setSaveEnabled(true);
        super.internalFrameDeactivated(e);
    }
     //COEUSQA:3333 - IRB and IACUC - Ability to Add Attachments to Minutes and Agenda - Start
    
    /**
     * Method to get PDF Attachments for Schedule ID
     * @param funcType
     * @throws edu.mit.coeus.exception.CoeusException
     * @return
     */
    public int getSchedulePDFAttachmets(char funcType)
    throws CoeusException {    
        int attachCount = 0;
        RequesterBean requesterBean = new RequesterBean();
        ScheduleAttachmentBean scheduleAttachmentBean = new ScheduleAttachmentBean();
        scheduleAttachmentBean.setScheduleId(scheduleId);
        if(funcType == 'A') {
            scheduleAttachmentBean.setAttachmentTypeCode(SCHEDULE_AGENDA_ATTACHMENTS);
        }else {
            scheduleAttachmentBean.setAttachmentTypeCode(SCHEDULE_MINITES_ATTACHMENTS);
        }
        scheduleAttachmentBean.setMimeType("application/pdf");
        requesterBean.setDataObject(scheduleAttachmentBean);
        requesterBean.setFunctionType(IACUC_GET_SCHEDULE_ATTAHMENTS);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL + SCHEDULE_MAINTENANCE_SERVLET,
                requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(),0);
        }
        attachCount = (Integer)responderBean.getDataObject();
        return attachCount;
    }
    
    /**
     * MetMethod to get a confirmation to add PDF as book
     * @return
     */
    public boolean attachBookMark(char funcType) {
         String attachType = null;
         if(funcType == 'A') {
            attachType = AGENDA_ATTACH;
        }else {
            attachType = MINUTE_ATTACH;
        }
        MessageFormat formatter = new MessageFormat("");        
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                formatter.format(coeusMessageResources.parseMessageKey("commSchdDetFrm_addPDFAsBookmarkConfirmCode.1030"),attachType),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES){
            return true;
        }
        return false;
    }
    //COEUSQA:3333 - End
    
}
