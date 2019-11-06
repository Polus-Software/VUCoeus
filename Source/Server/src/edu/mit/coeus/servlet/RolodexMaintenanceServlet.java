/*
 * @(#)RolodexMaintenanceServlet.java 1.0 16/08/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 21-APR-2011
 * by Bharati
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;


//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import javax.servlet.*;
import javax.swing.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.rolodexmaint.bean.RolodexReferencesBean; 
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import java.util.HashMap;


/**
 * <code>RolodexMaintenanceServlet</code> is a servlet which receives
 * requesterBean (the informatin from the Applet regarding the Component which
 * to be displayed in MDI form). This servlet delegates the process to the
 * Form Factory to get the required component and set the Responder bean with
 * the related information and send the info aas a serialized object to the
 * Applet.
 *
 * @version 1.0 August 16,2002
 * @author Phaneendra Kumar.
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling.
 *
 */

public class RolodexMaintenanceServlet extends CoeusBaseServlet {
    
    /** Initializes the servlet.
     */
    private final String IS_MAINTAIN_ROLODEX = "MAINTAIN_ROLODEX";
    private final String IS_ADD_ROLODEX = "ADD_ROLODEX";
    private final String IS_MODIFY_ROLODEX = "MODIFY_ROLODEX";
    private final String IS_DELETE_ROLODEX = "DELETE_ROLODEX";
    
    //Function Types
    private static final char GET_ROLODEX_REFERENCES = 'E';
    private static final char REPLACE_ROLODEX_REFERENCES = 'G';
    private static final char GET_VALID_SPONSOR_CODE = 'P';
    private static final char CAN_MODIFY_ROLODEX_STATUS = 'c';
    //Rights
    private static final String SYSTEM_MAINTENANCE = "SYSTEM_MAINTENANCE";
    
