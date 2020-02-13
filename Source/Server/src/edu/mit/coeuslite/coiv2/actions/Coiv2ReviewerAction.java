/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.utils.SessionConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import java.util.Iterator;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
/**
 *
 * @author indhulekha
 */
public class Coiv2ReviewerAction extends COIBaseAction {
    private static int ASSIGN_DISCLOSURE_REVIEW_COMPLETE=805;
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
         HttpSession session = request.getSession();
         String reviewerUserId = null;
         String reviewerPersonId = null;
         WebTxnBean webTxn = new WebTxnBean();
         DisclosureMailNotification subMailNotifixation = new  DisclosureMailNotification();
         String forwardPath = "success";

         if(session.getAttribute("reviewerUserId") != null) {
             reviewerUserId = (String)session.getAttribute("reviewerUserId");
         }

         HashMap inputMap = new HashMap();
         inputMap.put("userId", reviewerUserId);

         Hashtable htPersonId =(Hashtable)webTxn.getResults(request,"getPersonId",inputMap);
         Vector vcPersonId = (Vector)htPersonId.get("getPersonId");

         if(vcPersonId != null && vcPersonId.size() >0){
             DynaValidatorForm dynaForm = (DynaValidatorForm)vcPersonId.get(0);
            }

         PersonInfoBean person1 = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId1=person1.getPersonID();
        request.setAttribute("personId1", personId1);
         reviewerPersonId = request.getParameter("reviewerPersonId");
          if(reviewerPersonId == null){
             reviewerPersonId = person1.getPersonID();
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
        String personId = null;
        if(session.getAttribute("param3") != null){
            personId = (String)session.getAttribute("param3");
        }


        if (personId1!=null && !personId1.equalsIgnoreCase(personId))
        {
            request.setAttribute("ToShowMY", "true");
        }
        String disclosureNumber = null;
        Integer sequenceNumber = null;

        if(session.getAttribute("param1") != null) {
             disclosureNumber = (String)session.getAttribute("param1");
        }
        if(session.getAttribute("param2") != null) {
             sequenceNumber = Integer.parseInt(session.getAttribute("param2").toString());
        }

         if(disclosureNumber==null || (disclosureNumber!=null && disclosureNumber.trim().length()<=0)){
              disclosureNumber=(String)session.getAttribute("COIDiscNumber");
          }
         if (sequenceNumber == null ||sequenceNumber == 0){
                 sequenceNumber=Integer.parseInt(session.getAttribute("COISeqNumber").toString());
          }

            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);

           request.setAttribute("reviewingDisclNum", disclosureNumber);
           request.setAttribute("reviewingSeqNum", sequenceNumber);
           request.setAttribute("reviewerPersonId", reviewerPersonId);

          if (actionMapping.getPath().equals("/saveReviewAction")) {
              if(request.getParameter("reviewingDisclNum") != null) {
                disclosureNumber = request.getParameter("reviewingDisclNum").toString();
              }

              if(request.getParameter("reviewingSeqNum") != null) {
                sequenceNumber = Integer.parseInt(request.getParameter("reviewingSeqNum").toString());
              }
              if(request.getParameter("reviewerPersonId") != null) {
                reviewerPersonId = request.getParameter("reviewerPersonId").toString();
              }

              String returnToDiscl = "";
              String addNewComment = "";

              if(request.getParameter("returnToDiscl") != null) {
                  returnToDiscl = request.getParameter("returnToDiscl").toString();
              }

               if(request.getParameter("addNewComment") != null) {
                  addNewComment = request.getParameter("addNewComment").toString();
              }

              String comment = "";
              String restrictedView = "";
              comment = request.getParameter("ReviewerComment").toString();
              Integer entryNumber = getMaxEntryNumber(request, disclosureNumber);
              HashMap reviewMap = new HashMap();
              reviewMap.put("disclosureNumber", disclosureNumber);
              reviewMap.put("sequenceNumber", sequenceNumber);
              reviewMap.put("entityTypeCode", "2");
              reviewMap.put("entityNumber", entryNumber);
              reviewMap.put("comments", comment);
              if(request.getParameter("restrictedView") != null) {
                reviewMap.put("restrictedView", request.getParameter("restrictedView").toString());
              }else {
                reviewMap.put("restrictedView", "N");
              }
              reviewMap.put("updateUser", reviewerUserId);
              reviewMap.put("title", "Reviewer Comment");

              txnBean.getResults(request, "updateReviewerComment", reviewMap);

              if(returnToDiscl.equals("true")) {
                  forwardPath = "backToDiscl";
              }else if(addNewComment.equals("true")){
                  forwardPath = "reviewerAction";
              }else {
                   forwardPath = "success";
              }
          }

