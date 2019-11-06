/*
 * FormProtocolActionCorresp.java
 *
 * Created on March 11, 2003, 12:18 PM
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
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAboutForm ;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.utils.CoeusComboBox;


public class FormIacucProtoActionCorresp extends CoeusDlgWindow {
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblProtoActionCorresp;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private CoeusComboBox cmbProtocolAction = new CoeusComboBox();
    private javax.swing.JScrollPane scrlpnlMain;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlCorresp;
    private javax.swing.JPanel pnlProtocolAction;
    private javax.swing.JSplitPane splitpnlMain;
    private javax.swing.JButton btnSave;
    private JLabel lblAction;
    private JLabel lblCorresp;
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
    TableStructureBean tableStructureProtocolActionType;
    TableStructureBean tableStructureProtoActionCorresp;
    TableStructureBean tableStructureProtoCorrespType;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ;
    int numColumnsDisplay = 0 ;
    String protocolActionCode = new String();
    Vector vecDeletedRows = new Vector();
    Vector vectProtocolAction = null;
    Vector vectProtoCorrespType = null;
    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
    /** Creates a new instance of FormProtocolFollowupAction */
    public FormIacucProtoActionCorresp(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights)
            formatFields();
        cmbProtocolAction.setFont(CoeusFontFactory.getLabelFont());
        cmbProtocolAction.setSelectedItem("100") ; // Protocol Created
    }
    
    public void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        lblAction = new JLabel("Action");
        lblCorresp = new JLabel("Correspondence Generated");
        lblAction.setFont(CoeusFontFactory.getLabelFont());
        lblCorresp.setFont(CoeusFontFactory.getLabelFont());
        
        pnlCorresp = new JPanel(new GridBagLayout()) ;
        //pnlProtocolAction = new JPanel(new FlowLayout(SwingConstants.CENTER, 30, 10 ));
        pnlProtocolAction = new JPanel(new GridBagLayout()) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 0, 2, 1);
        pnlProtocolAction.add(lblAction, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 5, 2, 1);
        pnlProtocolAction.add(cmbProtocolAction, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(4, 10, 2, 1);
        pnlProtocolAction.add(lblCorresp, gridBagConstraints) ;
        
        
        initialiseData() ;
        
//        sorter = new TableSorter() ; 
        sorter = new TableSorterCodeTable() ; 
        tblProtoActionCorresp = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblProtoActionCorresp); 
        
        tblProtoActionCorresp.setRowHeight(22);
        tblProtoActionCorresp.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
        = tblProtoActionCorresp.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
        
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblProtoActionCorresp.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblProtoActionCorresp, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlProtocolAction, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
        cmbProtocolAction.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    GetLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbProtocolAction.getSelectedItem() ;
                    String newProtocolActionCode = cmb.getCode();
                    if (protocolActionCode != newProtocolActionCode && saveRequired) {
                        String msg = coeusMessageResources.parseMessageKey(
                        "saveConfirmCode.1002");
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
                        if(confirm == JOptionPane.YES_OPTION) {
                            if (!saveData()) {
                                //There are errors to be corrected before leaving this protocol action.
                                cancelRequired = true;
                                cmbProtocolAction.setSelectedItem(protocolActionCode) ;
                                return;
                            }
                        }else if (confirm == JOptionPane.CANCEL_OPTION) {
                            cancelRequired = true;
                            cmbProtocolAction.setSelectedItem(protocolActionCode) ;
                            return;
                        }
                        
                    }
                    if (cancelRequired) {
                        cancelRequired = false;
                        return;
                    }
                    saveRequired = false;
                    protocolActionCode = newProtocolActionCode ;
                    //System.out.println("set protocolActionCode to: "+protocolActionCode);
                    if (protocolActionCode.equals("")) {
                        // hide the table
                        tblProtoActionCorresp.setVisible(false) ;
                    }
                    else {
                        Vector vecTable = drawTableUsingTableStructureBean() ;
                    }
                }
            }
        });
        
        //GridBagConstraints gridBagConstraints;
        
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
        pnlCorresp.add(splitpnlMain, gridBagConstraints) ;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlCorresp.add(pnlForBtn, gridBagConstraints) ;
        
        getContentPane().add(pnlCorresp, BorderLayout.CENTER);
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
//        tblProtoActionCorresp.setEnabled(false);
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
    }
    
    public void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        Object[]  oneRow = {" "};
        
        sorter.insertRow(sorter.getRowCount(),oneRow);
        //Set the protocol action code with the user selection.
        codeTableModel.setValueAt(protocolActionCode, codeTableModel.getRowCount()-1, 0);
        // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1,
        tableStructureProtoActionCorresp.getDuplicateIndex(0) );
        //set the correspondence generated code
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, 1 );
        
        // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1,
        codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1,
        tableStructureProtoActionCorresp.getUserIndex() );
        //Comment out to avoid focus going to the very end of the row, which puts focus too far to the right.
        //tblProtoActionCorresp.changeSelection(codeTableModel.getRowCount()-1,  1, false, false) ;//correspondence is column 1 in CodeTableModel
        //tblProtoActionCorresp.editCellAt(codeTableModel.getRowCount()-1,  1) ;//correspondence is column 1 in CodeTableModel
        tblProtoActionCorresp.requestFocus();
    }
    
    public void btnSaveActionPerformed() {
        Object temp = null;
        int  selectedRow = tblProtoActionCorresp.getSelectedRow();
        GetLastValue();                        
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        //return if failed to save.
        if (!saveData()) return;
        drawTableUsingTableStructureBean();
        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

        if (selectedRow >= 0)
        {   temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);  
            for (int i = 0; i < tblProtoActionCorresp.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblProtoActionCorresp.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblProtoActionCorresp.getRowCount() -1)
                        tblProtoActionCorresp.setRowSelectionInterval(0,0);
                }
            }
        }
