/*
 * @(#)SponsorMaintenanceFormBean.java 1.0 8/14/02 2:15 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 20-APR-2011
 * by Bharati
 */

package edu.mit.coeus.sponsormaint.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;

/** 
 * This bean used to hold the information of <CODE>SponsorMaintenanceForm</CODE>
 * The data will get attached with the <CODE>SponsorMaintenanceBean</CODE>
 * this bean used set the sponsor data and get data,it is used by <CODE>TransactionBean</CODE>
 * <CODE>SponsorMaintenanceForm</CODE> and <CODE>SponsorMaintenanceservlet</CODE>.
 *
 * @version :1.0 Aug 14,2002 2:15 PM
 * @author Mukundan C
 */

public class SponsorMaintenanceFormBean implements CoeusBean, Serializable{
    
    //holds sponsor id
    private String sponsorCode;
    //holds Acronym for sponsor
    private String acronym;
    //holds sponsor name
    private String name;
    //holds sponsor type code
    private String type;
    //holds sponsor type description
    private String typeDescription;
    //holds Audit report number for sponsor
    private String auditReport;
    //holds sponsor duns number
    private String duns;
    //holds sponsor duns+4 number
    private String duns4;
    //holds sponsor dodc number
    private String dodc;
    //holds sponsor cage number
    private String cage;
    //holds state for a country
    private String state;
    //holds state for a country
    private String stateDescription;
    //holds country
    private String country;
    //holds country
    private String countryName;
    //holds Rolodex id
    private String rolodexId;
    //holds postal code
    private String postalCode;
    //holds owner for the sponsor
    private String ownedBy;
    //holds created the sponsor
    private String createUser;
    //holds update user id
    private String updateUser;
    //holds lastupdate tmiestamp
    private Timestamp lastUpdateTime;
    //holds flag set for Update,Add or delete
    private String acType;
    //holds Vector states for country USA
    private Vector states;
    //holds Vector Countries
    private Vector countries;
    //holds sponsor type
    private Vector types;
    private String refId;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
    // holds sponsor status
    private String status;
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end
    //holds Visual Compliance
    private String visualCompliance;
    //holds Visual Compliance Text
    private String visualComplianceExpl;
    private java.sql.Timestamp updateTimestamp = null;
    
    //Added for Userid to Username enhancement
    private String updateUserName;
    
    
    
    /** Creates new <CODE>SponsorMaintenanceFormBean</CODE>
     */
    public SponsorMaintenanceFormBean() {
        
    }
    
    /**
     * getSponsorCode gets SponsorCode.
     *
     * @return SponsorCode String
     */
    public String getSponsorCode(){
        return sponsorCode;
    }
    
    /**
     * setSponsorCode sets SponsorCode for SponsorMaintenance form.
     *
     * @param sponsorCode String
     */
    public void setSponsorCode(String sponsorCode){
        this.sponsorCode = sponsorCode;
    }
    
    /**
     * getAcronym gets Acronym for SponsorMaintenance form.
     *
     * @return Acronym String
     */
    public String getAcronym(){
        return acronym;
    }
    
    /**
     * setAcronym sets Acronym for SponsorMaintenance form.
     *
     * @param acronym String
     */
    public void setAcronym(String acronym){
        this.acronym = acronym;
    }
    
    /**
     * getName gets Name for SponsorMaintenance form.
     *
     * @return Name String
     */
    public String getName(){
        return name;
    }
    
    /**
     * setName sets Students Name for SponsorMaintenance form.
     *
     * @param name String
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * getType gets Type for SponsorMaintenance form.
     *
     * @return type String
     */
    public String getType(){
        return type;
    }
    
    /**
     * setType sets Type for SponsorMaintenance form.
     *
     * @param type String
     */
    public void setType(String type){
        this.type = type;
    }
    
    /**
     * getType gets Type for SponsorMaintenance form.
     *
     * @return type String
     */
    public String getTypeDescription(){
        return typeDescription;
    }
    
