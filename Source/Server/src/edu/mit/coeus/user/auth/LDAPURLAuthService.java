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
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import java.io.IOException;
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
public class LDAPURLAuthService extends AuthServiceServerBase{
    private Hashtable data;
    private Properties props;
    
    /** Creates a new instance of KerberosAuthService */
    public LDAPURLAuthService() {
    }
    public boolean authenticate() throws CoeusException {

        HttpServletRequest request = (HttpServletRequest)data.get(HttpServletRequest.class.getName());
        UtilFactory.log( "Starting LDAPURL validation...");
        String name = "REMOTE_USER"; // user information in header
        String cwid = getRemoteUser(request);
        setResponse(cwid);
        return true;
    }
    public static String getRemoteUser(HttpServletRequest request) {
        String cwid = "CWID HERE!";
        String auth = request.getHeader("authorization"); // as send by apache
        if (auth != null) {
            try {
                auth = Base64Decoder.decode(auth.substring(6));
            }
            catch (Exception exc) {
                exc.printStackTrace();
                return null;
            }
        }
        int p = 0;
        if (auth != null)
            p = auth.indexOf(":");
        if (p > 0)
            cwid = auth.substring(0, auth.indexOf(":"));
        return cwid;
    }
    public void init(java.util.Properties props) {
        this.props = props;
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    
}
