/*
 * NegotiationMaintenanceServlet.java
 *
 * Created on July 14, 2004, 2:51 PM
 */
/*
 * PMD check performed, and commented unused imports and variables on 11-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.servlet;

//import edu.mit.coeus.instprop.bean.InstituteProposalLogBean;
import edu.mit.coeus.mailaction.bean.MailActionInfoBean;
import edu.mit.coeus.mailaction.bean.MailActionTxnBean;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.negotiation.bean.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.bean.CoeusParameterBean;
//import edu.mit.coeus.instprop.bean.InstituteProposalTxnBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import java.util.ArrayList;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
//import java.util.HashMap;


/**
 * NegotiationTxnBean.java
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class NegotiationMaintenanceServlet extends CoeusBaseServlet implements TypeConstants {

	
	//Rights
	private static final String CREATE_NEGOTIATIONS = "CREATE_NEGOTIATIONS";
	private static final String MODIFY_ACTIVITIES = "MODIFY_ACTIVITIES";
	private static final String MODIFY_NEGOTIATIONS = "MODIFY_NEGOTIATIONS";
	private static final String VIEW_NEGOTIATIONS = "VIEW_NEGOTIATIONS";
	//private static final String UPDATE_NEGOTIATION_DATA = "UPDATE_NEGOTIATION_DATA";
	private static final String MAINTAIN_NEGOTIATIONS = "MAINTAIN_NEGOTIATIONS";
	
	//Negotiation Modes
	private static final char NEW_MODE = 'N';
	private static final char ADD_MODE = 'A';
	private static final char MODIFY_MODE = 'M';
		
	// Negotiation
	private static final char GET_NEGOTIATION_WHILEMODIFY = 'H';
	private static final char GET_NEGOTIATION_CUSTOM_DATA  = 'B';
	private static final char GET_NEGOTIATION_DATA = 'C';
	private static final char NEGOTIATION_COUNT = 'D';
	private static final char CHECK_NEGLIST_RIGHTS = 'E';
	private static final char UPDATE_NEGOTIATION = 'F';
	private static final char RELEASE_LOCK = 'G';
	private static final char NEGOTIATION_FROM_INSTPROPOSAL = 'J';
        
        //case 3590 start
        private static final char GET_NEGOTIATION_LOCATION_HISTORY = 'S';
        //case 3590 end
	
	//case 3961 start
        private static final char INST_PROP_COUNT = 'I';
        //case 3961 end
        
        //case 4185 - Saving negotiation Location History -Start
        private static final char SAVE_LOCATION_HISTORY = 'L';
        //case 4185 - Saving negotiation Location History -End
        //case 2806 - Upload Attachments to negotiation module - Start
        private static final char GET_BLOB_DATA_FOR_NEGOTIATION = 'y';
        //case 2806 - Upload Attachments to negotiation module - Start
        // 3587: Multi Campus Enahncements
        private static final char CAN_MODIFY_NEGOTIATION = 'K';
        //Added for COEUSDEV-294 :  Error adding activity to a negotiation - Start
        private static final char GET_MAX_NEGOTIATION_ACTIVITY_NUMBER = 'm';
        private static final String MAINTAIN_NEGOTIATION_ACTIVITY = "MAINTAIN_NEGOTIATION_ACTIVITY";
        //COEUSDEV-294 : End
        //COEUSDEV - 733 Create a new notification for negotiation module - Start
        private static final char COMPARE_PARAMETER_VALUE = 'T';
        //private static final char UPDATE_INBOX = 'U';
        //COEUSDEV - 733 Create a new notification for negotiation module - Start
                
	/** Creates a new instance of NegotiationMaintenanceServlet */
	public NegotiationMaintenanceServlet() {
	}
	/**
	 * This method handles all the POST requests from the Client
	 *
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException if any ServletException
	 * @throws IOException if any IOException
	 */
	public void doPost(HttpServletRequest request,
	HttpServletResponse response)
	throws ServletException, IOException {
		
//		UtilFactory UtilFactory = new UtilFactory();
		
		// the request object from applet
		RequesterBean requester = null;
		// the response object to applet
		ResponderBean responder = new ResponderBean();
		
		// open object input/output streams
		ObjectInputStream inputFromApplet = null;
		ObjectOutputStream outputToApplet = null;
		
		String loggedinUser ="";
		String unitNumber = "";
		String userId = "";
                // 3587: Multi Campus Enahncements
                String leadUnit = "";
		
		try {
			
			// get an input stream
			inputFromApplet = new ObjectInputStream(request.getInputStream());
			// read the serialized request object from applet
			requester = (RequesterBean) inputFromApplet.readObject();
			isValidRequest(requester);
			loggedinUser = requester.getUserName();
			
			// get the user
			UserInfoBean userBean = (UserInfoBean)new
			UserDetailsBean().getUserInfo(requester.getUserName());
			
			unitNumber = userBean.getUnitNumber();
			userId = userBean.getUserId();
			char functionType = requester.getFunctionType();
			String negotiationNumber = "";
			// To access the user rights
			UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
			NegotiationTxnBean negotiationTxnBean = new NegotiationTxnBean();
			
			if(functionType == MODIFY_MODE) {
				
				negotiationNumber = (String)requester.getDataObject();
				boolean hasRight = false;
				// keep all the beans into vector
				CoeusVector dataObjects = new CoeusVector();
				
				//Check for CREATE_AWARD OSP right
				hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_NEGOTIATIONS);
				dataObjects.addElement(new Boolean(hasRight));
				
				//Check for MODIFY_AWARD OSP right
                                // 3587: Multi Campus Enahncements - Start
//				hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_ACTIVITIES);
                                hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES, unitNumber);
				// 3587: Multi Campus Enahncements - End
                                dataObjects.addElement(new Boolean(hasRight));
				
				//Check for VIEW_AWARD right
				hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_NEGOTIATIONS, unitNumber);
				dataObjects.addElement(new Boolean(hasRight));
				
				responder.setDataObject(dataObjects);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if(functionType == ADD_MODE) {
				
				negotiationNumber = (String)requester.getDataObject();
				boolean hasRight = false;
				// keep all the beans into vector
				CoeusVector cvRights = new CoeusVector();
			
				//Check for CREATE_AWARD OSP right
                                // 3587: Multi Campus Enahncements- Start
//				hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_NEGOTIATIONS);
                                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, CREATE_NEGOTIATIONS);
                                // 3587: Multi Campus Enahncements - End
				cvRights.addElement(new Boolean(hasRight));
				
				//Check for MODIFY_AWARD OSP right
                                // 3587: Multi Campus Enahncements- Start
