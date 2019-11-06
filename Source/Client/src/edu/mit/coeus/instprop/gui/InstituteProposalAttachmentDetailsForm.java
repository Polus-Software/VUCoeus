/*
 * InstituteProposalAttachmentDetailsForm.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on January 24, 2010, 8:50 AM
 */

package edu.mit.coeus.instprop.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.URLOpener;
import edu.mit.coeus.instprop.bean.InstituteProposalAttachmentBean;
import edu.mit.coeus.instprop.bean.InstituteProposalAttachmentTypeBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.BaseWindowObservable;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author  satheeshkumarkn
 */
public class InstituteProposalAttachmentDetailsForm extends JPanel implements ActionListener{
    
    private InstituteProposalAttachmentBean attachmentBean;
    private Vector vecAttachmentTypes;
    private char functionType;
    private CoeusDlgWindow parent;
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final String GET_SERVLET = "/InstituteProposalMaintenanceServlet";
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private static final char UPDATE_ATTACHMENT = 'u';
    private BaseWindowObservable observable = new BaseWindowObservable();
    private boolean saveRequired = false;
    private CoeusVector cvAttachments;
    private CoeusMessageResources coeusMessageResources;
    private static final String ATTACHMENT_TYPE_EXISTS = "instPropAttachment_exceptionCode.1602";
    private boolean isDocumentUploaded = false;
    /**
     * Creates new form InstituteProposalAttachmentDetailsForm
     */
    public InstituteProposalAttachmentDetailsForm(InstituteProposalAttachmentBean attachmentBean,
            CoeusDlgWindow parentComponent) {
        this.attachmentBean = attachmentBean;
        this.parent = parentComponent;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        setFocusTraversal();
    }
    
    /*
     * Method to set the form data
     */
    public void setFormData(){
        lblProposalValue.setText(this.attachmentBean.getProposalNumber());
        txtSequenceNo.setText(
                this.attachmentBean.getSequenceNumber()+CoeusGuiConstants.EMPTY_STRING);
        txtAttachmentNumber.setText(
                this.attachmentBean.getAttachmentNumber()+CoeusGuiConstants.EMPTY_STRING);
        if(functionType == TypeConstants.MODIFY_MODE){
            txtArComments.setText(this.attachmentBean.getComments());
            txtArTitle.setText(this.attachmentBean.getAttachmentTitle());
            txtContactName.setText(this.attachmentBean.getContactName());
            txtEmailAddress.setText(this.attachmentBean.getEmailAddress());
            txtFileName.setText(this.attachmentBean.getFileName());
            txtPhoneNumber.setText(this.attachmentBean.getPhoneNumber());
        }
        //Creating ComboBoxBean for all the attachment types
        Vector vecAttachment = new Vector();
        if(this.vecAttachmentTypes != null && this.vecAttachmentTypes.size() > 0){
            for(int index = 0;index < this.vecAttachmentTypes.size();index++){
                InstituteProposalAttachmentTypeBean attachmentTypeBean =
                        (InstituteProposalAttachmentTypeBean)this.vecAttachmentTypes.get(index);
                vecAttachment.addElement(new ComboBoxBean(
                        new Integer(attachmentTypeBean.getAttachmentTypeCode()).toString(),
                        attachmentTypeBean.getDescription()));
            }
        }
        
        //Setting the attacment type combox box item selection
        if(cmbAttachmentType.getItemCount() == 0 && vecAttachment != null && vecAttachment.size()>0){
            for( int index  = 0; index < vecAttachment.size(); index++) {
                cmbAttachmentType.addItem( (ComboBoxBean)vecAttachment.elementAt(index) );
            }
            if(functionType == TypeConstants.ADD_MODE){
                cmbAttachmentType.setSelectedIndex(0);
            }else{
                if(this.vecAttachmentTypes != null && this.vecAttachmentTypes.size() > 0){
                    for(int index = 0;index < this.vecAttachmentTypes.size();index++){
                        InstituteProposalAttachmentTypeBean attachmentTypeBean =
                                (InstituteProposalAttachmentTypeBean)this.vecAttachmentTypes.get(index);
                        if(attachmentTypeBean.getAttachmentTypeCode() == this.attachmentBean.getAttachmentTypeCode()){
                            cmbAttachmentType.setSelectedIndex(index);
                            break;
                        }
                    }
                }
                
            }
        }
        //sets the file name in the file name text field
        if(attachmentBean.getFileName() != null && !attachmentBean.getFileName().equals("")){
            txtFileName.setText(attachmentBean.getFileName());
            btnView.setEnabled(true);
        }else{
            btnView.setEnabled(false);
        }
    }
    
