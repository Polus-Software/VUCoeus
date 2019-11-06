/*
 * @(#)ProtocolPrintSummary.java 1.0 April 28, 2009, 7:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.codetable.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.beans.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAboutForm ;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.utils.CoeusComboBox;

/**
 *
 * @author satheeshkumarkn
 */

public class FormSpecialReviewUsage extends CoeusDlgWindow {
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblSplReviewUsg;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private CoeusComboBox cmbCoeusModule = new CoeusComboBox();
    private javax.swing.JScrollPane scrlpnlMain;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlSplReviewUsg;
    private javax.swing.JPanel pnlModule;
    private javax.swing.JSplitPane splitpnlMain;
    private javax.swing.JButton btnSave;
    private JLabel lblModule;
    private JLabel lblSplReviewUsg;
    private GridBagConstraints gridBagConstraints;
    private CoeusMessageResources coeusMessageResources;
    private boolean saveRequired = false;
    private boolean cancelRequired = false;
    private TableSorterCodeTable sorter ;
    ExtendedDefaultTableModel codeTableModel ; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tblModuleStructure;
    TableStructureBean tblSplReviewStructure;
    TableStructureBean tblSplReviewTypeStructure;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ;
    int numColumnsDisplay = 0 ;
    String moduleCode = new String();
    Vector vecDeletedRows = new Vector();
    Vector vecModule = null;
    Vector vecSpecialReview = null;
    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    ComboBoxBean awardComboBoxBean = null;
    private static final int SPECIAL_REVIEW_COLUMN = 0;
    private static final int SHOW_IN_LITE_COLUMN = 2;
    
