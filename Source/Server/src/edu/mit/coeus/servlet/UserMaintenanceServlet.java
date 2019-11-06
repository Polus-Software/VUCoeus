/*
 * @(#)UserMaintenanceServlet.java 1.0 June 30, 2003, 5:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 09-AUG-2007
 * by Leena
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.user.bean.UserMaintUpdateTxnBean;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.*;
//import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.user.UserMaintenanceFactory;
import edu.mit.coeus.user.UserMaintenanceRequestHandler;
import edu.mit.coeus.user.bean.DataPositions;
import org.okip.service.shared.api.Exception;
//import edu.mit.coeus.utils.dbengine.DBException;
//import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
//import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author  senthilar
 */
public class UserMaintenanceServlet extends CoeusBaseServlet implements TypeConstants, DataPositions{
//    private String loggedInUser = null;
//    private String unitNumber = null;
//    private String command = null;
//    private String module = null;
//    private String selectedPersonID = null;
//    private char functionType = '\0';
//    private UtilFactory UtilFactory = new UtilFactory();
//    private Vector processedData = null;
    
    //Added for Coeus 4.3 PT ID 2232 - Custom Roles - start
    private static final char GET_ROLE_DETAILS = 'B';
    private static final char GET_NEXT_ROLE_ID = 'C';
    private static final char SAVE_ROLE_DETAILS = 'D';
    private static final char RIGHT_CHECK = 'E';
    // Added for COEUSQA-1692_User Access - Maintenance_start
    private static final char CHECK_USER_HAS_RIGHT_IN_HOME_UNIT = 'F';
    private static final char GET_ALL_ROLES_FOR_USER = 'G';
    private static final char CHECK_USER_HAS_RIGHT = 'H';
    private static final char DELETE_USER_ROLES = 'I';
    private static final char CHECK_IS_VALID_UNIT = 'J';
    // Added for COEUSQA-1692_User Access - Maintenance_end
    //Added for Coeus 4.3 PT ID 2232 - Custom Roles - end
    //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window  - Start
    private static final String PROPOSAL_MODULE_CODE = "3";
    private static final char CHECK_CREDIT_SPLIT_FOR_INVESTIGATOR = 'L';
    private static final char CHECK_CREDIT_SPLIT_FOR_INV_UNIT = 'U';
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_ITEM_KEY_INDEX = 1;
    private static final int MODULE_ITEM_KEY_SEQUENCE_INDEX = 2;
    private static final int PERSON_ID_INDEX = 3;
    private static final int ADDS_TO_HUNDRED_INDEX = 4;
    private static final int UNIT_NUMBER_INDEX = 5;
    //COEUSQA-2037 : END
    
    //Added for COEUSQA-2429 : CLONE -User preferences not getting saved - Start
    private static final char USER_PREFERENCES = 'Z';
    //COEUSQA-2429 : End
    
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        // the request object from applet
        RequesterBean requester = null;
        // the response object to applet
        ResponderBean responder = new ResponderBean();
        
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
//        UserInfoBean userBean;
        UserMaintenanceFactory factory = new UserMaintenanceFactory();
        UserMaintenanceRequestHandler handler = null;
        Vector dataObjects = null;
        Vector data = null;
        //Added for Case#4585 - Protocol opened from list window is not the correct one - Start 
        char functionType = '\0';
        //Case#4585 - End
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream( request.getInputStream() );
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // the response object to applet
            responder = new ResponderBean(); //moved on to Handler class
            // get the user
            String loggedInUser = requester.getUserName();
            
            functionType = requester.getFunctionType();
            
