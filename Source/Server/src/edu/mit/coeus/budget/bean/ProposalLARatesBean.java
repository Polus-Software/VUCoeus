/*
 * @(#)ProposalRatesBean.java September 29, 2003, 12:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.PrimaryKey;
import java.util.*;
import edu.mit.coeus.exception.CoeusException;


/**
 * The class is the used to hold data of Proposal Rates and Proposal LA rates.
 * NOTE: Since we cannot extend two classes we have included Proposal Number and 
 *       and Version Number attributes directly here instead of extending from
 *       BudgetBean.
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 12:58 PM
 */

public class ProposalLARatesBean extends BudgetBean implements PrimaryKey{
   
    //holds rate class code
    private int rateClassCode = -1;
    //holds rateTypeCode
    private int rateTypeCode = -1;
    //holds Fiscal Year
    private String fiscalYear = null;
    //holds start date
    private java.sql.Date startDate = null;   
    //holds the on Off Campus flag
    private boolean onOffCampusFlag;    
    //holds rate
    private double instituteRate;
    
    //holds activityTypeCode
    //private int activityCode = -1;
    //holds double applicableRate
    private double applicableRate;
    //holds rate Class Description
    private String rateClassDescription = null;
    //holds rate Type Description
    private String rateTypeDescription = null;    
    
    /** Creates a new instance of ProposalRatesBean */
    public ProposalLARatesBean() {
        
    }    
    
    /** Does ...
     *
     *
     * @return
     */
    public Object getPrimaryKey() {
        return new String(getProposalNumber()+getVersionNumber()+getRateClassCode()+getRateTypeCode()+getFiscalYear()+isOnOffCampusFlag()+getStartDate());
    }      
    
    /** Getter for property rateClassCode.
     * @return Value of property rateClassCode.
     */
    public int getRateClassCode() {
        return rateClassCode;
    }
    
    /** Setter for property rateClassCode.
     * @param rateClassCode New value of property rateClassCode.
     */
    public void setRateClassCode(int rateClassCode) {
        this.rateClassCode = rateClassCode;
    }
    
    /** Getter for property rateTypeCode.
     * @return Value of property rateTypeCode.
     */
    public int getRateTypeCode() {
        return rateTypeCode;
    }
    
    /** Setter for property rateTypeCode.
     * @param rateTypeCode New value of property rateTypeCode.
     */
    public void setRateTypeCode(int rateTypeCode) {
        this.rateTypeCode = rateTypeCode;
    }
    
    /** Getter for property fiscalYear.
     * @return Value of property fiscalYear.
     */
    public java.lang.String getFiscalYear() {
        return fiscalYear;
    }
    
    /** Setter for property fiscalYear.
     * @param fiscalYear New value of property fiscalYear.
     */
    public void setFiscalYear(java.lang.String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property instituteRate.
     * @return Value of property instituteRate.
     */
    public double getInstituteRate() {
        return instituteRate;
    }
    
    /** Setter for property instituteRate.
     * @param instituteRate New value of property instituteRate.
     */
    public void setInstituteRate(double instituteRate) {
        this.instituteRate = instituteRate;
    }    
    
    /** Getter for property applicableRate.
     * @return Value of property applicableRate.
     */
    public double getApplicableRate() {
        return applicableRate;
    }
    
    /** Setter for property applicableRate.
     * @param applicableRate New value of property applicableRate.
     */
    public void setApplicableRate(double applicableRate) {
        this.applicableRate = applicableRate;
    }
    
    /** Getter for property rateClassDescription.
     * @return Value of property rateClassDescription.
     */
    public java.lang.String getRateClassDescription() {
        return rateClassDescription;
    }
    
    /** Setter for property rateClassDescription.
     * @param rateClassDescription New value of property rateClassDescription.
     */
    public void setRateClassDescription(java.lang.String rateClassDescription) {
        this.rateClassDescription = rateClassDescription;
    }
    
    /** Getter for property rateTypeDescription.
     * @return Value of property rateTypeDescription.
     */
    public java.lang.String getRateTypeDescription() {
        return rateTypeDescription;
    }
    
    /** Setter for property rateTypeDescription.
     * @param rateTypeDescription New value of property rateTypeDescription.
     */
    public void setRateTypeDescription(java.lang.String rateTypeDescription) {
        this.rateTypeDescription = rateTypeDescription;
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;        
        if(comparableBean instanceof ProposalLARatesBean){
            ProposalLARatesBean proposalLARatesBean = (ProposalLARatesBean)comparableBean;
            
            //Proposal Number
            if(proposalLARatesBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(proposalLARatesBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(proposalLARatesBean.getVersionNumber()!=-1){
                if(getVersionNumber() == proposalLARatesBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Rate Class Code
            if(proposalLARatesBean.getRateClassCode()!=-1){
                if(getRateClassCode() == proposalLARatesBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Rate Type Code
            if(proposalLARatesBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == proposalLARatesBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Fiscal year
            if(proposalLARatesBean.getFiscalYear()!=null){
                if(getFiscalYear().equals(proposalLARatesBean.getFiscalYear())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Start Date
            if(proposalLARatesBean.getStartDate()!=null){
                if(getStartDate().equals(proposalLARatesBean.getStartDate())){
                    success = true;
                }else{
                    return false;
                }
            }
            //On Off Campus Flag
            /*if(isOnOffCampusFlag() == proposalLARatesBean.isOnOffCampusFlag()){
                success = true;
            }else{
                return false;
            }*/
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }
    
    
    // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        ProposalLARatesBean proposalLARatesBean = (ProposalLARatesBean)obj;
        if(proposalLARatesBean.getProposalNumber().equals(getProposalNumber()) &&
        proposalLARatesBean.getVersionNumber() == getVersionNumber() &&
        proposalLARatesBean.getRateTypeCode()== getRateTypeCode() &&
        proposalLARatesBean.getRateClassCode()== getRateClassCode() &&
        proposalLARatesBean.getFiscalYear().equals(getFiscalYear())&&
        proposalLARatesBean.isOnOffCampusFlag()== isOnOffCampusFlag()&&
        proposalLARatesBean.getStartDate().equals(getStartDate())){
        
            return true;
        }else {
            return false;
        }
    }
    // Added by Chandra 04/10/2003 - End
    
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        String parentContents = super.toString();
        StringBuffer strBffr = new StringBuffer(parentContents);
        strBffr.append("Rate Class Code=>"+rateClassCode);
        strBffr.append(";");
        strBffr.append("Rate Type Code=>"+rateTypeCode);  
        strBffr.append(";");
        strBffr.append("Applicable rate=>"+applicableRate);
        strBffr.append(";");
        strBffr.append("Fiscal year=>"+fiscalYear);
        strBffr.append(";");
        strBffr.append("Institue rate=>"+instituteRate);
        strBffr.append(";");
        strBffr.append("Onn Off Campus flag=>"+onOffCampusFlag);
        strBffr.append(";");
        strBffr.append("Rate class description=>"+rateClassDescription);
        strBffr.append(";");
        strBffr.append("Rate Type description=>"+rateTypeDescription);
        strBffr.append(";");
        strBffr.append("Start date=>"+startDate);
        strBffr.append("\n");
        return strBffr.toString();
    }
    
} // end ProposalRatesBean