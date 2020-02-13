/**
 * _GetApplicationListRequest_ApplicationFilter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetApplicationListRequest_ApplicationFilter  implements java.io.Serializable {
    private gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter_Filter filter;
    private java.lang.String filterValue;

    public _GetApplicationListRequest_ApplicationFilter() {
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter_Filter getFilter() {
        return filter;
    }

    public void setFilter(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0._GetApplicationListRequest_ApplicationFilter_Filter filter) {
        this.filter = filter;
    }

    public java.lang.String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(java.lang.String filterValue) {
        this.filterValue = filterValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetApplicationListRequest_ApplicationFilter)) return false;
        _GetApplicationListRequest_ApplicationFilter other = (_GetApplicationListRequest_ApplicationFilter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.filter==null && other.getFilter()==null) || 
             (this.filter!=null &&
              this.filter.equals(other.getFilter()))) &&
            ((this.filterValue==null && other.getFilterValue()==null) || 
             (this.filterValue!=null &&
              this.filterValue.equals(other.getFilterValue())));
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
        if (getFilter() != null) {
            _hashCode += getFilter().hashCode();
        }
        if (getFilterValue() != null) {
            _hashCode += getFilterValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetApplicationListRequest_ApplicationFilter.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest>ApplicationFilter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "Filter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetApplicationListRequest>ApplicationFilter>Filter"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filterValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "FilterValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
