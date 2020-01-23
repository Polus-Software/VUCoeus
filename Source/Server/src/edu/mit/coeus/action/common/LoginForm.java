/*
 * LoginForm.java
 *
 * Created on March 8, 2006, 12:05 PM
 */

package edu.mit.coeus.action.common;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author  gpanikul
 */
public class LoginForm  extends ActionForm{
    private String userId;
    private String password;
    /** Creates a new instance of LoginForm */
    public LoginForm() {
    }
    
    /**
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /**
     * Getter for property password.
     * @return Value of property password.
     */
    public java.lang.String getPassword() {
        return password;
    }
    
    /**
     * Setter for property password.
     * @param password New value of property password.
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }
    
}