    /**
     * setType sets Type for SponsorMaintenance form.
     *
     * @param typeDescription String
     */
    public void setTypeDescription(String typeDescription){
        this.typeDescription = typeDescription;
    }
    
    /**
     * getAuditReport gets Audit Report Sent for Fy for SponsorMaintenance form.
     *
     * @return auditReport String
     */
    public String getAuditReport(){
        return auditReport;
    }
    
    /**
     * setAuditReport sets Audit Report Sent for Fy for SponsorMaintenance form.
     *
     * @param auditReport String
     */
    public void setAuditReport(String auditReport){
        this.auditReport = auditReport;
    }
    
    /**
     * getDuns gets Duns for SponsorMaintenance form.
     *
     * @return duns String
     */
    public String getDuns(){
        return duns;
    }
    
    /**
     * setDuns sets Duns for SponsorMaintenance form.
     *
     * @param duns String
     */
    public void setDuns(String duns){
        this.duns = duns;
    }
    
    /**
     * getDuns4 gets Dun+4 for SponsorMaintenance form.
     *
     * @return duns4 String
     */
    public String getDuns4(){
        return duns4;
    }
    
    /**
     * setDuns4 sets Dun+4 for SponsorMaintenance form.
     *
     * @param duns4 String
     */
    public void setDuns4(String duns4){
        this.duns4 = duns4;
    }
    
    /**
     * getDodc gets Dodc for SponsorMaintenance form.
     *
     * @return dodc String
     */
    public String getDodc(){
        return dodc;
    }
    
    /**
     * setDodc sets Dodc for SponsorMaintenance form.
     *
     * @param dodc String
     */
    public void setDodc(String dodc){
        this.dodc = dodc;
    }
    
    /**
     * getCage gets Cage for SponsorMaintenance form.
     *
     * @return cage String
     */
    public String getCage(){
        return cage;
    }
    
    /**
     * setCage sets Cage for SponsorMaintenance form.
     *
     * @param cage String
     */
    public void setCage(String cage){
        this.cage = cage;
    }
    
    
    /**
     * getState gets State the Students belong for SponsorMaintenance form.
     *
     * @return state String
     */
    public String getState(){
        return state;
    }
    
    /**
     * setState sets State for the Student of SponsorMaintenance form.
     *
     * @param state String
     */
    public void setState(String state){
        this.state = state;
    }
    
    /**
     * getState gets State the Students belong for SponsorMaintenance form.
     *
     * @return state String
     */
    public String getStateDescription(){
        return stateDescription;
    }
    
    /**
     * setState sets State for the Student of SponsorMaintenance form.
     *
     * @param stateDescription String
     */
    public void setStateDescription(String stateDescription){
        this.stateDescription = stateDescription;
    }
    
    /**
     * getCountry gets Country the Students belong for SponsorMaintenance form.
     *
     * @return country String
     */
    public String getCountry(){
        return country;
    }
    
    /**
     * setCountry sets Country for the Student of SponsorMaintenance form.
     *
     * @param country String
     */
    public void setCountry(String country){
        this.country = country;
    }
    
    /**
     * getCountry gets Country the Students belong for SponsorMaintenance form.
     *
     * @return country String
     */
    public String getCountryName(){
        return countryName;
    }
    
    /**
     * setCountry sets Country for the Student of SponsorMaintenance form.
     *
     * @param countryName String
     */
    public void setCountryName(String countryName){
        this.countryName = countryName;
    }
    
    /**
     * getRolodexID gets rolodex id of the Students number for SponsorMaintenance form.
     *
     * @return rolodexId String
     */
    public String getRolodexID(){
        return rolodexId;
    }
    
    /**
     * setRolodexID sets rolodexid for the Student of SponsorMaintenance form.
     *
     * @param rolodexId String
     */
    public void setRolodexID(String rolodexId){
        this.rolodexId = rolodexId;
    }
    
    /**
     * getPostalCode gets postal code of the Students number for SponsorMaintenance form.
     *
     * @return postalCode String
     */
    public String getPostalCode(){
        return postalCode;
    }
    
