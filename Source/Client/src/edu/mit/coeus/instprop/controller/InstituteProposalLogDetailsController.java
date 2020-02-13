/*
 * InstituteProposalLogDetails.java
 *
 * Created on May 10, 2004, 2:09 PM
 * @author  ajaygm
 *
 */

/* PMD check performed, commented the unused imports on 05-MAY-2011 by Bharati
 */

package edu.mit.coeus.instprop.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.instprop.bean.*;
import edu.mit.coeus.instprop.gui.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.unit.gui.*;
import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.rolodexmaint.gui.*;
import edu.mit.coeus.departmental.gui.*;
import edu.mit.coeus.sponsormaint.gui.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
//import javax.jnlp.UnavailableServiceException;
//case 3263 start
import java.text.SimpleDateFormat;
import java.text.ParseException;
//case 3263 end





public class InstituteProposalLogDetailsController extends InstituteProposalController 
implements ActionListener, ItemListener{
//,FocusListener{
    
    /** Holds an instance of <CODE>CommentsHistoryForm</CODE> */
    private InstituteProposalLogDetailsForm instituteProposalLogDetailsForm = new InstituteProposalLogDetailsForm();;
     
    /**
     * To create an instance of MDIform
     */    
    private CoeusAppletMDIForm mdiForm  = CoeusGuiConstants.getMDIForm();
    /**
     * Instance of the Dialog
     */    
    private CoeusDlgWindow dlgInstituteProposalLogDetailsController;
    /**
     * Instance of Coeus Message Resources
     */    
    private CoeusMessageResources coeusMessageResources;
    /**
     * Instance of Query Engine
     */    
    private QueryEngine queryEngine;
    /**
     * Coeus Vector which contains Status Comboboxbean
     */    
    private CoeusVector cvpstatus = new CoeusVector();
    
    /**
     * Coeus Vector which contains Proposal Type Comboboxbean
     */    
    private CoeusVector cvPType = new CoeusVector();
    private CoeusSearch sponsorSearch;
    private edu.mit.coeus.utils.Utils Utils;    
     
    // Represents the bean class for storing Locally data from server 
    private InstituteProposalLogBean myIPlogbean;
    
    // Represents the bean class for storing data from server 
    private InstituteProposalLogBean ipLogBeanFromServer;
    
    //Represents the string for conneting to the servlet and getting Proposal Log data 
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    
    //Represents the string for conneting to the servlet and getting Rolodex data for Rolodex Search
    private static final String ROLODEX_SERVLET = "/rolMntServlet";
    

    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
   
    //Function Type for saving new data  
    private static final char NEW_PROPOSAL_DATA = 'G' ;

    //Function Type for Modifying new data  
    private static final char MODIFY_PROPOSAL = 'H';
    
    //Function Type to get sponsor name 
    private static final char GET_SPONSOR_NAME = 'S';
    
    //Function Type for checking for new data 
    private static final char CHECK_FOR_TEMP_PROPOSAL = 'K';
    
        //Function Type for printing proposal log 
    private static final char PRINT_PROPOSAL_LOG = 'L';

    
    //For checking if any data is changed 
    private boolean dataChanged = false;
    
    //For use in validation
    private String oldPersonInfo = "";
    
    //Coeus Vector for saving the status got from the server in new and temp mode
    private CoeusVector cvStatus;
    
    //check for the click of button
    /**
     * Flag for Cancel Clicked
     */    
    public static final int CANCEL_CLICKED = 0;
    /**
     * Flag for OK Clicked
     */    
    public static final int OK_CLICKED = 1;
    
    //For sponsor search
    private static final String SPONSOR_SEARCH = "sponsorSearch";
    private static final String SPONSOR_CODE = "SPONSOR_CODE";
    private static final String SPONSOR_NAME = "SPONSOR_NAME";
    private String sponsorName, sponsorCode;
    private String personId;
    private String lastPerson;
    //for saving proposal number form server 
    private static String proposalNumber = null;
    
    
    private static boolean nonMITPerson ;
    private static boolean validation = false;
    private static boolean validateFlag = false;
    private static boolean cancelValidate = false;
    private static boolean yesClicked = false;
    private static boolean inside = false;
    
    /**
     * An Empty String
     */    
    public final static String EMPTY = "";
    
    //Titles for window in diffferent modes 
    private static final String WINDOW_TITLE_FOR_NEW_LOG = "Add Proposal Log";
    private static final String WINDOW_TITLE_FOR_TEMP_LOG = "Create Temporary Proposal Log";
    private static final String WINDOW_TITLE_FOR_MODIFY = "Modify Proposal Log";
    private static final String WINDOW_TITLE_FOR_DISPLAY = "Display Proposal Log";
    
    
    //for setting dimentions
    private static final int WIDTH = 620;
    private static final int HEIGHT =  295;
    
    //for validations 
    private static final String ENTER_TITLE = "instPropLog_exceptionCode.1405";
    private static final String ENTER_PI = "instPropLog_exceptionCode.1406";
    private static final String LEAD_UNIT = "instPropLog_exceptionCode.1407";
    private static final String PROPOSAL_TYPE = "instPropLog_exceptionCode.1408";
    private static final String STATUS_CHANGE = "instPropLog_exceptionCode.1409";
    private static final String GENERATE_NUMBER = "instPropLog_exceptionCode.1411";
    
    private static final String SAVE_CONFIRM = "saveConfirmCode.1002";
    private static final String UNIT_DISPLAY_ERR = "protoInvFrm_exceptionCode.1136";
    private static final String SPONSOR_DISPLAY_ERR = "awardDetail_exceptionCode.1051";
    private static final String UNIT_VALIDATION_ERR = "protoInvFrm_exceptionCode.1138";
    private static final String PERSON_VALIDATION_ERR = "investigator_exceptionCode.1007";
    private static final String SOPNSOR_VALIDATION_ERR = "prop_invalid_sponsor_exceptionCode.2509";
    private static String propDescription ;
    private static final String CODE = "code";
    
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    
    //Listerner for updating to base window
    private BaseWindowObservable observable  = new BaseWindowObservable();
    private ComboBoxBean statusComboBean = new ComboBoxBean();
    
    /*For Bug Fix:1666 start step:1 start*/
    private static final char GET_VALID_SPONSOR_CODE = 'P';
    private static final String EMPTY_STRING = "";
    private static final String SERVER_ERROR = "Server Error";
    private static final String COULD_NOT_CONTACT_SERVER = "Could Not Contact Server";
    /*step:1 end*/
    
    //case 3263 start
    /** Date utils. */
    private DateUtils dateUtils = new DateUtils();
    private static final String INVALID_DATE = "Please enter a valid deadline date." ;
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yy";
    private static final String LAST_UPDATE_FORMAT = DATE_FORMAT_DISPLAY + " hh:mm a";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    //case 3263 end
    
    //Added for Case#3587 - multicampus enhancement  - Start
    private static final String CANNOT_CREATE_TEMPORARY_LOG = "instPropLog_exceptionCode.1413";
    private static final String CANNOT_CREATE_PROPOSAL_LOG = "instPropLog_exceptionCode.1412";
    private static final String CANNOT_MODIFY_PROPOSAL_LOG = "instPropLog_exceptionCode.1414";
    private static final String CANNOT_MODIFY_TEMPORARY_LOG = "instPropLog_exceptionCode.1416";
    private static final String CREATE_TEMPORARY_LOG = "CREATE_TEMPORARY_LOG";
    private static final String CREATE_PROPOSAL_LOG = "CREATE_PROPOSAL_LOG";
    private static final char TEMPORARY_PROPOSAL_LOG = 'T';
    private String leadUnit=EMPTY_STRING;
    //Case#3587 - End
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    private static final String TEMPORARY_PROPOSAL_LOG_STATUS = "T";
    private static final String DISCLOSURE_PROPOSAL_LOG_STATUS = "D";
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    private String sponsorStatus = CoeusGuiConstants.EMPTY_STRING;
    private static final String INACTIVE_STATUS = "I";
    boolean sponsorChanged = false;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    
    /** Creates a new instance of InstituteProposalLogDetailsController
    * @param proposalNumber, functionType.
    */
    public InstituteProposalLogDetailsController(String proposalNumber, char functionType){
        queryEngine = QueryEngine.getInstance();
        this.proposalNumber = proposalNumber;
        myIPlogbean = new InstituteProposalLogBean();
        ipLogBeanFromServer = new InstituteProposalLogBean(); 
        coeusMessageResources = CoeusMessageResources.getInstance();
        //CoeusVector cvStatus = new CoeusVector();
        registerComponents();
        postInitComponents();
        setFunctionType(functionType);
        observable.setFunctionType(functionType);
        setFormData(proposalNumber);
    }
    
     /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgInstituteProposalLogDetailsController = new CoeusDlgWindow(mdiForm);
        dlgInstituteProposalLogDetailsController.setResizable(false);
        dlgInstituteProposalLogDetailsController.setModal(true);
        dlgInstituteProposalLogDetailsController.getContentPane().add(instituteProposalLogDetailsForm);
        dlgInstituteProposalLogDetailsController.setFont(CoeusFontFactory.getLabelFont());
        dlgInstituteProposalLogDetailsController.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgInstituteProposalLogDetailsController.getSize();
        dlgInstituteProposalLogDetailsController.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgInstituteProposalLogDetailsController.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgInstituteProposalLogDetailsController.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    requestDefaultFocus();
                }
        });
        
        //code for disposing the window 
        dlgInstituteProposalLogDetailsController.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    performDisposeWIndow();
                }catch (CoeusUIException coeusUIException){
                    CoeusOptionPane.showDialog(coeusUIException);
                }
            }
        });
       
        
        dlgInstituteProposalLogDetailsController.addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent we){
                try{
                    performDisposeWIndow();
                }catch (CoeusUIException coeusUIException){
                    CoeusOptionPane.showDialog(coeusUIException);
                }
             }
        });
     //code for disposing the window ends
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        dlgInstituteProposalLogDetailsController.setVisible(true);
    }
   
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        instituteProposalLogDetailsForm.txtProposalNumber.setEnabled(false);
        instituteProposalLogDetailsForm.chkEmployee.setSelected(true);
        
        //Set the widow title depending on mode 
        if(getFunctionType() == 'A'){
            dlgInstituteProposalLogDetailsController.setTitle(WINDOW_TITLE_FOR_NEW_LOG);
            instituteProposalLogDetailsForm.btnPrint.setEnabled(false);
        }else if (getFunctionType() == 'T'){
            dlgInstituteProposalLogDetailsController.setTitle(WINDOW_TITLE_FOR_TEMP_LOG);
            instituteProposalLogDetailsForm.btnPrint.setEnabled(false);
        }else if(getFunctionType() == 'M'){
            dlgInstituteProposalLogDetailsController.setTitle(WINDOW_TITLE_FOR_MODIFY);
        }else if(getFunctionType() == 'D'){
            dlgInstituteProposalLogDetailsController.setTitle(WINDOW_TITLE_FOR_DISPLAY);
        }  
        
        //enabling disabling the buttons 
        if(getFunctionType() == 'M' || getFunctionType() == 'D'){
            instituteProposalLogDetailsForm.btnClose.setVisible(false);
            instituteProposalLogDetailsForm.btnLogProposal.setVisible(false);
            instituteProposalLogDetailsForm.btnOK.setVisible(true);
            instituteProposalLogDetailsForm.btnCancel.setVisible(true);
            instituteProposalLogDetailsForm.btnClose.setEnabled(false);
            instituteProposalLogDetailsForm.btnLogProposal.setEnabled(false);
        }else{
            instituteProposalLogDetailsForm.btnCancel.setEnabled(false);
            instituteProposalLogDetailsForm.btnOK.setEnabled(false);
            instituteProposalLogDetailsForm.btnClose.setVisible(true);
            instituteProposalLogDetailsForm.btnLogProposal.setVisible(true);
            instituteProposalLogDetailsForm.btnOK.setVisible(false);
            instituteProposalLogDetailsForm.btnCancel.setVisible(false);
        }
        if(getFunctionType() == 'D'){
            instituteProposalLogDetailsForm.btnOK.setEnabled(false);
        }
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start        
        instituteProposalLogDetailsForm.txtLogMerged.setEditable(false);
        instituteProposalLogDetailsForm.txtLogMerged.setEnabled(false);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    }
    
    /** An overridden method of the controller
    * @return instituteProposalLogDetailsForm returns the controlled form component
    */
    public Component getControlledUI() {
          return instituteProposalLogDetailsForm;
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
        //Add listeners to all the buttons 
        instituteProposalLogDetailsForm.btnClose.addActionListener(this);
        instituteProposalLogDetailsForm.btnLogProposal.addActionListener(this);
        instituteProposalLogDetailsForm.btnOK.addActionListener(this);
        instituteProposalLogDetailsForm.btnCancel.addActionListener(this);
        instituteProposalLogDetailsForm.btnPrint.addActionListener(this);
        instituteProposalLogDetailsForm.btnPINameSearch.addActionListener(this);
        instituteProposalLogDetailsForm.btnLeadUnitSearch.addActionListener(this);
        instituteProposalLogDetailsForm.btnSponsorSearch.addActionListener(this);
        instituteProposalLogDetailsForm.txtLeadUnit.addActionListener(this);
        instituteProposalLogDetailsForm.txtPIName.addActionListener(this);
        instituteProposalLogDetailsForm.txtSponsorID.addActionListener(this);
        instituteProposalLogDetailsForm.txtSponsorName.addActionListener(this);
        instituteProposalLogDetailsForm.chkEmployee.addActionListener(this);
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { 
        //case 3263 start
            instituteProposalLogDetailsForm.txtDeadlineDate,
        //case 3263 end
        instituteProposalLogDetailsForm.cmbProposalType,
        instituteProposalLogDetailsForm.cmbStatus,instituteProposalLogDetailsForm.txtArTitle,
        instituteProposalLogDetailsForm.txtPIName,instituteProposalLogDetailsForm.btnPINameSearch,
        instituteProposalLogDetailsForm.chkEmployee,instituteProposalLogDetailsForm.txtLeadUnit,
        instituteProposalLogDetailsForm.btnLeadUnitSearch,instituteProposalLogDetailsForm.txtSponsorID,
        instituteProposalLogDetailsForm.btnSponsorSearch,instituteProposalLogDetailsForm.txtSponsorName,
        instituteProposalLogDetailsForm.txtArComments,instituteProposalLogDetailsForm.btnLogProposal,
        instituteProposalLogDetailsForm.btnCancel,instituteProposalLogDetailsForm.btnClose,
        instituteProposalLogDetailsForm.btnOK,instituteProposalLogDetailsForm.btnPrint
        };
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        instituteProposalLogDetailsForm.setFocusTraversalPolicy(traversePolicy);
        instituteProposalLogDetailsForm.setFocusCycleRoot(true);
        
        CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        instituteProposalLogDetailsForm.txtSponsorID.addFocusListener(customFocusAdapter);
        instituteProposalLogDetailsForm.txtLeadUnit.addFocusListener(customFocusAdapter);
        instituteProposalLogDetailsForm.txtPIName.addFocusListener(customFocusAdapter);
        
        UnitDisplayAdapter unitDisplayAdapter = new UnitDisplayAdapter();
        instituteProposalLogDetailsForm.txtLeadUnit.addMouseListener(unitDisplayAdapter);
        instituteProposalLogDetailsForm.lblLeadUnitName.addMouseListener(unitDisplayAdapter);
        
        instituteProposalLogDetailsForm.txtPIName.addMouseListener(new InvestigatorDetailsAdapter());
        
        SponsorDisplayAdapter sponsorDisplayAdapter = new SponsorDisplayAdapter();
        instituteProposalLogDetailsForm.txtSponsorID.addMouseListener(sponsorDisplayAdapter);
        instituteProposalLogDetailsForm.txtSponsorName.addMouseListener(sponsorDisplayAdapter);
        
        instituteProposalLogDetailsForm.cmbStatus.addItemListener(this);
        // Case# 3822: Increase the length of the proposal title to 200 characters- Start
//        //Added by shiji for bug fix id : 1884 :start
//         instituteProposalLogDetailsForm.txtArTitle.setDocument(new LimitedPlainDocument(150));
//        //bug id : 1884 : end
        instituteProposalLogDetailsForm.txtArTitle.setDocument(new LimitedPlainDocument(200));
         // Case# 3822: Increase the length of the proposal title to 200 characters- End
         //Case 2848 - START
         instituteProposalLogDetailsForm.txtArComments.setDocument(new LimitedPlainDocument(300));
         //Case 2848 - END
        /** Code for focus traversal - end */
        //case 3263 start
        instituteProposalLogDetailsForm.txtDeadlineDate.setDocument(new LimitedPlainDocument(11));
//        if(functionType!= 'D'){
            instituteProposalLogDetailsForm.txtDeadlineDate.addFocusListener(customFocusAdapter);           
//        }
        //case 3263 end
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
        instituteProposalLogDetailsForm.txtLogMerged.setEditable(false);
        instituteProposalLogDetailsForm.txtLogMerged.setEnabled(false);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    } 
    
    
    /**
     * Registers the Observer
     */    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /** To set the default focus for the component
    */
    public void requestDefaultFocus(){    
        if( getFunctionType() == 'D' ){
            instituteProposalLogDetailsForm.btnCancel.requestFocus();
        }else{
            instituteProposalLogDetailsForm.cmbProposalType.requestFocus();
        }
    }
        
    /** Saves the Form Data.
    */
    public void saveFormData() {
        
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        this.proposalNumber = (String) data;
        try{
            if(getFunctionType() == 'A'){
                performAddLog();
            }else if(getFunctionType()=='T'){
                performTempLog();
            }else if(getFunctionType() == 'M' || getFunctionType() == 'D'){
                performModifyOperation();
            }
        }catch (CoeusUIException coeusUIException){
            CoeusOptionPane.showDialog(coeusUIException);
            coeusUIException.printStackTrace();
        }
     }
    
    
    //get the data for combo Box from server in New Mode 
    private Hashtable getNewProposalData() throws CoeusUIException{
        // To get the return values for the  ipLog 
        Hashtable htProposalData = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(NEW_PROPOSAL_DATA);

        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            htProposalData = (Hashtable)responderBean.getDataObject();
        }else{
            throw new CoeusUIException(responderBean.getMessage(), CoeusUIException.ERROR_MESSAGE);
        }
        return htProposalData;
    }
    
    //get the data for Modify mode from server  
    private Hashtable getModifyData() throws CoeusUIException{
        // To get the return values for the ipLog
        Hashtable htModifyData = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType(MODIFY_PROPOSAL);
        requesterBean.setDataObject(proposalNumber);

        AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        
        if(responderBean.isSuccessfulResponse()) {
            htModifyData = (Hashtable)responderBean.getDataObject();
        }else{
            throw new CoeusUIException(responderBean.getMessage(), CoeusUIException.ERROR_MESSAGE);
        }
        return htModifyData;
    }
    
    
    /**
     * For validating the fields
     */    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        cancelValidate = false;
        if(inside){
            validation = false;
        }
        String selItem = instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString();
        
        //validation for proposalType
        if(selItem.equals(EMPTY_STRING)){
           instituteProposalLogDetailsForm.cmbProposalType.requestFocusInWindow(); 
           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PROPOSAL_TYPE));
           return false;
        }else if(instituteProposalLogDetailsForm.txtArTitle.getText().trim().equals(EMPTY_STRING)){                  //validation for title
            instituteProposalLogDetailsForm.txtArTitle.requestFocusInWindow();   
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_TITLE));
            return false;
        }else if(instituteProposalLogDetailsForm.txtPIName.getText().trim().equals(EMPTY_STRING) && !validation){               //validation for PI name 
            instituteProposalLogDetailsForm.txtPIName.requestFocusInWindow();   
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_PI));
            return false;
        }else if(instituteProposalLogDetailsForm.txtLeadUnit.getText().trim().equals(EMPTY_STRING)){            //validation for Lead Unit 
            instituteProposalLogDetailsForm.txtLeadUnit.requestFocusInWindow();  
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(LEAD_UNIT));
            return false;
        }
        //Added for Case#3587 - multicampus enhancement  - Start
        String leadUnitNumber = instituteProposalLogDetailsForm.txtLeadUnit.getText()==null ? EMPTY_STRING : instituteProposalLogDetailsForm.txtLeadUnit.getText().trim();
        if(!leadUnitNumber.equals(EMPTY_STRING)){
            boolean hasRight = false;
            if((this.proposalNumber != null && this.proposalNumber.indexOf(TEMPORARY_PROPOSAL_LOG) > -1) || getFunctionType() == 'T') {
                if(!leadUnit.equalsIgnoreCase(leadUnitNumber)){
                    hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,CREATE_TEMPORARY_LOG);
                    if(!hasRight){
                        if(getFunctionType() == 'M'){
                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CANNOT_MODIFY_TEMPORARY_LOG));
                            return false;
                        }else{
                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CANNOT_CREATE_TEMPORARY_LOG));
                            return false;
                        }
                    }
                }
            }else {
                if(!leadUnit.equalsIgnoreCase(leadUnitNumber)){
                    hasRight = checkUserHasRightInLeadUnit(leadUnitNumber,CREATE_PROPOSAL_LOG);
                    if(!hasRight){
                        if(getFunctionType() == 'M'){
                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CANNOT_MODIFY_PROPOSAL_LOG));
                            return false;
                        }else{
                            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CANNOT_CREATE_PROPOSAL_LOG));
                            return false;
                        }
                    }
                }
            }
        }
        //Case#3587 - End
     cancelValidate = true;   
     return true;
    }
    
     /***
     * action performed
     * @param ActionEvent actionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource ();
        try{
            if(source.equals(instituteProposalLogDetailsForm.btnClose)){
                boolean flag = isDatachanged();
                if(flag == true){
                    performUpdate();
                    if(validation){
                        myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                        observable.notifyObservers(myIPlogbean);
                    }
                }else{
                        dlgInstituteProposalLogDetailsController.dispose();
                        if(ipLogBeanFromServer.getUpdateTimestamp () != null) {
                            observable.notifyObservers(ipLogBeanFromServer);
                        }
                }
            }else if(source.equals(instituteProposalLogDetailsForm.btnLogProposal)){
                performLogOperation();
            }else if(source.equals(instituteProposalLogDetailsForm.btnOK)){
                ipLogBeanFromServer.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                boolean flag = isDatachanged();
                if(flag == false){
                    dlgInstituteProposalLogDetailsController.dispose();
                }else{
                     performLogOperation();
                     if(cancelValidate){
                         myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                         observable.notifyObservers(myIPlogbean);
                         dlgInstituteProposalLogDetailsController.dispose();
                    }
                }
            }else if(source.equals(instituteProposalLogDetailsForm.btnCancel)){
                if(getFunctionType() == 'M'){
                    ipLogBeanFromServer.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                    boolean flag = isDatachanged();
                    if(flag == true){
                        validation = false;
                        performUpdate();
                         if(yesClicked == true){
                             myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                             observable.notifyObservers(myIPlogbean);
                         }
                    }else{
                        dlgInstituteProposalLogDetailsController.dispose();
                    }
                }else{
                      dlgInstituteProposalLogDetailsController.dispose();
                }
            }else if(source.equals(instituteProposalLogDetailsForm.btnPrint)){
                if(getFunctionType()=='M' ||getFunctionType()=='A'  ){
                    ipLogBeanFromServer.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                    boolean flag = isDatachanged();
                    if(flag == true){
                        validation = false;
                        performUpdate();
                         if(yesClicked == true){
                             myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                             observable.notifyObservers(myIPlogbean);
                         }
                    }
                }
                performPrintAction();
                dlgInstituteProposalLogDetailsController.dispose();
            }else if(source.equals(instituteProposalLogDetailsForm.btnPINameSearch)){
                performFindPerson();
            }else if(source.equals(instituteProposalLogDetailsForm.btnLeadUnitSearch)){
                performFindUnit();
            }else if(source.equals(instituteProposalLogDetailsForm.btnSponsorSearch)){
                performSearchOperation();            
            }else if(source.equals(instituteProposalLogDetailsForm.txtPIName)){
               checkPersonName();
            }else if(source.equals(instituteProposalLogDetailsForm.txtSponsorID)){
                checkSponsor();
            }else if(source.equals(instituteProposalLogDetailsForm.txtLeadUnit)){
                UnitDetailFormBean unitInfoBean;
                String leadUnit = instituteProposalLogDetailsForm.txtLeadUnit.getText().trim();
                unitInfoBean = getLeadUnit(leadUnit);
                if(unitInfoBean != null){
                    leadUnit = unitInfoBean.getUnitName();
                    instituteProposalLogDetailsForm.lblLeadUnitName.setText(leadUnit);
                } 
            }else if(source.equals(instituteProposalLogDetailsForm.chkEmployee)){
                boolean MitPerson = instituteProposalLogDetailsForm.chkEmployee.isSelected();
                if(MitPerson == false){
                    instituteProposalLogDetailsForm.txtPIName.setText(EMPTY);
                    instituteProposalLogDetailsForm.txtPIName.setEnabled(false);
                    instituteProposalLogDetailsForm.txtPIName.setEditable(false);
                }else{
                    instituteProposalLogDetailsForm.txtPIName.setText(EMPTY);
                    instituteProposalLogDetailsForm.txtPIName.setEnabled(true);
                    instituteProposalLogDetailsForm.txtPIName.setEditable(true);
                }
            }
        }catch(CoeusUIException coeusUIException){
            CoeusOptionPane.showDialog(coeusUIException);
            coeusUIException.printStackTrace();
        }catch(CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            coeusException.printStackTrace();
        }
    }
    
    /**
     * For Adding A New Log
     */    
    public void performAddLog()throws CoeusUIException{
        //get  data for combo box 
        Hashtable htData = getNewProposalData();       
        CoeusVector cvProposalType = null;
        CoeusVector cvProposalStatus = null;
        CoeusVector cvTempProposalStatus = null;

        cvProposalType = (CoeusVector)htData.get(KeyConstants.PROPOSAL_TYPE);
        cvProposalStatus = (CoeusVector)htData.get(KeyConstants.INSTITUTE_PROPOSAL_STATUS);
        cvpstatus = cvTempProposalStatus  = cvProposalStatus ;
        cvPType = cvProposalType;
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
        cvProposalType.add(0, emptyBean);
        instituteProposalLogDetailsForm.cmbStatus.setModel(new DefaultComboBoxModel(cvProposalStatus));
        instituteProposalLogDetailsForm.cmbProposalType.setModel(new DefaultComboBoxModel(cvProposalType));
        
        //set default log status to pending 
        myIPlogbean.setLogStatus('P');
        ComboBoxBean comboBean = new ComboBoxBean();
        Equals eqStatus = new Equals(CODE,EMPTY_STRING + myIPlogbean.getLogStatus());
        cvProposalStatus = cvProposalStatus.filter(eqStatus);
        comboBean = (ComboBoxBean)cvProposalStatus.elementAt(0);
        instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(comboBean);
        instituteProposalLogDetailsForm.cmbStatus.setEnabled(false);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
        instituteProposalLogDetailsForm.lblLogMerged.setVisible(false);
        instituteProposalLogDetailsForm.txtLogMerged.setVisible(false);
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
   }
    /**
     *  Code to go for printing
     */
    private String performPrintAction() throws CoeusException{
         String printServletURL;// = CoeusGuiConstants.CONNECTION_URL+"/printServlet";

         Hashtable htPrintParams = new Hashtable();
         htPrintParams.put("PROPOSAL_NUMBER",this.myIPlogbean.getProposalNumber());
         RequesterBean requester = new RequesterBean();
         requester.setFunctionType(PRINT_PROPOSAL_LOG);
         requester.setDataObject(htPrintParams);
         
         //For Streaming
         printServletURL = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
         requester.setId("ProposalLog/ProposalLog");
         requester.setFunctionType('R');
         //For Streaming
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(printServletURL, requester);
         
         comm.send();
         ResponderBean responder = comm.getResponse();
         String fileName = "";
         if(responder.isSuccessfulResponse()){
             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
             
             //             System.out.println("Got the Clob Data");
             fileName = (String)responder.getDataObject();
             //System.out.println("Report Filename is=>"+fileName);
             
             /*fileName.replace('\\', '/') ; // this is fix for Mac
             URL reportUrl = null;
             try{
                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
             
             
             if (coeusContxt != null) {
                 coeusContxt.showDocument( reportUrl, "_blank" );
             }else {
                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
                 bs.showDocument( reportUrl );
             }
             }catch(MalformedURLException muEx){
                 throw new CoeusException(muEx.getMessage());
             }catch(Exception uaEx){
                 throw new CoeusException(uaEx.getMessage());
             }*/
             try{
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch (MalformedURLException malformedURLException) {
                 throw new CoeusException(malformedURLException.getMessage());
             }
             
         }else{
             throw new CoeusException(responder.getMessage());
         }
         return fileName;
    }
    /*
    private void print() throws CoeusException {
        RequesterBean requester = new RequesterBean();
        requester.setId(report.getId());
        setSltdReportId(report.getId());
        requester.setDataObject(getPrintData());
        requester.setFunctionType('R');
        appSerComm.setRequest(requester);
        appSerComm.send();
        ResponderBean res = appSerComm.getResponse();
        String pdf = (String)res.getDataObject();
        try{
            URL url = new URL(pdf);
            URLOpener.openUrl(url);
        }catch (MalformedURLException malformedURLException) {
            throw new CoeusException(malformedURLException.getMessage());
        }
    }*/
    
    /**
     * For Adding A Temp Log
     */    
    public void performTempLog() throws CoeusUIException{
        Hashtable htData = getNewProposalData();       
        CoeusVector cvProposalType = null;
        CoeusVector cvProposalStatus = null;
        CoeusVector cvTempProposalStatus = null;
        cvProposalType = (CoeusVector)htData.get(KeyConstants.PROPOSAL_TYPE);
        cvProposalStatus = (CoeusVector)htData.get(KeyConstants.INSTITUTE_PROPOSAL_STATUS);
        cvpstatus = cvTempProposalStatus  = cvProposalStatus ;
        cvPType = cvProposalType;
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
        cvProposalType.add(0, emptyBean);
        instituteProposalLogDetailsForm.cmbStatus.setModel(new DefaultComboBoxModel(cvProposalStatus));
        instituteProposalLogDetailsForm.cmbProposalType.setModel(new DefaultComboBoxModel(cvProposalType));
        
        
        //set default status to temporary
        myIPlogbean.setLogStatus('T');
        ComboBoxBean tempComboBean = new ComboBoxBean();
        Equals eqTempStatus = new Equals(CODE,EMPTY_STRING + myIPlogbean.getLogStatus());
        cvProposalStatus = cvProposalStatus.filter(eqTempStatus);
        tempComboBean = (ComboBoxBean)cvProposalStatus.elementAt(0);
        instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(tempComboBean);
        instituteProposalLogDetailsForm.cmbStatus.setEnabled(false);
    }
    
    /**
     * For performing Modifying Log
     */    
    public void performModifyOperation()throws CoeusUIException{
        Hashtable htModifyData = getModifyData();
        CoeusVector cvProposalType = new CoeusVector();
        CoeusVector cvFilterStatus = new CoeusVector();
        CoeusVector cvIPLogBean = new CoeusVector();
        cvProposalType = (CoeusVector)htModifyData.get(KeyConstants.PROPOSAL_TYPE);
        cvStatus = (CoeusVector)htModifyData.get(KeyConstants.INSTITUTE_PROPOSAL_STATUS);
        cvIPLogBean = (CoeusVector)htModifyData.get(InstituteProposalLogBean.class); 
         
        ComboBoxBean emptyBean = new ComboBoxBean(EMPTY_STRING, EMPTY_STRING);
        cvProposalType.add(0, emptyBean);
        instituteProposalLogDetailsForm.cmbStatus.setModel(new DefaultComboBoxModel(cvStatus));
        instituteProposalLogDetailsForm.cmbProposalType.setModel(new DefaultComboBoxModel(cvProposalType));

        ipLogBeanFromServer = (InstituteProposalLogBean)cvIPLogBean.elementAt(0);
        
        ComboBoxBean comboBean = new ComboBoxBean();
        Equals eqStatus = new Equals(CODE,EMPTY_STRING + ipLogBeanFromServer.getLogStatus());
        cvFilterStatus = cvStatus.filter(eqStatus);
        comboBean = (ComboBoxBean)cvFilterStatus.elementAt(0);
        instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(comboBean);
        String code = comboBean.getCode();
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
        if(getFunctionType() != 'A'){
            if(ipLogBeanFromServer.getProposalNumber().trim().startsWith(TEMPORARY_PROPOSAL_LOG_STATUS) || 
                    ipLogBeanFromServer.getProposalNumber().trim().startsWith(DISCLOSURE_PROPOSAL_LOG_STATUS)){
                instituteProposalLogDetailsForm.txtLogMerged.setEditable(false);
                instituteProposalLogDetailsForm.txtLogMerged.setEnabled(false);
            }else{
                instituteProposalLogDetailsForm.lblLogMerged.setVisible(false);
                instituteProposalLogDetailsForm.txtLogMerged.setVisible(false);
            }
        }
        // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
        if(code.equals("M")){
                instituteProposalLogDetailsForm.cmbStatus.setEnabled(false);
                // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
                if(ipLogBeanFromServer.getMergedData() != null && !ipLogBeanFromServer.getMergedData().isEmpty()){
                    instituteProposalLogDetailsForm.txtLogMerged.setText(ipLogBeanFromServer.getMergedData().get(0).toString());
                }else{
                    instituteProposalLogDetailsForm.txtLogMerged.setText(CoeusGuiConstants.EMPTY_STRING);
                }
                // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
            }
        ComboBoxBean comboTypeBean = new ComboBoxBean();
        Equals eqTypeStatus = new Equals(CODE,EMPTY_STRING + ipLogBeanFromServer.getProposalTypeCode());
        cvProposalType = cvProposalType.filter(eqTypeStatus);
        comboTypeBean = (ComboBoxBean)cvProposalType.elementAt(0);
        instituteProposalLogDetailsForm.cmbProposalType.setSelectedItem(comboTypeBean);

        instituteProposalLogDetailsForm.txtProposalNumber.setText(proposalNumber);
        instituteProposalLogDetailsForm.txtArTitle.setText(ipLogBeanFromServer.getTitle());
        instituteProposalLogDetailsForm.txtArTitle.setCaretPosition(0);
        instituteProposalLogDetailsForm.txtPIName.setText(ipLogBeanFromServer.getPrincipleInvestigatorName());
        instituteProposalLogDetailsForm.txtLeadUnit.setText(ipLogBeanFromServer.getLeadUnit());
        leadUnit = ipLogBeanFromServer.getLeadUnit();
        /** Bug Fix #2065
         *start
         */
        if(ipLogBeanFromServer.getSponsorCode()!= null){
            instituteProposalLogDetailsForm.txtSponsorID.setText(ipLogBeanFromServer.getSponsorCode().trim());
        }else{
            instituteProposalLogDetailsForm.txtSponsorID.setText(EMPTY_STRING);
        }
        /** Bug Fix #2065
         *End
         */

        if(ipLogBeanFromServer.getSponsorName() != null && !ipLogBeanFromServer.getSponsorName().toString().equals(EMPTY)){
            instituteProposalLogDetailsForm.txtSponsorName.setEditable(false);
            instituteProposalLogDetailsForm.txtSponsorName.setEnabled(false);
            instituteProposalLogDetailsForm.txtSponsorName.setText(ipLogBeanFromServer.getSponsorName());
        }
        instituteProposalLogDetailsForm.lblLeadUnitName.setText(ipLogBeanFromServer.getUnitName());
        instituteProposalLogDetailsForm.txtArComments.setText(ipLogBeanFromServer.getComments());
        instituteProposalLogDetailsForm.txtArComments.setCaretPosition(0);
        nonMITPerson = ipLogBeanFromServer.isNonMITPersonFlag();  
        
        instituteProposalLogDetailsForm.chkEmployee.setSelected(!nonMITPerson);
        if(nonMITPerson){
            instituteProposalLogDetailsForm.txtPIName.setEditable(false);
            instituteProposalLogDetailsForm.txtPIName.setEnabled(false);
        }
      
        personId = ipLogBeanFromServer.getPrincipleInvestigatorId();
        
        //case 3263 start
        if(ipLogBeanFromServer.getDeadlineDate()!= null){
            instituteProposalLogDetailsForm.txtDeadlineDate.setText(
            dateUtils.formatDate(ipLogBeanFromServer.getDeadlineDate().toString(), DATE_FORMAT_DISPLAY));
        }else{
            instituteProposalLogDetailsForm.txtDeadlineDate.setText(null);
        }
        //case 3436 start
        simpleDateFormat.applyPattern(LAST_UPDATE_FORMAT);
        if(ipLogBeanFromServer.getCreateTimestamp()!= null){
            instituteProposalLogDetailsForm.txtCreateDate.setText(simpleDateFormat.format(ipLogBeanFromServer.getCreateTimestamp()));
        }
//        if(ipLogBeanFromServer.getCreateTimestamp()!= null){
//            instituteProposalLogDetailsForm.txtCreateDate.setText(
//            dateUtils.formatDate(ipLogBeanFromServer.getCreateTimestamp().toString(), DATE_FORMAT_DISPLAY));
//        }
//        simpleDateFormat.applyPattern(LAST_UPDATE_FORMAT); 
        //case 3436 end
        if(ipLogBeanFromServer.getUpdateTimestamp() != null) {
            instituteProposalLogDetailsForm.txtLastUpdate.setText(simpleDateFormat.format(ipLogBeanFromServer.getUpdateTimestamp()));
        }
       simpleDateFormat.applyPattern(SIMPLE_DATE_FORMAT);
        //case 3263 end
        try{
            myIPlogbean = (InstituteProposalLogBean)ObjectCloner.deepCopy(ipLogBeanFromServer);
        }catch (Exception e){
            e.printStackTrace();
        }  

        if(getFunctionType() == 'D'){
             instituteProposalLogDetailsForm.cmbProposalType.setEnabled(false);
             instituteProposalLogDetailsForm.cmbStatus.setEnabled(false);
             instituteProposalLogDetailsForm.txtArTitle.setEnabled(false);
             instituteProposalLogDetailsForm.txtPIName.setEnabled(false);
             instituteProposalLogDetailsForm.txtLeadUnit.setEnabled(false);
             instituteProposalLogDetailsForm.txtSponsorID.setEnabled(false);
             instituteProposalLogDetailsForm.txtSponsorName.setEnabled(false);
             instituteProposalLogDetailsForm.txtArComments.setEnabled(false);
             instituteProposalLogDetailsForm.chkEmployee.setEnabled(false);
             instituteProposalLogDetailsForm.btnPINameSearch.setEnabled(false);
             instituteProposalLogDetailsForm.btnLeadUnitSearch.setEnabled(false);
             instituteProposalLogDetailsForm.btnSponsorSearch.setEnabled(false);
             
             instituteProposalLogDetailsForm.cmbProposalType.setEditable(false);
             instituteProposalLogDetailsForm.cmbStatus.setEditable(false);
             instituteProposalLogDetailsForm.txtPIName.setEditable(false);
             instituteProposalLogDetailsForm.txtLeadUnit.setEditable(false);
             instituteProposalLogDetailsForm.txtSponsorID.setEditable(false);
             instituteProposalLogDetailsForm.txtSponsorName.setEditable(false);
             instituteProposalLogDetailsForm.txtArComments.setEditable(false);
             
             instituteProposalLogDetailsForm.txtArTitle.setEditable(false);
             instituteProposalLogDetailsForm.txtArTitle.setOpaque(false);
             
             instituteProposalLogDetailsForm.txtArComments.setEditable(false);
             instituteProposalLogDetailsForm.txtArComments.setOpaque(false);
             
             //case 3263 start
             instituteProposalLogDetailsForm.txtDeadlineDate.setEditable(false);
             //case 3263 end
             }
     }
    
    /**
     * Logs the New or Temp proposal
     */    
    public void performLogOperation() throws CoeusUIException,CoeusException{
         // To insert the bean and send to the server.
         RequesterBean requesterBean = new RequesterBean();
        
         // To get the return values for the insti proposal
         ResponderBean responderBean = new ResponderBean();
       
            if(validate()){
                if(getFunctionType() == 'A'){                                   //if new  mode 
                    requesterBean.setFunctionType(CHECK_FOR_TEMP_PROPOSAL);
                    String propData = instituteProposalLogDetailsForm.txtProposalNumber.getText().trim();
                    if(propData.equals(EMPTY_STRING)){
                        setDataToBean(ipLogBeanFromServer);
                        myIPlogbean.setAcType("I");
                    }else{
                        myIPlogbean.setAcType("U");
                    }
                }else if(getFunctionType() == 'M'){                             //if modify mode 
                    validateFlag = true ;
                    requesterBean.setFunctionType(CHECK_FOR_TEMP_PROPOSAL);
                    myIPlogbean.setAcType("U");
                    setDataToBean (ipLogBeanFromServer); 
                }else if(getFunctionType() == 'T'){                             //if temp mode set log type along with ac type 
                    requesterBean.setFunctionType (CHECK_FOR_TEMP_PROPOSAL);
                    myIPlogbean.setAcType("I");
                    myIPlogbean.setLogType('T');
                    setDataToBean (ipLogBeanFromServer);
                }
                
                requesterBean.setDataObject(myIPlogbean);   
                AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
                comm.setRequest(requesterBean);
                comm.send();
                responderBean = comm.getResponse();

                if(responderBean.isSuccessfulResponse()) {
                    
                    /*Returns a coeus vector which contains a boolean as First element 
                     *and IPlogbean as the Second, if the flag is true then temp logs 
                     *for the person is presen and hence the IPlogbean contains the temp logs.Else 
                     *IPlogbean contains newly generated prop no along with other data
                     */
                    
                    boolean tempLog = false;            
                    CoeusVector cvCheckTempLog = (CoeusVector) responderBean.getDataObject();
                    tempLog  = ((Boolean)cvCheckTempLog.elementAt(0)).booleanValue();

                    if(tempLog == true){
                        // Display the dialog temp log with the data
                        CoeusVector cvTempLogs = (CoeusVector)cvCheckTempLog.elementAt(1);
                        propDescription = instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim();
                        myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                        TemporaryLogListController temporaryLogListController = new TemporaryLogListController(cvTempLogs,myIPlogbean);
                        temporaryLogListController.display();
                        ipLogBeanFromServer = temporaryLogListController.getIpLogBean();
                       
                        //For updating to the screen ater generating new log 
                        performRefersh(ipLogBeanFromServer);
                        try{
                            myIPlogbean = (InstituteProposalLogBean)ObjectCloner.deepCopy(ipLogBeanFromServer);
                        }catch (Exception e){
                            throw new CoeusException(e.getMessage());
                        }
                    }else{
                            ipLogBeanFromServer = (InstituteProposalLogBean)cvCheckTempLog.elementAt(1);
                            if(getFunctionType() != 'D'){
                                if(getFunctionType() == 'M'){
                                            myIPlogbean = ipLogBeanFromServer;
                                }else{
                                    performRefersh(ipLogBeanFromServer);
                                }
                                try{
                                    myIPlogbean = (InstituteProposalLogBean)ObjectCloner.deepCopy(ipLogBeanFromServer);
                                }catch (Exception e){
                                    e.printStackTrace();
                                    throw new CoeusException(e.getMessage());
                                }
                            }
                        }
                    }else{
                        throw new CoeusUIException(responderBean.getMessage(), CoeusUIException.ERROR_MESSAGE);
                    }
                }
    }
    
   
    /**
     * If the data is changed after generating porpno(LOG PROPOSAL).Before closing(CLOSE) call this method
     */    
    public void performUpdate() throws CoeusUIException{
            inside = false;
            validation = false;
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CONFIRM),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                String propNumber = ipLogBeanFromServer.getProposalNumber();
                if(propNumber == null || propNumber.trim().equals(EMPTY_STRING)){
                   CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(GENERATE_NUMBER));
                   return;
                }else{
                    try{
                        if(validate()){
                            myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                            myIPlogbean.setProposalNumber(instituteProposalLogDetailsForm.txtProposalNumber.getText().trim());
                            ComboBoxBean comboProposalTypeBean  = new ComboBoxBean();
                            comboProposalTypeBean = (ComboBoxBean)instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem();
                            if(! comboProposalTypeBean.getCode().equals(EMPTY_STRING)) {
                                int code = Integer.parseInt(comboProposalTypeBean.getCode().trim());
                                myIPlogbean.setProposalTypeCode(code);
                               }

                            ComboBoxBean comboStatusBean  = new ComboBoxBean();
                            comboStatusBean = (ComboBoxBean)instituteProposalLogDetailsForm.cmbStatus.getSelectedItem();
                            if(! comboStatusBean.getCode().equals(EMPTY_STRING)) {
                                String status = comboStatusBean.getCode().trim();
                                myIPlogbean.setLogStatus(status.charAt(0));
                               }
                            myIPlogbean.setTitle(instituteProposalLogDetailsForm.txtArTitle.getText().trim());
                            myIPlogbean.setPrincipleInvestigatorName(instituteProposalLogDetailsForm.txtPIName.getText().trim());
                            myIPlogbean.setNonMITPersonFlag(!instituteProposalLogDetailsForm.chkEmployee.isSelected());
                            myIPlogbean.setLeadUnit(instituteProposalLogDetailsForm.txtLeadUnit.getText().trim());
                            myIPlogbean.setUnitName(instituteProposalLogDetailsForm.lblLeadUnitName.getText().trim());
                            /*modified for the Big Fix:1666 step:3 start*/
                            myIPlogbean.setSponsorCode(getValidSponsorCode(instituteProposalLogDetailsForm.txtSponsorID.getText().trim()));
                            /*step:3 end*/
                            myIPlogbean.setSponsorName(instituteProposalLogDetailsForm.txtSponsorName.getText().trim());
                            myIPlogbean.setComments(instituteProposalLogDetailsForm.txtArComments.getText().trim());
                            //case 3263 start
                            Date date;
                            String value = instituteProposalLogDetailsForm.txtDeadlineDate.getText().trim();
                            if (!value.equals(EMPTY)){
                                String strDate1 =  dateUtils.formatDate(value, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                                if (strDate1 == null){
                                    strDate1 =dateUtils.restoreDate(value, DATE_SEPARATERS);
                                    if(strDate1== null || strDate1.equals(value)){
                                       myIPlogbean.setDeadlineDate(null);
                                    }else{
                                        date = simpleDateFormat.parse(dateUtils.restoreDate(value,DATE_SEPARATERS));
                                        myIPlogbean.setDeadlineDate(new java.sql.Date(date.getTime()));
                                    }
                                }else{
                                    date = simpleDateFormat.parse(dateUtils.formatDate(value,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                                    myIPlogbean.setDeadlineDate(new java.sql.Date(date.getTime()));
                                }
                            }else{
                                    myIPlogbean.setDeadlineDate(null);            
                            }
//                            myIPlogbean.setDeadlineDate(instituteProposalLogDetailsForm.txtDeadlineDate.getText());
                            //case 3263 end
                            performLogOperation();
                            yesClicked = true;
                            validation = true;
                            inside = true;
                            dlgInstituteProposalLogDetailsController.dispose();
                        }
                    }catch (Exception e){
                        e.printStackTrace();                        
                        //Commented/Modified for case#3334 - NPE in CoeusClientException.setMessageKey(null)
                        //throw new CoeusUIException(e.getMessage());
                        if(e.getMessage() != null){
                            throw new CoeusUIException(e.getMessage());
                        }
                   }
                }
            }if(option == CoeusOptionPane.SELECTION_NO){
                if(getFunctionType() != 'M'){
                    dlgInstituteProposalLogDetailsController.dispose();
                    if(ipLogBeanFromServer.getUpdateTimestamp () != null){
                        observable.notifyObservers(ipLogBeanFromServer);
                    }
                }else {
                    yesClicked   = false;
                    dlgInstituteProposalLogDetailsController.dispose();
                }
            }
    }
    
    /** added for the Bug Fix:1666 for alpha numeric sponsor code step:4 start
     * contacts the server and fetches the valid Sponsor code for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor code
     * @param sponsorCode sponsor code for which valid sponsor code has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */
    private  String getValidSponsorCode(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_VALID_SPONSOR_CODE);
        requesterBean.setDataObject(sponsorCode);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusException(COULD_NOT_CONTACT_SERVER);
        }else if(!responderBean.isSuccessfulResponse()) {
            throw new CoeusException(SERVER_ERROR);
        }
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
        if(responderBean.getDataObject() == null) return EMPTY_STRING;
        String validSponsorCode = responderBean.getDataObject().toString().trim();
        return validSponsorCode;
    }/*Bug Fix:1666 step:4 end*/
    
    /**
     * Supporting method used for sponsearch
     */    
     public void performSearchOperation(){
         try{
             dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
             int value = sponsorSearch();
             if (value  == 1){
                    String sprname = getSponsorName();
                    String sprcode = getSponsorCode();
                    instituteProposalLogDetailsForm.txtSponsorName.setText(sprname);
                    instituteProposalLogDetailsForm.txtSponsorID.setText(sprcode);
                    instituteProposalLogDetailsForm.txtSponsorName.setEditable(false);
                    instituteProposalLogDetailsForm.txtSponsorName.setEnabled(false);
                    dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
             dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }catch (Exception e){
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                e.printStackTrace();
            }
       }
     
     
        /* supporting method used for person search */
        private void performFindPerson(){
            try{
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                boolean status = instituteProposalLogDetailsForm.chkEmployee.isSelected();
                if(status){
                    CoeusSearch proposalSearch = null;
                    proposalSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "PERSONSEARCH",
                    CoeusSearch.TWO_TABS ); 
                    proposalSearch.showSearchWindow();
                    if(proposalSearch.getSelectedRow() == null) {
                        dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        return ;
                    }
                    personId = proposalSearch.getSelectedRow().get("PERSON_ID").toString().trim();
                    instituteProposalLogDetailsForm.txtPIName.setText(proposalSearch.getSelectedRow().get("FULL_NAME").toString().trim());
                    
                    oldPersonInfo = proposalSearch.getSelectedRow().get("FULL_NAME").toString().trim();
                    
                    String leadUnit = null;
                    
                    if(proposalSearch.getSelectedRow().get("HOME_UNIT") != null){
                       leadUnit = proposalSearch.getSelectedRow().get("HOME_UNIT").toString().trim();
                    }
                    
                    if(leadUnit != null && !(leadUnit.equals(EMPTY))) {
                        UnitDetailFormBean  udfbean = new UnitDetailFormBean();
                        udfbean = getLeadUnit(leadUnit);
                        String unitName = udfbean.getUnitName().trim();
                        instituteProposalLogDetailsForm.txtLeadUnit.setText(leadUnit);
                        instituteProposalLogDetailsForm.lblLeadUnitName.setText(unitName);
                    }
                     dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }else{
                    CoeusSearch coeusSearch = new CoeusSearch(mdiForm, CoeusGuiConstants.ROLODEX_SEARCH, 1);
                    coeusSearch.showSearchWindow();
                    HashMap rolodexInfo = coeusSearch.getSelectedRow();
                    String name = null;
                    String firstName = null;
                    String lastName = null;
                    String middleName = null;
                    String namePreffix = null;
                    String nameSuffix = null;

                    if(rolodexInfo!=null){
                     personId = Utils.convertNull(rolodexInfo.get( "ROLODEX_ID" ));
                     firstName = Utils.convertNull(rolodexInfo.get( "FIRST_NAME" ));
                     lastName = Utils.convertNull(rolodexInfo.get( "LAST_NAME" ));
                     middleName = Utils.convertNull(rolodexInfo.get( "MIDDLE_NAME" ));
                     namePreffix = Utils.convertNull(rolodexInfo.get( "PREFIX" ));
                     nameSuffix = Utils.convertNull(rolodexInfo.get( "SUFFIX" ));

                     /* construct full name of the rolodex if his last name is present
                      *otherwise use his organization name to display in person name
                      *column of investigator table
                      */
                        if ( lastName.length() > 0) {
                            name = ( lastName + " "+nameSuffix +", "+ namePreffix
                            +" "+firstName +" "+ middleName ).trim();
                        } else {
                            name = Utils.convertNull(rolodexInfo.get("ORGANIZATION"));//result.get("ORGANIZATION"));
                        }  
                     instituteProposalLogDetailsForm.txtPIName.setText(name);
                     oldPersonInfo = name;
                    }
                    dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }catch( Exception err ){
                err.printStackTrace();
            }
        }
        
      
        /**
         * For displaying Person data on double click
         */        
     class InvestigatorDetailsAdapter extends MouseAdapter {
         /**
          * Event Listener For Mouse Clicked
          */         
        public void mouseClicked(MouseEvent me){
            if( me.getClickCount() == 2 ) {
                if( personId != null) {
                   boolean nonMITPerson = instituteProposalLogDetailsForm.chkEmployee.isSelected(); 
                   
                   instituteProposalLogDetailsForm.txtPIName.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                   dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                   
                   if( personId != null && personId.trim().length() > 0 ) {
                        if(nonMITPerson == false){
                            String rolodex = instituteProposalLogDetailsForm.txtPIName.getText().trim();
                            if(rolodex.equals(EMPTY)){
                   
                                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                instituteProposalLogDetailsForm.txtPIName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                
                                return;
                            }
                            
                            /* selected investigator is a rolodex, so show
                               rolodex details */
                            
                            RolodexMaintenanceDetailForm frmRolodex
                            = new RolodexMaintenanceDetailForm('V',personId);
                            frmRolodex.showForm(CoeusGuiConstants.getMDIForm(),
                            CoeusGuiConstants.TITLE_ROLODEX,true);
                        }else{
                             //Bug Fix: Pass the person id to get the person details Start 1
                             //if (validatePersonName()){//Bug Fix: Pass the person id to get the person details End 1
                                 String personName = instituteProposalLogDetailsForm.txtPIName.getText();
                                 String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                                 if(personName.equals(EMPTY)) {
                                
                                     dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                     instituteProposalLogDetailsForm.txtPIName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                     
                                     return;
                                 }
                                 try{
                                     //Bug Fix: Pass the person id to get the person details Start 2
                                     /*Bug fix:to get the person details with the person id instead of the person name*/
                                     //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                                     //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,'D');
                                     
                                     new PersonDetailForm(personId,loginUserName,'D');
                                     //Bug Fix: Pass the person id to get the person details End 2
                                     
                                 }catch ( Exception e) {
                                     CoeusOptionPane.showInfoDialog( e.getMessage() );
                                 }
                             //}
                             //Bug Fix: Pass the person id to get the person details End 1
                        }
                   }
                   instituteProposalLogDetailsForm.txtPIName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                   dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                   
                }
            }
        }
    }
     
     
     /**
      * For displaying Lead Unit data on double click
      */     
     class UnitDisplayAdapter extends MouseAdapter {
         /**
          * Event Listener For Mouse Clicked
          */         
        public void mouseClicked( MouseEvent me ) {
            if (me.getClickCount() == 2) {
                String unitNumber = instituteProposalLogDetailsForm.txtLeadUnit.getText();
                if( unitNumber != null ) {
                    if( unitNumber != null && unitNumber.trim().length() > 0 ) {
                        try{
                                instituteProposalLogDetailsForm.txtLeadUnit.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                                UnitDetailForm frmUnit = new UnitDetailForm(unitNumber,'G');
                                frmUnit.showUnitForm(CoeusGuiConstants.getMDIForm());
                            } catch(Exception ex){
                                CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(
                                UNIT_DISPLAY_ERR));
                            }
                            instituteProposalLogDetailsForm.txtLeadUnit.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
                            dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        }
                    }
                 }
              }
           }
     
     
      
     /**
      * For displaying Sponsor data on double click
      */     
     class SponsorDisplayAdapter extends MouseAdapter {
         /**
          * Event Listener For Mouse Clicked
          */         
         public void mouseClicked(MouseEvent me) {
             if (me.getClickCount() == 2) {
                String sponsorCode = instituteProposalLogDetailsForm.txtSponsorID.getText().trim();
                if( sponsorCode != null ) {
                    if( sponsorCode != null && sponsorCode.trim().length() > 0 ) {
                        try{
                              instituteProposalLogDetailsForm.txtSponsorID.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                              dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                              SponsorMaintenanceForm frmSponsor = new SponsorMaintenanceForm('D', sponsorCode);
                              frmSponsor.showForm(mdiForm, "Display Sponsor" , true);
                        } catch(Exception ex){
                            CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            SPONSOR_DISPLAY_ERR));
                        }
                        instituteProposalLogDetailsForm.txtSponsorID.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                        dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        }
     }
          
     /** displays sponsor search.
     * returns OK_CLICKED if OK button was Clicked.
     * else returns CANCEL_CLICKED if Cancel button was clicked.
     * @throws Exception if any error occurs.
     * @return OK_CLICKED if OK button was Clicked.
     * else returns CANCEL_CLICKED if Cancel button was clicked.
     */    
    public int sponsorSearch()throws Exception {
        //Do Lazy initialization as every subclass of this need not search for Sponsor.
        if(sponsorSearch == null) {
            sponsorSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), SPONSOR_SEARCH, CoeusSearch.TWO_TABS);
        }
        
        sponsorSearch.showSearchWindow();
        HashMap selectedRow = sponsorSearch.getSelectedRow();
        if(selectedRow == null) {
            return CANCEL_CLICKED;
        }
        sponsorCode = selectedRow.get(SPONSOR_CODE).toString();
        sponsorName = selectedRow.get(SPONSOR_NAME).toString();
        return OK_CLICKED;
        
    }
    
    
    /*
     *  Supporting method to perform unit search 
    */    
    public void performFindUnit(){
         //Show search screen and get the selected unit to newUnitId text box.
        //open search window with two tabs
        try{
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                CoeusSearch coeusSearch = new CoeusSearch(dlgInstituteProposalLogDetailsController,
                "leadUnitSearch", CoeusSearch.TWO_TABS);
                coeusSearch.showSearchWindow();

                if(coeusSearch.getSelectedRow() == null){
                    dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return ;
                }
                instituteProposalLogDetailsForm.txtLeadUnit.setText(coeusSearch.getSelectedRow().get("UNIT_NUMBER").toString().trim());
                instituteProposalLogDetailsForm.lblLeadUnitName.setText(coeusSearch.getSelectedRow().get("UNIT_NAME").toString());
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }catch (Exception exception) {
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                CoeusOptionPane.showInfoDialog(exception.getMessage());
            }
    }
    
    /** returns searched sponsor code
     * @return searched sponsor code
     */    
    public String getSponsorCode() {
        return sponsorCode;
    }
    
    /** returns searched sponsor code
     * @return returns searched sponsor code
     */    
    public String getSponsorName() {
        return sponsorName;
    }
     
    
    /**
     * Adapter class for focus lost ,focus gain
     */    
    public class CustomFocusAdapter extends FocusAdapter{
        /**
         * Method called upon Focus Gained
         */        
        public void focusGained(FocusEvent focusEvent) {            
            //case 3263 start
//            String str = instituteProposalLogDetailsForm.txtPIName.getText();
//            if(str != null){
//                lastPerson = str.trim();
//            }
            if(focusEvent.isTemporary()) return ;
            
            Object source = focusEvent.getSource();
            if(source.equals(instituteProposalLogDetailsForm.txtDeadlineDate)) {
                String deadlineDate;
                deadlineDate = instituteProposalLogDetailsForm.txtDeadlineDate.getText();
                deadlineDate  = dateUtils.restoreDate(deadlineDate , DATE_SEPARATERS);
                instituteProposalLogDetailsForm.txtDeadlineDate.setText(deadlineDate );
            }else if (source.equals(instituteProposalLogDetailsForm.txtPIName)){
                String str = instituteProposalLogDetailsForm.txtPIName.getText();
                if(str != null){
                    lastPerson = str.trim();
                }
            }
            //case 3263 end            
            return ;
        }
        /**
         * Method called upon Focus Lost
         */        
        public void focusLost (FocusEvent focusEvent){
            if(focusEvent.isTemporary()){
                return ;
            }
            Object source = focusEvent.getSource();
            
             if(source.equals(instituteProposalLogDetailsForm.txtPIName)){
                checkPersonName();
             }else if(source.equals(instituteProposalLogDetailsForm.txtLeadUnit)){
                UnitDetailFormBean unitInfoBean;
                String leadUnit = instituteProposalLogDetailsForm.txtLeadUnit.getText().trim();
                unitInfoBean = getLeadUnit(leadUnit);
                if(unitInfoBean != null){
                    leadUnit = unitInfoBean.getUnitName();
                    instituteProposalLogDetailsForm.lblLeadUnitName.setText(leadUnit);
                } 
                leadUnit = instituteProposalLogDetailsForm.txtLeadUnit.getText();
                if (leadUnit.equals(EMPTY)) {                                     //((JTextBox)source)
                    instituteProposalLogDetailsForm.lblLeadUnitName.setText(EMPTY);
                }
            }else if(source.equals(instituteProposalLogDetailsForm.txtSponsorID)) {
                //checks if entered sponsor is valid 
                checkSponsor();
                String sponsorName = instituteProposalLogDetailsForm.txtSponsorID.getText().trim();
                if(sponsorName.equals(EMPTY)){
                    instituteProposalLogDetailsForm.txtSponsorID.setText(EMPTY_STRING);
                    instituteProposalLogDetailsForm.txtSponsorName.setText(EMPTY);
//                    instituteProposalLogDetailsForm.txtSponsorName.setEditable(true);
//                    instituteProposalLogDetailsForm.txtSponsorName.setEnabled(true);
                }else{
                    instituteProposalLogDetailsForm.txtSponsorName.setEditable(false);
                    instituteProposalLogDetailsForm.txtSponsorName.setEnabled(false);
                }
                //case 3263 start
            }else if(source.equals(instituteProposalLogDetailsForm.txtDeadlineDate)) {                
                String deadlineDate;
                deadlineDate = instituteProposalLogDetailsForm.txtDeadlineDate.getText().trim();
                if(!deadlineDate.equals(EMPTY_STRING)) {
                         String strDate1 = dateUtils.formatDate(deadlineDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                         if(strDate1 == null) {
                             strDate1 = dateUtils.restoreDate(deadlineDate, DATE_SEPARATERS);
                             if( strDate1 == null || strDate1.equals(deadlineDate)) {
                                 CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                                 instituteProposalLogDetailsForm.txtDeadlineDate.setText("");
                                 setRequestFocusInThread(instituteProposalLogDetailsForm.txtDeadlineDate);
//                                 return ;
                             }
                         }else {
                             deadlineDate = strDate1;
                             instituteProposalLogDetailsForm.txtDeadlineDate.setText(deadlineDate);                             
                             
                         }
                     }
            }
            //case 3263 end 
        }
    }

            
     /** contacts the server and fetches the Sponsor name for the sponsor code.
     * returns "" if sponsor code is invalid.
     * @return sponsor name
     * @param sponsorCode sponsor code for which sponsor name has to be retrieved.
     * @throws CoeusException if cannot contact server or if server error occurs.
     */    
    public String getSponsorName(String sponsorCode)throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_SPONSOR_NAME);
        requesterBean.setDataObject(sponsorCode);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusException("Could not contact server");
        }else if(!responderBean.isSuccessfulResponse()) {
            throw new CoeusException("Server Error");
        }
        
        //Got data from server. return sponsor name.
        //sponsor name = EMPTY if not found.
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
        
