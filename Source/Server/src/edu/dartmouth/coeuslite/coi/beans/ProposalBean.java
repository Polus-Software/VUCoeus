/*
 * ProposalBean.java
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
public class ProposalBean {
    
    /** Creates a new instance of ProposalBean */
    public ProposalBean() {
    }
    private String coiDisclosureNumber;
    private int disclSeqNumber;
    private String proposalNumber;
    private int moduleCode;
    private String sponsor;
    private Date startDate;
    private Date endDate;
    private long totalCost;
    private String role;
    private String title;

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

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModule_code(int moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
}
