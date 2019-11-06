/*
 * FeedBatchListBean.java
 *
 * Created on January 6, 2005, 4:05 PM
 */

package edu.mit.coeus.centraladmin.bean;

import edu.mit.coeus.award.bean.SapFeedDetailsBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 *
 * @author  surekhan
 */
public class FeedBatchListBean implements CoeusBean, IBaseDataBean,java.io.Serializable {
    private int feedId;
    private int batchId;
    private String sapTransaction;
    private String projectType;
    private String wbsType;
    private String accountLevel;
    private String mitSapAccount;
    private String deptNo;
    private String billingElement; 
    private String billingType;
    private String billingForm;
    private String sponsorCode;
    private String primarySponsor;
    private String contract;
    private String customer;
    private String termCode;
    private String parentAccount;
    private String acctName;
    private String effectDate;
    private String expiration;    
    private String subPlan;
    private String mailCode;
    private String supervisor;
    private String superRoom;
    private String addressee;
    private String addrRoom;
    private String agreeType;
    private String authTotal;
    private String costShare;
    private String fundClass;
    private String poolCode;
    private String pendingCode;
    private String fsCode;
    private String dfafs;
    private String calcCode;
    private String cfdaNum;
    private String costingSheetKey;
    private String labAllocationKey;
    private String ebAdjustmentKey;
    private String ohAdjustmentkey;
    private String comment1;
    private String comment2;
    private String comment3;
    private String title;
    private int sortId;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    private int sequenceNumber;
    
    
    
    
    /** Creates a new instance of FeedBatchListBean */
    public FeedBatchListBean() {
    }
    
    /**
     * Getter for property sapTransaction.
     * @return Value of property sapTransaction.
     */
    public java.lang.String getSapTransaction() {
        return sapTransaction;
    }    
   
    /**
     * Setter for property sapTransaction.
     * @param sapTransaction New value of property sapTransaction.
     */
    public void setSapTransaction(java.lang.String sapTransaction) {
        this.sapTransaction = sapTransaction;
    }    
    
    /**
     * Getter for property wbsType.
     * @return Value of property wbsType.
     */
    public java.lang.String getWbsType() {
        return wbsType;
    }
    
    /**
     * Setter for property wbsType.
     * @param wbsType New value of property wbsType.
     */
    public void setWbsType(java.lang.String wbsType) {
        this.wbsType = wbsType;
    }
    
    /**
     * Getter for property accountLevel.
     * @return Value of property accountLevel.
     */
    public java.lang.String getAccountLevel() {
        return accountLevel;
    }
    
    /**
     * Setter for property accountLevel.
     * @param accountLevel New value of property accountLevel.
     */
    public void setAccountLevel(java.lang.String accountLevel) {
        this.accountLevel = accountLevel;
    }
    
    /**
     * Getter for property mitSapAccount.
     * @return Value of property mitSapAccount.
     */
    public java.lang.String getMitSapAccount() {
        return mitSapAccount;
    }
    
    /**
     * Setter for property mitSapAccount.
     * @param mitSapAccount New value of property mitSapAccount.
     */
    public void setMitSapAccount(java.lang.String mitSapAccount) {
        this.mitSapAccount = mitSapAccount;
    }
    
    /**
     * Getter for property deptNo.
     * @return Value of property deptNo.
     */
    public java.lang.String getDeptNo() {
        return deptNo;
    }
    
    /**
     * Setter for property deptNo.
     * @param deptNo New value of property deptNo.
     */
    public void setDeptNo(java.lang.String deptNo) {
        this.deptNo = deptNo;
    }
    
    /**
     * Getter for property billingElement.
     * @return Value of property billingElement.
     */
    public java.lang.String getBillingElement() {
        return billingElement;
    }
    
    /**
     * Setter for property billingElement.
     * @param billingElement New value of property billingElement.
     */
    public void setBillingElement(java.lang.String billingElement) {
        this.billingElement = billingElement;
    }
    
    /**
     * Getter for property billingType.
     * @return Value of property billingType.
     */
    public java.lang.String getBillingType() {
        return billingType;
    }
    
    /**
     * Setter for property billingType.
     * @param billingType New value of property billingType.
     */
    public void setBillingType(java.lang.String billingType) {
        this.billingType = billingType;
    }
    
    /**
     * Getter for property billingForm.
     * @return Value of property billingForm.
     */
    public java.lang.String getBillingForm() {
        return billingForm;
    }
    
