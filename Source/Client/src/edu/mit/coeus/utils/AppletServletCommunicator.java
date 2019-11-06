/*
 * @(#)AppletServletCommunicator.java 1.0 15/25/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.utils;

import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.RemindTask;
import edu.ucsd.coeus.personalization.model.FormAttrModel;
//import edu.mit.coeus.gui.ExceptionDetailDialog;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.awt.Cursor;
import java.awt.Component;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import java.util.Hashtable;

/**
 * This Class is used as Bridge to Communicate form the Client End Applet to
 * Middleware/Server Side Call. All the requests are handled by the RequesterBean
 * and the Responses are obtained by the ResponderBean. It uses URL based
 * Connection Architecture to communicate to the Server/servlet End.
 *
 * Created on November 18, 2002, 3:43 PM
 * @version 1.0
 */
public class AppletServletCommunicator {
    
    ResponderBean response;
    RequesterBean request;
    InputStream inputToApplet;
    ObjectOutputStream outputToServlet;
    String connectTo;
    //Store the name of logged in user in the RequesterBean by getting it
    //from CoeusAppletMDIForm.
    JDesktopPane deskTopPane = null;
    JInternalFrame selectedFrame = null;
    CoeusAppletMDIForm mdiForm = null;
    private Hashtable htLock;
    public RemindTask remindTask;
    
    //private ExceptionDetailDialog exceptionDetailDialog;
    
    
    private final String CONTENT_KEY = "Content-Type";
    private final String CONTENT_VALUE =    "application/octet-stream";
    
    /**
     * This will construct New AppletServletCommunicator.
     */
    public AppletServletCommunicator(){
        mdiForm = CoeusGuiConstants.getMDIForm();
    }
    
    /**
     * This will construct New AppletServletCommunicator with RequesterBean
     * and the connection String.
     * @param connectTo String represent the connection URL.
     * @param request RequesterBean contains the applet request info.
     */
    public AppletServletCommunicator(String connectTo,RequesterBean request){
        this();
        this.connectTo=connectTo;
        this.request=request;
        //mdiForm = CoeusGuiConstants.getMDIForm();
    }
    
    /**
     * This Method is used to Set the Connection URL.
     * @param connectTo String represent the connection URL.
     */
    public void setConnectTo(String connectTo){
        this.connectTo=connectTo;
    }
    
    /**
     * This Method is used to get the Connection URL of the connection.
     * @return String represent the connection URL.
     */
    public String getConnectTo(){
        return connectTo;
    }
    
    /**
     * This Method is used to set the RequesterBean Information.
     * @param request RequesterBean represent the applet request info.
     */
    public void setRequest(RequesterBean request){
        this.request=request;
    }
    
    /**
     * This Method is used to get the RequesterBean.
     * @return RequesterBean represent the Requester info.
     */
    public RequesterBean getRequest(){
        return request;
    }
    
    /**
     * This Method is used to set the ResponderBean Information.
     * @param responder ResponderBean represent the server/servlet response info.
     */
    public void setResponse(ResponderBean responder){
        this.response=response;
    }
    
    /**
     * This Method is used to get the ResponderBean.
     * @return ResponderBean represent the Responder info(from server side).
     */
    public ResponderBean getResponse(){
        return response;
    }
    
