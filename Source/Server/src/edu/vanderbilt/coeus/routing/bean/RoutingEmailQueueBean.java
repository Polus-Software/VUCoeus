/*
 * RoutingEmailQueueBean.java
 *
 */

package edu.vanderbilt.coeus.routing.bean;

import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import java.sql.Timestamp;

public class RoutingEmailQueueBean extends RoutingDetailsBean {
    
    private String notificationStatus;
    private int emailActionId;
    private String emailStatus;
    private Timestamp emailSentTimestamp;
    
    /**
     * Creates a new instance of RoutingEmailQueueBean
     */
    public RoutingEmailQueueBean() {
    }
    
    public String getNotificationStatus() {
    	return notificationStatus;
    }
    
    public void setNotificationStatus(String notificationStatus) {
    	this.notificationStatus = notificationStatus;
    }
    
    public int getEmailActionId() {
    	return emailActionId;
    }
    
    public void setEmailActionId(int emailActionId) {
    	this.emailActionId = emailActionId;
    }
    
    public String getEmailStatus() {
    	return emailStatus;
    }
    
    public void setEmailStatus(String emailStatus) {
    	this.emailStatus = emailStatus;
    }
    
    public Timestamp getEmailSentTimestamp() {
    	return emailSentTimestamp;
    }
    
    public void setEmailSentTimestamp(Timestamp emailSentTimestamp) {
    	this.emailSentTimestamp = emailSentTimestamp;
    }
    
}
