/*
 * NegotiationAttachmentBean.java
 *
 * Created on January 19, 2009, 7:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.negotiation.bean;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import java.sql.Timestamp;

/**
 *
 * @author keerthyjayaraj
 */
public class NegotiationAttachmentBean extends CoeusAttachmentBean implements java.io.Serializable{
    
    /**
     * Creates a new instance of NegotiationAttachmentBean
     */
    public NegotiationAttachmentBean() {
    }
    
    private String negotiationNumber;
    private int activityNumber;
    private Timestamp updateTimestamp;
    private String updateUser;
    private String acType;

    public String getAcType() {
        return acType;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public String getNegotiationNumber() {
        return negotiationNumber;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }

    public void setNegotiationNumber(String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

   


}
