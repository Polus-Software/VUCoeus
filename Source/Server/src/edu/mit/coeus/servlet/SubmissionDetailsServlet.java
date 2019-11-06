/*
 * SubmissionDetailsServlet.java
 *
 * Created on March 27, 2003, 12:47 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.utils.UtilFactory;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;
import edu.mit.coeus.irb.bean.* ;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.irb.bean.ActionTransaction;
import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;

public class SubmissionDetailsServlet extends CoeusBaseServlet
{
    
    //Right Ids
    private final String MAINTAIN_PROTOCOL_SUBMISSIONS = "MAINTAIN_PROTOCOL_SUBMISSIONS"; 
    
    //Protocol Enhancment - Action Comments Editable(Rights Checking) Start 1
    private final String ADMINISTRATIVE_CORRECTION = "ADMINISTRATIVE_CORRECTION";
    private static final char GET_DISCLOSURE_STATUS = '2';
    //Protocol Enhancment - Action Comments Editable(Rights Checking) End 1
   private static final char GET_NOTIFY_DETAILS = 'n';
   private static final char PROTOCOL_PERSON_SEND = 'p' ;
   private static final Integer PROTOCOL_SEND_NOTIFICATION_ACTION_CODE =603;
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy()
    {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException
    {
        ObjectInputStream iStream = null ; 
        ObjectOutputStream oStream = null ; 
        ResponderBean responder = null ;
        char functionType ;
        boolean isReleaseScheduleLock = false;
        String scheduleId = null;
       try
       {

       iStream = new ObjectInputStream(request.getInputStream()) ;
        RequesterBean requester = null;
        // the response object to applet
        responder = new ResponderBean();
        
            // read the serialized request object from applet
        requester = (RequesterBean) iStream.readObject();
        isValidRequest(requester);
        functionType = requester.getFunctionType() ;
        
        if (functionType == 'G' || functionType == 'X')
        {    
            SubmissionDetailsBean submissionDetailsBean = (SubmissionDetailsBean)requester.getDataObject() ; 

            if(submissionDetailsBean != null)
            {
              System.out.println("*** Servlet recvd a RequestTxnBean ***") ;
              SubmissionDetailsTxnBean txnBean = new SubmissionDetailsTxnBean() ;
			  Vector vecTotal = new Vector();
              Vector vecDetails = txnBean.getDataSubmissionDetails(submissionDetailsBean.getProtocolNumber()) ;
			  
              /*ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ;
              Vector vecReviewers = protocolSubmissionTxnBean.getProtocolReviewers(submissionDetailsBean.getProtocolNumber(),
                                                             submissionDetailsBean.getSequenceNumber().intValue(),
                                                             submissionDetailsBean.getSubmissionNumber().intValue()) ;
              */ 
			  // Added by Jobin to get the protocol actions
			  ProtocolDataTxnBean protocolTxnBean = new ProtocolDataTxnBean();
			  Vector vecActions = new Vector();
			  if(functionType == 'X') {
				  vecActions = protocolTxnBean.getProtocolActionsWithSubmission(submissionDetailsBean.getProtocolNumber(),
				  submissionDetailsBean.getSequenceNumber().intValue(),
				  submissionDetailsBean.getSubmissionNumber().intValue());
				  
			  } else {
				  vecActions = protocolTxnBean.getProtocolActions(submissionDetailsBean.getProtocolNumber(),
				  submissionDetailsBean.getSequenceNumber().intValue());
			  }
              if (vecDetails == null) 
              {
                System.out.println("*** submission details vector is null ***") ;
              }
              else
              {
				vecTotal.add(vecDetails);
                System.out.println("*** submission details vector is  not null ***") ;
                if (vecDetails.size() <=0)
                {
                    System.out.println("*** submission details vector is empty ***") ;
                }
                else
                {
                    System.out.println("*** submission details vector is not empty sixe = " + vecDetails.size() +" ***") ;
                }    
				if (vecActions != null) {
					vecTotal.add(vecActions);
				}

              }
              //submissionDetailsBean.setSubmissionDetails(vecDetails) ;
              //submissionDetailsBean.setSelectedReviewers(vecReviewers) ;
              responder.setDataObject(vecTotal);
              responder.setResponseStatus(true);
              responder.setMessage(null);
            }
            
          } // end 'G'   
        
        if (functionType == 'H')// retrieving based on the submission number
        {    
            SubmissionDetailsBean submissionDetailsBean = (SubmissionDetailsBean)requester.getDataObject() ; 

            if(submissionDetailsBean != null)
            {
              System.out.println("*** Servlet recvd a RequestTxnBean ***") ;
              SubmissionDetailsTxnBean txnBean = new SubmissionDetailsTxnBean() ;
              Vector vecDetails = txnBean.getDataSubmissionDetails(submissionDetailsBean.getProtocolNumber(), submissionDetailsBean.getSubmissionNumber() ) ;

              ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ;
              Vector vecReviewers = protocolSubmissionTxnBean.getProtocolReviewers(submissionDetailsBean.getProtocolNumber(),
                                                             submissionDetailsBean.getSequenceNumber().intValue(),
                                                             submissionDetailsBean.getSubmissionNumber().intValue()) ;

              if (vecDetails == null) 
              {
                System.out.println("*** submission details vector is null ***") ;
              }
              else
              {
                System.out.println("*** submission details vector is  not null ***") ;
                if (vecDetails.size() <=0)
                {
                    System.out.println("*** submission details vector is empty ***") ;
                }
                else
                {
                    System.out.println("*** submission details vector is not empty sixe = " + vecDetails.size() +" ***") ;
                }    

              }
              submissionDetailsBean.setSubmissionDetails(vecDetails) ;
              submissionDetailsBean.setSelectedReviewers(vecReviewers) ;
              responder.setDataObject(submissionDetailsBean);
              responder.setResponseStatus(true);
              responder.setMessage(null);
            }
            
          } // end 'H'   
        
        
        if (functionType == 'U' )
        {
              ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(requester.getUserName()) ;  
              //Added to get review comments - start
              //ProtocolSubmissionInfoBean submissionInfoBean = (ProtocolSubmissionInfoBean)requester.getDataObject() ;
              Vector dataObjects =  requester.getDataObjects();
              ProtocolSubmissionInfoBean submissionInfoBean = (ProtocolSubmissionInfoBean)dataObjects.elementAt(0);
              Boolean releaseScheduleLock = (Boolean)dataObjects.elementAt(1);
              isReleaseScheduleLock = releaseScheduleLock.booleanValue();
              scheduleId = submissionInfoBean.getScheduleId();
              
              //Added to get review comments - end
              
              //Commented By sharath for CheckList
              protocolSubmissionUpdateTxnBean.updProtocolSubmission(submissionInfoBean) ;
              
              //Case #2080 Start
              Vector vecData = (Vector)dataObjects.elementAt(2);
              if(vecData != null && vecData.size() > 0){
                  ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(requester.getUserName());
                  for (int index = 0 ; index < vecData.size() ; index++){
                      ProtocolActionsBean protocolActionsBean =
                      (ProtocolActionsBean)vecData.get(index);
                      if(protocolActionsBean.getAcType() != null &&
                      protocolActionsBean.getAcType().equals(TypeConstants.UPDATE_RECORD)){
                          protocolUpdateTxnBean.updProtoActionComments(protocolActionsBean);
                      }
                  }
              }
              //Case #2080 End
              
              //Added By sharath for CheckList - START
              //get the protocol Info Bean from the RequesterObject.
              /*ProtocolInfoBean protocolInfoBean =
                (ProtocolInfoBean)requester.getDataObjects().elementAt(0);
              
              protocolSubmissionUpdateTxnBean.addUpdProtocolSubmission(
                                                submissionInfoBean,
                                               protocolInfoBean);
               */
              //Added By sharath for CheckList - END
              
              ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
              submissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(submissionInfoBean.getProtocolNumber());
              responder.setDataObject(submissionInfoBean);
              responder.setResponseStatus(true);
              responder.setMessage(null);
              
            }
            
        
        if (functionType == 'C' ) // get default commitee list
        {
              ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean() ;
              responder.setDataObject(protocolSubmissionTxnBean.getMatchingCommitteeForProtocol(requester.getId()));
              responder.setResponseStatus(true);
              responder.setMessage(null);
        }
        
        if (functionType == 'R' ) // to check if user has rights to modify submission
        {
              SubmissionDetailsTxnBean txnBean = new SubmissionDetailsTxnBean() ;
              // get the user
              UserInfoBean userBean = (UserInfoBean)new
              UserDetailsBean().getUserInfo(requester.getUserName());
              String loggedInUser = userBean.getUserId();
              String unitNumber = null ;
              
              //Added on 14/10/2003 - start
              //responder.setResponseStatus(txnBean.checkUserCanModifysubmission(loggedInUser));
              //responder.setMessage(null);              
              boolean isAuthorised;
              ProtocolSubmissionInfoBean checkSubmissionBean = (ProtocolSubmissionInfoBean)requester.getDataObject() ;
//              isAuthorised = txnBean.checkUserCanModifysubmission(checkSubmissionBean) ;
//              if(isAuthorised){
                  //Before performing action check whether User has rights to perform Action.
                  UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                     //prps code start feb 6 2004
                    if (checkSubmissionBean.getCommitteeId() != null 
                    && !checkSubmissionBean.getCommitteeId().equals(""))
                    {
                            
                            CommitteeTxnBean committeeTxnBean  = new CommitteeTxnBean(loggedInUser);
                            CommitteeMaintenanceFormBean beanHomeUnit = committeeTxnBean.getCommitteeDetails(checkSubmissionBean.getCommitteeId()) ;
                            unitNumber = beanHomeUnit.getUnitNumber() ;
                        
                    }    
                    else  if (checkSubmissionBean.getScheduleId()!= null
                    && !checkSubmissionBean.getScheduleId().equals(""))
                    {
                            ScheduleTxnBean scheduleTxnBeanUnit = new ScheduleTxnBean(loggedInUser);
                            ScheduleDetailsBean beanHomeUnit 
                            = scheduleTxnBeanUnit.getScheduleDetails(checkSubmissionBean.getScheduleId()) ;
                            unitNumber = beanHomeUnit.getHomeUnitNumber() ;
                    }    
                    else
                    {
                        //Protocol Enhancment - Action Comments Editable(Rights Checking) Start 2
                        //unitNumber = "000001" ;
                        ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                        //Modified for COEUSQA-2314 : IRB Admin should have ability to assign committee based on lead unit of the protocol - Start
//                        leadUnit = protocolDataTxnBean.getLeadUnitForProtocol(
//                            checkSubmissionBean.getProtocolNumber(), checkSubmissionBean.getSequenceNumber());
//                        unitNumber = txnData.getCampusForDept(leadUnit);
                        unitNumber = protocolDataTxnBean.getLeadUnitForProtocol(
                            checkSubmissionBean.getProtocolNumber(), checkSubmissionBean.getSequenceNumber());
                        //COEUSQA-2314 : End
                        //Protocol Enhancment - Action Comments Editable(Rights Checking) End 2
                    }    
                    //prps code end feb 6 2004
                  
                  //Protocol Enhancment - Action Comments Editable(Rights Checking) Start 3
                  //isAuthorised = txnData.getUserHasRight(loggedInUser, MAINTAIN_PROTOCOL_SUBMISSIONS, unitNumber);
                  
                  if(checkSubmissionBean.getActionId() == 113){
                      isAuthorised = txnData.getUserHasRight(loggedInUser, ADMINISTRATIVE_CORRECTION, unitNumber);
                     
                     //Check if authorised to perform Administrative Correction
                     if( isAuthorised ){
                         ActionTransaction actionTxn = new ActionTransaction(loggedInUser);
                         int exceptionId = actionTxn.performAdministrativeCorrection(checkSubmissionBean.getProtocolNumber());
                         if( exceptionId == 1 ){
                             isAuthorised = true;
                         }else {
                             isAuthorised = false;
                         }
                     }//End if isAuthorised
                  }else{
                      isAuthorised = txnData.getUserHasRight(loggedInUser, MAINTAIN_PROTOCOL_SUBMISSIONS, unitNumber);
                  }
                   //Protocol Enhancment - Action Comments Editable(Rights Checking) End 3
                  
                  //Modified on 16/10/2003 - should be unit level right - end
                  if(!isAuthorised){
                    //No Action Rights message
                    responder.setResponseStatus(false);
                    responder.setMessage(null);
                    //Commented on 15/10/2003 - start
                    /*CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "submissionAuthorization_exceptionCode.3411");
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);*/                     
                    //Commented on 15/10/2003 - end
                  }else{
                      isAuthorised = txnBean.checkUserCanModifysubmission(checkSubmissionBean) ;
                      if(isAuthorised){
                      responder.setResponseStatus(true);
                      responder.setMessage(null);
                      }else{
                          responder.setResponseStatus(false);
                          responder.setMessage(null);
                  }                  
                }
//                  else{
//                    responder.setResponseStatus(false);
//                    responder.setMessage(null);                     
//              }
              //Added on 14/10/2003 - end
        }
        //Added for case #1264 start 1
        if (functionType == 'A' )
        {
              ProtocolSubmissionInfoBean submissionDetailsBean = (ProtocolSubmissionInfoBean)requester.getDataObject();
              String protocolNo = submissionDetailsBean.getProtocolNumber();
              int seqNo = submissionDetailsBean.getSequenceNumber();
              int subNo = submissionDetailsBean.getSubmissionNumber();
              ProtocolDataTxnBean protocolTxnBean = new ProtocolDataTxnBean();
              Vector vecActions = protocolTxnBean.getProtocolActionsWithSubmission(protocolNo,seqNo, subNo); 
              responder.setDataObject(vecActions);
              responder.setResponseStatus(true);
              responder.setMessage(null);
        }
        //Added for case #1264 end 1
        //Added for Case#4354 - When ENABLE_PROTOCOL_TO_DEV_PROPOSAL is set - Start
        if(functionType == 'c'){
            SubmissionDetailsTxnBean txnBean = new SubmissionDetailsTxnBean();
            String protocolNumber = requester.getId();
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = txnBean.getProtocolSubmissionDetails(protocolNumber);
            responder.setDataObject(protocolSubmissionInfoBean);
            responder.setResponseStatus(true);
            responder.setMessage(null);
        }
       //Case#4354 - End

