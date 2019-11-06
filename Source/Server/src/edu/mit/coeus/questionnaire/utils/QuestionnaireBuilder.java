package edu.mit.coeus.questionnaire.utils;
import org.apache.struts.action.DynaActionForm;
import java.util.*;

import edu.mit.coeus.questionnaire.utils.AnswerHandler;


/** This class builds a tree of questions based on the page number. It takes all the questions for a questionnaire
 *  and then form a tree. It decides which question to be asked based in the answer from the user.
 *
 */
public class QuestionnaireBuilder {

    private String rootParent = "0";
    private int rootRow;
    private String strPageNumber;
    private Vector vecConditionalQuestionNumbers;
    private Vector vecUnConditionalQuestionNumbers;
    private Vector vecFalseConditionValueQuestions;
    private HashMap hmapCollection;
    private Vector vecAllChildren;
    private Integer intPageNumber;
    private String delimiter = ",";
    private int rootLevel = 1;
    private int immediateChild = 2;
    private int otherChild = 3;
    private String qNumber;
    private String pLevel;
    /**
     * This method rebuilds the question tree when user clicks the previous button. Only the page to be shown will
     *  be rebuilt and already viewed pafe will not be affected
     * @param tmapQuestionnaire
     * @param tMapPageNumbers
     * @param intPageNumber
     * @return TreeMap
     */
    public TreeMap synchronizeQuestionnaireMap(TreeMap tmapQuestionnaire, TreeMap tMapPageNumbers,
                                               Integer intPageNumber,Vector vecQuestionnaireQuestion ){
        TreeMap tMapNewQuestions    = new TreeMap();
        DynaActionForm objDynaForm  = new DynaActionForm();
        Vector vecQuestions         = (Vector)tMapPageNumbers.get(intPageNumber);
        String strQNo               = null;
        String strAnswer            = null;
        Integer intPageLevel        = new Integer(0);
        if(checkVectorSize(vecQuestions)){
            int intSize = vecQuestions.size();
            for (int start=0;start<intSize;start++){
                objDynaForm     = (DynaActionForm)vecQuestions.elementAt(start);
                strQNo          = (String)objDynaForm.get("questionNumber");
                strAnswer       = (String)objDynaForm.get("answer");
                intPageLevel    = getPageNumberFromMap(strQNo,tmapQuestionnaire,objDynaForm);
            }
            getValidQuestions(vecQuestionnaireQuestion ,tMapPageNumbers,strQNo, strAnswer);
            tMapNewQuestions = prepareNewTree(tmapQuestionnaire,tMapPageNumbers,intPageLevel,intPageNumber);
            tMapNewQuestions = constructFinalTree(trimValue(strQNo),tMapNewQuestions,intPageNumber,vecQuestionnaireQuestion);
        }
        return tMapNewQuestions;
    }
    /**
     * This method rebuilds the question tree when user clicks the previous button. Only the page to be shown will
     *  be rebuilt and already viewed pafe will not be affected
     * @param strQNo
     * @param tmaptMapNewQuestions
     * @return TreeMap
     */

    public TreeMap constructFinalTree(String strQNo,TreeMap tmaptMapNewQuestions,
                                      Integer intPageCheckLevel, Vector vecQuestionnaireQuestion){
        TreeMap tmapFinalQuestionnaire = new TreeMap();
        Iterator questionnaireIterator  = tmaptMapNewQuestions.keySet().iterator();
        DynaActionForm  objDynaForm     = new DynaActionForm();
        Vector vecQuestionNos;
        Integer pageCheck = getPageNumberFromMap(strQNo,tmaptMapNewQuestions,objDynaForm);
        String  strPageLevel = trimValue(getQuestionPageLevel(strQNo,tmaptMapNewQuestions));
        Vector vecQuestionsToBeAsked = getAllChildrenTillLeaf();
        Vector vecUnMatchedChildren = new Vector();
        boolean boolCheckFlag = true;
        while(questionnaireIterator.hasNext()){
            boolCheckFlag = false;
            vecQuestionNos = new Vector();
            Integer intPage = (Integer)questionnaireIterator.next();
            if(intPage.intValue()> pageCheck.intValue()){
                vecQuestionNos = (Vector)tmaptMapNewQuestions.get(intPage);
                int intSize = vecQuestionNos.size();
                for(int start=0;start<intSize;start++){
                    objDynaForm = (DynaActionForm)vecQuestionNos.elementAt(start);
                    String strParent   = objDynaForm.get("parentQuestionNumber").toString();
                    String strLevel    = objDynaForm.get("pageLevel").toString();
                    String strQuestion = objDynaForm.get("questionNumber").toString();
                    String strCondFlag = trimValue(objDynaForm.get("conditionFlag").toString());
                    if (Integer.parseInt(strLevel)>intPageCheckLevel.intValue()){
                        if(checkVectorSize(vecQuestionsToBeAsked)&&vecQuestionsToBeAsked.contains(strQuestion)
                                || (strParent.equals(strQNo))){
                            boolCheckFlag = true;
                        }
                    }
                    /*
                    else if((Integer.parseInt(strLevel)== immediateChild) && strCondFlag.equals("Y")){
                        getUnmatchedChild(strParent, vecQuestionnaireQuestion,objDynaForm,tmaptMapNewQuestions);
                        vecUnMatchedChildren = getFalseConditionValueQuestions();
                        if (checkVectorSize(vecUnMatchedChildren) && !vecUnMatchedChildren.contains(strQuestion)){
                            boolCheckFlag = true;
                        }
                    }

                    else {
                        boolCheckFlag = true;
                    }
                    */
                    else if (Integer.parseInt(strLevel)<=intPageCheckLevel.intValue()){
                        boolCheckFlag = true;
                    }
                }
                if(boolCheckFlag && checkVectorSize(vecQuestionNos)){
                    tmapFinalQuestionnaire.put(incrementIntPage(getIntPageNumber()),vecQuestionNos);
                }
            }
            else{
                if (intPage.intValue() ==1){
                    tmapFinalQuestionnaire.put(intPage,tmaptMapNewQuestions.get(intPage));
                    setIntPageNumber(intPage);
                }
                else{
                    tmapFinalQuestionnaire.put(incrementIntPage(getIntPageNumber()),tmaptMapNewQuestions.get(intPage));
                }
            }
        }
        return tmapFinalQuestionnaire;
    }
    /**
     * Gets the page level of that question. Say for root question the page level will be 1 and the level
     *  increments to its sebsequent children
     * @param strQNo
     * @param tmapQuestionnaire
     * @return
     */
    public String getQuestionPageLevel(String strQNo, TreeMap tmapQuestionnaire){
        Iterator quesIterator       = tmapQuestionnaire.keySet().iterator();
        DynaActionForm  objDynaForm = new DynaActionForm();
        Vector vecQuestionNos;
        String  strPageLevel        = null;
        GET_PAGE_LEVEL:
        while(quesIterator.hasNext()){
            vecQuestionNos  = new Vector();
            Integer intPage = (Integer)quesIterator.next();
            vecQuestionNos  = (Vector)tmapQuestionnaire.get(intPage);
            int intSize     = vecQuestionNos.size();
            for(int start=0;start<intSize;start++){
                objDynaForm         = (DynaActionForm)vecQuestionNos.elementAt(start);
                if(trimValue(objDynaForm.get("questionNumber").toString()).equalsIgnoreCase(strQNo)){
                    strPageLevel    = objDynaForm.get("pageLevel").toString();
                    break GET_PAGE_LEVEL;
                }
            }
        }
        return strPageLevel;
    }

