/*
 * RecordLocker.java
 *
 * Created on October 17, 2003, 8:55 PM
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.*;
import java.util.Vector;
/**
 * This class is used to lock any record of a given module in display mode.
 * @author  ravikanth
 */
public class RecordLocker {
    
    /** Creates a new instance of RecordLocker */
    public RecordLocker() {
    }
    
    /**
     * This method is used to lock the record for the given primarykey and module name.
     * @param moduleName String representing the module in which the primaryKey 
     * record should be locked.
     * @param primaryKey String representing the primaryKey to indentify the record to be locked.
     * @returns true if it successfully locks the record else throws the execption with
     * the appropriate error message.
     */
    public static boolean lock(String moduleName, String primaryKey ) throws CoeusException{
            RequesterBean request = new RequesterBean();
            request.setDataObject("LOCK_RECORD");
            Vector dataObjects = new Vector();
            dataObjects.addElement(moduleName);
            dataObjects.addElement( primaryKey );
            request.setDataObjects( dataObjects );
            AppletServletCommunicator comm = new AppletServletCommunicator(
                    CoeusGuiConstants.CONNECTION_URL + CoeusGuiConstants.FUNCTION_SERVLET ,request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response.isSuccessfulResponse()) {
                return true;
            }else{
                throw (CoeusException)response.getDataObject();
            }
    
    }
}
