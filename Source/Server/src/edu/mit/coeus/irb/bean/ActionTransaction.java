/*
 * ActionTransaction.java
 *
 * Created on January 16, 2003, 2:45 PM
 */

 /*
  * PMD check performed, and commented unused imports and variables on 31-MAY-2011
  * by Maharaja Palanichamy
  */

package edu.mit.coeus.irb.bean; 

import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.documenttype.DocumentTypeChecker;
import java.util.* ;
import java.sql.Timestamp;
//import java.sql.Date;
//import java.text.SimpleDateFormat ; 
import java.io.ByteArrayOutputStream;
//import java.io.FileOutputStream;


import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.exception.CoeusException ;
import edu.mit.coeus.utils.*;

//import edu.mit.coeus.report.bean.ProcessReportXMLBean;
//import edu.mit.coeus.report.exception.CoeusReportException;
//import edu.mit.coeus.utils.dbengine.ProcReqParameter;

public class ActionTransaction
{
    private int actionCode ;
    private DBEngineImpl dbEngine;
    private String userId;
    
    private java.text.SimpleDateFormat dtFormat 
        = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    private DateUtils dtUtils ; 
    
    //prps new code start

    private HashMap hashUserPromptFlag = new HashMap() ;
    private HashMap hashUserPrompt = new HashMap() ;
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    private int documentId = 1;
    //COEUSDEV-328 : End
    //prps new code ends

    // holds the dataset name
    //private static final String DSN = "Coeus";
    
    //Protocol Enhancement -  Administrative Correction Start 1
    private String strComments = new String() ;
    //Protocol Enhancement -  Administrative Correction End 1
    
    public ActionTransaction()
    {
       this(null);
    }
   
    public ActionTransaction(String userId)
    {
        this.userId = userId;
       dbEngine = new DBEngineImpl();         
    }

    public ActionTransaction(int actionCode)
    {
        dbEngine = new DBEngineImpl();
        this.actionCode = actionCode ;
    }
 
    public void setActionCode(int actionCode)
    {
        this.actionCode = actionCode ;
    }
    
    public int getActionCode()
    {
        return actionCode ;
    }
    
    public Vector getSubAction()
    {         
        Vector vecSubAction = new Vector() ;
        try
        {
            Vector param = new Vector() ;
            param.add(new Parameter("AW_ACTION_ID",
                        DBEngineConstants.TYPE_INT, new Integer(actionCode).toString()));

            //System.out.println("*** Action code recvd is : " + actionCode + " ***") ;
            
            HashMap nextNumRow = null;
            Vector result = new Vector();
            if(dbEngine!=null)
            {
                result = dbEngine.executeRequest("Coeus",
                " call get_followup_action( << AW_ACTION_ID >>, << OUT RESULTSET rset >> )",
                "Coeus", param);
                //System.out.println("*** Procedure Executed ***") ;
            }
            else{
                throw new CoeusException("db_exceptionCode.1000");
                }
            if(!result.isEmpty())
            {
                for ( int actionCount= 0 ; actionCount < result.size() ; actionCount++ )
                {    
                    // the record obtd will be sorted in descending order 
                    // when u add it to the vector it gets sorted right 
                    nextNumRow = (HashMap)result.elementAt(actionCount) ;
                    vecSubAction.add(new Integer(nextNumRow.get("FOLLOWUP_ACTION_CODE").toString())) ; 
                    
                    //prps new code starts
                    
                    // user promptflag and the prompt msg also needs to be added
                    if (nextNumRow.get("USER_PROMPT_FLAG") != null)
                    {
                        hashUserPromptFlag.put(new Integer(nextNumRow.get("FOLLOWUP_ACTION_CODE").toString()), 
                                            nextNumRow.get("USER_PROMPT_FLAG")) ;
                        
                        // add prompt only if the flag is Y (yes)
                        if (nextNumRow.get("USER_PROMPT_FLAG").toString().equalsIgnoreCase("Y"))
                        {
                            hashUserPrompt.put(new Integer(nextNumRow.get("FOLLOWUP_ACTION_CODE").toString()), 
                                            nextNumRow.get("USER_PROMPT")) ;
                        }
                    }
                    else
                    {
                        hashUserPromptFlag.put(new Integer(nextNumRow.get("FOLLOWUP_ACTION_CODE").toString()), "N") ;
                    }
                    
                    //prps new code ends
                }     
            }    
            else
            {
                //System.out.println( "******** OSP$VALID_PROTO_ACTION_ACTION table is empty ******") ;
            }    
        }catch(Exception ex)
        {
//            System.out.println("*** Ignore the following exception. This will not be thrown if u populate data to OSP$VALID_PROTO_ACTION_ACTION  *** \n                 *****   ") ;
            ex.printStackTrace() ;
//            System.out.println("               ***                 ") ;
        }
               
        return vecSubAction ;
    }
 
