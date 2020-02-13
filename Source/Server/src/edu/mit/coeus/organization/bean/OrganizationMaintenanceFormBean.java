/**
 * @(#)OrganizationMaintenanceFormBean.java 1.0 8/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * This class used to set and gets the Organization Maintenance's form data.
 *
 * @version :1.0 August 13,2002
 * @author Guptha K
 */

public class OrganizationMaintenanceFormBean implements java.io.Serializable{

    private String organizationId;
    private String organizationName;
    private int contactAddressId;
    private String contactAddressName;
    private String address;
    private String cableAddress;
    private String telexNumber;
    private String county;
    private String congressionalDistrict;
    private String incorporatedIn;
    private String incorporatedDate;
    private int numberOfExmployees;
    private String irsTaxExcemption;
    private String federalEmployerID;
    private String massTaxExcemptNum;
    private String agencySymbol;
    private String vendorCode;
    private String comGovEntityCode;
    private String massEmployeeClaim;
    private String dunsNumber;
    private String dunsPlusFourNumber;
    private String dodacNumber;
    private String cageNumber;
    private String humanSubAssurance;
    private String animalWelfareAssurance;
    private String scienceMisconductComplDate;
    private String phsAcount;
    private String nsfInstitutionalCode;
    private String indirectCostRateAgreement;
    private int cognizantAuditor;
    private String cognizantAuditorName;
    private int onrResidentRep;
    private String onrResidentRepName;
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    private String refId;

	/**
	 *	Default Constructor
	 */

	public OrganizationMaintenanceFormBean() {
    }

	/**
	 *	This method is used to fetch the Organization Id
	 *	@return String organizationId
	 */

	public String getOrganizationId() {
        return organizationId;
    }

	/** 	This method is used to set the Organization Id
         * @param organizationId  string value
         */

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

	/**
	 *	This method is used to fetch the Organization Name
	 *	@return String organizationName
	 */

    public String getOrganizationName() {
        return organizationName;
    }

	/** 	This method is used to set the Organization Name
         * @param organizationName  string value
         */

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

	/**
	 *	This method is used to fetch the Contact Address Id
	 *	@return String contactAddressId
	 */

    public int getContactAddressId() {
        return contactAddressId;
    }

	/** 	This method is used to set the Contact Address Id
         * @param contactAddressId  int value
         */

    public void setContactAddressId(int contactAddressId) {
        this.contactAddressId = contactAddressId;
    }

	/**
	 *	This method is used to fetch the Contact Address Name
	 *	@return String contactAddressName
	 */

    public String getContactAddressName() {
        return contactAddressName;
    }


	/** 	This method is used to set the Contact Address Name
         * @param contactAddressName string value
         */

	public void setContactAddressName(String contactAddressName) {
        this.contactAddressName = contactAddressName;
    }

	/**
	 *	This method is used to fetch the Address
	 *	@return String address
	 */

    public String getAddress() {
        return address;
    }

	/** 	This method is used to set the Address
         * @param address string value  */

    public void setAddress(String address) {
        this.address = address;
    }

	/**
	 *	This method is used to fetch the Cable Address
	 *	@return String cableAddress
	 */

    public String getCableAddress() {
        return cableAddress;
    }

	/** 	This method is used to set the Cable Address
         * @param cableAddress  String value
         */
    public void setCableAddress(String cableAddress) {
        this.cableAddress = cableAddress;
    }

	/**
	 *	This method is used to fetch the Telex Number
	 *	@return String telexNumber
	 */

	public String getTelexNumber() {
        return telexNumber;
    }

	/** 	This method is used to set the Telex Number
         * @param telexNumber String value
         */

    public void setTelexNumber(String telexNumber) {
        this.telexNumber = telexNumber;
    }

	/**
	 *	This method is used to fetch the Country
	 *	@return String country
	 */

    public String getCounty() {
        return county;
    }

	/** 	This method is used to set the Country
         * @param county  String value
         */

