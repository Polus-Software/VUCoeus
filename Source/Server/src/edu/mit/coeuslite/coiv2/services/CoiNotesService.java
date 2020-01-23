/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.actions.SetStatusAdminCoiV2;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.formbeans.Coiv2Notes;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Mr Lijo Joy
 */
public class CoiNotesService {

    private CoiNotesService() {
    }
    private static int NOTES_ADDED_ACTION_CODE = 802;
    private static int NOTES_EDITED_ACTION_CODE = 806;
    private static int NOTES_DELETED_ACTION_CODE = 807;
    private static CoiNotesService instance = null;
    String EMPTY_STRING = "";

    public static CoiNotesService getInstance() {
        if (instance == null) {
            instance = new CoiNotesService();
        }
        return instance;
    }

    /**
     * Function to save or update or remove Notes
     * @param coiv2Notes
     * @param request
     * @param actionMapping
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws Exception
     */
   public String saveOrUpdateOrDeleteCoiNotes(Coiv2Notes coiv2Notes, HttpServletRequest request, ActionMapping actionMapping, String operationType) throws IllegalAccessException, InvocationTargetException, Exception {
        HttpSession session = request.getSession();
        session.removeAttribute("fromApprovedDisclView");
         CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           }
           String disclosureNumber = coiInfoBean.getDisclosureNumber();
           Integer sequenceNumber = coiInfoBean.getSequenceNumber();
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance(); 
        Coiv2NotesBean coiv2NotesBean = new Coiv2NotesBean();
        DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
        PersonInfoBean userInfoBean = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
        BeanUtils.copyProperties(coiv2NotesBean, coiv2Notes);
        coiv2NotesBean.setUpdateUser(userInfoBean.getUserId());
        coiv2NotesBean.setTitle(coiv2Notes.getTitle());
        coiv2NotesBean.setComments(coiv2Notes.getComments());
        WebTxnBean webTxnBean = new WebTxnBean();
        String disclNumber = "";
        Integer seqNumberdata=0;        
         coiv2NotesBean.setCoiDisclosureNumber(disclosureNumber);
         coiv2NotesBean.setCoiSequenceNumber(sequenceNumber);
        if (coiv2Notes.getResttrictedView() == null || coiv2Notes.getResttrictedView().equals("")) {
            coiv2NotesBean.setResttrictedView("N");
        }else
            coiv2NotesBean.setResttrictedView("R");
        //  coiv2NotesBean.setAcType("I");
        //Get Max Entry Number for a given Disclosure number
        if (coiv2Notes.getEntiryNumber() == null || coiv2Notes.getEntiryNumber().equals("")) {
            Integer entryNumber = getMaxEntryNumber(request, disclNumber);
            coiv2NotesBean.setEntiryNumber(entryNumber);
            coiv2NotesBean.setAcType("I");
            coiv2Notes.setAcType("I");

        } else {
            if (coiv2Notes.getEntiryNumber() != null) {
                coiv2NotesBean.setEntiryNumber(Integer.parseInt(coiv2Notes.getEntiryNumber()));
            }
            coiv2NotesBean.setAcType(coiv2Notes.getAcType().trim());

        }
        //update or save disclosure
        webTxnBean.getResults(request, "updateDisclosureNotes", coiv2NotesBean);
///edit for  new mail starts------------------------------@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


        int actionId=0;
     SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
     Vector vecRecipients = new Vector();
       if (coiv2Notes.getAcType().trim().equalsIgnoreCase("I")) {
          actionId= NOTES_ADDED_ACTION_CODE;
          UtilFactory.log("################ is :  "+coiv2Notes.getAcType());
        }
        if (coiv2Notes.getAcType().trim().equals("U")) {
           actionId= NOTES_EDITED_ACTION_CODE;
        }
        if (coiv2Notes.getAcType().trim().equals("R")) {
            actionId= NOTES_DELETED_ACTION_CODE;

        }
            boolean isAdmin = gettingRightsCoiV2Service.isAdmin(request);
            
            /*
            boolean isViewer = gettingRightsCoiV2Service.isViewer(request);
            */
            
//            PersonInfoBean personDet = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
//               String userId = personDet.getUserId();
//              String personId=(String)session.getAttribute(SessionConstants.USER_ID);               
//                   request.setAttribute("userId", userId);
//                   request.setAttribute("personId", personId);
            
