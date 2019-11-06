/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.irb.action;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.propdev.bean.ProposalUserRoleFormBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import edu.utk.coeuslite.propdev.action.ProposalBaseAction;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author indhulekha
 */
public class NewStubProposalAction extends ProposalBaseAction {
    
    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward performExecute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String protocolNumber = request.getParameter("protocolNumber").toString();


            protocolNumber = protocolNumber.trim();
            WebTxnBean webTxnBean = new WebTxnBean();
            HashMap hmData = new HashMap();
            hmData.put("AW_PROTOCOL_NUMBER", protocolNumber);
            hmData.put("AV_PROTOCOL_NUMBER", protocolNumber);
            hmData.put("AI_PROTOCOL_NUMBER", protocolNumber);
            hmData.put("AU_PROTOCOL_NUMBER", protocolNumber);

            Hashtable validateList = (Hashtable) webTxnBean.getResults(request, "getValidateFieldsForProtocol",hmData);
            HashMap validateMap=  (HashMap) validateList.get("getValidateFieldsForProtocol");

            if(validateMap!=null && validateMap.size() > 0){
               String isValid = validateMap.get("isValid").toString();
                if(isValid.equals("true")) {
                 // request.setAttribute("streamProposal", "true");
                  createNewStubProposal(mapping,form,request,response,protocolNumber);
                    return mapping.findForward(SUCCESS);
                }
                else {
                   if(isValid != null) {
                       String []splitList = isValid.split(",");

                       if(splitList != null && splitList.length > 0) {
                           if(splitList[0].trim().length() == 0) {
                               isValid = isValid.substring(1);
                           }

                       }
                   }
                     request.setAttribute("isValid", isValid);
                }
            }
        
