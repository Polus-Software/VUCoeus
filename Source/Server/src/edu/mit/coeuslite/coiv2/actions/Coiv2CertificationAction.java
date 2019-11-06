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
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.Coiv2CertificationBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Certification;
import edu.mit.coeuslite.coiv2.services.CoiCertificationService;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mr Lijo Joy
 */
public class Coiv2CertificationAction extends COIBaseAction {
     private static int DISCLOSURE_CERTIFY_ACTION_CODE_REVISED = 888;
    private static int DISCLOSURE_CERTIFY_ACTION_CODE = 810;
    private static int EVENT_TYPE_CODE=6;
    private static int projectCount=0;
    private static String disclExpDate = "";
    private static Date convertedDate;
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//added by 10-12-2010
HttpSession session = request.getSession();
PersonInfoBean person1 = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personHomeUnit="";
String personId1=person1.getPersonID();
WebTxnBean webTxn = new  WebTxnBean();
 HashMap hmData1 = new HashMap();
 request.removeAttribute("DiscViewByPrjt");//to check Certify menu selected
 request.setAttribute("DiscViewCertify", true);//to check Certify menu selected
        hmData1.put("personId", personId1);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            personHomeUnit=personInfoBean.getHomeUnit();
            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
//added by  10-12-2010
//HttpSession session = request.getSession();
PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personId=person.getPersonID();
CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           }
           String disclosureNumber = coiInfoBean.getDisclosureNumber();
           Integer sequenceNumber = coiInfoBean.getSequenceNumber();  
           // sub header details S T A R T S 
        getCoiPersonDetails(personId,request);       
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        Integer approvedSequenceNumber  = coiInfoBean.getSequenceNumber();
        if(approvedSequenceNumber == null){
            approvedSequenceNumber =coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,approvedSequenceNumber,personId,request);     
       // sub header details E N D S
      //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends  
                /*
 * Conflit status validation
 */
   if(coiInfoBean!=null && !coiInfoBean.getProjectType().equalsIgnoreCase("Travel")){
        String projectId=coiInfoBean.getProjectId();
        if(projectId!=null&&projectId.equalsIgnoreCase("null")){
           projectId=null;
         }
            projectCount = 0;
            getAllProposals(request); //  list only Disclosed submitted Dev Proposal  
            getAllInstProposals(request);
            getAllProtocolList(request);
            getAllIacucProtocolList(request);
            getAllAward(request);
            if(!(coiInfoBean.getEventType()==5 ||coiInfoBean.getEventType()==6)){
                   projectCount = 1;
                 } 

             HashMap hmData = new HashMap();             
             hmData.put("disclosureNumber", disclosureNumber);
             hmData.put("sequenceNumber", sequenceNumber);   
             hmData.put("personId", personId);  
             hmData.put("projectCount", projectCount);
            Hashtable projectDetailsList = (Hashtable) webTxn.getResults(request, "fnIsSavedCoiDiscDet", hmData);
            HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fnIsSavedCoiDiscDet");
            if(hmfinEntityList !=null && hmfinEntityList.size()>0){
            int count = Integer.parseInt(hmfinEntityList.get("li_return").toString());
            if(count ==0){
             request.setAttribute("coiCertValidFails",true);
            }
            }
   }      
