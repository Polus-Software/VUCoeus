/*
 * @(#)IBaseDataBean.java August 6, 2003, 12:00 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

/**
 * This class is used as a base interface for all beans. All beans should implement this interface.
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on August 6, 2003, 12:00 PM
 */
public interface IBaseDataBean {
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType();
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType);
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber();
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber);    
}
