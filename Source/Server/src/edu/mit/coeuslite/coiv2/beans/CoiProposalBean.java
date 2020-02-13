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
public class CoiProposalBean implements Serializable{
           private String proposalNumber;
           private Integer PproposalType;
           private Integer statusCode;
           private Integer creationStatusCode;
           private String baseProposalNumber;
           private String continuedFrom;
           private String templateFlag;
           private String organizationId;
           private String performingOrganizationId;
           private String currentAccountNumber;
           private String awardNumber;
           private String title;
           private String pid;
           private String personName;
           private String pjtName;
           private String cfdaCode;
           private String instsponsorCode;
           private String sponsorCode;
           private String primeSponsorCode;
           private String sponsorProposalNuber;
           private String intrCoopActivitiesFlag;
           private String intrCountrylist;
           private String otherAgencyFlag;
           private String noticeOfOppr;
           private String programAnnouncementNumber;
           private String programAnnouncementTitle;
           private Integer activityType;
           private Date startDate;
           private Date requestStartDateTotal;
           private Date endDate;
           private Date requestEndDateTotal;
           private Integer durationMonth;
           private String numberCopies;
           private Date deadLineDate;
           private String deadlineType;
           private Integer mailingAddresId;
           private String mailBy;
           private String mailType;
           private String carrierCodeType;
           private String carrierCode;
           private String mailDescription;
           private String mailAccountNumber;
           private String subcontractFlag;
           private String narrativeStatus;
           private String budgetStatus;
           private String unitNumber;
           private Date createTimestamp;
           private String createUser;
           private Date updateTimestamp;
           private String updateUser;
           private String anticipatedAwardType;
           private String nsfCode;
           private String nopDesc;
           private String agencyProgramCode;
           private String agencyDivCode;
           private String proposalTypeDesc;
           private String sponsorName;
           private String roleName;

           private Double totalCost;
           private Double totalDirectCost;
           private Double totalIndirectCost;
           private String acType;
           private boolean checked;
           private boolean nonIntegrated;
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
     * @return the PproposalType
     */
    public Integer getPproposalType() {
        return PproposalType;
    }