//        if (selectedRow > 0 )
//            tblProtoActionCorresp.setRowSelectionInterval(selectedRow,selectedRow);
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int rowNum = tblProtoActionCorresp.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblProtoActionCorresp.getEditingColumn() >= 0 ) {
            tblProtoActionCorresp.getCellEditor().stopCellEditing();
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
            tblProtoActionCorresp.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }
        
        if (tblProtoActionCorresp.getRowCount() > 0  ) {
            tblProtoActionCorresp.changeSelection(rowNum, 0, false, false) ;
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
        
        // do the validation b4 u go and build the vector of modified rows
        if (tableDataValid()) {
            // get the modified rows
            Vector vecModifiedRows = getModifiedRows() ;
            if (vecModifiedRows != null) {
                System.out.println("obtd modified rows successfuly") ;
            }
            
            HashMap hashStoredProcedure =
            (HashMap)tableStructureProtoActionCorresp.getHashStoredProceduresForThisTable();
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
        int rowTotal = tblProtoActionCorresp.getRowCount();
        int colTotal = tblProtoActionCorresp.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++) {
            for (int colIndex = 0; colIndex < colTotal; colIndex++) {
                TableColumn column = codeTableColumnModel.getColumn(colIndex+1) ;//+1 becasue protocol action is first column
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                && !columnBean.getColumnCanBeNull()) {
                    if ( tblProtoActionCorresp.getValueAt(rowIndex,colIndex) != null) {
                        if (tblProtoActionCorresp.getValueAt(rowIndex,colIndex).equals("")
                        || tblProtoActionCorresp.getValueAt(rowIndex,colIndex).toString().trim().equals("") ) {
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblProtoActionCorresp.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else {
                        String msg = coeusMessageResources.parseMessageKey(
                        "checkInputValue_exceptionCode.2402");
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblProtoActionCorresp.changeSelection(rowIndex,  colIndex, false, false) ;
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
        
        while(rowCount < tblProtoActionCorresp.getRowCount()) {// check AC_TYPE field
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
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_PROTOCOL_ACTION_TYPE")) {
                tableStructureProtocolActionType = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_PROTOCOL_ACTION_CORRESP")) {
                tableStructureProtoActionCorresp = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$AC_PROTOCOL_CORRESP_TYPE")) {
                tableStructureProtoCorrespType = tempTableStructBean ;
            }
        }// end for
        
        vectProtoCorrespType = getDataForCombo(tableStructureProtoCorrespType);
        vectProtocolAction = getDataForCombo(tableStructureProtocolActionType);
        HashMap hmRow = new HashMap();
        cmbProtocolAction.addItem(new ComboBoxBean("", "")) ;
        for (int i=0; i < vectProtocolAction.size(); i++) //loop for num of rows
        {
            hmRow = (HashMap)vectProtocolAction.elementAt(i);
            cmbProtocolAction.addItem(new ComboBoxBean
            (hmRow.get("PROTOCOL_ACTION_TYPE_CODE").toString() ,
            hmRow.get("DESCRIPTION").toString()));
        }//end for
        
    }
    
    
    public Vector drawTableUsingTableStructureBean() {
        vecDeletedRows.clear();
        
        codeTableModel = new ExtendedDefaultTableModel() ;
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureProtoActionCorresp.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsDisplay = tableStructureProtoActionCorresp.getNumColumns() ;
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
                
                if (i == 1)//protocol correspondence
                {
                    HashMap hmRow = new HashMap();
                    CoeusComboBox comb = new CoeusComboBox() ;
                    if (vectProtoCorrespType == null) continue;
                    comb.addItem(new ComboBoxBean("", "")) ;
                    
                    for (int j=0; j < vectProtoCorrespType.size(); j++) //loop for num of rows
                    {
                        hmRow = (HashMap)vectProtoCorrespType.elementAt(j);
                        comb.addItem
                        (new ComboBoxBean(hmRow.get("PROTO_CORRESP_TYPE_CODE").toString() ,
                        hmRow.get("DESCRIPTION").toString()));
                    }//end for
                    
                    newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
                }
                
                else {
                    newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean, i)) ;
                }
                if (newColumnBean.getDisplaySize()<=5) {
                    //newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 40) ;
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15) {
                    //newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 30) ;
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30) {
                    //newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 20) ;
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
                }
                else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50) {
                    //newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 12) ;
                    newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2);
                }
                else {
                    //newColumn.setPreferredWidth(newColumnBean.getDisplaySize()*2) ;
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
            // Code commented for coeus4.3 enhancement - starts
//            TableColumn newColumn = codeTableColumnModel.getColumn(lastDisplayCol) ;
//            newColumn.setPreferredWidth(1000) ;
//            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"                                                                                                                                                                                                                                                                                                                                                         ");
            // Code commented for coeus4.3 enhancement - ends
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureProtoActionCorresp and get the DataBean from the servlet.
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
                        if (vectProtoCorrespType != null) {
                            if (rowColumnDatas[j] != null) {
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectProtoCorrespType.size(); codeIndex++) {
                                    HashMap hmRow = (HashMap)vectProtoCorrespType.elementAt(codeIndex);
                                    if (hmRow.get("PROTO_CORRESP_TYPE_CODE").toString().equals(temp)) {
                                        rowColumnDatas[j] = new ComboBoxBean
                                        (hmRow.get("PROTO_CORRESP_TYPE_CODE").toString() ,
                                        hmRow.get("DESCRIPTION").toString());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    
                    Object obj = rowColumnDatas[j];
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
        tblProtoActionCorresp.setModel(sorter);
        
//        //temp out sorter
//        //        sorter.setModel(codeTableModel) ;
//        //        tblProtoActionCorresp.setModel(sorter) ;
//        tblProtoActionCorresp.setModel(codeTableModel);
//        //end temp out sorter
        
        tblProtoActionCorresp.setColumnModel(displayCodeTableColumnModel); // also give the structure
        
        // tblCodeTable.addFocusListener(this) ;
        tblProtoActionCorresp.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        // hide unwanted columns
        int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
        //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
        for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--) {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            
            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblProtoActionCorresp.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblProtoActionCorresp.removeColumn(inColumn );
            }
        }
        if (tblProtoActionCorresp.getRowCount() > 0) {
            tblProtoActionCorresp.setRowSelectionInterval(0,0) ;
            tblProtoActionCorresp.repaint() ;
        }
        
        return vdata;
    }
    
    
    
    private void getDataForTheTable() {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        Vector vectData = null;
        if(tableStructureProtoActionCorresp == null)
            System.out.println("tableStructureProtoActionCorresp == null");
        //Get all stored procedures associated with this table.
        HashMap hashStoredProcedure =
        (HashMap)tableStructureProtoActionCorresp.getHashStoredProceduresForThisTable();
        if(hashStoredProcedure == null)
            System.out.println("hashStoredProcedure for ProtoActionAction == null");
        //Get the select stored procedure associated witht this table.
        StoredProcedureBean selectStoredProcedure =
        (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
        
        RequestTxnBean requestTxnBean = new RequestTxnBean();
        requestTxnBean.setAction("GET_DATA");
        requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
        
        Vector param= new Vector();
        param.add("OSP$AC_PROTOCOL_ACTION_CORRESP");
        param.add("AW_ACTION_TYPE_CODE");
        param.add(protocolActionCode);
        
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
            System.out.println("*** Recvd a DataBean ***");
            vectData = accDataBean.getVectData();
            System.out.println("vectData.size() in GUI: "+vectData.size());
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
            System.out.println("  *** Error in Applet Servlet communication ***  ") ;
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
                if (columnBeanIn.getDataType().equals("UVARCHAR2")) {
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
                       // System.out.println("*** focus lost ***") ;
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
                    //System.out.println("***combo box focus Lost ***");
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
                    //System.out.println(column+": value is a ComboBoxBean");
                    ComboBoxBean cmbTmp = (ComboBoxBean)value ;
                    Object objTmp = cmbTmp.getCode() ;
                    CoeusComboBox CompTmp = (CoeusComboBox)getComponent();
                    CompTmp.setSelectedItem(objTmp);
                    return (Component)CompTmp;
                    
                }
                else {
                    //System.out.println("column: "+column+": value is CoeuscomboBox");
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
        
        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        
        /*public boolean stopCellEditing()
        {
            System.out.println("*** inside stopcellediting selRow: "+selRow+" selectedColumn: "+selColumn+ " ***") ;
            if (triggerValidation())
            {
               return super.stopCellEditing();
            }
            else
            { // returning false will not let the focus get out of the cell unless user
              // keys in valid data
                tblProtoActionCorresp.changeSelection(selRow,  1, false, false) ;
                return false ;
            }
        }*/
        
        
        public Object getCellEditorValue() {
            Object oldValue = tblProtoActionCorresp.getValueAt(selRow,selColumn);
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
            
            /*if (!tableStructureProtoActionCorresp.isIdAutoGenerated())
            {
                int colIdx1 = -1;
                int colIdx2 = -1;
                int colIdx3 = -1;
                Vector vecPrimaryKey = tableStructureProtoActionCorresp.getPrimaryKeyIndex();
                if ( vecPrimaryKey.size() == 1)
                {
                    colIdx1 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
                }else if ( vecPrimaryKey.size() == 2 )
                {
                    colIdx1 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(1);
                }else if ( vecPrimaryKey.size() == 3 )
                {
                    colIdx1 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(1);
                    colIdx3 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(2);
                }
                if (selColumn == colIdx1 || selColumn == colIdx2 || selColumn == colIdx3)
                {
                     if (checkDependency(selRow, ""))
                     {
                        if(!CheckUniqueId(newValue.toString(), selRow, selColumn))
                        {
                            String msg = coeusMessageResources.parseMessageKey(
                                "protocolFollowupAction_PKeyUniq_exceptionCode.2409");
             
                            CoeusOptionPane.showInfoDialog(msg);
                            return oldValue; //fail in uniqueid check
                        }
                     }
                     else
                     {
                        return oldValue;//fail in dependency check
                     }
                }
             
            }*/
            
            
            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            if(selRow > -1) {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null) {
                    if (tblProtoActionCorresp.getValueAt(selRow,selColumn)!=null) {
                        if (!(tblProtoActionCorresp.getValueAt(selRow,selColumn).toString().
                        equals(((CoeusTextField)txtCell).getText()))) {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureProtoActionCorresp.getUserIndex() );
                            saveRequired = true;
                        }
                    }
                    else {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow),
                        tableStructureProtoActionCorresp.getUserIndex() );
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
            //System.out.println("*** get cell editor value:in itemStateChanged event  ***"  +txtCell.getText()) ;
            super.fireEditingStopped();
        }
        
        
        /** Supporting method used for validation.  There will be various
         * types of triggerValidation functions which take care of validation
         * for different types of data.
         */
        private boolean triggerValidation(String newValue) {
            //System.out.println("*** Validation row : " + selRow + " col : " + selColumn + " ***") ;
          /*
           * using the colIndex u can get the column bean for that column and then depending the
           * datatype of the column u apply appropriate validation rules*/
            
           /* if (!tableStructureProtoActionCorresp.isIdAutoGenerated()
                && (selColumn == tableStructureProtoActionCorresp.getPrimaryKeyIndex(1)))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue, selRow, selColumn))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "protocolFollowupAction_PKeyUniq_exceptionCode.2409");
            
                        CoeusOptionPane.showInfoDialog(msg);
                        return false; //fail in uniqueid check
                    }
                }
                else
                {
                    return false;//fail in dependency check
                }
            }    */
            if (!tableStructureProtoActionCorresp.isIdAutoGenerated()) {
                int colIdx1 = -1;
                int colIdx2 = -1;
                int colIdx3 = -1;
                Vector vecPrimaryKey = tableStructureProtoActionCorresp.getPrimaryKeyIndex();
                if ( vecPrimaryKey.size() == 1) {
                    colIdx1 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
                }else if ( vecPrimaryKey.size() == 2 ) {
                    colIdx1 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(1);
                }else if ( vecPrimaryKey.size() == 3 ) {
                    colIdx1 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(1);
                    colIdx3 = tableStructureProtoActionCorresp.getPrimaryKeyIndex(2);
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
    
    //Although there are two primary keys, the first primary key is protocol action.
    //Protocol action value will be the same for all the rows on a given screen,
    //and is an invisible column.
    private boolean CheckUniqueId(String newVal, int selRow, int selCol) {
        int rowTotal = tblProtoActionCorresp.getRowCount();
        int row = tblProtoActionCorresp.getEditingRow();
        if (rowTotal > 0 && row >= 0) {
            for (int index = 0; index < rowTotal ; index++) {
                if (index != row) {
                    Object val = codeTableModel.getValueAt(sorter.getIndexForRow(index), selCol+1);//+ 1 because first column is protocol action
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
        HashMap hmDependencyCheckValues = new HashMap();
        int colNumber = tableStructureProtoActionCorresp.getPrimaryKeyIndex(0);
        TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
        ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
        HashMap hmDependencyTables = (HashMap)tableStructureProtoActionCorresp.getHashTableDependency();
        int depenTabNum;
        
        if (hmDependencyTables == null) {
            return true; //there is no dependency need to check
        }
        
        //There are no dependent tables.
    /*
    depenTabNum = hmDependencyTables.size();
    for(int i = 0; i < depenTabNum ; i++)
    {
        HashMap hmDependency = (HashMap)hmDependencyTables.get(new Integer(i));
        RequestTxnBean requestTxnBean = new RequestTxnBean();
     
        if (tblStateTable.getValueAt(rowNumber, colNumber) == null)
        {
            return true;
        }
        String  ls_select = "select count(*) ";
        String  ls_from = "from " + hmDependency.get("Table");
        String  ls_where = " where " +hmDependency.get("Column") + " = '" + tblStateTable.getValueAt(rowNumber, colNumber).toString() + "' and country_code = '" + countryCode + "'" ;
        hmDependencyCheckValues.put("as_select", ls_select);
        hmDependencyCheckValues.put("as_from", ls_from);
        hmDependencyCheckValues.put("as_where", ls_where);
        requestTxnBean.setAction("DEPENDENCY_CHECK_STATE");
     
        requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
        Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
     
        if (success == null)
        {
            String msg = coeusMessageResources.parseMessageKey(
                            "chkDependencyFailed_exceptionCode.2406");
     
            CoeusOptionPane.showInfoDialog(msg);
            return false;
        }
     
     
        if (success.booleanValue() != true)
        //there is dependency in other table.
        {
            String msg = coeusMessageResources.parseMessageKey(
                                    "chkPKeyDependency_exceptionCode.2403");
            String msg1;
     
            if (from == "Delete")
            {
                    msg1 = coeusMessageResources.parseMessageKey(
                                    "chkPKeyDependency_exceptionCode.2404");
            }
            else
            {
                    msg1 = coeusMessageResources.parseMessageKey(
                                    "chkPKeyDependency_exceptionCode.2405");
            }
     
            String tableName =  hmDependency.get("Table").toString() ;   //tableStructureBean.getActualName();
            CoeusOptionPane.showInfoDialog(msg + tableName + msg1 );
            //once found the dependency, just return
            return false;
        }
    }*/
        return true;
    }
    
    
    
    private void GetLastValue() {
        int row = tblProtoActionCorresp.getEditingRow();
        int col = tblProtoActionCorresp.getEditingColumn();
        if (row != -1 && col != -1) {
            tblProtoActionCorresp.getCellEditor(row, col).stopCellEditing();
            TableColumn column = codeTableColumnModel.getColumn(col+1) ; //here col +1 ,because correspondence type code is second col in codeTableColumnModel, is first col in tblProtoActionCorresp
            //make sure to set AC_TYPE
            column.getCellEditor().getCellEditorValue();
        }
        
    }
    
}  //end class
