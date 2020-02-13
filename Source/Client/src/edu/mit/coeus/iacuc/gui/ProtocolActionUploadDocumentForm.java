/*
 * ProtocolActionUploadDocumentForm.java
 *
 * Created on September 09, 2009, 11:57 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.CoeusMessageResources;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author satheeshkumarkn
 */
public class ProtocolActionUploadDocumentForm extends CoeusDlgWindow {
    
    private char functionType;
    private ProtocolActionDocumentBean protocolActionDocumentBean;
    private boolean fileSelected;
    private byte[] blobData;
    private boolean isdocumentLoad = false;
    private static final char ADD_DOCUMENT = 'A';
    private CoeusMessageResources coeusMessageResources;
    private static final String ADD_DOCUMENT_TITLE = "Add Attachment";
    private static final String MODIFY_DOCUMENT_TITLE = "Modify Attachment";
    private static final String SELECT_FILE = "protoSubmissionAttac_exceptionCode.2001";
    private static final String ENTER_DESCRIPTION = "protoSubmissionAttac_exceptionCode.2000";
    private static final String SAVE_CONFIRMATION = "user_details_exceptionCode.2547";
    private static final String EMPTY_STRING = "";
    private String userName = "";
    
    /**
     * Creates new form ProtocolActionUploadDocumentForm
     */
    public ProtocolActionUploadDocumentForm(ProtocolActionDocumentBean protocolActionDocumentBean,
            char functionType){
        super(CoeusGuiConstants.getMDIForm());
        this.protocolActionDocumentBean = protocolActionDocumentBean;
        setFunctionType(functionType);
    }
    
