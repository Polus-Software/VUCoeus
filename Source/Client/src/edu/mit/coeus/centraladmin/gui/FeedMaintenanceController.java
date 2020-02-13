/*
 * FeedMaintenanceController.java
 *
 * Created on December 31, 2004, 11:33 AM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.award.bean.SapFeedDetailsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.gui.InboxDetailForm;
import edu.mit.coeus.user.gui.UserDelegationForm;
import edu.mit.coeus.user.gui.UserPreferencesForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.ChangePassword;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CurrentLockForm;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  surekhan
 */
public class FeedMaintenanceController implements ListSelectionListener,MouseListener,ActionListener,VetoableChangeListener{
    
    
    private FeedMaintenanceForm feedMaintenanceForm;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private static final String EMPTY = "";
    private SapFeedBatchesTable sapFeedBatchesTable;
    private SapFeedDetailsTableModel sapFeedDetailsTableModel;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    private CoeusVector cvFeedBatches;
    private CoeusVector cvAllSapFeedDetails;
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/CentralAdminMaintenanceServlet";
    
    private static final int BATCH_ID = 0;
    private static final int BATCH_FILE_NAME = 1;
    private static final int NO_OF_RECORDS = 2;
    private static final int TIME_STAMP = 3;
    private static final int USER = 4;
    private int batchId;
    
    private static final int FEED_ID = 0;
    private static final int TRANS_ID = 1;
    private static final int AWARD_NUM = 2;
    private static final int SEQ_NUM = 3;
    private static final int TYPE = 4;
    private static final int STATUS = 5;
    private static final int TIME_STAMP_VALUE = 6;
    private static final int USER_VALUE = 7;
    
    private SapFeedDetailsRenderer sapFeedDetailsRenderer;
    private ShowBatchFeedDataForm showBatchFeedDataForm;
    private boolean modified;
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    /*for BugFix:1580*/
    private boolean sortCodeAsc = false;
    private boolean sortDescAsc = true;
    
    
    /*Select a feed to reject*/
    private static final String SELECT_FEED = "feedMaintenance_exceptionCode.2001";
    
    /*Selected feed has already been rejected*/
    private static final String REJECT_FEED = "feedMaintenance_exceptionCode.2002";
    
    /*Feed marked as Error, cannot be Rejected*/
    private static final String ERROR_REJECT = "feedMaintenance_exceptionCode.2003";
    
    /*Cancelled feed cannot be Rejected*/
    private static final String CANCELL_MSG = "feedMaintenance_exceptionCode.2004";
    
    /*Select a feed to Un-Reject */
    private static final String SELECT_UNDOREJECT = "feedMaintenance_exceptionCode.2005";
    
    /*This feed is not Rejected*/
    private static final String NOT_REJECT = "feedMaintenance_exceptionCode.2006";
    
    /*Do u want to save changes*/
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    /*Select an award*/
    private static final String SELECT_AWARD = "feedMaintenance_exceptionCode.2009";
    
    /*Select a Batch*/
    private static final String SELECT_BATCH = "feedMaintenance_exceptionCode.2010";
    
    /*This batch does not have feed data.*/
    private static final String NO_FEED_DATA = "feedMaintenance_exceptionCode.2011";
    
    private CoeusMessageResources coeusMessageResources;
    
    private static final String REQUIRED_DATE_FORMAT = "M/d/yyyy HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    
    private boolean dataModified;
    
     //holds change password instance.
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    private CoeusVector cvSaveFeeds;
    
    private SapFeedBatchesRenderer sapFeedBatchesRenderer;
    
    private boolean isSaved;
    
    private HashMap objMap = new HashMap();
    //Case#4579 - SAPFeed - Resend batch error - Start
    private static final String NO_BATCH_TO_RESEND = "feedMaintenance_exceptionCode.2012";
    private static final String NO_FEED_DATA_TO_SHOW = "feedMaintenance_exceptionCode.2013";
    //Case#4579 - End
    
    /** Creates a new instance of FeedAMaintenanceController */
    public FeedMaintenanceController(CoeusAppletMDIForm mdiform) {
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        cvFeedBatches = new CoeusVector();
        cvAllSapFeedDetails = new CoeusVector();
        cvSaveFeeds = new CoeusVector();
        sapFeedDetailsRenderer = new SapFeedDetailsRenderer();
        sapFeedBatchesRenderer = new SapFeedBatchesRenderer();
        initComponents();
        setFormData(null);
        setColumnData();
        registerComponents();
        //display();
    }
    
