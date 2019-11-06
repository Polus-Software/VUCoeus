/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.coiv2.beans.CoiDisclosureBean;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.coiv2.services.GetDisclosureForPrint;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.File;
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
/**
 *
 * @author Mr
 */
public class ApprovedDisclosurePrintCoiv2Action extends edu.dartmouth.coeuslite.coi.action.COIBaseAction{


    public static final String READER_CLASS = "edu.mit.coeus.xml.generator.ReportReader";

    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

            ActionForward actionForward=null;

             //getting approved disclosure
              CoiCommonService CommonServiceservice = CoiCommonService.getInstance();
              PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
              String loggedInPerson=personInfoBean.getPersonID();
              String fullName=personInfoBean.getFullName();
              HttpSession session=request.getSession();
              String disclosureType=null;
              if(request.getParameter("selected")!=null){
                 disclosureType=request.getParameter("selected"); 
              }
              CoiDisclosureBean disclosureBean=null;
              PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
              String disclosureNumber=null;
              Integer sequenceNumber=0;
              String disclosurePersonId=person.getPersonID();
              if(disclosureType!=null && disclosureType.equals("Approved")){
                 
                    if(request.getParameter("param1")!=null && request.getParameter("param2")!=null && request.getParameter("param7")!=null && !request.getParameter("param7").equals("")){
                        
                     disclosureNumber=(String) request.getParameter("param1");
                     sequenceNumber=Integer.parseInt(request.getParameter("param2"));
                     disclosurePersonId=(String) request.getParameter("param7");
                  }         
                 if(disclosureNumber!=null && sequenceNumber!=0 && disclosurePersonId!=null){
                 disclosureBean =getDisclosureDetails(disclosureNumber,sequenceNumber,disclosurePersonId,request);
                 }else{
                  disclosureBean=CommonServiceservice.getApprovedDisclosureBean(loggedInPerson, request);
                 }
              }
              
              
              
              if(disclosureType!=null && disclosureType.equals("current")){
                  if(request.getParameter("param1")!=null && request.getParameter("param2")!=null && request.getParameter("param7")!=null){
                     disclosureNumber=(String) request.getParameter("param1");
                     sequenceNumber=Integer.parseInt(request.getParameter("param2"));
                     disclosurePersonId=(String) request.getParameter("param7");
                  }                  
                  else {
                      if(session.getAttribute("DisclNumber")!=null && !session.getAttribute("DisclNumber").equals("")){
                          disclosureNumber=(String) session.getAttribute("DisclNumber");
                          }
                          if(session.getAttribute("currentSequence")!=null){
                          sequenceNumber=(Integer) session.getAttribute("currentSequence");
                          }
                           if(session.getAttribute("personId")!=null){
                          disclosurePersonId= (String)session.getAttribute("personId");
                          }
                }
                  if(disclosureNumber!=null && sequenceNumber!=0 && disclosurePersonId!=null){
                   disclosureBean =getDisclosureDetails(disclosureNumber,sequenceNumber,disclosurePersonId,request);
                  }
              //disclosureBean=    
              }
              
              String reportID = "Coiv2print/approveddisclosure";
              String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
              String reportPath = session.getServletContext().getRealPath("/") + File.separator + reportDir;
              request.setAttribute("DiscViewPrint", true);//to check Print menu selected

if(request.getParameter("param7")!=null && !request.getParameter("param7").equals("")){
    String persId = (String)request.getParameter("param7");    
    if(isRightAtPersonHomeUnit(persId,disclosurePersonId,request)==0){
     request.setAttribute("noRightAtPerHomeUnit", "true");
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllReview")){
          return actionMapping.findForward("showAllReview");
     }
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllAnnualReview")){
          return actionMapping.findForward("showAllAnnualReview");
     }
     if(request.getParameter("fromReview")!=null && request.getParameter("fromReview").equalsIgnoreCase("showAllEventReview")){
          return actionMapping.findForward("showAllEventReview");
     }
 }
}


             //new for COI
