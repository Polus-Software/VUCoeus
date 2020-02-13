/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * UserDetailsController.java
 *
 * Created on July 21, 2003, 4:25 PM
 */

package edu.mit.coeus.user.bean;

import java.util.*;

import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.UserInfoBean;


/** Communicates with the Server to set/get Values of User Details.
 * @author sharathk
 */
public class UserDetailsController {
    
    private String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE =    "application/octet-stream";
    private final String USER_MAINTENENCE_SERVLET = "/userMaintenanceServlet";
    
    private  RequesterBean requester;
    private Hashtable hashTable;
    
    //Class Variables
    private static final String GET_ROLES_NOT_ASSIGNED = "GET_ROLES_NOT_ASSIGNED";
    private static final String COMMAND = "UserDetailsHandler";
    private static final String READ_USER_ROLES = "READ_USER_ROLES";
    private static final String READ_ROLES_AND_AUTHORIZATIONS = "READ_ROLES_AND_AUTHORIZATIONS";
    private static final String IS_USER_DUPLICATE = "IS_USER_DUPLICATE";
    private static final String GET_DEPT_UNIT = "GET_DEPT_UNIT";
    
    private static final String COULD_NOT_FETCH_ROLES = "user_details_exceptionCode.2533";
    private static final String COULD_NOT_CREATE_USER = "user_details_exceptionCode.2534";
    private static final String COULD_NOT_READ_USER_DETAILS = "user_details_exceptionCode.2535";
    private static final String COULD_NOT_UPDATE_USER = "user_details_exceptionCode.2549";
    private static final String NOT_A_VALID_UNIT_NUMBER = "Not a valid unit number";
    
