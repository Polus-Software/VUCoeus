/*
 * FormEDIEnabledSpronsor.java
 *
 * Created on March 17, 2003, 12:50 PM
 */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.gui.* ;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.sponsormaint.gui.* ;
import edu.mit.coeus.sponsormaint.bean.* ;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.Date;
import java.beans.*;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAboutForm ;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.search.gui.* ;

public class FormEDIEnabledSponsor extends CoeusDlgWindow
{
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblEDIEnabledSponsor;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFind;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlForState;
   
    private javax.swing.JButton btnSave;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /* This is used to know whether data is modified or add  */
    private boolean saveRequired = false;  
    
//    private TableSorter sorter ;
    private TableSorterCodeTable sorter ;
    
    ExtendedDefaultTableModel codeTableModel ; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tableStructureBeanEDI ;
    TableStructureBean tableStructureBeanProposal ;
    TableStructureBean tableStructureBeanNotice ;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    CoeusComboBox cmbProposalType = new CoeusComboBox() ;
    CoeusComboBox cmbNotice = new CoeusComboBox() ;
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ; 
    int numColumnsDisplay = 0 ;
    Vector vecDeletedRows = new Vector(); 
    
    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
   private static final String SPONSOR_SEARCH = "sponsorSearch";
   
   private Frame parent;
    
    /** Creates a new instance of FormEDIEnabledSpronsor */
    public FormEDIEnabledSponsor(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean)
    {
        super(parent, title, modal);
        this.parent = parent ;
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights)
            formatFields(); 
        
        
    }
  
     public void initComponents() 
     {
       coeusMessageResources = CoeusMessageResources.getInstance();
       
        pnlForState = new JPanel(new GridBagLayout()) ; 
        initialiseData() ;
//        sorter = new TableSorter() ; 
        sorter = new TableSorterCodeTable() ;
        tblEDIEnabledSponsor = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblEDIEnabledSponsor); 

        tblEDIEnabledSponsor.setRowHeight(22) ;
        tblEDIEnabledSponsor.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        
        pnlTable.add(tblEDIEnabledSponsor.getTableHeader(), BorderLayout.NORTH) ; 
        pnlTable.add(tblEDIEnabledSponsor , BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
     
        tblEDIEnabledSponsor.addMouseListener( new SponsorDisplayAdapter());
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
                      
         
       java.awt.GridBagConstraints gridBagConstraints;
       
       pnlForBtn = new JPanel(new GridBagLayout()) ;
       btnAdd = new JButton("Add") ;
       btnSave = new JButton("Save") ;
       btnDelete = new JButton("Delete") ;
       btnClose = new JButton("Close") ;
       btnFind = new JButton("Find") ;
       btnAdd.setMnemonic('A');
       btnSave.setMnemonic('S');
       btnDelete.setMnemonic('D');
       btnClose.setMnemonic('C');
       btnFind.setMnemonic('F');
       
       btnAdd.setPreferredSize(new java.awt.Dimension(80, 25));
       btnSave.setPreferredSize(new java.awt.Dimension(80, 25));
       btnDelete.setPreferredSize(new java.awt.Dimension(80, 25));
       btnClose.setPreferredSize(new java.awt.Dimension(80, 25));
       btnFind.setPreferredSize(new java.awt.Dimension(80, 25));
       
       btnAdd.setFont(CoeusFontFactory.getLabelFont());
       btnSave.setFont(CoeusFontFactory.getLabelFont());
       btnDelete.setFont(CoeusFontFactory.getLabelFont());
       btnClose.setFont(CoeusFontFactory.getLabelFont());
       btnFind.setFont(CoeusFontFactory.getLabelFont());
       
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
       pnlForBtn.add(btnFind,gridBagConstraints) ;
       
       
       gridBagConstraints = new GridBagConstraints();
       gridBagConstraints.gridx = 2;
       gridBagConstraints.gridy = 3;
       gridBagConstraints.insets = new Insets(2, 1, 2, 0);
       pnlForBtn.add(btnSave,gridBagConstraints) ;
       
       gridBagConstraints = new GridBagConstraints();
       gridBagConstraints.gridx = 2;
       gridBagConstraints.gridy = 4;
       gridBagConstraints.insets = new Insets(2, 1, 2, 0);
       pnlForBtn.add(btnClose,gridBagConstraints) ;
       
       btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
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
       btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed();
            }
       });
       
       getContentPane().setLayout(new BorderLayout(0,0));
        
