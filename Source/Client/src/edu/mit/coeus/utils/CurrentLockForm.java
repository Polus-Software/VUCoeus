/*
 * LockDeleteForm.java
 *
 * Created on November 24, 2004, 10:21 PM
 */

/* PMD check performed, and commented unused imports and variables on 26-JAN-2011
 * by Bharati
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.UserUtils;
import java.text.MessageFormat;

import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author  chandrashekara
 */
public class CurrentLockForm extends javax.swing.JComponent implements ActionListener{
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgCurrentLock;
    private static final int WIDTH=765;
    private static final int HEIGHT = 420;
    private static final String EMPTY_STRING = "";
    private static final int MODULE_COLUMN = 0;
    private static final int ITEM_COLUMN = 1;
    private static final int USER_COLUMN = 2;
    private static final int CREATE_TIMESTAMP_COLUMN = 3;
    private static final int UPDATE_TIMESTAMP_COLUMN = 4;
    
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yy";
    private static final String LAST_UPDATE_FORMAT = DATE_FORMAT_DISPLAY + " hh:mm a";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    private CoeusMessageResources coeusMessageResources;
//    private boolean modified;
    private CoeusVector cvLock;
    private final String GET_SERVLET ="/LockingServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ GET_SERVLET;
    private static final char GET_ALL_LOCKIDS = 'F';
    private static final char UPDATE_LOCK_ID = 'G';    
    private CurrentLockTableModel currentLockTableModel;
    private CurrentTableCellRenderer currentTableCellRenderer;
    
    //Added for COEUSQA-1442 : Have current locks say "unlock" instead of "delete" - start
    //private String msg = "Are you sure you want to delete the lock on";
    private static final String SURE_TO_UNLOCK_MSG = "currentLockFrm_exceptionCode.1002";
   // private static final String SELECT_LOCK_DELETE = "orgIDCPnl_exceptionCode.1097";
    private static final String SELECT_UNLOCK_ROW = "currentLockFrm_exceptionCode.1001";
     //Added for COEUSQA-1442 : Have current locks say "unlock" instead of "delete" - end
    private String responderMessage;
    private boolean isLoggedInUser; 
    private CoeusVector cvUserLockData;
    /** Creates new form LockDeleteForm */
    public CurrentLockForm(CoeusAppletMDIForm mdiForm,boolean isLoggedInUser) throws CoeusException{
        this.mdiForm = mdiForm;
        this.isLoggedInUser = isLoggedInUser;
        initComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setFormData();
        setColumnData();
        simpleDateFormat.applyPattern(LAST_UPDATE_FORMAT);
        postInitComponents();
    }
    
