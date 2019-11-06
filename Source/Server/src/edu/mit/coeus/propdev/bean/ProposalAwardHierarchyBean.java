/**
 * @(#)AwardInstPropDevPropBean.java 1.0 12/29/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class is used to set and get the Award, Institute Proposal and Development Proposal Numbers
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on December 20, 2003 12:30 PM
 */

public class ProposalAwardHierarchyBean implements java.io.Serializable, BaseBean{
    
    private String developmentProposalNumber = null;
    private String instituteProposalNumber = null;
    private int instituteSequenceNumber = -1;
    private String negotiationNumber = null;   
    private String awardNumber = null;
    private String subcontractNumber = null;
    private boolean hasNegotiationNumber = false;
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private String irbProtocolNumber = null;
    private String iacucProtocolNumber = null;
    //COEUSQA:2653 - End
    
    /**
     *	Default Constructor
     */
    
    public ProposalAwardHierarchyBean(){
    }
    
    public ProposalAwardHierarchyBean( ProposalAwardHierarchyBean copyBean ) {
        this.instituteProposalNumber = copyBean.getInstituteProposalNumber();
        this.instituteSequenceNumber = copyBean.getInstituteSequenceNumber();
        this.awardNumber = copyBean.getAwardNumber();
        this.developmentProposalNumber = copyBean.getDevelopmentProposalNumber();
        this.negotiationNumber = copyBean.getNegotiationNumber();
        this.subcontractNumber = copyBean.getSubcontractNumber();
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        this.irbProtocolNumber = copyBean.getIrbProtocolNumber();
        this.iacucProtocolNumber = copyBean.getIacucProtocolNumber();
        //COEUSQA:2653 - End
    }
    /** Getter for property developmentProposalNumber.
     * @return Value of property developmentProposalNumber.
     */
    public java.lang.String getDevelopmentProposalNumber() {
        return developmentProposalNumber;
    }    

    /** Setter for property developmentProposalNumber.
     * @param developmentProposalNumber New value of property developmentProposalNumber.
     */
    public void setDevelopmentProposalNumber(java.lang.String developmentProposalNumber) {
        this.developmentProposalNumber = developmentProposalNumber;
    }
    
    /** Getter for property instituteProposalNumber.
     * @return Value of property instituteProposalNumber.
     */
    public java.lang.String getInstituteProposalNumber() {
        return instituteProposalNumber;
    }
    
    /** Setter for property instituteProposalNumber.
     * @param instituteProposalNumber New value of property instituteProposalNumber.
     */
    public void setInstituteProposalNumber(java.lang.String instituteProposalNumber) {
        this.instituteProposalNumber = instituteProposalNumber;
    }
    
    /** Getter for property instituteSequenceNumber.
     * @return Value of property instituteSequenceNumber.
     */
    public int getInstituteSequenceNumber() {
        return instituteSequenceNumber;
    }
    
    /** Setter for property instituteSequenceNumber.
     * @param instituteSequenceNumber New value of property instituteSequenceNumber.
     */
    public void setInstituteSequenceNumber(int instituteSequenceNumber) {
        this.instituteSequenceNumber = instituteSequenceNumber;
    }
    
    /** Getter for property negotiationNumber.
     * @return Value of property negotiationNumber.
     */
    public java.lang.String getNegotiationNumber() {
        return negotiationNumber;
    }
    
    /** Setter for property negotiationNumber.
     * @param negotiationNumber New value of property negotiationNumber.
     */
    public void setNegotiationNumber(java.lang.String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }
    
    /** Getter for property awardNumber.
     * @return Value of property awardNumber.
     */
    public java.lang.String getAwardNumber() {
        return awardNumber;
    }
    
    /** Setter for property awardNumber.
     * @param awardNumber New value of property awardNumber.
     */
    public void setAwardNumber(java.lang.String awardNumber) {
        this.awardNumber = awardNumber;
    }    
    
    /** Getter for property subcontractNumber.
     * @return Value of property subcontractNumber.
     */
    public java.lang.String getSubcontractNumber() {
        return subcontractNumber;
    }
    
    /** Setter for property subcontractNumber.
     * @param subcontractNumber New value of property subcontractNumber.
     */
    public void setSubcontractNumber(java.lang.String subcontractNumber) {
        this.subcontractNumber = subcontractNumber;
    }
    
     /** Getter for property irbProtocolNumber.
     * @return Value of property irbProtocolNumber.
     */
    public java.lang.String getIrbProtocolNumber() {
        return irbProtocolNumber;
    }    

    /** Setter for property irbProtocolNumber.
     * @param irbProtocolNumber New value of property irbProtocolNumber.
     */
    public void setIrbProtocolNumber(java.lang.String irbProtocolNumber) {
        this.irbProtocolNumber = irbProtocolNumber;
    }
    
