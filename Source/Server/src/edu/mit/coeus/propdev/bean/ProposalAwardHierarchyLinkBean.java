/*
 * ProposalAwardHierarchyLinkBean.java
 *
 * Created on January 12, 2004, 7:41 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.utils.CoeusConstants;

/**
 *
 * @author  chandrashekara
 */
public class ProposalAwardHierarchyLinkBean extends ProposalAwardHierarchyBean 
    {
    
    private String baseType;
    /** Creates a new instance of ProposalAwardHierarchyLinkBean */
    public ProposalAwardHierarchyLinkBean() {
    }
    
    public ProposalAwardHierarchyLinkBean( ProposalAwardHierarchyBean awardHierarchyBean ) {
        super(awardHierarchyBean);
    }
    
    /** Getter for property baseType.
     * @return Value of property baseType.
     *
     */
    public java.lang.String getBaseType() {
        return baseType;
    }
    
    /** Setter for property baseType.
     * @param baseType New value of property baseType.
     *
     */
    public void setBaseType(java.lang.String baseType) {
        this.baseType = baseType;
    }
    
    public String toString(){
        if( CoeusConstants.INST_PROP.equalsIgnoreCase(getBaseType())){
            return ( getInstituteProposalNumber() == null ? "" : getInstituteProposalNumber() );
        }else if( CoeusConstants.AWARD.equalsIgnoreCase(getBaseType())){
            return ( getAwardNumber() == null ? "" : getAwardNumber() );
        }else if( CoeusConstants.DEV_PROP.equalsIgnoreCase(getBaseType())){
            return ( getDevelopmentProposalNumber() == null ? "" : getDevelopmentProposalNumber() );
        }else if( CoeusConstants.SUBCONTRACT.equalsIgnoreCase(getBaseType())){
            return ( getSubcontractNumber() == null ? "" : getSubcontractNumber() );
        }
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        else if( CoeusConstants.IRB_PROTOCOL.equalsIgnoreCase(getBaseType())){
            return ( getIrbProtocolNumber() == null ? "" : getIrbProtocolNumber() );
        } else if( CoeusConstants.IACUC_PROTOCOL.equalsIgnoreCase(getBaseType())){
            return ( getIacucProtocolNumber() == null ? "" : getIacucProtocolNumber() );
        }
        //COEUSQA:2653 - End
        return "";
    }
    
}