//for annual disclosure menu change
        CoiCommonService coiCommonService1 = CoiCommonService.getInstance();
        Integer disclosureAvailable = coiCommonService1.userHasDisclosure(request);
        if (disclosureAvailable > 0) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        }
        //for annual disclosure menu change end

        String success = "success";
         int actionId=0;
        Coiv2Certification coiv2Certification = (Coiv2Certification) actionForm;
        if (actionMapping.getPath().equals("/saveCertificationCoiv2")) {
            session = request.getSession();
            Coiv2CertificationBean bean = new Coiv2CertificationBean();
            bean.setDisclosureNumber(disclosureNumber);          
            bean.setSequenceNumber(sequenceNumber);
            bean.setCertificationBy(personId);
            String cerText = request.getParameter("cerText");
            bean.setCertificationText(cerText);
            Integer reviewStatus=2;
            bean.setReviewStatusCode(reviewStatus);
            CoiCertificationService coiCertificationService = CoiCertificationService.getInstance();
            coiCertificationService.saveOrUpdate(bean, request);

            // Disclosure Mail Notification  Session
            //actionId=DISCLOSURE_CERTIFY_ACTION_CODE;
         
         

            String setDisclosureNumberData = "Disclosure Number : "+disclosureNumber;           
            HashMap hmData2=new HashMap();       
            hmData2.put("coiDisclosureNumber", disclosureNumber);
            hmData2.put("sequenceNumber",sequenceNumber);
            hmData2.put("personId", person1.getPersonID());
            try{
                    Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData2);
                    Vector statusDet = (Vector) DisclData.get("getDisclStatus");
                    CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
                    if (statusDet != null && statusDet.size() > 0) {
                        CurDisclosureBean = (CoiDisclosureBean) statusDet.get(0);
                    }
                     DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData2);
                Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
                if (DisclDet != null && DisclDet.size() > 0) {
                CurDisclosureBean= (CoiDisclosureBean) DisclDet.get(0);
            }
                   
                    String userName = "Person Name : ";
                    userName = userName + person1.getUserName();
                    String disclosureStatus = "Disclosure Status : Certified";
                    String personName="Person Name :  ";
                    //String sponserName="Sponser Name :  ";
                    String unit="Unit : ";                    
                    String personHomeUnitData="Unit:"+personHomeUnit;
                   // String dateData="Due Date:"+disclExpDate;
                   String duedate=CurDisclosureBean.getExpirDate();
