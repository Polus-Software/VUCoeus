/*
 * @(#)AbstractBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;

public class AbstractBean implements CoeusBean, java.io.Serializable{

    
    private int abstractType;
   
    private String abstractDescription ;
    
    private String abstractText;
   
    //holds update user id - needed to implement CoeusBean
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

    /**
     *	Default Constructor
     */
    
    public AbstractBean(){
    }
      
     /** Getter for property abstractType.
     * @return Value of property abstractType.
     */
    public int getAbstractType() {
        return abstractType;
    }
    
    /** Setter for property abstractType.
     * @param abstractType New value of property abstractType.
     */
    public void setAbstractType(int abstractType) {
        this.abstractType = abstractType;
    }
    
    
    /** Getter for property abstractDescription.
     * @return Value of property abstractDescription
     */
    public String getAbstractDescription() {
        return abstractDescription;
    }
    
    /** Setter for property abstractDescription.
     * @param abstractDescription New value of property abstractDescription.
     */
    public void setAbstractDescription(String abstractDescription) {
        this.abstractDescription = abstractDescription;
    }
    
    /** Getter for property abstractText.
     * @return Value of property abstractText
     */
    public String getAbstractText() {
        return abstractText;
    }
    
    /** Setter for property abstractText.
     * @param abstractText New value of property abstractText.
     */
    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
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