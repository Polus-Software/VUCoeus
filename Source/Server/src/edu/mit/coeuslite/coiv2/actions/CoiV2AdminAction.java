/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.dartmouth.coeuslite.coi.beans.FinEntMatrixBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2AssignDisclUserBean;
import edu.mit.coeuslite.coiv2.formbeans.CoiSearchBean;
import edu.mit.coeuslite.coiv2.services.CoiAttachmentService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.Format;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.coiv2.beans.CoiV2ReviewCode;
/**
 *
 * @author Mr.Roshin Roy K
 * for showing all disclosure,assign disclosure to user view,assign disclosure to viewer,
 * admin search window view,searching disclosure.
 */

    public class CoiV2AdminAction extends COIBaseAction {

       private static int ASSIGN_DISCLOSURE_ACTION_CODE = 803;
       public static final int RIGHTTOVIEW=1;
       private boolean flag=false;
       private static final String USER ="user"; 
    public ActionForward performExecuteCOI(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
         getDisclosureDet(request);
        String forward = "fail";
        HttpSession session = request.getSession(true);
        UserInfoBean userBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        String logInUserId = userBean.getUserId(); 
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();        
        String viewOnly = (String)request.getSession().getAttribute("param4");
         request.getSession().removeAttribute("frmEventDiscl");
         request.getSession().removeAttribute("historyView");
         request.getSession().removeAttribute("fromPending");
         request.getSession().removeAttribute("fromReviewlist");
         request.getSession().removeAttribute("disclosureNotesData");
         request.getSession().removeAttribute("projectList");
         request.getSession().removeAttribute("isReviewer");
         request.getSession().removeAttribute("reviewerUserId");
         request.getSession().removeAttribute("noQuestionnaireForModule");
         
         if(request.getSession().getAttribute("ownDisclosure") != null) {
            request.getSession().removeAttribute("ownDisclosure");
        }
        request.getSession().removeAttribute("fromViewCurrent");
        request.getSession().removeAttribute("fromViewerAction");
    DisclosureMailNotification subMailNotifixation = new  DisclosureMailNotification();

    //to remove session to show print on left menu
    request.getSession().removeAttribute("checkPrint");
   // SubcontractMailNotification subMailNotifixation=new SubcontractMailNotification();
        //  for showing all disclosure in the tree list    
        if (mapping.getPath().equals("/showAllDiscl")) {          
            request.getSession().removeAttribute("fromReviewlist");
            Integer sequenceNumber = (Integer)request.getSession().getAttribute("param2");
            request.getSession().setAttribute("frmShowAllReviews", true);
           WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = new Vector();
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                setFEFlag(title,coiDisclosureBean);
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            setFEFlag(coiDisclosureBean,coiDisclosureBean);
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
            }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            request.setAttribute("entityNameList", entityName);
            //        request.setAttribute("historyDetView", historyDet);
            request.setAttribute("pjtEntDetView", historyDet);
            forward = "success";
        }
    //To display the list of all Annual Reviews that have been submitted for review
      if (mapping.getPath().equals("/showAllAnnualDiscl")) {          
          request.getSession().removeAttribute("fromReviewlist");
          WebTxnBean webTxn = new WebTxnBean();
            Vector pendingAnnualDisclDet = new Vector();
            Vector entityName = new Vector();
            HashMap hmap = new HashMap();
            hmap.put("userId",logInUserId);
            Hashtable pendingAnnualCoiDiscl = (Hashtable) webTxn.getResults(request, "getAllOpenDisclosure", hmap);
            pendingAnnualDisclDet = (Vector) pendingAnnualCoiDiscl.get("getAllOpenDisclosure");

            if (pendingAnnualDisclDet != null && pendingAnnualDisclDet.size() > 0) {
                for (int i = 0; i < pendingAnnualDisclDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) pendingAnnualDisclDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                setFEFlag(title,coiDisclosureBean);
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            setFEFlag(coiDisclosureBean,coiDisclosureBean);
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
            }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            request.setAttribute("entityNameList", entityName);
            //        request.setAttribute("historyDetView", historyDet);
            request.setAttribute("pjtEntDetView", pendingAnnualDisclDet);


       //     request.setAttribute("pendingAnnualDiscl", pendingAnnualDisclDet);
            forward = "successToAnnualPendindCOIDiscl";
      }
          if (mapping.getPath().equals("/reviewEventFinEntity")) {
                  HashMap hmFinData=new HashMap();
                  DynaValidatorForm dynaForm=new DynaValidatorForm();
                  getFinEntityDetail(hmFinData,dynaForm,request,mapping);
           Vector questionDet = null;
           HashMap hmData = new HashMap();
           String disclosureNumber="";
           WebTxnBean webTxn=new WebTxnBean();
           disclosureNumber = (String)request.getSession().getAttribute("param1");
           Integer sequenceNumber = (Integer)request.getSession().getAttribute("param2");
           String personId = (String)request.getSession().getAttribute("param3");
           hmData.put("coiDisclosureNumber", disclosureNumber);
           hmData.put("sequenceNumber", sequenceNumber);
           hmData.put("personId", personId);
           Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
           questionDet = (Vector) questionData.get("getQnsAns");
           if (questionDet != null && questionDet.size() > 0) {
               request.setAttribute("questionDetView", questionDet);
           }
           CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();
               Vector docType = coiAttachmentService.getDocumentType(request);
                            request.setAttribute("DocTypes", docType);
                        String viewExists=(String)request.getSession().getAttribute("approvedView");
                        if (personId == null) {
                        PersonInfoBean person = (PersonInfoBean)request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
                        personId = person.getPersonID();
                        }
                        String disclosureHistory = (String)request.getSession().getAttribute("param6");
                        CoiAttachmentService attachmentService = CoiAttachmentService.getInstance();
                        Vector attDet = null;
                        if (disclosureHistory == null) {
                        CoiDisclosureBean lCoiDisclosureBean = getApprovedDisclosureBean(personId, request);
                        disclosureNumber = lCoiDisclosureBean.getCoiDisclosureNumber();
                        sequenceNumber = lCoiDisclosureBean.getSequenceNumber();
                        setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);
                      /*  
                        boolean userHaveRight = gettingRightsCoiV2Service.getCoiUserPrivilege(request, CoiConstants.MAINTAIN_COI_DISCL_ATTACHMENTS_STR);
                        if (userHaveRight) {
                            request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
                            request.setAttribute("isViewer", "VIEWER");
                            request.setAttribute("operationType", "MODIFY");
                           docType = coiAttachmentService.getDocumentType(request);
                            request.setAttribute("DocTypes", docType);
                            request.setAttribute("userHasRight", true);
                        } else {
                            request.setAttribute("userHasRight", false);
                        }
                          if (viewOnly != null && viewOnly.equals("ViewOnly")) {
                            request.setAttribute("userHasRight", false);
                        }
                        if(viewExists!=null){
                            request.getSession().setAttribute("SequenceNumberInUpdateSession", String.valueOf(sequenceNumber));
                            request.setAttribute("isViewer", "VIEWER");
                            request.setAttribute("operationType", "MODIFY");
                             docType = coiAttachmentService.getDocumentType(request);
                            request.setAttribute("DocTypes", docType);
                            request.setAttribute("userHasRight", true);

                         }
                        */
                        attDet = attachmentService.getUploadDocumentForPerson(disclosureNumber, sequenceNumber, personId);
                        if (attDet != null && attDet.size() > 0) {
                            request.setAttribute("attachmentList", attDet);
                            request.getSession().setAttribute("attachmentListInsession", attDet);
                        } else {
                            request.setAttribute("message", false);
                        }
                             request.setAttribute("option", "attachments");
                        }
                        forward = "successToEventDisclFinEntity";
      }

    //To display the list of all Annual Reviews that have been submitted for review ends.
          if (mapping.getPath().equals("/showDisclReview")) {
             WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = new Vector();
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
            }
            if (entityName.size() == 0) {
                request.setAttribute("message", false);
            }
            request.setAttribute("entityNameList", entityName);
            //        request.setAttribute("historyDetView", historyDet);
            request.setAttribute("pjtEntDetView", historyDet);
            //request.getSession().setAttribute("fromReviewlist",true);
            forward = "successToDiscl";
      }
        //To display the list of all Event Reviews that have been submitted for review
      if (mapping.getPath().equals("/showAllPendingEventDiscl")) {          
            WebTxnBean webTxn = new WebTxnBean();
            Vector pendingEventDisclDet = new Vector();
            Vector entityName = new Vector();
            HashMap hmap = new HashMap();
            Hashtable pendingEventCoiDiscl = (Hashtable) webTxn.getResults(request, "getAllPendingEventDiscl", hmap);
            pendingEventDisclDet = (Vector) pendingEventCoiDiscl.get("getAllPendingEventDiscl");
            request.setAttribute("pendingEventDiscl", pendingEventDisclDet);
//            if(mapping.getPath().equals("/showAllDiscl")){
//            request.getSession().setAttribute("frmEventDiscl", true);
            request.getSession().setAttribute("fromReviewlist",true);
            forward = "successToPendingEventCOIDiscl";
      }

    //To display the list of all Event Reviews that have been submitted for review ends.
        // show assign disclosure to user screen, only viewers listed in user list.
        if (mapping.getPath().equals("/assignDisclToUser")) {
            WebTxnBean webTxn = new WebTxnBean();
            HashMap hmData = new HashMap();            
            String disclosureNumber = (String) session.getAttribute("param1");
            Integer sequenceNumber = (Integer) session.getAttribute("param2");
            String personId = (String) session.getAttribute("param3");
            if(personId==null){
               PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
               personId=person.getPersonID();
             }
            String loggedInPersonIdForCoi=(String)session.getAttribute("loggedInPersonIdForCoi");
            if (loggedInPersonIdForCoi!=null && !loggedInPersonIdForCoi.equalsIgnoreCase(personId))
            {
                request.setAttribute("ToShowMY", "true");
            }
            LoadHeaderDetails(personId,request);
            Vector userDet = new Vector();            
            hmData.put("coiDisclNum", disclosureNumber);
             hmData.put("seqNumber", sequenceNumber);
            Hashtable users = (Hashtable) webTxn.getResults(request, "getCoiUserRoles", hmData);
            userDet = (Vector) users.get("getCoiUserRoles");
            request.setAttribute("usersList", userDet);
            request.setAttribute("ReviewActAssignViewr", true);//to check "Assign Viewers" menu selected            
            coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);//check Coi Saved menu.
            setApprovedDisclosureDetails(disclosureNumber, sequenceNumber, personId, request);

             HashMap inpuMap = new HashMap();

            inpuMap.put("disclosureNumber", disclosureNumber);
            inpuMap.put("seqNumber", sequenceNumber);
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            inpuMap.put("userId", person.getUserId().toUpperCase());
            String usrId = person.getUserId();
            session.setAttribute("reviewerUserId", usrId);
            Hashtable htResult =(Hashtable)webTxn.getResults(request,"isReviewer",inpuMap);
            HashMap  hmResult = (HashMap)htResult.get("isReviewer");
            int isReviewer = Integer.parseInt(hmResult.get("li_count").toString());

            if(isReviewer == 1) {
                session.setAttribute("isReviewer", true);
                session.setAttribute("reviewerUserId", usrId);
            }
            
           
           Vector details=new Vector();
            HashMap hmMap1 = new HashMap();
              hmMap1.put("coiDisclosureNumber", disclosureNumber);
              hmMap1.put("coiSequenceNumber", sequenceNumber);

                Hashtable htTble =(Hashtable)webTxn.getResults(request,"getRecmndDetails",hmMap1);
                Vector revDetails=(Vector)htTble.get("getRecmndDetails");
            request.setAttribute("details", revDetails);
            //request.setAttribute("usersList", ViewerList);
            forward = "assignDisclToUser";
        }

 if (mapping.getPath().equals("/removeDisclToUser")) {
            WebTxnBean webTxn = new WebTxnBean();           
            String userName = "";
            userName=(String)request.getParameter("userName");
            String persnId = request.getParameter("personId");
            Coiv2AssignDisclUserBean assignDisclUserBean = new Coiv2AssignDisclUserBean();
            String disclosureNumber = (String) session.getAttribute("param1");
                Integer sequenceNumber = (Integer) session.getAttribute("param2");
                assignDisclUserBean.setCoiDisclosureNumber(disclosureNumber);
                assignDisclUserBean.setCoiSequenceNumber(sequenceNumber);
                assignDisclUserBean.setUserId(userName);
                assignDisclUserBean.setPersonId(persnId);

                assignDisclUserBean.setAcType("R");//remove
try {
                    webTxn.getResults(request, "updateAssDisclUser", assignDisclUserBean);
                    webTxn.getResults(request, "updateDisclReviewerDetails", assignDisclUserBean);
                    request.setAttribute("message", true);
} catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
            forward = "savecomplete";
        }

