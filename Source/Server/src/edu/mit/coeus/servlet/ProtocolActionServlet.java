/*
 * @(#)ProtocolSubmissionServlet.java 1.0 11/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException; 
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.report.exception.CoeusReportException;
import edu.mit.coeus.irb.bean.ActionTransaction ;
//import edu.mit.coeus.report.bean.ProcessReportXMLBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.pdf.generator.XMLPDFTxnBean;

//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse; 
//import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector; 
//import java.util.Hashtable;
import java.util.Properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
//import edu.mit.coeus.utils.xml.generator.XMLStreamGenerator;
import edu.mit.coeus.utils.pdf.generator.XMLtoPDFConvertor;
import org.w3c.dom.Document;

/**
 * This servlet is a controller that retrieves data for 'Protocol Submission'
 * module. It receives the serialized object bean called 'Requester Bean'
 * from the applet and performs accordingly, the response from the TransactionBean
 * is set serialized object called 'Responder Bean' from the servlet.
 * the object received from the ProtocolSubmissionForm for insert/modify is 
 * received by the "this" servlet passed to ProtocolSubmissionUpdateTxnBean 
 * if the transactions fails the bean throws the exception to the servlet 
 * and servlet in turn finds what type exception and pass
 * the exception message to the ProtocolSubmissionForm.
 * It connects the DBEngine and executes the stored procedures or queries via
 * 'ProtocolSubmissionTxnBean and ProtocolSubmissionUpdateTxnBean'.
 *
 * @version :1.0 November 25, 2002, 2:26 PM
 * @author Mukundan.C
 *
 */
public class ProtocolActionServlet extends CoeusBaseServlet {
    //holds the char for add 
    private final static char ADD_MODE = 'A';
    //holds the char for modify 
    private final static char MODIFY_MODE = 'M';
    //holds the char for schedule selection
    private final static char SCHEDULE_SELECTION = 'V';
    //holds the char for reviewer selection
    private final static char REVIEWER_SELECTION = 'R';
    //holds the char for protocol submitted 
    private final static char PROTO_SUB_COUNT = 'C';
    //holds the char for display
    private final static char DISPLAY_MODE = 'D';
    //holds the char for save
    private final static char SAVE_MODE = 'S';
    //holds the char for check minute for protocol
    private final static char CHECK_PROTOCOL_MIN = 'P';
    
    private final static char ACTION_MENU_ITEM = 'X' ;
    private final static char ACTION_MENU_ITEM_TAGS = 'x' ;
    private final static char GET_CUSTOM_TAGS = 't' ;
    private final static char GET_CUSTOM_TAGS_REGENERATE = 'y';
    private final static char REGENERATE_CORRESPONDENCE_WITH_TAGS ='Y';
    
    //prps new code start
    private final static char VALID_STATUS_CHANGE = 'T' ; 
    //prps new code ends
    
    //Check if authorised to perform action - start
    private final static char CHECK_AUTHORISATION = 'H' ; 
    //end
    
    // prps start - nov 13 2003
    private final static char REGENERATE_CORRESPONDENCE = 'G' ;
    // prps end - nov 13 2003
    
    // prps start - dec 15 2003
    private final static char GET_FOLLOWUP_ACTIONS = 'F' ;
    // prps end - dec 15 2003
    
    // For Undo Last Action -- Added by Jobin 
	private final static char UNDO_LAST_ACTION = 'U';
	//end
     
    //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - start
    private final static char UNDO_LAST_ACTION_CONFIRMATION = 'u';
    private static final String DIALOG_TYPE_POP_UP_FOR_ACTION = "ConfirmationMessage"; 
    //Added for COEUSQA-3144 - end
	
    private final static String ACTION_RIGHT = "PERFORM_IRB_ACTIONS_ON_PROTO";
    
    // to check null String
    //private UtilFactory UtilFactory = null;
    
    //private String userId;
    
    //For the IRB Enhancement ,Assugning to agenda as the follow up action to submitted to IRB Step:1
    private final static char GET_SUBMISSION_BEAN = 'b';
    //Added for case #1961 start 1
    private final static char GET_ACTIONS_DATA = 'a';
    //Added for case #1961 start 2
    //End IRB Enhancement step:1
    private static final String EMPTY_STRING = "";
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException 
    {   
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        String userId = EMPTY_STRING;
        CoeusConstants.SERVER_HOME_PATH = this.getServletContext().getRealPath("/");
        
        try
        {   // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);           
            ProtocolActionsBean actionBean = null ;
            int actionCode = 0 ;
			
			String loggedinUser = requester.getUserName();
            
            // get the loggin user
            UserInfoBean userBean = (UserInfoBean)new 
                 UserDetailsBean().getUserInfo(requester.getUserName());
            
            //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
            //this.userId = requester.getUserName();
            userId = requester.getUserName();
            //Case#4585 - End
            
            //Get Unit Number
            String unitNumber = userBean.getUnitNumber();
            
            // all the action menu items will have the functiontype 
            // only submit to IRB which will be a follow up action will not have X
            if ( requester.getFunctionType() == ACTION_MENU_ITEM)
            {
              Vector dataObjects = requester.getDataObjects();  

              actionBean = (ProtocolActionsBean)dataObjects.elementAt(0);
              actionCode = actionBean.getActionTypeCode();
              
                // call ur actions here
                if (actionCode > 101) // all the actions do the same thing here, in the transaction bean it will change 
                {
                    //If Assigned to Schdule action - start
                    /*if(actionCode == 109){
                        responder = performAction(actionCode, dataObjects, userBean);
                    }else{
                        responder = performAction (actionCode, requester, userBean) ;
                    }*/
                    responder = performAction (actionCode, requester, userBean) ;
                    //If Assigned to Schdule action - end
                }    
                else // "Submit_To_IRB"
                {
    //               System.out.println(" \n **** IN servlet action code as 2 *** \n" ) ;
                    actionCode = 101 ;
                    responder = submitToIrbAction (actionCode, requester, userBean) ;
                }

              
            } 
            else if (requester.getFunctionType() == ACTION_MENU_ITEM_TAGS) {
                Vector dataObjects = requester.getDataObjects();  

              actionBean = (ProtocolActionsBean)dataObjects.elementAt(0);
              actionCode = actionBean.getActionTypeCode();
              
                // call ur actions here
                if (actionCode > 101) // all the actions do the same thing here, in the transaction bean it will change 
                {
                    //If Assigned to Schdule action - start
                    /*if(actionCode == 109){
                        responder = performAction(actionCode, dataObjects, userBean);
                    }else{
                        responder = performAction (actionCode, requester, userBean) ;
                    }*/
                    responder = performActionForTags(actionCode, requester, userBean) ;
                    //If Assigned to Schdule action - end
                }    
                else // "Submit_To_IRB"
                {
    //               System.out.println(" \n **** IN servlet action code as 2 *** \n" ) ;
                    actionCode = 101 ;
                    responder = submitToIrbAction (actionCode, requester, userBean) ;
                }

            }
            else if (requester.getFunctionType() == GET_CUSTOM_TAGS) { //Added by Bijosh to get the XSL data
                Vector dataObjects = requester.getDataObjects();
                actionBean = (ProtocolActionsBean)dataObjects.elementAt(0);
                actionCode = actionBean.getActionTypeCode();
                //ProtocolActionsBean actionBean = null;
                actionBean = (ProtocolActionsBean) dataObjects.elementAt(0);
                CorrespondenceTypeFormBean correpTypeFormBean = null;
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
                ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
                //Case#4585 - End
                Vector validActionCorrespTypes = null;
                validActionCorrespTypes = protoCorrespTypeTxnBean.getValidProtoActionCorrespTypes(actionBean);
                //For case#2004 start 1
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean
                                            = new ProtocolSubmissionInfoBean();
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean 
                        = new ProtocolSubmissionTxnBean();
                protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                                                    actionBean.getProtocolNumber());
                if(protocolSubmissionInfoBean!=null){
                   actionBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                   //Modified for the case#coeusdev219 - send functionality for correspondence
                    actionBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId());
                }
                    
                //For case#2004 end 1
                
                
                byte[] templateFileBytes=null;
                File reportFile = null;
                FileOutputStream fos = null;
                //        ProcessReportXMLBean bean = null;
                Vector vctReceipients = null;
                Vector vctRecp = null;
                ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
                Vector attachmentFilePath = new Vector();
                if(validActionCorrespTypes != null && validActionCorrespTypes.size() > 0){
                    for(int row = 0; row < validActionCorrespTypes.size(); row++ ){
                        correpTypeFormBean = (CorrespondenceTypeFormBean)validActionCorrespTypes.elementAt(row);
                        //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-start
                        templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), actionBean.getScheduleId(),actionBean.getCommitteeId());
                        //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-end
                    }

                InputStream is = getClass().getResourceAsStream("/coeus.properties");
                Properties coeusProps = new Properties();
                is = getClass().getResourceAsStream("/coeus.properties");
                coeusProps.load(is);
                String startingTag = coeusProps.getProperty("STARTING_CUSTOM_TAG");
                String endingTag = coeusProps.getProperty("ENDING_CUSTOM_TAG");