//       pnlForState.add(scrlpnlForTableCtrl, BorderLayout.CENTER) ;
//       pnlForState.add(pnlForBtn, BorderLayout.EAST) ;
       
       gridBagConstraints = new java.awt.GridBagConstraints();
       pnlForState.add(scrlpnlForTableCtrl, gridBagConstraints) ;
       gridBagConstraints = new java.awt.GridBagConstraints();
       gridBagConstraints.ipadx = 20;
       gridBagConstraints.ipady = 20;
       gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
       pnlForState.add(pnlForBtn, gridBagConstraints) ;
       
       getContentPane().add(pnlForState, BorderLayout.CENTER);
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
       
       // populate the table
       Vector vecData = drawTableUsingtableStructureBeanEDI() ;
              
     }
    private void formatFields()
    {
//        tblEDIEnabledSponsor.setEnabled(false);
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnFind.setEnabled(false);
    }
  
     public void addBtnActionPerformed(java.awt.event.ActionEvent evt) 
    {
        Object[]  oneRow = {" "};
        
        sorter.insertRow(sorter.getRowCount(),oneRow);
       // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBeanEDI.getDuplicateIndex(0) );
        codeTableModel.setValueAt(null, codeTableModel.getRowCount()-1, 2);
        codeTableModel.setValueAt(null, codeTableModel.getRowCount()-1, 3 );
        
      // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name 
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBeanEDI.getUserIndex() );
       
        tblEDIEnabledSponsor.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
        tblEDIEnabledSponsor.editCellAt(codeTableModel.getRowCount()-1,  0) ;
        tblEDIEnabledSponsor.requestFocus();
    }
    
     public void btnSaveActionPerformed()
    {
        int  selectedRow = tblEDIEnabledSponsor.getSelectedRow();
        GetLastValue();
        Object temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        //return if failed to save.
        if (!saveData()) return;
        drawTableUsingtableStructureBeanEDI();
        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

//        //re-selected the row same as before save performed.
//        if (selectedRow > 0)
//            tblEDIEnabledSponsor.setRowSelectionInterval(selectedRow,selectedRow);
        if (selectedRow >= 0)
        {
            for (int i = 0; i < tblEDIEnabledSponsor.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblEDIEnabledSponsor.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblEDIEnabledSponsor.getRowCount() -1)
                        tblEDIEnabledSponsor.setRowSelectionInterval(0,0);
                }
            }
        }

           
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) 
    {
        int rowNum = tblEDIEnabledSponsor.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblEDIEnabledSponsor.getEditingColumn() >= 0 )
        {
           tblEDIEnabledSponsor.getCellEditor().stopCellEditing(); 
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
        if (confirm == 1) return;
        System.out.println("  *** Delete row selected row is  ***  " + rowNum) ; 
        //keep all deleted rows into vecDeletedRows.
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),codeTableModel.getColumnCount()-1) != "I")
        {   
            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModel.setValueAt("D", sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1);
            saveRequired = true;
            vecDeletedRows.add(getTableRow(rowNum)) ;
        }//end if
        
          sorter.removeRow(rowNum);
          
          if ( rowNum != 0 )
          {
              rowNum--; 
          }
          else
          {
            tblEDIEnabledSponsor.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
          }
         
          if (tblEDIEnabledSponsor.getRowCount() > 0  )
          {
              tblEDIEnabledSponsor.changeSelection(rowNum,  1, false, false) ; 
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
                        //parent.dispose(); 
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
    
    
    public void btnFindActionPerformed()
    {
        try
        {
            CoeusSearch coeusSearch =   new CoeusSearch(parent, SPONSOR_SEARCH, 1);
            coeusSearch.showSearchWindow();
            HashMap hashSponsor = coeusSearch.getSelectedRow();
            if(hashSponsor !=null)
            {
                int selRow = tblEDIEnabledSponsor.getSelectedRow();
                String stSponsorCode=Utils.convertNull(hashSponsor.get("SPONSOR_CODE"));;
                String stSponsorName=Utils.convertNull(hashSponsor.get("SPONSOR_NAME"));  
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(stSponsorCode))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "chkPKeyUniqVal_exceptionCode.2401");
               
                        CoeusOptionPane.showInfoDialog(msg);
                              
                        return ; //fail in uniqueid check, not change anything
                    }
                    else
                    {
                        tblEDIEnabledSponsor.editCellAt(selRow,  2) ;
                        tblEDIEnabledSponsor.setValueAt(stSponsorCode,selRow, 0) ;
                        tblEDIEnabledSponsor.setValueAt(stSponsorName,selRow, 1) ;
                        if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanEDI.getUserIndex() );
                            saveRequired = true;
                        }
                        tblEDIEnabledSponsor.editCellAt(selRow,  2) ;
                    }
                }
                else
                {
                    return ;//fail in dependency check,not change anything
                }                       
           }
         }
         catch(Exception ex)
         {
            ex.printStackTrace() ;
         }     

    }
     public void initialiseData()
     {
        hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure() ;
        
        TableStructureBean tempTableStructBean = null ;
        for (int tableCount =0 ; tableCount < hashAllCodeTableStructure.size() ; tableCount++)
        {    
            tempTableStructBean  = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(tableCount)) ;
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$edi_enabled_sponsors"))
            {
                tableStructureBeanEDI = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$proposal_type"))
            {
                tableStructureBeanProposal = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$notice_of_opportunity"))
            {
                tableStructureBeanNotice = tempTableStructBean ;
            }
         }// end for    
        
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

                HashMap hashStoredProcedure = (HashMap)tableStructureBeanEDI.getHashStoredProceduresForThisTable();
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
   { 
    // if there are any form level validations that should go there
        int rowTotal = tblEDIEnabledSponsor.getRowCount();
        int colTotal = tblEDIEnabledSponsor.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModel.getColumn(colIndex) ;
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblEDIEnabledSponsor.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblEDIEnabledSponsor.getValueAt(rowIndex,colIndex).equals("")
                            || tblEDIEnabledSponsor.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {   
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblEDIEnabledSponsor.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                        
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblEDIEnabledSponsor.changeSelection(rowIndex,  colIndex, false, false) ;
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
      
      /* CASE #607 Add deletes to the vector before inserts and updates to avoid 
       * unique constraint error */
      
      if (vecDeletedRows.size() > 0 )
      {//append deleted rows to vecUpdatedRows
          for (int row = 0; row < vecDeletedRows.size(); row++)
          {
              vecUpdatedRows.add(vecDeletedRows.get(row));
          }          
      }      
    
      while(rowCount < tblEDIEnabledSponsor.getRowCount())
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
   
    //copy one table row to a hashmap
   private HashMap getTableRow(int rowNum)
   { 
        HashMap hashRow = new HashMap() ;
        int colTotal = codeTableModel.getColumnCount()-1;
        for (int colCount=0; colCount <= codeTableModel.getColumnCount()-1; colCount++)
        { // for each column u will build a hashmap 
            if (colCount == 1 )
            {//sponsor name is not in update sp.
                continue;
            }
            TableColumn column = codeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            Object val = null ;
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
                
            if (columnBean.getDataType().equals("VARCHAR2"))
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
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1).equals("I")) // if itz an insert
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
                        val =  java.sql.Timestamp.valueOf(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString()) ;
                    }
                }
            }
                
            hashRow.put(columnBean.getColIdentifier(), val) ;
        } //end for
        return hashRow;
   }
    public Vector drawTableUsingtableStructureBeanEDI() 
    {
        vecDeletedRows.clear();
        
        codeTableModel = new ExtendedDefaultTableModel() ;

        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ; 
        
        
       // tblCodeTable.addFocusListener(this) ;        
        tblEDIEnabledSponsor.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureBeanEDI.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
         numColumnsDisplay = tableStructureBeanEDI.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNames = new String[numColumnsDisplay + 1];
        
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
              if (i == 2)//proposal type column
              {
                  Vector vectCombData;
                  HashMap htRow = new HashMap();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  vectCombData = getDataForCombo(tableStructureBeanProposal);
                  if (vectCombData == null) continue;
                  comb.addItem(new ComboBoxBean("", "")) ;
                  for (int j=0; j < vectCombData.size(); j++) //loop for num of rows
                  {
                        htRow = (HashMap)vectCombData.elementAt(j);
                        comb.addItem(new ComboBoxBean(htRow.get("PROPOSAL_TYPE_CODE").toString() , htRow.get("DESCRIPTION").toString()));
                   }//end for  

                   newColumn.setCellEditor(new StateExtendedCellEditor(comb)) ;
              }
              
              if (i == 3 )//notice of opportunity
              {
                  Vector vectCombData;
                  HashMap htRow = new HashMap();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  vectCombData = getDataForCombo(tableStructureBeanNotice);
                  if (vectCombData == null) continue;
                  comb.addItem(new ComboBoxBean("", "")) ;
                  for (int j=0; j < vectCombData.size(); j++) //loop for num of rows
                  {
                        htRow = (HashMap)vectCombData.elementAt(j);
                        comb.addItem(new ComboBoxBean(htRow.get("NOTICE_OF_OPPORTUNITY_CODE").toString() , htRow.get("DESCRIPTION").toString()));
                   }//end for  

                  newColumn.setCellEditor(new StateExtendedCellEditor(comb)) ;

              }

              if( i!= 2 && i !=3 )
              {    
                 newColumn.setCellEditor(new StateExtendedCellEditor(newColumnBean, i)) ;
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
            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"                                                                "); 
            
            javax.swing.table.JTableHeader header
                            = tblEDIEnabledSponsor.getTableHeader();
                header.setReorderingAllowed(false);
                header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
                

        tblEDIEnabledSponsor.setColumnModel(displayCodeTableColumnModel); // also give the structure
       
            
                        
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureBeanEDI and get the DataBean from the servlet.
            getDataForTheTable();
            
            // populate the data on the the screen
            vdata = accDataBean.getVectData();
            HashMap htRow = new HashMap();
                
            for (int i=0; i < vdata.size(); i++) //loop for num of rows
            {
                htRow = (HashMap)vdata.elementAt(i);
                Object [] rowColumnDatas = new Object[numColumnsDisplay];
                  
                for (int j = 0; j < numColumnsDisplay; j++) // loop for num of columns will display on screen
                {
                    rowColumnDatas[j] = (Object)htRow.get(columnNames[j]);
                    TableColumn column = codeTableColumnModel.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                     if (j == 2 || j == 3 ) 
                     {
                         Vector vectCombData;
                         if (j == 2 )
                         {
                            vectCombData = getDataForCombo(tableStructureBeanProposal);
                         }else
                         {
                             vectCombData = getDataForCombo(tableStructureBeanNotice);
                         }
                         if (vectCombData != null)
                         {
                             if (rowColumnDatas[j] != null)
                             {
                                String temp = rowColumnDatas[j].toString();
                            /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectCombData.size(); codeIndex++)
                                {
                                    HashMap hmRow = (HashMap)vectCombData.elementAt(codeIndex);
                                    if (j == 2)
                                    {                                       
                                        if (hmRow.get("PROPOSAL_TYPE_CODE").toString().equals(temp))
                                        {
                                            rowColumnDatas[j] = new ComboBoxBean(hmRow.get("PROPOSAL_TYPE_CODE").toString() , hmRow.get("DESCRIPTION").toString());
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        if (hmRow.get("NOTICE_OF_OPPORTUNITY_CODE").toString().equals(temp))
                                        {
                                            rowColumnDatas[j] = new ComboBoxBean(hmRow.get("NOTICE_OF_OPPORTUNITY_CODE").toString() , hmRow.get("DESCRIPTION").toString());
                                            break;
                                        }
                                    }
                                }

                             }
                        

                         }
                        

                        
                     }
                    
                    Object obj = rowColumnDatas[j];
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
        
        sorter.setModel(codeTableModel, false);
        tblEDIEnabledSponsor.setModel(sorter);
        
       // hide unwanted columns
       int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
       //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
       for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)    
       {  
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblEDIEnabledSponsor.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblEDIEnabledSponsor.removeColumn(inColumn ); 
            }
       }
       if (vdata.size()>0)
       {  
            tblEDIEnabledSponsor.setRowSelectionInterval(0,0) ;
            tblEDIEnabledSponsor.repaint() ;
       }   

       return vdata;
    }
    
          
        
  private void getDataForTheTable()
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectData = null;
      try
      {
          if(tableStructureBeanEDI == null)
            System.out.println("tableStructure == null");
          //Get all stored procedures associated with this table.
          HashMap hashStoredProcedure =
            (HashMap)tableStructureBeanEDI.getHashStoredProceduresForThisTable();
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
          
          //after send(), we have a username in requester
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
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
        
     }
          
     accDataBean.setVectData(vectData);   
           
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
  