    /*
     * The process submit to irb when done from a protocol screen will have all the necessary details
     * available from the screen, meaning the protocol Info & submission Info bean details. But when it 
     * is done from the schedule screen there will be very less details, but u will have enough details
     * so to query the database and get the required details. So this method essentially queries the database
     * to build ProtocolInfoBean protocolInfoBean and pass it to ProtocolSubmissionUpdateTxnBean so that 
     * the submit to IRB functionality is done thro ProtocolSubmissionUpdateTxnBean.
     */
    public ProtocolInfoBean getRestOfDetailsProtocolInfoBean(ProtocolInfoBean protocolInfoBean)
    {
     //System.out.println("\n *** in getRestOfDetailsProtocolInfoBean *** \n" ) ;    

      String protocolId = protocolInfoBean.getProtocolNumber() ;
      //int sequenceId = protocolInfoBean.getSequenceNumber() ;
                  
      try
      {
        dtUtils = new DateUtils();  
        Vector param= new Vector();
        param.add(new Parameter("AW_PROTOCOL_ID",
                    DBEngineConstants.TYPE_STRING, protocolId));
                
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null)
        {
            result = dbEngine.executeRequest("Coeus",
            " call GET_PROTOCOL_INFO( << AW_PROTOCOL_ID >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }
        else{
            throw new CoeusException("db_exceptionCode.1000");
            }
        if(!result.isEmpty())
        {
            nextNumRow = (HashMap)result.elementAt(0) ;
//            System.out.println("*** Row retrieved successfully ***") ;
//            System.out.println(" 1: " + nextNumRow.get("PROTOCOL_TYPE_CODE") ) ;
//            System.out.println(" 2: " + nextNumRow.get("TITLE") ) ;
//            System.out.println(" 3: " + nextNumRow.get("APPLICATION_DATE") ) ;
//            System.out.println(" 4: " + nextNumRow.get("FDA_APPLICATION_NUMBER") ) ;
//            System.out.println(" 5: " + nextNumRow.get("CREATE_TIMESTAMP") ) ;
//            System.out.println(" 6: " + nextNumRow.get("CREATE_USER") ) ;
        
            // now u have the values required for all the columns, now u can insert the new record
           
            protocolInfoBean.setProtocolNumber(protocolId) ;
            protocolInfoBean.setSequenceNumber(Integer.parseInt(nextNumRow.get("SEQUENCE_NUMBER").toString())) ;
            protocolInfoBean.setProtocolTypeCode(Integer.parseInt(nextNumRow.get("PROTOCOL_TYPE_CODE").toString())) ;
            protocolInfoBean.setProtocolStatusCode(Integer.parseInt("2")) ; // need to put status for submitto IRB
            protocolInfoBean.setTitle(nextNumRow.get("TITLE").toString()) ;
            
            //prps start new code Apr 29 '03
            protocolInfoBean.setIsBillableFlag(nextNumRow.get("IS_BILLABLE").toString().equals("Y")? true: false) ;
            // retain the first char n make teh second char of the following fields as zero
            String tmpIndicator = nextNumRow.get("SPECIAL_REVIEW_INDICATOR").toString() ;
            tmpIndicator = tmpIndicator.charAt(0) + "0" ;
            protocolInfoBean.setSpecialReviewIndicator(tmpIndicator) ;
            tmpIndicator = nextNumRow.get("VULNERABLE_SUBJECT_INDICATOR").toString() ;
            tmpIndicator = tmpIndicator.charAt(0) + "0" ;
            protocolInfoBean.setVulnerableSubjectIndicator(tmpIndicator) ;
            //prps end
            
            
//            System.out.println("\n *** 1 *** \n" ) ;    
            if (nextNumRow.get("APPLICATION_DATE")!=null)
            {    
               String tmp = nextNumRow.get("APPLICATION_DATE").toString() ;
               String tmp2 = dtUtils.restoreDate( dtUtils.formatDate(tmp,"dd-MMM-yyyy") ,"/:-,") ;
               protocolInfoBean.setApplicationDate(new java.sql.Date(dtFormat.parse(tmp2).getTime())) ; 
            }
            else
            {
                protocolInfoBean.setApplicationDate(null) ; 
            }    
//            System.out.println("\n *** 2 *** \n" ) ;    
            if (nextNumRow.get("APPROVAL_DATE")!=null)
            {    
               String tmp = nextNumRow.get("APPROVAL_DATE").toString() ;
               String tmp2 = dtUtils.restoreDate( dtUtils.formatDate(tmp,"dd-MMM-yyyy") ,"/:-,") ;
               protocolInfoBean.setApplicationDate(new java.sql.Date(dtFormat.parse(tmp2).getTime())) ; 
            }
            else
            {
                protocolInfoBean.setApprovalDate(null) ;
            }    
//            System.out.println("\n *** 3 *** \n" ) ;    
            if (nextNumRow.get("EXPIRATION_DATE")!=null)
            {   
               String tmp = nextNumRow.get("EXPIRATION_DATE").toString() ;
               String tmp2 = dtUtils.restoreDate( dtUtils.formatDate(tmp,"dd-MMM-yyyy") ,"/:-,") ;
               protocolInfoBean.setApplicationDate(new java.sql.Date(dtFormat.parse(tmp2).getTime())) ;  
            }
            else
            {
                protocolInfoBean.setExpirationDate(null) ;
            }

//            System.out.println("\n *** 4 *** \n" ) ;    
            protocolInfoBean.setFDAApplicationNumber((String)nextNumRow.get("FDA_APPLICATION_NUMBER")) ;

//            System.out.println("\n *** 5 *** \n" ) ;    
            if (nextNumRow.get("CREATE_TIMESTAMP")!=null)
            {   
                protocolInfoBean.setCreateTimestamp(Timestamp.valueOf((nextNumRow.get("CREATE_TIMESTAMP")).toString())) ;
            }
            else
            {
                protocolInfoBean.setCreateTimestamp(null) ;
            }
//            System.out.println("\n *** 6 *** \n" ) ;    
            protocolInfoBean.setCreateUser(nextNumRow.get("CREATE_USER").toString()) ;
        }   
          
       }catch(Exception ex)
      {
        //System.out.println("***  Error in building protocol info***") ;  
        ex.printStackTrace() ;
        return null ;
      }
       
//       System.out.println("\n***  done  with protocol info***\n") ;  
       return protocolInfoBean;
    }
     
    /* Modified to return Action Id to generate Correspondences.*/
    public int performAction(ProtocolActionsBean actionBean, String loggedInUser)
        throws CoeusException,DBException
    {
        // in here to logic to fire the actual update needs to go...
        //   eg: Set OSP$PROTOCOL_SUBMISSION.SUBMISSION_STATUS_CODE = 3 (Deffered)
        //  Set Protocol status to 14 (Deffered) (osp$protocol.protocol_status_code)
        int actionId = 0;
       //try
       //{
        String protocolId = actionBean.getProtocolNumber() ; 
        int sequenceId = actionBean.getSequenceNumber() ;
        String scheduleId = actionBean.getScheduleId() ;
        String comments = actionBean.getComments() ;        
        int submissionNumber = actionBean.getSubmissionNumber();              
        String committeeId = actionBean.getCommitteeId();       

        Vector param= new Vector();
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        
        param= new Vector();
        param.add(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolId )) ;
        param.add(new Parameter("AV_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(sequenceId).toString())) ;
        param.add(new Parameter("AV_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING, scheduleId )) ;
        param.add(new Parameter("AV_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+submissionNumber)) ;
        param.add(new Parameter("AV_COMMENTS",
                    DBEngineConstants.TYPE_STRING, comments )) ;
        param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, loggedInUser )) ;		
        //For the action date field in the protocol actions table - added by Jobin - start
        param.add(new Parameter("AV_ACTION_DATE",
                    DBEngineConstants.TYPE_DATE, actionBean.getActionDate())) ; // Jobin - end
        
        //Added to include committeeId - start - 1
        param.add(new Parameter("AV_COMMITTEE_ID", DBEngineConstants.TYPE_STRING, committeeId));        
        //Added to include committeeId - end - 1
        
        
        if(dbEngine!=null)
        {
        
            java.sql.Date appDate = null;
            java.sql.Date expDate = null;
            //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
            CoeusVector cvActionDocuments = new CoeusVector();
            //COEUSDEV-328 : End
            // added the new value for the action date for the functions in the protocol_action package by Jobin
            switch (actionCode)
            {
                case 200:  //assign to agenda
                         result = dbEngine.executeFunctions("Coeus",
                                 "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_assign_to_agenda ( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>)}", param);
                         break ;
                         
                case 201: // defer
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_defer ( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >> , << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;
            
                case 204: //approve
                        appDate = actionBean.getApprovalDate() ;
                        expDate = actionBean.getExpirationDate() ;

                        param.add(new Parameter("AV_APPROVAL_DATE",
                                     DBEngineConstants.TYPE_DATE, appDate )) ;

                        param.add(new Parameter("AV_EXPIRATION_DATE",
                                     DBEngineConstants.TYPE_DATE, expDate )) ;

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_approve( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_APPROVAL_DATE >>, << AV_EXPIRATION_DATE >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);                        
                        break ;
                       
               case 202:  //Tabled
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_table( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);// Bug fix 2079
                        break ;
                   
               case 203:  //SMR Reqd
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_SMRReqd( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);//Bug fix 2079
                        break ;                        
               case 301:  //Terminate
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_TERMINATE( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                                                
               case 302:  //Suspend
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_SUSPEND( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                             
               case 305:  //Expire
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_expire( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                             
               case 306:  //Suspend by DSMB
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_suspend_by_dsmb( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                             
               case 300:  //Closed
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_CLOSE( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                                                     
               case 303:  //Withdraw
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_WITHDRAW( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                                                                             
               case 304:  //Disapprove
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_DISAPPROVE( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;                                                                             
                case 205: //Expedited approve
                        appDate = actionBean.getApprovalDate() ;
                        expDate = actionBean.getExpirationDate() ;

                        param.add(new Parameter("AV_APPROVAL_DATE",
                                     DBEngineConstants.TYPE_DATE, appDate )) ;

                        param.add(new Parameter("AV_EXPIRATION_DATE",
                                     DBEngineConstants.TYPE_DATE, expDate )) ;

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_EXPEDITEDAPPROVE( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_APPROVAL_DATE >>, << AV_EXPIRATION_DATE >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);                        
                        break ;                        
                case 206: //Grant Exempt
                        appDate = actionBean.getApprovalDate() ;
                        expDate = actionBean.getExpirationDate() ;

                        param.add(new Parameter("AV_APPROVAL_DATE",
                                     DBEngineConstants.TYPE_DATE, appDate )) ;

                        param.add(new Parameter("AV_EXPIRATION_DATE",
                                     DBEngineConstants.TYPE_DATE, expDate )) ;

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_GRANTEXEMPT( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_APPROVAL_DATE >>, << AV_EXPIRATION_DATE >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);                        
                        break ; 
                        //Added for Coeus enhancement Case id : 1880 - start
                case 210: //IRB Review not required
                        appDate = actionBean.getApprovalDate() ;
                        expDate = actionBean.getExpirationDate() ;

                        param.add(new Parameter("AV_APPROVAL_DATE",
                                     DBEngineConstants.TYPE_DATE, appDate )) ;

                        param.add(new Parameter("AV_EXPIRATION_DATE",
                                     DBEngineConstants.TYPE_DATE, expDate )) ;

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_IRB_Review_Not_Required( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_APPROVAL_DATE >>, << AV_EXPIRATION_DATE >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);                        
                        break ;    
                        // Coeus enhancement Case id : 1880 - end
                case 207: //Close for Enrollment

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_CLOSED_FOR_ENROLLMENT( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE>> )}", param);
                        break ; 
                //Added by Nadh for Response Approval Enhancement - Start
                case 208: //Response approve
                        appDate = actionBean.getApprovalDate() ;
                        expDate = actionBean.getExpirationDate() ;

                        param.add(new Parameter("AV_APPROVAL_DATE",
                                     DBEngineConstants.TYPE_DATE, appDate )) ;

                        param.add(new Parameter("AV_EXPIRATION_DATE",
                                     DBEngineConstants.TYPE_DATE, expDate )) ;

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_Response_Approve( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_APPROVAL_DATE >>, << AV_EXPIRATION_DATE >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);                        
                        break ;
                //Added by Nadh for Response Approval Enhancement - End
                //Coeus enhancement Case #1791 - step 1: start        
                case 209:
                        appDate = actionBean.getApprovalDate() ;
                        expDate = actionBean.getExpirationDate() ;

                        param.add(new Parameter("AV_APPROVAL_DATE",
                                     DBEngineConstants.TYPE_DATE, appDate )) ;

                        param.add(new Parameter("AV_EXPIRATION_DATE",
                                     DBEngineConstants.TYPE_DATE, expDate )) ;

                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_IRB_Acknowledge( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_APPROVAL_DATE >>, << AV_EXPIRATION_DATE >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);                        
                        break ;
                  //Coeus enhancement Case #1791 - step 1: end
                case 104: //Request for Termination
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_REQUEST_FOR_TERMINATION( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;    
                //Modified to include committeeId - start        
                case 106: //Request for Suspension                    
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_REQUEST_FOR_SUSPENSION( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>, << AV_COMMITTEE_ID >> )}", param);
                        //Added for COEUSDEV-327 : IRB - Ability to upload attachments while requesting to close, close enrolment, suspension etc- Start
                        cvActionDocuments = actionBean.getActionDocuments();
                        saveActionDocuments(cvActionDocuments,actionBean);
                        //COEUSDEV-327 : End
                        break;
                case 105: //Request to Close
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_REQUEST_TO_CLOSE( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>, << AV_COMMITTEE_ID >> )}", param);
                        //Added for COEUSDEV-327 : IRB - Ability to upload attachments while requesting to close, close enrolment, suspension etc- Start
                        cvActionDocuments = actionBean.getActionDocuments();
                        saveActionDocuments(cvActionDocuments,actionBean);
                        //COEUSDEV-327 : End
                        break;                        
                case 108: //Request to Close Enrollment
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_REQUEST_TO_CLOSE_ENROLLMENT( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>, << AV_COMMITTEE_ID >> )}", param);
                        //Added for COEUSDEV-327 : IRB - Ability to upload attachments while requesting to close, close enrolment, suspension etc- Start
                        cvActionDocuments = actionBean.getActionDocuments();
                        saveActionDocuments(cvActionDocuments,actionBean);
                        //COEUSDEV-327 : End
                        break;
                //Modified to include committeeId - end            
                case 109: //Assign to Schedule
                        param.add(new Parameter("AV_COMMITTEE_ID",
                                     DBEngineConstants.TYPE_STRING, committeeId));
                    
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_ASSIGN_TO_SCHEDULE( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >> , << AV_COMMITTEE_ID >>, << AV_ACTION_DATE >>)}", param);
                        break;
                        
                //Added for performing request actions through lite - start     
                case 114://Request for Data Analysis       
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_REQUEST_FOR_DATA_ANALYSIS( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>, << AV_COMMITTEE_ID >> )}", param);
                        //Added for COEUSDEV-327 : IRB - Ability to upload attachments while requesting to close, close enrolment, suspension etc- Start
                        cvActionDocuments = actionBean.getActionDocuments();
                        saveActionDocuments(cvActionDocuments,actionBean);
                        //COEUSDEV-327 : End
                        break;
                case 115://Request to re open enrollment
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_REQUEST_TO_REOPEN_ENROLL( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>, << AV_COMMITTEE_ID >> )}", param);
                        //Added for COEUSDEV-327 : IRB - Ability to upload attachments while requesting to close, close enrolment, suspension etc- Start
                        cvActionDocuments = actionBean.getActionDocuments();
                        saveActionDocuments(cvActionDocuments,actionBean);
                        //COEUSDEV-327 : End
                        break;                     
                case 116://Notify IRB
                        //Code added for Case#3554 - Notify IRB enhancement - starts
                        param.add(new Parameter("AV_SUBMISSION_TYPE_CODE", 
                                DBEngineConstants.TYPE_STRING, actionBean.getSubmissionTypeCode()));        
                        param.add(new Parameter("AV_REVIEW_TYPE_CODE", 
                                DBEngineConstants.TYPE_STRING, actionBean.getReviwerTypeCode()));
                        //Code added for Case#3554 - Notify IRB enhancement - ends
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_NOTIFY_IRB( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SUBMISSION_TYPE_CODE >>, << AV_REVIEW_TYPE_CODE >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >>, << AV_COMMITTEE_ID >> )}", param);
                        //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
                        //Modified to save multiple documents from the action
                        //Added for case#3046 - Notify IRB attachments - start
                        cvActionDocuments = actionBean.getActionDocuments();
                        saveActionDocuments(cvActionDocuments,actionBean);
//                        if(actionBean.getFileBytes() != null){
//                            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
//                            ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();        
//                            protocolActionDocumentBean.setProtocolNumber(actionBean.getProtocolNumber());
//                            protocolActionDocumentBean.setSequenceNumber(actionBean.getSequenceNumber());
//                            protocolActionDocumentBean.setSubmissionNumber(
//                                    protocolSubmissionTxnBean.getMaxSubmissionNumber(actionBean.getProtocolNumber()));
//                            protocolActionDocumentBean.setFileName(actionBean.getFileName());
//                            protocolActionDocumentBean.setFileBytes(actionBean.getFileBytes());       
//                            protocolActionDocumentBean.setMimeType(actionBean.getMimeType());//case 4007
//                            protocolActionDocumentBean.setUpdateUser(loggedInUser);  
//                            protocolActionDocumentBean.setAcType(TypeConstants.INSERT_RECORD);
//                            protocolSubmissionTxnBean.addUpdProtocolActionDocument(protocolActionDocumentBean);                   
//                        }
                        //Added for case#3046 - Notify IRB attachments - end
                        //COEUSDEV-328 : End
                        break;
                //Added for performing request actions through lite - end
                // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_start
                case 119:  //Abandon
                        result = dbEngine.executeFunctions("Coeus",
                               "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_abandon( "
                               +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break ;
                 // Added for COEUSQA-2692_Allow an investigator to abandon an IRB protocol that has never been approved_end
                //Added for performing non request actions through swing - start
                case 211: //DATA ANALYSIS ONLY
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_data_analysis_only( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break;
                case 212: //REOPEN ENROLLMENT
                        result = dbEngine.executeFunctions("Coeus",
                                "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_re_open_Enrollment( "
                                +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, << AV_ACTION_DATE >> )}", param);
                        break;                        
                //Added for performing non request actions through swing - end        
            }

        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);            
            actionId = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
        }
        //Modified for case 2176 - Protocol risk level -start
        // 4183:Risk level for Full review protocols - Start
//        //If action is reponse approval or expedeted approval save the risk levels
//        if(actionCode == 208 || actionCode == 205){
        if(actionCode == 208 || actionCode == 205 || actionCode == 204){
            // if the Action code is reponse approval or expedeted approval or full board approval from committe
        // 4183:Risk level for Full review protocols - End
            addUpdRiskLevels(actionBean, loggedInUser);
        }
        //Modified for case 2176 - Protocol risk level -end
      /*}catch(Exception ex)
      {
        ex.printStackTrace() ;
        //return false ;
      }*/
       
       //return true ;
        return actionId;
    }
    /** This method is to perform all request actions
     * @param actionBean ProtocolActionsBean
     * @param loggedInUser String
     * @return boolean
     */
    public int performRequestActions(ProtocolActionsBean actionBean, String loggedInUser)
        throws CoeusException, DBException
    {
        // in here to logic to fire the actual update needs to go...
        //   eg: Set OSP$PROTOCOL_SUBMISSION.SUBMISSION_STATUS_CODE = 3 (Deffered)
        //  Set Protocol status to 14 (Deffered) (osp$protocol.protocol_status_code)
        int intActionId = 0; 
       //try
       //{
        String protocolId = actionBean.getProtocolNumber() ; 
        //int sequenceId = actionBean.getSequenceNumber() ;
        //String scheduleId = actionBean.getScheduleId() ;
        //String comments = actionBean.getComments() ;
        //Integer submissionNumber = null ;
        //boolean success = false;
        
        //Vector param= new Vector();
        
        //HashMap nextNumRow = null;
        //Vector result = new Vector();
        
        ProtocolInfoBean protocolInfoBean = null; 
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = null;        
        ProtocolSubmissionUpdateTxnBean protocolSubmissionUpdateTxnBean = null;        
        ProtocolDataTxnBean protocolDataTxnBean = null;
        if(dbEngine!=null)
        {
            switch (actionCode)
            {
                case 104: //Request to Termination
                    protocolDataTxnBean = new ProtocolDataTxnBean();
                    protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolId);
                    //Need not Update ProtocolInfo on 06 Oct, 2003 - start
                    //protocolInfoBean.setSequenceNumber(protocolInfoBean.getSequenceNumber()+1);
                    //protocolInfoBean.setAcType("I");
                    //Need not Update ProtocolInfo on 06 Oct, 2003 - end
                    protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolId);    
                    //Need not Update sequence number on 06 Oct, 2003 - start
                    //protocolSubmissionInfoBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                    //Need not Update sequence number on 06 Oct, 2003 - end
                    protocolSubmissionInfoBean.setSubmissionTypeCode(108);
                    //Added by Prasanna 29th Sep 2003 - start
                    //For Request Actions there won't be any schedule or committee
                    protocolSubmissionInfoBean.setScheduleId(null);
                    protocolSubmissionInfoBean.setCommitteeId(null);
                    protocolSubmissionInfoBean.setCommitteeName(null);
                    //Added by Prasanna - end
                    protocolSubmissionInfoBean.setAcType("I");
                    protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(loggedInUser);
                    //Changed on 06 Oct, 2003 - start
                    //intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean, protocolInfoBean);
                    intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean);
                    //Changed on 06 Oct, 2003 - end
                    break;
                case 105: //Request to Close
                    protocolDataTxnBean = new ProtocolDataTxnBean();
                    protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolId);
                    //Need not Update ProtocolInfo on 06 Oct, 2003 - start
                    //protocolInfoBean.setSequenceNumber(protocolInfoBean.getSequenceNumber()+1);
                    //protocolInfoBean.setAcType("I");
                    //Need not Update ProtocolInfo on 06 Oct, 2003 - end
                    protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolId);    
                    //Need not Update sequence on 06 Oct, 2003 - start
                    //protocolSubmissionInfoBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                    //Need not Update sequence on 06 Oct, 2003 - end
                    protocolSubmissionInfoBean.setSubmissionTypeCode(109);
                    //Added by Prasanna 29th Sep 2003 - start
                    //For Request Actions there won't be any schedule or committee
                    protocolSubmissionInfoBean.setScheduleId(null);
                    protocolSubmissionInfoBean.setCommitteeId(null);
                    protocolSubmissionInfoBean.setCommitteeName(null);
                    //Added by Prasanna - end                    
                    protocolSubmissionInfoBean.setAcType("I");
                    protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(loggedInUser);
                    //Changed on 06 Oct, 2003 - start
                    //intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean, protocolInfoBean);                    
                    intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean);                    
                    //Changed on 06 Oct, 2003 - end
                    break;
                case 106: //Request to Suspension
                    protocolDataTxnBean = new ProtocolDataTxnBean();
                    protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolId);
                    //Need not Update ProtocolInfo on 06 Oct, 2003 - start
                    //protocolInfoBean.setSequenceNumber(protocolInfoBean.getSequenceNumber()+1);
                    //protocolInfoBean.setAcType("I");
                    //Need not Update ProtocolInfo on 06 Oct, 2003 - end
                    protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolId);    
                    //Need not Update sequence on 06 Oct, 2003 - start
                    //protocolSubmissionInfoBean.setSequenceNumber(protocolInfoBean.getSequenceNumber());
                    //Need not Update sequence on 06 Oct, 2003 - end
                    protocolSubmissionInfoBean.setSubmissionTypeCode(110);
                    //Added by Prasanna 29th Sep 2003 - start
                    //For Request Actions there won't be any schedule or committee
                    protocolSubmissionInfoBean.setScheduleId(null);
                    protocolSubmissionInfoBean.setCommitteeId(null);
                    protocolSubmissionInfoBean.setCommitteeName(null);
                    //Added by Prasanna - end
                    protocolSubmissionInfoBean.setAcType("I");
                    protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(loggedInUser);
                    //Changed on 06 Oct, 2003 - start
                    //intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean, protocolInfoBean);                                        
                    intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean);                                        
                    //Changed on 06 Oct, 2003 - end
                    break;
                case 108: //Request to Close Enrollment
                    protocolDataTxnBean = new ProtocolDataTxnBean();
                    protocolInfoBean = protocolDataTxnBean.getProtocolInfo(protocolId);
                    protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                    protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails(protocolId);    
                    protocolSubmissionInfoBean.setSubmissionTypeCode(112);
                    protocolSubmissionInfoBean.setScheduleId(null);
                    protocolSubmissionInfoBean.setCommitteeId(null);
                    protocolSubmissionInfoBean.setCommitteeName(null);
                    protocolSubmissionInfoBean.setAcType("I");
                    protocolSubmissionUpdateTxnBean = new ProtocolSubmissionUpdateTxnBean(loggedInUser);
                    intActionId = protocolSubmissionUpdateTxnBean.addUpdProtocolSubmissionForRequest(protocolSubmissionInfoBean);                                        
                    break;                    
            }

        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
      /*}catch(Exception ex)
      {
        ex.printStackTrace() ;
      }*/
       return intActionId ;
    }
    
