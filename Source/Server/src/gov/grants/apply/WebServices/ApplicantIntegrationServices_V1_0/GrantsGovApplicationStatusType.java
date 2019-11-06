/**
 * GrantsGovApplicationStatusType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class GrantsGovApplicationStatusType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected GrantsGovApplicationStatusType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _value1 = "Receiving";
    public static final java.lang.String _value2 = "Received";
    public static final java.lang.String _value3 = "Processing";
    public static final java.lang.String _value4 = "Validated";
    public static final java.lang.String _value5 = "Rejected with Errors";
    public static final java.lang.String _value6 = "Download Preparation";
    public static final java.lang.String _value7 = "Received by Agency";
    public static final java.lang.String _value8 = "Agency Tracking Number Assigned";
    public static final GrantsGovApplicationStatusType value1 = new GrantsGovApplicationStatusType(_value1);
    public static final GrantsGovApplicationStatusType value2 = new GrantsGovApplicationStatusType(_value2);
    public static final GrantsGovApplicationStatusType value3 = new GrantsGovApplicationStatusType(_value3);
    public static final GrantsGovApplicationStatusType value4 = new GrantsGovApplicationStatusType(_value4);
    public static final GrantsGovApplicationStatusType value5 = new GrantsGovApplicationStatusType(_value5);
    public static final GrantsGovApplicationStatusType value6 = new GrantsGovApplicationStatusType(_value6);
    public static final GrantsGovApplicationStatusType value7 = new GrantsGovApplicationStatusType(_value7);
    public static final GrantsGovApplicationStatusType value8 = new GrantsGovApplicationStatusType(_value8);
    public java.lang.String getValue() { return _value_;}
    public static GrantsGovApplicationStatusType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        GrantsGovApplicationStatusType enums = (GrantsGovApplicationStatusType)
            _table_.get(value);
        if (enums==null) throw new java.lang.IllegalStateException();
        return enums;
    }
    public static GrantsGovApplicationStatusType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