    //Added for Case#4252 - Rolodex state dropdown associated with country - Start
    private static final char GET_STATE_WITH_COUNTRY = 's';
    //Case#4252 - End
    // Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record  - Start
    private static final char GET_CODE_FOR_VALID_SPONSOR = 'p';
    // Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record  - End
            
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    public void service(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        //System.out.println("ENTERED INTO SERVLET");
        //Commented by Geo. No session handling is required
//        HttpSession session;
        // the request object
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        //Commented by Geo. session handling is not required
//        session = request.getSession(true);
        String loggedinUser ="";
        String loggedinUnitNumber ="";
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        RolodexDetailsBean formData=null;
        //Modified on 14 May, 2004 - start
        //RolodexMaintenanceDataTxnBean rdTxnBean = new RolodexMaintenanceDataTxnBean();
        RolodexMaintenanceDataTxnBean rdTxnBean = null;
        //Modified on 14 May, 2004 - ens
      
//        UtilFactory UtilFactory = new UtilFactory();
        
        
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            /* Get the user info Bean from the session for the logged
             * in user information
             */
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            loggedinUser = userBean.getUserId();
            loggedinUnitNumber = userBean.getUnitNumber();
            //Added on 14 May, 2004 - start
            rdTxnBean = new RolodexMaintenanceDataTxnBean(loggedinUser);
            //Added on 14 May, 2004 - end
            String rldxId = "";
            if ( (requester.getFunctionType() == 'U') ||
            (requester.getFunctionType() == 'I') ){
                /* Get the rolodex details bean sent from the Applet for
                 * saving the information
                 */
                formData = (RolodexDetailsBean)requester.getDataObject();
                if (requester.getFunctionType() == 'I') {
                    /* If the function type is to insert the record then get
                     * the next sequence number for rolodex id
                     */
                    rldxId = rdTxnBean.getNextSeqNum();
                    formData.setRolodexId(rldxId);
                    formData.setOwnedByUnit(loggedinUnitNumber);
                }
                //if (rdTxnBean.addUpdateRolodexMaintenanceDetails(formData)) {
                // Code commented by Shivakumar for locking enhancement
//                if (rdTxnBean.addUpdateRolodexMaintenanceDetails(formData,requester.getFunctionType())) {
                    // Code added by Shivakumar for locking enhancement - BEGIN
                // Calling releaseLock method for fixing bug in locking
                LockingBean lockingBean = new LockingBean();
                if(formData.getAcType() != null && formData.getAcType().equals("I")){                    
                    lockingBean  = rdTxnBean.addUpdateRolodexMaintenanceDetails(formData,requester.getFunctionType(),loggedinUser);                    
                    //lockingBean = rdTxnBean.getLock(formData.getRolodexId(), loggedinUser, loggedinUnitNumber);                    
                }else{
                    boolean lockCheck = rdTxnBean.lockCheck(formData.getRolodexId(), loggedinUser);
                    if(!lockCheck){
                        lockingBean  = rdTxnBean.addUpdateRolodexMaintenanceDetails(formData,requester.getFunctionType(),loggedinUser);
                    }else{
                        //String msg = "Sorry,  the lock for rolodex Id "+formData.getRolodexId()+"  has been deleted by DB Administrator ";
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                                                =new CoeusMessageResourcesBean();
                        String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1009")+" "+formData.getRolodexId()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");
                        throw new LockingException(msg);
                    }    
                }    
//                lockingBean  = rdTxnBean.addUpdateRolodexMaintenanceDetails(formData,requester.getFunctionType(),loggedinUser);
                boolean success = lockingBean.isGotLock();
                    //if (rdTxnBean.addUpdateRolodexMaintenanceDetails(formData,requester.getFunctionType(),loggedinUser)) {                            
                if (success) {                            
                    // Code added by Shivakumar for locking enhancement - END
                    RolodexDetailsBean rdNewBean = null;
                    rdNewBean = rdTxnBean.getRolodexMaintenanceDetails(formData.getRolodexId());
                    //rdNewBean = rdTxnBean.getRolodexDetails(formData.getRolodexId(),false);
                    responder.setLockingBean(lockingBean);
                    responder.setDataObject(rdNewBean);
                    responder.setResponseStatus(true);
                }else {
                    responder.setMessage("Rolodex updation failed");
                    responder.setResponseStatus(false);
                }
                
                
            }else if (requester.getFunctionType() == 'S'){
                /* If the requester function type is 'S' the servlet should get
                 * the sposnor name for the sponsor code and send the
                 * sponsorName back to the applet in responder bean
                 * serialized object
                 */
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                Vector vecResponseObject = new Vector();
                String sponsorCode = requester.getDataObject().toString().trim();
                String sponsorName = rdTxnBean.getSponsorName(sponsorCode);
                SponsorMaintenanceDataTxnBean spTxnBean = new SponsorMaintenanceDataTxnBean();
                SponsorMaintenanceFormBean sponsorMaintenanceFormBean = (SponsorMaintenanceFormBean)spTxnBean.getSponsorMaintenanceDetails(sponsorCode);
                String sponsorStatus = sponsorMaintenanceFormBean.getStatus();
                responder.setMessage("Sponsor Name retrieved successfuly");
                vecResponseObject.add(sponsorName);
                vecResponseObject.add(sponsorStatus);
                responder.setDataObjects(vecResponseObject);
                //responder.setDataObject(sponsorName);
                //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                responder.setResponseStatus(true);
            }else if (requester.getFunctionType() == GET_VALID_SPONSOR_CODE){
                /* If the requester function type is 'S' the servlet should get
                 * the sposnor name for the sponsor code and send the
                 * sponsorName back to the applet in responder bean
                 * serialized object
                 */
                String sponsorCode = requester.getDataObject()==null?"":requester.getDataObject().toString().trim();
                sponsorCode = rdTxnBean.getValidSponsorCode(sponsorCode);
                responder.setMessage("Sponsor Name retrieved successfuly");
                responder.setDataObject(sponsorCode);
                responder.setResponseStatus(true);
            }else if (requester.getFunctionType() == 'R'){
                /* If the function type is 'R' the servlet should get the
                 * rolodex id for the sponsorcode entered by the user
                 * in the applet
                 */
                String sponsorCode = requester.getDataObject().toString().trim();
                String rolodexId = rdTxnBean.getRolodexIdForSponsor(sponsorCode);
                responder.setResponseStatus(true);
                responder.setMessage("Sponsor Name retrieved successfuly");
                responder.setDataObject(rolodexId);
            }else if (requester.getFunctionType() == 'V'){
                /* If the requester function type is 'V' the servlet should
                 * get the rolodex Details bean for the rolodex id
                 * and send the bean back to the applet in responder
                 * bean serialized object
                 */
                String rolodexId = requester.getDataObject().toString().trim();
                //formData = rdTxnBean.getRolodexDetails(rolodexId);
                formData = rdTxnBean.getRolodexMaintenanceDetails(rolodexId);
                responder.setMessage("Sponsor Name retrieved successfuly");
                responder.setDataObject(formData);
                responder.setResponseStatus(true);
            }else if (requester.getFunctionType() == 'F'){
                /* If the requester function type is 'F' the servlet should get
                 * the countries and states information to display in
                 * RolodexDetails screen
                 */
                Vector responseData = new Vector();
                responseData.add(rdTxnBean.getStates());
                responseData.add(rdTxnBean.getCountries());
                responder.setResponseStatus(true);
                responder.setDataObjects(responseData);
            }else if (requester.getFunctionType() == 'M'){
                /* If the requester function type is 'M' the servlet should
                 * check the logged in user rights for modifying
                 * the selected rolodex information .
                 */
                boolean allowModify = false;
                String msgReply = "";
                int maintain_flag = 0;
                int modify_flag = 0;
                /** Bug Fix 1851 - start
                 *Step1
                 */
                boolean canLock = true;
                /** Bug Fix 1851 - start
                 *Step1
                 */
                String rolodexId = requester.getDataObject().toString().trim();
                //Added by Tarique Start here for Case #2008 Start
                try{
                   formData = rdTxnBean.getNewRolodexMaintenanceDetails(rolodexId,'M');
                    if (formData.getSponsorAddressFlag().trim().equalsIgnoreCase("Y")) {
                        msgReply =   "The rolodex entry " + formData.getRolodexId() +
                        " cannot be modified. It is the base address of sponsor '" +
                        formData.getSponsorCode() +
                        "'. \n Do you want to display it?";
                    }else {
                        
                        if ( rdTxnBean.isUserHasRight(
                        loggedinUser,
                        formData.getOwnedByUnit().toString().trim(),
                        IS_MAINTAIN_ROLODEX) ) {
                            maintain_flag = 1;
                            modify_flag = 1;
                        }else if ( rdTxnBean.isUserHasRight(loggedinUser,
                        loggedinUnitNumber,IS_MODIFY_ROLODEX) &&
                        (loggedinUnitNumber.equalsIgnoreCase(
                        formData.getOwnedByUnit().toString().trim())) &&
                        (loggedinUser.equalsIgnoreCase(
                        formData.getCreateUser().toString().trim())) )   {
                            modify_flag = 1;
                        }
                        
                        if  (modify_flag  == 0 )  {
                            msgReply =
                            "You do not have right to modify this rolodex '" +
                            formData.getRolodexId() +
                            "'. \n Do you want to display it?";
                        }else {
                            Vector result = rdTxnBean.checkRolodexIdIsUsed(
                            formData.getRolodexId(),loggedinUser, maintain_flag);
                            if ( result.get(0).toString().equalsIgnoreCase("0") ) {
                                msgReply = "You can not modify this rolodex '" +
                                formData.getRolodexId() +
                                "'.It has been used in '" +
                                result.get(1).toString() +
                                "'. \n Do you want to display it?";
                                /** Bug Fix 1851 - start
                                   *Step2
                                */
                                canLock = false;
                                /** Bug Fix 1851 - start
                                *Step2
                                */
                            }else {
                                allowModify = true;
                            }
                        }
                    }
                   
                }catch(DBException dbEx){
                    dbEx.printStackTrace();
                    rdTxnBean.transactionRollback();
                    throw dbEx;
                }
                
                if(modify_flag == 1 && canLock){// Bug Fix 1851
                    LockingBean lockingBean = rdTxnBean.getRolodexLock(rolodexId, loggedinUser, loggedinUnitNumber);
                    boolean success = lockingBean.isGotLock();
                    if(success){
                        Vector responseData = new Vector();
                        responseData.add(new Boolean(allowModify));
                        rdTxnBean.transactionCommit();
                        responder.setLockingBean(lockingBean);
                        responder.setResponseStatus(true);
                        responseData.add(msgReply);
                        responseData.add(formData);
                        responder.setDataObjects(responseData);
                        rdTxnBean.endConnection();
                    }
                }else{
                    Vector responseData = new Vector();
                    responseData.add(new Boolean(allowModify));
                    responder.setResponseStatus(true);
                    responseData.add(msgReply);
                    responseData.add(formData);
                    responder.setDataObjects(responseData);
                    rdTxnBean.endConnection();
                }
                //Added by Tarique End here for Case #2008 
                /* This is commented for testing the row locking mechanism*/
                //formData = rdTxnBean.getRolodexMaintenanceDetails(rolodexId);
                //formData = rdTxnBean.getRolodexDetails(rolodexId,false);
                // Code added by Shivakumar for locking enhancement - BEGIN
//                LockingBean lockingBean = rdTxnBean.getRolodexLock(rolodexId, loggedinUser, loggedinUnitNumber);
//                boolean success = lockingBean.isGotLock();
//                if(success){
//                    try{
//                // Code added by Shivakumar for locking enhancement - END
//                        // Code commented by Shivakumar for locking enhancement 
////                        formData = rdTxnBean.getRolodexMaintenanceDetails(rolodexId,'M');
//                        // Code added by Shivakumar for locking enhancement - BEGIN
//                        formData = rdTxnBean.getNewRolodexMaintenanceDetails(rolodexId,'M');
//                        // Code added by Shivakumar for locking enhancement - END
//                        if (formData.getSponsorAddressFlag().trim().equalsIgnoreCase("Y")) {
//                            msgReply =   "The rolodex entry " + formData.getRolodexId() +
//                            " cannot be modified. It is the base address of sponsor '" +
//                            formData.getSponsorCode() +
//                            "'. \n Do you want to display it?";
//                        }else {
//
//                            if ( rdTxnBean.isUserHasRight(
//                            loggedinUser,
//                            formData.getOwnedByUnit().toString().trim(),
//                            IS_MAINTAIN_ROLODEX) ) {
//                                maintain_flag = 1;
//                                modify_flag = 1;
//                            }else if ( rdTxnBean.isUserHasRight(loggedinUser,
//                            loggedinUnitNumber,IS_MODIFY_ROLODEX) &&
//                            (loggedinUnitNumber.equalsIgnoreCase(
//                            formData.getOwnedByUnit().toString().trim())) &&
//                            (loggedinUser.equalsIgnoreCase(
//                            formData.getCreateUser().toString().trim())) )   {
//                                modify_flag = 1;
//                            }
//
//                            if  (modify_flag  == 0 )  {
//                                msgReply =
//                                "You do not have right to modify this rolodex '" +
//                                formData.getRolodexId() +
//                                "'. \n Do you want to display it?";
//                            }else {
//                                Vector result = rdTxnBean.checkRolodexIdIsUsed(
//                                formData.getRolodexId(),loggedinUser, maintain_flag);
//                                if ( result.get(0).toString().equalsIgnoreCase("0") ) {
//                                    msgReply = "You can not modify this rolodex '" +
//                                    formData.getRolodexId() +
//                                    "'.It has been used in '" +
//                                    result.get(1).toString() +
//                                    "'. \n Do you want to display it?";
//                                }else {
//                                    allowModify = true;
//                                }
//                            }
//                        }
//                        /*if (allowModify) {
//                            //formData = rdTxnBean.getRolodexDetails(rolodexId,true);
//                            formData = rdTxnBean.getRolodexMaintenanceDetails(rolodexId,'M');
//                        }
//                         */
//                        Vector responseData = new Vector();
//                        responseData.add(new Boolean(allowModify));
//                        // Code added by Shivakumar for locking enhancement - BEGIN 1
//                        rdTxnBean.transactionCommit();
//                        responder.setLockingBean(lockingBean);
//                        // Code added by Shivakumar for locking enhancement - END 1
//                        responder.setResponseStatus(true);
//                        responseData.add(msgReply);
//                        responseData.add(formData);
//                        responder.setDataObjects(responseData);
//                        // Code added by Shivakumar for locking enhancement - BEGIN 
//                    }catch(DBException dbEx){
//                        dbEx.printStackTrace();
//                        rdTxnBean.transactionRollback();
//                        throw dbEx;
//                    }finally{
//                        rdTxnBean.endConnection();
//                    }    
//                        // Code added by Shivakumar for locking enhancement - END
//               }   
            }else if (requester.getFunctionType() == 'C'){
                /* If the requester function type is 'C' the servlet should
                 * check the logged in user rights for deleting
                 * the selected rolodex information .
                 */
                
                boolean allowDelete = false;
                String msgReply = "";
                int maintain_flag = 0;
                int delete_flag = 0;
                int okFlag =0;
                
                String rolodexId = requester.getDataObject().toString().trim();
                //To check whether the record has been already locked or not
                // Commented by Shivakumar for locking enhancement
//                rdTxnBean.canDeleteRolodex(rolodexId);
                // Code added by Shivakumar for locking enhancement - BEGIN
                rdTxnBean.canDeleteRolodex(rolodexId,loggedinUser);
                // Code added by Shivakumar for locking enhancement - END
                
                formData = rdTxnBean.getRolodexMaintenanceDetails(rolodexId);
                //formData = rdTxnBean.getRolodexDetails(rolodexId,false);
                if (
                formData.getSponsorAddressFlag().trim().equalsIgnoreCase(
                "Y") ) {
                    msgReply =   "The rolodex entry -" +
                    formData.getRolodexId() +
                    " cannot be deleted. It is associated by sponsor '" +
                    formData.getSponsorCode().toString() + "'";
                    
                }else {
                    
                    if ( rdTxnBean.isUserHasRight(loggedinUser,
                    loggedinUnitNumber,IS_DELETE_ROLODEX) &&
                    (loggedinUnitNumber.equalsIgnoreCase(
                    formData.getOwnedByUnit().toString().trim())) &&
                    (loggedinUser.equals(
                    formData.getCreateUser().toString().trim()))
                    )   {
                        delete_flag = 1;
                    }else  if ( rdTxnBean.isUserHasRight(loggedinUser,
                    formData.getOwnedByUnit().toString(),
                    IS_MAINTAIN_ROLODEX) ) {
                        delete_flag = 1;
                    }
                    if  (delete_flag  == 0 )  {
                        msgReply =
                        "You do not have right to delete this rolodex '" +
                        formData.getRolodexId() + "'";
                    }else {
                        Vector result = rdTxnBean.checkRolodexIdIsUsed(
                        formData.getRolodexId(),loggedinUser, maintain_flag);
                        okFlag =
                        (result.get(0) == null ? 0 : Integer.parseInt(
                        result.get(0).toString().trim()));
                        if ( okFlag == 0) {
                            msgReply = "You can not delete this rolodex '" +
                            formData.getRolodexId() +
                            "'.It has been used in " +
                            result.get(1).toString();
                        }else {
                            msgReply = "Do you want to delete  '" +
                            formData.getRolodexId() + "' ?";
                            allowDelete = true;
                        }
                    }
                }
                Vector responseData = new Vector();
                responseData.add(new Boolean(allowDelete));
                responder.setResponseStatus(true);
                responseData.add(msgReply);
                responder.setMessage("Delete privelege checked successfuly");
                responder.setDataObjects(responseData);
            }else if (requester.getFunctionType() == 'D'){
                /* If the requester function type is 'D' the servlet should
                 * delete the selected rolodex information from the database.
                 */
                String rolodexId = requester.getDataObject().toString().trim();
                if ( rdTxnBean.deleteRolodex(rolodexId) ) {
                    responder.setResponseStatus(true);
                }else  {
                    responder.setDataObject(
                    "RolodexId not found!!Deletion failed ");
                }
                
            }else if (requester.getFunctionType() == 'Z') {
                /*String refId = (requester.getDataObject()
                == null?null:requester.getDataObject().toString().trim());
                //System.out.println("************************************The RolodexMaintenanceServlet>>>>>>>refId for releasing Connection " + refId);
                releaseLock(refId);
                 */
                String rolodexId = requester.getDataObject().toString().trim();
                //rdTxnBean.releaseUpdateLock(rolodexId);
                // Commented by Shivakumar for locking enhancement
//                rdTxnBean.releaseEdit(rolodexId);
                // Code added by Shivakumar for locking enhancement - BEGIN
//                rdTxnBean.releaseEdit(rolodexId,loggedinUser);
                // Calling releaseLock method for bug fixng in locking
                LockingBean lockingBean  = rdTxnBean.releaseLock(rolodexId,loggedinUser);
                responder.setLockingBean(lockingBean);
                // Code added by Shivakumar for locking enhancement - END
                //System.out.println("going to release the rowid lock");
                //System.out.println("rolodexId=>"+rolodexId);
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
                
            }else if(requester.getFunctionType() == 'Y'){
                Vector vctRolodexRights = requester.getDataObjects();
                String systemMaintainRight = (String)vctRolodexRights.elementAt(0);
                String addRolodex = (String)vctRolodexRights.elementAt(1);
                String maintainRolodex = (String)vctRolodexRights.elementAt(2);
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasSystemMaintenance = userMaintDataTxnBean.getUserHasOSPRight(requester.getUserName(), systemMaintainRight);
                Vector responseData = new Vector();
                responseData.addElement(new Boolean(hasSystemMaintenance));
                boolean hasAddRight = userMaintDataTxnBean.getUserHasRight(requester.getUserName(), addRolodex, loggedinUnitNumber);
                boolean hasMaintainRolodexRight = userMaintDataTxnBean.getUserHasRight(requester.getUserName(), maintainRolodex, loggedinUnitNumber);
                //Check if User has both rights
                if(!hasAddRight && !hasMaintainRolodexRight){
                    responseData.addElement(new Boolean(false));
                }else{
                    responseData.addElement(new Boolean(true));
                }
                responder.setDataObjects(responseData);
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }else if(requester.getFunctionType() == GET_ROLODEX_REFERENCES){
                int rolodexId = ((Integer)requester.getDataObject()).intValue();
                //Get Rolodex Details
                formData = rdTxnBean.getRolodexMaintenanceDetails(""+rolodexId);
                //Get References Data
                CoeusVector references = rdTxnBean.getRolodexReferences(rolodexId);
                //Check for SYSTEM_MAINTENANCE OSP right
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, SYSTEM_MAINTENANCE);
                
