/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * SubcontractAttachmentController.java
 *
 * Created on January 4, 2012, 10:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.subcontract.bean.SubContractAttachmentBean;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.subcontract.controller.SubcontractController;
import edu.mit.coeus.subcontract.gui.SubContractAddAttachmentForm;
import edu.mit.coeus.subcontract.gui.SubContractAttachmentForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author manjunathabn
 */
public class SubcontractAttachmentController extends SubcontractController implements ActionListener, ListSelectionListener{
    
    private JScrollPane jscrPn;
    private SubContractAttachmentForm subcontractAttachmentForm ;    
//    private SubContractAddAttachmentController subContractAddAttachmentController;
    private static final int TYPE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int LAST_UPDATED_COLUMN = 2;
    private static final int UPDATED_BY_COLUMN = 3;
    private static final String SUBCONTRACT_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/SubcontractMaintenenceServlet";
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String EMPTY_STRING ="";
    private static final char GET_SUBCONTRACT_UPLOAD_ATTACH = 'o';
    private static final char ADD_VOID_AWARD_DOC_DATA = 'p';
    private static final char VIEW_SUBCONTRACT_DOC_DATA = 'q';
    private static final char GET_DOCUMENT_RIGHTS = 'n';
    private CoeusAppletMDIForm mdiForm;
    private SubcontractAttachmentTableModel subcontractAttachmentTableModel;
    private SubcontractAttachmentRenderer subcontractAttachmentRenderer;
    private QueryEngine queryEngine;
    private CoeusMessageResources coeusMessageResources;
    private CoeusVector vecSubcontrctAttachList = new CoeusVector();
    private CoeusVector vecActiveDoc = new CoeusVector();
    private boolean filterActiveDocs = true;
    private byte[] blobData;
    private boolean userHasModifyAward;
    private boolean userHasViewAward;
    private boolean canModifyAttachment;
    private boolean canViewAttachment;
    private boolean canViewSubContractDocument;
    private boolean canModifySubContract;
    private AppletServletCommunicator appletServletCommunicator;
    private CoeusVector vecListData;
    boolean isFired = false;
    private static final char MODIFY_DOCUMENT = 'M';
    private static final char CHANGE_STATUS = 'S';
    private static final char ADD_DOCUMENT = 'A';
    private CoeusVector cvDocType;
    private RequesterBean requester;
    private static final char ADD_UPD_DEL_SUBCONTRACT_ATTACH = 's';
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/SubcontractMaintenenceServlet";
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private AppletServletCommunicator comm;
    private ResponderBean responder;
    private char functionType;
    public static final char NEW_ENTRY = 'E';
    private static final char GET_ATTACHMENT_TYPES = 'b';

    /**
     * Creates a new instance of SubcontractAttachmentController
     */
    public SubcontractAttachmentController(SubContractBean subContractBean,
            CoeusAppletMDIForm mdiForm, char functionType) {
        super(subContractBean);
        setFunctionType(functionType);
        this.functionType = functionType;
        queryEngine = QueryEngine.getInstance();
        this.mdiForm = mdiForm;
        subcontractAttachmentForm = new SubContractAttachmentForm();
        jscrPn = new JScrollPane(subcontractAttachmentForm);
        postInitComponents();
        registerComponents();
        setTableEditors();
        setFormData();
        if(vecSubcontrctAttachList != null && !vecSubcontrctAttachList.isEmpty()){
            subcontractAttachmentForm.tblListDocument.setRowSelectionInterval(0,0);
        }
    }
    
    
    
    public Component getControlledUI() {
        return jscrPn;
    }
    
