/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * ScheduleAttachmentForm.java
 *
 * Created on November 24, 2011, 2:46 PM
 */

/* PMD check performed, and commented unused imports and variables on 21-MARCH-2012
 * by Bharati Umarani
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.iacuc.bean.ScheduleAttachmentBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.awt.Component;
//import java.awt.Cursor;
import java.awt.Dimension;
//import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
 *
 * @author  manjunathabn
 */
public class ScheduleAttachmentsForm extends javax.swing.JComponent
        implements ActionListener, MouseListener, ListSelectionListener {
    private static final String EMPTY_STRING = "";
    private static final int TYPE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int LAST_UPDATED_COLUMN = 2;
    private static final int UPDATED_BY_COLUMN = 3;
    private static final String PROTOCOL_SERVLET = "/scheduleMaintSrvlt";
    private static final String connectTo
            = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SERVLET;
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final char GET_DOCUMENT_TYPE = 'b';
    private static final char IACUC_UPLOAD_DOC_DATA = 'K';
    private static final char IACUC_ADD_UPD_DEL_SCHD_ATTACH = 'l';      
    private static final char MODIFY_DOCUMENT = 'M';
    private static final char CHANGE_STATUS = 'S';
    private static final char ADD_DOCUMENT = 'A';
    private Vector vecListData;
    private ScheduleAttachmentRenderer scheduleAttachmentRenderer;
    private ScheduleAttachmentTableModel scheduleAttachmentTableModel;
    private char functionType;
    private CoeusMessageResources coeusMessageResources;
    private boolean fileSelected;
    private AppletServletCommunicator comm;
    private RequesterBean requester;
    private ResponderBean responder;
    private byte[] blobData;
    private String windowType;
    private CoeusVector cvDocType;
    private ScheduleAttachmentBean scheduleAttachmentBean;
    private static final String DOC_TYPE = "docType";
    private static final String DESCRIPTION = "description";
    private static final String UPDATE_TIMESTAMP = "updateTimestamp";
    private static final String UPDATE_USER = "updateUserName";    
    private static final int COLUMN_TYPE = 110;
    private static final int COLUMN_DESCRIPTION = 270;
    private static final int COLUMN_LAST_UPDATED = 130;
    private static final int COLUMN_USER = 180;
    //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open - start
    private static final char HAS_IRB_IACUC_ADMIN_RIGHTS = '2';
    //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open - end
    
    /**
     * Creates new form ScheduleAttachmentsForm
     * @param scheduleAttachmentBean 
     * @param functionType 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    public ScheduleAttachmentsForm(ScheduleAttachmentBean scheduleAttachmentBean,
            char functionType) throws CoeusException {     
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        this.scheduleAttachmentBean = scheduleAttachmentBean;
        this.functionType = functionType;
        setFunctionType(functionType);
        registerComponents();
        setFormData();
    }
    /**
     * Method to register table Model and Listener
     * @throws CoeusException If Any exception occurs
     */
    private void registerComponents() throws CoeusException {
        btnDelete.addActionListener(this);
        btnView.addActionListener(this);    
        btnchangeStatus.addActionListener(this);
        btnModify.addActionListener(this);
        btnAdd.addActionListener(this);
        scheduleAttachmentTableModel = new ScheduleAttachmentTableModel();
        scheduleAttachmentRenderer = new ScheduleAttachmentRenderer();
        tblListDocument.setModel(scheduleAttachmentTableModel);
        tblListDocument.getSelectionModel().addListSelectionListener(this);
        tblListDocument.getTableHeader().addMouseListener(this);
    }
    /**
     * set The SChedule Attachment editor and headers
     * @throws CoeusException If Any exception occurs
     */
    private void setTableEditors() throws CoeusException {
        JTableHeader tableHeader = tblListDocument.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100, 25));
        tableHeader.setMinimumSize(new Dimension(100, 25));
        tableHeader.setPreferredSize(new Dimension(100, 25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblListDocument.setRowHeight(22);
        tblListDocument.setShowHorizontalLines(true);
        tblListDocument.setShowVerticalLines(true);
        tblListDocument.setOpaque(false);
        tblListDocument.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        tblListDocument.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);       
        TableColumn columnDetails;
        int []size = {110,270,130,180};
        for(int index = 0; index < size.length; index++) {
            columnDetails = tblListDocument.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            if(index == 0) {
                columnDetails.setMinWidth(COLUMN_TYPE);
            }else if(index == 1) {
                columnDetails.setMinWidth(COLUMN_DESCRIPTION);               
            }else if(index == 2) {
                columnDetails.setMinWidth(COLUMN_LAST_UPDATED);
            }else {
                columnDetails.setMinWidth(COLUMN_USER);
            }
            columnDetails.setCellRenderer(scheduleAttachmentRenderer);
        }
    }   
    
    /**
     * Method to set the form data
     * @throws CoeusException If Any exception occurs
     */
    public void setFormData() throws CoeusException {
        cvDocType = (CoeusVector)getDocumentData('b');
        //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open - start
        //Get the admin rights for logged in user
        boolean hasIRBAdminRights = false;
        boolean isMember = false;
        Vector  vecValues = getAdminRights(HAS_IRB_IACUC_ADMIN_RIGHTS);
        hasIRBAdminRights = (Boolean)vecValues.get(0);
        isMember = (Boolean)vecValues.get(1);
        if(isMember){
            btnView.setEnabled(true);
        }else{
            btnView.setEnabled(false);
        }
        if(hasIRBAdminRights){
            btnAdd.setEnabled(true);
            btnModify.setEnabled(true);
            btnDelete.setEnabled(true);
            btnView.setEnabled(true);
        }else{
            btnAdd.setEnabled(false);
            btnModify.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open  - end
        String scheduleId = null;
        if(scheduleAttachmentBean != null){
            scheduleId = scheduleAttachmentBean.getScheduleId();
        }
        setTableEditors();
        ScreenFocusTraversalPolicy traversePolicy;
        java.awt.Component[] components = { btnAdd, btnModify, btnDelete,
        btnView
        };
        
        traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
        refereshData(false);
    }
       /**
     * Populates the original protocol document table
     */
    public void setScheduleAttachmentsData() throws CoeusException{        
        if(scheduleAttachmentBean!=null ){
            vecListData = (Vector) getDocumentData(IACUC_UPLOAD_DOC_DATA);           
            scheduleAttachmentTableModel.setData(vecListData);
            scheduleAttachmentTableModel.fireTableDataChanged();
            if (tblListDocument.getRowCount() > 0) {
                tblListDocument.setRowSelectionInterval(0,0);
                tblListDocument.setColumnSelectionInterval(0,0);
            }
        }
    }    
      
    /**
     * Method to Refresh the form
     *
     * @param isUpdated 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    private void refereshData(boolean isUpdated) throws CoeusException {
        if (isUpdated) {
            cvDocType = (CoeusVector) getDocumentData(GET_DOCUMENT_TYPE);
        }
        setScheduleAttachmentsData();          
    }        
    
    /**
     * method to perform delete button operation
     * @throws CoeusException If Any exception occurs
     */
    private void performDeleteAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1000"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1001"));
            return;
        }
        ScheduleAttachmentBean scheduleAttachmentBean = (ScheduleAttachmentBean)vecListData.get(selectedRow);
        
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1002"),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES){
            
            scheduleAttachmentBean.setAcType(TypeConstants.DELETE_RECORD);            
            boolean isSuccess = addUpdDocument(scheduleAttachmentBean, true, false);
            if(isSuccess){
                refereshData(true);
            }
        }
    }
    /**
     * method to perform view button operation
     * @throws CoeusException If Any exception occurs
     */
    private void performViewAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
            return;
        }
        ScheduleAttachmentBean scheduleAttachmentBean
                = (ScheduleAttachmentBean)vecListData.get(selectedRow);
        viewDocument(scheduleAttachmentBean);
    }
   
    /**
     * Mehtod to perform the modify of the selected document
     * @throws CoeusException if any exception occurs
     */
    public void performModifyAction() throws CoeusException{
        int rowCount = tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1012"));
            return;
        }
        int selectedRow = tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
            return;
        }
        
        if(checkDocumentTypeExists()){
            ScheduleAttachmentBean scheduleAttachmentBean
                    = (ScheduleAttachmentBean)vecListData.get(selectedRow);
            ScheduleAttachUploadModifyForm scheduleAttachUploadModifyForm =
                    new ScheduleAttachUploadModifyForm(scheduleAttachmentBean,MODIFY_DOCUMENT);
            
            scheduleAttachUploadModifyForm.setCvDocType(cvDocType);
            scheduleAttachUploadModifyForm.setTblDocuments(tblListDocument);
            scheduleAttachUploadModifyForm.showWindow();
            refereshData(true);
        }
    }
    /**
     * Method performs addiction action
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    public void performAddAction() throws CoeusException{      
        ScheduleAttachUploadModifyForm scheduleAttachUploadModifyForm =
                new ScheduleAttachUploadModifyForm(scheduleAttachmentBean,ADD_DOCUMENT);
        if(cvDocType != null && !cvDocType.isEmpty()) {
            scheduleAttachUploadModifyForm.setCvDocType(cvDocType);
            scheduleAttachUploadModifyForm.showWindow();
            refereshData(true);
        } else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("scheduleAttachType_MessageCode.1031"));
        }
    }    
   
    /**
     * Method to add or update or delete data
     * @param scheduleDetailsBean 
     * @param newVersion 
     * @param newDocumentId 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return 
     */
    private boolean addUpdDocument(ScheduleAttachmentBean scheduleAttachmentBean,
            boolean newVersion, boolean newDocumentId)
            throws CoeusException{
        requester = new RequesterBean();
        requester.setFunctionType(IACUC_ADD_UPD_DEL_SCHD_ATTACH);
        Vector vecServerObjects = new Vector();
        vecServerObjects.add(0,scheduleAttachmentBean);
        requester.setDataObjects(vecServerObjects);
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        return responder.isSuccessfulResponse();
    }
  
    
    /**
     * Method to get the document url with file contents
     * @param scheduleDetailsBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return 
     */
    public String getDocumentURL(ScheduleAttachmentBean scheduleAttachmentBean) throws CoeusException{
        String templateUrl =  null;
        requester = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("SCHEDELE_DETAILS_BEAN", scheduleAttachmentBean);
        map.put("DOCUMENT_TYPE", "SCHEDULE_ATTACHMENT_DOC");
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requester.setDataObject(documentBean);
        requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        comm = new AppletServletCommunicator(STREMING_SERVLET, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        map = (Map)responder.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
        return templateUrl;
    }
    
    /**
     * Method to get the document url with file contents
     * @param scheduleDetailsBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     */
    public void viewDocument(ScheduleAttachmentBean scheduleAttachmentBean) throws CoeusException{
        String url = getDocumentURL(scheduleAttachmentBean);
        if(url == null || url.trim().length() == 0 ) {
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
            return;
        }
        url = url.replace('\\', '/') ;
        try{
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
        }catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();           
        }catch( Exception ue) {
            ue.printStackTrace() ;
        }
    }
    /**
     * Method to perform action on components
     * @param event event on components
     */
    public void actionPerformed(java.awt.event.ActionEvent event) {
        Object source= event. getSource();
        try{
            setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
            if(source.equals(btnDelete)) {
                performDeleteAction();
            }else if(source.equals(btnView) ) {
                performViewAction();
            }else if(source.equals(btnModify)){
                performModifyAction();
            }else if(source.equals(btnAdd)){
                performAddAction();
            }
        }catch(CoeusException ce){
            CoeusOptionPane.showErrorDialog(ce.getMessage());
            ce.printStackTrace();
        }finally{
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * Class for Schedule Attachment Table Model
     */
    private class ScheduleAttachmentTableModel extends AbstractTableModel{
        private String [] colNames = {"Type","Description","Last Updated","Updated By"};
        private Class colClass[]={String.class,String.class,String.class,String.class};
        private Vector vecListData;
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
            if(vecListData == null || vecListData.size() == 0){
                return 0;
            }
            return vecListData.size();
            
        }
        /**
         * method to set data in table
         * @param vecListData contains data for table
         */
        public void setData(Vector vecListData){
            this.vecListData = vecListData;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecListData != null && vecListData.size() > 0){
                ScheduleAttachmentBean scheduleAttachmentBean
                        = (ScheduleAttachmentBean)vecListData.get(rowIndex);
                switch(columnIndex){
                    case TYPE_COLUMN:
                        return scheduleAttachmentBean.getAttachmentType();
                    case DESCRIPTION_COLUMN:
                        return scheduleAttachmentBean.getDescription();
                    case LAST_UPDATED_COLUMN:
                        return CoeusDateFormat.format(scheduleAttachmentBean.getUpdateTimestamp().toString());
                    case UPDATED_BY_COLUMN:
                        return scheduleAttachmentBean.getUpdateUser();
                }
            }
            return EMPTY_STRING;
        }
    }
    /**
     * Class for Schedule Attachment renderer
     */
    private class ScheduleAttachmentRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        
        /**
         * contructor for Schedule Attachment renderer
         */
        public ScheduleAttachmentRenderer(){
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
                case UPDATED_BY_COLUMN:
                case LAST_UPDATED_COLUMN:
                    lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
                    if(col == LAST_UPDATED_COLUMN ){
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
                    value = (value==null ? EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                    
            }
            return lblComponent;
        }
    }    
    
    /**
     * Get the Document Data based on data Type
     * 
     * @return List containg data
     * @param getDataType data type for getting data or added data
     * @throws CoeusException If Any exception occurs
     */    
    public List getDocumentData(char getDataType)
    throws CoeusException {
        List cvParam = null;
        requester = new RequesterBean();
        if(getDataType == IACUC_UPLOAD_DOC_DATA){
            requester.setDataObject(scheduleAttachmentBean);
        }
        requester.setFunctionType(getDataType);
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        if(getDataType == GET_DOCUMENT_TYPE){
            cvParam = (CoeusVector)responder.getDataObject();
        }else if(getDataType == IACUC_UPLOAD_DOC_DATA){
            cvParam = (Vector)responder.getDataObject();
        }
        return cvParam;
    }
    
    //Added forCOEUSDEV-1057 : IRB Schedule Attachments window fails to open - start
    /**
     * Get the admin rights PERFORM_IRB_ACTIONS_ON_PROTO for loggedin user
     * @return boolean hasIRBAdminRights
     * @param getDataType data type for getting data from server
     */
    private Vector getAdminRights(char getDataType){
        Vector vecValues = new Vector();
        Vector vecScheduleData = new Vector();
        int commTypeCode  = CoeusConstants.IACUC_COMMITTEE_TYPE_CODE;
        boolean hasIRBAdminRights = false;
        requester = new RequesterBean();
        vecScheduleData.add(scheduleAttachmentBean);
        vecScheduleData.add(commTypeCode);
        requester.setFunctionType(getDataType);
        requester.setDataObjects(vecScheduleData);
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(responder.isSuccessfulResponse()){
            vecValues = (Vector)responder.getDataObjects();
        }
        return vecValues;
        
    }
    //Added for COEUSDEV-1057 : IRB Schedule Attachments window fails to open - end
    
    /**
     * Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Getter for property fileSelected.
     * @return Value of property fileSelected.
     */
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    /**
     * Setter for property fileSelected.
     * @param fileSelected New value of property fileSelected.
     */
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    /**
     * Getter for property blobData.
     * @return Value of property blobData.
     */
    public byte[] getBlobData() {
        return this.blobData;
    }
    
    /**
     * Setter for property blobData.
     * @param blobData New value of property blobData.
     */
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    /**
     * Getter for property windowType.
     * @return Value of property windowType.
     */
    public java.lang.String getWindowType() {
        return windowType;
    }
    
    /**
     * Setter for property windowType.
     * @param windowType New value of property windowType.
     */
    public void setWindowType(java.lang.String windowType) {
        this.windowType = windowType;
    }
     
    /**
     * Mouse click event
     * @param mouseEvent event for mouse clicked
     */
    public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {    
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
     * Coeus 4.3 enhancement
     * Disables the change status button according to the status of the document
     * and the protocol selected in table
     */
    public void valueChanged(ListSelectionEvent evt){
        if(evt.getSource().equals(tblListDocument.getSelectionModel())){
            if(tblListDocument.getSelectedRow()!=-1){
                ScheduleAttachmentBean scheduleAttachmentBean =
                        (ScheduleAttachmentBean)vecListData.get(tblListDocument.getSelectedRow());
            }
        }
    }
      
    /**
     * Shows warning message if the not document types available
     * @return boolean
     */
    public boolean checkDocumentTypeExists(){
        if(cvDocType == null || cvDocType.size() == 0){
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1008"));
                return false;
        }
        return true;
    }

    /**
     * This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={
            {"0", DOC_TYPE },
            {"1", DESCRIPTION },
            {"2", UPDATE_TIMESTAMP},
            {"3", UPDATE_USER},
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
                if(vecListData != null && vecListData.size() > 0){
                    for(int index=0; index<vecListData.size();index++){
                        cvListData.add(vecListData.get(index));
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
                            vecListData.set(index,cvListData.get(index));
                        }
                    }
                    scheduleAttachmentTableModel.fireTableRowsUpdated(0, scheduleAttachmentTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
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

        btnchangeStatus = new javax.swing.JButton();
        pnlUploadDocuments = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        pnlListDocument = new javax.swing.JPanel();
        scrPnListDocument = new javax.swing.JScrollPane();
        tblListDocument = new javax.swing.JTable();
        btnModify = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();

        btnchangeStatus.setFont(CoeusFontFactory.getLabelFont());
        btnchangeStatus.setMnemonic('H');
        btnchangeStatus.setText("Change Status");
        btnchangeStatus.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnchangeStatus.setMaximumSize(new java.awt.Dimension(110, 22));
        btnchangeStatus.setMinimumSize(new java.awt.Dimension(110, 22));
        btnchangeStatus.setPreferredSize(new java.awt.Dimension(110, 22));

        setLayout(new java.awt.GridBagLayout());

        pnlUploadDocuments.setLayout(new java.awt.GridBagLayout());

        pnlUploadDocuments.setMinimumSize(new java.awt.Dimension(850, 265));
        pnlUploadDocuments.setPreferredSize(new java.awt.Dimension(850, 265));
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('l');
        btnDelete.setText("Delete");
        btnDelete.setMargin(new java.awt.Insets(2, 10, 2, 14));
        btnDelete.setMaximumSize(new java.awt.Dimension(110, 22));
        btnDelete.setMinimumSize(new java.awt.Dimension(110, 22));
        btnDelete.setNextFocusableComponent(btnView);
        btnDelete.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnDelete, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        btnView.setMaximumSize(new java.awt.Dimension(110, 22));
        btnView.setMinimumSize(new java.awt.Dimension(110, 22));
        btnView.setNextFocusableComponent(btnchangeStatus);
        btnView.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnView, gridBagConstraints);

        pnlListDocument.setLayout(new java.awt.GridBagLayout());

        pnlListDocument.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agenda/Minutes Attachments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlListDocument.setMinimumSize(new java.awt.Dimension(720, 250));
        pnlListDocument.setPreferredSize(new java.awt.Dimension(720, 250));
        scrPnListDocument.setMaximumSize(new java.awt.Dimension(710, 220));
        scrPnListDocument.setMinimumSize(new java.awt.Dimension(710, 220));
        scrPnListDocument.setPreferredSize(new java.awt.Dimension(710, 220));
        tblListDocument.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        scrPnListDocument.setViewportView(tblListDocument);

        pnlListDocument.add(scrPnListDocument, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(pnlListDocument, gridBagConstraints);
        pnlListDocument.getAccessibleContext().setAccessibleName("Agenda/Minutes Attachments");

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('o');
        btnModify.setText("Modify");
        btnModify.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnModify.setMaximumSize(new java.awt.Dimension(110, 22));
        btnModify.setMinimumSize(new java.awt.Dimension(110, 22));
        btnModify.setNextFocusableComponent(btnDelete);
        btnModify.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        pnlUploadDocuments.add(btnModify, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMargin(new java.awt.Insets(2, 10, 2, 10));
        btnAdd.setMaximumSize(new java.awt.Dimension(110, 22));
        btnAdd.setMinimumSize(new java.awt.Dimension(110, 22));
        btnAdd.setName("btnAdd");
        btnAdd.setNextFocusableComponent(btnModify);
        btnAdd.setPreferredSize(new java.awt.Dimension(110, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 3, 3);
        pnlUploadDocuments.add(btnAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 5, 3);
        add(pnlUploadDocuments, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnView;
    private javax.swing.JButton btnchangeStatus;
    private javax.swing.JPanel pnlListDocument;
    private javax.swing.JPanel pnlUploadDocuments;
    private javax.swing.JScrollPane scrPnListDocument;
    private javax.swing.JTable tblListDocument;
    // End of variables declaration//GEN-END:variables
    
}
