package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import edu.mit.coeus.questionnaire.bean.QuestionnaireTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.beans.CoiMenuBean;
import edu.mit.coeuslite.coiv2.beans.CoiPersonProjectDetails;
import edu.mit.coeuslite.coiv2.beans.CoiProjectEntityDetailsBean;
import edu.mit.coeuslite.coiv2.beans.Coiv2NotesBean;
import edu.mit.coeuslite.coiv2.services.CoiAttachmentService;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.utils.SessionConstants;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Mr Lijo Joy
 * //second face of other disclosure
 */
public class CoiGetDisclAction extends COIBaseAction {

    private static final String questionType = "F";
    public static final Integer STATUS_IN_PROGRESS = new Integer(100);
    public static final Integer DISPOSITIONCODE = new Integer(3);
    public static final int RIGHTTOVIEW = 1;
    public static final int RIGHTTOEDIT = 2;
    public static final int RIGHTTOAPPROVE = 3;
    public static final int COI_ADMIN_CODE = 2;
    private final String AC_TYPE_INSERT = "I";
    private final String AC_TYPE_REVIEW = "V";
    private final String AC_TYPE_HIST_REV = "H";    
    private static int projectCount=0;
    private static String ANNUAL ="Annual";
    private static String REVISION ="Revision";
    private static String PROPOSAL ="Proposal";
    private static String PROTOCOL ="Protocol";
    private static String IACUCPROTOCOL ="IACUCProtocol"; 
    private static String AWARD ="Award";
    private static String TRAVEL ="Travel";
    public CoiGetDisclAction() {
    }

    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

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
         Vector projectDetails = new Vector();
         Integer modulecode = null;
HttpSession session = request.getSession();
PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
String personId=person.getPersonID();
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

String disclosureNumber=null;
if (session.getAttribute("param1")!=null)
{
 disclosureNumber=(String)session.getAttribute("param1");
}
else
{
CoiDisclosureBean disclosureBean=getApprovedDisclosureBean(personId,request);
disclosureNumber=disclosureBean.getCoiDisclosureNumber();
}
//Integer sequenceNumber=disclosureBean.getSequenceNumber();
 // Edited by Vineetha to display  the discl details that have been selected from the pending disclosure droplist
Integer sequenceNumber=null;
if(request.getParameter("param2")!=null){
sequenceNumber=Integer.parseInt(request.getParameter("param2"));
session.setAttribute("COISeqNumber",sequenceNumber);
session.setAttribute("param2", sequenceNumber);
}else if(session.getAttribute("currentSequence")!=null){
 sequenceNumber=(Integer) session.getAttribute("currentSequence");
 request.getSession().setAttribute("InPrgssSeqNo",sequenceNumber);
}
else if(session.getAttribute("param2")!=null){
sequenceNumber=(Integer) session.getAttribute("param2");
}
// NEW MENU CHANGE 
            if(disclosureNumber == null ||(disclosureNumber!=null && disclosureNumber.trim().length()<=0)){
               disclosureNumber = (String)session.getAttribute("COIDiscNumber");              
            }  
     if(sequenceNumber == null && session.getAttribute("DisclSeqNumber")!=null){
         sequenceNumber = (Integer)session.getAttribute("DisclSeqNumber");
     }  
     // NEW MENU CHANGE 
//coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);// saved menu           
setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
        String actionforward = "failure";
        ActionForward forward = new ActionForward();
        DynaValidatorForm dynaForm = null;
        if (actionForm instanceof DynaValidatorForm) {
            dynaForm = (DynaValidatorForm) actionForm;
        }
        String projectType = request.getParameter("projectType");
        if (projectType == null || projectType.equals("")) {
            projectType = (String) request.getAttribute("projectType");           
        }
        if (projectType != null && (actionMapping.getPath().equals("/getCompleteDisclCoiv2") || actionMapping.getPath().equals("/createDisclosureCoiv2"))) {
            request.getSession().removeAttribute("projectDetailsListInSeesion");
            request.getSession().setAttribute("projectType", projectType);
            // enhancement for new Menu start  
             request.getSession().setAttribute("frmPendingInPrg", "true");  
             request.getSession().setAttribute("dontShowProjects", "false");             
            CoiMenuBean coiMenuBean = (CoiMenuBean)request.getSession().getAttribute("coiMenuDatasaved");   
            if(coiMenuBean == null || (coiMenuBean!=null && coiMenuBean.isQuestDataSaved()==false)){
            request.setAttribute("QnrNotCompleted","true");
            }else{
                request.removeAttribute("QnrNotCompleted");
            }             
             request.getSession().setAttribute("InPrgssDisclNo",disclosureNumber);
             request.getSession().setAttribute("InPrgssSeqNo",sequenceNumber);
           // enhancement for new Menu ends  
             request.getSession().setAttribute("eventDiscl", true);
            if((request.getSession().getAttribute("projectType")).equals("Proposal")){
                modulecode = ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
            // enhancement for new Menu start      
                 request.getSession().setAttribute("InPrgssModulecode",modulecode);                 
                 request.getSession().setAttribute("InPrgssCode", "2");
           // enhancement for new Menu ends       
            String[] checkedPropsalProjects = request.getParameterValues("checkedPropsalProjects");
            for (int i = 0; i < checkedPropsalProjects.length; i++)
            {
                        String propVal = checkedPropsalProjects[i];
                        String[] splitList = propVal.split(";");
                        String propNo = splitList[0];
                        String propName = splitList[1];
                        session.setAttribute("checkedproposal", propNo);
                       // enhancement for new Menu start 
                         request.getSession().setAttribute("InPrgssProjectID",propNo);
                       // enhancement for new Menu ends  
                        session.setAttribute("pjctname", propName);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectSponser(splitList[2]);
                if(splitList.length > 3) {
                    Date date = null;
                     String d=splitList[3];

                    try {
                        date = dateFormat.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }

                    if(splitList.length > 4) {
                         Date date1=null;
                        String d1=splitList[4];
                        try {
                             date1 = dateFormat.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    }
                projectDet.setCoiProjectTitle(splitList[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(splitList[0]);
                projectDetails.add(projectDet);
                 request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
             request.getSession().setAttribute("checkedPropsalProjects", checkedPropsalProjects);}

            else if((request.getSession().getAttribute("projectType")).equals("Award")){
                modulecode = ModuleConstants.AWARD_MODULE_CODE;
           // enhancement for new Menu start      
                 request.getSession().setAttribute("InPrgssModulecode",modulecode); 
                 request.getSession().setAttribute("InPrgssCode", "1");
           // enhancement for new Menu ends 
            String[] checkedAwardBasedProjects = request.getParameterValues("checkedAwardBasedProjects");
            for (int i = 0; i < checkedAwardBasedProjects.length; i++)
            {
               String awardVal = checkedAwardBasedProjects[i];
               String[] awardValValArr = awardVal.split(";");
               String awardNo = awardValValArr[0];
               String awardName = awardValValArr[1];
              String projectSponser=awardValValArr[4];
               session.setAttribute("checkedawardno", awardNo);
               // enhancement for new Menu start 
                request.getSession().setAttribute("InPrgssProjectID",awardNo);
              // enhancement for new Menu ends 
               session.setAttribute("pjctname", awardName);
               session.setAttribute("projectsponser",projectSponser);
                CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(awardValValArr[0]);
                projectDet.setAwardTitle(awardValValArr[1]);
                projectDet.setModuleItemKey(awardValValArr[0]);
                projectDet.setCoiProjectSponser(awardValValArr[4]);
                projectDet.setModuleCode(modulecode);
                if(awardValValArr.length > 2) {
                    Date date = null;
                     String d=awardValValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(awardValValArr.length > 3) {
                         Date date1=null;
                        String d1=awardValValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = formatter.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    }
                    else {
                        projectDet.setCoiProjectEndDate(null);
                    }
                projectDetails.add(projectDet);
                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
            request.getSession().setAttribute("checkedAwardBasedProjects", checkedAwardBasedProjects);}

            else if((request.getSession().getAttribute("projectType")).equals("Protocol")){
                modulecode = ModuleConstants.PROTOCOL_MODULE_CODE;
            // enhancement for new Menu start      
                 request.getSession().setAttribute("InPrgssModulecode",modulecode); 
                 request.getSession().setAttribute("InPrgssCode", "3");
            // enhancement for new Menu ends       
            String[] checkedProtocolBasedProjects = request.getParameterValues("checkedProtocolBasedProjects");
            for (int i = 0; i < checkedProtocolBasedProjects.length; i++)
            {
                String protocolVal = checkedProtocolBasedProjects[i];
                String[] protocolValArr = protocolVal.split(";");
                String protocolNo = protocolValArr[0];
                String protocolName = protocolValArr[1];
                session.setAttribute("checkedprotocolno", protocolNo);
              // enhancement for new Menu start 
                request.getSession().setAttribute("InPrgssProjectID",protocolNo);
              // enhancement for new Menu ends 
                session.setAttribute("pjctname", protocolName);
                CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(protocolValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(protocolValArr[0]);
               if(protocolValArr.length > 2) {
                    Date date = null;
                     String d=protocolValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                         Date date1=null;
                        String d1=protocolValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = formatter.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    } else{
                        projectDet.setCoiProjectEndDate(null);
                    }
                projectDetails.add(projectDet);
                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
           request.getSession().setAttribute("checkedProtocolBasedProjects", checkedProtocolBasedProjects);
            }

            else if((request.getSession().getAttribute("projectType")).equals("IacucProtocol")){
                modulecode = ModuleConstants.IACUC_MODULE_CODE;
           // enhancement for new Menu start      
                 request.getSession().setAttribute("InPrgssModulecode",modulecode); 
                 request.getSession().setAttribute("InPrgssCode", "4");
           // enhancement for new Menu ends 
            String[] checkedIacucProtocolBasedProject = request.getParameterValues("checkedIacucProtocolBasedProject");

             for (int i = 0; i < checkedIacucProtocolBasedProject.length; i++)
            {

                 String protocolVal = checkedIacucProtocolBasedProject[i];
                String[] protocolValArr = protocolVal.split(";");
                 String iacucprotocolNo = protocolValArr[0];
                        String iacucprotocolName = protocolValArr[1];
                        session.setAttribute("checkediacucprotocolno", iacucprotocolNo);
                    // enhancement for new Menu start 
                        request.getSession().setAttribute("InPrgssProjectID",iacucprotocolNo);
                   // enhancement for new Menu ends 
                        session.setAttribute("pjctname", iacucprotocolName);
                        CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
                projectDet = new CoiPersonProjectDetails();
                projectDet.setCoiProjectTitle(protocolValArr[1]);
                projectDet.setModuleCode(modulecode);
                projectDet.setModuleItemKey(protocolValArr[0]);
                if(protocolValArr.length > 2) {
                    Date date = null;
                     String d=protocolValArr[2];

                    try {
                     DateFormat formatter ;
                      formatter = new SimpleDateFormat("yyyy-MM-dd");
                      date = formatter.parse(d);

                       } catch (ParseException e){}
                        projectDet.setCoiProjectStartDate(date);
                    }else {
                        projectDet.setCoiProjectStartDate(null);
                    }

                    if(protocolValArr.length > 3) {
                         Date date1=null;
                        String d1=protocolValArr[3];
                        try {
                          DateFormat formatter ;
                          formatter = new SimpleDateFormat("yyyy-MM-dd");
                          date1 = formatter.parse(d1);
                           } catch (ParseException e)
                          {}
                        projectDet.setCoiProjectEndDate(date1);
                    } else {
                        projectDet.setCoiProjectEndDate(null);
                    }
                projectDetails.add(projectDet);
                request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);//keeping project list in session
            }
            request.getSession().setAttribute("checkedIacucProtocolBasedProject", checkedIacucProtocolBasedProject);}

             else if((request.getSession().getAttribute("projectType")).equals("Travel")){
                modulecode = ModuleConstants.TRAVEL_MODULE_CODE;
                     request.getSession().setAttribute("InPrgssModulecode",modulecode);
                     request.getSession().setAttribute("InPrgssCode", "8");

                     if(request.getParameter("eventName") != null) {
                            String travelEvent = request.getParameter("eventName");
                            String destination = request.getParameter("destinatio");
                            String purpose = request.getParameter("purpose");
                            Double amount = Double.parseDouble(request.getParameter("amount"));
                            String sponsor = request.getParameter("sponsor");
                            String startDate = request.getParameter("startDate");
                            String endDate = request.getParameter("endDate");
                            CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();
                            projectDet.setEventName(travelEvent);
                            projectDet.setDestination(destination);
                            projectDet.setPurpose(purpose);
                            projectDet.setCoiProjectSponser(sponsor);
                            projectDet.setCoiProjectFundingAmount(amount);
                            Date date = null;
                            Date eDate = null;
                            try {
                             DateFormat formatter ;
                              formatter = new SimpleDateFormat("MM/dd/yyyy");
                              date = formatter.parse(startDate);

                               } catch (ParseException e){}
                                projectDet.setCoiProjectStartDate(date);

                                try {
                                 DateFormat formatter ;
                                  formatter = new SimpleDateFormat("MM/dd/yyyy");
                                  eDate = formatter.parse(endDate);

                                } catch (ParseException e){}
                            projectDet.setCoiProjectEndDate(eDate);
                            projectDetails.add(projectDet);
                            request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);
                            request.getSession().setAttribute("travelPjtDetails", projectDet);
                     }
                     else {
                         CoiPersonProjectDetails projectDet =  (CoiPersonProjectDetails)request.getSession().getAttribute("travelPjtDetails");
                         projectDetails.add(projectDet);
                         request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);
                         request.getSession().setAttribute("travelPjtDetails", projectDet);
                         request.setAttribute("operation", "MODIFY");
                     }
            }
      }
       if(actionMapping.getPath().equals("/createDisclosureFromInProgress")){
            int code=0;  
        request.getSession().removeAttribute("extcodeModuleKey"); // fix for questionnaire goes to  project detais  for Annual    
            String InPrgssprojectId=null;
            String InPrgssDisclNo=null;
            Integer InPrgssSeqNo=null;  
    request.getSession().removeAttribute("savedQnrList");    
    request.getSession().removeAttribute("getinonce");
    request.getSession().removeAttribute("qnranswerd"); 
    request.getSession().removeAttribute("AlreadySavedProjectsForDiscl");     
    request.setAttribute("DiscViewQtnr", true);//to check Questionnaire menu selected
    // save in In BYproject
            projectCount = 0;        
            getAllProposals(request);// list only Disclosed submitted Dev Proposal  
            getAllInstProposals(request);
            getAllProtocolList(request);
            getAllIacucProtocolList(request);
            getAllAward(request);
        request.getSession().setAttribute("certValidPjtCount",projectCount);
   
        // validate the projects certifying the disclosure  
    if(disclosureNumber==null && request.getSession().getAttribute("annualDisclosureNumber")!=null){
       disclosureNumber=(String)request.getSession().getAttribute("annualDisclosureNumber");
       request.getSession().setAttribute("InPrgssDisclNo",disclosureNumber);
       request.getSession().setAttribute("InPrgssSeqNo",sequenceNumber);
       session.setAttribute("param1",disclosureNumber);       
    }
    if (disclosureNumber == null && (session.getAttribute("DisclNumber")!=null && session.getAttribute("DisclNumber").toString().trim().length()>0))
    {      disclosureNumber = (String)session.getAttribute("DisclNumber");
           sequenceNumber  = Integer.parseInt(session.getAttribute("DisclSeqNumber").toString());
            request.getSession().setAttribute("InPrgssDisclNo",disclosureNumber);
            request.getSession().setAttribute("InPrgssSeqNo",sequenceNumber);
    }
    if (disclosureNumber==null){
        disclosureNumber = (String)session.getAttribute("InPrgssDisclNo");
        sequenceNumber  = Integer.parseInt(session.getAttribute("InPrgssSeqNo").toString());
    }
    
            request.getSession().setAttribute("DisclosureNumberInUpdateSession", disclosureNumber);
            request.getSession().setAttribute("SequenceNumberInUpdateSession", sequenceNumber);
            request.getSession().setAttribute("currentSequence",sequenceNumber);
            //request.getSession().removeAttribute("projectDetailsListInSeesion");                 
            request.getSession().setAttribute("eventDiscl", true);
            session.setAttribute("isEventForInProgress",true);
            String frmPendingInPrg=(String)request.getParameter("frmPendingInPrg");
          if(frmPendingInPrg!=null && frmPendingInPrg.trim().length()>0)
          {
              request.getSession().setAttribute("frmPendingInPrg", "true");
          }          
          CoiPersonProjectDetails projectDet = new CoiPersonProjectDetails();  
     // getting data from query string if fails gets from session       
            if(request.getParameter("projectID")!=null){
                 InPrgssprojectId=request.getParameter("projectID");
                request.getSession().setAttribute("InPrgssProjectID",InPrgssprojectId);
            }else{
                InPrgssprojectId=(String)request.getSession().getAttribute("InPrgssProjectID");// while commimg from pending>>In progress
            }
            
            if(request.getParameter("param1")!=null){
                 InPrgssDisclNo=request.getParameter("param1");
                request.getSession().setAttribute("InPrgssDisclNo",InPrgssDisclNo);
            }else{
                InPrgssDisclNo=(String)request.getSession().getAttribute("InPrgssDisclNo");// while commimg from pending>>In progress
            }
            
             if(request.getParameter("param2")!=null){
                 InPrgssSeqNo=Integer.parseInt(request.getParameter("param2"));
                request.getSession().setAttribute("InPrgssSeqNo",InPrgssSeqNo);
                // Used in continue without Questionnaire  
                request.getSession().setAttribute("SeqForWithQNR",InPrgssSeqNo);
                // Used in continue without Questionnaire
            }else{
                InPrgssSeqNo=(Integer)request.getSession().getAttribute("InPrgssSeqNo");// while commimg from pending>>In progress
            }            
          coiMenuDataSaved(disclosureNumber,sequenceNumber,personId,request);// saved menu
          if(request.getParameter("code")!=null){
          code=Integer.parseInt(request.getParameter("code"));
          request.getSession().setAttribute("InPrgssCode", code);
          request.getSession().setAttribute("ModuleCodeInUpdateSession", code);
          }else{
              code=Integer.parseInt(request.getSession().getAttribute("InPrgssCode").toString());
          }  
          if(code==1)
          {   projectType="Award";
              session.setAttribute("checkedawardno", InPrgssprojectId);
              request.getSession().setAttribute("projectType", projectType); 
              modulecode = ModuleConstants.AWARD_MODULE_CODE;
          }
          else if(code==2)
          {   projectType="Proposal";   
              session.setAttribute("checkedproposal", InPrgssprojectId);
              request.getSession().setAttribute("projectType", projectType); 
              modulecode = ModuleConstants.PROPOSAL_DEV_MODULE_CODE;
          }
          else if(code==3)
          {   projectType="Protocol"; 
              session.setAttribute("checkedprotocolno", InPrgssprojectId);
              request.getSession().setAttribute("projectType", projectType); 
              modulecode = ModuleConstants.PROTOCOL_MODULE_CODE;
          }
          else if(code==4)
          {   projectType="IacucProtocol";  
              session.setAttribute("checkediacucprotocolno", InPrgssprojectId);
              request.getSession().setAttribute("projectType", projectType); 
              modulecode = ModuleConstants.IACUC_MODULE_CODE;
          }
          else if(code==5)
          {   projectType="Annual";  
              session.setAttribute("Annual",true);
              //--   
      
      //  request.getSession().removeAttribute("fromReviewlist");
       // request.getSession().removeAttribute("frmShowAllReviews");
      //  request.getSession().removeAttribute("fromAttachment");
      //  request.getSession().removeAttribute("DisclosureNumberInUpdateSession");
           
        request.getSession().removeAttribute("certified");
             //--
              request.getSession().setAttribute("projectType", projectType);
              request.getSession().setAttribute("dontShowProjects", "false");              
          }
           else if(code==6)
          {   projectType="Revision";
              request.getSession().removeAttribute("CREATEFLAG");
              request.getSession().setAttribute("projectType", projectType); 
              request.getSession().setAttribute("dontShowProjects", "false");              
          }
           else if(code==8)
          {   projectType="Travel";
              //session.setAttribute("checkedtravelno", InPrgssprojectId);
              session.setAttribute("InPrgssProjectID", InPrgssprojectId);
             request.getSession().setAttribute("projectType", projectType);
              request.setAttribute("projectType",projectType);
              modulecode = ModuleConstants.TRAVEL_MODULE_CODE;
          }
          else
           {
              projectType="Other";   
              request.getSession().setAttribute("projectType", projectType);
              request.getSession().setAttribute("dontShowProjects", "false");              
           }         
      // NEW MENU CHANGE     
          if(InPrgssSeqNo ==null && sequenceNumber != null){
              InPrgssSeqNo=sequenceNumber;
          }
          if(InPrgssSeqNo ==null && session.getAttribute("currentSequence")!=null){
             InPrgssSeqNo =  Integer.parseInt(session.getAttribute("currentSequence").toString());
          }else if(InPrgssSeqNo ==null && session.getAttribute("COISeqNumber")!=null){
               InPrgssSeqNo =  Integer.parseInt(session.getAttribute("COISeqNumber").toString());
          }
          if(InPrgssDisclNo == null){
              InPrgssDisclNo = (String)session.getAttribute("COIDiscNumber");
          }
          // NEW MENU CHANGE 
         request.getSession().setAttribute("InPrgssModulecode",modulecode);    // fetching project details from procedure     
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", InPrgssDisclNo);
        hmData.put("sequenceNumber", InPrgssSeqNo);
        hmData.put("moduleCode", modulecode);
        hmData.put("moduleItemKey", InPrgssprojectId);        
        WebTxnBean webTxn = new WebTxnBean();
        Vector apprvdDiscl = null;
        CoiProjectEntityDetailsBean coiProjectEntityDetailsBean = new CoiProjectEntityDetailsBean();
        Hashtable apprvdDisclDet = (Hashtable) webTxn.getResults(request, "getCoiProjectDetails", hmData);
        apprvdDiscl = (Vector) apprvdDisclDet.get("getCoiProjectDetails");
        if (apprvdDiscl != null && apprvdDiscl.size() > 0) {
            coiProjectEntityDetailsBean = (CoiProjectEntityDetailsBean) apprvdDiscl.get(0);
            projectDet.setModuleCode(modulecode);
            projectDet.setCoiProjectTitle(coiProjectEntityDetailsBean.getCoiProjectTitle());
            projectDet.setAwardTitle(coiProjectEntityDetailsBean.getCoiProjectTitle());
            projectDet.setCoiProjectSponser(coiProjectEntityDetailsBean.getCoiProjectSponsor());
            projectDet.setCoiProjectStartDate(coiProjectEntityDetailsBean.getCoiProjectStartDate());
            projectDet.setCoiProjectEndDate(coiProjectEntityDetailsBean.getCoiProjectEndDate());
            projectDet.setEventName(coiProjectEntityDetailsBean.getCoiProjectTitle());
            projectDet.setDestination(coiProjectEntityDetailsBean.getDestination());
            projectDet.setPurpose(coiProjectEntityDetailsBean.getPurpose());
            projectDet.setCoiProjectFundingAmount(coiProjectEntityDetailsBean.getCoiProjectFundingAmount());
            projectDet.setModuleItemKey(InPrgssprojectId);
            projectDetails.add(projectDet);
            session.setAttribute("pjctname", coiProjectEntityDetailsBean.getCoiProjectTitle());
        }
             
 // fetching project details from procedure            
        
        request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);  
        if(code==8) {
           request.getSession().setAttribute("travelPjtDetails", projectDet);
        }
        if(code ==5 || code == 6){ //case of annual(5),event(6),other
        session.setAttribute("isEventForInProgress",false);
        return actionMapping.findForward("successToAnnual");  
        }
         
        }

        HashMap hmFinData = new HashMap();
        hmFinData.put("questionType", questionType);
        forward = actionMapping.findForward(actionforward);

        //Function call to get Disclosure
        if (actionMapping.getPath().equals("/getCompleteDisclCoiv2")) {
            forward = getQuestionnaire(hmFinData, dynaForm, request, actionMapping);
        }
        //Function call to create Disclosure
        if (actionMapping.getPath().equals("/createDisclosureCoiv2")||actionMapping.getPath().equals("/createDisclosureFromInProgress")) {
        if(session.getAttribute("getinonce") == null){
            forward = createDisclosure(hmFinData, actionForm, request, actionMapping);
        }else{
            forward = actionMapping.findForward("update");
        }
    }
        return forward;
    }

    /**
     *
     * Function to create coi disclosure
     * @param hmFinData
     * @param actionform
     * @param request
     * @param actionMapping
     * @return
     * @throws Exception
     */
    private ActionForward createDisclosure(HashMap hmFinData, ActionForm actionform, HttpServletRequest request, ActionMapping actionMapping) throws Exception {

        HttpSession session = request.getSession();
        PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String personId=person.getPersonID();
       String disclosureNumber=null;
        if (session.getAttribute("param1")!=null)
        {
         disclosureNumber=(String)session.getAttribute("param1");
        }
        else
        {
        CoiDisclosureBean disclosureBean=getApprovedDisclosureBean(personId,request);
        disclosureNumber=disclosureBean.getCoiDisclosureNumber();
        }
//        Integer sequenceNumber=disclosureBean.getSequenceNumber();
//        setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
        Integer sequenceNumber=null;
if(request.getParameter("param2")!=null){
sequenceNumber=Integer.parseInt(request.getParameter("param2"));
session.setAttribute("param2", sequenceNumber);
}else if(session.getAttribute("param2")!=null){
sequenceNumber=(Integer) session.getAttribute("param2");
}
if(sequenceNumber == null && session.getAttribute("InPrgssSeqNo")!=null){
    sequenceNumber = Integer.parseInt(session.getAttribute("InPrgssSeqNo").toString());
}
setApprovedDisclosureDetails(disclosureNumber,sequenceNumber,personId,request);
 String disclNumber=null;
 Integer seqNumber=null;
        String operation = request.getParameter("operation");
        String operationType = request.getParameter("operationType");
        if (operationType == null || operationType.equals("")) {
            operationType = (String) request.getAttribute("operationType");
            request.setAttribute("operationType", operationType);
            //request.removeAttribute("operationType");
        }

        //  request.setParameter(operationType);
        //HttpSession session = request.getSession();
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
        //right check ends***********
        request.setAttribute("operation", operation);
        String isValid = getCoiQuestionnaire(hmFinData, request);
        if (isValid != null && isValid.equals("inValid")) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noQuestionnaire",
                    new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
            saveMessages(request, actionMessages);
            return actionMapping.findForward("exception");
        }
        CoiDisclosureBean discl = getCurrentDisclPerson(request);
        if (operation != null && operation.equals("UPDATE")) {
            request.setAttribute("operation", "MODIFY");

            if (discl.getCoiDisclosureNumber() != null) {
                session.setAttribute("DisclNumber", discl.getCoiDisclosureNumber());
                session.setAttribute("DisclSeqNumber", discl.getSequenceNumber());
                session.setAttribute("DisclStatusCode", discl.getDisclosureStatusCode());
                request.setAttribute("mode", "edit");
                return actionMapping.findForward("update");
            } else {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("nodisclosure",
                        new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");

            }
        }
        if (operation != null && operation.equals("MODIFY")) {
            request.setAttribute("operation", "MODIFY");
            session.setAttribute("acType", "EDIT");
            return actionMapping.findForward("success");
        }
        WebTxnBean webTxnBean = new WebTxnBean();
        CoiDisclosureBean perDiscl = new CoiDisclosureBean();
        UtilFactory.log("-------------Disclosure Delete Start-----------------------");
        if (operation != null && operationType != null && operation.equals("SAVE") && operationType.equals("MODIFY")) {
            CoiAttachmentService coiAttachmentService = CoiAttachmentService.getInstance();
             disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
             seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
             if(disclNumber == null) {
                 disclNumber = disclosureNumber;
             }
             if(seqNumber == null) {
                 seqNumber = sequenceNumber;
             }
            session.setAttribute("DisclNumber", disclNumber);
            session.setAttribute("DisclSeqNumber", seqNumber);
             request.getSession().setAttribute("currentSequence",seqNumber);
            perDiscl.setAcType("U");
               setApprovedDisclosureDetails(disclNumber,seqNumber,personId,request);
                    ///     ------For exit purpose New
                    UtilFactory.log("-----------Status code:     new");
                    Integer exitCode=2;
                     String docDescription="";
                    request.getSession().setAttribute("docAttachDescription",docDescription);
                    session.setAttribute("exitCode", exitCode);
                    Vector docList = new Vector();
                    // NEW MENU CHANGE 
                    if(disclosureNumber == null){
                       disclosureNumber = disclNumber;
                       sequenceNumber   = seqNumber ;      
                    }
                    // NEW MENU CHANGE 
                    docList = coiAttachmentService.getUploadDocumentForDisclosure(disclosureNumber, sequenceNumber);
                     session.setAttribute("exitCodeattachment", docList);
                      Coiv2NotesBean coiv2NotesBean = new Coiv2NotesBean();
                      coiv2NotesBean.setCoiDisclosureNumber(disclNumber);
                      coiv2NotesBean.setCoiSequenceNumber(seqNumber);
                      Hashtable discNotesList = (Hashtable) webTxnBean.getResults(request, "getDisclosureNotesCoiv2", coiv2NotesBean);
                      Vector notes = (Vector) discNotesList.get("getDisclosureNotesCoiv2");
                      session.setAttribute("exitCodeNotes", notes);
                      //UtilFactory.log("=====Notes added======>");
                    ///     -----Code For exit purpose  End


        } else {
            setCurrentDisclosure(hmFinData, request);
            perDiscl.setAcType("I");
            String discNo=(String)session.getAttribute("DisclNumber");
           // Integer seqNumb = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
            //request.getSession().setAttribute("DisclStatusCode", STATUS_IN_PROGRESS);
             //setApprovedDisclosureDetails(discNo,seqNumb,personId,request);
        }
        String forward = "success";

        String projectID = "";
        disclosureNumber = (String) session.getAttribute("DisclNumber");
        perDiscl.setCoiDisclosureNumber(disclosureNumber);
        perDiscl.setExpirationDate(discl.getExpirationDate());
        Integer seq = (Integer) session.getAttribute("DisclSeqNumber");
        if (operationType != null && !operationType.equals("MODIFY") && session.getAttribute("acType") != null && ((String) session.getAttribute("acType")).equals("EDIT")) {
            if (session.getAttribute("DisclStatusCode") != null) {
                Integer status = (Integer) session.getAttribute("DisclStatusCode");
                request.setAttribute("mode", "saveUpdate");
                forward = "update";              
                    seq = new Integer(getNextSeqNumDisclosure(request, disclosureNumber));
                    seq = new Integer(seq.intValue() + 1);
                    request.getSession().setAttribute("currentSequence", seq); 
                    // NEW MENU CHANGE 
                    request.getSession().setAttribute("COISeqNumber",seq);

                   Hashtable projectDetailsList = (Hashtable) webTxnBean.getResults(request, "fngetNextCoiPjtId", null);
                    HashMap hmfinEntityList = (HashMap) projectDetailsList.get("fngetNextCoiPjtId");
                    if(hmfinEntityList !=null && hmfinEntityList.size()>0){
                        projectID = hmfinEntityList.get("ls_seq").toString();
                    }
                    // NEW MENU CHANGE 
            } else {               
                return actionMapping.findForward(forward);
            }
             ///     ------For exit purpose New
             UtilFactory.log("-----------Status code:     new");
                Integer exitCode=1;
                session.setAttribute("exitCode", exitCode);
                String discNo=(String)session.getAttribute("DisclNumber");
            Integer seqNumb = (Integer) request.getSession().getAttribute("currentSequence");
                setApprovedDisclosureDetails(discNo,seqNumb,personId,request);
            ///     -----Code For exit purpose  End
        }else {
             projectID = session.getAttribute("InPrgssProjectID").toString();
        }
        perDiscl.setSequenceNumber(seq);
        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }

        perDiscl.setPersonId(person.getPersonID());
        perDiscl.setDisclosureStatusCode(STATUS_IN_PROGRESS);

        perDiscl.setUpdateUser(loggedId);
        String projectType = (String) request.getSession().getAttribute("projectType");
        HashMap eventTypeMap = (HashMap)session.getAttribute("EventTypeCodeMap");

        String pjtType = "";

        if (projectType != null && !projectType.equals("")) {
            if (projectType.equals("Proposal")) {
                pjtType = ModuleCodeType.proposal.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_PROPOSAL);
                String proposalNo=(String)session.getAttribute("checkedproposal");
                perDiscl.setModuleItemKey(proposalNo);

            }
            if (projectType.equals("Protocol")) {
               // perDiscl.setModuleCode(ModuleCodeType.protocol.getValue());
                pjtType = ModuleCodeType.protocol.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_PROTOCOL);
                String protocolNo=(String)session.getAttribute("checkedprotocolno");
                perDiscl.setModuleItemKey(protocolNo);
            }
            if (projectType.equals("IacucProtocol")) {
               // perDiscl.setModuleCode(ModuleCodeType.protocol.getValue());
                pjtType = ModuleCodeType.iacucprotocol.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_IACUC);
                String iacucprotocolNo=(String)session.getAttribute("checkediacucprotocolno");
                perDiscl.setModuleItemKey(iacucprotocolNo);
            }
            if (projectType.equals("Award")) {
                //perDiscl.setModuleCode(ModuleCodeType.award.getValue());
                pjtType = ModuleCodeType.award.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_AWARD);
               String awardNo=(String)session.getAttribute("checkedawardno");
                perDiscl.setModuleItemKey(awardNo);

            }
              if (projectType.equals("Travel")) {              
                pjtType = ModuleCodeType.travel.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_TRAVEL);
                perDiscl.setModuleItemKey(projectID);
                session.setAttribute("InPrgssProjectID", projectID);

                if(request.getSession().getAttribute("travelPjtDetails") != null) {
                    CoiPersonProjectDetails pjtDet = (CoiPersonProjectDetails)request.getSession().getAttribute("travelPjtDetails");
                    pjtDet.setCoiProjectId(projectID);
                    pjtDet.setModuleItemKey(projectID);
                    pjtDet.setModuleCode(ModuleConstants.TRAVEL_MODULE_CODE);
                    request.getSession().setAttribute("travelPjtDetails", pjtDet);
                    Vector projectDetails = new Vector();
                    projectDetails.add(pjtDet);
                    request.getSession().setAttribute("projectDetailsListInSeesion", projectDetails);
                }

            }
            if (projectType.equals("Other")) {
               // perDiscl.setModuleCode(ModuleCodeType.other.getValue());
                pjtType = ModuleCodeType.other.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_OTHER);
            }
            if (projectType.equals("New")) {
               // perDiscl.setModuleCode(ModuleCodeType.annual.getValue());
                pjtType = ModuleCodeType.annual.getValue();
                perDiscl.setModuleCode(ModuleConstants.COI_EVENT_ANNUAL);
            }

        }
        Integer reviewStatus=1;
        perDiscl.setReviewStatusCode(reviewStatus);
        perDiscl.setDisclosureDispositionCode(DISPOSITIONCODE);
        webTxnBean.getResults(request, "coiPersonDisclosureCoiv2", perDiscl);
        session.setAttribute("isReviewDue",false);
        CoiDisclosureBean currDisclosure = new CoiDisclosureBean();
        if (!perDiscl.getAcType().equals("U")) {
            currDisclosure = this.getCurrentDisclPerson(request);
            session.setAttribute("CurrDisclPer", currDisclosure);
            session.setAttribute("DisclNumber", currDisclosure.getCoiDisclosureNumber());
            session.setAttribute("DisclSeqNumber", currDisclosure.getSequenceNumber());
        } else {
             disclNumber = (String) request.getSession().getAttribute("DisclosureNumberInUpdateSession");
             seqNumber = (Integer) request.getSession().getAttribute("SequenceNumberInUpdateSession");
            currDisclosure = new CoiDisclosureBean();
            currDisclosure.setCoiDisclosureNumber(disclNumber);
            currDisclosure.setSequenceNumber(seqNumber);
            session.setAttribute("DisclNumber", disclNumber);
            session.setAttribute("DisclSeqNumber", seqNumber);
        }
