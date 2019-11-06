/*
 * FormStateTable.java
 *
 * Created on March 11, 2003, 12:18 PM
 */

package edu.mit.coeus.codetable.client;

//import edu.mit.coeus.gui.* ;
//import edu.mit.coeus.utils.CoeusGuiConstants;

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


public class FormStateTable extends CoeusDlgWindow
{
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblStateTable;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private edu.mit.coeus.utils.CoeusComboBox cmbCountry = new CoeusComboBox() ;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlForState;
    private javax.swing.JSplitPane splitpnlMain;
    private javax.swing.JButton btnSave;

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
    TableStructureBean tableStructureBeanCountry ;
    TableStructureBean tableStructureBeanState ;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ; 
    int numColumnsDisplay = 0 ;
    String countryCode = new String() ; 
    Vector vecDeletedRows = new Vector();   
    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
    /** Creates a new instance of FormStateTable */
    public FormStateTable(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean)
    {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();
        if (!userRights)
            formatFields();
        cmbCountry.setFont(CoeusFontFactory.getLabelFont());
        cmbCountry.setSelectedItem("USA") ;
    }
    
     public void initComponents() 
     {
       coeusMessageResources = CoeusMessageResources.getInstance();
       
//        pnlForState = new JPanel(new BorderLayout(0,0)) ; 
       pnlForState = new JPanel(new GridBagLayout()) ; 
        
        JPanel pnlCountry = new JPanel(new FlowLayout(javax.swing.SwingConstants.CENTER, 30, 10 )) ;
        pnlCountry.add(cmbCountry, BorderLayout.NORTH) ;
//        pnlCountry.setMinimumSize(new java.awt.Dimension(500, 100)) ;
//        pnlCountry.setPreferredSize(new java.awt.Dimension(500, 100)) ;
        
        initialiseData() ;
        
//        sorter = new TableSorter() ;     
        sorter = new TableSorterCodeTable() ; 
        tblStateTable = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblStateTable); 
 
        tblStateTable.setRowHeight(22) ;
        tblStateTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
                    = tblStateTable.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));

        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
  //      pnlTable.setMinimumSize(new java.awt.Dimension(500, 400)) ;
  //      pnlTable.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        pnlTable.add(tblStateTable.getTableHeader(), BorderLayout.NORTH) ; 
        pnlTable.add(tblStateTable , BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        //prps new end
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
                       
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlCountry, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
              
        cmbCountry.addItemListener(new ItemListener()
        {
             public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {   
                    GetLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbCountry.getSelectedItem() ;
                    System.out.println("code " + cmb.getCode()) ;
                    String newCountryCode = cmb.getCode();
                    if (countryCode != newCountryCode && saveRequired)
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
                               cancelRequired = true; // don't repaint the table with original data from the retrieve
                               cmbCountry.setSelectedItem(countryCode) ;
                               /* CASE #555 End */
                                return;   
                           }
                        }else if (confirm == JOptionPane.CANCEL_OPTION)                           
                        {
                            cancelRequired = true;
                            cmbCountry.setSelectedItem(countryCode) ; 
                            return;
                        }
                   
                    }
                    if (cancelRequired)
                    {
                        cancelRequired = false;
                        return;
                    }
                    saveRequired = false;
                    countryCode = newCountryCode ;
                    if (countryCode.equals(""))
                    {
                        // hide the table
                        tblStateTable.setVisible(false) ;
                    }        
                    else
                    {
                        // even country with no states will have the table hidden
                        Vector vecTable = drawTableUsingtableStructureBeanState() ;

                    }    
                    
                }
                
            }
            
        });
        
       GridBagConstraints gridBagConstraints;
       
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
                addBtnActionPerformed(evt);
            }
        });
       btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                btnSaveActionPerformed(evt);
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
       pnlForState.add(splitpnlMain, gridBagConstraints) ;
       gridBagConstraints = new java.awt.GridBagConstraints();
       gridBagConstraints.ipadx = 23;
       gridBagConstraints.ipady = 75;
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
     }
     
    private void formatFields()
    {
//        tblStateTable.setEnabled(false);
//        cmbCountry.setEnabled(false);
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
    } 
     
    public void addBtnActionPerformed(java.awt.event.ActionEvent evt) 
    {
        Object[]  oneRow = {" "};
        
        //for state table do not auto generate id, has two key columns. 
        sorter.insertRow(sorter.getRowCount(),oneRow);
       // set the duplicate index interface aw_...
        codeTableModel.setValueAt(countryCode, codeTableModel.getRowCount()-1, tableStructureBeanState.getDuplicateIndex(0) );
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBeanState.getDuplicateIndex(1) );
       // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name 
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBeanState.getUserIndex() );
        //set the county code.
        codeTableModel.setValueAt(countryCode, codeTableModel.getRowCount()-1, tableStructureBeanState.getPrimaryKeyIndex(0) );
        
        tblStateTable.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
