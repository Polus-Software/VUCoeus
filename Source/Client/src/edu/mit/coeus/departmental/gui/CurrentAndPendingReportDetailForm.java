/*
 * CurrentAndPendingReportDetailForm.java
 *
 * Created on November 3, 2004, 10:36 AM
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.toolbar.*;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.gui.event.BlockingGlassPane;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.brokers.*;
import java.applet.AppletContext;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JToolBar;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.beans.*;
import java.net.URL;


/**
 *
 * @author  chandrashekara
 */
public class CurrentAndPendingReportDetailForm extends CoeusInternalFrame implements ActionListener,VetoableChangeListener{
    
    
    private CoeusMenuItem mnuItmInbox, mnuItmSaveAs,mnuItmPrint,mnuItmSort,
    mnuItmClose,mnuItmChangePassWord,
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    mnuItmDelegations,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    mnuItmPreference,mnuItmExit;
    private CoeusMenu mnuFile;
    private CoeusToolBarButton btnPrint, btnSaveAs,btnSort, btnClose;
    private CoeusAppletMDIForm mdiForm;
    private static final String EMPTY_STRING = "";
    private String title;
    private CoeusMessageResources coeusMessageResources;
    
    //specifies the tab and tab index for Current and Pending support
    private static final String CURRENT_SUPPORT = "Current Support";
    private static final String PENDING_SUPPORT = "Pending Support";
    private static final int CURRENT_TAB_INDEX = 0;
    private static final int PENDING_TAB_INDEX = 1;
    private static CurrentSupportForm currentSupportForm;
    private static PendingSupportForm pendingSupportForm;
    private Hashtable reportData;
    private CoeusTabbedPane tbdPnCurrentAndPending;
    private final String SEPERATOR="seperator";
    private CoeusVector currentData;
    private CoeusVector pendingData;
    private String headerValue;
    private boolean closed = false;
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
     private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    private BlockingGlassPane blockingGlassPane;
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    private static final char PRINT_CURRENT = 'L'; 
    private static final char PRINT_PENDING = 'O';
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
    public static final String PRINT_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
    
    private ChangePassword changePassword;
    
    //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - Start    
    private CurrentPendingReportForm currentpendingReportform;
    private static HashMap hmCurrentPendingReport;
    private static final String CURRENT_REPORT = "Current";
    private static final String PENDING_REPORT = "Pending";
    //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - End
    
