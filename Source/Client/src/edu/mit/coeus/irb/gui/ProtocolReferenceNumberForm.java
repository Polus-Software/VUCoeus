/**
 * @(#)ProtocolReferenceNumberForm.java  1.0  July 16, 2003, 10:09 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */ 
/* PMD check performed, and commented unused imports and variables on 28-JULY-2010
 * by MD.Ehtesham Ansari
 */

package edu.mit.coeus.irb.gui;

import edu.mit.coeus.utils.refno.ReferenceNumberForm;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.bean.*;

import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** The class is used to display the <code>ProtocolReferenceNumberForm/code> screen
 *
 * @author Raghunath P.V.
 * @version 1.0
 * Created on May 29, 2003, 12:17 PM
 */
public class ProtocolReferenceNumberForm implements TypeConstants {
    
    private char functionType;
    
    private Vector vecReferenceData;
    private Vector vecDeletedReferenceData;
    //private Vector vecReplicateData;
    private Vector vecReferenceTypeCode;
//    private Vector vecColumnNames; Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
    
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    
    private static final char RESPONSE_WINDOW = 'R';
    
    private CoeusDlgWindow dlgParentComponent;
    private ReferenceNumberForm referenceNumberForm;
    private boolean saveRequired;
    private CoeusMessageResources coeusMessageResources;
    
    private String protocolNumber;
    private Vector tempRefData;
    
    /** 
     * Creates a new instance of ProtocolReferenceNumberForm 
     */
    public ProtocolReferenceNumberForm() {
    }
    
