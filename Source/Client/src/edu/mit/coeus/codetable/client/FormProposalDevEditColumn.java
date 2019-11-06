/*
 * FormProposalDevEditColumn.java
 *
 * Created on March 24, 2003, 12:48 PM
 */

package edu.mit.coeus.codetable.client;

//import edu.mit.coeus.gui.* ;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.sponsormaint.gui.* ;
//import edu.mit.coeus.sponsormaint.bean.* ;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.Date;
import java.beans.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAboutForm ;
import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.search.gui.* ;


/**
 *
 * @author  prahalad
 */
public class FormProposalDevEditColumn extends JComponent //CoeusDlgWindow
{
//    private javax.swing.JButton btnAdd;
    private javax.swing.JTable tblColumnNames;
//    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlForState;
   
//    private javax.swing.JButton btnSave;

    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    /* This is used to know whether data is modified or add  */
//    private boolean saveRequired = false;  
    
//    private TableSorter sorter ;
    private TableSorterCodeTable sorter;
    
        
    DefaultTableModel codeTableModel ; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tableStructureBeanEDI ;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames =  {"COLUMN_NAME", "DATA_TYPE", "DATA_LENGTH", "NULLABLE" } ;  
    int numColumnsDisplay = 0 ;
    String countryCode = new String() ; 
    private HashMap hashSelectedRow = null ;
    private Vector vecFromParent = null ;  
    
    private Frame parent;
   
    private CoeusDlgWindow thisWindow ;
    // Added for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - Start
    private static final String GET_PROP_DEV_EDITABLE_COLUMNS = "GET_PROP_DEV_EDITABLE_COLUMNS";
    // Added for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - End
        
    /** Creates a new instance of FormProposalDevEditColumn */
    public FormProposalDevEditColumn(Frame parent, String title, boolean modal, AllCodeTablesBean allCodeTablesBean)
    {
        //super(parent, title, modal);
        this.parent = parent ;
        this.allCodeTablesBean = allCodeTablesBean ;
        initComponents();
    }
    
    public HashMap getSelectedRow()
    {
        return this.hashSelectedRow  ;
    }
    
    public void setSelectedRow(HashMap hashSelectedRow)
    {
        this.hashSelectedRow = hashSelectedRow ;
    }
    
   public Vector getItemsToRemove()
   {
    return vecFromParent ;
   }
    
   public void setItemsToRemove(Vector vecChild)
   {
        this.vecFromParent = vecChild ;
   }
    
  
     public JPanel initComponents() 
//    public void initComponents() 
     {
       coeusMessageResources = CoeusMessageResources.getInstance();
        System.out.println("  *** in prop dev edit column form init" ) ; 
        pnlForState = new JPanel(new BorderLayout(0,0)) ; 
        sorter = new TableSorterCodeTable();
        tblColumnNames = new javax.swing.JTable(sorter);
        tblColumnNames.setRowHeight(22) ;
        sorter.addMouseListenerToHeaderInTable(tblColumnNames); 
        tblColumnNames.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        tblColumnNames.getTableHeader().setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblColumnNames.getTableHeader(), BorderLayout.NORTH) ; 
        pnlTable.add(tblColumnNames , BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
                
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(100 , 100)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(100, 100)) ;
//        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
//        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
       java.awt.GridBagConstraints gridBagConstraints;
       pnlForBtn = new JPanel(new FlowLayout(javax.swing.SwingConstants.BOTTOM, 10, 10)) ;
       
       btnOk = new JButton("OK") ;
       btnOk.setFont(CoeusFontFactory.getLabelFont());
       btnOk.setMnemonic('O');
       pnlForBtn.add(btnOk) ;
       
