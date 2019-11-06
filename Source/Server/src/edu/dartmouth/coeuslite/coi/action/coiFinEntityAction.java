/*
 * coiFinEntityAction.java
 *
 * Created on April 8, 2008, 12:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.action;


import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;
import javax.servlet.jsp.PageContext;

/**
 *
 * @author blessy
 */
public class coiFinEntityAction extends COIBaseAction{
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
    public coiFinEntityAction() {
    }
    public ActionForward performExecuteCOI(ActionMapping actionMapping,
    ActionForm actionForm, HttpServletRequest request,
    HttpServletResponse response) throws Exception {
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
        
      /*  String returnTo = request.getParameter("return");
        if(returnTo != null && !returnTo.equalsIgnoreCase("null")){
            actionforward = actionMapping.findForward(returnTo);
        }
        
        PageContext context = null ;*/
       // String name  = ((HttpServletRequest)context.getRequest()).getParameter("actionFrom");
        
                
        PersonInfoBean personInfoBean  = (PersonInfoBean)session.getAttribute("person");
        String actionFrom = "";
        
        if( personInfoBean != null ) {
            personId = personInfoBean.getPersonID();
            String loggedinpersonid =
            (String)session.getAttribute(LOGGEDINPERSONID);
            String userprivilege = (String)session.getAttribute("userprivilege");
            if((!loggedinpersonid.equals(personId)) &&
            (Integer.parseInt(userprivilege) != 2)){
                personId = loggedinpersonid;
                personName = (String)session.getAttribute("loggedinpersonname");
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
        if(actionMapping.getPath().equals("/deactivateAnnFinEnt") ){
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
            (Hashtable)webTxnBean.getResults(request, "getFinDiscDet", hmData);
            Vector vecFinEnt=(Vector)htFinData.get("getFinDiscDet");
            for(int i=0;i<vecFinEnt.size();i++){
                DynaValidatorForm dynaform=(DynaValidatorForm)vecFinEnt.get(i);
                entityName=(String)dynaform.get("entityName");
            }
            request.setAttribute("entityName",entityName );
            session.setAttribute("entityDetails", htFinData.get("getFinDiscDet"));
            
            return actionforward;
        }else if(actionMapping.getPath().equals("/deactivateAnnFinEntSubmit")){
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
                webTxnBean.getResults(request,"activateDisclFinInt",dynaForm);
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
        else if(actionMapping.getPath().equals("/activateAnnFinEnt")){
            String entityNo = request.getParameter("entityNumber");
            HashMap hmData =new HashMap();
            webTxnBean = new WebTxnBean();
            hmData.put("entityNumber",entityNo);
            if(request.getParameter("actionFrom")!=null){
                 actionFrom = request.getParameter("actionFrom");
            }
            Hashtable htFinData = (Hashtable)webTxnBean.getResults(request, "getFinDiscDet", hmData);
            Vector vecData =(Vector)htFinData.get("getFinDiscDet");
            if(vecData!=null && vecData.size()>0){
                DynaValidatorForm dynaForm = (DynaValidatorForm)vecData.get(0);
                //update the sequence number by one for every update of Entity Status.
                int seqNumber = Integer.parseInt((String)dynaForm.get("sequenceNum"))+1;
                dynaForm.set("statusCode",ACTIVE_STATUS);
                dynaForm.set("statusDesc",EMPTY_STRING);
                dynaForm.set("sequenceNum",String.valueOf(seqNumber));
                dynaForm.set("acType", AC_TYPE_INSERT);
                //Update the financial Entity Status
                webTxnBean.getResults(request,"activateDisclFinInt",dynaForm);
                
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
    }//End of ActivateFinEnt
        
             /*Check for valid token associated with form.  If invalid, then this
              *is a duplicate submission.  Take user to error page, with appropriate
              *error msg based on whether this is an add or edit.*/
/*        boolean entSeqNoExists = false;
        if(!isTokenValid(request)){
            String entityNum = (String)dynaFinEntityForm.get("entityNumber");
            String sequenceNum = (String)dynaFinEntityForm.get("sequenceNum");
            // if(sequenceNum!=null && !sequenceNum.equals(EMPTY_STRING)){
            int seqNo =Integer.parseInt(sequenceNum);
            //}
            HashMap hmData = new HashMap();
            hmData.put("personId",personId);
            hmData.put("entityNumber", entityNum);
            hmData.put("sequenceNum",new Integer(seqNo));
            Hashtable htEntSeqExists =
            (Hashtable)webTxnBean.getResults(request,"checkEntSeqNoExists",hmData);
            HashMap hmEntSeqExists = (HashMap)htEntSeqExists.get("checkEntSeqNoExists");
            int entSeqNo = Integer.parseInt(hmEntSeqExists.get("count").toString());
            if(entSeqNo == 1){
                entSeqNoExists =true;
            }
            //If the entity number already exists show a error on the jsp
            if(entSeqNoExists){
             //   ActionMessages messages = new ActionMessages();
                if(acType.equals(defaultActionType)){
                    messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.fin_entity_exists"));
                    saveMessages(request, messages);
                    dynaFinEntityForm.reset(actionMapping,request);
                    return actionMapping.findForward("exception");
                }
                //                else{
                //                    messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.edit_fin_entity"));
                //                    saveMessages(request, messages);
                //                    dynaFinEntityForm.reset(actionMapping,request);
                //                    return actionMapping.findForward("exception");
                //
                //                }
            }//End if
        }//End !TokenValid
        
        //        if(isTokenValid(request)){
        //            System.out.println("Valid Token");
        //            resetToken(request);
        //        }
        
/*        if(!acType.equals(EMPTY_STRING)){
            if(!acType.equals(defaultActionType)){
                //Original Finanancial Entity Data
                isEntityChanged = isEntityChanged(session,request,dynaFinEntityForm);
                isCertAnsChanged = isCertAnsChanged(session,request,dynaFinEntityForm);
                if(!isTokenValid(request)){
                    if(isEntityChanged ||isCertAnsChanged){
                        ActionMessages errMsg = new ActionMessages();
                        errMsg.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.edit_fin_entity"));
                        saveMessages(request, errMsg);
                        dynaFinEntityForm.reset(actionMapping,request);
                        return actionMapping.findForward("exception");
                    }
                }
                
                if(isEntityChanged || isCertAnsChanged){
                    int seqNumber = Integer.parseInt((String)dynaFinEntityForm.get("sequenceNum"))+1;
                    dynaFinEntityForm.set("sequenceNum",String.valueOf(seqNumber));
                }
            }
        }
        
        /*If the acType is "I", by default status is set as Active
         */
/*       if(acType.equals(defaultActionType)){
            dynaFinEntityForm.set("acType",AC_TYPE_INSERT);
            isValid = validateAnswers(request,dynaFinEntityForm);
            if(isValid){
                addUpdFinEntity(request,dynaFinEntityForm);
                addCertQuest(request,dynaFinEntityForm, dbTimestamp);
                resetToken(request);
                request.setAttribute("entityNumber", dynaFinEntityForm.get("entityNumber"));
                request.setAttribute("entityName", dynaFinEntityForm.get("entityName"));
                request.setAttribute("FESubmitSuccess", "FESubmitSuccess");
                request.setAttribute("actionType", AC_TYPE_INSERT);
            }
        }
        boolean entityChanged =false;
        if(isEntityChanged){
            dynaFinEntityForm.set("acType",AC_TYPE_INSERT);
            addUpdFinEntity(request,dynaFinEntityForm);
            resetToken(request);
            entityChanged =true;
            request.setAttribute("entityNumber", dynaFinEntityForm.get("entityNumber"));
            request.setAttribute("entityName", dynaFinEntityForm.get("entityName"));
            request.setAttribute("actionType", AC_TYPE_UPDATE);
            request.setAttribute("FESubmitSuccess", "FESubmitSuccess");
        }
        if(isCertAnsChanged){
            dynaFinEntityForm.set("acType",AC_TYPE_INSERT);
            if(!entityChanged){
                addUpdFinEntity(request,dynaFinEntityForm);
            }
            isValid = validateAnswers(request,dynaFinEntityForm);
            if(isValid){
                addCertQuest(request,dynaFinEntityForm, dbTimestamp);
                resetToken(request);
            }
            request.setAttribute("entityNumber", dynaFinEntityForm.get("entityNumber"));
            request.setAttribute("entityName", dynaFinEntityForm.get("entityName"));
            request.setAttribute("actionType", AC_TYPE_UPDATE);
            request.setAttribute("FESubmitSuccess", "FESubmitSuccess");
        }
        
        if(!isValid){
            return actionMapping.findForward("exception");
        }else{
            if(actionFrom!=null){
                if(actionFrom.equals("editDiscl") ){
                    session.setAttribute("addFinEnt", "addFinEnt");
                    request.setAttribute("actionFrom","editFinEnt");
                    return actionMapping.findForward( "toEditDiscl" );
                }else if(actionFrom.equals("addDiscl")){
                    request.setAttribute("actionFrom","addDiscl");
                    return actionMapping.findForward( "toAddDiscl" );
                }else if(actionFrom.equals("annDiscCert")){
                    request.setAttribute("actionFrom", "editFinEnt");
                    return actionMapping.findForward("toAnnDiscCert");
                }else if(actionFrom.equals("AnnualFE") ){
                    return actionMapping.findForward("toAnnDiscFE");
                }else if(actionFrom.equals("addFinEntity") ){
                    request.setAttribute("actionFrom", "addFinEntity");
                    return actionMapping.findForward("EditCOI");
                }
            }
            dynaFinEntityForm.reset(actionMapping,request);
            return actionforward;
        }
    }
    //Method to Update the Fin Entity Details
  public void addUpdFinEntity(HttpServletRequest request,DynaValidatorForm dynaValidatorForm) throws Exception{
        WebTxnBean webTxnBean = new WebTxnBean();
        webTxnBean.getResults(request,"updFinancialEntity",dynaValidatorForm);
    }
    
    //Method to Update the Certificate Details
    public void addCertQuest(HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm, Timestamp dbTimestamp) throws Exception{
        
        String[] collQuestionIDs = request.getParameterValues("hdnQuestionId");
        String[] collQuestionDesc = request.getParameterValues("hdnQuestionDesc");
        String[] collSequenceNum =request.getParameterValues("hdnSeqNo");
        String explanation = request.getParameter("explanation");
        HashMap data = new HashMap();
        for(int index= 0; index < collQuestionIDs.length; index++){
            String answer = request.getParameter(collQuestionIDs[index] );
            dynaValidatorForm.set("answer",answer);
            dynaValidatorForm.set("updtimestamp",dbTimestamp.toString());
            dynaValidatorForm.set("questionId",collQuestionIDs[index]);
            WebTxnBean webTxnBean = new WebTxnBean();
            webTxnBean.getResults(request,"updFinEntityYNQ",dynaValidatorForm);
        }//End For
    }//End addCertQuest
    
    private boolean validateAnswers(HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm){
        String[] arrQuestionIDs = request.getParameterValues( "hdnQuestionId" );
        boolean isAnswered = true;
        String[] arrAnswers = null;
        if(arrQuestionIDs != null){
            arrAnswers= new String[arrQuestionIDs.length];
            for(int index= 0; index < arrQuestionIDs.length; index++){
                String answer = request.getParameter(arrQuestionIDs[index] );
                String questionId = arrQuestionIDs[index];
                ActionMessages actionMessages = new ActionMessages();
                if(answer == null){
                    String msg = "Please answer the question " + questionId + "";
                    actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.answer_required"));
                    saveMessages(request, actionMessages);
                    isAnswered = false;
                    break;
                }
                arrAnswers[index]=answer;
                request.setAttribute("selectedQuestions",arrQuestionIDs);
                request.setAttribute("selectedAnswers",arrAnswers);
            }
        }
        return isAnswered;
    }
    
    // method to check whether financial entity has changed
    public boolean isEntityChanged(HttpSession session,HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm) throws Exception{
        boolean isEntityChanged = false;
        DynaValidatorForm formData =(DynaValidatorForm)session.getAttribute("finEntityForm");
        
        if(!formData.get("entityTypeCode").equals(dynaValidatorForm.get("entityTypeCode"))){
            isEntityChanged =true;
        }
        if(!formData.get("entityName").equals(dynaValidatorForm.get("entityName"))){
            isEntityChanged =true;
        }
        if(formData.get("shareOwnerShip")!=null &&!formData.get("shareOwnerShip").equals(dynaValidatorForm.get("shareOwnerShip"))){
            isEntityChanged =true;
        }
        String orginalDesc =(String)formData.get("relationShipDesc");
        String relnDesc = (String)dynaValidatorForm.get("relationShipDesc");
        orginalDesc = (orginalDesc == null ) ? EMPTY_STRING : orginalDesc.trim();
        relnDesc = (relnDesc == null ) ? EMPTY_STRING : relnDesc.trim();
        if(!orginalDesc.equals(relnDesc)){
            isEntityChanged =true;
        }
        if(!formData.get("entityRelTypeCode").equals(dynaValidatorForm.get("entityRelTypeCode"))){
            isEntityChanged =true;
        }
        return  isEntityChanged;
    }
    
    //method to check whether certification answer has been changed
    public boolean isCertAnsChanged(HttpSession session,HttpServletRequest request,
    DynaValidatorForm dynaValidatorForm) throws Exception{
        
        Vector vecCertData =(Vector)session.getAttribute("ynqList");
        String[] collQuestionIDs = request.getParameterValues("hdnQuestionId");
        String[] collQuestionDesc = request.getParameterValues("hdnQuestionDesc");
        String[] collSequenceNum =request.getParameterValues("hdnSeqNo");
        
        boolean isCertAnsChanged =false;
        if(vecCertData!=null && vecCertData.size()>0){
            //if(collQuestionIDs!=null){
            for(int index=0;index<collQuestionIDs.length;index++){
                DynaValidatorForm dynaform=(DynaValidatorForm)vecCertData.get(index);
                String answer =(String)dynaform.get("answer");
                answer = (answer == null ) ? "" : answer.trim();
                //if(answer!=null || !answer.equals(EMPTY_STRING)){
                String ansValue = (String)request.getParameter(collQuestionIDs[index]);
                if(!answer.equals(ansValue)){
                    isCertAnsChanged =true;
                    // }
                }
            }//End for
            // }
        } //End if
        
        return isCertAnsChanged;
    }*/
} 