/**************************************************************************/
  
  class StateExtendedCellEditor extends DefaultCellEditor implements TableCellEditor
  {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        String stSearch=null;

        public StateExtendedCellEditor(CoeusComboBox comb)
        {
            super(comb);
            comb.addFocusListener(new FocusAdapter()
            {
                       
                public void focusGained(FocusEvent e)
                {
                   // System.out.println("*** focus Gained ***") ; 
                }
                       
                public void focusLost(FocusEvent fe)
                {
                     if (!fe.isTemporary())
                     {
                         GetLastValue(); 
                     }
                }
            }); 
        }

        public StateExtendedCellEditor(ColumnBean columnBeanIn, int colIndex)
        {
            super( new CoeusTextField() );

            if (columnBeanIn.getDataType().equals("NUMBER"))
            {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,columnBeanIn.getDisplaySize( )));
            }
            else
            {
                txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));

            }
              
 
            txtCell.addMouseListener(new MouseAdapter()
            {
                public void mouseClicked(MouseEvent me)
                {
                         System.out.println("Mouse clicked..") ;
                        try
                        {  
//                           String stId = new String() ; 
//                           if (tblEDIEnabledSponsor.getValueAt(tblEDIEnabledSponsor.getSelectedRow(),0) != null)
//                           {    
//                             stId =  tblEDIEnabledSponsor.getValueAt(tblEDIEnabledSponsor.getSelectedRow(),0).toString() ;
//                           }
//                           else
//                           {
//                            stId = "" ;
//                           }
//                          
                           if (me.getClickCount() == 2)
                           {
                             if (txtCell.getText() == null || txtCell.getText().trim().equals(""))  
                             {    
                                 if (tblEDIEnabledSponsor.getSelectedColumn() == 0) //click on the first column, shud show the search screen
                                { 
                                    btnFindActionPerformed();
                                  
//                                    CoeusSearch coeusSearch =   new CoeusSearch(parent, SPONSOR_SEARCH, 1);
//                                    coeusSearch.showSearchWindow();
//                                    HashMap hashSponsor = coeusSearch.getSelectedRow();
//                                      if(hashSponsor !=null)
//                                      {
//                                            String stSponsorCode=Utils.
//                                                convertNull(hashSponsor.get("SPONSOR_CODE"));;
//                                            String stSponsorName=Utils.
//                                                convertNull(hashSponsor.get("SPONSOR_NAME"));  
//                                            txtCell.setText(stSponsorCode);
//                                            tblEDIEnabledSponsor.setValueAt(stSponsorCode,tblEDIEnabledSponsor.getSelectedRow(), 0) ;
//                                            tblEDIEnabledSponsor.setValueAt(stSponsorName,tblEDIEnabledSponsor.getSelectedRow(), 1) ;
//                                            if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
//                                            {
//                                                codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
//                                                codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanEDI.getUserIndex() );
//                                                saveRequired = true;
//                                            }
//                                            
//                                       }// end hashNull     
                                   }//end if
                                 }
                                else 
                                {
                                     String stId = new String() ; 
                                     if (tblEDIEnabledSponsor.getValueAt(tblEDIEnabledSponsor.getSelectedRow(),0) != null)
                                     {    
                                         stId =  tblEDIEnabledSponsor.getValueAt(tblEDIEnabledSponsor.getSelectedRow(),0).toString() ;
                                     }
                                     else
                                     {
                                        stId = "" ;
                                     }
                          
                                    // check if itz a valid sponsor id n display the sponsor screen  
                                    SponsorMaintenanceForm frmSponsor =  new SponsorMaintenanceForm('D',stId);
                                    frmSponsor.showForm((CoeusAppletMDIForm)parent, "Sponsor", true );
                                }
                             }// mouse click
                        } 
                        catch(Exception ex)
                        {
                           ex.printStackTrace() ;
                        }
                    }
            
        }) ;
            
                txtCell.addFocusListener(new FocusAdapter()
                    {
                        public void focusGained(FocusEvent e)
                        {
                            System.out.println("*** focus Gained ***") ; 
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
        
        
        
        /* This method is used to get the Sponsor information 
     * based on the sponsor code. It instantiates the RequesterBean class 
     * invokes the coeusFunctionsServlet and retrieves information from the database.
     * @param id , a UnitNumber on which the information to retrieve
     */
    private Vector getSponsorInfo(String id){
        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_SPONSORINFO");
        requester.setId(id);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            vsearchData = new Vector();
            SponsorMaintenanceFormBean sponsorInfo =
            (SponsorMaintenanceFormBean) response.getDataObject();

            if(sponsorInfo!=null && sponsorInfo.getName() !=null){
                String stName = sponsorInfo.getName();
                vsearchData.addElement(stName);
            }
        }
        return vsearchData;
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
           
            if (column == 2 || column == 3 )
            {
                if (value  instanceof ComboBoxBean)
                {
                        ComboBoxBean cmbTmp = (ComboBoxBean)value ; 
                        Object objTmp = cmbTmp.getCode() ;
                        CoeusComboBox CompTmp = (CoeusComboBox)getComponent();
                        CompTmp.setSelectedItem(objTmp);
                        return (Component)CompTmp;
                       
                 }  
                else
                {
                    return (Component)((CoeusComboBox)getComponent());
                }
            }
          
            String newValue =  (String)value;
            
            if( newValue != null && newValue.trim().length() > 0 ){
                txtCell.setText( value.toString().trim() );
            }else{
                txtCell.setText("");
            }
            return txtCell;
        }


        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
     
        public Object getCellEditorValue() {
            System.out.println("*** get cell editor value:  ***"  +txtCell.getText()) ;
            Object oldValue = tblEDIEnabledSponsor.getValueAt(selRow,selColumn);
            Object newValue = ((CoeusTextField)txtCell).getText(); 
            
            //check Unique key if the column is an editable PrimaryKey
            if (!tableStructureBeanEDI.isIdAutoGenerated() 
                && (selColumn == tableStructureBeanEDI.getPrimaryKeyIndex(0)))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString()))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "chkPKeyUniqVal_exceptionCode.2401");
               
                        CoeusOptionPane.showInfoDialog(msg);
                       return oldValue; //fail in uniqueid check
                    }
                }
                else
                {
                    return oldValue;//fail in dependency check
                }
                    
            }
            
            if (selColumn == 0)
            {
                String strId = txtCell.getText().trim(); 
               
               if (strId.equals(""))
               {
                    txtCell.setText("");
                    tblEDIEnabledSponsor.setValueAt("",selRow,0);
                    tblEDIEnabledSponsor.setValueAt("",selRow,1);
                    if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                    {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanEDI.getUserIndex() );
                        saveRequired = true;
                    }
                    System.out.println("strId is empty space here.") ;
               }else
               {   if (!oldValue.equals(newValue))
                   {
                      Vector vecData= getSponsorInfo(strId);
                      if(!((vecData.isEmpty()) || (vecData.size()<0)))
                      {
                        String stNm=(String)vecData.get(0);
                        tblEDIEnabledSponsor.setValueAt(strId,selRow,0);
                        tblEDIEnabledSponsor.setValueAt(stNm,selRow,1);
                        if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanEDI.getUserIndex() );
                            saveRequired = true;
                        }
                        System.out.println("setValueAt. saveRequired = true;") ;
                      }
                      else
                      {   
                        JOptionPane.showMessageDialog(parent,
                            coeusMessageResources.parseMessageKey(
                                        "protoFndSrcFrm_exceptionCode.1132"),
                         "Coeus",
                        JOptionPane.ERROR_MESSAGE);
                        txtCell.setText((String)oldValue);
                        tblEDIEnabledSponsor.setValueAt(oldValue,selRow,0);
                      }
                    }
               }
            }
              
      
            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblEDIEnabledSponsor.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblEDIEnabledSponsor.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name 
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanEDI.getUserIndex() );
                            saveRequired = true;
                            System.out.println("*** Set AcType to U  in getCellEditorValue***") ; 
                        }
                    }
                    else
                    { 
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name 
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanEDI.getUserIndex() );
                        saveRequired = true;
                        System.out.println("*** Set AcType to U  in getCellEditorValue***") ; 
                    } 
                }
        
            
        
             if (selColumn == 2 || selColumn == 3 ) 
                return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
          
            return ((CoeusTextField)txtCell).getText();
        }

        /**
         * This method is over loaded to handle the item state changed events.
         * @param e ItemEvent
         */
        
        public void itemStateChanged(ItemEvent e) 
        {
             System.out.println("*** get cell editor value:in itemStateChanged event  ***"  +txtCell.getText()) ;
             super.fireEditingStopped();
        }

        
        /**
         * Supporting method used to validate person Name for its correctness
         * existance with db data.
        */
        // there will various types of triggervalidation functions which takes care
        // of validation for different types of data.
        private boolean triggerValidation()
        {           
            System.out.println("*** Validation row : " + selRow + " col : " + selColumn + " ***") ;
          /*
           * using the colIndex u can get the column bean for that column and then depending the 
           * datatype of the column u apply appropriate validation rules
           */
            Object newValue = ((CoeusTextField)txtCell).getText(); 

            if (!tableStructureBeanEDI.isIdAutoGenerated() 
                && (selColumn == tableStructureBeanEDI.getPrimaryKeyIndex(0)))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString()))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "chkPKeyUniqVal_exceptionCode.2401");
               
                        CoeusOptionPane.showInfoDialog(msg);
                //after failure of checking, make sure selecting the failed row
               
                        return false; //fail in uniqueid check
                    }
                    else
                    {
                        return true;
                    }
                }
                else
                {
                    return false;//fail in dependency check
                }
                    
            }
             else
             {
                 return true;
             }
                
            
        }
        

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
 
 private boolean CheckUniqueId(String newVal)
 {
    int colIdx = tableStructureBeanEDI.getPrimaryKeyIndex(0) ;
    int rowTotal = tblEDIEnabledSponsor.getRowCount();
//    int row = tblEDIEnabledSponsor.getEditingRow();
     int row = tblEDIEnabledSponsor.getSelectedRow();
    if (rowTotal > 0 && row >= 0)
    {
        for (int index = 0; index < rowTotal ; index++)
        {
            if (index != row)
            {
                if (newVal.equals(tblEDIEnabledSponsor.getValueAt(index, colIdx).toString())) 
                    return false;
            }
        }
    }
    return true;
   
 }
 
