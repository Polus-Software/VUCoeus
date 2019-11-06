/*
 * SubcontractReportListController.java
 *
 * Created on January 3, 2005, 4:49 PM
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.SubcontractGoalsForm;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.admin.gui.SubcontractReportListForm;
import edu.mit.coeus.admin.gui.SubcontractValidationForm;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.award.bean.AwardDetailsBean;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.event.BlockingGlassPane;
import edu.mit.coeus.user.gui.UserDelegationForm;

import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.SortForm;
import edu.mit.coeus.utils.TableSorter;




import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


/**
 *
 * @author  chandrashekara
 */
public class SubcontractReportListController implements ActionListener,VetoableChangeListener{
    private SubcontractReportListForm subcontractReportListForm;
    private static BlockingGlassPane blockingGlassPane;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private CoeusMessageResources coeusMessageResources;
    private ChangePassword changePassword;
    private static final String EMPTY_STRING = "";
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    private String winTitle = EMPTY_STRING;
    private String title = EMPTY_STRING;
    private boolean closed = false;
    private JTable tblResultsTable;
    private ReportTableModel reportTableModel;
    private boolean hasGoalRight;
    private boolean hasModifyRight;
    private CoeusVector cvReportData;
    private Vector rightData;
    private MultipleTableColumnSorter sorter;
    private static final char GET_REPORT_DATA = 'O';
    private static final char GET_GOALS_DATA = 'G' ;
    private static final char GET_VALIDATION_DATA = 'I'; 
    private static final String GET_SERVLET = "/SubcontractMaintenenceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    // Setting column numbers.
    private static final int AWARD_NUMBER_COL = 0;
    private static final int SPONSOR_AWARD_COLUMN = 1;
    private static final int TITLE_COL = 2;
    private static final int ACCOUNT_COL = 3;
    private static final int STATUS_COL = 4;
    private static final int SPONSOR_CODE_COL = 5;
    private static final int SPONSOR_NAME_COL = 6;
    private static final String SELECT_ONLY_ONE_AWARD = "You may select only one award to enter goals ";
    private static final String SELECT_A_ROW = "Please select an award ";
    //Added for case 3587: Multi campus enhancement
    private static final String MODIFY_SUBCONTRACTING_GOALS = "MODIFY_SUBCONTRACTING_GOALS";
    private static final String NO_RIGHTS_TO_ENTER_GOAL = "subcontractGoal_exceptionCode.1451";
    private static final String NO_RIGHT_TO_PRINT = "subcontractPrint_exceptionCode.1402";
    private static final char CHECK_USER_HAS_GOAL_RIGHT = 'Q';
    //3587 End
    //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
    
    //holds sorted columns and its states
    private Vector vecSortedData;
    
    private static final int OK_CLICKED = 0;
    
    //holds column index
    private int oldCol = -1;
    
    //holds column status
    private int status=MultipleTableColumnSorter.NOT_SORTED;
    //Added by Nadh - End
    
    /** Creates a new instance of SubcontractReportListController */
    public SubcontractReportListController(CoeusAppletMDIForm mdiForm,String title) throws CoeusException {
        this.mdiForm = mdiForm;
        this.title = title;
        coeusMessageResources = CoeusMessageResources.getInstance();
        subcontractReportListForm = new SubcontractReportListForm(title,mdiForm);
        //registerComponents();

    }
    
    public  void initComponents() {
        subcontractReportListForm = new SubcontractReportListForm(CoeusGuiConstants.SUBCONTRACTING_REPORTS, mdiForm);
        reportTableModel = new ReportTableModel();
        subcontractReportListForm.tblReports.setModel(reportTableModel);
    }
    
    
    
