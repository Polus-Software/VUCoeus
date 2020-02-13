/* 
 * @(#)ProposalYNQExplantionFormBean.java 1.0 03/27/03 10:50 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;

/**
 * The class used to hold the information of <code>Proposal YesNoQuestionExplantion</code>
 * which extends ProposalYNQFormBean
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 27, 2003, 10:50 AM
 */
public class ProposalYNQExplantionFormBean extends ProposalYNQFormBean 
                                            implements java.io.Serializable {
    
    // holds the explantion type
    private char explantionType;
    // holds the explantion
    private String explantion;
    
    
    public char getExplantionType() {
        return explantionType;
    }

    
    public void setExplantionType(char explantionType) {
        this.explantionType = explantionType;
    }
    
    public String getExplantion() {
        return explantion;
    }

    
    public void setExplantion(String explantion) {
        this.explantion = explantion;
    }
    
}
