/*
 * @(#)FormIacucCorrespBatch.java 1.0 11/02/2010
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 10-NOV-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.codetable.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.beans.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;


public class FormIacucCorrespBatch extends CoeusDlgWindow implements ActionListener {


    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources; 
    private boolean saveRequired = false;
    int selectedBatchRow = -1;
   
    //hodls the correspondence batch list slection instance
    private ListSelectionModel batchSelectionModel;
    
    //holds the list selection model for corresp_batch_detail table   

    ExtendedDefaultTableModel codeTableModel,codeTableModelBatchDetail; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ,codeTableColumnModelBatchDetail; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel,displayCodeTableColumnModelBatchDetail ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tableStructureCorrespBatch;
    TableStructureBean tableStructureCorrespDetail;
    TableStructureBean tableStructureProtoCorrespType;
    TableStructureBean tableStructureFinalActionType;
    
    CoeusComboBox combShare = new CoeusComboBox() ;
    
    Vector vectFinalActionCorrespType = null;
    Vector vectFinalActionType = null;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    private final String DESCRIPTION = "DESCRIPTION";
    private final String PROTO_CORRESP_TYPE_CODE = "PROTO_CORRESP_TYPE_CODE";
    private final String NUMBER = "NUMBER";
    private final String DOT_UNDERSCORE = ". -";
    private final String EXCEPTION_DESCRIPTION = "checkInputValue_exceptionCode.2402";
    String[] columnNames = null ;
    String[] columnNamesBatchDetail = null ;
    Vector vecColNameBD = new Vector();
   
    int numColumnsDisplay = 0 ;
    int numColumnsDisplayBatchDetail = 0 ;
    Vector vecDeletedRows = new Vector();
    Vector vecDeletedRowsBatchDetail = new Vector();
    Vector vecBatchDetail = new Vector();
    Vector vecBatchDetailAll = new Vector();
    Vector vectProtoCorrespType = null;
    String   userId;
    private boolean userHasRights = false;
    
    /** Creates new form FormIacucCorrespBatch */
    public FormIacucCorrespBatch(Frame parent, String title, boolean modal,boolean userRights, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        this.allCodeTablesBean = allCodeTablesBean ;
        this.userHasRights = userRights;
        initComponents();        
        postInitComponents();
        if (!userRights){
            formatFields();
        }
    }
   
    /*
    * This method will initialize the form componenet and tables.   
    * @return Void
    */ 
    public void postInitComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        setResizable(false);
        setSize(575, 475) ;    
        
        setTableEditors();
         initialiseData() ;
        // populate the table
        drawTableForBatch() ;
        drawTableForBatchDetail();
        vecBatchDetailAll = (Vector)codeTableModelBatchDetail.getDataVector().clone()  ;
        iniBatchDetailRow();

        batchSelectionModel = tblCorrespBatch.getSelectionModel();
        batchSelectionModel.addListSelectionListener(new ListSelectionListener() 
        {
            public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) 
            {
                int newSelectedBatchRow = tblCorrespBatch.getSelectedRow();

                if (newSelectedBatchRow >= 0 )
                {
                    if ( tblCorrespDetail.getEditingColumn() >= 0 )
                    {
                        tblCorrespDetail.getCellEditor().stopCellEditing(); 
                    }
                    //do validation here
                    if (selectedBatchRow != newSelectedBatchRow && selectedBatchRow != -1)
                    { // not in the case delete or already did validation
                        if (!(checkBatchRow(selectedBatchRow))){
                            return;
                        }
                        if (!checkDetailRows())
                        {
                            tblCorrespBatch.changeSelection(selectedBatchRow, 0, false, false);
                            return;
                        }
                    }
                                     
                        
                    Object  batchTypeCode = codeTableModel.getValueAt(newSelectedBatchRow, 0);
                    if (codeTableModelBatchDetail.getRowCount() > 0 && 
                        !codeTableModelBatchDetail.getValueAt(0,0).equals(batchTypeCode))
                    {
                        //first save detail rows to the vector
                        vecBatchDetail = null ;
                        vecBatchDetail = (Vector)codeTableModelBatchDetail.getDataVector() ;
                        int j = vecBatchDetailAll.size();
                        int k = vecBatchDetail.size();
                        for (int i = 0; i < k; i++  )
                        {
                             vecBatchDetailAll.add(j+i, vecBatchDetail.get(i));
                        }
                        
                        //then clear the detail rows
                        for(int i = codeTableModelBatchDetail.getRowCount() - 1; i >= 0; i--)
                        {
                            codeTableModelBatchDetail.removeRow(i);
                        }
                    
                    }
                    
                    //and need to insert new rows to detailtable
                    int k = vecBatchDetailAll.size();
                    for (int i = 0 ; i < k; i++ )
                    {
                        if (((Vector)vecBatchDetailAll.elementAt(i)).elementAt(0).equals(batchTypeCode))
                        {
                            codeTableModelBatchDetail.addRow((Vector)vecBatchDetailAll.elementAt(i));
                            vecBatchDetailAll.remove(i);
                            k--;
                            i--;
                         }
                    }
                    
                    selectedBatchRow = newSelectedBatchRow;
                }
            
            }
        });
        
        btnOk.addActionListener( this );
        btnCancle.addActionListener( this );
        btnAdd.addActionListener( this );
        btnDelete.addActionListener( this );
        btnAddDetail.addActionListener( this );
        btnDeleteDetail.addActionListener( this );

        // this will take care of the window closing...
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                performCancle();

            }
        } ); 
    }
    
   /*
    * This method will set the button visible false    
    * @return Void
    */  
    private void formatFields()
    {
        btnOk.setEnabled(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAddDetail.setEnabled(false);
        btnDeleteDetail.setEnabled(false);
    }
    
    /*
     * This method will draw the table editor and its field length        
     * @return Void
     */ 
    private void setTableEditors(){
        
        tblCorrespBatch.setOpaque(false);
        tblCorrespBatch.setShowVerticalLines(false);
        tblCorrespBatch.setShowHorizontalLines(false);
        tblCorrespBatch.setRowHeight(22);        
        tblCorrespBatch.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCorrespBatch.setFont( CoeusFontFactory.getNormalFont() );
        JTableHeader header = tblCorrespBatch.getTableHeader();
        header.setReorderingAllowed(false);        
        
        tblCorrespDetail.setOpaque(false);
        tblCorrespDetail.setShowVerticalLines(false);
        tblCorrespDetail.setShowHorizontalLines(false);
        tblCorrespDetail.setRowHeight(22);
     
        tblCorrespDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblCorrespDetail.setFont( CoeusFontFactory.getNormalFont() );
        JTableHeader headerDetail = tblCorrespDetail.getTableHeader();
        headerDetail.setReorderingAllowed(false);
     
    }
    
    /*
     * This method will initialize the Batch Details data
     */ 
    public void initialiseData() {
        hashAllCodeTableStructure  = allCodeTablesBean.getHashAllCodeTableStructure() ;
        TableStructureBean tempTableStructBean = null ;
        for (int tableCount =0 ; tableCount < hashAllCodeTableStructure.size() ; tableCount++) {
            tempTableStructBean  = (TableStructureBean)hashAllCodeTableStructure.get(new Integer(tableCount)) ;
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$AC_CORRESPONDENCE_BATCH")) {
                tableStructureCorrespBatch = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_CORRESP_BATCH_DETAIL")) {
                tableStructureCorrespDetail = tempTableStructBean ;
            }
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_PROTOCOL_CORRESP_TYPE")) {
                tableStructureProtoCorrespType = tempTableStructBean ;                
            }
                           
            if (tempTableStructBean.getActualName().equalsIgnoreCase("OSP$AC_PROTOCOL_ACTION_TYPE")) {
                tableStructureFinalActionType = tempTableStructBean ;
            }            
        }// end for        
    }
    
     /**
     * This method will draw the table for Batch
     * @param TableStructureBean thisTblStrBean     
     * @return Vector vdata
     */ 
    public Vector drawTableForBatch() 
    {
        vecDeletedRows.clear();

        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ; 
        codeTableModel = new ExtendedDefaultTableModel(codeTableColumnModel) ;
        tblCorrespBatch.setModel(codeTableModel);
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureCorrespBatch.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
         numColumnsDisplay = tableStructureCorrespBatch.getNumColumns() ;
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
              if (i == 3){
                HashMap htRow = new HashMap();
                CoeusComboBox comb = new CoeusComboBox() ;
                getDataForTheTable(tableStructureFinalActionType);
                vectFinalActionType = accDataBean.getVectData();
                if (vectFinalActionType == null) {
                    continue;
                }
                comb.addItem(new ComboBoxBean("", "")) ;
                for (int j=0; j < vectFinalActionType.size(); j++) //loop for num of rows
                {
                    htRow = (HashMap)vectFinalActionType.elementAt(j);
                    comb.addItem(new ComboBoxBean(htRow.get("PROTOCOL_ACTION_TYPE_CODE").toString() , htRow.get(DESCRIPTION).toString()));
                }//end for  

                newColumn.setCellEditor(new ExtendedCellEditor(comb)) ;
                  
              }
              else if (i == 4 ){  
                HashMap htRowShare = new HashMap();
                getDataForTheTable(tableStructureProtoCorrespType);
                vectProtoCorrespType = accDataBean.getVectData();// for deatal use
                vectFinalActionCorrespType = accDataBean.getVectData();
                if (vectFinalActionCorrespType == null){
                    continue;
                }
                combShare.addItem(new ComboBoxBean("", "")) ;
                for (int j=0; j < vectFinalActionCorrespType.size(); j++) //loop for num of rows
                {
                    htRowShare = (HashMap)vectFinalActionCorrespType.elementAt(j);
                    combShare.addItem(new ComboBoxBean(htRowShare.get(PROTO_CORRESP_TYPE_CODE).toString() , htRowShare.get(DESCRIPTION).toString()));
                }//end for  

                newColumn.setCellEditor(new ExtendedCellEditor(combShare)) ;
  
              }
              else{
                  newColumn.setCellEditor(new ExtendedCellEditor(newColumnBean, i)) ;
              }             
              if (i == 0) // corresp_batch_type_code
              {
                   newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 15) ;
              }
              if (i == 1) // default_time_window
              {
                   newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 30) ;
              }
              if (i == 2) // description
              {          
                  newColumn.setPreferredWidth(70) ;
              }
              if (i == 3  )
              {
                   newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 45) ;
              }
              if ( i == 4 ) 
              {
                   newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 100) ;
              }            
              newColumn.setMinWidth(10) ;
                 
              // add the new columnto the column model also
              codeTableColumnModel.addColumn(newColumn) ;
              displayCodeTableColumnModel.addColumn(newColumn) ;

              // add it to the tablemodel
              codeTableModel.addColumn(newColumn);
              
              columnNames[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName(); }
            TableColumn newColumn = codeTableColumnModel.getColumn(lastDisplayCol) ;            
            newColumn.setPreferredWidth(100) ;
            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+" "); 
            
            javax.swing.table.JTableHeader header
                            = tblCorrespBatch.getTableHeader();
                header.setReorderingAllowed(false);
                header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
                
            tblCorrespBatch.setColumnModel(displayCodeTableColumnModel); // also give the structure
                   
            getDataForTheTable(tableStructureCorrespBatch);
            
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
                     if (j == 3 ) 
                    {                       
                         if (vectFinalActionType != null)
                         {
                             if (rowColumnDatas[j] != null)
                             {   
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectFinalActionType.size(); codeIndex++)
                                {
                                    HashMap hmRow = (HashMap)vectFinalActionType.elementAt(codeIndex);
                                                                      
                                    if (hmRow.get("PROTOCOL_ACTION_TYPE_CODE").toString().equals(temp))
                                    {
                                        rowColumnDatas[j] = new ComboBoxBean(hmRow.get("PROTOCOL_ACTION_TYPE_CODE").toString() , hmRow.get(DESCRIPTION).toString());
                                        break;
                                    }
                                    
                                }

                             }                       
                         }                     
                     }
                    if (j == 4 ) 
                    {                       
                         if (vectFinalActionCorrespType != null)
                         {
                             if (rowColumnDatas[j] != null)
                             {   
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectFinalActionCorrespType.size(); codeIndex++)
                                {
                                    HashMap hmRow = (HashMap)vectFinalActionCorrespType.elementAt(codeIndex);
                                                                      
                                    if (hmRow.get(PROTO_CORRESP_TYPE_CODE).toString().equals(temp))
                                    {
                                        rowColumnDatas[j] = new ComboBoxBean(hmRow.get(PROTO_CORRESP_TYPE_CODE).toString() , hmRow.get(DESCRIPTION).toString());
                                        break;
                                    }                                    
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
        
      
       // hide unwanted columns
       int availableNumOfCols = displayCodeTableColumnModel.getColumnCount()-1 ;      
       for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)    
       {  
            TableColumn column = displayCodeTableColumnModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblCorrespBatch.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblCorrespBatch.removeColumn(inColumn ); 
            }
       }
       if (vdata.size()>0)
       {  
            tblCorrespBatch.setRowSelectionInterval(0,0) ;
            tblCorrespBatch.repaint() ;
       }   

       return vdata;
    }

    /**
     * This method will draw the table for Batch Details
     * @param TableStructureBean thisTblStrBean     
     * @return Vector vdata
     */ 
     public Vector drawTableForBatchDetail() 
    {
        vecDeletedRowsBatchDetail.clear();
        
        codeTableColumnModelBatchDetail = new DefaultTableColumnModel() ;
        displayCodeTableColumnModelBatchDetail = new DefaultTableColumnModel() ; 
         codeTableModelBatchDetail = new ExtendedDefaultTableModel(codeTableColumnModelBatchDetail) ;
        tblCorrespDetail.setModel(codeTableModelBatchDetail);
 
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        HashMap hmTableColumnBean = tableStructureCorrespDetail.getHashTableColumns();
        // get the number of how many columns are going to display on the screen.
         numColumnsDisplayBatchDetail = tableStructureCorrespDetail.getNumColumns() ;
        //need to add a AC_TYPE column, that's +1
        columnNamesBatchDetail = new String[numColumnsDisplayBatchDetail + 1];
        
        int lastDisplayCol = 0;
        
        // populate the column header name on the the screen
        if (numColumnsDisplayBatchDetail > 0)
        {
           
            for (int i = 0; i < numColumnsDisplayBatchDetail; i++)
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
              if (i == 3)
              { 
                  newColumn.setCellEditor(new ExtendedCellEditorBatchDetail(combShare)) ;                  
              }
              else
              {
                newColumn.setCellEditor(new ExtendedCellEditorBatchDetail(newColumnBean, i)) ;
              }
              
              if (i == 0) // corresp_batch_type_code
              {
                newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 15) ;
              }
              if (i == 1) // correspondence number
              {
                newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 28) ;
              }
              if (i == 2) // no_of_days
              {
                newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 20) ;
              }
               if (i == 3) // ComboBox
              {
                newColumn.setPreferredWidth(newColumnBean.getDisplaySize()* 95) ;
              }

              newColumn.setMinWidth(10) ;
                 
              // add the new columnto the column model also
              codeTableColumnModelBatchDetail.addColumn(newColumn) ;
              displayCodeTableColumnModelBatchDetail.addColumn(newColumn) ;

              // add it to the tablemodel
              codeTableModelBatchDetail.addColumn(newColumn);
              
              columnNamesBatchDetail[i] = ((ColumnBean)hmTableColumnBean.get(new Integer(i))).getColumnName();}
            TableColumn newColumn = codeTableColumnModelBatchDetail.getColumn(lastDisplayCol) ;            
            newColumn.setPreferredWidth(220) ;
            newColumn.setHeaderValue(" " + newColumn.getHeaderValue().toString().trim()+" "); 
            
            javax.swing.table.JTableHeader header
                            = tblCorrespDetail.getTableHeader();
                header.setReorderingAllowed(false);
                header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
                
                tblCorrespDetail.setColumnModel(displayCodeTableColumnModelBatchDetail); // also give the structure
       
            getDataForTheTable(tableStructureCorrespDetail);
            
            // populate the data on the the screen
            vdata = accDataBean.getVectData();
            HashMap htRow = new HashMap();
                
            for (int i=0; i < vdata.size(); i++) //loop for num of rows
            {
                htRow = (HashMap)vdata.elementAt(i);
                Object [] rowColumnDatas = new Object[numColumnsDisplayBatchDetail];
                  
                for (int j = 0; j < numColumnsDisplayBatchDetail; j++) // loop for num of columns will display on screen
                {
                    rowColumnDatas[j] = (Object)htRow.get(columnNamesBatchDetail[j]);
                    TableColumn column = codeTableColumnModelBatchDetail.getColumn(j) ;
                    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
                    
                    Object obj = rowColumnDatas[j];
                    if (j == 3 ) 
                    {                       
                         if (vectProtoCorrespType != null)
                         {
                             if (rowColumnDatas[j] != null)
                             {   
                                String temp = rowColumnDatas[j].toString();
                                /* get the code for the selected description for combobox */
                                for(int codeIndex=0;codeIndex<vectProtoCorrespType.size(); codeIndex++)
                                {
                                    HashMap hmRow = (HashMap)vectProtoCorrespType.elementAt(codeIndex);
                                                                      
                                    if (hmRow.get(PROTO_CORRESP_TYPE_CODE).toString().equals(temp))
                                    {
                                        rowColumnDatas[j] = new ComboBoxBean(hmRow.get(PROTO_CORRESP_TYPE_CODE).toString() , hmRow.get(DESCRIPTION).toString());
                                        break;
                                    }                                    
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
                
                codeTableModelBatchDetail.addRow(rowColumnDatas);
            }            
             
       } 
        
      
       // hide unwanted columns
       int availableNumOfCols = displayCodeTableColumnModelBatchDetail.getColumnCount()-1 ;        
       for (int colCount=availableNumOfCols ; colCount >= 0 ; colCount--)    
       {  
            TableColumn column = displayCodeTableColumnModelBatchDetail.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

            if (columnBean.getColumnVisible() == false) // hide columns with visble property as false
            {
                TableColumnModel columnModel = tblCorrespDetail.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblCorrespDetail.removeColumn(inColumn ); 
            }
       }
       if (vdata.size()>0)
       {  
            tblCorrespDetail.setRowSelectionInterval(0,0) ;
            tblCorrespDetail.repaint() ;
       }   
      
       return vdata;
       
    }

    /**
     * This method will return the data object of all existing Batch correspondence data.
     * @param TableStructureBean thisTblStrBean     
     * @return Void
     */ 
    private void getDataForTheTable(TableStructureBean thisTblStrBean)
    {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        
      Vector vectData = null;
      try
      {
          HashMap hashStoredProcedure = null;
          if(thisTblStrBean!= null){         
          //Get all stored procedures associated with this table.
          hashStoredProcedure = (HashMap)thisTblStrBean.getHashStoredProceduresForThisTable();
          }
          if(hashStoredProcedure != null){           

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
            vectData = accDataBean.getVectData();           
          }
      }
      }
      catch(Exception ex)
      {
          ex.printStackTrace() ;
        
      }          
      accDataBean.setVectData(vectData);     
    
    }
  
    
    /**
     * This method will return the data object of all existing Batch correspondence data.     
     * @param value object request.      
     * @return Object response
     */
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
        ex.printStackTrace() ;
       }

       return response ;
   }
    
    
 class ExtendedDefaultTableModel extends DefaultTableModel   
 {
     DefaultTableColumnModel thisColumnModel;
    public ExtendedDefaultTableModel(DefaultTableColumnModel columnModel)
    {
        thisColumnModel = columnModel;
    }
   
     public boolean isCellEditable(int row, int col)
     {
        boolean isCellEditable = false;
        if (!userHasRights){
            isCellEditable = false;
        }else{
            TableColumn column = thisColumnModel.getColumn(col) ;
            ColumnBean columnBean = (ColumnBean) column.getIdentifier();
            isCellEditable =  columnBean.getColumnEditable() ;
        }
       return isCellEditable; 
     }
          
 }// end inner class
    
 private class ExtendedCellEditor extends DefaultCellEditor implements TableCellEditor
  {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        //case 1358 begin
        public ExtendedCellEditor(CoeusComboBox comb)
        {
            super(comb);
            comb.addFocusListener(new FocusAdapter()
            {
                       
                public void focusGained(FocusEvent e)
                {                           
                }
                       
                public void focusLost(FocusEvent fe)
                {
                    if (!fe.isTemporary())
                    {                      
                        GetLastValue(); 
                    }// end if
                }// end focus lost
            }); // end focus listener 
        }       
        public ExtendedCellEditor(ColumnBean columnBeanIn, int colIndex)
        {
            super( new CoeusTextField() );

            if (columnBeanIn.getDataType().equals(NUMBER))
            {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,columnBeanIn.getDisplaySize( )));
            }
            else
            {

                if (columnBeanIn.getDataType().equals("FNUMBER"))
                {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC + DOT_UNDERSCORE,columnBeanIn.getDisplaySize( )));
                }
                else
                {
                    if (columnBeanIn.getDataType().equals("UVARCHAR2"))
                    {
                        txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, columnBeanIn.getDisplaySize( )));
                    }
                    else
                    {
                        if (columnBeanIn.getDataType().equals("MVARCHAR2"))//upcpercase with "." and NUMBER
                        {
                            txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE + DOT_UNDERSCORE + JTextFieldFilter.NUMERIC, columnBeanIn.getDisplaySize( )));
                        }
                        else
                        {
                            txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));
                        }
                    }
                }
            }
                txtCell.addFocusListener(new FocusAdapter()
                    {
                       
                        public void focusGained(FocusEvent e)
                        {                          
                        }
                       
                        public void focusLost(FocusEvent fe)
                        {
                            if (!fe.isTemporary())
                            {                               
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
            
            TableColumn tColumn = codeTableColumnModel.getColumn(selColumn) ;
            ColumnBean columnBean = (ColumnBean)tColumn.getIdentifier() ;            
            if (column == 3 ||column == 4 )
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
 
            if (value != null)
            {                
                txtCell.setText(value.toString().trim()) ;
            }
            else
            {  
                txtCell.setText(null) ;
            }
            return txtCell;
        }

    
        /* This method will Returns the value contained in the editor.
         * @return Object the value contained in the editor - newValue
         */
               
        public Object getCellEditorValue() {                              
           
            Object oldValue = tblCorrespBatch.getValueAt(selRow,selColumn);
                         
            Object newValue;
            if(selColumn == 3 || selColumn == 4 )
            {
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            else
                {
                newValue=((CoeusTextField)txtCell).getText();
            }           
            
            if (selRow > -1)
            {   
                if (codeTableModel.getValueAt(selRow,codeTableModel.getColumnCount()-1) == null)
                {
                    if (tblCorrespBatch.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblCorrespBatch.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModel.setValueAt("U", selRow, codeTableModel.getColumnCount()-1);
                            // set the user name 
                            codeTableModel.setValueAt(userId, selRow, tableStructureCorrespBatch.getUserIndex() );
                            saveRequired = true;                            
                        }
                    }
                    else
                    { 
                        codeTableModel.setValueAt("U", selRow, codeTableModel.getColumnCount()-1);
                        // set the user name 
                        codeTableModel.setValueAt(userId, selRow, tableStructureCorrespBatch.getUserIndex() );
                        saveRequired = true;                        
                    } 
                }
        
            }
 
            return newValue;
             
        }

        /**
         * This method is over loaded to handle the item state changed events.
         * @param e ItemEvent
         */
        
        public void itemStateChanged(ItemEvent e) 
        {             
             super.fireEditingStopped();
        }

  } // end class
 
//for batch detail
  private class ExtendedCellEditorBatchDetail extends DefaultCellEditor implements TableCellEditor
  {
        private CoeusTextField txtCell = new CoeusTextField();
        int selRow=0 ;
        int selColumn=0 ;
        Object oldValue = null ;
        
        public ExtendedCellEditorBatchDetail(CoeusComboBox comb)
        {
            super(comb);
            comb.addFocusListener(new FocusAdapter()
            {
                       
                public void focusGained(FocusEvent e)
                {                          
                }
                       
                public void focusLost(FocusEvent fe)
                {
                    if (!fe.isTemporary())
                    {                        
                        GetLastValue(); 
                    }// end if
                }// end focus lost
            }); // end focus listener 
        }

        public ExtendedCellEditorBatchDetail(ColumnBean columnBeanIn, int colIndex)
        {
            super( new CoeusTextField() );

            if (columnBeanIn.getDataType().equals(NUMBER))
            {
                txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC,columnBeanIn.getDisplaySize( )));
            }
            else
            {
                if (columnBeanIn.getDataType().equals("FNUMBER"))
                {
                    txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.NUMERIC + DOT_UNDERSCORE,columnBeanIn.getDisplaySize( )));
                }
                else
                {
                    if (columnBeanIn.getDataType().equals("UVARCHAR2"))
                    {
                        txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE, columnBeanIn.getDisplaySize( )));
                    }
                    else
                    {
                        if (columnBeanIn.getDataType().equals("MVARCHAR2"))//upcpercase with "." and NUMBER
                        {
                            txtCell.setDocument( new JTextFieldFilter(JTextFieldFilter.UPPERCASE + DOT_UNDERSCORE + JTextFieldFilter.NUMERIC, columnBeanIn.getDisplaySize( )));
                        }
                        else
                        {
                            txtCell.setDocument( new LimitedPlainDocument( columnBeanIn.getDisplaySize( )));
                        }
                    }
                }
            }
                txtCell.addFocusListener(new FocusAdapter()
                    {
                       
                        public void focusGained(FocusEvent e)
                        {                           
                        }
                       
                        public void focusLost(FocusEvent fe)
                        {
                            if (!fe.isTemporary())
                            {                                
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
            
            TableColumn tColumn = codeTableColumnModelBatchDetail.getColumn(selColumn) ;
            ColumnBean columnBean = (ColumnBean)tColumn.getIdentifier() ;
              
            if (column == 3 )
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
                        
            if (value != null)
            {                
                txtCell.setText(value.toString().trim()) ;
            }
            else
            {  
                txtCell.setText(null) ;
            }
          
            return txtCell;
        }

        
        /** this method will Returns the value contained in the editor.
         * @return Object the value contained in the editor - newValue
         */
               
        public Object getCellEditorValue() {
            Object oldValue = tblCorrespDetail.getValueAt(selRow,selColumn);
            Object newValue = null;
            if(selColumn == 3 )
            {
                newValue = (ComboBoxBean)((CoeusComboBox)getComponent()).getSelectedItem();
            }
            else
                {
                newValue=((CoeusTextField)txtCell).getText();
            }

            if (selRow > -1)
            {   
                if (codeTableModelBatchDetail.getValueAt(selRow,codeTableModelBatchDetail.getColumnCount()-1) == null)
                {
                    if (tblCorrespDetail.getValueAt(selRow,selColumn)!=null)
                    {
                        if (!(tblCorrespDetail.getValueAt(selRow,selColumn).toString().equals(((CoeusTextField)txtCell).getText())))
                        {
                            codeTableModelBatchDetail.setValueAt("U", selRow, codeTableModelBatchDetail.getColumnCount()-1);
                            // set the user name 
                            codeTableModelBatchDetail.setValueAt(userId, selRow, tableStructureCorrespDetail.getUserIndex() );
                            saveRequired = true;                           
                        }
                    }
                    else
                    { 
                        codeTableModelBatchDetail.setValueAt("U", selRow, codeTableModelBatchDetail.getColumnCount()-1);
                        // set the user name 
                        codeTableModelBatchDetail.setValueAt(userId, selRow, tableStructureCorrespDetail.getUserIndex() );
                        saveRequired = true;                        
                    } 
                }
        
            }
 
            return newValue;
             
        }

        /**
         * This method is over loaded to handle the item state changed events.
         * @param e ItemEvent
         */
        
        public void itemStateChanged(ItemEvent e) 
        {            
             super.fireEditingStopped();
        }

  } // end class
  //end for batch detail
  
    
    /* This method will perform the submit/ok operation over the batch correspondence table data  
     * @return Void
     */ 
    public void performOK()
    {        
        int  selectedRow = tblCorrespBatch.getSelectedRow();
        Object temp = null;
        if(selectedRow != -1){
        temp = codeTableModel.getValueAt(selectedRow,0);
        GetLastValue();        
        }
        
        //return if failed to save.
        if (!saveData()){
            return;
        }
        drawTableForBatch();
        drawTableForBatchDetail();
        vecBatchDetailAll = (Vector)codeTableModelBatchDetail.getDataVector().clone()  ;
        iniBatchDetailRow();
       
        if (selectedRow >= 0)
        {
            for (int i = 0; i < tblCorrespBatch.getRowCount(); i++)
            {
                if (codeTableModel.getValueAt(i,0).toString().equals(temp.toString()))
                {
                    tblCorrespBatch.setRowSelectionInterval(i,i);
                    break;
                }
                else
                {
                    if ( i == tblCorrespBatch.getRowCount() -1){
                        tblCorrespBatch.setRowSelectionInterval(0,0);
                    }
                }
            }
        }
    }
    
    /* This method will perform the cancle operation over the batch correspondence
     * @return Void
     */ 
    public void performCancle()
    {       
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
    
    /* This method will perform the add operation over the batch correspondence detailed table data  
     * @return Void
     */ 
    private void performAdd()
    {
        //validate the current row first
        int selectedBatchRow = tblCorrespBatch.getSelectedRow();
        if (!checkBatchRow(selectedBatchRow))return;        
        if (!checkDetailRows())return;       
        
        Object[]  oneRow = {" "};
        
        codeTableModel.insertRow(codeTableModel.getRowCount(),oneRow);
        
        // set primary key
        int maxId = getMaxNumber(codeTableModel,0 );
         codeTableModel.setValueAt(new Integer(maxId),codeTableModel.getRowCount()-1,0);
       // set the duplicate index interface aw_...
        codeTableModel.setValueAt("", codeTableModel.getRowCount()-1, tableStructureCorrespBatch.getDuplicateIndex(0) );
        
      // set AC_TYPE
        codeTableModel.setValueAt("I", codeTableModel.getRowCount()-1, codeTableModel.getColumnCount()-1);
        saveRequired = true;
        // set the user name 
        codeTableModel.setValueAt(userId, codeTableModel.getRowCount()-1, tableStructureCorrespBatch.getUserIndex() );
       tblCorrespBatch.changeSelection(codeTableModel.getRowCount()-1,  1, false, false) ;
       tblCorrespBatch.editCellAt(codeTableModel.getRowCount()-1,  0) ;
       tblCorrespBatch.requestFocus();
       
    }
    
    /* This method will perform the delete operation over the batch correspondence detail table data  
     * @return Void
     */ 
    private void performDelete()
    {
        int rowNum = tblCorrespBatch.getSelectedRow();
        if (rowNum == -1){
            return;
        }
        
        if ( tblCorrespBatch.getEditingColumn() >= 0 )
        {
           tblCorrespBatch.getCellEditor().stopCellEditing(); 
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
        if (confirm == 1){
            return;
        }         
        if (codeTableModel.getValueAt(rowNum,codeTableModel.getColumnCount()-1) != "I")
        {   
            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModel.setValueAt("D", rowNum, codeTableModel.getColumnCount()-1);
            saveRequired = true;
            vecDeletedRows.add(getTableRow(codeTableModel,codeTableColumnModel,rowNum)) ;
        }//end if
        
        codeTableModel.removeRow(rowNum);
        //also need to remove rows form detail table
        for(int i = codeTableModelBatchDetail.getRowCount() - 1; i >= 0; i--)
        {
            if (codeTableModelBatchDetail.getValueAt(i,codeTableModelBatchDetail.getColumnCount()-1) != "I")
            {   
                //if not new inserted row, come to here and set AC_tYPE to "D"
                codeTableModelBatchDetail.setValueAt("D", i, codeTableModelBatchDetail.getColumnCount()-1);
            }
            vecDeletedRowsBatchDetail.add(getTableRow(codeTableModelBatchDetail,codeTableColumnModelBatchDetail,i)) ;
            codeTableModelBatchDetail.removeRow(i);
        } 
          
        if ( rowNum != 0 )
        {
            rowNum--; 
        }
        else
        {
            tblCorrespBatch.changeSelection(codeTableModel.getRowCount()-1, 1, false, false) ;           
        }
         
        if (tblCorrespBatch.getRowCount() > 0  )
        {
                 selectedBatchRow = rowNum; //here sets selectedBatchRow to avoid validation in valuechange event.
            tblCorrespBatch.changeSelection(rowNum,  1, false, false) ; 
        }
   
    }
    
    /* This method will perform the add operation over the batch correspondence table data  
     * @return Void
     */ 
    private void performAddDetail()
    {
        //need add Batch row first
        if ( tblCorrespBatch.getRowCount() == 0 ) {
            return;
        }
        
        Object[]  oneRow = {" "};
        
        codeTableModelBatchDetail.insertRow(codeTableModelBatchDetail.getRowCount(),oneRow);
       
        // set primary key
        codeTableModelBatchDetail.setValueAt(codeTableModel.getValueAt(tblCorrespBatch.getSelectedRow(),0),codeTableModelBatchDetail.getRowCount()-1,0);
       //set correspondence_number
        int maxId = getMaxNumber(codeTableModelBatchDetail, 1);
        codeTableModelBatchDetail.setValueAt(new Integer(maxId),codeTableModelBatchDetail.getRowCount()-1,1);
       // set the duplicate index interface aw_...
        codeTableModelBatchDetail.setValueAt("", codeTableModelBatchDetail.getRowCount()-1, tableStructureCorrespDetail.getDuplicateIndex(0) );

         codeTableModelBatchDetail.setValueAt(null, codeTableModelBatchDetail.getRowCount()-1, 2);
        codeTableModelBatchDetail.setValueAt(null, codeTableModelBatchDetail.getRowCount()-1, 3 );
      // set AC_TYPE
        codeTableModelBatchDetail.setValueAt("I", codeTableModelBatchDetail.getRowCount()-1, codeTableModelBatchDetail.getColumnCount()-1);
        saveRequired = true;
        // set the user name 
       codeTableModelBatchDetail.setValueAt(userId, codeTableModelBatchDetail.getRowCount()-1, tableStructureCorrespDetail.getUserIndex() );
       tblCorrespDetail.changeSelection(codeTableModelBatchDetail.getRowCount()-1,  1, false, false) ;
       tblCorrespDetail.editCellAt(codeTableModelBatchDetail.getRowCount()-1,  2) ;
       tblCorrespDetail.requestFocus();
    }
    
     /* This method will perform the delete operation over the batch correspondence table data  
     * @return Void
     */  
    private void performDeleteDetail()
    {
        int rowNum = tblCorrespDetail.getSelectedRow();
        if (rowNum == -1){
            return;
        }
        
        if ( tblCorrespDetail.getEditingColumn() >= 0 )
        {
           tblCorrespDetail.getCellEditor().stopCellEditing(); 
        }        

        String msg = coeusMessageResources.parseMessageKey(
                            "generalDelConfirm_exceptionCode.2100");
        int confirm = CoeusOptionPane.showQuestionDialog(
                                            msg,
                                            CoeusOptionPane.OPTION_YES_NO,
                                            CoeusOptionPane.DEFAULT_YES);
        if (confirm == 1){
            return;
        }         
        //keep all deleted rows into vecDeletedRows.
        if (codeTableModelBatchDetail.getValueAt(rowNum,codeTableModelBatchDetail.getColumnCount()-1) != "I")
        {   
            //if not new inserted row, come to here and set AC_tYPE to "D"
            codeTableModelBatchDetail.setValueAt("D", rowNum, codeTableModelBatchDetail.getColumnCount()-1);
            saveRequired = true;
            vecDeletedRowsBatchDetail.add(getTableRow(codeTableModelBatchDetail, codeTableColumnModelBatchDetail,rowNum)) ;
        }//end if
        
        codeTableModelBatchDetail.removeRow(rowNum);
                
        if ( rowNum != 0 )
        {
            rowNum--; 
        }
        else
        {
          tblCorrespDetail.changeSelection(codeTableModelBatchDetail.getRowCount()-1, 1, false, false) ; 
        }
         
        if (tblCorrespDetail.getRowCount() > 0  )
        {
            tblCorrespDetail.changeSelection(rowNum,  1, false, false) ; 
        }
    }
    
/* This method will stop all the table cell editing  
 * @return void
 */  
    private void stopAllEditing( ) 
    {
        tblCorrespBatch.getCellEditor().cancelCellEditing();
        tblCorrespDetail.getCellEditor().cancelCellEditing();
               
    }
    
/* This method is used to set the action perform event on buttons
 * @param ActionEvent actionEvent  
 * @return void
 */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) 
    {
        Object actionSource = actionEvent.getSource();
        if( actionSource.equals( btnOk ) ) {
            performOK();
        }else if( actionSource.equals( btnCancle ) ) {
            performCancle();
        }else if( actionSource.equals( btnAdd ) ) {
            performAdd();
        }else if( actionSource.equals( btnDelete ) ) {
            performDelete();
        }else if( actionSource.equals( btnAddDetail ) ) {
            performAddDetail();
        }else if( actionSource.equals( btnDeleteDetail ) ) {
            performDeleteDetail();
        }
    }    
    
/* This method will get the last modified table row value  
 * @return void
 */  
 private void GetLastValue()
 {
    int row = tblCorrespBatch.getEditingRow();
    int col = tblCorrespBatch.getEditingColumn();
    if (row != -1 && col != -1) 
    {
        tblCorrespBatch.getCellEditor(row, col).stopCellEditing();
        TableColumn column = codeTableColumnModel.getColumn(col) ;
        //make sure to set AC_TYPE
        column.getCellEditor().getCellEditorValue();
    }
    
    int rowDetail = tblCorrespDetail.getEditingRow();
    int colDetail = tblCorrespDetail.getEditingColumn();
    if (rowDetail != -1 && colDetail != -1) 
    {
        tblCorrespDetail.getCellEditor(rowDetail, colDetail).stopCellEditing();
        TableColumn column = codeTableColumnModelBatchDetail.getColumn(colDetail) ;
        //make sure to set AC_TYPE
        column.getCellEditor().getCellEditorValue();
    }
 }
 
/* This method will initialize the batch row details
 * @return void
 */
public void iniBatchDetailRow()
{
    int selBatchRow = tblCorrespBatch.getSelectedRow();

    if (selBatchRow >= 0)
    {
        selectedBatchRow = selBatchRow;
        Object  batchTypeCode = codeTableModel.getValueAt(selBatchRow, 0);
                    
        if (codeTableModelBatchDetail.getRowCount() > 0 )
        {
            //ini the detail rows in the table
            int j = 0;
            for(int i = codeTableModelBatchDetail.getRowCount() - 1; i >= 0; i--)
            {
                if (!codeTableModelBatchDetail.getValueAt(i, 0).equals(batchTypeCode))
                {
                    codeTableModelBatchDetail.removeRow(i);
                }
            }
            
            for(int i = vecBatchDetailAll.size() - 1 ; i >= 0; i -- )
            {
                if (((Vector)vecBatchDetailAll.elementAt(i)).elementAt(0).equals(batchTypeCode))
                {
                    vecBatchDetailAll.remove(i);
                }
            }
          
                        
        }
    }
      
}
  
 /* This method will copy one table row to a hashmap
 * @param ExtendedDefaultTableModel codeTM
 * @param DefaultTableColumnModel codeTCM
 * @param int rowNum    
 * @return HashMap hashRow
 */  
   private HashMap getTableRow(ExtendedDefaultTableModel codeTM, DefaultTableColumnModel codeTableColModel,int rowNum)
   { 
        HashMap hashRow = new HashMap() ;
        int colTotal = codeTM.getColumnCount()-1;
        for (int colCount=0; colCount <= codeTM.getColumnCount()-1; colCount++)
        { // for each column u will build a hashmap 
  
            TableColumn column = codeTableColModel.getColumn(colCount) ;
            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
            Object val = null ;
            // the whole idea of setting up the columnBean as the identifier is that u get to know
            // all abt the structure details of the column, so u can create appropriate type of object
            // and stick it in to the hashMap
            if (columnBean.getDataType().equals(NUMBER))
            {
                if (codeTM.getValueAt(rowNum,colCount) == null
                        ||(codeTM.getValueAt(rowNum,colCount).equals("")))
                {  
                    val = null ; //new Integer(0) 
                }
                else
                {
                    val = codeTM.getValueAt(rowNum,colCount);
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
                if (codeTM.getValueAt(rowNum,colCount)==null)
                {
                    val = "";
                }
                else
                {
                    val = codeTM.getValueAt(rowNum,colCount).toString() ;
                }
            }
                
            if (columnBean.getDataType().equals("DATE"))
            {                 
                if (!(codeTM.getValueAt(rowNum, codeTM.getColumnCount()-1) == null) && codeTM.getValueAt(rowNum, codeTM.getColumnCount()-1).equals("I")) // if itz an insert
                {   
                    val = CoeusUtils.getDBTimeStamp();                   
                }
                else
                {  
                    if (columnBean.getQualifier().equals("VALUE"))
                    {   
                       val = CoeusUtils.getDBTimeStamp();                        
                    }
                    else
                    {  //AW_...
                        val =  java.sql.Timestamp.valueOf(codeTM.getValueAt(rowNum,colCount).toString()) ;
                    }
                }
            }
                
            hashRow.put(columnBean.getColIdentifier(), val) ;
        } //end for
        return hashRow;
   }

 /* This method will save the code table batch correspondence table data  
  * @return Boolean value
  */  
    private boolean saveData()
    {
         // do the validation b4 u go and build the vector of modified rows
          if (tableDataValid())
          {//save batch table
                // get the modified rows for batch table
                Vector vecModifiedRows = getModifiedRows(tblCorrespBatch,vecDeletedRows,codeTableModel,codeTableColumnModel) ;
                
                HashMap hashStoredProcedure = (HashMap)tableStructureCorrespBatch.getHashStoredProceduresForThisTable();
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
                  {//batch Data Saved Successfully
                     //save detail data:  
                      
                    //there are may be some modified rows in vecBatchDetailAll which are filtered out on screen
                    int sizeDetailSaveInVec = vecBatchDetailAll.size();
                    for (int i = 0 ; i < sizeDetailSaveInVec; i++ )
                    {
                        codeTableModelBatchDetail.addRow((Vector)vecBatchDetailAll.elementAt(i));
                    }
                                     
                    // get the modified rows for detail table
                    Vector vecModifiedDetailsRows = getModifiedRows(tblCorrespDetail,vecDeletedRowsBatchDetail,codeTableModelBatchDetail,codeTableColumnModelBatchDetail) ;
                    
                    int selectedRow = tblCorrespBatch.getSelectedRow();
                    Object  batchTypeCode = null;
                    if(selectedRow != -1){
                    batchTypeCode = codeTableModel.getValueAt(selectedRow, 0);
                    }
                    //then filter out detail table 
                    for(int i = codeTableModelBatchDetail.getRowCount() - 1; i >= 0; i--)
                    {
                        if ( !(codeTableModelBatchDetail.getValueAt(i,0).equals(batchTypeCode)))
                        {
                            codeTableModelBatchDetail.removeRow(i);
                        }
                    }                                      

                    HashMap hashDetailStoredProcedure = (HashMap)tableStructureCorrespDetail.getHashStoredProceduresForThisTable();
                    if(hashDetailStoredProcedure == null)
                        System.out.println("hashDetailStoredProcedure == null");
                      //Get the update stored procedure associated witht this table.
                    StoredProcedureBean updateDetailStoredProcedure =
                        (StoredProcedureBean)hashDetailStoredProcedure.get(new Integer(1));

                    RequestTxnBean requestDetailTxnBean = new RequestTxnBean();
                    requestDetailTxnBean.setAction("MODIFY_DATA");
                    requestDetailTxnBean.setStoredProcedureBean(updateDetailStoredProcedure);
                    requestDetailTxnBean.setRowsToModify(vecModifiedDetailsRows) ;

                    // the servlet will return if the saving process was successful or not
                    Boolean successDetail = (Boolean)getDataFromServlet(requestDetailTxnBean) ;
                    if (successDetail == null) // Error while saving data
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            "saveFail_exceptionCode.1102");

                        CoeusOptionPane.showInfoDialog(msg);
                        return false;
                    }
                    else
                    {
                      
                      saveRequired = false;
                      return true;
                    }

                  }// end if data validation
          }
          else
          {
            return false;
          }
    }
  
  /* This method will validate a batch row
   * @return Boolean value
   */   
   private boolean checkBatchRow(int rowSelect)
   {
        if (tblCorrespBatch.getRowCount() == 0){
            return true;
        }
       
        int colTotal = tblCorrespBatch.getColumnCount();
        for (int colIndex = 0; colIndex < colTotal; colIndex++)
        {
            TableColumn column = codeTableColumnModel.getColumn(colIndex) ;

            ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

            if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
            {
                if ( tblCorrespBatch.getValueAt(rowSelect,colIndex) != null)
                {
                    if (tblCorrespBatch.getValueAt(rowSelect,colIndex).equals("")
                            || tblCorrespBatch.getValueAt(rowSelect,colIndex).toString().trim().equals("") )
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            EXCEPTION_DESCRIPTION);
                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblCorrespBatch.changeSelection(rowSelect,  colIndex, false, false) ;
                        return false;
                    }
                }
                else
                {
                        String msg = coeusMessageResources.parseMessageKey(
                            EXCEPTION_DESCRIPTION);

                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblCorrespBatch.changeSelection(rowSelect,  colIndex, false, false) ;
                        return false;
                }
            }
        
        }
        return true ;
   }
   
    
 /* This method will validate all detail rows with the selected batch row. 
  * @return Boolean value
  */  
   private boolean checkDetailRows()
   {
        int rowTotal = tblCorrespDetail.getRowCount();
        int colTotal = tblCorrespDetail.getColumnCount();
        for (int rowIndex = 0; rowIndex < rowTotal ; rowIndex++)
        {
            for (int colIndex = 0; colIndex < colTotal; colIndex++)
            {
                TableColumn column = codeTableColumnModelBatchDetail.getColumn(colIndex) ;

                ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;

                if (columnBean.getColumnEditable()
                    && !columnBean.getColumnCanBeNull())
                {
                     if ( tblCorrespDetail.getValueAt(rowIndex,colIndex) != null)
                    {
                        if (tblCorrespDetail.getValueAt(rowIndex,colIndex).equals("")
                            || tblCorrespDetail.getValueAt(rowIndex,colIndex).toString().trim().equals("") )
                        {
                            String msg = coeusMessageResources.parseMessageKey(
                            EXCEPTION_DESCRIPTION);
                            String msgColName = " " + columnBean.getDisplayName() + ". ";
                            CoeusOptionPane.showInfoDialog(msg + msgColName);
                            tblCorrespDetail.changeSelection(rowIndex,  colIndex, false, false) ;
                            return false;
                        }
                    }
                    else
                    {
                        String msg = coeusMessageResources.parseMessageKey(
                            EXCEPTION_DESCRIPTION);

                        String msgColName = " " + columnBean.getDisplayName() + ". ";
                        CoeusOptionPane.showInfoDialog(msg + msgColName);
                        tblCorrespDetail.changeSelection(rowIndex,  colIndex, false, false) ;
                        return false;
                    }
                }
            }

        }
        return true ;
   }
  
  
 /* This method will do the validation of rows
  * @return Vector vecUpdatedRows
  */ 
   private boolean tableDataValid()
   {
       if ( tblCorrespBatch.getEditingColumn() >= 0 )
       {
            tblCorrespBatch.getCellEditor().stopCellEditing(); 
       }
       if ( tblCorrespDetail.getEditingColumn() >= 0 )
       {
            tblCorrespDetail.getCellEditor().stopCellEditing(); 
       }
       if (!checkBatchRow(tblCorrespBatch.getSelectedRow())){
           return false;
       }
       if (!checkDetailRows()) {
           return false;
       }
       return true;
   }

 /* This method will return all the modify row and newly added row in the table
 * @param javax.swing.JTable tblCorresp    
 * @param Vector vecDeleted 
 * @paramExtendedDefaultTableModel codeTM\
 * @param DefaultTableColumnModel codeTCM
 * @return Vector vecUpdatedRows
 */  
   private Vector getModifiedRows(javax.swing.JTable tblCorresp,Vector vecDeleted, ExtendedDefaultTableModel codeTM, DefaultTableColumnModel codeTCM)
   {
      Vector vecUpdatedRows = new Vector();
      int rowCount = 0;            
      
      if (vecDeleted.size() > 0 )
      {//append deleted rows to vecUpdatedRows
          for (int row = 0; row < vecDeleted.size(); row++)
          {
              vecUpdatedRows.add(vecDeleted.get(row));
          }          
      }            

      while(rowCount < tblCorresp.getRowCount())
      {// check AC_TYPE field
        String tmpACType ;
        if (codeTM.getValueAt(rowCount, codeTM.getColumnCount()-1) != null)
        {
            vecUpdatedRows.add(getTableRow(codeTM,codeTCM,rowCount)) ;
        }
        rowCount++ ;
      }//end while

      return vecUpdatedRows ;
  }

