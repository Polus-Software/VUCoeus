/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.negotiation.controller;


/**
 * NegotiationListController.java
 * Created on July 16, 2004, 2:17 PM
 * @author  nadhgj
 */




import edu.mit.coeus.exception.*;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.saveas.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.negotiation.gui.NegotiationListForm;
import edu.mit.coeus.negotiation.bean.*;
import edu.mit.coeus.propdev.gui.MedusaDetailForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.negotiation.bean.NegotiationInfoBean;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.negotiation.controller.NegotiationController;
import edu.mit.coeus.negotiation.controller.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.saveas.SaveAsDialog;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.negotiation.gui.*;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.gui.event.*;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.user.gui.UserPreferencesForm;
//start case 1735
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
//import java.util.Hashtable;
//end case 1735

import java.beans.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.*;





public class NegotiationListController extends NegotiationController 
implements MouseListener,ActionListener,VetoableChangeListener, Observer ,BeanUpdatedListener{
    
    private NegotiationListForm negotiationListForm;  
    
    /** Coeus Serach instance to search Awards. */
    private CoeusSearch coeusSearch;
    
    private boolean closed = false;
    
    /** Holds the selectedRow in the Proposal Log List
     */
    private int baseTableRow;
        
    /** Holds CoeusMessageResources instance used for reading message Properties.
     */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private QueryEngine queryEngine = QueryEngine.getInstance();
    
    private CoeusVector cvData;
    
    private DateUtils dateUtils = new DateUtils();
    //Code modified for PT ID#3242 - date format
    private static final String REQUIRED_DATEFORMAT = "yyyy/MM/dd";
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private JTable tblResultsTable;
    private static final char CHECK_RIGHT = 'E'; 
    private static final String GET_SERVLET = "/NegotiationMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    private static final int NEGOTIATION_COLUMN = 0;
    public static final char NEW_NEGOTIATION = 'N';
    private static final String NEGOTIATION_LIST_SEARCH = "NEGOTIATIONSEARCH";
    
    private static final String NO_MORE_NEGOTIATIONS_TO_DISPLAY = "negotiationBaseWindow_exceptionCode.1053";
    private static final String CANNOT_OPEN_MEDUSA_FOR_TEMP_PROP = "negotiationBaseWindow_exceptionCode.1054";
    
    private static final String LETTER_T = "T";
    
    public final static String EMPTY_STRING = "";
    
    public final static String FUNC_NOT_IMPL="Fuction not implemented";
    
    private int selectedRow; 
    
    private String piUserName;
   
    private static final int PROP_NO_COLUMN = 0;
    private static final int PI_COLUMN = 1;
    private static final int SPONSOR_CODE_COLUMN = 2;
    private static final int SPONSOR_NAME_COLUMN = 3;
    private static final int TITLE_COLUMN = 4;
    private static final int NGOTIATOR = 5;
    private static final int NEGO_START_DATE = 6;
    private static final int NEGO_STATUS = 7;
    private static final int PROPOSAL_TYPE = 8;
    private static final int LEAD_UNIT_COLUMN = 9;
    private static final int LEAD_UNIT_NAME_COLUMN = 10;
    
    
    
//    private boolean iPReviewRight = false;
    private boolean isViewNegotiation = false;
    
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End
    
    //start case 1735
     private static final char PRINT_NEGOTIATION_ACTIVITY = 'A';
     //private static final String printConnect = CoeusGuiConstants.CONNECTION_URL+"/printServlet";
     private static final String printConnect = CoeusGuiConstants.CONNECTION_URL + "/ReportConfigServlet";
     private static final String PRINT_NEGOTIATION = "printNegotiation";
    //end case 1735 
    // 3587: Multi Campus Enahncements
    private static final char CAN_MODIFY_NEGOTIATION = 'K';
     
    public NegotiationListController() throws Exception{
        initComponents();
        registerComponents();
    }
    
     private void initComponents() {
        try{
            negotiationListForm = new 
            NegotiationListForm("Negotiation List", mdiForm);
            coeusSearch = new CoeusSearch(mdiForm, NEGOTIATION_LIST_SEARCH , 0);
            JTable tblResults = coeusSearch.getEmptyResTable();
            negotiationListForm.initComponents(tblResults);
            
            /** contact server and get the rightCheckings and then 
             *enable and disable the menu items and tool bar buttons.
             */
            CoeusVector cvRightChecking = checkIsRightAvailable();
            if(cvRightChecking!= null && cvRightChecking.size() > 0){
                enableDisableMenuItems(cvRightChecking);
            }
        }catch (Exception exception) {
                
            exception.printStackTrace();
        }
    }
     
     /** displays Award Search Window. */
    private void showNegotiationSearch() {
        try {
            coeusSearch.showSearchWindow();
            tblResultsTable = coeusSearch.getSearchResTable();
            negotiationListForm.displayResults(tblResultsTable);
            
           // adding listener for table.
            if(tblResultsTable != null) {
                negotiationListForm.tblResults.addMouseListener(this);
                //Added by nadh - 19/01/2005 for table header
                negotiationListForm.tblResults.getTableHeader().addMouseListener(this);
                //Added by Nadh - End
                 //Case 2451 start
                   vecSortedData = new Vector();
                   //Case 2451 end
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     /** Displays Award List. */
    public void display() {
        try{
            mdiForm.putFrame(CoeusGuiConstants.NEGOTIATION_DETAILS_FRAME_TITLE, negotiationListForm);
            mdiForm.getDeskTopPane().add(negotiationListForm);
            negotiationListForm.setSelected(true);
            negotiationListForm.setVisible(true);
            showNegotiationSearch();
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return negotiationListForm;
    }
    
    public Object getFormData() {
        return negotiationListForm;
    }
    
    public void registerComponents() {
        
        negotiationListForm.addVetoableChangeListener(this);
        // Setting listener for the Tool bar buttons
        
        negotiationListForm.btnClose.addActionListener(this);
        negotiationListForm.btnCorrectNegotiation.addActionListener(this);
        negotiationListForm.btnDisplayNegotiation.addActionListener(this);
        negotiationListForm.btnMedusa.addActionListener(this);
        negotiationListForm.btnNewNegotiation.addActionListener(this);
        negotiationListForm.btnPrintAllNegotiation.addActionListener(this);
        negotiationListForm.btnSearchNegotiation.addActionListener(this);
        negotiationListForm.btnSort.addActionListener(this);//Added by nadh - 19/01/2005 for Sort ToolBar Button
        
        //Setting the listener for the  file menu items
        
        negotiationListForm.mnuItmChangePassword.addActionListener(this);
        negotiationListForm.mnuItmClose.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        negotiationListForm.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        negotiationListForm.mnuItmPreferences.addActionListener(this);
        negotiationListForm.mnuItmExit.addActionListener(this);
        negotiationListForm.mnuItmSort.addActionListener(this);
        negotiationListForm.mnuItmPrintAll.addActionListener(this);
        negotiationListForm.mnuItmSaveas.addActionListener(this);
        //Added for Case#2805 - Save As functionality on Negotiation Search results - no icon on tool bar - Start
        negotiationListForm.btnSaveas.addActionListener(this);
        //End - Case#2805
        //Setting the listener for the  edit menu items
        negotiationListForm.mnuItmCorrectNegotiation.addActionListener(this);
        negotiationListForm.mnuItmDisplayNegotiation.addActionListener(this);
        negotiationListForm.mnuItmMedusa.addActionListener(this);
        negotiationListForm.mnuItmNewNegotiation.addActionListener(this);
        
        //Case 2110 Start
        negotiationListForm.mnuCurrentLocks.addActionListener(this);
        //Case 2110 End
        
        //Setting the listener for the  tools menu items
        
        negotiationListForm.mnuItmSearch.addActionListener(this);
        addBeanUpdatedListener(this, NegotiationInfoBean.class);
    }
    
    public void saveFormData() {
    }
    
    public void setFormData(Object data) {
    }
    
    public boolean validate() throws CoeusUIException {
        return false;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            
                blockEvents(true);
                mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                if(source.equals(negotiationListForm.mnuItmCorrectNegotiation) ||
                source.equals(negotiationListForm.btnCorrectNegotiation)){
                    correctNegotiation();
                }else if(source.equals(negotiationListForm.mnuItmDisplayNegotiation) ||
                 source.equals(negotiationListForm.btnDisplayNegotiation)){
                    displayNegotiation();            
                }else if(source.equals(negotiationListForm.btnSearchNegotiation) ||
                 source.equals(negotiationListForm.mnuItmSearch)){
                    showNegotiationSearch();
                }else if(source.equals(negotiationListForm.mnuItmNewNegotiation) ||
                 source.equals(negotiationListForm.btnNewNegotiation)){
                    newNegotiation();
                }else if(source.equals(negotiationListForm.btnClose) ||
                 source.equals(negotiationListForm.mnuItmClose)){
                    close();
                }else if(source.equals(negotiationListForm.mnuItmMedusa) || 
                 source.equals(negotiationListForm.btnMedusa)){
                    showMedusaWindow();
                //Modified for Case#2805 - Save As functionality on Negotiation Search results - no icon on tool bar - Start
                //}else if(source.equals(negotiationListForm.mnuItmSaveas)){
                }else if(source.equals(negotiationListForm.mnuItmSaveas) || source.equals(negotiationListForm.btnSaveas)){
                    saveNegotiationList();
                }else if(source.equals(negotiationListForm.mnuItmPrintAll) || source.equals(negotiationListForm.btnPrintAllNegotiation)){
                    //start case 17335
//                    CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);                    
                    try{
                        printNegotiation();
                    }catch(CoeusException coeusException){
                        CoeusOptionPane.showErrorDialog(coeusException.getMessage());
                        coeusException.printStackTrace();
                    }
                    //end case 1735
                }else if(source.equals(negotiationListForm.mnuItmChangePassword)){
                    showChangePassword();
                    //CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);
                }else if(source.equals(negotiationListForm.mnuItmPreferences)) {
                    showPreference();
                //Added for Case#3682 - Enhancements related to Delegations - Start
                }else if(source.equals(negotiationListForm.mnuItmDelegations)) {
                    displayUserDelegation();
                //Added for Case#3682 - Enhancements related to Delegations - End
                //start of bug fix id 1651
                } else if(source.equals(negotiationListForm.mnuItmExit)) {
                    exitApplication();
                }//end of bug fix id 1651
                //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
                else if (source.equals(negotiationListForm.btnSort) || source.equals(negotiationListForm.mnuItmSort)) {
                    showSort();
                }//Added by Nadh - End
                //Case 2110 Start
                else if(source.equals(negotiationListForm.mnuCurrentLocks)){
                   showLocksForm(); 
                }                
                //Case 2110 End
                else {
                 CoeusOptionPane.showInfoDialog(FUNC_NOT_IMPL);
                }        
          }catch(CoeusException coeusException){
              coeusException.printStackTrace();
              CoeusOptionPane.showErrorDialog(coeusException.getMessage());
          }catch(Exception exception){
              exception.printStackTrace();
              CoeusOptionPane.showErrorDialog(exception.getMessage());
          }finally {
            blockEvents(false);
            mdiForm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
          }
        
    }
     
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     *Display Delegations window
     */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - Start
    
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha


    // added by Nadh to implement sorting proposals start - 19-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(negotiationListForm.tblResults,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            coeusSearch.sortByColumns(negotiationListForm.tblResults,vecSortedData);
        else
            return;
    }// Added by Nadh - end     
    
    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //Case 2110 Start
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End 
    

    /** Opens negotiation in Modify mode
     */
   private void correctNegotiation(){
        selectedRow = negotiationListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        
        String negotiationNumber = negotiationListForm.tblResults.getValueAt(selectedRow, NEGOTIATION_COLUMN).toString();
        
        // For Locking mechanism
        if(isNegotiationOpen(negotiationNumber, CoeusGuiConstants.MODIFY_MODE)) {
          return ;
        }
        // 3587: Multi Campus Enahncements -Start
        boolean modifyRight = checkUserHasModifyRight(negotiationNumber);
        if(!modifyRight){
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("negotiationBaseWindow_exceptionCode.1056"));
            return;
        }        
        // 3587: Multi Campus Enahncements - End
        NegotiationInfoBean negotiationInfoBean = new NegotiationInfoBean();
        negotiationInfoBean.setNegotiationNumber(negotiationNumber);
        NegotiationBaseWindowController negotiationBaseWindowController = 
            new NegotiationBaseWindowController(CoeusGuiConstants.NEGOTIATION_LIST, CoeusGuiConstants.MODIFY_MODE, negotiationInfoBean);
        negotiationBaseWindowController.registerObserver(this);
        
        baseTableRow = negotiationListForm.tblResults.getSelectedRow();
        negotiationBaseWindowController.display();
    }
    
    /** Opens the negotiation in display mode
     */
    private void displayNegotiation(){  
        int selectedRow = negotiationListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
        piUserName = negotiationListForm.tblResults.getValueAt(selectedRow, PI_COLUMN).toString();
        
        if(!isViewNegotiation){
            
            if(!piUserName.equals(mdiForm.getUserName())){
            CoeusOptionPane.showInfoDialog("You do not have the right to view this negotiation");
            return;
            }
        
        }
        String negotiationNumber = negotiationListForm.tblResults.getValueAt(selectedRow, NEGOTIATION_COLUMN).toString();
       
       // For Locking mechanism
        if(isNegotiationOpen(negotiationNumber, CoeusGuiConstants.DISPLAY_MODE)) {
          return ;
        }
        NegotiationInfoBean negotiationInfoBean = new NegotiationInfoBean();
        negotiationInfoBean.setNegotiationNumber(negotiationNumber);
        
        NegotiationBaseWindowController negotiationBaseWindowController = 
            new NegotiationBaseWindowController(CoeusGuiConstants.NEGOTIATION_LIST, CoeusGuiConstants.DISPLAY_MODE, negotiationInfoBean);
                negotiationBaseWindowController.display();
    }
    
    private void saveNegotiationList() {
        //SaveAsDialog saveAsDialog = new SaveAsDialog(tblResultsTable);
        SaveAsDialog saveAsDialog = new SaveAsDialog(negotiationListForm.tblResults);
        
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
    
    private CoeusVector checkIsRightAvailable(){
        cvData = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CHECK_RIGHT);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator(connect, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean.isSuccessfulResponse()) {
            cvData = (CoeusVector)responderBean.getDataObject();
                     
            
        }else {
            CoeusOptionPane.showErrorDialog(responderBean.getMessage());
            return null;
        }
        return cvData;
    }
    
    private void enableDisableMenuItems(CoeusVector rightsData){
        boolean isModifyNegotiation = ((Boolean)rightsData.elementAt(0)).booleanValue();
        isViewNegotiation = ((Boolean)rightsData.elementAt(1)).booleanValue();
               
         if(!isModifyNegotiation){
            negotiationListForm.mnuItmNewNegotiation.setEnabled(false);
            negotiationListForm.mnuItmCorrectNegotiation.setEnabled(false);
            negotiationListForm.btnNewNegotiation.setEnabled(false);
            negotiationListForm.btnCorrectNegotiation.setEnabled(false);
        }
        
        if(!isViewNegotiation){
            negotiationListForm.mnuItmDisplayNegotiation.setEnabled(false);
            negotiationListForm.btnDisplayNegotiation.setEnabled(false);
            negotiationListForm.mnuItmPrintAll.setEnabled(false);
            negotiationListForm.btnPrintAllNegotiation.setEnabled(false);
        }     
        
    }
    
    
    private void newNegotiation() {
        NegotiationBaseWindow negotiationBaseWindow = null;
        
        if(isNegotiationOpen("New Negotiation", CoeusGuiConstants.MODIFY_MODE)) {
          return ;
        }
        
        NegotiationBaseWindowController negotiationBaseWindowController = 
            new NegotiationBaseWindowController(CoeusGuiConstants.NEGOTIATION_LIST, CoeusGuiConstants.ADD_MODE,null);
        negotiationBaseWindowController.registerObserver(this);
        
        baseTableRow = negotiationListForm.tblResults.getSelectedRow();
                negotiationBaseWindowController.display();
    }
    
    /** To show the medusa window whenever the user select the particular row and 
     *click on medusa icon
     */
    private void  showMedusaWindow(){
        try{
            ProposalAwardHierarchyLinkBean linkBean;
            MedusaDetailForm medusaDetailform;
            int selectedRow = negotiationListForm.tblResults.getSelectedRow();
            if( selectedRow >= 0 ){
                String propNumber = (String)negotiationListForm.tblResults.getValueAt(selectedRow, PROP_NO_COLUMN);
                if(propNumber.startsWith(LETTER_T)){
                    CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey(CANNOT_OPEN_MEDUSA_FOR_TEMP_PROP));
                    return ;
                }
                linkBean = new ProposalAwardHierarchyLinkBean();
                linkBean.setInstituteProposalNumber(propNumber);
                linkBean.setBaseType(CoeusConstants.INST_PROP);
                if( ( medusaDetailform = (MedusaDetailForm)mdiForm.getFrame(
                CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                    if( medusaDetailform.isIcon() ){
                        medusaDetailform.setIcon(false);
                    }
                    medusaDetailform.setSelectedNodeId(propNumber);
                    medusaDetailform.setSelected( true );
                    return;
                }
                medusaDetailform = new MedusaDetailForm(mdiForm,linkBean);
                medusaDetailform.setVisible(true);
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    
       private String getSelectedProposalNumber() throws Exception{
        String proposalNum = "";
        if ( ((
        (DefaultTableModel)
        tblResultsTable.getModel()).getRowCount()
        == 0) ||
        (tblResultsTable.getSelectedRow() < 0) ) {
                /* If the there is no row selected in NegotiationWindow  ...raise
                 * and error message to the user
                 */

            throw new Exception(coeusMessageResources.parseMessageKey(
                                            "roldxBaseWin_exceptionCode.1104"));

        }else {
                /* If there is  a row selected in Negotiation Window get the first
                 * column value in the selected row from the table
                 * assign it to the proposalNum variable
                 */
            proposalNum =
            tblResultsTable.getValueAt(
            tblResultsTable.getSelectedRow(),
            0).toString();
                /* Call this method to get the Negotiation window with proper
                 * checking for modifying the Negotiation info*/

        }
        return proposalNum;

    }    
    
       public void mouseClicked(MouseEvent mouseEvent) {
           
           //Added by Nadh to get the column header and its status Start 18-01-2005
           if(mouseEvent.getSource() instanceof JTableHeader) {
               JTableHeader tblHeader = (JTableHeader) mouseEvent.getSource();
               TableColumnModel columnModel = tblHeader.getColumnModel();
               int viewColumn = columnModel.getColumnIndexAtX(mouseEvent.getX());
               int column = columnModel.getColumn(viewColumn).getModelIndex();
               int sortStatus = getStatus();
               if(oldCol != column )
                   sortStatus = MultipleTableColumnSorter.NOT_SORTED;
               sortStatus = sortStatus + (mouseEvent.isShiftDown() ? -1 : 1);
               sortStatus = (sortStatus + 4) % 3 - 1;
               setStatus(sortStatus);
               oldCol = column;
               if(getStatus()==MultipleTableColumnSorter.ASCENDING || getStatus() == MultipleTableColumnSorter.DESCENDING) {
                   Vector newSortedData = new Vector();
                   newSortedData.addElement(negotiationListForm.tblResults.getColumnName(column));
                   newSortedData.addElement(new Integer(column));
                   newSortedData.addElement(new Boolean(status == 1 ? true : false));
                   if(vecSortedData == null)
                       vecSortedData = new Vector();
                   vecSortedData.removeAllElements();
                   vecSortedData.addElement(newSortedData);
               }else {
                   vecSortedData = null;
               }
           }//End Nadh
           if(mouseEvent.getSource() instanceof JTable) {
               if(mouseEvent.getClickCount() != 2) return ;
               int selectedRow = negotiationListForm.tblResults.getSelectedRow();
               if(selectedRow == -1)return ;
               piUserName = negotiationListForm.tblResults.getValueAt(selectedRow, PI_COLUMN).toString();
               if(!isViewNegotiation){
                   
                   if(!piUserName.equals(mdiForm.getUserName())){
                       CoeusOptionPane.showInfoDialog("You do not have the right to view this negotiation");
                       return;
                   }
                   
               }
               //Buf Fix:1062 Hour glass implemntation start
               mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
               //Double Clicked on Table. open Negotiation in display Mode.
               displayNegotiation();
               mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
               //Buf Fix:1062 Hour glass implemntation start
           }
       }
  
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
        
    } 
    private void close() {
        mdiForm.removeFrame(CoeusGuiConstants.NEGOTIATION_DETAILS_FRAME_TITLE);
        //negotiationList.dispose();
        closed = true;
        //select next Internal Frame.
        negotiationListForm.doDefaultCloseAction();
        cleanUp();

    }
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent) throws PropertyVetoException {
         if(closed) return ;
        
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            close();
        }
    }
    
    /** Listens to bean updated event.
     * @param beanEvent beanEvent
     */    
    public void beanUpdated(BeanEvent beanEvent) {
        Controller source = beanEvent.getSource();
        BaseBean baseBean = beanEvent.getBean();
        if(source.getClass().equals(NegotiationBaseWindowController.class) && baseBean.getClass().equals(NegotiationInfoBean.class)) {
            NegotiationBaseWindowController negotiationBaseWindowController = (NegotiationBaseWindowController)source;
            negotiationBaseWindowController.clearOldInstance();
            showNegotiation(beanEvent.getMessageId(), negotiationBaseWindowController);
            negotiationBaseWindowController.updateNewInstance();
        }
    }
    
    /** Displays the negotiation details for the next/prev negotiation in the 
     * Negotiation List depending on the navigation value
     * @param navigation holds 1 or 2, indicating Previous or Next, respectively
     */
    private void showNegotiation(int navigation, 
    NegotiationBaseWindowController negotiationBaseWindowController ){
        int selectedRow = negotiationListForm.tblResults.getSelectedRow();
        if(selectedRow == -1)return ;
              
        if( navigation == SHOW_NEXT_NEGOTIATION ){
            if(negotiationListForm.tblResults.getRowCount() == 1 || selectedRow == negotiationListForm.tblResults.getRowCount() - 1) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                NO_MORE_NEGOTIATIONS_TO_DISPLAY));
                return ;
            }
            selectedRow++;
        }else if( navigation == SHOW_PREV_NEGOTIATION){
            if(negotiationListForm.tblResults.getRowCount() == 1 || selectedRow == 0) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                NO_MORE_NEGOTIATIONS_TO_DISPLAY));
                return ;
            }
            selectedRow--;
        }
        
        String negotiationNumber = negotiationListForm.tblResults.getValueAt(selectedRow, NEGOTIATION_COLUMN).toString();
        if(isNegotiationOpen(negotiationNumber, CoeusGuiConstants.DISPLAY_MODE)) {
            return ;
        }
        
        negotiationListForm.tblResults.setRowSelectionInterval(selectedRow, selectedRow);
        
        NegotiationInfoBean negotiationInfoBean = new NegotiationInfoBean();
        negotiationInfoBean.setNegotiationNumber(negotiationNumber);
        //start case 1735
        negotiationBaseWindowController.dataFetched = false;
