/*
 * AwardTemplateBaseWindowController.java
 *
 * Created on December 14, 2004, 5:27 PM
 * @author  bijosht
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 2-Sept-2010
 * by Keerthy Jayaraj
 */
package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.bean.AwardTemplateBean;
import edu.mit.coeus.admin.bean.AwardTemplateCommentsBean;
import edu.mit.coeus.admin.bean.AwardTemplateContactsBean;
import edu.mit.coeus.admin.bean.AwdTemplateRepTermsBean;
import edu.mit.coeus.admin.bean.TemplateBaseBean;
import edu.mit.coeus.admin.gui.AwardTemplateBaseWindow;
import edu.mit.coeus.admin.gui.SelectAwardTemplateForm;
import edu.mit.coeus.award.bean.TemplateTermsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.gui.event.Controller;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTabbedPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import edu.mit.coeus.utils.query.Or;
import edu.mit.coeus.utils.query.QueryEngine;
import java.net.MalformedURLException;
import java.net.URL;
//case 1632 end
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeListener;


public class AwardTemplateBaseWindowController extends AwardTemplateController
implements VetoableChangeListener,ActionListener,ChangeListener {
    
    private AwardTemplateBaseWindow awardTemplateBaseWindow;
    private CoeusTabbedPane tbdPnAwardTemplate;
    
    private SimpleDateFormat simpleDateFormat;
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
//    private static final String DATE_SEPARATERS = "-:/.,|";
    
//    private String titleStart = "Template ";
    private String title = "Template " ;
    private String titleAdd = "Add Template";
    private String titleCopy = "Copy Template";
    private QueryEngine queryEngine;
    private TemplateBaseBean templateBaseBean;
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private Controller controller[];
    
    private static final String MAIN = "Main";
    private static final String CONTACTS = "Contacts";
    private static final String COMMENTS = "Comments";
    private static final String TERMS = "Terms";
    private static final String REPORTS = "Reports";
    
    private static final int MAIN_TAB_INDEX = 0;
    private static final int CONTACTS_TAB_INDEX = 1;
    private static final int COMMENTS_TAB_INDEX = 2;
    private static final int TERMS_TAB_INDEX = 3;
    private static final int REPORTS_TAB_INDEX = 4;
    
    private static final char GET_TEMPLATE_DETAILS = 'A';
    private static final char SAVE_AWARD_TEMPLATE = 'S';
    private static final char GET_TEMPLATE_COUNT = 'C';
    
    private final String SERVLET ="/AwardTemplateMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ SERVLET;
    private static final String PRINT_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    
    //case 1632 begin
    private static final char AWARD_TEMPLATE_REPORT = 'T';
//    private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
    //case 1632 end
    
    private final String COULD_NOT_CONTACT_SERVER = "Could not contact the server";
    
    private TemplateMainController templateMainController;
    private TemplateTermsController templateTermsController;
    private TemplateCommentsController templateCommentsController;
    private TemplateContactsController templateContactsController;
    private TemplateReportsController templateReportsController;
    
    private ChangePassword changePassword;
    private SelectAwardTemplateForm selectAwardTemplateForm = null;
    private UserPreferencesForm userPreferencesForm = null;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm = null;
    //Added for Case#3682 - Enhancements related to Delegations - End
    private boolean closed = false;
    private int selectedTabIndex;
    /** Do you want to save changes ? */
    private static final String SAVE_CHANGES = "award_exceptionCode.1004";
    private static final String TEMPLATE_COUNT_MSQ_KEY = "awardTemplateExceptionCode.1603";
    private static final String DUPLICATE_TEMPLATE_DESC_MSQ_KEY = "awardTemplateExceptionCode.1604";
    /** Creates a new instance of AwardTemplateBaseWindowController */
    public AwardTemplateBaseWindowController(String title, final char functionType,final TemplateBaseBean bean,SelectAwardTemplateForm selectAwardTemplateForm ) {
        super(bean);
        this.selectAwardTemplateForm = selectAwardTemplateForm;
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        if(bean != null) {
            this.templateBaseBean = bean;
        }
        this.title = this.title + title;
        setFunctionType(functionType);
        
        queryEngine = QueryEngine.getInstance();
        
        awardTemplateBaseWindow = new AwardTemplateBaseWindow(title, mdiForm);
        awardTemplateBaseWindow.setTemplateBaseBean(templateBaseBean);
        tbdPnAwardTemplate = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        blockEvents(true);
        
        if(!fetchData(getFunctionType(), templateBaseBean.getTemplateCode())){
            //canDisplay = false;
            return ;
        }else {
            //Get Data From server and initialize components.
            //  try{
            
            int controllers = 5;
            controller = new Controller[controllers];
            registerComponents();
            
            //canDisplay  = true;
                /*if(awardDetailController == null) {
                    loadTab(AWARD_DETAIL_TAB_INDEX);
                }*/
            
           /* }catch (CoeusException coeusException) {
                coeusException.printStackTrace();
                //canDisplay = false;
            }*/
        }
        blockEvents(false);
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
   }
    
    /** instantiates instance variables and tabs. */
    private void initComponents() {
        awardTemplateBaseWindow.setFunctionType(getFunctionType());
        //If in display Mode disable Save
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            awardTemplateBaseWindow.btnSave.setEnabled(false);
            awardTemplateBaseWindow.mnuItmSave.setEnabled(false);
        }
        
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BorderLayout());
        Container awardTemplateBaseContainer = awardTemplateBaseWindow.getContentPane();
        for(int index = 0; index < controller.length; index++) {
            loadTab(index);
            //Component cmp  = controller[index].getControlledUI();
            //tbdPnAwardTemplate.setComponentAt(index, cmp);
        }
        //Added For COEUSDEV-562: Changes to comments in award templates not getting saved : Start
        templateMainController.registerObserver(templateCommentsController);
        templateCommentsController.registerObserver(templateMainController);
        //COEUSDEV-562: End
        /*for (int index=0;index<controller.length;index++) {
            AwardTemplateController cntrler = (AwardTemplateController) controller[index];
            cntrler.setCopyData(htTemplateData);
        }*/
        
        tbdPnAwardTemplate.addTab(MAIN, new JScrollPane(templateMainController.getControlledUI()));
        tbdPnAwardTemplate.addTab(CONTACTS, new JScrollPane(templateContactsController.getControlledUI()));
        tbdPnAwardTemplate.addTab(COMMENTS, new JScrollPane(templateCommentsController.getControlledUI()));
        tbdPnAwardTemplate.addTab(TERMS, new JScrollPane(templateTermsController.getControlledUI()));
        tbdPnAwardTemplate.addTab(REPORTS, new JScrollPane(templateReportsController.getControlledUI()));
        
        
        //if in new mode load all tabs
        //if(getFunctionType() != TypeConstants.MODIFY_MODE && getFunctionType() != TypeConstants.DISPLAY_MODE) {
        
        //}
        
        basePanel.add(tbdPnAwardTemplate);
        awardTemplateBaseContainer.add(basePanel);
    }
    
    public void setDisplayProperties(char mode) {
        AwardTemplateController templatecontoller;
        for (int index=0;index<controller.length;index++) {
            templatecontoller = (AwardTemplateController)controller[index];
            templatecontoller.setFunctionType(mode);
        }
    }
    
    /** registers the components with event listeners. */
    public void registerComponents() {
        //templateMainController = new TemplateMainController();
        awardTemplateBaseWindow.addVetoableChangeListener(this);
        
        //adding listeners to toolbar buttons
        awardTemplateBaseWindow.btnSeclectTemplate.addActionListener(this);
        awardTemplateBaseWindow.btnSave.addActionListener(this);
        awardTemplateBaseWindow.btnClose.addActionListener(this);
        //case 1632 begin
        awardTemplateBaseWindow.btnPrint.addActionListener(this);
        awardTemplateBaseWindow.mnuItmPrint.addActionListener(this);
        //case 1632 end
        //adding listeners to File menu
        awardTemplateBaseWindow.mnuItmInbox.addActionListener(this);
        awardTemplateBaseWindow.mnuItmClose.addActionListener(this);
        awardTemplateBaseWindow.mnuItmSave.addActionListener(this);
        awardTemplateBaseWindow.mnuItmChangePassword.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        awardTemplateBaseWindow.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        awardTemplateBaseWindow.mnuItmPreferences.addActionListener(this);
        awardTemplateBaseWindow.mnuItmExit.addActionListener(this);
        //Case 2110 Start
        awardTemplateBaseWindow.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        tbdPnAwardTemplate.addChangeListener(this);
    }
    
    /** displays thie internal frame which is controlled by this. */
    public void display() {
        try {
            char formFunctionType;
            if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
                formFunctionType = TypeConstants.MODIFY_MODE;
            }else {
                formFunctionType  = TypeConstants.DISPLAY_MODE;
            }
            //mdiForm.putFrame(CoeusGuiConstants.AWARD_TEMPLATE_BASE_WINDOW, ""+templateBaseBean.getTemplateCode(), formFunctionType, awardTemplateBaseWindow);
            mdiForm.putFrame(CoeusGuiConstants.AWARD_TEMPLATE_BASE_WINDOW, awardTemplateBaseWindow);
            if (!awardTemplateBaseWindow.isVisible()) {
                mdiForm.getDeskTopPane().add(awardTemplateBaseWindow);
                awardTemplateBaseWindow.setSelected(true);
            }
            
            awardTemplateBaseWindow.setVisible(true);
        } catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    public void setTitle(String title) {
        this.title ="Template "+ title;
    }
    

    private boolean fetchData(char functionType,int templateCode) {
        CoeusVector cvDataObjects = new CoeusVector();
        
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_TEMPLATE_DETAILS);
        
        cvDataObjects.add(0,new Character(functionType));
        cvDataObjects.add(1,new Integer(templateCode));
        requesterBean.setDataObjects(cvDataObjects);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connectTo, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        
        if(responderBean.isSuccessfulResponse()) {
            Hashtable templateData = (Hashtable)responderBean.getDataObject();
           // htTemplateData = templateData;
            if(getFunctionType() == TypeConstants.NEW_MODE){
                //Character newMode = new Character(TypeConstants.NEW_MODE);
                Integer templCode = (Integer)templateData.get(KeyConstants.TEMPLATE_CODE);
                
                //prepare query key
                queryKey = ""+templCode;
                templateBaseBean.setTemplateCode(templCode.intValue());
                
                //Set title
                if(awardTemplateBaseWindow != null){
                    awardTemplateBaseWindow.setTitle(titleAdd);
                }
                
            }else{
                if(getFunctionType() == TypeConstants.COPY_MODE){
                    CoeusVector cvAwdTemplate =
                    (CoeusVector)templateData.get(AwardTemplateBean.class);
                    if(cvAwdTemplate != null && cvAwdTemplate.size()>0){
                        AwardTemplateBean awardTemplateBean =
                        (AwardTemplateBean)cvAwdTemplate.get(0);
                        //prepare query key
                        //queryKey = ""+awardTemplateBean.getTemplateCode();
                        super.prepareQueryKey(awardTemplateBean);
                        templateBaseBean.setTemplateCode(awardTemplateBean.getTemplateCode());
                    }
                    /*if (controller != null) {
                        for (int index=0;index<controller.length;index++) {
                            AwardTemplateController cntrler = (AwardTemplateController) controller[index];
                            cntrler.setCopyData(templateData);
                        }
                    }*/
                    //Set title
                    if(awardTemplateBaseWindow != null){
                        awardTemplateBaseWindow.setTitle(titleCopy);
                    }
                }else {
                    //prepare query key
                    super.prepareQueryKey(templateBaseBean);
                    //queryKey = ""+this.templateBaseBean.getTemplateCode();
                    
                    //Set title.
                    if(awardTemplateBaseWindow != null) {
                        awardTemplateBaseWindow.setTitle(title);
                    }
                }
                
            }

            extractToQueryEngine(templateData);
        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        
        return true;
    }
    
    /** extracts award data and stores to QueryEngine.
     * @param awardData award data to be saves to query engine.
     */
    private void extractToQueryEngine(Hashtable templateData) {
        //awardTemplateBean = (AwardTemplateBean)templateData.get(AwardTemplateBean.class);
        queryEngine.addDataCollection(queryKey,templateData);
    }
    
    private void loadTab(int index) {
        //lazy loading tab controller and data - START
        switch (index) {
            case MAIN_TAB_INDEX:
                templateMainController = new TemplateMainController(templateBaseBean, getFunctionType());
                controller[index] = templateMainController;
                break;
            case CONTACTS_TAB_INDEX:
                templateContactsController = new TemplateContactsController(templateBaseBean, mdiForm,getFunctionType());
                controller[index] = templateContactsController;
                break;
            case COMMENTS_TAB_INDEX:
                templateCommentsController = new TemplateCommentsController(templateBaseBean, getFunctionType());
                controller[index] = templateCommentsController;
                break;
            case TERMS_TAB_INDEX:
                templateTermsController = new TemplateTermsController(templateBaseBean, getFunctionType());
                controller[index] = templateTermsController;
                break;
            case REPORTS_TAB_INDEX:
                templateReportsController = new TemplateReportsController(templateBaseBean,  getFunctionType());
                controller[index] = templateReportsController;
                break;
        }
      }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return awardTemplateBaseWindow;
    }
    
    public Object getFormData() {
        return null;
    }
    /**
     * Saves the data to the query engine by calling all the controller's saveFormData
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        for(int index = 0; index < controller.length; index++) {
            if(controller[index] != null) {
                controller[index].saveFormData();
            }
        }
    }
    /**
     * Refreshes the data by calling the controllers'
     */
   /*public void refresh () {
    
    }*/
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        templateBaseBean = (TemplateBaseBean)data;
        try{
            blockEvents(true);
            if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
                awardTemplateBaseWindow.btnSave.setEnabled(false);
                awardTemplateBaseWindow.mnuItmSave.setEnabled(false);
            } else {
                awardTemplateBaseWindow.btnSave.setEnabled(true);
                awardTemplateBaseWindow.mnuItmSave.setEnabled(true);
            }
            if (!fetchData(getFunctionType(),templateBaseBean.getTemplateCode())) {
                return ;
            } else {
                AwardTemplateController awardTemplateController;
                int selectedIndex = tbdPnAwardTemplate.getSelectedIndex();
                
                for(int index = 0; index < controller.length; index++) {
                    if(controller[index] == null) {
                        continue;
                    }
                    awardTemplateController = (AwardTemplateController)controller[index];
                    awardTemplateController.prepareQueryKey(templateBaseBean);
                    awardTemplateController.setFormData(templateBaseBean);
                    awardTemplateController.setRefreshRequired(true);
                    if(index == selectedIndex) {
                        awardTemplateController.refresh();
                    }
                }
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }finally {
            blockEvents(false);
        }
    }
    /**
     * Checks whether the Template is modified or not
     */
    public boolean isSaveRequired() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE) {
            return false;
        }
        //Save Data for the currently selected Tab
        int selectedTabIndex = tbdPnAwardTemplate.getSelectedIndex();
        try{
            controller[selectedTabIndex].saveFormData();
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            return true;
        }
        //Commented for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        //enumeration object is not been used.
        //When multiple award templates are opened and try to close all exception throws