//        tblStateTable.editCellAt(codeTableModel.getRowCount()-1,  0) ;
        tblStateTable.requestFocus();
    }
    
//    public void btnSaveActionPerformed(java.awt.event.ActionEvent evt) 
     public void btnSaveActionPerformed()
    {
        int  selectedRow = tblStateTable.getSelectedRow();
        GetLastValue();
        
        Object temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),1);
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        //return if failed to save.
        if (!saveData()) return;
        drawTableUsingtableStructureBeanState();
        
        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

        if (selectedRow >= 0)
        {
            for (int i = 0; i < tblStateTable.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),1).toString().equals(temp.toString()))
                {
                    tblStateTable.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblStateTable.getRowCount() -1)
                        tblStateTable.setRowSelectionInterval(0,0);
                }
            }
        }
//        if (selectedRow > 0 )
//            tblStateTable.setRowSelectionInterval(selectedRow,selectedRow);
//       
    }
    
    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) 
    {
        int rowNum = tblStateTable.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblStateTable.getEditingColumn() >= 0 )
        {
           tblStateTable.getCellEditor().stopCellEditing(); 
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
        //keep all deleted row into vecDeletedRows.
        if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),codeTableModel.getColumnCount()-1) != "I")
        {   
            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModel.setValueAt("D", sorter.getIndexForRow(rowNum), codeTableModel.getColumnCount()-1);
            saveRequired = true;
            //save to vecDeletedRows with hash of vec
            //vecDeletedRows.add(codeTableModel.getDataVector().elementAt(rowNum));              
            vecDeletedRows.add(getTableRow(rowNum)) ;
        }//end if
        
        sorter.removeRow(rowNum);
          
        if ( rowNum != 0 )
        {
            rowNum--; 
        }
        else
        {
            tblStateTable.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }
         
        if (tblStateTable.getRowCount() > 0  )
        {
            tblStateTable.changeSelection(rowNum,  1, false, false) ; 
        }
         
    }
    
