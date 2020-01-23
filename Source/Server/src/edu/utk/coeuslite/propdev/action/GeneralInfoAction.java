/*
 * GeneralInfoAction.java
 *
 * Created on April 20, 2006, 4:28 PM
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.propdev.bean.ProposalUserRoleFormBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import java.util.Map;

/**
 *
 * @author  vinayks
 */
public class GeneralInfoAction extends ProposalBaseAction {
    private static final String AC_TYPE_INSERT = "I";
    private static final String STATUS_CODE = "1";
    private static final String CREATION_STATUS_CODE = "1";
    //Removing instance variable case# 2960
//    private Timestamp dbTimestamp;
    private static final String DEFAULT_ORGANIZATION_ID = "DEFAULT_ORGANIZATION_ID";
    private static final String AC_TYPE_UPDATE="U";
    private static final String DEFAULT_CREATION_STATUS_DESC = "In Progress";
    //Added for Case#2406 - Proposal organizations and locations - start
    private static final String PROPOSAL_NUMBER = "proposalNumber";
    private static final String SITE_NUMBER = "siteNumber";
    private static final String LOCATION_NAME =  "locationName";
    private static final String LOC_TYPE_CODE = "locationTypeCode";
    private static final String ORG_ID = "organizationId";
    private static final String ROLODEX_ID = "rolodexId";
    private static final String UPD_TIME_STAMP = "updateTimestamp";
    private static final String UPD_USER = "updateUser";
    private static final String AW_UPD_TIME = "awUpdateTimeStamp";
    private static final String ACTYPE = "acType";
    private static final String UPD_PROPOSAL_SITES = "updProposalSites";
            
    private static final String CD_PROP_NUMBER = "proposalNumberCD";
    private static final String CD_SITE_NUMBER = "siteNumberCD";
    private static final String CONG_DIST = "congDistrict";
    private static final String CD_UPD_TIME = "updateTimeStampCD";
    private static final String CD_UPD_USER = "updateUserCD";
    private static final String CD_AW_CONG_DIST = "awCongDistrict";
    private static final String CD_AW_UPD_TIME = "awUpdateTimeStampCD";
    private static final String CD_ACTYPE = "acTypeCD";
    private static final String UPD_PROP_SITE_CONG_DIST = "updPropSiteCongDistrict";
    private static final String GET_NXT_SITE_NUM = "getNextSiteNumber";
    
    //Added for Case#2406 - Proposal organizations and locations - End
    /** Creates a new instance of GeneralInfoAction */
    public GeneralInfoAction() {
    }
    
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm,
    HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaForm = (DynaValidatorForm)actionForm;
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        DateUtils dtUtils = new DateUtils();
        String acType = (String)dynaForm.get("acType");
        String date=dynaForm.get("startDate").toString();
        date = dtUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
        dynaForm.set("startDate",date);
        date=dynaForm.get("endDate").toString();
        date = dtUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
        dynaForm.set("endDate",date);
        date=dynaForm.get("deadLineDate").toString();
        date = dtUtils.formatDate(date,":/.,|-","MM/dd/yyyy");
        dynaForm.set("deadLineDate",date);
        String cfdaCode = dynaForm.get("cfdaCode").toString();
        cfdaCode = cfdaCode.replaceAll("\\.","");
        dynaForm.set("cfdaCode", cfdaCode);
        
