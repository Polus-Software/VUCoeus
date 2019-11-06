/*
 * NarrativeTypeBean.java
 *
 * Created on July 12, 2006, 10:36 AM
 */

package edu.utk.coeuslite.propdev.bean;

import edu.mit.coeus.propdev.bean.ProposalNarrativeTypeBean;

/**
 *
 * @author  noorula
 */
public class NarrativeTypeBean extends ProposalNarrativeTypeBean{
    
    private String narrativeTypeGroup ;
    
    /** Creates a new instance of NarrativeTypeBean */
    public NarrativeTypeBean() {
    }
    
    /**
     * Getter for property narrativeTypeGroup.
     * @return Value of property narrativeTypeGroup.
     */
    public java.lang.String getNarrativeTypeGroup() {
        return narrativeTypeGroup;
    }
    
    /**
     * Setter for property narrativeTypeGroup.
     * @param narrativeTypeGroup New value of property narrativeTypeGroup.
     */
    public void setNarrativeTypeGroup(java.lang.String narrativeTypeGroup) {
        this.narrativeTypeGroup = narrativeTypeGroup;
    }
    
}
