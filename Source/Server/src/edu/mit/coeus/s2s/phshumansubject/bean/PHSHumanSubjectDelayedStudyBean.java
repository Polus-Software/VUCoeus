/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.s2s.phshumansubject.bean;

import java.sql.Timestamp;

/**
 *
 * @author Polusdev
 */
public class PHSHumanSubjectDelayedStudyBean {

    private Integer studyNumber;
    private String studyTitle;
    private String isAnticipatedCt;
    private byte[] justification;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String proposalNumber;
    private String fileName;
    private String ContentType;

    public Integer getStudyNumber() {
        return studyNumber;
    }

    public void setStudyNumber(Integer studyNumber) {
        this.studyNumber = studyNumber;
    }

    public String getStudyTitle() {
        return studyTitle;
    }

    public void setStudyTitle(String studyTitle) {
        this.studyTitle = studyTitle;
    }

    public String getIsAnticipatedCt() {
        return isAnticipatedCt;
    }

    public void setIsAnticipatedCt(String isAnticipatedCt) {
        this.isAnticipatedCt = isAnticipatedCt;
    }

    public byte[] getJustification() {
        return justification;
    }

    public void setJustification(byte[] justification) {
        this.justification = justification;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * @return the updateTimestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * @param updateTimestamp the updateTimestamp to set
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the ContentType
     */
    public String getContentType() {
        return ContentType;
    }

    /**
     * @param ContentType the ContentType to set
     */
    public void setContentType(String ContentType) {
        this.ContentType = ContentType;
    }

}
