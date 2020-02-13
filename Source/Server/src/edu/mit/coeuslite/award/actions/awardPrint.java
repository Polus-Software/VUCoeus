/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.actions;

import edu.mit.coeus.award.bean.AwardBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.document.CoeusDocument;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.ReportReader;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.award.services.GetAwardHierarchyForPrint;
import edu.mit.coeuslite.award.services.GetAwardSummaryDetailsForPrint;
import edu.mit.coeuslite.utils.SessionConstants;
import java.io.File;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author midhunmk
 */
public class awardPrint extends AwardBaseAction {
      private static final String DLG_TITLE = "Print Notice";
    //private static final String SERVLET = "/AwardMaintenanceServlet";
    private static final String SERVLET = "/ReportConfigServlet";
    public static final String READER_CLASS = "edu.mit.coeus.xml.generator.ReportReader";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    private static final String ADDRESS_LIST = "ADDRESS_LIST";
    private static final String CLOSEOUT = "CLOSEOUT";
    private static final String COMMENTS = "COMMENTS";
    private static final String COST_SHARING = "COST_SHARING";
    private static final String EQUIPMENT = "EQUIPMENT";
    private static final String FLOW_THRU = "FLOW_THRU";
    private static final String FOREIGN_TRAVEL = "FOREIGN_TRAVEL";
    private static final String HIERARCHY_INFO = "HIERARCHY_INFO";
    private static final String INDIRECT_COST = "INDIRECT_COST";
    private static final String PAYEMENT = "PAYEMENT";
    private static final String PROPOSAL_DUE = "PROPOSAL_DUE";
    private static final String REPORTING = "REPORTING";
    private static final String SIGNATURE_REQUIRED ="SIGNATURE_REQUIRED";
    private static final String SCIENCE_CODE = "SCIENCE_CODE";
    private static final String SPECIAL_REVIEW = "SPECIAL_REVIEW";
    private static final String SUBCONTRACT = "SUBCONTRACT";
    private static final String TECH_REPORTING = "TECH_REPORTING";
    private static final String TERMS = "TERMS";
    //start csse 2010
    private static final String OTHER_DATA = "OTHER_DATA";
    //end case 2010
    //Added for Case 3122 - Award Notice Enhancement - Start
    private static final String FUNDING_SUMMARY = "FUNDING_SUMMARY";
     //Added for Case 3122 - Award Notice Enhancement - End
    
