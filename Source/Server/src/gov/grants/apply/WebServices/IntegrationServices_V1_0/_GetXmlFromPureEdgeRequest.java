/**
 * _GetXmlFromPureEdgeRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.IntegrationServices_V1_0;

public class _GetXmlFromPureEdgeRequest  implements java.io.Serializable {
    private gov.grants.apply.WebServices.IntegrationServices_V1_0.EncodingTypeEnum encodingType;
    private gov.grants.apply.WebServices.IntegrationServices_V1_0._OrganizationID organizationID;

    public _GetXmlFromPureEdgeRequest() {
    }

    public gov.grants.apply.WebServices.IntegrationServices_V1_0.EncodingTypeEnum getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(gov.grants.apply.WebServices.IntegrationServices_V1_0.EncodingTypeEnum encodingType) {
        this.encodingType = encodingType;
    }

    public gov.grants.apply.WebServices.IntegrationServices_V1_0._OrganizationID getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(gov.grants.apply.WebServices.IntegrationServices_V1_0._OrganizationID organizationID) {
        this.organizationID = organizationID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetXmlFromPureEdgeRequest)) return false;
        _GetXmlFromPureEdgeRequest other = (_GetXmlFromPureEdgeRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.encodingType==null && other.getEncodingType()==null) || 
             (this.encodingType!=null &&
              this.encodingType.equals(other.getEncodingType()))) &&
            ((this.organizationID==null && other.getOrganizationID()==null) || 
             (this.organizationID!=null &&
              this.organizationID.equals(other.getOrganizationID())));
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
        if (getEncodingType() != null) {
            _hashCode += getEncodingType().hashCode();
        }
        if (getOrganizationID() != null) {
            _hashCode += getOrganizationID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetXmlFromPureEdgeRequest.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">GetXmlFromPureEdgeRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encodingType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "encodingType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "EncodingTypeEnum"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organizationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", "OrganizationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/IntegrationServices-V1.0", ">OrganizationID"));
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
