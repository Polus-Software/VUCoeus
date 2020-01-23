/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.actions;

import edu.dartmouth.coeuslite.coi.action.COIBaseAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeuslite.coiv2.services.CoiCommonService;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
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
public class CoiFinEntityAction extends COIBaseAction{
   //private ActionForward actionForward = null;
    //private WebTxnBean webTxnBean ;
    private static final String ACTIVE_STATUS = "1";
    private static final String INACTIVE_STATUS = "2";
    private static final String DEFAULT_ORG_RELATIONSHIP = "X";
    private static final String EMPTY_STRING ="";
    private static final String AC_TYPE_UPDATE="U";
    private static final String AC_TYPE_INSERT = "I";
    //private Timestamp dbTimestamp;
    /** Creates a new instance of coiFinEntityAction */
  
    public ActionForward performExecuteCOI(ActionMapping actionMapping,
    ActionForm actionForm, HttpServletRequest request,
    HttpServletResponse response) throws Exception {
        request.getSession().setAttribute("disclosureAvailableMessage",true);
//            request.getSession().setAttribute("ApprovedDisclDetView",true);
//             request.getSession().setAttribute("ApprovedDisclDetView",true);
//             request.getSession().setAttribute("annualDisclosureBeanDisclosureNumber",true);
        boolean isValid  = true;
        boolean isEntityChanged =false;
        boolean isCertAnsChanged = false;
        HttpSession session = request.getSession();
        WebTxnBean webTxnBean = new WebTxnBean();
        DynaValidatorForm dynaFinEntityForm = (DynaValidatorForm)actionForm;
        ActionMessages messages = new ActionMessages();
        String personId = EMPTY_STRING;
        String personName = EMPTY_STRING;
        String defaultTypeCode = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_TYPE_CODE);
        String defaultActionType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_ACTION_TYPE);
        // String defaultSeqNum = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_SEQ_NUM);
        String defaultOrgRelType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_ORG_REL_TYPE);
        String defaultType = CoeusProperties.getProperty(CoeusPropertyKeys.DEFAULT_TYPE);
        ActionForward actionforward = actionMapping.findForward("success");

       CoiCommonService coiCommonService = CoiCommonService.getInstance();
        coiCommonService.getDisclosureDet(request);
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String actionFrom = "";

        if( personInfoBean != null ) {
            personId = personInfoBean.getPersonID();
            String loggedinpersonid =
            (String)session.getAttribute(LOGGEDINPERSONID);
            String userprivilege = (String)session.getAttribute(PRIVILEGE);
            if((!loggedinpersonid.equals(personId)) &&
            (Integer.parseInt(userprivilege) != 2)){
                personId = loggedinpersonid;
                personName = (String)session.getAttribute(LOGGEDINPERSONNAME);
            }
            else{
                personName = personInfoBean.getFullName();
            }
        }

        Timestamp dbTimestamp = prepareTimeStamp();
        dynaFinEntityForm.set("personId",personId);
        dynaFinEntityForm.set("statusCode",Integer.getInteger(ACTIVE_STATUS));
        dynaFinEntityForm.set("relatedToOrgFlag",DEFAULT_ORG_RELATIONSHIP);
        dynaFinEntityForm.set("updtimestamp",dbTimestamp);
        String acType =(String)dynaFinEntityForm.get("acType");

        /* if the action is coming from remove financial Entity*
         * make the status as INACTIVE*/
        if(actionMapping.getPath().equals("/deactivateCoiAnnFinEnt") ){
            String entityNo = request.getParameter("entityNumber");
            HashMap hmData =new HashMap();
            webTxnBean = new WebTxnBean();

            if(request.getParameter("actionFrom")!=null){
                 actionFrom = request.getParameter("actionFrom");
            }
            String entityName = request.getParameter("entityName");

            request.setAttribute("actionFrom", actionFrom );

       //     dynaFinEntityForm.set("actionFrom",actionFrom);

            hmData.put("entityNumber",entityNo);
            Hashtable htFinData =
            (Hashtable)webTxnBean.getResults(request, "getFinDiscDetCoiv2", hmData);
            Vector vecFinEnt=(Vector)htFinData.get("getFinDiscDetCoiv2");
            if(vecFinEnt != null && vecFinEnt.size() >0) {
                for(int i=0;i<vecFinEnt.size();i++){
                    DynaValidatorForm dynaform=(DynaValidatorForm)vecFinEnt.get(i);
                    entityName=(String)dynaform.get("entityName");
                }
            }
            request.setAttribute("entityName",entityName );
            session.setAttribute("entityDetails", htFinData.get("getFinDiscDetCoiv2"));

            return actionforward;
        }else if(actionMapping.getPath().equals("/deactivateCoiAnnFinEntSubmit")){
            Vector vecData =(Vector)session.getAttribute("entityDetails");

            if(request.getParameter("actionFrom")!=null){
                 actionFrom = request.getParameter("actionFrom");
            }
            String entityName = request.getParameter("entityName");
            request.setAttribute("entityName",entityName );

            if(vecData!=null && vecData.size()>0){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(0);
                //update the sequence number by one for every update of Entity Status.
                int seqNumber = Integer.parseInt((String)dynaForm.get("sequenceNum"))+1;
                String relationShip =(String)dynaFinEntityForm.get("statusDesc");
                if(relationShip ==null || ( relationShip.trim().equals( "" ))) {
                    messages.add("finEntitiesExplanationRequired",
                    new ActionMessage("error.finEntitiesExplanation.required"));
                    saveMessages(request, messages);
                    return actionMapping.findForward("exception");
                }
                dynaForm.set("statusCode",INACTIVE_STATUS);
                dynaForm.set("statusDesc",relationShip);
                dynaForm.set("sequenceNum",String.valueOf(seqNumber));
                dynaForm.set("acType", AC_TYPE_INSERT);

                //update the Financial Entity status
                webTxnBean.getResults(request,"activateDisclFinIntCoiv2",dynaForm);
                request.setAttribute("entityNumber", dynaForm.get("entityNumber"));
                request.setAttribute("entityName", dynaForm.get("entityName"));
                request.setAttribute("FESubmitSuccess", "FESubmitSuccess");
                request.setAttribute("actionType", "deactivate");
                if(actionFrom!=null || !actionFrom.equals(EMPTY_STRING)){
                    if(actionFrom.equals("coiDiscl")){
                        return actionMapping.findForward("coiCertYesAnswers");
                    }
                }
                return actionforward;
            }//End if
        }
        else if(actionMapping.getPath().equals("/activateCoiAnnFinEnt")){
            String entityNo = request.getParameter("entityNumber");
            HashMap hmData =new HashMap();
            webTxnBean = new WebTxnBean();
            hmData.put("entityNumber",entityNo);
            if(request.getParameter("actionFrom")!=null){
                 actionFrom = request.getParameter("actionFrom");
            }
            Hashtable htFinData = (Hashtable)webTxnBean.getResults(request, "getFinDiscDetCoiv2", hmData);
            Vector vecData =(Vector)htFinData.get("getFinDiscDetCoiv2");
            if(vecData!=null && vecData.size()>0){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(0);
                //update the sequence number by one for every update of Entity Status.
                int seqNumber = Integer.parseInt((String)dynaForm.get("sequenceNum"))+1;
                dynaForm.set("statusCode",ACTIVE_STATUS);
                dynaForm.set("statusDesc",EMPTY_STRING);
                dynaForm.set("sequenceNum",String.valueOf(seqNumber));
                dynaForm.set("acType", AC_TYPE_INSERT);
                //Update the financial Entity Status
                webTxnBean.getResults(request,"activateDisclFinIntCoiv2",dynaForm);

                request.setAttribute("entityNumber", dynaForm.get("entityNumber"));
                request.setAttribute("entityName", dynaForm.get("entityName"));
                request.setAttribute("FESubmitSuccess", "FESubmitSuccess");
                request.setAttribute("actionType", "activate");
                String to = request.getParameter("to");
                if(to != null){
                    actionforward = actionMapping.findForward(to);
                }

            }
            if(actionFrom!=null || !actionFrom.equals(EMPTY_STRING)){
                    if(actionFrom.equals("coiDiscl")){
                        return actionMapping.findForward("coiCertYesAnswers");
                    }
                }
        }

        return actionforward;
    }
}