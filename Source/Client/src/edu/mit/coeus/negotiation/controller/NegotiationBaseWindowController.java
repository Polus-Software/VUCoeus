/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, commented unused imports and variables
 * on 11-APR-2011 by Maharaja Palanichamy
 */

package edu.mit.coeus.negotiation.controller;

import edu.mit.coeus.user.gui.UserDelegationForm;
import java.awt.event.ActionListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JInternalFrame;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.CoeusParameterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
//import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.negotiation.gui.NegotiationBaseWindow;
import edu.mit.coeus.negotiation.bean.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.search.gui.NewNegotiationSearch;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.BaseWindowObservable;
//import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
//import edu.mit.coeus.utils.Utils;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.utils.CoeusUtils;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import javax.swing.JOptionPane;
//start case 1735
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.utils.CurrentLockForm;

//end case 1735

/**
 * NegotiationBaseWindowController.java
 * Created on July 13, 2004, 3:08 PM
 * @author  Vyjayanthi
 */

//start case 1735
//public class NegotiationBaseWindowController extends NegotiationController
//implements ActionListener, VetoableChangeListener {
public class NegotiationBaseWindowController extends NegotiationController
implements ActionListener, BeanUpdatedListener,VetoableChangeListener {   
//end case 1735    
    
    private NegotiationBaseWindow negotiationBaseWindow;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private NegotiationDetailController negotiationDetailController;
    private CoeusMessageResources coeusMessageResources;
    private boolean closed;
    private boolean canDisplay;
    private QueryEngine queryEngine;
    private String invokedFrom;
    private CoeusVector cvUserRights;
    private NegotiationCustomDataController negotiationCustomDataController;
    private Hashtable negotiationData;
    private String negotiationNumber;
    private boolean closeWindow;
    private BaseWindowObservable observable  = new BaseWindowObservable();
    private NegotiationHeaderBean negotiationHeaderBean;
    private NegotiationInfoBean negotiationInfoBean;
    private CoeusVector cvInstitutePropRights;
    private CoeusVector cvMedusaRights;
    //start case 1735
//    private boolean dataFetched;
    public boolean dataFetched;
    //end case 1735
    // 2812: Modify Negotiation Record Titlebar
    private String title;           
    
    private static final String PARAMETER_NAME_FIELD = "parameterName";
    private static final String AC_TYPE = "acType";
    
    private static final String SERVLET = "/NegotiationMaintenanceServlet";
    
    private static final String PROPOSAL_NUMBER = "PROPOSAL_NUMBER";
    private static final String PROPOSAL_TYPE_CODE = "PROPOSAL_TYPE_CODE";
    private static final String PROPOSAL_TYPE_DESC = "PROPOSAL_TYPE_DESC";
    private static final String TITLE = "TITLE";
    private static final String SPONSOR_CODE = "SPONSOR_CODE";
    private static final String SPONSOR_NAME = "SPONSOR_NAME";
    /*modified for the Bug Fix:1729 start step:1*/
//    private static final String PI_NAME = "PERSON_NAME";
//    private static final String LEAD_UNIT = "UNIT_NUMBER";
//    private static final String LEAD_UNIT_NAME = "UNIT_NAME";
    private static final String PI_NAME = "PI_NAME";
    private static final String LEAD_UNIT = "LEAD_UNIT";
    private static final String LEAD_UNIT_NAME = "LEAD_UNIT_NAME";
    //End Bug Fix:1729 Step:1
    
    private static final String LETTER_T = "T";
    private static final String NEW_NEGOTIATION = "New Negotiation";
    private static final String COLON = " : ";
    
    private static final char GET_NEGOTIATION_DATA = 'C';
    private static final char UPDATE_NEGOTIATION = 'F';
    private static final char NEW_MODE = 'N';
    private static final char RELEASE_LOCK = 'G';
    private static final char GET_NEGOTIATION_WHILEMODIFY = 'H';
    private static final char NEGOTIATION_FROM_INSTPROPOSAL = 'J';
    
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
//    private static final String FUNCTIONALITY_NOT_IMPL = "funcNotImpl_exceptionCode.1100";
    private static final String ERROR_RETRIEVING_DETAILS = "negotiationDetail_exceptionCode.1101";
    private static final String MISSING_PARAMETER = "negotiationDetail_exceptionCode.1102";
    private static final String CANNOT_OPEN_MEDUSA_FOR_TEMP_PROP = "negotiationBaseWindow_exceptionCode.1054";
    private static final String NO_NEGOTIATION_EXISTS = "negotiationDetail_exceptionCode.1107";
    private static final String NO_RIGHT_TO_VIEW_NEGOTIATION = "negotiationBaseWindow_exceptionCode.1055";    
    private static final String CREATE_NEGOTIATION = "negotiationDetail_exceptionCode.1108";
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    // 2812: Modify Negotiation Record Titlebar 
    private boolean isNewNegotiation;
    // 3587: Multi Campus Enahncements
    private static final char CAN_MODIFY_NEGOTIATION = 'K';
    //Added for COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
    private String proposalLeadUnit = CoeusGuiConstants.EMPTY_STRING;
    //COEUSDEV-268 : End
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    public static final int ACTION_MAIL_NEGOTIATION = 100;
    private static final char COMPARE_PARAMETER_VALUE = 'T';
    private static final String CLOSED_NEGOTIATION_STATUS = "CLOSED_NEGOTIATION_STATUS";
    //COEUSDEV - 733 Create a new notification for negotiation module - End
    
    /** Creates a new instance of NegotiationBaseWindowController
     * @param invokedFrom holds from where Negotiation is opened
     * it is either Negotiation List or Institute Proposal List
     * @param functionType functiontype
     * @param negotiationBaseBean negotiation base bean.
     */
   
    public NegotiationBaseWindowController(final String invokedFrom, 
    final char functionType,final NegotiationBaseBean bean) {
        super(bean);
        this.invokedFrom = invokedFrom;
        negotiationInfoBean = new NegotiationInfoBean();
        if(bean != null) {
            this.negotiationBaseBean = bean;
            negotiationNumber = negotiationBaseBean.getNegotiationNumber();
            negotiationInfoBean.setNegotiationNumber(negotiationNumber);
        }else {
            negotiationNumber = EMPTY;
        }
        setFunctionType(functionType);
        observable.setFunctionType(functionType);
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        // 2812: Modify Negotiation Record Titlebar - Start
        if(CoeusGuiConstants.ADD_MODE == functionType){
            isNewNegotiation = true;
        }
        generateNegotiationWindowTitle();
//        negotiationBaseWindow = new NegotiationBaseWindow(CoeusGuiConstants.NEGOTIATION_DETAILS_FRAME_TITLE + COLON + negotiationNumber, mdiForm);
        negotiationBaseWindow = new NegotiationBaseWindow(this.title, mdiForm);
        // 2812: Modify Negotiation Record Titlebar - End
        blockEvents(true);
        if( getFunctionType() != TypeConstants.ADD_MODE ){
            if(!fetchData(getFunctionType())){
                canDisplay = false;
                return ;
            }else {
                //Get Data From server and initialize components.
                canDisplay  = true;
                if(negotiationDetailController == null) {
                    negotiationDetailController = new NegotiationDetailController(negotiationBaseBean, getFunctionType());
                }
                unRegisterComponents();
                registerComponents();
                if( getFunctionType() != TypeConstants.ADD_MODE ){
                    negotiationDetailController.prepareQueryKey(negotiationBaseBean);
                    negotiationDetailController.initComponents();

                    //Initialize the Custom Data controller
                    negotiationCustomDataController = new NegotiationCustomDataController(
                        negotiationBaseBean, getFunctionType());
                }
                negotiationDetailController.unRegisterComponents();
                negotiationDetailController.registerComponents();
                negotiationDetailController.setFunctionType(getFunctionType());
                negotiationDetailController.setUserRights(cvUserRights);
                negotiationDetailController.setNegotiationInfoBean(negotiationInfoBean);
                negotiationDetailController.setFormData(null);
            }
        }
        blockEvents(false);
        initComponents();
    }
    
    /** Get data depending on the mode
     */
    private boolean fetchData(char functionType) {
        if( dataFetched ){
            return true;
        }
        RequesterBean requesterBean = new RequesterBean();
        if( functionType == TypeConstants.ADD_MODE ) {
            requesterBean.setFunctionType(NEW_MODE);
        }else if( functionType == TypeConstants.MODIFY_MODE ) {
            requesterBean.setFunctionType(GET_NEGOTIATION_WHILEMODIFY);
        }else if( functionType == TypeConstants.DISPLAY_MODE ) {
            requesterBean.setFunctionType(GET_NEGOTIATION_DATA);
        }
        
        if( !invokedFrom.equalsIgnoreCase(CoeusGuiConstants.NEGOTIATION_LIST) ){
            requesterBean.setFunctionType(NEGOTIATION_FROM_INSTPROPOSAL);
        }
        requesterBean.setDataObject(negotiationInfoBean.getNegotiationNumber());
        //Added for COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
        //Proposal leadunit to check user has MODIFY_ACTIVIES right
        requesterBean.setId(proposalLeadUnit);
        //COEUSDEV-268 : End
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);        
        
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
                        return false;
        }
        
        //Prepare query key
        queryKey = NEGOTIATION + this.negotiationInfoBean.getNegotiationNumber();
       
        if(responderBean.isSuccessfulResponse()) {
            if( invokedFrom.equalsIgnoreCase(CoeusGuiConstants.NEGOTIATION_LIST) ){
                Hashtable negotiationData = new Hashtable();
                if( functionType != TypeConstants.ADD_MODE ) {
                    Vector vecData = responderBean.getDataObjects();
                    negotiationData = (Hashtable)vecData.get(0);
                    
                    this.cvUserRights = (CoeusVector)vecData.get(1);
                    extractDataToQueryEngine(negotiationData);
                }else {
                    this.negotiationData = (Hashtable)responderBean.getDataObject();
                    extractDataToQueryEngine(this.negotiationData);
                }
            }else {
                //Opened from Medusa or Institute Proposal
                Vector vecData = responderBean.getDataObjects();
                this.negotiationData = (Hashtable)vecData.get(0);
                this.cvUserRights = (CoeusVector)vecData.get(2);
                if( invokedFrom.equalsIgnoreCase(CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST) ){
                    //Opened from IP
                    cvInstitutePropRights = (CoeusVector)vecData.get(1);
                    checkRightsFromIP();
                }else{
                    //Opened from Medusa
                    cvMedusaRights = (CoeusVector)vecData.get(1);
                    checkRightsFromMedusa();
                }        
                if( canDisplay ){
                    extractDataToQueryEngine(negotiationData);
                }else {
                    return false;
                }
            }
            //Added for COEUSDEV-294 : Error adding activity to a negotiation - Start
            //Refreshing the Actvities data when negotiation is saved
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(new NegotiationActivitiesBean());
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
            //COEUSDEV-294 : End
            dataFetched = true;
        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            // Added by chandra. In new mode if it is locked then show the message
            // and close the internal frame immediately.
            // 15th Sept 2004 -start
            negotiationBaseWindow.doDefaultCloseAction();
            // End chandra 15th Sept 2004
            return false;
        }
        
        return true;
    }
    
    /** Extracts negotiation data and stores to QueryEngine.
     * @param negotiationData negotiation data to be saves to query engine.
     */