                XMLPDFTxnBean xmlPDFTxnBean = new XMLPDFTxnBean() ;
                int correspCode =  correpTypeFormBean.getProtoCorrespTypeCode();
                // IACUC Parent case, Correpsondence related changes
//                String desc = xmlPDFTxnBean.getProtocolcorrespondenceDesc(correspCode);
                String desc = xmlPDFTxnBean.getIRBProtocolcorrespondenceDesc(correspCode);
                Vector dataVector = new Vector();
                dataVector.add(templateFileBytes);
                dataVector.add(startingTag);
                dataVector.add(endingTag);
                dataVector.add(desc);
                responder.setDataObject(dataVector);
                responder.setResponseStatus(true);
                } else {
                    responder.setResponseStatus(false);
                    responder.setMessage("NO_TAGS");
                }

            }
            
            else if (requester.getFunctionType() == VALID_STATUS_CHANGE) 
            { // this will make sure that the action requested by the user on a paricular protocol or schedule
              // is valid one
               responder = performStatusChangeValidation (requester, userBean) ;
            }
            else if (requester.getFunctionType() == SAVE_MODE) 
            { 
                actionCode = 101 ;
                responder = submitToIrbAction (actionCode, requester, userBean) ;
            }else if(requester.getFunctionType() == CHECK_AUTHORISATION){
                //Check authorisation for action menus involving multiple actions
                //Eg: Committee Action
                //prps start feb 5 2004
                responder.setResponseStatus(true);
                responder.setMessage(null);
                //prps end feb 5 2004
 //prps commented this on feb 5 2004  //////             
//////                
//////                actionBean = (ProtocolActionsBean)requester.getDataObject() ;    
//////                String strActionDescription = actionBean.getActionTypeDescription();
//////                
//////                if(strActionDescription!=null){
//////                    strActionDescription = " " +strActionDescription.trim() + " action";
//////                }else{
//////                    strActionDescription = " this action";
//////                }
//////                
//////                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
//////                boolean isAuthorised = txnData.getUserHasRight(userBean.getUserId(), ACTION_RIGHT, unitNumber);
//////                if(isAuthorised){
//////                    responder.setResponseStatus(true);
//////                    responder.setMessage(null);
//////                }else{
//////                    System.out.println("Inside validation message");
//////                    //No Action Rights message
//////                    responder.setResponseStatus(false);
//////                    CoeusMessageResourcesBean coeusMessageResourcesBean 
//////                        =new CoeusMessageResourcesBean();
//////                    String errMsg = 
//////                    coeusMessageResourcesBean.parseMessageKey(
//////                                "protocolAction_exceptionCode." + 2200);
//////                    //Add action Description as well 
//////                    //responder.setMessage(errMsg + strActionDescription);                             
//////                    
//////                    //Sending exception to client side. - Prasanna
//////                    responder.setMessage(errMsg);
//////                    CoeusException ex = new CoeusException(errMsg,1);
//////                    System.out.println("Exception :"+ex);
//////                    responder.setDataObject(ex);
//////                    //end
//////                }
            }// end if CHECK_AUTHORISATION
            // prps start - nov 13 2003
            else if (requester.getFunctionType() == GET_CUSTOM_TAGS_REGENERATE) {
                actionBean = (ProtocolActionsBean)requester.getDataObject();  
                int protoCorrespTypeCode = Integer.parseInt(requester.getId().toString()) ;
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //byte[] templates =getTemplatesForRegenerate(actionBean, protoCorrespTypeCode);
		         byte[] templates =getTemplatesForRegenerate(userId,actionBean, protoCorrespTypeCode);
                //Case#4585 - End
                responder.setResponseStatus(true);
                Vector vecData = new Vector();
                InputStream is = getClass().getResourceAsStream("/coeus.properties");
                Properties coeusProps = new Properties();
                int correspCode = Integer.parseInt(requester.getId().toString()) ;
                XMLPDFTxnBean xmlPDFTxnBean = new XMLPDFTxnBean() ;
                // IACUC Parent case, Correpsondence related changes
//                String desc = xmlPDFTxnBean.getProtocolcorrespondenceDesc(correspCode);
                String desc = xmlPDFTxnBean.getIRBProtocolcorrespondenceDesc(correspCode);
                is = getClass().getResourceAsStream("/coeus.properties");
                coeusProps.load(is);
                String startingTag = coeusProps.getProperty("STARTING_CUSTOM_TAG");
                String endingTag = coeusProps.getProperty("ENDING_CUSTOM_TAG");
                vecData.add(0,templates);
                vecData.add(1,startingTag);
                vecData.add(2,endingTag);
                vecData.add(3,desc);
                responder.setDataObjects(vecData);
            } 
            else if (requester.getFunctionType() == REGENERATE_CORRESPONDENCE_WITH_TAGS) {
                Vector vecDataFromClient = requester.getDataObjects();
                actionBean = (ProtocolActionsBean)vecDataFromClient.get(0);
                byte [] templates = (byte [])vecDataFromClient.get(1);
                actionBean = (ProtocolActionsBean)requester.getDataObject();
                int protoCorrespTypeCode = Integer.parseInt(requester.getId().toString()) ;
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //if (regenerateCorrespondencesWithTags(actionBean, protoCorrespTypeCode,templates)) {
                if (regenerateCorrespondencesWithTags(userId,actionBean, protoCorrespTypeCode,templates)) {//Case#4585 - End
                    responder.setResponseStatus(true) ;
                }
                responder.setResponseStatus(true);
            }
            else if (requester.getFunctionType() == REGENERATE_CORRESPONDENCE) {
                actionBean = (ProtocolActionsBean)requester.getDataObject();  
                int protoCorrespTypeCode = Integer.parseInt(requester.getId().toString()) ;
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //if (regenerateCorrespondences(actionBean, protoCorrespTypeCode))
                if (regenerateCorrespondences(userId,actionBean, protoCorrespTypeCode))//Case#4585 - End
                {
                    responder.setResponseStatus(true) ;
                }    
            }
            // prps end nov 13 2003
            
            //prps start dec 15 2003
            else if (requester.getFunctionType() == GET_FOLLOWUP_ACTIONS)
            {
                actionCode = Integer.parseInt(requester.getId()) ;
              
                ActionTransaction actionTxn = new ActionTransaction(actionCode) ; 
                Vector vecActionDetails = new Vector() ;
                vecActionDetails.add(actionTxn.getSubAction());
                vecActionDetails.add(actionTxn.getHashUserPrompt());
                vecActionDetails.add(actionTxn.getHashUserPromptFlag());
                responder.setResponseStatus(true);
                responder.setDataObject(vecActionDetails) ;
            }    
            
            //prps end dec 15 2003
			
			else if (requester.getFunctionType() == UNDO_LAST_ACTION) { // Added by Jobin - start
					String protocolId = requester.getId();
					ActionTransaction axnTxn = new ActionTransaction();
					ProtocolInfoBean protocolInfoBean; 
					ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
					//check whether the undo action can be done or not.
					int canUndo = axnTxn.canUndoLastAction(protocolId, loggedinUser);
					// if success then do the undo action else display the corresponding message
					if (canUndo == 1) {
						int success = axnTxn.undoLastAction(protocolId, loggedinUser);
						if (success == 1) {
							//getting the new details
							protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolId);
							responder.setDataObject(protocolInfoBean);
						}
						responder.setResponseStatus(true);
                        responder.setMessage(null);
					} else {
						CoeusMessageResourcesBean coeusMessageResourcesBean 
                                            =new CoeusMessageResourcesBean();
                        String errMsg = coeusMessageResourcesBean.parseMessageKey(
                                                            "undoLastAction_exceptionCode." + canUndo);
						//print the error message at client side
                        responder.setMessage(errMsg);
                        
                        //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - start
                        if ( (canUndo == 4091) || (canUndo == 4092) || (canUndo == 4093) ) {
                            
                            responder.setDataObject(DIALOG_TYPE_POP_UP_FOR_ACTION);
                        }
                        //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - end

					}
                                //For the IRB Enhancement Assigning to agenda as the follow up action to Submitted To IRB step:2        
				}
            
             //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - start
                        else if (requester.getFunctionType() == UNDO_LAST_ACTION_CONFIRMATION) { // Added on 9th April 2011
                String protocolId = requester.getId();
                ActionTransaction axnTxn = new ActionTransaction();
                ProtocolInfoBean protocolInfoBean;
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();
                
                int success = axnTxn.undoLastAction(protocolId, loggedinUser);
                if (success == 1) {
                    protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolId);
                    responder.setDataObject(protocolInfoBean);
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
                
                        }
            //Added for COEUSQA-3144 : IRB and IACUC - Add warning to Undo Last Action that review comments will be lost - end

                        
                        else if(requester.getFunctionType() == GET_SUBMISSION_BEAN){
                                        ProtocolSubmissionTxnBean protocolSubmissionTxnBean =
                                                     new ProtocolSubmissionTxnBean();
                                        ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
                                        protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                                        requester.getId());
                                        responder.setDataObject(protocolSubmissionInfoBean);
                                        responder.setResponseStatus(true);
                                        responder.setMessage(null);
                       }// End  IRB Enhancement step:2
                       //Added for case #1961 start 2
                                else if(requester.getFunctionType() == GET_ACTIONS_DATA){
                                    ProtocolDataTxnBean protocolDataTxnBean =
                                            new ProtocolDataTxnBean();
                                    String protocolNumber = requester.getId();
                                    int seqNo = ((Integer)requester.getDataObject()).intValue();
                                    Vector data = protocolDataTxnBean.getProtocolActions(protocolNumber,seqNo);
                                    responder.setDataObject(data);
                                    responder.setResponseStatus(true);
                                    responder.setMessage(null);
                                }
                                //Added for case #1961 end 2
         }catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side.
            //Code added for Case#3554 - Notify IRB enhancement - starts
            //To display the user exception message thrown from database
            responder.setMessage(e.getMessage());
            if(e.getMessage() != null && e.getMessage().indexOf("20000") != -1){
                responder.setMessage(((DBException)e).getUserMessage());
            }
            //Code added for Case#3554 - Notify IRB enhancement - ends
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolActionServlet", "perform");
            
        }finally {
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
                "ProtocolActionServlet", "perform");
            }
        }
        
    }
    
  
    private ResponderBean performStatusChangeValidation(RequesterBean requester, UserInfoBean userBean) throws ServletException, IOException 
    {
        ResponderBean responder = new ResponderBean();    
        String loggedinUser ="";
        try
        {  
//            UtilFactory = new UtilFactory();
            // get the loggin user
            loggedinUser = userBean.getUserId(); 
            
            ProtocolActionsBean actionBean = (ProtocolActionsBean)requester.getDataObject() ;
            ActionTransaction actionTxn = new ActionTransaction(loggedinUser) ;            
            String strActionDescription = actionBean.getActionTypeDescription();
            if(strActionDescription!=null){
                strActionDescription = " " +strActionDescription.trim() + " action";
            }else{
                strActionDescription = " this action";
            }
            
            //Before performing action check whether User has rights to perform Action.
            ActionAuthorizationTxnBean actionAuthorizationTxnBean = new ActionAuthorizationTxnBean(actionBean, userBean) ;
            int authCode = actionAuthorizationTxnBean.isAuthorizedToPerformAction() ;
            if ( authCode == 1) 
            {  // action can be performed
               responder.setResponseStatus(true);
               responder.setMessage(null);
            } 
            else if (authCode == 2200) 
            { //No Action Rights message
                responder.setResponseStatus(false);
                CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
                String errMsg = 
                coeusMessageResourcesBean.parseMessageKey(
                            "protocolAction_exceptionCode." + 2200);
                //Add action Description as well 
                responder.setMessage(errMsg + strActionDescription);         
                //Sending exception to client side. - Prasanna
                CoeusException ex = new CoeusException(errMsg + strActionDescription,1);
                responder.setDataObject(ex);
                //end       
            }   
            else
            { // cannot perform this action
                responder.setResponseStatus(false);
                CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
                String errMsg= 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "protocolAction_exceptionCode." + authCode);
                responder.setMessage(errMsg);
                //Sending exception to client side. - Prasanna
                CoeusException ex = new CoeusException(errMsg,1);
                responder.setDataObject(ex);
               //end    
            }     
        }catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolActionServlet", "perform");
        }         

        return responder ;   
   }

   
    private ResponderBean  performAction(int actionCode, RequesterBean requester,
         UserInfoBean userBean) throws ServletException, IOException   
    {
        // the response object to applet
        ResponderBean responder = new ResponderBean();    
        String loggedinUser ="";
        //String unitNumber = "";
        int intActionId = 0;
        boolean statusChange = false;        
        Vector vecActionDetails = new Vector();        
        boolean isReleaseScheduleLock = false;
        Boolean releaseSchedlueLock = null;
        ProtocolActionsBean actionBean = null;
        ProtocolActionChangesBean protocolActionChangesBean  = null;
        //Addded for Case#4585 - Protocol opened from list window is not the correct one - Start 
        String userId = EMPTY_STRING;
        //Case#4585 - End
        try
        {
            // to check the null string
            
            // get the loggin user
            loggedinUser = userBean.getUserId();
            //userId = userBean.getUserName();
	userId = userBean.getUserId();
            Vector dataObjects = new Vector();
            ActionTransaction actionTxn = new ActionTransaction(actionCode) ;             
            
            // using action txn bean fire the update reqd in order to set the status
            // of the protocol to Assign to Agenda
            //ProtocolActionsBean actionBean = (ProtocolActionsBean)requester.getDataObject() ;
            //Added following code to release Schedule Lock if 
            dataObjects = requester.getDataObjects();
            actionBean = (ProtocolActionsBean) dataObjects.elementAt(0);
            releaseSchedlueLock = (Boolean) dataObjects.elementAt(1);
            isReleaseScheduleLock = releaseSchedlueLock.booleanValue();
            
            String strActionDescription = actionBean.getActionTypeDescription();
            if(strActionDescription!=null){
                strActionDescription = " " +strActionDescription.trim() + " action";
            }else{
                strActionDescription = " this action";
            }
            
            //All request actions are Performed here

            //Before performing action check whether User has rights to perform Action.
            
            String protocolId =  null ;
            if (actionBean.getProtocolNumber() == null)
            {
                protocolId = requester.getId() ;
            } 
            ActionAuthorizationTxnBean actionAuthorizationTxnBean = new ActionAuthorizationTxnBean(actionBean, userBean) ;
            if(actionCode == 104 || actionCode == 105 || actionCode == 106 || actionCode == 108 || actionCode == 109)
            {                
                int authCode = actionAuthorizationTxnBean.isAuthorizedToPerformAction() ;
                if ( authCode == 1) 
                {
                    if(actionCode == 104)
                    {
                       actionBean.setComments("Request for Termination");
                    }
                    else if(actionCode == 105)
                    {
                       actionBean.setComments("Request to Close");
                    }
                    else if(actionCode == 106)
                    {
                       actionBean.setComments("Request for Suspension");
                    }
                    else if(actionCode == 108)
                    {
                       actionBean.setComments("Request to Close Enrollment");
                    }
                     intActionId = actionTxn.performAction(actionBean, loggedinUser) ;
                  
                     if(intActionId > 0)
                     {
                        statusChange = true;
                        actionBean.setActionId(intActionId);
                     }
                }
                else if(authCode == 2200)
                {   //No Action rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + 2200);
                    //Add action Description as well 
                    responder.setMessage(errMsg + strActionDescription);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg + strActionDescription,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }
                else
                { // action cannot be performed
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + authCode);
                    responder.setMessage(errMsg);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }    
              }// end if 104, 106, 108, 109
            else
            { // all other actions
                boolean success = false ;
                int authCode = actionAuthorizationTxnBean.isAuthorizedToPerformAction() ;
                if (authCode == 1)
                {
                    //Update Review Comments - start
                        ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                        Vector vctReviewComments = actionBean.getReviewComments();
                        boolean blnReviewComments = false;
                        if(vctReviewComments!=null){
                            blnReviewComments = scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(vctReviewComments);
                        }
                      //Update Review Comments - end

                        //Get ActionId from Action Log
                        intActionId = actionTxn.performAction(actionBean, loggedinUser) ;
                        if(intActionId > 0) 
                        {
                            //Code added for Case#3070 - Ability to modify funding source - starts
                            //sending mail to the recepients
                            if(actionCode == 208 && actionBean.getProtocolNumber() != null
                                    && actionBean.getProtocolNumber().length() > 10){
                                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean(userId);
                                CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                                Vector vecProtocolLink = protocolDataTxnBean.getProtocolLinksData(actionBean.getProtocolNumber());
                                if(vecProtocolLink != null && vecProtocolLink.size() > 0){
                                    for(int index = 0; index < vecProtocolLink.size(); index++){
                                        ProtocolLinkBean protocolLinkBean = (ProtocolLinkBean) vecProtocolLink.get(index);
                                        //Modified for COEUSDEV-75:Rework email engine so the email body is picked up from one place
                                        if(protocolLinkBean.getActionType() != null && protocolLinkBean.getActionType().equals("I")){
                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_INSERTED,
                                                    actionBean.getProtocolNumber(),actionBean.getSequenceNumber(),protocolLinkBean.getModuleItemKey(),protocolLinkBean.getModuleCode());
                                        } else if(protocolLinkBean.getActionType() != null && protocolLinkBean.getActionType().equals("D")){
                                            protocolDataTxnBean.sendEmailNotification(ModuleConstants.PROTOCOL_MODULE_CODE,MailActions.SPECIAL_REVIEW_DELETED,
                                                    actionBean.getProtocolNumber(),actionBean.getSequenceNumber(),protocolLinkBean.getModuleItemKey(),protocolLinkBean.getModuleCode());
                                        }
                                        //COEUSDEV-75:End
                                    }
                                }
                            }
                            //Code added for Case#3070 - Ability to modify funding source - ends
                            statusChange = true;
                            actionBean.setActionId(intActionId);
                        }
                }    
                
                else if (authCode == 2200)
                { // No Action rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + 2200);
                    //Add action Description as well 
                    responder.setMessage(errMsg + strActionDescription);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg + strActionDescription,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }
                else
                { // cannot perform this action
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + authCode);
                    responder.setMessage(errMsg);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }
            } // end else
            
                
                
            if (statusChange)
            {
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();                
                ProtocolInfoBean protocolInfoBean = null;
                String protocolNumber = null;
                //Coeus enhancement Case #1791 - step 1: start
                if(actionCode==204 || actionCode==205 || actionCode==208 || actionCode==209){ //If Approval or Expedited Approval get Original Protocol Number
                //Coeus enhancement Case #1791 - step 1: end
                    if(actionBean.getProtocolNumber().length() > 10){
                        protocolNumber = actionBean.getProtocolNumber().substring(0,10);
                        protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        actionBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                        actionBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                    }else{
                        protocolNumber = actionBean.getProtocolNumber();
                        protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        actionBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                        actionBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());                        
                    }
                }else{
                    protocolNumber = actionBean.getProtocolNumber();
                    protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                    actionBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                    actionBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());                    
                }
                //Add sub action details
                vecActionDetails.add(actionTxn.getSubAction());
                vecActionDetails.add(actionTxn.getHashUserPrompt());
                vecActionDetails.add(actionTxn.getHashUserPromptFlag());
                
                //Get all the latest data to update the list - start
                protocolActionChangesBean  = new ProtocolActionChangesBean();
                protocolActionChangesBean.setProtocolNumber(actionBean.getProtocolNumber());
                protocolActionChangesBean.setSequenceNumber(actionBean.getSequenceNumber());
                protocolActionChangesBean.setProtocolStatusCode(protocolInfoBean.getProtocolStatusCode());
                protocolActionChangesBean.setProtocolStatusDescription(protocolInfoBean.getProtocolStatusDesc());
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                //protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(actionBean.getProtocolNumber());
                //Check if there is Submission Number.
                //If present get that Submission details else get latest Submission details
                if(actionBean.getSubmissionNumber() == 0){
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(actionBean.getProtocolNumber());        
                }else{
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber());
                }                
                protocolActionChangesBean.setSubmissionNumber(protocolSubmissionInfoBean.getSubmissionNumber());                
                protocolActionChangesBean.setSubmissionStatusCode(protocolSubmissionInfoBean.getSubmissionStatusCode());
                protocolActionChangesBean.setSubmissionStatusDescription(protocolSubmissionInfoBean.getSubmissionStatusDesc());
                protocolActionChangesBean.setSubmissionTypeCode(protocolSubmissionInfoBean.getSubmissionTypeCode());
                protocolActionChangesBean.setSubmissionTypeDescription(protocolSubmissionInfoBean.getSubmissionTypeDesc());
                //Added for Assign to Schedule - start
                protocolActionChangesBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId());
                protocolActionChangesBean.setScheduleDate(protocolSubmissionInfoBean.getScheduleDate());
                protocolActionChangesBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                //Added for Assign to Schedule - end                
                //Get all the latest data to update the list - end                
                //For Case #2004 Start 2
                actionBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                //Modified for the case# COESDEV-219 - send functionality for correspondence-start
                actionBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId());
                //Modified for the case# COESDEV-219 - send functionality for correspondence-end
                //For Case #2004 End 2
                
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //boolean corrGenerated = generateCorrespondences(actionBean) ; 
                boolean corrGenerated = generateCorrespondences(userId,actionBean) ; 
                //Case#4585 - End
                
                Vector vecGeneratedDocs = null;
               if(corrGenerated){
                   vecGeneratedDocs = protocolDataTxnBean.getAllCorrespondenceDocuments(actionBean.getProtocolNumber(), actionBean.getSequenceNumber(), actionBean.getActionId());
               }
               //Add generated docs as well
                vecActionDetails.addElement(vecGeneratedDocs);
                vecActionDetails.addElement(protocolActionChangesBean);                
                
                responder.setDataObject(vecActionDetails);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else
            {
                responder.setResponseStatus(false);
                CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
                String errMsg= 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "schdMnt_upd_exceptionCode.1010");
                //print the error message at client side
                responder.setMessage(errMsg);
            }    
            
         }catch(Exception e) {
            if(statusChange){
                //If Action performed successfully set Response Status as true
                vecActionDetails.addElement(new Vector());
                vecActionDetails.addElement(protocolActionChangesBean);                 
                responder.setResponseStatus(true);
                responder.setDataObject(vecActionDetails);
                CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
                String errMsg= 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "protocolCorrespondence_exceptionCode.3400");
                //print the error message at client side
                responder.setMessage(errMsg);
            }else{
                //print the error message at server side
                responder.setResponseStatus(false);
                //print the error message at client side
                responder.setMessage(e.getMessage());
            }
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolActionServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "ProtocolActionServlet", "doPost");
        //Case 3193 - END
        }finally{
            //Release lock of schedule - start
            if(isReleaseScheduleLock){
                if(actionBean.getScheduleId()!=null && !actionBean.getScheduleId().equalsIgnoreCase("9999999999")){
                    String rowLockStr = actionBean.getScheduleId();
                    ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                    scheduleMaintenanceTxnBean.releaseEdit(rowLockStr);                
                }
            }
            //Release lock of schedule - end
        }
        
        return responder ;   
            
    }
    
    /** Overloaded method
     *  This method is used if a action has to be performed on a set of Records 
    **/
    private ResponderBean  performAction(int actionCode, Vector actionBeans, UserInfoBean userBean) 
        throws ServletException, IOException   
    {
        // the response object to applet
        ResponderBean responder = new ResponderBean();    
        String loggedinUser ="";
        String unitNumber = "";
        int intActionId = 0;
        boolean statusChange = false;        
        Vector vecActionDetails = new Vector();        
        boolean isReleaseScheduleLock = false;
        Boolean releaseSchedlueLock = null;
        //ProtocolActionsBean actionBean = null;
        ProtocolActionChangesBean protocolActionChangesBean  = null;
        String strActionDescription = "";
        try
        {
            // to check the null string
            
            // get the loggin user
            loggedinUser = userBean.getUserId();
            //Get Unit Number
            unitNumber = userBean.getUnitNumber();
            
            ActionTransaction actionTxn = new ActionTransaction(actionCode) ;             
            
            if(actionCode == 109){
                //Before performing action check whether User has rights to perform Action.
                UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                boolean success = txnData.getUserHasRight(loggedinUser, ACTION_RIGHT, unitNumber);
                int check = 0;
                if(success){
                    ProtocolActionsBean protocolActionsBean = null;
                    for(int row =0;row<actionBeans.size();row++){
                       protocolActionsBean = (ProtocolActionsBean)actionBeans.elementAt(row);
                        strActionDescription = protocolActionsBean.getActionTypeDescription();
                        if(strActionDescription!=null){
                            strActionDescription = " " +strActionDescription.trim() + " action";
                        }else{
                            strActionDescription = " this action";
                        }
                        intActionId = actionTxn.performOtherActions(protocolActionsBean, loggedinUser);                            

                        if(intActionId > 0){
                            statusChange = true;
                            protocolActionsBean.setActionId(intActionId);
                            //actionBean.setSequenceNumber(actionBean.getSequenceNumber()+1);
                        }                                
                    }
                    responder.setResponseStatus(true);
                    //responder.setMessage(null);
                }else{
                    //No Action rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + 2200);
                    //Add action Description as well 
                    responder.setMessage(errMsg + strActionDescription);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg + strActionDescription,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                    
                }
            }
            
         }catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolActionServlet", "perform");
        }        
        return responder ;               
    }
    
    //Bijosh
        private ResponderBean  performActionForTags(int actionCode, RequesterBean requester,
         UserInfoBean userBean) throws ServletException, IOException   
    {
        // the response object to applet
        ResponderBean responder = new ResponderBean();    
        String loggedinUser ="";
        //String unitNumber = "";
        int intActionId = 0;
        boolean statusChange = false;        
        Vector vecActionDetails = new Vector();        
        boolean isReleaseScheduleLock = false;
        Boolean releaseSchedlueLock = null;
        ProtocolActionsBean actionBean = null;
        ProtocolActionChangesBean protocolActionChangesBean  = null;
        String userId = EMPTY_STRING;
        try
        {
            // to check the null string
            
            // get the loggin user
            loggedinUser = userBean.getUserId();
            //userId = userBean.getUserName();
	userId = userBean.getUserId();
            Vector dataObjects = new Vector();
            ActionTransaction actionTxn = new ActionTransaction(actionCode) ;             
            
            // using action txn bean fire the update reqd in order to set the status
            // of the protocol to Assign to Agenda
            //ProtocolActionsBean actionBean = (ProtocolActionsBean)requester.getDataObject() ;
            //Added following code to release Schedule Lock if 
            dataObjects = requester.getDataObjects();
            actionBean = (ProtocolActionsBean) dataObjects.elementAt(0);
            releaseSchedlueLock = (Boolean) dataObjects.elementAt(1);
            byte[] templates = (byte[]) dataObjects.elementAt(2);
            isReleaseScheduleLock = releaseSchedlueLock.booleanValue();
            
            String strActionDescription = actionBean.getActionTypeDescription();
            if(strActionDescription!=null){
                strActionDescription = " " +strActionDescription.trim() + " action";
            }else{
                strActionDescription = " this action";
            }
            
            //All request actions are Performed here

            //Before performing action check whether User has rights to perform Action.
            
            String protocolId =  null ;
            if (actionBean.getProtocolNumber() == null)
            {
                protocolId = requester.getId() ;
            } 
            ActionAuthorizationTxnBean actionAuthorizationTxnBean = new ActionAuthorizationTxnBean(actionBean, userBean) ;
            if(actionCode == 104 || actionCode == 105 || actionCode == 106 || actionCode == 108 || actionCode == 109)
            {                
                int authCode = actionAuthorizationTxnBean.isAuthorizedToPerformAction() ;
                if ( authCode == 1) 
                {
                    if(actionCode == 104)
                    {
                       actionBean.setComments("Request for Termination");
                    }
                    else if(actionCode == 105)
                    {
                       actionBean.setComments("Request to Close");
                    }
                    else if(actionCode == 106)
                    {
                       actionBean.setComments("Request for Suspension");
                    }
                    else if(actionCode == 108)
                    {
                       actionBean.setComments("Request to Close Enrollment");
                    }
                    
                    intActionId = actionTxn.performAction(actionBean, loggedinUser) ;
                     if(intActionId > 0)
                     {
                        statusChange = true;
                        actionBean.setActionId(intActionId);
                     }
                }
                else if(authCode == 2200)
                {   //No Action rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + 2200);
                    //Add action Description as well 
                    responder.setMessage(errMsg + strActionDescription);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg + strActionDescription,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }
                else
                { // action cannot be performed
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + authCode);
                    responder.setMessage(errMsg);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }    
              }// end if 104, 106, 108, 109
            else
            { // all other actions
                boolean success = false ;
                int authCode = actionAuthorizationTxnBean.isAuthorizedToPerformAction() ;
                
                if (authCode == 1)
                {
                    //Update Review Comments - start
                        ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean = new ScheduleMaintenanceUpdateTxnBean(loggedinUser);
                        Vector vctReviewComments = actionBean.getReviewComments();
                        boolean blnReviewComments = false;
                        if(vctReviewComments!=null){
                            blnReviewComments = scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(vctReviewComments);
                        }
                      //Update Review Comments - end

                        //Get ActionId from Action Log
                        intActionId = actionTxn.performAction(actionBean, loggedinUser) ;
                        if(intActionId > 0)
                        {
                            statusChange = true;
                            actionBean.setActionId(intActionId);
                        }
                }    
                else if (authCode == 2200)
                { // No Action rights message
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + 2200);
                    //Add action Description as well 
                    responder.setMessage(errMsg + strActionDescription);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg + strActionDescription,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }
                else
                { // cannot perform this action
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean 
                        =new CoeusMessageResourcesBean();
                    String errMsg = 
                    coeusMessageResourcesBean.parseMessageKey(
                                "protocolAction_exceptionCode." + authCode);
                    responder.setMessage(errMsg);         
                    //Sending exception to client side. - Prasanna
                    CoeusException ex = new CoeusException(errMsg,1);
                    responder.setDataObject(ex);
                    //end                        
                    return responder;
                }
            } // end else
            
                
                
            if (statusChange)
            {
                ProtocolDataTxnBean protocolDataTxnBean = new ProtocolDataTxnBean();                
                ProtocolInfoBean protocolInfoBean = null;
                String protocolNumber = null;
                //Coeus enhancement Case #1791 - step 2: start
                if(actionCode==204 || actionCode==205 || actionCode==208 || actionCode==209){ //If Approval or Expedited Approval get Original Protocol Number
                    //Coeus enhancement Case #1791 - step 2: end
                    if(actionBean.getProtocolNumber().length() > 10){
                        protocolNumber = actionBean.getProtocolNumber().substring(0,10);
                        protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        actionBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                        actionBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                    }else{
                        protocolNumber = actionBean.getProtocolNumber();
                        protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                        actionBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                        actionBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());                        
                    }
                }else{
                    protocolNumber = actionBean.getProtocolNumber();
                    protocolInfoBean = protocolDataTxnBean.getProtocolMaintenanceDetails(protocolNumber);
                    actionBean.setProtocolNumber(protocolInfoBean.getProtocolNumber());
                    actionBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());                    
                }
                //Add sub action details
                vecActionDetails.add(actionTxn.getSubAction());
                vecActionDetails.add(actionTxn.getHashUserPrompt());
                vecActionDetails.add(actionTxn.getHashUserPromptFlag());
                
                //Get all the latest data to update the list - start
                protocolActionChangesBean  = new ProtocolActionChangesBean();
                protocolActionChangesBean.setProtocolNumber(actionBean.getProtocolNumber());
                protocolActionChangesBean.setSequenceNumber(actionBean.getSequenceNumber());
                protocolActionChangesBean.setProtocolStatusCode(protocolInfoBean.getProtocolStatusCode());
                protocolActionChangesBean.setProtocolStatusDescription(protocolInfoBean.getProtocolStatusDesc());
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                //protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(actionBean.getProtocolNumber());
                //Check if there is Submission Number.
                //If present get that Submission details else get latest Submission details
                if(actionBean.getSubmissionNumber() == 0){
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(actionBean.getProtocolNumber());        
                }else{
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getSubmissionForSubmissionNumber(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber());
                }                
                protocolActionChangesBean.setSubmissionNumber(protocolSubmissionInfoBean.getSubmissionNumber());                
                protocolActionChangesBean.setSubmissionStatusCode(protocolSubmissionInfoBean.getSubmissionStatusCode());
                protocolActionChangesBean.setSubmissionStatusDescription(protocolSubmissionInfoBean.getSubmissionStatusDesc());
                protocolActionChangesBean.setSubmissionTypeCode(protocolSubmissionInfoBean.getSubmissionTypeCode());
                protocolActionChangesBean.setSubmissionTypeDescription(protocolSubmissionInfoBean.getSubmissionTypeDesc());
                //Added for Assign to Schedule - start
                protocolActionChangesBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId());
                protocolActionChangesBean.setScheduleDate(protocolSubmissionInfoBean.getScheduleDate());
                protocolActionChangesBean.setScheduleId(protocolSubmissionInfoBean.getScheduleId());
                //Added for Assign to Schedule - end                
                //Get all the latest data to update the list - end                
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //boolean corrGenerated = generateCorrespondencesWithTags(actionBean,templates) ; 
                boolean corrGenerated = generateCorrespondencesWithTags(userId,actionBean,templates) ; 
                //Case#4585 - End                
                Vector vecGeneratedDocs = null;
               if(corrGenerated){
                   vecGeneratedDocs = protocolDataTxnBean.getAllCorrespondenceDocuments(actionBean.getProtocolNumber(), actionBean.getSequenceNumber(), actionBean.getActionId());
               }
               //Add generated docs as well
                vecActionDetails.addElement(vecGeneratedDocs);
                vecActionDetails.addElement(protocolActionChangesBean);                
                
                responder.setDataObject(vecActionDetails);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            else
            {
                responder.setResponseStatus(false);
                CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
                String errMsg= 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "schdMnt_upd_exceptionCode.1010");
                //print the error message at client side
                responder.setMessage(errMsg);
            }    
            
         }catch(Exception e) {
            if(statusChange){
                //If Action performed successfully set Response Status as true
                vecActionDetails.addElement(new Vector());
                vecActionDetails.addElement(protocolActionChangesBean);                 
                responder.setResponseStatus(true);
                responder.setDataObject(vecActionDetails);
                CoeusMessageResourcesBean coeusMessageResourcesBean 
                    =new CoeusMessageResourcesBean();
                String errMsg= 
                        coeusMessageResourcesBean.parseMessageKey(
                                    "protocolCorrespondence_exceptionCode.3400");
                //print the error message at client side
                responder.setMessage(errMsg);
            }else{
                //print the error message at server side
                responder.setResponseStatus(false);
                //print the error message at client side
                responder.setMessage(e.getMessage());
            }
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolActionServlet", "perform");
        }finally{
            //Release lock of schedule - start
            if(isReleaseScheduleLock){
                if(actionBean.getScheduleId()!=null && !actionBean.getScheduleId().equalsIgnoreCase("9999999999")){
                    String rowLockStr = actionBean.getScheduleId();
                    ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
                    scheduleMaintenanceTxnBean.releaseEdit(rowLockStr);                
                }
            }
            //Release lock of schedule - end
        }
        
        return responder ;   
            
    }
    
    //Bijosh 
    
    
    
    private ResponderBean submitToIrbAction (int actionCode, RequesterBean requester,
         UserInfoBean userBean) throws ServletException, IOException   
    {
//       System.out.println(" \n **** IN Submit to IRB function *** \n" ) ;
        
        // the response object to applet
        ResponderBean responder = new ResponderBean();    
        String loggedinUser ="";
        String exceptionCode = "";
        try
        {  
//            System.out.println("\n *** s1 *** \n") ;
            // to check the null string
            
            // get the loggin user
            loggedinUser = userBean.getUserId();

            ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                         new ScheduleMaintenanceTxnBean();
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean =
                                         new ProtocolSubmissionTxnBean();
            ProtocolDataTxnBean protocolDataTxnBean =
                                    new ProtocolDataTxnBean();
            ProtocolSubmissionUpdateTxnBean  protocolSubmissionUpdateTxnBean = 
                             new ProtocolSubmissionUpdateTxnBean(loggedinUser);
            
            String protocolNumber = "";
 
            // keep all the beans into vector
            Vector dataObjects = new Vector();

            char functionType = requester.getFunctionType();
//            System.out.println("\n *** s2 func type " +  functionType + " *** \n") ;
            
            if (functionType == ADD_MODE || functionType == MODIFY_MODE ) 
            {
//                System.out.println("\n *** s2.1 *** \n") ;
                
                //0-set the Protocol Submission Type 
                    dataObjects.addElement(
                            scheduleMaintenanceTxnBean.getProtocolSubmissionTypes());
                    //1 - set protocol Submission Type Qualifiers information
                    dataObjects.addElement(
                    scheduleMaintenanceTxnBean.getProtocolSubmissionTypeQualifiers());
                    //2 - set Protocol review Types information
                    dataObjects.addElement(
                     scheduleMaintenanceTxnBean.getProtocolSubmissionReviewTypes());
                    //3 - set Protocol reviewer Types information
                    dataObjects.addElement(
                              protocolSubmissionTxnBean.getProtocolReviewerTypes());
                    try
                    {
                        if (!requester.getId().equalsIgnoreCase("")) 
                        {
                            //4 - set CommitteeMaintenanceFormBean
                            dataObjects.addElement(
                            protocolSubmissionTxnBean.getMatchingCommitteeForProtocol(
                            requester.getId()));
                            //5 - set ProtocolSubmissionInfoBean
                            ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
                              protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                            requester.getId());
                            dataObjects.addElement(protocolSubmissionInfoBean);

                        }
                     }catch(DBException dbe)
                        {
                            responder.setDataObjects(dataObjects);
                            throw dbe;
                        }
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
//                    System.out.println("\n *** s2.2 *** \n") ;
                
            }
            else if(functionType == PROTO_SUB_COUNT)
            {
//                System.out.println("\n *** s3 *** \n") ;
                    // get the protocol submission list for protocol number
                   int count =  protocolSubmissionTxnBean.getNumberOfSubmittedProtocols(
                                                        requester.getId()); 

                    dataObjects.addElement(new Integer(count));
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
             }
             else if(functionType == SCHEDULE_SELECTION)
             {
//                 System.out.println("\n *** s4 *** \n") ;       
                 // get the schedule details for committee
                        dataObjects.addElement(
                                protocolSubmissionTxnBean.getScheduleListForCommittee(
                                                                requester.getId()));
                        responder.setDataObjects(dataObjects);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
             }
             else if (functionType == REVIEWER_SELECTION)
             {
//                 System.out.println("\n *** s5 *** \n") ;
                                // get the committee membership for schedule
                            dataObjects.addElement(
                                    protocolSubmissionTxnBean.getCommitteeMembersForSchedule(
                                                                    requester.getId()));
                            responder.setDataObjects(dataObjects);
                            responder.setResponseStatus(true);
                            responder.setMessage(null);
              }
              else if (functionType == SAVE_MODE )
              {
//                  System.out.println("\n *** s6 *** \n") ;
                                //get the protocol submission Info Bean from the RequesterObject.
                                ProtocolInfoBean newProtocolData = new ProtocolInfoBean();
                                ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
                                 (ProtocolSubmissionInfoBean)requester.getDataObjects().elementAt(0);
                                //get the protocol Info Bean from the RequesterObject.
                                ProtocolInfoBean protocolInfoBean =
                                  (ProtocolInfoBean)requester.getDataObjects().elementAt(1);
                                System.out.println("protocol info bean=>"+protocolInfoBean);
                
//                   System.out.println("\n *** s6.1 *** \n") ;             
                                // prps added, since the call to this servlet is from schedule screen lots
                                // of data will be missing, so use ActionTransaction to get the rest of the data
                                ActionTransaction actionTransaction = new ActionTransaction() ;
                                protocolInfoBean = actionTransaction.getRestOfDetailsProtocolInfoBean(protocolInfoBean) ;
                                
//                  System.out.println("\n *** s6.2 *** \n") ;              
                                if (protocolInfoBean == null)
                                { System.out.println("\n *** ProtocolInfoBean is Null *** \n" ) ; }
                                else { System.out.println("\n *** ProtocolInfoBean is not Null *** \n" ) ; }
                                
                                   if ( protocolSubmissionUpdateTxnBean.addUpdProtocolSubmission(
                                                                protocolSubmissionInfoBean,
                                                                protocolInfoBean) )
                                   {
//                                    System.out.println("\n *** s6.3 *** \n") ;
                                       protocolNumber = 
                                                protocolSubmissionInfoBean.getProtocolNumber();                            
                                        newProtocolData =
                                          protocolDataTxnBean.getProtocolInfo(protocolNumber);
                                        dataObjects = new Vector();
                                        dataObjects.addElement(newProtocolData);
                                        responder.setDataObjects(dataObjects);
                                        responder.setResponseStatus(true);
                                     
                                        //prps
                                        // when u get here u r completely done with the submit_to _irb steps
                                        // so u can go ahead n generate correspondences and also what u do is
                                        // find the sub actions or the follow up actions and put it in a bean and
                                        // send t back to the caller, so that the follow up actiosn are called recursively
                                        
                                        boolean corrGenerated = generateCorrespondences() ; 
                                        
                                        // find the sub actions and get the vector
                                        ActionTransaction actionTxn = new ActionTransaction(actionCode) ; 
                                        Vector vecSubAction = actionTxn.getSubAction() ;
                                        
                                        //to be coded..  (sending this vector back to the client, i think if u add 
                                        // this vector to dataObjects (a vector again) it shud take care of it )
//                                        System.out.println("\n *** s6.4 *** \n") ;
                                    }
                                    else
                                    {
                                        responder.setResponseStatus(false);
                                        CoeusMessageResourcesBean coeusMessageResourcesBean 
                                            =new CoeusMessageResourcesBean();
                                        String errMsg= 
                                                coeusMessageResourcesBean.parseMessageKey(
                                                            "schdMnt_upd_exceptionCode.1010");
                                        //print the error message at client side
                                        responder.setMessage(errMsg);
                                    }
              }
              else if (functionType == DISPLAY_MODE)
              {
//                  System.out.println("\n *** s6.5 *** \n") ;
                                //0 - set Protocol Submission Status info
                                ProtocolSubmissionInfoBean protocolSubmissionInfoBean =  
                                       protocolSubmissionTxnBean.getProtocolSubmissionDetails(
                                                                        requester.getId());
                                dataObjects.addElement(protocolSubmissionInfoBean);
                                responder.setDataObjects(dataObjects);
                                responder.setResponseStatus(true);
                                responder.setMessage(null);
               }
               else if (functionType == CHECK_PROTOCOL_MIN)
               {
                                    //0 - set check for minute protocol
                                    boolean checkMinute =  
                                                protocolSubmissionTxnBean.checkMinutesForProtocol(
                                                                            requester.getId());
                                    responder.setDataObject(new Boolean(checkMinute));
                                    responder.setResponseStatus(true);
                                    responder.setMessage(null);
                } 
            

        } catch( CoeusException coeusEx ) {
            int index=0;
            String errMsg;
            errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                    new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "ProtocolSubmissionServlet",
                                                                "perform");
            
        } catch( DBException dbEx ) {
            int index=0;
            String errMsg;
            errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean =
                            new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "ProtocolActionServlet",
                                                                "perform");
            
        } catch(Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, 
                "ProtocolActionServlet", "perform");
            
        }         
        
        return responder ;
    }
    
    
    private boolean initialSteps (HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException   
    {
//        System.out.println(" ***** Basic steps successfully done   *****  ") ;
        return true ;
    }
    //Bijosh
    /*
     *To get the Template for regenrating the reports
     */
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //private byte[] getTemplatesForRegenerate(ProtocolActionsBean actionBean, int protoCorrespTypeCode) 
    private byte[] getTemplatesForRegenerate(String userId,ProtocolActionsBean actionBean, int protoCorrespTypeCode) //Case#4585 - End
    throws CoeusException, DBException {
        boolean blnCorrGenerated = false;
        ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean() ;
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
        submissionTxnBean.getSubmissionForSubmissionNumber(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber()) ;
        //Modified to fix Bug Id: 980 - start
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean("COEUS");
        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        //Case#4585 - End
        //Modified to fix Bug Id: 980 - end
        
        byte[] templateFileBytes=null;
        File reportFile = null;
        FileOutputStream fos = null;
        //        ProcessReportXMLBean bean = null;
        Vector vctReceipients = null;
        Vector vctRecp = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null ;
        Vector attachmentFilePath = new Vector();
        if (protocolSubmissionInfoBean == null)// this will happen when submission is zero(for adhoc reports) then pass null scheduleid to get the template, which will bring back DEFAULT committee template
        {
            //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-start
            templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(protoCorrespTypeCode, null,null); // prps-use protocolSubmissionInfoBean instead of actionBean
            //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-end
        }
        else {
            //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-start
            templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(protoCorrespTypeCode, protocolSubmissionInfoBean.getScheduleId(),protocolSubmissionInfoBean.getCommitteeId()); // prps-use protocolSubmissionInfoBean instead of actionBean
            //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-end
        }
        return templateFileBytes;
    }
    //Bijosh       
    //Bijosh
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //public boolean regenerateCorrespondencesWithTags(ProtocolActionsBean actionBean, int protoCorrespTypeCode,byte[] templates)
    public boolean regenerateCorrespondencesWithTags(String userId,ProtocolActionsBean actionBean, int protoCorrespTypeCode,byte[] templates)//Case#4585 - End
    throws CoeusException, DBException,
    javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException,
    javax.xml.transform.TransformerConfigurationException,
    javax.xml.transform.TransformerException, IOException,
    org.apache.fop.apps.FOPException {
        boolean blnCorrGenerated = false;
        ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean() ;
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean =
        submissionTxnBean.getSubmissionForSubmissionNumber(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber()) ;
        //Added for case id COEUSQA-1724 iacuc protocol stream generation start
        CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
        int committeeTypeCode = commTxnBean.getCommitteeType(protocolSubmissionInfoBean.getCommitteeId());
         //Added for case id COEUSQA-1724 iacuc protocol stream generation end
        //Modified to fix Bug Id: 980 - start
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean("COEUS");
        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        //Case#4585 - End
        //Modified to fix Bug Id: 980 - end
        
        byte[] templateFileBytes = templates ;
        File reportFile = null;
        FileOutputStream fos = null;
        //        ProcessReportXMLBean bean = null;
        Vector vctReceipients = null;
        Vector vctRecp = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null ;
        Vector attachmentFilePath = new Vector();
       /* if (protocolSubmissionInfoBean == null)// this will happen when submission is zero(for adhoc reports) then pass null scheduleid to get the template, which will bring back DEFAULT committee template
        {
            templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(protoCorrespTypeCode, null); // prps-use protocolSubmissionInfoBean instead of actionBean
        }
        else {
            templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(protoCorrespTypeCode, protocolSubmissionInfoBean.getScheduleId()); // prps-use protocolSubmissionInfoBean instead of actionBean
        }*/
        if(templateFileBytes!=null) {
            
            //prps start nov 03 2003
            // Generator here will build a xml with all possible details abt a particular Protocol
            // the template will decide which details to show or which details to be put in the pdf( or correspondence).
            // Even though xml is big the generated pdf will have only necessary data in it (determined by the xsl template)
            
            
            //XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
            if (protocolSubmissionInfoBean != null) {
                actionBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId()) ; // add committeeId to actionBean
            }
                /*
                 *  Getting the current sequence number for a particular protocol for
                 *  regenerating the correspondence. It uses fn_get_curr_seq_for_corresp
                 *  to fetch the sequence number and use this sequence number for
                 *  regeneration. Since the action bean is nowhere using after this processing,
                 *  set the actionbean sequence number to the seq number which function returns.
                 */
            int actualSeqNumber = actionBean.getSequenceNumber();
            int currSeqNum = submissionTxnBean.getSeqNumberForRegeneration(actionBean.getProtocolNumber(),
            actionBean.getSequenceNumber(), actionBean.getActionId());
            actionBean.setSequenceNumber(currSeqNum);
                /*
                 *  End block
                 */
            //Added for case id COEUSQA-1724 iacuc protocol stream generation start
            Document xmlDoc = null;
             try{
                if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                    xmlDoc = getIACUCCorrespondenceXMLStream(actionBean);
                }else {
                    xmlDoc = getIRBCorrespondenceXMLStream(actionBean);
                }
            }catch(Exception e){
                  UtilFactory.log("Error generating xml. ", e, "ProtocolActionServlet", "regenerateCorrespondencesWithTags" );
            }
            //Added for case id COEUSQA-1724 iacuc protocol stream generation end
           // Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ;
            //Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber(), protocolSubmissionInfoBean.getCommitteeId()) ;
//            System.out.println("Data xml - regenerating complete!") ;
            UtilFactory.log("Xml doc regenerating complete!") ;
            
//            System.out.println("Start Pdf generation...") ;
            XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
            boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes) ;
            if (fileGenerated) {
                templateFileBytes = conv.getGeneratedPdfFileBytes() ;
//                System.out.println("Pdf generation successfull...") ;
            }
