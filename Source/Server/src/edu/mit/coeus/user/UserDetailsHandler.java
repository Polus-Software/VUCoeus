/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * UserDetailsHandler.java
 *
 * Created on July 21, 2003, 5:43 PM
 */

package edu.mit.coeus.user;

import java.util.*;

import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.utils.dbengine.DBException;

/** handles all requests from User Details Controller
 * to get/set User Details related information.
 * @author sharathk
 */
public class UserDetailsHandler extends  UserMaintenanceRequestHandler{
    
    private Vector data;
    private Hashtable hashtable;
    private char functionType;
    private String userId;
    
    private static final String GET_ROLES_NOT_ASSIGNED = "GET_ROLES_NOT_ASSIGNED";
    private static final String READ_USER_ROLES = "READ_USER_ROLES";
    private static final String READ_ROLES_AND_AUTHORIZATIONS = "READ_ROLES_AND_AUTHORIZATIONS";
    private static final String IS_USER_DUPLICATE = "IS_USER_DUPLICATE";
    private static final String USER_FOR_UNIT = "USERFORUNIT";
    private static final String GET_DEPT_UNIT = "GET_DEPT_UNIT";
    
    //Authorization Function Types
    private static final String ADD_USER = "ADD_USER";
    private static final String MODIFY_USER = "MODIFY_USER";
    private static final String MAINTAIN_USER_ACCESS = "MAINTAIN_USER_ACCESS";
    private static final String GRANT_INSTITUTE_ROLES = "GRANT_INSTITUTE_ROLES";
    
    /** Creates a new instance of UserDetailsHandler */
    public UserDetailsHandler() {
    }
    
    public void doPerform() throws Exception {
        hashtable = (Hashtable)dataObjects.get(0);
        data = (Vector)hashtable.get(DataPositions.DATA);
        if(data.get(0).toString().equalsIgnoreCase(GET_ROLES_NOT_ASSIGNED)) {
            //System.out.println("GET_ROLES_NOT_ASSIGNED");
            fetchValues();
        }
        else if(data.get(0).toString().equalsIgnoreCase(READ_USER_ROLES)) {
            //1st value is User Id , second value is Unit Number.
            readUserRoles(data.get(1).toString().trim(), data.get(2).toString().trim());
        }
        else if(data.get(0).toString().equalsIgnoreCase(READ_ROLES_AND_AUTHORIZATIONS)) {
            //1st value is User Id , Second is allRolesUnitNumber, Third value is Unit Number
            readRolesAndAuthorizations(data.get(1).toString().trim(), data.get(2).toString().trim(), data.get(3).toString().trim());
        }
        else if(data.get(0).toString().equalsIgnoreCase(IS_USER_DUPLICATE)) {
            isUserDuplicate(data.get(1).toString().trim());
        // Added by chandra to check for the already existing user - 21-Sept-2004 - start    
        }else if(data.get(0).toString().equalsIgnoreCase(USER_FOR_UNIT)){
            getUserForUnit(data.get(1).toString().trim());
            // end chandra - 21 sept 2004
        }
        // bug Fix #1699 - start
        else if(data.get(0).toString().equalsIgnoreCase(GET_DEPT_UNIT)){
            getCampusForDept(data.get(1).toString().trim());
        }// bug Fix #1699 - end
        
        else {
            setValues(data);
        }
    }
    
