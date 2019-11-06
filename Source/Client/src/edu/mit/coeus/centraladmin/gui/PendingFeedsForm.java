/*
 * PendingFeedsForm.java
 *
 * Created on January 6, 2005, 10:51 AM
 */
/* PMD check performed, and commented unused imports and variables on 27-OCT-2010
 * by Keerthy Jayaraj
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
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  surekhan
 */
public class PendingFeedsForm extends javax.swing.JPanel implements ActionListener , MouseListener{
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgPendingFeeds;
    // Modified for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
    // Column widths altered to fit window 
    private static final int WIDTH = 950;
    private static final int HEIGHT = 500;
    // COEUSDEV-563: End
    private static final String WINDOW_TITLE = "Pending Feeds";
    private CoeusVector cvPendingFeeds;
    
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
    "/CentralAdminMaintenanceServlet";
    private static final int FEED_ID = 0;
    private static final int TRANS_ID = 1;
    private static final int AWARD_NUM = 2;
    private static final int SEQ_NUM = 3;
    private static final int TYPE = 4;
    private static final int STATUS = 5;
    private static final int BATCH_ID = 6;
    private static final int TIME_STAMP = 7;
    private static final int USER = 8;
    
    private static final String EMPTY = "";
    
    private PendingFeedsTableModel pendingFeedsTableModel;
    
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    
    private static final String SELECT_FEED = " Select a feed to cancel ";
    
    private CoeusVector cvSavePendingFeeds;
    
    private static final String REQUIRED_DATE_FORMAT = "M/d/yyyy HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    
    private PendingFeedsRenderer pendingFeedsRenderer;
    
    private CoeusVector cvData;
    
    //variables for sorting purpose. will be true if last sort was Ascending
    //else will be false.
    private boolean sortCodeAsc = true;
    private boolean sortDescAsc = false;
    
//    private HashMap objMap = new HashMap();
    
    /** Creates new form PendingFeedsForm */
    public PendingFeedsForm() {
        pendingFeedsTableModel = new PendingFeedsTableModel();
        cvData = new CoeusVector();
        cvPendingFeeds = new CoeusVector();
        cvSavePendingFeeds = new CoeusVector();
        pendingFeedsRenderer = new PendingFeedsRenderer();
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
        dlgPendingFeeds = new CoeusDlgWindow(mdiForm);
        dlgPendingFeeds.setResizable(false);
        dlgPendingFeeds.setModal(true);
        dlgPendingFeeds.getContentPane().add(this);
        dlgPendingFeeds.setFont(CoeusFontFactory.getLabelFont());
        dlgPendingFeeds.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgPendingFeeds.setSize(WIDTH, HEIGHT);
        dlgPendingFeeds.setTitle(WINDOW_TITLE); 
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgPendingFeeds.getSize();
        dlgPendingFeeds.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgPendingFeeds.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                dlgPendingFeeds.dispose();
                return;
            }
        });
        
        dlgPendingFeeds.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgPendingFeeds.dispose();
                return;
            }
        });
     }
     
     /*to set the column data*/
      private void setColumnData(){
          JTableHeader tableHeader = tblPendingFeeds.getTableHeader();
          tblPendingFeeds.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
          tableHeader.setReorderingAllowed(false);
          tableHeader.setFont(CoeusFontFactory.getLabelFont());
          tblPendingFeeds.setRowHeight(22);
          tblPendingFeeds.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
          tblPendingFeeds.setBackground(disabledBackground);
          
          // setting up the table columns
          // Modified for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
          // Column widths altered to fit window 
          TableColumn column = tblPendingFeeds.getColumnModel().getColumn(0);
          column.setPreferredWidth(66 );
          
          column = tblPendingFeeds.getColumnModel().getColumn(1);
          column.setPreferredWidth(100);
          
          column = tblPendingFeeds.getColumnModel().getColumn(2);
          column.setPreferredWidth(100);
          
          column = tblPendingFeeds.getColumnModel().getColumn(3);
          column.setPreferredWidth(90);
          
          column = tblPendingFeeds.getColumnModel().getColumn(4);
          column.setPreferredWidth(65);
          
          column = tblPendingFeeds.getColumnModel().getColumn(5);
          column.setPreferredWidth(65);
          
          column = tblPendingFeeds.getColumnModel().getColumn(6);
          column.setPreferredWidth(90);
          
          column = tblPendingFeeds.getColumnModel().getColumn(7);
          column.setPreferredWidth(120);
          column.setCellRenderer(pendingFeedsRenderer);
          
          column = tblPendingFeeds.getColumnModel().getColumn(8);
          column.setPreferredWidth(120);
          // COEUSDEV-563: End
      }
     
      /*to set the data to the form*/
     private void setFormData(){
         try{
              RequesterBean requester = new RequesterBean();
              requester.setFunctionType('S');
              AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
              comm.send();
              ResponderBean response = comm.getResponse();
              if(response.isSuccessfulResponse()){
                  cvPendingFeeds = (CoeusVector)response.getDataObject();
                  // cvAwardHistory.sort("batchId" , false);
              }else {
                  throw new CoeusClientException(response.getMessage());
              }
          }catch(CoeusClientException e){
              e.printStackTrace();
          }
         if(cvPendingFeeds != null){
             tblPendingFeeds.setRowSelectionInterval(0,0);
         }
     }
     
     /*to display the form*/
     private void display(){
         dlgPendingFeeds.show();
     }
     
     /*to register all the components in the form*/
     public void registerComponents() {
         java.awt.Component[] component = {
             scrPnPendingFeeds,btnClose,btnCancelFeed};
             ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
             setFocusTraversalPolicy(policy);
             setFocusCycleRoot(true);
             
             tblPendingFeeds.setModel(pendingFeedsTableModel);
             btnCancelFeed.addActionListener(this);
             btnClose.addActionListener(this);
             tblPendingFeeds.getTableHeader().addMouseListener(this);
     }
     
