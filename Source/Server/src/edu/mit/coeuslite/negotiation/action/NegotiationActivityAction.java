/*
 * NegotiationActivityAction.java
 *
 * Created on December 30, 2009, 3:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.negotiation.action;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.negotiation.bean.NegotiationActivitiesBean;
import edu.mit.coeus.negotiation.bean.NegotiationAttachmentBean;
import edu.mit.coeus.negotiation.bean.NegotiationTxnBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.document.DocumentIdGenerator;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
import edu.mit.coeus.xml.generator.ReportReaderConstants;
import edu.mit.coeuslite.utils.CoeusliteMenuItems;
import java.io.File;
import java.io.IOException;
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
 * @author suganyadevipv
 */
public class NegotiationActivityAction extends NegotiationBaseAction{
    private static final String EMPTY_STRING = "";
    private static final String ACTION_SUCCESS = "success";
    private static final String GET_NEGOTIATION_ACTIVITIES = "/getActivitiesForNegotiation";
    private static final String VIEW_ATTACHMENT_FOR_ACTIVITY = "/viewAttachmentForActivity";
    private static final String PRINT_ACTIVITY = "/printActivity";
    private static final String NEGOTIATION_NUMBER = "negotiationNumber";
    private static final String LEAD_UNIT = "leadUnit";
    private static final String NEG_NUMBER_FOR_ACTIVITY= "NEGOTIATION_NUMBER";
    private static final String ACTIVITY_NUMBER = "ACTIVITY_NUMBER";
    private static final String NEGOTIATION_ACTIVITIES = "negotiationActivities";
    private static final String PRINT_TYPE = "PRINT_TYPE";
    private static final String USER = "user";
    /** Creates a new instance of NegotiationActivityAction */
    public NegotiationActivityAction() {
    }
    
