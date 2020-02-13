/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiEventTypeAttrBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author indhulekha
 */
public class CreateNewTravelAction extends COIBaseAction{
    private static String PROJECT_TYPE ="Travel";
    private boolean flag=false;
    private String SHRT_TXT_FIELD = "USE_SHRT_TXT_FLD";
    private String LNG_TXT_FIELD = "USE_LNG_TXT_FLD";
    private String DATE_FIELD = "USE_DATE_FLD";
    private String NBR_FIELD = "USE_NMBR_FLD";
    private String SLCT_FIELD = "USE_SLCT_BOX";

    private String REQ_SHRT_TXT_FIELD = "REQ_SHRT_TXT_FLD";
    private String REQ_LNG_TXT_FIELD = "REQ_LNG_TXT_FLD";
    private String REQ_DATE_FIELD = "REQ_DATE_FLD";
    private String REQ_NBR_FIELD = "REQ_NMBR_FLD";
    private String REQ_SLCT_FIELD = "REQ_SLCT_BOX";

    private String SHRT_TXT_FIELD_LBL = "SHRT_TXT_FLD";
    private String LNG_TXT_FIELD_LBL = "LNG_TXT_FLD";
    private String DATE_FIELD_LBL = "DATE_FLD";
    private String NBR_FIELD_LBL = "NMBR_FLD";
    private String SLCT_FIELD_LBL = "SLCT_BOX";

    private int FIELD_COUNT = 5;

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
            String success = "success";
            request.getSession().removeAttribute("travelList");
            String eventCode="";
              CoiDisclosureBean annualBean=null;
              request.setAttribute("DiscViewTravel", true);
            boolean edit=false;
                   edit =Boolean.parseBoolean(request.getParameter("edit"));
             if(request.getParameter("fromReview")!=null && !request.getParameter("fromReview").equals("null"))
          { request.setAttribute("showDiscDet",true);
             request.getSession().setAttribute("fromReview", true);
             }
            boolean view = false;

            if(request.getAttribute("viewMode") != null) {
                view=(Boolean)request.getAttribute("viewMode");
                edit = true;
                request.setAttribute("edit", true);
            }else{
             view=Boolean.parseBoolean(request.getParameter("view"));
            }
       request.getSession().setAttribute("disableCoiMenu",true);// to disable the questionnaire menu
        request.getSession().removeAttribute("hasEnteredCoiNonQnr"); //checking whether was once entered or to without QNR ACTION
        request.getSession().removeAttribute("InPrgssProjectID");
        HttpSession session = request.getSession();
        session.setAttribute("travelEvent", true);
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
       CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
       if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        coiInfoBean.setProjectType(PROJECT_TYPE);
        coiInfoBean.setModuleCode(ModuleConstants.TRAVEL_MODULE_CODE);
        coiInfoBean.setEventType(ModuleConstants.COI_EVENT_TRAVEL);
        coiInfoBean.setMenuType("NewTravel");
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        request.getSession().setAttribute("projectType",PROJECT_TYPE);
        session.removeAttribute("CREATEFLAG");
        session.removeAttribute("getinonce");
        session.removeAttribute("qnranswerd");
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
             annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        request.setAttribute("personId", personId);
        getCoiPersonDetails(personId,request);
        String disclosureNumber=null;
        Integer sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getApprovedSequence();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getSequenceNumber();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
        WebTxnBean webTxn = new WebTxnBean();
        HashMap hmData = new HashMap();
         hmData.put("eventTypeCode",ModuleConstants.COI_EVENT_TRAVEL);

       Hashtable htAttr = (Hashtable) webTxn.getResults(request, "getCoiDisclEventTypeAttr", hmData);
        //Vector  inputFields = (Vector)htAttr.get("getCoiDisclEventTypeAttr");
       HashMap attrMap = (HashMap)htAttr.get("getCoiDisclEventTypeAttr");
       Set keySets = attrMap.entrySet();

       int shrtTxtField = 0;
       int lngTxtField = 0;
       int dateField = 0;
       int SlctField = 0;
       int nmbrField = 0;
       HashMap travelKeyMap = new HashMap();
       int index = 0;

