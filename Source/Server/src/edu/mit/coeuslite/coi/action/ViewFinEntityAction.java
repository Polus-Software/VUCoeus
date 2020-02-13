/*
 * viewFinEntityCOIAction.java
 *
 * Created on 27 December 2005, 20:28
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.CoeusException;
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

/**
 *
 * @author  mohann
 * To get the details(History, Current and Certificates) of a Financial Entity.
 */
public class ViewFinEntityAction extends COIBaseAction  {
    
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest req;
    //private HttpServletResponse res;
    //private ActionMapping mapping;
    private static final String EMPTY_STRING = "";
    // default question type is F to get the YNQList data
    private static final String questionType = "F";
    
    /** Creates a new instance of viewFinEntityCOIAction */
    public ViewFinEntityAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public org.apache.struts.action.ActionForward performExecuteCOI(
    org.apache.struts.action.ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    javax.servlet.http.HttpServletRequest request,
    javax.servlet.http.HttpServletResponse response) throws Exception {
        
        //this.req = request;
        //this.res = response;
        //this.mapping = actionMapping;
        DynaValidatorForm dynaValidatorForm =
        (DynaValidatorForm)actionForm;
        
        HttpSession session= request.getSession();
        
        WebTxnBean webTxnBean=new WebTxnBean();
        
        HashMap hmpFinEntData = new HashMap();
        HashMap hmpFinEntCertData = new HashMap();
        HashMap hmFinData = new HashMap();
        
        hmFinData.put("questionType", questionType);
        
        PersonInfoBean personInfoBean  =
        (PersonInfoBean)session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        
        String entityNo = request.getParameter("entityNumber");
        if(entityNo!=null || !entityNo.equals("")){
            request.setAttribute("entityNumber",entityNo);
        }
        hmpFinEntData.put("entityNumber",entityNo);
        
        String header = request.getParameter("header");
        
        Hashtable hashtable = null;
        DynaValidatorForm dynaForm = null;
        Vector vecFindata = null;
         
        String sequenceNum = request.getParameter("seqNum");
        if(sequenceNum == null || sequenceNum.trim().equals("")) {
        // Get the sequence number
            hashtable = viewFinEntityCOIData(hmpFinEntData, request);
            vecFindata  = (Vector)hashtable.get("getFinDiscDet");
        }else {
            hmpFinEntData.put("sequenceNumber",new Integer(sequenceNum));
            hmpFinEntData.put("personId",personId);
            hashtable = viewFinEntityHistoryData(hmpFinEntData, request);
            vecFindata  = (Vector)hashtable.get("getFinDiscHistoryDetail");
        }

        if(vecFindata!=null && vecFindata.size() > 0){
            dynaForm = (DynaValidatorForm)vecFindata.get(0);
        }
        String sequenceNo = (String)dynaForm.get("sequenceNum");
        int seqNo=0;
        if(sequenceNo !=null){
            seqNo = Integer.parseInt(sequenceNo);
        }
        
        boolean hasRightToView = false;
        String loggedinpersonid =
        (String)session.getAttribute(LOGGEDINPERSONID);
        String strUserprivilege = (String)session.getAttribute("userprivilege");
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
        
        hmpFinEntCertData.put("entityNumber",entityNo);
        hmpFinEntCertData.put("sequenceNum",new Integer(seqNo));
        hmpFinEntCertData.put("personId",personId);
        
        if(actionMapping.getPath().equals("/viewFinEntityDisplay")){
            request.setAttribute("viewFinEntityDisplay","viewFinEntityDisplay");
        }
        Vector vecCertData = viewFinEntityCertData(hmFinData,hmpFinEntCertData, request);
        //request.setAttribute("viewFinEntityData", hashtable.get("getFinDiscDet"));
        request.setAttribute("viewFinEntityData", vecFindata);
        request.setAttribute("viewFinEntityCertData", vecCertData);
        
        if(header != null && (header.equalsIgnoreCase("no") || header.equalsIgnoreCase("n"))) {
            return actionMapping.findForward("successNoHeader");
        }
        return actionMapping.findForward("success");
    }
    
    /*
     * The method used to get all the details of a Financial Entity.
     */
    private Hashtable viewFinEntityCOIData(HashMap hmpFinEntData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htviewFinEntity =
        (Hashtable)webTxnBean.getResults(request,"getFinDiscDet",hmpFinEntData);
        return htviewFinEntity;
    }
    
    /*
     * The method used to get all the details of a Financial Entity for a Sequence Number.
     */
    private Hashtable viewFinEntityHistoryData(HashMap hmpFinEntData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htviewFinEntity = (Hashtable)webTxnBean.getResults(request,"getFinDiscHistoryDetail",hmpFinEntData);
        return htviewFinEntity;
    }
    
    /*
     * The method used to get all Financial Entity certificate details for a disclosure.
     */
    private Vector viewFinEntityCertData(HashMap hmFinData,
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
