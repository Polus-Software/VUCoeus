/*
 * LDAPAuthentication.java
 *
 * Created on February 16, 2006, 3:20 PM
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusProperties;
import edu.mit.coeus.utils.UtilFactory;
import java.io.IOException;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * @author  Geo Thomas
 * This class performs LDAP authentication for Coeus, which allows users to
 * use their single sign on id login to Coeus.
 *
 */
public class LDAPAuthentication {
    private static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
    private static final String LDAP_PROVIDER_URL = "ldap://loclhost:123/";
    private static final String LDAP_SECURITY_AUTH_MODE = "simple";
    private static final String LDAP_SECURITY_PROTOCOL = "ssl";
    private static final String LDAP_FILTER_UID_NAME = "samAccountName";
    private static final String LDAP_DN_KEY = "distinguishedName";
    private static DirContext ctx;
    /** Creates a new instance of LDAPAuthentication */
    public LDAPAuthentication() throws CoeusException{
        init();
    }
    private void init() throws CoeusException{
        try{
            // Set up the environment for creating the initial context
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, CoeusProperties.getProperty("LDAP_CONTEXT_FACTORY",LDAP_CONTEXT_FACTORY));
            env.put(Context.PROVIDER_URL, CoeusProperties.getProperty("LDAP_PROVIDER_URL",LDAP_PROVIDER_URL));
//
            env.put(Context.SECURITY_AUTHENTICATION, CoeusProperties.getProperty("LDAP_SECURITY_AUTH_MODE",LDAP_SECURITY_AUTH_MODE));
            env.put(Context.SECURITY_PROTOCOL, CoeusProperties.getProperty("LDAP_SECURITY_PROTOCOL",LDAP_SECURITY_PROTOCOL));
//    //        System.setProperty("javax.net.debug", "all");
//            System.setProperty("javax.net.ssl.trustStore", CoeusProperties.getProperty("TRUSTSTORE_FILE",TRUSTSTORE_FILE));

            if(CoeusProperties.getProperty("COEUS_LDAP_ADMIN_DN")!=null)
                env.put(Context.SECURITY_PRINCIPAL, CoeusProperties.getProperty("COEUS_LDAP_ADMIN_DN"));
            if(CoeusProperties.getProperty("COEUS_LDAP_ADMIN_CREDENTIALS")!=null)
                env.put(Context.SECURITY_CREDENTIALS, CoeusProperties.getProperty("COEUS_LDAP_ADMIN_CREDENTIALS"));

//            System.setProperty("javax.net.debug", "all");
            if(CoeusProperties.getProperty("TRUSTSTORE_FILE")!=null)
                System.setProperty("javax.net.ssl.trustStore", CoeusProperties.getProperty("TRUSTSTORE_FILE"));
        
            // Create the initial context
            ctx = new InitialDirContext(env);
        }catch(NamingException nEx){
            nEx.printStackTrace();
            UtilFactory.log(nEx.getMessage(),nEx,"LDAPAuthentication", "LDAPAuthentication");
            throw new CoeusException("exceptionCode.100003");
        }catch(IOException ioEx){
            UtilFactory.log(ioEx.getMessage(),ioEx,"LDAPAuthentication","authenticate");
            throw new CoeusException(ioEx.getMessage());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CoeusException{
        new LDAPAuthentication().authenticate("userid","password");
    }
    public boolean authenticate(String userId,String password) throws CoeusException{
        try{
            if(ctx==null) init();
            String filter = "(&("+CoeusProperties.getProperty("LDAP_FILTER_UID_NAME",LDAP_FILTER_UID_NAME)+"="+userId+"))";
            SearchControls ctls = new SearchControls();
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration answer = ctx.search(CoeusProperties.getProperty("LDAP_DOMAIN_BASE"), filter,ctls);
            
            String userDN = null;
            Attributes attrs = null;
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult)answer.nextElement();
                if(sr!=null)
                    attrs = sr.getAttributes();
                if(attrs!=null){
//                    userDN = (String)attrs.get(CoeusProperties.getProperty("LDAP_DN_KEY",LDAP_DN_KEY)).get();
                    userDN = sr.getName() + "," +CoeusProperties.getProperty("LDAP_DOMAIN_BASE");
                    break;
                }
            }
            
            if(userDN!=null){
                
                Hashtable envUser = new Hashtable();
                envUser.put(Context.INITIAL_CONTEXT_FACTORY, CoeusProperties.getProperty("LDAP_CONTEXT_FACTORY"));
                if(CoeusProperties.getProperty("LDAP_PROVIDER_URL")!=null)
                    envUser.put(Context.PROVIDER_URL, CoeusProperties.getProperty("LDAP_PROVIDER_URL"));
                if(CoeusProperties.getProperty("LDAP_SECURITY_AUTH_MODE")!=null)
                    envUser.put(Context.SECURITY_AUTHENTICATION, CoeusProperties.getProperty("LDAP_SECURITY_AUTH_MODE"));
                envUser.put(Context.SECURITY_PRINCIPAL, userDN);
                envUser.put(Context.SECURITY_CREDENTIALS, password);
                try{
                    DirContext userCtx = new InitialDirContext(envUser);
                }catch(AuthenticationException authEx){
                    UtilFactory.log(authEx.getMessage(),authEx,"LDAPAuthentication","authenticate");
                    String msg = authEx.getExplanation();
                    if(msg.indexOf(" 52e")!=-1){
                        throw new CoeusException("exceptionCode.100002");
                    }else{
                        throw new CoeusException(authEx.getMessage());
                    }
                }
                UtilFactory.log("User "+userId+"logged in");
            }else{
                throw new CoeusException("exceptionCode.100000");
            }
        }catch(NamingException nEx){
            
            UtilFactory.log(nEx.getMessage(),nEx,"LDAPAuthentication","authenticate");
            if(nEx.getMessage().indexOf(" 525")!=-1){
                throw new CoeusException("exceptionCode.100000");
            }else{
                throw new CoeusException("exceptionCode.100002");
            }
        }catch(IOException ioEx){
            UtilFactory.log(ioEx.getMessage(),ioEx,"LDAPAuthentication","authenticate");
            throw new CoeusException(ioEx.getMessage());
        }
        UtilFactory.log("LDAP Authentication successful");
        return true;
        
    }
}
