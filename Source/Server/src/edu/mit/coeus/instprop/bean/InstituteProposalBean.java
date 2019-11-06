/*
 * @(#)InstituteProposalBean.java 1.0 01/19/04 2:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and
 * variables on 24-SEPT-2007 by Divya
 */

package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.organization.bean.*;
import edu.mit.coeus.rolodexmaint.bean.*;
import java.beans.*;
//import java.util.Vector;
import java.sql.Timestamp;
import java.sql.Date;
//import java.util.Hashtable;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.CoeusVector;

/**
 * The class used to hold the information of <code>Institute Proposal Details</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 19, 2004, 2:58 PM
 */

public class InstituteProposalBean extends InstituteProposalBaseBean implements java.io.Serializable {

    //holds the proposal number
    //private String proposalNumber;
    //holds the sponsor proposal number
    private String sponsorProposalNumber;
    //holds sequence number
    //private int sequenceNumber;    
    //holds the proposal type code    
    private int proposalTypeCode ;
    //holds the proposal type Description   
    private String proposalTypeDescription ;    
    //holds current account number
    private String currentAccountNumber;
    //holds the title
    private String title;
    //holds Sponsor Code
    private String sponsorCode = "";
    //holds sponsor name
    private String sponsorName = "";
    //holds rolodexId
    private int rolodexId;
    //holds rolodexId
    private int noticeOfOpportunityCode;
    //holds noticeOfOpportunityDescription
    private String noticeOfOpportunityDescription;
    //holds gradStudHeadCount
    private int gradStudHeadCount;
    //holds gradStudPersonMonths
    private double gradStudPersonMonths;
    //holds typeOfAccount
    private String typeOfAccount;
    //holds the activity type code
    private int proposalActivityTypeCode;
    //holds activity type description
    private String proposalActivityTypeDescription;
    //holds the request start date initial
    private Date requestStartDateInitial;
    //holds the request start date total
    private Date requestStartDateTotal;
    //holds the request end date initial
    private Date requestEndDateInitial;
    //holds the request end date total
    private Date requestEndDateTotal;
    //holds totalDirectCostInitial
    private double totalDirectCostInitial;
    //holds totalDirectCostTotal
    private double totalDirectCostTotal;
    //holds totalInDirectCostInitial
    private double totalInDirectCostInitial;
    //holds totalInDirectCostTotal
    private double totalInDirectCostTotal;
    //holds numberOfCopies
    private String numberOfCopies;
    //holds the deadLineDate
    private Date deadLineDate;    
    //holds the deadLineType
    //private String deadLineType;
    private char deadLineType;
    //holds the mail by
    //private String mailBy;
    private char mailBy;
    //holds the mail type
    //private String mailType;
    private char mailType;
    //holds the mail account number
    private String mailAccountNumber;
    //holds the sub contract flag
    private boolean subcontractFlag;    
    //holds cost sharing indicator
    private String costSharingIndicator;
    //holds cost sharing indicator
    private String idcRateIndicator;
    //holds specialReviewIndicator
    private String specialReviewIndicator;
    // 3823: Key person record needed in IP and Award
    private String keyPersonIndicator;
    //holds the status code
    private int statusCode;
    //holds status description
    private String statusDescription;
    //holds scienceCodeIndicator
    private String scienceCodeIndicator;    
    //holds nsfCode
    private String nsfCode;    
    //holds nsfcode description
    private String nsfCodeDescription;
    //holds nsfCode
    private String primeSponsorCode;
    //holds prime sponsor name
    private String primeSponsorName;    
    //holds create user
    private String createUser;
    //holds create timestamp
    private Timestamp createTimeStamp;
    //holds update user id
    //private String updateUser;
    //holds update timestamp
    //private Timestamp updateTimestamp;
    //holds account type
    //private String acType;
    //holds initialContractAdmin
    private String initialContractAdmin;
    //holds initialContractAdminName
    private String initialContractAdminName;
    //holds ipReviewRequestTypeCode
    private int ipReviewRequestTypeCode;
    //holds reviewSubmissionDate
    private Date reviewSubmissionDate;
    //holds reviewReceiveDate
    private Date reviewReceiveDate;    
    //holds reviewResultCode
    private int reviewResultCode;    
    //holds ipReviewer
    private String ipReviewer;
    //holds ipReviewerName
    private String ipReviewerName;
    //holds ipReviewActivityIndicator
    private String ipReviewActivityIndicator;
    //holds current award number
    private String currentAwardNumber;
    //holds mailing address of Rolodex
    private String mailingAddress;
    //holds name of Rolodex
    private String rolodexName;
    //holds Investigators
    private CoeusVector investigators;   
    //holds IDC Rates
    //private CoeusVector idcRates;
    //holds mode
    private char mode;
    // enhacement to have cfda number.Case # 2097
    private String cfdaNumber;
    //enhancement to have opprtunity.Case # 2097
    private String opportunity;
    