    /** Register all the components, instantiate utility classes for the 
     *corresponding components
     */
     private void registerComponents(){
        currentLockTableModel= new CurrentLockTableModel();
        currentTableCellRenderer = new CurrentTableCellRenderer();
        tblLockDelete.setModel(currentLockTableModel);
        btnClose.addActionListener(this);
        btnDelete.addActionListener(this);
        btnRefresh.addActionListener(this);
        
        java.awt.Component[] components = {btnRefresh,btnRefresh,btnClose};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);  
        
    }
     /** Get the data from the server and set it to the respective component
      *like table
      */
     public void setFormData() throws CoeusException{
         cvLock = new CoeusVector();
         cvLock = getLockData();
         Equals eqLoggedInUser = null;
         /* Allow user to manipulate the lock data for the logged in user
          *The window opens from File menu will contain the list of locks
          *which are acquired by the logged in user
          */
         if(isLoggedInUser){
             if(cvLock!= null && cvLock.size() > 0){
                 cvUserLockData = new CoeusVector();
                 for(int index = 0; index < cvLock.size(); index++){
                     LockingBean bean = (LockingBean)cvLock.get(index);
                     if(bean.getUserID().equalsIgnoreCase(mdiForm.getUserId())){
                         eqLoggedInUser = new Equals("userId", bean.getUserID());
                         cvUserLockData.addElement(bean);
                         continue;
                     }
                 }
             }
                 
         }
         if(isLoggedInUser){
             cvLock = new CoeusVector();
             if(cvUserLockData!= null && cvUserLockData.size() > 0){
                cvLock.addAll(cvUserLockData);
             }
         }
         currentLockTableModel.setData(cvLock);
     }
     /** communicate with the server to get all the locks available
      *for the coeus Application in an Vector
      *@returns coeusVector
      */
     private CoeusVector getLockData() throws CoeusException{
        CoeusVector data = null;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_ALL_LOCKIDS);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder!= null){
            if(responder.isSuccessfulResponse()){
                data = (CoeusVector)responder.getDataObjects();
            }
			responderMessage = responder.getMessage();
		} else{
            throw new CoeusException(responder.getMessage(),0);
        }
        return data;
     }
     
     /** Set the headers, set its properticies. Get the respective column
      *indicies and set the renderers and set the column size
      */
     private void setColumnData(){
        JTableHeader tableHeader = tblLockDelete.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        tblLockDelete.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblLockDelete.setRowHeight(22);
        tblLockDelete.setShowHorizontalLines(true);
        tblLockDelete.setShowVerticalLines(true);
        tblLockDelete.setOpaque(true);
        tblLockDelete.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        tblLockDelete.setRowSelectionAllowed(true);
        
        TableColumn column = tblLockDelete.getColumnModel().getColumn(MODULE_COLUMN);
        column.setPreferredWidth(150);
        column.setResizable(true);
        
        column = tblLockDelete.getColumnModel().getColumn(ITEM_COLUMN);
        column.setPreferredWidth(145);
        column.setResizable(true);
        
        column = tblLockDelete.getColumnModel().getColumn(USER_COLUMN);
        column.setPreferredWidth(99);
        column.setResizable(true);
        
        column = tblLockDelete.getColumnModel().getColumn(CREATE_TIMESTAMP_COLUMN);
        column.setPreferredWidth(120);
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
        
        column = tblLockDelete.getColumnModel().getColumn(UPDATE_TIMESTAMP_COLUMN);
        column.setPreferredWidth(125);
        column.setResizable(true);
        column.setCellRenderer(currentTableCellRenderer);
    }
     
     private void postInitComponents(){
        dlgCurrentLock = new CoeusDlgWindow(mdiForm);
        dlgCurrentLock.getContentPane().add(this);
        if(isLoggedInUser){
            //dlgCurrentLock.setTitle("Current Locks for "+mdiForm.getUserId());
          /*
           *UserId to UserName Enhancement - Start
           *Added UserUtils class to change userid to username
           */
            dlgCurrentLock.setTitle("Current Locks for "+UserUtils.getDisplayName(mdiForm.getUserId()));
            // UserId to UserName Enhancement - End
        }else{
            dlgCurrentLock.setTitle("Current Locks " );
        }
        dlgCurrentLock.setFont(CoeusFontFactory.getLabelFont());
        dlgCurrentLock.setModal(true);
        dlgCurrentLock.setResizable(false);
        dlgCurrentLock.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCurrentLock.getSize();
        dlgCurrentLock.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgCurrentLock.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                 performCancelAction();
                return;
            }
        });
        dlgCurrentLock.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgCurrentLock.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                 performCancelAction();
                return;
            }
        });
        
        dlgCurrentLock.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setWindowFocus();
            }
        });
    }
    // Set the default focus to the component when the form is loaded
    private void setWindowFocus(){
        btnClose.requestFocusInWindow();
		// if lock id's are there in DB with old format then display a message.
		if (!("").equals(responderMessage)) {
			CoeusOptionPane.showInfoDialog(responderMessage);
		}
    }
    
     public void actionPerformed(ActionEvent actionEvent) {
         Object source = actionEvent.getSource();
         try{
             dlgCurrentLock.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
             if(source.equals(btnClose)){
                 performCancelAction();
             }else if(source.equals(btnRefresh)){
                 performRefreshAction();
             }else if(source.equals(btnDelete)){
                 performDeleteAction();
             }
         }catch (CoeusException coeusException){
             coeusException.printStackTrace();
             CoeusOptionPane.showErrorDialog(coeusException.getMessage());
         }
         finally{
             dlgCurrentLock.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
         }
    }
     
     /** event method to perform the close action
      */
      private void performCancelAction(){
        dlgCurrentLock.setVisible(false);
      }
      
      /** When the user performs Refresh action, refresh the data. Get
       *the data from the server again.
       */
      private void performRefreshAction() throws CoeusException{
          setFormData();
          if(tblLockDelete.getRowCount()>0){
              tblLockDelete.setRowSelectionInterval(0,0);
          }
		  // if lock id's are there in DB with old format then display a message.
			if (!("").equals(responderMessage)) {
				CoeusOptionPane.showInfoDialog(responderMessage);
			}
      }
      // Display the window
      public void display(){
          if(tblLockDelete.getRowCount() > 0){
              btnDelete.setEnabled(true);
          }else{
              btnDelete.setEnabled(false);
          }
          dlgCurrentLock.setVisible(true);
	}
      
      private void performDeleteAction() throws CoeusException{
          int selRow = tblLockDelete.getSelectedRow();
          String item = EMPTY_STRING;
          String moduleName = EMPTY_STRING;
          if(selRow!=-1){
              moduleName = (String)tblLockDelete.getValueAt(selRow, MODULE_COLUMN);
              item = (String)tblLockDelete.getValueAt(selRow, ITEM_COLUMN);
              //Added for Coeusqa-1442 - Have current locks say unlock instead of delete - start
              String message = "";
              StringBuffer moduleData = new StringBuffer();
              moduleData.append(" "+moduleName);
              moduleData.append(" "+item);                      
              MessageFormat formatter = new MessageFormat("");
              message = formatter.format(coeusMessageResources.parseMessageKey(SURE_TO_UNLOCK_MSG),
                                            moduleData.toString());                                                   
              int selectedOption = CoeusOptionPane.showQuestionDialog(
              coeusMessageResources.parseMessageKey(message), 
              CoeusOptionPane.OPTION_YES_NO,
              CoeusOptionPane.DEFAULT_YES);
              //Added for Coeusqa-1442 - Have current locks say unlock instead of delete - end
              if(selectedOption == CoeusOptionPane.SELECTION_YES){
                  cvLock.remove(selRow);
                  currentLockTableModel.fireTableRowsDeleted(selRow, selRow);
                  updateData(moduleName,item);
                  
                  if(tblLockDelete.getRowCount()>0){
                      btnDelete.setEnabled(true);
                  }else{
                      btnDelete.setEnabled(false);
                  }
              }else{
                  // do nothing
              }
          }else{
              CoeusOptionPane.showInfoDialog(
              coeusMessageResources.parseMessageKey(SELECT_UNLOCK_ROW));
          }
      }
      
      /** Update the lock table.When user clicks on delete button , it immediately
       *commits the transactions
       */
      private void updateData(String module, String item) throws CoeusException{
          CoeusVector data = new CoeusVector();
          data.addElement(module);
          data.addElement(item);
          RequesterBean requester = new RequesterBean();
          requester.setFunctionType(UPDATE_LOCK_ID);
          requester.setDataObjects(data);
          AppletServletCommunicator comm
          = new AppletServletCommunicator(connectTo, requester);
          
          comm.send();
          ResponderBean responder = comm.getResponse();
          if(responder!= null){
              if(responder.isSuccessfulResponse()){
              }else{
                  throw new CoeusException(responder.getMessage(),0);
              }
          }else{
              throw new CoeusException(responder.getMessage(),0);
          }
      }
      
      public class CurrentLockTableModel extends AbstractTableModel{
          private String[] colName = {"Module","Item","User","Create Timestamp","Update Timestamp"};
          private Class[] colClass = {String.class,String.class,String.class,String.class,String.class,String.class};
          
          public boolean isCellEditable(int row,int col){
              return false;
          }
          public int getColumnCount() {
              return colName.length;
          }
          
          public String getColumnName(int col){
              return colName[col];
          }
          
          public Class getColumnClass(int col){
              return colClass[col];
          }
          
          public void setData(CoeusVector cvLock){
              cvLock = cvLock;
              fireTableDataChanged();
          }
          
          public int getRowCount() {
              if(cvLock==null){
                  return 0;
              }else{
                  return cvLock.size();
              }
          }
          
          public Object getValueAt(int row, int col) {
              LockingBean lockingBean = (LockingBean)cvLock.get(row);
              switch(col){
                  case MODULE_COLUMN:
                      return lockingBean.getModuleName();
                  case ITEM_COLUMN:
                      return lockingBean.getLockID();
                  case USER_COLUMN:
//                      return lockingBean.getUserID();
                      /*
                       * UserId to UserName Enhancement - Start
                       * Added new property getUpdateUserName to get username
                       */
                      return lockingBean.getUpdateUserName();
                      // UserId to UserName Enhancement - End
                  case CREATE_TIMESTAMP_COLUMN:
                      return lockingBean.getCreateTimestamp();
                  case UPDATE_TIMESTAMP_COLUMN:
                      //Modified for COEUSDEV-192 : Update timestamp is not refreshed in Current locks window  -Start
//                      return lockingBean.getCreateTimestamp();
                      return lockingBean.getUpdateTimestamp();
                      //COEUSDEV-192 : END
                      
              }
              return EMPTY_STRING;
          }
      }
      
      public class CurrentTableCellRenderer extends DefaultTableCellRenderer{
          
          public CurrentTableCellRenderer(){
              
          }
          
          public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus,int row, int col){
              switch(col){
                  case CREATE_TIMESTAMP_COLUMN:
                  case UPDATE_TIMESTAMP_COLUMN:
                      if(value!= null && !(value.toString().trim().equals(EMPTY_STRING))){
                          //Commented  for COEUSDEV-192 : Update timestamp is not refreshed in Current locks window - Start
                          //Commented to display update time and Create time in 'YYYY-MM-DD HH:MM:SEC.0'
//                          value = simpleDateFormat.format(value);
                          //COEUSDEV-192 : END
                       setText(value.toString());
                      }else{
                          setText(EMPTY_STRING);
                      }
              }
              return super.getTableCellRendererComponent(
              table, value, isSelected, hasFocus, row, col);
          }
      }
      
       /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort by Question Id, 
     *explanation Type and Explnation
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        // Modified for COEUSDEV-787 - Current Lock user column not sorting alphabetically - Start
        // Instead of sorting based on the userId, sort will happen based on updateUserName
