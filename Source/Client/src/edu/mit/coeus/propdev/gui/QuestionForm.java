/*
 * @(#)QuestionForm.java 1.0 8/31/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import edu.mit.coeus.propdev.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.question.bean.QuestionListBean;
import edu.mit.coeus.propdev.bean.ProposalYNQBean;
import edu.mit.coeus.propdev.bean.ProposalYNQExplantionFormBean;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.organization.bean.YNQExplanationBean;
import java.util.Date;

/**
 * This class constructs the Organization detail's question tab form
 *
 * @version :1.0 August 31, 2002, 1:35 PM
 * @author Guptha K
 */
public class QuestionForm extends JPanel implements ListSelectionListener, TypeConstants{

    private JPanel pnlQuestion;
    private QuestionTable questTable;
    private JLabel lblExplanation;
    private JLabel lblReviewDate;
    private CoeusTextField txtReviewDate;
    private JButton btnMore;
    private JScrollPane scrlPnExplanation;
    private JScrollPane scrlPnQuestionTable;
    public JTextArea txtrExplanation;

    private boolean saveRequired;
    private QuestionListBean[] questionList;
    private ProposalYNQBean[] proposalQuestionList;
    char functionType;
    private Hashtable exp;
    private Hashtable expList;
    private String selectedQuestionId;
    private String selectedQuestion;
    private DateUtils dateUtils;
    private String focusDate;
    String proposalId;
    //Bug Fix 2017 - start
    private boolean isEditable;
    //Bug Fix 2017 - End
    private static final char QUESTION_ANSWER = 'T';
    
    //Bug Fix 1565: Start 1
    private Hashtable moreExpList;
    //Bug Fix 1565: End 1
    
    //Bug Fix 1665: Start 1
    private boolean inactiveQuesPresent = false;
    //Bug Fix 1665: End 1
    
    public QuestionForm() {
        
    }

    public QuestionForm(String proposalId, char functionType) {
        this.proposalId = proposalId;
        this.functionType = functionType;
        try {
        showQuestionForm();
        }catch(CoeusClientException coeusClientException) {
            CoeusOptionPane.showDialog(coeusClientException);
        }
    }
    /**
     * Constructor which instantiates QuestionForm and populates them with data
     * specified, in Organization Module. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param questionList
     * @param orgQuestionList
     * @param expList
     */
    
    public QuestionForm(char functionType,QuestionListBean[] questionList, ProposalYNQBean[] proposalQuestionList,Hashtable expList) {
        this.functionType=functionType;
        //this.proposalId = proposalNumber;
        this.questionList = questionList;
        
        //Bug Fix 1665: Start 2
        //this.proposalQuestionList = proposalQuestionList;
        ProposalYNQBean[] porposalYNQBean = proposalQuestionList;
        preparePorpQuestionList(porposalYNQBean);
        //Bug Fix 1665: End 2
        
        this.expList = expList;
        initComponents();
        formatFields();
    }
    
    //Bug Fix 1565: Start 2 created new constructor to accept on more value - moreExpList
    /**
     * Constructor which instantiates QuestionForm and populates them with data
     * specified, in Organization Module. And sets the enabled status
     * for all components depending on the functionType specified.
     * @param functionType Character which specifies the mode in which the
     * form will be shown.
     * 'A' specifies that the form is in Add Mode
     * 'M' specifies that the form is in Modify Mode
     * 'D' specifies that the form is in Display Mode
     * @param questionList
     * @param orgQuestionList
     * @param expList
     * @param moreExpList
     */
    