    public void registerComponents() {
        
      //  setColumnData();
        subcontractReportListForm.addVetoableChangeListener(this);
        subcontractReportListForm.mnuItmChangePassword.addActionListener(this);
        subcontractReportListForm.mnuItmClose.addActionListener(this);
        subcontractReportListForm.mnuItmDeselectAll.addActionListener(this);
        subcontractReportListForm.mnuItmEnterGoals.addActionListener(this);
        subcontractReportListForm.mnuItmExit.addActionListener(this);
        subcontractReportListForm.mnuItmInbox.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        subcontractReportListForm.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        subcontractReportListForm.mnuItmPreferences.addActionListener(this);
        subcontractReportListForm.mnuItm294.addActionListener(this);
        subcontractReportListForm.mnuItm295.addActionListener(this);
        subcontractReportListForm.mnuItmSelectAll.addActionListener(this);
        subcontractReportListForm.mnuItmValidationChecks.addActionListener(this);
        subcontractReportListForm.mnuItmSort.addActionListener(this);
        
        subcontractReportListForm.btnClose.addActionListener(this);
        subcontractReportListForm.btnDeselectAll.addActionListener(this);
        subcontractReportListForm.btnGoals.addActionListener(this);
        subcontractReportListForm.btnSelectAll.addActionListener(this);
        subcontractReportListForm.btnValidationChecks.addActionListener(this);
        
        //Case 2110 Start
        subcontractReportListForm.mnuItmCurrentLocks.addActionListener(this);
        //Case 2110 End
        
        //Added by nadh - 19/01/2005 for Sort Button - Start
        subcontractReportListForm.btnSort.addActionListener(this);
        subcontractReportListForm.tblReports.getTableHeader().addMouseListener(new SubcontactRptMouseAdapter());
        //Added by nadh - End
    }
    