//        if(responderBean.getDataObject() == null){
//            return EMPTY;
//        }
//        String sponsorName = responderBean.getDataObject().toString();
        String sponsorName  = CoeusGuiConstants.EMPTY_STRING;
        if(responderBean.getDataObjects() != null){
            sponsorName = responderBean.getDataObjects().get(0).toString();
            if(responderBean.getDataObjects().get(1) != null){
                setSponsorStatus(responderBean.getDataObjects().get(1).toString());
            }else{
                setSponsorStatus(CoeusGuiConstants.EMPTY_STRING);
            }
            
        }
        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
        return sponsorName;
    }
    
    /**
     * Contacts the server and fetches the Lead Unit
     */    
    public UnitDetailFormBean getLeadUnit( String unitNumber ){
       
        boolean success=false;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType('G');
        requester.setId( unitNumber );
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/unitServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        UnitDetailFormBean unitInfoBean = null;
        if ( response!=null ){
            success=true;
            unitInfoBean = (UnitDetailFormBean) response.getDataObject();
        }
        
	String leadUnit = instituteProposalLogDetailsForm.txtLeadUnit.getText().trim();
         if( unitInfoBean == null || unitInfoBean.getUnitNumber() == null){
            unitInfoBean = null;
            if(!leadUnit.equals(EMPTY)){
                CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(
                UNIT_VALIDATION_ERR));
                instituteProposalLogDetailsForm.txtLeadUnit.setText("");
            }
        }
        return unitInfoBean;
    }
    
    
    /**
     * Sets data to bean for sending it to server for saving
     */    
    public void setDataToBean(InstituteProposalLogBean ipLogBean){        
        String value;
        try{
        myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
        
        value = instituteProposalLogDetailsForm.txtPIName.getText().trim();
        myIPlogbean.setPrincipleInvestigatorName(value.equals(EMPTY) ? null : value);
        if(!value.equals(EMPTY)){

            myIPlogbean.setPrincipleInvestigatorId(personId);
        }
        
        
        ComboBoxBean comboProposalTypeBean  = new ComboBoxBean();
        comboProposalTypeBean = (ComboBoxBean)instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem();
            if(!comboProposalTypeBean.getCode().equals(EMPTY_STRING)) {
                int code = Integer.parseInt(comboProposalTypeBean.getCode().trim());
                myIPlogbean.setProposalTypeCode(code);
               }

        ComboBoxBean comboStatusBean  = new ComboBoxBean();
        comboStatusBean = (ComboBoxBean)instituteProposalLogDetailsForm.cmbStatus.getSelectedItem();
            if(! comboStatusBean.getCode().equals(EMPTY_STRING)) {
                String status = comboStatusBean.getCode().trim();
                myIPlogbean.setLogStatus(status.charAt(0));
               }
        
        
        value = instituteProposalLogDetailsForm.txtArTitle.getText().trim();
        myIPlogbean.setTitle(value.equals(EMPTY) ? null : value);
        
        value = instituteProposalLogDetailsForm.txtLeadUnit.getText().trim();
        myIPlogbean.setLeadUnit(value.equals(EMPTY) ? null : value);
        
        value = instituteProposalLogDetailsForm.lblLeadUnitName.getText().trim();
        myIPlogbean.setUnitName(value.equals(EMPTY) ? null : value);
        
        boolean flag = instituteProposalLogDetailsForm.chkEmployee.isSelected();
        myIPlogbean.setNonMITPersonFlag(!flag);
        
        value = instituteProposalLogDetailsForm.txtArComments.getText().trim();
        myIPlogbean.setComments(value.equals(EMPTY) ? null : value);
        
        /*modified for the bug fix:1666 step:2 start*/
        value = instituteProposalLogDetailsForm.txtSponsorID.getText().trim();
        myIPlogbean.setSponsorCode(getValidSponsorCode(value.equals(EMPTY) ? null : value.trim()));
        /*step:2 end*/
        
        value = instituteProposalLogDetailsForm.txtSponsorName.getText().trim();
        myIPlogbean.setSponsorName(value.equals(EMPTY) ? null : value);
        
        //case 3263 start
        Date date;
        value = instituteProposalLogDetailsForm.txtDeadlineDate.getText().trim();
        if (!value.equals(EMPTY)){
            String strDate1 =  dateUtils.formatDate(value, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if (strDate1 == null){
                strDate1 =dateUtils.restoreDate(value, DATE_SEPARATERS);
                if(strDate1== null || strDate1.equals(value)){
                   myIPlogbean.setDeadlineDate(null);
                }else{
                    date = simpleDateFormat.parse(dateUtils.restoreDate(value,DATE_SEPARATERS));
                    myIPlogbean.setDeadlineDate(new java.sql.Date(date.getTime()));
                }
            }else{
                date = simpleDateFormat.parse(dateUtils.formatDate(value,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                myIPlogbean.setDeadlineDate(new java.sql.Date(date.getTime()));
            }
        }else{
                myIPlogbean.setDeadlineDate(null);            
       }       
       }catch (ParseException parseException){
            parseException.printStackTrace();
        //case 3263 end
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
    }
    
    /**
     * Refreshes the screen after getting back the data form server
     */    
    public void performRefersh(InstituteProposalLogBean myIPlogbean){
        String value; 
        CoeusVector cvProposalStatus = new CoeusVector();
        CoeusVector cvTypeStatus = new CoeusVector();
        cvProposalStatus  = cvpstatus;
        cvTypeStatus = cvPType ;
        instituteProposalLogDetailsForm.txtProposalNumber.setText(myIPlogbean.getProposalNumber().trim());
        instituteProposalLogDetailsForm.btnLogProposal.setEnabled(false);
        instituteProposalLogDetailsForm.btnPrint.setEnabled(true);
        
        /*for refreshing status combo box*/
        ComboBoxBean statusComboBean = new ComboBoxBean();
        Equals eqStatus = new Equals(CODE,EMPTY_STRING + myIPlogbean.getLogStatus());
        cvProposalStatus = cvProposalStatus.filter(eqStatus);
        statusComboBean = (ComboBoxBean)cvProposalStatus.elementAt(0);
        instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(statusComboBean);
        
        /*for refreshing proposal type combo box*/
        ComboBoxBean proposaltypeComboBean = new ComboBoxBean();
        Equals eqProposal = new Equals(CODE,EMPTY_STRING + myIPlogbean.getProposalTypeCode());
        cvTypeStatus = cvTypeStatus.filter(eqProposal);
        proposaltypeComboBean = (ComboBoxBean)cvTypeStatus.elementAt(0);
        instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(proposaltypeComboBean);
        
        instituteProposalLogDetailsForm.txtArTitle.setText(myIPlogbean.getTitle().trim());
        instituteProposalLogDetailsForm.txtArTitle.setCaretPosition(0);
        instituteProposalLogDetailsForm.txtPIName.setText(myIPlogbean.getPrincipleInvestigatorName().trim());
        instituteProposalLogDetailsForm.txtLeadUnit.setText(myIPlogbean.getLeadUnit().trim());
        instituteProposalLogDetailsForm.lblLeadUnitName.setText(myIPlogbean.getUnitName().trim());
        
        
        value  = myIPlogbean.getSponsorCode();
        instituteProposalLogDetailsForm.txtSponsorID.setText( value == null? EMPTY : value.trim() );
        
        value = myIPlogbean.getSponsorName();
        instituteProposalLogDetailsForm.txtSponsorName.setText( value == null? EMPTY : value.trim() );
        
        value = myIPlogbean.getComments();
        instituteProposalLogDetailsForm.txtArComments.setText( value == null? EMPTY : value.trim() );
        instituteProposalLogDetailsForm.txtArComments.setCaretPosition(0);
        
        //case 3263 start
        if(myIPlogbean.getDeadlineDate()!= null){
            instituteProposalLogDetailsForm.txtDeadlineDate.setText(
            dateUtils.formatDate(myIPlogbean.getDeadlineDate().toString(), DATE_FORMAT_DISPLAY));
        }else{
            instituteProposalLogDetailsForm.txtDeadlineDate.setText(null);
        }
        //case 3436 start
        simpleDateFormat.applyPattern(LAST_UPDATE_FORMAT);
        if(myIPlogbean.getCreateTimestamp()!= null){
            instituteProposalLogDetailsForm.txtCreateDate.setText(simpleDateFormat.format(myIPlogbean.getCreateTimestamp()));
        }
//        if(myIPlogbean.getCreateTimestamp()!= null){
//            instituteProposalLogDetailsForm.txtCreateDate.setText(
//            dateUtils.formatDate(myIPlogbean.getCreateTimestamp().toString(), DATE_FORMAT_DISPLAY));
//        }
//        simpleDateFormat.applyPattern(LAST_UPDATE_FORMAT); 
        //case 3436 end 
        if(myIPlogbean.getUpdateTimestamp() != null) {
            instituteProposalLogDetailsForm.txtLastUpdate.setText(simpleDateFormat.format(myIPlogbean.getUpdateTimestamp()));
        }
        simpleDateFormat.applyPattern(SIMPLE_DATE_FORMAT);
        //case 3263 end
    }
    
    //checks for data changes in the window 
    private boolean isDatachanged(){
        //for New mode 
        if(getFunctionType() == 'A'){
            ipLogBeanFromServer.setLogStatus('P');
            ipLogBeanFromServer.setNonMITPersonFlag(!instituteProposalLogDetailsForm.chkEmployee.isSelected());
            ipLogBeanFromServer.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
            setDataToBean(ipLogBeanFromServer);
        }else if(getFunctionType() == 'T'){                                     //for Temp mode 
            ipLogBeanFromServer.setLogStatus('T');
            ipLogBeanFromServer.setNonMITPersonFlag(!instituteProposalLogDetailsForm.chkEmployee.isSelected());
            ipLogBeanFromServer.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
            setDataToBean(ipLogBeanFromServer);
        }else if(getFunctionType() == 'M'){                                     //for Modify mode 
            setDataToBean(ipLogBeanFromServer);
        }

        boolean flag = false;
        try{
            StrictEquals stDataEquals = new StrictEquals();
            dataChanged = stDataEquals.compare(ipLogBeanFromServer,myIPlogbean);

            // If data changed.
            if(!dataChanged){
                flag = true;
            }else{
                flag = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //to fire save required in add/temp mode before window closing.
        if( getFunctionType() == 'A' || getFunctionType() == 'T') {
            if( instituteProposalLogDetailsForm.txtProposalNumber.getText().length() == 0){
                //proposal not yet logged , so fire save required.
                flag = true;
            }
        }
     return flag;
   }
    
   
    /**
     * For Validating Status combo box in modify mode
     */    
    public void itemStateChanged(ItemEvent e) {
        //if dialog is not visible then return
        if(dlgInstituteProposalLogDetailsController != null && 
            !dlgInstituteProposalLogDetailsController.isVisible()){
                return ;
        }
        CoeusVector cvFilterStatus = new CoeusVector();
 
        if(e.getStateChange() == e.DESELECTED){
            return ;
        }

        if(getFunctionType() == 'M'){
            
            ComboBoxBean comboStatus= (ComboBoxBean)instituteProposalLogDetailsForm.cmbStatus.getSelectedItem();
            String code = comboStatus.getCode();
            
            if(ipLogBeanFromServer.getProposalNumber().trim().startsWith("T") ){
                if(code.equals("T")){
                    statusComboBean = new ComboBoxBean("T",instituteProposalLogDetailsForm.cmbStatus.getSelectedItem().toString());
                    return ;
                }
            }else if(code.equals("P")){
                statusComboBean = new ComboBoxBean("P",instituteProposalLogDetailsForm.cmbStatus.getSelectedItem().toString());
                return;
            }
            if(code.equals("V")){
                statusComboBean = new ComboBoxBean("V",instituteProposalLogDetailsForm.cmbStatus.getSelectedItem().toString());
                return ;
            }else {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    STATUS_CHANGE) +comboStatus.toString().trim());
                    if(statusComboBean.getDescription().equals(EMPTY)){
                        Equals eqStatus = new Equals(CODE,EMPTY + ipLogBeanFromServer.getLogStatus());
                        cvFilterStatus = cvStatus.filter(eqStatus);
                        statusComboBean = (ComboBoxBean)cvFilterStatus.elementAt(0);
                    }
//                return ;
            }
            instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(statusComboBean);
            
//            if(!(code.equals("P"))&& !(code.equals("V"))){
//                if(ipLogBeanFromServer.getProposalNumber().trim().startsWith("T") && code.equals("T")){
//                    statusComboBean = new ComboBoxBean("T",instituteProposalLogDetailsForm.cmbStatus.getSelectedItem().toString().trim());
//                    return ;
//                }else{
//                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
//                        "instPropLog_exceptionCode.1409") +comboStatus.toString().trim());
//                    if (statusComboBean.getDescription().equals(EMPTY)){
//                        Equals eqStatus = new Equals(CODE,EMPTY_STRING + ipLogBeanFromServer.getLogStatus());
//                        cvFilterStatus = cvStatus.filter(eqStatus);
//                        statusComboBean = (ComboBoxBean)cvFilterStatus.elementAt(0);
//                    }
//                    instituteProposalLogDetailsForm.cmbStatus.setSelectedItem(statusComboBean);
//                }
//                return;
//            }
        }
    }
    
     //for validating the person name when focus is lost
     private boolean checkPersonName(){
       // boolean newPerson = false;
        PersonInfoFormBean personInfo = new PersonInfoFormBean();
        String personName = instituteProposalLogDetailsForm.txtPIName.getText().trim();
        if(personName.equals(EMPTY)){
            return false; 
        }
        String leadUnit = null;
        boolean mitPerson = instituteProposalLogDetailsForm.chkEmployee.isSelected();
        if(mitPerson == true){
            if(oldPersonInfo.equalsIgnoreCase(personName)){
               return true;
            }
            //Bug fix for person validation Start 1
            //personInfo = CoeusUtils.getInstance().getPersonInfoID( personName );
            personInfo = validatePersonName(personName);
            //Bug fix for person validation End 1
            
            
            if(personName != EMPTY ){
            //    newPerson= true;
                oldPersonInfo = personName;
            }
            if( personInfo == null || personInfo.getPersonID() == null){
                personInfo = null;
                instituteProposalLogDetailsForm.txtPIName.requestFocusInWindow();
                instituteProposalLogDetailsForm.txtPIName.setText(EMPTY);
                instituteProposalLogDetailsForm.txtPIName.setText(lastPerson);
                instituteProposalLogDetailsForm.chkEmployee.setSelected(true);
                
                //Bug fix for person validation Start 2
                /*CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                PERSON_VALIDATION_ERR ));*/
                //Bug fix for person validation End 2
                
		instituteProposalLogDetailsForm.lblPIName.requestFocusInWindow();
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                instituteProposalLogDetailsForm.txtPIName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return false;
            }else {
                UnitDetailFormBean unitInfoBean = new UnitDetailFormBean(); 
                personId = personInfo.getPersonID().trim();
                personName = personInfo.getFullName().trim();
                leadUnit = personInfo.getHomeUnit().trim();
                instituteProposalLogDetailsForm.txtPIName.setText(personName);
                lastPerson = personName;
                unitInfoBean = getLeadUnit(leadUnit);
                if(unitInfoBean != null && leadUnit != null){
                   // if(getFunctionType() != 'M' && getFunctionType() != 'D' && newPerson){
                        instituteProposalLogDetailsForm.txtLeadUnit.setText(leadUnit);
                        leadUnit = unitInfoBean.getUnitName();
                        instituteProposalLogDetailsForm.lblLeadUnitName.setText(leadUnit);
                    //}
                } 
                return true;
            }
        }
        return true;
     } 
     
     /**
      * For Validating Sponsor
      */     
     public void checkSponsor(){
         String sponsorName = CoeusGuiConstants.EMPTY_STRING;
         String sponsorCode = instituteProposalLogDetailsForm.txtSponsorID.getText().trim();
         if(sponsorCode.trim().equals(EMPTY)){
             if(!sponsorCode.equals(ipLogBeanFromServer.getSponsorCode())){
                 sponsorChanged = true;
             }
             return;
         }else if(!sponsorChanged && !sponsorCode.equals(ipLogBeanFromServer.getSponsorCode())){
             sponsorChanged = true;
         }
         if(sponsorChanged){
             try{
                 sponsorName= getSponsorName(instituteProposalLogDetailsForm.txtSponsorID.getText().trim());
                 //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                 //If sponsor is inactive through the sponsor invalid msg
//                if(sponsorName.trim().equals(EMPTY)) {
                 if(sponsorCode.trim().equals(EMPTY) || INACTIVE_STATUS.equals(getSponsorStatus()) || EMPTY.equals(getSponsorStatus())){
                 //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                     sponsorName = EMPTY;
                     CoeusOptionPane.showInfoDialog(
                             coeusMessageResources.parseMessageKey(SOPNSOR_VALIDATION_ERR));
                     instituteProposalLogDetailsForm.txtSponsorID.setText("");
                     instituteProposalLogDetailsForm.txtSponsorName.setText("");
                 }else{
                     instituteProposalLogDetailsForm.txtSponsorName.setText(sponsorName);
                 }
             }catch (CoeusException coeusException) {
                 coeusException.printStackTrace();
                 sponsorName = EMPTY;
             }
         }else{
             instituteProposalLogDetailsForm.txtSponsorName.setEditable(false);
             instituteProposalLogDetailsForm.txtSponsorName.setEnabled(false);
             instituteProposalLogDetailsForm.txtSponsorName.setText(ipLogBeanFromServer.getSponsorName());
         }
     }
        
     /**
      * For Disposing window upon esc pressed
      */     
     public void performDisposeWIndow() throws CoeusUIException{
         boolean flag = isDatachanged();
         if(getFunctionType() == 'D'){
             dlgInstituteProposalLogDetailsController.dispose();
         }else if(getFunctionType() == 'A' || getFunctionType()=='T'){
               flag = isDatachanged();
                if(flag == true){
                    performUpdate();
                    if(validation){
                        myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                        observable.notifyObservers(myIPlogbean);
                    }
                }else{
                        dlgInstituteProposalLogDetailsController.dispose();
                        if(ipLogBeanFromServer.getUpdateTimestamp () != null) {
                            observable.notifyObservers(ipLogBeanFromServer);
                        }
                }
         }else if(getFunctionType() == 'M') {
             ipLogBeanFromServer.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
             flag = isDatachanged();
             if(flag == true){
                 validation = false;
                 performUpdate();
                 if(yesClicked == true){
                     myIPlogbean.setProposalTypeDescription(instituteProposalLogDetailsForm.cmbProposalType.getSelectedItem().toString().trim());
                     observable.notifyObservers(myIPlogbean);
                 }
             }else{
                 dlgInstituteProposalLogDetailsController.dispose();
             }
         }else{
             dlgInstituteProposalLogDetailsController.dispose();
         }
     }
     
     
     //for validating the person name when focus is lost
     
     //Bug fix for person validation Start 3
     //private boolean validatePersonName(){
     private PersonInfoFormBean validatePersonName(String personName){
  
         
       /* PersonInfoFormBean personInfo = new PersonInfoFormBean();
        String personName = instituteProposalLogDetailsForm.txtPIName.getText().trim();
        if(personName.equals(EMPTY)){
            return false; 
        }
   //     String leadUnit = null;
        boolean mitPerson = instituteProposalLogDetailsForm.chkEmployee.isSelected();
        if(mitPerson == true){
            personInfo = CoeusUtils.getInstance().getPersonInfoID( personName );
            if(oldPersonInfo.equalsIgnoreCase(personName)){
               return true;
            }
            
            if(personName != EMPTY ){
            //    newPerson= true;
                oldPersonInfo = personName;
            }
            if( personInfo == null || personInfo.getPersonID() == null){
                personInfo = null;
                instituteProposalLogDetailsForm.txtPIName.requestFocusInWindow();
                instituteProposalLogDetailsForm.txtPIName.setText(EMPTY);
                instituteProposalLogDetailsForm.txtPIName.setText(lastPerson);
                instituteProposalLogDetailsForm.chkEmployee.setSelected(true);
                CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                PERSON_VALIDATION_ERR ));
                
		instituteProposalLogDetailsForm.lblPIName.requestFocusInWindow();
                dlgInstituteProposalLogDetailsController.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                instituteProposalLogDetailsForm.txtPIName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return false;
             }else {
                 return true;
             }
        } 
        return true;*/
      
        PersonInfoFormBean personInfo = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType('J');
        requesterBean.setDataObject(personName);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/unitServlet";
        
        AppletServletCommunicator comm = new AppletServletCommunicator
        (connectTo, requesterBean);
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
     //Bug fix for person validation End 3
     
     //case 3263 start
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
     //case 3263 end

    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    /**
     *This method used to get the sponsor status
     *@return sponsor status
     */
    public String getSponsorStatus() {
        return sponsorStatus;
    }

    /**
     * Method used to set the sponsor status
     * @param sponsorStatus
     */
    public void setSponsorStatus(String sponsorStatus) {
        this.sponsorStatus = sponsorStatus;
    }
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
}

