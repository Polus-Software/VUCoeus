
package gov.grants.apply.system.grantscommontypes_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OpportunityCategoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OpportunityCategoryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="D"/>
 *     &lt;enumeration value="M"/>
 *     &lt;enumeration value="C"/>
 *     &lt;enumeration value="E"/>
 *     &lt;enumeration value="O"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "OpportunityCategoryType", namespace = "http://apply.grants.gov/system/GrantsCommonTypes-V1.0")
@XmlEnum
public enum OpportunityCategoryType {

    D,
    M,
    C,
    E,
    O;

    public String value() {
        return name();
    }

    public static OpportunityCategoryType fromValue(String v) {
        return valueOf(v);
    }

}