    /**
     * setPostalCode sets postalCode for the Student of SponsorMaintenance form.
     *
     * @param postalCode String
     */
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    
    /**
     * getOwnedBy gets owner name of the Students for SponsorMaintenance form.
     *
     * @return ownedBy String
     */
    public String getOwnedBy(){
        return ownedBy;
    }
    
    /**
     * setOwnedBy sets owner name for the Student of SponsorMaintenance form.
     *
     * @param ownedBy String
     */
    public void setOwnedBy(String ownedBy){
        this.ownedBy = ownedBy;
    }
    
    /**
     * getCreateUser gets owner who created this sponsor.
     *
     * @return createUser String
     */
    public String getCreateUser(){
        return createUser;
    }
    
    /**
     * setCreateUser sets owner who has right to create.
     *
     * @param createUser String
     */
    public void setCreateUser(String createUser){
        this.createUser = createUser;
    }
    
    
    /**
     * getUpdateUser gets owner name of the Students for SponsorMaintenance form.
     *
     * @return ownedBy String
     */
    public String getLastUpdateUser(){
        return updateUser;
    }
    
    /**
     * setUpdateUser sets owner name for the Student of SponsorMaintenance form.
     *
     * @param updateUser String
     */
    public void setLastUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }
    
    /**
     * getLastUpdateTime gets owner name of the Students for SponsorMaintenance form.
     *
     * @return lastUpdateTime String
     */
    public Timestamp getLastUpdateTime(){
        return lastUpdateTime;
    }
    
    /**
     * setLastUpdateTime sets owner name for the Student of SponsorMaintenance form.
     *
     * @param lastUpdateTime String
     */
    public void setLastUpdateTime(Timestamp lastUpdateTime){
        this.lastUpdateTime = lastUpdateTime;
    }
    
    /**
     * getAcType gets char whether the user wants to update or add.
     *
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * setAcType sets type on the conditions.
     *
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
    
    /**
     * getStates gets Vector states from the database for USA.
     *
     * @return states Vector
     */
    public Vector getStates() {
        return states ;
    }
    
    /**
     * setStates sets the states after collecting from the database.
     *
     * @param states Vector
     */
    public void setStates(Vector states){
        this.states = states;
    }
    
    /**
     * getCountries gets Vector countries from the database which unversity approved.
     *
     * @return countries Vector
     */
    public Vector getCountries() {
        return countries ;
    }
    
    /**
     * setCountries sets the countries after collecting from the database.
     *
     * @param countries Vector
     */
    public void setCountries(Vector countries){
        this.countries = countries;
    }
    
    /**
     * getTypes gets Vector Sponsor Types from the database for Sponsor maintenance.
     *
     * @return types Vector
     */
    public Vector getTypes() {
        return types ;
    }
    
    /**
     * setTypes sets the sponsor types after collecting from the database.
     *
     * @param types Vector
     */
    public void setTypes(Vector types){
        this.types = types;
    }
    
    /**
     * Method used to set the reference Id
     * @param refId String
     */
    public void setRefId(String refId){
        this.refId = refId;
    }
    
    /**
     * Method used to get the reference Id
     * @return refId String
     */
    public String getRefId(){
        return this.refId;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     *
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     *
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
     public boolean isLike(ComparableBean comparableBean) throws CoeusException {
         return true;
     }

     //UserId to Username enhancement - Start
     /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //UserId to Username enhancement - End
    
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - start
     /**
     * gets the status of the Sponsor
     * @return status of the Sponsor
     */
    public String getStatus() {
        return status;
    }
    /**
     * sets the status to the Sponsor for database operations
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    //Added for COEUSQA-1434 : Add the functionality to set a status on a Sponsor record - end

    public String getVisualCompliance() {
        return visualCompliance;
    }

    public void setVisualCompliance(String visualCompliance) {
        this.visualCompliance = visualCompliance;
    }

    public String getVisualComplianceExpl() {
        return visualComplianceExpl;
    }

    public void setVisualComplianceExpl(String visualComplianceExpl) {
        this.visualComplianceExpl = visualComplianceExpl;
    }
    
    
}