//        end case 1735
        negotiationBaseWindowController.setFormData(negotiationInfoBean);
    }
    public void cleanUp(){
        removeBeanUpdatedListener(this, NegotiationInfoBean.class);
        tblResultsTable = null;
        coeusMessageResources = null;
        cvData = null;
        coeusSearch = null;
        coeusMessageResources = null;
        dateUtils = null;       
    }
    
    public void update(java.util.Observable observable, Object arg) {
        
            Hashtable negData = (Hashtable)arg;
            
           if (negData.get(NegotiationInfoBean.class) instanceof NegotiationInfoBean ){
            NegotiationInfoBean infoBean = (NegotiationInfoBean)negData.get(NegotiationInfoBean.class);
            NegotiationHeaderBean headerBean = (NegotiationHeaderBean)negData.get(NegotiationHeaderBean.class);
           
            if( ((BaseWindowObservable)observable).getFunctionType() == TypeConstants.ADD_MODE ){
                Vector vecTableRow = new Vector();
                vecTableRow.addElement(infoBean.getNegotiationNumber());
                vecTableRow.addElement(headerBean.getPiName());
                vecTableRow.addElement(headerBean.getSponsorCode());
                vecTableRow.addElement(headerBean.getSponsorName());
                vecTableRow.addElement(headerBean.getTitle());
                vecTableRow.addElement(infoBean.getNegotiatorName());
                //Added for PT ID#3243 - date format
                String dateFormat = CoeusServerProperties.getProperty(CoeusPropertyKeys.SEARCH_DATE_FORMAT, REQUIRED_DATEFORMAT);
                //COEUSQA-1477 Dates in Search Results - Start
                vecTableRow.addElement(dateUtils.parseDateForSearchResults(infoBean.getStartDate().toString(), dateFormat ));
                //vecTableRow.addElement(dateUtils.formatDate(infoBean.getStartDate().toString(), dateFormat ));
                //COEUSQA-1477 Dates in Search Results - End
                
                vecTableRow.addElement(infoBean.getStatusDescription());
                vecTableRow.addElement(headerBean.getProposalTypeDescription());
                vecTableRow.addElement(headerBean.getLeadUnit());
                vecTableRow.addElement(headerBean.getUnitName());
                int lastRow = negotiationListForm.tblResults.getRowCount();
                vecTableRow.addElement( EMPTY_STRING + lastRow);
                ((DefaultTableModel)negotiationListForm.tblResults.getModel()).insertRow(lastRow, vecTableRow);
                if( lastRow == 0 ) {
                    negotiationListForm.tblResults.setRowSelectionInterval(0,0);
                }
                baseTableRow = lastRow;
                negotiationListForm.tblResults.scrollRectToVisible(
                    negotiationListForm.tblResults.getCellRect(baseTableRow, NEGOTIATION_COLUMN, true));            
            }else{
                ((DefaultTableModel)negotiationListForm.tblResults.getModel()).setValueAt(
                    infoBean.getNegotiatorName(), baseTableRow, NGOTIATOR);
                ((DefaultTableModel)negotiationListForm.tblResults.getModel()).setValueAt(
                    infoBean.getStatusDescription(), baseTableRow, NEGO_STATUS);
                
                int selRow = negotiationListForm.tblResults.getSelectedRow();
                //Modified for COEUSDEV-294 : Error adding activity to a negotiation - Start
                //When row count is 1, then the selected row is assigned as the basetable row
//                if(  selRow != -1 ) {
//                    baseTableRow = Integer.parseInt((String)negotiationListForm.tblResults.getValueAt(
//                    selRow,negotiationListForm.tblResults.getColumnCount()-1));
//                }
                if(selRow != -1 && negotiationListForm.tblResults.getRowCount() == 1 && selRow == 0){
                    baseTableRow = selRow;
                }else if(  selRow != -1 ) {
                    baseTableRow = Integer.parseInt((String)negotiationListForm.tblResults.getValueAt(
                    selRow,negotiationListForm.tblResults.getColumnCount()-1));
                }
                //COEUSDEV-294 : End
            }            
        }
    }  
    
    //start case 1735
    /** 
     * Print Negotiation Activity Report
     * @param printType to print one activity or all activities
     */
    private void printNegotiation()throws CoeusException{
        int totalRow = negotiationListForm.tblResults.getModel().getRowCount();
        CoeusVector cvData = new CoeusVector();
        for (int index = 0; index < totalRow; index++){
            cvData.add(tblResultsTable.getValueAt(index, 0).toString());        
        }
        Hashtable htPrintParams = new Hashtable(); 
        
        htPrintParams.put("PRINT_TYPE",PRINT_NEGOTIATION);
        htPrintParams.put("NEGOTIATION_NUMS", cvData);
        
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PRINT_NEGOTIATION_ACTIVITY);
        requester.setDataObject(htPrintParams);
        
         //For Streaming
        String printType = (String)htPrintParams.get("PRINT_TYPE");
        if (printType.equalsIgnoreCase("printNegotiation")) {
             requester.setId("Negotiation/ActivityPrintAll");
        }else{
           requester.setId("Negotiation/Activity");
        }            
        requester.setFunctionType('R');
        //For Streaming
        
        AppletServletCommunicator comm
         = new AppletServletCommunicator(printConnect, requester);
         
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
//             try{
//                reportUrl = new URL( CoeusGuiConstants.CONNECTION_URL + fileName );
//             
//             
//             if (coeusContxt != null) {
//                 coeusContxt.showDocument( reportUrl, "_blank" );
//             }else {
//                 javax.jnlp.BasicService bs = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
//                 bs.showDocument( reportUrl );
//             }
//             }catch(MalformedURLException muEx){
//                 throw new CoeusException(muEx.getMessage());
//             }catch(Exception uaEx){
//                 throw new CoeusException(uaEx.getMessage());
//             }
             try{
                 URL url = new URL(fileName);
                 URLOpener.openUrl(url);
             }catch (MalformedURLException malformedURLException) {
                 throw new CoeusException(malformedURLException.getMessage());
             }
         }else{
             throw new CoeusException(responder.getMessage());
         }               
    }
    //end case 1735
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
   // 3587: Multi Campus Enahncements - Start
    private boolean checkUserHasModifyRight(String negotiationNumber) {
        boolean modifyRight = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(CAN_MODIFY_NEGOTIATION);
        requesterBean.setDataObject(negotiationNumber);
        
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(connect, requesterBean);
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
    
}

    