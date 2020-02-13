/*
 * EditFinEntityAction.java
 *
 * Created on January 2, 2006, 9:46 PM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;


/**
 *
 * @author  vinayks
 */
public class EditFinEntityAction  extends COIBaseAction{
    
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    private static final String EMPTY_STRING ="";
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        
        WebTxnBean webTxnBean = new WebTxnBean();
        
        DynaValidatorForm dynaFinForm = (DynaValidatorForm)actionForm;
        
        String sequenceNum = EMPTY_STRING;
        
        String entityNumber = (String)request.getParameter("entityNumber");
        
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        
        String personId = personInfoBean.getPersonID();
        
        String seqNum = (String)dynaFinForm.get("sequenceNum");
        
        HashMap hmFinData = new HashMap();
        hmFinData.put("entityNumber",entityNumber);
        Hashtable htFinData = 
            (Hashtable)webTxnBean.getResults(request,"getFinDiscDet",hmFinData);
        
        Vector vecFinData = (Vector)htFinData.get("getFinDiscDet");
        
        String actionFrom = request.getParameter("actionFrom");
          if(actionFrom!=null){
            if(actionFrom.equals("editDiscl")){
                request.setAttribute("actionFrom","editDiscl");
            }else if(actionFrom.equals("addDiscl")){
                request.setAttribute("actionFrom","addDiscl");
            }else if(actionFrom.equals("annDiscCert")){
                request.setAttribute("actionFrom", "editFinEnt");
            }else if(actionFrom.equals("AnnualFE")){
                request.setAttribute("actionFrom","AnnualFE");
            }
        }
        
        //Get the FinEntity
        if(vecFinData!=null && vecFinData.size() >0){
            DynaValidatorForm formData = (DynaValidatorForm)vecFinData.get(0);
            sequenceNum = (String)formData.get("sequenceNum");  
            //Case 3838 - START
            if (sequenceNum!= null && Integer.parseInt(sequenceNum) != 1) {
                HashMap hmFinHistData = new HashMap();
                hmFinHistData.put("personId", personId);
                hmFinHistData.put("entityNumber", entityNumber);
                hmFinHistData.put("sequenceNumber", new Integer(1));
                Hashtable htFinHistData = (Hashtable) webTxnBean.getResults(request, "getFinDiscHistoryDetail", hmFinHistData);
                Vector vecFinHistData = (Vector) htFinHistData.get("getFinDiscHistoryDetail");
                if (vecFinHistData != null && vecFinHistData.size() > 0) {
                    DynaValidatorForm formHistData = (DynaValidatorForm) vecFinHistData.get(0);
                    Object obj = formHistData.get("updtimestamp");
                    Object createUser = formHistData.get("upduser");
                    request.setAttribute("createTimestamp", obj);
                    request.setAttribute("createUser", createUser);
                }
            }else {
                request.setAttribute("createTimestamp", formData.get("updtimestamp"));
                request.setAttribute("createUser", formData.get("upduser"));
            }
            //Case 3838 - END
            BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
            beanUtilsBean.copyProperties(dynaFinForm,formData);
//            dynaFinForm.set("personId",(String)formData.get("personId"));
//            dynaFinForm.set("entityNumber",(String)formData.get("entityNumber"));
//            dynaFinForm.set("sequenceNum",(String)formData.get("sequenceNum"));
//            dynaFinForm.set("statusCode",(String)formData.get("statusCode"));
//            dynaFinForm.set("entityName",(String)formData.get("entityName"));
//            dynaFinForm.set("status",(String)formData.get("status"));
//            dynaFinForm.set("statusDesc",(String)formData.get("statusDesc"));
//            dynaFinForm.set("entityName",(String)formData.get("entityName"));
//            dynaFinForm.set("entityTypeCode",(String)formData.get("entityTypeCode"));
//            dynaFinForm.set("entityType",(String)formData.get("entityType"));
//            dynaFinForm.set("shareOwnerShip",(String)formData.get("shareOwnerShip"));
//            dynaFinForm.set("entityRelTypeCode",(String)formData.get("entityRelTypeCode"));
//            dynaFinForm.set("relationShipTypeDesc",(String)formData.get("relationShipTypeDesc"));
//            dynaFinForm.set("relationShipDesc",(String)formData.get("relationShipDesc"));
//            dynaFinForm.set("relatedToOrgFlag",(String)formData.get("relatedToOrgFlag"));
//            dynaFinForm.set("orgRelnDesc",(String)formData.get("orgRelnDesc"));
//            dynaFinForm.set("sponsorCode",(String)formData.get("sponsorCode"));
//            dynaFinForm.set("sponsorName",(String)formData.get("sponsorName"));
//            dynaFinForm.set("updtimestamp",(String)formData.get("updtimestamp"));
//            dynaFinForm.set("upduser",(String)formData.get("upduser"));
            dynaFinForm.set("acType","U");
            dynaFinForm.set("actionFrom",actionFrom);
            session.setAttribute("finEntityForm",dynaFinForm);
        }
        
        
        String loggedinpersonid =
                            (String)session.getAttribute(LOGGEDINPERSONID);                          
        boolean hasRightToEdit = false;
        String strUserprivilege = (String)session.getAttribute("userprivilege");
        int userprivilege = Integer.parseInt(strUserprivilege);
        if(userprivilege > 1){
            hasRightToEdit = true;
        }
        else if(dynaFinForm.get("personId").toString().equals(loggedinpersonid)){
            hasRightToEdit = true;
        }
        if(!hasRightToEdit){
            String errorMsg = "You do not have rights to edit this ";
            errorMsg += "financial entity.  If you believe you are ";
            errorMsg += "seeing this message in error, please contact ";
            errorMsg += "the Office of Sponsored Programs.";
            throw new CoeusException(errorMsg);
        }
        
        
        
        int seqNo = Integer.parseInt(sequenceNum);
        HashMap hmFinCertData = new HashMap();
        hmFinCertData.put("entityNumber",entityNumber);
        hmFinCertData.put("sequenceNum",new Integer(seqNo));
        hmFinCertData.put("personId",personId);        
        /*This is a single transaction "reviewFinEntity" is a transaction id*/
        
        Hashtable htFinEntData =
            (Hashtable)webTxnBean.getResults(request,"reviewFinEntity",hmFinCertData);
        Vector vecCertData = (Vector)htFinEntData.get("viewFinEntityCertLst");
        HashMap hmQuestion = new HashMap();
        
        Vector questionData = new Vector();
        String questionId = EMPTY_STRING;       
        if(vecCertData!= null && vecCertData.size()> 0){
            for(int index = 0 ; index < vecCertData.size(); index++){
                DynaValidatorForm formData  = (DynaValidatorForm)vecCertData.get(index);               
                questionId = (String)formData.get("questionId");
                hmQuestion.put("questionId",questionId);
                Hashtable htLableData =
                        (Hashtable)webTxnBean.getResults(request,"getQuestionLabel",hmQuestion);
                /** Prepare questionLabelBean  with Label and Description and then add
                 *to the vector Object and then set to the request.
                 */                
                String labelId = (String)((HashMap)htLableData.get("getQuestionLabel")).get("ls_value");
                  formData.set("label",labelId);  
                  questionData.addElement(formData);
            }
        }       
      
        session.setAttribute("orgtypelist",htFinEntData.get("getOrgTypeList"));
        session.setAttribute("finentityreltype",htFinEntData.get("getFinEntityRelType"));
        session.setAttribute("ynqList",questionData);
        //session.setAttribute("questionData",questionData);
        saveToken(request);
        return actionMapping.findForward("success");
    }
    
}
