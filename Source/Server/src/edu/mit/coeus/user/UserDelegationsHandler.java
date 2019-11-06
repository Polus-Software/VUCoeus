/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * UserDelegationsHandler.java
 *
 * Created on August 4, 2003, 10:15 AM
 */

package edu.mit.coeus.user;

import edu.mit.coeus.utils.MailActions;
import java.util.*;

import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.UserDelegationsBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;


/** handles all requests from Delegation Controller
 * to get/set Delegation related information.
 * @author sharathk
 */
public class UserDelegationsHandler extends  UserMaintenanceRequestHandler{
    
    private Vector data;
    private Hashtable hashtable;
    private UserMaintDataTxnBean userMaintDataTxnBean;
    private String requestType;
    
    //Request Types  
    private static final String GET_DELEGATIONS = "GET_DELEGATIONS";
    private static final String GET_USER_NAME = "GET_USER_NAME";
    private static final String CAN_DELEGATE = "CAN_DELEGATE";
    private static final String CREATE_DELEGATION = "CREATE_DELEGATION";
    private static final String UPDATE_DELEGATION = "UPDATE_DELEGATION";
    private static final String UPDATE_DELEGATED_STATUS = "UPDATE_DELEGATED_STATUS";
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private static final String DELETE_DELEGATION = "DELETE_DELEGATION";
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Creates a new instance of UserDelegationsHandler */
    public UserDelegationsHandler() {
    }
    
    /** overidden method which will be called to perform
     * initializing operations.
     * @throws Exception if could not perform the operation.
     */
    public void doPerform() throws Exception {
        hashtable = (Hashtable)dataObjects.get(0);
        data = (Vector)hashtable.get(DataPositions.DATA);
        requestType = data.get(0).toString().trim();
        
        if(requestType.equals(GET_DELEGATIONS)) {
            getDelegations(data.get(1).toString());
        }
        else if(requestType.equals(GET_USER_NAME)) {
            getUserName(data.get(1).toString());
        }
        else if(requestType.equals(CAN_DELEGATE)) {
            //Delegated By, Delegated To, Effective Date
            canDelegate(data.get(1).toString(), data.get(2).toString(), (Date)data.get(3));
        }
        else if(requestType.equals(CREATE_DELEGATION)) {
            create((UserDelegationsBean)data.get(1));
        }
        else if(requestType.equals(UPDATE_DELEGATION)) {
            update((UserDelegationsBean)data.get(1));
        }
        else if(requestType.equals(UPDATE_DELEGATED_STATUS)) {
            updateDelegatedStatus((UserDelegationsBean)data.get(1));
        }
        //Added for Case#3682 - Enhancements related to Delegations - Start
        else if(requestType.equals(DELETE_DELEGATION)) {
            delete((UserDelegationsBean)data.get(1));
        }
        //Added for Case#3682 - Enhancements related to Delegations - End
    }
    
    public void fetchValues() {
    }
    
    public void setValues(Vector vector) {
    }
    
    /** creates a new UserMaintenenceTxnBean if not initialized.
     *
     */
    private void prepareUserMaintDataTxnBean() {
        if(userMaintDataTxnBean == null) {
            userMaintDataTxnBean = new UserMaintDataTxnBean();
        }
    }
    