//            System.out.println("End Pdf generation...") ;
                /*
                 *  Setting back the actual sequance number to action bean before
                 *  updating to the database.
                 */
            actionBean.setSequenceNumber(actualSeqNumber);
            //prps end nov 03 2003
            blnCorrGenerated = protoCorrespTypeTxnBean.UpdateProtocolCorrespondence(actionBean, protoCorrespTypeCode, templateFileBytes);
            
        }
        
        return blnCorrGenerated ;
    }
    //Bijosh
    
    // this method is called from the protocol screen. The only difference between this
    // method and generateCorrespondences is that this method will have to obtain scheduleId
    // using the protocolid and submissionNumber. 
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //public boolean regenerateCorrespondences(ProtocolActionsBean actionBean, int protoCorrespTypeCode) 
    public boolean regenerateCorrespondences(String userId,ProtocolActionsBean actionBean, int protoCorrespTypeCode) //Case#4585 - End
        throws CoeusException, DBException, 
                javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException, 
                javax.xml.transform.TransformerConfigurationException, 
                javax.xml.transform.TransformerException, IOException, 
                org.apache.fop.apps.FOPException
    {
        boolean blnCorrGenerated = false;
        ProtocolSubmissionTxnBean submissionTxnBean = new ProtocolSubmissionTxnBean() ;
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = 
                                   submissionTxnBean.getSubmissionForSubmissionNumber(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber()) ;
        //Modified to fix Bug Id: 980 - start
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean("COEUS");
        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        //Case#4585 - End
        //Modified to fix Bug Id: 980 - end
        
        byte[] templateFileBytes;
        File reportFile = null;
        FileOutputStream fos = null;
//        ProcessReportXMLBean bean = null;
        Vector vctReceipients = null; 
        Vector vctRecp = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null ;
        Vector attachmentFilePath = new Vector();
        //Added for case id COEUSQA-1724 iacuc protocol stream generation start
        CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
        //Added for case id COEUSQA-1724 iacuc protocol stream generation end
         int committeeTypeCode = 0;

            if (protocolSubmissionInfoBean == null)// this will happen when submission is zero(for adhoc reports) then pass null scheduleid to get the template, which will bring back DEFAULT committee template
            {
                //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-start
                templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(protoCorrespTypeCode, null,null); // prps-use protocolSubmissionInfoBean instead of actionBean
                //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-end
            }    
            else
            {    
                //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-start
                templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(protoCorrespTypeCode, protocolSubmissionInfoBean.getScheduleId(),protocolSubmissionInfoBean.getCommitteeId()); // prps-use protocolSubmissionInfoBean instead of actionBean
                //Modified for the case#coeusdev-229- Gnerate corrspondence Manually-end
            }   
                if(templateFileBytes!=null)
                {
                    
                //prps start nov 03 2003
                // Generator here will build a xml with all possible details abt a particular Protocol
                // the template will decide which details to show or which details to be put in the pdf( or correspondence).
                // Even though xml is big the generated pdf will have only necessary data in it (determined by the xsl template)
               // XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
                if (protocolSubmissionInfoBean != null)
                {    
                    actionBean.setCommitteeId(protocolSubmissionInfoBean.getCommitteeId()) ; // add committeeId to actionBean
                    committeeTypeCode = commTxnBean.getCommitteeType(protocolSubmissionInfoBean.getCommitteeId());
                }
               
	   
                /*
                 *  Getting the current sequence number for a particular protocol for
                 *  regenerating the correspondence. It uses fn_get_curr_seq_for_corresp
                 *  to fetch the sequence number and use this sequence number for 
                 *  regeneration. Since the action bean is nowhere using after this processing,
                 *  set the actionbean sequence number to the seq number which function returns.
                 */
                int actualSeqNumber = actionBean.getSequenceNumber();
                int currSeqNum = submissionTxnBean.getSeqNumberForRegeneration(actionBean.getProtocolNumber(), 
                                            actionBean.getSequenceNumber(), actionBean.getActionId());
                actionBean.setSequenceNumber(currSeqNum);
                /*
                 *  End block
                 */
                
                //Added for case id COEUSQA-1724 iacuc protocol stream generation start
                Document xmlDoc = null;
                try{
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                       xmlDoc = getIACUCCorrespondenceXMLStream(actionBean);
                    }else{
                       xmlDoc = getIRBCorrespondenceXMLStream(actionBean);
                    }
                }catch(Exception e){
                    UtilFactory.log("Error generating xml. ", e, "ProtocolActionServlet", "regenerateCorrespondences" );
                }
                 //Added for case id COEUSQA-1724 iacuc protocol stream generation end.
                //Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ;
                //Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber(), protocolSubmissionInfoBean.getCommitteeId()) ; 
//                System.out.println("Data xml - regenerating complete!") ;
                UtilFactory.log("Xml doc regenerating complete!") ;
                               
//                System.out.println("Start Pdf generation...") ;
                XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes) ;
                if (fileGenerated)
                {    
                   templateFileBytes = conv.getGeneratedPdfFileBytes() ;
                   System.out.println("Pdf generation successfull...") ;
                }
