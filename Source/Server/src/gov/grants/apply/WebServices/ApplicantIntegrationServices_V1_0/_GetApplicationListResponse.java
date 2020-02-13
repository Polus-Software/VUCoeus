/**
 * _GetApplicationListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetApplicationListResponse  implements java.io.Serializable {
    private int availableApplicationNumber;
    private gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType[] applicationInformation;

    public _GetApplicationListResponse() {
    }

    public int getAvailableApplicationNumber() {
        return availableApplicationNumber;
    }

    public void setAvailableApplicationNumber(int availableApplicationNumber) {
        this.availableApplicationNumber = availableApplicationNumber;
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType[] getApplicationInformation() {
        return applicationInformation;
    }

    public void setApplicationInformation(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType[] applicationInformation) {
        this.applicationInformation = applicationInformation;
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType getApplicationInformation(int i) {
        return applicationInformation[i];
    }

    public void setApplicationInformation(int i, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicationInformationType value) {
        this.applicationInformation[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetApplicationListResponse)) return false;
        _GetApplicationListResponse other = (_GetApplicationListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.availableApplicationNumber == other.getAvailableApplicationNumber() &&
            ((this.applicationInformation==null && other.getApplicationInformation()==null) || 
             (this.applicationInformation!=null &&
              java.util.Arrays.equals(this.applicationInformation, other.getApplicationInformation())));
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
        _hashCode += getAvailableApplicationNumber();
        if (getApplicationInformation() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getApplicationInformation());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getApplicationInformation(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetApplicationListResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("availableApplicationNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "AvailableApplicationNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationInformation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ApplicationInformation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ApplicationInformationType"));
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
