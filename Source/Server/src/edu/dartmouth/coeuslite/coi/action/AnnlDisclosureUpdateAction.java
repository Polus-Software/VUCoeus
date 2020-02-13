/*
 * @(#)AnnDisclosureAction.java 1.0 March 06, 2009, 3:26 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.dartmouth.coeuslite.coi.action;

import edu.dartmouth.coeuslite.coi.beans.DisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
public class AnnlDisclosureUpdateAction extends COIBaseAction {
    private static String personId = EMPTY_STRING;
    private static String userName = EMPTY_STRING;
    private static final int COI_ADMIN_CODE = 2;
    private String entityNumber = EMPTY_STRING;
    private Integer entitySeqNumber = null;
    private static final String NOT_UPDATED = "N";
    private static final String AC_TYPE = "acType";
    private static final String ENTITY_NUMBER = "entityNumber";
    private static final String MODULE_ITEM_KEY_NOT_PRESENT = "0";
    /**
     * Creates a new instance of AnnlDisclosureUpdateAction
     */
    public AnnlDisclosureUpdateAction() {
    }
    
     /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br>The method update the entity project status by updating 
     * <br>through updAnnualDisclosure method and move to the next active entity
     * <br><b>Validation</b>
     * <li> It will check whether the entity has projects using getEntityProjects method
     * @param mapping The ActionMapping used to select this instance
     * @actionForm The optional ActionForm bean for this request (if any)
     * @request The HTTP request we are processing
     * @response The HTTP response we are creating
     *
     * @throws java.io.IOException if an input/output error occurs
     * @throws javax.servlet.ServletException if a servlet exception occurs.
     */
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean =
                (PersonInfoBean)session.getAttribute("person");
        
        
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
            userName = personInfoBean.getUserName();
        }

        entityNumber = (String)request.getParameter(ENTITY_NUMBER);
        
        entitySeqNumber = (Integer)session.getAttribute("entitySequenceNumber");
        
        Vector vecEntityProjects =(Vector)session.getAttribute("allEntityPro");
        session.removeAttribute("allEntityPro");
        if(vecEntityProjects == null){
            vecEntityProjects = getEntityProjects(entityNumber,personId, request);
        }
        if(vecEntityProjects !=null) {
            if(validateDisclosure(vecEntityProjects, request)) {
                updAnnualDisclosure(vecEntityProjects, request);
            }else {
                return actionMapping.findForward("annualDisclosure");
            }
        }
        //Get finEntities from session in order to improve performance.
        Vector vecFinEntities = (Vector)session.getAttribute("allAnnualDiscEntities");
        String nextEntityNumber = null;
        int nextEntIndex = 0;
        //First, check whether there are entities that have not been updated.
        boolean allOtherEntUpdated = true;
        if(vecFinEntities  !=null && vecFinEntities.size() > 0){
            for(int checkAll=0; checkAll<vecFinEntities.size()-1; checkAll++){
                DynaValidatorForm tempEntityDetails =
                        (DynaValidatorForm)vecFinEntities.get(checkAll);
                if(tempEntityDetails.get("updated") != null &&
                        NOT_UPDATED.equals(tempEntityDetails.get("updated"))){
                    allOtherEntUpdated = false;
                    break;
                }
            }
            
            //Get the index of the next entity in the collection.
            for(int lookForEnt = 0; lookForEnt < vecFinEntities.size(); lookForEnt++){
                DynaValidatorForm tempEntityDetails =
                        (DynaValidatorForm)vecFinEntities.get(lookForEnt);
                if(tempEntityDetails.get(ENTITY_NUMBER).equals(entityNumber)){
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
                        if(nextFinEntityToUpdate != null && NOT_UPDATED.equals(nextFinEntityToUpdate.get("updated"))){
                            nextEntityNumber = (String)nextFinEntityToUpdate.get(ENTITY_NUMBER);
                            break;
                        }
                    }
                }else{
                    //If there are no more fin entities in the list, check if
                    //user is ready to submit annual disclosures.  If yes,
                    //forward to confirmation page. Else, display main page
                    //with errors.
                    return actionMapping.findForward("getCertified");
                }
            }
            //this is not the last entity in the collection.  Go to the next
            //entity in the collection.
            else{
                DynaValidatorForm nextFinEntity =
                        (DynaValidatorForm)vecFinEntities.get(nextEntIndex);
                nextEntityNumber =(String) nextFinEntity.get(ENTITY_NUMBER);
            }
        }
        request.setAttribute("nextEntityNumber", nextEntityNumber);
        return actionMapping.findForward("annualDisclosure");
    }
    
    /**
     * The method is used to get all the projects related to the disclosure financial entity
     */
    private Vector getEntityProjects(String entityNumber, String personId, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        DisclosureBean disclosureBean = (DisclosureBean)request.getSession().getAttribute("CurrDisclPer");
        String coiDisclosureNumber = disclosureBean.getCoiDisclosureNumber();
        Integer sequenceNumber =  disclosureBean.getSequenceNumber();
        hmData.put("coiDisclosureNumber",coiDisclosureNumber);
        hmData.put("sequenceNumber",sequenceNumber);
        hmData.put(ENTITY_NUMBER,entityNumber);
        hmData.put("personId",personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPendingData =
                (Hashtable)webTxnBean.getResults(request, "getCoiPersonFnProjects", hmData);
        Vector vecPendingData =
                (Vector)htPendingData.get("getCoiPersonFnProjects");
        return vecPendingData;
    }
    
    /**
     *  The method used to update the conflict status of pendidng annual disclosure
     *  for a particular Financial Entity.
     */
    private void updAnnualDisclosure(Vector vecAnnualData, HttpServletRequest request) throws Exception{
        
        if(vecAnnualData != null){
            int entSeqNumberInsert =0;
            for(int cnt=0; cnt<vecAnnualData.size(); cnt++){
                DynaValidatorForm formData =
                        (DynaValidatorForm)vecAnnualData.get(cnt);
                String conflictStatusCode =
                        request.getParameter(("disclConflictStatus"+cnt));
                Integer statusCode =
                        (Integer)formData.get("coiStatusCode");
                conflictStatusCode =
                        (conflictStatusCode == null ) ? EMPTY_STRING : conflictStatusCode.trim();
                statusCode = (statusCode == null ) ? new Integer(0) : statusCode;
                if(!conflictStatusCode.equals(statusCode.toString())){
                    formData.set("coiStatusCode",new Integer(conflictStatusCode));
                }
                DisclosureBean disclosureBean = (DisclosureBean)request.getSession().getAttribute("CurrDisclPer");
                if(disclosureBean != null){
                    String coiDisclosureNumber = disclosureBean.getCoiDisclosureNumber();
                    Integer sequenceNumber =  disclosureBean.getSequenceNumber();
                    formData.set("coiDisclosureNumber",coiDisclosureNumber);
                    formData.set("sequenceNumber",sequenceNumber);
                }
                formData.set("updateUser",userName);
                WebTxnBean webTxnBean = new WebTxnBean();
                
                if(formData.get(ENTITY_NUMBER) != null){
                    formData.set(AC_TYPE,"U");
                }else{
                    formData.set(ENTITY_NUMBER,entityNumber);
                    Hashtable moduleItemCheck = (Hashtable)webTxnBean.getResults(request, "checkModuleItem", formData);
                    HashMap hmModuleItemCheck = (HashMap)moduleItemCheck.get("checkModuleItem");
                    String isModuleItemAvailable = (String)hmModuleItemCheck.get("ll_count");
                    Timestamp dbTimestamp=prepareTimeStamp();
                    formData.set("updtimestamp",dbTimestamp);
                    if(MODULE_ITEM_KEY_NOT_PRESENT.equals(isModuleItemAvailable)){
                        formData.set(AC_TYPE,"I");
                        webTxnBean.getResults(request, "updPersonDisclProjects", formData);
                    }
                    formData.set(AC_TYPE,"I");
                        if(entSeqNumberInsert == 0){
                        entSeqNumberInsert = entitySeqNumber.intValue()+1;
                        String entSeqNumber = String.valueOf(entSeqNumberInsert);
                        entitySeqNumber = Integer.valueOf(entSeqNumber);
                        updFinEntity(request,entityNumber);
                    }
                    
                }
                formData.set("entitySeqNumber",entitySeqNumber);                
                formData.set("coiReviewerCode",new Integer(COI_ADMIN_CODE));
                webTxnBean.getResults(request, "updateCoiPersonEntProject", formData);
            }
        }
        
    }
    
    /**
     * This method is used to validate the conflict status and relationship for the projects
     */
    private boolean validateDisclosure(Vector vecAnnualData, HttpServletRequest request)throws Exception{
        boolean statusValidate = true;
        boolean relValidate = true;
        boolean isValidate = true;
        ActionMessages actionMessages = new ActionMessages();
        Vector vecNewAnnualData = new Vector();
        if(vecAnnualData != null){
            for(int index=0; index<vecAnnualData.size(); index++){
                DynaValidatorForm formData =
                        (DynaValidatorForm)vecAnnualData.get(index);
                String conflictStatusCode = request.getParameter(("disclConflictStatus"+index));
                if(conflictStatusCode != null){
                    try{
                        formData.set("coiStatusCode",Integer.valueOf(conflictStatusCode));
                    }catch(NumberFormatException numExce){
                        conflictStatusCode = "0";
                    }
                }
                String  newRelationShip  = request.getParameter(("relComments"+index));
                String oldRelationShip = (String)formData.get("relationship");
                if((EMPTY_STRING.equals(newRelationShip) && (oldRelationShip == null || EMPTY_STRING.equals(oldRelationShip)))){
                    formData.set("relationShipFlag",new Boolean(true));
                    if(relValidate){
                        relValidate = false;
                    }
                }else if(!EMPTY_STRING.equals(newRelationShip)){
                    formData.set("relationShipFlag",new Boolean(false));
                    formData.set("relationship",newRelationShip);
                }
                if(conflictStatusCode.equals("0")){
                    statusValidate = false;
                }
                vecNewAnnualData.add(formData);
            }
            //Error messeage is displayed if any of the project conflict status is not selected
            if(!statusValidate){
                actionMessages.add("invalidConflictStatus", new ActionMessage("error.annualDisclosure.invalidConflictStatus"));
                saveMessages(request, actionMessages);
            }
            //Error messeage is displayed if any of the project relationship is not mentioned
//            if(!relValidate){
//                actionMessages.add("invalidRelationShip", new ActionMessage("error.annualDisclosure.invalidRelationShip"));
//                saveMessages(request, actionMessages);
//            }
//            if(!statusValidate ||!relValidate){
            if(!statusValidate){
                request.setAttribute("entPrjValidateData",vecNewAnnualData);
                isValidate = false;
            }
        }
        return isValidate;
    }

    /*
     * MEthod to update financial entity once project updated
     */
    private void updFinEntity(HttpServletRequest request,String entityNumber)throws Exception{
        HashMap hmData=new HashMap();
        Vector vecEntity = new Vector();
        hmData.put(ENTITY_NUMBER,entityNumber);
        WebTxnBean webtxn=new WebTxnBean();
        Hashtable htFinEntity=(Hashtable)webtxn.getResults(request,"getFinDiscDet",hmData);
        if(htFinEntity != null && htFinEntity.size()>0 ){
            vecEntity=(Vector)htFinEntity.get("getFinDiscDet");
        }
        Timestamp dbTimestamp=prepareTimeStamp();
        if(vecEntity !=null && vecEntity.size() > 0){
            for(int i=0;i<vecEntity.size();i++){
                DynaValidatorForm finEntity=(DynaValidatorForm)vecEntity.get(i);
                int seqNum=Integer.parseInt((String)finEntity.get("sequenceNum"));
                seqNum=seqNum+1;
                finEntity.set("sequenceNum",Integer.toString(seqNum));
                finEntity.set(AC_TYPE,"I");
                finEntity.set("updtimestamp",dbTimestamp.toString());
                webtxn.getResults(request,"updPersonFinDisc",finEntity);
            }
        }
    }
    /*
     * Method to get the current Timestamp from DB
     */
    public Timestamp prepareTimeStamp() throws Exception{
        Timestamp dbTimestamp = null;
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        return dbTimestamp;
    }
    
}
