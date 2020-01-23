/*
 * @(#)CoeusApplet.java 1.0 7/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * UserRolesHandler.java
 *
 * Created on August 2, 2003, 11:25 AM
 */

package edu.mit.coeus.user;

import java.util.*;

import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.bean.UserInfoBean;

/**handles all requests from UserRolesController
 * to get/set User Roles related information.
 * @author  chandrashekara
 */
public class UserRolesHandler extends UserMaintenanceRequestHandler {
    
    private Vector data=null;
    private Hashtable hashtable;
    private char functionType;
    String userId;
    
    private static final String MAINTAIN_PROPOSAL_ACCESS = "MAINTAIN_PROPOSAL_ACCESS";
    private static final String READ_USER = "READ_USER";
    
    /** Creates a new instance of UserRolesHandler */
    public UserRolesHandler() {
        
    }
    /**
     *This is a Overridden method.This will communicate with the server and performs the actions
     */
    public void doPerform() throws java.lang.Exception {
        System.out.println(dataObjects.getClass());
        System.out.println(dataObjects.get(0).getClass());
        hashtable = (Hashtable)dataObjects.get(0);
        functionType =  ((Character)hashtable.get(DataPositions.FUNCTION)).charValue();
        Vector vctData = null;
        vctData = (Vector)hashtable.get(DataPositions.DATA);
        userId = (String)vctData.elementAt(0);
        
        String type = vctData.get(0).toString();
        if(type.equalsIgnoreCase(MAINTAIN_PROPOSAL_ACCESS)) {
            //1st data is userId, 2nd Data is Unit Number
            maintainProposalAccess(vctData.get(1).toString(), vctData.get(2).toString());
        }
        else if(type.equals(READ_USER)) {
            readUser(vctData.get(1).toString());
        }
        else if(functionType == edu.mit.coeus.utils.TypeConstants.DISPLAY_MODE) {
            fetchValues();
        }
    }
    /**
     *Fetches the data from the ServerSide bean  -  UserMaintDataTxnBean
     */
    public void fetchValues(){
        try{
            UserMaintDataTxnBean userRolebean = new UserMaintDataTxnBean();
            dataObjects.removeAllElements();
            data = userRolebean.getAllRolesForUser(userId);
            dataObjects.addElement(data);
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    /**
     *Overridden method. Abstract method in UserMaintenanceRequestHandler. To set the  values.
     */
    public void setValues(Vector vector){
        
    }
    
    private void maintainProposalAccess(String userId, String unitNo) {
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        try{
            boolean hasRight = bean.getUserHasRight(userId, MAINTAIN_PROPOSAL_ACCESS, unitNo);
            dataObjects.removeAllElements();
            dataObjects.add(new Boolean(hasRight));
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    private void readUser(String userId) {
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        try{
            UserInfoBean userInfoBean = bean.getUser(userId);
            dataObjects.removeAllElements();
            dataObjects.add(userInfoBean);
            writeToClient();
        }catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
}
