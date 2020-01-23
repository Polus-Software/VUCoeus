/*
 * DisplayProposalAction.java
 *
 * Created on 16 February 2006, 16:23
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.CoeusReportGroupBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.xml.conf.ReportConfigEngine;
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
import edu.utk.coeuslite.propdev.bean.EPSProposalHeaderBean;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author  mohann
 *
 * To initiate response to a user request to display information about a given proposal.
 */
public class DisplayProposalAction extends COIBaseAction   {
    //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    //private HttpServletRequest request;
    //private ActionMapping mapping;
    private static final String EMPTY_STRING = "";
    private static final String USER ="user";
    //private String proposalNo;


    /** Creates a new instance of DisplayProposalAction */
    public DisplayProposalAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */

    public ActionForward performExecuteCOI(
    ActionMapping actionMapping,
    org.apache.struts.action.ActionForm actionForm,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception{

        //this.request = request;
        //this.mapping = actionMapping;
        ActionMessages messages = new ActionMessages();
        HttpSession session= request.getSession();
        WebTxnBean webTxnBean=new WebTxnBean();
        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute(USER);
        String userId = userInfoBean.getUserId();
        if(userId == null || userId.equals(EMPTY_STRING)){
            //session expired
        }

        ActionForward actionforward = actionMapping.findForward("success");
        String proposalNo = request.getParameter("proposalNo");
        HashMap hmPropData =  new HashMap();
        HashMap hmpData =  new HashMap();
        boolean errorFlag = false;
        hmPropData.put("proposalNumber", proposalNo );

        hmpData.put("userId",userId);
        hmpData.put("proposalNumber", proposalNo);

        //Check if user has right to view the proposal.
        boolean hasRightToView = userCanViewProposal(hmpData, request);
        if(!hasRightToView){
            //            messages.add("hasNoRightToView", new ActionMessage("error.Proposal.hasNoRightToView"));
            //            saveMessages(session, messages);
            //            errorFlag = true;
            throw new CoeusException("error.Proposal.hasNoRightToView");
        }
        //To get the Proposal and budget details.
        Hashtable htData=getProposalAndBudgetDetails(hmPropData, request);
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
            // Get budget Peridos data
            hmPropData = new HashMap();
            hmPropData.put("proposalNumber", proposalNo );
            int value = Integer.parseInt(dynaForm.get("versionNumber").toString());
            hmPropData.put("versionNumber", new Integer(value));
            htData= getBudgetPeriods(hmPropData, request);
            vecBudgetPeriods =  (Vector)htData.get("budgetPeriodDataSummary");
            CoeusReportGroupBean coeusReportGroupBean = ReportConfigEngine.get("ProposalBudget");
            vecReports = new Vector(coeusReportGroupBean.getReports().values());
        }
        Hashtable htProposalDetail = getProposalHeader(hmPropData, request);
        Vector vecProposalHeader = (Vector)htProposalDetail.get("getProposalHeaderData");
        if(proposalNo!= null && !proposalNo.equals(EMPTY_STRING)){
            if(vecProposalHeader!=null && vecProposalHeader.size()>0) {
                session.setAttribute("epsProposalHeaderBean",(EPSProposalHeaderBean)vecProposalHeader.get(0));
            }
        }else{
            /**while creating a new proposal,the bean is removed from session
             */
            session.removeAttribute("epsProposalHeaderBean");
        }
        Vector data  = (Vector)htData.get("getProposalDevelopmentDet");
        DynaValidatorForm form = (DynaValidatorForm)data.get(0);
        String sponsorCode = (String)form.get("sponsorCode");

        HashMap hmSponsorCode= new HashMap();
        hmSponsorCode.put("sponsorCode",sponsorCode);
        Hashtable htSponsorInfo= getSponsorMaintenanceDetails(hmSponsorCode, request);

        String unitNum = (String)form.get("ownedUnit");
        String unitDesc= getUnitName(unitNum, request);
        //System.out.println("unitDesc>>>"+unitDesc);

        Hashtable htDataInfo=getNarrativeAndApprovalRights(hmpData, request);

        Vector vecBudgetData =  new Vector();
        vecBudgetData.addElement(dynaForm);
        request.setAttribute("budgetPeriods", vecBudgetPeriods);
        request.setAttribute("budgetReports", vecReports);
        request.setAttribute("vecReports",vecReports);
        request.setAttribute("ProposalDevelopmentDet", htData.get("getProposalDevelopmentDet"));
        request.setAttribute("BudgetForProposalDet", vecBudgetData);
        request.setAttribute("ProposalInvestigators", htData.get("getProposalInvestigators"));
        request.setAttribute("NarrativeInfo", htDataInfo.get("getNarrativeInfo"));
        request.setAttribute("SponsorMaintenanceData", htSponsorInfo.get("getSponsorMaintenanceData"));
        request.setAttribute("budgetExists",String.valueOf(budgetExists));
        request.setAttribute("hasRightToView",String.valueOf(hasRightToView));
        request.setAttribute("unitDescription",unitDesc);

        // if( errorFlag ) {
        //     actionforward = actionMapping.findForward( "exception" );
        //  }
        return actionforward;

    }
    /*
     * To get proposal and Budget details for a given proposal number
     */
    private Hashtable getProposalAndBudgetDetails(HashMap hmData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalAndBudgetData",hmData);
        return htPropData;
    }
    /**
     * Get all narratives and approval rights for a given proposal.
     */
    private Hashtable getNarrativeAndApprovalRights(HashMap hmpData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getNarrativeAndApprovalData",hmpData);
        return htData;
    }

    /*
     * To get Budget PeriodsData for a given proposal number and version Number
     */
    private Hashtable getBudgetPeriods(HashMap hmData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"budgetPeriodDatas",hmData);
        return htPropData;
    }

    /*
     * To get Proposal Headr Details for a given proposal number
     */
    private Hashtable getProposalHeader(HashMap hmData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htPropData = (Hashtable)webTxnBean.getResults(request,"getProposalHeaderData",hmData);
        return htPropData;
    }

     /*
      * Method used to get entire details of Sponsor details for the sponsor id.
      */
    private Hashtable getSponsorMaintenanceDetails(HashMap hmpData, HttpServletRequest request) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htData = (Hashtable)webTxnBean.getResults(request,"getSponsorMaintenanceData",hmpData);
        return htData;
    }

    /*
     * Method used to check if user has right to view the proposal.
     **/
    private boolean userCanViewProposal(HashMap hmData, HttpServletRequest request)throws Exception{
        boolean canViewProposal = false;
        WebTxnBean webTxnBean = new WebTxnBean();
        Hashtable htViewProposal =
        (Hashtable)webTxnBean.getResults(request, "canViewProposal", hmData);
        HashMap hmViewProposal = (HashMap)htViewProposal.get("canViewProposal");
        int canView = Integer.parseInt(hmViewProposal.get("canView").toString());
        if(canView == 1){
            canViewProposal = true ;
        }

        return canViewProposal;
    }

    /*
     *To get the Unit Description
     */
    private String getUnitName(String unitNumber, HttpServletRequest request)throws Exception {
        WebTxnBean webTxnBean = new WebTxnBean();
        HashMap hmUnitNumber = new HashMap();
        hmUnitNumber.put("ownedUnit",unitNumber);
        Hashtable htUnitDesc = (Hashtable)webTxnBean.getResults(request,"getUnitDesc",hmUnitNumber);
        HashMap hmUnitDesc = (HashMap)htUnitDesc.get("getUnitDesc");
        String unitDesc = hmUnitDesc.get("RetVal").toString();
        return unitDesc;
    }


}
