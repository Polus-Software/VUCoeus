/*
 * StartupServlet.java
 *
 * Created on November 29, 2004, 12:46 PM
 */

package edu.mit.coeus.servlet;

import edu.mit.coeus.codetable.bean.CodeTableTxnBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.scheduler.SchedulerEngine;
import edu.mit.coeus.utils.security.CoeusCipher;
import edu.mit.coeus.utils.security.SecureSeedTxnBean;
import gov.grants.apply.soap.util.SoapUtils;
import java.io.*;
import java.net.*;
import java.util.TimeZone;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  geot
 * @version
 */
public class StartupServlet extends HttpServlet implements SingleThreadModel{
    private static final String COEUS_PROP_FILE = "/coeus.properties";
    private SchedulerEngine scheduler;
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // COEUSQA-2116: Automate_email_notifications_for_annual_renewal_and_IRB_notification_reminders
        CoeusConstants.SERVER_HOME_PATH = getServletContext().getRealPath("/");
        TimeZone utcZone = TimeZone .getTimeZone( "UTC" ) ;
        TimeZone.setDefault(utcZone);
        //COEUSQA-1372 - START
        UtilFactory.log("Local Timezone=>"+UtilFactory.getLocalTimeZoneId());
        //COEUSQA-1372 - END
        configureSSL();
        configureSoap();
        try{
            scheduler = SchedulerEngine.getInstance();
            scheduler.startAllServices();
            getServletContext().setAttribute("Scheduler", scheduler);
            SecureSeedTxnBean secureSeedTxnBean = new SecureSeedTxnBean();
            secureSeedTxnBean.insertSecureSeed();
            String authKey = secureSeedTxnBean.getServerSecureSeedValue(
                            CoeusCipher.SERVLET_SECURE_SEED);
            getServletContext().setAttribute(CoeusCipher.SERVLET_SECURE_SEED, authKey);
            
        }catch(CoeusException ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "StartupServlet", "processRequest");
        }catch(DBException ex){
            ex.printStackTrace();
            UtilFactory.log(ex.getMessage(),ex, "StartupServlet", "processRequest");
        }
        //COEUSQA:1691 - Front End Configurations to CoeusLite Pages - Start
        //The replaceAllMessageProperties() will be called on server startup to update all the message properties files
        //like ArraMessages.properties, BudgetMessages.properties, COIMessages.properties, ProposalMessages.properties
        //IRBMessages.properties, IACUCProtocolMessages.properties, NegotiationMessages.properties 
        //SubcontractMessages.properties from database.
        CodeTableTxnBean codeTabletxnBean = new CodeTableTxnBean();
        try {
            codeTabletxnBean.replaceAllMessageProperties();
        } catch(Exception ex){            
            UtilFactory.log(ex.getMessage(),ex, "StartupServlet", "replaceAllMessageProperties");
        }
        //COEUSQA:1691 - End
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Init servlet for coeus application";
    }
    private void configureSoap() {
        String path= getServletContext().getRealPath("/").replace( '\\', '/');
        String soapServerPropertyFile =path+"/"+"WEB-INF"+"/"+S2SConstants.SOAP_SERVER_PROPERTY_FILE;
        SoapUtils.setSoapServerPropFile( soapServerPropertyFile );
        java.lang.System.setProperty("javax.xml.soap.MessageFactory",
        "org.apache.axis.soap.MessageFactoryImpl");
        
        java.lang.System.setProperty("javax.xml.soap.SOAPConnectionFactory",
        "org.apache.axis.soap.SOAPConnectionFactoryImpl");
        
    }
    private void configureSSL() throws ServletException{
        try{
            if(SoapUtils.getProperty("javax.net.ssl.keyStore")!=null){
                System.setProperty("javax.net.ssl.keyStore",
                SoapUtils.getProperty("javax.net.ssl.keyStore"));
                System.setProperty("javax.net.ssl.keyStorePassword",
                SoapUtils.getProperty("javax.net.ssl.keyStorePassword"));
            }
            if(SoapUtils.getProperty("javax.net.ssl.trustStore")!=null){
                System.setProperty("javax.net.ssl.trustStore", SoapUtils
                .getProperty("javax.net.ssl.trustStore"));
                System.setProperty("javax.net.ssl.trustStorePassword", SoapUtils
                .getProperty("javax.net.ssl.trustStorePassword"));
            }
        }catch(IOException ioEx){
            UtilFactory.log(ioEx.getMessage(),ioEx,"S2SServlet","ConfigureSSL");
            throw new ServletException(ioEx.getMessage());
        }
    }
    
}