private boolean checkDependency(int rowNumber, String from)
{
    HashMap hmDependencyCheckValues = new HashMap();
    int colNumber = tableStructureBeanEDI.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureBeanEDI.getHashTableDependency();
    int depenTabNum; 
    
    if (hmDependencyTables == null)
    {    
        return true; //there is no dependency need to check
    }    
    
    depenTabNum = hmDependencyTables.size();
    for(int i = 0; i < depenTabNum ; i++)
    {
        HashMap hmDependency = (HashMap)hmDependencyTables.get(new Integer(i));
        hmDependencyCheckValues.put("a_table",hmDependency.get("Table"));  
        hmDependencyCheckValues.put("a_column",hmDependency.get("Column"));
        RequestTxnBean requestTxnBean = new RequestTxnBean();
    
        if (columnBean.getDataType().equals("NUMBER"))
        {
            hmDependencyCheckValues.put("a_column_value",new Integer(tblEDIEnabledSponsor.getValueAt(rowNumber, colNumber).toString()));
            requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
         }
        else
        {
            hmDependencyCheckValues.put("a_column_value",tblEDIEnabledSponsor.getValueAt(rowNumber, colNumber).toString());
            requestTxnBean.setAction("DEPENDENCY_CHECK_VARCHAR2");
        }
    
        requestTxnBean.setDependencyCheck(hmDependencyCheckValues);
        Boolean success = (Boolean)getDataFromServlet(requestTxnBean) ;
        
        
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
    }
    return true;
}


 private void GetLastValue()
 {
    int row = tblEDIEnabledSponsor.getEditingRow();
    int col = tblEDIEnabledSponsor.getEditingColumn();
    if (row != -1 && col != -1) 
    {
        tblEDIEnabledSponsor.getCellEditor(row, col).stopCellEditing();
        TableColumn column = codeTableColumnModel.getColumn(col) ;
        //make sure to set AC_TYPE
        column.getCellEditor().getCellEditorValue();
    }

 }
 
  
  
  
/**************************************************************************/  
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
    
    // supporting class to display sponsor details on double clicking of any row
    // in the table.
    class SponsorDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
            int selRow = tblEDIEnabledSponsor.getSelectedRow();
            int selCol = tblEDIEnabledSponsor.getSelectedColumn();
            String sponsorId=
            (String)tblEDIEnabledSponsor.getModel().getValueAt(selRow,0);
            if(sponsorId != null && sponsorId.trim().length() >0){
                if (me.getClickCount() == 2) {
                    try{
                        SponsorMaintenanceForm frmSponsor =
                        new SponsorMaintenanceForm('D',sponsorId);
                                    frmSponsor.showForm((CoeusAppletMDIForm)parent, "Sponsor", true );
                    } catch(Exception ex){
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "frmEDIEnabledSponsor_exceptionCode.2407"));
                    }
                    ((StateExtendedCellEditor)tblEDIEnabledSponsor.getCellEditor(selRow,
                    0)).cancelCellEditing();
                }
            }
        }
    }
}
