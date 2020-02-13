/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.formbeans;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Mr
 */
public class Coiv2DispDisclStatusForm extends ActionForm{
private String dispositionStatus;
private String disclosureStatus;

    /**
     * @return the dispositionStatus
     */
    public String getDispositionStatus() {
        return dispositionStatus;
    }

    /**
     * @param dispositionStatus the dispositionStatus to set
     */
    public void setDispositionStatus(String dispositionStatus) {
        this.dispositionStatus = dispositionStatus;
    }

    /**
     * @return the disclosureStatus
     */
    public String getDisclosureStatus() {
        return disclosureStatus;
    }

    /**
     * @param disclosureStatus the disclosureStatus to set
     */
    public void setDisclosureStatus(String disclosureStatus) {
        this.disclosureStatus = disclosureStatus;
    }
}
