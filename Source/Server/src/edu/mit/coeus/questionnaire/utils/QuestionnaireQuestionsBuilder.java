/*
 * @(#)QuestionnaireQuestionsBuilder.java July 31, 2007, 11:31 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireQuestionsBuilder.java
 *
 * Created on July 31, 2007, 11:31 AM
 *
 */

package edu.mit.coeus.questionnaire.utils;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.mapsrules.bean.RulesTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireQuestionsBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireUpdateTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.NotEquals;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author noorula
 */
public class QuestionnaireQuestionsBuilder {
    
    private CoeusVector cvOriginalData;
    private CoeusVector cvToDisplay;
    private int stage = 0;
    private int level = 0;
    private int page = 0;
    private LinkedHashMap lhmQuestionnaireTree;
    private CoeusVector cvQuestionnaireLevel;
    private static final String EMPTY_STRING = "";
    private static final String PARENT_QUESTION_NUMBER= "parentQuestionNumber";
    private static final String PAGE = "page";
    private String loggedInUser;
    
    /** Creates a new instance of QuestionnaireQuestionsBuilder */
    
    public QuestionnaireQuestionsBuilder() {
        cvToDisplay = new CoeusVector();
        cvQuestionnaireLevel = new CoeusVector();
        lhmQuestionnaireTree = new LinkedHashMap();
    }
    
    /**
     * Creates a new instance of QuestionnaireQuestionsBuilder
     * @param vecOriginalData 
     */
    public QuestionnaireQuestionsBuilder(CoeusVector vecOriginalData, String loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.cvOriginalData = vecOriginalData;
        cvToDisplay = new CoeusVector();
        lhmQuestionnaireTree = new LinkedHashMap();
        cvQuestionnaireLevel = new CoeusVector();
    }
    
    /**
     * To build the questionnaire tree, 
     * This tree tells about the questions present in each and every page.
     * @param parentQuestion Parent Question number
     */
    public void buildQuestions(int parentQuestion){
        CoeusVector cvQuestions = null;
        stage++;
        cvQuestions = getChildQuestions(parentQuestion);
        Equals eqParentQuesNum = new Equals(PARENT_QUESTION_NUMBER, new Integer(parentQuestion));
        Equals eqParentQuesCond = new Equals("conditionalFlag", true);
        And eqParentAndEqConition = new And(eqParentQuesNum, eqParentQuesCond);
        CoeusVector vecCondifionalQuestions = cvOriginalData.filter(eqParentAndEqConition);
        
        if(cvQuestions != null && cvQuestions.size() >0){
            if((vecCondifionalQuestions != null && vecCondifionalQuestions.size() > 0)){
                for(int index=0; index < vecCondifionalQuestions.size(); index++){
                    if(cvToDisplay.size()>0){
                        cvQuestionnaireLevel.add(""+level);
                        lhmQuestionnaireTree.put(""+(++page), cvToDisplay);                    
                        cvToDisplay = new CoeusVector();
                        break;
                    }
                }
            }
            int index = 0;
            int localStage = stage;
            for(index = 0 ; index < cvQuestions.size() ; index++){
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        (QuestionnaireQuestionsBean) cvQuestions.get(index);
                if(cvToDisplay.size() == 0)
                    level = localStage;
                cvToDisplay.add(questionnaireQuestionsBean);
                buildQuestions(questionnaireQuestionsBean.getQuestionNumber().intValue());
                stage = localStage;
            }
            if(index == cvQuestions.size()){
                if(cvToDisplay.size()>0){
                    cvQuestionnaireLevel.add(""+level);
                    lhmQuestionnaireTree.put(""+(++page), cvToDisplay);                    
                    cvToDisplay = new CoeusVector();
                }
            }
        }
    }

    /**
     * Getter for property lhmQuestionnaireTree.
     * @return Value of property lhmQuestionnaireTree.
     */
    public LinkedHashMap getLhmQuestionnaireTree() {
        return lhmQuestionnaireTree;
    }

    /**
     * Setter for property lhmQuestionnaireTree.
     * @param lhmQuestionnaireTree New value of property lhmQuestionnaireTree.
     */
    public void setLhmQuestionnaireTree(LinkedHashMap lhmQuestionnaireTree) {
        this.lhmQuestionnaireTree = lhmQuestionnaireTree;
    }

    /**
     * Getter for property cvQuestionnaireLevel.
     * @return Value of property cvQuestionnaireLevel.
     */
    public CoeusVector getCvQuestionnaireLevel() {
        return cvQuestionnaireLevel;
    }

    /**
     * Setter for property cvQuestionnaireLevel.
     * @param cvQuestionnaireLevel New value of property cvQuestionnaireLevel.
     */
    public void setCvQuestionnaireLevel(CoeusVector cvQuestionnaireLevel) {
        this.cvQuestionnaireLevel = cvQuestionnaireLevel;
    }
    
