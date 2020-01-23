/*
 * ProposalHierarchyBean.java
 *
 * Created on August 11, 2005, 12:56 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;

/**
 *
 * @author  chandrashekara
 */
public class ProposalHierarchyBean implements BaseBean,java.io.Serializable {
    private String parentProposalNumber;
    private CoeusVector proposalData;
    
    /** Creates a new instance of ProposalHierarchyBean */
    public ProposalHierarchyBean() {
    }
    
    /**
     * Getter for property parentProposalNumber.
     * @return Value of property parentProposalNumber.
     */
    public java.lang.String getParentProposalNumber() {
        return parentProposalNumber;
    }
    
    /**
     * Setter for property parentProposalNumber.
     * @param parentProposalNumber New value of property parentProposalNumber.
     */
    public void setParentProposalNumber(java.lang.String parentProposalNumber) {
        this.parentProposalNumber = parentProposalNumber;
    }
    
    /**
     * Getter for property proposalData.
     * @return Value of property proposalData.
     */
    public edu.mit.coeus.utils.CoeusVector getProposalData() {
        return proposalData;
    }    
    
    /**
     * Setter for property proposalData.
     * @param proposalData New value of property proposalData.
     */
    public void setProposalData(edu.mit.coeus.utils.CoeusVector proposalData) {
        this.proposalData = proposalData;
    }
}
