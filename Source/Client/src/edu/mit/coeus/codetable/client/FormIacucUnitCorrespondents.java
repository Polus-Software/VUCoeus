/*
 * @(#)FormMembershipRoleUsage.java 1.0 04 August 2010, 18:20 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 09-August-2010
 * by Md.Ehtesham Ansari
 */
package edu.mit.coeus.codetable.client;

import edu.mit.coeus.gui.* ;
import edu.mit.coeus.unit.gui.* ;
import edu.mit.coeus.unit.bean.* ;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.rolodexmaint.gui.RolodexMaintenanceDetailForm;
import edu.mit.coeus.departmental.gui.PersonDetailForm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.beans.*;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.search.gui.* ;

public class FormIacucUnitCorrespondents extends CoeusDlgWindow 
{
    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblUnitCorrespondents;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnUnit;
    private javax.swing.JButton btnPerson;
    private javax.swing.JButton btnRolodex;
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
    TableStructureBean tableStructureBeanUnitCorres ;
    TableStructureBean tableStructureBeanCorrespondents ;
    TableStructureBean tableStructureBeanNotice ;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
//    CoeusComboBox cmbCorrespondentsType = new CoeusComboBox() ;
//    CoeusComboBox cmbNotice = new CoeusComboBox() ;
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames = null ;
    int numColumnsDisplay = 0 ;
    Vector vecDeletedRows = new Vector();

    //hold login user id
    String   userId;
    private boolean userHasRights = false;
    
   private static final String UNIT_SEARCH = "leadUnitSearch";

   private Frame parent;

