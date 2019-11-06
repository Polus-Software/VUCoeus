/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 *
 * @author UnNamed
 */
public class CoiCommonSaveAction extends COIBaseAction {
    private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";
    public static final Integer STATUS_IN_PROGRESS = new Integer(100);
    public static final Integer DISPOSITIONCODE = new Integer(3);

    private String SHRT_TXT_FIELD = "USE_SHRT_TXT_FLD";
    private String LNG_TXT_FIELD = "USE_LNG_TXT_FLD";
    private String DATE_FIELD = "USE_DATE_FLD";
    private String NBR_FIELD = "USE_NMBR_FLD";
    private String SLCT_FIELD = "USE_SLCT_BOX";

    private String SHRT_TXT_FIELD_LBL = "SHRT_TXT_FLD";
    private String LNG_TXT_FIELD_LBL = "LNG_TXT_FLD";
    private String DATE_FIELD_LBL = "DATE_FLD";
    private String NBR_FIELD_LBL = "NMBR_FLD";
    private String SLCT_FIELD_LBL = "SLCT_BOX";

    private int PJT_SHRT_TXT_COUNT = 3;
    private int PJT_LNG_TXT_COUNT = 3;
    private int PJT_NMB_COUNT = 2;
    private int PJT_DATE_COUNT = 2;
    private int PJT_SLT_COUNT = 1;

    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {       
       String forward = "success";
       //request.setAttribute("disableCoiMenu",true);// to disable the questionnaire menu
       // request.getSession().removeAttribute("disableCoiMenu");
       request.setAttribute("DiscViewTravel", true);
       CoiCommonService coiCommonService1 = CoiCommonService.getInstance();       
       String disclosureNumber = null;
       Integer sequenceNumber = null;
       String moduleItemKey = null;
       CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");
       if(coiInfoBean==null){
          coiInfoBean =new CoiInfoBean();
       } 
       disclosureNumber = coiInfoBean.getDisclosureNumber();
       sequenceNumber =  coiInfoBean.getSequenceNumber();
       String projectType = coiInfoBean.getProjectType();
       if (projectType!=null && projectType.equalsIgnoreCase(ANNUAL)){
          disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
         if(disclosureNumber==null){
          disclosureNumber = getNextDisclNum(request);
          sequenceNumber = 1;
          coiInfoBean.setDisclosureNumber(disclosureNumber);
          coiInfoBean.setSequenceNumber(sequenceNumber);
          request.setAttribute("disclosureAvailableMessage", false);
          }else{
             sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
             sequenceNumber = sequenceNumber + 1;
             coiInfoBean.setSequenceNumber(sequenceNumber);  
            request.setAttribute("disclosureAvailableMessage", true);
            CoiDisclosureBean annualBean = new CoiDisclosureBean();
            annualBean = coiCommonService1.getAnnualDisclosure(request);
            request.setAttribute("annualDisclosureBean", annualBean);
         } 
         moduleItemKey = null;
         saveDisclosure(request,coiInfoBean,moduleItemKey);
       }
       if (projectType!=null && projectType.equalsIgnoreCase(REVISION)){           
          disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
           if(disclosureNumber==null){
              disclosureNumber = getNextDisclNum(request);
              sequenceNumber = 1;
              coiInfoBean.setDisclosureNumber(disclosureNumber);
              coiInfoBean.setSequenceNumber(sequenceNumber);
              request.setAttribute("disclosureAvailableMessage", false);
          }else{
              request.setAttribute("disclosureAvailableMessage", true);
              sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
              sequenceNumber = sequenceNumber + 1;
              coiInfoBean.setSequenceNumber(sequenceNumber);  
           }
          moduleItemKey = null;
          saveDisclosure(request,coiInfoBean,moduleItemKey);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(PROPOSAL)){  
           disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
           if(disclosureNumber==null){
              disclosureNumber = getNextDisclNum(request);
              sequenceNumber = 1;
              coiInfoBean.setDisclosureNumber(disclosureNumber);
              coiInfoBean.setSequenceNumber(sequenceNumber);
              request.setAttribute("disclosureAvailableMessage", false);
          }else{
              request.setAttribute("disclosureAvailableMessage", true);
              sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
              sequenceNumber = sequenceNumber + 1;
              coiInfoBean.setSequenceNumber(sequenceNumber);  
           }
          saveProposal(request,coiInfoBean,actionForm);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(PROTOCOL)){  
          disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
           if(disclosureNumber==null){
              disclosureNumber = getNextDisclNum(request);
              sequenceNumber = 1;
              coiInfoBean.setDisclosureNumber(disclosureNumber);
              coiInfoBean.setSequenceNumber(sequenceNumber);
              request.setAttribute("disclosureAvailableMessage", false);
          }else{
              request.setAttribute("disclosureAvailableMessage", true);
              sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
              sequenceNumber = sequenceNumber + 1;
              coiInfoBean.setSequenceNumber(sequenceNumber);  
           }         
          saveProtocol(request,coiInfoBean,actionForm);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(IACUCPROTOCOL)){  
          disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
           if(disclosureNumber==null){
              disclosureNumber = getNextDisclNum(request);
              sequenceNumber = 1;
              coiInfoBean.setDisclosureNumber(disclosureNumber);
              coiInfoBean.setSequenceNumber(sequenceNumber);
              request.setAttribute("disclosureAvailableMessage", false);
          }else{
              request.setAttribute("disclosureAvailableMessage", true);
              sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
              sequenceNumber = sequenceNumber + 1;
              coiInfoBean.setSequenceNumber(sequenceNumber);  
           }          
          saveIACUCProtocol(request,coiInfoBean,actionForm);
       } 
       if (projectType!=null && projectType.equalsIgnoreCase(AWARD)){  
          disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
           if(disclosureNumber==null){
              disclosureNumber = getNextDisclNum(request);
              sequenceNumber = 1;
              coiInfoBean.setDisclosureNumber(disclosureNumber);
              coiInfoBean.setSequenceNumber(sequenceNumber);
              request.setAttribute("disclosureAvailableMessage", false);
          }else{
              request.setAttribute("disclosureAvailableMessage", true);
              sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
              sequenceNumber = sequenceNumber + 1;
              coiInfoBean.setSequenceNumber(sequenceNumber);  
           }        
          saveAward(request,coiInfoBean,actionForm);
       } 
        if (projectType!=null && projectType.equalsIgnoreCase(TRAVEL)){  
          disclosureNumber = userHasDisclosure(request,coiInfoBean.getPersonId());
           if(disclosureNumber==null){
              disclosureNumber = getNextDisclNum(request);
              sequenceNumber = 1;
              coiInfoBean.setDisclosureNumber(disclosureNumber);
              coiInfoBean.setSequenceNumber(sequenceNumber);
              request.setAttribute("disclosureAvailableMessage", false);
          }else{
              request.setAttribute("disclosureAvailableMessage", true);
              sequenceNumber = getNextSeqNumDisclosure(request,disclosureNumber);
              sequenceNumber = sequenceNumber + 1;
              coiInfoBean.setSequenceNumber(sequenceNumber);  
           }          
          saveTravel(request,coiInfoBean,actionForm);
          request.getSession().removeAttribute("disableCoiMenu");
       } 
        // to get the questionnaire details
         String isValid = getCoiQuestionnaire(coiInfoBean, request);
            if (isValid != null && isValid.equals("inValid")) {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noQuestionnaire",
                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");
            }
          if(isValid == null){
             request.getSession().setAttribute("noQuestionnaireForModule", true);
             return actionMapping.findForward("continue");
         }
        // to get the questionnaire details 
       // sub header details S T A R T S      
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();
        getCoiPersonDetails(personId,request);
        disclosureNumber=null;
        sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber = coiInfoBean.getSequenceNumber();
        if(sequenceNumber == null){
            sequenceNumber = coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);     
       // sub header details E N D S  
      //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
     //Menu Saved ends 
        request.getSession().setAttribute("CoiInfoBean",coiInfoBean);
        return actionMapping.findForward(forward); 
    }
     /**
     * Function to getNextDisclosureNUmber
     * @param request
     * @return
     * @throws Exception
     */
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