    public void setCounty(String county) {
        this.county = county;
    }

	/**
	 *	This method is used to fetch the Congressional District
	 *	@return String congressionalDistrict
	 */

	public String getCongressionalDistrict() {
        return congressionalDistrict;
    }

	/** 	This method is used to set the Congressional District
         * @param congressionalDistrict  String value
         */

    public void setCongressionalDistrict(String congressionalDistrict) {
        this.congressionalDistrict = congressionalDistrict;
    }

	/**
	 *	This method is used to fetch Number of Employees
	 *	@return String numberOfEmployees
	 */

    public int getNumberOfExmployees() {
        return numberOfExmployees;
    }

	/** 	This method is used to set the Number of Employees
         * @param numberOfExmployees String value
         */

    public void setNumberOfExmployees(int numberOfExmployees) {
        this.numberOfExmployees = numberOfExmployees;
    }

	/**
	 *	This method is used to fetch Incorporated In detail
	 *	@return String incorporatedIn
	 */

    public String getIncorporatedIn() {
        return incorporatedIn;
    }

	/** 	This method is used to set the Incorporated In detail
         * @param incorporatedIn  String value
         */

    public void setIncorporatedIn(String incorporatedIn) {
        this.incorporatedIn = incorporatedIn;
    }

	/**
	 *	This method is used to fetch the Incorporated Date
	 *	@return String incorporatedDate
	 */

    public String getIncorporatedDate() {
        return incorporatedDate;
    }

	/** 	This method is used to set the Incorporated Date
         * @param incorporatedDate  String value
         */

    public void setIncorporatedDate(String incorporatedDate) {
        this.incorporatedDate = incorporatedDate;
    }

	/**
	 *	This method is used to fetch the IRS Tax Excemption
	 *	@return String irsTaxExemption
	 */

    public String getIrsTaxExcemption() {
        return irsTaxExcemption;
    }

	/**
	 *	This method is used to set the IRS Tax Excemption
	 *	@param irsTaxExcemption String 
	 */

    public void setIrsTaxExcemption(String irsTaxExcemption) {
        this.irsTaxExcemption = irsTaxExcemption;
    }

	/**
	 *	This method is used to fetch the Federal Employer ID
	 *	@return String federalEmployerId
	 */

    public String getFederalEmployerID() {
        return federalEmployerID;
    }

	/** 	This method is used to set the Federal Employer ID
         * @param federalEmployerID  string value
         */
    public void setFederalEmployerID(String federalEmployerID) {
        this.federalEmployerID = federalEmployerID;
    }

	/** 	This method is used to fetch the Mass Tax Exemption Number
         * @return  MassTaxExcemptNumber
         */

    public String getMassTaxExcemptNum() {
        return massTaxExcemptNum;
    }

	/** 	This method is used to set the Mass Tax Exemption Number
         * @param massTaxExcemptNum  string value */

    public void setMassTaxExcemptNum(String massTaxExcemptNum) {
        this.massTaxExcemptNum = massTaxExcemptNum;
    }

	/**
	 *	This method is used to fetch the Agency Symbol
	 *	@return String agencySymbol
	 */

    public String getAgencySymbol() {
        return agencySymbol;
    }

	/**
	 *	This method is used to set the Agency Symbol
	 *	@param agencySymbol String 
	 */

    public void setAgencySymbol(String agencySymbol) {
        this.agencySymbol = agencySymbol;
    }

	/**
	 *	This method is used to fetch the Vendor Code
	 *	@return String vendorCode
	 */

    public String getVendorCode() {
        return vendorCode;
    }

	/**
	 *	This method is used to set the Vendor Code
	 *	@param vendorCode String 
	 */

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

	/**
	 *	This method is used to fetch the Com Government Entity Code
	 *	@return String comGovEntityCode
	 */

    public String getComGovEntityCode() {
        return comGovEntityCode;
    }

	/**
	 *	This method is used to set the Com Government Entity Code
	 *	@param comGovEntityCode String 
	 */