//				hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_ACTIVITIES);
                                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_ACTIVITIES);
                                // 3587: Multi Campus Enahncements - End
				cvRights.addElement(new Boolean(hasRight));
				
				//Check for VIEW_AWARD right
				hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_NEGOTIATIONS, unitNumber);
				cvRights.addElement(new Boolean(hasRight));
				
				responder.setDataObject(cvRights);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == GET_NEGOTIATION_WHILEMODIFY) {
				negotiationNumber = (String)requester.getDataObject();
				//while modifying lock the negotiation number and sending the values.
                                // Code commented by Shivakumar for locking enhancement
//				boolean lockSuccess = negotiationTxnBean.getNegotiatorLock(negotiationNumber);
                                // Code added by Shivakumar - BEGIN
                                LockingBean lockingBean =  negotiationTxnBean.getNegotiatorLock(negotiationNumber,loggedinUser, unitNumber);
                                boolean lockSuccess = lockingBean.isGotLock();
                                // Code added by Shivakumar - END
				if (lockSuccess) {
                                    // Adding try/catch block for locking enhancement
                                    // Code added by Shivakumar - BEGIN 1
                                    try{
                                    // Code added by Shivakumar - END 1   
					Hashtable negotiationDataTable = new Hashtable();
					// getting the negotiation header bean
					NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.
					getNegotiationHeader(negotiationNumber);
					//getting the negotiation info bean
					NegotiationInfoBean negotiationInfoBean = negotiationTxnBean.
					getNegotiationInfo(negotiationNumber);
					//putting into the hash table
					negotiationDataTable.put(NegotiationHeaderBean.class, negotiationHeaderBean);
					negotiationDataTable.put(NegotiationInfoBean.class, negotiationInfoBean);
					//getting the status list
					CoeusVector cvStatusCombo = negotiationTxnBean.getNegotiationStatusList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_STATUS,cvStatusCombo);
                                        //case 3590 start
                                        //getting the agreement type list
                                        CoeusVector cvAgreementTypeCombo = negotiationTxnBean.getNegotiationAgreementTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_AGREEMENT_TYPE,cvAgreementTypeCombo);
                                        //getting the location type list
                                        CoeusVector cvLocationTypeCombo = negotiationTxnBean.getNegotiationLocationTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_LOCATION_TYPE,cvLocationTypeCombo);
                                        // getting the location type and putting into the hash table
					CoeusVector cvNegotiationLocaton = negotiationTxnBean.
					getNegotiationLocation(negotiationNumber);					
					negotiationDataTable.put(NegotiationLocationBean.class,cvNegotiationLocaton);					
                                        //case 3590 end
					//getting the custom elements and put into the hash table..Combination of all.
					CoeusVector cvNegotiationCustomData = negotiationTxnBean.
					getCustomData(negotiationNumber);
					negotiationDataTable.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
                    //Modified for COEUSDEV-294 : Error adding activity to a negotiation - Start
                    // getting the activities type and putting into the hash table based on the 'MAINTAIN_NEGOTIATION_ACTIVITY' right in the IP lead unit
//					CoeusVector cvNegotiationActivities = negotiationTxnBean.
//					getNegotiationActivities(negotiationNumber,loggedinUser);
                    leadUnit = negotiationHeaderBean.getLeadUnit();
                    CoeusVector cvNegotiationActivities = negotiationTxnBean.
                            getNegotiationActivities(negotiationNumber,loggedinUser,leadUnit);
                    //COEUSDEV-294 : End
					CoeusVector cvActivityType = negotiationTxnBean.getNegotiationActTypes();
					negotiationDataTable.put(NegotiationActivitiesBean.class,cvNegotiationActivities);
					negotiationDataTable.put(KeyConstants.NEGOTIATION_ACTIVITY_TYPE,cvActivityType);

					// getting the proposal type code and description
					CoeusVector cvProposalList = negotiationTxnBean.
					getProposalTypeList();
					negotiationDataTable.put(KeyConstants.PROPOSAL_TYPE,cvProposalList);

					// getting the parameter name by passing the parameter value and set for the negotiation

					//Get Comment codes for Cost Sharing comments and IDC Rates
					CoeusFunctions coeusFunctions = new CoeusFunctions();
					CoeusParameterBean coeusParameterBean = null;
					//COST_SHARING_COMMENT_CODE
					coeusParameterBean = new CoeusParameterBean();
					coeusParameterBean.setParameterName("COEUS_MODULE_NEGOTIATION");
					coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
					negotiationDataTable.put(CoeusParameterBean.class,coeusParameterBean);

					Vector negDataVec = new Vector();
					CoeusVector cvRights = new CoeusVector();

					//Check for MODIFY_ACTIVITIES OSP right
                                        // 3587: Multi Campus Enahncements - Start
                                        //Commented for COEUSDEV-294 :  Error adding activity to a negotiation - Start
//                                        leadUnit = negotiationHeaderBean.getLeadUnit();
                                        //COEUSDEV-294 : End
//					boolean hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_ACTIVITIES);
                                        boolean hasOSPRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES, leadUnit);
                                        // 3587: Multi Campus Enahncements - End
					cvRights.addElement(new Boolean(hasOSPRight));
                    //Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                    negotiationDataTable.put("MODIFY_ACTIVITIES",new Boolean(hasOSPRight));
                			//Check for any OSP right for the user