           boolean isAdminOrViewer = false;
            /*
            boolean onlyViewer = false;
            
            Integer isAdminViewer = 0;
            */
            /*
            if(isViewer) {
                if (!isAdminOrViewer) {
                    onlyViewer = true;
                }
                isAdminOrViewer = true;
            }
            */
            if (isAdmin) {
                isAdminOrViewer = true;
            }
            /*
            isAdminViewer=(Integer) session.getAttribute("ADMINVIEWER");
            */  
 String setDisclosureNumberData = "Disclosure Number : "+ disclNumber;
            WebTxnBean webTxn = new WebTxnBean();
            HashMap hmData2=new HashMap();
                    Vector reporterpre = null;
                    reporterpre =setstatus.getReporterByDiscl(disclNumber, request);
                PersonInfoBean person1 = new PersonInfoBean();

                    if (reporterpre != null && reporterpre.size() > 0) {
                        person1 = (PersonInfoBean) reporterpre.get(0);
                    }


            hmData2.put("coiDisclosureNumber", disclNumber);
            hmData2.put("sequenceNumber",seqNumberdata);
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

                    Vector reporter = null;

                    reporter = setstatus.getReporterByDiscl(disclNumber, request);
                          
                    if (reporter != null && reporter.size() > 0) {
                        for (Iterator it = reporter.iterator(); it.hasNext();) {
                                     PersonInfoBean ob = (PersonInfoBean) it.next();
                                     PersonRecipientBean reciepientob = new PersonRecipientBean();
                                     if(ob.getEmail()!=null){
                                         reciepientob.setEmailId(ob.getEmail());
                                     }

                                   
                                    vecRecipients.add(reciepientob);
                        }
                    }
                      UtilFactory.log("@@@@@ notes reporter assigned  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");



//            Vector viewers = null;
//            viewers = setstatus.getViewersByDiscl(disclNumber, request);
//            if (viewers != null && viewers.size() > 0) {
//                for (Iterator itv = viewers.iterator(); itv.hasNext();) {
//                    PersonInfoBean ob = (PersonInfoBean) itv.next();
//                         PersonRecipientBean reciepientob = new PersonRecipientBean();
//                         if(ob.getEmail()!=null){
//                             reciepientob.setEmailId(ob.getEmail());
//                         }
//                        vecRecipients.add(reciepientob);
//                }
//            }

            Vector adminsviewer = null;
            HashMap hmData = new HashMap();
//            WebTxnBean webTxn = new WebTxnBean();
            Hashtable users = (Hashtable) webTxn.getResults(request, "getAdminsByDisclosure", hmData);
            adminsviewer = (Vector) users.get("getAdminsByDisclosure");
            if (adminsviewer != null && adminsviewer.size() > 0) {
                for (Iterator ita = adminsviewer.iterator(); ita.hasNext();) {
                     PersonRecipientBean ob = (PersonRecipientBean) ita.next();

//                      PersonInfoBean ob = (PersonInfoBean) ita.next();
//                         PersonRecipientBean adminobject = new PersonRecipientBean();
//                         if(ob.getEmail()!=null){
//                             adminobject.setEmailId(ob.getEmail());
//                        }


                   vecRecipients.add(ob);
                }
            }


                MailMessageInfoBean mailMsgInfoBean = null;
                // Send mail to newly added reviewer
                try{
                    boolean  mailSent;
                    if(isAdminOrViewer){

                      mailMsgInfoBean = discloNotification.prepareNotification(actionId);
                      if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                            mailMsgInfoBean.setPersonRecipientList(vecRecipients);

                                    mailMsgInfoBean.appendMessage(" ", "\n");
                                    mailMsgInfoBean.appendMessage(" ", "\n");
                                   mailMsgInfoBean.appendMessage(setDisclosureNumberData, "\n");
                                   mailMsgInfoBean.appendMessage(disclosureStatus, "\n");
                                   mailMsgInfoBean.appendMessage(userName, "\n");

                            mailSent = discloNotification.sendNotification(mailMsgInfoBean);

                        }
                    }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
       } catch (Exception ex){
              UtilFactory.log(ex.getMessage());
       }
        //for mail
