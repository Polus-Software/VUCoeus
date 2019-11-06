/*
 * ProtocolAlterDatabaseSearchBean.java
 *
 * Created on December 7, 2010, 10:21 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author mdehtesham
 */
public class ProtocolAlterDatabaseSearchBean implements java.io.Serializable, IBaseDataBean, BaseBean{
    
    private  String protocolNumber;
    private int sequenceNumber;
    private String acType;
    private String description;      
    private int databaseSearchedCode;   
    private PropertyChangeSupport propertySupport;
    private boolean selected;
    /** Creates a new instance of ProtocolAlternativeSearchBean */
    public ProtocolAlterDatabaseSearchBean() {
         propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * Method used to get protocolNumber
     * @return protocolNumber
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to get protocolNumber
     * @param protocolNumber
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method used to get acType
     * @return acType
     */
    public String getAcType() {
        return acType;
    }

    /**
     * Method used to set acType
     * @param acType
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }

    /**
     * Method used to get sequenceNumber
     * @return sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set sequenceNumber
     * @param sequenceNumber
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Method used to get databaseSearchedCode
     * @return databaseSearchedCode
     */
    public int getDatabaseSearchedCode() {
        return databaseSearchedCode;
    }

    /**
     * Method used to set databaseSearchedCode
     * @param databaseSearchedCode
     */
    public void setDatabaseSearchedCode(int databaseSearchedCode) {
        this.databaseSearchedCode = databaseSearchedCode;
    }

    /**
     * Method used to get description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method used to set description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }       

    /**
     * Method used to get checkboxFlag
     * @return selected boolean value
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Method used to set checkboxFlag
     * @param selected boolean value
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
