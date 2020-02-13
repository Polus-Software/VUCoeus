/*
 * @(#)CoeusApplet.java 1.0 7/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * UserRolesController.java
 *
 * Created on August 2, 2003, 11:10 AM
 */

package edu.mit.coeus.user.bean;

import java.util.*;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.user.bean.DataPositions;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.bean.UserInfoBean;

/**
 *
 * @author  chandrashekara
 */
public class UserRolesController {
    
    private String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    private final String USER_ROLES_SERVLET = "/userMaintenanceServlet";
    private static final String COMMAND = "UserRolesHandler";
    
    private  RequesterBean requesterBean;
    ResponderBean responderBean;
    private Hashtable hashTable;
    
    private String COULD_NOT_LOAD_USER_ROLES = "user_roles_exceptionCode.2563";
    
    private static final String MAINTAIN_PROPOSAL_ACCESS = "MAINTAIN_PROPOSAL_ACCESS";
    private static final String READ_USER = "READ_USER";
    
    /** 
     *Creates a new instance of UserRolesContrller 
     */
    
    public UserRolesController() {
        hashTable = new Hashtable();
        hashTable.put(DataPositions.UNIT_NUMBER,CoeusGuiConstants.INSTITUTE_UNIT_NUMBER);
        hashTable.put(DataPositions.COMMAND,COMMAND);
        hashTable.put(DataPositions.MODULE,"user");
        hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.ADD_MODE));
        hashTable.put(DataPositions.PERSON_ID,"");
    }

     /**
     *  Method used to send the requester Bean to the servlet for database communication.
     * @param userId the Servlet to be used for communication to the database
     * @param requesterBean a RequesterBean which consist of userId and servlet details.
     * @return ResponderBean
     */
  
    private ResponderBean sendToServer(String servlet,RequesterBean requesterBean){
        
        String connectTo = CONNECTION_URL + USER_ROLES_SERVLET;
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        return responderBean;
    }

      /**
     *  Method used to get the User Roles Details for a given userId from
     *  the databse
     *
     *  @param userId id for the user.
     *
     *  @return Collection of getAllRolesForUser
     */
    
    public Vector displayUserRolesInfo(String userId) throws 
    CoeusClientException, Exception  {
      
        Vector responseData = new Vector();
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean=null;
        
        /*set the function type as 'D' to notify the servlet to get the information from the database */
        //Commented for Case#3500- Exception thrown while trying to view 'Assigned Roles' - Start
//        requesterBean.setFunctionType('D');
        //Commented for Case#3500- Exception thrown while trying to view 'Assigned Roles' - End
        requesterBean.setRequestedForm("UserRole");
        
        /*set the data for requesterBean bean with 'userId' a key value to get the information */
        Vector data = new Vector();
        java.util.Hashtable hashTable = new java.util.Hashtable();
        hashTable.put(DataPositions.UNIT_NUMBER,"");
        hashTable.put(DataPositions.COMMAND,"UserRolesHandler");
        hashTable.put(DataPositions.MODULE,"user");
        hashTable.put(DataPositions.FUNCTION,new Character(
        edu.mit.coeus.utils.TypeConstants.DISPLAY_MODE));
        hashTable.put(DataPositions.PERSON_ID,"");
        Vector vctDataToBeSent = new Vector();
        vctDataToBeSent.addElement(userId);
        hashTable.put(DataPositions.DATA, vctDataToBeSent);
        
        data.add(0,hashTable);
        
        requesterBean.setUserName(" ");
        requesterBean.setDataObjects(data);

        /* send the requesterBean bean with the information to the server */
       responderBean = sendToServer(USER_ROLES_SERVLET,requesterBean);

        if(responderBean.isSuccessfulResponse()){
            
            /* responseBean will be having UserRoles bean if it successful in retrieving the information
             * from the database
             */
            responseData  = (Vector)responderBean.getDataObjects();
             
            /* return the UserRole bean Collection*/
            return responseData;
         }
        else{
             throw new CoeusClientException(0, COULD_NOT_LOAD_USER_ROLES, 
             CoeusClientException.ERROR_MESSAGE);
        }
    }
    
    public boolean getProposalAccessRights(String userId, String unitNumber) {
        ResponderBean response = null;
        
        Vector details = new Vector();
        details.add(MAINTAIN_PROPOSAL_ACCESS);
        details.add(userId);
        details.add(unitNumber);
        
        requesterBean = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        requesterBean.setDataObjects(data);
        
        response = sendToServer(USER_ROLES_SERVLET, requesterBean);

        if(response.isSuccessfulResponse()){
            return ((Boolean)response.getDataObjects().get(0)).booleanValue();
        }
        else{
            return false;
        }
        
    }
    
    public UserInfoBean getUser(String userId) throws CoeusClientException{
        ResponderBean response = null;
        
        Vector details = new Vector();
        details.add(READ_USER);
        details.add(userId);
                
        requesterBean = new RequesterBean();
        hashTable.put(DataPositions.DATA,details);
        
        Vector data = new Vector();
        data.add(hashTable);
        requesterBean.setDataObjects(data);
        
        response = sendToServer(USER_ROLES_SERVLET, requesterBean);

        if(response.isSuccessfulResponse()){
            return (UserInfoBean)response.getDataObjects().get(0);
        }
        else{
            throw new CoeusClientException();
        }
    }
    
    
}