    /*
     * Method to set the focus when the window is opened
     */
    public void setInitialFocus(){
        txtArTitle.setCaretPosition(0);
        txtArTitle.requestFocus();
        
    }
    
    /*
     * Method to set the focus traversal policy
     */
    private void setFocusTraversal(){
        Component[] comp = {cmbAttachmentType,txtArTitle,txtContactName,txtPhoneNumber,
        txtEmailAddress,txtArComments,btnOk,btnCancel,btnUpload, btnView};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        pnlMain.setFocusTraversalPolicy(traversal);
        pnlMain.setFocusCycleRoot(true);
        txtArTitle.requestFocus();
    }
    
    /*
     * Method to register all the components in the window
     */
    private void registerComponents(){
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        btnUpload.addActionListener(this);
        btnView.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if(source.equals(btnOk)){
            try {
                if(validateAttachmentDetails()){
                    boolean canDispose = saveAttachmentDetails();
                    if(canDispose){
                        parent.dispose();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if(source.equals(btnCancel)){
            validateData();
        }else if(source.equals(btnUpload)){
            uploadDocument();
        }else if(source.equals(btnView)){
            try {
                viewDocument();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /*
     * Method to upload the document
     */
    private void uploadDocument(){
        String extension;
        CoeusFileChooser fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
            String fileName = fileChooser.getSelectedFile();
            File file = fileChooser.getFileName();
            //Modified for COEUSQA-3690	Coeus4.5: Institute Proposal Attachments File Name Display Issue - Start
            //Displaying the entire path name instead of just the file name and type extension.
//            String path = file.getAbsolutePath();
            String path = file.getName();
            //Modified for COEUSQA-3690 -End
            attachmentBean.setFileName(path);
            txtFileName.setText(path);
            attachmentBean.setFileBytes(fileChooser.getFile());
            CoeusDocumentUtils docUtils = CoeusDocumentUtils.getInstance();
            String mimeType = docUtils.getDocumentMimeType(attachmentBean);
            attachmentBean.setMimeType(mimeType);
            if(fileName != null && !fileName.trim().equals("")){
                int index = fileName.lastIndexOf('.');
                if(index != -1 && index != fileName.length()){
                    extension = fileName.substring(index+1, fileName.length());
                    if(extension != null){
                        attachmentBean.setFileBytes(fileChooser.getFile());
                    }
                }
            }
            btnView.setEnabled(true);
            isDocumentUploaded = true;
        }
    }
    
    private void viewDocument() throws Exception{
        Map map = new HashMap();
        if(txtFileName.getText() != null && !CoeusGuiConstants.EMPTY_STRING.equals(txtFileName.getText())){
            Vector dataObjects = new Vector();
            if(isDocumentUploaded){
                map.put("VIEW_DOCUMENT", new Boolean(true));
            }else{
                map.put("VIEW_DOCUMENT", new Boolean(false));
            }
            //COEUSQA:3589 - Institute Proposal Attachments can't be fetched from Modify IP Attachment Window
            attachmentBean.setSequenceNumber(attachmentBean.getSequenceNumber());
            //COEUSQA:3589 - End
            dataObjects.add(0, attachmentBean);
            dataObjects.add(1, CoeusGuiConstants.getMDIForm().getUnitNumber());
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            map.put("DATA", dataObjects);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.instprop.InstituteProposalDocumentReader");
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            AppletServletCommunicator comm
                    = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/');
                URL urlObj = new URL(reportUrl);
                URLOpener.openUrl(urlObj);
            }else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
            }
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("proposal_narr_exceptionCode.6606"));
            return ;
        }
    }
    
    public boolean saveAttachmentDetails() throws Exception{
        ComboBoxBean attachmentType = (ComboBoxBean)cmbAttachmentType.getSelectedItem();
        attachmentBean.setAttachmentTypeCode(Integer.parseInt(attachmentType.getCode()));
        if(Integer.parseInt(attachmentType.getCode()) == 0) {
            CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("instPropAttachment_exceptionCode.1608"));
              txtArTitle.requestFocusInWindow();
                return false;
        }
        if(txtArTitle.getText() != null && txtArTitle.getText().trim().length() > 0) {
            attachmentBean.setAttachmentTitle(txtArTitle.getText());
        }
        else {
            CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("instPropAttachment_exceptionCode.1607"));
              txtArTitle.requestFocusInWindow();
                return false;
        }

        if(attachmentBean.getFileName() == null) {
           CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("instPropAttachment_exceptionCode.1609"));
              txtArTitle.requestFocusInWindow();
                return false;
        }
        attachmentBean.setContactName(txtContactName.getText());
        attachmentBean.setPhoneNumber(txtPhoneNumber.getText());
        attachmentBean.setEmailAddress(txtEmailAddress.getText());
        attachmentBean.setComments(txtArComments.getText());
        RequesterBean request = new RequesterBean();
        request.setFunctionType(UPDATE_ATTACHMENT);
        request.setDataObject(attachmentBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            Vector dataObjects = response.getDataObjects();
            observable.setFunctionType( getFunctionType());
            observable.notifyObservers( dataObjects );
        }else{
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        return true;
    }
    
    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    public Vector getAttachmentTypes() {
        return vecAttachmentTypes;
    }
    
    public void setAttachmentTypes(Vector vecAttachmentTypes) {
        this.vecAttachmentTypes = vecAttachmentTypes;
    }
    
    public char getFunctionType() {
        return functionType;
    }
    
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    private void validateData(){
        if (isDataChanged()) {
            String msg = coeusMessageResources.parseMessageKey(
                    "saveConfirmCode.1002");
            int confirm = CoeusOptionPane.showQuestionDialog(msg,
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            switch(confirm){
                case ( JOptionPane.NO_OPTION ) :
                    setSaveRequired( false );
                    parent.dispose();
                    break;
                case ( JOptionPane.YES_OPTION ) :
                    try{
                        if(validateAttachmentDetails() ){
                            boolean canDispose = saveAttachmentDetails();
                            if(canDispose){
                                parent.dispose();
                            }
                        }
                    }catch(Exception e) {
                        CoeusOptionPane.showInfoDialog( e.getMessage() );
                    }
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    parent.setVisible(true);
                    break;
            }
            
        }else{
            parent.dispose();
        }
    }
    public boolean isDataChanged(){
        getFormData();
        return saveRequired;
    }
    public void setAttachments(CoeusVector cvAttachments){
        this.cvAttachments = cvAttachments;
    }
    private void getFormData() {
        attachmentBean.addPropertyChangeListener( new PropertyChangeListener() {
            public void propertyChange( PropertyChangeEvent pce ) {
                if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                    saveRequired = true;
                    if( attachmentBean.getAcType() == null ) {
                        attachmentBean.setAcType(TypeConstants.UPDATE_RECORD );
                    }
                }
                if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                    saveRequired = true;
                    if( attachmentBean.getAcType() == null ) {
                        attachmentBean.setAcType(TypeConstants.UPDATE_RECORD );
                    }
                }
                if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                    if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                        saveRequired = true;
                        if( attachmentBean.getAcType() == null ) {
                            attachmentBean.setAcType(TypeConstants.UPDATE_RECORD );
                        }
                    }
                }
                
            }
        });
        if(vecAttachmentTypes != null && vecAttachmentTypes.size() >0){
            attachmentBean.setAttachmentTypeCode( Integer.parseInt(((ComboBoxBean)cmbAttachmentType.getSelectedItem()).getCode()));
        }
        attachmentBean.setAttachmentTitle( txtArTitle.getText().length() == 0 ? null : txtArTitle.getText() );
        attachmentBean.setContactName( txtContactName.getText().length() == 0 ? null : txtContactName.getText() );
        attachmentBean.setEmailAddress( txtEmailAddress.getText().length() == 0 ? null : txtEmailAddress.getText() );
        attachmentBean.setPhoneNumber( txtPhoneNumber.getText().length() == 0 ? null : txtPhoneNumber.getText() );
        attachmentBean.setComments( txtArComments.getText().length() == 0 ? null : txtArComments.getText() );
        attachmentBean.setFileName( txtFileName.getText().length() == 0 ? null : txtFileName.getText() );
    }
    
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }
    
    public boolean validateAttachmentDetails(){
        ComboBoxBean attachmentType = (ComboBoxBean)cmbAttachmentType.getSelectedItem();
        int selectedTypeCode = Integer.parseInt(attachmentType.getCode());
        if(cvAttachments != null && cvAttachments.size()>0 && vecAttachmentTypes!= null && vecAttachmentTypes.size()>0){
            for(int index=0;index<cvAttachments.size();index++){
                InstituteProposalAttachmentBean attachBean = (InstituteProposalAttachmentBean)cvAttachments.get(index);
                if(this.attachmentBean.getAttachmentNumber()!= attachBean.getAttachmentNumber()){
                    for(int j=0;j<vecAttachmentTypes.size();j++){
                        InstituteProposalAttachmentTypeBean attachmentTypeBean =
                                (InstituteProposalAttachmentTypeBean)vecAttachmentTypes.get(j);
                        if(selectedTypeCode == attachBean.getAttachmentTypeCode()){
                            if(selectedTypeCode == attachmentTypeBean.getAttachmentTypeCode()){
                                if(attachmentTypeBean.getAllowMultiple().equals("N")){
                                    String message = "";
                                    MessageFormat formatter = new MessageFormat("");
                                    message = formatter.format(coeusMessageResources.parseMessageKey(ATTACHMENT_TYPE_EXISTS),
                                            "'",cmbAttachmentType.getSelectedItem().toString(),"'");
                                    CoeusOptionPane.showWarningDialog(message);
                                    return false;
                                }
                            }
                        }
                    }
                }
                //COEUSQA:1525 - Attachments for Institute Proposal - Start
                else{
                    this.attachmentBean.setAwSequenceNumber(attachBean.getSequenceNumber());
                }
                //COEUSQA:1525 - End
            }
        }
        return true;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlContents = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblAttachmentNo = new javax.swing.JLabel();
        txtAttachmentNumber = new edu.mit.coeus.utils.CoeusTextField();
        lblSequenceNo = new javax.swing.JLabel();
        txtSequenceNo = new edu.mit.coeus.utils.CoeusTextField();
        lblTitle = new javax.swing.JLabel();
        scrPnTitle = new javax.swing.JScrollPane();
        txtArTitle = new javax.swing.JTextArea();
        lblContactName = new javax.swing.JLabel();
        txtContactName = new edu.mit.coeus.utils.CoeusTextField();
        lblPhone = new javax.swing.JLabel();
        txtPhoneNumber = new edu.mit.coeus.utils.CoeusTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmailAddress = new edu.mit.coeus.utils.CoeusTextField();
        lblComments = new javax.swing.JLabel();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblAttachmentType = new javax.swing.JLabel();
        cmbAttachmentType = new edu.mit.coeus.utils.CoeusComboBox();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();
        pnlButtons = new javax.swing.JPanel();
        pnlInternalButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblTemp = new javax.swing.JLabel();
        btnUpload = new edu.mit.coeus.utils.CoeusButton();
        btnView = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.BorderLayout());

        pnlMain.setLayout(new java.awt.BorderLayout());

        pnlContents.setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setText("Proposal No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblProposalValue, gridBagConstraints);

        lblAttachmentNo.setFont(CoeusFontFactory.getLabelFont());
        lblAttachmentNo.setText("Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblAttachmentNo, gridBagConstraints);

        txtAttachmentNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,2));
        txtAttachmentNumber.setEditable(false);
        txtAttachmentNumber.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtAttachmentNumber, gridBagConstraints);

        lblSequenceNo.setFont(CoeusFontFactory.getLabelFont());
        lblSequenceNo.setText("Sequence No.:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlContents.add(lblSequenceNo, gridBagConstraints);

        txtSequenceNo.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,2));
        txtSequenceNo.setEditable(false);
        txtSequenceNo.setMinimumSize(new java.awt.Dimension(100, 20));
        txtSequenceNo.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(txtSequenceNo, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Title:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblTitle, gridBagConstraints);

        scrPnTitle.setMinimumSize(new java.awt.Dimension(200, 80));
        scrPnTitle.setPreferredSize(new java.awt.Dimension(200, 80));
        txtArTitle.setDocument(new LimitedPlainDocument(150));
        txtArTitle.setFont(CoeusFontFactory.getNormalFont());
        txtArTitle.setLineWrap(true);
        txtArTitle.setWrapStyleWord(true);
        scrPnTitle.setViewportView(txtArTitle);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(scrPnTitle, gridBagConstraints);

        lblContactName.setFont(CoeusFontFactory.getLabelFont());
        lblContactName.setText("Contact Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblContactName, gridBagConstraints);

        txtContactName.setDocument(new LimitedPlainDocument(90));
        txtContactName.setMinimumSize(new java.awt.Dimension(200, 20));
        txtContactName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtContactName, gridBagConstraints);

        lblPhone.setFont(CoeusFontFactory.getLabelFont());
        lblPhone.setText("Phone Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblPhone, gridBagConstraints);

        txtPhoneNumber.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,20));
        txtPhoneNumber.setMinimumSize(new java.awt.Dimension(200, 20));
        txtPhoneNumber.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtPhoneNumber, gridBagConstraints);

        lblEmail.setFont(CoeusFontFactory.getLabelFont());
        lblEmail.setText("Email Address:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblEmail, gridBagConstraints);

        txtEmailAddress.setDocument(new LimitedPlainDocument(60));
        txtEmailAddress.setMinimumSize(new java.awt.Dimension(200, 20));
        txtEmailAddress.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(txtEmailAddress, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblComments, gridBagConstraints);

        scrPnComments.setMinimumSize(new java.awt.Dimension(200, 80));
        scrPnComments.setPreferredSize(new java.awt.Dimension(200, 80));
        txtArComments.setDocument(new LimitedPlainDocument(300));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(scrPnComments, gridBagConstraints);

        lblAttachmentType.setFont(CoeusFontFactory.getLabelFont());
        lblAttachmentType.setText("Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblAttachmentType, gridBagConstraints);

        cmbAttachmentType.setMinimumSize(new java.awt.Dimension(385, 19));
        cmbAttachmentType.setPreferredSize(new java.awt.Dimension(388, 19));
        cmbAttachmentType.setShowCode(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(cmbAttachmentType, gridBagConstraints);

        lblFileName.setFont(CoeusFontFactory.getLabelFont());
        lblFileName.setText("File Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        pnlContents.add(lblFileName, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setMaximumSize(new java.awt.Dimension(382, 20));
        txtFileName.setMinimumSize(new java.awt.Dimension(382, 20));
        txtFileName.setPreferredSize(new java.awt.Dimension(388, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        pnlContents.add(txtFileName, gridBagConstraints);

        pnlMain.add(pnlContents, java.awt.BorderLayout.CENTER);

        pnlButtons.setMaximumSize(new java.awt.Dimension(80, 143));
        pnlButtons.setMinimumSize(new java.awt.Dimension(80, 143));
        pnlButtons.setPreferredSize(new java.awt.Dimension(80, 143));
        pnlInternalButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(67, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(67, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(67, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlInternalButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInternalButtons.add(btnCancel, gridBagConstraints);

        lblTemp.setMaximumSize(new java.awt.Dimension(20, 99));
        lblTemp.setMinimumSize(new java.awt.Dimension(20, 99));
        lblTemp.setPreferredSize(new java.awt.Dimension(20, 99));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        pnlInternalButtons.add(lblTemp, gridBagConstraints);

        btnUpload.setMnemonic('U');
        btnUpload.setLabel("Upload");
        btnUpload.setMaximumSize(new java.awt.Dimension(67, 23));
        btnUpload.setMinimumSize(new java.awt.Dimension(67, 23));
        btnUpload.setPreferredSize(new java.awt.Dimension(67, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlInternalButtons.add(btnUpload, gridBagConstraints);

        btnView.setMnemonic('V');
        btnView.setText("View");
        btnView.setEnabled(false);
        btnView.setMaximumSize(new java.awt.Dimension(67, 23));
        btnView.setMinimumSize(new java.awt.Dimension(67, 23));
        btnView.setPreferredSize(new java.awt.Dimension(67, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlInternalButtons.add(btnView, gridBagConstraints);

        pnlButtons.add(pnlInternalButtons);

        pnlMain.add(pnlButtons, java.awt.BorderLayout.EAST);

        add(pnlMain, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private edu.mit.coeus.utils.CoeusButton btnUpload;
    private edu.mit.coeus.utils.CoeusButton btnView;
    private edu.mit.coeus.utils.CoeusComboBox cmbAttachmentType;
    private javax.swing.JLabel lblAttachmentNo;
    private javax.swing.JLabel lblAttachmentType;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblContactName;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblProposalNo;
    private javax.swing.JLabel lblProposalValue;
    private javax.swing.JLabel lblSequenceNo;
    private javax.swing.JLabel lblTemp;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlContents;
    private javax.swing.JPanel pnlInternalButtons;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrPnComments;
    private javax.swing.JScrollPane scrPnTitle;
    private javax.swing.JTextArea txtArComments;
    private javax.swing.JTextArea txtArTitle;
    private edu.mit.coeus.utils.CoeusTextField txtAttachmentNumber;
    private edu.mit.coeus.utils.CoeusTextField txtContactName;
    private edu.mit.coeus.utils.CoeusTextField txtEmailAddress;
    private edu.mit.coeus.utils.CoeusTextField txtFileName;
    private edu.mit.coeus.utils.CoeusTextField txtPhoneNumber;
    private edu.mit.coeus.utils.CoeusTextField txtSequenceNo;
    // End of variables declaration//GEN-END:variables
    
    
}