//    public void btnCloseActionPerformed(java.awt.event.ActionEvent evt) 
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
            if (columnBean.getDataType().equals("NUMBER"))
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) == null
                        ||(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).equals("")))
                {  
                    val = null ; //new Integer(0) 
                }
                else
                {
                    val = new Integer(codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount).toString()) ;
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

                HashMap hashStoredProcedure = (HashMap)tableStructureBeanState.getHashStoredProceduresForThisTable();
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
        int rowTotal = tblStateTable.getRowCount();
        int colTotal = tblStateTable.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModel.getColumn(colIndex + 1) ;//becasue county code is first column and not display
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblStateTable.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblStateTable.getValueAt(rowIndex,colIndex).equals("")
                            || tblStateTable.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {   
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblStateTable.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                        
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblStateTable.changeSelection(rowIndex,  colIndex, false, false) ;
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
      
      while(rowCount < tblStateTable.getRowCount())
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
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$country_code"))
            {
                tableStructureBeanCountry = tempTableStructBean ;
            }    
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$state_code"))
            {
                tableStructureBeanState = tempTableStructBean ;
            }
         }// end for    
         
        getDataForTheCountryCombo() ;
        
     }
     
     
    public Vector drawTableUsingtableStructureBeanState() 
    {
        vecDeletedRows.clear();
        codeTableModel = new ExtendedDefaultTableModel() ;
     
                     
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureBeanState.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
         numColumnsDisplay = tableStructureBeanState.getNumColumns() ;
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
              
              if (newColumnBean.getOptions()!= null )
              {
                  HashMap hashOptions = newColumnBean.getOptions() ;
                  CoeusComboBox comb = new CoeusComboBox() ;
                  for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); )
                  {    
                    String strKey =  it.next().toString() ; 
                    ComboBoxBean comboBoxBean = new ComboBoxBean(strKey, hashOptions.get(strKey).toString()) ;
                    //JComboBox comb = new JComboBox(newColumnBean.getOptions()) ;
                    comb.addItem(comboBoxBean) ;
                   }   
                           
                   newColumn.setCellEditor(new DefaultCellEditor(comb)) ;
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
           
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureBeanState and get the DataBean from the servlet.
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
      
    
        sorter.setModel(codeTableModel) ;
        tblStateTable.setModel(sorter) ;
  
        tblStateTable.setColumnModel(displayCodeTableColumnModel); // also give the structure
       
       // tblCodeTable.addFocusListener(this) ;        
        tblStateTable.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
            // hide unwanted columns
            int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
            //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
            for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)    
            {  
                TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

                if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
                {
                    TableColumnModel columnModel = tblStateTable.getColumnModel();
                    TableColumn inColumn = columnModel.getColumn(colCount );
                    tblStateTable.removeColumn(inColumn ); 
                }
            }
            if (tblStateTable.getRowCount() > 0)
            {
                tblStateTable.setRowSelectionInterval(0,0) ;
                tblStateTable.repaint() ;
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
          if(tableStructureBeanState == null)
            System.out.println("tableStructure == null");
          //Get all stored procedures associated with this table.
          HashMap hashStoredProcedure =
            (HashMap)tableStructureBeanState.getHashStoredProceduresForThisTable();
          if(hashStoredProcedure == null)
            System.out.println("hashStoredProcedure == null");
          //Get the select stored procedure associated witht this table.
          StoredProcedureBean selectStoredProcedure =
            (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
          
          RequestTxnBean requestTxnBean = new RequestTxnBean();
          requestTxnBean.setAction("GET_DATA");
          requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
          
          Vector param= new Vector();
          param.add("osp$state_code") ; // 
          param.add("AW_COUNTRY_CODE") ; // 
          param.add(countryCode)  ;
           
          requestTxnBean.setSelectProcParameters(param) ;
                     
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
  
  
  
  
  private void getDataForTheCountryCombo()
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectCountry = null;
      try
      {
          if(tableStructureBeanCountry == null)
            System.out.println("tableStructure == null");
          //Get all stored procedures associated with this table.
          HashMap hashStoredProcedure =
            (HashMap)tableStructureBeanCountry.getHashStoredProceduresForThisTable();
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
             vectCountry = accDataBean.getVectData();
          }
          
          // populate the data to the combo
           HashMap htRow = new HashMap();
           cmbCountry.addItem(new ComboBoxBean("", "")) ;
           for (int i=0; i < vectCountry.size(); i++) //loop for num of rows
           {
                htRow = (HashMap)vectCountry.elementAt(i);
                cmbCountry.addItem(new ComboBoxBean(htRow.get("COUNTRY_CODE").toString() , htRow.get("COUNTRY_NAME").toString()));
            }//end for  
          
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
        
     }
           
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
            
            if (value != null)
            {
                System.out.println("*** setText in getTableCellEditorComponent ***" + value) ;
                txtCell.setText(value.toString()) ;
            }
            else
            {  
                txtCell.setText(null) ;
            }
            return txtCell;
        }


        /** Returns the value contained in the editor.
         * @return Object the value contained in the editor
         */
         public Object getCellEditorValue() {
            System.out.println("*** get cell editor value:  ***"  +txtCell.getText()) ;
            Object oldValue = tblStateTable.getValueAt(selRow,selColumn);
            Object newValue = ((CoeusTextField)txtCell).getText(); 
            
            if (!tableStructureBeanState.isIdAutoGenerated() 
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
                    
            }
            
           if (selRow > -1)
            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblStateTable.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblStateTable.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name 
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanState.getUserIndex() );
                            saveRequired = true;
                            System.out.println("*** Set AcType to U  in getCellEditorValue***") ; 
                        }
                    }
                    else
                    { 
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name 
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanState.getUserIndex() );
                        saveRequired = true;
                        System.out.println("*** Set AcType to U  in getCellEditorValue***") ; 
                    } 
                }
        
            }
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

            if (!tableStructureBeanState.isIdAutoGenerated() 
                && (selColumn == tableStructureBeanState.getPrimaryKeyIndex(1)))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString()))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "chkPKeyUniqVal_exceptionCode.2401");
               
                        CoeusOptionPane.showInfoDialog(msg);
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
    if (newVal == null) return true;
    int colIdx = tableStructureBeanState.getPrimaryKeyIndex(0) ;
    int rowTotal = tblStateTable.getRowCount();
    int row = tblStateTable.getEditingRow();
    if (rowTotal > 0 && row >= 0)
    {
        for (int index = 0; index < rowTotal ; index++)
        {
            if (index != row)
            {
                if (newVal.equals(tblStateTable.getValueAt(index, colIdx).toString())) 
                    return false;
            }
        }
    }
    return true;
   
 }
 
private boolean checkDependency(int rowNumber, String from)
{
    HashMap hmDependencyCheckValues = new HashMap();
    int colNumber = tableStructureBeanState.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureBeanState.getHashTableDependency();
    int depenTabNum; 
    
    if (hmDependencyTables == null)
    {    
        return true; //there is no dependency need to check
    }    
    
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
    }
    return true;
}


 private void GetLastValue()
 {
    int row = tblStateTable.getEditingRow();
    int col = tblStateTable.getEditingColumn();
    if (row != -1 && col != -1) 
    {
        tblStateTable.getCellEditor(row, col).stopCellEditing();
        TableColumn column = codeTableColumnModel.getColumn(col + 1) ; //here col +1 ,because state code is second col in codeTableColumnModel, is first col in tblStateTable
        //make sure to set AC_TYPE
        column.getCellEditor().getCellEditorValue();
    }

 }
     
}  //end class
