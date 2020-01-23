/*
 * CentralAdminMaintenanceServlet.java
 *
 * Created on December 21, 2004, 5:29 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.award.bean.SapFeedDetailsBean;
import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Date;
import java.text.SimpleDateFormat;


import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.centraladmin.bean.CentralAdminTxnBean;
import edu.mit.coeus.propdev.bean.MessageBean;
import edu.mit.coeus.propdev.bean.ProposalActionTxnBean;
import edu.mit.coeus.subcontract.bean.RTFFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
import edu.mit.coeus.user.bean.UserPreferencesBean;
import edu.mit.coeus.utils.CoeusFunctions;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  shivakumarmj
 * @version
 */

public class CentralAdminMaintenanceServlet extends CoeusBaseServlet implements TypeConstants{
    
    private final char INSERT_MESSAGE = 'A';
    
    private final char GET_FEED_COUNT = 'B';
    
    private final char GENERATE_MASTER_DATA_FEED = 'C';
    
    private final char DELETE_FORM_ID = 'E';
    
    private final char UPDATE_FORM_ID = 'F';
    
    private final char EOM_DATA = 'G';
    
    private final char EOM_PROCESS_DATA = 'H';
    
    private final char SUBCONTRACT_EXPENSE_DATA = 'I';
    
    private final char FEED_MAINT_FEED_BATCHES ='J';
    
    private final char ALL_SAP_FEED_DETAILS = 'K';
    
    private final char GET_FISCAL_MONTH_PARAMETER = 'L';
    
    private static final char INIT_ROLODEX_FEED = 'M';
    
    private static final char GENERATE_ROLODEX_FEED = 'N';
    
    private static final char INIT_SPONSOR_FEED = 'O';
    
    private static final char GENERATE_SPONSOR_FEED = 'P';
    
    private static final char FEED_AWARD_HISTORY = 'Q';
    
    private static final char UPDATE_FEED_DETAILS = 'R';
    
    private static final char GET_ALL_PENDING_FEEDS = 'S';
    
    private static final char GET_FEED_DATA_FOR_BATCH = 'T';
    
    private static final char FEED_DATA = 'U';
    
    private static final char RESEND_FEED = 'V';
    
    private static final char CANCEL_FEED = 'W';
    
    private static final char GET_MESSAGE = 'X';
    
