/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import edu.mit.coeuslite.coiv2.utilities.ModuleCodeType;
import java.util.Date;
import java.sql.Timestamp;

/**
 *
 * @author Sony
 * To map database values for all the cases of retrieving Disclosure details
 */
public class CoiDisclosureBean {

    private String coiDisclosureNumber;
    private Integer sequenceNumber;
    private String personId;
    private String certifiedBy;
    private String certificationText;
    private Date certificationTimestamp;
    private String certificationTimestampNew;
    private Date expirationDate;
    private Date updateTimestamp;
    private Date updateTimestampEnd;
    private String updateUser;
    private Integer disclosureDispositionCode;
    private Integer disclosureStatusCode;
    private String acType;
    private String disclosureStatus;
    private String docName;
    private String userName;
    private String dispositionStatus;
    private Integer moduleCode;
    private String moduleName;
    private String loggedPerson;
    private String entityName;
     private String entityNumber;
    private String updateTime;
    private String expirDate;
    private Integer reviewStatusCode;
    private String department;
    private Integer isFEPresent;
    private Integer isFEPresentBoth;
    private Integer isFEFlag;
    private Integer recmdAction;
    private String reviewComplete;
    private String reviewerName;
    private String recmdDescptn;
    private String updateTimestampNew;

    public String getCertificationTimestampNew() {
        return certificationTimestampNew;
    }

    public void setCertificationTimestampNew(String certificationTimestampNew) {
        this.certificationTimestampNew = certificationTimestampNew;
    }

    public String getUpdateTimestampNew() {
        return updateTimestampNew;
    }

    public void setUpdateTimestampNew(String updateTimestampNew) {
        this.updateTimestampNew = updateTimestampNew;
    }
    public String getRecmdDescptn() {
        return recmdDescptn;
    }

