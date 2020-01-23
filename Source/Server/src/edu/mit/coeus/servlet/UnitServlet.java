/*
 * @(#)UnitServlet.java  August 28, 2002, 12:43 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.Hashtable;

import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.unit.bean.UnitHierarchyFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
//Added for unit hierarchy enhancement start 1 by tarique
import edu.mit.coeus.unit.bean.UnitAdministratorBean;
//Added for unit hierarchy enhancement end 1 by tarique
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.unit.bean.UnitUpdateTxnBean;
import edu.mit.coeus.user.bean.UserDelegationsBean;
//Added for unit hierarchy enhancement start 2 by tarique
import edu.mit.coeus.utils.ComboBoxBean;
import java.util.HashMap;
import edu.mit.coeus.utils.query.Equals;
 //coeusqa-1563  start 
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
 //coeusqa-1563  end
//Added for unit hierarchy enhancement end 2 by tarique

/**
 * The servlet used to service all the requests for the unit module in the
 * coeus web application. It will accept the request from applet and service the
 * request by using the UnitTxnBean and send the response to the applet back.
 * For the Applet-Communication channel, it use http tunneling.
 * @author  geo
 * @version 1.0 Created on August 28, 2002, 12:43 PM
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
//public class UnitServlet extends HttpServlet {
public class UnitServlet extends CoeusBaseServlet {
    //Rights
    private static final String MAINTAIN_UNIT_LA_RATES = "MAINTAIN_UNIT_LA_RATES";
    private static final String MODIFY_DELEGATIONS = "MODIFY_DELEGATIONS";
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    private static final char GET_UNIT_FORMULATED_COST = 'Z';
    private static final char UPDATE_UNIT_FORMULATED_COST = 'z';
    private static final String GET_INSERT_UPDATE_FORM_COST_BEAN = "GET_INSERT_UPDATE_BEAN";
    private static final String GET_DELETE_FORM_COST_BEAN = "GET_DELETE_BEAN";
   // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
            
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods. It will extract the <code>RequesterBean</code> instance from the
     * request object and process the request accoring to the request type.
     * Perform all the database transactions by using UnitTxnBean and send the
     * resut back to Applet as a form of <code>Response</code> object.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, java.io.IOException {
        //        UtilFactory UtilFactory = new UtilFactory();
        RequesterBean requester = null;
        ResponderBean responder = new ResponderBean();
        // open object input/output streams
        ObjectInputStream inputFromApplet = null;
        ObjectOutputStream outputToApplet = null;
        try {
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            // get the user
            UserInfoBean userInfo = (UserInfoBean)new
            UserDetailsBean().getUserInfo(requester.getUserName());
            
            if(userInfo==null)
                throw new CoeusException(
                "sess_exceptionCode.1003");
            String userId = userInfo.getUserId();
            String loggedInUnitNumber = userInfo.getUnitNumber();
            //System.out.println("userid=>"+userId);
            
            char functionType = requester.getFunctionType();
            String unitNumber = requester.getId();
            
            UnitDetailFormBean unitDetailRes = new UnitDetailFormBean();
            UnitDataTxnBean unitTxn = new UnitDataTxnBean(userId);
            Vector dataObjects = new Vector(3,2);
            switch(functionType){
                case('G'):
                    unitDetailRes = unitTxn.getUnitDetails(unitNumber);
                    responder.setDataObject(unitDetailRes);
                    break;
                case('U'):
                    Hashtable addUpdateNodes = (Hashtable)requester.getDataObject();
                    unitTxn.addUpdDelUnitDetails(addUpdateNodes);
                    //Added for unit hierarchy enhancement start 4 by tarique
                    // Update the Admin Type data
                    UnitUpdateTxnBean unitUpdateTxnBean = null;
                    CoeusVector clientData = (CoeusVector)requester.getDataObjects();
                    unitUpdateTxnBean = new UnitUpdateTxnBean(userId);
                    if(clientData != null && clientData.size()>0){
                        //Modified for COEUSDEV-70 : Array index out of range error - Unit Administrators  - Start
                        //Updating the database based on the acType('D' - First, 'U' - Second and 'I')
//                        for(int index=0;index<clientData.size();index++){
//                            UnitAdministratorBean unitAdministratorBean = (UnitAdministratorBean)clientData.get(index);
//                            
//                            if(unitAdministratorBean!= null){
//                                if(unitAdministratorBean.getAcType()==null ||
//                                unitAdministratorBean.getAcType().equals("")){
//                                    continue;
//                                }else{
//                                    unitUpdateTxnBean.addUpdDeleteUnitAdminType(unitAdministratorBean);
//                                }
//                            }
//                        }
                        CoeusVector cvFilteredData = new CoeusVector();
                        Equals deleteAcType = new Equals("acType", "D");
                        cvFilteredData.addAll(clientData.filter(deleteAcType));
                        Equals updateAcType = new Equals("acType", "U");
                        cvFilteredData.addAll(clientData.filter(updateAcType));
                        Equals insertAcType = new Equals("acType", "I");
                        cvFilteredData.addAll(clientData.filter(insertAcType));
                        if(cvFilteredData != null && cvFilteredData.size() > 0){
                            for(int index=0;index<cvFilteredData.size();index++){
                                UnitAdministratorBean unitAdministratorBean = (UnitAdministratorBean)cvFilteredData.get(index);
                                if(unitAdministratorBean!= null){
                                    unitUpdateTxnBean.addUpdDeleteUnitAdminType(unitAdministratorBean);
                                }
                            }
                        }

                    }
                    //Added for unit hierarchy enhancement end 4 by tarique
                    
                    break;
                case('A'):
                    if(functionType=='I'){
                        unitTxn.checkDuplicateUnitNumber(unitNumber);
                    }
                    //Bug Fix:Since the validation for Unique Name is done in the
                    //Form on focus lost.Commeted this code Start
                    /*UnitDetailFormBean unitDetail =
                            (UnitDetailFormBean)requester.getDataObject();
                    unitDetailRes = unitTxn.validateUnitData(unitDetail);
                    responder.setDataObject(unitDetailRes);*/
                    break;
                    //Bug Fix: End
                case('H'):
                    Vector unitHierarchyNodes = unitTxn.getUnitHierarchyDetails();
                    responder.setDataObject(unitHierarchyNodes);
                    break;
                /* JM 7-23-2015 custom hierarchy getter */
                case('V'):
                    Vector customUnitHierarchyNodes = unitTxn.getCustomUnitHierarchyDetails();
                    responder.setDataObject(customUnitHierarchyNodes);
                    break;
                /* JM END */
                case('T'):
                    break;
                case('B'):
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    
                    unitNumber = (String)requester.getDataObject();
                    CoeusVector rateClass = unitTxn.getRateClassList();
                    CoeusVector rateType = unitTxn.getRateTypeList();
                    CoeusVector instituteRates = unitTxn.getAllInstituteLARatesForUnit(unitNumber);
                    //Check MAINTAIN_UNIT_LA_RATES right for the given Unit Number
                    boolean hasRight = userMaintDataTxnBean.getUserHasRight(userId, MAINTAIN_UNIT_LA_RATES, unitNumber);
                    
                    dataObjects.addElement(rateClass);
                    dataObjects.addElement(rateType);
                    dataObjects.addElement(instituteRates);
                    dataObjects.addElement(new Boolean(hasRight));
                    responder.setDataObjects(dataObjects);
                    responder.setMessage(null);
                    break;
                case('C'):
                    Vector saveData = requester.getDataObjects();
                    unitNumber = (String)saveData.elementAt(0);
                    CoeusVector laRates = (CoeusVector)saveData.elementAt(1);
                    unitUpdateTxnBean = new UnitUpdateTxnBean(userId);
                    boolean updated = unitUpdateTxnBean.addUpdInstituteLARates(laRates);
                    
                    rateClass = unitTxn.getRateClassList();
                    rateType = unitTxn.getRateTypeList();
                    instituteRates = unitTxn.getAllInstituteLARatesForUnit(unitNumber);
                    userMaintDataTxnBean = new UserMaintDataTxnBean();
                    //Check MAINTAIN_UNIT_LA_RATES right for the given Unit Number
                    hasRight = userMaintDataTxnBean.getUserHasRight(userId, MAINTAIN_UNIT_LA_RATES, unitNumber);
                    
                    dataObjects.addElement(rateClass);
                    dataObjects.addElement(rateType);
                    dataObjects.addElement(instituteRates);
                    dataObjects.addElement(new Boolean(hasRight));
                    responder.setDataObjects(dataObjects);
                    
                    responder.setMessage(null);
                    break;
                case('E'):
                    unitNumber = (String)requester.getDataObject();
                    userMaintDataTxnBean = new UserMaintDataTxnBean();
                    hasRight = userMaintDataTxnBean.getUserHasRight(userId, MODIFY_DELEGATIONS, unitNumber);
                    CoeusVector unitDelegations = unitTxn.getUnitDelegations(unitNumber);
                    dataObjects.addElement(new Boolean(hasRight));
                    dataObjects.addElement(unitDelegations);
                    
                    responder.setDataObjects(dataObjects);
                    responder.setMessage(null);
                    break;
                case('F'):
                    Vector vctUserDelegations = requester.getDataObjects();
                    unitUpdateTxnBean = new UnitUpdateTxnBean(userId);
                    boolean success = unitUpdateTxnBean.updateDelegatedTo(vctUserDelegations);
                    responder.setMessage(null);
                    break;
                    
                    //Bug Fix: For Person name validation on focus lost. Start
                case('J'):
                    PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
                    PersonInfoFormBean personInfoFormBean = new PersonInfoFormBean();
                    String personName = (String)requester.getDataObject();
                    String personId = personInfoTxnBean.getPersonID(personName);
                    
                    if(personId != null && personId.equalsIgnoreCase("TOO_MANY")){
                        personInfoFormBean.setPersonID("TOO_MANY");
                    }else if(personId != null){
                        personInfoFormBean = personInfoTxnBean.getPersonInfo(personId);
                    }else{
                        personInfoFormBean.setPersonID(personId);
                    }
                    responder.setDataObject(personInfoFormBean);
                    break;
                    //Bug Fix: For Person name validation on focus lost. End
                //Added for unit hierarchy enhancement start 3 by tarique 
                     //coeusqa-1563  start 
                case('x'):
                    String rolodexFullName = (String)requester.getDataObject();
                    RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean = new RolodexMaintenanceDataTxnBean();
                    String resultRolodex = rolodexMaintenanceDataTxnBean.getRolodexFullName(rolodexFullName);
                    if(resultRolodex!=null && resultRolodex.equalsIgnoreCase("TOO MANY")) {
                        responder.setDataObject(resultRolodex);
                    }
                    break;
                     //coeusqa-1563  END
                case('K'):
                    CoeusVector adminVector = new CoeusVector();
                    unitNumber = (String)requester.getDataObject();
                    unitTxn = new UnitDataTxnBean(userId);
                    CoeusVector cvAdminData = null;
                    if(unitNumber!=null){
                        cvAdminData = unitTxn.getAdminData(unitNumber);
                        
                    }
                    CoeusVector vecAdminTypeCode = unitTxn.getAdminTypeCode();
