/*
 * S2SServlet.java
 *
 * Created on October 19, 2004, 11:25 AM
 */

package edu.mit.coeus.servlet;
/*
 *  Grants.gov classes
 */
import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.bean.BudgetSubAwardAttachmentBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.s2s.GetXMLFromPureEdge;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import gov.grants.apply.soap.util.SoapUtils;

/*
 *  Coeus Specific classes
 */
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.dbengine.DBException;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.GetAppStatusDetails;
import edu.mit.coeus.s2s.GetApplication;
import edu.mit.coeus.s2s.GetApplicationInfo;
import edu.mit.coeus.s2s.GetOpportunity;
import edu.mit.coeus.s2s.GetSubmission;
import edu.mit.coeus.s2s.SubmissionEngine;
import edu.mit.coeus.s2s.bean.ApplicationInfoBean;
import edu.mit.coeus.s2s.bean.DBOpportunityInfoBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.s2s.bean.S2SPrintForm;
import edu.mit.coeus.s2s.bean.S2SSubmissionDataTxnBean;
import edu.mit.coeus.s2s.bean.SubmissionDetailInfoBean;
import edu.mit.coeus.s2s.bean.SubmissionInfoBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2SFormBean;
import edu.mit.coeus.s2s.bean.UserAttachedS2STxnBean;
import edu.mit.coeus.s2s.formattachment.FormAttachmentExtractService;
import edu.mit.coeus.s2s.validator.OpportunitySchemaParser;
import edu.mit.coeus.s2s.validator.S2SValidationException;
import edu.mit.coeus.s2s.validator.UniqueSchemaNotFoundException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import gov.grants.apply.xml.util.InvalidXmlException;


import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.axis.AxisFault;

/**
 *
 * @author  geot
 * @version
 */
