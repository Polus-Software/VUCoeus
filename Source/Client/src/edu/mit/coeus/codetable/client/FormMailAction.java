/*
 * FormMailAction.java
 *
 * Created on May 8, 2007, 4:55 PM
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
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow ;
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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class FormMailAction extends CoeusDlgWindow {
    
    private javax.swing.JButton btnAdd;
    
    private javax.swing.JTable tblMailAction;
    
    private javax.swing.JButton btnDelete;
    
    private javax.swing.JButton btnClose;
    
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    
    private javax.swing.JPanel pnlForBtn;
    
    private javax.swing.JPanel pnlActionCreation;
    
    private javax.swing.JPanel pnlMailAction;
    
    private javax.swing.JSplitPane splitpnlMain;
    
    private javax.swing.JButton btnSave;
    
    private JLabel lblAction;
    
    private JLabel lblModule;
    
    private GridBagConstraints gridBagConstraints;
    
    private CoeusMessageResources coeusMessageResources;
    
    private boolean saveRequired = false;
    
    private TableSorterCodeTable sorter ;
    
    ExtendedDefaultTableModel codeTableModel ; // this model will hold the actual data
    
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    
    AllCodeTablesBean allCodeTablesBean ;
    
    TableStructureBean tableStructureModule;
    
    TableStructureBean tableStructureMailAction;
    
    HashMap hashAllCodeTableStructure = null ;
    
    DataBean accDataBean = new DataBean();
    
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    
    String[] columnNames = null ;
    
    int numColumnsDisplay = 0 ;
    
    Vector vecDeletedRows = new Vector();
    
    Vector vectModule = null;
    
    //hold login user id
    String   userId;
    
    private boolean userHasRights = false;
    
    private static final String DESCRIPTION = "DESCRIPTION";
    
    private static final String MODULE_CODE = "MODULE_CODE";
    
    private static final char GET_MAIL_ACTION_LIST = 'U';
    
    private CoeusComboBox cmbModule = new CoeusComboBox();

    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ CODE_TABLE_SERVLET;
    
    private String moduleCode = "";
    
    private boolean cancelRequired = false;
    
    /** Creates a new instance of FormMailAction */
    public FormMailAction(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights) {
            formatFields();
        }
        cmbModule.setFont(CoeusFontFactory.getLabelFont());
        drawTableUsingTableStructureBean();
    }
    
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        lblModule = new JLabel("Module Code");
        lblAction = new JLabel("Action Type Creation");
        lblAction.setFont(CoeusFontFactory.getLabelFont());
        lblModule.setFont(CoeusFontFactory.getLabelFont());
        
        pnlActionCreation = new JPanel(new GridBagLayout()) ;
        pnlMailAction = new JPanel(new GridBagLayout()) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 0, 2, 1);
        pnlMailAction.add(lblModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 5, 2, 1);
        pnlMailAction.add(cmbModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(4, 10, 2, 1);
        pnlMailAction.add(lblAction, gridBagConstraints) ;
        
        initialiseData() ;
        
        sorter = new TableSorterCodeTable() ;
        tblMailAction = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblMailAction);
        
        tblMailAction.setRowHeight(22);
        tblMailAction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
                = tblMailAction.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
        
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblMailAction.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblMailAction, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlMailAction, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
        cmbModule.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    getLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbModule.getSelectedItem() ;
                    String newModuleCode = cmb.getCode();
                    if (moduleCode != newModuleCode && saveRequired) {
                        String msg = coeusMessageResources.parseMessageKey(
                                "saveConfirmCode.1002");
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                                CoeusOptionPane.DEFAULT_YES);
                        if(confirm == JOptionPane.YES_OPTION) {
                            if (!saveData()) {
                                //There are errors to be corrected before leaving this Mail Recipient Module.
                                cancelRequired = true;
                                cmbModule.setSelectedItem(moduleCode) ;
                                return;
                            }
                        }else if (confirm == JOptionPane.CANCEL_OPTION) {
                            cancelRequired = true;
                            cmbModule.setSelectedItem(moduleCode) ;
                            return;
                        }
                        
                    }
                    if (cancelRequired) {
                        cancelRequired = false;
                        return;
                    }
                    saveRequired = false;
                    moduleCode = newModuleCode ;
                    if (moduleCode.equals("")) {
                        // hide the table
                        tblMailAction.setVisible(false) ;
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
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed();
            }
        });
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed();
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        pnlActionCreation.add(splitpnlMain, gridBagConstraints) ;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlActionCreation.add(pnlForBtn, gridBagConstraints) ;
        
        getContentPane().add(pnlActionCreation, BorderLayout.CENTER);
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
    
    public void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        Object[]  oneRow = {""};
        sorter.insertRow(sorter.getRowCount(),oneRow);
        int maxId = 0;
        if(codeTableModel.getRowCount() > 1) {
            //Get the max action number.
            for(int rowIndex = 0; rowIndex < codeTableModel.getRowCount()-1; rowIndex++) {
                String actionNumberStr = codeTableModel.getValueAt(sorter.getIndexForRow(rowIndex), 0).toString();
                Integer actionNumber = new Integer(actionNumberStr);
                int tempActionNumber = actionNumber.intValue();
                if(tempActionNumber > maxId) {
                    maxId = tempActionNumber;
                }
            }
        }
        Integer newActionNumber = new Integer(maxId +1);
        codeTableModel.setValueAt(newActionNumber, codeTableModel.getRowCount()-1, 0);
        // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1,
                tableStructureMailAction.getDuplicateIndex(0) );
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, 1 );
        // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1,
                codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1,
                tableStructureMailAction.getUserIndex() );
        tblMailAction.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
        tblMailAction.editCellAt(codeTableModel.getRowCount()-1,  0) ;
        tblMailAction.requestFocus();
    }
    
    public void btnSaveActionPerformed() {
        int  selectedRow = tblMailAction.getSelectedRow();
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
            for (int i = 0; i < tblMailAction.getRowCount(); i++) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString())) {
                    tblMailAction.setRowSelectionInterval(i,i);
                    break;
                } else {
                    if ( i == tblMailAction.getRowCount() -1) {
                        tblMailAction.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        boolean canDelete = true;
        int rowNum = tblMailAction.getSelectedRow();
        if (rowNum == -1) {
            return;
        }
        
        String actionCode = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), 0).toString();
        try {
            canDelete = checkDeleteValidity(actionCode);
        }catch(CoeusClientException ce) {
            CoeusOptionPane.showDialog(ce);
        }catch(CoeusException ce) {
            ce.printStackTrace();
        }
        
        if(!canDelete) {
            String msg = coeusMessageResources.parseMessageKey("mailActionExceptionCode.2001");
            CoeusOptionPane.showInfoDialog(msg);
            return;
        }
        
        if ( tblMailAction.getEditingColumn() >= 0 ) {
            tblMailAction.getCellEditor().stopCellEditing();
        }
        
        String msg = coeusMessageResources.parseMessageKey(
                "generalDelConfirm_exceptionCode.2100");
        int confirm = CoeusOptionPane.showQuestionDialog(
                msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        if (confirm == JOptionPane.NO_OPTION){
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
            tblMailAction.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ;
        }
        
        if (tblMailAction.getRowCount() > 0  ) {
            tblMailAction.changeSelection(rowNum,  1, false, false) ;
        }
    }
    
    private boolean checkDeleteValidity(String actionCode) throws CoeusClientException, CoeusException {
        boolean canDelete = true;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        Hashtable htMailListData = null;
        
        requesterBean.setFunctionType(GET_MAIL_ACTION_LIST);
        AppletServletCommunicator comm
        = new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+"/personRoleServlet", requesterBean);
        
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean!= null){
            if(responderBean.hasResponse()){
                htMailListData = (Hashtable)responderBean.getDataObject();
            }else{
                throw new CoeusClientException(responderBean.getMessage(),CoeusClientException.ERROR_MESSAGE);
            }
        }
        CoeusVector cvMailActionData = (CoeusVector)htMailListData.get(PersonRoleInfoBean.class);
        if(cvMailActionData != null && cvMailActionData.size() > 0) {
            PersonRoleInfoBean personInfoBean = null;
            for(int index = 0; index < cvMailActionData.size(); index++) {
                personInfoBean = (PersonRoleInfoBean) cvMailActionData.get(index);
                if(actionCode != null && actionCode.equals(personInfoBean.getActionCode())) {
                    canDelete = false;
                }
            }
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
                    (HashMap)tableStructureMailAction.getHashStoredProceduresForThisTable();
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
        int rowTotal = tblMailAction.getRowCount();
        int colTotal = tblMailAction.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++) {
            for (int colIndex = 0; colIndex < colTotal; colIndex++) {
                TableColumn column = codeTableColumnModel.getColumn(colIndex + 1) ;//becasue protocol action is first column
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                && !columnBean.getColumnCanBeNull()) {
                    if ( tblMailAction.getValueAt(rowIndex,colIndex) != null) {
                        if (tblMailAction.getValueAt(rowIndex,colIndex).equals("")
                        || tblMailAction.getValueAt(rowIndex,colIndex).toString().trim().equals("") ) {
                            String msg = coeusMessageResources.parseMessageKey(
                                    "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblMailAction.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    } else {
                        String msg = coeusMessageResources.parseMessageKey(
                                "checkInputValue_exceptionCode.2402");
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblMailAction.changeSelection(rowIndex,  colIndex, false, false) ;
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
        
        //Delete the rows to be deleted before inserting or updating rows, in order to avoid primary key constraint violation.
        if (vecDeletedRows.size() > 0 ) {//append deleted rows to vecUpdatedRows
            for (int row = 0; row < vecDeletedRows.size(); row++) {
                vecUpdatedRows.add(vecDeletedRows.get(row));
            }
        }
        
        while(rowCount < tblMailAction.getRowCount()) {// check AC_TYPE field
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
                    val = null ; //new Integer(0)
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
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) ;
                    if(val instanceof ComboBoxBean) {
                        if(((ComboBoxBean)val).getCode().equals("")) {
                            val = null;
                        } else {
                            val = ((ComboBoxBean)val).getCode().toString();
                        }
                    } else {
                        val = val.toString();
                    }
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
        hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure() ;
        TableStructureBean tempTableStructBean = null ;
        for (int tableCount =0 ; tableCount < hashAllCodeTableStructure.size() ; tableCount++) {
            tempTableStructBean  = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(tableCount)) ;
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$coeus_modules")) {
                tableStructureModule = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$notif_action_type")) {
                tableStructureMailAction = tempTableStructBean ;
            }
        }// end for
        
        vectModule = getDataForCombo(tableStructureModule);
        HashMap hmRow = new HashMap();
        cmbModule.addItem(new ComboBoxBean("", "")) ;
        for (int i=0; i < vectModule.size(); i++) //loop for num of rows
        {
            hmRow = (HashMap)vectModule.elementAt(i);
            cmbModule.addItem(new ComboBoxBean
                    (hmRow.get(MODULE_CODE).toString() ,
                    hmRow.get(DESCRIPTION).toString()));
        }//end for
    }
    
    private Vector getDataForCombo(TableStructureBean tableStructureBean) {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        Vector vectData = null;
        try {
            if(tableStructureBean == null) {
                System.out.println("tableStructure == null");
            }
            //Get all stored procedures associated with this table.
            HashMap hashStoredProcedure =
                    (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
            if(hashStoredProcedure == null) {
                System.out.println("hashStoredProcedure == null");
            }
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
            
            userId = requester.getUserName();
            
            ResponderBean responder = comm.getResponse();
            if (responder.hasResponse()) {
                accDataBean = (DataBean)responder.getDataObject() ;
            }
            
            if(accDataBean != null) {
                vectData = accDataBean.getVectData();
            }
        } catch(Exception ex) {
            ex.printStackTrace() ;
            
        }
        
        return  vectData ;
    }
    
    public Vector drawTableUsingTableStructureBean() {
        vecDeletedRows.clear();
        codeTableModel = new ExtendedDefaultTableModel();
        Vector vecData = new Vector();
        HashMap hmTableColumnBean = tableStructureMailAction.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsDisplay = tableStructureMailAction.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNames = new String[numColumnsDisplay + 1];
        // create a table colunmodel thing can used to access Table Column directly
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        
        if(numColumnsDisplay > 0) {
            for(int index = 0; index < numColumnsDisplay; index++) {
                //to add columns
                ColumnBean newColumnBean = (ColumnBean)hmTableColumnBean.get(new Integer(index)) ;
                TableColumn newColumn = new TableColumn(index, newColumnBean.getDisplaySize()) ; // set the model index and width
                newColumn.setIdentifier(newColumnBean) ; // set the column bean itself as identifier
                newColumn.setHeaderValue(newColumnBean.getDisplayName()) ;
                
                if(index == 1) {
                    HashMap hmRow = new HashMap();
                    CoeusComboBox cmbModule = new CoeusComboBox();
                    if(vectModule == null) {
                        continue;
                    }
                    cmbModule.addItem(new ComboBoxBean("", ""));
                    
                    for(int j = 0; j < vectModule.size(); j++) {
                        hmRow = (HashMap) vectModule.elementAt(j);
                        cmbModule.addItem
                                (new ComboBoxBean(hmRow.get(MODULE_CODE).toString(),
                                hmRow.get(DESCRIPTION).toString()));
                    }
                    newColumn.setCellEditor(new ExtendedCellEditor(cmbModule)) ;
                } else if(index == 3) {
                    HashMap hashOptions = newColumnBean.getOptions() ;
                    CoeusComboBox cmbUserPrompt = new CoeusComboBox() ;
                    cmbUserPrompt.addItem(new ComboBoxBean("", ""));
                    for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); ) {
                        String strKey =  it.next().toString() ;
                        cmbUserPrompt.addItem(new ComboBoxBean(strKey, hashOptions.get(strKey).toString())) ;
                    }
                    newColumn.setCellEditor(new ExtendedCellEditor(cmbUserPrompt)) ;
                } else {
                    newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean));
                }
                if (newColumnBean.getDisplaySize()<=5) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*40) ;
                } else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*30) ;
                } else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*20) ;
                } else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*12);
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
        
        vecData = getDataForCombo(tableStructureMailAction);
        HashMap hashRow = new HashMap();
        if(vecData != null && vecData.size() > 0) {
            for(int index = 0; index < vecData.size(); index++) {
                hashRow = (HashMap) vecData.elementAt(index);
                Object[] rowColumnData = new Object[numColumnsDisplay];
                
                for(int j = 0; j < numColumnsDisplay; j++) {
                    rowColumnData[j] = (Object)hashRow.get(columnNames[j]);
                    TableColumn column = codeTableColumnModel.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                    
                    if(j == 1) {
                        if(vectModule != null) {
                            if(rowColumnData[j] != null) {
                                String strTemp = rowColumnData[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex = 0; codeIndex < vectModule.size(); codeIndex++) {
                                    HashMap hmRow = (HashMap) vectModule.elementAt(codeIndex);
                                    if (hmRow.get(MODULE_CODE).toString().equals(strTemp)) {
                                        rowColumnData[j] = new ComboBoxBean
                                                (hmRow.get(MODULE_CODE).toString() ,
                                                hmRow.get(DESCRIPTION).toString());
                                        break;
                                    }
                                }
                            }
                        }
                    } else if(j == 3) {
                        HashMap hashOptions = columnBean.getOptions();
                        for(Iterator itr = hashOptions.keySet().iterator(); itr.hasNext(); ) {
                            String strKey = itr.next().toString();
                            if(rowColumnData[j] != null) {
                                if(rowColumnData[j].toString().equals(strKey)) {
                                    rowColumnData[j] = new ComboBoxBean(strKey, hashOptions.get(strKey).toString());
                                }
                            }
                        }
                    }
                    if (columnBean.getColumnVisible()) {
                        if (rowColumnData[j] !=null) {
                            if (rowColumnData[j].equals("null")) {
                                rowColumnData[j] = "";
                            }
                        } else {
                            rowColumnData[j] = null ;
                        }
                    }
                }
                codeTableModel.addRow(rowColumnData);
            }
        }
        
        sorter.setModel(codeTableModel, false);
        tblMailAction.setModel(sorter);
        
        
        tblMailAction.setColumnModel(displayCodeTableColumnModel); // also give the structure
        
        // hide unwanted columns
        int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
        for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--) {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            
            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblMailAction.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblMailAction.removeColumn(inColumn );
            }
        }
        if (tblMailAction.getRowCount() > 0) {
            tblMailAction.setRowSelectionInterval(0,0) ;
            tblMailAction.repaint() ;
        }
        
        return vecData;
    }
    
