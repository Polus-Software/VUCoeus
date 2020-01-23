/*
 * @(#)ProtocolOtherAttachmentsForm.java 1.0 05/07/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 14-JULY-2010
 * by Md.Ehtesham Ansari
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UserUtils;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * This class is used to create the 'Other' tab in the 'Attachments' tab of
 * ProtocolDetailForm
 *
 * @author  leenababu
 */
public class ProtocolOtherAttachmentsForm extends javax.swing.JPanel implements ActionListener,MouseListener {
    private ProtocolInfoBean protocolInfoBean;
    private char functionType;
    private final char COPY_MODE = 'P';
    private ProtocolDetailForm protocolDetailForm;
    private CoeusMessageResources coeusMessageResources;
    
    private NotifyAttachmentTableModel notifyAttachmentTableModel;
    private NotifyAtachmntTableCellRenderer notifyAtachmntTableCellRenderer;
    private NotifyAttachmentTableCellEditor notifyAttachmentTableCellEditor;
    private OtherAttachmentTableModel otherAttachmentTableModel;
    private OtherAttachTableCellRenderer approvalTableCellRenderer;
    private EmptyHeaderRenderer emptyHeaderRenderer;
    
    
    private Vector vecNotifyIRBActions;
    private CoeusVector cvOtherAttachments;
    private CoeusVector cvDocumentTypes;
    private ImageIcon imgIcnPDF, imgIcnComments;
//    private int NOTIFY_IRB_CODE = 116;
    
    private int NOTIFY_ATTACHMENT_COL = 0;
    private int NOTIFY_DESCRIPTION_COL = 1;
    private int NOTIFY_DATE_COL = 2;
    private int NOTIFY_ACTIONDATE_COL = 3;
    private int NOTIFY_COMMENTS_COL = 4;
    private int NOTIFY_COMMENT_BUTTON_COL = 5;
    
    private int OTHER_ATTACHMENT_TYPE_COL = 0;
    private int OTHER_DESCRIPTION_COL = 1;
    private int OTHER_UPDATED_COL = 2;
    
    private int NOTIFY_ATTACHMENT_COL_WIDTH = 20;
    private int NOTIFY_DESCRIPTION_COL_WIDTH = 220;
    private int NOTIFY_DATE_COL_WIDTH = 100;
    private int NOTIFY_ACTIONDATE_COL_WIDTH = 100;
    private int NOTIFY_COMMENTS_COL_WIDTH = 300;
    private int NOTIFY_COMMENT_BUTTON_COL_WIDTH = 20;
    
    private int OTHER_ATTACHMENT_TYPE_COL_WIDTH = 220;
    private int OTHER_DESCRIPTION_COL_WIDTH = 240;
    private int OTHER_UPDATED_COL_WIDTH = 300;
    
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String PROTOCOL_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
    private static final char GET_OTHER_ATTACHMENTS = '3';
    private static final char ADD_UPD_DEL_OTHER_DOCUMENT = '4';
    boolean canAddAttachment = false;
    private boolean dataFetched = false;
    
    //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
    private static final char COPY_PROTOCOL = 'P';
    //COEUSQA:3503 - End
    
    /** Creates new form ProtocolOtherAttachmentsForm */
    public ProtocolOtherAttachmentsForm(ProtocolInfoBean protocolInfoBean, char functionType) {
        this.protocolInfoBean = protocolInfoBean;
        this.functionType = functionType;
        
        initComponents();
        postInitComponents();
    }
    
    /**
     * Sets the properties for the gui components
     */
    public void postInitComponents(){
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//        imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
        imgIcnPDF = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
        imgIcnComments = new ImageIcon(getClass().getClassLoader().getResource(
                CoeusGuiConstants.JUSTIFIED));
        notifyAtachmntTableCellRenderer = new NotifyAtachmntTableCellRenderer();
        otherAttachmentTableModel = new OtherAttachmentTableModel();
        approvalTableCellRenderer = new OtherAttachTableCellRenderer();
        notifyAttachmentTableCellEditor = new NotifyAttachmentTableCellEditor();
        emptyHeaderRenderer = new EmptyHeaderRenderer();
        
        tblOtherAtthmnts.getTableHeader().setReorderingAllowed(false);
        tblOtherAtthmnts.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblOtherAtthmnts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblOtherAtthmnts.setRowHeight(20);
        
        tblNotifyAttachments.getTableHeader().setReorderingAllowed(false);
        tblNotifyAttachments.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblNotifyAttachments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNotifyAttachments.setRowHeight(20);
        //Added for the Case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-start
        tblNotifyAttachments.getTableHeader().addMouseListener(this);
        tblOtherAtthmnts.getTableHeader().addMouseListener(this);
        //Added for the Case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-end
        
        otherAttachmentTableModel = new OtherAttachmentTableModel();
        tblOtherAtthmnts.setModel(otherAttachmentTableModel);
        btnAdd.addActionListener(this);
        btnModify.addActionListener(this);
        btnDelete.addActionListener(this);
        btnView.addActionListener(this);
    }
    
