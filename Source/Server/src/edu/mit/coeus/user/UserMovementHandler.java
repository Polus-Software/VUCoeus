/*
 * UserMovementHandler.java
 *
 * Created on August 11, 2003, 2:27 PM
 */

package edu.mit.coeus.user;

import java.util.*;

import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.user.bean.*;

/**
 *
 * @author  sharathk
 */
public class UserMovementHandler extends  UserMaintenanceRequestHandler{
    
    private static final String GET_MAP_INFO = "GET_MAP_INFO";
    private static final String MOVE_USER = "MOVE_USER";
    
    private Hashtable hashTable;
    private Vector data;
    
    /** Creates a new instance of UserMovementHandler */
    public UserMovementHandler() {
    }
    
    public void doPerform() throws Exception {
        hashTable = (Hashtable)dataObjects.get(0);
        data = (Vector)hashTable.get(DataPositions.DATA);
        String type = data.get(0).toString().trim();
        
        if(type.equals(GET_MAP_INFO)) {
            getMapInfo((UserInfoBean)data.get(1));
        }
        else if(type.equals(MOVE_USER)) {
            moveUser((UserInfoBean)data.get(1));
        }
    }
    
    public void fetchValues() {
    }
    
    public void setValues(Vector vector) {
    }
    
    private void getMapInfo(UserInfoBean userInfoBean) {
        UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
        try{
            int maps = userMaintDataTxnBean.getUserUnitMapInfo(userInfoBean.getUserId(), userInfoBean.getUnitNumber());
            dataObjects.removeAllElements();
            dataObjects.add(new Integer(maps));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    private void moveUser(UserInfoBean userInfoBean) {
        //Should always instantiate with the Logged in User Id - 18th March, 2004        
        //UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userInfoBean.getUpdateUser());
        UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(userID);
        try{
            userMaintUpdateTxnBean.updateUser(userInfoBean);
            dataObjects.removeAllElements();
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