    private static final char UPDATE_MESSAGE = 'Y';

    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            //Should always be the User Id with which user logged in - March 20, 2004
            //loggedinUser = userBean.getUserId();
            loggedinUser = requester.getUserName();
            unitNumber = userBean.getUnitNumber();
            char functionType = requester.getFunctionType();
            if (functionType == INSERT_MESSAGE) {
                String message = (String)requester.getDataObject();
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                boolean status = centralAdminTxnBean.insertMessage(message, loggedinUser);
                responder.setMessage(null);
                responder.setResponseStatus(status);
            }else if(functionType == GET_FEED_COUNT){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvFeedData = new CoeusVector();
                // Getting the feed count
                int feedCount = centralAdminTxnBean.getPendingFeedCount();
                // Getting the status of the sponsor table
                boolean sponsorCheck = centralAdminTxnBean.checkSponsorTableHasChanged();
                // Adding the data to CoeusVector
                cvFeedData.addElement(new Integer(feedCount));
                cvFeedData.addElement(new Boolean(sponsorCheck));
                //sending the directory path to the client side
                String developmentPath = CoeusProperties.getProperty("Development".toUpperCase());
                String productionPath = CoeusProperties.getProperty("Production".toUpperCase());
                String testPath = CoeusProperties.getProperty("Test".toUpperCase());
                cvFeedData.addElement(developmentPath);
                cvFeedData.addElement(productionPath);
                cvFeedData.addElement(testPath);
                responder.setDataObjects(cvFeedData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(functionType == GENERATE_MASTER_DATA_FEED){
                String genMode = (String)requester.getDataObject();
                // Modified for COEUSDEV-563:Award Sync to Parent is not triggering SAP feed for child accounts its touching
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean(loggedinUser);
                // COEUSDEV-563: End
                boolean status = false;
                // Getting path from properties file
                String dirPath = CoeusProperties.getProperty(genMode.toUpperCase());
                CoeusVector cvValues = null;
                // Modified error handling with COEUSDEV-563: Award Sync to Parent is not triggering SAP feed
                try {
                    cvValues = centralAdminTxnBean.generateSAPFeed(dirPath);
                    
                    if(cvValues != null && !cvValues.isEmpty()) {
                        responder.setDataObject(cvValues);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    } else {
                        responder.setResponseStatus(false);
                        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                        responder.setMessage(coeusMessageResourcesBean.parseMessageKey("generateMasterDataFeed_exceptionCode.1001"));
//                        responder.setMessage("Error generating Feed - ");
                    }
                } catch (Exception ex) {
                    UtilFactory.log(ex.getMessage(), ex, "CentralAdminMaintenanceServlet", "GENERATE_MASTER_DATA_FEED");
//                    if (ex.getErrorId() == 0) {
                    responder.setResponseStatus(false);
                    CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
                    responder.setMessage(coeusMessageResourcesBean.parseMessageKey("generateMasterDataFeed_exceptionCode.1001"));
//                        responder.setMessage("Please select a valid target directory for the Feed.");
//                    }
                }
                // COEUSDEV-563 : End
            }else if(functionType == DELETE_FORM_ID){
                String formId = (String)requester.getDataObject();
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                boolean deleteStatus = centralAdminTxnBean.deleteRTFForm(formId);
                responder.setMessage(null);
                responder.setResponseStatus(deleteStatus);
            }else if(functionType == UPDATE_FORM_ID){
                RTFFormBean rtfFormBean = (RTFFormBean)requester.getDataObject();
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean(loggedinUser);
                boolean updateStatus = centralAdminTxnBean.updateRTFForm(rtfFormBean);
                responder.setMessage(null);
                responder.setResponseStatus(updateStatus);
            }else if(functionType == EOM_DATA){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusFunctions functions = new CoeusFunctions();
                String fiscalYear = functions.getParameterValue("CURRENT_FISCAL_YEAR");
                int count = centralAdminTxnBean.getCountForEOM();
                String date = functions.getParameterValue("EOM_PROCESS_DATE");
                String user = functions.getParameterValue("EOM_PROCESS_USER");
                Hashtable dataTable = new Hashtable();
                dataTable.put(new Integer(0),fiscalYear);
                dataTable.put(new Integer(1),new Integer(count));
                dataTable.put(new Integer(2), date);
                dataTable.put(new Integer(3), user);
                responder.setDataObject(dataTable);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == EOM_PROCESS_DATA){
                
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvParamData = (CoeusVector)requester.getDataObject();
                String year = cvParamData.get(0).toString();
                int month = Integer.parseInt(cvParamData.get(1).toString());
                int processValue = centralAdminTxnBean.processEOM(year,month);
                if(processValue == 0) {
                    CoeusFunctions functions = new CoeusFunctions();
                    java.sql.Timestamp timeStamp = functions.getDBTimestamp();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(timeStamp);
                    String amOrPm = null;
                    if(cal.AM == cal.get(cal.AM_PM))
                        amOrPm =  "am";
                    if(cal.PM == cal.get(cal.AM_PM))
                        amOrPm =  "pm";
                    String DATE_FORMAT = "MMM-dd-yyyy HH:mm:ss ";
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                    sdf.setTimeZone(cal.getTimeZone());
                    String rolodexDate = sdf.format(cal.getTime());
                    boolean isDateUpdated = centralAdminTxnBean.setParameterValue("EOM_PROCESS_DATE",rolodexDate+amOrPm);
                    boolean isUserUpdated = centralAdminTxnBean.setParameterValue("EOM_PROCESS_USER",loggedinUser);
                }
                responder.setDataObject(new Integer(processValue));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SUBCONTRACT_EXPENSE_DATA){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvData = (CoeusVector)requester.getDataObject();
                String periodStart = cvData.get(0).toString();
                String periodEnd = cvData.get(1).toString();
                int status = centralAdminTxnBean.populateSubcontractExpData(periodStart, periodEnd);
                responder.setDataObject(new Integer(status));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == FEED_MAINT_FEED_BATCHES){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvFeedBatch = new CoeusVector();
                cvFeedBatch = centralAdminTxnBean.getAllSAPFeedBatches();
                responder.setDataObject(cvFeedBatch);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == ALL_SAP_FEED_DETAILS){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvAllSapFeedDetails = new CoeusVector();
                String batchId = requester.getDataObject().toString();
                int batchValue = Integer.parseInt(batchId);
                cvAllSapFeedDetails = centralAdminTxnBean.getAllSAPFeedDetails(batchValue);;
                responder.setDataObject(cvAllSapFeedDetails);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_FISCAL_MONTH_PARAMETER){
                CoeusFunctions functions = new CoeusFunctions();
                String monthParameter = functions.getParameterValue("FISCAL_YEAR_START_MONTH");
                responder.setDataObject(monthParameter);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            } else if (functionType == INIT_ROLODEX_FEED) {
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvRolFeedData = new CoeusVector();
                //sending the directory path to the client side
                String developmentPath = CoeusProperties.getProperty("Development".toUpperCase());
                String productionPath = CoeusProperties.getProperty("Production".toUpperCase());
                String testPath = CoeusProperties.getProperty("Test".toUpperCase());
                cvRolFeedData.addElement(developmentPath);
                cvRolFeedData.addElement(productionPath);
                cvRolFeedData.addElement(testPath);
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String rolodexFeedDate = coeusFunctions.getParameterValue("ROLODEX_FEED_DATE");
                cvRolFeedData.addElement(rolodexFeedDate);
                String rolodexFeedUser = coeusFunctions.getParameterValue("ROLODEX_FEED_USER");
                cvRolFeedData.addElement(rolodexFeedUser);
                responder.setDataObject(cvRolFeedData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            } else if(functionType == GENERATE_ROLODEX_FEED) {
                String genMode = (String)requester.getDataObject();
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                boolean status = false;
                boolean isErrorOccured = false;
                CoeusVector cvStatus = new CoeusVector();
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                // Getting path from properties file
                String dirPath = CoeusProperties.getProperty(genMode.toUpperCase());
                try {
                    status = centralAdminTxnBean.generateRolodexFeed(dirPath);
                    
                    if (status == true) {
                        boolean isUserUpdated = centralAdminTxnBean.setParameterValue("ROLODEX_FEED_USER",loggedinUser);
                        Timestamp dbTimeStamp = coeusFunctions.getDBTimestamp();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dbTimeStamp);
                        String amOrPm = null;
                        if(cal.AM == cal.get(cal.AM_PM))
                            amOrPm =  "am";
                        if(cal.PM == cal.get(cal.AM_PM))
                            amOrPm =  "pm";
                        String DATE_FORMAT = "MMM-dd-yyyy HH:mm:ss ";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                        sdf.setTimeZone(cal.getTimeZone());
                        String rolodexDate = sdf.format(cal.getTime());
                        boolean isDateUpdated = centralAdminTxnBean.setParameterValue("ROLODEX_FEED_DATE",rolodexDate+amOrPm);
                        cvStatus.add(0,new Boolean(status));
                        cvStatus.add(1,new Boolean(isErrorOccured));
                        responder.setDataObject(cvStatus);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }
                } catch (DBException ex) {
                    if (ex.getErrorId() == 0) {
                        isErrorOccured = true;
                        cvStatus.add(0,new Boolean(false));
                        cvStatus.add(1,new Boolean(isErrorOccured));
                        responder.setDataObject(cvStatus);
                        responder.setResponseStatus(true);
                    }
                }
            } else if (functionType == INIT_SPONSOR_FEED) {
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvSponsorFeedData = new CoeusVector();
                // Getting the status of the sponsor table
                boolean sponsorCheck = centralAdminTxnBean.checkSponsorTableHasChanged();
                // Adding the data to CoeusVector
                cvSponsorFeedData.addElement(new Boolean(sponsorCheck));
                //sending the directory path to the client side
                String developmentPath = CoeusProperties.getProperty("Development".toUpperCase());
                String productionPath = CoeusProperties.getProperty("Production".toUpperCase());
                String testPath = CoeusProperties.getProperty("Test".toUpperCase());
                cvSponsorFeedData.addElement(developmentPath);
                cvSponsorFeedData.addElement(productionPath);
                cvSponsorFeedData.addElement(testPath);
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String sponsorFeedDate = coeusFunctions.getParameterValue("SPONSOR_FEED_DATE");
                cvSponsorFeedData.addElement(sponsorFeedDate);
                String sponsorFeedUser = coeusFunctions.getParameterValue("SPONSOR_FEED_USER");
                cvSponsorFeedData.addElement(sponsorFeedUser);
                responder.setDataObject(cvSponsorFeedData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            } else if(functionType == GENERATE_SPONSOR_FEED) {
                String genMode = (String)requester.getDataObject();
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                boolean status = false;
                boolean isErrorOccured = false;
                CoeusVector cvStatus = new CoeusVector();
                // Getting path from properties file
                String dirPath = CoeusProperties.getProperty(genMode.toUpperCase());
                try {
                    status = centralAdminTxnBean.generateSponsorFeed(dirPath);
                    
                    if (status == true) {
                        boolean isUserUpdated = centralAdminTxnBean.setParameterValue("SPONSOR_FEED_USER",loggedinUser);
                        Timestamp dbTimeStamp = coeusFunctions.getDBTimestamp();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dbTimeStamp);
                        String amOrPm = null;
                        if(cal.AM == cal.get(cal.AM_PM))
                            amOrPm =  "am";
                        if(cal.PM == cal.get(cal.AM_PM))
                            amOrPm =  "pm";
                        String DATE_FORMAT = "MMM-dd-yyyy HH:mm:ss ";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
                        sdf.setTimeZone(cal.getTimeZone());
                        String sponsorDate = sdf.format(cal.getTime());
                        boolean isDateUpdated = centralAdminTxnBean.setParameterValue("SPONSOR_FEED_DATE",sponsorDate+amOrPm);
                        cvStatus.add(0,new Boolean(status));
                        cvStatus.add(1,new Boolean(isErrorOccured));
                        responder.setDataObject(cvStatus);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }
                } catch (DBException ex) {
                    if (ex.getErrorId() == 0) {
                        isErrorOccured = true;
                        cvStatus.add(0,new Boolean(false));
                        cvStatus.add(1,new Boolean(isErrorOccured));
                        responder.setDataObject(cvStatus);
                        responder.setResponseStatus(true);
                    }
                }
            }else if(functionType == FEED_AWARD_HISTORY){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvAwardHistory = new CoeusVector();
                String awardNumber = requester.getDataObject().toString();
                cvAwardHistory = centralAdminTxnBean.getSAPFeedForAwards(awardNumber);;
                responder.setDataObject(cvAwardHistory);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == UPDATE_FEED_DETAILS){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                SapFeedDetailsBean sapFeedDetailsBean = null;
                CoeusVector cvUpdateVector = new CoeusVector();
                boolean success = false;
                cvUpdateVector = (CoeusVector)requester.getDataObject();
                if(cvUpdateVector != null && cvUpdateVector.size() > 0){
                    for(int index=0; index < cvUpdateVector.size(); index++){
                        sapFeedDetailsBean= (SapFeedDetailsBean)cvUpdateVector.elementAt(index);
                        if(sapFeedDetailsBean.getAcType()==null) {
                            continue;
                        }
                        CoeusVector cvStatus = centralAdminTxnBean.updateSAPFeedDetails(sapFeedDetailsBean);
                        cvStatus.add(2, loggedinUser);
                        boolean isUpdated = ((Boolean)cvStatus.get(0)).booleanValue();
                        if (isUpdated == true) {
                            responder.setDataObject(cvStatus);
                            responder.setResponseStatus(true);
                            responder.setMessage(null);
                        } else {
                            responder.setResponseStatus(false);
                        }
                    }
                }
            }else if(functionType == GET_ALL_PENDING_FEEDS){
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvPendingFeeds = new CoeusVector();
                cvPendingFeeds = centralAdminTxnBean.getAllPendingFeed();
                responder.setDataObject(cvPendingFeeds);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == CANCEL_FEED){
                CoeusVector cvCancelFeed = new CoeusVector();
                Timestamp dbTimeStamp = new CoeusFunctions().getDBTimestamp();
                cvCancelFeed.addElement(dbTimeStamp);
                cvCancelFeed.addElement(loggedinUser);
                responder.setDataObject(cvCancelFeed);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_FEED_DATA_FOR_BATCH){
                String batchId = requester.getDataObject().toString();
                int batchValue = Integer.parseInt(batchId);
                Hashtable feedValues = new Hashtable();
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvFeedForBatch = new CoeusVector();
                cvFeedForBatch = centralAdminTxnBean.getFeedForBatch(batchValue);
                CoeusVector cvFeedData = new CoeusVector();
                //sending the directory path to the client side
                String developmentPath = CoeusProperties.getProperty("Development".toUpperCase());
                String productionPath = CoeusProperties.getProperty("Production".toUpperCase());
                String testPath = CoeusProperties.getProperty("Test".toUpperCase());
                cvFeedData.addElement(developmentPath);
                cvFeedData.addElement(productionPath);
                cvFeedData.addElement(testPath);
                feedValues.put(SapFeedDetailsBean.class, cvFeedForBatch);
                feedValues.put("DIRECTORY_PATH", cvFeedData);
                responder.setDataObject(feedValues);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == FEED_DATA){
                String batchId = requester.getDataObject().toString();
                int batchValue = Integer.parseInt(batchId);
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvFeedBatch = new CoeusVector();
                cvFeedBatch = centralAdminTxnBean.getFeedData(batchValue);
                responder.setDataObject(cvFeedBatch);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == RESEND_FEED) {
                CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
                CoeusVector cvFeed = (CoeusVector)requester.getDataObject();
				CoeusVector cvStatus = new CoeusVector();
                boolean isSend = false;
				boolean isErrorOccured = false;
				try {
					if (cvFeed != null && cvFeed.size() > 0) {
						int batchId = ((Integer)cvFeed.get(0)).intValue();
						String genMode = (String)cvFeed.get(1);
						// Getting path from properties file
						String dirPath = CoeusProperties.getProperty(genMode.toUpperCase());
						isSend = centralAdminTxnBean.resendFeed(batchId, dirPath);
						cvStatus.add(0,new Boolean(isSend));
                        cvStatus.add(1,new Boolean(isErrorOccured));
                        responder.setDataObject(cvStatus);
                        responder.setResponseStatus(true);
						responder.setMessage(null);
					}
				} catch (DBException ex ) {
					if (ex.getErrorId() == 0) {
						isErrorOccured = true;
                        cvStatus.add(0,new Boolean(false));
                        cvStatus.add(1,new Boolean(isErrorOccured));
                        responder.setDataObject(cvStatus);
                        responder.setResponseStatus(true);
						responder.setMessage(ex.getMessage());
                    }
				}
			} else if (functionType == GET_MESSAGE) {
                String parameterValue = new CoeusFunctions().getParameterValue("PUBLIC_MESSAGE_ID");
                HashMap data = new HashMap();
                if (!("").equals(parameterValue) && parameterValue != null) {
                    data.put("MESSAGE_ID", parameterValue);
                    
                    ProposalActionTxnBean proposalActionTxnBean = new ProposalActionTxnBean();
                    MessageBean messageBean = proposalActionTxnBean.getMessage(parameterValue);
                    data.put(MessageBean.class, messageBean);
                    
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    java.util.Vector userPreferences = new Vector();
                    String userId = (String)requester.getDataObject();
                    userPreferences = userMaintDataTxnBean.getUserPreferences(userId);
                    data.put(UserPreferencesBean.class, userPreferences);
                    
                    responder.setDataObject(data);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }
            } else if (functionType == UPDATE_MESSAGE){
                java.util.Vector userPreferences = (java.util.Vector)requester.getDataObject();
                if(userPreferences != null){
                    for(int i=0;i<userPreferences.size();i++){
                        UserPreferencesBean bean = (UserPreferencesBean)userPreferences.elementAt(i);
                        if(bean != null){
                            UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(bean.getUserId());
                            userMaintUpdateTxnBean.addUpdDeletePreferences(userPreferences);
                            break;
                        }
                    }
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
        } catch( LockingException lockEx ) {
            //lockEx.printStackTrace();
            LockingBean lockingBean = lockEx.getLockingBean();
            String errMsg = lockEx.getErrorMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean
            =new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            responder.setException(lockEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, lockEx,
            "CentralAdminMaintenanceServlet", "perform");
        }
        catch( CoeusException coeusEx ) {
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
            
            responder.setResponseStatus(false);
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx,
            "CentralAdminMaintenanceServlet", "perform");
            
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
            "CentralAdminMaintenanceServlet", "perform");
            
        }catch(Exception e) {
            //e.printStackTrace();
            responder.setResponseStatus(false);
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
            "CentralAdminMaintenanceServlet", "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "CentralAdminMaintenanceServlet", "doPost");
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
                "CentralAdminMaintenanceServlet", "perform");
            }
        }
        
    }
}
