/*
 * @(#)PersonCustomElementsInfoBean.java 1.0 03/14/03 12:50 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;
import java.sql.Timestamp;
import edu.mit.coeus.customelements.bean.*;

/**
 * The class used to hold the information of <code>Department Others</code>
 *
 * @author  Raghunath
 * @version 1.0
 * Created on March 14, 2003, 12:50 PM
 */

public class PersonCustomElementsInfoBean extends CustomElementsInfoBean implements java.io.Serializable{
    
     //holds the person id
     private String personId;
     
     /** Creates a new instance of PersonCustomElementsInfoBean */
     
     public PersonCustomElementsInfoBean() {
         super();
     }
     
     public PersonCustomElementsInfoBean(CustomElementsInfoBean customElementsInfoBean) {
         super(customElementsInfoBean);
     }
     
     public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