    /** Creates a new instance of ProtocolReferenceNumberForm
     *
     * @param proposalNumber Proposal Number
     * @param vecReviewData Vector of Special Review data
     * @param functionType indicates whether INSERT/MODIFY/DISPLAY 
     */
    public ProtocolReferenceNumberForm( String protocolId, char functionType, Vector referenceData ) {

        this.functionType = functionType;
        this.vecReferenceData = referenceData;
        try {
            tempRefData = (Vector)ObjectCloner.deepCopy(referenceData);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        this.vecDeletedReferenceData = new Vector();
        this.protocolNumber = protocolId;
//        try
//        {
//            vecReplicateData = (Vector)ObjectCloner.deepCopy(vecReferenceData);
//        }
//        catch(Exception ex){
//            ex.printStackTrace();
//        }
    }

    /** This method is used to display the Proposal Special Form
     *
     */
    public void showProtocolReferenceForm(){
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.GridBagConstraints gridBagConstraints;        
        //Added for pass the module code-new
        referenceNumberForm = new ReferenceNumberForm( functionType, vecReferenceData, ModuleConstants.PROTOCOL_MODULE_CODE );
        referenceNumberForm.setWindowType(RESPONSE_WINDOW);
        referenceNumberForm.setReferenceTypes(vecReferenceTypeCode);
        //referenceNumberForm.setVecColumnNames(vecColumnNames);// Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
        
        referenceNumberForm.setProtocolNumber(protocolNumber);
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMaximumSize(new java.awt.Dimension(106, 26));
        btnOk.setMinimumSize(new java.awt.Dimension(106, 26));
        btnOk.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        
        //Disable OK if DISPLAY_MODE
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        boolean enabled = functionType != DISPLAY_MODE ? true : false;
        boolean enabled = (functionType == DISPLAY_MODE 
                || functionType == CoeusGuiConstants.AMEND_MODE) ? false : true;
        btnOk.setEnabled(enabled);                        
        
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMaximumSize(new java.awt.Dimension(106, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(106, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(85, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        btnOk.addActionListener( new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                try{
                    if(isSaveRequired()){
                        if(referenceNumberForm.validateData()){
                            //Get Deleted/Non deleted records from SpecialReviewForm
                            vecReferenceData = referenceNumberForm.getFormData();
                            //Merge the Deleted and Non deleted records
                            vecReferenceData = getFormData();
                            dlgParentComponent.dispose();
                        }
                    }
                    else{
                        dlgParentComponent.dispose();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }                
            }
        });
        
        btnCancel.addActionListener( new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                performWindowClosing();
            }
        });

        //specialReviewForm.setProposalDescription(proposalNumber,strSponsor);        
        referenceNumberForm.preInitComponents(btnOk,btnCancel);
        
        JComponent cmpMain = referenceNumberForm.showReferenceNumbers(CoeusGuiConstants.getMDIForm());
        
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Reference Numbers", true);
        dlgParentComponent.getContentPane().add(cmpMain);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);    
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();        
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
            screenSize.height/2 - (dlgSize.height/2));        

        dlgParentComponent.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae) {
                    performWindowClosing();
                }
        });
        
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                requestDefaultFocusForComponent();
            }
            public void windowClosing(WindowEvent we){
                 performWindowClosing();
                 //return;
            }
            });

        dlgParentComponent.show();
    }
    private void requestDefaultFocusForComponent(){
        //code modified for coeus4.3 enhancements that UI to be in display mode
        //when new amendment or renewal is created
//        if( functionType == DISPLAY_MODE ) {        
        if( functionType == DISPLAY_MODE || functionType == CoeusGuiConstants.AMEND_MODE) {
            btnCancel.requestFocusInWindow();
        }else{
            referenceNumberForm.requestDefaultFocusForComponent();
        }
    }
    
   /**
    * This method is used to merge the Delete vedtors and non Deleted Vectors 
    *
    * @return Vector of Special Review Form beans.
    */
    
    public Vector getFormData(){
        
        if(vecDeletedReferenceData != null){
            int delSize = vecDeletedReferenceData.size();
            ReferencesBean referencesBean = null;
            for(int index = 0; index < delSize; index++){
                referencesBean = (ReferencesBean)vecDeletedReferenceData.get(index);
                if(referencesBean != null && vecReferenceData != null){
                    vecReferenceData.insertElementAt( referencesBean, index );
                }
            }
        }
        vecDeletedReferenceData = null;
        return vecReferenceData;
    }
    
    /**
    * This method is used to separate the Delete & non Deleted data into respective Vectors.
    */
    
    public void setFormData(){
        if(vecReferenceData != null){
            ReferencesBean refBean = null;
            for(int index = 0; index < vecReferenceData.size(); index++){
                refBean = (ReferencesBean)vecReferenceData.elementAt(index);
                if(refBean.getAcType() != null && refBean.getAcType().equals("D")){
                    vecDeletedReferenceData.addElement(refBean);
                    vecReferenceData.removeElementAt(index);
                    index--;
                }
            }
        }
    }

    /** This method is used to get the saveRequired Flag
     *
     * @return boolean true if changes are made in the form, else false
     */
    public boolean isSaveRequired(){
        try{
            if( functionType != DISPLAY_MODE ) {
               if(referenceNumberForm.isSaveRequired()) {
                   saveRequired = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return saveRequired;
    }
    
    public void setSaveRequired(boolean flag){
        saveRequired = flag;
        if(referenceNumberForm != null){
            referenceNumberForm.setSaveRequired(flag);
        }
    }
    
    /**
     * This method is used to perform the Window closing operation
     */
    private void performWindowClosing(){
        int option = JOptionPane.NO_OPTION;
        if(functionType != DISPLAY_MODE){
            if(isSaveRequired()){
                option
                    = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                                "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
            }

            if(option == JOptionPane.YES_OPTION){
                try{
                    if(referenceNumberForm.validateData())
                    {                        
                        //Get Deleted/Non deleted records from SpecialReviewForm
                        vecReferenceData = referenceNumberForm.getFormData();
                        //Merge the Deleted and Non deleted records
                        vecReferenceData = getFormData();
                        dlgParentComponent.dispose();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == JOptionPane.NO_OPTION){
                //saveRequired = false;
                referenceNumberForm.setSaveRequired(false);
                vecReferenceData = tempRefData;
                dlgParentComponent.dispose();
            }
        }else{
            dlgParentComponent.dispose();
        }
    }
    
    /** Getter for property vecReferenceData.
     * @return Value of property vecReferenceData.
     */
    public java.util.Vector getVecReferenceData() {
        return vecReferenceData;
    }
    
    /** Setter for property vecReferenceData. 
     * @param vecReferenceData New value of property vecReferenceData.
     */
    public void setVecReferenceData(java.util.Vector vecReferenceData) {
        this.vecReferenceData = vecReferenceData;
    }
    
    /** Getter for property vecReplicateData.
     * @return Value of property vecReplicateData.
     */
//    public java.util.Vector getVecReplicateData() {
//        return vecReplicateData;
//    }
    
    /** Setter for property vecReplicateData.
     * @param vecReplicateData New value of property vecReplicateData.
     */
//    public void setVecReplicateData(java.util.Vector vecReplicateData) {
//        this.vecReplicateData = vecReplicateData;
//    }
    
    /** Getter for property vecReferenceTypeCode.
     * @return Value of property vecReferenceTypeCode.
     */
    public java.util.Vector getVecReferenceTypeCode() {
        return vecReferenceTypeCode;
    }
    
    /** Setter for property vecReferenceTypeCode.
     * @param vecReferenceTypeCode New value of property vecReferenceTypeCode.
     */
    public void setVecReferenceTypeCode(java.util.Vector vecReferenceTypeCode) {
        this.vecReferenceTypeCode = vecReferenceTypeCode;
    }
      // Commented to use CoeusLabel instead Module Parameter XML file Start :02-Sep-2005
//    /** Getter for property vecColumnNames.
//     * @return Value of property vecColumnNames.
//     */
//    public java.util.Vector getVecColumnNames() {
//        return vecColumnNames;
//    }
//    
//    /** Setter for property vecColumnNames.
//     * @param vecColumnNames New value of property vecColumnNames.
//     */
//    public void setVecColumnNames(java.util.Vector vecColumnNames) {
//        this.vecColumnNames = vecColumnNames;
//    } end : 02-Sep-2005
    
}