     /** Getter for property iacucProtocolNumber.
     * @return Value of property iacucProtocolNumber.
     */
    public java.lang.String getIacucProtocolNumber() {
        return iacucProtocolNumber;
    }    

    /** Setter for property iacucProtocolNumber.
     * @param iacucProtocolNumber New value of property iacucProtocolNumber.
     */
    public void setIacucProtocolNumber(java.lang.String iacucProtocolNumber) {
        this.iacucProtocolNumber = iacucProtocolNumber;
    }
    /*
    public boolean equals(Object obj){
        ProposalAwardHierarchyBean proposalAwardHierarchyBean = (ProposalAwardHierarchyBean)obj;
        if((proposalAwardHierarchyBean.getInstituteProposalNumber() != null && 
                getInstituteProposalNumber()!=null && proposalAwardHierarchyBean.getInstituteProposalNumber() == getInstituteProposalNumber()) &&
            (proposalAwardHierarchyBean.getDevelopmentProposalNumber() != null && 
                getDevelopmentProposalNumber()!=null && proposalAwardHierarchyBean.getDevelopmentProposalNumber() == getDevelopmentProposalNumber()) &&
            (proposalAwardHierarchyBean.getAwardNumber() != null && 
                getAwardNumber()!=null && proposalAwardHierarchyBean.getAwardNumber() == getAwardNumber()) &&
            (proposalAwardHierarchyBean.getNegotiationNumber() != null && 
                getNegotiationNumber()!=null && proposalAwardHierarchyBean.getNegotiationNumber() == getNegotiationNumber()) &&
            (proposalAwardHierarchyBean.getSubcontractNumber() != null && 
                getSubcontractNumber()!=null && proposalAwardHierarchyBean.getSubcontractNumber() == getSubcontractNumber())){
                return true;
        }else{
            return false;
        }
    }*/
    
    public boolean equals(Object obj){
        ProposalAwardHierarchyBean proposalAwardHierarchyBean = (ProposalAwardHierarchyBean)obj;
        boolean isLike = false;
        if(proposalAwardHierarchyBean.getInstituteProposalNumber() != null && getInstituteProposalNumber()!=null){
            if(proposalAwardHierarchyBean.getInstituteProposalNumber().equals(getInstituteProposalNumber())){
                isLike = true;
            }else{
                return false;
            }
        }

        if(proposalAwardHierarchyBean.getDevelopmentProposalNumber() != null && getDevelopmentProposalNumber()!=null){
            if(proposalAwardHierarchyBean.getDevelopmentProposalNumber().equals(getDevelopmentProposalNumber())){
                isLike = true;
            }else{
                return false;
            }
        }

        if(proposalAwardHierarchyBean.getAwardNumber() != null && getAwardNumber()!=null){
            if(proposalAwardHierarchyBean.getAwardNumber().equals(getAwardNumber())){
                isLike = true;
            }else{
                return false;
            }
        }        
        
        if(proposalAwardHierarchyBean.getNegotiationNumber() != null && getNegotiationNumber()!=null){
            if(proposalAwardHierarchyBean.getNegotiationNumber().equals(getNegotiationNumber())){
                isLike = true;
            }else{
                return false;
            }
        }                
        
        if(proposalAwardHierarchyBean.getSubcontractNumber() != null && getSubcontractNumber()!=null){
            if(proposalAwardHierarchyBean.getSubcontractNumber().equals(getSubcontractNumber())){
                isLike = true;
            }else{
                return false;
            }
        }   
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        if(proposalAwardHierarchyBean.getIrbProtocolNumber() != null && getIrbProtocolNumber()!=null){
            if(proposalAwardHierarchyBean.getIrbProtocolNumber().equals(getIrbProtocolNumber())){
                isLike = true;
            }else{
                return false;
            }
        }
        
        if(proposalAwardHierarchyBean.getIacucProtocolNumber() != null && getIacucProtocolNumber()!=null){
            if(proposalAwardHierarchyBean.getIacucProtocolNumber().equals(getIacucProtocolNumber())){
                isLike = true;
            }else{
                return false;
            }
        }
        //COEUSQA:2653 - End
        
        return isLike;
    }
    
    /** Getter for property hasNegotiationNumber.
     * @return Value of property hasNegotiationNumber.
     */
    public boolean isHasNegotiationNumber() {
        return hasNegotiationNumber;
    }
    
    /** Setter for property hasNegotiationNumber.
     * @param hasNegotiationNumber New value of property hasNegotiationNumber.
     */
    public void setHasNegotiationNumber(boolean hasNegotiationNumber) {
        this.hasNegotiationNumber = hasNegotiationNumber;
    }
    
}