//					boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
//					cvRights.addElement(new Boolean(hasAnyOSPRight));
                                        //Check user has 'MAINTAIN_NEGOTIATION_ACTIVITY' right
                                        boolean hasMaintainActivityRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NEGOTIATION_ACTIVITY,leadUnit);
                                        cvRights.addElement(new Boolean(hasMaintainActivityRight));
                                        //COEUSDEV-294 : END
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
                                        negotiationDataTable.put(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY,
                                                coeusFunctions.getParameterValue(CoeusConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY));
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End
					negDataVec.addElement(negotiationDataTable);
					negDataVec.addElement(cvRights);
                                        // Code added by Shivakumar - BEGIN
                                        negotiationTxnBean.transactionCommit();
                                        responder.setLockingBean(lockingBean);
                                        // Code added by Shivakumar - END
                                        responder.setDataObjects(negDataVec);
					responder.setMessage(null);
					responder.setResponseStatus(true);
                                    // Code added by Shivakumar - BEGIN    
                                    }catch(DBException dbEx){
                                        dbEx.printStackTrace();
                                        negotiationTxnBean.transactionRollback();
                                        throw dbEx;
                                    }finally{
                                        negotiationTxnBean.endConnection();
                                    }    
				    // Code added by Shivakumar - END	
				} 
			} else if (functionType == GET_NEGOTIATION_CUSTOM_DATA) {
				negotiationNumber = (String)requester.getDataObject();
				Hashtable negotiationCustomData = new Hashtable();
				CoeusVector cvNegotiationCustomData = negotiationTxnBean.
									getNegotiationCustomData(negotiationNumber);
				negotiationCustomData.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
				responder.setDataObject(negotiationCustomData);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == NEW_MODE) {
				negotiationNumber = (String)requester.getDataObject();
                                // Commented by Shivakumar for locking enhancement
//				boolean lockSuccess = negotiationTxnBean.getNegotiatorLock(negotiationNumber);
                                // Code added by Shivakumar - BEGIN
                                //LockingBean lockingBean =  negotiationTxnBean.getNegotiatorLock(negotiationNumber,loggedinUser, unitNumber);
                                //boolean lockSuccess = lockingBean.isGotLock();
                                // Code added by Shivakumar - END
                                
				//if (lockSuccess) {
                                    // Adding try/catch block for locking enhancement
                                    // Code added by Shivakumar - BEGIN 1
                                    try{
                                    // Code added by Shivakumar - END 1  
					Hashtable negotiationDataTable = new Hashtable();

					//getting the status list
					CoeusVector cvStatusCombo = negotiationTxnBean.getNegotiationStatusList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_STATUS,cvStatusCombo);
                                        //case 3590 start
                                        //getting the agreement type list
                                        CoeusVector cvAgreementTypeCombo = negotiationTxnBean.getNegotiationAgreementTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_AGREEMENT_TYPE,cvAgreementTypeCombo);
                                        //getting the location type list
                                        CoeusVector cvLocationTypeCombo = negotiationTxnBean.getNegotiationLocationTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_LOCATION_TYPE,cvLocationTypeCombo);
                                        CoeusVector cvNegotiationLocaton = negotiationTxnBean.
					getNegotiationLocation(negotiationNumber);					
					negotiationDataTable.put(NegotiationLocationBean.class,cvNegotiationLocaton);	
                                        //case 3590 end

					// getting the proposal type code and description 
					CoeusVector cvProposalList = negotiationTxnBean.
										getProposalTypeList();
					negotiationDataTable.put(KeyConstants.PROPOSAL_TYPE,cvProposalList);

					// getting the parameter name by passing the parameter value and set for the negotiation

					//Get Comment codes for negotiations
					CoeusFunctions coeusFunctions = new CoeusFunctions();
					CoeusParameterBean coeusParameterBean = null;
					//COEUS_MODULE_NEGOTIATION
					coeusParameterBean = new CoeusParameterBean();
					coeusParameterBean.setParameterName("COEUS_MODULE_NEGOTIATION");
					coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
					negotiationDataTable.put(CoeusParameterBean.class,coeusParameterBean);

					CoeusVector cvActivityType = negotiationTxnBean.getNegotiationActTypes();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_ACTIVITY_TYPE,cvActivityType);

					//getting the custom elements and put into the hash table
					CoeusVector cvNegotiationCustomData = negotiationTxnBean.
										getCustomDataForNewMode(negotiationNumber);
                                        // Bug Fix #1131 - start - chandra 12-Aug-2004
                                        if(cvNegotiationCustomData!= null && cvNegotiationCustomData.size() >0){
                                            negotiationDataTable.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
                                        }// Bug Fix #1131 - End - chandra 12-Aug-2004
                                        //Added COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
                                        //Check for 'MODIFY_ACTIVITITES' right while creating a new negotiation
                                        String proposalLeadUnit = requester.getId();
                                        boolean hasModifyActivityRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES, proposalLeadUnit);
                                        negotiationDataTable.put("MODIFY_ACTIVITIES",new Boolean(hasModifyActivityRight));
                                        //COEUSDEV-268 :End
                                        // Code added by Shivakumar - BEGIN
                                        negotiationTxnBean.transactionCommit();
                                        //responder.setLockingBean(lockingBean);
                                        // Code added by Shivakumar - END
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
                                        negotiationDataTable.put(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY,
                                                coeusFunctions.getParameterValue(CoeusConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY));
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End
					responder.setDataObject(negotiationDataTable);
					responder.setMessage(null);
					responder.setResponseStatus(true);
                                    // Code added by Shivakumar - BEGIN 2    
                                    }catch(DBException dbEx){
                                        dbEx.printStackTrace();
                                        negotiationTxnBean.transactionRollback();
                                        throw dbEx;
                                    }finally{
                                        negotiationTxnBean.endConnection();
                                    }    
                                    // Code added by Shivakumar - END 2
				//}
			} else if (functionType == GET_NEGOTIATION_DATA) {
				negotiationNumber = (String)requester.getDataObject();
				Hashtable negotiationDataTable = new Hashtable();
				// getting the negotiation header bean
				NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.
											getNegotiationHeader(negotiationNumber);
				//getting the negotiation info bean
				NegotiationInfoBean negotiationInfoBean = negotiationTxnBean.
											getNegotiationInfo(negotiationNumber);
				//putting into the hash table
				negotiationDataTable.put(NegotiationHeaderBean.class, negotiationHeaderBean);
				negotiationDataTable.put(NegotiationInfoBean.class, negotiationInfoBean);
				//getting the status list
				CoeusVector cvStatusCombo = negotiationTxnBean.getNegotiationStatusList();
				negotiationDataTable.put(KeyConstants.NEGOTIATION_STATUS,cvStatusCombo);
                                //case 3590 start
                                //getting the agreement type list
                                CoeusVector cvAgreementTypeCombo = negotiationTxnBean.getNegotiationAgreementTypeList();
				negotiationDataTable.put(KeyConstants.NEGOTIATION_AGREEMENT_TYPE,cvAgreementTypeCombo);
                                //getting the location type list
                                CoeusVector cvLocationTypeCombo = negotiationTxnBean.getNegotiationLocationTypeList();
				negotiationDataTable.put(KeyConstants.NEGOTIATION_LOCATION_TYPE,cvLocationTypeCombo);
                                // getting the location type and putting into the hash table
				CoeusVector cvNegotiationLocaton = negotiationTxnBean.
				getNegotiationLocation(negotiationNumber);					
				negotiationDataTable.put(NegotiationLocationBean.class,cvNegotiationLocaton);			
                                //case 3590 end
				//getting the custom elements and put into the hash table
				CoeusVector cvNegotiationCustomData = negotiationTxnBean.
									getNegotiationCustomData(negotiationNumber);
				negotiationDataTable.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
				//Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
				// getting the activities type and putting into the hash table based on the 'MAINTAIN_NEGOTIATION_ACTIVITY' right
//				CoeusVector cvNegotiationActivities = negotiationTxnBean.
//									getNegotiationActivities(negotiationNumber,loggedinUser);
                                leadUnit = negotiationHeaderBean.getLeadUnit();
                                CoeusVector cvNegotiationActivities = negotiationTxnBean.
									getNegotiationActivities(negotiationNumber,loggedinUser,leadUnit);
                                //COEUSDEV-294 : End
				CoeusVector cvActivityType = negotiationTxnBean.getNegotiationActTypes();
				negotiationDataTable.put(NegotiationActivitiesBean.class,cvNegotiationActivities);
				negotiationDataTable.put(KeyConstants.NEGOTIATION_ACTIVITY_TYPE,cvActivityType);
				
				// getting the proposal type code and description 
				CoeusVector cvProposalList = negotiationTxnBean.
									getProposalTypeList();
				negotiationDataTable.put(KeyConstants.PROPOSAL_TYPE,cvProposalList);
				
				// getting the parameter name by passing the parameter value and set for the negotiation
				
				//Get Comment codes for Cost Sharing comments and IDC Rates
				CoeusFunctions coeusFunctions = new CoeusFunctions();
				CoeusParameterBean coeusParameterBean = null;
				//COST_SHARING_COMMENT_CODE
				coeusParameterBean = new CoeusParameterBean();
				coeusParameterBean.setParameterName("COEUS_MODULE_NEGOTIATION");
				coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
				negotiationDataTable.put(CoeusParameterBean.class,coeusParameterBean);
				
				Vector negDataVec = new Vector();
				CoeusVector cvRights = new CoeusVector();
				
				//Check for MODIFY_ACTIVITIES OSP right
                                // 3587: Multi Campus Enahncements- Start
//				boolean hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_ACTIVITIES);
                                //Commented for COEUSDEV-294 :  Error adding activity to a negotiation - Start
//                                leadUnit = negotiationHeaderBean.getLeadUnit();
                                //COEUSDEV-294 : END
                                boolean hasOSPRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES, leadUnit);
                                //Added for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                                negotiationDataTable.put("MODIFY_ACTIVITIES",new Boolean(hasOSPRight));
                                //COEUSDEV-294 : End
				// 3587: Multi Campus Enahncements - End
                                cvRights.addElement(new Boolean(hasOSPRight));
				//Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
				//Check for any OSP right for the user
