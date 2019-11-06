/*
 * @(#)DepartmentBioPDFPersonFormBean.java 1.0 03/31/03 12:15 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.departmental.bean;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import java.beans.*;
import java.sql.Timestamp;
/**
 * The class used to hold the information of <code>Department Biography PDF</code>
 * which extends DepartmentBioPersonFormBean
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 31, 2003, 12:15 PM
 */

public class DepartmentBioPDFPersonFormBean extends CoeusAttachmentBean
                                        implements java.io.Serializable {
    
    private static final String PROP_FILE_NAME ="fileName";
    //Added on 23 March, 2004 - start
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
    //Added on 23 March, 2003 - end
    //holds the file name
    private String fileName;
    
    private PropertyChangeSupport propertySupport;
    
    //holds file data
//    private byte[] fileBytes;
    
    /** Creates new DepartmentBioPDFPersonFormBean */
    public DepartmentBioPDFPersonFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String newFileName) {
        String oldFileName = fileName;
        this.fileName = newFileName;
        propertySupport.firePropertyChange(PROP_FILE_NAME,
        oldFileName, fileName);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    //Commented with case 4007:Icon based on mime type
    /** Getter for property fileBytes.
     * @return Value of property fileBytes.
     */
//    public byte[] getFileBytes() {
//        return this.fileBytes;
//    }
    
    /** Setter for property fileBytes.
     * @param fileBytes New value of property fileBytes.
     */
//    public void setFileBytes(byte[] fileBytes) {
//        this.fileBytes = fileBytes;
//    }    
    //4007 End
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
    
}
