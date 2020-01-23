/*
 * @(#)OtherPersonnelBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.beans.*;
import java.util.Vector;
import java.math.*;
 

public class OtherPersonnelBean implements CoeusBean, java.io.Serializable{
 
  

    private String personnelType ;
    private BigInteger numberPersonnel;
    private String role;
    private CompensationBean compensation;

   
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

 
    /**
     *	Default Constructor
     */
    
    public OtherPersonnelBean(){
    }
      
   
    /** Getter for property numberPersonnel.
     * @return Value of property numberPersonnel.
    */
    public BigInteger getNumberPersonnel() {
        return numberPersonnel;
    }    
    
    /** Setter for property numberPersonnel.
     * @param numberPersonnel New value of property numberPersonnel.
    */
    public void setNumberPersonnel(BigInteger numberPersonnel) {
        this.numberPersonnel = numberPersonnel;
    }
    
    /** Getter for property personnelType.
     * @return Value of property personnelType.
     */
    public String getPersonnelType() {
        return personnelType;
    }
    
    /** Setter for property personnelType.
     * @param personnelType New value of property personnelType.
     */
    public void setPersonnelType(String personnelType) {
        this.personnelType = personnelType;
    }
    
   
    
    
     /** Getter for property role.
     * @return Value of property role.
     */
    public String getRole() {
        return role;
    }
    
    /** Setter for property role.
     * @param role New value of property role.
     */
    public void setRole(String role) {
        this.role = role;
    }
    
     /** Getter for property compensation.
     * @return Value of property compensation.
     */
    public CompensationBean getCompensation() {
        return compensation;
    }
    
    /** Setter for property compensation.
     * @param compensation New value of property compensation.
     */
    public void setCompensation(CompensationBean compensation) {
        this.compensation = compensation;
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