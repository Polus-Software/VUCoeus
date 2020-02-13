/*
 * @(#)CoeusTableWindow.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.gui;

import java.awt.event.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import javax.swing.table.*;
import javax.swing.ListSelectionModel;

import edu.mit.coeus.utils.OtherLookupBean;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;

/**
 * This is a reusbale component used to which contain a Table with Ok and Cancel
 * Button. It is mainly used in response windows like the display of code/
 * description and selecting particular entry. All the supporting properties like
 * modal, escape key etc are confined to derived class or container class.
 *
 * Created on March 7, 2003, 9:39 AM
 * 
 * @author  subramanya
 * Changes Done by Raghunath P.V.
 */
public class CoeusTableWindow extends javax.swing.JComponent 
                                                    implements ActionListener{
    
                                                    
     //holds the selected rows of the Table window
    private int selectedRow;
    
    //holds the Parent window component.
    private CoeusDlgWindow dlgParentComponent;
    private OtherLookupBean otherLookupBean;
    private CoeusAppletMDIForm mdiForm;
    private String title;
    /** 
     * Creates new form CoeusTableWindow 
     * @param dlgPrnt parent dialog window component.
     */
    public CoeusTableWindow( CoeusDlgWindow dlgPrnt ) {
        initComponents();
        DefaultTableModel defaultTableModel = prepareTableModel();
        //by default it is single slection.
        postInitComponent( defaultTableModel );        
        dlgParentComponent = dlgPrnt;
    }
    
    public CoeusTableWindow(OtherLookupBean otherLookupBean ) {
        
        this.otherLookupBean = otherLookupBean;
        if(otherLookupBean != null){
            this.title = otherLookupBean.getWindowTitle();
            this.otherLookupBean.setSelectedInd(-1);
        }
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                title, true);
        dlgParentComponent.getContentPane().add(createLookupWindow());
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);

        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        dlgParentComponent.setSize(670,300);
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
//        dlgParentComponent.addKeyListener(new KeyAdapter(){
//            public void keyReleased(KeyEvent ke){
//                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
//                    dlgParentComponent.dispose();
//                }
//            }
//        });
        dlgParentComponent.addWindowListener( new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                if( tblDetail.getRowCount() > 0 ) {
                    tblDetail.setRowSelectionInterval(0,0);
                    tblDetail.requestFocusInWindow();
                }else{
                    btnCancel.requestFocusInWindow();
                }
            
            }
            public void windowClosing(WindowEvent we){
                dlgParentComponent.dispose();
            }
        });
        dlgParentComponent.show();
        //initComponents();
        //by default it is single slection.
          
        //dlgParent = dlgPrnt;
    }
    
    private JComponent createLookupWindow(){
        initComponents();
        DefaultTableModel defaultTableModel = prepareTableModel();
        postInitComponent(defaultTableModel );      
        return this;
    }
    
    //Added By Raghunath P.V.
    private DefaultTableModel prepareTableModel(){
        ComboBoxBean cbBean = null;
        Vector data = null;
        String[] colNames = null;
        Vector vecColNames = null;
        String windowTitle = null;
        Vector vcData = null;
        Vector vcDataPopulate = new Vector();
        DefaultTableModel tableModel = null;
        if(otherLookupBean != null){
            data = otherLookupBean.getLookupDataValues();
            colNames = otherLookupBean.getColumnNames();
            windowTitle = otherLookupBean.getWindowTitle();
            if(data != null){
                int dataSize = data.size();
                for(int index = 0 ; index < dataSize; index++){
                    cbBean = (ComboBoxBean)data.elementAt(index);
                    if(cbBean != null){
                        vcData = new Vector();
                        String valueForCode = cbBean.getCode();
                        String descForCode = cbBean.getDescription();
                        vcData.addElement(valueForCode);
                        vcData.addElement(descForCode);
                        vcDataPopulate.addElement(vcData);
                    }
                }
            }
            if(colNames != null){
                vecColNames = new Vector();
                String colNameForCode = colNames[0];
                String colNameForDesc = colNames[1];
                vecColNames.addElement(colNameForCode);
                vecColNames.addElement(colNameForDesc);
            }
            
            if(vcDataPopulate != null && vecColNames != null){
                tableModel = new DefaultTableModel(vcDataPopulate, vecColNames ){
                      public boolean isCellEditable(int rowIndex, int columnIndex){
                          return false;
                      }
                };
            }
        }
        return tableModel;
    }
    
    /** 
     * Creates new form CoeusTableWindow. 
     * @param dlgPrnt parent dialog window.
     * @param selType boolean value true refer to single selection and
     * false value refer to multiple selection.
     * @param tblModel DefaultTableModel for the table
     */
    public CoeusTableWindow( CoeusDlgWindow dlgPrnt, 
                                boolean selType, DefaultTableModel tblModel ){        
        initComponents();
        DefaultTableModel defaultTableModel = prepareTableModel();
        postInitComponent(defaultTableModel );
        dlgParentComponent = dlgPrnt;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnDetail = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnDetail.setPreferredSize(new java.awt.Dimension(400, 250));
        scrPnDetail.setMinimumSize(new java.awt.Dimension(400, 250));
        scrPnDetail.setMaximumSize(new java.awt.Dimension(400, 250));
        tblDetail.setFont(CoeusFontFactory.getNormalFont());
        scrPnDetail.setViewportView(tblDetail);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 5, 0);
        add(scrPnDetail, gridBagConstraints);

        btnOk.setMnemonic('O');
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 0, 2);
        add(btnOk, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 2);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblDetail;
    private javax.swing.JScrollPane scrPnDetail;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    // End of variables declaration//GEN-END:variables
 
    /**
     * This method is used to set the component properties after the instantiation
     * of the component.
     * @param isSingleSelection boolean value true refer to single selection and
     * false value refer to multiple selection.
     * @param tblModel DefaultTableModel for the table
     */
    public void postInitComponent( DefaultTableModel tblModel ){
        
        this.tblDetail.setModel( tblModel );
//        this.scrPnDetail.setSize(600,300);
        Dimension detailPaneSize = new Dimension(570,250);
        this.scrPnDetail.setMinimumSize(detailPaneSize);
        this.scrPnDetail.setMaximumSize(detailPaneSize);
        this.scrPnDetail.setPreferredSize(detailPaneSize);
        this.scrPnDetail.setViewportView( this.tblDetail );
        this.tblDetail.setSelectionMode( 
                                          ListSelectionModel.SINGLE_SELECTION );
        tblDetail.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        JTableHeader header = tblDetail.getTableHeader(); 
        
        header.setFont(CoeusFontFactory.getLabelFont());
        
        TableColumn column = tblDetail.getColumnModel().getColumn(0);
        
        column.setMinWidth(200);
        //column.setMaxWidth(80);
        column.setPreferredWidth(200);
        column.setResizable(true);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);// Added by chandra 04/02/04
        
        // Added by chandra for column resizing - 04/02/2004
        column = tblDetail.getColumnModel().getColumn(1);
        column.setMinWidth(345);
        // End Chandra - 04/02/2004
        /*tblDetail.setRowSelectionAllowed( true );
        tblDetail.setCellSelectionEnabled( false );
        tblDetail.setColumnSelectionAllowed( false );*/
        this.btnOk.addActionListener( this );
        this.btnCancel.addActionListener( this );
    }
    
    /**
     * This method is used to fetch the Table component of this window.
     * @return JTable table contained in this compoent.
     */
    public JTable getDisplayTable(){
        return this.tblDetail;
    }
    
    /**
     * This method is used to set the Table Model explicity at run time.
     * @param tblModel DefaultTableModel for the table
     */
    public void setTableModel( DefaultTableModel tblModel ){
        this.tblDetail.setModel( tblModel );
    }
        
    /**
     * This method is used to capture the action events fired by ok/cancel 
     * button of this table window compoent.
     * @param actionEvent component (button) Action instance
     */
    public void actionPerformed( ActionEvent actionEvent ) {
        
        Object actionSource = actionEvent.getSource();
        if( actionSource.equals( this.btnOk ) ){
            selectedRow = this.tblDetail.getSelectedRow();
            this.otherLookupBean.setSelectedInd(selectedRow);
            if( dlgParentComponent != null ){            
                dlgParentComponent.dispose();
            }
        }else if(actionSource.equals( this.btnCancel )){
            if( dlgParentComponent != null ){            
                dlgParentComponent.dispose();
            }
        }
    }
    
    /**
     * This method is used to get All the Selected Rows Indices from the table.
     * @return Hashtable all the selected indices as key and data of the table
     *  as value ( contined in Vector).
     */
    /*
    public Hashtable getAllSelectedRows(){
        Hashtable hsSelectedTableData = new Hashtable();
        for( int indx = 0 ; indx < selectedRows.length; indx++ ){
            hsSelectedTableData.put( Integer.toString( indx ),getRowData( indx));
        }
        return hsSelectedTableData;
    }*/
    
    //method to fetch one complet row data
    private Vector getRowData( int rowIndx ){
        Vector vctRowData = new Vector( this.tblDetail.getRowCount() );
        int clmCount = this.tblDetail.getColumnCount();
        for( int clmIndx = 0; clmIndx < clmCount; clmIndx++ ){
            vctRowData.addElement( this.tblDetail.getValueAt( rowIndx, clmIndx));
        }
        return vctRowData;
    }
    
    /**
     * Unit Testing Routine for this CoeusTableWindow component.
     */
    /*public static void main( String[] arg ){
        javax.swing.JDialog frm = new javax.swing.JDialog();
        frm.setTitle("Unit Testing of CoeusTableWindow");
        
        DefaultTableModel tblCodeDescription = new DefaultTableModel(
            new String[][]{{"1","desc of one"},{"2", "desc of Two"}},
            new String[]{"Code", "Description"}){
                Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class             
            };
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            public boolean isCellEditable(int row, int col){
                return false;
            }
            };
        
        frm.getContentPane().add( new CoeusTableWindow( frm, false, 
                                                tblCodeDescription ));
        frm.pack();
        frm.show();
    }*/
    
}
