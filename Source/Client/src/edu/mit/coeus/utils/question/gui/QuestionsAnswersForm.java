/*
 * @(#)QuestionForm.java 1.0 8/31/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.question.gui;

import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.Hashtable;
import java.text.SimpleDateFormat;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.question.bean.*;
import edu.mit.coeus.utils.CoeusDateFormat;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.question.bean.YNQBean;
import edu.mit.coeus.utils.question.bean.QuestionListBean;


/**
 * This class constructs the Component with question details
 * with related other details like more question details .
 * The data is set in YNQ bean for modify/Display mode
 *
 * @version :1.0 August 31, 2002, 1:35 PM
 * @author Guptha K
 */
public class QuestionsAnswersForm extends JPanel implements ListSelectionListener{

    private JPanel pnlQuestion;
    private QuestionTable questTable;
    private JLabel lblExplanation;
    private JLabel lblReviewDate;
    //private CoeusTextField txtReviewDate;
    private JButton btnMore;
    private JScrollPane scrlPnExplanation;
    private JScrollPane scrlPnQuestionTable;
    public JTextArea txtrExplanation;

    //********************added by mukund
    //private JDialog dlgParentComponent;
    //*********************

    private boolean saveRequired;
    public QuestionListBean[] questionList;
    public YNQBean[] orgQuestionList;
    char functionType;
    private Hashtable exp;
    private String selectedQuestionId;
    private String selectedQuestion;
    private DateUtils dateUtils;
    private String focusDate;

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

    public QuestionsAnswersForm(char functionType,QuestionListBean[] questionList, YNQBean[] orgQuestionList) {
        this.functionType=functionType;
        this.questionList = questionList;
        this.orgQuestionList = orgQuestionList;
        initComponents();
        formatFields();
    }

    public QuestionsAnswersForm(char functionType,Vector vQuestions, Vector vAnswers) {
        //this(functionType,(QuestionListBean[]) vQuestions.toArray(),(YNQBean[]) vAnswers.toArray(),expList);
        this(functionType,
        (QuestionListBean[]) vQuestions.toArray(new QuestionListBean[vQuestions.size()]),
        (YNQBean[]) vAnswers.toArray(new YNQBean[vAnswers.size()]) );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        //prepare question table
		prepareQuestionTable();
		
        pnlQuestion = new JPanel();
        //Bug fix - 2343 - START
        //scrlPnQuestionTable = new JScrollPane();
        //scrlPnQuestionTable.setViewportView(questTable);
        //scrlPnQuestionTable.setMinimumSize(new Dimension(22, 15));
        
        //Bug Fix: 907 Start 1
        //scrlPnQuestionTable.setPreferredSize(new Dimension(545, 235));
        //Bug Fix: 907 End 1 
	//	pnlQuestion.setLayout(new BorderLayout());
	//	pnlQuestion.add(scrlPnQuestionTable, BorderLayout.CENTER);
        //add(pnlQuestion);
        add(questTable);
        //Bug fix - 2343 - END
    }

    public Vector getAnswers() {
		Vector answers = new Vector();

        for (int i=0;i<orgQuestionList.length;i++){
			answers.addElement(orgQuestionList[i]);
        }

        return answers;
    }