//        String nameBeanId [][] ={
//            {"0","moduleName" },
//            {"1","lockID" },
//            {"2","userID"},
//            {"3","createTimestamp"},
//            {"4","updateTimestamp"},
//        };
        String nameBeanId [][] ={
            {"0","moduleName" },
            {"1","lockID" },
            {"2","updateUserName"},
            {"3","createTimestamp"},
            {"4","updateTimestamp"},
        };
        // Modified for COEUSDEV-787 - Current Lock user column not sorting alphabetically - End
        boolean sort =true;
        /**
         * @param evt
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvLock!=null && cvLock.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvLock).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    currentLockTableModel.fireTableRowsUpdated(0, currentLockTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.printStackTrace();
                //exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
      
      /** This method is called from within the constructor to
       * initialize the form.
       * WARNING: Do NOT modify this code. The content of this method is
       * always regenerated by the Form Editor.
       */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnRefresh = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnLockDelete = new javax.swing.JScrollPane();
        tblLockDelete = new javax.swing.JTable();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(730, 420));
        setPreferredSize(new java.awt.Dimension(730, 420));
        btnRefresh.setFont(CoeusFontFactory.getLabelFont());
        btnRefresh.setMnemonic('R');
        btnRefresh.setText("Refresh");
        btnRefresh.setMinimumSize(new java.awt.Dimension(79, 26));
        btnRefresh.setPreferredSize(new java.awt.Dimension(79, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(btnRefresh, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(79, 26));
        btnClose.setMinimumSize(new java.awt.Dimension(79, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(79, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(btnClose, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Unlock");
        btnDelete.setMinimumSize(new java.awt.Dimension(79, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(79, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(btnDelete, gridBagConstraints);

        scrPnLockDelete.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrPnLockDelete.setMinimumSize(new java.awt.Dimension(660, 395));
        scrPnLockDelete.setPreferredSize(new java.awt.Dimension(680, 395));
        tblLockDelete.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnLockDelete.setViewportView(tblLockDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
        add(scrPnLockDelete, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnRefresh;
    public javax.swing.JScrollPane scrPnLockDelete;
    public javax.swing.JTable tblLockDelete;
    // End of variables declaration//GEN-END:variables
    
}
