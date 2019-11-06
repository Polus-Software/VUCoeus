/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.services;

import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeuslite.coiv2.utilities.CoiConstants;
import edu.mit.coeuslite.coiv2.utilities.UserDetailsBeanCoiV2;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.okip.service.shared.api.Exception;

/**
 *
 * @author Mr
 */
public class GettingRightsCoiV2Service {    

    private static final String RIGHT="rights";
    private static final String USER ="user";   
    public static final String COI_ADMIN="COI_ADMIN";
    private GettingRightsCoiV2Service() {
    }
    private static GettingRightsCoiV2Service instance = null;

    public static GettingRightsCoiV2Service getInstance() {
        if (instance == null) {
            instance = new GettingRightsCoiV2Service();
        }
        return instance;
    }
    
    public void getCoiPrivilegesCoiV2(HttpServletRequest request) throws Exception, CoeusException, DBException {
        HttpSession session = request.getSession();

        UserDetailsBeanCoiV2 userDetailsBean = new UserDetailsBeanCoiV2();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            Map thisUserRights = userDetailsBean.getRightsCoiv2(userName);
            request.setAttribute(RIGHT, thisUserRights);
         //   request.setAttribute("coiroleassigned", userDetailsBean.getUserHasRole(userName, 513));
            //  request.setAttribute("viewtemplate", userDetailsBean.getUserHasRole(userName, 14));

        }
    }

    public boolean getCoiUserPrivilege(HttpServletRequest request, String riteType) throws Exception, DBException {
        final String DISCL = "disclosure";
        final String ATTACHMENT = "attachment";
        final String NOTES = "note";
        HttpSession session = request.getSession();

        UserDetailsBeanCoiV2 userDetailsBean = new UserDetailsBeanCoiV2();
        PersonInfoBean personInfoBean = (PersonInfoBean) session.getAttribute(
                SessionConstants.LOGGED_IN_PERSON);
        if (personInfoBean != null && personInfoBean.getPersonID() != null && personInfoBean.getUserName() != null) {
            //setting personal details with the session object
            // session.setAttribute(PERSON_DETAILS_REF,personInfo);
            //setting privilege of a logged in user with the session
            String userName = personInfoBean.getUserName();
            Map thisUserRights = null;
            try {
                thisUserRights = userDetailsBean.getRightsCoiv2(userName);
            } catch (CoeusException ex) {
                Logger.getLogger(GettingRightsCoiV2Service.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (riteType != null && !riteType.equals("") && riteType.equals(CoiConstants.MAINTAIN_COI_DISCL_STR)) {
                Integer val = (Integer) thisUserRights.get(DISCL);
                if (val.intValue() == 2) {
                    return true;
                }
            } else if (riteType != null && !riteType.equals("") && riteType.equals(CoiConstants.VIEW_COI_DISCL_STR)) {
                Integer val = (Integer) thisUserRights.get(DISCL);
                if (val.intValue() == 2) {
                    return true;
                }
            } else if (riteType != null && !riteType.equals("") && riteType.equals(CoiConstants.MAINTAIN_COI_DISCL_NOTES_STR)) {
                Integer val = (Integer) thisUserRights.get(NOTES);
                if (val.intValue() == 2) {
                    return true;
                }
            } else if (riteType != null && !riteType.equals("") && riteType.equals(CoiConstants.VIEW_COI_DISCL_NOTES_STR)) {
                Integer val = (Integer) thisUserRights.get(NOTES);
                if (val.intValue() == 2) {
                    return true;
                }
            } else if (riteType != null && !riteType.equals("") && riteType.equals(CoiConstants.MAINTAIN_COI_DISCL_ATTACHMENTS_STR)) {
                Integer val = (Integer) thisUserRights.get(ATTACHMENT);
                if (val.intValue() == 2) {
                    return true;
                }
            } else if (riteType != null && !riteType.equals("") && riteType.equals(CoiConstants.VIEW_COI_DISCL_ATTACHMENT_STR)) {
                Integer val = (Integer) thisUserRights.get(ATTACHMENT);
                if (val.intValue() == 2) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Function to check whether logged in person is admin
     * @param request
     * @return
     * @throws DBException
     * @throws Exception
     * @throws CoeusException
     */
    public boolean isAdmin(HttpServletRequest request) throws DBException, Exception, CoeusException, java.lang.Exception {
        boolean isAdmin = false;        
        HttpSession session = request.getSession();
        UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute(USER+session.getId());         
        if (userInfoBean != null) {
            String logInUserId = userInfoBean.getUserId();
            isAdmin = checkUserHasRight(request,logInUserId);
        }        
        session.setAttribute(CoiConstants.IS_ADMIN, isAdmin);
        return isAdmin;
    } 
    public boolean isAdmin(String userName) throws DBException, Exception, CoeusException {
        boolean isAdmin = false;
        UserDetailsBeanCoiV2 userDetailsBean = new UserDetailsBeanCoiV2();
         if (userName != null && !userName.equals("")) {
            isAdmin = userDetailsBean.isAdmin(userName);
        }
        return isAdmin;
    }
   private boolean checkUserHasRight(HttpServletRequest request,String createUser)throws Exception, java.lang.Exception{
        boolean isRight = false;       
        HashMap hmData = new HashMap();
        WebTxnBean webTxn = new WebTxnBean();
        hmData.put("userid" , createUser);       
        hmData.put("rightid" ,COI_ADMIN);
        Hashtable htHasRight =
        (Hashtable)webTxn.getResults(request, "checkProtocolRight", hmData);
        HashMap hmHasRight = (HashMap)htHasRight.get("checkProtocolRight");
        if(hmHasRight !=null && hmHasRight.size()>0){
            int canView = Integer.parseInt(hmHasRight.get("protocolResult").toString());
            
            if(canView == 1){
                isRight = true ;
            }
        }         
        return isRight;
    }
   public boolean isReviewer(HttpServletRequest request)throws DBException,Exception,CoeusException,java.lang.Exception{
       boolean isReviewer = false;
        int isReviewerCount;
        HashMap inpuMap = new HashMap();
        HttpSession session=request.getSession();
       WebTxnBean webTxn=new WebTxnBean();
        String disclosureNumber = (String) session.getAttribute("param1");
            Integer sequenceNumber = (Integer) session.getAttribute("param2");   
        inpuMap.put("disclosureNumber", disclosureNumber);
            inpuMap.put("seqNumber", sequenceNumber);
            PersonInfoBean person = (PersonInfoBean) session.getAttribute(SessionConstants.LOGGED_IN_PERSON);
            inpuMap.put("userId", person.getUserId().toUpperCase());
            String usrId = person.getUserId();
            Hashtable htResult =(Hashtable)webTxn.getResults(request,"isReviewer",inpuMap);
            HashMap  hmResult = (HashMap)htResult.get("isReviewer");
            isReviewerCount = Integer.parseInt(hmResult.get("li_count").toString());
            if(isReviewerCount==1){
            isReviewer=true;    
            
                session.setAttribute("isReviewer", true);
                session.setAttribute("reviewerUserId", usrId);
         }
            return isReviewer;
   }
}
