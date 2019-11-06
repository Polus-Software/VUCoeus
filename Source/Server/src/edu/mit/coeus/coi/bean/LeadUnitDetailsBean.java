/*
 * @(#)LeadUnitDetailsBean.java 1.0 3/30/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is for getting and setting various details/values concerning lead unit.
 *
 * @version 1.0 March 30, 2002, 7:22 PM
 * @author  Anil Nandakumar
 */
public class LeadUnitDetailsBean extends java.lang.Object {
    private String leadUnitNumber;
    private String leadUnitName;
    private String adminOfficer;
    private String unitHead;
    private String deanVP;
    private String notify;
    private String ospAdmin;
    /** Creates new LeadUnitDetailsBean */
    public LeadUnitDetailsBean() {
    }
    /**
     *  This method gets the Lead Unit Number
     *  @return String leadUnitNumber
     */
    public String getLeadUnitNumber() {
        return leadUnitNumber;
    }
    /**
     *  This method sets the  Lead Unit Number
     *  @param String leadUnitNumber
     */
    public void setLeadUnitNumber(java.lang.String leadUnitNumber) {
        this.leadUnitNumber = UtilFactory.checkNullStr(leadUnitNumber);
    }
    /**
     *  This method gets the Lead Unit Name
     *  @return String leadUnitName
     */
    public String getLeadUnitName() {
        return leadUnitName;
    }
    /**
     *  This method sets the Lead Unit Name
     *  @param String leadUnitName
     */
    public void setLeadUnitName(java.lang.String leadUnitName) {
        this.leadUnitName = UtilFactory.checkNullStr(leadUnitName);
    }
    /**
     *  This method gets the Admin Officer
     *  @return String adminOfficer
     */
    public String getAdminOfficer() {
        return adminOfficer;
    }
    /**
     *  This method sets the Admin Officer
     *  @param String adminOfficer
     */
    public void setAdminOfficer(java.lang.String adminOfficer) {
        this.adminOfficer = UtilFactory.checkNullStr(adminOfficer);
    }
    /**
     *  This method gets the Unit Head
     *  @return String unitHead
     */
    public String getUnitHead() {
        return unitHead;
    }
    /**
     *  This method sets the Unit Head
     *  @param String unitHead
     */
    public void setUnitHead(java.lang.String unitHead) {
        this.unitHead = UtilFactory.checkNullStr(unitHead);
    }
    /**
     *  This method gets the Dean VP
     *  @return String deanVP
     */
    public String getDeanVP() {
        return deanVP;
    }
    /**
     *  This method sets the Dean VP
     *  @param String deanVP
     */
    public void setDeanVP(java.lang.String deanVP) {
        this.deanVP = UtilFactory.checkNullStr(deanVP);
    }
    /**
     *  This method gets the Notify parameter
     *  @return String notify
     */
    public String getNotify() {
        return notify;
    }
    /**
     *  This method sets the Notify parameter
     *  @param String notify
     */
    public void setNotify(java.lang.String notify) {
        this.notify = UtilFactory.checkNullStr(notify);
    }
    /**
     *  This method gets the OSP Admin
     *  @return String ospAdmin
     */
    public String getOSPAdmin() {
        return ospAdmin;
    }
    /**
     *  This method sets the OSP Admin
     *  @param String ospAdmin
     */
    public void setOSPAdmin(java.lang.String ospAdmin) {
        this.ospAdmin = UtilFactory.checkNullStr(ospAdmin);
    }
}

