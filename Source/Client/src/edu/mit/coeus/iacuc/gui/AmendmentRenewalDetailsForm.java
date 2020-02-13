/*
 * @(#)AmendmentRenewalDetailsForm.java November 16, 2009, 12:23 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AmendmentRenewalDetailsForm.java
 *
 * Created on November 16, 2009, 12:23 PM
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.iacuc.bean.IrbWindowConstants;
import edu.mit.coeus.iacuc.bean.ProtocolAmendRenewalBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.controller.QuestionAnswersController;
import edu.mit.coeus.questionnaire.gui.QuestionAnswersForm;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusLabel;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * @author satheeshkumarkn
 */
public class AmendmentRenewalDetailsForm extends javax.swing.JPanel  {
    private ProtocolAmendRenewalBean amendRenewalBean;
    private ProtocolDocumentTableModel protocolDocumentTableModel;
    private ProtocolDocumentTableRenderer protocolDocumentTableRenderer;
    
    private DateUtils dtUtils = new DateUtils();
    private static final int TYPE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final int LAST_UPDATED_COLUMN = 2;
    private static final int UPDATED_BY_COLUMN = 3;
    private static final int TYPE_COLUMN_SIZE= 110;
    private static final int DESCRIPTION_COLUMN_SIZE = 225;
    private static final int LAST_UPDATED_COLUMN_SIZE = 130;
    private static final int UPDATED_BY_COLUMN_SIZE = 130;
    private static final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String UPLOAD_DOC_BEAN = "UPLOAD_DOC_BEAN";
    private static final char AMEND_RENEW_DETAILS = '9';
    private static final int DOCUMENTS_DETAILS_IN_VECTOR = 0;
    private static final int PROTO_AMEND_RENEW_EDITABLE_MODULES = 1;
    private static final int PROTOCOL_MODULE = 7;
    private static final int AMENDMENT_RENEWAL_SUB_MODULE = 1;
    private static final String AMENDMENT_SEQUENCE_NUMBER = "1";
    private Vector vcModuleDetails;
    private Vector vcDocumentsDetails;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private CoeusDlgWindow parent;
    private QuestionAnswersForm questionAnswersForm;
    private CoeusDlgWindow dlgProtoSubQuestionnaire;
    private String parentDialogTitle;
    /**
     * Creates new customizer AmendmentRenewalDetailsForm
     */
    public AmendmentRenewalDetailsForm(ProtocolAmendRenewalBean amendRenewalBean, CoeusDlgWindow parent)  throws CoeusException{
        this.parent = parent;
        parentDialogTitle = this.parent.getTitle();
        this.amendRenewalBean = amendRenewalBean;
        initComponents();
        //Changes the details panel title to 'Renewal Details' when the protocol is Renewal
        if(amendRenewalBean != null && amendRenewalBean.getProtocolAmendRenewalNumber()!= null &&
                amendRenewalBean.getProtocolAmendRenewalNumber().indexOf("R") > -1){
            String detailsPnlTitle = "Renewal Details";
            pnlAmendRenewDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, 
                    detailsPnlTitle, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                    CoeusFontFactory.getLabelFont()));
        }
        registerComponents();
        setFormData();
    }
    
    /*
     * Method to set data to all the forms
     */
    public void setFormData() throws CoeusException{
        setTableHeaders();
        txtProtocolNumber.setText(amendRenewalBean.getProtocolAmendRenewalNumber());
        txtCreationDate.setText(dtUtils.formatDate(
                amendRenewalBean.getDateCreated().toString(),"dd-MMM-yyyy"));
        txtApprovedDate.setText(dtUtils.formatDate(
                amendRenewalBean.getUpdateTimestamp().toString(),"dd-MMM-yyyy"));
        txtSummary.setText(amendRenewalBean.getSummary());
        getAmendRenewDetails();
        if(vcModuleDetails != null && vcModuleDetails.size()>0){
            for(int index=0;index<vcModuleDetails.size();index++){
                FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
                pnlModules.setLayout(flowLayout);
                java.awt.Label moduleLabel = new java.awt.Label();
                Dimension size = new Dimension(150, 15);
                moduleLabel.setMaximumSize(size);
                moduleLabel.setMinimumSize(size);
                moduleLabel.setPreferredSize(size);
                moduleLabel.setText(getModuleLabel((String)vcModuleDetails.get(index)));
                pnlModules.add(moduleLabel);
            }
            
        }
        protocolDocumentTableModel.setData(vcDocumentsDetails);
        //Diabling the document view button when there is no document
        if(vcDocumentsDetails == null || (vcDocumentsDetails != null && vcDocumentsDetails.size() < 1)){
            btnAttachmentView.setEnabled(false);
        }
    }

    /**
     * Method to register table Model and Listener
     * @throws CoeusException 
     */
    private void registerComponents() throws CoeusException {
        btnAttachmentView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try{
                    performDocumentViewAction();
                }catch(CoeusException ce){
                    CoeusOptionPane.showErrorDialog(ce.getMessage());
                    ce.printStackTrace();
                }
            }
        });
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.dispose();
            }
        });
        btnQuestionnaire.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showQuestionnaireWindow();
            }
        });
        protocolDocumentTableModel = new ProtocolDocumentTableModel();
        protocolDocumentTableRenderer = new ProtocolDocumentTableRenderer();
        tblDocuments.setModel(protocolDocumentTableModel);
    }
    
    /**
     * Method to get the module label based on the module code
     * @param moduleCode
     * @return moduleLabel
     */
    private String getModuleLabel(String moduleCode){
        String moduleLabel = "";
        if(moduleCode != null){
            if(moduleCode.equals(IrbWindowConstants.GENERAL_INFO)){
                moduleLabel = IrbWindowConstants.GENERAL_INFO_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.ORGANIZATION)){
                moduleLabel = IrbWindowConstants.ORGANIZATION_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.INVESTIGATOR)){
                moduleLabel = IrbWindowConstants.INVESTIGATOR_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.KEY_STUDY_PERSONS)){
                moduleLabel = IrbWindowConstants.KEY_STUDY_PERSONS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.CORRESPONDENTS)){
                moduleLabel = IrbWindowConstants.CORRESPONDENTS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.AREA_OF_RESEARCH)){
                moduleLabel = IrbWindowConstants.AREA_OF_RESEARCH_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.FUNDING_SOURCE)){
                moduleLabel = IrbWindowConstants.FUNDING_SOURCE_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.SUBJECTS)){
                moduleLabel = IrbWindowConstants.SUBJECTS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.SPECIAL_REVIEW)){
                moduleLabel = IrbWindowConstants.SPECIAL_REVIEW_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.IDENTIFIERS)){
                moduleLabel = IrbWindowConstants.IDENTIFIERS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.UPLOAD_DOCUMENTS)){
                moduleLabel = IrbWindowConstants.UPLOAD_DOCUMENTS_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.OTHERS)){
                moduleLabel = IrbWindowConstants.OTHERS_LABEL;       
            }else if(moduleCode.equals(IrbWindowConstants.UPLOAD_OTHER_DOCUMENTS)){
                moduleLabel = IrbWindowConstants.UPLOAD_OTHER_DOCUMENTS_LABEL;            
//            }else if(moduleCode.equals(IrbWindowConstants.SPECIES)){
//                moduleLabel = IrbWindowConstants.SPECIES_LABEL;
//            }else if(moduleCode.equals(IrbWindowConstants.STUDY_GROUP)){
//                moduleLabel = IrbWindowConstants.STUDY_GROUP_LABEL;
            }else if(moduleCode.equals(IrbWindowConstants.SPECIES_STUDY_GROUP)){
                moduleLabel = IrbWindowConstants.SPECIES_STUDY_GROUP_LABEL; 
            }else if(moduleCode.equals(IrbWindowConstants.SCIENTIFIC_JUSTIFICATION)){
                moduleLabel = IrbWindowConstants.SCIENTIFIC_JUSTIFICATION_LABEL;            
            }else if(moduleCode.equals(IrbWindowConstants.ALTERNATIVE_SEARCH)){
                moduleLabel = IrbWindowConstants.ALTERNATIVE_SEARCH_LABEL;            
            }
        }  
        return moduleLabel;
    }
    
    /**
     * Method to set the amendment/renewal attachments and questionnaire headers
     * @throws CoeusException
     */
    private void setTableHeaders() throws CoeusException {
        JTableHeader tblAttachmentHeader = tblDocuments.getTableHeader();
        tblAttachmentHeader.setReorderingAllowed(false);
        tblAttachmentHeader.setMaximumSize(new Dimension(100, 25));
        tblAttachmentHeader.setMinimumSize(new Dimension(100, 25));
        tblAttachmentHeader.setPreferredSize(new Dimension(100, 25));
        tblAttachmentHeader.setFont(CoeusFontFactory.getLabelFont());
        tblDocuments.setRowHeight(22);
        tblDocuments.setShowHorizontalLines(true);
        tblDocuments.setShowVerticalLines(true);
        tblDocuments.setOpaque(false);
        tblDocuments.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        tblDocuments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn attcahmentColumnDetails;
        int []size = {TYPE_COLUMN_SIZE,DESCRIPTION_COLUMN_SIZE,
        LAST_UPDATED_COLUMN_SIZE,UPDATED_BY_COLUMN_SIZE};
        for(int index = 0; index < size.length; index++) {
            attcahmentColumnDetails = tblDocuments.getColumnModel().getColumn(index);
            attcahmentColumnDetails.setPreferredWidth(size[index]);
            if(index == TYPE_COLUMN) {
                attcahmentColumnDetails.setMinWidth(110);
            }else if(index == DESCRIPTION_COLUMN) {
                attcahmentColumnDetails.setMinWidth(225);
            }else if(index == LAST_UPDATED_COLUMN) {
                attcahmentColumnDetails.setMinWidth(130);
            }else if(index == UPDATED_BY_COLUMN) {
                attcahmentColumnDetails.setMinWidth(130);
            }
            attcahmentColumnDetails.setCellRenderer(protocolDocumentTableRenderer);
        }
    }
    
    private class ProtocolDocumentTableModel extends AbstractTableModel{
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
                UploadDocumentBean uploadDocBean
                        = (UploadDocumentBean)vecListData.get(rowIndex);
                switch(columnIndex){
                    case TYPE_COLUMN:
                        return uploadDocBean.getDocType();
                    case DESCRIPTION_COLUMN:
                        return uploadDocBean.getDescription();
                    case LAST_UPDATED_COLUMN:
                        return CoeusDateFormat.format(uploadDocBean.getUpdateTimestamp().toString());
                    case UPDATED_BY_COLUMN:
                        return uploadDocBean.getUpdateUserName();
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
    }
    
    /**
     * Class for Upload Document renderer
     */
    private class ProtocolDocumentTableRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        
        /**
         * contruttor for Upload Document renderer
         */
        public ProtocolDocumentTableRenderer(){
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
                    value = (value==null ? CoeusGuiConstants.EMPTY_STRING : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                    
            }
            return lblComponent;
        }
    }
    
    /*
     * Method to get editable module details and documents
     */
    private void getAmendRenewDetails(){
        RequesterBean requester = new RequesterBean();
        requester.setDataObject(amendRenewalBean);
        requester.setFunctionType(AMEND_RENEW_DETAILS);
        String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response != null) {
            if (response.isSuccessfulResponse()) {
                Vector vcAmendRenewDetails = response.getDataObjects();
                vcDocumentsDetails = (Vector)vcAmendRenewDetails.get(DOCUMENTS_DETAILS_IN_VECTOR);
                vcModuleDetails = (Vector)vcAmendRenewDetails.get(PROTO_AMEND_RENEW_EDITABLE_MODULES);
            }
        }
    }
    
    /**
     * Method to perform view button operation
     * @throws CoeusException
     */
    private void performDocumentViewAction() throws CoeusException{
        int rowCount = tblDocuments.getRowCount();
        if(rowCount == 0){
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1004"));
            return;
        }
        int selectedRow = tblDocuments.getSelectedRow();
        if(selectedRow == -1){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1003"));
            return;
        }
        UploadDocumentBean uploadDocumentBean
                = (UploadDocumentBean)vcDocumentsDetails.get(selectedRow);
        viewDocument(uploadDocumentBean);
    }
    
    /*
     * Method to show questionnaire details of the amendment
     */
    private void showQuestionnaireWindow(){
        try{
            QuestionAnswersController questionAnswersController = new QuestionAnswersController(amendRenewalBean.getProtocolAmendRenewalNumber(),true);
            questionAnswersController.setFunctionType(TypeConstants.DISPLAY_MODE);
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
            questionnaireAnswerHeaderBean.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
            questionnaireAnswerHeaderBean.setModuleSubItemCode(AMENDMENT_RENEWAL_SUB_MODULE);
            questionnaireAnswerHeaderBean.setModuleItemKey(amendRenewalBean.getProtocolAmendRenewalNumber());
            //ModuleSubItemKey - sequence number for the amendment/renewal will always be '1'
            questionnaireAnswerHeaderBean.setModuleSubItemKey(AMENDMENT_SEQUENCE_NUMBER);
            questionAnswersController.setFormData(questionnaireAnswerHeaderBean);
            CoeusVector cvFormData = (CoeusVector)questionAnswersController.getFormData();
            questionAnswersForm = ((QuestionAnswersForm)questionAnswersController.getControlledUI());
            questionAnswersForm.setFocusable(true);
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    questionAnswersForm.requestFocusInWindow();
                }
            });
            questionAnswersForm.btnClose.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dlgProtoSubQuestionnaire.dispose();
                }
            });
            
            if(cvFormData != null && cvFormData.size() > 0){
                String title = parentDialogTitle.replaceAll("Details","Questionnaire");
                dlgProtoSubQuestionnaire= new CoeusDlgWindow(parent, title, true);
                dlgProtoSubQuestionnaire.setFocusable(true);
                dlgProtoSubQuestionnaire.setEnabled(true);
                questionAnswersForm.btnModify.setVisible(false);
                questionAnswersForm.btnStartOver.setVisible(false);
                questionAnswersForm.btnSaveAndProceed.setVisible(false);
                questionAnswersForm.btnGoBack.setVisible(false);
                dlgProtoSubQuestionnaire.addEscapeKeyListener(new AbstractAction("escPressed"){
                    public void actionPerformed(ActionEvent ae){
                            dlgProtoSubQuestionnaire.dispose();
                    }
                });
                
                dlgProtoSubQuestionnaire.addWindowListener( new WindowAdapter() {
                    public void windowClosing(WindowEvent we){
                            dlgProtoSubQuestionnaire.dispose();
                    }
                });
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 6;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.insets = new Insets(15, 10, 530, 2);
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
                Dimension closeBtnSize = new Dimension(80,22);
                questionAnswersForm.btnClose.setMaximumSize(closeBtnSize);
                questionAnswersForm.btnClose.setMinimumSize(closeBtnSize);
                questionAnswersForm.btnClose.setPreferredSize(closeBtnSize);
                questionAnswersForm.pnlQuestionsAnswers.add(questionAnswersForm.btnClose,gridBagConstraints);
                dlgProtoSubQuestionnaire.setResizable(false);
                dlgProtoSubQuestionnaire.getContentPane().add(questionAnswersController.getControlledUI());
                dlgProtoSubQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
                dlgProtoSubQuestionnaire.setSize(990, 600);
                
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension dlgSize = dlgProtoSubQuestionnaire.getSize();
                dlgProtoSubQuestionnaire.setLocation(screenSize.width/2 - (dlgSize.width/2),
                        screenSize.height/2 - (dlgSize.height/2));
                dlgProtoSubQuestionnaire.show();
            }else{
                String message = "";
                MessageFormat formatter = new MessageFormat("");
                message = formatter.format(coeusMessageResources.parseMessageKey("protocoSubmissionQuestions_exceptionCode.1003"),amendRenewalBean.getProtocolAmendRenewalNumber());
                CoeusOptionPane.showWarningDialog(message);
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException If any exception occurs
     */
    public void viewDocument(UploadDocumentBean uploadDocumentBean) throws CoeusException{
        String url = getDocumentURL(uploadDocumentBean);
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
     * Method to get the document url with file contents
     * @param uploadDocumentBean bean containing data for view document
     * @throws CoeusException
     * @return url of view document
     */
    public String getDocumentURL(UploadDocumentBean uploadDocumentBean) throws CoeusException{
        String templateUrl =  null;
        RequesterBean requester = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put(UPLOAD_DOC_BEAN, uploadDocumentBean);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requester.setDataObject(documentBean);
        requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        AppletServletCommunicator comm = new AppletServletCommunicator(STREMING_SERVLET, requester);
        comm.send();
        ResponderBean responder = comm.getResponse();
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        map = (Map)responder.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
        return templateUrl;
    }
    
   
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlAmendRenewDetails = new javax.swing.JPanel();
        lblProtocolNumber = new javax.swing.JLabel();
        txtProtocolNumber = new javax.swing.JTextField();
        lblCreationDate = new javax.swing.JLabel();
        txtCreationDate = new javax.swing.JTextField();
        lblApprovedDate = new javax.swing.JLabel();
        txtApprovedDate = new javax.swing.JTextField();
        lblSummary = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSummary = new javax.swing.JTextPane();
        pnlModules = new javax.swing.JPanel();
        pnlAttachments = new javax.swing.JPanel();
        scrPnParent1 = new javax.swing.JScrollPane();
        tblDocuments = new javax.swing.JTable();
        btnAttachmentView = new javax.swing.JButton();
        btnQuestionnaire = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(100, 40));
        setPreferredSize(new java.awt.Dimension(800, 500));
        pnlAmendRenewDetails.setLayout(new java.awt.GridBagLayout());

        pnlAmendRenewDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Amendment Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlAmendRenewDetails.setPreferredSize(new java.awt.Dimension(650, 125));
        lblProtocolNumber.setFont(CoeusFontFactory.getLabelFont());
        lblProtocolNumber.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProtocolNumber.setText("Protocol Number : ");
        lblProtocolNumber.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 1);
        pnlAmendRenewDetails.add(lblProtocolNumber, gridBagConstraints);

        txtProtocolNumber.setEditable(false);
        txtProtocolNumber.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtProtocolNumber.setEnabled(false);
        txtProtocolNumber.setOpaque(false);
        txtProtocolNumber.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlAmendRenewDetails.add(txtProtocolNumber, gridBagConstraints);

        lblCreationDate.setFont(CoeusFontFactory.getLabelFont());
        lblCreationDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCreationDate.setText("Created Date : ");
        lblCreationDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 1);
        pnlAmendRenewDetails.add(lblCreationDate, gridBagConstraints);

        txtCreationDate.setEditable(false);
        txtCreationDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCreationDate.setEnabled(false);
        txtCreationDate.setOpaque(false);
        txtCreationDate.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 9);
        pnlAmendRenewDetails.add(txtCreationDate, gridBagConstraints);

        lblApprovedDate.setFont(CoeusFontFactory.getLabelFont());
        lblApprovedDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblApprovedDate.setText("Approved Date : ");
        lblApprovedDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 1);
        pnlAmendRenewDetails.add(lblApprovedDate, gridBagConstraints);

        txtApprovedDate.setEditable(false);
        txtApprovedDate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtApprovedDate.setEnabled(false);
        txtApprovedDate.setOpaque(false);
        txtApprovedDate.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlAmendRenewDetails.add(txtApprovedDate, gridBagConstraints);

        lblSummary.setFont(CoeusFontFactory.getLabelFont());
        lblSummary.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSummary.setText("Summary : ");
        lblSummary.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblSummary.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSummary.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 39, 0);
        pnlAmendRenewDetails.add(lblSummary, gridBagConstraints);

        jScrollPane3.setEnabled(false);
        jScrollPane3.setRequestFocusEnabled(false);
        txtSummary.setEditable(false);
        txtSummary.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSummary.setEnabled(false);
        txtSummary.setOpaque(false);
        txtSummary.setPreferredSize(new java.awt.Dimension(6, 60));
        jScrollPane3.setViewportView(txtSummary);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        pnlAmendRenewDetails.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(pnlAmendRenewDetails, gridBagConstraints);

        pnlModules.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Edited Modules", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlModules.setPreferredSize(new java.awt.Dimension(650, 125));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlModules, gridBagConstraints);

        pnlAttachments.setLayout(new java.awt.GridBagLayout());

        pnlAttachments.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attachments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        pnlAttachments.setMinimumSize(new java.awt.Dimension(646, 130));
        pnlAttachments.setPreferredSize(new java.awt.Dimension(650, 200));
        scrPnParent1.setMaximumSize(new java.awt.Dimension(630, 190));
        scrPnParent1.setMinimumSize(new java.awt.Dimension(630, 170));
        scrPnParent1.setPreferredSize(new java.awt.Dimension(630, 190));
        tblDocuments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {}
            },
            new String [] {

            }
        ));
        tblDocuments.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        scrPnParent1.setViewportView(tblDocuments);

        pnlAttachments.add(scrPnParent1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlAttachments, gridBagConstraints);

        btnAttachmentView.setFont(CoeusFontFactory.getLabelFont());
        btnAttachmentView.setText("View");
        btnAttachmentView.setMaximumSize(new java.awt.Dimension(110, 22));
        btnAttachmentView.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 130, 0);
        add(btnAttachmentView, gridBagConstraints);

        btnQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
        btnQuestionnaire.setMnemonic('Q');
        btnQuestionnaire.setText("Questionnaire");
        btnQuestionnaire.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 9, 0, 0);
        add(btnQuestionnaire, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 9, 0, 0);
        add(btnClose, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAttachmentView;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnQuestionnaire;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblApprovedDate;
    private javax.swing.JLabel lblCreationDate;
    private javax.swing.JLabel lblProtocolNumber;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JPanel pnlAmendRenewDetails;
    private javax.swing.JPanel pnlAttachments;
    private javax.swing.JPanel pnlModules;
    private javax.swing.JScrollPane scrPnParent1;
    private javax.swing.JTable tblDocuments;
    private javax.swing.JTextField txtApprovedDate;
    private javax.swing.JTextField txtCreationDate;
    private javax.swing.JTextField txtProtocolNumber;
    private javax.swing.JTextPane txtSummary;
    // End of variables declaration//GEN-END:variables
    
}
