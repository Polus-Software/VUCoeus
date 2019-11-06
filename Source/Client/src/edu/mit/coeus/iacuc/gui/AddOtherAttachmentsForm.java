/*
 * @(#)AddOtherAttachmentsForm.java 1.0 05/08/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.iacuc.bean.UploadDocumentBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author  leenababu
 */
public class AddOtherAttachmentsForm extends CoeusDlgWindow implements ActionListener{
    
    private char functionType;
    private UploadDocumentBean uploadDocumentBean;
    private ProtocolInfoBean protocolInfoBean;
    private CoeusVector cvDocumentTypes;
    
    private boolean fileSelected;
    private boolean saveRequired;
    private byte[] blobData;
    private CoeusMessageResources coeusMessageResources;
    
    private static final char ADD_UPD_DEL_OTHER_DOCUMENT = '4';
    private static final String PROTOCOL_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/IacucProtocolServlet";
    
    /**
     * Creates new form AddOtherAttachmentsForm
     */
    public AddOtherAttachmentsForm(char functionType, UploadDocumentBean uploadDocumentBean,
            ProtocolInfoBean protocolInfoBean) {
        super(CoeusGuiConstants.getMDIForm(), true);
        this.functionType = functionType;
        this.uploadDocumentBean = uploadDocumentBean;
        this.protocolInfoBean = protocolInfoBean;
        
        initComponents();
        postInitComponents();
    }
    
    /**
     * Set the properties for the gui components
     */
    public void postInitComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        Component components[] = {cmbDocumentType, txtDescription, btnBrowse, btnOk, btnCancel};
        ScreenFocusTraversalPolicy screenFocusTraversalPolicy =
                new ScreenFocusTraversalPolicy(components);
        setFocusTraversalPolicy(screenFocusTraversalPolicy);
        setFocusCycleRoot(true);
        
