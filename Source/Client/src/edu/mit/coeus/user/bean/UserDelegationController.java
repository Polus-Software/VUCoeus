/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * UserDelegationController.java
 *
 * Created on August 4, 2003, 9:59 AM
 */

package edu.mit.coeus.user.bean;

import edu.mit.coeus.exception.CoeusException;
import java.util.*;

import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;

/** Communicates with the Server to set/get Values of Delegations.
 * @author sharathk
 */
public class UserDelegationController {
    
    private  RequesterBean requester;
    private Hashtable hashTable;
    
    private static final String COMMAND = "UserDelegationsHandler";
    private static final String USER_MAINTENENCE_SERVLET = "/userMaintenanceServlet";
    
    private static final String BASE = "user_delegations_exceptionCode.";
    private static final String COULD_NOT_FETCH_DETAILS = BASE+"2533";
    private static final String VALIDATION_FAILED = "The validation check for the delegation failed";
    private static final String COULD_NOT_CREATE = "Could Not Create Delegation";
    
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
    
    /** Creates a new instance of UserDelegationController */
    public UserDelegationController() {
        hashTable = new Hashtable();
        hashTable.put(DataPositions.UNIT_NUMBER,CoeusGuiConstants.INSTITUTE_UNIT_NUMBER);
        hashTable.put(DataPositions.COMMAND,COMMAND);
        hashTable.put(DataPositions.MODULE,"user");
        hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.ADD_MODE));
        hashTable.put(DataPositions.PERSON_ID,"");
    }
    
    /**
     *  Method used to send the requester Bean to the servlet for database communication.
     * @param srvComponentName the Servlet to be used for communication to the database
     * @param requester a RequesterBean which consist of userId and servlet details.
     * @return ResponderBean
     */
    private ResponderBean sendToServer(String srvComponentName,RequesterBean requester) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL+srvComponentName;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
    }
    
    /** gets Delegations/Responsibilities Delegated for a Logged in User.
     * @return Delegations
     * @param userId logged in userId
     * @throws CoeusClientException if could not get Details.
     */
    public Object getDelegations(String userId) throws CoeusClientException{
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(GET_DELEGATIONS);
        vecUserDetails.add(userId);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            return response.getDataObjects();
        }
        else{
            throw new CoeusClientException(0, COULD_NOT_FETCH_DETAILS, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** returns UserName for UserId.
     * @param userId for which Name has to be returned.
     * @throws CoeusClientException if name could not be retrieved.
     * @return Name for userId.
     */
    public String getUserName(String userId) throws CoeusClientException{
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(GET_USER_NAME);
        vecUserDetails.add(userId);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            Vector responseData = response.getDataObjects();
            UserInfoBean userInfoBean = (UserInfoBean)responseData.get(0);
            return userInfoBean.getUserName();
        }
        else{
            throw new CoeusClientException(0, COULD_NOT_FETCH_DETAILS, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** checks wheather the delegatedBy user can delegate work to delegatedTo user
     * for the effectiveDate.
     *
     * @param delegatedBy userId who has delegated work.
     * @param delegatedTo userId who has been delegated work.
     * @param effectiveDate date from which the Delegations take effect.
     * @throws CoeusClientException if server could not perform the request.
     * @return value got from the server.
     * possible return values are 1, -1, -2, -3.
     *
     * 1 : OK Can Proceed with Saving.
     * -1 : This person has delegated work to someone else.
     * -2 : You have accepted delegated work.  You cannot delegate to someone else.
     * -3 : This person has previously rejected your delegation.
     */
    public int canDelegate(String delegatedBy, String delegatedTo, Date effectiveDate) throws CoeusClientException {
        ResponderBean response = null;
        
        Vector details = new Vector();
        details.add(CAN_DELEGATE);
        details.add(delegatedBy);
        details.add(delegatedTo);
        details.add(effectiveDate);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            return ((Integer)response.getDataObjects().get(0)).intValue();
        }
        else {
            throw new CoeusClientException(0, VALIDATION_FAILED, CoeusClientException.ERROR_MESSAGE);
        }
        
    }
    
    /** creates new Delegation.
     * @param userDelegationsBean to create new Delegation.
     * @throws CoeusClientException if server could not perform the request.
     */
    public void create(UserDelegationsBean userDelegationsBean) throws CoeusClientException{
        ResponderBean response = null;
        
        Vector details = new Vector();
        details.add(CREATE_DELEGATION);
        details.add(userDelegationsBean);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            //return ((Integer)response.getDataObjects().get(0)).intValue();
        }
        else {
            throw new CoeusClientException(0, COULD_NOT_CREATE, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** updates the Delegation.
     * @param userDelegationsBean the Delegation to be updated.
     * @throws CoeusClientException if server could not perform the request.
     */
    public void update(UserDelegationsBean userDelegationsBean) throws CoeusClientException{
        ResponderBean response = null;
        
        Vector details = new Vector();
        details.add(UPDATE_DELEGATION);
        details.add(userDelegationsBean);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            
        }
        else {
            throw new CoeusClientException();
        }
    }
    
    /** updates the Delegated Responsibilities status.
     * @param userDelegationsBean to update Responsibility Status.
     * @throws CoeusClientException if server could not perform requested operation.
     */
    public void updateDelegatedStatus(UserDelegationsBean userDelegationsBean) throws CoeusClientException {
        ResponderBean response = null;
        
        Vector details = new Vector();
        details.add(UPDATE_DELEGATED_STATUS);
        details.add(userDelegationsBean);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
        }
        else {
            throw new CoeusClientException();
        }
    }
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    /*
     * Delete the 'Closed' and 'Rejected' Delegations
     * @param userDelegationsBean is the delegation of status 'Closed' / 'Rejected'
     * which is selected to be deleted
     * @exception throws CoeusClientException,CoeusException
     */
    public void deleteDelegation(UserDelegationsBean userDelegationsBean)
        throws CoeusClientException,CoeusException{
        
        ResponderBean response = null;
        Vector details = new Vector();
        details.add(DELETE_DELEGATION);
        details.add(userDelegationsBean);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.hasResponse()) {
        }
        else {
            throw new CoeusClientException();
        }
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
}
