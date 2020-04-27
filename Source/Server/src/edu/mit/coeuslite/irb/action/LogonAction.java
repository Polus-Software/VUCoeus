/*
 * LogonAction.java
 *
 * Created on May 13, 2005, 10:53 AM
 */

package edu.mit.coeuslite.irb.action;


import edu.mit.coeus.action.common.ValidateUserAction;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.irb.bean.ReadProtocolDetails;
import edu.mit.coeuslite.irb.form.MyLogonForm;
import edu.mit.coeuslite.utils.CoeusBaseAction;
import static edu.mit.coeuslite.utils.CoeusBaseAction.AWARD_ENABLE;
import edu.mit.coeuslite.utils.CoeusLiteConstants;
import edu.mit.coeuslite.utils.SessionConstants;
import edu.mit.coeuslite.utils.bean.CoeusHeaderBean;
import edu.mit.coeuslite.utils.bean.WebTxnBean;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
//import sun.security.krb5.internal.crypto.t;

/**
 *
 * @author  chandrashekara
 */
public class LogonAction extends ValidateUserAction{
    private String DB_DRIVER;
    private String DB_URL;
    private static final String EMPTY_STRING = "";
    private static final String HEADER_ITEMS ="headerItemsVector";
    public static final String USER_ID = "userId";
    
    public static final String USER_INFO_REF = "userInfoBean";
    public static final String LOGGEDINPERSONID = "loggedinpersonid";
    