    /** reads the roles of the user in the unit.
     * @param userId user Id whose roles have to be read.
     * @param unitNumber unit number of user.
     */    
    public void readUserRoles(String userId, String unitNumber) {
        //System.out.println("User Id : "+userId);
        //System.out.println("Unit Number : "+unitNumber);
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        try{
            data = bean.getUserRolesDetails(userId, unitNumber);
            dataObjects.removeAllElements();
            dataObjects.add(data);
            writeToClient();
        }catch (edu.mit.coeus.exception.CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch ( edu.mit.coeus.utils.dbengine.DBException dbException) {
            dbException.printStackTrace();
        }catch (org.okip.service.shared.api.Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /** reads roles and Authorization details of the user.
     * @param userId user Id whose Authorizations have to be got.
     * @param allRolesUnitNumber unit number to read all Roles.
     * @param unitNumber unit number of user
     */    
    public void readRolesAndAuthorizations(String userId, String allRolesUnitNumber, String unitNumber) {
         UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
         try{
            boolean addUser = bean.getUserHasRight(userId, ADD_USER , unitNumber);
            boolean modifyUser = bean.getUserHasRight(userId, MODIFY_USER, unitNumber);
            boolean maintainUser = bean.getUserHasRight(userId, MAINTAIN_USER_ACCESS, unitNumber);
            boolean hasOSPRight = bean.getUserHasOSPRight(userId, GRANT_INSTITUTE_ROLES);
            
            Vector allRoles = bean.getRolesForUnit(allRolesUnitNumber);
            data = bean.getRolesForUnit(unitNumber);
            dataObjects.removeAllElements();
            
            HashMap authorizations = new HashMap();
            authorizations.put(ADD_USER, new Boolean(addUser));
            authorizations.put(MODIFY_USER, new Boolean(modifyUser));
            authorizations.put(MAINTAIN_USER_ACCESS, new Boolean(maintainUser));
            authorizations.put(GRANT_INSTITUTE_ROLES, new Boolean(hasOSPRight));
            
            dataObjects.add(authorizations);
            dataObjects.add(allRoles);
            dataObjects.add(data);
            writeToClient();
        }catch (edu.mit.coeus.exception.CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch ( edu.mit.coeus.utils.dbengine.DBException dbException) {
            dbException.printStackTrace();
        }catch (org.okip.service.shared.api.Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void fetchValues() {
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        String unitNumber = (String)hashtable.get(DataPositions.UNIT_NUMBER);
        try{
            data = bean.getRolesForUnit(unitNumber);
            dataObjects.removeAllElements();
            dataObjects.add(data);
            writeToClient();
        }catch (edu.mit.coeus.exception.CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch ( edu.mit.coeus.utils.dbengine.DBException dbException) {
            dbException.printStackTrace();
        }catch (org.okip.service.shared.api.Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void setValues(Vector vecData){
        //0 th element contains user info bean
        //1 st element contains vector of user Role Info Beans
        UserInfoBean userInfoBean = (UserInfoBean)vecData.get(0);
        Vector vecRoleInfoBean = (Vector)vecData.get(1);
        
        //Should always take Loggedd In User - March 18, 2004
        //UserMaintUpdateTxnBean bean = new UserMaintUpdateTxnBean(userInfoBean.getUpdateUser());
        UserMaintUpdateTxnBean bean = new UserMaintUpdateTxnBean(userID);
        try{
            
            //Commented the following as all Updations should go in one Transaction - March 18, 2004
            /*boolean done = bean.updateUser(userInfoBean, vecRoleInfoBean);
            
            if(vecData.size() >= 3 && vecData.get(2) != null){
                Vector vecDbUser = (Vector)vecData.get(2);
                String name, password;
                name = vecDbUser.get(0).toString().trim();
                password = vecDbUser.get(1).toString();
                bean.createDBUser(name, password);
            }*/
            String password = "";
            boolean isCreateDBUser = false;
            if(vecData.size() >= 3 && vecData.get(2) != null){
                Vector vecDbUser = (Vector)vecData.get(2);
                password = vecDbUser.get(1).toString();
                isCreateDBUser = true;
            }
            boolean done = bean.updateUser(userInfoBean, vecRoleInfoBean, isCreateDBUser, password);
            
            dataObjects.removeAllElements();
            writeToClient();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** checks whether the user already exists.
     * @param userId who has to be checked for duplicacy.
     */
    public void isUserDuplicate(String userId) {
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        dataObjects.removeAllElements();
        try{
            dataObjects.add(new Boolean(bean.getIsUserExist(userId)));
            writeToClient();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
     /** Added by chandra 21 Sept 2004
      *checks whether the user already exists in any other unit.
      *If the user presents then throw the message
      * @param userId who has to be checked for existency.
      */
    public void getUserForUnit(String userId){
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        dataObjects.removeAllElements();
        try{
            UserInfoBean userInfoBean = bean.getUser(userId);
            if(userInfoBean.getUnitNumber()!= null){
                dataObjects.add(userInfoBean.getUnitNumber());
            }else{
                dataObjects.add(null);
            }
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }// End getUserForUnit(String userId) - 21 Sept 2004
    
    /** 
      *Get the toplevel unit for the dept unit number.
      * @param unit number.
      */
    public void getCampusForDept(String unitNumber) throws Exception{
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        dataObjects.removeAllElements();
      //  try{
            String topLevelUnitNumber = bean.getCampusForDept(unitNumber);
            if(topLevelUnitNumber!= null){
                dataObjects.add(topLevelUnitNumber);
            }else{
                dataObjects.add(null);
            }
            writeToClient();
//        }catch (Exception exception) {
//            exception.printStackTrace();
//        }
    }
}
