/*
 * @(#)ProtocolCustomElementsInfoBean.java 1.0 03/13/03 10:45 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.customelements.bean.*;

import java.beans.*;
import java.sql.Timestamp;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 * The class used to hold the information of <code>Protocol Others</code>
 * which extends the PersonCustomElementsInfoBean.
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on July 17, 2003, 01:09 PM
 */

public class ProtocolCustomElementsInfoBean extends PersonCustomElementsInfoBean
                                        implements java.io.Serializable,IBaseDataBean {
    //holds the protocol number
    private String protocolNumber;

    //holds the sequence number
    private int sequenceNumber;
    
    public ProtocolCustomElementsInfoBean(){
        super();
    }
    
    public ProtocolCustomElementsInfoBean(CustomElementsInfoBean customElementsInfoBean){
        super(customElementsInfoBean);
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
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