    /**
     * Setter for property billingForm.
     * @param billingForm New value of property billingForm.
     */
    public void setBillingForm(java.lang.String billingForm) {
        this.billingForm = billingForm;
    }
    
    /**
     * Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /**
     * Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /**
     * Getter for property primarySponsor.
     * @return Value of property primarySponsor.
     */
    public java.lang.String getPrimarySponsor() {
        return primarySponsor;
    }
    
    /**
     * Setter for property primarySponsor.
     * @param primarySponsor New value of property primarySponsor.
     */
    public void setPrimarySponsor(java.lang.String primarySponsor) {
        this.primarySponsor = primarySponsor;
    }
    
    /**
     * Getter for property contract.
     * @return Value of property contract.
     */
    public java.lang.String getContract() {
        return contract;
    }
    
    /**
     * Setter for property contract.
     * @param contract New value of property contract.
     */
    public void setContract(java.lang.String contract) {
        this.contract = contract;
    }
    
    /**
     * Getter for property customer.
     * @return Value of property customer.
     */
    public java.lang.String getCustomer() {
        return customer;
    }
    
    /**
     * Setter for property customer.
     * @param customer New value of property customer.
     */
    public void setCustomer(java.lang.String customer) {
        this.customer = customer;
    }
    
    /**
     * Getter for property termCode.
     * @return Value of property termCode.
     */
    public java.lang.String getTermCode() {
        return termCode;
    }
    
    /**
     * Setter for property termCode.
     * @param termCode New value of property termCode.
     */
    public void setTermCode(java.lang.String termCode) {
        this.termCode = termCode;
    }
    
    /**
     * Getter for property parentAccount.
     * @return Value of property parentAccount.
     */
    public java.lang.String getParentAccount() {
        return parentAccount;
    }
    
    /**
     * Setter for property parentAccount.
     * @param parentAccount New value of property parentAccount.
     */
    public void setParentAccount(java.lang.String parentAccount) {
        this.parentAccount = parentAccount;
    }
    
    /**
     * Getter for property acctName.
     * @return Value of property acctName.
     */
    public java.lang.String getAcctName() {
        return acctName;
    }
    
    /**
     * Setter for property acctName.
     * @param acctName New value of property acctName.
     */
    public void setAcctName(java.lang.String acctName) {
        this.acctName = acctName;
    }
    
    /**
     * Getter for property effectDate.
     * @return Value of property effectDate.
     */
    public java.lang.String getEffectDate() {
        return effectDate;
    }
    
    /**
     * Setter for property effectDate.
     * @param effectDate New value of property effectDate.
     */
    public void setEffectDate(java.lang.String effectDate) {
        this.effectDate = effectDate;
    }
    
    /**
     * Getter for property expiration.
     * @return Value of property expiration.
     */
    public java.lang.String getExpiration() {
        return expiration;
    }
    
    /**
     * Setter for property expiration.
     * @param expiration New value of property expiration.
     */
    public void setExpiration(java.lang.String expiration) {
        this.expiration = expiration;
    }
    
    /**
     * Getter for property subPlan.
     * @return Value of property subPlan.
     */
    public java.lang.String getSubPlan() {
        return subPlan;
    }
    
    /**
     * Setter for property subPlan.
     * @param subPlan New value of property subPlan.
     */
    public void setSubPlan(java.lang.String subPlan) {
        this.subPlan = subPlan;
    }
    
    /**
     * Getter for property mailCode.
     * @return Value of property mailCode.
     */
    public java.lang.String getMailCode() {
        return mailCode;
    }
    
    /**
     * Setter for property mailCode.
     * @param mailCode New value of property mailCode.
     */
    public void setMailCode(java.lang.String mailCode) {
        this.mailCode = mailCode;
    }
    
    /**
     * Getter for property supervisor.
     * @return Value of property supervisor.
     */
    public java.lang.String getSupervisor() {
        return supervisor;
    }
    
    /**
     * Setter for property supervisor.
     * @param supervisor New value of property supervisor.
     */
    public void setSupervisor(java.lang.String supervisor) {
        this.supervisor = supervisor;
    }
    
    /**
     * Getter for property superRoom.
     * @return Value of property superRoom.
     */
    public java.lang.String getSuperRoom() {
        return superRoom;
    }
    
