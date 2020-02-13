/*
 * ProtocolAmendmentRevisionForm.java
 *
 * Created on August 12, 2003, 3:39 PM
 */

package edu.mit.coeus.irb.gui;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.brokers.*;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.JTable;
/**
 *
 * @author  ravikanth
 */
public class ProtocolAmendmentRevisionForm extends edu.mit.coeus.gui.CoeusDlgWindow 
    implements ActionListener {
    
    private String protocolId;
    private CoeusMessageResources messageResources;
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
    private final char ALL_AMEND_AND_RENEWAL = 'L';
    private Vector columnNames;
    private VersionTableModel tableModel;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    /** Creates new form ProtocolAmendmentRevisionForm */
    public ProtocolAmendmentRevisionForm(String protocolNo) {
        super(CoeusGuiConstants.getMDIForm(), "Amendments and Renewals for - " 
            + protocolNo, true);
        this.protocolId = protocolNo;
        this.messageResources = CoeusMessageResources.getInstance();
    }

    public void showForm() throws Exception{
        getContentPane().add( createForm() );
        setResizable(false);
        setValues();
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getSize();
        setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        show();        
    }

    public JComponent createForm(){
        initComponents();

        btnEdit.addActionListener(this);
        btnView.addActionListener(this);
        btnCancel.addActionListener(this);
        tblVersions.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableModel = new VersionTableModel(
                    ((DefaultTableModel)tblVersions.getModel()).getDataVector(),
                    getColumnNames());
        tblVersions.setModel(tableModel);
        
        return pnlMain;
    }
    
    private void setValues() throws Exception{
        if( protocolId != null && protocolId.trim().length() > 0 ) {
            Vector data = getValues();
            if( data != null && data.size() > 0 ) {
                tableModel.setDataBeans(data);
            }else{
                throw new Exception(messageResources.parseMessageKey(
                    "proto_amend_renewal_exceptionCode.33333"));
            }
        }else{
            setColumnWidths();
        }
    }
    
    private Vector getValues() throws Exception{
        RequesterBean request = new RequesterBean();
        request.setFunctionType(ALL_AMEND_AND_RENEWAL );
        request.setId(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response.isSuccessfulResponse()) {
            Vector data = (Vector)response.getDataObjects();
            if( data != null && data.size() > 0 ) {
                return (Vector) data.elementAt( 0 );
            }
        } else {
            throw new Exception(response.getMessage());
        }
        return null;
    }
    
    /**
     * This method is used to return the names of the columns used in the table
     * @return Collection of column names in the table used to show the versions
     * of the amendments and revisions.
     */
    private Vector getColumnNames() {
        if( columnNames == null ) {
            columnNames = new Vector(); 
            Enumeration enumColNames = tblVersions.getColumnModel().getColumns();
            while(enumColNames.hasMoreElements()){
                String strName = (String)((TableColumn)enumColNames.nextElement()).getHeaderValue();
                columnNames.addElement(strName);
            }
        }
        return columnNames;
    
    }
    
    public void actionPerformed( ActionEvent ae ) {
        Object source = ae.getSource();
        if( source.equals( btnCancel ) ) {
            this.dispose();
        }else if ( source.equals( btnEdit ) ){
            showProtocolDetails(CoeusGuiConstants.MODIFY_MODE);
        }else if ( source.equals( btnView ) ){
            showProtocolDetails(CoeusGuiConstants.DISPLAY_MODE);
        }
    }
    private void showProtocolDetails(char fnType ) {
        int selRow = tblVersions.getSelectedRow();
        try{
            if( selRow != -1 ) {
                checkDuplicateAndShow(
                    getModuleCode(tableModel.getSelectedModuleType()),fnType);
            }
        }catch(Exception e) {
            CoeusOptionPane.showInfoDialog(e.getMessage());
        }
    
    }
    
    /**
     * This method is used to check whether the given protocol number is already 
     * opened in the given mode or not. 
     */
    private void checkDuplicateAndShow(int moduleCode, char mode)throws Exception {
        boolean duplicate = false;
        String moduleName = "";
        String refId = "";
        try{
            if( moduleCode == CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE ) {
                moduleName = CoeusGuiConstants.AMENDMENT_DETAILS_TITLE;
            }else if( moduleCode == CoeusGuiConstants.PROTOCOL_RENEWAL_CODE ) {
                moduleName = CoeusGuiConstants.RENEWAL_DETAILS_TITLE;
            }
            refId = protocolId + " Version "+ tableModel.getSelectedVersionNumber();
            duplicate = mdiForm.checkDuplicate(moduleName, refId, mode );
                
        }catch(Exception e){
            /* Exception occured.  Record may be already opened in requested mode
               or if the requested mode is edit mode and application is already
               editing any other record. */
            duplicate = true;
            if(e.getMessage().length() > 0 ) {
                CoeusOptionPane.showInfoDialog(e.getMessage());
            }
            /* try to get the requested frame which is already opened */
            CoeusInternalFrame frame = mdiForm.getFrame(moduleName,refId);
            if(frame == null){
                /* if no frame opened for the requested record then the 
                   requested mode is edit mode. So get the frame of the
                   editing record. */
                frame = mdiForm.getEditingFrame( moduleName );
            }
            if (frame != null){
                this.dispose();
                frame.setSelected(true);
                frame.setVisible(true);
            }
            return;
        }
        ProtocolDetailForm detailForm = null;
        try{
            String protocolNo = constructNewProtocolNumber();
            detailForm = new ProtocolDetailForm(protocolNo, mode, moduleCode);
            this.dispose();
            detailForm.showDialogForm();
        }catch ( Exception ex) {
            ex.printStackTrace();
            try{
                if (!detailForm.isModifiable() ) {
                    String msg = messageResources.parseMessageKey(ex.getMessage());
                    int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
                    if (resultConfirm == 0) {
                        showProtocolDetails(CoeusGuiConstants.DISPLAY_MODE);
                    }
                }else {
                    throw new Exception(ex.getMessage());
                }
            }catch (Exception excep){
                excep.printStackTrace();
                throw new Exception(excep.getMessage());
            }
        }
    }
    
    private int getModuleCode(String protocolNo ) {
        if( protocolNo.indexOf('A') != -1 ) {
            return CoeusGuiConstants.PROTOCOL_AMENDMENT_CODE;
        }else if( protocolNo.indexOf('R') != -1 ){
            return CoeusGuiConstants.PROTOCOL_RENEWAL_CODE;
        }else{
            return CoeusGuiConstants.PROTOCOL_DETAIL_CODE;
        }
    }
    private String constructNewProtocolNumber() {
        StringBuffer sbNewProtocolNo = new StringBuffer(protocolId);
        String versionNo = tableModel.getSelectedVersionNumber();
        String moduleType = tableModel.getSelectedModuleType();
        sbNewProtocolNo.append(moduleType);
        sbNewProtocolNo.append(versionNo);
        return sbNewProtocolNo.toString();
    }
    
    private void setColumnWidths() {
        TableColumn column = tblVersions.getColumnModel().getColumn(0);
        RevisionRenderer revisionRenderer = new RevisionRenderer();
        column.setMaxWidth(0);
        column.setMinWidth(0);
        column.setPreferredWidth(0);
        
        column = tblVersions.getColumnModel().getColumn(1);
        //column.setMaxWidth(100);
        column.setMinWidth(110);
        //column.setPreferredWidth(100);        
        column.setCellRenderer(revisionRenderer);

        column = tblVersions.getColumnModel().getColumn(2);
        //column.setMaxWidth(180);
        column.setMinWidth(200);
        column.setCellRenderer(revisionRenderer);
        //column.setPreferredWidth(180);        
        tblVersions.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblVersions.getTableHeader().setReorderingAllowed(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        scrPnVersions = new javax.swing.JScrollPane();
        tblVersions = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlMain.setLayout(new java.awt.GridBagLayout());

        scrPnVersions.setMinimumSize(new java.awt.Dimension(320, 200));
        scrPnVersions.setPreferredSize(new java.awt.Dimension(320, 200));
        tblVersions.setFont(CoeusFontFactory.getNormalFont());
        tblVersions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Module Type", "Version No.", "Created Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVersions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnVersions.setViewportView(tblVersions);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlMain.add(scrPnVersions, gridBagConstraints);

        btnEdit.setFont(CoeusFontFactory.getLabelFont());
        btnEdit.setMnemonic('E');
        btnEdit.setText("Edit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnEdit, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlMain.add(btnView, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlMain.add(btnCancel, gridBagConstraints);

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    //    new ProtocolAmendmentRevisionForm(new javax.swing.JFrame(), true).show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblVersions;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JButton btnView;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnCancel;
    private javax.swing.JScrollPane scrPnVersions;
    // End of variables declaration//GEN-END:variables

    class VersionTableModel extends DefaultTableModel {
        public VersionTableModel(Vector data, Vector columnNames){
            super(data, columnNames);
        }
        public void setDataBeans(Vector vecBeans){
            Vector data = getData(vecBeans);
            setDataVector(data,columnIdentifiers);
            fireTableDataChanged();
            setColumnWidths();
        }
        protected Vector getData(Vector vecBeans) {
            Vector tableData = new Vector();
            if( vecBeans != null && vecBeans.size() > 0 ) {
                tableData = new Vector();
                int beanCount = vecBeans.size();
                ProtocolInfoBean infoBean = null;
                for(int beanIndex = 0; beanIndex < beanCount; beanIndex++) {
                    infoBean = (ProtocolInfoBean) vecBeans.elementAt(beanIndex);
                    tableData.addElement(constructTableRowData(infoBean));
                }
            }
            return tableData;
        }
        
        protected Vector constructTableRowData(ProtocolInfoBean infoBean ) {
            Vector rowData = new Vector();
            String versionNo = "",moduleType = "", createdTimestamp = "";
            String protocolNo = null;
            if( infoBean != null ) {
                protocolNo = infoBean.getProtocolNumber();
                if( protocolNo.length() == 14 ) {
                    versionNo = protocolNo.substring(11); 
                    moduleType = ""+protocolNo.charAt(10);
                }
                createdTimestamp = CoeusDateFormat.format(
                            infoBean.getCreateTimestamp().toString() );                
                rowData.addElement(moduleType);
                rowData.addElement(versionNo);
                rowData.addElement(createdTimestamp);
            }
            return rowData;
        }
        public String getSelectedModuleType(){
            int selRow = tblVersions.getSelectedRow();
            if( selRow != -1 && dataVector != null ){
                return (String) getValueAt(selRow,0);
            }
            return "";
        }
        
        public String getSelectedVersionNumber(){
            int selRow = tblVersions.getSelectedRow();
            if( selRow != -1 && dataVector != null ){
                return (String) getValueAt(selRow,1);
            }
            return "";
        }
        
    }
    
 class RevisionRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable table,
     Object value, boolean isSelected, boolean hasFocus, int row,
     int column) {
         setText((String)value);
         String val = (String)table.getValueAt(row,0);
         if( val != null ){
             
             if( val.equals("R") ) {
                 setForeground(Color.pink);
             }else {
                 setForeground(Color.black);
             }
         }
         if( table.isRowSelected(row) ) {
            setBackground((Color)javax.swing.UIManager.getDefaults().get(
                "Table.selectionBackground"));
            if( getForeground().equals( Color.black ) ) {
                setForeground( Color.white );
            }
         }else{
            setBackground(Color.white);
            if( getForeground().equals( Color.white ) ) {
                setForeground( Color.black );
            }
         }
         setFont(CoeusFontFactory.getNormalFont());
         return this;
     }
 }
    
    
}
