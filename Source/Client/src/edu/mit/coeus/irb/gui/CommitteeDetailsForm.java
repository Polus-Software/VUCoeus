/*
 * @(#)CommitteeDetailsForm.java 1.0 9/20/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 09-NOV-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.irb.bean.AdhocDetailsBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.event.*;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.irb.bean.CommitteeMembershipDetailsBean;
import edu.mit.coeus.irb.bean.CommitteeResearchAreasBean;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.menu.*;
//import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.CoeusTabbedPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/** The class is used to maintain the Committee details.
 * This will be invoked from the <CODE>CommitteeBaseWindow</CODE> in three different modes,
 * Add, Modify and Display. The enabled status of all the components will change
 * according to the mode of operation.
 *
 * @version :1.0 September 20, 2002, 7:35 PM
 * @author ravikanth
 * @modified by Phaneendra Kumar
 */
public class CommitteeDetailsForm extends CoeusInternalFrame
implements ActionListener{
    
    private JPanel pnlForm;
    
    private CommitteeMaintenanceForm committeeMaintenanceForm;
    private CommMembersListForm commMembersListForm;
    private AreaOfResearchDetailForm areaOfResearch;
    private CommitteeScheduleDetailsForm scheduleDetailsForm;
    private CoeusAppletMDIForm mdiForm = null;
    private CommitteeMaintenanceFormBean  committeeDetails = null;
    private CoeusToolBarButton btnSaveCommittee;
    private CoeusToolBarButton btnCloseCommittee;
    
    private Vector committeeResearchAreas = new Vector();
    private Vector memberParameters;
    // Added by chandra - 21/10/2003
    private Boolean hasScheduleRights;
    private JTabbedPane tabbedPane = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
    private int prevSelectedTab =-1;
    private char functionType;
    private int advSubmissionDays;
    private int maximumProtocols;
    private String committeeId;
    private JTable tblCommitteesheet;
    
    //holds the close button call
    private boolean closeButtonCalled = false;
    
    private boolean cnclClose;
    
    private boolean modifiable = true;
    
    /** Case 683  - prps start mar 02 2004 **/
    private CoeusMenu batchCorrespondence ;                    
    private CoeusMenuItem batchProtocolRenewal ;                    
    private CoeusMenuItem  batchIRBNotification ;
    private CoeusMenuItem  batchHistory ;
    private final String SEPERATOR="seperator";
    /** Case 683  - prps end mar 02 2004 **/ 
    
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    private BaseWindowObservable observable;
    
    private boolean isLockReleased;
    //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
    private static final int RESEARCH_AREA_TAB_INDEX = 2;
    /**
     * Default constructor to create <code>CommitteeDetailsForm</code> with the
     * given parent component and Committee id whose details will be populated
     * to the form components.
     * @param functionType character which specifies the form opened mode.
     * @param committeeId String representing the Committee ID whose details has
     * to be fetched.
     * @param mdiForm reference to <code>CoeusAppletMDIForm</code>.
     */
    public CommitteeDetailsForm(char functionType,String committeeId,
    CoeusAppletMDIForm mdiForm){
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        super("IRB Committee Details" + ( (committeeId != null &&
        committeeId.length() > 0 ) ? " - " + committeeId :"" ), mdiForm);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        this.mdiForm = mdiForm; 
        this.committeeId = committeeId;
        this.functionType = functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
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
        this.dispatchEvent(new InternalFrameEvent(this,InternalFrameEvent.INTERNAL_FRAME_ACTIVATED));
    }
    /* End Block */
    
    /** This method is used to get the form opened mode. This will be called
     * from <CODE>CommitteeBaseWindow</CODE>.
     * @return Character which specifies the form opened mode.
     */
    public char getFunctionType(){
        return functionType;
    }
    
    /** This method is used to set the form opened mode. This will be called from
     * <CODE>CommitteeBaseWindow</CODE>.
     * @param functionType character which specifies the form opened mode.
     */
    public void setFunctionType(char functionType){
        this.functionType = functionType;
    }
    
    
    /** This method is used to get the <CODE>CommitteeMaintenanceFormBean</CODE>. This will
     *  be called from <CODE>CommitteeBaseWindow</CODE>.
     *
     * @return <CODE>CommitteeMaintenanceFormBean</CODE> with all the details of a Committee.
     */
    public CommitteeMaintenanceFormBean getCommMaintFormBean(){
        return committeeDetails;
    }
    
    /** This method is used to set the <CODE>CommitteeMaintenanceFormBean</CODE>.
     * This will be called from <CODE>CommitteeBaseWindow</CODE>.
     *
     * @param committeeDetails <CODE>CommitteeMaintenanceFormBean</CODE> with all the
     * Committee details.
     */
    public  void setCommMaintFormBean(
    CommitteeMaintenanceFormBean committeeDetails){
        this.committeeDetails = committeeDetails;
    }
    
    
    /** This method is used to set the reference of the committee list table used
     * in <CODE>CommitteeBaseWindow</CODE>.
     *
     * @param committeesheet JTable reference to the committee list table.
     */
    public void setCommitteesheet(JTable committeesheet){
        this.tblCommitteesheet = committeesheet;
    }
    
    /**
     * This method is used to set the modifiable flag.
     *
     * @param modifiable boolean value true if the selected record can be
     * modified else false.
     */
    public void setModifiable(boolean modifiable){
        this.modifiable = modifiable;
    }
    /**
     * This method is used to check whether the selected record can be modified
     * or not.
     *
     * @return boolean value true if the selected record can be
     * modified else false.
     */
    public boolean isModifiable(){
        return this.modifiable;
    }
    
    /** This method is used to show <CODE>CommitteeDetailsForm</CODE>.
     *
     * @throws Exception if it is unable to show the <CODE>CommitteeDetailsForm</CODE>.
     */
    public void showCommitteDetailForm() throws Exception{
        getContentPane().add(createCommitteePanel(), BorderLayout.CENTER);
        
        /* This will catch the window closing event and
         * checks any data is modified.If any changes are done by
         * the user the system will ask for confirmation of Saving the info.
         */
        this.addVetoableChangeListener(new VetoableChangeListener(){
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
				if (pce.getPropertyName().equals(
                JInternalFrame.IS_CLOSED_PROPERTY)) {
                    if( closeButtonCalled ){
                        return;
                    }
                    boolean changed = (
                    (Boolean) pce.getNewValue()).booleanValue();
                    String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
					if (changed && isSaveRequired()) {
						cnclClose = false;//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                        switch(confirm){
							case(JOptionPane.YES_OPTION):
                                try{
                                    setCommitteeInfo();
                                    releaseUpdateLock();
                                    mdiForm.removeFrame(
                                    CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                                    ,committeeId);
                                    CoeusInternalFrame frame = mdiForm.getFrame(
                                    CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                                    if (frame != null){
                                        frame.setSelected(true);
                                        frame.setVisible(true);
                                    }
                                    
                                }catch(Exception ex){
                                    //ex.printStackTrace();
                                    //Added By sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
                                    cnclClose = true;
                                    //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
                                    String exMsg = ex.getMessage();
                                    CoeusOptionPane.showWarningDialog(exMsg);
                                    throw new PropertyVetoException(
                                    "Cancelled",pce);
                                }
                                break;
                            case(JOptionPane.NO_OPTION):
                                releaseUpdateLock();
                                mdiForm.removeFrame(
                                CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                                ,committeeId);
                                CoeusInternalFrame frame = mdiForm.getFrame(
                                CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                                if (frame != null){
                                    frame.setSelected(true);
                                    frame.setVisible(true);
                                }
                                break;
                            case(JOptionPane.CANCEL_OPTION):
                            case(JOptionPane.CLOSED_OPTION):
                                cnclClose = true;
								isLockReleased = true;//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
                                throw new PropertyVetoException(
                                "Cancelled",pce);
                        }
                        /** Added by chandra.
                         *Check if the lock is already released. This check is to
                         *control the vetoableChangeListener.Once the button Close
                         *is clicked it is calling this.doDefaultCloseAction() which inturn
                         *call close action again.
                         *To avoid this listeners conflict added the code
                         *22 nov 2004
                         */
                    }else if(!isLockReleased){
						if(!cnclClose)//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
						releaseUpdateLock();
                        /**
                         * Bug Fix for Selection of Protocol Detail/Protocol Base Window.
                         * Updated Subramanya Feb' 13 2003.
                         */
                        if( !cnclClose ) {
                            //Modified By sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
                            //Was outside this Block
                            mdiForm.removeFrame(
                            CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                            ,committeeId);
                            //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
                            CoeusInternalFrame frame = mdiForm.getFrame(
                            CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                            if (frame != null){
                                frame.setSelected(true);
                                frame.setVisible(true);
                            }
                            cnclClose = false;
                        }
                    }
                }
            }
        });
        //this.setSelected(true);
        //this.setVisible(true);
        
        this.addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameOpened(InternalFrameEvent e) {
                /* If the functionType is not 'D' then set the focus to the
                 * first editable field in the committeMaintenanceForm
                 */
                if (functionType != 'D'|| functionType == 'A' ) {
                    //Updated by Subramanya
                   
                    committeeMaintenanceForm.setFocus();
                    
                }
                
                
            }
        });
        
        //prps start dec 23 2003 - to add edit menu to committee detail screen
        setFrameMenu(committeeDetailsEditMenu());
        
        //prps end dec 23 2003
        final JToolBar committeeDetailsToolBar = committeeDetailsToolBar();
        this.setFrameToolBar(committeeDetailsToolBar);
        this.setToolsMenu(null);
        this.setFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE);
        this.setFrameIcon(mdiForm.getCoeusIcon());
        mdiForm.getDeskTopPane().add(this);
        //mdiForm.putFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE,this);
        mdiForm.putFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE,committeeId,functionType,this);
        this.setSelected(true);
        this.setVisible(true);
        committeeMaintenanceForm.setFocus();
    }
    
    //prps start dec 23 2003
    private CoeusMenuItem printAdhoc ;
    private CoeusMenu committeeDetailsEditMenu() {
        CoeusMenu mnuScheduleDetails = null;
        Vector fileChildren = new Vector();
        
        printAdhoc = new CoeusMenuItem("Generate Correspondence",null,true,true);
        printAdhoc.setMnemonic('C');
        printAdhoc.addActionListener(this);
     
        /** Case 683  - prps start mar 02 2004 **/
        
        Vector batchChildren = new Vector();
        //Modified to seperate IRB renewal reminder description-Start
        batchProtocolRenewal = new CoeusMenuItem("IRB Renewal Reminder",null,true,true);
        //Modified to seperate IRB renewal reminder description-End
        batchProtocolRenewal.setMnemonic('R');
        batchProtocolRenewal.addActionListener(this);                 
                        
        batchIRBNotification = new CoeusMenuItem("IRB Notification Reminder",null,true,true);
        batchIRBNotification.setMnemonic('I');
        batchIRBNotification.addActionListener(this);                
        
        batchHistory = new CoeusMenuItem("Batch History",null,true,true);
        batchHistory.setMnemonic('H');
        batchHistory.addActionListener(this);                
        
        batchChildren.add(batchProtocolRenewal) ;
        batchChildren.add(batchIRBNotification) ;
        batchChildren.add(SEPERATOR) ;
        batchChildren.add(batchHistory) ;
        
        batchCorrespondence = new CoeusMenu("Batch Correspondence",null,batchChildren, true,true);
        batchCorrespondence.setMnemonic('B');
        batchCorrespondence.addActionListener(this);                 
                
        //prps start Apr 14 2004
        if (functionType != 'M')
        {
            printAdhoc.setEnabled(false) ;
            batchProtocolRenewal.setEnabled(false) ;
            batchIRBNotification.setEnabled(false) ;
            batchHistory.setEnabled(false) ;
        }   
        
        //prps end Apr 14 2004
        
        /** Case 683  - prps end mar 02 2004 **/     
        
       fileChildren.add(printAdhoc);
        /** Case 683  - prps start mar 02 2004 **/
       fileChildren.add(batchCorrespondence) ;
        /** Case 683  - prps start mar 02 2004 **/
        mnuScheduleDetails = new CoeusMenu("Edit",null,fileChildren,true,true);
        mnuScheduleDetails.setMnemonic('E');
        return mnuScheduleDetails;
    }
    
    //prps end dec 23 2003
    
    
    public void registerObserver( java.util.Observer observer ) {
        observable.addObserver( observer );
    }
    
    /**
     * This method creates a panel with tab pages and other required components.
     * @return JPanel with tab controls and other components.
     *
     * @throws Exception if it is unable to create the committee panel with
     * tabbedpane and other components.
     */
    public JPanel createCommitteePanel() throws Exception{
        pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout(10,10));
        pnlForm.add(createForm(),BorderLayout.CENTER);
        pnlForm.setSize(550,300);
        return pnlForm;
    }
    
    
    /** This method creates the tabbedpane used in CommitteeDetailsForm.
     * @return JTabbedPane with all the tab controls.
     * @throws Exception if it is unable to fetch the lookup details or unable
     * to create the tabbedpane.
     */
    public JTabbedPane createForm() throws Exception {
        Vector details = null;
        Vector committeeTypes = null;
        Vector reviewTypes = null;
        Vector membershipTypes = null;
        Vector memberStatus = null;
        Vector scheduleStatus = null;
        String committeeId = "";
        /* Get all the look up details required for Committee tab pages to
         * display.
         *  In committeeMaintenanceForm
         *  0 - CommitteeTypes information
         *  1 - ReviewTypes information
         * InMemberMaintenanceForm
         *  2 - MembershipTypes
         *  3 - MemberStatus
         *  4 - SheduleStatus
         *  If it is not in modify the existing Committee details with
         * members complete details and shedule status information
         *
         *  If it is in Modify mode this will get the complete Committee
         * details information from the Database for modifications by the user
         */
        details = getLookupDetails();
        if (details != null) {
            if ( functionType != 'D') {
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                if ((details.get(0) != null) &&
                (details.get(0) instanceof ComboBoxBean)) {                    
                    committeeTypes  = new Vector();
                    ComboBoxBean comboBoxBean = (ComboBoxBean)details.get(0);
                    committeeTypes.add(comboBoxBean);
                    // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                }
                if ((details.get(1) != null) &&
                (details.get(1) instanceof Vector)) {
                    reviewTypes = (Vector)details.get(1);
                }
                if ((details.get(2) != null) &&
                (details.get(2) instanceof Vector)) {
                    membershipTypes = (Vector)details.get(2);
                }
                if ((details.get(3) != null) &&
                (details.get(3) instanceof Vector)) {
                    memberStatus = (Vector)details.get(3);
                }
                if ((details.get(4) != null) &&
                (details.get(4) instanceof Vector)){
                    scheduleStatus = (Vector)details.get(4);
                }
                if (functionType != 'A' && details.size() > 4){
                    //Modify mode
                    if ((details.get(5) != null) && (
                    details.get(5) instanceof CommitteeMaintenanceFormBean)) {
                        committeeDetails =
                        (CommitteeMaintenanceFormBean)details.get(5);
                    }
                    if ((details.get(6) != null) &&
                    (details.get(6) instanceof Vector)){
                        memberParameters = (Vector)details.get(6);
                    }
                }else{
                    //Add mode
                    if ((details.get(5) != null) &&
                    (details.get(5) instanceof Vector)){
                        memberParameters = (Vector)details.get(5);
                    }
                }
            }else {
                if ((details.get(0) != null) && (
                details.get(0) instanceof CommitteeMaintenanceFormBean)) {
                    committeeDetails =
                    (CommitteeMaintenanceFormBean)details.get(0);
                }
                if ((details.get(1) != null) &&
                (details.get(1) instanceof Vector)){
                    memberParameters = (Vector)details.get(1);
                }
                // Added by chandra - 21/11/2003
               if((details.get(2)!=null) &&
               (details.get(2) instanceof  Boolean)) {
                   hasScheduleRights = (Boolean)details.get(2);
               }// End Chandra - 21/11/2003
            }
        }
        
        /* create CommitteeMaintenanceForm tab with the functionType
         * and the committeeDetails
         */
        committeeMaintenanceForm = new CommitteeMaintenanceForm(functionType,
        committeeDetails, memberParameters);
        //set the committeeTypes in committeeMaintenanceForm
        committeeMaintenanceForm.setCommitteeTypes(committeeTypes);
        //set the committeeReviewTypes in committeeMaintenanceForm
        committeeMaintenanceForm.setCommitteeReviewTypes(reviewTypes);
        Vector commMembers = new Vector();
        Vector schedules = new Vector();
        
        if (committeeDetails != null ) {
            //IACUC Changes - Start
            if(committeeDetails.getCommitteeTypeCode() == CoeusConstants.IRB_COMMITTEE_TYPE_CODE){
                this.setTitle("IRB Committee Details "+ ( (committeeDetails.getCommitteeId() != null &&
                        committeeDetails.getCommitteeId().length() > 0 ) ? " - " + committeeDetails.getCommitteeId() :"" ));
            }else if(committeeDetails.getCommitteeTypeCode() == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                this.setTitle("IACUC Committee Details "+ ( (committeeDetails.getCommitteeId() != null &&
                        committeeDetails.getCommitteeId().length() > 0 ) ? " - " + committeeDetails.getCommitteeId() :"" ));
            }
            //IACUC Changes - End
            committeeId = committeeDetails.getCommitteeId();
            commMembers  = committeeDetails.getCommitteeMembers();
            schedules = committeeDetails.getCommitteeSchedules();
            advSubmissionDays = committeeDetails.getAdvSubmissionDaysReq();
            maximumProtocols = committeeDetails.getMaxProtocols();
            committeeResearchAreas
            = committeeDetails.getCommitteeResearchAreas();
        }
        // create Members tab
        commMembersListForm = new CommMembersListForm(
        functionType,getMembersList(commMembers));
        if((committeeId != null) && (committeeId.length()>0)){
            commMembersListForm.setCommitteeId(committeeId);
        }
        commMembersListForm.setCommDetailForm(this);
        /* create Area of Research tab with functionType and The ResearchAreas
         * the committee will be reviewing
         */
        areaOfResearch = new AreaOfResearchDetailForm(
        functionType,getResearchAreas(committeeResearchAreas ));
        
        // create Schedule tab
        scheduleDetailsForm = new CommitteeScheduleDetailsForm(
        functionType,schedules);
        scheduleDetailsForm.setStatusCodes(scheduleStatus);
        scheduleDetailsForm.setCommitteeId(committeeId);
        scheduleDetailsForm.setAdvSubmissionDays(advSubmissionDays);
        scheduleDetailsForm.setMaximumProtocols(maximumProtocols);
        //Added for IACUC Changes - Start
        if(functionType != TypeConstants.ADD_MODE){
            scheduleDetailsForm.setCommitteeTypeCode(committeeDetails.getCommitteeTypeCode());
        }
        //IACUC Changes - End
        // Added by chandra 21/11/2003
        if( functionType == 'D'){
            scheduleDetailsForm.setScheduleRights(hasScheduleRights.booleanValue());
        }// End chandra
        JPanel pnlCommittee = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlCommittee.add(
        committeeMaintenanceForm.getCommitteMaintenanceForm(mdiForm));
        tabbedPane.setFont(CoeusFontFactory.getNormalFont());
        JScrollPane jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlCommittee );
        
        tabbedPane.addTab("Committee",  jscrPnTab );
        JPanel pnlMembers = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlMembers.add(commMembersListForm.getMembersListForm(mdiForm));
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlMembers );
        
        tabbedPane.addTab("Members", jscrPnTab );
        JPanel pnlResearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlResearch.add(areaOfResearch.showAreaOfResearch(mdiForm));
        
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlResearch );
        tabbedPane.addTab("Area of Research",   jscrPnTab);
        areaOfResearch.setAORHeader(
        "Areas of Research this committee will be reviewing");
        JPanel pnlSchedule = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSchedule.add(scheduleDetailsForm.getScheduleDetailsForm(mdiForm));
        
        jscrPnTab = new JScrollPane();
        jscrPnTab.setViewportView( pnlSchedule );
        tabbedPane.addTab("Schedule",  jscrPnTab );
        tabbedPane.setSelectedIndex(0);
        /* This  will catch the tab change event in the tabbed pane.
         * If the user selects any other tab from the CommitteeTab without
         * saving the committee information then it will prompt the user
         * to save the committee information before shifting to the other tab.
         */
        tabbedPane.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ce){
                JTabbedPane pn = (JTabbedPane)ce.getSource();
                int selectedTab = pn.getSelectedIndex();
                try {
                    checkInfo(selectedTab);
                    switch ( selectedTab ) {
                        case 0 :
                                committeeMaintenanceForm.setFocus();
                                 break;
                        case 1 :
                                commMembersListForm.setDefaultFocusToComponent();
                                 break;
                        case 2 :
                                 areaOfResearch.setDefaultFocusToComponent();
                                 break;
                        case 3 :
                                 scheduleDetailsForm.setDefaultFocusForComponent();   
                                 break;
                    }
                }catch(Exception e) {
                    tabbedPane.setSelectedIndex(0);
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }
        });
        prevSelectedTab = 0;
        return tabbedPane;
    }
    
    /** This method is used to check whether there are any unsaved modifications
     * in <CODE>CommitteeDetailsForm</CODE>.
     * @return boolean true if there are any unsaved modifications else false.
     */
    protected boolean isSaveRequired() {
        boolean saveRequired = false;
        /* check wll the falgs in CommitteeMaintenaceForm and CommMembersList
         * and ScheduleDetails form whether any data is changed by the user or
         * not to set the save required flag in this committeeDetails form.
         *
         */
        
        if ( (committeeMaintenanceForm.isSaveRequired())
        || (commMembersListForm.isSaveRequired())
        || (scheduleDetailsForm.isSaveRequired())
        || (areaOfResearch.isSaveRequired())) {
            saveRequired =true;
        }
        return saveRequired;
    }
    
    /**
     * The method used to get the save status of committee.
     * This will be called from CommitteeBaseWindow
     *  @return committee save status
     */
  /*  public boolean isCommitteeSaveRequired(){
        isSaveRequired = committeeMaintenanceForm.isSaveRequired();
        return isSaveRequired;
    }
   */
    
    /**
     * This method is used to show the confirmation message if the user tries
     * to select other tabbedpane without saving the entered Committee details.
     *
     * @param selectedTab integer representing the selected tab.
     *
     * @throws Exception if unable to save the changes to the database.
     */
    protected void checkInfo(int selectedTab) throws Exception {
        if ((prevSelectedTab != selectedTab) && (selectedTab != 0)) {
            if(prevSelectedTab == 0 ) {
                if (functionType == 'A') {
                    if (committeeMaintenanceForm.isSaveRequired()) {
                        int answer = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                        if (answer == JOptionPane.YES_OPTION){
                            setCommitteeInfo();
                            selectedTab = 0;
                        }
                    }
                }
            }
        }
        prevSelectedTab = selectedTab;
    }
    
    
    
    /** Get the required details from the database for adding a new Committee or
     * modifying the existing Committee information. This method communicates
     * with the server using java io streams and gets the details and returns as a
     * Vector.
     *
     * @return <CODE>CommitteeMaintenanceFormBean</CODE> used to set the Committee details.
     * @throws Exception if unable to fetch the details from the database.
     */
    public Vector getLookupDetails() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        /* connect to the database and get the formData for the given committee
         * id.
         */
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(committeeId);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        Vector vecParam = new Vector();
        vecParam.add(new Integer(CoeusConstants.IRB_COMMITTEE_TYPE_CODE)); 
        request.setDataObjects(vecParam);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        AppletServletCommunicator comm =
        new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector dataObjects = null;
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        }else {
            // added by manoj to throw authorization messages as CoeusException 15/09/2003
            if(response.getDataObject() != null){
                Object obj = response.getDataObject();
                if(obj instanceof CoeusException){
                    throw (CoeusException)obj;
                }
            }else{
                if (response.isLocked()) {
            /* the row is being locked by some other user
             */
                    setModifiable(false);
                    throw new Exception("committee_row_clocked_exceptionCode.444444");
                }else {
                    throw new Exception(response.getMessage());
                }
            }
        }
        return dataObjects;
    }
    /**
     *  The method to get the members list in a hash Table structure
     *
     * @param Vector of MemberDetails in a committee
     */
    private Hashtable getMembersList(Vector members){
        CommitteeMembershipDetailsBean committeeMember = null;
        Hashtable membersList = new Hashtable();
        if (members != null && members.size() > 0 ) {
            for (int member = 0 ; member < members.size();member++)  {
                if (members.get(member) instanceof
                CommitteeMembershipDetailsBean) {
                    committeeMember = (CommitteeMembershipDetailsBean)
                    members.get(member);
                    membersList.put(new Integer(member),committeeMember);
                }
            }
        }
        return membersList;
    }
    /**
     *  method to get all area of reasearch areas from database to the Vector
     * of rows information to the AreaOfResearchDetails form.
     */
    private Vector getResearchAreas(Vector researchAreas){
        Vector newResearchAreas = null;
        if (researchAreas != null) {
            newResearchAreas = new Vector();
            for (int count =0;count < researchAreas.size();count++){
                Vector row =  new Vector();
                CommitteeResearchAreasBean area =
                (CommitteeResearchAreasBean)researchAreas.elementAt(count);
                row.add(area.getAreaOfResearchCode());
                row.add(area.getAreaOfResearchDescription());
                newResearchAreas.add(row);
            }
        }
        return newResearchAreas;
    }
    
    /**
     * Method used to save the Committee information to the database.
     * @throws Exception if unable to save the given Committee details to the
     * database.
     */
    protected void setCommitteeInfo() throws Exception{
        if(functionType == 'D'){
            //btnSaveCommittee.setEnabled(false);
            //do nothing
            
        }else {
           
            if(!committeeMaintenanceForm.validateData()){
                prevSelectedTab = 0;
                tabbedPane.setSelectedIndex(0);
            }
            //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
            else if(!areaOfResearch.validateData()) {
                tabbedPane.setSelectedIndex(RESEARCH_AREA_TAB_INDEX);
            }
            else{
                /*Get the members information and the schedules information
                 * for saving complete information
                 */
                if (functionType == 'A' || functionType == 'M'){
                    committeeDetails = committeeMaintenanceForm.getValues();
                    committeeDetails.setCommitteeMembers(
                    commMembersListForm.getCommitteeMembersList());
                    committeeDetails.setCommitteeResearchAreas(
                    getCommitteeAreaOfResearch());
                    committeeDetails.setCommitteeSchedules(
                    scheduleDetailsForm.getCommScheduleData());
                    //isLockReleased = true;
                }
                
                String connectTo = CoeusGuiConstants.CONNECTION_URL +
                "/comMntServlet";
                /* connect to the database and get the formData for the
                 * given committee id
                 */
                
                RequesterBean request = new RequesterBean();
                request.setFunctionType('S');
                request.setId(committeeId);
                request.setDataObject(committeeDetails);
                //Added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - Start
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
                Vector vecData = new Vector();
                vecData.add(0,new Boolean(commMembersListForm.showDetails));
                vecData.add(1,CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
                request.setDataObjects(vecData);
                // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
                //Added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - End
                AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
                comm.send();
                ResponderBean response = comm.getResponse();
                if (response.isSuccessfulResponse()) {
                    committeeDetails = (CommitteeMaintenanceFormBean)
                    response.getDataObject();
                } else {
                    if (response.isCloseRequired()) {
                        /*mdiForm.removeFrame(
                        CoeusGuiConstants.COMMITTEE_FRAME_TITLE);*/
                        mdiForm.removeFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                        ,committeeId);
                        this.doDefaultCloseAction();
                        CoeusInternalFrame frame = mdiForm.getFrame(
                        CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                        if (frame != null){
                            frame.setSelected(true);
                            frame.setVisible(true);
                        }
                    }else {
                        if (committeeDetails != null ){
                            functionType = 'M';
                            if(this.getTitle().indexOf('-') == -1 ){
                                setTitle(getTitle() + " - " +
                                committeeDetails.getCommitteeId());
                            }
                            committeeMaintenanceForm.setFunctionType('M');
                            commMembersListForm.setFunctionType('M');
                            scheduleDetailsForm.setFunctionType('M');
                            areaOfResearch.setFunctionType('M');
                            commMembersListForm.setCommitteeId(
                            committeeDetails.getCommitteeId());
                            scheduleDetailsForm.setCommitteeId(
                            committeeDetails.getCommitteeId());
                            advSubmissionDays
                            = committeeDetails.getAdvSubmissionDaysReq();
                            maximumProtocols = committeeDetails.getMaxProtocols();
                            scheduleDetailsForm.setAdvSubmissionDays(advSubmissionDays);
                            scheduleDetailsForm.setMaximumProtocols(maximumProtocols);
                            scheduleDetailsForm.setSaveRequired(false);
                            committeeMaintenanceForm.setValues(committeeDetails);
                            commMembersListForm.setValues(getMembersList(
                            committeeDetails.getCommitteeMembers()));
                            scheduleDetailsForm.setValues(
                            committeeDetails.getCommitteeSchedules());
                            committeeResearchAreas =
                            committeeDetails.getCommitteeResearchAreas();
                            areaOfResearch.setValues(
                            getResearchAreas(committeeResearchAreas));
                        }
                        throw new Exception(response.getMessage());
                    }
                }
                if (committeeDetails != null ){
                    /** begin: fixed bug with id #147  */
                    /*   set the message to be displayed in status bar. RefID:#147 */
                    /*setStatusMessage(
                        coeusMessageResources.parseMessageKey("general_saveCode.2275"));*/
                    /** end: fixed bug with id #147  */
                    //commented by ravi while implementing observer pattern
                    //updateRow();
                    observable.setFunctionType( functionType );
                    observable.notifyObservers(committeeDetails);
                    functionType = 'M';
                    if(this.getTitle().indexOf('-') == -1 ){
                        setTitle(getTitle() + " - " +
                        committeeDetails.getCommitteeId());
                    }
                    committeeMaintenanceForm.setFunctionType('M');
                    commMembersListForm.setFunctionType('M');
                    scheduleDetailsForm.setFunctionType('M');
                    areaOfResearch.setFunctionType('M');
                    commMembersListForm.setCommitteeId(
                    committeeDetails.getCommitteeId());
                    scheduleDetailsForm.setCommitteeId(
                    committeeDetails.getCommitteeId());
                    advSubmissionDays
                    = committeeDetails.getAdvSubmissionDaysReq();
                    maximumProtocols = committeeDetails.getMaxProtocols();
                    scheduleDetailsForm.setAdvSubmissionDays(advSubmissionDays);
                    scheduleDetailsForm.setMaximumProtocols(maximumProtocols);
                    scheduleDetailsForm.setSaveRequired(false);
                    committeeMaintenanceForm.setValues(committeeDetails);
                    commMembersListForm.setValues(getMembersList(
                    committeeDetails.getCommitteeMembers()));
                    scheduleDetailsForm.setValues(
                    committeeDetails.getCommitteeSchedules());
                    committeeResearchAreas =
                    committeeDetails.getCommitteeResearchAreas();
                    areaOfResearch.setValues(
                    getResearchAreas(committeeResearchAreas));
                    setSelected(true);
                    setVisible(true);
                }else {
                    throw new Exception(coeusMessageResources.parseMessageKey(
                    "saveFail_exceptionCode.1102"));
                }
                committeeMaintenanceForm.setSaveRequired(false);
            }
        }
    }
    
    /**
     *  The method used to get the area of research for a particular committee
     */
    private Vector getCommitteeAreaOfResearch(){
        Vector newAreaOfResearches = new Vector();
        //These are the selected AreaOfReasech for the committee
        Vector expertise = areaOfResearch.getExpertiseData();
        for(int expertiseRow = 0;expertiseRow < expertise.size();
        expertiseRow++){
            boolean found = false;
            CommitteeResearchAreasBean newCommitteResearchAreasBean
            = new CommitteeResearchAreasBean();
            Vector committeeExpertise
            = (Vector)expertise.elementAt(expertiseRow);
            newCommitteResearchAreasBean.setAreaOfResearchCode(
            committeeExpertise.elementAt(0).toString());
            newCommitteResearchAreasBean.setAreaOfResearchDescription(
            committeeExpertise.elementAt(1).toString());
            /* If the existing AreaOfResearch information for this committee is
             * existing check the selected ones with the existing ones to get
             * the new selected AORs for this committee.
             */
            if(committeeResearchAreas!=null){
                CommitteeResearchAreasBean commResearchAreasBean = null;
                for(int availableExpertiseRow=0;
                availableExpertiseRow<committeeResearchAreas.size();
                availableExpertiseRow++){
                    commResearchAreasBean =
                    (CommitteeResearchAreasBean)committeeResearchAreas.elementAt(
                    availableExpertiseRow);
                    if(commResearchAreasBean.getAreaOfResearchCode().equals(
                    newCommitteResearchAreasBean.getAreaOfResearchCode())){
                        found = true;
                        break;
                    }
                }
            }
            /*
             * If the new AreaOfResearch is selected for the committee
             * create a new bean and set the acType of that particular bean as
             * 'I' to insert in the database.
             */
            if(!found){
                newCommitteResearchAreasBean.setAcType("I");
            }
            newAreaOfResearches.addElement(newCommitteResearchAreasBean);
        }
        
       /* If the existing AreaOfResearch information for this committee is
        * existing check the selected ones with the existing ones to get
        * the deselected AORs for this committee from the existing to remove
        * from the database.
        */
        if(committeeResearchAreas!=null){
            CommitteeResearchAreasBean commResearchAreasBean = null;
            for(int availableExpertiseRow=0;
            availableExpertiseRow < committeeResearchAreas.size();
            availableExpertiseRow++){
                boolean found = false;
                commResearchAreasBean =
                (CommitteeResearchAreasBean)committeeResearchAreas.elementAt(
                availableExpertiseRow);
                for(int expertiseRow = 0; expertiseRow < expertise.size();
                expertiseRow++){
                    Vector committeeExpertise = (Vector)expertise.elementAt(
                    expertiseRow);
                    if(commResearchAreasBean.getAreaOfResearchCode().equals(
                    committeeExpertise.elementAt(0).toString())){
                        found = true;
                        break;
                    }
                }
                /*
                 * If the existing area Of Research is not found in the selected
                 * list delete that particular one from the data base. setAcType
                 * 'D'
                 */
                if(!found){
                    CommitteeResearchAreasBean newCommitteeExpertiseBean =
                    (CommitteeResearchAreasBean)
                    committeeResearchAreas.elementAt(
                    availableExpertiseRow);
                    newCommitteeExpertiseBean.setAcType("D");
                    newAreaOfResearches.addElement(newCommitteeExpertiseBean);
                }
                
            }
        }
        return newAreaOfResearches;
    }
    /**
     * Committee ToolBar is a which provides the Icons for Performing
     * Save, Close buttons.
     *
     * @returns JToolBar committee Toolbar
     */
    private JToolBar committeeDetailsToolBar() {
        JToolBar toolbar = new JToolBar();
        btnSaveCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),null,
        "Save Committee");
        
        btnCloseCommittee = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
        "Close Committee");
        
        //         btnSaveCommittee = new CoeusToolBarButton(new ImageIcon(
        //        getClass().getResource(CoeusGuiConstants.SAVE_ICON)),null,
        //        "Save Committee");
        //
        //        btnCloseCommittee = new CoeusToolBarButton(new ImageIcon(
        //        getClass().getResource(CoeusGuiConstants.CLOSE_ICON)),null,
        //        "Close Committee");
        
        btnSaveCommittee.addActionListener(this);
        btnCloseCommittee.addActionListener(this);
        
        toolbar.add(btnSaveCommittee);
        toolbar.addSeparator();
        toolbar.add(btnCloseCommittee);
        
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    /** Overridden method of <code>ActionListener</code>. All the  actions
     * for the menu and toolbar associated with the <CODE>CommitteDetailsForm</CODE> will be
     * invoked from this method.
     * @param ae <CODE>ActionEvent</CODE>, a semantic event which indicates that a
     * component-defined action occured.
     */
    public void actionPerformed(ActionEvent ae){
        try {
            Object actSource = ae.getSource();
            if (actSource.equals(btnSaveCommittee)) {
                setCommitteeInfo();
            } else if (actSource.equals(btnCloseCommittee)) {
                String msg = coeusMessageResources.parseMessageKey(
                "saveConfirmCode.1002");
                /* before closing this window check for saveRequired flag and
                 * confirm the user for saving the information.
                 */
                if ( isSaveRequired()) {
                    
                    int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
                    isLockReleased = false;//Modified to false by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004
                    switch(confirm){
                        case(JOptionPane.YES_OPTION):
                            try{
                                /* save the committeeInfo and remove this frame
                                 * instance from the mdiForm.And get the
                                 * Committee Base frame instance from the
                                 * mdiForm and set it as selected.
                                 */
                                //bug fix for case #2051 by tarique start
                                closeButtonCalled = true;
                                //bug fix for case #2051 by tarique end
                                setCommitteeInfo();
                                releaseUpdateLock();
                                /*mdiForm.removeFrame(
                                CoeusGuiConstants.COMMITTEE_FRAME_TITLE);*/
                                mdiForm.removeFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                                ,committeeId);
                                this.doDefaultCloseAction();
                                CoeusInternalFrame frame = mdiForm.getFrame(
                                CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                                if (frame != null){
                                    frame.setSelected(true);
                                    frame.setVisible(true);
                                }
                            }catch(Exception ex){
                                //ex.printStackTrace();
                                String exMsg = ex.getMessage();
                                CoeusOptionPane.showWarningDialog(exMsg);
                                throw new PropertyVetoException(
                                "Cancelled",null);
                            }
                            break;
                        case(JOptionPane.NO_OPTION):
                            /* remove this frame
                             * instance from the mdiForm.And get the
                             * Committee Base frame instance from the
                             * mdiForm and set it as selected.
                             */
                            
                            releaseUpdateLock();
                            /*mdiForm.removeFrame(
                            CoeusGuiConstants.COMMITTEE_FRAME_TITLE);*/
                            mdiForm.removeFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                            ,committeeId);
                            //flag to referenced in veto close action. Updated. Subramanya
                            closeButtonCalled = true;
                            this.doDefaultCloseAction();
                            CoeusInternalFrame frame = mdiForm.getFrame(
                            CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                            if (frame != null) {
                                frame.setSelected(true);
                                frame.setVisible(true);
                            }
                            break;
                        case(JOptionPane.CANCEL_OPTION):
                            //  releaseUpdateLock();
							isLockReleased = true;
                            break;
                    }
                    
                }else {
                    //  releaseUpdateLock();
                    /*mdiForm.removeFrame(
                    CoeusGuiConstants.COMMITTEE_FRAME_TITLE);*/
                    mdiForm.removeFrame(CoeusGuiConstants.COMMITTEE_FRAME_TITLE
                    ,committeeId);
                    this.doDefaultCloseAction();
                    CoeusInternalFrame frame = mdiForm.getFrame(
                    CoeusGuiConstants.COMMITTEEBASE_FRAME_TITLE);
                    if (frame != null){
                        frame.setSelected(true);
                        frame.setVisible(true);
                    }
                }
            }//prps start dec 23 2003
            else if (actSource.equals(printAdhoc)) 
            {
                showAdhocReports() ;
            }
            //prps end dec 23 2003
            /** Case 683  - prps start mar 02 2004 **/
            else if(actSource.equals(batchProtocolRenewal))
            {
                 // type 1
                Vector vecParam = new Vector() ;
                vecParam.add(0, committeeDetails.getCommitteeId()) ;
                vecParam.add(1,  committeeDetails.getCommitteeName()) ;
                vecParam.add(2, new String("1")) ;
                
                BatchCorrespondenceForm batchCorrespondenceForm = new BatchCorrespondenceForm(vecParam) ;
                batchCorrespondenceForm.showForm() ;
            }    
            else if (actSource.equals(batchIRBNotification))
            {
                //type 2
                Vector vecParam = new Vector() ;
                vecParam.add(0, committeeDetails.getCommitteeId()) ;
                vecParam.add(1,  committeeDetails.getCommitteeName()) ;
                vecParam.add(2, new String("2")) ;
                
                BatchCorrespondenceForm batchCorrespondenceForm = new BatchCorrespondenceForm(vecParam) ;
                batchCorrespondenceForm.showForm() ;
            }    
            else if (actSource.equals(batchHistory))
            {
                Vector vecParam = new Vector() ;
                vecParam.add(0, committeeDetails.getCommitteeId()) ;
                vecParam.add(1,  committeeDetails.getCommitteeName()) ;
                                
                BatchCorrespondenceHistoryForm batchCorrespondenceHistoryForm = new BatchCorrespondenceHistoryForm(vecParam) ;
                batchCorrespondenceHistoryForm.showForm() ;
            }
                
            /** Case 683  - prps end mar 02 2004 **/
        }catch(Exception e){
            //e.printStackTrace();
            tabbedPane.setSelectedIndex(0);
            //COEUSQA-2802 - Allow user to remove values for areas of research in IRB and IACUC 
            if(e instanceof CoeusUIException) {
                CoeusUIException coeusUIException = (CoeusUIException)e;
                int tabindex = coeusUIException.getTabIndex();
                tabbedPane.setSelectedIndex(tabindex);
            }
            if( !(e instanceof PropertyVetoException) ){
                CoeusOptionPane.showErrorDialog(e.getMessage());
            }else{
                setVisible(true);
            }
        }
    }
    
    //prps start dec 23 2003
     public void showAdhocReports()
    {
      try
      {
        AdhocDetailsBean adhocDetailsBean = new AdhocDetailsBean() ;
        adhocDetailsBean.setCommitteeId(committeeId) ;
        adhocDetailsBean.setModule('C') ;
        //Added for case id COEUSQA-1724 iacuc protocol stream generation start.

        int committeeTypeCode = committeeDetails.getCommitteeTypeCode();
        if (committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE) {
            edu.mit.coeus.iacuc.gui.AdhocReportsForm adhocReportsForm = new edu.mit.coeus.iacuc.gui.AdhocReportsForm(adhocDetailsBean) ;
            adhocReportsForm.showForm() ;  
        }else {
            edu.mit.coeus.irb.gui.AdhocReportsForm adhocReportsForm = new edu.mit.coeus.irb.gui.AdhocReportsForm(adhocDetailsBean) ;
            adhocReportsForm.showForm() ;              
        }
        //Added for case id COEUSQA-1724 iacuc protocol stream generation end.

      }
      catch(Exception ex)
      {
        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("correspType_exceptionCode.1015")) ;
      }
    }
    //prps end dec 23 2003
    
    
    
    //udated for Row Locking. Subramanya
    private void releaseUpdateLock(){
        try{
            //connect to server and get org detail form
            String rowId = null;
            
            CommitteeMaintenanceFormBean commMntBean = getCommMaintFormBean();
            if ( functionType == 'M' ) {
                rowId = commMntBean.getCommitteeId();
                RequesterBean requester = new RequesterBean();
                requester.setDataObject(rowId);
                requester.setFunctionType('Z');
                String connectTo =CoeusGuiConstants.CONNECTION_URL+"/comMntServlet";
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
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
    
    /** This method called from Save Menu Item under File Menu.
     * Saves the changes made for all the tab pages in this screen.
     */
    public void saveActiveSheet() {
        try {
			if(isSaveRequired())//Added by Nadh for Save Confirmation Bug Fix 0n 6th Dec - 2004 - start
				cnclClose = false;
			isLockReleased = false; // end
            setCommitteeInfo();
        } catch(Exception e) {
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    public void saveAsActiveSheet() {
    }
    
    public void internalFrameActivated(InternalFrameEvent e)
    {
	if(functionType=='D' )
        {
            mdiForm.getFileMenu().setSaveEnabled(false);
            btnSaveCommittee.setEnabled(false);
        }
	else
        {
            mdiForm.getFileMenu().setSaveEnabled(true);
            btnSaveCommittee.setEnabled(true);
        }
        super.internalFrameActivated(e);
    }
     

    public void internalFrameDeActivated(InternalFrameEvent e)
    {
	mdiForm.getFileMenu().setSaveEnabled(true);
	super.internalFrameDeactivated(e);
    }
    
}