    /**
     * This method rebuilds the question tree when user clicks the previous button. Only the page to be shown will
     *  be rebuilt and already viewed pafe will not be affected
     * @param vecQuestionnaireQuestion
     * @param tMapPageNumbers
     * @param strLastQuestion
     * @param strAnswer
     * @return
     */
    public void getValidQuestions(Vector vecQuestionnaireQuestion ,TreeMap tMapPageNumbers,
                                  String strLastQuestion, String strAnswer){
        DynaActionForm objDynaForm      = new DynaActionForm();
        int intVecQuestionnairreSize    = vecQuestionnaireQuestion.size();
        Vector vecWantedChild           = new Vector();
        for (int start=0;start<intVecQuestionnairreSize;start++){
            objDynaForm         = (DynaActionForm)vecQuestionnaireQuestion.elementAt(start);
            String strParent    = (String)objDynaForm.get("parentQuestionNumber");
            String strCondFlag      = (String)objDynaForm.get("conditionFlag");
            if(strLastQuestion.equals(strParent) ){
                String strQNO               = (String)objDynaForm.get("questionNumber");
                vecWantedChild.add(strQNO);
            }
        }
        if(checkVectorSize(vecWantedChild)){
            for (int start=0; start<vecWantedChild.size();start++){
                setAllChildrenTillLeaf(vecWantedChild.elementAt(start).toString());
            }
            getRecursiveChildren(vecWantedChild,vecQuestionnaireQuestion);
        }
    }
    /**
     * This method rebuilds the question tree when user clicks the previous button. Only the page to be shown will
     *  be rebuilt and already viewed pafe will not be affected
     * @param tMapQuestionnaireQuestions
     * @param tMapPageNumbers
     * @param intCopyFrom
     * @param intCopyTo
     * @return
     */
    public TreeMap prepareNewTree(TreeMap tMapQuestionnaireQuestions,TreeMap tMapPageNumbers,
                                  Integer intCopyFrom, Integer intCopyTo){
        TreeMap tMapNewQuestions = new TreeMap();
        Iterator questionnaireIterator  = tMapQuestionnaireQuestions.keySet().iterator();
        Iterator pageIterator           = tMapPageNumbers.keySet().iterator();
        DynaActionForm objDynaForm      = new DynaActionForm();
        while (pageIterator.hasNext()){
            //Put the previous page questions in the map first
            Integer page  = (Integer)pageIterator.next();
            if(page.intValue() <= intCopyTo.intValue()){
                tMapNewQuestions.put(page,tMapPageNumbers.get(page));
                setIntPageNumber(page);
            }
        }
        while(questionnaireIterator.hasNext()){
            //Put the Next questions to be asked in the map
            Integer pageQuestionnaire  = (Integer)questionnaireIterator.next();
            if(pageQuestionnaire.intValue() > intCopyFrom.intValue()){
                tMapNewQuestions.put(incrementIntPage(getIntPageNumber()),tMapQuestionnaireQuestions.get(pageQuestionnaire));
            }
        }

        return tMapNewQuestions;
    }
    /**
     * Utility method to get page number from the map
     * @param strQNo
     * @param tMapQuestionnaireQuestions
     * @param objDynaForm
     * @return
     */
    public Integer getPageNumberFromMap(String strQNo,TreeMap tMapQuestionnaireQuestions,DynaActionForm objDynaForm){

        Iterator questionnaireIterator   = tMapQuestionnaireQuestions.keySet().iterator();
        Integer mapPageNumber = new Integer(1);
        GET_PAGE_NUMBER:
        while(questionnaireIterator.hasNext()){
            Vector vecQuestNumbers      = new Vector();
            Integer pageQuestionnaire  = (Integer)questionnaireIterator.next();
            vecQuestNumbers    = (Vector)tMapQuestionnaireQuestions.get(pageQuestionnaire);
            int intVecSize     = vecQuestNumbers.size();
            for(int start=0;start<intVecSize;start++){
                objDynaForm       = (DynaActionForm)vecQuestNumbers.elementAt(start);
                if(trimValue(objDynaForm.get("questionNumber").toString()).equalsIgnoreCase(strQNo)){
                    mapPageNumber = pageQuestionnaire;
                    break GET_PAGE_NUMBER;
                }
            }
        }
        return mapPageNumber;
    }
    /**
     * This method builds a base tree whith all possible questions to be asked. This is done for one time during
     * the strt of the questionnaire.
     *
     * @param vecQuestionnaireQuestions
     * @return
     * @throws Exception
     */
    // Remove
    public TreeMap prepareQuestionnaireTree(Vector vecQuestionnaireQuestions) throws Exception{

        Vector vecImmediateChildren     = new Vector();
        HashMap mapQuestions            = new HashMap();
        String strRootQuestionNumber    = null;
        DynaActionForm objDynaForm      = new DynaActionForm();
        Vector vecRootQuestionNumber    = null;
        TreeMap objTreeMap = new TreeMap();
        vecRootQuestionNumber = getRootQuestionNumber(vecQuestionnaireQuestions, objDynaForm);
        initializePage();
        for (int start=0;start<vecRootQuestionNumber.size();start++){
            strRootQuestionNumber = (String)vecRootQuestionNumber.elementAt(start);
            Vector vecRootQuestion      = new Vector();
            vecRootQuestion.add(strRootQuestionNumber+delimiter+rootLevel);
            // put root row in the map as first page
            if(start == 0){
                mapQuestions.put(getIntPageNumber(), vecRootQuestion);
            }
            else{
                mapQuestions.put(incrementIntPage(getIntPageNumber()), vecRootQuestion);
            }


            // Find out the imnmediate children of root
            vecImmediateChildren = getImmdeiateChildren(vecQuestionnaireQuestions, strRootQuestionNumber,objDynaForm);
            // Iterate thru all children till leaf and Build questions in a map with page number
            //mapQuestions = buildNewQuestions (vecQuestionnaireQuestions,vecImmediateChildren,objDynaForm);
            for(int begin=0; begin<vecImmediateChildren.size();begin++){
                String strImmediateChild =  (String)vecImmediateChildren.elementAt(begin);
                mapQuestions = buildQuestionsConditionally(vecQuestionnaireQuestions,strImmediateChild,objDynaForm,strRootQuestionNumber,mapQuestions);
            }
        }
        objTreeMap = buildQuestionnaire(new TreeMap(mapQuestions), vecQuestionnaireQuestions, objDynaForm);
        return objTreeMap;
    }
    public void setHashMapCollection(HashMap mapQuestions){


    }
    public HashMap formHashMap(HashMap mapQuestionsPage){
        HashMap mapQuestions            = new HashMap();
        Iterator questionIterator   = mapQuestionsPage.keySet().iterator();
         while(questionIterator.hasNext()){
            Integer intPageNumber   = (Integer)questionIterator.next();
            HashMap hmap            = (HashMap)mapQuestionsPage.get(intPageNumber);
            Iterator mapIterator    = hmap.keySet().iterator();
            while (mapIterator.hasNext()){
                Integer intNumber   = (Integer)mapIterator.next();
                mapQuestions.put(intNumber,hmap.get(intNumber));
            }

        }
        return mapQuestions;
    }
    /**
     * Utility  method to get next question
     * @param tMapQuestions
     * @param strAnswer
     * @param strPage
     * @return
     */
    public TreeMap getNextQuestions(TreeMap tMapQuestions, String strAnswer, String strPage){
        TreeMap tMapOldQuestions = new TreeMap();
        HashMap hMapNewQuestions = new HashMap();
        TreeMap tMapNewQuestions = new TreeMap();
        Integer intNextPageNo = new Integer(Integer.parseInt(strPage));
        // Store the records till the page where questions had already asked
        Iterator questionIterator   = tMapQuestions.keySet().iterator();
        while(questionIterator.hasNext()){
            Integer intPageNumber   = (Integer)questionIterator.next();
            if (intPageNumber.intValue() < intNextPageNo.intValue()){
                tMapOldQuestions.put(intPageNumber,tMapQuestions.get(intPageNumber));
            }
            else{
                hMapNewQuestions.put(intPageNumber,tMapQuestions.get(intPageNumber));
            }
        }
        // Check the conditional Flag of Next Question, if true store in a Vector for comparing answers.
         boolean isConditionalQuestion  = isConditionalQuestion(hMapNewQuestions, intNextPageNo);
         // Convert the Map Questions to Vector
         Vector vecNewQuestions = getListFromMap(new TreeMap(hMapNewQuestions));
        // If conditional Question compare the answer with the conditional value of child question.
        if(isConditionalQuestion){
            //setUnMatchedQuestions(hMapNewQuestions, intNextPageNo, strAnswer);
            setUnMatchedQuestions(vecNewQuestions, intNextPageNo, strAnswer,tMapQuestions);
            // Remove all the grandchildren of false conditional value questions;
            Vector vecUnWantedChildren = removeAllUnwantedChild(vecNewQuestions);
            // Delete all children for that parent and form a new Map
            if(checkVectorSize(vecUnWantedChildren)){
                tMapNewQuestions            = formNewQuestionnaireTree(tMapQuestions, vecUnWantedChildren);
            }
            else{
                tMapNewQuestions = tMapQuestions;
            }
        }
        else{
            tMapNewQuestions = tMapQuestions;
        }
        return tMapNewQuestions;
    }
    /**
     * Removes questions which do not match the answer
     * @param vecAllQuestions
     * @return
     */
    public Vector removeAllUnwantedChild (Vector vecAllQuestions){

        Vector vecUnmatchedQuestion = getFalseConditionValueQuestions();
        initUnMatchedChildren();
        for (int start=0; start<vecUnmatchedQuestion.size();start++){
            setAllChildrenTillLeaf(vecUnmatchedQuestion.elementAt(start).toString());
        }
        getRecursiveChildren(vecUnmatchedQuestion,vecAllQuestions);

        return getAllChildrenTillLeaf();
        //Vector vecNewQuestions = buildNextQuestions(vecAllQuestions);
        //return vecNewQuestions;
    }