/*
 * Created by Geo on  26-Mar-2003 4:53PM
 */    
 /**
  * The method used to get the correspondence type code and associated committees,
  *  which will be used to generate correspondence when an action occures. This
  * method will get invoked from the ProtocolActionServlet. 
  * <li> The procedure used to fetch the data is <code>GET_PROTO_CORRESP_FOR_ACTION</code>
  *
  * @param ProtocolActionBean has all the required parameters to fetch the correspondence type
  * and committee.
  * @throws DBException
  * @throws CoeusException
  */
  
    private Vector getProtoCorresCommitteeForAction(ProtocolActionsBean actionBean) 
                                            throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        StringBuffer strCorrBfr = new StringBuffer("");
        strCorrBfr.append("call GET_PROTO_CORRESP_FOR_ACTION( ");
        strCorrBfr.append("<<PROTOCOL_NUMBER>> , ");
        strCorrBfr.append("<<SEQUENCE_NUMBER>> , ");
        strCorrBfr.append("<<ACTION_ID>> , ");
        strCorrBfr.append("<<PROTOCOL_ACTION_TYPE_CODE>> , ");
        strCorrBfr.append("<<SCHEDULE_ID>> , ");
        strCorrBfr.append("<<OUT RESULTSET rset>> )");
        param.addElement(new Parameter("PROTOCOL_NUMBER",DBEngineConstants.TYPE_STRING,
                                actionBean.getProtocolNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",DBEngineConstants.TYPE_INT,
                                ""+actionBean.getSequenceNumber()));
        param.addElement(new Parameter("ACTION_ID",DBEngineConstants.TYPE_INT,
                                ""+actionBean.getActionId()));
        param.addElement(new Parameter("PROTOCOL_ACTION_TYPE_CODE",DBEngineConstants.TYPE_INT,
                                ""+actionBean.getActionTypeCode()));
        param.addElement(new Parameter("SCHEDULE_ID",DBEngineConstants.TYPE_INT,
                                ""+actionBean.getScheduleId()));
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",strCorrBfr.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        
        //System.out.println("list size of corresp=>"+listSize);
        //System.out.println("proto number=>"+actionBean.getProtocolNumber());
        //System.out.println("seq number=>"+actionBean.getSequenceNumber());
        //System.out.println("action id=>"+actionBean.getActionId());
        //System.out.println("action type code=>"+actionBean.getActionTypeCode());
        //System.out.println("schedule id=>"+actionBean.getScheduleId());
        
        Vector correspListForAction = new Vector(3,2);
        CorrespInfoForActionBean infoForAction = null;
        HashMap correspActionRow = null;
        for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            infoForAction = new CorrespInfoForActionBean();
            correspActionRow = (HashMap)result.elementAt(rowIndex);
            infoForAction.setCorrespTypeCode(Integer.parseInt(
                      correspActionRow.get("PROTOCOL_ACTION_TYPE_CODE").toString()));
            infoForAction.setCommitteeId((String)
                      correspActionRow.get("COMMITTEE_ID").toString());
            infoForAction.setScheduleId(Integer.parseInt(
                      correspActionRow.get("SCHEDULE_ID").toString()));
            
            correspListForAction.add(infoForAction);
        }
        return correspListForAction;
    }
    
    private CorrespInfoForActionBean getCorresTemplForAction(int protoCorrespTypeCode,String committeeId) 
                                            throws CoeusException,DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        StringBuffer strCorrBfr = new StringBuffer("");
        strCorrBfr.append("call GET_CORRESPONDENCE_TEMPLATE( ");
        strCorrBfr.append("<<CORRESPONDENT_TYPE_CODE>> , ");
        strCorrBfr.append("<<COMMITTEE_ID>> )");
        param.addElement(new Parameter("CORRESPONDENT_TYPE_CODE",DBEngineConstants.TYPE_INT,
                                ""+protoCorrespTypeCode));
        param.addElement(new Parameter("COMMITTEE_ID",DBEngineConstants.TYPE_STRING,
                                committeeId));
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",strCorrBfr.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        //int listSize = result.size();
        CorrespInfoForActionBean infoForAction = null;
        HashMap correspActionRow = null;
        if(!result.isEmpty()){
            infoForAction = new CorrespInfoForActionBean();
            correspActionRow = (HashMap)result.elementAt(0);
            infoForAction.setCorrespTypeCode(Integer.parseInt(
                      correspActionRow.get("CORRESPONDENT_TYPE_CODE").toString()));
            infoForAction.setCommitteeId((String)
                      correspActionRow.get("COMMITTEE_ID"));
            infoForAction.setTemplate((ByteArrayOutputStream)
                      correspActionRow.get("CORESPONDENCE_TEMPLATE"));
        }
        return infoForAction;
    }

