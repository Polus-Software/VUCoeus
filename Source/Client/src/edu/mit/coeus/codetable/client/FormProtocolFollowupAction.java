/*
 * FormProtocolFollowupAction.java
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


public class FormProtocolFollowupAction extends CoeusDlgWindow
{
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblFollowupAction;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private CoeusComboBox cmbProtocolAction = new CoeusComboBox();
    private javax.swing.JScrollPane scrlpnlMain;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlFollowupAction;
    private javax.swing.JPanel pnlProtocolAction;
    private javax.swing.JSplitPane splitpnlMain;
    private javax.swing.JButton btnSave;
    private JLabel lblAction;
    private JLabel lblFollowupAction;
    private GridBagConstraints gridBagConstraints;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;  
    
    /* This is used to know whether data need reflash or not */
    private boolean cancelRequired = false;
    
//    private TableSorter sorter ;
    private TableSorterCodeTable sorter ;
    
    ExtendedDefaultTableModel codeTableModel ; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tableStructureProtocolActionType;
    TableStructureBean tableStructureProtoActionAction;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ; 
    int numColumnsDisplay = 0 ;
    String protocolActionCode = new String();
    Vector vecDeletedRows = new Vector();
    Vector vectProtocolAction = null;
    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
    /** Creates a new instance of FormProtocolFollowupAction */
    public FormProtocolFollowupAction(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean)
    {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights)
            formatFields();
        cmbProtocolAction.setFont(CoeusFontFactory.getLabelFont());
        cmbProtocolAction.setSelectedItem("100") ; // Protocol Created
    }
    
     public void initComponents() 
     {
       coeusMessageResources = CoeusMessageResources.getInstance();
       
       lblAction = new JLabel("Action");
       lblFollowupAction = new JLabel("Followup Action");
       lblAction.setFont(CoeusFontFactory.getLabelFont());
       lblFollowupAction.setFont(CoeusFontFactory.getLabelFont());
       
       pnlFollowupAction = new JPanel(new GridBagLayout()) ;
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
       pnlProtocolAction.add(lblFollowupAction, gridBagConstraints) ;
        
        initialiseData() ;
         
//        sorter = new TableSorter() ; 
        sorter = new TableSorterCodeTable() ; 
        tblFollowupAction = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblFollowupAction); 
        
        tblFollowupAction.setRowHeight(22);
        tblFollowupAction.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
                    = tblFollowupAction.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));

        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblFollowupAction.getTableHeader(), BorderLayout.NORTH) ; 
        pnlTable.add(tblFollowupAction, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
                       
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlProtocolAction, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
              
        cmbProtocolAction.addItemListener(new ItemListener()
        {
             public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {   
                    GetLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbProtocolAction.getSelectedItem() ;
                    String newProtocolActionCode = cmb.getCode();
                    if (protocolActionCode != newProtocolActionCode && saveRequired)
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                                                        "saveConfirmCode.1002"); 
                        int confirm = CoeusOptionPane.showQuestionDialog(msg,
                                    CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                                    CoeusOptionPane.DEFAULT_YES);
                        if(confirm == JOptionPane.YES_OPTION)
                        {
                           if (!saveData()) 
                           {
                               /* CASE #555 Begin */
                               //There are errors to be corrected before leaving this protocol action.
                               cancelRequired = true; // don't repaint the table with original data from the retrieve.
                               cmbProtocolAction.setSelectedItem(protocolActionCode) ;
                               /* CASE #555 End */
                               return;   
                           }
                        }else if (confirm == JOptionPane.CANCEL_OPTION)                           
                        {
                            cancelRequired = true;
                            cmbProtocolAction.setSelectedItem(protocolActionCode) ; 
                            return;
                        }
                   
                    }
                    if (cancelRequired)
                    {
                        cancelRequired = false;
                        return;
                    }
                    saveRequired = false;
                    protocolActionCode = newProtocolActionCode ;
                    //System.out.println("set protocolActionCode to: "+protocolActionCode);
                    if (protocolActionCode.equals(""))
                    {
                        // hide the table
                        tblFollowupAction.setVisible(false) ;
                    }        
                    else
                    {
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
       pnlFollowupAction.add(splitpnlMain, gridBagConstraints) ;
       gridBagConstraints = new java.awt.GridBagConstraints();
       gridBagConstraints.ipadx = 23;
       gridBagConstraints.ipady = 75;
       gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
       pnlFollowupAction.add(pnlForBtn, gridBagConstraints) ;
       
       getContentPane().add(pnlFollowupAction, BorderLayout.CENTER);
       pack() ;   
       
         // this will take care of the window closing...
       this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
           public void windowClosing (WindowEvent event)
           {
               btnCloseActionPerformed() ;
           } 
        } );  
     }
     
    
    private void formatFields()
    {
//        tblFollowupAction.setEnabled(false);
//        cmbProtocolAction.setEnabled(false);
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
    }
     
     public void btnAddActionPerformed(java.awt.event.ActionEvent evt) 
    {
        Object[]  oneRow = {" "};
         
        sorter.insertRow(sorter.getRowCount(),oneRow);
        // increment action number by 1, or if table has no rows, set to 1.
        int maxActionInt = 0;
        if(codeTableModel.getRowCount() > 1)
        {  
            //Get the max action number.
            for(int rowIndex = 0; rowIndex < codeTableModel.getRowCount()-1; rowIndex++)
            {
                String actionNumberStr = codeTableModel.getValueAt(sorter.getIndexForRow(rowIndex), 1).toString();
                Integer actionNumber = new Integer(actionNumberStr);
                int tempActionNumber = actionNumber.intValue();
                if(tempActionNumber > maxActionInt)
                {
                    maxActionInt = tempActionNumber;
                }
            }
        }
        Integer newActionNumber = new Integer(maxActionInt +1);
        codeTableModel.setValueAt(newActionNumber, codeTableModel.getRowCount()-1, 1);         
       // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, 
                    tableStructureProtoActionAction.getDuplicateIndex(0) );
        //Set the protocol action code with the user selection.
        codeTableModel.setValueAt(protocolActionCode, codeTableModel.getRowCount()-1, 0); 
        //set the followup action code
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, 2 );
        
      // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, 
            codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name 
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, 
            tableStructureProtoActionAction.getUserIndex() );       
        tblFollowupAction.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
        tblFollowupAction.editCellAt(codeTableModel.getRowCount()-1,  0) ;
        tblFollowupAction.requestFocus();
    }
    
     public void btnSaveActionPerformed()
    {
        Object temp = null;
        int  selectedRow = tblFollowupAction.getSelectedRow();
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
            for (int i = 0; i < tblFollowupAction.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblFollowupAction.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblFollowupAction.getRowCount() -1)
                        tblFollowupAction.setRowSelectionInterval(0,0);
                }
            }
        }
