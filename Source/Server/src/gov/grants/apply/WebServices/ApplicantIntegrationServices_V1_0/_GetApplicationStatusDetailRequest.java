/**
 * _GetApplicationStatusDetailRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetApplicationStatusDetailRequest  implements java.io.Serializable {
    private gov.grants.apply.system.Global_V1_0.StringMin1Max240Type grants_govTrackingNumber;

    public _GetApplicationStatusDetailRequest() {
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max240Type getGrants_govTrackingNumber() {
        return grants_govTrackingNumber;
    }

    public void setGrants_govTrackingNumber(gov.grants.apply.system.Global_V1_0.StringMin1Max240Type grants_govTrackingNumber) {
        this.grants_govTrackingNumber = grants_govTrackingNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetApplicationStatusDetailRequest)) return false;
        _GetApplicationStatusDetailRequest other = (_GetApplicationStatusDetailRequest) obj;
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
              this.grants_govTrackingNumber.equals(other.getGrants_govTrackingNumber())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetApplicationStatusDetailRequest.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationStatusDetailRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grants_govTrackingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "Grants_govTrackingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max240Type"));
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