        btnBrowse.addActionListener(this);
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        
        setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performCancelAction();
            }
        });
        
        addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
            }
        });
    }
    
    /**
     * Sets the form data
     */
    public void setFormData(){
        if(cvDocumentTypes!=null){
            cmbDocumentType.setModel(new DefaultComboBoxModel(cvDocumentTypes));
        }
        if(functionType == TypeConstants.MODIFY_MODE && uploadDocumentBean != null){
            ComboBoxBean comboBoxBean = new ComboBoxBean(
                    Integer.toString(uploadDocumentBean.getDocCode()),
                    uploadDocumentBean.getDocType());
            cmbDocumentType.setSelectedItem(comboBoxBean);
            txtDescription.setText(uploadDocumentBean.getDescription());
            txtFileName.setText(uploadDocumentBean.getFileName());
        }
    }
    
    /**
     * Initializes the components and sets size of the form
     */
    public void showWindow(){
        setFormData();
        Dimension screenSize;
        Dimension dlgSize;
        if(functionType == TypeConstants.ADD_MODE){
            setTitle("Add Attachment");
        }else{
            setTitle("Modify Attachment");
        }
        screenSize =Toolkit.getDefaultToolkit().getScreenSize();
        dlgSize = getSize();
        setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                screenSize.height / 2 - (dlgSize.height / 2));
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source.equals(btnOk)){
            performOkAction();
        }else if(source.equals(btnCancel)){
            performCancelAction();
        }else if(source.equals(btnBrowse)){
            performBrowseAction();
        }
    }
    
    /**
     * Performs the action for btnOK
     */
    public void performOkAction(){
        if(validateData()){
            if(functionType == TypeConstants.ADD_MODE){
                UploadDocumentBean uploadDocBean = new UploadDocumentBean();
                uploadDocBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                uploadDocBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                uploadDocBean.setDescription(txtDescription.getText());
                uploadDocBean.setFileName(txtFileName.getText());
                uploadDocBean.setDocument(getBlobData());
                ComboBoxBean comboBoxBean = (ComboBoxBean)cmbDocumentType.getSelectedItem();
                uploadDocBean.setDocCode(Integer.parseInt(comboBoxBean.getCode()));
                uploadDocBean.setAcType(TypeConstants.INSERT_RECORD);
                
                boolean success = saveDocument(uploadDocBean);
                if(success){
                    dispose();
                }
            }else if(functionType == TypeConstants.MODIFY_MODE){
                if(uploadDocumentBean != null){
                    uploadDocumentBean.setDescription(txtDescription.getText());
                    uploadDocumentBean.setFileName(txtFileName.getText());
                    ComboBoxBean comboBoxBean = (ComboBoxBean)cmbDocumentType.getSelectedItem();
                    uploadDocumentBean.setDocCode(Integer.parseInt(comboBoxBean.getCode()));
                    uploadDocumentBean.setAcType(TypeConstants.UPDATE_RECORD);
                    if(isFileSelected()){
                        uploadDocumentBean.setDocument(getBlobData());
                    }
                    boolean success = saveDocument(uploadDocumentBean);
                    if(success){
                        dispose();
                    }
                }
            }
        }
    }
    
    /**
     * Validate the form data
     *
     * @return boolean true if all data is valid else false
     */
    public boolean validateData(){
        if(txtDescription.getText().trim().length()==0){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1006"));
            txtDescription.requestFocusInWindow();
            return false;
        }else if(functionType == TypeConstants.ADD_MODE && !isFileSelected()){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
            btnBrowse.requestFocusInWindow();
            return false;
        }else if(isFileSelected() && getBlobData() != null && getBlobData().length == 0 ){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1005"));
            return false;
        }
        return true;
    }
    
    /**
     * Performs the action for btnCancel
     */
    public void performCancelAction(){
        if(checkSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("budget_baseWindow_exceptionCode.1402"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
            switch( option ) {
                case (CoeusOptionPane.SELECTION_YES):
                    setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    performOkAction();
                    setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                    break;
                case(CoeusOptionPane.SELECTION_NO):
                    dispose();
                    break;
                default:
                    break;
            }
        }else{
            dispose();
        }
        
    }
    /**
     * Checks whether any data is changed in the form
     */
    public boolean checkSaveRequired(){
        if(functionType == TypeConstants.ADD_MODE){
            if(cmbDocumentType.getSelectedIndex()>0){
                return true;
            }else if(txtDescription.getText().length() > 0 || isFileSelected()){
                return true;
            }
        }else if(functionType == TypeConstants.MODIFY_MODE && uploadDocumentBean != null){
            ComboBoxBean comboBoxBean = ((ComboBoxBean)cmbDocumentType.getSelectedItem());
            if(comboBoxBean != null){
                if(uploadDocumentBean.getDocCode() != Integer.parseInt(comboBoxBean.getCode())){
                    return true;
                }
            }
            if(!txtDescription.getText().equals(uploadDocumentBean.getDescription())){
                return true;
            }else if(isFileSelected()){
                return true;
            }
        }
        return false;
    }
    /**
     * Performs the action for btnBrowse
     */
    public void performBrowseAction(){
        CoeusFileChooser fileChooser = new CoeusFileChooser(this);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
            String fileName = fileChooser.getSelectedFile();
            if(fileName != null && !fileName.trim().equals("")){
                int index = fileName.lastIndexOf('.');
                if(index != -1 && index != fileName.length()){
                    setFileSelected(true);
                    txtFileName.setText(fileChooser.getFileName().getName());
                    setBlobData(fileChooser.getFile());
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "correspType_exceptionCode.1012"));
                    setFileSelected(false);
                    setBlobData(null);
                }
            }
        }
    }
    
    /**
     * Save the document to the database
     * @return boolean - true if success
     */
    public boolean saveDocument(UploadDocumentBean uploadDocBean){
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
                saveRequired = true;
            }
        }catch(CoeusException e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        return success;
    }
    
    /**
     * Getter method for the property fileSelected
     *
     * @return boolean
     */
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    /**
     * Setter method for the property fileSelected
     *
     * @param fileSelected
     */
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    /**
     * Getter method for the property blobData
     *
     * @return blob[]
     */
    public byte[] getBlobData() {
        return blobData;
    }
    
    /**
     * Setter method for the property blobData
     *
     * @param blobData
     */
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    /**
     * Getter method for the property saveRequired
     *
     * @return boolean
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /**
     * Setter method for the property saveRequired
     *
     * @param saveRequired
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /**
     * Setter method for the property cvDocumentTypes
     *
     * @param cvDocumentTypes
     */
    public void setDocumentTypes(CoeusVector cvDocumentTypes) {
        this.cvDocumentTypes = cvDocumentTypes;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        lblDescription = new javax.swing.JLabel();
        txtDescription = new edu.mit.coeus.utils.CoeusTextField();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();
        btnBrowse = new javax.swing.JButton();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblDocumentType = new javax.swing.JLabel();
        cmbDocumentType = new edu.mit.coeus.utils.CoeusComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblDescription.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblDescription.setText("Description: ");
        lblDescription.setMaximumSize(new java.awt.Dimension(72, 20));
        lblDescription.setMinimumSize(new java.awt.Dimension(72, 20));
        lblDescription.setName("lblDescription");
        lblDescription.setPreferredSize(new java.awt.Dimension(72, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 0);
        jPanel1.add(lblDescription, gridBagConstraints);

        txtDescription.setDocument(new LimitedPlainDocument(2000));
        txtDescription.setName("txtDescription");
        txtDescription.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel1.add(txtDescription, gridBagConstraints);

        lblFileName.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblFileName.setText("File Name: ");
        lblFileName.setMaximumSize(new java.awt.Dimension(64, 21));
        lblFileName.setMinimumSize(new java.awt.Dimension(64, 21));
        lblFileName.setName("lblFileName");
        lblFileName.setPreferredSize(new java.awt.Dimension(64, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 0);
        jPanel1.add(lblFileName, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setName("txtFileName");
        txtFileName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel1.add(txtFileName, gridBagConstraints);

        btnBrowse.setFont(CoeusFontFactory.getLabelFont());
        btnBrowse.setMnemonic('B');
        btnBrowse.setText("Browse...");
        btnBrowse.setName("btnBrowse");
        btnBrowse.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        jPanel1.add(btnBrowse, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setName("btnOk");
        btnOk.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 3, 5);
        jPanel1.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setName("btnCancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 5);
        jPanel1.add(btnCancel, gridBagConstraints);

        lblDocumentType.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblDocumentType.setText("Document Type: ");
        lblDocumentType.setName("lblDocumentType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        jPanel1.add(lblDocumentType, gridBagConstraints);

        cmbDocumentType.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(cmbDocumentType, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnBrowse;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public edu.mit.coeus.utils.CoeusComboBox cmbDocumentType;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblDocumentType;
    public javax.swing.JLabel lblFileName;
    public edu.mit.coeus.utils.CoeusTextField txtDescription;
    public edu.mit.coeus.utils.CoeusTextField txtFileName;
    // End of variables declaration//GEN-END:variables
    
}
