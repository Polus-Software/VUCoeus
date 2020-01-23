/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.mail.CoeusMailService;
import edu.mit.coeus.utils.mail.SetMailAttributes;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.CoiUsersBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.ApprovedProjectComparator;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.coiv2.utilities.UserDetailsBeanCoiV2;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;


/**
 *
 * @author Mr.Roshin Roy K
 * for showing set status screen and updating the status.
 */
public class SetStatusAdminCoiV2 extends COIBaseAction {

        private static int REVIEWER_ADMIN_STATUS_CHANGE_ACTION_CODE_APPROVE_DISCLOSURE = 890;
        private static int REVIEWER_ADMIN_STATUS_CHANGE_ACTION_CODE_DISAPPROVE_DISCLOSURE = 897;
        private static Integer sequenceNumber=0;
          
       @Override
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String forward = "fail";
        DisclosureMailNotification discloNotification = new  DisclosureMailNotification();       
        HttpSession session = request.getSession();
       // Integer sequenceNumber = (Integer) session.getAttribute("param2");
        //for showing status screen
        if (mapping.getPath().equals("/setstatus")) {
            String param=(String)request.getParameter("param");
       // new tick and Attchment fix from "Approve" Link
            String disclNumber = request.getParameter("param1");
            String discSequenceNumber = request.getParameter("param2");
            String mdlName = "";
            if(request.getParameter("param5") !=null){
               mdlName = request.getParameter("param5"); 
            }else if(request.getSession().getAttribute("projectType") != null){               
               mdlName = request.getSession().getAttribute("projectType").toString();
            }
            
            request.getSession().setAttribute("selectedAnnualDisclosureNo",disclNumber);
            request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclNumber);
             request.getSession().setAttribute("DisclNumber",disclNumber);
            request.getSession().setAttribute("selectedAnnualDisclosureSerialNo",discSequenceNumber);
            request.getSession().setAttribute("disclPjctModuleName",mdlName);
            request.getSession().setAttribute("projectType",mdlName);
            request.getSession().removeAttribute("projectList");
            
            
       // new tick and Attchment fix from "Approve" Link
            if (param!=null && param.equalsIgnoreCase("pending"))
            {
             request.setAttribute("ReviewActPending", true);//to check "Set Review Status" menu selected               
            }
            else if(param!=null && param.equalsIgnoreCase("approve"))
            {
              request.setAttribute("ReviewActApprove", true);//to check " Approve" menu selected
            }
             else if(param!=null && param.equalsIgnoreCase("disapprove"))
            {
              request.setAttribute("ReviewActDisapprove", true);//to check " Disapprove" menu selected
            }
            String disclosureNumber = "";
            String personId = "";
            
            if(request.getParameter("param1") != null) {
                disclosureNumber = (String)request.getParameter("param1");
                session.setAttribute("param1",disclosureNumber);
            }
            else {
                disclosureNumber = (String) session.getAttribute("param1");
            }
            if(request.getParameter("param2") != null){
                sequenceNumber = Integer.parseInt(request.getParameter("param2").toString());
                session.setAttribute("param2",sequenceNumber);
            }
            else {
                sequenceNumber = (Integer) session.getAttribute("param2");
            }
            if(request.getParameter("param3") != null) {
                personId = (String) request.getParameter("param3");
                session.setAttribute("param3",personId);
            }
            else{
                 personId = (String) session.getAttribute("param3");
            }
           PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            if (personId == null) {               
                personId = person.getPersonID();
                request.setAttribute("owner", person.getFullName());
            }
 /**/
   if(isRightAtPersonHomeUnit(personId,person.getPersonID(),request)==0){
     request.setAttribute("noRightAtPerHomeUnit", "true");
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllReview")){
          return mapping.findForward("showAllReview");
     }
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllAnnualReview")){
          return mapping.findForward("showAllAnnualReview");
     }
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllEventReview")){
          return mapping.findForward("showAllEventReview");
     }
 }        
            
