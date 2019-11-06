/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.SessionConstants;
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
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;

/**
 *
 * @author Sony
 * To fetch the list of all awards for the corresponding logged in user
 * This would further be used for Creating an Award Based Disclosure
 */
public class AwardBasedCoiDisclAction extends COIBaseAction {
    private static String PROJECT_TYPE ="Award";
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {           
        request.getSession().setAttribute("disableCoiMenu",true);// to disable the questionnaire menu
        request.getSession().removeAttribute("hasEnteredCoiNonQnr");//checking whether was once entered or to without QNR ACTION 
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        coiInfoBean.setProjectType(PROJECT_TYPE);
        coiInfoBean.setModuleCode(ModuleConstants.AWARD_MODULE_CODE);
        coiInfoBean.setEventType(ModuleConstants.COI_EVENT_AWARD);
        coiInfoBean.setMenuType("NewAwardProtocol");
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        request.getSession().setAttribute("projectType",PROJECT_TYPE);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }        
        HttpSession session = request.getSession();        
        session.removeAttribute("CREATEFLAG");
        session.removeAttribute("getinonce");
        session.removeAttribute("qnranswerd");
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getApprovedSequence();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getSequenceNumber();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);        
        getAllAward(request,disclosureNumber);              
        String actionForward ="success";
     //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends   
        session.setAttribute("CoiInfoBean",coiInfoBean);
        return actionMapping.findForward(actionForward);
    }

      private Vector getAllAward(HttpServletRequest request,String disclosureNumber) throws Exception {
        HttpSession session = request.getSession();
        Vector awardDet = null;
        WebTxnBean webTxn = new WebTxnBean();       
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();
        HashMap hmData = new HashMap();
        hmData.put("updateUser", personId);
        hmData.put("discnumber", disclosureNumber);
        Hashtable awardDetList = (Hashtable) webTxn.getResults(request, "getAllNewAwards", hmData);
        awardDet = (Vector) awardDetList.get("getAllNewAwards");        
        if (awardDet != null && awardDet.size() > 0) {
            request.setAttribute("allAwardList", awardDet);
            session.setAttribute("allAwardProjectList", awardDet);
        }

        return awardDet;
    }
    private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {        
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
        if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("moduleCode",null);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
       if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
        } 
    }
}