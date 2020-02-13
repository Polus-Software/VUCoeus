/*
 * FormActionModule.java
 *
 * Created on June 19, 2007, 12:08 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.codetable.client;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.codetable.bean.AllCodeTablesBean;
import edu.mit.coeus.codetable.bean.DataBean;
import edu.mit.coeus.codetable.bean.RequestTxnBean;
import edu.mit.coeus.codetable.bean.StoredProcedureBean;
import edu.mit.coeus.codetable.bean.TableStructureBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TableSorterCodeTable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author talarianand
 */
public class FormActionModule extends CoeusDlgWindow {
    
    private Frame parent;
    
    DefaultTableModel codeTableModel ; // this model will hold the actual data
    
    DefaultTableColumnModel codeTableColumnModel ; // this model will hold the structure if the table
    
    DefaultTableColumnModel displayCodeTableColumnModel ; // this model will hold just the visual columns structure
    
    private AllCodeTablesBean allCodeTablesBean;
    
    private TableSorterCodeTable sorter;
    
    private JTable tblColumnNames;
    
    private JScrollPane scrlpnlForTableCtrl;
    
    private JPanel pnlForBtn;
    
    private JButton btnOk;
    
    private HashMap hashSelectedRow = null;
    
    private CoeusDlgWindow thisWindow;
    
    private CoeusComboBox cmbModule = new CoeusComboBox();
    
    private JLabel lblModule;
    
    private JLabel lblAction;
    
    private JPanel pnlAction;
    
    private JPanel pnlModule;
    
    private GridBagConstraints gridBagConstraints;
    
    private JSplitPane splitpnlMain;
    
    private String moduleCode = "";
    
    private HashMap hashAllCodeTableStructure;
    
    private TableStructureBean tableStructureModule;
    
    private Vector vectModule;
    
    private DataBean accDataBean;
    
    private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
    
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ CODE_TABLE_SERVLET;
    
    TableStructureBean tableStructureMailAction;
    
    int numColumnsDisplay = 0;
    
    String[] columnNames = {"ACTION_CODE", "DESCRIPTION", "MODULE_CODE"};
    
    private Vector vecFromParent = null;
    
    /** Creates a new instance of FormActionModule */
    public FormActionModule(Frame parent, String title, boolean modal, AllCodeTablesBean allCodeTablesBean) {
        super(parent, title, modal);
        //this.parent = parent ;
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
    
    private void initComponents() {
        
        
//        coeusMessageResources = CoeusMessageResources.getInstance();
        
        lblModule = new JLabel("Module Code");
        lblAction = new JLabel("Action");
        lblModule.setFont(CoeusFontFactory.getLabelFont());
        lblAction.setFont(CoeusFontFactory.getLabelFont());
        
        pnlAction = new JPanel(new GridBagLayout());
        pnlModule = new JPanel(new GridBagLayout());
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 0, 2, 1);
        pnlModule.add(lblModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(2, 5, 2, 1);
        pnlModule.add(cmbModule, gridBagConstraints) ;
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(4, 10, 2, 1);
        pnlModule.add(lblAction, gridBagConstraints) ;
        
        fillModuleData();
        
        sorter = new TableSorterCodeTable() ;
        tblColumnNames = new javax.swing.JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(tblColumnNames);
        
        tblColumnNames.setRowHeight(22);
        tblColumnNames.setAutoResizeMode(JTable.AUTO_RESIZE_OFF) ;
        javax.swing.table.JTableHeader header
                = tblColumnNames.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(new javax.swing.plaf.FontUIResource("Ms Sans Serif",Font.BOLD,11));
        
        BorderLayout borderLayout = new BorderLayout(0,0 ) ;
        JPanel pnlTable = new JPanel() ;
        pnlTable.setLayout(borderLayout) ;
        
        pnlTable.add(tblColumnNames.getTableHeader(), BorderLayout.NORTH) ;
        pnlTable.add(tblColumnNames, BorderLayout.CENTER ) ;
        pnlTable.setBackground(Color.white) ;
        
        scrlpnlForTableCtrl = new javax.swing.JScrollPane(pnlTable);
        scrlpnlForTableCtrl.setMinimumSize(new java.awt.Dimension(500 , 400)) ;
        scrlpnlForTableCtrl.setPreferredSize(new java.awt.Dimension(500, 400)) ;
        
        splitpnlMain = new javax.swing.JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlModule, scrlpnlForTableCtrl );
        splitpnlMain.setDividerSize(2);
        splitpnlMain.setOneTouchExpandable(true) ;
        splitpnlMain.setBackground(Color.white) ;
        
        cmbModule.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    getLastValue();
                    ComboBoxBean cmb = (ComboBoxBean) cmbModule.getSelectedItem() ;
                    String newRoleModuleCode = cmb.getCode();
                    moduleCode = newRoleModuleCode ;
                    if (moduleCode.equals("")) {
                        // hide the table
                        tblColumnNames.setVisible(false) ;
                    } else {
                        initialiseData();
                    }
                }
            }
        });
        
        pnlForBtn = new JPanel();
        pnlForBtn.setLayout(new GridBagLayout());
        btnOk = new JButton("OK") ;
        
        btnOk.setMnemonic('O');
        
        btnOk.setPreferredSize(new java.awt.Dimension(80, 25));
        
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(2, 1, 2, 0);
        pnlForBtn.add(btnOk,gridBagConstraints) ;
        
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (tblColumnNames.getRowCount()>0 ) {
                    if (tblColumnNames.getSelectedRow() != -1) {
                        hashSelectedRow = new HashMap() ;
                        hashSelectedRow.put("ACTIONID", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 0)) ;
                        hashSelectedRow.put("DESCRIPTION", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 1));
                        hashSelectedRow.put("MODULEID", ((ComboBoxBean)cmbModule.getSelectedItem()).getCode());
                        hashSelectedRow.put("MODULE", ((ComboBoxBean)cmbModule.getSelectedItem()).getDescription());
