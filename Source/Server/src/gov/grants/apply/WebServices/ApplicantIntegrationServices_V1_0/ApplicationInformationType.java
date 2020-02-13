/**
 * ApplicationInformationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class ApplicationInformationType  implements java.io.Serializable {
    private gov.grants.apply.system.Global_V1_0.StringMin1Max15Type CFDANumber;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max100Type opportunityID;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max100Type competitionID;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max240Type grants_govTrackingNumber;
    private java.util.Calendar receivedDateTime;
    private gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.GrantsGovApplicationStatusType grantsGovApplicationStatus;
    private java.util.Calendar statusDateTime;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max240Type agencyTrackingNumber;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max240Type submissionTitle;

    public ApplicationInformationType() {
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max15Type getCFDANumber() {
        return CFDANumber;
    }

    public void setCFDANumber(gov.grants.apply.system.Global_V1_0.StringMin1Max15Type CFDANumber) {
        this.CFDANumber = CFDANumber;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max100Type getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(gov.grants.apply.system.Global_V1_0.StringMin1Max100Type opportunityID) {
        this.opportunityID = opportunityID;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max100Type getCompetitionID() {
        return competitionID;
    }

    public void setCompetitionID(gov.grants.apply.system.Global_V1_0.StringMin1Max100Type competitionID) {
        this.competitionID = competitionID;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max240Type getGrants_govTrackingNumber() {
        return grants_govTrackingNumber;
    }

    public void setGrants_govTrackingNumber(gov.grants.apply.system.Global_V1_0.StringMin1Max240Type grants_govTrackingNumber) {
        this.grants_govTrackingNumber = grants_govTrackingNumber;
    }

    public java.util.Calendar getReceivedDateTime() {
        return receivedDateTime;
    }

    public void setReceivedDateTime(java.util.Calendar receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.GrantsGovApplicationStatusType getGrantsGovApplicationStatus() {
        return grantsGovApplicationStatus;
    }

    public void setGrantsGovApplicationStatus(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.GrantsGovApplicationStatusType grantsGovApplicationStatus) {
        this.grantsGovApplicationStatus = grantsGovApplicationStatus;
    }

    public java.util.Calendar getStatusDateTime() {
        return statusDateTime;
    }

    public void setStatusDateTime(java.util.Calendar statusDateTime) {
        this.statusDateTime = statusDateTime;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max240Type getAgencyTrackingNumber() {
        return agencyTrackingNumber;
    }

    public void setAgencyTrackingNumber(gov.grants.apply.system.Global_V1_0.StringMin1Max240Type agencyTrackingNumber) {
        this.agencyTrackingNumber = agencyTrackingNumber;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max240Type getSubmissionTitle() {
        return submissionTitle;
    }

    public void setSubmissionTitle(gov.grants.apply.system.Global_V1_0.StringMin1Max240Type submissionTitle) {
        this.submissionTitle = submissionTitle;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ApplicationInformationType)) return false;
        ApplicationInformationType other = (ApplicationInformationType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.CFDANumber==null && other.getCFDANumber()==null) || 
             (this.CFDANumber!=null &&
              this.CFDANumber.equals(other.getCFDANumber()))) &&
            ((this.opportunityID==null && other.getOpportunityID()==null) || 
             (this.opportunityID!=null &&
              this.opportunityID.equals(other.getOpportunityID()))) &&
            ((this.competitionID==null && other.getCompetitionID()==null) || 
             (this.competitionID!=null &&
              this.competitionID.equals(other.getCompetitionID()))) &&
            ((this.grants_govTrackingNumber==null && other.getGrants_govTrackingNumber()==null) || 
             (this.grants_govTrackingNumber!=null &&
              this.grants_govTrackingNumber.equals(other.getGrants_govTrackingNumber()))) &&
            ((this.receivedDateTime==null && other.getReceivedDateTime()==null) || 
             (this.receivedDateTime!=null &&
              this.receivedDateTime.equals(other.getReceivedDateTime()))) &&
            ((this.grantsGovApplicationStatus==null && other.getGrantsGovApplicationStatus()==null) || 
             (this.grantsGovApplicationStatus!=null &&
              this.grantsGovApplicationStatus.equals(other.getGrantsGovApplicationStatus()))) &&
            ((this.statusDateTime==null && other.getStatusDateTime()==null) || 
             (this.statusDateTime!=null &&
              this.statusDateTime.equals(other.getStatusDateTime()))) &&
            ((this.agencyTrackingNumber==null && other.getAgencyTrackingNumber()==null) || 
             (this.agencyTrackingNumber!=null &&
              this.agencyTrackingNumber.equals(other.getAgencyTrackingNumber()))) &&
            ((this.submissionTitle==null && other.getSubmissionTitle()==null) || 
             (this.submissionTitle!=null &&
              this.submissionTitle.equals(other.getSubmissionTitle())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCFDANumber() != null) {
            _hashCode += getCFDANumber().hashCode();
        }
        if (getOpportunityID() != null) {
            _hashCode += getOpportunityID().hashCode();
        }
        if (getCompetitionID() != null) {
            _hashCode += getCompetitionID().hashCode();
        }
        if (getGrants_govTrackingNumber() != null) {
            _hashCode += getGrants_govTrackingNumber().hashCode();
        }
        if (getReceivedDateTime() != null) {
            _hashCode += getReceivedDateTime().hashCode();
        }
        if (getGrantsGovApplicationStatus() != null) {
            _hashCode += getGrantsGovApplicationStatus().hashCode();
        }
        if (getStatusDateTime() != null) {
            _hashCode += getStatusDateTime().hashCode();
        }
        if (getAgencyTrackingNumber() != null) {
            _hashCode += getAgencyTrackingNumber().hashCode();
        }
        if (getSubmissionTitle() != null) {
            _hashCode += getSubmissionTitle().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ApplicationInformationType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ApplicationInformationType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CFDANumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "CFDANumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max15Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opportunityID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max100Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("competitionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "CompetitionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max100Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grants_govTrackingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "Grants_govTrackingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max240Type"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("receivedDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ReceivedDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grantsGovApplicationStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GrantsGovApplicationStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "GrantsGovApplicationStatusType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "StatusDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("agencyTrackingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "AgencyTrackingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max240Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("submissionTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "SubmissionTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max240Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
