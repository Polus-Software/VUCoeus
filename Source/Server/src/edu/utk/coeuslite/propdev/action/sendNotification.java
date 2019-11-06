/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeuslite.coiv2.actions.SetStatusAdminCoiV2;
import edu.mit.coeuslite.coiv2.utilities.DisclosureMailNotification;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.lang.String;

/**
 *
 * @author twinkle
 */
public class sendNotification extends ProposalBaseAction
{
    private static final String SUCCESS="success";
    private int actionId=811;

     public sendNotification()
     {
    }
    @Override
    public ActionForward performExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        System.out.println("Hi");
         String forward = "success";
 if (actionMapping.getPath().equals("/sendNotificationMsg")) {

            String[] total = request.getParameterValues("check");
             HttpSession session = request.getSession();

           String proposalNumber = request.getParameter("proposalNumber");
           session.setAttribute("proposalNumber", proposalNumber);
            WebTxnBean webTxnBean = new WebTxnBean();
            Map Hmap = new HashMap();
            Hmap.put("proposalnumber",proposalNumber);

            Hashtable sendnotification = (Hashtable) webTxnBean.getResults(request,"getSendNotificationDetail",Hmap);
            Vector notificationDetailVector = (Vector)sendnotification.get("getSendNotificationDetail");

             request.setAttribute("notificationDetails",notificationDetailVector);
             forward = "success";

 }
  if (actionMapping.getPath().equals("/sendEmail")) {

boolean flag;
   String[] id;
HttpSession session = request.getSession();
   id = request.getParameterValues("check");

SetStatusAdminCoiV2 status=new SetStatusAdminCoiV2();
 DisclosureMailNotification discloNotification = new  DisclosureMailNotification();
String proposalNumber = (String) session.getAttribute("proposalNumber");
String sch=request.getScheme();
String serv=request.getServerName();
int portno=request.getServerPort();
String url=null;
String proposalNumberData="Proposal Number : "+proposalNumber;


  Vector vecRecipients=new Vector();
  Vector lVector=new Vector();
  //vecRecipients.add(vect_id);
  Vector reporter = null;
  Vector vecRecipientsdata=new Vector();
String personId=null;
String emailId=null;
for(int i=0;i<id.length;i++){
    personId=id[i];
     HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("personId", personId);
        hmData.put("proposalNumber", proposalNumber);
        
        Hashtable reporterData = (Hashtable) webTxn.getResults(request, "getEmailId", hmData);
        reporter = (Vector) reporterData.get("getEmailId");
        if (reporter != null && reporter.size() > 0) {
                for (Iterator it = reporter.iterator(); it.hasNext();) {
                      PersonRecipientBean ob = (PersonRecipientBean) it.next();
                      vecRecipientsdata=new Vector();
                      emailId=ob.getEmailId();
                      vecRecipientsdata.add(ob);
                            lVector.add(id[i]);

                }
            if(emailId!=null && !emailId.equals("")){
            url=sch+":"+"//"+serv+":"+portno+request.getContextPath()+"/proposalPersonsCertify.do?proposalNo="+proposalNumber+"&personId="+personId;

                           // personId="+personId+"&proposalNumber="+proposalNum
                        MailMessageInfoBean mailMsgInfoBean = null;
                        //personId=   (String) lVector.get(i);
                        String personIdData="Person Id : "+personId;
                        try{
                            boolean  mailSent;
                              mailMsgInfoBean = discloNotification.prepareNotificationCertify(actionId);
                              if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                                    mailMsgInfoBean.setPersonRecipientList(vecRecipientsdata);
                                    mailMsgInfoBean.appendMessage(proposalNumberData, "\n") ;
                                    mailMsgInfoBean.appendMessage(personIdData, "\n");
                                    mailMsgInfoBean.appendMessage("Please certify click on the following URL ", "\n") ;
                                    mailMsgInfoBean.setUrl(url);
                                    mailSent = discloNotification.sendNotification(mailMsgInfoBean);
                                    flag=true;
                                    request.setAttribute("mailSend",flag);
                                }
                        } catch (Exception ex){
                            UtilFactory.log(ex.getMessage());

                        }

              updateLastNotificationDate(personId,proposalNumber,request);
            }
            }

}

if(reporter!=null && !reporter.isEmpty()){

    
//updateing notification date

}
forward = "success";

//            send notification mail ends............................


 }

          return actionMapping.findForward(forward);

    }

    public void updateLastNotificationDate(String personId,String proposalNumber,HttpServletRequest request)throws Exception{

        HashMap hmData = new HashMap();
        hmData.put("personId", personId);
        hmData.put("proposalNumber",proposalNumber);
        WebTxnBean webTxn = new WebTxnBean();
         Hashtable htTableData = (Hashtable)webTxn.getResults(request,"updateLastNotificationDateFunction",hmData);
        HashMap sampResult = (HashMap) htTableData.get("updateLastNotificationDateFunction");
    }

}

