/*
 * @(#)NSFOtherPersonnelBean.java 
 *
 * This bean contains information for printing NSF Other Personnel
 * added as part of NSF schema extensions
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType;
import java.beans.*;
import java.util.Vector;
import java.math.*;

public class NSFOtherPersonnelBean implements CoeusBean, java.io.Serializable{
    
    private BigInteger gradCount;  
    private BigInteger postDocCount; 
    private BigInteger clericalCount; 
    private BigInteger underGradCount;
    private BigInteger otherProfCount;
    private BigInteger otherCount;
    
    private BigDecimal gradFunds;
    private BigDecimal otherFunds ;
    private BigDecimal underGradFunds ;
    private BigDecimal otherProfFunds ;
    private BigDecimal otherLAFunds ;
    private BigDecimal postDocFunds ;
    private BigDecimal clericalFunds ;
   
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

  
    /**
     *	Default Constructor
     */
    
    public NSFOtherPersonnelBean(){
    }
      
   
    /** Getter for property gradCount.
     * @return Value of property gradCount.
    */
    public BigInteger getGradCount() {
        return gradCount;
    }    
    
    /** Setter for property gradCount.
     * @param gradCount New value of property gradCount.
    */
    public void setGradCount(BigInteger gradCount) {
        this.gradCount = gradCount;
    }
    
  /** Getter for property postDocCount.
     * @return Value of property postDocCount.
    */
    public BigInteger getPostDocCount() {
        return postDocCount;
    }    
    
    /** Setter for property postDocCount.
     * @param postDocCount New value of property postDocCount.
    */
    public void setPostDocCount(BigInteger postDocCount) {
        this.postDocCount = postDocCount;
    }
      
    /** Getter for property clericalCount.
     * @return Value of property clericalCount.
    */
    public BigInteger getClericalCount() {
        return clericalCount;
    }    
    
    /** Setter for property clericalCount.
     * @param clericalCount New value of property clericalCount.
    */
    public void setClericalCount(BigInteger clericalCount) {
        this.clericalCount = clericalCount;
    }
  
  
    /** Getter for property underGradCount.
     * @return Value of property underGradCount.
    */
    public BigInteger getUnderGradCount() {
        return underGradCount;
    }    
    
    /** Setter for property underGradCount.
     * @param underGradCount New value of property underGradCount.
    */
    public void setUnderGradCount(BigInteger underGradCount) {
        this.underGradCount = underGradCount;
    }
    
    /** Getter for property otherProfCount.
     * @return Value of property otherProfCount.
    */
    public BigInteger getOtherProfCount() {
        return otherProfCount;
    }    
    
    /** Setter for property otherProfCount.
     * @param otherProfCount New value of property otherProfCount.
    */
    public void setOtherProfCount(BigInteger otherProfCount) {
        this.otherProfCount = otherProfCount;
    }
    
    /** Getter for property otherCount.
     * @return Value of property otherCount.
    */
    public BigInteger getOtherCount() {
        return otherCount;
    }    
    
    /** Setter for property otherCount.
     * @param otherCount New value of property otherCount.
    */
    public void setOtherCount(BigInteger otherCount) {
        this.otherCount = otherCount;
    }

    
    
    /** Getter for property gradFunds.
     * @return Value of property gradFunds.
     */
    public BigDecimal getGradFunds() {
        return gradFunds;
    }
    
    /** Setter for property gradFunds.
     * @param gradFunds New value of property gradFunds.
     */
    public void setGradFunds(BigDecimal gradFunds) {
        this.gradFunds = gradFunds;
    }
   

    
     /** Getter for property underGradFunds.
     * @return Value of property underGradFunds.
     */
    public BigDecimal getUnderGradFunds() {
        return underGradFunds;
    }
    
    /** Setter for property underGradFunds.
     * @param underGradFunds New value of property underGradFunds.
     */
    public void setUnderGradFunds(BigDecimal underGradFunds) {
        this.underGradFunds = underGradFunds;
    }
    /** Getter for property otherFunds.
     * @return Value of property otherFunds.
     */
    public BigDecimal getOtherFunds() {
        return otherFunds;
    }
    
    /** Setter for property otherFunds.
     * @param otherFunds New value of property otherFunds.
     */
    public void setOtherFunds(BigDecimal otherFunds) {
        this.otherFunds = otherFunds;
    }
    
    /** Getter for property otherProfFunds.
     * @return Value of property otherProfFunds.
     */
    public BigDecimal getOtherProfFunds() {
        return otherProfFunds;
    }
    
    /** Setter for property otherProfFunds.
     * @param otherProfFunds New value of property otherProfFunds.
     */
    public void setOtherProfFunds(BigDecimal otherProfFunds) {
        this.otherProfFunds = otherProfFunds;
    }
   
     /** Getter for property otherLAFunds.
     * @return Value of property otherLAFunds.
     */
    public BigDecimal getOtherLAFunds() {
        return otherLAFunds;
    }
    
    /** Setter for property otherLAFunds.
     * @param otherLAFunds New value of property otherLAFunds.
     */
    public void setOtherLAFunds(BigDecimal otherLAFunds) {
        this.otherLAFunds = otherLAFunds;
    }
    
     /** Getter for property postDocFunds.
     * @return Value of property postDocFunds.
     */
    public BigDecimal getPostDocFunds() {
        return postDocFunds;
    }
    
    /** Setter for property postDocFunds.
     * @param postDocFunds New value of property postDocFunds.
     */
    public void setPostDocFunds(BigDecimal postDocFunds) {
        this.postDocFunds = postDocFunds;
    }
    
    /** Getter for property clericalFunds.
     * @return Value of property clericalFunds.
     */
    public BigDecimal getClericalFunds() {
        return clericalFunds;
    }
    
    /** Setter for property clericalFunds.
     * @param clericalFunds New value of property clericalFunds.
     */
    public void setClericalFunds(BigDecimal clericalFunds) {
        this.clericalFunds = clericalFunds;
    }
    
     // following methods are necessary to implement CoeusBean  
   
    public String getUpdateUser() {
     return updateUser;
    }
    public void setUpdateUser(String userId) {
    }
    
  public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
    return true;
  }

    public java.lang.String getAcType() {
     return acType;
    }
    
    public void setAcType(java.lang.String acType) {      
    }  
   
    public java.sql.Timestamp getUpdateTimestamp() {
     return updateTimestamp;
    }
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {   
    }
    
    
}