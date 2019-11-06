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
public class propBasedDiclForm extends ActionForm{
    private String checkbox[];

    /**
     * @return the checkbox
     */
    public String[] getCheckbox() {
        return checkbox;
    }

    /**
     * @param checkbox the checkbox to set
     */
    public void setCheckbox(String[] checkbox) {
        this.checkbox = checkbox;
    }

    

}
