/*
 * SubcontactExpenditureBean.java
 *
 * Created on January 4, 2005, 4:06 PM
 */

package edu.mit.coeus.award.bean;

/**
 *
 * @author  nadhgj
 */
public class SubcontactExpenditureBean extends SubcontactingBudgetBean {
    
    private double a8DisadvantageAmt;
    
    private double serviceDisabledVetOwnedAmt;
    
    private double historicalBlackCollegeAmt;
    
    /** Creates a new instance of SubcontactExpenditureBean */
    public SubcontactExpenditureBean() {
    }
    
    /** Getter for property serviceDisabledVetOwnedAmt.
     * @return Value of property serviceDisabledVetOwnedAmt.
     *
     */
    public double getServiceDisabledVetOwnedAmt() {
        return serviceDisabledVetOwnedAmt;
    }
    
    /** Setter for property serviceDisabledVetOwnedAmt.
     * @param serviceDisabledVetOwnedAmt New value of property serviceDisabledVetOwnedAmt.
     *
     */
    public void setServiceDisabledVetOwnedAmt(double serviceDisabledVetOwnedAmt) {
        this.serviceDisabledVetOwnedAmt = serviceDisabledVetOwnedAmt;
    }
    
    /** Getter for property historicalBlackCollegeAmt.
     * @return Value of property historicalBlackCollegeAmt.
     *
     */
    public double getHistoricalBlackCollegeAmt() {
        return historicalBlackCollegeAmt;
    }
    
    /** Setter for property historicalBlackCollegeAmt.
     * @param historicalBlackCollegeAmt New value of property historicalBlackCollegeAmt.
     *
     */
    public void setHistoricalBlackCollegeAmt(double historicalBlackCollegeAmt) {
        this.historicalBlackCollegeAmt = historicalBlackCollegeAmt;
    }
    
    /** Getter for property a8DisadvantageAmt.
     * @return Value of property a8DisadvantageAmt.
     *
     */
    public double getA8DisadvantageAmt() {
        return a8DisadvantageAmt;
    }
    
    /** Setter for property a8DisadvantageAmt.
     * @param a8DisadvantageAmt New value of property a8DisadvantageAmt.
     *
     */
    public void setA8DisadvantageAmt(double a8DisadvantageAmt) {
        this.a8DisadvantageAmt = a8DisadvantageAmt;
    }
    
}