//    public synchronized void generateAndStoreCorresp(ProtocolActionsBean actionBean)
//            throws CoeusException,DBException,CoeusReportException{
//        Vector correspList = getProtoCorresCommitteeForAction(actionBean);
//        int listSize = correspList.size();
//        for(int corresIndex = 0;corresIndex<listSize;corresIndex++){
//            CorrespInfoForActionBean corresInfoBean = 
//                (CorrespInfoForActionBean)correspList.elementAt(corresIndex);
//            CorrespInfoForActionBean actionTmplBean  = getCorresTemplForAction(
//                corresInfoBean.getCorrespTypeCode(),corresInfoBean.getCommitteeId());
//            ByteArrayOutputStream correspTemplate = 
//                               (ByteArrayOutputStream)actionTmplBean.getTemplate();
//            byte[] buffer = correspTemplate.toByteArray();
//            Hashtable inputParamValues = new Hashtable();
//            inputParamValues.put("PERSON_ID", "999999900");
//            inputParamValues.put("SCHEDULE_ID", "112");
//            inputParamValues.put("COUNTRY_CODE", "USA");
//            inputParamValues.put("STATE_CODE", "NJ");
//
//            ProcessReportXMLBean bean = new ProcessReportXMLBean("AGENDAREPORT", 
//                        buffer, inputParamValues);
//            System.out.println("file is**************************");
//            System.out.println(bean.getPdfFile().toString());
//            System.out.println("**************************");
//            FileOutputStream pdfFileOutStream = bean.getPdfFile();
//            byte[] pdfFileBytes = bean.getPdfFileBytes();
//
//            /*
//             *  Store pdf file into database
//             */            
//            CoeusFunctions coeusFunctions = new CoeusFunctions();
//            java.sql.Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
//            /*Test  - Insert BLOB data */
//            String insertStmt = "insert into OSP$PROTOCOL_CORRESPONDENCE( PROTOCOL_NUMBER , "+
//                        "SEQUENCE_NUMBER , ACTION_ID, PROTO_CORRESP_TYPE_CODE) "+
//                        "UPDATE_TIMESTAMP, UPDATE_USER ) "+
//                        "values ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>> , "+
//                        "<<ACTION_ID>>, <<PROTO_CORRESP_TYPE_CODE>> )"+
//                        "<<UPDATE_TIMESTAMP>>, <<UPDATE_USER>> )";
//            Vector param = new Vector();
//            Vector resultsU = new Vector();
//            param.addElement(new Parameter("PROTOCOL_NUMBER", "String", actionBean.getProtocolNumber()));
//            param.addElement(new Parameter("SEQUENCE_NUMBER", "int", ""+actionBean.getSequenceNumber()));
//            param.addElement(new Parameter("ACTION_ID", "int", ""+actionBean.getActionId()));
//            param.addElement(new Parameter("PROTO_CORRESP_TYPE_CODE", "int", ""+corresInfoBean.getCorrespTypeCode()));
//            param.addElement(new Parameter("UPDATE_TIMESTAMP", "Timestamp", dbTimestamp));
//            param.addElement(new Parameter("UPDATE_USER", "String", this.userId));
//
//            String sltQuery = "Select OSP$PROTOCOL_CORRESPONDENCE from CORRESPONDENCE where "+
//                            "UPDATE_TIMESTAMP = ? for update";
//            System.out.println("slt qry=>"+sltQuery);
//            if(dbEngine!=null)
//            {   
//                boolean status = dbEngine.insertBlob("Coeus", insertStmt, 
//                    param,sltQuery,pdfFileBytes,dbTimestamp);
//                System.out.println("status=>"+status);
//            }
//            else
//            {   
//                throw new DBException("error.database.instance.null");
//            }
//            
//            
//        }
//    }
//    
    
 //prps new code start

    public HashMap getHashUserPromptFlag()
    {
        return hashUserPromptFlag ;
    }
    
    
    public void setHashUserPromptFlag(HashMap hashUserPromptFlag)
    {
        this.hashUserPromptFlag = hashUserPromptFlag ;
    }
    
    public HashMap getHashUserPrompt()
    {
        return hashUserPrompt ;
    }
    
    
    public void setHashUserPrompt(HashMap hashUserPrompt)
    {
        this.hashUserPrompt = hashUserPrompt ;
    }


    public int performStatusChangeValidation(ProtocolActionsBean actionBean)
    throws CoeusException, DBException{
    int actionId = actionBean.getActionTypeCode() ;
    String protocolId = actionBean.getProtocolNumber() ;
    int sequenceId = actionBean.getSequenceNumber() ;
    String scheduleId = actionBean.getScheduleId() ;    
    int submissionNumber = actionBean.getSubmissionNumber() ;
    
    int success = 0 ; 
     try
       {
       // get the new seq id and do the updation 
        Vector param= new Vector();
               
        HashMap nextNumRow = null;
        Vector result = new Vector();
        
        
        param= new Vector();

        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolId )) ;
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(sequenceId).toString())) ;
        param.add(new Parameter("AS_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING, scheduleId )) ;
        param.add(new Parameter("AI_PROTOCOL_ACTION_TYPE_CODE",
                    DBEngineConstants.TYPE_STRING, new Integer(actionId).toString() )) ;
        //prps start - jul 16 2003
        param.add(new Parameter("AS_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(submissionNumber).toString())) ;
        
        //prps end
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SUCCESS>> = call FN_CAN_PERFORM_PROTOCOL_ACTION( "
            + " << AS_PROTOCOL_NUMBER >> , << AS_SEQUENCE_NUMBER >> , << AS_SCHEDULE_ID >> , << AI_PROTOCOL_ACTION_TYPE_CODE >> , << AS_SUBMISSION_NUMBER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            System.out.println("** Server side validation returned " + success + "**") ;
            
        }
    
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
     }
      
     return success ;
    }
    
    //Added by Vyjayanthi - 11/08/2004 for IRB Enhancement
    /** To check if Administrative Correction can be performed on the given protocol
     * @param protocolNumber holds the protocol number
     */
    public int performAdministrativeCorrection(String protocolNumber) {
        int success = 0 ;
        try {
            Vector param= new Vector();
            
            HashMap nextNumRow = null;
            Vector result = new Vector();
            
            param= new Vector();
            
            param.add(new Parameter("AS_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call FN_CAN_PERFM_PROTO_ADMIN_CORR( "
                + " << AS_PROTOCOL_NUMBER >>)}", param) ;
            }else{
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()){
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            }            
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }        
        return success ;
    }
    
    public int performRequestValidation(ProtocolActionsBean actionBean )
        throws CoeusException, DBException
    {
        int actionId = actionBean.getActionTypeCode() ;
        String protocolId = actionBean.getProtocolNumber() ;
        int sequenceId = actionBean.getSequenceNumber() ;
        //String scheduleId = actionBean.getScheduleId() ;        
        //int submissionNumber = actionBean.getSubmissionNumber() ;        
        int success = 0 ; 
    
       // get the new seq id and do the updation 
        Vector param= new Vector();
               
        HashMap nextNumRow = null;
        Vector result = new Vector();
        
        
        param= new Vector();

        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolId )) ;
        param.add(new Parameter("AS_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(sequenceId).toString())) ;
        param.add(new Parameter("AI_PROTOCOL_ACTION_TYPE_CODE",
                    DBEngineConstants.TYPE_STRING, new Integer(actionId).toString() )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SUCCESS>> = call FN_CAN_PERFORM_REQUEST_ACTION( "
            + " << AS_PROTOCOL_NUMBER >> , << AS_SEQUENCE_NUMBER >> , << AI_PROTOCOL_ACTION_TYPE_CODE >>  )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            //System.out.println("** Server side validation returned " + success + "**") ;
        }    
        return success ;
    }    
    
    // this method is called when protocol is created. Rest of the actions 
    // will call performAction method (above) which will take care of it
    public int logStatusChangeToProtocolAction(String protocolId, int sequenceId, Integer submissionNumber, String loggedInUser)
    {
        
      //System.out.println("*** inside logStatusChangeToProtocolAction ") ;  
      // action code = 1 for protocol creation & 2 for submit to IRB
      
      //Protocol Enhancement -  Administrative Correction Start 2
      //String strComments = new String() ;
      //Protocol Enhancement -  Administrative Correction End 2
      
      //Set Appropriate comments.
      if (actionCode == 100)
      {
        strComments = "Protocol Created" ; 
      }else if(actionCode == 101){    
        strComments = "Submit to IRB" ;
      }else if(actionCode == 102){
          strComments = "Renewal Created" ;
      }else if(actionCode == 103){
          strComments = "Amendment Created" ;
      }else if(actionCode == 104){
          strComments = "Request for Termination" ;
      }else if(actionCode == 105){
          strComments = "Request to Close" ;
      }else if(actionCode == 106){
          strComments = "Request for Suspension" ;
      }else if(actionCode == 108){
          strComments = "Request to Close Enrollment" ;
      }
      /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -start*/
      else if(actionCode == 117){
          strComments = "Renewal with Amendment Created" ;
      }
      /*Added for case#4278-In Comments on Action Details, indicate Renewal with Amendment for submissions created via New Am -end*/
      //COEUSQA-1433 - Allow Recall from Routing - Start
      else if(actionCode == 123){
          strComments = "Submission Recalled" ;
      }
      //COEUSQA-1433 - Allow Recall from Routing - End
      int success = -1 ;
      try
       {
       // get the new seq id and do the updation 
        Vector param= new Vector();
               
        HashMap nextNumRow = null;
        Vector result = new Vector();
         
        param= new Vector();

        param.add(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolId )) ;
        param.add(new Parameter("AV_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, new Integer(sequenceId).toString())) ;
        param.add(new Parameter("AV_PROTOCOL_ACTION_TYPE_CODE",
                    DBEngineConstants.TYPE_INT, new Integer(actionCode).toString())) ;
        param.add(new Parameter("AV_COMMENTS",
                    DBEngineConstants.TYPE_STRING, strComments )) ;
        if (submissionNumber == null)
        {   
            param.add(new Parameter("AV_SUBMISSION_NUMBER",
                            DBEngineConstants.TYPE_STRING, null)) ;
        }
        else
        {
            param.add(new Parameter("AV_SUBMISSION_NUMBER",
                            DBEngineConstants.TYPE_INT, submissionNumber.toString())) ;
        }
        param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, loggedInUser ));
		
        //System.out.println("*** database insert ***") ;
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.fn_log_protocol_action( "
            + " << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> , << AV_PROTOCOL_ACTION_TYPE_CODE >> , << AV_COMMENTS >>, << AV_SUBMISSION_NUMBER >> , << AV_UPDATE_USER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
            //System.out.println("** Server side validation returned " + success + "**") ;
            
        }
    
     }
     catch(Exception ex)
     {
        ex.printStackTrace() ;
     }
       
     return success ;  
        
    }
    
    
 //prps new code ends   
	
	/** Added overloaded method to Perform Action.
     *  This method is used to Update a set of Action Beans.
     * @param actionBean Vector
     * @param loggedInUser String
     * @throws CoeusException 
     * @throws DBException 
     * @return boolean
     */    
    public int performOtherActions(ProtocolActionsBean protocolActionsBean, String loggedInUser)
        throws CoeusException,DBException
    {
        int actionId = 0;

        Vector param= new Vector();
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        //StringBuffer sqlBuffer = null;
        //ProcReqParameter procReqParameter = new ProcReqParameter();
        //Vector procedures = new Vector(5,3);
        
        param= new Vector();
        param.add(new Parameter("AV_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolActionsBean.getProtocolNumber())) ;
        param.add(new Parameter("AV_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+protocolActionsBean.getSequenceNumber())) ;
        param.add(new Parameter("AV_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING, protocolActionsBean.getScheduleId())) ;
        param.add(new Parameter("AV_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT, ""+protocolActionsBean.getSubmissionNumber())) ;
        param.add(new Parameter("AV_COMMENTS",
                    DBEngineConstants.TYPE_STRING, protocolActionsBean.getComments())) ;
        param.add(new Parameter("AV_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, loggedInUser )) ;            
        param.add(new Parameter("AV_COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING, protocolActionsBean.getCommitteeId())) ; 
		
		// getting the action date and passing - Added by Jobin - start
		param.add(new Parameter("AV_ACTION_DATE",
                    DBEngineConstants.TYPE_DATE, protocolActionsBean.getActionDate())); //Jobin - end 
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
                    "{<<OUT INTEGER SUCCESS>> = call pkg_protocol_actions.FN_ASSIGN_TO_SCHEDULE( "
                    +" << AV_PROTOCOL_NUMBER >> , << AV_SEQUENCE_NUMBER >> ,<< AV_SCHEDULE_ID >>, << AV_SUBMISSION_NUMBER >>, << AV_COMMENTS >>, << AV_UPDATE_USER >>, <<AV_COMMITTEE_ID>>, << AV_ACTION_DATE >> )}", param);
            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        actionId = 0;
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            actionId = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
        }    
        return actionId ;
    }
    
    //prps start feb 05 2004
    /*
     * This method will return a committeeId of a committee to which the protocol was last submitted. 
     * 
     */
    
    public String getLastSubmitCommitteeForProtocol(String protocolId) throws CoeusException, DBException
    {
        String committeeId = null ; 
    
       // get the new seq id and do the updation 
        Vector param= new Vector();
               
        HashMap nextNumRow = null;
        Vector result = new Vector();
        
        
        param= new Vector();

        param.add(new Parameter("AS_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING, protocolId )) ;
        
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT STRING COMMITTEE_ID>> = call FN_GET_PROTO_LAST_APPR_COMM( "
            + " << AS_PROTOCOL_NUMBER >> )}", param) ;
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            if (nextNumRow.get("COMMITTEE_ID") != null)
            {
                committeeId = nextNumRow.get("COMMITTEE_ID").toString() ;
            }
            
        }    
        return committeeId ;
    
    }
    
  //prps end feb 05 2004
	
	// Added by Jobin for the Undo last action
	/**
	 * Calls the function FN_CAN_UNDO_PROTO_LAST_ACTION and gets the status 
	 * for the undo last action
	 * @param protocolNumber String
	 * @param userId String
	 * @return int
	 */
	public int canUndoLastAction(String protocolNumber, String userId) 
                    throws CoeusException, DBException{
            int success = -1 ;
            Vector param= new Vector();
            HashMap nextNumRow = null;
            Vector result = new Vector();

            param= new Vector();

            param.add(new Parameter("AS_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            param.add(new Parameter("AS_USER_ID",
            DBEngineConstants.TYPE_STRING, userId )) ;

            if (dbEngine!=null) {
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call FN_CAN_UNDO_PROTO_LAST_ACTION ( "
                + " << AS_PROTOCOL_NUMBER >> , << AS_USER_ID >> )}", param) ;
            } else {
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()) {
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
                //System.out.println("** Server side validation returned " + success + "**") ;

            }
            return success ;
	}
	
	// Added by Jobin for the Undo last action
	/**
	 * Calls the function fn_undo_last_action and performs 
	 * the undo last action
	 * @return int
	 */
	public int undoLastAction(String protocolNumber, String userId) 
                    throws CoeusException,DBException{
            int success = -1 ;
            Vector param= new Vector();
            HashMap nextNumRow = null;
            Vector result = new Vector();
            
            param= new Vector();
            param.add(new Parameter("AV_PROTOCOL_NUMBER",
            DBEngineConstants.TYPE_STRING, protocolNumber )) ;
            param.add(new Parameter("AV_USER_ID",
            DBEngineConstants.TYPE_STRING, userId )) ;
            
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER SUCCESS>> = call PKG_PROTOCOL_ACTIONS.fn_undo_last_action( "
                + " << AV_PROTOCOL_NUMBER >> , << AV_USER_ID >> )}", param) ;
            } else {
                throw new CoeusException("db_exceptionCode.1000") ;
            }
            if(!result.isEmpty()) {
                nextNumRow = (HashMap)result.elementAt(0);
                success = Integer.parseInt(nextNumRow.get("SUCCESS").toString());
//                System.out.println("** Server side validation returned " + success + "**") ;
            }
            
            return success ;
	}

        /*
         *  Getting the actions for the committe action combo box in the schedule
         * deails.
         */
    public HashMap getCommActions() throws CoeusException, DBException{
        Vector result = new Vector(3,2);
        Vector param= new Vector();
        StringBuffer strCorrBfr = new StringBuffer("");
        strCorrBfr.append("call GET_ACTIONS_FOR_COMM( ");
        strCorrBfr.append("<<OUT RESULTSET rset>> )");
        if(dbEngine != null){
            result = dbEngine.executeRequest("Coeus",strCorrBfr.toString(),"Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        HashMap commActions = new HashMap();
        HashMap actionRow = null;
        for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            actionRow = (HashMap)result.elementAt(rowIndex);
            commActions.put(actionRow.get("PROTOCOL_ACTION_TYPE_CODE").toString(), 
                            actionRow.get("DESCRIPTION").toString());
        }
        return commActions;
    }
    
    //Protocol Enhancement -  Administrative Correction Start 3
    /**
     * Getter for property strComments.
     * @return Value of property strComments.
     */
    public java.lang.String getStrComments() {
        return strComments;
    }    

    /**
     * Setter for prope4rty strComments.
     * @param strComments New value of property strComments.
     */
    public void setStrComments(java.lang.String strComments) {
        this.strComments = strComments;
    }
    //Protocol Enhancement -  Administrative Correction End 3

    /**
     * //Added for performing request actions through swing - start
     * This method gets submission details for a given protocol
     * @param protocolNumber
     * @throws CoeusException
     * @throws DBException
     * @return submissionDetails
     */
    public Vector getSubmissionDetails(String protocolNumber) 
    throws CoeusException, DBException{
        
        Vector param= new Vector();
        //HashMap nextNumRow = null;
        //Vector submissionDetails = new Vector();
        Vector result = new Vector();
        param= new Vector();
        param.add(new Parameter("AW_PROTOCOL_NUMBER", DBEngineConstants.TYPE_STRING, protocolNumber));        

        if (dbEngine!=null) {
            result = dbEngine.executeRequest("Coeus",
            " call GET_PROTO_SUBMISSION_DETAIL( << AW_PROTOCOL_NUMBER >>, <<OUT RESULTSET rset>> )",
            "Coeus", param);            
        } else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()) {
            return result;
        }
        return null;
    }    
    //Added for performing request actions through lite - end
    //Added for case 2176 - Protocol risk level -start
    /**
     * Add/Updates the protocol risk level to the database
     *
     * @param protocolActionBean  instance of ProtocolActionsBean
     * @param loggedInUser 
     * @return ProcReqParameter
     */
    public boolean addUpdRiskLevels(ProtocolActionsBean protocolActionBean,
            String loggedInUser) throws CoeusException, DBException{
        Vector vecRiskLevels = protocolActionBean.getRiskLevels();
        Vector procedures = new Vector();
        if(vecRiskLevels!=null){
            String protocolNumber = protocolActionBean.getProtocolNumber();
            if( protocolNumber.indexOf( 'A' ) != -1 ||  protocolNumber.indexOf( 'R' ) != -1 ) {
                protocolNumber = protocolNumber.substring(0,10);
            }
            ProtocolRiskLevelBean protocolRiskLevelBean = null;
            for(int i = 0; i < vecRiskLevels.size(); i++){
                protocolRiskLevelBean = (ProtocolRiskLevelBean)vecRiskLevels.get(i);
                protocolRiskLevelBean.setProtocolNumber(protocolNumber);
                procedures.add(addUpdRiskLevel(protocolRiskLevelBean, loggedInUser));
            }
            if(dbEngine!=null ){
                if(procedures.size() > 0){
                    dbEngine.executeStoreProcs(procedures);
                }
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
        }
        return true;
    }
    /**
     * Updates the protocol risk level to the database
     *
     * @param protocolRiskLevelBean  instance of ProtocolRiskLevelBean
     * @param loggedInUser 
     * @return ProcReqParameter
     */
    public ProcReqParameter addUpdRiskLevel(ProtocolRiskLevelBean protocolRiskLevelBean,
            String loggedInUser) throws CoeusException, DBException{
        Vector param = new Vector();
        Timestamp dbTimestamp = new CoeusFunctions().getDBTimestamp();
        param.addElement(new Parameter("AV_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                protocolRiskLevelBean.getProtocolNumber()));
        param.addElement(new Parameter("AV_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                String.valueOf(protocolRiskLevelBean.getSequenceNumber())));
         param.addElement(new Parameter("AV_RISK_LEVEL_CODE",
                DBEngineConstants.TYPE_INT,
                String.valueOf(protocolRiskLevelBean.getRiskLevelCode())));
         param.addElement(new Parameter("AV_STATUS",
                DBEngineConstants.TYPE_STRING,
                protocolRiskLevelBean.getStatus()));
         param.addElement(new Parameter("AV_COMMENTS",
                DBEngineConstants.TYPE_STRING,
                protocolRiskLevelBean.getComments()));
         param.addElement(new Parameter("AV_DATE_ASSIGNED",
                DBEngineConstants.TYPE_DATE,
                protocolRiskLevelBean.getDateAssigned()));
         param.addElement(new Parameter("AV_DATE_UPDATED",
                DBEngineConstants.TYPE_DATE,
                protocolRiskLevelBean.getDateUpdated()));
         param.addElement(new Parameter("AV_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                dbTimestamp));
        param.add(new Parameter("AV_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, loggedInUser)) ;
        param.add(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                protocolRiskLevelBean.getUpdateTimestamp())) ;
        param.add(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                protocolRiskLevelBean.getAcType())) ;
        
        StringBuffer sql = new StringBuffer(
                "call UPD_PROTOCOL_RISK_LEVEL(");
        sql.append(" <<AV_PROTOCOL_NUMBER>> , ");
        sql.append(" <<AV_SEQUENCE_NUMBER>> , ");
        sql.append(" <<AV_RISK_LEVEL_CODE>> , ");
        sql.append(" <<AV_STATUS>> , ");
        sql.append(" <<AV_COMMENTS>> , ");
        sql.append(" <<AV_DATE_ASSIGNED>> , ");
        sql.append(" <<AV_DATE_UPDATED>> , ");
        sql.append(" <<AV_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AV_UPDATE_USER>> , ");
        sql.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sql.append(" <<AC_TYPE>> ) ");
        
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN("Coeus");
        procReqParameter.setParameterInfo(param);
        procReqParameter.setSqlCommand(sql.toString());
        
        return procReqParameter;        
    }
    //Added for case 2176 - Protocol risk level -end
     
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    /*
     * Method to save the documents uploaded in the action
     * @param cvActionDocuments - CoeusVector
     * @param actionBean - ProtocolActionsBean
     */
    private void saveActionDocuments(CoeusVector cvActionDocuments, ProtocolActionsBean actionBean){
        try{
            if(cvActionDocuments != null && cvActionDocuments.size() > 0){
                cvActionDocuments.sort("documentId",true);
                for(int index=0;index<cvActionDocuments.size();index++){
                    ProtocolActionDocumentBean protocolActionDocumentBean = (ProtocolActionDocumentBean)cvActionDocuments.get(index);
                    if(protocolActionDocumentBean.getFileBytes() != null){
                        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
                        protocolActionDocumentBean.setDocumentId(index+documentId);
                        protocolActionDocumentBean.setProtocolNumber(actionBean.getProtocolNumber());
                        protocolActionDocumentBean.setSequenceNumber(actionBean.getSequenceNumber());
                        protocolActionDocumentBean.setSubmissionNumber(
                                protocolSubmissionTxnBean.getMaxSubmissionNumber(actionBean.getProtocolNumber()));
                        protocolActionDocumentBean.setFileName(protocolActionDocumentBean.getFileName());
                        protocolActionDocumentBean.setFileBytes(protocolActionDocumentBean.getFileBytes());
                        DocumentTypeChecker typeChecker = new DocumentTypeChecker();
                        //Sets the mime type of the uploaded document 
                        protocolActionDocumentBean.setMimeType(typeChecker.getDocumentMimeType(protocolActionDocumentBean));
                        protocolActionDocumentBean.setAcType(TypeConstants.INSERT_RECORD);
                        protocolSubmissionTxnBean.addUpdProtocolActionDocument(protocolActionDocumentBean);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    //COEUSDEV-328 : End
}
