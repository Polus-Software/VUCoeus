/*
 * BudgetSubAwardBean.java
 *
 * Created on May 19, 2006, 2:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author sharathk
 */
public class BudgetSubAwardBean extends BudgetBean {
    private int subAwardNumber;
    private int awSubAwardNumber;
    private String OrganizationName;
    private int subAwardStatusCode;
    private byte subAwardPDF[];
    private String pdfUpdateUser;
    private Timestamp pdfUpdateTimestamp;
    private String pdfAcType;
    private String pdfFileName;
    private String comments;
    private char subAwardXML[];
    private String xmlUpdateUser;
    private String xmlAcType;
    private Timestamp xmlUpdateTimestamp;
    private String translationComments;
    private String namespace;
    private String formName;
    
    private List attachments;
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    private Vector vecSubAwardPeriodDetails;
    private Vector vecPDFSubAwardPeriodDetails;
    private String awOrganizationName;
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    /** Creates a new instance of BudgetSubAwardBean */
    public BudgetSubAwardBean() {
    }
    
    public int getSubAwardNumber() {
        return subAwardNumber;
    }
    
    public void setSubAwardNumber(int subAwardNumber) {
        this.subAwardNumber = subAwardNumber;
    }
    
    public String getOrganizationName() {
        return OrganizationName;
    }
    
    public void setOrganizationName(String OrganizationName) {
        this.OrganizationName = OrganizationName;
    }
    
    // JM 6-25-2014 methods to handle organization id
    private String organizationId;
    
    public String getOrganizationId() {
    	return organizationId;
    }
    
    public void setOrganizationId(String organizationId) {
    	this.organizationId = organizationId;
    }
    // JM END
    
    // JM 5-23-2016 methods to handle location type code
    private int locationTypeCode;
    
    public int getLocationTypeCode() {
    	return locationTypeCode;
    }
    
    public void setLocationTypeCode(int locationTypeCode) {
    	this.locationTypeCode = locationTypeCode;
    }
    // JM END
    
    public int getSubAwardStatusCode() {
        return subAwardStatusCode;
    }
    
    public void setSubAwardStatusCode(int subAwardStatusCode) {
        this.subAwardStatusCode = subAwardStatusCode;
    }
    
    public byte[] getSubAwardPDF() {
        return subAwardPDF;
    }
    
    public void setSubAwardPDF(byte subAwardPDF[]) {
        this.subAwardPDF = subAwardPDF;
    }
    
    public String getPdfUpdateUser() {
        return pdfUpdateUser;
    }
    
    public void setPdfUpdateUser(String pdfUpdateUser) {
        this.pdfUpdateUser = pdfUpdateUser;
    }
    
    public Timestamp getPdfUpdateTimestamp() {
        return pdfUpdateTimestamp;
    }
    
    public void setPdfUpdateTimestamp(Timestamp pdfUpdateTimestamp) {
        this.pdfUpdateTimestamp = pdfUpdateTimestamp;
    }
    
    public char[] getSubAwardXML() {
        return subAwardXML;
    }
    
    public void setSubAwardXML(char subAwardXML[]) {
        this.subAwardXML = subAwardXML;
    }
    
    public String getXmlUpdateUser() {
        return xmlUpdateUser;
    }
    
    public void setXmlUpdateUser(String xmlUpdateUser) {
        this.xmlUpdateUser = xmlUpdateUser;
    }
    
    public Timestamp getXmlUpdateTimestamp() {
        return xmlUpdateTimestamp;
    }
    
    public void setXmlUpdateTimestamp(Timestamp xmlUpdateTimestamp) {
        this.xmlUpdateTimestamp = xmlUpdateTimestamp;
    }
    
    public boolean isLike(ComparableBean comparableBean)
    throws CoeusException {
        throw new CoeusException("Do Not Use isLike use QueryEngine.filter instead");
    }
    
    public String getPdfAcType() {
        return pdfAcType;
    }
    
    public void setPdfAcType(String pdfAcType) {
        this.pdfAcType = pdfAcType;
    }
    
    public String getPdfFileName() {
        return pdfFileName;
    }
    
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getTranslationComments() {
        return translationComments;
    }
    
    public void setTranslationComments(String translationComments) {
        this.translationComments = translationComments;
    }
    
    public int getAwSubAwardNumber() {
        return awSubAwardNumber;
    }
    
    public void setAwSubAwardNumber(int awSubAwardNumber) {
        this.awSubAwardNumber = awSubAwardNumber;
    }

    public String getXmlAcType() {
        return xmlAcType;
    }

    public void setXmlAcType(String xmlAcType) {
        this.xmlAcType = xmlAcType;
    }

    public List getAttachments() {
        return attachments;
    }

    public void setAttachments(List attachments) {
        this.attachments = attachments;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @return the formName
     */
    public String getFormName() {
        return formName;
    }

    /**
     * @param formName the formName to set
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }
    
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /**
     * Method to get the sub award period details
     * @return vecSubAwardPeriodDetails
     */
    public Vector getSubAwardPeriodDetails() {
        return vecSubAwardPeriodDetails;
    }
    
    /**
     * Method to set the sub award period details
     * @param vecSubAwardPeriodDetails 
     */
    public void setSubAwardPeriodDetails(Vector vecSubAwardPeriodDetails) {
        this.vecSubAwardPeriodDetails = vecSubAwardPeriodDetails;
    }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End

    /**
     * Method to get aworaganizationName
     * @return awOrganizationName
     */
    public String getAwOrganizationName() {
        return awOrganizationName;
    }

    /**
     * Method to set aworaganizationName
     * @param awOrganizationName 
     */
    public void setAwOrganizationName(String awOrganizationName) {
        this.awOrganizationName = awOrganizationName;
    }

    /**
     * Method to get the PDF Sub award period details
     * @return vecPDFSubAwardPeriodDetails
     */
    public Vector getPDFSubAwardPeriodDetails() {
        return vecPDFSubAwardPeriodDetails;
    }

    /**
     * Method to set the PDF Sub award period details
     * @param vecPDFSubAwardPeriodDetails 
     */
    public void setPDFSubAwardPeriodDetails(Vector vecPDFSubAwardPeriodDetails) {
        this.vecPDFSubAwardPeriodDetails = vecPDFSubAwardPeriodDetails;
    }
}