/*
 * ProposalHierarchyChildTypeBean.java
 *
 * Created on January 2, 2006, 6:52 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Timestamp;

/**
 *
 * @author  tarique
 */
public class ProposalHierarchyChildTypeBean implements BaseBean,java.io.Serializable{
    
    private int proposalHierarchyChildCode;
    private String description;
    private String comments;
    private String acType;
    private String updateUser;
    private Timestamp updateTimestamp;
  
    private int awProposalHierarchyChildCode;
    private String awDescription;
    /** Creates a new instance of ProposalHierarchyChildTypeBean */
    public ProposalHierarchyChildTypeBean() {
    }
    
    /**
     * Getter for property proposalHierarchyChildCode.
     * @return Value of property proposalHierarchyChildCode.
     */
    public int getProposalHierarchyChildCode() {
        return proposalHierarchyChildCode;
    }
    
    /**
     * Setter for property proposalHierarchyChildCode.
     * @param proposalHierarchyChildCode New value of property proposalHierarchyChildCode.
     */
    public void setProposalHierarchyChildCode(int proposalHierarchyChildCode) {
        this.proposalHierarchyChildCode = proposalHierarchyChildCode;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /**
     * Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
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
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
}