public class S2SServlet  extends CoeusBaseServlet implements S2SConstants{
//    private UtilFactory UtilFactory;
    private CoeusMessageResourcesBean coeusMessageResourcesBean;
    /** Initializes the servlet.
     */
   public void init(ServletConfig config) throws ServletException {
        super.init(config);
//        configureSSL();
        configureSoap();
        coeusMessageResourcesBean = new CoeusMessageResourcesBean();
//        UtilFactory = new UtilFactory();
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        char functionType ;
        
        boolean validXml = true;
//        Properties coeusProps = (Properties)getServletContext().getAttribute(CoeusConstants.COEUS_PROPS);
        String reportFolder = CoeusProperties.getProperty(CoeusPropertyKeys.REPORT_GENERATED_PATH,"Reports");
        String debugMode = CoeusProperties.getProperty(CoeusPropertyKeys.GENERATE_XML_FOR_DEBUGGING) ;
        String reportPath = getServletContext().getRealPath("/")+reportFolder+"/";

        try{
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            String loggedinUser = requester.getUserName();
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            txnBean.setUserId(loggedinUser);
            // keep all the beans into vector

            UserAttachedS2STxnBean userAttachedS2STxnBean = new UserAttachedS2STxnBean();
            userAttachedS2STxnBean.setUserId(loggedinUser);

            functionType = requester.getFunctionType();
            SubmissionEngine subEngine = null;
            S2SHeader headerParam = null;
            Object[] s2sDetails = null;
            Object[] tmpArray = null;
            HashMap rightFlags = null;
            
            UserMaintDataTxnBean usrTxn = new UserMaintDataTxnBean();
            ProposalDevelopmentTxnBean propTxnBean = new ProposalDevelopmentTxnBean();//3587
            switch(functionType){
                case(IS_S2S_CANDIDATE):
                    responder.setDataObject(new Boolean(txnBean.isS2SCandidate((String)requester.getDataObject())));
                    break;
//                case(SUBMIT_FORM_WITH_SCHEMA):
//                    int mode = functionType==SUBMIT_FORM_FIRST?1:(functionType==SUBMIT_FORM_WITH_SCHEMA?2:(functionType==SUBMIT_FORM_WITH_FORM_URL_LIST?3:4));
//                    subEngine = new SubmissionEngine(mode);
//                    subEngine.setLoggedInUser(loggedinUser);
//                    subEngine.setValidateAgainstSchema(false);
//                    S2SHeader submReqData = (S2SHeader)requester.getDataObject();
//                    _SubmitApplicationResponse submitResponse = 
//                            subEngine.submitApplication(submReqData);
//                    UtilFactory.log("Submitted the form");
//                    System.out.println("Tracking number"+submitResponse.getGrants_govTrackingNumber().toString());
//                    System.out.println("Recieved time"+submitResponse.getReceivedDateTime().getTime().toString());
//                    S2SSubmissionStatusBean s2sSubStatusBean = new S2SSubmissionStatusBean();
//                    s2sSubStatusBean.setAppReceiptDate(submitResponse.getReceivedDateTime());
//                    s2sSubStatusBean.setGgTrackingNumber(submitResponse.getGrants_govTrackingNumber().toString());
//                    responder.setDataObject(s2sSubStatusBean);
//                    break;
                case(SAVE_FORMS_N_SUBMIT_APP):
                    txnBean.addUpdDelOppForms(requester.getDataObjects());
                    subEngine = SubmissionEngine.getInstance();
                    subEngine.setLoggedInUser(loggedinUser);
                    subEngine.setLogDir(reportPath);
                    subEngine.submitApplication((S2SHeader)requester.getDataObject());
                    responder.setDataObject(txnBean.getS2SDetails((S2SHeader)requester.getDataObject()));
                    break;
                case(VALIDATE_APPLICATION):
                    Vector oppData = requester.getDataObjects();
                    if(oppData!=null)
                        txnBean.addUpdDelOppForms(oppData);
                    subEngine = SubmissionEngine.getInstance();
                    subEngine.setLoggedInUser(loggedinUser);
                    subEngine.setLogDir(reportPath);
                    subEngine.validateData((S2SHeader)requester.getDataObject());
                    responder.setDataObject(txnBean.getS2SDetails((S2SHeader)requester.getDataObject()));
                    break;
                case(GET_OPPORTUNITY_LIST):
                    responder.setDataObject(GetOpportunity.getInstance().searchOpportunityList(
                                            (S2SHeader)requester.getDataObject()));
                    break;
                case(GET_STATUS_DETAIL):
//                    responder.setDataObject(new GetAppStatusDetails().getStatusDetails(
//                            (String)requester.getDataObject()));
                    //sending proposal number and ggtracking id for getting the status detail
                    Vector data = requester.getDataObjects();
                    responder.setDataObject(GetApplicationInfo.getInstance().getStatusDetails(
                            (String)data.get(0),(String)data.get(1)));
                    break;
                case (CHECK_FORMS_AVAILABLE):
                    //JIRA COEUSDEV 61 - START
                	DBOpportunityInfoBean sltdOppBean = (DBOpportunityInfoBean)requester.getDataObject();
                    new OpportunitySchemaParser().checkFormsAvailable(sltdOppBean.getProposalNumber(),sltdOppBean.getSchemaUrl());
                    //JIRA COEUSDEV 61 - END
                    break;
                case(SAVE_OPPORTUNITY):
                    txnBean.addOpportunity((OpportunityInfoBean)requester.getDataObject());
                    break;
                case(DELETE_OPPORTUNITY):
                    txnBean.updateDelOpportunity((OpportunityInfoBean)requester.getDataObject());
                    break;
                case(SAVE_GRANTS_GOV):
                    txnBean.addUpdDelOppForms(requester.getDataObjects());
                    responder.setDataObject(txnBean.getS2SDetails((S2SHeader)requester.getDataObject()));
                    break;
                case(REFRESH_GRANTS_DATA):
                    headerParam = (S2SHeader)requester.getDataObject();
                    SubmissionDetailInfoBean localSubInfo = (SubmissionDetailInfoBean)txnBean.getSubmissionDetails(headerParam);
                    GetSubmission appReq = GetSubmission.getInstance();
                    SubmissionInfoBean[] appList = appReq.getSubmissionList(headerParam);
                    localSubInfo.setAcType('U');
                    localSubInfo.setUpdateUser(loggedinUser);
                    if(appList==null || appList.length==0){//Need to check whether there is any error during the submission
                                        //by calling getApplicationStatusDetail web service
                        Object statusDetail = GetApplicationInfo.getInstance().getStatusDetails(localSubInfo.getGrantsGovTrackingNumber(),headerParam.getSubmissionTitle());
                        if(statusDetail==null) 
                            throw new CoeusException(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90001"));
                        String comments = statusDetail.toString();
                        localSubInfo.setComments(comments);
                        String status = comments.toUpperCase().indexOf("ERROR")==-1?
                                           comments:
                                           coeusMessageResourcesBean.parseMessageKey("exceptionCode.90011");
                        localSubInfo.setStatus(status);
                    }else for(SubmissionInfoBean latestInfo:appList){
                        //Case# COEUSDEV-1101 
                        //BEGIN
                        //ApplicationInfoBean latestInfo = application;
                        if(latestInfo.getGrantsGovTrackingNumber()!=null && 
                                latestInfo.getGrantsGovTrackingNumber().equals(localSubInfo.getGrantsGovTrackingNumber())){
                            localSubInfo.setReceivedDateTime(latestInfo.getReceivedDateTime());
                            localSubInfo.setStatus(latestInfo.getStatus());
                            localSubInfo.setStatusDate(latestInfo.getStatusDate());
                            localSubInfo.setAgencyTrackingNumber(latestInfo.getAgencyTrackingNumber());
                            if(latestInfo.getAgencyTrackingNumber()!=null && latestInfo.getAgencyTrackingNumber().length()>0){
                                localSubInfo.setComments(coeusMessageResourcesBean.parseMessageKey("exceptionCode.90003"));
                            }else{
                                localSubInfo.setComments(latestInfo.getStatus());
                            }
                            break;
                        }
                        //END
                    }
                    txnBean.addUpdDeleteSubmissionDetails(localSubInfo);
                    s2sDetails = new Object[4];
                    tmpArray = txnBean.getS2SDetails(headerParam);
                    for(int i=0;i<3;i++) s2sDetails[i] = tmpArray[i];
                    rightFlags = new HashMap();
                    //Modified with Case 3587:Multicampus Enhancement
                    String leadUnit = propTxnBean.getProposalLeadUnit(headerParam.getSubmissionTitle());
                    rightFlags.put(SUBMIT_TO_SPONSOR,
                        new Boolean(usrTxn.getUserHasRight(loggedinUser, 
                            SUBMIT_TO_SPONSOR,leadUnit)));
//                    rightFlags.put(SUBMIT_TO_SPONSOR,
//                        new Boolean(usrTxn.getUserHasOSPRight(loggedinUser, 
//                            SUBMIT_TO_SPONSOR)));
                    //3587 End
                    rightFlags.put(IS_READY_TO_SUBMIT,new Boolean(txnBean.isProposalReadyForS2S(
                                headerParam.getSubmissionTitle())));
                    rightFlags.put(IS_ATTR_MATCH,new Boolean(txnBean.isS2SAttrMatch(
                                headerParam.getSubmissionTitle())));
                    s2sDetails[3] = rightFlags;
                    responder.setDataObject(txnBean.getS2SDetails(headerParam));
                    break;
                case(GET_DATA):
                    headerParam = (S2SHeader)requester.getDataObject();
                    s2sDetails = new Object[7];
                    tmpArray = txnBean.getS2SDetails(headerParam);
                    for(int i=0;i<3;i++) s2sDetails[i] = tmpArray[i];
                    if(s2sDetails[0]==null){//opportunity is null, try to submit and 
                                            //it will internaly call getOpportunity web service 
                                            //and throw UniquSchemaNotFoundException
                        ArrayList oppList = GetOpportunity.getInstance().searchOpportunityList(headerParam);
                        UniqueSchemaNotFoundException usnEx = new UniqueSchemaNotFoundException();
                        usnEx.setOpportunityList(oppList);
                        throw usnEx;
                    }else if(s2sDetails[1]==null){
                        OpportunityInfoBean oppInfo = (OpportunityInfoBean)s2sDetails[0];
                        try{
                            s2sDetails[1] = new OpportunitySchemaParser().getFormsList(headerParam.getSubmissionTitle(),oppInfo.getSchemaUrl());
                        }catch(FileNotFoundException fnfEx){
                            ArrayList oppList = GetOpportunity.getInstance().searchOpportunityList(headerParam);
                            UniqueSchemaNotFoundException usnEx = new UniqueSchemaNotFoundException();
                            usnEx.setMainError("The Selected Opportunity "+oppInfo.getSchemaUrl() +
                                                " is not found at the specified location."+
                                                "Please select another opportunity from the list");
                            usnEx.setOpportunityList(oppList);
                            throw usnEx;
                        }
                    }
                    rightFlags = new HashMap();
                    //Modified with Case 3587:Multicampus Enhancement
                    String unitNumber = propTxnBean.getProposalLeadUnit(headerParam.getSubmissionTitle());
                    rightFlags.put(SUBMIT_TO_SPONSOR,
                        new Boolean(usrTxn.getUserHasRight(loggedinUser, 
                            SUBMIT_TO_SPONSOR,unitNumber)));
                    rightFlags.put(IS_READY_TO_SUBMIT,
                        new Boolean(txnBean.isProposalReadyForS2S(
                                headerParam.getSubmissionTitle())));
                    rightFlags.put(IS_ATTR_MATCH,new Boolean(txnBean.isS2SAttrMatch(
                                headerParam.getSubmissionTitle())));
//                    rightFlags.put(ALTER_PROPOSAL_DATA,
//                        new Boolean(usrTxn.getUserHasOSPRight(loggedinUser, 
//                            ALTER_PROPOSAL_DATA)));
                    rightFlags.put(ALTER_PROPOSAL_DATA,
                        new Boolean(usrTxn.getUserHasRight(loggedinUser, 
                            ALTER_PROPOSAL_DATA,unitNumber)));
                    //3587:End
                    s2sDetails[3] = rightFlags;
                    
                    //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - START
                    List submissionTypes = txnBean.getSubmissionTypes();
                    s2sDetails[4] = submissionTypes;
                    CoeusFunctions coeusFunctions = new CoeusFunctions();
                    String defaultSelect = coeusFunctions.getParameterValue("DEFAULT_S2S_SUBMISSION_TYPE");
                    s2sDetails[5] = defaultSelect;
                    //modification for new columns in OSP$S2S_OPPORTUNITY, S2S_SUBMISSION_TYPE_CODE, REVISION_CODE, REVISION_OTHER_DESCRIPTION - END

                    List submissionEndPoints = txnBean.getSubmissionEndPoints();
                    s2sDetails[6] = submissionEndPoints;
                    
                    responder.setDataObject(s2sDetails);
                    
                    break;
                case(PRINT_FORM):
                    S2SPrintForm formPrint = new S2SPrintForm();
                    headerParam = (S2SHeader)requester.getDataObject();
                    Vector formsList = requester.getDataObjects();
                    String fileName = formPrint.printForm(formsList, headerParam,reportPath);
                    responder.setDataObject("/"+reportFolder+"/"+fileName);
                    break;
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                case(GET_DIVISION):
                    String homeUnit = (String)requester.getDataObject();
                    S2STxnBean s2STxnBean = new S2STxnBean();
                    String divisionValue = s2STxnBean.fn_get_division(homeUnit);
                    responder.setDataObject(divisionValue);
                    break;
                //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
                case GET_XML_FROM_PURE_EDGE:
                    /**
                     * If a List of BudgetSubAwardBeans is sent as Request, the XML from pureEdge
                     * would be set in the bean and the List is sent back as response.
                     * If BudgetSubAwardBean or byte array of PDF is sent as request, the XMl from pureEdge
                     * would be sent back as an char array.
                     */
                    GetXMLFromPureEdge getXMLFromPureEdge = new GetXMLFromPureEdge();
                    Object reqObject = requester.getDataObject();
                    Object retObject = null;
                    String userId = requester.getUserName();
                    if(reqObject instanceof List) {
                        List list = (List)reqObject;
                        BudgetSubAwardBean budgetSubAwardBean;
                        char xmlData[];
                        for(int index = 0; index < list.size(); index++) {
                            budgetSubAwardBean = (BudgetSubAwardBean)list.get(index);
                            if(budgetSubAwardBean.getPdfAcType() != null || budgetSubAwardBean.getXmlAcType() != null) {
                                try{
                                    xmlData = getXMLFromPureEdge.getXML(budgetSubAwardBean.getSubAwardPDF());
                                    budgetSubAwardBean.setSubAwardXML(xmlData);
                                    budgetSubAwardBean.setTranslationComments(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY);
                                    budgetSubAwardBean.setXmlUpdateUser(userId);
                                    //Attachments - START
                                    Object attachments[] = getXMLFromPureEdge.getAttachments();
                                    if(attachments != null) {
                                        BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
                                        javax.xml.soap.AttachmentPart attachmentPart;
                                        List attachmentList = new ArrayList();
                                        InputStream inputStream;
                                        byte attachmentBytes[];
                                        //Note : Start Index from 1, since 0th index is reserved for XML Genaration.
                                        for(int attachIndex = 1; attachIndex < attachments.length; attachIndex++) {
                                            attachmentPart = (javax.xml.soap.AttachmentPart)attachments[attachIndex];
                                            inputStream = attachmentPart.getDataHandler().getInputStream();
                                            attachmentBytes = new byte[inputStream.available()];
                                            inputStream.read(attachmentBytes);
                                            inputStream.close();
                                            budgetSubAwardAttachmentBean = new BudgetSubAwardAttachmentBean();
                                            budgetSubAwardAttachmentBean.setAttachment(attachmentBytes);
                                            budgetSubAwardAttachmentBean.setContentId(attachmentPart.getContentId());
                                            budgetSubAwardAttachmentBean.setContentType(attachmentPart.getContentType());
                                            budgetSubAwardAttachmentBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
                                            budgetSubAwardAttachmentBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
                                            budgetSubAwardAttachmentBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                                            budgetSubAwardAttachmentBean.setAcType(TypeConstants.INSERT_RECORD);
                                            attachmentList.add(budgetSubAwardAttachmentBean);
                                        }//End For
                                        budgetSubAwardBean.setAttachments(attachmentList);
                                    }//End if attachments != null
                                    //Attachments - END
                                }catch (CoeusException coeusException) {
                                    budgetSubAwardBean.setTranslationComments(coeusException.getMessage());
                                }
                            }
                        }
                        retObject = list;
                    }else if(reqObject instanceof BudgetSubAwardBean) {
                        BudgetSubAwardBean budgetSubAwardBean = (BudgetSubAwardBean)reqObject;
                        try{
                            char xmlData[] = getXMLFromPureEdge.getXML(budgetSubAwardBean.getSubAwardPDF());
                            budgetSubAwardBean.setSubAwardXML(xmlData);
                            budgetSubAwardBean.setTranslationComments(BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY);
                            budgetSubAwardBean.setXmlUpdateUser(userId);
                            //Attachments - START
                            Object attachments[] = getXMLFromPureEdge.getAttachments();
                            if(attachments != null) {
                                BudgetSubAwardAttachmentBean budgetSubAwardAttachmentBean;
                                javax.xml.soap.AttachmentPart attachmentPart;
                                List attachmentList = new ArrayList();
                                InputStream inputStream;
                                byte attachmentBytes[];
                                //Note : Start Index from 1, since 0th index is reserved for XML Genaration.
                                for(int attachIndex = 1; attachIndex < attachments.length; attachIndex++) {
                                    attachmentPart = (javax.xml.soap.AttachmentPart)attachments[attachIndex];
                                    inputStream = attachmentPart.getDataHandler().getInputStream();
                                    attachmentBytes = new byte[inputStream.available()];
                                    inputStream.read(attachmentBytes);
                                    inputStream.close();
                                    budgetSubAwardAttachmentBean = new BudgetSubAwardAttachmentBean();
                                    budgetSubAwardAttachmentBean.setAttachment(attachmentBytes);
                                    budgetSubAwardAttachmentBean.setContentId(attachmentPart.getContentId());
                                    budgetSubAwardAttachmentBean.setContentType(attachmentPart.getContentType());
                                    budgetSubAwardAttachmentBean.setProposalNumber(budgetSubAwardBean.getProposalNumber());
                                    budgetSubAwardAttachmentBean.setVersionNumber(budgetSubAwardBean.getVersionNumber());
                                    budgetSubAwardAttachmentBean.setSubAwardNumber(budgetSubAwardBean.getSubAwardNumber());
                                    budgetSubAwardAttachmentBean.setAcType(TypeConstants.INSERT_RECORD);
                                    attachmentList.add(budgetSubAwardAttachmentBean);
                                }//End For
                                budgetSubAwardBean.setAttachments(attachmentList);
                            }//End if attachments != null
                            //Attachments - END
                        }catch (CoeusException coeusException) {
                            budgetSubAwardBean.setTranslationComments(coeusException.getMessage());
                        }
                        retObject = budgetSubAwardBean;
                    }else if(reqObject instanceof byte[]){
                        byte pdfData[] = (byte[])requester.getDataObject();
                        reqObject = getXMLFromPureEdge.getXML(pdfData);
                    }
                    
                    responder.setDataObject(retObject);
                    break;
             case GET_USER_ATTACHED_S2S_FORM:
                userAttachedS2STxnBean = new UserAttachedS2STxnBean();
                UserAttachedS2SFormBean userAttachedS2SFormBean = (UserAttachedS2SFormBean)requester.getDataObject();
                Vector vecResult = new Vector();
                vecResult.add(userAttachedS2STxnBean.getUserAttachedS2SForm(userAttachedS2SFormBean.getProposalNumber()));
                responder.setDataObjects(vecResult);                
                responder.setResponseStatus(true);
                  break;
             case SAVE_USER_ATTACHED_S2S_FORM:
                 userAttachedS2STxnBean = new UserAttachedS2STxnBean();
                 List list = (List) requester.getDataObject();
                 List userAttachedFormsList = new ArrayList();
                 for (int i = 0; i < list.size(); i++) {
                     UserAttachedS2SFormBean userAttachedS2SForm = (UserAttachedS2SFormBean) list.get(i);
                     FormAttachmentExtractService attachmentExtractService = new FormAttachmentExtractService();
                     userAttachedFormsList.addAll(attachmentExtractService.processPdfForm(userAttachedS2SForm));
                 if (userAttachedFormsList!= null && userAttachedFormsList.size()==0){
                    throw new Exception();
                }
                 }
                 list = userAttachedS2STxnBean.saveUserS2SForm(list);
                 responder.setDataObject(list);
                 responder.setResponseStatus(true);
                 break;
            case TRANSLATE_USER_ATTACHED_S2S_FORM:
            	FormAttachmentExtractService attachmentExtractService = new FormAttachmentExtractService();
            	List formList = (List)requester.getDataObject();
            	if(formList.isEmpty()) throw new CoeusException("Please select a form");
            	UserAttachedS2SFormBean userAttachedS2SForm = null;
            	List userAttachedForms = new ArrayList();
            	for (int i = 0; i < formList.size(); i++) {
            		userAttachedS2SForm = (UserAttachedS2SFormBean)formList.get(i);
            		if(userAttachedS2SForm.getAcType().equals(TypeConstants.DELETE_RECORD)){
            			userAttachedForms.add(userAttachedS2SForm);
            		}
				}
                
                if(userAttachedS2SForm.getUserAttachedS2SPDF()==null || userAttachedS2SForm.getUserAttachedS2SPDF().length==0){
                	ByteArrayOutputStream pdfByteArray = null;
	                try{
	                	pdfByteArray = userAttachedS2STxnBean.getPDF(userAttachedS2SForm.getProposalNumber(), userAttachedS2SForm.getUserAttachedFormNumber());
	                	if(pdfByteArray==null) throw new CoeusException("Not able to get the pdf form");
	                	byte[] pdfBytes = pdfByteArray.toByteArray();
	                	userAttachedS2SForm.setUserAttachedS2SPDF(pdfBytes);
	                }finally{
	                	if(pdfByteArray!=null) pdfByteArray.close();
	                	pdfByteArray=null;
	                }
                }
                userAttachedForms.addAll(attachmentExtractService.processPdfForm(userAttachedS2SForm));
//            	userAttachedForms.add(userAttachedS2SForm);
                if (userAttachedForms!= null && userAttachedForms.size()==0){
                    throw new Exception();
                }
                List savedList = userAttachedS2STxnBean.saveUserS2SForm(userAttachedForms);
                List innerList = savedList.isEmpty()?new ArrayList():(List)savedList.get(0);
                for (int i = 0; i < innerList.size(); i++) {
                	UserAttachedS2SFormBean savedAttachedS2SForm = (UserAttachedS2SFormBean)innerList.get(i);
                	savedAttachedS2SForm.setAcType(null);
				}
                responder.setDataObject(savedList);
                break;     
            }
            responder.setResponseStatus(true);
        }catch (UniqueSchemaNotFoundException uEx) {
            responder.setResponseStatus(false);
            responder.setMessage(uEx.getMessage());
            responder.setException(uEx);
            //Not logging this exception bcoz, This is being thrown for letting 
            //system know that there is no schema selected for the proposal
            //This has been handled at GUI
        } catch (S2SValidationException sEx) {
            responder.setResponseStatus(false);
            responder.setMessage(sEx.getMessage());
            responder.setException(sEx);
            UtilFactory.log( sEx.getMessage(), sEx,
                "S2SServlet", "processRequest");
        } catch (InvalidXmlException ex) {
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( ex.getMessage(), ex,
                "S2SServlet", "processRequest");
        }catch( CoeusException coeusEx ) {
            int index=0;
            
            String errMsg= coeusMessageResourcesBean.parseMessageKey(coeusEx.getUserMessage());
            
            //added by eleanor
            CoeusMessageResourcesBean coeusMessageResourcesBean =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            //end addition
            
            responder.setResponseStatus(false);
            //print the error message at client side
            //eleanor change start
            responder.setMessage(errMsg);
            //responder.setMessage(coeusEx.getMessage());
            //eleanor change end
            UtilFactory.log( errMsg, coeusEx, "S2SServlet",
            "processRequest");
            
        }catch( DBException dbEx ) {

            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            coeusMessageResourcesBean
                = new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);

            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                "S2SServlet", "processRequest");

        }catch(AxisFault ex){
            UtilFactory.log(ex.getMessage(),ex,"S2SServlet", "processRequest");
//            ex.printStackTrace();
            String faultStr = ex.getFaultReason();
            
            String errMsg= new CoeusMessageResourcesBean().parseMessageKey("exceptionCode.90012");
            responder.setResponseStatus(false);
            //print the error message at client side
            String msg = faultStr.indexOf("ConnectException")==-1?faultStr:errMsg;
            responder.setMessage(msg);
        }catch(Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                "S2SServlet", "processRequest");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "S2SServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                // send the object to applet
                outputToApplet
                = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                "S2SServlet", "processRequest");
            }
        }
        
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return coeusMessageResourcesBean.parseMessageKey("exceptionCode.90004");
    }
    private void configureSSL() throws ServletException{
        try{
            String path= getServletContext().getRealPath("/").replace( '\\', '/');
            String soapServerPropertyFile =path+"/"+"WEB-INF"+"/"+SOAP_SERVER_PROPERTY_FILE;
            SoapUtils.setSoapServerPropFile( soapServerPropertyFile );
            System.setProperty("javax.net.ssl.keyStore", SoapUtils
            .getProperty("javax.net.ssl.keyStore"));
            System.setProperty("javax.net.ssl.keyStorePassword", SoapUtils
            .getProperty("javax.net.ssl.keyStorePassword"));
            System.setProperty("javax.net.ssl.trustStore", SoapUtils
            .getProperty("javax.net.ssl.trustStore"));
            System.setProperty("javax.net.ssl.trustStorePassword", SoapUtils
            .getProperty("javax.net.ssl.trustStorePassword"));
        }catch(IOException ioEx){
            UtilFactory.log(ioEx.getMessage(),ioEx,"S2SServlet","ConfigureSSL");
            throw new ServletException(ioEx.getMessage());
        }
    }

    private void configureSoap() {
            String path= getServletContext().getRealPath("/").replace( '\\', '/');
            String soapServerPropertyFile =path+"/"+"WEB-INF"+"/"+SOAP_SERVER_PROPERTY_FILE;
            SoapUtils.setSoapServerPropFile( soapServerPropertyFile );
            java.lang.System.setProperty("javax.xml.soap.MessageFactory",
                            "org.apache.axis.soap.MessageFactoryImpl");

            java.lang.System.setProperty("javax.xml.soap.SOAPConnectionFactory",
                            "org.apache.axis.soap.SOAPConnectionFactoryImpl");

    }
    
}
