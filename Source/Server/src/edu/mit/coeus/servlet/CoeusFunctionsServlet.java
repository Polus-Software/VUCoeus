/*
 * @(#)CoeusFunctionsServlet.java 1.0 09/14/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/* PMD check performed, and commented unused imports and variables on 16-JULY-2010
 * by George J Nirappeal
 */
package edu.mit.coeus.servlet;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.irb.bean.CommitteeTxnBean;
import edu.mit.coeus.organization.bean.OrganizationAddressFormBean;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceDataTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.irb.bean.PersonInfoTxnBean;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.irb.bean.ProtocolDataTxnBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceFormBean;
import edu.mit.coeus.sponsormaint.bean.SponsorMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexMaintenanceDataTxnBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentTxnBean;
import edu.mit.coeus.unit.bean.UnitDetailFormBean;
import edu.mit.coeus.unit.bean.UnitDataTxnBean;
import edu.mit.coeus.irb.bean.ScheduleMaintenanceTxnBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.award.bean.AwardTxnBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.locking.LockingBean;
import edu.mit.coeus.bean.CoeusParameterBean;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.Hashtable;

/**
 * This servlet is a controller that performs the execution of oracle functions.
 * It receives the serialized object bean called 'Requester Bean'
 * from the applet and performs accordingly.
 * It connects the DBEngine and executes the functions via transaction beans
 *
 * @version :1.0 August 13, 2002, 11:45 AM
 * @author Guptha K
 * @modified by Sagin
 * @date 29-10-02
 * Description : Implemented Standard Error Handling.
 *
 */
public class CoeusFunctionsServlet extends CoeusBaseServlet {
 //Commented for unused private variabe PMD check
   // private final char GET_USER_DETAILS = 'Y';
    
    // For retrieving parameters
    //Commented for unused private variabe PMD check
   // private final String GET_ALL_PARAMETERS = "GET_ALL_PARAMETERS";
    
    // For updating parameter table     
   //Commented for unused private variabe PMD check
   // private final String UPDATE_PARAMETER_DATA = "UPDATE_PARAMETER_DATA";
    
    private final String USER_HAS_PROPOSAL_ROLE ="USER_HAS_PROP_ROLE";
    //Commented for unused private variabe PMD check
    //private final String USER_HAS_OSP_RIGHT ="USER_HAS_OSP_RIGHT";
    
    //Added by shiji for Right Checking - step 1 : start
    //Commented for unused private variabe PMD check
     //private final String FN_GET_CAMPUS_FOR_DEPT ="FN_GET_CAMPUS_FOR_DEPT";
    //Right Checking - step 1 : end
    
    // Added for Coeus 4.3 enhancements
    private static final String GET_PARAMETER_VALUE = "GET_PARAMETER_VALUE";   
    
    //Added for Case#3587 - multicampus enhancement  - Start
    private final String USER_HAS_DEPARTMENTAL_RIGHT = "USER_HAS_DEPARTMENTAL_RIGHT";
    //Case#3587 - End
    
    // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - Start
    private static final String FN_USER_HAS_DEPARTMENTAL_RIGHT = "FN_USER_HAS_DEPARTMENTAL_RIGHT";
    // Added for COEUSQA-3330 IACUC - Ability to Edit Species/Group after Protocol Submission  - End
            
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

        //OrganizationMaintenanceDataTxnBean orgMaintTxnBean = new OrganizationMaintenanceDataTxnBean();
//        UtilFactory UtilFactory = new UtilFactory();

