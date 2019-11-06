/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


/*
 * AwardKeyPersonBean.java
 *
 * Created on January 27, 2009, 4:00 PM
 */

package edu.mit.coeus.award.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.utils.CoeusConstants;

/**
 *
 * @author sreenath
 */
public class AwardKeyPersonBean extends KeyPersonBean implements IBaseDataBean{
    
    private String mitAwardNumber;
    private int sequenceNumber;
    /** Creates a new instance of AwardKeyPersonBean */
    public AwardKeyPersonBean() {}
    
    public AwardKeyPersonBean(KeyPersonBean keyPersonBean) {
        super(keyPersonBean);
    }
    
    public String getMitAwardNumber() {
        return mitAwardNumber;
    }
    
    public void setMitAwardNumber(String mitAwardNumber) {
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
    
    /**
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj instanceof AwardKeyPersonBean){
            AwardKeyPersonBean awardKeyPersonBean = (AwardKeyPersonBean)obj;
            if(awardKeyPersonBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
                    awardKeyPersonBean.getSequenceNumber() == getSequenceNumber() &&
                    awardKeyPersonBean.getPersonId().equals(getPersonId())){
                return true;
            }
        }
        return false;
    }
    public int hashCode(){
        StringBuffer buffer= new StringBuffer(30);
        buffer.append(mitAwardNumber);
        buffer.append('-');
        buffer.append(sequenceNumber);
        buffer.append('-');
        buffer.append(getPersonId());
        return buffer.toString().hashCode();
    }
}