    /**
     * Setter for property superRoom.
     * @param superRoom New value of property superRoom.
     */
    public void setSuperRoom(java.lang.String superRoom) {
        this.superRoom = superRoom;
    }
    
    /**
     * Getter for property addressee.
     * @return Value of property addressee.
     */
    public java.lang.String getAddressee() {
        return addressee;
    }
    
    /**
     * Setter for property addressee.
     * @param addressee New value of property addressee.
     */
    public void setAddressee(java.lang.String addressee) {
        this.addressee = addressee;
    }
    
    /**
     * Getter for property addrRoom.
     * @return Value of property addrRoom.
     */
    public java.lang.String getAddrRoom() {
        return addrRoom;
    }
    
    /**
     * Setter for property addrRoom.
     * @param addrRoom New value of property addrRoom.
     */
    public void setAddrRoom(java.lang.String addrRoom) {
        this.addrRoom = addrRoom;
    }
    
    /**
     * Getter for property agreeType.
     * @return Value of property agreeType.
     */
    public java.lang.String getAgreeType() {
        return agreeType;
    }
    
    /**
     * Setter for property agreeType.
     * @param agreeType New value of property agreeType.
     */
    public void setAgreeType(java.lang.String agreeType) {
        this.agreeType = agreeType;
    }
    
    /**
     * Getter for property authTotal.
     * @return Value of property authTotal.
     */
    public java.lang.String getAuthTotal() {
        return authTotal;
    }
    
    /**
     * Setter for property authTotal.
     * @param authTotal New value of property authTotal.
     */
    public void setAuthTotal(java.lang.String authTotal) {
        this.authTotal = authTotal;
    }
    
    /**
     * Getter for property costShare.
     * @return Value of property costShare.
     */
    public java.lang.String getCostShare() {
        return costShare;
    }
    
    /**
     * Setter for property costShare.
     * @param costShare New value of property costShare.
     */
    public void setCostShare(java.lang.String costShare) {
        this.costShare = costShare;
    }
    
    /**
     * Getter for property fundClass.
     * @return Value of property fundClass.
     */
    public java.lang.String getFundClass() {
        return fundClass;
    }
    
    /**
     * Setter for property fundClass.
     * @param fundClass New value of property fundClass.
     */
    public void setFundClass(java.lang.String fundClass) {
        this.fundClass = fundClass;
    }
    
    /**
     * Getter for property poolCode.
     * @return Value of property poolCode.
     */
    public java.lang.String getPoolCode() {
        return poolCode;
    }
    
    /**
     * Setter for property poolCode.
     * @param poolCode New value of property poolCode.
     */
    public void setPoolCode(java.lang.String poolCode) {
        this.poolCode = poolCode;
    }
    
    /**
     * Getter for property pendingCode.
     * @return Value of property pendingCode.
     */
    public java.lang.String getPendingCode() {
        return pendingCode;
    }
    
    /**
     * Setter for property pendingCode.
     * @param pendingCode New value of property pendingCode.
     */
    public void setPendingCode(java.lang.String pendingCode) {
        this.pendingCode = pendingCode;
    }
    
    /**
     * Getter for property fsCode.
     * @return Value of property fsCode.
     */
    public java.lang.String getFsCode() {
        return fsCode;
    }
    
    /**
     * Setter for property fsCode.
     * @param fsCode New value of property fsCode.
     */
    public void setFsCode(java.lang.String fsCode) {
        this.fsCode = fsCode;
    }
    
    /**
     * Getter for property dfafs.
     * @return Value of property dfafs.
     */
    public java.lang.String getDfafs() {
        return dfafs;
    }
    
    /**
     * Setter for property dfafs.
     * @param dfafs New value of property dfafs.
     */
    public void setDfafs(java.lang.String dfafs) {
        this.dfafs = dfafs;
    }
    
    /**
     * Getter for property calcCode.
     * @return Value of property calcCode.
     */
    public java.lang.String getCalcCode() {
        return calcCode;
    }
    
    /**
     * Setter for property calcCode.
     * @param calcCode New value of property calcCode.
     */
    public void setCalcCode(java.lang.String calcCode) {
        this.calcCode = calcCode;
    }
    
    /**
     * Getter for property cfdaNum.
     * @return Value of property cfdaNum.
     */
    public java.lang.String getCfdaNum() {
        return cfdaNum;
    }
    