//				boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
//                                cvRights.addElement(new Boolean(hasAnyOSPRight)); 
                                //Check 'MAINTAIN_NEGOTIATION_ACTIVITY' right for the activities
                                boolean hasMaintainActivityRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NEGOTIATION_ACTIVITY,leadUnit);
                                cvRights.addElement(new Boolean(hasMaintainActivityRight)); 				
                                //COEUSDEV-294 : END
                                // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
                                negotiationDataTable.put(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY,
                                        coeusFunctions.getParameterValue(CoeusConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY));
                                // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End                                
				negDataVec.addElement(negotiationDataTable);
				negDataVec.addElement(cvRights);
				responder.setDataObjects(negDataVec);
				responder.setMessage(null);
				responder.setResponseStatus(true);
			} else if (functionType == NEGOTIATION_COUNT) {
				negotiationNumber = (String)requester.getDataObject();
				int negCount = negotiationTxnBean.getNegotiationCount(negotiationNumber);
				boolean isNegCount = false;
				if (negCount > 0) {
					isNegCount = true;
				}
				responder.setDataObject(new Boolean(isNegCount));
				responder.setMessage(null);
				responder.setResponseStatus(true);
				
			}else if(functionType == CHECK_NEGLIST_RIGHTS) {
                CoeusVector cvAuthRights = new CoeusVector();
                boolean hasRight = false;
//				boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, CREATE_NEGOTIATIONS);
//                cvAuthRights.addElement(new Boolean(hasRight));
                // 3587: Multi Campus Enahncements- Start
//                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_NEGOTIATIONS);
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, MODIFY_NEGOTIATIONS);
                // 3587: Multi Campus Enahncements - End
                cvAuthRights.addElement(new Boolean(hasRight));
                hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, VIEW_NEGOTIATIONS);
                cvAuthRights.addElement(new Boolean(hasRight));
                hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MAINTAIN_NEGOTIATIONS);
                cvAuthRights.addElement(new Boolean(hasRight));
                
                responder.setDataObject(cvAuthRights);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if(functionType == UPDATE_NEGOTIATION) {
                Hashtable negotiationData = (Hashtable)requester.getDataObject();
                negotiationNumber = requester.getId();               
                negotiationTxnBean = new NegotiationTxnBean(loggedinUser);
                // Code added by Shivakumar for bug fixing locking - BEGIN
                boolean success = false;
                int count = 0;
                CoeusVector cvNegInfo = (CoeusVector)negotiationData.get(NegotiationInfoBean.class);
		if (cvNegInfo != null && cvNegInfo.size() > 0) {
                	for (int index = 0; index < cvNegInfo.size(); index++) {
				NegotiationInfoBean negotiationInfoBean = (NegotiationInfoBean)cvNegInfo.get(index);
                                     if((negotiationInfoBean.getAcType() != null) && (negotiationInfoBean.getAcType().equals("I"))){                                                                                  
                                         success = negotiationTxnBean.updateNegotiation(negotiationData);
                                         // For new negotiation lock should be inserted.
                                         // Bug fix for case #2687 starts
                                         LockingBean lockingBean  = negotiationTxnBean.getNegotiatorLock(negotiationInfoBean.getNegotiationNumber(),loggedinUser,unitNumber);
                                         negotiationTxnBean.transactionCommit();
                                         responder.setLockingBean(lockingBean);
                                         // Bug fix for case #2687 ends
                                     }else{
                                         boolean lockCheck = negotiationTxnBean.lockCheck(negotiationInfoBean.getNegotiationNumber(), loggedinUser);                                         
                                         if(!lockCheck){
                                              success = negotiationTxnBean.updateNegotiation(negotiationData);
                                          }else{
                                              //String msg = "Sorry,  the lock for negotiation "+negotiationInfoBean.getNegotiationNumber()+"  has been deleted by DB Administrator ";
                                              CoeusMessageResourcesBean coeusMessageResourcesBean
                                                    =new CoeusMessageResourcesBean();
                                              String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1007")+" "+negotiationInfoBean.getNegotiationNumber()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                                              throw new LockingException(msg);
                                          }    
                                     }    
                        }        
                }       
                
                
                // Code added by Shivakumar for bug fixing locking - END
//                boolean success = negotiationTxnBean.updateNegotiation(negotiationData);
                //Check whether to Release the Lock 
                boolean releaseLock = ((Boolean)negotiationData.get(CoeusConstants.IS_RELEASE_LOCK)) == null 
                        ? false : ((Boolean)negotiationData.get(CoeusConstants.IS_RELEASE_LOCK)).booleanValue();
                if(releaseLock){
                    //If release lock is requested
                    // Commented by Shivakumar for locking enhancement
//                    negotiationTxnBean.releaseEdit(negotiationNumber);
                    // Code aded by Shivakumar - BEGIN
//                    negotiationTxnBean.releaseEdit(negotiationNumber,loggedinUser);
                    // Calling releaseLock method for bug fixing
                    LockingBean lockingBean = negotiationTxnBean.releaseLock(negotiationNumber,loggedinUser);
                    responder.setLockingBean(lockingBean);
                    // Code aded by Shivakumar - END
                    responder.setMessage(null);
                    responder.setResponseStatus(true);                    
                } else {
                   
                    Hashtable negotiationDataTable = new Hashtable();
					// getting the negotiation header bean
					NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.
					getNegotiationHeader(negotiationNumber);
					//getting the negotiation info bean
					NegotiationInfoBean negotiationInfoBean = negotiationTxnBean.
					getNegotiationInfo(negotiationNumber);
					//putting into the hash table
					negotiationDataTable.put(NegotiationHeaderBean.class, negotiationHeaderBean);
					negotiationDataTable.put(NegotiationInfoBean.class, negotiationInfoBean);
					//getting the status list
					CoeusVector cvStatusCombo = negotiationTxnBean.getNegotiationStatusList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_STATUS,cvStatusCombo);
                                        //case 3590 start
                                        //getting the agreement type list
                                        CoeusVector cvAgreementTypeCombo = negotiationTxnBean.getNegotiationAgreementTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_AGREEMENT_TYPE,cvAgreementTypeCombo);
                                        //getting the location type list
                                        CoeusVector cvLocationTypeCombo = negotiationTxnBean.getNegotiationLocationTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_LOCATION_TYPE,cvLocationTypeCombo);
                                        // getting the location type and putting into the hash table
					CoeusVector cvNegotiationLocaton = negotiationTxnBean.
					getNegotiationLocation(negotiationNumber);					
					negotiationDataTable.put(NegotiationLocationBean.class,cvNegotiationLocaton);		
                                        //case 3590 end
					//getting the custom elements and put into the hash table
					CoeusVector cvNegotiationCustomData = negotiationTxnBean.
					getNegotiationCustomData(negotiationNumber);
					negotiationDataTable.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
					
					// getting the activities type and putting into the hash table
					
                                        //Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
