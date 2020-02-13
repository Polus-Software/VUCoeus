/*
 * UserPreferencesController.java
 *
 * Created on July 8, 2003, 2:45 PM
 */

package edu.mit.coeus.user.bean;

import java.util.Vector;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.user.bean.DataPositions;

/**
 *
 * @author  sharathk
 */
public class UserPreferencesController {
    
    private String CONNECTION_URL = CoeusGuiConstants.CONNECTION_URL;
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE =    "application/octet-stream";
    private final String USER_PREFERENCES_SERVLET = "/userMaintenanceServlet";
    private  RequesterBean requester;
    
    //Added COEUSQA-2429 : CLONE -User preferences not getting saved - Start
    private static final char USER_PREFERENCES = 'Z';
    //COEUSQA-2429 : End
    
    /** Creates a new instance of UserPreferencesController */
    public UserPreferencesController() {
    }
    
    /**
     *  Method used to send the requester Bean to the servlet for database communication.
     *
     * @param srvComponentName the Servlet to be used for communication to the database
     * @param requester a RequesterBean which consist of userId and servlet details.
     *
     * @return ResponderBean
     *
     */
    private ResponderBean sendToServer(String srvComponentName,RequesterBean requester) {
        String connectTo =CoeusGuiConstants.CONNECTION_URL+srvComponentName;
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        return response;
    }
    
    /**
     *  Method used to get the User Preferences Details for a given userId from
     *  the databse
     *
     *  @param userId id for the user.
     *
     *  @return Collection od UserPreferencesBean
     */
    public Vector displayUserPreferencesInfo(String userId) {
        String DISPLAY_TITLE = "DISPLAY USER PREFERENCES";
        Vector responseData = new Vector();
        UserPreferencesBean userPreferencesBean = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean response=null;
        
        /*set the function type as 'V' to notify the servlet to get the information from the database */
        
        //Modified for COEUSQA-2429 : CLONE -User preferences not getting saved - Start
//        requester.setFunctionType('U');
        requester.setFunctionType(USER_PREFERENCES);
        //COEUSQA-2429 : End
        requester.setRequestedForm("User Preference Details");
        
        /*set the data for requester bean with 'userId' a key value to get the information */
        Vector data = new Vector();
        java.util.Hashtable hashTable = new java.util.Hashtable();
        hashTable.put(DataPositions.UNIT_NUMBER,"");
        hashTable.put(DataPositions.COMMAND,"UserPreferencesHandler");
        hashTable.put(DataPositions.MODULE,"user");
        hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.DISPLAY_MODE));
        hashTable.put(DataPositions.PERSON_ID,userId);
        
        data.add(0,hashTable);
        
        requester.setUserName(userId);
        requester.setDataObjects(data);
        
        /* send the requester bean with the information to the server */
        response = sendToServer(USER_PREFERENCES_SERVLET,requester);
        if (response != null) {
            /* responseBean will be having UserPreferencesDetails bean if it successful in retrieving the information
             * from the database
             */
            responseData  = (Vector)response.getDataObjects();
        }
        /* return the UserPreferences bean Collection*/
        return responseData;
    }
    
    /** Updates the collection of UserPreferencesBean.
     * @param vecUserPreferences Collection of UserPreferences.
     * @throws CoeusException when not able to update to Database.
     */
    public void update(Vector vecUserPreferences)throws Exception {
        String DISPLAY_TITLE = "UPDATE USER PREFERENCES";
        Vector responseData = new Vector();
        UserPreferencesBean userPreferencesBean = null;
        RequesterBean requester = new RequesterBean();
        ResponderBean response=null;
        
        /*set the function type as 'U' to notify the servlet to update the information to the database */
        //Modified for COEUSQA-2429 : CLONE -User preferences not getting saved - Start
//        requester.setFunctionType('U');
        requester.setFunctionType(USER_PREFERENCES);
        //COEUSQA-2429 : End
        requester.setRequestedForm("User Preference Details");
        
        /*Set acType as Update for UserPreferencesBean*/
        /*for(int count=0;count < vecUserPreferences.size();count++)
        {
            UserPreferencesBean bean = (UserPreferencesBean)vecUserPreferences.get(count);
            bean.setAcType("U");
        }*/
        
        /*set the data for requester bean with 'userId' a key value to get the information */
        Vector data = new Vector();
        java.util.Hashtable hashTable = new java.util.Hashtable();
        hashTable.put(DataPositions.UNIT_NUMBER,"");
        hashTable.put(DataPositions.COMMAND,"UserPreferencesHandler");
        hashTable.put(DataPositions.MODULE,"user");
        hashTable.put(DataPositions.FUNCTION,new Character(edu.mit.coeus.utils.TypeConstants.MODIFY_MODE));
        hashTable.put(DataPositions.PERSON_ID,CoeusGuiConstants.getMDIForm().getUserId());
        hashTable.put(DataPositions.DATA,vecUserPreferences);
        
        data.add(0,hashTable);
        
        requester.setUserName("");
        requester.setDataObjects(data);
        
        /* send the requester bean with the information to the server */
        response = sendToServer(USER_PREFERENCES_SERVLET,requester);
        if (response != null) {
            /* responseBean will be having UserPreferencesDetails bean if it successful in retrieving the information
             * from the database
             */
            responseData  = (Vector)response.getDataObjects();
        }
    }
    
}