    /**
     * Enable/Disable the components in the gui according the value of enable
     * argument
     *
     * @param enable
     */
    public void enableComponents(boolean enable){
        btnAdd.setEnabled(enable);
        btnModify.setEnabled(enable);
        btnDelete.setEnabled(enable);
    }
    
    /**
     * Sets the table properties
     */
    public void setTableEditors(){
        TableColumn tableColumn = tblNotifyAttachments.getColumnModel().getColumn(NOTIFY_ATTACHMENT_COL);
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
        JTableHeader tableHeader = tblNotifyAttachments.getTableHeader();
        tableHeader.addMouseListener(new NotifyColumnHeaderListener());
        JTableHeader otherAttTableHeader = tblOtherAtthmnts.getTableHeader();
        otherAttTableHeader.addMouseListener(new OtherColumnHeaderListener());
        //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
        tableColumn.setCellRenderer(notifyAtachmntTableCellRenderer);
        tableColumn.setCellEditor(notifyAttachmentTableCellEditor);
        tableColumn.setHeaderRenderer(emptyHeaderRenderer);
        tableColumn.setMaxWidth(NOTIFY_ATTACHMENT_COL_WIDTH);
        tableColumn.setMinWidth(NOTIFY_ATTACHMENT_COL_WIDTH);
        tableColumn.setPreferredWidth(NOTIFY_ATTACHMENT_COL_WIDTH);
        
        tableColumn = tblNotifyAttachments.getColumnModel().getColumn(NOTIFY_DESCRIPTION_COL);
        tableColumn.setCellRenderer(notifyAtachmntTableCellRenderer);
        tableColumn.setPreferredWidth(NOTIFY_DESCRIPTION_COL_WIDTH);
        
        tableColumn = tblNotifyAttachments.getColumnModel().getColumn(NOTIFY_DATE_COL);
        tableColumn.setCellRenderer(notifyAtachmntTableCellRenderer);
        tableColumn.setPreferredWidth(NOTIFY_DATE_COL_WIDTH);
        
        tableColumn = tblNotifyAttachments.getColumnModel().getColumn(NOTIFY_ACTIONDATE_COL);
        tableColumn.setCellRenderer(notifyAtachmntTableCellRenderer);
        tableColumn.setPreferredWidth(NOTIFY_ACTIONDATE_COL_WIDTH);
        
        tableColumn = tblNotifyAttachments.getColumnModel().getColumn(NOTIFY_COMMENTS_COL);
        tableColumn.setCellRenderer(notifyAtachmntTableCellRenderer);
        tableColumn.setPreferredWidth(NOTIFY_COMMENTS_COL_WIDTH);
        
        tableColumn =tblNotifyAttachments.getColumnModel().getColumn(NOTIFY_COMMENT_BUTTON_COL);
        tableColumn.setCellRenderer(notifyAtachmntTableCellRenderer);
        tableColumn.setCellEditor(notifyAttachmentTableCellEditor);
        tableColumn.setHeaderRenderer(emptyHeaderRenderer);
        tableColumn.setMaxWidth(NOTIFY_COMMENT_BUTTON_COL_WIDTH);
        tableColumn.setMinWidth(NOTIFY_COMMENT_BUTTON_COL_WIDTH);
        tableColumn.setPreferredWidth(NOTIFY_COMMENT_BUTTON_COL_WIDTH);
        
        // #Case 3855 -- start new column to store the file name value
        tableColumn =tblNotifyAttachments.getColumnModel().getColumn(6);
        tableColumn.setMinWidth(0);
        tableColumn.setPreferredWidth(0);
        // #Case 3855 -- end
        
        tableColumn = tblOtherAtthmnts.getColumnModel().getColumn(OTHER_ATTACHMENT_TYPE_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(OTHER_ATTACHMENT_TYPE_COL_WIDTH);
        
        tableColumn = tblOtherAtthmnts.getColumnModel().getColumn(OTHER_DESCRIPTION_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(OTHER_DESCRIPTION_COL_WIDTH);
        
        tableColumn = tblOtherAtthmnts.getColumnModel().getColumn(OTHER_UPDATED_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(OTHER_UPDATED_COL_WIDTH);
    }
    
    /**
     * Sets the form data
     */
    public void setFormData(){
        //Set the function type as display mode if the the protocol is an
        //amendment or renewal
        if(protocolDetailForm != null && protocolDetailForm.getOriginalFunctionType() == 'E'){
            functionType = TypeConstants.DISPLAY_MODE;
        }
        
        if(protocolInfoBean!= null && isProtocolAmendRenewalType()){
            functionType = TypeConstants.DISPLAY_MODE;
        }
        
        if(functionType == TypeConstants.DISPLAY_MODE){
            enableComponents(false);
        }
        if(functionType == COPY_MODE){
            vecNotifyIRBActions = new Vector();
        }else{
            vecNotifyIRBActions = filterNotifyIRBActions();
        }
        if(functionType == TypeConstants.DISPLAY_MODE){
            btnView.requestFocus();
        }else{
            btnAdd.requestFocus();
        }
        Component components[] = {btnAdd, btnModify, btnDelete, btnView};
        ScreenFocusTraversalPolicy screenFocusTraversalPolicy =
                new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(screenFocusTraversalPolicy);
        setFocusCycleRoot(true);
        
        notifyAttachmentTableModel  = new NotifyAttachmentTableModel();
        tblNotifyAttachments.setModel(notifyAttachmentTableModel);
        populateOtherAttachments();
        setTableEditors();
        dataFetched = true;
    }
    
    /**
     * Populate data in the other attachments table
     */
    public void populateOtherAttachments(){
         //COEUSQA:3503 - Protocol Copying - Option to Copy Attachments and Questionnaires - Start
        //cvOtherAttachments = getOtherAttachments();
        if(functionType != COPY_PROTOCOL){
            cvOtherAttachments = getOtherAttachments();
        }
        else {
            if(protocolInfoBean.isCopyOtherAttachments() == true){
                cvOtherAttachments = (CoeusVector) protocolInfoBean.getOtherAttachments();
            }
            else{
                cvOtherAttachments = new CoeusVector();
            }
        }
        //COEUSQA:3503 - End
        otherAttachmentTableModel.fireTableDataChanged();
    }
    
    /**
     * Get the data for the other attachments from the database
     *
     * @return CoeusVector
     */
    private CoeusVector getOtherAttachments(){
        CoeusVector cvOtherAttachmentsFromDb = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(GET_OTHER_ATTACHMENTS);
        String protocolNumber = null;
        if(protocolInfoBean != null){
            protocolNumber = protocolInfoBean.getProtocolNumber();
            if(protocolNumber.indexOf(CoeusConstants.IACUC_AMENDMENT)!=-1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf(CoeusConstants.IACUC_AMENDMENT));
            }else if(protocolNumber.indexOf(CoeusConstants.IACUC_RENEWAL) != -1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf(CoeusConstants.IACUC_RENEWAL));
            }else if(protocolNumber.indexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT) != -1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT));
            }
            /*COEUSQA-1724-New Condition Added for new Amendment/Renewal type-start*/ 
            else if(protocolNumber.indexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW) != -1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW));
            }else if(protocolNumber.indexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND) != -1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND));
            }
            /*COEUSQA-1724-New Condition Added for new Amendment/Renewal type-end*/
        }
        requesterBean.setDataObject(protocolNumber);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(PROTOCOL_SERVLET, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        try{
            if(response != null && response.hasResponse()){
                Vector vecServerData = response.getDataObjects();
                if(vecServerData != null && vecServerData.size()== 2){
                    cvOtherAttachmentsFromDb = (CoeusVector)vecServerData.get(0);
                    cvDocumentTypes = (CoeusVector)vecServerData.get(1);
                }
            }
        }catch(CoeusException e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        return cvOtherAttachmentsFromDb;
    }
    /**
     * Opens the document for viewing
     * @param protocolActionBean containg document details
     */
    private void viewSubmissionDocument(ProtocolActionsBean protocolActionBean) throws Exception{
        String templateUrl =  null;
        DocumentBean documentBean = new DocumentBean();
        RequesterBean requesterBean = new RequesterBean();
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        protocolActionDocumentBean.setProtocolNumber(protocolActionBean.getProtocolNumber());
        protocolActionDocumentBean.setSequenceNumber(protocolActionBean.getSequenceNumber());
        protocolActionDocumentBean.setSubmissionNumber(protocolActionBean.getSubmissionNumber());
        //Commented for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
        //Setting the selected document document id
//        protocolActionDocumentBean.setDocumentId(1);
        protocolActionDocumentBean.setDocumentId(protocolActionBean.getProtocolActionDocumentBean().getDocumentId());
        //COEUSDEV-328 : End
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "SUBMISSION_DOC_DB");
        map.put("PROTO_ACTION_BEAN", protocolActionDocumentBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(STREAMING_SERVLET, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        responderBean = comm.getResponse();
        if(!responderBean.hasResponse()){
            throw new CoeusException(responderBean.getMessage(),0);
        }
        map = (Map)responderBean.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
        
        try{
            URL urlObj = new URL(templateUrl);
            URLOpener.openUrl(urlObj);
        }catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }catch(Exception ue) {
            ue.printStackTrace() ;
        }
    }
    
    /**
     * Filter the notify irb actions from the protcol actions which has a
     * document uploaded to it
     *
     * @return Vector
     */
    public Vector filterNotifyIRBActions(){
        Vector vecNotifyIRBAction = new Vector();
        if(protocolInfoBean!=null){
            //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//            Vector vecProtocolActions = protocolInfoBean.getActions();
            Vector vecProtocolActions = protocolInfoBean.getActionsDocuments();
            //COEUSDEV-328 : End
            if(vecProtocolActions!=null){
                ProtocolActionsBean protocolActionBean;
                for(int i=0; i<vecProtocolActions.size(); i++){
                    protocolActionBean = (ProtocolActionsBean)vecProtocolActions.get(i);
                    //Commented for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
                    //All the action documents will be dispayed in the other attachment
//                    if(protocolActionBean.getActionTypeCode() == NOTIFY_IRB_CODE){
//                            && protocolActionBean.isIsDocumentExists()){
                    //COEUSDEV-328 : End
                    vecNotifyIRBAction.add(protocolActionBean);
//                    }
                }
            }
        }
        return vecNotifyIRBAction;
    }
    
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source.equals(btnAdd)){
            //Save the protcol before performing the add action if required
            if(!canAddAttachment && protocolDetailForm != null
                    && (protocolDetailForm.getOriginalFunctionType() == 'A'
                    || protocolDetailForm.isSaveRequired())){
                ActionEvent ae = new ActionEvent(protocolDetailForm.btnSaveProtocol, 1001, null);
                protocolDetailForm.actionPerformed(ae);
                if(protocolDetailForm.isSaveSuccessful()){
                    protocolDetailForm.setSaveSuccessful(false);
                    canAddAttachment = true;
                }
            }else{
                canAddAttachment = true;
            }
            
            if(canAddAttachment &&  checkDocumentTypeExists()){
                AddOtherAttachmentsForm addOtherAttachmentForm =
                        new AddOtherAttachmentsForm(TypeConstants.ADD_MODE, null, protocolInfoBean);
                addOtherAttachmentForm.setDocumentTypes(cvDocumentTypes);
                addOtherAttachmentForm.showWindow();
                if(addOtherAttachmentForm.isSaveRequired()){
                    populateOtherAttachments();
                }
            }else{
                return;
            }
        }else if(source.equals(btnModify)){
            int rowCount = tblOtherAtthmnts.getRowCount();
            if(rowCount == 0){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1012"));
                return;
            }
            int selectedRow = tblOtherAtthmnts.getSelectedRow();
            if(selectedRow == -1){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
                return;
            }else{
                UploadDocumentBean uploadDocumentBean = (UploadDocumentBean)cvOtherAttachments.get(selectedRow);
                AddOtherAttachmentsForm addOtherAttachmentForm =
                        new AddOtherAttachmentsForm(TypeConstants.MODIFY_MODE, uploadDocumentBean, protocolInfoBean);
                addOtherAttachmentForm.setDocumentTypes(cvDocumentTypes);
                addOtherAttachmentForm.showWindow();
                if(addOtherAttachmentForm.isSaveRequired()){
                    populateOtherAttachments();
                }
            }
        }else if(source.equals(btnDelete)){
            int rowCount = tblOtherAtthmnts.getRowCount();
            if(rowCount == 0){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1000"));
                return;
            }
            int selectedRow = tblOtherAtthmnts.getSelectedRow();
            if(selectedRow == -1){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1001"));
                return;
            }
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
            
            if(selectedOption == CoeusOptionPane.SELECTION_YES){
                UploadDocumentBean uploadDocumentBean = (UploadDocumentBean)cvOtherAttachments.get(selectedRow);
                uploadDocumentBean.setAcType(TypeConstants.DELETE_RECORD);
                saveUploadDocument(uploadDocumentBean);
                populateOtherAttachments();
            }
        }else if(source.equals(btnView)){
            int rowCount = tblOtherAtthmnts.getRowCount();
            if(rowCount == 0){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
                return;
            }
            int selectedRow = tblOtherAtthmnts.getSelectedRow();
            if(selectedRow == -1){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
                return;
            }else{
                UploadDocumentBean uploadDocumentBean = (UploadDocumentBean)cvOtherAttachments.get(selectedRow);
                viewDocument(uploadDocumentBean);
            }
        }
    }
    /**
     * Shows warning message if the not document types available
     * @return boolean
     */
    public boolean checkDocumentTypeExists(){
        if(cvDocumentTypes == null || cvDocumentTypes.size() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1008"));
            return false;
        }
        return true;
    }
    
    /**
     * Save the upload document to the db
     *@return boolean - true if success
     */
    public boolean saveUploadDocument(UploadDocumentBean uploadDocBean){
        boolean success = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(uploadDocBean);
        requesterBean.setFunctionType(ADD_UPD_DEL_OTHER_DOCUMENT);
        AppletServletCommunicator comm = new AppletServletCommunicator(PROTOCOL_SERVLET, requesterBean);
        comm.send();
        
        ResponderBean responder = comm.getResponse();
        try{
            if(responder.hasResponse()){
                success = true;
            }
        }catch(CoeusException e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        return success;
    }
    
    /**
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException If any exception occurs
     */
    public void viewDocument(UploadDocumentBean uploadDocumentBean){
        try{
            String url = getDocumentURL(uploadDocumentBean);
            if(url == null || url.trim().length() == 0 ) {
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
                return;
            }
            url = url.replace('\\', '/') ; // this is fix for Mac
            try{
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }catch( Exception ue) {
                CoeusOptionPane.showErrorDialog(ue.getMessage());
            }
        }catch(CoeusException e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    /**
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException If any exception occurs
     * @return url of view document
     */
    public String getDocumentURL(UploadDocumentBean uploadDocumentBean) throws CoeusException{
        String templateUrl =  null;
        RequesterBean requester = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("PROTOCOL_OTHER_DOC_BEAN", uploadDocumentBean);
        map.put("DOCUMENT_TYPE", "PROTO_OTHER_DOC");
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        
        documentBean.setParameterMap(map);
        requester.setDataObject(documentBean);
        requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        AppletServletCommunicator comm = new AppletServletCommunicator(STREAMING_SERVLET, requester);
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(responder!=null){
            if(responder.hasResponse()){
                map = (Map)responder.getDataObject();
                templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
            }else{
                throw new CoeusException(responder.getMessage(),0);
            }
        }
        return templateUrl;
    }
    /**
     * This class is used as the Table Model for the table tblNotifyAttachments
     */
    public class NotifyAttachmentTableModel extends AbstractTableModel{
        //#Case 3855 add a dummy column to store file name which is not visible to the user.
        private String colNames[] = {" ","Action","Date","Action Date","Description"," ", ""};
        edu.mit.coeus.utils.DateUtils dateUtils = new edu.mit.coeus.utils.DateUtils();
        
        public Object getValueAt(int row, int col){
            ProtocolActionsBean protocolActionsBean = (ProtocolActionsBean)vecNotifyIRBActions.get(row);
            if(col == NOTIFY_ATTACHMENT_COL ){
                return "";
            }else if(col == NOTIFY_DESCRIPTION_COL){
                return protocolActionsBean.getActionTypeDescription();
            }else if(col == NOTIFY_DATE_COL){
                return dateUtils.formatDate(protocolActionsBean.getUpdateTimestamp().toString(), "dd-MMM-yyyy");
            }else if(col == NOTIFY_ACTIONDATE_COL){
                if(protocolActionsBean.getActionDate() != null){
                    return dateUtils.formatDate(protocolActionsBean.getActionDate().toString(), "dd-MMM-yyyy");
                }else{
                    return CoeusGuiConstants.EMPTY_STRING;
                }
            }else if(col == NOTIFY_COMMENTS_COL){
                //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//                return protocolActionsBean.getComments();
                return protocolActionsBean.getProtocolActionDocumentBean().getDescription();
                //COEUSDEV-328 : End
            }else if(col == NOTIFY_COMMENT_BUTTON_COL){
                return "";
            }
            //# Case 3855 start return the file name
            else if(col == 6) {
//                return  protocolActionsBean.getFileName();
                return protocolActionsBean;//case 4007
            }
            // #Case 3855 -- end
            return "";
        }
        
        public int getRowCount() {
            if(vecNotifyIRBActions != null){
                return vecNotifyIRBActions.size();
            }
            return 0;
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public boolean isCellEditable(int row, int column){
            if(column == NOTIFY_ATTACHMENT_COL || column == NOTIFY_COMMENT_BUTTON_COL){
                return true;
            }
            return false;
        }
    }
    
    /**
     * This class is used as the Table Model for the table tblApprovalAttachments
     */
    public class OtherAttachmentTableModel extends AbstractTableModel{
        private String colNames[] = {"Type","Description","Last Updated by"};
        
        public Object getValueAt(int row, int col){
            UploadDocumentBean uploadDocumentBean = (UploadDocumentBean)cvOtherAttachments.get(row);
            if(col == OTHER_ATTACHMENT_TYPE_COL){
                return uploadDocumentBean.getDocType();
            }else if(col == OTHER_DESCRIPTION_COL){
                return uploadDocumentBean.getDescription();
            }else if(col == OTHER_UPDATED_COL){
                return uploadDocumentBean.getUpdateUserName() + " at " +
                        CoeusDateFormat.format(uploadDocumentBean.getUpdateTimestamp().toString());
            }
            return "";
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount(){
            if(cvOtherAttachments!=null){
                return cvOtherAttachments.size();
            }else{
                return 0;
            }
        }
        
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        public void setDataVector(Vector newData) {
            
        }
        
        public boolean isCellEditable(int row, int column){
            return false;
        }
    }
    
    /**
     * This class is used as the Table Cell Renderer for the table tblNotifyAttachments
     */
    public class NotifyAtachmntTableCellRenderer extends DefaultTableCellRenderer{
        private JButton btnAttachment, btnDescription;
        private JLabel lblText;
        Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
        public NotifyAtachmntTableCellRenderer(){
            // #Case 3855 commented to add document specific icon
            // btnAttachment = new JButton(imgIcnPDF);
            btnAttachment = new JButton();
            btnDescription = new JButton(imgIcnComments);
            lblText = new JLabel();
            lblText.setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if(column == NOTIFY_ATTACHMENT_COL){
                //Modified with case 4007 : Icon based on mime Type :Start
                // #Case3855 - start  Added attachment specific icon.
//                String fileExtension = UserUtils.getFileExtension((String) table.getValueAt(row,6));
//                btnAttachment.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                // #Case3855 - end  Added attachment specific icon.
                //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//                CoeusAttachmentBean attachmentBean = (CoeusAttachmentBean) table.getValueAt(row,6);
//                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
//                btnAttachment.setIcon(docUtils.getAttachmentIcon(attachmentBean));
                  ProtocolActionsBean protocolActionsBean = (ProtocolActionsBean) table.getValueAt(row,6);
                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
                btnAttachment.setIcon(docUtils.getAttachmentIcon(protocolActionsBean.getProtocolActionDocumentBean()));
                //COEUSDEV-328 : End
                //4007 End
                return btnAttachment;
            }else if(column == NOTIFY_COMMENT_BUTTON_COL){
                return btnDescription;
            }else{
                if(value != null){
                    lblText.setText(value.toString());
                }
                if(isSelected){
                    lblText.setBackground(java.awt.Color.YELLOW);
                }else{
                    lblText.setBackground(bgListColor);
                }
                return lblText;
            }
        }
        
    }
    
    /**
     * This class is used as the Table cell editor for the table tblNotifyAttachments
     */
    public class NotifyAttachmentTableCellEditor extends DefaultCellEditor implements ActionListener{
        private JButton btnAttachment, btnDescription;
        //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//        private edu.mit.coeus.utils.CommentsForm commentsForm;
        private edu.mit.coeus.utils.CommentsForm descriptionForm;
        //COEUSDEV-328 : End
        private ProtocolActionsBean protocolActionsBean;
        public NotifyAttachmentTableCellEditor(){
            super(new JComboBox());
            //#Case 3855 - modified to set attachment specific icon.
            // btnAttachment = new JButton(imgIcnPDF);
            btnAttachment = new JButton();
            btnDescription = new JButton(imgIcnComments);
            
            btnAttachment.addActionListener(this);
            btnDescription.addActionListener(this);
            //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//            commentsForm = new edu.mit.coeus.utils.CommentsForm("Protocol Action Comments");
            descriptionForm = new edu.mit.coeus.utils.CommentsForm("Attachment Description");
            //COEUSDEV-328 : End
        }
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if(column == NOTIFY_ATTACHMENT_COL){
                //Modified with case 4007:Icon based on mime Type
                // #Case3855 - start  Added attachment specific icon.
//                String fileExtension = UserUtils.getFileExtension((String) table.getValueAt(row,6));
//                btnAttachment.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                // #Case3855 - end  Added attachment specific icon.
                //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//                CoeusAttachmentBean attachmentBean = (CoeusAttachmentBean) table.getValueAt(row,6);
//                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
//                btnAttachment.setIcon(docUtils.getAttachmentIcon(attachmentBean));
                ProtocolActionsBean protocolActionsBean = (ProtocolActionsBean) table.getValueAt(row,6);
                CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
                btnAttachment.setIcon(docUtils.getAttachmentIcon(protocolActionsBean.getProtocolActionDocumentBean()));
                //COEUSDEV-328 : End
                return btnAttachment;
            }else if(column == NOTIFY_COMMENTS_COL){
                return btnDescription;
            }else{
                return btnDescription;
            }
        }
        public void actionPerformed(ActionEvent e){
            Object source = e.getSource();
            
            protocolActionsBean = (ProtocolActionsBean)vecNotifyIRBActions.
                    get(tblNotifyAttachments.getSelectedRow());
            if(source.equals(btnAttachment)){
                tblNotifyAttachments.getCellEditor().stopCellEditing();
                try {
                    viewSubmissionDocument(protocolActionsBean);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if(source.equals(btnDescription)){
                //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
                //commentsForm.setData(protocolActionsBean.getComments());
                descriptionForm.setData(protocolActionsBean.getProtocolActionDocumentBean().getDescription());
                //COEUSDEV-328 : End
                descriptionForm.display();
            }
        }
    }
    
    /**
     * This class is used as the Table cell renderer for the table tblApprovalAttachments
     */
    public class OtherAttachTableCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblText;
        Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
        public OtherAttachTableCellRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if(value != null){
                lblText.setText(value.toString());
            }
            if(isSelected){
                lblText.setBackground(java.awt.Color.YELLOW);
            }else{
                lblText.setBackground(bgListColor);
            }
            return lblText;
        }
    }
    
    public ProtocolDetailForm getProtocolDetailForm() {
        return protocolDetailForm;
    }
    
    public void setProtocolDetailForm(ProtocolDetailForm protocolDetailForm) {
        this.protocolDetailForm = protocolDetailForm;
    }
    
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    public void setProtocolInfoBean(ProtocolInfoBean protocolInfoBean) {
        this.protocolInfoBean = protocolInfoBean;
    }
    
    public boolean isDataFetched() {
        return dataFetched;
    }
    //COEUSQA-1724-Added for new amendment/Renewals type-start
    /*This method is used to check the Protocol type is amendment protocol
     *return boolean value
     */
    boolean isProtocolAmendRenewalType(){
        boolean isProtocolAmendType = false;
        if(protocolInfoBean.getProtocolNumber().indexOf(CoeusConstants.IACUC_AMENDMENT)!=-1
          || protocolInfoBean.getProtocolNumber().indexOf(CoeusConstants.IACUC_RENEWAL)!=-1
          || protocolInfoBean.getProtocolNumber().indexOf(CoeusConstants.IACUC_RENEWAL_WITH_AMENDMENT)!=-1
          || protocolInfoBean.getProtocolNumber().indexOf(CoeusConstants.IACUC_CONTINUATION_CONTINUING_REVIEW)!=-1
          || protocolInfoBean.getProtocolNumber().indexOf(CoeusConstants.IACUC_CONTI_CONTINUING_REVIEW_WITH_AMEND)!=-1){
               isProtocolAmendType = true;
                }
        return isProtocolAmendType;
    }
    //COEUSQA-1724-Added for new amendment/Renewals type-end
    
     //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
     /*
      *  Class for Notification List HeaderColumn for sorting.
      */
    public class NotifyColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={    
            {"0","" },
            {"1","actionTypeDescription" },
            {"2","updateTimestamp" },
            {"3","actionDate"},
            {"4","protocolActionDocumentDescription"},{"5","" },
            
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
                if(vecNotifyIRBActions != null && vecNotifyIRBActions.size() > 0){
                    for(int index=0; index<vecNotifyIRBActions.size();index++){
                        cvListData.add(vecNotifyIRBActions.get(index));
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
                            vecNotifyIRBActions.set(index,cvListData.get(index));
                        }
                    }
                    notifyAttachmentTableModel.fireTableRowsUpdated(0, notifyAttachmentTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //Modified for Case COEUSQA-1724_COEUSQA-1724_ Implement validation based on rules in protocols_start
                //exception.getMessage();
                exception.printStackTrace();
                //Modified for Case COEUSQA-1724_COEUSQA-1724_ Implement validation based on rules in protocols_end
            }
        }
        
    }// End of ColumnHeaderListener.................
     /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionnaireId, name and description
     */
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
     /*
      *  Class for Other Attachments List HeaderColumn for sorting.
      */    
    public class OtherColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={    
            {"0","docType" },
            {"1","description" },
            {"2","updateTimestamp"},
        };
        boolean sort =true;
        /**
         * Mouse click event for column whose header was clicked
         * @param mouseEvent
         */
        public void mouseClicked(MouseEvent mouseEvent) {
            try {

                JTable table = ((JTableHeader)mouseEvent.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(mouseEvent.getX());
              
                if(cvOtherAttachments!=null && cvOtherAttachments.size()>0 &&
                        nameBeanId [vColIndex][1].length() >1 ){
                    (cvOtherAttachments).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                   
                   otherAttachmentTableModel.fireTableRowsUpdated(0, otherAttachmentTableModel.getRowCount());
                   
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
        
    }// End of ColumnHeaderListener
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNotifyAttachments = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOtherAtthmnts = new javax.swing.JTable();
        btnAdd = new edu.mit.coeus.utils.CoeusButton();
        btnModify = new edu.mit.coeus.utils.CoeusButton();
        btnDelete = new edu.mit.coeus.utils.CoeusButton();
        btnView = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attachments from Notifications", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 200));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 200));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(780, 170));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(780, 170));
        tblNotifyAttachments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblNotifyAttachments.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblNotifyAttachments);

        jPanel1.add(jScrollPane1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Other Attachments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        jPanel2.setMinimumSize(new java.awt.Dimension(800, 290));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 290));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(780, 240));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(780, 240));
        tblOtherAtthmnts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblOtherAtthmnts.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(tblOtherAtthmnts);

        jPanel2.add(jScrollPane2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jPanel2, gridBagConstraints);

        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        add(btnAdd, gridBagConstraints);

        btnModify.setMnemonic('o');
        btnModify.setText("Modify");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnModify, gridBagConstraints);

        btnDelete.setMnemonic('l');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnDelete, gridBagConstraints);

        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(btnView, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnAdd;
    public edu.mit.coeus.utils.CoeusButton btnDelete;
    public edu.mit.coeus.utils.CoeusButton btnModify;
    public edu.mit.coeus.utils.CoeusButton btnView;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable tblNotifyAttachments;
    public javax.swing.JTable tblOtherAtthmnts;
    // End of variables declaration//GEN-END:variables
    
}
