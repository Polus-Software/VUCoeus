/**
 * @(#)AwardBudgetBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Budget data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardBudgetBean extends AwardBaseBean{
    
    private int lineItemNumber;
    private String costElement;
    private String costElementDescription;
    private String lineItemDescription;
    private double anticipatedAmount;
    private double obligatedAmount;    
    
    /**
     *	Default Constructor
     */
    
    public AwardBudgetBean(){
    }        
    
    /** Getter for property lineItemNumber.
     * @return Value of property lineItemNumber.
     */
    public int getLineItemNumber() {
        return lineItemNumber;
    }
    
    /** Setter for property lineItemNumber.
     * @param lineItemNumber New value of property lineItemNumber.
     */
    public void setLineItemNumber(int lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }
    
    /** Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }
    
    /** Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }
    
    /** Getter for property lineItemDescription.
     * @return Value of property lineItemDescription.
     */
    public java.lang.String getLineItemDescription() {
        return lineItemDescription;
    }
    
    /** Setter for property lineItemDescription.
     * @param lineItemDescription New value of property lineItemDescription.
     */
    public void setLineItemDescription(java.lang.String lineItemDescription) {
        this.lineItemDescription = lineItemDescription;
    }
    
    /** Getter for property anticipatedAmount.
     * @return Value of property anticipatedAmount.
     */
    public double getAnticipatedAmount() {
        return anticipatedAmount;
    }
    
    /** Setter for property anticipatedAmount.
     * @param anticipatedAmount New value of property anticipatedAmount.
     */
    public void setAnticipatedAmount(double anticipatedAmount) {
        this.anticipatedAmount = anticipatedAmount;
    }
    
    /** Getter for property obligatedAmount.
     * @return Value of property obligatedAmount.
     */
    public double getObligatedAmount() {
        return obligatedAmount;
    }
    
    /** Setter for property obligatedAmount.
     * @param obligatedAmount New value of property obligatedAmount.
     */
    public void setObligatedAmount(double obligatedAmount) {
        this.obligatedAmount = obligatedAmount;
    }    
    
    /** Getter for property costElementDescription.
     * @return Value of property costElementDescription.
     */
    public java.lang.String getCostElementDescription() {
        return costElementDescription;
    }
    
    /** Setter for property costElementDescription.
     * @param costElementDescription New value of property costElementDescription.
     */
    public void setCostElementDescription(java.lang.String costElementDescription) {
        this.costElementDescription = costElementDescription;
    }    
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardBudgetBean awardBudgetBean = (AwardBudgetBean)obj;
            if(awardBudgetBean.getLineItemNumber() == getLineItemNumber()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}