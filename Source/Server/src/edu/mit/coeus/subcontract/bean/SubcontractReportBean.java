/*
 * SubcontractReportBean.java
 *
 * Created on February 1, 2012, 5:04 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.subcontract.bean;

/**
 *
 * @author satheeshkumarkn
 */
public class SubcontractReportBean extends SubContractBaseBean{
    
    private int reportTypeCode;
     private String reportTypeDescription;

    /** Creates a new instance of SubcontractReportBean */
    public SubcontractReportBean() {
    }

    public int getReportTypeCode() {
        return reportTypeCode;
    }

    public void setReportTypeCode(int reportTypeCode) {
        this.reportTypeCode = reportTypeCode;
    }

    public String getReportTypeDescription() {
        return reportTypeDescription;
    }

    public void setReportTypeDescription(String reportTypeDescription) {
        this.reportTypeDescription = reportTypeDescription;
    }
    
    public boolean equals(Object obj) {
        boolean isEquals = false;
        SubcontractReportBean subcontractReportBean = (SubcontractReportBean)obj;
        if(subcontractReportBean.getSubContractCode().equals(getSubContractCode()) &&
                subcontractReportBean.getSequenceNumber() == getSequenceNumber() && 
                subcontractReportBean.getReportTypeCode() == getReportTypeCode()){
            isEquals = true;
        }
        return isEquals;
    }    
    
}
