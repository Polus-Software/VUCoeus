/*
 * KerberosClientAuthService.java
 *
 * Created on August 29, 2006, 4:13 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.applet.CoeusKerberosCallbackHandler;
import edu.mit.coeus.applet.KerberosLoginWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusPropertyKeys;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.swing.JDialog;
import javax.swing.JFrame;
import sun.security.krb5.internal.KrbApErrException;

/**
 *
 * @author  Geo Thomas
 */
public class KerberosClientAuthService extends ClientAuthServiceHelper{
    private Hashtable data;
    private Properties props;
    private static final String jaasFile = "/edu/mit/coeus/resources/jaas.conf";
    private CoeusKerberosCallbackHandler coeusKerbHandler;
    private Frame parentFrame;
    private static String CANCEL_MESSAGE = CoeusMessageResources.getInstance().parseMessageKey("kerberosLoginScreenCancelCode.100");//"User Clicked Cancel";
    /** Creates a new instance of KerberosClientAuthService */
    public KerberosClientAuthService() {
    }
    public void addParam(Object key, Object value) {
        if(data==null) data = new Hashtable();
        data.put(key, value);
    }
    
    public boolean authenticate() throws edu.mit.coeus.exception.CoeusException {
        String userId = validateKerberosUserId();
        RequesterBean request = new RequesterBean();
        String loginMode = (String)data.get("LOGIN_MODE");
        request.setId(loginMode) ;
        request.setDataObject(userId);
        
        return connectAndValidate(request);
    }
    
    public void init(java.util.Properties props) {
        this.props = props;
    }
    
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    public String validateKerberosUserId() throws CoeusException{
        Properties sysProps = new Properties(System.getProperties());
        try{
            String kerbRealm = (String)props.get(CoeusPropertyKeys.KERBEROS_REALM);
            String kerbServer = (String)props.get(CoeusPropertyKeys.KERBEROS_KDC_SERVER);
            sysProps.setProperty(CoeusPropertyKeys.KERBEROS_REALM, kerbRealm);
            sysProps.setProperty(CoeusPropertyKeys.KERBEROS_KDC_SERVER, kerbServer);
            
            File userDir = new File(System.getProperty("user.home"));
            File confFile = new File(userDir,"CoeusJaas.conf");
            if(System.getProperty("java.security.auth.login.config")==null){
                if(!confFile.exists()){
                    InputStream isJaas = getClass().getResourceAsStream(jaasFile);
                    byte[] jaasBytes = new byte[isJaas.available()];
                    isJaas.read(jaasBytes);
                    isJaas.close();
                    FileOutputStream fops = new FileOutputStream(confFile);
                    fops.write(jaasBytes);
                    fops.flush();
                }
                sysProps.put("java.security.auth.login.config", confFile.getAbsolutePath());
            }
        }catch(IOException ioEx){
            ioEx.printStackTrace();
            throw new CoeusException(ioEx.getMessage());
        }
        System.setProperties(sysProps);
        try{
            parentFrame = (Frame)data.get("PARENT_FRAME");
            coeusKerbHandler  = new CoeusKerberosCallbackHandler(parentFrame);
//            coeusKerbHandler.setInitialFocus();
            return invokeLogin();
        }catch(LoginException ex){
            ex.printStackTrace();
            throw new CoeusException(ex.getMessage());
        }
    }
    private int loginTryCount = 0;
    private String invokeLogin() throws LoginException{
        ++loginTryCount;
        String kerbPrincipal = "";
        String userId = "";
        try{
            LoginContext lc = new LoginContext("CoeusKerbClient", coeusKerbHandler);
            lc.login();
            System.out.println("login successfull and trying to parse kerberos principal");
            Set kerbPrincipals = lc.getSubject().getPrincipals();
            if(kerbPrincipals!=null && !kerbPrincipals.isEmpty()){
                kerbPrincipal = kerbPrincipals.iterator().next().toString();
                if(kerbPrincipal.indexOf('@')==-1){
                    throw new Exception("Not able to extract user id from the principal : "+kerbPrincipal);
                }else{
                    userId = kerbPrincipal.substring(0,kerbPrincipal.indexOf('@'));
                }
            }else{
                throw new LoginException("Not able to get the principal");
            }
        }catch(LoginException lEx){
            if(coeusKerbHandler.isCancelClicked()) throw new LoginException(CANCEL_MESSAGE);//return "User Clicked Cancel"; //User Clicked on Cancel
            String errorMessage = lEx.getMessage();
            Throwable exCause = lEx.getCause();
            if (exCause!=null && exCause instanceof KrbApErrException){
                KrbApErrException krbException = (KrbApErrException)lEx.getCause();
                if(31==krbException.returnCode()){
                   errorMessage = "coeusApplet_exceptionCode.100005" ;
                }
            }
            lEx.printStackTrace();
            String parsedError = CoeusMessageResources.getInstance().parseMessageKey(errorMessage);
//            CoeusOptionPane.showErrorDialog(coeusKerbHandler,parsedError);
            CoeusOptionPane.showErrorDialog(parsedError);
            if(loginTryCount>=3){//try three times
                throw new LoginException("coeusApplet_exceptionCode.100006");
            }else{
                userId = invokeLogin();
            }
        }catch(Exception lEx){
            lEx.printStackTrace();
            throw new LoginException(lEx.getMessage());
        }
        System.out.println("all done, and returning user id");
        return userId;
    }
    
}
