/*
 * @(#)ProposalCertifyForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.propdev.bean.ProposalYNQFormBean;
import edu.mit.coeus.propdev.bean.ProposalInvestigatorFormBean;
import edu.mit.coeus.utils.question.gui.QuestionsAnswersForm;
import edu.mit.coeus.utils.question.bean.YNQBean;
import edu.mit.coeus.utils.question.bean.QuestionListBean;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * This class is used to allow the proposal investigator to certify. Each investigator
 * will be proived a set of questions of type 'I' and has to select the answers. The user
 * has to either answer all the questions or none.
 *
 * @author  Raghunath
 */
public class ProposalCertifyForm extends javax.swing.JComponent implements TypeConstants{

    private char functionType;
    private Vector vQuestions;
    private Vector vAnswers;
    private Hashtable moreExplanations;
    private CoeusDlgWindow dlgParentComponent;
    private Vector data;
    private CoeusMessageResources coeusMessageResources;
    private QuestionsAnswersForm questionAnswerForm ;
    private boolean saveRequired;
    private Vector previousAnswers;
    private String personId;
    private Vector vecPrevAns = new Vector();
    private String proposalNumber;
    private String sposorCode;
    private String personName;
    private static final String NO_QUESTIONS_AVAILABLE = "prop_inv_certForm_exceptionCode.7114";
    private ProposalInvestigatorFormBean propInvestigatorBean;
    private static char DISPLAY_MODE = 'D';

    /** Creates new form ProposalCertifyForm */
    public ProposalCertifyForm() {
    }

