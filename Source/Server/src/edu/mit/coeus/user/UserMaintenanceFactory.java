/*
 * UserMaintenanceFactory.java
 *
 * Created on June 30, 2003, 5:34 PM
 */

package edu.mit.coeus.user;

/**
 *
 * @author  senthilar
 */
public class UserMaintenanceFactory {
    
    /** Creates a new instance of UserMaintenanceFactory */
    public UserMaintenanceFactory() {
    }
    
    public UserMaintenanceRequestHandler create(String module, String command) throws Exception {
        UserMaintenanceRequestHandler handler = null;
        
        if ((command != null) && (module != null)){
             handler = (UserMaintenanceRequestHandler)Class.forName("edu.mit.coeus." + module + "." + command).newInstance();
        }
        return handler;
    }
}
