/*
 * FormRoleModule.java
 *
 * Created on May 15, 2007, 3:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.codetable.bean.AllCodeTablesBean;
import edu.mit.coeus.codetable.bean.ColumnBean;
import edu.mit.coeus.codetable.bean.DataBean;
import edu.mit.coeus.codetable.bean.RequestTxnBean;
import edu.mit.coeus.codetable.bean.StoredProcedureBean;
import edu.mit.coeus.codetable.bean.TableStructureBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.personroles.bean.PersonRoleInfoBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
//coeusdev-568 start
import edu.mit.coeus.utils.CoeusUtils;
//coeusdev-568 end
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.TableSorterCodeTable;
import edu.mit.coeus.utils.query.Equals;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author talarianand
 */
public class FormRoleModule extends CoeusDlgWindow {
    
    
    private CoeusMessageResources coeusMessageResources;
    
    private JLabel lblModule;
    
    private JLabel lblRole;
    
    private JPanel pnlRole;
    
    private JPanel pnlModule;
    
    private GridBagConstraints gridBagConstraints;
    
    AllCodeTablesBean allCodeTablesBean ;
    
    private CoeusComboBox cmbModule = new CoeusComboBox();
    
    private boolean userHasRights = false;
    
    private TableSorterCodeTable sorter;
    
    private JTable tblModuleRole;
    
    private JScrollPane scrlpnlForTableCtrl;
    
    private JSplitPane splitpnlMain;
    
    private JPanel pnlForBtn;
    
    private JButton btnAdd;
    
    private JButton btnSave;
    
    private JButton btnDelete;
    
    private JButton btnClose;
    
    private HashMap hashAllCodeTableStructure = null;
    
    private TableStructureBean tableStructureModule;
    
    private TableStructureBean tableStructureModuleRole;
    
    private TableStructureBean tableStructureRole;
    
    private TableStructureBean tableStructureSubModule;
    
    private String[] columnNames = null ;
    
    private Vector vectModule = null;
    
    private Vector vectRole = null;
    
    private Vector vectSubModule = null;
    
    private Vector vecDeletedRows = new Vector();
    
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    
    private DataBean accDataBean;
    
    private ExtendedDefaultTableModel codeTableModel;
    
    private int numColumnsToDisplay = 0;
    
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    
    private DefaultTableColumnModel displayCodeTableColumnModel;
    
    private String roleModuleCode = "";
    
    private boolean saveRequired = false;
    
    private boolean cancelRequired = false;
    
    private static final String DESCRIPTION = "DESCRIPTION";
    
    private static final char GET_ROLE_LIST = 'R';
    
    private String userId;

    private final String connectTo =CoeusGuiConstants.CONNECTION_URL + CODE_TABLE_SERVLET ;
    