        try {
            responder = new ResponderBean();
            // get an input stream
            inputFromApplet = new ObjectInputStream(request.getInputStream());
            // read the serialized request object from applet
            requester = (RequesterBean) inputFromApplet.readObject();
            isValidRequest(requester);
            String function = (String) requester.getDataObject();
            if (function.equalsIgnoreCase("FN_ORGADDRESS")) {
                //get organization address
                OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
                OrganizationAddressFormBean organizationAddress = orgMaintTxnBean.getOrganizationAddress(requester.getId());
                //responder.setDataObjects(dataObjects);
                responder.setResponseStatus(true);
                responder.setDataObject(organizationAddress);
            }else if (function.equalsIgnoreCase("GET_PERSONINFO")) {
                //get personnel info bean
                PersonInfoTxnBean personInfoTxnBean = new PersonInfoTxnBean();
                PersonInfoFormBean personInfoFormBean =
                            personInfoTxnBean.getPersonInfoForName(requester.getId() );
                responder.setResponseStatus(true);
                responder.setDataObject(personInfoFormBean);
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
             }else if (function.equalsIgnoreCase("GET_IACUC_PERSONINFO")) {
                //get personnel info bean
                edu.mit.coeus.iacuc.bean.PersonInfoTxnBean personInfoTxnBean = new edu.mit.coeus.iacuc.bean.PersonInfoTxnBean();
                edu.mit.coeus.iacuc.bean.PersonInfoFormBean personInfoFormBean =
                            personInfoTxnBean.getPersonInfoForName(requester.getId() );
                responder.setResponseStatus(true);
                responder.setDataObject(personInfoFormBean);   
            // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
            }else if (function.equalsIgnoreCase("VALIDATE_AWARDNUMBER")) {
                //validate award number
                AwardTxnBean awTxnBean =  new AwardTxnBean();
                responder.setResponseStatus(true);
                responder.setId(""+awTxnBean.validateAwardNumber(requester.getId()));
            }else if (function.equalsIgnoreCase("GET_SPONSORINFO")) {
                //get Sponsor information
                SponsorMaintenanceDataTxnBean spTxnBean =
                                            new SponsorMaintenanceDataTxnBean();
                SponsorMaintenanceFormBean sponsorFormBean =
                    spTxnBean.getSponsorMaintenanceDetails(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(sponsorFormBean);
            }else if (function.equalsIgnoreCase("GET_ROLODEXINFO")) {
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean =
                                            new RolodexMaintenanceDataTxnBean();
                RolodexDetailsBean rolodexDetailsBean =
                    rolodexMaintenanceDataTxnBean.
                        getRolodexMaintenanceDetails(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(rolodexDetailsBean);
            }else if (function.equalsIgnoreCase("GET_UNITINFO")) {
                //get Unit information
                //Commented for unused local variabe PMD check
                //UnitDetailFormBean unitDetailRes = new UnitDetailFormBean();
                UnitDataTxnBean unitTxn = new UnitDataTxnBean();
                UnitDetailFormBean unitFormBean  =
                                    unitTxn.getUnitDetails(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(unitFormBean);
            }else if (function.equalsIgnoreCase("GET_MINUTE_ENTRY_TYPES")) {
                //get Minute entry types
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                            new ScheduleMaintenanceTxnBean();
                Vector minuteEntryTypes  =
                            scheduleMaintenanceTxnBean.getMinutesEntryTypes();
                responder.setResponseStatus(true);
                responder.setDataObject(minuteEntryTypes);
             // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
             }else if (function.equalsIgnoreCase("GET_IACUC_MINUTE_ENTRY_TYPES")) {
                //get Minute entry types
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                            new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                Vector minuteEntryTypes  =
                            scheduleMaintenanceTxnBean.getMinutesEntryTypes();
                responder.setResponseStatus(true);
                responder.setDataObject(minuteEntryTypes);   
             // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
            }else if (function.equalsIgnoreCase("GET_CONTINGENCY")) {
                //get Contingency code and desc
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                                new ScheduleMaintenanceTxnBean();
                Vector contingences  =
                            scheduleMaintenanceTxnBean.getProtocolContingency();
                responder.setResponseStatus(true);
                responder.setDataObject(contingences);
             //Added for COEUSQA-1724_FS_Reviewer View of Protocol begin    
            }else if (function.equalsIgnoreCase("GET_IACUC_CONTINGENCY")) {
                //get Contingency code and desc
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                                new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                Vector contingences  =
                            scheduleMaintenanceTxnBean.getProtocolContingency();
                responder.setResponseStatus(true);
                responder.setDataObject(contingences);
                //Added for COEUSQA-1724_FS_Reviewer View of Protocol end
            } else if (function.equalsIgnoreCase("GET_CONTINGENCY_DESC")) {
                //get Contingency desc
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                                new ScheduleMaintenanceTxnBean();
                String contingencyDesc =
                    scheduleMaintenanceTxnBean.getContingencyDesc(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(contingencyDesc);
                //Added for COEUSQA-1724_FS_Reviewer View of Protocol begin
            } else if (function.equalsIgnoreCase("GET_IACUC_CONTINGENCY_DESC")) {
                //get Contingency desc
                edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                                new edu.mit.coeus.iacuc.bean.ScheduleMaintenanceTxnBean();
                String contingencyDesc =
                    scheduleMaintenanceTxnBean.getContingencyDesc(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(contingencyDesc);
             //Added for COEUSQA-1724_FS_Reviewer View of Protocol end    
            }else if (function.equalsIgnoreCase("GET_RIGHTS_FOR_PROPOSAL")) {
                
                int userHasPropRole = 0;
                //Modified for Case#3587 - multicampus enhancement  - Start
//                int userHasOspRight = 0;
                int userHasRight = 0;
                //Case#3587 - End
                Vector vecData = requester.getDataObjects();
                Vector reqData = null;
                Vector paramData = null;
                
                if(vecData != null){
                    reqData = (Vector)vecData.elementAt(0);
                    paramData = (Vector)vecData.elementAt(1);
                }
                
                String processRoleRequest = (String)reqData.elementAt(0);
                String ospRightRequest = (String)reqData.elementAt(1);
                
                String userName = (String)paramData.elementAt(0);
                int role = ((Integer)paramData.elementAt(1)).intValue();
                String proposalId = (String)paramData.elementAt(2);
                String rightId = (String)paramData.elementAt(3);
                
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                
                if(processRoleRequest.equalsIgnoreCase(USER_HAS_PROPOSAL_ROLE)){
                    userHasPropRole = proposalDevelopmentTxnBean.getUserHasProposalRole(userName, proposalId, role);
                }
                //Modified for Case#3587 - multicampus enhancement  - Start
//                if(ospRightRequest.equalsIgnoreCase(USER_HAS_OSP_RIGHT)){
//                    userHasOspRight = proposalDevelopmentTxnBean.getUserHasOSPRight(userName, rightId);
                if(ospRightRequest.equalsIgnoreCase(USER_HAS_DEPARTMENTAL_RIGHT)){ 
                    String leadUnitNumber = proposalDevelopmentTxnBean.getProposalLeadUnit(proposalId);
                    UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                    boolean hasRight = userMaintDataTxnBean.getUserHasRight(userName,rightId,leadUnitNumber);//Case#3587 - End
                    if(hasRight){
                        userHasRight = 1;
                    }
                }
                
                Vector rightData = new Vector();
                rightData.addElement(new Integer(userHasPropRole));
                //Modified for Case#3587 - multicampus enhancement  - Start
//                rightData.addElement(new Integer(userHasOspRight));
                rightData.addElement(new Integer(userHasRight));
                responder.setResponseStatus(true);
                responder.setDataObject(rightData);
                
            }else if (function.equalsIgnoreCase("GET_ATTENDEES")) {
                //get Contingency desc
                ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean =
                                                new ScheduleMaintenanceTxnBean();
                String attendees  =
                    scheduleMaintenanceTxnBean.getGenerateAttendanceFeed(
                                                            requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(attendees);
            }else if( function.equalsIgnoreCase("GET_USER_DETAILS") ){

                String unitNumber = (requester.getId() == null ? ""
                                                        : (String)requester.getId());

                UserDetailsBean userDetailsBean = new UserDetailsBean();
                Vector vecUserUnitDetails = userDetailsBean.getUserForUnit(unitNumber);

                responder.setResponseStatus(true);
                responder.setDataObject(vecUserUnitDetails);
                /**
                 * Code Added for implementing user Roles.
                 * Implemented by Raghunath P.V.
                 */
            }


            
            else if (function.equalsIgnoreCase("DW_GET_ARG_CODE_TBL_NEW")) {
                //get Contingency desc
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecLookupValues = departmentPersonTxnBean.getArgumentCodeDescription(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(vecLookupValues);
            }


            
            else if (function.equalsIgnoreCase("DW_GET_ARG_VALUE_DESC_TBL_NEW")) {
                //get Contingency desc
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecLookupValues = departmentPersonTxnBean.getArgumentValueList(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(vecLookupValues);
            }
            /** Added for the Business Rules modules
             */
            else if (function.equalsIgnoreCase("DW_GET_ARG_VALUE_LIST")) {
                // Get the lookup value list for the business Rules module
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecLookupValues = departmentPersonTxnBean.getArgumentValueList(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(vecLookupValues);
            }
            
            else if (function.equalsIgnoreCase("DW_GET_COST_ELEMENTS")) {
                //get Contingency desc
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecLookupValues = departmentPersonTxnBean.getCostElements();
                responder.setResponseStatus(true);
                responder.setDataObject(vecLookupValues);
            }


            else if (function.equalsIgnoreCase("DW_GET_DEGREE_TYPE")) {
                //get Contingency desc
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecDegreeTypeLookupValues = departmentPersonTxnBean.getDegreeTypeCodeDescription();
                responder.setResponseStatus(true);
                responder.setDataObject(vecDegreeTypeLookupValues);
            }



            else if (function.equalsIgnoreCase("DW_GET_SCIENCE_CODE")) {
                //get Contingency desc
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecScienceCodeLookupValues = departmentPersonTxnBean.getSchoolCodeDescription();
                responder.setResponseStatus(true);
                responder.setDataObject(vecScienceCodeLookupValues);
            }


            else if (function.equalsIgnoreCase("DW_GET_DEGREE_DETAILS")) {
                //get Contingency desc
                DepartmentPersonTxnBean departmentPersonTxnBean =
                                                new DepartmentPersonTxnBean();
                Vector vecDegreeDetailValues = departmentPersonTxnBean.getDepartmentPersonDegree(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(vecDegreeDetailValues);
            }


            else if (function.equalsIgnoreCase("GET_USER_PROPOSAL_RIGHTS")) {
                UserDetailsBean userDetailsBean =
                                                new UserDetailsBean();
                Vector vecParams = (Vector) requester.getDataObjects();
                
                int rightExists = userDetailsBean.getUserHasRole((String)vecParams.elementAt(0), 
                                    Integer.parseInt((String)vecParams.elementAt(1)));
                responder.setResponseStatus(true);
                
                
                responder.setId(rightExists+"");
            }

            else if (function.equalsIgnoreCase("GET_DBTIMESTAMP")) {
                
                java.sql.Timestamp dbTimeStamp = (new CoeusFunctions()).getDBTimestamp();
                
                responder.setDataObject(dbTimeStamp);
            }
            
            else if (function.equalsIgnoreCase("GET_USER_UNITS")) {
                UserDetailsBean userDetailsBean =
                                                new UserDetailsBean();
                Vector vecParams = (Vector) requester.getDataObjects();
                
                Vector userUnits = (Vector) userDetailsBean.getUnitsForUser((String)vecParams.elementAt(0), 
                                    (String)vecParams.elementAt(1));
                responder.setResponseStatus(true);
                
                responder.setDataObject(userUnits);
            }


            else if (function.equalsIgnoreCase("GET_USER_HAS_ANY_OSP_RIGHTS")) {
                UserDetailsBean userDetailsBean =
                                                new UserDetailsBean();
                
                int rightExists  = userDetailsBean.getUserHasAnyOSPRights(requester.getId());
                responder.setResponseStatus(true);
                responder.setId(rightExists + "");
            }else if( function.equalsIgnoreCase("GET_ROLE_RIGHTS") ){

                String roleId;
                Vector vecRightsForRole = null;
                RoleTxnBean roleTxnBean = new RoleTxnBean();
                roleId = requester.getId();
                if(roleId != null){
                    int id = Integer.parseInt(roleId);
                    vecRightsForRole = roleTxnBean.getProposalRightRole(id);
                }
                responder.setResponseStatus(true);
                responder.setDataObject(vecRightsForRole);
            }

            else if(function.equalsIgnoreCase("GET_DEFAULT_LOCATION"))
            {
               Vector dataObjects = new Vector() ;
                //get the default organization and its address
                OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
                String orgId = orgMaintTxnBean.getDefaultLocationFromParamTable() ;
                OrganizationAddressFormBean organizationAddress = orgMaintTxnBean.getOrganizationAddress(orgId) ;
                
                // rolodex info    
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean =
                                            new RolodexMaintenanceDataTxnBean();
                RolodexDetailsBean rolodexDetailsBean =
                    rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(String.valueOf(organizationAddress.getContactAddressId()));
                
                dataObjects.add(organizationAddress) ;
                dataObjects.add(rolodexDetailsBean) ;
                responder.setResponseStatus(true) ;
                responder.setId(orgId) ;
                responder.setDataObjects(dataObjects) ;
                
            }
            //Added for IACUC Changes
            // Added for Enable multicampus for default organization in protocols - Start
            else if(function.equalsIgnoreCase("GET_ORGANIZATION_FOR_UNIT")) {
                String unitNumber = requester.getId();
                if(unitNumber != null && !"".equals(unitNumber)){
                    Vector dataObjects = new Vector() ;
                    
                    OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
                    String orgId = orgMaintTxnBean.getOrganizationId(unitNumber);
                    OrganizationAddressFormBean organizationAddress = orgMaintTxnBean.getOrganizationAddress(orgId) ;
                    
                    // rolodex info
                    RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean =
                            new RolodexMaintenanceDataTxnBean();
                    RolodexDetailsBean rolodexDetailsBean =
                            rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(String.valueOf(organizationAddress.getContactAddressId()));
                    dataObjects.add(organizationAddress);
                    dataObjects.add(rolodexDetailsBean);
                    responder.setResponseStatus(true);
                    responder.setId(orgId);
                    responder.setDataObjects(dataObjects);
                }else{
                    responder.setResponseStatus(false);
                }
            }
            // Added for Enable multicampus for default organization in protocols - End
            else if(function.equalsIgnoreCase("GET_IACUC_DEFAULT_LOCATION")) {
                Vector dataObjects = new Vector() ;
                //get the default organization and its address
                OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                String orgId = coeusFunctions.getParameterValue("DEFAULT_IACUC_ORGANIZATION_ID");
                OrganizationAddressFormBean organizationAddress = orgMaintTxnBean.getOrganizationAddress(orgId) ;
                
                RolodexMaintenanceDataTxnBean rolodexMaintenanceDataTxnBean =
                        new RolodexMaintenanceDataTxnBean();
                RolodexDetailsBean rolodexDetailsBean =
                        rolodexMaintenanceDataTxnBean.getRolodexMaintenanceDetails(String.valueOf(organizationAddress.getContactAddressId()));
                
                dataObjects.add(organizationAddress) ;
                dataObjects.add(rolodexDetailsBean) ;
                responder.setResponseStatus(true) ;
                responder.setId(orgId) ;
                responder.setDataObjects(dataObjects) ;
                
            }
            //IACUC Changes - End
            else if(function.equalsIgnoreCase("IRB_COMM_SELECTION_DURING_SUBMISSION"))
            {
                //get the default submission mode
                OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
                String submissionMode = orgMaintTxnBean.getDefaultSubmissionModeFromParamTable() ;
                               
                responder.setResponseStatus(true) ;
                responder.setId(submissionMode) ;
                                
            }
            //Added for IACUC Parameter -Start.
             else if(function.equalsIgnoreCase("IACUC_COMM_SELECTION_DURING_SUBMISSION"))
            {
                //get the default submission mode
                OrganizationMaintenanceDataTxnBean orgMaintTxnBean =  new OrganizationMaintenanceDataTxnBean();
                String submissionMode = orgMaintTxnBean.getDefaultIacucSubmission() ;                               
                responder.setResponseStatus(true) ;
                responder.setId(submissionMode) ;
                                
            }
             //Added for IACUC Parameter -end.
            else if (function.equalsIgnoreCase("VALIDATE_DEVPROP_NUMBER")) {
                //validate Development Proposal number
                CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                boolean isValid = coeusDataTxnBean.isValidDevProposalNumber(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(new Boolean(isValid));
            }
            else if (function.equalsIgnoreCase("VALIDATE_INSTPROP_NUMBER")) {
                //validate Institute Proposal number
                CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                boolean isValid = coeusDataTxnBean.isValidInstProposalNumber(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(new Boolean(isValid));
            }else if ( function.equalsIgnoreCase("LOCK_RECORD") ) {
                Vector dataObjects = requester.getDataObjects();
                if( dataObjects != null && dataObjects.size() > 0 ) {
                    String moduleName = (String)dataObjects.elementAt(0);
                    String primaryKey = (String)dataObjects.elementAt(1);
                    //String dbTableName = getDBTableName(moduleName);
                    /* Code added by Shivakumar for locking enhancement
                     */
                    loggedinUser = requester.getUserName();
                    //Modified for Iacuc changes - Start
                    if("PROTOCOL".equals(moduleName)){
                        ProtocolDataTxnBean protoTxnBean = new ProtocolDataTxnBean();
                        LockingBean lockingBean = protoTxnBean.getLock(primaryKey, loggedinUser, "111111");
                        boolean lockCheck = lockingBean.isGotLock();
                    /* Commented by Shivakumar for locking enhancement
                     */
                        //TransactionMonitor monitor = TransactionMonitor.getInstance();
//                    if( monitor.canEdit(dbTableName+primaryKey) ){
                        try{
                            if(lockCheck){
                                protoTxnBean.transactionCommit();
                                responder.setResponseStatus(true);
                            }else{
                                protoTxnBean.transactionRollback();
                                responder.setResponseStatus(false);
                                CoeusMessageResourcesBean coeusMessageResourcesBean
                                        =new CoeusMessageResourcesBean();
                                String errMsg = coeusMessageResourcesBean.parseMessageKey(
                                        "protocolDetForm_exceptionCode.1019");
                                responder.setMessage(errMsg);
                                CoeusException ex = new CoeusException(errMsg,1);
                                responder.setDataObject(ex);
                            }
                        }catch(DBException dbEx){
                            dbEx.printStackTrace();
                            protoTxnBean.transactionRollback();
                            throw dbEx;
                        }finally{
                            protoTxnBean.endConnection();
                        }
                    }else if("IACUC".equals(moduleName)){
                        edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean protoTxnBean = 
                                new edu.mit.coeus.iacuc.bean.ProtocolDataTxnBean();
                        LockingBean lockingBean = protoTxnBean.getLock(primaryKey, loggedinUser, "111111");
                        boolean lockCheck = lockingBean.isGotLock();
                        try{
                            if(lockCheck){
                                protoTxnBean.transactionCommit();
                                responder.setResponseStatus(true);
                            }else{
                                protoTxnBean.transactionRollback();
                                responder.setResponseStatus(false);
                                CoeusMessageResourcesBean coeusMessageResourcesBean
                                        =new CoeusMessageResourcesBean();
                                String errMsg = coeusMessageResourcesBean.parseMessageKey(
                                        "protocolDetForm_exceptionCode.1019");
                                responder.setMessage(errMsg);
                                CoeusException ex = new CoeusException(errMsg,1);
                                responder.setDataObject(ex);
                            }
                        }catch(DBException dbEx){
                            dbEx.printStackTrace();
                            protoTxnBean.transactionRollback();
                            throw dbEx;
                        }finally{
                            protoTxnBean.endConnection();
                        }
                    }
                }
             
            }else if(function.equalsIgnoreCase("FN_USER_HAS_OSP_RIGHT")){
                String rightId = requester.getId();
                loggedinUser = requester.getUserName();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasOSPRight = userMaintDataTxnBean.getUserHasOSPRight(loggedinUser, rightId);
                responder.setDataObject(new Boolean(hasOSPRight));
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }else if(function.equalsIgnoreCase("FN_USER_HAS_RIGHT_IN_ANY_UNIT")){
                
                loggedinUser = requester.getUserName();
                                
                String rightId = requester.getId();
                
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                boolean hasRight = userMaintDataTxnBean.getUserHasRightInAnyUnit(loggedinUser, rightId);
                responder.setDataObject(new Boolean(hasRight));
                responder.setMessage(null);
                responder.setResponseStatus(true);
                //Added by shiji for Right Checking - step 2 : start
            }else if(function.equalsIgnoreCase("FN_GET_CAMPUS_FOR_DEPT")){
                String unitNumber = requester.getId();
                UserMaintDataTxnBean userMaintDataTxnBean = new UserMaintDataTxnBean();
                String topLevelUnit = userMaintDataTxnBean.getCampusForDept(unitNumber);
                responder.setDataObject(topLevelUnit);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }//Right Checking - step 2 : end
            else if(function.equalsIgnoreCase("GET_ALL_PROPOSAL_ROLE_RIGHTS")){  
                Hashtable data = new Hashtable();
                ProposalDevelopmentTxnBean proposalDevelopmentTxnBean = new ProposalDevelopmentTxnBean();
                CoeusVector roleRights = proposalDevelopmentTxnBean.getAllProposalRoleRights();                
                /* Added for fixing Narrative Bug. 
                 While loading the proposal roles get all the narrative
                 rights and Narrative roles if it present- BEGIN
                 *Added on 03-Sept-2004
                 */
                if(roleRights!= null && roleRights.size()>0){
                    data.put(KeyConstants.ROLE_RIGHTS, roleRights);
                }
                Vector vcProposalNumber = (Vector)requester.getDataObjects();
                String proposalNumber = (String)vcProposalNumber.elementAt(0);
                CoeusVector cvNarrativeRights = proposalDevelopmentTxnBean.getNarrativeRights(proposalNumber);
                if(cvNarrativeRights!=null && cvNarrativeRights.size()>0){
                    data.put(KeyConstants.NARRATIVE_RIGHTS, cvNarrativeRights);
                }
                CoeusVector cvNarrativeModule = proposalDevelopmentTxnBean.getNarrativeModule(proposalNumber);
                if(cvNarrativeModule!= null && cvNarrativeModule.size()>0){
                    data.put(KeyConstants.NARRATIVE_MODULE, cvNarrativeModule);
                }
                responder.setDataObject(data);
                responder.setMessage(null);
                responder.setResponseStatus(true);
                // end of the function.
            }else if(function.equalsIgnoreCase("GET_ALL_PARAMETERS")){  
                
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                CoeusVector cvParameterData = coeusFunctions.getAllParameters();
                responder.setDataObject(cvParameterData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }
            else if(function.equalsIgnoreCase("UPDATE_PARAMETER_DATA")){         
                CoeusVector cvParamData = (CoeusVector)requester.getDataObjects();
                if (cvParamData!=null && cvParamData.size()>0) {
                  for(int index=0; index < cvParamData.size(); index++){
                      CoeusParameterBean coeusParameterBean = (CoeusParameterBean)cvParamData.elementAt(index);
                      if(coeusParameterBean.getAcType()==null) {
                          continue;
                      }
                      CoeusFunctions coeusFunctions = new CoeusFunctions(loggedinUser);
                      boolean success = coeusFunctions.updateParameters(coeusParameterBean);
                  }
                }
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                CoeusVector cvParameterData = coeusFunctions.getAllParameters();
                responder.setDataObject(cvParameterData);
                responder.setMessage(null);
                responder.setResponseStatus(true);
            }// Code added for coeus 4.3 enhancement - starts
            else if(function.equalsIgnoreCase(GET_PARAMETER_VALUE)){  
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                Vector vecParameter = (Vector)requester.getDataObjects();
                if(vecParameter!=null && vecParameter.size()>0){
                    String value = (String) vecParameter.get(0);
                    value = coeusFunctions.getParameterValue(value);
                    responder.setDataObject(value);
                    responder.setResponseStatus(true);
                } else {
                    responder.setResponseStatus(false);
                }
            }// Code added for coeus 4.3 enhancement - ends
            else if(function.equalsIgnoreCase("GET_LICENSE_TEXT")){  
                CoeusFunctions coeusFunctions = new CoeusFunctions();
                responder.setDataObject(coeusFunctions.getLicenseText());
                responder.setMessage(null);
                responder.setResponseStatus(true);
            //Added for the Coeus Enhancement case:#1799 start     
            }else if(function.equalsIgnoreCase("VALIDATE_PROTOCOL_NUMBER")){
                // to validate protocol number
                CoeusDataTxnBean coeusDataTxnBean =  new CoeusDataTxnBean();
                boolean isValid = coeusDataTxnBean.validateProtocolNumber(requester.getId());
                responder.setResponseStatus(true);
                responder.setDataObject(new Boolean(isValid));
                /** case Id 1856 and 1860
                 *Checking for the CREATE_PROPOSAL right insted of role_Id = 7
                 *which is checking against the role. The function for the role_id
                 *checking is GET_USER_PROPOSAL_RIGHTS which is not used now
                 *Start
                 */
                 
            }else if(function.equalsIgnoreCase("GET_USER_HAS_PROPOSAL_RIGHTS")){
                UserMaintDataTxnBean userMaintDataTxnBean =
                                                new UserMaintDataTxnBean();
                Vector vecParams = (Vector) requester.getDataObjects();
                
                boolean isRightExists = userMaintDataTxnBean.getUserHasRightInAnyUnit(
                            (String)vecParams.elementAt(0), (String)vecParams.elementAt(1));
                responder.setResponseStatus(true);
                responder.setDataObject(new Boolean(isRightExists));
            }// End for case Id 1856 and 1860
            // Added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
            else if(function.equalsIgnoreCase("GET_COMMITTEE_TYPES")){
                CommitteeTxnBean committeeTxnBean = new CommitteeTxnBean();
                Vector vecCommitteeTypes = new Vector();
                vecCommitteeTypes = committeeTxnBean.getCommitteeTypes();
                responder.setResponseStatus(true);
                responder.setDataObject(vecCommitteeTypes);
            }    
            // added for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
            //End Coeus Enhancement case:#1799 end
            else if(FN_USER_HAS_DEPARTMENTAL_RIGHT.equals(function)){
                Vector vecRightDetails = requester.getDataObjects();
                boolean hasRight = false;
                loggedinUser = requester.getUserName();
                if(vecRightDetails != null && !vecRightDetails.isEmpty()){
                    String right_id = (String)vecRightDetails.get(0);
                    String unitNumber = (String)vecRightDetails.get(1);
                    UserMaintDataTxnBean txnData = new UserMaintDataTxnBean();
                    hasRight = txnData.getUserHasRight(loggedinUser, right_id, unitNumber);
                }
                responder.setDataObject(new Boolean(hasRight));
                responder.setMessage(null);
                responder.setResponseStatus(true);

            }
            else if(function.equalsIgnoreCase("GET_PROTOCOL_LEAD_UNIT")){
                UserMaintDataTxnBean userMaintDataTxnBean =
                                                new UserMaintDataTxnBean();
                Vector vecParams = (Vector) requester.getDataObjects();

                String leadUnit = userMaintDataTxnBean.getProtocolLeadUnit(
                            (String)vecParams.elementAt(0), (Integer)vecParams.elementAt(1));
                responder.setResponseStatus(true);
                responder.setDataObject(new String(leadUnit));
            }
        } catch( CoeusException coeusEx ) {
        //Commented for unused local variabe PMD check
            //int index=0;
            String errMsg;
            //coeusEx.printStackTrace();
            errMsg = coeusEx.getMessage();
            CoeusMessageResourcesBean coeusMessageResourcesBean=new CoeusMessageResourcesBean();
            errMsg= coeusMessageResourcesBean.parseMessageKey(errMsg);
            responder.setResponseStatus(false);
            responder.setDataObject(coeusEx);    
            //print the error message at client side
            responder.setException(coeusEx);
            responder.setMessage(errMsg);
            UtilFactory.log( errMsg, coeusEx, "CoeusFunctionsServlet",
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
            UtilFactory.log( errMsg, dbEx, "CoeusFunctionsServlet", "perform");

        } catch (Exception e) {
            //print the error message at server side
            responder.setResponseStatus(false);
            //print the error message at client side
            responder.setException(e);
            responder.setMessage(e.getMessage());
            UtilFactory.log( e.getMessage(), e,"CoeusFunctionsServlet",
                                                                "perform");
        //Case 3193 - START
        }catch(Throwable throwable){
            Exception ex = new Exception(throwable);
            responder.setException(ex);
            responder.setResponseStatus(false);
            responder.setMessage(ex.getMessage());
            UtilFactory.log( throwable.getMessage(), throwable, "CoeusFunctionsServlet", "doPost");
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
                UtilFactory.log(ioe.getMessage(),ioe,"CoeusFunctionsServlet",
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

    public void getOrganizationAddress() {

    }

    private String getDBTableName(String moduleName ) {
        if( moduleName.equalsIgnoreCase( "PROTOCOL" ) ){
            return "osp$protocol";
        }
        return "";
    }
}