    public void setComGovEntityCode(String comGovEntityCode) {
        this.comGovEntityCode = comGovEntityCode;
    }

	/**
	 *	This method is used to fetch the Mass Employee Claim
	 *	@return String massEmployeeClaim
	 */

    public String getMassEmployeeClaim() {
        return massEmployeeClaim;
    }

	/**
	 *	This method is used to set the Mass Employee Claim
	 *	@param String massEmployeeClaim
	 */

    public void setMassEmployeeClaim(String massEmployeeClaim) {
        this.massEmployeeClaim = massEmployeeClaim;
    }

	/**
	 *	This method is used to fetch the Duns Number
	 *	@return String dunsNumber
	 */

    public String getDunsNumber() {
        return dunsNumber;
    }

	/**
	 *	This method is used to set the Duns Number
	 *	@param dunsNumber String 
	 */

    public void setDunsNumber(String dunsNumber) {
        this.dunsNumber = dunsNumber;
    }

	/**
	 *	This method is used to fetch the Duns+4 Number
	 *	@return String dunsPlusFourNumber
	 */

    public String getDunsPlusFourNumber() {
        return dunsPlusFourNumber;
    }

	/**
	 *	This method is used to set the Duns+4 Number
	 *	@param dunsPlusFourNumber String 
	 */

    public void setDunsPlusFourNumber(String dunsPlusFourNumber) {
        this.dunsPlusFourNumber = dunsPlusFourNumber;
    }

	/**
	 *	This method is used to fetch the Dodac Number
	 *	@return String dodacNumber
	 */

    public String getDodacNumber() {
        return dodacNumber;
    }

	/**
	 *	This method is used to set the Dodac Number
	 *	@param dodacNumber String 
	 */

    public void setDodacNumber(String dodacNumber) {
        this.dodacNumber = dodacNumber;
    }

	/**
	 *	This method is used to fetch the Cage Number
	 *	@return String cageNumber
	 */

    public String getCageNumber() {
        return cageNumber;
    }

	/**
	 *	This method is used to set the Cage Number
	 *	@param cageNumber String 
	 */

    public void setCageNumber(String cageNumber) {
        this.cageNumber = cageNumber;
    }

	/**
	 *	This method is used to fetch the Human Sub Assurance
	 *	@return String humanSubAssurance
	 */

    public String getHumanSubAssurance() {
        return humanSubAssurance;
    }

	/**
	 *	This method is used to set the Human Sub Assurance
	 *	@param humanSubAssurance String 
	 */

    public void setHumanSubAssurance(String humanSubAssurance) {
        this.humanSubAssurance = humanSubAssurance;
    }

	/**
	 *	This method is used to fetch the Animal Welfare Assurance
	 *	@return String animalWelfareAssurance
	 */

    public String getAnimalWelfareAssurance() {
        return animalWelfareAssurance;
    }

	/**
	 *	This method is used to set the Animal Welfare Assurance
	 *	@param animalWelfareAssurance String 
	 */

    public void setAnimalWelfareAssurance(String animalWelfareAssurance) {
        this.animalWelfareAssurance = animalWelfareAssurance;
    }

	/**
	 *	This method is used to fetch the ScienceMisconductComplDate
	 *	@return String scienceMisconductComplDate
	 */

	public String getScienceMisconductComplDate() {
        return scienceMisconductComplDate;
    }

	/**
	 *	This method is used to set the ScienceMisconductComplDate
	 *	@param scienceMisconductComplDate String 
	 */

	public void setScienceMisconductComplDate(String scienceMisconductComplDate) {
        this.scienceMisconductComplDate = scienceMisconductComplDate;
    }

	/**
	 *	This method is used to fetch the PHS Account
	 *	@return String phsAccount
	 */

    public String getPhsAcount() {
        return phsAcount;
    }

	/**
	 *	This method is used to set the PHS Account
	 *	@param phsAccount String 
	 */

