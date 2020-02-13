package edu.vanderbilt.coeus.utils;

import java.util.Vector;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;

public class UserPermissions {
	
	private String loggedinUser;
    private static final char USER_HAS_MODIFY_PERSON_RIGHT = 'R';
    private static final char USER_HAS_RIGHT_IN_UNIT = 'H';
    private static final char USER_HAS_ROLE = 'O';
    
    private static final String PERSON_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/personMaintenanceServlet";
    private static final String COEUS_FUNCTIONS_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/coeusFunctionsServlet";
    private static final String USER_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/userMaintenanceServlet";
    private static final String CUSTOM_SERVLET = CoeusGuiConstants.CONNECTION_URL+"/CustomFunctionsServlet";
    
	public UserPermissions(String loggedinUser) {
		this.loggedinUser = loggedinUser;
	}

    public boolean canModifyPerson() throws CoeusClientException {
    	boolean canModifyPerson = false;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(USER_HAS_MODIFY_PERSON_RIGHT);
        requester.setDataObject(loggedinUser);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(PERSON_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
        	canModifyPerson = (Boolean) response.getDataObject();
        }else {
        	System.out.println("Can not retrieve permissions for user " + loggedinUser);
        }
        return canModifyPerson;
    }
    
    public String hasOspRight() throws CoeusClientException {
    	String hasOspRight = null;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("GET_USER_HAS_ANY_OSP_RIGHTS");
        requester.setUserName(loggedinUser);
        requester.setId(loggedinUser);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(COEUS_FUNCTIONS_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasOspRight = (String) response.getId();
        } else {
        	System.out.println("Can not retrieve OSP permissions for user " + loggedinUser);
        }
        return hasOspRight;
    }

    public boolean hasRole(int roleId) throws CoeusClientException {
    	boolean hasRole = false;
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(USER_HAS_ROLE);
        requester.setDataObject(loggedinUser);
        requester.setId("" + roleId);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(CUSTOM_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
        	hasRole = (Boolean) response.getDataObject();
        } else {
        	System.out.println("Can not retrieve permissions for user " + loggedinUser);
        }
        return hasRole;
    }
    
    public boolean hasRight(String rightId) throws CoeusClientException {
    	boolean hasRight = false;
        RequesterBean requester = new RequesterBean();
        requester.setDataObject("FN_USER_HAS_RIGHT_IN_ANY_UNIT");
        requester.setId(rightId);
        requester.setUserName(loggedinUser);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(COEUS_FUNCTIONS_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasRight = (Boolean) response.getDataObject();
        } else {
        	System.out.println("Can not retrieve permissions for user " + loggedinUser);
        }
        return hasRight;
    }
    
    public boolean hasRightInUnit(String rightId,String unitCode) throws CoeusClientException {
    	Vector dataObjects = new Vector();
    	
    	boolean hasRight = false;
        RequesterBean requester = new RequesterBean();
        
        dataObjects.add(loggedinUser);
        dataObjects.add(unitCode);
        dataObjects.add(rightId);
        
        requester.setFunctionType(USER_HAS_RIGHT_IN_UNIT);
        
        requester.setDataObjects(dataObjects);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(USER_SERVLET, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            hasRight = (Boolean) response.getDataObject();
        } else {
        	System.out.println("Can not retrieve rights for user " + loggedinUser);
        }
        return hasRight;
    }
    
}
