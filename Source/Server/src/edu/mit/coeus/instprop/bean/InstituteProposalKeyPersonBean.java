/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * InstituteProposalKeyPersonBean.java
 *
 * Created on January 15, 2009, 5:14 PM
 *
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.bean.KeyPersonBean;

/**
 *
 * @author sreenathv
 */
public class InstituteProposalKeyPersonBean extends KeyPersonBean implements IBaseDataBean{
    
    private String proposalNumber;
    private int sequenceNumber;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    //holds the true value if person is both PI and Key person flag
    private boolean bothPIAndKeyPersonFlag;
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    
    /** Creates a new instance of InstituteProposalKeyPersonBean */
    public InstituteProposalKeyPersonBean(KeyPersonBean keyPersonBean) {
        super(keyPersonBean);
    }
    
    public InstituteProposalKeyPersonBean() { }
    
    
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
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
        if(obj instanceof InstituteProposalKeyPersonBean){
            InstituteProposalKeyPersonBean instituteProposalKeyPersonBean = (InstituteProposalKeyPersonBean)obj;
            if(instituteProposalKeyPersonBean.getProposalNumber().equals(getProposalNumber()) &&
                    instituteProposalKeyPersonBean.getPersonId().equals(getPersonId())){
                return true;
            }
        }
        return false;
    }
    public int hashCode(){
        StringBuffer buffer= new StringBuffer(30);
        buffer.append(proposalNumber);
        buffer.append('-');
        buffer.append(sequenceNumber);
        buffer.append('-');
        buffer.append(getPersonId());
        return buffer.toString().hashCode();
    }
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** Getter for property bothPIAndKeyPersonFlag.
     * @return Value of property bothPIAndKeyPersonFlag.
     */
    public boolean isBothPIAndKeyPerson() {
        return bothPIAndKeyPersonFlag;
    }
    
    /** Setter for property bothPIAndKeyPersonFlag.
     * @param bothPIAndKeyPersonFlag property bothPIAndKeyPersonFlag.
     */
    public void setBothPIAndKeyPerson(boolean bothPIAndKeyPersonFlag) {
        this.bothPIAndKeyPersonFlag = bothPIAndKeyPersonFlag;
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
}
