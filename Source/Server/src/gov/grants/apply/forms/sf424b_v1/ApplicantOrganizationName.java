//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424b_v1;


/**
 * Java content class for ApplicantOrganizationName element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424B-V1.0.xsd line 20)
 * <p>
 * <pre>
 * &lt;element name="ApplicantOrganizationName" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max60Type"/>
 * </pre>
 * 
 */
public interface ApplicantOrganizationName
    extends javax.xml.bind.Element
{


    /**
     * String - Min length 1, max length 60
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getValue();

    /**
     * String - Min length 1, max length 60
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setValue(java.lang.String value);

}