    /**
     * Initializes the components and sets the form data and the size of the form
     */
    public void showWindow(){
        try {
            initComponents();
            postInitComponents();
            setFormData();
            Dimension screenSize;
            Dimension dlgSize;
            if(functionType == TypeConstants.ADD_MODE){
                this.setTitle(ADD_DOCUMENT_TITLE);
            }else{
                this.setTitle(MODIFY_DOCUMENT_TITLE);
            }
            screenSize =Toolkit.getDefaultToolkit().getScreenSize();
            dlgSize = getSize();
            setLocation(screenSize.width / 2 - (dlgSize.width / 2),
                    screenSize.height / 2 - (dlgSize.height / 2));
            setVisible(true);
        } catch (HeadlessException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
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

        pnlUploadDocument = new javax.swing.JPanel();
        lblDescription = new javax.swing.JLabel();
        lblFileName = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnBrowse = new javax.swing.JButton();
        txtDescription = new edu.mit.coeus.utils.CoeusTextField();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        pnlUploadDocument.setLayout(new java.awt.GridBagLayout());

        lblDescription.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblDescription.setText("Description: ");
        lblDescription.setName("lblDescription");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 3, 0);
        pnlUploadDocument.add(lblDescription, gridBagConstraints);

        lblFileName.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblFileName.setText("File Name: ");
        lblFileName.setName("lblFileName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 9, 8);
        pnlUploadDocument.add(lblFileName, gridBagConstraints);

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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 3, 5);
        pnlUploadDocument.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setName("btnCancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(90, 22));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 3, 5);
        pnlUploadDocument.add(btnCancel, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 10, 10);
        pnlUploadDocument.add(btnBrowse, gridBagConstraints);

        txtDescription.setName("txtDescription");
        txtDescription.setNextFocusableComponent(btnBrowse);
        txtDescription.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlUploadDocument.add(txtDescription, gridBagConstraints);

        txtFileName.setEditable(false);
        txtFileName.setName("txtFileName");
        txtFileName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        pnlUploadDocument.add(txtFileName, gridBagConstraints);

        getContentPane().add(pnlUploadDocument, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    /**
     * Method for cancel action
     * @param evt 
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        isdocumentLoad = false;
        try{
            performCloseAction();
        }catch(CoeusException ce){
            ce.printStackTrace();
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    /*
     * Method for ok action
     */
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
       performOkAction();
    }//GEN-LAST:event_btnOkActionPerformed
    
    /*
     * Method for ok action
     */
    private void performOkAction(){
       boolean success = true;
        String description = txtDescription.getText().trim();
        String fileName = txtFileName.getText();
        coeusMessageResources = CoeusMessageResources.getInstance();
        //If user not entered description validation message will throw
        if(EMPTY_STRING.equals(description)){
            txtDescription.setText(description);
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_DESCRIPTION));
            txtDescription.requestFocusInWindow();
            success = false;
            return;
        }
        //If file is selected, blob data is set to bean other wise validation message will throw
        if(isFileSelected()){
            protocolActionDocumentBean.setFileBytes(getBlobData());
        }else if(functionType == ADD_DOCUMENT){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_FILE));
            btnBrowse.requestFocusInWindow();
            success = false;
            return;
        }
        if(success){
            if(protocolActionDocumentBean!=null){
                protocolActionDocumentBean.setDescription(description);
                protocolActionDocumentBean.setFileName(fileName);
                protocolActionDocumentBean.setUpdateTimestamp(CoeusUtils.getDBTimeStamp());
                CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
                if(EMPTY_STRING.equals(userName)){
                    userName = UserUtils.getUserName(mdiForm.getUserId());
                }
                protocolActionDocumentBean.setUpdateUser(mdiForm.getUserId());
                protocolActionDocumentBean.setUpdateUserName(userName);
                isdocumentLoad = true;
            }
            this.dispose();
        }
    }
    /**
     * Method for browse file action
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
     * Method to initialise dialog
     * @throws CoeusException If Any exception occurs
     */
    private void postInitComponents() throws CoeusException{
        txtDescription.setDocument(new LimitedPlainDocument(200));
        java.awt.Component[] components = { txtDescription, btnBrowse, btnOk,
        btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        this.setFocusTraversalPolicy(traversePolicy);
        this.setFocusCycleRoot(true);
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
                    performCloseAction();
                }catch(CoeusException ce){
                    ce.printStackTrace();
                }
            }
        });
        
        addEscapeKeyListener(new AbstractAction("escPressed"){
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
     * Method to set default focus based on open window
     * @throws CoeusException If Any exception occurs
     */
    private void requestDefaultFocusToComp() throws CoeusException{
        txtDescription.requestFocusInWindow();
    }
    
    /**
     * Method to perform the actions on closing the dialog
     */
    public void performCloseAction() throws CoeusException{
        if(checkDocumentDetailsChanged()){
            coeusMessageResources = CoeusMessageResources.getInstance();
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CONFIRMATION),
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
     * Populate the data in the form fields
     */
    private void setFormData(){
        txtDescription.setText(protocolActionDocumentBean.getDescription());
        txtFileName.setText(protocolActionDocumentBean.getFileName());
    }
    
    /*
     * Getter method for function type
     * @return functionType
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /*
     * Setter method to set function type
     * @Param functionType - char
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    /*
     * Method to check file is selected from browse window
     *
     */
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    
    /**
     * Setter method for file selection
     * @param fileSelected 
     */
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }
    
    /**
     * Getter method for blob data
     * @return blobData
     */
    public byte[] getBlobData() {
        return blobData;
    }
    
    /**
     * Setter method for blob data
     * @param blobData 
     */
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
        
    /**
     * Method to check document is loaded
     * @return isdocumentLoad
     */
    public boolean isDocumentLoad(){
        return isdocumentLoad;
    }
    
    /**
     * Checks whether any data is changed in the form
     */
    public boolean checkDocumentDetailsChanged(){
        if(functionType == TypeConstants.ADD_MODE){
            if(txtDescription.getText().length() > 0 || isFileSelected()){
                return true;
            }
        }else if(functionType == TypeConstants.MODIFY_MODE && protocolActionDocumentBean != null){
            if(!txtDescription.getText().trim().equals(protocolActionDocumentBean.getDescription())){
                return true;
            }else if(isFileSelected()){
                return true;
            }
        }
        return false;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowse;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JPanel pnlUploadDocument;
    private edu.mit.coeus.utils.CoeusTextField txtDescription;
    private edu.mit.coeus.utils.CoeusTextField txtFileName;
    // End of variables declaration//GEN-END:variables
    
}
