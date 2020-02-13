/*
 * @(#) ChangePasswordForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeuslite.common.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * Struts "Form bean" for validating and passing parameters needed to change
 * Coeus password.
 * This class is associated with a particular action through an
 * action mapping in <code>struts-config.xml</code>.
 * @author Coeus Dev Team
 * @version $Revision:   1.1  $ $Date:   Aug 12 2002 15:13:56  $
 */

public final class ChangePasswordForm extends ActionForm
{
    /**
     * Entered confirm password value.
     */
    private String confirmPassword;

    /**
     * Entered new password value.
     */
    private String newPassword;

    public ChangePasswordForm()
    {   }

    /**
     * Get confirm password.
     * @return
     */
    public String getConfirmPassword()
    {   return confirmPassword;
    }

    /**
     * Get new password.
     * @return
     */
    public String getNewPassword()
    {   return newPassword;
    }

    /**
     * Set the username.
     * @param username The new username
     */
    public void setConfirmPassword(String confirmPassword)
    {   this.confirmPassword = confirmPassword;
    }

    /**
     * Set the password.
     * @param password The new password
     */
    public void setNewPassword(String newPassword)
    {   this.newPassword = newPassword;
    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {   this.newPassword = null;
        this.confirmPassword = null;
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
        ActionErrors errors = new ActionErrors();
        if (newPassword == null || newPassword.length() < 1)
        {   errors.add("newPassword",
            new ActionError("error.password.required"));
        }
        if (confirmPassword == null || confirmPassword.length() <1 )
        {   errors.add("confirmPassword",
            new ActionError("error.password2.required"));
        }
        /* CASE #736 Comment Begin */
        /*if (!(confirmPassword.equals(newPassword)))
        {   errors.add("passwordMatch",
                new ActionError("error.password.match"));
        }*/
        /* CASE #736 Comment End */
        /* CASE #736 Begin */
        if (confirmPassword != null && !confirmPassword.equals(newPassword))
        {   errors.add("confirmPassword",
                new ActionError("error.password.match"));
        }
        /* CASE #736 End */
        /* CASE #1408 Begin */
        if(newPassword != null && !(newPassword.length() <1) ){
            char firstChar = newPassword.charAt(0);
            System.out.println("firstChar: "+firstChar);
            boolean beginsWithLetter = Character.isLetter(firstChar);
            if(!beginsWithLetter){
                errors.add("newPassword", 
                    new ActionError("changePassword.error.beginWithLetter"));
            }
            for(int charCnt=0; charCnt<newPassword.length(); charCnt++){
                char character = newPassword.charAt(charCnt);
                if(!Character.isLetter(character) && !Character.isDigit(character)
                    && !String.valueOf(character).equals("$") && !String.valueOf(character).equals("_")
                    && !String.valueOf(character).equals("#") ){
                        System.out.println("String.valueOf(character): "+String.valueOf(character) );
                        errors.add("newPassword",
                            new ActionError("changePassword.error.invalidCharacter") );
                }
            }
            if(newPassword.length() > 30){
                errors.add("newPassword", 
                        new ActionError("changePassword.error.tooLong") );
            }
            if(newPassword.equalsIgnoreCase("TABLE") || newPassword.equalsIgnoreCase("DELETE") 
                || newPassword.equalsIgnoreCase("UPDATE") || newPassword.equalsIgnoreCase("SELECT")
                || newPassword.equalsIgnoreCase("INSERT") || newPassword.equalsIgnoreCase("ORDER") ){
                errors.add("newPassword", 
                        new ActionError("changePassword.error.reserved"));
            }
        }

        /* CASE #1408 End */
        return errors;
    }
}
