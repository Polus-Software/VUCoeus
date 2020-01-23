/*
 * @(#)ProgramIncomeBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;
import java.math.BigDecimal;

public class ProgramIncomeBean implements CoeusBean {

      private BigDecimal amount;
      private String source;
      //holds update user id - needed to implement CoeusBean
      private String updateUser = null;
      //holds update timestamp - needed to implement CoeusBean
      private java.sql.Timestamp updateTimestamp = null;
      //holds account type - needed to implement CoeusBean
      private String acType = null;
      
   
     /**
     *	 Default Constructor
     */
    
      public  ProgramIncomeBean(){
       }
     
      /**
       * Getter for property amount.
       * @return Value of property amount.
       */
      public java.math.BigDecimal getAmount() {
          return amount;
      }      
      
      /**
       * Setter for property amount.
       * @param amount New value of property amount.
       */
      public void setAmount(java.math.BigDecimal amount) {
          this.amount = amount;
      }
      
      /**
       * Getter for property source.
       * @return Value of property source.
       */
      public java.lang.String getSource() {
          return source;
      }
      
      /**
       * Setter for property source.
       * @param source New value of property source.
       */
      public void setSource(java.lang.String source) {
          this.source = source;
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