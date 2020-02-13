/*
 * ProtocolRoleInfoBean.java
 *
 * Created on August 2, 2009, 2:16 PM
 *
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.RoleInfoBean;

/**
 *
 * @author sreenath
 */
public class ProtocolRoleInfoBean extends RoleInfoBean{
    
    public static final int ADMINISTRATOR_ROLE_ID = 77;
    public static final int REVIEWER_ROLE_ID = 76;
    public static final int PROTOCOL_CREATOR_ROLE_ID = 75;
    public static final int VIEW_PROTOCOL_ROLE_ID = 74;
    public static final int MODIFY_PROTOCOL_ROLE_ID = 73;
    public static final int AGGREGATOR_ROLE_ID = 300;
    public static final int APPROVER_ROLE_ID = 301;
    public static final int VIEWER_ROLE_ID = 302;
    private String protocolNumber;
    private int sequenceNumber;
    /**
     * Creates a new instance of ProtocolRoleInfoBean
     */
    public ProtocolRoleInfoBean() {
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
}
