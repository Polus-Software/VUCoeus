/*
 * Created on Jul 1, 2004
 *
 * Created on August 8, 2006, 12:17 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.v2.util;

import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.v2.JAXWSTestHostnameVerifier;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.S2SConstants;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesPortType;
import gov.grants.apply.services.applicantwebservices_v2.ApplicantWebServicesV20;
import gov.grants.apply.soap.util.SoapUtils;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;

/**
 * @author Geo Thomas
 *
 */
public class S2SStub {
    
    private static Properties props = null;
    
    public static final String DUNS_NUMBER_KEY = "DUNS_NUMBER_KEY";
    public static final String KEY_PASS_KEY = "KEY_PASS_KEY";
    public static final String KEYSTORE = "javax.net.ssl.keyStore";
    public static final String KEYSTOREPASS = "javax.net.ssl.keyStorePassword";
    public static final String TRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String TRUSTSTOREPASS = "javax.net.ssl.trustStorePassword";
    public static final String S2S_SOAP_REQUEST_TIMEOUT = "S2S_SOAP_REQUEST_TIMEOUT";

    public static final String END_POINT = "END_POINT";
    private static String SOAP_URL_PREFIX = null;
    private static final String SOAP_URL_SUFFIX = "/grantsws-applicant/services/v2/ApplicantWebServicesSoapPort";
    
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
    public  ApplicantWebServicesPortType getApplicantSoapStub(String propNumber)
    throws Exception {
        Hashtable<String,String> ht = new Hashtable<String,String>();
        String endPoint = SoapUtils.getSoapURL();
        if(propNumber!=null && propNumber.length()>0){
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            String dunsNum = new S2SSubmissionDataTxnBean().getDunsNumber(propNumber);
            OpportunityInfoBean oppInfo = txnBean.getLocalOpportunity(propNumber);
            String submittedEndPoint = oppInfo.getSubmissionEndPoint();
            endPoint = SoapUtils.getSoapURL(submittedEndPoint,SoapUtils.SOAP_PORT);
            
            ht.put(S2SStub.DUNS_NUMBER_KEY, dunsNum);
        }
        ht.put(END_POINT, endPoint);
        return getApplicantPort(ht);
    }
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
    public  ApplicantWebServicesPortType getApplicantSoapStub()
    throws Exception {
        return getApplicantSoapStub("");
    }
    
    protected ApplicantWebServicesPortType getApplicantPort(Hashtable certParams) throws Exception {
		ApplicantWebServicesV20 service = new ApplicantWebServicesV20();
		ApplicantWebServicesPortType port = service.getApplicantWebServicesSoapPort( new MTOMFeature( true ) ); // Enable MTOM;
		BindingProvider bp = ( BindingProvider ) port;
                
                final Map<String, Object> requestContext = bp.getRequestContext();
                String timeOutStr = new CoeusFunctions().getParameterValue(S2S_SOAP_REQUEST_TIMEOUT);
                int timeOut = (timeOutStr==null||timeOutStr.equals(""))?3:Integer.parseInt(timeOutStr);
                String endPoint = (String)certParams.get(END_POINT);
                requestContext.put( BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPoint );
		requestContext.put( "com.sun.xml.internal.ws.transport.http.client.streaming.chunk.size", 8192 );// enables streaming
		requestContext.put( "com.sun.xml.internal.ws.request.timeout", timeOut*60*1000 );
                requestContext.put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",new S2SSSLSocketFactory(certParams ));
		HttpsURLConnection.setDefaultHostnameVerifier( new JAXWSTestHostnameVerifier() );
		return port;
	}// getApplicantPort
	
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
//    public  ApplicantWebServicesV20 getApplicantSoapStub(Hashtable certParams)
//    throws Exception {
//        ApplicantIntegrationServicesLocator service = new ApplicantIntegrationServicesLocator(
//                            new S2SEngineConfiguration(certParams));
//        
//        String endPoint = (String)certParams.get(END_POINT);
//        ApplicantIntegrationSoapBindingStub stub = new ApplicantIntegrationSoapBindingStub(
//                            new URL(SoapUtils.getSoapURL(endPoint,null)),  service);
////        String timeOutStr = SoapUtils.getProperty("REQUEST_TIMEOUT");
//        
//        String timeOutStr = new CoeusFunctions().getParameterValue(S2S_SOAP_REQUEST_TIMEOUT);
//        int timeOut = (timeOutStr==null||timeOutStr.equals(""))?3:Integer.parseInt(timeOutStr);
//        stub.setTimeout(timeOut*60*1000);
//        return stub;
//    }
//    
//    class S2SEngineConfiguration extends SimpleProvider {
//        public S2SEngineConfiguration(Hashtable param) {
//            super();
//            HTTPSender hs = new HTTPSender();
//            hs.setOptions(param);
//            this.deployTransport("http", new SimpleTargetedChain(hs));
//            
//        }
//        public void configureEngine(AxisEngine engine) throws
//        ConfigurationException {
//            engine.refreshGlobalOptions();
//        }
//    }
    
}