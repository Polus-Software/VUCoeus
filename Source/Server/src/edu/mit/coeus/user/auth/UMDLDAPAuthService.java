/*
 * KerberosAuthService.java
 *
 * Created on August 29, 2006, 3:12 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.bean.ValidateUserTxnBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * @author  Geo Thomas
 */
public class UMDLDAPAuthService extends AuthServiceServerBase{
    private Hashtable data;
    private Properties props;
    private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String LDAP_PROVIDER_URL = "ldap://loclhost:123/";
    private static final String LDAP_SECURITY_AUTH_MODE = "simple";
    private static final String LDAP_SECURITY_PROTOCOL = "ssl";
    private static final String LDAP_FILTER_UID_NAME = "samAccountName";
    private static final String LDAP_DN_KEY = "distinguishedName";
    private static DirContext ctx;
    
    /** Creates a new instance of KerberosAuthService */
    public UMDLDAPAuthService() {
    }
    private void createInitialDirContext() throws CoeusException{
       if(ctx!=null) return;
        try{
            UtilFactory.log("Entering ldap auth");
            // Set up the environment for creating the initial context
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, props.getProperty("LDAP_CONTEXT_FACTORY",LDAP_CONTEXT_FACTORY));
            env.put(Context.PROVIDER_URL, props.getProperty("LDAP_PROVIDER_URL",LDAP_PROVIDER_URL));
//
            env.put(Context.SECURITY_AUTHENTICATION, props.getProperty("LDAP_SECURITY_AUTH_MODE",LDAP_SECURITY_AUTH_MODE));
            env.put(Context.SECURITY_PROTOCOL, props.getProperty("LDAP_SECURITY_PROTOCOL",LDAP_SECURITY_PROTOCOL));
//            System.setProperty("javax.net.debug", "all");
//            System.setProperty("javax.net.ssl.trustStore", CoeusProperties.getProperty("TRUSTSTORE_FILE",TRUSTSTORE_FILE));

           if(props.getProperty("LDAP_DEBUG")!=null)
                System.setProperty("javax.net.debug", props.getProperty("LDAP_DEBUG"));
            if(props.getProperty("COEUS_LDAP_ADMIN_DN")!=null)
                env.put(Context.SECURITY_PRINCIPAL, props.getProperty("COEUS_LDAP_ADMIN_DN"));
            if(props.getProperty("COEUS_LDAP_ADMIN_CREDENTIALS")!=null)
                env.put(Context.SECURITY_CREDENTIALS, props.getProperty("COEUS_LDAP_ADMIN_CREDENTIALS"));

            UtilFactory.log("going to create admin ldap context");
            // Create the initial context
            ctx = new InitialDirContext(env);
            UtilFactory.log("created admin ldap context");
        }catch(NamingException nEx){
            nEx.printStackTrace();
            UtilFactory.log(nEx.getMessage(),nEx,"LDAPAuthentication", "LDAPAuthentication");
            throw new CoeusException("exceptionCode.100003");
        }
    }
    public boolean authenticate() throws CoeusException {
        createInitialDirContext();
        
        RequesterBean requester = (RequesterBean)data.get(RequesterBean.class.getName());
        LoginBean loginBean = (LoginBean)requester.getDataObject();
        String userId = loginBean.getUserId();
        String password = loginBean.getPassword();
        try{
            UtilFactory.log("going to search user specific record with userid "+userId);
            
            String filter = "(&("+props.getProperty("LDAP_FILTER_UID_NAME",LDAP_FILTER_UID_NAME)+"="+userId+"))";
            UtilFactory.log("filter for the search=> "+filter);
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration answer = ctx.search(props.getProperty("LDAP_DOMAIN_BASE"), filter,ctls);
            
            String userDN = null;
            Attributes attrs = null;
            SearchResult sr = null;
            while (answer.hasMore()) {
                UtilFactory.log("Found some records from the search");
                sr = (SearchResult)answer.next();
                attrs = sr.getAttributes();
                userDN = (String)attrs.get(props.getProperty("LDAP_DN_KEY",LDAP_DN_KEY)).get();
                UtilFactory.log("UserDN from the search result=> "+userDN);
                break;
            }
            NamingEnumeration  ids = attrs.getIDs();
//            while (ids.hasMore()) {
//                String id = ids.next().toString();
//                System.out.println(attrs.get(id));
//                UtilFactory.log("Attributes from search result=> "+attrs.get(id));
//            }
            if(userDN!=null && sr!=null){
                userDN= props.getProperty("LDAP_DN_KEY",LDAP_DN_KEY)+"="+userDN+
                        "," + props.getProperty("LDAP_DOMAIN_BASE");
                UtilFactory.log("UserDN after appending with LDAP_DOMAIN_BASE=> "+userDN);
/* Begin EDY Changes 07-06-06 */
/* Instead of creating a new Directory Context for the user, do an LDAP compare
   of the user's cn values and password value
*/
               // Create new search contraints for LDAP compare operation
               SearchControls pwConstraints = new SearchControls();
               pwConstraints.setSearchScope(SearchControls.OBJECT_SCOPE);
               pwConstraints.setReturningAttributes(new String[0]); // Return no attrs
               // get the search results
               String commonName = (String)sr.getName();
               /** Modify the original search base to include the cn value returned */
               String pwSearchBase = commonName + "," + props.getProperty("LDAP_DOMAIN_BASE");
               NamingEnumeration pwResults =
                 ctx.search(pwSearchBase, "(userPassword={0})"
                 , new Object[] {password}, pwConstraints);
               // If the compare is successful, the resulting enumeration
               // will contain a single item
               if ((pwResults != null) && pwResults.hasMoreElements()) {
                 // Password compare was successful
                 // Next determine if user is an authorized coeus user
                 UtilFactory.log("User "+userId+"logged in");
               } // end if (pwResults != null)
               else {
                 // the emplid was found in the directory, but the password
                 // did not match the one listed for this emplid in the
                 // directory
                 throw new CoeusException("exceptionCode.100002");
               } // end else !pwResults.hasMoreElements()


/* End EDY Changes 07-06-06 */

            }else{
                throw new CoeusException("exceptionCode.100000");
            }
        }catch(NamingException nEx){
            
            UtilFactory.log(nEx.getMessage(),nEx,"UMDLDAPAuthentication","authenticate");
            if(nEx.getMessage().indexOf(" 525")!=-1){
                throw new CoeusException("exceptionCode.100000");
            }else{
                throw new CoeusException("exceptionCode.100002");
            }
        }
        setResponse(userId);
        UtilFactory.log("LDAP Authentication successful");
        return true;
    }
    public void init(java.util.Properties props) {
        this.props = props;
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    
}
