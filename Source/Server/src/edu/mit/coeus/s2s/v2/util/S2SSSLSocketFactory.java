/*
 * S2SSecureSocketFactory.java
 *
 * Created on July 31, 2006, 3:44 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.v2.util;

import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;

import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.*;
import java.util.Hashtable;

import javax.net.ssl.*;


/**
 *
 * @author  Geo Thomas
 */
public class S2SSSLSocketFactory extends SSLSocketFactory{
    private Hashtable certParams;
    public S2SSSLSocketFactory(Hashtable certParams) {
        this.certParams = certParams;
    }
    public java.net.Socket create(java.lang.String host,
                              int port)
                       throws IOException {
       System.out.println("In S2S Secure Socket Factory"); 
        KeyStore ks;
        SSLContext sslContext=null;
        try {
            ks = KeyStore.getInstance("JKS");
        
        String certAlias = certParams==null?null:(String)certParams.get(S2SStub.DUNS_NUMBER_KEY);
        String keyPass = certParams==null?null:(String)certParams.get(S2SStub.KEY_PASS_KEY);
        if(keyPass==null)
            keyPass = gov.grants.apply.soap.util.SoapUtils.getProperty(S2SStub.KEYSTOREPASS);
        
        ks.load(new FileInputStream(gov.grants.apply.soap.util.SoapUtils.getProperty(S2SStub.KEYSTORE)), 
                            gov.grants.apply.soap.util.SoapUtils.getProperty(S2SStub.KEYSTOREPASS).toCharArray());
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore ks1 = null;
        String multiCampStr = "0";
        try{
            multiCampStr = new CoeusFunctions().getParameterValue("MULTI_CAMPUS_ENABLED");
        }catch(DBException ex){
            //do nothing
            //by default mulitcampus is disabled
        }
        boolean mulitCampusEnabled = multiCampStr==null||Integer.parseInt(multiCampStr)==0?false:true;
        if(mulitCampusEnabled&certAlias!=null){
            ks1 = KeyStore.getInstance("JKS");
            java.security.cert.Certificate cer[] = ks.getCertificateChain(certAlias);
            UtilFactory.log("Multi campus is enabled");
            if(cer==null || cer.length==0){
                throw new edu.mit.coeus.exception.CoeusException("No certificate found for the organization duns: "+certAlias);
            }
            UtilFactory.log("Certificate Type=> "+cer[0].getType());
            UtilFactory.log("Duns number/ Certificate Alias => "+certAlias);
            if(cer[0] instanceof X509Certificate){
                try{
                byte[] hxBytes = ((X509Certificate)cer[0]).getSerialNumber().toByteArray();
                BigInteger bi = new BigInteger(hxBytes);
                // Format to hexadecimal
                String s = bi.toString(16);            // 120ff0
                if (s.length() % 2 != 0) {
                    // Pad with 0
                    s = "0"+s;
                }
                UtilFactory.log("X509 Certificate serial number Hex value =>"+s.toUpperCase());
                }catch(Exception ex){
                    UtilFactory.log("X509 Certificate serial number not able to convert to hex");
                    UtilFactory.log("X509 Certificate serial number string=>"+((X509Certificate)cer[0]).getSerialNumber().toString());
                }
            }
            Key k = ks.getKey(certAlias, keyPass.toCharArray());
            UtilFactory.log("Key algorithm=>"+k.getAlgorithm());
            //java.security.cert.Certificate[] cerArr = {cer};
            ks1.load(null, null);
            ks1.setKeyEntry(certAlias,k,keyPass.toCharArray(), cer);
            kmf.init(ks1, keyPass.toCharArray());
        }else{
            kmf.init(ks, keyPass.toCharArray());
        }
        
        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(new FileInputStream(gov.grants.apply.soap.util.SoapUtils.getProperty(S2SStub.TRUSTSTORE)),
        gov.grants.apply.soap.util.SoapUtils.getProperty(S2SStub.TRUSTSTOREPASS).toCharArray());
        TrustManager tm[];
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ts);
        tm = tmf.getTrustManagers();
        //sslContext = SSLContext.getInstance("SSL"); // JM 8-7-2015 new for SHA2
        sslContext = SSLContext.getInstance("TLSv1.2"); // JM
         
        sslContext.init(kmf.getKeyManagers(), tm, null);
        } catch (Exception ex) {
            UtilFactory.log(ex.getMessage(), ex, "S2SSecureSocketFactory", "create");
            throw new RuntimeException(ex.getMessage());
        }
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        Socket sk = sslSocketFactory.createSocket(host, port);
        return sk;
    }
    
    /**
     * Getter for property certParams.
     * @return Value of property certParams.
     */
    public java.util.Hashtable getCertParams() {
        return certParams;
    }
    
    /**
     * Setter for property certParams.
     * @param certParams New value of property certParams.
     */
    public void setCertParams(java.util.Hashtable certParams) {
        this.certParams = certParams;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return HttpsURLConnection.getDefaultSSLSocketFactory().getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return HttpsURLConnection.getDefaultSSLSocketFactory().getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean bln) throws IOException{
        return createSocket(host,port);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return create(host,port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress ia, int i1) throws IOException, UnknownHostException {
        return createSocket(host,port);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