//assign disclosure to user
        if (mapping.getPath().equals("/saveDisclToUser")) {           
                String userId = "";   
//        disclNo = string.split(":");
                Coiv2AssignDisclUserBean assignDisclUserBean = new Coiv2AssignDisclUserBean();
//        assignDisclUserBean.setCoiDisclosureNumber(disclNo[0]);
//        String dis = disclNo[2];
//        assignDisclUserBean.setCoiSequenceNumber(Integer.parseInt(dis));
                String persnId = request.getParameter("personId");                
                userId=(String)request.getParameter("UserName");
                SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
                String disclosureNumber = (String) session.getAttribute("param1");
                Integer sequenceNumber = (Integer) session.getAttribute("param2");
                assignDisclUserBean.setCoiDisclosureNumber(disclosureNumber);
                assignDisclUserBean.setCoiSequenceNumber(sequenceNumber);
                assignDisclUserBean.setUserId(userId);
                Date update = new Date();
                assignDisclUserBean.setUpdateTime(update);
                PersonInfoBean userInfoBean = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
                assignDisclUserBean.setUpdateUser(userInfoBean.getUserId());

                assignDisclUserBean.setAcType("I");

                assignDisclUserBean.setPersonId(persnId);
                assignDisclUserBean.setAssignedDate(update);

                WebTxnBean webTxnBean = new WebTxnBean();
             /*
                 UserDetailsBeanCoiV2 userDetailsBean = new UserDetailsBeanCoiV2();
                boolean isViewer = userDetailsBean.isViewer(userId);                
                if(isViewer){
                    assignDisclUserBean.setRoleId(80);
                }else {
                    assignDisclUserBean.setRoleId(79);
                }
              */
                assignDisclUserBean.setRoleId(1);
                try {
                    webTxnBean.getResults(request, "updateAssDisclUser", assignDisclUserBean);
                    webTxnBean.getResults(request, "updateDisclReviewerDetails", assignDisclUserBean);
                    request.setAttribute("message", true);

                    ///-------edit Start @@@@@@@@---------


//   WebTxnBean webTxn = new WebTxnBean();
//          HashMap hmData = new HashMap();
//          Vector historyDet = null;
//          Vector entityName = new Vector();
//          Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);

               // PersonInfoTxnBean personTxnBean = new PersonInfoTxnBean();
               // PersonInfoFormBean personBean = null;
                //int totalReviewers = reviewerList.size();

               // PersonRecipientBean recipient = new PersonRecipientBean();
               // personBean = personTxnBean.getPersonInfo(protocolReviewerInfoBean.getPersonId()) ;
               // recipient.setEmailId(personBean.getEmail());

            String setDisclosureNumberData = "Disclosure Number : "+ disclosureNumber;
            WebTxnBean webTxn = new WebTxnBean();
            PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
            String personId =person.getPersonID();
            HashMap hmData2=new HashMap();
                    Vector reporterpre = null;
                    reporterpre = setstatus.getReporterByDiscl(disclosureNumber, request);
                PersonInfoBean person1 = new PersonInfoBean();

                    if (reporterpre != null && reporterpre.size() > 0) {
                        person1 = (PersonInfoBean) reporterpre.get(0);
                    }


             //personId=person1.getPersonID();
            //CoiDisclosureBean CurDisclosureBean = new CoiDisclosureBean();
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


                Vector vecRecipients = new Vector();
                //vecRecipients.add(recipient);

                  String viewer = "";
                    Vector viewers = null;

                   // viewers = setstatus.getViewersByDiscl(assignDisclUserBean.getCoiDisclosureNumber(), request);//getViewersByDisclosure
                    viewers = setstatus.getViewerByDisclosure(assignDisclUserBean.getCoiDisclosureNumber(), request);

                    if (viewers != null && viewers.size() > 0) {

                        for (Iterator it = viewers.iterator(); it.hasNext();) {
                            PersonRecipientBean ob = (PersonRecipientBean) it.next();
                            vecRecipients.add(ob);
                        }
                    }
                   // vecRecipients.add(viewer);


                MailMessageInfoBean mailMsgInfoBean = null;
                // Send mail to newly added reviewer
                try{
                    boolean  mailSent;
                   // mailMsgInfoBean = subMailNotifixation.prepareNotification(ASSIGN_DISCLOSURE_ACTION_CODE,"8", sequenceNumber);

                        // the parallel Code with action Id
                      mailMsgInfoBean = subMailNotifixation.prepareNotification(ASSIGN_DISCLOSURE_ACTION_CODE);
                    if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                         mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
                                   mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");

                          mailSent = subMailNotifixation.sendNotification(mailMsgInfoBean);

                    }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
              } catch (Exception ex){
                  UtilFactory.log(ex.getMessage());
              }
                ///subMailNotifixation.sendNotification(int actionId, String moduleItemKey, int moduleItemKeySequence, MailMessageInfoBean mailMessageInfoBean)

///--------------------------------------------------------edit end---------
//for mail-----------Ros Edit
//                    UtilFactory.log("===================Assign Disclosure mail start===================" + new Date());
//                    //for mail: one disclosure is assign to a viewer
//                    SetMailAttributes att = new SetMailAttributes();
//                    CoeusMailService cms = new CoeusMailService();
//                    att.setAttachmentPresent(false);
//                    att.setSubject("Test Message from coeus");
//                    att.setFrom("roshin@invisionlabs.com");
//                    //for getting viewers email ids
//                    String viewer = "";
//                    Vector viewers = null;
//                    SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
//                    viewers = setstatus.getViewersByDiscl(assignDisclUserBean.getCoiDisclosureNumber(), request);
//                    if (viewers != null && viewers.size() > 0) {
//                        for (Iterator it = viewers.iterator(); it.hasNext();) {
//                            PersonInfoBean object = (PersonInfoBean) it.next();
//                            viewer += object.getEmail() + ",";
//                        }
//                    }
//                    att.setTo(viewer);
//                    att.setMessage("Coeus Welcomes You \n Disclosure Assigned Successful...");
//                    cms.sendMessage(att);
//                    System.out.println("Done");
//                    UtilFactory.log("===================Assign Disclosure mail end===================" + new Date());



                } catch (Exception e) {
                    //if it is already assigned;
                     String exp = e.toString();
                    if (exp.indexOf("ORA-00001") > 0) {
                     request.setAttribute("message", true);
                }
            }
            
            CoiV2ReviewCode bean = new CoiV2ReviewCode();
                bean.setDisclosureNumber(disclosureNumber);
                bean.setSequenceNumber(sequenceNumber);
                PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
                bean.setUpdateUser(person.getUserId());
            Integer revStatus=4;
            bean.setReviewStatusCode(revStatus);
            WebTxnBean webTxn = new WebTxnBean();
            webTxn.getResults(request, "updateAssignReviewStatus", bean);

            forward = "savecomplete";
        }
        if (mapping.getPath().equals("/popsequence")) {
            String disNumber = request.getParameter("disNo");
            request.setAttribute("disNum", disNumber);
            String seleindx = request.getParameter("seleindx");
            request.setAttribute("seleindx", seleindx);
            int ind = disNumber.indexOf(":", 0);
            String select = disNumber.substring(0, ind);
            disNumber = select;
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = new Vector();
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector seqName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (coiDisclosureBean.getCoiDisclosureNumber().equalsIgnoreCase(disNumber)) {
                        seqName.add(coiDisclosureBean);
                    }
                }
            }
            request.setAttribute("seqNameList", seqName);



            WebTxnBean webTxn1 = new WebTxnBean();
            Vector historyDet1 = new Vector();
            HashMap hmData1 = new HashMap();
            hmData1.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData1 = (Hashtable) webTxn1.getResults(request, "getAllDisclVersions", hmData1);
            historyDet1 = (Vector) historyData1.get("getAllDisclVersions");
            if (historyDet1 != null && historyDet1.size() > 0) {
                for (int i = 0; i < historyDet1.size(); i++) {
                    Boolean isTitlePresent1 = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                isTitlePresent1 = true;
                                break;
                            }
                        }
                        if (isTitlePresent1 == false) {
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
                request.setAttribute("entityNameList", entityName);
            }


            Vector userDet = null;
            Hashtable users = (Hashtable) webTxn.getResults(request, "getUsers", hmData);
            userDet = (Vector) users.get("getUsers");
            request.setAttribute("usersList", userDet);

            forward = "assignDisclToUser";
        }

