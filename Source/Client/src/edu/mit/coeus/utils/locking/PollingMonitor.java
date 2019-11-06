/*
 * PollingMonitor.java
 *
 * Created on August 7, 2004, 5:24 PM
 */

package edu.mit.coeus.utils.locking;

/**
 *
 * @author  shivakumarmj
 */
//import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;


import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Properties;
import java.util.Enumeration;


public class PollingMonitor {
    
    
    //Represents the string for conneting to the servlet and polling the lock Ids
    private static final String GET_SERVLET = "/LockingServlet";
    
    /*For connecting to the server*/
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    private Hashtable lockIds;
    private static PollingMonitor pollingMonitor;
    /** Creates a new instance of PollingMonitor */
    private PollingMonitor() {
        lockIds = new Hashtable();
        
    }
    
    /**
     * @return
     */    
    public static synchronized PollingMonitor getInstance(){
        if(pollingMonitor==null){
            pollingMonitor= new PollingMonitor();
        }
        return pollingMonitor;
    }
    
    
    /**The following method is used send the Hashtable to server which are to be polled 
     * @param htLockIds is the input
     * @throws CoeusException is an Exception class, which is used to represnt 
      * any exception comes in COEUS web module
     * @throws DBException is an Exception class, which is used to handle 
      * exceptions in dbEngine package during SQL Command execution
     * @return is Hashtable which contains the lock Idfs along with updated timestamp
     */    
    public Hashtable updateLockIdTimestamp(Hashtable htLockIds) throws 
           CoeusException{
            Hashtable htLockIdData = null;   
            RequesterBean requesterBean = new RequesterBean();
            ResponderBean responderBean = new ResponderBean();
            requesterBean.setFunctionType('E');
            CoeusVector cvTestData = new CoeusVector();
            cvTestData.addElement(htLockIds);
            /*Uncheck hard codeded the data*/
            requesterBean.setDataObject(htLockIds);

            AppletServletCommunicator comm = new AppletServletCommunicator(connect, requesterBean);
            comm.setRequest(requesterBean);
            comm.send();
            responderBean = comm.getResponse();   
            
            if(responderBean.isSuccessfulResponse()) {
                htLockIdData = (Hashtable)responderBean.getDataObject();
            }else{
                System.out.println("Unsuccessful in connecting to Server");
            }                
            return htLockIdData;               
    }    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
    
}