    public Component getControlledUI(){
        return subcontractReportListForm;
    }
    
    
    public void setFormData() throws CoeusException{
        cvReportData = new CoeusVector();
        Hashtable data = getReportsData();
        cvReportData = (CoeusVector)data.get(CoeusVector.class);
        rightData = (Vector)data.get(Vector.class);
        if(rightData!= null && rightData.size() > 0){
            hasGoalRight = ((Boolean)rightData.get(0)).booleanValue();
            hasModifyRight = ((Boolean)rightData.get(1)).booleanValue();
            // check for MODIFY_SUBCONTRACTING_GOALS right
            subcontractReportListForm.btnGoals.setEnabled(hasGoalRight);
            subcontractReportListForm.mnuItmEnterGoals.setEnabled(hasGoalRight);
            subcontractReportListForm.mnuItm294.setEnabled(hasGoalRight);
            subcontractReportListForm.mnuItm295.setEnabled(hasGoalRight);
            subcontractReportListForm.mnuPrint.setSelected(false);
            
            // Check for MODIFY_SUBCONTRACT right
            subcontractReportListForm.mnuItm294.setEnabled(hasGoalRight);
            subcontractReportListForm.mnuItm295.setEnabled(hasGoalRight);
            subcontractReportListForm.mnuItmValidationChecks.setEnabled(hasModifyRight);
            subcontractReportListForm.btnValidationChecks.setEnabled(hasModifyRight);
            subcontractReportListForm.mnuPrint.setSelected(false);
        }
        reportTableModel.setData(cvReportData);
        if( sorter == null ) {
            //Added for supporting multiple sorting - nadh - 18-01-2005
            sorter = new MultipleTableColumnSorter((AbstractTableModel)subcontractReportListForm.tblReports.getModel());
            subcontractReportListForm.tblReports.setModel(sorter);
            sorter.setTableHeader(subcontractReportListForm.tblReports.getTableHeader());
        }
        
    }
    
  
    public void setColumnData(){
        JTableHeader tableHeader = subcontractReportListForm.tblReports.getTableHeader();
        tableHeader.setVisible(true);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        subcontractReportListForm.tblReports.setRowHeight(22);
        
        TableColumn column = subcontractReportListForm.tblReports.getColumnModel().getColumn(AWARD_NUMBER_COL);
        column.setPreferredWidth(80);
        
        
        
        column = subcontractReportListForm.tblReports.getColumnModel().getColumn(SPONSOR_AWARD_COLUMN);
        column.setPreferredWidth(130);
        
        
        column = subcontractReportListForm.tblReports.getColumnModel().getColumn(TITLE_COL);
        column.setPreferredWidth(342);
        
        
        column = subcontractReportListForm.tblReports.getColumnModel().getColumn(ACCOUNT_COL);
        column.setPreferredWidth(80);
        
        
        
        column = subcontractReportListForm.tblReports.getColumnModel().getColumn(STATUS_COL);
        column.setPreferredWidth(70);
        
        
        column = subcontractReportListForm.tblReports.getColumnModel().getColumn(SPONSOR_CODE_COL);
        column.setPreferredWidth(100);
        
        
        column = subcontractReportListForm.tblReports.getColumnModel().getColumn(SPONSOR_NAME_COL);
        column.setPreferredWidth(180);
        
        
        subcontractReportListForm.tblReports.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    
    
    
    private Hashtable getReportsData() throws CoeusException{
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(GET_REPORT_DATA);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                htData = (Hashtable)responder.getDataObject();
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
        return htData;
    }
    
    public void display() throws PropertyVetoException{
        if(cvReportData!= null && cvReportData.size() > 0){
            setColumnData();
            mdiForm.putFrame(CoeusGuiConstants.SUBCONTRACTING_REPORTS, subcontractReportListForm);
            mdiForm.getDeskTopPane().add(subcontractReportListForm);
            subcontractReportListForm.setSelected(true);
            subcontractReportListForm.setVisible(true);
            
        }else{
            CoeusOptionPane.showErrorDialog("There are no awards in the subcontracting table.");
        }
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            blockEvents(true);
            if(source.equals(subcontractReportListForm.mnuItmExit)){
                exitApplication();
            }else if(source.equals(subcontractReportListForm.mnuItmPreferences)){
                showPreference();
            }else if(source.equals(subcontractReportListForm.mnuItmInbox)){
                showInboxDetails();
                //Added for Case#3682 - Enhancements related to Delegations- Start
            }else if(source.equals(subcontractReportListForm.mnuItmDelegations)){
                displayUserDelegation();              
                //Added for Case#3682 - Enhancements related to Delegations - End
            }else if(source.equals(subcontractReportListForm.mnuItmChangePassword)){
                showChangePassword();
            }else if(source.equals(subcontractReportListForm.mnuItmSelectAll) ||
            source.equals(subcontractReportListForm.btnSelectAll)){
                selectAll();
            }else if(source.equals(subcontractReportListForm.mnuItmDeselectAll) ||
            source.equals(subcontractReportListForm.btnDeselectAll)){
                deselectAll();
            }else if(source.equals(subcontractReportListForm.mnuItmClose) ||
                source.equals(subcontractReportListForm.btnClose)){
                    subcontractReportListForm.doDefaultCloseAction();
            }else if(source.equals(subcontractReportListForm.mnuItmEnterGoals) ||
                source.equals(subcontractReportListForm.btnGoals)){
                    enterGoals();
            }else if(source.equals(subcontractReportListForm.mnuItmValidationChecks)||
                source.equals(subcontractReportListForm.btnValidationChecks)){
                    doValidationChecks();
            }
            //Added by nadh - 19/01/2005 for Sort ToolBar Button - Start
            else if (source.equals(subcontractReportListForm.btnSort) || source.equals(subcontractReportListForm.mnuItmSort)) {
                showSort();
            }//Added by Nadh - End
            else if(source.equals(subcontractReportListForm.mnuItm294) ){
                    showPrintSubcontractForm("294");
            }
            else if(source.equals(subcontractReportListForm.mnuItm295) ){
                    showPrintSubcontractForm("295");
            }//Case 2110 Start
            else if(source.equals(subcontractReportListForm.mnuItmCurrentLocks)){
                    showLocksForm();
            } //Case 2110 End           
            else{
                CoeusOptionPane.showInfoDialog(
             coeusMessageResources.parseMessageKey("funcNotImpl_exceptionCode.1100"));
            }
        }catch (CoeusException coeusException){
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }
        finally{
            blockEvents(false);
        }
    }
    
    // added by Nadh to implement sorting proposals start - 19-01-2004
    /*
     * this method shows the sort window
     * return void
     */
    private void showSort() {
        if(vecSortedData==null) {
            vecSortedData = new Vector();
        }
        SortForm sortForm = new SortForm(subcontractReportListForm.tblReports,vecSortedData);
        Vector sortedData = sortForm.display();
        vecSortedData = (Vector)sortedData.get(1);
        if(((Integer)sortedData.get(0)).intValue() == OK_CLICKED)
            sorter.doSort(subcontractReportListForm.tblReports,vecSortedData);
        else
            return;
    }// Added by Nadh - end
    
