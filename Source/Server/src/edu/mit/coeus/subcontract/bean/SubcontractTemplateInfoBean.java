/*
 * SubcontractTemplateInfoBean.java
 *
 * Created on February 3, 2012, 9:34 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.subcontract.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author satheeshkumarkn
 */
public class SubcontractTemplateInfoBean extends SubContractBaseBean{
    
    private char sowOrSubProposalBudget;
    private java.sql.Date subProposalDate;
    private int invoiceOrPaymentContactTypeCode;
    private String invoiceOrPaymentContactTypeDesc;
    private int finalStmtOfCostsContactTypeCode;
    private String finalStmtOfCostsContactTypeDesc;
    private int changeRequestsContactTypeCode;
    private String changeRequestsContactTypeDesc;
    private int terminationContactTypeCode;
    private String terminationContactTypeDesc;
    private int noCostExtensionContactTypeCode;
    private String noCostExtensionContactTypeDesc;
    private char perfSiteDiffFromOrgAddr;
    private char perfSiteSameAsSubPiAddr;
    private char subRegisteredInCcr;
    private char subExemptFromReportingComp;
    private String parentDunsNumber;
    private String parentCongressionalDistrict;
    private char exemptFromRprtgExecComp;
    private int copyrightTypeCode;
    private String copyrightTypeDesc;
    private char automaticCarryForward;
    private int carryForwardRequestsSentTo;
    private String carryForwardRequestsSentToDesc;
    private char treatmentPrgmIncomeAdditive;
    private String applicableProgramRegulations;
    private java.sql.Date applicableProgramRegsDate;
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of SubcontractTemplateInfoBean */
    public SubcontractTemplateInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    public char getSowOrSubProposalBudget() {
        return sowOrSubProposalBudget;
    }

    public void setSowOrSubProposalBudget(char newSowOrSubProposalBudget) {
        char oldSowOrSubProposalBudget = sowOrSubProposalBudget;
        this.sowOrSubProposalBudget = newSowOrSubProposalBudget;
        propertySupport.firePropertyChange("sowOrSubProposalBudget", oldSowOrSubProposalBudget, sowOrSubProposalBudget);
    }

    public java.sql.Date getSubProposalDate() {
        return subProposalDate;
    }

    public void setSubProposalDate(java.sql.Date newSubProposalDate) {
        java.sql.Date oldSubProposalDate = subProposalDate;
        this.subProposalDate = newSubProposalDate;
        propertySupport.firePropertyChange("subProposalDate", oldSubProposalDate, subProposalDate);
    }

    public int getInvoiceOrPaymentContactTypeCode() {
        return invoiceOrPaymentContactTypeCode;
    }

    public void setInvoiceOrPaymentContactTypeCode(int newInvoiceOrPaymentContact) {
        int oldInvoiceOrPaymentContactTypeCode = invoiceOrPaymentContactTypeCode;
        this.invoiceOrPaymentContactTypeCode = newInvoiceOrPaymentContact;
        propertySupport.firePropertyChange("invoiceOrPaymentContactTypeCode", oldInvoiceOrPaymentContactTypeCode, invoiceOrPaymentContactTypeCode);
    }

    public int getFinalStmtOfCostsContactTypeCode() {
        return finalStmtOfCostsContactTypeCode;
    }

    public void setFinalStmtOfCostsContactTypeCode(int newFinalStmtOfCostsContact) {
        int oldFinalStmtOfCostsContactTypeCode = finalStmtOfCostsContactTypeCode;
        this.finalStmtOfCostsContactTypeCode = newFinalStmtOfCostsContact;
        propertySupport.firePropertyChange("finalStmtOfCostsContactTypeCode", oldFinalStmtOfCostsContactTypeCode, finalStmtOfCostsContactTypeCode);
        
    }

    public int getChangeRequestsContactTypeCode() {
        return changeRequestsContactTypeCode;
    }

    public void setChangeRequestsContactTypeCode(int newChangeRequestsContact) {
        int oldChangeRequestsContactTypeCode = changeRequestsContactTypeCode;
        this.changeRequestsContactTypeCode = newChangeRequestsContact;
        propertySupport.firePropertyChange("changeRequestsContactTypeCode", oldChangeRequestsContactTypeCode, changeRequestsContactTypeCode);
    }

    public int getTerminationContactTypeCode() {
        return terminationContactTypeCode;
    }

    public void setTerminationContactTypeCode(int newTerminationContact) {
        int oldTerminationContactTypeCode = terminationContactTypeCode;
        this.terminationContactTypeCode = newTerminationContact;
        propertySupport.firePropertyChange("terminationContactTypeCode", oldTerminationContactTypeCode, terminationContactTypeCode);
    }

    public int getNoCostExtensionContactTypeCode() {
        return noCostExtensionContactTypeCode;
    }

    public void setNoCostExtensionContactTypeCode(int newNoCostExtensionContact) {
        int oldNoCostExtensionContactTypeCode = noCostExtensionContactTypeCode;
        this.noCostExtensionContactTypeCode = newNoCostExtensionContact;
        propertySupport.firePropertyChange("noCostExtensionContactTypeCode", oldNoCostExtensionContactTypeCode, terminationContactTypeCode);
    }

