/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sony
 */
public class CoiNotesInfoBean implements Serializable{

    public CoiNotesInfoBean() {
    }

    private String coiDisclosureNumber;
    private Integer sequenceNumber;
    private String dsclDispositionstatus;
    private Integer entryNumber;
    private String notepadEntryTypeCode;
    private String notesType;
    private String description;
    private Date updateTimestamp;
    private String updateUser;
    private String title;
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
     * @return the sequenceNumber
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the dsclDispositionstatus
     */
    public String getDsclDispositionstatus() {
        return dsclDispositionstatus;
    }

    /**
     * @param dsclDispositionstatus the dsclDispositionstatus to set
     */
    public void setDsclDispositionstatus(String dsclDispositionstatus) {
        this.dsclDispositionstatus = dsclDispositionstatus;
    }

    /**
     * @return the entryNumber
     */
    public Integer getEntryNumber() {
        return entryNumber;
    }

    /**
     * @param entryNumber the entryNumber to set
     */
    public void setEntryNumber(Integer entryNumber) {
        this.entryNumber = entryNumber;
    }

    /**
     * @return the notepadEntryTypeCode
     */
    public String getNotepadEntryTypeCode() {
        return notepadEntryTypeCode;
    }

    /**
     * @param notepadEntryTypeCode the notepadEntryTypeCode to set
     */
    public void setNotepadEntryTypeCode(String notepadEntryTypeCode) {
        this.notepadEntryTypeCode = notepadEntryTypeCode;
    }

    /**
     * @return the notesType
     */
    public String getNotesType() {
        return notesType;
    }

    /**
     * @param notesType the notesType to set
     */
    public void setNotesType(String notesType) {
        this.notesType = notesType;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the tittle
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param tittle the tittle to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    
}