    // Added for Case 2162  - adding Award Type - Start 
    private int awardTypeCode = 0;
    private String AwardTypeDesc;
    // Added for Case 2162  - adding Award Type - End
    
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    private CoeusVector mergedProposalData;
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    
    public InstituteProposalBean(){
    }    
    
    /** Getter for property sponsorProposalNumber.
     * @return Value of property sponsorProposalNumber.
     */
    public java.lang.String getSponsorProposalNumber() {
        return sponsorProposalNumber;
    }
    
    /** Setter for property sponsorProposalNumber.
     * @param sponsorProposalNumber New value of property sponsorProposalNumber.
     */
    public void setSponsorProposalNumber(java.lang.String sponsorProposalNumber) {
        this.sponsorProposalNumber = sponsorProposalNumber;
    }    
    
    /** Getter for property proposalTypeCode.
     * @return Value of property proposalTypeCode.
     */
    public int getProposalTypeCode() {
        return proposalTypeCode;
    }
    
    /** Setter for property proposalTypeCode.
     * @param proposalTypeCode New value of property proposalTypeCode.
     */
    public void setProposalTypeCode(int proposalTypeCode) {
        this.proposalTypeCode = proposalTypeCode;
    }
    
    /** Getter for property currentAccountNumber.
     * @return Value of property currentAccountNumber.
     */
    public java.lang.String getCurrentAccountNumber() {
        return currentAccountNumber;
    }
    
