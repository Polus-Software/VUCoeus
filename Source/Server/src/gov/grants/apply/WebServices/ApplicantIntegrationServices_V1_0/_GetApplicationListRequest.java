/**
 * _GetApplicationListRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetApplicationListRequest  implements java.io.Serializable {
    private gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter[] applicationFilter;

    public _GetApplicationListRequest() {
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter[] getApplicationFilter() {
        return applicationFilter;
    }

    public void setApplicationFilter(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter[] applicationFilter) {
        this.applicationFilter = applicationFilter;
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter getApplicationFilter(int i) {
        return applicationFilter[i];
    }

    public void setApplicationFilter(int i, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter value) {
        this.applicationFilter[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetApplicationListRequest)) return false;
        _GetApplicationListRequest other = (_GetApplicationListRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.applicationFilter==null && other.getApplicationFilter()==null) || 
             (this.applicationFilter!=null &&
              java.util.Arrays.equals(this.applicationFilter, other.getApplicationFilter())));
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
        if (getApplicationFilter() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getApplicationFilter());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getApplicationFilter(), i);
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
        new org.apache.axis.description.TypeDesc(_GetApplicationListRequest.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationFilter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ApplicationFilter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest>ApplicationFilter"));
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
