/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing.gui;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Screen to allow the user to Recall the proposal
 * RoutingRecallForm.java
 *
 * @author Maharaja Palanichamy
 * Created on May 12, 2011, 10:58 AM
 */

public class RoutingRecallForm extends javax.swing.JComponent
        implements ActionListener {
    
    private CoeusDlgWindow dlgRecallWindow;
    
    private static final int WIDTH = 420;
    private static final int HEIGHT = 200;
      
    /** Flag to check if the proposal is recalled */
    private boolean recallClicked = false;
    
    /** Flag to check if any data is modified */
    private boolean saveRequired = false;
    
    private static final String RECALL_ACTION = "RE";
    
    private static final char PROPOSAL_RECALL_UPDATE = 'U';
    
    private static final char PROPOSAL_APPROVE_UPDATE = 'Q';
    
    /** Holds the current Approval bean from the routing table */
    private RoutingDetailsBean currentApprovalBean;
    
    /** Holds an instance of the ProposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private static final String ENTER_COMMENTS = "proposal_Action_exceptionCode.8009";
    private static final String ERROR_DURING_RECALL = "proposal_Action_exceptionCode.8019";
    private static final String SAVE_COMMENTS = "routing_comments_exceptionCode.1007";
    private static final String RECALL_PROPOSAL = "proposal_Action_exceptionCode.8021";
    private static final String CONFIRM_RECALL_PROPOSAL = "proposal_Action_exceptionCode.8022";
    private static final String RECALL_COMMENTS = "proposal_Action_exceptionCode.8026";
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private boolean parenProposal;
    
    private int moduleCode;
    private String moduleItemKey;
    private Object moduleBean;
    private RoutingBean routingBean;
    private boolean saveComment = false;
    private CoeusVector cvComments;
    private CoeusVector cvCommentsData;
    private static final int COMMENTS_COLUMN = 0;
    private static final int MORE_COMMENTS_COLUMN = 1;   
    private int submissionStatusCode;
    
    // JM 3-24-2014 rolling our own routing
    private static final char UPDATE_ROUTING_APPROVALS = 'U';
    private static final String ROUTING_QUEUE_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/RoutingQueueServlet";
    // JM END
    
    /**
     * Creates new form RoutingRecallForm
     *
     * @param parent holds the frame
     * @param modal holds true if modal, false otherwise
     */
    public RoutingRecallForm(Component parent, int moduleCode, String moduleItemKey, boolean modal) {
        this.moduleCode = moduleCode;
        this.moduleItemKey = moduleItemKey;
        cvComments = new CoeusVector();
        initComponents();
        postInitComponents();
        registerComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form. */
    private void postInitComponents(){
        dlgRecallWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Recall", true);
        dlgRecallWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRecallWindow.getContentPane().add(this);
        dlgRecallWindow.setResizable(false);
         String title = "Proposal Recall";
        if(isProtocol(moduleCode)){
            title = "Protocol Recall";
            lblTitle.setText("Confirm your Recall of the Protocol");
        }
        dlgRecallWindow.setTitle(title);
        dlgRecallWindow.setFont(CoeusFontFactory.getLabelFont());
        dlgRecallWindow.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRecallWindow.getSize();
        dlgRecallWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
    }
    
    /** This method is used to set the listeners to the components. */
    private void registerComponents(){
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtArCommentsList, btnRecall, btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        /** Code for focus traversal - end */
        
        //Add listeners to components
        btnRecall.addActionListener(this);
        btnCancel.addActionListener(this);
        
        dlgRecallWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        
        dlgRecallWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        
        dlgRecallWindow.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                txtArCommentsList.requestFocus();
            }
        });
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnRecall = new javax.swing.JButton();
        tbdPnAttAndCommts = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        scrPnComments1 = new javax.swing.JScrollPane();
        txtArCommentsList = new javax.swing.JTextArea();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(420, 200));
        setMinimumSize(new java.awt.Dimension(420, 200));
        setPreferredSize(new java.awt.Dimension(420, 200));
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnCancel, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Confirm your Recall of the Proposal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblTitle, gridBagConstraints);

        btnRecall.setFont(CoeusFontFactory.getLabelFont());
        btnRecall.setMnemonic('Z');
        btnRecall.setText("Recall");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnRecall, gridBagConstraints);

        tbdPnAttAndCommts.setMaximumSize(new java.awt.Dimension(315, 70));
        tbdPnAttAndCommts.setMinimumSize(new java.awt.Dimension(315, 70));
        tbdPnAttAndCommts.setName("tbdPnAttAndCommts");
        tbdPnAttAndCommts.setPreferredSize(new java.awt.Dimension(315, 70));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(350, 25));
        jPanel1.setMinimumSize(new java.awt.Dimension(350, 25));
        jPanel1.setPreferredSize(new java.awt.Dimension(350, 25));
        scrPnComments1.setMaximumSize(new java.awt.Dimension(310, 70));
        scrPnComments1.setMinimumSize(new java.awt.Dimension(310, 70));
        scrPnComments1.setPreferredSize(new java.awt.Dimension(310, 70));
        txtArCommentsList.setDocument(new LimitedPlainDocument(300));
        txtArCommentsList.setFont(CoeusFontFactory.getNormalFont());
        txtArCommentsList.setLineWrap(true);
        txtArCommentsList.setWrapStyleWord(true);
        txtArCommentsList.setMaximumSize(new java.awt.Dimension(310, 70));
        txtArCommentsList.setMinimumSize(new java.awt.Dimension(310, 70));
        txtArCommentsList.setPreferredSize(new java.awt.Dimension(310, 70));
        scrPnComments1.setViewportView(txtArCommentsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(scrPnComments1, gridBagConstraints);

        tbdPnAttAndCommts.addTab("Comments", jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        add(tbdPnAttAndCommts, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnRecall;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JScrollPane scrPnComments1;
    public javax.swing.JTabbedPane tbdPnAttAndCommts;
    public javax.swing.JTextArea txtArCommentsList;
    // End of variables declaration//GEN-END:variables
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            dlgRecallWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(btnRecall) ){
                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(RECALL_PROPOSAL),
                        CoeusOptionPane.OPTION_OK_CANCEL,2);
                switch( option ){
                    case ( JOptionPane.OK_OPTION ):
                        if(txtArCommentsList.getText().trim().length()>0){
                            if(checkForLastStageApproval()){
                                performRecallOperation();
                                // Update all the child proposoal status
                                if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                                    if(isParenProposal()){
                                        updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                                    }
                                }
                            }
                            break;
                        }else{
                            CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(RECALL_COMMENTS),
                                CoeusOptionPane.OPTION_OK,1);
                        }
                    case ( JOptionPane.CANCEL_OPTION ) :
                        break;
                }
            }else if( source.equals(btnCancel) ){
                performWindowClosing();
            }            
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            dlgRecallWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){
        int option = JOptionPane.NO_OPTION;
        if(isSaveRequired()){
            String messageKey = "saveConfirmCode.1002";
                        
            option
                    = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(messageKey),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            switch( option ){
                case ( JOptionPane.YES_OPTION ):
                    try{
                        performRecallOperation();
                        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                        }
                    }catch(Exception e){
                        e.getMessage();
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    dlgRecallWindow.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    break;
            }
        }else{
            dlgRecallWindow.dispose();
        }
    }
    
    /**
     * This method is used to perform the recall action    
     */
    private void performRecallOperation(){
        if(txtArCommentsList.getText().trim().length() > 0){
            performCommentsAction("Update");            
        }
        if( currentApprovalBean.getComments() != null
                && currentApprovalBean.getComments().size() > 0 ){
            currentApprovalBean.setAction(RECALL_ACTION);
            currentApprovalBean.setApproveAll(0);
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(UPDATE_ROUTING_APPROVALS); // JM 3-24-2014 our own routing
            Vector vecData = new Vector();
            currentApprovalBean.setAcType(TypeConstants.UPDATE_RECORD);
            vecData.addElement(null);
            vecData.addElement(currentApprovalBean);
            vecData.addElement(routingBean);
            String CONNECT_TO = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
            requester.setDataObjects(vecData);
            // JM 3-24-2014 our routing
            AppletServletCommunicator comm = new AppletServletCommunicator(ROUTING_QUEUE_SERVLET, requester);
            comm.send();
            // JM END
            
            ResponderBean response = comm.getResponse();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    Vector vecUpdatedData = response.getDataObjects();
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        if( vecUpdatedData.get(1) != null ){
                            setProposalDevelopmentFormBean((ProposalDevelopmentFormBean)vecUpdatedData.get(1));
                            setModuleBean(vecUpdatedData.get(1));
                        }
                    }
                    setSaveRequired(false);
                    //Close the form after saving
                    dlgRecallWindow.dispose();
                    recallClicked = true;
                }else{
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(ERROR_DURING_RECALL));
                }
            }
        }else{
            txtArCommentsList.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    ENTER_COMMENTS));
        }
    }
    
    /** Displays the form
     * @return recallClicked holds true if proposal is recalled, false otherwise
     */
    public boolean display(){
        dlgRecallWindow.show();
        return recallClicked;
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
    public RoutingDetailsBean getCurrentApprovalBean() {
        return currentApprovalBean;
    }
    
    /** Setter for property currentApprovalBean.
     * @param currentApprovalBean New value of property currentApprovalBean.
     *
     */
    public void setCurrentApprovalBean(RoutingDetailsBean currentApprovalBean) {
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
     *performed actions like Submit, Approve, Reject, PostSubmission, Recall
     */
    private boolean updateChildStatus(String proposalNumber) throws CoeusException{
        final String connect = CoeusGuiConstants.CONNECTION_URL +"/ProposalActionServlet";
        boolean success = false;
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
    
    /**
     * Getter for property moduleBean.
     * @return Value of property moduleBean.
     */
    public Object getModuleBean() {
        return moduleBean;
    }
    
    /**
     * Setter for property moduleBean.
     * @param moduleBean New value of property moduleBean.
     */
    public void setModuleBean(Object moduleBean) {
        this.moduleBean = moduleBean;
    }

    /**
     * Getter for property routingBean.
     * @return Value of property routingBean.
     */
    public RoutingBean getRoutingBean() {
        return routingBean;
    }

    /**
     * Setter for property routingBean.
     * @param routingBean New value of property routingBean.
     */
    public void setRoutingBean(RoutingBean routingBean) {
        this.routingBean = routingBean;
    }
    
    /**
     * Method used to save the comments.
     * @param action code containing the performed action
     */
    public void performCommentsAction(String action){
        if(action.equals("Update")){
            if(txtArCommentsList.getText().trim().length()==0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1006"));
                return;
            }
            RoutingCommentsBean routingCommentsBean = new RoutingCommentsBean();
            routingCommentsBean.setRoutingNumber(currentApprovalBean.getRoutingNumber());
            routingCommentsBean.setApproverNumber(currentApprovalBean.getApproverNumber());
            routingCommentsBean.setComments(txtArCommentsList.getText());
            routingCommentsBean.setLevelNumber(currentApprovalBean.getLevelNumber());
            routingCommentsBean.setMapNumber(currentApprovalBean.getMapNumber());
            routingCommentsBean.setStopNumber(currentApprovalBean.getStopNumber());
            routingCommentsBean.setAcType("I");
            routingBean.setRoutingEndUser(CoeusGuiConstants.getMDIForm().getUserId());
            routingBean.setRecallEndDate(CoeusUtils.getDBTimeStamp());
            if(currentApprovalBean!=null){
                if(currentApprovalBean.getComments()==null){
                    CoeusVector cvComments = new CoeusVector();
                    cvComments.add(routingCommentsBean);
                    currentApprovalBean.setComments(cvComments);
                }else{
                    currentApprovalBean.getComments().add(routingCommentsBean);
                }
            }
            txtArCommentsList.setText("");
            saveComment = true;
        }
    }
    
    /**
     * Method used to check whether the proposal/protocol is in the last stage of approval in the routing map 
     * before performing the Recall action.
     * @return returnFlag
     */
    public boolean checkForLastStageApproval(){
        boolean returnFlag = false;
        final String connect = CoeusGuiConstants.CONNECTION_URL +"/RoutingServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('V');
        request.setDataObject(routingBean.getRoutingNumber().toString());
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if( response.isSuccessfulResponse() ){
            boolean success = false;
            //counter to match the vector size
            int counter = 0;
            //counter to check for last map in case of multiple maps
            int stopDelimiter = 0;
            Vector vctApprovalMaps = ((Vector)response.getDataObjects());
            if(vctApprovalMaps == null){
                vctApprovalMaps = new Vector();
            }
            //to check whether the proposal/protocol is in the last approval stage in last sequential stop
            for(Object routingData:vctApprovalMaps){
                stopDelimiter++;
                RoutingMapBean routingMapBean = (RoutingMapBean)routingData;
                //fetch the routing map details
                CoeusVector cvRoutingMap = routingMapBean.getRoutingMapDetails();
                for(Object routingMapData:cvRoutingMap){
                    ++counter;
                    RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)routingMapData;
                    //to check for last map in case of multiple maps
                    if(vctApprovalMaps.size() == stopDelimiter){
                        if(cvRoutingMap.size()>1){
                            if((counter+1) == cvRoutingMap.size()){
                                //fetch the approval status for the approval stage just above the last approval
                                String approvalStatus = routingDetailsBean.getApprovalStatus();
                                if(approvalStatus.equals("W") || approvalStatus.equals("T")){
                                    success = false;
                                }else{
                                    success = true;
                                }
                            }
                        }else{
                            //fetch the approval status for the approval stage just above the last approval
                            String approvalStatus = routingDetailsBean.getApprovalStatus();
                            if(approvalStatus.equals("W") || approvalStatus.equals("T")){
                                success = true;
                            }else{
                                success = false;
                            }
                        }
                    }
                }
            }
            //if proposal/protocol is in the last stage of approval then show message
            if(success){
                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(CONFIRM_RECALL_PROPOSAL),
                        CoeusOptionPane.OPTION_OK_CANCEL,2);
                switch( option ){
                    case ( JOptionPane.OK_OPTION ):
                        returnFlag = true;
                        break;
                    case ( JOptionPane.CANCEL_OPTION ) :
                        returnFlag = false;
                        break;
                }
            }else{
                returnFlag = true;
            }
        }
        return returnFlag;
    }
    
    /**
     * This method is used to check whether the module is protocol or proposal
     * @param moduleCode
     * @return isProtocol
     */
    private boolean isProtocol(int moduleCode){
        boolean isProtocol = false;
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE || moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            isProtocol = true;
        }
        return isProtocol;
    }

    /**
     * Getter for property submissionStatusCode.
     * @return Value of property submissionStatusCode.
     */
    public int getSubmissionStatusCode() {
        return submissionStatusCode;
    }

    /**
     * Setter for property submissionStatusCode.
     * @param submissionStatusCode New value of property submissionStatusCode.
     */
    public void setSubmissionStatusCode(int submissionStatusCode) {
        this.submissionStatusCode = submissionStatusCode;
    }
}
