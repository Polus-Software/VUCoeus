/*
 * ProtocolPrinciplesBean.java
 *
 * Created on March 19, 2010, 4:48 PM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author sreenathv
 */
public class ProtocolPrinciplesBean implements java.io.Serializable, IBaseDataBean {
    
    private  String protocolNumber;
    private int sequenceNumber;
    private String acType;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    private String principleOfReduction;
    private String principleOfReplacement;
    private String principleOfRefinement;
    private boolean exceptionPresent;
    
    private PropertyChangeSupport propertySupport;
    private static final String PRINCIPLE_OF_REDUCTION = "principleOfReduction";
    private static final String PRINCIPLE_OF_REPLACAMENT = "principleOfReplacement";
    private static final String PRINCIPLE_OF_REFINEMENT = "principleOfRefinement";
    private static final String EXCEPTION_PRESENT = "exceptionPresent";
    
    /** Creates a new instance of ProtocolPrinciplesBean */
    public ProtocolPrinciplesBean() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
     /**
    * Method used to add propertychange listener to the fields
    * @param listener PropertyChangeListener
    */
   public void addPropertyChangeListener(PropertyChangeListener listener) {
       propertySupport.addPropertyChangeListener(listener);
   }
   
   /**
    * Method used to remove propertychange listener
    * @param listener PropertyChangeListener
    */
   public void removePropertyChangeListener(PropertyChangeListener listener) {
       propertySupport.removePropertyChangeListener(listener);
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

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getPrincipleOfReduction() {
        return principleOfReduction;
    }

    public void setPrincipleOfReduction(String newPrincipleOfReduction) {
        String oldPrincipleOfReduction = principleOfReduction;
        this.principleOfReduction = newPrincipleOfReduction;
        propertySupport.firePropertyChange(PRINCIPLE_OF_REDUCTION, oldPrincipleOfReduction, principleOfReduction);
    }

    public String getPrincipleOfReplacement() {
        return principleOfReplacement;
    }

    public void setPrincipleOfReplacement(String newPrincipleOfReplacement) {
        String oldPrincipleOfReplacement = principleOfReplacement;
        this.principleOfReplacement = newPrincipleOfReplacement;
        propertySupport.firePropertyChange(PRINCIPLE_OF_REPLACAMENT, oldPrincipleOfReplacement, principleOfReplacement);
    }

    public String getPrincipleOfRefinement() {
        return principleOfRefinement;
    }

    public void setPrincipleOfRefinement(String newPrincipleOfRefinement) {
        String oldPrincipleOfRefinement = principleOfRefinement;
        this.principleOfRefinement = newPrincipleOfRefinement;
        propertySupport.firePropertyChange(PRINCIPLE_OF_REFINEMENT, oldPrincipleOfRefinement, principleOfRefinement);
    }

    public boolean isExceptionPresent() {
        return exceptionPresent;
    }

    public void setExceptionPresent(boolean newExceptionPresent) {
        boolean oldExceptionPresent = exceptionPresent;
        this.exceptionPresent = newExceptionPresent;
        propertySupport.firePropertyChange(EXCEPTION_PRESENT, oldExceptionPresent, exceptionPresent);
    }   
}
