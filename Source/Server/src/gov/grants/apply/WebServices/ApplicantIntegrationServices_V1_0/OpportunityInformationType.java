/**
 * OpportunityInformationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class OpportunityInformationType  implements java.io.Serializable {
    private gov.grants.apply.system.Global_V1_0.StringMin1Max100Type opportunityID;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max255Type opportunityTitle;
    private java.util.Date openingDate;
    private java.util.Date closingDate;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max15Type CFDANumber;
    private gov.grants.apply.system.Global_V1_0.StringMin1Max100Type competitionID;
    private java.lang.String schemaURL;
    private java.lang.String instructionURL;

    public OpportunityInformationType() {
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max100Type getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(gov.grants.apply.system.Global_V1_0.StringMin1Max100Type opportunityID) {
        this.opportunityID = opportunityID;
    }

    public gov.grants.apply.system.Global_V1_0.StringMin1Max255Type getOpportunityTitle() {
        return opportunityTitle;
    }

    public void setOpportunityTitle(gov.grants.apply.system.Global_V1_0.StringMin1Max255Type opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
    }

    public java.util.Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(java.util.Date openingDate) {
        this.openingDate = openingDate;
    }

    public java.util.Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(java.util.Date closingDate) {
        this.closingDate = closingDate;
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

    public java.lang.String getSchemaURL() {
        return schemaURL;
    }

    public void setSchemaURL(java.lang.String schemaURL) {
        this.schemaURL = schemaURL;
    }

    public java.lang.String getInstructionURL() {
        return instructionURL;
    }

    public void setInstructionURL(java.lang.String instructionURL) {
        this.instructionURL = instructionURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OpportunityInformationType)) return false;
        OpportunityInformationType other = (OpportunityInformationType) obj;
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
            ((this.opportunityTitle==null && other.getOpportunityTitle()==null) || 
             (this.opportunityTitle!=null &&
              this.opportunityTitle.equals(other.getOpportunityTitle()))) &&
            ((this.openingDate==null && other.getOpeningDate()==null) || 
             (this.openingDate!=null &&
              this.openingDate.equals(other.getOpeningDate()))) &&
            ((this.closingDate==null && other.getClosingDate()==null) || 
             (this.closingDate!=null &&
              this.closingDate.equals(other.getClosingDate()))) &&
            ((this.CFDANumber==null && other.getCFDANumber()==null) || 
             (this.CFDANumber!=null &&
              this.CFDANumber.equals(other.getCFDANumber()))) &&
            ((this.competitionID==null && other.getCompetitionID()==null) || 
             (this.competitionID!=null &&
              this.competitionID.equals(other.getCompetitionID()))) &&
            ((this.schemaURL==null && other.getSchemaURL()==null) || 
             (this.schemaURL!=null &&
              this.schemaURL.equals(other.getSchemaURL()))) &&
            ((this.instructionURL==null && other.getInstructionURL()==null) || 
             (this.instructionURL!=null &&
              this.instructionURL.equals(other.getInstructionURL())));
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
        if (getOpportunityTitle() != null) {
            _hashCode += getOpportunityTitle().hashCode();
        }
        if (getOpeningDate() != null) {
            _hashCode += getOpeningDate().hashCode();
        }
        if (getClosingDate() != null) {
            _hashCode += getClosingDate().hashCode();
        }
        if (getCFDANumber() != null) {
            _hashCode += getCFDANumber().hashCode();
        }
        if (getCompetitionID() != null) {
            _hashCode += getCompetitionID().hashCode();
        }
        if (getSchemaURL() != null) {
            _hashCode += getSchemaURL().hashCode();
        }
        if (getInstructionURL() != null) {
            _hashCode += getInstructionURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(OpportunityInformationType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityInformationType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opportunityID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max100Type"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("opportunityTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpportunityTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "StringMin1Max255Type"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "OpeningDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("closingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "ClosingDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schemaURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "SchemaURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instructionURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://apply.grants.gov/WebServices/ApplicantIntegrationServices-V1.0", "InstructionURL"));
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
