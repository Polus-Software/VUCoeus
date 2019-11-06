/**
 * _GetApplicationListRequest_ApplicationFilter_Filter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0;

public class _GetApplicationListRequest_ApplicationFilter_Filter implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected _GetApplicationListRequest_ApplicationFilter_Filter(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Status = "Status";
    public static final java.lang.String _OpportunityID = "OpportunityID";
    public static final java.lang.String _CFDANumber = "CFDANumber";
    public static final java.lang.String _SubmissionTitle = "SubmissionTitle";
    public static final _GetApplicationListRequest_ApplicationFilter_Filter Status = new _GetApplicationListRequest_ApplicationFilter_Filter(_Status);
    public static final _GetApplicationListRequest_ApplicationFilter_Filter OpportunityID = new _GetApplicationListRequest_ApplicationFilter_Filter(_OpportunityID);
    public static final _GetApplicationListRequest_ApplicationFilter_Filter CFDANumber = new _GetApplicationListRequest_ApplicationFilter_Filter(_CFDANumber);
    public static final _GetApplicationListRequest_ApplicationFilter_Filter SubmissionTitle = new _GetApplicationListRequest_ApplicationFilter_Filter(_SubmissionTitle);
    public java.lang.String getValue() { return _value_;}
    public static _GetApplicationListRequest_ApplicationFilter_Filter fromValue(java.lang.String value)
          throws java.lang.IllegalStateException {
        _GetApplicationListRequest_ApplicationFilter_Filter enums = (_GetApplicationListRequest_ApplicationFilter_Filter)
            _table_.get(value);
        if (enums==null) throw new java.lang.IllegalStateException();
        return enums;
    }
    public static _GetApplicationListRequest_ApplicationFilter_Filter fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