    /**
     * Recursives till leaf questionb of a parent question
     * @param vecChild
     * @param vecAllQuestions
     */
    public void getRecursiveChildren(Vector vecChild, Vector vecAllQuestions){
        Vector vecChildren = new Vector();
        DynaActionForm objDynaForm  = new DynaActionForm();
        int intAllQuestionsSize     = vecAllQuestions.size();
        int intChild       = vecChild.size();
        for(int start=0;start<intChild;start++){
            String strChild = vecChild.elementAt(start).toString();
            for (int begin=0;begin<intAllQuestionsSize;begin++){
                objDynaForm         = (DynaActionForm)vecAllQuestions.elementAt(begin);
                String strParentQNO = (String)objDynaForm.get("parentQuestionNumber");
                if(strChild.equals(strParentQNO)){
                    objDynaForm         = (DynaActionForm)vecAllQuestions.elementAt(begin);
                    String strQuestion  = (String)objDynaForm.get("questionNumber");
                    setAllChildrenTillLeaf(strQuestion);
                    vecChildren.add(strQuestion);
                }
            }
        }
        if(checkVectorSize(vecChildren)){
            getRecursiveChildren(vecChildren, vecAllQuestions);
        }
    }

    /**
     *
     */
    public void initUnMatchedChildren(){
        this.vecAllChildren = new Vector();
    }
    public Vector getAllChildrenTillLeaf(){
        return this.vecAllChildren;
    }
    public void setAllChildrenTillLeaf(String strChild){
        if (checkVectorSize(this.vecAllChildren) &&
           ! this.vecAllChildren.contains(strChild)){
            this.vecAllChildren.add(strChild);
        }
        else{
            this.vecAllChildren = new Vector();
            this.vecAllChildren.add(strChild);
        }
    }
    /**
     *
     * @param tMapQuestions
     * @return
     */
    public TreeMap formNewQuestionnaireTree(TreeMap tMapQuestions, Vector vecUnMatchedChildren){

        DynaActionForm objDynaForm  = new DynaActionForm();
        TreeMap tMapNewQuestions    = new TreeMap();
        Iterator questionIterator   = tMapQuestions.keySet().iterator();
        Vector vecQuestionnaire     = new Vector();
        while(questionIterator.hasNext()){
            boolean flag            = true;
            Integer intPageNumber   = (Integer)questionIterator.next();
            vecQuestionnaire        = (Vector)tMapQuestions.get(intPageNumber);
            Vector vecNewQuestions  = new Vector();
            int intVecSize          = vecQuestionnaire.size();
            int intIndex            = 0;
            for(int start=0;start<intVecSize;start++){
                objDynaForm     = (DynaActionForm)vecQuestionnaire.elementAt(start);
                String strQNO   = objDynaForm.get("questionNumber").toString();
                if( !vecUnMatchedChildren.contains(strQNO)){
                    vecNewQuestions.add(vecQuestionnaire.elementAt(start));
                }
                /*
                MATCH_QUESTION:
                for (int begin =0;begin < vecUnMatchedChildren.size();begin++){
                    flag = true;
                    String strUnMatchedQuestion = vecUnMatchedChildren.elementAt(begin).toString();
                    if(strQNO.equals(strUnMatchedQuestion)){
                        flag = false;
                        break MATCH_QUESTION;
                    }
                }
                if(flag){
                    vecNewQuestions.add(vecQuestionnaire.elementAt(start));
                }
                */
            }

            if (checkVectorSize(vecNewQuestions)){
                if(intPageNumber.intValue() == 1){
                    tMapNewQuestions.put(getIntPageNumber(),vecNewQuestions);
                }
                else{
                    tMapNewQuestions.put(incrementIntPage(getIntPageNumber()),vecNewQuestions);
                }

            }
        }
        return tMapNewQuestions;
    }


