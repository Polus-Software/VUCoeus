/*
 * ContingencyLookUpWindow.java
 *
 * Created on November 26, 2002, 10:41 AM
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.gui.MinuteEntryForm;

import javax.swing.table.* ;
import java.util.Vector;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
/**
 * This class provides the Contingency Code and Description in a list.
 *
 * @author  Raghunath P.V.
 * modified Sagin on Dec 02 2002
 */
public class ContingencyLookUpWindow extends javax.swing.JComponent {
    
    private Vector contingencyData;
    //Dialog window in which this component is shown
    private CoeusDlgWindow dlgWindow;
    
    private ContigencyCellRenderer contigencyCellRenderer;
    
    //Used to read messages from Messages.properties file
    private CoeusMessageResources coeusMessageResources;
    
    //Holds reference of the parent form
    private MinuteEntryForm minuteEntryForm;
    
    private DefaultTableColumnModel colModel ;
    
    private static Vector contingencies = null;
    public static final int OK = 1;
    public static final int CANCEL = 0;
    private int retValue;
    
    /** Creates new form ContingencyLookUpWindow */
    public ContingencyLookUpWindow() {

        initComponents();
        Vector contingencies = getValues();
        setValues(contingencies);
        coeusMessageResources = CoeusMessageResources.getInstance();
        contigencyCellRenderer = new ContigencyCellRenderer();
        setColumnSize() ;
    }
    
    //private DefaultTableColumnModel colModel ;
    private void setColumnSize() {
        
        
        tblContingencyDescription.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        colModel = new DefaultTableColumnModel() ;
        colModel.addColumn(new TableColumn(0, 50)) ;
        //colModel.addColumn(new TableColumn(1, 800)) ;
        colModel.addColumn(new TableColumn(1, 545)) ;
        
        // Added by chandra - 20/10/2003 - start
        tblContingencyDescription.getColumnModel().getColumn(0).setMaxWidth(50);
        tblContingencyDescription.getColumnModel().getColumn(0).setMinWidth(50);
        tblContingencyDescription.getColumnModel().getColumn(0).setPreferredWidth(50);
        
        tblContingencyDescription.getColumnModel().getColumn(1).setCellRenderer(contigencyCellRenderer);
        tblContingencyDescription.getColumnModel().getColumn(1).setMaxWidth(900);
        tblContingencyDescription.getColumnModel().getColumn(1).setMinWidth(545);
        tblContingencyDescription.getColumnModel().getColumn(1).setPreferredWidth(545);
        // Added by chandra - 20/10/2003 - End
        
        colModel.getColumn(0).setHeaderValue("Code") ;
        colModel.getColumn(1).setHeaderValue("Description") ;
        //tblContingencyDescription.setColumnModel(colModel) ;
        
    }
    
    /**
     * Sets the reference of the Minute Entry Form(parent).
     * @param object MinuteEntryForm
     */
    public void setMinuteEntryForm(MinuteEntryForm minuteEntryFrm) {
        this.minuteEntryForm = minuteEntryFrm;
    }
    
    
    /**
     * connect to the server and execute the procedure to get the contingency values.
     * the servlet returns the vector which contain the combo box bean with the values
     * of contingency code and desc.
     *
     * @return Vector contain the comboboxbean which contain the code and desc
     */
    
    private static Vector getValues(){
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_CONTINGENCY");
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        if (response!=null){
            // get the contingency data. the vector contains the contingency beans
            // contigencies vector elements contains coeus combo box bean with code and desc
            contingencies = (Vector) response.getDataObject();
        }
        return contingencies;
    }
    
