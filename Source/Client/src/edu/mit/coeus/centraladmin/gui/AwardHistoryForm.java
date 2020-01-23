/*
 * AwardHistoryForm.java
 *
 * Created on January 5, 2005, 12:28 PM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.award.bean.SapFeedDetailsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  surekhan
 */
public class AwardHistoryForm extends javax.swing.JPanel implements ActionListener,MouseListener{
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgAwardHistory;
    private static final String WINDOW_TITLE = "Award History";
    private static final int WIDTH = 550;
    private static final int HEIGHT = 370;
    private String awardNumber;
    private CoeusVector cvAwardHistory;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/CentralAdminMaintenanceServlet";
    private static final int FEED_ID = 0;
    private static final int TRANS_ID = 1;
    private static final int AWARD_NUM = 2;
    private static final int SEQ_NUM = 3;
    private static final int TYPE = 4;
    private static final int STATUS = 5;
    private static final int BATCH_FILE_NAME = 6;
    private static final int TIME_STAMP = 7;
    private static final int USER = 8;
    
    private static final String EMPTY = "";
    private AwardHistoryTableModel awardHistoryTableModel;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    /*Select a feed to reject*/
    private static final String SELECT_FEED = "feedMaintenance_exceptionCode.2001";
    
    /*Selected feed has already been rejected*/
    private static final String REJECT_FEED = "feedMaintenance_exceptionCode.2007";
    
    /*Feed marked as Error, cannot be Rejected*/
    private static final String ERROR_REJECT = "feedMaintenance_exceptionCode.2003";
    
    /*Cancelled feed cannot be Rejected*/
    private static final String CANCELL_MSG = "feedMaintenance_exceptionCode.2004";
    
    /*Select a feed to Un-Reject */
    private static final String SELECT_UNDOREJECT = "feedMaintenance_exceptionCode.2008";
    
    /*This feed is not Rejected*/
    private static final String NOT_REJECT = "feedMaintenance_exceptionCode.2006";
    
    /*Do u want to save the changes*/
    private static final String CANCEL_CONFIRMATION = "frequeny_exceptionCode.1403";
    
    private CoeusMessageResources coeusMessageResources;
    
    private boolean dataModified;
    
    private CoeusVector cvSaveAwardHistory;
    
    private boolean windowClose;
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    private static final String REQUIRED_DATE_FORMAT = "M/d/yyyy HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    
   private AwardHistoryRenderer awardHistoryRenderer;
   
