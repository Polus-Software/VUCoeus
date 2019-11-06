/*
 * FormProtoCorrespDefRecip.java
 *
 * Created on November 17, 2003, 4:20 PM
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

public class FormProtoCorrespDefRecip extends CoeusDlgWindow
{
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblProtoCorresp;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
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
    TableStructureBean tableStructureBeanPCDR ;
    TableStructureBean tableStructureBeanProtoCorresp ;
    TableStructureBean tableStructureBeanCorrespondent ;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    CoeusComboBox cmbProtoCorrespType = new CoeusComboBox() ;
    CoeusComboBox cmbCorrespondentType = new CoeusComboBox() ;
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ;
    int numColumnsDisplay = 0 ;
    Vector vecDeletedRows = new Vector();

    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
//   private static final String SPONSOR_SEARCH = "sponsorSearch";

   private Frame parent;

    /** Creates a new instance of FormEDIEnabledSpronsor */
    public FormProtoCorrespDefRecip(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean)
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
        tblProtoCorresp = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblProtoCorresp);
     
        tblProtoCorresp.setRowHeight(22) ;
        tblProtoCorresp.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;


        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;


        pnlTable.add(tblProtoCorresp.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblProtoCorresp , BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;


        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);

        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;


       java.awt.GridBagConstraints gridBagConstraints;

       pnlForBtn = new JPanel(new GridBagLayout()) ;
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
       Vector vecData = drawTableUsingtableStructureBeanPCDR() ;

     }

    private void formatFields()
    {
//        tblProtoCorresp.setEnabled(false);
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
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBeanPCDR.getDuplicateIndex(0) );
        codeTableModel.setValueAt(null, codeTableModel.getRowCount()-1, 0);
        codeTableModel.setValueAt(null, codeTableModel.getRowCount()-1, 1 );

      // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBeanPCDR.getUserIndex() );

        tblProtoCorresp.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
        tblProtoCorresp.editCellAt(codeTableModel.getRowCount()-1,  0) ;
        tblProtoCorresp.requestFocus();
    }

     public void btnSaveActionPerformed()
    {
        Object temp = null;
        int  selectedRow = tblProtoCorresp.getSelectedRow();
        GetLastValue();                
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        //return if failed to save.
        if (!saveData()) return;
        drawTableUsingtableStructureBeanPCDR();

        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

        if (selectedRow >= 0)
        {   temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);        
            for (int i = 0; i < tblProtoCorresp.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblProtoCorresp.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblProtoCorresp.getRowCount() -1)
                        tblProtoCorresp.setRowSelectionInterval(0,0);
                }
            }
        }
