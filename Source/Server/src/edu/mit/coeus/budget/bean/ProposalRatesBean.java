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
 * The class is the used to hold data of Proposal LA rates.
 
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 12:58 PM
 */

public class ProposalRatesBean extends ProposalLARatesBean implements SortBean{
   
    //holds activityTypeCode
    private int activityCode = -1;
    
    //holds activity type description
    private String activityTypeDescription = null;
    
    //holds aw_ActivityTypeCode
    private int aw_ActivityTypeCode = -1;
    
    /** Creates a new instance of ProposalRatesBean */
    public ProposalRatesBean() {
        
    }    
    
    /** Getter for property activityCode.
     * @return Value of property activityCode.
     */
    public int getActivityCode() {
        return activityCode;
    }
    
    /** Setter for property activityCode.
     * @param activityCode New value of property activityCode.
     */
    public void setActivityCode(int activityCode) {
        this.activityCode = activityCode;
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;        
        if(comparableBean instanceof ProposalRatesBean){
            ProposalRatesBean proposalRatesBean = (ProposalRatesBean)comparableBean;
            
            //Proposal Number
            if(proposalRatesBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(proposalRatesBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(proposalRatesBean.getVersionNumber()!=-1){
                if(getVersionNumber() == proposalRatesBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Rate Class Code
            if(proposalRatesBean.getRateClassCode()!=-1){
                if(getRateClassCode() == proposalRatesBean.getRateClassCode()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Rate Type Code
            if(proposalRatesBean.getRateTypeCode()!=-1){
                if(getRateTypeCode() == proposalRatesBean.getRateTypeCode()){
                    success = true;
                }else{
                    return false;
                }
            }            
            //Fiscal year
            if(proposalRatesBean.getFiscalYear()!=null){
                if(getFiscalYear().equals(proposalRatesBean.getFiscalYear())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Start Date
            if(proposalRatesBean.getStartDate()!=null){
                if(getStartDate().equals(proposalRatesBean.getStartDate())){
                    success = true;
                }else{
                    return false;
                }
            }
            //On Off Campus Flag
            if(isOnOffCampusFlag() == proposalRatesBean.isOnOffCampusFlag()){
                success = true;
            }else{
                return false;
            }
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }    
    
    /** Getter for property activityTypeDescription.
     * @return Value of property activityTypeDescription.
     */
    public String getActivityTypeDescription() {
        return activityTypeDescription;
    }
    
    /** Setter for property activityTypeDescription.
     * @param activityTypeDescription New value of property activityTypeDescription.
     */
    public void setActivityTypeDescription(String activityTypeDescription) {
        this.activityTypeDescription = activityTypeDescription;
    }
    
    
    // Added by Ranjeev on 12/11/2003 - start
    public boolean equals(Object obj) {
        ProposalRatesBean proposalRatesBean = (ProposalRatesBean)obj;
        if(proposalRatesBean.getProposalNumber().equals(getProposalNumber()) &&
        proposalRatesBean.getVersionNumber() == getVersionNumber() &&
        proposalRatesBean.getRateTypeCode()== getRateTypeCode() &&
        proposalRatesBean.getRateClassCode()== getRateClassCode() &&
        proposalRatesBean.getFiscalYear().equals(getFiscalYear())&&
        proposalRatesBean.isOnOffCampusFlag()== isOnOffCampusFlag()&&
        proposalRatesBean.getStartDate().equals(getStartDate())){
            return true;
        }else {
            return false;
        }
    }
    // Added by Ranjeev 12/11/2003 - End
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        String parentContents = super.toString();
        StringBuffer strBffr = new StringBuffer(parentContents);
        strBffr.append("Activity Code=>"+activityCode);
        strBffr.append(";");
        strBffr.append("Activity Description=>"+activityTypeDescription);
        strBffr.append("\n");
        return strBffr.toString();
    }    
    
    public java.util.Date getFieldToBeSorted() {
        return getStartDate();
    }
    
    /** Getter for property aw_ActivityTypeCode.
     * @return Value of property aw_ActivityTypeCode.
     */
    public int getAw_ActivityTypeCode() {
        return aw_ActivityTypeCode;
    }
    
    /** Setter for property aw_ActivityTypeCode.
     * @param aw_ActivityTypeCode New value of property aw_ActivityTypeCode.
     */
    public void setAw_ActivityTypeCode(int aw_ActivityTypeCode) {
        this.aw_ActivityTypeCode = aw_ActivityTypeCode;
    }
    
} // end ProposalRatesBean