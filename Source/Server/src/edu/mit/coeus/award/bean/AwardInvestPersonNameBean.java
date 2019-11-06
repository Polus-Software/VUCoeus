/*
 * AwardInvestPersonNameBean.java
 *
 * Created on September 2, 2004, 11:48 AM
 */

package edu.mit.coeus.award.bean;

/**
 *
 * @author  shivakumarmj
 */
public class AwardInvestPersonNameBean extends AwardAmountInfoBean implements java.io.Serializable {
    // holds the person name
    private String personName;
    
    /** Creates a new instance of AwardInvestPersonNameBean */
    public AwardInvestPersonNameBean() {
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     *
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     *
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
}