        if(acType!=null && !acType.equals(EMPTY_STRING)){
            if(acType.equals(AC_TYPE_INSERT)){
                addNewGeneralInfo(request,dynaForm);
            }else{
                // Check if lock exists or not
                LockBean lockBean = getLockingBean(userInfoBean, (String)dynaForm.get("proposalNumber"), request);
                
                boolean isLockExists = isLockExists(lockBean, lockBean.getModuleKey());
                LockBean lockData = getLockedData(CoeusLiteConstants.PROP_DEV_LOCK_STR+lockBean.getModuleNumber(), request);
                if(!isLockExists && lockBean.getSessionId().equals(lockData.getSessionId())) {
                    updateGeneralInfo(request,dynaForm);
                }else{
                    String errMsg = "release_lock_for";
                    ActionMessages messages = new ActionMessages();
                    messages.add("errMsg", new ActionMessage(errMsg,lockBean.getModuleKey(),lockBean.getModuleNumber()));
                    saveMessages(request, messages);
                }
            }
        }
        String budgetStatusCode = (String)dynaForm.get("budgetStatus");
        if(budgetStatusCode == null || budgetStatusCode.equals(EMPTY_STRING)){
            request.setAttribute("createBudget", "createBudget");
        }
        /**
         * get proposal the header details and set the bean in session
         */
        HashMap hmProposalHeader =  new HashMap();
        if(webTxnBean == null){
            webTxnBean = new WebTxnBean();
        }
        hmProposalHeader.put("proposalNumber",(String)dynaForm.get("proposalNumber"));
        Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getProposalHeaderData" , hmProposalHeader );
        Vector vecProposalHeader = (Vector)htProposalHeader.get("getProposalHeaderData");
        if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
            session.setAttribute("epsProposalHeaderBean", (EPSProposalHeaderBean)vecProposalHeader.elementAt(0));
        }
        readSavedStatus(request);        
        String proposalNumber = (String)dynaForm.get("proposalNumber");
        /*
         * commented because, currently there is no need for S2SSubmissionType checking 
        //COEUSQA-3951
                if(checkS2SSubmissionType(proposalNumber,request)){
                    request.setAttribute("isS2SSubTypIs_3","true");
                }
        //COEUSQA-3951         
         */
        //JIRA COEUSDEV 61 - START
        String cfda = (String)dynaForm.get("cfdaCode");
        String programAnnouncementNumber = (String)dynaForm.get("programAnnouncementNumber");
        String competitionId = (String)session.getAttribute("competitionId");
        //JIRA COEUSDEV-338 - START - END
        if(acType.equals(AC_TYPE_INSERT) && cfda!=null && cfda.length() > 0 && programAnnouncementNumber != null && programAnnouncementNumber.length() > 0) {
            //proposal created from GG
            return new ActionForward("/saveOpportunity.do?opportunityId="+programAnnouncementNumber+"&competitionId="+competitionId+"&proposalNumber="+proposalNumber+"&forward=generalInfo");
        }
        //JIRA COEUSDEV 61 - END
        return actionMapping.findForward("success");
