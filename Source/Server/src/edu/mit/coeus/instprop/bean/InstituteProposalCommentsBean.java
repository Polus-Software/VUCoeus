/*
 * @(#)InstituteProposalIDCRateBean.java 1.0 April 27, 2004, 6:55 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.instprop.bean;

import java.util.*;
import java.sql.Date;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/** This class is used to hold Comments of a Institute Proposal
 *
 * @version :1.0 April 27, 2004, 6:55 AM
 * @author Prasanna Kumar K
 */

public class InstituteProposalCommentsBean extends InstituteProposalBaseBean{
    
    private int commentCode;
    private String comments;    
    
    public InstituteProposalCommentsBean(){
    }
    
    /** Getter for property commentCode.
     * @return Value of property commentCode.
     */
    public int getCommentCode() {
        return commentCode;
    }
 
    /** Setter for property commentCode.
     * @param commentCode New value of property commentCode.
     */
    public void setCommentCode(int commentCode) {
        this.commentCode = commentCode;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            InstituteProposalCommentsBean instituteProposalCommentsBean = (InstituteProposalCommentsBean)obj;
            if(instituteProposalCommentsBean.getCommentCode() == getCommentCode()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
} // end