    /**
     * set the column values to the contingency table.
     *
     * @param Vector contains the combo box bean which contain the contingency code and desc
     */
    private void setValues(Vector contingencies){
        if (contingencies!=null) {
            // vector contain code and desc
            Vector rowData = null;
            
            // encapsulate the rowData vector into contingencyData vector
            // Vector contingencyData = new Vector();
            contingencyData = new Vector();
            
            
            // read the elements from the vector, get the code/desc from combo box bean
            // and set it to table
            int count = contingencies.size();
            DefaultTableModel contingencyTableModel =
            (DefaultTableModel)tblContingencyDescription.getModel();
            for( int rowIndex = 0; rowIndex < count; rowIndex++ ){
                rowData = new Vector();
                ComboBoxBean comboBoxBean = (ComboBoxBean) contingencies.elementAt(rowIndex);
                // get contingency code and add to the vector
                rowData.addElement(comboBoxBean.getCode());
                // get contingency desc and add to the vector
                rowData.addElement(comboBoxBean.getDescription());
                // add the vecot rowData into contingencyData
                contingencyData .addElement(rowData);
                contingencyTableModel.addRow(rowData);
            }
            contingencyTableModel.fireTableDataChanged();
            if (count > 0) {
                tblContingencyDescription.setRowSelectionInterval(0, 0);
            }
            
            //This loop is to set the widths for the table according to description.
            for(int index=0;index<contingencyData.size();index++){
                             
                tblContingencyDescription.setRowMargin(2);
                tblContingencyDescription.setRowHeight(index,32);
                tblContingencyDescription.setRowSelectionInterval(0,index);
            }
            
            //raghuSV included to always set selection to first row.
            if(tblContingencyDescription.getRowCount()>0){
                tblContingencyDescription.setRowSelectionInterval(0, 0);
            }
            
        }
        
        
        
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JButton btnOk;
        javax.swing.JButton btnCancel;
        javax.swing.JScrollPane scrPnContingencyDescription;

        scrPnContingencyDescription = new javax.swing.JScrollPane();
        tblContingencyDescription = new javax.swing.JTable();
        tblContingencyDescription.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(700, 350));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ContingencyLookUpWindow.this.keyPressed(evt);
            }
        });

        scrPnContingencyDescription.setPreferredSize(new java.awt.Dimension(250, 200));
        scrPnContingencyDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ContingencyLookUpWindow.this.keyPressed(evt);
            }
        });

        tblContingencyDescription.setFont(CoeusFontFactory.getNormalFont());
        tblContingencyDescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblContingencyDescription.setToolTipText("");
        tblContingencyDescription.setMaximumSize(new java.awt.Dimension(2147483647, 5));
        tblContingencyDescription.setRowHeight(20);
        tblContingencyDescription.setShowHorizontalLines(false);
        tblContingencyDescription.setShowVerticalLines(false);
        tblContingencyDescription.setOpaque(false);
        tblContingencyDescription.getTableHeader().
        setFont(CoeusFontFactory.getLabelFont());
        tblContingencyDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ContingencyLookUpWindow.this.keyPressed(evt);
            }
        });

        scrPnContingencyDescription.setViewportView(tblContingencyDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(scrPnContingencyDescription, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setToolTipText("");
        btnOk.setMaximumSize(new java.awt.Dimension(80, 21));
        btnOk.setMinimumSize(new java.awt.Dimension(80, 21));
        btnOk.setPreferredSize(new java.awt.Dimension(80, 21));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOK_actionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setToolTipText("");
        btnCancel.setMaximumSize(new java.awt.Dimension(80, 21));
        btnCancel.setMinimumSize(new java.awt.Dimension(80, 21));
        btnCancel.setPreferredSize(new java.awt.Dimension(80, 21));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel_actionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    /**
     * Close the window on pressing Escape key.
     */
    private void keyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_keyPressed
        if (evt.getKeyCode() == evt.VK_ESCAPE) {
            dlgWindow.dispose();
        }
    }//GEN-LAST:event_keyPressed
    
    /**
     * This is main method which is called by the listener to load the form
     * for Add/Modify.
     *
     * @param mdiForm CoeusAppletMDIForm
     * @param isModal boolean
     */
    public int showForm(CoeusAppletMDIForm mdiForm,boolean isModal){
        //this.schedMinMaintenance = schedMinMaintenance;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        /*check for ESC button if the user presses the button the dialog should close*/
        dlgWindow = new CoeusDlgWindow(mdiForm,"Contingency LookUp",isModal);
        dlgWindow.getContentPane().add(this);
        dlgWindow.setResizable(false);
        //dlgWindow.pack();
        dlgWindow.setSize(700, 350) ;
        Dimension dlgSize = dlgWindow.getSize();
        dlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        screenSize = null;
        dlgWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgWindow.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                tblContingencyDescription.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
                dlgWindow.dispose();
            }
        });
        dlgWindow.show();
        return retValue;
    }
    
    /**
     * displays the message,it gives the error message.
     * @param mesg String
     */
    private static void log(String mesg) {
        CoeusOptionPane.showErrorDialog(mesg);
    }
    
    private void btnCancel_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel_actionPerformed
        retValue = CANCEL;
        dlgWindow.dispose();
    }//GEN-LAST:event_btnCancel_actionPerformed
    
    private void btnOK_actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOK_actionPerformed
        int selectedRowIndex;
        JTable tblLocalContingencyDesc = this.tblContingencyDescription;
        if (tblLocalContingencyDesc.getRowCount() > 0 ) {
            selectedRowIndex = tblLocalContingencyDesc.getSelectedRow();
            if(selectedRowIndex == -1){
                log(coeusMessageResources.parseMessageKey(
                "searchResultWin_exceptionCode.1109"));
                return;
            }
            if(minuteEntryForm != null){
                minuteEntryForm.setContingencyCodeDesc(
                tblLocalContingencyDesc.getValueAt(selectedRowIndex,0).toString(),
                tblLocalContingencyDesc.getValueAt(selectedRowIndex,1).toString());
            }
        }
        retValue = OK;
        dlgWindow.dispose();
    }//GEN-LAST:event_btnOK_actionPerformed
    
    public ComboBoxBean getSelectedBean() {
        int selectedRow = tblContingencyDescription.getSelectedRow();
        ComboBoxBean comboBoxBean = (ComboBoxBean)contingencies.get(selectedRow);
        return comboBoxBean;
    }
    
    public String getDescription(String code) {
        ComboBoxBean comboBoxBean;
        for(int index = 0; index < contingencies.size(); index++) {
            comboBoxBean = (ComboBoxBean)contingencies.get(index);
            if(comboBoxBean.getCode().equalsIgnoreCase(code)) {
                return comboBoxBean.getDescription();
            }
        }
        return null;
    }
    
    public void setSelectedRow(String code) {
        ComboBoxBean comboBoxBean;
        for(int index = 0; index < contingencies.size(); index++) {
            comboBoxBean = (ComboBoxBean)contingencies.get(index);
            if(comboBoxBean.getCode().equalsIgnoreCase(code)) {
                tblContingencyDescription.setRowSelectionInterval(index, index);
                break;
            }
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable tblContingencyDescription;
    // End of variables declaration//GEN-END:variables
    
    // Added by Chandra 20/10/2003 - start
    private class ContigencyCellRenderer extends DefaultTableCellRenderer{
        
        JTextArea txtArCodeDesc;
        JScrollPane jcrPntxtCodeDesc;
        
        ContigencyCellRenderer(){
            txtArCodeDesc = new JTextArea();
            jcrPntxtCodeDesc = new JScrollPane();
            txtArCodeDesc.setLineWrap(true);
            txtArCodeDesc.setEditable(false);
            txtArCodeDesc.setWrapStyleWord(true);
            txtArCodeDesc.setFont(CoeusFontFactory.getNormalFont());
            
        }
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table,
        Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            switch(column){
                case 1:
                    txtArCodeDesc.setText(value.toString().trim());
                    if ( isSelected ) {
                        txtArCodeDesc.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
                        txtArCodeDesc.setForeground(Color.white);
                    }else{
                        txtArCodeDesc.setBackground(Color.white);
                        txtArCodeDesc.setForeground(Color.black);
                    }
            }
            return txtArCodeDesc;
            
        }
        
    }// End of ContigencyCellRenderer class
    //// Added by Chandra 20/10/2003 - End
}