     /** Creates new form ProposalCertify
      * @param functionType char specifies the modify/delete/display type
      * @param vQuestions Vector contains the QuestionListBean
      * @param expList Hashtable contains the moreExplanation data for each questions
      * @param data Vector contains the proposal number and sponsor number for display
      * @param propInvestigatorBean the bean contains the investigator deatils
      * @param proposalId proposal Number
      */
    public ProposalCertifyForm(char functionType,Vector vQuestions,
                                Hashtable expList,Vector data,
                                ProposalInvestigatorFormBean propInvestigatorBean, String proposalId ) {

        this.vQuestions = vQuestions;
        this.functionType = functionType;
        this.moreExplanations = expList;
        this.data = data;
        this.propInvestigatorBean = propInvestigatorBean;
        this.vAnswers = propInvestigatorBean.getInvestigatorAnswers();
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        postInitComponents();
        enableDisableComponents();
        setListeners();
        //Bug fix 2017 - start
        if(this.vQuestions!= null && this.vQuestions.size() > 0){
            buildGui(proposalId);
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_QUESTIONS_AVAILABLE));
        }//Bug fix 2017 - start
        
    }
    
    private void enableDisableComponents(){
        if(functionType == DISPLAY_MODE){
            btnOK.setEnabled(false);
        }
    }
    
    /** Bug Fix 2017 
     *This method will display the dialog box with the form contents
     */
    private void buildGui(String proposalId){
        sposorCode = (String)data.get(0);
        proposalNumber = (String)data.get(1);
        personName = (String)data.get(2);
        personId = (String)data.get(3);

        lblProposalValue.setText(proposalNumber);
        lblSponsorValue.setText(sposorCode);
        String sponsorValue = "";
        if(functionType == ADD_MODE){

            String code = ProposalDetailAdminForm.SPONSOR_CODE;
            String desc = ProposalDetailAdminForm.SPONSOR_DESCRIPTION;
            if(desc != null){
                sponsorValue = code + " : " + desc;
            }
            lblSponsorValue.setText(sponsorValue);
            lblProposalValue.setText(proposalId);
        }
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
               "Certify for "+personName, true);

        dlgParentComponent.getContentPane().add(this);
        dlgParentComponent.pack();
        dlgParentComponent.setResizable(false);

        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));

        dlgParentComponent.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
            }
        });
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                 performWindowClosing();
                 //return;
            }
        });
       dlgParentComponent.show();
    }

	/**
	 * set the user entered answers to the proposals answers bean ie. ProposalYNQBean.
	 * This overwrites the answers available in ProposalYNQBean.
	 * @param answers the vector of answers bean ie. YNQBean
	 */

    public void setAnswers(Vector answers){

        Vector answerList = new Vector();
        for (int i = 0 ; i < answers.size() ;i++ )
        {
            YNQBean ynqbean = (YNQBean) answers.elementAt(i);
            ProposalYNQFormBean propYNQBean = (ProposalYNQFormBean) vAnswers.elementAt(i);

            propYNQBean.setQuestionId(ynqbean.getQuestionId());
            propYNQBean.setPersonId(personId);
            propYNQBean.setAnswer(ynqbean.getAnswer());
            propYNQBean.setAcType(ynqbean.getAcType());

            propYNQBean.setUpdateTimeStamp(ynqbean.getUpdateTimeStamp());
            propYNQBean.setUpdateUser(ynqbean.getUpdateUser());

            answerList.addElement(propYNQBean);
        }
        propInvestigatorBean.setInvestigatorAnswers(answerList);
        //propInvestigatorBean.setInvestigatorAnswers(answers);
    }

	/**
	 * initialize the components with the bean values. In this method, the anwers for the
	 * investigators will be stored in the previousAnswers, so that during cancelation of
	 * of the activity, the user can replace the previousAnswers back to the answers bean.
	 * Here, property change listener is getting registered with the YNQBean. Any changes
	 * in the YNQBean will be firing the event and set the 'Save required' true.
	 * This method also calls the QuestionAnswersForm, which is an individual component
	 * that display the questions and answers.
	 *
	 */

    private void postInitComponents(){
        btnMore.setFont(CoeusFontFactory.getLabelFont());
        //Bug fixed case #2698 - starts
        vQuestions = filterRequiredQuestions();
        //Bug fixed case #2698 - ends
        // Bug Fix 2017 - start
        if (vAnswers == null || vAnswers.size() == 0) {
            // Bug Fix 2017 - End
           vAnswers = new Vector();
           for(int i = 0 ; i< vQuestions.size();i++){

               QuestionListBean question = (QuestionListBean) vQuestions.elementAt(i);
               ProposalYNQFormBean answer = new ProposalYNQFormBean();
               answer.setQuestionId(question.getQuestionId());
               answer.setProposalNumber(propInvestigatorBean.getProposalNumber());
               answer.setPersonId(propInvestigatorBean.getPersonId());
               //setting all answers actype to I because if you wish to certify you
               // should answer all the questions and as there were no previous 
               // answers all should be inserted
               answer.setAcType( "I" );
               // Handle
               answer.setPersonId(personId);
               vAnswers.addElement(answer);
          }
        }
        //Bug fix : 2343 - START
        if(vQuestions != null && vQuestions.size()>0) {
            //Check for new Questions added.
            // Bug fix case #2698 starts
            // code commented because if number of Questions exceeds the number of answers it gives 
            // ArrayIndexOutOfBoundsException
            // boolean matched[] = new boolean[vAnswers.size()];
            question:for(int index = 0; index < vQuestions.size(); index++) {
                QuestionListBean questionListBean = (QuestionListBean)vQuestions.elementAt(index);
                answer:for(int subIndex = 0;subIndex < vAnswers.size(); subIndex++) {
                    // Bug fix case #2698
                    // if(matched[subIndex]) continue;
                    YNQBean ynqBean = (YNQBean) vAnswers.elementAt(subIndex);
                    if(questionListBean.getQuestionId().equalsIgnoreCase(ynqBean.getQuestionId())) {
                        // Bug fix case #2698
                        // matched[subIndex] = true;
                        // Bug fix case #2698 ends
                        continue question;
                    }
                }
                ProposalYNQFormBean ynqBean = new ProposalYNQFormBean();
                ynqBean.setQuestionId(questionListBean.getQuestionId());
                ynqBean.setAnswer(null);
                ynqBean.setAcType("I");
                vAnswers.add(ynqBean);
            }
        }
        //Bug fix : 2343 - END

        // copy the answers to previousAnswers.
        previousAnswers = new Vector();

        for (int i=0;i<vAnswers.size() ;i++ ) {

            ProposalYNQFormBean ynqBean = new ProposalYNQFormBean();
            String questionId = ((ProposalYNQFormBean) vAnswers.elementAt(i)).getQuestionId();
            String answer = ((ProposalYNQFormBean) vAnswers.elementAt(i)).getAnswer();
            String acType = ((ProposalYNQFormBean) vAnswers.elementAt(i)).getAcType();

            ynqBean.setQuestionId(questionId);
            ynqBean.setPersonId(personId);
            ynqBean.setAnswer(answer);
            ynqBean.setAcType(acType);
            ynqBean.setUpdateTimeStamp(((ProposalYNQFormBean) vAnswers.elementAt(i)).getUpdateTimeStamp() );
            ynqBean.setUpdateUser(((ProposalYNQFormBean) vAnswers.elementAt(i)).getUpdateUser() );
            previousAnswers.addElement(ynqBean);
        }

        // register the listeners
        for (int i=0;i<vAnswers.size() ;i++ ) {

            YNQBean ynqBean = (YNQBean) vAnswers.elementAt(i);

            ynqBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){

                        if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                            setSaveRequired(true);
                        }
                        if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                            setSaveRequired(true);
                        }
                        if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                            if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                                setSaveRequired(true);
                            }
                        }
                }
            });
        }
            questionAnswerForm = new QuestionsAnswersForm(functionType,vQuestions, vAnswers);
        pnlCertifyGrid.add(questionAnswerForm);
    }

    /**
     * sets listeners to OK, cancel and more buttons. Clicking of 'More' button will display
     * the 'More Explanation' window.
     */
    private void setListeners(){

        btnOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (questionAnswerForm.validateData()) {
                    setAnswers(questionAnswerForm.getAnswers());
                    dlgParentComponent.dispose();
                }

            }});

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                performWindowClosing();

            }});

        btnMore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnMore.setFunctionType(functionType);
                btnMore.setQuestionId(questionAnswerForm.getSelectedQuestionId());
                btnMore.setQuestion(questionAnswerForm.getSelectedQuestion());
                btnMore.setExplanation(moreExplanations);
                btnMore.showMore();
            }
        });

        }

    /**This method used to check for value changed before the window closing
     * or on click of close button .
     * it will fire the save confirmation message if the data changed
     */
    private void performWindowClosing(){

        // set the answers to the bean and it will trigger the saveRequired
//        setAnswers(questionAnswerForm.getAnswers());
        int option = JOptionPane.NO_OPTION;
        if(functionType != DISPLAY_MODE){
            if(isSaveRequired()){
                option
                    = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                                "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);

                if(option == JOptionPane.YES_OPTION){
                    try{
                        if (questionAnswerForm.validateData()) {
                                 setAnswers(questionAnswerForm.getAnswers());
                                dlgParentComponent.dispose();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                }else if(option == JOptionPane.NO_OPTION){
                    setSaveRequired(false);
                  //  setAnswers(previousAnswers);
                    dlgParentComponent.dispose();
                }
            }else{
                setSaveRequired(false);
                dlgParentComponent.dispose();
            }
        }else{
            dlgParentComponent.dispose();
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

        pnlCertifyGrid = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        pnlButtons = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnMore = new edu.mit.coeus.utils.question.gui.MoreButton();

        setLayout(new java.awt.GridBagLayout());

        pnlCertifyGrid.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlCertifyGrid.setMaximumSize(new java.awt.Dimension(610, 350));
        pnlCertifyGrid.setMinimumSize(new java.awt.Dimension(610, 350));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(pnlCertifyGrid, gridBagConstraints);

        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        pnlProposalDescription.setPreferredSize(new java.awt.Dimension(380, 40));
        lblProposalNo.setText("Proposal Number:");
        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalValue.setPreferredSize(new java.awt.Dimension(270, 17));
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
        lblSponsorValue.setPreferredSize(new java.awt.Dimension(270, 17));
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(270, 17));
        lblSponsorValue.setMaximumSize(new java.awt.Dimension(270, 17));
        lblSponsorValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblSponsorValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        add(pnlProposalDescription, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMaximumSize(new java.awt.Dimension(75, 25));
        btnOK.setMinimumSize(new java.awt.Dimension(75, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlButtons.add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(75, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnMore.setMnemonic('M');
        btnMore.setText("More");
        btnMore.setMaximumSize(new java.awt.Dimension(75, 25));
        btnMore.setMinimumSize(new java.awt.Dimension(75, 25));
        btnMore.setPreferredSize(new java.awt.Dimension(75, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        pnlButtons.add(btnMore, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        add(pnlButtons, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }

    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }

    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }

    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }

	/**
	 * returns the final answers set for the investigator
	 */
    public Vector getFormData(){
       return propInvestigatorBean.getInvestigatorAnswers();
    }
    
    //Bug fixed case #2698 - starts
    /**
     * To filter the active questions and Inactive questions which is having answers.
     * @return Vector
     */    
    public Vector filterRequiredQuestions(){
        Vector vecFiltQues = new Vector();
        if(vQuestions!=null && vQuestions.size()>0){
            for(int index=0 ; index < vQuestions.size() ; index++){
                QuestionListBean questionListBean = (QuestionListBean)vQuestions.elementAt(index);
                if(questionListBean.getStatus().equalsIgnoreCase("I")){
                    boolean isPresent = false;
                    if(vAnswers!=null && vAnswers.size()>0){
                        for(int subIndex = 0;subIndex < vAnswers.size(); subIndex++) {
                            YNQBean ynqBean = (YNQBean) vAnswers.elementAt(subIndex);
                            if(questionListBean.getQuestionId().equalsIgnoreCase(ynqBean.getQuestionId())) {
                                isPresent = true;
                                break;
                            }
                        }
                    }
                    if(isPresent){
                        vecFiltQues.addElement(questionListBean);
                    }
                } else {
                    vecFiltQues.addElement(questionListBean);
                }
            }
        }
        return vecFiltQues;
    }
    // Bug fixed case #2698 - ends
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public edu.mit.coeus.utils.question.gui.MoreButton btnMore;
    public javax.swing.JButton btnOK;
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalValue;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlCertifyGrid;
    public javax.swing.JPanel pnlProposalDescription;
    // End of variables declaration//GEN-END:variables

}
