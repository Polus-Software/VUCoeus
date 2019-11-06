/*
 * InboxDetailForm.java
 *
 * Created on December 24, 2003, 12:58 PM
 */

/* PMD check performed, and commented unused imports and variables on 25-SEPTEMBER-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.propdev.gui;

//java imports
import edu.mit.coeus.bean.AuthorizationBean;
import edu.mit.coeus.search.bean.SearchInfoHolderBean;
import edu.mit.coeus.search.gui.CoeusSearch;
//import edu.mit.coeus.subcontract.bean.SubContractAmountReleased;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.controller.SubcontractBaseWindowController;
import edu.mit.coeus.utils.query.AuthorizationOperator;
import javax.swing.*;
//import javax.swing.JComponent;
import java.awt.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
import java.util.*;
import javax.swing.event.*;
//java.beans.* packg is used to support the addVetoable change listener event while closing
import java.beans.*;
import java.awt.event.ActionListener;
//import javax.swing.event.InternalFrameEvent;


//coeus imports
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.propdev.bean.InboxBean;
import edu.mit.coeus.propdev.bean.MessageBean;
//import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.propdev.gui.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.gui.ProtocolDetailForm;

import edu.mit.coeus.utils.saveas.SaveAsDialog;



/**This form is used to show tabbed panes for resolved and unresolved.
 *
 * @author  raghusv
 */