//                                        CoeusVector cvNegotiationActivities = negotiationTxnBean.
//					getNegotiationActivities(negotiationNumber,loggedinUser);
                                        leadUnit = negotiationHeaderBean.getLeadUnit();
                                        CoeusVector cvNegotiationActivities = negotiationTxnBean.
                                                getNegotiationActivities(negotiationNumber,loggedinUser,leadUnit);
                                        //COEUSDEV-294 : End
					CoeusVector cvActivityType = negotiationTxnBean.getNegotiationActTypes();
					negotiationDataTable.put(NegotiationActivitiesBean.class,cvNegotiationActivities);
					negotiationDataTable.put(KeyConstants.NEGOTIATION_ACTIVITY_TYPE,cvActivityType);
					
					// getting the proposal type code and description
					CoeusVector cvProposalList = negotiationTxnBean.
					getProposalTypeList();
					negotiationDataTable.put(KeyConstants.PROPOSAL_TYPE,cvProposalList);
					
					// getting the parameter name by passing the parameter value and set for the negotiation
					
					//Get Comment codes for Cost Sharing comments and IDC Rates
					CoeusFunctions coeusFunctions = new CoeusFunctions();
					CoeusParameterBean coeusParameterBean = null;
					//COST_SHARING_COMMENT_CODE
					coeusParameterBean = new CoeusParameterBean();
					coeusParameterBean.setParameterName("COEUS_MODULE_NEGOTIATION");
					coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
					negotiationDataTable.put(CoeusParameterBean.class,coeusParameterBean);
					
					Vector negDataVec = new Vector();
					CoeusVector cvRights = new CoeusVector();
                                        
					//Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
					//Check for MODIFY_ACTIVITIES OSP right
//					boolean hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_ACTIVITIES);
//					cvRights.addElement(new Boolean(hasOSPRight));
                   			//Check for any OSP right for the user
