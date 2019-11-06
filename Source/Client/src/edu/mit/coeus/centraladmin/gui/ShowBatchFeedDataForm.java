/*
 * ShowBatchFeedDataForm.java
 *
 * Created on January 4, 2005, 10:34 AM
 */

package edu.mit.coeus.centraladmin.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.centraladmin.bean.FeedBatchListBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.menu.CoeusFileMenu;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TableSorter;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;



/**
 *
 * @author  surekhan
 */
public class ShowBatchFeedDataForm extends CoeusInternalFrame  implements ActionListener{
    
    public JTable tblFeedData;
    private JScrollPane scrPnFeedData;
    public CoeusToolBarButton btnClose;
    private CoeusAppletMDIForm mdiForm;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    private CoeusVector cvFeedData;
    private static final String EMPTY = "";
    private ShowFeedDataTableModel showFeedDataTableModel;
    
    private int batchValue;
    
    private boolean display; 
    
    private static final int FEED_ID = 0;
    private static final int SAP_TRANS = 1;
    private static final int PROJECT_TYPE = 2;
    private static final int WBS_TYPE = 3;
    private static final int ACCNT_LEVEL = 4;
    private static final int MIT_SAP_ACCOUNT = 5;
    private static final int DEPT_NO = 6;
    private static final int BILLING_ELEMENT = 7;
    private static final int BILLING_TYPE = 8;
    private static final int BILLING_FORM = 9;
    private static final int SPON_CODE = 10;
    private static final int PRIMARY_SPONSOR = 11;
    private static final int CONTRACT = 12;
    private static final int CUSTOMER = 13;
    private static final int TERM_CODE = 14;
    private static final int PARENT_ACCOUNT = 15;
    private static final int ACCT_NAME = 16;
    private static final int EFFCT_DATE = 17;
    private static final int EXP = 18;
    private static final int SUB_PLAN = 19;
    private static final int MAIL_CODE = 20;
    private static final int SUPERVISOR = 21;
    private static final int SUPER_ROOM = 22;
    private static final int ADDRESSEE = 23;
    private static final int ADDR_ROOM = 24;
    private static final int AGREE_TYPE = 25;
    private static final int AUTH_TOTAL = 26;
    private static final int COST_SHARE = 27;
    private static final int FUND_CLASS = 28;
    private static final int POOL_CODE = 29;
    private static final int PENDING_CODE = 30;
    private static final int FS_CODE = 31;
    private static final int DFAFS = 32;
    private static final int CALC_CODE = 33;
    private static final int CFDA_NO = 34;
    private static final int COST_SHEETING_KEY = 35;
    private static final int LAB_ALLCN_KEY = 36;
    private static final int EB_ADJ_KEY = 37;
    private static final int OH_ADJ_KEY = 38;
    private static final int COMM_1 = 39;
    private static final int COMM_2 = 40;
    private static final int COMM_3 = 41;
    private static final int TITLE = 42;
    private static final int SORT_ID = 43;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/CentralAdminMaintenanceServlet";
    
     
    
    
    /** Creates a new instance of ShowBatchFeedDataForm */
    public ShowBatchFeedDataForm(String title , CoeusAppletMDIForm mdiForm , int batchId) {
        super(title,mdiForm);
        this.mdiForm = mdiForm;
        batchValue = batchId;
        showFeedDataTableModel = new ShowFeedDataTableModel();
        cvFeedData = new CoeusVector();
        initComponents();
        registerComponents();
        setColumnData();
        setFormData();
        
    }
    
    /*to register the listeners*/
    private void registerComponents(){
        tblFeedData.setModel(showFeedDataTableModel);
        btnClose.addActionListener(this);
    }
    
    /*to instantiate the components*/
    public void initComponents() {
        
        setFrameToolBar(getFeedDataToolBar());
        tblFeedData = new javax.swing.JTable();
        tblFeedData.setBackground(disabledBackground);
        scrPnFeedData = new JScrollPane(tblFeedData);
        
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        JPanel pnlFeedData = new JPanel();
        pnlFeedData.setLayout(new java.awt.GridBagLayout());
        scrPnFeedData.setBorder(new javax.swing.border.EtchedBorder());
        scrPnFeedData.setMinimumSize(new java.awt.Dimension(1000, 800));
        scrPnFeedData.setPreferredSize(new java.awt.Dimension(1000, 800));
        tblFeedData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnFeedData.setBackground(bgColor);
        pnlFeedData.setBackground(bgColor);
        scrPnFeedData.setOpaque(true);
        scrPnFeedData.setViewportView(tblFeedData);
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlFeedData.add(scrPnFeedData,gridBagConstraints);
        getContentPane().add(pnlFeedData);
        
        //scrPnFeedData.getViewport().setBackground(disabledBackground);
        //getContentPane().add(scrPnFeedData);
        
    }
    
