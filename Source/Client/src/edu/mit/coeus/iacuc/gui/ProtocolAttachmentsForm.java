/*
 * @(#)ProtocolAttachmentsForm.java 1.0 03/17/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.gui;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.apache.batik.ext.swing.GridBagConstants;

/**
 * This class is used to create Attachments tab window in Protocol Details window
 *
 * @author leenababu
 */
public class ProtocolAttachmentsForm extends javax.swing.JComponent{
    
    private JTabbedPane tbdPnAttachments;
    
    private ProtocolDetailForm protocolDetailForm;
    private ProtocolInfoBean protocolInfoBean;
    private ProtocolUploadDocumentForm protocolUploadDocumentForm;
    private ProtocolOtherAttachmentsForm protocolOtherAttachForm;
    
    private char functionType;
    private char otherFunctionType;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
    private static final int PROTOCOL_ABANDON_STATUS_CODE= 309;
    // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
    
    /** Creates a new instance of ProtocolAttachmentsForm */
    //Modified to get the otherFunctionType from ProtocolDetailForm
//    public ProtocolAttachmentsForm(ProtocolDetailForm protocolDetailForm,
//            ProtocolInfoBean protocolInfoBean, char functionType) throws CoeusException{
    public ProtocolAttachmentsForm(ProtocolDetailForm protocolDetailForm,
            ProtocolInfoBean protocolInfoBean, char functionType, char otherFunctionType) throws CoeusException{
        this.protocolDetailForm = protocolDetailForm;
        this.protocolInfoBean = protocolInfoBean;
        this.functionType = functionType;
        //this.otherFunctionType = functionType;
        this.otherFunctionType = otherFunctionType;
        initComponents();
    }
    
    /**
     * Initialize the gui components in the form
     */
    public void initComponents() throws CoeusException{
        setLayout(new GridBagLayout());
        tbdPnAttachments = new JTabbedPane();
        JPanel pnlAttachments = new JPanel();
        pnlAttachments.setPreferredSize(new Dimension(850, 500));
        pnlAttachments.setLayout(new GridBagLayout());
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = GridBagConstants.NORTHWEST;
        
        protocolUploadDocumentForm = new ProtocolUploadDocumentForm(protocolInfoBean, functionType);
        pnlAttachments.add(protocolUploadDocumentForm, gridBagConstraints);
        if(protocolUploadDocumentForm != null){
            tbdPnAttachments.addTab("Protocol Attachments",pnlAttachments);
        }
        
        protocolOtherAttachForm = new ProtocolOtherAttachmentsForm(protocolInfoBean, functionType);
        protocolOtherAttachForm.setPreferredSize(new java.awt.Dimension(600,400));
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start        
        if(protocolInfoBean != null && protocolInfoBean.getProtocolStatusCode() == PROTOCOL_ABANDON_STATUS_CODE){
            protocolOtherAttachForm.btnAdd.setEnabled(false);
            protocolOtherAttachForm.btnDelete.setEnabled(false);
            protocolOtherAttachForm.btnModify.setEnabled(false);
        }        
        // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
        tbdPnAttachments.addTab("Other Attachments",protocolOtherAttachForm);
        tbdPnAttachments.setPreferredSize(new Dimension(900, 510));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = GridBagConstants.NORTHWEST;
        
        add(tbdPnAttachments, gridBagConstraints);
        
        setPreferredSize( new java.awt.Dimension(900, 510 ));
    }
    
    /**
     * Sets the data to the form
     */
    public void setFormData(){
        
        try{
            if(protocolUploadDocumentForm != null){
                //Commented/Modified for case#4275 - upload attachments until in agenda - Start
//                protocolUploadDocumentForm.setFormData();
//                protocolUploadDocumentForm.setProtocolDetailForm(protocolDetailForm);
                protocolUploadDocumentForm.setFunctionType(functionType);
                protocolUploadDocumentForm.setFormData();
                protocolUploadDocumentForm.setProtocolDetailForm(protocolDetailForm);
                formatFields();
                //Case#4275 - End
                
            }
            if(protocolOtherAttachForm != null){
                //Modified for case#4275 - upload attachments until in agenda - Start
                protocolOtherAttachForm.setFunctionType(otherFunctionType);
                //Case#4275 - End
                protocolOtherAttachForm.setProtocolDetailForm(protocolDetailForm);
                if(!protocolOtherAttachForm.isDataFetched()){
                    protocolOtherAttachForm.setFormData();
                }
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    /**
     * Getter method for the property protocolInfoBean
     * @return protocolInfoBean
     */
    public ProtocolInfoBean getProtocolInfoBean() {
        return protocolInfoBean;
    }
    
    /**
     * Setter method for the property protocolInfoBean. Also set the value
     * in protocolUploadDocumentForm and protocolOtherAttachForm
     * @param protocolInfoBean
     */
    public void setProtocolInfoBean(ProtocolInfoBean protocolInfoBean) {
        this.protocolInfoBean = protocolInfoBean;
        if(protocolUploadDocumentForm != null){
            protocolUploadDocumentForm.setProtocolInfoBean(protocolInfoBean);
        }
        if(protocolOtherAttachForm != null){
            protocolOtherAttachForm.setProtocolInfoBean(protocolInfoBean);
        }
    }
    
    /**
     * Getter method for the property functionType
     * @return char functionType
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /**
     * Sets the function type in this form, ProtocolUploadAttachmentForm and
     * ProtocolOtherAttachmentsForm
     * @param char function Type
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
        if(protocolUploadDocumentForm != null){
            protocolUploadDocumentForm.setFunctionType(functionType);
        }
        if(protocolOtherAttachForm != null){
            protocolOtherAttachForm.setFunctionType(functionType);
        }
    }
    
    /**
     * Calls the formatFields method in protocolUploadDocumentForm
     */
    public void formatFields(){
        if(protocolUploadDocumentForm != null){
            protocolUploadDocumentForm.formatFields();
        }
    }

    /**
     * Setter method for the property otherFunctionType
     *
     * @param otherFunctionType
     */
    public void setOtherFunctionType(char otherFunctionType) {
        this.otherFunctionType = otherFunctionType;
    }
    
    /**
     * Getter method for the property otherFunctionType
     * @return char otherFunctionType
     */
    public char getOtherFunctionType() {
        return otherFunctionType;
    }
}