//					boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
//					cvRights.addElement(new Boolean(hasAnyOSPRight));
                                        //Commented for COEUSDEV-294
//                                        leadUnit = negotiationHeaderBean.getLeadUnit();
                                        boolean hasActivityRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES,leadUnit);
					cvRights.addElement(new Boolean(hasActivityRight));
					//Check user has MAINTAIN_NEGOTIATION_ACTIVITY right to view the osp activities
					boolean hasMaintainActivityRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NEGOTIATION_ACTIVITY,leadUnit);
					cvRights.addElement(new Boolean(hasMaintainActivityRight));
                                        //COEUSDEV-294 :End
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
                                        negotiationDataTable.put(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY,
                                                coeusFunctions.getParameterValue(CoeusConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY));
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End
					negDataVec.addElement(negotiationDataTable);
					negDataVec.addElement(cvRights);
					responder.setDataObjects(negDataVec);
					responder.setMessage(null);
					responder.setResponseStatus(true);
                }
            } else if (functionType == RELEASE_LOCK) {
				negotiationNumber = (String)requester.getDataObject();
                                // Commented by Shivakumar for locking enhancement
//				negotiationTxnBean.releaseEdit(negotiationNumber);
                                // Code added by Shivakumar - BEGIN
//                                negotiationTxnBean.releaseEdit(negotiationNumber,loggedinUser);
                                // Calling releaseLock method for bug fixing 
                                LockingBean lockingBean = negotiationTxnBean.releaseLock(negotiationNumber,loggedinUser);
                                responder.setLockingBean(lockingBean);
                                // Code added by Shivakumar - END
				responder.setMessage(null);
				responder.setResponseStatus(true);   
			} else if (functionType == NEGOTIATION_FROM_INSTPROPOSAL) {
				CoeusVector cvNegFromInst = new CoeusVector();
				Hashtable negotiationDataTable = new Hashtable();
				negotiationNumber = (String)requester.getDataObject();
				int negCount = negotiationTxnBean.getNegotiationCount(negotiationNumber);
				cvNegFromInst.addElement(new Integer(negCount));
                                //Added for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                                // getting the negotiation header bean
                                NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.
                                        getNegotiationHeader(negotiationNumber);
                                negotiationDataTable.put(NegotiationHeaderBean.class, negotiationHeaderBean);
                                leadUnit = negotiationHeaderBean.getLeadUnit();
                                //COEUSDEV-294 : End
                                        if (negCount > 0) {      
                                            // Code added by Shivakumar for locking - BEGIN    
                                                LockingBean lockingBean = negotiationTxnBean.getNegotiatorLock(negotiationNumber,loggedinUser,unitNumber);
                                                boolean lockCheck = lockingBean.isGotLock();                                               
                                                if(lockCheck){
                                                    try{
                                                        negotiationTxnBean.transactionCommit();
                                                        responder.setLockingBean(lockingBean);                                                        
                                                     }catch(DBException dbEx){
                                                        dbEx.printStackTrace();
                                                        negotiationTxnBean.transactionRollback();
                                                        throw dbEx;
                                                      }finally{
                                                          negotiationTxnBean.endConnection();
                                                      }    
                                                }   
                                                // Code added by Shivakumar for locking - END    
                                                //getting the negotiation info bean if count is > 0
                                                NegotiationInfoBean negotiationInfoBean = negotiationTxnBean.
                                                                                                getNegotiationInfo(negotiationNumber);
                                                negotiationDataTable.put(NegotiationInfoBean.class, negotiationInfoBean);
                                                //Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                                                // getting the activities bean
//                                                CoeusVector cvNegotiationActivities = negotiationTxnBean.
//                                                        getNegotiationActivities(negotiationNumber,loggedinUser);
                                                //Getting the activities bean based on 'MAINTAIN_NEGOTIATION_ACTIVITY' right
                                                CoeusVector cvNegotiationActivities = negotiationTxnBean.
                                                        getNegotiationActivities(negotiationNumber,loggedinUser,leadUnit);
                                                //COEUSDEV-294 : End
                                                negotiationDataTable.put(NegotiationActivitiesBean.class,cvNegotiationActivities);
                                                //Case 3408 - START
                                                //getting the custom elements and put into the hash table
                                                //CoeusVector cvNegotiationCustomData = negotiationTxnBean.
                                                //                                getNegotiationCustomData(negotiationNumber);
                                                CoeusVector cvNegotiationCustomData = negotiationTxnBean.getCustomData(negotiationNumber);
                                                //Case 3408 - END
                                                negotiationDataTable.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
                                                //Case#4258 -Negotiation opened via IP List Edit:Negotiation User Cannot View or Maintain Location Tracking - Start
                                                CoeusVector cvNegotiationLocaton = negotiationTxnBean.getNegotiationLocation(negotiationNumber);
                                                negotiationDataTable.put(NegotiationLocationBean.class,cvNegotiationLocaton);
                                                //Case#4258 -Negotiation opened via IP List Edit:Negotiation User Cannot View or Maintain Location Tracking - End
                                        }
                                        //Case 3408 - START
                                        else {
                                            //getting the custom elements and put into the hash table
                                            CoeusVector cvNegotiationCustomData = negotiationTxnBean.getCustomDataForNewMode(negotiationNumber);
                                            if(cvNegotiationCustomData != null) {
                                                negotiationDataTable.put(NegotiationCustomElementsBean.class,cvNegotiationCustomData);
                                            }
                                        }
                                        //Case 3408 - END
                                
                                        //getting the act types
                                        CoeusVector cvActivityType = negotiationTxnBean.getNegotiationActTypes();
                                        negotiationDataTable.put(KeyConstants.NEGOTIATION_ACTIVITY_TYPE,cvActivityType);

                                        //getting the status list
                                        CoeusVector cvStatusCombo = negotiationTxnBean.getNegotiationStatusList();
                                        negotiationDataTable.put(KeyConstants.NEGOTIATION_STATUS,cvStatusCombo);
                                        //case 3590 start
                                        //getting the agreement type list
                                        CoeusVector cvAgreementTypeCombo = negotiationTxnBean.getNegotiationAgreementTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_AGREEMENT_TYPE,cvAgreementTypeCombo);
                                        //getting the location type list
                                        CoeusVector cvLocationTypeCombo = negotiationTxnBean.getNegotiationLocationTypeList();
					negotiationDataTable.put(KeyConstants.NEGOTIATION_LOCATION_TYPE,cvLocationTypeCombo);
                                        //case 3590 end
                                        
                                        //Get Comment codes for Cost Sharing comments and IDC Rates
                                        CoeusFunctions coeusFunctions = new CoeusFunctions();
                                        CoeusParameterBean coeusParameterBean = null;
                                        //COST_SHARING_COMMENT_CODE
                                        coeusParameterBean = new CoeusParameterBean();
                                        coeusParameterBean.setParameterName("COEUS_MODULE_NEGOTIATION");
                                        coeusParameterBean.setParameterValue(coeusFunctions.getParameterValue(coeusParameterBean.getParameterName()));
                                        negotiationDataTable.put(CoeusParameterBean.class,coeusParameterBean);

                                        //passing the negotiation number and has to get the PIUserId
                                        String piUserName = negotiationTxnBean.getPIUserName(negotiationNumber);
                                        if(piUserName!= null && !("").equals(piUserName)){
                                            negotiationDataTable.put(KeyConstants.PI_USERNAME,piUserName);
                                        }
                                        //Commented for COEUSDEV-294 :  Error adding activity to a negotiation - Start