    /**
     * To get the Questions for next page
     * @param htQuestionnaireData 
     * @param cvQuestionAnswers 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws java.lang.Exception 
     * @return CoeusVector
     */
    public CoeusVector getNextQuestions(Hashtable htQuestionnaireData, CoeusVector cvQuestionAnswers,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean)
    throws CoeusException, Exception {
        String page = (String) htQuestionnaireData.get(PAGE);
        String oldPage = page;
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        if(page != null && !page.equals("")){
            int pageValue = Integer.parseInt(page);
            pageValue++;
            page = new Integer(pageValue).toString();
        }
        cvQuestionAnswers = getNextQuestions(htQuestionnaireData, cvQuestionAnswers, page,questionnaireAnswerHeaderBean);
        if(oldPage != null && !oldPage.equals(htQuestionnaireData.get(PAGE))){
            lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), null);
        }
        htQuestionnaireData.put("pagesAnswered", lhmPagesAnswered);          
        return cvQuestionAnswers;
    }
    
    /**
     * To get the Questions for next page
     * @param htQuestionnaireData 
     * @param cvQuestionAnswers 
     * @param page 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws java.lang.Exception 
     * @return CoeusVector
     */
    public CoeusVector getNextQuestions(Hashtable htQuestionnaireData,
            CoeusVector cvQuestionAnswers, String page,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws CoeusException, Exception{
        cvOriginalData =(CoeusVector) htQuestionnaireData.get("originalQuestions");
        cvQuestionnaireLevel =(CoeusVector) htQuestionnaireData.get("questionnaireLevel");        
        lhmQuestionnaireTree = (LinkedHashMap) htQuestionnaireData.get("questionnaireTree");
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        CoeusVector cvNextPageQuestions = (CoeusVector) ObjectCloner.deepCopy(lhmQuestionnaireTree.get(page));
        cvNextPageQuestions = (cvNextPageQuestions == null) ? new CoeusVector() : cvNextPageQuestions;
        if(cvNextPageQuestions == null || cvNextPageQuestions.isEmpty()){
            htQuestionnaireData.put(PAGE, "");
            return new CoeusVector();
        }
        int pageValue = Integer.parseInt(page);
        int currentLevel = 1;
        // To get the parent question answer.
        // To check the answer is valid to show this page questons
        if(cvQuestionnaireLevel.size() >= pageValue){
            currentLevel = Integer.parseInt((String)cvQuestionnaireLevel.get((pageValue-1)));
            if(currentLevel == 1){
                htQuestionnaireData.put(PAGE, page);
                return cvNextPageQuestions;
            }
            for(int index = pageValue-1; index > 0; index--){
                if(Integer.parseInt((String)cvQuestionnaireLevel.get(index-1)) == currentLevel-1){
                    cvQuestionAnswers = (CoeusVector) lhmPagesAnswered.get(""+(index));
                    break;
                }
            }
        }

        cvNextPageQuestions = validateForConditionalQuestions(htQuestionnaireData,cvQuestionAnswers, cvNextPageQuestions,questionnaireAnswerHeaderBean, false);
        
        //If the list of questions to display is empty, then go to next page level.
        if(cvNextPageQuestions.size() == 0){
            boolean isPresent = false;
            currentLevel = Integer.parseInt((String)cvQuestionnaireLevel.get((pageValue-1)));
            for(int index = pageValue; index < cvQuestionnaireLevel.size(); index++){
                if(Integer.parseInt((String)cvQuestionnaireLevel.get(index)) <= currentLevel){
                    currentLevel = index+1;
                    isPresent = true;
                    break;
                }
            }
            //if next page level is present then proceed.
            if(isPresent){
                htQuestionnaireData.put(PAGE, ""+currentLevel);
                cvNextPageQuestions = getNextQuestions(htQuestionnaireData, cvQuestionAnswers, ""+currentLevel,questionnaireAnswerHeaderBean);
            }
        } else {
            htQuestionnaireData.put(PAGE, page);
        }
        
        return cvNextPageQuestions;
    }
    
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    /**
     * To get the Questions for next page
     * @param htQuestionnaireData
     * @param cvQuestionAnswers
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return CoeusVector
     */
    public CoeusVector getNextCondtionalQuestions(Hashtable htQuestionnaireData, CoeusVector cvQuestionAnswers,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean)
    throws CoeusException, Exception {
        String page = (String) htQuestionnaireData.get(PAGE);
        String oldPage = page;
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        if(page != null && !page.equals("")){
            int pageValue = Integer.parseInt(page);
            pageValue++;
            page = new Integer(pageValue).toString();
        }
        cvQuestionAnswers = getNextConditionalQuestions(htQuestionnaireData, cvQuestionAnswers, page,questionnaireAnswerHeaderBean);
        if(oldPage != null && !oldPage.equals(htQuestionnaireData.get(PAGE))){
            lhmPagesAnswered.put(htQuestionnaireData.get(PAGE), null);
        }
        htQuestionnaireData.put("pagesAnswered", lhmPagesAnswered);
        if(checkAllQuestionsAnswered(cvQuestionAnswers)){
            cvQuestionAnswers = null;
        }
        return cvQuestionAnswers;
    }
    
    /**
     * Method to check is all the questions are answered in a page
     * @param cvQuestionAnswers 
     * @return 
     */
    private boolean checkAllQuestionsAnswered(CoeusVector cvQuestionAnswers){
        HashSet hsQuestionNumber = new HashSet();
        if(cvQuestionAnswers != null && !cvQuestionAnswers.isEmpty()){
            for(Object questionnaireQuestion : cvQuestionAnswers){
                QuestionnaireQuestionsBean questionBean = (QuestionnaireQuestionsBean)questionnaireQuestion;
                hsQuestionNumber.add(questionBean.getQuestionNumber());
            }
            Iterator itQuestionNumber = hsQuestionNumber.iterator();
            while(itQuestionNumber.hasNext()){
                Integer questionNumber = (Integer)itQuestionNumber.next();
                if(questionNumber.intValue() != 0){
                    Equals eqQuestionNumber = new Equals("questionNumber",questionNumber);
                    NotEquals eqEmptyAnswer = new NotEquals("answer", "");
                    NotEquals eqNullAnswer = new NotEquals("answer", null);
                    CoeusVector cvFiltered = cvQuestionAnswers.filter(eqQuestionNumber).filter(eqNullAnswer).filter(eqEmptyAnswer);
                    if(cvFiltered == null || cvFiltered.isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * To get the Questions for next conditional page
     * @param htQuestionnaireData
     * @param cvQuestionAnswers
     * @param page
     * @throws edu.mit.coeus.exception.CoeusException
     * @throws java.lang.Exception
     * @return CoeusVector
     */
    public CoeusVector getNextConditionalQuestions(Hashtable htQuestionnaireData,
            CoeusVector cvQuestionAnswers, String page,QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws CoeusException, Exception{
        cvOriginalData =(CoeusVector) htQuestionnaireData.get("originalQuestions");
        cvQuestionnaireLevel =(CoeusVector) htQuestionnaireData.get("questionnaireLevel");
        lhmQuestionnaireTree = (LinkedHashMap) htQuestionnaireData.get("questionnaireTree");
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        CoeusVector cvNextPageQuestions = (CoeusVector) ObjectCloner.deepCopy(lhmQuestionnaireTree.get(page));
        cvNextPageQuestions = (cvNextPageQuestions == null) ? new CoeusVector() : cvNextPageQuestions;
        if(cvNextPageQuestions == null || cvNextPageQuestions.isEmpty()){
            htQuestionnaireData.put(PAGE, "");
            return new CoeusVector();
        }
        int pageValue = Integer.parseInt(page);
        int currentLevel = 1;
        // To get the parent question answer.
        // To check the answer is valid to show this page questons
        if(cvQuestionnaireLevel.size() >= pageValue){
            currentLevel = Integer.parseInt((String)cvQuestionnaireLevel.get((pageValue-1)));

            for(int index = pageValue-1; index > 0; index--){
                if(Integer.parseInt((String)cvQuestionnaireLevel.get(index-1)) == currentLevel-1){
                    cvQuestionAnswers = (CoeusVector) lhmPagesAnswered.get(""+(index));
                    break;
                }
            }
        }
        
        cvNextPageQuestions = validateForConditionalQuestions(htQuestionnaireData,cvQuestionAnswers, cvNextPageQuestions,questionnaireAnswerHeaderBean,true);
        
        if(cvNextPageQuestions.size() == 0){
            boolean isPresent = false;
            currentLevel = Integer.parseInt((String)cvQuestionnaireLevel.get((pageValue-1)));
            for(int index = pageValue; index < cvQuestionnaireLevel.size(); index++){
                if(Integer.parseInt((String)cvQuestionnaireLevel.get(index)) <= currentLevel){
                    currentLevel = index+1;
                    isPresent = true;
                    break;
                }
            }
            //if next page level is present then proceed.
            if(isPresent){
                htQuestionnaireData.put(PAGE, ""+currentLevel);
                cvNextPageQuestions = getNextConditionalQuestions(htQuestionnaireData, cvQuestionAnswers, ""+currentLevel,questionnaireAnswerHeaderBean);
            }
        } else {
            htQuestionnaireData.put(PAGE, page);
        }

        return cvNextPageQuestions;
    }
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
    /**
     * It will return the List of child questions for the parent question number
     * @param parentQuestionNumber 
     * @return CoeusVector
     */
    private CoeusVector getChildQuestions(int parentQuestionNumber){
        Equals eqParentQuesNum = new Equals(PARENT_QUESTION_NUMBER, new Integer(parentQuestionNumber));
        return cvOriginalData.filter(eqParentQuesNum);
    }
    
    /**
     * To validate the anwer with the condition and condition value
     * @param answer 
     * @param condition 
     * @param conditionValue 
     * @return boolean condition satisfied or not.
     */
    private boolean conditionResult(String answer, String condition, 
                            String conditionValue){
        boolean isPassed = false;
        try{
            condition = (condition == null) ? EMPTY_STRING : condition;
            if(condition.equals("CONTAINS")){
                isPassed = (answer.toUpperCase().indexOf(conditionValue.toUpperCase()) != -1);
            } else if(condition.equals("BEGINS WITH")){
                isPassed = answer.toUpperCase().startsWith(conditionValue.toUpperCase());
            } else if(condition.equals("ENDS WITH")){
                isPassed = answer.toUpperCase().endsWith(conditionValue.toUpperCase());
            } else if(condition.equals("EQUAL TO") || condition.equals("=")){
                isPassed = answer.equalsIgnoreCase(conditionValue);
            } else if(condition.equals("NOT EQUAL") || condition.equals("!=")){
                isPassed = !answer.equalsIgnoreCase(conditionValue);
            } else if(condition.equals("GREATER THAN") || condition.equals(">")){
                isPassed = (Double.parseDouble(answer) > Double.parseDouble(conditionValue));
            } else if(condition.equals("LESS THAN") || condition.equals("<")){
                isPassed = (Double.parseDouble(answer) < Double.parseDouble(conditionValue));
            } else if(condition.equals("GREATER THAN EQUAL") || condition.equals(">=")){
                isPassed = (Double.parseDouble(answer) >= Double.parseDouble(conditionValue));
            } else if(condition.equals("LESS THAN EQUAL") || condition.equals("<=")){
                isPassed = (Double.parseDouble(answer) <= Double.parseDouble(conditionValue));
            }
        } catch(Exception e){
            isPassed = false;
        }
        return isPassed;
    }
    
    /**
     * To get the previous page questions
     * @param htQuestionnaireData 
     * @return CoeusVector
     */
    public CoeusVector getPreviousQuestions(Hashtable htQuestionnaireData){
        String page = (String) htQuestionnaireData.get(PAGE);
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        lhmPagesAnswered.remove(page);
        Set keySet = lhmPagesAnswered.keySet();
        Object[] objQuestions = keySet.toArray();
        page = (String) objQuestions[objQuestions.length-1];
        htQuestionnaireData.put(PAGE, page);
        return (CoeusVector)lhmPagesAnswered.get(page);
    }
    
    /**
     * To delete the answered questions present after the current page.
     * @param htQuestionnaireData 
     */
    public void deleteAnsweredQuestion(Hashtable htQuestionnaireData){
        String page = (String) htQuestionnaireData.get(PAGE);
        page = (page == null)? EMPTY_STRING : page;
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        Set keySet = lhmPagesAnswered.keySet();
        Object[] objQuestions = keySet.toArray();
        boolean isReached = false;
        for(int index = 0; index < objQuestions.length; index++){
            if(page.equals(objQuestions[index])){
                isReached = true;
                continue;
            }
            if(isReached){
                lhmPagesAnswered.remove(objQuestions[index]);
            }
        }
    }
    
    /**
     * To get all the answered questions
     * @param htQuestionnaireData 
     * @return CoeusVector
     */
    public CoeusVector getQuestionsForDisplayMode(Hashtable htQuestionnaireData){
        CoeusVector cvDataToDisplay = new CoeusVector();
        LinkedHashMap lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("pagesAnswered");
        Set keySet = lhmPagesAnswered.keySet();
        Object[] objQuestions = keySet.toArray();
        for(int index = 0; index < objQuestions.length; index++){
            CoeusVector cvPageQuestions = (CoeusVector) lhmPagesAnswered.get(objQuestions[index]);
            if(cvPageQuestions != null && !cvPageQuestions.isEmpty()){
                cvDataToDisplay.addAll(cvPageQuestions);
            }
        }        
        return cvDataToDisplay;
    }

    /**
     * Validate the nextpage questions according to the entered answers
     * @param cvQuestionAnswers 
     * @param cvNextPageQuestions 
     * @return CoeusVector
     */
    public CoeusVector validateForConditionalQuestions(Hashtable htQuestionnaireData,CoeusVector cvQuestionAnswers, CoeusVector cvNextPageQuestions,
            QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean, boolean isSaveAndComplete) throws DBException, CoeusException{
        String answer = EMPTY_STRING;
        int parentQuesNum = -1;
        boolean isPassed = false;
        CoeusVector cvFilteredQuestions = new CoeusVector();
        CoeusVector cvQuestionToAdd = new CoeusVector();
        // To get the child questions according to the answer for the parent question
        if(cvQuestionAnswers != null && cvQuestionAnswers.size() > 0){
            QuestionnaireUpdateTxnBean qnrUpdTxnBean = null;
            QuestionnaireUpdateTxnBean questionnaireUpdateTxnBean =  null;
            LinkedHashMap lhmPagesAnswered = null;
            CoeusVector cvChildQuestions = null;
            CoeusVector cvQuestionnaireTree = null;
            if(isSaveAndComplete){
                qnrUpdTxnBean = new QuestionnaireUpdateTxnBean();
                questionnaireUpdateTxnBean = new QuestionnaireUpdateTxnBean();
                
                lhmPagesAnswered =(LinkedHashMap) htQuestionnaireData.get("questionnaireTree");
                Set keySet = lhmPagesAnswered.keySet();
                Object[] objQuestions = keySet.toArray();
                cvQuestionnaireTree = new CoeusVector();
                for(int index1 = 0; index1 < objQuestions.length; index1++){
                    CoeusVector cvPageQuestions = (CoeusVector) lhmPagesAnswered.get(objQuestions[index1]);
                    cvQuestionnaireTree.addAll(cvPageQuestions);
                }
                cvChildQuestions = new CoeusVector();
            }
            HashMap hmQuestionValidation = new HashMap();
            for(int index = 0 ; index < cvQuestionAnswers.size() ; index++){
                QuestionnaireQuestionsBean questionnaireQuestionsBean =
                        (QuestionnaireQuestionsBean) cvQuestionAnswers.get(index);
                Equals eqParentQuesNum = new Equals(PARENT_QUESTION_NUMBER, 
                        questionnaireQuestionsBean.getQuestionNumber());
                
                Equals eqParentQuesCond = new Equals("conditionalFlag", true);
                And eqParentAndEqConition = new And(eqParentQuesNum, eqParentQuesCond);
                if(cvNextPageQuestions.filter(eqParentAndEqConition).size() > 0){
                    parentQuesNum = questionnaireQuestionsBean.getQuestionNumber().intValue();
                    answer = questionnaireQuestionsBean.getAnswer();
                    if(answer == null || answer.equals(EMPTY_STRING)){
                        continue;
                    }
                    if(isSaveAndComplete){
                        cvChildQuestions.addAll(cvQuestionnaireTree.filter(eqParentQuesNum));
                    }
                    for(int count = 0 ; count < cvNextPageQuestions.size() ; count++){
                        QuestionnaireQuestionsBean questionsBean =
                                (QuestionnaireQuestionsBean) cvNextPageQuestions.get(count);
                        if(questionsBean.getParentQuestionNumber().intValue() == parentQuesNum
                                && questionsBean.isConditionalFlag()){
                            // Modidied for COEUSQA-3287 Questionnaire Maintenance Features - Start
//                            boolean isPassed = conditionResult(answer, questionsBean.getCondition(), 
//                                                    questionsBean.getConditionValue());
                         
//                            if(!"".equals(questionsBean.getConditionValue())){
//                                boolean isConditionPassed = conditionResult(answer, questionsBean.getCondition(), questionsBean.getConditionValue());
//                                if(questionsBean.getConditionRuleId() != 0){
//                                    boolean isRulePassed = ruleConditionResult(questionnaireAnswerHeaderBean, questionsBean.getConditionRuleId());
//                                    if(isConditionPassed && isRulePassed){
//                                        isPassed = true;
//                                    }else{
//                                        isPassed = false;
//                                    }
//                                }else if(isConditionPassed){
//                                    isPassed = true;
//                                }else{
//                                    isPassed = false;
//                                }
//                            }else if(questionsBean.getConditionRuleId() != 0){
//                                isPassed = ruleConditionResult(questionnaireAnswerHeaderBean, questionsBean.getConditionRuleId());
//                            }
                            isPassed = isConditionPassed(answer,questionsBean,questionnaireAnswerHeaderBean);
                            
                            if(isSaveAndComplete){
                                if(hmQuestionValidation.get(questionsBean.getQuestionNumber()) == null){
                                    HashSet hsConQuesPassed = new HashSet();
                                    hsConQuesPassed.add(isPassed);
                                    hmQuestionValidation.put(questionsBean.getQuestionNumber(),hsConQuesPassed);
                                }else{
                                    ((HashSet)hmQuestionValidation.get(questionsBean.getQuestionNumber())).add(isPassed);
                                }
                                if(isPassed){
                                    cvQuestionToAdd.add(questionsBean.getQuestionNumber());
                                    cvNextPageQuestions.remove(count--);
                                    cvFilteredQuestions.add(questionsBean);
                                }
                            }else if(isPassed){
                                cvQuestionToAdd.add(questionsBean.getQuestionNumber());
                                cvNextPageQuestions.remove(count--);
                                cvFilteredQuestions.add(questionsBean);
                            }
                            
                        }
                    }   
                }  
            }
            
            
            if(isSaveAndComplete){
                
                if(!hmQuestionValidation.isEmpty()){
                    Iterator itQuestionvalidation = hmQuestionValidation.keySet().iterator();
                    while(itQuestionvalidation.hasNext()){
                        Integer questionNumber = (Integer)itQuestionvalidation.next();
                        HashSet hsCondQuesPass =  (HashSet)hmQuestionValidation.get(questionNumber);
                        if(hsCondQuesPass != null && !hsCondQuesPass.isEmpty()){
                            Iterator itCondQuesPass = hsCondQuesPass.iterator();
                            boolean isConQuesPassed = true;
                            while(itCondQuesPass.hasNext()){
                                if(!((Boolean)itCondQuesPass.next()).booleanValue()){
                                    isConQuesPassed = false;
                                }else{
                                    isConQuesPassed = true;
                                }
                            }
                            if(!isConQuesPassed){
                                CoeusVector cvChildQuestion = new CoeusVector();
                                QuestionnaireQuestionsBean questionsBean = new QuestionnaireQuestionsBean();
                                questionsBean.setQuestionNumber(questionNumber);
                                cvChildQuestion.add(questionsBean);
                                int qnrCompletionId = questionnaireUpdateTxnBean.getQuestionnaireCompletId(questionnaireAnswerHeaderBean);
                                removeConditionalChildQuestions(qnrUpdTxnBean,cvChildQuestion,qnrCompletionId);
                            }
                        }
                    }
                }
                cvFilteredQuestions = getNextPageQuestions(parentQuesNum, cvNextPageQuestions, cvFilteredQuestions, cvQuestionToAdd, cvQuestionAnswers);
                
            }
        }
        
        
        
        if(!isSaveAndComplete){
            cvFilteredQuestions = getNextPageQuestions(parentQuesNum, cvNextPageQuestions, cvFilteredQuestions, cvQuestionToAdd, cvQuestionAnswers);
            //To get the non conditional child questions for the parent question
//            if(parentQuesNum != -1){
//                Equals eqParentQuesNum = new Equals(PARENT_QUESTION_NUMBER, new Integer(parentQuesNum));
//                Equals eqParentQuesCond = new Equals("conditionalFlag", false);
//                And eqParentAndEqConition = new And(eqParentQuesNum, eqParentQuesCond);
//                CoeusVector cvQuestions = cvNextPageQuestions.filter(eqParentAndEqConition);
//                if(cvQuestions != null && !cvQuestions.isEmpty()){
//                    cvFilteredQuestions.addAll(cvQuestions);
//                    for(int index = 0; index < cvQuestions.size(); index++){
//                        QuestionnaireQuestionsBean questionsBean =
//                                (QuestionnaireQuestionsBean) cvQuestions.get(index);
//                        cvQuestionToAdd.add(questionsBean.getQuestionNumber());
//                    }
//                }
//                
//            } else {
//                if(cvQuestionAnswers != null && cvQuestionAnswers.size() > 0){
//                    for(int index = 0; index < cvNextPageQuestions.size(); index++){
//                        QuestionnaireQuestionsBean questionnaireQuestionsBean =
//                                (QuestionnaireQuestionsBean) cvNextPageQuestions.get(index);
//                        for(int count = 0; count < cvQuestionAnswers.size(); count++){
//                            QuestionnaireQuestionsBean questionsBean =
//                                    (QuestionnaireQuestionsBean) cvQuestionAnswers.get(count);
//                            if(questionsBean.getQuestionNumber().intValue() ==
//                                    questionnaireQuestionsBean.getParentQuestionNumber().intValue()
//                                    && !questionnaireQuestionsBean.isConditionalFlag()
//                                    && questionsBean.getAnswerNumber() == 1){
//                                cvFilteredQuestions.add(questionnaireQuestionsBean);
//                                cvQuestionToAdd.add(questionnaireQuestionsBean.getQuestionNumber());
//                            }
//                        }
//                    }
//                }
//            }
//            //Adding the child questions for the already added questions
//            if(cvQuestionToAdd.size() > 0){
//                for(int index = 0; index < cvQuestionToAdd.size(); index++){
//                    for(int count = 0; count < cvNextPageQuestions.size(); count++){
//                        QuestionnaireQuestionsBean questionnaireQuestionsBean =
//                                (QuestionnaireQuestionsBean) cvNextPageQuestions.get(count);
//                        if(questionnaireQuestionsBean.getParentQuestionNumber().intValue() ==
//                                ((Integer)cvQuestionToAdd.get(index)).intValue()){
//                            cvFilteredQuestions.add(questionnaireQuestionsBean);
//                            cvNextPageQuestions.remove(count--);
//                            cvQuestionToAdd.add(questionnaireQuestionsBean.getQuestionNumber());
//                        }
//                    }
//                }
//            }
        }
        return cvFilteredQuestions;
    }
    
    
    private CoeusVector getNextPageQuestions(int parentQuesNum, CoeusVector cvNextPageQuestions,
            CoeusVector cvFilteredQuestions, CoeusVector cvQuestionToAdd, CoeusVector cvQuestionAnswers){
        if(parentQuesNum != -1){
            Equals eqParentQuesNum = new Equals(PARENT_QUESTION_NUMBER, new Integer(parentQuesNum));
            Equals eqParentQuesCond = new Equals("conditionalFlag", false);
            And eqParentAndEqConition = new And(eqParentQuesNum, eqParentQuesCond);
            CoeusVector cvQuestions = cvNextPageQuestions.filter(eqParentAndEqConition);
            if(cvQuestions != null && !cvQuestions.isEmpty()){
                cvFilteredQuestions.addAll(cvQuestions);
                for(int index = 0; index < cvQuestions.size(); index++){
                    QuestionnaireQuestionsBean questionsBean =
                            (QuestionnaireQuestionsBean) cvQuestions.get(index);
                    cvQuestionToAdd.add(questionsBean.getQuestionNumber());
                }
            }
            
        } else {
            if(cvQuestionAnswers != null && cvQuestionAnswers.size() > 0){
                for(int index = 0; index < cvNextPageQuestions.size(); index++){
                    QuestionnaireQuestionsBean questionnaireQuestionsBean =
                            (QuestionnaireQuestionsBean) cvNextPageQuestions.get(index);
                    for(int count = 0; count < cvQuestionAnswers.size(); count++){
                        QuestionnaireQuestionsBean questionsBean =
                                (QuestionnaireQuestionsBean) cvQuestionAnswers.get(count);
                        if(questionsBean.getQuestionNumber().intValue() ==
                                questionnaireQuestionsBean.getParentQuestionNumber().intValue()
                                && !questionnaireQuestionsBean.isConditionalFlag()
                                && questionsBean.getAnswerNumber() == 1){
                            cvFilteredQuestions.add(questionnaireQuestionsBean);
                            cvQuestionToAdd.add(questionnaireQuestionsBean.getQuestionNumber());
                        }
                    }
                }
            }
        }
        //Adding the child questions for the already added questions
        if(cvQuestionToAdd.size() > 0){
            for(int index = 0; index < cvQuestionToAdd.size(); index++){
                for(int count = 0; count < cvNextPageQuestions.size(); count++){
                    QuestionnaireQuestionsBean questionnaireQuestionsBean =
                            (QuestionnaireQuestionsBean) cvNextPageQuestions.get(count);
                    if(questionnaireQuestionsBean.getParentQuestionNumber().intValue() ==
                            ((Integer)cvQuestionToAdd.get(index)).intValue()){
                        cvFilteredQuestions.add(questionnaireQuestionsBean);
                        cvNextPageQuestions.remove(count--);
                        cvQuestionToAdd.add(questionnaireQuestionsBean.getQuestionNumber());
                    }
                }
            }
        }
        return cvFilteredQuestions;
    }
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Method to get the rule condition result
     * @param questionnaireQuestionsBean 
     * @param conditionRuleId 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     * @return 
     */
    private boolean ruleConditionResult(QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean, int conditionRuleId) throws DBException{
        boolean isPassed = false;
        RulesTxnBean ruleTxnBean = new RulesTxnBean();
        isPassed = ruleTxnBean.evaluateRule(questionnaireAnswerHeaderBean.getModuleItemCode(),questionnaireAnswerHeaderBean.getModuleSubItemCode(),
                questionnaireAnswerHeaderBean.getModuleItemKey(),questionnaireAnswerHeaderBean.getModuleSubItemKey(),
                conditionRuleId,"Q",loggedInUser);
        return isPassed;
    }    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    /**
     * Method to remove all the conditional child branching questions
     * @param qnrUpdTxnBean 
     * @param cvChildQuestions 
     * @param qnrCompletionId 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    private void removeConditionalChildQuestions(QuestionnaireUpdateTxnBean qnrUpdTxnBean, CoeusVector cvChildQuestions,int qnrCompletionId) throws CoeusException, DBException{
        if(!cvChildQuestions.isEmpty()){
            QuestionnaireQuestionsBean questionBean = (QuestionnaireQuestionsBean)cvChildQuestions.get(0);
            int questionNumber = questionBean.getQuestionNumber().intValue();
            cvChildQuestions.addAll(getChildQuestions(questionNumber));
            qnrUpdTxnBean.deleteQuesAnsForCompletionId(qnrCompletionId,questionNumber);
            cvChildQuestions.remove(0);
            if(cvChildQuestions != null && !cvChildQuestions.isEmpty()){
                QuestionnaireQuestionsBean qnrQuestionBean = (QuestionnaireQuestionsBean)cvChildQuestions.get(0);
                qnrUpdTxnBean.deleteQuesAnsForCompletionId(qnrCompletionId,qnrQuestionBean.getQuestionNumber().intValue());
                cvChildQuestions.remove(0);
                 
                cvChildQuestions.addAll(getChildQuestions(qnrQuestionBean.getQuestionNumber().intValue()));
                
                removeConditionalChildQuestions(qnrUpdTxnBean,cvChildQuestions,qnrCompletionId);
            }else{
                qnrUpdTxnBean.deleteQuesAnsForCompletionId(qnrCompletionId,questionBean.getQuestionNumber().intValue());
            }
        }
    }
    
    /**
     * Method to remove all the child branching questions
     * @param parentQuestionNumber 
     * @param questionnaireComplId 
     * @param cvQuestionnaireQuestions 
     * @param cvFilteredQuesNumber 
     * @param qnrUpdTxnBean 
     * @throws edu.mit.coeus.exception.CoeusException 
     * @throws edu.mit.coeus.utils.dbengine.DBException 
     */
    private void removeChildQuestions(Integer parentQuestionNumber,int questionnaireComplId,
            CoeusVector cvQuestionnaireQuestions,CoeusVector cvFilteredQuesNumber,QuestionnaireUpdateTxnBean qnrUpdTxnBean) throws CoeusException, DBException{
        Equals eqParentQuesNumber = new Equals("parentQuestionNumber",parentQuestionNumber);
        CoeusVector cvFiltered = cvQuestionnaireQuestions.filter(eqParentQuesNumber);
        HashSet hsFilteredQuesNumber = new HashSet();
        for(Object questionnaireQuestions: cvFiltered){
            QuestionnaireQuestionsBean questionnaireQuestionBean = (QuestionnaireQuestionsBean)questionnaireQuestions;
            hsFilteredQuesNumber.add(questionnaireQuestionBean.getQuestionNumber());
        }
        cvFilteredQuesNumber.addAll(hsFilteredQuesNumber);
        if(!hsFilteredQuesNumber.isEmpty()){
            Iterator itQuesNumber = hsFilteredQuesNumber.iterator();
            while(itQuesNumber.hasNext()){
                Integer questionNumber =  (Integer)itQuesNumber.next();
                qnrUpdTxnBean.deleteQuesAnsForCompletionId(questionnaireComplId,questionNumber.intValue());
            }
        }
        hsFilteredQuesNumber = null;
        if(cvFilteredQuesNumber != null && !cvFilteredQuesNumber.isEmpty()){
            Integer questionNumber = (Integer)cvFilteredQuesNumber.get(0);
            cvFilteredQuesNumber.remove(0);
            removeChildQuestions(questionNumber,questionnaireComplId,cvQuestionnaireQuestions,cvFilteredQuesNumber,qnrUpdTxnBean);
        }

    }
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
    /**
     * Method to check the validation for condition questions
     * @param answer
     * @param questionsBean
     * @param questionnaireAnswerHeaderBean
     * @throws edu.mit.coeus.utils.dbengine.DBException
     * @return
     */
    public boolean isConditionPassed(String answer,
            QuestionnaireQuestionsBean questionsBean, QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean) throws DBException{
        boolean isPassed = false;
        if(!"".equals(questionsBean.getConditionValue())){
            boolean isConditionPassed = conditionResult(answer, questionsBean.getCondition(), questionsBean.getConditionValue());
            if(questionsBean.getConditionRuleId() != 0){
                boolean isRulePassed = ruleConditionResult(questionnaireAnswerHeaderBean, questionsBean.getConditionRuleId());
                if(isConditionPassed && isRulePassed){
                    isPassed = true;
                }else{
                    isPassed = false;
                }
            }else if(isConditionPassed){
                isPassed = true;
            }
        }else if(questionsBean.getConditionRuleId() != 0){
            isPassed = ruleConditionResult(questionnaireAnswerHeaderBean, questionsBean.getConditionRuleId());
        }
        return isPassed;
    }

}