//show admin search window
        if (mapping.getPath().equals("/searchDisclosure")) {
            WebTxnBean webTxn = new WebTxnBean();
            Vector historyDet = null;
            HashMap hmData = new HashMap();
            hmData.put("userId",logInUserId);
            Vector entityName = new Vector();
            Hashtable historyData = (Hashtable) webTxn.getResults(request, "getAllDisclVersions", hmData);
            historyDet = (Vector) historyData.get("getAllDisclVersions");
            if (historyDet != null && historyDet.size() > 0) {
                for (int i = 0; i < historyDet.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureBean coiDisclosureBean = (CoiDisclosureBean) historyDet.elementAt(i);
                    if (entityName != null && entityName.size() > 0) {
                        for (Iterator it = entityName.iterator(); it.hasNext();) {
                            CoiDisclosureBean title = (CoiDisclosureBean) it.next();
                            if (title.getCoiDisclosureNumber().equalsIgnoreCase(coiDisclosureBean.getCoiDisclosureNumber())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            entityName.add(coiDisclosureBean);
                        }
                    } else {
                        entityName.add(coiDisclosureBean);
                    }
                }
            }
            if (entityName != null && entityName.size() > 0) {
                request.setAttribute("entityNameList", entityName);
            }
            Vector userDet = null;
            Hashtable users = (Hashtable) webTxn.getResults(request, "getUsers", hmData);
            userDet = (Vector) users.get("getUsers");
            request.setAttribute("usersList", userDet);

            hmData = new HashMap();
            Vector DisposStatusList = null;
            Vector DisclStatusList = null;
            Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclosureDispositionStatus", hmData);
            DisposStatusList = (Vector) statusData.get("getAllCoiDispositionStatus");
            DisclStatusList = (Vector) statusData.get("getAllCoiDisclStatus");
            if (DisclStatusList != null && DisclStatusList.size() > 0) {
                request.setAttribute("DisclStatusListView", DisclStatusList);
            }
            if (DisposStatusList != null && DisposStatusList.size() > 0) {
                request.setAttribute("DisposStatusListView", DisposStatusList);
            }            
             HashMap eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");

            Vector diclosureEventList = new Vector();
            ComboBoxBean comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_AWARD));
            comboBean.setDescription("Award");
            diclosureEventList.add(comboBean);
            comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_PROPOSAL));
            comboBean.setDescription("Proposal");
            diclosureEventList.add(comboBean);
            comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_PROTOCOL));
            comboBean.setDescription("Protocol");
            diclosureEventList.add(comboBean);
            comboBean = new ComboBoxBean();
            comboBean.setCode(String.valueOf(ModuleConstants.COI_EVENT_OTHER));
            comboBean.setDescription("Other");
            diclosureEventList.add(comboBean);
            request.setAttribute("disclosureEvent", diclosureEventList);
            forward = "searchDisclosure";
        }