       btnOk.addActionListener(new ActionListener()
       {
        public void actionPerformed(ActionEvent ae)
        {
            if (tblColumnNames.getRowCount()>0 )
            {
                if (tblColumnNames.getSelectedRow() != -1)
                {
                    hashSelectedRow = new HashMap() ;
                    hashSelectedRow.put("COLUMN_NAME", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 0)) ;
                    hashSelectedRow.put("DATA_TYPE", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 1)) ;
                    hashSelectedRow.put("DATA_LENGTH", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 2)) ;
                    hashSelectedRow.put("NULLABLE", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 3)) ;
                }    
            }    
            
            thisWindow.dispose() ;
        }
       }) ;
       
       //getContentPane().setLayout(new BorderLayout(0,0));
        
       pnlForState.add(scrlpnlForTableCtrl, BorderLayout.CENTER) ;
       pnlForState.add(pnlForBtn, BorderLayout.EAST) ;
       
       //getContentPane().add(pnlForState, BorderLayout.CENTER);
       //pack() ;                
        pnlForState.setMinimumSize(new Dimension(200, 200));
        pnlForState.setPreferredSize(new Dimension(200, 200));       
            
       return pnlForState ;
     }
    
    public void showForm()
    {
        thisWindow = new CoeusDlgWindow(parent, "ColumnNames", true){
            protected JRootPane createRootPane() {
                ActionListener actionListener = new ActionListener() {
                    public void actionPerformed(ActionEvent actionEvent) {
                        try{
                            //validateWindow();
                        }catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                };
                JRootPane rootPane = new JRootPane();
                KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
                0);
                rootPane.registerKeyboardAction(actionListener, stroke,
                JComponent.WHEN_IN_FOCUSED_WINDOW);
                return rootPane;
            }
        };
                
       thisWindow.getContentPane().add(pnlForState);
       thisWindow.setSize(400,400);   
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       thisWindow.setLocation(screenSize.width/5 , screenSize.height/5 );
        
        initialiseData() ;
        thisWindow.show() ;
    }
     
     
    public void initialiseData() 
    {
//        codeTableModel = new DefaultTableModel() ;
        codeTableModel = new DefaultTableModel()
        {
            public boolean isCellEditable(int row,int col)
            {
                return false;
            }
        };
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ; 
         
       // tblCodeTable.addFocusListener(this) ;        
        tblColumnNames.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
      
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        numColumnsDisplay = 4 ;
        // populate the column header name on the the screen
        if (numColumnsDisplay > 0)
        {
            for (int i = 0; i < numColumnsDisplay; i++)
            {   
              TableColumn newColumn = new TableColumn(i) ; // set the model index and width
              newColumn.setHeaderValue(columnNames[i]) ;
              newColumn.setCellEditor(new DefaultCellEditor(new JTextField())) ;
              
              // add the new columnto the column model also
              codeTableColumnModel.addColumn(newColumn) ;
              
              displayCodeTableColumnModel.addColumn(newColumn) ;
              
              // add it to the tablemodel
              codeTableModel.addColumn(newColumn);
             }
        
            tblColumnNames.setColumnModel(displayCodeTableColumnModel); // also give the structure
                        
            // using the table sturcture bean u can find the stored proc for select or insert or update
            // so pass that tableStructureBeanEDI and get the DataBean from the servlet.
            getDataForTheTable();
            
            // populate the data on the the screen
            vdata = accDataBean.getVectData();
            HashMap htRow = new HashMap();
           
            
            for (int i=0; i < vdata.size(); i++) //loop for num of rows
            {
                htRow = (HashMap)vdata.elementAt(i);
                Object [] rowColumnDatas = new Object[4];
                  
                for (int j = 0; j < numColumnsDisplay; j++) // loop for num of columns will display on screen
                {
                    rowColumnDatas[j] = (Object)htRow.get(columnNames[j]);
                    Object obj = rowColumnDatas[j];
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
                  }//end for
                
                // make sure u dont add a row which is already present in the parent form
                    if (!duplicateItem(rowColumnDatas)) // add non duplicate rows
                    {    
                        codeTableModel.addRow(rowColumnDatas);
                    }    
            }            
             
       } 
       
       sorter.setModel(codeTableModel) ;
       tblColumnNames.setModel(sorter) ;
            
       if (vdata.size()>0)
       {    
            // hide unwanted columns , in this case except for first column hide the rest
            int availableNumOfCols = 4 ;
            
            for (int colCount=availableNumOfCols-1 ; colCount > 0 ; colCount--)    
            {  
                    TableColumnModel columnModel = tblColumnNames.getColumnModel();
                    TableColumn inColumn = columnModel.getColumn(colCount );
                    tblColumnNames.removeColumn(inColumn ); 
             }
            tblColumnNames.setRowSelectionInterval(0,0) ;
            tblColumnNames.repaint() ;
       }   
               
    }
    
    private boolean duplicateItem(Object [] rowColumnData)
    {
        boolean duplicate = false ;
        if (vecFromParent == null) 
        {
            return duplicate;
        }
         if (vecFromParent.size()<=0)
         {
             return duplicate ;
         }
        
        for (int loopCount=0; loopCount< vecFromParent.size(); loopCount++)
        {
            if (rowColumnData[0].toString().equals(vecFromParent.get(loopCount).toString()))
            {
                duplicate = true ;
            }    
        }    
        
        return duplicate ;
    }
     
     
     private void getDataForTheTable()
     {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
      Vector vectData = null;
      try
      {
          // Modified for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - Start
//          RequestTxnBean requestTxnBean = new RequestTxnBean();
//          requestTxnBean.setAction("GET_COLUMNS_FOR_TABLE");
//          requestTxnBean.setTableName("OSP$EPS_PROPOSAL");
//   
//          RequesterBean requester = new RequesterBean();
//          requester.setDataObject(requestGET_COLUMNS_FOR_TABLETxnBean) ;
          
          RequestTxnBean requestTxnBean = new RequestTxnBean();
          requestTxnBean.setAction(GET_PROP_DEV_EDITABLE_COLUMNS);
          RequesterBean requester = new RequesterBean();
          requester.setDataObject(requestTxnBean) ;
          // Modified for COEUSQA-2570 : OSP Administrators should have the rights to override cost sharing submission field value before submitting the proposal to the sponsor - End
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
  
     
     
     
}