    /** Setter for property currentAccountNumber.
     * @param currentAccountNumber New value of property currentAccountNumber.
     */
    public void setCurrentAccountNumber(java.lang.String currentAccountNumber) {
        this.currentAccountNumber = currentAccountNumber;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /** Getter for property rolodexId.
     * @return Value of property rolodexId.
     */
    public int getRolodexId() {
        return rolodexId;
    }
    
    /** Setter for property rolodexId.
     * @param rolodexId New value of property rolodexId.
     */
    public void setRolodexId(int rolodexId) {
        this.rolodexId = rolodexId;
    }
    
    /** Getter for property noticeOfOpportunityCode.
     * @return Value of property noticeOfOpportunityCode.
     */
    public int getNoticeOfOpportunityCode() {
        return noticeOfOpportunityCode;
    }
    
    /** Setter for property noticeOfOpportunityCode.
     * @param noticeOfOpportunityCode New value of property noticeOfOpportunityCode.
     */
    public void setNoticeOfOpportunityCode(int noticeOfOpportunityCode) {
        this.noticeOfOpportunityCode = noticeOfOpportunityCode;
    }
    
    /** Getter for property gradStudHeadCount.
     * @return Value of property gradStudHeadCount.
     */
    public int getGradStudHeadCount() {
        return gradStudHeadCount;
    }
    
    /** Setter for property gradStudHeadCount.
     * @param gradStudHeadCount New value of property gradStudHeadCount.
     */
    public void setGradStudHeadCount(int gradStudHeadCount) {
        this.gradStudHeadCount = gradStudHeadCount;
    }
    
    /** Getter for property gradStudPersonMonths.
     * @return Value of property gradStudPersonMonths.
     */
    public double getGradStudPersonMonths() {
        return gradStudPersonMonths;
    }
    
    /** Setter for property gradStudPersonMonths.
     * @param gradStudPersonMonths New value of property gradStudPersonMonths.
     */
    public void setGradStudPersonMonths(double gradStudPersonMonths) {
        this.gradStudPersonMonths = gradStudPersonMonths;
    }
    
    /** Getter for property typeOfAccount.
     * @return Value of property typeOfAccount.
     */
    public java.lang.String getTypeOfAccount() {
        return typeOfAccount;
    }
    
    /** Setter for property typeOfAccount.
     * @param typeOfAccount New value of property typeOfAccount.
     */
    public void setTypeOfAccount(java.lang.String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }
    
    /** Getter for property proposalActivityTypeCode.
     * @return Value of property proposalActivityTypeCode.
     */
    public int getProposalActivityTypeCode() {
        return proposalActivityTypeCode;
    }
    
    /** Setter for property proposalActivityTypeCode.
     * @param proposalActivityTypeCode New value of property proposalActivityTypeCode.
     */
    public void setProposalActivityTypeCode(int proposalActivityTypeCode) {
        this.proposalActivityTypeCode = proposalActivityTypeCode;
    }
    
    /** Getter for property requestStartDateInitial.
     * @return Value of property requestStartDateInitial.
     */
    public java.sql.Date getRequestStartDateInitial() {
        return requestStartDateInitial;
    }
    
    /** Setter for property requestStartDateInitial.
     * @param requestStartDateInitial New value of property requestStartDateInitial.
     */
    public void setRequestStartDateInitial(java.sql.Date requestStartDateInitial) {
        this.requestStartDateInitial = requestStartDateInitial;
    }
    
    /** Getter for property requestStartDateTotal.
     * @return Value of property requestStartDateTotal.
     */
    public java.sql.Date getRequestStartDateTotal() {
        return requestStartDateTotal;
    }
    
    /** Setter for property requestStartDateTotal.
     * @param requestStartDateTotal New value of property requestStartDateTotal.
     */
    public void setRequestStartDateTotal(java.sql.Date requestStartDateTotal) {
        this.requestStartDateTotal = requestStartDateTotal;
    }
    
    /** Getter for property requestEndDateInitial.
     * @return Value of property requestEndDateInitial.
     */
    public java.sql.Date getRequestEndDateInitial() {
        return requestEndDateInitial;
    }
    
    /** Setter for property requestEndDateInitial.
     * @param requestEndDateInitial New value of property requestEndDateInitial.
     */
    public void setRequestEndDateInitial(java.sql.Date requestEndDateInitial) {
        this.requestEndDateInitial = requestEndDateInitial;
    }
    
    /** Getter for property requestEndDateTotal.
     * @return Value of property requestEndDateTotal.
     */
    public java.sql.Date getRequestEndDateTotal() {
        return requestEndDateTotal;
    }
    
    /** Setter for property requestEndDateTotal.
     * @param requestEndDateTotal New value of property requestEndDateTotal.
     */
    public void setRequestEndDateTotal(java.sql.Date requestEndDateTotal) {
        this.requestEndDateTotal = requestEndDateTotal;
    }
    
    /** Getter for property totalDirectCostInitial.
     * @return Value of property totalDirectCostInitial.
     */
    public double getTotalDirectCostInitial() {
        return totalDirectCostInitial;
    }
    
    /** Setter for property totalDirectCostInitial.
     * @param totalDirectCostInitial New value of property totalDirectCostInitial.
     */
    public void setTotalDirectCostInitial(double totalDirectCostInitial) {
        this.totalDirectCostInitial = totalDirectCostInitial;
    }
    
    /** Getter for property totalDirectCostTotal.
     * @return Value of property totalDirectCostTotal.
     */
    public double getTotalDirectCostTotal() {
        return totalDirectCostTotal;
    }
    
    /** Setter for property totalDirectCostTotal.
     * @param totalDirectCostTotal New value of property totalDirectCostTotal.
     */
    public void setTotalDirectCostTotal(double totalDirectCostTotal) {
        this.totalDirectCostTotal = totalDirectCostTotal;
    }
    
    /** Getter for property totalInDirectCostInitial.
     * @return Value of property totalInDirectCostInitial.
     */
    public double getTotalInDirectCostInitial() {
        return totalInDirectCostInitial;
    }
    
    /** Setter for property totalInDirectCostInitial.
     * @param totalInDirectCostInitial New value of property totalInDirectCostInitial.
     */
    public void setTotalInDirectCostInitial(double totalInDirectCostInitial) {
        this.totalInDirectCostInitial = totalInDirectCostInitial;
    }
    
    /** Getter for property totalInDirectCostTotal.
     * @return Value of property totalInDirectCostTotal.
     */
    public double getTotalInDirectCostTotal() {
        return totalInDirectCostTotal;
    }
    
    /** Setter for property totalInDirectCostTotal.
     * @param totalInDirectCostTotal New value of property totalInDirectCostTotal.
     */
    public void setTotalInDirectCostTotal(double totalInDirectCostTotal) {
        this.totalInDirectCostTotal = totalInDirectCostTotal;
    }
    
    /** Getter for property numberOfCopies.
     * @return Value of property numberOfCopies.
     */
    public java.lang.String getNumberOfCopies() {
        return numberOfCopies;
    }
    
    /** Setter for property numberOfCopies.
     * @param numberOfCopies New value of property numberOfCopies.
     */
    public void setNumberOfCopies(java.lang.String numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }
    
    /** Getter for property deadLineDate.
     * @return Value of property deadLineDate.
     */
    public java.sql.Date getDeadLineDate() {
        return deadLineDate;
    }
    
    /** Setter for property deadLineDate.
     * @param deadLineDate New value of property deadLineDate.
     */
    public void setDeadLineDate(java.sql.Date deadLineDate) {
        this.deadLineDate = deadLineDate;
    }
    
    /** Getter for property deadLineType.
     * @return Value of property deadLineType.
     */
    public char getDeadLineType() {
        return deadLineType;
    }
    
    /** Setter for property deadLineType.
     * @param deadLineType New value of property deadLineType.
     */
    public void setDeadLineType(char deadLineType) {
        this.deadLineType = deadLineType;
    }
    
    /** Getter for property mailBy.
     * @return Value of property mailBy.
     */
    public char getMailBy() {
        return mailBy;
    }
    
    /** Setter for property mailBy.
     * @param mailBy New value of property mailBy.
     */
    public void setMailBy(char mailBy) {
        this.mailBy = mailBy;
    }
    
    /** Getter for property mailType.
     * @return Value of property mailType.
     */
    public char getMailType() {
        return mailType;
    }
    
    /** Setter for property mailType.
     * @param mailType New value of property mailType.
     */
    public void setMailType(char mailType) {
        this.mailType = mailType;
    }
    
    /** Getter for property mailAccountNumber.
     * @return Value of property mailAccountNumber.
     */
    public java.lang.String getMailAccountNumber() {
        return mailAccountNumber;
    }
    
    /** Setter for property mailAccountNumber.
     * @param mailAccountNumber New value of property mailAccountNumber.
     */
    public void setMailAccountNumber(java.lang.String mailAccountNumber) {
        this.mailAccountNumber = mailAccountNumber;
    }
    
    /** Getter for property subcontractFlag.
     * @return Value of property subcontractFlag.
     */
    public boolean isSubcontractFlag() {
        return subcontractFlag;
    }
    
    /** Setter for property subcontractFlag.
     * @param subcontractFlag New value of property subcontractFlag.
     */
    public void setSubcontractFlag(boolean subcontractFlag) {
        this.subcontractFlag = subcontractFlag;
    }
    
    /** Getter for property costSharingIndicator.
     * @return Value of property costSharingIndicator.
     */
    public java.lang.String getCostSharingIndicator() {
        return costSharingIndicator;
    }
    
    /** Setter for property costSharingIndicator.
     * @param costSharingIndicator New value of property costSharingIndicator.
     */
    public void setCostSharingIndicator(java.lang.String costSharingIndicator) {
        this.costSharingIndicator = costSharingIndicator;
    }
    
    /** Getter for property idcRateIndicator.
     * @return Value of property idcRateIndicator.
     */
    public java.lang.String getIdcRateIndicator() {
        return idcRateIndicator;
    }
    
    /** Setter for property idcRateIndicator.
     * @param idcRateIndicator New value of property idcRateIndicator.
     */
    public void setIdcRateIndicator(java.lang.String idcRateIndicator) {
        this.idcRateIndicator = idcRateIndicator;
    }
    
    /** Getter for property specialReviewIndicator.
     * @return Value of property specialReviewIndicator.
     */
    public java.lang.String getSpecialReviewIndicator() {
        return specialReviewIndicator;
    }
    
    /** Setter for property specialReviewIndicator.
     * @param specialReviewIndicator New value of property specialReviewIndicator.
     */
    public void setSpecialReviewIndicator(java.lang.String specialReviewIndicator) {
        this.specialReviewIndicator = specialReviewIndicator;
    }
    
    /** Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /** Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /** Getter for property scienceCodeIndicator.
     * @return Value of property scienceCodeIndicator.
     */
    public java.lang.String getScienceCodeIndicator() {
        return scienceCodeIndicator;
    }
    
    /** Setter for property scienceCodeIndicator.
     * @param scienceCodeIndicator New value of property scienceCodeIndicator.
     */
    public void setScienceCodeIndicator(java.lang.String scienceCodeIndicator) {
        this.scienceCodeIndicator = scienceCodeIndicator;
    }
    
    /** Getter for property nsfCode.
     * @return Value of property nsfCode.
     */
    public java.lang.String getNsfCode() {
        return nsfCode;
    }
    
    /** Setter for property nsfCode.
     * @param nsfCode New value of property nsfCode.
     */
    public void setNsfCode(java.lang.String nsfCode) {
        this.nsfCode = nsfCode;
    }
    
    /** Getter for property primeSponsorCode.
     * @return Value of property primeSponsorCode.
     */
    public java.lang.String getPrimeSponsorCode() {
        return primeSponsorCode;
    }
    
    /** Setter for property primeSponsorCode.
     * @param primeSponsorCode New value of property primeSponsorCode.
     */
    public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
        this.primeSponsorCode = primeSponsorCode;
    }
    