    /** Creates a new instance of CurrentAndPendingReportDetailForm */
    public CurrentAndPendingReportDetailForm(String title, CoeusAppletMDIForm mdiForm) {
        super(title,mdiForm);
        this.mdiForm = mdiForm;
        this.title = title;
        tbdPnCurrentAndPending = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    // Initialize the components.Set the menu items, toolbar buttons and build
    public  void initComponents() {
        setFrameToolBar(getProposalBaseWindowToolBar());
        prepareMenus();
        buildFrame();
        setListeners();
    }
    
    /** This method will sets the lestsners for the toolbar buttons and menu items
     */
    private void setListeners(){
        
        this.addVetoableChangeListener(this);
        
        btnClose.addActionListener(this);
        btnPrint.addActionListener(this);
        btnSaveAs.addActionListener(this);
        btnSort.addActionListener(this);
        
        mnuItmChangePassWord.addActionListener(this);
        mnuItmClose.addActionListener(this);
        mnuItmExit.addActionListener(this);
        mnuItmInbox.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start 
        mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        mnuItmPreference.addActionListener(this);
        mnuItmPrint.addActionListener(this);
        mnuItmSaveAs.addActionListener(this);
        mnuItmSort.addActionListener(this);
    }
    // Build the various classes for the component and add to the internaframe 
    // Create the internalFrame
    private void buildFrame(){
        try{
            java.awt.Container currentPendingContainer = this.getContentPane();
            javax.swing.JPanel basePanel = new javax.swing.JPanel();
            basePanel.setLayout(new BorderLayout());
            
            currentSupportForm = new CurrentSupportForm();
            pendingSupportForm = new PendingSupportForm();
            
            
            tbdPnCurrentAndPending.addTab(CURRENT_SUPPORT, currentSupportForm.getControlledUI());
            tbdPnCurrentAndPending.addTab(PENDING_SUPPORT, pendingSupportForm.getControlledUI());
            
            basePanel.add(tbdPnCurrentAndPending);
            //modified with case 3505:Data must be visible even in low resolution
            //javax.swing.JScrollPane jScrollPane = new javax.swing.JScrollPane(basePanel);
            currentPendingContainer.add(basePanel);
            //3505 end
            
            mdiForm.putFrame(CoeusGuiConstants.CURRENT_AND_PENDING_SUPPORT, this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // Set the data once getting from the server. Set for the both the component 
    public void setFormData(){
        Hashtable data = getReportData();
        if(data!= null){
            currentData = (CoeusVector)data.get(KeyConstants.DEPARTMENT_CURRENT_REPORT_DATA);
            pendingData  = (CoeusVector)data.get(KeyConstants.DEPARTMENT_PENDING_REPORT_DATA);
        }
        currentData.sort("sponsorAwardNumber",true);
        pendingData.sort("proposalNumber",true);
        currentSupportForm.setCurrentData(currentData);
        currentSupportForm.registerModels();
        currentSupportForm.setColumnData();
        pendingSupportForm.setPendingData(pendingData);
        pendingSupportForm.registerModels();
        pendingSupportForm.setColumnData();
    }
    // build the menu and menu items
    private void prepareMenus(){
        if(mnuFile == null){
            // holds the data for the File Menu
            Vector vecFileMenu = new Vector();
            // Build the File Menu...
            mnuItmInbox  = new CoeusMenuItem("Inbox", null, true, true);
            mnuItmInbox.setMnemonic('I');
            
            mnuItmSaveAs = new CoeusMenuItem("Save As...", null, true, true);
            mnuItmSaveAs.setMnemonic('A');
            
            mnuItmPrint = new CoeusMenuItem("Print...", null, true, true);
            mnuItmPrint.setMnemonic('P');
            
            mnuItmSort= new CoeusMenuItem("Sort...", null, true, true);
            mnuItmSort.setMnemonic('S');
            
            mnuItmClose= new CoeusMenuItem("Close", null, true, true);
            mnuItmClose.setMnemonic('C');
            
            mnuItmChangePassWord= new CoeusMenuItem("Change Password", null, true, true);
            mnuItmChangePassWord.setMnemonic('h');
            
            //Added for Case#3682 - Enhancements related to Delegations - Start             
            mnuItmDelegations= new CoeusMenuItem("Delegations...", null, true, true);
            mnuItmDelegations.setMnemonic('D');
            //Added for Case#3682 - Enhancements related to Delegations - End
            
            mnuItmPreference= new CoeusMenuItem("Preferences...", null, true, true);
            mnuItmPreference.setMnemonic('f');
            
            mnuItmExit= new CoeusMenuItem("Exit", null, true, true);
            mnuItmExit.setMnemonic('x');
            
            vecFileMenu.add(mnuItmInbox);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmSaveAs);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmPrint);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmSort);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmClose);
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmChangePassWord);
            //Added for Case#3682 - Enhancements related to Delegations - Start 
            vecFileMenu.add(SEPERATOR);
            vecFileMenu.add(mnuItmDelegations);
            vecFileMenu.add(SEPERATOR);
            //Added for Case#3682 - Enhancements related to Delegations - End
            vecFileMenu.add(mnuItmPreference);
            vecFileMenu.add(mnuItmExit);
            mnuFile = new CoeusMenu("File", null, vecFileMenu, true, true);
            mnuFile.setMnemonic('F');
        }
    }
    
    // Build the toolbar buttons and @return the JToolBar and create the toolbar
    private JToolBar getProposalBaseWindowToolBar(){
        JToolBar toolBar = new JToolBar();
        btnPrint = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.PRINT_ICON)),
        null, "Print");
        
        btnSaveAs = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVEAS_ICON)),
        null, "Save As");
        
        btnSort = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SORT_ICON)),
        null, "Sort");
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        
        toolBar.add(btnPrint);
        toolBar.addSeparator();
        toolBar.add(btnSaveAs);
        toolBar.addSeparator();
        toolBar.add(btnPrint);
        toolBar.addSeparator();
        toolBar.add(btnSort);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        toolBar.addSeparator();
        return toolBar;
    }
    
    
    // It is a overidden method of the internalFrame 
    public void saveActiveSheet() {
    }
    
    // This is a overidden method used to implement the saveAs functionality
    public void saveAsActiveSheet() {
        if(tbdPnCurrentAndPending.getSelectedIndex()==CURRENT_TAB_INDEX){
            SaveAsDialog saveAsDialog = new SaveAsDialog(currentSupportForm.tblCurrentSupport);
        }else if(tbdPnCurrentAndPending.getSelectedIndex()==PENDING_TAB_INDEX){
            SaveAsDialog saveAsDialog = new SaveAsDialog(pendingSupportForm.tblPendingSupport);
        }
    }
    
    //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - Start
    
    public static void saveAsActiveSheetCurrent() {    
        SaveAsDialog saveAsDialog = new SaveAsDialog(currentSupportForm.tblCurrentSupport);        
    }
    
     public static void saveAsActiveSheetPending() {  
        SaveAsDialog saveAsDialog = new SaveAsDialog(pendingSupportForm.tblPendingSupport);
    }
     
    public static void saveAsActiveSheetBoth() {         
        hmCurrentPendingReport = new HashMap();
        hmCurrentPendingReport.put(CURRENT_REPORT, currentSupportForm.tblCurrentSupport);
        hmCurrentPendingReport.put(PENDING_REPORT, pendingSupportForm.tblPendingSupport); 
    
        SaveAsDialog saveAsDialog = new SaveAsDialog(hmCurrentPendingReport);
    }
    //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - End
    
    /** Getter for property reportData.
     * @return Value of property reportData.
     *
     */
    public Hashtable getReportData() {
        return reportData;
    }
    
    /** Setter for property reportData.
     * @param reportData New value of property reportData.
     *
     */
    public void setReportData(Hashtable reportData) {
        this.reportData = reportData;
    }
    
    // Load the menus from MDIForm
    private void loadMenus() {
        mdiForm.getCoeusMenuBar().remove(0);
        mdiForm.getCoeusMenuBar().add(mnuFile, 0);
    }
    
    // UnLoad the menus from MDIForm
    private void unloadMenus() {
        mdiForm.getCoeusMenuBar().remove(mnuFile);
    }
    
    // Activate the internalFrame based on the actionevent. when it is
    //activated load the menus
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated(e);
        loadMenus();
    }
    
    // Activate the internalFrame based on the actionevent. when it is
    //deActivated unLoad the menus
    public void internalFrameDeactivated(InternalFrameEvent e) {
        unloadMenus();
        super.internalFrameDeactivated(e);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            if(source.equals(btnClose) || source.equals(mnuItmClose)){
                blockEvents(true);
                closeForm();
                blockEvents(false);
            }else if(source.equals(mnuItmInbox)){
                blockEvents(true);
                showInbox();
                blockEvents(false);
            }else if(source.equals(btnSaveAs) || source.equals(mnuItmSaveAs)){
                //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - Start
                showCurrentPendingSelection();                               
               // blockEvents(true);
               // saveAsActiveSheet();
               // blockEvents(false);
               //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - End
                
            //Added for Case#3682 - Enhancements related to Delegations - Start
            }else if(source.equals(mnuItmDelegations)){
                blockEvents(true);
                displayUserDelegation();
                blockEvents(false);
            //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(mnuItmPreference)){
                blockEvents(true);
                showPreferences();
                blockEvents(false);
            }else if(source.equals(mnuItmExit)){
                blockEvents(true);
                exitApplication();
                blockEvents(false);
            }else if(source.equals(mnuItmChangePassWord)){
                blockEvents(true);
                showChangePassword();
                blockEvents(false);
            }else if(source.equals(mnuItmPrint)|| source.equals(btnPrint)){
                blockEvents(true);
                printCurrentAndPending();
                blockEvents(false);
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("funcNotImpl_exceptionCode.1100"));
            }
        }catch (CoeusClientException coeusClientException){
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    
    //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - Start       
     private void showCurrentPendingSelection(){
        if(currentpendingReportform == null) {
            currentpendingReportform = new CurrentPendingReportForm(mdiForm,true);
        }
        currentpendingReportform.display();
    }
    //COEUSQA-1686 : Add additional fields to the Current  Pending Support Schema - End
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Displays Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    
    // Added by chandra to print the CurrentAndPending Report Screen
    
    private void printCurrentAndPending() throws CoeusClientException{
        if(tbdPnCurrentAndPending.getSelectedIndex()==CURRENT_TAB_INDEX){
            getPDFUrl(CURRENT_TAB_INDEX);
        }else if(tbdPnCurrentAndPending.getSelectedIndex()==PENDING_TAB_INDEX){
           getPDFUrl(PENDING_TAB_INDEX);
        }
    }
   
    /** Communicate with the server and write the pdf based on selected tab. If the 
     *selected tab is CurrentSuportReport, write Current Support else write
     *Pending support
     */
    private boolean getPDFUrl(int selectedTab) throws CoeusClientException {
        RequesterBean requesterBean = new RequesterBean();
        Hashtable hashtable = new Hashtable();
        if(selectedTab==CURRENT_TAB_INDEX){
            requesterBean.setId("Proposal/CurrentReport");
            hashtable.put("DATA", currentData);
            hashtable.put("REPORT_TYPE", new Character('L'));
            //requesterBean.setDataObjects(currentData);
//            requesterBean.setFunctionType(PRINT_CURRENT);
            
        }else if(selectedTab == PENDING_TAB_INDEX){
            requesterBean.setId("Proposal/PendingReport");
            hashtable.put("DATA", pendingData);
            hashtable.put("REPORT_TYPE", new Character('O'));
            //requesterBean.setDataObjects(pendingData);
//            requesterBean.setFunctionType(PRINT_PENDING);
        }
//        requesterBean.setId(headerValue);
        
        //For Streaming
        //case 3385 start
//        hashtable.put("PERSON_NAME", CoeusGuiConstants.getMDIForm().getUserId());
        hashtable.put("PERSON_NAME", headerValue);
        //case 3385 end

        requesterBean.setDataObject(hashtable);
        requesterBean.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm = new AppletServletCommunicator(PRINT_SERVLET, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(responderBean == null) {
            //Could not contact server.
            throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
        }
        if (!responderBean.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return false;
        }
        String url = (String)responderBean.getDataObject();
        url = url.replace('\\', '/');
//        AppletContext coeusContext = CoeusGuiConstants.getMDIForm().getCoeusAppletContext();
        try{
//            URL templateURL = new URL(CoeusGuiConstants.CONNECTION_URL + url);
//            if(coeusContext != null){
//                coeusContext.showDocument( templateURL, "_blank" );
//            }else{
//                javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                bs.showDocument(templateURL);
//            }
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
            return true;
        } catch (java.net.MalformedURLException me) {
            me.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+me.getMessage());
            return false;
        }
        catch (Exception usEx) {
            usEx.printStackTrace();
            CoeusOptionPane.showErrorDialog(""+usEx.getMessage());
            return false;
        }
    }// End getPDFUrl(...) method.
    
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
        if (answer == javax.swing.JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }
    
    // To specify the user preferences who are logged in with user id and user name
    private void showPreferences(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
    // Used to show the inbox details
    private void showInbox(){
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
    // close the internalFrame
    private void closeForm(){
        this.doDefaultCloseAction();
    }
    
    /** Getter for property headerValue.
     * @return Value of property headerValue.
     *
     */
    public java.lang.String getHeaderValue() {
        return headerValue;
    }
    
    /** Setter for property headerValue.
     * @param headerValue New value of property headerValue.
     *
     */
    public void setHeaderValue(java.lang.String headerValue) {
        this.headerValue = headerValue;
        currentSupportForm.setHeaderValue(headerValue);
        pendingSupportForm.setHeaderValue(headerValue);
    }
    
    // Used to close the internal frame
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
        if(closed) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(javax.swing.JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /** closes this window. */
    private void close() {
        mdiForm.removeFrame(CoeusGuiConstants.CURRENT_AND_PENDING_SUPPORT);
        closed = true;
        this.doDefaultCloseAction();
    }
    
    // Utility method used to get the hour glass Icon untill the process complete
    public void blockEvents(boolean block) {
        if(blockingGlassPane == null) {
            blockingGlassPane = new BlockingGlassPane();
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
        }
        blockingGlassPane.block(block);
    }
}
