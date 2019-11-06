/*
 * RoutingQueueMessageBean.java
 *
 */

package edu.vanderbilt.coeus.routing.bean;

import edu.mit.coeus.utils.CoeusVector;

public class RoutingQueueMessageBean {
    
    private String action;
    private int emailActionCode;
    private CoeusVector approvers;
    
    /**
     * Creates a new instance of RoutingQueueMessageBean
     */
    public RoutingQueueMessageBean() {
    }
    
    public String getAction() {
    	return action;
    }
    
    public void setAction(String action) {
    	this.action = action;
    }
    
    public int getEmailActionCode() {
    	return emailActionCode;
    }
    
    public void setEmailActionCode(int emailActionCode) {
    	this.emailActionCode = emailActionCode;
    }
    
    public CoeusVector getApprovers() {
    	return approvers;
    }
    
    public void setApprovers(CoeusVector approvers) {
    	this.approvers = approvers;
    }

    public void addApproverToBean(String action,RoutingEmailQueueBean queueBean) {
    	 
    }
    
}