    /** Getter for property createUser.
     * @return Value of property createUser.
     */
    public java.lang.String getCreateUser() {
        return createUser;
    }
    
    /** Setter for property createUser.
     * @param createUser New value of property createUser.
     */
    public void setCreateUser(java.lang.String createUser) {
        this.createUser = createUser;
    }
    
    /** Getter for property createTimeStamp.
     * @return Value of property createTimeStamp.
     */
    public java.sql.Timestamp getCreateTimeStamp() {
        return createTimeStamp;
    }
    
    /** Setter for property createTimeStamp.
     * @param createTimeStamp New value of property createTimeStamp.
     */
    public void setCreateTimeStamp(java.sql.Timestamp createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }
    
    /** Getter for property initialContractAdmin.
     * @return Value of property initialContractAdmin.
     */
    public java.lang.String getInitialContractAdmin() {
        return initialContractAdmin;
    }
    
    /** Setter for property initialContractAdmin.
     * @param initialContractAdmin New value of property initialContractAdmin.
     */
    public void setInitialContractAdmin(java.lang.String initialContractAdmin) {
        this.initialContractAdmin = initialContractAdmin;
    }
    
    /** Getter for property ipReviewRequestTypeCode.
     * @return Value of property ipReviewRequestTypeCode.
     */
    public int getIpReviewRequestTypeCode() {
        return ipReviewRequestTypeCode;
    }
    
