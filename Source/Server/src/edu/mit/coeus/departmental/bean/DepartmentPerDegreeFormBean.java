/* 
 * @(#)DepartmentPerDegreeFormBean.java 1.0 03/31/03 3:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;
import java.sql.Date;
import java.sql.Timestamp;
/**
 * The class used to hold the information of <code>Department Degree</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 31, 2003, 3:17 PM
 */

public class DepartmentPerDegreeFormBean implements java.io.Serializable {
    
    private static final String PROP_GRAD_DATE ="graduationDate";
    private static final String PROP_FILED_STUDY ="fieldofStudy"; 
    private static final String PROP_SPECIALIZATION ="specialization";
    private static final String PROP_SCHOOL ="school"; 
    private static final String PROP_SCHOOL_CODE ="schoolIdCode";
    private static final String PROP_SCHOOL_ID ="schoolId"; 
     //holds the person id
     private String personId;
     //holds the degree code;
     private String degreeCode;
     //holds the degree code value
     private String awDegreeCode;
     //holds the degree value
     private String awDegree;
     //holds the degree description
     private String degreeDescription;
     //holds the graduation date
     private Date graduationDate;
     //holds the degree
     private String degree;
     //holds the field of study
     private String fieldofStudy;
     //holds the specialization
     private String specialization;
     //holds the school
     private String school;
     //holds the school id code
     private String schoolIdCode;
     //holds the school description
     private String schoolDescription;
     //holds the school id
     private String schoolId;
    //holds update user id
     private String updateUser;
     //holds update timestamp
     private Timestamp updateTimestamp;
     //holds account type
     private String acType;   
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new DepartmentPerDegreeFormBean */
    public DepartmentPerDegreeFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getDegreeCode() {
        return degreeCode;
    }

    public void setDegreeCode(String degreeCode) {
        this.degreeCode = degreeCode;
    }
    
    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date newGraduationDate) {
        Date oldGraduationDate = graduationDate;
        this.graduationDate = newGraduationDate;
        propertySupport.firePropertyChange(PROP_GRAD_DATE, 
                oldGraduationDate, graduationDate);
    }
    
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    public String getFieldOfStudy() {
        return fieldofStudy;
    }

    public void setFieldOfStudy(String newFieldofStudy) {
        String oldFieldofStudy = fieldofStudy;
        this.fieldofStudy = newFieldofStudy;
        propertySupport.firePropertyChange(PROP_FILED_STUDY, 
                oldFieldofStudy, fieldofStudy);
    }
    
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String newSpecialization) {
        String oldSpecialization = specialization;
        this.specialization = newSpecialization;
        propertySupport.firePropertyChange(PROP_SPECIALIZATION, 
                oldSpecialization, specialization);
    }
    
    public String getSchool() {
        return school;
    }

    public void setSchool(String newSchool) {
        String oldSchool = school;
        this.school = newSchool;
        propertySupport.firePropertyChange(PROP_SCHOOL, 
                oldSchool, school);
    }
    
    public String getSchoolIdCode() {
        return schoolIdCode;
    }

    public void setSchoolIdCode(String newSchoolIdCode) {
        String oldSchoolIdCode = schoolIdCode;
        this.schoolIdCode = newSchoolIdCode;
        propertySupport.firePropertyChange(PROP_SCHOOL_CODE, 
                oldSchoolIdCode, schoolIdCode);
    }
    
    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String newSchoolId) {
        String oldSchoolId = schoolId;
        this.schoolId = newSchoolId;
        propertySupport.firePropertyChange(PROP_SCHOOL_ID, 
                oldSchoolId, schoolId);

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
    
    /** Getter for property degreeDescription.
     * @return Value of property degreeDescription.
     */
    public java.lang.String getDegreeDescription() {
        return degreeDescription;
    }
    
    /** Setter for property degreeDescription.
     * @param degreeDescription New value of property degreeDescription.
     */
    public void setDegreeDescription(java.lang.String degreeDescription) {
        this.degreeDescription = degreeDescription;
    }
    
    /** Getter for property schoolDescription.
     * @return Value of property schoolDescription.
     */
    public java.lang.String getSchoolDescription() {
        return schoolDescription;
    }
    
    /** Setter for property schoolDescription.
     * @param schoolDescription New value of property schoolDescription.
     */
    public void setSchoolDescription(java.lang.String schoolDescription) {
        this.schoolDescription = schoolDescription;
    }
    
    /** Getter for property awDegreeCode.
     * @return Value of property awDegreeCode.
     */
    public java.lang.String getAwDegreeCode() {
        return awDegreeCode;
    }
    
    /** Setter for property awDegreeCode.
     * @param awDegreeCode New value of property awDegreeCode.
     */
    public void setAwDegreeCode(java.lang.String awDegreeCode) {
        this.awDegreeCode = awDegreeCode;
    }
    
    /** Getter for property awDegree.
     * @return Value of property awDegree.
     */
    public java.lang.String getAwDegree() {
        return awDegree;
    }
    
    /** Setter for property awDegree.
     * @param awDegree New value of property awDegree.
     */
    public void setAwDegree(java.lang.String awDegree) {
        this.awDegree = awDegree;
    }
    
}
