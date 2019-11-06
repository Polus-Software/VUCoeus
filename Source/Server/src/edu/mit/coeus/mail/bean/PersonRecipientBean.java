/*
 * PersonRecipientBean.java
 *
 * Created on July 10, 2009, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.mail.bean;

import java.io.Serializable;

/**
 *
 * @author keerthyjayaraj
 */
public class PersonRecipientBean implements Serializable{
    
   private String personId;
   private String personName;
   private String userId;
   private String emailId;
   private char ToOrCC;

    public String getEmailId() {
        return emailId;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public char getToOrCC() {
        return ToOrCC;
    }

    public String getUserId() {
        return userId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setToOrCC(char ToOrCC) {
        this.ToOrCC = ToOrCC;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String toString(){
        return getPersonName();
    }
    
    public boolean equals(Object compareTo){
        if(!(compareTo instanceof PersonRecipientBean)){
            return false;
        }
        PersonRecipientBean bean = (PersonRecipientBean)compareTo;
        return getPersonId().equals(bean.getPersonId());
    }
}