    /** Setter for property ipReviewRequestTypeCode.
     * @param ipReviewRequestTypeCode New value of property ipReviewRequestTypeCode.
     */
    public void setIpReviewRequestTypeCode(int ipReviewRequestTypeCode) {
        this.ipReviewRequestTypeCode = ipReviewRequestTypeCode;
    }
    
    /** Getter for property reviewSubmissionDate.
     * @return Value of property reviewSubmissionDate.
     */
    public java.sql.Date getReviewSubmissionDate() {
        return reviewSubmissionDate;
    }
    
    /** Setter for property reviewSubmissionDate.
     * @param reviewSubmissionDate New value of property reviewSubmissionDate.
     */
    public void setReviewSubmissionDate(java.sql.Date reviewSubmissionDate) {
        this.reviewSubmissionDate = reviewSubmissionDate;
    }
    
    /** Getter for property reviewReceiveDate.
     * @return Value of property reviewReceiveDate.
     */
    public java.sql.Date getReviewReceiveDate() {
        return reviewReceiveDate;
    }
    
    /** Setter for property reviewReceiveDate.
     * @param reviewReceiveDate New value of property reviewReceiveDate.
     */
    public void setReviewReceiveDate(java.sql.Date reviewReceiveDate) {
        this.reviewReceiveDate = reviewReceiveDate;
    }
    
    /** Getter for property reviewResultCode.
     * @return Value of property reviewResultCode.
     */
    public int getReviewResultCode() {
        return reviewResultCode;
    }
    
