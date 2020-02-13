/*
 * @(#)ProposalColumnsToAlterBean.java 1.0 01/09/04 11:23 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.customelements.bean.*;


import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Proposal Column to Alter</code>
 * used in Data Overides module
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 09, 2004, 11:23 AM
 */

public class ProposalColumnsToAlterBean extends CustomElementsInfoBean implements java.io.Serializable, BaseBean {
    //holds the proposal number
    private CoeusVector proposalDataOverides;    

    public ProposalColumnsToAlterBean(){
    }

    /** Getter for property proposalDataOverides.
     * @return Value of property proposalDataOverides.
     */
    public CoeusVector getProposalDataOverides() {
        return proposalDataOverides;
    }
    
    /** Setter for property proposalDataOverides.
     * @param proposalDataOverides New value of property proposalDataOverides.
     */
    public void setProposalDataOverides(CoeusVector proposalDataOverides) {
        this.proposalDataOverides = proposalDataOverides;
    }
    
}
