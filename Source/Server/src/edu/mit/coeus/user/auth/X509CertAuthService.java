/*
 * KerberosAuthService.java
 *
 * Created on August 29, 2006, 3:12 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import com.oreilly.servlet.Base64Decoder;
import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.bean.PersonInfoBean;
import edu.mit.coeus.bean.UserDetailsBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeuslite.utils.SessionConstants;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  Geo Thomas
 */
public class X509CertAuthService extends AuthServiceServerBase{
    private Hashtable data;
    private Properties props;
    
    /** Creates a new instance of KerberosAuthService */
    public X509CertAuthService() {
    }
    public boolean authenticate() throws CoeusException {
        HttpServletRequest request = (HttpServletRequest)data.get(HttpServletRequest.class.getName());
        
        UtilFactory.log( "Starting SSL validation...");
        
        X509Certificate[] certs = (X509Certificate[])request.getAttribute(
                    "javax.servlet.request.X509Certificate");
        String userId = getUserName(certs,request);//"g";

        if(userId==null || userId.equals("")){
            return false;
        }else {
            UserDetailsBean userDetails = new UserDetailsBean();
            try{
                PersonInfoBean personInfo = userDetails.getPersonInfo(userId);
                UserInfoBean userInfoBean = userDetails.getUserInfo(userId);
//                HashMap map = new HashMap();
//                if(personInfo.getPersonID()!=null) map.put(SessionConstants.PERSON, personInfo);
//                if(userInfoBean.getUserId()!=null) map.put(SessionConstants.USER, userInfoBean);
//                getResponseListener().respond(map);
                setResponseForWeb(userId);
                return (personInfo.getPersonID()!=null || userInfoBean.getUserId()!=null);
            }catch(DBException ex){
                UtilFactory.log(ex.getMessage(),ex, "X509CertAuthService", "authenticate");
                throw new CoeusException(ex.getMessage());
            }
        }
    }
    
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
    

    public void init(java.util.Properties props) {
        this.props = props;
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    
}
