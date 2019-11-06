/*
 * Created on Jul 1, 2004
 *
 * Created on August 8, 2006, 12:17 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.util;

import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.S2SConstants;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.*;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationServicesLocator;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.soap.util.SoapUtils;
import gov.grants.apply.struts.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import javax.net.ssl.*;
import org.apache.axis.*;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.*;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.transport.http.*;
import org.apache.log4j.Logger;

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
    
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
    public  ApplicantIntegrationSoapBindingStub getApplicantSoapStub(String propNumber)
    throws Exception {
        Hashtable ht = new Hashtable();
        String endPoint = S2SConstants.SOAP_HOST;
        if(propNumber!=null && propNumber.length()>0){
            S2SSubmissionDataTxnBean txnBean = new S2SSubmissionDataTxnBean();
            String dunsNum = new S2SSubmissionDataTxnBean().getDunsNumber(propNumber);
            OpportunityInfoBean oppInfo = txnBean.getLocalOpportunity(propNumber);
            endPoint = oppInfo.getSubmissionEndPoint();
            ht.put(S2SStub.DUNS_NUMBER_KEY, dunsNum);
        }
        ht.put(END_POINT, endPoint);
        return getApplicantSoapStub(ht);
    }
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
    public  ApplicantIntegrationSoapBindingStub getApplicantSoapStub()
    throws Exception {
        return getApplicantSoapStub("");
    }
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
    public  ApplicantIntegrationSoapBindingStub getApplicantSoapStub(Hashtable certParams)
    throws Exception {
        ApplicantIntegrationServicesLocator service = new ApplicantIntegrationServicesLocator(
                            new S2SEngineConfiguration(certParams));
        
        String endPoint = (String)certParams.get(END_POINT);
        ApplicantIntegrationSoapBindingStub stub = new ApplicantIntegrationSoapBindingStub(
                            new URL(SoapUtils.getSoapURL(endPoint,null)),  service);
//        String timeOutStr = SoapUtils.getProperty("REQUEST_TIMEOUT");
        
        String timeOutStr = new CoeusFunctions().getParameterValue(S2S_SOAP_REQUEST_TIMEOUT);
        int timeOut = (timeOutStr==null||timeOutStr.equals(""))?3:Integer.parseInt(timeOutStr);
        stub.setTimeout(timeOut*60*1000);
        return stub;
    }
    
    class S2SEngineConfiguration extends SimpleProvider {
        public S2SEngineConfiguration(Hashtable param) {
            super();
            HTTPSender hs = new HTTPSender();
            hs.setOptions(param);
            this.deployTransport("http", new SimpleTargetedChain(hs));
            
        }
        public void configureEngine(AxisEngine engine) throws
        ConfigurationException {
            engine.refreshGlobalOptions();
        }
    }
    
}