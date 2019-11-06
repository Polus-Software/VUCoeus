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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
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
public class SSLAuthService extends AuthServiceServerBase{
    private Hashtable data;
    private Properties props;
    
    /** Creates a new instance of KerberosAuthService */
    public SSLAuthService() {
    }
    public boolean authenticate() throws CoeusException {
        
        HttpServletRequest request = (HttpServletRequest)data.get(HttpServletRequest.class.getName());
        
        UtilFactory.log( "Starting SSL validation...");
        String emailKey = props.getProperty("SSL_CLIENT_EMAIL_KEY","SSL_CLIENT_S_DN_EMAILADDRESS");
        if (request.getAttribute(emailKey) == null) {
            //responder.setDataObject("Null object") ;
            UtilFactory.log( "Null object", null ,"SSLAuthService", "authenticate" );
            return false ;
        }else if (request.getAttribute(emailKey).toString().equals("nodefault")) {
            //responder.setDataObject("Not Null but no default object") ;
            UtilFactory.log( "Not Null but no default object", null ,"SSLAuthService", "authenticate" );
            return false ;
        }else {
            UtilFactory.log( "Everything is fine getting userid", null ,"SSLAuthService", "authenticate" );
            // extract kerb ID from email address
            int at_pos = request.getAttribute(emailKey).toString().indexOf("@");
            String userId = request.getAttribute(emailKey).toString().substring(0, at_pos);
            setResponseForWeb(userId);
            UserDetailsBean userDetails = new UserDetailsBean();
            try{
                PersonInfoBean personInfo = userDetails.getPersonInfo(userId);
                UserInfoBean userInfoBean = userDetails.getUserInfo(userId);
//                HashMap map = new HashMap();
//                map.put(SessionConstants.PERSON, personInfo);
//                map.put(SessionConstants.USER, userInfoBean);
//                getResponseListener().respond(map);
                return (personInfo.getPersonID()!=null || userInfoBean.getUserId()!=null);
            }catch(DBException ex){
                UtilFactory.log(ex.getMessage(),ex, "SSLAuthService", "authenticate");
                throw new CoeusException(ex.getMessage());
            }
        }
    }

    public void init(java.util.Properties props) {
        this.props = props;
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    
}
