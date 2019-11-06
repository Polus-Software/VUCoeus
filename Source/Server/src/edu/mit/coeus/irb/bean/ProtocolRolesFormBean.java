/*
 * ProtocolRolesFormBean.java
 *
 * Created on April 11, 2003, 4:46 PM
 */

package edu.mit.coeus.irb.bean;

import java.beans.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 *
 * @author  mukund
 */
public class ProtocolRolesFormBean extends RoleBean implements IBaseDataBean {

    private String protocolNumber;
    private int sequenceNumber;
    // COEUSDEV-273: Protocol roles update error - new se & save  
    // to hold the sequence number in which the user was added as a protocol user.
    private int awSequenceNumber;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolRolesFormBean */
    public ProtocolRolesFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public ProtocolRolesFormBean ( RoleBean roleBean ) {
        super( roleBean );
        propertySupport = new PropertyChangeSupport( this );
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
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
    
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public int getAwSequenceNumber() {
        return awSequenceNumber;
    }

    public void setAwSequenceNumber(int awSequenceNumber) {
        this.awSequenceNumber = awSequenceNumber;
    }
}
