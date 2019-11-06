
package edu.mit.coeuslite.coiv2.actions;

/**
 *
 * @author twinkle
 */
/*
 * To fetch the list of all iacuc protocols for the corresponding logged in user
 * This would further be used for Creating iacuc Protocol Based Disclosure
 */

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
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

public class IacucProtocolBasedCoiDisclAction extends COIBaseAction {
      private static String PROJECT_TYPE ="IACUCProtocol";
      public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("disableCoiMenu",true);// to disable the questionnaire menu
        request.getSession().removeAttribute("hasEnteredCoiNonQnr");//checking whether was once entered or to without QNR ACTION   
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        coiInfoBean.setProjectType(PROJECT_TYPE);
        coiInfoBean.setModuleCode(ModuleConstants.IACUC_MODULE_CODE);
        coiInfoBean.setEventType(ModuleConstants.COI_EVENT_IACUC);
        coiInfoBean.setMenuType("NewIACUCProtocol");
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
        getAllProtocolList(request,disclosureNumber);               
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

    private Vector getAllProtocolList(HttpServletRequest request,String disclosureNumber) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;        
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
        hmData.put("discnumber", disclosureNumber);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllIACUCProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllIACUCProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            request.setAttribute("protocolList", protocolDet);
            request.getSession().setAttribute("protocolProjectListList", protocolDet);
        }

        return protocolDet;
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