    /** Creates a new instance of FormRoleModule */
    public FormRoleModule(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights) {
            formatFields();
        }
        cmbModule.setFont(CoeusFontFactory.getLabelFont());
        cmbModule.setSelectedItem("1");
    }
    
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        lblModule = new JLabel("Module Code");
        lblRole = new JLabel("Role Code");
        lblModule.setFont(CoeusFontFactory.getLabelFont());
        lblRole.setFont(CoeusFontFactory.getLabelFont());
        
        pnlRole = new JPanel(new GridBagLayout());
        pnlModule = new JPanel(new GridBagLayout());
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 0, 2, 1);
        pnlModule.add(lblModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 5, 2, 1);
        pnlModule.add(cmbModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(4, 10, 2, 1);
        pnlModule.add(lblRole, gridBagConstraints) ;
        
        initialiseData();
        
        sorter = new TableSorterCodeTable() ;
        tblModuleRole = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblModuleRole);
        
        tblModuleRole.setRowHeight(22);
        tblModuleRole.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
                = tblModuleRole.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblModuleRole.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblModuleRole, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlModule, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
        cmbModule.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    getLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbModule.getSelectedItem() ;
                    String newRoleModuleCode = cmb.getCode();
                    if (roleModuleCode != newRoleModuleCode && saveRequired) {
                        String msg = coeusMessageResources.parseMessageKey(
                                "saveConfirmCode.1002");
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                CoeusOptionPane.DEFAULT_YES);
                        if(confirm == JOptionPane.YES_OPTION) {
                            if (!saveData()) {
                                //There are errors to be corrected before leaving this Mail Recipient Module.
                                cancelRequired = true;
                                cmbModule.setSelectedItem(roleModuleCode) ;
                                return;
                            }
                        }else if (confirm == JOptionPane.CANCEL_OPTION) {
                            cancelRequired = true;
                            cmbModule.setSelectedItem(roleModuleCode) ;
                            return;
                        }
                        
                    }
                    if (cancelRequired) {
                        cancelRequired = false;
                        return;
                    }
                    saveRequired = false;
                    roleModuleCode = newRoleModuleCode ;
                    if (roleModuleCode.equals("")) {
                        // hide the table
                        tblModuleRole.setVisible(false) ;
                    } else {
                        //Vector vecTable =
                        drawTableUsingTableStructureBean() ;
                    }
                }
            }
        });
        
        pnlForBtn = new JPanel();
        pnlForBtn.setLayout(new GridBagLayout());
        btnAdd = new JButton("Add") ;
        btnSave = new JButton("Save") ;
        btnDelete = new JButton("Delete") ;
        btnClose = new JButton("Close") ;
        
        btnAdd.setMnemonic('A');
        btnSave.setMnemonic('S');
        btnDelete.setMnemonic('D');
        btnClose.setMnemonic('C');
        
        btnAdd.setPreferredSize(new java.awt.Dimension(80, 25));
        btnSave.setPreferredSize(new java.awt.Dimension(80, 25));
        btnDelete.setPreferredSize(new java.awt.Dimension(80, 25));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 25));
        
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnSave.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setFont(CoeusFontFactory.getLabelFont());
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 1, 2, 0);
        pnlForBtn.add(btnAdd,gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(2, 1, 2, 0);
        pnlForBtn.add(btnDelete,gridBagConstraints) ;
        
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(2, 1, 2, 0);
        pnlForBtn.add(btnSave,gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(2, 1, 2, 0);
        pnlForBtn.add(btnClose,gridBagConstraints) ;
        
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCloseActionPerformed();
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        pnlRole.add(splitpnlMain, gridBagConstraints) ;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlRole.add(pnlForBtn, gridBagConstraints) ;
        
        getContentPane().add(pnlRole, BorderLayout.CENTER);
        pack() ;
        
        // this will take care of the window closing...
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                btnCloseActionPerformed() ;
            }
        } );
    }
    
    private void formatFields() {
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    
    public void btnAddActionPerformed(ActionEvent evt) {
        Object[]  oneRow = {""};
        
        sorter.insertRow(sorter.getRowCount(),oneRow);
        codeTableModel.setValueAt(roleModuleCode, codeTableModel.getRowCount()-1, 2);
        // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1,
                tableStructureModuleRole.getDuplicateIndex(0) );
        //set the submodule code
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, 0);
        // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1,
                codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1,
                tableStructureModuleRole.getUserIndex() );
        tblModuleRole.requestFocus();
    }
    
    public void btnSaveActionPerformed(ActionEvent evt) {
        int  selectedRow = tblModuleRole.getSelectedRow();
        getLastValue();
        Object temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        //return if failed to save.
        if (!saveData()) {
            return;
        }
        drawTableUsingTableStructureBean();
        if (sorted) {
            sorter.sortByColumn(sorteColumn,asc);
        }
        
        if (selectedRow >= 0) {
            for (int i = 0; i < tblModuleRole.getRowCount(); i++) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString())) {
                    tblModuleRole.setRowSelectionInterval(i,i);
                    break;
                } else {
                    if ( i == tblModuleRole.getRowCount() -1) {
                        tblModuleRole.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    public void btnDeleteActionPerformed(ActionEvent evt) {
        boolean canDelete = true;
        int rowNum = tblModuleRole.getSelectedRow();
        if (rowNum == -1) {
            return;
        }
        
        String roleCode = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), 0).toString();
        try {
            canDelete = checkDeleteValidity(roleCode);
        }catch(CoeusClientException ce) {
            ce.printStackTrace();
        }catch(CoeusException ce) {
            ce.printStackTrace();
        }
        
        if(!canDelete) {
            CoeusOptionPane.showInfoDialog("This role is in use, you can not delete");
            return;
        }
        
        if ( tblModuleRole.getEditingColumn() >= 0 ) {
            tblModuleRole.getCellEditor().stopCellEditing();
        }
        
        //here need to check dependency first
//        if (!checkDependency(rowNum, "Delete")) {
//            return;
//        }
        String msg = coeusMessageResources.parseMessageKey(
                "generalDelConfirm_exceptionCode.2100");
        int confirm = CoeusOptionPane.showQuestionDialog(
                msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        if (confirm == JOptionPane.NO_OPTION) {
            return;
        }
        //keep all deleted rows in vecDeletedRows.
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),codeTableModel.getColumnCount()-1) != "I") {            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModel.setValueAt("D", sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1);
            saveRequired = true;
            //save to vecDeletedRows with hash of vec
            vecDeletedRows.add(getTableRow(rowNum)) ;
        }//end if
        
        sorter.removeRow(rowNum);
        
        if ( rowNum != 0 ) {
            rowNum--;
        } else {
            tblModuleRole.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ;
        }
        
        if (tblModuleRole.getRowCount() > 0  ) {
            tblModuleRole.changeSelection(rowNum, 0, false, false) ;
        }
    }
    
    private boolean checkDeleteValidity(String roleCode) throws CoeusClientException, CoeusException {
        boolean canDelete = true;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htRoleListData = null;
        
        requesterBean.setFunctionType(GET_ROLE_LIST);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+"/personRoleServlet", requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htRoleListData = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        CoeusVector cvRoleData = (CoeusVector)htRoleListData.get(PersonRoleInfoBean.class);
        Equals operator = new Equals("roleCode", roleCode);
        Vector vecData = cvRoleData.filter(operator);
        if(vecData != null && vecData.size() > 0) {
            canDelete = false;
        }
        return canDelete;
    }
    
    public void btnCloseActionPerformed() {
        getLastValue();
        if ( saveRequired ) {
            String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            
            switch(confirm) {
                case(JOptionPane.YES_OPTION):
                    try {
                        if (!saveData()) {
                            return;
                        }
                        this.dispose();
                    }catch(Exception ex) {
                        ex.printStackTrace();
                        String exMsg = ex.getMessage();
                        CoeusOptionPane.showWarningDialog(exMsg);
                    }
                case(JOptionPane.NO_OPTION):
                    this.dispose();
            }
            
        } else {
            this.dispose();
        }
    }
    
    private boolean saveData() {
        
        // do the validation b4 u go and build the vector of modified rows
        if (tableDataValid()) {
            // get the modified rows
            Vector vecModifiedRows = getModifiedRows() ;
            if (vecModifiedRows != null) {
                System.out.println("obtd modified rows successfuly") ;
            }
            
            HashMap hashStoredProcedure =
                    (HashMap)tableStructureModuleRole.getHashStoredProceduresForThisTable();
            if(hashStoredProcedure == null) {
                System.out.println("hashStoredProcedure == null");
            }
            //Get the update stored procedure associated witht this table.
            StoredProcedureBean updateStoredProcedure =
                    (StoredProcedureBean)hashStoredProcedure.get(new Integer(1));
            
            RequestTxnBean requestTxnBean = new RequestTxnBean();
            requestTxnBean.setAction("MODIFY_DATA");
            requestTxnBean.setStoredProcedureBean(updateStoredProcedure);
            requestTxnBean.setRowsToModify(vecModifiedRows) ;
            
            // the servlet will return if the saving process was successful or not
            Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
            if (success == null) // Error while saving data
            {
                String msg = coeusMessageResources.parseMessageKey(
                        "saveFail_exceptionCode.1102");
                
                CoeusOptionPane.showInfoDialog(msg);
                return false;
            } else {//Data Saved Successfully
                saveRequired = false;
                return true;
            }
            
        }// end if data validation
        else {
            return false;
        }
    }
    
    // this function should do the validation of rows
    private boolean tableDataValid() { // prps check
        // if there are any form level validations that should go there
        int rowTotal = tblModuleRole.getRowCount();
        int colTotal = tblModuleRole.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++) {
            for (int colIndex = 0; colIndex < colTotal; colIndex++) {
                TableColumn column = codeTableColumnModel.getColumn(colIndex) ;
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                && !columnBean.getColumnCanBeNull()) {
                    if ( tblModuleRole.getValueAt(rowIndex,colIndex) != null) {
                        if (tblModuleRole.getValueAt(rowIndex,colIndex).equals("")
                        || tblModuleRole.getValueAt(rowIndex,colIndex).toString().trim().equals("") ) {
                            String msg = coeusMessageResources.parseMessageKey(
                                    "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblModuleRole.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    } else {
                        String msg = coeusMessageResources.parseMessageKey(
                                "checkInputValue_exceptionCode.2402");
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblModuleRole.changeSelection(rowIndex,  colIndex, false, false) ;
                        return false;
                    }
                }
            }
        }
        return true ;
    }
    
    // this method will check the AC_Type field of all the rows in the table and build a vector of modified
    // rows. modified rows include newly inserted rows as well
    private Vector getModifiedRows() {
        Vector vecUpdatedRows = new Vector();
        int rowCount = 0;
        
        if (vecDeletedRows.size() > 0 ) {//append deleted rows to vecUpdatedRows
            for (int row = 0; row < vecDeletedRows.size(); row++) {
                vecUpdatedRows.add(vecDeletedRows.get(row));
            }
            
        }
        
        while(rowCount < tblModuleRole.getRowCount()) {// check AC_TYPE field
            //String tmpACType ;
            if (codeTableModel.getValueAt(sorter.getIndexForRow(rowCount), codeTableModel.getColumnCount()-1) != null) {
                vecUpdatedRows.add(getTableRow(rowCount)) ;
            }
            rowCount++ ;
        }//end while
        
        
        return vecUpdatedRows ;
    }
    
    //copy one table row to a hashmap
    private HashMap getTableRow(int rowNum) {
        HashMap hashRow = new HashMap() ;
        //int colTotal = codeTableModel.getColumnCount()-1;
        for (int colCount=0; colCount <= codeTableModel.getColumnCount()-1; colCount++) { // for each column u will build a hashmap
            TableColumn column = codeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            Object val = null ;
            // the whole idea of setting up the columnBean as the identifier is that u get to know
            // all abt the structure details of the column, so u can create appropriate type of object
            // and stick it in to the hashMap
            // the whole idea of setting up the columnBean as the identifier is that u get to know
            // all abt the structure details of the column, so u can create appropriate type of object
            // and stick it in to the hashMap
            if (columnBean.getDataType().equals("NUMBER")) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) == null
                        ||(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).equals(""))) {
                    val = new Integer(0);
                } else {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount);
                    if (val instanceof ComboBoxBean) {
                        if (((ComboBoxBean)val).getCode().equals(""))// user deleted selection
                            val = null;
                        else
                            val = new Integer(((ComboBoxBean)val).getCode().toString());
                    } else {
                        val = new Integer(val.toString()) ;
                    }
                }
            }
            
            if (columnBean.getDataType().indexOf("VARCHAR2") != -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount)==null) {
                    val = "";
                } else {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString() ;
                }
            }
            
            if (columnBean.getDataType().equals("DATE")) {
                //coeusdev-568 start
                //Date today = new Date();
                //coeusdev-568 end
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),
                        codeTableModel.getColumnCount()-1).equals("I")) // if itz an insert
                {   //AV_ & AW_ will be the same for insert
                    //coeusdev-568 start
                    //val = new java.sql.Timestamp(today.getTime());
                    val = CoeusUtils.getDBTimeStamp();
                    //coeusdev-568 end
                } else {  // for update
                    // there will be only two dates in any table one AV_UPDATE_TIMESTAMP and the other one AW_UPDATE_TIMESTAMP
                    if (columnBean.getQualifier().equals("VALUE")) {  //AV_...
                       //coeusdev-568 start
                       //val = new java.sql.Timestamp(today.getTime());
                       val = CoeusUtils.getDBTimeStamp();
                       //coeusdev-568 end
                    } else {  //AW_...
                        val =  java.sql.Timestamp.valueOf(codeTableModel.getValueAt
                                (sorter.getIndexForRow(rowNum),colCount).toString()) ;
                    }
                }
            }
            
            hashRow.put(columnBean.getColIdentifier(), val) ;
        } //end for
        return hashRow;
    }
    
    private Object getDataFromServlet(Object request) {
        Object response = null ;
        try {
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(request) ;
            
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.hasResponse()) {
                response = responder.getDataObject() ;
            }
            
        }catch(Exception ex) {
            System.out.println("  *** Error in Applet Servlet communication ***  ") ;
            ex.printStackTrace() ;
        }
        
        return response ;
    }
    
    public void initialiseData() {
        hashAllCodeTableStructure = allCodeTablesBean.getHashAllCodeTableStructure();
        TableStructureBean tempTableStructBean = null;
        
        for(int tableCount = 0; tableCount < hashAllCodeTableStructure.size(); tableCount++) {
            tempTableStructBean = (TableStructureBean) hashAllCodeTableStructure.get(new Integer(tableCount));
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$coeus_modules")) {
                tableStructureModule = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$person_role_module")) {
                tableStructureModuleRole = tempTableStructBean ;
            }
            
            if(tempTableStructBean.getActualName().equalsIgnoreCase("osp$person_role_type")) {
                tableStructureRole = tempTableStructBean;
            }
            
            if(tempTableStructBean.getActualName().equalsIgnoreCase("osp$coeus_sub_module")) {
                tableStructureSubModule = tempTableStructBean;
            }
        }
        
        vectModule = getDataForCombo(tableStructureModule);
        vectRole = getDataForCombo(tableStructureRole);
        HashMap hmRow = new HashMap();
        cmbModule.addItem(new ComboBoxBean("", "")) ;
        for (int i=0; i < vectModule.size(); i++) //loop for num of rows
        {
            hmRow = (HashMap)vectModule.elementAt(i);
            cmbModule.addItem(new ComboBoxBean
                    (hmRow.get("MODULE_CODE").toString() ,
                    hmRow.get(DESCRIPTION).toString()));
        }//end for
    }
    
    public Vector drawTableUsingTableStructureBean() {
        vecDeletedRows.clear();
        codeTableModel = new ExtendedDefaultTableModel() ;
        
        //hold all data of one table in vdata.
        Vector vecData = new Vector();
        HashMap hmTableColumnBean = tableStructureModuleRole.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsToDisplay = tableStructureModuleRole.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNames = new String[numColumnsToDisplay + 1];
        // create a table colunmodel thing can used to access Table Column directly
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        
        if(numColumnsToDisplay > 0) {
            for(int index = 0; index < numColumnsToDisplay; index++) {
                //to add columns
                ColumnBean newColumnBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                TableColumn newColumn = new TableColumn(index, newColumnBean.getDisplaySize()) ; // set the model index and width
                newColumn.setIdentifier(newColumnBean) ; // set the column bean itself as identifier
                newColumn.setHeaderValue(newColumnBean.getDisplayName()) ;
                
                if(index == 0) {
                    HashMap hmRow = new HashMap();
                    CoeusComboBox cmbSubModule = new CoeusComboBox() ;
                    getDataForTheTable(tableStructureSubModule, "osp$coeus_sub_module", "AW_MODULE_CODE");
                    vectSubModule = accDataBean.getVectData();
                    if (vectSubModule == null) {
                        continue;
                    }
                    cmbSubModule.addItem(new ComboBoxBean("0", "")) ;
                    
                    for (int j=0; j < vectSubModule.size(); j++) //loop for num of rows
                    {
                        hmRow = (HashMap)vectSubModule.elementAt(j);
                        cmbSubModule.addItem
                                (new ComboBoxBean(hmRow.get("SUB_MODULE_CODE").toString() ,
                                hmRow.get(DESCRIPTION).toString()));
                    }//end for
                    
                    newColumn.setCellEditor(new ExtendedCellEditor(cmbSubModule)) ;
                } else if(index == 1) {
                    HashMap hmRow = new HashMap();
                    CoeusComboBox cmbRole = new CoeusComboBox() ;
                    if (vectRole == null) {
                        continue;
                    }
                    cmbRole.addItem(new ComboBoxBean("", "")) ;
                    
                    for (int j=0; j < vectRole.size(); j++) //loop for num of rows
                    {
                        hmRow = (HashMap)vectRole.elementAt(j);
                        cmbRole.addItem
                                (new ComboBoxBean(hmRow.get("ROLE_TYPE_CODE").toString() ,
                                hmRow.get(DESCRIPTION).toString()));
                    }//end for
                    
                    newColumn.setCellEditor(new ExtendedCellEditor(cmbRole)) ;
                } else {
                    newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean)) ;
                }
                if (newColumnBean.getDisplaySize()<=5) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                } else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                } else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                } else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2);
                } else {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                
                newColumn.setMinWidth(10) ;
                
                // add the new columnto the column model also
                codeTableColumnModel.addColumn(newColumn) ;
                displayCodeTableColumnModel.addColumn(newColumn) ;
                
                // add it to the tablemodel
                codeTableModel.addColumn(newColumn);
                
                columnNames[index] = ((ColumnBean)hmTableColumnBean.get(new Integer(index))).getColumnName();
            }
        }
        
        getDataForTheTable(tableStructureModuleRole, "osp$person_role_module", "AW_ROLE_MODULE_CODE");
        vecData = accDataBean.getVectData();
        HashMap hashRow = new HashMap();
        
        if(vecData != null && vecData.size() > 0) {
            for(int index = 0; index < vecData.size(); index++) {
                hashRow = (HashMap)vecData.elementAt(index);
                Object [] rowColumnDatas = new Object[numColumnsToDisplay];
                
                for(int j = 0; j < numColumnsToDisplay; j++) {
                    rowColumnDatas[j] = (Object)hashRow.get(columnNames[j]);
                    TableColumn column = codeTableColumnModel.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                    
                    if(j == 0) {
                        if(vectSubModule != null) {
                            if (rowColumnDatas[j] != null) {
                                String temp = rowColumnDatas[j].toString();
                                if(temp == "0") {
                                    rowColumnDatas[j] = new ComboBoxBean("", "");
                                }
                                /* get the code for the selected description for combobox */
                                for(int codeIndex = 0; codeIndex < vectSubModule.size(); codeIndex++) {
                                    HashMap hmRow = (HashMap) vectSubModule.elementAt(codeIndex);
                                    if (hmRow.get("SUB_MODULE_CODE").toString().equals(temp)) {
                                        rowColumnDatas[j] = new ComboBoxBean
                                                (hmRow.get("SUB_MODULE_CODE").toString() ,
                                                hmRow.get(DESCRIPTION).toString());
                                        break;
                                    }
                                }
                            }
                        }
                    } else if(j == 1) {
                        if(vectRole != null) {
                            if (rowColumnDatas[j] != null) {
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex = 0; codeIndex < vectRole.size(); codeIndex++) {
                                    HashMap hmRow = (HashMap) vectRole.elementAt(codeIndex);
                                    if (hmRow.get("ROLE_TYPE_CODE").toString().equals(temp)) {
                                        rowColumnDatas[j] = new ComboBoxBean
                                                (hmRow.get("ROLE_TYPE_CODE").toString() ,
                                                hmRow.get(DESCRIPTION).toString());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    //Object obj = rowColumnDatas[j];
                    if (columnBean.getColumnVisible()) {
                        if (rowColumnDatas[j] !=null) {
                            if (rowColumnDatas[j].equals("null")) {
                                rowColumnDatas[j] = "";
                            }
                        } else {
                            rowColumnDatas[j] = null ;
                        }
                    }
                }
                codeTableModel.addRow(rowColumnDatas);
            }
        }
        sorter.setModel(codeTableModel, false);
        tblModuleRole.setModel(sorter);
        
        
        tblModuleRole.setColumnModel(displayCodeTableColumnModel); // also give the structure
        
        // hide unwanted columns
        int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
        for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--) {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            
            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblModuleRole.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblModuleRole.removeColumn(inColumn );
            }
        }
        if (tblModuleRole.getRowCount() > 0) {
            tblModuleRole.setRowSelectionInterval(0,0) ;
            tblModuleRole.repaint() ;
        }
        
        return vecData;
    }
    
    private void getDataForTheTable(TableStructureBean tableStructureBean, String tableName, String moduleId) {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        try {
            Vector vectData = null;
            if(tableStructureModuleRole == null) {
                System.out.println("tableStructureModuleRecipient == null");
            }
            //Get all stored procedures associated with this table.
            HashMap hashStoredProcedure =
                    (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
            if(hashStoredProcedure == null) {
                System.out.println("hashStoredProcedure for ProtoActionAction == null");
            }
            //Get the select stored procedure associated witht this table.
            StoredProcedureBean selectStoredProcedure =
                    (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));

            RequestTxnBean requestTxnBean = new RequestTxnBean();
            requestTxnBean.setAction("GET_DATA");
            requestTxnBean.setStoredProcedureBean(selectStoredProcedure);

            Vector param= new Vector();
            param.add(tableName);
            param.add(moduleId);
            param.add(roleModuleCode);

            requestTxnBean.setSelectProcParameters(param) ;

            RequesterBean requester = new RequesterBean();
            requester.setDataObject(requestTxnBean) ;

            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();

            userId = requester.getUserName();

            ResponderBean responder = comm.getResponse();
            if (responder.hasResponse()) {
                accDataBean = (DataBean)responder.getDataObject() ;
            }

            if(accDataBean != null) {
                System.out.println("*** Recvd a DataBean ***");
                vectData = accDataBean.getVectData();
                System.out.println("vectData.size() in GUI: "+vectData.size());
            }

            accDataBean.setVectData(vectData);
        }catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public Vector getDataForCombo(TableStructureBean tableStructureBean) {
        Vector vectData = null;
        
        try {
            if(tableStructureBean != null) {
                //Get all stored procedures associated with this table.
                HashMap hashStoredProcedure =
                        (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
                if(hashStoredProcedure != null) {
                    //Get the select stored procedure associated witht this table.
                    StoredProcedureBean selectStoredProcedure =
                            (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
                    
                    RequestTxnBean requestTxnBean = new RequestTxnBean();
                    requestTxnBean.setAction("GET_DATA");
                    requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
                    
                    RequesterBean requester = new RequesterBean();
                    requester.setDataObject(requestTxnBean) ;
                    
                    AppletServletCommunicator comm
                            = new AppletServletCommunicator(connectTo,requester);
                    comm.send();
                    
                    ResponderBean responder = comm.getResponse();
                    
                    if (responder.hasResponse()) {
                        accDataBean = (DataBean)responder.getDataObject() ;
                    }
                    
                    if(accDataBean != null) {
                        vectData = accDataBean.getVectData();
                    }
                }
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return vectData;
    }
    
    class ExtendedDefaultTableModel extends DefaultTableModel {
        
        public ExtendedDefaultTableModel() {
            
        }
        
        public boolean isCellEditable(int row, int col) {
            if (!userHasRights){
                return false;
            }else{
                TableColumn column = codeTableColumnModel.getColumn(col) ;
                ColumnBean columnBean = (ColumnBean) column.getIdentifier();
                return columnBean.getColumnEditable() ;
            }
            
        }
        
        
    }// end inner class
    
    class ExtendedCellEditor extends DefaultCellEditor implements TableCellEditor {
        
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        public ExtendedCellEditor(CoeusComboBox comb) {
            super(comb);
            comb.addFocusListener(new FocusAdapter() {
                
                public void focusGained(FocusEvent e) {
                    // System.out.println("*** combo box focus Gained ***") ;
                }
                
                public void focusLost(FocusEvent fe) {
                    //System.out.println("***combo box focus Lost ***");
                    if (!fe.isTemporary()) {
                        getLastValue();
                    }
                }
            });
        }
        public ExtendedCellEditor(ColumnBean columnBeanIn) {
            super( new CoeusTextField() );
            
            if (columnBeanIn.getDataType().equals("NUMBER")) {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,columnBeanIn.getDisplaySize( )));
            } else {
                
                if (columnBeanIn.getDataType().equals("FNUMBER")) {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC + ". -",columnBeanIn.getDisplaySize( )));
                } else {
                    if (columnBeanIn.getDataType().equals("UVARCHAR2")) {
                        txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, columnBeanIn.getDisplaySize( )));
                    } else {
                        if (columnBeanIn.getDataType().equals("MVARCHAR2"))//upcpercase with "." and "number"
                        {
                            txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE + ". -" + JTextFieldFilter.NUMERIC, columnBeanIn.getDisplaySize( )));
                        } else {
                            txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));
                        }
                    }
                }
            }
            txtCell.addFocusListener(new FocusAdapter() {
                
                public void focusGained(FocusEvent e) {
                    // System.out.println("*** focus Gained ***") ;
                }
                
                public void focusLost(FocusEvent fe) {
                    if (!fe.isTemporary()) {
                        System.out.println("*** focus lost ***") ;
                        getLastValue();
                    }// end if
                }// end focus lost
            }); // end focus listener
        }
        
        /**
         * This method is overloaded to get the selected cell component in the
         * table.
         * @param table JTable instance for which component is derived
         * @param value object value.
         * @param isSelected particular table cell is selected or not
         * @param row row index
         * @param colunn column index
         * @return Component
         */
        
        public Component getTableCellEditorComponent(JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {
            selRow = row ;
            selColumn = column ;
            
            if (column == 0 || column == 1)//SubModule / Role Type Code
            {
                if (value  instanceof ComboBoxBean) {
                    ComboBoxBean cmbTmp = (ComboBoxBean)value ;
                    Object objTmp = cmbTmp.getCode() ;
                    CoeusComboBox CompTmp = (CoeusComboBox)getComponent();
                    CompTmp.setSelectedItem(objTmp);
                    return (Component)CompTmp;
                    
                } else {
                    return (Component)((CoeusComboBox)getComponent());
                }
            }
            
            
            if (value != null) {
                txtCell.setText(value.toString()) ;
            } else {
                txtCell.setText(null) ;
            }
            return txtCell;
        }
        
        public Object getCellEditorValue() {
            Object oldValue = tblModuleRole.getValueAt(selRow,selColumn);
            Object subModule = null;
//            Object roleCode = null;
            if(selColumn == 0 || selColumn == 1) // SubModule Code
            {
                subModule = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
//            if(selColumn == 1) {
//                roleCode = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
//            }
            
            if((subModule != null) && !(triggerValidation(subModule.toString()))) {
                return oldValue;
            }
//            if((roleCode != null) && !(triggerValidation(roleCode.toString()))) {
//                return oldValue;
//            }
            
            if(selRow > -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null) {
                    if (tblModuleRole.getValueAt(selRow,selColumn)!=null) {
                        if (!(tblModuleRole.getValueAt(selRow,selColumn).toString().
                                equals(((CoeusTextField)txtCell).getText()))) {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureModuleRole.getUserIndex() );
                            saveRequired = true;
                        }
                    } else {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow),
                                tableStructureModuleRole.getUserIndex() );
                        saveRequired = true;
                    }
                }
            }
