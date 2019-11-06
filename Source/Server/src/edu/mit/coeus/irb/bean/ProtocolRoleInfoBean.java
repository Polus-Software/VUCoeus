/*
 * ProtocolRoleInfoBean.java
 *
 * Created on August 2, 2009, 2:16 PM
 *
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.RoleInfoBean;

/**
 *
 * @author sreenath
 */
public class ProtocolRoleInfoBean extends RoleInfoBean{
    
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