    public char getPerfSiteDiffFromOrgAddr() {
        return perfSiteDiffFromOrgAddr;
    }

    public void setPerfSiteDiffFromOrgAddr(char newPerfSiteDiffFromOrgAddr) {
        char oldPerfSiteDiffFromOrgAddr = perfSiteDiffFromOrgAddr;
        this.perfSiteDiffFromOrgAddr = newPerfSiteDiffFromOrgAddr;
        propertySupport.firePropertyChange("perfSiteDiffFromOrgAddr", oldPerfSiteDiffFromOrgAddr, perfSiteDiffFromOrgAddr);
    }

    public char getPerfSiteSameAsSubPiAddr() {
        return perfSiteSameAsSubPiAddr;
    }

    public void setPerfSiteSameAsSubPiAddr(char newPerfSiteSameAsSubPiAddr) {
        char oldPerfSiteSameAsSubPiAddr = perfSiteSameAsSubPiAddr;
        this.perfSiteSameAsSubPiAddr = newPerfSiteSameAsSubPiAddr;
        propertySupport.firePropertyChange("perfSiteSameAsSubPiAddr", oldPerfSiteSameAsSubPiAddr, perfSiteSameAsSubPiAddr);
    }

    public char getSubRegisteredInCcr() {
        return subRegisteredInCcr;
    }

    public void setSubRegisteredInCcr(char newSubRegisteredInCcr) {
        char oldSubRegisteredInCcr = subRegisteredInCcr;
        this.subRegisteredInCcr = newSubRegisteredInCcr;
        propertySupport.firePropertyChange("subRegisteredInCcr", oldSubRegisteredInCcr, subRegisteredInCcr);
    }

    public char getSubExemptFromReportingComp() {
        return subExemptFromReportingComp;
    }

    public void setSubExemptFromReportingComp(char newSubExemptFromReportingComp) {
        char oldSubExemptFromReportingComp = subExemptFromReportingComp;
        this.subExemptFromReportingComp = newSubExemptFromReportingComp;
        propertySupport.firePropertyChange("subExemptFromReportingComp", oldSubExemptFromReportingComp, subExemptFromReportingComp);
    }

    public String getParentDunsNumber() {
        return parentDunsNumber;
    }

    public void setParentDunsNumber(String newParentDunsNumber) {
        String oldParentDunsNumber = parentDunsNumber;
        this.parentDunsNumber = newParentDunsNumber;
        propertySupport.firePropertyChange("parentDunsNumber", oldParentDunsNumber, parentDunsNumber);
    }

    public String getParentCongressionalDistrict() {
        return parentCongressionalDistrict;
    }

    public void setParentCongressionalDistrict(String newParentCongressionalDistrict) {
        String oldParentCongressionalDistrict = parentCongressionalDistrict;
        this.parentCongressionalDistrict = newParentCongressionalDistrict;
        propertySupport.firePropertyChange("parentCongressionalDistrict", oldParentCongressionalDistrict, parentCongressionalDistrict);
    }

    public char getExemptFromRprtgExecComp() {
        return exemptFromRprtgExecComp;
    }

    public void setExemptFromRprtgExecComp(char newExemptFromRprtgExecComp) {
        char oldExemptFromRprtgExecComp = exemptFromRprtgExecComp;
        this.exemptFromRprtgExecComp = newExemptFromRprtgExecComp;
        propertySupport.firePropertyChange("exemptFromRprtgExecComp", oldExemptFromRprtgExecComp, exemptFromRprtgExecComp);
    }

    public int getCopyrightTypeCode() {
        return copyrightTypeCode;
    }

    public void setCopyrightTypeCode(int newCopyrightType) {
        int oldCopyrightTypeCode = copyrightTypeCode;
        this.copyrightTypeCode = newCopyrightType;
        propertySupport.firePropertyChange("copyrightTypeCode", oldCopyrightTypeCode, copyrightTypeCode);
    }

    public char getAutomaticCarryForward() {
        return automaticCarryForward;
    }

    public void setAutomaticCarryForward(char newAutomaticCarryForward) {
        char oldAutomaticCarryForward = automaticCarryForward;
        this.automaticCarryForward = newAutomaticCarryForward;
        propertySupport.firePropertyChange("automaticCarryForward", oldAutomaticCarryForward, automaticCarryForward);
    }

    public int getCarryForwardRequestsSentTo() {
        return carryForwardRequestsSentTo;
    }

    public void setCarryForwardRequestsSentTo(int newCarryForwardRequestsSentTo) {
        int oldCarryForwardRequestsSentTo = carryForwardRequestsSentTo;
        this.carryForwardRequestsSentTo = newCarryForwardRequestsSentTo;
        propertySupport.firePropertyChange("carryForwardRequestsSentTo", oldCarryForwardRequestsSentTo, carryForwardRequestsSentTo);
    }

    public char getTreatmentPrgmIncomeAdditive() {
        return treatmentPrgmIncomeAdditive;
    }

