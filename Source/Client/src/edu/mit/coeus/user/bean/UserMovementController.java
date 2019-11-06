/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * UserMovementController.java
 *
 * Created on August 11, 2003, 2:13 PM
 */

package edu.mit.coeus.user.bean;

import java.util.*;

import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;

/**
 *
 * @author  sharathk
 */
public class UserMovementController {
    
    private Hashtable hashTable;
    
    private RequesterBean requester;
    
    private static final String COMMAND = "UserMovementHandler";
    private static final String USER_MAINTENENCE_SERVLET = "/userMaintenanceServlet";
    
    private static final String UPDATE = "U";
    
    private static final String GET_MAP_INFO = "GET_MAP_INFO";
    private static final String MOVE_USER = "MOVE_USER";
    
    private static final String COULD_NOT_FETCH_DETAILS = "Could Not Fetch Details";
    
    /** Creates a new instance of UserMovementController */
    public UserMovementController() {
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
    
    public boolean isUserMapped(UserInfoBean userInfoBean) throws CoeusClientException {
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(GET_MAP_INFO);
        vecUserDetails.add(userInfoBean);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            
            if(((Integer)response.getDataObjects().get(0)).intValue() > 0)
            {
                return true;
            }
            return false;
        }
        else{
            throw new CoeusClientException(0, COULD_NOT_FETCH_DETAILS, CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    public boolean save(UserInfoBean userInfoBean){
        userInfoBean.setAcType(UPDATE);
        ResponderBean response = null;
        
        Vector vecUserDetails = new Vector();
        vecUserDetails.add(MOVE_USER);
        vecUserDetails.add(userInfoBean);
        
        requester = new RequesterBean();
        hashTable.put(DataPositions.DATA,vecUserDetails);
        
        Vector data = new Vector();
        data.add(hashTable);
        
        requester.setDataObjects(data);
        
        response = sendToServer(USER_MAINTENENCE_SERVLET,requester);
        if(response.isSuccessfulResponse()) {
            return true;
        }
        else{
            return false;
        }
        
    }
    
}
