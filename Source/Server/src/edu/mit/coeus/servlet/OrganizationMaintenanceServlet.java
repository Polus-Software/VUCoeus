/*
 * @(#)OrganizationMaintenanceServlet.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;
 

/**
 * This servlet is a controller that retrieves data for 'Organization
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
public class OrganizationMaintenanceServlet extends CoeusBaseServlet {

    /**
     *  This method is used for applets.
     *  Post the information into server using object serialization.
     */
    public void service(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        //System.out("I am in the Organization servlet");
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();

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
            // Code added by Shivakumar for locking enhancement - BEGIN
            String unitNumber = userBean.getUnitNumber();
            // Code added by Shivakumar for locking enhancement - END

            OrganizationMaintenanceDataTxnBean orgMaintTxnBean = new OrganizationMaintenanceDataTxnBean();
            RolodexMaintenanceDataTxnBean rolodexTxnBean = new RolodexMaintenanceDataTxnBean();

            // keep all the beans into vector
            Vector dataObjects = new Vector();

            
            //update for Row Lock Update. Subramanya
            char functionType = requester.getFunctionType();
            if( functionType == 'C' ){
                // organization id auto generate - get parameter config value
                /* get organization id auto generate config */
                boolean generateOrganizationID = orgMaintTxnBean.getGenerateOrganizationParamValue();
                //System.out.println("servlet - organizationmaintenanceservlet - generate param " + generateOrganizationID);
                //responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                /* return sponsor generate flag */
                if(generateOrganizationID){
                    responder.setDataObject("TRUE");
                }else{
                    responder.setDataObject("FALSE");
                }
            }else if( functionType == 'Z' ){
                /*String refId = (requester.getDataObject()
                    == null?null:requester.getDataObject().toString().trim()); 
                releaseLock(refId);
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
                 */
                String rowId = requester.getDataObject().toString().trim();
                // Commented by Shivakumar for locking enhancement
//                orgMaintTxnBean.releaseEdit(rowId);
                // Code added by Shivakumar - BEGIN 1
//                orgMaintTxnBean.releaseEdit(rowId,loggedinUser);
                // Calling releaseLock method for bug fixing
                LockingBean lockingBean = orgMaintTxnBean.releaseLock(rowId,loggedinUser);
                responder.setLockingBean(lockingBean);
                // Code added by Shivakumar - END 1
                responder.setResponseStatus(true);
                responder.setDataObject("updateLock connection released");
            // JM 1-14-2015 restore getOrganization method but changed letter
            }
            else if( functionType == 'J' ){
                String orgId = requester.getId();
                OrganizationMaintenanceFormBean bean = orgMaintTxnBean.getOrganizationMaintenanceDetails(orgId);
                responder.setResponseStatus(true);
                responder.setDataObject(bean);
            // JM END
            }else{
                
            // boolean isModify = functionType == 'U' ? true : false;   
            // 0 get form data from the transaction class
            // Commented by Shivakumar for locking enhancement                
//            OrganizationMaintenanceFormBean formData = 
//                orgMaintTxnBean.getOrganizationMaintenanceDetails(
//                    requester.getId(),functionType);
            // Code added by Shivakumar - BEGIN
            // IF condition added for checking null condition    
                
                
           // if(requester.getId() != null){
                boolean success = true;
                if(functionType == 'U'){
                    LockingBean lockingBean =  orgMaintTxnBean.getLockingBean(requester.getId(),loggedinUser,unitNumber);
                    success = lockingBean.isGotLock();
                }
                if(success){
                    try{
                        OrganizationMaintenanceFormBean formData = 
                            orgMaintTxnBean.getOrganizationMaintenanceDetails(
                                requester.getId(),functionType,loggedinUser,unitNumber);
                        if (formData == null) {
                            formData = new OrganizationMaintenanceFormBean();
                        }
                        dataObjects.addElement(formData);                   
                        orgMaintTxnBean.transactionCommit();
                // Code added by Shivakumar - END
                //System.out("form data=>"+formData);
                //System.out("function type=>"+functionType);
                // Commented by Shivakumar as this part has been included 
                // in try/catch block in line 129,130.
    //            if (formData == null) {
    //                formData = new OrganizationMaintenanceFormBean();
    //            }
    //            dataObjects.addElement(formData);
                // 1 get the organization list
                    OrganizationListBean[] orgLists = orgMaintTxnBean.getOrganizationList();
                    dataObjects.addElement(orgLists);
                    // 2 get the selected organization list
                    orgLists = orgMaintTxnBean.getSelectedOrganizationList(requester.getId());
                    dataObjects.addElement(orgLists);
                    // 3 get the question list
                    String questionType = "O";
                    QuestionListBean[] questionsList = orgMaintTxnBean.getQuestionList(questionType);
                    dataObjects.addElement(questionsList);
                    // 4 get organization question list
                    OrganizationYNQBean[] orgQuestionList = orgMaintTxnBean.getOrganizationYNQ(requester.getId());
                    if (orgQuestionList == null) {
                        orgQuestionList = new OrganizationYNQBean[questionsList.length];
                        for (int i = 0; i < questionsList.length; i++) {
                            orgQuestionList[i] = new OrganizationYNQBean();
                            orgQuestionList[i].setQuestionId(questionsList[i].getQuestionId());
                            orgQuestionList[i].setOrgId(requester.getId());
                            orgQuestionList[i].setAcType("I");
                            //orgQuestionList[i].setOrgId(requester.getId());
                            //setting default answer.
                            orgQuestionList[i].setAnswer("");
                        }
                        //orgQuestionList = new OrganizationYNQBean[questionsList.length];
                    }

                    dataObjects.addElement(orgQuestionList);
                    // 5 get the question explanation and review date
                    Hashtable expList = orgMaintTxnBean.getQuestionExplanationAll();
                    dataObjects.addElement(expList);
                    // 6 get the audit
                    OrganizationAuditBean[] auditList = orgMaintTxnBean.getOrganizationAudit(requester.getId());
                    dataObjects.addElement(auditList);
                    // 7 get the IDC List
                    OrganizationIDCBean[] idcList = orgMaintTxnBean.getOrganizationIDC(requester.getId());
                    dataObjects.addElement(idcList);
                    // 8 get idc rate type list to display in combo box on IDC tab
                    IDCRateTypesBean[] idcRateTypeList = (new CoeusFunctions()).idcRateTypes();
                    dataObjects.addElement(idcRateTypeList);
                    // 9 get organization address
                    //OrganizationAddressFormBean orgAddress = orgMaintTxnBean.getOrganizationAddress(requester.getId());
                    //dataObjects.addElement(orgAddress);
                    if (formData.getContactAddressId() != 0) {
                        String contactAddress = rolodexTxnBean.getRolodexName(new Integer(formData.getContactAddressId()).toString());
                        formData.setContactAddressName(contactAddress);
                    }
                    if (formData.getCognizantAuditor() != 0) {
                        String cognizantAuditor = rolodexTxnBean.getRolodexName(new Integer(formData.getCognizantAuditor()).toString());
                        formData.setCognizantAuditorName(cognizantAuditor);
                    }
                    if (formData.getOnrResidentRep() != 0) {
                        String onrResidentRep = rolodexTxnBean.getRolodexName(new Integer(formData.getOnrResidentRep()).toString());
                        formData.setOnrResidentRepName(onrResidentRep);
                    }
                    // set the responder object
                    // Committing the transaction


                    // Code added by Shivakumar - BEGIN 2
                    responder.setDataObjects(dataObjects);
                    responder.setResponseStatus(true);
                    responder.setMessage(null);
                }catch(DBException dbEx){
                    dbEx.printStackTrace();
                    orgMaintTxnBean.transactionRollback();
                    throw dbEx;
                 }finally{
                     orgMaintTxnBean.endConnection();
                 }    
                }
                // Code added by Shivakumar - END 2
                //}
            }
        }catch( LockingException lockEx ) {
               //lockEx.printStackTrace();
               LockingBean lockingBean1 = lockEx.getLockingBean();
               String errMsg = lockEx.getErrorMessage();        
               CoeusMessageResourcesBean coeusMessageResourcesBean
                    =new CoeusMessageResourcesBean();
                errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);            
                responder.setResponseStatus(false);            
                responder.setException(lockEx);
                responder.setMessage(errMsg);               
                UtilFactory.log( errMsg, lockEx, "OrganizationMaintenanceServlet",
                                                                "perform");
              }   
        catch( CoeusException coeusEx ) {
            
            int index=0;
            String errMsg;
            if(coeusEx.getErrorId()==999999){
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
                responder.setLocked(true);
            }else{
                errMsg = coeusEx.getMessage();
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "OrganizationMaintenanceServlet",
                                                                "perform");
            
        } catch( DBException dbEx ) {
            
            int index=0;
            String errMsg = dbEx.getUserMessage();
            if (dbEx.getErrorId() == 20102 ) {
                errMsg = "dbEngine_intlErr_exceptionCode.1028";
            }
            if (errMsg.equals("db_exceptionCode.1111")) {
                responder.setCloseRequired(true);
            }
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            
            responder.setResponseStatus(false);

            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx, "OrganizationMaintenanceServlet",
                                                                "perform");
            
        } catch (Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e, "OrganizationMaintenanceServlet",
                                                                "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "OrganizationMaintenanceServlet", "doPost");
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
                UtilFactory.log( ioe.getMessage(), ioe, "OrganizationMaintenanceServlet",
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
                doPost(request,response);
    }

}
