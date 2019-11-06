/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * PersonMassChangeServlet.java
 * @author bijosht
 * Created on January 24, 2005, 5:05 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.centraladmin.bean.CentralAdminTxnBean;
import edu.mit.coeus.centraladmin.bean.MassChangeDataBean;
import edu.mit.coeus.centraladmin.bean.ModuleBean;
import edu.mit.coeus.centraladmin.bean.PersonTypeBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.utils.locking.LockingException;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import edu.mit.coeus.utils.query.Equals;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PersonMassChangeServlet extends CoeusBaseServlet implements TypeConstants{
    
    private static final char GET_MODULE_DETAILS = 'A';
    private static final char GET_SHOWLIST = 'B';
    private static final char UPDATE_RECORDS = 'C';
    private static final char CHECK_LOCKS = 'D';
    
    /**
     * This method handles all the POST requests from the Client
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if any ServletException
     * @throws IOException if any IOException
     */
    public void doPost(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        
        String loggedinUser ="";
        String unitNumber = "";
        try {
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            loggedinUser = requester.getUserName();
            
            // get the user
            UserInfoBean userBean = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            unitNumber = userBean.getUnitNumber();
            
            // keep all the beans into vector
            Vector dataObjects = new Vector();
            
            char functionType = requester.getFunctionType();
            CentralAdminTxnBean centralAdminTxnBean = new CentralAdminTxnBean();
            if (functionType== GET_MODULE_DETAILS) {
                Hashtable massChangeData = null;
                massChangeData = centralAdminTxnBean.getMassChangeData();
                responder.setDataObject(massChangeData);
                responder.setResponseStatus(true);
            }else if (functionType == GET_SHOWLIST ) {
                Hashtable htData =(Hashtable)requester.getDataObject();
                String personId = htData.get("PERSON_ID").toString();
                String nonMitPersonFlag = htData.get("EMPLOYEE").toString();
                CoeusVector cvModulesSelected = (CoeusVector) htData.get(ModuleBean.class);
                CoeusVector cvPersonTypeSelected = (CoeusVector) htData.get(PersonTypeBean.class);
                CoeusVector cvAllSeqData= null;
                CoeusVector cvCurrSeqData = null;
                CoeusVector cvMain = new CoeusVector();
                String prevModuleId = null;
                String strModuleId = null;
                Hashtable htShowListData = new Hashtable();
                CoeusVector cvRmvPersons = new CoeusVector();
                //(CoeusVector)htData.get("RMV_PERSONS");
                for (int index=0;index<cvPersonTypeSelected.size();index++) {
                    PersonTypeBean personTypeBean = (PersonTypeBean) cvPersonTypeSelected.get(index);
                    strModuleId = personTypeBean.getModuleId();
                    if (prevModuleId == null) {
                        prevModuleId = strModuleId;
                    }
                    String strPersonTypeId = personTypeBean.getTypeId();
                    if (!strModuleId.equals(prevModuleId)) {
                        //hashtable add
                        if (cvMain.size()<=0) {
                            cvRmvPersons.add(prevModuleId);
                        }
                        
                        cvMain.sort("identificationNo");
                        htShowListData.put(prevModuleId,cvMain);
                        cvMain = new CoeusVector();
                    }
                    cvAllSeqData = centralAdminTxnBean.getShowListData(personId,nonMitPersonFlag,strModuleId,strPersonTypeId,"ALL");
                    cvCurrSeqData = centralAdminTxnBean.getShowListData(personId,nonMitPersonFlag,strModuleId,strPersonTypeId,"CURRENT");
                    
                    MassChangeDataBean massChangeDataBean= null;
                     for (int indexCurr=0; indexCurr<cvCurrSeqData.size();indexCurr++) {
                         massChangeDataBean =(MassChangeDataBean)cvCurrSeqData.get(indexCurr);
                         massChangeDataBean.setIsCurrentSeq(true);
                         Equals eqIdentification = new Equals("identificationNo",massChangeDataBean.getIdentificationNo());
                         CoeusVector cvMainDataFiltered = cvMain.filter(eqIdentification);
                         if (cvMainDataFiltered.size() <=0) {
                              cvMain.add(massChangeDataBean);
                         }
                     }
                    
                    MassChangeDataBean massChangeDataBeanAll=null;
                    for (int indexAll=0; indexAll<cvAllSeqData.size();indexAll++) {
                        massChangeDataBeanAll =(MassChangeDataBean)cvAllSeqData.get(indexAll);
                        Equals eqIdentification = new Equals("identificationNo",massChangeDataBeanAll.getIdentificationNo());
                        CoeusVector cvCueSeqFiltered = cvCurrSeqData.filter(eqIdentification);
                        CoeusVector cvMainDataFiltered = cvMain.filter(eqIdentification);
                        if (cvCueSeqFiltered.size()<=0 && cvMainDataFiltered.size() <=0) {
                            massChangeDataBeanAll.setIsCurrentSeq(false);
                            cvMain.add(massChangeDataBeanAll);
                        }
                    }
                   prevModuleId = strModuleId;
                } //Outer for loop ends
                if (cvMain.size()<=0) {
                    cvMain.sort("identificationNo");
                    cvRmvPersons.add(prevModuleId);
                }
                htShowListData.put(prevModuleId,cvMain);
                htShowListData.put("RMV_PERSONS",cvRmvPersons);
                responder.setDataObject(htShowListData);
                responder.setResponseStatus(true);
            } else if (functionType == UPDATE_RECORDS) {
                Hashtable htUpdateData =(Hashtable)requester.getDataObject();
                boolean isLockCheckRequired = ((Boolean)htUpdateData.get("LOCK_CHECK")).booleanValue();
                boolean isMarkSapFeedRequired = ((Boolean)htUpdateData.get("MARK_SAP_FEED")).booleanValue();
                CoeusVector cvLockIds = new CoeusVector();
                if (isLockCheckRequired) {
                    LockingTxnBean lockingTxnBean = new LockingTxnBean();
                    cvLockIds = lockingTxnBean.getAllLockIds();
                    /*String msg = "";
                    if(((Boolean)cvLockIds.get(cvLockIds.size()-1)).booleanValue()) {
                        CoeusMessageResourcesBean coeusMessageResourcesBean
                        = new CoeusMessageResourcesBean();
                        msg= coeusMessageResourcesBean.parseMessageKey("locking_exceptionCode.1020");
                    }*/
                    responder.setResponseStatus(true);
                    if (cvLockIds.size()>1) {
                        responder.setCloseRequired(true);
                        //responder.setMessage("LOCKS_EXIST");
                    } else {
                        responder.setCloseRequired(false);
                        //responder.setMessage("NO_LOCKS");
                    }
                } else {
                    String personId = htUpdateData.get("PERSON_ID_NEW").toString();
                    String personIdOld = htUpdateData.get("PERSON_ID").toString();
                    String nonMitPersonFlag = htUpdateData.get("EMPLOYEE").toString();
                    String nonMitPersonFlagNew = htUpdateData.get("EMPLOYEE_NEW").toString();
                    String personNameNew = htUpdateData.get("PERSON_NAME_NEW").toString();
                    boolean isAllsequence = ((Boolean)htUpdateData.get("ALL_SEQUENCE")).booleanValue();
                    CoeusVector cvModulesSelected = (CoeusVector) htUpdateData.get(ModuleBean.class);
                    CoeusVector cvPersonTypeSelected = (CoeusVector) htUpdateData.get(PersonTypeBean.class);
                    centralAdminTxnBean.updateMassChangeDetails(personId, personNameNew,nonMitPersonFlag,nonMitPersonFlagNew,isAllsequence,personIdOld,cvPersonTypeSelected,isMarkSapFeedRequired);
                    responder.setResponseStatus(true);
                }
            }         
    } catch( LockingException lockEx ) {
        //lockEx.printStackTrace();
        LockingBean lockingBean = lockEx.getLockingBean();
        String errMsg = lockEx.getErrorMessage();
        CoeusMessageResourcesBean coeusMessageResourcesBean
        =new CoeusMessageResourcesBean();
        errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
        responder.setException(lockEx);
        responder.setResponseStatus(false);
        responder.setMessage(errMsg);
        UtilFactory.log( errMsg, lockEx,
        "PersonMassChangeServlet", "doPost");
    } catch( CoeusException coeusEx ) {
        //coeusEx.printStackTrace();
        int index=0;
        String errMsg;
        if(coeusEx.getErrorId()==999999){
            errMsg = "dbEngine_intlErr_exceptionCode.1028";
            responder.setLocked(true);
        }else{
            errMsg = coeusEx.getMessage();
        }
        CoeusMessageResourcesBean coeusMessageResourcesBean
        =new CoeusMessageResourcesBean();
        errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
        
        responder.setResponseStatus(false);
        responder.setException(coeusEx);
        responder.setMessage(errMsg);
        UtilFactory.log( errMsg, coeusEx,
        "PersonMassChangeServlet", "doPost");
        
    } catch( DBException dbEx ) {
        //dbEx.printStackTrace();
        int index=0;
        String errMsg = dbEx.getUserMessage();
        if (dbEx.getErrorId() == 20102 ) {
            errMsg = "dbEngine_intlErr_exceptionCode.1028";
        }
        if (errMsg.equals("db_exceptionCode.1111")) {
            responder.setCloseRequired(true);
        }
        CoeusMessageResourcesBean coeusMessageResourcesBean
        = new CoeusMessageResourcesBean();
        errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
        
        responder.setResponseStatus(false);
        responder.setException(dbEx);
        responder.setMessage(errMsg);
        UtilFactory.log( errMsg, dbEx,
        "PersonMassChangeServlet", "doPost");
        
    } catch(Exception e) {
        //e.printStackTrace();
        responder.setResponseStatus(false);
        responder.setException(e);
        responder.setMessage(e.getMessage());
        UtilFactory.log( e.getMessage(), e,
        "PersonMassChangeServlet", "doPost");
    //Case 3193 - START
    }catch(Throwable throwable){
        Exception ex = new Exception(throwable);
        responder.setException(ex);
        responder.setResponseStatus(false);
        responder.setMessage(ex.getMessage());
        UtilFactory.log( throwable.getMessage(), throwable, "PersonMassChangeServlet", "doPost");
    //Case 3193 - END
    } finally {
        try{
            
            outputToApplet
            = new ObjectOutputStream(response.getOutputStream());
            outputToApplet.writeObject(responder);
            // close the streams
            if (inputFromApplet!=null){
                inputFromApplet.close();
            }
            if (outputToApplet!=null){
                outputToApplet.flush();
                outputToApplet.close();
            }
        } catch (IOException ioe){
            UtilFactory.log( ioe.getMessage(), ioe,
            "PersonMassChangeServlet", "doPost");
        }
    }
    
}

/**
 * @param args the command line arguments
 */
public static void main(String[] args) {
}

}
