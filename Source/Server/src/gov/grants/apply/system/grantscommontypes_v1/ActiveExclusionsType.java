
package gov.grants.apply.system.grantscommontypes_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActiveExclusionsType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActiveExclusionsType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Yes"/>
 *     &lt;enumeration value="No"/>
 *     &lt;enumeration value="Not Available"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActiveExclusionsType", namespace = "http://apply.grants.gov/system/GrantsCommonTypes-V1.0")
@XmlEnum
public enum ActiveExclusionsType {

    @XmlEnumValue("Yes")
    YES("Yes"),
    @XmlEnumValue("No")
    NO("No"),
    @XmlEnumValue("Not Available")
    NOT_AVAILABLE("Not Available");
    private final String value;

    ActiveExclusionsType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ActiveExclusionsType fromValue(String v) {
        for (ActiveExclusionsType c: ActiveExclusionsType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
