/*
 * ProposalPrintForm.java
 *
 * Created on September 2, 2004, 4:00 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.sponsormaint.bean.SponsorTemplateBean;

/**
 *
 * @author  geot
 */
public class ProposalPrintFormBean extends SponsorTemplateBean{
    
    private String packageName;
    
    /** Creates a new instance of ProposalPrintForm */
    public ProposalPrintFormBean() {
    }
    
    /**
     * Getter for property packageName.
     * @return Value of property packageName.
     */
    public java.lang.String getPackageName() {
        return packageName;
    }    
   
    /**
     * Setter for property packageName.
     * @param packageName New value of property packageName.
     */
    public void setPackageName(java.lang.String packageName) {
        this.packageName = packageName;
    }
    
}