   private HashMap objMap = new HashMap();
    
    
    /** Creates new form AwardHistoryForm */
    public AwardHistoryForm(String awardNum) {
        awardNumber = awardNum;
        cvSaveAwardHistory = new CoeusVector();
        coeusMessageResources = CoeusMessageResources.getInstance();
        awardHistoryTableModel = new AwardHistoryTableModel();
        cvAwardHistory = new CoeusVector();
        awardHistoryRenderer = new AwardHistoryRenderer();
        initComponents();
        postInitComponents();
        registerComponents();
        setColumnData();
        setFormData();
        display();
    }
    
    
    /*to instantiate the components*/
     private void postInitComponents() {
        mdiForm = CoeusGuiConstants.getMDIForm(); 
        dlgAwardHistory = new CoeusDlgWindow(mdiForm);
        dlgAwardHistory.setResizable(false);
        dlgAwardHistory.setModal(true);
        dlgAwardHistory.getContentPane().add(this);
        dlgAwardHistory.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardHistory.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardHistory.setSize(WIDTH, HEIGHT);
        dlgAwardHistory.setTitle(WINDOW_TITLE); 
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardHistory.getSize();
        dlgAwardHistory.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAwardHistory.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                if(!windowClose){
                    performCancelAction();
                }else{
                    dlgAwardHistory.dispose();
                }
                //performCancelAction();
                //return;
            }
        });
        
        dlgAwardHistory.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                //performCancelAction();
                if(!windowClose){
                    performCancelAction();
                }else{
                    dlgAwardHistory.dispose();
                }
                //return;
            }
        });
     }
     
     
     /*to display the form*/
     private void display(){
         setRequestFocusInThread(txtAwardNumber);
         dlgAwardHistory.show();
     }
     
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
     
     /*to register all the components in the form*/
     public void registerComponents() {
         java.awt.Component[] component = {
             txtAwardNumber,scrPnHistory,btnRefresh,btnClose,btnReject,btnRejectAll,btnUndoReject};
             ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
             setFocusTraversalPolicy(policy);
             setFocusCycleRoot(true);
             
             tblAwardHistory.setModel(awardHistoryTableModel);
             
             btnClose.addActionListener(this);
             btnRefresh.addActionListener(this);
             btnReject.addActionListener(this);
             btnRejectAll.addActionListener(this);
             btnUndoReject.addActionListener(this);
             
             //listener for header for sorting purpose
             tblAwardHistory.getTableHeader().addMouseListener(this);
             tblAwardHistory.addMouseListener(this);
     }
     
     /*to set the data to the form*/
     private void setFormData(){
         txtAwardNumber.setText(awardNumber);
         
         getAwardData(awardNumber);
         tblAwardHistory.setRowSelectionInterval(0,0);
     }
     
     private void getAwardData(String mitAwardNumber){
         String awardNum = mitAwardNumber;
         try{
             RequesterBean requester = new RequesterBean();
             requester.setFunctionType('Q');
             requester.setDataObject(awardNum);
             AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
             comm.send();
             ResponderBean response = comm.getResponse();
             if(response.isSuccessfulResponse()){
                 cvAwardHistory = (CoeusVector)response.getDataObject();
             }else {
                 throw new CoeusClientException(response.getMessage());
             }
         }catch(CoeusClientException e){
             e.printStackTrace();
         }
     }
     
     /*to set the column data,column widths*/
     private void setColumnData(){
         JTableHeader tableHeader = tblAwardHistory.getTableHeader();
         tblAwardHistory.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
         tableHeader.setReorderingAllowed(false);
         tableHeader.setFont(CoeusFontFactory.getLabelFont());
         tblAwardHistory.setRowHeight(22);
         tblAwardHistory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
         tblAwardHistory.setBackground(disabledBackground);
         
         // setting up the table columns
         TableColumn column = tblAwardHistory.getColumnModel().getColumn(0);
         column.setPreferredWidth(75);
         
         column = tblAwardHistory.getColumnModel().getColumn(1);
         column.setPreferredWidth(100);
         
         column = tblAwardHistory.getColumnModel().getColumn(2);
         column.setPreferredWidth(100);
         
         column = tblAwardHistory.getColumnModel().getColumn(3);
         column.setPreferredWidth(95);
         
         column = tblAwardHistory.getColumnModel().getColumn(4);
         column.setPreferredWidth(75);
         
         column = tblAwardHistory.getColumnModel().getColumn(5);
         column.setPreferredWidth(75);
         
         column = tblAwardHistory.getColumnModel().getColumn(6);
         column.setPreferredWidth(250);
         
         column = tblAwardHistory.getColumnModel().getColumn(7);
         column.setPreferredWidth(150);
         column.setCellRenderer(awardHistoryRenderer);
         
         column = tblAwardHistory.getColumnModel().getColumn(8);
         column.setPreferredWidth(90);
         
     }
     
     /*the table model for the award history table*/
     public class AwardHistoryTableModel extends AbstractTableModel {
         
         // represents the column names of the table
         private String colName[] = {"Feed Id","Transaction Id","Award Number","Seq Number","Type","Status","Batch File Name","Timestamp","User"};
         // represents the column class of the fields of table
         private Class colClass[] = {String.class, String.class,String.class, String.class,String.class,
         String.class, String.class,String.class, String.class};
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
             if (cvAwardHistory == null){
                 return 0;
             } else {
                 return cvAwardHistory.size();
             }
             
         }
         
         /**
          * To set the data for the model.
          * @param cvAwardBasis CoeusVector
          * @return void
          **/
         public void setData(CoeusVector cvAwardHistory) {
             cvAwardHistory = cvAwardHistory;
             fireTableDataChanged();
         }
         /**
          * To get the value from the table
          * @param rowIndex int
          * @param columnIndex int
          * @return Object
          **/
         public Object getValueAt(int rowIndex, int columnIndex) {
             SapFeedDetailsBean sapFeedDetailsBean = (SapFeedDetailsBean)cvAwardHistory.get(rowIndex);
             switch(columnIndex){
                 case FEED_ID:
                     return  new Integer(sapFeedDetailsBean.getFeedId());
                 case TRANS_ID:
                     return sapFeedDetailsBean.getTransactionId();
                 case AWARD_NUM:
                     return sapFeedDetailsBean.getMitAwardNumber();
                 case SEQ_NUM:
                     return new Integer(sapFeedDetailsBean.getSequenceNumber());
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
                     }else if(sapFeedDetailsBean.getFeedStatus().equals("P")){
                         statusValue = "Pending";
                         return statusValue;
                     }
                     return statusValue;
                 case BATCH_FILE_NAME:
                     return sapFeedDetailsBean.getBatchFileName();
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
             SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAwardHistory.get(row);
             switch(col){
                 case STATUS:
                     if (!value.toString().trim().equals(bean.getFeedStatus())) {
                         bean.setFeedStatus(value.toString());
                         dataModified = true;
                     }
                     break;
             }
             awardHistoryTableModel.fireTableDataChanged();
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
     
     /** This method is called from within the constructor to
      * initialize the form.
      * WARNING: Do NOT modify this code. The content of this method is
      * always regenerated by the Form Editor.
      */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblAwardNumber = new javax.swing.JLabel();
        txtAwardNumber = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        scrPnHistory = new javax.swing.JScrollPane();
        tblAwardHistory = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnRejectAll = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnUndoReject = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblAwardNumber.setFont(CoeusFontFactory.getLabelFont());
        lblAwardNumber.setText("Award Number:    ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        add(lblAwardNumber, gridBagConstraints);

        txtAwardNumber.setFont(CoeusFontFactory.getNormalFont());
        txtAwardNumber.setMinimumSize(new java.awt.Dimension(175, 23));
        txtAwardNumber.setPreferredSize(new java.awt.Dimension(175, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        add(txtAwardNumber, gridBagConstraints);

        btnRefresh.setFont(CoeusFontFactory.getLabelFont());
        btnRefresh.setMnemonic('f');
        btnRefresh.setText("Refresh");
        btnRefresh.setMaximumSize(new java.awt.Dimension(90, 25));
        btnRefresh.setMinimumSize(new java.awt.Dimension(90, 25));
        btnRefresh.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 25, 0, 0);
        add(btnRefresh, gridBagConstraints);

        scrPnHistory.setMinimumSize(new java.awt.Dimension(420, 280));
        scrPnHistory.setPreferredSize(new java.awt.Dimension(420, 280));
        tblAwardHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrPnHistory.setViewportView(tblAwardHistory);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(scrPnHistory, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(105, 25));
        btnClose.setMinimumSize(new java.awt.Dimension(105, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(105, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 5);
        add(btnClose, gridBagConstraints);

        btnRejectAll.setFont(CoeusFontFactory.getLabelFont());
        btnRejectAll.setMnemonic('A');
        btnRejectAll.setText("Reject All");
        btnRejectAll.setMaximumSize(new java.awt.Dimension(105, 25));
        btnRejectAll.setMinimumSize(new java.awt.Dimension(105, 25));
        btnRejectAll.setPreferredSize(new java.awt.Dimension(105, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnRejectAll, gridBagConstraints);

        btnReject.setFont(CoeusFontFactory.getLabelFont());
        btnReject.setMnemonic('R');
        btnReject.setText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(105, 25));
        btnReject.setMinimumSize(new java.awt.Dimension(105, 25));
        btnReject.setPreferredSize(new java.awt.Dimension(105, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnReject, gridBagConstraints);

        btnUndoReject.setFont(CoeusFontFactory.getLabelFont());
        btnUndoReject.setMnemonic('U');
        btnUndoReject.setText("Undo Reject");
        btnUndoReject.setMaximumSize(new java.awt.Dimension(105, 25));
        btnUndoReject.setMinimumSize(new java.awt.Dimension(105, 25));
        btnUndoReject.setPreferredSize(new java.awt.Dimension(105, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnUndoReject, gridBagConstraints);

    }//GEN-END:initComponents
    /*the actions perfomed pn the click of the different buttons in  the window*/   
    public void actionPerformed(ActionEvent actionEvent) {
        mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
        Object source = actionEvent.getSource();
        if(source.equals(btnClose)){
            if(!windowClose){
                performCancelAction();
            }else{
                dlgAwardHistory.dispose();
            }
        }else if(source.equals(btnReject)){
            performRejectAction();
        }else if(source.equals(btnUndoReject)){
            performUndoReject();
        }else if(source.equals(btnRejectAll)){
            performRejectAllAction();
        }else if(source.equals(btnRefresh)){
            performRefreshAction();
        }
        mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }
    
    /*the action performed on the click of the Close Buttton in the window*/
    private void performCancelAction(){
        try{
            if(dataModified){
                String mesg = CANCEL_CONFIRMATION;
                int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(mesg),
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    saveFormData();
                    dlgAwardHistory.dispose();
                } else if(selectedOption == CoeusOptionPane.OPTION_OK_CANCEL){
                    dlgAwardHistory.setVisible(false);
                }
            }else{
                dlgAwardHistory.setVisible(false);
            }
            
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /*to save the data to the form*/
    private void saveFormData() throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('R');
        if(cvSaveAwardHistory != null && cvSaveAwardHistory.size() > 0){
            requesterBean.setDataObject(cvSaveAwardHistory);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CONNECTION_STRING);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }
            }
        }
    }
    
    /*the action performed on the click of the RejectAll Button*/
    private void performRejectAllAction(){
        int rowCount = tblAwardHistory.getRowCount();
        int selectedOption = CoeusOptionPane.showQuestionDialog(
        "Are you sure you want to Reject all the feeds " + " " ,
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        if(selectedOption == CoeusOptionPane.SELECTION_YES) {
            for(int i = 0; i < rowCount; i++){
                SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAwardHistory.get(i);
                if(bean.getFeedStatus().equals("F")){
                    awardHistoryTableModel.setValueAt("R", i, 5);
                    bean.setFeedStatus("R");
                    bean.setAcType(TypeConstants.UPDATE_RECORD);
                    cvSaveAwardHistory.add(bean);
                    awardHistoryTableModel.fireTableDataChanged();
                    tblAwardHistory.setRowSelectionInterval(0,0);
                    windowClose = false;
                }
            }
        } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
            return;
        }
        
    }
    
    /*action performed on the click of the undoReject Buttton*/
    private void performUndoReject(){
        int selRow = tblAwardHistory.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_UNDOREJECT));
            return;
        }
        SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvAwardHistory.get(selRow);
        int feedId = sapBean.getFeedId();
        String status = sapBean.getFeedStatus();
        if(status.equals("R")){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            "Are you sure you want to un-reject the feed - " + feedId + " " ,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                awardHistoryTableModel.setValueAt("F", selRow,5);
                sapBean.setFeedStatus("F");
                sapBean.setAcType(TypeConstants.UPDATE_RECORD);
                cvSaveAwardHistory.add(sapBean);
                awardHistoryTableModel.fireTableDataChanged();
                tblAwardHistory.setRowSelectionInterval(selRow,selRow);
                windowClose = false;
            } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                return;
            }
        }else if(status.equals("F") || status.equals("C") || status.equals("E")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NOT_REJECT));
            return;
        }
    }
    
    /*the action performed on the click of the Reject Button*/
    private void performRejectAction(){
        int selRow = tblAwardHistory.getSelectedRow();
        if(selRow == -1){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_FEED));
            return;
        }
        SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvAwardHistory.get(selRow);
        int feedId = sapBean.getFeedId();
        String status = sapBean.getFeedStatus();
        if(status.equals("R")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(REJECT_FEED));
            return;
        }else if(status.equals("F")){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            "Are you sure you want to reject the feed - "+feedId + " ",
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                awardHistoryTableModel.setValueAt("R", selRow,5);
                awardHistoryTableModel.fireTableDataChanged();
                tblAwardHistory.setRowSelectionInterval(selRow,selRow);
                sapBean.setFeedStatus("R");
                sapBean.setAcType(TypeConstants.UPDATE_RECORD);
                cvSaveAwardHistory.add(sapBean);
                dataModified = true;
                windowClose = false;
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
    
    /*the action performed on the click of the Refresh Button*/
    private void performRefreshAction(){
        try{
            // Bug Fix #1882  -start step1
            dlgAwardHistory.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            // Bug Fix #1882 - End step1
            if(txtAwardNumber.getText().equals(EMPTY)){
                cvAwardHistory = null;
                tblAwardHistory.setModel(awardHistoryTableModel);
                //awardHistoryTableModel.fireTableDataChanged();
            }else {
                getAwardData(txtAwardNumber.getText());
                awardHistoryTableModel.setData(cvAwardHistory);
                tblAwardHistory.setModel(awardHistoryTableModel);
                if(cvAwardHistory != null && cvAwardHistory.size() > 0){
                    tblAwardHistory.setRowSelectionInterval(0,0);
                }
            }
            if(dataModified){
                saveFormData();
                windowClose = true;
            }
            // Bug Fix #1882 - start step2
            if(dataModified){
                getAwardData(txtAwardNumber.getText());
                windowClose = true;
            }
            
            // Bug Fix #1882 - End step2
        }catch(Exception exception){
            exception.printStackTrace();
            // Bug Fix #1882 - step3
        }finally{
            dlgAwardHistory.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            dataModified = false;
            windowClose = false;
        }
        // Bug Fix #1882 - step3
    }
    
    /*The actions performed on the click of the TAble Column Headers*/
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        int selRow = tblAwardHistory.getSelectedRow();
        SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAwardHistory.get(selRow);
        int feedId = bean.getFeedId();
        int clickCount = mouseEvent.getClickCount();
        if(source.equals(tblAwardHistory.getTableHeader())){
            mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            int row = 0;
            if(clickCount == 1){
                Point clickedPoint = mouseEvent.getPoint();
                int xPosition = (int)clickedPoint.getX();
                int columnIndex = tblAwardHistory.getColumnModel().getColumnIndexAtX(xPosition);
                switch (columnIndex) {
                    case FEED_ID:
                        if(sortCodeAsc) {
                            //Code already sorted in Ascending order. Sort now in Descending order.
                            cvAwardHistory.sort("feedId", false);
                            sortCodeAsc = false;
                        }else {
                            //Code already sorted in Descending order. Sort now in Ascending order.
                            cvAwardHistory.sort("feedId", true);
                            sortCodeAsc = true;
                        }
                        break;
                    case TRANS_ID:
                        if(sortDescAsc){
                            cvAwardHistory.sort("transactionId",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("transactionId",true);
                            sortDescAsc = true;
                        }
                        break;
                    case AWARD_NUM:
                        if(sortDescAsc){
                            cvAwardHistory.sort("mitAwardNumber",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("mitAwardNumber",true);
                            sortDescAsc = true;
                        }
                        break;
                    case SEQ_NUM:
                        if(sortDescAsc){
                            cvAwardHistory.sort("sequenceNumber",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("sequenceNumber",true);
                            sortDescAsc = true;
                        }
                        break;
                    case TYPE:
                        if(sortDescAsc){
                            cvAwardHistory.sort("feedType",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("feedType",true);
                            sortDescAsc = true;
                        }
                        break;
                    case STATUS:
                        if(sortDescAsc){
                            cvAwardHistory.sort("feedStatus",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("feedStatus",true);
                            sortDescAsc = true;
                        }
                        break;
                    case BATCH_FILE_NAME:
                        if(sortDescAsc){
                            cvAwardHistory.sort("batchFileName",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("batchFileName",true);
                            sortDescAsc = true;
                        }
                        break;
                    case TIME_STAMP:
                        if(sortDescAsc){
                            cvAwardHistory.sort("updateTimestamp",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("updateTimestamp",true);
                            sortDescAsc = true;
                        }
                        break;
                    case USER:
                        if(sortDescAsc){
                            cvAwardHistory.sort("updateUser",false);
                            sortDescAsc = false;
                        }else {
                            cvAwardHistory.sort("updateUser",true);
                            sortDescAsc = true;
                        }
                        break;
                        
                }//End Switch
                awardHistoryTableModel.fireTableDataChanged();
                for(int i = 0;i<cvAwardHistory.size();i++){
                    SapFeedDetailsBean dataBean = (SapFeedDetailsBean)cvAwardHistory.get(i);
                    if(dataBean.getFeedId() == feedId){
                        row = i;
                    }
                }
                tblAwardHistory.setRowSelectionInterval(row,row);
                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
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
    
    /*The renderer for the Award History Table*/
    class AwardHistoryRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private JLabel lblText;
        
        public AwardHistoryRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAwardHistory.get(row);
            java.awt.Color color = tblAwardHistory.getSelectionBackground();
            switch(col){
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
            }
            return lblText;
        }
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnRefresh;
    public javax.swing.JButton btnReject;
    public javax.swing.JButton btnRejectAll;
    public javax.swing.JButton btnUndoReject;
    public javax.swing.JLabel lblAwardNumber;
    public javax.swing.JScrollPane scrPnHistory;
    public javax.swing.JTable tblAwardHistory;
    public javax.swing.JTextField txtAwardNumber;
    // End of variables declaration//GEN-END:variables
    
}