//                    adminMap.put(UnitAdministratorBean.class, (cvAdminData ==null
//                    ?new CoeusVector():cvAdminData));
//                    adminMap.put(ComboBoxBean.class, vecAdminTypeCode);
                    adminVector.add(cvAdminData);
                    adminVector.add(vecAdminTypeCode);
                    responder.setMessage(null);
                    responder.setDataObject(adminVector);
                    break;
                    //Added for unit hierarchy enhancement end 3 by tarique
                    //added for unit detail - organization search - start
               case('N'):
                    String strOrgName = (String)requester.getDataObject();
                    unitTxn = new UnitDataTxnBean(userId);
                    String strOrgId = unitTxn.getOrganisationId(strOrgName);
                    if(strOrgId == null || strOrgId.equals("")){
                        responder.setDataObject(null);
                    }else{
                        responder.setDataObject(strOrgId);
                    }
                    break;
                    //added for unit detail - organization search - end
                // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting - Start
                case GET_UNIT_FORMULATED_COST:
                    unitTxn = new UnitDataTxnBean(userId);
                    CoeusVector cvFormulatedCost = unitTxn.getUnitFormulatedCosts(unitNumber);
                    responder.setDataObject(cvFormulatedCost);
                    break;
                case UPDATE_UNIT_FORMULATED_COST:
                    HashMap hmFormulatedCostForUpd = (HashMap)requester.getDataObject();
                    CoeusVector cvFormCostToInsUpd = (CoeusVector)hmFormulatedCostForUpd.get(GET_INSERT_UPDATE_FORM_COST_BEAN);
                    CoeusVector cvFormCostToDel = (CoeusVector)hmFormulatedCostForUpd.get(GET_DELETE_FORM_COST_BEAN);
                    UnitUpdateTxnBean unitUpdTxnBean  = new UnitUpdateTxnBean(userId);
                    if(cvFormCostToDel != null && !cvFormCostToDel.isEmpty()){
                        for(Object formulatedCost : cvFormCostToDel){
                            UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)formulatedCost;
                            unitUpdTxnBean.updateUnitFormulatedCost(unitFormulatedCostBean);        
                        }
                    }
                    if(cvFormCostToInsUpd != null && !cvFormCostToInsUpd.isEmpty()){
                        for(Object formulatedCost : cvFormCostToInsUpd){
                            UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)formulatedCost;
                            unitUpdTxnBean.updateUnitFormulatedCost(unitFormulatedCostBean);
                        }
                    }
                    break;
                // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting - End
           }
            responder.setResponseStatus(true);
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
            UtilFactory.log( errMsg, coeusEx, "UnitServlet", "perform");
            
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
            UtilFactory.log( errMsg, dbEx, "UnitServlet", "perform");
            
        } catch(Exception e) {
            responder.setResponseStatus(false);
            
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log(e.getMessage(),e,"UnitServlet","perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "UnitServlet", "doPost");
        //Case 3193 - END
        } finally {
            try{
                // send the object to applet
                outputToApplet = new ObjectOutputStream(response.getOutputStream());
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
                UtilFactory.log(ioe.getMessage(),ioe,"UnitServlet","perform");
            }
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
}
