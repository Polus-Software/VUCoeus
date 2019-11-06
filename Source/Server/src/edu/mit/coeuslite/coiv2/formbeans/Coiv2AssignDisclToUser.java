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
public class Coiv2AssignDisclToUser extends ActionForm {
    private String[] assignedList;

    /**
     * @return the assignedList
     */
    public String[] getAssignedList() {
        return assignedList;
    }

    /**
     * @param assignedList the assignedList to set
     */
    public void setAssignedList(String[] assignedList) {
        this.assignedList = assignedList;
    }
   
}