//                                        // getting the negotiation header bean
//                                        NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.
//                                                getNegotiationHeader(negotiationNumber);
//                                        negotiationDataTable.put(NegotiationHeaderBean.class, negotiationHeaderBean);
                                        //COEUSDEV-294 : END
                                       
                                        boolean hasRight = false;
                                        // check for the osp rights in modify and view.
                                        // 3587: Multi Campus Enahncements - Start   
                                        //Commented for COEUSDEV-294 :  Error adding activity to a negotiation - Start
//                                        leadUnit = negotiationHeaderBean.getLeadUnit();
                                        //COEUSDEV-294 : End
//                                       hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_NEGOTIATIONS);
                                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_NEGOTIATIONS, leadUnit);
                                        // 3587: Multi Campus Enahncements - End
                        cvNegFromInst.addElement(new Boolean(hasRight));

                                        hasRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, VIEW_NEGOTIATIONS, unitNumber);
                        cvNegFromInst.addElement(new Boolean(hasRight));

                                        CoeusVector cvRights = new CoeusVector();

                                        //Check for MODIFY_ACTIVITIES OSP right
                                        // 3587: Multi Campus Enahncements - Start
//                                        boolean hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, MODIFY_ACTIVITIES);
                                        boolean hasOSPRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES, leadUnit);
                                        //Added for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                                        negotiationDataTable.put("MODIFY_ACTIVITIES",new Boolean(hasOSPRight));
                                        //COEUSDEV-294 : End
                                        // 3587: Multi Campus Enahncements
                                        cvRights.addElement(new Boolean(hasOSPRight));
                                        
                                        //Modified for COEUSDEV-294 :  Error adding activity to a negotiation - Start
                                        //Check for any OSP right for the user
//                                        boolean hasAnyOSPRight = userMaintDataTxnBean.getUserHasAnyOSPRight(loggedinUser);
//                                        cvRights.addElement(new Boolean(hasAnyOSPRight));
                                        boolean hasMaintainActivityRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MAINTAIN_NEGOTIATION_ACTIVITY,leadUnit);
                                        cvRights.addElement(new Boolean(hasMaintainActivityRight));
                                        //COEUSDEV-294 : END
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - Start
                                        negotiationDataTable.put(KeyConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY,
                                                coeusFunctions.getParameterValue(CoeusConstants.ENABLE_NEGOTIATION_ACTIVITY_DESC_MANDATORY));
                                        // Added for COEUSDEV-738 : Build parameter to make 'description' field not required for new activity in the negotiation module - End
                                        Vector vecNeg = new Vector();
                                        vecNeg.addElement(negotiationDataTable);
                                        vecNeg.addElement(cvNegFromInst);
                                        vecNeg.addElement(cvRights);
                                        responder.setDataObjects(vecNeg);
                                        responder.setMessage(null);
                                        responder.setResponseStatus(true);
                        //case 3590 start            
			}else if ( functionType == GET_NEGOTIATION_LOCATION_HISTORY){
                            negotiationNumber = (String)requester.getDataObject();
                            CoeusVector cvNegotiationLocHis = negotiationTxnBean.
				getNegotiationLocationHistory(negotiationNumber);
                            responder.setDataObjects(cvNegotiationLocHis);
                            responder.setMessage(null);
                            responder.setResponseStatus(true);
                      
                        //case 3590 end
                        //case 3961 start
                        } else if (functionType == INST_PROP_COUNT) {
				negotiationNumber = (String)requester.getDataObject();
				int instPropCount = negotiationTxnBean.getInstituteProposalCount(negotiationNumber);
				boolean hasInstProp = false;
				if (instPropCount > 0) {
					hasInstProp = true;
				}
				responder.setDataObject(new Boolean(hasInstProp));
				responder.setMessage(null);
				responder.setResponseStatus(true);
                        }//case 3961 end
                       //case 4185 - Saving negotiation Location History -Start 
                       else if (functionType == SAVE_LOCATION_HISTORY) {
                            Vector locData  = (Vector)requester.getDataObjects();
                            negotiationNumber = (String)locData.get(0);
                            CoeusVector cvLocData = (CoeusVector)locData.get(1);
                            negotiationTxnBean = new NegotiationTxnBean(loggedinUser);
                            negotiationTxnBean.updateLocationHistory(cvLocData);
                            CoeusVector cvNegotiationLocaton = negotiationTxnBean.getNegotiationLocation(negotiationNumber);
                            responder.setDataObject(cvNegotiationLocaton);
                            responder.setMessage(null);
                            responder.setResponseStatus(true);
                       }
                       //case 4185 - Saving negotiation Location History -End
                        //case 2806 - Upload documents to negotiations -Start 
                        else if (functionType == GET_BLOB_DATA_FOR_NEGOTIATION){
                            NegotiationAttachmentBean attachmentBean 
                                = (NegotiationAttachmentBean)requester.getDataObject();
                        negotiationTxnBean = new NegotiationTxnBean(loggedinUser);
                        attachmentBean = negotiationTxnBean.getNegotiationDocument(attachmentBean);
                        responder.setDataObject(attachmentBean);
                        responder.setResponseStatus(true);
                        }
                        //case 2806 - Upload documents to negotiations -Start
                        // 3587: Multi Campus Enahncements- Start
                        else if(functionType == CAN_MODIFY_NEGOTIATION){
                            // 3587: Multi Campus Enahncements - Start
                            
                            negotiationNumber = (String)requester.getDataObject();
                            boolean modifyRight = false;
                            userMaintDataTxnBean = new UserMaintDataTxnBean();
                            negotiationTxnBean = new NegotiationTxnBean();
                            NegotiationHeaderBean negotiationHeaderBean = negotiationTxnBean.getNegotiationHeader(negotiationNumber);
                            leadUnit = negotiationHeaderBean.getLeadUnit();
                            //To check MODIY_ACTIVITIES right if MODIFY_NEGOTAITIONS right exist
                            modifyRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_NEGOTIATIONS, leadUnit);
                            //Commented for  COEUSDEV-268 : user without the modify negotiation role should not see the new modify activity buttons as enabled - Start
                            //Negotiation can be edited only if user has MODIFY_NEGOTIATION not MODIFY_ACTIVITIES
