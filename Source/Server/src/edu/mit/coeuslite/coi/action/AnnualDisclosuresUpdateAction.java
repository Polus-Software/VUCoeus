/*
 * AnnualDisclosuresUpdateAction.java
 *
 * Created on 06 March 2006, 15:16
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 * This Action class is to update the reviewed pending AnnualDisclosures.
 *
 * @author  mohann
 */
public class AnnualDisclosuresUpdateAction extends COIBaseAction {
    //private WebTxnBean webTxnBean;
    //private HttpServletRequest request;
    public static final String TO_CONFIRMATION_PAGE = "annDisclConfirmation";
    public static final String CHECK_UPDATE_FAILURE = "checkUpdateFailure";
    
    /** Creates a new instance of AnnualDisclosuresUpdateAction */
    public AnnualDisclosuresUpdateAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        //this.request = request;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        PersonInfoBean personInfoBean =
        (PersonInfoBean)session.getAttribute("person");
        String personId = EMPTY_STRING;
        String entityNumber = EMPTY_STRING;
        
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
        }
        ActionForward actionforward = actionMapping.findForward("success");
        entityNumber = (String)request.getParameter("entityNumber");
        if(entityNumber == null || entityNumber.equals(EMPTY_STRING)){
            entityNumber = (String)request.getAttribute("entityNumber");
        }
        //Added for case:3838
        String entityOrgRln=EMPTY_STRING;
        entityOrgRln=request.getParameter("entityOrgRln");
        //ends 3838
        Vector vecAnnualDisclosures = getPendingDisclosures(entityNumber,personId, request);
        
        
        if(vecAnnualDisclosures !=null) {
            
            if(validateDisclosure(vecAnnualDisclosures, request)) {
                 //updFinEntity(request,entityNumber);
                updAnnualDisclosure(vecAnnualDisclosures, request);
                updFinEntity(request,entityNumber);
                //Added for Case:
               
                //ends Case
            }else {
                //actionforward = actionMapping.findForward("exception");
                //Case 3838
                request.setAttribute("entityOrgRln", entityOrgRln);
                return actionMapping.findForward("exception");
            }
        }
        
        //Get finEntities from session in order to improve performance.
        Vector vecFinEntities = (Vector)session.getAttribute("allAnnualDiscEntities");
        String nextEntityNumber = null;
        int nextEntIndex = 0;
        //First, check whether there are entities that have not been updated.
        boolean allOtherEntUpdated = true;
        for(int checkAll=0; checkAll<vecFinEntities.size()-1; checkAll++){
            DynaValidatorForm tempEntityDetails =
            (DynaValidatorForm)vecFinEntities.get(checkAll);
            if(tempEntityDetails.get("updated") != null &&
            tempEntityDetails.get("updated").equals("N")){
                allOtherEntUpdated = false;
                break;
            }
        }
        
        //Get the index of the next entity in the collection.
        for(int lookForEnt = 0; lookForEnt < vecFinEntities.size(); lookForEnt++){
            DynaValidatorForm tempEntityDetails =
            (DynaValidatorForm)vecFinEntities.get(lookForEnt);
            if(tempEntityDetails.get("entityNumber").equals(entityNumber)){
                nextEntIndex = lookForEnt + 1;
            }
        }
        
        //If this is the last entity in the collection...
        if(nextEntIndex == vecFinEntities.size()){
            if(!allOtherEntUpdated){
                //loop from beginning to find the next entity to update.
                DynaValidatorForm nextFinEntityToUpdate = null;
                for(int entCount=0; entCount<vecFinEntities.size(); entCount++){
                    nextFinEntityToUpdate =
                    (DynaValidatorForm)vecFinEntities.get(entCount);
                    if(nextFinEntityToUpdate.get("updated").equals("N")){
                        nextEntityNumber = (String)nextFinEntityToUpdate.get("entityNumber");
                        break;
                    }
                }
            }else{
                //If there are no more fin entities in the list, check if
                //user is ready to submit annual disclosures.  If yes,
                //forward to confirmation page. Else, display main page
                //with errors.
                
                String annDiscComplete = checkAnnualDisclComplete(personId, request);
                if(annDiscComplete!=null || !annDiscComplete.equals(EMPTY_STRING)){
                    if(annDiscComplete.equals("1")){
                        actionforward = actionMapping.findForward( TO_CONFIRMATION_PAGE );
                    }else if(annDiscComplete.equals("0")){
                        actionforward = actionMapping.findForward( CHECK_UPDATE_FAILURE );
                    }else{
                        actionforward = actionMapping.findForward( CHECK_UPDATE_FAILURE );
                    }
                }
            }
        }
        //this is not the last entity in the collection.  Go to the next
        //entity in the collection.
        else{
            DynaValidatorForm nextFinEntity =
            (DynaValidatorForm)vecFinEntities.get(nextEntIndex);
            nextEntityNumber =(String) nextFinEntity.get("entityNumber");
        }
        request.setAttribute("nextEntityNumber", nextEntityNumber);
        return actionforward;
    }
    /**
     * The method used to get all pending disclosures that attached with
     * a particular FinancialEntity.The method will return a vector
     * of getDisclsoreForPerson.
     */
    
    private Vector getPendingDisclosures(String entityNumber, String personId, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId",personId);
        hmData.put("entityNumber",entityNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPendingData =
        (Hashtable)webTxnBean.getResults(request, "getDisclsoreForPerson", hmData);
        Vector vecPendingData =
        (Vector)htPendingData.get("getDisclsoreForPerson");
        return vecPendingData;
    }
    /**
     *  The method used to update the conflict status of pendidng annual disclosure
     *  for a particular Financial Entity.
     */
    private void updAnnualDisclosure(Vector vecAnnualData, HttpServletRequest request) throws Exception{
        
        if(vecAnnualData != null){
            for(int cnt=0; cnt<vecAnnualData.size(); cnt++){
                DynaValidatorForm formData =
                (DynaValidatorForm)vecAnnualData.get(cnt);
                //                String updatedConflictStatus =
                //                request.getParameter("disclConflictStatus"+cnt);
                String conflictStatusCode =
                request.getParameter(("disclConflictStatus"+cnt));
                String statusCode =
                (String)formData.get("coiStatusCode");
                conflictStatusCode =
                (conflictStatusCode == null ) ? EMPTY_STRING : conflictStatusCode.trim();
                statusCode = (statusCode == null ) ? EMPTY_STRING : statusCode.trim();
                
                if(!conflictStatusCode.equals(statusCode)){
                    formData.set("coiStatusCode",conflictStatusCode);
                }
                String reviewCode =(String)formData.get("coiReviewerCode");
                formData.set("coiReviewerCode","1");
                formData.set("acType","I");
                
                String relDesc = request.getParameter("relationShipDesc");
                formData.set("relationShipDesc", relDesc);
                
                int seqNumber = Integer.parseInt((String)formData.get("sequenceNum"))+1;
                formData.set("sequenceNum",String.valueOf(seqNumber));
                WebTxnBean webTxnBean = new WebTxnBean();
                webTxnBean.getResults(request, "updInvCOIDiscDetails", formData);
            }
        }
        
    }
    /**
     *  The method used to check whether a person's annual disclosures are
     *  ready to be updated before the user is shown the page with the submit
     *  annual disclosures button.
     */
    private String checkAnnualDisclComplete(String personId, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAnnDiscComplete =
        (Hashtable)webTxnBean.getResults(request, "checkAnnDisclComplete", hmData);
        HashMap hmAnnDiscComplete =
        (HashMap)htAnnDiscComplete.get("checkAnnDisclComplete");
        String annDisclComplete = (String)hmAnnDiscComplete.get("entityNumber");
        return  annDisclComplete;
    }
    /**
     * This method is used to validate the conflict status
     */
    private boolean validateDisclosure(Vector vecAnnualData, HttpServletRequest request)throws Exception{
        boolean isValidate = true;
        ActionMessages actionMessages = new ActionMessages();
        if(vecAnnualData != null){
            for(int cnt=0; cnt<vecAnnualData.size(); cnt++){
                DynaValidatorForm formData =
                (DynaValidatorForm)vecAnnualData.get(cnt);
                String conflictStatusCode =
                request.getParameter(("disclConflictStatus"+cnt));
                if( conflictStatusCode.equals("301") || conflictStatusCode.equals("200") ) {
                    isValidate = true;
                }else{
                    actionMessages.add("invalidConflictStatus", new ActionMessage("error.annualDisclosure.invalidConflictStatus"));
                    saveMessages(request, actionMessages);
                    isValidate = false;
                    break;
                }
            }
        }
        return isValidate;
    }
    private void updFinEntity(HttpServletRequest request,String entityNumber)throws Exception{
      String entityOrgRln=EMPTY_STRING;
        entityOrgRln=request.getParameter("entityOrgRln");   
        HashMap hmData=new HashMap();
        hmData.put("entityNumber",entityNumber);
        WebTxnBean webtxn=new WebTxnBean();
        boolean reqUpd=false;
        Hashtable htFinEntity=(Hashtable)webtxn.getResults(request,"getFinDiscDet",hmData);
        Vector vecEntity=(Vector)htFinEntity.get("getFinDiscDet");
        Timestamp dbTimestamp=prepareTimeStamp();
        for(int i=0;i<vecEntity.size();i++){
            DynaValidatorForm finEntity=(DynaValidatorForm)vecEntity.get(i);
            String rln=(String)finEntity.get("orgRelnDesc");
            if(rln!=null){
            if(!(rln.trim().equals(entityOrgRln.trim()))){
                reqUpd=true;
            } 
            }else if(rln==null &&(entityOrgRln.trim()!=null) ){
                reqUpd=true;
            }
        if(reqUpd){
            finEntity.set("orgRelnDesc",entityOrgRln);
            int seqNum=Integer.parseInt((String)finEntity.get("sequenceNum"));
            seqNum=seqNum+1;
            finEntity.set("sequenceNum",Integer.toString(seqNum));
            finEntity.set("acType","I");
            finEntity.set("updtimestamp",dbTimestamp.toString());
            webtxn.getResults(request,"updFinancialEntity",finEntity);
        }
      }
    }
    public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
}
