/*
 * ProposalYesNoQuestionsForm.java
 *
 * Created on May 19, 2003, 11:42 AM
 */

/* PMD check performed, and commented unused imports and
 * variables on 10th Nov 2009 by Satheesh Kumar K N
 */

package edu.mit.coeus.propdev.gui;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.question.bean.QuestionListBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;

import javax.swing.*;
import java.util.*;
//import java.awt.event.ActionEvent;
//import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
//import java.awt.event.ComponentAdapter;
//import javax.swing.AbstractAction;
import javax.swing.event.*;

/**
 *
 * @author  Raghunath
 */
public class ProposalYesNoQuestionsForm extends javax.swing.JComponent implements TypeConstants {
    private String proposalID;
    private QuestionListBean[] questionList;
    private ProposalYNQBean[] proposalQuestionList;
    private Vector vecAnswerList, vecQuestionList;
    private Vector answerListCopy;
    private Hashtable explanation;
    private char functionType;
//    private JDialog dlgParentComponent;
    private CoeusDlgWindow dlgParentComponent;
    private boolean saveRequired;
    private QuestionForm questionForm;
    private static final char SAVE_QUESTION_ANSWERS = 's';
    private String sponsorName;
    private Vector vecProposalQuestionTable;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;

    //Bug Fix 1565: Start 1
    private Hashtable moreExplanation;
    //Bug Fix 1565: End 1
    
    //Bug Fix 1665 Start 1
//    private boolean inactiveQuesPresent = false;
    //Bug Fix 1665 End 1
    
    /** Creates new form ProposalYesNoQuestionsForm */
    public ProposalYesNoQuestionsForm() {
    }

    /** Creates new form ProposalYesNoQuestionsForm */
    public ProposalYesNoQuestionsForm(Vector vecQuestionList, Vector vecAnswerList, Hashtable hashExplanation) {

        this.questionList = (QuestionListBean[]) vecQuestionList.toArray(new QuestionListBean[vecQuestionList.size()]);
        this.proposalQuestionList = (ProposalYNQBean[]) vecAnswerList.toArray(new ProposalYNQBean[vecAnswerList.size()]);
        this.explanation = hashExplanation;
        vecProposalQuestionTable = new Vector();
        initComponents();
        //System.out.println("Inside ProposalYesNoQuestionsForm constructor");
        //System.out.println("questionList == null " + (questionList==null) + ", proposalQuestionList = null " + (proposalQuestionList == null) + ", explanation == null " + (explanation==null));
        try{
            answerListCopy = (Vector)ObjectCloner.deepCopy(vecAnswerList);
        }
        catch(Exception ex){
            ex.printStackTrace();
            //System.out.println("Exception raised while deep copying the answer list.");
        }
        questionForm = new QuestionForm(functionType, questionList, proposalQuestionList,explanation);
        questionForm.setSize(pnlQuestionTable.getSize());
        pnlQuestionTable.add(questionForm);
        postInitComponents();
        showDialog();
    }

    /** Creates new form ProposalYesNoQuestionsForm */
    public ProposalYesNoQuestionsForm(String proposalID, char functionType) {

        vecProposalQuestionTable = new Vector();
        this.proposalID = proposalID;
        this.functionType = functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
    }

    public void showProposalQuestionForm(){
        //System.out.println("Inside showProposalQuestionForm()");
        //System.out.println("questionList == null " + (questionList==null) + ", proposalQuestionList = null " + (proposalQuestionList == null) + ", explanation == null " + (explanation==null));
        //getQuestionData();
        initComponents();
        initAnswerList();
        
        //Bug Fix 1565: Start 2
        //Changed the constructor to accept one more Value
        //questionForm = new QuestionForm(functionType, questionList, proposalQuestionList,explanation);
        //Bug Fix #2017. If there are no questions then disable the OK button - start
        if(questionList.length == 0){
            btnOK.setEnabled(false);
        }//Bug Fix #2017 - End
        questionForm = new QuestionForm(functionType, questionList, proposalQuestionList,explanation,moreExplanation);
        //Bug Fix 1565: End 2
        
        questionForm.setSize(pnlQuestionTable.getSize());
        pnlQuestionTable.add(questionForm);
        postInitComponents();
        showDialog();
    }

