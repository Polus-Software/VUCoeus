/*
 * DisclProposalBean.java
 *
 * Created on April 24, 2008, 4:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

/**
 *
 * @author blessy
 */
public class DisclProposalBean {
    
    /** Creates a new instance of DisclProposalBean */
    public DisclProposalBean() {
    }
   private String moduleItemKey;
   private Integer moduleCode;
   private String title;
   private Integer statusCode;
   private String statusDescription;
   private String sponsorCode;
   private String sponsor;
   private Integer disclExistsFlg;

    public String getModuleItemKey() {
        return moduleItemKey;
    }

    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    public Integer getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(Integer moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public Integer getDisclExistsFlg() {
        return disclExistsFlg;
    }

    public void setDisclExistsFlg(Integer disclExistsFlg) {
        this.disclExistsFlg = disclExistsFlg;
    }
		 
}