public class InboxDetailForm extends edu.mit.coeus.gui.CoeusInternalFrame implements
ActionListener,ListSelectionListener{//Modified for Case #1828
    
    /** Creates a new instance of InboxDetailForm */
    private CoeusAppletMDIForm mdiForm;
    
    private String PROPOSAL_ACTION_SERVLET="/ProposalActionServlet";
    
    //Code added for princeton enhancement case#2802
    private static final String AUTH_SERVLET = "/AuthorizationServlet";    
    
    private static final String SUBCONTRACT_SERVLET = "/SubcontractMaintenenceServlet";    
    
    private CoeusToolBarButton btnDelete,btnChangeStatus,btnRefresh,btnModifyInbox,btnDisplayInbox,btnCloseInbox;
    
    
    private String usrName;
    
    /** */
    public InboxForm unresForm;
    
    public InboxForm resForm;
    
    private CoeusMenuItem Refresh,propModify,propDisplay,propSummary,delMessage,changeStatus;
    
    private JTabbedPane tbdPnInbox;
    
    //This vector holds all the inboxbeans that have changed and need to be send to server
    private Vector saveInboxVector=new Vector();
    
    //private String proposalNum;
    
    private final String SEPERATOR="seperator";
    
    //Stores the RIGHT ID which is required for checking user rights to modify proposal
    private final String MODIFY_RIGHT="MODIFY_PROPOSAL";
    
    //Stores the RIGHT ID which is required for checking user rights to view proposal
    private final String VIEW_RIGHT="VIEW_PROPOSAL";
    
    
    private final String VIEW_ANY_PROPOSAL_RIGHT="VIEW_ANY_PROPOSAL";
    
    private final String MODIFY_NARRATIVE_RIGHT="MODIFY_NARRATIVE";
    
    private final String MODIFY_BUDGET_RIGHT="MODIFY_BUDGET";
    
    private final String MODIFY_ANY_PROPOSAL_RIGHT="MODIFY_ANY_PROPOSAL";
    
    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //private boolean opened=false;
    
    private ProposalDetailForm mainProposal;
    
    //This vector holds all the inboxbean objects retrieved from the server side.
    private Vector inputInboxVector;
    
    private boolean saveRequired = false;
    
    private int selectedRow;
    
    //Coeus enhancement Case #1828 :step 1 : start
    private int moduleCode=0;
    boolean isDeleteOrRefreshOrChangeStatus= false;
    //Coeus enhancement Case #1828 :step 1 : end
    
    //Added for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
//    private static final int  PROPOSAL_DEVELOPMENT_MODULE = 3;
//    private static final int  SUBCONTRACT_MODULE = 4;       
//    private static final int  PROTOCOL_MODULE = 7;
    private final char CHECK_IF_EDITABLE = 'C';
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    //COEUSDEV-299 : End
    //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -start
    private static final char GET_MESSAGE_FOR_INBOX = 'g';
    //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -end            
                  
    /**
     * @param mdiForm
     */
    public InboxDetailForm(CoeusAppletMDIForm mdiForm ) {
        super( "Inbox", mdiForm,LIST_MODE );
        coeusMessageResources = CoeusMessageResources.getInstance();
        try{
            this.mdiForm = mdiForm;
            this.mdiForm=mdiForm;
            usrName=mdiForm.getUserName();
            getContentPane().add( inboxPanel() );
            initComponents();
            mdiForm.putFrame("Inbox", this);
            mdiForm.getDeskTopPane().add(this);
            this.setSelected(true);
            this.setVisible(true);
            setListeners();
        }catch(Exception e){
            //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            CoeusOptionPane.showInfoDialog(e.getMessage());
            //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - end
            e.printStackTrace();
        }
    }
    
    private void setListeners(){
        //Coeus enhancement Case #1828 :step 2 : start
        int paneIndex=tbdPnInbox.getSelectedIndex();
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
        //Setting the list selection listener while initializing the inbox components
//        if( paneIndex==0 ){
//           unresForm.tblInbox.getSelectionModel().addListSelectionListener(this);
//        }
//        }else if( paneIndex == 1){
//            resForm.tblInbox.getSelectionModel().addListSelectionListener(this);
//        }
        if(unresForm != null){
            unresForm.tblInbox.getSelectionModel().addListSelectionListener(this);
            
        }
        if(resForm != null){
            resForm.tblInbox.getSelectionModel().addListSelectionListener(this);
        }
        //COEUSDEV-299 : End
        //Coeus enhancement Case #1828 :step 2 : end
        //Code added for Bug fix that Highlighted color is not coming while opening inbox.
        unresForm.setUnresolved(true);
        tbdPnInbox.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent ce){
                JTabbedPane pn = (JTabbedPane)ce.getSource();
                int selectedTab = pn.getSelectedIndex();
                //pn.getTitleAt(0).get
                switch(selectedTab){
                    case 0:
                        //case 2306 -start
                        unresForm.setUnresolved(true);
                        //case 2306 -End
                        unresForm.showRowMessage();
                        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
                        //modify and display toolbar button and menuitem's are enabled/disabled based on the module selected
                        setModuleCode();
                        enableModifyDisplay();
                        //COEUSDEV-299 : End
                    break;
                    
                    case 1:
                        //case 2306 -start
                        unresForm.setUnresolved(false);
                        //case 2306 -End
                        resForm.showRowMessage();
                        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
                        //modify and display toolbar button and menuitem's are enabled/disabled based on the module selected
                        setModuleCode();
                        enableModifyDisplay();
                        //COEUSDEV-299 : End
                    break;
                }
            }
        });
        
        this.addVetoableChangeListener(new VetoableChangeListener(){
            
            private int vecSaveSize = saveInboxVector.size();
            
            public void vetoableChange(PropertyChangeEvent pce)
            throws PropertyVetoException {
                //saveRequired = true;
                if (pce.getPropertyName().equals(
                JInternalFrame.IS_CLOSED_PROPERTY) ) {
                    boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
                    if( changed && saveRequired ) {
                        try {
                            close();
                        } catch ( Exception e) {
                            
                            if(!( e.getMessage().equals(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130")) )){
                                CoeusOptionPane.showInfoDialog(e.getMessage());
                            }
                            throw new PropertyVetoException(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130"),null);
                        }
                    }else{
                        //mdiForm.removeFrame("Inbox");
                        try{
                            mdiForm.removeFrame("Inbox");
                        }catch(Exception exc){
                            
                        }
                    }
                }
                
            }
        });
        setModuleCode();
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
        //modify and display toolbar button and menuitem's are enabled/disabled based on the module selected
//        if(moduleCode !=3) {
//            propModify.setEnabled(false);
//            propDisplay.setEnabled(false);
//        }
       enableModifyDisplay();
       //COEUSDEV-299 : End
        
    }
    
    
    private boolean isSaveRequired(){
        int size;
        if( saveInboxVector != null){
            size = saveInboxVector.size();
            if(size > 0){
                return true;
            }
        }
        return false;
    }
    
    private void initComponents(){
        try{
            setFrameToolBar(getInboxToolBar());
            this.setFrameMenu(getViewMenu());
            this.setFrameIcon(mdiForm.getCoeusIcon());
            //Coeus enhancement Case #1828 :step 3 : start
            setModuleCode();
            //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            //modify and display toolbar button and menuitem's are enabled/disabled based on the module selected
//            if(moduleCode != 3) {
//                btnModifyInbox.setEnabled(false);
//                btnDisplayInbox.setEnabled(false);
//            }
            enableModifyDisplay();
            //COEUSDEV-299 : End
            //Coeus enhancement Case #1828 :step 3 : end
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private CoeusMenu getViewMenu() {
        CoeusMenu mnuView = null;
        Vector fileChildren = new Vector();
        
        Refresh = new CoeusMenuItem("Refresh", null, true, true);
        Refresh.setMnemonic('h');
        Refresh.addActionListener(this);
        
        fileChildren.add(Refresh);
        
        mnuView = new CoeusMenu("View", null, fileChildren, true, true);
        mnuView.setMnemonic('V');
        return mnuView;
    }
    
    private CoeusMenu getActionMenu() {
        CoeusMenu mnuAction = null;
        Vector fileChildren = new Vector();
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start                
//        propModify = new CoeusMenuItem("Modify Proposal...", null, true, true);
        propModify = new CoeusMenuItem("Modify", null, true, true);
        //COEUSDEV-299 : End
        propModify.setMnemonic('M');
        propModify.addActionListener(this);
        
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start                    
//        propDisplay = new CoeusMenuItem("Display Proposal", null, true, true);
        propDisplay = new CoeusMenuItem("Display", null, true, true);
        //COEUSDEV-299 : End
        propDisplay.setMnemonic('D');
        propDisplay.addActionListener(this);
        
        propSummary = new CoeusMenuItem("Proposal Summary", null, true, true);
        propSummary.setMnemonic('S');
        propSummary.addActionListener(this);
        
        delMessage = new CoeusMenuItem("Delete Message...", null, true, true);
        delMessage.setMnemonic('l');
        delMessage.addActionListener(this);
        
        changeStatus = new CoeusMenuItem("Change Status", null, true, true);
        changeStatus.setMnemonic('g');
        changeStatus.addActionListener(this);
        
        fileChildren.add(propModify);
        fileChildren.add(propDisplay);
        fileChildren.add(propSummary);
        fileChildren.add(SEPERATOR);
        fileChildren.add(delMessage);
        fileChildren.add(changeStatus);
        
        mnuAction = new CoeusMenu("Action", null, fileChildren, true, true);
        mnuAction.setMnemonic('A');
        return mnuAction;
    }
    
    /**
     * @param e
     */
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated( e );
        mdiForm.getCoeusMenuBar().add(getActionMenu(),2);
        mdiForm.getCoeusMenuBar().validate();
    }
    
    /**
     * @param e
     */
    public void internalFrameDeactivated(InternalFrameEvent e) {
        mdiForm.getCoeusMenuBar().remove( 2 );
        super.internalFrameDeactivated( e );
        mdiForm.getCoeusMenuBar().validate();
        mdiForm.getCoeusMenuBar().revalidate();
    }
    
    private JToolBar getInboxToolBar() {
        
        JToolBar toolbar = new JToolBar();
        //
        btnDelete = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DELETE_ICON)),
        null, "Delete the selected message");
        
        btnChangeStatus = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.STATUS_ICON)),
        null, "Change the Status");
        
        btnRefresh = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.REFRESH_ICON)),
        null, "Refresh the view");
        
        btnModifyInbox = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EDIT_ICON)),
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start                
//        null, "Modify the selected Proposal ");
         null, "Modify");
        //COEUSDEV-299 : End
        
        btnDisplayInbox = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DISPLAY_ICON)),
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start                
//        null, "Display the selected Proposal");
         null, "Display");
        //COEUSDEV-299 : End
        
        btnCloseInbox = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        btnDelete.addActionListener(this);
        btnChangeStatus.addActionListener(this);
        btnRefresh.addActionListener(this);
        btnModifyInbox.addActionListener(this);
        btnDisplayInbox.addActionListener(this);
        btnCloseInbox.addActionListener(this);
        
        toolbar.add(btnDelete);
        toolbar.add(btnChangeStatus);
        toolbar.addSeparator();
        toolbar.add(btnRefresh);
        toolbar.addSeparator();
        toolbar.add(btnModifyInbox);
        toolbar.add(btnDisplayInbox);
        toolbar.addSeparator();
        toolbar.add(btnCloseInbox);
        toolbar.setFloatable(false);
        return toolbar;
    }
    
    //this method to get the full vector without filtering from the server side
    private Vector getInboxData() throws Exception {
        Vector dataObjects = null;
        //String userName = mdiForm.getUserName();
        //System.out.println("loggedin User Name:"+usrName);
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        PROPOSAL_ACTION_SERVLET;
        //System.out.println("In getInboxData function");
        // connect to the database and get the formData for the given organization id
        RequesterBean request = new RequesterBean();
        request.setFunctionType( 'B');
        request.setDataObject(usrName );
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            // response.setMessage(coeusMessageResources.parseMessageKey(
            // "server_exceptionCode.1000"));
        }
        if (response.isSuccessfulResponse()) {
            dataObjects = response.getDataObjects();
        } else {
            if (response.isLocked()) {
                
            }else {
                throw new Exception(response.getMessage());
            }
        }
        //System.out.println("IN GETINBOXDATA TO CHECK THE SIZE OF DATAOBJECTS"+ dataObjects.size() );
        return dataObjects;
    }
    
    //used to save the data at the server side
    private void updateInboxData(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL +
        PROPOSAL_ACTION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType( 'J');
        request.setDataObjects(saveInboxVector);
        AppletServletCommunicator comm
        = new AppletServletCommunicator( connectTo, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
        }
        if (response.isSuccessfulResponse()) {
            //System.out.println("The data is updated to the database");
        } else {
            if (response.isLocked()) {
            }else {
                //throw new Exception(Exception e);
            }
        }
        saveInboxVector.clear();
    }
    
    //This method is used to get the vector of data depending on the parameter if pass argument as 'R' gets all the resolved data vector and if the
    //the param is 'U' then gets the all Unresolved vector.
    private Vector getFilteredInbox(char type){
        
        Vector InboxResAndUnres=null;
        try{
            //InboxResAndUnres=getInboxData();
            InboxResAndUnres=inputInboxVector;
            if(InboxResAndUnres.size()<=0){
                return null;
            }
            //System.out.println("The size of the vector is   "+InboxResAndUnres.size());
        }catch(Exception e){
        }
        Vector inboxRes=new Vector();
        Vector inboxUnres=new Vector();
        InboxBean inboxBean=new InboxBean();
        if((InboxResAndUnres != null) &&
        (InboxResAndUnres.size() > 0)){
            int InboxSize = InboxResAndUnres.size();
            for(int index = 0; index < InboxSize; index++){
                inboxBean = (InboxBean)
                InboxResAndUnres.get(index);
                if(inboxBean.getOpenedFlag()=='Y'){
                    inboxRes.addElement(inboxBean);
                }else if( inboxBean.getOpenedFlag()=='N'){
                    inboxUnres.addElement(inboxBean);
                }
            }
            if(type=='U'){
                return inboxUnres;
            }else if(type=='R'){
                return inboxRes;
            }
        }
        return null;
    }
    
    private JComponent inboxPanel() throws Exception{
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BorderLayout());
        pnlForm.add( createForm(), BorderLayout.CENTER );
        pnlForm.setLocation(200, 200);
        return pnlForm;
    }
    
    /** This method constructs and returns tabbed pane having two tabs one for unresolved pane and the
     * other for resolved pane with the data set for it.
     * @throws Exception
     * @return
     */
    public JTabbedPane createForm() throws Exception{
        
        tbdPnInbox = new CoeusTabbedPane(CoeusTabbedPane.CTRL_T);
        tbdPnInbox.setFont( CoeusFontFactory.getNormalFont() );
        try{
            //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -start
            //inputInboxVector = getInboxData();
            //Added for the case# COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -end
        }catch(Exception exc){
        }       
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
//        unresForm=new InboxForm(getFilteredInbox('U'),mdiForm);
//        resForm=new InboxForm(getFilteredInbox('R'),mdiForm);
        Vector unresolvedMessages = getReslAndUnReslMessages("INBOX_UNRESOLVED");
        Vector resolvedMessages = getReslAndUnReslMessages("INBOX_RESOLVED");
        Vector  unResolDatas = null;
        Vector unResolDisplayLabels = null;
        Vector ResolDatas = null;
        Vector ResolDisplayLabels = null;
        if(unresolvedMessages!= null && unresolvedMessages.size()>0){
            unResolDatas = (Vector)unresolvedMessages.get(0);
            unResolDisplayLabels = (Vector)unresolvedMessages.get(1);
        }
        if(resolvedMessages!= null && resolvedMessages.size()>0){
            ResolDatas = (Vector)resolvedMessages.get(0);
            ResolDisplayLabels = (Vector)resolvedMessages.get(1);
        }
        Vector vec = new Vector();
        if(unResolDatas != null){
            unresForm=new InboxForm(unResolDatas,unResolDisplayLabels,mdiForm);
        }else{
            unresForm=new InboxForm(vec,vec,mdiForm);
        }
        if(ResolDatas != null){
            resForm=new InboxForm(ResolDatas,ResolDisplayLabels,mdiForm);
        }else{
            resForm=new InboxForm(vec,vec,mdiForm);
        }
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox -  end        
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        
        //constructing the unresolved tabbed pane.
        JPanel pnlUnresolved = new JPanel(layout);
        pnlUnresolved.add( unresForm );
        JScrollPane jscrPnInbox = new JScrollPane();
        jscrPnInbox.setViewportView( pnlUnresolved );
        
        //constructing the resolved tabbed pane
        JPanel pnlResolved = new JPanel(layout);
        pnlResolved.add( resForm );
        JScrollPane jscrPnInbox1 = new JScrollPane();
        jscrPnInbox1.setViewportView( pnlResolved );

        tbdPnInbox.setFont( CoeusFontFactory.getNormalFont() );
        tbdPnInbox.addTab("Unresolved",jscrPnInbox);
        tbdPnInbox.addTab("Resolved",jscrPnInbox1);
        if(unResolDatas  == null){
            //disable tab
            tbdPnInbox.setEnabledAt(0, false);
        }
        if(ResolDatas  == null){
            //disable tab
            tbdPnInbox.setEnabledAt(1, false);
        }
        //tbdPnInbox.getComponentAt(1).setFont( CoeusFontFactory.getLabelFont() );
        return tbdPnInbox;
        
    }
    
    /**
     * @param ae
     */
    public void actionPerformed(java.awt.event.ActionEvent ae)  {
        Object source = ae.getSource();
        try{
            
            if(source.equals(btnDelete) || source == delMessage){
                //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
                //After deletion of a message modify and display toolbar button and menuitem's are enabled/disabled based on the module selected
//                setModuleCode();//Coeus enhancement Case #1828
                deleteMessage();
                setModuleCode();
                enableModifyDisplay();
                //COEUSDEV-299 : End
            }else if(source.equals(btnChangeStatus) || source == changeStatus ){
                //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
//                setModuleCode();//Coeus enhancement Case #1828
                changeStatus();
                setModuleCode();
                enableModifyDisplay();
                //COEUSDEV-299 : End
            }else if(source.equals(btnRefresh) || source.equals(Refresh) ){
                //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
//                setModuleCode();//Coeus enhancement Case #1828
                refreshView();
                setModuleCode();                
                enableModifyDisplay();
                //COEUSDEV-299 : End
            }else if(source.equals(btnCloseInbox)){
//                System.out.println("IN action performed event ");
                //int size = saveInboxVector.size();
                close();
                //Coeus enhancement Case #1828 :step 4 : start
            }else if(source.equals(btnModifyInbox) || source.equals(propModify) ){
                setModuleCode();
                showDetailForm('M');
            }else if(source.equals(btnDisplayInbox) || source.equals(propDisplay)){
                setModuleCode();
                showDetailForm('D');
                //Coeus enhancement Case #1828 :step 4 : end
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                "funcNotImpl_exceptionCode.1100"));
            }
        }catch (PropertyVetoException propertyVetoException) {
            //Its Becoz of closing. so no need to print.
        }catch(CoeusException ex){
            // added by manoj to display action messages as information messages
            CoeusOptionPane.showDialog(new CoeusClientException(ex));
        }
        catch ( Exception e) {
            e.printStackTrace();
            if( ! (e.getMessage().equals("protoDetFrm_exceptionCode.1130")) ){
                String msg= coeusMessageResources.parseMessageKey(e.getMessage());
                CoeusOptionPane.showInfoDialog(msg);
            }
            //            if(!( e.getMessage().equals(
            //            coeusMessageResources.parseMessageKey(
            //            "protoDetFrm_exceptionCode.1130")) )){
            //                CoeusOptionPane.showInfoDialog(e.getMessage());
            //            }
        }
    }
    
    //Coeus enhancement Case #1828 :step 5 : start
    private void showDetailForm(char funcType)throws Exception {
        int selRows = unresForm.tblInbox.getSelectedRowCount();
        if(selRows > 1){
            String msg = "";
            if(funcType == 'M'){
                msg = coeusMessageResources.parseMessageKey("inboxDetail_exceptionCode.1117");
            }else if(funcType == 'D'){
                msg = coeusMessageResources.parseMessageKey("inboxDetail_exceptionCode.1118");
            }
            CoeusOptionPane.showInfoDialog(msg);
            return;
        }
        String itemNum=null;
        int paneIndex=tbdPnInbox.getSelectedIndex();
        if( paneIndex==0 ){
            itemNum = unresForm.getItemNumber();
        }else if( paneIndex == 1){
            itemNum = resForm.getItemNumber();
        }
        /** Added by chandra - Bug fix #990
         *start 6th July 2004
         */
        if( paneIndex==0 ){
            int selRow = unresForm.tblInbox.getSelectedRow();
            String propNum = (String)unresForm.tblInbox.getValueAt(selRow, 4);

            if(propNum.trim().equals("")){
                return ;
            }
        }
        //Added by chandra - 6th July 2004 - End 990
        try{
            checkDuplicateShow(itemNum ,funcType);
        }catch(Exception exc){
            throw new Exception(exc.getMessage());
        }
    }
    //Coeus enhancement Case #1828 :step 5 : end
    
    private void setModuleCode() {
        int paneIndex=tbdPnInbox.getSelectedIndex();
        if( paneIndex==0 ){
            moduleCode=unresForm.getModuleCode();
        }else if( paneIndex == 1){
            moduleCode = resForm.getModuleCode();
        }
    }
    
    //Coeus enhancement Case #1828 :step 6 : start
    private String getItemNumber(){
        String itemNum = null;
        int paneIndex=tbdPnInbox.getSelectedIndex();
        if( paneIndex==0 ){
            itemNum = unresForm.getItemNumber();
        }else if( paneIndex == 1){
            itemNum = resForm.getItemNumber();
        }
        return itemNum;
    }
    //Coeus enhancement Case #1828 :step 6 : end
    
    private boolean canModifyProposal(){
        
        int statusCode = getStatus();
        String msg ;
        boolean value = true;
        //1:In progress   2:Approval In Progress  3:Rejected  4:Approved  5:Submitted  6:Post Submission Approval
        //7:Post Submission Rejection.
        if( statusCode == 2 ){
            msg = coeusMessageResources.parseMessageKey("inboxDetail_exceptionCode.1110");//Approval in progress
            CoeusOptionPane.showInfoDialog(msg);
            value = false;
        }else if(  statusCode == 5 ){
            msg = coeusMessageResources.parseMessageKey("inboxDetail_exceptionCode.1111");//Submitted
            CoeusOptionPane.showInfoDialog(msg);
            value = false;
        }else if( statusCode == 4 ){
            msg = coeusMessageResources.parseMessageKey("inboxDetail_exceptionCode.1112");//Approved
            CoeusOptionPane.showInfoDialog(msg);
            value = false;
        }else if( statusCode == 6 ){
            msg = coeusMessageResources.parseMessageKey("proposal_DtlForm_exceptionCode.7114");//Post Submission Approval
            CoeusOptionPane.showInfoDialog(msg);
            value = false;
        }else if( statusCode == 7 ){
            msg = coeusMessageResources.parseMessageKey("proposal_DtlForm_exceptionCode.7115");//Post Submission Rejection
            CoeusOptionPane.showInfoDialog(msg);
            value = false;
        }else if( statusCode == -1  ){
//            System.out.println("There is nothing in the selected row");
            value = false;
        }
        return value;
    }
    
    /** This method used to get the status data by looking at the tabbed pane and getting data from that
     *respective instance
     *@return status
     */
    //private int String getStatus(){
    private int getStatus(){
        //String status = null;
        int statusCode = -1;
        int paneIndex=tbdPnInbox.getSelectedIndex();
        if( paneIndex==0 ){
            statusCode = unresForm.getStatusCode();
            //status = unresForm.getStatus();
        }else if( paneIndex == 1){
            statusCode = resForm.getStatusCode();
            //status = resForm.getStatus();
        }
        return statusCode;
    }
    
    
    /**
     * This method is used to check whether the given Proposal number is already
     * opened in the given mode or not.
     */
    //Coeus enhancement Case #1828 :step 7 : start
    private void checkDuplicateShow(String refId, char mode)throws Exception {
        boolean duplicate = false;
        String moduleId = null;
        if(moduleCode == 1) {
           moduleId= CoeusGuiConstants.AWARD_FRAME_TITLE;
        }else if(moduleCode ==2) {
            moduleId = CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE;
        }else if(moduleCode ==3) {
            moduleId = CoeusGuiConstants.PROPOSAL_DETAILS_FRAME_TITLE;
        }else if(moduleCode == 7) {
            moduleId= CoeusGuiConstants.PROTOCOL_FRAME_TITLE;
        }else if(moduleCode == 9) {
            moduleId= CoeusGuiConstants.IACUC_PROTOCOL_FRAME_TITLE;
        }
        //Code added for princeton enhancement case#2802
        else if(moduleCode == 4) {
            moduleId= CoeusGuiConstants.CORRECT_SUBCONTRACT_BASE_WINDOW;
        }
        try{
            refId = refId == null ? "" : refId;
            duplicate = mdiForm.checkDuplicate(
            moduleId, refId, mode );
        }catch(Exception e){
            // Exception occured.  Record may be already opened in requested mode
            //   or if the requested mode is edit mode and application is already
            //   editing any other record.
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            // try to get the requested frame which is already opened
            CoeusInternalFrame frame = mdiForm.getFrame(
            moduleId,refId);
            if(frame == null){
                // if no frame opened for the requested record then the
                //   requested mode is edit mode. So get the frame of the
                //   editing record.
                frame = mdiForm.getEditingFrame(
                moduleId );
            }
            if (frame != null){
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        
        if(moduleCode==1) {
            
        }else if(moduleCode==2) {
            
        }else if(moduleCode==ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
            try {
                showProposal(mode, refId);
            }catch(Exception ex){
                throw new Exception(ex.getMessage());
            }
        }else if(moduleCode==ModuleConstants.PROTOCOL_MODULE_CODE) {
            //Added for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            //To show protocol
            try {
                showProtocol(mode, refId);
            }catch(Exception ex){
                throw new Exception(ex.getMessage());
            }
           //COEUSDEV-299 : End
        }else if(moduleCode==ModuleConstants.IACUC_MODULE_CODE) {
            //Added for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
            //To show protocol
            try {
                showIACUCProtocol(mode, refId);
            }catch(Exception ex){
                throw new Exception(ex.getMessage());
            }
           //COEUSDEV-299 : End
        }
        //Code added for princeton enhancement case#2802
        else if(moduleCode==ModuleConstants.SUBCONTRACTS_MODULE_CODE) {
            try {
                showSubcontract(mode, refId);
            }catch(Exception ex){
                throw new Exception(ex.getMessage());
            }
        }
   }
   
    
    private void showProposal(char mode,String refId )throws Exception {
        try{
            //
            if ( mode == 'D' ){
                try {
                    if ( hasDisplayProposalRights(refId) ){
                        mainProposal = new ProposalDetailForm( mode, refId, mdiForm);
                        mainProposal.showDialogForm();
                    }else{
                        CoeusOptionPane.showInfoDialog("The user has no rights to display this proposal");
                    }
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                }
            }else if( mode == 'M' ){
                try {
                    if( canModifyProposal() ){
                        if( hasModifyProposalRights() ){
                            mainProposal = new ProposalDetailForm( mode, refId, mdiForm);
                            //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
                            //Getting the proposal lead unit from the inbox table list
                            int paneIndex=tbdPnInbox.getSelectedIndex();                
                            int selRow = -1;
                            String leadUnitNumber = "";
                            if(paneIndex == 0){
                                selRow = unresForm.tblInbox.getSelectedRow();   
                                leadUnitNumber = (String)unresForm.tblInbox.getValueAt(selRow, 8);
                            }else if(paneIndex == 1){
                                selRow = resForm.tblInbox.getSelectedRow();   
                                leadUnitNumber = (String)unresForm.tblInbox.getValueAt(selRow, 8);
                            }
                            mainProposal.setUnitNumber(leadUnitNumber);
                            //COEUSDEV-299: ENd
                            mainProposal.showDialogForm();
                        }else{
                            CoeusOptionPane.showInfoDialog("The user has no rights to modify this proposal");
                        }
                    }
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                }
            }
            
        }catch ( Exception ex) {
            ex.printStackTrace();
            try{
                if (!mainProposal.isModifiable() ) {
                     //Modified for case# 3439 to include the locking message - start
//                    String msg = coeusMessageResources.parseMessageKey(
//                    "proposal_row_clocked_exceptionCode.777777");
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(ex.getMessage(),
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                     //Modified for case# 3439 to include the locking message - end
                    if (resultConfirm == 0) {
                        showDetailForm('D');
                        //showProposalDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                //excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
        
    }
     //Coeus enhancement Case #1828 :step 7 : end
    
    private void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    //    private void modify(){
    //          showProposalDetailForm('M');
    //    }
    //
    //    private void display(){
    //           showProposalDetailForm('D');
    //    }
    
    private void close() throws Exception{
        
        if( saveRequired ){
            String msg=coeusMessageResources.parseMessageKey(
            "inboxDetail_exceptionCode.1113" );     //"Do you want to save the changes" ;
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                
                case( JOptionPane.YES_OPTION ):  updateInboxData();
                saveInboxVector.clear();
                saveRequired = false;
                mdiForm.removeFrame("Inbox");
                this.doDefaultCloseAction();
                break;
                
                case( JOptionPane.NO_OPTION ):   mdiForm.removeFrame("Inbox");
                saveInboxVector.clear();
                saveRequired = false;
                this.doDefaultCloseAction();
                break;
                
                case ( JOptionPane.CANCEL_OPTION ):
                case ( JOptionPane.CLOSED_OPTION ):
                    throw new PropertyVetoException(
                    coeusMessageResources.parseMessageKey(
                    "protoDetFrm_exceptionCode.1130"),null);
                    
            }
        }else{
            mdiForm.removeFrame("Inbox");
            this.doDefaultCloseAction();
        }
    }
    
    private void changeStatus(){
        isDeleteOrRefreshOrChangeStatus=true;
        int paneIndex=tbdPnInbox.getSelectedIndex();
        String msg;
        msg = coeusMessageResources.parseMessageKey("inboxDetail_exceptionCode.1114");
        if(paneIndex==0){
            //Modified for Case#3682 - Enhancements related to Delegations - start
//            if( ! unresForm.canDelete() ){
            if( ! unresForm.canChangeStatus() ){
            //Modified for Case#3682 - Enhancements related to Delegations - End
                CoeusOptionPane.showInfoDialog(msg);
                return ;
            }
            //Code added for princeton enhancement case#2802
            //To check the particular invoice is Approved or not
            else if(!unresForm.canResolved()){
                CoeusOptionPane.showInfoDialog(msg);
                return ;                
            }
        }else if(paneIndex==1){
            //Modified for Case#3682 - Enhancements related to Delegations - start
//            if( ! resForm.canDelete() ){
            if( ! resForm.canChangeStatus() ){
            //Modified for Case#3682 - Enhancements related to Delegations - end
                CoeusOptionPane.showInfoDialog(msg);
                return ;
            }
        }
        // Modified for COEUSDEV-612:Refresh and UI issues in inbox window in Premium - Start
        if(paneIndex==0){
            int[] selRows = unresForm.tblInbox.getSelectedRows();
            if(selRows.length>0){
                Vector data = unresForm.getRowData(selRows);
                Vector dataVector = (Vector)data.get(0);
                Vector beanVector = (Vector)data.get(1);
                resForm.addRowToTable(dataVector,beanVector);
                for(Object obj: beanVector){
                    InboxBean inbxBean = (InboxBean)obj;
                    removeDuplicate(inbxBean);
                    inbxBean.setOpenedFlag('Y');
                    inbxBean.setAcType("U");
                    saveInboxVector.add(inbxBean);
                    saveRequired = true;
                }
                resForm.refresh();
                unresForm.refresh();
            }
            //System.out.println("THE SAVE VECTOR SIZE IS "+saveInboxVector.size() );
        }else if( paneIndex == 1 ){
            int[] selRows = resForm.tblInbox.getSelectedRows();
            if(selRows.length>0){
                Vector data = resForm.getRowData(selRows);
                Vector dataVector = (Vector)data.get(0);
                Vector beanVector = (Vector)data.get(1);
                unresForm.addRowToTable(dataVector,beanVector);
                for(Object obj: beanVector){
                    InboxBean inbxBean = (InboxBean)obj;
                    removeDuplicate(inbxBean);
                    // Modified for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - Start
//                    inbxBean.setOpenedFlag('Y');
                    inbxBean.setOpenedFlag('N');
                    // Modified for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - Start
                    inbxBean.setAcType("U");
                    saveInboxVector.add(inbxBean);
                    saveRequired = true;
                }
                resForm.refresh();
                unresForm.refresh();
            }
            //System.out.println("THE SAVE VECTOR SIZE IS "+saveInboxVector.size() );
        }
        // Modified for COEUSDEV-612:Refresh and UI issues in inbox window in Premium - End
//        unresForm.tblInbox.clearSelection();
        if(unresForm.tblInbox.getModel().getRowCount()>0){
            unresForm.tblInbox.setRowSelectionInterval(0, 0);
        }
//        resForm.tblInbox.clearSelection();
        if(resForm.tblInbox.getModel().getRowCount()>0){
            resForm.tblInbox.setRowSelectionInterval(0, 0);
        }
    }
    
    private void removeDuplicate(InboxBean givenInboxBean){
        int count=saveInboxVector.size();
        //System.out.println("THE VECTOR SIZE IN removeDuplicate METHOD "+count );
        String givenMsgId=givenInboxBean.getMessageId();
        String msgId="";
        Date givenDt = (Date) givenInboxBean.getArrivalDate();
        long givenTime = givenDt.getTime();
        Date dt1;
        long presentTime;
        InboxBean inboxBean;
        if(count<=0){
            return ;
        }
        for(int i=0; i < count; i++){
            inboxBean=(InboxBean) saveInboxVector.get(i);
            msgId=inboxBean.getMessageId();
            msgId.trim();
            dt1 = inboxBean.getArrivalDate();
            presentTime = dt1.getTime();
            if(givenMsgId.equalsIgnoreCase(msgId) && givenTime == presentTime ){
                //System.out.println("message id of givenMsg is "+givenMsgId);
                //System.out.println("message id in the vector bean "+msgId);
                saveInboxVector.remove(i);
                return ;
            }
        }
    }
    
    private void refreshView(){
        isDeleteOrRefreshOrChangeStatus=true;
        Vector vecNewData=new Vector();
        int paneIndex=tbdPnInbox.getSelectedIndex();
        unresForm.emptyTable();
        try{
            //inputInboxVector = getInboxData();
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //vecNewData=getFilteredInbox('U');
            vecNewData = getReslAndUnReslMessages("INBOX_UNRESOLVED");
            if(vecNewData!= null && vecNewData.size()>0){
                vecNewData = (Vector)vecNewData.get(0);
            }
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end
            unresForm.initialSetup(vecNewData);
            resForm.emptyTable();
            // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start
            //vecNewData=getFilteredInbox('R');
            vecNewData = getReslAndUnReslMessages("INBOX_RESOLVED");
            if(vecNewData!= null && vecNewData.size()>0){
                vecNewData = (Vector)vecNewData.get(0);
            }
        // Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - en
            resForm.initialSetup(vecNewData);
            
            /*added for the row selection after we delete the row and refresh*/
            /*for bug fix:bug Id:1220 start*/
            if(selectedRow >0){
                resForm.tblInbox.setRowSelectionInterval(
                selectedRow - 1,selectedRow -1);
                resForm.tblInbox.scrollRectToVisible(
                resForm.tblInbox.getCellRect(
                selectedRow - 1  ,0, true));
                //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
                //resForm.tblInbox.setSelectionBackground(java.awt.Color.YELLOW);
                //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
            }else{
                if(resForm.tblInbox.getRowCount()>0){
                    resForm.tblInbox.setRowSelectionInterval(0,0);
                    //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
                    //resForm.tblInbox.setSelectionBackground(java.awt.Color.YELLOW);
                    //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
                    resForm.tblInbox.setColumnSelectionInterval(1,1);
                }
            }
            /* bug fix:bug Id:1220 end*/
            saveInboxVector.clear();
            
        }catch(Exception exc){
        }
    }
    
    private void deleteMessage(){
        InboxBean inboxBean;
        String msg;
        isDeleteOrRefreshOrChangeStatus=true;
         int paneIndex=tbdPnInbox.getSelectedIndex();
        if(paneIndex==0){
            if( ! unresForm.canDelete() ){
                //Modified for case#3682 - Enhancements related to Delegations - Start
                if(moduleCode == 0){
                    msg = "Cannot delete the selected Delegation";
                }else{
                    msg = coeusMessageResources.parseMessageKey(
                    "inboxDetail_exceptionCode.1115");
                }
                //Modified for case#3682 - Enhancements related to Delegations - End
                CoeusOptionPane.showInfoDialog(msg);
                return ;
            }
        }
        int rowCnt = 0;
        int noOfRows = 0;
        if(paneIndex==0){
            noOfRows = unresForm.tblInbox.getSelectedRowCount();
            rowCnt = unresForm.tblInbox.getRowCount();
        }else if(paneIndex == 1){
            noOfRows = resForm.tblInbox.getSelectedRowCount();
            rowCnt = resForm.tblInbox.getRowCount();
        }
        if(rowCnt <= 0){
            msg = coeusMessageResources.parseMessageKey(
            "adminAward_exceptionCode.1351");
            CoeusOptionPane.showInfoDialog(msg);
            return;
        }
        if(noOfRows <= 0){
            msg = coeusMessageResources.parseMessageKey(
            "search_exceptionCode.1119");
            CoeusOptionPane.showInfoDialog(msg);
            return;
        }else if(noOfRows > 1){
            msg = coeusMessageResources.parseMessageKey(
            "inboxDetail_exceptionCode.1119");
        }else{
            msg = coeusMessageResources.parseMessageKey(
            "inboxDetail_exceptionCode.1116");
        }

        int selection = CoeusOptionPane.showQuestionDialog(msg, CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
        if(selection == CoeusOptionPane.SELECTION_YES) {
            if(paneIndex== 0){
                int[] selRows = unresForm.tblInbox.getSelectedRows();
                for(int i=0;i<selRows.length;i++){
                    inboxBean = unresForm.getRowBean(selRows[i]-i);
                    inboxBean.setAcType("D");
                    MessageBean msgBean = inboxBean.getMessageBean();
                    msgBean.setAcType("D");
                    inboxBean.setMessageBean(msgBean);
                    //MessageBean msg=(MessageBean) inboxBean.getMessageBean();
                    //System.out.println("inboxBean message is "+msgBean.getMessage());
                    saveInboxVector.add(inboxBean);
                    //System.out.println("THE saveInboxVector SIZE is "+saveInboxVector.size());
                }
                if(unresForm.tblInbox.getRowCount()>0){
                    unresForm.tblInbox.setRowSelectionInterval(0,0);
                    //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
                    //unresForm.tblInbox.setSelectionBackground(java.awt.Color.YELLOW);
                    //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
                    unresForm.tblInbox.setColumnSelectionInterval(1,1);
                    selectedRow = unresForm.tblInbox.getSelectedRow();
                }
                
                updateInboxData();
                //saveInboxVector.clear();
            }else if(paneIndex == 1){
                int[] selRows = resForm.tblInbox.getSelectedRows();
                for(int i=0;i<selRows.length;i++){
                    inboxBean = resForm.getRowBean(selRows[i]-i);
                    inboxBean.setAcType("D");
                    MessageBean msgBean = inboxBean.getMessageBean();
                    //msgBean.setAcType("D");
                    inboxBean.setMessageBean(msgBean);
                    //System.out.println("inboxBean message is "+msgBean.getMessage());
                    saveInboxVector.add(inboxBean);
                    /*added for the row selection or for the setting of the focus after a row is deleted*/
                    /*for bug fix:bug Id:1220 start*/
                    int selRow = resForm.tblInbox.getSelectedRow();
                    int rowCount = resForm.tblInbox.getRowCount();
                    if(selRow == resForm.tblInbox.getRowCount()- 1){
                        if(selRow != -1)//Added for bug fix #1819
                            resForm.tblInbox.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                        selectedRow = rowCount;
                    }else{
                        if(selRow >0){
                            resForm.tblInbox.setRowSelectionInterval(
                            selRow-1,selRow-1);
                            resForm.tblInbox.scrollRectToVisible(
                            resForm.tblInbox.getCellRect(
                            selRow -1 ,0, true));
                            //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
                            //resForm.tblInbox.setSelectionBackground(java.awt.Color.YELLOW);
                            //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
                            selectedRow = selRow;
                        }else{
                            if(resForm.tblInbox.getRowCount()>0){
                                resForm.tblInbox.setRowSelectionInterval(0,0);
                                //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-start
                                //resForm.tblInbox.setSelectionBackground(java.awt.Color.YELLOW);
                                //Added for the case# COEUSQA-2529-Premium In-box: user cannot double-click to open Dev Proposal Record-end
                                resForm.tblInbox.setColumnSelectionInterval(1,1);
                                selectedRow = selRow;
                            }
                        }
                    }
                }
                /* /*for bug fix:bug Id:1220 end*/
                updateInboxData();
                //saveInboxVector.clear();
            }
            saveInboxVector.clear();
        }else {
            return ;
        }
    }
    
/*
 * This method is invoked when the user clicks Display in the Edit menu
 * of Proposal module. Checks whether the User has rights to View Proposal
 *
 * @return boolean. true indicates has rights and false indicates no rights.
 */
     
    private boolean hasDisplayProposalRights(String propsoalNumber) throws CoeusClientException{
        // check for any OSP right
     /*   boolean hasRights = false;
        String proposalNumber;
        Vector vecFnParams = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setId(mdiForm.getUserName());
        request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = Integer.parseInt(response.getId());
                if (right == 1) {
                    hasRights = true;
                }
            }
            else {
                throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        else {
            CoeusOptionPane.showErrorDialog("Could not contact server");
            //return;
        }
        // check for the MODIFY_PROPOSAL_RIGHT Case #1860
        if (!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            proposalNumber = getItemNumber();
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(proposalNumber);
            vecFnParams.addElement(MODIFY_PROPOSAL_RIGHT);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            comm.setRequest(request);
            comm.setConnectTo(connectTo);
            comm.send();
            ResponderBean responser = comm.getResponse();
            if (responser!=null){
                if (responser.isSuccessfulResponse()){
                    hasRights = ((Boolean) responser.getDataObject()).booleanValue();
                }
            }
            }
            // check for the VIEW_PROPOSAL right
            if (!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            //Coeus enhancement Case #1828 :step 8 : start
            proposalNumber = getItemNumber();
            //Coeus enhancement Case #1828 :step 8 : end
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(proposalNumber);
            vecFnParams.addElement(VIEW_RIGHT);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            comm.setRequest(request);
            /** Bug fix #1922 - start
             */
           /* comm.setConnectTo(connectTo);
            /** Bug fix #1922 - End
             */
          /*  comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                    /* bug Fix #1922 - start
                     *The Responder bean sending the Boolean Object not
                     *Integer Object
                    int right = ((Integer) response.getDataObject()).intValue();
                    if (right == 1) {
                        hasRights = true;
                    }End
                     */
             /*   }
            }*/
        
        
        
        boolean hasRights = false;
        String proposalNumber;
        Vector vecFnParams = new Vector();
        int selectedRow = unresForm.tblInbox.getSelectedRow();
        String unitNumber = (String)unresForm.tblInbox.getValueAt(selectedRow, 8);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        request.setId(mdiForm.getUserName());
        request.setFunctionType('b');
        request.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                int right = Integer.parseInt(response.getId());
                if (right == 1) {
                    hasRights = true;
                }
            }
        }
        
        /** Check if the User has VIEW_ANY_PROPOSAL in lead unit of the proposal.
         *pass the lead unit number of the selected proposal and find
         *whether right is there are not.
         *Call FN_USER_HAS_RIGHT(userId, leadUnit, VIEW_ANY_PROPOSAL) for right
         *checking
         */
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            request.setId(unitNumber);
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(unitNumber);
            vecFnParams.addElement(VIEW_ANY_PROPOSAL_RIGHT);
            request.setFunctionType('b');
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        /** If the user doesn't find the MODIFY_ANY_PROPOSAL right at the top level,
         *then pass the lead unit of the selected proposal and find
         *whether MODIFY_ANY_PROPOSAL right is there are not.
         *Call FN_USER_HAS_RIGHT(userId, leadUnit, MODIFY_ANY_PROPOSAL) for right
         *checking
         */
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(MODIFY_ANY_PROPOSAL_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        /** If the user doesn't find the right at the lead unit level ,
         *then pass the proposal number of the selected proposal and find
         *whether MODIFY_PROPOSAL right is there are not.
         *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, MODIFY_PROPOSAL) for right
         *checking
         */
        if (!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            selectedRow = unresForm.tblInbox.getSelectedRow();
            proposalNumber = unresForm.tblInbox.getValueAt(selectedRow, 4).toString();
            vecFnParams.setElementAt(proposalNumber, 1);
            vecFnParams.setElementAt(MODIFY_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        //Check if User has MODIFY_NARRATIVE for this particular proposal
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(MODIFY_NARRATIVE_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        //Check if User has MODIFY_BUDGET for this particular proposal
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(MODIFY_BUDGET_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        /** If the user doesn't find the right at the lead unit level ,
         *then pass the proposal number of the selected proposal and find
         *whether VIEW_PROPOSAL right is there are not.
         *Call FN_USER_HAS_PROP_RIGHT(userId, proposalNumber, VIEW_PROPOSAL) for right
         *checking
         */
        if(!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            vecFnParams.setElementAt(VIEW_RIGHT, 2);
            request.setDataObjects(vecFnParams);
            comm.setConnectTo(connectTo);
            comm.setRequest(request);
            comm.send();
            response = comm.getResponse();
            
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights  = ((Boolean) response.getDataObject()).booleanValue();
                }
            }
        }
        
        return hasRights;
    }
    
    private boolean hasModifyProposalRights() throws CoeusClientException {
        boolean hasRights = false;
        String proposalNumber;
        Vector vecFnParams = new Vector();
        String connectTo;
        RequesterBean request = new RequesterBean();
        if (!hasRights) {
            connectTo = CoeusGuiConstants.CONNECTION_URL + "/ProposalMaintenanceServlet";
            //Coeus enhancement Case #1828 :step 9 : start
            proposalNumber = getItemNumber();
            //Coeus enhancement Case #1828 :step 9 : end
            vecFnParams.addElement(mdiForm.getUserName());
            vecFnParams.addElement(proposalNumber);
            vecFnParams.addElement(MODIFY_RIGHT);
            request.setDataObjects(vecFnParams);
            request.setFunctionType('U');
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
            comm.setRequest(request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    hasRights = ((Boolean) response.getDataObject()).booleanValue();
                    /** Bug Fix 1922- start
                     *
                    int right = ((Integer) response.getDataObject()).intValue();
                    if (right == 1) {
                        hasRights = true;
                     
                    }*/
                } else {
                    throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
                }
            } else {
                CoeusOptionPane.showErrorDialog("Could not contact server");
                //return;
            }
        }
        return hasRights;
    }
    
    //Coeus enhancement Case #1828 :step 10 : start
    public void valueChanged(ListSelectionEvent e) {
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
        //For each and every selection module is assigned and button's and menu item's are enabled based on the selection
//        if(!isDeleteOrRefreshOrChangeStatus) {
//            setModuleCode();
//        }
        //Code modified for princeton enhancements case#2802
        //To enable the buttons for subcontract module where
        //the module code is 4.
//        if(moduleCode != 3 && moduleCode != 4) {
////        if(moduleCode != 3) {
//            btnModifyInbox.setEnabled(false);
//            btnDisplayInbox.setEnabled(false);
//            propModify.setEnabled(false);
//            propDisplay.setEnabled(false);
//        }else {
//            btnModifyInbox.setEnabled(true);
//            btnDisplayInbox.setEnabled(true);
//             propModify.setEnabled(true);
//            propDisplay.setEnabled(true);
//        }
        setModuleCode();
        enableModifyDisplay();
        //COEUSDEV-299:End
    }
    //Coeus enhancement Case #1828 :step 10 : end
    
    /**
     * code added for princeton enhancement case#2802
     * To open the Subcontract module for the given subcontract code
     * @param mode 
     * @param refId 
     * @throws java.lang.Exception 
     */
    private void showSubcontract(char mode,String refId )throws Exception {
        try{
            CoeusVector cvAmountInfo = getSubcontractAmountData(refId);
            if(cvAmountInfo == null){
                cvAmountInfo = new CoeusVector();
            }
            CoeusVector cvData = new CoeusVector();
            cvData.add(new Double(cvAmountInfo.sum("obligatedAmount")));
            cvData.add(new Double(cvAmountInfo.sum("anticipatedAmount")));
            cvData.add(new Double(0));
            cvData.add(new Double(0));            
            if ( mode == 'D' ){
                try {
                    if (authorizationCheck("VIEW_SUBCONTRACT")){
                        SubContractBean subContractBean = new SubContractBean();
                        subContractBean.setSubContractCode(refId);
                        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox  - Start
//                        SubcontractBaseWindowController subcontractBaseWindowController =
//                                new SubcontractBaseWindowController("Modify Subcontract ", CoeusGuiConstants.DISPLAY_MODE, subContractBean, cvData);
                        SubcontractBaseWindowController subcontractBaseWindowController =
                                new SubcontractBaseWindowController("Display Subcontract ", CoeusGuiConstants.DISPLAY_MODE, subContractBean, cvData);
                        //COEUSDEV-299 : End
                        subcontractBaseWindowController.display();
                    }else{
                        CoeusOptionPane.showInfoDialog("The user has no rights to display this subcontract");
                    }
                }catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                }
            }else if( mode == 'M' ){
                try {
                    if(authorizationCheck("MODIFY_SUBCONTRACT")){
                        SubContractBean subContractBean = new SubContractBean();
                        subContractBean.setSubContractCode(refId);
                        SubcontractBaseWindowController subcontractBaseWindowController =
                                new SubcontractBaseWindowController("Correct Subcontract ", CoeusGuiConstants.MODIFY_MODE, subContractBean, cvData);
                        subcontractBaseWindowController.display();
                    } else {
                        CoeusOptionPane.showInfoDialog("The user has no rights to modify this subcontract");
                    }
                } catch(CoeusClientException coeusClientException) {
                    CoeusOptionPane.showDialog(coeusClientException);
                }
            }
            
        }catch ( Exception ex) {
            ex.printStackTrace();
            try{
                if (!mainProposal.isModifiable() ) {
                    String msg = coeusMessageResources.parseMessageKey(
                            "proposal_row_clocked_exceptionCode.777777");
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                            CoeusOptionPane.OPTION_YES_NO,
                            CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        showDetailForm('D');
                        //showProposalDetails(refId);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                //excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
    }
    
    /**
     * code added for princeton enhancement case#2802
     * To check that the user has Subcontract module
     * Modify/View rights
     * @param right 
     * @return boolean
     */
    private boolean authorizationCheck(String right) {
        RequesterBean requester;
        ResponderBean responder;
        boolean subcontractRight = false;
        requester = new RequesterBean();
        Hashtable authorizations = new Hashtable();
        
        AuthorizationBean authorizationBean;
        AuthorizationOperator authorizationOperator;
        String MODIFY_SUBCONTRACT = "MODIFY_SUBCONTRACT";
        String VIEW_SUBCONTRACT = "VIEW_SUBCONTRACT";
        
        if(right != null && right.endsWith(MODIFY_SUBCONTRACT)){
            // Determine whether user has right to modify an subcontract
            authorizationBean = new AuthorizationBean();
            authorizationBean.setFunction(MODIFY_SUBCONTRACT);
            // 3587: Multi Campus Enahncements - Start
//            authorizationBean.setFunctionType("OSP");
            authorizationBean.setFunctionType("RIGHT_ID");
            // 3587: Multi Campus Enahncements - End
            authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
            authorizationOperator = new AuthorizationOperator(authorizationBean);
            authorizations.put(MODIFY_SUBCONTRACT, authorizationOperator);
        //Modified for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
//        } else if(right != null && right.endsWith(VIEW_SUBCONTRACT)){
        } else if(right != null && right.endsWith(VIEW_SUBCONTRACT)){//COEUSDEV-299: End
            // Determine whether user has right to display an subcontract
            authorizationBean = new AuthorizationBean();
            authorizationBean.setFunction(VIEW_SUBCONTRACT);
            authorizationBean.setFunctionType("RIGHT_ID");
            authorizationBean.setPerson(CoeusGuiConstants.getMDIForm().getUserId());
            authorizationOperator = new AuthorizationOperator(authorizationBean);
            authorizations.put(VIEW_SUBCONTRACT, authorizationOperator);
        }
        
        requester.setAuthorizationOperators(authorizations);
        requester.setIsAuthorizationRequired(true);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL + AUTH_SERVLET, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            authorizations = responder.getAuthorizationOperators();
            subcontractRight = ((Boolean)authorizations.get(right)).booleanValue();
        }else{
            CoeusOptionPane.showInfoDialog(responder.getMessage());
        }
        return subcontractRight;
    }
    
    /**
     * code added for princeton enhancement case#2802
     * To get the Subcontract Amount info datas for the given subcontract code.
     * @param subcontractcode 
     * @return CoeusVector
     */
    public CoeusVector getSubcontractAmountData(String subcontractcode){
        CoeusVector cvAmountInfo = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('a');
        requesterBean.setDataObject(subcontractcode);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                CoeusGuiConstants.CONNECTION_URL + SUBCONTRACT_SERVLET, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
        }
        if(responderBean.getDataObject() != null){
            cvAmountInfo = (CoeusVector) responderBean.getDataObject();
        }
        return cvAmountInfo;
    }

    //PT ID#2382 - Save As functionality - Start
    public void saveAsActiveSheet(){
        
        if(unresForm.isUnresolved()){
            SaveAsDialog saveAsDialog = new SaveAsDialog(unresForm.tblInbox);
        }else{
            SaveAsDialog saveAsDialog = new SaveAsDialog(resForm.tblInbox);
        }        
    }     
    
    public void saveActiveSheet(){
    } 
    //PT ID#2382 - Save As functionality - end
    
    //Added for COEUSDEV-299 : double click is no longer working to open protocol from inbox - Start
    /*
     * Method to enable Modify,Display buttons and menuitems based on the module
     *
     */
    private void enableModifyDisplay(){
        //modify and display Enabled for proposal and subcontract module
        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE || moduleCode == ModuleConstants.SUBCONTRACTS_MODULE_CODE) {
            btnModifyInbox.setEnabled(true);
            btnDisplayInbox.setEnabled(true);
            if(propModify != null) {
            propModify.setEnabled(true);
            propDisplay.setEnabled(true);
            }
        //Only display is enabled for protocol module
        }else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE || moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            btnDisplayInbox.setEnabled(true);
            btnModifyInbox.setEnabled(false);
            if(propModify != null) {
            propDisplay.setEnabled(true);
            propModify.setEnabled(false);
            }
        }else {
            btnModifyInbox.setEnabled(false);
            btnDisplayInbox.setEnabled(false);
            if(propModify != null) {
            propModify.setEnabled(false);
            propDisplay.setEnabled(false);
            }
        }
        
    }
    /*
     * Method to check user has right to open protocol in display mode
     */
    private boolean hasDisplayProtocolRights() {
        boolean hasRight = false;
        int paneIndex=tbdPnInbox.getSelectedIndex();
        int selectedRow = -1;
        String protocolNumber = "";
        if(paneIndex == 0){
            selectedRow = unresForm.tblInbox.getSelectedRow();
            if(selectedRow != -1){
                protocolNumber = (String)unresForm.tblInbox.getValueAt(selectedRow,4);
            }
        }else if(paneIndex == 1){
            selectedRow = resForm.tblInbox.getSelectedRow();
            if(selectedRow != -1){
                protocolNumber = (String)resForm.tblInbox.getValueAt(selectedRow,4);
            }
        }
        
        if(selectedRow != -1){
            
            Vector dataObjects = null;
            
            String PROTOCOL_SERVLET = "/protocolMntServlet";
            
            String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
            RequesterBean request = new RequesterBean();
            request.setFunctionType('D');
            request.setId(protocolNumber);
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
                hasRight = true;
            }else {
                if(response.getDataObject() != null ){
                    Object obj = response.getDataObject();
                    if(obj instanceof CoeusException){
                        CoeusOptionPane.showErrorDialog( ( (CoeusException)obj ).getMessage() );
                    }else{
                        CoeusOptionPane.showErrorDialog( response.getMessage() );
                    }
                }
            }
        }
        return hasRight;
    }
    
    
    /*
     * Method to check user has right to open IACUC protocol in display mode
     */
    private boolean hasDisplayIACUCProtocolRights() {
        boolean hasRight = false;
        int paneIndex=tbdPnInbox.getSelectedIndex();
        int selectedRow = -1;
        String protocolNumber = "";
        if(paneIndex == 0){
            selectedRow = unresForm.tblInbox.getSelectedRow();
            if(selectedRow != -1){
                protocolNumber = (String)unresForm.tblInbox.getValueAt(selectedRow,4);
            }
        }else if(paneIndex == 1){
            selectedRow = resForm.tblInbox.getSelectedRow();
            if(selectedRow != -1){
                protocolNumber = (String)resForm.tblInbox.getValueAt(selectedRow,4);
            }
        }
        
        if(selectedRow != -1){
            
            Vector dataObjects = null;
            
            String PROTOCOL_SERVLET = "/IacucProtocolServlet";
            
            String connectTo = CoeusGuiConstants.CONNECTION_URL+ PROTOCOL_SERVLET;
            RequesterBean request = new RequesterBean();
            request.setFunctionType('D');
            request.setId(protocolNumber);
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
                hasRight = true;
            }else {
                if(response.getDataObject() != null ){
                    Object obj = response.getDataObject();
                    if(obj instanceof CoeusException){
                        CoeusOptionPane.showErrorDialog( ( (CoeusException)obj ).getMessage() );
                    }else{
                        CoeusOptionPane.showErrorDialog( response.getMessage() );
                    }
                }
            }
        }
        return hasRight;
    }
    
    
     /**
     * This method get protocol detail 
     * @param String protocolNumber
     * @return 
     */
    private ProtocolInfoBean getProtocolDetails(String protocolNumber)throws CoeusException{
        ProtocolInfoBean protocolInfoBean = new ProtocolInfoBean();
        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/protocolMntServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('f');
            requester.setDataObject(protocolNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                protocolInfoBean = (ProtocolInfoBean)response.getDataObject();
            }
        }
        return protocolInfoBean;
    }
    
    /**
     * This method get IACUC protocol detail 
     * @param String protocolNumber
     * @return 
     */
    private edu.mit.coeus.iacuc.bean.ProtocolInfoBean getIACUCProtocolDetails(String protocolNumber)throws CoeusException{
        edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = new edu.mit.coeus.iacuc.bean.ProtocolInfoBean();
        if (protocolNumber != null && protocolNumber.trim().length() > 0 && !protocolNumber.equals("")) {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('f');
            requester.setDataObject(protocolNumber);
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(!response.isSuccessfulResponse()){
                throw new CoeusException(response.getMessage(), 1);
            }else{
                protocolInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)response.getDataObject();
            }
        }
        return protocolInfoBean;
    }
    /*
     * Method to open protocol
     * @param char mode - display/modify mode
     * @param String protocolNumber
     */
    private void showProtocol(char mode,String protocolNumber){
        try {
            ProtocolInfoBean protocolInfoBean = getProtocolDetails(protocolNumber);
            if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                if(!protocolNumber.trim().equals("")){
                    if ( mode == TypeConstants.DISPLAY_MODE ){
                        if(hasDisplayProtocolRights()){
                            ProtocolDetailForm protocolForm;
                            try {
                                protocolForm = new ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNumber, CoeusGuiConstants.getMDIForm());
                                protocolForm.showDialogForm();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } 
                    } 
                }
            }
           
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    
    /*
     * Method to open IACUC protocol
     * @param char mode - display/modify mode
     * @param String protocolNumber
     */
    private void showIACUCProtocol(char mode,String protocolNumber){
        try {
            edu.mit.coeus.iacuc.bean.ProtocolInfoBean protocolInfoBean = getIACUCProtocolDetails(protocolNumber);
            if(protocolInfoBean != null && protocolInfoBean.getProtocolNumber() != null){
                if(!protocolNumber.trim().equals("")){
                    if ( mode == TypeConstants.DISPLAY_MODE ){
                        if(hasDisplayIACUCProtocolRights()){
                            edu.mit.coeus.iacuc.gui.ProtocolDetailForm protocolForm;
                            try {
                                protocolForm = new edu.mit.coeus.iacuc.gui.ProtocolDetailForm(TypeConstants.DISPLAY_MODE, protocolNumber, CoeusGuiConstants.getMDIForm());
                                protocolForm.showDialogForm();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } 
                    } 
                }
            }
           
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    //Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - start  
    /** To get the values from coeussearch.xml
     * @param searchName
     * @Vecter resovled and unresolved messages 
     */
    private Vector getReslAndUnReslMessages(String searchName) throws Exception {
        CoeusSearch coeusSearch = new CoeusSearch();
        Hashtable hUnResolAndResolMess = coeusSearch.getInboxDetailsFromSearch(CoeusGuiConstants.getMDIForm().getUserId(),searchName);
        Vector inboxUnResolAndResolList = new Vector();
        if(hUnResolAndResolMess != null && !hUnResolAndResolMess.isEmpty()){
            Vector vecUnResolAndResolDisplay = (Vector)hUnResolAndResolMess.get("displaylabels");
            Vector vecUnResolAndResolInboxList = (Vector)hUnResolAndResolMess.get("reslist");
            if(vecUnResolAndResolInboxList != null && vecUnResolAndResolInboxList.size()>0){
               // inboxUnResolAndResolList = updateInboxBean(vecUnResolAndResolInboxList);
                inboxUnResolAndResolList.addElement(vecUnResolAndResolInboxList) ;
            }
            if(vecUnResolAndResolDisplay != null && vecUnResolAndResolDisplay.size()>0){
                inboxUnResolAndResolList.addElement(vecUnResolAndResolDisplay);
            }
        }else{
            // Added for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - Start
            // Gets the display label details, when no data rows are fetched
            SearchInfoHolderBean searchExecutionBean =  coeusSearch.fetchSearchInfoHolder();
            Vector vecDisplayList = searchExecutionBean.getDisplayList();
            inboxUnResolAndResolList.addElement(new Vector());
            inboxUnResolAndResolList.addElement(vecDisplayList);
            // Added for COEUSDEV-847 : Inbox not working properly both resolved and unresolved tabs - End
        }
        return inboxUnResolAndResolList;
    }
     //Modified for the case # COEUSQA-2073 - Improve management of Coeus Inbox - Ability to reorder columns in Inbox - end

}
