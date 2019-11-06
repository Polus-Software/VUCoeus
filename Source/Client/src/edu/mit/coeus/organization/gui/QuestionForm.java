/*
 * @(#)QuestionForm.java 1.0 8/31/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.organization.gui;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.text.SimpleDateFormat;

import edu.mit.coeus.organization.bean.QuestionListBean;
import edu.mit.coeus.organization.bean.OrganizationYNQBean;
import edu.mit.coeus.organization.bean.YNQExplanationBean;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.LimitedPlainDocument;

/**
 * This class constructs the Organization detail's question tab form
 *
 * @version :1.0 August 31, 2002, 1:35 PM
 * @author Guptha K
 */
public class QuestionForm extends JPanel implements ListSelectionListener{

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
    public QuestionListBean[] questionList;
    public OrganizationYNQBean[] orgQuestionList;
    char functionType;
    private Hashtable exp;
    private Hashtable expList;
    private String selectedQuestionId;
    private String selectedQuestion;
    private DateUtils dateUtils;
    private String focusDate;

    //Bug Fix: Validation , saving was not happening properly Start 1
    private boolean inactiveQuesPresent = false;
    //Bug Fix: Validation , saving was not happening properly End 1
    
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
    
    public QuestionForm(char functionType,QuestionListBean[] questionList, OrganizationYNQBean[] orgQuestionList,Hashtable expList) {
        this.functionType=functionType;
        this.questionList = questionList;
        
        //Bug Fix: Validation , saving was not happening properly Start 2
        //this.orgQuestionList = orgQuestionList;
        OrganizationYNQBean[] organizationYNQBean = orgQuestionList;
        if(organizationYNQBean.length > questionList.length){
            this.orgQuestionList = new OrganizationYNQBean[organizationYNQBean.length];
            inactiveQuesPresent = true;
        }else{
            this.orgQuestionList = new OrganizationYNQBean[questionList.length];
        }
        //JIRA COEUSDEV-1058 - START
        OrganizationYNQBean ynqBean;
        QuestionListBean qBean;
        ynq:for(int i=0; i< orgQuestionList.length; i++){
            ynqBean = orgQuestionList[i];
            for(int j=0; j< questionList.length; j++){
                qBean = questionList[j];
                if(qBean.getQuestionId().equals(ynqBean.getQuestionId())){
                    continue ynq;
                }
            }
            //ynq bean not found in questionList bean. Inactive question
            inactiveQuesPresent = true;
            break;
        }
        if(inactiveQuesPresent && this.orgQuestionList.length == questionList.length){
            //Since ynq array contains an Inactive One and since the question array size is same, contains an new question.
            this.orgQuestionList = new OrganizationYNQBean[questionList.length + 1];
        }
        //JIRA COEUSDEV-1058 - END
        prepareOrgQuestionList(organizationYNQBean);
        //Bug Fix: Validation , saving was not happening properly End 2
        
        this.expList = expList;
        initComponents();
        
        java.awt.Component[] component={scrlPnQuestionTable,txtrExplanation,btnMore,txtReviewDate};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(component);
        setFocusTraversalPolicy(traversalPolicy);
        setFocusCycleRoot(true);
        
        formatFields();
    }

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
                            log("Please enter valid date ");
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
        // Added by Chandra-27/8/2003
        txtrExplanation.setLineWrap(true);
        txtrExplanation.setWrapStyleWord(true);
        //End
        txtrExplanation.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                updateExplanation(txtrExplanation.getText());
            }
        });

        scrlPnExplanation = new JScrollPane(txtrExplanation,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrlPnExplanation.setMinimumSize(new Dimension(22, 15));
        scrlPnExplanation.setPreferredSize(new Dimension(60, 30));
        //prepare question table
        prepareQuestionTable();
        scrlPnQuestionTable = new JScrollPane();
        scrlPnQuestionTable.setViewportView(questTable);
        scrlPnQuestionTable.setMinimumSize(new Dimension(22, 15));
        scrlPnQuestionTable.setPreferredSize(new Dimension(500, 200));
        pnlQuestion.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 4;
        gridBagConstraints1.ipadx = 200;
        gridBagConstraints1.ipady = 50;
        gridBagConstraints1.insets = new Insets(0, 0, 20, 0);
        pnlQuestion.add(scrlPnQuestionTable, gridBagConstraints1);

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
        gridBagConstraints1.ipadx = 300;
        gridBagConstraints1.ipady = 30;
        gridBagConstraints1.insets = new Insets(0, 0, 10, 10);
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        pnlFields.add(scrlPnExplanation, gridBagConstraints1);

        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new Insets(0, 0, 20, 0);
        pnlQuestion.add(pnlFields, gridBagConstraints1);
        add(pnlQuestion, BorderLayout.CENTER);
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
        if( getSelectedQuestionId() != null ) {
            if (expList!=null){
                YNQExplanationBean explanationBean = (YNQExplanationBean) expList.get(questionId+"E");
                if (explanationBean!=null){
                    explanation = explanationBean.getExplanation();
                }
                explanationBean = (YNQExplanationBean) expList.get(questionId+"P");
                if (explanationBean!=null){
                    policy = explanationBean.getExplanation();
                }
                explanationBean = (YNQExplanationBean) expList.get(questionId+"R");
                if (explanationBean!=null){
                    regulation = explanationBean.getExplanation();
                }
            }
            QuestionMoreForm more =
                    new QuestionMoreForm(functionType,questionId,question,explanation,policy,regulation);
            more.setVisible(true);
        }else{
            CoeusOptionPane.showInfoDialog( "Please select a Question ");
        }
    }
    
    public void prepareQuestionTable(){
        dateUtils = new DateUtils();
        // prepare table of questions
        questTable = new QuestionTable(functionType,orgQuestionList,questionList);
        Vector colNames = new Vector();
        colNames.add(null); // no header for icon column
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
                row.addElement(null);
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
                if (orgQuestionList!=null){
                    for (int j=0;j<orgQuestionList.length;j++){
                        if(orgQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)){
                            if (orgQuestionList[j].getAnswer()!=null) {
                                 orgAnswer = orgQuestionList[j].getAnswer();
                            }
                            if (orgQuestionList[j].getExplanation()!=null){
                                 orgExplanation = orgQuestionList[j].getExplanation();
                             }
                            if (orgQuestionList[j].getReviewDate()!=null){
                                 orgReviewDate = orgQuestionList[j].getReviewDate();
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
                    strExp[1]=dateUtils.formatDate(orgReviewDate , "MM/dd/yyyy");
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
            //added null checking by ravi 
            if( questTable.getTableData() != null && questTable.getTableData().size() > 0) {
                // added by guptha
                // set the explanation and review date for first row(default)
                Vector row1 = (Vector) questTable.getTableData().elementAt(0);
                String questionId = (String) row1.elementAt(1);
                setSelectedQuestionId(questionId);
                setSelectedQuestion((String) row1.elementAt(2));
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
            }else{
                txtrExplanation.setEnabled( false );
                txtReviewDate.setEnabled( false );
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
    }

    public void valueChanged(ListSelectionEvent e) {
        // find the selected row
        int selectedRow = questTable.tblQuestionTable.getSelectedRow();

        // make the value at first column and prevSelectedRow empty
        if( questTable.prevSelectedRow > -1 ){
            questTable.tblQuestionTable.setValueAt(null,questTable.prevSelectedRow,0); // exception-prone if there is no previous selected row available
        }
        questTable.prevSelectedRow = selectedRow;
        // set the selection row in different color
        questTable.tblQuestionTable.setSelectionForeground(questTable.rowSelectionColor);

        // added by guptha
        Vector row = (Vector) questTable.getTableData().elementAt(selectedRow);
        String questionId = (String) row.elementAt(1);
        setSelectedQuestionId(questionId);
        setSelectedQuestion((String) row.elementAt(2));
        if (exp !=null) {
            String[] v = (String[]) exp.get(questionId);
            txtrExplanation.setText(v[0]==null || v[0].trim().length()<=0 ? "":v[0]);
            txtReviewDate.setText(v[1]!=null && v[1].trim().length()>0 ? dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy") : "");
            focusDate = v[1];
        }
        for (int j=0;j<orgQuestionList.length;j++){
            if(orgQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)){
                saveRequired=true;
                String[] v = (String[]) exp.get(questionId);
                orgQuestionList[j].setExplanation(v[0]);
                orgQuestionList[j].setReviewDate(v[1]);
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
               for(int j=0;j<orgQuestionList.length;j++){

                   String orgQuestionId = orgQuestionList[j].getQuestionId();
                   String answer = orgQuestionList[j].getAnswer();

                   if (questionList[i].getQuestionId().equalsIgnoreCase(orgQuestionId)) {
                       //set the explanation and review date into bean
                       String questionId = questionList[i].getQuestionId();
                       String[] v = (String[]) exp.get(questionId);
                       orgQuestionList[j].setExplanation(v[0]);
                       orgQuestionList[j].setReviewDate(v[1]);

                       // in add mode only
                       if (answer==null || answer.equals("")){
                           log("Please answer the question "+questionList[i].getDescription());
                           dataOK=false;
                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                           questTable.tblQuestionTable.scrollRectToVisible(
                            questTable.tblQuestionTable.getCellRect(i,0,true));
                           break;
                       }

                       if (v[1]!=null && v[1].trim().length()>0) {
                           if (dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy")==null){
                               dataOK=false;
                               log("Please enter valid date for question id "+questionId);                               
                               break;
                           }else{
                               orgQuestionList[j].setReviewDate(dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy"));
                           }
                       }
                       // set actype
                       
                       //Bug Fix: Validation , Saving was not happening properly Start 3
                       if(orgQuestionList[j].getAcType() == null || !orgQuestionList[j].getAcType().equals("I")){
                           orgQuestionList[j].setAcType(""+functionType);
                       }
                       
                       if (questionList[i].getExplanationRequiredFor()!= null 
                           && questionList[i].getExplanationRequiredFor().trim().equalsIgnoreCase("YNX") 
                           && orgQuestionList[j].getExplanation().trim().length()<=0) {
                               log("Please enter the explanation for question ID: "+orgQuestionId);
                               questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                               questTable.tblQuestionTable.scrollRectToVisible(
                               questTable.tblQuestionTable.getCellRect(i,0,true));
                            
                          dataOK=false;
                          
                      }else if(questionList[i].getDateRequiredFor()!= null
                               && questionList[i].getDateRequiredFor().trim().equalsIgnoreCase("YNX") 
                               &&(orgQuestionList[j].getReviewDate()==null || orgQuestionList[j].getReviewDate().trim().length()==0) ) {
                                   log("Please enter the review date for question ID: "+orgQuestionId);
                                   questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                                   questTable.tblQuestionTable.scrollRectToVisible(
                                   questTable.tblQuestionTable.getCellRect(i,0,true));
                                   dataOK=false;
                      }
                       
                       //Bug Fix: Validation , Saving was not happening properly End 3
                       
                       if (questionList[i].getExplanationRequiredFor()!= null && questionList[i].getExplanationRequiredFor().trim().equalsIgnoreCase(answer) &&
                            orgQuestionList[j].getExplanation().trim().length()<=0) {
                            log("Please enter the explanation for question ID: "+orgQuestionId);
                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                           questTable.tblQuestionTable.scrollRectToVisible(
                            questTable.tblQuestionTable.getCellRect(i,0,true));
                            
                          dataOK=false;
                          
                      }else if(questionList[i].getDateRequiredFor()!= null && questionList[i].getDateRequiredFor().trim().equalsIgnoreCase(answer) &&
                           (orgQuestionList[j].getReviewDate()==null || orgQuestionList[j].getReviewDate().trim().length()==0) ) {
                          log("Please enter the review date for question ID: "+orgQuestionId);
                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                           questTable.tblQuestionTable.scrollRectToVisible(
                            questTable.tblQuestionTable.getCellRect(i,0,true));
                           dataOK=false;
                      }
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
        orgQuestionList[selectedRow].setExplanation(explanation);
        //set the explanation in hashtable also
        String[] v = (String[]) exp.get(questionId);
        v[0]=explanation;
        exp.put(questionId,v);
    }
    
    public boolean isSaveRequired(){
        return saveRequired;
    }

    //Added by Amit 11/21/2003
    /** This method use to implement focus on first editable component in this page.
     */
    public void setDefaultFocusForComponent(){
        if(!( functionType == CoeusGuiConstants.DISPLAY_MODE )) {                        
                txtrExplanation.requestFocusInWindow();
        }
    }    
    //end Amit          
    
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
    
    //Bug Fix: Validation , Saving was not happening properly Start 4
    /* Adds all the question displayed in the Question form to the 
     * OrganizationYNQBean array.Earlier only the values saved in the 
     * OSP$ORGANIZATION_YNQ were present in the array.
     * Hence the validation was not firing and also the added/removed(incative) 
     * Questions were not added/removed from the OSP$ORGANIZATION_YNQ
     */
    
    private void prepareOrgQuestionList(OrganizationYNQBean[] organizationYNQBean){
        for(int i = 0 ; i < organizationYNQBean.length; i++){
            orgQuestionList[i] = new OrganizationYNQBean();
            orgQuestionList[i].setAcType(organizationYNQBean[i].getAcType());
            orgQuestionList[i].setAnswer(organizationYNQBean[i].getAnswer());
            orgQuestionList[i].setExplanation(organizationYNQBean[i].getExplanation());
            orgQuestionList[i].setOrgId(organizationYNQBean[i].getOrgId());
            orgQuestionList[i].setQuestionId(organizationYNQBean[i].getQuestionId());
            orgQuestionList[i].setReviewDate(organizationYNQBean[i].getReviewDate());
            orgQuestionList[i].setUpdateTimeStamp(organizationYNQBean[i].getUpdateTimeStamp());
            orgQuestionList[i].setUpdateUser(organizationYNQBean[i].getUpdateUser());
        }

        if(questionList.length > organizationYNQBean.length){
            addNewQuestion ();
        }
        //JIRA COEUSDEV-1058 - START
        if(questionList.length == organizationYNQBean.length && inactiveQuesPresent){
            addNewQuestion ();
        }
        //JIRA COEUSDEV-1058 - END

         for(int count = 0 ;count < orgQuestionList.length ; count ++){
            String orgQID = orgQuestionList[count].getQuestionId();
            for(int tnuoc = 0; tnuoc < questionList.length; tnuoc++){
                
                if(questionList[tnuoc].getQuestionId().equals(orgQID)){
                
                    
                    if(questionList[tnuoc].getNoOfAnswers() == 2 && 
                       orgQuestionList[count].getAnswer() != null  && orgQuestionList[count].getAnswer().equals("X") )
                        orgQuestionList[count].setAnswer(null);
                    
                }
            }
            
        }
    }
    
    /* Adds the new Questions displayed in the Questions Form
     * to orgQuestionList.
     */
    private void addNewQuestion(){
        
        for (int index = 0 ;index < questionList.length; index++){
            
            boolean foundQIdInOrg = false;
            int indexToAddQues = 0;
            String orgId = orgQuestionList[0].getOrgId();
            
            for(int xedni = 0; xedni < orgQuestionList.length; xedni++){
                if(orgQuestionList[xedni] == null){
                    continue;
                }
                String orgQIdInTheORG_YNQTable = orgQuestionList[xedni].getQuestionId();
                String qIdFromTheDisplayedList = questionList[index].getQuestionId();
                indexToAddQues = xedni;
                if(qIdFromTheDisplayedList.equals(orgQIdInTheORG_YNQTable)){
                    foundQIdInOrg = true;
                }
            }
            
            //if(!foundQIdInOrg && indexToAddQues < questionList.length-1){ //JIRA COEUSDEV-1058
            if(!foundQIdInOrg && indexToAddQues < orgQuestionList.length-1){ //JIRA COEUSDEV-1058
                String qID = questionList[index].getQuestionId();
                
                int orgLength = orgQuestionList.length;
                orgQuestionList[indexToAddQues+1] = new OrganizationYNQBean ();
                orgQuestionList[indexToAddQues+1].setQuestionId(qID);
                orgQuestionList[indexToAddQues+1].setOrgId(orgId);
                orgQuestionList[indexToAddQues+1].setAcType("I");
            }
        }
    }
    
    public OrganizationYNQBean[] getOrgQuestionList(){
        if(inactiveQuesPresent){
            for(int index = 0 ; index < orgQuestionList.length ; index++){
                //The inactive question has to be deleted from the DB
                if(orgQuestionList[index].getAcType() == null){
                    orgQuestionList[index].setAcType("D");
                    orgQuestionList[index].setReviewDate(null);
                    orgQuestionList[index].setExplanation(null);
                }
            }
        }
        return orgQuestionList;
    }
    //Bug Fix: Validation , Saving was not happening properly End 4
}

