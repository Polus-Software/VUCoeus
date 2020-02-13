/*
 * @(#)RolodexReferencesPanel.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.rolodexmaint.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.AppletServletCommunicator;                           
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.rolodexmaint.bean.RolodexReferencesBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.gui.CoeusMessageResources;

import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.event.*;
import java.awt.ActiveEvent.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.util.Vector;
import javax.swing.table.*;
import javax.swing.DefaultListSelectionModel;
import java.util.HashMap;

/** 
 * This class is used to handle rolodex references.
 * It gets the details of the selcted rolodex displays its details and shows its references. 
 * If the user has enough rights, it allows to replace those references with another one.
 * It is a dialog which contains
 * 1.  a panel for rolodex details. 
 *     It is a object of RolodexMaintenanceDetailForm.
 *     It gives a non editable panel for rolodex details. This is added to a scroll pane.
 * 2.  A table for rolodex references
 * 3.  Buttons Ok and cancel. 
 * Created on March 12, 2004, 12:21 PM
 * @author  bijosh  
 */
public class RolodexReferencesPanel extends javax.swing.JComponent
implements ActionListener{
    
    private JPanel rolodexDetailsPanel;
    private int rolodexId; 
    private boolean hasRights;
    private RolodexMaintenanceDetailForm rolodexMaintenanceDetailForm; 
    private CoeusVector rolodexReferencesVector;
    private edu.mit.coeus.utils.Utils Utils;
    private static final String EMPTY_STRING = "";
    private String newSelectedRolodexID = EMPTY_STRING;
    private RolodexTableModel rolodexTableModel;
    private RolodexTableRenderer  rolodexTableRenderer; 
    private static final int TABLE_COLUMN =0;
    private static final int COLUMN_COLUMN  = 1;
    private static final int COUNT_COLUMN = 2;
    //Parameter to the Coeus Search to display the search window.
    private  final static String ROLODEX_SEARCH = "rolodexSearch"; 
    private static final String TABLE_COLUMN_NAME="Table";
    private static final String COLUMN_COLUMN_NAME="Column";
    private static final String COUNT_COLUMN_NAME="Count";
    private static final int NUM_OF_COLUMNS=3;
    
    private static final String WINDOW_TITLE="Rolodex References";
    
    private final String ROLODEX_SERVLET = "/rolMntServlet"; 
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + ROLODEX_SERVLET; 
    private final char SERVLET_FN_TYPE='E';
    private RolodexDetailsBean rolodexDetailsBean; 
    private CoeusAppletMDIForm mdiForm; 
    private CoeusDlgWindow dlgRolodexReference;
    private String strSelectedTable;
    private CoeusVector rolodexReferencesVect;
    private RolodexReplacePanel rolodexReplacePanel;
    private boolean isReplacePanelCreated;
    private CoeusMessageResources coeusMessageResources;
    private final static String [] RESTICTEDTABLES = {"OSP$EPS_PROP_INVESTIGATORS",
                                                 "OSP$EPS_PROP_KEY_PERSONS",
                                                 "OSP$EPS_PROP_LOCATION",
                                                 "OSP$EPS_PROP_PERSON",
                                                 "OSP$EPS_PROPOSAL",
                                                 "OSP$SPONSOR",
                                                 "OSP$PROPOSAL_INV_CERTIFICATION"};
       
    /** Creates new form RolodexReferencesPanel
     * @param mdiForm Parent for thsi dialog
     * @param rolodexId Id for the rolodex
     */
     public RolodexReferencesPanel(CoeusAppletMDIForm mdiForm,String rolodexId) { 
        this.mdiForm=mdiForm; 
        this.rolodexId=Integer.parseInt(rolodexId);
        initComponents();
        coeusMessageResources = CoeusMessageResources.getInstance();
        isReplacePanelCreated=false;
        Vector dataVector=getDataFromServer(); 
        rolodexDetailsBean=(RolodexDetailsBean)dataVector.elementAt(0); // Rolodex details 
        rolodexReferencesVect = (CoeusVector)dataVector.elementAt(1); // Rolodex References
        hasRights =((Boolean)dataVector.elementAt(2)).booleanValue();  // User rights
        /* Creates an object of RolodexMaintenanceDetailForm for showing 
         * rolodex details in display mode.
         * The constructor passes rolodex id and the bean as parameters. 
         */
        rolodexMaintenanceDetailForm = new RolodexMaintenanceDetailForm(
                                                rolodexId,rolodexDetailsBean);
        rolodexTableModel = new RolodexTableModel();
        //sets the table model
        tblRolodex.setModel(rolodexTableModel);
        registerComponents();
        //sets the table data
        rolodexTableModel.setData(rolodexReferencesVect);
        // sets the table renderer
        rolodexTableRenderer = new RolodexTableRenderer();
        setColumnData();
        displayComponents();
        display();
    }
    
     /** Handles the actions for Close button and Replace button.
      * @param actionEvent action Event for the buttons
      */     
    public void actionPerformed(ActionEvent  actionEvent){
        Object source = actionEvent.getSource();
        if (source.equals(btnReplace)) {
            int iSelectedRowIndex=tblRolodex.getSelectedRow();
            if (iSelectedRowIndex==-1)  {
                CoeusOptionPane.showErrorDialog(this.getParent(),
                    coeusMessageResources.parseMessageKey(
                    "rolodexReferences_exceptionCode.1100")+rolodexId);
                return;
            }
            Object objectCount = (tblRolodex.getModel()).getValueAt(iSelectedRowIndex,2);
            Object objectTable = (tblRolodex.getModel()).getValueAt(iSelectedRowIndex,0);
            int intCountValue = Integer.parseInt(objectCount.toString());
            strSelectedTable=objectTable.toString();
            // To check whether the selected table is any of the replacing restricted table.
            for (int i=0;i<RESTICTEDTABLES.length;i++) {
                if (strSelectedTable.equals(RESTICTEDTABLES[i])) {
                    CoeusOptionPane.showErrorDialog(this.getParent(),
                    coeusMessageResources.parseMessageKey(
                    "rolodexReferences_exceptionCode.1101")+strSelectedTable); 
                    return;
                }
             }// end of for loop

            if (intCountValue <= 0) {
               CoeusOptionPane.showErrorDialog(this.getParent(),"The rolodex ID "+
               rolodexId +" is not being referenced in the table "+strSelectedTable); 
               return;
            }
           // Open the Rolodex lookup window
            dlgRolodexReference.setCursor( new Cursor( Cursor.WAIT_CURSOR ) ); 
            showRolodexSearchWindow();
            dlgRolodexReference.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );  
        }
        else if (source.equals(btnClose)) {
            dlgRolodexReference.dispose();
        }
            
    }
    
    /* Used  for showing the search window
     */
    private void showRolodexSearchWindow() {
        try{
            CoeusSearch coeusSearch = new CoeusSearch(mdiForm, ROLODEX_SEARCH, 1);
            coeusSearch.showSearchWindow();
            HashMap rolodexInfo = coeusSearch.getSelectedRow();
            if(rolodexInfo!=null){
                newSelectedRolodexID = Utils.convertNull(rolodexInfo.get(
                                                            "ROLODEX_ID"));
                //checks whether the rolodex selected and the current rolodex are the same or not
                if ((Integer.parseInt(newSelectedRolodexID))==rolodexId) { 
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(
                            "rolodexReferences_exceptionCode.1102"));
                    return;
                }
                /* This check is to avoid the creation of the panel each time. 
                   Only first time it is created.
                 */
                if (isReplacePanelCreated==false) {
                    rolodexReplacePanel = new RolodexReplacePanel(mdiForm,
                                                            rolodexDetailsBean);
                    isReplacePanelCreated=true;
                }
                
                RolodexReferencesBean selectedTableBean=(
                    RolodexReferencesBean)rolodexReferencesVect.elementAt(
                                                   tblRolodex.getSelectedRow());
                
                // assigns the new components for the newly selceted rolodex.
                rolodexReplacePanel.initailizeNewRolodexComponents(
                                newSelectedRolodexID, selectedTableBean);    
                dlgRolodexReference.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
                /* If rolodex has been replaced in the replace dialog, 
                 * the count for the that table is set to 0 in the table.
                 **/
                boolean replaced = rolodexReplacePanel.display();//checks whether reference is replaced 
                                                               
                if (replaced) {
                    selectedTableBean.setCount(0); //setting count coulumn bvalue to 0
                }
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    /* Adds the action listeners. 
     */
     private void registerComponents(){     
        btnReplace.addActionListener(this);
        btnClose.addActionListener(this);
        /* checks whether user has the right to replace rolodex references
         * Depending on that the replace button is made enabled or disabled
         */
        if (hasRights) {
            btnReplace.setEnabled(true);
        } else {
            btnReplace.setEnabled(false);
        }
        //gets the rolodex details panel and addes it to the scrollpane.
        rolodexDetailsPanel=rolodexMaintenanceDetailForm.getRolodexComponent();
        jcrPnRolodexDetails.setViewportView(rolodexDetailsPanel);
       
        Component[] comp = {btnClose,btnReplace};
                            
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);        

    }
      /* Sets the display attributes for the dialog
       */     
      private void displayComponents() {
        dlgRolodexReference = new CoeusDlgWindow(mdiForm);
        dlgRolodexReference.setModal(true);
        dlgRolodexReference .setTitle(WINDOW_TITLE);
        dlgRolodexReference .setFont(CoeusFontFactory.getLabelFont());
        dlgRolodexReference .setModal(true);
        dlgRolodexReference.setResizable(false);
        dlgRolodexReference .getContentPane().add(this);
        dlgRolodexReference.addEscapeKeyListener(
         new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgRolodexReference.dispose();
            }
        });
        dlgRolodexReference.setDefaultCloseOperation(
                                            CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRolodexReference.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                btnClose.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
               dlgRolodexReference.dispose();
            }
        });
      }
      
      private void display(){
          dlgRolodexReference.pack();
          dlgRolodexReference.setLocation(CoeusDlgWindow.CENTER);
          dlgRolodexReference.setVisible(true);
      }
      
     /* Sets the table colomn properties like headers,
      * cell renderers,selection properties width etc
      */  
     private void setColumnData() {
        JTableHeader tableHeader = tblRolodex.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblRolodex.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblRolodex.setRowHeight(22);
        tblRolodex.setSelectionBackground(java.awt.Color.white);
        tblRolodex.setSelectionForeground(java.awt.Color.black);
        tblRolodex.setShowHorizontalLines(false);
        tblRolodex.setShowVerticalLines(true);
        tblRolodex.setOpaque(false);
        tableHeader.setResizingAllowed(true);
        tblRolodex.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        //sets the selection backgrond as dark blue
        tblRolodex.setSelectionBackground(new Color(0,0,128)); 
        TableColumn column = tblRolodex.getColumnModel().getColumn(TABLE_COLUMN);
        column.setPreferredWidth(350);
        column.setCellRenderer(rolodexTableRenderer);
        column = tblRolodex.getColumnModel().getColumn(COLUMN_COLUMN);
        column.setPreferredWidth(250);
        column.setCellRenderer(rolodexTableRenderer);
        column = tblRolodex.getColumnModel().getColumn(COUNT_COLUMN);
        column.setPreferredWidth(70);
        column.setCellRenderer(rolodexTableRenderer);
 }
     
     /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and Effective date
     *columns only which are primary keys.
     */
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","tableName" },
            {"1","columnName" },
            {"2","count"}
       };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                 // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
               // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(rolodexReferencesVect!=null && rolodexReferencesVect.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)rolodexReferencesVect).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    rolodexTableModel.fireTableRowsUpdated(
                                            0, rolodexTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
   
    /** Table cell renderer for the table.
     * For rows with count > 0, sets the foreground color as red.
     */    