                Vector dataObjects = new Vector();
                
                dataObjects.addElement(formData);
                dataObjects.addElement(references);
                dataObjects.addElement(new Boolean(hasRight));
                
                responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }else if(requester.getFunctionType() == REPLACE_ROLODEX_REFERENCES){
                RolodexReferencesBean rolodexReferencesBean = (RolodexReferencesBean)requester.getDataObject();
                int updated = rdTxnBean.replaceColumnValue(rolodexReferencesBean);
                responder.setResponseStatus(true);
                responder.setMessage(null);                                
            }
            //System.out.println("rolodex=>"+((RolodexDetailsBean)responder.getDataObject()).getRolodexId());
            //Added for Case#4252 - Rolodex state dropdown associated with country - Start
            else if(requester.getFunctionType() == GET_STATE_WITH_COUNTRY){
                SponsorMaintenanceDataTxnBean spTxnBean = new SponsorMaintenanceDataTxnBean();
                HashMap hmStateWithCountry = spTxnBean.fetchStatesWithCountry();
                responder.setDataObject(hmStateWithCountry);
                responder.setResponseStatus(true);
                responder.setMessage(null);                
            }
            //Case#4252 - End
            //COEUSQA-1528 -START
            else if(requester.getFunctionType() == CAN_MODIFY_ROLODEX_STATUS) {
                    loggedinUser = userBean.getUserId();
                    boolean isModifyStatus = rdTxnBean.isUserHasRight(loggedinUser,userBean.getUnitNumber().toString().trim()
                                                                      ,IS_MAINTAIN_ROLODEX);
                    responder.setDataObject(new Boolean(isModifyStatus));
                    responder.setResponseStatus(true);
            }
            //COEUSQA-1528 END
            // Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record  - Start
            else if (requester.getFunctionType() == GET_CODE_FOR_VALID_SPONSOR){
                /* If the requester function type is 'S' the servlet should get
                 * the sposnor name for the sponsor code and send the
                 * sponsorName back to the applet in responder bean
                 * serialized object
                 */
                String sponsorCode = requester.getDataObject()==null?"":requester.getDataObject().toString().trim();
                sponsorCode = rdTxnBean.getCodeForValidSponsor(sponsorCode);
                responder.setMessage("Valid Sponsor Name retrieved successfuly");
                responder.setDataObject(sponsorCode);
                responder.setResponseStatus(true);
            }
            // Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record  - End
            
        }catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String message = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
               String msg = coeusMessageResourcesBean.parseMessageKey(message);
               responder.setException(lockEx);
               responder.setResponseStatus(false);
               responder.setMessage(msg); 
               UtilFactory.log( msg, lockEx, "RolodexMaintenanceServlet",
                "perform");
                //return; 
        }  catch( CoeusException coeusEx ) {
            
            int index=0;
            String errMsg;
            //System.out.println("errorid=>"+coeusEx.getErrorId());
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setException(coeusEx);
            responder.setResponseStatus(false);
            
            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "RolodexMaintenanceServlet",
            "perform");
             
        } catch( DBException dbEx ) {
            //System.out.println("RolodexMaintenanceServlet >>> the exception occured is " + dbEx.getErrorId());
            //dbEx.printStackTrace();
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            //System.out.println("RolodexMaintenanceServlet >>> the error is " + errMsg);
            responder.setException(dbEx);
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setMessage(errMsg);
            
            UtilFactory.log( errMsg, dbEx, "RolodexMaintenanceServlet",
            "perform");
            
        } catch(Exception e) {
            responder.setResponseStatus(false);
            
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "RolodexMaintenanceServlet",
            "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "RolodexMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                // send the object to applet
                outputToApplet = new ObjectOutputStream(
                response.getOutputStream());
                //System.out.println("RolodexMaintenanceServlet >>>>>the responder error message is " + responder.getMessage());
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
                UtilFactory.log( ioe.getMessage(), ioe, "RolodexMaintenanceServlet",
                "perform");
            }
        }
    }
    
    /**
     *  This method is used for applets.
     *  Gets an input stream from the applet, reads the username and password
     *  and authenticates with the database.
     */
    public void doGet(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        //  //System.out.println("GET METHOD");
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    
}