private void extractDataToQueryEngine(Hashtable negotiationData) {
        NegotiationHeaderBean negotiationHeaderBean = (NegotiationHeaderBean)negotiationData.get(NegotiationHeaderBean.class);
        if(negotiationHeaderBean != null) {
            CoeusVector cvNegotiationHeader = new CoeusVector();
            cvNegotiationHeader.add(negotiationHeaderBean);
            negotiationData.put(NegotiationHeaderBean.class, cvNegotiationHeader);
        }

        NegotiationInfoBean negotiationInfoBean = (NegotiationInfoBean)negotiationData.get(NegotiationInfoBean.class);
        if( negotiationInfoBean != null ){
            CoeusVector cvNegotiationInfo = new CoeusVector();
            cvNegotiationInfo.add(negotiationInfoBean);
            negotiationData.put(NegotiationInfoBean.class, cvNegotiationInfo);
            this.negotiationInfoBean = (NegotiationInfoBean)cvNegotiationInfo.get(0);
        }
        //Added for COEUSDEV-294 : Error adding activity to a negotiation - Start
        //Setting the new  activities bean  data from DB
        CoeusVector cvActivitiesData = (CoeusVector)negotiationData.get(NegotiationActivitiesBean.class);
        if(cvActivitiesData == null){
            cvActivitiesData = new CoeusVector();
        }
        negotiationData.put(NegotiationActivitiesBean.class, cvActivitiesData);
        //COEUSDEV-294 : End

        CoeusParameterBean coeusParameterBean = (CoeusParameterBean)negotiationData.get(CoeusParameterBean.class);
        if( coeusParameterBean != null ){
            CoeusVector cvParameter = new CoeusVector();
            cvParameter.add(coeusParameterBean);
            negotiationData.put(CoeusParameterBean.class, cvParameter);
        }
        //Added for COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
        //Adds the whether user has MODIFY_ACTIVITIES right or not to the query engine data collection
        Boolean modifyActivitiesRight = (Boolean)negotiationData.get("MODIFY_ACTIVITIES");
        if(modifyActivitiesRight != null){
            CoeusVector cvRight = new CoeusVector();
            cvRight.add(modifyActivitiesRight);
            negotiationData.put("MODIFY_ACTIVITIES", cvRight);
        }
        //COEUSDEV-268 : End
        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
        String enableNegotiationActivDescMand = (String)negotiationData.get(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY);
        CoeusVector cvActivityParameter = new CoeusVector();
        cvActivityParameter.add(enableNegotiationActivDescMand);
        negotiationData.put(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY, cvActivityParameter);
        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End

        queryEngine.addDataCollection(queryKey, negotiationData);
    }
    
    /** Initialize the form
     */
    private void initComponents(){
        if( negotiationDetailController == null ){
            negotiationDetailController = new NegotiationDetailController(new NegotiationInfoBean(), getFunctionType());
        }
        negotiationBaseWindow.getContentPane().setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        negotiationBaseWindow.getContentPane().add(negotiationDetailController.getControlledUI());
        negotiationBaseWindow.setFunctionType( getFunctionType() );

        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            //Disable save button and menu item
            negotiationBaseWindow.btnSave.setEnabled(false);
            negotiationBaseWindow.mnuItmSave.setEnabled(false);
            
            try{
                //Disable the Custom Data menu item
                CoeusVector cvData = queryEngine.getDetails(queryKey, NegotiationCustomElementsBean.class);
                if( cvData == null || cvData.size() == 0 ){
                    negotiationBaseWindow.mnuItmCustomData.setEnabled(false);
                }

            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
        }else {
            //Disable Next and Previous buttons
            negotiationBaseWindow.btnNext.setEnabled(false);
            negotiationBaseWindow.btnPrevious.setEnabled(false);
            negotiationBaseWindow.mnuItmNext.setEnabled(false);
            negotiationBaseWindow.mnuItmPrevious.setEnabled(false);
        }
        //Disable next and previous buttons if negotiation is opened from 
        //institute proposal list rather than negotiation list because there 
        //may not be a negotiation for the next proposal, and the check is done in the list window.
        if( invokedFrom.equalsIgnoreCase(CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST) ){
            negotiationBaseWindow.btnNext.setEnabled(false);
            negotiationBaseWindow.btnPrevious.setEnabled(false);
            negotiationBaseWindow.mnuItmNext.setEnabled(false);
            negotiationBaseWindow.mnuItmPrevious.setEnabled(false);
        }

        if( getFunctionType() != TypeConstants.ADD_MODE ){
            try{
                CoeusVector cvData = queryEngine.getDetails(queryKey, NegotiationInfoBean.class);
                if( getFunctionType() != TypeConstants.ADD_MODE && 
                ( cvData == null || cvData.size() == 0 )){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(ERROR_RETRIEVING_DETAILS));
                    canDisplay = false;
                    return ;
                }
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
            }
        }else {
            canDisplay = true;
        }
    }
    
    /** Called when opened from Medusa
     */
    private void checkRightsFromMedusa(){
        char functionType = getFunctionType();
        int negCount = ((Integer)cvMedusaRights.get(0)).intValue();
        boolean hasModifyRight = ((Boolean)cvMedusaRights.get(1)).booleanValue();
        boolean hasViewRight = ((Boolean)cvMedusaRights.get(2)).booleanValue();
        String piName = (String)negotiationData.get(KeyConstants.PI_USERNAME);

        if( hasModifyRight ){
            functionType = TypeConstants.MODIFY_MODE;
        }else {
            //Modified with case 2806:Lock should be released if the user doesnt have edit rights
            // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role.
            if( negCount > 0 ){
                releaseLock();
            } 
            //Check for view right in this unit
            if( hasViewRight ){
                functionType = TypeConstants.DISPLAY_MODE;
            }else {
                if( piName.equalsIgnoreCase(mdiForm.getUserName()) ){
                    functionType = TypeConstants.DISPLAY_MODE;
                }else {
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(NO_RIGHT_TO_VIEW_NEGOTIATION));
                    canDisplay = false;
                    return;
                }
            }
        }
        
        //A negotiation exists for this proposal
        if( negCount > 0 ){
            if( functionType == TypeConstants.DISPLAY_MODE ){
                setFunctionType(functionType);
            }else {
                setFunctionType(TypeConstants.MODIFY_MODE);
            }
        }else {
            //No negotiations exist for this proposal
            if( functionType == TypeConstants.DISPLAY_MODE ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_NEGOTIATION_EXISTS));
                canDisplay = false;
                return ;
            }else {
                //Ask if user wants to create a new negotiation
                int confirm = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(CREATE_NEGOTIATION),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                switch(confirm){
                    case CoeusOptionPane.SELECTION_YES:
                        setFunctionType(TypeConstants.ADD_MODE);
                        canDisplay = true;
                        return ;
                    case CoeusOptionPane.SELECTION_NO:
                        canDisplay = false;
                        return ;
                }
            }
        }
        canDisplay = true;
    }
    
    /** Called when opened from Institute Proposal
     */
    private void checkRightsFromIP(){
        char functionType = getFunctionType();
        int negCount = ((Integer)cvInstitutePropRights.get(0)).intValue();
        boolean hasModifyRight = ((Boolean)cvInstitutePropRights.get(1)).booleanValue();
        boolean hasViewRight = ((Boolean)cvInstitutePropRights.get(2)).booleanValue();
        boolean hasLeadUnit = true;//((Boolean)cvInstitutePropRights.get(3)).booleanValue();
        String piName = (String)negotiationData.get(KeyConstants.PI_USERNAME);
        if( hasModifyRight ){
            functionType = TypeConstants.MODIFY_MODE;
        }else {
            //Check if user is PI
            if( piName.equalsIgnoreCase(mdiForm.getUserName()) ){
                functionType = TypeConstants.DISPLAY_MODE;
            }else {
                //Check for right in Lead Unit
                if( hasLeadUnit ){
                    if( hasViewRight ){
                        functionType = TypeConstants.DISPLAY_MODE;
                    }else {
                        //Disable negotiation menu item
                        
                    }
                }else{
                    //No Lead Unit
                    //Disable negotiation menu item
                }
            }
        }
        
        //A negotiation exists for this proposal
        if( negCount > 0 ){
            if( functionType == TypeConstants.DISPLAY_MODE ){
                releaseLock();
                setFunctionType(functionType);
            }else {
                setFunctionType(TypeConstants.MODIFY_MODE);
            }
        }else {
            //No negotiations exist for this proposal
            if( functionType == TypeConstants.DISPLAY_MODE ){
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_NEGOTIATION_EXISTS));
                canDisplay = false;
                return ;
            }else {
                //Ask if user wants to create a new negotiation
                int confirm = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(CREATE_NEGOTIATION),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                switch(confirm){
                    case CoeusOptionPane.SELECTION_YES:
                        setFunctionType(TypeConstants.ADD_MODE);
                        // 2812: Modify Negotiation Record Titlebar - Start
                        generateNegotiationWindowTitle();
                        isNewNegotiation = true;
                        negotiationBaseWindow.setTitle(title);
                        // 2812: Modify Negotiation Record Titlebar - End
                        canDisplay = true;
                        return ;
                    case CoeusOptionPane.SELECTION_NO:
                        canDisplay = false;
                        return ;
                }
            }
        }
        canDisplay = true;
    }
    
    /** Displays the Form which is being controlled.
     */
    public void display() {
        if(!canDisplay){
            return ;
        }
        
        try{
            if( getFunctionType() != TypeConstants.ADD_MODE ){
                checkNegotiationParameter();
            }
            
            char functionType = getFunctionType();
            String negotiationNumber = NEW_NEGOTIATION;
            if( functionType == TypeConstants.ADD_MODE ){
                functionType = TypeConstants.MODIFY_MODE;
            }else {
                negotiationNumber = negotiationBaseBean.getNegotiationNumber();
            }
            
//            if( !invokedFrom.equalsIgnoreCase(CoeusGuiConstants.MEDUSA_NEGOTIATION) ){
            if( mdiForm.getFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationNumber) == null ){
                mdiForm.putFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationNumber, functionType, negotiationBaseWindow);
                mdiForm.getDeskTopPane().add(negotiationBaseWindow);
                negotiationBaseWindow.setSelected(true);
                negotiationBaseWindow.setVisible(true);
            }
            
            if( getFunctionType() == TypeConstants.ADD_MODE ){
                if( invokedFrom.equalsIgnoreCase(CoeusGuiConstants.NEGOTIATION_LIST) ){
                    showProposalSearch();
                    // 2812: Modify Negotiation Record Titlebar 
                    negotiationBaseWindow.setTitle("New Negotiation" + COLON + this.negotiationNumber);
                    if( closeWindow ){
                        mdiForm.removeFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, NEW_NEGOTIATION);
                        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
                        if( windowMenu != null ){
                            windowMenu = windowMenu.removeMenuItem( negotiationBaseWindow.getTitle() );
                        }
                        negotiationBaseWindow.doDefaultCloseAction();
                    }
                }else {
                    //If negotiation is opened from Medusa or Institute Proposal
                    //Populate the header details
                    setProposalHeaderInfo();
                }
            }
            
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    /** To set the proposal header data
     */
    private void setProposalHeaderInfo(){
        if(!fetchData(getFunctionType())){
            canDisplay = false;
            return ;
        }else {
            //Get Data From server and initialize components.
            canDisplay  = true;
            negotiationDetailController.setNegotiationInfoBean(negotiationInfoBean);
            unRegisterComponents();
            registerComponents();
            negotiationDetailController.prepareQueryKey(negotiationInfoBean);
            negotiationDetailController.initComponents();

            //Initialize the Custom Data controller
            negotiationCustomDataController = new NegotiationCustomDataController(
                negotiationInfoBean, getFunctionType());

            negotiationDetailController.unRegisterComponents();
            negotiationDetailController.registerComponents();
            negotiationDetailController.setFunctionType(getFunctionType());
            if( invokedFrom.equalsIgnoreCase( CoeusGuiConstants.INSTITUTE_PROPOSAL_LIST ) ){
                negotiationDetailController.setInstitutePropRights(cvInstitutePropRights);
            }else {
                negotiationDetailController.setMedusaRights(cvMedusaRights);
            }
            negotiationDetailController.setUserRights(cvUserRights);
            negotiationDetailController.setFormData(null);
        }            

        //Get Database Timestamp
        java.sql.Timestamp dbTimeStamp = CoeusUtils.getDBTimeStamp();
        java.sql.Date startDate = new java.sql.Date(dbTimeStamp.getTime());

        if( negotiationInfoBean.getStartDate() == null ){
            negotiationInfoBean.setStartDate(startDate);
            negotiationInfoBean.setAcType(TypeConstants.INSERT_RECORD);
        }

        //Prepare query key
        super.queryKey = NEGOTIATION + negotiationInfoBean.getNegotiationNumber();
        negotiationDetailController.prepareQueryKey(negotiationInfoBean);

        //Remove the displayed frame
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( mdiForm.getFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, NEW_NEGOTIATION) != null ){
//        if( !invokedFrom.equalsIgnoreCase(CoeusGuiConstants.MEDUSA_NEGOTIATION) ){
            mdiForm.removeFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, NEW_NEGOTIATION);
            if( windowMenu != null ){
                windowMenu = windowMenu.removeMenuItem( negotiationBaseWindow.getTitle() );
            }
            mdiForm.getDeskTopPane().remove(negotiationBaseWindow);
            try{
                negotiationBaseWindow.setSelected(false);
            }catch (PropertyVetoException propertyVetoException) {
                propertyVetoException.printStackTrace();
            }
            negotiationBaseWindow.setVisible(false);
        }

        //Add a new Frame with the negotiation number
        if( mdiForm.getFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationInfoBean.getNegotiationNumber()) == null ){
            mdiForm.putFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationInfoBean.getNegotiationNumber(), getFunctionType(), negotiationBaseWindow);
            //update to handle new window menu item to the existing Window Menu.
            if( windowMenu != null ){
                windowMenu = windowMenu.addNewMenuItem( negotiationBaseWindow.getTitle(), negotiationBaseWindow );
                windowMenu.updateCheckBoxMenuItemState( negotiationBaseWindow.getTitle(), true );
            }
            mdiForm.getDeskTopPane().add(negotiationBaseWindow);
            try{
                negotiationBaseWindow.setSelected(true);
            }catch (PropertyVetoException propertyVetoException) {
                propertyVetoException.printStackTrace();
            }
            negotiationBaseWindow.setVisible(true);
            mdiForm.refreshWindowMenu(windowMenu);
        }

        if( negotiationInfoBean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
            queryEngine.insert(queryKey, NegotiationInfoBean.class, negotiationInfoBean);
        }

        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(new NegotiationHeaderBean());
        beanEvent.setSource(this);
        fireBeanUpdated(beanEvent);
        
    }
    
    /** To check if the COEUS_MODULE_NEGOTIATION parameter is present in OSP$PARAMETER
     * display message if the parameter is missing
     */
    private void checkNegotiationParameter(){
        //Display message if COEUS_MODULE_NEGOTIATION parameter is missing
        try{
            CoeusVector cvData = queryEngine.getDetails(queryKey, CoeusParameterBean.class);
            if( cvData != null && cvData.size() > 0 ){
                CoeusVector cvParam = cvData.filter(new Equals(PARAMETER_NAME_FIELD,
                    CoeusConstants.COEUS_MODULE_NEGOTIATION));
                if( cvParam != null && cvParam.size() > 0 ){
                    CoeusParameterBean coeusParameterBean = (CoeusParameterBean)cvParam.get(0);
                    if( coeusParameterBean.getParameterValue() == null ){
                        CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(MISSING_PARAMETER));
                    }
                }
            }
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
    }
    
    /** Displays the Proposal search screen
     */
    private void showProposalSearch(){
        try{
            /*Modified for the Bug Fix:1729 start step:2*/
//             NewNegotiationSearch newNegotiationSearch = 
//                new NewNegotiationSearch(mdiForm, "PROPOSALSEARCH" , 
//                        CoeusSearch.TWO_TABS);
            NewNegotiationSearch newNegotiationSearch = 
                new NewNegotiationSearch(mdiForm, "NEWNEGOTIATIONSEARCH" , 
                        CoeusSearch.TWO_TABS);
            /*End Bug Fix:1729 End Step:2*/
            newNegotiationSearch.showSearchWindow();
            HashMap selectedRow = newNegotiationSearch.getSelectedRow();
            if(selectedRow == null || selectedRow.isEmpty()) {
                closeWindow = true;
                return ;
            }
            
            String proposalNumber = selectedRow.get(PROPOSAL_NUMBER).toString();
            negotiationNumber = proposalNumber;
            negotiationInfoBean.setNegotiationNumber(negotiationNumber);
            //Added for COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
            if(selectedRow != null){
             proposalLeadUnit = (String)selectedRow.get("LEAD_UNIT");
            }
            //COEUSDEV-268 : End
            if(!fetchData(getFunctionType())){
                canDisplay = false;
                return ;
            }else {
                //Get Data From server and initialize components.
                canDisplay  = true;
                negotiationDetailController.setNegotiationInfoBean(negotiationInfoBean);
                unRegisterComponents();
                registerComponents();
                negotiationDetailController.prepareQueryKey(negotiationInfoBean);
                negotiationDetailController.initComponents();

                //Initialize the Custom Data controller
                negotiationCustomDataController = new NegotiationCustomDataController(
                    negotiationInfoBean, getFunctionType());

                negotiationDetailController.unRegisterComponents();
                negotiationDetailController.registerComponents();
                negotiationDetailController.setFunctionType(getFunctionType());
                //negotiationDetailController.setUserRights(cvUserRights);
                negotiationDetailController.setFormData(null);
            }            
            
            int propTypeCode = Integer.parseInt(selectedRow.get(PROPOSAL_TYPE_CODE).toString());
            String propTypeDesc = selectedRow.get(PROPOSAL_TYPE_DESC).toString();
            String title = selectedRow.get(TITLE).toString();
            String sponsorCode = (String)selectedRow.get(SPONSOR_CODE);
            /*Bug Fix Id : 1864 Start 1
             */
            //String sponsorName = selectedRow.get(SPONSOR_NAME).toString();
            String sponsorName =null;
            if(selectedRow.get(SPONSOR_NAME) == null){
                sponsorName ="";
            }else{
                sponsorName = selectedRow.get(SPONSOR_NAME).toString();
            }
            /*Bug Fix Id : 1864 End 1
             */
            String piName = selectedRow.get(PI_NAME).toString();
            String leadUnit = selectedRow.get(LEAD_UNIT).toString();
            String leadUnitName = selectedRow.get(LEAD_UNIT_NAME).toString();
            
            NegotiationInfoBean negotiationInfoBean = new NegotiationInfoBean();
            negotiationHeaderBean = new NegotiationHeaderBean();

            //Set the proposal number as the negotiation number
            negotiationInfoBean.setNegotiationNumber(proposalNumber);
            
            //Get Database Timestamp
            java.sql.Timestamp dbTimeStamp = CoeusUtils.getDBTimeStamp();
            java.sql.Date startDate = new java.sql.Date(dbTimeStamp.getTime());
            negotiationInfoBean.setStartDate(startDate);
            negotiationInfoBean.setAcType(TypeConstants.INSERT_RECORD);
            
            negotiationHeaderBean.setNegotiationNumber(proposalNumber);
            negotiationHeaderBean.setProposalNumber(proposalNumber);
            negotiationHeaderBean.setProposalTypeCode(propTypeCode);
            negotiationHeaderBean.setProposalTypeDescription(propTypeDesc);
            negotiationHeaderBean.setTitle(title);
            negotiationHeaderBean.setSponsorCode(sponsorCode);
            negotiationHeaderBean.setSponsorName(sponsorName);
            negotiationHeaderBean.setPiName(piName);
            negotiationHeaderBean.setLeadUnit(leadUnit);
            negotiationHeaderBean.setUnitName(leadUnitName);
            negotiationHeaderBean.setAcType(TypeConstants.INSERT_RECORD);
            //case 3961 start            
            sponsorCode = (String)selectedRow.get("PRIME_SPONSOR_CODE");            
            sponsorName =null;
            if(selectedRow.get("PRIME_SPONSOR_NAME") == null){
                sponsorName ="";
            }else{
                sponsorName = selectedRow.get("PRIME_SPONSOR_NAME").toString();
            }
            negotiationHeaderBean.setPrimeSponsorCode(sponsorCode);
            negotiationHeaderBean.setPrimeSponsorName(sponsorName);
            //case 3961 end
            
            //Prepare query key
            super.queryKey = NEGOTIATION + negotiationInfoBean.getNegotiationNumber();
            negotiationDetailController.prepareQueryKey(negotiationInfoBean);
            
            //Remove the displayed frame
            mdiForm.removeFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, NEW_NEGOTIATION);
            edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
            if( windowMenu != null ){
                windowMenu = windowMenu.removeMenuItem( negotiationBaseWindow.getTitle() );
            }
            
            //Add a new Frame with the negotiation number
            mdiForm.putFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationInfoBean.getNegotiationNumber(), TypeConstants.MODIFY_MODE, negotiationBaseWindow);
            //update to handle new window menu item to the existing Window Menu.
            if( windowMenu != null ){
                windowMenu = windowMenu.addNewMenuItem( negotiationBaseWindow.getTitle(), negotiationBaseWindow );
                windowMenu.updateCheckBoxMenuItemState( negotiationBaseWindow.getTitle(), true );
            }
            mdiForm.refreshWindowMenu(windowMenu);

            queryEngine.insert(queryKey, NegotiationInfoBean.class, negotiationInfoBean);
            queryEngine.insert(queryKey, NegotiationHeaderBean.class, negotiationHeaderBean);        
            BeanEvent beanEvent = new BeanEvent();
            beanEvent.setBean(new NegotiationHeaderBean());
            beanEvent.setSource(this);
            fireBeanUpdated(beanEvent);
        }catch (Exception exception ){
            exception.printStackTrace();
        }       
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
    }
    
    /** An overridden method of the controller
     * @return negotiationDetailForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return negotiationBaseWindow;
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
        negotiationBaseWindow.addVetoableChangeListener(this);
        negotiationBaseWindow.btnNext.addActionListener(this);
        negotiationBaseWindow.btnPrevious.addActionListener(this);
        negotiationBaseWindow.btnSave.addActionListener(this);
        negotiationBaseWindow.btnMedusa.addActionListener(this);
        negotiationBaseWindow.btnClose.addActionListener(this);
        
        negotiationBaseWindow.mnuItmNext.addActionListener(this);
        negotiationBaseWindow.mnuItmPrevious.addActionListener(this);
        negotiationBaseWindow.mnuItmSave.addActionListener(this);
        negotiationBaseWindow.mnuItmMedusa.addActionListener(this);
        negotiationBaseWindow.mnuItmClose.addActionListener(this);
        negotiationBaseWindow.mnuItmCustomData.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        negotiationBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        negotiationBaseWindow.mnuItmPreferences.addActionListener(this);
        negotiationBaseWindow.mnuItmExit.addActionListener(this);//bug fix id 1651
        //Commented since we are not using it in Coeus 4.0
        //negotiationBaseWindow.mnuItmPrintSetup.addActionListener(this);
        //negotiationBaseWindow.mnuItmPrintSetup.addActionListener(this);   
        negotiationBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        
        //start case 1735
        addBeanUpdatedListener(this, NegotiationInfoBean.class);
        //end case 1735
    }
    
    /** This method is used to remove the listeners of the components.
     */
    private void unRegisterComponents() {
        negotiationBaseWindow.removeVetoableChangeListener(this);
        negotiationBaseWindow.btnNext.removeActionListener(this);        
        negotiationBaseWindow.btnPrevious.removeActionListener(this);
        negotiationBaseWindow.btnSave.removeActionListener(this);
        negotiationBaseWindow.btnMedusa.removeActionListener(this);
        negotiationBaseWindow.btnClose.removeActionListener(this);
        negotiationBaseWindow.mnuItmNext.removeActionListener(this);
        negotiationBaseWindow.mnuItmPrevious.removeActionListener(this);        
        negotiationBaseWindow.mnuItmSave.removeActionListener(this);
        negotiationBaseWindow.mnuItmMedusa.removeActionListener(this);
        negotiationBaseWindow.mnuItmClose.removeActionListener(this);
        negotiationBaseWindow.mnuItmCustomData.removeActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        //negotiationBaseWindow.mnuItmPrintSetup.removeActionListener(this);
        //negotiationBaseWindow.mnuItmPrintSetup.removeActionListener(this);
        
        //start case 1735
        removeBeanUpdatedListener(this, NegotiationInfoBean.class);
        //end case 1735
    }
    
    /** Saves the Form Data.
     */
    public void saveFormData() {
        negotiationDetailController.saveFormData();
    }
    
    /** Saves negotiation data to the server
     * @throws CoeusUIException if any exception occurs.
     */
    private void saveNegotiation(boolean beforeClose)throws CoeusUIException{
        try{
            if(!validate()) {
                throw new CoeusUIException();
            }
            //COEUSDEV - 733 Create a new notification for negotiation module - Start
            int oldStatusCode = negotiationInfoBean.getAwStatusCode();
            //COEUSDEV - 733 Create a new notification for negotiation module - End
            
            saveFormData();

            Hashtable negotiationData = new Hashtable();
            CoeusVector cvData = queryEngine.getDetails(queryKey, NegotiationInfoBean.class);
            negotiationData.put(NegotiationInfoBean.class, cvData);

            cvData = queryEngine.getDetails(queryKey, NegotiationActivitiesBean.class);
            negotiationData.put(NegotiationActivitiesBean.class, cvData);

            cvData = queryEngine.getDetails(queryKey, NegotiationCustomElementsBean.class);
            negotiationData.put(NegotiationCustomElementsBean.class, cvData);
            
            //case 3590 start
            cvData = queryEngine.getDetails(queryKey, NegotiationLocationBean.class);
            negotiationData.put(NegotiationLocationBean.class, cvData);
            //case 3590 end
            if(beforeClose) {
                //Saving before close. Release lock.
                negotiationData.put(CoeusConstants.IS_RELEASE_LOCK, new Boolean(true));
            }
            
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(UPDATE_NEGOTIATION);
            requesterBean.setId(negotiationNumber);
            requesterBean.setDataObject(negotiationData);
            
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            if(responderBean == null) {
                //Could not contact server.
                CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
                throw new CoeusUIException();
            }
            
            if(responderBean.isSuccessfulResponse()) {
                if(beforeClose) {
                    //Do nothing since window is being closed after save
                    return ;
                }
                Vector vecData = responderBean.getDataObjects();
                negotiationData = (Hashtable)vecData.get(0);
                this.cvUserRights = (CoeusVector)vecData.get(1);

                //Prepare query key
                queryKey = NEGOTIATION + negotiationNumber;
                // 2812: Modify Negotiation Record Titlebar - Start
//                //Set the title to the frame and refresh the menu
//                String title = NEGOTIATION + COLON + this.negotiationInfoBean.getNegotiationNumber();
                generateNegotiationWindowTitle();
                // 2812: Modify Negotiation Record Titlebar - End
                edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
                if( windowMenu != null ){
                    windowMenu = windowMenu.removeMenuItem( negotiationBaseWindow.getTitle() );
                }
                if(negotiationBaseWindow != null) {
                    negotiationBaseWindow.setTitle(title);
                }
                windowMenu = windowMenu.addNewMenuItem( negotiationBaseWindow.getTitle(), negotiationBaseWindow );
                
                extractDataToQueryEngine(negotiationData);
                negotiationDetailController.setRefreshRequired(true);
                
                observable.setFunctionType(getFunctionType());
                
                /**Bug Fix Case Id 1864 Start2
                 */
                negotiationDetailController.setFunctionType(TypeConstants.MODIFY_MODE);
                setFunctionType(TypeConstants.MODIFY_MODE);
                
                negotiationDetailController.setUserRights(cvUserRights);
                negotiationDetailController.setNegotiationInfoBean(negotiationInfoBean);
                /**Bug Fix Case Id 1864 End 2
                 */
                
                negotiationDetailController.refresh();
                
                //Reflect changes in the Negotiation List
                Hashtable htSavedData = new Hashtable();
                CoeusVector cvInfoBean = (CoeusVector)negotiationData.get(NegotiationInfoBean.class);
                NegotiationInfoBean negotiationInfoBean = (NegotiationInfoBean)cvInfoBean.get(0);
                negotiationHeaderBean = (NegotiationHeaderBean)((CoeusVector)negotiationData.get(NegotiationHeaderBean.class)).get(0);
                //Added for COEUSDEV-294 : Error adding activity to a negotiation - Start
                //Refreshing the Actvities data when negotiation is saved
                CoeusVector cvActivitiesData = (CoeusVector)negotiationData.get(NegotiationActivitiesBean.class);
                if(cvActivitiesData == null){
                    cvActivitiesData = new CoeusVector();
                }
                htSavedData.put(NegotiationInfoBean.class, cvActivitiesData);
                //COEUSDEV-294 : End
                htSavedData.put(NegotiationInfoBean.class, negotiationInfoBean);
                htSavedData.put(NegotiationHeaderBean.class, negotiationHeaderBean);
                //COEUSDEV - 733 Create a new notification for negotiation module - Start
                //To check if the status and the parameter values are matching
                CoeusVector cvParameterValues = checkParameterValue(negotiationInfoBean.getStatusCode());
                boolean isMatching = false;
                String loggedInUser = "";
                if(cvParameterValues != null && (negotiationInfoBean.getStatusCode() != oldStatusCode)){
                    isMatching = (Boolean)cvParameterValues.get(0);
                    loggedInUser = cvParameterValues.get(1).toString();
                }
                //If values are matching then notification mail is prompted
                if((isMatching && loggedInUser!= null) && (loggedInUser.length()>0)){
                    NegotiationMailController mailController = new NegotiationMailController(loggedInUser);
                    synchronized(mailController) {
                        negotiationHeaderBean.setUserId(loggedInUser);
                        mailController.sendMail(ACTION_MAIL_NEGOTIATION, negotiationNumber ,0);
                        //100 - Negotiation Status set to complete
                    }
                }
                //COEUSDEV - 733 Create a new notification for negotiation module - End
                observable.notifyObservers(htSavedData);

            }else {
                //Server Error
                CoeusOptionPane.showErrorDialog(responderBean.getMessage());
                throw new CoeusUIException();
            }
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }       

    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    public void setFormData(Object data) {
        if(!(data instanceof NegotiationBaseBean)) {
            return ;
        }
        
        negotiationBaseBean = (NegotiationBaseBean)data;
        negotiationNumber = negotiationBaseBean.getNegotiationNumber();
        negotiationInfoBean.setNegotiationNumber(negotiationNumber);
        
        // 2812: Modify Negotiation Record Titlebar - Start
//        String title = NEGOTIATION + COLON + negotiationBaseBean.getNegotiationNumber();
        generateNegotiationWindowTitle();
        // 2812: Modify Negotiation Record Titlebar - End
        negotiationBaseWindow.setTitle(title);
        
        
        blockEvents(true);
        if(!fetchData(getFunctionType())) {
            canDisplay = false;
            return ;
        }else {
            canDisplay  = true;
            negotiationDetailController.prepareQueryKey(negotiationBaseBean);
            negotiationDetailController.setRefreshRequired(true);
            negotiationDetailController.setFormData(negotiationBaseBean);
        }
        blockEvents(false);
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        boolean validate = true;
        if( !negotiationDetailController.validate() || 
        !negotiationCustomDataController.validate() ){
            validate = false;
        }
        return validate;
    }
    
    /** Listens to window closing event.
     * @param propertyChangeEvent propertyChangeEvent
     * @throws PropertyVetoException PropertyVetoException
     */    
    public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) throws java.beans.PropertyVetoException {
        if(closed) {
            return ;
        }
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    } 
    
    /** Remove the existing frame
     */
    protected void clearOldInstance() {
        mdiForm.removeFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationBaseBean.getNegotiationNumber());
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.removeMenuItem( negotiationBaseWindow.getTitle() );
        }
    }
    
    /** Update the existing sheet with new data
     */
    protected void updateNewInstance() {
        char functionType = getFunctionType();
        if( functionType == TypeConstants.ADD_MODE ){
            functionType = TypeConstants.MODIFY_MODE;
        }
        mdiForm.putFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationBaseBean.getNegotiationNumber(), functionType, negotiationBaseWindow);
        //update to handle new window menu item to the existing Window Menu.
         edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( negotiationBaseWindow.getTitle(), negotiationBaseWindow );
            windowMenu.updateCheckBoxMenuItemState( negotiationBaseWindow.getTitle(), true );
        }
        mdiForm.refreshWindowMenu(windowMenu);
    }
    
    /** Closes the Base Window and removes the reference from MDIForm.
     * @throws PropertyVetoException PropertyVetoException
     */
    public void close() throws PropertyVetoException{
        if( getFunctionType() != TypeConstants.DISPLAY_MODE ) {
            
            //Check if data modified and display save confirmation.
            if( isSaveRequired() ) {
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    try{
                        // 3587: Multi Campus Enahncements - Start
                        boolean userHasright = checkUserHasModifyRight(negotiationNumber);
                        if(!userHasright){
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("negotiationBaseWindow_exceptionCode.1056"));     
                            throw new PropertyVetoException(EMPTY, null);
                        } else {
                            // 3587: Multi Campus Enahncements - End
                            saveNegotiation(false);
                        }
                    }catch (CoeusUIException coeusUIException) {
                        //Validation Failed
                        throw new PropertyVetoException(EMPTY, null);
                    }
                }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                    throw new PropertyVetoException(EMPTY, null);
                }else if(selection == CoeusOptionPane.SELECTION_NO && getFunctionType() != TypeConstants.ADD_MODE) {
                    if(!releaseLock()) {
                        //Could not release lock.
                        throw new PropertyVetoException(EMPTY, null);
                    }
                }
            }else {
                if(!releaseLock()) {
                    //Could not release lock.
                    throw new PropertyVetoException(EMPTY, null);
                }
            }
        }//end if display mode
        mdiForm.removeFrame(CoeusGuiConstants.NEGOTIATION_DETAILS, negotiationNumber);
        if( queryKey != null ) {
            queryEngine.removeDataCollection(queryKey);
        }
        closed = true;
        //Select next Internal Frame.
        negotiationBaseWindow.doDefaultCloseAction();
        cleanUp();
    }
    
    /** This will be called when nothing to modify and Negotiation is closed or 
     * user chooses to close negotiation without saving changes
     */
    private boolean releaseLock() {
        //Release lock
     
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(negotiationNumber);
        
        requesterBean.setFunctionType(RELEASE_LOCK);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        
        if(!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
//        else{
//            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
//            }
        //End if Not successful response
        return true;
    }
    
    /** Checks if data in QueryEngine is modifed and needs to be saved.
     * @return returns false if nothing to save, true otherwise
     */
    public boolean isSaveRequired(){

        if( closeWindow && getFunctionType() == TypeConstants.ADD_MODE ){
            return false;
        }
        
        if( negotiationDetailController.isSaveRequired() || 
        negotiationDetailController.isDataModified()){
            return true;
        }
        Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
        
        Equals eqInsert = new Equals(AC_TYPE, TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals(AC_TYPE, TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals(AC_TYPE, TypeConstants.DELETE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        
        Object key;
        CoeusVector data;
        boolean negotiationModified = false;
        try{
            while(enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                
                if(!(key instanceof Class)) {
                    continue;
                }
                
                data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                if(! negotiationModified) {
                    if(data != null && data.size() > 0) {
                        negotiationModified = true;
                        break;
                    }
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        return negotiationModified;
    }

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        blockEvents(true);
        try{
            if( source.equals(negotiationBaseWindow.mnuItmMedusa) ||
            source.equals(negotiationBaseWindow.btnMedusa )) {
                showMedusa();
            }else if( source.equals(negotiationBaseWindow.btnNext ) ){
                showNegotiation(SHOW_NEXT_NEGOTIATION);
            }else if( source.equals(negotiationBaseWindow.btnPrevious ) ){
                showNegotiation(SHOW_PREV_NEGOTIATION);
            }else if( source.equals(negotiationBaseWindow.btnSave) ||
                    source.equals(negotiationBaseWindow.mnuItmSave ) ){
                try{
                    
                    //Bug Fix 1750 Start
                    //saveNegotiation(false);
                    if( isSaveRequired() ) {
                        // 3587: Multi Campus Enahncements - Start
                        boolean userHasright = checkUserHasModifyRight(negotiationNumber);
                        if(!userHasright){
                            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("negotiationBaseWindow_exceptionCode.1056"));
                            return;
                        }
                        // 3587: Multi Campus Enahncements - End
                        saveNegotiation(false);
                        negotiationDetailController.setSaveRequired(false);
                    }
                    //Bug Fix 1750 End
                    
                }catch (CoeusUIException coeusUIException) {
                    //Validation Failed
                }
            }else if( source.equals(negotiationBaseWindow.mnuItmCustomData )){
                showCustomData();
             //start of bug fix id 1651
            } else if(source.equals(negotiationBaseWindow.mnuItmExit)) {
                exitApplication();
            }//end of bug fix id 1651
            else if( source.equals(negotiationBaseWindow.btnClose) ||
            source.equals(negotiationBaseWindow.mnuItmClose ) ){
                try{
                    if( closed ) {
                        return ;
                    }
                    close();
                }catch (PropertyVetoException propertyVetoException){
                    //Don't do anything. this exception is thrown to stop window from closing.
                }
            }else if(source.equals(negotiationBaseWindow.mnuItmPreferences)){
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(negotiationBaseWindow.mnuItmPreferences)){
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }// Case 2110 Start
            else if(source.equals(negotiationBaseWindow.mnuItmCurrentLocks)){
                showLocksForm();
            }//Case 2110 End
        }catch(CoeusException coeusException){
           coeusException.printStackTrace();
           CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
           exception.printStackTrace();
           CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            blockEvents(false);
        }
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegation window
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
    
    
    /** Displays medusa for the selected negotiation */
    private void showMedusa() {
        String propNumber = negotiationNumber;
        if(propNumber.startsWith(LETTER_T)){
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey(CANNOT_OPEN_MEDUSA_FOR_TEMP_PROP));
            return ;
        }
        try{
            MedusaDetailForm medusaDetailform = null;
            ProposalAwardHierarchyLinkBean linkBean = new ProposalAwardHierarchyLinkBean();
            linkBean.setBaseType(CoeusConstants.INST_PROP);
            linkBean.setInstituteProposalNumber(propNumber);
            if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailform.isIcon() ){
                    medusaDetailform.setIcon(false);
                }
                linkBean.setBaseType(CoeusConstants.INST_PROP);
                linkBean.setInstituteProposalNumber(propNumber);
                medusaDetailform.setSelectedNodeId(propNumber);
                medusaDetailform.setSelected( true );
                return;
            }
            medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
            medusaDetailform.setVisible(true);
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** Displays the negotiation details for the next negotiation in the Negotiation List screen
     * @param navigation holds "1" or "2", indicating Next or Previous, respectively
     */
    private void showNegotiation(int navigation) {
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(new NegotiationInfoBean());
        beanEvent.setSource(this);
        beanEvent.setMessageId(navigation);
        fireBeanUpdated(beanEvent);
    }
    
    /** Displays the Custom Data
     */
    private void showCustomData() {
        negotiationCustomDataController.display();
        negotiationCustomDataController.cleanUp();
    }
    
    /** Register observer for updating to base window 
     */
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    //start case 1735
    public void beanUpdated(BeanEvent beanEvent) {
        Controller source = beanEvent.getSource();
        if(source.getClass().equals(NegotiationDetailController.class)) {
              try{                  
                  if(  getFunctionType()  != TypeConstants.DISPLAY_MODE ){
                      if( isSaveRequired() ) {
                            saveNegotiation(false);
                            negotiationDetailController.setSaveRequired(false);
                        }
                  }
                    negotiationDetailController.printActivity((Hashtable)(beanEvent.getObject()));
              }catch (CoeusUIException coeusUIException){
                    CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
                    coeusUIException.printStackTrace();
              }catch(CoeusException coeusException){
                    CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                    coeusException.printStackTrace();
              }
        }
    }
    //end case 1735
    //Case 2110 Start
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }//Case 2110 End
    
    /** Clean all objects
     */
    public void cleanUp(){        
        negotiationDetailController.cleanUp();
        //start case 1735 
        removeBeanUpdatedListener(this, NegotiationInfoBean.class);
        negotiationDetailController = null;
        //end case 1735
    }
    // 2812: Modify Negotiation Record Titlebar - Start
    /**
     * To generate proper Negotiation title
     * @return void
     */
    public void generateNegotiationWindowTitle(){
        char functionType = getFunctionType();
        if(CoeusGuiConstants.ADD_MODE == functionType || isNewNegotiation){
            this.title = "New Negotiation";
            if(negotiationNumber != null && !negotiationNumber.equals("")){
                title = title + COLON + negotiationNumber;
            }
        }else if(CoeusGuiConstants.MODIFY_MODE == functionType){
            this.title = "Correct Negotiation" + COLON + negotiationNumber;
        }else if(CoeusGuiConstants.DISPLAY_MODE == functionType){
            this.title = "Display Negotiation" + COLON + negotiationNumber;
        }else{
            this.title = CoeusGuiConstants.NEGOTIATION_DETAILS_FRAME_TITLE + COLON + negotiationNumber;
        }
    }
    // 2812: Modify Negotiation Record Titlebar - End
    // 3587: Multi Campus Enahncements - Start
    private boolean checkUserHasModifyRight(String negotiationNumber) {
        boolean modifyRight = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CAN_MODIFY_NEGOTIATION);
        requesterBean.setDataObject(negotiationNumber);
        
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                Boolean right = (Boolean) responderBean.getDataObject();
                modifyRight = right.booleanValue();
            }
        }
        return modifyRight;
    }
    // 3587: Multi Campus Enahncements - End
    
    //COEUSDEV - 733 Create a new notification for negotiation module - Start
    /**
     * This function is used to compare the values in parameter maintenance and status of negotiation
     * @param statusCode
     * @return CoeusVector
     */
    private CoeusVector checkParameterValue(int statusCode) {
        CoeusVector vecParameters = new CoeusVector();
        RequesterBean requesterBean = new RequesterBean();
        CoeusVector vecParameter = new CoeusVector();
        requesterBean.setFunctionType(COMPARE_PARAMETER_VALUE);
        vecParameter.add(CLOSED_NEGOTIATION_STATUS);
        vecParameter.add(statusCode);
        requesterBean.setDataObject(vecParameter);
        
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                vecParameters = (CoeusVector) responderBean.getDataObject();
            }
        }
        return vecParameters;
    }
    //COEUSDEV - 733 Create a new notification for negotiation module - End
}
