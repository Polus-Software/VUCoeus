/*
 * AuthBean.java
 *
 * Created on August 28, 2006, 10:21 AM
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.user.auth.bean;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author  Geo Thomas
 */
public class AuthXMLNodeBean implements Serializable{
    private String AuthMode;
    private String clientClass;
    private String serverClass;
    private Properties authProps;
    private boolean loginScreen;
    private Object otherNodes;//just in case
    /** Creates a new instance of AuthBean */
    public AuthXMLNodeBean() {
    }
    
    /**
     * Getter for property AuthMode.
     * @return Value of property AuthMode.
     */
    public java.lang.String getAuthMode() {
        return AuthMode;
    }
    
    /**
     * Setter for property AuthMode.
     * @param AuthMode New value of property AuthMode.
     */
    public void setAuthMode(java.lang.String AuthMode) {
        this.AuthMode = AuthMode;
    }
    
    /**
     * Getter for property clientClass.
     * @return Value of property clientClass.
     */
    public java.lang.String getClientClass() {
        return clientClass;
    }
    
    /**
     * Setter for property clientClass.
     * @param clientClass New value of property clientClass.
     */
    public void setClientClass(java.lang.String clientClass) {
        this.clientClass = clientClass;
    }
    
    /**
     * Getter for property serverClass.
     * @return Value of property serverClass.
     */
    public java.lang.String getServerClass() {
        return serverClass;
    }
    
    /**
     * Setter for property serverClass.
     * @param serverClass New value of property serverClass.
     */
    public void setServerClass(java.lang.String serverClass) {
        this.serverClass = serverClass;
    }
    
    /**
     * Getter for property authProps.
     * @return Value of property authProps.
     */
    public java.util.Properties getAuthProps() {
        return authProps;
    }
    
    /**
     * Setter for property authProps.
     * @param authProps New value of property authProps.
     */
    public void setAuthProps(java.util.Properties authProps) {
        this.authProps = authProps;
    }
    
    /**
     * Getter for property loginScreen.
     * @return Value of property loginScreen.
     */
    public boolean hasLoginScreen() {
        return loginScreen;
    }
    
    /**
     * Setter for property loginScreen.
     * @param loginScreen New value of property loginScreen.
     */
    public void setLoginScreen(String loginScreen) {
        this.loginScreen = Boolean.valueOf(loginScreen).booleanValue();
    }

    public Object getOtherNodes() {
        return otherNodes;
    }

    public void setOtherNodes(Object otherNodes) {
        this.otherNodes = otherNodes;
    }
    
}