//for mail
        return actionMapping.findForward(forward);
    }

    private String getCoiQuestionnaire(HashMap hmFinData, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        QuestionnaireTxnBean questionnaireTxnBean = new QuestionnaireTxnBean(personInfoBean.getUserId());
        session.setAttribute("actionFrom", "ANN_DISCL");
        request.setAttribute("actionFrom", "ANN_DISCL");
        Map hmQuestData = new HashMap();
         String projType = (String) request.getSession().getAttribute("projectType");
        Integer seqNum = 0;

         hmQuestData.put("module_sub_item_key", seqNum);

            if (projType != null && !projType.equals("")) {
             if (projType.equals(PROPOSAL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_PROPOSAL);
               String proposalNo=(String)session.getAttribute("checkedproposal");
               hmQuestData.put("module_item_key", proposalNo);
         }
             if (projType.equals(PROTOCOL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_PROTOCOL);
               String protocolNo=(String)session.getAttribute("checkedprotocolno");
               hmQuestData.put("module_item_key", protocolNo);
         }
              if (projType.equals(IACUCPROTOCOL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_IACUC);
               String iacucprotocolNo=(String)session.getAttribute("checkediacucprotocolno");
               hmQuestData.put("module_item_key", iacucprotocolNo);
         }
             if (projType.equals(ANNUAL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_ANNUAL);
         }
             if (projType.equals(AWARD)) {
             hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_AWARD);
               String awardNo=(String)session.getAttribute("checkedawardno");
               hmQuestData.put("module_item_key", awardNo);
             }
             if (projType.equals(REVISION)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_REVISION);              
             }
               if (projType.equals(TRAVEL)) {
               hmQuestData.put("as_module_item_code",ModuleConstants.ANNUAL_COI_DISCLOSURE);
               hmQuestData.put("as_module_sub_item_code",ModuleConstants.COI_EVENT_TRAVEL);
               String travelNo=(String)session.getAttribute("checkedtravelno");
               hmQuestData.put("module_item_key", travelNo);
             }                             
         }

         QuestionnaireAnswerHeaderBean questionnaireModuleObject = new QuestionnaireAnswerHeaderBean();
         questionnaireModuleObject.setModuleItemCode(Integer.parseInt(hmQuestData.get("as_module_item_code").toString()));
         questionnaireModuleObject.setModuleSubItemCode(Integer.parseInt(hmQuestData.get("as_module_sub_item_code").toString()));
         if(hmQuestData.get("module_item_key") != null) {
            questionnaireModuleObject.setModuleItemKey(hmQuestData.get("module_item_key").toString());
         }
         questionnaireModuleObject.setModuleSubItemKey(hmQuestData.get("module_sub_item_key").toString());
         questionnaireModuleObject.setCurrentPersonId(personInfoBean.getPersonID());
         
         CoeusVector qstnVector =  questionnaireTxnBean.fetchApplicableQuestionnairedForModule(questionnaireModuleObject);
         Vector qstnnrIdList = new Vector();
         if(qstnVector != null && qstnVector.size() > 0) {
             for(int i=0; i < qstnVector.size(); i++){
                 QuestionnaireAnswerHeaderBean qnrAnsHeadBean = (QuestionnaireAnswerHeaderBean)qstnVector.get(i);
                 int qstnnrId = qnrAnsHeadBean.getQuestionnaireId();
                 qstnnrIdList.add(qstnnrId);
             }
         }

         session.setAttribute("qstnnrIdList", qstnnrIdList);

        request.setAttribute("MenuId", "ANN_DISCL");
        request.setAttribute("questionaireLabel", "Annual Disclosure Certification");
        return "valid";
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

    private ActionForward getQuestionnaire(HashMap hmFinData, DynaValidatorForm dynaValidatorForm, HttpServletRequest request, ActionMapping actionMapping) throws Exception {
        // session.setAttribute("questionData",questionData);
        String acType = "";
        HttpSession session = request.getSession();
        if (request.getParameter("acType") != null) {
            acType = request.getParameter("acType").trim();
        } else {
            if (session.getAttribute("acType") != null) {
                session.removeAttribute("acType");
            }

            acType = AC_TYPE_INSERT;
            request.setAttribute("acType", AC_TYPE_INSERT);
        }

        if (session.getAttribute("person") == null) {
            checkCOIPrivileges(request);
        }

        PersonInfoBean personinfo = (PersonInfoBean) session.getAttribute("person");
        String personId = personinfo.getPersonID();
        dynaValidatorForm.set("personId", personId);
        String disclosureNum = "";
        Integer seqNum = new Integer(1);
        if (!acType.equals(EMPTY_STRING) || acType != null) {            /*
             * Check validity of token that was set in request when the form was displayed to
             * the user, to avoid duplicate submission.
             */
            if (acType.equals(AC_TYPE_INSERT)) {
                disclosureNum = getNextDisclNum(request);
                dynaValidatorForm.set("coiDisclosureNumber", disclosureNum);
                if (!isTokenValid(request)) {
                    boolean disclosureExists =
                            checkDisclosureExists(dynaValidatorForm, request);
                    if (disclosureExists) {
                        ActionMessages actionMessages = new ActionMessages();
                        actionMessages.add("duplicateSubmission",
                                new ActionMessage("error.coiMain.disclosureExists"));
                        saveMessages(request, actionMessages);
                        return actionMapping.findForward("exception");
                        // throw new Exception("Duplicate Submission");
                    }
                }
            }
            if (acType.equals(AC_TYPE_REVIEW)) {
                if (session.getAttribute("CurrDisclPer") != null) {
                    CoiDisclosureBean discl = (CoiDisclosureBean) session.getAttribute("CurrDisclPer");
                    disclosureNum = discl.getCoiDisclosureNumber();
                    seqNum = discl.getSequenceNumber();
                } else {
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("nodisclosure",
                            new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                    saveMessages(request, actionMessages);
                    return actionMapping.findForward("exception");
                    // throw new Exception("Duplicate Submission");
                }
                request.setAttribute("acType", "review");
                request.setAttribute("reviewType", "current");
            }
            if (acType.equals(AC_TYPE_HIST_REV)) {
                if (request.getParameter("disclNumber") != null) {
                    disclosureNum = request.getParameter("disclNumber").trim();
                    if (request.getParameter("seqNumber") != null) {
                        seqNum = new Integer(request.getParameter("seqNumber").trim());
                    }
                } else {
                    ActionMessages actionMessages = new ActionMessages();
                    actionMessages.add("nodisclosure",
                            new ActionMessage("error.reviewCOIDisclosure.nodisclosure"));
                    saveMessages(request, actionMessages);
                    return actionMapping.findForward("exception");
                    // throw new Exception("Duplicate Submission");
                }
                request.setAttribute("acType", "review");
                request.setAttribute("reviewType", "history");
            }
            session.setAttribute("DisclNumber", disclosureNum);
            session.setAttribute("DisclSeqNumber", seqNum);

            //Modified for Case#4447 : Next phase of COI enhancements -Start
            String isValid = getCoiQuestionnaire(hmFinData, request);
            if (isValid != null && isValid.equals("inValid")) {
                ActionMessages actionMessages = new ActionMessages();
                actionMessages.add("noQuestionnaire",
                        new ActionMessage("error.reviewCOIDisclosure.noQuestionnaire"));
                saveMessages(request, actionMessages);
                return actionMapping.findForward("exception");
            }

            saveToken(request);
        }
        int role = 0;
        boolean setError = false;
        role = this.roleCheck(dynaValidatorForm, request);
        if (acType.equals(AC_TYPE_REVIEW)) {
            if (role < RIGHTTOVIEW) {
                setError = true;
            }
        } else if (acType.equals(AC_TYPE_INSERT)) {
            if (role < RIGHTTOEDIT) {
                setError = true;
            }
        }
        if (setError) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("noright",
                    new ActionMessage("error.reviewCOIDisclosure.noRighttoView"));
            saveMessages(request, actionMessages);
            return actionMapping.findForward("exception");
        }
        return actionMapping.findForward("questionnaire");
    }

    private int roleCheck(DynaValidatorForm dynaForm, HttpServletRequest request) throws Exception {
        int right = 0;
        boolean hasRightToView = false;
        boolean hasRightToEdit = false;
        boolean canApproveDisclosure = false;
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
    private boolean checkDisclosureExists(DynaValidatorForm dynaValidatorForm, HttpServletRequest request) throws Exception {
        boolean disclosureExists = false;
        String personId = (String) dynaValidatorForm.get("personId");
        WebTxnBean webTxn = new WebTxnBean();
        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        Hashtable discl = (Hashtable) webTxn.getResults(request, "getAnnDisclPersonCoiv2", hmData);
        Vector finDiscl = (Vector) discl.get("getDisclosureDetails");
        if (finDiscl != null && finDiscl.size() > 0) {
            disclosureExists = true;
        }
        return disclosureExists;
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
            request.setAttribute("apprvdDisclosureBean", apprvdDisclosureBean);
            request.getSession().setAttribute("disclosureBeanSession", apprvdDisclosureBean);
        }
        return apprvdDisclosureBean;
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
 /* **
        Vector statusDispDet = new Vector();
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
             //added by Vineetha
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
}