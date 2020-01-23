
package gov.grants.apply.system.grantscommontypes_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmissionMethodType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubmissionMethodType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PDF"/>
 *     &lt;enumeration value="Workspace"/>
 *     &lt;enumeration value="S2S"/>
 *     &lt;enumeration value="Third Party S2S"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubmissionMethodType", namespace = "http://apply.grants.gov/system/GrantsCommonTypes-V1.0")
@XmlEnum
public enum SubmissionMethodType {

    PDF("PDF"),
    @XmlEnumValue("Workspace")
    WORKSPACE("Workspace"),
    @XmlEnumValue("S2S")
    S_2_S("S2S"),
    @XmlEnumValue("Third Party S2S")
    THIRD_PARTY_S_2_S("Third Party S2S");
    private final String value;

    SubmissionMethodType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SubmissionMethodType fromValue(String v) {
        for (SubmissionMethodType c: SubmissionMethodType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