//                            if(!modifyRight){
//                                modifyRight = userMaintDataTxnBean.getUserHasRight(loggedinUser, MODIFY_ACTIVITIES, leadUnit);
//                            }
                            //COEUSDEV-268 : End
                            responder.setDataObject(new Boolean(modifyRight));
                            responder.setResponseStatus(true);
                        }
                        // 3587: Multi Campus Enahncements - End
                        //Added for COEUSDEV-294 : Error adding activity to a negotiation
                        else if (functionType == GET_MAX_NEGOTIATION_ACTIVITY_NUMBER){
                             negotiationNumber = (String)requester.getDataObject();
                             int maxNegotiationActivityNumber = negotiationTxnBean.getMaxNegotiationActivityNumber(negotiationNumber);
                             responder.setDataObject(new Integer(maxNegotiationActivityNumber));
                            responder.setResponseStatus(true);
                        }
                        //COEUSDEV-294 : End
                        //COEUSDEV - 733 Create a new notification for negotiation module - Start
                        else if(functionType == COMPARE_PARAMETER_VALUE) {
                            boolean hasRight = false;
                            Vector paramData  = (Vector)requester.getDataObject();
                            String parameterName = (String)paramData.get(0);
                            String negotiationStatusCode = (String)paramData.get(1).toString();
                            String negotiationParameterValue = negotiationTxnBean.getParameterValues(parameterName);
                            if(negotiationStatusCode!=null && negotiationParameterValue!=null){
                                hasRight = negotiationStatusCode.equalsIgnoreCase(negotiationParameterValue);
                            }                            
                            CoeusVector cvParameterValues = new CoeusVector();
                            cvParameterValues.add(hasRight);
                            cvParameterValues.add(loggedinUser);
                            responder.setDataObject(cvParameterValues);
                            responder.setResponseStatus(true);
                        }
                        //else if(functionType == UPDATE_INBOX) {
//                            Vector mailData = (Vector)requester.getDataObject();
//                            boolean updated = negotiationTxnBean.addUpdDeleteInbox(mailData);
//                            dataObjects = proposalActionTxnBean.getInboxForUser(loggedinUser);
//                            responder.setDataObjects(dataObjects);
//                            responder.setResponseStatus(true);
//                            responder.setMessage(null);
                        //}
                        //COEUSDEV - 733 Create a new notification for negotiation module - End
		}catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setException(lockEx);
                responder.setResponseStatus(false);            
                responder.setMessage(errMsg);
                UtilFactory.log( errMsg, lockEx,
			"NegotiationMaintenanceServlet", "doPost");
                }catch( CoeusException coeusEx ) {
			//coeusEx.printStackTrace();
			int index=0;
			String errMsg;
			if(coeusEx.getErrorId()==999999){
				errMsg = "dbEngine_intlErr_exceptionCode.1028";
				responder.setLocked(true);
			}else{
				errMsg = coeusEx.getMessage();
			}
			CoeusMessageResourcesBean coeusMessageResourcesBean
			=new CoeusMessageResourcesBean();
			errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
			responder.setException(coeusEx);
			responder.setResponseStatus(false);
			
			responder.setMessage(errMsg);
                        UtilFactory.log( errMsg, coeusEx,
			"NegotiationMaintenanceServlet", "doPost");
			
		}catch( DBException dbEx ) {
			//dbEx.printStackTrace();
			int index=0;
			String errMsg = dbEx.getUserMessage();
			if (dbEx.getErrorId() == 20102 ) {
				errMsg = "dbEngine_intlErr_exceptionCode.1028";
			}
			if (errMsg.equals("db_exceptionCode.1111")) {
				responder.setCloseRequired(true);
			}
			CoeusMessageResourcesBean coeusMessageResourcesBean
			= new CoeusMessageResourcesBean();
			errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
			
			responder.setResponseStatus(false);
			responder.setException(dbEx);
			responder.setMessage(errMsg);
			UtilFactory.log( errMsg, dbEx,
			"NegotiationMaintenanceServlet", "doPost");
			
		}catch(Exception e) {
			//e.printStackTrace();
			responder.setResponseStatus(false);
                        responder.setException(e);
			responder.setMessage(e.getMessage());
			UtilFactory.log( e.getMessage(), e,
			"NegotiationMaintenanceServlet", "doPost");
                 //Case 3193 - START
                }catch(Throwable throwable){
                    Exception ex = new Exception(throwable);
                    responder.setException(ex);
                    responder.setResponseStatus(false);
                    responder.setMessage(ex.getMessage());
                    UtilFactory.log( throwable.getMessage(), throwable, "NegotiationMaintenanceServlet", "doPost");
                //Case 3193 - END
		} finally {
			try{
				
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
				"NegotiationMaintenanceServlet", "doPost");
			}
		}
	}
}
