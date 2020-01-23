/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

/**
 *
 * @author ajay
 */


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
import edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



public class ViewFinEntityMitAction extends COIBaseAction  {
    private static final String questionType = "F";
    public ViewFinEntityMitAction() {
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
   Hashtable hashtable = null;
   DynaValidatorForm dynaForm = null;
   Vector vecFindata = null;
   PersonInfoBean personInfoBean  =(PersonInfoBean)session.getAttribute("person");
   String personId = personInfoBean.getPersonID();
   String entityNo = request.getParameter("entityNumber");     
   String sequenceNum = request.getParameter("seqNum");
   getMatrix(hmFinData,request);
   getFinEntityDetail(hmFinData,dynaForm,request,actionMapping);
   hmFinData.put("questionType", questionType);
   hmpFinEntData.put("entityNumber",entityNo);
   hmpFinEntData.put("sequenceNumber",Integer.parseInt(sequenceNum));
   hmpFinEntData.put("personId",personId);
   hashtable = (Hashtable)webTxnBean.getResults(request,"getFinDiscDetCoiv2Seq",hmpFinEntData);
   vecFindata  = (Vector)hashtable.get("getFinDiscDetCoiv2Seq");
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
     private void getMatrix(HashMap hmFinData,HttpServletRequest request)throws Exception{
HttpSession session=request.getSession();
WebTxnBean webTxn=new WebTxnBean();
Hashtable htFinData=(Hashtable)webTxn.getResults(request,"finEntDiscl",hmFinData);
//Vector entityType=(Vector)htFinData.get("getOrgTypeList");
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
     //session.setAttribute("entityType",entityType);
      session.setAttribute("rltnType",rltnType);
     session.setAttribute("finEntdataGroup",dtGrp);
     session.setAttribute("finEntdataMatrix",dtaMtrx);
     //Case#4447 - Next phase of COI enhancements - Start
     session.setAttribute("finRelType",relationShipType);
     //Case#4447 - End
}
private ActionForward getFinEntityDetail(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
     String entityNumber=request.getParameter("entityNumber").trim();
     int entitySeqNumber=0;
     if(request.getParameter("seqNum")!= null){
      entitySeqNumber= Integer.parseInt(request.getParameter("seqNum").trim());
     }
     HashMap hmDetails=new HashMap();
     hmDetails.put("entityNumber",entityNumber);
     hmDetails.put("seqNumber",entitySeqNumber);
     HttpSession session=request.getSession();
     WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"finEntDisclDetailsHistCoiv2",hmDetails);
    Vector vecData = (Vector)htData.get("getPerFinEntDetailsHistCoiv2");
    Vector vecEntity=(Vector)htData.get("getFinDiscDetHistCoiv2");
    DynaValidatorForm dynaForm=new DynaValidatorForm();
    if(vecEntity!=null && vecEntity.size()>0){
              for(int index=0 ;index < vecEntity.size();index++){
            dynaForm = (DynaValidatorForm)vecEntity.get(index);
               session.setAttribute("annDisclFinEntity",dynaForm);
       }}else{
       ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("invalidEntity",
                        new ActionMessage("error.FinEntity.invalid"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
     } if(vecData!=null && vecData.size()>0){
    session.setAttribute("entityDetails",vecData);
  }else{
        session.removeAttribute("entityDetails");
  }
    
    if(request.getParameter("mode")!=null){
        request.setAttribute("mode",request.getParameter("mode"));
    }
        request.setAttribute("mode","review"); 
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

}