    /** Setter for property reviewResultCode.
     * @param reviewResultCode New value of property reviewResultCode.
     */
    public void setReviewResultCode(int reviewResultCode) {
        this.reviewResultCode = reviewResultCode;
    }
    
    /** Getter for property ipReviewer.
     * @return Value of property ipReviewer.
     */
    public java.lang.String getIpReviewer() {
        return ipReviewer;
    }
    
    /** Setter for property ipReviewer.
     * @param ipReviewer New value of property ipReviewer.
     */
    public void setIpReviewer(java.lang.String ipReviewer) {
        this.ipReviewer = ipReviewer;
    }
    
    /** Getter for property ipReviewActivityIndicator.
     * @return Value of property ipReviewActivityIndicator.
     */
    public java.lang.String getIpReviewActivityIndicator() {
        return ipReviewActivityIndicator;
    }
    
    /** Setter for property ipReviewActivityIndicator.
     * @param ipReviewActivityIndicator New value of property ipReviewActivityIndicator.
     */
    public void setIpReviewActivityIndicator(java.lang.String ipReviewActivityIndicator) {
        this.ipReviewActivityIndicator = ipReviewActivityIndicator;
    }
    
    /** Getter for property currentAwardNumber.
     * @return Value of property currentAwardNumber.
     */
    public java.lang.String getCurrentAwardNumber() {
        return currentAwardNumber;
    }
    
    /** Setter for property currentAwardNumber.
     * @param currentAwardNumber New value of property currentAwardNumber.
     */
    public void setCurrentAwardNumber(java.lang.String currentAwardNumber) {
        this.currentAwardNumber = currentAwardNumber;
    }    
    
    /** Getter for property investigators.
     * @return Value of property investigators.
     */
    public CoeusVector getInvestigators() {
        return investigators;
    }
    
    /** Setter for property investigators.
     * @param investigators New value of property investigators.
     */
    public void setInvestigators(CoeusVector investigators) {
        this.investigators = investigators;
    }
    
    /** Getter for property proposalTypeDescription.
     * @return Value of property proposalTypeDescription.
     */
    public java.lang.String getProposalTypeDescription() {
        return proposalTypeDescription;
    }
    
    /** Setter for property proposalTypeDescription.
     * @param proposalTypeDescription New value of property proposalTypeDescription.
     */
    public void setProposalTypeDescription(java.lang.String proposalTypeDescription) {
        this.proposalTypeDescription = proposalTypeDescription;
    }
    
    /** Getter for property statusDescription.
     * @return Value of property statusDescription.
     */
    public java.lang.String getStatusDescription() {
        return statusDescription;
    }
    
    /** Setter for property statusDescription.
     * @param statusDescription New value of property statusDescription.
     */
    public void setStatusDescription(java.lang.String statusDescription) {
        this.statusDescription = statusDescription;
    }
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /** Getter for property primeSponsorName.
     * @return Value of property primeSponsorName.
     */
    public java.lang.String getPrimeSponsorName() {
        return primeSponsorName;
    }
    
    /** Setter for property primeSponsorName.
     * @param primeSponsorName New value of property primeSponsorName.
     */
    public void setPrimeSponsorName(java.lang.String primeSponsorName) {
        this.primeSponsorName = primeSponsorName;
    }    
    
    /** Getter for property proposalActivityTypeDescription.
     * @return Value of property proposalActivityTypeDescription.
     */
    public java.lang.String getProposalActivityTypeDescription() {
        return proposalActivityTypeDescription;
    }
    
    /** Setter for property proposalActivityTypeDescription.
     * @param proposalActivityTypeDescription New value of property proposalActivityTypeDescription.
     */
    public void setProposalActivityTypeDescription(java.lang.String proposalActivityTypeDescription) {
        this.proposalActivityTypeDescription = proposalActivityTypeDescription;
    }
    
    /** Getter for property nsfCodeDescription.
     * @return Value of property nsfCodeDescription.
     */
    public java.lang.String getNsfCodeDescription() {
        return nsfCodeDescription;
    }
    
    /** Setter for property nsfCodeDescription.
     * @param nsfCodeDescription New value of property nsfCodeDescription.
     */
    public void setNsfCodeDescription(java.lang.String nsfCodeDescription) {
        this.nsfCodeDescription = nsfCodeDescription;
    }
    
