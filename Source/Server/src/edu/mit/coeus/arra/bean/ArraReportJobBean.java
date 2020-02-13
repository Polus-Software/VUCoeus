/*
 * ArraReportJobBean.java
 *
 * Created on September 21, 2009, 2:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.arra.bean;

/**
 *
 * @author keerthyjayaraj
 */
public class ArraReportJobBean extends ArraAwardBaseBean{
    
    private String personId;
    private String personName;
    private String jobTitle;
    private double fte;
    
    /** Creates a new instance of ArraReportJobBean */
    public ArraReportJobBean() {
    }
    
    public double getFte() {
        return fte;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setFte(double fte) {
        this.fte = fte;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    
}
