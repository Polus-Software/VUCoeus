/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.validator.DynaValidatorForm;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;
import edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean;

/**
 *
 * @author midhunmk
 */
public class ViewFinEntityAction extends COIBaseAction  {
    private static final String questionType = "F";
    public ViewFinEntityAction() {
    }
    
    
    public org.apache.struts.action.ActionForward performExecuteCOI(
    org.apache.struts.action.ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    javax.servlet.http.HttpServletRequest request,
    javax.servlet.http.HttpServletResponse response) throws Exception {
    
        
        
   HttpSession session= request.getSession();
   WebTxnBean webTxnBean=new WebTxnBean();
   HashMap hmpFinEntData = new HashMap();
   HashMap hmFinData = new HashMap();
   hmFinData.put("questionType", questionType);
   Hashtable hashtable = null;
   DynaValidatorForm dynaForm = null;
   Vector vecFindata = null;
   PersonInfoBean personInfoBean  =(PersonInfoBean)session.getAttribute("person");
   String personId = personInfoBean.getPersonID();
   getMatrix(hmFinData,request);
   String entityNo = request.getParameter("entityNumber");     
   String sequenceNum = request.getParameter("seqNum"); 
   hmpFinEntData.put("entityNumber",entityNo);
   hmpFinEntData.put("sequenceNumber",Integer.parseInt(sequenceNum));
   Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getFinEntDetailsCoiv2",hmpFinEntData);
   Vector vecData = (Vector)htData.get("getFinDiscDetCoiv2Seq");
   Vector vecEntity=(Vector)htData.get("getFinEntDetailsCoiv2Matrix");
   
   if(vecData!=null && vecData.size()>0){
              for(int index=0 ;index < vecData.size();index++){
            dynaForm = (DynaValidatorForm)vecData.get(index);
               session.setAttribute("annDisclFinEntity",dynaForm);
       }}else{
       ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("invalidEntity",
                        new ActionMessage("error.FinEntity.invalid"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
     }
 
    if(vecEntity!=null && vecEntity.size()>0){
    session.setAttribute("entityDetails",vecEntity);
  }else{
        session.removeAttribute("entityDetails");
  }
 
    request.setAttribute("mode","review");
     request.setAttribute("removelink","true");
    
   if(vecFindata!=null && vecFindata.size() > 0){
            dynaForm = (DynaValidatorForm)vecFindata.get(0);
        }
   boolean hasRightToView = false;
        String loggedinpersonid =
        (String)session.getAttribute(LOGGEDINPERSONID);
        String strUserprivilege = (String)session.getAttribute(PRIVILEGE);
        int userprivilege = Integer.parseInt(strUserprivilege);
        if(userprivilege > 0){
            hasRightToView = true;
        }
        else if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
            hasRightToView = true;
        }
        if(!hasRightToView){
            String errorMsg = "You do not have the right to view this financial ";
            errorMsg += "entity.please contact the Office of Sponsored ";
            errorMsg += "Programs";
            throw new CoeusException(errorMsg);
        }
        Vector vecCertData = viewFinEntityCertData(hmpFinEntData, request);
        request.setAttribute("viewFinEntityData", vecFindata);
        request.setAttribute("viewFinEntityCertData", vecCertData);
    return actionMapping.findForward("success");
    }
    private Vector viewFinEntityCertData(
    HashMap hmpFinEntCertData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecQuestData = new Vector();
        Hashtable htViewFinCert =
        (Hashtable)webTxnBean.getResults(request,"viewFinEntityHistoryCertLst",hmpFinEntCertData);
        Vector vecCertData =(Vector)htViewFinCert.get("viewFinEntityHistoryCertLst");
        if(vecCertData!=null && vecCertData.size()>0){
            for(int index =0;index <vecCertData.size();index++){
                DynaValidatorForm dynaForm =
                (DynaValidatorForm)vecCertData.get(index);
                Hashtable htQuestData =
                (Hashtable)webTxnBean.getResults(request, "getQuestionLabel", dynaForm);
                String questLabel = (String)(
                (HashMap)htQuestData.get("getQuestionLabel")).get("ls_value");
                dynaForm.set("label",questLabel);
                
                vecQuestData.addElement(dynaForm);
            }
        }
        return vecQuestData;
    }
    
    
private void getMatrix(HashMap hmFinData,HttpServletRequest request)throws Exception{
HttpSession session=request.getSession();
WebTxnBean webTxn=new WebTxnBean();
Hashtable htFinData=(Hashtable)webTxn.getResults(request,"finEntDiscl",hmFinData);
Vector entityType=(Vector)htFinData.get("getOrgTypeList");
Vector rltnType=(Vector)htFinData.get("getFinEntityRelType");
Vector dtGrp=(Vector)htFinData.get("getFinEntdataGroups");
Vector dtaMtrx=(Vector)htFinData.get("getfinEntDataMatrix");
//Case#4447 - Next phase of COI enhancements - Start
Vector relationShipType = (Vector)htFinData.get("getFinEntityRelType");
FinEntMatrixBean fin=new FinEntMatrixBean();
for(int index=0;index<dtaMtrx.size();index++){
  fin=(FinEntMatrixBean)dtaMtrx.get(index);
  if(fin!=null && fin.getLookupArgument()!=null){
  String arg=fin.getLookupArgument();
  HashMap hmarg=new HashMap();
  hmarg.put("argumentName",arg);
  Hashtable htList=(Hashtable)webTxn.getResults(request,"getArgValueList",hmarg);
  Vector argList=(Vector)htList.get("getArgValueList");
  session.setAttribute(arg,argList);
}
}
     session.setAttribute("entityType",entityType);
      session.setAttribute("rltnType",rltnType);
     session.setAttribute("finEntdataGroup",dtGrp);
     session.setAttribute("finEntdataMatrix",dtaMtrx);
     //Case#4447 - Next phase of COI enhancements - Start
     session.setAttribute("finRelType",relationShipType);
     //Case#4447 - End
}
}
