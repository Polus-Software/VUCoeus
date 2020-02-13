
package gov.grants.apply.system.applicantcommonelements_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmissionFilterType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubmissionFilterType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FundingOpportunityNumber"/>
 *     &lt;enumeration value="GrantsGovTrackingNumber"/>
 *     &lt;enumeration value="PackageID"/>
 *     &lt;enumeration value="SubmissionTitle"/>
 *     &lt;enumeration value="Status"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubmissionFilterType")
@XmlEnum
public enum SubmissionFilterType {

    @XmlEnumValue("FundingOpportunityNumber")
    FUNDING_OPPORTUNITY_NUMBER("FundingOpportunityNumber"),
    @XmlEnumValue("GrantsGovTrackingNumber")
    GRANTS_GOV_TRACKING_NUMBER("GrantsGovTrackingNumber"),
    @XmlEnumValue("PackageID")
    PACKAGE_ID("PackageID"),
    @XmlEnumValue("SubmissionTitle")
    SUBMISSION_TITLE("SubmissionTitle"),
    @XmlEnumValue("Status")
    STATUS("Status");
    private final String value;

    SubmissionFilterType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SubmissionFilterType fromValue(String v) {
        for (SubmissionFilterType c: SubmissionFilterType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
