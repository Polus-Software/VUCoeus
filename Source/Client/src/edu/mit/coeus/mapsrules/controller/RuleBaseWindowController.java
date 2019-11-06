/*
 * MetaRuleBaseWindowController.java
 *
 * Created on October 17, 2005, 12:17 PM
 */

package edu.mit.coeus.mapsrules.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.mapsrules.bean.BusinessRuleBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleBean;
import edu.mit.coeus.mapsrules.bean.MetaRuleDetailBean;
import edu.mit.coeus.mapsrules.bean.RuleBaseBean;
import edu.mit.coeus.mapsrules.gui.RuleBaseWindow;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTabbedPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;

/**
 *
 * @author  chandrashekara
 */
public class RuleBaseWindowController extends RuleController implements ActionListener,VetoableChangeListener{
    private JTabbedPane tbdPnRules;
    private RuleBaseWindow ruleBaseWindow;
    private static final String RULE_SERVLET = "/RuleMaintenanceServlet";
    private static final char GET_RULE_DATA = 'A';
    private static final String META_RULES = "Meta Rules";
    private static final String BUSINESS_RULES = "Business Rules";
    private MetaRuleDetailController metaRuleDetailController;
    private BusinessRuleDetailController businessRuleDetailController;
    private char functionType='M';
    private CoeusAppletMDIForm mdiForm;
    private String title;
    private QueryEngine queryEngine;
    private String queryKey = EMPTY_STRING;
    private String unitNumber;
    private RuleBaseBean ruleBaseBean;
    private Hashtable ruleMasterData;
    private ChangePassword changePassword;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    private CoeusMessageResources coeusMessageResources;
    private static final String SAVE_CHANGES = "award_exceptionCode.1004";
    private boolean closed = false;
    private boolean addRuleRight;
    private boolean modifyRuleRight;
    private boolean deleteRuleRight;
    private static final char UPDATE_META_RULE_DATA = 'D'; 
    private boolean isClosed;
    private boolean refreshRequired;
    /** Creates a new instance of MetaRuleBaseWindowController */
    public RuleBaseWindowController(String title,CoeusAppletMDIForm mdiForm,String unitNumber) {
        this.title = title;
        this.mdiForm = mdiForm;
        this.unitNumber = unitNumber;
        ruleBaseWindow = new RuleBaseWindow(title+" "+unitNumber,mdiForm);
        registerComponents();
        queryEngine = QueryEngine.getInstance();
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    private void initComponents() throws CoeusException{
        // Set the rights into the vector and send to the child windows
        Vector rightData = new Vector();
        rightData.addElement(new Boolean(addRuleRight));
        rightData.addElement(new Boolean(modifyRuleRight));
        rightData.addElement(new Boolean(deleteRuleRight));
        tbdPnRules = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        refreshRequired = true;
        // Instantiate the MetaRules screen and set the data.
        metaRuleDetailController = new MetaRuleDetailController(functionType,ruleBaseBean);
        metaRuleDetailController.setRefreshRequired(refreshRequired);
        metaRuleDetailController.setFormData(null);
        metaRuleDetailController.setRightsData(rightData);
        metaRuleDetailController.formatFields();
        metaRuleDetailController.setUnitNumber(unitNumber);
        
        // Instantiate the BusinessRules screen and set the data.
        businessRuleDetailController = new BusinessRuleDetailController(functionType,ruleBaseBean);
        businessRuleDetailController.setRightsData(rightData);
        businessRuleDetailController.formatFields();
        
        //Modified for Case#3893 - Java 1.5 issues
        //tbdPnRules.addTab(META_RULES,metaRuleDetailController.getControlledUI());
        //tbdPnRules.addTab(BUSINESS_RULES,businessRuleDetailController.getControlledUI());
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING);
        JPanel pnlMetaRules = new JPanel(layout);
        pnlMetaRules.add(metaRuleDetailController.getControlledUI());
        tbdPnRules.addTab(META_RULES,pnlMetaRules);

        JPanel pnlBusinnessRules = new JPanel(layout);
        pnlBusinnessRules.add(businessRuleDetailController.getControlledUI());
        tbdPnRules.addTab(BUSINESS_RULES,pnlBusinnessRules);
        //Case#3893 - End
    }
    /** Get the data from the server for the Meta rules and Business Rules
     *Get the Hashtable which contains MetaRule, MetaRulesDetails(tree Data),
     *Business Rules data and the Rights for the user
     *@ returns the hashtable
     *@ throws CoeusException
     */
    private Hashtable fetchDataFromServer() throws CoeusException{
        //Hashtable ruleMasterData = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( GET_RULE_DATA );
        requester.setDataObject(unitNumber);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            ruleMasterData  = (Hashtable)response.getDataObject();
        }
        queryKey = unitNumber;
        ruleBaseBean = new RuleBaseBean();
        ruleBaseBean.setUnitNumber(unitNumber);
        queryEngine.addDataCollection(queryKey,ruleMasterData);
        // get the user rights.
        addRuleRight = (((Boolean)ruleMasterData.get(CoeusConstants.ADD_BUSINESS_RULE)).booleanValue());
        modifyRuleRight = (((Boolean)ruleMasterData.get(CoeusConstants.MODIFY_BUSINESS_RULE)).booleanValue());
        deleteRuleRight = (((Boolean)ruleMasterData.get(CoeusConstants.DELETE_BUSINESS_RULE)).booleanValue());
        // If the user doesn't have all the right then disable the save option.
        if(!addRuleRight && !modifyRuleRight && !deleteRuleRight){
            ruleBaseWindow.saveMenuItem.setEnabled(false);
            ruleBaseWindow.btnSave.setEnabled(false);
        }
        return ruleMasterData;
    }
    
    
    public void display() {
        try{
            fetchDataFromServer();
            initComponents();
            ruleBaseWindow.setMenus();
            java.awt.Container container = ruleBaseWindow.getContentPane();
            container.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
            container.add(tbdPnRules);
            mdiForm.putFrame(title,ruleBaseWindow);
            mdiForm.getDeskTopPane().add(ruleBaseWindow);
            ruleBaseWindow.setVisible(true);
            ruleBaseWindow.setSelected(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
            CoeusOptionPane.showErrorDialog(propertyVetoException.getMessage());
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return ruleBaseWindow;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        ruleBaseWindow.addVetoableChangeListener(this);
        ruleBaseWindow.saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        ruleBaseWindow.saveMenuItem.addActionListener(this);
        ruleBaseWindow.btnSave.addActionListener(this);
        ruleBaseWindow.btnClose.addActionListener(this);
        ruleBaseWindow.closeMenuItem.addActionListener(this);
        ruleBaseWindow.inboxMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        ruleBaseWindow.delegationsMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        ruleBaseWindow.preferencesMenuItem.addActionListener(this);
        ruleBaseWindow.changePasswordMenuItem.addActionListener(this);
        ruleBaseWindow.exitMenuItem.addActionListener(this);
        //Case 2110 Start3
        ruleBaseWindow.currentLocksMenuItem.addActionListener(this);
        //Case 2110 End 3
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        metaRuleDetailController.saveFormData();
    }
    
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        ruleBaseBean = (RuleBaseBean)data;
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source =  actionEvent.getSource();
        try{
            blockEvents(true);
        if(source.equals(ruleBaseWindow.saveMenuItem) || source.equals(ruleBaseWindow.btnSave)){
            performSaveAction(true);
           
        }else if(source.equals(ruleBaseWindow.changePasswordMenuItem)){
            showChangePassword();
        }else if(source.equals(ruleBaseWindow.preferencesMenuItem)){
            showPreference();
        //Added for Case#3682 - Enhancements related to Delegations - Start
        }else if(source.equals(ruleBaseWindow.delegationsMenuItem)){
            displayUserDelegation();
        //Added for Case#3682 - Enhancements related to Delegations - End
        }else if(source.equals(ruleBaseWindow.exitMenuItem)){
            exitApplication();
        }else if(source.equals(ruleBaseWindow.closeMenuItem) || source.equals(ruleBaseWindow.btnClose)){
            try{
                close();
            }catch (PropertyVetoException propertyVetoException) {
                //Don't do anything. this exception is thrown to stop window from closing.
            }
        }else if(source.equals(ruleBaseWindow.inboxMenuItem)){
            showInbox();
        }//Case 2110 Start1
        else if(source.equals(ruleBaseWindow.currentLocksMenuItem)){
            showLocksForm();
        }//Case 2110 End 1   
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch (CoeusUIException coeusUIException){
            coeusUIException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusUIException.getMessage());
        }finally{
            blockEvents(false);
        }
    }
    
    private void showInbox(){
        InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame("Inbox" ))!= null ){
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
    
    //Case 2110 Start 2
    private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End 2
    
    private void close()throws PropertyVetoException{
        metaRuleDetailController.setCreateMetaRule(false);
        if(metaRuleDetailController.isSaveRequired() || businessRuleDetailController.isModified()) {
            int selection = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                try{
                    performSaveAction(false);
                     if(isClosed){
                        throw new PropertyVetoException("Cancel",null);
                        }
                }catch (CoeusUIException coeusUIException) {
                    //Validation Failed
                    throw new PropertyVetoException(EMPTY_STRING, null);
                }catch (CoeusException coeusException){
                    //Validation Failed
                    throw new PropertyVetoException(EMPTY_STRING, null);
                }
            }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
                throw new PropertyVetoException(EMPTY_STRING, null);
            }else if(selection == CoeusOptionPane.SELECTION_NO) {
                isClosed = false;
            }
        }
            mdiForm.removeFrame(title);
            closed = true;
            ruleBaseWindow.doDefaultCloseAction();
            cleanUp();
            refreshRequired = false;
            isClosed = false;
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
    
    public  void cleanUp(){
        
    }
    

    /** Save the Business Rules which contains Business Rule Details and Metarule
     *Details data
     */
    private void performSaveAction(boolean isSave)throws CoeusException,CoeusUIException{
        CoeusVector cvMetaRuleDetailData = null;
        CoeusVector cvBusinessRuleData = null;
        HashMap hmBusinessRuleData = new HashMap();
        metaRuleDetailController.setCreateMetaRule(isSave);
        if(metaRuleDetailController.prevalidation()){
            if(metaRuleDetailController.validate()){
                if(metaRuleDetailController.isModified() || businessRuleDetailController.isModified()){
                    metaRuleDetailController.saveDescriptionData();
                    cvMetaRuleDetailData = metaRuleDetailController.saveMetaRuleFormData();
                    cvBusinessRuleData = (CoeusVector)businessRuleDetailController.getFormData();
                    hmBusinessRuleData.put(MetaRuleDetailBean.class,cvMetaRuleDetailData);
                    hmBusinessRuleData.put(MetaRuleBean.class,metaRuleDetailController.getCvDescriptionData());
                    hmBusinessRuleData.put(BusinessRuleBean.class,cvBusinessRuleData);
                    boolean canSave = checkForUpdate(hmBusinessRuleData);
                    if(canSave){
                        saveBusinessRules(hmBusinessRuleData);
                        queryEngine.removeDataCollection(ruleMasterData);
                        fetchDataFromServer();
                        businessRuleDetailController.setFormData(null);
                        businessRuleDetailController.setModified(false);
                        metaRuleDetailController.setFormData(null);
                        metaRuleDetailController.setModified(false);
                        isClosed = false;
                    }
                }
            }else{
               isClosed = true;
            }
        }else{
            isClosed = true;
        }
    }
    
    private boolean checkForUpdate(HashMap businessRuleData){
        CoeusVector cvBusinessRule = (CoeusVector)businessRuleData.get(BusinessRuleBean.class);
        CoeusVector cvMetaRule = (CoeusVector)businessRuleData.get(MetaRuleBean.class);
        CoeusVector cvMetaRuleDetail = (CoeusVector)businessRuleData.get(MetaRuleDetailBean.class);
        if((cvBusinessRule!= null && cvBusinessRule.size() > 0 )
            || (cvMetaRule!= null && cvMetaRule.size() > 0)
            || (cvMetaRuleDetail!= null && cvMetaRuleDetail.size() > 0)){
                return true;
        }else{
            return false;
        }
            
    }
    
    private void saveBusinessRules(HashMap businessRuleData) throws CoeusException{
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType( UPDATE_META_RULE_DATA );
        requester.setDataObject(businessRuleData);
        requester.setId(ruleBaseBean.getUnitNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CoeusGuiConstants.CONNECTION_URL+RULE_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( !response.isSuccessfulResponse() ){
            throw new CoeusException(response.getMessage());
        }else{
            System.out.println("Successfull Updation");
        }
    }
    
    
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End   
    
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
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
}
