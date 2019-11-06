/*
 * ProtocolBaseActionForm.java
 *
 * Created on March 29, 2005, 11:08 AM
 */

package edu.mit.coeuslite.iacuc.form;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  chandrashekara
 */
public abstract class ProtocolBaseActionForm extends ActionForm{
    
    private String protocolNumber; //= "0311000014";
    private int sequenceNumber; //= 1;
    private String acType;
    private String mode;
    
    /** Creates a new instance of ProtocolBaseActionForm */
    public ProtocolBaseActionForm() {
    }
    
    /**
     * Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /**
     * Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public java.lang.String getMode() {
        return mode;
    }
    
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }
    
    public abstract ActionErrors validate(ActionMapping mapping,HttpServletRequest req);
    
    /**
     * Reset all properties to their default values.
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public abstract void reset(ActionMapping mapping, HttpServletRequest request);
    
}
