/*
 * ProtocolBean.java
 *
 * Created on August 19, 2008, 4:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

import java.sql.Date;

/**
 *
 * @author blessy
 */
public class ProtocolBean {
    
    /** Creates a new instance of ProtocolBean */
    public ProtocolBean() {
    }
     private String coiDisclosureNumber;
    private int disclSeqNumber;
    private String protocolNumber;
    private int module_code;
    private String fundingSource;
    private String status;
    private Date lastApprDate;
    private Date expDate;
    private String title;
    private String role;

    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    public void setDisclosureNumber(String disclosureNumber) {
        this.coiDisclosureNumber = disclosureNumber;
    }

    public int getDisclSeqNumber() {
        return disclSeqNumber;
    }

    public void setDisclSeqNumber(int disclSeqNumber) {
        this.disclSeqNumber = disclSeqNumber;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getModule_code() {
        return module_code;
    }

    public void setModule_code(int module_code) {
        this.module_code = module_code;
    }

    public String getFundingSource() {
        return fundingSource;
    }

    public void setFundingSource(String fundingSource) {
        this.fundingSource = fundingSource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastApprDate() {
        return lastApprDate;
    }

    public void setLastApprDate(Date lastApprDate) {
        this.lastApprDate = lastApprDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
