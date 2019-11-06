/**
 * _GetOpportunityListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetOpportunityListResponse  implements java.io.Serializable {
    private gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType[] opportunityInformation;

    public _GetOpportunityListResponse() {
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType[] getOpportunityInformation() {
        return opportunityInformation;
    }

    public void setOpportunityInformation(gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType[] opportunityInformation) {
        this.opportunityInformation = opportunityInformation;
    }

    public gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType getOpportunityInformation(int i) {
        return opportunityInformation[i];
    }

    public void setOpportunityInformation(int i, gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.OpportunityInformationType value) {
        this.opportunityInformation[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetOpportunityListResponse)) return false;
        _GetOpportunityListResponse other = (_GetOpportunityListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.opportunityInformation==null && other.getOpportunityInformation()==null) || 
             (this.opportunityInformation!=null &&
              java.util.Arrays.equals(this.opportunityInformation, other.getOpportunityInformation())));
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
        if (getOpportunityInformation() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOpportunityInformation());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOpportunityInformation(), i);
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
        new org.apache.axis.description.TypeDesc(_GetOpportunityListResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", ">GetOpportunityListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opportunityInformation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityInformation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityInformationType"));
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
