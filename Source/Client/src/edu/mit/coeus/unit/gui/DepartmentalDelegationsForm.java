/*
 * DepartmentalDelegationsForm.java
 *
 * Created on March 8, 2004, 1:41 PM
 */

package edu.mit.coeus.unit.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.user.bean.UserDelegationController;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;
import java.util.HashMap;

/**
 *
 * @author  chandru
 */
public class DepartmentalDelegationsForm extends javax.swing.JComponent
implements ActionListener{
    
    private DateUtils dtUtils = new DateUtils();
    private static final String REQUIRED_DATE_FORMAT = "dd-MMM-yyyy";
    private static final int DELEGATED_BY_COLUMN = 0;
    private static final int DELEGATED_TO_COLUMN = 1;
    private static final int STATUS_COLUMN = 2;
    private static final int DATE_COLUMN = 3;
    private static final int WIDTH = 590;
    private static final int HEIGHT = 320;
    private static final String EMPTY_STRING = "";
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgDelegations;
    private String unitNumber = EMPTY_STRING;
    private String unitName = EMPTY_STRING;
    private static final char GET_DELEGATION = 'E';
    private static final char UPDATE_DELAGATION = 'F'; 
    private final String GET_DELEGATION_SERVLET ="/unitServlet";
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + GET_DELEGATION_SERVLET;
    
    private CoeusVector cvDelegation;
    private CoeusVector dataVector;
    private boolean hasRights = false;
    
    private DelegationTableModel delegationTableModel;
    private DelegationTableRenderer delegationTableRenderer;
    private DelegationCellEditor  delegationCellEditor;
    private UserDelegationsBean userDelegationsBean;
    
    //status
    private static final char NOT_ACCEPTED = 'Q';
    private static final char REQUESTED = 'Q';
    private static final char ACCEPTED = 'P';
    private static final char REJECTED = 'R';
    private static final char CLOSED = 'C';
    
    //Status Text
    private static final String MSG_REQUESTED = "Requested";
    private static final String MSG_ACCEPTED = "Accepted";
    private static final String MSG_REJECTED = "Rejected";
    private static final String MSG_CLOSED = "Closed";
    
    private static final String USER_SEARCH = "USERSEARCH";
    private static final String USER_ID_KEY = "USER_ID";
    private static final String USER_NAME_KEY = "USER_NAME";
    
    private static final String SELF_DELEGATION = "departmetalSelf_exceptionCode.1100";
    private static final String DELEGATED_SOMEONE =  "departmetal_exceptionCode.1101";
    private static final String REJECTED_BY_USER = "departmetal_exceptionCode.1102";
    private static final String NOT_ACTIVE_USERS = "departmental_exceptionCode.1104";
    
    
    private CoeusMessageResources  coeusMessageResources;
    private CoeusSearch coeusSearch;
    private boolean modified = false;
    
    /** Creates new form DepartmentalDelegationsForm */
    public DepartmentalDelegationsForm(CoeusAppletMDIForm mdiForm,String unitNumber, String unitName) {
        this.mdiForm = mdiForm;
        this.unitNumber = unitNumber;
        this.unitName = unitName;
        initComponents();
        registerComponents();
        setFormData();
        coeusMessageResources = CoeusMessageResources.getInstance();
        lblUnitName.setText(unitName);
        dataVector = new CoeusVector();
        setColumnData();
        postInitComponents();
        display();
    }
    
    /** display this form based on right and the data in the table. */    
    private void display(){
        //Added for Case#3682 - Enhancements related to Delegations - Start
        btnCancel.setVisible(false);
        //Added for Case#3682 - Enhancements related to Delegations - End
        if(!hasRights){
            CoeusOptionPane.showInfoDialog("You do not have the right to view unit delegations for  "+unitName);
            dlgDelegations.setVisible(false);
        }else if(tblDeptDelegations.getRowCount() <= 0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NOT_ACTIVE_USERS));
            dlgDelegations.setVisible(false);
        }else{
            dlgDelegations.setVisible(true);
        }
    }
    
    /** Initialize the dialog box and set all the properticies to the dialog box */    
    private void postInitComponents(){
        dlgDelegations = new CoeusDlgWindow(mdiForm);
        dlgDelegations.getContentPane().add(this);
        dlgDelegations.setTitle("User Delegations");
        dlgDelegations.setFont(CoeusFontFactory.getLabelFont());
        dlgDelegations.setModal(true);
        dlgDelegations.setResizable(false);
        dlgDelegations.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgDelegations.getSize();
        dlgDelegations.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgDelegations.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgDelegations.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgDelegations.addWindowListener(new WindowAdapter(){
             public void windowActivated(WindowEvent we){
                 setDefaultFocus();
             }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
    }
    private void setDefaultFocus(){
        if(!hasRights){
            btnCancel.requestFocusInWindow();
        }else{
            btnOk.requestFocusInWindow();
        }
    }
    
    // Set the form data. get the data from the server one as vector of table data
    // and the other one as right for opening this form
    private void setFormData(){
        Vector data = getFormData();
        cvDelegation = new CoeusVector();
        Boolean obj = (Boolean) data.elementAt(0);
        cvDelegation = (CoeusVector)data.elementAt(1);
        hasRights = obj.booleanValue();
        
        delegationTableModel.setData(cvDelegation);
    }
    
    /** communicate with the server to get the table data and the right for this
     *form. Set unit number as a data object
     */
    private Vector getFormData(){
        Vector data = null;
        
        RequesterBean requester;
        ResponderBean responder;
        
        requester = new RequesterBean();
        requester.setFunctionType(GET_DELEGATION);
        requester.setDataObject(unitNumber);
        
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            data = (Vector)responder.getDataObjects();
//            System.out.println("Successfull Response !!!");
        }else{
//            System.out.println("Problem while getting !!!");
        }
        return data;
    }
    
    // register all the components and create the instance of renderer and 
    // editor
    private void registerComponents(){
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        delegationCellEditor = new DelegationCellEditor();
        delegationTableRenderer = new DelegationTableRenderer();
        delegationTableModel = new DelegationTableModel();
        tblDeptDelegations.setModel(delegationTableModel);
        
        Component[] comp = {btnOk, btnCancel};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
    
    
    // set table related details. set the column header, column size and other
    // properticies
    private void setColumnData(){
        
       JTableHeader tableHeader = tblDeptDelegations.getTableHeader();
       tableHeader.setReorderingAllowed(false);
       tableHeader.setFont(CoeusFontFactory.getLabelFont());
       
       tblDeptDelegations.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
       tblDeptDelegations.setRowHeight(22);
       tblDeptDelegations.setSelectionBackground(java.awt.Color.white);
       tblDeptDelegations.setSelectionForeground(java.awt.Color.black);
       tblDeptDelegations.setShowHorizontalLines(true);
       tblDeptDelegations.setShowVerticalLines(true);
       tblDeptDelegations.setOpaque(false);
       tblDeptDelegations.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
       
       TableColumn column = tblDeptDelegations.getColumnModel().getColumn(DELEGATED_BY_COLUMN);
       column.setMinWidth(100);
       column.setMaxWidth(180);
       column.setPreferredWidth(150);
       column.setResizable(true);
       column.setCellRenderer(delegationTableRenderer);
       
       column = tblDeptDelegations.getColumnModel().getColumn(DELEGATED_TO_COLUMN);
       column.setMinWidth(146);
       column.setMaxWidth(146);
       column.setPreferredWidth(176);// To set exactly for the column width
       column.setResizable(true);
       column.setCellRenderer(delegationTableRenderer);
       //Commented for Case#3682 - Enhancements related to Delegations - Start
//       column.setCellEditor(delegationCellEditor);
       //Commented for Case#3682 - Enhancements related to Delegations - End
       column = tblDeptDelegations.getColumnModel().getColumn(STATUS_COLUMN);
       column.setMinWidth(90);
       column.setMaxWidth(120);
       column.setPreferredWidth(85);
       column.setResizable(true);
       column.setCellRenderer(delegationTableRenderer);
       
       column = tblDeptDelegations.getColumnModel().getColumn(DATE_COLUMN);
       column.setMinWidth(110);
       column.setMaxWidth(120);
       column.setPreferredWidth(95);
       column.setResizable(true);
       column.setCellRenderer(delegationTableRenderer);
    }
    
    /** Listener for the form buttons
     * @param actionEvent
     */    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnCancel)){
            performCancelAction();
        }else if(source.equals(btnOk)){
            performOkAction();
        }
    }
    
    /** Method to define the cancel operation. This method is trigger when cancel button
     * has clicked
     */    
    private void performCancelAction(){
        if(modified){
            confirmClosing();
        }else{
            dlgDelegations.dispose();
        }
    }
    
    /** Confirm the closing action */    
    private void confirmClosing(){
         try{
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                performOkAction();
            }else if(option == CoeusOptionPane.SELECTION_NO){
                dlgDelegations.setVisible(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return;
            }
        }catch(Exception exception){
            exception.getMessage();
        }
    }
    
    /** Performs the Ok action. When button ok is clicked,it has to update the form data
     * only if the data has been modified
     */    
    private void performOkAction(){
            updateFormData();
        dlgDelegations.dispose();
    }
    
    public class DelegationTableModel extends AbstractTableModel implements TableModel{
        
        private String colNames[] = {"Delegated By","Delegated To","Status","Effective Date"};
        private Class colClass[] = {String.class,String.class, String.class,String.class};
        DelegationTableModel(){
        }
        /**
         * @param row
         * @param col
         * @return
         */        
        public boolean isCellEditable(int row, int col){
            //Added for Case#3682 - Enhancements related to Delegations - Start
            if(col==DELEGATED_BY_COLUMN || col==STATUS_COLUMN || col==DATE_COLUMN || col==DELEGATED_TO_COLUMN){
            //Added for Case#3682 - Enhancements related to Delegations - End
                return false;
            }else{
                return true;
            }
        }
        
        /**
         * @return
         */        
        public int getColumnCount(){
            return colNames.length;
        }
        
        /**
         * @param columnIndex
         * @return
         */        
        public Class getColumnClass(int columnIndex) {
            return colClass [columnIndex];
        }
        
        /**
         * @param column
         * @return
         */        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /**
         * @return
         */        
        public int getRowCount() {
            if(cvDelegation==null){
                return 0;
            }else{
                return cvDelegation.size();
            }
        }
        
        /**
         * @param cvDelegation
         */        
        public void setData(CoeusVector cvDelegation){
            cvDelegation = cvDelegation;
        }
        
        /**
         * @param row
         * @param column
         * @return
         */        
        public Object getValueAt(int row, int column) {
            UserDelegationsBean userDelegationsBean = (UserDelegationsBean)cvDelegation.get(row);
            switch(column){
                case DELEGATED_BY_COLUMN:
                    return userDelegationsBean.getUserName();
                case DELEGATED_TO_COLUMN:
                    return userDelegationsBean.getDelegatedToName();
                case STATUS_COLUMN:
                    return new Character(userDelegationsBean.getStatus());
                case DATE_COLUMN:
                    return userDelegationsBean.getEffectiveDate();
            }
            return EMPTY_STRING;
        }
    }
    
    public class DelegationCellEditor extends AbstractCellEditor 
    implements TableCellEditor,ActionListener{
        
        private JPanel pnlDelegate;
        private JTextField txtDelegateTo;
        //Commented for Case#3682 - Enhancements related to Delegations - Start
        //private JButton btnDelegateTo;
        //Commented for Case#3682 - Enhancements related to Delegations - End
        private ImageIcon imgDelegate;
        private int column, row;
        
        
        public DelegationCellEditor(){
           java.net.URL findUser= getClass().getClassLoader().getResource( CoeusGuiConstants.SEARCH_ICON );
           
           pnlDelegate = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
           txtDelegateTo = new JTextField();
           txtDelegateTo.setMinimumSize(new java.awt.Dimension(120, 22));
           txtDelegateTo.setPreferredSize(new java.awt.Dimension(120, 22));
           txtDelegateTo.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
           txtDelegateTo.setEditable(false);
           
           //Commented for Case#3682 - Enhancements related to Delegations - Start
           /*btnDelegateTo = new JButton();
           
           // to specify the button properticies
           btnDelegateTo.setVerticalTextPosition(SwingConstants.BOTTOM);
           btnDelegateTo.setHorizontalTextPosition(SwingConstants.CENTER);
           btnDelegateTo.setMargin(new Insets(0,0,0,0));*/
           
           
           //imgDelegate = new ImageIcon(findUser);
           //Commented for Case#3682 - Enhancements related to Delegations - End
            postRegisterComponents();
        }
        
        /** add the action listener for the button delegate */        
         private void postRegisterComponents(){
             //Commented for Case#3682 - Enhancements related to Delegations - Start
           // btnDelegateTo.addActionListener(this);
             //Commented for Case#3682 - Enhancements related to Delegations - End
        }
        
         /**
          * @param table
          * @param value
          * @param isSelected
          * @param row
          * @param column
          * @return
          */         
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
                this.column = column;
                this.row = row;
                switch(column){
                    //Commented for Case#3682 - Enhancements related to Delegations - Start
                    /*case DELEGATED_TO_COLUMN:
                        txtDelegateTo.setText(value.toString());
                        btnDelegateTo.setIcon(imgDelegate);
                        pnlDelegate.add(txtDelegateTo);
                        pnlDelegate.add(btnDelegateTo);
                        return pnlDelegate;*/
                    //Commented for Case#3682 - Enhancements related to Delegations - End
                    }
                return pnlDelegate;
        }
        
        /**
         * @return
         */        
         public Object getCellEditorValue() {
             switch(column){
                 case DELEGATED_TO_COLUMN:
                     return txtDelegateTo.getText();
                     //return pnlDelegate;
             }
             return ((JTextField)txtDelegateTo).getText();
             //return pnlDelegate;
        }
        
         /**
          * @param e
          */         
          public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            //Commented for Case#3682 - Enhancements related to Delegations - Start
            /*if(source.equals(btnDelegateTo)){
                findUser();
            }*/
            //Commented for Case#3682 - Enhancements related to Delegations - End
        }
       
          /** Search for the new user as a delegate.Pass the values to pass all the
           * validations. finally find the user and set to the textfield
           */          
          private void findUser(){
              UserDelegationController userDelegationController = new UserDelegationController();;
              UserDelegationsBean bean = (UserDelegationsBean)cvDelegation.get(row);
              String delegatedUserId = bean.getDelegatedBy();
              java.sql.Date effectiveDate = bean.getEffectiveDate();
              int selRow = tblDeptDelegations.getSelectedRow();
              String delegatedByName = (String)delegationTableModel.getValueAt(selRow, DELEGATED_BY_COLUMN);
              String userId = EMPTY_STRING;
              String userName = EMPTY_STRING;
              int value = 0;
              try{
                  coeusSearch = new CoeusSearch(dlgDelegations, USER_SEARCH, CoeusSearch.TWO_TABS);
                  coeusSearch.showSearchWindow();
                  HashMap selectedRow= coeusSearch.getSelectedRow();
                  if(selectedRow != null) {
                      userId = selectedRow.get(USER_ID_KEY).toString().trim();
                      userName = selectedRow.get(USER_NAME_KEY).toString().trim();
                      // Make a server call to call fn_ok_to_delegate. This will help 
                      // for the user details.
                      value = userDelegationController.canDelegate(delegatedUserId ,
                      userId, effectiveDate);
                      if(delegatedByName.trim().equals(userName)){
                          CoeusOptionPane.showInfoDialog(
                          coeusMessageResources.parseMessageKey(SELF_DELEGATION));
                          return; 
                      }else if(value==-1){
                          CoeusOptionPane.showInfoDialog(
                          coeusMessageResources.parseMessageKey(DELEGATED_SOMEONE));
                          return ;
                      }else if(value==-3){
                          CoeusOptionPane.showInfoDialog(
                          coeusMessageResources.parseMessageKey(REJECTED_BY_USER));
                          return ;
                      }else{
                        txtDelegateTo.setText(userName);
                        modified = true;
                        bean.setDelegatedTo(userId);
                        bean.setStatus(bean.getStatus());
                        bean.setDelegatedBy(delegatedUserId);
                        bean.setEffectiveDate(effectiveDate);
                        bean.setDelegatedToName(userName);
                        bean.setAcType(TypeConstants.UPDATE_RECORD);
                        dataVector.add(bean);
                      }
                  }
              }catch(Exception exception){
                  exception.printStackTrace();
              }
          }
    }
    
    /** Update the form data only if the data has been chansged */    
    private void updateFormData(){
        CoeusVector data = new CoeusVector();
        if(dataVector!=null && dataVector.size() > 0){
            data = dataVector.filter(new NotEquals("acType", null));
        }
        if(data!=null && data.size() > 0){
            
            RequesterBean requester;
            ResponderBean responder;
            requester = new RequesterBean();
            requester.setFunctionType(UPDATE_DELAGATION);
            requester.setDataObjects(data);
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
//                System.out.println("Successfull Update!!!");
            }else{
//                System.out.println("Problem while Updating!!!");
            }
        }
        
    }
    
    
    
    
    public class DelegationTableRenderer extends DefaultTableCellRenderer {
        JPanel pnlDelegate;
        JLabel lblComponent;
        JLabel lblDateComponent;
        JLabel lblStatus;
        JTextField txtDelegateTo;
        //Commented for Case#3682 - Enhancements related to Delegations - Start
        //JButton btnDelegateTo;
        //Commented for Case#3682 - Enhancements related to Delegations - End
        ImageIcon imgDelegate;
        
        
        
        public DelegationTableRenderer(){
            java.net.URL findUser= getClass().getClassLoader().getResource( CoeusGuiConstants.SEARCH_ICON );
            
            pnlDelegate = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
            txtDelegateTo = new JTextField();
            
            txtDelegateTo.setMinimumSize(new java.awt.Dimension(120, 22));
            txtDelegateTo.setPreferredSize(new java.awt.Dimension(120, 22));
            
            txtDelegateTo.setEditable(false);
            txtDelegateTo.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            
            //Commented for Case#3682 - Enhancements related to Delegations - Start
            /*btnDelegateTo = new JButton();
            btnDelegateTo.setMaximumSize(new java.awt.Dimension(43, 20));
            btnDelegateTo.setMinimumSize(new java.awt.Dimension(43, 20));
            btnDelegateTo.setPreferredSize(new java.awt.Dimension(25, 20));
            // to specify the button properticies
            btnDelegateTo.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnDelegateTo.setHorizontalTextPosition(SwingConstants.CENTER);
            btnDelegateTo.setMargin(new Insets(0,0,0,0));*/
            //Commented for Case#3682 - Enhancements related to Delegations - End
            
            lblComponent = new JLabel();
            lblDateComponent = new JLabel();
            lblStatus = new JLabel();
            //Commented for Case#3682 - Enhancements related to Delegations - Start
            //imgDelegate = new ImageIcon(findUser);
            //Commented for Case#3682 - Enhancements related to Delegations - End
            
        }
        
        
        
        /**
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param column
         * @return
         */        
        public java.awt.Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected, boolean hasFocus, int row, int column){
            
            switch(column){
                case STATUS_COLUMN:
                    UserDelegationsBean bean = (UserDelegationsBean)cvDelegation.get(row);
                    if(bean.getStatus()== REQUESTED){
                        value = MSG_REQUESTED;
                    }else if(bean.getStatus()==ACCEPTED){
                        value = MSG_ACCEPTED;
                    }else if(bean.getStatus()==REJECTED){
                        value = MSG_REJECTED;
                    }else if(bean.getStatus()==CLOSED){
                        value = MSG_CLOSED;
                    }
                    lblStatus.setText(value.toString());
                    return lblStatus;
                case DELEGATED_BY_COLUMN:
                    lblComponent.setText(value.toString());
                    return lblComponent;
                case DATE_COLUMN:
                    value = dtUtils.formatDate(value.toString(),REQUIRED_DATE_FORMAT);
                    lblDateComponent.setText(value.toString());
                    return lblDateComponent;
                    //Modified for Case#3682 - Enhancements related to Delegations - Start
                case DELEGATED_TO_COLUMN:
                    lblComponent.setText(value.toString());
                    //btnDelegateTo.setIcon(imgDelegate);
//                    pnlDelegate.add(lblComponent);
                    //pnlDelegate.add(btnDelegateTo);
//                    return pnlDelegate;
                    //Modified for Case#3682 - Enhancements related to Delegations - End
            }
            
            return lblComponent;
        }
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jcPnDeptDelegations = new javax.swing.JScrollPane();
        tblDeptDelegations = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblUnitName = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jcPnDeptDelegations.setBorder(new javax.swing.border.EtchedBorder());
        jcPnDeptDelegations.setMinimumSize(new java.awt.Dimension(500, 320));
        jcPnDeptDelegations.setPreferredSize(new java.awt.Dimension(500, 340));
        tblDeptDelegations.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jcPnDeptDelegations.setViewportView(tblDeptDelegations);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        add(jcPnDeptDelegations, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 7, 0, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 7, 0, 0);
        add(btnCancel, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Delegations for");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        add(lblTitle, gridBagConstraints);

        lblUnitName.setFont(CoeusFontFactory.getLabelFont());
        lblUnitName.setText("label1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        add(lblUnitName, gridBagConstraints);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JScrollPane jcPnDeptDelegations;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JLabel lblUnitName;
    public javax.swing.JTable tblDeptDelegations;
    // End of variables declaration//GEN-END:variables
    
}
