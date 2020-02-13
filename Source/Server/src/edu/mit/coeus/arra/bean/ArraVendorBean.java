/*
 * ArraVendorBean.java
 *
 * Created on September 21, 2009, 2:08 PM
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
public class ArraVendorBean extends ArraAwardBaseBean{
    
    private String vendorNumber;
    private String subContractCode;
    private String vendorDUNS;
    private String vendorHQZipCode;
    private String vendorName;
    private String serviceDescription;
    private double paymentAmount;
    
    /** Creates a new instance of ArraVendorBean */
    public ArraVendorBean() {
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getSubContractCode() {
        return subContractCode;
    }

    public String getVendorDUNS() {
        return vendorDUNS;
    }

    public String getVendorHQZipCode() {
        return vendorHQZipCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public void setSubContractCode(String subContractCode) {
        this.subContractCode = subContractCode;
    }

    public void setVendorDUNS(String vendorDUNS) {
        this.vendorDUNS = vendorDUNS;
    }

    public void setVendorHQZipCode(String vendorHQZipCode) {
        this.vendorHQZipCode = vendorHQZipCode;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }
    

}
