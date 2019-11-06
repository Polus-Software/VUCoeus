/*
 * UniqueSchemaNotFoundException.java
 *
 * Created on December 27, 2004, 12:10 PM
 */

package edu.mit.coeus.s2s.validator;

import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetOpportunityListResponse;
import java.util.ArrayList;

/**
 *
 * @author  geot
 */
public class UniqueSchemaNotFoundException extends S2SValidationException {
    private ArrayList opportunityList;
    /**
     * Creates a new instance of <code>UniqueSchemaNotFoundException</code> without detail message.
     */
    public UniqueSchemaNotFoundException() {
    }
    
    
    /**
     * Constructs an instance of <code>UniqueSchemaNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UniqueSchemaNotFoundException(String msg) {
        super(msg);
    }
    
    /**
     * Setter for property opportunityList.
     * @param opportunityList New value of property opportunityList.
     */
    public void setOpportunityList(ArrayList opportunityList) {
        this.opportunityList = opportunityList;
    }
    
    /**
     * Getter for property opportunityList.
     * @return Value of property opportunityList.
     */
    public java.util.ArrayList getOpportunityList() {
        return opportunityList;
    }
    
}
