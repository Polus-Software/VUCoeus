/*
 * MyLogonForm.java
 *
 * Created on March 3, 2005, 10:37 AM
 */

package edu.mit.coeuslite.irb.form;

/**
 *
 * @author  chandrashekara
 */
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;


/**
 * Struts "Form bean" for validating and passing parameters needed to change
 * Coeus password.
 * This class is associated with a particular action through an
 * action mapping in <code>struts-config.xml</code>.
 * 
 */

public final class MyLogonForm extends ActionForm
{
    /**
     * Entered username and password value.
     */
    private String username;
    private String password;
    private String loggedOn;
    private String campusCode;
    
    public MyLogonForm(){
    }
    
    public String getLoggedOn(){   
        return loggedOn;
    }
    public void setLoggedOn(String logon){
        this.loggedOn = logon;
    }
/**
     * Get confirm password.
     * @return
     */
    public String getUsername(){ 
        return username;
    }

    /**
     * Set the password.
     * @param password The new password
     */
    public void setUsername(String username)
    {   this.username = username;
    }
    /**
     * Get confirm password.
     * @return
     */
    public String getPassword()
    {   return password;
    }

    /**
     * Set the password.
     * @param password The new password
     */
    public void setPassword(String password)
    {   this.password = password;
    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {   this.password = null;
        this.username = null;
    }


    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request){   
        System.out.println("*** In Validate ");
        ActionErrors errors = new ActionErrors();
        
        if (password == null || password.length() < 1){   
            System.out.println("*** Password required Added Error");
            errors.add("password",new ActionError("error.password.required"));
        }
        if (username == null || username.length() <1 ){   
            System.out.println("*** Username required Added error");
            errors.add("username",new ActionError("error.username.required"));
        }
        
        return errors;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }
}

