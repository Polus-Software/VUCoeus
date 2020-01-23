/*
 * @(#)ApprovalRightsBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean.web;

import edu.mit.coeus.utils.UtilFactory;

/**
 *
 */
public class ApprovalRightsBean {


    int viewRoutingRight;
    int approveRejectRight;
    String proposalNumber;

    /** Creates new ApprovalRightsBean
     */
    public ApprovalRightsBean() {
    }

    public String getProposalNumber(){
        return this.proposalNumber;
    }

    public void setProposalNumber(String proposalnumber ){
        this.proposalNumber = proposalnumber;
    }

    public int getViewRoutingRight(){
        System.out.println("in bean detail get view routing right * " + this.viewRoutingRight);
        return this.viewRoutingRight;
    }

    public void setViewRoutingRight(int ViewRoutingRight){
        System.out.println("in bean detail set view routing right * " + ViewRoutingRight);
        this.viewRoutingRight = ViewRoutingRight;
    }

    public int getApproveRejectRight(){
        return this.approveRejectRight;
    }

    public void setApproveRejectRight(int ApproveRejectRight){
        this.approveRejectRight = ApproveRejectRight;
    }

}