    /** gets the Delegations for the Logged in User.
     * @param delegatedBy delegated by userId
     */
    public void getDelegations(String delegatedBy) {
        prepareUserMaintDataTxnBean();
        try{
            Vector delegations = userMaintDataTxnBean.getDelegations(delegatedBy);
            Vector delegatedTo = userMaintDataTxnBean.getDelegatedTo(delegatedBy);
            //Added for Case#3682 - Enhancements related to Delegations - Start
            delegations = delegations != null ? delegations : new Vector();
            delegatedTo = delegatedTo != null ? delegatedTo : new Vector();
            //Added for Case#3682 - Enhancements related to Delegations - End
            dataObjects.removeAllElements();
            dataObjects.add(delegations);
            dataObjects.add(delegatedTo);
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /** gets the Username for userId.
     * @param userId for which name has to be got.
     */
    public void getUserName(String userId) {
        prepareUserMaintDataTxnBean();
        try{
            UserInfoBean userInfoBean = userMaintDataTxnBean.getUser(userId);
            dataObjects.removeAllElements();
            dataObjects.add(userInfoBean);
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        System.out.println("Getting User Id on Server");
    }
    
    /** checks wheather Delegations can be Delegated to delegatedTo user
     * by delegetedBy user for the effectiveDate.
     * @param delegatedBy userId who has to delegate work.
     * @param delegatedTo userId for whom the work has been delegated.
     * @param effectiveDate from which the delegations take effect.
     */
    public void canDelegate(String delegatedBy, String delegatedTo, Date effectiveDate) {
        prepareUserMaintDataTxnBean();
        UserDelegationsBean userDelegationsBean = new UserDelegationsBean();
        userDelegationsBean.setDelegatedBy(delegatedBy);
        userDelegationsBean.setDelegatedTo(delegatedTo);
        userDelegationsBean.setEffectiveDate(new java.sql.Date(effectiveDate.getTime()));
        try{
            //method call here
            int canDelegate;
            canDelegate = userMaintDataTxnBean.getIsOkToDelegate(userDelegationsBean);
            dataObjects.removeAllElements();
            dataObjects.add(new Integer(canDelegate));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /** creates new Delegation.
     * @param userDelegationsBean from which new Delegation has to be created.
     */
    public void create(UserDelegationsBean userDelegationsBean) {
        userDelegationsBean.setAcType(INSERT_RECORD);
        userDelegationsBean.setAw_EffectiveDate(userDelegationsBean.getEffectiveDate());
        userDelegationsBean.setAw_Status(userDelegationsBean.getStatus());
        //Should always instantiate with the Logged in User Id - 18th March, 2004
        //UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userDelegationsBean.getDelegatedBy());
        UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userID);
        try{
            //Added for Case#3682 - Enhancements related to Delegations - Start
            int delegationId = userMaintUpdateTxnBean.getNextDelegationId();
            userDelegationsBean.setDelegationID(delegationId);
            userMaintUpdateTxnBean.addUpdDeleteDelegations(userDelegationsBean);
            //inbox and mail notification
            userMaintUpdateTxnBean.updateInboxTable(userDelegationsBean);
            prepareUserMaintDataTxnBean();
            String userName = userMaintDataTxnBean.getUserName(userDelegationsBean.getDelegatedBy());
            String message = userName+" has delegated his/her approval responsibilities in Coeus to you with effective date " +
                            userDelegationsBean.getEffectiveDate()+". Use Coeus premium application to accept or reject the delegation.";
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            String subject = "Delegation Request";
            userMaintUpdateTxnBean.sendMail(MailActions.DELEGATION_REQUEST,userDelegationsBean.getDelegatedTo(),message);
            //COEUSDEV-75:End
            //Added for Case#3682 - Enhancements related to Delegations - End            
            Vector data = userMaintDataTxnBean.getDelegations(userDelegationsBean.getDelegatedBy());
            UserDelegationsBean bean = (UserDelegationsBean)data.get(0);
            userDelegationsBean.setUpdateTimestamp(bean.getUpdateTimestamp());
            userMaintUpdateTxnBean.updateDelegatedStatus(userDelegationsBean);
            dataObjects.removeAllElements();
            dataObjects.add(new Boolean(true));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /** updates Delegation.
     * @param userDelegationsBean which has to be updated.
     */
    public void update(UserDelegationsBean userDelegationsBean) {
        userDelegationsBean.setAcType(UPDATE_RECORD);
        //Should always instantiate with the Logged in User Id - 18th March, 2004
        //UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userDelegationsBean.getDelegatedBy());
        UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userID);
        try{
            userMaintUpdateTxnBean.addUpdDeleteDelegations(userDelegationsBean);
            //Added for Case#3682 - Enhancements related to Delegations - Start
            userMaintUpdateTxnBean.updateInboxTable(userDelegationsBean);
            prepareUserMaintDataTxnBean();
            String userName = userMaintDataTxnBean.getUserName(userDelegationsBean.getDelegatedBy());
            String message = userName+" has removed the delegation request ";
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
//            String subject = "Delegation Removed";
            userMaintUpdateTxnBean.sendMail(MailActions.DELEGATION_REMOVED,userDelegationsBean.getDelegatedTo(),message);
            //COEUSDEV-75:End
            //Added for Case#3682 - Enhancements related to Delegations - End
            dataObjects.removeAllElements();
            dataObjects.add(new Boolean(true));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Delete Delegations of status' 'Cloed' and 'rejected'
     * @param userDelegationsBean has Delegation details to be deleted
     */
    public void delete(UserDelegationsBean userDelegationsBean){
      userDelegationsBean.setAcType(DELETE_RECORD);  
      UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userID);
      try{
            userMaintUpdateTxnBean.addUpdDeleteDelegations(userDelegationsBean);
            dataObjects.removeAllElements();
            dataObjects.add(new Boolean(true));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** updates the status of the Delegation.
     * @param userDelegationsBean whose status has to be updated.
     */
    public void updateDelegatedStatus(UserDelegationsBean userDelegationsBean) {
        userDelegationsBean.setAcType(UPDATE_RECORD);
        //Should always instantiate with the Logged in User Id - 18th March, 2004
        //UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userDelegationsBean.getDelegatedBy());
        UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userID);
        try{
            userMaintUpdateTxnBean.updateDelegatedStatus(userDelegationsBean);
            //Added for Case#3682 - Enhancements related to Delegations - Start
            //inbox and mail notification
            userMaintUpdateTxnBean.updateInboxTable(userDelegationsBean);
            prepareUserMaintDataTxnBean();
            String message = "";
//            String subject = "";
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            int actionCode = -1;
            String delegatedToUserName = userMaintDataTxnBean.getUserName(userDelegationsBean.getDelegatedTo());
            if(userDelegationsBean.getStatus() == 'R'){
//                subject = "Delegation Rejected";                
                message = delegatedToUserName+" has rejected the delegation request with effective date "
                            + userDelegationsBean.getEffectiveDate();
                actionCode = MailActions.DELEGATION_REJECTED;
            }else if(userDelegationsBean.getStatus() == 'P'){
//                subject = "Delegation Accepted";                
                message = delegatedToUserName+" has accepted the delegation request with effective date "
                            + userDelegationsBean.getEffectiveDate();
                actionCode = MailActions.DELEGATION_ACCEPTED;
            }            
            userMaintUpdateTxnBean.sendMail(actionCode,userDelegationsBean.getDelegatedBy(),message);
            //COEUSDEV-75:End
            //Added for Case#3682 - Enhancements related to Delegations - End
            dataObjects.removeAllElements();
            dataObjects.add(new Boolean(true));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