        return mapping.findForward(SUCCESS);
    }

    public void createNewStubProposal(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response, String protocolNumber) throws Exception {

         DynaValidatorForm dynaForm = (DynaValidatorForm)form;
          WebTxnBean webTxnBean = new WebTxnBean();
          HashMap   hmRequiredDetails = new HashMap();
          HttpSession session = request.getSession();

           UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String createUser =  userInfoBean.getUserId();
        String seq = (String)session.getAttribute(CoeusLiteConstants.SEQUENCE_NUMBER+session.getId());

        try{
            HashMap hmData = new HashMap();
            hmData.put("protocolNum", protocolNumber);
            Hashtable htProtoHeader = (Hashtable)webTxnBean.getResults(request, "getProtocolDetailsForStubProposal" , hmData );
            HashMap vecProtocolHeader = (HashMap)htProtoHeader.get("getProtocolDetailsForStubProposal");

             String title = "";
             String unitNumber = "";
             Date startDate = new Date();
             Date endDate = new Date();


             if(vecProtocolHeader != null && vecProtocolHeader.size() > 0) {
                title = (String)vecProtocolHeader.get("TITLE");
                startDate = (Date) vecProtocolHeader.get("APPLICATION_DATE");
                endDate = (Date) vecProtocolHeader.get("EXPIRATION_DATE");
            }

              HashMap usrData = new HashMap();
            usrData.put("userId", createUser);

            Hashtable htunitDt = (Hashtable)webTxnBean.getResults(request, "getUserUnit" , usrData );
            HashMap vecUnit = (HashMap)htunitDt.get("getUserUnit");

            if(vecUnit != null && vecUnit.size() > 0) {
               unitNumber = (String)vecUnit.get("UNIT_NUMBER");
            }

            hmRequiredDetails.put("unitNumber",unitNumber);

             String updateUser = createUser;
            Date updateTimestamp = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(updateTimestamp.getTime());


            Hashtable htNextProposalNumber =
            (Hashtable)webTxnBean.getResults(request, "getProposalNumber", hmRequiredDetails);

            String proposalNumber =
            (String)((HashMap)htNextProposalNumber.get("getNextProposalNumber")).get("nextProposalNumber");

            String activityType = getParameterValue(request);

            HashMap hMap = new HashMap();
            hMap.put("proposalNumber", proposalNumber);
            hMap.put("proposalType", 1);
            hMap.put("statusCode", 1);
            hMap.put("title", title);
            //hMap.put("SPONSOR_CODE", fundingSource);
            hMap.put("creationStatusCode", 1);
            hMap.put("templateFlag", "N");
            hMap.put("activityType", Integer.parseInt(activityType));
            hMap.put("subcontractFlag", "N");
            hMap.put("unitNumber", unitNumber);
            hMap.put("createUser", createUser);
            hMap.put("PRE_DEFINED", updateUser);
            hMap.put("newUpdateTimetamp", timestamp);
            hMap.put("updateTimestamp", timestamp);
            hMap.put("startDate", startDate);
            hMap.put("endDate", endDate);
            hMap.put("acType", "I");

            Hashtable htProposal = (Hashtable)webTxnBean.getResults(request, "addUpdProposal" , hMap);

             request.setAttribute("streamProposal", "true");

            Vector vecProposalUserRoles = getProposalUserRoles(request,proposalNumber, unitNumber, "N", userInfoBean.getUserId());

            HashMap roleMap = new HashMap();
            roleMap.put("proposalNumber", proposalNumber);
            roleMap.put("userId", updateUser);
            roleMap.put("PRE_DEFINED", updateUser);
            roleMap.put("newUpdateTimetamp", timestamp.toString());
            roleMap.put("updateTimestamp", timestamp.toString());
            roleMap.put("acType", "I");

            updateProposalUserRoles(request,vecProposalUserRoles , proposalNumber ,roleMap);

            Hashtable htfundIndicator = (Hashtable)webTxnBean.getResults(request, "update_proto_funding_indicator" , hmData);
            /*Get the investigators for the stub proposl from protocol*/
            HashMap inputMap = new HashMap();
            inputMap.put("protocolNumber",protocolNumber);
            inputMap.put("sequenceNumber",Integer.parseInt(seq));

            Hashtable invstTable = (Hashtable)webTxnBean.getResults(request, "getProtocolInvestigatorsDatas" , inputMap);
            Vector vecProtoInvst = (Vector)invstTable.get("getProtocolInvestigatorsDatas");

            String personId = "";
            String personName = "";
            String invstFlag = "";
            String nonEmplFlag = "";
            
            if(vecProtoInvst != null && vecProtoInvst.size() >0) {
                for(int i = 0; i <vecProtoInvst.size(); i++) {
                DynaValidatorForm dynaInvstForm = (DynaValidatorForm) vecProtoInvst.get(i);
               personId =  dynaInvstForm.getString("personId");
               personName =  dynaInvstForm.getString("personName");
               invstFlag =  dynaInvstForm.getString("principleInvestigatorFlag");
               nonEmplFlag =  dynaInvstForm.getString("nonEmployeeFlag");

               /*Insert stub proposal investigators */
                HashMap invstMap = new HashMap();
                invstMap.put("proposalNumber", proposalNumber);
                invstMap.put("personId", personId);
                invstMap.put("personName", personName);
                invstMap.put("principalInvestigatorFlag", invstFlag);
                invstMap.put("is_Employee", nonEmplFlag);
                invstMap.put("PRE_DEFINED", updateUser);
                invstMap.put("propInvUpdateUser", updateUser);
                 invstMap.put("awUpdateTimestamp", timestamp);
                  invstMap.put("propInvTimestamp", timestamp);
                  invstMap.put("multiPIFlag", "N");
                  invstMap.put("acType", "I");
                  invstMap.put("percentageEffort", new Float(0));


                Hashtable htProtoInvst = (Hashtable)webTxnBean.getResults(request, "updatePropdevInvestigator" , invstMap);
                }
            }

            

             /*Get the units for the stub proposl from protocol*/
            Hashtable unitTable = (Hashtable)webTxnBean.getResults(request, "get_proto_units" , hmData);
            Vector vecProtoUnits = (Vector)unitTable.get("get_proto_units");

            String unitPersonId = "";
            String unitNum = "";
            String leadUnitFlag = "";

            if(vecProtoUnits != null && vecProtoUnits.size() > 0) {
                for(int j = 0; j < vecProtoUnits.size(); j++) {
                    DynaValidatorForm dynaUnitForm = (DynaValidatorForm)vecProtoUnits.get(j);
                    unitNum = dynaUnitForm.getString("departmentNumber");
                    unitPersonId = dynaUnitForm.getString("personId");
                    leadUnitFlag = dynaUnitForm.getString("principleInvestigatorFlag");
                    /*insert unit details for stub proposal*/
                    HashMap unitMap = new HashMap();
                    unitMap.put("proposalNumber", proposalNumber);
                    unitMap.put("personId", unitPersonId);
                    unitMap.put("unitNumber", unitNum);
                    unitMap.put("leadUnitFlag", leadUnitFlag);
                    unitMap.put("PRE_DEFINED", updateUser);
                    unitMap.put("propInvTimestamp", timestamp);
                    unitMap.put("updateUser", updateUser);
                    unitMap.put("awUpdateTimestamp", timestamp);
                    unitMap.put("acType", "I");
                    
                     Hashtable htProtoUnits = (Hashtable)webTxnBean.getResults(request, "updatePropdevInvUnits" , unitMap);
                }
            }


            /*Get protocol organization details for stub proposal*/

             Hashtable orgsTable = (Hashtable)webTxnBean.getResults(request, "getProtocolLocationData" , inputMap);
            Vector vecProtoOrgs = (Vector)orgsTable.get("getProtocolLocationData");

            String locationTypeCode = "";
            String OrgsnId = "";
            String rolodexId = "";
            String orgsName = "";

            if(vecProtoOrgs != null && vecProtoOrgs.size() > 0) {
                for(int i = 0; i < vecProtoOrgs.size(); i++) {
                    DynaValidatorForm dynaOrgsForm = (DynaValidatorForm)vecProtoOrgs.get(i);
                    locationTypeCode = String.valueOf(dynaOrgsForm.get("protocolOrgTypeCode"));
                    OrgsnId = dynaOrgsForm.getString("organizationId");
                    rolodexId = dynaOrgsForm.getString("rolodexId");
                    orgsName = dynaOrgsForm.getString("organizationName");
                    /*Insert organization details for stub proposal*/
                    HashMap orgsMap = new HashMap();
                    orgsMap.put("proposalNumber", proposalNumber);
                    orgsMap.put("siteNumber", locationTypeCode);
                    orgsMap.put("locationName", orgsName);
                    orgsMap.put("locationTypeCode", locationTypeCode);
                    orgsMap.put("organizationId", OrgsnId);
                    orgsMap.put("rolodexId", rolodexId);
                    orgsMap.put("updateTimestamp", timestamp);
                    orgsMap.put("updateUser", updateUser);
                    orgsMap.put("awUpdateTimeStamp", timestamp);
                    orgsMap.put("acType", "I");
                    
                    Hashtable htProtoOrgs = (Hashtable)webTxnBean.getResults(request, "updProposalSites" , orgsMap);
                }
            }           

            /*get protocol special review for stub proposal*/
            Hashtable splRvTable = (Hashtable)webTxnBean.getResults(request, "getProtocolSpecialReview" , inputMap);
            Vector vecProtoSplRv = (Vector)splRvTable.get("getProtocolSpecialReview");

            String splRvprotoNumber = "";
            String splReviewNum = "";
            String splReviewCode = "";
            String approvalTypeCode = "";
            String applicationDate = "";
            String approvalDate = "";
            String comments = "";
                        
             int splRvNum = 1;
        
            if(vecProtoSplRv != null && vecProtoSplRv.size() > 0) {
                for(int j = 0; j < vecProtoSplRv.size(); j++) {
                    DynaValidatorForm dynaSpRvForm = (DynaValidatorForm)vecProtoSplRv.get(j);
                    splReviewNum = dynaSpRvForm.get("specialReviewNumber").toString();
                    splReviewCode = dynaSpRvForm.get("specialReviewCode").toString();
                    if(dynaSpRvForm.get("spRevProtocolNumber") != null) {
                        splRvprotoNumber = dynaSpRvForm.get("spRevProtocolNumber").toString();
                    }
                    if(dynaSpRvForm.get("specialReviewNumber") == null) {
                        splRvNum = 1;
                    }
                    else {
                        String splNum = dynaSpRvForm.get("specialReviewNumber").toString();
                        int num = Integer.parseInt(splNum);
                        num++;
                        splRvNum = num;
                    }
                    approvalTypeCode = dynaSpRvForm.get("approvalCode").toString();
                    applicationDate = dynaSpRvForm.getString("applicationDate");
                    approvalDate = dynaSpRvForm.getString("approvalDate");
                    comments = dynaSpRvForm.getString("comments");

                    if(applicationDate != null) {
                        String[] list = applicationDate.trim().split("\\s");
                        applicationDate = list[0];
                        list = applicationDate.split("-");
                        if(list.length > 2) {
                            applicationDate = list[1]+"/"+list[2]+"/"+list[0];
                        }
                    }
                    if(approvalDate != null) {
                        String[] listapp = approvalDate.trim().split("\\s");
                        approvalDate = listapp[0];

                        listapp = approvalDate.split("-");
                        if(listapp.length > 2) {
                            approvalDate = listapp[1]+"/"+listapp[2]+"/"+listapp[0];
                        }
                    }
                     DateUtils dateUtils = new DateUtils();

                    /*insert special review for stub proposal*/
                    HashMap splMap = new HashMap();
                    splMap.put("proposalNumber", proposalNumber);
                    splMap.put("specialReviewNumber", splReviewNum);
                    splMap.put("specialReviewCode", splReviewCode);
                    splMap.put("approvalCode", approvalTypeCode);
                    splMap.put("spRevProtocolNumber", splRvprotoNumber);
                    applicationDate = dateUtils.formatDate(applicationDate, ":/.,|-", "MM/dd/yyyy");
                    approvalDate = dateUtils.formatDate(approvalDate, ":/.,|-", "MM/dd/yyyy");

                    if(applicationDate != null && applicationDate.length() > 0) {                        
                        splMap.put("applicationDate", new Date(applicationDate));
                    }
                    if(dynaSpRvForm.get("approvalDate") != null && approvalDate.length() > 0){
                         splMap.put("approvalDate", new Date(approvalDate));
                    }
                    splMap.put("comments", comments);
                    splMap.put("PRE_DEFINED", updateUser);
                    splMap.put("pdSpTimestamp", timestamp);
                    splMap.put("acType", "I");


                 Hashtable htProtoSplRv = (Hashtable)webTxnBean.getResults(request, "updateProposalSpecialReview" , splMap);
                }
            }
                
                if(startDate != null) {
                    applicationDate = startDate.toString();
                }else {
                    applicationDate = null;
                }
                
                if(endDate != null) {
                    approvalDate = endDate.toString();
                }else {
                   approvalDate = null; 
                }
                comments = "";
                    if(applicationDate != null) {
                        String[] list = applicationDate.trim().split("\\s");
                        applicationDate = list[0];
                        list = applicationDate.split("-");
                        if(list.length > 2) {
                            applicationDate = list[1]+"/"+list[2]+"/"+list[0];
                        }
                    }
                    if(approvalDate != null) {
                        String[] listapp = approvalDate.trim().split("\\s");
                        approvalDate = listapp[0];

                        listapp = approvalDate.split("-");
                        if(listapp.length > 2) {
                            approvalDate = listapp[1]+"/"+listapp[2]+"/"+listapp[0];
                        }
                    }
                     DateUtils dateUtils = new DateUtils();

                    /*insert special review for stub proposal*/
                    HashMap splMap = new HashMap();
                    splMap.put("proposalNumber", proposalNumber);
                    splMap.put("specialReviewNumber", String.valueOf(splRvNum));
                    splMap.put("specialReviewCode", "1");
                    splMap.put("approvalCode", "1");
                    splMap.put("spRevProtocolNumber", protocolNumber);
                    applicationDate = dateUtils.formatDate(applicationDate, ":/.,|-", "MM/dd/yyyy");
                    approvalDate = dateUtils.formatDate(approvalDate, ":/.,|-", "MM/dd/yyyy");

                    if(applicationDate != null && applicationDate.length() > 0) {                        
                        splMap.put("applicationDate", new Date(applicationDate));
                    }
                    if(approvalDate != null && approvalDate.length() > 0){
                         splMap.put("approvalDate", new Date(approvalDate));
                    }
                    splMap.put("comments", comments);
                    splMap.put("PRE_DEFINED", updateUser);
                    splMap.put("pdSpTimestamp", timestamp);                   
                    splMap.put("acType", "I");


                 Hashtable htProtoSplRv = (Hashtable)webTxnBean.getResults(request, "updateProposalSpecialReview" , splMap);

            /*Insert funding source for protocol itself*/
            HashMap fundMap = new HashMap();
            fundMap.put("protocolNum", protocolNumber);
            fundMap.put("sequenceNum", 1);
            fundMap.put("fundingSourceType", "4");
            fundMap.put("fundingSource", proposalNumber);

             Hashtable htProtoFund = (Hashtable)webTxnBean.getResults(request, "insert_stub_protocol_funding_source" , fundMap);

            HashMap hmpProtocolData = new HashMap();
            hmpProtocolData.put("protocolNumber", protocolNumber);
            hmpProtocolData.put("sequenceNumber", Integer.parseInt(seq));
            
           getFundingSourcesData(hmpProtocolData,request,mapping);

        }catch(Exception ex) {
              request.setAttribute("streamProtocol", "false");
               UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"NewStubProposalAction","createNewStubProposal");
          }

    }


    /*To get the Funding Source Data
     * @param hmpProtocolData contains protocolNumber ,sequenceNumber
     * @throws Exception
     * @return actionForward
     */
    private void getFundingSourcesData(HashMap hmpProtocolData,
    HttpServletRequest request,ActionMapping mapping) throws Exception{
        WebTxnBean webTxnBean  = new WebTxnBean();
        Hashtable htFundingSourceTypes = (Hashtable)webTxnBean.getResults(request,"fundingSourceFrm",hmpProtocolData);
        HttpSession session = request.getSession();
        Vector vecFundingSrcTypes = (Vector) htFundingSourceTypes.get("getFundSourceTypes");

        /** Added for Coeus 4.3 Enhancement -Start
         *  Added fundingSourceTypeFlag for Customization, these flags can be set from CodeTables.
         *  filter if fundingSourceTypeFlag is Y
         */
        Vector vecFilterFundingSrcTypes = new Vector();
        if( vecFundingSrcTypes !=null && vecFundingSrcTypes.size()>0 ){
            for(int index=0; index < vecFundingSrcTypes.size(); index++ ){
                DynaValidatorForm dynaForm = (DynaValidatorForm) vecFundingSrcTypes.get(index);
                String fundingSourceTypeFlag = (String) dynaForm.get("fundingSourceTypeFlag");
                if(fundingSourceTypeFlag !=null && fundingSourceTypeFlag.equals("Y")){
                    vecFilterFundingSrcTypes.addElement(dynaForm);
                }
            }
        }
        session.setAttribute("fundingTypes",  vecFilterFundingSrcTypes);
        //Added for Coeus 4.3 Enhancement - End

        session.setAttribute("fundingSources",htFundingSourceTypes.get("getProtocolFundingSources"));
       // return mapping.findForward("fundingSource");
    }

    private String getParameterValue(HttpServletRequest request)throws Exception{
        Map hmParameter = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmParameter.put("parameterName", "DEFAULT_ACTIVITY_TYPE");
        Hashtable htParameterValue = (Hashtable)webTxnBean.getResults(request, "getActivityTypeForStubProp", hmParameter);
        hmParameter = (HashMap)htParameterValue.get("getActivityTypeForStubProp");
        String flag = (String)hmParameter.get("parameterValue");
        return flag;
    }

     /**
     * This Method is used to get the ProposalUserRoles
     * @param proposalNumber is used to get Users for this Proposal
     * @param unitNumber is used to get Users for this Unit Number
     * @param strMode is used to differentiate the mode like New or Modify/Display
     * @param loggedUserId is the logged in User Id
     * @return Vector UserRoleInfoBean
     * @exception Exception
     */
    public Vector getProposalUserRoles(HttpServletRequest request,String proposalNumber, String unitNumber, String strMode, String loggedUserId)
    throws Exception{
        HashMap hmProposalUserRoles = new HashMap();
        hmProposalUserRoles.put("unitNumber",unitNumber);
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecUsersRole = new Vector();
        Vector vecRolesBean = new Vector();
        Hashtable htUsersRoles = (Hashtable)webTxnBean.getResults(request, "getProposalUserRoles" , hmProposalUserRoles );
        Vector vecProposalRoles = (Vector)htUsersRoles.get("getProposalRoles");
        vecRolesBean = getRoleInfoBean(vecProposalRoles);

        vecUsersRole = (Vector)htUsersRoles.get("getUsersForRole");
        Hashtable htRoleId = getUserRoles(vecUsersRole);

        UserDetailsBean userDetailsBean = new UserDetailsBean();
        Vector roles = new Vector();
        Vector vecUserId = null;

        if ((vecRolesBean != null) && (vecRolesBean.size() >0)){
            int outerlength = vecRolesBean.size();
            //If new records get all Users for this Unit number

            for(int index1=0;index1<outerlength;index1++){
                RoleInfoBean roleInfoBean =
                (RoleInfoBean)vecRolesBean.elementAt(index1);
                int roleId = roleInfoBean.getRoleId();
                UserRolesInfoBean userRolesInfoBean = new UserRolesInfoBean();
                userRolesInfoBean.setRoleBean(roleInfoBean);
                //Get User Id vector for this Role Id
                vecUserId = (Vector)htRoleId.get(new Integer(roleId));

                if ((vecUserId != null) && (vecUserId.size() >0)){
                    //Check if the Logged in User Id not present in vector
                    //Only in case of Add mode and Aggregator role
                    if(roleId == CoeusConstants.PROPOSAL_ROLE_ID) {
                        boolean blnFoundUserId = false;
                        for(int userIndex = 0;userIndex<vecUserId.size();userIndex++) {
                            String userId = (String)vecUserId.elementAt(userIndex);
                            if(userId.equals(loggedUserId)) {
                                blnFoundUserId = true;
                                break;
                            }
                        }
                        if(blnFoundUserId == false) {
                            vecUserId.add(loggedUserId);
                        }
                    }
                }else{
                    //If new Mode and if the User does not exist,
                    //add Logged in User as Agregator
                    if( roleId == CoeusConstants.PROPOSAL_ROLE_ID){
                        vecUserId = new Vector();
                        vecUserId.add(loggedUserId);
                    }

                }
                if ((vecUserId != null) && (vecUserId.size() >0)){
                    int innerlength = vecUserId.size();
                    Vector users = new Vector();
                    ProposalUserRoleFormBean proposalUserRoleFormBean = null;
                    String userId = "";
                    for(int index2=0;index2<innerlength;index2++){
                        //In new mode vector contains String objects
                        userId = (String)vecUserId.elementAt(index2);
                        UserInfoBean userInfoBean =
                        userDetailsBean.getUserInfo(userId);
                        UserRolesInfoBean userRolesInfoBean2 = new UserRolesInfoBean();
                        userRolesInfoBean2.setUserBean(userInfoBean);
                        users.addElement(userRolesInfoBean2);
                    }//end inner for loop
                    userRolesInfoBean.setUsers(users);
                }//end of inner  if loop
                roles.addElement(userRolesInfoBean);
            }//end of outer for loop
        }//end of outer if loop
        return roles;
    }

       /**
     * prepare RoleInfoBean from the vector
     * @param vecProposalRoles containing dynaProposalRoles
     * @throws Exception
     * @return
     */
    private Vector getRoleInfoBean(Vector vecProposalRoles)throws Exception{
        Vector vecNewProposalRoles = new Vector();
        if(vecProposalRoles!=null && vecProposalRoles.size() > 0){
            for(int index = 0 ;index < vecProposalRoles.size(); index++){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecProposalRoles.get(index);
                RoleInfoBean roleInfoBean = new RoleInfoBean();
                BeanUtilsBean copyRoleBean = new BeanUtilsBean();
                roleInfoBean.setRoleId(Integer.parseInt((String)dynaForm.get("roleId")));
                roleInfoBean.setRoleName((String)dynaForm.get("roleName"));
                vecNewProposalRoles.addElement(roleInfoBean);
            }
        }
        return vecNewProposalRoles ;
    }

     /**This method is to get the user roles for unit number
     * @param vecUserRoles
     * @throws Exception
     * @return
     */
    private Hashtable getUserRoles(Vector vecUserRoles)throws Exception{
        Hashtable hstRoleId = new Hashtable();
        Vector vctUserIds = null ;
        String userId = EMPTY_STRING;
        int roleId = 0;
        Integer role = null ;
        if(vecUserRoles!=null && vecUserRoles.size() > 0){
            for(int index = 0; index < vecUserRoles.size();index++ ){
                DynaValidatorForm dynaUserForm = (DynaValidatorForm)vecUserRoles.get(index);
                userId = (String)dynaUserForm.get("userId");
                roleId = Integer.parseInt((String)dynaUserForm.get("roleId"));
                role = new Integer(roleId);
                if(!hstRoleId.containsKey(role)) {
                    vctUserIds = new Vector();
                    vctUserIds.add(userId);
                    hstRoleId.put(role,vctUserIds);
                }
                else {
                    vctUserIds = (Vector)hstRoleId.get(role);
                    vctUserIds.add(userId);
                    hstRoleId.put(role,vctUserIds);
                }
            }//End for
        }//End if
        return hstRoleId;
    }

    /**
     * this method is to update the osp$eps_proposal_roles table
     * @param vecProposalUserRoles
     * @param proposalNumber
     * @throws Exception
     */
    private void updateProposalUserRoles(HttpServletRequest request,Vector vecProposalUserRoles,String proposalNumber,
    HashMap roleMap)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecRole = null ;
        RoleInfoBean roleInfoBean = null ;
        Vector vecUsers = null ;
        if(vecProposalUserRoles!=null && vecProposalUserRoles.size() > 0){
            for(int index = 0 ; index < vecProposalUserRoles.size() ; index ++){
                UserRolesInfoBean userRolesInfoBean = (UserRolesInfoBean)vecProposalUserRoles.get(index);
                roleInfoBean = userRolesInfoBean.getRoleBean();
                roleMap.put("roleId",String.valueOf(roleInfoBean.getRoleId()));
                vecUsers = userRolesInfoBean.getUsers();
                if(vecUsers!=null && vecUsers.size() > 0){
                    for( int userIndex = 0; userIndex < vecUsers.size(); userIndex++){
                        UserRolesInfoBean userRolesInfoBean1 = (UserRolesInfoBean)vecUsers.get(userIndex);
                        UserInfoBean userInfoBean = userRolesInfoBean1.getUserBean();
                        roleMap.put("userId",userInfoBean.getUserId());
                        webTxnBean.getResults(request, "addUpdProposalRoles", roleMap );
                    }
                }
            }
        }
    }

}
