/*
 * COIAction.java
 *
 * Created on December 27, 2005, 9:36 AM
 */

package edu.mit.coeuslite.coi.action;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import edu.mit.coeuslite.utils.SessionConstants;
import java.util.Vector;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;

/**
 *
 * @author  vinayks
 */
/** This Action class gets the subheader details for COI page
 * The subheaders are mentioned in a Xml file
 **/
public class COIAction extends COIBaseAction {
//    private HttpServletRequest request;
    private static final String SUB_HEADER = "headerVector";
    private static final String XML_PATH = "/edu/mit/coeuslite/coi/xml/COISubMenu.xml";
    public static final String LOGGEDINPERSONID = "loggedinpersonid";
    public static final String LOGGEDINPERSONNAME = "loggedinpersonname";
    public static final String VIEW_PENDING_DISC = "viewPendingDisc";
   //FOR Dartmouth COI module
    public static final String COI_MIT = "MIT";
    public static final String COI_DARTMOUTH ="DARTMOUTH";  
    /**
     * The session scope attribute under which the user's COI privilege is stored.
     */
    public static final String PRIVILEGE = "userprivilege";
    
    /** Creates a new instance of COIAction */
    public COIAction() {
    }
    
    public ActionForward performExecuteCOI(ActionMapping actionMapping, ActionForm actionForm, 
                HttpServletRequest request,HttpServletResponse response) throws Exception {
      /*
         *Fix # 2866
         *moved to base class
       */
      //begin fix
//    this.request = request;
//    checkCOIPrivileges(request);
//    getSubheaderDetails();
      //end fix
        
    //FOR Dartmouth COI module
        String actionForward="success";        
       if(CoeusProperties.getProperty(CoeusPropertyKeys.COI_MODULE)!=null){
            String coimodule=CoeusProperties.getProperty(CoeusPropertyKeys.COI_MODULE);
                         if(coimodule.trim().equals(COI_DARTMOUTH)){
                          actionForward="dartmouth";  
                          //Added for Case#4447 : Next phase of COI enhancements 
                                request.getSession().setAttribute("disclosure","disclosure");
                          }
                }
    return actionMapping.findForward(actionForward);                    
    }
    
    /** This method reads the xml file and gets the subheader data 
     **/
    private void getSubheaderDetails()throws Exception{
         ServletContext application = getServlet().getServletConfig().getServletContext();
         Vector vecCOISubHeader ;
         ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
         vecCOISubHeader = (Vector)application.getAttribute(SUB_HEADER);
         if(vecCOISubHeader == null || vecCOISubHeader.size()==0){
             vecCOISubHeader = readProtocolDetails.readXMLDataForSubHeader(XML_PATH);             
             application.setAttribute(SUB_HEADER,vecCOISubHeader);
         }
    }
    
    private void checkCOIPrivileges(HttpServletRequest request)throws Exception{
        HttpSession session = request.getSession();
        /*
         *  Commented by Geo
         *  take person and user from session
         */
        //BEGIN Block
//        UserInfoBean userInfoBean = (UserInfoBean)session.getAttribute("user"+session.getId());
//        String userName = userInfoBean.getUserId();        
//        PersonInfoBean personInfoBean = null;
//        UserDetailsBean userDetailsBean = new UserDetailsBean();
//        if(userInfoBean!=null){            
////            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
//            personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getUserId());
//        }
        
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
