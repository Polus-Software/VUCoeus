/*
 * UpdateInboxMessagesAction.java
 *
 * Created on February 14, 2006, 11:53 AM
 */


/* PMD check performed, and commented unused imports and variables on 18-AUG-2008
 * by Nandkumar S. Naik
 */

package edu.mit.coeuslite.coi.action;

//import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
//import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
//import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.DynaValidatorForm;

/**
 *
 * @author  vinayks
 */
public class UpdateInboxMessagesAction extends COIBaseAction{
    //private WebTxnBean webTxnBean;
    //private Timestamp dbTimestamp;
    private static final String RESOLVED = "resolved";
    private static final String ACTYPE ="acType";
    private static final String UNRESOLVED = "unresolved";
    /** Creates a new instance of UpdateInboxMessagesAction */
    public UpdateInboxMessagesAction() {
    }
    /*
    *Fix # 2866
    *change method name from performExecute to performExecuteCOI
    */
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping,ActionForm actionForm, 
    HttpServletRequest request,HttpServletResponse response) throws Exception {
        
    DynaValidatorForm dynaValidatorForm = (DynaValidatorForm)actionForm;
    ActionForward actionforward = null;
    WebTxnBean webTxnBean = new WebTxnBean();
    String messageType = request.getParameter("messageType");    
    String[] messagesId = request.getParameterValues("hdnMessageId");    
    //String[] messages = (String)dynaValidatorForm.get("messageId");
    HttpSession session = request.getSession();
    Vector vecInbox = (Vector)session.getAttribute("inboxList");
//    Timestamp dbTimestamp = prepareTimeStamp();
    String[] checkedMessages = (String[])dynaValidatorForm.get("whichMessagesAreChecked");
   // String[] checkedMessages =(String[])PropertyUtils.getProperty(actionForm,"whichMessagesAreChecked");
   
    if(checkedMessages!=null && checkedMessages.length>0){
        ActionMessages messages = new ActionMessages();
          for(int i=0; i<checkedMessages.length; i++){
             int index = Integer.parseInt(checkedMessages[i].toString());
             DynaValidatorForm dynaForm = (DynaValidatorForm)vecInbox.get(index);
             //Commented for Princeton Enhancement Case # 2802  - Start
//             if(messageType.equalsIgnoreCase("unresolved")){
//                dynaForm.set("acType","U");
//                dynaForm.set("openedFlag","Y"); 
//                actionforward = actionMapping.findForward("unresolved");
//             }else{
//                dynaForm.set("acType","D"); 
//                 actionforward = actionMapping.findForward("resolved");
//             }
             //Commented for Princeton Enhancement Case # 2802  -End
             
             //Added for Princeton Enhancement Case # 2802  -Start
             
             if(Integer.parseInt(dynaForm.get("moduleCode").toString()) == 4){
                 String moduleKeyItem = dynaForm.get("moduleItemKey").toString();
                 String message = (String)dynaForm.get("message");
                 String seqNum = message.substring(0,message.indexOf("|"));
                 message = message.substring(message.indexOf("|")+1);
                 String lineNum = message.substring(0,message.indexOf("|"));
                 int seqNumber = Integer.parseInt(seqNum);
                 int lineNumber = Integer.parseInt(lineNum);
                 HashMap hmDetails = new HashMap();
                 hmDetails.put("subcontractId",moduleKeyItem );
                 hmDetails.put("sequenceNumber",new Integer(seqNumber));
                 hmDetails.put("lineNumber", new Integer(lineNumber));
                 Hashtable htGetDetails = (Hashtable)webTxnBean.getResults(request, "getSubcontractInvDetails", hmDetails);
                 Vector vecInvoice = (Vector)htGetDetails.get("getSubcontractInvDetails");
                 DynaValidatorForm dynaInvoice = (DynaValidatorForm)vecInvoice.get(0);
                 String invoiceStatus = (String)dynaInvoice.get("invoiceStatus");
                 if(messageType.equalsIgnoreCase(UNRESOLVED) && (invoiceStatus.equals("Sent") || invoiceStatus.equals("In Progress"))){
                     // throw error message
                     messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("subcontract.error.StatusChange",
                     moduleKeyItem, (new Integer(seqNumber)).toString(), (new Integer(lineNumber)).toString()));
                     saveMessages(request, messages);
                     actionforward = actionMapping.findForward("unresolvedException");
                 }else if(messageType.equalsIgnoreCase(UNRESOLVED) && (invoiceStatus.equals("Approved") || invoiceStatus.equals("Rejected"))){
                     dynaForm.set(ACTYPE,"U");
                     dynaForm.set("openedFlag","Y");
                     webTxnBean.getResults(request, "updateInbox",dynaForm);
                     actionforward = actionMapping.findForward(UNRESOLVED);
                 }else if(messageType.equalsIgnoreCase(RESOLVED)){
                     dynaForm.set(ACTYPE,"D");
                     webTxnBean.getResults(request, "updateInbox",dynaForm);
                     actionforward = actionMapping.findForward(RESOLVED);
                 }
             }
             //Added for Case#3682 - Enhancements related to Delegations - Start
             // Delegation inbox messages of status 'Requested' cannot be changed
             else  if(Integer.parseInt(dynaForm.get("moduleCode").toString()) == 0){
                 if(dynaForm.get("creationStatusCode") != null && "1".equalsIgnoreCase((String)dynaForm.get("creationStatusCode"))
                        && messageType.equalsIgnoreCase(UNRESOLVED)){
                     messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.changeDelegationStatus"));
                     saveMessages(request, messages);
                     actionforward = actionMapping.findForward("unresolvedException");
                 }else if(messageType.equalsIgnoreCase(UNRESOLVED) ){
                     dynaForm.set(ACTYPE,"U");
                     dynaForm.set("openedFlag","Y");
                     webTxnBean.getResults(request, "updateInbox",dynaForm); 
                     actionforward = actionMapping.findForward(UNRESOLVED);
                 }else if(messageType.equalsIgnoreCase(RESOLVED)){
                     dynaForm.set(ACTYPE,"D");
                     webTxnBean.getResults(request, "updateInbox",dynaForm);
                     actionforward = actionMapping.findForward(RESOLVED);
                 }
             }
             //Added for Case#3682 - Enhancements related to Delegations - End
             else{
                 if(messageType.equalsIgnoreCase(UNRESOLVED)){
                     dynaForm.set(ACTYPE,"U");
                     dynaForm.set("openedFlag","Y");
                     actionforward = actionMapping.findForward(UNRESOLVED);
                     //Added/Modified for case#3621 - Lite Inbox bug - start
                     String subjectType = (String)dynaForm.get("subjectType");
                     if(!subjectType.equals("A")){
                        webTxnBean.getResults(request, "updateInbox",dynaForm);
                     }else{
                         // Case# 3621 - Lite Inbox bug- Start
//                         messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cannot_be_moved"));
//                         saveMessages(request, messages);
                         //Added for the case# COEUSQA-2059 & COEUSQA-2302 - Alternate Approver cannot remove an unresolved message - start
                         //We are allowing the user to delete or change the status for the following proposal and protocol status
                         // status == 4 => Approved
                         // status == 5 => Submitted
                         // status == 6 => Post-Submission Approval
                         // status == 7 => Post-Submission Rejection
                         // status == 200 => Active - Open to Enrollment
                         // status == 201 => Active - Closed to Enrollment
                         // status == 202 => Active - Data Analysis Only
                         // status == 203 => Active - Exempt
                         // status == 300 => Closed Administratively for lack of response
                         // status == 301 => Closed by Investigator
                         String creationStatusCode = (String)dynaForm.get("creationStatusCode");
                         if(creationStatusCode != null && creationStatusCode.equalsIgnoreCase("4")||
                                 creationStatusCode.equalsIgnoreCase("5")||
                                 creationStatusCode.equalsIgnoreCase("6")||
                                 creationStatusCode.equalsIgnoreCase("7")||
                                 creationStatusCode.equalsIgnoreCase("200")||
                                 creationStatusCode.equalsIgnoreCase("201")||
                                 creationStatusCode.equalsIgnoreCase("202")||
                                 creationStatusCode.equalsIgnoreCase("203")||
                                 creationStatusCode.equalsIgnoreCase("300")||
                                 creationStatusCode.equalsIgnoreCase("301")
                                 ){
                             webTxnBean.getResults(request, "updateInbox",dynaForm);
                         }else{
                             //Added for the case# COEUSQA-2059 & COEUSQA-2302 - Alternate Approver cannot remove an unresolved message - end
                             String module = "Proposal";
                             module = (String)dynaForm.get("moduleDesc");
                             if("Development Proposal".equalsIgnoreCase(module)){
                                 module = "Proposal";
                             } else if("IRB".equalsIgnoreCase(module)){
                                 module = "Protocol";
                             }
                             messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.cannot_be_moved", module,(String)dynaForm.get("moduleItemKey")));
                             saveMessages(request, messages);
                             actionforward = actionMapping.findForward("unresolvedException");
                             // Case# 3621 - Lite Inbox bug- End
                         }
                     }
                 }else{
                     dynaForm.set(ACTYPE,"D");
                     actionforward = actionMapping.findForward(RESOLVED);
                     webTxnBean.getResults(request, "updateInbox",dynaForm);
                 }
//                 webTxnBean.getResults(request, "updateInbox",dynaForm);
                 //Added/Modified for case#3621 - Lite Inbox bug - end
             }
             //Added for Princeton Enhancement Case # 2802  - End
             
//             webTxnBean.getResults(request, "updateInbox",dynaForm);//Commented for Princeton Enhancement Case # 2802 
            // dynaForm.set("whichMessagesAreChecked",null); 
          }
        
    }
    if((checkedMessages==null || checkedMessages.length==0)&& messageType.equals("resolved")){
         ActionMessages messages = new ActionMessages();               
         messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.no_messages_checked"));
         saveMessages(request, messages);
         return actionMapping.findForward("resolvedException");
    } if((checkedMessages==null || checkedMessages.length==0)&& messageType.equals("unresolved")){
         ActionMessages messages = new ActionMessages();               
         messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.no_messages_checked"));
         saveMessages(request, messages);
         return actionMapping.findForward("unresolvedException");
    }
    return actionforward;
    }
    
}
