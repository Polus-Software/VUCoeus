/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;


import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.*;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
/**
 *
 * @author x + iy
 */
public class CoiV2AnnContinueWithoutQnr extends COIBaseAction{
     private static final String questionType = "F";
     public static final Integer DISPOSITIONCODE = new Integer(3);
     public static final Integer STATUS_IN_PROGRESS = new Integer(100);
     public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
         
        HashMap hmFinData = new HashMap();
        String forward = "success";
        int flag = 0;
        hmFinData.put("questionType", questionType); 
        HttpSession session = request.getSession();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId=person.getPersonID();
        String disclosureNumber=null;
        HashMap hmData1 = new HashMap();
        WebTxnBean webTxn1 = new WebTxnBean();
        hmData1.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn1.getResults(request, "getPersonDetails", hmData1);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }

 String disclNumber=null;
 Integer seqNumber=null;
        String operation = request.getParameter("operation");
        String operationType = request.getParameter("operationType");
        if (operationType == null || operationType.equals("")) {
            operationType = (String) request.getAttribute("operationType");
            request.setAttribute("operationType", operationType);           
        }
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }
        person = (PersonInfoBean) session.getAttribute("person");
        PersonInfoBean loggedPer = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
        String loggedId = loggedPer.getUserId();
        //right Check starts***********

        boolean hasRightToEdit = false;
        String strUserprivilege =
                session.getAttribute(PRIVILEGE).toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if (userprivilege > 1) {
            hasRightToEdit = true;
        } else if (userprivilege > 0) {

            if (person.getPersonID().equals(loggedPer.getPersonID())) {
                hasRightToEdit = true;
            }
        } else if (userprivilege == 0) {
            if (person.getPersonID().equals(loggedPer.getPersonID())) {
                hasRightToEdit = true;
            }
        }
        if (!hasRightToEdit) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noright",
                    new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
            saveMessages(request, actionMessages);
            return actionMapping.findForward("exception");
        }      
     
        WebTxnBean webTxnBean = new WebTxnBean();
        CoiDisclosureBean perDiscl = new CoiDisclosureBean();
        
        /**/
        
        Date expirationDate = new Date();
        Date date1 = new Date();
        boolean isDue = false;

        if(session.getAttribute("AnnualDueDate") != null) {
            String expDate = (String)session.getAttribute("AnnualDueDate");
            DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            date1 = (Date) formatter.parse(expDate);
            if(session.getAttribute("isReviewDue") != null) {
                 isDue = (Boolean)session.getAttribute("isReviewDue");
                if(isDue) {
                    try
                    {
                        date1 = (Date) formatter.parse(expDate);

                        Calendar cal=Calendar.getInstance();
                        cal.setTime(date1);
                        cal.add(Calendar.MONTH, 12);
                        expirationDate = cal.getTime();
                        perDiscl.setExpirationDate(expirationDate);

                    } catch (ParseException ex)
                    {
                        Logger.getLogger(COIBaseAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else {
                    expirationDate = date1;
                    perDiscl.setExpirationDate(expirationDate);
                }
            }
        }
          
         
         /**/
       
        setCurrentDisclosure(hmFinData, request);
        CoiDisclosureBean discl = getCurrentDisclPerson(request);
        disclosureNumber = (String) session.getAttribute("DisclNumber");
        perDiscl.setCoiDisclosureNumber(disclosureNumber);        
        Integer seq = (Integer) session.getAttribute("DisclSeqNumber");       
        Integer seqNum =new  Integer(1);
        String  disclosureNum =null;
         perDiscl.setAcType("U");      
         boolean disclosureExists =  checkDisclosureExists(personId, request);
       if (disclosureExists == false) {
                flag = 1;
                disclosureNum = getNextDisclNum(request);
                if(discl!=null){
                   discl.setCoiDisclosureNumber(disclosureNum); 
                   discl.setSequenceNumber(seqNum);
                }
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_ANNUAL);
                request.getSession().setAttribute("InPrgssCode","5");
                perDiscl.setAcType("I");      
                // NEW MENU CHANGE 
                request.getSession().setAttribute("COIDiscNumber",disclosureNum);
                request.getSession().setAttribute("COISeqNumber",seqNum);
               // NEW MENU CHANGE                
                 perDiscl.setCoiDisclosureNumber(disclosureNum);
                 perDiscl.setSequenceNumber(seqNum);
                if (!isTokenValid(request)) {
                    boolean disclExists =
                            checkDisclosureExists(personId, request);
                    if (disclExists) {
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("duplicateSubmission",
                                new ActionMessage("error.coiMain.disclosureExists"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
                        // throw new Exception("Duplicate Submission");
                    }
                }
            } 
       String projectType =(String)request.getSession().getAttribute("AnnualRevision");
      if(flag!=1 && projectType!=null && projectType.equalsIgnoreCase("Revision")&& request.getSession().getAttribute("hasEnteredCoiNonQnr")== null){
          
          discl = getCurrentDisclPerson(request);
        disclosureNumber = (String) session.getAttribute("DisclNumber");
         seq =(Integer)session.getAttribute("DisclSeqNumber");
        perDiscl.setCoiDisclosureNumber(disclosureNumber);
        perDiscl.setExpirationDate(discl.getExpirationDate());
        
        
      
        String dNumber =null;
        Integer sNumber = -1;
        if(discl!=null ){
           dNumber = discl.getCoiDisclosureNumber();
           sNumber =discl.getSequenceNumber();
        }else{
           dNumber = disclosureNumber;
           sNumber = seq;         
        }
           int reviseFlag = 0;
        if(dNumber!=null){              
               int projectCount= -1;
               String projectId=null;                 
                if(request.getSession().getAttribute("certValidPjtCount")!=null){
                    projectCount=Integer.parseInt(request.getSession().getAttribute("certValidPjtCount").toString());
                }
                WebTxnBean  webTxn = new WebTxnBean();
                HashMap hmData = new HashMap();
                hmData.put("coiDisclosureNumber", dNumber);
                hmData.put("sequenceNumber", sNumber);
                hmData.put("projectId", projectId);
                hmData.put("projectCount", projectCount);
                Hashtable projectDetailsList = (Hashtable) webTxn.getResults(request, "fnValidateCoiCertify", hmData);
                HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fnValidateCoiCertify");
                if(hmfinEntityList !=null && hmfinEntityList.size()>0){
                int count = Integer.parseInt(hmfinEntityList.get("count").toString());
                if(count <=0){
                 reviseFlag = 1;
                }                
                
                }
        }
                    if(reviseFlag==1 ||(request.getSession().getAttribute("hasEnteredCoiNonQnr")== null)){   
                             flag = 1;
                            perDiscl.setModuleCode(ModuleConstants.COI_EVENT_REVISION);
                            request.getSession().setAttribute("InPrgssCode","6");
                            perDiscl.setAcType("I");    
                            seq = new Integer(getNextSeqNumDisclosure(request, disclosureNumber));
                            seq = new Integer(seq.intValue() + 1);
                            request.getSession().setAttribute("currentSequence", seq); 
                            session.setAttribute("param2",seq);
                            perDiscl.setCoiDisclosureNumber(disclosureNumber);
                            perDiscl.setSequenceNumber(seq);
                            request.getSession().setAttribute("SequenceNumberInUpdateSession",seq);
                            // NEW MENU CHANGE 
                            request.getSession().setAttribute("COISeqNumber",seq);
                            request.getSession().setAttribute("hasEnteredCoiNonQnr","true");//checking whether was once entered or not                            
                            // NEW MENU CHANGE                    
                    }
          
      } 
      
      
        perDiscl.setPersonId(person.getPersonID());
        perDiscl.setDisclosureStatusCode(STATUS_IN_PROGRESS);
        perDiscl.setUpdateUser(loggedId);
        Integer reviewStatus=1;
        perDiscl.setReviewStatusCode(reviewStatus);
        perDiscl.setDisclosureDispositionCode(DISPOSITIONCODE);
        perDiscl.setModuleItemKey(null);
        if(flag == 1){
//         if(isDue) {
//            HashMap inputMap = new HashMap();
//            inputMap.put("personId", person.getPersonID());
//            webTxnBean.getResults(request, "updateDisclosureStatus", inputMap);
//        }
        webTxnBean.getResults(request, "coiPersonDisclosureCoiv2", perDiscl);
        session.setAttribute("isReviewDue",false);
        request.getSession().setAttribute("CREATEFLAG","Y");
        }        
        CoiDisclosureBean currDisclosure = new CoiDisclosureBean();
        if (!perDiscl.getAcType().equals("U")) {
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
            currDisclosure = coiCommonService.getCurrentDisclPerson(request);
            session.setAttribute("CurrDisclPer", currDisclosure);
            session.setAttribute("DisclNumber", currDisclosure.getCoiDisclosureNumber());
            session.setAttribute("DisclSeqNumber", currDisclosure.getSequenceNumber());
        } 
        String dNumber =null;
        Integer sNumber = -1;
        if(discl!=null ){
           dNumber = discl.getCoiDisclosureNumber();
           sNumber =discl.getSequenceNumber();
        }else{
           dNumber = disclosureNumber;
           sNumber = seq;         
        }
            session.setAttribute("DisclNumber", dNumber);
            session.setAttribute("DisclSeqNumber", sNumber);          
           request.removeAttribute("QnrNotCompleted");             
           session.setAttribute("contineWithoutQnr","true");
         return actionMapping.findForward(forward);
     }
     private void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();  //added by Vineetha
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
         if(coiDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
        }
        hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);

            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
    }

    }
     private void checkCOIPrivileges(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            int value = userDetailsBean.getCOIPrivilege(userName);
            session.setAttribute(PRIVILEGE, "" + userDetailsBean.getCOIPrivilege(userName));

            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);
            //Check whether to show link for View Pending Disclosures
            if (userDetailsBean.canViewPendingDisc(userName)) {
                session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        }
        session.setAttribute("person", personInfoBean);
    }
        public CoiDisclosureBean getCurrentDisclPerson(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCurrDiscl =
                (Hashtable) webTxnBean.getResults(request, "getCurrDisclPerCoiv2", hmreviewData);
        Vector vecDiscl = (Vector) htCurrDiscl.get("getCurrDisclPerCoiv2");
        CoiDisclosureBean discl = new CoiDisclosureBean();
        if (vecDiscl != null && vecDiscl.size() > 0) {
            for (int i = 0; i < vecDiscl.size(); i++) {
                discl = (CoiDisclosureBean) vecDiscl.get(0);
            }
        }
        return discl;
    }

    public void setCurrentDisclosure(HashMap hmData, HttpServletRequest request) throws Exception {
        CoiDisclosureBean discl = this.getCurrentDisclPerson(request);
        if (discl.getCoiDisclosureNumber() != null && !discl.getCoiDisclosureNumber().equals("")) {
            HttpSession session = request.getSession();
            session.setAttribute("DisclNumber", discl.getCoiDisclosureNumber());
            session.setAttribute("acType", "EDIT");
        }
    }
      public int getNextSeqNumDisclosure(HttpServletRequest request, String disclosureNumber) throws Exception {
        int seq = 0;
        HashMap hmData = new HashMap();
        hmData.put("disclosureNumber", disclosureNumber);

        WebTxnBean webTxnBean = new WebTxnBean();
        //   updateDisclosure(request);

        Hashtable htSyncData = (Hashtable) webTxnBean.getResults(request, "getMaxDisclSeqNumberCoiv2", hmData);

        HashMap hmSeq = (HashMap) htSyncData.get("getMaxDisclSeqNumberCoiv2");
        seq = Integer.parseInt(hmSeq.get("ll_Max").toString());
        return seq;
    }
        private String getNextDisclNum(HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htEntNo =
                (Hashtable) webTxnBean.getResults(request, "getNextPerDiscNum", null);

        String disclNumber =
                (String) ((HashMap) htEntNo.get("getNextPerDiscNum")).get("perDisclNumber");
        return disclNumber;
    }
     /**
     * Function to check Disclosure Exist or not
     * @param dynaValidatorForm
     * @param request
     * @return
     * @throws Exception
     */
    private boolean checkDisclosureExists(String personId, HttpServletRequest request) throws Exception {
        boolean disclosureExists = false;        
        WebTxnBean webTxn = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable discl = (Hashtable) webTxn.getResults(request, "getAnnualDisclnewCoiv2", hmData);
        Vector finDiscl = (Vector) discl.get("getAnnualDisclnewCoiv2");
        if (finDiscl != null && finDiscl.size() > 0) {
            disclosureExists = true;
        }
        return disclosureExists;
    }      
}