//        response.sendRedirect("getGeneralInfo.do?proposalNumber="+proposalNumber);
//        return null;
    }
    
    /*This method is to add a new proposal  */
    /**
     * This method is to add new Proposal Information
     * @param dynaForm
     * @throws Exception
     */
    private void addNewGeneralInfo(HttpServletRequest request,DynaValidatorForm dynaForm) throws Exception{
        HttpSession session = request.getSession();
        //Modified for instance variable case#2960.
        Timestamp dbTimestamp;        
        WebTxnBean webTxnBean = new WebTxnBean();
        boolean isSavedRequired = true ;
        ActionMessages actionMessages = new ActionMessages();
        HashMap   hmRequiredDetails = new HashMap();
        //commented for proposal Organization - Start
        //hmRequiredDetails.put("defaultOrgId",DEFAULT_ORGANIZATION_ID);
        //commented for proposal Organization -end
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
        String createUser =  userInfoBean.getUserId();
        
        String unitNumber = (String)dynaForm.get("unitNumber");
        String unitName = (String)dynaForm.get("unitName");
        if(unitNumber ==null || unitNumber.equals(EMPTY_STRING)){
            unitNumber = (String)session.getAttribute("unitNumber");
            unitName = (String)session.getAttribute("unitName");
            dynaForm.set("unitNumber",unitNumber);
            dynaForm.set("unitName",unitName);
        }
        //Added for getting organisation id for fn_get_organisation_id start 1
        hmRequiredDetails.put("unitNumber",unitNumber);
        //Added for getting organisation id for fn_get_organisation_id end 1
        Hashtable htNextProposalNumber =
        (Hashtable)webTxnBean.getResults(request, "getProposalNumber", hmRequiredDetails);
        
        String proposalNumber =
        (String)((HashMap)htNextProposalNumber.get("getNextProposalNumber")).get("nextProposalNumber");
        
        Vector vecProposalUserRoles = getProposalUserRoles(request,proposalNumber, unitNumber, "N", userInfoBean.getUserId());
        /* commented for getting organisation id from fn_get_organisation_id
        String organizationId =
            (String)((HashMap)htNextProposalNumber.get("getDefaultOrganizationId")).get("ls_value");
         */
        //Added for getting organisation id for fn_get_organisation_id start 2
        String organizationId =
        (String)((HashMap)htNextProposalNumber.get("getOrganisationId")).get("organisationId");
        
        //added for organization page- start
        String acType = (String)dynaForm.get("acType");
        session.setAttribute("orgId",organizationId );
        Vector vecOrgNameAddr =  getOrganisationNameAddr(request,organizationId);
        String organizationName = (String)vecOrgNameAddr.get(0);
        //Modified for case#2406 - Proposal organizations and locations- Start
        String congDist = (String)vecOrgNameAddr.get(1);
        //Modified for case#2406 - Proposal organizations and locations - End
        String contactAddressId = (String)vecOrgNameAddr.get(2);
        organizationName = organizationName!= null?organizationName:EMPTY_STRING;
        contactAddressId = contactAddressId != null?contactAddressId:EMPTY_STRING;
        // added for organization page- end
        
        //Added for getting organisation id for fn_get_organisation_id end 2
        String subContractFlag = (String)dynaForm.get("subcontractFlag") ;
        if(subContractFlag == null ||subContractFlag.equals(EMPTY_STRING)){
            dynaForm.set("subcontractFlag","N");
        }
        //check the validity of award number, if not valid show the error message
        String awardNumber = (String)dynaForm.get("awardNumber");
        if(awardNumber!=null && !awardNumber.equals(EMPTY_STRING)){
            boolean isValidAward = isValidAwardNumber(request,awardNumber);
            if(!isValidAward){
                isSavedRequired = false ;
                actionMessages.add("isNotValidAward",
                new ActionMessage("generalInfoProposal.isNotValidAward"));
                saveMessages(request, actionMessages);
            }
        }
        
        //check the validity of sponsor Code, if not valid show the error message
        String primeSponsorCode = (String)dynaForm.get("primeSponsorCode");
        if(primeSponsorCode!=null && !primeSponsorCode.equals(EMPTY_STRING)){
            boolean isValidAward = isValidSponsorCode(request,primeSponsorCode);
            if(!isValidAward){
                isSavedRequired = false ;
                actionMessages.add("isNotValidPrimeSponsor",
                new ActionMessage("generalInfoProposal.isNotValidPrimeSponsor"));
                saveMessages(request, actionMessages);
            }else{
                String primeSponsorName = getSponsorDetails(request , primeSponsorCode);
                dynaForm.set("primeSponsorName" ,primeSponsorName);
            }
        }
        
        //Check the validity of the prime sponsor Code, if not show the error message
        String sponsorCode = (String)dynaForm.get("sponsorCode");
        if(sponsorCode!=null && !sponsorCode.equals(EMPTY_STRING)){
            boolean isValidSponsor = isValidSponsorCode(request,sponsorCode);
            if(!isValidSponsor){
                isSavedRequired = false ;
                actionMessages.add("isNotValidSponsor",
                new ActionMessage("generalInfoProposal.isNotValidSponsor"));
                saveMessages(request, actionMessages);
            }else{
                String sponsorName = getSponsorDetails(request , sponsorCode );
                dynaForm.set("sponsorName" ,sponsorName);
            }
        }
             
        //added for new field(Original Proposal Number) validation -- start -- nandakumar sn
        //Check the institute proposal number validity        
        String instPropCode = (String)dynaForm.get("continuedFrom");
        if(instPropCode != null && !instPropCode.equals(EMPTY_STRING)){
            boolean isValidInstProp = isValidInstPropCode(request,instPropCode);
            if(!isValidInstProp){
                isSavedRequired = false ;
                actionMessages.add("generalInfoProposal.error.invalidInstNo", new ActionMessage("generalInfoProposal.error.invalidInstNo"));
                saveMessages(request, actionMessages);
            }else{                
                dynaForm.set("continuedFrom", instPropCode);
            }
        }  
        //added for new field validation -- end
        
        if( dynaForm.get("sponsorProposalNuber") !=null && !dynaForm.get("sponsorProposalNuber").equals(EMPTY_STRING)){
            dynaForm.set("sponsorProposalNuber",dynaForm.get("sponsorProposalNuber").toString().trim());
        }
        //Commented for Case# 2406 - Proposal organizations and locations - start
        /*dynaForm.set("organizationId",organizationId);
        dynaForm.set("performingOrganizationId",organizationId);*/
        //Commented for Case# 2406 - Proposal organizations and locations - End
        dynaForm.set("proposalNumber",proposalNumber);
        dynaForm.set("statusCode",STATUS_CODE);
        dynaForm.set("creationStatusCode",CREATION_STATUS_CODE);
        dynaForm.set("templateFlag","N");
        dynaForm.set("intrCoopActivitiesFlag","N");
        dynaForm.set("otherAgencyFlag","N");
        dynaForm.set("durationMonth","0");
        dynaForm.set("mailingAddresId",null);
        dynaForm.set("acType",AC_TYPE_INSERT);
        dbTimestamp = prepareTimeStamp();
        dynaForm.set("updateTimestamp",dbTimestamp.toString());
        dynaForm.set("newUpdateTimetamp",dbTimestamp.toString());
        dynaForm.set("createUser" ,createUser);
        if(isSavedRequired){
            webTxnBean.getResults(request, "addUpdProposal" , dynaForm);
            request.setAttribute("dataSaved", "true");
            session.setAttribute("proposalNumber"+session.getId(), proposalNumber);
            session.setAttribute("createTimestamp",dbTimestamp.toString());
            request.setAttribute("updateTimestamp",dbTimestamp.toString());
            String updUser =  userInfoBean.getUserId();
            String updUserFullName = getUserName(request , updUser);
            session.setAttribute("updUser" , updUserFullName);
            updateProposalUserRoles(request,vecProposalUserRoles , proposalNumber ,dynaForm);
            
            // added for including Proposal Organization -Location - Start
            //Modified for case#2406 - Proposal organizations and locations - start
            updateProposalLocation(request,organizationId,
                    userInfoBean,organizationName,contactAddressId,
                    congDist,dynaForm );
            //Modified for case#2406 - Proposal organizations and locations - End
            // added for including Proposal Organization -Location - end
            
            //request.setAttribute("unitNumber", unitNumber);
            //request.setAttribute("unitName",unitName);
            request.setAttribute("creationStatDesc", DEFAULT_CREATION_STATUS_DESC);
            lockProposal(request,userInfoBean,proposalNumber);
            dynaForm.set("acType",AC_TYPE_UPDATE);
        }else{
            dynaForm.set("acType",AC_TYPE_INSERT);
        }
         //lockProposal(userInfoBean,proposalNumber); commented by noor
    }
    
    /**
     * This method is to update the proposal summary
     * @param dynaForm
     * @throws Exception
     */
    private void updateGeneralInfo(HttpServletRequest request,DynaValidatorForm dynaForm)throws Exception{
        //Modified for instance variable case#2960.
        Timestamp dbTimestamp;        
        dbTimestamp = prepareTimeStamp();
        WebTxnBean webTxnBean = new WebTxnBean();
        HttpSession session = request.getSession();
        boolean isSavedRequired = true ;
        ActionMessages actionMessages = new ActionMessages();
        
        //check the validity of award number, if not valid show the error message
        String awardNumber = (String)dynaForm.get("awardNumber");
        if(awardNumber!=null && !awardNumber.equals(EMPTY_STRING)){
            boolean isValidAward = isValidAwardNumber(request,awardNumber);
            if(!isValidAward){
                isSavedRequired = false ;
                actionMessages.add("isNotValidAward",
                new ActionMessage("generalInfoProposal.isNotValidAward"));
                saveMessages(request, actionMessages);
            }
        }
        //check the validity of sponsor Code, if not valid show the error message
        String sponsorCode = (String)dynaForm.get("sponsorCode");
        if(sponsorCode!=null && !sponsorCode.equals(EMPTY_STRING)){
            boolean isValidSponsor = isValidSponsorCode(request,sponsorCode);
            if(!isValidSponsor){
                isSavedRequired = false ;
                actionMessages.add("isNotValidSponsor",
                new ActionMessage("generalInfoProposal.isNotValidSponsor"));
                saveMessages(request, actionMessages);
            }else{
                String sponsorName = getSponsorDetails(request , sponsorCode );
                dynaForm.set("sponsorName" ,sponsorName);
            }
        }else{
            dynaForm.set("sponsorName" ,EMPTY_STRING);
        }
        
        //Check the validity of the prime sponsor Code, if not show the error message
        String primeSponsorCode = (String)dynaForm.get("primeSponsorCode");
        if(primeSponsorCode!=null && !primeSponsorCode.equals(EMPTY_STRING)){
            boolean isValidAward = isValidSponsorCode(request,primeSponsorCode);
            if(!isValidAward){
                isSavedRequired = false ;
                actionMessages.add("isNotValidPrimeSponsor",
                new ActionMessage("generalInfoProposal.isNotValidPrimeSponsor"));
                saveMessages(request, actionMessages);
            }else{
                String primeSponsorName = getSponsorDetails(request , primeSponsorCode);
                dynaForm.set("primeSponsorName" ,primeSponsorName);
            }
        }else{
            dynaForm.set("primeSponsorName" ,EMPTY_STRING);
        }
        
        
        //added for new field(Original Proposal Number) validation -- start -- nandakumar sn
        //Check the institute proposal number validity        
        String instPropCode = (String)dynaForm.get("continuedFrom");
        if(instPropCode != null && !instPropCode.equals(EMPTY_STRING)){
            boolean isValidInstProp = isValidInstPropCode(request,instPropCode);
            if(!isValidInstProp){
                isSavedRequired = false ;
                actionMessages.add("generalInfoProposal.error.invalidInstNo", new ActionMessage("generalInfoProposal.error.invalidInstNo"));
                saveMessages(request, actionMessages);
            }else{                
                dynaForm.set("continuedFrom", instPropCode);
            }
        }  
        //added for new field validation -- end
        
        String subContractFlag = (String)dynaForm.get("subcontractFlag") ;
        if(subContractFlag == null ||subContractFlag.equals(EMPTY_STRING)){
            dynaForm.set("subcontractFlag","N");
        }
        if( dynaForm.get("sponsorProposalNuber") !=null && !dynaForm.get("sponsorProposalNuber").equals(EMPTY_STRING)){
            dynaForm.set("sponsorProposalNuber",dynaForm.get("sponsorProposalNuber").toString().trim());
        }
        dynaForm.set("durationMonth","0");
        dynaForm.set("mailingAddresId",null);
        dynaForm.set("newUpdateTimetamp",dbTimestamp.toString());
        dynaForm.set("acType",AC_TYPE_UPDATE);
        String proposalNumber = (String)dynaForm.get("proposalNumber");
        if(isSavedRequired){
            webTxnBean.getResults(request, "addUpdProposal" , dynaForm);
            request.setAttribute("updateTimestamp",dbTimestamp.toString());
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            String updUser =  userInfoBean.getUserId();
            String updUserFullName = getUserName(request , updUser);
            session.setAttribute("updUser" , updUserFullName);
            //request.setAttribute("unitNumber", unitNumber);
            //request.setAttribute("unitName",unitName);
            dynaForm.set("updateTimestamp",dbTimestamp.toString());
            // Update the proposal hierarchy sync flag
            updateProposalSyncFlags(request, proposalNumber);
        }
        request.setAttribute("updateTimestamp",dynaForm.get("updateTimestamp").toString());
        //        session.setAttribute("proposalNumber"+session.getId(), proposalNumber);
        session.setAttribute("proposalNumber"+session.getId(), proposalNumber);
    }
    
    
    /**
     * This method is to check whether the entered award number is valid or not
     * @param awardNumber
     * @throws Exception
     * @return
     */
    private boolean isValidAwardNumber(HttpServletRequest request,String awardNumber) throws Exception{
        HashMap hmAwardNumber = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmAwardNumber.put("sponsorAwdNumber",awardNumber);
        Hashtable htAwardNumber =
        (Hashtable)webTxnBean.getResults(request, "isValidAwardNumber" , hmAwardNumber);
        HashMap hmValidAward = (HashMap)htAwardNumber.get("isValidAwardNumber");
        int awardNo = Integer.parseInt(hmValidAward.get("isValid").toString());
        if(awardNo == 1){
            return true ;
        }else {
            return false ;
        }
    }
    
    
    /**
     * This method is to check whether the entered sponsor number is valid or not
     * @param sponsorCode
     * @throws Exception
     * @return
     */
    private boolean isValidSponsorCode(HttpServletRequest request,String sponsorCode) throws Exception{
        HashMap hmSponsorCode = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmSponsorCode.put("sponsorCode" , sponsorCode);
        Hashtable htSponsorCode =
        (Hashtable)webTxnBean.getResults(request, "isValidSponsorCode" , hmSponsorCode );
        HashMap hmValidSponsor = (HashMap)htSponsorCode.get("isValidSponsorCode");
        String isValidSponsor = (String)hmValidSponsor.get("isValid");
        if(isValidSponsor!=null && isValidSponsor.equals(sponsorCode)){
            return true ;
        }else{
            return false ;
        }
    }
    
    
    /**
     * Added for validation of Original Proposal Number
     * This method checks whether the user has entered the Institute proposal number correctly.
     */    
    private boolean isValidInstPropCode(HttpServletRequest request,String instPropCode) throws Exception{
        
        boolean valid = true;        
        Map hmNumber  = new HashMap();
        String count;        
        WebTxnBean webTxnBean = new WebTxnBean();                
        //Execute the stored function fn_is_valid_inst_prop_num to get the count
        hmNumber.put("proposalNumber", instPropCode);
        hmNumber =(Hashtable)webTxnBean.getResults(request,"checkInstPropNumber",hmNumber);
        hmNumber = (HashMap)hmNumber.get("checkInstPropNumber");
        count = (String)hmNumber.get("ll_count");
        if(Integer.parseInt(count) < 1){
            valid = false;
        }
        return valid;
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
    DynaValidatorForm dynaForm)throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector vecRole = null ;
        RoleInfoBean roleInfoBean = null ;
        Vector vecUsers = null ;
        if(vecProposalUserRoles!=null && vecProposalUserRoles.size() > 0){
            for(int index = 0 ; index < vecProposalUserRoles.size() ; index ++){
                UserRolesInfoBean userRolesInfoBean = (UserRolesInfoBean)vecProposalUserRoles.get(index);
                roleInfoBean = userRolesInfoBean.getRoleBean();
                dynaForm.set("roleId",String.valueOf(roleInfoBean.getRoleId()));
                vecUsers = userRolesInfoBean.getUsers();
                if(vecUsers!=null && vecUsers.size() > 0){
                    for( int userIndex = 0; userIndex < vecUsers.size(); userIndex++){
                        UserRolesInfoBean userRolesInfoBean1 = (UserRolesInfoBean)vecUsers.get(userIndex);
                        UserInfoBean userInfoBean = userRolesInfoBean1.getUserBean();
                        dynaForm.set("userId",userInfoBean.getUserId());
                        webTxnBean.getResults(request, "addUpdProposalRoles", dynaForm );
                    }
                }
            }
        }
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
    
    private void lockProposal(HttpServletRequest request,UserInfoBean userInfoBean, String proposalNumber) throws Exception{
        HttpSession session = request.getSession();
        LockBean lockBean = getLockingBean(userInfoBean,proposalNumber,request);
        session.removeAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        session.setAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId(), lockBean);
        session.setAttribute(CoeusLiteConstants.RECORD_LOCKED+session.getId(),
        new Boolean(true));
        lockModule(lockBean, request);
    }
    
    /**
     * This method is used to get the Organization Name and contact Address Id
     * based on the organization Id
     * @param organizationId
     * @throws Exception
     * @return Vector containing organization name,contactAddressId 
     */
    private Vector getOrganisationNameAddr(HttpServletRequest request,String organizationId) throws Exception{
        
        HashMap hmOrgAddr = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        hmOrgAddr.put("organizationId", organizationId);
        Hashtable htOrgNameAddr = (Hashtable)webTxnBean.getResults(request, "getOrganizationName", hmOrgAddr);
        HashMap hmOrg = (HashMap)htOrgNameAddr.get("getOrganizationName");
        Vector vecOrgNameAddr = new Vector();
        vecOrgNameAddr.add(0, (String)hmOrg.get("as_organization_name"));
        vecOrgNameAddr.add(1, (String)hmOrg.get("as_congressional_district"));
        vecOrgNameAddr.add(2,(String)hmOrg.get("as_contact_address_id"));
        return vecOrgNameAddr;
    }
    
    /**
     * this method is used for updating the Proposal
     * Location details in the OSP$EPS_PROPOSAL table
     * @param dynaForm
     * @param request
     * @param organizationName
     * @param contactAddressId
     * @throws Exception
     */    
   private void updateProposalLocation(HttpServletRequest request,
           String organizationId, UserInfoBean userInfoBean,
           String organizationName, String contactAddressId ,
           String congDist,DynaValidatorForm dynaForm) throws Exception{
       //Modified for instance variable case#2960.
       //Modified for case#2406 - Proposal organizations and locations - start
       Timestamp dbTimestamp;        
       dbTimestamp = prepareTimeStamp();       
//       ProposalDevelopmentUpdateTxnBean proposalDevelopmentUpdateTxnBean = new ProposalDevelopmentUpdateTxnBean();
//       ProposalLocationFormBean proposalLocationFormBean = new ProposalLocationFormBean();
       String proposalNumber = (String)dynaForm.get(PROPOSAL_NUMBER);
       String updateUser = (String)dynaForm.get("createUser");
       String acType =  (String)dynaForm.get(ACTYPE);
//       proposalLocationFormBean.setProposalNumber(proposalNumber);
//       proposalLocationFormBean.setProposalLocation(organizationName);
       String roloId = EMPTY_STRING;
       String userId =  userInfoBean.getUserId();
       int rolodexId = roloId !=null && !roloId.equals(EMPTY_STRING) ? Integer.parseInt(roloId):0;
//       proposalLocationFormBean.setRolodexId(rolodexId);
//       proposalLocationFormBean.setUpdateTimestamp(dbTimestamp);
//       proposalLocationFormBean.setUpdateUser(userId);
//       proposalLocationFormBean.setAcType(TypeConstants.INSERT_RECORD);
//       proposalDevelopmentUpdateTxnBean.addUpdateProposalLocations(proposalLocationFormBean);
       //Modified for case#2406 - Proposal organizations and locations - End
       HashMap hmPropSite = new HashMap();
       hmPropSite.put(PROPOSAL_NUMBER,proposalNumber);
       int siteNumberPropOrg = -1;
       int siteNumPerfOrg = -1;
       WebTxnBean propOrgWebTxnBean = new WebTxnBean();
       Hashtable htOrgDetails = (Hashtable)propOrgWebTxnBean.getResults(request,GET_NXT_SITE_NUM,hmPropSite);
       hmPropSite = (HashMap)htOrgDetails.get(GET_NXT_SITE_NUM);
       if(hmPropSite != null){
           siteNumberPropOrg = Integer.parseInt(hmPropSite.get("ll_site_number").toString()) ;
       }
       if(siteNumberPropOrg != -1 ){
           HashMap  hmPropOrg = new HashMap();
           
           hmPropOrg.put(PROPOSAL_NUMBER,proposalNumber);
           hmPropOrg.put(SITE_NUMBER,new Integer(siteNumberPropOrg));
           hmPropOrg.put(LOCATION_NAME,organizationName);
           hmPropOrg.put(LOC_TYPE_CODE,new Integer(1));
           hmPropOrg.put(ORG_ID,organizationId);
           hmPropOrg.put(ROLODEX_ID,contactAddressId);
           hmPropOrg.put(UPD_TIME_STAMP,dbTimestamp);
           hmPropOrg.put(UPD_USER,updateUser);
           hmPropOrg.put(AW_UPD_TIME,dbTimestamp);
           hmPropOrg.put(ACTYPE,TypeConstants.INSERT_RECORD);
           propOrgWebTxnBean.getResults(request,UPD_PROPOSAL_SITES,hmPropOrg);

           if(congDist != null){
               HashMap hmCongDist = new HashMap();
               hmCongDist.put(CD_PROP_NUMBER,proposalNumber);
               hmCongDist.put(CD_SITE_NUMBER,new Integer(siteNumberPropOrg));
               hmCongDist.put(CONG_DIST,congDist);
               hmCongDist.put(CD_UPD_TIME,dbTimestamp);
               hmCongDist.put(CD_UPD_USER,updateUser);
               hmCongDist.put(CD_AW_CONG_DIST,congDist);
               hmCongDist.put(CD_AW_UPD_TIME,dbTimestamp);
               hmCongDist.put(CD_ACTYPE,TypeConstants.INSERT_RECORD);
               propOrgWebTxnBean.getResults(request,UPD_PROP_SITE_CONG_DIST,hmCongDist);
           }
       }
       HashMap hmPerfOrg = new HashMap();
       hmPerfOrg.put(PROPOSAL_NUMBER,proposalNumber);
       htOrgDetails = (Hashtable)propOrgWebTxnBean.getResults(request,GET_NXT_SITE_NUM,hmPerfOrg);
       hmPerfOrg = (HashMap)htOrgDetails.get(GET_NXT_SITE_NUM);
       if(hmPerfOrg != null){
           siteNumPerfOrg = Integer.parseInt(hmPerfOrg.get("ll_site_number").toString()) ;
       }
       if(siteNumPerfOrg != -1){
           HashMap  hmPropOrg = new HashMap();

           hmPropOrg.put(PROPOSAL_NUMBER,proposalNumber);
           hmPropOrg.put(SITE_NUMBER,new Integer(siteNumPerfOrg));
           hmPropOrg.put(LOCATION_NAME,organizationName);
           hmPropOrg.put(LOC_TYPE_CODE,new Integer(2));
           hmPropOrg.put(ORG_ID,organizationId);
           hmPropOrg.put(ROLODEX_ID,contactAddressId);
           hmPropOrg.put(UPD_TIME_STAMP,dbTimestamp);
           hmPropOrg.put(UPD_USER,updateUser);
           hmPropOrg.put(AW_UPD_TIME,dbTimestamp);
           hmPropOrg.put(ACTYPE,TypeConstants.INSERT_RECORD);
           propOrgWebTxnBean.getResults(request,UPD_PROPOSAL_SITES,hmPropOrg);

           if(congDist != null){
               HashMap hmCongDist = new HashMap();
               hmCongDist.put(CD_PROP_NUMBER,proposalNumber);
               hmCongDist.put(CD_SITE_NUMBER,new Integer(siteNumPerfOrg));
               hmCongDist.put(CONG_DIST,congDist);
               hmCongDist.put(CD_UPD_TIME,dbTimestamp);
               hmCongDist.put(CD_UPD_USER,updateUser);
               hmCongDist.put(CD_AW_CONG_DIST,congDist);
               hmCongDist.put(CD_AW_UPD_TIME,dbTimestamp);
               hmCongDist.put(CD_ACTYPE,TypeConstants.INSERT_RECORD);
               propOrgWebTxnBean.getResults(request,UPD_PROP_SITE_CONG_DIST,hmCongDist);
           }
       }
 }
}  