//            if(roleCode != null) {
//                subModule = roleCode;
//            }
            
            return subModule;
        }
        
        /** Supporting method used for validation.  There will be various
         * types of triggerValidation functions which take care of validation
         * for different types of data.
         */
        private boolean triggerValidation(String newValue) {
            
            if (!tableStructureModuleRole.isIdAutoGenerated()) {
                int colIdx1 = -1;
                int colIdx2 = -1;
                int colIdx3 = -1;
                Vector vecPrimaryKey = tableStructureModuleRole.getPrimaryKeyIndex();
                if ( vecPrimaryKey.size() == 1) {
                    colIdx1 = tableStructureModuleRole.getPrimaryKeyIndex(0);
                }else if ( vecPrimaryKey.size() == 2 ) {
                    colIdx1 = tableStructureModuleRole.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureModuleRole.getPrimaryKeyIndex(1);
                }else if ( vecPrimaryKey.size() == 3 ) {
                    colIdx1 = tableStructureModuleRole.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureModuleRole.getPrimaryKeyIndex(1);
                    colIdx3 = tableStructureModuleRole.getPrimaryKeyIndex(2);
                }
                if (selColumn == colIdx1 || selColumn == colIdx2 || selColumn == colIdx3) {
//                    if (checkDependency(selRow, "")) {
                    if(!checkUniqueId(newValue, selColumn, selRow)) {
                        String msg = coeusMessageResources.parseMessageKey(
                                "protocolFollowupAction_PKeyUniq_exceptionCode.2409");
                        
                        CoeusOptionPane.showInfoDialog(msg);
                        return false; //fail in uniqueid check
                    }
//                    } else {
//                        return false;//fail in dependency check
//                    }
                }
            }
            return true;
        }// end method
    }
    
    //Although there are two primary keys, the first primary key is module code.
    //Module code value will be the same for all the rows on a given screen,
    //and is an invisible column.
    private boolean checkUniqueId(String newVal, int selCol, int selRow) {
        int rowTotal = tblModuleRole.getRowCount();
        int row = tblModuleRole.getEditingRow();
        if (rowTotal > 0 && row >= 0) {
            int colIndx1 = -1;
            int colIndx2 = -1;
            if(selCol == tableStructureModuleRole.getPrimaryKeyIndex(0)) {
                colIndx1 = tableStructureModuleRole.getPrimaryKeyIndex(0);
                colIndx2 = tableStructureModuleRole.getPrimaryKeyIndex(1);
            }
            if(selCol == tableStructureModuleRole.getPrimaryKeyIndex(1)) {
                colIndx1 = tableStructureModuleRole.getPrimaryKeyIndex(1);
                colIndx2 = tableStructureModuleRole.getPrimaryKeyIndex(0);
            }
            Object valTemp1 = tblModuleRole.getValueAt(selRow, colIndx2);
            
            if(valTemp1 instanceof ComboBoxBean) {
                ComboBoxBean cmbTemp = (ComboBoxBean) valTemp1;
                valTemp1 = cmbTemp.getCode();
            }
            
            for (int index = 0; index < rowTotal ; index++) {
                if (index != row) {
                    //Object val1 = tblModuleRole.getValueAt(selRow, colIndx1);
                    //Object val2 = tblModuleRole.getValueAt(selRow, colIndx2);
                    Object val1 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIndx1);
                    Object val2 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIndx2);
                    
                    if(val1 instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val1;
                        val1 = cmbTmp.getDescription();
                    }
                    
                    if(val2 instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val2;
                        val2 = cmbTmp.getCode();
                    }
                    
                    if(val1 != null && val2 != null && valTemp1 != null) {
                        if(newVal.equals(val1.toString()) &&
                                val2.toString().equals(valTemp1.toString())) {
                            return false;
                        }
                    }
//                    Object val = codeTableModel.getValueAt(sorter.getIndexForRow(index), selCol);
//                    if (val instanceof ComboBoxBean) {
//                        ComboBoxBean cmbTmp = (ComboBoxBean)val ;
//                        //                        val = cmbTmp.getCode() ;
//                        val = cmbTmp.getDescription();
//                    }
//
//                    if (val != null) {
//                        if (newVal.equals(val)) {
//                            return false;
//                        }
//                    }
                }
            }
        }
        return true;
    }
    
    private void getLastValue() {
        int row = tblModuleRole.getEditingRow();
        int col = tblModuleRole.getEditingColumn();
        if (row != -1 && col != -1) {
            tblModuleRole.getCellEditor(row, col).stopCellEditing();
            TableColumn column = codeTableColumnModel.getColumn(col+1) ;
            //make sure to set AC_TYPE
            column.getCellEditor().getCellEditorValue();
        }
        
    }
}