    /**
     *
     * @param vecNewQuestions
     * @param intNextPageNo
     * @param strAnswer
     * @return
     */
    public void setUnMatchedQuestions(Vector vecNewQuestions, Integer intNextPageNo,
                                      String strAnswer, TreeMap tMapQuestions){
        boolean isMatchingAnswer;
        Vector vecConditionalQuestions;
        DynaActionForm objDynaActionForm    = new DynaActionForm();;
        vecConditionalQuestions             = getConditionalQuestionNumbers();
        int intConditionSize                = vecConditionalQuestions.size();
        int intAllQuestionSize              = vecNewQuestions.size();
        Vector vecFalseValues               = new Vector();
        Vector vecMatchingQuestions         = new Vector();
        for(int start = 0; start<intConditionSize;start++){
            String strCondQno       = (String) vecConditionalQuestions.elementAt(start);
            for(int begin=0;begin<intAllQuestionSize;begin++){
                objDynaActionForm       = (DynaActionForm)vecNewQuestions.elementAt(begin);
                String strQuestionNo    = (String)objDynaActionForm.get("questionNumber");
                String strDataType      = (String)objDynaActionForm.get("answerDataType");
                if(strCondQno.equals(strQuestionNo)){
                    String strParent        = (String)objDynaActionForm.get("parentQuestionNumber");
                    getUnmatchedChild(strParent, vecNewQuestions,objDynaActionForm, tMapQuestions);
                    break;
                }
            }

        }
    }
    public void getUnmatchedChild(String strParent, Vector vecNewQuestions,DynaActionForm objDynaActionForm,TreeMap tMapQuestions ){
        Vector vecUnmatchedChild =  new Vector();

        for (int start=0;start<vecNewQuestions.size();start++){
             boolean isMatchingAnswer = false;
            objDynaActionForm       = (DynaActionForm)vecNewQuestions.elementAt(start);
            String strParentChild   = (String)objDynaActionForm.get("parentQuestionNumber");
            if(strParent.equals(strParentChild)) {
                String strParentAnswer  = getParentAnswer(tMapQuestions,strParent);
                String strDataType      = getDataTypeofParent(tMapQuestions,strParentChild);
                String strQuestionNo    = (String)objDynaActionForm.get("questionNumber");
                String strCondValue     = (String)objDynaActionForm.get("conditionValue");
                String strCondition     = (String)objDynaActionForm.get("condition");
                String strCondFlag      =  (String)objDynaActionForm.get("conditionFlag");
                strCondFlag             = (strCondFlag == null)?"N":"Y";
                if ( strCondFlag.equals("Y") && isNotNull(strCondition) && isNotNull(strCondValue) && isNotNull(strDataType)){
                    AnswerHandler objAnswer = new AnswerHandler();
                    isMatchingAnswer        = objAnswer.isAnswerValid(strParentAnswer,strCondition,strDataType,strCondValue);
                    if(!isMatchingAnswer){
                        vecUnmatchedChild.add(strQuestionNo);

                    }
                }
            }
        }
        setFalseConditionValueQuestions(vecUnmatchedChild);
    }
    public String getDataTypeofParent(TreeMap tMapQuestions,String strParent){
        String strDataType = null;
        DynaActionForm objDynaForm  = new DynaActionForm();
        Iterator questionIterator   = tMapQuestions.keySet().iterator();
        Vector vecQuestionnaire = new Vector();
        while(questionIterator.hasNext()){
            Integer intPageNumber   = (Integer)questionIterator.next();
            vecQuestionnaire        = (Vector)tMapQuestions.get(intPageNumber);
            int intVecSize          = vecQuestionnaire.size();
            for(int start=0;start<intVecSize;start++){
                objDynaForm     = (DynaActionForm)vecQuestionnaire.elementAt(start);
                String strQNO   = objDynaForm.get("questionNumber").toString();
                if(strParent.equals(strQNO)){
                    strDataType     = (String)objDynaForm.get("answerDataType");
                }
            }
        }
        return strDataType;

    }
    public String getParentAnswer(TreeMap tMapQuestions,String strParent){
        String strParentAnswer = null;
        DynaActionForm objDynaForm  = new DynaActionForm();
        Iterator questionIterator   = tMapQuestions.keySet().iterator();
        Vector vecQuestionnaire = new Vector();
        while(questionIterator.hasNext()){
            Integer intPageNumber   = (Integer)questionIterator.next();
            vecQuestionnaire        = (Vector)tMapQuestions.get(intPageNumber);
            int intVecSize          = vecQuestionnaire.size();
            for(int start=0;start<intVecSize;start++){
                objDynaForm     = (DynaActionForm)vecQuestionnaire.elementAt(start);
                String strQNO   = objDynaForm.get("questionNumber").toString();
                if(strParent.equals(strQNO)){
                    strParentAnswer     = (String)objDynaForm.get("answer");
                }
            }
        }
         return strParentAnswer;
    }
    /**
     *
     * @param hMapQuestions
     * @param intNextPageNo
     * @return
     */
    public boolean isConditionalQuestion(HashMap hMapQuestions, Integer intNextPageNo){
        boolean isConditionalQuestion       = false;
        Vector vecNextQuestions             = new Vector();
        Vector vecConditionFlags            = new Vector();
        Vector vecUnconditionalQuestions    = new Vector();
        Vector vecConditionalQuestions      = new Vector();
        DynaActionForm objDynaActionForm    = new DynaActionForm();
        vecNextQuestions                    = (Vector)hMapQuestions.get(intNextPageNo);
        int intSizeOfVector                 = 0;
        if(checkVectorSize(vecNextQuestions)){
            intSizeOfVector = vecNextQuestions.size();
        }
        String strCondFlag                  = null;

        for(int start = 0; start<intSizeOfVector;start++){
            objDynaActionForm       = (DynaActionForm)vecNextQuestions.elementAt(start);
            strCondFlag             =  (String)objDynaActionForm.get("conditionFlag");
            vecConditionFlags.add(strCondFlag);
            if ( strCondFlag.equals("Y")){
                vecConditionalQuestions.add(objDynaActionForm.get("questionNumber"));
            }
            else{
                vecUnconditionalQuestions.add(objDynaActionForm.get("questionNumber"));
            }
        }
        setUnconditionalQuestions(vecUnconditionalQuestions);
        setConditionalQuestionNumbers(vecConditionalQuestions);
        isConditionalQuestion = (vecConditionFlags.contains("Y"))?true:false;
        return isConditionalQuestion;
    }
    /**
     *
     * @param hmapDynaBean
     * @return
     */
    public Vector getListFromMap(TreeMap hmapDynaBean){
        Vector vecDynaBean = new Vector();
        Iterator questionIterator = hmapDynaBean.keySet().iterator();
        while (questionIterator.hasNext()){
            Integer intKey = (Integer)questionIterator.next();
            Vector vecTemp = (Vector)hmapDynaBean.get(intKey);
            for(int start=0;start<vecTemp.size();start++){
                vecDynaBean.add(vecTemp.elementAt(start));
            }
        }

        return vecDynaBean;
    }
    /**
     *
     * @param vecFalseConditionValueQuestions
     */
    public void setFalseConditionValueQuestions(Vector vecFalseConditionValueQuestions){
        this.vecFalseConditionValueQuestions = new Vector();
        this.vecFalseConditionValueQuestions = vecFalseConditionValueQuestions;

    }
    /**
     *
     * @return
     */
    public Vector getFalseConditionValueQuestions(){

        return this.vecFalseConditionValueQuestions;
    }