    public QuestionForm(char functionType,QuestionListBean[] questionList, ProposalYNQBean[] proposalQuestionList
                        ,Hashtable expList,Hashtable moreExpList) {
        this.functionType=functionType;
        //this.proposalId = proposalNumber;
        this.questionList = questionList;
        
        //Bug Fix 1665: Start 3
        //this.proposalQuestionList = proposalQuestionList;
        ProposalYNQBean[] porposalYNQBean = proposalQuestionList;
        preparePorpQuestionList(porposalYNQBean);
        //Bug Fix 1665: End 3
        
        this.expList = expList;
        this.moreExpList = moreExpList;
        initComponents();
        formatFields();
    }
    //Bug Fix 1565: End 2
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        pnlQuestion = new JPanel();
        lblExplanation = new JLabel();
        lblExplanation.setFont(CoeusFontFactory.getLabelFont());
        lblReviewDate = new JLabel();
        lblReviewDate.setFont(CoeusFontFactory.getLabelFont());
        txtReviewDate = new CoeusTextField();
        txtReviewDate.setDocument( new LimitedPlainDocument(11) );
        txtReviewDate.setPreferredSize(new Dimension(80,20));
        txtReviewDate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                validateReviewDate(txtReviewDate.getText());
            }
        });

        txtReviewDate.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                updateReviewDate(txtReviewDate.getText());
            }
        });


        txtReviewDate.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent fe){                
                if ( (txtReviewDate.getText() != null) &&  (!txtReviewDate.getText().trim().equals("")) ) {
                    txtReviewDate.setText(focusDate);
                }
            }

            public void focusLost(FocusEvent fe){
                String editingValue = null;
                if (txtReviewDate.getText().indexOf("-") < 0) {
                    if ( (txtReviewDate.getText() != null) &&  (!txtReviewDate.getText().trim().equals("")) &&
                                functionType != 'D') {
                        String convertedDate = dateUtils.formatDate(txtReviewDate.getText(), "/-:," , "dd-MMM-yyyy");
                        boolean isTemporaryFocus = fe.isTemporary();
                        if (convertedDate==null && ! isTemporaryFocus ){
                            log("Please enter valid date");
                            txtReviewDate.setText("");
                            txtReviewDate.requestFocus();                                                          
                        }else {
                            focusDate = txtReviewDate.getText();
                            txtReviewDate.setText(convertedDate);
                        }
                    }
                }
            }

        });
        btnMore = new JButton();
        btnMore.setMnemonic('M');
        btnMore.setFont(CoeusFontFactory.getLabelFont());
        btnMore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showMore();
            }
        });

        txtrExplanation = new JTextArea();
        txtrExplanation.setFont(CoeusFontFactory.getNormalFont());
        txtrExplanation.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                updateExplanation(txtrExplanation.getText());
            }
        });

        scrlPnExplanation = new JScrollPane(txtrExplanation,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrlPnExplanation.setMinimumSize(new Dimension(22, 15));
        scrlPnExplanation.setPreferredSize(new Dimension(300, 60));
        //prepare question table
        prepareQuestionTable();
        /*
        scrlPnQuestionTable = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrlPnQuestionTable.setViewportView(questTable);
        scrlPnQuestionTable.setMinimumSize(new Dimension(22, 15));
        scrlPnQuestionTable.setPreferredSize(new Dimension(580,250));
         */
        questTable.setMinimumSize(new Dimension(22, 15));
        // Modified for COEUSDEV-1135 : Unable to scroll in YNQ - Start
//        questTable.setPreferredSize(new Dimension(580,230));
        questTable.setPreferredSize(new Dimension(580,400));
        // Modified for COEUSDEV-1135 : Unable to scroll in YNQ - End
        pnlQuestion.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 4;
        //gridBagConstraints1.ipadx = 200;
        //gridBagConstraints1.ipady = 50;
        gridBagConstraints1.insets = new Insets(0, 0, 20, 0);
        //pnlQuestion.add(scrlPnQuestionTable, gridBagConstraints1);
        pnlQuestion.add(questTable, gridBagConstraints1);

        JPanel pnlFields = new JPanel();
        pnlFields.setLayout(new GridBagLayout());

        lblExplanation.setText("Explanation:");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new Insets(0, 0, 10, 10);
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        pnlFields.add(lblExplanation, gridBagConstraints1);

        lblReviewDate.setText("Review Date:");
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 10);
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        pnlFields.add(lblReviewDate, gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new Insets(0, 0, 0, 10);
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        pnlFields.add(txtReviewDate, gridBagConstraints1);

        btnMore.setText("More");
        btnMore.setPreferredSize(new Dimension(72, 26));
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 3;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.insets = new Insets(0, 0, 10, 0);
        gridBagConstraints1.anchor = GridBagConstraints.NORTH;
        pnlFields.add(btnMore, gridBagConstraints1);

        scrlPnExplanation.setViewportView(txtrExplanation);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 2;
        //gridBagConstraints1.ipadx = 300;
        //gridBagConstraints1.ipady = 30;
        gridBagConstraints1.insets = new Insets(0, 0, 10, 10);
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        pnlFields.add(scrlPnExplanation, gridBagConstraints1);

        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new Insets(0, 0, 20, 0);
        pnlQuestion.add(pnlFields, gridBagConstraints1);
        add(pnlQuestion, BorderLayout.WEST);
        Component[] comp = { questTable,txtrExplanation,txtReviewDate,btnMore };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
    }
    public void requestDefaultFocusForComponent(){
        questTable.requestFocusInWindow();
    }
    /**
     * create tabs for more explanation on click of More JButton
     */
    public void showMore(){
        String questionId=getSelectedQuestionId();
        String question=getSelectedQuestion();
        String explanation="";
        String policy="";
        String regulation="";
        
        //Bug Fix 1565: Start 3
        //Commented the below code for showing the more dialog
        //The hashtable expList was the wrong one since the key value pairs do not match
        //hence the new code uses the moreExpList which has the correct key.
//        if (expList!=null){
//            ProposalYNQExplantionFormBean explanationBean = (ProposalYNQExplantionFormBean) expList.get(questionId+"E");
//            if (explanationBean!=null){
//                explanation = explanationBean.getExplanation();
//            }
//            explanationBean = (ProposalYNQExplantionFormBean) expList.get(questionId+"P");
//            if (explanationBean!=null){
//                policy = explanationBean.getExplanation();
//            }
//            explanationBean = (ProposalYNQExplantionFormBean) expList.get(questionId+"R");
//            if (explanationBean!=null){
//                regulation = explanationBean.getExplanation();
//            }
//        }
            
        if (moreExpList!=null){
            YNQExplanationBean explanationBean = (YNQExplanationBean) moreExpList.get(questionId+"E");
            if (explanationBean!=null){
                explanation = explanationBean.getExplanation();
            }
            explanationBean = (YNQExplanationBean) moreExpList.get(questionId+"P");
            if (explanationBean!=null){
                policy = explanationBean.getExplanation();
            }
            explanationBean = (YNQExplanationBean) moreExpList.get(questionId+"R");
            if (explanationBean!=null){
                regulation = explanationBean.getExplanation();
            }
        }
        //Bug Fix 1565: End 3
        
        QuestionMoreForm more =
                new QuestionMoreForm(functionType,questionId,question,explanation,policy,regulation);
        more.setVisible(true);
    }
    
    public void prepareQuestionTable(){
        dateUtils = new DateUtils();
        // prepare table of questions
        questTable = new QuestionTable(functionType,proposalQuestionList, questionList);
        Vector colNames = new Vector();
        
        //Bug Fix : 907 Start 1
//        colNames.add(null); // no header for icon column
        //Bug Fix : 907 End 1
        
        colNames.add("Code");
        colNames.add("Question");
        colNames.add("Answer");
        questTable.setTableColumnNames(colNames);
        questTable.setFont(CoeusFontFactory.getLabelFont());
            exp = new Hashtable();
            Vector data = new Vector();
            Vector row = null;
            int[] answerOptions = new int[questionList.length];
            for (int i = 0; i < questionList.length; i++) {
                row = new Vector();
                // 0 element
                
                //Bug Fix : 907 Start 2
//                row.addElement(null);
                //Bug Fix : 907 End 2
                // code
                String questionId=questionList[i].getQuestionId();
                // 1 element
                row.addElement(questionId);
                // question
                // 2 element
                row.addElement(questionList[i].getDescription());
                // selected answer
                String orgAnswer="";
                String orgExplanation="";
                String orgReviewDate="";
                if (proposalQuestionList!=null){
                    for (int j=0;j<proposalQuestionList.length;j++){
                        if(proposalQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)){
                            if (proposalQuestionList[j].getAnswer()!=null) {
                                 orgAnswer = proposalQuestionList[j].getAnswer();
                            }
                            if (proposalQuestionList[j].getExplanation()!=null){
                                 orgExplanation = proposalQuestionList[j].getExplanation();
                             }
                            if (proposalQuestionList[j].getReviewDate()!=null){
                                 orgReviewDate = proposalQuestionList[j].getReviewDate();
                             }
                           break;
                        }
                    }
                }
                String answer="";
                if (orgAnswer.equalsIgnoreCase("Y")){
                    answer="1";
                }else if (orgAnswer.equalsIgnoreCase("N")){
                    answer="2";
                }else if (orgAnswer.equalsIgnoreCase("X")){
                    answer="3";
                }else{
                    //default answer
                    answer="4";
                }
                //3 element
                row.addElement(answer);

                // added by guptha
                // 4 element
                row.addElement(orgExplanation);
                // 5 element
                row.addElement(orgReviewDate);
                String[] strExp = new String[2];
                strExp[0]=orgExplanation;

                if (orgReviewDate!=null && orgReviewDate.trim().length()>0){
                    try{
                        strExp[1]=dateUtils.formatDate(orgReviewDate , "MM/dd/yyyy");
                    }
                    catch(NumberFormatException nfe){
                        //System.out.println("Number Format Exception in QuestionForm.ReviewDate");
                        //strExp[1]=dateUtils.formatDate(orgReviewDate , "MM/dd/yyyy");
                        strExp[1]=dateUtils.restoreDate(orgReviewDate,"-");
                    }
                }else{
                    strExp[1]=orgReviewDate;
                }
                exp.put(questionId,strExp);

                data.addElement(row);
                /** Number of RadioButtons to be shown in last column*/
                answerOptions[i] = questionList[i].getNoOfAnswers();

            }
            questTable.setTableData(data);
            questTable.setAnswerOptions(answerOptions);
            // add Component
            questTable.addTable();
            
            ListSelectionModel listSelectionModel =questTable.tblQuestionTable.getSelectionModel();
            listSelectionModel.addListSelectionListener(this);
            questTable.tblQuestionTable.setSelectionForeground(questTable.rowSelectionColor);


            // added by guptha
            // set the explanation and review date for first row(default)
            if( data != null && data. size () > 0 ) {
                Vector row1 = (Vector) questTable.getTableData().elementAt(0);
                
                //Bug Fix : 907 Start 3 changed to 0 and 1 
                String questionId = (String) row1.elementAt(0);
                setSelectedQuestionId(questionId);
                setSelectedQuestion((String) row1.elementAt(1));
                //Bug Fix : 907 End 3 
                
                
                if (exp !=null) {
                    String[] v = (String[]) exp.get(questionId);
                    txtrExplanation.setText(v[0]!=null && v[0].trim().length()>0 ? v[0]:"");
                    txtReviewDate.setText(v[1]!=null && v[1].trim().length()>0 ? dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy") : "");
                    if (v[1]!=null) {
                        focusDate = v[1];
                    }else{
                        focusDate = "";
                    }
                }
            }
    }

    /**
     * set enabled or diabled for form controls as per the functionality
     *
     * @param functionType the functionality type
     *                 'D' - display
     *                 'M' - modify
     */
    public void formatFields() {
        boolean enableStatus = true;
        switch (functionType) {
            case 'D': // display only
                enableStatus = false;
                break;
            case 'M': // allows modification
                enableStatus = true;
                break;
            default: // plain
                enableStatus = true;
                break;
        }
        txtrExplanation.setEditable(enableStatus);
        txtReviewDate.setEditable(enableStatus);
        if (!enableStatus){
            txtrExplanation.setBackground(this.getBackground());
        }
        // //Bug Fix 2017 - start
        if(isEditable){
            btnMore.setEnabled(false);
            txtReviewDate.setEditable(false);
            txtrExplanation.setEditable(false);
            txtrExplanation.setBackground(this.getBackground());
        }//Bug Fix 2017 - End
    }

    public void valueChanged(ListSelectionEvent e) {
        // find the selected row
        int selectedRow = questTable.tblQuestionTable.getSelectedRow();

        // make the value at first column and prevSelectedRow empty
        //Bug Fix : 907 Start 4  commented since no hand icon 
//        if( questTable.prevSelectedRow > -1 ){
//            questTable.tblQuestionTable.setValueAt(null,questTable.prevSelectedRow,0); // exception-prone if there is no previous selected row available
//        }
        //Bug Fix : 907 End 4 
        
        questTable.prevSelectedRow = selectedRow;
        // set the selection row in different color
        questTable.tblQuestionTable.setSelectionForeground(questTable.rowSelectionColor);

        // added by guptha
        
        Vector row = (Vector) questTable.getTableData().elementAt(selectedRow);
        
        //Bug Fix : 907 Start 5
        String questionId = (String) row.elementAt(0);
        setSelectedQuestionId(questionId);
        setSelectedQuestion((String) row.elementAt(1));
        //Bug Fix : 907 End 5 
        
        if (exp !=null) {
            String[] v = (String[]) exp.get(questionId);
            txtrExplanation.setText(v[0]==null || v[0].trim().length()<=0 ? "":v[0]);
            txtReviewDate.setText(v[1]!=null && v[1].trim().length()>0 ? dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy") : "");
            focusDate = v[1];
        }
        for (int j=0;j<proposalQuestionList.length;j++){
            //System.out.println("Value Change event is fired...");
            if(proposalQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)){
                
                //BugFix :963 Start
                saveRequired=questTable.isSaveRequired();
                //BugFix :963 End
                
                String[] v = (String[]) exp.get(questionId);
                proposalQuestionList[j].setExplanation(v[0]);
                proposalQuestionList[j].setReviewDate(v[1]);
                break;
            }//if
        }//for
        txtrExplanation.requestFocus();
    } // end of valueChanged

    public void setSelectedQuestionId(String selectedQuestionId){
        this.selectedQuestionId=selectedQuestionId;
    }

    public String getSelectedQuestionId(){
        return selectedQuestionId;
    }
    public void setSelectedQuestion(String selectedQuestion){
        this.selectedQuestion=selectedQuestion;
    }

    public String getSelectedQuestion(){
        return selectedQuestion;
    }

    public void setData(){
    }

    //validate question
   public boolean validateData(){
       boolean dataOK=true;
       for(int i=0;i<questionList.length;i++){
           if (!dataOK){
               break;
           }
               for(int j=0;j<proposalQuestionList.length;j++){
                   String orgQuestionId = proposalQuestionList[j].getQuestionId();
                   String answer = proposalQuestionList[j].getAnswer();
                   answer = (answer==null)?"" : answer;
                   if (questionList[i].getQuestionId().equalsIgnoreCase(orgQuestionId)) {
                       //set the explanation and review date into bean
                       String questionId = questionList[i].getQuestionId();
                       String[] v = (String[]) exp.get(questionId);
                       proposalQuestionList[j].setExplanation(v[0]);
                       proposalQuestionList[j].setReviewDate(v[1]);

                       // in add mode only
                       //Code commented for coeus4.3 YNQ enhancement - starts.
                       //Validation for all the questions should be answered mandatory is removed.
//                       if (answer==null || answer.equals("")){
//                           log("Please answer the question "+questionList[i].getDescription());
//                           dataOK=false;
//                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
//                           questTable.tblQuestionTable.scrollRectToVisible(
//                            questTable.tblQuestionTable.getCellRect(i,0,true));
//                           break;
//                       }
                       //Code commented for coeus4.3 YNQ enhancement - ends.
                   //Code added for coeus4.3 YNQ enhancement.
                    if (answer!=null && !answer.equals("")){
                           
                       if (v[1]!=null && v[1].trim().length()>0) {
                           if (dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy")==null){
                               dataOK=false;
                               log("Please enter valid date for question id "+questionId);                               
                               break;
                           }else{
                               proposalQuestionList[j].setReviewDate(dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy"));
                           }
                       }
                       // set actype
                       //proposalQuestionList[j].setAcType(""+functionType);
                       
                       //Bug Fix 1665: Start 4
                       //if (proposalQuestionList[j].getAcType()!=INSERT_RECORD) {
                       //     proposalQuestionList[j].setAcType(UPDATE_RECORD);
                       //}
                       if(proposalQuestionList[j].getAcType() == null || !proposalQuestionList[j].getAcType().equals("I")){
                           proposalQuestionList[j].setAcType(UPDATE_RECORD);
                       }
                       
                       // Checking for the selected answer requires explanation and date.
                       // Bug Fix 2689: Starts
                       if (questionList[i].getExplanationRequiredFor()!= null 
                           && questionList[i].getExplanationRequiredFor().toUpperCase().indexOf(answer)!=-1                            
                           && proposalQuestionList[j].getExplanation().trim().length()<=0) {
                               txtrExplanation.requestFocus();
                               log("Please enter the explanation for question ID: "+orgQuestionId);
                               questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                               questTable.tblQuestionTable.scrollRectToVisible(
                               questTable.tblQuestionTable.getCellRect(i,0,true));
                               dataOK=false;
                      }else if(questionList[i].getDateRequiredFor()!= null
                               && questionList[i].getDateRequiredFor().toUpperCase().indexOf(answer)!=-1
                               &&(proposalQuestionList[j].getReviewDate()==null || proposalQuestionList[j].getReviewDate().trim().length()==0) ) {
                                   txtReviewDate.requestFocus();
                                   log("Please enter the review date for question ID: "+orgQuestionId);
                                   questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                                   questTable.tblQuestionTable.scrollRectToVisible(
                                   questTable.tblQuestionTable.getCellRect(i,0,true));
                                   dataOK=false;
                      }
                    }
                       
//                      if (questionList[i].getExplanationRequiredFor()!= null 
//                          && questionList[i].getExplanationRequiredFor().trim().equalsIgnoreCase("YN") 
//                          && proposalQuestionList[j].getExplanation().trim().length()<=0
//                          && !answer.equalsIgnoreCase("X")) {
//                              log("Please enter the explanation for question ID: "+orgQuestionId);
//                              questTable.tblQuestionTable.setRowSelectionInterval(i,i);
//                              questTable.tblQuestionTable.scrollRectToVisible(
//                              questTable.tblQuestionTable.getCellRect(i,0,true));
//                              dataOK=false;
//                      }else if(questionList[i].getDateRequiredFor()!= null
//                               && questionList[i].getDateRequiredFor().trim().equalsIgnoreCase("YN") 
//                               &&(proposalQuestionList[j].getReviewDate()==null || proposalQuestionList[j].getReviewDate().trim().length()==0) 
//                               && !answer.equalsIgnoreCase("X")) {
//                      
//                                   log("Please enter the review date for question ID: "+orgQuestionId);
//                                   questTable.tblQuestionTable.setRowSelectionInterval(i,i);
//                                   questTable.tblQuestionTable.scrollRectToVisible(
//                                   questTable.tblQuestionTable.getCellRect(i,0,true));
//                                   dataOK=false;
//                      }
//                      //Bug Fix 1665: End 4
//                       
//                       if (questionList[i].getExplanationRequiredFor() !=null &&
//                       questionList[i].getExplanationRequiredFor().trim().equalsIgnoreCase(answer) &&
//                       proposalQuestionList[j].getExplanation().trim().length()<=0) {
//                           
//                           //For Bug fix :ajay
//                           txtrExplanation.requestFocus();
//                           //end
//                           
//                           
//                           log("Please enter the explanation for question ID: "+orgQuestionId);
//                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
//                           questTable.tblQuestionTable.scrollRectToVisible(
//                           questTable.tblQuestionTable.getCellRect(i,0,true));
//                           
//                           dataOK=false;
//                       }else if(questionList[i].getDateRequiredFor() !=null &&
//                       questionList[i].getDateRequiredFor().trim().equalsIgnoreCase(answer) &&
//                       (proposalQuestionList[j].getReviewDate()==null || proposalQuestionList[j].getReviewDate().trim().length()==0) ) {
//                           
//                           //For Bug fix :ajay
//                           txtReviewDate.requestFocus();
//                           //end
//
//                           log("Please enter the review date for question ID: "+orgQuestionId);
//                           
//                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
//                           questTable.tblQuestionTable.scrollRectToVisible(
//                           questTable.tblQuestionTable.getCellRect(i,0,true));
//                           dataOK=false;
//                       }
                       // Bug Fix 2689: ends
                      break;
                   }
               }
       }
       return dataOK;
   }

    /**
      * display message from the server on mdi form
      */
     public void log(String mesg){
        CoeusOptionPane.showErrorDialog(mesg);
     }


    public void updateExplanation(String explanation){
        saveRequired=true;
        int selectedRow = questTable.tblQuestionTable.getSelectedRow();
        String questionId = questionList[selectedRow].getQuestionId();
        //set the explanation in bean
        proposalQuestionList[selectedRow].setExplanation(explanation);
        //set the explanation in hashtable also
        String[] v = (String[]) exp.get(questionId);
        v[0]=explanation;
        exp.put(questionId,v);
    }
    public boolean isSaveRequired(){
        //Case 3596 - START
        return (saveRequired || questTable.isSaveRequired());
        //Case 3596 - END
    }

    public void updateReviewDate(String reviewDate){
        saveRequired=true;
        int selectedRow = questTable.tblQuestionTable.getSelectedRow();
        String questionId = questionList[selectedRow].getQuestionId();
        String[] v = (String[]) exp.get(questionId);
        v[1]=reviewDate;
        exp.put(questionId,v);
    }
    public void validateReviewDate(String rDate){
        int selectedRow = questTable.tblQuestionTable.getSelectedRow();
        String questionId = questionList[selectedRow].getQuestionId();
        String[] v = (String[]) exp.get(questionId);
        v[1]=rDate;

        //date in datetime format mm/dd/yyyy hh:mm:ss
        String reviewDate = "";
        //conver the string date to date object and validate
        if (rDate!=null && rDate.trim().length()>0){
            // if date is valid, set the review date in bean
        }
    }
    
    public void showQuestionForm() throws CoeusClientException{
        try{
            RequesterBean requester = new RequesterBean();
            requester.setId(proposalId);
            requester.setFunctionType( 'T' );
            
            String connectTo = CoeusGuiConstants.CONNECTION_URL +
                        CoeusGuiConstants.PROPOSAL_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            /*if (res != null && !res.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(res.getMessage());
                return;
            }*/
            if(res != null) {
                if(!res.isSuccessfulResponse()) {
                    throw new CoeusClientException(res.getMessage(),CoeusClientException.ERROR_MESSAGE);
                }
            }
            

            Vector dataObject = null; 
            Vector vQuestionList = null;
            Vector answerList = null;
            Hashtable explanation = null;
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

    public static void main(String[] args) {
        try{
            //connect to server and release the lock
            String proposalId = args[0];
            //System.out.println("The passed proposal number is " + args[0]);
            RequesterBean requester = new RequesterBean();
            requester.setId(proposalId);
            requester.setFunctionType( 'T' );
            
            String connectTo = "http://localhost:8080/CoeusApplet" +
                        CoeusGuiConstants.PROPOSAL_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if (res != null && !res.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(res.getMessage());
                return;
            }
            Vector dataObject = null; 
            Vector roleIDs = null;
            Vector userRole = null;
            Vector questionList = null;
            Vector answerList = null;
            Hashtable explanation = null;
            try { 
                dataObject = (Vector)res.getDataObjects();
                /* roleIDs = (Vector)dataObject.get(1);
                if (roleIDs ==null) {
                    //System.out.println("roleIDs is null. Terminating...");
                    System.exit(0);
                }
                userRole = (Vector)dataObject.get(2);
                if (userRole ==null) {
                    //System.out.println("userRole is null. Terminating...");
                    System.exit(0);
                }  
                 */              
                questionList = (Vector)dataObject.get(0);
                answerList = (Vector)dataObject.get(1);
                explanation = (Hashtable)dataObject.get(2);
            }
            catch(NullPointerException npe) {
                npe.printStackTrace();
                System.exit(0);
            }
            catch(Exception ex) {
                ex.printStackTrace();
                System.exit(0);
            }
            //System.out.println("The stmt after catch..");
            QuestionListBean[] qList = new QuestionListBean[questionList.size()];
            ProposalYNQBean[] aList = new ProposalYNQBean[questionList.size()];
            /*
                qList = (QuestionListBean[]) questionList.toArray(new QuestionListBean[questionList.size()]);
                aList = (ProposalYNQBean[]) answerList.toArray(new ProposalYNQBean[answerList.size()]);
             */
            //System.out.println("The stmt before for loop..");
            for (int index=0; index<questionList.size(); index++){
                qList[index] = (QuestionListBean) questionList.elementAt(index);
                aList[index] = (ProposalYNQBean) answerList.elementAt(index);
            }
            if ((qList == null) || (aList == null)){
                //System.out.println("The result List is Null.");
            }
            //System.out.println("The stmt before initialization..");
            QuestionForm form = new QuestionForm('A',qList,aList,explanation);
            
            //System.out.println("The stmt after initialization..");
            JFrame frame = new JFrame("Question Table...");
            frame.getContentPane().add(form);
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we){
                    System.exit(0);
                }
            });
            frame.pack();frame.show();
       }catch(Exception e){
           e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    /**
     * This method is introduced to change the saverequired set to False when Cancel or No button 
     * is pressed. This method is exclusively called from ProposalYesNoQuestionForm
     */
    public void setSaveRequired(boolean newSaveRequired){
        saveRequired = newSaveRequired;
    }
    
    /** Getter for property questTable.
     * @return Value of property questTable.
     *
     */
    public edu.mit.coeus.propdev.gui.QuestionTable getQuestTable() {
        return questTable;
    }
    
    /** Setter for property questTable.
     * @param questTable New value of property questTable.
     *
     */
    public void setQuestTable(edu.mit.coeus.propdev.gui.QuestionTable questTable) {
        this.questTable = questTable;
    }
 
    //Bug Fix 1665: Start 5
    /* Adds all the question displayed in the Question form to the 
     * OrganizationYNQBean array.Earlier only the values saved in the 
     * OSP$EPS_PROP_YNQ were present in the array.
     * Hence the validation was not firing and also the added/removed(incative) 
     * Questions were not added/removed from the OSP$EPS_PROP_YNQ
     */
    
    private void preparePorpQuestionList(ProposalYNQBean[] proposalYNQBean){
        CoeusVector cvQuesData = getModifiedQuestionData(proposalYNQBean);
        //Bug Fix 2017 - start
        if(cvQuesData == null || cvQuesData.size() <=0){
            isEditable = true;
        }else{
            isEditable = false;
        }
        //Bug Fix 2017 - End
        this.proposalQuestionList = new ProposalYNQBean[cvQuesData.size()];
        for(int i = 0 ; i < proposalQuestionList.length; i++){
            ProposalYNQBean dataBean = (ProposalYNQBean)cvQuesData.get(i);
            proposalQuestionList[i] = new ProposalYNQBean();
            proposalQuestionList[i].setAcType(dataBean.getAcType());
            proposalQuestionList[i].setAnswer(dataBean.getAnswer());
            proposalQuestionList[i].setExplanation(dataBean.getExplanation());
            proposalQuestionList[i].setProposalNumber(dataBean.getProposalNumber());
            proposalQuestionList[i].setQuestionId(dataBean.getQuestionId());
            proposalQuestionList[i].setReviewDate(dataBean.getReviewDate());
            proposalQuestionList[i].setUpdateTimeStamp(dataBean.getUpdateTimeStamp());
            proposalQuestionList[i].setUpdateUser(dataBean.getUpdateUser());
        }

         for(int count = 0 ;count < proposalQuestionList.length ; count ++){
            String proposalQID = proposalQuestionList[count].getQuestionId();
            for(int tnuoc = 0; tnuoc < questionList.length; tnuoc++){
                
                if(questionList[tnuoc].getQuestionId().equals(proposalQID)){
                
                    
                    if(questionList[tnuoc].getNoOfAnswers() == 2 && 
                       proposalQuestionList[count].getAnswer() != null  && proposalQuestionList[count].getAnswer().equals("X") )
                        proposalQuestionList[count].setAnswer(null);
                    
                }
            }
            
        }
    }
    
    public ProposalYNQBean[] getPropQuestionList(){
        if(!saveRequired){
            saveRequired = questTable.isSaveRequired();
        }
        return proposalQuestionList;
    }
    
    private CoeusVector getModifiedQuestionData(ProposalYNQBean[] propYNQBean){
        
        //ProposalYNQBean[] newpropYNQ = new ProposalYNQBean(propQuestionList);
        CoeusVector cvYnqData = new CoeusVector();
        String proposalNumber = "";
        //Bug Fix 2017 start
        if(propYNQBean.length != 0){
            proposalNumber = propYNQBean[0].getProposalNumber();
        }//Bug Fix 2017 End
        CoeusVector cvTemp = new CoeusVector() ;
        
        /*
         * This loop adds the new questions if present to the 
         * proposal question array. 
         **/
        for(int i = 0; i < questionList.length ; i++){
            String qId = questionList[i].getQuestionId();
           
            
            boolean found = false;
            
            for(int j =0; j < propYNQBean.length; j++ ){
                if(qId.equals(propYNQBean[j].getQuestionId())){
                    cvYnqData.add(propYNQBean[j]);
                    found = true;
                }
            }//End of for
            
            if(!found){
                ProposalYNQBean newProposalYNQBean = new ProposalYNQBean();
                
                questionList[i].getDateRequiredFor();
                 
                newProposalYNQBean.setQuestionId(qId);
                newProposalYNQBean.setProposalNumber(proposalNumber);
                newProposalYNQBean.setAcType("I");
                cvYnqData.add(newProposalYNQBean);
                found = true;
            }//End of if
        }//End of outer for
        
        /*
         * This loop delets the inactive questions if present to the 
         * proposal question array. 
         **/
        for (int x =0 ; x < propYNQBean.length; x++){
            boolean questionFound = false;
            String propQId = propYNQBean[x].getQuestionId();
            for(int y = 0; y < cvYnqData.size(); y++){
                
                ProposalYNQBean compareBean = 
                    (ProposalYNQBean)cvYnqData.get(y);
                if(propQId.equals(compareBean.getQuestionId())){
                   questionFound = true; 
                }
            }//End of inner for
            
            if(!questionFound){
                ProposalYNQBean newBean = propYNQBean[x]; 
                
                String date = newBean.getReviewDate();
                if(date != null){
                    DateUtils dtUtils = new DateUtils();
                    String tempDate  = dtUtils.restoreDate(date, ":/.,|-");
                    if(date.equals(tempDate)) {
                        date = dtUtils.formatDate(date , "MM/dd/yyyy");  
                        date = dtUtils.formatDate(date, "/-:," , "dd-MMM-yyyy");
                    }
                    newBean.setReviewDate(date);
                }
                
                newBean.setAcType("D");
                cvTemp.add(newBean);
                inactiveQuesPresent = true;
                
            }//End of if
        }//End of outer for
        
        cvYnqData.addAll(cvTemp);
        return cvYnqData;
        
    }//End of getQuestionData
    
    /**
     * Getter for property inactiveQuesPresent.
     * @return Value of property inactiveQuesPresent.
     */
    public boolean isInactiveQuesPresent() {
        return inactiveQuesPresent;
    }
    
    /**
     * Setter for property inactiveQuesPresent.
     * @param inactiveQuesPresent New value of property inactiveQuesPresent.
     */
    public void setInactiveQuesPresent(boolean inactiveQuesPresent) {
        this.inactiveQuesPresent = inactiveQuesPresent;
    }
    //Bug Fix 1665: End 5
    
}

