/*
 * @(#)ProposalCongDistrictBean.java 1.0 06/12/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Timestamp;

/**
 *
 * @author leenababu
 */
public class ProposalCongDistrictBean implements java.io.Serializable, BaseBean{
    private String proposalNumber;
    private int siteNumber;
    private String congDistrict;
    private String aw_CongDistrict;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String acType;

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public int getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(int siteNumber) {
        this.siteNumber = siteNumber;
    }

    public String getCongDistrict() {
        return congDistrict;
    }

    public void setCongDistrict(String congDistrict) {
        this.congDistrict = congDistrict;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getAw_CongDistrict() {
        return aw_CongDistrict;
    }

    public void setAw_CongDistrict(String aw_CongDistrict) {
        this.aw_CongDistrict = aw_CongDistrict;
    }
    
}
