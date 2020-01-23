/*
 * ProtocolReviewerAttachmentsForm.java
 *
 * Created on April 7, 2011, 11:39 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 13-APR-2011
 * by Maharaja Palanichamy
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ReviewAttachmentsBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
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
import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * This class is used to view the reviewer 'Attachments' details of
 * ProtocolDetailForm
 *
 * @author  Maharaja Palanichamy
 */
public class ProtocolReviewerAttachmentsForm extends javax.swing.JPanel implements ActionListener,MouseListener {
    
    private CoeusDlgWindow coeusDlgWindow;
    private boolean modal;
    private Frame owner = CoeusGuiConstants.getMDIForm();
    private ProtocolSubmissionInfoBean submissionBean;
    private char functionType;
    private CoeusMessageResources coeusMessageResources;
        
    private ReviewerAttachmentTableModel reviewerAttachmentTableModel;
    private ReviewerAttachTableCellRenderer approvalTableCellRenderer;
    private EmptyHeaderRenderer emptyHeaderRenderer;
    
    private CoeusVector cvReviewerAttachments;
    private CoeusVector cvDocumentTypes;
   
    private int REVIEWER_NAME_COL = 0;
    private int REVIEWER_DESCRIPTION_COL = 1;
    private int PRIVATE_FLAG_COL = 2;
    private int LAST_UPDATED_COL = 3;
    private int UPDATED_BY_COL = 4;
    
    private int REVIEWER_NAME_COL_WIDTH = 100;
    private int REVIEWER_DESCRIPTION_COL_WIDTH = 240;
    private int PRIVATE_FLAG_COL_WIDTH = 60;
    private int LAST_UPDATED_COL_WIDTH = 160;
    private int UPDATED_BY_COL_WIDTH = 140;
    
    private static final int HEIGHT = 290;
    private static final int WIDTH = 800;
    
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String SCHEDULE_MAINTENENCE_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
    private static final char IACUC_GET_REVIEW_ATTACHMENTS = 'W';
    private static final char IACUC_GET_USER_PREVILEGES = 'X';
    
    private boolean dataFetched = false;
    /** Creates new form ProtocolReviewerAttachmentsForm */
    public ProtocolReviewerAttachmentsForm(boolean modal, ProtocolSubmissionInfoBean submissionBean) {
        this.modal = modal;
        this.submissionBean = submissionBean;
        initComponents();
        postInitComponents();
    }
    
