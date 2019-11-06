/*
 * UserPreferencesHandler.java
 *
 * Created on July 10, 2003, 5:50 PM
 */

package edu.mit.coeus.user;

import java.util.*;

import edu.mit.coeus.user.bean.*;
/**
 *
 * @author  sharathk
 */
public class UserPreferencesHandler extends  UserMaintenanceRequestHandler{
    
    private Vector data;
    private Hashtable hashtable;
    private char functionType;
    String userId;
    
    /** Creates a new instance of UserPreferencesHandler */
    public UserPreferencesHandler() {
        System.out.println("UserPreferencesHandler instance created");
    }
    
    public void doPerform() throws java.lang.Exception {
        System.out.println(dataObjects.getClass());
        System.out.println(dataObjects.get(0).getClass());
        hashtable = (Hashtable)dataObjects.get(0);
        functionType =  ((Character)hashtable.get(DataPositions.FUNCTION)).charValue();
        userId = hashtable.get(DataPositions.PERSON_ID).toString();
        if(functionType == edu.mit.coeus.utils.TypeConstants.DISPLAY_MODE) {
            fetchValues();
        }
        else if(functionType == edu.mit.coeus.utils.TypeConstants.MODIFY_MODE) {
            setValues(getData());
        }
        
    }
    
    public void fetchValues() {
        UserMaintDataTxnBean bean = new UserMaintDataTxnBean();
        try{
            dataObjects.removeAllElements();
            dataObjects.addElement(bean.getUserPreferences(userId));
            dataObjects.addElement(bean.getPreferenceVariablesNotAssigned(userId));
            writeToClient();
        }catch (edu.mit.coeus.exception.CoeusException coeusException) {
            coeusException.printStackTrace();
        }catch ( edu.mit.coeus.utils.dbengine.DBException dbException) {
            dbException.printStackTrace();
        }
    }
    
    public void setValues(java.util.Vector userPreferences) {
        //System.out.println("Set Values for User Preferences");
        //Should always instantiate with the Logged in User Id - 18th March, 2004
        //UserMaintUpdateTxnBean bean = new UserMaintUpdateTxnBean(userId);
        UserMaintUpdateTxnBean bean = new UserMaintUpdateTxnBean(userID);
        try{
            bean.addUpdDeletePreferences(userPreferences);
            // Start - Added by chandra to avoid EOFException
            writeToClient();
            // End chandra 
        }catch (edu.mit.coeus.exception.CoeusException coeusException) {
            coeusException.printStackTrace();
        }
        catch ( edu.mit.coeus.utils.dbengine.DBException dbException) {
            dbException.printStackTrace();
        }
    }
    
}