    /** Creates a new instance of FormEDIEnabledSpronsor */
    public FormIacucUnitCorrespondents(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean)
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
        tblUnitCorrespondents = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblUnitCorrespondents);  

        tblUnitCorrespondents.setRowHeight(22) ;
        tblUnitCorrespondents.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        tblUnitCorrespondents.addMouseListener( new UnitPersonDisplayAdapter());

        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;


        pnlTable.add(tblUnitCorrespondents.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblUnitCorrespondents , BorderLayout.CENTER ) ;
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
       btnUnit = new JButton("Find Unit") ;
       btnPerson = new JButton("Find Person") ;
       btnRolodex = new JButton("Find Rolodex") ;
       btnAdd.setMnemonic('A');
       btnSave.setMnemonic('S');
       btnDelete.setMnemonic('D');
       btnClose.setMnemonic('C');
       btnUnit.setMnemonic('U');
       btnPerson.setMnemonic('P');
       btnRolodex.setMnemonic('R');
       

       btnAdd.setPreferredSize(new java.awt.Dimension(105, 25));
       btnSave.setPreferredSize(new java.awt.Dimension(105, 25));
       btnDelete.setPreferredSize(new java.awt.Dimension(105, 25));
       btnClose.setPreferredSize(new java.awt.Dimension(105, 25));
       btnUnit.setPreferredSize(new java.awt.Dimension(105, 25));
       btnPerson.setPreferredSize(new java.awt.Dimension(105, 25));
       btnRolodex.setPreferredSize(new java.awt.Dimension(105, 25));

       btnAdd.setFont(CoeusFontFactory.getLabelFont());
       btnSave.setFont(CoeusFontFactory.getLabelFont());
       btnDelete.setFont(CoeusFontFactory.getLabelFont());
       btnClose.setFont(CoeusFontFactory.getLabelFont());
       btnUnit.setFont(CoeusFontFactory.getLabelFont());
       btnPerson.setFont(CoeusFontFactory.getLabelFont());
       btnRolodex.setFont(CoeusFontFactory.getLabelFont());

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
       pnlForBtn.add(btnUnit,gridBagConstraints) ;
       
       gridBagConstraints = new GridBagConstraints();
       gridBagConstraints.gridx = 2;
       gridBagConstraints.gridy = 3;
       gridBagConstraints.insets = new Insets(2, 1, 2, 0);
       pnlForBtn.add(btnPerson,gridBagConstraints) ;
       
       gridBagConstraints = new GridBagConstraints();
       gridBagConstraints.gridx = 2;
       gridBagConstraints.gridy = 4;
       gridBagConstraints.insets = new Insets(2, 1, 2, 0);
       pnlForBtn.add(btnRolodex,gridBagConstraints) ;
       
       gridBagConstraints = new GridBagConstraints();
       gridBagConstraints.gridx = 2;
       gridBagConstraints.gridy = 5;
       gridBagConstraints.insets = new Insets(2, 1, 2, 0);
       pnlForBtn.add(btnSave,gridBagConstraints) ;

       gridBagConstraints = new GridBagConstraints();
       gridBagConstraints.gridx = 2;
       gridBagConstraints.gridy = 6;
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
       
       btnUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnitActionPerformed();
            }
       });
             
       btnPerson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPersonActionPerformed();
            }
       });
       
       btnRolodex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRolodexActionPerformed();
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
       Vector vecData = drawTableUsingtableStructureBeanUnitCorres() ;

     }

     private void formatFields()
    {
//        tblUnitCorrespondents.setEnabled(false);
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnUnit.setEnabled(false);
        btnPerson.setEnabled(false);
        btnRolodex.setEnabled(false);
    }
     
     public void addBtnActionPerformed(java.awt.event.ActionEvent evt)
    {
        Object[]  oneRow = {" "};

        //for state table do not auto generate id, has two key columns.
        sorter.insertRow(sorter.getRowCount(),oneRow);
       // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureBeanUnitCorres.getDuplicateIndex(0) );
        codeTableModel.setValueAt(null, codeTableModel.getRowCount()-1, 2);
        codeTableModel.setValueAt(null, codeTableModel.getRowCount()-1, 3 );

      // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureBeanUnitCorres.getUserIndex() );

        tblUnitCorrespondents.changeSelection(codeTableModel.getRowCount()-1,  0, false, false) ;
        tblUnitCorrespondents.editCellAt(codeTableModel.getRowCount()-1,  0) ;
        tblUnitCorrespondents.requestFocus();
    }

     public void btnSaveActionPerformed()
    {
        Object temp = null;
        int  selectedRow = tblUnitCorrespondents.getSelectedRow();
        GetLastValue();                  
        int sorteColumn = sorter.getSortedColNum();
        boolean asc = sorter.getAscending();
        boolean sorted = sorter.getHasSorted();
        //return if failed to save.
        if (!saveData()) return;
        drawTableUsingtableStructureBeanUnitCorres();

        if (sorted)
        {
            sorter.sortByColumn(sorteColumn,asc);
        }

        if (selectedRow >= 0)
        {   temp = codeTableModel.getValueAt(sorter.getIndexForRow(selectedRow),0);      
            for (int i = 0; i < tblUnitCorrespondents.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(i),0).toString().equals(temp.toString()))
                {
                    tblUnitCorrespondents.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblUnitCorrespondents.getRowCount() -1){
                        tblUnitCorrespondents.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
        //re-selected the row same as before save performed.
//        if ( selectedRow > 0 )
//            tblUnitCorrespondents.setRowSelectionInterval(selectedRow,selectedRow);

    }

    public void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)
    {
        int rowNum = tblUnitCorrespondents.getSelectedRow();
        if (rowNum == -1) return;
        
        if ( tblUnitCorrespondents.getEditingColumn() >= 0 )
        {
           tblUnitCorrespondents.getCellEditor().stopCellEditing(); 
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
            vecDeletedRows.add(getTableRow(rowNum)) ;
        }//end if

        sorter.removeRow(rowNum);

        if ( rowNum != 0 )
        {
            rowNum--; 
        }
        else
        {
            tblUnitCorrespondents.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ; 
        }
        tblUnitCorrespondents.editCellAt(rowNum,  0) ;  
        if (tblUnitCorrespondents.getRowCount() > 0  )
        {
            tblUnitCorrespondents.changeSelection(rowNum,  1, false, false) ;
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
    
    public void btnUnitActionPerformed()
    {
        
        try
        {
            CoeusSearch coeusSearch =   new CoeusSearch(parent, UNIT_SEARCH, 1);
            coeusSearch.showSearchWindow();
            HashMap hashUnit = coeusSearch.getSelectedRow();
            if(hashUnit !=null)
            {
                int selRow = tblUnitCorrespondents.getSelectedRow();
                String stUnitCode=Utils.
                        convertNull(hashUnit.get("UNIT_NUMBER"));;
                String stUnitName=Utils.
                       convertNull(hashUnit.get("UNIT_NAME"));
//                tblUnitCorrespondents.editCellAt(codeTableModel.getRowCount()-1,  1) ;
                tblUnitCorrespondents.editCellAt(selRow,  2) ;
                tblUnitCorrespondents.setValueAt(stUnitName,selRow, 1) ;
                tblUnitCorrespondents.setValueAt(stUnitCode,selRow, 0) ;
               
                if ( codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1) == null)     
                {
                    codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                    // set the user name
                    codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                    saveRequired = true;
                }
                tblUnitCorrespondents.editCellAt(selRow,  1) ;
             }
         }
         catch(Exception ex)
         {
            ex.printStackTrace() ;
         }                           
    }
     
    public void btnPersonActionPerformed()
    {
        try
        {
            CoeusSearch coeusSearch =   new CoeusSearch(parent, "PERSONSEARCH", 1);
            coeusSearch.showSearchWindow();

            HashMap hashSelectedPerson = coeusSearch.getSelectedRow();
            if ( hashSelectedPerson != null )
            {
                int selRow = tblUnitCorrespondents.getSelectedRow();
                String personId = Utils.convertNull(hashSelectedPerson.get( "PERSON_ID" ));//result.get( "PERSON_ID" )) ;
                String personName = Utils.convertNull(hashSelectedPerson.get( "FULL_NAME" ));//result.get( "FULL_NAME" )) ;

                tblUnitCorrespondents.setValueAt(personName,selRow, 3) ;
                codeTableModel.setValueAt(personId,sorter.getIndexForRow(selRow), 6) ;
                codeTableModel.setValueAt("N",sorter.getIndexForRow(selRow), 5) ;
                if ( codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1) == null)     
                {
                    codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                    // set the user name
                    codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                    saveRequired = true;
                }  
                
                tblUnitCorrespondents.editCellAt(selRow,  3) ;
            }
         }
         catch(Exception ex)
         {
            ex.printStackTrace() ;
         }     

    }
    
    public void btnRolodexActionPerformed()
    {
         try
        {
            CoeusSearch coeusSearch =   new CoeusSearch(parent, "ROLODEXSEARCH", 1);
            coeusSearch.showSearchWindow();
                HashMap hashSelectedRolodex = coeusSearch.getSelectedRow();
                if ( hashSelectedRolodex != null )
                {
                    int selRow = tblUnitCorrespondents.getSelectedRow();
                    String rolodexID = Utils.convertNull(hashSelectedRolodex.get( "ROLODEX_ID" ));//result.get( "ROLODEX_ID" ));
                    String firstName = Utils.convertNull(hashSelectedRolodex.get( "FIRST_NAME" ));//result.get( "FIRST_NAME" ));
                    String lastName = Utils.convertNull(hashSelectedRolodex.get( "LAST_NAME" ));//result.get( "LAST_NAME" ));
                    String middleName = Utils.convertNull(hashSelectedRolodex.get( "MIDDLE_NAME" ));//result.get( "MIDDLE_NAME" ));
                    String namePreffix = Utils.convertNull(hashSelectedRolodex.get( "PREFIX" ));//result.get( "PREFIX" ));
                    String nameSuffix = Utils.convertNull(hashSelectedRolodex.get( "SUFFIX" ));//result.get( "SUFFIX" ));
                    String rolodexName = null;
                    /* construct full name of the rolodex if his last name is present
                      otherwise use his organization name to display in person name
                      column of investigator table */
                    if ( lastName.length() > 0) {
                        rolodexName = ( lastName + " "+nameSuffix +", "+ namePreffix
                        +" "+firstName +" "+ middleName ).trim();
                    } else {
                        rolodexName = Utils.convertNull(hashSelectedRolodex.get("ORGANIZATION"));//result.get("ORGANIZATION"));
                    }
                   
                    tblUnitCorrespondents.setValueAt(rolodexName,selRow, 3) ;
                    codeTableModel.setValueAt(rolodexID,sorter.getIndexForRow(selRow), 6) ; 
                    codeTableModel.setValueAt("Y",sorter.getIndexForRow(selRow), 5) ; 
                    if ( codeTableModel.getValueAt(sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1) == null)     
                    {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                        saveRequired = true;
                    }
                    tblUnitCorrespondents.editCellAt(selRow,  3) ;
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

            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_UNIT_CORRESPONDENTS"))
            {
                tableStructureBeanUnitCorres = tempTableStructBean ;
            }

            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_CORRESPONDENT_TYPE"))
            {
                tableStructureBeanCorrespondents = tempTableStructBean ;
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

                HashMap hashStoredProcedure = (HashMap)tableStructureBeanUnitCorres.getHashStoredProceduresForThisTable();
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
        int rowTotal = tblUnitCorrespondents.getRowCount();
        int colTotal = tblUnitCorrespondents.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModel.getColumn(colIndex) ;
                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
             
                if (columnBean.getColumnVisible() && columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblUnitCorrespondents.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblUnitCorrespondents.getValueAt(rowIndex,colIndex).equals("")
                            || tblUnitCorrespondents.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {
                            String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblUnitCorrespondents.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "checkInputValue_exceptionCode.2402");

                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblUnitCorrespondents.changeSelection(rowIndex,  colIndex, false, false) ;
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

      while(rowCount < tblUnitCorrespondents.getRowCount())
      {// check AC_TYPE field       
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
        HashMap hashRow = new HashMap();         
        for (int colCount=0; colCount <= codeTableModel.getColumnCount()-1; colCount++)
        { // for each column u will build a hashmap
            if (colCount == 1 )
            {//unit name is not in update sp.
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
                    val = codeTableModel.getValueAt(sorter.getIndexForRow(rowNum),colCount) ;
                    if (val instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val ; 
                        val = cmbTmp.getCode() ;
                    }

                    else
                    {
                        val = val.toString() ;
                    }
                    
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
    public Vector drawTableUsingtableStructureBeanUnitCorres()
    {
        vecDeletedRows.clear();
        
        codeTableModel = new ExtendedDefaultTableModel() ;
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;


       // tblCodeTable.addFocusListener(this) ;
        tblUnitCorrespondents.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureBeanUnitCorres.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
         numColumnsDisplay = tableStructureBeanUnitCorres.getNumColumns() ;
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
              if (i == 2)//correspondents type column
              {
                  Vector vectCombData;
                  HashMap htRow = new HashMap();
                  CoeusComboBox comb = new CoeusComboBox() ;
                  vectCombData = getDataForCombo(tableStructureBeanCorrespondents);
                  if (vectCombData == null) continue;
                  comb.addItem(new ComboBoxBean("", "")) ;
                  for (int j=0; j < vectCombData.size(); j++) //loop for num of rows
                  {
                        htRow = (HashMap)vectCombData.elementAt(j);
                        if (htRow.get("QUALIFIER").toString().equals("U"))
                        {
                            //list only when qulifier is 'U'
                            comb.addItem(new ComboBoxBean(htRow.get("CORRESPONDENT_TYPE_CODE").toString() , htRow.get("DESCRIPTION").toString()));
                        }
                  }//end for

                   newColumn.setCellEditor(new StateExtendedCellEditor(comb)) ;
              }
              if (newColumnBean.getOptions()!= null )
              {
                HashMap hashOptions = newColumnBean.getOptions() ;
                CoeusComboBox comb = new CoeusComboBox() ;
                
                if (newColumnBean.getColumnCanBeNull())
                {
                    comb.addItem(new ComboBoxBean("", "")) ;
                }
                for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); )
                {    
                    String strKey =  it.next().toString() ; 
                    
                    ComboBoxBean comboBoxBean = new ComboBoxBean(strKey, hashOptions.get(strKey).toString());
                    comb.addItem(comboBoxBean) ;
                                       
                } 
                
                 newColumn.setCellEditor(new StateExtendedCellEditor(comb)) ;

              }

              if (i != 2 && newColumnBean.getOptions() == null ) 
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
              if ( i == 0) newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 10) ;

              newColumn.setMinWidth(10) ;

              // add the new columnto the column model also
              codeTableColumnModel.addColumn(newColumn) ;
              displayCodeTableColumnModel.addColumn(newColumn) ;

              // add it to the tablemodel
              codeTableModel.addColumn(newColumn);

              columnNames[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName();
            }
            TableColumn newColumn = codeTableColumnModel.getColumn(lastDisplayCol) ;
            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+"                                                                  ");

            javax.swing.table.JTableHeader header
                            = tblUnitCorrespondents.getTableHeader();
                header.setReorderingAllowed(false);
                header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));

  
            tblUnitCorrespondents.setColumnModel(displayCodeTableColumnModel); // also give the structure



            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureBeanUnitCorres and get the DataBean from the servlet.
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
                    if (j == 2 )
                     {
                         Vector vectCombData;
                         vectCombData = getDataForCombo(tableStructureBeanCorrespondents);
                         
                         if (vectCombData != null)
                         {
                             if (rowColumnDatas[j] != null)
                             {
                                String temp = rowColumnDatas[j].toString();
                            /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectCombData.size(); codeIndex++)
                                {
                                    HashMap hmRow = (HashMap)vectCombData.elementAt(codeIndex);

                                    if (hmRow.get("CORRESPONDENT_TYPE_CODE").toString().equals(temp))
                                    {
                                        rowColumnDatas[j] = new ComboBoxBean(hmRow.get("CORRESPONDENT_TYPE_CODE").toString() , hmRow.get("DESCRIPTION").toString());
                                        break;
                                    }

                                }
                             }
                         }
                     }
                    if (columnBean.getOptions()!= null ) 
                     {
                         HashMap hashOptions = columnBean.getOptions() ;
                         for (Iterator it=hashOptions.keySet().iterator(); it.hasNext(); )
                         {    
                            String strKey =  it.next().toString() ; 
                            if (rowColumnDatas[j] != null)
                            {  
                                if (rowColumnDatas[j].toString().equals(strKey))
                                {
                                    // add comboboxbean itself to the data
                                    rowColumnDatas[j] =   new ComboBoxBean(strKey, hashOptions.get(strKey).toString()) ; 
                               
                                }    
                            }    
                            
                          }   
                     }
                     
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
       tblUnitCorrespondents.setModel(sorter) ;
       
       // hide unwanted columns
       int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;
       //for (int colCount=0; colCount <= availableNumOfCols ; colCount++)
       for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)
       {
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblUnitCorrespondents.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblUnitCorrespondents.removeColumn(inColumn );
            }
       }
       if (vdata.size()>0)
       {
            tblUnitCorrespondents.setRowSelectionInterval(0,0) ;
            tblUnitCorrespondents.repaint() ;
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
          if(tableStructureBeanUnitCorres == null)
            System.out.println("tableStructure == null");
          //Get all stored procedures associated with this table.
          HashMap hashStoredProcedure =
            (HashMap)tableStructureBeanUnitCorres.getHashStoredProceduresForThisTable();
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
//                           if (tblUnitCorrespondents.getValueAt(tblUnitCorrespondents.getSelectedRow(),0) != null)
//                           {
//                             stId =  tblUnitCorrespondents.getValueAt(tblUnitCorrespondents.getSelectedRow(),0).toString() ;
//                           }
//                           else
//                           {
//                            stId = "" ;
//                           }

                           if (me.getClickCount() == 2)
                           {
                             if (txtCell.getText() == null || txtCell.getText().trim().equals(""))
                             {
                                if (tblUnitCorrespondents.getSelectedColumn() == 0) //click on the first column,  show the search screen
                                {
                                    btnUnitActionPerformed();
                                }//end if
                                 
                             }
                             else if (tblUnitCorrespondents.getSelectedColumn() == 0 || tblUnitCorrespondents.getSelectedColumn() == 1) 
                             {
                                 // check if itz a valid Unit id n display the Unit screen
                                 String stId = tblUnitCorrespondents.getValueAt(tblUnitCorrespondents.getSelectedRow(),0).toString() ;
                                 UnitDetailForm frmUnit =  new UnitDetailForm(stId,'G');
                                 frmUnit.showUnitForm((CoeusAppletMDIForm)parent );
                             }
                             else if (tblUnitCorrespondents.getSelectedColumn() == 3) //click on person name
                             {
                                String stPersonId = (String)codeTableModel.getValueAt(sorter.getIndexForRow(tblUnitCorrespondents.getSelectedRow()), 6);
//                                if(stPersonId.trim().length() != 9)
                                if (codeTableModel.getValueAt(sorter.getIndexForRow(tblUnitCorrespondents.getSelectedRow()), 5).toString().equalsIgnoreCase("Y"))
                                {
                                    /* selected investigator is a rolodex, so show
                                       rolodex details */
                                    RolodexMaintenanceDetailForm frmRolodex 
                                       = new RolodexMaintenanceDetailForm('V',stPersonId);
                                    frmRolodex.showForm((CoeusAppletMDIForm)parent,"DISPLAY ROLODEX",true);
                                }else
                                {
                                  try
                                  {
//                                      String loginUserName = (parent.getParent()).getUserName();
                                      
                                      //Bug Fix: Pass the person id to get the person details Start 
                                      //String personName=(String)tblUnitCorrespondents.getValueAt(tblUnitCorrespondents.getSelectedRow(), 3);
                                      /*Bug Fix:to get the person details with the person id instead of the person name*/
                                      //PersonInfoFormBean personInfoFormBean = (PersonInfoFormBean)coeusUtils.getPersonInfoID(personName);
                                      //PersonDetailForm personDetailForm = 
                                        //new PersonDetailForm(personInfoFormBean.getPersonID(),userId,'D');

                                      String strPersonId = (String)codeTableModel.getValueAt(sorter.getIndexForRow(tblUnitCorrespondents.getSelectedRow()), 6);
                                      PersonDetailForm personDetailForm = new PersonDetailForm(stPersonId,userId,'D');
                                      
                                      //Bug Fix: Pass the person id to get the person details End 
                                  }catch(Exception exception)
                                  {
                                      exception.printStackTrace();
                                  }
                                }
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



        /* This method is used to get the Unit information
     * based on the Unit code. It instantiates the RequesterBean class
     * invokes the coeusFunctionsServlet and retrieves information from the database.
     * @param id , a UnitNumber on which the information to retrieve
     */
    private Vector getUnitInfo(String id){
        Vector vsearchData=null;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_UNITINFO");
        requester.setId(id);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            vsearchData = new Vector();
            UnitDetailFormBean unitInfo =
            (UnitDetailFormBean) response.getDataObject();

            if(unitInfo!=null && unitInfo.getUnitName() !=null){
                String stName = unitInfo.getUnitName();
                vsearchData.addElement(stName);
            }
        }
        return vsearchData;
    }
    
    

    //supporting method to get PersonInfoBean for specific person which will
    //validate the person name against the db value
    private PersonInfoFormBean getNameInfo( String name ){
        boolean success=false;         
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_PERSONINFO");
        requester.setId(name);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        PersonInfoFormBean personInfoFormBean = null;
        if ( response!=null ){
            success=true;
            personInfoFormBean = (PersonInfoFormBean) response.getDataObject();
        }
        
        return personInfoFormBean;
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

            if (column == 2 )
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
           
            
            TableColumn tColumn = codeTableColumnModel.getColumn(selColumn) ;
//            TableColumn tColumn = tblUnitCorrespondents.getColumn(selColumn) ;
            
            ColumnBean columnBean = (ColumnBean)tColumn.getIdentifier() ;
            
            if (columnBean.getOptions()!= null ) 
            {
//                return (Component)((CoeusComboBox)getComponent());
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
            Object oldValue = tblUnitCorrespondents.getValueAt(selRow,selColumn);
            Object newValue = ((CoeusTextField)txtCell).getText();
            
            TableColumn column = codeTableColumnModel.getColumn(selColumn) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            

            if (oldValue instanceof ComboBoxBean | columnBean.getOptions()!= null) 
            {
//                ComboBoxBean cmbTmp = (ComboBoxBean)oldValue ; 
//                oldValue = cmbTmp.getCode() ;
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            //check Unique key if the column is an editable PrimaryKey
            if (!tableStructureBeanUnitCorres.isIdAutoGenerated()
                && (selColumn == 0 || selColumn == 2 || selColumn == 3))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString(),selRow, selColumn))
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "chkPKeyUniqVal_exceptionCode.2401");

                        CoeusOptionPane.showInfoDialog(msg);  
                        if(newValue.equals(oldValue)){
                            oldValue=null;
                        }
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
                    tblUnitCorrespondents.setValueAt("",selRow,0);
                    tblUnitCorrespondents.setValueAt("",selRow,1);
                    if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                    {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                        saveRequired = true;
                    }
                    System.out.println("strId is empty space here.") ;
               }else
               {   if (!oldValue.equals(newValue))
                   {
                      Vector vecData= getUnitInfo(strId);
                      if(!((vecData.isEmpty()) || (vecData.size()<0)))
                      {
                        String stNm=(String)vecData.get(0);
                        tblUnitCorrespondents.setValueAt(strId,selRow,0);
                        tblUnitCorrespondents.setValueAt(stNm,selRow,1);
                        
                        if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                            saveRequired = true;
                        }
                        System.out.println("setValueAt. saveRequired = true;") ;
                      }
                      else
                      {
                        JOptionPane.showMessageDialog(parent,
                            coeusMessageResources.parseMessageKey(
                                        "protoFndSrcFrm_exceptionCode.1133"),
                         "Coeus",
                        JOptionPane.ERROR_MESSAGE);
                        txtCell.setText((String)oldValue);
                        tblUnitCorrespondents.setValueAt(oldValue,selRow,0);
                      }
                    }
               }
            }
            
            if (selColumn == 3)
            {
                String strName = txtCell.getText().trim();

               if (strName.equals(""))
               {
                    txtCell.setText("");
                    tblUnitCorrespondents.setValueAt("",selRow,3);
                    codeTableModel.setValueAt("",sorter.getIndexForRow(selRow),6);
                    codeTableModel.setValueAt("",sorter.getIndexForRow(selRow), 5) ;
                    if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                    {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                        saveRequired = true;
                    }
                    System.out.println("person name is empty space here.") ;
               }else
               {   if ( oldValue == null || !oldValue.equals(newValue))
                   {
                      PersonInfoFormBean personInfo = null; 
                      personInfo = getNameInfo(strName);
                      if( personInfo == null || personInfo.getPersonID() == null)
                      { 
                        JOptionPane.showMessageDialog(parent,
                            coeusMessageResources.parseMessageKey(
                                        "protoCorroFrm_exceptionCode.1054"),
                         "Coeus",
                        JOptionPane.ERROR_MESSAGE);
                        txtCell.setText((String)oldValue);
                        tblUnitCorrespondents.setValueAt(oldValue,selRow,3);                          
                      }
                      else
                      {
                        String stPersonId= personInfo.getPersonID();
                        String stPersonName = personInfo.getFullName();
                        if(stPersonName.equalsIgnoreCase(newValue.toString())){
                           stPersonId = null; 
                           stPersonName = null;
                        }
                        codeTableModel.setValueAt(stPersonId,sorter.getIndexForRow(selRow),6);
                        // only allow typing a name from the person table
                        codeTableModel.setValueAt("N",sorter.getIndexForRow(selRow), 5) ;// NON EMPLOYEE FLAG
                        tblUnitCorrespondents.setValueAt(stPersonName,selRow,3);
                        
                        if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                            saveRequired = true;
                        }
                        System.out.println("setValueAt. saveRequired = true;") ;
                      }

                    }
               }
            }

            //when the cell value changed,
            //set AC_TYPE only the this row is first time be modified.
            //"U" means this row needs to update.