       if(keySets != null && keySets.size() >0) {
          Iterator it = keySets.iterator();

          String shrtTextField = "";
          String longTextField = "";
          String numberField = "";
          String date_Field = "";
          String selectField = "";

          while (it.hasNext()) {
            //  keyMap = (HashMap)it.next();
            index++;
              shrtTextField = SHRT_TXT_FIELD+"_"+index;

              if(attrMap.containsKey(shrtTextField) && attrMap.get(shrtTextField).toString().equalsIgnoreCase("Y")) {
                shrtTxtField++;
                travelKeyMap.put(shrtTextField, attrMap.get(shrtTextField));
                shrtTextField = REQ_SHRT_TXT_FIELD+"_"+index;
                travelKeyMap.put(shrtTextField, attrMap.get(shrtTextField));
                shrtTextField = SHRT_TXT_FIELD_LBL+"_"+index+"_LABEL";
                travelKeyMap.put(shrtTextField, attrMap.get(shrtTextField));

              }
              longTextField = LNG_TXT_FIELD+"_"+index;
              if(attrMap.containsKey(longTextField) && attrMap.get(longTextField).toString().equalsIgnoreCase("Y")) {
                lngTxtField++;

                travelKeyMap.put(longTextField, attrMap.get(longTextField));
                longTextField = REQ_LNG_TXT_FIELD+"_"+index;
                travelKeyMap.put(longTextField, attrMap.get(longTextField));
                longTextField = LNG_TXT_FIELD_LBL+"_"+index+"_LABEL";
                travelKeyMap.put(longTextField, attrMap.get(longTextField));
              }

              numberField = NBR_FIELD+"_"+index;
              if(attrMap.containsKey(numberField) && attrMap.get(numberField).toString().equalsIgnoreCase("Y")) {
                nmbrField++;
                travelKeyMap.put(numberField, attrMap.get(numberField));
                numberField = REQ_NBR_FIELD+"_"+index;
                travelKeyMap.put(numberField, attrMap.get(numberField));
                numberField = NBR_FIELD_LBL+"_"+index+"_LABEL";
                travelKeyMap.put(numberField, attrMap.get(numberField));
              }

              date_Field = DATE_FIELD+"_"+index;
              if(attrMap.containsKey(date_Field) && attrMap.get(date_Field).toString().equalsIgnoreCase("Y")) {
                dateField++;
                travelKeyMap.put(date_Field, attrMap.get(date_Field));
                date_Field = REQ_DATE_FIELD+"_"+index;
                travelKeyMap.put(date_Field, attrMap.get(date_Field));
                date_Field = DATE_FIELD_LBL+"_"+index+"_LABEL";
                travelKeyMap.put(date_Field, attrMap.get(date_Field));
              }

              selectField = SLCT_FIELD+"_"+index;
              if(attrMap.containsKey(selectField) && attrMap.get(selectField).toString().equalsIgnoreCase("Y")) {
                SlctField++;

                travelKeyMap.put(selectField, attrMap.get(selectField));
                selectField = REQ_SLCT_FIELD+"_"+index;
                travelKeyMap.put(selectField, attrMap.get(selectField));
                selectField = SLCT_FIELD_LBL+"_"+index+"_LABEL";
                travelKeyMap.put(selectField, attrMap.get(selectField));
              }

              if(index == FIELD_COUNT) {
                  break;
              }
           }
       }

       request.setAttribute("shrtTxtCount", shrtTxtField);
       request.setAttribute("lngTxtCount", lngTxtField);
       request.setAttribute("nmrFldCount", nmbrField);
       request.setAttribute("dateCount", dateField);
       request.setAttribute("selectCount", SlctField);
       request.setAttribute("travelInputFields", travelKeyMap);

       session.setAttribute("shrtTxtCount", shrtTxtField);
       session.setAttribute("lngTxtCount", lngTxtField);
       session.setAttribute("nmrFldCount", nmbrField);
       session.setAttribute("dateCount", dateField);
       session.setAttribute("selectCount", SlctField);
       session.setAttribute("travelInputFields", travelKeyMap);

