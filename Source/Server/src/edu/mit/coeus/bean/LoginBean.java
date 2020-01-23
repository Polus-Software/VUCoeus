/*
 * @(#)LoginBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on December 4, 2002, 12:10 PM
 * @version 1.0
 */

package edu.mit.coeus.bean;

import java.io.Serializable;

/**
 *
 * This serialized bean will hold the logged in user information like 
 * name and password. This bean is used at server side to validate the user 
 * information.
 *
 * @version :1.0 December 4, 2002, 12:10 PM
 * @author Guptha K
 *
 */
public class LoginBean implements Serializable{
    //holds user id
    private String userId;
    //holds password id
    private String password;

    /**
     * creates login bean
     */
    public LoginBean() {
    }

    /**
     * creates login bean
     *
     * @param userId the logged in user name
     * @param password the logger in user's password
     */
    public LoginBean(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
    
    /** Get login User Id
     * @return String user Id
     */
    public String getUserId() {
        return userId;
    }
    /**  Set User login Id
     * @param userId  set the logged in the user id.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**  Get logged in user Password
     * @return String Password
     */
    public String getPassword() {
        return password;
    }
    
    /**  Set the logged in user Password
     * @param password String represent the logged in user password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