//            {
                if (codeTableModel.getValueAt(sorter.getIndexForRow(selRow),codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblUnitCorrespondents.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblUnitCorrespondents.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                            // set the user name
                            codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                            saveRequired = true;
                            System.out.println("*** Set AcType to U  in getCellEditorValue***") ;
                        }
                    }
                    else
                    {
                        codeTableModel.setValueAt("U", sorter.getIndexForRow(selRow), codeTableModel.getColumnCount()-1);
                        // set the user name
                        codeTableModel.setValueAt(userId, sorter.getIndexForRow(selRow), tableStructureBeanUnitCorres.getUserIndex() );
                        saveRequired = true;
                        System.out.println("*** Set AcType to U  in getCellEditorValue***") ;
                    }
                }

//            }

            if (columnBean.getOptions()!= null | selColumn == 2) 
            {
                return (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
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

            if (!tableStructureBeanUnitCorres.isIdAutoGenerated()
                && (selColumn == 0 || selColumn == 2 || selColumn == 3))
            {
                if (checkDependency(selRow, ""))
                {
                    if(!CheckUniqueId(newValue.toString(),selRow, selColumn))
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
    int colIdx = tableStructureBeanUnitCorres.getPrimaryKeyIndex(0) ;
    int rowTotal = tblUnitCorrespondents.getRowCount();
    int row = tblUnitCorrespondents.getEditingRow();
    if (rowTotal > 0 && row >= 0)
    {
        Vector vecPrimaryKey = tableStructureBeanUnitCorres.getPrimaryKeyIndex();
        if  (vecPrimaryKey.size() == 3)
        {   
            int colIdx1 ;    
            int colIdx2 ; 
            int colIdx3 ; 
            //keep selected column number in colIdx1
            if (selCol == tableStructureBeanUnitCorres.getPrimaryKeyIndex(0))
            {
//                colIdx1 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(0) ; 
                colIdx1 = selCol;
                colIdx2 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(1) ; 
                colIdx3 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(2) ;
            }
            else if (selCol == tableStructureBeanUnitCorres.getPrimaryKeyIndex(1))
            {
//                colIdx1 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(1) ;  
                colIdx1 = selCol;
                colIdx2 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(0) ;
                colIdx3 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(2) ;
            }
            else
            {
//                colIdx1 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(2) ;  
                colIdx1 = selCol;
                colIdx2 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(0) ;
                colIdx3 = tableStructureBeanUnitCorres.getPrimaryKeyIndex(1) ;
            }

            Object valTemp = codeTableModel.getValueAt(sorter.getIndexForRow(selRow), colIdx2);
            Object valTemp2 = codeTableModel.getValueAt(sorter.getIndexForRow(selRow), colIdx3);
            if ( valTemp instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp ; 
                valTemp = cmbTmp.getCode() ;
            }
            
            if ( valTemp2 instanceof ComboBoxBean)
            {
                ComboBoxBean cmbTmp = (ComboBoxBean)valTemp2 ; 
                valTemp2 = cmbTmp.getCode() ;
            }
            
            for (int index = 0; index < rowTotal ; index++)
            {
                if (index != row)
                {
                    Object val1 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx1);
                    Object val2 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx2);
                    Object val3 = codeTableModel.getValueAt(sorter.getIndexForRow(index), colIdx3);
                    
                    if (val1 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val1 ; 
//                        val1 = cmbTmp.getCode() ;
                        val1 = cmbTmp.getDescription() ;//because the newVal is description
                    }  
                    
                    if (val2 instanceof ComboBoxBean)
                    {
                        ComboBoxBean cmbTmp = (ComboBoxBean)val2 ; 
                        val2 = cmbTmp.getCode() ;
                    }  
                    if (val3 instanceof ComboBoxBean)
                    { 
                        ComboBoxBean cmbTmp = (ComboBoxBean)val3;
                        val3 = cmbTmp.getCode() ;
                    }  
                    
                    if (val1 != null && val2 != null && val3 != null && valTemp != null && valTemp2 != null)
                    {
                        if (newVal.toString().equalsIgnoreCase(val1.toString()) && 
                            valTemp.toString().equals(val2.toString()) &&
                            valTemp2.toString().equals(val3.toString()))
                            
                            return false;                                         
                            
                           
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
    int colNumber = tableStructureBeanUnitCorres.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureBeanUnitCorres.getHashTableDependency();
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
            hmDependencyCheckValues.put("a_column_value",new Integer(tblUnitCorrespondents.getValueAt(rowNumber, colNumber).toString()));
            requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
         }
        else
        {
            hmDependencyCheckValues.put("a_column_value",tblUnitCorrespondents.getValueAt(rowNumber, colNumber).toString());
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

            String tableName =  hmDependency.get("Table").toString() ;   //tableStructureBeanUnitCorres.getActualName();
            CoeusOptionPane.showInfoDialog(msg + tableName + msg1 );
            //once found the dependency, just return
            return false;
        }
    }
    return true;
}


 private void GetLastValue()
 {
    int row = tblUnitCorrespondents.getEditingRow();
    int col = tblUnitCorrespondents.getEditingColumn();
    if (row != -1 && col != -1)
    {
        tblUnitCorrespondents.getCellEditor(row, col).stopCellEditing();
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
// supporting class to display unit details or person details on double clicking any row of unit number/name or person column
// in the table.
    class UnitPersonDisplayAdapter extends MouseAdapter {
        public void mouseClicked( MouseEvent me ) {
            int selRow = tblUnitCorrespondents.getSelectedRow();
            int selCol = tblUnitCorrespondents.getSelectedColumn();
            if (me.getClickCount() == 2) {
                if (selCol == 0 ||selCol == 1 ){
                    String unitId= (String)tblUnitCorrespondents.getModel().getValueAt(selRow,0);
                    if(unitId != null && unitId.trim().length() >0){
                        try{
                            UnitDetailForm frmUnit =
                            new UnitDetailForm(unitId,'G');
                            frmUnit.showUnitForm((CoeusAppletMDIForm)parent );
                        } catch(Exception ex){
                            CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoInvFrm_exceptionCode.1136"));
                        }
                        ((StateExtendedCellEditor)tblUnitCorrespondents.getCellEditor(selRow,
                        0)).cancelCellEditing();
                    }
                        
                }else if (selCol == 3){
                    String stPersonId = (String)codeTableModel.getValueAt(sorter.getIndexForRow(tblUnitCorrespondents.getSelectedRow()), 6);
                    if (codeTableModel.getValueAt(sorter.getIndexForRow(tblUnitCorrespondents.getSelectedRow()), 5).toString().equalsIgnoreCase("Y")){
                    /* selected investigator is a rolodex, so show rolodex details */
                        RolodexMaintenanceDetailForm frmRolodex 
                                = new RolodexMaintenanceDetailForm('V',stPersonId);
                        frmRolodex.showForm((CoeusAppletMDIForm)parent,"DISPLAY ROLODEX",true);
                    }else{
                        PersonDetailForm personDetailForm = new PersonDetailForm(stPersonId,userId,'D');
                    }
                    
                }
                    
            }
        }
    }

}