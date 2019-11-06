/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeuslite.coiv2.beans.CoiAnnualPersonProjectDetails;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author Invision
 */
public class Coiv2AnnualProjectFinancialEntitySaveAction extends COIBaseAction{

    @Override
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        CoiCommonService lCoiCommonService=CoiCommonService.getInstance();
        WebTxnBean webTxnBean = new WebTxnBean();
        Vector lVector=new Vector();
        lVector=lCoiCommonService.getFinancialEntityForUser(request);
        HttpSession session=request.getSession();
        String success = "success";
        CoiAnnualPersonProjectDetails lCoiAnnualPersonProjectDetails=new CoiAnnualPersonProjectDetails();
        String lAnnualCoiDisclNo= (String) session.getAttribute("DisclosureNumberInUpdateSession");
        if(lAnnualCoiDisclNo==null){
               lAnnualCoiDisclNo= (String) session.getAttribute("DisclNumber");
        }

        Integer lAnnualSeqNo = (Integer) session.getAttribute("SequenceNumberInUpdateSession");
        if(lAnnualSeqNo==null){
                lAnnualSeqNo = (Integer) session.getAttribute("DisclSeqNumber");
        }
        Integer lModuleCode = (Integer)session.getAttribute("ModuleCodeInUpdateSession");
                if(lModuleCode==null){
                    lModuleCode=13;
                }
        //String lUpdateUser= (String) session.getAttribute("loggedinUser");
        //for getting username rather than full name
        PersonInfoBean personInfoBean = (PersonInfoBean) request.getSession().getAttribute("person");
        String lUpdateUser= personInfoBean.getUserName();
        String statusCode=null;
        if(lVector!=null && !lVector.isEmpty()){
            session.removeAttribute("QstnAnsFlag");
            session.removeAttribute("annualQstnFlag");
            //insCoiDisclosureDetailsCoiv2
            Iterator lIterator=lVector.iterator();
            while (lIterator.hasNext()){
                DynaValidatorForm lDynaValidatorForm=(DynaValidatorForm)lIterator.next();
                lCoiAnnualPersonProjectDetails = new CoiAnnualPersonProjectDetails();
                lCoiAnnualPersonProjectDetails.setCoiDisclosureNumber(lAnnualCoiDisclNo);
                lCoiAnnualPersonProjectDetails.setSequenceNumber(lAnnualSeqNo);
                lCoiAnnualPersonProjectDetails.setModuleCode(lModuleCode);
                lCoiAnnualPersonProjectDetails.setUpdateUser(lUpdateUser);
                lCoiAnnualPersonProjectDetails.setEntityNumber((String) lDynaValidatorForm.get("entityNumber"));
                lCoiAnnualPersonProjectDetails.setEntitySequenceNumber((String) lDynaValidatorForm.get("sequenceNum"));
                 statusCode=(String)lDynaValidatorForm.get("statusCode");
                lCoiAnnualPersonProjectDetails.setCoiStatusCode(Integer.parseInt(statusCode));
                //lCoiAnnualPersonProjectDetails.setCoiStatusCode(1);
                String Desc=(String)lDynaValidatorForm.get("description");
                Desc="Description";
                if(lDynaValidatorForm.get("description")!=null){
                    Desc ="Description";
                }
                lCoiAnnualPersonProjectDetails.setRelationShipDescription(Desc);
                //lCoiAnnualPersonProjectDetails.setCoiDiscDetails(10524);
                webTxnBean.getResults(request, "insCoiDisclosureDetailsCoiv2", lCoiAnnualPersonProjectDetails);
            }



                    //CoiAnnualPersonProjectDetails



        } else {
            success = "createFinEntity";
        }

        return actionMapping.findForward(success);

    }


}
