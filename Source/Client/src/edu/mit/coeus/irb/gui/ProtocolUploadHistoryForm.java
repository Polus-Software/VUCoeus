/*
 * @(#)ProtocolUploadDocumentForm.java  8/31/06
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 21-DEC-2010
 * by Bharati Umarani
 */
package edu.mit.coeus.irb.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.ProtocolInfoBean;
import edu.mit.coeus.irb.bean.UploadDocumentBean;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Class for Protocol History Dialog
 * @author tarique
 */
public class ProtocolUploadHistoryForm extends javax.swing.JComponent
        implements ActionListener, MouseListener, ListSelectionListener {
    //Modified for Coeus4.3 enhancement - starts
    private static final int WIDTH = 770;
    private static final int HEIGHT = 235;
    //Modified for Coeus4.3 enhancement - ends
    
    private static final String EMPTY_STRING = "";
    private static final int TYPE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    //Commented for Coeus 4.3 enhancement to remove the status column
    //private static final int STATUS_COLUMN = 2;
    private static final int LAST_UPDATED_COLUMN = 2;
    private static final int UPDATED_BY_COLUMN = 3;
    private ProtocolUploadHistoryTableModel protocolUploadHistoryTableModel;
    private ProtocolUploadHistoryRenderer protocolUploadHistoryRenderer;
    private Vector vecHistoryData;
    private CoeusDlgWindow dlgUploadHistory;
    //Modified for Coeus4.3 enhancement
    //private static final String TITLE = "History for ";
    private static final String TITLE = "Attachment History ";
    private ProtocolInfoBean protocolInfoBean;
    private CoeusMessageResources coeusMessageResources;
    private Component parentWindow;
    //Commented for case 3552 - IRB attachments - start
    //private String baseWindowTitle;
    //Commented for case 3552 - IRB attachments - end
    
    //Modified the method signature for case 3552 - IRB attachments - start
    //Remove the argument baseWindowTitle
    /**
     * Creates new form ProtocolUploadHistoryForm
     * @param protocolInfoBean contain protocol Information
     * @throws CoeusException if any exception occurs
     */
    public ProtocolUploadHistoryForm(ProtocolInfoBean protocolInfoBean) throws CoeusException {
//        public ProtocolUploadHistoryForm(ProtocolInfoBean protocolInfoBean
//            , String baseWindowTitle) throws CoeusException {
//        
        this.protocolInfoBean = protocolInfoBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        pnlProtocolHistory.setBorder(new javax.swing.border.TitledBorder(null,
                "History of Attachment",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                CoeusFontFactory.getLabelFont()));
        registerComponents();
        setTableEditors();
        postInitComponents();
    }
    /**
     * Method to register table Model and Listener
     * @throws CoeusException If Any exception occurs
     */
    private void registerComponents() throws CoeusException{
        btnView.addActionListener(this);
        btnClose.addActionListener(this);
        //Commented for case 3552 - IRB attachments - start
        //tblProtocolHistory.addMouseListener(this);
        //Commented for case 3552 - IRB attachments - end
        protocolUploadHistoryTableModel = new ProtocolUploadHistoryTableModel();
        protocolUploadHistoryRenderer = new ProtocolUploadHistoryRenderer();
        tblProtocolHistory.setModel(protocolUploadHistoryTableModel);
        tblProtocolHistory.getSelectionModel().addListSelectionListener(this);
        //Added for the Case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-start
        tblProtocolHistory.getTableHeader().addMouseListener(this);
        //Added for the Case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-end
        java.awt.Component[] components = {btnView, btnClose };
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
    }
    /**
     * set The protocol Document editor and headers
     * @throws CoeusException If Any exception occurs
     */
    private void setTableEditors() throws CoeusException{
        JTableHeader tableHeader = tblProtocolHistory.getTableHeader();
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
        tableHeader.addMouseListener(new ColumnHeaderListener());
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100,25));
        tableHeader.setMinimumSize(new Dimension(100,25));
        tableHeader.setPreferredSize(new Dimension(100,25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblProtocolHistory.setRowHeight(22);
        tblProtocolHistory.setShowHorizontalLines(true);
        tblProtocolHistory.setShowVerticalLines(true);
        tblProtocolHistory.setOpaque(false);
        tblProtocolHistory.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        tblProtocolHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn columnDetails;
        //Modified for Coeus4.3 enhancement - start
        //Removed the status column, changed the table size
//            int []size = {120,200,80,120,90};
//            for(int index = 0;index < size.length;index++){
//                columnDetails = tblProtocolHistory.getColumnModel().getColumn(index);
//                columnDetails.setPreferredWidth(size[index]);
//                if(index == 0){
//                    columnDetails.setMinWidth(40);
//                }else if(index == 1){
//                    columnDetails.setMinWidth(75);
//                }else if(index == 2){
//                    columnDetails.setMinWidth(55);
//                }else{
//                    columnDetails.setMinWidth(80);
//                }
//                columnDetails.setCellRenderer(protocolUploadHistoryRenderer);
//            }
        int []size = {110,220,130,180};
        for(int index = 0; index < size.length; index ++) {
            columnDetails = tblProtocolHistory.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            if(index == 0) {
                columnDetails.setMinWidth(110);
            }else if(index == 1) {
                columnDetails.setMinWidth(200);
            }else if(index == 2) {
                columnDetails.setMinWidth(120);
            }else if(index == 3) {
                columnDetails.setMinWidth(95);
            }
            columnDetails.setCellRenderer(protocolUploadHistoryRenderer);
        }
        //Modified for Coeus4.3 enhancement - end
    }
    /**
     * Method to set the form data
     * @param data contains form data
     * @throws CoeusException If Any exception occurs
     */
    public void setFormData(Object data) throws CoeusException{
        try{
            vecHistoryData = (Vector) data;
            CoeusVector cvSortData = new CoeusVector();
            cvSortData.addAll(vecHistoryData);
            String []fieldName = {"sequenceNumber", "docCode","versionNumber"};
            cvSortData.sort(fieldName, false);
            vecHistoryData.removeAllElements();
            for(int index = 0; index < cvSortData.size() ; index ++) {
                UploadDocumentBean uploadDocumentBean
                        = (UploadDocumentBean)cvSortData.get(index);
                vecHistoryData.add(index,uploadDocumentBean);
            }
            
        }catch(Exception exp) {
            exp.printStackTrace();
            throw new CoeusException(exp.getMessage(),0);
        }
        
        protocolUploadHistoryTableModel.setData(vecHistoryData);
        if(tblProtocolHistory.getRowCount() > 0){
            tblProtocolHistory.setRowSelectionInterval(0,0);
            tblProtocolHistory.setColumnSelectionInterval(0,0);
        }
    }
    /**
     * method to set default focus based on open window
     * @throws CoeusException If Any exception occurs
     */
    private void requestDefaultFocusToComp() throws CoeusException{
        btnView.requestFocusInWindow();
    }
    /**
     * Method to initialise dialog
     * @throws CoeusException If Any exception occurs
     */
    private void postInitComponents() throws CoeusException{
        dlgUploadHistory = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm());
        dlgUploadHistory.setResizable(false);
        dlgUploadHistory.setModal(true);
        dlgUploadHistory.getContentPane().add(this);
        dlgUploadHistory.setFont(CoeusFontFactory.getLabelFont());
        dlgUploadHistory.setSize(WIDTH, HEIGHT);
        //dlgUploadHistory.setTitle(TITLE+baseWindowTitle);
        dlgUploadHistory.setTitle(TITLE);
        dlgUploadHistory.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgUploadHistory.getSize();
        dlgUploadHistory.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgUploadHistory.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                try{
                    requestDefaultFocusToComp();
                }catch(CoeusException ce){
                    ce.printStackTrace();
                }
            }
        });
        dlgUploadHistory.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                try{
                    performCloseAction();
                }catch(CoeusException ce){
                    ce.printStackTrace();
                }
            }
        });
        
        dlgUploadHistory.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    performCloseAction();
                }catch(CoeusException ce){
                    ce.printStackTrace();
                }
            }
        });
    }
    /**
     * method to display dialog
     * @throws CoeusException If Any exception occurs
     */
    public void display() throws CoeusException{
        dlgUploadHistory.setVisible(true);
    }
    /**
     * method to perform action on close button
     * @throws CoeusException If Any exception occurs
     */
    private void performCloseAction() throws CoeusException {
        dlgUploadHistory.dispose();
    }
    /**
     * method to perform action on close button
     * @throws CoeusException If Any exception occurs
     */
    private void performViewAction() throws CoeusException {
        int rowCount = tblProtocolHistory.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
            return;
        }
        int selectedRow = tblProtocolHistory.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vecHistoryData.get(selectedRow);
        ((ProtocolUploadDocumentForm)getParentWindow()).viewDocument(uploadDocumentBean);
    }
    /**
     * method to clean up when window dispose.
     * @throws CoeusException If Any exception occurs
     */
    public void cleanup() throws CoeusException{
        btnClose.removeActionListener(this);
        btnView.removeActionListener(this);
        tblProtocolHistory.removeMouseListener(this);
        protocolUploadHistoryRenderer = null;
        protocolUploadHistoryTableModel = null;
        dlgUploadHistory = null;
    }
    /**
     * Table Model for Protocol History Document
     */
    private class ProtocolUploadHistoryTableModel extends AbstractTableModel{
        //Modified for Coeus 4.3 enhancement - start
        //Removed the status column
        //private String []colNames = {"Type","Description","Status","Last Updated","Updated By"};
        private String []colNames = {"Type","Description","Last Updated","Updated By"};
        private Class colClass[]={String.class,String.class,String.class, String.class};
        //Modified for Coeus 4.3 enhancement - end
        private Vector vecHistoryListData;
        /**
         * method to make cell editable
         * @param row disable this row
         * @param col disable this column
         * @return boolean for cell editable state
         */
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * method to count total column
         * @return number of column
         */
        public int getColumnCount() {
            return colNames.length;
        }
        /**
         * Method to get column name
         * @param col column number to get name
         * @return name of the column
         */
        public String getColumnName(int col){
            return colNames[col];
        }
        /**
         * method to get column class
         * @param col number of column
         * @return Class of column
         */
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * method to get total rows
         * @return total number of rows
         */
        public int getRowCount() {
            if(vecHistoryListData == null || vecHistoryListData.size() == 0){
                return 0;
            }
            return vecHistoryListData.size();
            
        }
        /**
         * method to set data in table
         * @param vecHistoryListData set data for history table
         */
        public void setData(Vector vecHistoryListData){
            this.vecHistoryListData = vecHistoryListData;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecHistoryListData != null && vecHistoryListData.size() > 0){
                UploadDocumentBean uploadDocBean
                        = (UploadDocumentBean)vecHistoryListData.get(rowIndex);
                switch(columnIndex){
                    case TYPE_COLUMN:
                        return uploadDocBean.getDocType();
                    case DESCRIPTION_COLUMN:
                        return uploadDocBean.getDescription();
                        //Modified for Coeus 4.3 enhancement - start
                        //Removed the status column
                        //case STATUS_COLUMN:
                        //return uploadDocBean.getStatusDescription();
                        //Modified for Coeus 4.3 enhancement - end
                    case LAST_UPDATED_COLUMN:
                        //Modified for COEUSDEV-322 :  Premium - Protocol attachments - Delete Document line when a Document was removed resulting in no document being stored - Start
                        //Display date in 12 hrs format with AM/PM
//                        return uploadDocBean.getUpdateTimestamp().toString();
                        return CoeusDateFormat.format(uploadDocBean.getUpdateTimestamp().toString());
                        //COEUSDEV-322 : End
                    case UPDATED_BY_COLUMN:
//                        return uploadDocBean.getUpdateUser();
                        /*
                         * UserID to UserName Enhancement - Start
                         * Added new property getUpdateUserName to get username
                         */
                        return uploadDocBean.getUpdateUserName();
                        // UserId to UserName Enhancement - End
                }
            }
            return EMPTY_STRING;
        }
    }
    /**
     * Table Renderer for Protocol History Document
     */
    private class ProtocolUploadHistoryRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        
        /**
         * contrutor for Parent document renderer
         */
        public ProtocolUploadHistoryRenderer(){
            lblComponent = new CoeusLabel();
            lblComponent.setOpaque(true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            
        }
        /**
         * Method to get table cell component
         * @param table table object
         * @param value component value
         * @param isSelected component state
         * @param hasFocus component focus
         * @param row on which row exist
         * @param col on which column exist
         * @return object component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col){
            switch (col){
                case TYPE_COLUMN:
                case DESCRIPTION_COLUMN:
                //Commented for Coeus 4.3 enhancement
                //Removed status column
                //case STATUS_COLUMN:
                case UPDATED_BY_COLUMN:
                case LAST_UPDATED_COLUMN:
                    lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
                    if(col == LAST_UPDATED_COLUMN){
                        lblComponent.setHorizontalAlignment(CoeusLabel.RIGHT);
                    }
                    lblComponent.setFont(CoeusFontFactory.getNormalFont());
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value = (value == null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                    
            }
            return lblComponent;
        }
    }
    /**
     * Action on Components
     * @param actionEvent event on components
     */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            dlgUploadHistory.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
            if(source.equals(btnClose) ) {
                performCloseAction();
            }else if(source.equals(btnView )){
                performViewAction();
            }
        }catch(CoeusException ce){
            ce.printStackTrace();
        }finally {
            dlgUploadHistory.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    /**
     * Getter for property parentWindow.
     * @return Value of property parentWindow.
     */
    public java.awt.Component getParentWindow() {
        return parentWindow;
    }
    
    /**
     * Setter for property parentWindow.
     * @param parentWindow New value of property parentWindow.
     */
    public void setParentWindow(java.awt.Component parentWindow) {
        this.parentWindow = parentWindow;
    }
    
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        //Not used, so commented for the case # COEUSQA-2353 - Need to a better way to handle large numbers of protocol attachments
//        if( mouseEvent.getClickCount() == 2 ){
//            int row;
//            if(mouseEvent.getSource().equals(tblProtocolHistory)) {
//                row = tblProtocolHistory.rowAtPoint(mouseEvent.getPoint());
//            }else {
//                row = tblProtocolHistory.getSelectedRow();
//            }
//            try{
//                UploadDocumentBean uploadDocumentBean = (UploadDocumentBean)vecHistoryData.get(row);
//                ((ProtocolUploadDocumentForm)getParentWindow()).viewDocument(uploadDocumentBean);
//            }catch( CoeusException ce ) {
//                ce.printStackTrace();
//            }
//        }
    }
    /**
     * Method to get mouse entered
     * @param e event for mouse entered
     */
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }
    /**
     * Method to perform action when mouse exit
     * @param e event for mouse exited
     */
    public void mouseExited(java.awt.event.MouseEvent e) {
    }
    /**
     * method to perform mouse event when mouse pressed
     * @param e event for mouse press
     */
    public void mousePressed(java.awt.event.MouseEvent e) {
    }
    /**
     * Method to get action when mouse released
     * @param e event for mouse released
     */
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }
    
    /**
     * COEUSQA-2431 : Amendment Attachment View Error -start
     *  if rows are present whether it is deleted attachment or finalized attachment
     *  enable the view button
     */
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource().equals(tblProtocolHistory.getSelectionModel())){
            if(tblProtocolHistory.getSelectedRow()!=-1){
                UploadDocumentBean documentBean =
                        (UploadDocumentBean)vecHistoryData.get(tblProtocolHistory.getSelectedRow());
                         btnView.setEnabled(true); //COEUSQA-2431 -end
//                if(documentBean.getStatusCode()==3){
//                    btnView.setEnabled(false);
//                }else{
//                    btnView.setEnabled(true);
//                }
            }
        }
    }
     /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionnaireId, name and description
     */
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
    public class ColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={
            {"0","docType" },
            {"1","description" },
            {"2","updateTimestamp"},
            {"3","updateUserName"},
        };
        boolean sort =true;
        /**
         * @param evt
         */
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                JTable table = ((JTableHeader)mouseEvent.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(mouseEvent.getX());
                CoeusVector cvListData =  new CoeusVector();
                if(vecHistoryData != null && vecHistoryData.size() > 0){
                    for(int index=0; index<vecHistoryData.size();index++){
                        cvListData.add(vecHistoryData.get(index));
                    }
                }
                if(cvListData!=null && cvListData.size()>0 &&
                        nameBeanId [vColIndex][1].length() >1 ){
                    (cvListData).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    
                    if(cvListData != null && cvListData.size() > 0){
                        for(int index=0; index<cvListData.size();index++){
                            vecHistoryData.set(index,cvListData.get(index));
                        }
                    }
                    protocolUploadHistoryTableModel.fireTableRowsUpdated(0, protocolUploadHistoryTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
        
    }// End of ColumnHeaderListener.................
     //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlProtocolHistory = new javax.swing.JPanel();
        scrPnProtocolHistory = new javax.swing.JScrollPane();
        tblProtocolHistory = new javax.swing.JTable();
        btnView = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlProtocolHistory.setLayout(new java.awt.GridBagLayout());

        pnlProtocolHistory.setMinimumSize(new java.awt.Dimension(663, 187));
        pnlProtocolHistory.setPreferredSize(new java.awt.Dimension(683, 187));
        scrPnProtocolHistory.setAutoscrolls(true);
        scrPnProtocolHistory.setMaximumSize(new java.awt.Dimension(645, 160));
        scrPnProtocolHistory.setMinimumSize(new java.awt.Dimension(645, 160));
        scrPnProtocolHistory.setPreferredSize(new java.awt.Dimension(670, 160));
        tblProtocolHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        scrPnProtocolHistory.setViewportView(tblProtocolHistory);

        pnlProtocolHistory.add(scrPnProtocolHistory, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        add(pnlProtocolHistory, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setText("View");
        btnView.setMaximumSize(new java.awt.Dimension(70, 22));
        btnView.setMinimumSize(new java.awt.Dimension(70, 22));
        btnView.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 3, 3);
        add(btnView, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(70, 22));
        btnClose.setMinimumSize(new java.awt.Dimension(70, 22));
        btnClose.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnView;
    private javax.swing.JPanel pnlProtocolHistory;
    private javax.swing.JScrollPane scrPnProtocolHistory;
    private javax.swing.JTable tblProtocolHistory;
    // End of variables declaration//GEN-END:variables
    
}