    public void setPhsAcount(String phsAcount) {
        this.phsAcount = phsAcount;
    }

	/**
	 *	This method is used to fetch the NSF Institutional Code
	 *	@return String nsfInstitutionalCode
	 */

    public String getNsfInstitutionalCode() {
        return nsfInstitutionalCode;
    }

	/**
	 *	This method is used to set the NSF Institutional Code
	 *	@param nsfInstitutionalCode String 
	 */

	public void setNsfInstitutionalCode(String nsfInstitutionalCode) {
        this.nsfInstitutionalCode = nsfInstitutionalCode;
    }

	/**
	 *	This method is used to fetch the Indirect Cost Rate Agreement
	 *	@return String indirectCostRateAgreement
	 */


	public String getIndirectCostRateAgreement() {
        return indirectCostRateAgreement;
    }

	/**
	 *	This method is used to set the Indirect Cost Rate Agreement
	 *	@param indirectCostRateAgreement String 
	 */

    public void setIndirectCostRateAgreement(String indirectCostRateAgreement) {
        this.indirectCostRateAgreement = indirectCostRateAgreement;
    }

	/**
	 *	This method is used to fetch the Cognizant Auditor
	 *	@return String cognizantAuditor
	 */

    public int getCognizantAuditor() {
        return cognizantAuditor;
    }

	/**
	 *	This method is used to set the Cognizant Auditor
	 *	@param cognizantAuditor int value
	 */

    public void setCognizantAuditor(int cognizantAuditor) {
        this.cognizantAuditor = cognizantAuditor;
    }

	/**
	 *	This method is used to fetch the Cognizant Auditor Name
	 *	@return String cognizantAuditorName
	 */

    public String getCognizantAuditorName() {
        return cognizantAuditorName;
    }

	/**
	 *	This method is used to set the Cognizant Auditor Name
	 *	@param cognizantAuditorName String 
	 */

    public void setCognizantAuditorName(String cognizantAuditorName) {
        this.cognizantAuditorName = cognizantAuditorName;
    }

	/**
	 *	This method is used to fetch the ONR Resident Representative
	 *	@return String onrResidentRep
	 */

    public int getOnrResidentRep() {
        return onrResidentRep;
    }

	/**
	 *	This method is used to set the ONR Resident Representative
	 *	@param onrResidentRep int
	 */

    public void setOnrResidentRep(int onrResidentRep) {
        this.onrResidentRep = onrResidentRep;
    }

	/**
	 *	This method is used to fetch the ONR Resident Representative Name
	 *	@return String onrResidentRepName
	 */

    public String getOnrResidentRepName() {
        return onrResidentRepName;
    }

	/**
	 *	This method is used to set the ONR Resident Representative Name
	 *	@param onrResidentRepName String 
	 */

    public void setOnrResidentRepName(String onrResidentRepName) {
        this.onrResidentRepName = onrResidentRepName;
    }

	/**
	 *	This method is used to fetch the timestamp of the update
	 *	@return String updateTimeStamp
	 */

    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

	/**
	 *	This method is used to set the timestamp of the update
	 *	@param updateTimeStamp Timestamp
	 */

    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    /**
	 *	This method is used to fetch the user who updated the details
	 *	@return String updateUser
	 */
	
    public String getUpdateUser() {
        return updateUser;
    }

	/**
	 *	This method is used to set the user who updated the details
	 *	@param updateUser String 
	 */

	public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     *	This method is used to get the action type Add/Update
     *	@return String updateUser
     */
    
    public String getAcType() {
        return acType;
    }
    
    /**
     *	This method is used to set the action type Add/Update
     *	@param acType String 
     */
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /**
     * This method set the ref ID.
     * @param refId string value
     */
   public void setRefId(String refId){
        this.refId = refId;
    }
   
    /**
     * This method set the ref ID.
     * @return string value refId
     */
    public String getRefId(){
        return this.refId;
    }
}