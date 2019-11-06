/**
 * @(#)TemplateBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.ComboBoxBean;
/**
 * This class is used to hold Template data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class TemplateBean extends ComboBoxBean implements CoeusBean, java.io.Serializable{
    
    private int statusCode;
    private String primeSponsorCode;
    private int nonCompetingContPrpslDue;
    private int competingRenewalPrpslDue;
    private int basisOfPaymentCode;
    private int methodOfPaymentCode;
    private int paymentInvoiceFreqCode;
    private int invoiceNumberOfCopies;  
    private int finalInvoiceDue;
    private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;
    private String acType = null;    
    //holds Invoice Instructions
    private String invoiceInstructions;
    private char syncIndicator; // 'C' - Contacts, 'T' - Terms, 'R' - Reports, 'M' - Comments
    private String statusDescription; // Added by Jobin to set the status description - (Award Template)
    private int tempCode;// Added by Jobin to set the template code - (Award Template)
	/**
     *	Default Constructor
     */
    
    public TemplateBean(){
    }
    
    public boolean isLike(ComparableBean comparableBean)
    throws CoeusException{
        return true;
    }    
    
    /** Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /** Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    /** Getter for property primeSponsorCode.
     * @return Value of property primeSponsorCode.
     */
    public java.lang.String getPrimeSponsorCode() {
        return primeSponsorCode;
    }
    
    /** Setter for property primeSponsorCode.
     * @param primeSponsorCode New value of property primeSponsorCode.
     */
    public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
        this.primeSponsorCode = primeSponsorCode;
    }
    
    /** Getter for property nonCompetingContPrpslDue.
     * @return Value of property nonCompetingContPrpslDue.
     */
    public int getNonCompetingContPrpslDue() {
        return nonCompetingContPrpslDue;
    }
    
    /** Setter for property nonCompetingContPrpslDue.
     * @param nonCompetingContPrpslDue New value of property nonCompetingContPrpslDue.
     */
    public void setNonCompetingContPrpslDue(int nonCompetingContPrpslDue) {
        this.nonCompetingContPrpslDue = nonCompetingContPrpslDue;
    }
    
    /** Getter for property competingRenewalPrpslDue.
     * @return Value of property competingRenewalPrpslDue.
     */
    public int getCompetingRenewalPrpslDue() {
        return competingRenewalPrpslDue;
    }
    
    /** Setter for property competingRenewalPrpslDue.
     * @param competingRenewalPrpslDue New value of property competingRenewalPrpslDue.
     */
    public void setCompetingRenewalPrpslDue(int competingRenewalPrpslDue) {
        this.competingRenewalPrpslDue = competingRenewalPrpslDue;
    }
    
    /** Getter for property basisOfPaymentCode.
     * @return Value of property basisOfPaymentCode.
     */
    public int getBasisOfPaymentCode() {
        return basisOfPaymentCode;
    }
    
    /** Setter for property basisOfPaymentCode.
     * @param basisOfPaymentCode New value of property basisOfPaymentCode.
     */
    public void setBasisOfPaymentCode(int basisOfPaymentCode) {
        this.basisOfPaymentCode = basisOfPaymentCode;
    }
    
    /** Getter for property methodOfPaymentCode.
     * @return Value of property methodOfPaymentCode.
     */
    public int getMethodOfPaymentCode() {
        return methodOfPaymentCode;
    }
    
    /** Setter for property methodOfPaymentCode.
     * @param methodOfPaymentCode New value of property methodOfPaymentCode.
     */
    public void setMethodOfPaymentCode(int methodOfPaymentCode) {
        this.methodOfPaymentCode = methodOfPaymentCode;
    }
    
    /** Getter for property paymentInvoiceFreqCode.
     * @return Value of property paymentInvoiceFreqCode.
     */
    public int getPaymentInvoiceFreqCode() {
        return paymentInvoiceFreqCode;
    }
    
    /** Setter for property paymentInvoiceFreqCode.
     * @param paymentInvoiceFreqCode New value of property paymentInvoiceFreqCode.
     */
    public void setPaymentInvoiceFreqCode(int paymentInvoiceFreqCode) {
        this.paymentInvoiceFreqCode = paymentInvoiceFreqCode;
    }
    
    /** Getter for property invoiceNumberOfCopies.
     * @return Value of property invoiceNumberOfCopies.
     */
    public int getInvoiceNumberOfCopies() {
        return invoiceNumberOfCopies;
    }
    
    /** Setter for property invoiceNumberOfCopies.
     * @param invoiceNumberOfCopies New value of property invoiceNumberOfCopies.
     */
    public void setInvoiceNumberOfCopies(int invoiceNumberOfCopies) {
        this.invoiceNumberOfCopies = invoiceNumberOfCopies;
    }
    
    /** Getter for property finalInvoiceDue.
     * @return Value of property finalInvoiceDue.
     */
    public int getFinalInvoiceDue() {
        return finalInvoiceDue;
    }
    
    /** Setter for property finalInvoiceDue.
     * @param finalInvoiceDue New value of property finalInvoiceDue.
     */
    public void setFinalInvoiceDue(int finalInvoiceDue) {
        this.finalInvoiceDue = finalInvoiceDue;
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
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /** Getter for property invoiceInstructions.
     * @return Value of property invoiceInstructions.
     */
    public java.lang.String getInvoiceInstructions() {
        return invoiceInstructions;
    }
    
    /** Setter for property invoiceInstructions.
     * @param invoiceInstructions New value of property invoiceInstructions.
     */
    public void setInvoiceInstructions(java.lang.String invoiceInstructions) {
        this.invoiceInstructions = invoiceInstructions;
    }
    
    /** Getter for property syncIndicator.
     * @return Value of property syncIndicator.
     *
     */
    public char getSyncIndicator() {
        return syncIndicator;
    }
    
    /** Setter for property syncIndicator.
     * @param syncIndicator New value of property syncIndicator.
     *
     */
    public void setSyncIndicator(char syncIndicator) {
        this.syncIndicator = syncIndicator;
    }
    
	/**
	 * Getter for property statusDescription.
	 * @return Value of property statusDescription.
	 */
	public java.lang.String getStatusDescription() {
		return statusDescription;
	}
	
	/**
	 * Setter for property statusDescription.
	 * @param statusDescription New value of property statusDescription.
	 */
	public void setStatusDescription(java.lang.String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
	/**
	 * Getter for property tempCode.
	 * @return Value of property tempCode.
	 */
	public int getTempCode() {
		return tempCode;
	}
	
	/**
	 * Setter for property tempCode.
	 * @param tempCode New value of property tempCode.
	 */
	public void setTempCode(int tempCode) {
		this.tempCode = tempCode;
	}
        
        // Equals implementation
       /* public boolean equals(Object obj) {
            TemplateBean templateBean= (TemplateBean)obj;
            if(templateBean.getTempCode() == getTempCode()) {
                return true;
            }else{
                return false;
            }
        }*/
	
}