      private Hashtable getSelectedItems(int i) {
        Hashtable htSelectedItems = new Hashtable();
//to all the details
        htSelectedItems.put(SIGNATURE_REQUIRED, true);
        htSelectedItems.put(ADDRESS_LIST, true);
        htSelectedItems.put(CLOSEOUT, true);
        htSelectedItems.put(COMMENTS, true);
        htSelectedItems.put(COST_SHARING, true);
        htSelectedItems.put(EQUIPMENT, true);
        htSelectedItems.put(FLOW_THRU, true);
        htSelectedItems.put(FOREIGN_TRAVEL, true);
        htSelectedItems.put(HIERARCHY_INFO,true);
        htSelectedItems.put(INDIRECT_COST, true);
        htSelectedItems.put(PAYEMENT,true);
        htSelectedItems.put(PROPOSAL_DUE,true);
        htSelectedItems.put(REPORTING, true);
        htSelectedItems.put(SCIENCE_CODE,true);
        htSelectedItems.put(SPECIAL_REVIEW,true);
        htSelectedItems.put(SUBCONTRACT, true);
        htSelectedItems.put(TECH_REPORTING,true);
        htSelectedItems.put(TERMS,true);
        //start csse 2010
        htSelectedItems.put(OTHER_DATA,true);
       //end case 2010
         //Added for Case 3122 - Award Notice Enhancement - Start
        htSelectedItems.put(FUNDING_SUMMARY,true);
         //Added for Case 3122 - Award Notice Enhancement - End
        if(i==2)
        {//award summary.
        htSelectedItems.put(SIGNATURE_REQUIRED, false);
        htSelectedItems.put(ADDRESS_LIST, false);
        htSelectedItems.put(CLOSEOUT, false);
        htSelectedItems.put(COMMENTS, false);
       // htSelectedItems.put(COST_SHARING, true);
       // htSelectedItems.put(EQUIPMENT, true);
        htSelectedItems.put(FLOW_THRU, false);
      //  htSelectedItems.put(FOREIGN_TRAVEL, true);
        htSelectedItems.put(HIERARCHY_INFO,false);
     //   htSelectedItems.put(INDIRECT_COST, true);
    //    htSelectedItems.put(PAYEMENT,true);
   //     htSelectedItems.put(PROPOSAL_DUE,true);
        htSelectedItems.put(REPORTING, false);
        htSelectedItems.put(SCIENCE_CODE,false);
        htSelectedItems.put(SPECIAL_REVIEW,false);
   //     htSelectedItems.put(SUBCONTRACT, true);
        htSelectedItems.put(TECH_REPORTING,false);
        htSelectedItems.put(TERMS,false);
        //start csse 2010
      //  htSelectedItems.put(OTHER_DATA,true);
        }
        if(i==3)
        {//award hierarchy
         htSelectedItems.put(SIGNATURE_REQUIRED, false);
        htSelectedItems.put(ADDRESS_LIST, false);
        htSelectedItems.put(CLOSEOUT, false);
        htSelectedItems.put(COMMENTS, false);
        htSelectedItems.put(COST_SHARING, false);
        htSelectedItems.put(EQUIPMENT, false);
        htSelectedItems.put(FLOW_THRU, false);
        htSelectedItems.put(FOREIGN_TRAVEL, false);
      //  htSelectedItems.put(HIERARCHY_INFO,true);
        htSelectedItems.put(INDIRECT_COST, false);
        htSelectedItems.put(PAYEMENT,false);
        htSelectedItems.put(PROPOSAL_DUE,false);
        htSelectedItems.put(REPORTING, false);
        htSelectedItems.put(SCIENCE_CODE,false);
        htSelectedItems.put(SPECIAL_REVIEW,false);
        htSelectedItems.put(SUBCONTRACT, false);
        htSelectedItems.put(TECH_REPORTING,false);
        htSelectedItems.put(TERMS,false);
        //start csse 2010
        htSelectedItems.put(OTHER_DATA,false);
       //end case 2010
         //Added for Case 3122 - Award Notice Enhancement - Start
        htSelectedItems.put(FUNDING_SUMMARY,false);
        }

        return htSelectedItems;
    }
        private static final char AWARD_NOTICE = 'a';
       private AwardBean awardBean;
    private static final String AWARD_BEAN="AWARD_BEAN";
    
    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
        HttpServletRequest request, HttpServletResponse response) throws Exception {
           ActionForward actionForward=null;
            HttpSession session = request.getSession();
            String awdType=request.getParameter("awdType");
  RequesterBean requesterBean = new RequesterBean();
   PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON);
              String loggedInPerson=personInfoBean.getPersonID();
        requesterBean.setFunctionType(AWARD_NOTICE);
        if(awdType.compareTo("2")==0)
        {
              String reportID = "AwardCoeuslite/AwardSummary";
              String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
              String reportPath = session.getServletContext().getRealPath("/") + File.separator + reportDir;
              String mitAwardNumber=session.getAttribute("mitawardnumber").toString();
              Hashtable awardDetails=new Hashtable();
              GetAwardSummaryDetailsForPrint getAwardSummaryDetailsForPrint=new GetAwardSummaryDetailsForPrint();
              awardDetails.put("AwardDetails",
                      getAwardSummaryDetailsForPrint.getAwardSummaryDetails(request,mitAwardNumber));
                     Map map = new HashMap();
                     map.put(ReportReaderConstants.REPORT_ID, reportID);
                     map.put(ReportReaderConstants.REPORT_PARAMS, awardDetails);
                     map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
                     map.put(ReportReaderConstants.REPORT_NAME, String.valueOf(mitAwardNumber));
                     map.put(DocumentConstants.READER_CLASS, READER_CLASS);
                     map.put("personId",loggedInPerson);
                       DocumentBean documentBean = new DocumentBean();
                       documentBean.setParameterMap(map);

                       DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
                       String documentId = documentIdGenerator.generateDocumentId();

                       request.getSession().setAttribute(documentId, documentBean);
                       actionForward = new ActionForward("/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + documentId, true);

        }//for first print option
        else if(awdType.compareTo("3")==0){
            String reportID = "AwardCoeuslite/AwardHierarchy";
              String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
              String reportPath = session.getServletContext().getRealPath("/") + File.separator + reportDir;
              String mitAwardNumber=session.getAttribute("mitawardnumber").toString();
              Hashtable awardDetails=new Hashtable();
             GetAwardHierarchyForPrint getAwardHierarchyForPrint= new GetAwardHierarchyForPrint();
             awardDetails.put("AwardHierarchy",
                      getAwardHierarchyForPrint.getAwardHierarchy(request, mitAwardNumber));
             awardDetails.put("mitAwardNumber",mitAwardNumber);
                     Map map = new HashMap();
                     map.put(ReportReaderConstants.REPORT_ID, reportID);
                     map.put(ReportReaderConstants.REPORT_PARAMS, awardDetails);
                     map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
                     map.put(ReportReaderConstants.REPORT_NAME, String.valueOf(mitAwardNumber));
                     map.put(DocumentConstants.READER_CLASS, READER_CLASS);
                     map.put("personId",loggedInPerson);
                       DocumentBean documentBean = new DocumentBean();
                       documentBean.setParameterMap(map);

                       DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
                       String documentId = documentIdGenerator.generateDocumentId();

                       request.getSession().setAttribute(documentId, documentBean);
                       actionForward = new ActionForward("/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + documentId, true);

        }//for first print option


        else
        {
        Hashtable htDataToSend = getSelectedItems(Integer.parseInt(awdType));
        AwardTxnBean atb=new AwardTxnBean();
      
        awardBean=atb.getAward(session.getAttribute("mitawardnumber").toString());
        htDataToSend.put(AWARD_BEAN, awardBean);
        requesterBean.setDataObject(htDataToSend);


        //For Streaming
        htDataToSend.put("LOGGED_IN_USER", loggedInPerson);
        htDataToSend.put("REPORT_TYPE","AwardNotice");
        requesterBean.setId("Award/AwardNotice");
        requesterBean.setFunctionType('R');
        String repId = (String)requesterBean.getId();
                    CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(repId);
                    Hashtable repParams = (Hashtable)requesterBean.getDataObject();
                    
                    repParams.put("REPORT_ID", repId);
                    String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
                    String reportPath = request.getContextPath()+("/")+File.separator+reportDir;

                    String repName = report.getDispValue().replace(' ','_');

                    Map map = new HashMap();
                    map.put(ReportReaderConstants.REPORT_ID, repId);
                    map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
                    map.put(ReportReaderConstants.REPORT_NAME, repName);
                    map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
                    ReportReader reportReader = new ReportReader();
                    CoeusDocument coeusDocument = reportReader.read(map);
                    DocumentIdGenerator documentIdGenerator = new DocumentIdGenerator();
                    String documentId = documentIdGenerator.generateDocumentId();
                     HashMap retMap = new HashMap();
                     retMap.put(DocumentConstants.DOCUMENT_URL, "/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + documentId);
                    retMap.put(DocumentConstants.COEUS_DOCUMENT, coeusDocument);

                      DocumentBean documentBean = new DocumentBean();
                      documentBean.setParameterMap(retMap);
                   
                       
               request.getSession().setAttribute(documentId, documentBean);
                       actionForward = new ActionForward("/StreamingServlet?" + DocumentConstants.DOC_ID + "=" + documentId, true);
       }
        return actionForward;
    }

  
}
