/*
 * @(#)CommitteeActiveMemberBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.irb.bean;

import java.io.Serializable;

/**
 * This Class is used to contain the Data Object for the active member information.
 * It contain the member information like MemberID, CommitteeID, personID,
 * PersonName, Description, Non-Employee Flag. This class is used as 
 * data object while communicating from server/client and viceversa
 * This will list all active members of the committee. 
 *
 * @author  subramanya  Created on November 28, 2002, 5:11 PM
 * @version 1.1
 */
public class CommitteeActiveMemberBean implements Serializable {
    
    //holds the committee ID
    private String committeeID = null;
    
    //holds the Member ID
    private String memberID = null;
    
    //holds the personID 
    private String personID = null;
    
    //holds the personName 
    private String personName = null;
    
    //holds the employee flag
    private boolean isEmployee = false;
        
    //holds the comment
    private String comment = null;
    
    private boolean alternatePerson;
    
    
    /** 
     * Creates a new instance of CommitteeActiveMemberBean 
     * @param comID String containing the committeeID
     */
    public CommitteeActiveMemberBean( String comID ) {
        committeeID = comID;
    }
    
    /** 
     * Creates a new instance of CommitteeActiveMemberBean 
     * @param comID String containing the committeeID
     * @param memID String represent the Member ID
     * @param perID String represent the Person ID
     * @param perName String represent the Person Name
     * @param isNonEmp boolean represent the Non Employee Flag
     * @param comnt String represent the Comment of this Member
     */
    public CommitteeActiveMemberBean( String comID, String memID, String perID,
                                      String perName, String isNonEmp, 
                                      String comnt ) {
        this.committeeID = comID;
        this.memberID = memID;
        this.personID = perID;
        this.personName = perName;
        this.isEmployee = isNonEmp.equalsIgnoreCase("N")? true : false ;
        this.comment = comnt;
    }
    
    
    /** 
     * Creates a new instance of CommitteeActiveMemberBean 
     * @param comID String containing the committeeID
     * @param memID String represent the Member ID
     * @param perID String represent the Person ID
     * @param perName String represent the Person Name
     * @param isNonEmp boolean represent the Non Employee Flag
     * @param comnt String represent the Comment of this Member
     */
    public CommitteeActiveMemberBean( String comID, String memID, String perID,
                                      String perName, String isNonEmp, 
                                      String comnt,String alternatePerson ) {
        this.committeeID = comID;
        this.memberID = memID;
        this.personID = perID;
        this.personName = perName;
        this.isEmployee = isNonEmp.equalsIgnoreCase("N")? true : false ;
        this.comment = comnt;
        this.alternatePerson = alternatePerson.equalsIgnoreCase("N")? false : true;
    }
    
    
    /**
     * This method is used to get the committee ID of this Bean
     * @return String represent the committeeID
     */
    public String getCommitteeID(){
        return this.committeeID;
    }
    
    /**
     * This method is used to get the Member ID of this Bean
     * @return String represent the Member ID
     */
    public String getMemberID(){
        return this.memberID;
    }
    
    /**
     * his method is used to Set the Member ID of this Bean
     * @param memID represent the Member ID
     */
    public void setMemberID( String memID ){
        this.memberID = memID;
    }
    
    /**
     * This method is used to get the Person ID of this Bean
     * @return String represent the Person ID
     */
    public String getPersonID(){
        return this.personID;
    }
    
    /**
     * This method is used to set the Person ID of this Bean
     * @param perID  String represent the Person ID
     */
    public void setPersonID( String perID ){
        this.personID = perID;
    }
    
    /**
     * This method is used to get the Person Name of this Bean
     * @return String represent the Person Name
     */
    public String getPersonName(){
        return this.personName;
    }
    
    /**
     * This method is used to set the Person Name of this Bean
     * @param personName  String represent the Person Name
     */
    public void setPersonName( String personName ){
        this.personName = personName;
    }
    
    /**
     * This method is used to get the is Employee Flag of this Bean
     * @return boolean represent the Non employee falg
     */
    public boolean isEmployee(){
        return this.isEmployee;
    }
    
    /**
     * This method is used to set the Employee Flag of this Bean
     * @param isNonEmp String represent the Employee Flag
     */
    public void setEmployeeFlag( String isNonEmp ){        
        this.isEmployee = isNonEmp.equalsIgnoreCase("N")? true : false ;
    }
    
    
    /**
     * This method is used to get the comments of Active Member.
     * @return String represent the comments
     */
    public String getComments(){
        return this.comment;
    }
    
    /**
     * This method is used to set the Comments of Active Member.
     * @param comment  String represent the Comments of Active Member.
     */
    public void setComments( String comment ){
        this.comment = comment;
    }
    
    /**
     * Getter for property alternatePerson.
     * @return Value of property alternatePerson.
     */
    public boolean isAlternatePerson() {
        return alternatePerson;
    }    
    
    /**
     * Setter for property alternatePerson.
     * @param alternatePerson New value of property alternatePerson.
     */
    public void setAlternatePerson(boolean alternatePerson) {
        this.alternatePerson = alternatePerson;
    }
    
}
