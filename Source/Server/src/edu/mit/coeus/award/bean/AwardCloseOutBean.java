/**
 * @(#)AwardCloseOutBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Close Out data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardCloseOutBean extends AwardBaseBean{
    
    private Date finalInvSubmissionDate;
    private Date finalTechSubmissionDate;
    private Date finalPatentSubmissionDate;
    private Date finalPropSubmissionDate;    
    private String archiveLocation;
    private Date closeOutDate;
    private String invoiceDueDate;
    private String technicalDueDate;    
    private String patentDueDate;
    private String propertyDueDate;
    
//    private Date invoiceDueDate;
//    private Date technicalDueDate;
//    private Date patentDueDate;
//    private Date propertyDueDate;
//    private boolean invoiceDueDateMultipe;
//    private boolean technicalDueDateMultiple;
//    private boolean patentDueDateMultiple;
//    private boolean propertyDueDateMultiple;
    
    /**
     *	Default Constructor
     */
    
    public AwardCloseOutBean(){
    }    
    
    /** Getter for property finalInvSubmissionDate.
     * @return Value of property finalInvSubmissionDate.
     */
    public java.sql.Date getFinalInvSubmissionDate() {
        return finalInvSubmissionDate;
    }    
    
    /** Setter for property finalInvSubmissionDate.
     * @param finalInvSubmissionDate New value of property finalInvSubmissionDate.
     */
    public void setFinalInvSubmissionDate(java.sql.Date finalInvSubmissionDate) {
        this.finalInvSubmissionDate = finalInvSubmissionDate;
    }    
    
    /** Getter for property finalTechSubmissionDate.
     * @return Value of property finalTechSubmissionDate.
     */
    public java.sql.Date getFinalTechSubmissionDate() {
        return finalTechSubmissionDate;
    }
    
    /** Setter for property finalTechSubmissionDate.
     * @param finalTechSubmissionDate New value of property finalTechSubmissionDate.
     */
    public void setFinalTechSubmissionDate(java.sql.Date finalTechSubmissionDate) {
        this.finalTechSubmissionDate = finalTechSubmissionDate;
    }
    
    /** Getter for property finalPatentSubmissionDate.
     * @return Value of property finalPatentSubmissionDate.
     */
    public java.sql.Date getFinalPatentSubmissionDate() {
        return finalPatentSubmissionDate;
    }
    
    /** Setter for property finalPatentSubmissionDate.
     * @param finalPatentSubmissionDate New value of property finalPatentSubmissionDate.
     */
    public void setFinalPatentSubmissionDate(java.sql.Date finalPatentSubmissionDate) {
        this.finalPatentSubmissionDate = finalPatentSubmissionDate;
    }
    
    /** Getter for property finalPropSubmissionDate.
     * @return Value of property finalPropSubmissionDate.
     */
    public java.sql.Date getFinalPropSubmissionDate() {
        return finalPropSubmissionDate;
    }
    
    /** Setter for property finalPropSubmissionDate.
     * @param finalPropSubmissionDate New value of property finalPropSubmissionDate.
     */
    public void setFinalPropSubmissionDate(java.sql.Date finalPropSubmissionDate) {
        this.finalPropSubmissionDate = finalPropSubmissionDate;
    }
    
    /** Getter for property archiveLocation.
     * @return Value of property archiveLocation.
     */
    public java.lang.String getArchiveLocation() {
        return archiveLocation;
    }
    
    /** Setter for property archiveLocation.
     * @param archiveLocation New value of property archiveLocation.
     */
    public void setArchiveLocation(java.lang.String archiveLocation) {
        this.archiveLocation = archiveLocation;
    }
    
    /** Getter for property closeOutDate.
     * @return Value of property closeOutDate.
     */
    public java.sql.Date getCloseOutDate() {
        return closeOutDate;
    }
    
    /** Setter for property closeOutDate.
     * @param closeOutDate New value of property closeOutDate.
     */
    public void setCloseOutDate(java.sql.Date closeOutDate) {
        this.closeOutDate = closeOutDate;
    }       
    
    /** Getter for property invoiceDueDate.
     * @return Value of property invoiceDueDate.
     *
     */
    public java.lang.String getInvoiceDueDate() {
        return invoiceDueDate;
    }
    
    /** Setter for property invoiceDueDate.
     * @param invoiceDueDate New value of property invoiceDueDate.
     *
     */
    public void setInvoiceDueDate(java.lang.String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }
    
    /** Getter for property technicalDueDate.
     * @return Value of property technicalDueDate.
     *
     */
    public java.lang.String getTechnicalDueDate() {
        return technicalDueDate;
    }
    
    /** Setter for property technicalDueDate.
     * @param technicalDueDate New value of property technicalDueDate.
     *
     */
    public void setTechnicalDueDate(java.lang.String technicalDueDate) {
        this.technicalDueDate = technicalDueDate;
    }
    
    /** Getter for property patentDueDate.
     * @return Value of property patentDueDate.
     *
     */
    public java.lang.String getPatentDueDate() {
        return patentDueDate;
    }
    
    /** Setter for property patentDueDate.
     * @param patentDueDate New value of property patentDueDate.
     *
     */
    public void setPatentDueDate(java.lang.String patentDueDate) {
        this.patentDueDate = patentDueDate;
    }
    
    /** Getter for property propertyDueDate.
     * @return Value of property propertyDueDate.
     *
     */
    public java.lang.String getPropertyDueDate() {
        return propertyDueDate;
    }
    
    /** Setter for property propertyDueDate.
     * @param propertyDueDate New value of property propertyDueDate.
     *
     */
    public void setPropertyDueDate(java.lang.String propertyDueDate) {
        this.propertyDueDate = propertyDueDate;
    }
    
}