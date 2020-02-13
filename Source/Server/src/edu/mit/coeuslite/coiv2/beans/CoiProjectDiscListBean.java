/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import java.util.Date;

/**
 *
 * @author midhunmk
 */
public class CoiProjectDiscListBean {
    private String coiDisclosureNumber;
    private Integer coiSequenceNumber;
    private Integer coiEntityCode;
    private String coiEntityDesc;
    private String coiUpdateTimeStamp;
    private Integer coiProjectStatusCode;
    private String coiProjectStatusDesc;
    private String coiEventName;

    public String getCoiEventName() {
        return coiEventName;
    }

    public void setCoiEventName(String coiEventName) {
        this.coiEventName = coiEventName;
    }
    public String getCoiModuleItemKey() {
        return coiModuleItemKey;
    }

    public void setCoiModuleItemKey(String coiModuleItemKey) {
        this.coiModuleItemKey = coiModuleItemKey;
    }

    public String getCoititle() {
        return coititle;
    }

    public void setCoititle(String coititle) {
        this.coititle = coititle;
    }
    private String coiModuleItemKey;
    private String coititle;
    public Integer getCoiProjectStatusCode() {
        return coiProjectStatusCode;
    }

    public void setCoiProjectStatusCode(Integer coiProjectStatusCode) {
        this.coiProjectStatusCode = coiProjectStatusCode;
    }

    public String getCoiProjectStatusDesc() {
        return coiProjectStatusDesc;
    }

    public void setCoiProjectStatusDesc(String coiProjectStatusDesc) {
        this.coiProjectStatusDesc = coiProjectStatusDesc;
    }

    public Integer getCoiEntityCode() {
        return coiEntityCode;
    }

    public void setCoiEntityCode(Integer coiEntityCode) {
        this.coiEntityCode = coiEntityCode;
    }

    public String getCoiEntityDesc() {
        return coiEntityDesc;
    }

    public void setCoiEntityDesc(String coiEntityDesc) {
        this.coiEntityDesc = coiEntityDesc;
    }

    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    public Integer getCoiSequenceNumber() {
        return coiSequenceNumber;
    }

    public void setCoiSequenceNumber(Integer coiSequenceNumber) {
        this.coiSequenceNumber = coiSequenceNumber;
    }

    public String getCoiUpdateTimeStamp() {
        return coiUpdateTimeStamp;
    }

    public void setCoiUpdateTimeStamp(String coiUpdateTimeStamp) {
        this.coiUpdateTimeStamp = coiUpdateTimeStamp;
    }
    
}
