/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Mr Lijo JOy
 */
public class Coiv2NotesBean implements Serializable {
    private String coiDisclosureNumber;
    private Integer coiSequenceNumber;
    private Integer entiryNumber;
    private String coiNotepadEntryTypeCode;
    private String title;
    private String comments;
    private String resttrictedView;
    private Date updateTimestamp;
    private String updateUser;
    private String updateUserName;
    private String acType;
    private String moduleItemKey;
    private String coiNotepadEntryTypeDesc;

    public String getCoiNotepadEntryTypeDesc() {
        return coiNotepadEntryTypeDesc;
    }

    public void setCoiNotepadEntryTypeDesc(String coiNotepadEntryTypeDesc) {
        this.coiNotepadEntryTypeDesc = coiNotepadEntryTypeDesc;
    }

    /**
     * @return the coiDisclosureNumber
     */
    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    /**
     * @param coiDisclosureNumber the coiDisclosureNumber to set
     */
    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    /**
     * @return the coiSequenceNumber
     */
    public Integer getCoiSequenceNumber() {
        return coiSequenceNumber;
    }

    /**
     * @param coiSequenceNumber the coiSequenceNumber to set
     */
    public void setCoiSequenceNumber(Integer coiSequenceNumber) {
        this.coiSequenceNumber = coiSequenceNumber;
    }

    /**
     * @return the entiryNumber
     */
    public Integer getEntiryNumber() {
        return entiryNumber;
    }

    /**
     * @param entiryNumber the entiryNumber to set
     */
    public void setEntiryNumber(Integer entiryNumber) {
        this.entiryNumber = entiryNumber;
    }

    /**
     * @return the coiNotepadEntryTypeCode
     */
    public String getCoiNotepadEntryTypeCode() {
        return coiNotepadEntryTypeCode;
    }

    /**
     * @param coiNotepadEntryTypeCode the coiNotepadEntryTypeCode to set
     */
    public void setCoiNotepadEntryTypeCode(String coiNotepadEntryTypeCode) {
        this.coiNotepadEntryTypeCode = coiNotepadEntryTypeCode;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the Comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param Comments the Comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the resttrictedView
     */
    public String getResttrictedView() {
        return resttrictedView;
    }

    /**
     * @param resttrictedView the resttrictedView to set
     */
    public void setResttrictedView(String resttrictedView) {
        this.resttrictedView = resttrictedView;
    }

    /**
     * @return the updateTimestamp
     */
    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * @param updateUserName the updateUserName to set
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    /**
     * @return the acType
     */
    public String getAcType() {
        return acType;
    }

    /**
     * @param acType the acType to set
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }

    /**
     * @return the moduleItemKey
     */
    public String getModuleItemKey() {
        return moduleItemKey;
    }

    /**
     * @param moduleItemKey the moduleItemKey to set
     */
    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }
    
}