    /** Creates a new instance of UserDetailsController */
    public UserDetailsController() {
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
    
    /** creates User Details.
     * @param userInfoBean contains user Information.
     * @param vecUserRoles user Roles.
     * @param vecDbUser if user is a DB User this vector
     * contains name and password as the first two elements.
     * @throws CoeusClientException if server could not process the request.
     * @return data from server.
     */    
    public Object create(UserInfoBean userInfoBean, Vector vecUserRoles, Vector vecDbUser)throws CoeusClientException{
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(userInfoBean);
        vecUserDetails.add(vecUserRoles);
        vecUserDetails.add(vecDbUser);
        
        requester = new RequesterBean();
        //Hashtable hashTable = new Hashtable();
        //hashTable.put(DataPositions.UNIT_NUMBER,CoeusGuiConstants.INSTITUTE_UNIT_NUMBER);
        //hashTable.put(DataPositions.COMMAND,COMMAND);
        //hashTable.put(DataPositions.MODULE,"user");
        //hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.ADD_MODE));
        //hashTable.put(DataPositions.PERSON_ID,"");
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            return response.getDataObjects();
        }
        else{
            throw new CoeusClientException(0, COULD_NOT_CREATE_USER, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** reads User Details.
     * @param userId whose details has to be read.
     * @param unitNumber of the User.
     * @throws CoeusClientException if server could not process the request.
     * @return User Details.
     */    
    public Object read(String userId, String unitNumber)throws CoeusClientException {
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(READ_USER_ROLES);
        vecUserDetails.add(userId);
        vecUserDetails.add(unitNumber);
        
        requester = new RequesterBean();
        //Hashtable hashTable = new Hashtable();
        //hashTable.put(DataPositions.UNIT_NUMBER,CoeusGuiConstants.INSTITUTE_UNIT_NUMBER);
        //hashTable.put(DataPositions.COMMAND,COMMAND);
        //hashTable.put(DataPositions.MODULE,"user");
        //hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.ADD_MODE));
        //hashTable.put(DataPositions.PERSON_ID,"");
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            return response.getDataObjects();
        }
        else {
            throw new CoeusClientException(0, COULD_NOT_READ_USER_DETAILS, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** reads Roles and Authorization Details for the logged in User.
     * @param userId logged in User Id.
     * @param allRolesUnitNumber unit number to get All Roles.
     * @param unitNumber unit Number of Logged in User.
     * @throws CoeusClientException if server could not process the request.
     * @return All Roles and User Authorization Details.
     */    
    public Object readRolesAndAuthorizations(String userId, String allRolesUnitNumber, String unitNumber) throws CoeusClientException {
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(READ_ROLES_AND_AUTHORIZATIONS);
        vecUserDetails.add(userId);
        vecUserDetails.add(allRolesUnitNumber);
        vecUserDetails.add(unitNumber);
        requester = new RequesterBean();
        
        //Hashtable hashTable = new Hashtable();
        //hashTable.put(DataPositions.UNIT_NUMBER,CoeusGuiConstants.INSTITUTE_UNIT_NUMBER);
        //hashTable.put(DataPositions.COMMAND,COMMAND);
        //hashTable.put(DataPositions.MODULE,"user");
        //hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.ADD_MODE));
        //hashTable.put(DataPositions.PERSON_ID,"");
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        
        if(response.isSuccessfulResponse()) {
            return response.getDataObjects();
        }
        else {
            throw new CoeusClientException(0,COULD_NOT_FETCH_ROLES,CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    
    /** gets the Roles Not Assigned to User.
     * @param unitNumber unit Number of User.
     * @throws CoeusClientException if server could not process the request.
     * @return Roles Not Assigned.
     */    
    public Object getRolesNotAssigned(String unitNumber)throws CoeusClientException {
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(GET_ROLES_NOT_ASSIGNED);
        requester = new RequesterBean();
        
        //Hashtable hashTable = new Hashtable();
        //hashTable.put(DataPositions.UNIT_NUMBER,CoeusGuiConstants.INSTITUTE_UNIT_NUMBER);
        //hashTable.put(DataPositions.COMMAND,COMMAND);
        //hashTable.put(DataPositions.MODULE,"user");
        //hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.ADD_MODE));
        //hashTable.put(DataPositions.PERSON_ID,"");
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        
        if(response.isSuccessfulResponse()) {
            return response.getDataObjects();
        }
        else {
            throw new CoeusClientException(0,COULD_NOT_FETCH_ROLES,CoeusClientException.ERROR_MESSAGE);//response.getException();
        }
    }
    
     /** updates User Details.
      * @param userInfoBean user Information which has to be updated.
      * @param vecUserRoles modified Roles.
      * @throws CoeusClientException if server could not process the request.
      * @return data from Server.
      */     
    public Object update(UserInfoBean userInfoBean, Vector vecUserRoles)throws CoeusClientException{
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(userInfoBean);
        vecUserDetails.add(vecUserRoles);
        
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
            throw new CoeusClientException(0, COULD_NOT_UPDATE_USER, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /** checks whether user already exists.
     * @param userId who has to be checked for duplicacy.
     * @throws CoeusClientException if server could not process the request.
     * @return true if user already exists.
     * else returns false.
     */    
    public boolean isUserDuplicate(String userId) throws CoeusClientException {
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(IS_USER_DUPLICATE);
        vecUserDetails.add(userId);
        requester = new RequesterBean();
        
        hashTable.put(DataPositions.DATA,vecUserDetails);
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        
        if(response.isSuccessfulResponse()) {
            return ((Boolean)response.getDataObjects().get(0)).booleanValue();
        }
        else {
            throw new CoeusClientException(0, COULD_NOT_READ_USER_DETAILS, CoeusClientException.ERROR_MESSAGE);
        }

    }
    
    /** Bug Fix #1699
     *@param Department Unit number.
     * @throws CoeusClientException if server could not process the request.
     * @return String of unit number.
     * else returns null.
     */   
    public String getCampusForDept(String unitnumber)throws CoeusClientException{
        ResponderBean response = null;
        String topLevelUnitNumber = null;
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(GET_DEPT_UNIT);
        vecUserDetails.add(unitnumber);
        requester = new RequesterBean();
        
        hashTable.put(DataPositions.DATA,vecUserDetails);
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        
        if(response.isSuccessfulResponse()) {
            if(response.getDataObjects().get(0)!= null){
                topLevelUnitNumber = response.getDataObjects().get(0).toString();
                return topLevelUnitNumber;
            }else{
                return null;
            }
        }
        else {
            throw new CoeusClientException(0, NOT_A_VALID_UNIT_NUMBER,CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    /**Added by chandra 21-Sept-2004 - start 
     *checks whether user already exists in any other unit.
     * @param userId who has to be checked existency of corresponding unit number .
     * @throws CoeusClientException if server could not process the request.
     * @return String of unit number.
     * else returns null.
     */    
    public String getUserForUnit(String userId) throws CoeusClientException {
        ResponderBean response = null;
        String unitNumber = null;
        Vector vecUserDetails = new Vector();
        vecUserDetails.add("USERFORUNIT");
        vecUserDetails.add(userId);
        requester = new RequesterBean();
        
        hashTable.put(DataPositions.DATA,vecUserDetails);
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        
        if(response.isSuccessfulResponse()) {
            if(response.getDataObjects().get(0)!= null){
                unitNumber = response.getDataObjects().get(0).toString();
                return unitNumber;
            }else{
                return null;
            }
        }
        else {
            throw new CoeusClientException(0, COULD_NOT_READ_USER_DETAILS, CoeusClientException.ERROR_MESSAGE);
        }

    }// end Chandra 21-Sept-2004
}
