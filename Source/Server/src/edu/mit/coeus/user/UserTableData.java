/*
 * UserTableData.java
 *
 * Created on July 8, 2003, 6:41 PM
 */

package edu.mit.coeus.user;

import edu.mit.coeus.bean.UserDetailsBean;
import java.util.*;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.bean.*;
/**
 *
 * @author  senthilar
 */
public class UserTableData extends UserMaintenanceRequestHandler{
    UserDetailsBean userDetailsBean = null;
    Vector userInfo = null;
    Vector userRoleInfo = null;

    /** Creates a new instance of UserTableData */
    public UserTableData() {
    }
        
    public void doPerform() throws Exception{

        if (functionType == 'D') {
            fetchValues();
        }
        else if (functionType == 'U'){
            setValues(data);
        }
    }
        
    public void fetchValues() {
        
        //System.out.println("Entering fetchValues()");
        userDetailsBean = new UserDetailsBean();        
        try{
            UserMaintDataTxnBean txnBean = new UserMaintDataTxnBean();
            userInfo = txnBean.getUsersForUnit(unitNumber);          
            userRoleInfo = txnBean.getAllUserRolesForUnit(unitNumber);
            
            if ((userInfo == null) || (userRoleInfo == null)){
                //System.out.println("NullPointerException");
                return;
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        //System.out.println("Sending the vector");
        dataObjects = new Vector();
        dataObjects.addElement(userInfo);
        dataObjects.addElement(userRoleInfo);
        writeToClient();
    }
    
    public void setValues(Vector vector) {
        //System.out.println("Entering setValues()");
        UserMaintUpdateTxnBean userMaintUpdateTxnBean = null;
        try{
            //System.out.println("userID = " + userID);
                   
            Hashtable hashTable = (Hashtable)dataObjects.get(0);
            Vector data = (Vector)hashTable.get(DataPositions.DATA);
            Vector added = (Vector)data.get(0);
            Vector deleted = (Vector)data.get(1);
            int rolesAdded, rolesDeleted;
            
            Vector userRole = new Vector();
            
            if(added == null) rolesAdded=0;
            else rolesAdded = added.size();
            
            if(deleted == null) rolesDeleted=0;
            else rolesDeleted = deleted.size();
            
            RoleInfoBean roleInfoBean;
            UserInfoBean userInfoBean;
            for(int index = 0;index < rolesAdded; index++) {
                UserRolesInfoBean rolesBean= (UserRolesInfoBean)added.get(index);
                roleInfoBean = rolesBean.getRoleBean();
                userInfoBean = rolesBean.getUserBean();
                
                roleInfoBean.setUserId(userInfoBean.getUserId());
                roleInfoBean.setAcType(INSERT_RECORD);
                userRole.add(roleInfoBean);
            }
            
            for(int index = 0; index < rolesDeleted; index++) {
                UserRolesInfoBean rolesBean= (UserRolesInfoBean)deleted.get(index);
                roleInfoBean = rolesBean.getRoleBean();
                userInfoBean = rolesBean.getUserBean();
                
                //System.out.println("Update Time Stamp : "+userInfoBean.getUpdateTimestamp());
                //System.out.println("Update User : "+userInfoBean.getUpdateUser());
                
                roleInfoBean.setUpdateTimestamp(userInfoBean.getUpdateTimestamp());
                roleInfoBean.setUpdateUser(userInfoBean.getUpdateUser());
                roleInfoBean.setUserId(userInfoBean.getUserId());
                roleInfoBean.setAcType(DELETE_RECORD);
                userRole.add(roleInfoBean);
            }
            
            userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userID);
            boolean updated = userMaintUpdateTxnBean.addUpdDeleteUserRoles(userRole);
            //System.out.println("is the setValues in UserTableData went good.. " + updated);     
            
        }
        catch(DBException dbe){
            dbe.printStackTrace();
            //System.out.println("DBException raised in UserTableData.setValue(Vector)");
        }
        catch(CoeusException ce){
            ce.printStackTrace();
            //System.out.println("CoeusException raised in UserTableData.setValue(Vector)");
        }
    }
    
}



