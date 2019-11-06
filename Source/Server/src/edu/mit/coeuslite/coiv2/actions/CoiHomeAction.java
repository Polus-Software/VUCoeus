/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureDetailsListBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GettingRightsCoiV2Service;
import edu.mit.coeuslite.coiv2.utilities.ReadProtocolDetailsCoiV2;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Sony
 * This action class generates the Coi Home page which will list
 * 1. Logged in Reporter Details
 * 2. Current/Latest approved disclosure of the corresponding Reporter
 * 3. Pending Disclosure versions for the Reporter
 */
public class CoiHomeAction extends COIBaseAction {

    private static final String SUB_HEADER = "subheaderVectorCoiv2";
    private static final String XML_PATH_ADMIN = "/edu/mit/coeuslite/coiv2/xml/COIV2SubmenuAdmin.xml";
    private static final String XML_PATH_VIEWER = "/edu/mit/coeuslite/coiv2/xml/COIV2SubmenuViewer.xml";
    private static final String XML_VIEWER_REPORTER = "/edu/mit/coeuslite/coiv2/xml/COIV2ViewerSubMenuReporter.xml";
    private static final String BODY_SUB_HEADER = "bodySubHeaderVectorCoiv2";
    private static final String XML_BODY_HEADER = "/edu/mit/coeuslite/coiv2/xml/COIV2Subheader.xml";
    private static final int COI_STATUS_APPROVED=2;
    private static final int COI_STATUS_CLOSED=1;
    private static final int COI_STATUS_PENDING=3;
    private static final int COI_STATUS_MANAGED=0;
    private static final String USER ="user";    
    