//        Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
        //COEUSQA-1456 : End
        
//        Enumeration enumeration =  queryEngine.getKeyEnumeration(queryKey);
        
        Equals eqInsert = new Equals("acType", TypeConstants.INSERT_RECORD);
        Equals eqUpdate = new Equals("acType", TypeConstants.UPDATE_RECORD);
        Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
        
        Or insertOrUpdate = new Or(eqInsert, eqUpdate);
        Or insertOrUpdateOrDelete = new Or(insertOrUpdate, eqDelete);
        
        
//        Object key;
        CoeusVector data;
        boolean templateModified = false;
        try{        
        data = queryEngine.executeQuery(queryKey,AwardTemplateBean.class,insertOrUpdate);
        if(data != null && data.size() > 0) {
            templateModified = true;
        }
        //modified for the Bug Fix:1871 start
        //data = queryEngine.executeQuery(queryKey,AwardTemplateContactsBean.class,insertOrUpdate);
        data = queryEngine.executeQuery(queryKey,AwardTemplateContactsBean.class,insertOrUpdateOrDelete);
         //End Bug Fix:1871
        if(data != null && data.size() > 0) {
            templateModified = true;
        }       
       
        data = queryEngine.executeQuery(queryKey,AwardTemplateCommentsBean.class,insertOrUpdate);
        if(data != null && data.size() > 0) {
            templateModified = true;
        }
        data = queryEngine.executeQuery(queryKey,TemplateTermsBean.class,insertOrUpdate);
        if(data != null && data.size() > 0) {
            templateModified = true;
        }
        data = queryEngine.executeQuery(queryKey,AwdTemplateRepTermsBean.class,insertOrUpdate);
        if(data != null && data.size() > 0) {
            templateModified = true;
        }

        /*    while(enumeration.hasMoreElements()) {
                key = enumeration.nextElement();
                
                if(!(key instanceof Class)|| key.equals(AwardTemplateBean.class) || key.equals(ComboBoxBean.class)) continue;
                
                data = queryEngine.executeQuery(queryKey, (Class)key, insertOrUpdateOrDelete);
                if(! templateModified) {
                    if(data != null && data.size() > 0) {
                        templateModified = true;
                        break;
                    }
                }
            }*/
            
            //save reqd check for Award Terms
            if(!templateModified) {
                String termsKey[] = {KeyConstants.EQUIPMENT_APPROVAL_TERMS, KeyConstants.INVENTION_TERMS,
                KeyConstants.PRIOR_APPROVAL_TERMS, KeyConstants.PROPERTY_TERMS, KeyConstants.PUBLICATION_TERMS,
                KeyConstants.REFERENCED_DOCUMENT_TERMS, KeyConstants.RIGHTS_IN_DATA_TERMS,
                KeyConstants.SUBCONTRACT_APPROVAL_TERMS, KeyConstants.TRAVEL_RESTRICTION_TERMS};
                for(int index = 0; index < termsKey.length; index++) {
                    data = queryEngine.executeQuery(queryKey, termsKey[index], insertOrUpdateOrDelete);
                    if(! templateModified) {
                        if(data != null && data.size() > 0) {
                            templateModified = true;
                            break;
                        }
                    }
                }//end for
            }//end if
            
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        return templateModified;
    }
    
    /**
     * Saves the template data to the database
     */
    private void saveAwardTemplate() throws CoeusUIException {
        try {
            saveFormData();
        } catch (CoeusException ce){
            throw new CoeusUIException(ce.getMessage());
        }
        try{
            if(!validate()) {
                throw new CoeusUIException();
            }
        }catch (CoeusUIException coeusUIException) {
            throw coeusUIException;
        }
        
        Hashtable htDataToSave = queryEngine.getDataCollection(queryKey);
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(SAVE_AWARD_TEMPLATE);
        
        CoeusVector cvDataObjects = new CoeusVector();
        cvDataObjects.add(0,new Integer(templateBaseBean.getTemplateCode()));
        cvDataObjects.add(1,htDataToSave);
        
        requesterBean.setDataObjects(cvDataObjects);
        
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            throw new CoeusUIException();
        }
        
        if(responderBean.isSuccessfulResponse()) {
            Hashtable templateData = (Hashtable)responderBean.getDataObject();
            extractToQueryEngine(templateData);
            CoeusVector cvTemplate = (CoeusVector) templateData.get(AwardTemplateBean.class);
            
            if(cvTemplate != null && cvTemplate.size()>0){
                AwardTemplateBean awardTemplateBean = 
                    (AwardTemplateBean )cvTemplate.get(0);
                mdiForm.addToHashSet(new Integer(awardTemplateBean.getTemplateCode()));
            }
            
            
            try {
                refresh();
            } catch (CoeusException ce){
                throw new CoeusUIException(ce.getMessage());
            }
        }else {
            //Server Error
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            throw new CoeusUIException();
        }
    }
    /*
     * Refreshes all the tabs
     */
    public void refresh() throws CoeusException {
        Controller tabController;
        for(int index = 0; index < controller.length; index++) {
            tabController = controller[index];
            if(tabController != null) {
                tabController.setRefreshRequired(true);
                tabController.refresh();
            }
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        boolean valid = true;
        CoeusVector cvAwardTemplateBean = null;
        try {
            cvAwardTemplateBean = queryEngine.executeQuery(queryKey,AwardTemplateBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
        } catch (CoeusException ce){
            throw new edu.mit.coeus.exception.CoeusUIException(ce.getMessage());
        }
        AwardTemplateBean awardTemplateBean= null;
        if(cvAwardTemplateBean!= null && cvAwardTemplateBean.size() > 0){
            awardTemplateBean = (AwardTemplateBean)cvAwardTemplateBean.get(0);
        }
        CoeusVector cvAllTemplateData = selectAwardTemplateForm.getAllTemplateData();
        Equals eqTemplateDesc = new Equals("description",awardTemplateBean.getDescription());
        NotEquals ntEqTemplateCode = new NotEquals("templateCode",new Integer(awardTemplateBean.getTemplateCode()));
        And andTempDescNotTempCode = new And(ntEqTemplateCode,eqTemplateDesc);
        CoeusVector cvFilteredData = cvAllTemplateData.filter(andTempDescNotTempCode);
        if (cvFilteredData.size()>0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DUPLICATE_TEMPLATE_DESC_MSQ_KEY));
            tbdPnAwardTemplate.setSelectedIndex(0);
            templateMainController.setFocusToDescp();
            return false;
        }
        
        for(int index = 0; index < controller.length; index++) {
            if(controller[index] != null && !controller[index].validate()) {
                tbdPnAwardTemplate.setSelectedIndex(index);
                //valid = false;
                return false;
            }
        }
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_TEMPLATE_COUNT);
        requesterBean.setDataObject(new Integer(templateBaseBean.getTemplateCode()));
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connectTo, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            CoeusOptionPane.showErrorDialog(COULD_NOT_CONTACT_SERVER);
            return false;
        }
        Integer integerTemplateCount =(Integer)responderBean.getDataObject();
        if (integerTemplateCount.intValue() >0) {
            int choice = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(TEMPLATE_COUNT_MSQ_KEY),CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            if (choice == CoeusOptionPane.SELECTION_NO) {
                return false;
            }
        }
        return valid;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        Object source = ae.getSource();
        if (source == awardTemplateBaseWindow.mnuItmChangePassword) {
            showChangePassword();
        } else if (source == awardTemplateBaseWindow.mnuItmClose || source == awardTemplateBaseWindow.btnClose) {
            try{
                close();
            }catch (PropertyVetoException propertyVetoException) {
                //Don't do anything. this exception is thrown to stop window from closing.
            }
        } else if (source == awardTemplateBaseWindow.mnuItmExit) {
            exitApplication();
        } else if (source == awardTemplateBaseWindow.mnuItmInbox ) {
            showInboxDetails();
        } else if (source == awardTemplateBaseWindow.mnuItmPreferences) {
            showPreferences();
        //Added for Case#3682 - Enhancements related to Delegations - Start
        } else if (source == awardTemplateBaseWindow.mnuItmDelegations) {
            displayUserDelegation();
        //Added for Case#3682 - Enhancements related to Delegations - End
        } else if (source == awardTemplateBaseWindow.mnuItmSave || source == awardTemplateBaseWindow.btnSave) {
            try {
                saveAwardTemplate();
            } catch (CoeusUIException cue) {
                //CoeusOptionPane.showErrorDialog(cue.getMessage());
            }
        } else if (source == awardTemplateBaseWindow.mnuItmSeclectTemplate || source == awardTemplateBaseWindow.btnSeclectTemplate) {
            if (selectAwardTemplateForm == null) {
                selectAwardTemplateForm = new SelectAwardTemplateForm(mdiForm,false);
            } else {
                selectAwardTemplateForm.setOpenedFromMenu(false);
                try {
                    if(isSaveRequired()) {
                        int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
                        if(selection == CoeusOptionPane.SELECTION_YES) {
                            try {
                                saveAwardTemplate();
                            } catch (CoeusUIException e) {
                                return ;
                            }
                        }else if (selection == CoeusOptionPane.SELECTION_CANCEL) {
                            return;
                        }
                    }
                    selectAwardTemplateForm.setFormData();
                } catch (CoeusClientException cce){
                    CoeusOptionPane.showDialog(cce);
                }
            }
            selectAwardTemplateForm.display();
        }
        //case 1632 begin
        else if (source == awardTemplateBaseWindow.mnuItmPrint || source == awardTemplateBaseWindow.btnPrint){
            try{
                performReport();
            }catch(CoeusException coeusException){
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                coeusException.printStackTrace();
            }
        }
        //case 1632 end
       
        //Case 2110 Start
        else if(source.equals(awardTemplateBaseWindow.mnuItmCurrentLocks)){
             try{
                 showLocksForm();                 
             }catch(CoeusException coeusException){
                coeusException.printStackTrace();
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
             }catch(Exception exception){
                exception.printStackTrace();
                CoeusOptionPane.showErrorDialog(exception.getMessage());
             }
            
        } //Case 2110 End
       
    }
    
    /** closes the Base Window and removes the reference from MDIForm.
     * @throws PropertyVetoException PropertyVetoException
     */
    public void close() throws PropertyVetoException{
        if(isSaveRequired()) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                try{
                    saveAwardTemplate();
                }catch (CoeusUIException coeusUIException) {
                    //Validation Failed
                    throw new PropertyVetoException(EMPTY, null);
                }
            }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                throw new PropertyVetoException(EMPTY, null);
            }
        }
        mdiForm.removeFrame(CoeusGuiConstants.AWARD_TEMPLATE_BASE_WINDOW );
        closed = true;
        awardTemplateBaseWindow.doDefaultCloseAction();
        cleanUp();
    }
    
    /** listens to window closing event.
     * @param propertyChangeEvent propertyChangeEvent
     * @throws PropertyVetoException PropertyVetoException
     */
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /**
     * Method to implement the change password
     */
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
    
    //Case 2110 Start
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End
    
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
    
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /**
     * Displyas the Prefrences
     */
    private void showPreferences(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
    public void stateChanged(javax.swing.event.ChangeEvent ce) {
        try {
            if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
                //save data for earlier selected tab
                controller[selectedTabIndex].saveFormData();
            }
        }catch (CoeusException coeusException) {
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            tbdPnAwardTemplate.setSelectedIndex(selectedTabIndex);
        }
        selectedTabIndex = tbdPnAwardTemplate.getSelectedIndex();
        if(controller[selectedTabIndex] == null) {
            loadTab(selectedTabIndex);
            tbdPnAwardTemplate.setComponentAt(selectedTabIndex, new JScrollPane(controller[selectedTabIndex].getControlledUI()));
        }
        //if in display mode no need to refresh just return
        if (getFunctionType() == TypeConstants.DISPLAY_MODE) {
            return ;
        }
        
        switch (selectedTabIndex) {
            case MAIN_TAB_INDEX:
                templateMainController.refresh();
                break;
            case CONTACTS_TAB_INDEX:
                templateContactsController.refresh();
                break;
            case COMMENTS_TAB_INDEX:
                templateCommentsController.refresh();
                break;
            case TERMS_TAB_INDEX:
                templateTermsController.refresh();
                break;
            case REPORTS_TAB_INDEX:
                templateReportsController.refresh();
                break;
        }
    }
    //case 1632 begin
    //add for creating awardTemplate report
    private boolean performReport()throws CoeusException{
         if(isSaveRequired()) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                try{
                    saveAwardTemplate();
                }catch (CoeusUIException coeusUIException) {
                    //Validation Failed
                    coeusUIException.printStackTrace();
                    return false;
                }
            }else if(selection == CoeusOptionPane.SELECTION_NO) {
                return false;
            }
         }    
         Hashtable htPrintParams = new Hashtable(); 
         htPrintParams.put("TEMPLATE_CODE",""+templateBaseBean.getTemplateCode());
         RequesterBean requester = new RequesterBean();
         requester.setFunctionType(AWARD_TEMPLATE_REPORT);
         requester.setDataObject(htPrintParams);
         
         //For Streaming
         requester.setId("Award/AwardTemplate");
         requester.setFunctionType('R');
         //For Streaming
         
         AppletServletCommunicator comm
         = new AppletServletCommunicator(PRINT_SERVLET, requester);
         
         comm.send();
         ResponderBean responder = comm.getResponse();
         String fileName = "";
         if(responder.isSuccessfulResponse()){
//             AppletContext coeusContxt = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
//             
//           
             fileName = (String)responder.getDataObject();
//             System.out.println("Report Filename is=>"+fileName);
//             
//             fileName.replace('\\', '/') ; // this is fix for Mac
//             URL reportUrl = null;
             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch(MalformedURLException muEx){
                 throw new CoeusException(muEx.getMessage());
             }catch(Exception uaEx){
                 throw new CoeusException(uaEx.getMessage());
             }
             
         }else{
             throw new CoeusException(responder.getMessage());
         }
         return true;
    }   
    //case 1632 end
    /** cleans up instance variables.
     * i.e. sets instance variables to null.
     */
    
    public void cleanUp(){
        mdiForm.removeHashSet();
        for(int index = 0; index < controller.length; index++) {
            if(controller[index] != null) {
                if(controller[index] instanceof AwardTemplateController) {
                    ((AwardTemplateController)controller[index]).cleanUp();
                }
                controller[index] = null;
            }
        }
        queryEngine.removeDataCollection(queryKey);
    }
    
    /** Getter for property templateBaseBean.
     * @return Value of property templateBaseBean.
     *
     */
      public int getTemplateCode(){
        return templateBaseBean.getTemplateCode();
      }
}
