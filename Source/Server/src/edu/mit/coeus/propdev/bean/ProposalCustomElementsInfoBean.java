/*
 * @(#)ProposalCustomElementsInfoBean.java 1.0 03/13/03 10:45 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.customelements.bean.*;

import java.beans.*;
import java.sql.Timestamp;

/**
 * The class used to hold the information of <code>Proposal Others</code>
 * which extends the PersonCustomElementsInfoBean.
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 13, 2003, 10:45 AM
 */

public class ProposalCustomElementsInfoBean extends PersonCustomElementsInfoBean
                                        implements java.io.Serializable {
    //holds the proposal number
    private String proposalNumber;
    
    public ProposalCustomElementsInfoBean(){
        super();
    }
    
    public ProposalCustomElementsInfoBean(CustomElementsInfoBean customElementsInfoBean){
        super(customElementsInfoBean);
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
}
