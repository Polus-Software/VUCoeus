/*
 * @(#) OrganizationSearchForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeuslite.irb.form;


import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;



/**
 * This class is associated with a particular action through an
 * action mapping in <code>struts-config.xml</code>.
 * @author nadhgj
 * @Created on March 3, 2005, 11:20 AM
 */

public final class OrganizationSearchForm extends ActionForm
{
    /**
     * Entered username and password value.
     */
    private String orgName;
    private String orgId;
        
    public OrganizationSearchForm() {
    }
    
    /**
     * Getter for property orgName.
     * @return Value of property orgName.
     */
    public java.lang.String getOrgName() {
        return orgName;
    }    
    
    /**
     * Setter for property orgName.
     * @param orgName New value of property orgName.
     */
    public void setOrgName(java.lang.String orgName) {
        this.orgName = orgName;
    }
    
    /**
     * Getter for property orgId.
     * @return Value of property orgId.
     */
    public java.lang.String getOrgId() {
        return orgId;
    }
    
    /**
     * Setter for property orgId.
     * @param orgId New value of property orgId.
     */
    public void setOrgId(java.lang.String orgId) {
        this.orgId = orgId;
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {   this.orgName = null;
        this.orgId = null;
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
                                 HttpServletRequest request)
    {   
        ActionErrors errors = new ActionErrors();
        return errors;
    }
}
