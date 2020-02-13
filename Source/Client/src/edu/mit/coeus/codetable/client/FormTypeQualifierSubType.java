/*
 * FormTypeQualifierSubType.java
 *
 * Created on February 4, 2008, 12:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.codetable.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.Date;
import java.beans.*;
import java.awt.Font;

import edu.mit.coeus.gui.CoeusFontFactory;
//import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAboutForm ;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.utils.CoeusComboBox;

/**
 *
 * @author divyasusendran
 */
public class FormTypeQualifierSubType extends CoeusDlgWindow {
    
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblTypeQualSubType;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private CoeusComboBox cmbSubmissionType = new CoeusComboBox();
//    private javax.swing.JScrollPane scrlpnlMain;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlQualifier;
    private javax.swing.JPanel pnlSubTypeQualifier;
    private javax.swing.JSplitPane splitpnlMain;
    private javax.swing.JButton btnSave;
    private JLabel lblSubmissionType;
    private JLabel lblTypeQualifier;
    private GridBagConstraints gridBagConstraints;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;
    
//    private TableSorter sorter ;
    private TableSorterCodeTable sorter ;
    /* This is used to know whether data need reflash or not */
    private boolean cancelRequired = false;
    
    ExtendedDefaultTableModel codeTableModel ; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tableStructureProtocolSubType;
    TableStructureBean tableStructureTypeQualSubmissionType;
    TableStructureBean tableStructureTypeQualifier;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ;
    int numColumnsDisplay = 0 ;
    String subTypeCode = new String();
    Vector vecDeletedRows = new Vector();
    Vector vectSubType = null;
    Vector vectTypeQualifier = null;
    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
    
    
    
