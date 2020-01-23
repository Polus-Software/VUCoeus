
package gov.grants.apply.system.grantscommontypes_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExpandedApplicationFilterType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExpandedApplicationFilterType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Status"/>
 *     &lt;enumeration value="FundingOpportunityNumber"/>
 *     &lt;enumeration value="CFDANumber"/>
 *     &lt;enumeration value="SubmissionTitle"/>
 *     &lt;enumeration value="GrantsGovTrackingNumber"/>
 *     &lt;enumeration value="OpportunityID"/>
 *     &lt;enumeration value="AgencyCode"/>
 *     &lt;enumeration value="CompetitionID"/>
 *     &lt;enumeration value="PackageID"/>
 *     &lt;enumeration value="SubmissionMethod"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExpandedApplicationFilterType", namespace = "http://apply.grants.gov/system/GrantsCommonTypes-V1.0")
@XmlEnum
public enum ExpandedApplicationFilterType {

    @XmlEnumValue("Status")
    STATUS("Status"),
    @XmlEnumValue("FundingOpportunityNumber")
    FUNDING_OPPORTUNITY_NUMBER("FundingOpportunityNumber"),
    @XmlEnumValue("CFDANumber")
    CFDA_NUMBER("CFDANumber"),
    @XmlEnumValue("SubmissionTitle")
    SUBMISSION_TITLE("SubmissionTitle"),
    @XmlEnumValue("GrantsGovTrackingNumber")
    GRANTS_GOV_TRACKING_NUMBER("GrantsGovTrackingNumber"),
    @XmlEnumValue("OpportunityID")
    OPPORTUNITY_ID("OpportunityID"),
    @XmlEnumValue("AgencyCode")
    AGENCY_CODE("AgencyCode"),
    @XmlEnumValue("CompetitionID")
    COMPETITION_ID("CompetitionID"),
    @XmlEnumValue("PackageID")
    PACKAGE_ID("PackageID"),
    @XmlEnumValue("SubmissionMethod")
    SUBMISSION_METHOD("SubmissionMethod");
    private final String value;

    ExpandedApplicationFilterType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExpandedApplicationFilterType fromValue(String v) {
        for (ExpandedApplicationFilterType c: ExpandedApplicationFilterType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