/* This method will check the dependency when user is trying to deleta the record
 * @param int rowNumber    
 * @param String form 
 * @return boolean value
 */    
private boolean checkDependency(int rowNumber, String from)
{
    HashMap hmDependencyCheckValues = new HashMap();
    int colNumber = tableStructureCorrespBatch.getPrimaryKeyIndex(0);
    TableColumn column = codeTableColumnModel.getColumn(colNumber) ;
    ColumnBean columnBean = (ColumnBean)column.getIdentifier() ;
    HashMap hmDependencyTables = (HashMap)tableStructureCorrespBatch.getHashTableDependency();
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
    
        if (columnBean.getDataType().equals(NUMBER))
        {
            hmDependencyCheckValues.put("a_column_value",new Integer(tblCorrespBatch.getValueAt(rowNumber, colNumber).toString()));
            requestTxnBean.setAction("DEPENDENCY_CHECK_INT");
         }
        else
        {
            hmDependencyCheckValues.put("a_column_value",tblCorrespBatch.getValueAt(rowNumber, colNumber).toString());
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
 
/* This method will return the maxId of code table batch correspondence
 * @param ExtendedDefaultTableModel codeTM    
 * @param int colNum 
 * @return int maxCodeId      
 */  
private int getMaxNumber(ExtendedDefaultTableModel codeTM, int colNum)
{
    int maxCodeId = 0;
    if(codeTM.getRowCount() > 1)
    {  
        //Get the max number.
        for(int rowIndex = 0; rowIndex < codeTM.getRowCount()-1; rowIndex++)
        {
            String codeIdStr = codeTM.getValueAt(rowIndex, colNum).toString();
            Integer codeId = new Integer(codeIdStr);
            int tempCodeId = codeId.intValue();
            if(tempCodeId > maxCodeId)
            {
                maxCodeId = tempCodeId;
            }
        }
    }
    maxCodeId++;
    return maxCodeId;
}


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new javax.swing.JButton();
        btnCancle = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        scrPnBatch = new javax.swing.JScrollPane();
        tblCorrespBatch = new javax.swing.JTable();
        scrPnDetail = new javax.swing.JScrollPane();
        tblCorrespDetail = new javax.swing.JTable();
        btnAddDetail = new javax.swing.JButton();
        btnDeleteDetail = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('S');
        btnOk.setText("Save");
        btnOk.setMaximumSize(new java.awt.Dimension(107, 24));
        btnOk.setMinimumSize(new java.awt.Dimension(107, 24));
        btnOk.setPreferredSize(new java.awt.Dimension(107, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        getContentPane().add(btnOk, gridBagConstraints);

        btnCancle.setFont(CoeusFontFactory.getLabelFont());
        btnCancle.setMnemonic('C');
        btnCancle.setText("Close");
        btnCancle.setMaximumSize(new java.awt.Dimension(107, 24));
        btnCancle.setMinimumSize(new java.awt.Dimension(107, 24));
        btnCancle.setPreferredSize(new java.awt.Dimension(107, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnCancle, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(107, 24));
        btnAdd.setMinimumSize(new java.awt.Dimension(107, 24));
        btnAdd.setPreferredSize(new java.awt.Dimension(107, 24));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnAdd, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(107, 24));
        btnDelete.setMinimumSize(new java.awt.Dimension(107, 24));
        btnDelete.setPreferredSize(new java.awt.Dimension(107, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnDelete, gridBagConstraints);

        scrPnBatch.setBorder(null);
        scrPnBatch.setMaximumSize(new java.awt.Dimension(200, 100));
        scrPnBatch.setMinimumSize(new java.awt.Dimension(200, 100));
        scrPnBatch.setPreferredSize(new java.awt.Dimension(200, 100));
        tblCorrespBatch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnBatch.setViewportView(tblCorrespBatch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 5);
        getContentPane().add(scrPnBatch, gridBagConstraints);

        scrPnDetail.setBorder(null);
        scrPnDetail.setMaximumSize(new java.awt.Dimension(200, 100));
        scrPnDetail.setMinimumSize(new java.awt.Dimension(200, 100));
        scrPnDetail.setPreferredSize(new java.awt.Dimension(200, 100));
        tblCorrespDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnDetail.setViewportView(tblCorrespDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 3, 0, 5);
        getContentPane().add(scrPnDetail, gridBagConstraints);

        btnAddDetail.setFont(CoeusFontFactory.getLabelFont());
        btnAddDetail.setMnemonic('t');
        btnAddDetail.setText("Add Detail");
        btnAddDetail.setMaximumSize(new java.awt.Dimension(107, 24));
        btnAddDetail.setMinimumSize(new java.awt.Dimension(107, 24));
        btnAddDetail.setPreferredSize(new java.awt.Dimension(107, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        getContentPane().add(btnAddDetail, gridBagConstraints);

        btnDeleteDetail.setFont(CoeusFontFactory.getLabelFont());
        btnDeleteDetail.setMnemonic('l');
        btnDeleteDetail.setText("Delete Detail");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(btnDeleteDetail, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddDetail;
    private javax.swing.JButton btnCancle;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteDetail;
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane scrPnBatch;
    private javax.swing.JScrollPane scrPnDetail;
    private javax.swing.JTable tblCorrespBatch;
    private javax.swing.JTable tblCorrespDetail;
    // End of variables declaration//GEN-END:variables
    
}