    /** Getter for property noticeOfOpportunityDescription.
     * @return Value of property noticeOfOpportunityDescription.
     */
    public java.lang.String getNoticeOfOpportunityDescription() {
        return noticeOfOpportunityDescription;
    }
    
    /** Setter for property noticeOfOpportunityDescription.
     * @param noticeOfOpportunityDescription New value of property noticeOfOpportunityDescription.
     */
    public void setNoticeOfOpportunityDescription(java.lang.String noticeOfOpportunityDescription) {
        this.noticeOfOpportunityDescription = noticeOfOpportunityDescription;
    }    
    
    /** Getter for property mode.
     * @return Value of property mode.
     */
    public char getMode() {
        return mode;
    }
    
    /** Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(char mode) {
        this.mode = mode;
    }
    
    /** Getter for property mailingAddress.
     * @return Value of property mailingAddress.
     */
    public java.lang.String getMailingAddress() {
        return mailingAddress;
    }
    
    /** Setter for property mailingAddress.
     * @param mailingAddress New value of property mailingAddress.
     */
    public void setMailingAddress(java.lang.String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
    
    /** Getter for property rolodexName.
     * @return Value of property rolodexName.
     */
    public java.lang.String getRolodexName() {
        return rolodexName;
    }
    
    /** Setter for property rolodexName.
     * @param rolodexName New value of property rolodexName.
     */
    public void setRolodexName(java.lang.String rolodexName) {
        this.rolodexName = rolodexName;
    }
    
    /** Getter for property initialContractAdminName.
     * @return Value of property initialContractAdminName.
     */
    public java.lang.String getInitialContractAdminName() {
        return initialContractAdminName;
    }
    
    /** Setter for property initialContractAdminName.
     * @param initialContractAdminName New value of property initialContractAdminName.
     */
    public void setInitialContractAdminName(java.lang.String initialContractAdminName) {
        this.initialContractAdminName = initialContractAdminName;
    }
    
    /** Getter for property ipReviewerName.
     * @return Value of property ipReviewerName.
     */
    public java.lang.String getIpReviewerName() {
        return ipReviewerName;
    }
    
    /** Setter for property ipReviewerName.
     * @param ipReviewerName New value of property ipReviewerName.
     */
    public void setIpReviewerName(java.lang.String ipReviewerName) {
        this.ipReviewerName = ipReviewerName;
    }
    
    /**
     * Getter for property cfdaNumber.
     * @return Value of property cfdaNumber.
     */
    public java.lang.String getCfdaNumber() {
        return cfdaNumber;
    }
    
    /**
     * Setter for property cfdaNumber.
     * @param cfdaNumber New value of property cfdaNumber.
     */
    public void setCfdaNumber(java.lang.String cfdaNumber) {
        this.cfdaNumber = cfdaNumber;
    }
    
    /**
     * Getter for property opportunity.
     * @return Value of property opportunity.
     */
    public java.lang.String getOpportunity() {
        return opportunity;
    }
    
    /**
     * Setter for property opportunity.
     * @param opportunity New value of property opportunity.
     */
    public void setOpportunity(java.lang.String opportunity) {
        this.opportunity = opportunity;
    }
    
    // Added for Case 2162  - adding Award Type - Start 
    public int getAwardTypeCode() {
        return awardTypeCode;
    }

    public void setAwardTypeCode(int awardTypeCode) {
        this.awardTypeCode = awardTypeCode;
    }

    public String getAwardTypeDesc() {
        return AwardTypeDesc;
    }

    public void setAwardTypeDesc(String AwardTypeDesc) {
        this.AwardTypeDesc = AwardTypeDesc;
    } 
    // Added for Case 2162  - adding Award Type - End

    // 3823: Key person record needed in IP and Award - Start
    public String getKeyPersonIndicator() {
        return keyPersonIndicator;
    }

    public void setKeyPersonIndicator(String keyPersonIndicator) {
        this.keyPersonIndicator = keyPersonIndicator;
    }
    // 3823: Key person record needed in IP and Award - End

    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    /*Method to get merged proposals data
     *@return mergedProposalData
     */
    public CoeusVector getMergedProposalData() {
        return mergedProposalData;
    }
    
    /*Method to set merged proposals data
     *@param mergedProposalData
     */
    public void setMergedProposalData(CoeusVector mergedProposalData) {
        this.mergedProposalData = mergedProposalData;
    }
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
}