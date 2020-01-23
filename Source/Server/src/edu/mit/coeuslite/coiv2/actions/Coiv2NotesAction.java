/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Notes;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.CoiNotesService;
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
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Mr Lijo
 */
public class Coiv2NotesAction extends COIBaseAction {

    /**
     * Function to save, update, remove and get Notes
     * @param actionMapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();       
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           }
           String disclosureNumber = coiInfoBean.getDisclosureNumber();
           Integer sequenceNumber = coiInfoBean.getSequenceNumber();
        session.removeAttribute("formPendingDisl");
        session.removeAttribute("frmReviewList");
        session.removeAttribute("fromReview");
        session.removeAttribute("disclosureNotesData");
        request.removeAttribute("DiscViewByPrjt");//to check Certify menu selected
        request.setAttribute("DiscViewNotes", true);//to check Certify menu selected
        CoiNotesService coiNotesService = CoiNotesService.getInstance();       
        String operationType = (String) request.getAttribute("operationType");
        String isViewer = (String) request.getAttribute("isViewer");
        String forward = "failure";
        Coiv2Notes coiv2NotesBean = (Coiv2Notes) actionForm;
        request.setAttribute("notepadTypeList",  coiNotesService.getCoiNotesType(request));
//added by 10-12-2010

PersonInfoBean person1 = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
 String personId1=coiInfoBean.getPersonId();

if(personId1 == null || personId1.equals("null")){

   personId1 = person1.getPersonID();
}
WebTxnBean txnBean = new WebTxnBean();
 HashMap hmData1 = new HashMap();
        hmData1.put("personId", personId1);
        Hashtable htPersonData = (Hashtable) txnBean.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
            request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
//added by  10-12-2010
        //for approved disclosure details start
      //  HttpSession session = request.getSession();        
     // sub header details S T A R T S 
        getCoiPersonDetails(personId1,request);       
        disclosureNumber = coiInfoBean.getDisclosureNumber();
       Integer  approvedSequenceNumber = coiInfoBean.getSequenceNumber();
        if(approvedSequenceNumber == null){
            approvedSequenceNumber =coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,approvedSequenceNumber,personId1,request);     
       // sub header details E N D S
        //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends
        Integer disclosureAvailable = userHasDisclosure(request);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        //for annual disclosure menu change end

        if (actionMapping.getPath().equals("/saveNotesCoiv2")) {
            // Function to save, update and remove notes according to  acType
            operationType = request.getParameter("operationType");
            isViewer = request.getParameter("isViewer");
            if (isTokenValid(request)) {
                forward = coiNotesService.saveOrUpdateOrDeleteCoiNotes(coiv2NotesBean, request, actionMapping, operationType);
            } else {
                forward = "success";
            }
           String persnId = null;
            persnId = (String) session.getAttribute("param3");
            if (persnId == null) {
                PersonInfoBean personDet = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                persnId = personDet.getPersonID();
            }
             String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(persnId))
            {
                request.setAttribute("ToShowMY", "true");
            }
          LoadHeaderDetails(persnId,request);
           //Menu Saved Start   
            request.setAttribute("byProjectMenu",true);
            if(coiInfoBean!=null){
            coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
            }
          //Menu Saved ends
          request.setAttribute("DiscViewNotes", true);//to check Notes menu selected
          resetToken(request);

        }
        //calling function to get notes     
            coiv2NotesBean.setCoiDisclosureNumber(disclosureNumber);          
            coiv2NotesBean.setCoiSequenceNumber(sequenceNumber.toString());
       forward = coiNotesService.getCoiNotes(coiv2NotesBean, request, actionMapping);
        request.setAttribute("operationType", operationType);
        request.setAttribute("isViewer", isViewer);
        if (isViewer != null && isViewer.equals("VIEWER")) {
            request.setAttribute("option", "notes");
            forward = "notesViewer";
        }
        BeanUtils.copyProperties(actionForm, new Coiv2Notes());
        saveToken(request);
        return actionMapping.findForward(forward);
    }
        //get Approved DisclosureBean
    public CoiDisclosureBean getApprovedDisclosureBean(String personId,HttpServletRequest request)throws Exception{
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
}

  private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {       
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
            if(coiDisclosureNumber==null)
            { hmData.put("sequenceNumber",0);

            }
            else
            {hmData.put("sequenceNumber", sequenceNumber);
            }
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();            
            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
            if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("ApprovedDisclDetView", DisclDet);
            }
      
    }
        private Integer userHasDisclosure(HttpServletRequest request) throws Exception {

        WebTxnBean webTxn = new WebTxnBean();
        Integer hasDisclosure = 0;
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userId = personBean.getPersonID();
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", userId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "checkDisclosureAvailable", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("checkDisclosureAvailable");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            hasDisclosure = Integer.parseInt((String) hasRightMap.get("disclosureAvailable"));
        }
        return hasDisclosure;
    }        

}