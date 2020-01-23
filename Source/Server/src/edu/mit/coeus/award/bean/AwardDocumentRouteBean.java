/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeus.award.bean;


import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author midhunmk
 */
public class AwardDocumentRouteBean implements Serializable {
    private String mitAwardNumber;
    private int sequenceNumber;
    private int routingDocumentNumber;
    private int documentTypeCode;
    private String documentTypeDesc;
    private int routingStatusCode;
    private String routingStatusDesc;
    private String signatureFlag;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    private String mimeType;
    private byte[] documentData;
    private String documentName;
    
    private java.sql.Timestamp routeStartDate;
    private java.sql.Timestamp routeEndDate;
    
    private int routingApprovalSeq;

    public int getRoutingApprovalSeq() {
        return routingApprovalSeq;
    }

    public void setRoutingApprovalSeq(int routingApprovalSeq) {
        this.routingApprovalSeq = routingApprovalSeq;
    }
    

    public String getRouteEndDate() {
        if(routeEndDate!=null){
       return new SimpleDateFormat("MM/dd/yyyy hh:mm").format(routeEndDate);}
        else{return "";}
    }

    public void setRouteEndDate(Timestamp routeEndDate) {
        this.routeEndDate = routeEndDate;
    }

    public String getRouteStartDate() {
       return new SimpleDateFormat("MM/dd/yyyy hh:mm").format(routeStartDate); 
    }

    public void setRouteStartDate(Timestamp routeStartDate) {
        this.routeStartDate = routeStartDate;
    }
    
    
     public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public String getDocumentName() {
        if(documentName == null || documentName.length() == 0) {
            documentName = "CoeusDocument";
        }
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(int documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getDocumentTypeDesc() {
        return documentTypeDesc;
    }

    public void setDocumentTypeDesc(String documentTypeDesc) {
        this.documentTypeDesc = documentTypeDesc;
    }



    public String getMitAwardNumber() {
        return mitAwardNumber;
    }

    public void setMitAwardNumber(String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }

    public int getRoutingDocumentNumber() {
        return routingDocumentNumber;
    }

    public void setRoutingDocumentNumber(int routingDocumentNumber) {
        this.routingDocumentNumber = routingDocumentNumber;
    }

    public int getRoutingStatusCode() {
        return routingStatusCode;
    }

    public void setRoutingStatusCode(int routingStatusCode) {
        this.routingStatusCode = routingStatusCode;
    }

    public String getRoutingStatusDesc() {
        return routingStatusDesc;
    }

    public void setRoutingStatusDesc(String routingStatusDesc) {
        this.routingStatusDesc = routingStatusDesc;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSignatureFlag() {
        return signatureFlag;
    }

    public void setSignatureFlag(String signatureFlag) {
        this.signatureFlag = signatureFlag;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    public AwardDocumentRouteBean(){}
    public AwardDocumentRouteBean(AwardDocumentRouteBean adrb){
        mitAwardNumber=adrb.getMitAwardNumber();
        routingApprovalSeq=adrb.getRoutingApprovalSeq();
        
    }
    
}