    /**
     *
     * @param vecUnconditionalQuestionNumbers
     */
    public void setUnconditionalQuestions(Vector vecUnconditionalQuestionNumbers){
        this.vecUnConditionalQuestionNumbers = vecUnconditionalQuestionNumbers;
    }

    /**
     *
     * @return
     */
    public Vector getUnconditionalQuestions(){
        return this.vecUnConditionalQuestionNumbers;
    }
    /**
     *
     * @param vecConditionalQuestionNumbers
     */
    public void setConditionalQuestionNumbers(Vector vecConditionalQuestionNumbers){
        this.vecConditionalQuestionNumbers = vecConditionalQuestionNumbers;

    }
    /**
     *
     * @return
     */
    public Vector getConditionalQuestionNumbers(){

        return this.vecConditionalQuestionNumbers;
    }
    /**
     * @param vecQuestionnaireQuestions
     * @return  strRootQuestionNumber
     */
    public Vector getRootQuestionNumber(Vector vecQuestionnaireQuestions, DynaActionForm objDynaActionForm){
        String strRootQuestionNumber    = null;
        String strParentQuestionNumber  = null;
        Vector vecRootQuestions         = new Vector();
        int intSizeofQuestionnaire      = vecQuestionnaireQuestions.size();
        for (int start=0;start<intSizeofQuestionnaire;start++){
            objDynaActionForm       = (DynaActionForm)vecQuestionnaireQuestions.elementAt(start);
            strParentQuestionNumber = (String)objDynaActionForm.get("parentQuestionNumber");
            if(trimValue(strParentQuestionNumber).equals(rootParent)){
                vecRootQuestions.add((String)objDynaActionForm.get("questionNumber"));
            }

        }
        return vecRootQuestions;
    }
    /**
     *
     * @param vecQuestionnaireQuestions
     * @param strRootQuestionNumber
     * @return
     */
    public Vector getImmdeiateChildren (Vector vecQuestionnaireQuestions, String strRootQuestionNumber,
                                        DynaActionForm objDynaActionForm){
        Vector vecImmediateChildren     = new Vector();
        String strChildQuestionNumber   = null;
        String strParentQuestionNumber  = null;
        int intSizeofQuestionnaire      = vecQuestionnaireQuestions.size();

        for (int start=0;start<intSizeofQuestionnaire;start++){
            objDynaActionForm       = (DynaActionForm)vecQuestionnaireQuestions.elementAt(start);
            strParentQuestionNumber = (String)objDynaActionForm.get("parentQuestionNumber");
            if(trimValue(strParentQuestionNumber).equals(strRootQuestionNumber)){
                strChildQuestionNumber = (String)objDynaActionForm.get("questionNumber");
                vecImmediateChildren.add(strChildQuestionNumber);
            }

        }
        return vecImmediateChildren;
    }