    public int getNextSeqNumDisclosure(HttpServletRequest request, String disclosureNumber) throws Exception {
        int seq = 0;
        HashMap hmData = new HashMap();
        hmData.put("disclosureNumber", disclosureNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htSyncData = (Hashtable) webTxnBean.getResults(request, "getMaxDisclSeqNumberCoiv2", hmData);
        HashMap hmSeq = (HashMap) htSyncData.get("getMaxDisclSeqNumberCoiv2");
        seq = Integer.parseInt(hmSeq.get("ll_Max").toString());
        return seq;
    }
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
    private void saveDisclosure(HttpServletRequest request ,CoiInfoBean coiInfoBean,String moduleItemKey) throws DBException, IOException, Exception{
        CoiDisclosureBean perDiscl = new CoiDisclosureBean();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
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
        else {
            Calendar cal=Calendar.getInstance();
            cal.setTime(date1);
            cal.add(Calendar.MONTH, 12);
            expirationDate = cal.getTime();
            perDiscl.setExpirationDate(expirationDate);
        }
            perDiscl.setAcType("I");
            request.getSession().setAttribute("DisclStatusCode", STATUS_IN_PROGRESS);
            perDiscl.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
      if(coiInfoBean.getProjectType().equalsIgnoreCase(ANNUAL)) {
         perDiscl.setModuleCode(ModuleConstants.COI_EVENT_ANNUAL); 
         perDiscl.setPjctName(ANNUAL);
         perDiscl.setModuleItemKey(moduleItemKey);
      } 
      else if(coiInfoBean.getProjectType().equalsIgnoreCase(REVISION)) {
          perDiscl.setModuleCode(ModuleConstants.COI_EVENT_REVISION);
          perDiscl.setPjctName(REVISION);
          perDiscl.setModuleItemKey(moduleItemKey);
      } 
      else if(coiInfoBean.getProjectType().equalsIgnoreCase(AWARD)) {
          perDiscl.setModuleCode(ModuleConstants.COI_EVENT_AWARD);
          perDiscl.setPjctName(AWARD);
          perDiscl.setModuleItemKey(moduleItemKey);
      } 
      else if(coiInfoBean.getProjectType().equalsIgnoreCase(PROPOSAL)) {
          perDiscl.setModuleCode(ModuleConstants.COI_EVENT_PROPOSAL);
          perDiscl.setPjctName(PROPOSAL);
          perDiscl.setModuleItemKey(moduleItemKey);
      } 
      else if(coiInfoBean.getProjectType().equalsIgnoreCase(PROTOCOL)) {
          perDiscl.setModuleCode(ModuleConstants.COI_EVENT_PROTOCOL);
          perDiscl.setPjctName(PROTOCOL);
          perDiscl.setModuleItemKey(moduleItemKey);
      } 
      else if(coiInfoBean.getProjectType().equalsIgnoreCase(IACUCPROTOCOL)) {
          perDiscl.setModuleCode(ModuleConstants.COI_EVENT_IACUC);
          perDiscl.setPjctName(IACUCPROTOCOL);
          perDiscl.setModuleItemKey(moduleItemKey);
      } 
      else if(coiInfoBean.getProjectType().equalsIgnoreCase(TRAVEL)) {
          perDiscl.setModuleCode(ModuleConstants.COI_EVENT_TRAVEL);
          perDiscl.setPjctName(TRAVEL);
          perDiscl.setModuleItemKey(moduleItemKey);
      }                                 
             
            perDiscl.setSequenceNumber(coiInfoBean.getSequenceNumber());
            perDiscl.setPersonId(coiInfoBean.getPersonId());
            perDiscl.setDisclosureStatusCode(STATUS_IN_PROGRESS);
            perDiscl.setUpdateUser(coiInfoBean.getUserId());        
            Integer reviewStatus=1;
            perDiscl.setReviewStatusCode(reviewStatus);
            perDiscl.setDisclosureDispositionCode(DISPOSITIONCODE);            
            webTxnBean.getResults(request, "coiPersonDisclosureCoiv2", perDiscl);  
        
        
    }
     private void saveProposal(HttpServletRequest request ,CoiInfoBean coiInfoBean,ActionForm actionForm) throws DBException, IOException, Exception{
         Vector projectDetails = new Vector();
         HashMap proposalInput = new HashMap();
         CoiPersonProjectDetails projectDet  = new CoiPersonProjectDetails(); 
         projectDet.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
         projectDet.setSequenceNumber(coiInfoBean.getSequenceNumber());
         projectDet.setPersonId(coiInfoBean.getPersonId());
         proposalInput.put("coiDisclosureNumber", coiInfoBean.getDisclosureNumber());
         proposalInput.put("sequenceNumber", coiInfoBean.getSequenceNumber());
         HttpSession session = request.getSession();
         String proposalNumber = null;
         Boolean nonIntegrated = false;
         Double amount = 0.00;
         String role = "testRole";
         String proposalType = null;         
         String[] checkedPropsalProjects = request.getParameterValues("checkedPropsalProjects");
          if(checkedPropsalProjects == null){ //checking whether it is from mail process
             checkedPropsalProjects = (String[]) request.getAttribute("checkedPropsalProjects");
         }
         if(checkedPropsalProjects!=null){
            for (int i = 0; i < checkedPropsalProjects.length; i++)
            {
                        String propVal = checkedPropsalProjects[i];
                        String[] splitList = propVal.split(";");
                        proposalNumber = splitList[0];                        
                        String propName = splitList[1];
                        if(splitList.length>6 && splitList[6]!=null && splitList[6].trim().length()>0){
                        amount=Double.valueOf(splitList[6].trim()).doubleValue(); 
                        }
                        if(splitList.length >7){
                        proposalType = splitList[7];
                        }
                        if(splitList.length >8){
                        role = splitList[8];
                        }                        
                        if(splitList.length>5 && splitList[5]!=null && splitList[5].trim().length()>0){
                        nonIntegrated= Boolean.parseBoolean(splitList[5]);
                        }
                        session.setAttribute("checkedproposal", proposalNumber);                       
                        request.getSession().setAttribute("InPrgssProjectID",proposalNumber);                       
                        session.setAttribute("pjctname", propName);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");              
                projectDet.setCoiProjectSponser(splitList[2]);
                if(splitList.length > 3) {
                    Date date = null;
                     String d=splitList[3];
                    try {
                        date = dateFormat.parse(d);
                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                        proposalInput.put("DATE_FLD_1", date);
                    }

                    if(splitList.length > 4) {
                         Date date1=null;
                        String d1=splitList[4];
                        try {
                             date1 = dateFormat.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                         proposalInput.put("DATE_FLD_2", date1);
                    }
                    coiInfoBean.setProjectId(splitList[0]);
                projectDet.setCoiProjectTitle(splitList[1]);
                 proposalInput.put("LNG_TXT_FLD_1", splitList[1]);
                projectDet.setModuleCode(coiInfoBean.getModuleCode());
                 proposalInput.put("moduleCode", coiInfoBean.getModuleCode());
                projectDet.setModuleItemKey(splitList[0]);
                 proposalInput.put("moduleItemKey", splitList[0]);
                projectDet.setCoiProjectId(splitList[0]);
                proposalInput.put("coiProjectId", splitList[0]);
                projectDet.setNonIntegrated(nonIntegrated);
                projectDet.setCoiProjectSponser(splitList[2]);
                proposalInput.put("SHRT_TXT_FLD_1", splitList[2]);
                projectDet.setCoiProjectFundingAmount(amount);
                proposalInput.put("NMBR_FLD_1", amount);
                projectDet.setCoiProjectType(proposalType);
                projectDet.setCoiProjectRole(role);
                proposalInput.put("SHRT_TXT_FLD_2", role);
                projectDet.setCoiProjectTitle(propName);
                proposalInput.put("LNG_TXT_FLD_1", propName);
                proposalInput.put("LNG_TXT_FLD_2", null);
                projectDet.setEventTypeCode(ModuleConstants.COI_EVENT_PROPOSAL);
                proposalInput.put("eventTypeCode", ModuleConstants.COI_EVENT_PROPOSAL);
                projectDet.setAcType("I");
                proposalInput.put("acType", "I");
                projectDetails.add(projectDet);

                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);
              //keeping project list in session
            }
         }         
            request.getSession().setAttribute("checkedPropsalProjects", checkedPropsalProjects);
            saveDisclosure(request,coiInfoBean,proposalNumber);
             if(nonIntegrated){
              saveNonIntegratedProjects(request,proposalInput);
            }            
     }
      private void saveNonIntegratedProjects(HttpServletRequest request,HashMap inputMap) throws DBException, IOException, Exception{
          WebTxnBean webTxn = new WebTxnBean();

              webTxn.getResults(request, "updateCoiProjectDetails", inputMap);

      }
      private void saveProtocol(HttpServletRequest request ,CoiInfoBean coiInfoBean,ActionForm actionForm) throws DBException, IOException, Exception{
         Vector projectDetails = new Vector();
         CoiPersonProjectDetails projectDet  = new CoiPersonProjectDetails(); 
         projectDet.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
         projectDet.setSequenceNumber(coiInfoBean.getSequenceNumber());
         projectDet.setPersonId(coiInfoBean.getPersonId());
         HttpSession session = request.getSession();
         String protocolNumber = null;
         String title = null;
         Boolean nonIntegrated = false;                 
         String[] checkedProtocolBasedProjects = request.getParameterValues("checkedProtocolBasedProjects");
         if(checkedProtocolBasedProjects!=null){
               for (int i = 0; i < checkedProtocolBasedProjects.length; i++) {
                    String protocolVal = checkedProtocolBasedProjects[i];
                    String[] protocolValArr = protocolVal.split(";");
                    projectDet = new CoiPersonProjectDetails();
                    if(protocolValArr!=null && protocolValArr.length>0)
                    protocolNumber = protocolValArr[0]; 
                    title = protocolValArr[1];                    
                    if(protocolValArr.length > 2) {
                        Date date5 = null;
                         String d5=protocolValArr[2];
                        try {
                              DateFormat formatter ;
                              formatter = new SimpleDateFormat("yyyy-MM-dd");
                              date5 = (Date)formatter.parse(d5);
                               } catch (ParseException e)
                              {}
                            projectDet.setCoiProjectStartDate(date5);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                      Date date6 = null;
                     String d6=protocolValArr[3];
                    try {
                      DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date6 = (Date)formatter.parse(d6);
                       } catch (ParseException e)
                      {}projectDet.setCoiProjectEndDate(date6);

                    }else {
                        projectDet.setCoiProjectEndDate(null);
                    }  
                    coiInfoBean.setProjectId(protocolNumber);
                    projectDet.setNonIntegrated(nonIntegrated);
                    projectDet.setEventTypeCode(ModuleConstants.COI_EVENT_PROTOCOL);
                    projectDet.setAcType("I");
                    projectDet.setCoiProjectId(protocolNumber);
                    projectDet.setCoiProjectTitle(title); 
                    projectDet.setModuleCode(coiInfoBean.getModuleCode());                   
                    projectDet.setModuleItemKey(protocolNumber);
                    projectDetails.add(projectDet); 
                    session.setAttribute("checkedprotocolno", protocolNumber);                       
                    request.getSession().setAttribute("InPrgssProjectID",protocolNumber); 
                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
         }         
            request.getSession().setAttribute("checkedProtocolBasedProjects", checkedProtocolBasedProjects);            
            saveDisclosure(request,coiInfoBean,protocolNumber);
     }
         private void saveIACUCProtocol(HttpServletRequest request ,CoiInfoBean coiInfoBean,ActionForm actionForm) throws DBException, IOException, Exception{
         Vector projectDetails = new Vector();
         HttpSession session = request.getSession();
         CoiPersonProjectDetails projectDet  = new CoiPersonProjectDetails(); 
         projectDet.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
         projectDet.setSequenceNumber(coiInfoBean.getSequenceNumber());
         projectDet.setPersonId(coiInfoBean.getPersonId());         
         String protocolNumber = null;
         String title = null;
         Boolean nonIntegrated = false;                 
         String[] checkedIACUCProtocolBasedProjects = request.getParameterValues("checkedIacucProtocolBasedProject");
         if(checkedIACUCProtocolBasedProjects!=null){
               for (int i = 0; i < checkedIACUCProtocolBasedProjects.length; i++) {
                    String protocolVal = checkedIACUCProtocolBasedProjects[i];
                    String[] protocolValArr = protocolVal.split(";");
                    projectDet = new CoiPersonProjectDetails();
                    if(protocolValArr!=null && protocolValArr.length>0)
                    protocolNumber = protocolValArr[0]; 
                    title = protocolValArr[1];                    
                    if(protocolValArr.length > 2) {
                        Date date5 = null;
                         String d5=protocolValArr[2];
                        try {
                              DateFormat formatter ;
                              formatter = new SimpleDateFormat("yyyy-MM-dd");
                              date5 = (Date)formatter.parse(d5);
                               } catch (ParseException e)
                              {}
                            projectDet.setCoiProjectStartDate(date5);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                      Date date6 = null;
                     String d6=protocolValArr[3];
                    try {
                      DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date6 = (Date)formatter.parse(d6);
                       } catch (ParseException e)
                      {}projectDet.setCoiProjectEndDate(date6);

                    }else {
                        projectDet.setCoiProjectEndDate(null);
                    }  
                    coiInfoBean.setProjectId(protocolNumber);
                    projectDet.setNonIntegrated(nonIntegrated);
                    projectDet.setEventTypeCode(ModuleConstants.COI_EVENT_IACUC);
                    projectDet.setAcType("I");
                    projectDet.setCoiProjectId(protocolNumber);
                    projectDet.setCoiProjectTitle(title); 
                    projectDet.setModuleCode(coiInfoBean.getModuleCode());                   
                    projectDet.setModuleItemKey(protocolNumber);
                    projectDetails.add(projectDet); 
                session.setAttribute("checkediacucprotocolno", protocolNumber);                       
                request.getSession().setAttribute("InPrgssProjectID",protocolNumber);     
                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
         }         
            request.getSession().setAttribute("checkedIacucProtocolBasedProject", checkedIACUCProtocolBasedProjects);            
            saveDisclosure(request,coiInfoBean,protocolNumber);
     } 
         private void saveTravel(HttpServletRequest request ,CoiInfoBean coiInfoBean,ActionForm actionForm) throws DBException, IOException, Exception{
         Vector projectDetails = new Vector();
         CoiPersonProjectDetails projectDet  = new CoiPersonProjectDetails();
         projectDet.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
         projectDet.setSequenceNumber(coiInfoBean.getSequenceNumber());
         projectDet.setPersonId(coiInfoBean.getPersonId());
         String projectId = null;
         Boolean nonIntegrated = true;
         HashMap travelFields = new HashMap();
        int shrtTxtField_count = 0;
        int lngTxtField_count = 0;
        int dateField_count = 0;
        int SlctField_count = 0;
        int nmbrField_count = 0;

         if(request.getSession().getAttribute("travelInputFields") != null) {
             travelFields = (HashMap)request.getSession().getAttribute("travelInputFields");
         }
        if(request.getSession().getAttribute("shrtTxtCount") != null) {
             shrtTxtField_count = Integer.parseInt(request.getSession().getAttribute("shrtTxtCount").toString());
         }
        if(request.getSession().getAttribute("lngTxtCount") != null) {
             lngTxtField_count = Integer.parseInt(request.getSession().getAttribute("lngTxtCount").toString());
         }
        if(request.getSession().getAttribute("nmrFldCount") != null) {
             nmbrField_count = Integer.parseInt(request.getSession().getAttribute("nmrFldCount").toString());
         }
        if(request.getSession().getAttribute("dateCount") != null) {
             dateField_count = Integer.parseInt(request.getSession().getAttribute("dateCount").toString());
         }
        if(request.getSession().getAttribute("selectCount") != null) {
             SlctField_count = Integer.parseInt(request.getSession().getAttribute("selectCount").toString());
         }
         String field1="";
         String id1="";

         String fieldValue = "";

         String shrtTxtFld = "SHRT_TXT_FLD";
         String lngTxtFld = "LNG_TXT_FLD";
         String nmbFld = "NMBR_FLD";
         String dateFld ="DATE_FLD";
         String sltFld = "SLCT_BOX";

        HashMap travelInputMap = new HashMap();

         if(shrtTxtField_count > 0){
            String useShtFld = "";
            String srtTxtLabel  = "";
            for(int i=1; i <= PJT_SHRT_TXT_COUNT; i++ ){
                useShtFld = SHRT_TXT_FIELD+"_"+i;
                fieldValue = shrtTxtFld+"_"+i;
                if(travelFields.containsKey(useShtFld) && travelFields.get(useShtFld).toString().equalsIgnoreCase("Y")) {
                    srtTxtLabel = SHRT_TXT_FIELD_LBL+"_"+i+"_LABEL";

                    if(travelFields.get(srtTxtLabel) != null) {
                        field1 = travelFields.get(srtTxtLabel).toString();
                        id1 = field1.replaceAll(" ", "_");
                        travelInputMap.put(fieldValue, request.getParameter(id1));
                        if(i==1){
                            projectDet.setCoiProjectTitle(request.getParameter(id1));
                        }
                    }
                }
                else{
                   travelInputMap.put(fieldValue, null);
                }
            }
         }else {
            for(int i=1; i <= PJT_SHRT_TXT_COUNT; i++ ){
              fieldValue = shrtTxtFld+"_"+i;
              travelInputMap.put(fieldValue, request.getParameter(id1));
            }
         }

         if(lngTxtField_count > 0){
            String useLngFld = "";
            String lngTxtLabel  = "";
            for(int i=1; i <= PJT_LNG_TXT_COUNT; i++ ){
                useLngFld = LNG_TXT_FIELD+"_"+i;
                fieldValue = lngTxtFld+"_"+i;
                if(travelFields.containsKey(useLngFld) && travelFields.get(useLngFld).toString().equalsIgnoreCase("Y")) {
                    lngTxtLabel = LNG_TXT_FIELD_LBL+"_"+i+"_LABEL";

                    if(travelFields.get(lngTxtLabel) != null) {
                        field1 = travelFields.get(lngTxtLabel).toString();
                        id1 = field1.replaceAll(" ", "_");
                       travelInputMap.put(fieldValue, request.getParameter(id1));
                    }
                }else {
                    travelInputMap.put(fieldValue, null);
                }
            }
         }else{
            for(int i=1; i <= PJT_LNG_TXT_COUNT; i++ ){
              fieldValue = lngTxtFld+"_"+i;
              travelInputMap.put(fieldValue, null);
            }
         }

        if(nmbrField_count > 0){
            String useNmbFld = "";
            String NmTxtLabel  = "";

            for(int i=1; i <= PJT_NMB_COUNT; i++ ){
                useNmbFld = NBR_FIELD+"_"+i;
                fieldValue = nmbFld+"_"+i;
                if(travelFields.containsKey(useNmbFld) && travelFields.get(useNmbFld).toString().equalsIgnoreCase("Y")) {
                    NmTxtLabel = NBR_FIELD_LBL+"_"+i+"_LABEL";

                    if(travelFields.get(NmTxtLabel) != null) {
                        field1 = travelFields.get(NmTxtLabel).toString();
                        String lb = field1.replaceAll("\\(", "");
                        field1 = lb.replaceAll("\\)", "");
                        id1 = field1.replaceAll(" ", "_");
                        Double inValue = Double.parseDouble(request.getParameter(id1));
                        travelInputMap.put(fieldValue, inValue);
                    }
                }else{
                   travelInputMap.put(fieldValue, 0.0);
                }
            }
        }else{
            for(int i=1; i <= PJT_NMB_COUNT; i++ ){
                 fieldValue = nmbFld+"_"+i;
                 travelInputMap.put(fieldValue, 0.0);
            }
        }

        if(dateField_count > 0){
            String useNmbFld = "";
            String NmTxtLabel  = "";

            for(int i=1; i <= PJT_DATE_COUNT; i++ ){
                useNmbFld = DATE_FIELD+"_"+i;
                fieldValue = dateFld+"_"+i;
                 Date date = null;
                if(travelFields.containsKey(useNmbFld) && travelFields.get(useNmbFld).toString().equalsIgnoreCase("Y")) {
                    NmTxtLabel = DATE_FIELD_LBL+"_"+i+"_LABEL";

                    if(travelFields.get(NmTxtLabel) != null) {
                        field1 = travelFields.get(NmTxtLabel).toString();
                        id1 = field1.replaceAll(" ", "_");
                        String dateValue = request.getParameter(id1);
                        DateFormat formatter ;
                              formatter = new SimpleDateFormat("MM/dd/yyyy");
                              if(dateValue!=null && dateValue.trim().length()!=0){
                              date = formatter.parse(dateValue);
                              }
                        travelInputMap.put(fieldValue, date);

                        if(i == 1) {
                            projectDet.setCoiProjectStartDate(date);
                        }else if(i==2){
                            projectDet.setCoiProjectEndDate(date);
                        }
                    }
                }else {
                  travelInputMap.put(fieldValue, null);
                }
            }
        }else{
            for(int i=1; i <= PJT_DATE_COUNT; i++ ){
                fieldValue = dateFld+"_"+i;
                 travelInputMap.put(fieldValue, null);
            }
        }

        if(SlctField_count > 0){
            String useNmbFld = "";
            String NmTxtLabel  = "";

            for(int i=1; i <= PJT_SLT_COUNT; i++ ){
                useNmbFld = SLCT_FIELD+"_"+i;
                fieldValue = sltFld+"_"+i;
                if(travelFields.containsKey(useNmbFld) && travelFields.get(useNmbFld).toString().equalsIgnoreCase("Y")) {
                    NmTxtLabel = SLCT_FIELD_LBL+"_"+i+"_LABEL";

                    if(travelFields.get(NmTxtLabel) != null) {
                        field1 = travelFields.get(NmTxtLabel).toString();
                        id1 = field1.replaceAll(" ", "_");
                        travelInputMap.put(fieldValue, request.getParameter(id1));
                    }
                }
                else{
                    travelInputMap.put(fieldValue, null);
                }
            }
        }else{
            for(int i=1; i <= PJT_SLT_COUNT; i++ ){
                fieldValue = sltFld+"_"+i;
                travelInputMap.put(fieldValue, null);
            }
        }
            travelInputMap.put("coiDisclosureNumber", coiInfoBean.getDisclosureNumber());
            travelInputMap.put("sequenceNumber", coiInfoBean.getSequenceNumber());
            projectId = getNextProjectId(request);
            travelInputMap.put("eventTypeCode", ModuleConstants.COI_EVENT_TRAVEL);
            travelInputMap.put("acType", "I");
            travelInputMap.put("moduleCode", coiInfoBean.getModuleCode());
            travelInputMap.put("moduleItemKey", projectId);
            travelInputMap.put("coiProjectId",projectId);
            coiInfoBean.setProjectId(projectId);
            projectDet.setModuleItemKey(projectId);
            projectDet.setModuleCode(coiInfoBean.getModuleCode());
            projectDet.setEventTypeCode(ModuleConstants.COI_EVENT_TRAVEL);

            projectDetails.add(projectDet);
            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            request.getSession().setAttribute("travelPjtDetails", travelInputMap);

            saveDisclosure(request,coiInfoBean,projectId);
            request.getSession().setAttribute("CoiInfoBean",coiInfoBean);
            if(nonIntegrated){
                saveNonIntegratedProjects(request,travelInputMap);
            }
     }
      private void saveAward(HttpServletRequest request ,CoiInfoBean coiInfoBean,ActionForm actionForm) throws DBException, IOException, Exception{
         Vector projectDetails = new Vector();
          HttpSession session = request.getSession();
         CoiPersonProjectDetails projectDet  = new CoiPersonProjectDetails(); 
         projectDet.setCoiDisclosureNumber(coiInfoBean.getDisclosureNumber());
         projectDet.setSequenceNumber(coiInfoBean.getSequenceNumber());
         projectDet.setPersonId(coiInfoBean.getPersonId());         
         String awardNumber = null;
         String title = null;
         Boolean nonIntegrated = false;                 
         String[] checkedAwardBasedProjects = request.getParameterValues("checkedAwardBasedProjects");
         if(checkedAwardBasedProjects!=null){
               for (int i = 0; i < checkedAwardBasedProjects.length; i++) {
                    String awardVal = checkedAwardBasedProjects[i];
                    String[] awardValArr = awardVal.split(";");
                    projectDet = new CoiPersonProjectDetails();
                    if(awardValArr!=null && awardValArr.length>0)
                    awardNumber = awardValArr[0]; 
                    title = awardValArr[1];                    
                    if(awardValArr.length > 2) {
                        Date date5 = null;
                         String d5=awardValArr[2];
                        try {
                              DateFormat formatter ;
                              formatter = new SimpleDateFormat("yyyy-MM-dd");
                              date5 = (Date)formatter.parse(d5);
                               } catch (ParseException e)
                              {}
                            projectDet.setCoiProjectStartDate(date5);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }
                    if(awardValArr.length > 3) {
                      Date date6 = null;
                      String d6=awardValArr[3];
                     try {
                      DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date6 = (Date)formatter.parse(d6);
                       } catch (ParseException e)
                      {}projectDet.setCoiProjectEndDate(date6);

                    }else {
                        projectDet.setCoiProjectEndDate(null);
                    } 
                    String projectSponser=awardValArr[4];
                    coiInfoBean.setProjectId(awardNumber);
                    projectDet.setNonIntegrated(nonIntegrated);
                    projectDet.setEventTypeCode(ModuleConstants.COI_EVENT_AWARD);
                    projectDet.setAcType("I");
                    projectDet.setCoiProjectId(awardNumber);
                    projectDet.setCoiProjectTitle(title); 
                    projectDet.setAwardTitle(title);
                    projectDet.setModuleCode(coiInfoBean.getModuleCode());                   
                    projectDet.setModuleItemKey(awardNumber);
                    projectDet.setCoiProjectSponser(projectSponser);
                    projectDetails.add(projectDet);  
                session.setAttribute("checkedawardno", awardNumber);                       
                request.getSession().setAttribute("InPrgssProjectID",awardNumber);     
                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
         }         
            request.getSession().setAttribute("checkedAwardBasedProjects", checkedAwardBasedProjects);            
            saveDisclosure(request,coiInfoBean,awardNumber);
     } 
      private String getNextProjectId(HttpServletRequest request) throws DBException, IOException, Exception{ 
                String projid = null;
                WebTxnBean webTxnBean = new WebTxnBean();
                Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "fngetNextCoiPjtId", null);
                HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fngetNextCoiPjtId");
                if(hmfinEntityList !=null && hmfinEntityList.size()>0){
                    projid = hmfinEntityList.get("ls_seq").toString();
                }
           return projid;     
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
}