//                System.out.println("End Pdf generation...") ;   
                /*
                 *  Setting back the actual sequance number to action bean before 
                 *  updating to the database.
                 */
                actionBean.setSequenceNumber(actualSeqNumber);
                //prps end nov 03 2003
                 blnCorrGenerated = protoCorrespTypeTxnBean.UpdateProtocolCorrespondence(actionBean, protoCorrespTypeCode, templateFileBytes);

                }
            
        return blnCorrGenerated ;
    }
    
    //Bijosh
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //public boolean generateCorrespondencesWithTags(ProtocolActionsBean actionBean,byte [] templateFileBytes) 
    public boolean generateCorrespondencesWithTags(String userId,ProtocolActionsBean actionBean,byte [] templateFileBytes) //Case#4585 - End
        throws CoeusException, DBException, 
                javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException, 
                javax.xml.transform.TransformerConfigurationException, 
                javax.xml.transform.TransformerException, IOException, 
                org.apache.fop.apps.FOPException
    {
       boolean blnCorrGenerated = false;
        
        CorrespondenceTypeFormBean correpTypeFormBean = null;
        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        //Case#4585 - End
        Vector validActionCorrespTypes = null;
        validActionCorrespTypes = protoCorrespTypeTxnBean.getValidProtoActionCorrespTypes(actionBean);
        //byte[] templateFileBytes;
        File reportFile = null;
        FileOutputStream fos = null;
//        ProcessReportXMLBean bean = null;
        Vector vctReceipients = null;
        Vector vctRecp = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        Vector attachmentFilePath = new Vector();
         //Added for case id COEUSQA-1724 iacuc protocol stream generation start
        CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
        int committeeTypeCode = 0;
	  //Added for case id COEUSQA-1724 iacuc protocol stream generation end   
        if(validActionCorrespTypes != null && validActionCorrespTypes.size() > 0){
            for(int row = 0; row < validActionCorrespTypes.size(); row++ ){
                correpTypeFormBean = (CorrespondenceTypeFormBean)validActionCorrespTypes.elementAt(row);
               
                //templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), actionBean.getScheduleId());
                if(templateFileBytes!=null)
                {
                 committeeTypeCode = commTxnBean.getCommitteeType(correpTypeFormBean.getCommitteeId());
                // get committee id 
                 ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean();
                // ScheduleDetailsBean scheduleBean =  scheduleTxnBean.getScheduleDetails(actionBean.getScheduleId()) ;   
                //Added for case id COEUSQA-1724 iacuc protocol stream generation start  
                Document xmlDoc = null;
                try{
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        xmlDoc = getIACUCCorrespondenceXMLStream(actionBean);
                    }else{
                        xmlDoc = getIRBCorrespondenceXMLStream(actionBean);
                    }
                }catch(Exception e){
                     UtilFactory.log("Error generating xml. ", e, "ProtocolActionServlet", "generateCorrespondencesWithTags" );
                }
                //Added for case id COEUSQA-1724 iacuc protocol stream generation end
                //XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
                //Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ;
                //Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber(), actionBean.getCommitteeId()) ;
                
//                System.out.println("Data xml - generating complete!") ;
                UtilFactory.log("Xml doc generating complete!") ;
                               
//                System.out.println("Start Pdf generation...") ;
                XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes) ;
                if (fileGenerated)
                {    
                   templateFileBytes = conv.getGeneratedPdfFileBytes() ;
//                   System.out.println("Pdf generation successfull...") ;
                }
//                System.out.println("End Pdf generation...") ;   
                                
                //prps end nov 03 2003
                    //Code added for 4.3 Enhancement - starts
                    actionBean.setFinalFlag(correpTypeFormBean.isFinalFlag()==true?"Y" : "N");
                    //Code added for 4.3 Enhancement - ends
                    blnCorrGenerated = protoCorrespTypeTxnBean.addUpdProtocolCorrespondence(actionBean, correpTypeFormBean.getProtoCorrespTypeCode(), templateFileBytes);
                    vctReceipients = protoCorrespTypeTxnBean.getCorrespondenceReceipients(actionBean.getProtocolNumber(),actionBean.getSequenceNumber(),correpTypeFormBean.getProtoCorrespTypeCode());
                    String strMailIds = "";
                    vctRecp = new Vector();
                    if(vctReceipients!=null){
                        for(int intRecepRow = 0 ; intRecepRow < vctReceipients.size();intRecepRow++){
                            protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vctReceipients.elementAt(intRecepRow);
                            strMailIds =  strMailIds + protoCorrespRecipientsBean.getMailId()+",";
                            protoCorrespRecipientsBean.setProtocolNumber(actionBean.getProtocolNumber());
                            protoCorrespRecipientsBean.setProtoCorrespTypeCode(correpTypeFormBean.getProtoCorrespTypeCode());
                            protoCorrespRecipientsBean.setActionId(actionBean.getActionId());
                            protoCorrespRecipientsBean.setAcType("I");
                            protoCorrespRecipientsBean.setNumberOfCopies(1);
                            vctRecp.addElement(protoCorrespRecipientsBean);
                        }
                        if(vctRecp.size()>0){
                            boolean success = protoCorrespTypeTxnBean.addUpdCorrespRecipients(vctRecp);
                        }
                    }
                }
            } 
        }else{
//            System.out.println("No valid corresp Types. No correspondences generated.");
            blnCorrGenerated = false;
        }
            
        //System.out.println("Try to generate corerspondence");
        /*Hashtable inputParamValues = new Hashtable();
        inputParamValues.put("PERSON_ID", "999999900");
        inputParamValues.put("SCHEDULE_ID", "112");
        inputParamValues.put("COUNTRY_CODE", "USA");
        inputParamValues.put("STATE_CODE", "NJ");
        ProcessReportXMLBean bean = null;
        try{
            bean = new ProcessReportXMLBean("AGENDAREPORT", inputParamValues);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("file************************");
        System.out.println(bean.getPdfFile());
        System.out.println("****************************");
        System.out.println("*** All correspondences for this action code are generated ***") ;
         */
        //ActionTransaction actionTxnBean = new ActionTransaction(this.userId);
        //actionTxnBean.generateAndStoreCorresp(actionBean);
        //System.out.println("*** All correspondences for this action code are generated ***") ;
        return blnCorrGenerated ; 
    }
    
    //Bijosh
    //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
    //public boolean generateCorrespondences(ProtocolActionsBean actionBean) 
    public boolean generateCorrespondences(String userId, ProtocolActionsBean actionBean) //Case#4585 - End
        throws CoeusException, DBException, 
                javax.xml.bind.JAXBException, javax.xml.parsers.ParserConfigurationException, 
                javax.xml.transform.TransformerConfigurationException, 
                javax.xml.transform.TransformerException, IOException, 
                org.apache.fop.apps.FOPException
    {
        boolean blnCorrGenerated = false;
        
        CorrespondenceTypeFormBean correpTypeFormBean = null;
        //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
        //ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(this.userId);
        ProtoCorrespTypeTxnBean protoCorrespTypeTxnBean = new ProtoCorrespTypeTxnBean(userId);
        //Case#4585 - End
        Vector validActionCorrespTypes = null;
        validActionCorrespTypes = protoCorrespTypeTxnBean.getValidProtoActionCorrespTypes(actionBean);
        byte[] templateFileBytes;
        File reportFile = null;
        FileOutputStream fos = null;
//        ProcessReportXMLBean bean = null;
        Vector vctReceipients = null;
        Vector vctRecp = null;
        ProtoCorrespRecipientsBean protoCorrespRecipientsBean = null;
        Vector attachmentFilePath = new Vector();
         //Added for case id COEUSQA-1724 iacuc protocol stream generation start
        CommitteeTxnBean commTxnBean = new CommitteeTxnBean();
        int committeeTypeCode = 0;
        // //Added for case id COEUSQA-1724 iacuc protocol stream generation end
        if(validActionCorrespTypes != null && validActionCorrespTypes.size() > 0){
            for(int row = 0; row < validActionCorrespTypes.size(); row++ ){
                correpTypeFormBean = (CorrespondenceTypeFormBean)validActionCorrespTypes.elementAt(row);
                //Modified for the case#coeusdev-220 generate correspondence-start
                //templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), actionBean.getScheduleId());
                templateFileBytes = protoCorrespTypeTxnBean.getCorrespTemplate(correpTypeFormBean.getProtoCorrespTypeCode(), actionBean.getScheduleId(),actionBean.getCommitteeId());
                //Modified for the case#coeusdev-220 generate correspondence-end
                if(templateFileBytes!=null)
                {
                    /* This code is commented by Prps on Nov 03 2003 in order to remove iText and use JAXB n FOP
                     
                    Hashtable inputParamValues = new Hashtable();
                    //inputParamValues.put("SCHEDULE_ID", actionBean.getScheduleId());
                    inputParamValues.put("PROTOCOL_NUMBER", actionBean.getProtocolNumber());
                    inputParamValues.put("ACTION_ID", new Integer(actionBean.getActionId()));
                    inputParamValues.put("SEQUENCE_NUMBER", new Integer(actionBean.getSequenceNumber()));

                    bean = new ProcessReportXMLBean("CORRESREPORT", templateFileBytes, inputParamValues);
                    String newfileRelativeName = bean.getPdfFileName();
                    templateFileBytes = bean.getPdfFileBytes();
                    */
                    
                //prps start nov 03 2003
                 // get committee id 
                 ScheduleTxnBean scheduleTxnBean = new ScheduleTxnBean();
                // ScheduleDetailsBean scheduleBean =  scheduleTxnBean.getScheduleDetails(actionBean.getScheduleId()) ;   
                    
                // Generator here will build a xml with all possible details abt a particular Protocol
                // the template will decide which details to show or which details to be put in the pdf( or correspondence).
                // Even though xml is big the generated pdf will have only necessary data in it (determined by the xsl template)
                 
                //Added for case id COEUSQA-1724 iacuc protocol stream generation start
            
	        committeeTypeCode = commTxnBean.getCommitteeType(correpTypeFormBean.getCommitteeId());
                Document xmlDoc = null;
                try{
                    if(committeeTypeCode == CoeusConstants.IACUC_COMMITTEE_TYPE_CODE){
                        xmlDoc = getIACUCCorrespondenceXMLStream(actionBean);
                    }else{
                        xmlDoc = getIRBCorrespondenceXMLStream(actionBean);
                    }
                }catch(Exception e){
                    UtilFactory.log("Error generating xml. ", e, "ProtocolActionServlet", "generateCorrespondences" );
                }
                //Added for case id COEUSQA-1724 iacuc protocol stream generation end
               // XMLStreamGenerator xmlStreamGenerator = new XMLStreamGenerator() ;
               // Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean) ;
                //Document xmlDoc = xmlStreamGenerator.correspondenceXMLStreamGenerator(actionBean.getProtocolNumber(), actionBean.getSubmissionNumber(), actionBean.getCommitteeId()) ;
                
//                System.out.println("Data xml - generating complete!") ;
                UtilFactory.log("Xml doc generating complete!") ;
                               
//                System.out.println("Start Pdf generation...") ;
                XMLtoPDFConvertor conv = new XMLtoPDFConvertor() ;
                boolean fileGenerated = conv.generatePDF(xmlDoc, templateFileBytes) ;
                if (fileGenerated)
                {    
                   templateFileBytes = conv.getGeneratedPdfFileBytes() ;
//                   System.out.println("Pdf generation successfull...") ;
                }
//                System.out.println("End Pdf generation...") ;   
                                
                //prps end nov 03 2003
                    // Code added for 4.3 Enhancement - starts
                    actionBean.setFinalFlag(correpTypeFormBean.isFinalFlag()==true ? "Y" : "N");
                    // Code added for 4.3 Enhancement - ends
                    blnCorrGenerated = protoCorrespTypeTxnBean.addUpdProtocolCorrespondence(actionBean, correpTypeFormBean.getProtoCorrespTypeCode(), templateFileBytes);
                    vctReceipients = protoCorrespTypeTxnBean.getCorrespondenceReceipients(actionBean.getProtocolNumber(),actionBean.getSequenceNumber(),correpTypeFormBean.getProtoCorrespTypeCode());
                    String strMailIds = "";
                    vctRecp = new Vector();
                    if(vctReceipients!=null){
                        for(int intRecepRow = 0 ; intRecepRow < vctReceipients.size();intRecepRow++){
                            protoCorrespRecipientsBean = (ProtoCorrespRecipientsBean)vctReceipients.elementAt(intRecepRow);
                            strMailIds =  strMailIds + protoCorrespRecipientsBean.getMailId()+",";
                            protoCorrespRecipientsBean.setProtocolNumber(actionBean.getProtocolNumber());
                            protoCorrespRecipientsBean.setProtoCorrespTypeCode(correpTypeFormBean.getProtoCorrespTypeCode());
                            protoCorrespRecipientsBean.setActionId(actionBean.getActionId());
                            protoCorrespRecipientsBean.setAcType("I");
                            protoCorrespRecipientsBean.setNumberOfCopies(1);
                            vctRecp.addElement(protoCorrespRecipientsBean);
                        }
                        if(vctRecp.size()>0){
                            boolean success = protoCorrespTypeTxnBean.addUpdCorrespRecipients(vctRecp);
                        }
                    }
                }
            } 
        }else{
//            System.out.println("No valid corresp Types. No correspondences generated.");
            blnCorrGenerated = false;
        }
            
        //System.out.println("Try to generate corerspondence");
        /*Hashtable inputParamValues = new Hashtable();
        inputParamValues.put("PERSON_ID", "999999900");
        inputParamValues.put("SCHEDULE_ID", "112");
        inputParamValues.put("COUNTRY_CODE", "USA");
        inputParamValues.put("STATE_CODE", "NJ");
        ProcessReportXMLBean bean = null;
        try{
            bean = new ProcessReportXMLBean("AGENDAREPORT", inputParamValues);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println("file************************");
        System.out.println(bean.getPdfFile());
        System.out.println("****************************");
        System.out.println("*** All correspondences for this action code are generated ***") ;
         */
        //ActionTransaction actionTxnBean = new ActionTransaction(this.userId);
        //actionTxnBean.generateAndStoreCorresp(actionBean);
        //System.out.println("*** All correspondences for this action code are generated ***") ;
        return blnCorrGenerated ;
    }
        
    private boolean generateCorrespondences() 
    {
//        System.out.println("*** All correspondences for this action code are generated ***") ;
        
        return true ;
    }
      
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     * @return which returns the description
     */
    public String getServletInfo() {
        return "Short description";
    }
     //Added for case id COEUSQA-1724 iacuc protocol stream generation start
     private Document getIRBCorrespondenceXMLStream(final ProtocolActionsBean protoActionBean) throws Exception{
         edu.mit.coeus.utils.xml.generator.XMLStreamGenerator xmlStreamGenerator = 
                                new edu.mit.coeus.utils.xml.generator.XMLStreamGenerator() ;
         
         return xmlStreamGenerator.correspondenceXMLStreamGenerator(protoActionBean) ; 
    }
    
     
      private Document getIACUCCorrespondenceXMLStream(final ProtocolActionsBean irbProtoActionBean) throws Exception{
          edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator xmlStreamGenerator = 
                                new edu.mit.coeus.xml.iacuc.generator.XMLStreamGenerator() ;
        
          edu.mit.coeus.iacuc.bean.ProtocolActionsBean iacucProtoActionBean = 
                                                new edu.mit.coeus.iacuc.bean.ProtocolActionsBean();
        
          iacucProtoActionBean.setProtocolNumber(irbProtoActionBean.getProtocolNumber());
          iacucProtoActionBean.setSubmissionNumber(irbProtoActionBean.getSubmissionNumber());
          iacucProtoActionBean.setCommitteeId(irbProtoActionBean.getCommitteeId());
          iacucProtoActionBean.setSequenceNumber(irbProtoActionBean.getSequenceNumber());
          
          return xmlStreamGenerator.correspondenceXMLStreamGenerator(iacucProtoActionBean) ; 
    }         
  //Added for case id COEUSQA-1724 iacuc protocol stream generation end.
}