    private void doValidationChecks() throws CoeusException{
        CoeusVector data = getValidationData();
        SubcontractValidationForm validationForm = new SubcontractValidationForm(mdiForm,data);
        validationForm.display();
    }
    
    private CoeusVector getValidationData() throws CoeusException{
        CoeusVector cvData=null;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(GET_VALIDATION_DATA);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                cvData = (CoeusVector)responder.getDataObject();
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
         return cvData;
    }
    
    private void enterGoals() throws CoeusException{
        int[] selectedRows = subcontractReportListForm.tblReports.getSelectedRows();
        int selRow = subcontractReportListForm.tblReports.getSelectedRow();
        if(selectedRows.length < 1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW));
            return ;
        }else if(selectedRows.length > 1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ONLY_ONE_AWARD));
            return ;
        }else{
            String awardNumber = (String)subcontractReportListForm.tblReports.getValueAt(selRow, AWARD_NUMBER_COL);
            Hashtable goalsData = getGoalsData(awardNumber);
            //Case 3587: Multicampus enhancement : Start
            //Check if for the selected award, user has MODIFY_SUBCONTRACTING_GOALS right.
            boolean goalRight = ((Boolean)goalsData.get(MODIFY_SUBCONTRACTING_GOALS)).booleanValue();
            if(goalRight){
            SubcontractGoalsForm goalsForm = new SubcontractGoalsForm(mdiForm,awardNumber);
            goalsForm.setFormData(goalsData);
            goalsForm.display();
            }else{
                CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(NO_RIGHTS_TO_ENTER_GOAL));
                return ;
            }
            //3587: End
        }
    }
    
    private Hashtable getGoalsData(String awardNumber) throws CoeusException{
        Hashtable htData = null;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(GET_GOALS_DATA);
        requester.setDataObject(awardNumber);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                htData = (Hashtable)responder.getDataObject();
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
        return htData;
    }
    
    private void selectAll(){
        int rowCount = subcontractReportListForm.tblReports.getRowCount();
        if(rowCount >0){
            subcontractReportListForm.tblReports.setRowSelectionInterval(0, rowCount-1);
        }
    }
    
    private void deselectAll(){
        int rowCount = subcontractReportListForm.tblReports.getRowCount();
        if(rowCount >0){
            subcontractReportListForm.tblReports.clearSelection();
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
    //Added for Case#3682 - Enhancements related to Delegations - End
    
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
    
    /**
     * To show the Change password
     */
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }
    
    //Case 2110 Start To get Current Locks for loggedin user
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End
    
    /**
     * To show the preference Screen
     */
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
    
    public void vetoableChange(PropertyChangeEvent propertyChangeEvent){
        if(propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)) {
            closeWindow();
        }
    }
    
    private void closeWindow(){
        mdiForm.removeFrame(CoeusGuiConstants.SUBCONTRACTING_REPORTS);
        subcontractReportListForm=null;
        cvReportData = null;
    }
    public class ReportTableModel extends AbstractTableModel{
        private String colNames[] = {"Award No.", "Sponsor Award No.", "Title",
        "Account No.","Status","Sponsor Code","Sponsor Name"};
        private Class colClass[] = {String.class,String.class,String.class,Integer.class,
        String.class,String.class,String.class};
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        public String getColumnName(int col){
            return colNames[col];
        }
        
        public void setData(CoeusVector cvReportData){
            cvReportData = cvReportData;
        }
        
        public int getRowCount() {
            if(cvReportData==null){
                return 0;
            }else{
                return cvReportData.size();
            }
        }
        
        public Object getValueAt(int row, int col) {
            AwardDetailsBean awardDetailsBean = (AwardDetailsBean)cvReportData.get(row);
            switch(col){
                case AWARD_NUMBER_COL:
                    return awardDetailsBean.getMitAwardNumber();
                case SPONSOR_AWARD_COLUMN:
                    return awardDetailsBean.getSponsorAwardNumber();
                case TITLE_COL:
                    return awardDetailsBean.getTitle();
                case ACCOUNT_COL:
                    return new Integer(awardDetailsBean.getAccountNumber());
                case STATUS_COL:
                    return awardDetailsBean.getStatusDescription();
                case SPONSOR_CODE_COL:
                    return awardDetailsBean.getSponsorCode();
                case SPONSOR_NAME_COL:
                    return awardDetailsBean.getSponsorName();
            }
            return EMPTY_STRING;
        }
    }
        // Calling a thread to bring up the Hour Glass Icon.
        public void blockEvents(boolean block) {
        if(blockingGlassPane == null) {
            blockingGlassPane = new BlockingGlassPane();
            CoeusGuiConstants.getMDIForm().setGlassPane(blockingGlassPane);
        }
        blockingGlassPane.block(block);
    }
        
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
        //Added by Nadh to get the column header and its status Start 19-01-2005
    //Inner Class Mouse Adapter - START
    class SubcontactRptMouseAdapter extends MouseAdapter{
        public void mouseClicked(MouseEvent mouseEvent){
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
                newSortedData.addElement(subcontractReportListForm.tblReports.getColumnName(column));
                newSortedData.addElement(new Integer(column));
                newSortedData.addElement(new Boolean(status == 1 ? true : false));
                if(vecSortedData == null)
                    vecSortedData = new Vector();
                vecSortedData.removeAllElements();
                vecSortedData.addElement(newSortedData);
            }else {
                vecSortedData = null;
            }
            
        }
    }// Added by Nadh - End
    
    //added for print subcontract
    private void showPrintSubcontractForm(String formID) throws CoeusException{
        
        Hashtable htFormParams = new Hashtable();
        if (formID.equals("294")){
            int[] selectedRows = subcontractReportListForm.tblReports.getSelectedRows();
            int selRow = subcontractReportListForm.tblReports.getSelectedRow();
            if(selectedRows.length < 1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW));
                return ;
            }else{
                String awardNum = (String)subcontractReportListForm.tblReports.getValueAt(selRow, AWARD_NUMBER_COL);
                htFormParams.put("FORM_ID",formID );
                htFormParams.put("AWARD_NUM",awardNum );
                //Bug fix : select multiple forms and print prints only the first selected - START
                String awardNums[] = new String[selectedRows.length];
                int rowIndex;
                for(int index = 0; index < selectedRows.length; index++) {
                    rowIndex = selectedRows[index];
                    //case 3587: Multicampus enhancement
                    awardNum = (String)subcontractReportListForm.tblReports.getValueAt(rowIndex, AWARD_NUMBER_COL);
                    if(!checkPrintingRights(awardNum)){
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(NO_RIGHT_TO_PRINT));
                        return ;
                    }
                    //3587 End
                    awardNums[index] = awardNum;
                }
                htFormParams.put("AWARD_NUMS",awardNums );
                //Bug fix : select multiple forms and print prints only the first selected - END
            }
           
        }
        htFormParams.put("FORM_ID",formID );
        PrintSubcontractController  printSub = new PrintSubcontractController(htFormParams);
//        printSub.
//        int[] selectedRows = subcontractReportListForm.tblReports.getSelectedRows();
//        int selRow = subcontractReportListForm.tblReports.getSelectedRow();
//        if(selectedRows.length < 1){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW));
//            return ;
//        }else if(selectedRows.length > 1){
//            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_ONLY_ONE_AWARD));
//            return ;
//        }else{
//            String awardNumber = (String)subcontractReportListForm.tblReports.getValueAt(selRow, AWARD_NUMBER_COL);
//            Hashtable goalsData = getGoalsData(awardNumber);
//            SubcontractGoalsForm goalsForm = new SubcontractGoalsForm(mdiForm,awardNumber);
//            goalsForm.setFormData(goalsData);
//            goalsForm.display();
//        }
    }
    //Case 3587:multicampus enhancement
    private boolean checkPrintingRights(String awardNumber){
        boolean hasRight = false;
        RequesterBean requester;
        ResponderBean responder;
        requester = new RequesterBean();
        requester.setFunctionType(CHECK_USER_HAS_GOAL_RIGHT);
        requester.setDataObject(awardNumber);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connect, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                hasRight = ((Boolean)responder.getDataObject()).booleanValue();
            }
        }
        return hasRight;
    }
}
