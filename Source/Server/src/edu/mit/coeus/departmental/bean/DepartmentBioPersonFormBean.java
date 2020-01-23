/*
 * @(#)DepartmentBioPersonFormBean.java 1.0 03/31/03 12:05 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;
import java.sql.Timestamp;

/**
 * The class used to hold the information of <code>Department Biography</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 31, 2003, 12:05 PM
 */

public class DepartmentBioPersonFormBean implements java.io.Serializable {
    
    private static final String PROP_DESCRIPTION ="description";
    private static final String PROP_BIO_NUMBER ="bioNumber";
    //holds the person id
    private String personId;
    //holds the description
    private String description;
    //holds the bio number
    private int bioNumber;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    //holds whether Source word document is present
    private boolean hasBioSource;
    //holds whether PDF document is present
    private boolean hasBioPDF;
    //holds departmentBioPDFPersonFormBean
    private DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean;
    //holds departmentBioSourceFormBean
    private DepartmentBioSourceFormBean departmentBioSourceFormBean;
    //Case 2793:NOW Person Maintainer - Uploading documents
    private int documentTypeCode;
    private String documentTypeDescription;
    //2793 End
    private PropertyChangeSupport propertySupport;
    
    /** Creates new DepartmentBioPerson */
    public DepartmentBioPersonFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public int getBioNumber() {
        return bioNumber;
    }
    
    public void setBioNumber(int newBioNumber) {
        int oldBioNumber = bioNumber;
        this.bioNumber = newBioNumber;
        propertySupport.firePropertyChange(PROP_BIO_NUMBER,
        oldBioNumber, bioNumber);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String newDescription) {
        String oldDescription = description;
        this.description = newDescription;
        propertySupport.firePropertyChange(PROP_DESCRIPTION,
        oldDescription, description);
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    public String getAcType() {
        return acType;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /** Getter for property hasBioSource.
     * @return Value of property hasBioSource.
     */
    public boolean isHasBioSource() {
        return hasBioSource;
    }
    
    /** Setter for property hasBioSource.
     * @param hasBioSource New value of property hasBioSource.
     */
    public void setHasBioSource(boolean hasBioSource) {
        this.hasBioSource = hasBioSource;
    }
    
    /** Getter for property hasBioPDF.
     * @return Value of property hasBioPDF.
     */
    public boolean isHasBioPDF() {
        return hasBioPDF;
    }
    
    /** Setter for property hasBioPDF.
     * @param hasBioPDF New value of property hasBioPDF.
     */
    public void setHasBioPDF(boolean hasBioPDF) {
        this.hasBioPDF = hasBioPDF;
    }
    
    /** Getter for property departmentBioPDFPersonFormBean.
     * @return Value of property departmentBioPDFPersonFormBean.
     */
    public edu.mit.coeus.departmental.bean.DepartmentBioPDFPersonFormBean getDepartmentBioPDFPersonFormBean() {
        return departmentBioPDFPersonFormBean;
    }
    
    /** Setter for property departmentBioPDFPersonFormBean.
     * @param departmentBioPDFPersonFormBean New value of property departmentBioPDFPersonFormBean.
     */
    public void setDepartmentBioPDFPersonFormBean(edu.mit.coeus.departmental.bean.DepartmentBioPDFPersonFormBean departmentBioPDFPersonFormBean) {
        this.departmentBioPDFPersonFormBean = departmentBioPDFPersonFormBean;
    }
    
    /** Getter for property departmentBioSourceFormBean.
     * @return Value of property departmentBioSourceFormBean.
     */
    public edu.mit.coeus.departmental.bean.DepartmentBioSourceFormBean getDepartmentBioSourceFormBean() {
        return departmentBioSourceFormBean;
    }
    
    /** Setter for property departmentBioSourceFormBean.
     * @param departmentBioSourceFormBean New value of property departmentBioSourceFormBean.
     */
    public void setDepartmentBioSourceFormBean(edu.mit.coeus.departmental.bean.DepartmentBioSourceFormBean departmentBioSourceFormBean) {
        this.departmentBioSourceFormBean = departmentBioSourceFormBean;
    }
    //Case 2793:NOW Person Maintainer - Uploading documents
    public void setDocumentTypeCode(int documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeDescription() {
        return documentTypeDescription;
    }

    public void setDocumentTypeDescription(String documentTypeDescription) {
        this.documentTypeDescription = documentTypeDescription;
    }

    public int getDocumentTypeCode() {
        return documentTypeCode;
    }
    //2793 End
}