    /**
     * Setter for property cfdaNum.
     * @param cfdaNum New value of property cfdaNum.
     */
    public void setCfdaNum(java.lang.String cfdaNum) {
        this.cfdaNum = cfdaNum;
    }
    
    /**
     * Getter for property costingSheetKey.
     * @return Value of property costingSheetKey.
     */
    public java.lang.String getCostingSheetKey() {
        return costingSheetKey;
    }
    
    /**
     * Setter for property costingSheetKey.
     * @param costingSheetKey New value of property costingSheetKey.
     */
    public void setCostingSheetKey(java.lang.String costingSheetKey) {
        this.costingSheetKey = costingSheetKey;
    }
    
    /**
     * Getter for property labAllocationKey.
     * @return Value of property labAllocationKey.
     */
    public java.lang.String getLabAllocationKey() {
        return labAllocationKey;
    }
    
    /**
     * Setter for property labAllocationKey.
     * @param labAllocationKey New value of property labAllocationKey.
     */
    public void setLabAllocationKey(java.lang.String labAllocationKey) {
        this.labAllocationKey = labAllocationKey;
    }
    
    /**
     * Getter for property ebAdjustmentKey.
     * @return Value of property ebAdjustmentKey.
     */
    public java.lang.String getEbAdjustmentKey() {
        return ebAdjustmentKey;
    }
    
    /**
     * Setter for property ebAdjustmentKey.
     * @param ebAdjustmentKey New value of property ebAdjustmentKey.
     */
    public void setEbAdjustmentKey(java.lang.String ebAdjustmentKey) {
        this.ebAdjustmentKey = ebAdjustmentKey;
    }
    
    /**
     * Getter for property ohAdjustmentkey.
     * @return Value of property ohAdjustmentkey.
     */
    public java.lang.String getOhAdjustmentkey() {
        return ohAdjustmentkey;
    }
    
    /**
     * Setter for property ohAdjustmentkey.
     * @param ohAdjustmentkey New value of property ohAdjustmentkey.
     */
    public void setOhAdjustmentkey(java.lang.String ohAdjustmentkey) {
        this.ohAdjustmentkey = ohAdjustmentkey;
    }
    
    /**
     * Getter for property comment1.
     * @return Value of property comment1.
     */
    public java.lang.String getComment1() {
        return comment1;
    }
    
    /**
     * Setter for property comment1.
     * @param comment1 New value of property comment1.
     */
    public void setComment1(java.lang.String comment1) {
        this.comment1 = comment1;
    }
    
    /**
     * Getter for property comment2.
     * @return Value of property comment2.
     */
    public java.lang.String getComment2() {
        return comment2;
    }
    
    /**
     * Setter for property comment2.
     * @param comment2 New value of property comment2.
     */
    public void setComment2(java.lang.String comment2) {
        this.comment2 = comment2;
    }
    
    /**
     * Getter for property comment3.
     * @return Value of property comment3.
     */
    public java.lang.String getComment3() {
        return comment3;
    }
    
    /**
     * Setter for property comment3.
     * @param comment3 New value of property comment3.
     */
    public void setComment3(java.lang.String comment3) {
        this.comment3 = comment3;
    }
    
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /**
     * Getter for property sortId.
     * @return Value of property sortId.
     */
    public int getSortId() {
        return sortId;
    }
    
    /**
     * Setter for property sortId.
     * @param sortId New value of property sortId.
     */
    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
    
    /**
     * Getter for property feedId.
     * @return Value of property feedId.
     */
    public int getFeedId() {
        return feedId;
    }
    
    /**
     * Setter for property feedId.
     * @param feedId New value of property feedId.
     */
    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }
    
    /**
     * Getter for property batchId.
     * @return Value of property batchId.
     */
    public int getBatchId() {
        return batchId;
    }
    
    /**
     * Setter for property batchId.
     * @param batchId New value of property batchId.
     */
    public void setBatchId(int batchId) {
        this.batchId = batchId;
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
    
    public String getAcType() {
        return acType;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public boolean isLike(ComparableBean comparableBean){
        return true;
    }
    
    /**
     * Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Getter for property projectType.
     * @return Value of property projectType.
     */
    public java.lang.String getProjectType() {
        return projectType;
    }
    
    /**
     * Setter for property projectType.
     * @param projectType New value of property projectType.
     */
    public void setProjectType(java.lang.String projectType) {
        this.projectType = projectType;
    }
    
}
