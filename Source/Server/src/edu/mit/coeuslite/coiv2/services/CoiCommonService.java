/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.coi.bean.ComboBoxBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiFinancialEntityBean;
import java.text.Format;
import java.text.SimpleDateFormat;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Mr Lijo
 */

public class CoiCommonService {

    private static CoiCommonService instance = null;
    public static final String LOGGEDINPERSONNAME = "loggedinpersonname";
    public static final String VIEW_PENDING_DISC = "viewPendingDisc";
    public static final String PRIVILEGE = "userprivilege";
    public static final String LOGGEDINPERSONID = "loggedinpersonid";

    private CoiCommonService() {
    }

    public static CoiCommonService getInstance() {
        if (instance == null) {
            instance = new CoiCommonService();
        }
        return instance;
    }

    public Vector getProjects(String diclosureNumber, Integer SequenceNumber, HttpServletRequest request, String transcationId) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        Vector projectDetVector = null;
        hmData.put("coiDisclosureNumber", diclosureNumber);
        hmData.put("sequenceNumber", SequenceNumber);
        Hashtable projectDetHashtable = (Hashtable) webTxnBean.getResults(request, transcationId, hmData);
        projectDetVector = (Vector) projectDetHashtable.get(transcationId);
        return projectDetVector;
    }

    public Vector getProjectDetails(String diclosureNumber, Integer SequenceNumber, String moduleItemKey, HttpServletRequest request, String transcationId) throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmData = new HashMap();
        Vector projectFinDetVector = null;
        hmData.put("coiDisclosureNumber", diclosureNumber);
        hmData.put("sequenceNumber", SequenceNumber);
        hmData.put("moduleItemKey", moduleItemKey);
        Hashtable projectFinDetHashtable = (Hashtable)webTxnBean.getResults(request, transcationId, hmData);
        projectFinDetVector = (Vector)projectFinDetHashtable.get(transcationId);
        return projectFinDetVector;
    }

    public CoiDisclosureBean getAnnualDisclosure(HttpServletRequest request)throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean userInfoBean = (PersonInfoBean) session.getAttribute("LOGGED_IN_PERSON");
        String personId = userInfoBean.getPersonID();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        hmData.put("coiDisclosureNumber",null);
        hmData.put("seqNum",null);
        hmData.put("moduleCode",5);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htAnnualDiscl =
                (Hashtable) webTxnBean.getResults(request, "getAnnualDisclCoiv2", hmData);
        Vector vecDiscl = (Vector) htAnnualDiscl.get("getAnnualDisclCoiv2");
        CoiDisclosureBean discl = new CoiDisclosureBean();
        if (vecDiscl != null && vecDiscl.size() > 0) {
            for (int i = 0; i < vecDiscl.size(); i++) {
                discl = (CoiDisclosureBean) vecDiscl.get(0);
              // NEW MENU CHANGE 
                request.getSession().setAttribute("COISeqNumber",discl.getSequenceNumber());  
           // NEW MENU CHANGE 
            }
        }
        request.getSession().setAttribute("vecDiscl", vecDiscl);
        return discl;
    }
        public Integer userHasDisclosure(HttpServletRequest request) throws Exception{
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
 public  void setApprovedDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
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
            Vector DisclDet = new Vector();
            Hashtable DisclData = new Hashtable();
            Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
            statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
            if (statusDispDet != null && statusDispDet.size() > 0) {
                request.setAttribute("statusDispDetView", statusDispDet);
            }
** */
            Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
            Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
           if (DisclDet != null && DisclDet.size() > 0) {
                request.setAttribute("ApprovedDisclDetView", DisclDet);
/* **
                for (Iterator it = DisclDet.iterator(); it.hasNext();) {
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
 public  void checkCOIPrivileges(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(
                        SessionConstants.LOGGED_IN_PERSON);
        if(personInfoBean!=null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null){
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            session.setAttribute(PRIVILEGE,""+userDetailsBean.getCOIPrivilege(userName));
            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);
            //Check whether to show link for View Pending Disclosures
            if(userDetailsBean.canViewPendingDisc(userName)){
              session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        }
        session.setAttribute("person", personInfoBean);
    }
  public CoiDisclosureBean getCurrentDisclPerson(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            this.checkCOIPrivileges(request);
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
    public CoiDisclosureBean getLatestApprvdDisclPerson(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("person") == null) {
            this.checkCOIPrivileges(request);
        }
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute("person");
        String personId = personInfoBean.getPersonID();
        HashMap hmreviewData = new HashMap();
        hmreviewData.put("personId", personId);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htCurrDiscl =
                (Hashtable) webTxnBean.getResults(request, "getLatestApprvCoiDiscl", hmreviewData);
        Vector vecDiscl = (Vector) htCurrDiscl.get("getLatestApprvCoiDiscl");
        CoiDisclosureBean discl = new CoiDisclosureBean();
        if (vecDiscl != null && vecDiscl.size() > 0) {
            for (int i = 0; i < vecDiscl.size(); i++) {
                discl = (CoiDisclosureBean) vecDiscl.get(0);
            }
        }
        return discl;
    }
 public  Vector getFinEntity(HttpServletRequest request, HashMap hmData) throws IOException, Exception {
               WebTxnBean webTxnBean = new WebTxnBean();
       Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "getFinEntityListCoiv2Bean", hmData);
        Vector finEntityList = (Vector) projectDetailsList.get("getFinEntityListCoiv2Bean");
        Vector finEntityListNew=new Vector();
        if(finEntityList!=null){
        for(int index=0;index<finEntityList.size();index++){
        if(((CoiFinancialEntityBean)finEntityList.get(index)).getStatusCode()==1){
            finEntityListNew.add(finEntityList.get(index));}
        }
            }
        request.getSession().setAttribute("finEntityComboList", finEntityListNew);
        return finEntityListNew;
    }
 public int roleCheck(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        int right = 0;
        boolean hasRightToView = false;
        boolean hasRightToEdit = false;
        HttpSession session = request.getSession();
        String loggedinpersonid =
                (String) session.getAttribute(LOGGEDINPERSONID);
        String strUserprivilege =
                session.getAttribute(PRIVILEGE).toString();
        int userprivilege = Integer.parseInt(strUserprivilege);
        if (userprivilege > 1) {
            hasRightToEdit = true;
            hasRightToView = true;
        } else if (userprivilege > 0) {
            hasRightToView = true;
            if (dynaForm.get("personId").toString().equals(loggedinpersonid)) {
                hasRightToEdit = true;
            }
        } else if (userprivilege == 0) {
            hasRightToView = true;
            if (dynaForm.get("personId").toString().equals(loggedinpersonid)) {
                hasRightToEdit = true;
            }
        }
        if (!hasRightToView && !hasRightToEdit) {
            right = 0;
        } else if (hasRightToView && !hasRightToEdit) {
            right = 1;
        } else if (hasRightToView && hasRightToEdit) {
            right = 2;
        }

        return right;
    }
        //getting fullName from a userName
     public String getFullNameFromUserName(HttpServletRequest request) throws Exception{

        WebTxnBean webTxn = new WebTxnBean();
        String  fullName = null;
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String userName = personBean.getUserName();
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userName", userName);
        Hashtable fullNameHashmap = (Hashtable) webTxn.getResults(request, "getfullnamefromusername", hmData);
        hasRightMap = (HashMap) fullNameHashmap.get("getfullnamefromusername");
        if (fullNameHashmap != null && fullNameHashmap.size() > 0) {
            fullName =(String) hasRightMap.get("fullName");
        }
        return fullName;
    }

      public String getFormatedDate(Date d){
//        Format formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
          Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
          String uddate = formatter.format(d);
          return uddate;
      }

      public void setDisclosureNumberForApprovedAnnualDisclosure(HttpServletRequest request)throws Exception{
           WebTxnBean webTxn = new WebTxnBean();
           HttpSession session = request.getSession();
           PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
           String personId = person.getPersonID();
           Vector apprvdAnnualDiscl = null;
           CoiDisclosureBean apprvdAnnualDisclosureBean = new CoiDisclosureBean();
           HashMap hmData = new HashMap();
           hmData.put("personId", personId);
           Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprvdAnnualDisclCoiv2", hmData);
           apprvdAnnualDiscl = (Vector) apprvdDisclDet.get("getApprvdAnnualDisclCoiv2");
           if (apprvdAnnualDiscl != null && apprvdAnnualDiscl.size() > 0) {
               apprvdAnnualDisclosureBean = (CoiDisclosureBean) apprvdAnnualDiscl.get(0);
               UtilFactory.log("apprvdAnnualDisclosureBean.getCoiDisclosureNumber() is"+apprvdAnnualDisclosureBean.getCoiDisclosureNumber());
               request.getSession().setAttribute("annualDisclosureBeanDisclosureNumber", apprvdAnnualDisclosureBean.getCoiDisclosureNumber());
           }

      }

      /*
       * get all the financial Entities of the user
       *
       */

 public Vector getFinancialEntityForUser(HttpServletRequest request)throws Exception{

           WebTxnBean webTxn = new WebTxnBean();
           HttpSession session = request.getSession();
           HashMap hmData = new HashMap();
           PersonInfoBean personinfo=(PersonInfoBean)session.getAttribute("person");
           String personId=personinfo.getPersonID();
           hmData.put("personId",personId);
           WebTxnBean webTxnBean = new WebTxnBean();
           Hashtable htData =(Hashtable)webTxnBean.getResults(request,"getPerFinEntityCoiv2",hmData);
           Vector vecData = (Vector)htData.get("getFinEntityListCoiv2");
           return vecData;
      }


 public CoiDisclosureBean getApprovedDisclosureBean(String personID,HttpServletRequest request)throws Exception{

          CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
          Vector apprvdDiscl=new Vector();
          WebTxnBean webTxn=new WebTxnBean();
                HashMap hmData = new HashMap();
                 hmData.put("personId", personID);
                 Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
                apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
                if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
                 apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
                }


          return apprvdDisclosureBean;
      }


      public void getDisclosureDet(HttpServletRequest request) throws Exception {

        Vector statusDispDet = new Vector();
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        session.setAttribute("person", person);
        Vector apprvdDiscl = null;

        /** Gets Latest Version of the disclosure for the logged in Reporter **/
        String personId = person.getPersonID();
        CoiDisclosureBean apprvdDisclosureBean = new CoiDisclosureBean();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprovedDisclosure", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprovedDisclosure");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        hmData.put("coiDisclosureNumber", apprvdDisclosureBean.getCoiDisclosureNumber());
       Object chkDisclosureNumber=apprvdDisclosureBean.getCoiDisclosureNumber();
        if(chkDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", apprvdDisclosureBean.getSequenceNumber());}

        hmData.put("personId", personId);
        hmData.put("moduleCode",null);
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
        }else {
            request.removeAttribute("ApprovedDisclDetView");
        }
    }

}