//                  try{
//                    duedate=convertedDate.toString();}
//                    catch(Exception e){}
                    String dueDateData="Due Date : "+duedate;
                    //disclosureStatus = disclosureStatus+CurDisclosureBean.getDisclosureStatus();
                    SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
                    Vector reporter = null;
                    Vector vecRecipients = new Vector();
                   // reporter = setstatus.getReporterMailDisclosure(disclosureNumber, request);
                    reporter = setstatus.getReporterByDiscl(disclosureNumber, request);
                            //viewers = setstatus.getViewerByDisclosure(assignDisclUserBean.getCoiDisclosureNumber(), request);
                    if (reporter != null && reporter.size() > 0) {
                        for (Iterator it = reporter.iterator(); it.hasNext();) {
                                    // PersonRecipientBean ob = (PersonRecipientBean) it.next();
                                     PersonInfoBean ob = (PersonInfoBean) it.next();
                                     PersonRecipientBean reciepientob = new PersonRecipientBean();
                                     if(ob.getEmail()!=null){
                                         reciepientob.setEmailId(ob.getEmail());
                                     }

                                    //ob.setUserId(string);//set userid
                                    vecRecipients.add(reciepientob);
                        }
                    } 
                  DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
                  MailMessageInfoBean mailMsgInfoBean = null;
                  String pjctType="";
                                // Send mail to newly added reviewer
                  if(session.getAttribute("projectType")!=null)
                  {
                      pjctType=session.getAttribute("projectType").toString();
                  }
                  String message="Your Disclosure has been submitted";
                  String pjctId="";
                  String pjctTitle="";
                   String pjctNoData="";
                   String pjctNameData="";
                   String pjctEventData="";
                  if((pjctType.equalsIgnoreCase("Proposal"))||(pjctType.equalsIgnoreCase("Protocol"))||(pjctType.equalsIgnoreCase("Iacuc Protocol"))||(pjctType.equalsIgnoreCase("Award"))||(pjctType.equalsIgnoreCase("Travel")))
                  {
                      if(session.getAttribute("projectDetailsListInSeesioncoi2")!=null){
                       Vector pjctDteVec=(Vector)session.getAttribute("projectDetailsListInSeesioncoi2");
                       if(pjctDteVec.size()>0){
                          CoiPersonProjectDetails coiPersonProjectDetails =new CoiPersonProjectDetails();
                          coiPersonProjectDetails = (CoiPersonProjectDetails) pjctDteVec.get(0);
                          pjctId=coiPersonProjectDetails.getModuleItemKey();
                          pjctTitle=coiPersonProjectDetails.getCoiProjectTitle();
                       }
                       pjctNoData="Project Number: "+pjctId;
                       pjctNameData="Project Name: "+pjctTitle;
                       pjctEventData="Event Type: "+pjctType;
                      }
                            try{
                            boolean  mailSent;
                             actionId=DISCLOSURE_CERTIFY_ACTION_CODE;
                              mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                   mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                    mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", message);
                                   mailMsgInfoBean.appendMessage(userName, "\n");
                                   mailMsgInfoBean.appendMessage(personHomeUnitData, "\n");
                                   mailMsgInfoBean.appendMessage(dueDateData, "\n");
                                   mailMsgInfoBean.appendMessage(pjctEventData, "\n");
                                   mailMsgInfoBean.appendMessage(pjctNoData, "\n");
                                   mailMsgInfoBean.appendMessage(pjctNameData, "\n");
                                   
                                   mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                                }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());
                        }
                  }
                  else{
                   
                        
                        if(EVENT_TYPE_CODE==6){
                        actionId=DISCLOSURE_CERTIFY_ACTION_CODE_REVISED;
                        }
                        actionId=DISCLOSURE_CERTIFY_ACTION_CODE;
                      try{
                            boolean  mailSent;
                              mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                   mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                    mailMsgInfoBean.appendMessage(" ",message);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");
                                   mailMsgInfoBean.appendMessage(personHomeUnitData, "\n");
                                   mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
//                                 mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                                 //  mailMsgInfoBean.appendMessage(dueDateData, "\n");
                                   mailMsgInfoBean.appendMessage(disclosureNumber, "\n");
                                   mailMsgInfoBean.appendMessage(personName, "\n");
                                   mailMsgInfoBean.appendMessage(unit, "\n");
                                  //mailMsgInfoBean.appendMessage(sponserName, "\n");
                                   mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                                }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());
                        }
                  }
                 } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }




            request.getSession().removeAttribute("currentSequence");
            ///-------code for exit status------
            request.getSession().removeAttribute("extProjcode");
            request.getSession().removeAttribute("currentSequence");
            request.getSession().removeAttribute("exitCode");
            request.getSession().removeAttribute("exitCodeNotes");
            request.getSession().removeAttribute("extProjListAll");
            request.getSession().removeAttribute("AlreadySavedProjectsForExt");
            request.getSession().removeAttribute("extCodeAlreadySavedProjects");
            request.getSession().removeAttribute("projectDetailsListInSeesion_ext");
            request.getSession().removeAttribute("AlreadySavedProjectsForExt");
            request.getSession().removeAttribute("extCodeAlreadySavedProjects_upd");
            request.getSession().removeAttribute("financialArrayEntityList_ext");
            request.getSession().removeAttribute("financialEntityList_ext");
            ///-------code for exit status------
            success = "continue";
        } else {

             HashMap hmapData = new HashMap();
                hmapData.put("coiDisclosureNumber", disclosureNumber);
                hmapData.put("sequenceNumber", sequenceNumber);
                Hashtable hashtable = (Hashtable) webTxn.getResults(request,"getCoiUpdateTimeStamp",hmapData);
                 Vector coiDiscl=(Vector)hashtable.get("getCoiUpdateTimeStamp");
                 CoiDisclosureBean coiBean=new CoiDisclosureBean();
                   if (coiDiscl != null && coiDiscl.size() > 0) {
                       coiBean =(CoiDisclosureBean)coiDiscl.get(0);
                   }
//            CoiDisclosureBean discl = getCurrentDisclPerson(request);
//            coiv2Certification.setCertificationText(discl.getCertificationText());
              coiv2Certification.setUpdateTimeStampNew(coiBean.getUpdateTimestampNew());
//            coiv2Certification.setUpdateTimeStamp(discl.getUpdateTimestampFormat());
        }
         return actionMapping.findForward(success);
    }

