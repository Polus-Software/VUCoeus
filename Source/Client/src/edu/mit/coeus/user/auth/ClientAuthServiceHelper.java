/*
 * ClientAuthServiceHelper.java
 *
 * Created on August 28, 2006, 4:13 PM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth;

import edu.mit.coeus.bean.LoginBean;
import edu.mit.coeus.bean.UserInfoBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author  Geo Thomas
 */
public class ClientAuthServiceHelper implements CoeusAuthService{
    private Vector responseVector;
    private Hashtable data;
    private Properties props;
    private CoeusMessageResources msgRes;
    private AuthResponseListener lsnr;
    private static final String AUTH_SERVLET_URL = CoeusGuiConstants.CONNECTION_URL+
                                                        "/AuthenticationServlet";
    /** Creates a new instance of ClientAuthServiceHelper */
    public ClientAuthServiceHelper() {
        msgRes = CoeusMessageResources.getInstance();
    }
    
    public void addParam(Object key, Object value) {
        if(data==null) data = new Hashtable();
        data.put(key, value);
    }
    
    public boolean authenticate() throws edu.mit.coeus.exception.CoeusException {
        String userId = (String)data.get("USER_ID");
        String password = (String)data.get("PASSWORD");
        if ( userId!=null && userId.trim().equals("") ){
            throw new CoeusException(msgRes.parseMessageKey("coeusApplet_exceptionCode.1163"));
        }else if (password!=null &&  password.trim().equals("") ){
            throw new CoeusException(msgRes.parseMessageKey("coeusApplet_exceptionCode.1164"));
        }
        String loginMode = (String)data.get("LOGIN_MODE");
        LoginBean loginBean = new LoginBean(userId,password);
        RequesterBean request = new RequesterBean();
        request.setId(loginMode);
        request.setDataObject(loginBean);
        return connectAndValidate(request);
    }
    
    public void init(java.util.Properties props) {
        this.props = props;
    }
    public Properties getProps(){
        return props;
    }
    public void setParams(java.util.Hashtable data) {
        this.data = data;
    }
    public Hashtable getParams(){
        return data;
    }
    public boolean connectAndValidate(RequesterBean request) throws CoeusException{
        System.out.println("CONNECTION_URL IS " + CoeusGuiConstants.CONNECTION_URL) ;
        AppletServletCommunicator comm = new AppletServletCommunicator(AUTH_SERVLET_URL, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        String msg = "";
        if (response == null) {
            response = new ResponderBean();
            response.setResponseStatus(false);
            msg = msgRes.parseMessageKey("coeusApplet_exceptionCode.1147");
            response.setMessage(msg);
            throw new CoeusException(msg);
        }else if(response != null){
            if(!response.isSuccessfulResponse()){
                if(response.getException() != null){
                    response.getException().printStackTrace();
                    msg = response.getException().getMessage();
                }else{
                    msg = response.getMessage();
                }
                throw new CoeusException(msg);
            }
        }
        if(lsnr!=null){
            lsnr.respond(response);
        }
        return response.isSuccessfulResponse();
    }
    
    public final void addResponseListener(AuthResponseListener authLis) {
        lsnr = authLis;
    }
}
