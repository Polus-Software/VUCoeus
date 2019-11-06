/*
 * FormEditModules.java
 *
 * Created on May 22, 2007, 4:43 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.codetable.bean.AllCodeTablesBean;
import edu.mit.coeus.codetable.bean.DataBean;
import edu.mit.coeus.codetable.bean.RequestTxnBean;
import edu.mit.coeus.codetable.bean.TableStructureBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TableSorterCodeTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author noorula
 */
public class FormEditModules extends JComponent{
    
    private javax.swing.JTable tblColumnNames;
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane scrlpnlForTableCtrl;
    private javax.swing.JPanel pnlForBtn;
    private javax.swing.JPanel pnlForState;
    
//    private TableSorter sorter ;
    private TableSorterCodeTable sorter ;
    
    
    DefaultTableModel codeTableModel ; // this model will hold the actual data
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    AllCodeTablesBean allCodeTablesBean ;
    TableStructureBean tableStructureBeanEDI ;
    HashMap hashAllCodeTableStructure = null ;
    DataBean accDataBean = new DataBean();
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    String[] columnNames =  {"DESCRIPTION" } ;
    int numColumnsDisplay = 0 ;
    String countryCode = new String() ;
    private HashMap hashSelectedRow = null ;
    private Vector vecFromParent = null ;
    
    private Frame parent;
    int moduleCode=0;
    private CoeusDlgWindow thisWindow ;
    /** Creates a new instance of FormEditModules */
    public FormEditModules(Frame parent, AllCodeTablesBean allCodeTablesBean,int moduleCode) {
        this.parent = parent ;
        this.allCodeTablesBean = allCodeTablesBean ;
        this.moduleCode=moduleCode;
        initComponents();
        
    }
    
    public HashMap getSelectedRow() {
        return this.hashSelectedRow  ;
    }
    
    public void setSelectedRow(HashMap hashSelectedRow) {
        this.hashSelectedRow = hashSelectedRow ;
    }
    
    public Vector getItemsToRemove() {
        return vecFromParent ;
    }
    
    public void setItemsToRemove(Vector vecChild) {
        this.vecFromParent = vecChild ;
    }
    
    
    public JPanel initComponents()
//    public void initComponents()
    {
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
        pnlForBtn = new JPanel(new FlowLayout(javax.swing.SwingConstants.BOTTOM, 10, 10)) ;
        
        btnOk = new JButton("OK") ;
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        pnlForBtn.add(btnOk) ;
        
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (tblColumnNames.getRowCount()>0 ) {
                    if (tblColumnNames.getSelectedRow() != -1) {
                        hashSelectedRow = new HashMap() ;
                        String moduleName = (String) codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 0);
                        Vector vecData = accDataBean.getVectData();
                        if(vecData!=null && vecData.size()>0){
                            for(int index=0 ; index<vecData.size() ; index++){
                                HashMap hmModuleData = (HashMap) vecData.get(index);
                                if(hmModuleData!=null && hmModuleData.get("DESCRIPTION").equals(moduleName)){
                                    hashSelectedRow.put("PROTOCOL MODULE CODE", hmModuleData.get("PROTOCOL_MODULE_CODE")) ;
                                    hashSelectedRow.put("DESCRIPTION", moduleName) ;
                                    break;
                                }
                            }
                        }
                    }
                }
                
                thisWindow.dispose() ;
            }
        }) ;
        pnlForState.add(scrlpnlForTableCtrl, BorderLayout.CENTER) ;
        pnlForState.add(pnlForBtn, BorderLayout.EAST) ;
        pnlForState.setMinimumSize(new Dimension(200, 200));
        pnlForState.setPreferredSize(new Dimension(200, 200));
        
        return pnlForState ;
    }
    
    public void showForm() {
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
    
    
    public void initialiseData() {
        codeTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row,int col) {
                return false;
            }
        };
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        tblColumnNames.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        numColumnsDisplay = 1 ;
        // populate the column header name on the the screen
        if (numColumnsDisplay > 0) {
            for (int i = 0; i < numColumnsDisplay; i++) {
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
                    if (rowColumnDatas[j] !=null) {
                        if (rowColumnDatas[j].equals("null")) {
                            rowColumnDatas[j] = "";
                        }
                    } else {
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
        
        sorter.setModel(codeTableModel, false) ;
        tblColumnNames.setModel(sorter) ;
        
        if (vdata.size()>0) {
            // hide unwanted columns , in this case except for first column hide the rest
            int availableNumOfCols = 1 ;
            
            for (int colCount=availableNumOfCols-1 ; colCount > 0 ; colCount--) {
                TableColumnModel columnModel = tblColumnNames.getColumnModel();
                TableColumn inColumn = columnModel.getColumn(colCount );
                tblColumnNames.removeColumn(inColumn );
            }
            tblColumnNames.setRowSelectionInterval(0,0) ;
            tblColumnNames.repaint() ;
        }
        
    }
    
    private boolean duplicateItem(Object [] rowColumnData) {
        boolean duplicate = false ;
        if (vecFromParent == null) {
            return duplicate;
        }
        if (vecFromParent.size()<=0) {
            return duplicate ;
        }
        
        for (int loopCount=0; loopCount< vecFromParent.size(); loopCount++) {
            if (rowColumnData[0].toString().equalsIgnoreCase(vecFromParent.get(loopCount).toString())) {
                duplicate = true ;
            }
        }
        
        return duplicate ;
    }
    
    
    private void getDataForTheTable() {
      /*
       Send the RequestTxnBean with appropraite parameters and get back the DataBean
       */
        Vector vectData = null;
        try {
            RequestTxnBean requestTxnBean = new RequestTxnBean();
            //requestTxnBean.setAction("GET_PROTOCOL_MODULES_DATA");
            //requestTxnBean.setProcedureName("GET_PROTOCOL_MODULES_DATA");
            //Changed For getting IACUC protocolModules -start
            if(moduleCode==7){
            requestTxnBean.setAction("GET_PROTOCOL_MODULES_DATA");
            requestTxnBean.setProcedureName("GET_PROTOCOL_MODULES_DATA");
            }
            else{
                requestTxnBean.setAction("GET_PROTOCOL_MODULES_DATA");
                requestTxnBean.setProcedureName("GET_AC_PROTOCOL_MODULES_DATA");
            }
            //Changed For getting IACUC protocolModules -End
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
                System.out.println("*** Recvd a DataBean ***");
                vectData = accDataBean.getVectData();
                System.out.println("vectData.size() in GUI: "+vectData.size());
            }
        } catch(Exception ex) {
            ex.printStackTrace() ;
            
        }
        
        accDataBean.setVectData(vectData);
        
    }
}

