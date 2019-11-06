/*
 * @(#)SponsorMaintenanceServlet.java 1.0 8/16/02 3:28 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-APR-2011
 * by Bharati
 */

package edu.mit.coeus.servlet;

import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.sponsormaint.bean.SponsorFormsBean;
import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorHierarchyBean;
//import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.bean.UserDetailsBean; 
import edu.mit.coeus.sponsormaint.bean.SponsorContactBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.ParameterUtils;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import java.util.HashMap;

/**
 * <code>SponsorMaintenanceServlet</code> is a servlet which receives 
 * requesterBean (the informatin from the Applet regarding the Component which 
 * to be displayed in MDI form). 
 * This servlet delegates the process to the FormFactory to get the required 
 * component and set the Responder bean with the related information and 
 * send the info as a serialized object to the Applet.
 * It connects the DBEngine and executes the stored procedures or queries via
 * 'SponsorMaintenanceDataTxnBean'.
 *
 * @version 1.0 August 16,2002 3:28 PM
 * @author Mukundan C.
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling.
 *
 */

public class SponsorMaintenanceServlet extends CoeusBaseServlet {
    
    
    private final String IS_MAINTAIN_SPONSOR = "MAINTAIN_SPONSOR";
    private final String IS_MODIFY_SPONSOR = "MODIFY_SPONSOR";
    private final String IS_DELETE_SPONSOR = "DELETE_SPONSOR";
    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
    private static final char GET_HIERAR_GROUP_MAX_PACKAGE_NUMBER ='F';
    private static final char GET_PACKAGE_MAX_PAGE_NUMBER ='f';
    private static final char GET_HIERAR_FORM_DATA = 'k';
    private static final char HAS_FORM_LOAD_RIGHTS = 'l';
    private static final char DELETE_FORM_DATA = 'd';
    private static final char FORM_EXISTS_IN_GROUP = 'e';
    //Case#2445 - End
    
    
    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     *
     * @param request HttpServletRequest
     * @param responseHttpServletResponse
     * @exception ServletException,IOException.
     */
    public void doPost(HttpServletRequest request,HttpServletResponse response)
                                    throws ServletException, IOException {
        
        //HttpSession session;
        // the request object
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        UserInfoBean infoBean = new UserInfoBean();
        //Added on 14th May, 2004 - start
        //Logged In User Id
        String loggedinUser = null;
        SponsorMaintenanceDataTxnBean spTxnBean = null;
        //Added on 14th May, 2004 - end
        SponsorMaintenanceFormBean sponsorFormBean = 
                    new SponsorMaintenanceFormBean();
        RolodexDetailsBean rolDetailBean = new RolodexDetailsBean();
        //session = request.getSession(true);
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
                
        try {
            /* get an input stream */
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            /* read the serialized request object from applet */
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            //Added on 14th May, 2004 - start
            //Logged In User Id
            loggedinUser = requester.getUserName();
            spTxnBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
            //Added on 14th May, 2004 - end            
            /* get the fucntion type from the client side */            
            char functionType = requester.getFunctionType();
            Vector vecResBeans = new Vector();
            Vector vecReqBeans = new Vector();
            Vector responseData = new Vector();
            boolean spSuccess = true;
            String userName ="";
            String msgReply = "";
            String loggedinUserUnitNumber = "";
            int maintain_flag = 0;
            int delete_flag = 0;
            /* To get the data from the database */
            switch(functionType){
//*** Alphabets not used -   W
                case('D'):
                    /* pass the id get the data for that id using 
                     * getSponsorRolodexDetails method 
                     */
                    vecResBeans = spTxnBean.getSponsorRolodexDetails(
                            requester.getDataObject().toString());
                    /* pass the bean to the gui using responder bean */
                    responder.setDataObjects(vecResBeans);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                    /* To check for sponsor code duplications */
                case('R'):
                    /* if the count is true the sponsor code is already there 
                     * the user trying to duplicate 
                     */
                    boolean count = spTxnBean.getSponsorCount(
                                    requester.getDataObject().toString());
                    if (!count)
                        responder.setResponseStatus(false);
                    else
                        responder.setResponseStatus(true);
                    break;
                    /* The data is sent for updation in osp$sponsor and 
                     * osp$rolodex table 
                     */
                case('U'):
                    /* get the data from the applet */
                    vecReqBeans = requester.getDataObjects();
                    sponsorFormBean = 
                            (SponsorMaintenanceFormBean)vecReqBeans.elementAt(0);
                    rolDetailBean = (RolodexDetailsBean)vecReqBeans.elementAt(1);
                    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                    //Getting status of rolodex from database
                    Vector vecDBData =spTxnBean.getSponsorRolodexDetails(sponsorFormBean.getSponsorCode());
                    RolodexDetailsBean dbRoldexBean =(RolodexDetailsBean)vecDBData.get(1);
                    if(rolDetailBean.getRolodexId().equals(dbRoldexBean.getRolodexId())){
                        rolDetailBean.setStatus(dbRoldexBean.getStatus());
                    }
                    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                    
                    /* pass the data to the Txn bean for update */
                    // Commented by Shivakumar for locking enhancement
//                    spSuccess = spTxnBean.addUpdateSponsorMaintenance(
//                                sponsorFormBean,rolDetailBean,'U');
                    // Code added by Shivakumar - BEGIN 1                    
//                    spSuccess = spTxnBean.addUpdateSponsorMaintenance(
//                                sponsorFormBean,rolDetailBean,loggedinUser,'U'); 
                    // Code added by Shivakumar for locking enhancement - BEGIN
                    LockingBean lockingBean = new LockingBean();
                    if(sponsorFormBean.getAcType() != null && sponsorFormBean.getAcType().equals("I")){
                        lockingBean = spTxnBean.addUpdateSponsorMaintenance(
                                sponsorFormBean,rolDetailBean,loggedinUser,'U'); 
                    }else{
                        boolean lockCheck = spTxnBean.lockCheck(sponsorFormBean.getSponsorCode(), loggedinUser);
                        if(!lockCheck){
                            lockingBean = spTxnBean.addUpdateSponsorMaintenance(
                                sponsorFormBean,rolDetailBean,loggedinUser,'U'); 
                        }else{
                            //String msg = "Sorry,  the lock for sponsorCode "+sponsorFormBean.getSponsorCode()+"  has been deleted by DB Administrator ";
                            CoeusMessageResourcesBean coeusMessageResourcesBean
                                                =new CoeusMessageResourcesBean();
                            String msg = coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1011")+" "+sponsorFormBean.getSponsorCode()+" "+coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1002");                          
                            throw new LockingException(msg);
                        }    
                    }    
                    // Code added by Shivakumar for locking enhancement - END
                    // Code commented by Shivakumar for bug fixing in locking while releasing lock
//                    LockingBean lockingBean = spTxnBean.addUpdateSponsorMaintenance(
//                                sponsorFormBean,rolDetailBean,loggedinUser,'U'); 
                    spSuccess = lockingBean.isGotLock();
                    responder.setLockingBean(lockingBean);
                    // Code added by Shivakumar - END 1
                    if (!spSuccess){
                        
                        responder.setResponseStatus(false);
                        responder.setMessage("The Sponsor Maintenance "+
                                            "Data has not been updated");
                    }else{
                        try{
                            vecResBeans = spTxnBean.getSponsorRolodexDetails(
                                            sponsorFormBean.getSponsorCode());
                            spTxnBean.transactionCommit();
                        }catch(DBException dbEx){
                            dbEx.printStackTrace();
                            spTxnBean.transactionRollback();
                            throw dbEx;
                        }finally{
                            spTxnBean.endConnection();
                        }    
                        responder.setDataObjects(vecResBeans);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }
                    break;
                    /* To load the combo box(Sponsor types,states,countries) 
                     *on load of the form 
                     */
                case('G'):
                    /* get sponsor code auto generate config */
                    boolean generateSponsorCode = spTxnBean.getGenerateSponsorParamValue();

                    /* get the sponsor types from the database */
                    Vector vecTypes =  spTxnBean.getSponsorTypes();
                    /* get the states for USA from the database */
                    //Modified for Case#4248 -  State Provinces changes  - Start
                    //Instead of get all the states, hmStateWithCountry will holds country and it's states
//                    Vector vecStates = spTxnBean.getStates();
                    HashMap hmStateWithCountry = spTxnBean.fetchStatesWithCountry();
                    //Case#4248 - End
                    /* get the all approved countries from the database */
                    Vector vecCountries =  spTxnBean.getCountries();
                    /* the sponsor types,states and countries is collected 
                     * inside in the Vector passed to client 
                     */
                    Vector allComboObjects = new Vector();
                    allComboObjects.add(vecTypes);
                    //Modified for Case#4248 -  State Provinces changes  - Start
//                    allComboObjects.add(vecStates);
                    allComboObjects.add(hmStateWithCountry);
                    //Case#4248 - End
                    allComboObjects.add(vecCountries);

                    /* return sponsor generate flag */
                    if(generateSponsorCode){
                        allComboObjects.add("TRUE");
                    }else{
                        allComboObjects.add("FALSE");
                    }

                    /* set all the object in the responder bean */
                    responder.setDataObjects(allComboObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                    /* The data is sent to osp$sponsor ans osp$rolodex table for Add */
                case('I'):
                    /* get the data from the applet */
                    vecReqBeans = requester.getDataObjects();
                    /* get first element from the vector that is sponsor bean */
                    sponsorFormBean = 
                            (SponsorMaintenanceFormBean)vecReqBeans.elementAt(0);
                    /* get second element from the vector that is rolodex bean */
                    rolDetailBean = (RolodexDetailsBean)vecReqBeans.elementAt(1);
                    String rldID=spTxnBean.getNextSeqNum();
                    /* get the unit number from the session */
                    //infoBean = (UserInfoBean)session.getAttribute("USERINFO");
                    infoBean = (UserInfoBean)new 
                        UserDetailsBean().getUserInfo(requester.getUserName());
                    /* get Unit number from the session */
                    String unitNumber = infoBean.getUnitNumber();
                    sponsorFormBean.setOwnedBy(unitNumber);
                    rolDetailBean.setOwnedByUnit(unitNumber);
                    rolDetailBean.setRolodexId(rldID);

                    /* if sponsor code is null - accepted form validation indicates that
                       sponsor code should be generated based on parameter config */
                    if((sponsorFormBean.getSponsorCode() == null) || (sponsorFormBean.getSponsorCode().trim().length() ==0)){
                        String sponsorCode = spTxnBean.generateSponsorCode();
                        sponsorFormBean.setSponsorCode(sponsorCode);
                    }    
                    
                    /* pass the data to the Txn bean for validation */
//                    spSuccess = spTxnBean.addUpdateSponsorMaintenance(
//                                sponsorFormBean,rolDetailBean);
                    
                    // Code added by Shivakumar - BEGIN 1
//                    spSuccess = spTxnBean.addUpdateSponsorMaintenance(
//                                sponsorFormBean,rolDetailBean,loggedinUser,'U'); 
                    LockingBean relLockingBean = spTxnBean.addUpdateSponsorMaintenance(
                                sponsorFormBean,rolDetailBean,loggedinUser,'U'); 
                    spSuccess = relLockingBean.isGotLock();
                    // Code added by Shivakumar - END 1
                    if (!spSuccess){
                        responder.setLockingBean(relLockingBean);
                        responder.setResponseStatus(false);
                        responder.setMessage("The Sponsor Maintenance "+
                                            "Data has not been Inserted");
                    }else{
                        /* if successfully updated or added get sponsor data 
                         *back from the database 
                         */
                        vecResBeans = spTxnBean.getSponsorRolodexDetails(
                                        sponsorFormBean.getSponsorCode());

                        /* testing */
                        //System.out.println("*** return after insert *** ");
                        //SponsorMaintenanceFormBean sss = (SponsorMaintenanceFormBean)vecResBeans.elementAt(0);
                        //System.out.println("sponsor bean *** " + sss.getSponsorCode());
                        //System.out.println("*** return after insert *** ");
                        
                        /* set the bean to the responder bean */
                        responder.setDataObjects(vecResBeans);
                        responder.setResponseStatus(true);
                        responder.setMessage(null);
                    }
                    break;
                    /* to check whether user has right to modify or not */
                case('M'):
                    /* declare these variables for validation */
                    
                    boolean allowModify = false;
                    int modify_flag = 0;
                    sponsorFormBean = spTxnBean.getSponsorMaintenanceDetails(
                                        requester.getDataObject().toString());
                    /* the userid and unit number taken from the session */
                    //infoBean = (UserInfoBean)session.getAttribute("USERINFO");
                    infoBean = (UserInfoBean)new 
                        UserDetailsBean().getUserInfo(requester.getUserName());
                    /* get user Id from the session */
                    userName = infoBean.getUserId();
                    loggedinUserUnitNumber = infoBean.getUnitNumber();
                    /* the userid and unit number is passed to method 
                     * isUserHasRight to check the user has right 
                     */
                    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
                    //Sponsor Maintainer Role (modify Sponsor right) should have the ability to modify the
                    //inactive/ active flag for sponsors owned by the unit where the role is granted.
                    //if ( spTxnBean.isUserHasRight(userName,
                    //       sponsorFormBean.getOwnedBy(),IS_MAINTAIN_SPONSOR)){
                    if ( spTxnBean.isUserHasRight(userName,
                            sponsorFormBean.getOwnedBy(),IS_MAINTAIN_SPONSOR) || spTxnBean.isUserHasRight(userName,
                            sponsorFormBean.getOwnedBy(),IS_MODIFY_SPONSOR)){
                        /* user has right but needs to check other tables */
                        maintain_flag = 1;
                        modify_flag = 1;
                        //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
                    }else if ( spTxnBean.isUserHasRight(userName,
                            loggedinUserUnitNumber,IS_MODIFY_SPONSOR) &&
                            ((sponsorFormBean.getOwnedBy()).equalsIgnoreCase(
                            loggedinUserUnitNumber)) &&
                            ((sponsorFormBean.getCreateUser()).equalsIgnoreCase(
                            userName)) )   {
                        /* user has right but needs to check other tables */
                        modify_flag = 1;
                    }
                    /* if modify_flag is zero the user doesn't have right to modify */
                    if  (modify_flag  == 0 )  {
                        msgReply = "You do not have right to modify this "+
                                    "sponsor entry '" + 
                                    sponsorFormBean.getSponsorCode() +
                                    ".\n Do you want to display it?";
                    }else {
                        /* to check the sponsor is used by other user before modify */
                        Vector result = spTxnBean.checkSponsorIsUsed(
                                    sponsorFormBean.getSponsorCode(),userName,
                                    maintain_flag ,delete_flag);
                        String temp ="";
                        if ( result.get(2).toString().trim().equals("0") ) {
                            if (Integer.parseInt(result.get(0).toString()) > 0){
                                temp = "\n proposal table";
                            }
                            if (Integer.parseInt(result.get(1).toString()) >0){
                                if (temp.equals(""))
                                    temp = "\n rolodex table";
                                else
                                    temp = temp+"\n nrolodex table";
                            }
                            /* user cannot modify the data is used in another table */
                            msgReply = "You can not modify this sponsor '" +
                                        sponsorFormBean.getSponsorCode() +
                                        "'. It has been used in " + temp + 
                                        ". Do you want to display it?";
                        }else {
                            allowModify = true;
                        }
                    }
                    /* the boolean will return true if the user has right 
                     * to modify else false 
                     */
                    if (allowModify) {
                          //sponsorFormBean = spTxnBean.getSponsorRolodexDetails(
                            //            requester.getDataObject().toString(), true );
                       Vector resBeans = new Vector();  
                       // Commented by Shivakumar for locking enhancement
//                       resBeans = spTxnBean.getSponsorRolodexDetails(  
//                                   requester.getDataObject().toString(),'M');
                       // Code added by Shivakumar -BEGIN
                       LockingBean releaseLockingBean = spTxnBean.getSponsorRolodexDetails(  
                                   requester.getDataObject().toString(),'M',loggedinUser,loggedinUserUnitNumber);
                       boolean success = releaseLockingBean.isGotLock();
                       //responder.setLockingBean(releaseLockingBean);
                       if(success){
                           try{
                                resBeans = spTxnBean.getSponsorRolodexDetails(  
                                           requester.getDataObject().toString(),'M');
                                spTxnBean.transactionCommit();
                                responder.setLockingBean(releaseLockingBean);
                           }catch(DBException dbEx){
                               dbEx.printStackTrace();
                               spTxnBean.transactionRollback();
                               throw dbEx;
                           }finally{
                               spTxnBean.endConnection();
                           }    
                       }    
                       // Code added by Shivakumar -END
                       sponsorFormBean = (SponsorMaintenanceFormBean)resBeans.get(0);
                       rolDetailBean = (RolodexDetailsBean)resBeans.get(1);
                    }
                    responder.setResponseStatus(true);
                    responseData.add(new Boolean(allowModify));
                    /* the message sent to the client */
                    responseData.add(msgReply);
                    responseData.add(sponsorFormBean);
                    responseData.add(rolDetailBean);
                    responder.setDataObjects(responseData);
                    break;
                    /* to check whether user has right to delete or not */
                case('C'):
                    boolean allowDelete = false;
                    sponsorFormBean = spTxnBean.getSponsorMaintenanceDetails(
                                        requester.getDataObject().toString());
                    
                    //To check whether the record has been already locked or not
                    spTxnBean.canDeleteSponsor(requester.getDataObject().toString());

                    //infoBean = (UserInfoBean)session.getAttribute("USERINFO");
                    infoBean = (UserInfoBean)new 
                        UserDetailsBean().getUserInfo(requester.getUserName());
                    /* get user Id from the session */
                    userName = infoBean.getUserId().toUpperCase();
                    loggedinUserUnitNumber = infoBean.getUnitNumber();
                    /* the userid and unit number is passed to method 
                     * isUserHasRight to check the user has right to delete 
                     */
                    if ( spTxnBean.isUserHasRight(userName,
                            loggedinUserUnitNumber,IS_DELETE_SPONSOR) &&
                            (loggedinUserUnitNumber.equalsIgnoreCase(
                            sponsorFormBean.getOwnedBy())) &&
                            (userName.equalsIgnoreCase(
                            sponsorFormBean.getCreateUser())) )   {
                        /* user has right but needs to check other tables */
                        delete_flag = 1;
                    }else if ( spTxnBean.isUserHasRight(userName,
                               sponsorFormBean.getOwnedBy(),IS_MAINTAIN_SPONSOR)){
                        /* user has right but needs to check other tables */
                        delete_flag = 1;
                    }
                    /* user doesn't have right to delete */
                    if  (delete_flag  == 0 )  {
                        msgReply = "You do not have right to delete this sponsor'"+
                                    sponsorFormBean.getSponsorCode();
                    }else {
                    /* for delete:  li_maintain_flag = 0 means do not check 
                     * column create_status_code and owned_by_user of 
                     * osp$proposal in stored procedure check_sponsor_is_used()
                     */
                        Vector result = spTxnBean.checkSponsorIsUsed(
                                        sponsorFormBean.getSponsorCode(),
                                        userName,maintain_flag,delete_flag);
                        String temp ="";
                        if ( result.get(2).toString().trim().equals("0") ) {
                            if (Integer.parseInt(result.get(0).toString()) > 0){
                                temp = "\n proposal table";
                            }
                            if (Integer.parseInt(result.get(1).toString()) >0){
                                if (temp.equals(""))
                                    temp = "\n rolodex table";
                                else
                                    temp = temp+"\n rolodex table";
                            }
                            if (temp.equals(""))
                                msgReply = "You can not delete this sponsor'" + 
                                            sponsorFormBean.getSponsorCode();
                            else
                                msgReply = "You can not delete this sponsor'" +
                                            sponsorFormBean.getSponsorCode() +
                                            "'. It has been used in " + temp ;
                        }else {
                            msgReply = "Do you want to delete  " + 
                                        sponsorFormBean.getSponsorCode() +"  ?";
                            allowDelete = true;
                        }
                    }
                    responseData.add(new Boolean(allowDelete));
                    responseData.add(msgReply);
                    responder.setMessage("Delete privilege checked successfuly");
                    responder.setDataObjects(responseData);
                    responder.setResponseStatus(true);
                    break;
                    /* once the check is over and the user has right to 
                     * delete then the data is delete 
                     */
                    //Modified by Jinu to incorporate Sponsor Contacts 02-02-2005
                case('E'):  
                    if(spTxnBean.deleteSponsorContacts(requester.getDataObject().toString())){
                        if ( spTxnBean.deleteSponsorMaintenance(
                                requester.getDataObject().toString(), false ) ) {
                            responder.setDataObject("Sponsor Info Deleted successfully");
                        }else  {
                            responder.setDataObject("Sponsor not found !Deletion failed");
                        }
                    }else  {
                        responder.setDataObject("Sponsor not found !Deletion failed");
                    }
                    break;
                    //end Jinu.
                    //Updated for Row Locking. Subramanya    
                case('Z') :                    
                /*String refId = (requester.getDataObject() 
                    == null?null:requester.getDataObject().toString().trim()); 
                releaseLock(refId);
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
                 */
                    
                    String sponsorCode = requester.getDataObject().toString().trim();
                    // Commented by Shivakumar for locking enhancement
//                    spTxnBean.releaseEdit(sponsorCode);
                    // Code added by Shivakumar -BEGIN 1
                    loggedinUser = requester.getUserName();
//                    spTxnBean.releaseEdit(sponsorCode,loggedinUser);
                    // Calling releaseLock method for fixing bug in locking
                    LockingBean releaseLockingBean = spTxnBean.releaseLock(sponsorCode,loggedinUser);
                    responder.setLockingBean(releaseLockingBean);
                    // Code added by Shivakumar -END 1
                    responder.setResponseStatus(true);
                    responder.setDataObject("updateLock connection released");
                    break;
                case('Y') :
                    Vector vctSponsorRights = requester.getDataObjects();
                    String maintainSponsorForms = (String)vctSponsorRights.elementAt(0);
                    String addSponsor = (String)vctSponsorRights.elementAt(1);
                    String maintainSponsor = (String)vctSponsorRights.elementAt(2);
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    infoBean = (UserInfoBean)new 
                        UserDetailsBean().getUserInfo(requester.getUserName());
                    boolean hasSponsorFormsRight = userMaintDataTxnBean.getUserHasOSPRight(requester.getUserName(), maintainSponsorForms);
                    responseData.addElement(new Boolean(hasSponsorFormsRight));
                    boolean hasAddRight = userMaintDataTxnBean.getUserHasRight(requester.getUserName(), addSponsor, infoBean.getUnitNumber());
                    boolean hasMaintainSponsorRight = userMaintDataTxnBean.getUserHasRight(requester.getUserName(), maintainSponsor, infoBean.getUnitNumber());
                    //Check if User has both rights
                    if(!hasAddRight && !hasMaintainSponsorRight){
                        responseData.addElement(new Boolean(false));
                    }else{
                        responseData.addElement(new Boolean(true));
                    }
                    responder.setDataObjects(responseData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;                    
                    // Code added by Shivakumar - BEGIN
                case('K') :   
                    String sponsorDataCode = (String)requester.getDataObject();
                    Hashtable hshSponsorData  = new Hashtable();
                    SponsorMaintenanceDataTxnBean  spTxnDataBean = new SponsorMaintenanceDataTxnBean();
                    CoeusVector cvPageData = spTxnDataBean.getSponsorForms(sponsorDataCode);         
                    hshSponsorData.put(KeyConstants.PACKAGE_DATA, cvPageData);
                    CoeusVector  cvPackageData = spTxnDataBean.getSponsorFormTemplates(sponsorDataCode);
                    hshSponsorData.put(KeyConstants.PAGE_DATA, cvPackageData);
                    responder.setDataObject(hshSponsorData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;       
                case('J') :    
                    SponsorFormsBean sponsorFormsBean = (SponsorFormsBean)requester.getDataObject();
                    spTxnDataBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    boolean success = spTxnDataBean.updateSponsorForm(sponsorFormsBean);
                    responder.setDataObject(new Boolean(success));
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                case('S') :                                        
                    SponsorTemplateBean sponTemplBean = (SponsorTemplateBean)requester.getDataObject();
                    Hashtable htSponsorData  = new Hashtable();
                    spTxnDataBean = new SponsorMaintenanceDataTxnBean(loggedinUser);                    
                    SponsorTemplateBean sponsorUpdateTemplateBean = spTxnDataBean.updateSponsorFormTemplates(sponTemplBean);
                    responder.setDataObject(sponsorUpdateTemplateBean);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);                                        
                    break;
                case('V') :    
                    Hashtable htSponsorBulkData = (Hashtable)requester.getDataObject();                    
                    spTxnDataBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    Hashtable htUpdSponsorBulkData = spTxnDataBean.updateSponsorDataBulk(htSponsorBulkData);
                    responder.setDataObject(htUpdSponsorBulkData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                    
                 case('X') :                    
                    SponsorTemplateBean clobSponsorPageData = (SponsorTemplateBean)requester.getDataObject();                    
                    String clbSponsorCode = clobSponsorPageData.getSponsorCode();
                    int  clbPackageNumber = clobSponsorPageData.getPackageNumber();
                    int  clbPageNumber = clobSponsorPageData.getPageNumber();
                    spTxnDataBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    SponsorTemplateBean clobPageData = spTxnDataBean.getPageCLOBData(clbSponsorCode, clbPackageNumber, clbPageNumber);
                    responder.setDataObject(clobPageData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                    // Added by Shivakumar - END
                    /* Code added by Shivakumar for implementing
                     * sponsor hierarchy
                     */
                    // Functionality for getting the sponsor hierarchy list
                case('L'):                         
                    UserMaintDataTxnBean userMaintTxnBean = new UserMaintDataTxnBean();
                    SponsorMaintenanceDataTxnBean sponTxnBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    boolean status = false;
                    CoeusVector cvSponsorHierarchyList = null;
                    
                    CoeusVector cvSponsorHierarchy = null;
                    CoeusVector cvSponNotInHierarchy = null;
                    CoeusVector cvSponsorHierarchyData = new CoeusVector();
                    boolean hasSponsorMaintRight = userMaintTxnBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_SPONSOR_HIERARCHY");
                    
//                        // Getting sponsor hierarchy list
                        cvSponsorHierarchyList = sponTxnBean.getSponsorHierarchyList();                        
                        cvSponsorHierarchyData.add(cvSponsorHierarchyList);                            
                        cvSponsorHierarchyData.add(new Boolean(hasSponsorMaintRight));
                        status = true;
                    
                    responder.setDataObject(cvSponsorHierarchyData);                    
                    responder.setResponseStatus(status);
                    responder.setMessage(null);
                    break; 
                    // Functionality for deleting sponsor hierarchy 
                case('N'):
                    String hierarchyName = (String)requester.getDataObject();
                    SponsorMaintenanceDataTxnBean sponTnBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start
                    //deletes all sponsor forms and templates related to the printing hierarchy
                    String sponsorPrintHierarName = ParameterUtils.getPropPrintHierarchyName();
                    if(sponsorPrintHierarName.equals(hierarchyName)){
                       sponTnBean.deleteFormsAndTempInHierarchy();
                    }
                    //Case#2445 - End
                    boolean deleteStatus = sponTnBean.deleteSponsorHierarchy(hierarchyName);
                    responder.setResponseStatus(deleteStatus);
                    responder.setMessage(null);
                    break;
                    // Functionality for copying sponsor hierarchy 
                case('P'):
                    CoeusVector cvSponsorData = (CoeusVector)requester.getDataObject();
                    String sourceHierarchy = (String)cvSponsorData.get(0);
                    String targetHierarchy = (String)cvSponsorData.get(1);
                    spTxnDataBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    boolean copyStatus = spTxnBean.copySponsorHierarchy(sourceHierarchy, targetHierarchy, loggedinUser);
                    responder.setResponseStatus(copyStatus);
                    responder.setMessage(null);
                    break;
                    // Function for getting sponsor not in hierarchy 
                case('Q'):
                    String sponsorName = (String)requester.getDataObject();
                    spTxnBean  = new SponsorMaintenanceDataTxnBean();
                    CoeusVector cvSpon = spTxnBean.getSponsorNotInHierarchy(sponsorName);
                    responder.setDataObject(cvSpon);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                    // Function for getting all hierarchy
                case('O'):
                    Hashtable htSpData = new Hashtable();
                    String hierName = (String)requester.getDataObject();
                    spTxnBean  = new SponsorMaintenanceDataTxnBean();
                    CoeusVector cvSpHier = spTxnBean.getSponsorHierarchy(hierName);
                    CoeusVector cvSpNoInHier = spTxnBean.getSponsorNotInHierarchy(hierName);
                    if(cvSpHier != null && cvSpHier.size() > 0){
                        htSpData.put(SponsorHierarchyBean.class, cvSpHier);                        
                    }    
                    if(cvSpNoInHier != null && cvSpNoInHier.size() > 0){
                        htSpData.put(SponsorMaintenanceFormBean.class, cvSpNoInHier);
                    }    
                    
                    responder.setDataObject(htSpData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                 // Function for updating sponsor hierarchy
                case('A'):
                    CoeusVector cvSponData = (CoeusVector)requester.getDataObject();
                    spTxnBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    String hirName = "";
                    boolean updationStatus = false;
                    if(cvSponData != null && cvSponData.size() > 0){
                        for(int vecCount = 0;vecCount < cvSponData.size(); vecCount++){
                            SponsorHierarchyBean sponHierBean = (SponsorHierarchyBean)cvSponData.elementAt(vecCount);
                            if(sponHierBean.getAcType() == null){
                                continue;
                            }    
                            hirName = sponHierBean.getHierarchyName();
                            updationStatus = spTxnBean.updateSponsorHierarchy(sponHierBean);                            
                        }    
                        CoeusVector cvSponHierarchy = spTxnBean.getSponsorHierarchy(hirName);
                        responder.setDataObjects(cvSponHierarchy);
                        responder.setResponseStatus(updationStatus);
                        responder.setMessage(null);                        
                    }    
                    break;
                    // Code added by Shivakumar for sponsor hierarchy -- End
                    
                case('T'):// Added by chandra - Checking for existing templates - 25th Aug
                    CoeusVector cvData = (CoeusVector)requester.getDataObject();
                    String sponsor = (String)cvData.get(0);
                    int packageNumber = ((Integer)cvData.get(1)).intValue();
                    int pageNumber = ((Integer)cvData.get(2)).intValue();
                    spTxnDataBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    int value = spTxnDataBean.checkSponsorFormPageExists(sponsor,packageNumber,pageNumber);
                    responder.setDataObject(new Integer(value));
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                    // End Chandra 25th Aug.
//Added by Jinu - For implementing Sponsor Contacts - 28th Jan 2005.
                case('B')://Updating Sponsor Contacts
                    HashMap hmSponsorContactData = (HashMap)requester.getDataObject();
                    if(hmSponsorContactData != null){
                        CoeusVector cvSponsorContactUpdateData = (CoeusVector)hmSponsorContactData.get("UPDATE_DATA");
                        CoeusVector cvSponsorContactDeleteData = (CoeusVector)hmSponsorContactData.get("DELETE_DATA");
                        SponsorMaintenanceDataTxnBean txnBean = new SponsorMaintenanceDataTxnBean(loggedinUser);
                        if(cvSponsorContactDeleteData != null && cvSponsorContactDeleteData.size()>0){
                            for(int count2=0;count2<cvSponsorContactDeleteData.size();count2++){
                                SponsorContactBean sponsorContactBean = (SponsorContactBean)cvSponsorContactDeleteData.get(count2);
                                if(sponsorContactBean != null){
                                    if(sponsorContactBean.getAcType()==null || sponsorContactBean.getAcType().equals("")){
                                        continue;
                                    }else{
                                        txnBean.addUpdDeleteSponsorContact(sponsorContactBean);
                                    }
                                }
                            }
                        }
                        if(cvSponsorContactUpdateData != null && cvSponsorContactUpdateData.size()>0){
                            for(int count2=0;count2<cvSponsorContactUpdateData.size();count2++){
                                SponsorContactBean sponsorContactBean = (SponsorContactBean)cvSponsorContactUpdateData.get(count2);
                                if(sponsorContactBean != null){
                                    if(sponsorContactBean.getAcType()==null || sponsorContactBean.getAcType().equals("")){
                                        continue;
                                    }else{
                                        txnBean.addUpdDeleteSponsorContact(sponsorContactBean);
                                    }
                                }
                            }
                        }
                    }
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
                case('H')://Getting Sponsor Contacts
                    HashMap hmSponsorContacts = new HashMap();
                    String strSponsorCode = (String)requester.getDataObject();
                    SponsorMaintenanceDataTxnBean txnBean2 = new SponsorMaintenanceDataTxnBean(loggedinUser);
                    CoeusVector cvData2 = null;
                    if(strSponsorCode != null && !strSponsorCode.trim().equals("")){
                       cvData2 = txnBean2.getSponsorContacts(strSponsorCode);
                    }
                    Vector sponsorType = txnBean2.getSponsorContactTypes();
                    CoeusVector cvSponsorType = new CoeusVector();
                    cvSponsorType.addAll(sponsorType);
                    
                    hmSponsorContacts.put(SponsorContactBean.class, cvData2);
                    hmSponsorContacts.put(ComboBoxBean.class, cvSponsorType);
                    responder.setDataObject(hmSponsorContacts);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
// End Jinu 
            //Case#2445 - proposal development print forms linked to indiv sponsor, should link to sponsor hierarchy -Start        
            //fetch and send all sponsor form and it's template related to printing hierarchy to client
            case(GET_HIERAR_FORM_DATA) :   
                    sponsorFormsBean = (SponsorFormsBean)requester.getDataObject();
                    String hierarSponsorCode = sponsorFormsBean.getSponsorCode();
                    String groupName = sponsorFormsBean.getGroupName();
                    Hashtable hshHierarSponsorData  = new Hashtable();
                    CoeusVector cvHierarPackData = spTxnBean.getHierarchySponsorForms(hierarSponsorCode,groupName);         
                    hshHierarSponsorData.put(KeyConstants.PACKAGE_DATA, cvHierarPackData);
                    CoeusVector  cvHierarPageData = spTxnBean.getSponsorFormTemplates(cvHierarPackData);
                    hshHierarSponsorData.put(KeyConstants.PAGE_DATA, cvHierarPageData);
                    responder.setDataObject(hshHierarSponsorData);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;       
             
            case (GET_HIERAR_GROUP_MAX_PACKAGE_NUMBER):
                    sponsorCode = (String)requester.getDataObject();
                    Integer maxHierPackNumber = new Integer(spTxnBean.getSponsorFormMaxPackNumber(sponsorCode));
                    responder.setDataObject(maxHierPackNumber);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                    break;
            case (GET_PACKAGE_MAX_PAGE_NUMBER):
                   SponsorFormsBean formsBean = (SponsorFormsBean)requester.getDataObject();
                   Integer maxPackPageNumber = new Integer(spTxnBean.getPackageMaxPageNumber(formsBean.getSponsorCode(),formsBean.getPackageNumber()));
                   responder.setDataObject(maxPackPageNumber);
                   responder.setResponseStatus(true);
                   responder.setMessage(null);
                   break;
             //checks for user has MAINTAIN_SPONSOR_FORMS rights and hierarchy name is same as value in parameter, 
             //then allows the user to upload the forms and templates
             case (HAS_FORM_LOAD_RIGHTS):
                   String hierarchyNam = (String)requester.getDataObject();
                   String printHierarchyName = ParameterUtils.getPropPrintHierarchyName();
                   if(printHierarchyName.equals(hierarchyNam)){
                       UserMaintDataTxnBean userMaintBean = new UserMaintDataTxnBean();
                       boolean hasMaintainFormsRight = userMaintBean.getUserHasOSPRight(loggedinUser, "MAINTAIN_SPONSOR_FORMS");
                       responder.setDataObject(new Boolean(hasMaintainFormsRight));
                       responder.setResponseStatus(true);
                       responder.setMessage(null);
                   }else{
                       responder.setDataObject(new Boolean(false));
                       responder.setResponseStatus(true);
                       responder.setMessage(null);
                   }
                   break;
             case (DELETE_FORM_DATA):
                  String hierarGroupName = (String)requester.getDataObject();
                  spTxnBean.deleteSponsorFormTempInHierarchy(hierarGroupName);
                  spTxnBean.deleteSponsorFormInHierarchy(hierarGroupName);
                  responder.setResponseStatus(true);
                  responder.setMessage(null);
                  break;               
             case (FORM_EXISTS_IN_GROUP):
                 String level1GroupName = (String)requester.getDataObject();
                 boolean isFormExists = spTxnBean.isFormExistsForGroup(level1GroupName);
                 responder.setDataObject(new Boolean(isFormExists));
                 responder.setResponseStatus(true);
                 responder.setMessage(null);
            }// end of Switch case
            
        }catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setResponseStatus(false);            
                responder.setMessage(errMsg);               
        }catch( CoeusException coeusEx ) {
            
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                            new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "SponsorMaintenanceServlet",
                                                                "perform");
            
        } catch( DBException dbEx ) {
            
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }

            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean = 
                                                new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "SponsorMaintenanceServlet",
                                                                "perform");
            
        } catch(Exception e) {
            responder.setResponseStatus(false);
            /* print the error message at client side */
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "SponsorMaintenanceServlet",
                                                                "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "SponsorMaintenanceServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                /* send the object to applet */
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                outputToApplet.flush();
                /* close the streams */
                if (inputFromApplet!=null){
                    inputFromApplet.close();
                }
                if (outputToApplet!=null){
                    outputToApplet.close();
                }
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe, "SponsorMaintenanceServlet",
                                                                "perform");
            }
        }
    }
    
   /** Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    * @exception ServletException,IOException.
    */
    public void doGet(HttpServletRequest request,HttpServletResponse response)
                                    throws ServletException, IOException {
    }
    
}