    /**
     * This method is used to set the data
     * @param a CoeusVector Object which contains the both Active and Void documents depending whether
     * the documents have been filtered or not
     */
    //this method to be used for setting data only , no filtering of docs done here
    public void setFormData(){
        
        getSubcontractAttachments();
        getSubContractDocumentRights();
        try{
            if(vecSubcontrctAttachList == null) {
                vecSubcontrctAttachList = new CoeusVector();
            }
            if(functionType == TypeConstants.DISPLAY_MODE){
                subcontractAttachmentForm.btnAdd.setEnabled(false);
                subcontractAttachmentForm.btnDelete.setEnabled(false);
                subcontractAttachmentForm.btnModify.setEnabled(false);
                if(vecSubcontrctAttachList.isEmpty() || (!canViewSubContractDocument && !canModifySubContract)){
                    subcontractAttachmentForm.btnView.setEnabled(false);
                }else if(canViewSubContractDocument || canModifySubContract){
                    subcontractAttachmentForm.btnView.setEnabled(true);
                }
                
            }else{
                if(vecSubcontrctAttachList.isEmpty()){
                    subcontractAttachmentForm.btnAdd.setEnabled(true);
                    subcontractAttachmentForm.btnDelete.setEnabled(false);
                    subcontractAttachmentForm.btnModify.setEnabled(false);
                    subcontractAttachmentForm.btnView.setEnabled(false);
                }else{
                    subcontractAttachmentForm.btnAdd.setEnabled(true);
                    subcontractAttachmentForm.btnDelete.setEnabled(true);
                    subcontractAttachmentForm.btnModify.setEnabled(true);
                    subcontractAttachmentForm.btnView.setEnabled(true);
                }
            }
            subcontractAttachmentTableModel.setData(vecSubcontrctAttachList);
            isFired = true;
            subcontractAttachmentTableModel.fireTableDataChanged();
            
            isFired = false;
            subcontractAttachmentForm.btnAdd.requestFocusInWindow();
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    
    public Object getFormData() {
        return null;
    }        
    
    public void formatFields() {
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    private void postInitComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        screenFocusTraversal();
    }
    
    /**
     * This method is used to set the traversal of components through
     * components present in the form
     */
    private void screenFocusTraversal(){
        ScreenFocusTraversalPolicy traversePolicy;
        java.awt.Component[] components = { subcontractAttachmentForm.btnAdd,
        subcontractAttachmentForm.btnDelete,
        subcontractAttachmentForm.btnModify,
        subcontractAttachmentForm.btnView};
        traversePolicy = new ScreenFocusTraversalPolicy( components );
        subcontractAttachmentForm.setFocusTraversalPolicy(traversePolicy);
        subcontractAttachmentForm.setFocusCycleRoot(true);
    }
    
    /**
     * This method is used to register components
     */
    public void registerComponents() {
        subcontractAttachmentForm.btnDelete.addActionListener(this);
        subcontractAttachmentForm.btnModify.addActionListener(this);
        subcontractAttachmentForm.btnAdd.addActionListener(this);
        subcontractAttachmentForm.btnView.addActionListener(this);
        subcontractAttachmentTableModel = new SubcontractAttachmentTableModel();
        subcontractAttachmentRenderer = new SubcontractAttachmentRenderer();
        subcontractAttachmentForm.tblListDocument.setModel(subcontractAttachmentTableModel);
        subcontractAttachmentForm.tblListDocument.getSelectionModel().addListSelectionListener(this);
    }
    
    
    public void saveFormData() throws CoeusException {
    }
    
    public void display() {
    }
    /**
     * set The Award Document editor and headers
     */
    private void setTableEditors() {
        JTableHeader tableHeader = subcontractAttachmentForm.tblListDocument.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100, 25));
        tableHeader.setMinimumSize(new Dimension(100, 25));
        tableHeader.setPreferredSize(new Dimension(100, 25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        subcontractAttachmentForm.tblListDocument.setRowHeight(22);
        subcontractAttachmentForm.tblListDocument.setShowHorizontalLines(true);
        subcontractAttachmentForm.tblListDocument.setShowVerticalLines(true);
        subcontractAttachmentForm.tblListDocument.setOpaque(false);
        subcontractAttachmentForm.tblListDocument.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        subcontractAttachmentForm.tblListDocument.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn columnDetails;
        int []size = {130,300,130,140};
        for(int index = 0; index < size.length; index++) {
            columnDetails = subcontractAttachmentForm.tblListDocument.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            columnDetails.setCellRenderer(subcontractAttachmentRenderer);
        }
    }
    
    
    private class SubcontractAttachmentTableModel extends AbstractTableModel{
        private String [] colNames = {"Type","Description","Last Updated","Updated By"};
        private Class colClass[]={String.class,String.class,String.class,String.class};
        private CoeusVector vecListData1;
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
            if(vecListData1 == null || vecListData1.size() == 0){
                return 0;
            }
            return vecListData1.size();
            
        }
        /**
         * method to set data in table
         * @param vecListData contains data for table
         */
        public void setData(CoeusVector vecListData){
            this.vecListData1 = vecListData;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(vecListData1 != null && vecListData1.size() > 0){
                SubContractAttachmentBean subContractAttachmentBean
                        = (SubContractAttachmentBean)vecListData1.get(rowIndex);
                switch(columnIndex){
                    case TYPE_COLUMN:
                        return subContractAttachmentBean.getAttachmentTypeDescription();
                    case DESCRIPTION_COLUMN:
                        return subContractAttachmentBean.getDescription();
                    case LAST_UPDATED_COLUMN:
                        return subContractAttachmentBean.getUpdateTimestamp().toString();
                    case UPDATED_BY_COLUMN:
                        return subContractAttachmentBean.getUpdateUser();
                }
            }
            return EMPTY_STRING;
        }
    }
    
    private class SubcontractAttachmentRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        
        /**
         * constructor for Award Document renderer
         */
        public SubcontractAttachmentRenderer(){
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
            
            SubContractAttachmentBean subContractAttachmentBean
                    = (SubContractAttachmentBean)vecSubcontrctAttachList.get(row);
            switch (col){
                case TYPE_COLUMN:
                case DESCRIPTION_COLUMN:
                case UPDATED_BY_COLUMN:
                case LAST_UPDATED_COLUMN:
                    lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
                    if(col == LAST_UPDATED_COLUMN ){
                        lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
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
    
    
    public void refresh(){
        if( !isRefreshRequired() ) return ;
        setRefreshRequired(false);
    }
    
    /**
     * This method is used to route to the corresponding method
     * depending on the action performed
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            if( source.equals(subcontractAttachmentForm.btnAdd)) {
                if(getFunctionType() == NEW_ENTRY){                    
                     CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                "subcontract_addAttachmentConfirmationCode.1030"));
                }
                else {
                    performAddDocument();
                }
            }else if (source.equals(subcontractAttachmentForm.btnDelete)){
                performDeleteAction();
            }else if (source.equals(subcontractAttachmentForm.btnModify)){
                performModifyAction();
            }else if(source.equals(subcontractAttachmentForm.btnView)){
                performViewAction();
            }
        }catch(Exception ce){
            CoeusOptionPane.showErrorDialog(ce.getMessage());
            ce.printStackTrace();
        }
    }
    /**
     * In this method a server call is made to get the list of updated documents
     * @return Vector Object which contains updated award document list
     */
    //private CoeusVector getUpdatedAwardData(){
    private CoeusVector getSubcontractAttachments(){
        RequesterBean requesterBean = new RequesterBean();        
        Vector vecServerObjects = new Vector();
        vecServerObjects.add(0,subContractBean);
        requesterBean.setDataObject(subContractBean);
        requesterBean.setFunctionType(GET_SUBCONTRACT_UPLOAD_ATTACH);
        
        appletServletCommunicator = new
                AppletServletCommunicator(SUBCONTRACT_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();
        if(!responder.isSuccessfulResponse()){
            try {
                throw new CoeusException(responder.getMessage(),0);
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }
        }
        vecSubcontrctAttachList = (CoeusVector)responder.getDataObject();
        vecSubcontrctAttachList = vecSubcontrctAttachList != null && vecSubcontrctAttachList.size() >0 ? vecSubcontrctAttachList : new CoeusVector();
        return vecSubcontrctAttachList;
    }
    /**
     * Method to Add documents
     * Add window is called ,document details are entered and saved
     */
    
    private void performAddDocument() throws CoeusException {
        SubContractAttachmentBean subContractAttachmentBean = new SubContractAttachmentBean();
        subContractAttachmentBean.setSubContractCode(subContractBean.getSubContractCode());
        subContractAttachmentBean.setSequenceNumber(subContractBean.getSequenceNumber());
        subContractAttachmentBean.setUpdateUser(subContractBean.getUpdateUser());
        CoeusVector cvDocType = (CoeusVector)getDocumentData(GET_ATTACHMENT_TYPES, subContractAttachmentBean);
        if(cvDocType == null || cvDocType.isEmpty()){            
            CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(
                                "subcontractAttachType_MessageCode.1031"));
        } else{
            SubContractAddAttachmentForm subContractAddAttachmentForm =
                    new SubContractAddAttachmentForm(subContractAttachmentBean,ADD_DOCUMENT, cvDocType);
            
            subContractAddAttachmentForm.showWindow();
            setFormData();
            int rowCount = subcontractAttachmentForm.tblListDocument.getRowCount();
            if(rowCount > 0){
                subcontractAttachmentForm.tblListDocument.setRowSelectionInterval(rowCount-1,rowCount-1);
            }
        }
       
    }
    
    
    /**
     * Mehtod to perform the modify of the selected document
     * @throws CoeusException if any exception occurs
     */
    public void performModifyAction() throws CoeusException{
        int rowCount = subcontractAttachmentForm.tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1012"));
            return;
        }
        int selectedRow = subcontractAttachmentForm.tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
            return;
        }
        SubContractAttachmentBean subContractAttachmentBean
                = (SubContractAttachmentBean)vecSubcontrctAttachList.get(selectedRow);
        CoeusVector cvDocType = (CoeusVector)getDocumentData(GET_ATTACHMENT_TYPES, subContractAttachmentBean);
        SubContractAddAttachmentForm subContractAddAttachmentForm =
                new SubContractAddAttachmentForm(subContractAttachmentBean,MODIFY_DOCUMENT,cvDocType);
        
        subContractAddAttachmentForm.showWindow();
        setFormData();
        if(selectedRow > -1){
            subcontractAttachmentForm.tblListDocument.setRowSelectionInterval(selectedRow,selectedRow);
        }
    }
    
    
    /**
     * method to perform delete button operation
     * @throws CoeusException If Any exception occurs
     */
    private void performDeleteAction() throws CoeusException{
        int rowCount = subcontractAttachmentForm.tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1000"));
            return;
        }
        int selectedRow = subcontractAttachmentForm.tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1001"));
            return;
        }
        
        SubContractAttachmentBean subContractAttachmentBean
                = (SubContractAttachmentBean)vecSubcontrctAttachList.get(selectedRow);
        
        
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1002"),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES){
            
            subContractAttachmentBean.setAcType(TypeConstants.DELETE_RECORD);
            boolean isSuccess = addUpdDocument(subContractAttachmentBean, true, false);
            if(isSuccess){
                setFormData();
                rowCount = subcontractAttachmentForm.tblListDocument.getRowCount();
                if(rowCount > 0){
                    subcontractAttachmentForm.tblListDocument.setRowSelectionInterval(0,0);
                }
            }
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
    
    
    private boolean addUpdDocument(SubContractAttachmentBean subContractAttachmentBean,
            boolean newVersion, boolean newDocumentId)
            throws CoeusException{
        requester = new RequesterBean();
        requester.setFunctionType(ADD_UPD_DEL_SUBCONTRACT_ATTACH);
        Vector vecServerObjects = new Vector();
        vecServerObjects.add(0,subContractAttachmentBean);
        requester.setDataObjects(vecServerObjects);
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        return responder.isSuccessfulResponse();
    }
    
    
    
    public boolean checkDocumentTypeExists(){
        if(cvDocType == null || cvDocType.size() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1008"));
            return false;
        }
        return true;
    }
    
    
    
    /**
     * Method for viewing the BLOB
     * @param awardDocumentBean which has the awardNumber,sequenceNumber
     * fileName and document for viewing on the browser
     * @return String the url to display the selected file
     */
    
    private void viewDocumentData(SubContractAttachmentBean subContractAttachmentBean){
        try{
            CoeusVector cvDataObject = new CoeusVector();
            HashMap hmDocumentDetails = new HashMap();
            hmDocumentDetails.put("subContractCode", subContractAttachmentBean.getSubContractCode());
            hmDocumentDetails.put("sequenceNumber", EMPTY_STRING+subContractAttachmentBean.getSequenceNumber());
            hmDocumentDetails.put("fileName", subContractAttachmentBean.getFileName());
            hmDocumentDetails.put("document", subContractAttachmentBean.getDocument());
            cvDataObject.add(hmDocumentDetails);
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", cvDataObject);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("MODULE_NAME", "VIEW_DOCUMENT");
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            appletServletCommunicator = new
                    AppletServletCommunicator(STREAMING_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            if(!responder.isSuccessfulResponse()){
                throw new CoeusException(responder.getMessage(),0);
            }
            map = (Map)responder.getDataObject();
            String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
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
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void valueChanged(ListSelectionEvent e) {
        
        int rowCount = subcontractAttachmentForm.tblListDocument.getRowCount();
        if(isFired){
            return;
        }
        int selectedRow = subcontractAttachmentForm.tblListDocument.getSelectedRow();
        SubContractAttachmentBean subContractAttachmentBean =(SubContractAttachmentBean)vecSubcontrctAttachList.get(selectedRow);
    }
    
    
    
    private void performViewAction() throws CoeusException{
        int rowCount = subcontractAttachmentForm.tblListDocument.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
            return;
        }
        int selectedRow = subcontractAttachmentForm.tblListDocument.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
            return;
        }
        SubContractAttachmentBean subContractAttachmentBean = (SubContractAttachmentBean)vecSubcontrctAttachList.get(selectedRow);
        viewDocument(subContractAttachmentBean);
    }
    
    
    public void viewDocument(SubContractAttachmentBean subContractAttachmentBean) throws CoeusException{
        String url = getDocumentURL(subContractAttachmentBean);
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
    
    public String getDocumentURL(SubContractAttachmentBean subContractAttachmentBean) throws CoeusException{
        String templateUrl =  null;
        RequesterBean requesterBean = new RequesterBean();
        
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("SUBCONTRACT_ATTACH_BEAN", subContractAttachmentBean);
        map.put("MODULE_NAME", "SUBCONTRACT_ATTACHMENT_DOC");
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
        documentBean.setParameterMap(map);
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        comm = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        map = (Map)responder.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
        return templateUrl;
    }
    
    
    public byte[] getBlobData() {
        return blobData;
    }
    
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    public void cleanUp(){
        subcontractAttachmentForm = null;
        mdiForm = null;
    }
    
    
    public void setFormData(Object data) throws CoeusException {
    }
    
    /**
     * Method to get Subcontract Attachments Types
     *
     * @param getDataType 
     * @param subContractAttachmentBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return 
     */
    public List getDocumentData(char getDataType, SubContractAttachmentBean subContractAttachmentBean)
    throws CoeusException {
        List cvParam = null;
        requester = new RequesterBean();
        requester.setDataObject(subContractAttachmentBean);
        requester.setFunctionType(getDataType);
        comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();
        responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        cvParam = (CoeusVector)responder.getDataObject();
        return cvParam;
    }

    /**
     * This method is used to get the rights required in Subcontract Attachment
     * @param
     * @return hashtable which contains boolean values for
     * canViewAttachment
     */
    private void getSubContractDocumentRights(){
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean;
        requesterBean.setDataObject(subContractBean.getSubContractCode());
        requesterBean.setFunctionType(GET_DOCUMENT_RIGHTS);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean.isSuccessfulResponse()){
            Hashtable htDocumentRights = (Hashtable)responderBean.getDataObject();
            if(htDocumentRights != null){
                canViewSubContractDocument = new Boolean(htDocumentRights.get("canViewAttachment").toString()).booleanValue();
                canModifySubContract = new Boolean(htDocumentRights.get("canModifySubContract").toString()).booleanValue();
            }
        }
    }
}
