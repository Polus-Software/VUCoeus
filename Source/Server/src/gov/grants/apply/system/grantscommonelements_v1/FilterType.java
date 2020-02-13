
package gov.grants.apply.system.grantscommonelements_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FilterType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FilterType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Status"/>
 *     &lt;enumeration value="OpportunityID"/>
 *     &lt;enumeration value="CFDANumber"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FilterType")
@XmlEnum
public enum FilterType {

    @XmlEnumValue("Status")
    STATUS("Status"),
    @XmlEnumValue("OpportunityID")
    OPPORTUNITY_ID("OpportunityID"),
    @XmlEnumValue("CFDANumber")
    CFDA_NUMBER("CFDANumber");
    private final String value;

    FilterType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FilterType fromValue(String v) {
        for (FilterType c: FilterType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