    public static final String WELCOME = "welcome";
       /*
        *  constants defined for coeusDB.Properties file.
        */
    private final String kStrDBDriver = "Driver";
    private final String kStrDBDriverUrl = "JDBCDriverUrl";
    /** Creates a new instance of LogonAction */
    public LogonAction() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        String url = request.getParameter("url");
        try{
            if(url != null){
                String loginMode = getLoginMode();
               
                if(loginMode.equals("USERID")) {
                    ActionForward forward = new ActionForward("/coeuslite/mit/irb/cwLogon.jsp");
                    return forward;
                }else {
                    //Authenticate using Cert/LDAP etc.
                    boolean isCerticateAuthenticated = performCertificateChecks(mapping, form, request, response);
                    if (isCerticateAuthenticated) {
                         getHeaderDetails(request);
                         return mapping.findForward("success");
                    } else {
                        return mapping.findForward("failure");
                    }
                   
                }
            }else {
                //Authenticate userId and password
                 getHeaderDetails(request);
                MyLogonForm logonForm = (MyLogonForm)form;
                HttpSession session = request.getSession(true);
                String strFullName = EMPTY_STRING;
                String strDepartmentNumber = EMPTY_STRING;
                String strPersonId = EMPTY_STRING;
                String strUserId = EMPTY_STRING;
                // get the user name
                String userName = logonForm.getUsername();
                // get the password
                String password = logonForm.getPassword();
                
                /* get the Driver and URL from the dbProperties file */
                initProps();
                Class.forName(DB_DRIVER);
                Connection con = DriverManager.getConnection(DB_URL, userName, password);
                //Connection con = getConnection();
                if (con != null) {
                    UserInfoBean userInfoBean = null;
                    PersonInfoBean personInfoBean = null;
                    UserDetailsBean userDetailsBean = new UserDetailsBean();
                    userInfoBean    = userDetailsBean.getUserInfo(userName.trim());
                    //  Found the user and they do exist
                    if (userInfoBean != null && userInfoBean.getUserId()!=null){
                        strFullName = userInfoBean.getUserName();
                        strUserId = userInfoBean.getUserId();
                        session.setAttribute("user"+session.getId(), userInfoBean);
                        personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
                        if (personInfoBean == null || personInfoBean.getPersonID()==null) {
                            throw new CoeusException("Could not find the person ID for the user attempting to log on, or the user may not be entered in the osp$person table.");
                        } else if (personInfoBean != null)
                            session.setAttribute("person", personInfoBean);
                        strPersonId = personInfoBean.getPersonID();
                        String loggedinUser = personInfoBean.getFullName();
                        session.setAttribute("loggedinUser", loggedinUser);
                        return mapping.findForward("success");
                    }
                }
                
            }
        }catch (Exception exception){
            exception.printStackTrace();
            request.setAttribute("Exception", exception);
            return mapping.findForward("failure");
            
        }
        return mapping.findForward("failure");
    }
    
    /**
     * Return login mode. Read login mode from coeus.properties file.
     */
    private String getLoginMode() throws CoeusException {
        String loginMode;
        try{
            loginMode = CoeusProperties.getProperty(CoeusPropertyKeys.WEB_LOGIN_MODE);
        }catch(IOException e) {
            UtilFactory.log("Can't read the properties file. " +
            "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
            throw new CoeusException("Can't read the properties file. " +
            "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
        }
        return loginMode;
    }
    
    private void getHeaderDetails(HttpServletRequest request) throws Exception {
        ServletContext application = getServlet().getServletConfig().getServletContext();
        HttpSession session = request.getSession();
        Vector headerVector;
        ReadProtocolDetails readProtocolDetails = new ReadProtocolDetails();
        headerVector = (Vector)session.getAttribute(HEADER_ITEMS);

        if (headerVector==null || headerVector.size()==0) {
            headerVector = readProtocolDetails.readXMLDataForMainHeader("/edu/mit/coeuslite/irb/xml/MainMenu.xml");
            session.setAttribute(HEADER_ITEMS, headerVector);
        }

        /**
             * check for MY Award enable
             */

             String myAwardEnabled = fetchParameterValue(request, AWARD_ENABLE);
            if(myAwardEnabled != null){
                if("0".equals(myAwardEnabled.trim())){
                    session.setAttribute("tdWidth", "525px");
                    session.setAttribute(SessionConstants.AWARD_ENABLE,CoeusLiteConstants.NO);
                    for(int i = 0; i < headerVector.size(); i++) {
                        CoeusHeaderBean headerBean = new CoeusHeaderBean();
                        headerBean = (CoeusHeaderBean)headerVector.get(i);
                        if(headerBean.getHeaderId().equals("009")) {
                           headerVector.remove(headerBean);
                        }
                    }
                   // headerVector.removeElementAt(headerVector.);
                } else if("1".equals(myAwardEnabled.trim())){
                    session.setAttribute("tdWidth", "650px");
                    session.setAttribute(SessionConstants.AWARD_ENABLE,CoeusLiteConstants.YES);
                }
            }
        
    }
    
    /**
     * Loads properties and initializes the instance with its values.
     */
    private void initProps() {
        InputStream is = getClass().getResourceAsStream("/coeus.properties");
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
        } catch (Exception e) {
            UtilFactory.log("Can't read the properties file. " +
            "Make sure db.properties is in the CLASSPATH", e,
            "LoginServlet", "initProps" );
            return;
        } finally {
            try{
                if (is !=null) {
                    is.close();
                }
            }catch (IOException ioe){
                UtilFactory.log("Can't read the properties file. " +
                "Make sure db.properties is in the CLASSPATH", ioe,
                "LoginServlet", "initProps");
            }
        }
        DB_DRIVER = dbProps.getProperty(kStrDBDriver, "ociDriver");
        DB_URL = (String) dbProps.get(kStrDBDriverUrl);
        
        if (DB_URL == null) {
            UtilFactory.log("No URL specified in coeus.properties for "
            + DB_DRIVER, null, "LoginServlet", "initProps");
            return;
        }
    }
    
    
    public boolean performCertificateChecks(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        HttpSession session = request.getSession(true);
        UserInfoBean userInfoBean = null;
        String userName = null;
        boolean errorFlag = true;
        ActionForward actionforward = null;
        String validationType = null;
        String requestedURL = (String)request.getAttribute("requestedURL");
        String loginMode = "";
        String requestURI = request.getRequestURI();
        validationType = (String)request.getAttribute("validationType");
        if(validationType == null && requestURI != null){
            if(requestURI.indexOf(CoeusConstants.LOGIN_COI_KEY) != -1){
                validationType = CoeusConstants.LOGIN_COI_KEY;
            }
            else {
                validationType = CoeusConstants.LOGIN_KEY;
            }
        } else {
            validationType = CoeusConstants.LOGIN_KEY;
        }
        //try {
            loginMode = getLoginMode(); // get login mode from coeus.properties file
        //}
        /*catch (Exception e) {
            UtilFactory.log("Can't read the properties file. " +
            "Make sure Coeus.properties is in the CLASSPATH or in the classes folder");
        }*/
        
       // try{
            if(loginMode.equalsIgnoreCase("CERTTOMCAT")) { // certificate - Tomcat Standalone.
                /* following code for Tomcat 4.0 */
                X509Certificate[] certs = (X509Certificate[])request.getAttribute(
                "javax.servlet.request.X509Certificate");
                userName = getUserName(certs,request);
                
                if(userName==null || userName.equals("")){
                    /*If certificate has no username, forward to error page.*/
                    //throw new CoeusException("exceptionCode.error.cert.no.username");
                    throw new CoeusException(" Certificate not available");
                }
            }
            
            //Change environment variable from which to parse username.
            else if(loginMode.equalsIgnoreCase("CERTAPACHE")) { // Certificate - Tomcat/Apache.
                //Get value of env variable from which to parse the username.
                String emailString = "";
                try {
                    emailString = CoeusProperties.getProperty(CoeusPropertyKeys.EMAIL_STRING);
                    
                }
                catch (Exception e) {
                    String errorMsg = "Value for EMAIL_STRING not found in coeus.properties.";
                    
                    UtilFactory.log(errorMsg, e, "ValidateUserAction", "perform");
                }
                if (request.getAttribute(emailString) != null
                && ! request.getAttribute(emailString).toString().equals("nodefault")) {
                    
                    // extract kerb ID from email address
                    int at_pos = request.getAttribute(emailString).toString().indexOf("@");
                    userName = request.getAttribute(emailString).toString().substring(0, at_pos);
                    
                }
                else {
                    throw new CoeusException("Certificate not available");
                }
            }
            else if(loginMode.equalsIgnoreCase("LDAPURL")) { // LDAP authentication - LDAP url.
                userName = getRemoteUser(request);
                
                if(userName==null || userName.equals("")){
                    /*If certificate has no username, forward to error page.*/
                    throw new CoeusException("exceptionCode.error.LDAP");
                }
            }
            UserDetailsBean userDetails = new UserDetailsBean();
            if(validationType.equalsIgnoreCase(CoeusConstants.LOGIN_KEY)){
                if(isValidCoeusUser(userName)){
                    userInfoBean = userDetails.getUserInfo(userName);
                    session.setAttribute("user"+session.getId(), userInfoBean);
                    session.setAttribute(USER_ID, userName);
                    
                    UserDetailsBean userDetailsBean = new UserDetailsBean();
                    PersonInfoBean personInfoBean  = userDetailsBean.getPersonInfo(userInfoBean.getPersonId(),false);
                    if (personInfoBean == null || personInfoBean.getPersonID()==null) {
                        throw new CoeusException("Could not find the person ID for the user attempting to log on, or the user may not be entered in the osp$person table.");
                    } else if (personInfoBean != null)
                        session.setAttribute("person", personInfoBean);
                    actionforward = mapping.findForward(WELCOME);
                    errorFlag = false;
                }
                else{
                    throw new CoeusException("Invalid user");
                }
            }
            else if(validationType.equalsIgnoreCase(CoeusConstants.LOGIN_COI_KEY)){
                PersonInfoBean personInfo = userDetails.getPersonInfo(userName);
                if(personInfo.getPersonID() != null ){
                    //setting personal details with the session object
                    session.setAttribute(PERSON_DETAILS_REF,personInfo);
                    //setting privilege of a logged in user with the session
                    session.setAttribute(PRIVILEGE,""+userDetails.getCOIPrivilege(userName));
                    //setting logged in user name with the session
                    session.setAttribute( USERNAME , userName);
                    
                    //setting logged in user's person id with the session
                    session.setAttribute(LOGGEDINPERSONID, personInfo.getPersonID());
                    //setting logged in user's person name with the session
                    String personName = personInfo.getFullName();
                    session.setAttribute(LOGGEDINPERSONNAME, personName);
                    /* CASE #1046 Begin */
                    //Check whether to show link for View Pending Disclosures
                    if(userDetails.canViewPendingDisc(userName)){
                        session.setAttribute(VIEW_PENDING_DISC, VIEW_PENDING_DISC);
                    }
                    /* CASE #1046 End */
                    actionforward = mapping.findForward(WELCOME_COI);
                    errorFlag = false;
                }
                else{
                    throw new CoeusException("Invalid person ");
                }
            }
            else{
                throw new CoeusException("exceptionCode.validationType.unavailable");
            }
            /* CASE #748 End */
            return true;
        }

       /* if(errorFlag){
            if(validationType != null &&
            validationType.equalsIgnoreCase(CoeusConstants.LOGIN_KEY)){
                //So that propdev nav bar will be displayed on Error Page.
                request.setAttribute("usePropDevErrorPage", "true");
            }
            actionforward = mapping.findForward(FAILURE);
            return  false;
        }
        else if(requestedURL != null){
            
            if(requestedURL.equalsIgnoreCase("ChangePassword.jsp")){
                actionforward = mapping.findForward(CHANGE_PASSWORD);
                //return actionforward;
            }
            
            RequestDispatcher rd = request.getRequestDispatcher(requestedURL);
            rd.forward(request, response);
        }
        //If no requestedURL exists in request object and user has been validated,
        //then forward to appropriate welcome page.
       // return actionforward;
        return true;
    }*/
    
    
    
    /**
     *  Extract user id from the certificate.
     *  Fetch all the information from all the certificates of the type 'X509Certificate'
     *  Take subject name from the certificate. This may vary with certificate to certificate
     *  Since mit certificate user id comes with email, check the subject name
     *  has the email format. ie, check for 'emailaddress' string and '@' format.
     *  If yes, parse the String and take out the user id from it.
     */
    private String getUserName(X509Certificate[] certs,HttpServletRequest req) throws CoeusException{
        String userName = null;
        if (certs != null) {
            for (int i = 0; i < certs.length; i++) {
                X509Certificate certificate = certs[i];
                String sigAlgName = certificate.getSigAlgName();
                String sigAlgOID = certificate.getSigAlgOID();
                String subName = certificate.getSubjectDN().getName();
                byte[] sigAlgParams = certificate.getSigAlgParams();
                String emailKey = subName.substring(0,12);
                if(emailKey.equalsIgnoreCase("emailaddress")
                && subName.indexOf('@')!=-1){
                    StringTokenizer stkr = new StringTokenizer(subName,",");
                    while(stkr.hasMoreElements()){
                        String token = stkr.nextToken();
                        int eqIndex = token.indexOf('=');
                        int atIndex = token.indexOf('@');
                        if(eqIndex!=-1 && atIndex!=-1){
                            userName = token.substring(eqIndex+1,atIndex);
                        }
                    }
                }
            }
            UtilFactory.log("User name from the Client Certificate = "+userName);
        }
        else {
            if ("https".equals(req.getScheme())) {
                UtilFactory.log("This was an HTTPS request, " +
                "but no client certificate is available");
                throw new CoeusException("This was an HTTPS request, " +
                "but no client certificate is available");
            }
            else {
                UtilFactory.log("This was not an HTTPS request, " +
                "so no client certificate is available");
                throw new CoeusException("This was not an HTTPS request, " +
                "so no client certificate is available");
            }
        }
        return userName;
    }
    
    
    private boolean isValidCoeusUser(String userName)
    throws CoeusException, DBException{
        ValidateUserTxnBean validateUserTxn = new ValidateUserTxnBean();
        return validateUserTxn.isThisUserValidUser(userName);
    }

    /**
     * This method is to get the Parameter Value for a particular Parameter
     * Parameter value is fetched from OSP$PARAMETER table, through the procedure get_parameter_value 
     * @throws Exception
     * @return String Parameter Value
     */
    protected String fetchParameterValue(HttpServletRequest request, String parameter)throws Exception{
        Map mpParameterName = new HashMap();
        WebTxnBean webTxnBean = new WebTxnBean();
        String value = "";
        mpParameterName.put("parameterName",parameter);
        Hashtable htParameterValue =
                (Hashtable)webTxnBean.getResults(request, "getParameterValue", mpParameterName );
        if(htParameterValue != null){
            HashMap hmParameterValue = (HashMap)htParameterValue.get("getParameterValue");
            if(htParameterValue != null){
                value = (String)hmParameterValue.get("parameterValue");
            }
        }
        return value ;
    }
    
}