            if(functionType == 'A'){
                String userId = (String) requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                String userName = userMaintDataTxnBean.getUserName(userId);
                responder.setDataObject(userName);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for Coeus 4.3 : PT ID 2232 - Custom Roles - start
            else if(functionType == GET_NEXT_ROLE_ID){
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                int roleId = userMaintDataTxnBean.getNextRoleId();
                responder.setDataObject(new Integer(roleId));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == GET_ROLE_DETAILS){
                String roleId = (String)requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                RoleTxnBean roleTxnBean = new RoleTxnBean();
                Vector vecServerDataObjects = new Vector();
                
                Vector vecAllRights = userMaintDataTxnBean.getAllRights();
                vecServerDataObjects.add(0, vecAllRights);
                
                Vector vecRoleRights = roleTxnBean.getProposalRightRole(Integer.parseInt(roleId));
                vecServerDataObjects.add(1, vecRoleRights);
                
                RoleInfoBean roleInfoBean = userMaintDataTxnBean.getRole(roleId);
                vecServerDataObjects.add(2, roleInfoBean);
                
                responder.setDataObjects(vecServerDataObjects);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == SAVE_ROLE_DETAILS){
                Vector vecDataObjects = (Vector)requester.getDataObjects();
                RoleInfoBean roleInfoBean = (RoleInfoBean)vecDataObjects.get(0);
                Vector vecRoleRights = (Vector)vecDataObjects.get(1);
                
                UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(loggedInUser);
                boolean success = userMaintUpdateTxnBean.addUpdRoleDetails(roleInfoBean, vecRoleRights);
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }else if(functionType == RIGHT_CHECK){
                Vector vecDataObjects = (Vector)requester.getDataObjects();
                String unitNumber = (String)vecDataObjects.get(0);
                String unitName = (String)vecDataObjects.get(1);
                String right = (String)vecDataObjects.get(2);
                UserMaintDataTxnBean dataTxnBean = new UserMaintDataTxnBean();
                boolean userHasRight = dataTxnBean.getUserHasRight(loggedInUser, right, unitNumber);
                responder.setDataObject(new Boolean(userHasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
            }
            //Added for Coeus 4.3 : PT ID 2232 - Custom Roles - end
            //Added for COEUSQA-2037 : Software allows you to delete an investigator who is assigned credit in the credit split window
             else if(functionType == CHECK_CREDIT_SPLIT_FOR_INVESTIGATOR){
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                Vector cvInvModuleData= requester.getDataObjects();
                boolean hasCreditSplit = false;
                String moduleCode = (String)cvInvModuleData.get(MODULE_CODE_INDEX);
                String moduleItemKey = (String)cvInvModuleData.get(MODULE_ITEM_KEY_INDEX);
                int moduleItemKeySequence = ((Integer)cvInvModuleData.get(MODULE_ITEM_KEY_SEQUENCE_INDEX)).intValue();
                String personId = (String)cvInvModuleData.get(PERSON_ID_INDEX);
                String addsToHundred = (String)cvInvModuleData.get(ADDS_TO_HUNDRED_INDEX);
                hasCreditSplit = userMaintDataTxnBean.isInvHasCreditSplit(moduleCode,moduleItemKey,moduleItemKeySequence,personId,addsToHundred);
                responder.setDataObject(new Boolean(hasCreditSplit));
                responder.setResponseStatus(true);
                responder.setMessage(null);
             }else if(functionType == CHECK_CREDIT_SPLIT_FOR_INV_UNIT){
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                Vector cvInvUnitData= requester.getDataObjects();
                boolean hasCreditSplit = false;
                String moduleCode = (String)cvInvUnitData.get(MODULE_CODE_INDEX);
                String moduleItemKey = (String)cvInvUnitData.get(MODULE_ITEM_KEY_INDEX);
                int moduleItemKeySequence = ((Integer)cvInvUnitData.get(MODULE_ITEM_KEY_SEQUENCE_INDEX)).intValue();
                String personId = (String)cvInvUnitData.get(PERSON_ID_INDEX);
                String addsToHundred = (String)cvInvUnitData.get(ADDS_TO_HUNDRED_INDEX);
                String unitNumber = (String)cvInvUnitData.get(UNIT_NUMBER_INDEX);
                hasCreditSplit = userMaintDataTxnBean.isInvUnitHasCreditSplit(moduleCode,moduleItemKey,moduleItemKeySequence,personId,unitNumber,addsToHundred);
                responder.setDataObject(new Boolean(hasCreditSplit));
                responder.setResponseStatus(true);
                responder.setMessage(null);
             }
            //COEUSQA-2037 : End
            // Added for COEUSQA-1692_User Access - Maintenance_start
             else if(functionType == CHECK_USER_HAS_RIGHT_IN_HOME_UNIT){
                Vector vecRequestObjects = (Vector)requester.getDataObjects();
                String logInUser = (String)vecRequestObjects.get(0);
                UserInfoBean userInfoBean = (UserInfoBean)vecRequestObjects.get(1);
                // from userInfoBean get the user id and get the person info bean to get the home unit
                UserDetailsBean  userDetailsBean = new UserDetailsBean();
                //Gets the person detail from OSP$PERSON table
                PersonInfoBean personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getUserId());                
                ProposalDevelopmentTxnBean dataTxnBean = new ProposalDevelopmentTxnBean();
                boolean userHasRight = dataTxnBean.isUserHasRight(logInUser,personInfoBean.getHomeUnit(),"MAINTAIN_USER_ROLES");
                responder.setDataObject(new Boolean(userHasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
             }else if(functionType == GET_ALL_ROLES_FOR_USER){
                String userId = (String)requester.getDataObject();
                Vector vecRoles = new Vector();
                Vector vecResponse = new Vector();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                Vector vecUnits = (Vector)userMaintDataTxnBean.getAllUnitsForUser(userId);
                if( vecUnits != null && vecUnits.size() >0){
                    for(int index = 0; index < vecUnits.size();index++){
                        UnitUserRolesMaintenanceFormBean unitUserRolesMaintenanceFormBean =
                                (UnitUserRolesMaintenanceFormBean)vecUnits.get(index);
                        vecRoles = userMaintDataTxnBean.getUnitUserRoles(userId,unitUserRolesMaintenanceFormBean.getUnitNumber());
                        unitUserRolesMaintenanceFormBean.setVecRoles(vecRoles);                        
                    }                    
                    vecResponse.addAll(vecUnits);
                    responder.setDataObjects(vecResponse);
                }
                responder.setResponseStatus(true);
                responder.setMessage(null);                
             }else if(functionType == CHECK_USER_HAS_RIGHT){
                Vector vecRequestObjects = (Vector)requester.getDataObjects();
                String logInUser = (String)vecRequestObjects.get(0);
                String unitCode = (String)vecRequestObjects.get(1);
                String right = (String)vecRequestObjects.get(2);
                ProposalDevelopmentTxnBean dataTxnBean = new ProposalDevelopmentTxnBean();
                boolean userHasRight = dataTxnBean.isUserHasRight(logInUser,unitCode,right);
                responder.setDataObject(new Boolean(userHasRight));
                responder.setResponseStatus(true);
                responder.setMessage(null);
             }else if(functionType == DELETE_USER_ROLES){
                Vector vecRequestObject = (Vector)requester.getDataObject();                
                UserMaintUpdateTxnBean userMaintUpdateTxnBean = new UserMaintUpdateTxnBean(loggedInUser);
                boolean isSuccess = userMaintUpdateTxnBean.deleteUserRoleData(vecRequestObject);
                if(isSuccess){
                    responder.setResponseStatus(true);
                }else{
                    responder.setResponseStatus(false);
                }
                responder.setMessage(null);
             }else if(functionType == CHECK_IS_VALID_UNIT){
                String unitNumber = (String)requester.getDataObject();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean validUnit = userMaintDataTxnBean.checkUnitIsValid(unitNumber);
                responder.setDataObject(new Boolean(validUnit));
                responder.setResponseStatus(true);
                responder.setMessage(null);
             }
            
            // Added for COEUSQA-1692_User Access - Maintenance_end
            //Added for COEUSQA-2429 : CLONE -User preferences not getting saved
             else if(functionType == USER_PREFERENCES){
                // keep all the beans into vector
                dataObjects = requester.getDataObjects();
                if (dataObjects == null) {
                    throw new Exception("Vector dataObjects is Null");
                }
                
                // Read in all the values that are needed to process the data
                Hashtable hashTable = (Hashtable)dataObjects.get(0);
                String unitNumber = (String)hashTable.get(UNIT_NUMBER);
                String command = (String)hashTable.get(COMMAND);
                String module = (String)hashTable.get(MODULE);
                String selectedPersonID = (String)hashTable.get(PERSON_ID);
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start 
                //char functionType = ((Character)hashTable.get(FUNCTION)).charValue();
                functionType = ((Character)hashTable.get(FUNCTION)).charValue();
                //Case#4585 - End
                data = (Vector)hashTable.get(DATA);
                
                // Now set the values read in the parent class so that it can be used
                // by any subclasses
                handler = factory.create(module,command);
                handler.setDataObjects(dataObjects);
                handler.setHttpServletResponse(response);
                handler.setUnitNumber(unitNumber);
                handler.setCommand(command);
                handler.setSelectedPersonID(selectedPersonID);
                handler.setFucntionType(functionType);
                handler.setData(data);
                handler.setUserID(requester.getUserName());
                handler.doPerform();
                //COEUSQA-2429 :End
             }else{
                // keep all the beans into vector
                dataObjects = requester.getDataObjects();
                if (dataObjects == null) {
                    throw new Exception("Vector dataObjects is Null");
                }
                
                // Read in all the values that are needed to process the data
                Hashtable hashTable = (Hashtable)dataObjects.get(0);
                String unitNumber = (String)hashTable.get(UNIT_NUMBER);
                String command = (String)hashTable.get(COMMAND);
                String module = (String)hashTable.get(MODULE);
                String selectedPersonID = (String)hashTable.get(PERSON_ID);
                //Modified for Case#4585 - Protocol opened from list window is not the correct one - Start
                //char functionType = ((Character)hashTable.get(FUNCTION)).charValue();
                functionType = ((Character)hashTable.get(FUNCTION)).charValue();
                //Case#4585 - End
                data = (Vector)hashTable.get(DATA);
                
                // Now set the values read in the parent class so that it can be used
                // by any subclasses
                handler = factory.create(module,command);
                handler.setDataObjects(dataObjects);
                handler.setHttpServletResponse(response);
                handler.setUnitNumber(unitNumber);
                handler.setCommand(command);
                handler.setSelectedPersonID(selectedPersonID);
                handler.setFucntionType(functionType);
                handler.setData(data);
                handler.setUserID(requester.getUserName());
                handler.doPerform();
             }
           
        } catch (InstantiationException ie){
            ie.printStackTrace();
//            System.out.println("Instantiation Exception while instantiating the subservlet");
        } catch(IllegalAccessException iae){
            iae.printStackTrace();
//            System.out.println("IllegalAccess Exception while instantiating the subservlet");
        } catch( CoeusException coeusEx ) {
//            int index=0;
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
            
            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "UserMaintenanceServlet",
                    "perform");
            
        } catch( DBException dbEx ) {
            
//            int index=0;
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
            
            //print the error message at client side
            responder.setException(dbEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, dbEx,
                    "UserMaintenanceServlet", "perform");
            
        } catch(NullPointerException npe){
            //npe.printStackTrace();
//            System.out.println("NullPointerException raised while trying read the input values");
            UtilFactory.log( npe.getMessage(), npe, "UserMaintenanceServlet", "perform");
        } catch(ClassCastException cce){
            //cce.printStackTrace();
//            System.out.println("ClassCastException. Check the COMMAND and MODULE passed.");
            UtilFactory.log( cce.getMessage(), cce, "UserMaintenanceServlet", "perform");
            return;
        } catch(org.okip.service.shared.api.Exception ee){
            //ee.printStackTrace();
//            System.out.println("org.okip.service.shared.api.Exception is caught while processing the request");
            UtilFactory.log( ee.getMessage(), ee, "UserMaintenanceServlet", "perform");
        } catch(java.lang.Exception e) {
            //e.printStackTrace();
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,
                    "UserMaintenanceServlet", "perform");
            
        //Donot send the response to Client as this is done in Handler it self.
        //Case 3193 - START
        }catch(Throwable throwable){
            java.lang.Exception ex = new java.lang.Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "UserMaintenanceServlet", "doPost");
        //Case 3193 - END
        }finally {
            try{
                // send the object to applet
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
            }catch (IOException ioe){
                UtilFactory.log( ioe.getMessage(), ioe,
                        "UserMaintenanceServlet", "perform");
            }
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
    }
}
