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
public class CoiAwardInfoBean implements Serializable {

    public CoiAwardInfoBean() {
    }

    private String mitAwardNumber;
    private Integer sequenceNumber;
    private String modificationNumber;
    private String sponsorAwardNumber;
    private Integer statusCode;
    private String awardStatusDescription;
    private Integer templateCode;
    private Date awardExecutionDate;
    private Date awardEffectiveDate;
    private Date startDate;
    private Integer sponsorCode;
    private String accountNumber;
    private String apprvdEquipmentIndicator;
    private String apprvdForiegnTripIndicator;
    private String apprvdSubcontractIndicator;
    private String paymentScheduleIndicator;
    private String idcIndicator;
    private String transferSponsorIndicator;
    private String costSharingIndicator;
    private Date updateTimestamp;
    private String updateUser;
    private String specialReviewIndicator;
    private String scienceCodeIndicator;
    private String nsfCode;
    private String keyPersonIndicator;
    private String projectSponsor;
    private boolean checked;

    //added to get proposal details from eps_proposal table
    private String title;
    private String awardTitle;
    private String proposalNumber;
    private String proposalTypeDesc;

    /**
     * @return the mitAwardNumber
     */
    public String getMitAwardNumber() {
        return mitAwardNumber;
    }

    /**
     * @param mitAwardNumber the mitAwardNumber to set
     */
    public void setMitAwardNumber(String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
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
     * @return the modificationNumber
     */
    public String getModificationNumber() {
        return modificationNumber;
    }

    /**
     * @param modificationNumber the modificationNumber to set
     */
    public void setModificationNumber(String modificationNumber) {
        this.modificationNumber = modificationNumber;
    }

    /**
     * @return the sponsorAwardNumber
     */
    public String getSponsorAwardNumber() {
        return sponsorAwardNumber;
    }

    /**
     * @param sponsorAwardNumber the sponsorAwardNumber to set
     */
    public void setSponsorAwardNumber(String sponsorAwardNumber) {
        this.sponsorAwardNumber = sponsorAwardNumber;
    }

    /**
     * @return the statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode the statusCode to set
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return the awardStatusDescription
     */
    public String getAwardStatusDescription() {
        return awardStatusDescription;
    }

    /**
     * @param awardStatusDescription the awardStatusDescription to set
     */
    public void setAwardStatusDescription(String awardStatusDescription) {
        this.awardStatusDescription = awardStatusDescription;
    }

    /**
     * @return the templateCode
     */
    public Integer getTemplateCode() {
        return templateCode;
    }

    /**
     * @param templateCode the templateCode to set
     */
    public void setTemplateCode(Integer templateCode) {
        this.templateCode = templateCode;
    }

    /**
     * @return the awardExecutionDate
     */
    public Date getAwardExecutionDate() {
        return awardExecutionDate;
    }

    /**
     * @param awardExecutionDate the awardExecutionDate to set
     */
    public void setAwardExecutionDate(Date awardExecutionDate) {
        this.awardExecutionDate = awardExecutionDate;
    }

    /**
     * @return the awardEffectiveDate
     */
    public Date getAwardEffectiveDate() {
        return awardEffectiveDate;
    }

    /**
     * @param awardEffectiveDate the awardEffectiveDate to set
     */
    public void setAwardEffectiveDate(Date awardEffectiveDate) {
        this.awardEffectiveDate = awardEffectiveDate;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the sponsorCode
     */
    public Integer getSponsorCode() {
        return sponsorCode;
    }

    /**
     * @param sponsorCode the sponsorCode to set
     */
    public void setSponsorCode(Integer sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the apprvdEquipmentIndicator
     */
    public String getApprvdEquipmentIndicator() {
        return apprvdEquipmentIndicator;
    }

    /**
     * @param apprvdEquipmentIndicator the apprvdEquipmentIndicator to set
     */
    public void setApprvdEquipmentIndicator(String apprvdEquipmentIndicator) {
        this.apprvdEquipmentIndicator = apprvdEquipmentIndicator;
    }

    /**
     * @return the apprvdForiegnTripIndicator
     */
    public String getApprvdForiegnTripIndicator() {
        return apprvdForiegnTripIndicator;
    }

    /**
     * @param apprvdForiegnTripIndicator the apprvdForiegnTripIndicator to set
     */
    public void setApprvdForiegnTripIndicator(String apprvdForiegnTripIndicator) {
        this.apprvdForiegnTripIndicator = apprvdForiegnTripIndicator;
    }

    /**
     * @return the apprvdSubcontractIndicator
     */
    public String getApprvdSubcontractIndicator() {
        return apprvdSubcontractIndicator;
    }

    /**
     * @param apprvdSubcontractIndicator the apprvdSubcontractIndicator to set
     */
    public void setApprvdSubcontractIndicator(String apprvdSubcontractIndicator) {
        this.apprvdSubcontractIndicator = apprvdSubcontractIndicator;
    }

    /**
     * @return the paymentScheduleIndicator
     */
    public String getPaymentScheduleIndicator() {
        return paymentScheduleIndicator;
    }

    /**
     * @param paymentScheduleIndicator the paymentScheduleIndicator to set
     */
    public void setPaymentScheduleIndicator(String paymentScheduleIndicator) {
        this.paymentScheduleIndicator = paymentScheduleIndicator;
    }

    /**
     * @return the idcIndicator
     */
    public String getIdcIndicator() {
        return idcIndicator;
    }

    /**
     * @param idcIndicator the idcIndicator to set
     */
    public void setIdcIndicator(String idcIndicator) {
        this.idcIndicator = idcIndicator;
    }

    /**
     * @return the transferSponsorIndicator
     */
    public String getTransferSponsorIndicator() {
        return transferSponsorIndicator;
    }

    /**
     * @param transferSponsorIndicator the transferSponsorIndicator to set
     */
    public void setTransferSponsorIndicator(String transferSponsorIndicator) {
        this.transferSponsorIndicator = transferSponsorIndicator;
    }

    /**
     * @return the costSharingIndicator
     */
    public String getCostSharingIndicator() {
        return costSharingIndicator;
    }

    /**
     * @param costSharingIndicator the costSharingIndicator to set
     */
    public void setCostSharingIndicator(String costSharingIndicator) {
        this.costSharingIndicator = costSharingIndicator;
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
     * @return the specialReviewIndicator
     */
    public String getSpecialReviewIndicator() {
        return specialReviewIndicator;
    }

    /**
     * @param specialReviewIndicator the specialReviewIndicator to set
     */
    public void setSpecialReviewIndicator(String specialReviewIndicator) {
        this.specialReviewIndicator = specialReviewIndicator;
    }

    /**
     * @return the scienceCodeIndicator
     */
    public String getScienceCodeIndicator() {
        return scienceCodeIndicator;
    }

    /**
     * @param scienceCodeIndicator the scienceCodeIndicator to set
     */
    public void setScienceCodeIndicator(String scienceCodeIndicator) {
        this.scienceCodeIndicator = scienceCodeIndicator;
    }

    /**
     * @return the nsfCode
     */
    public String getNsfCode() {
        return nsfCode;
    }

    /**
     * @param nsfCode the nsfCode to set
     */
    public void setNsfCode(String nsfCode) {
        this.nsfCode = nsfCode;
    }

    /**
     * @return the keyPersonIndicator
     */
    public String getKeyPersonIndicator() {
        return keyPersonIndicator;
    }

    /**
     * @param keyPersonIndicator the keyPersonIndicator to set
     */
    public void setKeyPersonIndicator(String keyPersonIndicator) {
        this.keyPersonIndicator = keyPersonIndicator;
    }

    /**
     * @return the projectSponsor
     */
    public String getProjectSponsor() {
        return projectSponsor;
    }

    /**
     * @param projectSponsor the projectSponsor to set
     */
    public void setProjectSponsor(String projectSponsor) {
        this.projectSponsor = projectSponsor;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
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
     * @return the proposalNumber
     */
    public String getProposalNumber() {
        return proposalNumber;
    }

    /**
     * @param proposalNumber the proposalNumber to set
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * @return the proposalTypeDesc
     */
    public String getProposalTypeDesc() {
        return proposalTypeDesc;
    }

    /**
     * @param proposalTypeDesc the proposalTypeDesc to set
     */
    public void setProposalTypeDesc(String proposalTypeDesc) {
        this.proposalTypeDesc = proposalTypeDesc;
    }

    /**
     * @return the awardTitle
     */
    public String getAwardTitle() {
        return awardTitle;
    }

    /**
     * @param awardTitle the awardTitle to set
     */
    public void setAwardTitle(String awardTitle) {
        this.awardTitle = awardTitle;
    }

    
    
}
