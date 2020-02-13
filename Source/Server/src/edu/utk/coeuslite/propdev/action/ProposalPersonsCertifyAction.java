//investigator and key person S T A R T
package edu.utk.coeuslite.propdev.action;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Map;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.utk.coeuslite.propdev.bean.PersonCertifyInfoBean;

//new edit 12th jan 2011 end

/**
 * @author  athul
 *
 * To initiate response to a user request to display information about a given proposal.
 */
public class ProposalPersonsCertifyAction extends ProposalBaseAction   {
    private static final String USER ="user"; 
    private static final String PROPOSAL_NUMBER="proposalNumber";
    private static String navigate;

    /** Creates a new instance of DisplayProposalAction */
    public ProposalPersonsCertifyAction() {
    }

    public ActionForward performExecute(
    ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception{        
        navigate = "success";
        PersonCertifyInfoBean personCertifyInfoBean = (PersonCertifyInfoBean)request.getSession().getAttribute("personCertifyInfoBean");
        if(personCertifyInfoBean==null){
          personCertifyInfoBean =new PersonCertifyInfoBean();
        } 
        HttpSession session= request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER+session.getId());
        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute(SessionConstants.LOGGED_IN_PERSON); 
        String userId = userInfoBean.getUserId();
        String personId=request.getParameter("personId");
        if(personId!=null){
           personCertifyInfoBean.setPersonId(personId);
        }
        else{
            personId = personCertifyInfoBean.getPersonId();
        }
        String proposalNo = request.getParameter("proposalNo");
        if(proposalNo!=null){
            personCertifyInfoBean.setProposalNumber(proposalNo);        
        }
        else{
            proposalNo=(String)personCertifyInfoBean.getProposalNumber();
        }        
          String prop_persName=userInfoBean.getUserName();
          session.setAttribute("prop_persName", prop_persName);
          personCertifyInfoBean.setUserName(prop_persName);
          session.setAttribute("proposalNumber"+session.getId(),proposalNo);
          String ActualUser = null;
          if(personInfoBean!=null && personInfoBean.getPersonID()!= null){
              ActualUser = personInfoBean.getPersonID();
          }
        //checking for login user is the same
                    /*
                     HashMap map=new HashMap();
                     
                        map.put("as_personId",personId);
                         try{
                        Hashtable htdata = (Hashtable)webTxnBean.getResults(request,"getUserNameForPPC",map);
                        HashMap nameMap=  (HashMap) htdata.get("getUserNameForPPC");
                      if(nameMap!=null && !nameMap.isEmpty()){
                        ActualUser=(String) nameMap.get("name");

                     }
                        }catch(Exception e)
                        {
                            ActualUser=personId;
                        }           
                      */
                    if(ActualUser!=null && !ActualUser.equalsIgnoreCase(personId))
                    {
                     request.setAttribute("InvalidPersonFromPremium", true);
                     navigate = "invalidUser";
                     return actionMapping.findForward(navigate);
                    }
                    
             Map hmQuestData=new HashMap();        
             hmQuestData.put("proposalNumber", proposalNo );
             hmQuestData.put("personId",personId);
             String ProposalRole = null;
                 try{
                        HashMap idMap = new HashMap();
                        Hashtable htFinData = (Hashtable) webTxnBean.getResults(request,"getPersonProposalRoleCert", hmQuestData);
                        idMap=  (HashMap) htFinData.get("getPersonProposalRoleCert");
                        if(htFinData!=null && !htFinData.isEmpty()){
                            ProposalRole=(String) idMap.get("proprole");
                        }
                        if (ProposalRole!=null && ProposalRole.trim().length() > 0) {
                              session.setAttribute("ProposalRoles",ProposalRole);
                              personCertifyInfoBean.setProposalRole(ProposalRole);
                        }

                    }catch(Exception e){
                         System.out.println(e.getMessage());
                    }
        HashMap hmPropData =  new HashMap();
        HashMap hmpData =  new HashMap();       
        hmPropData.put("proposalNumber", proposalNo);
        hmpData.put("userId",userId);
        hmpData.put("proposalNumber", proposalNo);
        Hashtable htData=getProposalAndBudgetDetails(request,hmPropData);
        Vector vecBudgetPeriods = null;
        Vector vecReports = null;
        Vector vecBudgetVersions =
        (Vector)htData.get("getBudgerForProposalDet");
        boolean budgetExists = false;
        DynaValidatorForm dynaForm =null;
        if(vecBudgetVersions!=null && vecBudgetVersions.size()>0){
            if(vecBudgetVersions.size() == 1){
                dynaForm = (DynaValidatorForm)vecBudgetVersions.get(0);
            }else{
                for(int index = 0;index<vecBudgetVersions.size();index++){
                    DynaValidatorForm dynaValidatorForm =
                    (DynaValidatorForm)vecBudgetVersions.get(index);
                    String finalFlag = (String)dynaValidatorForm.get("finalVersionFlag");
                    String versionNum = (String)dynaValidatorForm.get("versionNumber");
                    if(finalFlag.equalsIgnoreCase("Y")){
                        dynaForm = dynaValidatorForm;
                        break;
                    }else if(Integer.parseInt(versionNum)== 1){
                        dynaForm = dynaValidatorForm;
                    }

                }///end for
            }//end else
            budgetExists = true ;
        }
        getProposalHeader(request);
        if(proposalNo !=null && !proposalNo.equals(EMPTY_STRING)) {
            String grants= getS2SAttrMatch(proposalNo, request);
            session.setAttribute("grantsGovExist", "0");
            if(grants !=null && grants.equals("1")) {
                session.setAttribute("grantsGovExist", grants);
            }
        }
        Vector data  = (Vector)htData.get("getProposalDevelopmentDet");
        if(data.size()>0){
        DynaValidatorForm form = (DynaValidatorForm)data.get(0);
        String sponsorCode = (String)form.get("sponsorCode");
        HashMap hmSponsorCode= new HashMap();
        hmSponsorCode.put("sponsorCode",sponsorCode);
        }        
        Hashtable htDataInfo=getNarrativeAndApprovalRights(request,hmpData);
        Vector vecBudgetData =  new Vector();
        vecBudgetData.addElement(dynaForm);
        request.setAttribute("budgetPeriods", vecBudgetPeriods);
        request.setAttribute("budgetReports", vecReports);
        request.setAttribute("vecReports",vecReports);
        request.setAttribute("ProposalDevelopmentDet", htData.get("getProposalDevelopmentDet"));
        request.setAttribute("BudgetForProposalDet", vecBudgetData);
        request.setAttribute("ProposalInvestigators", htData.get("getProposalInvestigators"));
        request.setAttribute("NarrativeInfo", htDataInfo.get("getNarrativeInfo"));       
        request.setAttribute("budgetExists",String.valueOf(budgetExists)); 
        session.setAttribute("ppcBudgetForProposalDet", vecBudgetData);
 
        getApprovalRights(request, "3", proposalNo, "0");     
        HashMap hmpProposalData = new HashMap();
        String proposalNumber  = (String)session.getAttribute(CoeusLiteConstants.PROPOSAL_NUMBER+session.getId());
        hmpProposalData.put(PROPOSAL_NUMBER,proposalNumber);
        hmpProposalData.put("moduleCode", ModuleConstants.PROPOSAL_DEV_MODULE_CODE);           
         
        session.setAttribute("fromMailPPC",true);
        session.setAttribute("notInProposal",true);
        session.setAttribute("personCertifyInfoBean",personCertifyInfoBean);
        return actionMapping.findForward(navigate);
    }

    /*
     * To get proposal and Budget details for a given proposal number
     */
    private Hashtable getProposalAndBudgetDetails(HttpServletRequest request,HashMap hmData) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalAndBudgetData",hmData);
        return htPropData;
    }
    /**
     * Get all narratives and approval rights for a given proposal.
     */
    private Hashtable getNarrativeAndApprovalRights(HttpServletRequest request,HashMap hmpData) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getNarrativeAndApprovalData",hmpData);
        return htData;
    }    
}
