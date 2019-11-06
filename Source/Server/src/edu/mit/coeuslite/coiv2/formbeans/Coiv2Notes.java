/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.formbeans;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Mr Lijo joy
 */
public class Coiv2Notes extends ActionForm {

    private String coiDisclosureNumber;
    private String coiSequenceNumber;
    private String entiryNumber;
    private String coiNotepadEntryTypeCode;
    private String title;
    private String comments;
    private String resttrictedView;
    private String updateTimestamp;
    private String updateUser;
    private String acType;
    
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
    public String getCoiSequenceNumber() {
        return coiSequenceNumber;
    }

    /**
     * @param coiSequenceNumber the coiSequenceNumber to set
     */
    public void setCoiSequenceNumber(String coiSequenceNumber) {
        this.coiSequenceNumber = coiSequenceNumber;
    }

    /**
     * @return the entiryNumber
     */
    public String getEntiryNumber() {
        return entiryNumber;
    }

    /**
     * @param entiryNumber the entiryNumber to set
     */
    public void setEntiryNumber(String entiryNumber) {
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
     * @return the Comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param Comments the Comments to set
     */
    public void setComments(String Comments) {
        this.comments = Comments;
    }

    /**
     * @return the esttrictedView
     */
    public String getEsttrictedView() {
        return getResttrictedView();
    }

    /**
     * @param esttrictedView the esttrictedView to set
     */
    public void setEsttrictedView(String resttrictedView) {
        this.setResttrictedView(resttrictedView);
    }

    /**
     * @return the updateTimestamp
     */
    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(String updateTimestamp) {
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
    
}
