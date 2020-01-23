
/*
 * ReportingReqBaseWindowController.java
 * Created on July 14, 2004, 8:28 PM
 * @author   bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.controller;

import edu.mit.coeus.award.bean.AwardBaseBean;
import edu.mit.coeus.award.bean.AwardContactDetailsBean;
import edu.mit.coeus.award.bean.AwardReportReqBean;
import edu.mit.coeus.award.controller.AwardAddReportingReqController;
import edu.mit.coeus.award.controller.ReportingRequirementsController;
import edu.mit.coeus.award.controller.RepRequirementController;
import edu.mit.coeus.award.gui.CustomizeRepReqForm;
import edu.mit.coeus.award.gui.ReportingReqBaseWindow;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.CoeusTabbedPane;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.user.gui.UserPreferencesForm;

import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.Cursor;
import java.util.Hashtable;
import java.beans.PropertyVetoException;
import javax.swing.JTabbedPane;
import java.beans.VetoableChangeListener;

import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CurrentLockForm;
import javax.swing.JOptionPane;

public class ReportingReqBaseWindowController extends RepRequirementController 
implements ActionListener,VetoableChangeListener{
    
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private ReportingReqBaseWindow reportingReqBaseWindow;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private String title;
    private AwardBaseBean awardBaseBean;
    private CoeusVector cvTabs;
    private CoeusVector cvReportData;
    private static final char GET_DISPLAY_AWARD_REPORTING_DETAILS = 'D';
    private static final char UPDATE_AWARD_REPORTING_DETAILS = 'F';
    private static final char UPDATE_AND_RELEASE_LOCK = 'J';
    private static final char RELEASE_LOCK = 'K';
    private static final String SERVLET = "/AwardReportReqMaintenanceServlet";
    public static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final String WINDOW_TITLE = "Reporting Requirements for Award - ";
    private static final String SAVE_CONFIRMATION = "saveConfirmCode.1002";
    private static final String NOT_AVAILABLE_MSG ="repRequirements_exceptionCode.1054";
    private JTabbedPane  tbdPnReports;
    private ReportingRequirementsController reportingRequirementsController;
    private Controller controllerArray[];
    private int groupColumns[];
    private int detailColumns[];
    private CustomizeRepReqForm customizeRepReqForm;
    private boolean isModified = false;
    private char saveType = ' ';
    private boolean closed = false;
    private ChangePassword changePassword;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Creates a new instance of ReportingReqBaseWindowController */
    public ReportingReqBaseWindowController(AwardBaseBean awardBaseBean,char functionType) {
        super(awardBaseBean);
        this.awardBaseBean =awardBaseBean;
        setFunctionType(functionType);
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryKey = awardBaseBean.getMitAwardNumber();
        if (getFunctionType()== TypeConstants.DISPLAY_MODE) {
            title = WINDOW_TITLE+awardBaseBean.getMitAwardNumber()+" Display";
        } else if (getFunctionType()== TypeConstants.MODIFY_MODE) {
            title = WINDOW_TITLE+awardBaseBean.getMitAwardNumber()+" Modify";
        }
        boolean isDataPresent = fetchData();
        if(!isDataPresent) {
            return;
        }
        reportingReqBaseWindow = new ReportingReqBaseWindow(title,getFunctionType(),mdiForm);
        registerComponents();
        createTabs();

    }
    /**
     *  Displays the window
     */
    public void display() {
        try {
        mdiForm.putFrame(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW, awardBaseBean.getMitAwardNumber(), getFunctionType(),reportingReqBaseWindow);
        javax.swing.JPanel basePanel = new javax.swing.JPanel();
        basePanel.setLayout(new java.awt.BorderLayout());
        basePanel.add(tbdPnReports);
        reportingReqBaseWindow.getContentPane().add(basePanel);
        mdiForm.getDeskTopPane().add(reportingReqBaseWindow);
        reportingReqBaseWindow.setSelected(true);
        reportingReqBaseWindow.setVisible(true);
        } catch(PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return null;
    }
    /**
     * Registers the listeners
     */
    public void registerComponents() {
        reportingReqBaseWindow.btnAdd.addActionListener(this);
        reportingReqBaseWindow.btnClose.addActionListener(this);
        reportingReqBaseWindow.btnCustomizeView.addActionListener(this);
        reportingReqBaseWindow.btnEdit.addActionListener(this);
        reportingReqBaseWindow.btnSave.addActionListener(this);
        reportingReqBaseWindow.btnSelectAll.addActionListener(this);
        
        reportingReqBaseWindow.mnuItmAdd.addActionListener(this);
        reportingReqBaseWindow.mnuItmChangePassword.addActionListener(this);
        reportingReqBaseWindow.mnuItmClose.addActionListener(this);
        reportingReqBaseWindow.mnuItmExit.addActionListener(this);
        reportingReqBaseWindow.mnuItmInbox.addActionListener(this);
        reportingReqBaseWindow.mnuItmModify.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        reportingReqBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        reportingReqBaseWindow.mnuItmPreferences.addActionListener(this);
        reportingReqBaseWindow.mnuItmPrintSetup.addActionListener(this);
        reportingReqBaseWindow.mnuItmSave.addActionListener(this);
        reportingReqBaseWindow.mnuItmSelectAll.addActionListener(this);
        //Case 2110 Start
        reportingReqBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        //reportingReqBaseWindow.setDefaultCloseOperation(edu.mit.coeus.gui.CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        reportingReqBaseWindow.addVetoableChangeListener(this);
    }
    /**
     * Saves the data. Also it refreshes the data.
     */
    public void saveFormData() {
        ReportingRequirementsController reportingRequirementsController;
        for (int index=0;index<controllerArray.length;index++) {
            reportingRequirementsController = (ReportingRequirementsController)controllerArray[index];
            reportingRequirementsController.saveFormData();
        }
        Hashtable htDataToSend = queryEngine.getDataCollection(queryKey);
        CoeusVector cvSaveData = (CoeusVector)htDataToSend.get(AwardReportReqBean.class);
        Equals eqAcTypeUpdate = new Equals("acType",TypeConstants.UPDATE_RECORD);
        Equals eqAcTypeInsert = new Equals("acType",TypeConstants.INSERT_RECORD);
        Or orUpdateOrInsert = new Or(eqAcTypeUpdate,eqAcTypeInsert);
        CoeusVector cvSaveDataFiltered=cvSaveData.filter(orUpdateOrInsert);
        if (cvSaveDataFiltered.size()<1) {
            return;
        }
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(saveType);
        requesterBean.setDataObject(htDataToSend);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getException().getMessage());
        } else { //success
            if (saveType == UPDATE_AWARD_REPORTING_DETAILS) {
               Hashtable htData=(Hashtable)responderBean.getDataObject();
               CoeusVector cvReportData = (CoeusVector) htData.get(AwardReportReqBean.class);
               queryEngine.removeData(queryKey, AwardReportReqBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
               queryEngine.addCollection(queryKey,AwardReportReqBean.class,cvReportData);
               for (int index=0;index<cvTabs.size();index++) {
                   ComboBoxBean classBean = (ComboBoxBean)cvTabs.get(index);
                   reportingRequirementsController = (ReportingRequirementsController)controllerArray[index];
                   reportingRequirementsController.setTableModified(false);
                   Equals eqClassCode = new Equals("reportClassCode",new Integer(Integer.parseInt(classBean.getCode())));
                   CoeusVector cvCurrentTabData = cvReportData.filter(eqClassCode);
                   reportingRequirementsController.setFormData(cvCurrentTabData);
               } // for loop ends
               isModified = false; 
            } // update only ends
        } //success ends 
    }
    /**
     * Releases the lock for the award
     */
    private void releaseLock () {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(RELEASE_LOCK);
        requesterBean.setDataObject(awardBaseBean.getMitAwardNumber());
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getException().getMessage());
        }
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    /**
     * Handlers for menu items and tool bar buttons
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            reportingReqBaseWindow.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            Object source = actionEvent.getSource();
            if (source.equals(reportingReqBaseWindow.btnAdd) || source.equals(reportingReqBaseWindow.mnuItmAdd)) {
                AwardAddReportingReqController awardAddReportingReqController = new AwardAddReportingReqController(awardBaseBean,getFunctionType());
                ComboBoxBean reportClassBean = (ComboBoxBean)cvTabs.get(tbdPnReports.getSelectedIndex());
                awardAddReportingReqController.setFormData(reportClassBean);
                int clickedItem = awardAddReportingReqController.displayForm();
                if (clickedItem==awardAddReportingReqController.OK_CLICKED) {
                    isModified = true;
                    setRefreshRequired(true);
                    refresh();
                }
            } else if (source.equals(reportingReqBaseWindow.btnClose) || source.equals(reportingReqBaseWindow.mnuItmClose)) {
                try {
                    close();
                }catch (PropertyVetoException propertyVetoException) {
                    
                }
            } else if (source.equals(reportingReqBaseWindow.btnCustomizeView)) {
                showCustomizedView();
            } else if (source.equals(reportingReqBaseWindow.btnEdit) || source.equals(reportingReqBaseWindow.mnuItmModify)) {
                int selectedTabIndex= tbdPnReports.getSelectedIndex();
                ReportingRequirementsController repController = (ReportingRequirementsController)controllerArray[selectedTabIndex];
                repController.editReportingRequirements();
                setRefreshRequired(true);
                refresh();
            } else if (source.equals(reportingReqBaseWindow.btnSave) || source.equals(reportingReqBaseWindow.mnuItmSave)) {
                saveType = UPDATE_AWARD_REPORTING_DETAILS;
                saveFormData();
            } else if (source.equals(reportingReqBaseWindow.btnSelectAll) || source.equals(reportingReqBaseWindow.mnuItmSelectAll)) {
                int selectedTabIndex= tbdPnReports.getSelectedIndex();
                ReportingRequirementsController repController = (ReportingRequirementsController)controllerArray[selectedTabIndex];
                repController.selectAll();
            } else if (source.equals(reportingReqBaseWindow.mnuItmChangePassword)) {
                showChangePassword();
                //Added for Case#3682 - Enhancements related to Delegations - Start
            } else if (source.equals(reportingReqBaseWindow.mnuItmDelegations)) {
                displayUserDelegation();
                //Added forCase#3682 - Enhancements related to Delegations - End
            } else if (source.equals(reportingReqBaseWindow.mnuItmPreferences)) {
                showPreference();
             //start of bug fix id 1651
            } else if(source.equals(reportingReqBaseWindow.mnuItmExit)) {
                exitApplication();
            }//end of bug fix id 1651
            //Case 2110 Start
            else if(source.equals(reportingReqBaseWindow.mnuItmCurrentLocks)){
               showLocksForm(); 
            }
            //Case 2110 End
             else {
                CoeusOptionPane.showInfoDialog("Functionality not implemented");
            }  /*else if (source.equals(reportingReqBaseWindow.mnuItmExit)) {
                
            } else if (source.equals(reportingReqBaseWindow.mnuItmInbox)) {
                
            } else if (source.equals(reportingReqBaseWindow.mnuItmPreferences)) {
                
            } else if (source.equals(reportingReqBaseWindow.mnuItmPrintSetup)) {
                
            } */
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally {
            reportingReqBaseWindow.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }
    
     //Added by shiji for bug fix id 1651
     public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
     }//End bug fix id 1651
    
     //Added for Case#3682 - Enhancements related to Delegations - Start
     /*
      * Display Delegations window
      */
     private void displayUserDelegation() {
         userDelegationForm = new UserDelegationForm(mdiForm,true);
         userDelegationForm.display();
     }
     //Added for Case#3682 - Enhancements related to Delegations - End
     
     
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha
    
    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //CAse 2110 Start To get the Current Locks of the USer
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //CAse 2110 ENd
    
    /** 
     * Shows the customized view window
     */
     private void showCustomizedView() {
         int selTabIndex = tbdPnReports.getSelectedIndex();
         int selectedDetailedIndex = ((ReportingRequirementsController)controllerArray[selTabIndex]).getSelectedDetailItem();
         if (customizeRepReqForm == null) {
             customizeRepReqForm = new CustomizeRepReqForm();
         }
         int returnVal = customizeRepReqForm.display();
         if (returnVal==customizeRepReqForm.OK_CLICKED) {
             groupColumns = customizeRepReqForm.getGroupColumns();
             detailColumns = customizeRepReqForm.getDetailColumns();
             ReportingRequirementsController repReqController ;
             for (int index=0; index < controllerArray.length;index++) {
                 repReqController = (ReportingRequirementsController)controllerArray[index];
                 repReqController.customizeView(groupColumns,detailColumns);
                 //For Bugfix 1151
                 
             }
             for (int index=0; index < controllerArray.length;index++) {
                 repReqController = (ReportingRequirementsController)controllerArray[index];
                 repReqController.removeGroupListener();
                 repReqController.movingColumns(groupColumns);
                 repReqController.addGroupListener();
                 repReqController.sortGroup();
                 // Bug Fix 2101 - start
                 if(repReqController.reportingRequirementsForm.tblGroup.getRowCount() > 0){
                     repReqController.reportingRequirementsForm.tblGroup.setRowSelectionInterval(0,0);
                 }// Bug Fix 2101 - End
             }
             
             
         }
         ((ReportingRequirementsController)controllerArray[selTabIndex]).setSelectedDetailItem(selectedDetailedIndex);
     }
    /**
     * Method to close the window.
     */
    public void close() throws PropertyVetoException {
        ReportingRequirementsController controller;
        int selTabIndex = tbdPnReports.getSelectedIndex();
        int selectedDetailedIndex = ((ReportingRequirementsController)controllerArray[selTabIndex]).getSelectedDetailItem();
        for (int index=0; index<controllerArray.length;index++) {
            controller=(ReportingRequirementsController)controllerArray[index];
            controller.stopEditingTable();
            if(controller.isTableModified()) {
                isModified = true;
            }
        }
        if(isModified) {
            int selectionOption = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CONFIRMATION),CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if (selectionOption==CoeusOptionPane.SELECTION_CANCEL) {
                ((ReportingRequirementsController)controllerArray[selTabIndex]).setSelectedDetailItem(selectedDetailedIndex);
                throw new PropertyVetoException("", null);
            } else if (selectionOption == CoeusOptionPane.SELECTION_YES) {
                saveType = UPDATE_AND_RELEASE_LOCK;
                saveFormData();
            } else if (selectionOption == CoeusOptionPane.SELECTION_NO ) {
                if (getFunctionType()!=TypeConstants.DISPLAY_MODE) {
                    releaseLock();
                }
            }
        } else {
            if (getFunctionType()!=TypeConstants.DISPLAY_MODE) {
                releaseLock();
            }
        }
        mdiForm.removeFrame(CoeusGuiConstants.REPORTING_REQ_BASE_WINDOW,awardBaseBean.getMitAwardNumber());
        closed=true;
        reportingReqBaseWindow.doDefaultCloseAction();
        
    }
    /**
     * To get the data from the server
     */
    private boolean fetchData() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_DISPLAY_AWARD_REPORTING_DETAILS);
        AwardReportReqBean awardReportReqBean = new AwardReportReqBean();
        awardReportReqBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        requesterBean.setDataObject(awardReportReqBean);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
         if(responderBean.isSuccessfulResponse()) {
             Hashtable reportData = (Hashtable)responderBean.getDataObject();
             boolean isDataPresent = extractReportToQueryEngine(reportData);
             return isDataPresent;
         } else {
             CoeusOptionPane.showErrorDialog(responderBean.getException().getMessage());
         }
        return false;
    }
    /**
     * REfresehes the window
     */
    public void refresh() {
        if(!isRefreshRequired()) return ;
        try{
            int selectedIndex = tbdPnReports.getSelectedIndex();
            ComboBoxBean classBean = (ComboBoxBean)cvTabs.get(selectedIndex);
            cvReportData = (CoeusVector)queryEngine.executeQuery(queryKey,AwardReportReqBean.class,CoeusVector.FILTER_ACTIVE_BEANS);
            Controller controller = controllerArray[selectedIndex];
            Equals eqClassCode = new Equals("reportClassCode",new Integer(Integer.parseInt(classBean.getCode())));
            CoeusVector cvCurrentTabData = cvReportData.filter(eqClassCode);
            controller.setFormData(cvCurrentTabData);
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
    }
    
    /**
     * To put the data to the queryEngine
     */
    private boolean extractReportToQueryEngine(Hashtable reportData) {
        Hashtable htReportData = new Hashtable();
        cvReportData = (CoeusVector) reportData.get(AwardReportReqBean.class);
        if (cvReportData!=null) {
            htReportData.put(AwardReportReqBean.class, cvReportData);
        }
        if (cvReportData==null || cvReportData.size()<1) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NOT_AVAILABLE_MSG)+awardBaseBean.getMitAwardNumber());
            return false;
        }
        CoeusVector cvStatusData = (CoeusVector) reportData.get(KeyConstants.AWARD_REPORT_STATUS);
        
        if (cvStatusData!=null) {
            htReportData.put(KeyConstants.AWARD_REPORT_STATUS, cvStatusData);
        }
        CoeusVector cvContactdetailsData = (CoeusVector) reportData.get(AwardContactDetailsBean.class);
        if (cvContactdetailsData!=null) {
            htReportData.put(AwardContactDetailsBean.class, cvContactdetailsData);
        }
        CoeusVector cvContactTypeData = (CoeusVector) reportData.get(KeyConstants.AWARD_CONTACT_TYPE);
        if (cvContactTypeData!=null) {
            htReportData.put(KeyConstants.CONTACT_TYPES, cvContactTypeData);
        }
        queryEngine.addDataCollection(queryKey, htReportData);
        cvTabs = (CoeusVector)reportData.get(ComboBoxBean.class);
        return true;
    }
    
    /**
     * To create  the tabs dynamically
     */
    private void createTabs() {
        if (cvTabs == null ) {
            return;
        }
        Container reportBaseContainer = reportingReqBaseWindow.getContentPane();
        tbdPnReports = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        controllerArray = new Controller[cvTabs.size()];
        for (int index=0;index<cvTabs.size();index++) {
           ComboBoxBean classBean = (ComboBoxBean)cvTabs.get(index);
           reportingRequirementsController = new ReportingRequirementsController(awardBaseBean,getFunctionType());
           Equals eqClassCode = new Equals("reportClassCode",new Integer(Integer.parseInt(classBean.getCode())));
           CoeusVector cvCurrentTabData = cvReportData.filter(eqClassCode);
           reportingRequirementsController.setFormData(cvCurrentTabData);
           tbdPnReports.add(classBean.getDescription(),reportingRequirementsController.getControlledUI());
           controllerArray[index] = reportingRequirementsController;
        }
        reportBaseContainer.add(tbdPnReports);
    }
    
    /**
     * Getter for property groupColumns.
     * @return Value of property groupColumns.
     */
    public int[] getGroupColumns() {
        return this.groupColumns;
    }
    
    /**
     * Setter for property groupColumns.
     * @param groupColumns New value of property groupColumns.
     */
    public void setGroupColumns(int[] groupColumns) {
        this.groupColumns = groupColumns;
    }
    
    /**
     * Getter for property detailColumns.
     * @return Value of property detailColumns.
     */
    public int[] getDetailColumns() {
        return this.detailColumns;
    }
    
    /**
     * Setter for property detailColumns.
     * @param detailColumns New value of property detailColumns.
     */
    public void setDetailColumns(int[] detailColumns) {
        this.detailColumns = detailColumns;
    }
    
    public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(javax.swing.JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
}