    public void setTreatmentPrgmIncomeAdditive(char newTreatmentPrgmIncomeAdditive) {
        char oldTreatmentPrgmIncomeAdditive = treatmentPrgmIncomeAdditive;
        this.treatmentPrgmIncomeAdditive = newTreatmentPrgmIncomeAdditive;
        propertySupport.firePropertyChange("treatmentPrgmIncomeAdditive", oldTreatmentPrgmIncomeAdditive, treatmentPrgmIncomeAdditive);
    }

    public String getApplicableProgramRegulations() {
        return applicableProgramRegulations;
    }

    public void setApplicableProgramRegulations(String newApplicableProgramRegulations) {
        String oldApplicableProgramRegulations = applicableProgramRegulations;
        this.applicableProgramRegulations = newApplicableProgramRegulations;
        propertySupport.firePropertyChange("applicableProgramRegulations", oldApplicableProgramRegulations, applicableProgramRegulations);
    }

    public java.sql.Date getApplicableProgramRegsDate() {
        return applicableProgramRegsDate;
    }

    public void setApplicableProgramRegsDate(java.sql.Date newAplicableProgramRegsDate) {
        java.sql.Date oldApplicableProgramRegsDate = applicableProgramRegsDate;
        this.applicableProgramRegsDate = newAplicableProgramRegsDate;
        propertySupport.firePropertyChange("applicableProgramRegsDate", oldApplicableProgramRegsDate, applicableProgramRegsDate);
    }

    public String getInvoiceOrPaymentContactTypeDesc() {
        return invoiceOrPaymentContactTypeDesc;
    }

    public void setInvoiceOrPaymentContactTypeDesc(String newInvoiceOrPaymentContactTypeDesc) {
        String oldInvoiceOrPaymentContactTypeDesc = invoiceOrPaymentContactTypeDesc;
        this.invoiceOrPaymentContactTypeDesc = newInvoiceOrPaymentContactTypeDesc;
        propertySupport.firePropertyChange("invoiceOrPaymentContactTypeDesc", oldInvoiceOrPaymentContactTypeDesc, invoiceOrPaymentContactTypeDesc);
    }

    public String getFinalStmtOfCostsContactTypeDesc() {
        return finalStmtOfCostsContactTypeDesc;
    }

    public void setFinalStmtOfCostsContactTypeDesc(String newFinalStmtOfCostsContactTypeDesc) {
        String oldFinalStmtOfCostsContactTypeDesc = finalStmtOfCostsContactTypeDesc;
        this.finalStmtOfCostsContactTypeDesc = newFinalStmtOfCostsContactTypeDesc;
        propertySupport.firePropertyChange("finalStmtOfCostsContactTypeDesc", oldFinalStmtOfCostsContactTypeDesc, finalStmtOfCostsContactTypeDesc);
    }

    public String getChangeRequestsContactTypeDesc() {
        return changeRequestsContactTypeDesc;
    }

    public void setChangeRequestsContactTypeDesc(String newChangeRequestsContactTypeDesc) {
        String oldChangeRequestsContactTypeDesc = changeRequestsContactTypeDesc;
        this.changeRequestsContactTypeDesc = newChangeRequestsContactTypeDesc;
        propertySupport.firePropertyChange("changeRequestsContactTypeDesc", oldChangeRequestsContactTypeDesc, changeRequestsContactTypeDesc);
    }

    public String getTerminationContactTypeDesc() {
        return terminationContactTypeDesc;
    }

    public void setTerminationContactTypeDesc(String newTerminationContactTypeDesc) {
        String oldTerminationContactTypeDesc = terminationContactTypeDesc;
        this.terminationContactTypeDesc = newTerminationContactTypeDesc;
        propertySupport.firePropertyChange("terminationContactTypeDesc", oldTerminationContactTypeDesc, terminationContactTypeDesc);
    }

    public String getNoCostExtensionContactTypeDesc() {
        return noCostExtensionContactTypeDesc;
    }

    public void setNoCostExtensionContactTypeDesc(String newNoCostExtensionContactTypeDesc) {
        String oldNoCostExtensionContactTypeDesc = noCostExtensionContactTypeDesc;
        this.noCostExtensionContactTypeDesc = newNoCostExtensionContactTypeDesc;
        propertySupport.firePropertyChange("noCostExtensionContactTypeDesc", oldNoCostExtensionContactTypeDesc, noCostExtensionContactTypeDesc);
    }

    public String getCopyrightTypeDesc() {
        return copyrightTypeDesc;
    }

    public void setCopyrightTypeDesc(String newCopyrightTypeDesc) {
        String oldCopyrightTypeDesc = copyrightTypeDesc;
        this.copyrightTypeDesc = newCopyrightTypeDesc;
        propertySupport.firePropertyChange("copyrightTypeDesc", oldCopyrightTypeDesc, copyrightTypeDesc);
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

    public String getCarryForwardRequestsSentToDesc() {
        return carryForwardRequestsSentToDesc;
    }

    public void setCarryForwardRequestsSentToDesc(String carryForwardRequestsSentToDesc) {
        this.carryForwardRequestsSentToDesc = carryForwardRequestsSentToDesc;
    }

}
