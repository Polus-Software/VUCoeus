
package gov.grants.apply.system.grantscommontypes_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for YesNoType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="YesNoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "YesNoType", namespace = "http://apply.grants.gov/system/GrantsCommonTypes-V1.0")
@XmlEnum
public enum YesNoType {

    Y,
    N;

    public String value() {
        return name();
    }

    public static YesNoType fromValue(String v) {
        return valueOf(v);
    }

}