    /** Creates a new instance of FormSpecialReviewUsage */
    public FormSpecialReviewUsage(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights)
            formatFields();
        cmbCoeusModule.setFont(CoeusFontFactory.getLabelFont());
        cmbCoeusModule.setSelectedItem(awardComboBoxBean);
        loadForm();

    }
    
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        lblModule = new JLabel("Module : ");
        lblSplReviewUsg = new JLabel("Special Review Usage");
        lblModule.setFont(CoeusFontFactory.getLabelFont());
        lblSplReviewUsg.setFont(CoeusFontFactory.getLabelFont());
        
        pnlSplReviewUsg = new JPanel(new GridBagLayout()) ;
        //pnlModule = new JPanel(new FlowLayout(SwingConstants.CENTER, 30, 10 ));
        pnlModule = new JPanel(new GridBagLayout()) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 0, 2, 1);
        pnlModule.add(lblModule, gridBagConstraints) ;
        Dimension moduleWisth = new Dimension(200,22);
        cmbCoeusModule.setMaximumSize(moduleWisth);
        cmbCoeusModule.setMinimumSize(moduleWisth);
        cmbCoeusModule.setPreferredSize(moduleWisth);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 5, 2, 1);
        pnlModule.add(cmbCoeusModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(4, 10, 2, 1);
        pnlModule.add(lblSplReviewUsg, gridBagConstraints) ;
        
        initialiseData() ;
        
        sorter = new TableSorterCodeTable() ;
        tblSplReviewUsg = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblSplReviewUsg);
        
        tblSplReviewUsg.setRowHeight(22);
        tblSplReviewUsg.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
                = tblSplReviewUsg.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
        
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblSplReviewUsg.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblSplReviewUsg, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlModule, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
        cmbCoeusModule.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadForm();
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
        pnlSplReviewUsg.add(splitpnlMain, gridBagConstraints) ;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlSplReviewUsg.add(pnlForBtn, gridBagConstraints) ;
        
        getContentPane().add(pnlSplReviewUsg, BorderLayout.CENTER);
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
    
    private void loadForm(){
        GetLastValue();
        ComboBoxBean cmb = (ComboBoxBean) cmbCoeusModule.getSelectedItem() ;
        String newModuleCode = cmb.getCode();
        if (moduleCode != newModuleCode && saveRequired) {
            String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            if(confirm == JOptionPane.YES_OPTION) {
                if (!saveData()) {
                    cancelRequired = true;
                    cmbCoeusModule.setSelectedItem(moduleCode) ;
                    return;
                }
            }else if (confirm == JOptionPane.CANCEL_OPTION) {
                cancelRequired = true;
                cmbCoeusModule.setSelectedItem(moduleCode) ;
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
            tblSplReviewUsg.setVisible(false) ;
        } else {
            Vector vecTable = drawTableUsingTableStructureBean() ;
        }
    }
    
    public void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        Object[]  oneRow = {" "};
        
        sorter.insertRow(sorter.getRowCount(),oneRow);
//        int maxActionInt = 0;
//        if(codeTableModel.getRowCount() > 1) {
//            //Get the max action number.
//            for(int rowIndex = 0; rowIndex < codeTableModel.getRowCount()-1; rowIndex++) {
//                String actionNumberStr = codeTableModel.getValueAt(sorter.getIndexForRow(rowIndex), 1).toString();
//                Integer actionNumber = new Integer(actionNumberStr);
//                int tempActionNumber = actionNumber.intValue();
//                if(tempActionNumber > maxActionInt) {
//                    maxActionInt = tempActionNumber;
//                }
//            }
//        }
 
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1,
                tblSplReviewStructure.getDuplicateIndex(0) );
        codeTableModel.setValueAt("No", codeTableModel.getRowCount()-1, SHOW_IN_LITE_COLUMN );
        
        // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1,
                codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1,
                tblSplReviewStructure.getUserIndex() );
        tblSplReviewUsg.changeSelection(codeTableModel.getRowCount()-1,  SPECIAL_REVIEW_COLUMN, false, false) ;
        tblSplReviewUsg.editCellAt(codeTableModel.getRowCount()-1,  SPECIAL_REVIEW_COLUMN) ;
        tblSplReviewUsg.requestFocus();
    }
    
    public void btnSaveActionPerformed() {
        int  selectedRow = tblSplReviewUsg.getSelectedRow();
        GetLastValue();
        boolean asc = false;
        boolean sorted = false;
        int sorteColumn = -1;
        Object temp  = null;
        if (selectedRow >= 0) {
            temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),SPECIAL_REVIEW_COLUMN);
            sorteColumn = sorter.getSortedColNum();
            asc = sorter.getAscending();
            sorted = sorter.getHasSorted();
        }
            //return if failed to save.
            if (!saveData()) return;
            drawTableUsingTableStructureBean();
            if (selectedRow >= 0) {
                if (sorted) {
                    sorter.sortByColumn(sorteColumn,asc);
                }
            }  
            if (selectedRow >= 0) {
                for (int i = 0; i < tblSplReviewUsg.getRowCount(); i++) {
                    if (codeTableModel.getValueAt(sorter.getIndexForRow(i),SPECIAL_REVIEW_COLUMN).toString().equals(temp.toString())) {
                        tblSplReviewUsg.setRowSelectionInterval(i,i);
                        break;
                    } else {
                        if ( i == tblSplReviewUsg.getRowCount() -1)
                            tblSplReviewUsg.setRowSelectionInterval(SPECIAL_REVIEW_COLUMN,SPECIAL_REVIEW_COLUMN);
                    }
                }
            }
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int rowNum = tblSplReviewUsg.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblSplReviewUsg.getEditingColumn() >= SPECIAL_REVIEW_COLUMN ) {
            tblSplReviewUsg.getCellEditor().stopCellEditing();
        }
        
        //here need to check dependency first
        if (!checkDependency(rowNum, "Delete")) {
            return;
        }
        String msg = coeusMessageResources.parseMessageKey(
                "generalDelConfirm_exceptionCode.2100");
        int confirm = CoeusOptionPane.showQuestionDialog(
                msg,
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        if (confirm == JOptionPane.NO_OPTION) return;
        String message = coeusMessageResources.parseMessageKey(
                "protocolFollowupAction_resetNumbers_exceptionCode.2408");
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
            tblSplReviewUsg.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ;
        }
        
        if (tblSplReviewUsg.getRowCount() > 0  ) {
            tblSplReviewUsg.changeSelection(rowNum,  1, false, false) ;
        }
    }
    
    public void btnCloseActionPerformed() {
        GetLastValue();
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
    
    //copy one table row to a hashmap
    private HashMap getTableRow(int rowNum) {
        HashMap hashRow = new HashMap() ;
        int colTotal = codeTableModel.getColumnCount()-1;
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
                if(colCount == 1){
                    val = moduleCode;
                }else
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
                        val = val.toString() ;
                    }
                }
            }
            
            if (columnBean.getDataType().indexOf("VARCHAR2") != -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount)==null) {
                    val = "";
                } else {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString() ;
                    if(colCount == SHOW_IN_LITE_COLUMN){
                        if("No".equalsIgnoreCase(val.toString())){
                            val = "N";
                        }else if("Yes".equalsIgnoreCase(val.toString())){
                            val = "Y";
                        }
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
                        Object valTest = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), colCount);
                        val =  java.sql.Timestamp.valueOf(codeTableModel.getValueAt
                                (sorter.getIndexForRow(rowNum),colCount).toString()) ;
                    }
                }
            }
            
            hashRow.put(columnBean.getColIdentifier(), val) ;
        } //end for
        return hashRow;
    }
    
    private boolean saveData() {
        
        // do the validation b4 u go and build the vector of modified rows
        if (tableDataValid()) {
            // get the modified rows
            Vector vecModifiedRows = getModifiedRows() ;
            HashMap hashStoredProcedure =
                    (HashMap)tblSplReviewStructure.getHashStoredProceduresForThisTable();
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
        int rowTotal = tblSplReviewUsg.getRowCount();
        int colTotal = tblSplReviewUsg.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++) {
            for (int colIndex = 0; colIndex < colTotal; colIndex++) {
                TableColumn column = codeTableColumnModel.getColumn(colIndex + 1) ;//becasue protocol action is first column
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                && !columnBean.getColumnCanBeNull()) {
                    if ( tblSplReviewUsg.getValueAt(rowIndex,colIndex) != null) {
                        if (tblSplReviewUsg.getValueAt(rowIndex,colIndex).equals("")
                        || tblSplReviewUsg.getValueAt(rowIndex,colIndex).toString().trim().equals("") ) {
                            String msg = coeusMessageResources.parseMessageKey(
                                    "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblSplReviewUsg.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    } else {
                        String msg = coeusMessageResources.parseMessageKey(
                                "checkInputValue_exceptionCode.2402");
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblSplReviewUsg.changeSelection(rowIndex,  colIndex, false, false) ;
                        return false;
                    }
                }
                //Check that values for Protocol Action and Protocol Followup Action are not the same
                if( colIndex == SPECIAL_REVIEW_COLUMN){ 
                    Object specialReview = tblSplReviewUsg.getValueAt(rowIndex, 0);
                    if(specialReview instanceof ComboBoxBean){
                        ComboBoxBean specialReviewCombo =
                                (ComboBoxBean)specialReview;
                        if(specialReviewCombo == null || specialReviewCombo.getCode() == null ||
                                specialReviewCombo.getCode().equals("") ){
                            CoeusOptionPane.showInfoDialog("Select a special review type.");
                            return false;
                        }
                    }else{
                        CoeusOptionPane.showInfoDialog("Select a special review type.");
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
        
        while(rowCount < tblSplReviewUsg.getRowCount()) {// check AC_TYPE field
            String tmpACType ;
            if (codeTableModel.getValueAt(sorter.getIndexForRow(rowCount), codeTableModel.getColumnCount()-1) != null) {
                vecUpdatedRows.add(getTableRow(rowCount)) ;
            }
            rowCount++ ;
        }//end while
        
        return vecUpdatedRows ;
    }
    
    public void initialiseData() {
        hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure() ;
        TableStructureBean tempTableStructBean = null ;
        for (int tableCount =0 ; tableCount < hashAllCodeTableStructure.size() ; tableCount++) {
            tempTableStructBean  = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(tableCount)) ;
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$COEUS_MODULES")) {
                tblModuleStructure = tempTableStructBean ;
            }
             
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$SPECIAL_REVIEW_USAGE")) {
                tblSplReviewStructure = tempTableStructBean ;
            }
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$SPECIAL_REVIEW")) {
                tblSplReviewTypeStructure = tempTableStructBean ;
            }
        }// end for
        
//        vecModule = getDataForCombo(tblModuleStructure);
//        HashMap hmRow = new HashMap();
//        cmbCoeusModule.addItem(new ComboBoxBean("", "")) ;
//        for (int i=0; i < vecModule.size(); i++) {
//            hmRow = (HashMap)vecModule.elementAt(i);
//            cmbCoeusModule.addItem(new ComboBoxBean
//                    (hmRow.get("MODULE_CODE").toString() ,
//                    hmRow.get("DESCRIPTION").toString()));
//        }//end for
        HashMap hmTableColumnBean = tblSplReviewStructure.getHashTableColumns();
        HashMap hmModule = null;
        if (hmTableColumnBean.size() > 0) {
                ColumnBean newColumnBean = (ColumnBean)hmTableColumnBean.get(new Integer(1)) ;
                 hmModule = newColumnBean.getOptions();
        }
        ComboBoxBean comboBoxBean = null;
        
        if(hmModule != null && hmModule.size() > 0){
            Set moduleSet = hmModule.keySet();
            Iterator moduleIterator  = moduleSet.iterator();
            while(moduleIterator.hasNext()){
                String moduleCode = (String)moduleIterator.next();
                comboBoxBean = new ComboBoxBean(moduleCode , hmModule.get(moduleCode).toString());
                if("1".equals(moduleCode)){
                    awardComboBoxBean = comboBoxBean;
                }
                cmbCoeusModule.addItem(comboBoxBean);
            }
        }
        
        
        vecSpecialReview = getDataForCombo(tblSplReviewTypeStructure);
        
     
    }
    
    
    public Vector drawTableUsingTableStructureBean() {
        vecDeletedRows.clear();
        codeTableModel = new ExtendedDefaultTableModel() ;
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tblSplReviewStructure.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsDisplay = tblSplReviewStructure.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNames = new String[numColumnsDisplay + 1];
        // create a table colunmodel thing can used to access Table Column directly
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        
        int lastDisplayCol = 0;
        
        // populate the column header name on the the screen
        if (numColumnsDisplay > 0) {
            for (int i = 0; i < numColumnsDisplay; i++) {
                //to add columns
                ColumnBean newColumnBean = (ColumnBean)hmTableColumnBean.get(new Integer(i)) ;
                TableColumn newColumn = new TableColumn(i, newColumnBean.getDisplaySize()) ; // set the model index and width
                newColumn.setIdentifier(newColumnBean) ; // set the column bean itself as identifier
                newColumn.setHeaderValue(newColumnBean.getDisplayName()) ;
                
                if (newColumnBean.getColumnVisible()) {
                    lastDisplayCol = i;
                }
                
                if (i == SPECIAL_REVIEW_COLUMN) {
                    HashMap hmRow = new HashMap();
                    CoeusComboBox comb = new CoeusComboBox() ;
                    if (vecSpecialReview == null) continue;
                    comb.addItem(new ComboBoxBean("", "")) ;
                    for (int j=0; j < vecSpecialReview.size(); j++) //loop for num of rows
                    {
                        hmRow = (HashMap)vecSpecialReview.elementAt(j);
                        comb.addItem
                                (new ComboBoxBean(hmRow.get("SPECIAL_REVIEW_CODE").toString() ,
                                hmRow.get("DESCRIPTION").toString()));
                    }
                    newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
                }
                
                else if(i == SHOW_IN_LITE_COLUMN) {
                    CoeusComboBox comb = new CoeusComboBox();
                    comb.addItem(new ComboBoxBean("N", "No"));
                    comb.addItem(new ComboBoxBean("Y", "Yes"));
                    newColumn.setCellEditor(new ExtendedCellEditor(comb));
                }
                
                else {
                    newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean, i)) ;
                }
                if (newColumnBean.getDisplaySize()<=5) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 40) ;
                } else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 30) ;
                } else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 20) ;
                } else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 12) ;
                } else {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                if(i == SHOW_IN_LITE_COLUMN){
                    newColumn.setMinWidth(100) ;
                    newColumn.setMaxWidth(100) ;
                    newColumn.setPreferredWidth(100);
                }
                
                // add the new columnto the column model also
                codeTableColumnModel.addColumn(newColumn) ;
                displayCodeTableColumnModel.addColumn(newColumn) ;
                
                // add it to the tablemodel
                codeTableModel.addColumn(newColumn);
                
                columnNames[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName();
            }
            TableColumn newColumn = codeTableColumnModel.getColumn(lastDisplayCol) ;
            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"");
            
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tblSplReviewStructure and get the DataBean from the servlet.
            getDataForTheTable();
            
            // populate the data on the the screen
            vdata = accDataBean.getVectData();
            HashMap hashRow = new HashMap();
            
            for (int i=0; i < vdata.size(); i++) //loop for num of rows
            {
                hashRow = (HashMap)vdata.elementAt(i);
                Object [] rowColumnDatas = new Object[numColumnsDisplay];
                
                for (int j = 0; j < numColumnsDisplay; j++) {
                    rowColumnDatas[j] = (Object)hashRow.get(columnNames[j]);
                    TableColumn column = codeTableColumnModel.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                    if (j == SPECIAL_REVIEW_COLUMN) {
                        if (vecSpecialReview != null) {
                            if (rowColumnDatas[j] != null) {
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vecSpecialReview.size(); codeIndex++) {
                                    HashMap hmRow = (HashMap)vecSpecialReview.elementAt(codeIndex);
                                    if (hmRow.get("SPECIAL_REVIEW_CODE").toString().equals(temp)) {
                                        rowColumnDatas[j] = new ComboBoxBean
                                                (hmRow.get("SPECIAL_REVIEW_CODE").toString() ,
                                                hmRow.get("DESCRIPTION").toString());
                                        break;
                                    }
                                }
                            }
                        } 
                    }else if(j == 1) { //Show In Lite Column
                        CoeusComboBox comb = new CoeusComboBox();
                        comb.addItem(new ComboBoxBean("N", "No"));
                        comb.addItem(new ComboBoxBean("Y", "Yes"));
                        newColumn.setCellEditor(new ExtendedCellEditor(comb));
                    }

                    Object obj = rowColumnDatas[j];
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
                if("N".equals(rowColumnDatas[2])){
                    rowColumnDatas[2] = new ComboBoxBean("N", "No");
                }else if("Y".equals(rowColumnDatas[2])){
                    rowColumnDatas[2] = new ComboBoxBean("Y", "Yes");
                }
                codeTableModel.addRow(rowColumnDatas);
            }
            
        }
        
        sorter.setModel(codeTableModel,false) ;
        tblSplReviewUsg.setModel(sorter) ;
        
        tblSplReviewUsg.setColumnModel(displayCodeTableColumnModel); // also give the structure
        
        // tblCodeTable.addFocusListener(this) ;
        tblSplReviewUsg.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        // hide unwanted columns
        int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
        //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
        for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--) {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            
            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblSplReviewUsg.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblSplReviewUsg.removeColumn(inColumn );
            }
        }
        if (tblSplReviewUsg.getRowCount() > 0) {
            tblSplReviewUsg.setRowSelectionInterval(0,0) ;
            tblSplReviewUsg.repaint() ;
        }
        
        return vdata;
    }
    
    
    
    private void getDataForTheTable() {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        Vector vectData = null;
        //Get all stored procedures associated with this table.
        HashMap hashStoredProcedure =
                (HashMap)tblSplReviewStructure.getHashStoredProceduresForThisTable();
 
        //Get the select stored procedure associated witht this table.
        StoredProcedureBean selectStoredProcedure =
                (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
        
        RequestTxnBean requestTxnBean = new RequestTxnBean();
        requestTxnBean.setAction("GET_DATA");
        requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
        
        Vector param= new Vector();
        param.add("OSP$SPECIAL_REVIEW_USAGE");
        param.add("AW_MODULE_CODE");
        param.add(moduleCode);
        
        requestTxnBean.setSelectProcParameters(param) ;
        
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(requestTxnBean) ;
        
        String connectTo =CoeusGuiConstants.CONNECTION_URL
                + CODE_TABLE_SERVLET ;
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        
        userId = requester.getUserName();
        
        ResponderBean responder = comm.getResponse();
        if (responder.isSuccessfulResponse()) {
            accDataBean = (DataBean)responder.getDataObject() ;
        }
        
        if(accDataBean != null) {

            vectData = accDataBean.getVectData();

        }
        
        accDataBean.setVectData(vectData);
        
    }
    
    private Vector getDataForCombo(TableStructureBean tableStructureBean) {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        Vector vectData = null;
        try {
 
            //Get all stored procedures associated with this table.
            HashMap hashStoredProcedure =
                    (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
            //Get the select stored procedure associated witht this table.
            StoredProcedureBean selectStoredProcedure =
                    (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
            
            RequestTxnBean requestTxnBean = new RequestTxnBean();
            requestTxnBean.setAction("GET_DATA");
            requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
            
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(requestTxnBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + CODE_TABLE_SERVLET ;
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
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
    
    private Object getDataFromServlet(Object request) {
        Object response = null ;
        try {
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(request) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL
                    + CODE_TABLE_SERVLET ;
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                response = responder.getDataObject() ;
            }
            
        }catch(Exception ex) {
            ex.printStackTrace() ;
        }
        
        return response ;
    }
    
    class ExtendedCellEditor extends DefaultCellEditor implements TableCellEditor {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        public ExtendedCellEditor(ColumnBean columnBeanIn, int colIndex) {
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
                public void focusLost(FocusEvent fe) {
                    if (!fe.isTemporary()) {

                        GetLastValue();
                    }// end if
                }// end focus lost
            }); // end focus listener
            
        }
        
        public ExtendedCellEditor(CoeusComboBox comb) {
            super(comb);
            comb.addFocusListener(new FocusAdapter() {
                
                public void focusLost(FocusEvent fe) {
                    if (!fe.isTemporary()) {
                        GetLastValue();
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
            
            if (column == 0 || column == 1 )//protocol followup action or user prompt flag
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
        
        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
        public Object getCellEditorValue() {
            
            Object oldValue = tblSplReviewUsg.getValueAt(selRow,selColumn);
            Object newValue = null;
            if(selColumn == 0 || selColumn == 1) {
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            } else {
                newValue=((CoeusTextField)txtCell).getText();
            }
            if(!triggerValidation(newValue.toString())) {
                return oldValue;
            }
            
            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            if(selRow > -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null) {
                    if (tblSplReviewUsg.getValueAt(selRow,selColumn)!=null) {
                        if (!(tblSplReviewUsg.getValueAt(selRow,selColumn).toString().
                                equals(((CoeusTextField)txtCell).getText()))) {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tblSplReviewStructure.getUserIndex() );
                            saveRequired = true;
                        }
                    } else {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow),
                                tblSplReviewStructure.getUserIndex() );
                        saveRequired = true;
                    }
                }
            }
            
            // validations passed and ac_type has been set.
            return newValue;
        }
        
        /**
         * This method is over loaded to handle the item state changed events.
         * @param e ItemEvent
         */
        
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        
        // there will various types of triggervalidation functions which takes care
        // of validation for different types of data.
        private boolean triggerValidation(String newValue) {
            if (!tblSplReviewStructure.isIdAutoGenerated()) {
                int colIdx1 = -1;
                int colIdx2 = -1;
                int colIdx3 = -1;
                Vector vecPrimaryKey = tblSplReviewStructure.getPrimaryKeyIndex();
                if ( vecPrimaryKey.size() == 1) {
                    colIdx1 = tblSplReviewStructure.getPrimaryKeyIndex(0);
                }else if ( vecPrimaryKey.size() == 2 ) {
                    colIdx1 = tblSplReviewStructure.getPrimaryKeyIndex(0);
                    colIdx2 = tblSplReviewStructure.getPrimaryKeyIndex(1);
                }else if ( vecPrimaryKey.size() == 3 ) {
                    colIdx1 = tblSplReviewStructure.getPrimaryKeyIndex(0);
                    colIdx2 = tblSplReviewStructure.getPrimaryKeyIndex(1);
                    colIdx3 = tblSplReviewStructure.getPrimaryKeyIndex(2);
                }
                if (selColumn == colIdx1 || selColumn == colIdx2 ) {
                    if (checkDependency(selRow, "")) {
                        if(!CheckUniqueId(newValue, selRow, selColumn)) {
                            String msg = coeusMessageResources.parseMessageKey(
                                    "protocolFollowupAction_PKeyUniq_exceptionCode.2409");
                            
                            CoeusOptionPane.showInfoDialog(msg);
                            return false; //fail in uniqueid check
                        }
                    } else {
                        return false;//fail in dependency check
                    }
                }
                if(selColumn == 1) {
                    ComboBoxBean newValueCombo = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
                    String newValueCode = newValueCombo.getCode();
                    if(newValueCode.equals(moduleCode)) {
                        String msg = coeusMessageResources.parseMessageKey(
                                "protocolFollowupAction_exceptionCode.2407");
                        
                        CoeusOptionPane.showInfoDialog(msg);
                        return false;
                    }
                }
            }
            return true;
        }// end method
        
    } // end class
    
    
    
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
    
    private boolean CheckUniqueId(String newVal, int selRow, int selCol) {
        int rowTotal = tblSplReviewUsg.getRowCount();
        int row = tblSplReviewUsg.getEditingRow();
        if (rowTotal > 0 && row >= 0) {
            Vector vecPrimaryKey = tblSplReviewStructure.getPrimaryKeyIndex();
            int colIdx1 = -1;
            int colIdx3 = -1;
            //keep selected column number in colIdx1
            if (selCol == (tblSplReviewStructure.getPrimaryKeyIndex(1)-1)) {
                colIdx1 = tblSplReviewStructure.getPrimaryKeyIndex(0) ; 
                colIdx3 = tblSplReviewStructure.getPrimaryKeyIndex(1) ; 

            } else {
                return true;
            }
            Object valTemp2 = moduleCode;
            for (int index = 0; index < rowTotal ; index++) {
                if (index != row) {
                    Object val1 = tblSplReviewUsg.getValueAt(index, colIdx1);
                    Object val3 = moduleCode;
                    
                    if (val1 instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val1 ;
                        val1 = cmbTmp.getDescription() ;//because the newVal is description
                    }
                    if (val3 instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val3;
                        val3 = cmbTmp.getCode() ;
                    }
                    if (val1 != null && val3 != null  && valTemp2 != null) {
                        if (newVal.toString().equals(val1.toString()) &&
                                valTemp2.toString().equals(val3.toString()))
                            return false;
                    }
                }
            }
        }
        return true;
    }
    
 private boolean checkDependency(int rowNumber, String from) {
        HashMap hmDependencyCheckValues = new HashMap();
        int colNumber = tblSplReviewStructure.getPrimaryKeyIndex(0);
        TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
        ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
        HashMap hmDependencyTables = (HashMap)tblSplReviewStructure.getHashTableDependency();
        int depenTabNum;
        
        if (hmDependencyTables == null) {
            return true; //there is no dependency need to check


        }
        return true;
    }
    
    
    
    private void GetLastValue() {
        int row = tblSplReviewUsg.getEditingRow();
        int col = tblSplReviewUsg.getEditingColumn();
        if (row != -1 && col != -1) {
            tblSplReviewUsg.getCellEditor(row, col).stopCellEditing();
            TableColumn column = codeTableColumnModel.getColumn(col + 1) ; //here col +1 ,because state code is second col in codeTableColumnModel, is first col in tblSplReviewUsg
            //make sure to set AC_TYPE
            column.getCellEditor().getCellEditorValue();
        }
        
    }
    
}  //end class