    /** Creates a new instance of FormTypeQualifierSubType */
    public FormTypeQualifierSubType(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights)
            formatFields();
        cmbSubmissionType.setFont(CoeusFontFactory.getLabelFont());
        cmbSubmissionType.setSelectedItem("100") ; 
        
    }
    
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        lblSubmissionType = new JLabel("Submission Type");
        lblTypeQualifier = new JLabel("");
        lblSubmissionType.setFont(CoeusFontFactory.getLabelFont());
        lblTypeQualifier.setFont(CoeusFontFactory.getLabelFont());
        
        pnlQualifier = new JPanel(new GridBagLayout()) ;
        pnlSubTypeQualifier = new JPanel(new GridBagLayout()) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 0, 2, 1);
        pnlSubTypeQualifier.add(lblSubmissionType, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 5, 2, 1);
        cmbSubmissionType.setMinimumSize(new Dimension(250,20));
        cmbSubmissionType.setMaximumSize(new Dimension(250,20));
        cmbSubmissionType.setPreferredSize(new Dimension(250,20));
        pnlSubTypeQualifier.add(cmbSubmissionType, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(4, 10, 2, 1);
        lblTypeQualifier.setMinimumSize(new Dimension(250,5));
        lblTypeQualifier.setMaximumSize(new Dimension(250,5));
        lblTypeQualifier.setPreferredSize(new Dimension(250,5));
        pnlSubTypeQualifier.add(lblTypeQualifier, gridBagConstraints) ;        
        
        initialiseData() ;
        
        sorter = new TableSorterCodeTable() ; 
        tblTypeQualSubType = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblTypeQualSubType); 
        
        tblTypeQualSubType.setRowHeight(22);
        tblTypeQualSubType.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
        = tblTypeQualSubType.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));        
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblTypeQualSubType.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblTypeQualSubType, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlSubTypeQualifier, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
        cmbSubmissionType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    GetLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbSubmissionType.getSelectedItem() ;
                    String newSubmissionTypeCode = cmb.getCode();
                    if (subTypeCode != newSubmissionTypeCode && saveRequired) {
                        String msg = coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002");
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                        if(confirm == JOptionPane.YES_OPTION) {
                            if (!saveData()) {
                                //There are errors to be corrected before leaving this submission type.
                                cancelRequired = true;
                                cmbSubmissionType.setSelectedItem(subTypeCode) ;
                                return;
                            }
                        }else if (confirm == JOptionPane.CANCEL_OPTION) {
                            cancelRequired = true;
                            cmbSubmissionType.setSelectedItem(subTypeCode) ;
                            return;
                        }
                        
                    }
                    if (cancelRequired) {
                        cancelRequired = false;
                        return;
                    }
                    saveRequired = false;
                    subTypeCode = newSubmissionTypeCode ;
                    if (subTypeCode.equals("")) {
                        // hide the table
                        tblTypeQualSubType.setVisible(false) ;
                    }
                    else {
                        Vector vecTable = drawTableUsingTableStructureBean() ;
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
        pnlQualifier.add(splitpnlMain, gridBagConstraints) ;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlQualifier.add(pnlForBtn, gridBagConstraints) ;
        
        getContentPane().add(pnlQualifier, BorderLayout.CENTER);
        pack() ;
        
        // this will take care of the window closing...
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                btnCloseActionPerformed() ;
            }
        } );
    }
    
    private void formatFields()
    {
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    
    public void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        Object[]  oneRow = {" "};
        
        sorter.insertRow(sorter.getRowCount(),oneRow);
        //Set the submission type code with the user selection.
        codeTableModel.setValueAt(subTypeCode, codeTableModel.getRowCount()-1, 0);
        // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1,
        tableStructureTypeQualSubmissionType.getDuplicateIndex(0) );
        //set the type qualifier code
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, 1 );
        
        // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1,
        codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1,
        tableStructureTypeQualSubmissionType.getUserIndex() );
        tblTypeQualSubType.requestFocus();
    }
    
    public void btnSaveActionPerformed() {
        Object temp = null;
        int  selectedRow = tblTypeQualSubType.getSelectedRow();
        GetLastValue();               
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();       
        if (!saveData()) return;
        drawTableUsingTableStructureBean();
        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

        if (selectedRow >= 0)
        {    temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);      
            for (int i = 0; i < tblTypeQualSubType.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblTypeQualSubType.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblTypeQualSubType.getRowCount() -1)
                        tblTypeQualSubType.setRowSelectionInterval(0,0);
                }
            }
        }
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int rowNum = tblTypeQualSubType.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblTypeQualSubType.getEditingColumn() >= 0 ) {
            tblTypeQualSubType.getCellEditor().stopCellEditing();
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
        //keep all deleted rows in vecDeletedRows.
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),codeTableModel.getColumnCount()-1) != "I") {            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModel.setValueAt("D", sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1);
            saveRequired = true;
            //save to vecDeletedRows with hash of vec
            vecDeletedRows.add(getTableRow(rowNum)) ;
        }//end if
        
        sorter.removeRow(rowNum);
        
        if ( rowNum != 0 )
        {
            rowNum--; 
        }
        else
        {
            tblTypeQualSubType.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }
        
        if (tblTypeQualSubType.getRowCount() > 0  ) {
            tblTypeQualSubType.changeSelection(rowNum, 0, false, false) ;
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
            
        }
        else {
            this.dispose();
        }
        
    }
    
    //copy one table row to a hashmap
    private HashMap getTableRow(int rowNum) {
        HashMap hashRow = new HashMap() ;
//        int colTotal = codeTableModel.getColumnCount()-1;
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
                }
                else {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount);
                    if (val instanceof ComboBoxBean) {
                        if (((ComboBoxBean)val).getCode().equals(""))// user deleted selection
                            val = null;
                        else
                            val = new Integer(((ComboBoxBean)val).getCode().toString());
                    }
                    else {
                        val = new Integer(val.toString()) ;
                    }
                }
            }
            
            if (columnBean.getDataType().indexOf("VARCHAR2") != -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount)==null) {
                    val = "";
                }
                else {
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
                }
                else {  // for update
                    // there will be only two dates in any table one AV_UPDATE_TIMESTAMP and the other one AW_UPDATE_TIMESTAMP
                    if (columnBean.getQualifier().equals("VALUE")) {  //AV_...
                        //coeusdev-568 start
                        //val = new java.sql.Timestamp(today.getTime());
                        val = CoeusUtils.getDBTimeStamp();
                        //coeusdev-568 end
                    }
                    else {  //AW_...
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
        
        // do the validation before u go and build the vector of modified rows
        if (tableDataValid()) {
            // get the modified rows
            Vector vecModifiedRows = getModifiedRows() ;
            if (vecModifiedRows != null) {
                System.out.println("obtd modified rows successfuly") ;
            }
            
            HashMap hashStoredProcedure =
            (HashMap)tableStructureTypeQualSubmissionType.getHashStoredProceduresForThisTable();
            if(hashStoredProcedure == null)
                System.out.println("hashStoredProcedure == null");
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
            }
            else {//Data Saved Successfully
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
        int rowTotal = tblTypeQualSubType.getRowCount();
        int colTotal = tblTypeQualSubType.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++) {
            for (int colIndex = 0; colIndex < colTotal; colIndex++) {
                TableColumn column = codeTableColumnModel.getColumn(colIndex+1) ;//+1 becasue submission type is first column
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                && !columnBean.getColumnCanBeNull()) {
                    if ( tblTypeQualSubType.getValueAt(rowIndex,colIndex) != null) {
                        if (tblTypeQualSubType.getValueAt(rowIndex,colIndex).equals("")
                        || tblTypeQualSubType.getValueAt(rowIndex,colIndex).toString().trim().equals("") ) {
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblTypeQualSubType.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else {
                        String msg = coeusMessageResources.parseMessageKey(
                        "checkInputValue_exceptionCode.2402");
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblTypeQualSubType.changeSelection(rowIndex,  colIndex, false, false) ;
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
        
        while(rowCount < tblTypeQualSubType.getRowCount()) {// check AC_TYPE field
//            String tmpACType ;
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
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$SUBMISSION_TYPE")) {
                tableStructureProtocolSubType = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$VALID_PROTO_SUB_TYPE_QUAL")) {
                tableStructureTypeQualSubmissionType = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$SUBMISSION_TYPE_QUALIFIER")) {
                tableStructureTypeQualifier = tempTableStructBean ;
            }
        }// end for
        
        vectTypeQualifier = getDataForCombo(tableStructureTypeQualifier);
        vectSubType = getDataForCombo(tableStructureProtocolSubType);
        HashMap hmRow = new HashMap();
        cmbSubmissionType.addItem(new ComboBoxBean("", "")) ;
        for (int i=0; i < vectSubType.size(); i++) //loop for num of rows
        {
            hmRow = (HashMap)vectSubType.elementAt(i);
            cmbSubmissionType.addItem(new ComboBoxBean
            (hmRow.get("SUBMISSION_TYPE_CODE").toString() ,
            hmRow.get("DESCRIPTION").toString()));
        }//end for
        
    }
    
    
    public Vector drawTableUsingTableStructureBean() {
        vecDeletedRows.clear();
        
        codeTableModel = new ExtendedDefaultTableModel() ;
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureTypeQualSubmissionType.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsDisplay = tableStructureTypeQualSubmissionType.getNumColumns() ;
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
                
                if (i == 1)//Type Qualifier
                {
                    HashMap hmRow = new HashMap();
                    CoeusComboBox comb = new CoeusComboBox() ;
                    if (vectTypeQualifier == null) continue;
                    comb.addItem(new ComboBoxBean("", "")) ;
                    
                    for (int j=0; j < vectTypeQualifier.size(); j++) //loop for num of rows
                    {
                        hmRow = (HashMap)vectTypeQualifier.elementAt(j);
                        comb.addItem
                        (new ComboBoxBean(hmRow.get("SUBMISSION_TYPE_QUAL_CODE").toString() ,
                        hmRow.get("DESCRIPTION").toString()));
                    }//end for
                    
                    newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
                }
                
                else {
                    newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean, i)) ;
                }
                if (newColumnBean.getDisplaySize()<=5) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50) {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2);
                }
                else {
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                
                newColumn.setMinWidth(10) ;
                
                // add the new columnto the column model also
                codeTableColumnModel.addColumn(newColumn) ;
                displayCodeTableColumnModel.addColumn(newColumn) ;
                
                // add it to the tablemodel
                codeTableModel.addColumn(newColumn);
                
                columnNames[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName();
                
            }            
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureTypeQualSubmissionType and get the DataBean from the servlet.
            getDataForTheTable();
            
            // populate the data on the the screen
            vdata = accDataBean.getVectData();
            HashMap hashRow = new HashMap();
            
            for (int i=0; i < vdata.size(); i++) //loop for num of rows
            {
                hashRow = (HashMap)vdata.elementAt(i);
                Object [] rowColumnDatas = new Object[numColumnsDisplay];
                
                for (int j = 0; j < numColumnsDisplay; j++) // loop for num of columns will display on screen
                {
                    rowColumnDatas[j] = (Object)hashRow.get(columnNames[j]);
                    TableColumn column = codeTableColumnModel.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                    if (j == 1) // correspondence type
                    {
                        if (vectTypeQualifier != null) {
                            if (rowColumnDatas[j] != null) {
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectTypeQualifier.size(); codeIndex++) {
                                    HashMap hmRow = (HashMap)vectTypeQualifier.elementAt(codeIndex);
                                    if (hmRow.get("SUBMISSION_TYPE_QUAL_CODE").toString().equals(temp)) {
                                        rowColumnDatas[j] = new ComboBoxBean
                                        (hmRow.get("SUBMISSION_TYPE_QUAL_CODE").toString() ,
                                        hmRow.get("DESCRIPTION").toString());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
//                    Object obj = rowColumnDatas[j];
                    if (columnBean.getColumnVisible()) {
                        if (rowColumnDatas[j] !=null) {
                            if (rowColumnDatas[j].equals("null")) {
                                rowColumnDatas[j] = "";
                            }
                        }
                        else {
                            rowColumnDatas[j] = null ;
                        }
                    }
                }
                
                codeTableModel.addRow(rowColumnDatas);
            }
            
        }
        
        sorter.setModel(codeTableModel, false);
        tblTypeQualSubType.setModel(sorter);
        
        tblTypeQualSubType.setColumnModel(displayCodeTableColumnModel); // also give the structure
        
        // tblCodeTable.addFocusListener(this) ;
        tblTypeQualSubType.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        // hide unwanted columns
        int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
        //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
        for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--) {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            
            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblTypeQualSubType.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblTypeQualSubType.removeColumn(inColumn );
            }
        }
        if (tblTypeQualSubType.getRowCount() > 0) {
            tblTypeQualSubType.setRowSelectionInterval(0,0) ;
            tblTypeQualSubType.repaint() ;
        }
        
        return vdata;
    }
    
    
    
    private void getDataForTheTable() {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        Vector vectData = null;
        if(tableStructureTypeQualSubmissionType == null)
            System.out.println("tableStructureTypeQualSubmissionType == null");
        //Get all stored procedures associated with this table.
        HashMap hashStoredProcedure =
        (HashMap)tableStructureTypeQualSubmissionType.getHashStoredProceduresForThisTable();
        if(hashStoredProcedure == null)
            System.out.println("hashStoredProcedure for Type Qualifier - Submission Type == null");
        //Get the select stored procedure associated witht this table.
        StoredProcedureBean selectStoredProcedure =
        (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
        
        RequestTxnBean requestTxnBean = new RequestTxnBean();
        requestTxnBean.setAction("GET_DATA");
        requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
        
        Vector param= new Vector();
        param.add("OSP$VALID_PROTO_SUB_TYPE_QUAL");
        param.add("av_submission_type_code");
        param.add(subTypeCode);
        
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
            if(tableStructureBean == null)
                System.out.println("tableStructure == null");
            //Get all stored procedures associated with this table.
            HashMap hashStoredProcedure =
            (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
            if(hashStoredProcedure == null)
                System.out.println("hashStoredProcedure == null");
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
        }
        catch(Exception ex) {
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
            }
            else {
                if (columnBeanIn.getDataType().equals("VARCHAR2")) {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, columnBeanIn.getDisplaySize( )));
                }
                else {
                    txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));
                }
            }
            
            txtCell.addFocusListener(new FocusAdapter() {
                
                public void focusGained(FocusEvent e) {
                    // System.out.println("*** focus Gained ***") ;
                }
                
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
                
                public void focusGained(FocusEvent e) {
                    // System.out.println("*** combo box focus Gained ***") ;
                }
                
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
            
            if (column == 0)//correspondence types
            {
                if (value  instanceof ComboBoxBean) {
                    ComboBoxBean cmbTmp = (ComboBoxBean)value ;
                    Object objTmp = cmbTmp.getCode() ;
                    CoeusComboBox CompTmp = (CoeusComboBox)getComponent();
                    CompTmp.setSelectedItem(objTmp);
                    return (Component)CompTmp;
                    
                }
                else {
                    return (Component)((CoeusComboBox)getComponent());
                }
            }
            
            
            if (value != null) {
                txtCell.setText(value.toString()) ;
            }
            else {
                txtCell.setText(null) ;
            }
            return txtCell;
        }
        
        public Object getCellEditorValue() {
            Object oldValue = tblTypeQualSubType.getValueAt(selRow,selColumn);
            Object newValue = null;
            if(selColumn == 0) // correspondence types
            {
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            } 
            // Code added for coeus4.3 enhancement - starts
            else if(selColumn == 1) {
                newValue = txtCell.getText();
            }
            // Code added for coeus4.3 enhancement - ends
            if(!triggerValidation(newValue.toString())) {
                return oldValue;
            }
            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            if(selRow > -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null) {
                    if (tblTypeQualSubType.getValueAt(selRow,selColumn)!=null) {
                        if (!(tblTypeQualSubType.getValueAt(selRow,selColumn).toString().
                        equals(((CoeusTextField)txtCell).getText()))) {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureTypeQualSubmissionType.getUserIndex() );
                            saveRequired = true;
                        }
                    }
                    else {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow),
                        tableStructureTypeQualSubmissionType.getUserIndex() );
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
        
        
        /** Supporting method used for validation.  There will be various
         * types of triggerValidation functions which take care of validation
         * for different types of data.
         */
        private boolean triggerValidation(String newValue) {
            if (!tableStructureTypeQualSubmissionType.isIdAutoGenerated()) {
                int colIdx1 = -1;
                int colIdx2 = -1;
                int colIdx3 = -1;
                Vector vecPrimaryKey = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex();
                if ( vecPrimaryKey.size() == 1) {
                    colIdx1 = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(0);
                }else if ( vecPrimaryKey.size() == 2 ) {
                    colIdx1 = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(1);
                }else if ( vecPrimaryKey.size() == 3 ) {
                    colIdx1 = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(1);
                    colIdx3 = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(2);
                }
                if (selColumn == colIdx1 || selColumn == colIdx2 || selColumn == colIdx3) {
                    if (checkDependency(selRow, "")) {
                        if(!CheckUniqueId(newValue, selRow, selColumn)) {
                            String msg = coeusMessageResources.parseMessageKey(
                            "protocolFollowupAction_PKeyUniq_exceptionCode.2409");
                            
                            CoeusOptionPane.showInfoDialog(msg);
                            return false; //fail in uniqueid check
                        }
                    }
                    else {
                        return false;//fail in dependency check
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
    
    //Although there are two primary keys, the first primary key is Submission type.
    //Submission Type value will be the same for all the rows on a given screen,
    //and is an invisible column.
    private boolean CheckUniqueId(String newVal, int selRow, int selCol) {
        int rowTotal = tblTypeQualSubType.getRowCount();
        int row = tblTypeQualSubType.getEditingRow();
        if (rowTotal > 0 && row >= 0) {
            for (int index = 0; index < rowTotal ; index++) {
                if (index != row) {
                    Object val = codeTableModel.getValueAt(sorter.getIndexForRow(index), selCol+1);//+ 1 because first column is Submission Type
                    if (val instanceof ComboBoxBean) {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val ;
                        //                        val = cmbTmp.getCode() ;
                        val = cmbTmp.getDescription();
                    }
                    
                    if (val != null) {
                        if (newVal.toString().equals(val.toString()))
                            return false;
                    }
                }
            }
        }
        return true;
    }
    
    
    private boolean checkDependency(int rowNumber, String from) {
        int colNumber = tableStructureTypeQualSubmissionType.getPrimaryKeyIndex(0);
        TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
        ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
        HashMap hmDependencyTables = (HashMap)tableStructureTypeQualSubmissionType.getHashTableDependency();
        if (hmDependencyTables == null) {
            return true; //there is no dependency need to check
        }        
        return true;
    }
    
    
    
    private void GetLastValue() {
        int row = tblTypeQualSubType.getEditingRow();
        int col = tblTypeQualSubType.getEditingColumn();
        if (row != -1 && col != -1) {
            tblTypeQualSubType.getCellEditor(row, col).stopCellEditing();
            TableColumn column = codeTableColumnModel.getColumn(col+1) ; //here col +1 ,because correspondence type code is second col in codeTableColumnModel, is first col in tblTypeQualSubType
            //make sure to set AC_TYPE
            column.getCellEditor().getCellEditorValue();
        }
        
    }
    
}