//    private void getDataForTheTable(TableStructureBean tableStructureBean, String tableName, String moduleId) {
//      /*
//       Send the RequestTxnBean with appropraite parameters and get back the DataBean
//       */
//        Vector vectData = null;
//        if(tableStructureMailAction == null) {
//            System.out.println("tableStructureModuleRecipient == null");
//        }
//        //Get all stored procedures associated with this table.
//        HashMap hashStoredProcedure =
//                (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
//        if(hashStoredProcedure == null) {
//            System.out.println("hashStoredProcedure for ProtoActionAction == null");
//        }
//        //Get the select stored procedure associated witht this table.
//        StoredProcedureBean selectStoredProcedure =
//                (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
//        
//        RequestTxnBean requestTxnBean = new RequestTxnBean();
//        requestTxnBean.setAction("GET_DATA");
//        requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
//        
//        Vector param= new Vector();
//        param.add(tableName);
//        param.add(moduleId);
//        
//        requestTxnBean.setSelectProcParameters(param) ;
//        
//        RequesterBean requester = new RequesterBean();
//        requester.setDataObject(requestTxnBean) ;
//        
//        AppletServletCommunicator comm
//                = new AppletServletCommunicator(connectTo,requester);
//        comm.send();
//        
//        userId = requester.getUserName();
//        
//        ResponderBean responder = comm.getResponse();
//        if (responder.isSuccessfulResponse()) {
//            accDataBean = (DataBean)responder.getDataObject() ;
//        }
//        
//        if(accDataBean != null) {
//            System.out.println("*** Recvd a DataBean ***");
//            vectData = accDataBean.getVectData();
//            System.out.println("vectData.size() in GUI: "+vectData.size());
//        }
//        
//        accDataBean.setVectData(vectData);
//        
//    }
    
    class ExtendedCellEditor extends DefaultCellEditor implements TableCellEditor {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        public ExtendedCellEditor(ColumnBean columnBeanIn) {
            super( new CoeusTextField() );
            
            if (columnBeanIn.getDataType().equals("NUMBER")) {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,columnBeanIn.getDisplaySize( )));
            } else {
                if (columnBeanIn.getDataType().equals("UVARCHAR2")) {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, columnBeanIn.getDisplaySize( )));
                } else {
                    txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));
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
            
            if (column == 1 || column == 3)//Module Code
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
            Object newValue = null;

            if(selColumn == 1 || selColumn == 3) 
            {
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            } else {
                newValue = txtCell.getText();
            }
            
            if(selRow > -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null) {
                    if (tblMailAction.getValueAt(selRow,selColumn)!=null) {
                        if (!(tblMailAction.getValueAt(selRow,selColumn).toString().
                                equals(((CoeusTextField)txtCell).getText()))) {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureMailAction.getUserIndex() );
                            saveRequired = true;
                        }
                    } else {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow),
                                tableStructureMailAction.getUserIndex() );
                        saveRequired = true;
                    }
                }
            }
            return newValue;
        }
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
    
    private void getLastValue() {
        int row = tblMailAction.getEditingRow();
        int col = tblMailAction.getEditingColumn();
        if (row != -1 && col != -1) {
            tblMailAction.getCellEditor(row, col).stopCellEditing();
            TableColumn column = codeTableColumnModel.getColumn(col + 1) ; //here col +1 ,because state code is second col in codeTableColumnModel, is first col in tblMailAction
            //make sure to set AC_TYPE
            column.getCellEditor().getCellEditorValue();
        }
        
    }
}