    /**
     * @param PproposalType the PproposalType to set
     */
    public void setPproposalType(Integer PproposalType) {
        this.PproposalType = PproposalType;
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
     * @return the creationStatusCode
     */
    public Integer getCreationStatusCode() {
        return creationStatusCode;
    }

    /**
     * @param creationStatusCode the creationStatusCode to set
     */
    public void setCreationStatusCode(Integer creationStatusCode) {
        this.creationStatusCode = creationStatusCode;
    }

    /**
     * @return the baseProposalNumber
     */
    public String getBaseProposalNumber() {
        return baseProposalNumber;
    }

    /**
     * @param baseProposalNumber the baseProposalNumber to set
     */
    public void setBaseProposalNumber(String baseProposalNumber) {
        this.baseProposalNumber = baseProposalNumber;
    }

    /**
     * @return the continuedFrom
     */
    public String getContinuedFrom() {
        return continuedFrom;
    }

    /**
     * @param continuedFrom the continuedFrom to set
     */
    public void setContinuedFrom(String continuedFrom) {
        this.continuedFrom = continuedFrom;
    }

    /**
     * @return the templateFlag
     */
    public String getTemplateFlag() {
        return templateFlag;
    }

    /**
     * @param templateFlag the templateFlag to set
     */
    public void setTemplateFlag(String templateFlag) {
        this.templateFlag = templateFlag;
    }

    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the performingOrganizationId
     */
    public String getPerformingOrganizationId() {
        return performingOrganizationId;
    }

    /**
     * @param performingOrganizationId the performingOrganizationId to set
     */
    public void setPerformingOrganizationId(String performingOrganizationId) {
        this.performingOrganizationId = performingOrganizationId;
    }

    /**
     * @return the currentAccountNumber
     */
    public String getCurrentAccountNumber() {
        return currentAccountNumber;
    }

    /**
     * @param currentAccountNumber the currentAccountNumber to set
     */
    public void setCurrentAccountNumber(String currentAccountNumber) {
        this.currentAccountNumber = currentAccountNumber;
    }

    /**
     * @return the awardNumber
     */
    public String getAwardNumber() {
        return awardNumber;
    }

    /**
     * @param awardNumber the awardNumber to set
     */
    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
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
     * @return the cfdaCode
     */
    public String getCfdaCode() {
        return cfdaCode;
    }

    /**
     * @param cfdaCode the cfdaCode to set
     */
    public void setCfdaCode(String cfdaCode) {
        this.cfdaCode = cfdaCode;
    }

    /**
     * @return the sponsorCode
     */
    public String getSponsorCode() {
        return sponsorCode;
    }

    /**
     * @param sponsorCode the sponsorCode to set
     */
    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    /**
     * @return the primeSponsorCode
     */
    public String getPrimeSponsorCode() {
        return primeSponsorCode;
    }

    /**
     * @param primeSponsorCode the primeSponsorCode to set
     */
    public void setPrimeSponsorCode(String primeSponsorCode) {
        this.primeSponsorCode = primeSponsorCode;
    }

    /**
     * @return the sponsorProposalNuber
     */
    public String getSponsorProposalNuber() {
        return sponsorProposalNuber;
    }

    /**
     * @param sponsorProposalNuber the sponsorProposalNuber to set
     */
    public void setSponsorProposalNuber(String sponsorProposalNuber) {
        this.sponsorProposalNuber = sponsorProposalNuber;
    }

    /**
     * @return the intrCoopActivitiesFlag
     */
    public String getIntrCoopActivitiesFlag() {
        return intrCoopActivitiesFlag;
    }

    /**
     * @param intrCoopActivitiesFlag the intrCoopActivitiesFlag to set
     */
    public void setIntrCoopActivitiesFlag(String intrCoopActivitiesFlag) {
        this.intrCoopActivitiesFlag = intrCoopActivitiesFlag;
    }

    /**
     * @return the intrCountrylist
     */
    public String getIntrCountrylist() {
        return intrCountrylist;
    }

    /**
     * @param intrCountrylist the intrCountrylist to set
     */
    public void setIntrCountrylist(String intrCountrylist) {
        this.intrCountrylist = intrCountrylist;
    }

    /**
     * @return the otherAgencyFlag
     */
    public String getOtherAgencyFlag() {
        return otherAgencyFlag;
    }

    /**
     * @param otherAgencyFlag the otherAgencyFlag to set
     */
    public void setOtherAgencyFlag(String otherAgencyFlag) {
        this.otherAgencyFlag = otherAgencyFlag;
    }

    /**
     * @return the noticeOfOppr
     */
    public String getNoticeOfOppr() {
        return noticeOfOppr;
    }

    /**
     * @param noticeOfOppr the noticeOfOppr to set
     */
    public void setNoticeOfOppr(String noticeOfOppr) {
        this.noticeOfOppr = noticeOfOppr;
    }

    /**
     * @return the programAnnouncementNumber
     */
    public String getProgramAnnouncementNumber() {
        return programAnnouncementNumber;
    }

    /**
     * @param programAnnouncementNumber the programAnnouncementNumber to set
     */
    public void setProgramAnnouncementNumber(String programAnnouncementNumber) {
        this.programAnnouncementNumber = programAnnouncementNumber;
    }

    /**
     * @return the programAnnouncementTitle
     */
    public String getProgramAnnouncementTitle() {
        return programAnnouncementTitle;
    }

    /**
     * @param programAnnouncementTitle the programAnnouncementTitle to set
     */
    public void setProgramAnnouncementTitle(String programAnnouncementTitle) {
        this.programAnnouncementTitle = programAnnouncementTitle;
    }

    /**
     * @return the activityType
     */
    public Integer getActivityType() {
        return activityType;
    }

    /**
     * @param activityType the activityType to set
     */
    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
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
     * @return the requestStartDateTotal
     */
    public Date getRequestStartDateTotal() {
        return requestStartDateTotal;
    }

    /**
     * @param requestStartDateTotal the requestStartDateTotal to set
     */
    public void setRequestStartDateTotal(Date requestStartDateTotal) {
        this.requestStartDateTotal = requestStartDateTotal;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the requestEndDateTotal
     */
    public Date getRequestEndDateTotal() {
        return requestEndDateTotal;
    }

    /**
     * @param requestEndDateTotal the requestEndDateTotal to set
     */
    public void setRequestEndDateTotal(Date requestEndDateTotal) {
        this.requestEndDateTotal = requestEndDateTotal;
    }

    /**
     * @return the durationMonth
     */
    public Integer getDurationMonth() {
        return durationMonth;
    }

    /**
     * @param durationMonth the durationMonth to set
     */
    public void setDurationMonth(Integer durationMonth) {
        this.durationMonth = durationMonth;
    }

    /**
     * @return the numberCopies
     */
    public String getNumberCopies() {
        return numberCopies;
    }

    /**
     * @param numberCopies the numberCopies to set
     */
    public void setNumberCopies(String numberCopies) {
        this.numberCopies = numberCopies;
    }

    /**
     * @return the deadLineDate
     */
    public Date getDeadLineDate() {
        return deadLineDate;
    }

    /**
     * @param deadLineDate the deadLineDate to set
     */
    public void setDeadLineDate(Date deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    /**
     * @return the deadlineType
     */
    public String getDeadlineType() {
        return deadlineType;
    }

    /**
     * @param deadlineType the deadlineType to set
     */
    public void setDeadlineType(String deadlineType) {
        this.deadlineType = deadlineType;
    }

    /**
     * @return the mailingAddresId
     */
    public Integer getMailingAddresId() {
        return mailingAddresId;
    }

    /**
     * @param mailingAddresId the mailingAddresId to set
     */
    public void setMailingAddresId(Integer mailingAddresId) {
        this.mailingAddresId = mailingAddresId;
    }

    /**
     * @return the mailBy
     */
    public String getMailBy() {
        return mailBy;
    }

    /**
     * @param mailBy the mailBy to set
     */
    public void setMailBy(String mailBy) {
        this.mailBy = mailBy;
    }

    /**
     * @return the mailType
     */
    public String getMailType() {
        return mailType;
    }

    /**
     * @param mailType the mailType to set
     */
    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    /**
     * @return the carrierCodeType
     */
    public String getCarrierCodeType() {
        return carrierCodeType;
    }

    /**
     * @param carrierCodeType the carrierCodeType to set
     */
    public void setCarrierCodeType(String carrierCodeType) {
        this.carrierCodeType = carrierCodeType;
    }

    /**
     * @return the carrierCode
     */
    public String getCarrierCode() {
        return carrierCode;
    }

    /**
     * @param carrierCode the carrierCode to set
     */
    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    /**
     * @return the mailDescription
     */
    public String getMailDescription() {
        return mailDescription;
    }

    /**
     * @param mailDescription the mailDescription to set
     */
    public void setMailDescription(String mailDescription) {
        this.mailDescription = mailDescription;
    }

    /**
     * @return the mailAccountNumber
     */
    public String getMailAccountNumber() {
        return mailAccountNumber;
    }

    /**
     * @param mailAccountNumber the mailAccountNumber to set
     */
    public void setMailAccountNumber(String mailAccountNumber) {
        this.mailAccountNumber = mailAccountNumber;
    }

    /**
     * @return the subcontractFlag
     */
    public String getSubcontractFlag() {
        return subcontractFlag;
    }

    /**
     * @param subcontractFlag the subcontractFlag to set
     */
    public void setSubcontractFlag(String subcontractFlag) {
        this.subcontractFlag = subcontractFlag;
    }

    /**
     * @return the narrativeStatus
     */
    public String getNarrativeStatus() {
        return narrativeStatus;
    }

    /**
     * @param narrativeStatus the narrativeStatus to set
     */
    public void setNarrativeStatus(String narrativeStatus) {
        this.narrativeStatus = narrativeStatus;
    }

    /**
     * @return the budgetStatus
     */
    public String getBudgetStatus() {
        return budgetStatus;
    }

    /**
     * @param budgetStatus the budgetStatus to set
     */
    public void setBudgetStatus(String budgetStatus) {
        this.budgetStatus = budgetStatus;
    }

    /**
     * @return the unitNumber
     */
    public String getUnitNumber() {
        return unitNumber;
    }

    /**
     * @param unitNumber the unitNumber to set
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    /**
     * @return the createTimestamp
     */
    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    /**
     * @param createTimestamp the createTimestamp to set
     */
    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
     * @return the anticipatedAwardType
     */
    public String getAnticipatedAwardType() {
        return anticipatedAwardType;
    }

    /**
     * @param anticipatedAwardType the anticipatedAwardType to set
     */
    public void setAnticipatedAwardType(String anticipatedAwardType) {
        this.anticipatedAwardType = anticipatedAwardType;
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
     * @return the nopDesc
     */
    public String getNopDesc() {
        return nopDesc;
    }

    /**
     * @param nopDesc the nopDesc to set
     */
    public void setNopDesc(String nopDesc) {
        this.nopDesc = nopDesc;
    }

    /**
     * @return the agencyProgramCode
     */
    public String getAgencyProgramCode() {
        return agencyProgramCode;
    }

    /**
     * @param agencyProgramCode the agencyProgramCode to set
     */
    public void setAgencyProgramCode(String agencyProgramCode) {
        this.agencyProgramCode = agencyProgramCode;
    }

    /**
     * @return the agencyDivCode
     */
    public String getAgencyDivCode() {
        return agencyDivCode;
    }

    /**
     * @param agencyDivCode the agencyDivCode to set
     */
    public void setAgencyDivCode(String agencyDivCode) {
        this.agencyDivCode = agencyDivCode;
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
     * @return the sponsorName
     */
    public String getSponsorName() {
        return sponsorName;
    }

    /**
     * @param sponsorName the sponsorName to set
     */
    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    /**
     * @return the roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * @return the totalCost
     */
    public Double getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the totalDirectCost
     */
    public Double getTotalDirectCost() {
        return totalDirectCost;
    }

    /**
     * @param totalDirectCost the totalDirectCost to set
     */
    public void setTotalDirectCost(Double totalDirectCost) {
        this.totalDirectCost = totalDirectCost;
    }

    /**
     * @return the totalIndirectCost
     */
    public Double getTotalIndirectCost() {
        return totalIndirectCost;
    }

    /**
     * @param totalIndirectCost the totalIndirectCost to set
     */
    public void setTotalIndirectCost(Double totalIndirectCost) {
        this.totalIndirectCost = totalIndirectCost;
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
     * @return the nonIntegrated
     */
    public boolean isNonIntegrated() {
        return nonIntegrated;
    }

    /**
     * @param nonIntegrated the nonIntegrated to set
     */
    public void setNonIntegrated(boolean nonIntegrated) {
        this.nonIntegrated = nonIntegrated;
    }

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * @return the personName
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName the personName to set
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return the instsponsorCode
     */
    public String getInstsponsorCode() {
        return instsponsorCode;
    }

    /**
     * @param instsponsorCode the instsponsorCode to set
     */
    public void setInstsponsorCode(String instsponsorCode) {
        this.instsponsorCode = instsponsorCode;
    }

    /**
     * @return the pjtName
     */
    public String getPjtName() {
        return pjtName;
    }

    /**
     * @param pjtName the pjtName to set
     */
    public void setPjtName(String pjtName) {
        this.pjtName = pjtName;
    }

  

}
