

/*
 * SubcontractBaseWindowController.java
 *
 * Created on September 3, 2004, 3:23 PM
 */

package edu.mit.coeus.subcontract.controller;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */

import edu.mit.coeus.subcontract.controller.SubcontractTemplateInfoController;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import javax.swing.*;
import java.util.Hashtable;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.*;
import java.util.Enumeration;
import java.awt.*;
import javax.swing.event.ChangeListener;
import java.util.Observer;
import javax.swing.JOptionPane;

import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.subcontract.gui.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.user.gui.UserPreferencesForm;

/* JM 3-3-015 needed to check permissions */
import edu.vanderbilt.coeus.utils.UserPermissions;
/* JM END */

public class SubcontractBaseWindowController extends SubcontractController
implements ActionListener, VetoableChangeListener, ChangeListener, BeanUpdatedListener {
    
    
    private SubcontractBaseWindow subcontractBaseWindow;
    private static final char SAVE_SUBCONTRACT = 'S';
    private static final char RELEASE_LOCK = 'R';
    private static final char UNLOCK = 'U';
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private static final String AC_TYPE = "acType";
    private QueryEngine queryEngine;
    private static final String EMPTY_STRING = "";
    public CoeusTabbedPane tbdPnSubcontract;
    private char functionType;
    
    private String wintitle = EMPTY_STRING;
    private String title = EMPTY_STRING;
    public SubContractBean subContractBean;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private BaseWindowObservable observable  = new BaseWindowObservable();
    // holds data from server
    private Hashtable htSubcontractData;
    private String subcontractCode;
    private CoeusMessageResources coeusMessageResources;
    private SubContractBaseBean subContractBaseBean;
    //holds amount info data
    private CoeusVector cvOther;
    private boolean closed = false;
    // holds index of selected tab.
    private int selectedTabIndex;
    private boolean windowOpen = false;
    private javax.swing.JTable tblResult;
    private double amtReleased = 0.0;
    private char GET_SUBCONTRACT_DATA = 'A';
    private static final String NEXT = "NEXT_SUBCONTRACT";
    private static final String PREVIOUS = "PREVIOUS_SUBCONTRACT";
    private static final String FUNCTIONALITY_NOT_IMPLEMENTED = "Functionality not implemented ";
    
    //specifies the tab and tab captions for the Subcontract
    
    private static final String SUBCONTRACT_DETAIL = "Subcontract";
    private static final String FUNDING_SOURCE = "Funding Source";
    private static final String AMOUNT_INFO = "Amount Info";
    private static final String AMOUNT_RELEASED = "Amount Released";
    private static final String CONTACTS = "Contacts";
    private static final String CLOSE_OUT = "Closeout";
    // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
    private final String SUBCONTRACT_REPORTS = "Reports";
    private final String SUBCONTRACT_ATTACHMENT = "Attachment";
    private final String TEMPLATE_INFO = "Template Info";
    private static final String OTHERS = "Others";
    private static final String ATTACHMENTS = "Attachments";
    // Added for COEUSQA-1412 Subcontract Module changes - Change - End
    
    //specifies the tab and tab index for the Subcontract
    private static final int SUBCONTRACT_TAB_INDEX = 0;
    private static final int FUNDING_SOURCE_TAB_INDEX = 1;
    private static final int AMOUNT_INFO_TAB_INDEX = 2;
    private static final int AMOUNT_RELEASED_TAB_INDEX = 3;
    private static final int CONTACTS_TAB_INDEX = 4;
    private static final int CLOSE_OUT_TAB_INDEX = 5;
    // Modified for COEUSQA-1412 Subcontract Module changes - Change - Start
//    private static final int OTHERS_TAB_INDEX = 6;
    private final int SUBCONTRACT_REPORTS_TAB = 6;
    private final int SUBCONTRACT_ATTACHMENT_TAB = 8;
    private final int TEMPLATE_INFO_TAB = 7;
    private static final int OTHERS_TAB_INDEX = 9;
    // Modified for COEUSQA-1412 Subcontract Module changes - Change - End
    //holds amount data
    private CoeusVector amountInfoData;
    
    private SubcontractDetailController subcontractDetailController;
    private FundingSourceController fundingSourceController;
    private SubcontractCloseoutController subcontractCloseoutController;
    private SubcontractsContactsController subcontractsContactsController;
    private AmountInfoController amountInfoController;
    private AmountReleasedController amountReleasedController;
    
    //String used in get and putFrame to maintain same number for both methods especially
    // for new mode.
    private String referenceId;
    
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    private boolean isAward;
    
    //Case :#3149 – Tabbing between fields does not work on others tabs - Start
    private int row = 0 ;
    private int column =0;
    private JTable customTable = null;
    private int count = 1;
    private CustomElementsForm customElementsForm;
    //Case :#3149 - End
    // 3587: Multi Campus Enahncements - Start
    private static final char CHECK_USER_HAS_CREATE_RIGHT = 'L';
    private String requisitionerHomeUnit = EMPTY_STRING;
    // 3587: Multi Campus Enhancement - End
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    public static final String NO_ATTACHMENT_FOR_GENERATE_AGREEMENT = "subcontractGenerate_exceptionCode.1000";
    private SubcontractReportsController subcontractReportsController;
    private SubcontractTemplateInfoController subcontractTemplateInfoController;
    private SubcontractOtherController subcontractOtherController;
    private SubcontractAttachmentController subcontractAttachmentController;
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
    /* JM 3-3-2015 check rights */
    private final String MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
    private final String MODIFY_SUBCONTRACT_DOCUMENTS = "MODIFY_SUBCONTRACT_DOCUMENTS";
    private final String CREATE_SUBCONTRACT = "CREATE_SUBCONTRACT";
    private boolean hasModify, hasModifyDocuments, hasCreate;
    /* JM END */
    
    /** Creates a new instance of SubcontractBaseWindowController
     * @param title holds title of the window
     * @param functionType holds functiontype
     * @param SubContractBean holds subcontract information like subcontract code.
     * @param amountInfo holds amount data
     */
    
    /** Creates a new instance of SubcontractBaseWindowController */
    public SubcontractBaseWindowController(String title, char functionType,
    SubContractBean subContractBean,CoeusVector amountInfo) throws CoeusUIException{
        super(subContractBean);
        
        if(subContractBean!= null){
            this.subContractBean = subContractBean;
            subcontractCode = subContractBean.getSubContractCode();
        }
        this.wintitle = title;
        this.functionType = functionType;
        this.amountInfoData = amountInfo;
        this.referenceId = (functionType == NEW_SUBCONTRACT ? "" :subcontractCode);
        if(this.functionType == NEW_ENTRY_SUBCONTRACT) {
            GET_SUBCONTRACT_DATA = 'E';
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        tbdPnSubcontract = new CoeusTabbedPane();
        setFunctionType(functionType);
        observable.setFunctionType(functionType);
        subcontractBaseWindow = new SubcontractBaseWindow(title,mdiForm);
        fetchData(getFunctionType());
        registerComponents();
        initComponents();
        
    }
    
    public SubcontractBaseWindowController(String title, char functionType,
    SubContractBean subContractBean,CoeusVector amountInfo, boolean isAward) throws CoeusUIException{
        super(subContractBean);
        
        if(subContractBean!= null){
            this.subContractBean = subContractBean;
            subcontractCode = subContractBean.getSubContractCode();
        }
        this.isAward  = isAward;
        this.wintitle = title;
        this.functionType = functionType;
        this.amountInfoData = amountInfo;
        this.referenceId = (functionType == NEW_SUBCONTRACT ? "" :subcontractCode);
        if(this.functionType == NEW_ENTRY_SUBCONTRACT) {
            GET_SUBCONTRACT_DATA = 'E';
        }
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
        tbdPnSubcontract = new CoeusTabbedPane();
        setFunctionType(functionType);
        observable.setFunctionType(functionType);
        subcontractBaseWindow = new SubcontractBaseWindow(title,mdiForm);
        if(isAward){
            subcontractBaseWindow.btnNext.setEnabled(false);
            subcontractBaseWindow.btnPrevious.setEnabled(false);
        }
        fetchData(getFunctionType());
        registerComponents();
        initComponents();
        
    }
    
    /* JM 3-3-2015 check user permissions */
    private boolean checkUserHasRight(String rightId) {
    	UserPermissions userPermissions = new UserPermissions(mdiForm.getUserId());
    	boolean hasRight = false;
		try {
			hasRight = userPermissions.hasRightInUnit(rightId,requisitionerHomeUnit);
		} catch (CoeusClientException e) {
			System.out.println("Cannot retrieve user permissions");
		}
		
		//System.out.println("user " + mdiForm.getUserId() + " has " + rightId + " in unit " + requisitionerHomeUnit + "? " + hasRight);
		
    	return hasRight;
    }
    
    private boolean checkUserHasCreateRight() {
    	UserPermissions userPermissions = new UserPermissions(mdiForm.getUserId());
    	boolean hasCreate = false;
		try {
			hasCreate = userPermissions.hasRight(CREATE_SUBCONTRACT);
		} catch (CoeusClientException e) {
			System.out.println("Cannot retrieve user permissions");
		}
    	return hasCreate;
    }
    /* JM END */
    
    /** Setter for property setAmounts.
     * @param data New value of property setAmounts.
     */
    public void setAmounts(CoeusVector data) {
        amountInfoData = data;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            blockEvents(true);
            if(source.equals(subcontractBaseWindow.btnClose) ||
            source.equals(subcontractBaseWindow.mnuItmClose)){
                subcontractBaseWindow.doDefaultCloseAction();
                
            }else if(source.equals(subcontractBaseWindow.btnSave) ||
            source.equals(subcontractBaseWindow.mnuItmSave)){
                //Case :#3149 – Tabbing between fields does not work on others tabs - Start    
                if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX && getFunctionType() != DISPLAY_SUBCONTRACT && subcontractOtherController != null){
                    customElementsForm =subcontractOtherController.getCustomElementsForm();
                    customElementsForm.setSaveAction(true);
                }
                //Case ;#3149 - End
                if(validate() && isSaveRequired()) {
                    try {
                        //Case :#3149 – Tabbing between fields does not work on others tabs - Start    
                        if(customElementsForm != null && getFunctionType() != DISPLAY_SUBCONTRACT && subcontractOtherController != null){
                            customElementsForm =subcontractOtherController.getCustomElementsForm();
                            JTable customTable = customElementsForm.getTable();
                            row = customElementsForm.getRow();
                            column = customElementsForm.getColumn();
                        }
                        //Case :#3149 - End
                        saveSubcontract();
                    }catch(CoeusUIException coeusUIException) {
                        if(!coeusUIException.getMessage().equals("null.")) {
                            CoeusOptionPane.showDialog(coeusUIException);
                            tbdPnSubcontract.setSelectedIndex(coeusUIException.getTabIndex());
                        }
                        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                        if(subcontractOtherController != null && getFunctionType() != DISPLAY_SUBCONTRACT){
                            customElementsForm =subcontractOtherController.getCustomElementsForm();
                            customTable = customElementsForm.getTable();
                            if(customElementsForm.getOtherTabMandatory()){
                                boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                                row = customElementsForm.getmandatoryRow();
                                column = customElementsForm.getmandatoryColumn();
                                if(lookUpAvailable[row]){
                                    customTable.editCellAt(row,column+1);
                                    customTable.setRowSelectionInterval(row,row);
                                    customTable.setColumnSelectionInterval(column+1,column+1);
                                    
                                }else{
                                    customTable.editCellAt(row,column);
                                    customTable.setRowSelectionInterval(row,row);
                                    customTable.setColumnSelectionInterval(column,column);
                                }
                            }
                            customTable.setEnabled(true);
                            //Case :#3149 - End
                        }
                    }
                }
                //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                if(subcontractOtherController != null && getFunctionType() != DISPLAY_SUBCONTRACT){
                    CustomElementsForm customElementsForm = subcontractOtherController.getCustomElementsForm();
                    customTable = customElementsForm.getTable();
                    if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX && !customElementsForm.getOtherTabMandatory() ) {
                        boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                        row = customElementsForm.getRow();
                        column = customElementsForm.getColumn();
                        if(lookUpAvailable[row]){
                            customTable.editCellAt(row,column);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column,column);
                            
                        }else{
                            customTable.editCellAt(row,column);
                            customTable.setRowSelectionInterval(row,row);
                            customTable.setColumnSelectionInterval(column,column);
                        }
                    }
                    customTable.setEnabled(true);
                }
                //Case :#3149 - End
            }else if((source.equals(subcontractBaseWindow.btnNext)) ||
            source.equals(subcontractBaseWindow.mnuItmNext)) {
                showSubcontract(NEXT);
            }else if((source.equals(subcontractBaseWindow.btnPrevious)) ||
            source.equals(subcontractBaseWindow.mnuItmPrevious)) {
                showSubcontract(PREVIOUS);
            }else if(source.equals(subcontractBaseWindow.mnuItmSelectRequisitioner)){
                subcontractDetailController.displayPersonSearch();
            }else if(source.equals(subcontractBaseWindow.mnuItmSelectSubcontractor)){
                subcontractDetailController.displayOrganizationSearch();
            }else if(source.equals(subcontractBaseWindow.mnuItmChangePassword)) {
                showChangePassword();
            }else if(source.equals(subcontractBaseWindow.mnuItmInbox)) {
                showInboxDetails();
            }else if((source.equals(subcontractBaseWindow.mnuItmPrint)) ||
            source.equals(subcontractBaseWindow.btnPrint)) {
                showPrint();
            // Added for COEUSQA-1412 Subcontract Module changes - Start                
            }else if((source.equals(subcontractBaseWindow.mnuItmGenerateAgreeModifaction))) {
                if(validate()){
                    saveFormData();
                    generateAgreementModification();
                }
            // Added for COEUSQA-1412 Subcontract Module changes - Start
            }else if(source.equals(subcontractBaseWindow.mnuItmExit)) {
                exitApplication();
                
            }else if(source.equals(subcontractBaseWindow.mnuItmPreferences)){
                showPreference();
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(subcontractBaseWindow.mnuItmDelegations)){
                displayUserDelegation();
            //Added for Case#3682 - Enhancements related to Delegations - End
            }//Case 2110 Start
            else if(source.equals(subcontractBaseWindow.mnuItmCurrentLocks)){
                showLocksForm();
            }//Case 2110 End
            else {
                CoeusOptionPane.showInfoDialog("Functionality not implemented");
            }
        }catch (Exception exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
        blockEvents(false);
    }
    
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
    
    /**
     * Method used to close the application after confirmation.
     */
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
    }
    /**
     * This method is to show the Subcontract Forms screen which will display the RTF forms.
     */
    private void showPrint() throws Exception{
        SubcontractFormsController subcontractFormsController =
        new SubcontractFormsController(subContractBaseBean);
        //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -Start
        subcontractFormsController.setAmtReleasedLineItemNumber(
            amountReleasedController.getAmtReleasedLineItemNumber());
        //Added  for  Case #3338 - Add new elements the person and rolodex details to Subcontract schema -End
        subcontractFormsController.display();
    }
    
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    /**
     * Method to generate agreement / modification
     * @throws java.lang.Exception 
     */
    private void generateAgreementModification() throws Exception{
        CoeusVector cvFundingSource = queryEngine.getDetails(queryKey,SubContractFundingSourceBean.class);
        if(cvFundingSource == null || cvFundingSource.isEmpty() || cvFundingSource.size() == 1){
            SubContractFundingSourceBean subContractFundingSourceBean = null;
            if(cvFundingSource != null && !cvFundingSource.isEmpty()){
                subContractFundingSourceBean = (SubContractFundingSourceBean)cvFundingSource.get(0);
            }
            SubcontractGenerateAgreementModification subcontractGenerateAgreementModification
                    = new SubcontractGenerateAgreementModification(subContractBean,subContractFundingSourceBean);
            if(subcontractGenerateAgreementModification.isAttachmentExists()){
                subcontractGenerateAgreementModification.display();
            }else{
                 CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(NO_ATTACHMENT_FOR_GENERATE_AGREEMENT));
            }
        }else{
            FundingSourceController fundingSourceController = new FundingSourceController(subContractBean, TypeConstants.DISPLAY_MODE,true);
            fundingSourceController.displayFundingSourceForGenerateAgreement();
            fundingSourceController = null;
        }
    }
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
    public void performNavigation(String newSubcontractCode){
        this.subcontractCode = newSubcontractCode;
        this.subContractBean.setSubContractCode(newSubcontractCode);
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        setFormData(subContractBean);
        referenceId = newSubcontractCode;
        mdiForm.putFrame(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, referenceId,functionType,subcontractBaseWindow);
        //update to handle new window menu item to the existing Window Menu.
        if( windowMenu != null ){
            windowMenu = windowMenu.addNewMenuItem( title, subcontractBaseWindow );
            windowMenu.updateCheckBoxMenuItemState( title, true );
        }
        mdiForm.refreshWindowMenu(windowMenu);
    }
    
    /** Displays the Subcontract details for the next/prev subcontract in the
     * Subcontract List depending on the navigation value
     * @param navigation holds 1 or 2, indicating Previous or Next, respectively
     */
    
    public void showSubcontract(String navigation) {
        observable.notifyObservers(navigation);
    }
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        int getSelectedTab = tbdPnSubcontract.getSelectedIndex();
        if(getFunctionType() != DISPLAY_SUBCONTRACT) {
            switch(getSelectedTab) {
                case SUBCONTRACT_TAB_INDEX:
                    subcontractDetailController.saveFormData();
                    break;
                case FUNDING_SOURCE_TAB_INDEX:
                    fundingSourceController.saveFormData();
                    break;
                case AMOUNT_INFO_TAB_INDEX:
                    try {
                        amountInfoController.saveFormData();
                    }catch(CoeusException coeusException) {
                        coeusException.printStackTrace();
                    }
                    break;
                case AMOUNT_RELEASED_TAB_INDEX:
                    amountReleasedController.saveFormData();
                    break;
                case CONTACTS_TAB_INDEX:
                    subcontractsContactsController.saveFormData();
                    break;
                case CLOSE_OUT_TAB_INDEX:
                    subcontractCloseoutController.saveFormData();
                    break;
                case SUBCONTRACT_REPORTS_TAB:
                    subcontractReportsController.saveFormData();
                    break;
                case TEMPLATE_INFO_TAB:
                    subcontractTemplateInfoController.saveFormData();
                    break;
                case OTHERS_TAB_INDEX:
                    try {
                        if(subcontractOtherController != null) {
                            subcontractOtherController.saveFormData();
                        }
                        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                        if(getFunctionType() != DISPLAY_SUBCONTRACT && subcontractOtherController != null){
                            subcontractOtherController.getCustomElementsForm().setTabFocus();
                            if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX && count != 0 && subcontractOtherController != null){
                                subcontractOtherController.getCustomElementsForm().setTableFocus();
                            }
                            tbdPnSubcontract.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent e) {
                                    if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX)
                                        count = 1;
                                    subcontractOtherController.getCustomElementsForm().setTableFocus();
                                }
                            });
                        }
                        //Case :#3149 -  End
                    }catch(CoeusException coeusException) {
                        coeusException.printStackTrace();
                    }
                    break;
            }
            
        }
    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /**
     * initialize controller componentes.
     */
    private void initComponents() {
        subcontractBaseWindow.setFunctionType(getFunctionType());
        Container subcontractBaseContainer = subcontractBaseWindow.getContentPane();
        
        //If in display Mode disable Save
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            subcontractBaseWindow.btnSave.setEnabled(false);
            subcontractBaseWindow.mnuItmSave.setEnabled(false);
        }else {
            subcontractBaseWindow.btnPrevious.setEnabled(false);
            subcontractBaseWindow.btnNext.setEnabled(false);
            subcontractBaseWindow.mnuItmPrevious.setEnabled(false);
            subcontractBaseWindow.mnuItmNext.setEnabled(false);
        }
        
        JPanel basePanel = new JPanel();
        
        basePanel.setLayout(new BorderLayout());
        
        if(subcontractDetailController == null){
            subcontractDetailController =
            new SubcontractDetailController(subContractBean,getFunctionType());
            subcontractDetailController.setAmounts(amountInfoData);
            
        }
        if(fundingSourceController == null){
            // Modified for COEUSQA-1412 Subcontract Module changes - Start
//            fundingSourceController =
//            new FundingSourceController(subContractBean,getFunctionType());
            fundingSourceController =
            new FundingSourceController(subContractBean,getFunctionType(),false);
            // Modified for COEUSQA-1412 Subcontract Module changes - End
        }
        if(subcontractCloseoutController == null){
            subcontractCloseoutController =
            new SubcontractCloseoutController(subContractBean,getFunctionType());
        }
        if(subcontractsContactsController == null){
            subcontractsContactsController =
            new SubcontractsContactsController(subContractBean,getFunctionType());
        }
        if(amountInfoController == null){
            amountInfoController =
            new AmountInfoController(subContractBean,getFunctionType());
            amountInfoController.setAmounts(amountInfoData);
        }
        if(amountReleasedController == null) {
            amountReleasedController = new AmountReleasedController(subContractBean,getFunctionType());
            amountReleasedController.setAmounts(amountInfoData);
            //Code added for princeton enhancement case#2802
            amountReleasedController.setSubcontractBaseWindowController(this);
        }
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
        if(subcontractReportsController == null){
            subcontractReportsController =
                    new SubcontractReportsController(subContractBean,getFunctionType());
        }
        
        if(subcontractTemplateInfoController == null){
            subcontractTemplateInfoController =
                    new SubcontractTemplateInfoController(subContractBean,getFunctionType());
        }
        
        // Added for COEUSQA-1412 Subcontract Module changes - Change - End
        try {
            cvOther = queryEngine.executeQuery(queryKey, SubContractCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvOther != null && cvOther.size() > 0) {
                if(subcontractOtherController == null)
                    subcontractOtherController = new SubcontractOtherController(subContractBean,getFunctionType());
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
        if(subcontractAttachmentController == null){
            subcontractAttachmentController = new SubcontractAttachmentController(subContractBean,mdiForm,getFunctionType());
        }
        
        tbdPnSubcontract.addTab(SUBCONTRACT_DETAIL, subcontractDetailController.getControlledUI());
        tbdPnSubcontract.addTab(FUNDING_SOURCE, fundingSourceController.getControlledUI());
        tbdPnSubcontract.addTab(AMOUNT_INFO, amountInfoController.getControlledUI());
        tbdPnSubcontract.addTab(AMOUNT_RELEASED, amountReleasedController.getControlledUI());
        tbdPnSubcontract.addTab(CONTACTS, subcontractsContactsController.getControlledUI());
        tbdPnSubcontract.addTab(CLOSE_OUT, subcontractCloseoutController.getControlledUI());
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
        tbdPnSubcontract.addTab(SUBCONTRACT_REPORTS, subcontractReportsController.getControlledUI());
        tbdPnSubcontract.addTab(TEMPLATE_INFO, subcontractTemplateInfoController.getControlledUI());
        tbdPnSubcontract.addTab(SUBCONTRACT_ATTACHMENT, subcontractAttachmentController.getControlledUI());
        // Added for COEUSQA-1412 Subcontract Module changes - Change - End
        
        if(subcontractOtherController != null) {
            tbdPnSubcontract.addTab(OTHERS,subcontractOtherController.getControlledUI());
        }
        
        basePanel.add(tbdPnSubcontract);
        JScrollPane jScrollPane = new JScrollPane(basePanel);
        subcontractBaseContainer.add(jScrollPane);
        
        
    }
    /**
     * displays thie internal frame which is controlled by this controller.
     */
    public void display() {
        try{
            char functionType = getFunctionType();
            String subcontractCode = EMPTY_STRING;
            if( functionType != TypeConstants.DISPLAY_MODE ){
                functionType = TypeConstants.MODIFY_MODE;
            }else {
                functionType = TypeConstants.DISPLAY_MODE;
            }
            
            subcontractCode = subContractBean.getSubContractCode();
            if( mdiForm.getFrame(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, subcontractCode) == null ){
                mdiForm.putFrame(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, subcontractCode, functionType, subcontractBaseWindow);
                mdiForm.getDeskTopPane().add(subcontractBaseWindow);
                subcontractBaseWindow.setSelected(true);
                subcontractBaseWindow.setVisible(true);
            }
            
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return subcontractBaseWindow;
    }
    
    public Object getFormData() {
        return null;
    }
    
    /**
     * cleans up all the instance objects.
     */
    public void cleanUp(){
        subContractBean = null;
        coeusMessageResources = null;
        queryEngine = null;
        subContractBaseBean = null;
        //For bug fix id 1651: start
       // mdiForm = null; 
        //For bug fix id 1651: end
        htSubcontractData = null;
        observable = null;
        amountInfoData = null;
        tbdPnSubcontract = null;
        tblResult = null;
        cvOther = null;
        subcontractCloseoutController.cleanUp();
        subcontractDetailController.cleanUp();
        amountInfoController.cleanUp();
        amountReleasedController.cleanUp();
        subcontractsContactsController.cleanUp();
        fundingSourceController.cleanUp();
        if(subcontractOtherController != null ) {
            subcontractOtherController.cleanUp();
            subcontractOtherController = null;
        }
        subcontractDetailController = null;
        subcontractCloseoutController = null;
        amountInfoController = null;
        amountReleasedController = null;
        subcontractsContactsController = null;
        fundingSourceController = null;
        subcontractBaseWindow = null;
        removeBeanUpdatedListener(this, SubContractAmountInfoBean.class);
        removeBeanUpdatedListener(this, SubContractAmountReleased.class);
    }
    
    /** *
     * registers the components with event listeners.
     */
    
    public void registerComponents() {
        subcontractBaseWindow.addVetoableChangeListener(this);
        subcontractBaseWindow.btnSave.addActionListener(this);
        subcontractBaseWindow.mnuItmSave.addActionListener(this);
        subcontractBaseWindow.btnClose.addActionListener(this);
        subcontractBaseWindow.mnuItmClose.addActionListener(this);
        subcontractBaseWindow.mnuItmPrevious.addActionListener(this);
        subcontractBaseWindow.btnPrevious.addActionListener(this);
        subcontractBaseWindow.mnuItmNext.addActionListener(this);
        subcontractBaseWindow.btnNext.addActionListener(this);
        subcontractBaseWindow.btnPrint.addActionListener(this);
        subcontractBaseWindow.mnuItmPrint.addActionListener(this);
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        subcontractBaseWindow.mnuItmGenerateAgreeModifaction.addActionListener(this);
        // Added for COEUSQA-1412 Subcontract Module changes - End
        subcontractBaseWindow.mnuItmInbox.addActionListener(this);
        subcontractBaseWindow.mnuItmSelectRequisitioner.addActionListener(this);
        subcontractBaseWindow.mnuItmSelectSubcontractor.addActionListener(this);
        subcontractBaseWindow.mnuItmPreferences.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        subcontractBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        subcontractBaseWindow.mnuItmChangePassword.addActionListener(this);
        subcontractBaseWindow.mnuItmExit.addActionListener(this);
        //Case 2110 Start
        subcontractBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        addBeanUpdatedListener(this, SubContractAmountInfoBean.class);
        addBeanUpdatedListener(this, SubContractAmountReleased.class);
        tbdPnSubcontract.addChangeListener(this);
        
    }
    
    /** sends Subcontract data to server to be saved.
     * @throws CoeusUIException if any exception occurs.
     */
    public void saveSubcontract() throws CoeusUIException{
        
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        if(getFunctionType() != DISPLAY_SUBCONTRACT && subcontractOtherController!= null){
            customTable = subcontractOtherController.getCustomElementsForm().getTable();
            if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX ) {
                row = customTable.getSelectedRow();
                column = customTable.getSelectedColumn();
                count = 0;
            }
        }
      //case :#3149 - End
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            return ;
        }
        try{
            
          
            
            Hashtable subcontractData = new Hashtable();
            CoeusVector cvData = new CoeusVector();
            cvData = queryEngine.getDetails(queryKey, SubContractBean.class);
            SubContractBean saveSubContractBean = (SubContractBean)cvData.get(0);
            if(getFunctionType() == CORRECT_SUBCONTRACT) {
                if(saveSubContractBean.getAcType() == null)
                    saveSubContractBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
            if(getFunctionType() == NEW_SUBCONTRACT || getFunctionType() == NEW_ENTRY_SUBCONTRACT) {
                saveSubContractBean.setAcType(TypeConstants.INSERT_RECORD);
            }
            // 3587: Multi Campus Enahncements - Start
            //Modified for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
//          if(requisitionerHomeUnit != null && saveSubContractBean.getRequisitionerUnit() != null &&
//                  !"".equals(saveSubContractBean.getRequisitionerUnit()) && !requisitionerHomeUnit.equals(saveSubContractBean.getRequisitionerUnit())){
            if((requisitionerHomeUnit != null && !requisitionerHomeUnit.equals(saveSubContractBean.getRequisitionerUnit())) || (saveSubContractBean.getRequisitionerUnit() != null &&
                    !"".equals(saveSubContractBean.getRequisitionerUnit()))){
                /* JM 3-9-2015 check if user had modify documents also */
                boolean hasCreateRight = checkUserHasCreateRight(saveSubContractBean.getRequisitionerUnit());
            	boolean hasModifyDocuments = checkUserHasRight(MODIFY_SUBCONTRACT_DOCUMENTS);
                if(!hasCreateRight && !hasModifyDocuments){
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey("subcontractBasewindow_exceptionCode.1006"));
                    //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                    // return;
                    // In order to retain the window after validation message popup, thrown the exception
                    throw new CoeusUIException();
                    //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end 
                }
            }
            /* JM END */
            // 3587: Multi Campus Enahncements - End
            subcontractData.put(SubContractBean.class,saveSubContractBean);
            cvData = queryEngine.getDetails(queryKey, SubcontractContactDetailsBean.class);
            cvData.sort("acType");
            subcontractData.put(SubcontractContactDetailsBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubcontractCloseoutBean.class);
            cvData.sort("acType");
            subcontractData.put(SubcontractCloseoutBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey, SubContractFundingSourceBean.class);
            subcontractData.put(SubContractFundingSourceBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubContractAmountInfoBean.class);
            subcontractData.put(SubContractAmountInfoBean.class, cvData);
            cvData = queryEngine.getDetails(queryKey,SubContractAmountReleased.class);
            subcontractData.put(SubContractAmountReleased.class, cvData);
            if(subcontractOtherController != null) {
                cvData = queryEngine.getDetails(queryKey,SubContractCustomDataBean.class);
                subcontractData.put(SubContractCustomDataBean.class, cvData);
            }
            cvData = queryEngine.getDetails(queryKey,SubcontractReportBean.class);
            cvData.sort("acType");
            subcontractData.put(SubcontractReportBean.class, cvData);

            cvData = queryEngine.getDetails(queryKey,SubcontractTemplateInfoBean.class);
            subcontractData.put(SubcontractTemplateInfoBean.class, cvData);

            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(windowOpen == true ? RELEASE_LOCK : SAVE_SUBCONTRACT);
            requesterBean.setDataObject(subcontractData);
            
            AppletServletCommunicator appletServletCommunicator = new
            AppletServletCommunicator(connect, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            
            if(responderBean != null) {
                
                if(responderBean.isSuccessfulResponse()) {
                    subcontractData = (Hashtable)responderBean.getDataObject();
                    extractSubcontractToQueryEngine(subcontractData);
                    observable.setFunctionType(getFunctionType());
                    observable.notifyObservers(subcontractData);
                    if(!windowOpen) {
                        setFunctionType(CORRECT_SUBCONTRACT);
                        refresh();
                    }
                }else {
                    //Server Error
                    CoeusOptionPane.showErrorDialog(responderBean.getMessage());
                    throw new CoeusUIException();
                }
            }
            
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        //Case :#3149 – Tabbing between fields does not work on others tabs - Start
        if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX && getFunctionType() != DISPLAY_SUBCONTRACT && subcontractOtherController != null) {
            //Modified to fix Subcontract Custom data error - Start
//            boolean[] lookUpAvailable = subcontractOtherController.getCustomElementsForm().getLookUpAvailable();
//            row = subcontractOtherController.getCustomElementsForm().getRow();
//            column = subcontractOtherController.getCustomElementsForm().getColumn();
            customElementsForm = subcontractOtherController.getCustomElementsForm();
            boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
            row = customElementsForm.getRow();
            column = customElementsForm.getColumn();
            customTable = customElementsForm.getTable();
            //Modified to fix Subcontract Custom data error - End
            count = 0;
            if(lookUpAvailable[row]){
                customTable.editCellAt(row,column);
                customTable.setRowSelectionInterval(row,row);
                customTable.setColumnSelectionInterval(column,column);
                
            }else{
                customTable.editCellAt(row,column);
                customTable.setRowSelectionInterval(row,row);
                customTable.setColumnSelectionInterval(column,column);
            }
            customTable.setEnabled(true);
        }
        //Case :#3149 - End
    }
    
    /**
     * saves all the tab page data to the query engine.
     */
    
    public void saveFormData() {
        subcontractDetailController.saveFormData();
        subcontractsContactsController.saveFormData();
        subcontractCloseoutController.saveFormData();
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
        subcontractReportsController.saveFormData();
        subcontractTemplateInfoController.saveFormData();
        // Added for COEUSQA-1412 Subcontract Module changes - Change - End
        fundingSourceController.saveFormData();
        amountReleasedController.saveFormData();
        try{
            amountInfoController.saveFormData();
            if(subcontractOtherController != null) {
                subcontractOtherController.saveFormData();
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        
    }
    
    /**
     * resets all forms with new data
     * @returns void
     */
    public void refresh() {
        subcontractCloseoutController.setRefreshRequired(true);
        subcontractCloseoutController.refresh();
        subcontractDetailController.setRefreshRequired(true);
        subcontractDetailController.refresh();
        subcontractsContactsController.setRefreshRequired(true);
        subcontractsContactsController.refresh();
        amountInfoController.setRefreshRequired(true);
        amountInfoController.refresh();
        amountReleasedController.setRefreshRequired(true);
        amountReleasedController.refresh();
        fundingSourceController.setRefreshRequired(true);
        fundingSourceController.refresh();
        // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
        subcontractReportsController.setRefreshRequired(true);
        subcontractReportsController.refresh();
        subcontractTemplateInfoController.setRefreshRequired(true);
        subcontractTemplateInfoController.refresh();
        subcontractAttachmentController.setFunctionType(getFunctionType());
        // Added for COEUSQA-1412 Subcontract Module changes - Change - End
        if(subcontractOtherController != null) {
            subcontractOtherController.setRefreshRequired(true);
            subcontractOtherController.refresh();
        }
        
    }
    
    /** Register observer for updating to base window
     */
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    /**
     *Unregister observer
     */
    public void removeObserver( Observer observer ) {
        observable.deleteObserver( observer );
    }
    /**
     * gets the subcontract data from the server
     * @returns true if successful else false
     */
    private void fetchData(char functionType) throws CoeusUIException{
        /* JM 3-3-2015 check for create permission */
    	hasCreate = checkUserHasCreateRight();
    	/* JM END */
    	
        RequesterBean requesterBean = new RequesterBean();
        if(functionType != DISPLAY_SUBCONTRACT) {
            requesterBean.setFunctionType(functionType==NEW_SUBCONTRACT ? NEW_SUBCONTRACT : GET_SUBCONTRACT_DATA);
        }else{
            requesterBean.setFunctionType('K');
        }
        
        SubContractBaseBean subcontractBeanToServ = new SubContractBaseBean();
        if(subContractBean != null) {
            subcontractBeanToServ.setSubContractCode(subContractBean.getSubContractCode());
            subcontractBeanToServ.setSequenceNumber(subContractBean.getSequenceNumber());
            /* JM 3-3-2015 check and set permissions that need a unit based on current version of bean */
            requisitionerHomeUnit = subContractBean.getRequisitionerUnit();
        	hasModify = checkUserHasRight(MODIFY_SUBCONTRACT);
        	hasModifyDocuments = checkUserHasRight(MODIFY_SUBCONTRACT_DOCUMENTS);
            subContractBean.setHasModify(hasModify);
            subContractBean.setHasModifyDocuments(hasModifyDocuments);
            subContractBean.setHasCreate(hasCreate);
        	/* JM END */
        }
        
        
        requesterBean.setDataObject(subcontractBeanToServ);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + GET_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        
        
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean != null) {
            if(responderBean.isSuccessfulResponse()) {
                Hashtable subcontractData = (Hashtable)responderBean.getDataObject();
                subContractBean = (SubContractBean)subcontractData.get(SubContractBean.class);
                this.subContractBaseBean = (SubContractBaseBean)subContractBean;
                // 3587: Multi Campus Enhancement 
                requisitionerHomeUnit = subContractBean.getRequisitionerUnit();
                
                /* JM 3-3-2015 set permissions in new bean */
            	hasModify = checkUserHasRight(MODIFY_SUBCONTRACT);
            	hasModifyDocuments = checkUserHasRight(MODIFY_SUBCONTRACT_DOCUMENTS);
                subContractBean.setHasModify(hasModify);
                subContractBean.setHasModifyDocuments(hasModifyDocuments);
                subContractBean.setHasCreate(hasCreate);
            	/* JM END */
                
                if(functionType == NEW_SUBCONTRACT /*|| functionType == NEW_ENTRY_SUBCONTRACT*/) {
                    this.subContractBaseBean.setSequenceNumber(1);
                    this.subContractBean.setSequenceNumber(1);
                }
                
                queryKey = this.subContractBaseBean.getSubContractCode() + this.subContractBaseBean.getSequenceNumber();
                //Set title.
                title = "";
                title = wintitle + this.subContractBaseBean.getSubContractCode() +": Sequence : "+this.subContractBaseBean.getSequenceNumber();
                if(subcontractBaseWindow != null) {
                    subcontractBaseWindow.setTitle(title);
                }
                extractSubcontractToQueryEngine(subcontractData);
            }else {
                //Server Error
                throw new CoeusUIException(responderBean.getMessage(),CoeusUIException.ERROR_MESSAGE);
            }
        }//End for checking null responder bean
        
    }
    
    /**
     * validate all the tab page data throws CoeusUIException.
     */
    public boolean validate() throws CoeusUIException {
        boolean validTab = true;
        
        if(!subcontractDetailController.validate()) {
            tbdPnSubcontract.setSelectedIndex(SUBCONTRACT_TAB_INDEX);
            validTab = false;
        }else if(!subcontractCloseoutController.validate()) {
            tbdPnSubcontract.setSelectedIndex(CLOSE_OUT_TAB_INDEX);
            validTab = false;
        }else if(!subcontractsContactsController.validate()) {
            tbdPnSubcontract.setSelectedIndex(CONTACTS_TAB_INDEX);
            validTab = false;
        }else if(!amountInfoController.validate()) {
            tbdPnSubcontract.setSelectedIndex(AMOUNT_INFO_TAB_INDEX);
            validTab = false;
        }else if(!amountReleasedController.validate()) {
            tbdPnSubcontract.setSelectedIndex(AMOUNT_RELEASED_TAB_INDEX);
            validTab = false;
        }else if(!fundingSourceController.validate()) {
            tbdPnSubcontract.setSelectedIndex(FUNDING_SOURCE_TAB_INDEX);
            validTab = false;
        }else if(!subcontractTemplateInfoController.validate()) {
                tbdPnSubcontract.setSelectedIndex(TEMPLATE_INFO_TAB);
                validTab = false;
            
        }else if(subcontractOtherController != null) {
            if(!subcontractOtherController.validate()) {
                tbdPnSubcontract.setSelectedIndex(OTHERS_TAB_INDEX);
                validTab = false;
            }
        }
        else
            validTab = true;
        return validTab;
    }
    
    /**
     * sets the form data.
     * @param data form data
     */
    public void setFormData(Object data) {
        this.subContractBean = (SubContractBean)data;
        this.subContractBean.setSubContractCode(subcontractCode);
        try{
            fetchData(getFunctionType());
        }catch(CoeusUIException coeusUIException) {
            coeusUIException.printStackTrace();
        }
        subcontractDetailController.prepareQueryKey(subContractBean);
        fundingSourceController.prepareQueryKey(subContractBean);
        amountInfoController.prepareQueryKey(subContractBean);
        amountReleasedController.prepareQueryKey(subContractBean);
        subcontractsContactsController.prepareQueryKey(subContractBean);
        subcontractCloseoutController.prepareQueryKey(subContractBean);
        try {
            cvOther = queryEngine.executeQuery(queryKey, SubContractCustomDataBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            if(cvOther != null && cvOther.size() >0) {
                if(subcontractOtherController == null) {
                    subcontractOtherController = new SubcontractOtherController(subContractBean,getFunctionType());
                    tbdPnSubcontract.addTab(OTHERS,subcontractOtherController.getControlledUI());
                }else{
                    subcontractOtherController.prepareQueryKey(subContractBean);
                }
                subcontractOtherController.setFormData(subContractBean);
            }
        }catch(CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        subcontractDetailController.setFormData(subContractBean);
        subcontractDetailController.setAmounts(amountInfoData);
        amountInfoController.setFormData(subContractBean);
        amountInfoController.setAmounts(amountInfoData);
        amountReleasedController.setFormData(subContractBean);
        fundingSourceController.setFormData(subContractBean);
        subcontractCloseoutController.setFormData(subContractBean);
        subcontractsContactsController.setFormData(subContractBean);
        // 3587: Multi Campus Enhancement 
        requisitionerHomeUnit = subContractBean.getRequisitionerUnit();
    }
    
    /** closes the Base Window and removes the reference from MDIForm.
     * @throws PropertyVetoException PropertyVetoException
     */
    
    public void close() throws PropertyVetoException {
        
        if(getFunctionType() != CoeusGuiConstants.DISPLAY_MODE) {
            try {
                if(validate() && isSaveRequired()){
                    int optionSelected = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("subcontractBasewindow_exceptionCode.1004"), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                    if(optionSelected == CoeusOptionPane.SELECTION_YES) {
                        try{
                            windowOpen = true;
                            saveSubcontract();
                            
                        }catch(CoeusUIException coeusUIException) {
                            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-start
                            //if we set windowOpen = true,then lock is deleted so when any validation fails or exception occurs then lock is not released.
                            windowOpen = false;
                            //Addeed for the case# COEUSQA-1522 - Subcontract module: Ability to Change Lead Unit for the Requisitioner-end
                            throw new PropertyVetoException(EMPTY_STRING, null);
                        }
                    }else if(optionSelected == CoeusOptionPane.SELECTION_NO ) {
                        if(getFunctionType() != NEW_SUBCONTRACT)  {
                            if(!releaseLock()) {
                                //Could not release lock.
                                throw new PropertyVetoException(EMPTY_STRING, null);
                            }
                        }
                        
                    }else if(optionSelected == CoeusOptionPane.SELECTION_CANCEL){
                         //Case :#3149 – Tabbing between fields does not work on others tabs - Start
                        if(tbdPnSubcontract.getSelectedIndex() == OTHERS_TAB_INDEX && subcontractOtherController != null && getFunctionType() != DISPLAY_SUBCONTRACT) {
                            CustomElementsForm customElementsForm = subcontractOtherController.getCustomElementsForm();
                            boolean[] lookUpAvailable = customElementsForm.getLookUpAvailable();
                            customTable = customElementsForm.getTable();
                            row = customElementsForm.getRow();
                            column = customElementsForm.getColumn();
                            if(lookUpAvailable[row]){
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                                
                            }else{
                                customTable.editCellAt(row,column);
                                customTable.setRowSelectionInterval(row,row);
                                customTable.setColumnSelectionInterval(column,column);
                            }
                            customTable = customElementsForm.getTable();
                        }
                        //Case :#3149 - End
                        throw new PropertyVetoException(EMPTY_STRING, null);
                    }
                }else {
                    releaseLock();
                }
            } catch (CoeusUIException ex) {
                ex.printStackTrace();
            } catch (PropertyVetoException ex) {
                ex.printStackTrace();
            }
        }
        mdiForm.removeFrame(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, subContractBaseBean.getSubContractCode());
        queryEngine.removeDataCollection(queryKey);
        closed = true;
        cleanUp();
    }
    
    /**
     * add subcontract data to QueryEngine.
     */
    
    private void extractSubcontractToQueryEngine(Hashtable subcontractData) {
        
        subContractBean = (SubContractBean)subcontractData.get(SubContractBean.class);
        subcontractData.remove(SubContractBean.class);
        
        if(subContractBean != null) {
            CoeusVector cvSubcontractDetails = new CoeusVector();
            cvSubcontractDetails.add(subContractBean);
            subcontractData.put(SubContractBean.class, cvSubcontractDetails);
        }
        // Case 3566: Amount Available in Subcontract - Anomalous Result
//        if(getFunctionType() != NEW_SUBCONTRACT) {
            CoeusVector cvTotalReleasedAmt = (CoeusVector) subcontractData.get(SubContractAmountReleased.class);
            CoeusVector cvObligated = (CoeusVector) subcontractData.get(SubContractAmountInfoBean.class);
            if(cvObligated != null && cvObligated.size() > 0) {
                cvObligated.sort("lineNumber", false);
                SubContractAmountInfoBean obligatedBean = (SubContractAmountInfoBean) cvObligated.get(0);
                // Case 3566: Amount Available in Subcontract - Anomalous Result - Start
                // Sync the  amount info in DB with the amountInfoData 
//                if (amountInfoData == null) {
                    amountInfoData = new CoeusVector();
                    amountInfoData.add(new Double(obligatedBean.getObligatedAmount()));
                    amountInfoData.add(new Double(obligatedBean.getAnticipatedAmount()));
                    amountInfoData.add(new Double(0));
                    amountInfoData.add(new Double(0));
//                }
                // Case 3566: Amount Available in Subcontract - Anomalous Result - End
                if(cvTotalReleasedAmt != null && cvTotalReleasedAmt.size() > 0) {
                    amountInfoData.removeElementAt(2);
                    //Code modified for Princeton enhancements case#2802 - starts
                    //To get the Latest amount released. the calculation shoul not include the rejection amount.
                    amtReleased = cvTotalReleasedAmt.sum("amountReleased");
                    // 3566: Amount Available in Subcontract - Anomalous Result - Start
                    amtReleased = Utils.round(amtReleased);
                    // 3566: Amount Available in Subcontract - Anomalous Result - End
                    amountInfoData.add(2, new Double(amtReleased));
                }
                amountInfoData.removeElementAt(3);
                amountInfoData.add(3, new Double(obligatedBean.getObligatedAmount()-amtReleased));
                
            } // Added by nadh Bug Fix 1426 - start 04-Feb-2005 - start
            else {
                amountInfoData = new CoeusVector();
                amountInfoData.add(new Double(0));
                amountInfoData.add(new Double(0));
                amountInfoData.add(new Double(0));
                amountInfoData.add(new Double(0));
            }// Added by nadh Bug Fix 1426 - start 04-Feb-2005 - End
//        }
        htSubcontractData = subcontractData;
        queryEngine.addDataCollection(queryKey,subcontractData);
        
    }
    
    /** this method removes frame title
     */
    protected void clearOldInstance() {
        mdiForm.removeFrame(CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW, subContractBaseBean.getSubContractCode());
        edu.mit.coeus.gui.menu.CoeusWindowMenu windowMenu = mdiForm.getWindowMenu();
        if( windowMenu != null ){
            windowMenu = windowMenu.removeMenuItem( title );
        }
        if(subcontractOtherController != null) {
            tbdPnSubcontract.removeTabAt(OTHERS_TAB_INDEX);
            subcontractOtherController = null;
        }
    }
    
    /** Checks if data in QueryEngine is modifed and needs to be saved.
     * returns false if nothing to save.
     * else returns true.
     * @return false if nothing to save.
     * else returns true.
     */
    
    public boolean isSaveRequired(){
        
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            return false;
        }
        
        saveFormData();
        
        Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
        
        Equals eqInsert = new Equals(AC_TYPE, TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals(AC_TYPE, TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals(AC_TYPE, TypeConstants.DELETE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        
        Object key;
        CoeusVector data;
        boolean subcontractModified = false;
        try{
            while(enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                
                if(!(key instanceof Class)) continue;
                
                data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                if(! subcontractModified) {
                    if(data != null && data.size() > 0) {
                        subcontractModified = true;
                        break;
                    }
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        if(getFunctionType() == NEW_SUBCONTRACT || getFunctionType() == NEW_ENTRY_SUBCONTRACT) {
            subcontractModified = true;
        }
        return subcontractModified;
    }
    
    /**
     * Release lock
     */
    private boolean releaseLock() {
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(subContractBean.getSubContractCode());
        
        requesterBean.setFunctionType(UNLOCK);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + GET_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean != null) {
            if(!responderBean.isSuccessfulResponse()) {
                CoeusOptionPane.showErrorDialog(responderBean.getMessage());
                return false;
            }//End if Not successful response
        }
        return true;
    }
    
    
    public void beanUpdated(BeanEvent beanEvent) {
        Controller source = beanEvent.getSource();
        BaseBean baseBean = beanEvent.getBean();
        int msgId = beanEvent.getMessageId();
        CoeusVector updatedData = new CoeusVector();
        double obligatedAmount = 0;
        double amountreleased = 0;
        if(!amountInfoData.get(0).equals(EMPTY_STRING)) {
            obligatedAmount = Double.parseDouble(amountInfoData.get(0).toString());
        }
        if(!amountInfoData.get(2).equals(EMPTY_STRING) ) {
            amountreleased = Double.parseDouble(amountInfoData.get(2).toString());
        }
        if(source.getClass().equals(AmountInfoController.class) && baseBean.getClass().equals(SubContractAmountInfoBean.class)) {
            SubContractAmountInfoBean amountsBean = (SubContractAmountInfoBean)baseBean;
            updatedData.add(new Double(amountsBean.getObligatedAmount()));
            updatedData.add(new Double(amountsBean.getAnticipatedAmount()));
            updatedData.add(new Double(amtReleased));
            updatedData.add(new Double(amountsBean.getObligatedAmount() - amountreleased));
            amountInfoData.removeAllElements();
            amountInfoData.addAll(updatedData);
            subcontractDetailController.setAmounts(updatedData);
            amountReleasedController.setAmounts(updatedData);
            updatedData = null;
        }else if(source.getClass().equals(AmountReleasedController.class) &&
        baseBean.getClass().equals(SubContractAmountReleased.class)) {
            if(msgId == 2) {
                tbdPnSubcontract.setSelectedIndex(msgId);
                return;
            }
            //Code added for Princeton enhancements case#2802 - starts
            //To get the Latest amount released, if any Amount released information is updated.
            Hashtable subcontractData = queryEngine.getDataCollection(queryKey);
            CoeusVector cvTotalReleasedAmt = (CoeusVector) subcontractData.get(SubContractAmountReleased.class);
            if(cvTotalReleasedAmt != null && cvTotalReleasedAmt.size() > 0) {
                amtReleased = cvTotalReleasedAmt.sum("amountReleased");
                // 3566: Amount Available in Subcontract - Anomalous Result - Start
                amtReleased = Utils.round(amtReleased);
                // 3566: Amount Available in Subcontract - Anomalous Result - End
            }
            //Code added for Princeton enhancements case#2802 - ends
            SubContractAmountReleased amountReleased = (SubContractAmountReleased)baseBean;
            //Code added for Princeton enhancement case#2802
//            amtReleased = amtReleased + amountReleased.getAmountReleased();
            updatedData.add(amountInfoData.get(0));
            updatedData.add(amountInfoData.get(1));
            updatedData.add(new Double(amtReleased));
            updatedData.add(new Double(obligatedAmount - amtReleased));
            amountInfoData.removeAllElements();
            amountInfoData.addAll(updatedData);
            subcontractDetailController.setAmounts(amountInfoData);
            amountInfoController.setAmounts(amountInfoData);
            updatedData = null;
        }
    }
    
    /** displays inbox details. */
    private void showInboxDetails() {
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }
    
    //Case 2110 Start To Show the Current Locks of Loggein User
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End
    // 3587: Multi Campus Enahncements - Start
     private boolean checkUserHasCreateRight(String reqHomeUnit) {
         boolean createRight = false;
         RequesterBean requesterBean = new RequesterBean();
         requesterBean.setFunctionType(CHECK_USER_HAS_CREATE_RIGHT);
         requesterBean.setDataObject(reqHomeUnit);
         
         AppletServletCommunicator appletServletCommunicator = new
                 AppletServletCommunicator(connect, requesterBean);
         appletServletCommunicator.send();
         ResponderBean responderBean = appletServletCommunicator.getResponse();
         
         if(responderBean != null) {
             if(responderBean.isSuccessfulResponse()) {
                 Boolean right = (Boolean) responderBean.getDataObject();
                 createRight = right.booleanValue();
             }
         }
         return createRight;
     }
    // 3587: Multi Campus Enahncements - End
}
