/*
 * CoiMainAction.java
 *
 * Created on February 26, 2008, 10:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.action;

import edu.dartmouth.coeuslite.coi.beans.DisclosureBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.dbengine.DBConnectionManager;
import edu.mit.coeuslite.utils.ComboBoxBean;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.axis.utils.Mapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author blessy
 */
public class CoiMainAction extends COIBaseAction{
    private static final String DISCLOSURE = "disclosure";
    private static final String COI_DISCLOSURE_SEARCH = "disclosureSearch";
    
    /** Creates a new instance of CoiMainAction */
    public CoiMainAction() {
    }
 public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, 
                HttpServletRequest request,HttpServletResponse response) throws Exception {
     String actionforward="failure";
     HttpSession session=request.getSession();
     
     getAnnDisclosure(request);
     //session.setAttribute("FinDiscl",finDisc);
     actionforward="success";
     if(request.getParameter("history")!=null){
     String history=request.getParameter("history");
     if(history.equals("history")){
     actionforward="disclHistory";
     }
     }
     return actionMapping.findForward(actionforward);
 }    
  private Vector getAnnDisclosure(HttpServletRequest request)throws Exception{
      
      WebTxnBean webTxn=new WebTxnBean();
       HttpSession session=request.getSession();
      if(session.getAttribute("person")==null){
      checkCOIPrivileges(request);
      }     
      //Case#4447 : Next phase of COI enhancements - Start
       String disclosureType = (String)request.getParameter("COI_DISCL_TYPE");
       //Case#4447 - End
//      PersonInfoBean person=(PersonInfoBean)session.getAttribute("person");
       PersonInfoBean person = (PersonInfoBean)session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
       session.setAttribute("person",person);
      //Modified for Case#4447 : FS_Next phase of COI enhancements - Start
      Vector finDiscl = null;
      Vector vecStatus = null;
      //String disclosure = (String)session.getAttribute(DISCLOSURE);
     
      //if(DISCLOSURE.equalsIgnoreCase(disclosure)){
      if(disclosureType == null || (disclosureType != null && !disclosureType.equals("disclosureSearch"))){
          String personId=person.getPersonID();
          HashMap hmData=new HashMap();
          hmData.put("personId",personId);
  /*    Connection conn=DBConnectionManager.getInstance().getConnection("jdbc/coeus");
      PreparedStatement stmt=conn.prepareStatement("select * from OSP$PERSON_DISCLOSURE where person_Id='924828240'");
      //stmt.setString(1,personId);
      ResultSet rs=stmt.executeQuery();*/
          Hashtable discl=(Hashtable)webTxn.getResults(request,"getAnnDisclPerson",hmData);
          finDiscl=(Vector)discl.get("getAnnDisclData");
          vecStatus=(Vector)discl.get("getDisclosureStatus");
          request.setAttribute("disclosurePerson",person.getFullName());
      }
      //Get's disclosure data based on search
      else if(disclosureType != null && COI_DISCLOSURE_SEARCH.equals(disclosureType)){
          HashMap hmData=new HashMap();
          String coiDisclosureNumber = request.getParameter("coiDisclosureNumber");
          if(coiDisclosureNumber == null){
              coiDisclosureNumber = (String)session.getAttribute("SearchDisclosureNumber");
          }else{
              session.setAttribute("SearchDisclosureNumber",coiDisclosureNumber);
          }
          request.setAttribute("COI_DISCL_TYPE","disclosureSearch");
          hmData.put("coiDisclosureNumber",coiDisclosureNumber);
          Hashtable discl=(Hashtable)webTxn.getResults(request,"getSearAnnDiscl",hmData);
          finDiscl=(Vector)discl.get("getAnnDisclSearcData");
          if(finDiscl != null && finDiscl.size() > 0){
              DisclosureBean disclosureBean = (DisclosureBean)finDiscl.get(0);
              if(disclosureBean != null){
                  String personId = disclosureBean.getPersonId();
                  hmData.put("personId",personId);
                  Hashtable htPersonData = (Hashtable)webTxn.getResults(request,"getPersonDetails",hmData);
                  Vector personDatas = (Vector)htPersonData.get("getPersonDetails");
                  if(personDatas != null && personDatas.size() > 0){
                      PersonInfoBean personInfoBean = (PersonInfoBean)personDatas.get(0);
                      request.setAttribute("disclosurePerson",personInfoBean.getFullName());
                      session.setAttribute("person",personInfoBean);
                  }
              }
          }
          vecStatus=(Vector)discl.get("getDisclosureStatus");
      }

      //Case#4447 - End

    //  HttpSession session=request.getSession();
      session.setAttribute("DisclStatus",vecStatus);
      session.setAttribute("finDiscl",finDiscl);
       if(vecStatus!=null && vecStatus.size()>0){
          ComboBoxBean combo=new ComboBoxBean();       
      for(int index=0;index<vecStatus.size();index++){
      combo=(ComboBoxBean)vecStatus.get(index);           
      }
     }
      int temp=0;
      int gtValue=0;
      Vector disclHistory=new Vector();
      if(session.getAttribute("DisclHistory")!=null){
          session.removeAttribute("DisclHistory");
      }
      if(session.getAttribute("CurrDisclPer")!=null){
          session.removeAttribute("CurrDisclPer");
      }
      DisclosureBean currDiscl=new DisclosureBean();
      if(finDiscl!=null && finDiscl.size()>0){
      for(int index=0;index<finDiscl.size();index++){
          DisclosureBean disclBean=(DisclosureBean)finDiscl.get(index);
          disclHistory.add(disclBean);
          Integer seq=disclBean.getSequenceNumber();
          if (gtValue<seq.intValue()){
              gtValue=seq.intValue();
              currDiscl=disclBean;              
          }
          
      }
      disclHistory.remove(currDiscl);
      session.setAttribute("DisclHistory",disclHistory);
      session.setAttribute("CurrDisclPer",currDiscl);
      }
    return finDiscl;
  }
   private void checkCOIPrivileges(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
               
        UserDetailsBean userDetailsBean = new UserDetailsBean();
        PersonInfoBean personInfoBean = (PersonInfoBean)session.getAttribute(
                        SessionConstants.LOGGED_IN_PERSON);
        if(personInfoBean!=null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null){
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            int value = userDetailsBean.getCOIPrivilege(userName);
            session.setAttribute(PRIVILEGE,""+userDetailsBean.getCOIPrivilege(userName));
           
            //setting logged in user's person id with the session
            session.setAttribute(LOGGEDINPERSONID, personInfoBean.getPersonID());
            //setting logged in user's person name with the session
            String personName = personInfoBean.getFullName();
            session.setAttribute(LOGGEDINPERSONNAME, personName);           
            //Check whether to show link for View Pending Disclosures
            if(userDetailsBean.canViewPendingDisc(userName)){
              session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
            }
        } 
        session.setAttribute("person", personInfoBean);
    }
}