//                        hashSelectedRow.put("Module", codeTableModel.getValueAt(sorter.getIndexForRow(tblColumnNames.getSelectedRow()), 2));
                    }
                }
                
                thisWindow.dispose() ;
            }
        }) ;
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        pnlAction.add(splitpnlMain, gridBagConstraints) ;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlAction.add(pnlForBtn, gridBagConstraints) ;
        
        getContentPane().add(pnlAction, BorderLayout.CENTER);
        pack() ;
        
        // this will take care of the window closing...
        this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
//                btnCloseActionPerformed() ;
            }
        } );
    }
    
    public void showForm() {
        thisWindow = new CoeusDlgWindow(parent, "Actions", true){
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
        
        thisWindow.getContentPane().add(pnlAction);
        thisWindow.setSize(600,400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        thisWindow.setLocation(screenSize.width/5 , screenSize.height/5 );
        
//        initialiseData();
        thisWindow.setVisible(true);
    }
    
    public void initialiseData() {
        codeTableModel = new DefaultTableModel() {
            public boolean isCellEditable(int row,int col) {
                return false;
            }
        };
        
        codeTableColumnModel = new DefaultTableColumnModel() ;
        displayCodeTableColumnModel = new DefaultTableColumnModel() ;
        
        // tblCodeTable.addFocusListener(this) ;
        tblColumnNames.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        
        
        //hold all data of one table in vdata.
        Vector vdata = new Vector();
        numColumnsDisplay = 2 ;
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
            
            Vector vecModuleData = filterData(vdata);
            
            for (int i=0; i < vecModuleData.size(); i++) //loop for num of rows
            {
                htRow = (HashMap)vecModuleData.elementAt(i);
                Object [] rowColumnDatas = new Object[4];
                
                for (int j = 0; j < numColumnsDisplay; j++) // loop for num of columns will display on screen
                {
                    if(moduleCode.equals((String)htRow.get(columnNames[2]))) {
                        rowColumnDatas[j] = (Object)htRow.get(columnNames[j]);
                        Object obj = rowColumnDatas[j];
                    }
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
    
    private Vector filterData(Vector vdata) {
        Vector vecData = new Vector();
        
        if(vdata != null && vdata.size() > 0) {
            HashMap hmRow = new HashMap();
            for(int index = 0; index < vdata.size(); index++) {
                hmRow = (HashMap)vdata.get(index);
                if(moduleCode.equals((String)hmRow.get("MODULE_CODE"))) {
                    vecData.addElement(hmRow);
                }
            }
        }
        return vecData == null ? new Vector() : vecData;
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
            if (rowColumnData[0].toString().equals(vecFromParent.get(loopCount).toString())) {
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
            requestTxnBean.setAction("GET_ACTIONS_FOR_EMAIL_NOTIF");
            
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(requestTxnBean) ;
            
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.hasResponse()) {
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
    
    private void fillModuleData() {
        hashAllCodeTableStructure = allCodeTablesBean.getHashAllCodeTableStructure();
        TableStructureBean tempTableStructBean = null;
        
        for(int tableCount = 0; tableCount < hashAllCodeTableStructure.size(); tableCount++) {
            tempTableStructBean = (TableStructureBean) hashAllCodeTableStructure.get(new Integer(tableCount));
            
            if (tempTableStructBean.getActualName().equalsIgnoreCase("osp$coeus_modules")) {
                tableStructureModule = tempTableStructBean ;
            }
        }
        
        vectModule = getDataForCombo(tableStructureModule);
        HashMap hmRow = new HashMap();
        cmbModule.addItem(new ComboBoxBean("", "")) ;
        for (int i=0; i < vectModule.size(); i++) //loop for num of rows
        {
            hmRow = (HashMap)vectModule.elementAt(i);
            cmbModule.addItem(new ComboBoxBean
                    (hmRow.get("MODULE_CODE").toString() ,
                    hmRow.get("DESCRIPTION").toString()));
        }//end for
    }

    public Vector getDataForCombo(TableStructureBean tableStructureBean) {
        Vector vectData = null;
        
        try {
            if(tableStructureBean != null) {
                //Get all stored procedures associated with this table.
                HashMap hashStoredProcedure =
                        (HashMap)tableStructureBean.getHashStoredProceduresForThisTable();
                if(hashStoredProcedure != null) {
                    //Get the select stored procedure associated witht this table.
                    StoredProcedureBean selectStoredProcedure =
                            (StoredProcedureBean)hashStoredProcedure.get(new Integer(0));
                    
                    RequestTxnBean requestTxnBean = new RequestTxnBean();
                    requestTxnBean.setAction("GET_DATA");
                    requestTxnBean.setStoredProcedureBean(selectStoredProcedure);
                    
                    RequesterBean requester = new RequesterBean();
                    requester.setDataObject(requestTxnBean) ;
                    
                    AppletServletCommunicator comm
                            = new AppletServletCommunicator(connectTo,requester);
                    comm.send();
                    
                    ResponderBean responder = comm.getResponse();
                    
                    if (responder.hasResponse()) {
                        accDataBean = (DataBean)responder.getDataObject() ;
                    }
                    
                    if(accDataBean != null) {
                        vectData = accDataBean.getVectData();
                    }
                }
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return vectData;
    }
    
}
