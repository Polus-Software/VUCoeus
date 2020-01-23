/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utk.coeuslite.propdev.bean;

/**
 *
 * @author unNamed
 */
public class PersonCertifyInfoBean {
    private String personId;
    private String userId;
    private String userName;
    private String proposalNumber;
    private String fromName;//indicates where certifation came from proposalDetails,Notification,Proposal summary
    private String proposalRole;
    //COEUSQA 3827
    private String updateTimestamp;
    private String updateUser;
    //COEUSQA 3827

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProposalRole() {
        return proposalRole;
    }

    public void setProposalRole(String proposalRole) {
        this.proposalRole = proposalRole;
    }
    
}
