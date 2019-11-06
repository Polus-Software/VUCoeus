/*
 * AwardTemplateBean.java
 *
 * Created on December 16, 2004, 4:20 PM
 */

package edu.mit.coeus.admin.bean;


/**
 *
 * @author  bijosht
 */
public class AwardTemplateBean extends TemplateBaseBean {
    
    private int statusCode;
    private String description;
    private String primeSponsorCode;
    private int nonCompetingContPrpslDue;
    private int competingRenewalPrpslDue;
    private int basisOfPaymentCode;
    private int methodOfPaymentCode;
    private int paymentInvoiceFreqCode;
    private int invoiceNoOfCopies;  
    private Integer finalInvoiceDue;
    
    //holds Invoice Instructions
    private String invoiceInstructions;
    private char syncIndicator; // 'C' - Contacts, 'T' - Terms, 'R' - Reports, 'M' - Comments
    private String statusDescription; // Added by Jobin to set the status description - (Award Template)
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start    
    private String updateUserName;
    //COEUSQA-1456 : End
    /** Creates a new instance of AwardTemplateBean */
    public AwardTemplateBean() {
    }
    
    // Equals implementation
    public boolean equals(Object obj) {
        AwardTemplateBean awardTemplateBean= (AwardTemplateBean)obj;
        if(awardTemplateBean.getTemplateCode() == getTemplateCode()) {
            return true;
        }else{
            return false;
        }
    }
    /**
     * Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }    
   
    /**
     * Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }    
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property primeSponsorCode.
     * @return Value of property primeSponsorCode.
     */
    public java.lang.String getPrimeSponsorCode() {
        return primeSponsorCode;
    }
    
    /**
     * Setter for property primeSponsorCode.
     * @param primeSponsorCode New value of property primeSponsorCode.
     */
    public void setPrimeSponsorCode(java.lang.String primeSponsorCode) {
        this.primeSponsorCode = primeSponsorCode;
    }
    
    /**
     * Getter for property nonCompetingContPrpslDue.
     * @return Value of property nonCompetingContPrpslDue.
     */
    public int getNonCompetingContPrpslDue() {
        return nonCompetingContPrpslDue;
    }
    
    /**
     * Setter for property nonCompetingContPrpslDue.
     * @param nonCompetingContPrpslDue New value of property nonCompetingContPrpslDue.
     */
    public void setNonCompetingContPrpslDue(int nonCompetingContPrpslDue) {
        this.nonCompetingContPrpslDue = nonCompetingContPrpslDue;
    }
    
    /**
     * Getter for property competingRenewalPrpslDue.
     * @return Value of property competingRenewalPrpslDue.
     */
    public int getCompetingRenewalPrpslDue() {
        return competingRenewalPrpslDue;
    }
    
    /**
     * Setter for property competingRenewalPrpslDue.
     * @param competingRenewalPrpslDue New value of property competingRenewalPrpslDue.
     */
    public void setCompetingRenewalPrpslDue(int competingRenewalPrpslDue) {
        this.competingRenewalPrpslDue = competingRenewalPrpslDue;
    }
    
    /**
     * Getter for property basisOfPaymentCode.
     * @return Value of property basisOfPaymentCode.
     */
    public int getBasisOfPaymentCode() {
        return basisOfPaymentCode;
    }
    
    /**
     * Setter for property basisOfPaymentCode.
     * @param basisOfPaymentCode New value of property basisOfPaymentCode.
     */
    public void setBasisOfPaymentCode(int basisOfPaymentCode) {
        this.basisOfPaymentCode = basisOfPaymentCode;
    }
    
    /**
     * Getter for property methodOfPaymentCode.
     * @return Value of property methodOfPaymentCode.
     */
    public int getMethodOfPaymentCode() {
        return methodOfPaymentCode;
    }
    
    /**
     * Setter for property methodOfPaymentCode.
     * @param methodOfPaymentCode New value of property methodOfPaymentCode.
     */
    public void setMethodOfPaymentCode(int methodOfPaymentCode) {
        this.methodOfPaymentCode = methodOfPaymentCode;
    }
    
    /**
     * Getter for property paymentInvoiceFreqCode.
     * @return Value of property paymentInvoiceFreqCode.
     */
    public int getPaymentInvoiceFreqCode() {
        return paymentInvoiceFreqCode;
    }
    
    /**
     * Setter for property paymentInvoiceFreqCode.
     * @param paymentInvoiceFreqCode New value of property paymentInvoiceFreqCode.
     */
    public void setPaymentInvoiceFreqCode(int paymentInvoiceFreqCode) {
        this.paymentInvoiceFreqCode = paymentInvoiceFreqCode;
    }
    
    /**
     * Getter for property invoiceInstructions.
     * @return Value of property invoiceInstructions.
     */
    public java.lang.String getInvoiceInstructions() {
        return invoiceInstructions;
    }
    
    /**
     * Setter for property invoiceInstructions.
     * @param invoiceInstructions New value of property invoiceInstructions.
     */
    public void setInvoiceInstructions(java.lang.String invoiceInstructions) {
        this.invoiceInstructions = invoiceInstructions;
    }
    
    /**
     * Getter for property syncIndicator.
     * @return Value of property syncIndicator.
     */
    public char getSyncIndicator() {
        return syncIndicator;
    }
    
    /**
     * Setter for property syncIndicator.
     * @param syncIndicator New value of property syncIndicator.
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
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }
    
    /**
     * Getter for property invoiceNoOfCopies.
     * @return Vale of property invoiceNoOfCopies.
     */
    public int getInvoiceNoOfCopies() {
        return invoiceNoOfCopies;
    }
    
    /**
     * Setter for property invoiceNoOfCopies.
     * @param invoiceNoOfCopies New value of property invoiceNoOfCopies.
     */
    public void setInvoiceNoOfCopies(int invoiceNoOfCopies) {
        this.invoiceNoOfCopies = invoiceNoOfCopies;
    }
    
    /**
     * Getter for property finalInvoiceDue.
     * @return Value of property finalInvoiceDue.
     */
    public java.lang.Integer getFinalInvoiceDue() {
        return finalInvoiceDue;
    }
    
    /**
     * Setter for property finalInvoiceDue.
     * @param finalInvoiceDue New value of property finalInvoiceDue.
     */
    public void setFinalInvoiceDue(java.lang.Integer finalInvoiceDue) {
        this.finalInvoiceDue = finalInvoiceDue;
    }
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
     /**
     * Getter for property updateUser.
     * @return updateUser.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //COEUSQA-1456 : End
}
