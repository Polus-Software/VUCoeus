/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.util.Vector;

/**
 *
 * @author Mr
 */
public class ProjectBean {

    private String projectID;
    private String projectModule;
    private String projectSponsor;
    private Vector financialEntityStatus=new Vector();

    /**
     * @return the projectID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * @param projectID the projectID to set
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    /**
     * @return the projectModule
     */
    public String getProjectModule() {
        return projectModule;
    }

    /**
     * @param projectModule the projectModule to set
     */
    public void setProjectModule(String projectModule) {
        this.projectModule = projectModule;
    }

    /**
     * @return the projectSponsor
     */
    public String getProjectSponsor() {
        return projectSponsor;
    }

    /**
     * @param projectSponsor the projectSponsor to set
     */
    public void setProjectSponsor(String projectSponsor) {
        this.projectSponsor = projectSponsor;
    }

    /**
     * @return the financialEntityStatus
     */
    public Vector getFinancialEntityStatus() {
        return financialEntityStatus;
    }

    /**
     * @param financialEntityStatus the financialEntityStatus to set
     */
    public void setFinancialEntityStatus(Vector financialEntityStatus) {
        this.financialEntityStatus = financialEntityStatus;
    }



}
