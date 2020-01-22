/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

/*
 * SubContractAddAttachmentForm.java
 *
 * Created on January 2, 2012, 05:04 PM
 */

package edu.mit.coeus.subcontract.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;

import edu.mit.coeus.subcontract.bean.SubContractAttachmentBean;
import edu.mit.coeus.subcontract.bean.SubContractBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusFileChooser;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;

/**
 *
 * @author  manjunathabn
 */
public class SubContractAddAttachmentForm extends CoeusDlgWindow {
    
    private static final char MODIFY_DOCUMENT = 'M';
    private static final char CHANGE_STATUS = 'S';
    private static final char ADD_DOCUMENT = 'A';
    private static final char ADD_UPD_DEL_SUBCONTRACT_ATTACH = 's';
    private char functionType = 'M';
    private CoeusVector cvDocType;
    private CoeusVector cvDocStatus;
    private boolean fileSelected;
    private byte[] blobData;
    private JTable tblDocuments;
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + "/SubcontractMaintenenceServlet";
    private CoeusMessageResources coeusMessageResources;
    private RequesterBean requester = null;
    private ResponderBean responder = null;
    private AppletServletCommunicator comm;
   
    private SubContractAttachmentBean subContractAttachmentBean;
    
    /** Creates new form SubContractAddAttachmentForm */
    public SubContractAddAttachmentForm(SubContractAttachmentBean subContractAttachmentBean, char functionType, CoeusVector cvDocType){
        super(CoeusGuiConstants.getMDIForm());
        this.subContractAttachmentBean = subContractAttachmentBean;
        setFunctionType(functionType);
        this.cvDocType = cvDocType;
        setCvDocType(cvDocType);               
    }
    
     
    /**
     * Initializes the components and sets the form data and the size of the form
     */
    public void showWindow(){
        try {
            initComponents();
            postInitComponents();
            coeusMessageResources = coeusMessageResources.getInstance();
            disableComponents(functionType);
            setFormData();
            
            Dimension screenSize;
            Dimension dlgSize;
            
            screenSize =Toolkit.getDefaultToolkit().getScreenSize();
            dlgSize = getSize();
            setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                    screenSize.height / 2 - (dlgSize.height / 2));
            setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
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

        pnlAttachModify = new javax.swing.JPanel();
        lblDocumentType = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblFileName = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnBrowse = new javax.swing.JButton();
        cmbDocumentType = new edu.mit.coeus.utils.CoeusComboBox();
        txtDescription = new edu.mit.coeus.utils.CoeusTextField();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        pnlAttachModify.setLayout(new java.awt.GridBagLayout());

        lblDocumentType.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblDocumentType.setText("Attachment Type: ");
        lblDocumentType.setName("lblDocumentType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 3, 0);
        pnlAttachModify.add(lblDocumentType, gridBagConstraints);

        lblDescription.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblDescription.setText("Description: ");
        lblDescription.setName("lblDescription");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 0);
        pnlAttachModify.add(lblDescription, gridBagConstraints);

        lblFileName.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblFileName.setText("File Name: ");
        lblFileName.setName("lblFileName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 0);
        pnlAttachModify.add(lblFileName, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setName("btnOk");
        btnOk.setNextFocusableComponent(btnCancel);
        btnOk.setPreferredSize(new java.awt.Dimension(90, 22));
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 3, 5);
        pnlAttachModify.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setName("btnCancel");
        btnCancel.setNextFocusableComponent(cmbDocumentType);
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 22));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 0);
        pnlAttachModify.add(btnCancel, gridBagConstraints);

        btnBrowse.setFont(CoeusFontFactory.getLabelFont());
        btnBrowse.setMnemonic('B');
        btnBrowse.setText("Browse...");
        btnBrowse.setName("btnBrowse");
        btnBrowse.setPreferredSize(new java.awt.Dimension(90, 21));
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 0);
        pnlAttachModify.add(btnBrowse, gridBagConstraints);

        cmbDocumentType.setEnabled(false);
        cmbDocumentType.setNextFocusableComponent(txtDescription);
        cmbDocumentType.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 3, 0);
        pnlAttachModify.add(cmbDocumentType, gridBagConstraints);

        txtDescription.setEditable(false);
        txtDescription.setName("txtDescription");
        txtDescription.setNextFocusableComponent(btnBrowse);
        txtDescription.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlAttachModify.add(txtDescription, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setName("txtFileName");
        txtFileName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        pnlAttachModify.add(txtFileName, gridBagConstraints);

        getContentPane().add(pnlAttachModify, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Method to initialise dialog
     * @throws CoeusException If Any exception occurs
     */
    private void postInitComponents() throws CoeusException{
        
        setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                try{
                    requestDefaultFocusToComp();
                }catch(CoeusException ce){
                    ce.printStackTrace();
                }
            }
        });
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                try{
                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                            CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                    
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){
                        saveModify();
                    } else {
                        dispose();
                    }
                }catch(Exception ce){
                    ce.printStackTrace();
                }
            }
        });
        
        addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                try{
                    int selectedOption = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                            CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                    
                    if(selectedOption == CoeusOptionPane.SELECTION_YES){
                        saveModify();
                    } else {
                        dispose();
                    }
                }catch(Exception ce){
                    ce.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Method to perform Cancel 
     * @param evt 
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int selectedOption = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
        
        if(selectedOption == CoeusOptionPane.SELECTION_YES){      
            saveModify();   
        }
        else {
            this.dispose();
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    
    /**
     * Method to perform OK Action
     * @param evt 
     */
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
            saveModify();         
    }//GEN-LAST:event_btnOkActionPerformed
    
    /**
     * Method to save or update the schedule attachments
     *
     */
    private void saveModify() {
        try {
            boolean success = true;
            if(functionType == MODIFY_DOCUMENT){
                if(validateForm()){
                    CoeusDocumentUtils docTypeUtils =  CoeusDocumentUtils.getInstance();
                    subContractAttachmentBean.setAcType(TypeConstants.UPDATE_RECORD);
                    subContractAttachmentBean.setFileName(txtFileName.getText());
                    subContractAttachmentBean.setDescription(txtDescription.getText());
                    if(isFileSelected()){
                        subContractAttachmentBean.setDocument(getBlobData());
                        subContractAttachmentBean.setMimeType(docTypeUtils.getDocumentMimeType(subContractAttachmentBean));
                    }
                    success = saveUploadDocument();
                } else{
                    success = false;
                }
                
            }else if(functionType == ADD_DOCUMENT){
                success = performUploadAction();
            }
            if(success){
                this.dispose();
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Method performs when btnBrowse is clicked
     * @param evt 
     */
    private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
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
    }//GEN-LAST:event_btnBrowseActionPerformed
      
    /**
     * method to set default focus based on open window
     * @throws CoeusException If Any exception occurs
     */
    private void requestDefaultFocusToComp() throws CoeusException{
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            if(cmbDocumentType.isEnabled()){
                cmbDocumentType.requestFocusInWindow();
            }else{
                txtDescription.requestFocusInWindow();
            }
        }else{
            btnCancel.requestFocusInWindow();
        }
    }          
    
    /**
     * method to perform upload Attachment
     * @return boolean true if successful
     * @throws CoeusException If Any exception occurs
     */
    private boolean performUploadAction() throws CoeusException{
        boolean isSuccess = false;
        if(validateForm()){
            if(isFileSelected()){
                if(getBlobData().length == 0 ){
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1005"));
                    return false;
                }
            }
            ComboBoxBean cmbTypeCode =
                    (ComboBoxBean)cmbDocumentType.getSelectedItem();
            subContractAttachmentBean.setAttachmentTypeCode(Integer.parseInt(cmbTypeCode.getCode()));
            subContractAttachmentBean.setDescription(txtDescription.getText().trim());
            subContractAttachmentBean.setDocument(getBlobData());
            subContractAttachmentBean.setFileName(txtFileName.getText().trim());
            subContractAttachmentBean.setAcType(TypeConstants.INSERT_RECORD);
            CoeusDocumentUtils docTypeUtils =  CoeusDocumentUtils.getInstance();
            subContractAttachmentBean.setMimeType(docTypeUtils.getDocumentMimeType(subContractAttachmentBean));
            isSuccess = saveUploadDocument();
        }
        return isSuccess;
    }
    
    /**
     * Method to validate form data
     * @return boolean true data is valid else false
     */
    private boolean validateForm() {
        if(txtDescription.getText() != null && txtFileName.getText() != null){
            if(txtDescription.getText().trim().length() == 0){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1006"));
                txtDescription.requestFocusInWindow();
                return false;
            }
            if(txtDescription.getText().trim().length()>=200){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1018"));
                txtDescription.requestFocusInWindow();
                return false;
            }
            if(txtFileName.getText().trim().length() == 0){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1007"));
                return false;
            }
            if(txtFileName.getText().trim().length()>=300){
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1019"));
                txtDescription.requestFocusInWindow();
                return false;
            }            
        }
        return true;
    }
    
    /**
     * Save the upload Attachment to the db
     * @throws edu.mit.coeus.exception.CoeusException 
     * @return 
     */
    public boolean saveUploadDocument() throws CoeusException{
        boolean success = true;
        requester = new RequesterBean();
        Vector vecServerObjects = new Vector();      
        vecServerObjects.add(0,subContractAttachmentBean);
        requester.setDataObjects(vecServerObjects);
        requester.setFunctionType(ADD_UPD_DEL_SUBCONTRACT_ATTACH);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requester);
        comm.send();        
        responder = comm.getResponse();        
        if(!responder.isSuccessfulResponse()){
            success = false;
            throw new CoeusException(responder.getMessage(),0);
        }
        return success;
    }
    
    /**
     *  Disable the components of the form according to the functionType
     * @param functionType the mode in which the window is opened
     */
    private void disableComponents(char functionType){
        if(functionType == MODIFY_DOCUMENT){
            txtDescription.setEditable(true);
            setTitle("Modify Attachment");
        }else if(functionType == ADD_DOCUMENT ){
            cmbDocumentType.setEnabled(true);
            txtDescription.setEditable(true);
            setTitle("Add Attachment");
        }
    }
    /**
     * Populate the data in the form fields
     */
    private void setFormData(){
        if(subContractAttachmentBean!=null){
            cmbDocumentType.setModel(new DefaultComboBoxModel(cvDocType));
           // cmbDocumentType.setSelectedIndex(getDocumentTypeIndex());
            if(functionType == MODIFY_DOCUMENT) {
                txtDescription.setText(subContractAttachmentBean.getDescription());
                txtFileName.setText(subContractAttachmentBean.getFileName());
            }
        }           
    }
    
    /**
     * Get the index to be set selected for the document type combobox
     * @return int -index
     */
//    public int getDocumentTypeIndex(){
//        int index = 0;
//        if(subContractAttachmentBean!=null && cvDocType!=null && subContractAttachmentBean.getAttachmentType()!=null){
//            if(cvDocType!=null ){
//                for(int i = 0; i<cvDocType.size(); i++){
//                    if(subContractAttachmentBean.getAttachmentType().equalsIgnoreCase(cvDocType.get(i).toString())){
//                        index = i;
//                        break;
//                    }
//                }
//            }
//        }
//        return index;
//    }
        
    /**
     * Method used to set the functionType
     * @param functionType
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Method used to get the functionType
     * @param functionType
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /**
     * Method used to get the cvDocType
     * @param cvDocType
     */
    public CoeusVector getCvDocType() {
        return cvDocType;
    }
    
    /**
     * Method used to get the cvDocType
     * @param cvDocType
     */
    public void setCvDocType(CoeusVector cvDocType) {
        this.cvDocType = cvDocType;
    }
    
    /**
     * Method used to get Fileselected or not
     * @param fileSelected
     */
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    /**
     * Method used to set Fileselected value
     * @param fileSelected
     */
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    /**
     * Method used to get BlobData
     * @param blobData
     */
    public byte[] getBlobData() {
        return blobData;
    }
    
    /**
     * Method used to set BlobData
     * @param blobData
     */
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    /**
     * Method used to get Table Documents
     * @param tblDocuments
     */
    public JTable getTblDocuments() {
        return tblDocuments;
    }
    
    /**
     * Method used to set Table Documents
     * @param tblDocuments
     */
    public void setTblDocuments(JTable tblDocuments) {
        this.tblDocuments = tblDocuments;
    }      
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private edu.mit.coeus.utils.CoeusComboBox cmbDocumentType;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDocumentType;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JPanel pnlAttachModify;
    private edu.mit.coeus.utils.CoeusTextField txtDescription;
    private edu.mit.coeus.utils.CoeusTextField txtFileName;
    // End of variables declaration//GEN-END:variables
    
}