    /*to add to the tool bar*/
    private JToolBar getFeedDataToolBar(){
        JToolBar feedDataToolBar = new JToolBar();
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null, "Close");
        
        feedDataToolBar.add(btnClose);
        
        return feedDataToolBar;
    }
    
    /*to display the form*/
    public boolean display() {
        if(cvFeedData == null){
            return false;
        }else{
            return true;
        }
    }
    
    /*to set the data to the form*/
    private void setFormData(){
         try{
              RequesterBean requester = new RequesterBean();
              requester.setFunctionType('U');
              String batchId = String.valueOf(batchValue);
              requester.setDataObject(batchId);
              AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
              comm.send();
              ResponderBean response = comm.getResponse();
              if(response.isSuccessfulResponse()){
                  cvFeedData = (CoeusVector)response.getDataObject();
                  
             }else {
                  throw new CoeusClientException(response.getMessage());
             }
          }catch(CoeusClientException e){
              e.printStackTrace();
          }
    }
    
    /*to set the column data*/
    private void setColumnData(){
        JTableHeader tableHeader = tblFeedData.getTableHeader();
        tblFeedData.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblFeedData.setRowHeight(22);
        tblFeedData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFeedData.setRowSelectionAllowed(false);
        
        
        TableSorter sorter = new TableSorter((AbstractTableModel)tblFeedData.getModel(),false);
        tblFeedData.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable(tblFeedData);
        
        TableColumn column = tblFeedData.getColumnModel().getColumn(0);
        column.setPreferredWidth(75);
        
        column = tblFeedData.getColumnModel().getColumn(1);
        column.setPreferredWidth(130);
        column = tblFeedData.getColumnModel().getColumn(2);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(3);
        column.setPreferredWidth(80);
        column = tblFeedData.getColumnModel().getColumn(4);
        column.setPreferredWidth(100);
        column = tblFeedData.getColumnModel().getColumn(5);
        column.setPreferredWidth(125);
        column = tblFeedData.getColumnModel().getColumn(6);
        column.setPreferredWidth(75);
        column = tblFeedData.getColumnModel().getColumn(7);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(8);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(9);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(10);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(11);
        column.setPreferredWidth(120);
        column = tblFeedData.getColumnModel().getColumn(12);
        column.setPreferredWidth(190);
        column = tblFeedData.getColumnModel().getColumn(13);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(14);
        column.setPreferredWidth(80);
        column = tblFeedData.getColumnModel().getColumn(15);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(16);
        column.setPreferredWidth(350);
        column = tblFeedData.getColumnModel().getColumn(17);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(18);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(19);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(20);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(21);
        column.setPreferredWidth(190);
        column = tblFeedData.getColumnModel().getColumn(22);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(23);
        column.setPreferredWidth(150);
        column = tblFeedData.getColumnModel().getColumn(24);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(25);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(26);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(27);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(28);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(29);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(30);
        column.setPreferredWidth(110);
        column = tblFeedData.getColumnModel().getColumn(31);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(32);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(33);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(34);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(35);
        column.setPreferredWidth(150);
        column = tblFeedData.getColumnModel().getColumn(36);
        column.setPreferredWidth(130);
        column = tblFeedData.getColumnModel().getColumn(37);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(38);
        column.setPreferredWidth(90);
        column = tblFeedData.getColumnModel().getColumn(39);
        column.setPreferredWidth(350);
        column = tblFeedData.getColumnModel().getColumn(40);
        column.setPreferredWidth(350);
        column = tblFeedData.getColumnModel().getColumn(41);
        column.setPreferredWidth(350);
        column = tblFeedData.getColumnModel().getColumn(42);
        column.setPreferredWidth(800);
        column = tblFeedData.getColumnModel().getColumn(43);
        column.setPreferredWidth(90);
     
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    public void saveActiveSheet() {
    }
    
    public void saveAsActiveSheet() {
    }
    
    
    
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameActivated(internalFrameEvent);
    }
    
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
        super.internalFrameDeactivated(internalFrameEvent);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnClose)){
            doDefaultCloseAction();
        }
    }    
    
    /*Table model for feed data table*/
    public class ShowFeedDataTableModel extends AbstractTableModel {
        //Coeus 4.3 enhancement - Cfda no. display change - start
        // Cfda changed to CFDA
        // represents the column names of the table
        private String colName[] = {"Feed Id","Sap Transaction","Project Type","Wbs Type","Account Level" , "Mit Sap Account" ,"Deptno",
        "Billing Element" , "Billing Type" , "Billing Form","Spon Code" , "Primary Sponsor" ,"Contract","Customer","Term Code" ,"Parent Account",
        "Acctname","Effect Date" ,"Expiration","Sub Plan" ,"Mail Code" , "Supervisor" ,"Super Room" ,"Addressee","Addr Room" ,"Agree Type" ,"Auth Total",
        "Cost Share","Fund Class","Pool Code","Pending code" ,"Fs Code","Dfafs","Calc Code","CFDA No.","Costing Sheet Key" , "Lab Allocation Key" ,"Eb Adj. Key",
        "Oh Adj. Key", "Comment1" ,"Comment2","Comment3","Title","Sort Id"};
        //Coeus 4.3 enhancement - Cfda no. display change - end
        
        // represents the column class of the fields of table
        private Class colClass[] = {Integer.class, String.class,String.class, String.class,String.class,String.class, String.class,String.class, String.class,String.class,
        String.class, String.class,String.class, String.class,String.class,String.class, String.class,String.class, String.class,String.class,
        String.class, String.class,String.class, String.class,String.class,String.class, String.class,String.class, String.class,String.class,
        String.class, String.class,String.class, String.class,String.class,String.class, String.class,String.class, String.class,String.class,
        String.class, String.class,String.class, Integer.class};
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
            if (cvFeedData == null){
                return 0;
            } else {
                return cvFeedData.size();
            }
            
        }
        
        /**
         * To set the data for the model.
         * @param cvAwardBasis CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvFeedData) {
            cvFeedData = cvFeedData;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            FeedBatchListBean bean = (FeedBatchListBean)cvFeedData.get(rowIndex);
            switch(columnIndex){
                case FEED_ID:
                    return new Integer(bean.getFeedId());
                case SAP_TRANS:
                    return bean.getSapTransaction();
                case PROJECT_TYPE:
                    return bean.getProjectType();
                case WBS_TYPE:
                    return bean.getWbsType();
                case ACCNT_LEVEL:
                    return bean.getAccountLevel();
                case MIT_SAP_ACCOUNT:
                    return bean.getMitSapAccount();
                case DEPT_NO:
                    return bean.getDeptNo();
                case BILLING_ELEMENT:
                    return bean.getBillingElement();
                case BILLING_TYPE:
                    return bean.getBillingType();
                case BILLING_FORM:
                    return bean.getBillingForm();
                case SPON_CODE:
                    return bean.getSponsorCode();
                case PRIMARY_SPONSOR:
                    return bean.getPrimarySponsor();
                case CONTRACT:
                    return bean.getContract();
                case CUSTOMER:
                    return bean.getCustomer();
                case TERM_CODE:
                    return bean.getTermCode();
                case PARENT_ACCOUNT:
                    return bean.getParentAccount();
                case ACCT_NAME:
                    return bean.getAcctName();
                case EFFCT_DATE:
                    return bean.getEffectDate();
                case EXP:
                    return bean.getExpiration();
                case SUB_PLAN:
                    return bean.getSubPlan();
                case MAIL_CODE:
                    return bean.getMailCode();
                case SUPERVISOR:
                    return bean.getSupervisor();
                case SUPER_ROOM:
                    return bean.getSuperRoom();
                case ADDRESSEE:
                    return bean.getAddressee();
                case ADDR_ROOM:
                    return bean.getAddrRoom();
                case AGREE_TYPE:
                    return bean.getAgreeType();
                case AUTH_TOTAL:
                    return bean.getAuthTotal();
                case COST_SHARE:
                    return bean.getCostShare();
                case FUND_CLASS:
                    return bean.getFundClass();
                case POOL_CODE:
                    return bean.getPoolCode();
                case PENDING_CODE:
                    return bean.getPendingCode();
                case FS_CODE:
                    return bean.getFsCode();
                case DFAFS:
                    return bean.getDfafs();
                case CALC_CODE:
                    return bean.getCalcCode();
                case CFDA_NO:
                    return bean.getCfdaNum();
                case COST_SHEETING_KEY:
                    return bean.getCostingSheetKey();
                case LAB_ALLCN_KEY:
                    return bean.getLabAllocationKey();
                case EB_ADJ_KEY:
                    return bean.getEbAdjustmentKey();
                case OH_ADJ_KEY:
                    return bean.getOhAdjustmentkey();
                case COMM_1:
                    return bean.getComment1();
                case COMM_2:
                    return bean.getComment2();
                case COMM_3:
                    return bean.getComment3();
                case TITLE:
                    return bean.getTitle();
                case SORT_ID:
                    return new Integer(bean.getSortId());
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
    
    
}