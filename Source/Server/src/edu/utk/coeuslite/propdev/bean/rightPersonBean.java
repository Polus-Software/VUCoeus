/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.utk.coeuslite.propdev.bean;

import java.sql.Date;
import oracle.sql.TIMESTAMP;

/**
 *
 * @author twinkle
 */
public class rightPersonBean extends org.apache.struts.action.ActionForm {

   //private Date notificationDate;
   
//
//    public TIMESTAMP getNotificationDate() {
//        return notificationDate;
//    }
//
//    public void setNotificationDate(TIMESTAMP notificationDate) {
//        this.notificationDate = notificationDate;
//    }

  

   private String notificationDate;
   private String personId;
   private String personName;
   private String userId;
  // private String emailId;
   private char ToOrCC;

    public char getToOrCC() {
        return ToOrCC;
    }

    public void setToOrCC(char ToOrCC) {
        this.ToOrCC = ToOrCC;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    private String name;
private String id;
private String emailId;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
   

    /**
     * @return
     */
    public String getName() {
        return name;
    }


    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @return
     */
    

    /**
     *
     */
    public rightPersonBean()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the notificationDate
     */
    public String getNotificationDate() {
        return notificationDate;
    }

    /**
     * @param notificationDate the notificationDate to set
     */
    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

//    /**
//     * @return the notificationDate
//     */
//    public Date getNotificationDate() {
//        return notificationDate;
//    }
//
//    /**
//     * @param notificationDate the notificationDate to set
//     */
//    public void setNotificationDate(Date notificationDate) {
//        this.notificationDate = notificationDate;
//    }

    /**
     * @return the lastnotificationdate
     */
    
    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
//    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
//        ActionErrors errors = new ActionErrors();
//        if (getName() == null || getName().length() < 1) {
//            errors.add("name", new ActionMessage("error.name.required"));
//            // TODO: add 'error.name.required' key to your resources
//        }
//        return errors;
//    }

}