        if (actionMapping.getPath().equals("/completeReviewAction")) {
            Hashtable htrecommendedAction = (Hashtable) txnBean.getResults(request, "getCOIRecommendedAction", null);
            Vector vcRecommendedAction = (Vector)htrecommendedAction.get("getCOIRecommendedAction");

            if(vcRecommendedAction != null && vcRecommendedAction.size() > 0) {
                request.setAttribute("recommendedActionList",vcRecommendedAction);
            }
            String reviewComplete = "";
            if(request.getParameter("reviewComplete") != null) {
                reviewComplete = request.getParameter("reviewComplete").toString();
            }

            String reviewCancelled = "";
             if(request.getParameter("calcelReview") != null) {
                reviewCancelled = request.getParameter("calcelReview").toString();
            }

            if(reviewComplete.equals("true")) {
                String recommendedAction = "";
                String isReviewComplete = "Y";
                if(request.getParameter("reviewingDisclNum") != null) {
                    disclosureNumber = request.getParameter("reviewingDisclNum").toString();
                  }

                  if(request.getParameter("reviewingSeqNum") != null) {
                    sequenceNumber = Integer.parseInt(request.getParameter("reviewingSeqNum").toString());
                  }
                  if(request.getParameter("reviewerPersonId") != null) {
                    reviewerPersonId = request.getParameter("reviewerPersonId").toString();
                  }

                if(request.getParameter("recommendedAction") != null) {
                    recommendedAction = request.getParameter("recommendedAction");
                }
                  for(int i=0;i<vcRecommendedAction.size();i++)
                  {
                      ComboBoxBean bean=(ComboBoxBean)vcRecommendedAction.get(i);
                      if(bean.getCode().equals(recommendedAction))
                      {
                          String reviewC=bean.getDescription();
                          session.setAttribute("reviewC", reviewC);
                      }
                  }
                HashMap raMap = new HashMap();
                raMap.put("disclosureNumber", disclosureNumber);
                raMap.put("sequenceNumber", sequenceNumber);
                raMap.put("personId", reviewerPersonId);
                raMap.put("reviewComplete", isReviewComplete);
                raMap.put("recommendedAZction", recommendedAction);
                raMap.put("updateUser", reviewerUserId);

               txnBean.getResults(request, "updateRecommendedAction", raMap);
               String loggedId= (String)session.getAttribute("loggedinpersonid");
               HashMap hmMap = new HashMap();

              hmMap.put("disclosureNumber", disclosureNumber);
              hmMap.put("seqNumber", sequenceNumber);

                Hashtable htResult =(Hashtable)txnBean.getResults(request,"isCOIReviewComplete",hmMap);
            HashMap  hmResult = (HashMap)htResult.get("isCOIReviewComplete");
            int isReviewCompleted = Integer.parseInt(hmResult.get("isComplete").toString());

                if(isReviewCompleted == 1) {
                hmMap.clear();
                hmMap.put("disclosureNumber", disclosureNumber);
                hmMap.put("sequenceNumber", sequenceNumber);
                hmMap.put("reviewStatusCode", 5);
                hmMap.put("updateUser", person1.getUserId());
                txnBean.getResults(request,"updateAssignReviewStatus",hmMap);
                Coiv2AssignDisclUserBean assignDisclUserBean = new Coiv2AssignDisclUserBean();
                session = request.getSession();
                SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
                PersonInfoBean userInfoBean = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
                assignDisclUserBean.setUnitNumber(userInfoBean.getHomeUnit());
                String setDisclosureNumberData = "Disclosure Number : "+ disclosureNumber;
                 webTxn = new WebTxnBean();
                 HashMap hmData2=new HashMap();
                 Vector reporterpre = null;
                 reporterpre = setstatus.getReporterByDiscl(disclosureNumber, request);
                 person1 = new PersonInfoBean();
                  if (reporterpre != null && reporterpre.size() > 0) {
                        person1 = (PersonInfoBean) reporterpre.get(0);
                    }
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
                    String userName = "User Name : ";
                    userName = userName + person1.getUserName();
                    String disclosureStatus = "Disclosure Status : ";
                    disclosureStatus = disclosureStatus+CurDisclosureBean.getDisclosureStatus();
                     Vector vecRecipients = new Vector();
                     Vector viewers = null;
                      viewers = setstatus.getAdminCheck(request,assignDisclUserBean.getUnitNumber());
                       if (viewers != null && viewers.size() > 0) {

                        for (Iterator it = viewers.iterator(); it.hasNext();) {
                            PersonRecipientBean ob = (PersonRecipientBean) it.next();
                            vecRecipients.add(ob);
                        }
                    }
                       MailMessageInfoBean mailMsgInfoBean = null;
                       try{
                     boolean  mailSent;
                     mailMsgInfoBean = subMailNotifixation.prepareNotification(ASSIGN_DISCLOSURE_REVIEW_COMPLETE);
                     if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                         mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
                                   mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");

                          mailSent = subMailNotifixation.sendNotification(mailMsgInfoBean);

                    }
                       }catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
                    }catch (Exception ex){
                  UtilFactory.log(ex.getMessage());
              }
                }
                if(reviewerPersonId.equals(personId1)){
                request.setAttribute("update", true);
                forwardPath = "completeReview";
               }
                else{
                forwardPath = "adminReview";
                }
            }

            else if(reviewCancelled.equals("true")) {
                forwardPath = "cancelReview";
            }else {
             Vector details=new Vector();
                HashMap hmMap1 = new HashMap();
                hmMap1.put("coiDisclosureNumber", disclosureNumber);
                hmMap1.put("coiSequenceNumber", sequenceNumber);
                Hashtable htTble =(Hashtable)webTxn.getResults(request,"getRecmndDetails",hmMap1);
                Vector revDetails=(Vector)htTble.get("getRecmndDetails");
                request.setAttribute("details", revDetails);

             forwardPath = "success";
            }
        }

         return actionMapping.findForward(forwardPath);
     }


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
        HttpSession session = request.getSession();
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
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
    }

     private void setSavedMenuData(String disclosure_Number,Integer seqNumber,String persnId,HttpServletRequest request) throws Exception {
           CoiDisclosureBean currDisc = (CoiDisclosureBean) request.getSession().getAttribute("disclosureBeanSession");
                     Integer sequenceN=0;
                     if (disclosure_Number == null){
                    if(currDisc!=null){
                      if(currDisc.getCoiDisclosureNumber() != null) {
                     disclosure_Number =(String)currDisc.getCoiDisclosureNumber();
                      }
                       if(currDisc.getSequenceNumber() != null) {
                       sequenceN = (Integer) currDisc.getSequenceNumber();
                       }
                      }
                     }
                      if(seqNumber==null){
                          seqNumber= (Integer) request.getSession().getAttribute("currentSequence");
                      }
                    if(disclosure_Number  == null || (disclosure_Number!=null && disclosure_Number.trim().length()<=0)){
                        disclosure_Number=(String)request.getSession().getAttribute("COIDiscNumber");
                    }
            coiMenuDataSaved(disclosure_Number,seqNumber,persnId,request);//coi saved menu
        }

     private Integer getMaxEntryNumber(HttpServletRequest request, String disclNumber) throws Exception {
        int newMaxEntryNumber = 0;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmMaxEntryNumber = new HashMap();
        CoiDisclosureBean currDisclosure = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");
        hmMaxEntryNumber.put("coiDisclosureNumber", disclNumber);
        //Get max entry number
        Hashtable htMaxEntryNumber = (Hashtable) webTxnBean.getResults(request, "getMaxEntryNumberCoiv2", hmMaxEntryNumber);
        hmMaxEntryNumber = (HashMap) htMaxEntryNumber.get("getMaxEntryNumberCoiv2");
        if (hmMaxEntryNumber != null && hmMaxEntryNumber.size() > 0) {
            String maxEntryNumber = (String) hmMaxEntryNumber.get("maxEntryNumber");
            if (maxEntryNumber != null) {
                newMaxEntryNumber = Integer.parseInt(maxEntryNumber) + 1;
            }
        }
        return new Integer(newMaxEntryNumber);
    }

}