//for search disclosure
        if (mapping.getPath().equals("/searchDisclByElements")) {
            Boolean samePage = false;
            CoiSearchBean coiSearchBean = (CoiSearchBean) actionForm;
            String disNumber = coiSearchBean.getDiscl();
            if (disNumber.equalsIgnoreCase("select")) {
                disNumber = "%";
            }
            if (disNumber.equalsIgnoreCase("")) {
                disNumber = "%";
            }
            String user = coiSearchBean.getUser();
            if (user.equalsIgnoreCase("select")) {
                user = "%";
            }
            String dispStsCode = coiSearchBean.getDispositionStatus();
            if (dispStsCode.equalsIgnoreCase("select")) {
                dispStsCode = "%";
            }
            String disclStsCode = coiSearchBean.getDisclosureStatus();
            if (disclStsCode.equalsIgnoreCase("select")) {
                disclStsCode = "%";
            }
            String disclosureEvent = coiSearchBean.getDisclosureEvent();
            if (disclosureEvent.equalsIgnoreCase("select")) {
                disclosureEvent = "%";
            }
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Format formatter = new SimpleDateFormat("dd-MMM-yy");
            String updateTimestamp = coiSearchBean.getSubDate();
            if (updateTimestamp.equalsIgnoreCase("")) {
                updateTimestamp = "%";
            } else {
                Date update = new Date();
                try {
                    update = dateFormat.parse(updateTimestamp);
                    updateTimestamp = formatter.format(update);
                    updateTimestamp = updateTimestamp.toUpperCase();
                } catch (Exception e) {
                    request.setAttribute("dateMessage", false);
                    samePage = true;
                }
            }
            String updateTimestampEnd = coiSearchBean.getSubEndDate();
            if (updateTimestampEnd.equalsIgnoreCase("")) {
                updateTimestampEnd = "%";
            } else {
                Date exp = new Date();
                try {
                    exp = dateFormat.parse(updateTimestampEnd);
                    updateTimestampEnd = formatter.format(exp);
                    updateTimestampEnd = updateTimestampEnd.toUpperCase();
                } catch (Exception e) {
                    request.setAttribute("dateEndMessage", false);
                    samePage = true;
                }
            }
            WebTxnBean webTxn = new WebTxnBean();
            Vector searchDet = null;
            HashMap hmData = new HashMap();
            if (!samePage) {
                if (!disNumber.equalsIgnoreCase("%") || !user.equalsIgnoreCase("%") || !dispStsCode.equalsIgnoreCase("%") || !disclStsCode.equalsIgnoreCase("%") || !disclosureEvent.equalsIgnoreCase("%") || !updateTimestamp.equalsIgnoreCase("%")) {
                    if (updateTimestamp.equalsIgnoreCase("%")) {
                        hmData.put("personId", user);
                        hmData.put("coiDisclosureNumber", disNumber);
                        hmData.put("disclosureDispositionCode", dispStsCode);
                        hmData.put("disclosureStatusCode", disclStsCode);
                        hmData.put("moduleCode", disclosureEvent);
                        hmData.put("updateTimestamp", updateTimestamp);
                        Hashtable searchData = (Hashtable) webTxn.getResults(request, "getSearchedDiscAdmin", hmData);
                        searchDet = (Vector) searchData.get("getSearchedDiscAdmin");
                        if (searchDet != null && searchDet.size() > 0) {
                            request.setAttribute("searchDetView", searchDet);
                        } else {
                            request.setAttribute("message", false);
                        }
                        request.setAttribute("noselectionmessage", true);
                    } else {
                        hmData.put("personId", user);
                        hmData.put("coiDisclosureNumber", disNumber);
                        hmData.put("disclosureDispositionCode", dispStsCode);
                        hmData.put("disclosureStatusCode", disclStsCode);
                        hmData.put("moduleCode", disclosureEvent);
                        hmData.put("updateTimestamp", updateTimestamp);
                        hmData.put("updateTimestampEnd", updateTimestampEnd);
                        Hashtable searchData = (Hashtable) webTxn.getResults(request, "getSearchedDiscDateAdmin", hmData);
                        searchDet = (Vector) searchData.get("getSearchedDiscDateAdmin");
                        if (searchDet != null && searchDet.size() > 0) {
                            request.setAttribute("searchDetView", searchDet);
                        } else {
                            request.setAttribute("message", false);
                        }
                        request.setAttribute("noselectionmessage", true);
                    }
                } else {
                    request.setAttribute("noselectionmessage", false);
                }
            }
            if (samePage) {
                request.setAttribute("noselectionmessage", false);
                request.removeAttribute("message");
            }
            forward = "searchDisclByElements";
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


    private void getDisclosureDet(HttpServletRequest request) throws Exception {

        Vector statusDispDet = new Vector();
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }
         String personId = (String) session.getAttribute("param3");
          String disclosureNumber = (String) session.getAttribute("param1");
//chnges made by Vineetha
           Integer sequenceNumber = null;
           sequenceNumber =  (Integer) request.getSession().getAttribute("currentSequence");
           if(null == sequenceNumber){
              sequenceNumber =  (Integer) session.getAttribute("param2");
           }
         if(personId==null){
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        session.setAttribute("person", person);
         }
        Vector apprvdDiscl = new Vector();

        /** Gets Latest Version of the disclosure for the logged in Reporter **/
        //personId = person.getPersonID();
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("certText", apprvdDisclosureBean.getCertificationText());
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        hmData.put("coiDisclosureNumber", disclosureNumber);
               if(disclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", sequenceNumber);}
        hmData.put("personId", personId);       
        Hashtable statusData=new Hashtable();
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
/* **            for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                if(object.getCertificationTimestamp()!=null){
                     request.setAttribute("isCertified", true);
                }else{
                    request.setAttribute("isCertified", false);
                }
            }
 ** */
        }

/* **
        DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
        Vector statusDet = (Vector) DisclData.get("getDisclStatus");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("statusDetView", statusDet);
        }
** */
         String unitNumber = "";
        hmData = new HashMap();
        Vector statusDet = new Vector();
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            unitNumber=personInfoBean.getHomeUnit();
            //added by Vineetha
              request.setAttribute("PersonDetails", personDatas);
            session.setAttribute("person", personInfoBean);
        }
        /**/
        String Desc ="";
        if(unitNumber!=null)     { 
         HashMap inputMap1 = new HashMap();
         inputMap1.put("unitNumber", unitNumber);         
         Hashtable htUnitDesc = (Hashtable)webTxn.getResults(request,"getUnitDescription",inputMap1);
         HashMap hmUnitDesc = (HashMap)htUnitDesc.get("getUnitDescription");
         if(hmUnitDesc!= null && hmUnitDesc.size() > 0){
            Desc = (String)hmUnitDesc.get("RetVal");
         }
        }
        if(unitNumber!=null && Desc!=null){
         Desc = unitNumber +":"+ Desc;
        }       
         session.setAttribute("Desc", Desc);
        
        /**/
        statusDet = (Vector) statusData.get("getDisclStatus");
        Vector pendingDiscl = null;
        hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable pendingDisclDet = (Hashtable) webTxn.getResults(request, "getPendingDisclosure", hmData);
        pendingDiscl = (Vector) pendingDisclDet.get("getPendingDisclosure");
        if (pendingDiscl != null && pendingDiscl.size() > 0) {
            CoiDisclosureBean pendingDisclosureBean = new CoiDisclosureBean();
            request.setAttribute("pendingDiscl", pendingDiscl);
            pendingDisclosureBean = (CoiDisclosureBean) pendingDiscl.get(0);
            request.getSession().setAttribute("disclosureBeanSession", pendingDisclosureBean);
        }
    }
        //UtilFactory.log("pendingDiscl  is " + pendingDiscl);
       // UtilFactory.log("apprvdDisclosureBean  is " + apprvdDisclosureBean);
 private void checkCOIPrivileges(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
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
   private ActionForward getFinEntityDetail(HashMap hmFinData,DynaValidatorForm dynaValidatorForm, HttpServletRequest request,ActionMapping actionMapping)throws Exception{
 // String entityNumber=request.getParameter("entityNumber").trim();
//  if(entityNumber.equals(null)||entityNumber.equals(""))
//  {
       String entityNumber=(String)request.getSession().getAttribute("entityNumber");
//  }
       //String entityNumber="0000000003";entityNo
     getMatrix(hmFinData,request);
     HashMap hmDetails=new HashMap();
     hmDetails.put("entityNumber",entityNumber);
     HttpSession session=request.getSession();
     WebTxnBean webTxnBean = new WebTxnBean();
    Hashtable htData = (Hashtable)webTxnBean.getResults(request,"finEntDisclDetails",hmDetails);
    Vector vecData = (Vector)htData.get("getPerFinEntDetails");
    Vector vecEntity=(Vector)htData.get("getFinDiscDet");
    DynaValidatorForm dynaForm=new DynaValidatorForm();
    if(vecEntity!=null && vecEntity.size()>0){
              for(int index=0 ;index < vecEntity.size();index++){
            dynaForm = (DynaValidatorForm)vecEntity.get(index);
               session.setAttribute("annDisclFinEntity",dynaForm);
       }}else{
       ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("invalidEntity",
                        new ActionMessage("error.FinEntity.invalid"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
     }
    //********Right Check Starts
 int role=0;
        boolean setError=false;
        role=this.roleCheck(dynaForm,request);
        if(role<RIGHTTOVIEW)
        setError=true;
        if(setError){
            ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("noright",
                        new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
                        saveMessages(request,actionMessages);
                        return actionMapping.findForward( "exception" );

        }
        //*********Right Check Ends
    if(vecData!=null && vecData.size()>0){
    session.setAttribute("entityDetails",vecData);
  }else{
        session.removeAttribute("entityDetails");
  }
    if(request.getParameter("mode")!=null){
        request.setAttribute("mode",request.getParameter("mode"));
    }
    /*else if(hasRightToView && !hasRightToEdit){
     request.setAttribute("mode","review");
     } */
    else{
    request.setAttribute("mode","review");
    }
    if(request.getParameter("actionFrom")!=null){
    String actFrom=request.getParameter("actionFrom").trim();
    if(actFrom.equals("finEnt")){
     request.setAttribute("addFinEntFrom","finEnt");
    }else  if(actFrom.equals("main")){
     request.setAttribute("addFinEntFrom","main");
    }
    else  if(actFrom.equals("coiDiscl")){
     request.setAttribute("addFinEntFrom","coiDiscl");
    }
    else  if(actFrom.equals("revDiscl")){
     request.setAttribute("addFinEntFrom","revDiscl");
    }
    System.out.println(actFrom);
}
     return actionMapping.findForward("success");

 }
   private int roleCheck(DynaValidatorForm dynaForm,HttpServletRequest request)throws Exception{
    int right=0;
    boolean hasRightToView = false;
        boolean hasRightToEdit = false;
        boolean canApproveDisclosure = false;
        HttpSession session=request.getSession();
        String loggedinpersonid =
            (String)session.getAttribute(LOGGEDINPERSONID);
        String strUserprivilege =
            session.getAttribute(PRIVILEGE).toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if(userprivilege > 1){
            hasRightToEdit = true;
            hasRightToView = true;
        }else if(userprivilege > 0){
            hasRightToView = true;
            if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
              /*  String disclStatCode = (String)dynaForm.get("coiDisclosureStatusCode");
                if(disclStatCode.equals("100") || disclStatCode.equals("101")
                || disclStatCode.equals("104")){*/
                    hasRightToEdit = true;
                }
            //}
        }else if(userprivilege == 0){
            hasRightToView = true;
            if(dynaForm.get("personId").toString().equals(loggedinpersonid)){
            /*    String disclStatCode = (String)dynaForm.get("coiDisclosureStatusCode");
                if(disclStatCode.equals("100") || disclStatCode.equals("101")
                || disclStatCode.equals("104")){*/
                    hasRightToEdit = true;
                }
            }
        if(!hasRightToView && !hasRightToEdit )
                right=0;
        else if(hasRightToView && !hasRightToEdit)
            right=1;
        else if(hasRightToView && hasRightToEdit)
            right=2;

    return right;
}
   private void getMatrix(HashMap hmFinData,HttpServletRequest request)throws Exception{
HttpSession session=request.getSession();
WebTxnBean webTxn=new WebTxnBean();
Hashtable htFinData=(Hashtable)webTxn.getResults(request,"finEntDiscl",hmFinData);
Vector entityType=(Vector)htFinData.get("getOrgTypeList");
Vector rltnType=(Vector)htFinData.get("getFinEntityRelType");
Vector dtGrp=(Vector)htFinData.get("getFinEntdataGroups");
Vector dtaMtrx=(Vector)htFinData.get("getfinEntDataMatrix");
//Case#4447 - Next phase of COI enhancements - Start
Vector relationShipType = (Vector)htFinData.get("getFinEntityRelType");
FinEntMatrixBean fin=new FinEntMatrixBean();
for(int index=0;index<dtaMtrx.size();index++){
  fin=(FinEntMatrixBean)dtaMtrx.get(index);
  if(fin.getLookupArgument()!=null){
  String arg=fin.getLookupArgument();
  HashMap hmarg=new HashMap();
  hmarg.put("argumentName",arg);
  Hashtable htList=(Hashtable)webTxn.getResults(request,"getArgValueList",hmarg);
  Vector argList=(Vector)htList.get("getArgValueList");
  session.setAttribute(arg,argList);
}
}
     session.setAttribute("entityType",entityType);
      session.setAttribute("rltnType",rltnType);
     session.setAttribute("finEntdataGroup",dtGrp);
     session.setAttribute("finEntdataMatrix",dtaMtrx);
     //Case#4447 - Next phase of COI enhancements - Start
     session.setAttribute("finRelType",relationShipType);
     //Case#4447 - End
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


        HttpSession session = request.getSession();
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
/* **            for (Iterator it = DisclDet.iterator(); it.hasNext();) {
                CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                if(object.getCertificationTimestamp()!=null){
                     request.setAttribute("isCertified", true);
                }else{
                    request.setAttribute("isCertified", false);
                }
            }
 ** */
        }

/* **
        DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
        Vector statusDet = (Vector) DisclData.get("getDisclStatus");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("statusDetView", statusDet);
        }
** */
          
            if (personId == null) {
                PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
                personId = person.getPersonID();
          }

        String person_Id = (String) session.getAttribute("param3");
        hmData = new HashMap();
        if(flag)
        hmData.put("personId", person_Id);
        else
        hmData.put("personId", personId);
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            session.setAttribute("person", personInfoBean);
            // Added by Vineetha
            request.setAttribute("PersonDetails",personDatas);
        }

    }
        private void setFEFlag(CoiDisclosureBean entityList,CoiDisclosureBean mainList){
            
            if(entityList!=null && entityList.getIsFEFlag()!=null){
                if(entityList.getIsFEFlag()!=2){
                    int isfe = mainList.getIsFEPresent();
                    if(entityList.getIsFEPresent()!= isfe){
                        entityList.setIsFEFlag(2);//value 2 means it has projects with FE as well as non FE's
                    }                    
                }
            }else{
               entityList.setIsFEFlag(entityList.getIsFEPresent()); 
            }
        }
    }