//code added for displaying DisclosureStatusForm to show the protocol disclosure status-starts
        else if(functionType == GET_DISCLOSURE_STATUS){
               String protocolNumber = (String)requester.getDataObject();
               Vector dataObjects = new Vector();
               Vector vctDataObjects = new Vector();
               ProtocolDataTxnBean protocolDataTxnBean=new ProtocolDataTxnBean();
               dataObjects=protocolDataTxnBean.getDisclosureStatusDetails(protocolNumber);
               vctDataObjects.addElement(dataObjects);
               responder.setDataObjects(vctDataObjects);
               responder.setResponseStatus(true);
               responder.setMessage(null);
            }
//code added for displaying DisclosureStatusForm to show the protocol disclosure status--ends
        
//code added to send coi notification to protocol persons..start
else if(functionType == GET_NOTIFY_DETAILS){
               String propNumber = (String)requester.getDataObject();
               Vector vctDataObjects = new Vector();
               Vector dataObjects = new Vector();
               Vector personid=new Vector();
                ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
               dataObjects=submissionTxnBean.getNotificationPersonsDetails(propNumber);
               if(dataObjects.size()>0){
               personid=(Vector)dataObjects.lastElement();
               dataObjects.removeElementAt(dataObjects.size()-1);
               }
               vctDataObjects.addElement(dataObjects);
               vctDataObjects.addElement(personid);
                responder.setDataObjects(vctDataObjects);
               responder.setResponseStatus(true);
               responder.setMessage(null);
            }
             else if(functionType == PROTOCOL_PERSON_SEND){
               boolean mailSent=true;
               int mailCount=0;
               String protocolnumber="";
               Vector toPersonList;
               ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean();
               if(requester.getDataObjects()!=null){
               toPersonList=(Vector) requester.getDataObjects().get(0);
               protocolnumber=(String) requester.getDataObjects().get(1);
               HashMap propDetails=submissionTxnBean.getProtoPersonDetailsForMail(protocolnumber);
               Vector propPersonList=submissionTxnBean.getPropPersonDetailsForNotif(protocolnumber);
               
               //prepare the mail details
               if(toPersonList!=null){
            String url=null;
            String coiUrl=null;
            String protocolNumberData="Protocol Number : "+protocolnumber;
            String piData = "PI :" +propDetails.get("PI_NAME").toString();
            String unitData = "Lead Unit :"+propDetails.get("UNIT_NUMBER").toString() +":"+propDetails.get("UNIT_NAME").toString();
            String applicationDate = "Application Date :"+propDetails.get("APPLICATION_DATE").toString();
            String approvalDate = "Approval Date :"+((propDetails.get("APPROVAL_DATE")==null)? " ":propDetails.get("APPROVAL_DATE").toString());
            String title = "Title :"+propDetails.get("TITLE").toString();
            String protocolType = "Type :"+propDetails.get("PROTOCOL_TYPE_DESC").toString();
            String protocolStatus = "Protocol Status :"+propDetails.get("PROTOCOL_STATUS_DESC").toString();
            String personName = "Person Name :";
            String personRole = "Person Role/Affiliation :";
            
            Vector vecRecipientsdata=new Vector();
            PersonRecipientBean personRecipientBean=new PersonRecipientBean();
            vecRecipientsdata.add(personRecipientBean);
            String personId=null;
            String emailId=null;
            url= CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"getProtocolData.do?PAGE=G&FROM_EMAIL=true&protocolNumber="+protocolnumber+"&sequenceNumber="+propDetails.get("SEQUENCE_NUMBER").toString();
            coiUrl= CoeusProperties.getProperty(CoeusPropertyKeys.APP_HOME_URL)+"coi.do?protocolNumber="+protocolnumber+"&personId=";
            MailHandler mailhandler =new MailHandler();   
            
            
                for(int i=0;i<toPersonList.size();i++){
                      if(toPersonList.get(i)!=null){
                          personId=toPersonList.get(i).toString();
                          for(int k = 0;  k< propPersonList.size(); k++) {
                            HashMap persondet = (HashMap)propPersonList.get(k);
                            if(persondet.get("EMAIL_ADDRESS")!=null){
                                emailId=persondet.get("EMAIL_ADDRESS").toString();
                                if(personId.equals(persondet.get("PERSON_ID").toString())){
                                    personRecipientBean.setEmailId(emailId);
                                    personRecipientBean.setPersonId(personId);
                                    personRecipientBean.setPersonName(persondet.get("PERSON_NAME").toString());
                                    MailMessageInfoBean mailMsgInfoBean = mailhandler.getNotification(ModuleConstants.PROTOCOL_MODULE_CODE, PROTOCOL_SEND_NOTIFICATION_ACTION_CODE);
                                      if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                            mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                                            //mailMsgInfoBean.setSubject("Notification");
                                            mailMsgInfoBean.appendMessage(piData, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(unitData, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(personName+persondet.get("PERSON_NAME").toString(), "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(personRole+persondet.get("PERSON_ROLE")+"/"+persondet.get("AFFILIATION"), "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolNumberData, "\n") ;
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(applicationDate, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(approvalDate, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(title, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolType, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage(protocolStatus, "\n");
                                             mailMsgInfoBean.appendMessage("", "\n");
                                            mailMsgInfoBean.appendMessage("You have been named as "+persondet.get("PERSON_ROLE")+ " for the above referenced project.", "\n\n") ;
                                            mailMsgInfoBean.setCoiUrl(coiUrl+personId);
                                            mailMsgInfoBean.setUrl(url);
                                            mailSent = mailhandler.sendMail(ModuleConstants.PROTOCOL_MODULE_CODE, PROTOCOL_SEND_NOTIFICATION_ACTION_CODE, mailMsgInfoBean);
                                            String selModule = "";
                                            if((persondet.get("PERSON_ROLE")!=null)&&((persondet.get("PERSON_ROLE").toString()).equalsIgnoreCase("Investigator"))) {
                                                selModule = "PI";
                                            }
                                            else {
                                                selModule = "KP";
                                                }
                                            submissionTxnBean.updateLastNotificationDate(personId,protocolnumber,selModule);
                                            mailCount++;
                                            }
                                      }
                                
                                }
                            }//end of for loop
                  
                    }
                
                
            }
               
               responder.setResponseStatus(mailSent);
               responder.setDataObject(mailCount);
               }
               }

                  
            }
//code added to send coi notification to protocol persons..end
        

       }catch(Exception ex)
       {
           UtilFactory.log( ex.getMessage(), ex, "SubmissionDetailsServlet", "doPost");
           responder.setResponseStatus(false);
           responder.setMessage(null);
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "SubmissionDetailsServlet", "doPost");
        //Case 3193 - END
       }finally {
            try{
                
                //Release Schedule Lock - start
                if(isReleaseScheduleLock){
                    if(scheduleId!=null && !scheduleId.equalsIgnoreCase("9999999999")){
                        ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();                        
                        scheduleMaintenanceTxnBean.releaseEdit(scheduleId);
                    }                    
                }
                //Release Schedule Lock - end
                    
                // send the object to applet
                oStream
                = new ObjectOutputStream(response.getOutputStream());
                oStream.writeObject(responder);
                // close the streams
                if (iStream!=null){
                    iStream.close();
                }
                if (oStream!=null){
                    oStream.flush();
                    oStream.close();
                }
            }catch (IOException ioe){
                //ioe.printStackTrace() ;
                UtilFactory.log( ioe.getMessage(), ioe, "SubmissionDetailsServlet", "doPost");
            }
        }
       
        
    }

        
        
        

    
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException
    {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException
    {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }
    
}