    /**
     *
     * @param vecAllChildren
     * @param vecQuestionnaireQuestions
     * @param objDynaActionForm
     * @param mapQuestions
     * @return
     */
    public HashMap processTillLeaf(Vector vecAllChildren, Vector vecQuestionnaireQuestions,
                                   DynaActionForm objDynaActionForm, HashMap mapQuestions){
        int intVectorSize           = vecAllChildren.size();
        int  intSizeofQuestionnaire = vecQuestionnaireQuestions.size();
        String strParentQuestion    = null;
        String strQuestionNumber    = null;
        String strChild = null;
        Vector vecGrandChildren;
        int pageLevels = 0;
        for (int start = 0;start<intVectorSize;start++){
            pageLevels = otherChild+1;
            vecGrandChildren     = new Vector();
            strChild = trimValue(vecAllChildren.elementAt(start).toString());
            getQNumberAndPageLevelFromToken(strChild);
            for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
                objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
                strParentQuestion   = (String)objDynaActionForm.get("parentQuestionNumber");
                if(trimValue(strParentQuestion).equals(getQNbrFromToken())){
                    strQuestionNumber = (String)objDynaActionForm.get("questionNumber");
                    boolean boolFlag    = true;
                    Vector vecFalseQNos = getFalseConditionValueQuestions();
                    if(checkVectorSize(vecFalseQNos)){
                        for (int count =0; count<vecFalseQNos.size();count++){
                            String strFalseQuestion = vecFalseQNos.elementAt(count).toString();
                            if (trimValue(strFalseQuestion).equals(strQuestionNumber)){
                                boolFlag = false;
                            }
                        }
                    }
                    if(boolFlag){
                        vecGrandChildren.add(strQuestionNumber+delimiter+pageLevels);
                    }

                }
            }
            if(checkVectorSize(vecGrandChildren)){
                mapQuestions.put(incrementIntPage(getIntPageNumber()), vecGrandChildren);
            }
            processTillLeaf(vecGrandChildren,vecQuestionnaireQuestions,
                            objDynaActionForm,mapQuestions);
        }

