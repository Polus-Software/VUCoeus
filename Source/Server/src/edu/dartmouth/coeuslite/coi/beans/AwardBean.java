/*
 * AwardBean.java
 *
 * Created on August 19, 2008, 4:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

import java.sql.Date;/**
 *
 * @author blessy
 */
public class AwardBean {
    
    /** Creates a new instance of AwardBean */
    public AwardBean() {
    }
   private String coiDisclosureNumber;
    private int disclSeqNumber;
    private String awardNumber;
    private int moduleCode; 
    private String sponsor;
    private Date startDate;
    private Date expDate;
    private Double oblTotal;
    private Double antTotal;
    private String role;
    private String title;

    public String getDisclosureNumber() {
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

    public String getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModule_code(int module_code) {
        this.moduleCode = module_code;
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

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Double getOblTotal() {
        return oblTotal;
    }

    public void setOblTotal(Double oblTotal) {
        this.oblTotal = oblTotal;
    }

    public Double getAntTotal() {
        return antTotal;
    }

    public void setAntTotal(Double antTotal) {
        this.antTotal = antTotal;
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
