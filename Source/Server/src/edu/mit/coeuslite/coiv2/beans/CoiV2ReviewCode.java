/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author veena
 */
public class CoiV2ReviewCode extends org.apache.struts.action.ActionForm {
    private String disclosureNumber;
    private int sequenceNumber;
    private Integer reviewStatusCode;
    private String updateUser;

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }


    public String getDisclosureNumber() {
        return disclosureNumber;
    }

    public void setDisclosureNumber(String disclosureNumber) {
        this.disclosureNumber = disclosureNumber;
    }

    public Integer getReviewStatusCode() {
        return reviewStatusCode;
    }

    public void setReviewStatusCode(Integer reviewStatusCode) {
        this.reviewStatusCode = reviewStatusCode;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

}
