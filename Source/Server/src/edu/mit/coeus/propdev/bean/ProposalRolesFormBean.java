/*
 * ProtocolRolesFormBean.java
 *
 * Created on April 11, 2003, 4:46 PM
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import edu.mit.coeus.bean.*;

/**
 *
 * @author  mukund
 */
public class ProposalRolesFormBean extends RoleBean {

    private String proposalNumber;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;

    
    /** Creates new ProtocolRolesFormBean */
    public ProposalRolesFormBean() {
        
    }
    
    
    public ProposalRolesFormBean(RoleBean roleBean) {
        super( roleBean );
    }
    
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
}