    public ActionForward performExecuteNegotiation(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        String negotiationNumber = (String)session.getAttribute(NEGOTIATION_NUMBER);
        String leadUnitNumber = (String)session.getAttribute(LEAD_UNIT);
        String navigator = EMPTY_STRING;
        setSelectedStatusMenu(CoeusliteMenuItems.NEGOTIATION_ACTIVITY_MENU,session);
        if(actionMapping.getPath().equals(GET_NEGOTIATION_ACTIVITIES)){
            CoeusVector cvNegotiationActivities = negotiationTxnBean.getNegotiationActivities(negotiationNumber,loggedinUser,leadUnitNumber);
            NegotiationActivitiesBean negotiationActivitiesBean = new NegotiationActivitiesBean();
            Vector vctAllActivities = new Vector();
            int negActivitySize = cvNegotiationActivities.size();
            if(cvNegotiationActivities != null  && negActivitySize>0){
                for(int negActvity=0; negActvity < negActivitySize; negActvity++){
                    negotiationActivitiesBean = (NegotiationActivitiesBean)cvNegotiationActivities.get(negActvity);
                    vctAllActivities.add(negotiationActivitiesBean);
                }
            }
            request.setAttribute(NEGOTIATION_ACTIVITIES,vctAllActivities);
            navigator = ACTION_SUCCESS;
        }else if(actionMapping.getPath().equals(VIEW_ATTACHMENT_FOR_ACTIVITY)){
            String negNumberForActivity = request.getParameter(NEG_NUMBER_FOR_ACTIVITY);
            String activityNumber = request.getParameter(ACTIVITY_NUMBER);
            navigator = performViewAction(negNumberForActivity,activityNumber,request,response);
        }else if(actionMapping.getPath().equals(PRINT_ACTIVITY)){
            String negNumberForActivity = request.getParameter(NEG_NUMBER_FOR_ACTIVITY);
            String activityNumber = request.getParameter(ACTIVITY_NUMBER);
            String printType =  request.getParameter(PRINT_TYPE);
            navigator = performPrintAction(negNumberForActivity,activityNumber,printType,request,response);
        }
        
        return actionMapping.findForward(navigator);
    }
    /**This method is used View the attachement, if it is present
     *@param request - HttpServletRequestObject.
     *@param response - HttpServletResponseObject.
     *@param negotiationNumber - negotiationNumber .
     *@param activityNumber - ACTIVITY_NUMBER from OSP$NEGOTIATION_ACTIVITIES.
     */
    private String performViewAction(String negotiationNumber,String activityNumber,HttpServletRequest request,HttpServletResponse response ) throws Exception{
        //RequesterBean request = new RequesterBean();
        HttpSession session = request.getSession();
        Vector dataObjects = new Vector();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        NegotiationAttachmentBean negotiationAttachmentBean = new NegotiationAttachmentBean();
        negotiationAttachmentBean = getBlobDataForNegActivity(negotiationNumber,activityNumber);
        if(negotiationAttachmentBean !=null){
            negotiationAttachmentBean.getFileBytes();
        }
        dataObjects.add(0, negotiationAttachmentBean);
        Map map = new HashMap();
        map.put("DATA", dataObjects);
        map.put("USER_ID", loggedinUser);
        map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.negotiation.NegotiationAttachmentReader");
        DocumentBean documentBean = new DocumentBean();
        documentBean.setParameterMap(map);
        String docId = DocumentIdGenerator.generateDocumentId();
        
        //Prepare Complete path Info
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        
        
        request.getSession().removeAttribute(docId);
        request.getSession().setAttribute(docId, documentBean);
        
        response.sendRedirect("StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId);
        return null;
    }
    
    /**This method is used get the Blob data for negotiation activity
     *@param request - HttpServletRequestObject.
     *@param response - HttpServletResponseObject.
     *@param negotiationNumber - negotiationNumber .
     *@param activityNumber - ACTIVITY_NUMBER from OSP$NEGOTIATION_ACTIVITIES.
     */
    private NegotiationAttachmentBean getBlobDataForNegActivity(String negotiationNumber,String activityNumber) throws Exception{
        NegotiationAttachmentBean negotiationAttachmentBean = new NegotiationAttachmentBean();
        negotiationAttachmentBean.setNegotiationNumber(negotiationNumber);
        negotiationAttachmentBean.setActivityNumber(Integer.parseInt(activityNumber));
        NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
        negotiationAttachmentBean = negotiationTxnBean.getNegotiationDocument(negotiationAttachmentBean);
        return negotiationAttachmentBean;
    }
    /**This method is used print the Activities
     *@param request - HttpServletRequestObject.
     *@param response - HttpServletResponseObject.
     *@param negotiationNumber - negotiationNumber .
     *@param activityNumber - ACTIVITY_NUMBER from OSP$NEGOTIATION_ACTIVITIES.
     */
    private String performPrintAction(String negNumberForActivity, String activityNumber, String printType, HttpServletRequest request, HttpServletResponse response) throws IOException, CoeusException {
        String reportId = EMPTY_STRING;
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        String loggedinUser = userInfoBean.getUserId();
        if (printType.equalsIgnoreCase("printNegotiation")) {
            reportId = "Negotiation/ActivityPrintAll";
        }else{
            reportId = "Negotiation/Activity";
        }
        CoeusReportGroupBean.Report report = ReportConfigEngine.getReport(reportId);
        Hashtable repParams = new Hashtable();
        if("printOne".equalsIgnoreCase(printType)){
            repParams.put("ACTIVITY_NUM",activityNumber);
        }
        repParams.put("NEGOTIATION_NUM", negNumberForActivity);
        repParams.put("PRINT_TYPE", printType);
        repParams.put("USER_ID", loggedinUser);
        
        String reportDir = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH);
        String reportPath = request.getSession().getServletContext().getRealPath("/")+File.separator+reportDir;
        String repName = report.getDispValue().replace(' ','_');
        
        Map map = new HashMap();
        map.put(ReportReaderConstants.REPORT_ID, reportId);
        map.put(ReportReaderConstants.REPOORT_PATH, reportPath);
        map.put(ReportReaderConstants.REPORT_NAME, repName);
        map.put(ReportReaderConstants.REPORT_PARAMS, repParams);
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.xml.generator.ReportReader");
        
        
        DocumentBean documentBean = new DocumentBean();
        documentBean.setParameterMap(map);
        String docId = DocumentIdGenerator.generateDocumentId();
        
        //Prepare Complete path Info
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("/StreamingServlet");
        stringBuffer.append("?");
        stringBuffer.append(DocumentConstants.DOC_ID);
        stringBuffer.append("=");
        stringBuffer.append(docId);
        
        
        request.getSession().removeAttribute(docId);
        request.getSession().setAttribute(docId, documentBean);
        
        response.sendRedirect("StreamingServlet?"+DocumentConstants.DOC_ID+"="+docId);
        return null;
    }
    
}