public class RolodexTableRenderer extends  DefaultTableCellRenderer{
                  
         public Component getTableCellRendererComponent(JTable table,Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
             Object object = rolodexTableModel.getValueAt(row, 2);
             int intValue = Integer.parseInt(object.toString());
             Component component = super.getTableCellRendererComponent(table,value,
                                                isSelected, hasFocus, row, column);
             if (intValue > 0) {        // count greater than 0
                 if (isSelected) {      // row is selected
                      component.setForeground(java.awt.Color.white); 
                 } else {               //row not selected
                      component.setForeground(new java.awt.Color(255,0,0)); //red
                 }
             } else {                   // Count is 0
                 if (isSelected) {      // row is selected
                    component.setForeground(Color.white);
                 } else {               //row not selected
                    component.setForeground(Color.black);
                 }
              }
            return component;
         }
     }

     /* Retrieves data from server for the rolodex details (RolodexDetailsBean), 
      *     references (Vector of RolodexReferencesBean beans) 
      *     and for user right checking (Boolean object).
      * Returns a Vector with these 3 elements
      */
     private Vector getDataFromServer() {
        RequesterBean request = new RequesterBean();
        request.setDataObject(new Integer(rolodexId));
        request.setFunctionType(SERVLET_FN_TYPE);
        AppletServletCommunicator comm
           = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
             Vector vctRolReferences = response.getDataObjects();
             return vctRolReferences;
        } else{
             CoeusOptionPane.showErrorDialog(response.getMessage());
             return null;
        }
    }
     /** Table model for the table
       */
     public class RolodexTableModel extends AbstractTableModel 
                                    implements TableModel {
         public boolean isCellEditable(int row, int col) {
             return false;
         }
         
         public void setData(CoeusVector rolodexReferencesVect) {
             rolodexReferencesVector = rolodexReferencesVect;
         }
         
         public int getColumnCount() {
             return NUM_OF_COLUMNS;
         }
         
         public int getRowCount() {
             if(rolodexReferencesVector==null) {
                 return 0;
             }else{
                return rolodexReferencesVector.size();
             }
         }
         
         public Object getValueAt(int row, int col) {
             RolodexReferencesBean rolodexReferencesBean = 
                (RolodexReferencesBean)rolodexReferencesVector.get(row);
             switch(col){
                 case TABLE_COLUMN:
                     return rolodexReferencesBean.getTableName();
                 case COLUMN_COLUMN:
                     return rolodexReferencesBean.getColumnName();
                 case COUNT_COLUMN:
                     return new Integer(rolodexReferencesBean.getCount());
                 default:
                     return EMPTY_STRING;
                         
             }
             
         }
         
         public String getColumnName(int col){
             switch(col){
                 case TABLE_COLUMN:
                     return TABLE_COLUMN_NAME;
                 case COLUMN_COLUMN:
                     return COLUMN_COLUMN_NAME;
                 case COUNT_COLUMN:
                     return COUNT_COLUMN_NAME;
                 default:
                     return EMPTY_STRING;
             }
             
             
         }
     }    
     
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jcrPnRolodexDetails = new javax.swing.JScrollPane();
        jcrPnRolodexTableData = new javax.swing.JScrollPane();
        tblRolodex = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnReplace = new javax.swing.JButton();
        lblRolodexRefSummary = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(790, 529));
        setPreferredSize(new java.awt.Dimension(790, 529));
        jcrPnRolodexDetails.setMinimumSize(new java.awt.Dimension(690, 412));
        jcrPnRolodexDetails.setPreferredSize(new java.awt.Dimension(690, 412));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(jcrPnRolodexDetails, gridBagConstraints);

        jcrPnRolodexTableData.setMinimumSize(new java.awt.Dimension(690, 115));
        jcrPnRolodexTableData.setPreferredSize(new java.awt.Dimension(690, 115));
        tblRolodex.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jcrPnRolodexTableData.setViewportView(tblRolodex);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 0, 0);
        add(jcrPnRolodexTableData, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 3);
        add(btnClose, gridBagConstraints);

        btnReplace.setFont(CoeusFontFactory.getLabelFont());
        btnReplace.setMnemonic('R');
        btnReplace.setText("Replace");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 3);
        add(btnReplace, gridBagConstraints);

        lblRolodexRefSummary.setFont(CoeusFontFactory.getLabelFont());
        lblRolodexRefSummary.setText("Rolodex References Summary ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblRolodexRefSummary, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnReplace;
    private javax.swing.JScrollPane jcrPnRolodexDetails;
    private javax.swing.JScrollPane jcrPnRolodexTableData;
    private javax.swing.JLabel lblRolodexRefSummary;
    private javax.swing.JTable tblRolodex;
    // End of variables declaration//GEN-END:variables
    
}