    /**
     * To initializes the components before opening the screen
     * @return void
     */
    private void initComponents(){
        try{
            feedMaintenanceForm = new FeedMaintenanceForm("Feed Maintenance", mdiForm);
            sapFeedBatchesTable = new SapFeedBatchesTable();
            sapFeedDetailsTableModel = new SapFeedDetailsTableModel();
            feedMaintenanceForm.initComponents();
            feedMaintenanceForm.tblSapFeedBatches.setModel(sapFeedBatchesTable);
            feedMaintenanceForm.tblSapFeedDetails.setModel(sapFeedDetailsTableModel);
            feedMaintenanceForm.postInitComponents();
            
        }catch (Exception exception){
            CoeusOptionPane.showErrorDialog(exception.getMessage());
            exception.printStackTrace();
        }
    }
    
    
      
    /*to display the form*/
    public void display(){
        try{
            mdiForm.putFrame("Feed Maintenance", feedMaintenanceForm);
            mdiForm.getDeskTopPane().add(feedMaintenanceForm);
            //Case 3234 - START
             if(feedMaintenanceForm.tblSapFeedBatches.getRowCount() > 0){
                feedMaintenanceForm.tblSapFeedBatches.setRowSelectionInterval(0,0);
            }
            //Case 3234 - END
            feedMaintenanceForm.setSelected(true);
            feedMaintenanceForm.setVisible(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    /*to get the data from the data base and set it to the form*/
    private void setFormData(Object data){
        getFeedData(null);
        int selectedRow = feedMaintenanceForm.tblSapFeedBatches.getSelectedRow();
        if(selectedRow!= -1){
            //Case 3234 - START
            //if(feedMaintenanceForm.tblSapFeedBatches.getRowCount() > 0){
                //feedMaintenanceForm.tblSapFeedBatches.setRowSelectionInterval(0,0);
            //}
            //Case 3234 - END
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedBatches.get(selectedRow);
            batchId = bean.getBatchId();
            feedMaintenanceForm.lblFeedDetails.setText("Details for batch : " +batchId);
            getAllSapFeedDetails(batchId);
            if(feedMaintenanceForm.tblSapFeedDetails.getRowCount() > 0){
                feedMaintenanceForm.tblSapFeedDetails.setRowSelectionInterval(0,0);
            }
            int selRow = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
            int row = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
            SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(selectedRow);
            String statusText = sapBean.getErrorMessage();
            if(statusText == null){
                feedMaintenanceForm.lblStatus.setText("Error :" +"");
            }else{
                feedMaintenanceForm.lblStatus.setText("Error :" +statusText);
            }
        }        
    }
    
    /*to get the batch feed data from the data base*/
    private void getFeedData(Object data) {
        try{
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('J');
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                cvFeedBatches = (CoeusVector)response.getDataObject();
				if (cvFeedBatches != null && cvFeedBatches.size() > 0) {
					cvFeedBatches.sort("batchId" , false);
				}
            }else {
                throw new CoeusClientException(response.getMessage());
            }
        }catch(CoeusClientException e){
            e.printStackTrace();
        }
        
    }
    
    
    /*to get the sap feed details from the data base*/
    private void getAllSapFeedDetails(int batchId){
        int batchIdvalue = batchId;
        try{
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('K');
            requester.setDataObject(new Integer(batchIdvalue));
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                cvAllSapFeedDetails = (CoeusVector)response.getDataObject();
                //Modified for Case#4579 - SAPFeed - Resend batch error - Start
                //cvAllSapFeedDetails.sort("feedId" , true);
                if(cvAllSapFeedDetails != null && cvAllSapFeedDetails.size() > 0){
                    cvAllSapFeedDetails.sort("feedId" , true);
                }else{
                    cvAllSapFeedDetails = new CoeusVector();
                }
                //Case#4579 - end
            }else {
                throw new CoeusClientException(response.getMessage());
            }
        }catch(CoeusClientException e){
            e.printStackTrace();
        }
    }
    
    /*to register all the listeners and the components*/
    private void registerComponents(){
        feedMaintenanceForm.tblSapFeedBatches.getSelectionModel().addListSelectionListener(this);
        feedMaintenanceForm.tblSapFeedDetails.getSelectionModel().addListSelectionListener(this);
        feedMaintenanceForm.tblSapFeedBatches.addMouseListener(this);
        
        //listener for header for sorting purpose
        feedMaintenanceForm.tblSapFeedBatches.getTableHeader().addMouseListener(this);
        feedMaintenanceForm.tblSapFeedDetails.getTableHeader().addMouseListener(this);
        
        feedMaintenanceForm.mnuItmRejectFeed.addActionListener(this);
        feedMaintenanceForm.mnuItmUndoReject.addActionListener(this);
        feedMaintenanceForm.mnuItmAwardFeedHistory.addActionListener(this);
        feedMaintenanceForm.mnuItmShowPendingFeeds.addActionListener(this);
        feedMaintenanceForm.mnuItmInbox.addActionListener(this);
        feedMaintenanceForm.mnuItmSave.addActionListener(this);
        feedMaintenanceForm.btnSave.addActionListener(this);
        feedMaintenanceForm.btnClose.addActionListener(this);
        feedMaintenanceForm.mnuItmChangePassword.addActionListener(this);
        feedMaintenanceForm.mnuItmExit.addActionListener(this);
        feedMaintenanceForm.mnuItmClose.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - Start
        feedMaintenanceForm.mnuItmDelegations.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        feedMaintenanceForm.mnuItmPreferences.addActionListener(this);
        feedMaintenanceForm.mnuItmShowFeedData.addActionListener(this);
        feedMaintenanceForm.mnuItmResendBatch.addActionListener(this);
        //Case 2110 Start
        feedMaintenanceForm.mnuItmCurrenLocks.addActionListener(this);
        //Case 2110 End
        feedMaintenanceForm.addVetoableChangeListener(this);
    }
    
    
    /**
     * Setting up the column data
     * @return void
     **/
    private void setColumnData(){
        JTableHeader tableHeader = feedMaintenanceForm.tblSapFeedBatches.getTableHeader();
        JTableHeader tableHeaders = feedMaintenanceForm.tblSapFeedDetails.getTableHeader();
        feedMaintenanceForm.tblSapFeedBatches.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        feedMaintenanceForm.tblSapFeedDetails.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeaders.setReorderingAllowed(false);
        tableHeaders.setFont(CoeusFontFactory.getLabelFont());
        feedMaintenanceForm.tblSapFeedBatches.setRowHeight(22);
        feedMaintenanceForm.tblSapFeedDetails.setRowHeight(22);
        feedMaintenanceForm.tblSapFeedBatches.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        feedMaintenanceForm.tblSapFeedDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        //Case 3234 - START
        //if(feedMaintenanceForm.tblSapFeedBatches.getRowCount() > 0){
            //feedMaintenanceForm.tblSapFeedBatches.setRowSelectionInterval(0,0);
        //}
        //Case 3234 - END
        
        // setting up the table columns
        TableColumn column = feedMaintenanceForm.tblSapFeedBatches.getColumnModel().getColumn(0);
        column.setPreferredWidth(75);
        column.setCellRenderer(sapFeedBatchesRenderer);
        
        column = feedMaintenanceForm.tblSapFeedBatches.getColumnModel().getColumn(1);
        column.setPreferredWidth(200);
        column.setCellRenderer(sapFeedBatchesRenderer);
       
        column = feedMaintenanceForm.tblSapFeedBatches.getColumnModel().getColumn(2);
        column.setPreferredWidth(180);
        column.setCellRenderer(sapFeedBatchesRenderer);
        
        column = feedMaintenanceForm.tblSapFeedBatches.getColumnModel().getColumn(3);
        column.setPreferredWidth(180);
        column.setCellRenderer(sapFeedBatchesRenderer);
        
        column = feedMaintenanceForm.tblSapFeedBatches.getColumnModel().getColumn(4);
        column.setPreferredWidth(200);
        column.setCellRenderer(sapFeedBatchesRenderer);
        
        // setting up the table columns
        TableColumn columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(0);
        columnvalue.setPreferredWidth(75);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(1);
        columnvalue.setPreferredWidth(110);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
       
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(2);
        columnvalue.setPreferredWidth(110);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(3);
        columnvalue.setPreferredWidth(110);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(4);
        columnvalue.setPreferredWidth(80);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(5);
        columnvalue.setPreferredWidth(80);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(6);
        columnvalue.setPreferredWidth(180);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumn(7);
        columnvalue.setPreferredWidth(190);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        
    }
    
    /*the action performed on the value changed*/
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        Object source = listSelectionEvent.getSource();
        mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
            int selRow = feedMaintenanceForm.tblSapFeedBatches.getSelectedRow();
            int row = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
            if( (source.equals(feedMaintenanceForm.tblSapFeedBatches.getSelectionModel()) )&& (selRow >= 0 ) &&
            cvFeedBatches !=null && cvFeedBatches.size() > 0) {
                SapFeedDetailsBean bean = null;
                bean = (SapFeedDetailsBean)cvFeedBatches.get(selRow);
                int batchId = bean.getBatchId();
                CoeusVector cvData = cvAllSapFeedDetails.filter(new Equals("batchId" ,new Integer(batchId)));
                getAllSapFeedDetails(batchId);
                sapFeedDetailsTableModel.setData(cvAllSapFeedDetails);
                feedMaintenanceForm.tblSapFeedDetails.setModel(sapFeedDetailsTableModel);
                feedMaintenanceForm.lblFeedDetails.setText("Details for batch : " + batchId);
                if(feedMaintenanceForm.tblSapFeedDetails.getRowCount() > 0){
                    feedMaintenanceForm.tblSapFeedDetails.setRowSelectionInterval(0,0);
                }
                
            }else if( (source.equals(feedMaintenanceForm.tblSapFeedDetails.getSelectionModel()) )&& (row >= 0 ) &&
            cvAllSapFeedDetails !=null && cvAllSapFeedDetails.size() > 0) {
                SapFeedDetailsBean bean = null;
                bean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(row);
                String msg = bean.getErrorMessage();
                if(msg == null){
                    feedMaintenanceForm.lblStatus.setText("Error : " +"");
                }else{
                    feedMaintenanceForm.lblStatus.setText("Error : " +msg);
                }
            }
        }finally{
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

    }
    
