/*
 * ProtocolUserRoleInfoBean.java
 *
 * Created on August 2, 2009, 5:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.UserRolesInfoBean;
import edu.mit.coeus.iacuc.bean.*;

/**
 *
 * @author sreenathv
 */
public class ProtocolUserRoleInfoBean extends UserRolesInfoBean {
    
       private String protocolNumber;
    private int sequenceNumber;
    /** Creates a new instance of ProtocolUserRoleInfoBean */
    public ProtocolUserRoleInfoBean() {
    }
    

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    

    
}
