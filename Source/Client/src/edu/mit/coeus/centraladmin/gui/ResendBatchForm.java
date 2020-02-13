/*
 * ResendBatch.java
 *
 * Created on January 6, 2005, 11:01 AM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.award.bean.SapFeedDetailsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import java.awt.Color;
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
import java.util.Hashtable;
import javax.swing.AbstractAction;
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
//import sun.security.krb5.internal.crypto.e;

/**
 *
 * @author  Surekhan
 */
public class ResendBatchForm extends javax.swing.JPanel implements ActionListener , MouseListener,ListSelectionListener{
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgRescendBatch;
    private static final int WIDTH = 653;
    private static final int HEIGHT = 478;
    private static final String WINDOW_TITLE = " Resend Batch ";
    private int batchIdValue;
    private CoeusVector cvAllSapDetails;
    private SapFeedDetailsTableModel sapFeedDetailsTableModel;
    
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
    
    private static final String EMPTY = "";
	
	private CoeusMessageResources coeusMessageResources;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    private CoeusVector cvFeedDataForBatch;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/CentralAdminMaintenanceServlet";
    
    private FeedBatchTableModel feedBatchTableModel;
    
    private Hashtable htResendData;
    
    private CoeusVector cvPaths;
    
    private String developomentText;
    
    private String productionText;
    
    private String testText;
    
    private String selectedTarget;
    
    private static final String REQUIRED_DATE_FORMAT = "M/d/yyyy HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    
    private SapFeedBatchesRenderer sapFeedBatchesRenderer;
    
    private SapFeedDetailsRenderer sapFeedDetailsRenderer;
    