        //Menu Saved Start
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
        //Menu Saved ends
        if(actionMapping.getPath().equals("/travelDetails")){
           request.getSession().removeAttribute("disableCoiMenu");
            disclosureNumber=request.getParameter("param1");


            if(disclosureNumber == null) {
                disclosureNumber = coiInfoBean.getDisclosureNumber();
            }
            personId=request.getParameter("param7");
            if(personId == null){
            personId=request.getParameter("param3");}

            if(personId == null){
                personId = coiInfoBean.getPersonId();
            }
            request.setAttribute("personId", personId);
            getCoiPersonDetails(personId,request);
            Integer seqnum = 0;
            if(request.getParameter("sequenceNumber") != null) {
                seqnum=Integer.parseInt(request.getParameter("sequenceNumber"));
            request.setAttribute("sequenceNumber", request.getParameter("sequenceNumber"));
            }else {
              seqnum = coiInfoBean.getSequenceNumber();
            }

            if (request.getParameter("param7") != null) {
                session.setAttribute("param6", request.getParameter("param7"));
            }
//for getting current notes
            session.setAttribute("selectedValue","current");  
//for getting travel details for selected user
            session.setAttribute("param1",disclosureNumber);
            session.setAttribute("param3",personId);        
            session.setAttribute("param7",personId);
            eventCode=coiInfoBean.getProjectType();
            request.setAttribute("flag", true);
             coiInfoBean.setDisclosureNumber(disclosureNumber);
            coiInfoBean.setSequenceNumber(seqnum);
            getAllTravel(request,disclosureNumber,seqnum,eventCode);
            coiInfoBean.setDisclosureNumber(disclosureNumber);
            coiInfoBean.setSequenceNumber(seqnum);
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", seqnum);
            hmData.put("personId", personId);
            Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
            Vector questionDet = (Vector) questionData.get("getQnsAns");

            String isValid = null;

            if(questionDet == null || (questionDet != null && questionDet.size() ==0)) {

                 isValid = getCoiQuestionnaire(coiInfoBean, request);
            }else{
               isValid = "valid" ;
            }

             if(isValid == null){
                 session.setAttribute("noQuestionnaireForModule", true);
                }

        if(edit){
        GetAnnulaInternalDisclosureCoiV2 annualDisclosure = new GetAnnulaInternalDisclosureCoiV2();
            HashMap dataMap = new HashMap();
            dataMap.put("coiDisclosureNumber", disclosureNumber);
            dataMap.put("sequenceNumber", seqnum);
            dataMap.put("personId", personId);
            annualDisclosure.getProjectDetailsForEvent(request, dataMap, eventCode);
            session.setAttribute("isEvent",true);
            request.getSession().removeAttribute("disableCoiMenu");
            if(view){
            success="viewMode";
            }
            request.getSession().removeAttribute("disableCoiMenu");
        }
             coiMenuDataSaved(disclosureNumber,seqnum,personId,request);

              String userId = person.getUserId();
              HashMap inpuMap = new HashMap();

            inpuMap.put("disclosureNumber", disclosureNumber);
            inpuMap.put("seqNumber", seqnum);
            inpuMap.put("userId", userId.toUpperCase());

            Hashtable htResult =(Hashtable)webTxn.getResults(request,"isReviewer",inpuMap);
            HashMap  hmResult = (HashMap)htResult.get("isReviewer");
            int isReviewer = Integer.parseInt(hmResult.get("li_count").toString());

            if(isReviewer == 1) {
                session.setAttribute("isReviewer", true);
                session.setAttribute("reviewerUserId", userId);
            }

             hmData = new HashMap();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        hmData.put("sequenceNumber", seqnum);
        hmData.put("personId", personId);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
                    if (DisclDet != null && DisclDet.size() > 0) {
                        request.setAttribute("ApprovedDisclDetView", DisclDet);
                        for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                            CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                        }
                    }

        }
        session.setAttribute("CoiInfoBean",coiInfoBean);
        return actionMapping.findForward(success);

    }
     private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
         coiDisclosureNumber=(String)annualBean.getCoiDisclosureNumber();
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
     private void getAllTravel(HttpServletRequest request,String disclosureNumber, Integer seqnum,String eventCode) throws Exception {

         HttpSession session = request.getSession();
        String projectCount="";
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        HashMap travelUserDet = new HashMap();
        if(eventCode!=null && eventCode.equalsIgnoreCase("Travel")){
            int eventType=8;
        CoiProjectEntityDetailsBean coiDiscl = new CoiProjectEntityDetailsBean();
        hmData.put("disclosureNumber", disclosureNumber);
        request.setAttribute("disclosureNumber", disclosureNumber);

        hmData.put("sequenceNumber",seqnum);
                request.setAttribute("sequenceNumber", seqnum);

        hmData.put("eventType",eventType);
        Hashtable tvlUserRole = (Hashtable) webTxn.getResults(request, "getAllTvlData", hmData);
        travelUserDet = (HashMap) tvlUserRole.get("getAllTvlData");

        request.setAttribute("DiscViewTravel", true);

         if (travelUserDet != null && travelUserDet.size() > 0) {
             for(int i=0;i<travelUserDet.size();i++){
             coiDiscl = (CoiProjectEntityDetailsBean)travelUserDet.get(i);
             }
            projectCount = projectCount + travelUserDet.size();
            request.setAttribute("travelList", travelUserDet);
            session.setAttribute("travelList", travelUserDet);
          }}

        }

              }
