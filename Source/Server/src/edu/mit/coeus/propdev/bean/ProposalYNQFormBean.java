/* 
 * @(#)ProposalYNQFormBean.java 1.0 03/26/03 3:26 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.beans.*;
import edu.mit.coeus.utils.question.bean.YNQBean;

/**
 * The class used to hold the information of <code>Proposal yesNoQuestione</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 12, 2003, 4:40 PM
 */

public class ProposalYNQFormBean extends YNQBean implements java.io.Serializable {
    
    private static final String PROP_ANSWER_PROPERTY = "answer";
    // holds the proposal number
    private String proposalNumber;
    // holds the person id
    private String personId;
    // holds the question id
    //private String questionId;
    // holds the answer
    //private boolean answer;
    //holds update user id
    //private String updateUser;
    //holds update timestamp
    //private java.sql.Timestamp updateTimestamp;
    //holds Action type
    //private String acType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProposalYNQFormBean */
    public ProposalYNQFormBean() {
        //propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    /**
     * Method used to set the proposal number
     * @param proposalNumber String
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * Method used to get the person id
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method used to set the person id
     * @param personId String
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
