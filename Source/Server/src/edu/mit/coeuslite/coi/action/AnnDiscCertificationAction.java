/*
 * AnnDiscCertificationAction.java
 *
 * Created on 02 March 2006, 11:13
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessages;

/**
  * @author  mohann
 */
/**
 * This Action Class is used to collect and validate user answers to certification questions for any
 * user disclosed financial entities.
 **/
public class AnnDiscCertificationAction extends COIBaseAction {
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest request;
    //private HttpServletResponse response;
    //private ActionMapping mapping;
    
    /** Creates a new instance of AnnDiscCertificationAction */
    public AnnDiscCertificationAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
                
        //this.request = request;        
        HttpSession session = request.getSession();
        String personId = EMPTY_STRING;
        DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
        String pageName = "";
        PersonInfoBean personInfoBean  = 
            (PersonInfoBean)session.getAttribute("person");
        
        if(personInfoBean!=null){
           personId = personInfoBean.getPersonID(); 
        }
        String validateRequired = request.getParameter("validate");
        Vector vecQuestData = getAnnCertDetails(dynaValidatorForm,personId, request);
        session.setAttribute("ynqList",vecQuestData);
        ActionMessages actionMessages = new ActionMessages();
        if(validateRequired!=null && validateRequired.equals("false")){
            
        }else{
            actionMessages = validateAnnualDisclosure(request,actionMapping,dynaValidatorForm);  
        }
        pageName = "success";
        if(!actionMessages.isEmpty()){
            saveMessages(request, actionMessages); 
//            return actionMapping.findForward("exception");
            pageName = "exception";
        }else{
             session.removeAttribute("certQuestionErrors"); 
        }
        return actionMapping.findForward(pageName);
    }
    
    private Vector getAnnCertDetails(DynaValidatorForm dynaForm,
        String personId , HttpServletRequest request) throws Exception {
        
        HashMap hmCertData = new HashMap();
        boolean hasActiveEntities = false;
        hmCertData.put("personId",personId);
        WebTxnBean webTxnBean = new  WebTxnBean();
        
        //get all COI Disclosure Certificate details
        Hashtable htCertData =
        (Hashtable)webTxnBean.getResults(request, "getAnnDiscDetails", hmCertData);
        Vector vecCertData = (Vector)htCertData.get("getAnnDiscCertQuestions");
        
        HashMap hmActiveFE = (HashMap)htCertData.get("checkPersonHasActiveFE");
        int activeFE = Integer.parseInt(hmActiveFE.get("RetVal").toString());
        if(activeFE !=1 ){
           hasActiveEntities = false ; 
        }else{
           hasActiveEntities = true;
        }
        dynaForm.set("hasActiveEntities",new Boolean(hasActiveEntities));        
        
        Vector vecQuestData = new Vector();
        if(vecCertData!=null && vecCertData.size()>0){
            for(int index =0;index<vecCertData.size();index++){
                DynaValidatorForm form =(DynaValidatorForm)vecCertData.get(index);
                
                Hashtable htCorrespEntData =
                (Hashtable)webTxnBean.getResults(request,"getCorrespEntQuestId",form);
                String entQuestId =
                (String)((HashMap)htCorrespEntData.get("getCorrespEntQuestId")).get("correspEntQuestId");
                //certBean.setCorrespEntQuestId(entQuestId);
                form.set("correspEntQuestId",entQuestId);
                Hashtable htLabelData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String entQuestLabel = (String)((HashMap)htLabelData.get("getQuestionLabel")).get("ls_value");
                // certBean.setCorrespEntQuestLabel(entQuestLabel);
                form.set("correspEntQuestLabel",entQuestLabel);
                Hashtable htQuestData =
                (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",form);
                String label = (String)((HashMap)htQuestData.get("getQuestionLabel")).get("ls_value");
                //certBean.setLabel(label);
                form.set("label",label);
                vecQuestData.addElement(form);
            }
        }
        return vecQuestData;
    }
    
}