    /*the actions performed on the mouse click*/
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        int clickCount = mouseEvent.getClickCount();
        int selRow = feedMaintenanceForm.tblSapFeedBatches.getSelectedRow();
        SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedBatches.get(selRow);
        int batchId = bean.getBatchId();
        
        int selectedRows = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
        SapFeedDetailsBean detailBean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(selectedRows);
        int feedId = detailBean.getFeedId();
             
        if(source.equals(feedMaintenanceForm.tblSapFeedBatches) && clickCount == 2){
           showFeedData();
           
        }else if(source.equals(feedMaintenanceForm.tblSapFeedBatches.getTableHeader())){
            int row = 0;
            if(clickCount == 1){
             Point clickedPoint = mouseEvent.getPoint();
             int xPosition = (int)clickedPoint.getX();
             int columnIndex = feedMaintenanceForm.tblSapFeedBatches.getColumnModel().getColumnIndexAtX(xPosition);
             switch (columnIndex) {
                 case BATCH_ID:
                     if(sortCodeAsc) {
                         //Code already sorted in Ascending order. Sort now in Descending order.
                         cvFeedBatches.sort("batchId", false);
                         sortCodeAsc = false;
                     }else {
                         //Code already sorted in Descending order. Sort now in Ascending order.
                         cvFeedBatches.sort("batchId", true);
                         sortCodeAsc = true;
                     }
                     break;
                 case BATCH_FILE_NAME:
                     if(sortCodeAsc){
                         cvFeedBatches.sort("batchFileName",false);
                         sortCodeAsc = false;
                     }else {
                         cvFeedBatches.sort("batchFileName",true);
                         sortCodeAsc = true;
                     }
                     break;
                 case NO_OF_RECORDS:
                     if(sortCodeAsc){
                         cvFeedBatches.sort("noOfRecords",false);
                         sortCodeAsc = false;
                     }else {
                         cvFeedBatches.sort("noOfRecords",true);
                         sortCodeAsc = true;
                     }
                     break;
                 case TIME_STAMP:
                     if(sortCodeAsc){
                         cvFeedBatches.sort("updateTimestamp",false);
                         sortCodeAsc = false;
                     }else {
                         cvFeedBatches.sort("updateTimestamp",true);
                         sortCodeAsc = true;
                     }
                     break;
                 case USER:
                     if(sortCodeAsc){
                         cvFeedBatches.sort("updateUser",false);
                         sortCodeAsc = false;
                     }else {
                         cvFeedBatches.sort("updateUser",true);
                         sortCodeAsc = true;
                     }
                     break;
                     
             }//End Switch
             sapFeedBatchesTable.fireTableDataChanged();
             for(int i = 0;i<cvFeedBatches.size();i++){
                 SapFeedDetailsBean dataBean = (SapFeedDetailsBean)cvFeedBatches.get(i);
                 if(dataBean.getBatchId() == batchId){
                     row = i;
                 }
             }
             if(feedMaintenanceForm.tblSapFeedBatches.getRowCount() > 0){
                 feedMaintenanceForm.tblSapFeedBatches.setRowSelectionInterval(row,row);
              }
            
            }
        }else if(source.equals(feedMaintenanceForm.tblSapFeedDetails.getTableHeader())){
            int row = 0;
            if(clickCount == 1){
             Point clickedPoint = mouseEvent.getPoint();
             int xPosition = (int)clickedPoint.getX();
             int columnIndex = feedMaintenanceForm.tblSapFeedDetails.getColumnModel().getColumnIndexAtX(xPosition);
             switch (columnIndex) {
                 case FEED_ID:
                     if(sortCodeAsc) {
                         //Code already sorted in Ascending order. Sort now in Descending order.
                         cvAllSapFeedDetails.sort("feedId", true);
                         sortCodeAsc = false;
                     }else {
                         //Code already sorted in Descending order. Sort now in Ascending order.
                         cvAllSapFeedDetails.sort("feedId", false);
                         sortCodeAsc = true;
                     }
                     break;
                 case TRANS_ID:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("transactionId",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("transactionId",false);
                         sortCodeAsc = true;
                     }
                     break;
                 case AWARD_NUM:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("mitAwardNumber",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("mitAwardNumber",false);
                         sortCodeAsc = true;
                     }
                     break;
                 case SEQ_NUM:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("sequenceNumber",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("sequenceNumber",false);
                         sortCodeAsc = true;
                     }
                     break;
                 case TYPE:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("feedType",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("feedType",false);
                         sortCodeAsc = true;
                     }
                     break; 
                 case STATUS:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("feedStatus",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("feedStatus",false);
                         sortCodeAsc = true;
                     }
                     break;
                case TIME_STAMP_VALUE:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("updateTimestamp",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("updateTimestamp",false);
                         sortCodeAsc = true;
                     }
                     break;
               case USER_VALUE:
                     if(sortCodeAsc){
                         cvAllSapFeedDetails.sort("updateUser",true);
                         sortCodeAsc = false;
                     }else {
                         cvAllSapFeedDetails.sort("updateUser",false);
                         sortCodeAsc = true;
                     }
                     break;       
                     
             }//End Switch
            sapFeedDetailsTableModel.fireTableDataChanged();
            for(int i = 0;i<cvAllSapFeedDetails.size();i++){
                 SapFeedDetailsBean dataBean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(i);
                 if(dataBean.getFeedId() == feedId){
                     row = i;
                 }
             }
            if(feedMaintenanceForm.tblSapFeedDetails.getRowCount() > 0){
                 feedMaintenanceForm.tblSapFeedDetails.setRowSelectionInterval(row,row);
                }
            }
        }
    }
    
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    
    /* the actions performed on the click of the buttons and the menu items in the form*/
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
        if(source.equals(feedMaintenanceForm.mnuItmRejectFeed)){
            performRejectAction();
        }else if(source.equals(feedMaintenanceForm.mnuItmUndoReject)){
            performUndoReject();
        }else if(source.equals(feedMaintenanceForm.mnuItmAwardFeedHistory)){
            int row = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
            if(row == -1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_AWARD));
                return;
            }else{
                SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(row);
                String awardNumber = bean.getMitAwardNumber();
                edu.mit.coeus.centraladmin.gui.AwardHistoryForm form = new AwardHistoryForm(awardNumber);
            }
        }else if(source.equals(feedMaintenanceForm.mnuItmShowPendingFeeds)){
            PendingFeedsForm form = new PendingFeedsForm();
        }else if(source.equals(feedMaintenanceForm.mnuItmInbox)){
            showInboxDetails();
        //Added for Case#3682 - Enhancements related to Delegations - Start
        }else if(source.equals(feedMaintenanceForm.mnuItmDelegations)){
            displayUserDelegation();
        //Added for Case#3682 - Enhancements related to Delegations - End
        }else if(source.equals(feedMaintenanceForm.mnuItmClose)|| 
                        source.equals(feedMaintenanceForm.btnClose)) {
                      feedMaintenanceForm.doDefaultCloseAction();
            
        }else if(source.equals(feedMaintenanceForm.btnSave) ||
                    source.equals(feedMaintenanceForm.mnuItmSave)) {
                        
                    try {
                        if(modified) {
                            saveFormData();
                            //isSaved = true;
                        }
                    } catch (CoeusException coeusException) {
                         coeusException.printStackTrace();
                    }
        }else if(source.equals(feedMaintenanceForm.mnuItmChangePassword)){
            showChangePassword();
        }else if(source.equals(feedMaintenanceForm.mnuItmPreferences)){
            showPreference();
        }else if(source.equals(feedMaintenanceForm.mnuItmExit)){
            exitApplication();
        }else if(source.equals(feedMaintenanceForm.mnuItmShowFeedData)){
            showFeedData();
        }else if(source.equals(feedMaintenanceForm.mnuItmResendBatch)){
            showRescendBatch();
        }//CAse 2110 Start
        else if(source.equals(feedMaintenanceForm.mnuItmCurrenLocks)){
           showLocksForm();
        }//CAse 2110 End        
        }catch(CoeusException coeusException){
            coeusException.printStackTrace();
            CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /** Listens to window closing event.
     * @param propertyChangeEvent propertyChangeEvent
     * @throws PropertyVetoException PropertyVetoException
     */    
    public void vetoableChange(java.beans.PropertyChangeEvent propertyChangeEvent) throws java.beans.PropertyVetoException {
        if(isSaved) return ;
        boolean changed = ((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
        if(propertyChangeEvent.getPropertyName().equals(feedMaintenanceForm.IS_CLOSED_PROPERTY) && changed) {
                close();
        }
    }
    
    /*the action performed on the close of the window frame*/
    private void close()throws PropertyVetoException{
        
        if( modified ) {
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_CHANGES), CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES) {
                try{
                    saveFormData();
                }catch (CoeusException coeusException) {
                    coeusException.printStackTrace();
                }
            }else if(selection == CoeusOptionPane.SELECTION_CANCEL) {
               throw new PropertyVetoException(EMPTY, null);
            }else if(selection == CoeusOptionPane.SELECTION_NO ) {
                feedMaintenanceForm.dispose();
            }
            
        }
        mdiForm.removeFrame("Feed Maintenance");
        isSaved = true;
    }
   
    
    /* to show the rescend batch form*/
    private void  showRescendBatch(){
        int selRow = feedMaintenanceForm.tblSapFeedBatches.getSelectedRow();
        //Modified for Case#4579 - SAPFeed - Resend batch error  - Start
//        SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedBatches.get(selRow);
        SapFeedDetailsBean bean = null;
        if(cvFeedBatches != null && cvFeedBatches.size() > 0){
            bean = (SapFeedDetailsBean)cvFeedBatches.get(selRow);
            if(selRow == -1){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_BATCH));
                return;
            }else{
                //Modified for Case#4579 - SAPFeed - Resend batch error  - Start
                if(bean != null){//Case#4579 - End
                    int batchId = bean.getBatchId();
                    CoeusVector cvSapDetails = new CoeusVector();
                    getAllSapFeedDetails(batchId);
                    cvSapDetails = cvAllSapFeedDetails;
                    ResendBatchForm form = new ResendBatchForm(batchId,cvSapDetails);
                }
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_BATCH_TO_RESEND));
            return;
        }
        //Case#4579 - End
       
    }
   
    /*to show the feed data form*/
    private void showFeedData(){
        int selectedRow = feedMaintenanceForm.tblSapFeedBatches.getSelectedRow();
        //Modified for Case#4579 - SAPFeed - Resend batch error  - Start
//        SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvFeedBatches.get(selectedRow);
        SapFeedDetailsBean sapBean = null;
        if(cvFeedBatches != null && cvFeedBatches.size() > 0){
            sapBean = (SapFeedDetailsBean)cvFeedBatches.get(selectedRow);
            int batchIdValue = sapBean.getBatchId();
            showBatchFeedDataForm = new ShowBatchFeedDataForm("Feed data for batch "+ batchIdValue, mdiForm,batchIdValue);
            if(showBatchFeedDataForm.display()){
                mdiForm.getDeskTopPane().add(showBatchFeedDataForm);
                showBatchFeedDataForm.setVisible(true);
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_FEED_DATA));
                return;
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_FEED_DATA_TO_SHOW));
            return;
        }
        
        
    }
    
    /**
     * Method used to close the application after confirmation.
     */
    public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == CoeusOptionPane.SELECTION_YES) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
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
    
    
    //  to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }
    
       
     // displays change password
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
    //CAse 2110 End
    
    /*to save the data to the data base*/
    private void saveFormData() throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('R');
        if(cvSaveFeeds != null && cvSaveFeeds.size() > 0){
            requesterBean.setDataObject(cvSaveFeeds);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CONNECTION_STRING);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }else {
                    modified = false;
                }
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
    
    /*the action performed on the click of the Reject Button*/
    private void performRejectAction(){
        int selRow = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_FEED));
            return;
        }
        SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(selRow);
        int feedId = sapBean.getFeedId();
        String status = sapBean.getFeedStatus();
        if(status.equals("R")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REJECT_FEED));
            return;
        }else if(status.equals("F")){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                "Are you sure you want to reject the feed - "+ feedId + " " ,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    sapFeedDetailsTableModel.setValueAt("R", selRow,5);
                    sapBean.setFeedStatus("R");
                    sapBean.setAcType(TypeConstants.UPDATE_RECORD);
                    sapBean.setUpdateUser(sapBean.getUpdateUser());
                    cvSaveFeeds.add(sapBean);
                    sapFeedDetailsTableModel.fireTableDataChanged();
                    feedMaintenanceForm.tblSapFeedDetails.setRowSelectionInterval(selRow,selRow);
                    dataModified = true; 
                } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                    return;
                }
       }else if(status.equals("E")){
           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERROR_REJECT));
           return;
       }else if(status.equals("C")){
           CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(CANCELL_MSG));
           return;
       }
        
    }
    
    /*The action performed on the click of the Undo Reject Button*/
    private void performUndoReject(){
        int selRow = feedMaintenanceForm.tblSapFeedDetails.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_UNDOREJECT));
            return;
        }
        SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(selRow);
        int feedId = sapBean.getFeedId();
        String status = sapBean.getFeedStatus();
        if(status.equals("R")){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            "Are you sure you want to un-reject the feed - " + feedId + " "  ,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                sapFeedDetailsTableModel.setValueAt("F", selRow,5);
                sapBean.setFeedStatus("F");
                sapBean.setAcType(TypeConstants.UPDATE_RECORD);
                cvSaveFeeds.add(sapBean);
                sapFeedDetailsTableModel.fireTableDataChanged();
                feedMaintenanceForm.tblSapFeedDetails.setRowSelectionInterval(selRow,selRow);
            } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                return;
            }
        }else if(status.equals("F") || status.equals("E") || status.equals("C")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NOT_REJECT));
            return;
        }
    }
    
    
    /*the model for the SapFeedBatchesTable*/
    public class SapFeedBatchesTable extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {"Batch Id","Batch File Name","No Of Records Fed","Timestamp","User"};
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class,String.class, String.class,String.class};
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To check whether the table cell is editable or not
         * @param row int
         * @param col int
         * @return boolean
         **/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvFeedBatches == null){
                return 0;
            } else {
                return cvFeedBatches.size();
            }
            
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvFeedBatches) {
            cvFeedBatches = cvFeedBatches;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            SapFeedDetailsBean sapFeedDetailsBean = (SapFeedDetailsBean)cvFeedBatches.get(rowIndex);
            switch(columnIndex){
                case BATCH_ID:
                    return  new Integer(sapFeedDetailsBean.getBatchId());
                case BATCH_FILE_NAME:
                    return sapFeedDetailsBean.getBatchFileName();
                case NO_OF_RECORDS:
                    return new Integer(sapFeedDetailsBean.getNoOfRecords());
                case TIME_STAMP:
                    return sapFeedDetailsBean.getUpdateTimestamp();
                case USER:
                    /*
                     * UserId to UserName Enhancement - Start
                     * Added new property getUpdateUserName to get username
                     */
                    return sapFeedDetailsBean.getUpdateUserName();
                    // UserId to UserName Enhancement - End
            }
            return EMPTY;
        }
        
    /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    
    
    /*The table model for the SapFeedDetails table*/
    public class SapFeedDetailsTableModel extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {"Feed Id","Transaction Id","Award Number","Seq Number","Type","Status","TimeStamp" ,"User"};
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class,String.class, String.class,String.class,String.class,String.class,String.class};
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To check whether the table cell is editable or not
         * @param row int
         * @param col int
         * @return boolean
         **/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvAllSapFeedDetails == null){
                return 0;
            } else {
                return cvAllSapFeedDetails.size();
            }
            
        }
        
        
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvAllSapFeedDetails) {
            cvAllSapFeedDetails = cvAllSapFeedDetails;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            SapFeedDetailsBean sapFeedDetailsBean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(rowIndex);
            switch(columnIndex){
                case FEED_ID:
                   return  new Integer(sapFeedDetailsBean.getFeedId());
               case TRANS_ID:
                    return sapFeedDetailsBean.getTransactionId();
                case AWARD_NUM:
                    return sapFeedDetailsBean.getMitAwardNumber();
                case SEQ_NUM:
                    return new Integer(sapFeedDetailsBean.getSequenceNumber()).toString();
                case TYPE:
                    String value = "";
                    if(sapFeedDetailsBean.getFeedType().equals("C")){
                        value = "Change";
                        return value;
                    }else if(sapFeedDetailsBean.getFeedType().equals("N")){
                        value = "New";
                        return value;
                    }
                    return value;
                case STATUS:
                    String statusValue = "";
                    if(sapFeedDetailsBean.getFeedStatus().equals("F")){
                        statusValue = "Fed";
                        return statusValue;
                    }else if(sapFeedDetailsBean.getFeedStatus().equals("E")){
                        statusValue = "Error";
                        return statusValue;
                    }else if(sapFeedDetailsBean.getFeedStatus().equals("R")){
                        statusValue = "Reject";
                        return statusValue;
                    }else if(sapFeedDetailsBean.getFeedStatus().equals("C")){
                        statusValue = "Cancelled";
                        return statusValue;
                    }
                    return statusValue;
                case TIME_STAMP_VALUE:
                    return sapFeedDetailsBean.getUpdateTimestamp();
                case USER_VALUE:
                    /*
                     * UserId to UserName Enhancement - Start
                     * Added new getter property getUpdateUserName to get the 
                     * corresponding user name from the preocedure
                     */
                    return sapFeedDetailsBean.getUpdateUserName();
                    // UserId to UserName Enhancement - End
            }
            return EMPTY;
        }
        
       /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(row);
            switch(col){
                case STATUS:
                    if (!value.toString().trim().equals(bean.getFeedStatus())) {
                        bean.setFeedStatus(value.toString());
                        modified = true;
                    }
                    break;
            }
            sapFeedDetailsTableModel.fireTableDataChanged();
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    
    /*the renderer for the SapFeedBatches Table*/
    class SapFeedBatchesRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private JLabel lblText;
        
        public SapFeedBatchesRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,5,0,0));
            lblText.setBorder(new javax.swing.border.EmptyBorder(0,5,0,0));
            setBorder(new javax.swing.border.EmptyBorder(0,5,0,0));
            
        }
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedBatches.get(row);
            java.awt.Color color = feedMaintenanceForm.tblSapFeedDetails.getSelectionBackground();
            switch(col){
                case BATCH_ID:
                     if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }
                    if(value!= null && !(value.toString().trim().equals(EMPTY))){
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
                case BATCH_FILE_NAME:
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }
                    if(value!= null && !(value.toString().trim().equals(EMPTY))){
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
                case NO_OF_RECORDS:
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }
                    if(value!= null && !(value.toString().trim().equals(EMPTY))){
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
                case TIME_STAMP:
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }
                    if(value!= null && !(value.toString().trim().equals(EMPTY))){
                        value = simpleDateFormat.format(value);
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
                case USER:
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }
                    if(value!= null && !(value.toString().trim().equals(EMPTY))){
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }
                    
                    return lblText;
            }
            return lblText;
        }
    }
    
    
    /* renderer for the FEED DETAILS TABLE table*/
    class SapFeedDetailsRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private JLabel lblText;
        
        public SapFeedDetailsRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,5,0,0));
            lblText.setBorder(new javax.swing.border.EmptyBorder(0,5,0,0));
        }
        
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
            String error = "E";
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAllSapFeedDetails.get(row);
            java.awt.Color color = feedMaintenanceForm.tblSapFeedDetails.getSelectionBackground();
            switch(col){
                
                case FEED_ID:
                    if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case TRANS_ID:
                    lblText.setForeground(java.awt.Color.black);
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                   return lblText;
                case AWARD_NUM:
                    if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                   return lblText; 
                case SEQ_NUM:
                    if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                   }else{
                        lblText.setBackground(disabledBackground);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    
                   return lblText;    
               case TYPE:
                    if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                   if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                   }else{
                        lblText.setBackground(disabledBackground);
                    }
                   
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                   return lblText;   
              case STATUS:
                  if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                  if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                  }else{
                        lblText.setBackground(disabledBackground);
                    }
                 
                     if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                   return lblText; 
                   
              case TIME_STAMP_VALUE:
                  if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                    }else{
                        lblText.setBackground(disabledBackground);
                    }
                    
                    if(value!= null && !(value.toString().trim().equals(EMPTY))){
                         value = simpleDateFormat.format(value);
                         txtComponent.setText(value.toString());
                         lblText.setText(txtComponent.getText());
                     }else{
                         txtComponent.setText(EMPTY);
                         lblText.setText(txtComponent.getText());
                     }
                    
                 return lblText;
             case USER_VALUE:
                 if(bean.getFeedStatus().equals(error)){
                        lblText.setForeground(java.awt.Color.RED);
                    }else{
                        lblText.setForeground(java.awt.Color.black);
                    }
                 if(isSelected){
                        lblText.setBackground(color);
                        lblText.setForeground(java.awt.Color.WHITE);
                 }else{
                        lblText.setBackground(disabledBackground);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY)){
                        txtComponent.setText(EMPTY);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                   return lblText;      
                   
            }
            return lblText;
        }
        
    }
  
}