/**/            
            
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            String disclosureHistory = "";
            
            if(request.getParameter("param6") != null) {
                disclosureHistory = (String)request.getParameter("param6");
                request.getSession().setAttribute("param6", disclosureHistory);
            }else {
                disclosureHistory = (String) session.getAttribute("param6");
            }            
            if (disclosureHistory == null) {
                CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
            }
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);// saved menu   
            WebTxnBean webTxn = new WebTxnBean();
            Vector statusDet = null;
            Vector statusDispDet = null;
            Vector DisposStatusList = null;
            Vector DisclStatusList = null;
            Vector DisclDet = null;

            HashMap inputMap = new HashMap();
            inputMap.put("disclosureNumber", disclosureNumber);
            inputMap.put("sequenceNumber", sequenceNumber);
            Hashtable tbEntList = (Hashtable) webTxn.getResults(request, "getEntityStatusForDiscl", inputMap);
            Vector vcEntList = (Vector) tbEntList.get("getEntityStatusForDiscl");

            TreeSet entTreeSet = new TreeSet();

            if(vcEntList != null && vcEntList.size() > 0) {

                for(int i=0; i < vcEntList.size(); i++) {
                    CoiProjectEntityDetailsBean bean = (CoiProjectEntityDetailsBean)vcEntList.get(i);
                    String desc = bean.getEntityStatusCode() +":"+bean.getEntityStatus();
                    entTreeSet.add(desc);
                }
            }
             String selectedEntDesc = "";
             String selEntCode = "";
            if(entTreeSet != null && entTreeSet.size() > 0)  {
               String selEnt = String.valueOf(entTreeSet.last());
               String[] splitList = selEnt.split(":");
                   selectedEntDesc =  splitList[1];
                   selEntCode =  splitList[0];
            }

            request.setAttribute("selectedEntDesc", selectedEntDesc);
            request.setAttribute("selEntCode", selEntCode);

            HashMap hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
                        if(disclosureNumber==null){hmData.put("sequenceNumber",sequenceNumber);}
            else{hmData.put("sequenceNumber", sequenceNumber);}
            hmData.put("personId", personId);

            Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclosureDispositionStatus", hmData);
            statusDet = (Vector) statusData.get("getDisclStatus");
            statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
            DisposStatusList = (Vector) statusData.get("getAllCoiDispositionStatus");
            DisclStatusList = (Vector) statusData.get("getAllCoiDisclStatus");
            if (statusDet != null && statusDet.size() > 0) {
                request.setAttribute("statusDetView", statusDet);
            }
         /* **   if (statusDispDet != null && statusDispDet.size() > 0) {
                request.setAttribute("statusDispDetView", statusDispDet);
            }
         ** */
            Vector<CoiDisclosureBean> ApprovedDisclStatusList = new Vector<CoiDisclosureBean>();
            Vector<CoiDisclosureBean> DisAppDisclStatusList = new Vector<CoiDisclosureBean>();
            Vector<CoiDisclosureBean> PendingDisclStatusList = new Vector<CoiDisclosureBean>();
            if (DisclStatusList != null && DisclStatusList.size() > 0) {
                for (Iterator it = DisclStatusList.iterator(); it.hasNext();) {
                    CoiDisclosureBean bean = (CoiDisclosureBean) it.next();
                    if ( bean.getDisclosureStatusCode().toString().equalsIgnoreCase("300") || bean.getDisclosureStatusCode().toString().equalsIgnoreCase("2")) {
                        DisAppDisclStatusList.add(bean);
                    } else if(bean.getDisclosureStatusCode().toString().equalsIgnoreCase("102") || bean.getDisclosureStatusCode().toString().equalsIgnoreCase("103") ||
                            bean.getDisclosureStatusCode().toString().equalsIgnoreCase("104") ||  bean.getDisclosureStatusCode().toString().equalsIgnoreCase("100")) {
                   //     PendingDisclStatusList.add(bean);
                    }
                    else if (bean.getDisclosureStatusCode() >= 200 && bean.getDisclosureStatusCode() < 300 || bean.getDisclosureStatusCode().toString().equalsIgnoreCase("1")) {
                        ApprovedDisclStatusList.add(bean);
                    }
                }
                request.setAttribute("DisclStatusListView", DisclStatusList);
                request.setAttribute("ApprovedDisclStatusList", ApprovedDisclStatusList);
                request.setAttribute("DisAppDisclStatusList", DisAppDisclStatusList);
              //  request.setAttribute("PendingDisclStatusList", PendingDisclStatusList);
            }

            Hashtable htReviewStatus = (Hashtable) webTxn.getResults(request, "getAllReviewStatus", null);
            Vector vcReviewStatus = (Vector)htReviewStatus.get("getAllReviewStatus");

            if(vcReviewStatus != null && vcReviewStatus.size() > 0) {
                for(int i=0; i<vcReviewStatus.size(); i++) {
                    CoiDisclosureBean bean = (CoiDisclosureBean) vcReviewStatus.get(i);
                    if(bean.getReviewStatusCode() != 1 && bean.getReviewStatusCode() != 2 && bean.getReviewStatusCode() != 3)
                    PendingDisclStatusList.add(bean);
                }
               request.setAttribute("PendingDisclStatusList", PendingDisclStatusList);
            }

            if (DisposStatusList != null && DisposStatusList.size() > 0) {
                request.setAttribute("DisposStatusListView", DisposStatusList);
            }

            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            DisclDet = (Vector) DisclData.get("getDisclBySequnce");
            if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("DisclDetView", DisclDet);
            }

            request.setAttribute("option", "setstatus");
            if (request.getAttribute("message") != null || request.getAttribute("revmessage") != null) {
                request.setAttribute("setStatusMessage", false);
            } else {
                request.setAttribute("setStatusMessage", true);
            }

            if(request.getAttribute("ApprovedDisclDetView") != null) {
                  Vector approvedDisclosureView= (Vector) request.getAttribute("ApprovedDisclDetView");
                  CoiDisclosureBean bean = (CoiDisclosureBean)approvedDisclosureView.get(0);
                  Integer reviewStatus = bean.getReviewStatusCode();

                  if(reviewStatus == 4) {
                      request.setAttribute("assignedToReviewer", true);
                  }
            }

            GetAnnulaInternalDisclosureCoiV2 annualDisclosure = new GetAnnulaInternalDisclosureCoiV2();
            HashMap dataMap = new HashMap();
            dataMap.put("coiDisclosureNumber", disclosureNumber);
            dataMap.put("sequenceNumber", sequenceNumber);
            dataMap.put("personId", personId);
            annualDisclosure.getProjectDetailsForEvent(request, dataMap, mdlName);
            forward = "setstatus";
        }
