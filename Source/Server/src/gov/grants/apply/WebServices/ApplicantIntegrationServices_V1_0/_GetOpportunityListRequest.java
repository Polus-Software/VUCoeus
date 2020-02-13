/**
 * _GetOpportunityListRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetOpportunityListRequest  implements java.io.Serializable {
    private gov.grants.apply.system.Global_V1_0.StringMin1Max100Type opportunityID;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max15Type CFDANumber;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max100Type competitionID;

    public _GetOpportunityListRequest() {
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max100Type getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(gov.grants.apply.system.Global_V1_0.StringMin1Max100Type opportunityID) {
        this.opportunityID = opportunityID;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max15Type getCFDANumber() {
        return CFDANumber;
    }

    public void setCFDANumber(gov.grants.apply.system.Global_V1_0.StringMin1Max15Type CFDANumber) {
        this.CFDANumber = CFDANumber;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max100Type getCompetitionID() {
        return competitionID;
    }

    public void setCompetitionID(gov.grants.apply.system.Global_V1_0.StringMin1Max100Type competitionID) {
        this.competitionID = competitionID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetOpportunityListRequest)) return false;
        _GetOpportunityListRequest other = (_GetOpportunityListRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.opportunityID==null && other.getOpportunityID()==null) || 
             (this.opportunityID!=null &&
              this.opportunityID.equals(other.getOpportunityID()))) &&
            ((this.CFDANumber==null && other.getCFDANumber()==null) || 
             (this.CFDANumber!=null &&
              this.CFDANumber.equals(other.getCFDANumber()))) &&
            ((this.competitionID==null && other.getCompetitionID()==null) || 
             (this.competitionID!=null &&
              this.competitionID.equals(other.getCompetitionID())));
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
        if (getOpportunityID() != null) {
            _hashCode += getOpportunityID().hashCode();
        }
        if (getCFDANumber() != null) {
            _hashCode += getCFDANumber().hashCode();
        }
        if (getCompetitionID() != null) {
            _hashCode += getCompetitionID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetOpportunityListRequest.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetOpportunityListRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opportunityID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max100Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CFDANumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "CFDANumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max15Type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("competitionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "CompetitionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max100Type"));
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
