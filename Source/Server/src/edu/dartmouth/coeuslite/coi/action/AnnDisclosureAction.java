/*
 * @(#)AnnDisclosureAction.java 1.0 February 20, 2009, 3:26 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.dartmouth.coeuslite.coi.action;

import edu.dartmouth.coeuslite.coi.beans.DisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author satheeshkumarkn
 * @version
 */
public class AnnDisclosureAction extends COIBaseAction {
    private String EMPTY_STRING  = "";
    private String personId = EMPTY_STRING;
    private static final String ENTITY_NUMBER = "entityNumber";
    
    /** Creates a new instance of AnnualDisclosureAction */
    public AnnDisclosureAction() {
    }
     /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an ActionForward instance describing where and how control
     * should be forwarded, or null if the response has already been completed.
     *
     * <br>The method fetches all the Financial Entities with it's project for a person 
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
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean =
        (PersonInfoBean)session.getAttribute("person");
        if(personInfoBean!=null){
            personId = personInfoBean.getPersonID();
        }

        String entityNumber  = request.getParameter(ENTITY_NUMBER);
        String nextEntityNumber =(String)request.getAttribute("nextEntityNumber");
        if(nextEntityNumber!=null && !nextEntityNumber.equals(EMPTY_STRING)){
            entityNumber = nextEntityNumber;
        }
        Vector vecAnnDiscEntities = getAnnualDiscEntities(personId, request);
        Integer entitySeqNumber = null;
        String entityName  = EMPTY_STRING;
        DynaValidatorForm dynaForm = null;
        if(vecAnnDiscEntities !=null && vecAnnDiscEntities.size()>0){
            if((entityNumber == null || EMPTY_STRING.equals(entityNumber))){
                dynaForm = (DynaValidatorForm)vecAnnDiscEntities.get(0);
                entityNumber = (String)dynaForm.get(ENTITY_NUMBER);
                entitySeqNumber =(Integer)dynaForm.get("sequenceNumber");
                entityName =(String)dynaForm.get("entityName");
            }else{
                for(int index =0; index<vecAnnDiscEntities.size();index++){
                    dynaForm = (DynaValidatorForm)vecAnnDiscEntities.get(index);
                    if(dynaForm.get(ENTITY_NUMBER).equals(entityNumber)){
                        entitySeqNumber = (Integer)dynaForm.get("sequenceNumber");
                        entityName =(String)dynaForm.get("entityName");
                    }
                }
            }
        }else{
            return actionMapping.findForward("getCertified");
        }
        Vector vecFinEntities = getFinancialEntityDetails(entityNumber, request);
        Vector vecCoiStatus = getCoiStatus(request);
        Vector vecEntityProjects = new Vector();
        if(request.getAttribute("entPrjValidateData") == null){
                //If there is no projects, page is redirected to certify page
                vecEntityProjects = getEntityProjects(entityNumber,personId, request);
                 if(vecEntityProjects ==null || vecEntityProjects.size()<1){
                     return actionMapping.findForward("getCertified");
                 }
        }else{
            vecEntityProjects = (Vector)request.getAttribute("entPrjValidateData");
        }
        Vector vcDiscEntities = checkEntityUpdated(vecAnnDiscEntities,request);
        session.setAttribute("coiStatus", vecCoiStatus);
        session.setAttribute("financialEntity", vecFinEntities);
        session.setAttribute("allEntityProjects", vecEntityProjects);
        session.setAttribute("allAnnualDiscEntities",vcDiscEntities);
        request.setAttribute(ENTITY_NUMBER,entityNumber);
        request.setAttribute("entityName",entityName);
        session.setAttribute("entitySequenceNumber",entitySeqNumber);
        return actionMapping.findForward("success");
    }
    /*
     * Method to check financial entities project conflict status is set
     */
    private Vector checkEntityUpdated(Vector vecAnnDiscEntities, HttpServletRequest request)throws Exception{
        Vector vcDiscEntities = new Vector();
        for(int index =0 ;index<vecAnnDiscEntities.size() ; index++){
            DynaValidatorForm entityData = (DynaValidatorForm)vecAnnDiscEntities.get(index);
            String entityNumber = (String)entityData.get(ENTITY_NUMBER);
            Vector vecEntityProjects = getEntityProjects(entityNumber,personId, request);
            if(vecEntityProjects != null && vecEntityProjects.size()>0){
                for(int prjIndex=0; prjIndex<vecEntityProjects.size();prjIndex++){
                    DynaValidatorForm entityProject = (DynaValidatorForm)vecEntityProjects.get(prjIndex);
                    String entityNumberPrj = (String)entityProject.get(ENTITY_NUMBER);
                    if(entityNumberPrj != null){
                        entityData.set("updated","Y");
                    }else{
                        entityData.set("updated","N");
                        break;
                    }
                }
            }
            vcDiscEntities.add(entityData);
        }
            
        return vcDiscEntities;
    }
    
    /**
     * To get active financial entities
     * @return vecAnnDiscEntities - will contain all the active entities
     */
    private Vector getAnnualDiscEntities(String personId, HttpServletRequest request) throws Exception{
        HashMap hmData = new HashMap();
        Vector vecAnnDiscEntities  = new Vector();
        hmData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAnnDisc =
        (Hashtable)webTxnBean.getResults(request, "getActiveEntity", hmData);
        if(htAnnDisc != null && htAnnDisc .size() > 0){
            vecAnnDiscEntities = (Vector)htAnnDisc.get("getActiveEntity");
        }
        return vecAnnDiscEntities;
    }
    
  
    /*
     * To get details of an financial entity
     * @return vecFinData - Financial Entity data
     */
    private Vector getFinancialEntityDetails(String entityNumber, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        Vector vecFinData  = new Vector();
        hmData.put(ENTITY_NUMBER,entityNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htFinEntDetails =
        (Hashtable)webTxnBean.getResults(request, "getFinDiscDet", hmData);
        if(htFinEntDetails != null && htFinEntDetails .size() > 0){
            vecFinData = (Vector)htFinEntDetails.get("getFinDiscDet");
        }
        return vecFinData;
    }
    
    /*
     * To get entity related projects
     * @return vecPendingData 
     */
    private Vector getEntityProjects(String entityNumber, String personId, HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        Vector vecPendingData = new Vector();
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
        if(htPendingData != null && htPendingData .size() > 0){
            vecPendingData = (Vector)htPendingData.get("getCoiPersonFnProjects");
        }
        return vecPendingData;
    }
    
    /*
     *  Method to get all conflict status details
     */
    private Vector getCoiStatus(HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecCoiStatus = new Vector();
        Hashtable htCoiStatus =
        (Hashtable)webTxnBean.getResults(request,"getCOIStatus",null);
        if(htCoiStatus != null && htCoiStatus .size() > 0){
            vecCoiStatus =(Vector)htCoiStatus.get("getCOIStatus");
        }
        return vecCoiStatus;
    }
}
