/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.actions;
import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.coiv2.beans.CoiInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
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
 * @author anishk
 */
public class CoiInProgressAction extends COIBaseAction{
    private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";
    private static String COI_QUESTIONNAIRE ="/coiQuestionnaire";
    private static String COI_FROM_MAIL ="/coiMailInProgress";
    
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

    private int projectCount = 0;
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String navigator = "success";
        request.setAttribute("DiscViewTravel", true);
       request.setAttribute("DiscViewQtnr", true);//to check Questionnaire menu selected
        HttpSession session = request.getSession();
      boolean edit=Boolean.parseBoolean(request.getParameter("edit"));
        String disclosureNumber = null;
        Integer sequenceNumber = null;
        String personId = null;
        String projectId = null;
        Integer moduleCode = null;
        String eventCode = null;
        Vector questionDet = new Vector();
        WebTxnBean webTxn = new WebTxnBean();
        CoiInfoBean coiInfoBean = (CoiInfoBean)session.getAttribute("CoiInfoBean");
        if(coiInfoBean==null){
          coiInfoBean =new CoiInfoBean();
        }   
        if(actionMapping.getPath().equalsIgnoreCase(COI_QUESTIONNAIRE)){
            disclosureNumber = coiInfoBean.getDisclosureNumber();
            sequenceNumber = coiInfoBean.getSequenceNumber();
            personId = coiInfoBean.getPersonId();
            projectId = coiInfoBean.getProjectId();
            moduleCode = coiInfoBean.getModuleCode();
            eventCode = coiInfoBean.getProjectType();
        }
        else if(actionMapping.getPath().equalsIgnoreCase(COI_FROM_MAIL)){
            disclosureNumber = (String) request.getAttribute("discl");
            sequenceNumber = Integer.parseInt(request.getAttribute("seq").toString());
            personId = (String) request.getAttribute("personId");
            projectId = (String) request.getAttribute("projectID");       
            eventCode = (String)request.getAttribute("eventType"); 
        }
        else{
        disclosureNumber = request.getParameter("discl");
        sequenceNumber = Integer.parseInt(request.getParameter("seq").toString());
        personId = request.getParameter("personId");
        projectId = request.getParameter("projectID");
    //    moduleCode = Integer.parseInt(request.getParameter("moduleCode").toString());
         HashMap hmData = new HashMap();
          hmData.put("coiDisclosureNumber", disclosureNumber);
          hmData.put("sequenceNumber", sequenceNumber);
          hmData.put("personId", personId);
          Hashtable questionData = (Hashtable) webTxn.getResults(request, "getQnsAns", hmData);
          questionDet = (Vector) questionData.get("getQnsAns");
        eventCode = (String)request.getParameter("eventType");
            if(eventCode!=null && eventCode.equalsIgnoreCase("IRB Protocol")){
                eventCode =PROTOCOL;
            }
             if(eventCode!=null && eventCode.equalsIgnoreCase("IACUC Protocol")){
                eventCode =IACUCPROTOCOL;
            }
        coiInfoBean.setDisclosureNumber(disclosureNumber);
        coiInfoBean.setSequenceNumber(sequenceNumber);
        coiInfoBean.setPersonId(personId);
        coiInfoBean.setProjectType(eventCode);
        coiInfoBean.setProjectId(projectId); 
        coiInfoBean.setMenuType("InProgress");
        }
        request.getSession().setAttribute("projectType",eventCode);
        if(eventCode!=null && (eventCode.equalsIgnoreCase(ANNUAL)||eventCode.equalsIgnoreCase(REVISION))){
        projectCount = 0;
        getAllAward(request);
        getAllInstProposals(request);   
        getAllProposals(request);      
        getAllProtocolList(request);
        getAllIacucProtocolList(request);  
        coiInfoBean.setEventType(ModuleConstants.COI_EVENT_ANNUAL);
        coiInfoBean.setProjectCount(projectCount);
        coiInfoBean.setProjectId(null);
        }      
        else if(eventCode!=null && eventCode.equalsIgnoreCase(PROPOSAL)){
                    moduleCode = ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
                    coiInfoBean.setModuleCode(moduleCode); 
                    getEventProjects(request,disclosureNumber,sequenceNumber,moduleCode,projectId);
                    coiInfoBean.setEventType(ModuleConstants.COI_EVENT_PROPOSAL);                    
                    request.getSession().setAttribute("checkedproposal", projectId);
        }
        else if(eventCode!=null &&( eventCode.equalsIgnoreCase(PROTOCOL))){
                    moduleCode = ModuleConstants.PROTOCOL_MODULE_CODE;
                    coiInfoBean.setModuleCode(moduleCode); 
                    getEventProjects(request,disclosureNumber,sequenceNumber,moduleCode,projectId);    
                    coiInfoBean.setEventType(ModuleConstants.COI_EVENT_PROTOCOL);                    
                    request.getSession().setAttribute("checkedprotocolno", projectId);
        }
        else if(eventCode!=null && (eventCode.equalsIgnoreCase(IACUCPROTOCOL))){
                    moduleCode = ModuleConstants.IACUC_MODULE_CODE;
                    coiInfoBean.setModuleCode(moduleCode); 
                    getEventProjects(request,disclosureNumber,sequenceNumber,moduleCode,projectId);
                    coiInfoBean.setEventType(ModuleConstants.COI_EVENT_IACUC);
                    request.getSession().setAttribute("checkediacucprotocolno", projectId);
        }
        else if(eventCode!=null && eventCode.equalsIgnoreCase(AWARD)){
                    moduleCode = ModuleConstants.AWARD_MODULE_CODE;
                    coiInfoBean.setModuleCode(moduleCode); 
                    getEventProjects(request,disclosureNumber,sequenceNumber,moduleCode,projectId);
                    coiInfoBean.setEventType(ModuleConstants.COI_EVENT_AWARD);
                    request.getSession().setAttribute("checkedawardno", projectId);
        }
        else if(eventCode!=null && eventCode.equalsIgnoreCase(TRAVEL)){
                    moduleCode = ModuleConstants.TRAVEL_MODULE_CODE;
                    coiInfoBean.setModuleCode(moduleCode);
                    if((request.getParameter("eventName")) != null){
                    updateTravel(request,disclosureNumber,sequenceNumber);
                    }
                    getEventProjects(request,disclosureNumber,sequenceNumber,moduleCode,projectId);
                    coiInfoBean.setEventType(ModuleConstants.COI_EVENT_TRAVEL);
                    request.getSession().setAttribute("checkedtravelno", projectId);
                    request.getSession().removeAttribute("disableCoiMenu");
                    request.removeAttribute("DiscViewTravel");
                   
        }
         session.setAttribute("CoiInfoBean",coiInfoBean);
                // to get the questionnaire details
            String isValid = getCoiQuestionnaire(coiInfoBean, request);
            if (isValid != null && isValid.equals("inValid")) {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noQuestionnaire",
                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");
            }
             if(questionDet == null && isValid == null){
                 session.setAttribute("noQuestionnaireForModule", true);
                 return actionMapping.findForward("continue");
             }        
        // to get the questionnaire details 
       // sub header details S T A R T S      
       PersonInfoBean person = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String pId = person.getPersonID();
        getCoiPersonDetails(pId,request);
        disclosureNumber=null;
        sequenceNumber=null;
        disclosureNumber = coiInfoBean.getDisclosureNumber();
        sequenceNumber =coiInfoBean.getSequenceNumber();
        if(sequenceNumber == null){
            sequenceNumber =coiInfoBean.getApprovedSequence();
        }
        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);     
       // sub header details E N D S  
       //Menu Saved Start   
        request.setAttribute("byProjectMenu",true);
        if(coiInfoBean!=null){
        coiMenuDataSaved(coiInfoBean.getDisclosureNumber(),coiInfoBean.getSequenceNumber(),coiInfoBean.getPersonId(),request);
        }
        if(eventCode!=null && eventCode.equalsIgnoreCase(TRAVEL)){
         navigator="travelPage";
        }
         if(!edit){
            navigator="success";
            }
      
     //Menu Saved ends
        return actionMapping.findForward(navigator);
    }
     private Vector getAllAward(HttpServletRequest request) throws Exception {        
        Vector awardDet = null;  
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();       
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();
        HashMap hmData = new HashMap();
        hmData.put("updateUser", personId);
        hmData.put("discnumber", null);
        Hashtable awardDetList = (Hashtable) webTxn.getResults(request, "getAllNewAwards", hmData);
        awardDet = (Vector) awardDetList.get("getAllNewAwards");        
        if (awardDet != null && awardDet.size() > 0) {
            projectCount = projectCount + awardDet.size();
            request.setAttribute("allAwardList", awardDet);
            session.setAttribute("allAwardProjectList", awardDet);
            session.setAttribute("allAwardList", awardDet);
        }

        return awardDet;
    }
     private Vector getAllInstProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();      
        HashMap hmData = new HashMap();
        hmData.put("pid", personId);
        hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getInstProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getInstProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            projectCount = projectCount +  propUserDet.size();
            request.setAttribute("getInstProposals", propUserDet);
            session.setAttribute("getInstProposals", propUserDet);            
        }
        return propUserDet;
    }
      private Vector getAllProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;              
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();       
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllNewProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllNewProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            projectCount = projectCount + protocolDet.size();
            request.setAttribute("protocolList", protocolDet);
            session.setAttribute("protocolProjectListList", protocolDet);           
        }

        return protocolDet;
    }
     private Vector getAllIacucProtocolList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector protocolDet = null;       
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId = person.getPersonID();       
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
         hmData.put("discnumber",null);
        Hashtable protocolData = (Hashtable) webTxn.getResults(request, "getAllIACUCProtocolList", hmData);
        protocolDet = (Vector) protocolData.get("getAllIACUCProtocolList");
        if (protocolDet != null && protocolDet.size() > 0) {
            projectCount = projectCount + protocolDet.size();
            request.setAttribute("getAllIACUCProtocolList", protocolDet);
            session.setAttribute("getAllIACUCProtocolList", protocolDet);            
        }
        return protocolDet;
    }
        private Vector getAllProposals(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxn = new WebTxnBean();
        Vector propUserDet = null;
        PersonInfoBean personBean = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);       
        String personId = personBean.getPersonID();        
        HashMap hmData = new HashMap();
        hmData.put("createUser", personId);
           hmData.put("discnumber",null);
        Hashtable propUserRole = (Hashtable) webTxn.getResults(request, "getAllNewProposals", hmData);
        propUserDet = (Vector) propUserRole.get("getAllNewProposals");
        if (propUserDet != null && propUserDet.size() > 0) {
            projectCount = projectCount + propUserDet.size();
            request.setAttribute("proposalList", propUserDet);
            session.setAttribute("proposalListForAttachment", propUserDet);          
          }
        return propUserDet;
    }

     private void getEventProjects(HttpServletRequest request,String discl,Integer seq,Integer moduleCode,String projectId) throws Exception{
        HashMap hmData = new HashMap();
        Vector projectDetails = new Vector();
        CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();  
        hmData.put("coiDisclosureNumber", discl);
        hmData.put("sequenceNumber",seq);
        hmData.put("moduleCode", moduleCode);
        hmData.put("moduleItemKey",projectId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiProjectEntityDetailsBean coiProjectEntityDetailsBean = new CoiProjectEntityDetailsBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getCoiProjectDetails", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getCoiProjectDetails");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            coiProjectEntityDetailsBean = (CoiProjectEntityDetailsBean) apprvdDiscl.get(0);
            projectDet.setModuleCode(moduleCode);
            projectDet.setCoiProjectTitle(coiProjectEntityDetailsBean.getCoiProjectTitle());
            projectDet.setAwardTitle(coiProjectEntityDetailsBean.getCoiProjectTitle());

            if(moduleCode == 0) {              
               projectDet.setEventName(coiProjectEntityDetailsBean.getEventName());
               projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getEventName()) ;
               projectDet.setPurpose(coiProjectEntityDetailsBean.getPurpose());
            }else {
                projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getCoiProjectSponsor());
            }
            projectDet.setCoiProjectStartDate(coiProjectEntityDetailsBean.getCoiProjectStartDate());
            projectDet.setCoiProjectEndDate(coiProjectEntityDetailsBean.getCoiProjectEndDate());  
            projectDet.setModuleItemKey(projectId);
          //  projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getCoiProjectSponsor());
            projectDetails.add(projectDet);     
            request.setAttribute("proposalList", projectDetails);
            request.setAttribute("InProgProjectList", projectDetails);
            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);
        } 
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
     private void updateTravel(HttpServletRequest request,String discl,Integer seq) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        CoiPersonProjectDetails projectDet  = new CoiPersonProjectDetails(); 
         projectDet.setCoiDisclosureNumber(discl);
         projectDet.setSequenceNumber(seq);
         projectDet.setEventTypeCode(8);
         String projectId = null;
         HashMap travelFields = new HashMap();
        int shrtTxtField_count = 0;
        int lngTxtField_count = 0;
        int dateField_count = 0;
        int SlctField_count = 0;
        int nmbrField_count = 0;
        CoiInfoBean coiInfoBean = (CoiInfoBean)request.getSession().getAttribute("CoiInfoBean");

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
                   travelInputMap.put(fieldValue, null);
                }
            }
        }else{
            for(int i=1; i <= PJT_NMB_COUNT; i++ ){
                 fieldValue = nmbFld+"_"+i;
                 travelInputMap.put(fieldValue, null);
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
                              date = formatter.parse(dateValue);
                        travelInputMap.put(fieldValue, date);
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

            projectId = coiInfoBean.getProjectId();
            travelInputMap.put("coiDisclosureNumber", coiInfoBean.getDisclosureNumber());
            travelInputMap.put("sequenceNumber", coiInfoBean.getSequenceNumber());

            travelInputMap.put("eventTypeCode", ModuleConstants.COI_EVENT_TRAVEL);
            travelInputMap.put("acType", "U");
//            travelInputMap.put("moduleCode", coiInfoBean.getModuleCode());
            travelInputMap.put("moduleItemKey", projectId);
//            travelInputMap.put("coiProjectId",projectId);
            coiInfoBean.setProjectId(projectId);
                            webTxnBean.getResults(request, "updateCoiProjectDetails", travelInputMap);
             }
}