//    private CoiDisclosureBean getCurrentDisclPerson(HttpServletRequest request) throws Exception {
//
//        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
//        String personId = personInfoBean.getPersonID();
//        HashMap hmreviewData = new HashMap();
//        hmreviewData.put("personId", personId);
//        WebTxnBean webTxnBean = new WebTxnBean();
//        Hashtable htCurrDiscl =
//                (Hashtable) webTxnBean.getResults(request, "getCurrDisclPerCoiv2", hmreviewData);
//        Vector vecDiscl = (Vector) htCurrDiscl.get("getCurrDisclPerCoiv2");
//        CoiDisclosureBean discl = new CoiDisclosureBean();
//        if (vecDiscl != null && vecDiscl.size() > 0) {
//            for (int i = 0; i < vecDiscl.size(); i++) {
//                discl = (CoiDisclosureBean) vecDiscl.get(0);
//            }
//        }
//        return discl;
//    }

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
            disclExpDate=apprvdDisclosureBean.getExpirDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");
            Date convertedDate = dateFormat.parse(disclExpDate);
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
     private Vector getAllProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
       // String createUser = personBean.getUserName();
         String personId = personBean.getPersonID();
         /* **  speed
         PropBasedCoiDisclAction pDisclActn=new PropBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = pDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        ** */
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
           hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getAllNewProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getAllNewProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            request.setAttribute("proposalList", propUserDet);
            request.getSession().setAttribute("proposalListForAttachment", propUserDet);
            projectCount=projectCount+propUserDet.size();
          }
        return propUserDet;
    }

     private Vector getAllInstProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String createUser = personBean.getUserName();
         String personId = personBean.getPersonID();
         /* **  speed
         PropBasedCoiDisclAction pDisclActn=new PropBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = pDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
         ** */
        HashMap hmData = new HashMap();
        hmData.put("pid", personId);
        hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getInstProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getInstProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            request.setAttribute("getInstProposals", propUserDet);
            request.getSession().setAttribute("getInstProposals", propUserDet);
            projectCount=projectCount+propUserDet.size();
        }
        return propUserDet;
    }
      private Vector getAllProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;

        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String createUser = personBean.getUserId();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        /* **  speed
        ProtocolBasedCoiDisclAction protoCoiDisclActn=new ProtocolBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = protoCoiDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        ** */
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllNewProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllNewProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            request.setAttribute("protocolList", protocolDet);
            request.getSession().setAttribute("protocolProjectListList", protocolDet);
            projectCount=projectCount+protocolDet.size();
        }

        return protocolDet;
    }
     private Vector getAllIacucProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String createUser = personBean.getUserId();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        /* **  speed
        ProtocolBasedCoiDisclAction protoCoiDisclActn=new ProtocolBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = protoCoiDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
        ** */
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllIACUCProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllIACUCProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            request.setAttribute("getAllIACUCProtocolList", protocolDet);
            request.getSession().setAttribute("getAllIACUCProtocolList", protocolDet);
            projectCount=projectCount+protocolDet.size();
        }

        return protocolDet;
    }

    private Vector getAllAward(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        Vector awardDet = null;
        WebTxnBean webTxn = new WebTxnBean();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String updateUser = personBean.getUserName();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
      /* **  speed
        AwardBasedCoiDisclAction protoCoiDisclActn=new AwardBasedCoiDisclAction();
        CoiDisclosureBean disclosureBean = protoCoiDisclActn.getApprovedDisclosureBean(personId, request);
        String disclosureNumber = disclosureBean.getCoiDisclosureNumber();
         ** */
        HashMap hmData = new HashMap();
        hmData.put("updateUser", personId);
        hmData.put("discnumber",null);
        Hashtable awardDetList = (Hashtable) webTxn.getResults(request, "getAllNewAwards", hmData);
        awardDet = (Vector) awardDetList.get("getAllNewAwards");
        if (awardDet != null && awardDet.size() > 0) {
            request.setAttribute("allAwardList", awardDet);
            session.setAttribute("allAwardProjectList", awardDet);
            session.setAttribute("awardListForAttachment", awardDet);
            projectCount=projectCount+awardDet.size();
        }

        return awardDet;
    }
}