    private boolean selectedButton;
    
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
    private HashMap objMap = new HashMap();
    private static final String UPDATE_TIME_STAMP = "updateTimestamp";
    /** Creates new form ResendBatch */
    public ResendBatchForm(int batchId, CoeusVector cvAllSapFeedDetails) {
        htResendData = new Hashtable();
        cvPaths = new CoeusVector();
        cvAllSapDetails = new CoeusVector();
        cvFeedDataForBatch = new CoeusVector();
        cvAllSapDetails = cvAllSapFeedDetails;
		coeusMessageResources = CoeusMessageResources.getInstance();
        feedBatchTableModel = new FeedBatchTableModel();
        sapFeedDetailsTableModel = new SapFeedDetailsTableModel();
        sapFeedBatchesRenderer = new SapFeedBatchesRenderer();
        sapFeedDetailsRenderer = new SapFeedDetailsRenderer();
        batchIdValue = batchId;
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
        dlgRescendBatch = new CoeusDlgWindow(mdiForm);
        dlgRescendBatch.setResizable(false);
        dlgRescendBatch.setModal(true);
        dlgRescendBatch.getContentPane().add(this);
        dlgRescendBatch.setFont(CoeusFontFactory.getLabelFont());
        dlgRescendBatch.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRescendBatch.setSize(WIDTH, HEIGHT);
        dlgRescendBatch.setTitle(WINDOW_TITLE + batchIdValue);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRescendBatch.getSize();
        dlgRescendBatch.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgRescendBatch.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                dlgRescendBatch.dispose();
                return;
            }
        });
        
        dlgRescendBatch.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgRescendBatch.dispose();
                return;
            }
        });
    }
    
    /*to display the window*/
    private void display(){
        setRequestFocusInThread(btnClose);
        dlgRescendBatch.show();
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
    /*to set the form data*/
    private void setFormData(){
        rdBtnBatch26.setText("Resend Batch " + batchIdValue + " only ");
        rdBtnBatch26.setSelected(true);
        rdBtnProduction.setSelected(true);
        pnlTarget.setOpaque(true);
        pnlTarget.setBackground(disabledBackground);
        txtProduction.setFont(CoeusFontFactory.getLabelFont());
        txtProduction.setEditable(true);
        txtProduction.setRequestFocusEnabled(false);
        txtProduction.setBackground(Color.YELLOW);
        txtTest.setEditable(false);
        txtTest.setEnabled(false);
        txtDevelopment.setEditable(false);
        txtDevelopment.setEnabled(false);
        
        rdBtnBatch26.setBackground(disabledBackground);
        rdBtnSubsequentBatches.setBackground(disabledBackground);
        selectedTarget = "Production";
        getFeedDataForBatch();
        //Modified for Case#4579 - SAPFeed - Resend batch error  - Start
        //tblFeedBatches.setRowSelectionInterval(0,0);
        getAllSapFeedDetails(batchIdValue);
        tblFeedBatches.getSelectionModel().addListSelectionListener(this);
        int rowCount = tblFeedBatches.getRowCount();
        for(int index = 0;index<rowCount;index++){
          Integer batch =(Integer)tblFeedBatches.getModel().getValueAt(index,0);
          if(batch!=null && batch.intValue() == batchIdValue){
              tblFeedBatches.setRowSelectionInterval(index,index);
          }
        }
        int feedRowCount = tblFeedDetails.getRowCount();
        if(feedRowCount != 0){
            tblFeedDetails.setRowSelectionInterval(0,0);
        }
        //Case#4579 - End
        txtDevelopment.setText(developomentText);
        txtProduction.setText(productionText);
        txtTest.setText(testText);
        
    }
  
    /*to get the feed data for becth from the data base*/
    private void getFeedDataForBatch(){ 
        try{
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('T');
            String batchIdVal = String.valueOf(batchIdValue);
            requester.setDataObject(batchIdVal);
            
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                htResendData = (Hashtable)response.getDataObject();
                cvFeedDataForBatch = (CoeusVector)htResendData.get(SapFeedDetailsBean.class);
                //Addded for Case#4579 - SAPFeed - Resend batch error  - Start
                if(cvFeedDataForBatch != null && cvFeedDataForBatch.size() > 0){
                    cvFeedDataForBatch.sort(UPDATE_TIME_STAMP,false);
                }else{
                    cvFeedDataForBatch = new CoeusVector();
                }
                //Case#4579 - End
                cvPaths = (CoeusVector)htResendData.get("DIRECTORY_PATH");
                developomentText = cvPaths.get(0).toString();
                productionText = cvPaths.get(1).toString();
                testText = cvPaths.get(2).toString();
                
                
            }else {
                throw new CoeusClientException(response.getMessage());
            }
        }catch(CoeusClientException e){
            e.printStackTrace();
        }
        
    }
    
    /*to register the listeners and the other components*/
    private void registerComponents(){
        java.awt.Component[] component = {
            btnClose,btnSend,rdBtnBatch26 ,rdBtnSubsequentBatches, rdBtnProduction,rdBtnTest,rdBtnDevelopment};
            ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
            setFocusTraversalPolicy(policy);
            setFocusCycleRoot(true);
            tblFeedDetails.setModel(sapFeedDetailsTableModel);
            tblFeedBatches.setModel(feedBatchTableModel);
            btnClose.addActionListener(this);
            btnSend.addActionListener(this);
            rdBtnProduction.addActionListener(this);
            rdBtnTest.addActionListener(this);
            rdBtnDevelopment.addActionListener(this);
               /*rdBtnProduction.addKeyListener(this);
               rdBtnDevelopment.addKeyListener(this);
               rdBtnTest.addKeyListener(this);
               rdBtnBatch26.addKeyListener(this);
               rdBtnSubsequentBatches.addKeyListener(this);*/
            rdBtnBatch26.addActionListener(this);
            rdBtnSubsequentBatches.addActionListener(this);
            tblFeedBatches.getTableHeader().addMouseListener(this);
    }
    
    /*the table model for the Sap Feed Details Table*/
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
            if (cvAllSapDetails == null){
                return 0;
            } else {
                return cvAllSapDetails.size();
            }
            
        }
        
        
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvAllSapDetails) {
            cvAllSapDetails = cvAllSapDetails;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            SapFeedDetailsBean sapFeedDetailsBean = (SapFeedDetailsBean)cvAllSapDetails.get(rowIndex);
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
    
    /*the table model for the feed batch table*/
    public class FeedBatchTableModel extends AbstractTableModel {
        
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
            if (cvFeedDataForBatch == null){
                return 0;
            } else {
                return cvFeedDataForBatch.size();
            }
            
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvFeedDataForBatch) {
            cvFeedDataForBatch = cvFeedDataForBatch;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            SapFeedDetailsBean sapFeedDetailsBean = (SapFeedDetailsBean)cvFeedDataForBatch.get(rowIndex);
            
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
    
    /*to set the column data*/
    private void setColumnData(){
        JTableHeader tableHeader = tblFeedBatches.getTableHeader();
        JTableHeader tableHeaders = tblFeedDetails.getTableHeader();
        tblFeedBatches.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tblFeedDetails.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeaders.setReorderingAllowed(false);
        tableHeaders.setFont(CoeusFontFactory.getLabelFont());
        tblFeedBatches.setRowHeight(22);
        tblFeedDetails.setRowHeight(22);
        tblFeedBatches.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFeedDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFeedDetails.setBackground(disabledBackground);
        tblFeedBatches.setBackground(disabledBackground);
        
        
        // setting up the table columns
        TableColumn column = tblFeedBatches.getColumnModel().getColumn(0);
        column.setPreferredWidth(75);
        
        column = tblFeedBatches.getColumnModel().getColumn(1);
        column.setPreferredWidth(200);
        
        column = tblFeedBatches.getColumnModel().getColumn(2);
        column.setPreferredWidth(180);
        
        column = tblFeedBatches.getColumnModel().getColumn(3);
        column.setPreferredWidth(180);
        column.setCellRenderer(sapFeedBatchesRenderer);
        
        column = tblFeedBatches.getColumnModel().getColumn(4);
        column.setPreferredWidth(200);
        
        // setting up the table columns
        TableColumn columnvalue = tblFeedDetails.getColumnModel().getColumn(0);
        columnvalue.setPreferredWidth(75);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(1);
        columnvalue.setPreferredWidth(110);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(2);
        columnvalue.setPreferredWidth(110);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(3);
        columnvalue.setPreferredWidth(110);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(4);
        columnvalue.setPreferredWidth(80);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(5);
        columnvalue.setPreferredWidth(80);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(6);
        columnvalue.setPreferredWidth(180);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        columnvalue = tblFeedDetails.getColumnModel().getColumn(7);
        columnvalue.setPreferredWidth(190);
        columnvalue.setCellRenderer(sapFeedDetailsRenderer);
        
        
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnGrpPaths = new javax.swing.ButtonGroup();
        btnGrpHeaders = new javax.swing.ButtonGroup();
        pnlTarget = new javax.swing.JPanel();
        rdBtnProduction = new javax.swing.JRadioButton();
        rdBtnTest = new javax.swing.JRadioButton();
        rdBtnDevelopment = new javax.swing.JRadioButton();
        txtProduction = new edu.mit.coeus.utils.CoeusTextField();
        txtTest = new edu.mit.coeus.utils.CoeusTextField();
        txtDevelopment = new edu.mit.coeus.utils.CoeusTextField();
        scrPnFeedBatches = new javax.swing.JScrollPane();
        tblFeedBatches = new javax.swing.JTable();
        scrPnFeedDetails = new javax.swing.JScrollPane();
        tblFeedDetails = new javax.swing.JTable();
        rdBtnBatch26 = new javax.swing.JRadioButton();
        rdBtnSubsequentBatches = new javax.swing.JRadioButton();
        btnClose = new javax.swing.JButton();
        btnSend = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(575, 318));
        setPreferredSize(new java.awt.Dimension(653, 478));
        pnlTarget.setLayout(new java.awt.GridBagLayout());

        pnlTarget.setBackground(new java.awt.Color(230, 227, 223));
        pnlTarget.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Target", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont(), new java.awt.Color(255, 51, 51)));
        pnlTarget.setMinimumSize(new java.awt.Dimension(550, 96));
        btnGrpPaths.add(rdBtnProduction);
        rdBtnProduction.setFont(CoeusFontFactory.getLabelFont());
        rdBtnProduction.setText("Production        ");
        rdBtnProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdBtnProductionActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlTarget.add(rdBtnProduction, gridBagConstraints);

        btnGrpPaths.add(rdBtnTest);
        rdBtnTest.setFont(CoeusFontFactory.getLabelFont());
        rdBtnTest.setText("Test        ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlTarget.add(rdBtnTest, gridBagConstraints);

        btnGrpPaths.add(rdBtnDevelopment);
        rdBtnDevelopment.setFont(CoeusFontFactory.getLabelFont());
        rdBtnDevelopment.setText("Development     ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlTarget.add(rdBtnDevelopment, gridBagConstraints);

        txtProduction.setMinimumSize(new java.awt.Dimension(385, 22));
        txtProduction.setPreferredSize(new java.awt.Dimension(420, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTarget.add(txtProduction, gridBagConstraints);

        txtTest.setMinimumSize(new java.awt.Dimension(385, 22));
        txtTest.setPreferredSize(new java.awt.Dimension(420, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlTarget.add(txtTest, gridBagConstraints);

        txtDevelopment.setMinimumSize(new java.awt.Dimension(385, 22));
        txtDevelopment.setPreferredSize(new java.awt.Dimension(420, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        pnlTarget.add(txtDevelopment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(pnlTarget, gridBagConstraints);

        scrPnFeedBatches.setMinimumSize(new java.awt.Dimension(550, 110));
        tblFeedBatches.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnFeedBatches.setViewportView(tblFeedBatches);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(scrPnFeedBatches, gridBagConstraints);

        scrPnFeedDetails.setMinimumSize(new java.awt.Dimension(550, 140));
        tblFeedDetails.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnFeedDetails.setViewportView(tblFeedDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 3, 0);
        add(scrPnFeedDetails, gridBagConstraints);

        rdBtnBatch26.setBackground(new java.awt.Color(226, 226, 223));
        btnGrpHeaders.add(rdBtnBatch26);
        rdBtnBatch26.setFont(CoeusFontFactory.getLabelFont());
        rdBtnBatch26.setText("Resend  batch 26 only");
        rdBtnBatch26.setMinimumSize(new java.awt.Dimension(200, 23));
        rdBtnBatch26.setPreferredSize(new java.awt.Dimension(300, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(rdBtnBatch26, gridBagConstraints);

        rdBtnSubsequentBatches.setBackground(new java.awt.Color(230, 226, 220));
        btnGrpHeaders.add(rdBtnSubsequentBatches);
        rdBtnSubsequentBatches.setFont(CoeusFontFactory.getLabelFont());
        rdBtnSubsequentBatches.setText("Resend the selected batch and all subsequent batches");
        rdBtnSubsequentBatches.setMinimumSize(new java.awt.Dimension(350, 23));
        rdBtnSubsequentBatches.setPreferredSize(new java.awt.Dimension(350, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(rdBtnSubsequentBatches, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(75, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(75, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(75, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 0, 0);
        add(btnClose, gridBagConstraints);

        btnSend.setFont(CoeusFontFactory.getLabelFont());
        btnSend.setMnemonic('S');
        btnSend.setText("Send");
        btnSend.setMaximumSize(new java.awt.Dimension(75, 23));
        btnSend.setMinimumSize(new java.awt.Dimension(75, 23));
        btnSend.setPreferredSize(new java.awt.Dimension(75, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(btnSend, gridBagConstraints);

        lblStatus.setFont(CoeusFontFactory.getLabelFont());
        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setMinimumSize(new java.awt.Dimension(452, 23));
        lblStatus.setPreferredSize(new java.awt.Dimension(452, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        add(lblStatus, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    private void rdBtnSubsequentBatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdBtnSubsequentBatchesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdBtnSubsequentBatchesActionPerformed
    
    private void rdBtnProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdBtnProductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdBtnProductionActionPerformed
    
    /*the actions peformed on the click of the various buttons*/
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if(source.equals(rdBtnProduction)) {
                selectedTarget = "Production";
                txtProduction.setEditable(true);
                txtProduction.setRequestFocusEnabled(false);
                txtProduction.setBackground(Color.yellow);
                txtDevelopment.setEditable(false);
                txtTest.setEditable(false);
                txtProduction.setFont(CoeusFontFactory.getLabelFont());
                txtDevelopment.setFont(CoeusFontFactory.getNormalFont());
                txtTest.setFont(CoeusFontFactory.getNormalFont());
                selectedButton = false;
            }else if(source.equals(rdBtnTest)) {
                selectedTarget = "Test";
                txtTest.setEditable(true);
                txtTest.setRequestFocusEnabled(false);
                txtTest.setBackground(Color.yellow);
                txtProduction.setEditable(false);
                txtDevelopment.setEditable(false);
                txtTest.setFont(CoeusFontFactory.getLabelFont());
                txtDevelopment.setFont(CoeusFontFactory.getNormalFont());
                txtProduction.setFont(CoeusFontFactory.getNormalFont());
                selectedButton = false;
            }else if(source.equals(rdBtnDevelopment)) {
                selectedTarget = "Development";
                txtDevelopment.setEditable(true);
                txtDevelopment.setRequestFocusEnabled(false);
                txtDevelopment.setBackground(Color.yellow);
                txtTest.setEditable(false);
                txtProduction.setEditable(false);
                txtDevelopment.setFont(CoeusFontFactory.getLabelFont());
                txtProduction.setFont(CoeusFontFactory.getNormalFont());
                txtTest.setFont(CoeusFontFactory.getNormalFont());
                selectedButton = false;
            }else if(source.equals(rdBtnBatch26)){
                selectedTarget="Resend Batch " + batchIdValue + " only ";
                selectedButton = true;
            }else if(source.equals(rdBtnSubsequentBatches)){
                selectedTarget="Resend the selected batch and all subsequent batches";
                selectedButton = true;
            }else if(source.equals(btnClose)){
                dlgRescendBatch.dispose();
            }else if(source.equals(btnSend)){
                try{
                    performSendAction();
                }catch(Exception e){
					e.printStackTrace();
                }
            }
        }finally{
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*the action peormed on the click of the Send Button*/
    private void performSendAction() throws CoeusClientException {
		try {
			boolean status = false;
			String selectedText = "";
			String rdBtnText = null;
			CoeusVector cvStatus = new CoeusVector();
			boolean isErrorOccured = false;
			if(rdBtnDevelopment.isSelected()){
				selectedText = txtDevelopment.getText();
				rdBtnText = rdBtnDevelopment.getText().trim();
			}else if(rdBtnProduction.isSelected()){
				selectedText = txtProduction.getText();
				rdBtnText = rdBtnProduction.getText().trim();
			}else if(rdBtnTest.isSelected()){
				selectedText = txtTest.getText();
				rdBtnText = rdBtnTest.getText().trim();
			}
			if(selectedText == null || selectedText.equals("")){
				CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
				return;
			}
			if(rdBtnBatch26.isSelected()){
				int selRow = tblFeedBatches.getSelectedRow();
				SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedDataForBatch.get(selRow);
				int batchId = bean.getBatchId();
				CoeusVector cvData = new CoeusVector();
				cvData.add(0,new Integer(batchId));
				cvData.add(1,rdBtnText);
				RequesterBean requester = new RequesterBean();
				requester.setFunctionType('V');
				requester.setDataObject(cvData);
				AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
				comm.send();

				ResponderBean response = comm.getResponse();
				if(response.isSuccessfulResponse()){
					cvStatus = (CoeusVector)response.getDataObject();
					if (cvStatus != null && cvStatus.size() > 0) {
						status = ((Boolean)cvStatus.get(0)).booleanValue();
						isErrorOccured = ((Boolean)cvStatus.get(1)).booleanValue();
						if(status){
							lblStatus.setText("Resending Batch - " +batchId );
							lblStatus.setFont(CoeusFontFactory.getLabelFont());
							lblStatus.setAlignmentY(JLabel.RIGHT_ALIGNMENT);
						} else if (isErrorOccured) {
							CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
						} else {
							throw new CoeusClientException(response.getMessage());
						}
					}
				} else {
					 if (response.getMessage() == null) {
						 CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
					 }
					 //throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
				 }
			} else {
				int rowCount = tblFeedBatches.getRowCount();
				for(int i = 0 ; i < rowCount ; i++){
					SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedDataForBatch.get(i);
					int batchId = bean.getBatchId();
					CoeusVector cvData = new CoeusVector();
					cvData.add(0,new Integer(batchId));
					cvData.add(1,rdBtnText);
					RequesterBean requester = new RequesterBean();
					requester.setFunctionType('V');
					requester.setDataObject(cvData);
					AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
					comm.send();
					ResponderBean response = comm.getResponse();
					if(response.isSuccessfulResponse()){
						cvStatus = (CoeusVector)response.getDataObject();
						if (cvStatus != null && cvStatus.size() > 0) {
							status = ((Boolean)cvStatus.get(0)).booleanValue();
							isErrorOccured = ((Boolean)cvStatus.get(1)).booleanValue();
							if(status) {
								lblStatus.setText("Resending Batch - " +batchId );
								lblStatus.setFont(CoeusFontFactory.getLabelFont());
								lblStatus.setAlignmentY(JLabel.RIGHT_ALIGNMENT);
							} else if (isErrorOccured) {
								CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
							} else {
								throw new CoeusClientException(response.getMessage());
							}
						}
					}else {
					 if (response.getMessage() == null) {
						 CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey("generateSponsorFeedExceptionCode.2051"));
					 }
					 //throw new CoeusClientException(response.getMessage(),CoeusClientException.ERROR_MESSAGE);
				 }
				}
			}
		} catch(CoeusClientException coeusClientException) {
			CoeusOptionPane.showDialog(coeusClientException);
			dlgRescendBatch.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		} 
    }
    
    /*the action performed on the click of the Table Column Header*/
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        int clickCount = mouseEvent.getClickCount();
        if(source.equals(tblFeedBatches.getTableHeader())){
            int row = 0;
            if(clickCount == 1){
                Point clickedPoint = mouseEvent.getPoint();
                int xPosition = (int)clickedPoint.getX();
                int columnIndex = tblFeedBatches.getColumnModel().getColumnIndexAtX(xPosition);
                switch (columnIndex) {
                    case BATCH_ID:
                        if(sortCodeAsc) {
                            //Code already sorted in Ascending order. Sort now in Descending order.
                            cvFeedDataForBatch.sort("batchId", false);
                            sortCodeAsc = false;
                        }else {
                            //Code already sorted in Descending order. Sort now in Ascending order.
                            cvFeedDataForBatch.sort("batchId", true);
                            sortCodeAsc = true;
                        }
                        break;
                    case BATCH_FILE_NAME:
                        if(sortDescAsc){
                            cvFeedDataForBatch.sort("batchFileName",false);
                            sortDescAsc = false;
                        }else {
                            cvFeedDataForBatch.sort("batchFileName",true);
                            sortDescAsc = true;
                        }
                        break;
                    case NO_OF_RECORDS:
                        if(sortDescAsc){
                            cvFeedDataForBatch.sort("noOfRecords",false);
                            sortDescAsc = false;
                        }else {
                            cvFeedDataForBatch.sort("noOfRecords",true);
                            sortDescAsc = true;
                        }
                        break;
                    case TIME_STAMP:
                        if(sortDescAsc){
                            cvFeedDataForBatch.sort("updateTimestamp",false);
                            sortDescAsc = false;
                        }else {
                            cvFeedDataForBatch.sort("updateTimestamp",true);
                            sortDescAsc = true;
                        }
                        break;
                    case USER:
                        if(sortDescAsc){
                            cvFeedDataForBatch.sort("updateUser",false);
                            sortDescAsc = false;
                        }else {
                            cvFeedDataForBatch.sort("updateUser",true);
                            sortDescAsc = true;
                        }
                        break;
                        
                }//End Switch
                feedBatchTableModel.fireTableDataChanged();
                for(int i = 0;i<cvFeedDataForBatch.size();i++){
                    SapFeedDetailsBean dataBean = (SapFeedDetailsBean)cvFeedDataForBatch.get(i);
                    if(dataBean.getBatchId() == batchId){
                        row = i;
                    }
                }
                tblFeedBatches.setRowSelectionInterval(row,row);
                
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
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.ButtonGroup btnGrpHeaders;
    private javax.swing.ButtonGroup btnGrpPaths;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel pnlTarget;
    private javax.swing.JRadioButton rdBtnBatch26;
    private javax.swing.JRadioButton rdBtnDevelopment;
    private javax.swing.JRadioButton rdBtnProduction;
    private javax.swing.JRadioButton rdBtnSubsequentBatches;
    private javax.swing.JRadioButton rdBtnTest;
    private javax.swing.JScrollPane scrPnFeedBatches;
    private javax.swing.JScrollPane scrPnFeedDetails;
    private javax.swing.JTable tblFeedBatches;
    private javax.swing.JTable tblFeedDetails;
    private edu.mit.coeus.utils.CoeusTextField txtDevelopment;
    private edu.mit.coeus.utils.CoeusTextField txtProduction;
    private edu.mit.coeus.utils.CoeusTextField txtTest;
    // End of variables declaration//GEN-END:variables
    /*The renderer for the sap feed baches stable*/
    class SapFeedBatchesRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private JLabel lblText;
        
        public SapFeedBatchesRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedDataForBatch.get(row);
            java.awt.Color color = tblFeedBatches.getSelectionBackground();
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
    
    /* renderer for the FEED DETAILS TABLE table*/
    class SapFeedDetailsRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private JLabel lblText;
        
        public SapFeedDetailsRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            
            String error = "E";
            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvAllSapDetails.get(row);
            java.awt.Color color = tblFeedDetails.getSelectionBackground();
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
    //Addded for Case#4579 - SAPFeed - Resend batch error - Start
    /**
     * Called whenever the value of the batch row is selected to display
     * corresponding Feed data.
     */
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        Object source = listSelectionEvent.getSource();
        mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try{
            int selRow = tblFeedBatches.getSelectedRow();
            if( (source.equals(tblFeedBatches.getSelectionModel()) )&& (selRow >= 0 ) &&
                    cvFeedDataForBatch !=null && cvFeedDataForBatch.size() > 0) {
                SapFeedDetailsBean bean = (SapFeedDetailsBean)cvFeedDataForBatch.get(selRow);
                int batchId = bean.getBatchId();
                getAllSapFeedDetails(batchId);
                sapFeedDetailsTableModel.setData(cvAllSapDetails);
                tblFeedDetails.setModel(sapFeedDetailsTableModel);
                if(tblFeedDetails.getRowCount() > 0){
                    tblFeedDetails.setRowSelectionInterval(0,0);
                }
            }
        }finally{
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*
     * Method to get the sap feed details from the data base
     * @param - batchId - contains batch id
     */
    private void getAllSapFeedDetails(int batchId){
        try{
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('K');
            requester.setDataObject(new Integer(batchId));
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                cvAllSapDetails = (CoeusVector)response.getDataObject();
                if(cvAllSapDetails != null){
                    cvAllSapDetails.sort(UPDATE_TIME_STAMP,false);
                }else{
                    cvAllSapDetails = new CoeusVector();
                }
            }else {
                throw new CoeusClientException(response.getMessage());
            }
        }catch(CoeusClientException e){
            e.printStackTrace();
        }
    }
    //Case#4579 - End
}
