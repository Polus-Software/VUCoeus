/*
 * @(#)DepartmentBioSourceFormBean.java 1.0 03/31/03 12:19 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;
import java.sql.Timestamp;
/**
 * The class used to hold the information of <code>Department Biography Source</code>
 * which extends DepartmentBioPDFPersonFormBean
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 31, 2003, 12:19 PM
 */

public class DepartmentBioSourceFormBean
                            implements java.io.Serializable {
    
    private static final String PROP_INPUT_TYPE ="inputType";
    private static final String PROP_PLATFORM_TYPE ="platformType";
    private static final String PROP_SOURCE_EDITOR ="sourceEditor";
    //Added on 23 March, 2004 - start
    private static final String PROP_FILE_NAME ="fileName";    
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
    //holds the file name
    private String fileName;    
    //holds file data
    private byte[] fileBytes;    
    //Added on 23 March, 2003 - end    
    //holds the source editor
    private String sourceEditor;
    //holds the input type
    private char inputType;
    //holds the platform type
    private char platformType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new DepartmentBioSourceFormBean */
    public DepartmentBioSourceFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getSourceEditor() {
        return sourceEditor;
    }
    
    public void setSourceEditor(String newSourceEditor) {
        String oldSourceEditor = sourceEditor;
        this.sourceEditor = newSourceEditor;
        propertySupport.firePropertyChange(PROP_SOURCE_EDITOR,
        oldSourceEditor, sourceEditor);
    }
    
    public char getInputType() {
        return inputType;
    }
    
    public void setInputType(char newInputType) {
        char oldInputType = inputType;
        this.inputType = newInputType;
        propertySupport.firePropertyChange(PROP_INPUT_TYPE,
        oldInputType, inputType);
    }
    
    public char getPlatformType() {
        return platformType;
    }
    
    public void setPlatformType(char newPlatformType) {
        char oldPlatformType = platformType;
        this.platformType = newPlatformType;
        propertySupport.firePropertyChange(PROP_PLATFORM_TYPE,
        oldPlatformType, platformType);
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
    
    /** Getter for property fileBytes.
     * @return Value of property fileBytes.
     */
    public byte[] getFileBytes() {
        return this.fileBytes;
    }
    
    /** Setter for property fileBytes.
     * @param fileBytes New value of property fileBytes.
     */
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
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
    
    
}
