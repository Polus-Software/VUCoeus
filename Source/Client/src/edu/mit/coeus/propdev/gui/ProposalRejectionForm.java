/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.propdev.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.*;

/** Screen to allow the user to Reject the proposal
 * ProposalRejectionForm.java
 * @author  Vyjayanthi
 * Created on January 5, 2004, 11:58 AM
 */

public class ProposalRejectionForm extends javax.swing.JComponent
implements ActionListener {
    
    private CoeusDlgWindow dlgProposalRejection;
    
    private static final int WIDTH = 425;
    private static final int HEIGHT = 165;
    
    private Frame parent;
    private boolean modal;
    
    /** Flag to check if the proposal is rejected */
    private boolean rejectClicked = false;
    
    /** Flag to check if any data is modified */
    private boolean saveRequired = false;
    
    private static final String REJECT_ACTION = "R";
    
    private static final char PROPOSAL_APPROVE_UPDATE = 'Q';
        
    /** Holds the current Approval bean from the routing table */
    private ProposalApprovalBean currentApprovalBean;
    
    /** Holds an instance of the ProposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
                                             "/ProposalActionServlet";
    
    private static final String TITLE = "Proposal Rejection";
    private static final String ENTER_COMMENTS = "proposal_Action_exceptionCode.8009";
    private static final String ERROR_DURING_REJECTION = "proposal_Action_exceptionCode.8010";
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */    
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private boolean parenProposal;
  
    /** Creates new form ProposalRejectionForm
     * @param parent holds the frame
     * @param modal holds true if modal, false otherwise
     */
    public ProposalRejectionForm( Frame parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initComponents();
        postInitComponents();
        registerComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form. */
    private void postInitComponents(){    
        dlgProposalRejection = new CoeusDlgWindow(parent, modal);
        dlgProposalRejection.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgProposalRejection.getContentPane().add(this);
        dlgProposalRejection.setResizable(false);
        dlgProposalRejection.setTitle(TITLE);
        dlgProposalRejection.setFont(CoeusFontFactory.getLabelFont());
        dlgProposalRejection.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgProposalRejection.getSize();
        dlgProposalRejection.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
    }
    
    /** This method is used to set the listeners to the components. */
    private void registerComponents(){

        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtArComments, btnReject, btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        //Add listeners to components
        btnReject.addActionListener(this);
        btnCancel.addActionListener(this);
        
        dlgProposalRejection.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        dlgProposalRejection.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        
        dlgProposalRejection.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    txtArComments.requestFocus();
                }
        });

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        lblComments = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblTitle = new javax.swing.JLabel();
        btnReject = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblComments, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnCancel, gridBagConstraints);

        scrPnComments.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnComments.setMinimumSize(new java.awt.Dimension(325, 80));
        scrPnComments.setPreferredSize(new java.awt.Dimension(325, 80));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPnComments, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Confirm your Rejection of the Proposal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblTitle, gridBagConstraints);

        btnReject.setFont(CoeusFontFactory.getLabelFont());
        btnReject.setMnemonic('R');
        btnReject.setText("Reject");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnReject, gridBagConstraints);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnReject;
    public javax.swing.JLabel lblComments;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JScrollPane scrPnComments;
    public javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(btnReject) ){
                performRejectOperation();
                // Update all the child propsoal status
                if(isParenProposal()){
                    updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                }
            }else if( source.equals(btnCancel) ){
                performWindowClosing();
            }
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){
        //If comments are added/modified
        if( txtArComments.getText().trim().length() > 0 ){
            if( !txtArComments.getText().trim().equals(currentApprovalBean.getComments()) ){
                setSaveRequired(true);
            }
        }
        
        int option = JOptionPane.NO_OPTION;
        if(isSaveRequired()){
            option
            = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            "saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch( option ){
                case ( JOptionPane.YES_OPTION ):
                    try{
                        performRejectOperation();
                        updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                    }catch(Exception e){
                        e.getMessage();
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    dlgProposalRejection.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    break;
            }
        }else{
            dlgProposalRejection.dispose();
        }
    }
    
    /** */
    private void performRejectOperation(){
        if( txtArComments.getText().trim().length() > 0 ){
            currentApprovalBean.setAction(REJECT_ACTION);
            currentApprovalBean.setApprovalAll(0);
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(PROPOSAL_APPROVE_UPDATE);
            
            Vector vecData = new Vector();
            CoeusVector cvNewApprovers = null;
            vecData.addElement(cvNewApprovers);
            
            if( !txtArComments.getText().trim().equals(currentApprovalBean.getComments() )){
                currentApprovalBean.setComments(txtArComments.getText().trim());
            }
            currentApprovalBean.setAcType(TypeConstants.UPDATE_RECORD);
            vecData.addElement(currentApprovalBean);
            
            requester.setDataObjects(vecData);
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();

            ResponderBean response = comm.getResponse();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    Vector vecUpdatedData = response.getDataObjects();
                    if( vecUpdatedData.get(1) != null ){
                        setProposalDevelopmentFormBean((ProposalDevelopmentFormBean)vecUpdatedData.get(1));
                    }
                    setSaveRequired(false);
                    //Close the form after saving
                    dlgProposalRejection.dispose();
                    rejectClicked = true;
                }else{
                    CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(ERROR_DURING_REJECTION));
                }
            }
        }else{
            txtArComments.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            ENTER_COMMENTS));
        }
    }
    
    /** Displays the form
     * @return rejectClicked holds true if proposal is rejected, false otherwise
     */
    public boolean display(){
        txtArComments.setText(currentApprovalBean.getComments());
        dlgProposalRejection.show();
        return rejectClicked;
    }

    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     *
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     *
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** Getter for property currentApprovalBean.
     * @return Value of property currentApprovalBean.
     *
     */
    public ProposalApprovalBean getCurrentApprovalBean() {
        return currentApprovalBean;
    }
    
    /** Setter for property currentApprovalBean.
     * @param currentApprovalBean New value of property currentApprovalBean.
     *
     */
    public void setCurrentApprovalBean(ProposalApprovalBean currentApprovalBean) {
        this.currentApprovalBean = currentApprovalBean;
    }
    
    /** Getter for property proposalDevelopmentFormBean.
     * @return Value of property proposalDevelopmentFormBean.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     *
     */
    public void setProposalDevelopmentFormBean(edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
      /** Update the child proposal's creation status code, if the parent proposal
     *performed actions like Submit, Approve, Reject, PostSubmission
     */
    private boolean updateChildStatus(String proposalNumber) throws CoeusException{
        final String connect = CoeusGuiConstants.CONNECTION_URL +"/ProposalActionServlet";
         boolean success = false;
        Vector data = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType( 'z' );
        request.setDataObject( proposalNumber );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if( response.isSuccessfulResponse() ){
            success = ((Boolean)response.getDataObject()).booleanValue();
        }else{
            throw new CoeusException(response.getMessage());
        }
        return success;
    }
    
    /**
     * Getter for property parenProposal.
     * @return Value of property parenProposal.
     */
    public boolean isParenProposal() {
        return parenProposal;
    }
    
    /**
     * Setter for property parenProposal.
     * @param parenProposal New value of property parenProposal.
     */
    public void setParenProposal(boolean parenProposal) {
        this.parenProposal = parenProposal;
    }
    
}