    private void showDialog(){
//        dlgParentComponent = new JDialog(CoeusGuiConstants.getMDIForm(),
//        "Proposal Question", true);
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
        "Proposal Question", true);
        
        dlgParentComponent.getContentPane().add(this);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);
        
        Dimension screenSize
        = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
//        dlgParentComponent.addKeyListener(new KeyAdapter(){
//            public void keyReleased(KeyEvent ke){
//                if(ke.getKeyCode() == KeyEvent.VK_ESCAPE){
//                    performWindowClosing();
//                }
//            }
//        });
        
          dlgParentComponent.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        // Bug fix # 1435 - start
        dlgParentComponent.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                if(functionType==DISPLAY_MODE){
                    btnCancel.requestFocusInWindow();
                }else{
                    btnOK.requestFocusInWindow();
                }
            }
        });// Bug fix # 1435 - start
        dlgParentComponent.show();
    }
    
    public boolean isSaveRequired(){
        // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - Start
//        if (functionType != DISPLAY_MODE) {
//            saveRequired = questionForm.isSaveRequired();
//        }else {
//            saveRequired = false;
//        }
        if (functionType != DISPLAY_MODE && questionForm != null) {
            saveRequired = questionForm.isSaveRequired();
        }else {
            saveRequired = false;
        }
        // Modified for COEUSQA-2881 : Last Update User and Timestamp changes - End
        
        return saveRequired;
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the educational details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){
        // Bug fix # 1435 - start
        int rowCount = questionForm.getQuestTable().getTblQuestionTable().getRowCount();
        if( rowCount > 0 && questionForm.getQuestTable().getTblQuestionTable().isEditing()){
            questionForm.getQuestTable().getTblQuestionTable().getCellEditor().stopCellEditing();
        }// Bug fix # 1435 - End
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
                    saveQuestionDetails();
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == JOptionPane.NO_OPTION){
                //saveRequired = false;
                questionForm.setSaveRequired(false);
                //proposalQuestionList = answerListCopy;
                vecProposalQuestionTable = answerListCopy;
                
                //Bug Fix: 1665 Start 2
                questionForm.setInactiveQuesPresent(false);
                //Bug Fix: 1665 End 2
                
                dlgParentComponent.dispose();
            }
        }else{
            /** Bug Fix 2073
             */
            questionForm.setSaveRequired(false);
            vecProposalQuestionTable = answerListCopy;
            questionForm.setInactiveQuesPresent(false);
            dlgParentComponent.dispose();
        }
    }
    
    /**
     * This method will initialize the proposalQuestionList - a vector that contains the array of
     * ProposalYNQBean. Actually the initialization is done on this ProposalYNQBean setting all
     * the values to null and action type as I (Insert mode).
     */
    private void initAnswerList(){
        //System.out.println("Entering initAnswerList");
        //Bug fix case #2045 for one question by tarique start
        if (proposalQuestionList == null || proposalQuestionList.length == 0 ) {
            //Bug fix case #2045 for one question by tarique end
            //System.out.println("The AnswerList is null in initProposalQuestionList().");
            proposalQuestionList = new ProposalYNQBean[questionList.length];
            
            for (int i = 0; i < questionList.length; i++) {
                proposalQuestionList[i] = new ProposalYNQBean();
                proposalQuestionList[i].setQuestionId(((edu.mit.coeus.utils.question.bean.QuestionListBean)questionList[ i ]).getQuestionId());
                proposalQuestionList[i].setProposalNumber(proposalID);
                proposalQuestionList[i].setAcType("I");
                proposalQuestionList[i].setAnswer("");
                //proposalQuestionList.addElement(yqnArray[i]);
            }
        }
    }
    
    /**
     * This method will check whether the SAVE is required. If REQUIRED then the ProposalYNQBean's
     * are send to the Server for updation.
     */
    private void saveQuestionDetails(){
        ////System.out.println("questionForm.validateData() =" + questionForm.validateData());
        if (!questionForm.validateData()){
            return;
        }
        //sendUpdatedYNQBean();
        updateDataVector();
        dlgParentComponent.dispose();
    }
    
    private void updateDataVector(){
        
        //Bug Fix 1665: Start 4
        //for (int index=0; index<proposalQuestionList.length; index++){
        //    vecProposalQuestionTable.addElement(proposalQuestionList[index]);
        //}
        
        ProposalYNQBean[] propQestList = questionForm.getPropQuestionList();
        for (int index=0; index<propQestList.length; index++){
            //Code added for coeus4.3 YNQ enhancement.
            //To save only the answered questions and answers.
            if(propQestList[index]!=null && propQestList[index].getAnswer()!=null
                    && !propQestList[index].getAnswer().equals("")){
                vecProposalQuestionTable.addElement(propQestList[index]);
            }
        }
        
    }
    
    /**
     * Getter for property inactiveQuesPresent.
     * @return Value of property inactiveQuesPresent.
     */
    public boolean isInactiveQuesPresent() {
        return questionForm.isInactiveQuesPresent();
    }
    //Bug Fix 1665: End 4
    
    private void sendUpdatedYNQBean(){
        //System.out.println("Inside the sendUpdatedYNQBean()");
        vecProposalQuestionTable = new Vector();
        for (int index=0; index<proposalQuestionList.length; index++){
            vecProposalQuestionTable.addElement(proposalQuestionList[index]);
        }
        String connectTo = CoeusGuiConstants.CONNECTION_URL
        + CoeusGuiConstants.PROPOSAL_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(SAVE_QUESTION_ANSWERS);
        request.setId(proposalID);
        request.setDataObject(vecProposalQuestionTable);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                dlgParentComponent.dispose();
            }
        }
        return;
    }
    
    /**
     * This method will set the listeners for the OK and Cancel button
     */
    private void postInitComponents() {
        btnOK.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                //System.out.println("OK Button");
                saveQuestionDetails();
            }
        });
        
        btnCancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                //System.out.println("Cancel Button");
                performWindowClosing();
            }
        });
        if(functionType == DISPLAY_MODE){
            btnOK.setEnabled(false);
        }
        lblProposalValue.setText(proposalID);
        lblSponsorValue.setText(sponsorName);
    }
    
    /**
     * This method will communicate with server to get the question table data
     */
    public void getQuestionData(){
        try{
            RequesterBean requester = new RequesterBean();
            requester.setId(proposalID);
            requester.setFunctionType( 'T' );
            String connectTo = CoeusGuiConstants.CONNECTION_URL +
            CoeusGuiConstants.PROPOSAL_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if (res != null && !res.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(res.getMessage());
                return;
            }
            Vector dataObject = null;
            Vector vQuestionList = null;
            Vector answerList = null;
            try {
                dataObject = (Vector)res.getDataObjects();
                vQuestionList = (Vector)dataObject.get(0);
                answerList = (Vector)dataObject.get(1);
                explanation = (Hashtable)dataObject.get(2);
            }
            catch(NullPointerException npe) {
                npe.printStackTrace();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            
            questionList = (QuestionListBean[]) vQuestionList.toArray(new QuestionListBean[vQuestionList.size()]);
            proposalQuestionList = (ProposalYNQBean[]) answerList.toArray(new ProposalYNQBean[answerList.size()]);
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlDescriptionContainer = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        pnlQuestionContainer = new javax.swing.JPanel();
        pnlQuestionTable = new javax.swing.JPanel();
        pnlButtons = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        pnlDescriptionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setText("Proposal Number:");
        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblProposalValue, gridBagConstraints);

        lblSponsor.setText("Sponsor:");
        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(270, 17));
        lblSponsorValue.setMaximumSize(new java.awt.Dimension(270, 17));
        lblSponsorValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblSponsorValue, gridBagConstraints);

        pnlDescriptionContainer.add(pnlProposalDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlDescriptionContainer, gridBagConstraints);

        pnlQuestionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        pnlQuestionTable.setLayout(new java.awt.GridBagLayout());

        pnlQuestionContainer.add(pnlQuestionTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlQuestionContainer, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOK.setMnemonic('O');
        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setText("OK");
        btnOK.setPreferredSize(new java.awt.Dimension(82, 23));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        pnlButtons.add(btnOK, gridBagConstraints);

        btnCancel.setMnemonic('C');
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(82, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        pnlButtons.add(btnCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);
        add(pnlButtons, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOKActionPerformed

    /** Getter for property vecProposalQuestionTable.
     * @return Value of property vecProposalQuestionTable.
     */
    public java.util.Vector getVecProposalQuestionTable() {
        return vecProposalQuestionTable;
    }

    /** Setter for property vecProposalQuestionTable.
     * @param vecProposalQuestionTable New value of property vecProposalQuestionTable.
     */
    public void setVecProposalQuestionTable(java.util.Vector vecProposalQuestionTable) {
        this.vecProposalQuestionTable = vecProposalQuestionTable;
    }

    /** Getter for property vecAnswerList.
     * @return Value of property vecAnswerList.
     */
    public java.util.Vector getVecAnswerList() {
        return vecAnswerList;
    }

    /** Setter for property vecAnswerList.
     * @param vecAnswerList New value of property vecAnswerList.
     */
    public void setVecAnswerList(java.util.Vector vecAnswerList) {
        this.vecAnswerList = vecAnswerList;
        if (vecAnswerList != null){
            this.proposalQuestionList = (ProposalYNQBean[]) vecAnswerList.toArray(new ProposalYNQBean[vecAnswerList.size()]);
        }
        try{
            answerListCopy = (Vector)ObjectCloner.deepCopy(vecAnswerList);
        }
        catch(Exception ex){
            ex.printStackTrace();
            //System.out.println("Exception raised while copying the answer list inside setVecAnswerList()");
        }
    }

    /** Getter for property vecQuestionList.
     * @return Value of property vecQuestionList.
     */
    public java.util.Vector getVecQuestionList() {
        return vecQuestionList;
    }

    /** Setter for property vecQuestionList.
     * @param vecQuestionList New value of property vecQuestionList.
     */
    public void setVecQuestionList(java.util.Vector vecQuestionList) {
        this.vecQuestionList = vecQuestionList;
        this.questionList = (QuestionListBean[]) vecQuestionList.toArray(new QuestionListBean[vecQuestionList.size()]);
    }

    /** Getter for property explanation.
     * @return Value of property explanation.
     */
    public java.util.Hashtable getExplanation() {
        return explanation;
    }

    /** Setter for property explanation.
     * @param explanation New value of property explanation.
     */
    public void setExplanation(java.util.Hashtable explanation) {
        this.explanation = explanation;
    }

    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }

    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }

    //Bug Fix 1565: Start 3
    /**
     * Getter for property moreExplanation.
     * @return Value of property moreExplanation.
     */
    public java.util.Hashtable getMoreExplanation() {
        return moreExplanation;
    }
    
   /**
     * Setter for property moreExplanation.
     * @param moreExplanation New value of property moreExplanation.
     */
    public void setMoreExplanation(java.util.Hashtable moreExplanation) {
        this.moreExplanation = moreExplanation;
    }
    //Bug Fix 1565: End 3
    
    // Added for COEUSQA-2881 : Last Update User and Timestamp changes - Start
    /**
     * This method is introduced to change the saverequired set to False after the proposal data upadted to the database
     * This method is exclusively called from ProposalDetailForm
     */
    public void setSaveRequired(boolean newSaveRequired){
        saveRequired = newSaveRequired;
        if(questionForm != null){
            questionForm.setSaveRequired(newSaveRequired);
            questionForm.getQuestTable().setSaveRequired(newSaveRequired);
        }
    }
    
    // Added for COEUSQA-2881 : Last Update User and Timestamp changes - End
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalValue;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlDescriptionContainer;
    public javax.swing.JPanel pnlProposalDescription;
    public javax.swing.JPanel pnlQuestionContainer;
    public javax.swing.JPanel pnlQuestionTable;
    // End of variables declaration//GEN-END:variables

}