//for status updation
        if (mapping.getPath().equals("/updateStatus")) {
            String discloStatuscode = request.getParameter("discNoValue");
            String disposStatuscode = request.getParameter("dispoNoValue");
            String dispcloStatus = request.getParameter("discstatus");
          //HttpSession session = request.getSession();
            String disclosureNumber = (String) session.getAttribute("param1");
            String isFRmListApprv = "false";
            String fromList = "";

            if(request.getParameter("isFRmListApprv") != null){
                isFRmListApprv = request.getParameter("isFRmListApprv").toString();
            }
            if(request.getParameter("frmList") != null){
                fromList = request.getParameter("frmList").toString();
            }

            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId = person.getUserName();

            String isFromReviewList = "";

            if(request.getParameter("fromReviewList") != null) {
                isFromReviewList = request.getParameter("fromReviewList").toString();
            }

            if(isFromReviewList.equals("true")) {
                WebTxnBean webTxn = new WebTxnBean();
                HashMap inputMap = new HashMap();
                String disclNum = request.getParameter("param1");
                Integer seqNumber = Integer.parseInt(request.getParameter("param2").toString());
                String prsnId = request.getParameter("param3");
                inputMap.put("disclosureNumber", disclNum);
                inputMap.put("sequenceNumber", seqNumber);
                disclosureNumber = disclNum;
                sequenceNumber = seqNumber;
                request.getSession().setAttribute("param1", disclNum);
                request.getSession().setAttribute("param2", seqNumber);
                request.getSession().setAttribute("param3", prsnId);
                Hashtable tbEntList = (Hashtable) webTxn.getResults(request, "getEntityStatusForDiscl", inputMap);
                Vector vcEntList = (Vector) tbEntList.get("getEntityStatusForDiscl");

                TreeSet entTreeSet = new TreeSet();

                if(vcEntList != null && vcEntList.size() > 0) {

                    for(int i=0; i < vcEntList.size(); i++) {
                        CoiProjectEntityDetailsBean bean = (CoiProjectEntityDetailsBean)vcEntList.get(i);
                        String desc = bean.getEntityStatusCode() +":"+bean.getEntityStatus();
                        entTreeSet.add(desc);
                    }
                }
                 String selectedEntDesc = "";
                 String selEntCode = "";
                if(entTreeSet != null && entTreeSet.size() > 0)  {
                   String selEnt = String.valueOf(entTreeSet.last());
                   String[] splitList = selEnt.split(":");
                       selectedEntDesc =  splitList[1];
                       selEntCode =  splitList[0];
                }

                 if(selEntCode != null && selEntCode.trim().length() == 0) {
                     selEntCode="210";
                     selectedEntDesc = "No Conflict Exists";
                 }

                request.setAttribute("selectedEntDesc", selectedEntDesc);
                request.setAttribute("selEntCode", selEntCode);
                dispcloStatus = selectedEntDesc;
                disposStatuscode = "1";
                discloStatuscode = selEntCode;
            }
//Coiv2DispDisclStatusForm coiv2DispDisclStatusForm = (Coiv2DispDisclStatusForm)actionForm;
//int disclosureStatus =Integer.parseInt(coiv2DispDisclStatusForm.getDisclosureStatus());
//Integer discStatus=new Integer(disclosureStatus);
//int dispositionStatus = Integer.parseInt(coiv2DispDisclStatusForm.getDispositionStatus());
//Integer dispStatus=new Integer(dispositionStatus);
//Integer disclosureStatus = Integer.parseInt(coiv2DispDisclStatusForm.getDisclosureStatus());
//Integer dispositionStatus = Integer.parseInt(coiv2DispDisclStatusForm.getDispositionStatus());\
            Integer disclosureStatuscode = null;
            Integer dispositionStatuscode = null;
            String dispos_Status="";
            if(disposStatuscode.equals("1"))
                 dispos_Status="Approved";
            else if(disposStatuscode.equals("2"))
                 dispos_Status="Disapproved";
            else
                 dispos_Status="Pending";

            //setting changed status
           request.setAttribute("changedStatus",dispcloStatus);


            if (discloStatuscode != null && discloStatuscode.trim().length() > 0) {
                disclosureStatuscode = Integer.parseInt(discloStatuscode);
            }else {
               disclosureStatuscode = 210;
            }
            if (disposStatuscode != null) {
                dispositionStatuscode = Integer.parseInt(disposStatuscode);
            }
            CoiDisclosureBean coiDisclosureBean = new CoiDisclosureBean();
            coiDisclosureBean.setCoiDisclosureNumber(disclosureNumber);
            coiDisclosureBean.setSequenceNumber(sequenceNumber);
            coiDisclosureBean.setDisclosureDispositionCode(dispositionStatuscode);
            coiDisclosureBean.setDisclosureStatusCode(disclosureStatuscode);
            coiDisclosureBean.setDisclosureStatus(dispcloStatus);
            coiDisclosureBean.setDispositionStatus(dispos_Status);
            coiDisclosureBean.setUpdateUser(personId);

            String projectType = null;
            if(session.getAttribute("projectType") != null) {
                projectType = session.getAttribute("projectType").toString();
            }
            else{
                if(request.getParameter("param5") != null) {
                    projectType = request.getParameter("param5");
                }
            }

            // expiration date

            if(projectType != null && (projectType.equals("Annual") ||
                    projectType.equals("Revision"))) {
                    Date expirationDate = getExpirationDate(request);

                    if(expirationDate == null){
                        Date expDate = new Date();
                        Calendar cal=Calendar.getInstance();
                        cal.setTime(expDate);
                        cal.add(Calendar.MONTH, 12);
                        cal.add(Calendar.DATE, -1);
                        expirationDate = cal.getTime();
                        String formatDate = "";
                        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                        formatDate = formatter.format(expirationDate);
                        expirationDate = formatter.parse(formatDate);
                    }
                    coiDisclosureBean.setExpirationDate(expirationDate);
            }
            else {
                WebTxnBean webTxnBean = new WebTxnBean();
                HashMap inputMap = new HashMap();
                String expDate = null;
                Date expirationDate = null;
                inputMap.put("disclosureNumber", disclosureNumber);
                Hashtable htExpDate = (Hashtable)webTxnBean.getResults(request, "fngetExpirationDate", inputMap);
                HashMap hmExpDate = (HashMap) htExpDate.get("fngetExpirationDate");
                if(hmExpDate != null) {
                    if(hmExpDate.get("ls_exp_date") != null) {
                        expDate = hmExpDate.get("ls_exp_date").toString();
                    }
                }

                if(expDate != null) {
                    DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                    expirationDate = formatter.parse(expDate);
                }
                else {
                    expirationDate = getExpirationDate(request);

                    if(expirationDate == null){
                        Date exDate = new Date();
                        Calendar cal=Calendar.getInstance();
                        cal.setTime(exDate);
                        cal.add(Calendar.MONTH, 12);
                        cal.add(Calendar.DATE, -1);
                        expirationDate = cal.getTime();
                        String formatDate = "";
                        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                        formatDate = formatter.format(expirationDate);
                        expirationDate = formatter.parse(formatDate);
                    }
                }

                coiDisclosureBean.setExpirationDate(expirationDate);
            }

            // end of exp date logic

            Boolean cont = false;
            try {
                // if the disposition status is approved [1] we have to copy the approved sequence..
            if(dispositionStatuscode==1){
                 WebTxnBean webTxnBean = new WebTxnBean();
                Vector apprvdDiscl=new Vector();
              //  webTxnBean.getResults(request, "saveDisclosureHistory", coiDisclosureBean);
                Integer reviewStatus=3;
                coiDisclosureBean.setReviewStatusCode(reviewStatus);
                webTxnBean.getResults(request, "updateDispDisclStatus", coiDisclosureBean);
               if (!(projectType != null && (projectType.equals("Annual") ||
                    projectType.equals("Revision")))){

                        HashMap hmp=new HashMap();
                        hmp.put("coiDisclosureNumber", disclosureNumber);
                        hmp.put("sequenceNumber", sequenceNumber);
                        Hashtable approvedDislosures=(Hashtable)webTxnBean.getResults(request,"getApprovedDisclosures", hmp);
                        apprvdDiscl= (Vector)approvedDislosures.get("getApprovedDisclosures");
                        if(apprvdDiscl !=null && apprvdDiscl.size()>0){
                        Collections.sort(apprvdDiscl, new ApprovedProjectComparator());
                        CoiDisclosureBean disclbean =  (CoiDisclosureBean)apprvdDiscl.get(0);
                        Integer lastApprovedSeqNo=disclbean.getSequenceNumber();
                          HashMap hmData=new HashMap();
                          hmData.put("coiDisclosureNumber", disclosureNumber);
                          hmData.put("sequence_number", sequenceNumber);
                          hmData.put("last_apprvd_sequence_number", lastApprovedSeqNo);
                          Hashtable result=(Hashtable)webTxnBean.getResults(request,"copyLastApprovedSeq", hmData);
                          HashMap resultMap=(HashMap)result.get("copyLastApprovedSeq");
                          int copyResultFlag=Integer.parseInt((resultMap.get("resultFlag")).toString());
                        }

               }

                request.setAttribute("message", true);
            } else if(dispositionStatuscode==2){
                WebTxnBean webTxnBean = new WebTxnBean();
                Integer reviewStatus=3;
                coiDisclosureBean.setReviewStatusCode(reviewStatus);
                webTxnBean.getResults(request, "updateDispDisclStatus", coiDisclosureBean);
                 request.setAttribute("message", true);
            }else {
                WebTxnBean webTxnBean = new WebTxnBean();
                String rvStatus = "";
                  if(request.getParameter("reviewStatus") != null) {
                 rvStatus = request.getParameter("reviewStatus").toString();
                }
                Integer reviewStatus= Integer.parseInt(rvStatus);
                coiDisclosureBean.setReviewStatusCode(reviewStatus);
                webTxnBean.getResults(request, "updateDispDisclStatus", coiDisclosureBean);
                request.setAttribute("revmessage", true);
            }
            //copy function ends

                cont = true;
            } catch (Exception e) {
                 e.printStackTrace();
                 request.setAttribute("Exception", e);                 
                request.setAttribute("message", false);
                 request.setAttribute("revmessage", false);
                 return mapping.findForward("failure");
            }
            try {
                if (cont) {
                     ////-------------start
                     String setDisclosureNumberData = "Disclosure Number : "+ disclosureNumber;
            WebTxnBean webTxn = new WebTxnBean();
//            PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
//            String personId =person.getPersonID();
            HashMap hmData2=new HashMap();
                    Vector reporterpre = null;
                    reporterpre = getReporterByDiscl(disclosureNumber, request);
                PersonInfoBean person1 = new PersonInfoBean();

                    if (reporterpre != null && reporterpre.size() > 0) {
                        person1 = (PersonInfoBean) reporterpre.get(0);
                    }
            hmData2.put("coiDisclosureNumber", disclosureNumber);
            hmData2.put("sequenceNumber",sequenceNumber);
            hmData2.put("personId", person1.getPersonID());

           // Hashtable DisclData1 = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData2);
         //   DisclDet = (Vector) DisclData.get("getDisclBySequnce");
           try{
                    Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData2);
                    Vector statusDet = (Vector) DisclData.get("getDisclStatus");
                    CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
                    if (statusDet != null && statusDet.size() > 0) {
                        CurDisclosureBean = (CoiDisclosureBean) statusDet.get(0);
                    }
                    String userName = "User Name : ";
                    userName = userName + person1.getUserName();
                    String disclosureStatus = "Disclosure Status : ";
                    disclosureStatus = disclosureStatus+CurDisclosureBean.getDisclosureStatus();
                    String proposalNumber="Project Number : ";
                    String personName="Person Name : ";
                    String sponserCode="Sponsor Code : ";


                    String pjctEventData = null;
                    String pjctType = null;
                    int actionId=0;
                    if(session.getAttribute("projectType")!=null)
                  {
                      pjctType=session.getAttribute("projectType").toString();
                  }
                   if(dispositionStatuscode ==1 ){
                       actionId=REVIEWER_ADMIN_STATUS_CHANGE_ACTION_CODE_APPROVE_DISCLOSURE;
                   }else if(dispositionStatuscode ==2){
                       actionId=REVIEWER_ADMIN_STATUS_CHANGE_ACTION_CODE_DISAPPROVE_DISCLOSURE;
                   }




                   // int actionId=REVIEWER_ADMIN_STATUS_CHANGE_ACTION_CODE;
            Vector reporter = null;
            Vector vecRecipients = new Vector();
            reporter = getReporterByDisclosure(disclosureNumber, request);
            if (reporter != null && reporter.size() > 0) {
                for (Iterator it = reporter.iterator(); it.hasNext();) {
                            PersonRecipientBean ob = (PersonRecipientBean) it.next();
                            vecRecipients.add(ob);
                }
            }
            Vector viewers = null;
            viewers =getViewerByDisclosure(disclosureNumber, request);
            if (viewers != null && viewers.size() > 0) {
                for (Iterator itv = viewers.iterator(); itv.hasNext();) {
                    PersonRecipientBean object = (PersonRecipientBean) itv.next();
                     vecRecipients.add(object);
                }
            }
            Vector adminsviewer = null;
            HashMap hmData = new HashMap();
            Hashtable users = (Hashtable) webTxn.getResults(request, "getAdminsByDisclosure", hmData);
            adminsviewer = (Vector) users.get("getAdminsByDisclosure");
            if (adminsviewer != null && adminsviewer.size() > 0) {
                for (Iterator ita = adminsviewer.iterator(); ita.hasNext();) {
                    PersonRecipientBean adminobject = (PersonRecipientBean) ita.next();
                   vecRecipients.add(adminobject);
                }
            }
            PersonInfoBean personInfoBean ;
            String personUnit="";
            if(session.getAttribute("person")!=null){
            personInfoBean  = (PersonInfoBean)session.getAttribute("person");
            personUnit=personInfoBean.getHomeUnit();}
            String message="Your submitted project event has been Approved.";
            String disclMessage="Your submitted Disclosure has been Approved.";
            pjctType=session.getAttribute("projectType").toString();

            MailMessageInfoBean mailMsgInfoBean = null;

                try{
                    boolean  mailSent;
                      mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                      if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                            mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                   mailMsgInfoBean.appendMessage("",disclMessage);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
                                   mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");
                                   mailMsgInfoBean.appendMessage(personName, "\n");
                                   mailMsgInfoBean.appendMessage(sponserCode, "\n");
                             if((pjctType.equalsIgnoreCase("Proposal"))||(pjctType.equalsIgnoreCase("IRB Protocol"))||(pjctType.equalsIgnoreCase("IACUC Protocol"))||(pjctType.equalsIgnoreCase("Award")))
                                {
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(proposalNumber, "\n");
                                }

                             //  end  set the status to message---------
                            ////UtilFactory.log("======set recipnt to msginfobean in Set Status service");
                            mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                        }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
             } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
             }

                }
            } catch (Exception e) {
                request.setAttribute("message1", false);
            }
            request.setAttribute("option", "setstatus");
            forward = "updateStatus";

            if(isFRmListApprv.equals("true")) {
                if(fromList.equals("showAll")) {
                     forward = "showAll";
                }else if(fromList.equals("annualDiscl")){
                    forward = "annualDiscl";
                }

            }
        }
        return mapping.findForward(forward);
    }




    public Vector getViewersByDiscl(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector viewerDet = null;
        Hashtable viewerData = (Hashtable) webTxn.getResults(request, "getViewersByDiscl", hmData);
        viewerDet = (Vector) viewerData.get("getViewersByDiscl");
        return viewerDet;
    }

    public Vector getReporterByDiscl(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector reporterDet = null;
            Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getReporterByDiscl", hmData);
            reporterDet = (Vector) reporterData.get("getReporterByDiscl");
        return reporterDet;
    }

    public Vector getPersonById(String reporterId, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("personId", reporterId);
        Vector personDet = null;
            Hashtable personData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
            personDet = (Vector) personData.get("getPersonDetails");
        return personDet;
    }


     public Vector getViewerByDisclosure(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector viewerDet = null;
            Hashtable viewerData = (Hashtable) webTxn.getResults(request, "getViewersByDisclosure", hmData);
            viewerDet = (Vector) viewerData.get("getViewersByDisclosure");
        return viewerDet;
    }

     public Vector getReporterByDisclosure(String disclosureNumber, HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("coiDisclosureNumber", disclosureNumber);
        Vector reporterDet = null;
            Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getReporterByDisclosure", hmData);
            reporterDet = (Vector) reporterData.get("getReporterByDisclosure");
        return reporterDet;
    }


    //get Approved DisclosureBean
    public CoiDisclosureBean getApprovedDisclosureBean(String personId, HttpServletRequest request) throws Exception {
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

    private void setApprovedDisclosureDetails(String coiDisclosureNumber, Integer sequenceNumber, String personId, HttpServletRequest request) throws Exception {

       HttpSession session = request.getSession();// by Vineetha
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
                 if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
/* **
        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
        if (statusDispDet != null && statusDispDet.size() > 0) {
            request.setAttribute("statusDispDetView", statusDispDet);
        }
 ** */
        hmData.put("moduleCode",null);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);

        }


                  //added by Vineetha
             hmData = new HashMap();
            hmData.put("personId", personId);
            Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
            Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
            if (personDatas != null && personDatas.size() > 0) {
                PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

                        request.setAttribute("PersonDetails", personDatas);
                session.setAttribute("person", personInfoBean);
            }

    }

    private Date getExpirationDate(HttpServletRequest request){
        Date expirationDate = null;
         try {
            String parameterName = "COI_ANNUAL_REVIEW_DUE";
            Hashtable param_value = new Hashtable();
            HashMap hmCertInv = new HashMap();
            HashMap hmMap = new HashMap();
            WebTxnBean webTxnBean = new WebTxnBean();
            hmMap.put("parameterName", parameterName);
            param_value = (Hashtable) webTxnBean.getResults(request, "getParameterValue", hmMap);
            hmCertInv = (HashMap) param_value.get("getParameterValue");
            String parameterValue = (String) hmCertInv.get(("parameterValue").toString());
            boolean isValid = false;
            if(parameterValue != null && parameterValue.trim().length() != 0 && !parameterValue.equals("0") && !parameterValue.equalsIgnoreCase("null")) {
                Matcher matcher;
                Pattern pattrn = Pattern.compile("[0-9][0-9]\\-[0-9][0-9]\\-\\d\\d\\d\\d");
                Pattern pattern = Pattern.compile("[0-9][0-9]\\-[0-9][0-9]");
                matcher = pattrn.matcher(parameterValue);
                isValid = matcher.matches();

                if(!isValid) {
                   matcher = pattern.matcher(parameterValue);
                   isValid = matcher.matches();

                   if(isValid) {
                       Calendar cal=Calendar.getInstance();
                       Date currentDate =  cal.getTime();
                       String currentYear = "";

                       String[] splitList = currentDate.toString().split("\\s");

                       if(splitList != null && splitList.length > 0) {
                           currentYear = splitList[splitList.length - 1];
                           parameterValue = parameterValue + "-" + currentYear;
                       }
                   }
                }
                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                 Calendar currentDateTime = Calendar.getInstance();               
                String dateNow = formatter.format(currentDateTime.getTime());
                Date currentDate = (Date) formatter.parse(dateNow);
                if(isValid){
                    expirationDate = formatter.parse(parameterValue);
                     boolean results = expirationDate.before(currentDate);                
                    if(results) {
                         Calendar cal=Calendar.getInstance();
                        cal.setTime(expirationDate);
                        cal.add(Calendar.MONTH, 12);
                        expirationDate = cal.getTime();
                    }
                    
                }             
                                 
               
            }
            else {
                Date expDate = new Date();
                Calendar cal=Calendar.getInstance();
                cal.setTime(expDate);
                cal.add(Calendar.MONTH, 12);
                cal.add(Calendar.DATE, -1);
                expirationDate = cal.getTime();
                String formatDate = "";
                DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
                formatDate = formatter.format(expirationDate);
                expirationDate = formatter.parse(formatDate);
            }

             } catch(Exception ex) {
                UtilFactory.log(ex.getMessage());
             }
        return expirationDate;
    }
    
     public Vector getAdminCheck(HttpServletRequest request,String unitNumber)throws Exception, java.lang.Exception{
         HashMap hmData = new HashMap();
         WebTxnBean webTxn = new WebTxnBean();
         hmData.put("unitNumber",unitNumber);
         Vector checkAdminDet = new Vector();
        Hashtable checkAdminData = (Hashtable)webTxn.getResults(request,"getAdminCheck", hmData);
        checkAdminDet=(Vector)checkAdminData.get("getAdminCheck");
        return checkAdminDet;
     }
    
}