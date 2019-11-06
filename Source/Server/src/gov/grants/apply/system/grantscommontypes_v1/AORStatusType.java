
package gov.grants.apply.system.grantscommontypes_v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AORStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AORStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AUTHORIZED"/>
 *     &lt;enumeration value="UNAUTHORIZED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AORStatusType", namespace = "http://apply.grants.gov/system/GrantsCommonTypes-V1.0")
@XmlEnum
public enum AORStatusType {

    AUTHORIZED,
    UNAUTHORIZED;

    public String value() {
        return name();
    }

    public static AORStatusType fromValue(String v) {
        return valueOf(v);
    }

}