//              Vector selectedDisclosure=null;
//              if(session.getAttribute("ApprovedDisclDetView")!=null){
//                  selectedDisclosure=(Vector) session.getAttribute("ApprovedDisclDetView");
//              }else{
//                  if(session.getAttribute("ApprovedDisclDet")!=null){
//                    selectedDisclosure=(Vector) session.getAttribute("ApprovedDisclDet");  
//                  }
//               if(request.getAttribute("ApprovedDisclDetView")!=null){
//                  selectedDisclosure=(Vector) session.getAttribute("ApprovedDisclDetView"); 
//                  request.setAttribute( "ApprovedDisclDetView",selectedDisclosure);
//               }   
//              }
              
              
              
              //new For COI




              try{

                    disclosureNumber=disclosureBean.getCoiDisclosureNumber();
                    sequenceNumber=disclosureBean.getSequenceNumber();
                    String personID="";
                    if(disclosurePersonId!=null){
                     personID=disclosurePersonId;   
                    }else{
                    personID=loggedInPerson;
                    }
                    if(disclosureType!=null && disclosureType.equals("current") && disclosureBean!=null && disclosureBean.getPersonId()!=null){
                        personID=disclosureBean.getPersonId();
                        if(disclosureBean!=null && disclosureBean.getUpdateUser()!=null){
                        fullName=getFullNameFromuserName(disclosureBean.getUpdateUser(),request);
                        }
                    }
                    String expiryDate=null;
                    Integer moduleCode=disclosureBean.getModuleCode();
                    Hashtable approvedTable=new Hashtable();
                    approvedTable.put(ReportReaderConstants.REPORT_ID, reportID);
                    approvedTable.put("DATA", disclosureBean);
                        approvedTable.put(ReportReaderConstants.DISCLOSURE_NUMBER, disclosureNumber);
                        approvedTable.put(ReportReaderConstants.SEQUENCE_NUMBER, String.valueOf(sequenceNumber));
                        approvedTable.put(ReportReaderConstants.PERSONID, personID);
                        approvedTable.put(ReportReaderConstants.FULLNAME, fullName);
                        approvedTable.put(ReportReaderConstants.MODULE_CODE, String.valueOf(moduleCode));
                        if(disclosureBean.getExpirationDate()!=null){
                            expiryDate=disclosureBean.getExpirationDate().toString().substring(0, 10);
                        }
                        if(expiryDate!=null){
                        approvedTable.put(ReportReaderConstants.EXPIRYDATE,expiryDate);
                        }
                        approvedTable.put("approvedDisclosure", disclosureBean);
                        GetDisclosureForPrint getdisclosureForPrint = new GetDisclosureForPrint();
                        Map disclosureMap = new HashMap();
                        disclosureMap = getdisclosureForPrint.getADisclosure(request, disclosureNumber, String.valueOf(sequenceNumber), personID, moduleCode.intValue());
                        approvedTable.put("disclosureINFO", disclosureMap);

                    approvedTable.put(ReportReaderConstants.REQUEST,request);

                     request.setAttribute(DocumentConstants.READER_CLASS, READER_CLASS);
                     Map map = new HashMap();
                     map.put(ReportReaderConstants.REPORT_ID, reportID);
                     map.put(ReportReaderConstants.REPORT_PARAMS, approvedTable);
                     map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
                     map.put(ReportReaderConstants.DATA, disclosureBean);
                     map.put(ReportReaderConstants.REPORT_NAME, String.valueOf(disclosureNumber) + " Ver :" + String.valueOf(sequenceNumber));
                     map.put(DocumentConstants.READER_CLASS, READER_CLASS);
                     map.put("personId",loggedInPerson);

                       DocumentBean documentBean = new DocumentBean();
                       documentBean.setParameterMap(map);

                       DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
                       String documentId = DocumentIdGenerator.generateDocumentId();

                       request.getSession().setAttribute(documentId, documentBean);
                       actionForward = new ActionForward("/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + documentId, true);



              }catch(Exception e){

                   e.printStackTrace();
                   UtilFactory.log(e.getMessage(), e, "PrintProcessCoiv2", "performExecuteCOI()");

              }







       



              return actionForward;


    }

   private CoiDisclosureBean getDisclosureDetails(String coiDisclosureNumber,Integer sequenceNumber,String personId,HttpServletRequest request) throws Exception {
        HashMap hmData = new HashMap();
        hmData.put("coiDisclosureNumber", coiDisclosureNumber);
        hmData.put("sequenceNumber", sequenceNumber);
        //hmData.put("personId", personId);
        WebTxnBean webTxn = new WebTxnBean();
        Vector statusDispDet = new Vector();
        CoiDisclosureBean coiDisclosurebean=null;
        Hashtable statusData= (Hashtable) webTxn.getResults(request, "getDisclosureDet", hmData);
        statusDispDet = (Vector) statusData.get("getDisclosureDet");
        if(statusDispDet!=null && !statusDispDet.isEmpty()){
            coiDisclosurebean=(CoiDisclosureBean)statusDispDet.get(0);
        }
//        Hashtable statusData = (Hashtable) webTxn.getResults(request, "getDisclDispositionStatus", hmData);
//        statusDispDet = (Vector) statusData.get("getDisclDispositionStatus");
//        if (statusDispDet != null && statusDispDet.size() > 0) {
//            request.setAttribute("statusDispDetView", statusDispDet);
//        }
//        Hashtable DisclData = (Hashtable) webTxn.getResults(request, "getDisclBySequnce", hmData);
//        Vector DisclDet = (Vector) DisclData.get("getDisclBySequnce");
//        if (DisclDet != null && DisclDet.size() > 0) {
//            coiDisclosurebean=(CoiDisclosureBean)DisclDet.get(0);
//            request.setAttribute("ApprovedDisclDetView", DisclDet);
//        }
//        
//        DisclData = (Hashtable) webTxn.getResults(request, "getDisclStatus", hmData);
//        Vector statusDet = (Vector) DisclData.get("getDisclStatus");
//        if (DisclDet != null && DisclDet.size() > 0) {
//            request.setAttribute("statusDetView", statusDet);
//        }
    return coiDisclosurebean;
    }
   public String getFullNameFromuserName(String userName,HttpServletRequest request) throws Exception{
       String fullName=null;
        HashMap hmData = new HashMap();
        hmData.put("userName", userName);
        WebTxnBean webTxn = new WebTxnBean();
        HashMap hasRightMap=null;
        Hashtable fullNameHashmap = (Hashtable) webTxn.getResults(request, "getfullnamefromusername", hmData);
        hasRightMap = (HashMap) fullNameHashmap.get("getfullnamefromusername");
        if (fullNameHashmap != null && fullNameHashmap.size() > 0) {
            fullName =(String) hasRightMap.get("fullName");
        }
        return fullName;
   }

}
