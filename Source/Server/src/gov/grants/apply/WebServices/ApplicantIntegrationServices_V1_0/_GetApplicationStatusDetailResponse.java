/**
 * _GetApplicationStatusDetailResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetApplicationStatusDetailResponse  implements java.io.Serializable {
    private gov.grants.apply.system.Global_V1_0.StringMin1Max240Type grants_govTrackingNumber;
    private java.lang.Object detailedStatus;

    public _GetApplicationStatusDetailResponse() {
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max240Type getGrants_govTrackingNumber() {
        return grants_govTrackingNumber;
    }

    public void setGrants_govTrackingNumber(gov.grants.apply.system.Global_V1_0.StringMin1Max240Type grants_govTrackingNumber) {
        this.grants_govTrackingNumber = grants_govTrackingNumber;
    }

    public java.lang.Object getDetailedStatus() {
        return detailedStatus;
    }

    public void setDetailedStatus(java.lang.Object detailedStatus) {
        this.detailedStatus = detailedStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetApplicationStatusDetailResponse)) return false;
        _GetApplicationStatusDetailResponse other = (_GetApplicationStatusDetailResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.grants_govTrackingNumber==null && other.getGrants_govTrackingNumber()==null) || 
             (this.grants_govTrackingNumber!=null &&
              this.grants_govTrackingNumber.equals(other.getGrants_govTrackingNumber()))) &&
            ((this.detailedStatus==null && other.getDetailedStatus()==null) || 
             (this.detailedStatus!=null &&
              this.detailedStatus.equals(other.getDetailedStatus())));
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
        if (getGrants_govTrackingNumber() != null) {
            _hashCode += getGrants_govTrackingNumber().hashCode();
        }
        if (getDetailedStatus() != null) {
            _hashCode += getDetailedStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetApplicationStatusDetailResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationStatusDetailResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grants_govTrackingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "Grants_govTrackingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max240Type"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailedStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "DetailedStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
