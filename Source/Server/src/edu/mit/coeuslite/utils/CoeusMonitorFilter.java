/*
 * CoeusMonitorFilter.java
 *
 * Created on August 16, 2006, 2:23 PM
 */

package edu.mit.coeuslite.utils;

/**
 *
 * @author  chandrashekara
 */
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.http.*;


public class CoeusMonitorFilter implements Filter {
    private ServletContext ctx;
//    private HttpSession session;
    private static final String HEADER_ITEMS ="headerItemsVector";
    public void init(javax.servlet.FilterConfig   filterConfig) throws ServletException {
        ctx = filterConfig.getServletContext();
        ctx.log("Filter " + filterConfig.getFilterName() + " initialized.");
    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws  IOException, ServletException {
        HttpServletRequest request      = (HttpServletRequest)servletRequest;
        HttpServletResponse resp    = (HttpServletResponse)servletResponse;
        request.removeAttribute(CoeusLiteConstants.RELEASE_LOCK);
        HttpSession session    = request.getSession();
        LockBean objLockBean = (LockBean)session.getAttribute(CoeusLiteConstants.LOCK_BEAN+session.getId());
        String menuID = request.getParameter("Menu_Id");
//        If Condition modified for case#3236 - Unlocking of Amendment/renewal
//        if (menuID != null && objLockBean != null ){
        if (menuID != null && !"000".equals(menuID) && objLockBean != null){
            request.setAttribute(CoeusLiteConstants.RELEASE_LOCK, CoeusLiteConstants.YES);
        }
        //filterChain.doFilter(request, resp);

        //These Files are bypassed for session/Authentication
        String noAuthenticationHost[] = {"SurveyComplete.jsp","SurveyOptout.jsp","userSecondaryAuthAction.do","CitiLogin.jsp"};
        String remoteHost = ((HttpServletRequest)servletRequest).getRequestURI();
        boolean bypassAuthentication = false;
        for(int index=0;index<noAuthenticationHost.length;index++){
            if(remoteHost.indexOf(noAuthenticationHost[index]) > -1){
                bypassAuthentication = true;
                break;
            }
        }
        if(!bypassAuthentication){
        //JIRA COEUSQA-2864 - START
        if(session.isNew()){
            //Return to Login Page with Session Expired Message
            //if its requesting LoginPage don't need to test.
            //if the pages are not .do or .jps, no need to test. Coeus premium uses servlets which doesn't maintain session.
            //JIRA COEUSQA-3409 - avoid rightPersonCertify action from this checking
    //        String remoteHost = ((HttpServletRequest)servletRequest).getRequestURI();
            if(remoteHost != null && remoteHost.indexOf("userAuthAction") == -1 && remoteHost.indexOf("rightPersonCertify") == -1 && remoteHost.indexOf("getPHSHumanSubjectForm") == -1 && (remoteHost.indexOf(".do") > -1 || remoteHost.indexOf(".jsp") > -1)) {
                RequestDispatcher reqDisp = servletRequest.getRequestDispatcher("/userAuthAction.do?reason=sessionExpired");
                reqDisp.forward(servletRequest, servletResponse);
                return;
            }
            }
        }
        //JIRA COEUSQA-2864 - END

        //JIRA COEUSQA-2770 - START
        /*if(request.getAttribute(CoeusSkipFilter.SKIP_XSS_SCAN) != null){
            //Skip Request Scanning
            filterChain.doFilter(request, resp);
        }else {*/
            filterChain.doFilter(new CoeusRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
        //}
        //JIRA COEUSQA-2770 - END
    }
    public void destroy() {
        // final operations to be done if container is closed abruptly
    }
 
}
