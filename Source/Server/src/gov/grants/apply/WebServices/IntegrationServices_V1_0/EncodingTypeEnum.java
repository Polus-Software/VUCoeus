/**
 * EncodingTypeEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.IntegrationServices_V1_0;

public class EncodingTypeEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EncodingTypeEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _MIME = "MIME";
    public static final java.lang.String _DIME = "DIME";
    public static final EncodingTypeEnum MIME = new EncodingTypeEnum(_MIME);
    public static final EncodingTypeEnum DIME = new EncodingTypeEnum(_DIME);
    public java.lang.String getValue() { return _value_;}
    public static EncodingTypeEnum fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        EncodingTypeEnum enums = (EncodingTypeEnum)
            _table_.get(value);
        if (enums==null) throw new java.lang.IllegalStateException();
        return enums;
    }
    public static EncodingTypeEnum fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
