/*
 * MetaRuleBean.java
 *
 * Created on October 10, 2005, 4:56 PM
 */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  nadhgj
 */

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;

/**
 *
 * @author  chandrashekar
 */
//public class MetaRuleDetailBean implements  BaseBean, Serializable{
public class MetaRuleDetailBean implements BaseBean,Serializable{
    
    private String metaRuleId;
    private String ruleId;
    private String nodeId;
    private String parentNodeId;
    private String description;
    private String nextNode;
    private String nodeIfTrue;
    private String nodeIfFalse;
    private java.sql.Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    /** Creates a new instance of NodeBean */
    public MetaRuleDetailBean() {
    }
    
    /**
     * Getter for property nodeId.
     * @return Value of property nodeId.
     */
    public String getNodeId() {
        return nodeId;
    }
    
    /**
     * Setter for property nodeId.
     * @param nodeId New value of property nodeId.
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    /**
     * Getter for property parentNodeId.
     * @return Value of property parentNodeId.
     */
    public String getParentNodeId() {
        return parentNodeId;
    }
    
    /**
     * Setter for property parentNodeId.
     * @param parentNodeId New value of property parentNodeId.
     */
    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }
    
//     public String toString() {
//           return discription;
//    }
    
    /**
     * Getter for property nextNode.
     * @return Value of property nextNode.
     */
    public java.lang.String getNextNode() {
        return nextNode;
    }
    
    /**
     * Setter for property nextNode.
     * @param nextNode New value of property nextNode.
     */
    public void setNextNode(java.lang.String nextNode) {
        this.nextNode = nextNode;
    }
    
    /**
     * Getter for property nodeIfTrue.
     * @return Value of property nodeIfTrue.
     */
    public java.lang.String getNodeIfTrue() {
        return nodeIfTrue;
    }
    
    /**
     * Setter for property nodeIfTrue.
     * @param nodeIfTrue New value of property nodeIfTrue.
     */
    public void setNodeIfTrue(java.lang.String nodeIfTrue) {
        this.nodeIfTrue = nodeIfTrue;
    }
    
    /**
     * Getter for property nodeIfFalse.
     * @return Value of property nodeIfFalse.
     */
    public java.lang.String getNodeIfFalse() {
        return nodeIfFalse;
    }
    
    /**
     * Setter for property nodeIfFalse.
     * @param nodeIfFalse New value of property nodeIfFalse.
     */
    public void setNodeIfFalse(java.lang.String nodeIfFalse) {
        this.nodeIfFalse = nodeIfFalse;
    }
    
    /**
     * Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /**
     * Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimeStamp(java.sql.Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
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
     * Getter for property metaRuleId.
     * @return Value of property metaRuleId.
     */
    public java.lang.String getMetaRuleId() {
        return metaRuleId;
    }
    
    /**
     * Setter for property metaRuleId.
     * @param metaRuleId New value of property metaRuleId.
     */
    public void setMetaRuleId(java.lang.String metaRuleId) {
        this.metaRuleId = metaRuleId;
    }
    
    /**
     * Getter for property ruleId.
     * @return Value of property ruleId.
     */
    public java.lang.String getRuleId() {
        return ruleId;
    }
    
    /**
     * Setter for property ruleId.
     * @param ruleId New value of property ruleId.
     */
    public void setRuleId(java.lang.String ruleId) {
        this.ruleId = ruleId;
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
    
    public String toString(){
        return nodeId;
    }
}
    
