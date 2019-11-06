/*
 * QuestionAction.java
 *
 * Created on February 20, 2006, 2:45 PM
 */

package edu.mit.coeuslite.coi.action;


import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author vinayks
 */
public class QuestionAction extends COIBaseAction{
    //private HttpServletRequest request;
    private static final String NO_EXPLANATION = "No Explanation";
    
    /** Creates a new instance of QuestionAction */
    public QuestionAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //this.request = request;
        HttpSession session = request.getSession();
        String questionID = null;
        String questionDesc = NO_EXPLANATION;
        String questionExplanation = NO_EXPLANATION;
        String questionPolicy = NO_EXPLANATION;
        String questionRegulation = NO_EXPLANATION;
        String questionNo = request.getParameter("questionNo");
        Vector vecQuestionDetails = getQuestionDetails(questionNo, request);
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        Vector vesQuestions = new Vector();
        if(vecQuestionDetails!= null && vecQuestionDetails.size() > 0){
            for(int index = 0; index< vecQuestionDetails.size() ; index++){
                DynaValidatorForm dynaServerdata = (DynaValidatorForm)vecQuestionDetails.get(index);
                questionDesc = (String)dynaServerdata.get("description");
                dynaForm.set("description",questionDesc);
                dynaForm.set("questionId",dynaServerdata.get("questionId"));
                dynaForm.set("explanation",dynaServerdata.get("explanation"));
                
                String explanationType =(String) dynaServerdata.get("explanationType");
                // get Question policy
                if( explanationType != null && explanationType.trim().equalsIgnoreCase( "P" ) ) {
                    questionPolicy = (String) dynaServerdata.get("explanation");
                }
                //get Question Regulation
                if( explanationType != null && explanationType.trim().equalsIgnoreCase( "R" ) ) {
                    questionRegulation = (String) dynaServerdata.get("explanation");
                }
                //get Question Explanation
                if( explanationType != null && explanationType.equalsIgnoreCase( "E" ) ) {
                    questionExplanation = (String) dynaServerdata.get("explanation");
                }
                
            }
            vesQuestions.addElement(dynaForm);
            request.setAttribute("questionExplanation",questionExplanation);
            request.setAttribute("questionPolicy",questionPolicy);
            request.setAttribute("questionRegulation",questionRegulation);
            request.setAttribute( "questionDetails" , vesQuestions);
            
        }
        return actionMapping.findForward("success");
    }
    
    /*This method gets the Question Details for particular
     * @param questionNumber
     * @throws Exception
     * @return Vector vecQuestDetails containing QuestionDetailsBean
     */
    private Vector getQuestionDetails(String questionNumber, HttpServletRequest request)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("questionId", questionNumber);
        Hashtable htQuestionDetails =
        (Hashtable)webTxnBean.getResults(request, "getQuestionDetails", hmData);
        Vector vecQuestDetails = (Vector)htQuestionDetails.get("getQuestionDetails");
        return vecQuestDetails;
    }
    
}//End of Action class
