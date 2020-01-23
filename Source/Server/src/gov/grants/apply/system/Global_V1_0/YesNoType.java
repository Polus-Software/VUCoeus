/**
 * YesNoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.system.Global_V1_0;

public class YesNoType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected YesNoType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Y = "Y";
    public static final java.lang.String _N = "N";
    public static final YesNoType Y = new YesNoType(_Y);
    public static final YesNoType N = new YesNoType(_N);
    public java.lang.String getValue() { return _value_;}
    public static YesNoType fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        YesNoType enums = (YesNoType)
            _table_.get(value);
        if (enums==null) throw new java.lang.IllegalStateException();
        return enums;
    }
    public static YesNoType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