        return mapQuestions;
    }
    /**
     *
     * @param vecAllChildren
     * @param vecQuestionnaireQuestions
     * @param objDynaActionForm
     * @param mapQuestions
     * @return
     */
    public HashMap  processTillLeafConditionally(Vector vecAllChildren,
                                                   Vector vecQuestionnaireQuestions,
                                                   DynaActionForm objDynaActionForm, HashMap mapQuestions, int intLevel){
        intLevel++;
        int intAllchildren          = vecAllChildren.size();
        int  intSizeofQuestionnaire = vecQuestionnaireQuestions.size();
        String strParentQuestion    = null;
        String strQuestionNumber    = null;
        String strChild = null;
        Vector vecGrandChildren;

        for (int start = 0;start<intAllchildren;start++){
            Vector vecLeafQuestion  = new Vector();
            vecGrandChildren        = new Vector();
            strChild = trimValue(vecAllChildren.elementAt(start).toString());
            getQNumberAndPageLevelFromToken(strChild);
            strChild = getQNbrFromToken();
            for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
                objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
                strParentQuestion   = (String)objDynaActionForm.get("parentQuestionNumber");
                if(trimValue(strParentQuestion).equals(strChild)){
                    strQuestionNumber = (String)objDynaActionForm.get("questionNumber");

                    if (isParentQuestion(strQuestionNumber,vecQuestionnaireQuestions,objDynaActionForm)){
                        vecGrandChildren.add(strQuestionNumber+delimiter+intLevel);
                        mapQuestions.put(incrementIntPage(getIntPageNumber()), vecGrandChildren);
                        mapQuestions = iterateRecursiveTillLeaf(vecGrandChildren, vecQuestionnaireQuestions,
                                                        objDynaActionForm, mapQuestions,intLevel);
                    }
                    else{
                        vecLeafQuestion.add(strQuestionNumber+delimiter+otherChild);
                    }


                }
            }
            if(checkVectorSize(vecLeafQuestion)){
                mapQuestions.put(incrementIntPage(getIntPageNumber()), vecLeafQuestion);
                mapQuestions = iterateRecursiveTillLeaf(vecLeafQuestion,vecQuestionnaireQuestions,objDynaActionForm,mapQuestions,intLevel);
            }
        }
     return mapQuestions;
    }

    public HashMap iterateRecursiveTillLeaf(Vector vecChildren, Vector vecQuestionnaireQuestions,
                                            DynaActionForm objDynaActionForm, HashMap mapQuestions, int intPageLevel){
        intPageLevel++;
        int intChildren =  vecChildren.size();
        int intSizeofQuestionnaire = vecQuestionnaireQuestions.size();
        String strChild = null;
        String strParentQuestion = null;
        String strQuestionNumber = null;
        for(int start=0; start<intChildren;start++){
            Vector vecLeafQuestion = new Vector();
            Vector vecGrandChildren = new Vector();
            strChild = trimValue(vecChildren.elementAt(start).toString());
            getQNumberAndPageLevelFromToken(strChild);
            strChild = getQNbrFromToken();
            for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
                objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
                strParentQuestion   = (String)objDynaActionForm.get("parentQuestionNumber");
                if(trimValue(strParentQuestion).equals(strChild)){
                    strQuestionNumber = (String)objDynaActionForm.get("questionNumber");
                    if (isParentQuestion(strQuestionNumber,vecQuestionnaireQuestions,objDynaActionForm)){
                        vecGrandChildren.add(strQuestionNumber+delimiter+intPageLevel);
                        mapQuestions.put(incrementIntPage(getIntPageNumber()), vecGrandChildren);
                        mapQuestions = iterateRecursiveTillLeaf(vecGrandChildren,vecQuestionnaireQuestions,
                                     objDynaActionForm,mapQuestions, intPageLevel);
                    }
                    else{
                        vecLeafQuestion.add(strQuestionNumber+delimiter+otherChild);
                    }

                }
            }
            if (checkVectorSize(vecLeafQuestion)){
                mapQuestions.put(incrementIntPage(getIntPageNumber()), vecLeafQuestion);

            }
            if (checkVectorSize(vecLeafQuestion)){
                iterateRecursiveTillLeaf(vecLeafQuestion,vecQuestionnaireQuestions,
                                     objDynaActionForm,mapQuestions, intPageLevel);
            }

        }

        return mapQuestions;
    }
    /**
     *
     * @param strQno
     * @param vecQuestionnaireQuestions
     * @param objDynaForm
     * @return
     */
    public boolean isConditionalLeafQuestion(String strQno,Vector vecQuestionnaireQuestions, DynaActionForm objDynaForm){
        boolean boolCondition = false;
        int intSize = vecQuestionnaireQuestions.size();
        String conditionFlag= null;
        Vector vecConditionFlag = new Vector();

        for (int start=0; start < intSize; start++ ){
            objDynaForm = (DynaActionForm)vecQuestionnaireQuestions.elementAt(start);
            String strCondQNo   = (String)objDynaForm.get("questionNumber");
            String strParent    = (String)objDynaForm.get("parentQuestionNumber");
            //Check for the child question's condition flag if the question is not a leaf
            // If the question is a leaf, then check for its conditional flag
            if(strQno.equals(strCondQNo)){
                conditionFlag = (String)objDynaForm.get("conditionFlag");
                vecConditionFlag.add(conditionFlag);
            }
        }
        boolCondition = (vecConditionFlag.contains("Y"))?true:false;
        return boolCondition;
    }
    /**
     *
     * @param strQno
     * @param vecQuestionnaireQuestions
     * @param objDynaForm
     * @return
     */
    public boolean isConditionalQuestion(String strQno,Vector vecQuestionnaireQuestions, DynaActionForm objDynaForm){
        boolean boolCondition = false;
        int intSize = vecQuestionnaireQuestions.size();
        String conditionFlag= null;
        Vector vecConditionFlag = new Vector();

        for (int start=0; start < intSize; start++ ){
            objDynaForm = (DynaActionForm)vecQuestionnaireQuestions.elementAt(start);
            String strCondQNo   = (String)objDynaForm.get("questionNumber");
            String strParent    = (String)objDynaForm.get("parentQuestionNumber");
            //Check for the child question's condition flag if the question is not a leaf
            // If the question is a leaf, then check for its conditional flag
            if(strQno.equals(strParent) ){
                conditionFlag = (String)objDynaForm.get("conditionFlag");
                conditionFlag = (conditionFlag.equalsIgnoreCase("Y"))?"Y":"N";
                vecConditionFlag.add(conditionFlag);
            }

        }
        boolCondition = (vecConditionFlag.contains("Y"))?true:false;
        return boolCondition;
    }
     /**
     *
     * @param vecQuestionnaireQuestions
     * @param strImmediateChild
     * @param objDynaActionForm
     * @return
     */
    public HashMap buildQuestionsConditionally (Vector vecQuestionnaireQuestions,
                                                String strImmediateChild,
                                                DynaActionForm objDynaActionForm,
                                                String strRootQuestion,
                                                HashMap mapQuestions)  {
        int intSizeofQuestionnaire  = vecQuestionnaireQuestions.size();
        String strParentQuestion    = null;
        String strQuestionNumber    = null;
         String strImmdeiatChildWithLevel = strImmediateChild+delimiter+immediateChild;
        //HashMap mapQuestions        = new HashMap();
            Vector vecAllChildren;
            vecAllChildren       = new Vector();

            // put the child in a vector
            vecAllChildren.add(strImmdeiatChildWithLevel);
            // Check if the immediate chid is conditional question. If yes, put in a separate page
            //getQNumberAndPageLevelFromToken(strImmediateChild);
            mapQuestions.put(incrementIntPage(getIntPageNumber()), vecAllChildren);
            vecAllChildren = new Vector();
           /*
            if (isConditionalQuestion(getQNbrFromToken(),vecQuestionnaireQuestions,objDynaActionForm)){
                vecAllChildren = new Vector();
            }
            */
            // Get all the child level questions for the immediate hild question.
            for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
                Vector vecGrandChild = new Vector();
                objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
                strParentQuestion   = (String)objDynaActionForm.get("parentQuestionNumber");
                // Extract QuestionNumber from the Token containing Qnumber and Page level.
                // Question Number will look like 1,2 which means QNo is 1 and level is 2
                //getQNumberAndPageLevelFromToken(strImmediateChild);
                //String strImmChild = getQNbrFromToken();
                if(trimValue(strParentQuestion).equals(strImmediateChild)){
                    strQuestionNumber   = (String)objDynaActionForm.get("questionNumber");
                    if (isParentQuestion(strQuestionNumber,vecQuestionnaireQuestions,objDynaActionForm)){
                        vecGrandChild.add(strQuestionNumber+delimiter+otherChild);
                        mapQuestions.put(incrementIntPage(getIntPageNumber()), vecGrandChild);
                        mapQuestions = processTillLeafConditionally(vecGrandChild, vecQuestionnaireQuestions,
                                                        objDynaActionForm, mapQuestions,otherChild);
                    }
                    else{
                        vecAllChildren.add(strQuestionNumber+delimiter+otherChild);
                    }
                }
            }
            if (checkVectorSize(vecAllChildren)){
                mapQuestions.put(incrementIntPage(getIntPageNumber()), vecAllChildren);
                mapQuestions = processTillLeafConditionally(vecAllChildren, vecQuestionnaireQuestions,
                                                        objDynaActionForm, mapQuestions,otherChild);
            }
        return mapQuestions;
    }
    public boolean isParentQuestion(String strQuestionNumber, Vector vecQuestionnaireQuestions,DynaActionForm objDynaActionForm){
        boolean isParent = false;
        int intSizeofQuestionnaire  = vecQuestionnaireQuestions.size();
        for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
            objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
            String strParentQuestion   = (String)objDynaActionForm.get("parentQuestionNumber");
            if(strQuestionNumber.equals(strParentQuestion)) {
                isParent = true;
            }
        }
        return isParent;

    }
    /**
     *
     * @param vecQuestionnaireQuestions
     * @param objDynaActionForm
     * @return
     */
    public Vector getAllChildren (Vector vecQuestionnaireQuestions,DynaActionForm objDynaActionForm,
                                  String strImmediateChild){
        Vector vecAllChildren       = new Vector();
        int intSizeofQuestionnaire  = vecQuestionnaireQuestions.size();
        String strParentQuestion    = null;
        String strQuestionNumber    = null;
        for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
                objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
                strParentQuestion   = (String)objDynaActionForm.get("parentQuestionNumber");
                if(trimValue(strParentQuestion).equals(strImmediateChild)){
                    strQuestionNumber = (String)objDynaActionForm.get("questionNumber");
                    vecAllChildren.add(strQuestionNumber);
                }
            }
        return vecAllChildren;
    }
    /**
     *
     * @param mapQuestions
     * @param vecQuestionnaireQuestions
     * @param objDynaActionForm
     * @return
     */
    public TreeMap buildQuestionnaire(TreeMap mapQuestions, Vector vecQuestionnaireQuestions,DynaActionForm objDynaActionForm){
        TreeMap tmapDynaBeans = new TreeMap();
        int intSizeofQuestionnaire  = vecQuestionnaireQuestions.size();
        String strQuestionNumber = null;
        Iterator questionIterator = mapQuestions.keySet().iterator();
        Vector vecDynaBeans;
        while(questionIterator.hasNext()) {
            vecDynaBeans = new Vector();
            Integer intPageNumber = (Integer)questionIterator.next();
            Vector vecQuestions   = (Vector)mapQuestions.get(intPageNumber);
            for(int i=0; i<vecQuestions.size();i++){
                String qNbrWithPageLevel = (String) vecQuestions.elementAt(i);
                getQNumberAndPageLevelFromToken(qNbrWithPageLevel);
                for (int begin = 0; begin < intSizeofQuestionnaire;begin++){
                    objDynaActionForm   = (DynaActionForm)vecQuestionnaireQuestions.elementAt(begin);
                    strQuestionNumber = (String)objDynaActionForm.get("questionNumber");
                    if (getQNbrFromToken().equals(strQuestionNumber)){
                        //Add Pagelevel in the DynaForm
                        objDynaActionForm.set("pageLevel",getPLevelFromToken());
                        vecDynaBeans.add(vecQuestionnaireQuestions.elementAt(begin));
                    }
                }
                tmapDynaBeans.put(intPageNumber,vecDynaBeans);
            }
        }
        // Returns an order of sorted Dynabeabs with Page number
        return tmapDynaBeans;
    }
    /**
     *
     * @param qNbrWithPageLevel
     */
    public void getQNumberAndPageLevelFromToken(String qNbrWithPageLevel){
        String strQNbr;
        String strPageLevel;
        StringTokenizer stToken = new StringTokenizer(trimValue(qNbrWithPageLevel),delimiter);
        while (stToken.hasMoreTokens()){
            strQNbr         = stToken.nextToken();
            strPageLevel    = stToken.nextToken();
            setQNbrFromToken(strQNbr);
            setPLevelFromToken(strPageLevel);
        }
    }
    /**
     *
     * @param pLevel
     */
    public void setPLevelFromToken(String pLevel){
        this.pLevel = pLevel;

    }
    /**
     *
     * @return
     */
    public String getPLevelFromToken(){
        return this.pLevel;
    }
    /**
     *
      * @param qNumber
     */
    public void setQNbrFromToken( String qNumber){
        this.qNumber = qNumber;

    }
    /**
     *
     * @return
     */
    public String getQNbrFromToken(){
        return this.qNumber;
    }
    /**
     *
     * @param rootRow
     */
    public void setRootRow (int rootRow){
        this.rootRow = rootRow;
    }
    /**
     *
     * @return
     */
    public int getRootRow (){
        return this.rootRow ;
    }
    /**
     *
     * @param strPageNumber
     */
    public void setStrPageNumber(String strPageNumber){
        this.strPageNumber = strPageNumber;
    }
    /**
     *
     */
    public void initializePage(){
        this.intPageNumber = null;
    }
    /**
     *
     * @return
     */
    public Integer getIntPageNumber(){
        if (this.intPageNumber == null){
            this.intPageNumber = new Integer(1);
        }
        return this.intPageNumber;
    }
    public void setIntPageNumber(Integer intPageNumber){
        this.intPageNumber = intPageNumber;

    }
    /**
     *
     * @return
     */
    public String getStrPageNumber(){
        if(this.strPageNumber == null){
            this.strPageNumber = "1";
        }
        return this.strPageNumber;
    }
    /**
     *
     * @param strPageNumber
     * @return
     */
    public String incrementStrPage (String strPageNumber){
        int intPage = Integer.parseInt(strPageNumber);
        String strNewPage = new Integer(intPage+1).toString();
        setStrPageNumber(strNewPage);
        return strNewPage;
    }
    /**
     *
     * @param intPage
     * @return
     */
    public Integer incrementIntPage (Integer intPage){
        Integer intPNumber = new Integer (intPage.intValue() + 1);
        setIntPageNumber(intPNumber);
        return intPNumber;
    }
    /**
     *
     * @param strInput
     * @return
     */
    public boolean isNotNull (String strInput){
        boolean flag = false;
        if (trimValue(strInput).length() > 0){
            flag = true;
        }
        return flag;
    }
    /**
     *
     * @param input
     * @return
     */
    public String trimValue(String input){
        String output = "";
        if (input != null && input.length()!= 0){
            output = input.trim();
        }
        return output;

    }
    /**
     *
      * @param inVector
     * @return
     */
    public boolean checkVectorSize(Vector inVector){
        boolean boolFlag = false;
        if(inVector != null && inVector.size() != 0){
            boolFlag = true;
        }
        return boolFlag;
    }
}
