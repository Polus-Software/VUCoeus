/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * MassChangeDataBean.java
 * @author bijosht
 * Created on January 25, 2005, 5:27 PM
 */

package edu.mit.coeus.centraladmin.bean;

import edu.mit.coeus.bean.BaseBean;

public class MassChangeDataBean implements BaseBean,java.io.Serializable{
    
    private String identificationNo;
    private String fieldData;
    private boolean isCurrentSeq;
    
    /** Creates a new instance of MassChangeDataBean */
    public MassChangeDataBean() {
    }
    
    /** Getter for property identificationNo.
     * @return Value of property identificationNo.
     *
     */
    public java.lang.String getIdentificationNo() {
        return identificationNo;
    }
    
    /** Setter for property identificationNo.
     * @param identificationNo New value of property identificationNo.
     *
     */
    public void setIdentificationNo(java.lang.String identificationNo) {
        this.identificationNo = identificationNo;
    }
    
    /** Getter for property fieldData.
     * @return Value of property fieldData.
     *
     */
    public java.lang.String getFieldData() {
        return fieldData;
    }
    
    /** Setter for property fieldData.
     * @param fieldData New value of property fieldData.
     *
     */
    public void setFieldData(java.lang.String fieldData) {
        this.fieldData = fieldData;
    }
    
    /** Getter for property isCurrentSeq.
     * @return Value of property isCurrentSeq.
     *
     */
    public boolean isIsCurrentSeq() {
        return isCurrentSeq;
    }
    
    /** Setter for property isCurrentSeq.
     * @param isCurrentSeq New value of property isCurrentSeq.
     *
     */
    public void setIsCurrentSeq(boolean isCurrentSeq) {
        this.isCurrentSeq = isCurrentSeq;
    }
    
}