//     /** Supporting method which will be used for the focus lost for date
//      *fields. This will be fired when the request focus for the specified
//      *date field is invoked
//      */
//     private void setRequestFocusInThread(final Component component) {
//         SwingUtilities.invokeLater( new Runnable() {
//             public void run() {
//                 component.requestFocusInWindow();
//             }
//         });
//     }
     
     /*the table model for the pending feeds table*/
     public class PendingFeedsTableModel extends AbstractTableModel {
         
         // represents the column names of the table
         private String colName[] = {"Feed Id","Transaction Id","Award Number","Seq Number","Type","Status","Batch Id","Timestamp","User"};
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
             if (cvPendingFeeds == null){
                 return 0;
             } else {
                 return cvPendingFeeds.size();
             }
             
         }
         
         /**
          * To set the data for the model.
          * @param cvAwardBasis CoeusVector
          * @return void
          **/
         public void setData(CoeusVector cvPendingFeeds) {
             cvPendingFeeds = cvPendingFeeds;
             fireTableDataChanged();
         }
         /**
          * To get the value from the table
          * @param rowIndex int
          * @param columnIndex int
          * @return Object
          **/
         public Object getValueAt(int rowIndex, int columnIndex) {
             SapFeedDetailsBean sapFeedDetailsBean = (SapFeedDetailsBean)cvPendingFeeds.get(rowIndex);
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
                 case BATCH_ID:
                     if(sapFeedDetailsBean.getBatchId() == 0){
                         return "";
                     }else{
                         return new Integer(sapFeedDetailsBean.getBatchId());
                     }
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
             SapFeedDetailsBean bean = (SapFeedDetailsBean)cvPendingFeeds.get(row);
             switch(col){
                 case STATUS:
                     if (!value.toString().trim().equals(bean.getFeedStatus())) {
                         bean.setFeedStatus(value.toString());
                     }
                     break;
                 case TIME_STAMP:
                     if (!value.toString().trim().equals(bean.getUpdateTimestamp())) {
                         bean.setUpdateTimestamp((Timestamp)value);
                     }
                     break;
                 case USER:
                     if (!value.toString().trim().equals(bean.getUpdateUser())) {
                         bean.setUpdateUser(value.toString());
                     }
                     break;
             }
             pendingFeedsTableModel.fireTableDataChanged();
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnPendingFeeds = new javax.swing.JScrollPane();
        tblPendingFeeds = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnCancelFeed = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnPendingFeeds.setMaximumSize(new java.awt.Dimension(825, 425));
        scrPnPendingFeeds.setMinimumSize(new java.awt.Dimension(825, 425));
        scrPnPendingFeeds.setPreferredSize(new java.awt.Dimension(825, 425));
        tblPendingFeeds.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnPendingFeeds.setViewportView(tblPendingFeeds);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 5, 2);
        add(scrPnPendingFeeds, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(99, 23));
        btnClose.setMinimumSize(new java.awt.Dimension(99, 23));
        btnClose.setPreferredSize(new java.awt.Dimension(99, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        add(btnClose, gridBagConstraints);

        btnCancelFeed.setFont(CoeusFontFactory.getLabelFont());
        btnCancelFeed.setMnemonic('F');
        btnCancelFeed.setText("Cancel Feed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 0);
        add(btnCancelFeed, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        try{
            if(source.equals(btnClose)){
                dlgPendingFeeds.dispose();
            }else if(source.equals(btnCancelFeed)){
                performCancelFeedAction();
            }
        }finally{
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /*the action performed on the click of the Cancel Feed Button*/
    private void performCancelFeedAction(){
        try{
            int selRow = tblPendingFeeds.getSelectedRow();
            if(selRow == -1){
                CoeusOptionPane.showInfoDialog(SELECT_FEED);
                return;
            }
            
            SapFeedDetailsBean sapBean = (SapFeedDetailsBean)cvPendingFeeds.get(selRow);
            int feedId = sapBean.getFeedId();
//            String status = sapBean.getFeedStatus();
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            "Are you sure you want to cancel the feed - " + feedId + " " ,
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                tblPendingFeeds.setRowSelectionInterval(selRow, selRow);
                sapBean.setFeedStatus("C");
                sapBean.setAcType(TypeConstants.UPDATE_RECORD);
                cvSavePendingFeeds.add(sapBean);
                saveFormData();
                for(int i = 0;i<cvPendingFeeds.size();i++){
                    SapFeedDetailsBean bean = (SapFeedDetailsBean)cvPendingFeeds.get(i);
                    if(bean.getFeedId() == sapBean.getFeedId()){
                        bean.setUpdateTimestamp((Timestamp)cvData.get(1));
                        bean.setUpdateUser(cvData.get(2).toString());
                    }
                }
                
                pendingFeedsTableModel.setData(cvPendingFeeds);
                pendingFeedsTableModel.fireTableDataChanged();
                tblPendingFeeds.setRowSelectionInterval(selRow,selRow);
            } else if(selectedOption == CoeusOptionPane.SELECTION_NO){
                return;
            }
        }catch(CoeusException exception){
            exception.printStackTrace();
        }
    }
    
    
    /*to save the data to the form*/
    private void saveFormData() throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('R');
        if(cvSavePendingFeeds != null && cvSavePendingFeeds.size() > 0){
            requesterBean.setDataObject(cvSavePendingFeeds);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CONNECTION_STRING);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                } else {
                    cvData = (CoeusVector)responderBean.getDataObject();
                    
                }
            }
        }else{
            dlgPendingFeeds.setVisible(false);
        }
    }
    
    /*the action performed on the mouse clicked*/
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        Object source = mouseEvent.getSource();
        int clickCount = mouseEvent.getClickCount();
        int selectedRows = tblPendingFeeds.getSelectedRow();
        SapFeedDetailsBean detailBean = (SapFeedDetailsBean)cvPendingFeeds.get(selectedRows);
        int feedId = detailBean.getFeedId();
        if(source.equals(tblPendingFeeds.getTableHeader())){
            int row = 0;
            if(clickCount == 1){
                Point clickedPoint = mouseEvent.getPoint();
                int xPosition = (int)clickedPoint.getX();
                int columnIndex = tblPendingFeeds.getColumnModel().getColumnIndexAtX(xPosition);
                switch (columnIndex) {
                    case FEED_ID:
                        if(sortCodeAsc) {
                            //Code already sorted in Ascending order. Sort now in Descending order.
                            cvPendingFeeds.sort("feedId", false);
                            sortCodeAsc = false;
                        }else {
                            //Code already sorted in Descending order. Sort now in Ascending order.
                            cvPendingFeeds.sort("feedId", true);
                            sortCodeAsc = true;
                        }
                        break;
                    case TRANS_ID:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("transactionId",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("transactionId",true);
                            sortDescAsc = true;
                        }
                        break;
                    case AWARD_NUM:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("mitAwardNumber",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("mitAwardNumber",true);
                            sortDescAsc = true;
                        }
                        break;
                    case SEQ_NUM:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("sequenceNumber",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("sequenceNumber",true);
                            sortDescAsc = true;
                        }
                        break;
                    case TYPE:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("feedType",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("feedType",true);
                            sortDescAsc = true;
                        }
                        break;
                    case STATUS:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("feedStatus",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("feedStatus",true);
                            sortDescAsc = true;
                        }
                        break;
                    case BATCH_ID:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("batchId",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("batchId",true);
                            sortDescAsc = true;
                        }
                        break;
                    case TIME_STAMP:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("updateTimestamp",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("updateTimestamp",true);
                            sortDescAsc = true;
                        }
                        break;
                    case USER:
                        if(sortDescAsc){
                            cvPendingFeeds.sort("updateUser",false);
                            sortDescAsc = false;
                        }else {
                            cvPendingFeeds.sort("updateUser",true);
                            sortDescAsc = true;
                        }
                        break;
                        
                }//End Switch
                pendingFeedsTableModel.fireTableDataChanged();
                for(int i = 0;i<cvPendingFeeds.size();i++){
                    SapFeedDetailsBean dataBean = (SapFeedDetailsBean)cvPendingFeeds.get(i);
                    if(dataBean.getFeedId() == feedId){
                        row = i;
                    }
                }
                tblPendingFeeds.setRowSelectionInterval(row,row);
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
    
    /*the renderer for the pending feeds table*/
    class PendingFeedsRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private JLabel lblText;
        
        public PendingFeedsRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            txtComponent = new JTextField();
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
//            SapFeedDetailsBean bean = (SapFeedDetailsBean)cvPendingFeeds.get(row);
            java.awt.Color color = tblPendingFeeds.getSelectionBackground();
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
    public javax.swing.JButton btnCancelFeed;
    public javax.swing.JButton btnClose;
    public javax.swing.JScrollPane scrPnPendingFeeds;
    public javax.swing.JTable tblPendingFeeds;
    // End of variables declaration//GEN-END:variables
    
}