//        //re-selected the row same as before save performed.
//        if (selectedRow > 0 )
//            tblProtoCorresp.setRowSelectionInterval(selectedRow,selectedRow);

    }

    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)
    {
        int rowNum = tblProtoCorresp.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblProtoCorresp.getEditingColumn() >= 0 )
        {
           tblProtoCorresp.getCellEditor().stopCellEditing(); 
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
            tblProtoCorresp.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }

        if (tblProtoCorresp.getRowCount() > 0  )
        {
            tblProtoCorresp.changeSelection(rowNum,  1, false, false) ;
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
     public void initialiseData()
     {
        hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure() ;

        TableStructureBean tempTableStructBean = null ;
        for (int tableCount =0 ; tableCount < hashAllCodeTableStructure.size() ; tableCount++)
        {
            tempTableStructBean  = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(tableCount)) ;

            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$PROTO_CORRESP_DEF_RECIP"))
            {
                tableStructureBeanPCDR = tempTableStructBean ;
            }

            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$PROTO_CORRESP_TYPE"))
            {
                tableStructureBeanProtoCorresp = tempTableStructBean ;
            }

            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$CORRESPONDENT_TYPE"))
            {
                tableStructureBeanCorrespondent = tempTableStructBean ;
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

                HashMap hashStoredProcedure = (HashMap)tableStructureBeanPCDR.getHashStoredProceduresForThisTable();
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
        int rowTotal = tblProtoCorresp.getRowCount();
        int colTotal = tblProtoCorresp.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModel.getColumn(colIndex) ;

                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

                if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblProtoCorresp.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblProtoCorresp.getValueAt(rowIndex,colIndex).equals("")
                            || tblProtoCorresp.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblProtoCorresp.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");

                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblProtoCorresp.changeSelection(rowIndex,  colIndex, false, false) ;
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

      while(rowCount < tblProtoCorresp.getRowCount())
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
//            if (colCount == 1 )
//            {//sponsor name is not in update sp.
//                continue;
//            }
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
    public Vector drawTableUsingtableStructureBeanPCDR()
    {
        vecDeletedRows.clear();
        codeTableModel = new ExtendedDefaultTableModel() ;
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;


       // tblCodeTable.addFocusListener(this) ;
        tblProtoCorresp.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureBeanPCDR.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
         numColumnsDisplay = tableStructureBeanPCDR.getNumColumns() ;
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
              if (i == 0)//PROTO_CORRESP_TYPE_CODE
              {
                  Vector vectCombData;
                  HashMap htRow = new HashMap();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  vectCombData = getDataForCombo(tableStructureBeanProtoCorresp);
                  if (vectCombData == null) continue;
                  comb.addItem(new ComboBoxBean("", "")) ;
                  for (int j=0; j < vectCombData.size(); j++) //loop for num of rows
                  {
                        htRow = (HashMap)vectCombData.elementAt(j);
                        comb.addItem(new ComboBoxBean(htRow.get("PROTO_CORRESP_TYPE_CODE").toString() , htRow.get("DESCRIPTION").toString()));
                   }//end for

                   newColumn.setCellEditor(new StateExtendedCellEditor(comb)) ;
              }

              if (i == 1 )//CORRESPONDENT_TYPE_CODE
              {
                  Vector vectCombData;
                  HashMap htRow = new HashMap();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  vectCombData = getDataForCombo(tableStructureBeanCorrespondent);
                  if (vectCombData == null) continue;
                  comb.addItem(new ComboBoxBean("", "")) ;
                  for (int j=0; j < vectCombData.size(); j++) //loop for num of rows
                  {
                        htRow = (HashMap)vectCombData.elementAt(j);
                        comb.addItem(new ComboBoxBean(htRow.get("CORRESPONDENT_TYPE_CODE").toString() , htRow.get("DESCRIPTION").toString()));
                   }//end for

                  newColumn.setCellEditor(new StateExtendedCellEditor(comb)) ;

              }

              if( i!= 0 && i != 1 )
              {
                 newColumn.setCellEditor(new StateExtendedCellEditor(newColumnBean, i)) ;
              }
              if (newColumnBean.getDisplaySize()<=5)
              {
                 newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 75) ;
              }

              newColumn.setMinWidth(10) ;

              // add the new columnto the column model also
              codeTableColumnModel.addColumn(newColumn) ;
              displayCodeTableColumnModel.addColumn(newColumn) ;

              // add it to the tablemodel
              codeTableModel.addColumn(newColumn);

              columnNames[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName();
            }
            javax.swing.table.JTableHeader header
                            = tblProtoCorresp.getTableHeader();
            header.setReorderingAllowed(false);
            header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
              
            tblProtoCorresp.setColumnModel(displayCodeTableColumnModel); // also give the structure

            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureBeanPCDR and get the DataBean from the servlet.
            vdata = getDataForTheTable();

            // populate the data on the the screen
//            vdata = accDataBean.getVectData();
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
                     if (j == 0 || j == 1 )
                     {
                         Vector vectCombData;
                         if (j == 0 )
                         {
                            vectCombData = getDataForCombo(tableStructureBeanProtoCorresp);
                         }else
                         {
                             vectCombData = getDataForCombo(tableStructureBeanCorrespondent);
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
                                    if (j == 0)
                                    {
                                        if (hmRow.get("PROTO_CORRESP_TYPE_CODE").toString().equals(temp))
                                        {
                                            rowColumnDatas[j] = new ComboBoxBean(hmRow.get("PROTO_CORRESP_TYPE_CODE").toString() , hmRow.get("DESCRIPTION").toString());
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        if (hmRow.get("CORRESPONDENT_TYPE_CODE").toString().equals(temp))
                                        {
                                            rowColumnDatas[j] = new ComboBoxBean(hmRow.get("CORRESPONDENT_TYPE_CODE").toString() , hmRow.get("DESCRIPTION").toString());
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
       
       sorter.setModel(codeTableModel,false) ;
       tblProtoCorresp.setModel(sorter) ;

       // hide unwanted columns
       int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
       //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
       for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)
       {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblProtoCorresp.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblProtoCorresp.removeColumn(inColumn );
            }
        }
        if (vdata.size()>0)
        {
            tblProtoCorresp.setRowSelectionInterval(0,0) ;
            tblProtoCorresp.repaint() ;
        }


       return vdata;
    }



  private Vector getDataForTheTable()
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectData = null;
      try
      {
          if(tableStructureBeanPCDR == null)
            System.out.println("tableStructure == null");
          //Get all stored procedures associated with this table.
          HashMap hashStoredProcedure =
            (HashMap)tableStructureBeanPCDR.getHashStoredProceduresForThisTable();
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

//     accDataBean.setVectData(vectData);
      return vectData;

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
                           String stId = new String() ;
                           if (tblProtoCorresp.getValueAt(tblProtoCorresp.getSelectedRow(),0) != null)
                           {
                             stId =  tblProtoCorresp.getValueAt(tblProtoCorresp.getSelectedRow(),0).toString() ;
                           }
                           else
                           {
                            stId = "" ;
                           }

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



//        /* This method is used to get the Sponsor information
//     * based on the sponsor code. It instantiates the RequesterBean class
//     * invokes the coeusFunctionsServlet and retrieves information from the database.
//     * @param id , a UnitNumber on which the information to retrieve
//     */
//    private Vector getSponsorInfo(String id){
//        Vector vsearchData=null;
//        RequesterBean requester = new RequesterBean();
//        requester.setDataObject("GET_SPONSORINFO");
//        requester.setId(id);
//        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
//        AppletServletCommunicator comm
//                = new AppletServletCommunicator(connectTo, requester);
//        comm.send();
//        ResponderBean response = comm.getResponse();
//        if (response!=null){
//            vsearchData = new Vector();
//            SponsorMaintenanceFormBean sponsorInfo =
//            (SponsorMaintenanceFormBean) response.getDataObject();
//
//            if(sponsorInfo!=null && sponsorInfo.getName() !=null){
//                String stName = sponsorInfo.getName();
//                vsearchData.addElement(stName);
//            }
//        }
//        return vsearchData;
//    }
//



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

            if (column == 0 || column == 1 )
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
            Object oldValue = tblProtoCorresp.getValueAt(selRow,selColumn);
            Object newValue = ((CoeusTextField)txtCell).getText();
            TableColumn column = codeTableColumnModel.getColumn(selColumn) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
   
            //this window has only two combox columns to display
            newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();


                 if (checkDependency(selRow, ""))
                 {
                    if(!CheckUniqueId(newValue.toString(), selRow, selColumn))
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
           



            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
            if (selRow > -1)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblProtoCorresp.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblProtoCorresp.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanPCDR.getUserIndex() );
                            saveRequired = true;
                            System.out.println("*** Set AcType to U  in getCellEditorValue***") ;
                        }
                    }
                    else
                    {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanPCDR.getUserIndex() );
                        saveRequired = true;
                        System.out.println("*** Set AcType to U  in getCellEditorValue***") ;
                    }
                }

            }

             if (selColumn == 0 || selColumn == 1 )
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

            if (!tableStructureBeanPCDR.isIdAutoGenerated()
                && (selColumn == tableStructureBeanPCDR.getPrimaryKeyIndex(0)))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString(), selRow, selColumn))
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
 
 private boolean CheckUniqueId(String newVal, int selRow, int selCol)
 {
    int rowTotal = tblProtoCorresp.getRowCount();
    int row = tblProtoCorresp.getEditingRow();
    if (rowTotal > 0 && row >= 0)
    {   
        Vector vecPrimaryKey = tableStructureBeanPCDR.getPrimaryKeyIndex();
        
        if  (vecPrimaryKey.size() == 2)
        {   
            int colIdx1 ;    
            int colIdx2 ; 
            //keep selected column number in colIdx1
            if (selCol == tableStructureBeanPCDR.getPrimaryKeyIndex(0))
            {
                colIdx1 = tableStructureBeanPCDR.getPrimaryKeyIndex(0) ;    
                colIdx2 = tableStructureBeanPCDR.getPrimaryKeyIndex(1) ;  
            }
            else
            {
                colIdx1 = tableStructureBeanPCDR.getPrimaryKeyIndex(1) ;    
                colIdx2 = tableStructureBeanPCDR.getPrimaryKeyIndex(0) ;  
            }
            
            Object valTemp = tblProtoCorresp.getValueAt(selRow, colIdx2);
            if ( valTemp instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp ; 
                valTemp = cmbTmp.getCode() ;
            }
            if (valTemp != null && newVal != null)
            {
                for (int index = 0; index < rowTotal ; index++)
                {
                    if (index != row)
                    {
                        Object val1 = tblProtoCorresp.getValueAt(index, colIdx1);
                        Object val2 = tblProtoCorresp.getValueAt(index, colIdx2);
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
            }
        }
       
           
    }
  
    return true;
   
 }


private boolean checkDependency(int rowNumber, String from)
{
    HashMap hmDependencyCheckValues = new HashMap();
    int colNumber = tableStructureBeanPCDR.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureBeanPCDR.getHashTableDependency();
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
            hmDependencyCheckValues.put("a_column_value",new Integer(tblProtoCorresp.getValueAt(rowNumber, colNumber).toString()));
            requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
         }
        else
        {
            hmDependencyCheckValues.put("a_column_value",tblProtoCorresp.getValueAt(rowNumber, colNumber).toString());
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
    int row = tblProtoCorresp.getEditingRow();
    int col = tblProtoCorresp.getEditingColumn();
    if (row != -1 && col != -1)
    {
        tblProtoCorresp.getCellEditor(row, col).stopCellEditing();
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


}