    public void prepareQuestionTable(){
        dateUtils = new DateUtils();
        // prepare table of questions
        questTable = new QuestionTable(functionType,orgQuestionList,questionList);
        Vector colNames = new Vector();
       
        //Bug Fix: 907 Start 2 
        //colNames.add(null); // no header for icon column
        //Bug Fix: 907 End 2
        
        
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
                
                //Bug Fix: 907 Start 3
                //row.addElement(null);
                //Bug Fix: 907 End 3
                
                // code
                String questionId=questionList[i].getQuestionId();
                // 1 element
                row.addElement(questionId);
                // question
                // 2 element
                row.addElement(questionList[i].getDescription());
                // selected answer
                String orgAnswer="";
                //String orgExplanation="";
                //String orgReviewDate="";
                if (orgQuestionList!=null){
                    for (int j=0;j<orgQuestionList.length;j++){
                        if(orgQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)){
                            if (orgQuestionList[j].getAnswer()!=null) {
                                 orgAnswer = orgQuestionList[j].getAnswer();
                            }
                           /* if (orgQuestionList[j].getExplanation()!=null){
                                 orgExplanation = orgQuestionList[j].getExplanation();
                             }
                            if (orgQuestionList[j].getReviewDate()!=null){
                                 orgReviewDate = orgQuestionList[j].getReviewDate();
                             }*/
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
                //row.addElement(orgExplanation);
                // 5 element
               /* row.addElement(orgReviewDate);
                //String[] strExp = new String[2];
                strExp[0]=orgExplanation;

                if (orgReviewDate!=null && orgReviewDate.trim().length()>0){
                    strExp[1]=dateUtils.formatDate(orgReviewDate , "MM/dd/yyyy");
                }else{
                    strExp[1]=orgReviewDate;
                }*/
                //exp.put(questionId,strExp);

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

            if(this.questionList==null || this.questionList.length==0)
                return;
            // added by guptha
            // set the explanation and review date for first row(default)
            
            // Bug Fix 2017 - start
            if(questTable.getTableData()!= null && questTable.getTableData().size() > 0){
                Vector row1 = (Vector) questTable.getTableData().elementAt(0);
                //Bug Fix: 907 4 Start changed element at to 0 and 1 
                String questionId = (String) row1.elementAt(0);
                setSelectedQuestionId(questionId);
                setSelectedQuestion((String) row1.elementAt(1));
            }// Bug Fix 2017 - End
            
            //Bug Fix: 907 End 4            
            
            
            
            /*if (exp !=null) {
                String[] v = (String[]) exp.get(questionId);
                txtrExplanation.setText(v[0]!=null && v[0].trim().length()>0 ? v[0]:"");
                txtReviewDate.setText(v[1]!=null && v[1].trim().length()>0 ? dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy") : "");
                if (v[1]!=null) {
                    focusDate = v[1];
                }else{
                    focusDate = "";
                }
            }*/
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
       // txtrExplanation.setEditable(enableStatus);
        //txtReviewDate.setEditable(enableStatus);
        //if (!enableStatus){
          //  txtrExplanation.setBackground(this.getBackground());
        //}
    }

    public void valueChanged(ListSelectionEvent e) {
        // find the selected row
        int selectedRow = questTable.tblQuestionTable.getSelectedRow();

        // make the value at first column and prevSelectedRow empty
        
        
        //Bug Fix: 907 Start 5 commented since the hand icon was removed
//        if( questTable.prevSelectedRow > -1 ){
//            questTable.tblQuestionTable.setValueAt(null,questTable.prevSelectedRow,0); // exception-prone if there is no previous selected row available
//        }
        //Bug Fix: 907 End 5 
        
        questTable.prevSelectedRow = selectedRow;
        // set the selection row in different color
        questTable.tblQuestionTable.setSelectionForeground(questTable.rowSelectionColor);

        // added by guptha
        
        Vector row = (Vector) questTable.getTableData().elementAt(selectedRow);
        
        //Bug Fix: 907 Start 6  changed the element at value to 0 and 1 
        String questionId = (String) row.elementAt(0);
        setSelectedQuestionId(questionId);
        setSelectedQuestion((String) row.elementAt(1));
        ////Bug Fix: 907 End 6 
        
        
        /*if (exp !=null) {
            String[] v = (String[]) exp.get(questionId);
            txtrExplanation.setText(v[0]==null || v[0].trim().length()<=0 ? "":v[0]);
            txtReviewDate.setText(v[1]!=null && v[1].trim().length()>0 ? dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy") : "");
            focusDate = v[1];
        }*/
        for (int j=0;j<orgQuestionList.length;j++){
            if(orgQuestionList[j].getQuestionId().equalsIgnoreCase(questionId)){
                saveRequired=true;
                String[] v = (String[]) exp.get(questionId);
                //orgQuestionList[j].setExplanation(v[0]);
                //orgQuestionList[j].setReviewDate(v[1]);
                break;
            }//if
        }//for
        //txtrExplanation.requestFocus();*/

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
       System.out.println(" validate in question form >>>>"+orgQuestionList.length);
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
                       //orgQuestionList[j].setExplanation(v[0]);
                       //orgQuestionList[j].setReviewDate(v[1]);*/

                       // in add mode only
                       if (answer==null || answer.equals("")){
                           log("Please answer the question "+questionList[i].getDescription());
                           dataOK=false;
                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                           questTable.tblQuestionTable.scrollRectToVisible(
                            questTable.tblQuestionTable.getCellRect(i,0,true));
                           break;
                       }

                       /*if (v[1]!=null && v[1].trim().length()>0) {
                           if (dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy")==null){
                               dataOK=false;
                               log("Item "+v[1]+"does not pass validation test. Please enter valid date for question id "+questionId);
                               break;
                           }else{
                               orgQuestionList[j].setReviewDate(dateUtils.formatDate(v[1], "/-:," , "dd-MMM-yyyy"));
                           }
                       }*/
                       // set actype
                       //orgQuestionList[j].setAcType(""+functionType);
                       /*
                       if (questionList[i].getExplanationRequiredFor().trim().equalsIgnoreCase(answer) &&
                            orgQuestionList[j].getExplanation().trim().length()<=0) {
                            log("Please enter the explanation for question ID: "+orgQuestionId);
                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                           questTable.tblQuestionTable.scrollRectToVisible(
                            questTable.tblQuestionTable.getCellRect(i,0,true));

                          dataOK=false;
                      }*/
                       /*else if(questionList[i].getDateRequiredFor().trim().equalsIgnoreCase(answer) &&
                           (orgQuestionList[j].getReviewDate()==null || orgQuestionList[j].getReviewDate().trim().length()==0) ) {
                          log("Please enter the review date for question ID: "+orgQuestionId);
                           questTable.tblQuestionTable.setRowSelectionInterval(i,i);
                           questTable.tblQuestionTable.scrollRectToVisible(
                            questTable.tblQuestionTable.getCellRect(i,0,true));
                           dataOK=false;
                      }*/
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

    public static void main(String args[]){

		//set function type
		char functionType='D';
		//set questions
		QuestionListBean questions1 = new QuestionListBean();

		questions1.setQuestionId("Z1");
		questions1.setDescription("Is there a financial confilict of interest with regards to this proposal?");
		questions1.setNoOfAnswers(2);
		questions1.setExplanationRequiredFor("N");
		questions1.setDateRequiredFor("N");

		QuestionListBean questions2 = new QuestionListBean();
		questions2.setQuestionId("Z2");
		questions2.setDescription("Are you currently Debarred, Suspended or a proposal for Debarrment or suspension?");
		questions2.setNoOfAnswers(2);
		questions2.setExplanationRequiredFor("N");
		questions2.setDateRequiredFor("N");

		QuestionListBean questions3 = new QuestionListBean();
		questions3.setQuestionId("Z3");
		questions3.setDescription("Are you delinquent on any federal debt?");
		questions3.setNoOfAnswers(2);
		questions3.setExplanationRequiredFor("N");
		questions3.setDateRequiredFor("N");

		QuestionListBean questions4 = new QuestionListBean();
		questions4.setQuestionId("H4");
		questions4.setDescription("Lobbying activities having conducted regarding this proposal? ");
		questions4.setNoOfAnswers(2);
		questions4.setExplanationRequiredFor("N");
		questions4.setDateRequiredFor("N");

		Vector vQuestions = new Vector();
		vQuestions.add(questions1);
		vQuestions.add(questions2);
		vQuestions.add(questions3);
		vQuestions.add(questions4);


		//set answers
		YNQBean answers = new YNQBean();
		answers.setQuestionId("Q1");
		answers.setAnswer(null);
		Vector vAnswers = new Vector();
		vAnswers.add(answers);
		System.out.println("exp");

		//set explanation
		Hashtable expList = new Hashtable();
		expList.put("Q1E","Explanation");
		expList.put("Q1P","Explanation");
		expList.put("Q1E","Explanation");
		expList.put("Q1E","Explanation");

		JFrame frame = new JFrame();
		/*
		QuestionListBean[] questionListBean = new QuestionListBean[vQuestions.size()];
		for (int i=0; i< vQuestions.size(); i++){
			questionListBean[i]=(QuestionListBean) vQuestions.elementAt(i);
		}

		YNQBean[] ynqBean = new YNQBean[vAnswers.size()];
		for (int i=0; i< vAnswers.size(); i++){
			ynqBean[i]=(YNQBean)vAnswers.elementAt(i);
		}

		QuestionForm questionForm = new
		     QuestionForm(functionType, questionListBean, ynqBean,expList);
		     //frame.add(questionForm);
		*/

    	//QuestionListBean[] array = (QuestionListBean[]) vQuestions.toArray(new QuestionListBean[vQuestions.size()]);

		QuestionsAnswersForm questionForm = new
		     QuestionsAnswersForm(functionType, vQuestions, vAnswers);
		     frame.getContentPane().add(questionForm);
		     frame.setVisible(true);
	}

}