    /**
     * This is Synchronized to call the send the request to the server/servlet end.
     */
    public synchronized void send(){
        try{
            if (mdiForm != null) {
                deskTopPane = mdiForm.getDeskTopPane();
                if(deskTopPane!=null){
                    deskTopPane.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                    selectedFrame = mdiForm.getSelectedFrame();
                }
                
                if(selectedFrame!=null){
                    selectedFrame.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                    /* updated by ravi on 18-02-03 for ignoring mouse clicks
                       when the system is busy with some operation */
                    selectedFrame.getGlassPane().setVisible(true);
                }
                mdiForm.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
                /* updated by ravi on 18-02-03 for ignoring mouse clicks
                   when the system is busy with some operation */
                mdiForm.getGlassPane().setVisible(true);
                request.setUserName(mdiForm.getUserName());
                
            }
            request.setAuthKey(ServerSecureCredetials.getServerSecureKey());
            //JIRA COEUSQA 2527 - START
            request.setSessionId(ServerSecureCredetials.getSessionId());
            //JIRA COEUSQA 2527 - END
            //URL urlSrvServlet = new URL(connectTo);
            //Added for Auth Enhancement - start
            URL urlSrvServlet = null;
            if(request.isIsAuthorizationRequired()){
                connectTo = CoeusGuiConstants.CONNECTION_URL+"/AuthorizationServlet";
                urlSrvServlet = new URL(connectTo);
            }else{
                urlSrvServlet = new URL(connectTo);
            }
            
            //Added for Auth Enhancement - end
            URLConnection servletConnection = urlSrvServlet.openConnection();
            // prepare for both input and output
            servletConnection.setDoInput(true);
            servletConnection.setDoOutput(true);
            // turn off caching
            servletConnection.setUseCaches(false);
            // Specify the content type that we will send binary data
            servletConnection.setRequestProperty(CONTENT_KEY, CONTENT_VALUE);
            // send the requester object to the servlet using serialization
            outputToServlet = new ObjectOutputStream(servletConnection.getOutputStream());
            // serialize the object and send to servlet
            outputToServlet.writeObject(request);
            outputToServlet.flush();
            outputToServlet.close();
            
            // read the object
            inputToApplet = servletConnection.getInputStream();
            response = (ResponderBean) (new ObjectInputStream(inputToApplet)).readObject();
            String encAuthKey = response.getAuthKey();
            //Set servlet auth key after successfull logged in
            ServerSecureCredetials.setServerSecureKey(encAuthKey);
            //JIRA COEUSQA 2527 - START
            ServerSecureCredetials.setSessionId(response.getSessionId());
            //JIRA COEUSQA 2527 - END
            // Code to retrieve LockingBean data
            if(response.getLockingBean() != null){
                LockingBean lockingBean = (LockingBean)response.getLockingBean();
                String lockIdKey = null;
                String updateTimestamp = null;
                if(lockingBean.getLockID()!= null &&lockingBean.getUpdateTimestamp()!= null){
                    lockIdKey = lockingBean.getLockID().toString();
                    updateTimestamp = lockingBean.getUpdateTimestamp().toString();
                }
                mdiForm.setUpdateTimestamp(updateTimestamp);
                
                htLock = new Hashtable();
                
                //Added by Ajay to remove lockID from HashTable
                //in client's side 06-09-2004 Start 2
                
                
                
                if(lockingBean.getAcType()!= null && lockingBean.getAcType().equals("D")){
                    htLock = mdiForm.getLockingData();
                    /* Removing lockid from Hashtable of RemindTask */
                    remindTask = mdiForm.getRemindTask();
                    remindTask.alterLockIdTable(lockIdKey);
                    
                    if(htLock != null && htLock.size()>0){
                        htLock.remove(lockIdKey);
                    }
                    if(htLock != null && htLock.size()>0){
                        mdiForm.setLocking(htLock);
                    }
                }else if(lockingBean.getAcType()!= null && lockingBean.getAcType().equals("I")){
                    mdiForm.setLockIdKey(lockIdKey);
                    htLock.put(lockIdKey, updateTimestamp);
                    remindTask = mdiForm.getRemindTask();
                    if(remindTask.getHtLockIds() !=  null && remindTask.getHtLockIds().size() >0){
                        Hashtable htLockIds = remindTask.getHtLockIds();
                        htLockIds.put(lockIdKey, updateTimestamp);
                        remindTask.setHtLockIds(htLockIds);
                    }else{
                        remindTask.setHtLockIds(htLock);
                        remindTask.setTimer();
                    }
                    mdiForm.setLocking(htLock);
                }
                // Added by Shivakumar for bug fixing while releasing lock - BEGIN
                else if(lockingBean.getAcType()!= null && lockingBean.getAcType().equals("E") && lockingBean.getLockID()!= null){
                    String lockID = lockingBean.getLockID();
                    /* Removing lockid from Hashtable of RemindTask */
                    remindTask = mdiForm.getRemindTask();
                    remindTask.alterLockIdTable(lockID);
                    
                    StringBuffer moduleName = new StringBuffer(lockID);
                    char lockIdCheck = moduleName.charAt(moduleName.length()-1);
                    boolean b=Character.isDigit(lockIdCheck);
                    //Added for COEUSQA-1442 : Have current locks say "unlock" instead of "delete" - start
                    // CoeusOptionPane.showInfoDialog("The lock for "+ lockingBean.getModuleName() +" "+lockingBean.getLockID()+" "+" has been deleted by Administrator.");
                    CoeusOptionPane.showInfoDialog(lockingBean.getModuleName() +" "+lockingBean.getLockID()+" "+" has been unlocked by Administrator.");
                    //Added for COEUSQA-1442 : Have current locks say "unlock" instead of "delete" - end
                    if(b){
                        htLock = mdiForm.getLockingData();
                        if(htLock != null && htLock.size()>0){
                            htLock.remove(lockIdKey);
                        }
                        if(htLock != null && htLock.size()>0){
                            mdiForm.setLocking(htLock);
                        }
                        // CoeusOptionPane.showInfoDialog("The lock for "+ lockingBean.getModuleName() +" "+lockingBean.getLockID()+" "+" has been deleted by Administrator.");
                    }
                }
                // Added by Shivakumar for bug fixing while releasing lock - END
                //Added by Ajay to remove lockID from HashTable
                //in client's side 06-09-2004 End 2
            }      
            // Coeus pesonalization rdias UCSD 08/31/2007
            FormAttrModel.getInstance().setPersonalizationXML(response.getPersnXMLObject());
        }catch(Exception e){
            e.printStackTrace();
            //exceptionDetailDialog = new ExceptionDetailDialog(mdiForm);
            // If the connection is not established then show the message to the client...
            // exceptionDetailDialog.setMainMessage(e.getMessage());
            String msg = "";
            //CoeusOptionPane.showErrorDialog(e.getMessage());
            StackTraceElement element[] = e.getStackTrace();
            //            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            int length = element.length;
            for(int index=0; index < length;index++){
                //exceptionDetailDialog.setDetailMessage(element[index].toString());
                msg = msg +"\n"+ element[index].toString();
            }
            //exceptionDetailDialog.display(e.getMessage(),msg);
            //return ;
        }finally {
            try{
                if (outputToServlet !=null){
                    outputToServlet.close();
                }
                if (inputToApplet !=null){
                    inputToApplet.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            if (mdiForm != null) {
                mdiForm.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                /* updated by ravi on 18-02-03 for ignoring mouse clicks
                   when the system is busy with some operation */
                mdiForm.getGlassPane().setVisible(false);
            }
            if(selectedFrame!=null){
                selectedFrame.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                /* updated by ravi on 18-02-03 for ignoring mouse clicks
                   when the system is busy with some operation */
                selectedFrame.getGlassPane().setVisible(false);
            }
            if(deskTopPane!=null){
                deskTopPane.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }
    
    /**
     * This Method is used to release the Lock associated specific row
     * of the DB during the Transaction(usually modify).
     * @param refId String represent the Reference ID/ Lock ID.
     * @param srvltComponent String represent the servletName/alias Name
     */
    public  void releaseUpdateLock(String refId,String srvltComponent){
        if (refId != null) {
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('Z');
            /*set the RolodexId as dataObject for the requester bean  */
            requester.setDataObject(refId);
            /*send the requesterBean to the servlet */
            setRequest(requester);
            setConnectTo(CoeusGuiConstants.CONNECTION_URL + srvltComponent);
            send();
        }
    }
}
