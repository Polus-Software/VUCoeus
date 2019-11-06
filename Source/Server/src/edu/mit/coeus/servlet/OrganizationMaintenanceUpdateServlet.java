/*
 * @(#)OrganizationMaintenanceServlet.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Vector;


/**
 * This servlet is a controller that performs update/insert of data for 'Organization
 * Maintenance' module. It receives the serialized object bean called 'Requester Bean'
 * from the applet and performs accordingly.
 * It connects the DBEngine and executes the stored procedures or queries via
 * 'OrganizationDataTxnBean'.
 *
 * @version :1.0 August 13, 2002, 11:45 AM
 * @author Guptha K
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class OrganizationMaintenanceUpdateServlet extends CoeusBaseServlet {

    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = null;

        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        String loggedinUser = "";
//        UtilFactory UtilFactory = new UtilFactory();
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            UserInfoBean userBean = (UserInfoBean)new 
                UserDetailsBean().getUserInfo(requester.getUserName());
            loggedinUser = userBean.getUserId();

            OrganizationMaintenanceDataTxnBean orgMaintTxnBean = new OrganizationMaintenanceDataTxnBean();

            // keep all the beans into vector
            char functionType = requester.getFunctionType();
            Vector receivedObjects = requester.getDataObjects();
            // organization data
            OrganizationMaintenanceFormBean orgMaintData = (OrganizationMaintenanceFormBean) receivedObjects.elementAt(0);
            // selected organization type list
            OrganizationListBean[] orgLists = (OrganizationListBean[]) receivedObjects.elementAt(1);
            // organization question list
            OrganizationYNQBean[] orgQuestionList = (OrganizationYNQBean[]) receivedObjects.elementAt(2);

            OrganizationAuditBean[] auditList = (OrganizationAuditBean[]) receivedObjects.elementAt(3);
            OrganizationIDCBean[] idcList = (OrganizationIDCBean[]) receivedObjects.elementAt(4);

            if(functionType == 'I') {
                /* if organization id is null - accepted form validation indicates that
                   organization id should be generated based on parameter config */
                if((orgMaintData.getOrganizationId() == null) || (orgMaintData.getOrganizationId().trim().length() ==0)){
                    String organizationID = orgMaintTxnBean.generateOrganizationId();
                    //System.out.println("***generated organization id in servlet " + organizationID);
                    orgMaintData.setOrganizationId(organizationID);
                }    
            }

            /*for (int i=0; i<orgQuestionList.length;i++){
                //System.out("question id = "+ orgQuestionList[i].getOrgId());
                //System.out("answer      = "+ orgQuestionList[i].getAnswer());
            }*/
            // organization question list
            /*
            OrganizationYNQBean[] orgQuestionList = (OrganizationYNQBean[]) receivedObjects.elementAt(2);
            // audit list
            OrganizationAuditBean[] auditList = (OrganizationAuditBean[]) receivedObjects.elementAt(3);
            // IDC list
            OrganizationIDCBean[] idcList = (OrganizationIDCBean[]) receivedObjects.elementAt(4);
            */
            if (functionType == 'U') {
                // set the user
                orgMaintData.setUpdateUser(loggedinUser);
            }
            // get the system time stamp
            Timestamp currentDateTime = orgMaintTxnBean.getTimestamp();
            boolean success = false;
            // Commented by Shivakumar for locking enhancement 
//            success = orgMaintTxnBean.addUpdateOrganization(orgMaintData, 
//                orgLists, orgQuestionList, auditList, idcList, loggedinUser,'U');
            // Code added by Shivakumar for locking enhancement - BEGIN
            LockingBean lockingBean = orgMaintTxnBean.addUpdateOrganization(orgMaintData, 
                orgLists, orgQuestionList, auditList, idcList, loggedinUser,functionType);
            success = lockingBean.isGotLock();
            // Code added by Shivakumar for locking enhancement - END
            // set the responder object
            responder = new ResponderBean();
            responder.setLockingBean(lockingBean);
            //responder.setDataObjects(dataObjects);

            // return organization data - required when we generate organization id
            responder.setDataObject(orgMaintData);
            responder.setResponseStatus(true);

            if (success) {
                responder.setMessage("Saving is successful");
            }
        } catch( CoeusException coeusEx ) {
            
            int index=0;
            String errMsg;
            
            errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "OrganizationMaintenanceUpdateServlet",
                                                                "perform");
            
        } catch( DBException dbEx ) {
            
            int index=0;
            String errMsg;
            
            errMsg = dbEx.getUserMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "OrganizationMaintenanceUpdateServlet",
                                                                "perform");
            
        } catch (Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "OrganizationMaintenanceUpdateServlet",
                                                                "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "OrganizationMaintenanceUpdateServlet", "doPost");
        //Case 3193 - END
        } finally {
            try {
                // send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
                outputToApplet.writeObject(responder);
                // close the streams
                if (inputFromApplet != null) {
                    inputFromApplet.close();
                }
                if (outputToApplet != null) {
                    outputToApplet.flush();
                    outputToApplet.close();
                }
            } catch (IOException ioe) {
                UtilFactory.log( ioe.getMessage(), ioe, "OrganizationMaintenanceUpdateServlet",
                                                                "perform");
            }
        }
    }

    /**
     *  This method is used for applets.
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
    }
}