    /**
     * Sets the properties for the gui components
     */
    public void postInitComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        Component components[] = {btnView,btnClose};
        setFocusCycleRoot(true);
        coeusDlgWindow = new CoeusDlgWindow(owner, modal);
        coeusDlgWindow.getContentPane().add(this);
        coeusDlgWindow.setSize(WIDTH, HEIGHT);
        coeusDlgWindow.setResizable(false);
        coeusDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        coeusDlgWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae ) {
                performCancelAction();
            }
        });
        
        reviewerAttachmentTableModel = new ReviewerAttachmentTableModel();
        approvalTableCellRenderer = new ReviewerAttachTableCellRenderer();
        emptyHeaderRenderer = new EmptyHeaderRenderer();
        
        tblReviewerAtthmnts.getTableHeader().setReorderingAllowed(false);
        tblReviewerAtthmnts.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblReviewerAtthmnts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReviewerAtthmnts.setRowHeight(20);
                
        reviewerAttachmentTableModel = new ReviewerAttachmentTableModel();
        tblReviewerAtthmnts.setModel(reviewerAttachmentTableModel);        
        btnView.addActionListener(this);
        btnClose.addActionListener(this);
    }
    
    /**
     * Enable/Disable the components in the gui according the value of enable
     * argument
     *
     * @param enable
     */
    public void enableComponents(boolean enable){
        
    }
    
    /**
     * Sets the table properties
     */
    public void setTableEditors(){
        TableColumn tableColumn = tblReviewerAtthmnts.getColumnModel().getColumn(REVIEWER_NAME_COL);
        JTableHeader reviewAttTableHeader = tblReviewerAtthmnts.getTableHeader();
        reviewAttTableHeader.addMouseListener(new ReviewerColumnHeaderListener());
                
        tableColumn = tblReviewerAtthmnts.getColumnModel().getColumn(REVIEWER_NAME_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(REVIEWER_NAME_COL_WIDTH);
        
        tableColumn = tblReviewerAtthmnts.getColumnModel().getColumn(REVIEWER_DESCRIPTION_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(REVIEWER_DESCRIPTION_COL_WIDTH);
        
        tableColumn = tblReviewerAtthmnts.getColumnModel().getColumn(PRIVATE_FLAG_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(PRIVATE_FLAG_COL_WIDTH);
         
        tableColumn = tblReviewerAtthmnts.getColumnModel().getColumn(LAST_UPDATED_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(LAST_UPDATED_COL_WIDTH);
        
        tableColumn = tblReviewerAtthmnts.getColumnModel().getColumn(UPDATED_BY_COL);
        tableColumn.setCellRenderer(approvalTableCellRenderer);
        tableColumn.setPreferredWidth(UPDATED_BY_COL_WIDTH);
    }
    
    /**
     * Sets the title
     */
    public void setTitle(String title) {
        coeusDlgWindow.setTitle(title);
    }
    
    /**
     * Sets the form data
     */
    public void setFormData(){
        //Set the function type as display mode if the the protocol is an
        //amendment or renewal
        functionType = TypeConstants.DISPLAY_MODE;
                
        if(functionType == TypeConstants.DISPLAY_MODE){
            enableComponents(false);
        }
        
        //To check whether the user has rights to view
        String userRights = userHasRightsToView();
        if(userRights!=null && (userRights.contains("VIEW") || userRights.contains("ADMIN"))){
             btnView.setEnabled(true);
        }else{
            btnView.setEnabled(false);
        }
        
        if(functionType == TypeConstants.DISPLAY_MODE){
            btnView.requestFocus();
        }
        //call to populate the data for reviewer attachments
        populateReviewerAttachments();
        setTableEditors();
        dataFetched = true;
        //to display the reviewer attachment details
        display();
    }
    
    /**
     * To close the form data
     */
    private void performCancelAction(){        
        coeusDlgWindow.setVisible(false);
    }
    
    /**
     * Populate data in the other attachments table
     */
    public void populateReviewerAttachments(){
        //To fetch the attachment details
        cvReviewerAttachments = getReviewerAttachments();
        reviewerAttachmentTableModel.fireTableDataChanged();
    }
    
    /**
     * To display the data in the form
     */
    public void display() {
        setTitle(CoeusGuiConstants.REVIEW_ATTACHMENTS_TITLE);
        coeusDlgWindow.setLocation(CoeusDlgWindow.CENTER);
        coeusDlgWindow.setVisible(true);
    }
    
    /**
     * Get the data for the other attachments from the database
     *
     * @return CoeusVector
     */
    private CoeusVector getReviewerAttachments(){
        CoeusVector cvReviewerAttachmentsFromDb = null;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(IACUC_GET_REVIEW_ATTACHMENTS);
        /*if(submissionBean != null){
            protocolNumber = submissionBean.getProtocolNumber();
            if(protocolNumber.indexOf('R')!=-1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf('R'));
            }else if(protocolNumber.indexOf('A') != -1){
                protocolNumber = protocolNumber.substring(0, protocolNumber.indexOf('A'));
            }
        }*/
        requesterBean.setDataObject(submissionBean);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(SCHEDULE_MAINTENENCE_SERVLET, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        try{
            if(response != null && response.hasResponse()){
                Vector vecServerData = response.getDataObjects();
                if(vecServerData != null && vecServerData.size() > 0){
                    Vector cvReviewerAttachments = (Vector)vecServerData.get(0);
                    cvReviewerAttachmentsFromDb = setValuesForAttachments(cvReviewerAttachments);
                }
            }
        }catch(CoeusException e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        return cvReviewerAttachmentsFromDb;
    }
    
    /**
     * Adds the details of attachments in Vector
     * @param protocolActionBean containg document details
     * @param protocolActionBean containg document details
     */
    private CoeusVector setValuesForAttachments(Vector cvReviewerAttachments){
        CoeusVector cvReviewerAttachmentsDb = new CoeusVector();
        for(Object attachmentData : cvReviewerAttachments){
            cvReviewerAttachmentsDb.add(attachmentData);
        }
        return cvReviewerAttachmentsDb;
    }
    
    /**
     * Opens the document for viewing
     * @param protocolActionBean containg document details
     */
   /* private void viewSubmissionDocument(ProtocolActionsBean protocolActionBean) throws Exception{
        String templateUrl =  null;
        DocumentBean documentBean = new DocumentBean();
        RequesterBean requesterBean = new RequesterBean();
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        protocolActionDocumentBean.setProtocolNumber(protocolActionBean.getProtocolNumber());
        protocolActionDocumentBean.setSequenceNumber(protocolActionBean.getSequenceNumber());
        protocolActionDocumentBean.setSubmissionNumber(protocolActionBean.getSubmissionNumber());
        protocolActionDocumentBean.setDocumentId(protocolActionBean.getProtocolActionDocumentBean().getDocumentId());
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "SUBMISSION_DOC_DB");
        map.put("PROTO_ACTION_BEAN", protocolActionDocumentBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.irb.ProtocolDocumentReader");
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
    }*/
      
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source.equals(btnView)){
            int rowCount = tblReviewerAtthmnts.getRowCount();
            if(rowCount == 0){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
                return;
            }
            int selectedRow = tblReviewerAtthmnts.getSelectedRow();
            if(selectedRow == -1){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
                return;
            }else{
                ReviewAttachmentsBean reviewAttachmentsBean = (ReviewAttachmentsBean)cvReviewerAttachments.get(selectedRow);
                viewDocument(reviewAttachmentsBean);
            }
        }else if(source.equals(btnClose)){
            performCancelAction();
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
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException If any exception occurs
     */
    public void viewDocument(ReviewAttachmentsBean reviewAttachmentsBean){
        try{
            String url = getDocumentURL(reviewAttachmentsBean);
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
    public String getDocumentURL(ReviewAttachmentsBean reviewAttachmentsBean) throws CoeusException{
        String templateUrl =  null;
        RequesterBean requester = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("PROTO_REVIEW_ATTACH_BEAN", reviewAttachmentsBean);
        map.put("DOCUMENT_TYPE", "IACUC_REVIEW_ATTACHMENT_DOC");
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
     * This class is used as the Table Model for the table tblApprovalAttachments
     */
    public class ReviewerAttachmentTableModel extends AbstractTableModel{
        
        private Class columnClass[] = {String.class,String.class,Object.class,String.class,String.class};
        private String colNames[] = {"Reviewer","Description","Private","Last Updated","Updated By"};
        
        public Object getValueAt(int row, int col){
            ReviewAttachmentsBean reviewAttachmentsBean = (ReviewAttachmentsBean)cvReviewerAttachments.get(row);
            if(col == REVIEWER_NAME_COL){
                return reviewAttachmentsBean.getReviewerName();
            }else if(col == REVIEWER_DESCRIPTION_COL){
                return reviewAttachmentsBean.getDescription();
            }else if(col == PRIVATE_FLAG_COL){
                boolean privateFlag = false;
                if(reviewAttachmentsBean.getPrivateAttachmentFlag().equalsIgnoreCase("Y")){
                    privateFlag = true;
                }else{
                    privateFlag = false;
                }
                return privateFlag;
            }else if(col == LAST_UPDATED_COL){
                return CoeusDateFormat.format(reviewAttachmentsBean.getUpdateTimestamp().toString());
            }else if(col == UPDATED_BY_COL){
                return reviewAttachmentsBean.getUpdateUser();
            }
            return "";
        }
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount(){
            if(cvReviewerAttachments!=null){
                return cvReviewerAttachments.size();
            }else{
                return 0;
            }
        }
        
        public String getColumnName(int col) {
            return colNames[col];
        }
        
        public Class getColumnClass(int col) {
            return columnClass[col];
        }
        
        public void setDataVector(Vector newData) {
            
        }
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
    }
    
    /**
     * This class is used as the Table cell renderer for the table tblApprovalAttachments
     */
    public class ReviewerAttachTableCellRenderer extends DefaultTableCellRenderer{
        private JLabel lblText;
        private JCheckBox chkPrivateFlag;
        Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        
        public ReviewerAttachTableCellRenderer(){
            lblText = new JLabel();
            lblText.setOpaque(true);
            chkPrivateFlag = new JCheckBox();
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if(value != null && column != PRIVATE_FLAG_COL){
                lblText.setText(value.toString());
            }else{
                chkPrivateFlag.setSelected(new Boolean(value.toString()));
            }
            if(isSelected){
                lblText.setBackground(java.awt.Color.YELLOW);
                chkPrivateFlag.setBackground(java.awt.Color.YELLOW);
            }else{
                lblText.setBackground(bgListColor);
                chkPrivateFlag.setBackground(bgListColor);
                chkPrivateFlag.setHorizontalAlignment(CENTER);
            }
            if(column != PRIVATE_FLAG_COL){
               return lblText;
            }else{
               return chkPrivateFlag; 
            }
        }
    }    
    
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    public void setProtocolInfoBean(ProtocolSubmissionInfoBean submissionBean) {
        this.submissionBean = submissionBean;
    }
    
    public boolean isDataFetched() {
        return dataFetched;
    }
     
    /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only questionnaireId, name and description
     */
    public class ReviewerColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={    
            {"0","reviewerName" },
            {"1","description" },
            {"2","privateAttachmentFlag"},
            {"3","updateTimestamp"},
            {"4","updateuser"},
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
              
                if(cvReviewerAttachments!=null && cvReviewerAttachments.size()>0 &&
                        nameBeanId [vColIndex][1].length() >1 ){
                    (cvReviewerAttachments).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                   
                   reviewerAttachmentTableModel.fireTableRowsUpdated(0, reviewerAttachmentTableModel.getRowCount());
                   
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
        
    }// End of ColumnHeaderListener.................
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">                          
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        tblReviewerAtthmnts = new javax.swing.JTable();
        
        btnView = new edu.mit.coeus.utils.CoeusButton();
        btnClose = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(700, 240));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(700, 240));
        tblReviewerAtthmnts.setModel(new javax.swing.table.DefaultTableModel(
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
        tblReviewerAtthmnts.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(tblReviewerAtthmnts);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());
        
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(btnView,gridBagConstraints);
        
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel1.add(btnClose,gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>                        

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

    //For testing purpose only
    public static void main(String s[]){
        Frame frame = new Frame();
        ProtocolReviewerAttachmentsForm form = new ProtocolReviewerAttachmentsForm(true, null);
        form.setFormData();
    }

    /**
     * Method to check whether the user has the rights to view
     * @return String
     */
    private String userHasRightsToView() {
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType(IACUC_GET_USER_PREVILEGES);
        requesterBean.setDataObject(submissionBean);
        String userHasRights = null;
        AppletServletCommunicator comm = new AppletServletCommunicator(SCHEDULE_MAINTENENCE_SERVLET, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        try{
            if(response != null && response.hasResponse()){
                userHasRights = (String)response.getDataObject();                
            }
        }catch(CoeusException e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        return userHasRights;
    }
    
    // Variables declaration - do not modify                     
    public edu.mit.coeus.utils.CoeusButton btnView;
    public edu.mit.coeus.utils.CoeusButton btnClose;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tblReviewerAtthmnts;
    // End of variables declaration                   
}
