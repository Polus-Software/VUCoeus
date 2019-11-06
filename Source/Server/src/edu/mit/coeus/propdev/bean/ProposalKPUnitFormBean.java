/* 
 * @(#)ProposalKPUnitFormBean.java 1.0 03/14/03 11:41 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 * The class used to hold the information of <code>Proposal Key Person Unit</code>
 *
 * @author  
 * @version 1.0
 * Created on January 24, 2011, 11:41 AM
 */

public class ProposalKPUnitFormBean extends InvestigatorUnitBean implements IBaseDataBean{
    // holds the proposal number
    private String proposalNumber;
    //hoilds sequence number
    private int sequenceNumber;
    /** Creates new ProtocolInvestigatorUnitsBean */
    public ProposalKPUnitFormBean() {
        
    }
     public ProposalKPUnitFormBean (InvestigatorUnitBean investigatorUnitBean) {
        super(investigatorUnitBean);
    }

  public boolean equals(Object obj) {
        ProposalKPUnitFormBean awardUnitBean = (ProposalKPUnitFormBean)obj;
        //Donot check equality for sequence number - 8th April, 2004
        if(awardUnitBean.getProposalNumber().equals(getProposalNumber()) &&
            // Need to check for equlaity - Case 2037
            awardUnitBean.getSequenceNumber() == getSequenceNumber() &&
            awardUnitBean.getUnitNumber().equals(getUnitNumber()) &&
            awardUnitBean.getPersonId().equals(getPersonId())){
                return true;
       }else{
            return false;
        }
    }

    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }

    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

}