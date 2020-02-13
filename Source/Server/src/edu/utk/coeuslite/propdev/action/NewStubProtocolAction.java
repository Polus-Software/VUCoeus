/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.propdev.bean.ProposalLeadUnitFormBean;
import edu.mit.coeus.user.bean.UserMaintDataTxnBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.locking.LockingTxnBean;
import edu.mit.coeuslite.iacuc.action.ProtocolBaseAction;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.LockBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author indhulekha
 */
public class NewStubProtocolAction extends ProtocolBaseAction {
    
    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String GET_LOCK_IDS = "getLockIds";
    private static final String LOCK_IDS_LIST = "lockIdsList";
    private static final String UPDATE_USER = "updateUser";
    private static final String LOCK_ID = "lockId";
    private static final String MODULE = "module";
    private static final String ITEM = "item";
    private static final String USER = "user";
    
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
    public ActionForward performExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception{

        if(mapping.getPath().equals("/newStubProtocol")) {

            String proposalNum = request.getParameter("proposalNumber").toString();
            
            proposalNum = proposalNum.trim();
            WebTxnBean webTxnBean = new WebTxnBean();
            HashMap hmData = new HashMap();
            hmData.put("AW_PROPOSAL_NUMBER", proposalNum);
            hmData.put("AV_PROPOSAL_NUMBER", proposalNum);
            hmData.put("AS_PROPOSAL_NUMBER", proposalNum);

            Hashtable validateList = (Hashtable) webTxnBean.getResults(request, "getValidateFields",hmData);
            HashMap validateMap=  (HashMap) validateList.get("getValidateFields");

            if(validateMap!=null && validateMap.size() > 0){
               String isValid = validateMap.get("isValid").toString();
                if(isValid.equals("true")) {
                //  request.setAttribute("streamProtocol", "true");
                  createNewStubProtocol(mapping,form,request,response);
                    
                    return mapping.findForward("specialReview");
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
        }
        
        return mapping.findForward(SUCCESS);
    }

    @Override
    public void cleanUp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void createNewStubProtocol(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

       UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute(USER+session.getId());
        String userId = (String)userInfoBean.getUserId().toUpperCase();

         PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
        String discPrsnId = person.getPersonID();


        String proposalNum = request.getParameter("proposalNumber").toString();
            request.getSession().removeAttribute("validation");

            proposalNum = proposalNum.trim();
            WebTxnBean webTxnBean = new WebTxnBean();
            HashMap hmData = new HashMap();
            hmData.put("proposalNum", proposalNum);

            String title = "";
            String spnsrCode = "";
            String invstigatorId = "";
            String invstigatorName = "";
            String invstigatorFlag = "";
            String unitPersonId = "";
            String unitNumber = "";
            boolean unitFlag = false;
            String nonEmployee = "";
            String createdUser = userId;

          try{
            Hashtable htProposalHeader = (Hashtable)webTxnBean.getResults(request, "getPropDetails" , hmData );
            HashMap vecProposalHeader = (HashMap)htProposalHeader.get("getPropDetails");

            if(vecProposalHeader != null && vecProposalHeader.size() > 0) {
                title = (String)vecProposalHeader.get("TITLE");
                spnsrCode = (String)vecProposalHeader.get("SPONSOR_CODE");                
            }

             HashMap inputMap = new HashMap();
            inputMap.put("proposalNumber", proposalNum);

            String updateUser = userId;
            Date updateTimestamp = new Date();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(updateTimestamp.getTime());
            
            Hashtable htProtocolNumber = (Hashtable)webTxnBean.getResults(request, "generateProtocolNumber", null);
            HashMap mappedData = (HashMap)htProtocolNumber.get("generateProtocolNumber");
            String newProtocolNumber = (String)mappedData.get("protocolNumber");

            HashMap protocolData = new HashMap();
            protocolData.put("protocolNumber", newProtocolNumber);
            protocolData.put("sequenceNumber", 1);
            protocolData.put("title", title);
            protocolData.put("protocolTypeCode", 1);
            protocolData.put("protocolStatusCode", 100);
            protocolData.put("createUser", createdUser);
            protocolData.put("fundingSourceIndicator", "P1");
            protocolData.put("specialReviewIndicator", "P1");
            protocolData.put("applicationDate", updateTimestamp);
            protocolData.put("createTimestamp", timestamp);
            protocolData.put("PRE_DEFINED", updateUser);
            protocolData.put("newUpdateTimestamp", timestamp);
            protocolData.put("updateTimestamp", timestamp);
            protocolData.put("acType", "I");
            protocolData.put("vulnerableSubjectIndicator", "N0");
            protocolData.put("keyStudyPersonIndicator", "N0");
            protocolData.put("correspondantIndicator", "N0");
            protocolData.put("referenceIndicator", "N0");
            protocolData.put("relatedProjectIndicator", "N0");
            protocolData.put("isBillable", false);

            Hashtable htProtocol = (Hashtable)webTxnBean.getResults(request, "updProtocol", protocolData);

            request.setAttribute("streamProtocol", "true");

            HashMap roleMap = new HashMap();
            roleMap.put("protocolNumber", newProtocolNumber);
            roleMap.put("sequenceNumber", 1);
            roleMap.put("userId", updateUser);
            roleMap.put("PRE_DEFINED", updateUser);
            roleMap.put("updateTimetamp", timestamp);
            roleMap.put("acType", "I");

             updateProtocolUserRoles(request, roleMap);

            Hashtable htPropOrg = (Hashtable)webTxnBean.getResults(request, "getProposalSites" , inputMap );
            Vector vecPropOrg = (Vector)htPropOrg.get("getProposalSites");
            
            int orgsTypeCode = 0;
            String orgsId = "";
            int rolodexId = 0;

            if(vecPropOrg != null && vecPropOrg.size() > 0) {
                for(int i = 0; i < vecPropOrg.size(); i++) {
                    DynaValidatorForm dynaForm = (DynaValidatorForm) vecPropOrg.get(i);
                    orgsTypeCode = Integer.parseInt(dynaForm.get("locationTypeCode").toString());
                    orgsId = (String)dynaForm.get("organizationId");
                    rolodexId = Integer.parseInt(dynaForm.get("rolodexId").toString());

                    HashMap orgsMap = new HashMap();
                    orgsMap.put("protocolNumber", newProtocolNumber);
                    orgsMap.put("sequenceNumber", 1);
                    orgsMap.put("protocolOrgTypeCode", String.valueOf(orgsTypeCode));
                    orgsMap.put("organizationId", orgsId);
                    orgsMap.put("rolodexId", String.valueOf(rolodexId));
                    orgsMap.put("PRE_DEFINED", updateUser);
                    orgsMap.put("locationUpdateTimestamp", timestamp);
                    orgsMap.put("orgAcType", "I");


                    Hashtable htProtoOrg = (Hashtable)webTxnBean.getResults(request, "updProtoOrganizations", orgsMap);
                }
            }

            if(spnsrCode != null && spnsrCode.trim().length() > 0) {
                HashMap protocolFunding = new HashMap();
                protocolFunding.put("protocolNum", newProtocolNumber);
                protocolFunding.put("sequenceNumber", "1");
                protocolFunding.put("fundingType", "1");
                protocolFunding.put("fundingSource", spnsrCode);

                Hashtable htProtocolFunding = (Hashtable)webTxnBean.getResults(request, "insert_stub_protocol_funding", protocolFunding);
            }

            Hashtable htProposalInvst = (Hashtable)webTxnBean.getResults(request, "getProposalInvestigatorList" , inputMap );
            Vector vecProposalInvst = (Vector)htProposalInvst.get("getProposalInvestigatorList");

            if(vecProposalInvst != null && vecProposalInvst.size() > 0) {
                for(int i = 0; i < vecProposalInvst.size(); i++) {
                    DynaValidatorForm dynaForm = (DynaValidatorForm) vecProposalInvst.get(i);
                   invstigatorId = dynaForm.getString("personId");
                   invstigatorName = dynaForm.getString("personName");
                   invstigatorFlag = dynaForm.getString("principalInvestigatorFlag");
                   nonEmployee = dynaForm.getString("facultyFlag");
                   
                   if(nonEmployee == null) {
                       nonEmployee = "N";
                   }

                   HashMap protocolInvst = new HashMap();
                    protocolInvst.put("protocolNumber", newProtocolNumber);
                    protocolInvst.put("sequenceNumber", "1");
                    protocolInvst.put("personId", invstigatorId);
                    protocolInvst.put("personName", invstigatorName);
                    protocolInvst.put("principleInvestigatorFlag", invstigatorFlag);
                    protocolInvst.put("nonEmployeeFlag", nonEmployee);
                    protocolInvst.put("affiliationTypeCode", "1");
                    protocolInvst.put("PRE_DEFINED", updateUser);
                    protocolInvst.put("updateTimestamp", timestamp);
                     protocolInvst.put("acType", "I");
                    protocolInvst.put("awUpdateTimestamp", timestamp);
   
                    Hashtable htProtocolInvst = (Hashtable)webTxnBean.getResults(request, "updateInvestigatorDatas", protocolInvst);
                }
            }

//             HashMap unitInput = new HashMap();
//            unitInput.put("proposalNumber", proposalNum);
//            unitInput.put("personId", discPrsnId);

             Hashtable htProposalUnit = (Hashtable)webTxnBean.getResults(request, "get_prop_units_for_stub_proto" , hmData );
            Vector vecProposalUnit = (Vector)htProposalUnit.get("get_prop_units_for_stub_proto");

            if(vecProposalUnit != null && vecProposalUnit.size() > 0) {
                for(int i = 0; i < vecProposalUnit.size(); i++) {
                    DynaValidatorForm dynaForm = (DynaValidatorForm) vecProposalUnit.get(i);
                    unitPersonId = dynaForm.getString("personId");
                    unitNumber = dynaForm.getString("unitNumber");
                    String flag = dynaForm.getString("leadUnit");

                    if(flag.equals("Y")) {
                         unitFlag = true;
                    }
                    else {
                        unitFlag = false;
                    }


                    HashMap protocolUnits = new HashMap();
                    protocolUnits.put("protocolNumber", newProtocolNumber);
                    protocolUnits.put("sequenceNumber", "1");
                    protocolUnits.put("personId", unitPersonId);
                    protocolUnits.put("departmentNumber", unitNumber);
                    protocolUnits.put("principleInvestigatorFlag", unitFlag);
                    protocolUnits.put("PRE_DEFINED", updateUser);
                    protocolUnits.put("updateTimestamp", timestamp);
                    protocolUnits.put("awUnitUpdateTimestamp", timestamp);
                    protocolUnits.put("acType", "I");


                    Hashtable htProtocolUnits = (Hashtable)webTxnBean.getResults(request, "updateInvestigatorUnitsDatas", protocolUnits);
                }
            }

            HashMap rvhmData = new HashMap();
            rvhmData.put("proposalNum", proposalNum);
             Hashtable htReviewNum = (Hashtable)webTxnBean.getResults(request, "getSpecialReviewNum" , rvhmData );
            HashMap vecReviewNum = (HashMap)htReviewNum.get("getSpecialReviewNum");

            String splRvNum = "";
            if(vecReviewNum != null && vecReviewNum.size() >0) {
                if(vecReviewNum.get("REVIEW_NUMBER") == null) {
                    splRvNum = "1";
                }
                else {
                    splRvNum = vecReviewNum.get("REVIEW_NUMBER").toString();
                    int num = Integer.parseInt(splRvNum);
                    num++;
                    splRvNum = String.valueOf(num);
                }
            }

            HashMap specialReview = new HashMap();
            specialReview.put("proposalNum", proposalNum);
            specialReview.put("spReviewNum", splRvNum);
            specialReview.put("spReviewCode", "1");
            specialReview.put("approvalTypeCode", "1");
            specialReview.put("protocolNum", newProtocolNumber);

            Hashtable htProtocolSpclRview = (Hashtable)webTxnBean.getResults(request, "insert_stub_protocol_special_review", specialReview);

            Vector htPropDevSpRev =(Vector) request.getSession().getAttribute("pdReviewList");

                    int splRvNumber = 0;
                    int splRvCode = 0;
                    int approvalTypeCode = 0;
                    String splRvProtoNum = "";
                    String applDate ="";
                    String approvalDate = "";
                    String comments = "";
                    DateUtils dateUtils = new DateUtils();

            if(htPropDevSpRev!= null && htPropDevSpRev.size() >0){
                for(int i=0;i<htPropDevSpRev.size();i++){
                    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)htPropDevSpRev.get(i);
                     splRvNumber = Integer.parseInt(dynaValidatorForm.get("specialReviewNumber").toString());
                     splRvCode = Integer.parseInt(dynaValidatorForm.get("specialReviewCode").toString());
                     approvalTypeCode = Integer.parseInt(dynaValidatorForm.get("approvalCode").toString());
                     splRvProtoNum = (String)dynaValidatorForm.get("spRevProtocolNumber");
                     applDate = (String)dynaValidatorForm.get("applicationDate");
                     approvalDate = (String)dynaValidatorForm.get("approvalDate");
                     comments = (String)dynaValidatorForm.get("comments");

                     applDate = dateUtils.formatDate(applDate, ":/.,|-", "MM/dd/yyyy");
                     approvalDate = dateUtils.formatDate(approvalDate, ":/.,|-", "MM/dd/yyyy");

                    HashMap specialReviewProto = new HashMap();
                    specialReviewProto.put("protocolNumber", newProtocolNumber);
                    specialReviewProto.put("sequenceNumber", "1");
                    specialReviewProto.put("specialReviewNumber", String.valueOf(splRvNumber));
                    specialReviewProto.put("specialReviewCode", String.valueOf(splRvCode));
                    specialReviewProto.put("approvalCode", String.valueOf(approvalTypeCode));
                    specialReviewProto.put("spRevProtocolNumber", splRvProtoNum);
                    if(applDate != null) {
                        specialReviewProto.put("applicationDate", new Date(applDate));
                    }
                    if(approvalDate != null) {
                        specialReviewProto.put("approvalDate", new Date(approvalDate));
                    }
                    specialReviewProto.put("comments", comments);
                    specialReviewProto.put("PRE_DEFINED", updateUser);
                    specialReviewProto.put("updateTimestamp", timestamp);
                    specialReviewProto.put("awUpdateTimestamp", timestamp);
                    specialReviewProto.put("acType", "I");
           

                    Hashtable htProtoSpclRview = (Hashtable)webTxnBean.getResults(request, "updProtoSpecialReview", specialReviewProto);
                }
            }

          }catch(Exception ex) {
              request.setAttribute("streamProtocol", "false");
               UtilFactory.log("FROM COEUS LITE : "+ex.getMessage(),ex,"NewStubProtocolAction","createNewStubProtocol");
          }

    }

        private void updateProtocolUserRoles(HttpServletRequest request, HashMap roleMap)throws Exception{
            HashMap hmUserRoles = new HashMap();
            WebTxnBean webTxnBean = new WebTxnBean();
            Timestamp dbTimestamp = prepareTimeStamp();
            HttpSession session = request.getSession();
            hmUserRoles.put("protocolNumber", roleMap.get("protocolNumber"));
            hmUserRoles.put("sequenceNumber", roleMap.get("sequenceNumber"));
            UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
            hmUserRoles.put("userId", userInfoBean.getUserId());
            hmUserRoles.put("roleId", new Integer(CoeusLiteConstants.PROTOCOL_COORDINATOR_ID));
            hmUserRoles.put("updateTimetamp",dbTimestamp.toString());
            hmUserRoles.put("acType",roleMap.get("acType"));
            webTxnBean.getResults(request, "updProtocolUserRoles", hmUserRoles);
        }
}
