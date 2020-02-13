/* 
 * @(#)AwardUnitBean.java 1.0 01/05/04 11:41 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import java.beans.*;
import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Award Units</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 05, 2004, 11:41 AM
 */

public class AwardUnitBean extends InvestigatorUnitBean implements IBaseDataBean{
    // holds the proposal number
    private String mitAwardNumber;
    //hoilds sequence number
    private int sequenceNumber;
    
    /** Creates new AwardUnitBean */
    public AwardUnitBean() {
    }

    /** Creates new AwardUnitBean */
    public AwardUnitBean(InvestigatorUnitBean investigatorUnitBean) {
        super(investigatorUnitBean);
    }    
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */
    public boolean equals(Object obj) {
        AwardUnitBean awardUnitBean = (AwardUnitBean)obj;
        //Donot check equality for sequence number - 8th April, 2004
        if(awardUnitBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
            // Need to check for equlaity - Case 2037
            awardUnitBean.getSequenceNumber() == getSequenceNumber() &&        
            awardUnitBean.getUnitNumber().equals(getUnitNumber()) &&
            awardUnitBean.getPersonId().equals(getPersonId())){
                return true;
       }else{
            return false;
        }
    }            
    
    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     */
    public java.lang.String getMitAwardNumber() {
        return mitAwardNumber;
    }
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
}