    public void setRecmdDescptn(String recmdDescptn) {
        this.recmdDescptn = recmdDescptn;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
     


    public Integer getRecmdAction() {
        return recmdAction;
    }

    public void setRecmdAction(Integer recmdAction) {
        this.recmdAction = recmdAction;
    }

    public String getReviewComplete() {
        return reviewComplete;
    }

    public void setReviewComplete(String reviewComplete) {
        this.reviewComplete = reviewComplete;
    }

    public Integer getIsFEPresentBoth() {
        return isFEPresentBoth;
    }

    public void setIsFEPresentBoth(Integer isFEPresentBoth) {
        this.isFEPresentBoth = isFEPresentBoth;
    }
    private Integer eventNo;
    private String expDateFormated;

    public String getExpDateFormated() {
        return expDateFormated;
    }

    public void setExpDateFormated(String expDateFormated) {
        this.expDateFormated = expDateFormated;
    }

  

    
   
    public Integer getEventNo() {
        return eventNo;
    }

    public void setEventNo(Integer eventNo) {
        this.eventNo = eventNo;
    }
    
    
    public Integer getIsFEPresent() {
        return isFEPresent;
    }

    public void setIsFEPresent(Integer isFEPresent) {
        this.isFEPresent = isFEPresent;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    private String reviewStatus;

    public Integer getReviewStatusCode() {
        return reviewStatusCode;
    }

    public void setReviewStatusCode(Integer reviewStatusCode) {
        this.reviewStatusCode = reviewStatusCode;
    }
    

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }
    private Integer coiDiscDetailsNumber;
    private String moduleItemKey;
    private String description;
    private String pjctName;

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
     * @return the personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * @return the certifiedBy
     */
    public String getCertifiedBy() {
        return certifiedBy;
    }

    /**
     * @param certifiedBy the certifiedBy to set
     */
    public void setCertifiedBy(String certifiedBy) {
        this.certifiedBy = certifiedBy;
    }

    /**
     * @return the certificationText
     */
    public String getCertificationText() {
        return certificationText;
    }

    /**
     * @param certificationText the certificationText to set
     */
    public void setCertificationText(String certificationText) {
        this.certificationText = certificationText;
    }

    /**
     * @return the certificationTimestamp
     */
    public Date getCertificationTimestamp() {
        return certificationTimestamp;
    }

    /**
     * @param certificationTimestamp the certificationTimestamp to set
     */
    public void setCertificationTimestamp(Date certificationTimestamp) {
        this.certificationTimestamp = certificationTimestamp;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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
     * @return the disclosureDispositionCode
     */
    public Integer getDisclosureDispositionCode() {
        return disclosureDispositionCode;
    }

    /**
     * @param disclosureDispositionCode the disclosureDispositionCode to set
     */
    public void setDisclosureDispositionCode(Integer disclosureDispositionCode) {
        this.disclosureDispositionCode = disclosureDispositionCode;
    }

    /**
     * @return the disclosureStatusCode
     */
    public Integer getDisclosureStatusCode() {
        return disclosureStatusCode;
    }

    /**
     * @param disclosureStatusCode the disclosureStatusCode to set
     */
    public void setDisclosureStatusCode(Integer disclosureStatusCode) {
        this.disclosureStatusCode = disclosureStatusCode;
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
     * @return the disclosureStatus
     */
    public String getDisclosureStatus() {
        return disclosureStatus;
    }

    /**
     * @param disclosureStatus the disclosureStatus to set
     */
    public void setDisclosureStatus(String disclosureStatus) {
        this.disclosureStatus = disclosureStatus;
    }

    /**
     * @return the docName
     */
    public String getDocName() {
        return docName;
    }

    /**
     * @param docName the docName to set
     */
    public void setDocName(String docName) {
        this.docName = docName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getUpdateTimestampFormat() {
        Timestamp trimestmp = new Timestamp(getUpdateTimestamp().getTime());
        return trimestmp;
    }

    /**
     * @return the dispositionStatus
     */
    public String getDispositionStatus() {
        return dispositionStatus;
    }

    /**
     * @param dispositionStatus the dispositionStatus to set
     */
    public void setDispositionStatus(String dispositionStatus) {
        this.dispositionStatus = dispositionStatus;
    }

    /**
     * @return the moduleCode
     */
    public Integer getModuleCode() {
        return moduleCode;
    }

    /**
     * @param moduleCode the moduleCode to set
     */
    public void setModuleCode(Integer moduleCode) {
        this.moduleCode = moduleCode;
       // setModuleName(moduleName);
    }

    /**
     * @return the moduleName
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName the moduleName to set
     */
    public void setModuleName(String moduleName) {
        if (this.moduleCode != null) {
            if (moduleName.equals(ModuleCodeType.protocol.getValue())) {
                this.moduleName = "IRB Protocol";
            } else if (moduleCode.equals(ModuleCodeType.award.getValue())) {
                this.moduleName = "Award";
            } else if (moduleCode.equals(ModuleCodeType.proposal.getValue())) {
                this.moduleName = "Proposal";
            } else if (moduleCode.equals(ModuleCodeType.other.getValue())) {
                this.moduleName = "Miscellaneous";
            } else if (moduleCode.equals(ModuleCodeType.annual.getValue())) {
                this.moduleName = "New";
            }
        }
    }

    /**
     * @return the loggedPerson
     */
    public String getLoggedPerson() {
        return loggedPerson;
    }

    /**
     * @param loggedPerson the loggedPerson to set
     */
    public void setLoggedPerson(String loggedPerson) {
        this.loggedPerson = loggedPerson;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the updateTimestampEnd
     */
    public Date getUpdateTimestampEnd() {
        return updateTimestampEnd;
    }

    /**
     * @param updateTimestampEnd the updateTimestampEnd to set
     */
    public void setUpdateTimestampEnd(Date updateTimestampEnd) {
        this.updateTimestampEnd = updateTimestampEnd;
    }

    /**
     * @return the updateTime
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime the updateTime to set
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return the expirDate
     */
    public String getExpirDate() {
        return expirDate;
    }

    /**
     * @param expirDate the expirDate to set
     */
    public void setExpirDate(String expirDate) {
        this.expirDate = expirDate;
    }

    /**
     * @return the reviewStatusCode
     */
//    public Integer getReviewStatusCode() {
//        return reviewStatusCode;
//    }

    /**
     * @param reviewStatusCode the reviewStatusCode to set
     */
//    public void setReviewStatusCode(Integer reviewStatusCode) {
//        this.reviewStatusCode = reviewStatusCode;
//        setReviewStatus(reviewStatus);
//    }

    /**
     * @return the reviewStatus
     */
//    public String getReviewStatus() {
//        return reviewStatus;
//    }

    /**
     * @param reviewStatus the reviewStatus to set
     */
//    public void setReviewStatus(String reviewStatus) {
//       if (this.reviewStatusCode != null) {
//            if (reviewStatusCode.intValue() == 1) {
//                this.reviewStatus = "Recommend for Approval";
//            } else if (reviewStatusCode.intValue() == 2) {
//                this.reviewStatus = "Recommend for Disapproval";
//            } else if (reviewStatusCode.intValue() == 3) {
//                this.reviewStatus = "Recommend review by COI committee";
//            }
//        }
//    }

    /**
     * @return the coiDiscDetailsNumber
     */
    public Integer getCoiDiscDetailsNumber() {
        return coiDiscDetailsNumber;
    }

    /**
     * @param coiDiscDetailsNumber the coiDiscDetailsNumber to set
     */
    public void setCoiDiscDetailsNumber(Integer coiDiscDetailsNumber) {
        this.coiDiscDetailsNumber = coiDiscDetailsNumber;
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
     * @return the pjctName
     */
    public String getPjctName() {
        return pjctName;
    }

    /**
     * @param pjctName the pjctName to set
     */
    public void setPjctName(String pjctName) {
        this.pjctName = pjctName;
    }

    /**
     * @return the entityNumber
     */
    public String getEntityNumber() {
        return entityNumber;
    }

    /**
     * @param entityNumber the entityNumber to set
     */
    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }
    
    public Integer getIsFEFlag() {
        return isFEFlag;
    }

    public void setIsFEFlag(Integer isFEFlag) {
        this.isFEFlag = isFEFlag;
    }

}