//        UtilFactory.log("===================Notes mail start===================" + new Date());
//        HashMap hmData = new HashMap();
//        SetStatusAdminCoiV2 setstatus = new SetStatusAdminCoiV2();
//        SetMailAttributes att = new SetMailAttributes();
//        WebTxnBean webTxn = new WebTxnBean();
//        CoeusMailService cms = new CoeusMailService();
//        att.setAttachmentPresent(false);
//        att.setSubject("Test Message from coeus");
//        att.setFrom("roshin@invisionlabs.com");
//        if (coiv2Notes.getAcType().trim().equals("I")) {
//            att.setMessage("Coeus Welcomes You \n Notes Inserted Successfully");
//        }
//        if (coiv2Notes.getAcType().trim().equals("U")) {
//            att.setMessage("Coeus Welcomes You \n Notes Updated Successfully");
//        }
//        if (coiv2Notes.getAcType().trim().equals("R")) {
//            att.setMessage("Coeus Welcomes You \n Notes Remove Successfully");
//        }
//
//        //for getting viewers email ids
//        String viewer = "";
//        Vector viewers = null;
//        viewers = setstatus.getViewersByDiscl(disclNumber, request);
//        if (viewers != null && viewers.size() > 0) {
//            for (Iterator it = viewers.iterator(); it.hasNext();) {
//                PersonInfoBean object = (PersonInfoBean) it.next();
//                viewer += object.getEmail() + ",";
//            }
//        }
//
//        //for getting admin emails
//        String admins = "";
//        Vector userDet = null;
//        hmData = new HashMap();
//        webTxn = new WebTxnBean();
//        Hashtable users = (Hashtable) webTxn.getResults(request, "getAdmins", hmData);
//        userDet = (Vector) users.get("getAdmins");
//        if (userDet != null && userDet.size() > 0) {
//            for (Iterator it = userDet.iterator(); it.hasNext();) {
//                CoiUsersBean coiUsersBean = (CoiUsersBean) it.next();
//                admins += coiUsersBean.getEmail() + ",";
//            }
//        }
//        att.setTo(admins + viewer);
//        cms.sendMessage(att);
//        System.out.println("Done");
//        UtilFactory.log("===================Notes mail end===================" + new Date());

        return "success";
    }

    /**
     * Function to get  Notes
     * @param coiv2Notes
     * @param request
     * @param actionMapping
     * @return
     * @throws Exception
     */
    public String getCoiNotes(Coiv2Notes coiv2Notes, HttpServletRequest request, ActionMapping actionMapping) throws Exception {
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();
        HttpSession session=request.getSession();
        String disclosureNumber = null;
        Integer sequenceNumber = null;
         CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
           if(coiInfoBean==null){
              coiInfoBean =new CoiInfoBean();
           }
           if(coiv2Notes!=null){
             disclosureNumber = coiv2Notes.getCoiDisclosureNumber();
             sequenceNumber = Integer.parseInt(coiv2Notes.getCoiSequenceNumber()); 
           }else{
            disclosureNumber = coiInfoBean.getDisclosureNumber();
            sequenceNumber = coiInfoBean.getSequenceNumber(); 
           }
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        
        boolean isAdmin = gettingRightsCoiV2Service.isAdmin(request);
        boolean isReviewer=gettingRightsCoiV2Service.isReviewer(request);
        Coiv2NotesBean coiv2NotesBean = new Coiv2NotesBean();
         if(isAdmin || isReviewer){
               coiv2NotesBean.setResttrictedView("R"); 
            }
           request.setAttribute("isAdmin", isAdmin);
           request.setAttribute("isReviewer", isReviewer);
           
            
               
        WebTxnBean webTxnBean = new WebTxnBean();
        CoiDisclosureBean currDisclosure = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");      
       Boolean result=false;
        if (session.getAttribute("frmShowAllReviews") != null) {
            result=(Boolean)session.getAttribute("frmShowAllReviews");
        }
        if(result !=null && result==true){           
           session.setAttribute("fromReview",true);
        }
                
        coiv2NotesBean.setCoiDisclosureNumber(disclosureNumber);
        coiv2NotesBean.setCoiSequenceNumber(sequenceNumber);
 //code added to bring forward previously approved sequences notes start
        String check="";
        if(session.getAttribute("checkPrint") !=null){
         check =session.getAttribute("checkPrint").toString();}
        Vector notes=new Vector();
        Vector notesList=new Vector();
         // if((! check.equals("approvedDisclosureview"))&& check.equalsIgnoreCase("") ){
         if(!check.equals("approvedDisclosureview")){
        Hashtable discNotesList = (Hashtable) webTxnBean.getResults(request, "getDisclosureNotesCoiv2", coiv2NotesBean);
        notes = (Vector) discNotesList.get("getDisclosureNotesCoiv2");}
        Boolean viewCurrent=false;
//        if(session.getAttribute("fromViewCurrent") !=null){
//        viewCurrent =(Boolean)session.getAttribute("fromViewCurrent");}

        if(check.equalsIgnoreCase("approvedDisclosureview")){//code added to bring forward previously approved sequences notes start
            request.setAttribute("fromViewCurrent", true);
            HashMap hmData=new HashMap();
            hmData.put("coiDisclosureNumber",currDisclosure.getCoiDisclosureNumber());
            Hashtable disclNotes = (Hashtable) webTxnBean.getResults(request, "getApprvdDisclNotesCoiv2", hmData);
            notes = (Vector) disclNotes.get("getApprvdDisclNotesCoiv2");
            //code added to bring forward previously approved sequences notes ends
        }

        if(notes == null &&(session.getAttribute("saveNotesFromDiscl")!=null)&&((Boolean)session.getAttribute("saveNotesFromDiscl")==false) )
        {
             HashMap hmData=new HashMap();
            hmData.put("coiDisclosureNumber",currDisclosure.getCoiDisclosureNumber());
            Hashtable disclNotes = (Hashtable) webTxnBean.getResults(request, "getApprvdDisclNotesCoiv2", hmData);
            notes = (Vector) disclNotes.get("getApprvdDisclNotesCoiv2");
        }
         if((session.getAttribute("fromViewerAction")!=null)&&((Boolean)session.getAttribute("fromViewerAction")==true)){              
                 coiv2NotesBean.setCoiDisclosureNumber(disclosureNumber);
                  coiv2NotesBean.setCoiSequenceNumber(sequenceNumber);
            Hashtable disclNotes = (Hashtable) webTxnBean.getResults(request, "getDisclosureNotesCoiv2", coiv2NotesBean);
            notes = (Vector) disclNotes.get("getDisclosureNotesCoiv2");
         }
        if (notes != null && !notes.isEmpty()) {        
                notesList.addAll(notes);            
        }

//code added to bring forward previously approved sequences notes ends
        request.setAttribute("disclosureNotesData", notesList);
        return "success";
    }

    /**
     * function to get Max Entity Number
     * @param request
     * @return
     * @throws Exception
     */
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

    public String getCoiNotesAsperCondition(Coiv2Notes coiv2NotesBean, HttpServletRequest request, ActionMapping actionMapping) throws Exception {
        GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();
        HttpSession session=request.getSession();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        boolean isAdmin = gettingRightsCoiV2Service.isAdmin(request);
        request.removeAttribute("disclosureNotesData");
        WebTxnBean webTxnBean = new WebTxnBean();
        Coiv2NotesBean coiv2Notes = new Coiv2NotesBean();
        Integer seq = (Integer) request.getAttribute("currentSequence");
        if (seq != null) {
            coiv2Notes.setCoiSequenceNumber(seq);
        }else{
            if (coiv2NotesBean.getCoiSequenceNumber() != null && !coiv2NotesBean.getCoiSequenceNumber().equals("null")) {
                coiv2Notes.setCoiSequenceNumber(Integer.parseInt(coiv2NotesBean.getCoiSequenceNumber()));
            }
        }
 Boolean result=false;

 Vector notesList = new Vector();

// if(session.getAttribute("fromViewCurrent") !=null){
//    result=(Boolean)session.getAttribute("fromViewCurrent");}
 
  if(request.getParameter("selected")!=null && request.getParameter("selected").equalsIgnoreCase("Approved")){
      result=true;
  }
 
 Vector notes=null;
 if(result){
    CoiDisclosureBean currDisclosure = (CoiDisclosureBean) session.getAttribute("disclosureBeanSession");
    request.setAttribute("fromViewCurrent", true);
    HashMap hmData=new HashMap();
    hmData.put("coiDisclosureNumber",coiv2NotesBean.getCoiDisclosureNumber());
    Hashtable disclNotes = (Hashtable) webTxnBean.getResults(request, "getApprvdDisclNotesCoiv2", hmData);
    notes = (Vector) disclNotes.get("getApprvdDisclNotesCoiv2");
    
 }
 else{
    coiv2Notes.setCoiDisclosureNumber(coiv2NotesBean.getCoiDisclosureNumber());
    Hashtable discNotesList = (Hashtable) webTxnBean.getResults(request, "getDisclosureNotesCoiv2", coiv2Notes);
    notes = (Vector) discNotesList.get("getDisclosureNotesCoiv2");
 }
if (notes != null && !notes.isEmpty()) {            
                notesList.addAll(notes);           
        }
 
 
  
if (notesList.isEmpty()) {request.setAttribute("message", false);}

request.setAttribute("disclosureNotesData", notesList);
return "success";
}

    public Vector getCoiNotesType(HttpServletRequest request) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable hmNotesList = (Hashtable) webTxnBean.getResults(request, "geNotesTypeCoiv2", null);
        Vector hmNotesType = (Vector) hmNotesList.get("geNotesTypeCoiv2");
        return hmNotesType;
    }
}