    public CoiHomeAction() {
    }

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("homePage", true);
        String actionForward = "failure";
        HttpSession session = request.getSession();
        int maxSeq = 0;
        saveToken(request);
        removeCOISessions(request);
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
            coiInfoBean =new CoiInfoBean();
        }
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());
        setDisclosureNumberIfAnnualDisclosureAvailable(request);
        String disclosureNumber = userHasDisclosure(request,person.getPersonID());
        if(disclosureNumber!=null){
        maxSeq =  getNextSeqNumDisclosure(request,disclosureNumber);
        }        
        coiInfoBean.setPersonId(person.getPersonID());
        coiInfoBean.setUserId(userInfoBean.getUserId());
        coiInfoBean.setDisclosureNumber(disclosureNumber);
        coiInfoBean.setSequenceNumber(maxSeq);
        if (disclosureNumber!=null) {
            request.setAttribute("disclosureAvailableMessage", true);
            CoiCommonService coiCommonService = CoiCommonService.getInstance();
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
        } else {
            request.setAttribute("disclosureAvailableMessage", false);
        } 
        Vector revisedPjctList=new Vector();
         if(disclosureNumber !=null && !disclosureNumber.equalsIgnoreCase(""))
        {    HashMap hashMap=new HashMap();
             WebTxnBean webTxnBean = new WebTxnBean();
             hashMap.put("coiDisclosureNumber",disclosureNumber);

       Hashtable revisionList = (Hashtable) webTxnBean.getResults(request, "getPendingRevisions", hashMap);
       revisedPjctList = (Vector) revisionList.get("getPendingRevisions");
        String certificationText="";
       if(revisedPjctList!=null && revisedPjctList.size()>0)
       {
       request.getSession().setAttribute("pendingRevisedPjctList",revisedPjctList);
       CoiDisclosureBean coiDisclosureBean=(CoiDisclosureBean) revisedPjctList.get(0);
       certificationText=coiDisclosureBean.getCertificationText();
       request.setAttribute("projectType","Revision");
        if(certificationText!=null)
          {
              request.getSession().setAttribute("certified",true);
          }
          else
          {
             request.getSession().setAttribute("certified",false);
          }
       }else{
            request.getSession().setAttribute("certified",true);
       }         
        }  
            getDisclosureDet(request,coiInfoBean);
            session.setAttribute("CoiInfoBean",coiInfoBean);
            GettingRightsCoiV2Service gettingRightsCoiV2Service = GettingRightsCoiV2Service.getInstance();   
      //      gettingRightsCoiV2Service.getCoiPrivilegesCoiV2(request);
            boolean isAdmin = gettingRightsCoiV2Service.isAdmin(request);
            Vector vecCOISubHeader = new Vector();
            boolean isAdminOrViewer = false;               
            if (isAdmin) {
                 getSubheaderDetails(request);
                 vecCOISubHeader = (Vector) request.getSession().getAttribute(SUB_HEADER);
                actionForward = "successToAdmin";
                isAdminOrViewer = true;
                request.getSession().setAttribute("isAdmin", isAdmin);
            }else{
                getSubheaderDetailsNotAdmin(request);
                 vecCOISubHeader = (Vector) request.getSession().getAttribute(SUB_HEADER);
                actionForward = "success";                
            }
        getBodySubheaderDetails(request);
        return actionMapping.findForward(actionForward);
    }

    //for getting subheader Admin
    private void getSubheaderDetails(HttpServletRequest request) throws Exception {

        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader;
        ReadProtocolDetailsCoiV2 readProtocolDetails = new ReadProtocolDetailsCoiV2();
        vecCOISubHeader = (Vector) application.getAttribute(SUB_HEADER);
        if (vecCOISubHeader == null || vecCOISubHeader.size() == 0) {
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_PATH_ADMIN);
            request.getSession().setAttribute(SUB_HEADER, vecCOISubHeader);
        }

        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        request.getSession().setAttribute("person", person);
    }
    private void getSubheaderDetailsNotAdmin(HttpServletRequest request) throws Exception {

        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader;
        ReadProtocolDetailsCoiV2 readProtocolDetails = new ReadProtocolDetailsCoiV2();
        vecCOISubHeader = (Vector) application.getAttribute(SUB_HEADER);
        if (vecCOISubHeader == null || vecCOISubHeader.isEmpty()) {
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_PATH_VIEWER);
            request.getSession().setAttribute(SUB_HEADER, vecCOISubHeader);
        }

        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        request.getSession().setAttribute("person", person);
    }
       private void getDisclosureDet(HttpServletRequest request,CoiInfoBean coiInfoBean) throws Exception {     
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();        
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        session.setAttribute("person", person);
        Vector apprvdDiscl;

        /** Gets Latest Version of the disclosure for the logged in Reporter **/
        String personId = person.getPersonID();
        request.getSession().setAttribute("loggedInPersonIdForCoi", personId);// set to check whether to show "My" b4 "discl No" in sub header
        CoiDisclosureBean apprvdDisclosureBean=null ;
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getApprvdDisclForCoihome", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getApprvdDisclForCoihome");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            apprvdDisclosureBean = (CoiDisclosureBean) apprvdDiscl.get(0);
            coiInfoBean.setApprovedSequence(apprvdDisclosureBean.getSequenceNumber());
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
    if(apprvdDisclosureBean != null){
        hmData.put("coiDisclosureNumber", apprvdDisclosureBean.getCoiDisclosureNumber());
               Object chkDisclosureNumber=apprvdDisclosureBean.getCoiDisclosureNumber();
        if(chkDisclosureNumber==null)
        { hmData.put("sequenceNumber",0);}
        else
        {hmData.put("sequenceNumber", apprvdDisclosureBean.getSequenceNumber());}
        }
        Hashtable statusData = new Hashtable();
        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");

        String coiDisclosureNumber=null;
        Integer seqNumber=null;

        if (DisclDet != null && DisclDet.size() > 0) {
            request.setAttribute("ApprovedDisclDetView", DisclDet);
             for (Iterator it = DisclDet.iterator(); it.hasNext();)
           {
                CoiDisclosureBean object = (CoiDisclosureBean) it.next();
                 coiDisclosureNumber=object.getCoiDisclosureNumber();
                 seqNumber=object.getSequenceNumber();
            }

        }
        hmData = new HashMap();
        hmData.put("personId", personId);
        Vector statusDet = new Vector();
        String unitNumber = "";
        Hashtable htPersonData = (Hashtable) webTxn.getResults(request, "getPersonDetails", hmData);
        Vector personDatas = (Vector) htPersonData.get("getPersonDetails");
        if (personDatas != null && personDatas.size() > 0) {
            PersonInfoBean personInfoBean = (PersonInfoBean) personDatas.get(0);
            session.setAttribute("person", personInfoBean);
            // Vineetha
            request.setAttribute("PersonDetails",personDatas);
              unitNumber = personInfoBean.getHomeUnit();
        }
        statusDet = (Vector) statusData.get("getDisclStatus");
         String Desc = "";
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

        hmData = new HashMap();
        hmData.put("personId", personId);
        //Vector statusDet = new Vector();
        String Campus = "";
        Hashtable htCampusData = (Hashtable) webTxn.getResults(request, "getCoiCampusCd", hmData);
         HashMap campusDatas = (HashMap) htCampusData.get("getCoiCampusCd");
        if (campusDatas != null && campusDatas.size() > 0) {
            Campus =(String)campusDatas.get("ls_campus_cd");
        }
        session.setAttribute("Campus", Campus);

        Vector pendingDiscl = null;
        hmData = new HashMap();
        hmData.put("personId", personId);
        CoiDisclosureBean pendingDisclosureBean = new CoiDisclosureBean();
        Hashtable pendingDisclDet = (Hashtable) webTxn.getResults(request, "getPendingDisclosure", hmData);
        pendingDiscl = (Vector) pendingDisclDet.get("getPendingDisclosure");
        if (pendingDiscl != null && pendingDiscl.size() > 0) {
             pendingDisclosureBean = new CoiDisclosureBean();
            request.setAttribute("pendingDiscl", pendingDiscl);
            pendingDisclosureBean = (CoiDisclosureBean) pendingDiscl.get(0);
            request.getSession().setAttribute("disclosureBeanSession", pendingDisclosureBean);
            request.getSession().setAttribute("fromPending", true);
        }

        String disclrNum = pendingDisclosureBean.getCoiDisclosureNumber();

        //newly added by jaisha for disclosure By financial Entities
        String disclosureNumber=null;
        if (apprvdDisclosureBean != null && apprvdDisclosureBean.getCoiDisclosureNumber() != null) {
            request.setAttribute("tileaward", false);
            request.setAttribute("tileMiscellaneous", false);
            disclosureNumber  = apprvdDisclosureBean.getCoiDisclosureNumber();
            Integer sequenceNumber = apprvdDisclosureBean.getSequenceNumber();

            hmData = new HashMap();
            hmData.put("coiDisclosureNumber", disclosureNumber);
            hmData.put("sequenceNumber", sequenceNumber);
            hmData.put("personId", personId);
             //added new
            String moduleName = apprvdDisclosureBean.getModuleName();
            session.setAttribute("param1", disclosureNumber);
            session.setAttribute("param2", sequenceNumber);
            session.setAttribute("param3", personId);
            request.getSession().setAttribute("param4", "viewOnly");
            request.getSession().setAttribute("param5", moduleName);
       }
        // to display View by projects
            HashMap inputMap = new HashMap();
            if(coiDisclosureNumber != null) {
                inputMap.put("coiDisclosureNumber", coiDisclosureNumber);
            } else {
                inputMap.put("coiDisclosureNumber", disclrNum);
            }

            inputMap.put("personId", personId);
            getPendingDisclosureList(request,inputMap);
            getApprovedDisclosureList(request,inputMap);
            getManagedDisclosureList(request,inputMap);
            getClosedDisclosureList(request,inputMap);

            Integer travelEventType = 8;
            HashMap htMap = new HashMap();
            htMap.put("personId", personId);
           htMap.put("eventType", travelEventType);
            getTravelDisclosureList(request,htMap);


      hmData.put("personId", personId);
      Hashtable apprvdDisclDetail = (Hashtable) webTxn.getResults(request, "getCoiHeaderDetails", hmData);
      Vector apprvdDisclosure = (Vector) apprvdDisclDetail.get("getCoiHeaderDetails");
      if((apprvdDisclosure!=null)&&(apprvdDisclosure.size()>0)){
          request.setAttribute("ApprovedDisclDet",apprvdDisclosure);
      }
      else{
          request.setAttribute("ApprovedDisclDet",new Vector());
      }

    }
    private void getPendingDisclosureList(HttpServletRequest request, HashMap hmData) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Vector finEntDetPend = new Vector();
	hmData.put("listType", COI_STATUS_PENDING);
	 Hashtable hashListDetails = (Hashtable) webTxn.getResults(request, "getcoidisclosurependingdetaillist", hmData);
     finEntDetPend = (Vector) hashListDetails.get("getcoidisclosurependingdetaillist");
     request.setAttribute("pendingPjtList", finEntDetPend);
     }

     private void getApprovedDisclosureList(HttpServletRequest request, HashMap hmData) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Vector finEntDetPend = new Vector();
	Vector pendEntityName = new Vector();
	Vector lFinalFinEntDet = new Vector();
	hmData.put("listType", COI_STATUS_APPROVED);
	 Hashtable hashListDetails = (Hashtable) webTxn.getResults(request, "getcoidisclosuredetaillist", hmData);
     finEntDetPend = (Vector) hashListDetails.get("getcoidisclosuredetaillist");
	  pendEntityName.clear();
          Vector pendProjectIds = new Vector();
	   if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
                    //checking whether project title is available or not
                    if (coiDisclosureDetailsListBean.getCoiProjectTitle() != null && !coiDisclosureDetailsListBean.getCoiProjectTitle().equals("")) {
                       if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId())){
                             lFinalFinEntDet.add(coiDisclosureDetailsListBean);
                              pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId());
                          }

                    if (pendEntityName != null && !pendEntityName.isEmpty()) {
                        for (Iterator it = pendEntityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title!=null && title.equals(coiDisclosureDetailsListBean.getEntityName())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                             if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                             }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                        }
                    }
                  }
                }
            }

			if (pendEntityName.isEmpty()) {
                request.setAttribute("message", false);
            }


            if (pendEntityName != null && !pendEntityName.isEmpty()) {
                request.setAttribute("apprvdEntityNameLists", pendEntityName);
            }
             if (lFinalFinEntDet != null && !lFinalFinEntDet.isEmpty()) {
                request.setAttribute("apprvdPjtEntDetailsViews", lFinalFinEntDet);
            }


			 Vector projectName = new Vector();
            lFinalFinEntDet = new Vector();
            pendProjectIds = new Vector();
            if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
					if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("")) {
					if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber())){
					lFinalFinEntDet.add(coiDisclosureDetailsListBean);
					pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber());
					}
                    if (projectName != null && !projectName.isEmpty()) {
                        for (Iterator it = projectName.iterator(); it.hasNext();) {
                            CoiDisclosureDetailsListBean pjtDetails = (CoiDisclosureDetailsListBean) it.next();
                            String title = (String) pjtDetails.getCoiProjectId();
                            if (title.equals(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                if (!coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                    isTitlePresent = true;
                                }
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId()) && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                            if (!projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    }}
                }
            }
            if (projectName!=null && projectName.isEmpty()) {
                request.setAttribute("projectMessage", false);
            }

            if (projectName != null && !projectName.isEmpty()) {
                request.setAttribute("apprvdProjectNameList", projectName);
                request.setAttribute("apprvdEntPjtDetView", lFinalFinEntDet);
            }
    }


     private void getManagedDisclosureList(HttpServletRequest request, HashMap hmData) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Vector finEntDetPend = new Vector();
	Vector pendEntityName = new Vector();
	Vector lFinalFinEntDet = new Vector();

	hmData.put("listType", COI_STATUS_MANAGED);
	 Hashtable hashListDetails = (Hashtable) webTxn.getResults(request, "getcoidisclosuredetaillist", hmData);
     finEntDetPend = (Vector) hashListDetails.get("getcoidisclosuredetaillist");

	  pendEntityName.clear();
      Vector pendProjectIds = new Vector();

	   if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                //UtilFactory.log("finEntDet is " + finEntDet.size());
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
                    //checking whether project title is available or not
                    if (coiDisclosureDetailsListBean.getCoiProjectTitle() != null && !coiDisclosureDetailsListBean.getCoiProjectTitle().equals("")) {
                       if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId())){
                             lFinalFinEntDet.add(coiDisclosureDetailsListBean);
                              pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId());
                          }

                    if (pendEntityName != null && !pendEntityName.isEmpty()) {
                        for (Iterator it = pendEntityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title!=null && title.equals(coiDisclosureDetailsListBean.getEntityName())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                             if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                             }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                        }
                    }
                  }
                }
            }

			if (pendEntityName.isEmpty()) {
                request.setAttribute("message", false);
            }



                request.setAttribute("mngdEntityNameLists", pendEntityName);
            request.setAttribute("mngdPjtEntDetailsViews", lFinalFinEntDet);

			 Vector projectName = new Vector();
            lFinalFinEntDet = new Vector();
            pendProjectIds = new Vector();
            if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
					if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("")) {
					if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber())){
					lFinalFinEntDet.add(coiDisclosureDetailsListBean);
					pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber());
					}
                    if (projectName != null && !projectName.isEmpty()) {
                        for (Iterator it = projectName.iterator(); it.hasNext();) {
                            CoiDisclosureDetailsListBean pjtDetails = (CoiDisclosureDetailsListBean) it.next();
                            String title = (String) pjtDetails.getCoiProjectId();
                            if (title.equals(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                if (!coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                    isTitlePresent = true;
                                }
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId()) && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                            if (!projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    }}
                }
            }
            if (projectName!=null && projectName.isEmpty()) {
                request.setAttribute("projectMessage", false);
            }
           // UtilFactory.log("projectName is " + projectName);

            if (projectName != null && !projectName.isEmpty()) {
                request.setAttribute("mngdProjectNameList", projectName);
                request.setAttribute("mngdEntPjtDetView", lFinalFinEntDet);
            }
    }

   private void getClosedDisclosureList(HttpServletRequest request, HashMap hmData) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Vector finEntDetPend = new Vector();
	Vector pendEntityName = new Vector();
	Vector lFinalFinEntDet = new Vector();

	hmData.put("listType", COI_STATUS_CLOSED);
	 Hashtable hashListDetails = (Hashtable) webTxn.getResults(request, "getcoidisclosuredetaillist", hmData);
     finEntDetPend = (Vector) hashListDetails.get("getcoidisclosuredetaillist");

	  pendEntityName.clear();
      Vector pendProjectIds = new Vector();

	   if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                //UtilFactory.log("finEntDet is " + finEntDet.size());
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
                    //checking whether project title is available or not
                    if (coiDisclosureDetailsListBean.getCoiProjectTitle() != null && !coiDisclosureDetailsListBean.getCoiProjectTitle().equals("")) {
                       if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId())){
                             lFinalFinEntDet.add(coiDisclosureDetailsListBean);
                              pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId());
                          }

                    if (pendEntityName != null && !pendEntityName.isEmpty()) {
                        for (Iterator it = pendEntityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title!=null && title.equals(coiDisclosureDetailsListBean.getEntityName())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                             if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                             }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                        }
                    }
                  }
                }
            }

			if (pendEntityName.isEmpty()) {
                request.setAttribute("message", false);
            }


            if (pendEntityName != null && !pendEntityName.isEmpty()) {
                request.setAttribute("cldEntityNameLists", pendEntityName);
            }
            if (lFinalFinEntDet != null && !lFinalFinEntDet.isEmpty()) {
             request.setAttribute("cldPjtEntDetailsViews", lFinalFinEntDet);
            }


			 Vector projectName = new Vector();
            lFinalFinEntDet = new Vector();
            pendProjectIds = new Vector();
            if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
					if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("")) {
					if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber())){
					lFinalFinEntDet.add(coiDisclosureDetailsListBean);
					pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber());
					}
                    if (projectName != null && !projectName.isEmpty()) {
                        for (Iterator it = projectName.iterator(); it.hasNext();) {
                            CoiDisclosureDetailsListBean pjtDetails = (CoiDisclosureDetailsListBean) it.next();
                            String title = (String) pjtDetails.getCoiProjectId();
                            if (title.equals(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                if (!coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                    isTitlePresent = true;
                                }
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId()) && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                            if (!projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    }}
                }
            }
            if (projectName!=null && projectName.isEmpty()) {
                request.setAttribute("projectMessage", false);
            }
           // UtilFactory.log("projectName is " + projectName);

            if (projectName != null && !projectName.isEmpty()) {
                request.setAttribute("cldProjectNameList", projectName);
            }
            if (lFinalFinEntDet != null && !lFinalFinEntDet.isEmpty()) {
                request.setAttribute("cldEntPjtDetView", lFinalFinEntDet);
            }
    }
    private void getTravelDisclosureList(HttpServletRequest request, HashMap hmData) throws Exception {
        WebTxnBean webTxn = new WebTxnBean();
        Vector finEntDetPend = new Vector();
	Vector pendEntityName = new Vector();
	Vector lFinalFinEntDet = new Vector();

     Hashtable hashListDetails = (Hashtable) webTxn.getResults(request, "getcoipersondisclproject", hmData);
     finEntDetPend = (Vector) hashListDetails.get("getcoipersondisclproject");
      pendEntityName.clear();
      Vector pendProjectIds = new Vector();

	   if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                //UtilFactory.log("finEntDet is " + finEntDet.size());
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
                    //checking whether project title is available or not
                    if (coiDisclosureDetailsListBean.getEventName() != null && !coiDisclosureDetailsListBean.getEventName().equals("")) {
                       if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId())){
                             lFinalFinEntDet.add(coiDisclosureDetailsListBean);
                              pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId());
                          }

                    if (pendEntityName != null && !pendEntityName.isEmpty()) {
                        for (Iterator it = pendEntityName.iterator(); it.hasNext();) {
                            String title = (String) it.next();
                            if (title!=null && title.equals(coiDisclosureDetailsListBean.getEntityName())) {
                                isTitlePresent = true;
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                             if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                             }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getEntityName() != null) {
                            pendEntityName.add(coiDisclosureDetailsListBean.getEntityName());
                        }
                    }
                  }
                }
            }

			if (pendEntityName.isEmpty()) {
                request.setAttribute("message", false);
            }


            if (pendEntityName != null && !pendEntityName.isEmpty()) {
                request.setAttribute("tvlEntityNameLists", pendEntityName);
            }
            if (lFinalFinEntDet != null && !lFinalFinEntDet.isEmpty()) {
             request.setAttribute("tvlPjtEntDetailsViews", lFinalFinEntDet);
            }


			 Vector projectName = new Vector();
            lFinalFinEntDet = new Vector();
            pendProjectIds = new Vector();
            if (finEntDetPend != null && !finEntDetPend.isEmpty()) {
                for (int i = 0; i < finEntDetPend.size(); i++) {
                    Boolean isTitlePresent = false;
                    CoiDisclosureDetailsListBean coiDisclosureDetailsListBean = (CoiDisclosureDetailsListBean) finEntDetPend.elementAt(i);
					if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("")) {
					if(!pendProjectIds.contains(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber())){
					lFinalFinEntDet.add(coiDisclosureDetailsListBean);
					pendProjectIds.add(coiDisclosureDetailsListBean.getCoiProjectId()+"-"+coiDisclosureDetailsListBean.getEntityNumber());
					}
                    if (projectName != null && !projectName.isEmpty()) {
                        for (Iterator it = projectName.iterator(); it.hasNext();) {
                            CoiDisclosureDetailsListBean pjtDetails = (CoiDisclosureDetailsListBean) it.next();
                            String title = (String) pjtDetails.getCoiProjectId();
                            if (title.equals(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                if (!coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                    isTitlePresent = true;
                                }
                                break;
                            }
                        }
                        if (isTitlePresent == false) {
                            if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId()) && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    } else {
                        if (coiDisclosureDetailsListBean.getCoiProjectId() != null && !coiDisclosureDetailsListBean.getCoiProjectId().equals("null")) {
                            if (!projectName.contains(coiDisclosureDetailsListBean.getCoiProjectId())) {
                                projectName.add(coiDisclosureDetailsListBean);
                            }
                        }
                    }}
                }
            }
            if (projectName!=null && projectName.isEmpty()) {
                request.setAttribute("projectMessage", false);
            }
           // UtilFactory.log("projectName is " + projectName);

            if (projectName != null && !projectName.isEmpty()) {
                request.setAttribute("tvlProjectNameList", projectName);
            }
            if (lFinalFinEntDet != null && !lFinalFinEntDet.isEmpty()) {
                request.setAttribute("tvlEntPjtDetView", lFinalFinEntDet);
            }
    }
    /**
     * Added for checking whether disclosure available
     */
    private String userHasDisclosure(HttpServletRequest request,String personId) throws Exception {

        WebTxnBean webTxn = new WebTxnBean();
        String hasDisclosure = null;
        String disclNmbr = null;          
        HashMap hasRightMap = new HashMap();
        HashMap hmData = new HashMap();
        hmData.put("userId", personId);
        Hashtable hasRightHashtable = (Hashtable) webTxn.getResults(request, "fnGetUserCoiV2Discl", hmData);
        hasRightMap = (HashMap) hasRightHashtable.get("fnGetUserCoiV2Discl");
        if (hasRightMap != null && hasRightMap.size() > 0) {
            disclNmbr = ((String) hasRightMap.get("disclosureAvailable"));
            if(disclNmbr!=null && !disclNmbr.equalsIgnoreCase("0")){               
               hasDisclosure = disclNmbr ;
            }
        }
        return hasDisclosure;
    }

    //Added by Jaisha
    private void getBodySubheaderDetails(HttpServletRequest request) throws Exception {
        ServletContext application = getServlet().getServletConfig().getServletContext();
        Vector vecCOISubHeader;
        ReadProtocolDetailsCoiV2 readProtocolDetails = new ReadProtocolDetailsCoiV2();
        vecCOISubHeader = (Vector) application.getAttribute(BODY_SUB_HEADER);
        if (vecCOISubHeader == null || vecCOISubHeader.size() == 0) {
            vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_BODY_HEADER);
            request.getSession().setAttribute(BODY_SUB_HEADER, vecCOISubHeader);
        }
        PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        request.getSession().setAttribute("person", person);
    }
    //Added by Jaisha

    public void setDisclosureNumberIfAnnualDisclosureAvailable(HttpServletRequest request)throws Exception{
        WebTxnBean webTxn = new WebTxnBean();
        HttpSession session = request.getSession();
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = personBean.getPersonID();
        CoiDisclosureBean lcoiDisclosurebean=new CoiDisclosureBean();
        Vector lcoiDisclosurebeanVector=null;
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable getDisclosureNumberforAnnualHashtable = (Hashtable) webTxn.getResults(request, "getAnnualDisclnewCoiv2", hmData);
        lcoiDisclosurebeanVector = (Vector) getDisclosureNumberforAnnualHashtable.get("getAnnualDisclnewCoiv2");

        if (lcoiDisclosurebeanVector != null && lcoiDisclosurebeanVector.size() > 0) {
           lcoiDisclosurebean=(CoiDisclosureBean) lcoiDisclosurebeanVector.get(0);
        }
        String disclosureNumber=null;
        if(lcoiDisclosurebean!=null){
               disclosureNumber=lcoiDisclosurebean.getCoiDisclosureNumber();
               session.setAttribute("annualDisclosureNumber", disclosureNumber);
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
      private void removeCOISessions(HttpServletRequest request){
       request.getSession().removeAttribute("projectType");
        request.getSession().removeAttribute("proposalList");
        request.getSession().removeAttribute("protocolList");
        request.getSession().removeAttribute("allAwardList");
        request.getSession().removeAttribute("approvedView");
        request.getSession().removeAttribute("currentSequence");
        request.getSession().removeAttribute("exitCode");
        request.getSession().removeAttribute("exitCodeNotes");
        request.getSession().removeAttribute("extProjListAll");
        request.getSession().removeAttribute("docAttachDescription");
        request.getSession().removeAttribute("extcodequestionnaire");
        request.getSession().removeAttribute("questionnaireModuleObjectextcode");
        request.getSession().removeAttribute("extcodeModuleKey");
        request.getSession().removeAttribute("AlreadySavedProjectsForExt");
        request.getSession().removeAttribute("extCodeAlreadySavedProjects");
        request.getSession().removeAttribute("projectDetailsListInSeesion_ext");
        request.getSession().removeAttribute("extCodeAlreadySavedProjects_upd");
        request.getSession().removeAttribute("financialArrayEntityList_ext");
        request.getSession().removeAttribute("financialEntityList_ext");
        request.getSession().removeAttribute("list");
        request.getSession().removeAttribute("param6");
        request.removeAttribute("ApprovedDisclDetView");
        request.removeAttribute("QstnAnsFlag");
        request.removeAttribute("annualQstnFlag");
        request.getSession().removeAttribute("QstnAnsFlag");
        request.getSession().removeAttribute("annualQstnFlag");
        request.getSession().removeAttribute("CREATEFLAG");
        request.getSession().removeAttribute("getinonce");
        request.getSession().removeAttribute("isEvent");
        request.getSession().removeAttribute("historyView");
        request.getSession().removeAttribute("coiprojectid");
        request.getSession().removeAttribute("fromReviewlist");
        request.getSession().removeAttribute("frmShowAllReviews");
        request.getSession().removeAttribute("fromAttachment");
        request.getSession().removeAttribute("DisclosureNumberInUpdateSession");
        request.getSession().removeAttribute("fromViewCurrent");
        request.getSession().removeAttribute("checkPrint");
        request.getSession().removeAttribute("fromViewerAction");
        request.getSession().removeAttribute("certified");
        request.getSession().removeAttribute("frmPendingInPrg");
        request.getSession().removeAttribute("coiMenuDatasaved");
        request.getSession().removeAttribute("isEventForInProgress");
        request.getSession().removeAttribute("contineWithoutQnr");
        request.getSession().removeAttribute("isReviewer");
        request.getSession().removeAttribute("reviewerUserId");
        request.getSession().removeAttribute("toFEPage");
        request.getSession().removeAttribute("noQuestionnaireForModule");
    }
}
