/*
 * DBAttachmentBean.java
 *
 * Created on January 12, 2005, 3:52 PM
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.s2s.Attachment;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author  geot
 */
public class DBAttachmentBean  extends Attachment{
    private String awContentId;
    private Timestamp updateTimestamp;
    private String updateUser;
    /** Creates a new instance of DBAttachmentBean */
    public DBAttachmentBean(){
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property awContentId.
     * @return Value of property awContentId.
     */
    public java.lang.String getAwContentId() {
        return awContentId;
    }
    
    /**
     * Setter for property awContentId.
     * @param awContentId New value of property awContentId.
     */
    public void setAwContentId(java.lang.String awContentId) {
        this.awContentId = awContentId;
    }
    
}