//        if (selectedRow > 0)
//            tblFollowupAction.setRowSelectionInterval(selectedRow,selectedRow);   
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) 
    {
        int rowNum = tblFollowupAction.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblFollowupAction.getEditingColumn() >= 0 )
        {
           tblFollowupAction.getCellEditor().stopCellEditing(); 
        }
        
        //here need to check dependency first
        if (!checkDependency(rowNum, "Delete"))
        {
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
        //Don't need reset action numbers functionality
        /*if(rowNum != codeTableModel.getRowCount() -1)
        {
            int resetActionNumbers = CoeusOptionPane.showQuestionDialog(
                                                message,
                                                CoeusOptionPane.OPTION_YES_NO,
                                                CoeusOptionPane.DEFAULT_YES);   
            if(resetActionNumbers == JOptionPane.YES_OPTION)
            {
                String actionNumberToRemove = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), 1).toString();
                int numberOfRowsAfter = tblFollowupAction.getRowCount()- rowNum;
                for(int i=rowNum +1; i<tblFollowupAction.getRowCount(); i++)
                {
                    String actionNum = codeTableModel.getValueAt(i,1).toString();
                    int newActionNum = (Integer.parseInt(actionNum) -1);
                    codeTableModel.setValueAt(new Integer(newActionNum), i, 1);
                }
            }
        }    */    
        //keep all deleted rows in vecDeletedRows.
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),codeTableModel.getColumnCount()-1) != "I")
        {            //if not new inserted row, come to here and set AC_tYPE to "D"
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
            tblFollowupAction.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }
         
        if (tblFollowupAction.getRowCount() > 0  )
        {
            tblFollowupAction.changeSelection(rowNum,  1, false, false) ; 
        }         
    }
    
    public void btnCloseActionPerformed() 
    {
        GetLastValue();
         if ( saveRequired )
        {    
            String msg = coeusMessageResources.parseMessageKey(
                                                        "saveConfirmCode.1002"); 
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                                    CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                                    CoeusOptionPane.DEFAULT_YES);
            
             switch(confirm)
             {
                case(JOptionPane.YES_OPTION):
                    try
                    {
                        if (!saveData()) 
                        {
                            return;   
                        }
                        this.dispose();
                    }catch(Exception ex)
                     {
                        ex.printStackTrace();
                        String exMsg = ex.getMessage();
                                CoeusOptionPane.showWarningDialog(exMsg);
                    }
                case(JOptionPane.NO_OPTION):
                    this.dispose();
            }
                 
      }
      else
      {
        this.dispose();
      }
            
    }
    
   //copy one table row to a hashmap
   private HashMap getTableRow(int rowNum)
   {
        HashMap hashRow = new HashMap() ;
        int colTotal = codeTableModel.getColumnCount()-1;
        for (int colCount=0; colCount <= codeTableModel.getColumnCount()-1; colCount++)
        { // for each column u will build a hashmap 
            TableColumn column = codeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            Object val = null ;
            // the whole idea of setting up the columnBean as the identifier is that u get to know
            // all abt the structure details of the column, so u can create appropriate type of object
            // and stick it in to the hashMap
            // the whole idea of setting up the columnBean as the identifier is that u get to know
            // all abt the structure details of the column, so u can create appropriate type of object
            // and stick it in to the hashMap
            if (columnBean.getDataType().equals("NUMBER"))
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) == null
                        ||(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).equals("")))
                {  
                    val = null ; //new Integer(0) 
                }
                else
                {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount);
                    if (val instanceof ComboBoxBean)
                    {
                        if (((ComboBoxBean)val).getCode().equals(""))// user deleted selection
                            val = null;
                        else
                            val = new Integer(((ComboBoxBean)val).getCode().toString());
                    }
                    else
                    {
                        val = new Integer(val.toString()) ;
                    }
                }    
            }
            
            if (columnBean.getDataType().indexOf("VARCHAR2") != -1)
            {              
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount)==null)
                {
                    val = "";
                }
                else
                {
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString() ;
                }
            }
                
            if (columnBean.getDataType().equals("DATE"))
            {
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
                else
                {  // for update
                   // there will be only two dates in any table one AV_UPDATE_TIMESTAMP and the other one AW_UPDATE_TIMESTAMP
                    if (columnBean.getQualifier().equals("VALUE"))
                    {  //AV_...
                        //coeusdev-568 start
                        //val = new java.sql.Timestamp(today.getTime());
                        val = CoeusUtils.getDBTimeStamp();
                        //coeusdev-568 end
                    }
                    else
                    {  //AW_...
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
   
   private boolean saveData()
  {
      
     // do the validation b4 u go and build the vector of modified rows
      if (tableDataValid())
       { 
                // get the modified rows
                Vector vecModifiedRows = getModifiedRows() ;
                if (vecModifiedRows != null)
                {
                    System.out.println("obtd modified rows successfuly") ;
                }

                HashMap hashStoredProcedure = 
                    (HashMap)tableStructureProtoActionAction.getHashStoredProceduresForThisTable();
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
                  else
                  {//Data Saved Successfully
                      saveRequired = false;
                      return true;
                  }

      }// end if data validation
      else
      {
          return false;
      }
  }
   
     // this function should do the validation of rows
   private boolean tableDataValid()
   { // prps check
    // if there are any form level validations that should go there
        int rowTotal = tblFollowupAction.getRowCount();
        int colTotal = tblFollowupAction.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModel.getColumn(colIndex + 1) ;//becasue protocol action is first column
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblFollowupAction.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblFollowupAction.getValueAt(rowIndex,colIndex).equals("")
                            || tblFollowupAction.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {   
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblFollowupAction.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402"); 
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblFollowupAction.changeSelection(rowIndex,  colIndex, false, false) ;
                        return false;
                    }
                }
                //Check that values for Protocol Action and Protocol Followup Action are not the same
                if( colIndex == 1){//protocol followup action
                    ComboBoxBean followupActionCombo = 
                        (ComboBoxBean)tblFollowupAction.getValueAt(rowIndex, 1);
                    String followupActionCode = followupActionCombo.getCode();
                    if( protocolActionCode.equals(followupActionCode)){
                        CoeusOptionPane.showInfoDialog("protocolFollowupAction_exceptionCode.2407");
                        return false;
                    }
                }
            }           
        }   
        return true ;
   }
   
   // this method will check the AC_Type field of all the rows in the table and build a vector of modified
   // rows. modified rows include newly inserted rows as well
   private Vector getModifiedRows()
   {
      Vector vecUpdatedRows = new Vector();
      int rowCount = 0;
      
      //Delete the rows to be deleted before inserting or updating rows, in order to avoid primary key constraint violation.
      if (vecDeletedRows.size() > 0 )
      {//append deleted rows to vecUpdatedRows
          for (int row = 0; row < vecDeletedRows.size(); row++)
          {
              vecUpdatedRows.add(vecDeletedRows.get(row));
          }          
      }
      
      while(rowCount < tblFollowupAction.getRowCount())
      {// check AC_TYPE field
        String tmpACType ;
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowCount), codeTableModel.getColumnCount()-1) != null)
        {
            vecUpdatedRows.add(getTableRow(rowCount)) ;
        }
        rowCount++ ;  
      }//end while
   
      return vecUpdatedRows ;
   }
 
     public void initialiseData()
     {                
        hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure() ;        
        TableStructureBean tempTableStructBean = null ;
        for (int tableCount =0 ; tableCount < hashAllCodeTableStructure.size() ; tableCount++)
        {    
            tempTableStructBean  = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(tableCount)) ;
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$protocol_action_type"))
            {
                tableStructureProtocolActionType = tempTableStructBean ;
            }    
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$valid_proto_action_action"))
            {
                tableStructureProtoActionAction = tempTableStructBean ;
            }
         }// end for  
        
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
     
     
     public Vector drawTableUsingTableStructureBean() 
    {
        vecDeletedRows.clear();
        codeTableModel = new ExtendedDefaultTableModel() ;
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureProtoActionAction.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
        numColumnsDisplay = tableStructureProtoActionAction.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNames = new String[numColumnsDisplay + 1];
        // create a table colunmodel thing can used to access Table Column directly
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        
        int lastDisplayCol = 0;
        
        // populate the column header name on the the screen
        if (numColumnsDisplay > 0)
        {
            for (int i = 0; i < numColumnsDisplay; i++)
            {   
                //to add columns
              ColumnBean newColumnBean = (ColumnBean)hmTableColumnBean.get(new Integer(i)) ;
              TableColumn newColumn = new TableColumn(i, newColumnBean.getDisplaySize()) ; // set the model index and width
              newColumn.setIdentifier(newColumnBean) ; // set the column bean itself as identifier
              newColumn.setHeaderValue(newColumnBean.getDisplayName()) ;
              
              if (newColumnBean.getColumnVisible())
              {
                  lastDisplayCol = i;
              }
              
              if (i == 2)//protocol followup action
              {
                  HashMap hmRow = new HashMap();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  if (vectProtocolAction == null) continue;
                  comb.addItem(new ComboBoxBean("", "")) ;
                  for (int j=0; j < vectProtocolAction.size(); j++) //loop for num of rows
                  {
                        hmRow = (HashMap)vectProtocolAction.elementAt(j);
                        comb.addItem
                            (new ComboBoxBean(hmRow.get("PROTOCOL_ACTION_TYPE_CODE").toString() ,
                            hmRow.get("DESCRIPTION").toString()));
                   }//end for  
                   newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
              }
              
              else if(i == 3)
              {
                  CoeusComboBox comb = new CoeusComboBox();
                  comb.addItem(new ComboBoxBean("N", "N"));
                  comb.addItem(new ComboBoxBean("Y", "Y"));
                  newColumn.setCellEditor(new ExtendedCellEditor(comb));
              }
              
              else
              {    
                 newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean, i)) ;
              }
              if (newColumnBean.getDisplaySize()<=5)
              {    
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 40) ;
              }   
              else if (newColumnBean.getDisplaySize()>5 && newColumnBean.getDisplaySize() <= 15)
              {    
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 30) ;
              }
              else if (newColumnBean.getDisplaySize()>15 && newColumnBean.getDisplaySize() <= 30)
              {    
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 20) ;
              }
              else if (newColumnBean.getDisplaySize()>30 && newColumnBean.getDisplaySize() <= 50)
              {    
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 12) ;
              }
              else
              {
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
            TableColumn newColumn = codeTableColumnModel.getColumn(lastDisplayCol) ;  
            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"                                               "); 
              
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureProtoActionAction and get the DataBean from the servlet.
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
                     if (j == 2) // protocol follow up action
                     {
                         if (vectProtocolAction != null)
                         {
                             if (rowColumnDatas[j] != null)
                             {
                                String temp = rowColumnDatas[j].toString();
                            /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectProtocolAction.size(); codeIndex++)
                                {
                                    HashMap hmRow = (HashMap)vectProtocolAction.elementAt(codeIndex);                                      
                                        if (hmRow.get("PROTOCOL_ACTION_TYPE_CODE").toString().equals(temp))
                                        {
                                            rowColumnDatas[j] = new ComboBoxBean
                                            (hmRow.get("PROTOCOL_ACTION_TYPE_CODE").toString() , 
                                            hmRow.get("DESCRIPTION").toString());
                                            break;
                                        }
                                }
                             }
                         } 
                         else if(j == 3)
                         {
                            CoeusComboBox comb = new CoeusComboBox();
                            comb.addItem(new ComboBoxBean("N", "N"));
                            comb.addItem(new ComboBoxBean("Y", "Y"));
                            newColumn.setCellEditor(new ExtendedCellEditor(comb));
                         }
                     }
                    
                    Object obj = rowColumnDatas[j];   
                    //System.out.println("column: "+j+", "+columnBean.getDisplayName()+ " "+obj);                    
                    if (columnBean.getColumnVisible())
                    {    
                         if (rowColumnDatas[j] !=null)
                         {    
                            if (rowColumnDatas[j].equals("null"))
                            {
                                rowColumnDatas[j] = "";
                            }
                         }
                         else
                         {
                            rowColumnDatas[j] = null ; 
                         }    
                    }
                }
                
                codeTableModel.addRow(rowColumnDatas);
            }            
             
       } 
      
        sorter.setModel(codeTableModel,false) ;
        tblFollowupAction.setModel(sorter) ;
        
        tblFollowupAction.setColumnModel(displayCodeTableColumnModel); // also give the structure
       
       // tblCodeTable.addFocusListener(this) ;        
        tblFollowupAction.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
            // hide unwanted columns
            int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
            //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
            for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)    
            {  
                TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

                if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
                {
                    TableColumnModel columnModel = tblFollowupAction.getColumnModel();
                    TableColumn inColumn = columnModel.getColumn(colCount );
                    tblFollowupAction.removeColumn(inColumn ); 
                }
            }
            if (tblFollowupAction.getRowCount() > 0)
            {
                tblFollowupAction.setRowSelectionInterval(0,0) ;
                tblFollowupAction.repaint() ;
            }
        
       return vdata;
    }
    
          
        
    private void getDataForTheTable()
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectData = null;
      if(tableStructureProtoActionAction == null)
        System.out.println("tableStructureProtoActionAction == null");
      //Get all stored procedures associated with this table.
      HashMap hashStoredProcedure =
        (HashMap)tableStructureProtoActionAction.getHashStoredProceduresForThisTable();
      if(hashStoredProcedure == null)
        System.out.println("hashStoredProcedure for ProtoActionAction == null");
      //Get the select stored procedure associated witht this table.
      StoredProcedureBean selectStoredProcedure =
        (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));

      RequestTxnBean requestTxnBean = new RequestTxnBean();
      requestTxnBean.setAction("GET_DATA");
      requestTxnBean.setStoredProcedureBean(selectStoredProcedure);

      Vector param= new Vector();
      param.add("osp$valid_proto_action_action");
      param.add("AW_PROTOCOL_ACTION_TYPE_CODE");
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
      if (responder.isSuccessfulResponse())
     {
         accDataBean = (DataBean)responder.getDataObject() ;
     }

      if(accDataBean != null)
      {
        System.out.println("*** Recvd a DataBean ***");
        vectData = accDataBean.getVectData();
        System.out.println("vectData.size() in GUI: "+vectData.size());
      }
          
     accDataBean.setVectData(vectData);   
           
  }     
  
    private Vector getDataForCombo(TableStructureBean tableStructureBean)
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectData = null;
      try
      {
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
          if (responder.isSuccessfulResponse())
         {
             accDataBean = (DataBean)responder.getDataObject() ;
         }
          
          if(accDataBean != null)
          {
             vectData = accDataBean.getVectData();
          }          
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
        
     }
      
    return  vectData ;
  } 
  
  private Object getDataFromServlet(Object request)
    {
     Object response = null ;
     try
     { 
         RequesterBean requester = new RequesterBean();
         requester.setDataObject(request) ;
         
         String connectTo =CoeusGuiConstants.CONNECTION_URL
            + CODE_TABLE_SERVLET ;
        AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        
        ResponderBean responder = comm.getResponse();
        if (responder.isSuccessfulResponse())
        {
            response = responder.getDataObject() ;
        }    
           
       }catch(Exception ex)
       {
        System.out.println("  *** Error in Applet Servlet communication ***  ") ; 
        ex.printStackTrace() ;
       }

       return response ;
   }
   
  class ExtendedCellEditor extends DefaultCellEditor implements TableCellEditor
  {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        public ExtendedCellEditor(ColumnBean columnBeanIn, int colIndex)
        {
            super( new CoeusTextField() );

            if (columnBeanIn.getDataType().equals("NUMBER"))
            {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,columnBeanIn.getDisplaySize( )));
            }
            else
            {
                if (columnBeanIn.getDataType().equals("UVARCHAR2"))
                {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, columnBeanIn.getDisplaySize( )));
                }
                else
                {
                    txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));
                }
            }

                txtCell.addFocusListener(new FocusAdapter()
                    {
                     
                        public void focusGained(FocusEvent e)
                        {
                           // System.out.println("*** focus Gained ***") ; 
                        }
                       
                        public void focusLost(FocusEvent fe)
                        {
                            if (!fe.isTemporary())
                            {
                                System.out.println("*** focus lost ***") ; 
                                GetLastValue();
                            }// end if
                            }// end focus lost
                        }); // end focus listener
             
        }  
        
        public ExtendedCellEditor(CoeusComboBox comb)
        {
            super(comb);
            comb.addFocusListener(new FocusAdapter()
            {
                       
                public void focusGained(FocusEvent e)
                {
                  // System.out.println("*** combo box focus Gained ***") ; 
                }
                       
                public void focusLost(FocusEvent fe)
                {
                    //System.out.println("***combo box focus Lost ***"); 
                    if (!fe.isTemporary())
                     {
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
        int column)
        {
            selRow = row ;
            selColumn = column ;
            
            if (column == 1 || column == 2 )//protocol followup action or user prompt flag
            {
                if (value  instanceof ComboBoxBean)
                {
                        //System.out.println(column+": value is a ComboBoxBean");
                        ComboBoxBean cmbTmp = (ComboBoxBean)value ; 
                        Object objTmp = cmbTmp.getCode() ;
                        CoeusComboBox CompTmp = (CoeusComboBox)getComponent();
                        CompTmp.setSelectedItem(objTmp);
                        return (Component)CompTmp;
                       
                 }  
                else
                {
                    //System.out.println("column: "+column+": value is CoeuscomboBox");
                    return (Component)((CoeusComboBox)getComponent());
                }
            }
            
            
            if (value != null)
            {
                txtCell.setText(value.toString()) ;
            }
            else
            {  
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
                tblFollowupAction.changeSelection(selRow,  1, false, false) ; 
                return false ;
            }    
        }*/
        
        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
         public Object getCellEditorValue() {
             
            //Object oldValue = tblStateTable.getValueAt(selRow,selColumn);
            //Object newValue = ((CoeusTextField)txtCell).getText();              
             
            Object oldValue = tblFollowupAction.getValueAt(selRow,selColumn);
            Object newValue = null;
            if(selColumn == 1 || selColumn == 2)
            {
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            else
            {
                newValue=((CoeusTextField)txtCell).getText();
            }
            if(!triggerValidation(newValue.toString()))
            {
                return oldValue;
            }             

            
            /*if (!tableStructureBeanState.isIdAutoGenerated() 
                && (selColumn == tableStructureBeanState.getPrimaryKeyIndex(0)))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString()))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "chkPKeyUniqVal_exceptionCode.2401");
               
                        CoeusOptionPane.showInfoDialog(msg);
                //after failure of checking, make sure selecting the failed row
                //but following code does not work out.so comment out ????????????????????????????????
                       return oldValue; //fail in uniqueid check
                    }
                }
                else
                {
                    return oldValue;//fail in dependency check
                }
                    
            }*/
            
            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            if(selRow > -1)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblFollowupAction.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblFollowupAction.getValueAt(selRow,selColumn).toString().
                            equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name 
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureProtoActionAction.getUserIndex() );
                            saveRequired = true;
                        }
                    }
                    else
                    { 
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name 
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), 
                            tableStructureProtoActionAction.getUserIndex() );
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
        
        public void itemStateChanged(ItemEvent e) 
        {
             //System.out.println("*** get cell editor value:in itemStateChanged event  ***"  +txtCell.getText()) ;
             super.fireEditingStopped();
        }


        // there will various types of triggervalidation functions which takes care
        // of validation for different types of data.
        private boolean triggerValidation(String newValue)
        {           
            //System.out.println("*** Validation row : " + selRow + " col : " + selColumn + " ***") ;
          /*
           * using the colIndex u can get the column bean for that column and then depending the 
           * datatype of the column u apply appropriate validation rules*/
           
           /* if (!tableStructureProtoActionAction.isIdAutoGenerated() 
                && (selColumn == tableStructureProtoActionAction.getPrimaryKeyIndex(1)))
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
            if (!tableStructureProtoActionAction.isIdAutoGenerated())
            {
                int colIdx1 = -1;
                int colIdx2 = -1;
                int colIdx3 = -1;
                Vector vecPrimaryKey = tableStructureProtoActionAction.getPrimaryKeyIndex();
                if ( vecPrimaryKey.size() == 1)
                {
                    colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(0);
                }else if ( vecPrimaryKey.size() == 2 )
                {
                    colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(1);
                }else if ( vecPrimaryKey.size() == 3 )
                {
                    colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(0);
                    colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(1);
                    colIdx3 = tableStructureProtoActionAction.getPrimaryKeyIndex(2);
                }
                if (selColumn == colIdx1 || selColumn == colIdx2 || selColumn == colIdx3)
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
                }
                if(selColumn == 1)//protocolFollowupAction
                {
                    ComboBoxBean newValueCombo = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
                    String newValueCode = newValueCombo.getCode();
                    if(newValueCode.equals(protocolActionCode))
                    {
                        System.out.println("equals protocolActionCode");
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

   
     
 class ExtendedDefaultTableModel extends DefaultTableModel   
 {
    
    public ExtendedDefaultTableModel()
    {
        
    }
   
     public boolean isCellEditable(int row, int col)
     {
        if (!userHasRights){
            return false;
        }else{
            TableColumn column = codeTableColumnModel.getColumn(col) ;
            ColumnBean columnBean = (ColumnBean) column.getIdentifier();
            return columnBean.getColumnEditable() ;
        }
        
     }
     
          
 }// end inner class
 
  private boolean CheckUniqueId(String newVal, int selRow, int selCol)
 {
    //System.out.println("inside CheckUniqueId");
    int rowTotal = tblFollowupAction.getRowCount();
    int row = tblFollowupAction.getEditingRow();
    if (rowTotal > 0 && row >= 0)
    {   
        Vector vecPrimaryKey = tableStructureProtoActionAction.getPrimaryKeyIndex();
        /*if (vecPrimaryKey.size() == 1 )
        {
            int colIdx = tableStructureProtoActionAction.getPrimaryKeyIndex(0) ;    
    
            for (int index = 0; index < rowTotal ; index++)
            {
                if (index != row)
                {
                    Object val = tblFollowupAction.getValueAt(index, colIdx); 
                    if (val instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val ; 
//                        val = cmbTmp.getCode() ;
                        val = cmbTmp.getDescription();
                    }  
                    
                    if (val != null)
                    {
                        if (newVal.toString().equals(val.toString()))
                            return false;
                    }
                
                }
            }
        }
        else if  (vecPrimaryKey.size() == 2)
        {   
            int colIdx1 ;    
            int colIdx2 ; 
            //keep selected column number in colIdx1
            if (selCol == tableStructureProtoActionAction.getPrimaryKeyIndex(0))
            {
                colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(0) ;    
                colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(1) ;  
            }
            else
            {
                colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(1) ;    
                colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(0) ;  
            }
            
            Object valTemp = tblFollowupAction.getValueAt(selRow, colIdx2);
            if ( valTemp instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp ; 
                valTemp = cmbTmp.getCode() ;
            }
            for (int index = 0; index < rowTotal ; index++)
            {
                if (index != row)
                {
                    Object val1 = tblFollowupAction.getValueAt(index, colIdx1);
                    Object val2 = tblFollowupAction.getValueAt(index, colIdx2);
                    if (val1 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val1 ; 
//                        val1 = cmbTmp.getCode() ;
                        val1 = cmbTmp.getDescription();
                        
                    }
                    
                    if (val2 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val2 ; 
                        val2 = cmbTmp.getCode() ;
                        
                    }  
                    
                    if (val1 != null && val2 != null)
                    {
                        if (newVal.toString().equals(val1.toString()) && valTemp.toString().equals(val2.toString()))
                            return false;
                    }
                }
            }
        }*/
        //else if(vecPrimaryKey.size() == 3)
        //{
        //Modify to accomodate first primary key (of the 3 primary keys) is an invisible column. -LMR
        int colIdx1 = -1;    
        //int colIdx2; 
        int colIdx3 = -1; 
        //keep selected column number in colIdx1
        /*if (selCol == tableStructureProtoActionAction.getPrimaryKeyIndex(0))
        {
            colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(0) ;    
            colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(1) ; 
            colIdx3 = tableStructureProtoActionAction.getPrimaryKeyIndex(2) ;
        }*/
        if (selCol == (tableStructureProtoActionAction.getPrimaryKeyIndex(1)-1))
        {
            colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(1) -1 ; //action number   
            //colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(0) ; // Old and new value for protocol action are always the same
            colIdx3 = tableStructureProtoActionAction.getPrimaryKeyIndex(2) -1; // protocl followup action
        }
        else if(selCol == (tableStructureProtoActionAction.getPrimaryKeyIndex(2)-1))
        {
            colIdx1 = tableStructureProtoActionAction.getPrimaryKeyIndex(2) -1;// protocol followup action 
            //colIdx2 = tableStructureProtoActionAction.getPrimaryKeyIndex(0) ; //Old and new value for protocol action are always the same.
            colIdx3 = tableStructureProtoActionAction.getPrimaryKeyIndex(1) -1; // action number
        } 
        else
        {
            return true;
        }
        //Object valTemp = tblFollowupAction.getValueAt(selRow, colIdx2); //Old and new value for protocol action are always the same.
        Object valTemp2 = tblFollowupAction.getValueAt(selRow, colIdx3);

        /*if ( valTemp instanceof ComboBoxBean)
        {
            ComboBoxBean cmbTmp = (ComboBoxBean)valTemp ; 
            valTemp = cmbTmp.getCode() ;
        }*/

        if ( valTemp2 instanceof ComboBoxBean)
        {
            ComboBoxBean cmbTmp = (ComboBoxBean)valTemp2 ; 
            valTemp2 = cmbTmp.getCode();
        }
        //System.out.println("newVal: "+newVal);             
        //System.out.println("valTemp2: "+valTemp2);                        

        for (int index = 0; index < rowTotal ; index++)
        {
            if (index != row)
            {
                Object val1 = tblFollowupAction.getValueAt(index, colIdx1);
                //Object val2 = tblFollowupAction.getValueAt(index, colIdx2); // Old and new value for protocol action are always the same.
                Object val3 = tblFollowupAction.getValueAt(index, colIdx3);

                if (val1 instanceof ComboBoxBean)
                {
                    ComboBoxBean cmbTmp = (ComboBoxBean)val1 ; 
//                        val1 = cmbTmp.getCode() ;
                    val1 = cmbTmp.getDescription() ;//because the newVal is description                        
                }                      
                /*if (val2 instanceof ComboBoxBean)
                {
                    ComboBoxBean cmbTmp = (ComboBoxBean)val2 ; 
                    val2 = cmbTmp.getCode() ;

                } */
                if (val3 instanceof ComboBoxBean)
                { 
                    ComboBoxBean cmbTmp = (ComboBoxBean)val3;
                    val3 = cmbTmp.getCode() ;
                }  
               //System.out.println("val1: "+val1);
               //System.out.println("val3: "+val3);

                /*if (val1 != null && val2 != null && val3 != null && valTemp != null && valTemp2 != null)
                {
                    if (newVal.toString().equals(val1.toString()) && 
                        valTemp.toString().equals(val2.toString()) &&
                        valTemp2.toString().equals(val3.toString()))
                        return false;                            
                }*/
                if (val1 != null && val3 != null  && valTemp2 != null)
                {
                    if (newVal.toString().equals(val1.toString()) && 
                        //valTemp.toString().equals(val2.toString()) &&
                        valTemp2.toString().equals(val3.toString()))
                        return false;                            
                }                   
            }
        }
    }           
    //}
    return true;
}

private boolean checkDependency(int rowNumber, String from)
{
    HashMap hmDependencyCheckValues = new HashMap();
    int colNumber = tableStructureProtoActionAction.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureProtoActionAction.getHashTableDependency();
    int depenTabNum; 
    
    if (hmDependencyTables == null)
    {    
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
 


 private void GetLastValue()
 {
    int row = tblFollowupAction.getEditingRow();
    int col = tblFollowupAction.getEditingColumn();
    if (row != -1 && col != -1) 
    {
        tblFollowupAction.getCellEditor(row, col).stopCellEditing();
        TableColumn column = codeTableColumnModel.getColumn(col + 1) ; //here col +1 ,because state code is second col in codeTableColumnModel, is first col in tblFollowupAction
        //make sure to set AC_TYPE
        column.getCellEditor().getCellEditorValue();
    }

 }
     
}  //end class
